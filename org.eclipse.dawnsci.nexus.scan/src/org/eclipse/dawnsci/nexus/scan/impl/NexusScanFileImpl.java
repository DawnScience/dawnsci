/*-
 * Copyright © 2020 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package org.eclipse.dawnsci.nexus.scan.impl;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.eclipse.dawnsci.nexus.scan.NexusScanConstants.SYSTEM_PROPERTY_NAME_VALIDATE_NEXUS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.nexus.INexusDevice;
import org.eclipse.dawnsci.nexus.NXcollection;
import org.eclipse.dawnsci.nexus.NXdata;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusScanInfo;
import org.eclipse.dawnsci.nexus.NexusScanInfo.ScanRole;
import org.eclipse.dawnsci.nexus.builder.CustomNexusEntryModification;
import org.eclipse.dawnsci.nexus.builder.NexusBuilderFile;
import org.eclipse.dawnsci.nexus.builder.NexusEntryBuilder;
import org.eclipse.dawnsci.nexus.builder.NexusFileBuilder;
import org.eclipse.dawnsci.nexus.builder.NexusMetadataProvider;
import org.eclipse.dawnsci.nexus.builder.NexusObjectProvider;
import org.eclipse.dawnsci.nexus.builder.data.AxisDataDevice;
import org.eclipse.dawnsci.nexus.builder.data.DataDevice;
import org.eclipse.dawnsci.nexus.builder.data.DataDeviceBuilder;
import org.eclipse.dawnsci.nexus.builder.data.NexusDataBuilder;
import org.eclipse.dawnsci.nexus.builder.data.PrimaryDataDevice;
import org.eclipse.dawnsci.nexus.device.INexusDeviceService;
import org.eclipse.dawnsci.nexus.device.SimpleNexusDevice;
import org.eclipse.dawnsci.nexus.scan.IDefaultDataGroupCalculator;
import org.eclipse.dawnsci.nexus.scan.NexusScanFile;
import org.eclipse.dawnsci.nexus.scan.NexusScanMetadataWriter;
import org.eclipse.dawnsci.nexus.scan.NexusScanModel;
import org.eclipse.dawnsci.nexus.scan.ServiceHolder;
import org.eclipse.dawnsci.nexus.template.NexusTemplate;
import org.eclipse.dawnsci.nexus.template.NexusTemplateService;
import org.eclipse.dawnsci.nexus.validation.ValidationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An instance of this class knows how to build a {@link NexusBuilderFile} for a given {@link NexusScanModel}.
 */
class NexusScanFileImpl implements NexusScanFile {
	
	private static final Logger logger = LoggerFactory.getLogger(NexusScanFileImpl.class);

	private final NexusScanModel nexusScanModel;
	private final String filePath;
	private NexusFileBuilder fileBuilder;
	private NexusBuilderFile nexusBuilderFile;

	// we need to cache various things as they are used more than once
	/**
	 * A list of the nexus object providers for each category of device.
	 */
	private Map<ScanRole, List<NexusObjectProvider<?>>> nexusObjectProviders = null;

	/**
	 * A map from nexus object provider to the axis data device for that.
	 * This is used for devices added to an NXdata group other than the primary device
	 * (the one that supplies the signal field.)
	 */
	private Map<NexusObjectProvider<?>, AxisDataDevice<?>> dataDevices = new HashMap<>();

	/**
	 * A map from scannable name to the index of the scan for that scannable,
	 * or <code>null</code> if none
	 */
	private Map<String, Integer> defaultAxisIndexForScannable = null;

	private final INexusDeviceService nexusDeviceService = ServiceHolder.getNexusDeviceService();

	NexusScanFileImpl(NexusScanModel nexusScanModel) throws NexusException {
		this.nexusScanModel = nexusScanModel;
		this.filePath = nexusScanModel.getFilePath();

		if (fileBuilder != null) {
			throw new IllegalStateException("The nexus file has already been created");
		}

		// convert this to a map of nexus object providers for each type
		nexusObjectProviders = extractNexusProviders();
		if (nexusScanModel.getMetadataWriter() instanceof NexusScanMetadataWriter) {
			((NexusScanMetadataWriter) nexusScanModel.getMetadataWriter()).setNexusObjectProviders(nexusObjectProviders);
		}
	}
	
