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

package org.eclipse.dawnsci.nexus.validation;

import static org.eclipse.dawnsci.nexus.validation.NexusDataType.*;
import static org.eclipse.dawnsci.nexus.validation.NexusUnitCategory.*;

import java.util.Map;

import org.eclipse.january.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;

import org.eclipse.dawnsci.nexus.NXroot;
import org.eclipse.dawnsci.nexus.NXsubentry;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXinstrument;
import org.eclipse.dawnsci.nexus.NXsource;
import org.eclipse.dawnsci.nexus.NXmonochromator;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXmonitor;
import org.eclipse.dawnsci.nexus.NXdata;

/**
 * Validator for the application definition 'NXfluo'.
 */
public class NXfluoValidator extends AbstractNexusValidator implements NexusApplicationValidator {

	@Override
	public void validate(NXroot root) throws NexusValidationException {
		// validate child group 'entry' of type NXentry
		validateGroup_entry(root.getEntry());
	}

	@Override
	public void validate(NXentry entry) throws NexusValidationException {
		validateGroup_entry(entry);
	}

	@Override
	public void validate(NXsubentry subentry) throws NexusValidationException {
		validateGroup_entry(subentry);
	}


	/**
	 * Validate group 'entry' of type NXentry.
	 */
	private void validateGroup_entry(final NXsubentry group) throws NexusValidationException {
		// set the current entry, required for validating links
		setEntry(group);

		// validate that the group is not null
		validateGroupNotNull("entry", NXentry.class, group);

		// validate field 'title' of unknown type.
		final IDataset title = group.getTitle();
		validateFieldNotNull("title", title);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions

		// validate field 'start_time' of type NX_DATE_TIME.
		final IDataset start_time = group.getStart_time();
		validateFieldNotNull("start_time", start_time);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("start_time", start_time, NX_DATE_TIME);

		// validate field 'definition' of unknown type.
		final IDataset definition = group.getDefinition();
		validateFieldNotNull("definition", definition);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldEnumeration("definition", definition,
				"NXfluo");

		// validate unnamed child group of type NXinstrument (possibly multiple)
		final Map<String, NXinstrument> allInstrument = group.getAllInstrument();
		for (final NXinstrument instrument : allInstrument.values()) {
			validateGroup_entry_NXinstrument(instrument);
		}

		// validate unnamed child group of type NXsample (possibly multiple)
		final Map<String, NXsample> allSample = group.getAllSample();
		for (final NXsample sample : allSample.values()) {
			validateGroup_entry_NXsample(sample);
		}

		// validate unnamed child group of type NXmonitor (possibly multiple)
		final Map<String, NXmonitor> allMonitor = group.getAllMonitor();
		for (final NXmonitor monitor : allMonitor.values()) {
			validateGroup_entry_NXmonitor(monitor);
		}

		// validate child group 'data' of type NXdata
		validateGroup_entry_data(group.getData());
	}

	/**
	 * Validate unnamed group of type NXinstrument.
	 */
	private void validateGroup_entry_NXinstrument(final NXinstrument group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXinstrument.class, group);

		// validate unnamed child group of type NXsource (possibly multiple)
		final Map<String, NXsource> allSource = group.getAllSource();
		for (final NXsource source : allSource.values()) {
			validateGroup_entry_NXinstrument_NXsource(source);
		}

		// validate child group 'monochromator' of type NXmonochromator
		validateGroup_entry_NXinstrument_monochromator(group.getMonochromator());

		// validate child group 'fluorescence' of type NXdetector
		validateGroup_entry_NXinstrument_fluorescence(group.getDetector());
	}

	/**
	 * Validate unnamed group of type NXsource.
	 */
	private void validateGroup_entry_NXinstrument_NXsource(final NXsource group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXsource.class, group);

		// validate field 'type' of unknown type.
		final IDataset type = group.getType();
		validateFieldNotNull("type", type);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldEnumeration("type", type,
				"Spallation Neutron Source",
				"Pulsed Reactor Neutron Source",
				"Reactor Neutron Source",
				"Synchrotron X-ray Source",
				"Pulsed Muon Source",
				"Rotating Anode X-ray",
				"Fixed Tube X-ray",
				"UV Laser",
				"Free-Electron Laser",
				"Optical Laser",
				"Ion Source",
				"UV Plasma Source");

		// validate field 'name' of unknown type.
		final IDataset name = group.getName();
		validateFieldNotNull("name", name);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions

		// validate field 'probe' of unknown type.
		final IDataset probe = group.getProbe();
		validateFieldNotNull("probe", probe);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldEnumeration("probe", probe,
				"x-ray");
	}

	/**
	 * Validate group 'monochromator' of type NXmonochromator.
	 */
	private void validateGroup_entry_NXinstrument_monochromator(final NXmonochromator group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("monochromator", NXmonochromator.class, group);

		// validate field 'wavelength' of type NX_FLOAT.
		final IDataset wavelength = group.getWavelength();
		validateFieldNotNull("wavelength", wavelength);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("wavelength", wavelength, NX_FLOAT);
		validateFieldUnits("wavelength", wavelength, NX_WAVELENGTH);
	}

	/**
	 * Validate group 'fluorescence' of type NXdetector.
	 */
	private void validateGroup_entry_NXinstrument_fluorescence(final NXdetector group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("fluorescence", NXdetector.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", data, NX_ANY);
		validateFieldRank("data", data, 1);
		validateFieldDimensions("data", data, null, "nE");

		// validate field 'energy' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset energy = group.getDataset("energy");
		validateFieldNotNull("energy", energy);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("energy", energy, NX_FLOAT);
		validateFieldRank("energy", energy, 1);
		validateFieldDimensions("energy", energy, null, "nE");
	}

	/**
	 * Validate unnamed group of type NXsample.
	 */
	private void validateGroup_entry_NXsample(final NXsample group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXsample.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'name' of unknown type.
		final IDataset name = group.getName();
		validateFieldNotNull("name", name);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
	}

	/**
	 * Validate unnamed group of type NXmonitor.
	 */
	private void validateGroup_entry_NXmonitor(final NXmonitor group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXmonitor.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'mode' of unknown type.
		final IDataset mode = group.getMode();
		validateFieldNotNull("mode", mode);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldEnumeration("mode", mode,
				"monitor",
				"timer");

		// validate field 'preset' of type NX_FLOAT.
		final IDataset preset = group.getPreset();
		validateFieldNotNull("preset", preset);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("preset", preset, NX_FLOAT);
		validateFieldUnits("preset", preset, NX_ANY);

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", data, NX_ANY);
		validateFieldDimensions("data", data, "NXmonitor", "n");
	}

	/**
	 * Validate group 'data' of type NXdata.
	 */
	private void validateGroup_entry_data(final NXdata group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("data", NXdata.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate link 'energy' to location '/entry/instrument/fluorescence/energy
		final DataNode energy = group.getDataNode("energy");
		validateDataNodeLink("energy", energy, "/entry/instrument/fluorescence/energy");

		// validate link 'data' to location '/entry/instrument/fluorescence/data
		final DataNode data = group.getDataNode("data");
		validateDataNodeLink("data", data, "/entry/instrument/fluorescence/data");

	}
}
