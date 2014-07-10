package br.com.wfcreations.sannf.structure.feedforward;

import java.io.Serializable;
import java.util.List;

import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.ISynapse;

public interface IOutputtedNeuron extends INeuron, Serializable {

	public int getOutputsNum();

	public boolean hasOutputConnection();

	public boolean hasConnectionTo(INeuron neuron);

	public boolean addOutputSynapse(ISynapse synapse);

	public boolean removeOutputConnection(ISynapse synapse);

	public boolean removeConnectionTo(INeuron neuron);

	public boolean removeAllOutputConnections();

	public ISynapse getSynapsenTo(INeuron neuron);

	public List<ISynapse> getOutputConnections();

	public ISynapse getOutputConnectionAt(int index);

	public double getOutput();
}