	@Override
	public NexusScanModel getNexusScanModel() {
		return nexusScanModel;
	}

	@Override
	public String getFilePath() {
		return filePath;
	}

	@Override
	public Map<ScanRole, List<NexusObjectProvider<?>>> getNexusObjectProviders() {
		return nexusObjectProviders;
	}

	/**
	 *
	 * @return the paths of all the external files to which we will be writing.
	 */
	public Set<String> getExternalFilePaths() {
		Set<String> paths = new HashSet<>();
		// Big looking loop over small number of things.
		for (List<NexusObjectProvider<?>> provList : nexusObjectProviders.values()) {
			for (NexusObjectProvider<?> prov : provList) {
				paths.addAll(prov.getExternalFileNames());
			}
		}
		return paths;
	}

	public void createNexusFile(boolean async) throws NexusException {
		createNexusFile(async, true);
	}
	
	public void createNexusFile(boolean async, boolean useSwmr) throws NexusException {
		// We use the new nexus framework to join everything up into the scan
		// Create a builder
		fileBuilder = ServiceHolder.getNexusBuilderFactory().newNexusFileBuilder(nexusScanModel.getFilePath());

		try {
			createEntry(fileBuilder);
			applyTemplates(fileBuilder.getNexusTree());
			
			// create the file from the builder and open it
			nexusBuilderFile = fileBuilder.createFile(async, useSwmr);
			nexusBuilderFile.openToWrite();
		} catch (NexusException e) {
			throw new NexusException("Cannot create nexus file", e);
		}
	}
	
	@Override
	public int flush() throws NexusException {
		return nexusBuilderFile.flush();
	}

	/**
	 * Writes scan finished and closes the wrapped nexus file.
	 * @throws NexusException 
	 */
	public void scanFinished() throws NexusException {
		validate(fileBuilder);
		nexusBuilderFile.close();
	}

	private void validate(NexusFileBuilder fileBuilder) throws NexusException {
		if (Boolean.getBoolean(SYSTEM_PROPERTY_NAME_VALIDATE_NEXUS) || Boolean.getBoolean("GDA/gda."+SYSTEM_PROPERTY_NAME_VALIDATE_NEXUS)) {
			final ValidationReport validationReport =
					ServiceHolder.getNexusValidationService().validateNexusTree(fileBuilder.getNexusTree());
			if (validationReport.isError()) { // note we log an error rather than throwing an exception if the nexus file is invalid
				logger.error("The nexus file {} is invalid, see log for details", filePath);
			}
		}
	}

	private void applyTemplates(Tree tree) throws NexusException {
		final NexusTemplateService templateService = ServiceHolder.getTemplateService();
		for (String templateFilePath : nexusScanModel.getTemplateFilePaths()) {
			final NexusTemplate template = templateService.loadTemplate(templateFilePath);
			template.apply(tree);
		}
	}

	private Map<ScanRole, List<NexusObjectProvider<?>>> extractNexusProviders() throws NexusException {
		logger.trace("extractNexusProviders() called");
		final NexusScanInfo scanInfo = nexusScanModel.getNexusScanInfo();
		final Map<ScanRole, List<INexusDevice<?>>> nexusDevices = getNexusDevices(scanInfo);

		final Map<ScanRole, List<NexusObjectProvider<?>>> nexusObjectProviders = new EnumMap<>(ScanRole.class);
		for (ScanRole deviceType: ScanRole.values()) {
			logger.trace("extractNexusProviders deviceType={}", deviceType);
			final List<INexusDevice<?>> nexusDevicesForType = nexusDevices.getOrDefault(deviceType, emptyList());
			final List<NexusObjectProvider<?>> nexusObjectProvidersForType =
					new ArrayList<>(nexusDevicesForType.size());
			for (INexusDevice<?> nexusDevice : nexusDevicesForType) {
				logger.trace("extractNexusProviders nexusDevice={}", nexusDevice);
				try {
					final NexusObjectProvider<?> nexusProvider = nexusDevice.getNexusProvider(scanInfo);
					if (nexusProvider != null) {
						logger.trace("extractNexusProviders nexusProvider={}", nexusProvider);
						nexusObjectProvidersForType.add(nexusProvider);
					}
				} catch (NexusException e) {
					if (deviceType == ScanRole.MONITOR_PER_SCAN) {
						// A failure to get a Nexus object for a per-scan monitor is not regarded as fatal,
						// so just warn the user.
						logger.warn("Cannot create per-scan monitor {}: {}", nexusDevice.getName(), e.getMessage());
					} else {
						// For all other types of device throw an exception
						throw new NexusException("Cannot create device: " + nexusDevice.getName(), e);
					}
				}
			}

			nexusObjectProviders.put(deviceType, nexusObjectProvidersForType);
		}

		return nexusObjectProviders;
	}

