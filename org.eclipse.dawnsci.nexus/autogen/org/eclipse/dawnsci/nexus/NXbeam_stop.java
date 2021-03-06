/*-
 *******************************************************************************
 * Copyright (c) 2020 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This file was auto-generated from the NXDL XML definition.
 *******************************************************************************/

package org.eclipse.dawnsci.nexus;

import java.util.Map;

import org.eclipse.dawnsci.analysis.api.tree.DataNode;

import org.eclipse.january.dataset.IDataset;

/**
 * A device that blocks the beam completely, usually to protect a detector.
 * Beamstops and their positions are important for SANS
 * and SAXS experiments.
 * 
 */
public interface NXbeam_stop extends NXobject {

	public static final String NX_DESCRIPTION = "description";
	public static final String NX_SIZE = "size";
	public static final String NX_X = "x";
	public static final String NX_Y = "y";
	public static final String NX_DISTANCE_TO_DETECTOR = "distance_to_detector";
	public static final String NX_STATUS = "status";
	public static final String NX_ATTRIBUTE_DEFAULT = "default";
	/**
	 * engineering shape, orientation and position of the beam stop.
	 * 
	 * @return  the value.
	 */
	public NXgeometry getGeometry();
	
	/**
	 * engineering shape, orientation and position of the beam stop.
	 * 
	 * @param geometryGroup the geometryGroup
	 */
	public void setGeometry(NXgeometry geometryGroup);

	/**
	 * Get a NXgeometry node by name:
	 * <ul>
	 * <li>
	 * engineering shape, orientation and position of the beam stop.</li>
	 * </ul>
	 * 
	 * @param name  the name of the node.
	 * @return  a map from node names to the NXgeometry for that node.
	 */
	public NXgeometry getGeometry(String name);
	
	/**
	 * Set a NXgeometry node by name:
	 * <ul>
	 * <li>
	 * engineering shape, orientation and position of the beam stop.</li>
	 * </ul>
	 * 
	 * @param name the name of the node
	 * @param geometry the value to set
	 */
	public void setGeometry(String name, NXgeometry geometry);
	
	/**
	 * Get all NXgeometry nodes:
	 * <ul>
	 * <li>
	 * engineering shape, orientation and position of the beam stop.</li>
	 * </ul>
	 * 
	 * @return  a map from node names to the NXgeometry for that node.
	 */
	public Map<String, NXgeometry> getAllGeometry();
	
	/**
	 * Set multiple child nodes of a particular type.
	 * <ul>
	 * <li>
	 * engineering shape, orientation and position of the beam stop.</li>
	 * </ul>
	 * 
	 * @param geometry the child nodes to add 
	 */
	
	public void setAllGeometry(Map<String, NXgeometry> geometry);
	

	/**
	 * description of beamstop
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>circular</b> </li>
	 * <li><b>rectangular</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getDescription();
	
	/**
	 * description of beamstop
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>circular</b> </li>
	 * <li><b>rectangular</b> </li></ul></p>
	 * </p>
	 * 
	 * @param descriptionDataset the descriptionDataset
	 */
	public DataNode setDescription(IDataset descriptionDataset);

	/**
	 * description of beamstop
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>circular</b> </li>
	 * <li><b>rectangular</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public String getDescriptionScalar();

	/**
	 * description of beamstop
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>circular</b> </li>
	 * <li><b>rectangular</b> </li></ul></p>
	 * </p>
	 * 
	 * @param description the description
	 */
	public DataNode setDescriptionScalar(String descriptionValue);

	/**
	 * size of beamstop
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getSize();
	
	/**
	 * size of beamstop
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param sizeDataset the sizeDataset
	 */
	public DataNode setSize(IDataset sizeDataset);

	/**
	 * size of beamstop
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getSizeScalar();

	/**
	 * size of beamstop
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param size the size
	 */
	public DataNode setSizeScalar(Double sizeValue);

	/**
	 * x position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getX();
	
	/**
	 * x position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param xDataset the xDataset
	 */
	public DataNode setX(IDataset xDataset);

	/**
	 * x position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getXScalar();

	/**
	 * x position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param x the x
	 */
	public DataNode setXScalar(Double xValue);

	/**
	 * y position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getY();
	
	/**
	 * y position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param yDataset the yDataset
	 */
	public DataNode setY(IDataset yDataset);

	/**
	 * y position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getYScalar();

	/**
	 * y position of the beamstop in relation to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param y the y
	 */
	public DataNode setYScalar(Double yValue);

	/**
	 * distance of the beamstop to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getDistance_to_detector();
	
	/**
	 * distance of the beamstop to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param distance_to_detectorDataset the distance_to_detectorDataset
	 */
	public DataNode setDistance_to_detector(IDataset distance_to_detectorDataset);

	/**
	 * distance of the beamstop to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getDistance_to_detectorScalar();

	/**
	 * distance of the beamstop to the detector
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param distance_to_detector the distance_to_detector
	 */
	public DataNode setDistance_to_detectorScalar(Double distance_to_detectorValue);

	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>in</b> </li>
	 * <li><b>out</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getStatus();
	
	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>in</b> </li>
	 * <li><b>out</b> </li></ul></p>
	 * </p>
	 * 
	 * @param statusDataset the statusDataset
	 */
	public DataNode setStatus(IDataset statusDataset);

	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>in</b> </li>
	 * <li><b>out</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public String getStatusScalar();

	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>in</b> </li>
	 * <li><b>out</b> </li></ul></p>
	 * </p>
	 * 
	 * @param status the status
	 */
	public DataNode setStatusScalar(String statusValue);

	/**
	 * .. index:: plotting
	 * Declares which child group contains a path leading
	 * to a :ref:`NXdata` group.
	 * It is recommended (as of NIAC2014) to use this attribute
	 * to help define the path to the default dataset to be plotted.
	 * See https://www.nexusformat.org/2014_How_to_find_default_data.html
	 * for a summary of the discussion.
	 * 
	 * @return  the value.
	 */
	public String getAttributeDefault();
	
	/**
	 * .. index:: plotting
	 * Declares which child group contains a path leading
	 * to a :ref:`NXdata` group.
	 * It is recommended (as of NIAC2014) to use this attribute
	 * to help define the path to the default dataset to be plotted.
	 * See https://www.nexusformat.org/2014_How_to_find_default_data.html
	 * for a summary of the discussion.
	 * 
	 * @param defaultValue the defaultValue
	 */
	public void setAttributeDefault(String defaultValue);

}
