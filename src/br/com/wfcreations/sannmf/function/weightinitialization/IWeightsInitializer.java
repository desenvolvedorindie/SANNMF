package br.com.wfcreations.sannmf.function.weightinitialization;

import java.io.Serializable;

import br.com.wfcreations.sannmf.structure.INeuralNetwork;

public interface IWeightsInitializer extends Serializable {

	public void randomize(INeuralNetwork neuralnetwork);
}
