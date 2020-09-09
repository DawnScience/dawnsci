package org.eclipse.dawnsci.nexus.device.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.nexus.INexusDevice;
import org.eclipse.dawnsci.nexus.INexusDeviceDecorator;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.device.INexusDeviceService;

/**
 * Implementation of {@link INexusDeviceService}.
 * 
 * @author Matthew Dickie
 */
public class NexusDeviceService implements INexusDeviceService {

	private Map<String, INexusDeviceDecorator<?>> nexusDecorators = new HashMap<>();
	
	private Map<String, INexusDevice<?>> nexusDevices = new HashMap<>();
	
	public <N extends NXobject> void register(INexusDevice<N> nexusDevice) {
		if (nexusDevice.getName() == null) {
			throw new IllegalArgumentException("the nexus device name is not set");
		}
		
		if (nexusDevice instanceof INexusDeviceDecorator<?>) {
			nexusDecorators.put(nexusDevice.getName(), (INexusDeviceDecorator<?>) nexusDevice);
		} else {
			nexusDevices.put(nexusDevice.getName(), nexusDevice);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <N extends NXobject> INexusDevice<N> getNexusDevice(String name) {
		if (nexusDevices.containsKey(name)) {
			return (INexusDevice<N>) nexusDevices.get(name);
		}
		
		throw new IllegalArgumentException("Cannot find nexus device with name: " + name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <N extends NXobject> INexusDeviceDecorator<N> getDecorator(String name) {
		if (nexusDecorators.containsKey(name)) {
			return (INexusDeviceDecorator<N>) nexusDecorators.get(name);
		}
		
		throw new IllegalArgumentException("Cannot find nexus decorator with name: " + name);
	}

	@Override
	public <N extends NXobject> INexusDevice<N> decorateNexusDevice(INexusDevice<N> nexusDevice) {
		final String deviceName = nexusDevice.getName();
		if (!nexusDecorators.containsKey(deviceName)) {
			return nexusDevice;
		}
		
		// Get the existing nexus device if there is one
		@SuppressWarnings("unchecked")
		final INexusDevice<N> existingNexusDevice = (INexusDevice<N>) nexusDecorators.get(deviceName);
		if (existingNexusDevice instanceof INexusDeviceDecorator<?>) {
			// If it is a decorator, set the decorated nexus device to be the one passed in.
			((INexusDeviceDecorator<N>) existingNexusDevice).setDecorated(nexusDevice);
			return existingNexusDevice;
		}
		
		// otherwise use the passed in nexusDevice, replacing the old one in the caches 
		return nexusDevice;
	}

}