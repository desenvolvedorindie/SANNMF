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
package br.com.wfcreations.sannf.learning.algorithms;

import br.com.wfcreations.sannf.function.activation.DerivativeActivationFunction;
import br.com.wfcreations.sannf.function.error.MSE;
import br.com.wfcreations.sannf.neuralnetwork.NeuralNetwork;
import br.com.wfcreations.sannf.structure.Neuron;
import br.com.wfcreations.sannf.structure.Synapse;
import br.com.wfcreations.sannf.learning.ErrorCorrectionLearning;

public class DeltaRule extends ErrorCorrectionLearning {

	private static final long serialVersionUID = 1L;

	protected double learningRate = 0.1;

	public DeltaRule(NeuralNetwork network, double learnRate, boolean batchMode) {
		super(network, batchMode, new MSE());
		if (network.layersNum() != 2)
			throw new IllegalArgumentException("This algorithm only suport single-layer");
		this.setLearningRate(learnRate);
	}

	@Override
	protected void updateNetworkWeights(double[] outputError) {
		int i = 0;
		for (Neuron neuron : network.getOutputNeurons())
			this.updateNeuronWeights(neuron.setError(outputError[i++]));
	}

	protected void updateNeuronWeights(Neuron neuron) {
		for (Synapse synapse : neuron.getInputs()) {
			double weightChange = weightChange(neuron, synapse);
			if (this.batchMode)
				synapse.setWeightChange(synapse.getWeightChange() + weightChange);
			else
				synapse.setWeightChange(weightChange).incrementWeight(weightChange);
		}
	}

	protected double weightChange(Neuron neuron, Synapse synapse) {
		return this.learningRate * neuron.getError() * ((DerivativeActivationFunction) neuron.getActivationFunction()).derivative(neuron.inducedLocalField()) * synapse.input();
	}

	public double getLearningRate() {
		return learningRate;
	}

	public DeltaRule setLearningRate(double learningRate) {
		if (learningRate <= 0)
			throw new IllegalArgumentException("Must be greater than 0");
		this.learningRate = learningRate;
		return this;
	}
}