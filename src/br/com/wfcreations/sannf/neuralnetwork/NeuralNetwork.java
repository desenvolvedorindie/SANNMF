/*
 * Copyright (c) 2013, Welsiton Ferreira (wfcreations@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice, this
 *  list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
 *
 *  Neither the name of the WFCreation nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.wfcreations.sannf.neuralnetwork;

import java.util.Arrays;

import br.com.wfcreations.sannf.function.weightinitialization.WeightsInitializer;
import br.com.wfcreations.sannf.structure.InputNeuron;
import br.com.wfcreations.sannf.structure.Layer;
import br.com.wfcreations.sannf.structure.Neuron;
import br.com.wfcreations.sannf.structure.Synapse;

public abstract class NeuralNetwork {

	protected Layer[] layers;

	protected double[] output;

	protected Neuron[] inputNeurons;

	protected Neuron[] outputNeurons;

	public NeuralNetwork() {
		this.layers = new Layer[0];
	}

	public NeuralNetwork addLayer(Layer layer) {
		layer.setParentNeuralNetwork(this);
		this.layers = Arrays.copyOf(layers, layers.length + 1);
		this.layers[layers.length - 1] = layer;
		return this;
	}

	public NeuralNetwork addLayerAt(int index, Layer layer) {
		layer.setParentNeuralNetwork(this);
		this.layers = Arrays.copyOf(layers, layers.length + 1);
		for (int i = layers.length - 1; i > index; i--)
			this.layers[i] = this.layers[i - 1];
		this.layers[index] = layer;
		return this;
	}

	public NeuralNetwork removeLayer(Layer layer) {
		int index = indexOf(layer);
		return removeLayerAt(index);
	}

	public NeuralNetwork removeLayerAt(int index) {
		layers[index].removeAllNeurons();
		for (int i = index; i < layers.length - 1; i++)
			layers[i] = layers[i + 1];
		layers[layers.length - 1] = null;
		if (layers.length > 0)
			layers = Arrays.copyOf(layers, layers.length - 1);
		return this;
	}

	public final Layer[] getLayers() {
		return this.layers;
	}

	public Layer getLayerAt(int index) {
		return this.layers[index];
	}

	public int indexOf(Layer layer) {
		for (int i = 0; i < this.layers.length; i++)
			if (layers[i] == layer)
				return i;
		return -1;
	}

	public int layersNum() {
		return this.layers.length;
	}

	public NeuralNetwork setInput(double... inputVector) {
		if (inputVector.length != inputNeurons.length)
			throw new IllegalArgumentException("Input vector size does not match network input dimension");
		int i = 0;
		for (Neuron neuron : this.inputNeurons)
			if (neuron instanceof InputNeuron)
				((InputNeuron) neuron).setInput(inputVector[i++]);
		return this;
	}

	public double[] getOutputs() {
		if (output == null || output.length != outputNeurons.length) {
			output = new double[outputNeurons.length];
		}
		for (int i = 0; i < outputNeurons.length; i++)
			output[i] = outputNeurons[i].getOutput();
		return output;
	}

	public NeuralNetwork activate() {
		for (Layer layer : this.layers)
			layer.activate();
		return this;
	}

	public NeuralNetwork reset() {
		for (Layer layer : this.layers)
			layer.reset();
		return this;
	}

	public NeuralNetwork initializeWeights(WeightsInitializer weightInitializer) {
		weightInitializer.randomize(this);
		return this;
	}

	public NeuralNetwork initializeWeights(double value) {
		for (Layer layer : this.layers)
			for (Neuron neuron : layer.getNeurons())
				for (Synapse synapse : neuron.getInputConnections())
					synapse.setWeight(value);
		return this;
	}

	public Neuron[] getInputNeurons() {
		return this.inputNeurons;
	}

	public int inputs() {
		return this.inputNeurons.length;
	}

	public NeuralNetwork setInputNeurons(Neuron[] neurons) {
		this.inputNeurons = neurons;
		return this;
	}

	public Neuron[] getOutputNeurons() {
		return this.outputNeurons;
	}

	public int getOutputsCount() {
		return this.outputNeurons.length;
	}

	public NeuralNetwork setOutputNeurons(Neuron[] outputNeurons) {
		this.outputNeurons = outputNeurons;
		this.output = new double[outputNeurons.length];
		return this;
	}
}