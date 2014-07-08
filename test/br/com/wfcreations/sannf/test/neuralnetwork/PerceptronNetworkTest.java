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
package br.com.wfcreations.sannf.test.neuralnetwork;

import java.util.Arrays;

import org.junit.*;

import br.com.wfcreations.sannf.data.SupervisedPattern;
import br.com.wfcreations.sannf.data.SupervisedSet;
import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.activation.HardLimit;
import br.com.wfcreations.sannf.function.activation.Sign;
import br.com.wfcreations.sannf.function.weightinitialization.Range;
import br.com.wfcreations.sannf.learning.algorithms.LMS;
import br.com.wfcreations.sannf.learning.stopcondition.IStopCondition;
import br.com.wfcreations.sannf.learning.stopcondition.MaximumEpoch;
import br.com.wfcreations.sannf.learning.stopcondition.MaximumError;
import br.com.wfcreations.sannf.neuralnetwork.models.Perceptron;

public class PerceptronNetworkTest {

	@Test
	public void createPerceptronTest() {
		Perceptron perceptron = new Perceptron(2, 1, true, new Sign());
		perceptron.getLayerAt(1).getNeuronAt(0).getInputConnectionAt(0).setWeight(-0.8);
		perceptron.getLayerAt(1).getNeuronAt(0).getInputConnectionAt(1).setWeight(0.5);
		perceptron.getLayerAt(1).getNeuronAt(0).getInputConnectionAt(2).setWeight(0.5);
		System.out.println(Arrays.toString(perceptron.setInput(0, 0).activate().getOutputs()));
	}

	@Test
	public void trainPerceptonClassificationTest() {
		ActivationFunction activationFunction = new HardLimit();

		Perceptron perceptron = new Perceptron(2, 1, true, activationFunction);
		perceptron.initializeWeights(new Range(-1, 1));

		LMS deltaRule = new LMS(perceptron, 0.1, true);
		SupervisedSet trainingSet = new SupervisedSet(2, 1);
		trainingSet.addPattern(new SupervisedPattern(new double[] { 1, 1 }, new double[] { 1 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { 1, -1 }, new double[] { 1 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { -1, 1 }, new double[] { 1 }));
		trainingSet.addPattern(new SupervisedPattern(new double[] { -1, -1 }, new double[] { 0 }));

		IStopCondition[] stopCondition = new IStopCondition[] { new MaximumError(deltaRule, 0), new MaximumEpoch(deltaRule, 600) };

		deltaRule.learn(trainingSet, stopCondition);

		System.out.println("Error: " + deltaRule.getTotalNetworkError());
		System.out.println("PreviousEpochError: " + deltaRule.getPreviousEpochError());
		System.out.println("Epochs: " + deltaRule.getCurrentEpoch());
		System.out.println("LearRate: " + deltaRule.getLearningRate());
		System.out.println("[1,1]: " + Arrays.toString(perceptron.setInput(1, 1).activate().getOutputs()));
		System.out.println("[1,-1]: " + Arrays.toString(perceptron.setInput(1, -1).activate().getOutputs()));
		System.out.println("[-1,1]: " + Arrays.toString(perceptron.setInput(-1, 1).activate().getOutputs()));
		System.out.println("[-1,-1]: " + Arrays.toString(perceptron.setInput(-1, -1).activate().getOutputs()));
	}

}