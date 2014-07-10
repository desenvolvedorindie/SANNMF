package br.com.wfcreations.sannf.structure;

import java.util.ArrayList;
import java.util.List;

public class AbstractLayer implements ILayer {

	private static final long serialVersionUID = 1L;

	protected AbstractNeuralNetwork parentNeuralNetwork;

	protected List<INeuron> neurons = new ArrayList<INeuron>();

	public AbstractNeuralNetwork getParentNeuralNetwork() {
		return parentNeuralNetwork;
	}

	public AbstractLayer setParentNeuralNetwork(AbstractNeuralNetwork parentNeuralNetwork) {
		this.parentNeuralNetwork = parentNeuralNetwork;
		return this;
	}

	public List<INeuron> getNeurons() {
		return this.neurons;
	}

	public boolean addNeuron(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		return this.neurons.add(neuron);
	}

	public AbstractLayer addNeuronAt(int index, INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		this.neurons.add(index, neuron);
		return this;
	}

	public INeuron setNeuron(int index, INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		INeuron removed = this.neurons.set(index, neuron);
		removed.setParentLayer(null);
		return removed;
	}

	public boolean removeNeuron(AbstractNeuron neuron) {
		neuron.setParentLayer(null);
		return this.neurons.remove(neuron);
	}

	public INeuron removeNeuronAt(int index) {
		INeuron removed = this.neurons.remove(index);
		removed.setParentLayer(null);
		return removed;
	}

	public boolean removeAllNeurons() {
		boolean changed = this.neurons.size() > 0;
		for (INeuron neuron : this.neurons)
			neuron.setParentLayer(null);
		this.neurons.clear();
		return changed;
	}

	public INeuron getNeuronAt(int index) {
		return this.neurons.get(index);
	}

	public int indexOf(AbstractNeuron neuron) {
		return this.neurons.indexOf(neuron);
	}

	public int getNeuronsNum() {
		return this.neurons.size();
	}
}
