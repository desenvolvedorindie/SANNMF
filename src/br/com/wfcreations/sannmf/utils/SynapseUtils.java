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

import br.com.wfcreations.sannmf.structure.ILayer;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.Synapse;
import br.com.wfcreations.sannmf.structure.feedforward.BiasNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.IInputtedNeuron;
import br.com.wfcreations.sannmf.structure.feedforward.IOutputtedNeuron;

public class SynapseUtils {

	public static Synapse createSynapse(IOutputtedNeuron presynaptical, IInputtedNeuron postsynaptical) {
		Synapse synapse = new Synapse(presynaptical, postsynaptical);
		presynaptical.addOutputSynapse(synapse);
		postsynaptical.addInputSynapse(synapse);
		return synapse;
	}

	public static Synapse createSynapse(IOutputtedNeuron presynaptical, IInputtedNeuron postsynaptical, double weight) {
		Synapse synapse = new Synapse(presynaptical, postsynaptical, weight);
		presynaptical.addOutputSynapse(synapse);
		postsynaptical.addInputSynapse(synapse);
		return synapse;
	}

	public static void fullConnect(ILayer fromLayer, ILayer toLayer) {
		for (INeuron presynaptical : fromLayer.getNeurons()) {
			if (presynaptical instanceof IOutputtedNeuron)
				for (INeuron postsynaptical : toLayer.getNeurons())
					if (postsynaptical instanceof IInputtedNeuron)
						createSynapse((IOutputtedNeuron) presynaptical, (IInputtedNeuron) postsynaptical);
		}
	}

	public static void fullConnect(ILayer fromLayer, ILayer toLayer, boolean connectFromBias) {
		for (INeuron presynaptical : fromLayer.getNeurons()) {
			if (presynaptical instanceof IOutputtedNeuron) {
				if (presynaptical instanceof BiasNeuron && !connectFromBias)
					continue;
				for (INeuron postsynaptical : toLayer.getNeurons()) {
					if (postsynaptical instanceof IInputtedNeuron)
						createSynapse((IOutputtedNeuron) presynaptical, (IInputtedNeuron) postsynaptical);
				}
			}
		}
	}

	public static void fullConnect(ILayer layer) {
		int neuronNumber = layer.getNeuronsNum();
		for (int i = 0; i < neuronNumber; i++) {
			if (layer.getNeuronAt(i) instanceof IOutputtedNeuron)
				for (int j = 0; j < neuronNumber; j++) {
					if (j == i)
						continue;
					if (layer.getNeuronAt(j) instanceof IInputtedNeuron)
						createSynapse((IOutputtedNeuron) layer.getNeuronAt(i), (IInputtedNeuron) layer.getNeuronAt(j));
				}
		}
	}
}