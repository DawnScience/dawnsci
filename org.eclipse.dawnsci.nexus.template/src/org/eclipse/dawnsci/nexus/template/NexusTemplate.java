package org.eclipse.dawnsci.nexus.template;

import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.nexus.NexusException;
import org.eclipse.dawnsci.nexus.NexusFile;
import org.eclipse.dawnsci.nexus.context.NexusContext;
import org.eclipse.dawnsci.nexus.context.NexusContextFactory;

/**
 * A nexus template that can be applied to a nexus file one of two ways:<ul>
 * <li>by calling {@link #apply(String)} with a the file path of the nexus file</li>
 * <li>by calling {@link #apply(Tree)} with the {@link Tree} object at the root of an
 *   in-memory nexus tree.</li>
 * </ul>
 * 
 * @author Matthew Dickie
 */
public interface NexusTemplate {
	
	/**
	 * Applies the nexus template to the given in-memory NeXus {@link Tree}.
	 * @param nexusTree the nexus tree to apply the template to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *    note the template may have been partially applied, so the tree may have some
	 *    modifications
	 */
	public void apply(Tree nexusTree) throws NexusException;
	
	/**
	 * Create a new in-memory {@link GroupNode} according to the this nexus template.
	 * As the group node that is returned is not part of a larger nexus tree at this stage,
	 * groups and attributes cannot be copied from elsewhere in the tree, and node links
	 * are created as symbolic links rather than hard links. 
	 * @return newly created group node
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *    note the template may have been partially applied, so the tree may have some
	 *    modifications
	 */
	public GroupNode createNew() throws NexusException;
	
	/**
	 * Applies the nexus template to the given in-memory nexus object.
	 * @param nexusObject the nexus object to apply the temlate to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *    note the template may have been partially applied, so the object may have some
	 *    modifications
	 */
	public void apply(GroupNode node) throws NexusException;
	

	/**
	 * Applies the nexus template to the NeXus file at the given file path.
	 * @param nexusTree the path of the NeXus file to apply the template to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *    note the template may have been partially applied, so the file may have some
	 *    modifications
	 */
	public void apply(String nexusFilePath) throws NexusException;

	/**
	 * Applies the nexus template to the nexus file represented by the given {@link NexusFile} object.
	 * The file should already be open for writing.
	 * @param nexusFile the nexus file to apply the template to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *   note the template may have been partially applied, so the file may have some
	 *   modifications
	 */
	public void apply(NexusFile file) throws NexusException;

	/**
	 * Applies the nexus template to the given {@link GroupNode} within the given {@link NexusFile}.
	 * The file should already be open for writing.
	 * @param nexusFile the nexus file containing the node to apply the template to
	 * @param groupNode the group node to apply the template to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *   note the template may have been partially applied, so the file may have some
	 *   modifications
	 */
	public void apply(NexusFile nexusFile, GroupNode groupNode) throws NexusException;

	/**
	 * Applies the given template to the given {@link NexusContext}. A context can be created
	 * using {@link NexusContextFactory} and encapsulates whether a template is being applied to an
	 * in-memory nexus tree versus and on-disk nexus file, and where in the tree/file to apply the
	 * template.<p>
	 * There is only need to use this method if you already have a context. Otherwise call one of the
	 * other {@code apply} methods in this interface. 
	 * 
	 * @param context the context to apply the template to
	 * @throws NexusException if the template could not be fully applied for any reason,
	 *   note the template may have been partially applied, so the tree/file may have some
	 *   modifications
	 */
	public void apply(NexusContext context) throws NexusException;

}
