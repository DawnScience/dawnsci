package org.eclipse.dawnsci.remotedataset.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.io.IRemoteDataHolder;
import org.eclipse.dawnsci.analysis.api.persistence.IMarshallerService;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.IFindInTree;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.SymbolicNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.analysis.api.tree.TreeUtils;
import org.eclipse.dawnsci.remotedataset.XMLMarshallerService;
import org.eclipse.january.dataset.ILazyDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.io.DataHolder;


public class RemoteDataHolder extends DataHolder implements IRemoteDataHolder {

	private static final long serialVersionUID = 8895903503246027735L;

	/**
	 * List containing all the name and data pairs (to be) loaded.
	 */

	private Map<String, RemoteLoaderWithLazyDataset> nameLoaderMappings;

	private URLBuilder urlBuilder;
	
	private static final Logger logger = LoggerFactory.getLogger(RemoteDataHolder.class);
	
	public RemoteDataHolder(String path, String host, int port, boolean failWhenSymbolics) {
		super();
		this.urlBuilder = new URLBuilder(host, port);
		urlBuilder.setPath(path);
		this.setFilePath(path);
		nameLoaderMappings = new HashMap<>();
		
		try {
			this.setTree(MapToTreeUtils.mapToTree(getTreeMap(),path,host,port,nameLoaderMappings));
			
			for (Entry<String,RemoteLoaderWithLazyDataset> e : nameLoaderMappings.entrySet()) {
				this.addDataset(e.getKey(), e.getValue().getLazy());
			}
		} catch (Exception e) {
			// logger
		}
		
		if (failWhenSymbolics) {
			Map<String, NodeLink> symbolics = TreeUtils.treeBreadthFirstSearch(getTree().getGroupNode(), n -> n.getDestination() instanceof SymbolicNode, true, null);
			
			if (!symbolics.isEmpty()) {
				logger.info("Symbolic nodes still exist, {} in total, {} is first",symbolics.size(),symbolics.keySet().iterator().next());
				throw new RuntimeException("Symbolic nodes present in tree");
			}
		}
	}
	
	public static Map<String, ILazyDataset> buildDatasetMapFromTree(Tree tree) {
		IFindInTree datasetFinder = new IFindInTree() {
			
			@Override
			public boolean found(NodeLink node) {
				Node d = node.getDestination();
				return (d instanceof DataNode && ((DataNode)d).getDataset() != null);
			}
		};
		
		Map<String, NodeLink> map = TreeUtils.treeBreadthFirstSearch(tree.getGroupNode(), datasetFinder, false, null);
		
		Map<String, ILazyDataset> output = new HashMap<>();
		
		for (Entry<String, NodeLink> e : map.entrySet()) {
			output.put(Tree.ROOT + e.getKey(), ((DataNode)e.getValue().getDestination()).getDataset());
		}
		
		return output;
		
	}

	private Map<String, Object> getTreeMap() throws Exception {
		
		urlBuilder.setGet(true);
		final URL url = new URL(urlBuilder.getTreeURL());
		URLConnection  conn = url.openConnection();

		final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		try {
			String xml = reader.readLine();
			IMarshallerService marshaller = new XMLMarshallerService();
			return (Map<String, Object>)marshaller.unmarshal(xml, Map.class);

		} finally {
			reader.close();
		}
	}
	
	private Map<String, int[]> getRemoteShapes(){
		
		BufferedReader reader =null;
		
		try {
		urlBuilder.setGet(true);
		final URL url = new URL(urlBuilder.getShapesURL());
		URLConnection  conn = url.openConnection();

		 reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		 String xml = reader.readLine();
			IMarshallerService marshaller = new XMLMarshallerService();
			Map<String, int[]> shapes = (Map<String, int[]>)marshaller.unmarshal(xml, Map.class);
			return shapes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return null;
	}
	
	@Override
	public void update() {
		Map<String, int[]> remoteShapes = getRemoteShapes();
		
		if (remoteShapes == null) {
			logger.debug("Could not get shapes for update");
			return;
		}
		
		for (Entry<String, RemoteLoaderWithLazyDataset> e : nameLoaderMappings.entrySet()) {
			if (remoteShapes.containsKey(e.getKey())) {
				e.getValue().getLoader().setShape(remoteShapes.get(e.getKey()));
			}
		}
	}

	@Override
	public void clear() {
		throw new RuntimeException("Remote Dataholder should not be cleared!");
	}


	@Override
	public void setTree(Tree tree) {
		if (this.getTree() != null) throw new RuntimeException("Cannot overwrite tree in remote data holder!");
		super.setTree(tree);

	}

}
