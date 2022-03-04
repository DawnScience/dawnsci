/*-
 *******************************************************************************
 * Copyright (c) 2011, 2015 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.dawnsci.remotedataset.client.streamer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;

import uk.ac.diamond.scisoft.analysis.io.AWTImageUtils;

class MJPGStreamer extends AbstractNonCachingStreamer<BufferedImage> {

	/**
	 * 
	 * @param url - URL to read from
	 * @param sleepTime - time to sleep between image reads, we don't want to use all CPU
	 * @throws Exception
	 */
	public MJPGStreamer(URL url, long sleepTime) throws Exception {
		init(url, sleepTime);
	}
	
	private static BufferedImage QUEUE_END = new BufferedImage(1, 1, 1);
	
	@Override
	protected BufferedImage getQueueEndObject() {
		return QUEUE_END;
	}

	@Override
	protected BufferedImage getFromStream(ByteArrayInputStream bais) throws Exception {
		Iterator<ImageReader> readers = ImageIO.getImageReaders(bais);
		try {
			while (readers.hasNext()) {
				ImageReader rdr = null;
				try {
					rdr = readers.next();
					ImageReadParam its = AWTImageUtils.getRGBParam(rdr, 0);
					try {
						rdr.setInput(bais, true, true);
						if (its == null) { // JPEG exception
							return rdr.read(0);
						}
						try {
							return AWTImageUtils.makeBufferedImage(rdr.readRaster(0, its));
						} catch (UnsupportedOperationException e) {
							return rdr.read(0, its);
						}
					} catch (Exception e) {
					}
				} finally {
					rdr.dispose();
				}
			}
		} finally {
			bais.close();
		}

		return null;
	}
}
