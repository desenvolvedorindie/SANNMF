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

import java.util.Arrays;

import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.input.IInputFunction;
import br.com.wfcreations.sannf.structure.Neuron;

public class TemporalNeuron extends Neuron {

	private static final long serialVersionUID = 1L;

	protected transient double[] history;

	private int historySize;

	public TemporalNeuron(IInputFunction inputFunction, ActivationFunction activationFunction, int historySize) {
		super(inputFunction, activationFunction);
		this.setHistorySize(historySize);
		history = new double[0];
		history[0] = 0;
	}

	@Override
	public TemporalNeuron activate() {
		super.activate();
		if (history.length < getHistorySize())
			this.history = Arrays.copyOf(history, history.length + 1);
		for (int j = inputs.length - 1; j > 0; j--)
			history[j] = history[j - 1];
		history[0] = this.output;
		return this;
	}

	public double output(int delay) {
		return history[delay];
	}

	public int lenght() {
		return this.history.length;
	}

	public int getHistorySize() {
		return historySize;
	}

	public void setHistorySize(int historySize) {
		if (historySize < 1)
			throw new IllegalArgumentException("Must be greater 0");
		if (historySize < this.history.length)
			this.history = Arrays.copyOf(history, historySize);
		this.historySize = historySize;
	}
}