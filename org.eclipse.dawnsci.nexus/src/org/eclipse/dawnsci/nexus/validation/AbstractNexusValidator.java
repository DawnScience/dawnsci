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
package org.eclipse.dawnsci.nexus.validation;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import javax.measure.Unit;
import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.nexus.NXobject;
import org.eclipse.dawnsci.nexus.NXsubentry;
import org.eclipse.dawnsci.nexus.NXtransformations;
import org.eclipse.dawnsci.nexus.NexusBaseClass;
import org.eclipse.dawnsci.nexus.NexusFile;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.IDataset;
import org.eclipse.january.metadata.MetadataType;
import org.eclipse.january.metadata.UnitMetadata;

/**
 * Abstract superclass for Nexus application definition validators.
 * The logic for validating that nodes are not null, that fields and attributes
 * have the correct type etc. are in this abstract superclass.
 * A subclass exists for each application definition, generated by XSLT. Each subclass
 * invokes the methods in this abstract superclass as appropriate according to the
 * application definition as defined by the appropriate NXDL file.
 * <p>
 * Validators should not be reused.
 */
public abstract class AbstractNexusValidator implements NexusApplicationValidator {
	
	private static final class NexusPathSegment {
		private final String name;
		private final NexusBaseClass nexusBaseClass;
		
		NexusPathSegment(String name, NexusBaseClass nexusBaseClass) {
			this.name = name;
			this.nexusBaseClass = nexusBaseClass;
		}
		
		public String getName() {
			return name;
		}
		
		public NexusBaseClass getNexusBaseClass() {
			return nexusBaseClass;
		}
		
	}

	private NXsubentry entry = null;
	
	private Map<String, Integer> globalDimensionPlaceholderValues = new HashMap<>();
	
	private Map<String, Integer> localGroupDimensionPlaceholderValues = new HashMap<>();
	
	protected void setEntry(NXsubentry entry) {
		this.entry = entry;
	}
	
	/**
	 * Throw an {@link NexusValidationException} with the given message.
	 * @param message message
	 * @throws NexusValidationException always
	 */
	protected void failValidation(final String message) throws NexusValidationException {
		if (message == null) {
			throw new NexusValidationException(null);
		} else {
			throw new NexusValidationException(message);
		}
	}
	
	/**
	 * Validates that the given condition holds, throwing an {@link NexusValidationException} with the given message otherwise
	 * @param message message
	 * @param condition condition to check
	 * @throws NexusValidationException if the condition does not hold
	 */
	protected void validate(String message, boolean condition) throws NexusValidationException {
		if (!condition) {
			failValidation(message);
		}
	}
	
	/**
	 * Validates that the given object is not <code>null</code>.
	 * @param message message
	 * @param object object to check for <code>null</code>
	 * @throws NexusValidationException if the given object is <code>null</code>
	 */
	protected void validateNotNull(String message, Object object) throws NexusValidationException {
		validate(message, object != null);
	}
	
	/**
	 * Validates that the given group is not null.
	 * @param groupName name of group
	 * @param type type of group
	 * @param groupNode group node
	 * @throws NexusValidationException
	 */
	protected void validateGroupNotNull(String groupName, Class<? extends NXobject> type, GroupNode groupNode) throws NexusValidationException {
		validateNotNull((groupName == null ? "The unnamed group " : "The group '" + groupName + "' ") + "of type " + type.getSimpleName() + " must not be null", groupNode);
	}
	
	/**
	 * Validates that the given field value is not <code>null</code>.
	 * @param fieldName name of field
	 * @param dataset the field value, an {@link IDataset}
	 * @throws NexusValidationException if the field is <code>null</code>
	 */
	protected void validateFieldNotNull(String fieldName, IDataset dataset) throws NexusValidationException {
		validateNotNull("The field " + fieldName + " must be set", dataset);
	}
	
	/**
	 * Validates that the given attribute node is not <code>null</code>.
	 * @param attributeName name of attribute
	 * @param attribute attribute 
	 * @throws NexusValidationException if the attribute is <code>null</code>
	 */
	protected void validateAttributeNotNull(String attributeName, Attribute attribute) throws NexusValidationException {
		validateNotNull("The attribute " + attributeName + " must be set", attribute);
		validateNotNull("The dataset for the attribute " + attributeName + " must be set", attribute.getValue());
	}
	
	/**
	 * Validates that an enumeration field has one of the given permitted values.
	 * @param fieldName name of the field
	 * @param dataset the field value, a {@link Dataset}
	 * @param permittedValues the permitted values
	 * @throws NexusValidationException if the value of the field is not one of the permitted values
	 */
	protected void validateFieldEnumeration(String fieldName, IDataset dataset, String... permittedValues) throws NexusValidationException {
		validateEnumeration(fieldName, "field", dataset, permittedValues);
	}
	
