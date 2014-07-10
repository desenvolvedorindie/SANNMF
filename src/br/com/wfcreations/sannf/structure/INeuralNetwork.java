package br.com.wfcreations.sannf.structure;

import java.io.Serializable;
import java.util.List;

import br.com.wfcreations.sannf.function.weightinitialization.WeightsInitializer;

public interface INeuralNetwork extends Serializable {

	public boolean addLayer(ILayer layer);

	public INeuralNetwork addLayerAt(int index, ILayer layer);

	public boolean removeLayer(ILayer layer);

	public ILayer removeLayerAt(int index);

	public List<ILayer> getLayers();

	public ILayer getLayerAt(int index);

	public int indexOf(ILayer layer);

	public int getLayersNum();

	public INeuralNetwork initializeWeights(WeightsInitializer weightInitializer);

	public INeuralNetwork initializeWeights(double value);

}
