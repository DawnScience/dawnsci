/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.remotedataset.test.utilities.mock;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.eclipse.dawnsci.analysis.api.io.ScanFileHolderException;
import org.eclipse.january.dataset.IDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.io.DataHolder;
import uk.ac.diamond.scisoft.analysis.io.JavaImageLoader;

/**
 * This class loads a TIFF image file
 */
public class MockImageLoader extends JavaImageLoader {

	private static final Logger logger = LoggerFactory.getLogger(MockImageLoader.class);

	protected Map<String, Serializable> metadataMap = null;
	private boolean loadData = true;
	
	public MockImageLoader() {
		this(null, false);
	}
	
	/**
	 * @param FileName
	 */
	public MockImageLoader(String FileName) {
		this(FileName, false);
	}

	/**
	 * @param FileName
	 * @param convertToGrey
	 */
	public MockImageLoader(String FileName, boolean convertToGrey) {
		super(FileName, "tiff", convertToGrey);
	}

	/**
	 * @param FileName
	 * @param convertToGrey
	 * @param keepBitWidth
	 */
	public MockImageLoader(String FileName, boolean convertToGrey, boolean keepBitWidth) {
		super(FileName, "tiff", convertToGrey, keepBitWidth);
	}

	@Override
	public DataHolder loadFile() throws ScanFileHolderException {
		File f = null;

		// Check for file
		f = new File(fileName);
		if (!f.exists()) {
			logger.warn("File, {}, did not exist. Now trying to replace suffix", fileName);
			f = findCorrectSuffix();
		}

		// TODO cope with multiple images (tiff)
		DataHolder output = new MockDataHolder(null, fileName);
		ImageReader reader = null;
		try {
			int count = 0;
			try {
				BufferedImage image = ImageIO.read(f);
				IDataset set = SWTImageUtils.convertToRGBDataset(image);
				set.setName("image");
				output.addDataset("image", set);
			} catch (Exception ne) {
				if (count>10) throw ne;
				count++;
			}
		} catch (IllegalArgumentException e) {
			throw new ScanFileHolderException("IllegalArgumentException interpreting file '" + fileName + "'", e);
		} catch (NullPointerException e) {
			throw new ScanFileHolderException("NullPointerException interpreting file '" + fileName + "'", e);
		} catch (Exception other) {
			System.out.println("> Cannot read "+fileName);
			other.printStackTrace();
			throw new ScanFileHolderException("> Cannot read "+fileName, other);
		} finally {
			if (reader != null)
				reader.dispose();
		}

		if (!loadData) {
			return null;
		}

		return output;
	}

	@Override
	protected void clearMetadata() {
		super.clearMetadata();
		metadataMap.clear();
	}
}