	private Map<ScanRole, List<INexusDevice<?>>> getNexusDevices(NexusScanInfo info) throws NexusException {
		final Map<ScanRole, List<INexusDevice<?>>> oldNexusDevices = nexusScanModel.getNexusDevices();
		final Map<ScanRole, List<INexusDevice<?>>> newNexusDevices = new EnumMap<>(ScanRole.class);

		for (Map.Entry<ScanRole, List<INexusDevice<?>>> nexusDevicesForScanRoleEntry : oldNexusDevices.entrySet()) {
			final ScanRole scanRole = nexusDevicesForScanRoleEntry.getKey();
			final List<INexusDevice<?>> oldNexusDevicesForScanRole = nexusDevicesForScanRoleEntry.getValue();

			try {
				// decorate all nexus devices, expand any multiple nexus devices in the list of devices by scan role
				for (INexusDevice<?> nexusDevice : oldNexusDevicesForScanRole) {
					for (NexusObjectProvider<?> nexusProvider : nexusDevice.getNexusProviders(info)) {
						final ScanRole actualScanRole = nexusProvider.getScanRole() != null ? nexusProvider.getScanRole() : scanRole;
						final INexusDevice<?> newNexusDevice = nexusDeviceService.decorateNexusDevice(
								new SimpleNexusDevice<>(nexusProvider));
						newNexusDevices.computeIfAbsent(actualScanRole, role -> new ArrayList<>()).add(newNexusDevice);
					}
				}
			} catch (Exception e) {
				if (e instanceof RuntimeException && e.getCause() instanceof NexusException) {
					throw (NexusException) ((RuntimeException) e).getCause();
				}
				throw new NexusException("Error getting nexus devices", e);
			}
		}

		return newNexusDevices;
	}
	
	/**
	 * Creates and populates the {@link NXentry} for the NeXus file.
	 * @param fileBuilder a {@link NexusFileBuilder}
	 * @throws NexusException
	 */
	private void createEntry(NexusFileBuilder fileBuilder) throws NexusException {
		final String entryName = nexusScanModel.getEntryName();
		final NexusEntryBuilder entryBuilder = fileBuilder.newEntry(entryName); 
		entryBuilder.addDefaultGroups();

		addScanMetadata(entryBuilder, nexusScanModel.getNexusMetadataProviders());

		// add all the devices to the entry. Per-scan monitors are added first.
		for (ScanRole deviceType : EnumSet.allOf(ScanRole.class)) {
			addDevicesToEntry(entryBuilder, deviceType);
		}
		
		// add the nexus object for the metadata entry (TODO: merge with above)?
		addMetadataDeviceToEntry(entryBuilder, nexusScanModel.getMetadataWriter());

		// create the NXdata groups
		createNexusDataGroups(entryBuilder);
	}

	private void addDevicesToEntry(NexusEntryBuilder entryBuilder, ScanRole deviceType) throws NexusException {
		entryBuilder.addAll(nexusObjectProviders.get(deviceType));

		final List<CustomNexusEntryModification> customModifications =
				nexusScanModel.getNexusDevices().get(deviceType).stream().
				map(INexusDevice::getCustomNexusModification).
				filter(Objects::nonNull).
				collect(Collectors.toList());
		for (CustomNexusEntryModification customModification : customModifications) {
			entryBuilder.modifyEntry(customModification);
		}
	}
	
