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
 * An insertion device, as used in a synchrotron light source.
 * 
 */
public interface NXinsertion_device extends NXobject {

	public static final String NX_TYPE = "type";
	public static final String NX_GAP = "gap";
	public static final String NX_TAPER = "taper";
	public static final String NX_PHASE = "phase";
	public static final String NX_POLES = "poles";
	public static final String NX_MAGNETIC_WAVELENGTH = "magnetic_wavelength";
	public static final String NX_K = "k";
	public static final String NX_LENGTH = "length";
	public static final String NX_POWER = "power";
	public static final String NX_ENERGY = "energy";
	public static final String NX_BANDWIDTH = "bandwidth";
	public static final String NX_HARMONIC = "harmonic";
	public static final String NX_ATTRIBUTE_DEFAULT = "default";
	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>undulator</b> </li>
	 * <li><b>wiggler</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getType();
	
	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>undulator</b> </li>
	 * <li><b>wiggler</b> </li></ul></p>
	 * </p>
	 * 
	 * @param typeDataset the typeDataset
	 */
	public DataNode setType(IDataset typeDataset);

	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>undulator</b> </li>
	 * <li><b>wiggler</b> </li></ul></p>
	 * </p>
	 * 
	 * @return  the value.
	 */
	public String getTypeScalar();

	/**
	 * <p>
	 * <p><b>Enumeration:</b><ul>
	 * <li><b>undulator</b> </li>
	 * <li><b>wiggler</b> </li></ul></p>
	 * </p>
	 * 
	 * @param type the type
	 */
	public DataNode setTypeScalar(String typeValue);

	/**
	 * separation between opposing pairs of magnetic poles
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getGap();
	
	/**
	 * separation between opposing pairs of magnetic poles
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param gapDataset the gapDataset
	 */
	public DataNode setGap(IDataset gapDataset);

	/**
	 * separation between opposing pairs of magnetic poles
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getGapScalar();

	/**
	 * separation between opposing pairs of magnetic poles
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param gap the gap
	 */
	public DataNode setGapScalar(Double gapValue);

	/**
	 * angular of gap difference between upstream and downstream ends of the insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getTaper();
	
	/**
	 * angular of gap difference between upstream and downstream ends of the insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @param taperDataset the taperDataset
	 */
	public DataNode setTaper(IDataset taperDataset);

	/**
	 * angular of gap difference between upstream and downstream ends of the insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getTaperScalar();

	/**
	 * angular of gap difference between upstream and downstream ends of the insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @param taper the taper
	 */
	public DataNode setTaperScalar(Double taperValue);

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getPhase();
	
	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @param phaseDataset the phaseDataset
	 */
	public DataNode setPhase(IDataset phaseDataset);

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getPhaseScalar();

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * </p>
	 * 
	 * @param phase the phase
	 */
	public DataNode setPhaseScalar(Double phaseValue);

	/**
	 * number of poles
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getPoles();
	
	/**
	 * number of poles
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @param polesDataset the polesDataset
	 */
	public DataNode setPoles(IDataset polesDataset);

	/**
	 * number of poles
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Long getPolesScalar();

	/**
	 * number of poles
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @param poles the poles
	 */
	public DataNode setPolesScalar(Long polesValue);

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getMagnetic_wavelength();
	
	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * </p>
	 * 
	 * @param magnetic_wavelengthDataset the magnetic_wavelengthDataset
	 */
	public DataNode setMagnetic_wavelength(IDataset magnetic_wavelengthDataset);

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getMagnetic_wavelengthScalar();

	/**
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * </p>
	 * 
	 * @param magnetic_wavelength the magnetic_wavelength
	 */
	public DataNode setMagnetic_wavelengthScalar(Double magnetic_wavelengthValue);

	/**
	 * beam displacement parameter
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_DIMENSIONLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getK();
	
	/**
	 * beam displacement parameter
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_DIMENSIONLESS
	 * </p>
	 * 
	 * @param kDataset the kDataset
	 */
	public DataNode setK(IDataset kDataset);

