package org.eclipse.dawnsci.nexus.validation;

import static org.eclipse.dawnsci.nexus.validation.NexusDataType.*;
import static org.eclipse.dawnsci.nexus.validation.NexusUnitCategory.*;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.nexus.NXdata;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXinstrument;
import org.eclipse.dawnsci.nexus.NXmonochromator;
import org.eclipse.dawnsci.nexus.NXroot;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXsource;

public class NXarpesValidatorExample extends AbstractNXValidator implements NXApplicationValidator {

	@Override
	public void validate(NXroot root) throws Exception {
		validateGroup_NXentry(root.getEntry());
	}
	
	/**
	 * Validate unnamed group of type NXentry.
	 */
	private void validateGroup_NXentry(NXentry group) throws Exception {
		// validate attribute 'entry'
		validateAttributeNotNull("entry", group);
		
		// validate field 'title', type NX_CHAR
		final IDataset title = group.getTitle();
		validateFieldNotNull("title", title);
		validateFieldType("title", title, NX_CHAR);
		
		// validate field 'start_time', type NX_DATE_TIME
		final IDataset start_time = group.getStart_time();
		validateFieldNotNull("start_time", start_time);
		validateFieldType("start_time", start_time, NX_DATE_TIME);
		
		// validate field definition, single permitted value 'NXarpes'
		final IDataset definition = group.getDefinition();
		validateFieldNotNull("definition", definition);
		validateFieldEnumeration("definition", definition, "NXarpes");
		
		// validate unnamed group of type 'NXinstrument'
		validateGroup_NXentry_NXinstrument(group.getInstrument());
		
		// validate unnamed group of type 'NXsample'
		validateGroupNXsample(group.getSample());
		
		// validate unnamed group of type 'NXdata'
		// TODO how to translate links?
		validateNXdataGroup(group.getData());
	}
	
	/**
	 * Validate the unnamed group of type NXinstrument
	 * @throws Exception 
	 */
	private void validateGroup_NXentry_NXinstrument(NXinstrument nxInstrument) throws Exception {
		// validate that the group is not null 
		// NOTE: only call when minOccurs is not 0
		validateGroupNotNull(null, "NXinstrument", nxInstrument);
		
		// validate the unnamed group of type NXinstrument
		validateGroup_NXentry_NXinstrument_NXsourceGroup(nxInstrument.getSource());
		
		// validate group 'monochromator' of type NXmonochromator
		validateGroup_NXentry_NXinstrument_monochromatorGroup(nxInstrument.getMonochromator());
		
		// validate group 'analyser' of type NXdetector
		validateGroup_NXentry_NXinstrument_analyser(nxInstrument.getDetector());
	}
	
	/**
	 * Validate the unnamed group of type NXsource
	 */
	private void validateGroup_NXentry_NXinstrument_NXsourceGroup(NXsource nxSource) throws NexusValidationException {
		// validate that the group is not null
		validateGroupNotNull(null, "NXsource", nxSource);
		
		// validate enumeration field 'type'
		// XXX: this enumeration is from the NXsource base class definition
		// XXX: this assumes enumerations must be strings, so we don't validate the NX_CHAR type
		final IDataset type = nxSource.getType();
		validateFieldNotNull("type", type);
		validateFieldEnumeration("type", type,
				"Spallation Neutron Source",
				"Pulsed Reactor Neutron Source",
				"Reactor Neutron Source",
				"Synchrotron X-ray Source"); // XXX: and 8 more enumerated items
		
		// validate field 'name' of type NX_CHAR
		final IDataset name = nxSource.getName();
		validateFieldNotNull("name", name);
		validateFieldType("name", name, NexusDataType.NX_CHAR);
		
		// validate enumeration field 'probe'
		// XXX: this enumeration in the application definition overrides the one in the Nxsource base class definition
		// XXX: again, we don't need to validate that the enumeration is a string
		final IDataset probe = nxSource.getProbe();
		validateFieldNotNull("probe", probe);
		validateFieldEnumeration("probe", probe, "x-ray");
	}
	
	/**
	 * Validate group monochromator of type NXmonochromator
	 * XXX: should method name be based on group name or type?
	 * Is type unique?
	 */
	private void validateGroup_NXentry_NXinstrument_monochromatorGroup(NXmonochromator monochromator) throws Exception {
		// validate that the group is not null
		validateGroupNotNull("monochromator", "NXmonochromator", monochromator);
		
		// validate field 'energy'
		final IDataset energy = monochromator.getEnergy();
		validateFieldNotNull("energy", energy);
		validateFieldType("energy", energy, NX_NUMBER);
		validateFieldUnits("energy", energy, NX_ENERGY);
	}
	
