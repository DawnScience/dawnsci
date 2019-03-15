package org.eclipse.dawnsci.remotedataset.client;

public class ShapeSetRemoteLoader extends RemoteLoader {

	private static final long serialVersionUID = -2064894317874333376L;

	private int[] currentShape;
	
	public ShapeSetRemoteLoader(URLBuilder urlBuilder, int[] currentShape) {
		super(urlBuilder);
		this.currentShape = currentShape;
	}
	
	@Override
	public int[] refreshShape() {
		return currentShape;
	}

	public void setShape(int[] shape) {
		currentShape =shape;
	}
}
