package org.eclipse.dawnsci.remotedataset.client;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.analysis.api.tree.TreeFile;
import org.eclipse.dawnsci.analysis.tree.TreeFactory;
import org.eclipse.dawnsci.remotedataset.RemoteDatasetKeys;
import org.eclipse.january.dataset.DTypeUtils;
import org.eclipse.january.dataset.LazyDynamicDataset;

public class MapToTreeUtils {
	
	private MapToTreeUtils() {}

	/**
	 * Used to build a Tree from the map extracted from the remote data server tree get request
	 * 
	 * The map is modified, so make a copy if you wish to make more than one Tree (not recommended)
	 * 
	 * @param map
	 * @param fileName
	 * @param host
	 * @param port
	 * @param loaderMap
	 * @return tree
	 */
	public static Tree mapToTree(Map<String, Object> map, String fileName, String host, int port, Map<String, RemoteLoaderWithLazyDataset> loaderMap) {
		
		URLBuilder urlBuilder = new URLBuilder(host, port);
		urlBuilder.setPath(fileName);
		
		TreeFile tf = TreeFactory.createTreeFile(0, fileName);
		
		String root = map.keySet().iterator().next();
		
		Object o = map.get(root);
		
		if (o instanceof Map) {
			@SuppressWarnings("unchecked")
			Node n = parseMap((Map<String, Object>)o, "",  urlBuilder, loaderMap);
			if (n instanceof GroupNode) {
				tf.setGroupNode((GroupNode)n);
				return tf;
			}
		}
		
		return null;
		
	}
	
	private static Node parseMap(Map<String, Object> map, String nodeName, URLBuilder urlBuilder, Map<String, RemoteLoaderWithLazyDataset> loaderMap) {
		
		Node parent = null;
		
		if (map.containsKey(RemoteDatasetKeys.DATANODE.getID())) {
			DataNode dataNode = TreeFactory.createDataNode(0);
			parent = dataNode;
			String name = null;
			int[] shape = null;
			Integer elements = null;
			Integer dtype = null;
			long[] maxShape = null;
			long[] chunkShape = null;
			
			Object object = map.remove(RemoteDatasetKeys.NAME.getID());
			if (object instanceof String) name = (String)object;
			object = map.remove(RemoteDatasetKeys.SHAPE.getID());
			if (object instanceof int[]) shape = (int[])object;
			object = map.remove(RemoteDatasetKeys.ELEMENTS_PER_ITEM.getID());
			if (object instanceof Integer) elements = (Integer)object;
			object = map.remove(RemoteDatasetKeys.DTYPE.getID());
			if (object instanceof Integer) dtype = (Integer)object;
			object = map.remove(RemoteDatasetKeys.MAXSHAPE.getID());
			if (object instanceof long[]) maxShape = (long[])object;
			object = map.remove(RemoteDatasetKeys.CHUNK.getID());
			if (object instanceof long[]) chunkShape = (long[])object;
			
			if (name == null || shape == null || elements == null || dtype == null || maxShape == null) {
				//log error, add no dataset
			} else {
				URLBuilder b = new URLBuilder(urlBuilder);
				b.setDataset(nodeName);
				ShapeSetRemoteLoader loader = new ShapeSetRemoteLoader(b, shape);
				
				LazyDynamicDataset lazy = new LazyDynamicDataset(loader, name, elements.intValue(),
						DTypeUtils.getInterface(dtype.intValue()), shape,
						Arrays.stream(maxShape).mapToInt(l -> (int) l).toArray());
				
				dataNode.setDataset(lazy);
				dataNode.setChunkShape(chunkShape);
				dataNode.setMaxShape(maxShape);
				
				loaderMap.put(nodeName,new RemoteLoaderWithLazyDataset(loader, lazy));
			}
			
		} else if (map.containsKey(RemoteDatasetKeys.SYMBOLICNODE.getID())) {
			parent = TreeFactory.createSymbolicNode(0, (URI)null, null, Tree.ROOT);
		}else {
			parent = TreeFactory.createGroupNode(0);
		}
		
		for (String key : map.keySet()) {
			
			String newName = nodeName + Node.SEPARATOR + key;
			
			Object o = map.get(key);
			
			if (o instanceof Map) {
				@SuppressWarnings("unchecked")
				Node gn = parseMap((Map<String, Object>) o,newName, urlBuilder,loaderMap);
				if (parent instanceof GroupNode) ((GroupNode)parent).addNode(key, gn);
			}
			
			if (o instanceof String || o instanceof String[]) {
				parent.addAttribute(TreeFactory.createAttribute(key, o));
			}
			
		}
		
		return parent;
		
		
		
	}
	
}
