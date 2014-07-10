package br.com.wfcreations.sannf.structure;

import java.io.Serializable;

public interface INeuron extends Serializable {

	public ILayer getParentLayer();

	public INeuron setParentLayer(AbstractLayer parentLayer);
}