	private void addMetadataDeviceToEntry(NexusEntryBuilder entryBuilder, INexusDevice<NXcollection> metadataDevice) throws NexusException {
		if (metadataDevice == null) return;
		entryBuilder.add(metadataDevice.getNexusProvider(nexusScanModel.getNexusScanInfo()));
		if (metadataDevice.getCustomNexusModification() != null) {
			entryBuilder.modifyEntry(metadataDevice.getCustomNexusModification());
		}
	}

	private void addScanMetadata(NexusEntryBuilder entryBuilder, List<NexusMetadataProvider> nexusMetadataProviders) throws NexusException {
		for (NexusMetadataProvider nexusMetadataProvider : nexusMetadataProviders) {
			entryBuilder.addMetadata(nexusMetadataProvider);
		}
	}

	/**
	 * Create the {@link NXdata} groups for the scan
	 * @param entryBuilder
	 * @throws NexusException
	 */
	private void createNexusDataGroups(final NexusEntryBuilder entryBuilder) throws NexusException {
		final Set<ScanRole> deviceTypes = EnumSet.of(ScanRole.DETECTOR, ScanRole.SCANNABLE, ScanRole.MONITOR_PER_POINT);
		if (deviceTypes.stream().allMatch(t -> nexusObjectProviders.get(t).isEmpty())) {
			throw new NexusException("The scan must include at least one device in order to write a NeXus file.");
		}

		final List<NexusObjectProvider<?>> detectors = nexusObjectProviders.get(ScanRole.DETECTOR);
		final List<NexusObjectProvider<?>> primaryDetectors = detectors.stream() // exceptionally, some detectors may not have a primary field
				.filter(det -> det.getPrimaryDataFieldName() != null)
				.collect(toList());
		if (primaryDetectors.isEmpty()) {
			// create a NXdata groups when there is no detector
			// (uses first monitor, or first scannable if there is no monitor either)
			createNXDataGroups(entryBuilder, null);
		} else {
			// create NXdata groups for each detector
			for (NexusObjectProvider<?> detector : primaryDetectors) {
				createNXDataGroups(entryBuilder, detector);
			}
		}
	}

	private void createNXDataGroups(NexusEntryBuilder entryBuilder, NexusObjectProvider<?> detector) throws NexusException {
		final List<NexusObjectProvider<?>> scannables = nexusObjectProviders.get(ScanRole.SCANNABLE);
		final List<NexusObjectProvider<?>> monitors = new LinkedList<>(nexusObjectProviders.get(ScanRole.MONITOR_PER_POINT));

		// determine the primary device - i.e. the device whose primary dataset to make the @signal field
		NexusObjectProvider<?> primaryDevice = null;
		ScanRole primaryDeviceType = null;
		if (detector != null) {
			// if there's a detector then it is the primary device
			primaryDevice = detector;
			primaryDeviceType = ScanRole.DETECTOR;
		} else if (!monitors.isEmpty()) {
			// otherwise the first monitor which has primary data file name or default axis
			// data field name set is the primary device (and therefore is not a data device)
			primaryDevice = monitors.stream()
					.filter(m -> m.getPrimaryDataFieldName() != null || m.getDefaultAxisDataFieldName() != null)
					.findFirst().orElse(null);
			if (primaryDevice != null) {
				monitors.remove(primaryDevice);
				primaryDeviceType = ScanRole.MONITOR_PER_POINT;
			}
		} 
		if (primaryDevice == null) {
			// if there are no monitors either (a rare edge case), where we use the first scannable
			// note that this scannable is also added as data device
			for (NexusObjectProvider<?> scannable : scannables) {
				if (scannable.getPrimaryDataFieldName() != null) {
					primaryDevice = scannable;
					primaryDeviceType = ScanRole.SCANNABLE;
					break;
				}
			}
				
		}
		if (primaryDevice == null) {
			throw new IllegalArgumentException("No suitable dataset could be found to use as the signal dataset of an NXdata group.");
		}

		// create the NXdata group for the primary data field
		final String primaryDeviceName = primaryDevice.getName();
		final String primaryDataFieldName = primaryDevice.getPrimaryDataFieldName();
		createNXDataGroup(entryBuilder, primaryDevice, primaryDeviceType, monitors,
				scannables, primaryDeviceName, primaryDataFieldName);

		// create an NXdata group for each additional primary data field (if any)
		for (String dataFieldName : primaryDevice.getAdditionalPrimaryDataFieldNames()) {
			String dataGroupName = primaryDeviceName + "_" + dataFieldName;
			createNXDataGroup(entryBuilder, primaryDevice, primaryDeviceType, monitors,
					scannables, dataGroupName, dataFieldName);
		}
		
		setDefaultDataGroupName(entryBuilder);
	}