	/**
	 * Validates that the type of the given field is that given.
	 * @param fieldName field name
	 * @param dataset field value, an {@link IDataset}
	 * @param type expected type
	 * @throws NexusValidationException if the type of the field is not that given
	 */
	protected void validateFieldType(final String fieldName, final IDataset dataset, final NexusDataType type) throws NexusValidationException {
		type.validate(fieldName, dataset);
	}
	
	/**
	 * Validates that the given field has units consistent with the given unit category.
	 * 
	 * @param fieldName field name
	 * @param dataset field value, an {@link IDataset}
	 * @param unitCategory expected unit category
	 * @throws Exception if an unexpected exception occurs
	 * @throws NexusValidationException if the field's units are not consistent with the given unit category
	 */
	protected void validateFieldUnits(final String fieldName, final IDataset dataset,
			final NexusUnitCategory unitCategory) throws NexusValidationException {
		List<? extends MetadataType> metadata;
		try {
			metadata = dataset.getMetadata(UnitMetadata.class);
		} catch (Exception e) {
			throw new NexusValidationException("Could not get unit metadata for field '" + fieldName + "'", e);
		}
		// TODO why does getMetadata return a list? Can I assume I'm only interested in the first element?
		if (metadata == null || metadata.isEmpty() || !metadata.get(0).getClass().equals(UnitMetadata.class)) {
			failValidation("No unit metadata for field '" + fieldName + "', expected " + unitCategory);
		}
		
		if (metadata.size() > 1) {
			failValidation("Multiple unit metadata items found for field '" + fieldName + "'");
		}
		
		Unit<?> unit = ((UnitMetadata) metadata.get(0)).getUnit();
		if (!unitCategory.isCompatible(unit)) {
			failValidation("Unit " + unit + " is not compatible with the unit category " + unitCategory);
		}
	}

	/**
	 * Validates that the given field has the expected rank.
	 * @param fieldName field name
	 * @param dataset field value, an {@link IDataset}
	 * @param rank expected rank
	 * @throws NexusValidationException if the field does not have the expected rank
	 */
	protected void validateFieldRank(final String fieldName, final IDataset dataset, final int rank)
			throws NexusValidationException {
		if (dataset.getRank() != rank) {
			failValidation("The field " + fieldName + " has a rank of " + dataset.getRank() + ", expected " + rank); 
		}
	}
	
