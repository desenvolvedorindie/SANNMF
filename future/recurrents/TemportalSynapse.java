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
package br.com.wfcreations.sannf.structure.recurrents;

import br.com.wfcreations.sannf.structure.Neuron;
import br.com.wfcreations.sannf.structure.Synapse;

public class TemportalSynapse extends Synapse {

	private static final long serialVersionUID = 1L;

	private int delay;

	public TemportalSynapse(Neuron presynaptic, Neuron postsynaptic, double weight, int delay) {
		super(presynaptic, postsynaptic, weight);
		this.delay = delay;
	}

	public TemportalSynapse(Neuron presynaptic, Neuron postsynaptic, int delay) {
		super(presynaptic, postsynaptic);
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public TemportalSynapse setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	@Override
	public double input() {
		if (this.getPresynaptic() instanceof TemporalNeuron)
			return ((TemporalNeuron) this.presynaptic).output(delay);
		else
			return this.presynaptic.output();
	}
}
