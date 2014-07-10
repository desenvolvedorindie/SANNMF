package br.com.wfcreations.sannf.structure;

public abstract class AbstractNeuron implements INeuron {

	private static final long serialVersionUID = 1L;

	protected AbstractLayer parentLayer;

	public ILayer getParentLayer() {
		return this.parentLayer;
	}

	public AbstractNeuron setParentLayer(AbstractLayer parentLayer) {
		this.parentLayer = parentLayer;
		return this;
	}
}