	private void validateGroup_NXentry_NXinstrument_analyser(NXdetector analyser) throws Exception {
		// validate that the group is not null
		validateGroupNotNull("analyser", "NXanalyser", analyser);
		
		// validate field 'data' of type NX NUMBER, units (from base class def) NX_ANY
		final IDataset data = analyser.getData();
		validateFieldNotNull("data", data);
		validateFieldType("data", data, NX_NUMBER);
		validateFieldUnits("data", data, NX_ANY);
		validateFieldRank("data", data, 4);
		
		// validate field 'lens_mode' of type NX_CHAR
		// TODO this field and many others do not exist in the base class - should they go in some metadata class
		final IDataset lens_mode = analyser.getDataset("lens_mode"); // NOTE: get a field not in the base class
		validateFieldNotNull("lens_mode", lens_mode);
		validateFieldType("lens_mode", lens_mode, NX_CHAR);
		
		// validate field 'acquisition_mode' of type NX_CHAR (type from base class)
		// XXX enumeration different in application def from base class)
		final IDataset acquisition_mode = analyser.getAcquisition_mode();
		validateFieldNotNull("acquisition_mode", acquisition_mode);
		validateFieldEnumeration("acquisition_mode", acquisition_mode, "swept", "fixed");
		
		// validate enumeration field 'entrace_slit_shape"
		final IDataset entrance_slit_shape = analyser.getDataset("entrace_slit_shape");
		validateFieldNotNull("entrance_slit_shape", entrance_slit_shape);
		validateFieldEnumeration("entrance_slit_shape", entrance_slit_shape, "curved", "straight");
		
		// validate field 'entrance_slit_setting' of type 'NX_NUMBER', units NX_ANY
		final IDataset entrance_slit_setting = analyser.getDataset("entrance_slit_setting");
		validateFieldNotNull("entrance_slit_setting", entrance_slit_setting);
		validateFieldType("entrance_slit_setting", entrance_slit_setting, NX_NUMBER);
		validateFieldUnits("entrance_slit_setting", entrance_slit_setting, NX_ANY);
		
		// validate field 'entrance_slit_size' of unknown type with unit 'NX_LENGTH'
		// XXX: this and subsequence fields have unit category but no type
		final IDataset entrance_slit_size = analyser.getDataset("entrance_slit_size");
		validateFieldNotNull("entrance_slit_size", entrance_slit_size);
		validateFieldUnits("entrance_slit_size", entrance_slit_size, NX_LENGTH);
		
		// validate field 'pass_energy' of unknown type with unit 'NX_ENERGY'
		final IDataset pass_energy = analyser.getDataset("pass_energy");
		validateFieldNotNull("pass_energy", pass_energy);
		validateFieldUnits("pass_energy", pass_energy, NX_ENERGY);
		
		// validate field 'time_per_channel' of unknown type with unit 'NX_TIME'
		final IDataset time_per_channel = analyser.getDataset("time_per_channel");
		validateFieldNotNull("time_per_channel", time_per_channel);
		validateFieldUnits("time_per_channel", time_per_channel, NX_TIME);
		
		// validate field 'angles' of type 'NX_NUMBER' with unit 'NX_ANGLE'
		final IDataset angles = analyser.getDataset("angles");
		validateFieldNotNull("angles", angles);
		validateFieldType("angles", angles, NX_NUMBER);
		validateFieldUnits("angles", angles, NX_ANGLE);
		
		// validate field 'energies' of type 'NX_NUMBER' with unit 'NX_ENERGY'
		final IDataset energies = analyser.getDataset("energies");
		validateFieldNotNull("energies", energies);
		validateFieldType("energies", energies, NX_NUMBER);
		validateFieldUnits("energies", energies, NX_ENERGY);
		
		// validate field 'sensor_size' of type 'NX_INT'
		final IDataset sensor_size = analyser.getDataset("sensor_size");
		validateFieldNotNull("sensor_size", sensor_size);
		validateFieldType("sensor_size", sensor_size, NX_INT);
		
		// validate field 'region_origin' of type 'NX_INT'
		final IDataset region_origin = analyser.getDataset("region_origin");
		validateFieldNotNull("region_origin", region_origin);
		validateFieldType("region_origin", region_origin, NX_INT);
		
		// validate field 'region_size' of type 'NX_INT'
		final IDataset region_size = analyser.getDataset("region_size");
		validateFieldNotNull("region_size", region_size);
		validateFieldType("region_size", region_size, NX_INT);
	}
	
	private void validateGroupNXsample(NXsample sample) throws Exception {
		// validate that the group is not null
		validateGroupNotNull(null, "NXsample", sample);

		// validate field 'name' of type 'NX_CHAR'
		final IDataset name = sample.getName();
		validateFieldNotNull("name", name);
		validateFieldType("name", name, NX_CHAR);
		
		// validate field 'temperature' of type 'NX_NUMBER' with unit 'NX_TEMPERTURE'
		final IDataset temperature = sample.getTemperature();
		validateFieldNotNull("temperature", temperature);
		validateFieldType("temperature", temperature, NX_NUMBER);
		validateFieldUnits("temperature", temperature, NX_TEMPERATURE);
		validateFieldDimensions("temperature", temperature, null, "n_Temp");
	}
	
	private void validateNXdataGroup(NXdata data) throws Exception {
		// validate that the group is not null
		validateGroupNotNull(null, "NXdata", data);
	}

}
