/*-
 *******************************************************************************
 * Copyright (c) 2015 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Peter Chang - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dawnsci.nexus.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyWriteableDataset;
import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetUtils;
import org.eclipse.dawnsci.analysis.dataset.impl.LazyWriteableDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.StringDataset;
import org.eclipse.dawnsci.analysis.tree.TreeFactory;
import org.eclipse.dawnsci.analysis.tree.impl.DataNodeImpl;
import org.eclipse.dawnsci.analysis.tree.impl.GroupNodeImpl;
import org.eclipse.dawnsci.nexus.NXobject;

public abstract class NXobjectImpl extends GroupNodeImpl implements NXobject {

	protected static final long serialVersionUID = GroupNodeImpl.serialVersionUID;

	/**
	 * Name of attribute
	 */
	public static final String NX_CLASS = "NX_class";
	
	/**
	 * Node factory for creating new nodes, so that oids don't clash.
	 */
	private final NexusNodeFactory nodeFactory;

	/**
	 * Creates a new NeXus group node. This constructor is used when
	 * create a new NeXus file
	 * @param nodeFactory
	 */
	protected NXobjectImpl(final NexusNodeFactory nodeFactory) {
		super(nodeFactory.getNextOid());
		this.nodeFactory = nodeFactory;
		createNxClassAttribute();
	}
	
	@Override
	public boolean canAddChild(NXobject nexusObject) {
		return getPermittedChildGroupClasses().contains(nexusObject.getNexusBaseClass());
	}

	/**
	 * Creates a new NeXus group node. This constructor is used when loading
	 * a new NeXus file. No further nodes should be added to a NeXus tree that has
	 * been loaded from disk.
	 * @param oid
	 */
	protected NXobjectImpl(long oid) {
		super(oid);
		this.nodeFactory = null;
		createNxClassAttribute();
	}

	private void createNxClassAttribute() {
		Attribute a = TreeFactory.createAttribute(NX_CLASS);
		String n = getNXclass().getName();
		int i = n.lastIndexOf(".");
		a.setValue(n.substring(i + 1));
		addAttribute(a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N extends NXobject> N getChild(String name, Class<N> nxClass) {
		GroupNode g = getGroupNode(name);
		if (g != null && g instanceof NXobject && ((NXobject) g).getNXclass().equals(nxClass)) {
			return (N) g;
		}
		
		return null;
	}
	
	@Override
	public IDataset getDataset(String name) {
		if (!containsDataNode(name)) {
			return null;
		}
		return getCached(name);
	}
	
	@Override
	public ILazyWriteableDataset getLazyWritableDataset(String name) {
		if (containsDataNode(name)) {
			ILazyDataset dataset = getDataNode(name).getDataset();
			if (dataset instanceof ILazyWriteableDataset) {
				return (ILazyWriteableDataset) dataset;
			}
		}
		
		return null;
	}

	@Override
	public DataNode setDataset(String name, IDataset value) {
		DataNode dataNode;
		if (containsDataNode(name)) {
			dataNode = getDataNode(name);
			dataNode.setDataset(value);
		} else {
			dataNode = createDataNode(name, value);
		}
		// update the cache
		if (value instanceof Dataset) {
			cached.put(name, (Dataset) value);
		} else {
			// if this is a lazy dataset only, only clear the old value
			// the new value will be calculated when required
			cached.remove(name);
		}
		
		return dataNode;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dawnsci.nexus.NXobject#initializeLazyDataset(java.lang.String, int, int)
	 */
	public ILazyWriteableDataset initializeLazyDataset(String name, int rank, int dtype) {
		DataNode dataNode = nodeFactory.createDataNode();
		
		int[] shape = new int[rank];
		Arrays.fill(shape, ILazyWriteableDataset.UNLIMITED);
		
		ILazyWriteableDataset dataset = new LazyWriteableDataset(name, dtype, shape, null, null, null);
		dataNode.setDataset(dataset);
		addDataNode(name, dataNode);
		
		return dataset;
	}

	private DataNode createDataNode(String name, IDataset value) {
		// note that this method should only be used when creating a new NeXus tree
		DataNode dataNode = nodeFactory.createDataNode();
		addDataNode(name, dataNode);
		dataNode.setDataset(value);
		
		return dataNode;
	}

	@SuppressWarnings("unchecked")
	public <N extends NXobject> Map<String, N> getChildren(Class<N> nxClass) {
		Map<String, N> map = new LinkedHashMap<>();
		for (NodeLink n : this) {
			if (n.isDestinationGroup()) {
				GroupNode g = (GroupNode) n.getDestination();
				if (g instanceof NXobject && ((NXobject) g).getNXclass().equals(nxClass)) {
					map.put(n.getName(), (N) g);
				}
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	protected <N extends NXobject> void putChild(String name, N child) {
		if (containsGroupNode(name)) {
			NodeLink n = getNodeLink(name);
			GroupNode g = (GroupNode) n.getDestination();
			Class<?> existingNxClass = g instanceof NXobject ? ((NXobject) g).getNXclass() : null;
			Class<N> nxClass = (Class<N>) child.getNXclass();
			if (existingNxClass != null && !existingNxClass.equals(nxClass)) {
				throw new IllegalArgumentException("There is a group of given name but of a different NX class");
			}
		}

		addGroupNode(name, child);
	}

	@SuppressWarnings("unchecked")
	protected <N extends NXobject> void setChildren(Map<String, N> map) {
		map = new LinkedHashMap<>(map);
		for (NodeLink n : this) {
			if (!n.isDestinationGroup())
				continue;
			if (map.containsKey(n.getName())) {
				N child = map.remove(n.getName());
				GroupNode g = (GroupNode) n.getDestination();
				if (g.getClass().equals(child.getClass())) {
					addGroupNode(n.getName(), child);
					map.put(n.getName(), (N) g);
				}
			}
		}
		for (String n : map.keySet()) {
			N child = map.get(n);
			addGroupNode(n, child);
		}
	}

	public String getString(String name) {
		if (!containsDataNode(name)) {
			return null;
		}
		return getDataNode(name).getString();
	}

	public DataNode setString(String name, String value) {
		DataNode dataNode;
		if (containsDataNode(name)) {
			dataNode = getDataNode(name);
			if (!dataNode.isString()) {
				throw new IllegalArgumentException("Node is not a string");
			}
			dataNode.setString(value);
		} else {
			// create a new dataset, create a new DataNode containing that dataset
			StringDataset dataset = StringDataset.createFromObject(value);
			dataNode = createDataNode(name, dataset);
			// add the new dataset to the cache
			cached.put(name, dataset);
		}
		
		return dataNode;
	}

	protected Map<String, Dataset> getAllDatasets(String attrName) {
		Map<String, Dataset> map = new LinkedHashMap<>();
		
		for (NodeLink n : this) {
			if (!n.isDestinationData())
				continue;
			if (attrName != null) {
				Node d = n.getDestination();
				if (!d.containsAttribute(attrName))
					continue;
			}
			map.put(n.getName(), getCached(n.getName()));
		}
		return map;
	}

	private static final int CACHE_LIMIT = 1024;

	private Map<String, Dataset> cached = new HashMap<>();

	private Dataset getCached(String name) {
		if (!cached.containsKey(name)) {
			DataNodeImpl dataNode = getDataNode(name);
			if (dataNode != null) {
				ILazyDataset lazy = dataNode.getDataset();
				if (!(lazy instanceof IDataset)) {
					// if this is a lazy dataset, set the slice on it
					int size = lazy.getSize();
					if (size > CACHE_LIMIT) {
						// cannot return a Dataset if the size is too large
						throw new IllegalStateException("Dataset is too large to cache. This method should only be used for small datasets.");
					} else {
						lazy = lazy.getSlice();
					}
				}
				cached.put(name, DatasetUtils.convertToDataset((IDataset) lazy));
			}
		}
		return cached.get(name);
	}

	public boolean getBoolean(String name) {
		Dataset d = getCached(name);
		return d.getElementBooleanAbs(0);
	}

	public long getLong(String name) {
		Dataset d = getCached(name);
		return d.getElementLongAbs(0);
	}

	public double getDouble(String name) {
		Dataset d = getCached(name);
		return d.getElementDoubleAbs(0);
	}

	public Number getNumber(String name) {
		Dataset d = getCached(name);
		if (d.hasFloatingPointElements())
			return d.getElementDoubleAbs(0);
		return d.getElementLongAbs(0);
	}

	public Date getDate(String name) {
		try {
			return DateFormat.getDateTimeInstance().parse(getString(name));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Set the value of the given field to the given value. The
	 * value may be an atomic value (e.g. primitive wrapper, object or string),
	 * or a dataset.
	 * @param name name
	 * @param value value
	 */
	public DataNode setField(String name, Object value) {
		final DataNode dataNode;
		if (containsDataNode(name)) {
			dataNode = getDataNode(name);
			// create a new dataset, new DataNode and update the cache
			Dataset dataset = getCached(name);
			dataset.setObjectAbs(0, value);
		} else {
			Dataset dataset = DatasetFactory.createFromObject(value);
			dataNode = createDataNode(name, dataset);
			cached.put(name, dataset);
		}
		
		return dataNode;
	}

	protected DataNode setDate(String name, Date date) {
		return setString(name, DateFormat.getDateTimeInstance().format(date));
	}

	private Node getNode(String name) {
		final NodeLink link = getNodeLink(name);
		if (link == null) {
			throw new IllegalArgumentException("Node not in group");
		}
		return link.getDestination();
	}

	private static String makeAttributeKey(String name, String attrName) {
		return name == null ? ATTRIBUTE + attrName : name + ATTRIBUTE + attrName;
	}

	/**
	 * Set the value of the given attribute. If the first argument is not <code>null</code>
	 * then the attribute is set on the field or child group with this name
	 * @param name name of node (if null then current group)
	 * @param attrName
	 * @param attrValue
	 */
	public void setAttribute(String name, String attrName, Object attrValue) {
		Node node = name == null ? this : getNode(name);
		Attribute a = node.containsAttribute(attrName) ? node.getAttribute(attrName) : TreeFactory.createAttribute(attrName);
		a.setValue(attrValue);
		node.addAttribute(a);
		Dataset d = DatasetUtils.convertToDataset(a.getValue());
		d.setName(attrName);
		cached.put(makeAttributeKey(name, attrName), d);
	}

	private Dataset getCachedAttribute(String name, String attrName) {
		String key = makeAttributeKey(name, attrName);
		if (!cached.containsKey(key)) {
			Node node = name == null ? this : getNode(name);
			Attribute a = node.getAttribute(attrName);
			cached.put(key, DatasetUtils.convertToDataset(a.getValue()));
		}

		return cached.get(key);
	}
	
	public Dataset getAttr(String name, String attrName) {
		return getCachedAttribute(name, attrName);
	}

	public String getAttrString(String name, String attrName) {
		Node node = name == null ? this : getNode(name);
		Attribute a = node.getAttribute(attrName);
		return a.getFirstElement();
	}

	public boolean getAttrBoolean(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d.getElementLongAbs(0) != 0;
	}

	public long getAttrLong(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d.getElementLongAbs(0);
	}

	public double getAttrDouble(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d.getElementDoubleAbs(0);
	}

	public Number getAttrNumber(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		if (d.hasFloatingPointElements()) {
			return d.getElementDoubleAbs(0);
		}
		
		return d.getElementLongAbs(0);
	}

	public Date getAttrDate(String name, String attrName) {
		try {
			return DateFormat.getDateTimeInstance().parse(getAttrString(name, attrName));
		} catch (ParseException e) {
			return null;
		}
	}

	public void setAttrDate(String name, String attrName, Date date) {
		setAttribute(name, attrName, DateFormat.getDateTimeInstance().format(date));
	}
}
