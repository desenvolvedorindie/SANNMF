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
package br.com.wfcreations.sannf.learning;

import java.io.Serializable;

import br.com.wfcreations.observer.dispatcher.EventDispatcher;
import br.com.wfcreations.observer.dispatcher.IEventListener;
import br.com.wfcreations.sannf.neuralnetwork.NeuralNetwork;

public abstract class LearningRule implements Serializable {

	private static final long serialVersionUID = 1L;

	protected NeuralNetwork network;

	protected EventDispatcher eventDispatcher = new EventDispatcher();

	public NeuralNetwork getNetwork() {
		return network;
	}

	public LearningRule setNetwork(NeuralNetwork network) {
		this.network = network;
		return this;
	}

	public void addEventListener(String type, IEventListener listener) {
		this.eventDispatcher.addEventListener(type, listener);
	}

	public void removeEventListener(String type, IEventListener listener) {
		this.eventDispatcher.removeEventListener(type, listener);
	}

	public boolean hasEventListener(String type) {
		return this.eventDispatcher.hasEventListener(type);
	}
}