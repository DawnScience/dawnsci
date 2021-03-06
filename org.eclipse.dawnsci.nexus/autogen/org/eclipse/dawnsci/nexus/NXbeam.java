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
 * Properties of the neutron or X-ray beam at a given location.
 * It will be referenced
 * by beamline component groups within the :ref:`NXinstrument` group or by the :ref:`NXsample` group. Note
 * that variables such as the incident energy could be scalar values or arrays. This group is
 * especially valuable in storing the results of instrument simulations in which it is useful
 * to specify the beam profile, time distribution etc. at each beamline component. Otherwise,
 * its most likely use is in the :ref:`NXsample` group in which it defines the results of the neutron
 * scattering by the sample, e.g., energy transfer, polarizations.
 * 
 */
public interface NXbeam extends NXobject {

	public static final String NX_DISTANCE = "distance";
	public static final String NX_INCIDENT_ENERGY = "incident_energy";
	public static final String NX_FINAL_ENERGY = "final_energy";
	public static final String NX_ENERGY_TRANSFER = "energy_transfer";
	public static final String NX_INCIDENT_WAVELENGTH = "incident_wavelength";
	public static final String NX_INCIDENT_WAVELENGTH_SPREAD = "incident_wavelength_spread";
	public static final String NX_INCIDENT_BEAM_DIVERGENCE = "incident_beam_divergence";
	public static final String NX_EXTENT = "extent";
	public static final String NX_FINAL_WAVELENGTH = "final_wavelength";
	public static final String NX_INCIDENT_POLARIZATION = "incident_polarization";
	public static final String NX_FINAL_POLARIZATION = "final_polarization";
	public static final String NX_FINAL_WAVELENGTH_SPREAD = "final_wavelength_spread";
	public static final String NX_FINAL_BEAM_DIVERGENCE = "final_beam_divergence";
	public static final String NX_FLUX = "flux";
	public static final String NX_ATTRIBUTE_DEFAULT = "default";
	/**
	 * Distance from sample
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getDistance();
	
	/**
	 * Distance from sample
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param distanceDataset the distanceDataset
	 */
	public DataNode setDistance(IDataset distanceDataset);

	/**
	 * Distance from sample
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getDistanceScalar();

	/**
	 * Distance from sample
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * </p>
	 * 
	 * @param distance the distance
	 */
	public DataNode setDistanceScalar(Double distanceValue);

	/**
	 * Energy on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getIncident_energy();
	
	/**
	 * Energy on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_energyDataset the incident_energyDataset
	 */
	public DataNode setIncident_energy(IDataset incident_energyDataset);

	/**
	 * Energy on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getIncident_energyScalar();

	/**
	 * Energy on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_energy the incident_energy
	 */
	public DataNode setIncident_energyScalar(Double incident_energyValue);

	/**
	 * Energy on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFinal_energy();
	
	/**
	 * Energy on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_energyDataset the final_energyDataset
	 */
	public DataNode setFinal_energy(IDataset final_energyDataset);

	/**
	 * Energy on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFinal_energyScalar();

	/**
	 * Energy on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_energy the final_energy
	 */
	public DataNode setFinal_energyScalar(Double final_energyValue);

	/**
	 * Energy change caused by beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getEnergy_transfer();
	
	/**
	 * Energy change caused by beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param energy_transferDataset the energy_transferDataset
	 */
	public DataNode setEnergy_transfer(IDataset energy_transferDataset);

	/**
	 * Energy change caused by beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getEnergy_transferScalar();

	/**
	 * Energy change caused by beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ENERGY
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param energy_transfer the energy_transfer
	 */
	public DataNode setEnergy_transferScalar(Double energy_transferValue);

	/**
	 * Wavelength on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getIncident_wavelength();
	
	/**
	 * Wavelength on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_wavelengthDataset the incident_wavelengthDataset
	 */
	public DataNode setIncident_wavelength(IDataset incident_wavelengthDataset);

	/**
	 * Wavelength on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getIncident_wavelengthScalar();

	/**
	 * Wavelength on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_wavelength the incident_wavelength
	 */
	public DataNode setIncident_wavelengthScalar(Double incident_wavelengthValue);

	/**
	 * Wavelength spread FWHM on entering component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getIncident_wavelength_spread();
	
	/**
	 * Wavelength spread FWHM on entering component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_wavelength_spreadDataset the incident_wavelength_spreadDataset
	 */
	public DataNode setIncident_wavelength_spread(IDataset incident_wavelength_spreadDataset);

	/**
	 * Wavelength spread FWHM on entering component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getIncident_wavelength_spreadScalar();

	/**
	 * Wavelength spread FWHM on entering component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param incident_wavelength_spread the incident_wavelength_spread
	 */
	public DataNode setIncident_wavelength_spreadScalar(Double incident_wavelength_spreadValue);

	/**
	 * Divergence of beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getIncident_beam_divergence();
	
	/**
	 * Divergence of beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param incident_beam_divergenceDataset the incident_beam_divergenceDataset
	 */
	public DataNode setIncident_beam_divergence(IDataset incident_beam_divergenceDataset);

	/**
	 * Divergence of beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getIncident_beam_divergenceScalar();

	/**
	 * Divergence of beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param incident_beam_divergence the incident_beam_divergence
	 */
	public DataNode setIncident_beam_divergenceScalar(Double incident_beam_divergenceValue);

