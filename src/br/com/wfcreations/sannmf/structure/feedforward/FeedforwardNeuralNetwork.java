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
package br.com.wfcreations.sannmf.structure.feedforward;

import br.com.wfcreations.sannmf.function.weightinitialization.IWeightsInitializer;
import br.com.wfcreations.sannmf.structure.AbstractNeuralNetwork;
import br.com.wfcreations.sannmf.structure.ILayer;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.ISynapse;

public abstract class FeedforwardNeuralNetwork extends AbstractNeuralNetwork {

	private static final long serialVersionUID = 1L;

	protected double[] outputs = new double[0];

	protected InputNeuron[] inputNeurons;

	protected IOutputtedNeuron[] outputNeurons;

	public FeedforwardNeuralNetwork setInput(double... inputs) {
		if (inputs.length != this.inputNeurons.length)
			throw new IllegalArgumentException("Input vector size does not match network input dimension");
		int i = 0;
		for (InputNeuron neuron : this.inputNeurons)
			neuron.setInput(inputs[i++]);
		return this;
	}

	public double[] getOutput() {
		if (this.outputs.length != this.outputNeurons.length)
			this.outputs = new double[this.outputNeurons.length];
		for (int i = 0; i < this.outputNeurons.length; i++)
			this.outputs[i] = this.outputNeurons[i].getOutput();
		return this.outputs;
	}

	public FeedforwardNeuralNetwork activate() {
		for (ILayer layer : this.layers)
			if (layer instanceof ProcessorLayer)
				((ProcessorLayer) layer).activate();
		return this;
	}

	public FeedforwardNeuralNetwork reset() {
		for (ILayer layer : this.layers)
			if (layer instanceof ProcessorLayer)
				((ProcessorLayer) layer).reset();
		return this;
	}

	@Override
	public FeedforwardNeuralNetwork initializeWeights(double value) {
		for (ILayer layer : this.layers)
			for (INeuron neuron : layer.getNeurons())
				if (neuron instanceof IInputtedNeuron)
					for (ISynapse synapse : ((IInputtedNeuron) neuron).getInputConnections())
						synapse.setWeight(value);
		return this;
	}

	@Override
	public FeedforwardNeuralNetwork initializeWeights(IWeightsInitializer weightInitializer) {
		weightInitializer.randomize(this);
		return this;
	}

	public InputNeuron[] getInputNeurons() {
		return this.inputNeurons;
	}

	public int getInputsNum() {
		return this.inputNeurons.length;
	}

	public IOutputtedNeuron[] getOutputNeurons() {
		return this.outputNeurons;
	}

	public int getOutputsNum() {
		return this.outputNeurons.length;
	}
}