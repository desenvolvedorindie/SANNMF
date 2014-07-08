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
package br.com.wfcreations.sannf.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UnsupervisedSet implements Serializable {

	private static final long serialVersionUID = 1L;

	protected int inputsNum;

	protected List<UnsupervisedPattern> patterns;

	public UnsupervisedSet(int inputsNum) {
		if (inputsNum < 1)
			throw new IllegalArgumentException("Inputs must be greater than 0");
		this.inputsNum = inputsNum;
		patterns = new ArrayList<>();
	}

	public boolean addPattern(UnsupervisedPattern pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Supervised Pattern can't be null");
		if (pattern.getInputs().length != inputsNum)
			throw new IllegalArgumentException("Invalid input lenght");
		return this.patterns.add(pattern);
	}

	public boolean addPattern(double[] inputs) {
		return this.addPattern(new UnsupervisedPattern(inputs));
	}

	public UnsupervisedPattern removePatternAt(int index) {
		return this.patterns.remove(index);
	}

	public Iterator<UnsupervisedPattern> iterator() {
		return this.patterns.iterator();
	}

	public UnsupervisedPattern getPatternAt(int index) {
		return this.patterns.get(index);
	}

	public UnsupervisedSet clear() {
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

	public List<UnsupervisedPattern> getPatterns() {
		return patterns;
	}
}
