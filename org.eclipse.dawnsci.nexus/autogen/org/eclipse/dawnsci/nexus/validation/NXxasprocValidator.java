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

import org.eclipse.dawnsci.nexus.NexusApplicationDefinition;
import org.eclipse.january.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.Attribute;

import org.eclipse.dawnsci.nexus.NXroot;
import org.eclipse.dawnsci.nexus.NXsubentry;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXprocess;
import org.eclipse.dawnsci.nexus.NXparameters;
import org.eclipse.dawnsci.nexus.NXdata;

/**
 * Validator for the application definition 'NXxasproc'.
 */
public class NXxasprocValidator extends AbstractNexusValidator implements NexusApplicationValidator {

	public NXxasprocValidator() {
		super(NexusApplicationDefinition.NX_XASPROC);
	}

	@Override
	public ValidationReport validate(NXroot root) {
		// validate unnamed child group of type NXentry (possibly multiple)
		validateUnnamedGroupOccurrences(root, NXentry.class, false, true);
		final Map<String, NXentry> allEntry = root.getAllEntry();
		for (final NXentry entry : allEntry.values()) {
			validateGroup_NXentry(entry);
		}
		return validationReport;
	}

	@Override
	public ValidationReport validate(NXentry entry) {
		validateGroup_NXentry(entry);
		return validationReport;
	}

	@Override
	public ValidationReport validate(NXsubentry subentry) {
		validateGroup_NXentry(subentry);
		return validationReport;
	}


	/**
	 * Validate unnamed group of type NXentry.
	 */
	private void validateGroup_NXentry(final NXsubentry group) {
		// set the current entry, required for validating links
		setEntry(group);

		// validate that the group is not null
		if (!(validateGroupNotNull(null, NXentry.class, group))) return;

		// validate attribute 'entry' of type NX_CHAR.
		final Attribute entry_attr = group.getAttribute("entry");
		if (!(validateAttributeNotNull("entry", entry_attr))) return;
		// validate any properties of this attribute specified in the NXDL file: type, enumeration
		validateAttributeType("entry", entry_attr, NX_CHAR);

		// validate field 'title' of type NX_CHAR.
		final ILazyDataset title = group.getLazyDataset("title");
		validateFieldNotNull("title", title);
		if (title != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("title", title, NX_CHAR);
		}

		// validate field 'definition' of type NX_CHAR.
		final ILazyDataset definition = group.getLazyDataset("definition");
		validateFieldNotNull("definition", definition);
		if (definition != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("definition", definition, NX_CHAR);
			validateFieldEnumeration("definition", definition,
					"NXxasproc");
		}

		// validate unnamed child group of type NXsample (possibly multiple)
		validateUnnamedGroupOccurrences(group, NXsample.class, false, true);
		final Map<String, NXsample> allSample = group.getAllSample();
		for (final NXsample sample : allSample.values()) {
			validateGroup_NXentry_NXsample(sample);
		}

		// validate child group 'XAS_data_reduction' of type NXprocess
		validateGroup_NXentry_XAS_data_reduction(group.getProcess("XAS_data_reduction"));

		// validate unnamed child group of type NXdata (possibly multiple)
		validateUnnamedGroupOccurrences(group, NXdata.class, false, true);
		final Map<String, NXdata> allData = group.getAllData();
		for (final NXdata data : allData.values()) {
			validateGroup_NXentry_NXdata(data);
		}
	}

	/**
	 * Validate unnamed group of type NXsample.
	 */
	private void validateGroup_NXentry_NXsample(final NXsample group) {
		// validate that the group is not null
		if (!(validateGroupNotNull(null, NXsample.class, group))) return;
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'name' of type NX_CHAR.
		final ILazyDataset name = group.getLazyDataset("name");
		validateFieldNotNull("name", name);
		if (name != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("name", name, NX_CHAR);
		}
	}

	/**
	 * Validate group 'XAS_data_reduction' of type NXprocess.
	 */
	private void validateGroup_NXentry_XAS_data_reduction(final NXprocess group) {
		// validate that the group is not null
		if (!(validateGroupNotNull("XAS_data_reduction", NXprocess.class, group))) return;

		// validate field 'program' of type NX_CHAR.
		final ILazyDataset program = group.getLazyDataset("program");
		validateFieldNotNull("program", program);
		if (program != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("program", program, NX_CHAR);
		}

		// validate field 'version' of type NX_CHAR.
		final ILazyDataset version = group.getLazyDataset("version");
		validateFieldNotNull("version", version);
		if (version != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("version", version, NX_CHAR);
		}

		// validate field 'date' of type NX_DATE_TIME.
		final ILazyDataset date = group.getLazyDataset("date");
		validateFieldNotNull("date", date);
		if (date != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("date", date, NX_DATE_TIME);
		}

		// validate child group 'parameters' of type NXparameters
		validateGroup_NXentry_XAS_data_reduction_parameters(group.getChild("parameters", NXparameters.class));
	}

	/**
	 * Validate group 'parameters' of type NXparameters.
	 */
	private void validateGroup_NXentry_XAS_data_reduction_parameters(final NXparameters group) {
		// validate that the group is not null
		if (!(validateGroupNotNull("parameters", NXparameters.class, group))) return;

		// validate field 'raw_file' of type NX_CHAR. Note: field not defined in base class.
		final ILazyDataset raw_file = group.getLazyDataset("raw_file");
		validateFieldNotNull("raw_file", raw_file);
		if (raw_file != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("raw_file", raw_file, NX_CHAR);
		}
	}

	/**
	 * Validate unnamed group of type NXdata.
	 */
	private void validateGroup_NXentry_NXdata(final NXdata group) {
		// validate that the group is not null
		if (!(validateGroupNotNull(null, NXdata.class, group))) return;
		clearLocalGroupDimensionPlaceholderValues();

		// validate field 'energy' of type NX_CHAR. Note: field not defined in base class.
		final ILazyDataset energy = group.getLazyDataset("energy");
		validateFieldNotNull("energy", energy);
		if (energy != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("energy", energy, NX_CHAR);
			validateFieldRank("energy", energy, 1);
			validateFieldDimensions("energy", energy, null, "nP");
		}

		// validate field 'data' of type NX_FLOAT. Note: field not defined in base class.
		final ILazyDataset data = group.getLazyDataset("data");
		validateFieldNotNull("data", data);
		if (data != null) {
			// validate any properties of this field specified in the NXDL file: type, units, enumeration, dimensions
			validateFieldType("data", data, NX_FLOAT);
			validateFieldRank("data", data, 1);
			validateFieldDimensions("data", data, null, "nP");
		}
	}
}
