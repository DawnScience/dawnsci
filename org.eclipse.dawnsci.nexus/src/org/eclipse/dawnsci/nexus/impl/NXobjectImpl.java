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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.SymbolicNode;
import org.eclipse.dawnsci.analysis.tree.TreeFactory;
import org.eclipse.dawnsci.analysis.tree.impl.GroupNodeImpl;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.NexusNodeFactory;
import org.eclipse.january.DatasetException;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DatasetUtils;
import org.eclipse.january.dataset.DateDataset;
import org.eclipse.january.dataset.IDataset;
import org.eclipse.january.dataset.ILazyDataset;
import org.eclipse.january.dataset.ILazyWriteableDataset;
import org.eclipse.january.dataset.InterfaceUtils;
import org.eclipse.january.dataset.LazyWriteableDataset;
import org.eclipse.january.dataset.StringDataset;

/**
 * The abstract superclass of all base class implementation classes.
 * Unlike the base class implementation classes, this class is
 * <em>not</em> autogenerated.
 */
@SuppressWarnings("restriction")
public abstract class NXobjectImpl extends GroupNodeImpl implements NXobject {

	protected static final long serialVersionUID = GroupNodeImpl.serialVersionUID;

	/**
	 * Name of attribute
	 */
	public static final String NX_CLASS = "NX_class";
	
	private static final int CACHE_LIMIT = 1024;

	private Map<String, Dataset> cached = new HashMap<>();

	/**
	 * Creates a new NeXus group node. This constructor is used when
	 * create a new NeXus file
	 */
	protected NXobjectImpl() {
		super(NexusNodeFactory.getNextOid());
		createNxClassAttribute();
	}
	
	/**
	 * Creates a new NeXus group node. This constructor is used when loading
	 * a new NeXus file. No further nodes should be added to a NeXus tree that has
	 * been loaded from disk.
	 * @param oid
	 */
	protected NXobjectImpl(long oid) {
		super(oid);
		createNxClassAttribute();
	}

	private Dataset getCached(String name) {
		if (!cached.containsKey(name)) {
			DataNode dataNode = getDataNode(name);
			if (dataNode != null) {
				ILazyDataset lazy = dataNode.getDataset();
				if (!(lazy instanceof IDataset)) {
					// if this is a lazy dataset, set the slice on it
					int size = lazy.getSize();
					if (size > CACHE_LIMIT) {
						// cannot return a Dataset if the size is too large
						throw new IllegalStateException("Dataset is too large to cache. This method should only be used for small datasets.");
					} else {
						try {
							lazy = lazy.getSlice();
						} catch (DatasetException e) {
							throw new RuntimeException("Could not get data from lazy dataset", e);
						}
					}
				}
				cached.put(name, DatasetUtils.convertToDataset((IDataset) lazy));
			}
		}
		return cached.get(name);
	}

