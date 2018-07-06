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
package org.eclipse.dawnsci.nexus;


public class ServiceHolder {
	
	private static INexusFileFactory nexusFileFactory;
	
	public static INexusFileFactory getNexusFileFactory() {
		return nexusFileFactory;
	}
	
	public void setNexusFileFactory(INexusFileFactory nexusFileFactory) {
		ServiceHolder.nexusFileFactory = nexusFileFactory;
	}

}
