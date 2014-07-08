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
package br.com.wfcreations.sannf.structure.utils;

import br.com.wfcreations.sannf.structure.BiasNeuron;
import br.com.wfcreations.sannf.structure.Layer;
import br.com.wfcreations.sannf.structure.Neuron;
import br.com.wfcreations.sannf.structure.Synapse;

public class SynapseUtils {

	public static Synapse createSynapse(Neuron presynaptical, Neuron postsynaptical) {
		Synapse synapse = new Synapse(presynaptical, postsynaptical);
		postsynaptical.addInputSynapse(synapse);
		return synapse;
	}

	public static Synapse createSynapse(Neuron presynaptcial, Neuron postsynaptical, double weight) {
		Synapse connection = new Synapse(presynaptcial, postsynaptical, weight);
		postsynaptical.addInputSynapse(connection);
		return connection;
	}

	public static void fullConnect(Layer fromLayer, Layer toLayer) {
		for (Neuron presynaptical : fromLayer.getNeurons()) {
			for (Neuron postsynaptical : toLayer.getNeurons()) {
				createSynapse(presynaptical, postsynaptical);
			}
		}
	}

	public static void fullConnect(Layer fromLayer, Layer toLayer, boolean connectFromBias) {
		for (Neuron presynaptical : fromLayer.getNeurons()) {

			if (presynaptical instanceof BiasNeuron && !connectFromBias)
				continue;
			for (Neuron postsynaptical : toLayer.getNeurons()) {
				createSynapse(presynaptical, postsynaptical);
			}
		}
	}

	public static void fullConnect(Layer fromLayer, Layer toLayer, double weight) {
		for (Neuron presynaptical : fromLayer.getNeurons()) {
			for (Neuron toNeuron : toLayer.getNeurons()) {
				createSynapse(presynaptical, toNeuron, weight);
			}
		}
	}

	public static void fullConnect(Layer layer) {
		int neuronNumber = layer.neuronsNum();
		for (int i = 0; i < neuronNumber; i++) {
			for (int j = 0; j < neuronNumber; j++) {
				if (j == i)
					continue;
				createSynapse(layer.getNeuronAt(i), layer.getNeuronAt(j));
			}
		}
	}

	public static void fullConnect(Layer layer, double weight, boolean itSelf) {
		int neuronNumber = layer.neuronsNum();
		for (int i = 0; i < neuronNumber; i++) {
			for (int j = 0; j < neuronNumber; j++) {
				if (j == i && !itSelf)
					continue;
				createSynapse(layer.getNeuronAt(i), layer.getNeuronAt(j), weight);
			}
		}
	}
}