	private void setDefaultDataGroupName(NexusEntryBuilder entryBuilder) throws NexusException {
		// get the list of data group names, note as GroupNodeImpl uses a LinkedHashMap for child nodes, insertion order is preserved
		final List<String> dataGroupNames = new ArrayList<>(entryBuilder.getNXentry().getAllData().keySet());
		final IDefaultDataGroupCalculator calculator = ServiceHolder.getDefaultDataGroupConfiguration();
		final String defaultDataGroupName = calculator.getDefaultDataGroupName(dataGroupNames);
		entryBuilder.setDefaultDataGroupName(defaultDataGroupName);
	}

	/**
	 * Create the {@link NXdata} groups for the given primary device.
	 * @param entryBuilder the entry builder to add to
	 * @param primaryDevice the primary device (e.g. a detector or monitor)
	 * @param primaryDeviceType the type of the primary device
	 * @param monitors the monitors
	 * @param scannedDevices the devices being scanned
	 * @param dataGroupName the name of the {@link NXdata} group within the parent {@link NXentry}
	 * @param primaryDataFieldName the name that the primary data field name
	 *   (i.e. the <code>@signal</code> field) should have within the NXdata group
	 * @throws NexusException
	 */
	private void createNXDataGroup(NexusEntryBuilder entryBuilder,
			NexusObjectProvider<?> primaryDevice,
			ScanRole primaryDeviceType,
			List<NexusObjectProvider<?>> monitors,
			List<NexusObjectProvider<?>> scannedDevices,
			String dataGroupName,
			String primaryDataFieldName)
			throws NexusException {
		if (entryBuilder.getNXentry().containsNode(dataGroupName)) {
			dataGroupName += "_data"; // append _data if the node already exists
		}

		// create the data builder and add the primary device
		final NexusDataBuilder dataBuilder = entryBuilder.newData(dataGroupName);

		// the primary device for the NXdata, e.g. a detector
		final PrimaryDataDevice<?> primaryDataDevice = createPrimaryDataDevice(
				primaryDevice, primaryDeviceType, primaryDataFieldName);
		dataBuilder.setPrimaryDevice(primaryDataDevice);

		// add the monitors (excludes the first monitor if the scan has no detectors)
		for (NexusObjectProvider<?> monitor : monitors) {
			dataBuilder.addAxisDevice(getAxisDataDevice(monitor, null));
		}

		// Create the map from scannable name to default index of that scannable in the scan
		if (defaultAxisIndexForScannable == null) {
			defaultAxisIndexForScannable = createDefaultAxisMap();
		}

		// add the scannables to the data builder
		for (NexusObjectProvider<?> scannable : scannedDevices) {
			final Integer defaultAxisForDimensionIndex = defaultAxisIndexForScannable.get(scannable.getName());
			dataBuilder.addAxisDevice(getAxisDataDevice(scannable, defaultAxisForDimensionIndex));
		}
	}