	/**
	 * beam displacement parameter
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_DIMENSIONLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getKScalar();

	/**
	 * beam displacement parameter
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_DIMENSIONLESS
	 * </p>
	 * 
	 * @param k the k
	 */
	public DataNode setKScalar(Double kValue);

	/**
	 * length of insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getLength();
	
	/**
	 * length of insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param lengthDataset the lengthDataset
	 */
	public DataNode setLength(IDataset lengthDataset);

	/**
	 * length of insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getLengthScalar();

	/**
	 * length of insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param length the length
	 */
	public DataNode setLengthScalar(Double lengthValue);

	/**
	 * total power delivered by insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_POWER
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getPower();
	
	/**
	 * total power delivered by insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_POWER
	 * </p>
	 * 
	 * @param powerDataset the powerDataset
	 */
	public DataNode setPower(IDataset powerDataset);

	/**
	 * total power delivered by insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_POWER
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getPowerScalar();

	/**
	 * total power delivered by insertion device
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_POWER
	 * </p>
	 * 
	 * @param power the power
	 */
	public DataNode setPowerScalar(Double powerValue);

	/**
	 * energy of peak intensity in output spectrum
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getEnergy();
	
	/**
	 * energy of peak intensity in output spectrum
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @param energyDataset the energyDataset
	 */
	public DataNode setEnergy(IDataset energyDataset);

	/**
	 * energy of peak intensity in output spectrum
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getEnergyScalar();

	/**
	 * energy of peak intensity in output spectrum
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @param energy the energy
	 */
	public DataNode setEnergyScalar(Double energyValue);

	/**
	 * bandwidth of peak energy
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getBandwidth();
	
	/**
	 * bandwidth of peak energy
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @param bandwidthDataset the bandwidthDataset
	 */
	public DataNode setBandwidth(IDataset bandwidthDataset);

	/**
	 * bandwidth of peak energy
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getBandwidthScalar();

	/**
	 * bandwidth of peak energy
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * </p>
	 * 
	 * @param bandwidth the bandwidth
	 */
	public DataNode setBandwidthScalar(Double bandwidthValue);

	/**
	 * harmonic number of peak
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getHarmonic();
	
	/**
	 * harmonic number of peak
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @param harmonicDataset the harmonicDataset
	 */
	public DataNode setHarmonic(IDataset harmonicDataset);

	/**
	 * harmonic number of peak
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Long getHarmonicScalar();

	/**
	 * harmonic number of peak
	 * <p>
	 * <b>Type:</b> NX_INT
	 * <b>Units:</b> NX_UNITLESS
	 * </p>
	 * 
	 * @param harmonic the harmonic
	 */
	public DataNode setHarmonicScalar(Long harmonicValue);

	/**
	 * spectrum of insertion device
	 * 
	 * @return  the value.
	 */
	public NXdata getSpectrum();
	
	/**
	 * spectrum of insertion device
	 * 
	 * @param spectrumGroup the spectrumGroup
	 */
	public void setSpectrum(NXdata spectrumGroup);

	/**
	 * "Engineering" position of insertion device
	 * 
	 * @return  the value.
	 */
	public NXgeometry getGeometry();
	
	/**
	 * "Engineering" position of insertion device
	 * 
	 * @param geometryGroup the geometryGroup
	 */
	public void setGeometry(NXgeometry geometryGroup);

	/**
	 * Get a NXgeometry node by name:
	 * <ul>
	 * <li>
	 * "Engineering" position of insertion device</li>
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
	 * "Engineering" position of insertion device</li>
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
	 * "Engineering" position of insertion device</li>
	 * </ul>
	 * 
	 * @return  a map from node names to the NXgeometry for that node.
	 */
	public Map<String, NXgeometry> getAllGeometry();
	
	/**
	 * Set multiple child nodes of a particular type.
	 * <ul>
	 * <li>
	 * "Engineering" position of insertion device</li>
	 * </ul>
	 * 
	 * @param geometry the child nodes to add 
	 */
	
	public void setAllGeometry(Map<String, NXgeometry> geometry);
	

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
