package br.com.wfcreations.sannf.structure.feedforward;

import java.io.Serializable;
import java.util.List;

import br.com.wfcreations.sannf.function.input.IInputFunction;
import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.ISynapse;

public interface IInputtedNeuron extends INeuron, Serializable {

	public int getInputsNum();

	public boolean hasInputConnection();

	public boolean hasConnectionFrom(INeuron neuron);

	public boolean addInputSynapse(ISynapse synapse);

	public boolean removeInputConnection(ISynapse synapse);

	public boolean removeConnectionFrom(INeuron neuron);

	public boolean removeAllInputConnections();

	public ISynapse getSynapseFrom(INeuron neuron);

	public List<ISynapse> getInputConnections();

	public ISynapse getInputConnectionAt(int index);

	public IInputFunction getInputFunction();

	public ProcessorNeuron setInputFunction(IInputFunction inputFunction);
}
