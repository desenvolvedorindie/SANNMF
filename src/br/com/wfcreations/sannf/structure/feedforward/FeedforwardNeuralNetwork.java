package br.com.wfcreations.sannf.structure.feedforward;

import br.com.wfcreations.sannf.structure.AbstractNeuralNetwork;
import br.com.wfcreations.sannf.structure.ILayer;
import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.ISynapse;

public class FeedforwardNeuralNetwork extends AbstractNeuralNetwork {

	private static final long serialVersionUID = 1L;

	protected double[] outputs = new double[0];

	protected InputNeuron[] inputNeurons;

	protected AbstractOutputNeuron[] outputNeurons;

	public AbstractNeuralNetwork setInput(double... inputVector) {
		if (inputVector.length != this.inputNeurons.length)
			throw new IllegalArgumentException("Input vector size does not match network input dimension");
		int i = 0;
		for (InputNeuron neuron : this.inputNeurons)
			neuron.setInput(inputVector[i++]);
		return this;
	}

	public double[] getOutput() {
		if (this.outputs.length != this.outputNeurons.length)
			this.outputs = new double[this.outputNeurons.length];
		for (int i = 0; i < this.outputNeurons.length; i++)
			this.outputs[i] = this.outputNeurons[i].getOutput();
		return this.outputs;
	}

	public AbstractNeuralNetwork activate() {
		for (ILayer layer : this.layers)
			if (layer instanceof ProcessorLayer)
				((ProcessorLayer) layer).activate();
		return this;
	}

	public AbstractNeuralNetwork reset() {
		for (ILayer layer : this.layers)
			if (layer instanceof ProcessorLayer)
				((ProcessorLayer) layer).reset();
		return this;
	}

	public AbstractNeuralNetwork initializeWeights(double value) {
		for (ILayer layer : this.layers)
			for (INeuron neuron : layer.getNeurons())
				if (neuron instanceof ProcessorNeuron)
					for (ISynapse synapse : ((ProcessorNeuron) neuron).getInputConnections())
						synapse.setWeight(value);
		return this;
	}

	public InputNeuron[] getInputNeurons() {
		return this.inputNeurons;
	}

	public int getInputsNum() {
		return this.inputNeurons.length;
	}

	public AbstractNeuralNetwork setInputNeurons(InputNeuron[] neurons) {
		this.inputNeurons = neurons;
		return this;
	}

	public AbstractOutputNeuron[] getOutputNeurons() {
		return this.outputNeurons;
	}

	public int getOutputsNum() {
		return this.outputNeurons.length;
	}

	public AbstractNeuralNetwork setOutputNeurons(AbstractOutputNeuron[] outputNeurons) {
		this.outputNeurons = outputNeurons;
		return this;
	}
}