	@Override
	public boolean canAddChild(NXobject nexusObject) {
		return getPermittedChildGroupClasses().contains(nexusObject.getNexusBaseClass());
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
	
	public ILazyDataset getLazyDataset(String name) {
		final DataNode dataNode = getDataNode(name);
		return dataNode == null ? null : dataNode.getDataset();
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
	public ILazyWriteableDataset initializeLazyDataset(String name, int rank, Class<?> dtype) {
		int[] shape = new int[rank];
		Arrays.fill(shape, ILazyWriteableDataset.UNLIMITED);
		return initializeLazyDataset(name, shape, dtype);
	}
	
	@Override
	public ILazyWriteableDataset initializeLazyDataset(String name,
			int[] maxShape, Class<?> eClass) {
		ILazyWriteableDataset dataset = new LazyWriteableDataset(name, eClass, maxShape, null, null, null);
		createDataNode(name, dataset);
		
		return dataset;
	}
	
	@Override
	public ILazyWriteableDataset initializeFixedSizeLazyDataset(String name, int[] shape,
			Class<?> eClass) {
		ILazyWriteableDataset dataset = new LazyWriteableDataset(name, eClass, shape, shape, null, null);
		createDataNode(name, dataset);
		
		return dataset;
	}

	@Override
	public void addExternalLink(String name, String externalFileName, String pathToNode) {
		try {
			URI uri = new URI(externalFileName);
			SymbolicNode linkNode = NexusNodeFactory.createSymbolicNode(uri, pathToNode);
			addSymbolicNode(name, linkNode);
		} catch (URISyntaxException e) {
			// the filename is not a valid URI, not expected
			throw new IllegalArgumentException("Filename cannot be convert to a URI", e);
		}
	}

	@Override
	public DataNode createDataNode(String name, ILazyDataset value) {
		// note that this method should only be used when creating a new NeXus tree
		DataNode dataNode = NexusNodeFactory.createDataNode();
		addDataNode(name, dataNode);
		dataNode.setDataset(value);
		
		return dataNode;
	}

	@SuppressWarnings("unchecked")
	public <N extends NXobject> Map<String, N> getChildren(Class<N> nxClass) {
		final Map<String, N> map = new LinkedHashMap<>();
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
	
	public Map<String, NXobject> getChildren() {
		final Map<String, NXobject> map = new LinkedHashMap<>();
		for (NodeLink n : this) {
			if (n.isDestinationGroup()) {
				GroupNode g = (GroupNode) n.getDestination();
				if (g instanceof NXobject) {
					map.put(n.getName(), (NXobject) g);
				}
			}
		}
		
		return map;
	}

	@Override
	public <N extends NXobject> void putChild(String name, N child) {
		if (containsGroupNode(name)) {
			throw new IllegalArgumentException("A group node already exists with the name " + name);
		}

		addGroupNode(name, child);
	}

	@Override
	public <N extends NXobject> void setChildren(Map<String, N> map) {
		map = new LinkedHashMap<>(map);
		for (String name : map.keySet()) {
			N child = map.get(name);
			addGroupNode(name, child);
		}
	}

	@Override
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
			cached.put(name, (Dataset) dataNode.getDataset());
		} else {
			// create a new dataset, create a new DataNode containing that dataset
			StringDataset dataset = DatasetFactory.createFromObject(StringDataset.class, value);
			dataNode = createDataNode(name, dataset);
			// add the new dataset to the cache
			cached.put(name, dataset);
		}
		
		return dataNode;
	}

	@Override
	public Map<String, IDataset> getAllDatasets() {
		Map<String, IDataset> map = new LinkedHashMap<>();
		
		for (NodeLink n : this) {
			if (n.isDestinationData()) {
				map.put(n.getName(), getCached(n.getName()));
			}
		}
		return map;
	}

	@Override
	public Boolean getBoolean(String name) {
		Dataset d = getCached(name);
		return d == null ? null : d.getElementBooleanAbs(0);
	}

	@Override
	public Long getLong(String name) {
		Dataset d = getCached(name);
		return d == null ? null : d.getElementLongAbs(0);
	}

	@Override
	public Double getDouble(String name) {
		Dataset d = getCached(name);
		return d == null ? null : d.getElementDoubleAbs(0);
	}

	@Override
	public Number getNumber(String name) {
		Dataset d = getCached(name);
		if (d != null) {
			if (d.hasFloatingPointElements())
				return d.getElementDoubleAbs(0);
			return d.getElementLongAbs(0);
		}
		
		return null;
	}

	@Override
	public Date getDate(String name) {
		Dataset d = getCached(name);
		if (d instanceof DateDataset) {
			return ((DateDataset) d).getDate();
		}
		
		return null;
	}

	/**
	 * Set the value of the given field to the given value. The
	 * value may be an atomic value (e.g. primitive wrapper, object or string),
	 * or a dataset.
	 * @param name name
	 * @param value value
	 */
	@Override
	public DataNode setField(String name, Object value) {
		final DataNode dataNode;
		if (containsDataNode(name)) {
			dataNode = getDataNode(name);
			// create a new dataset, new DataNode and update the cache
			Dataset dataset = getCached(name);
			if (!InterfaceUtils.getInterface(value).isInstance(dataset)) {
				throw new IllegalArgumentException("Cannot overwrite existing dataset of " + dataset.getElementClass());
			}
			
			dataset.setObjectAbs(0, value);
		} else {
			Dataset dataset = DatasetFactory.createFromObject(value);
			dataset.setName(name);
			dataNode = createDataNode(name, dataset);
			cached.put(name, dataset);
		}
		
		return dataNode;
	}

	protected DataNode setDate(String name, Date date) {
		return setField(name, date);
	}

	private static String makeAttributeKey(String name, String attrName) {
		return name == null ? ATTRIBUTE + attrName : name + ATTRIBUTE + attrName;
	}

	@Override
	public void setAttribute(String name, String attrName, Object attrValue) {
		Node node = name == null ? this : getNode(name);
		if (node == null) {
			throw new IllegalArgumentException("No group of field with name " + name);
		}
		
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
			if (node == null) {
				throw new IllegalArgumentException("No group of field with name " + name);
			}
			Attribute a = node.getAttribute(attrName);
			if (a != null) {
				cached.put(key, DatasetUtils.convertToDataset(a.getValue()));
			}
		}

		return cached.get(key);
	}
	
	@Override
	public Dataset getAttr(String name, String attrName) {
		return getCachedAttribute(name, attrName);
	}

	@Override
	public String getAttrString(String name, String attrName) {
		Node node = name == null ? this : getNode(name);
		Attribute a = node.getAttribute(attrName);
		return a == null ? null : a.getFirstElement();
	}

	@Override
	public Boolean getAttrBoolean(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d == null ? null : d.getElementBooleanAbs(0);
	}

	@Override
	public Long getAttrLong(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d == null ? null : d.getElementLongAbs(0);
	}

	@Override
	public Double getAttrDouble(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		return d == null ? null : d.getElementDoubleAbs(0);
	}

	@Override
	public Number getAttrNumber(String name, String attrName) {
		Dataset d = getCachedAttribute(name, attrName);
		if (d == null) {
			return null;
		}
		if (d.hasFloatingPointElements()) {
			return d.getElementDoubleAbs(0);
		}
		
		return d.getElementLongAbs(0);
	}

	@Override
	public Date getAttrDate(String name, String attrName) {
//		try {
//			return DateFormat.getDateTimeInstance().parse(getAttrString(name, attrName));
//		} catch (ParseException e) {
//			return null;
//		}
		Dataset d = getCachedAttribute(name, attrName);
		if (d instanceof DateDataset) {
			return ((DateDataset) d).getDate();
		}
		
		return null;
	}
	
	@Override
	protected void appendNodeString(StringBuilder s, String n) {
		s.append(INDENT);
		s.append(n);
		Node node = getNode(n);
		if (node instanceof SymbolicNode)
			s.append('@');
		else if (node instanceof GroupNode) {
			if (node instanceof NXobject) { // append type of NXobject
				s.append('[');
				s.append(((NXobject) node).getNexusBaseClass());
				s.append(']');
			}
			s.append('/');
		}
//			else
//				s.append(String.format("(%d)", node.getID()));
		s.append('\n');
	}

}
