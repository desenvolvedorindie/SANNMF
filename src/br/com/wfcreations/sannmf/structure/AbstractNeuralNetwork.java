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
package br.com.wfcreations.sannmf.structure;

import java.util.List;
import java.util.Vector;

public abstract class AbstractNeuralNetwork implements INeuralNetwork {

	private static final long serialVersionUID = 1L;

	protected List<ILayer> layers = new Vector<ILayer>();

	@Override
	public boolean addLayer(ILayer layer) {
		layer.setParentNeuralNetwork(this);
		return this.layers.add(layer);
	}

	@Override
	public AbstractNeuralNetwork addLayerAt(int index, ILayer layer) {
		layer.setParentNeuralNetwork(this);
		return this.addLayerAt(index, layer);
	}

	@Override
	public boolean removeLayer(ILayer layer) {
		layer.setParentNeuralNetwork(null);
		return this.layers.remove(layer);
	}

	@Override
	public ILayer removeLayerAt(int index) {
		return this.layers.remove(index);
	}

	@Override
	public List<ILayer> getLayers() {
		return this.layers;
	}

	@Override
	public ILayer getLayerAt(int index) {
		return this.layers.get(index);
	}

	@Override
	public int indexOf(ILayer layer) {
		return this.layers.indexOf(layer);
	}

	@Override
	public int getLayersNum() {
		return this.layers.size();
	}
}