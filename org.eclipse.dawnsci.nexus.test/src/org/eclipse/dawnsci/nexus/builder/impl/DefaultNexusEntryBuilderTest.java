/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.dawnsci.nexus.builder.impl;

import static org.eclipse.dawnsci.nexus.NXentry.NX_ENTRY_IDENTIFIER;
import static org.eclipse.dawnsci.nexus.NXentry.NX_EXPERIMENT_IDENTIFIER;
import static org.eclipse.dawnsci.nexus.NXentry.NX_PROGRAM_NAME;
import static org.eclipse.dawnsci.nexus.NexusBaseClass.NX_SAMPLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.eclipse.dawnsci.nexus.NXcollection;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXinstrument;
import org.eclipse.dawnsci.nexus.NXpositioner;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NXsource;
import org.eclipse.dawnsci.nexus.NXsubentry;
import org.eclipse.dawnsci.nexus.NexusApplicationDefinition;
import org.eclipse.dawnsci.nexus.NexusBaseClass;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusNodeFactory;
import org.eclipse.dawnsci.nexus.builder.AbstractNexusObjectProvider;
import org.eclipse.dawnsci.nexus.builder.CustomNexusEntryModification;
import org.eclipse.dawnsci.nexus.builder.NexusEntryBuilder;
import org.eclipse.dawnsci.nexus.builder.NexusEntryModification;
import org.eclipse.dawnsci.nexus.builder.NexusObjectProvider;
import org.eclipse.dawnsci.nexus.builder.NexusObjectWrapper;
import org.eclipse.dawnsci.nexus.builder.data.NexusDataBuilder;
import org.eclipse.dawnsci.nexus.validation.ValidationReport;
import org.junit.Before;
import org.junit.Test;

public class DefaultNexusEntryBuilderTest {
	
	public static class TestPositioner extends AbstractNexusObjectProvider<NXpositioner> {
	
		public TestPositioner() {
			super("positioner", NexusBaseClass.NX_POSITIONER, NXpositioner.NX_VALUE);
		}
		
		public TestPositioner(String name) {
			super(name, NexusBaseClass.NX_POSITIONER);
		}
		
		@Override
		protected NXpositioner createNexusObject() {
			return NexusNodeFactory.createNXpositioner();
		}

	}
	
	public static class TestDetector extends AbstractNexusObjectProvider<NXdetector> {

		public TestDetector() {
			super("detector",  NexusBaseClass.NX_DETECTOR);
		}
		
		@Override
		protected NXdetector createNexusObject() {
			return NexusNodeFactory.createNXdetector();
		}
		
	}
	
	public static class TestSource extends AbstractNexusObjectProvider<NXsource> {
		
		public TestSource() {
			super("source", NexusBaseClass.NX_SOURCE);
		}
		
		@Override
		protected NXsource createNexusObject() {
			return NexusNodeFactory.createNXsource();
		}
		
	}
	
	private CustomNexusEntryModification customModification = new CustomNexusEntryModification() {
		
		@Override
		public void modifyEntry(NXentry entry) {
			entry.setField("foo", "bar");
		}
		
	};


	private NexusEntryBuilder entryBuilder;
	
	private NXentry nxEntry;
	
	@Before
	public void setUp() throws Exception {
		entryBuilder = new DefaultNexusFileBuilder("test").newEntry();
		nxEntry = entryBuilder.getNXentry();
	}
	
	@Test
	public void testGetNXentry() {
		assertThat(entryBuilder.getNXentry(), notNullValue(NXentry.class));
	}
	
