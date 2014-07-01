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
package br.com.wfcreations.sannf.structure;

import java.io.Serializable;
import java.util.Arrays;

import br.com.wfcreations.sannf.neuralnetwork.NeuralNetwork;

public class Layer implements Serializable {

	private static final long serialVersionUID = 1L;

	protected NeuralNetwork parentNeuralNetwork;

	protected Neuron[] neurons = new Neuron[0];

	public Layer() {
	}

	public Layer activate() {
		for (Neuron neuron : this.neurons)
			neuron.activate();
		return this;
	}

	public Layer reset() {
		for (Neuron neuron : this.neurons)
			neuron.reset();
		return this;
	}

	public Neuron[] getNeurons() {
		return neurons;
	}

	public Layer addNeuron(Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		this.neurons = Arrays.copyOf(neurons, neurons.length + 1);
		this.neurons[neurons.length - 1] = neuron;
		return this;
	}

	public Layer addNeuronAt(Neuron neuron, int index) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		if ((index >= neurons.length) || (index < 0))
			throw new IndexOutOfBoundsException("Neuron Index out of range: " + index);
		neuron.setParentLayer(this);
		this.neurons = Arrays.copyOf(neurons, neurons.length + 1);
		for (int i = neurons.length - 1; i > index; i--)
			this.neurons[i] = this.neurons[i - 1];
		this.neurons[index] = neuron;
		return this;
	}

	public Neuron setNeuron(int index, Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		if ((index >= neurons.length) || (index < 0))
			throw new IndexOutOfBoundsException("Neuron Index out of range: " + index);
		neuron.setParentLayer(this);
		Neuron temp = this.neurons[index];
		this.neurons[index] = neuron;
		return temp;
	}

	public boolean removeNeuron(Neuron neuron) {
		int index = indexOf(neuron);
		if (index > 0) {
			removeNeuronAt(index);
			return true;
		}
		return false;
	}

	public Neuron removeNeuronAt(int index) {
		Neuron neuron = neurons[index];
		neuron.removeAllConnections();
		neuron.setParentLayer(null);
		for (int i = index; i < neurons.length - 1; i++)
			neurons[i] = neurons[i + 1];
		neurons[neurons.length - 1] = null;
		if (neurons.length > 0)
			neurons = Arrays.copyOf(neurons, neurons.length - 1);
		return neuron;
	}

	public boolean removeAllNeurons() {
		boolean changed = neurons.length > 0;
		for (int i = 0; i < neurons.length; i++)
			neurons[i].setParentLayer(null).removeAllConnections();
		if (changed)
			this.neurons = new Neuron[0];
		return changed;
	}

	public Neuron getNeuronAt(int index) {
		return this.neurons[index];
	}

	public int indexOf(Neuron neuron) {
		for (int i = 0; i < this.neurons.length; i++)
			if (neurons[i] == neuron)
				return i;
		return -1;
	}

	public int neuronsNum() {
		return neurons.length;
	}

	public NeuralNetwork getParentNeuralNetwork() {
		return parentNeuralNetwork;
	}

	public Layer setParentNeuralNetwork(NeuralNetwork parentNeuralNetwork) {
		this.parentNeuralNetwork = parentNeuralNetwork;
		return this;
	}
}