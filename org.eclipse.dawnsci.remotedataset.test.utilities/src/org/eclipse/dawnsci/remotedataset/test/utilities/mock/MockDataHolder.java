/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.remotedataset.test.utilities.mock;

import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.api.io.IFileLoader;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.analysis.api.tree.TreeFile;
import org.eclipse.dawnsci.analysis.tree.TreeFactory;
import org.eclipse.dawnsci.hdf5.HDF5FileFactory;
import org.eclipse.dawnsci.nexus.INexusFileFactory;
import org.eclipse.dawnsci.nexus.NexusFile;
import org.eclipse.january.dataset.ILazyDataset;
import org.eclipse.january.metadata.IMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.io.DataHolder;

/**
 * This class is to marshal all the data for the purpose of loading from or saving to a file
 * directly or via a ScanFileHolder.
 * <p>
 * This is designed to take in any dataset obeying the IDataset interface but output an
 * object that is a subclass of Dataset - the dataset will be converted if necessary.
 * 
 * This implementation does not permit duplicated names.
 */
public class MockDataHolder extends DataHolder {

	private static final long serialVersionUID = 7555446959963559418L;

	private static final Logger logger = LoggerFactory.getLogger(MockDataHolder.class);

	private INexusFileFactory factory;

	/**
	 * This must create the three objects which will be put into the ScanFileHolder
	 */
	public MockDataHolder(INexusFileFactory factory, String filePath) {
		super();
		this.factory = factory;
		setFilePath(filePath);
	}

	/**
	 * Used to hold metadata
	 * @param meta
	 */
	public MockDataHolder(IMetadata meta) {
		super(meta);
	}

	/**
	 * Does not clone the meta data.
	 * @return shallow copy of DataHolder
	 */
	@Override
	public IDataHolder clone() {
		MockDataHolder ret = new MockDataHolder(factory, getFilePath());
		for (Entry<String, ILazyDataset> e : toLazyMap().entrySet()) {
			ret.addDataset(e.getKey(), e.getValue());
		}
		ret.setMetadata(getMetadata());
		ret.setLoaderClass(getLoaderClass());
		return ret;
	}


	/**
	 * This pulls out the dataset which could be lazy, maintaining its laziness.
	 * @param name
	 * @return Generic dataset with given name or null if it does not exist
	 */
	@Override
	public ILazyDataset getLazyDataset(String name) {
		if (contains(name)) return super.getLazyDataset(name);
		
		try {
			NexusFile file = factory.newNexusFile(getFilePath());
			try {
				file.openToRead();
				final DataNode node = file.getData(name);
				ILazyDataset ds = node.getDataset();
				return ds;
			} finally {
				file.close();
			}
		} catch (Exception ne) {
			throw new RuntimeException(ne);
		}

	}

	/**
	 * @return class
	 */
	@Override
	public Class<? extends IFileLoader> getLoaderClass() {
		return null;
	}

	@Override
	public Tree getTree() {
		Tree lTree = super.getTree();
		if (lTree==null) {
			try {
				try {
					long fid = HDF5FileFactory.acquireFile(getFilePath(), false).getID();
	
					final long oid = getFilePath().hashCode(); // include file name in ID
					TreeFile f = TreeFactory.createTreeFile(oid, getFilePath());
					lTree = f;
				} finally {
					HDF5FileFactory.releaseFile(getFilePath());
				}
			} catch (Exception ne) {
				throw new RuntimeException(ne);
			}
		}
		
		return lTree;
	}
}
