/*-
 *******************************************************************************
 * Copyright (c) 2015 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Dickie - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dawnsci.nexus.builder;

import java.util.Collection;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.nexus.NXdata;
import org.eclipse.dawnsci.nexus.NXdetector;
import org.eclipse.dawnsci.nexus.NXentry;
import org.eclipse.dawnsci.nexus.NXinstrument;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.NXsubentry;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusNodeFactory;
import org.eclipse.dawnsci.nexus.builder.data.NexusDataBuilder;
import org.eclipse.dawnsci.nexus.impl.NXentryImpl;
import org.eclipse.dawnsci.nexus.validation.ValidationReport;

/**
 * A builder for building a NeXus entry ({@link NXentry}).
 */
public interface NexusEntryBuilder {

	/**
	 * Returns the wrapped {@link NXentry} object.
	 * @return wrapped {@link NXentry} object
	 */
	public NXentry getNXentry();
	
	/**
	 * Add the default groups to the entry. This method should be called before
	 * calling {@link #add(NexusObjectProvider)}. 
	 */
	public void addDefaultGroups();
	
	/**
	 * Add the nexus object for the given {@link NexusObjectProvider} provider
	 * to this entry. The nexus object (i.e. group node) returned by the
	 * {@link NexusObjectProvider#createNexusObject(NexusNodeFactory)}
	 * is added to the entry at an appropriate location.
	 * For example, the {@link NexusObjectProvider} could provide an {@link NXdetector}
	 * which would be added to the {@link NXinstrument} object in the nexus tree.
	 * @param nexusObjectProvider nexus object provider
	 * @return the nexus object created by the {@link NexusObjectProvider}
	 * @throws NexusException if the nexus object could not be added for any reason
	 */
	public <N extends NXobject> N add(NexusObjectProvider<N> nexusObjectProvider) throws NexusException;
	
	/**
	 * Adds the nexus objects for the given {@link NexusObjectProvider}s to
	 * this entry. The nexus objects (i.e. group nodes) returned by the
	 * {@link NexusObjectProvider#createNexusObject(NexusNodeFactory)} is
	 * added to the entry at an appropriate location.
	 * For example, the {@link NexusObjectProvider} could provide an {@link NXdetector}
	 * which would be added to the {@link NXinstrument} object in the nexus tree.
	 * @param nexusObjectProviders
	 * @return the nexus objects created by the {@link NexusObjectProvider}s
	 * @throws NexusException if the nexus objects could not be added for any reason
	 */
	public List<NXobject> addAll(Collection<? extends NexusObjectProvider<?>> nexusObjectProviders) throws NexusException;
	
	/**
	 * Adds the given metadata to this entry. A field is added to the wrapped
	 * {@link NXentry} (or to a child node of the appropriate type
	 * as returned by {@link NexusMetadataProvider#getCategory()}) for each
	 * metadata entry.
	 * @param metadataProvider metadata provider
	 * @throws NexusException if the metadata could not be added for any reason
	 */
	public void addMetadata(NexusMetadataProvider metadataProvider) throws NexusException;
	
	/**
	 * Performs a custom modification to the wrapped {@link NXentry}.
	 * @param modification custom modification
	 * @throws NexusException if the modification could not be performed for any reason
	 */
	public void modifyEntry(CustomNexusEntryModification modification) throws NexusException;

	/**
	 * Modifies the entry according to the given modification.
	 * @param modification modification
	 * @throws NexusException if the modification could not be performed for any reason
	 */
	public void modifyEntry(NexusEntryModification modification) throws NexusException;
	
	/**
	 * Modifies the entry according to the given modifications.
	 * @param modifications modifications
	 * @throws NexusException if the modifications could not be performed for any reason
	 */
	public void modifyEntry(Collection<NexusEntryModification> modifications) throws NexusException;
	
	/**
	 * Creates a new {@link NexusDataBuilder} wrapping a new {@link NXdata} object,
	 * added to this entry with the default name 'data'. This is equivalent to 
	 * invoking <code>newData("data")</code>.
	 * @return new {@link NexusDataBuilder}
	 * @throws NexusException if the data could not be created for any reason
	 */
	public NexusDataBuilder createDefaultData() throws NexusException;
	
	/**
	 * Creates a new {@link NexusDataBuilder} wrapping a new {@link NXdata} object
	 * added to this entry with the given name.
	 * @param name
	 * @return new {@link NexusDataBuilder}
	 * @throws NexusException if the data could not be created for any reason
	 */
	public NexusDataBuilder newData(String name) throws NexusException;
	
	/**
	 * Sets the name of the default nexus data group. This must be the name of a {@link NXdata}
	 * group within this entry, in which case {@link NXentry#setAttributeDefault(String)} 
	 * is called, otherwise a {@link NexusException} is thrown. 
	 * @param the name of the default data group
	 * @throws NexusException if a data group with the given name is not present
	 */
	public void setDefaultDataGroupName(String name) throws NexusException;
	
	/**
	 * Sets the name of the {@link NXinstrument} object within this entry.
	 * @param instrumentName instrument name
	 */
	public void setInstrumentName(String instrumentName);
	
	/**
	 * Returns the data node with the given path relative to the wrapped {@link NXentryImpl}
	 * @param relativePath
	 * @return data node  the data node at the given relative path
	 * @throws NexusException if no such node exists, or the node at the given relative
	 *   path is a data node
	 */
	public DataNode getDataNode(String relativePath) throws NexusException;
	
	/**
	 * Validates the contained {@link NXentry} and any {@link NXsubentry}s within the entry  
	 * according to their declared application definition, as determined by the fields
	 * {@link NXentry#getDefinitionScalar()} and {@link NXsubentry#getDefinitionScalar()}.
	 * @return a report detailing any validation problems 
	 * @throws NexusException if an error occurred validating the entry, note: this exception
	 * 		will not be thrown simply due to an invalid entry
	 */
	public ValidationReport validate() throws NexusException;

	/**
	 * Returns the name of the wrapped {@link NXentry} within the root node
	 * of the nexus file
	 * @return name of entry
	 */
	public abstract String getEntryName();
	
}
