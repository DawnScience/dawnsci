/*-
 *******************************************************************************
 * Copyright (c) 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial version 'AbstractStreamer' on which this class is based
 *    Matthew Taylor  - modified to be non-caching, misses sleeps to catch-up to source, and only processing the data when it's requested
 *******************************************************************************/
package org.eclipse.dawnsci.remotedataset.client.streamer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.dawnsci.remotedataset.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractNonCachingStreamer<T> implements IStreamer<T>, Runnable {
		
	private static final Logger logger = LoggerFactory.getLogger(AbstractNonCachingStreamer.class);

	private BlockingQueue<byte[]> queue;
	private InputStream      in;
	private long             sleepTime;
	private long             receivedImages = 0;
	private boolean          isFinished;
	
	private String             delimiter;

	/**
	 * Initialises the connection
	 * @param url
	 * @param sleepTime
	 * @return
	 * @throws Exception
	 */
	protected void init(URL url, long sleepTime) throws Exception {
		String contentType = "";
		InputStream inpStream = null;

		HttpEntity ent = getConnection(url);
		if (ent != null) {
			inpStream = ent.getContent();
			try {
				contentType = ent.getContentType().getValue();
			} catch (Exception e) {
				logger.error("Exception in getConnection({}).getContentType().getValue()", url, e);
				contentType = null;
			}
		}

		if (inpStream == null || contentType == null) {
			String noStream = inpStream == null ? "No stream" : "";
			String noContent = contentType == null ? "Content type is empty" : "";
			throw new Exception("Problem connecting to " + url.toString() + " : " + noStream + " " + noContent);
		}

        if (!contentType.startsWith(Constants.MCONTENT_TYPE)) throw new Exception("getImages() may only be used with "+Constants.MCONTENT_TYPE+", not "+contentType);

        this.delimiter  = contentType.split("boundary=")[1];
        // Delimiter in the stream should start with -- but delimiter in content type also sometimes includes it.
        if (!delimiter.startsWith("--")) {
        	delimiter = "--"+delimiter;
        }
		this.queue      = new LinkedBlockingQueue<byte[]>(1);
		this.in         = new BufferedInputStream(inpStream);
		this.sleepTime  = sleepTime;
	}

	/**
	 * Connect to URL using using Apache {@link HttpClient}.
	 * Basic authentication is used if username and password information are present in the URL (expected format is : 
	 * {@code http://<username>:<password>@<ip address>}).
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private HttpEntity getConnection(URL url) throws IOException {
		logger.info("Trying to connect to {}", url.toString());
		if (url.getUserInfo() != null) {
			logger.info("Setting username and password : {}", url.getUserInfo());

			// Get username and password from the url
			String[] userPasswd = url.getUserInfo().split(":");

			// Setup credentials for authenticating connection
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userPasswd[0], userPasswd[1]));
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(new HttpGet(url.toString()));
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			logger.warn("Possible problem with connection - {} (code = {})", status.getReasonPhrase(),
					status.getStatusCode());
		} else {
			logger.info("Connected ok");
		}
		return response.getEntity();
	}
	
	/**
	 * Runs until finished or the stream is closed, continuously parsing the data in the stream to form an image
	 */
	public void run() {
		
		isFinished = false;
		try {
			final StringBuilder buf = new StringBuilder();

			int c       = -1;
			boolean foundImage = false;
			int bytesAvailableAfterLastSleep = in.available();
			int bytesReadSinceLastSleep = 0;
			
			while(!isFinished && (c=in.read())> -1 ) {

				bytesReadSinceLastSleep++;
				buf.append((char)c);
				if (buf.length()>0 && buf.charAt(buf.length()-1)  == '\n') { // Line found
					
					final String line = buf.toString().trim();
					if (line.startsWith(delimiter)) { // We found a new image
						foundImage = true;
					}
					if (foundImage && line.startsWith("Content-Length: ")) {
						int clength = Integer.parseInt(line.split("\\:")[1].trim());
						readImage(in, clength);
						if (isFinished) return;
						
						foundImage = false;
						bytesReadSinceLastSleep+=clength;
						
						// We don't want to use all the CPU so sleep unless there's a lot more data in the buffer we haven't read since the last sleep.
						// We don't want the buffer to build up too far ahead, so we don't sleep in order to catch up
						if (bytesReadSinceLastSleep >= bytesAvailableAfterLastSleep) {
							Thread.sleep(sleepTime);
							bytesAvailableAfterLastSleep = in.available();
							bytesReadSinceLastSleep = 0;
						}
					}
					
					buf.delete(0, buf.length());
					continue;
				}
			}
			
		} catch (Exception ne) {
			setFinished(true);
			logger.error("Cannot read input stream in "+getClass().getSimpleName(), ne);
			
		} finally {
			setFinished(true);
			try {
				in.close();
			} catch (Exception ne) {
				logger.error("Cannot close connection!", ne);
			}
			// Cannot have null, instead add tiny empty image
			queue.clear();
			queue.offer(new byte[]{});
		}
	}

	/**
	 * Reads an image from the stream, populating the array with the latest image bytes
	 * @param in
	 * @param clength
	 * @throws Exception
	 */
	private void readImage(InputStream in, int clength) throws Exception {
		
		int c= -1;
		// Scoot down until no more new lines (this loses first character of JPG)
		while((c=in.read())> -1) {
			if (c=='\r') continue;
			if (c=='\n') continue;
			break;
		}
			
		byte[] imageBytes = new byte[clength + 1];

		imageBytes[0] = (byte)c; // We took one
		int offset    = 1;
		int numRead   = 0;
		while (!isFinished && offset < imageBytes.length && (numRead=in.read(imageBytes, offset, imageBytes.length-offset)) >= 0) {
			offset += numRead;
		}       

		if (isFinished) return;
		queue.clear();
		queue.offer(imageBytes);
	}

	/**
	 * Implement to turn the raw stream bytes into type T
	 * @param bais
	 * @return
	 * @throws Exception
	 */
	protected abstract T getFromStream(ByteArrayInputStream bais) throws Exception;


	/**
	 * Blocks until image added, after that will take the latest data. Once null is added, we are done.
	 * @return Image or null when finished.
	 * 
	 * @throws InterruptedException
	 */
	public T take() throws InterruptedException {
		byte[] latestBytes = queue.take(); // Might get interrupted
		ByteArrayInputStream bais = new ByteArrayInputStream(latestBytes);	
		T bi = null;
		try {
			bi = getFromStream(bais);
			if (isFinished || bi == getQueueEndObject()) {
				setFinished(true);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		receivedImages++;
		return bi;
	}

	/**
	 * Implement to designate an object of tpye T as the end of queue object
	 * @return
	 */
	protected abstract T getQueueEndObject();

	/**
	 * Gets the dropped image count. Note, this does not apply to this streamer, so will return 0
	 */
	public long getDroppedImageCount() {
		return 0;
	}
	
	/**
	 * Gets the received image count
	 */
	public long getReceivedImageCount() {
		return receivedImages;
	}

	/**
	 * Starts the thread running
	 */
	public void start() {
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(true);
		thread.setName("MJPG Streamer");
		thread.start();
	}

	/**
	 * Call to tell the streamer to stop adding images to its queue.
	 * @param b
	 */
	public void setFinished(boolean b) {
		this.isFinished = b;
	}


}