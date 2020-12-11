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
import org.eclipse.dawnsci.nexus.NXcrystal;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXmonitor;
import org.eclipse.dawnsci.nexus.NXdata;

/**
 * Validator for the application definition 'NXtas'.
 */
public class NXtasValidator extends AbstractNexusValidator implements NexusApplicationValidator {

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

		// validate field 'title' of type NX_CHAR.
		final IDataset title = group.getTitle();
		validateFieldNotNull("title", title);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("title", title, NX_CHAR);

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
				"NXtas");

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

		// validate unnamed child group of type NXdata (possibly multiple)
		final Map<String, NXdata> allData = group.getAllData();
		for (final NXdata data : allData.values()) {
			validateGroup_entry_NXdata(data);
		}
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

		// validate child group 'monochromator' of type NXcrystal
		validateGroup_entry_NXinstrument_monochromator(group.getCrystal());

		// validate child group 'analyser' of type NXcrystal
		validateGroup_entry_NXinstrument_analyser(group.getCrystal());

		// validate unnamed child group of type NXdetector (possibly multiple)
		final Map<String, NXdetector> allDetector = group.getAllDetector();
		for (final NXdetector detector : allDetector.values()) {
			validateGroup_entry_NXinstrument_NXdetector(detector);
		}
	}

	/**
	 * Validate unnamed group of type NXsource.
	 */
	private void validateGroup_entry_NXinstrument_NXsource(final NXsource group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXsource.class, group);

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
				"x-ray");
	}

	/**
	 * Validate group 'monochromator' of type NXcrystal.
	 */
	private void validateGroup_entry_NXinstrument_monochromator(final NXcrystal group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("monochromator", NXcrystal.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'ei' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset ei = group.getDataset("ei");
		validateFieldNotNull("ei", ei);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("ei", ei, NX_FLOAT);
		validateFieldUnits("ei", group.getDataNode("ei"), NX_ENERGY);
		validateFieldRank("ei", ei, 1);
		validateFieldDimensions("ei", ei, null, "nP");

		// validate field 'rotation_angle' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset rotation_angle = group.getDataset("rotation_angle");
		validateFieldNotNull("rotation_angle", rotation_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("rotation_angle", rotation_angle, NX_FLOAT);
		validateFieldUnits("rotation_angle", group.getDataNode("rotation_angle"), NX_ANGLE);
		validateFieldRank("rotation_angle", rotation_angle, 1);
		validateFieldDimensions("rotation_angle", rotation_angle, null, "nP");
	}

	/**
	 * Validate group 'analyser' of type NXcrystal.
	 */
	private void validateGroup_entry_NXinstrument_analyser(final NXcrystal group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull("analyser", NXcrystal.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'ef' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset ef = group.getDataset("ef");
		validateFieldNotNull("ef", ef);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("ef", ef, NX_FLOAT);
		validateFieldUnits("ef", group.getDataNode("ef"), NX_ENERGY);
		validateFieldRank("ef", ef, 1);
		validateFieldDimensions("ef", ef, null, "nP");

		// validate field 'rotation_angle' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset rotation_angle = group.getDataset("rotation_angle");
		validateFieldNotNull("rotation_angle", rotation_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("rotation_angle", rotation_angle, NX_FLOAT);
		validateFieldUnits("rotation_angle", group.getDataNode("rotation_angle"), NX_ANGLE);
		validateFieldRank("rotation_angle", rotation_angle, 1);
		validateFieldDimensions("rotation_angle", rotation_angle, null, "nP");

		// validate field 'polar_angle' of type NX_FLOAT.
		final IDataset polar_angle = group.getPolar_angle();
		validateFieldNotNull("polar_angle", polar_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("polar_angle", polar_angle, NX_FLOAT);
		validateFieldUnits("polar_angle", group.getDataNode("polar_angle"), NX_ANGLE);
		validateFieldRank("polar_angle", polar_angle, 1);
		validateFieldDimensions("polar_angle", polar_angle, null, "nP");
	}

	/**
	 * Validate unnamed group of type NXdetector.
	 */
	private void validateGroup_entry_NXinstrument_NXdetector(final NXdetector group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXdetector.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'data' of type NX_INT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_INT);
		validateFieldUnits("data", group.getDataNode("data"), NX_ANY);
		validateFieldRank("data", data, 1);
		validateFieldDimensions("data", data, null, "nP");

		// validate field 'polar_angle' of type NX_FLOAT.
		final IDataset polar_angle = group.getPolar_angle();
		validateFieldNotNull("polar_angle", polar_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("polar_angle", polar_angle, NX_FLOAT);
		validateFieldUnits("polar_angle", group.getDataNode("polar_angle"), NX_ANGLE);
		validateFieldRank("polar_angle", polar_angle, 1);
		validateFieldDimensions("polar_angle", polar_angle, null, "nP");
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

		// validate field 'qh' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset qh = group.getDataset("qh");
		validateFieldNotNull("qh", qh);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("qh", qh, NX_FLOAT);
		validateFieldUnits("qh", group.getDataNode("qh"), NX_DIMENSIONLESS);
		validateFieldRank("qh", qh, 1);
		validateFieldDimensions("qh", qh, null, "nP");

		// validate field 'qk' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset qk = group.getDataset("qk");
		validateFieldNotNull("qk", qk);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("qk", qk, NX_FLOAT);
		validateFieldUnits("qk", group.getDataNode("qk"), NX_DIMENSIONLESS);
		validateFieldRank("qk", qk, 1);
		validateFieldDimensions("qk", qk, null, "nP");

		// validate field 'ql' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset ql = group.getDataset("ql");
		validateFieldNotNull("ql", ql);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("ql", ql, NX_FLOAT);
		validateFieldUnits("ql", group.getDataNode("ql"), NX_DIMENSIONLESS);
		validateFieldRank("ql", ql, 1);
		validateFieldDimensions("ql", ql, null, "nP");

		// validate field 'en' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset en = group.getDataset("en");
		validateFieldNotNull("en", en);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("en", en, NX_FLOAT);
		validateFieldUnits("en", group.getDataNode("en"), NX_ENERGY);
		validateFieldRank("en", en, 1);
		validateFieldDimensions("en", en, null, "nP");

		// validate field 'rotation_angle' of type NX_FLOAT.
		final IDataset rotation_angle = group.getRotation_angle();
		validateFieldNotNull("rotation_angle", rotation_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("rotation_angle", rotation_angle, NX_FLOAT);
		validateFieldUnits("rotation_angle", group.getDataNode("rotation_angle"), NX_ANGLE);
		validateFieldRank("rotation_angle", rotation_angle, 1);
		validateFieldDimensions("rotation_angle", rotation_angle, null, "nP");

		// validate field 'polar_angle' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset polar_angle = group.getDataset("polar_angle");
		validateFieldNotNull("polar_angle", polar_angle);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("polar_angle", polar_angle, NX_FLOAT);
		validateFieldUnits("polar_angle", group.getDataNode("polar_angle"), NX_ANGLE);
		validateFieldRank("polar_angle", polar_angle, 1);
		validateFieldDimensions("polar_angle", polar_angle, null, "nP");

		// validate field 'sgu' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset sgu = group.getDataset("sgu");
		validateFieldNotNull("sgu", sgu);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("sgu", sgu, NX_FLOAT);
		validateFieldUnits("sgu", group.getDataNode("sgu"), NX_ANGLE);
		validateFieldRank("sgu", sgu, 1);
		validateFieldDimensions("sgu", sgu, null, "nP");

		// validate field 'sgl' of type NX_FLOAT. Note: field not defined in base class.
		final IDataset sgl = group.getDataset("sgl");
		validateFieldNotNull("sgl", sgl);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("sgl", sgl, NX_FLOAT);
		validateFieldUnits("sgl", group.getDataNode("sgl"), NX_ANGLE);
		validateFieldRank("sgl", sgl, 1);
		validateFieldDimensions("sgl", sgl, null, "nP");

		// validate field 'unit_cell' of type NX_FLOAT.
		final IDataset unit_cell = group.getUnit_cell();
		validateFieldNotNull("unit_cell", unit_cell);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("unit_cell", unit_cell, NX_FLOAT);
		validateFieldUnits("unit_cell", group.getDataNode("unit_cell"), NX_LENGTH);
		validateFieldRank("unit_cell", unit_cell, 1);
		validateFieldDimensions("unit_cell", unit_cell, null, 6);

		// validate field 'orientation_matrix' of type NX_FLOAT.
		final IDataset orientation_matrix = group.getOrientation_matrix();
		validateFieldNotNull("orientation_matrix", orientation_matrix);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("orientation_matrix", orientation_matrix, NX_FLOAT);
		validateFieldUnits("orientation_matrix", group.getDataNode("orientation_matrix"), NX_DIMENSIONLESS);
		validateFieldRank("orientation_matrix", orientation_matrix, 1);
		validateFieldDimensions("orientation_matrix", orientation_matrix, null, 9);
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
		validateFieldUnits("preset", group.getDataNode("preset"), NX_ANY);

		// validate field 'data' of type NX_FLOAT.
		final IDataset data = group.getData();
		validateFieldNotNull("data", data);
		// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
		validateFieldType("data", data, NX_FLOAT);
		validateFieldUnits("data", group.getDataNode("data"), NX_ANY);
		validateFieldRank("data", data, 1);
		validateFieldDimensions("data", data, null, "nP");
	}

	/**
	 * Validate unnamed group of type NXdata.
	 */
	private void validateGroup_entry_NXdata(final NXdata group) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, NXdata.class, group);
		clearLocalGroupDimensionPlaceholderValues();

		// validate link 'ei' to location '/NXentry/NXinstrument/monochromator:NXcrystal/ei
		final DataNode ei = group.getDataNode("ei");
		validateDataNodeLink("ei", ei, "/NXentry/NXinstrument/monochromator:NXcrystal/ei");

		// validate link 'ef' to location '/NXentry/NXinstrument/analyzer:NXcrystal/ef
		final DataNode ef = group.getDataNode("ef");
		validateDataNodeLink("ef", ef, "/NXentry/NXinstrument/analyzer:NXcrystal/ef");

		// validate link 'en' to location '/NXentry/NXsample/en
		final DataNode en = group.getDataNode("en");
		validateDataNodeLink("en", en, "/NXentry/NXsample/en");

		// validate link 'qh' to location '/NXentry/NXsample/qh
		final DataNode qh = group.getDataNode("qh");
		validateDataNodeLink("qh", qh, "/NXentry/NXsample/qh");

		// validate link 'qk' to location '/NXentry/NXsample/qk
		final DataNode qk = group.getDataNode("qk");
		validateDataNodeLink("qk", qk, "/NXentry/NXsample/qk");

		// validate link 'ql' to location '/NXentry/NXsample/ql
		final DataNode ql = group.getDataNode("ql");
		validateDataNodeLink("ql", ql, "/NXentry/NXsample/ql");

		// validate link 'data' to location '/NXentry/NXinstrument/NXdetector/data
		final DataNode data = group.getDataNode("data");
		validateDataNodeLink("data", data, "/NXentry/NXinstrument/NXdetector/data");

	}
}
