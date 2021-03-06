/*-
 * Copyright 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.hdf5;

import java.io.File;
import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.io.ScanFileHolderException;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.PositionIterator;
import org.eclipse.january.dataset.ShapeUtils;
import org.eclipse.january.dataset.SliceND;
import org.junit.Test;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;
import hdf.hdf5lib.exceptions.HDF5Exception;
import hdf.hdf5lib.exceptions.HDF5LibraryException;

public class AccessSpeedTest extends TestBase {

	@Test
	public void testReadSpeed() throws HDF5LibraryException, NullPointerException, NexusException, ScanFileHolderException, InterruptedException {
		// read slices from two datasets alternating
		String file = "test-scratch/readspeed.h5";
		long fid = -1;
		try {
			fid = H5.H5Fcreate(file, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception e) {
			e.printStackTrace();
		}
		HDF5File f = new HDF5File(file, fid, true, false);
		int[] shape = new int[] {512, 128, 1024};
		int size = ShapeUtils.calcSize(shape);
		Dataset data1 = DatasetFactory.createRange(size);
		data1.setShape(shape);
		HDF5Utils.writeDataset(f, "data1", data1);

		Dataset data2 = DatasetFactory.createRange(size);
		data1.setShape(shape);
		HDF5Utils.writeDataset(f, "data2", data2);
		H5.H5Fclose(fid);

		PositionIterator iter = new PositionIterator(data1.getShapeRef(), 1, 2);
		int[] start = iter.getPos();
		int[] stop = data1.getShape();
		int[] step = new int[data1.getRank()];
		Arrays.fill(step, 1);
		int[] nshape = data1.getShape();
		nshape[0] = 1;
		long now;

		now = -System.nanoTime();
		while (iter.hasNext()) {
			stop[0] = start[0] + 1;
			data1 = HDF5Utils.loadDatasetWithClose(file, "data1", start, nshape, step, 1, DoubleDataset.class, false);
			data2 = HDF5Utils.loadDatasetWithClose(file, "data2", start, nshape, step, 1, DoubleDataset.class, false);
		}
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		iter.reset();
		now = -System.nanoTime();
		fid = H5.H5Fopen(file, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
		while (iter.hasNext()) {
			stop[0] = start[0] + 1;
			data1 = HDF5Utils.readDataset(f, "data1", start, nshape, step, 1, DoubleDataset.class, false);
			data2 = HDF5Utils.readDataset(f, "data2", start, nshape, step, 1, DoubleDataset.class, false);
		}
		H5.H5Fclose(fid);
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		iter.reset();
		now = -System.nanoTime();
		f = HDF5FileFactory.acquireFile(file, false);
		while (iter.hasNext()) {
			stop[0] = start[0] + 1;
			data1 = HDF5Utils.readDataset(f, "data1", start, nshape, step, 1, DoubleDataset.class, false);
			data2 = HDF5Utils.readDataset(f, "data2", start, nshape, step, 1, DoubleDataset.class, false);
		}
		HDF5FileFactory.releaseFile(file, true);
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		iter.reset();
		now = -System.nanoTime();
		while (iter.hasNext()) {
			stop[0] = start[0] + 1;
			data1 = HDF5Utils.loadDataset(file, "data1", start, nshape, step, 1, DoubleDataset.class, false);
			data2 = HDF5Utils.loadDataset(file, "data2", start, nshape, step, 1, DoubleDataset.class, false);
		}
		HDF5FileFactory.releaseFile(file);
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		Thread.sleep(6000);
	}

	@Test
	public void testWriteSpeed() throws HDF5LibraryException, NullPointerException, NexusException, ScanFileHolderException, InterruptedException {
		// write slices to two datasets alternating
		String file = "test-scratch/writespeed.h5";

		prepareFile(file);
		int[] shape = new int[] {1, 128, 1024};
		int size = ShapeUtils.calcSize(shape);
		Dataset data1 = DatasetFactory.createRange(size);
		data1.setShape(shape);
		Dataset data2 = DatasetFactory.createRange(size);
		data2.setShape(shape);

		shape[0] = 128;
		PositionIterator iter = new PositionIterator(shape, 1, 2);
		int[] pos = iter.getPos();
		long now;

		int[] nshape = shape.clone();
		nshape[0] = 1;
		SliceND slice = new SliceND(nshape);
		int[] start = slice.getStart();
		int[] stop = slice.getStop();

		now = -System.nanoTime();
		while (iter.hasNext()) {
			for (int i = 0; i < 3; i++) {
				start[i] = pos[i];
			}
			stop[0] = start[0] + 1;
			HDF5Utils.setDatasetSliceWithClose(file, "/entry", "data1", slice, data1);
			HDF5Utils.setDatasetSliceWithClose(file, "/entry", "data2", slice, data2);
		}
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		prepareFile(file);
		iter.reset();
		now = -System.nanoTime();
		long fid = H5.H5Fopen(file, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
		HDF5File f = new HDF5File(file, fid, true, false);
		while (iter.hasNext()) {
			for (int i = 0; i < 3; i++) {
				start[i] = pos[i];
			}
			stop[0] = start[0] + 1;
			HDF5Utils.writeDatasetSlice(f, "/entry/data1", slice, data1);
			HDF5Utils.writeDatasetSlice(f, "/entry/data2", slice, data2);
		}
		H5.H5Fclose(fid);
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");

		prepareFile(file);
		iter.reset();
		now = -System.nanoTime();
		while (iter.hasNext()) {
			for (int i = 0; i < 3; i++) {
				start[i] = pos[i];
			}
			stop[0] = start[0] + 1;
			HDF5Utils.setDatasetSlice(file, "/entry", "data1", slice, data1);
			HDF5Utils.setDatasetSlice(file, "/entry", "data2", slice, data2);
		}
		HDF5FileFactory.releaseFile(file);
		now += System.nanoTime();
		System.err.println("Test took " + now*1e-9 + "s");
		Thread.sleep(6000);
	}

	static void prepareFile(String file) throws NexusException, HDF5LibraryException, ScanFileHolderException {
		int[] chunk = new int[] {1, 128, 1024};
		int[] mshape = new int[] {-1, 128, 1024};

		File f = new File(file);
		if (f.exists()) {
			f.delete();
		}
		HDF5Utils.createDatasetWithClose(file, "/entry", "data1", chunk, mshape, chunk, DoubleDataset.class, null, false);
		HDF5Utils.createDatasetWithClose(file, "/entry", "data2", chunk, mshape, chunk, DoubleDataset.class, null, false);
	}

	@Test
	public void testShutdownHook() throws HDF5LibraryException, NullPointerException, NexusException, ScanFileHolderException, InterruptedException {
		String file = "test-scratch/shutdown.h5";
		HDF5File f = HDF5FileFactory.acquireFileAsNew(file);
		int[] shape = new int[] {1024, 128, 1024};
		int size = ShapeUtils.calcSize(shape);
		Dataset data1 = DatasetFactory.createRange(size);
		data1.setShape(shape);
		HDF5Utils.writeDataset(f, "data1", data1);
		HDF5FileFactory.releaseFile(file);
	}
}
