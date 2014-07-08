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
package br.com.wfcreations.sannf.structure;

import java.io.Serializable;
import java.util.Arrays;

import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.activation.Linear;
import br.com.wfcreations.sannf.function.input.IInputFunction;
import br.com.wfcreations.sannf.function.input.WeightedSum;

public class Neuron implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Layer parentLayer;

	protected Synapse[] inputs = new Synapse[0];

	protected Synapse[] outputs = new Synapse[0];

	protected IInputFunction inputFunction;

	protected ActivationFunction activationFunction;

	protected transient double inducedLocalField;

	protected transient double output;

	protected transient double error;

	public Neuron() {
		this(new WeightedSum(), new Linear());
	}

	public Neuron(IInputFunction inputFunction, ActivationFunction activationFunction) {
		this.setInputFunction(inputFunction).setActivationFunction(activationFunction);
	}

	public Neuron activate() {
		this.inducedLocalField = this.inputFunction.output(this.inputs);
		this.output = this.activationFunction.output(this.inducedLocalField);
		return this;
	}

	public double getInducedLocalField() {
		return this.inducedLocalField;
	}

	public double getOutput() {
		return this.output;
	}

	public Neuron reset() {
		this.output = 0;
		this.inducedLocalField = 0;
		return this;
	}

	public int getInputsNum() {
		return this.inputs.length;
	}

	public boolean hasInputConnection() {
		return this.getInputsNum() > 0;
	}

	public boolean hasConnectionFrom(Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (Synapse synapser : inputs)
			if (synapser.getPresynaptic() == neuron)
				return true;
		return false;
	}

	public boolean addInputSynapse(Synapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPostsynaptic() != this)
			throw new IllegalArgumentException("Postsynaptical neuron should connect to this");

		if (!this.hasConnectionFrom(synapse.getPresynaptic())) {
			this.inputs = Arrays.copyOf(inputs, inputs.length + 1);
			this.inputs[inputs.length - 1] = synapse;
			synapse.getPresynaptic().addOutputSynapse(synapse);
			return true;
		}
		return false;
	}

	public boolean removeInputConnection(Synapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPostsynaptic() != this)
			throw new IllegalArgumentException("Postsynaptical neuron shold connect to this");
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i] == synapse) {
				for (int j = i; j < inputs.length - 1; j++)
					inputs[j] = inputs[j + 1];
				inputs[inputs.length - 1] = null;
				if (inputs.length > 0)
					this.inputs = Arrays.copyOf(inputs, inputs.length - 1);
				return true;
			}
		}
		return false;
	}

	public boolean removeConnectionFrom(Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (int i = 0; i < inputs.length; i++)
			if (inputs[i].getPresynaptic() == neuron) {
				neuron.removeOutputConnection(inputs[i]);
				this.removeInputConnection(inputs[i]);
				return true;
			}
		return false;
	}

	public boolean removeAllInputConnections() {
		boolean changed = false;
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].getPresynaptic().removeOutputConnection(inputs[i]);
			changed = true;
		}
		this.inputs = new Synapse[0];
		return changed;
	}

	public int getOutputsNum() {
		return this.outputs.length;
	}

	public boolean hasOutputConnection() {
		return this.getOutputsNum() > 0;
	}

	public boolean hasConnectionTo(Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (Synapse connection : outputs)
			if (connection.getPostsynaptic() == neuron)
				return true;
		return false;
	}

	public boolean addOutputSynapse(Synapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptical neuron should connect to this");

		if (!this.hasConnectionTo(synapse.getPostsynaptic())) {
			this.outputs = Arrays.copyOf(outputs, outputs.length + 1);
			this.outputs[outputs.length - 1] = synapse;
			return true;
		}
		return false;
	}

	public boolean removeOutputConnection(Synapse synapse) {
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptic neuron shold connect to this");
		for (int i = 0; i < outputs.length; i++)
			if (outputs[i] == synapse) {
				for (int j = i; j < outputs.length - 1; j++)
					outputs[j] = outputs[j + 1];
				if (outputs.length > 0)
					this.outputs = Arrays.copyOf(outputs, outputs.length - 1);
				return true;
			}
		return false;
	}

	public boolean removeConnectionTo(Neuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i].getPostsynaptic() == neuron) {
				neuron.removeInputConnection(outputs[i]);
				this.removeOutputConnection(outputs[i]);
				return true;
			}
		}
		return false;
	}

	public boolean removeAllOutputConnections() {
		boolean changed = false;
		for (int i = 0; i < this.outputs.length; i++) {
			outputs[i].getPostsynaptic().removeInputConnection(outputs[i]);
			outputs[i] = null;
			changed = true;
		}
		this.outputs = new Synapse[0];
		return changed;
	}

	public boolean removeAllConnections() {
		return this.removeAllInputConnections() || this.removeAllOutputConnections();
	}

	public Synapse getSynapsenFrom(Neuron neuron) {
		for (Synapse synapse : this.inputs)
			if (synapse.presynaptic == neuron)
				return synapse;
		return null;
	}

	public Synapse getSynapsenTo(Neuron neuron) {
		for (Synapse synapse : this.outputs)
			if (synapse.postsynaptic == neuron)
				return synapse;
		return null;
	}

	public Synapse[] getInputConnections() {
		return this.inputs;
	}

	public Synapse getInputConnectionAt(int index) {
		return this.inputs[index];
	}

	public Synapse[] getOutputConnections() {
		return this.outputs;
	}

	public Synapse getOutputConnectionAt(int index) {
		return this.outputs[index];
	}

	public Layer getParentLayer() {
		return this.parentLayer;
	}

	public Neuron setParentLayer(Layer parentLayer) {
		this.parentLayer = parentLayer;
		return this;
	}

	public double getError() {
		return this.error;
	}

	public Neuron setError(double error) {
		this.error = error;
		return this;
	}

	public IInputFunction getInputFunction() {
		return this.inputFunction;
	}

	public Neuron setInputFunction(IInputFunction inputFunction) {
		this.inputFunction = inputFunction;
		return this;
	}

	public ActivationFunction getActivationFunction() {
		return this.activationFunction;
	}

	public Neuron setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
		return this;
	}
}