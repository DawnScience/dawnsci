package org.eclipse.dawnsci.nexus.scan.impl;

import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.scan.NexusScanFile;
import org.eclipse.dawnsci.nexus.scan.NexusScanFileService;
import org.eclipse.dawnsci.nexus.scan.NexusScanModel;
import org.eclipse.dawnsci.nexus.scan.ScanMetadataWriter;

public class NexusScanFileServiceImpl implements NexusScanFileService {

	@Override
	public NexusScanFile newNexusScanFile(NexusScanModel nexusScanModel,
			ScanMetadataWriter metadataWriter) throws NexusException {
		return new NexusScanFileImpl(nexusScanModel, metadataWriter);
	}

}
