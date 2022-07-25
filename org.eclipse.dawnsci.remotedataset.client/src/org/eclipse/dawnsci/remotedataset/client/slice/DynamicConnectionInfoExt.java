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
package org.eclipse.dawnsci.remotedataset.client.slice;

import org.eclipse.january.metadata.DynamicConnectionInfo;

/**
 * Not for use outside package. Bridges SliceClient to DynamicConnectionInfo
 * 
 * @author Matthew Gerring
 *
 */
public class DynamicConnectionInfoExt extends DynamicConnectionInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8129875989888252784L;
	
	private transient SliceClient<?> client;

	public DynamicConnectionInfoExt(SliceClient<?> client) {
		this.client = client;
	}
	@Override
	public boolean isConnected() {
		return !client.isFinished();
	}

	@Override
	public long getReceivedCount() {
		return client.getReceivedImageCount();
	}

	@Override
	public long getDroppedCount() {
		return client.getDroppedImageCount();
	}

}