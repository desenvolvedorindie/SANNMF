package br.com.wfcreations.sannf.structure.feedforward;

public class ErrorNeuron extends ProcessorNeuron {

	private static final long serialVersionUID = 1L;

	protected double error;

	public double getError() {
		return error;
	}

	public ErrorNeuron setError(double error) {
		this.error = error;
		return this;
	}
}
