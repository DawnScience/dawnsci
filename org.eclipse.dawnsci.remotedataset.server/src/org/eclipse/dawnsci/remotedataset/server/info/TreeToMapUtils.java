package org.eclipse.dawnsci.remotedataset.server.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.SymbolicNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.remotedataset.RemoteDatasetKeys;
import org.eclipse.january.dataset.DTypeUtils;
import org.eclipse.january.dataset.IDataset;
import org.eclipse.january.dataset.ILazyDataset;
import org.eclipse.january.dataset.StringDataset;

public class TreeToMapUtils {

	private TreeToMapUtils() {}

	public static Map<String, Object> treeToMap(Tree tree) {

		Map<String,Object> map = new HashMap<>();

		NodeLink nl = tree.getNodeLink();

		String name = nl.getName();
		map.put(name, parseNodeLink(nl.getDestination()));

		return map;

	}

	private static Object parseNodeLink(Node destination) {
		Map<String,Object> map = new HashMap<>();

		Iterator<? extends Attribute> it = destination.getAttributeIterator();

		while (it.hasNext()) {
			Attribute next = it.next();
			if (next.getSize() == 1) {
				map.put(next.getName(), next.getFirstElement());
			} else {

				IDataset value = next.getValue();

				if (value instanceof StringDataset) {
					map.put(next.getName(), ((StringDataset)value).getData());
				}

			}
		}

		if (destination instanceof GroupNode) {
			GroupNode gn = (GroupNode)destination;
			Iterator<String> nnit = gn.getNodeNameIterator();
			while (nnit.hasNext()){
				String next = nnit.next();
				Node node = gn.getNode(next);
				map.put(next, parseNodeLink(node));
			}
		}

		if (destination instanceof DataNode) {
			DataNode dn = (DataNode)destination;
			if (dn.getDataset() == null) {
				map.put(RemoteDatasetKeys.SYMBOLICNODE.getID(), true);
			} else {
				ILazyDataset lz = dn.getDataset();
				map.put(RemoteDatasetKeys.DATANODE.getID(), true);
				map.put(RemoteDatasetKeys.MAXSHAPE.getID(), dn.getMaxShape());
				map.put(RemoteDatasetKeys.SHAPE.getID(), lz.getShape());
				map.put(RemoteDatasetKeys.NAME.getID(), lz.getName());
				map.put(RemoteDatasetKeys.ELEMENTS_PER_ITEM.getID(), lz.getElementsPerItem());
				map.put(RemoteDatasetKeys.DTYPE.getID(), DTypeUtils.getDTypeFromClass(lz.getElementClass()));
				map.put(RemoteDatasetKeys.CHUNK.getID(), dn.getChunkShape());

			}
		}

		if (destination instanceof SymbolicNode) {
			map.put(RemoteDatasetKeys.SYMBOLICNODE.getID(), true);
		}

		return map;
	}

}
