package br.com.wfcreations.sannf.structure.feedforward;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.wfcreations.sannf.structure.AbstractNeuron;
import br.com.wfcreations.sannf.structure.INeuron;
import br.com.wfcreations.sannf.structure.ISynapse;

public abstract class AbstractOutputNeuron extends AbstractNeuron implements IOutputtedNeuron {

	private static final long serialVersionUID = 1L;

	protected List<ISynapse> outputSynapses = new ArrayList<ISynapse>();

	protected double output;

	@Override
	public int getOutputsNum() {
		return this.outputSynapses.size();
	}

	@Override
	public boolean hasOutputConnection() {
		return this.getOutputsNum() > 0;
	}

	@Override
	public boolean hasConnectionTo(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");
		for (ISynapse synapse : outputSynapses)
			if (synapse.getPostsynaptic() == neuron)
				return true;
		return false;
	}

	@Override
	public boolean addOutputSynapse(ISynapse synapse) {
		if (synapse == null)
			throw new IllegalArgumentException("Synapse can't be null");
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptical neuron should connect to this");

		if (!this.hasConnectionTo(synapse.getPostsynaptic())) {
			return this.outputSynapses.add(synapse);
		}
		return false;
	}

	@Override
	public boolean removeOutputConnection(ISynapse synapse) {
		if (synapse.getPresynaptic() != this)
			throw new IllegalArgumentException("Presynaptic neuron shold connect to this");
		return this.outputSynapses.remove(synapse);
	}

	@Override
	public boolean removeConnectionTo(INeuron neuron) {
		if (neuron == null)
			throw new IllegalArgumentException("Neuron can't be null");

		boolean changed = false;
		Iterator<ISynapse> iterator = this.outputSynapses.iterator();
		ISynapse synapse;
		while (iterator.hasNext()) {
			synapse = iterator.next();
			if (synapse.getPostsynaptic() == neuron) {
				iterator.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean removeAllOutputConnections() {
		boolean changed = this.outputSynapses.size() > 0;
		this.outputSynapses.clear();
		return changed;
	}

	public ISynapse getSynapsenTo(INeuron neuron) {
		for (ISynapse synapse : this.outputSynapses)
			if (synapse.getPostsynaptic() == neuron)
				return synapse;
		return null;
	}

	public List<ISynapse> getOutputConnections() {
		return this.outputSynapses;
	}

	public ISynapse getOutputConnectionAt(int index) {
		return this.outputSynapses.get(index);
	}

	public abstract double getOutput();
}