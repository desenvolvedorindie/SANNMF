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
package br.com.wfcreations.sannmf.unit.neuralnetwork;

import org.junit.Before;
import org.junit.Test;

import br.com.wfcreations.observer.dispatcher.Event;
import br.com.wfcreations.observer.dispatcher.IEventListener;
import br.com.wfcreations.sannmf.data.SupervisedPattern;
import br.com.wfcreations.sannmf.data.SupervisedSet;
import br.com.wfcreations.sannmf.event.IterativeLearningEvent;
import br.com.wfcreations.sannmf.function.activation.Sigmoid;
import br.com.wfcreations.sannmf.function.weightinitialization.UniformDistribution;
import br.com.wfcreations.sannmf.learning.ErrorCorrectionLearning;
import br.com.wfcreations.sannmf.learning.algorithms.Backpropagation;
import br.com.wfcreations.sannmf.learning.stopcondition.IStopCondition;
import br.com.wfcreations.sannmf.learning.stopcondition.MaximumEpoch;
import br.com.wfcreations.sannmf.neuralnetwork.MLP;

public class MLPNetworkTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Sigmoid activationFunction = new Sigmoid();

		MLP mlp = new MLP(2, new int[] { 2 }, 1, true, activationFunction, true);
		mlp.initializeWeights(new UniformDistribution(-1, 1));

		Backpropagation backpropagation = new Backpropagation(mlp, 2, true);
		SupervisedSet trainingSet = new SupervisedSet(2, 1);
		trainingSet.addPattern(new SupervisedPattern(new double[] { 1, 1 }, new double[] { 0 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { 1, -1 }, new double[] { 1 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { -1, 1 }, new double[] { 1 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { -1, -1 }, new double[] { 0 }));

		backpropagation.addEventListener(IterativeLearningEvent.END_EPOCH, new IEventListener() {
			@Override
			public void listen(Event e) {
				if (e instanceof IterativeLearningEvent) {
					IterativeLearningEvent event = (IterativeLearningEvent) e;
					System.out.println(((ErrorCorrectionLearning) event.getLearRule()).getPreviousEpochError());
				}
			}
		});

		IStopCondition[] stopCondition = new IStopCondition[] { new MaximumEpoch(backpropagation, 10000) };

		backpropagation.learn(trainingSet, stopCondition);

		System.out.println("Error: " + backpropagation.getTotalNetworkError());
		System.out.println("PreviousEpochError: " + backpropagation.getPreviousEpochError());
		System.out.println("Epochs: " + backpropagation.getCurrentEpoch());
		System.out.println("LearRate: " + backpropagation.getLearningRate());
		System.out.println("[1,1]: " + mlp.setInput(1, 1).activate().getOutput()[0]);
		System.out.println("[1,-1]: " + mlp.setInput(1, -1).activate().getOutput()[0]);
		System.out.println("[-1,1]: " + mlp.setInput(-1, 1).activate().getOutput()[0]);
		System.out.println("[-1,-1]: " + mlp.setInput(-1, -1).activate().getOutput()[0]);
	}
}