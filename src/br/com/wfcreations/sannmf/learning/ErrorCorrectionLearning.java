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
package br.com.wfcreations.sannmf.learning;

import java.util.Iterator;
import java.util.List;

import br.com.wfcreations.sannmf.data.SupervisedPattern;
import br.com.wfcreations.sannmf.data.SupervisedSet;
import br.com.wfcreations.sannmf.event.IterativeLearningEvent;
import br.com.wfcreations.sannmf.function.error.IErrorFunction;
import br.com.wfcreations.sannmf.learning.stopcondition.IStopCondition;
import br.com.wfcreations.sannmf.structure.ILayer;
import br.com.wfcreations.sannmf.structure.INeuralNetwork;
import br.com.wfcreations.sannmf.structure.INeuron;
import br.com.wfcreations.sannmf.structure.ISynapse;
import br.com.wfcreations.sannmf.structure.feedforward.FeedforwardNeuralNetwork;
import br.com.wfcreations.sannmf.structure.feedforward.IInputtedNeuron;

public abstract class ErrorCorrectionLearning extends IterativeLearning implements ISupervisedLearning {

	private static final long serialVersionUID = 1L;

	protected SupervisedSet trainingData;

	protected IErrorFunction errorFunction;

	protected double totalNetworkError;

	protected double previousEpochError;

	public ErrorCorrectionLearning(FeedforwardNeuralNetwork network, boolean batchMode, IErrorFunction errorFunction) {
		this.setNetwork(network);
		this.setErrorFunction(errorFunction);
		this.batchMode = batchMode;
		this.previousEpochError = this.totalNetworkError = Double.NaN;
	}

	@Override
	public ErrorCorrectionLearning learn(SupervisedSet trainingSet) {
		learn(trainingSet, null);
		return this;
	}

	public void learn(SupervisedSet trainingSet, List<IStopCondition> stopConditions) {
		if (trainingSet == null)
			throw new IllegalArgumentException("Training Set can't be null");

		this.trainingData = trainingSet;

		if (stopConditions != null)
			this.stopConditions = stopConditions;

		this.previousEpochError = this.totalNetworkError = Double.NaN;
		start();
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

	@Override
	public ErrorCorrectionLearning setNetwork(INeuralNetwork network) {
		if (!(network instanceof FeedforwardNeuralNetwork))
			throw new IllegalArgumentException("Neuralnetwork must be feedforward");
		super.setNetwork(network);
		return this;
	}

	@Override
	public SupervisedSet getTrainingData() {
		return this.trainingData;
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	protected void onBeforeEpoch() {
		this.previousEpochError = this.totalNetworkError;
		this.totalNetworkError = Double.NaN;
		this.errorFunction.reset();
	}

	@Override
	protected void doLearningEpoch() {
		Iterator<SupervisedPattern> iterator = this.trainingData.iterator();
		while (iterator.hasNext() && !isStopped()) {
			this.learnPattern(iterator.next());
			eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.ITERATION, this));
		}
		this.totalNetworkError = getErrorFunction().getTotalError();
	}

	@Override
	protected void onAfterEpoch() {
	}

	@Override
	protected void doBatchWeightsUpdate() {
		List<ILayer> layers = this.network.getLayers();
		for (int i = this.network.getLayersNum() - 1; i > 0; i--)
			for (INeuron neuron : layers.get(i).getNeurons())
				if (neuron instanceof IInputtedNeuron)
					for (ISynapse synapse : ((IInputtedNeuron) neuron).getInputConnections()) {
						synapse.incrementWeight(synapse.getWeightChange());
						synapse.setWeightChange(0);
					}

	}

	protected void learnPattern(SupervisedPattern trainingElement) {
		FeedforwardNeuralNetwork feedforwardNeuralNetwork = (FeedforwardNeuralNetwork) this.network;
		feedforwardNeuralNetwork.setInput(trainingElement.getInputs());
		feedforwardNeuralNetwork.activate();
		double[] outputError = this.calculateOutputError(trainingElement.getOutputs(), feedforwardNeuralNetwork.getOutput());
		this.errorFunction.addOutputsError(outputError);
		this.updateNetworkWeights(outputError);
	}

	protected double[] calculateOutputError(double[] desiredOutput, double[] output) {
		double[] outputError = new double[desiredOutput.length];
		for (int i = 0; i < output.length; i++)
			outputError[i] = desiredOutput[i] - output[i];
		return outputError;
	}

	protected abstract ErrorCorrectionLearning updateNetworkWeights(double[] outputError);
}