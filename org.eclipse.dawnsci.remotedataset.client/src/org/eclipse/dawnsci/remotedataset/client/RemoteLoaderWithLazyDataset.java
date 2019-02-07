/*
 * Copyright (c) 2019 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.remotedataset.client;

import org.eclipse.january.dataset.IDynamicDataset;

public class RemoteLoaderWithLazyDataset {
	
	private IDynamicDataset lazy;
	private ShapeSetRemoteLoader loader;
	
	public RemoteLoaderWithLazyDataset(ShapeSetRemoteLoader loader, IDynamicDataset lazy) {
		this.lazy = lazy;
		this.loader = loader;
	}

	public IDynamicDataset getLazy() {
		return lazy;
	}

	public ShapeSetRemoteLoader getLoader() {
		return loader;
	}

}
