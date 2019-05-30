package org.eclipse.dawnsci.nexus.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.TreeFile;
import org.eclipse.dawnsci.nexus.NXbeam;
import org.eclipse.dawnsci.nexus.NXdata;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXinstrument;
import org.eclipse.dawnsci.nexus.NXmonitor;
import org.eclipse.dawnsci.nexus.NXroot;
import org.eclipse.dawnsci.nexus.NXsample;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusFile;
import org.eclipse.dawnsci.nexus.NexusNodeFactory;
import org.eclipse.dawnsci.nexus.TestUtils;
import org.eclipse.dawnsci.nexus.template.NexusTemplateConstants.ApplicationMode;
import org.eclipse.dawnsci.nexus.template.impl.NexusTemplateServiceImpl;
import org.eclipse.dawnsci.nexus.test.util.NexusTestUtils;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.IDataset;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests applying a yaml template to nexus file, either on memory or on disk.
 */
public class NexusTemplateTest {
	
	private static final String NEXUS_TESTFILES_DIR = "testfiles/dawnsci/data/nexus/";
	private static final String P45_EXAMPLE_NEXUS_FILE_PATH = NEXUS_TESTFILES_DIR + "p45-example.nxs";
	private static final String SIMPLE_EXAMPLE_NEXUS_FILE_PATH = NEXUS_TESTFILES_DIR + "simpleTestFile.nxs";
	private static final String TEMPLATE_FILE_PATH = NEXUS_TESTFILES_DIR + "test-template.yaml";
	
	private static final String BASIC_TEMPLATE = "scan/:\n  NX_class@: NXentry\n";
	private NexusTemplateServiceImpl templateService;
	