	/**
	 * Validate the dimensions of the given field.
	 * @param fieldName field name
	 * @param dataset dataset to validate
	 * @param groupName the name of the group
	 * @param dimensions the dimensions, each value must be either an integer, interpreted as the expected size of
	 *    that dimension, or a placeholder string, in which case the size of this dimension will be validated
	 *    against any previous dimension with the same placeholder string
	 * @throws NexusValidationException if a dimension did not have the expected size
	 */
	protected void validateFieldDimensions(final String fieldName,
			final IDataset dataset, String groupName, Object... dimensions)
			throws NexusValidationException {
		final int[] shape = dataset.getShape();

		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i] instanceof Integer) {
				// the dimension value to validate against in an integer specifying exactly the expected dimension size to check
				if (shape[i] != ((Integer) dimensions[i]).intValue()) {
					failValidation(MessageFormat.format("The dimension with index {0} of field ''{1}'' expected to have size {2} was {3}", 
							(i + 1), fieldName, dimensions[i], shape[i]));
				}
			} else if (dimensions[i] instanceof String) {
				// the dimension value to validate against is a string placeholder
				// if the name of the group is specified, then this is defined in the NXDL for the base class for that group type
				// otherwise the placeholder is global across the whole application
				
				// we need to check that all dimensions (across the group or application depending on whether there is a group name)
				// have the same size. To do this, if this is the first time we've seen this placeholder we store the size of the
				// current dimension. On subsequent encounters, we check that the current dimension has the same size as this
				// stored value
				final String dimensionPlaceholder = (String) dimensions[i];
				Integer expectedSize = getDimensionPlaceholderValue(dimensionPlaceholder, groupName != null, shape[i]);
				if (expectedSize != null && shape[i] != expectedSize.intValue()) {
					if (groupName != null) {
						failValidation(MessageFormat.format("The dimension with index {0} of field ''{1}'' expected to have size {2} according to symbol ''{3}'' within group {4}, was {5}",
								(i + 1), fieldName, expectedSize, dimensions[i], groupName, shape[i]));
					} else {
						failValidation(MessageFormat.format("The dimension with index {0} of field ''{1}'' expected to have size {2} according to symbol ''{3}'', was {4}",
								(i + 1), fieldName, expectedSize, dimensions[i], shape[i]));
					}
				}
			} else {
				failValidation("Dimension size value must be an Integer or String, was: " + dimensions[i].getClass().getName());
			}
		}
	}
	
	/**
	 * Validates that the type of the given attribute is that given.
	 * @param fieldName field name
	 * @param dataset field value, an {@link IDataset}
	 * @param type expected type
	 * @throws NexusValidationException if the type of the field is not that given
	 */
	protected void validateAttributeType(final String fieldName, final Attribute attribute, final NexusDataType type) throws NexusValidationException {
		type.validate(fieldName, attribute.getValue());
	}

	/**
	 * Validates that an enumeration attribute has one of the given permitted values.
	 * @param attributeName name of the attribute
	 * @param attribute the attribute
	 * @param permittedValues the permitted values
	 * @throws NexusValidationException if the value of the field is not one of the permitted values
	 */
	protected void validateAttributeEnumeration(String attributeName, Attribute attribute, String... permittedValues) throws NexusValidationException {
		validateEnumeration(attributeName, "attribute", attribute.getValue(), permittedValues);
	}
	
	protected NexusPathSegment toNexusPathSegment(String segment) {
		if (segment.contains(NexusFile.NXCLASS_SEPARATOR)) {
			// segment specifies both name and nexus class
			final String[] parts = segment.split(NexusFile.NXCLASS_SEPARATOR, 2); // max segments is 2
			return new NexusPathSegment(parts[0], NexusBaseClass.getBaseClassForName(parts[1]));
		}
		if (segment.startsWith("NX") && NexusBaseClass.getBaseClassForName(segment) != null) {
			// segment nexus class only
			return new NexusPathSegment(null, NexusBaseClass.getBaseClassForName(segment));
		}
		// segment specifies name only
		return new NexusPathSegment(segment, null);
	}
	
	protected void validateDataNodeLink(String fieldName, DataNode dataNode, String targetPath) throws NexusValidationException {
		final DataNode targetNode = getTargetNode(fieldName, targetPath);
		if (targetNode != dataNode) { // data node should be same instance as target node
			failValidation(MessageFormat.format("The field ''{0}'' is not a link to the data node at the target path ''{1}''",
					fieldName, targetPath));
		}
	}
	
	private NexusPathSegment[] parseTargetPath(String targetPath) {
		if (!targetPath.startsWith(Node.SEPARATOR)) {
			throw new IllegalArgumentException("Target path must be absolute: " + targetPath);
		}
		
		final String[] segments = targetPath.split(Node.SEPARATOR);
		// note: since path starts with separator, first element is always empty
		return Arrays.stream(segments, 1, segments.length).
			map(this::toNexusPathSegment).
			toArray(NexusPathSegment[]::new);
	}

	private DataNode getTargetNode(String fieldName, String targetPath) throws NexusValidationException {
		final NexusPathSegment[] parsedTargetPath = parseTargetPath(targetPath);
		
		// The first segment is the entry we're validatings
		final NexusPathSegment firstSegment = parsedTargetPath[0];
		final NexusBaseClass firstSegmentClass = firstSegment.getNexusBaseClass();
		if (!(firstSegmentClass == NexusBaseClass.NX_ENTRY || firstSegmentClass == NexusBaseClass.NX_SUBENTRY
				|| "entry".equals(firstSegment.getName()))) {
			throw new IllegalArgumentException("First segment of target expected to be 'entry' or 'NXentry': " + targetPath);
		}
		
		// iterate through all nodes 
		NXobject currentGroup = entry;
		for (NexusPathSegment segment : Arrays.copyOfRange(parsedTargetPath, 1, parsedTargetPath.length - 1)) {
			if (segment.getName() != null) {
				// segment specifies a group name, get group and check nexus class if specified
				final NXobject groupNode = (NXobject) currentGroup.getGroupNode(segment.getName());
				if (groupNode == null) {
					failValidation(MessageFormat.format("No group found with name ''{0}'' for field ''{1}'' with target path ''{2}''",
							segment.getName(), fieldName, targetPath));
				}
				if (segment.getNexusBaseClass() != null && segment.getNexusBaseClass() != groupNode.getNexusBaseClass()) {
					failValidation(MessageFormat.format("For field ''{0}'' for group ''{0}'' in target path ''{1}'' has unexpected nexus class ''{2}''.",
							segment.getName(), targetPath, segment.getNexusBaseClass().getJavaClass().getSimpleName()));
				}
			}
			
			// if name is not specified, nexus class must be. There should be exactly one child group of this nexus class within the parent group
			final Class<? extends NXobject> nexusClass = segment.getNexusBaseClass().getJavaClass();
			final Map<String, ? extends NXobject> childGroups = currentGroup.getChildren(nexusClass);
			if (childGroups.isEmpty()) {
				failValidation(MessageFormat.format("No such target group of type ''{0}'' in target path ''{1}'' for field ''{2}''.", nexusClass.getSimpleName(), targetPath, fieldName));
			} else if (childGroups.size() > 1) {
				failValidation(MessageFormat.format("Ambiguous target path ''{0}'' for field ''{1}'', multiple ''{2}'' groups found.", targetPath, fieldName, nexusClass.getSimpleName())); 
			}
			currentGroup = childGroups.values().iterator().next();
		}
		
		final NexusPathSegment lastSegment = parsedTargetPath[parsedTargetPath.length - 1];
		final DataNode dataNode = currentGroup.getDataNode(lastSegment.getName());
		if (dataNode == null) {
			failValidation(MessageFormat.format("No DataNode found with name ''{0}'' for field ''{1}'' with target path ''{2}''",
					lastSegment.getName(), fieldName, targetPath));
		}
		
		return dataNode;
	}
	
	
	/**
	 * Validate the given transformations. Transformations have an order, whereby the initial dependsOnStr
	 * identifies the first transformation, and thereafter each transformation is identified by the
	 * value of the <code>depends_on</code> attribute of the previous transformation. The 
	 * final transformation is identified by having <code>"."</code> as the value of its depends_on attribute. 
	 * @param transformations transformations
	 * @param dependsOnStr the name of the first transformation
	 * @throws NexusValidationException if an expected transformation does not exist
	 */
	protected void validateTransformations(final Map<String, NXtransformations> transformations, String dependsOnStr) throws NexusValidationException {
		final Set<String> encounteredTransformationNames = new HashSet<String>();
		do {
			// get the tranformation with the given name
			final NXtransformations transformation = transformations.get(dependsOnStr);
			
			// check that the transformation exists
			if (transformation == null) {
				failValidation("No such transformation: " + dependsOnStr);
			}
			
			// check we haven't already encountered this transformation, if so the
			// transformations have a circular dependency
			if (encounteredTransformationNames.contains(dependsOnStr)) {
				failValidation("Circular dependency detected in transformations, transformation '" + dependsOnStr + "' encountered for second time.");
			}
			encounteredTransformationNames.add(dependsOnStr);
			Attribute dependsOnAttr = transformation.getAttribute("depends_on");
			dependsOnStr = (dependsOnAttr == null ? null : dependsOnAttr.getFirstElement());
		} while (dependsOnStr != null && !dependsOnStr.equals(".")); // "." marks the final transformation
	}
	
	/**
	 * Clears the map of values of dimension placeholders, as these are local only to the current group.
	 */
	protected void clearLocalGroupDimensionPlaceholderValues() {
		localGroupDimensionPlaceholderValues = new HashMap<String, Integer>();
	}

	private void validateEnumeration(String nodeName, String nodeType, IDataset dataset, String... permittedValues) throws NexusValidationException {
		// note: this method assumes that the enumeration values are always strings
		if (dataset.getRank() != 1) { // TODO confirm rank for enums: 0 or 1?
			failValidation(MessageFormat.format("The enumeration {0} ''{1}'' must have a rank of 1", nodeType, nodeName));
		}
		
		// the size of the field must be 1
		if (dataset.getSize() != 1) {
			failValidation(MessageFormat.format("The enumeration {0} ''{1}'' must have a size of 1", nodeType, nodeName));
		}
		
		String value = dataset.getString(0);
		validateNotNull(MessageFormat.format(
				"The value of the enumeration {0} ''{1}'' cannot be null", nodeType, nodeName), value);
		
		boolean valuePermitted = false;
		for (String permittedValue : permittedValues) {
			if (value.equals(permittedValue)) {
				valuePermitted = true;
				break;
			}
		}
		
		if (!valuePermitted) {
			failValidation(MessageFormat.format(
					"The value of the {0} ''{1}'' must be one of the enumerated values.", nodeType, nodeName));
		}
	}

	/**
	 * A helper method to get the actual dimension size for the given placeholder string, if it exists.
	 * If this is the first occurrence of this placeholder, 
	 * @param placeholder
	 * @param local
	 * @param actualDimensionSize
	 * @return the dimension placeholder value
	 */
	private Integer getDimensionPlaceholderValue(String placeholder, boolean local, int actualDimensionSize) {
		final Integer dimensionPlaceholderValue;
		if (local) {
			dimensionPlaceholderValue = localGroupDimensionPlaceholderValues.get(placeholder);
			if (dimensionPlaceholderValue == null) {
				localGroupDimensionPlaceholderValues.put(placeholder, actualDimensionSize);
			} else {
				
			}
		} else {
			dimensionPlaceholderValue = globalDimensionPlaceholderValues.get(placeholder);
			if (dimensionPlaceholderValue == null) {
				globalDimensionPlaceholderValues.put(placeholder, actualDimensionSize);
			}
		}
		
		return dimensionPlaceholderValue;
	}
	
}
