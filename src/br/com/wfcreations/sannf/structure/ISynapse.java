package br.com.wfcreations.sannf.structure;

import java.io.Serializable;

public interface ISynapse extends Serializable {

	public double incrementWeight(double amout);

	public double decrementWeight(double amout);

	public INeuron getPresynaptic();

	public ISynapse setPresynaptic(INeuron presynaptic);

	public INeuron getPostsynaptic();

	public ISynapse setPostsynaptic(INeuron postsynaptic);

	public double getWeight();

	public ISynapse setWeight(double weight);

	public double getWeightChange();

	public ISynapse setWeightChange(double weightChange);
}
