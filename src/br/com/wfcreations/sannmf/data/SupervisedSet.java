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
package br.com.wfcreations.sannmf.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class SupervisedSet implements Serializable {

	private static final long serialVersionUID = 1L;

	protected int inputsNum;

	protected int outputsNum;

	protected List<SupervisedPattern> patterns = new Vector<SupervisedPattern>();

	public SupervisedSet(int inputsNum, int outputsNum) {
		if (inputsNum < 1)
			throw new IllegalArgumentException("Inputs must be greater than 0");
		if (outputsNum < 1)
			throw new IllegalArgumentException("Outputs must be greater than 0");
		this.inputsNum = inputsNum;
		this.outputsNum = outputsNum;
	}

	public boolean addPattern(SupervisedPattern pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Supervised Pattern can't be null");
		if (pattern.getInputs().length != inputsNum)
			throw new IllegalArgumentException("Invalid input lenght");
		if (pattern.getOutputs().length != outputsNum)
			throw new IllegalArgumentException("Invalid output lenght");
		return this.patterns.add(pattern);
	}

	public boolean addPattern(double[] inputs, double[] outputs) {
		return this.addPattern(new SupervisedPattern(inputs, outputs));
	}

	public SupervisedPattern removePatternAt(int index) {
		return this.patterns.remove(index);
	}

	public Iterator<SupervisedPattern> iterator() {
		return this.patterns.iterator();
	}

	public SupervisedPattern getPatternAt(int index) {
		return this.patterns.get(index);
	}

	public SupervisedSet clear() {
		this.patterns.clear();
		return this;
	}

	public boolean isEmpty() {
		return this.patterns.isEmpty();
	}

	public int lenght() {
		return this.patterns.size();
	}

	public int inputsNum() {
		return inputsNum;
	}

	public int outputsNum() {
		return outputsNum;
	}

	public List<SupervisedPattern> getPatterns() {
		return patterns;
	}
}