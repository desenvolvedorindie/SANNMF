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
package br.com.wfcreations.sannf.neuralnetwork.models;

import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.activation.Sign;
import br.com.wfcreations.sannf.function.input.WeightedSum;
import br.com.wfcreations.sannf.neuralnetwork.NeuralNetwork;
import br.com.wfcreations.sannf.structure.BiasNeuron;
import br.com.wfcreations.sannf.structure.InputLayer;
import br.com.wfcreations.sannf.structure.Layer;
import br.com.wfcreations.sannf.structure.utils.LayerUtils;
import br.com.wfcreations.sannf.structure.utils.SynapseUtils;

public class Perceptron extends NeuralNetwork {

	public Perceptron(int inputs, int outputs, boolean hasBias) {
		this(inputs, outputs, hasBias, new Sign());
	}

	public Perceptron(int inputs, int outputs, boolean hasBias, ActivationFunction activationFunction) {
		if (inputs < -1)
			throw new IllegalArgumentException("Inputs must be greater then 0");
		if (outputs < -1)
			throw new IllegalArgumentException("Outputs must be greater then 0");
		if (activationFunction == null)
			throw new IllegalArgumentException("Activation function can't be null");

		InputLayer inputLayer = new InputLayer(inputs, false);
		Layer outputLayer = LayerUtils.createLayer(outputs, false, new WeightedSum(), activationFunction);
		this.addLayer(inputLayer).addLayer(outputLayer);
		this.inputNeurons = inputLayer.getNeurons();
		this.outputNeurons = outputLayer.getNeurons();
		if (hasBias)
			inputLayer.addNeuronAt(new BiasNeuron(), 0);
		SynapseUtils.fullConnect(inputLayer, outputLayer);
	}
}