	@Test
	public void testAddDefaultGroups() {
		assertThat(nxEntry.getNumberOfGroupNodes(), is(0));
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getInstrument(), notNullValue(NXinstrument.class));
		assertThat(nxEntry.getSample(), notNullValue(NXsample.class));
	}
	
	@Test
	public void testAdd() throws NexusException {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		
		TestPositioner positionerProvider = new TestPositioner();
		entryBuilder.add(positionerProvider);
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(instrument.getNumberOfGroupNodes(), is(1));
		assertThat(instrument.getPositioner(), is(sameInstance(positionerProvider.getNexusObject())));
	}
	
	@Test
	public void testAdd_namedGroup() throws NexusException {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		
		TestPositioner positionerProvider = new TestPositioner("x");
		entryBuilder.add(positionerProvider);

		assertThat(instrument.getNumberOfGroupNodes(), is(1));
		assertThat(instrument.getPositioner(), is(nullValue()));
		assertThat(instrument.getPositioner("x"), is(sameInstance(positionerProvider.getNexusObject())));
	}
	
	@Test
	public void testAdd_positionerWithCollectionName() throws NexusException {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		
		AbstractNexusObjectProvider<NXpositioner> positioner = new TestPositioner("xPos");
		positioner.setCollectionName("scannables");
		entryBuilder.add(positioner);
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(instrument.getNumberOfGroupNodes(), is(1));
		
		NXcollection collection = instrument.getCollection("scannables");
		assertNotNull(collection);
		NXpositioner xPositioner = (NXpositioner) collection.getGroupNode("xPos");
		assertNotNull(xPositioner);
		assertThat(xPositioner, is(sameInstance(positioner.getNexusObject())));
	}
	
	@Test
	public void testAdd_samplePositioner() throws NexusException {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		NXsample sample = nxEntry.getSample();
		assertThat(sample.getNumberOfGroupNodes(), is(0));
		
		TestPositioner positionerProvider = new TestPositioner();
		positionerProvider.setCategory(NX_SAMPLE);
		entryBuilder.add(positionerProvider);
		
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		assertThat(sample.getNumberOfGroupNodes(), is(1));
		assertThat(sample.getPositioner(), is(sameInstance(positionerProvider.getNexusObject())));
	}
	
	@Test
	public void testAdd_sample() throws NexusException {
		NexusObjectProvider<NXsample> sampleProvider =
				new AbstractNexusObjectProvider<NXsample>("sample", NexusBaseClass.NX_SAMPLE) {

			@Override
			protected NXsample createNexusObject() {
				return NexusNodeFactory.createNXsample();
			}
			
		};
		
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXsample oldSample = nxEntry.getSample();
		assertThat(oldSample, is(notNullValue()));
		
		entryBuilder.add(sampleProvider);
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getSample(), is(sameInstance(sampleProvider.getNexusObject())));
		assertThat(nxEntry.getSample(), is(not(sameInstance(oldSample))));
	}
	
	@Test
	public void testAddAll() throws NexusException {
		TestPositioner xPositioner = new TestPositioner("x");
		TestPositioner yPositioner = new TestPositioner("y");
		TestPositioner zPositioner = new TestPositioner("z");
		TestDetector detector = new TestDetector();
		TestPositioner samplePositioner = new TestPositioner();
		samplePositioner.setCategory(NX_SAMPLE);
		
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		NXsample sample = nxEntry.getSample();
		assertThat(sample.getNumberOfGroupNodes(), is(0));

		entryBuilder.addAll(Arrays.asList(xPositioner, yPositioner, zPositioner, samplePositioner, detector));
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(instrument.getNumberOfGroupNodes(), is(4));
		assertThat(instrument.getPositioner("x"), is(sameInstance(xPositioner.getNexusObject())));
		assertThat(instrument.getPositioner("y"), is(sameInstance(yPositioner.getNexusObject())));
		assertThat(instrument.getPositioner("z"), is(sameInstance(zPositioner.getNexusObject())));
		assertThat(instrument.getDetector(), is(sameInstance(detector.getNexusObject())));
		assertThat(sample.getPositioner(), is(sameInstance(samplePositioner.getNexusObject())));
	}
	
	@Test
	public void testAddMetadata() throws NexusException {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getNumberOfDataNodes(), is(0));
		
		MapBasedMetadataProvider metadata = new MapBasedMetadataProvider();
		metadata.addMetadataEntry(NX_ENTRY_IDENTIFIER, "12345");
		metadata.addMetadataEntry(NX_EXPERIMENT_IDENTIFIER, "myexperiment");
		metadata.addMetadataEntry(NX_PROGRAM_NAME, "GDA 8.36.0");
		metadata.addMetadataEntry("scan_command", "scan foo bar etc");
		metadata.addMetadataEntry("scan_identifier", "a3d668c0-e3c4-4ed9-b127-4a202b2b6bac");
		metadata.addMetadataEntry(NXentry.NX_TITLE, "Test Scan");

		entryBuilder.addMetadata(metadata);
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getNumberOfDataNodes(), is(6));
		assertThat(nxEntry.getEntry_identifierScalar(), is(equalTo("12345")));
		assertThat(nxEntry.getExperiment_identifierScalar(), is(equalTo("myexperiment")));
		assertThat(nxEntry.getProgram_nameScalar(), is(equalTo("GDA 8.36.0")));
		assertThat(nxEntry.getString("scan_command"), is(equalTo("scan foo bar etc")));
		assertThat(nxEntry.getString("scan_identifier"), is(equalTo("a3d668c0-e3c4-4ed9-b127-4a202b2b6bac")));
		assertThat(nxEntry.getTitleScalar(), is(equalTo("Test Scan")));
	}
	
	@Test
	public void testModifyEntry() throws Exception {
		assertThat(nxEntry.getDataNode("foo"), is(nullValue()));
		
		entryBuilder.modifyEntry(customModification);
		
		assertThat(nxEntry.getDataNode("foo").getString(), is(equalTo("bar"))); 
	}
	
	@Test
	public void testModifyEntry_nexusObjectProvider() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		
		TestPositioner positionerProvider = new TestPositioner();
		entryBuilder.modifyEntry(positionerProvider);
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(instrument.getNumberOfGroupNodes(), is(1));
		assertThat(instrument.getPositioner(), is(sameInstance(positionerProvider.getNexusObject())));
	}
	
	@Test
	public void testModifyEntry_metadataProvider() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getNumberOfDataNodes(), is(0));
		
		MapBasedMetadataProvider metadata = new MapBasedMetadataProvider();
		metadata.addMetadataEntry(NX_ENTRY_IDENTIFIER, "12345");
		metadata.addMetadataEntry(NXentry.NX_TITLE, "Test Scan");

		entryBuilder.addMetadata(metadata);
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getNumberOfDataNodes(), is(2));
		assertThat(nxEntry.getEntry_identifierScalar(), is(equalTo("12345")));
		assertThat(nxEntry.getTitleScalar(), is(equalTo("Test Scan")));
	}
	
	@Test
	public void testModifyEntry_customModification() throws Exception {
		assertThat(nxEntry.getDataNode("foo"), is(nullValue()));
		
		entryBuilder.modifyEntry((NexusEntryModification) customModification);
		
		assertThat(nxEntry.getDataNode("foo").getString(), is(equalTo("bar"))); 
	}
	
	@Test
	public void testModifyEntry_collection() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		assertThat(nxEntry.getDataNode("foo"), is(nullValue()));
		
		TestPositioner positionerProvider = new TestPositioner();
		MapBasedMetadataProvider metadata = new MapBasedMetadataProvider();
		metadata.addMetadataEntry(NX_ENTRY_IDENTIFIER, "12345");
		metadata.addMetadataEntry(NXentry.NX_TITLE, "Test Scan");

		entryBuilder.modifyEntry(Arrays.asList(positionerProvider, metadata, customModification));
		
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(instrument.getNumberOfGroupNodes(), is(1));
		assertThat(instrument.getPositioner(), is(sameInstance(positionerProvider.getNexusObject())));
		assertThat(nxEntry.getEntry_identifierScalar(), is(equalTo("12345")));
		assertThat(nxEntry.getTitleScalar(), is(equalTo("Test Scan")));
	}
	
	@Test
	public void testCreateDefaultData() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getData(), is(nullValue()));

		NexusDataBuilder dataBuilder = entryBuilder.createDefaultData();
		
		assertThat(dataBuilder, is(notNullValue()));
		assertThat(nxEntry.getNumberOfGroupNodes(), is(3));
		assertThat(nxEntry.getData(), is(notNullValue()));
		assertThat(dataBuilder.getNxData(), is(sameInstance(nxEntry.getData())));
	}

	@Test
	public void testNewData() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getData(), is(nullValue()));

		NexusDataBuilder dataBuilder = entryBuilder.newData("testdata");
		
		assertThat(dataBuilder, is(notNullValue()));
		assertThat(nxEntry.getNumberOfGroupNodes(), is(3));
		assertThat(nxEntry.getData(), is(nullValue()));
		assertThat(nxEntry.getData("testdata"), is(notNullValue()));
		assertThat(dataBuilder.getNxData(), is(sameInstance(nxEntry.getData("testdata"))));
	}
	
	@Test
	public void testSetInstrumentName() throws Exception {
		entryBuilder.addDefaultGroups();
		assertThat(nxEntry.getNumberOfGroupNodes(), is(2));
		assertThat(nxEntry.getData(), is(nullValue()));
		NXinstrument instrument = nxEntry.getInstrument();
		assertThat(instrument.getNumberOfGroupNodes(), is(0));
		assertThat(instrument.getNumberOfDataNodes(), is(0));
		assertThat(instrument.getName(), is(nullValue()));
		
		entryBuilder.setInstrumentName("i13");
		
		assertThat(instrument.getNumberOfDataNodes(), is(1));
		assertThat(instrument.getName().getString(), is(equalTo("i13")));
		assertThat(instrument.getNameScalar(), is(equalTo("i13")));
	}
	
	@Test
	public void testGetDataNode() throws Exception {
		entryBuilder.addDefaultGroups();
		nxEntry.setTitleScalar("My Entry");
		nxEntry.getInstrument().setNameScalar("My instrument");
		
		assertThat(entryBuilder.getDataNode("title").getString(), is(equalTo("My Entry")));
		assertThat(entryBuilder.getDataNode("instrument/name").getString(), is(equalTo("My instrument")));
	}
	
	@Test(expected = NexusException.class)
	public void testGetDataNode_NotExist() throws Exception {
		entryBuilder.getDataNode("doesnotexist");
	}

	@Test(expected = NexusException.class)
	public void testGetDataNode_groupNode() throws Exception {
		entryBuilder.getDataNode("groupnode");
	}
	
	@Test
	public void testValidate() throws Exception {
		// set the definition field to be 'NXtomo'. The entry will be invalid as the required groups are missing 
		nxEntry.setDefinitionScalar(NexusApplicationDefinition.NX_TOMO.toString());
		final ValidationReport validationReport = entryBuilder.validate();
		assertThat(validationReport, is(notNullValue()));
		assertThat(validationReport.isOk(), is(false));
	}
	
	@Test
	public void testValidateSubentry() throws Exception {
		entryBuilder.addDefaultGroups();
		
		final NXsubentry tomoSubentry = NexusNodeFactory.createNXsubentry();
		tomoSubentry.setDefinitionScalar(NexusApplicationDefinition.NX_TOMO.toString());
		
		// set the definition field to be 'NXtomo'. The entry will be invalid as the required groups are missing
		final NexusObjectProvider<NXsubentry> subEntryProvider = new NexusObjectWrapper<>("tomo", tomoSubentry);
		entryBuilder.add(subEntryProvider);
		
		final ValidationReport validationReport = entryBuilder.validate();
		assertThat(validationReport, is(notNullValue()));
		assertThat(validationReport.isOk(), is(false));
	}

}
