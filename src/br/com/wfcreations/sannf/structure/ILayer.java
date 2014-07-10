package br.com.wfcreations.sannf.structure;

import java.io.Serializable;
import java.util.List;

public interface ILayer extends Serializable {

	public AbstractNeuralNetwork getParentNeuralNetwork();

	public AbstractLayer setParentNeuralNetwork(AbstractNeuralNetwork parentNeuralNetwork);

	public List<INeuron> getNeurons();

	public boolean addNeuron(INeuron neuron);

	public ILayer addNeuronAt(int index, INeuron neuron);

	public INeuron setNeuron(int index, INeuron neuron);

	public boolean removeNeuron(AbstractNeuron neuron);

	public INeuron removeNeuronAt(int index);

	public boolean removeAllNeurons();

	public INeuron getNeuronAt(int index);

	public int indexOf(AbstractNeuron neuron);

	public int getNeuronsNum();
}
