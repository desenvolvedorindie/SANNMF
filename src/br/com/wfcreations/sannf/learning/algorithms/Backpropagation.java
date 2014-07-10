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
import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.feedforward.AbstractOutputNeuron;
import br.com.wfcreations.sannf.structure.feedforward.ErrorNeuron;
import br.com.wfcreations.sannf.structure.feedforward.FeedforwardNeuralNetwork;
import br.com.wfcreations.sannf.structure.feedforward.ProcessorNeuron;

public class Backpropagation extends DeltaRule {

	private static final long serialVersionUID = 1L;

	public Backpropagation(FeedforwardNeuralNetwork network, double learnRate, boolean batchMode) {
		super(network, learnRate, batchMode);
	}

	@Override
	protected void updateNetworkWeights(double[] outputError) {
		this.calculateErrorAndUpdateOutputNeurons(outputError);
		this.calculateErrorAndUpdateHiddenNeurons();
	}

	protected void calculateErrorAndUpdateOutputNeurons(double[] outputError) {
		int i = 0;
		ErrorNeuron errorNeuron;
		for (AbstractOutputNeuron neuron : ((FeedforwardNeuralNetwork) network).getOutputNeurons()) {
			if (neuron instanceof ErrorNeuron) {
				errorNeuron = (ErrorNeuron) neuron;
				if (outputError[i] == 0) {
					errorNeuron.setError(0);
					i++;
					continue;
				}

				DerivativeActivationFunction transferFunction = (DerivativeActivationFunction) errorNeuron.getActivationFunction();
				errorNeuron.setError(outputError[i] * transferFunction.derivative(errorNeuron.getInducedLocalField()));
				this.updateNeuronWeights(errorNeuron);
				i++;
			}
		}
	}

	protected void calculateErrorAndUpdateHiddenNeurons() {
		for (int i = network.getLayersNum() - 2; i > 0; i--)
			for (INeuron neuron : network.getLayerAt(i).getNeurons())
				if (neuron instanceof ErrorNeuron) {
					ErrorNeuron errorNeuron = (ErrorNeuron) neuron;
					this.updateNeuronWeights(errorNeuron.setError(this.calculateHiddenNeuronError(errorNeuron)));
				}
	}

	protected double calculateHiddenNeuronError(ProcessorNeuron neuron) {
		double deltaSum = 0d;
		for (int i = 1; i < neuron.getOutputsNum(); i++)
			deltaSum += ((ErrorNeuron) neuron.getOutputConnectionAt(i).getPostsynaptic()).getError() * neuron.getOutputConnectionAt(i).getWeight();
		DerivativeActivationFunction transferFunction = (DerivativeActivationFunction) neuron.getActivationFunction();
		return transferFunction.derivative(neuron.getInducedLocalField()) * deltaSum;
	}
}