	/**
	 * Size of the beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getExtent();
	
	/**
	 * Size of the beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param extentDataset the extentDataset
	 */
	public DataNode setExtent(IDataset extentDataset);

	/**
	 * Size of the beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getExtentScalar();

	/**
	 * Size of the beam entering this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_LENGTH
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param extent the extent
	 */
	public DataNode setExtentScalar(Double extentValue);

	/**
	 * Wavelength on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFinal_wavelength();
	
	/**
	 * Wavelength on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_wavelengthDataset the final_wavelengthDataset
	 */
	public DataNode setFinal_wavelength(IDataset final_wavelengthDataset);

	/**
	 * Wavelength on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFinal_wavelengthScalar();

	/**
	 * Wavelength on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_wavelength the final_wavelength
	 */
	public DataNode setFinal_wavelengthScalar(Double final_wavelengthValue);

	/**
	 * Polarization vector on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getIncident_polarization();
	
	/**
	 * Polarization vector on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param incident_polarizationDataset the incident_polarizationDataset
	 */
	public DataNode setIncident_polarization(IDataset incident_polarizationDataset);

	/**
	 * Polarization vector on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getIncident_polarizationScalar();

	/**
	 * Polarization vector on entering beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param incident_polarization the incident_polarization
	 */
	public DataNode setIncident_polarizationScalar(Double incident_polarizationValue);

	/**
	 * Polarization vector on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFinal_polarization();
	
	/**
	 * Polarization vector on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param final_polarizationDataset the final_polarizationDataset
	 */
	public DataNode setFinal_polarization(IDataset final_polarizationDataset);

	/**
	 * Polarization vector on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFinal_polarizationScalar();

	/**
	 * Polarization vector on leaving beamline component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANY
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param final_polarization the final_polarization
	 */
	public DataNode setFinal_polarizationScalar(Double final_polarizationValue);

	/**
	 * Wavelength spread FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFinal_wavelength_spread();
	
	/**
	 * Wavelength spread FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_wavelength_spreadDataset the final_wavelength_spreadDataset
	 */
	public DataNode setFinal_wavelength_spread(IDataset final_wavelength_spreadDataset);

	/**
	 * Wavelength spread FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFinal_wavelength_spreadScalar();

	/**
	 * Wavelength spread FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_WAVELENGTH
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param final_wavelength_spread the final_wavelength_spread
	 */
	public DataNode setFinal_wavelength_spreadScalar(Double final_wavelength_spreadValue);

	/**
	 * Divergence FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFinal_beam_divergence();
	
	/**
	 * Divergence FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param final_beam_divergenceDataset the final_beam_divergenceDataset
	 */
	public DataNode setFinal_beam_divergence(IDataset final_beam_divergenceDataset);

	/**
	 * Divergence FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFinal_beam_divergenceScalar();

	/**
	 * Divergence FWHM of beam leaving this component
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_ANGLE
	 * <b>Dimensions:</b> 1: 2; 2: j;
	 * </p>
	 * 
	 * @param final_beam_divergence the final_beam_divergence
	 */
	public DataNode setFinal_beam_divergenceScalar(Double final_beam_divergenceValue);

	/**
	 * flux incident on beam plane area
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_FLUX
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public IDataset getFlux();
	
	/**
	 * flux incident on beam plane area
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_FLUX
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param fluxDataset the fluxDataset
	 */
	public DataNode setFlux(IDataset fluxDataset);

	/**
	 * flux incident on beam plane area
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_FLUX
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @return  the value.
	 */
	public Double getFluxScalar();

	/**
	 * flux incident on beam plane area
	 * <p>
	 * <b>Type:</b> NX_FLOAT
	 * <b>Units:</b> NX_FLUX
	 * <b>Dimensions:</b> 1: i;
	 * </p>
	 * 
	 * @param flux the flux
	 */
	public DataNode setFluxScalar(Double fluxValue);

	/**
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.
	 * 
	 * @return  the value.
	 */
	public NXdata getData();
	
	/**
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.
	 * 
	 * @param dataGroup the dataGroup
	 */
	public void setData(NXdata dataGroup);

	/**
	 * Get a NXdata node by name:
	 * <ul>
	 * <li>
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.</li>
	 * </ul>
	 * 
	 * @param name  the name of the node.
	 * @return  a map from node names to the NXdata for that node.
	 */
	public NXdata getData(String name);
	
	/**
	 * Set a NXdata node by name:
	 * <ul>
	 * <li>
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.</li>
	 * </ul>
	 * 
	 * @param name the name of the node
	 * @param data the value to set
	 */
	public void setData(String name, NXdata data);
	
	/**
	 * Get all NXdata nodes:
	 * <ul>
	 * <li>
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.</li>
	 * </ul>
	 * 
	 * @return  a map from node names to the NXdata for that node.
	 */
	public Map<String, NXdata> getAllData();
	
	/**
	 * Set multiple child nodes of a particular type.
	 * <ul>
	 * <li>
	 * Distribution of beam with respect to relevant variable e.g. wavelength. This is mainly
	 * useful for simulations which need to store plottable information at each beamline
	 * component.</li>
	 * </ul>
	 * 
	 * @param data the child nodes to add 
	 */
	
	public void setAllData(Map<String, NXdata> data);
	

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
