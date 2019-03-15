package org.eclipse.dawnsci.remotedataset.test.client;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.api.io.ILoaderService;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.remotedataset.client.MapToTreeUtils;
import org.eclipse.dawnsci.remotedataset.client.RemoteLoaderWithLazyDataset;
import org.eclipse.dawnsci.remotedataset.server.info.TreeToMapUtils;
import org.eclipse.january.dataset.ILazyDataset;
import org.junit.Test;

import uk.ac.diamond.scisoft.analysis.io.LoaderServiceImpl;

public class RemoteDataHolderTest {

	@Test
	public void test() throws Exception {
		String path = "./testfiles/38323_processed.nxs";
		
		ILoaderService ls = new LoaderServiceImpl();
		
		IDataHolder dh = ls.getData(path,true, null);
	
		Tree tree = dh.getTree();
		Map<String, RemoteLoaderWithLazyDataset> nameLoaderMappings = new HashMap<>();
		Map<String, Object> treeToMap = TreeToMapUtils.treeToMap(tree);
		MapToTreeUtils.mapToTree(treeToMap, path, "", 1234, nameLoaderMappings);

		Map<String, ILazyDataset> output = new HashMap<>();
		
		for (Entry<String,RemoteLoaderWithLazyDataset> e : nameLoaderMappings.entrySet()) {
			output.put(e.getKey(), e.getValue().getLazy());
		}
		
		Map<String, int[]> dataShapes = new HashMap<String,int[]> (dh.getMetadata().getDataShapes());
		
		for (Entry<String, ILazyDataset> e : output.entrySet()) {
			
			if (!dataShapes.containsKey(e.getKey())) {
				fail("Does not contain " + e.getKey());
			} else {
				
				int[] shape = e.getValue().getShape();
				assertArrayEquals(dataShapes.get(e.getKey()), shape);
				dataShapes.remove(e.getKey());
			}
			
		}
		
		assertTrue(dataShapes.isEmpty());
		
	}
}
