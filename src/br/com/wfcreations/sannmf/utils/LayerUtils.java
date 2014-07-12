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
package br.com.wfcreations.sannmf.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.wfcreations.sannmf.function.activation.IActivationFunction;
import br.com.wfcreations.sannmf.function.input.IInputFunction;
import br.com.wfcreations.sannmf.structure.ILayer;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.feedforward.BiasNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.ErrorNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.InputLayer;
import br.com.wfcreations.sannmf.structure.feedforward.InputNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.ProcessorLayer;
import br.com.wfcreations.sannmf.structure.feedforward.ProcessorNeuron;

public abstract class LayerUtils {

	public static InputLayer createInputLayer(int neuronsNum, boolean bias) {
		InputLayer inputLayer = new InputLayer();
		if (bias)
			inputLayer.addNeuron(new BiasNeuron());
		for (int i = 0; i < neuronsNum; i++)
			inputLayer.addNeuron(new InputNeuron());
		return inputLayer;
	}

	public static ProcessorLayer createProcessorLayerWithProcessorNeuron(int neuronsNum, boolean bias) {
		ProcessorLayer layer = new ProcessorLayer();
		if (bias) {
			layer.addNeuron(new BiasNeuron());
		}
		for (int i = 0; i < neuronsNum; i++)
			layer.addNeuron(new ProcessorNeuron());
		return layer;
	}

	public static ProcessorLayer createProcessorLayerWithProcessorNeuron(int neuronsNum, boolean bias, IInputFunction inputFunction, IActivationFunction activationFunction) {
		ProcessorLayer layer = new ProcessorLayer();
		if (bias) {
			layer.addNeuron(new BiasNeuron());
		}
		for (int i = 0; i < neuronsNum; i++)
			layer.addNeuron(new ProcessorNeuron(inputFunction, activationFunction));
		return layer;
	}

	public static ProcessorLayer createProcessorLayerWithErrorNeuron(int neuronsCount, boolean bias) {
		ProcessorLayer layer = new ProcessorLayer();
		if (bias) {
			layer.addNeuron(new BiasNeuron());
		}
		for (int i = 0; i < neuronsCount; i++)
			layer.addNeuron(new ErrorNeuron());
		return layer;
	}

	public static ProcessorLayer createProcessorLayerWithErrorNeuron(int neuronsCount, boolean bias, IInputFunction inputFunction, IActivationFunction activationFunction) {
		ProcessorLayer layer = new ProcessorLayer();
		if (bias) {
			layer.addNeuron(new BiasNeuron());
		}
		for (int i = 0; i < neuronsCount; i++)
			layer.addNeuron(new ErrorNeuron(inputFunction, activationFunction));
		return layer;
	}

	public static List<BiasNeuron> getBiasNeurons(ILayer layer) {
		List<BiasNeuron> neurons = new ArrayList<BiasNeuron>();
		for (INeuron neuron : layer.getNeurons())
			if (neuron instanceof BiasNeuron)
				neurons.add((BiasNeuron) neuron);
		return neurons;
	}

	public static List<InputNeuron> getInputNeurons(ILayer layer) {
		List<InputNeuron> neurons = new ArrayList<InputNeuron>();
		for (INeuron neuron : layer.getNeurons()) {
			if (neuron instanceof InputNeuron)
				neurons.add((InputNeuron) neuron);
		}
		return neurons;
	}

	public static List<ProcessorNeuron> getProcessorNeurons(ILayer layer) {
		List<ProcessorNeuron> neurons = new ArrayList<ProcessorNeuron>();
		for (INeuron neuron : layer.getNeurons()) {
			if (neuron instanceof ProcessorNeuron)
				neurons.add((ProcessorNeuron) neuron);
		}
		return neurons;
	}

	public static List<ErrorNeuron> getErrorNeurons(ILayer layer) {
		List<ErrorNeuron> neurons = new ArrayList<ErrorNeuron>();
		for (INeuron neuron : layer.getNeurons()) {
			if (neuron instanceof ProcessorNeuron)
				neurons.add((ErrorNeuron) neuron);
		}
		return neurons;
	}
}