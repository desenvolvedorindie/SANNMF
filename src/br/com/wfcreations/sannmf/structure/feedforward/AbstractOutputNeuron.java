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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import br.com.wfcreations.sannmf.structure.AbstractNeuron;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.ISynapse;

public abstract class AbstractOutputNeuron extends AbstractNeuron implements IOutputtedNeuron {

	private static final long serialVersionUID = 1L;

	protected List<ISynapse> outputSynapses = new Vector<ISynapse>();

	protected double output;

	@Override
	public int getOutputsNum() {
		return this.outputSynapses.size();
	}

	@Override
	public boolean hasOutputConnection() {
		return this.getOutputsNum() > 0;
	}

	@Override
	public boolean hasConnectionTo(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (ISynapse synapse : outputSynapses)
			if (synapse.getPostsynaptic() == neuron)
				return true;
		return false;
	}

	@Override
	public boolean addOutputSynapse(ISynapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptical neuron should connect to this");

		if (!this.hasConnectionTo(synapse.getPostsynaptic()))
			return this.outputSynapses.add(synapse);
		return false;
	}

	@Override
	public boolean removeOutputConnection(ISynapse synapse) {
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptic neuron shold connect to this");
		return this.outputSynapses.remove(synapse);
	}

	@Override
	public boolean removeConnectionTo(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");

		boolean changed = false;
		Iterator<ISynapse> iterator = this.outputSynapses.iterator();
		ISynapse synapse;
		while (iterator.hasNext()) {
			synapse = iterator.next();
			if (synapse.getPostsynaptic() == neuron) {
				iterator.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean removeAllOutputConnections() {
		boolean changed = this.outputSynapses.size() > 0;
		this.outputSynapses.clear();
		return changed;
	}

	@Override
	public ISynapse getSynapsenTo(INeuron neuron) {
		for (ISynapse synapse : this.outputSynapses)
			if (synapse.getPostsynaptic() == neuron)
				return synapse;
		return null;
	}

	@Override
	public List<ISynapse> getOutputConnections() {
		return this.outputSynapses;
	}

	@Override
	public ISynapse getOutputConnectionAt(int index) {
		return this.outputSynapses.get(index);
	}

	@Override
	public abstract double getOutput();
}