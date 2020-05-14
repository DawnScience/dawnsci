package org.eclipse.dawnsci.nexus.device;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.nexus.INexusDevice;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.NexusBaseClass;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusNodeFactory;
import org.eclipse.dawnsci.nexus.NexusScanInfo;
import org.eclipse.dawnsci.nexus.builder.NexusObjectProvider;
import org.eclipse.dawnsci.nexus.builder.NexusObjectWrapper;

/**
 * A simple metadata {@link INexusDevice} that adds metadata as scalar fields according to the
 * {@link Map} as set by {@link #setNexusMetadata(Map)}.
 * 
 * @author Matthew Dickie
 */
public class NexusMetadataDevice implements INexusDevice<NXobject> {
	
	private NexusBaseClass nexusBaseClass;
	
	private String name;
	
	private Map<String, Object> nexusMetadata = new HashMap<>();
	
	public NexusMetadataDevice(String name, NexusBaseClass nexusClass) {
		this.name = name;
		this.nexusBaseClass = nexusClass;
	}
	
	public NexusMetadataDevice() {
		// private constructor for spring instantiation
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setNexusBaseClass(NexusBaseClass nexusBaseClass) {
		this.nexusBaseClass = nexusBaseClass;
	}
	
	/**
	 * Set the nexus class to create, calls {@link #setNexusBaseClass(NexusBaseClass)} with the
	 * appropriate {@link NexusBaseClass} enum.
	 * @param nxClass
	 * @throws IllegalArgumentException
	 */
	public void setNxClass(String nxClass) throws IllegalArgumentException {
		setNexusBaseClass(NexusBaseClass.getBaseClassForName(nxClass));
	}
	
	public void setNexusMetadata(Map<String, Object> nexusMetadata) {
		this.nexusMetadata = nexusMetadata;
	}
	
	public void addNexusMetadata(String name, Object value) {
		nexusMetadata.put(name, value);
	}
	
	public Map<String, Object> getNexusMetadata() {
		return nexusMetadata;
	}
	
	@Override
	public NexusObjectProvider<NXobject> getNexusProvider(NexusScanInfo info) throws NexusException {
		final NXobject nexusObject = NexusNodeFactory.createNXobjectForClass(nexusBaseClass);
		for (Map.Entry<String, Object> entry : nexusMetadata.entrySet()) {
			nexusObject.setField(entry.getKey(), entry.getValue());
		}
		
		return new NexusObjectWrapper<NXobject>(getName(), nexusObject);
	}
	
}
