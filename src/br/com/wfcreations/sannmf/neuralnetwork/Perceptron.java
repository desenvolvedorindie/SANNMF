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
package br.com.wfcreations.sannmf.neuralnetwork;

import br.com.wfcreations.sannmf.function.activation.HardLimit;
import br.com.wfcreations.sannmf.function.activation.IActivationFunction;
import br.com.wfcreations.sannmf.function.input.WeightedSum;
import br.com.wfcreations.sannmf.structure.feedforward.BiasNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.FeedforwardNeuralNetwork;
import br.com.wfcreations.sannmf.structure.feedforward.IOutputtedNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.InputLayer;
import br.com.wfcreations.sannmf.structure.feedforward.InputNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.ProcessorLayer;
import br.com.wfcreations.sannmf.utils.LayerUtils;
import br.com.wfcreations.sannmf.utils.SynapseUtils;

public class Perceptron extends FeedforwardNeuralNetwork {

	private static final long serialVersionUID = 1L;

	public Perceptron(int inputs, int outputs, boolean hasBias) {
		this(inputs, outputs, hasBias, new HardLimit());
	}

	public Perceptron(int inputs, int outputs, boolean hasBias, IActivationFunction activationFunction) {
		if (inputs < -1)
			throw new IllegalArgumentException("Inputs must be greater then 0");
		if (outputs < -1)
			throw new IllegalArgumentException("Outputs must be greater then 0");
		if (activationFunction == null)
			throw new IllegalArgumentException("Activation function can't be null");

		InputLayer inputLayer = LayerUtils.createInputLayer(inputs, false);
		ProcessorLayer outputLayer = LayerUtils.createProcessorLayerWithErrorNeuron(outputs, false, new WeightedSum(), activationFunction);

		this.addLayer(inputLayer);
		this.addLayer(outputLayer);

		this.inputNeurons = inputLayer.getNeurons().toArray(new InputNeuron[inputLayer.getNeuronsNum()]);
		this.outputNeurons = outputLayer.getNeurons().toArray(new IOutputtedNeuron[outputLayer.getNeuronsNum()]);

		if (hasBias)
			inputLayer.addNeuronAt(0, new BiasNeuron());

		SynapseUtils.fullConnect(inputLayer, outputLayer);
	}
}