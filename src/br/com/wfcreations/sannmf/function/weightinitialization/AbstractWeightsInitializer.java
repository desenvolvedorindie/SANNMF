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
package br.com.wfcreations.sannmf.function.weightinitialization;

import java.util.Random;

import br.com.wfcreations.sannmf.structure.ILayer;
import br.com.wfcreations.sannmf.structure.INeuralNetwork;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.ISynapse;
import br.com.wfcreations.sannmf.structure.feedforward.IInputtedNeuron;

public abstract class AbstractWeightsInitializer implements IWeightsInitializer {

	private static final long serialVersionUID = 1L;

	protected Random random = new Random();

	public void randomize(INeuralNetwork neuralnetwork) {
		for (ILayer layer : neuralnetwork.getLayers())
			for (INeuron neuron : layer.getNeurons())
				if (neuron instanceof IInputtedNeuron)
					for (ISynapse synapse : ((IInputtedNeuron) neuron).getInputConnections())
						synapse.setWeight(this.raffleWeight());
	}

	public Random getRandom() {
		return random;
	}

	public AbstractWeightsInitializer setRandom(Random random) {
		this.random = random;
		return this;
	}

	abstract protected double raffleWeight();
}