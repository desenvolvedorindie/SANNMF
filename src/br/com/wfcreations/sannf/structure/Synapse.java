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

public class Synapse implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Neuron presynaptic;

	protected Neuron postsynaptic;

	protected double weight;

	protected transient double weightChange;

	protected transient Object data;

	public Synapse(Neuron presynaptic, Neuron postsynaptic, double weight) {
		this.setPresynaptic(presynaptic).setPostsynaptic(postsynaptic).setWeight(weight);
	}

	public Synapse(Neuron presynaptic, Neuron postsynaptic) {
		this(presynaptic, postsynaptic, 0);
	}

	public double input() {
		return this.presynaptic.output();
	}

	public double output() {
		return this.input() * this.weight;
	}

	public double incrementWeight(double amout) {
		this.weight += amout;
		return this.weight;
	}

	public double decrementWeight(double amout) {
		this.weight -= amout;
		return this.weight;
	}

	public Neuron getPresynaptic() {
		return presynaptic;
	}

	public Synapse setPresynaptic(Neuron presynaptic) {
		if (presynaptic == null)
			throw new IllegalArgumentException("Presynaptic neuron can't be null");
		this.presynaptic = presynaptic;
		return this;
	}

	public Neuron getPostsynaptic() {
		return postsynaptic;
	}

	public Synapse setPostsynaptic(Neuron postsynaptic) {
		if (postsynaptic == null)
			throw new IllegalArgumentException("Postsynaptic neuron can't be null");
		this.postsynaptic = postsynaptic;
		return this;
	}

	public double getWeight() {
		return weight;
	}

	public Synapse setWeight(double weight) {
		this.weight = weight;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Synapse setData(Object data) {
		this.data = data;
		return this;
	}

	public double getWeightChange() {
		return weightChange;
	}

	public Synapse setWeightChange(double weightChange) {
		this.weightChange = weightChange;
		return this;
	}
}