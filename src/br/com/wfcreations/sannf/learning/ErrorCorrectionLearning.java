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
package br.com.wfcreations.sannf.learning;

import java.util.Iterator;

import br.com.wfcreations.sannf.data.SupervisedPattern;
import br.com.wfcreations.sannf.data.SupervisedSet;
import br.com.wfcreations.sannf.event.IterativeLearningEvent;
import br.com.wfcreations.sannf.function.error.IErrorFunction;
import br.com.wfcreations.sannf.learning.stopcondition.IStopCondition;
import br.com.wfcreations.sannf.neuralnetwork.NeuralNetwork;
import br.com.wfcreations.sannf.structure.Layer;
import br.com.wfcreations.sannf.structure.Neuron;
import br.com.wfcreations.sannf.structure.Synapse;

public abstract class ErrorCorrectionLearning extends IterativeLearning implements SupervisedLearning {

	private static final long serialVersionUID = 1L;

	protected SupervisedSet trainingData;

	protected transient double previousEpochError;

	protected IErrorFunction errorFunction;

	protected double totalNetworkError;

	public ErrorCorrectionLearning(NeuralNetwork network, boolean batchMode, IErrorFunction errorFunction) {
		this.setNetwork(network);
		this.setErrorFunction(errorFunction);
		this.batchMode = batchMode;
	}

	@Override
	public void learn(SupervisedSet trainingSet) {
		learn(trainingSet, null);
	}

	public void learn(SupervisedSet trainingSet, IStopCondition[] stopCondition) {
		if (trainingSet == null)
			throw new IllegalArgumentException("Training Set can't be null");
		this.trainingData = trainingSet;
		if (stopCondition == null)
			this.stopConditions = new IStopCondition[0];
		else
			this.stopConditions = stopCondition;
		this.totalNetworkError = this.previousEpochError = 0d;
		startLearning();
	}

	protected void learnPattern(SupervisedPattern trainingElement) {
		this.network.setInput(trainingElement.getInputs()).activate();
		double[] outputError = this.calculateOutputError(trainingElement.getOutputs(), this.network.getOutputs());
		this.errorFunction.addOutputsError(outputError);
		this.updateNetworkWeights(outputError);
	}

	protected double[] calculateOutputError(double[] desiredOutput, double[] output) {
		double[] outputError = new double[desiredOutput.length];
		for (int i = 0; i < output.length; i++)
			outputError[i] = desiredOutput[i] - output[i];
		return outputError;
	}

	public double getTotalNetworkError() {
		return totalNetworkError;
	}

	public double getPreviousEpochError() {
		return previousEpochError;
	}

	public IErrorFunction getErrorFunction() {
		return errorFunction;
	}

	public ErrorCorrectionLearning setErrorFunction(IErrorFunction errorFunction) {
		if (errorFunction == null)
			throw new IllegalArgumentException("Erro Function can't be null");
		this.errorFunction = errorFunction;
		return this;
	}

	public SupervisedSet getTrainingData() {
		return this.trainingData;
	}

	@Override
	protected void onStartLearning() {
	}

	@Override
	protected void onStopLearning() {
	}

	@Override
	protected void onBeforeEpoch() {
		this.previousEpochError = this.totalNetworkError;
		this.totalNetworkError = 0d;
		this.errorFunction.reset();
	}

	@Override
	protected void doLearningEpoch() {
		Iterator<SupervisedPattern> iterator = this.trainingData.iterator();
		while (iterator.hasNext() && !isStoppedLearning()) {
			this.learnPattern(iterator.next());
			eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.INTERATION, this));
		}
		this.totalNetworkError = getErrorFunction().getTotalError();
	}

	@Override
	protected void onAfterEpoch() {
	}

	@Override
	protected void doBatchWeightsUpdate() {
		Layer[] layers = network.getLayers();
		for (int i = network.layersNum() - 1; i > 0; i--)
			for (Neuron neuron : layers[i].getNeurons())
				for (Synapse synapse : neuron.getInputConnections()) {
					synapse.incrementWeight(synapse.getWeightChange());
					synapse.setWeightChange(0);
				}

	}

	protected abstract void updateNetworkWeights(double[] outputError);
}