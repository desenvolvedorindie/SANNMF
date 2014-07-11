/*
 * Copyright (c) Welsiton Ferreira (wfcreations@gmail.com)
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
package br.com.wfcreations.sannmf.structure;

import java.util.List;
import java.util.Vector;

public class AbstractLayer implements ILayer {

	private static final long serialVersionUID = 1L;

	protected INeuralNetwork parentNeuralNetwork;

	protected List<INeuron> neurons = new Vector<INeuron>();

	@Override
	public INeuralNetwork getParentNeuralNetwork() {
		return parentNeuralNetwork;
	}

	@Override
	public AbstractLayer setParentNeuralNetwork(INeuralNetwork parentNeuralNetwork) {
		this.parentNeuralNetwork = parentNeuralNetwork;
		return this;
	}

	@Override
	public List<INeuron> getNeurons() {
		return this.neurons;
	}

	@Override
	public boolean addNeuron(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		return this.neurons.add(neuron);
	}

	@Override
	public AbstractLayer addNeuronAt(int index, INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		this.neurons.add(index, neuron);
		return this;
	}

	@Override
	public INeuron setNeuron(int index, INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		neuron.setParentLayer(this);
		INeuron removed = this.neurons.set(index, neuron);
		removed.setParentLayer(null);
		return removed;
	}

	@Override
	public boolean removeNeuron(INeuron neuron) {
		neuron.setParentLayer(null);
		return this.neurons.remove(neuron);
	}

	@Override
	public INeuron removeNeuronAt(int index) {
		INeuron removed = this.neurons.remove(index);
		removed.setParentLayer(null);
		return removed;
	}

	@Override
	public boolean removeAllNeurons() {
		boolean changed = this.neurons.size() > 0;
		for (INeuron neuron : this.neurons)
			neuron.setParentLayer(null);
		this.neurons.clear();
		return changed;
	}

	@Override
	public INeuron getNeuronAt(int index) {
		return this.neurons.get(index);
	}

	@Override
	public int indexOf(INeuron neuron) {
		return this.neurons.indexOf(neuron);
	}

	@Override
	public int getNeuronsNum() {
		return this.neurons.size();
	}
}