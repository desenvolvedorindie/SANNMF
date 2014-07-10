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
package br.com.wfcreations.sannf.structure.feedforward;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.activation.Linear;
import br.com.wfcreations.sannf.function.input.IInputFunction;
import br.com.wfcreations.sannf.function.input.WeightedSum;
import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.ISynapse;

public class ProcessorNeuron extends InputNeuron implements IInputtedNeuron {

	private static final long serialVersionUID = 1L;

	protected List<ISynapse> inputSynapses = new ArrayList<ISynapse>();

	protected IInputFunction inputFunction;

	protected ActivationFunction activationFunction;

	protected transient double inducedLocalField;

	protected transient double output;

	public ProcessorNeuron() {
		this(new WeightedSum(), new Linear());
	}

	public ProcessorNeuron(IInputFunction inputFunction, ActivationFunction activationFunction) {
		this.setInputFunction(inputFunction).setActivationFunction(activationFunction);
	}

	public ProcessorNeuron activate() {
		this.inducedLocalField = this.inputFunction.output(this.inputSynapses);
		this.output = this.activationFunction.output(this.inducedLocalField);
		return this;
	}

	public double getInducedLocalField() {
		return this.inducedLocalField;
	}

	public ProcessorNeuron reset() {
		this.output = 0;
		this.inducedLocalField = 0;
		return this;
	}

	@Override
	public double getOutput() {
		return this.output;
	}

	@Override
	public int getInputsNum() {
		return this.inputSynapses.size();
	}

	@Override
	public boolean hasInputConnection() {
		return this.getInputsNum() > 0;
	}

	@Override
	public boolean hasConnectionFrom(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (ISynapse synapser : inputSynapses)
			if (synapser.getPresynaptic() == neuron)
				return true;
		return false;
	}

	@Override
	public boolean addInputSynapse(ISynapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPostsynaptic() != this)
			throw new IllegalArgumentException("Postsynaptical neuron should connect to this");

		if (!this.hasConnectionFrom(synapse.getPresynaptic())) {
			return this.inputSynapses.remove(synapse);
		}
		return false;
	}

	@Override
	public boolean removeInputConnection(ISynapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPostsynaptic() != this)
			throw new IllegalArgumentException("Postsynaptical neuron shold connect to this");
		return this.inputSynapses.remove(synapse);
	}

	@Override
	public boolean removeConnectionFrom(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		boolean change = false;
		Iterator<ISynapse> iterator = this.inputSynapses.iterator();
		ISynapse synapse;
		while (iterator.hasNext()) {
			synapse = iterator.next();
			if (synapse.getPresynaptic() == neuron) {
				iterator.remove();
				change = true;
			}
		}
		return change;
	}

	@Override
	public boolean removeAllInputConnections() {
		boolean changed = this.outputSynapses.size() > 0;
		this.inputSynapses.clear();
		return changed;
	}

	@Override
	public ISynapse getSynapseFrom(INeuron neuron) {
		for (ISynapse synapse : this.inputSynapses)
			if (synapse.getPresynaptic() == neuron)
				return synapse;
		return null;
	}

	@Override
	public List<ISynapse> getInputConnections() {
		return this.inputSynapses;
	}

	@Override
	public ISynapse getInputConnectionAt(int index) {
		return this.inputSynapses.get(index);
	}

	@Override
	public IInputFunction getInputFunction() {
		return this.inputFunction;
	}

	@Override
	public ProcessorNeuron setInputFunction(IInputFunction inputFunction) {
		this.inputFunction = inputFunction;
		return this;
	}

	public boolean removeAllConnections() {
		return this.removeAllInputConnections() || this.removeAllOutputConnections();
	}

	public ActivationFunction getActivationFunction() {
		return this.activationFunction;
	}

	public ProcessorNeuron setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
		return this;
	}
}