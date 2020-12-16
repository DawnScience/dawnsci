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
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXmonitor;
import org.eclipse.dawnsci.nexus.NXdata;

/**
 * Validator for the application definition 'NXtomophase'.
 */
public class NXtomophaseValidator extends AbstractNexusValidator implements NexusApplicationValidator {

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

		// validate field 'end_time' of type NX_DATE_TIME.
		final IDataset end_time = group.getEnd_time();
		validateFieldNotNull("end_time", end_time);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("end_time", end_time, NX_DATE_TIME);

		// validate field 'definition' of unknown type.
		final IDataset definition = group.getDefinition();
		validateFieldNotNull("definition", definition);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldEnumeration("definition", definition,
				"NXtomophase");

		// validate child group 'instrument' of type NXinstrument
		validateGroup_entry_instrument(group.getInstrument());

		// validate child group 'sample' of type NXsample
		validateGroup_entry_sample(group.getSample());

		// validate child group 'control' of type NXmonitor
		validateGroup_entry_control(group.getMonitor("control"));

		// validate child group 'data' of type NXdata
		validateGroup_entry_data(group.getData());
	}

	/**
	 * Validate group 'instrument' of type NXinstrument.
	 */
	private void validateGroup_entry_instrument(final NXinstrument group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("instrument", NXinstrument.class, group);

		// validate unnamed child group of type NXsource (possibly multiple)
		validateUnnamedGroupOccurrences(group, NXsource.class, false, true);
		final Map<String, NXsource> allSource = group.getAllSource();
		for (final NXsource source : allSource.values()) {
			validateGroup_entry_instrument_NXsource(source);
		}

		// validate child group 'bright_field' of type NXdetector
		validateGroup_entry_instrument_bright_field(group.getDetector("bright_field"));

		// validate child group 'dark_field' of type NXdetector
		validateGroup_entry_instrument_dark_field(group.getDetector("dark_field"));

		// validate child group 'sample' of type NXdetector
		validateGroup_entry_instrument_sample(group.getDetector("sample"));
	}

	/**
	 * Validate unnamed group of type NXsource.
	 */
	private void validateGroup_entry_instrument_NXsource(final NXsource group) throws NexusValidationException {
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
				"neutron",
				"x-ray",
				"electron");
	}

	/**
	 * Validate group 'bright_field' of type NXdetector.
	 */
	private void validateGroup_entry_instrument_bright_field(final NXdetector group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("bright_field", NXdetector.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", group.getDataNode("data"), NX_ANY);
		validateFieldRank("data", data, 3);
		validateFieldDimensions("data", data, null, "nBrightFrames", "xSize", "ySize");

		// validate field 'sequence_number' of type NX_INT.
		final IDataset sequence_number = group.getSequence_number();
		validateFieldNotNull("sequence_number", sequence_number);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("sequence_number", sequence_number, NX_INT);
		validateFieldRank("sequence_number", sequence_number, 1);
		validateFieldDimensions("sequence_number", sequence_number, null, "nBrightFrames");
	}

	/**
	 * Validate group 'dark_field' of type NXdetector.
	 */
	private void validateGroup_entry_instrument_dark_field(final NXdetector group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("dark_field", NXdetector.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", group.getDataNode("data"), NX_ANY);
		validateFieldRank("data", data, 3);
		validateFieldDimensions("data", data, null, "nDarkFrames", "xSize", "ySize");

		// validate field 'sequence_number' of type NX_INT.
		final IDataset sequence_number = group.getSequence_number();
		validateFieldNotNull("sequence_number", sequence_number);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("sequence_number", sequence_number, NX_INT);
		validateFieldRank("sequence_number", sequence_number, 1);
		validateFieldDimensions("sequence_number", sequence_number, null, "nDarkFrames");
	}

	/**
	 * Validate group 'sample' of type NXdetector.
	 */
	private void validateGroup_entry_instrument_sample(final NXdetector group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("sample", NXdetector.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", group.getDataNode("data"), NX_ANY);
		validateFieldRank("data", data, 4);
		validateFieldDimensions("data", data, null, "nSampleFrames", "nPhase", "xSize", "ySize");

		// validate field 'sequence_number' of type NX_INT.
		final IDataset sequence_number = group.getSequence_number();
		validateFieldNotNull("sequence_number", sequence_number);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("sequence_number", sequence_number, NX_INT);
		validateFieldRank("sequence_number", sequence_number, 2);
		validateFieldDimensions("sequence_number", sequence_number, null, "nSampleFrames", "nPhase");

		// validate field 'x_pixel_size' of type NX_FLOAT.
		final IDataset x_pixel_size = group.getX_pixel_size();
		validateFieldNotNull("x_pixel_size", x_pixel_size);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("x_pixel_size", x_pixel_size, NX_FLOAT);
		validateFieldUnits("x_pixel_size", group.getDataNode("x_pixel_size"), NX_LENGTH);
		validateFieldRank("x_pixel_size", x_pixel_size, 2);
		validateFieldDimensions("x_pixel_size", x_pixel_size, "NXdetector", "i", "j");

		// validate field 'y_pixel_size' of type NX_FLOAT.
		final IDataset y_pixel_size = group.getY_pixel_size();
		validateFieldNotNull("y_pixel_size", y_pixel_size);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("y_pixel_size", y_pixel_size, NX_FLOAT);
		validateFieldUnits("y_pixel_size", group.getDataNode("y_pixel_size"), NX_LENGTH);
		validateFieldRank("y_pixel_size", y_pixel_size, 2);
		validateFieldDimensions("y_pixel_size", y_pixel_size, "NXdetector", "i", "j");

		// validate field 'distance' of type NX_FLOAT.
		final IDataset distance = group.getDistance();
		validateFieldNotNull("distance", distance);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("distance", distance, NX_FLOAT);
		validateFieldUnits("distance", group.getDataNode("distance"), NX_LENGTH);
		validateFieldRank("distance", distance, 3);
		validateFieldDimensions("distance", distance, "NXdetector", "np", "i", "j");
	}

	/**
	 * Validate group 'sample' of type NXsample.
	 */
	private void validateGroup_entry_sample(final NXsample group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("sample", NXsample.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'name' of unknown type.
		final IDataset name = group.getName();
		validateFieldNotNull("name", name);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions

		// validate field 'rotation_angle' of type NX_FLOAT.
		final IDataset rotation_angle = group.getRotation_angle();
		validateFieldNotNull("rotation_angle", rotation_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("rotation_angle", rotation_angle, NX_FLOAT);
		validateFieldUnits("rotation_angle", group.getDataNode("rotation_angle"), NX_ANGLE);
		validateFieldRank("rotation_angle", rotation_angle, 1);
		validateFieldDimensions("rotation_angle", rotation_angle, null, "nSampleFrames");

		// validate field 'x_translation' of type NX_FLOAT.
		final IDataset x_translation = group.getX_translation();
		validateFieldNotNull("x_translation", x_translation);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("x_translation", x_translation, NX_FLOAT);
		validateFieldUnits("x_translation", group.getDataNode("x_translation"), NX_LENGTH);
		validateFieldRank("x_translation", x_translation, 1);
		validateFieldDimensions("x_translation", x_translation, null, "nSampleFrames");

		// validate field 'y_translation' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset y_translation = group.getDataset("y_translation");
		validateFieldNotNull("y_translation", y_translation);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("y_translation", y_translation, NX_FLOAT);
		validateFieldUnits("y_translation", group.getDataNode("y_translation"), NX_LENGTH);
		validateFieldRank("y_translation", y_translation, 1);
		validateFieldDimensions("y_translation", y_translation, null, "nSampleFrames");

		// validate field 'z_translation' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset z_translation = group.getDataset("z_translation");
		validateFieldNotNull("z_translation", z_translation);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("z_translation", z_translation, NX_FLOAT);
		validateFieldUnits("z_translation", group.getDataNode("z_translation"), NX_LENGTH);
		validateFieldRank("z_translation", z_translation, 1);
		validateFieldDimensions("z_translation", z_translation, null, "nSampleFrames");
	}

	/**
	 * Validate group 'control' of type NXmonitor.
	 */
	private void validateGroup_entry_control(final NXmonitor group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("control", NXmonitor.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'integral' of type NX_FLOAT.
		final IDataset integral = group.getIntegral();
		validateFieldNotNull("integral", integral);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("integral", integral, NX_FLOAT);
		validateFieldUnits("integral", group.getDataNode("integral"), NX_ANY);
		validateFieldRank("integral", integral, 1);
		validateFieldDimensions("integral", integral, null, "nDarkFrames + nBrightFrames + nSampleFrame");
	}

	/**
	 * Validate group 'data' of type NXdata.
	 */
	private void validateGroup_entry_data(final NXdata group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("data", NXdata.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate link 'data' to location '/NXentry/NXinstrument/sample:NXdetector/data
		final DataNode data = group.getDataNode("data");
		validateDataNodeLink("data", data, "/NXentry/NXinstrument/sample:NXdetector/data");

		// validate link 'rotation_angle' to location '/NXentry/NXsample/rotation_angle
		final DataNode rotation_angle = group.getDataNode("rotation_angle");
		validateDataNodeLink("rotation_angle", rotation_angle, "/NXentry/NXsample/rotation_angle");

	}
}
