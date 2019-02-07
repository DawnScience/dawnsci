package org.eclipse.dawnsci.remotedataset;

public enum RemoteDatasetKeys {

	DATANODE("org.dawnsci.remote.dataNode"),
	SYMBOLICNODE("org.dawnsci.remote.symbolicNode"),
	MAXSHAPE("org.dawnsci.remote.symbolic.maxShape"),
	SHAPE("org.dawnsci.remote.symbolic.shape"),
	DTYPE("org.dawnsci.remote.symbolic.dType"),
	ELEMENTS_PER_ITEM("org.dawnsci.remote.symbolic.elementsPerItem"),
	CHUNK("org.dawnsci.remote.symbolic.chunk"),
	NAME("org.dawnsci.remote.symbolic.name");
	
	private String id;
	
	RemoteDatasetKeys(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
}