	private static String testFilesDirName;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testFilesDirName = TestUtils.generateDirectorynameFromClassname(NexusTemplateTest.class.getCanonicalName());
		TestUtils.makeScratchDirectory(testFilesDirName);
	}
	
	@Before
	public void setUp() {
		templateService = new NexusTemplateServiceImpl();
	}
	
	private NXroot applyTemplateStringToEmptyTree(String templateString) throws Exception {
		final NexusTemplate template = templateService.loadTemplateFromString(templateString);
		final NXroot root = NexusNodeFactory.createNXroot();
		template.apply(root);
		return root;
	}
	
	private NXroot applyTemplateStringToTestFile(String templateString) throws Exception {
		return applyTemplateString(templateString, SIMPLE_EXAMPLE_NEXUS_FILE_PATH);
	}
	
	private NXroot applyTemplateString(String templateString, String nexusFilePath) throws Exception {
		final NexusTemplate template = templateService.loadTemplateFromString(templateString);
		final NXroot root = loadNexusFile(nexusFilePath);
		template.apply(root);
		return root;
	}
	
	private NXroot loadNexusFile(String fileName) throws Exception {
		// Arrange - load the nexus file and set up the template
		final TreeFile sourceTree = NexusTestUtils.loadNexusFile(fileName, true);
		assertThat(sourceTree, is(notNullValue()));
		return (NXroot) sourceTree.getGroupNode();
	}

	@Test
	public void testAddGroupNode() throws Exception {
		final NXroot root = applyTemplateStringToEmptyTree(BASIC_TEMPLATE);
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
	}

	@Test(expected = NexusException.class)
	public void testAddGroupNodeNoMapping() throws Exception {
		applyTemplateStringToEmptyTree("scan/:");
	}
	
	@Test(expected = NexusException.class)
	public void testAddGroupNodeWithoutNxClass() throws Exception {
		applyTemplateStringToEmptyTree("scan/:\n  foo: bar");
	}
	
	@Test(expected = NexusException.class)
	public void testAddGroupNodeWithInvalidNxClass() throws Exception {
		applyTemplateStringToEmptyTree("scan/:\n  NX_class@: NXnonexist");
	}
	
	@Test
	public void testAddAttribute() throws Exception {
		final NXroot root = applyTemplateStringToEmptyTree(BASIC_TEMPLATE
				+ "  myattr@: blah"
				+ "\n  numattr@: 27.45");
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
		
		Attribute myattr = entry.getAttribute("myattr");
		assertThat(myattr, is(notNullValue()));
		assertThat(myattr.getValue().getString(), is(equalTo("blah")));
		
		Attribute numattr = entry.getAttribute("numattr");
		assertThat(numattr, is(notNullValue()));
		assertThat(numattr.getValue().getDouble(), is(closeTo(27.45, 1e-15)));
		// TODO: construct expected tree? 
	}
	
	@Test
	public void testAddSimpleDataNode() throws Exception {
		final NXroot root = applyTemplateStringToEmptyTree(BASIC_TEMPLATE
				+ "  title: my scan\n"
				+ "  duration: 260\n"
				+ "  collection_time: 123.2");
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
		
		// TODO assert datanode?
		assertThat(entry.getTitleScalar(), is(equalTo("my scan")));
		assertThat(entry.getDurationScalar(), is(equalTo(260L)));
		assertThat(((Dataset) entry.getDuration()).getDType(), is(Dataset.INT32));
		assertThat(entry.getCollection_timeScalar(), is(closeTo(123.2, 1e-15)));
	}
	
	@Test
	public void testAddDataNodeWithAttributes() throws Exception {
		final NXroot root = applyTemplateStringToEmptyTree(BASIC_TEMPLATE
				+ "  program_name:\n"
				+ "    value: myprogram\n"
				+ "    configuration@: foo\n"
				+ "    version@: 3");
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
		
		assertThat(entry.getProgram_nameScalar(), is(equalTo("myprogram")));
		assertThat(entry.getProgram_nameAttributeConfiguration(), is(equalTo("foo")));
		assertThat(entry.getProgram_nameAttributeVersion(), is(equalTo("3")));
		assertThat(((Dataset) entry.getDataNode("program_name").getAttribute("version").getValue()).getDType(), is(Dataset.INT32));
	}
	
	@Test(expected = NexusException.class)
	public void testAddDataNodeWithNoValue() throws Exception {
		applyTemplateStringToEmptyTree(BASIC_TEMPLATE
				+ "  program_name:\n"
				+ "    configuration@: myprogram\n"
				+ "    version@: 3");

	}
	
	@Test(expected = NexusException.class)
	public void testAddDataNodeWithIllegalChildMapping() throws Exception {
		applyTemplateStringToTestFile(BASIC_TEMPLATE
				+ "  beamflux:\n"
				+ "    value: hello\n"
				+ "    foo: bar");
	}
	
	@Test
	public void testAddDataEmptyValue() throws Exception {
		applyTemplateStringToEmptyTree(BASIC_TEMPLATE
				+ "  foo: ");
	}
	
	private Node getNode(NXroot root, String path) {
		final String[] pathSegments = path.split(Node.SEPARATOR);
		assertThat(pathSegments, Matchers.arrayWithSize(Matchers.greaterThan(1)));
		assertThat(pathSegments[0], isEmptyString());
	
		Node node = root;
		for (int i = 1; i < pathSegments.length; i++) {
			assertThat(node, is(instanceOf(GroupNode.class)));
			node = ((GroupNode) node).getNode(pathSegments[i]);
		}
		assertThat(node, is(notNullValue()));
		return node;
	}

	@Test
	public void testAddLinkToGroupNode() throws Exception {
		final NXroot root = applyTemplateStringToTestFile(BASIC_TEMPLATE
				+ "  data/: /entry/data");
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
		
		NXdata data = entry.getData("data");
		assertThat(data, is(notNullValue()));
		assertThat(data, is(sameInstance(getNode(root, "/entry/data"))));
	}
	
	@Test(expected = NexusException.class)
	public void testAddLinkToGroupNodeWithAddedAttributes() throws Exception {
		NXroot root = applyTemplateStringToTestFile(BASIC_TEMPLATE
				+ "  data:\n"
				+ "    link: /entry/data\n"
				+ "    newAttr@: hello");
		
		// Note: we decided against allowing this for now.
//		NXentry entry = root.getEntry("scan");
//		assertThat(entry, is(notNullValue()));
//		
//		// check that the data group has been linked to
//		NXdata data = entry.getData("data");
//		assertThat(data, is(notNullValue()));
//		assertThat(data, is(sameInstance(getNode(root, "/entry/data"))));
//		Attribute attr = data.getAttribute("newAttr");
//		assertThat(attr, is(notNullValue()));
//		assertThat(attr.getValue().getString(), is(equalTo("hello")));
//		
//		// check that a 'link' node hasn't been added
//		assertThat(data.getNode("link"), is(nullValue()));
	}
	
	@Test(expected = NexusException.class)
	public void testAddGroupNodeLinkWithIllegalChildMapping() throws Exception {
		final NXroot root = applyTemplateStringToTestFile(BASIC_TEMPLATE
				+ "  data:\n"
				+ "    link: /entry/data\n"
				+ "    foo: bar");
	}
	
	@Test
	public void testAddLinkToDataNode() throws Exception {
		final NXroot root = applyTemplateStringToTestFile(BASIC_TEMPLATE +
				"  beamflux: /entry/sample/beam/flux");
		
		NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));
		
		DataNode beamflux = entry.getDataNode("beamflux");
		assertThat(beamflux, is(notNullValue()));
		assertThat(beamflux, is(sameInstance(getNode(root, "/entry/sample/beam/flux"))));
	}
	
	@Test(expected = NexusException.class)
	public void testAddDataNodeLinkWithAddedAttributes() throws Exception {
		final NXroot root = applyTemplateStringToTestFile(BASIC_TEMPLATE
				+ "  beamflux:\n"
				+ "    link: /entry/sample/beam/flux\n"
				+ "    newattr@: hello");
		
//		NXentry entry = root.getEntry("scan"); // TODO remove if we're not going to support this
//		assertThat(entry, is(notNullValue()));
//		
//		DataNode beamflux = entry.getDataNode("beamflux");
//		assertThat(beamflux, is(notNullValue()));
//		assertThat(beamflux, is(sameInstance(getNode(root, "/entry/sample/beam/flux"))));
//		beamflux.getAttribute("newattr");
	}
	
	@Test
	@Ignore
	public void testAddSymbolicNodeLink() throws Exception {
		// TODO add a test with a SymbolicNode link to an external file
		Assert.fail();
	}
	
	@Test(expected = NexusException.class)
	public void testBrokenLink() throws Exception {
		applyTemplateStringToTestFile(BASIC_TEMPLATE 
				+ "  beamflux: /entry/sample/nosuchfield");
	}
	
	@Test
	@Ignore
	public void testExternalLink() throws Exception {
		Assert.fail(); // TODO
		fail("Add appropriate assertions");
	}
	
	@Test
	@Ignore
	public void testAddSubentry() throws Exception {
		Assert.fail();
		// TODO
	}
	
	@Test
	public void testApplyLargeTemplate() throws Exception {
		final NexusTemplate template = templateService.loadTemplate(TEMPLATE_FILE_PATH);
		final NXroot root = loadNexusFile(P45_EXAMPLE_NEXUS_FILE_PATH);
		
		// apply template
		template.apply(root);
		
		// The template used is roughly based on the NXscan application definition, but for a mapping scan
		checkNexusTree(root);
	}
	
	private void checkNexusTree(final NXroot root) {
		final NXentry entry = root.getEntry("scan");
		assertThat(entry, is(notNullValue()));

//		final Date startTime = entry.getStart_timeScalar(); // TODO start/end time are currently written as strings
//		assertThat(startTime, is(equalTo(Date.from(Instant.now()))));
//		final Date endTime = entry.getStart_timeScalar();
//		assertThat(endTime, is(equalTo(Date.from(Instant.now())))); // TODO fix date
		
		IDataset startTime = entry.getDataset(NXentry.NX_START_TIME);
		assertThat(startTime, is(notNullValue()));
		assertThat(((Dataset) startTime).getDType(), is(Dataset.STRING));
		assertThat(startTime.getShape(), is(equalTo(new int[0])));
		assertThat(startTime.getString(), is(equalTo("2019-05-28T17:56:40.351+01:00")));
		
		IDataset endTime = entry.getDataset(NXentry.NX_END_TIME);
		assertThat(endTime, is(notNullValue()));
		assertThat(((Dataset) endTime).getDType(), is(Dataset.STRING));
		assertThat(endTime.getShape(), is(equalTo(new int[] { 1 })));
		assertThat(endTime.getString(0), is(equalTo("2019-05-28T17:56:40.895+01:00")));

		assertThat(entry.getDefinitionScalar(), is(equalTo("NXscan")));
		
		IDataset programName = entry.getProgram_name();
		assertThat(((Dataset) programName).getDType(), is(Dataset.STRING));
		assertThat(programName.getString(), is(equalTo("gda")));
		
		DataNode programNameDataNode = entry.getDataNode(NXentry.NX_PROGRAM_NAME);
		assertThat(entry.getProgram_nameAttributeVersion(), is(equalTo("9.13")));
		assertThat(((Dataset) programNameDataNode.getAttribute(NXentry.NX_PROGRAM_NAME_ATTRIBUTE_VERSION).getValue()).getDType(),
				is(Dataset.STRING));
		assertThat(entry.getProgram_nameAttributeConfiguration(), is(equalTo("dummy")));
		
		// TODO add some attributes
		NXinstrument instrument = entry.getInstrument();
		assertThat(instrument, is(notNullValue()));
		
		NXdetector detector = instrument.getDetector();
		assertThat(detector, is(notNullValue()));
		assertThat(detector, is(sameInstance(getNode(root, "/entry/instrument/mandelbrot"))));
		DataNode detectorDataNode = detector.getDataNode(NXdetector.NX_DATA);
		assertThat(detectorDataNode, is(notNullValue()));
		IDataset detectorData = detector.getData();
		assertThat(detectorData, is(notNullValue()));
		
		NXsample sample = entry.getSample();
		assertThat(sample, is(notNullValue()));
		NXbeam beam = sample.getBeam();
		assertThat(beam, is(notNullValue()));
		IDataset dataset = beam.getDataset("extent");
		assertThat(dataset.getDouble(), is(closeTo(0.1, 1e-15)));
		assertThat(beam.getDataNode("extent"), is(sameInstance(getNode(root, "/entry/sample/beam/extent"))));
		
		NXmonitor monitor = entry.getMonitor("somemonitor"); // a per-point monitor
		assertThat(monitor, is(notNullValue()));
		assertThat(monitor.getData(), is(notNullValue()));
		assertThat(monitor.getDataNode(NXmonitor.NX_DATA),
				is(sameInstance(getNode(root, "/entry/instrument/stagex/value"))));
		
		NXdata nxData = entry.getData();
		assertThat(nxData, is(notNullValue()));
		assertThat(nxData.getDataNode(NXdata.NX_DATA), is(sameInstance(detectorDataNode)));
	}

}