	/**
	 * Creates a map from scannable names to the index of the scan
	 * (and therefore the index of the signal dataset of each NXdata) that this
	 * scannable is the default axis for.
	 *
	 * @return map from scannable name to index that this scannable is the index for
	 */
	private Map<String, Integer> createDefaultAxisMap() {
		final Map<String, Integer> defaultAxisIndexForScannableMap = new HashMap<>();

		// Convert the list into a map from scannable name to index in scan, only including
		// scannable names which are the dimension name for exactly one index of the scan
		int dimensionIndex = 0;
		final Iterator<Set<String>> dimensionNamesIter = nexusScanModel.getDimensionNamesByIndex().iterator();
		while (dimensionNamesIter.hasNext()) {
			Collection<String> dimensionNamesForIndex = dimensionNamesIter.next();
			//need to iterate or the _indices attibute defaults to [0]
			Iterator<String> it = dimensionNamesForIndex.iterator();
			while (it.hasNext()) {
				String scannableName = it.next();
				if (defaultAxisIndexForScannableMap.containsKey(scannableName)) {
					// already seen this scannable name for another index,
					// so this scannable should not be the default axis for any index
					// note: we put null instead of removing the entry in case the scannable
					// because we don't want to add it again if the scannable is encountered again
					defaultAxisIndexForScannableMap.put(scannableName, null);
				}else {
					defaultAxisIndexForScannableMap.put(scannableName, dimensionIndex);
				}
			}

			dimensionIndex++;
		}

		return defaultAxisIndexForScannableMap;
	}

	private <N extends NXobject> PrimaryDataDevice<N> createPrimaryDataDevice(NexusObjectProvider<N> nexusObjectProvider,
			ScanRole primaryDeviceType, String signalDataFieldName) throws NexusException {

		final DataDeviceBuilder<N> dataDeviceBuilder = new DataDeviceBuilder<>(nexusObjectProvider, true);
		switch (primaryDeviceType) {
			case SCANNABLE: 
				// using scannable as primary device as well as a scannable
				// only use main data field (e.g. value for an NXpositioner)
				dataDeviceBuilder.setAxisFields();
				break;
			case MONITOR_PER_POINT:
				dataDeviceBuilder.setUseDeviceName(true);
				break;
			case DETECTOR:
				dataDeviceBuilder.setSignalField(signalDataFieldName);
				break;
			default:
				throw new IllegalArgumentException("Invalid primary device type: " + primaryDeviceType);
		}

		return (PrimaryDataDevice<N>) dataDeviceBuilder.build();
	}

	/**
	 * Gets the data device for the given {@link NexusObjectProvider},
	 * creating it if it doesn't exist.
	 *
	 * @param nexusObjectProvider nexus object provider
	 * @param scanIndex the index in the scan for the given {@link NexusObjectProvider},
	 *    or <code>null</code> if the device is not being scanned (i.e. is a monitor)
	 * @param isPrimaryDevice <code>true</code> if this is the primary device for
	 *    the scan, <code>false</code> otherwise
	 * @return the data device
	 * @throws NexusException
	 */
	private AxisDataDevice<?> getAxisDataDevice(NexusObjectProvider<?> nexusObjectProvider, Integer scanIndex) throws NexusException {
		AxisDataDevice<?> dataDevice = dataDevices.get(nexusObjectProvider);
		if (dataDevice == null) {
			dataDevice = createAxisDataDevice(nexusObjectProvider, scanIndex);
			// cache the non-primary devices for any other NXdata groups
			dataDevices.put(nexusObjectProvider, dataDevice);
		}

		return dataDevice;
	}

	/**
	 * Creates the {@link DataDevice} for the given {@link NexusObjectProvider},
	 * @param nexusObjectProvider
	 * @param scanIndex the index in the scan for the given {@link NexusObjectProvider},
	 *    or <code>null</code> if the device is not being scanned (i.e. is a monitor)
	 * @return
	 * @throws NexusException
	 */
	private <N extends NXobject> AxisDataDevice<N> createAxisDataDevice(
			NexusObjectProvider<N> nexusObjectProvider, Integer scannableIndex) throws NexusException {
		return DataDeviceBuilder.newAxisDataDevice(nexusObjectProvider, scannableIndex);
	}

}
