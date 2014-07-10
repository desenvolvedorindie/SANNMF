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
package br.com.wfcreations.sannf.test.structure;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.com.wfcreations.sannf.function.activation.ActivationFunction;
import br.com.wfcreations.sannf.function.activation.Linear;
import br.com.wfcreations.sannf.function.input.IInputFunction;
import br.com.wfcreations.sannf.function.input.WeightedSum;
import br.com.wfcreations.sannf.structure.Synapse;
import br.com.wfcreations.sannf.structure.feedforward.InputNeuron;
import br.com.wfcreations.sannf.structure.feedforward.ProcessorLayer;
import br.com.wfcreations.sannf.structure.feedforward.ProcessorNeuron;

public class NeuronTest {

	InputNeuron n1, n2;

	ProcessorNeuron n3, n4;

	ProcessorNeuron n5;

	Synapse s13, s23, s14, s24, s35, s45;

	double W13 = 0.1;
	double W23 = 0.2;
	double W14 = 0.3;
	double W24 = 0.4;
	double W35 = 0.5;
	double W45 = 0.6;

	@Before
	public void setUp() {
		n1 = new InputNeuron();
		n2 = new InputNeuron();
		n3 = new ProcessorNeuron();
		n4 = new ProcessorNeuron();
		n5 = new ProcessorNeuron();

		s13 = new Synapse(n1, n3, W13);
		n1.addOutputSynapse(s13);
		n3.addInputSynapse(s13);

		s23 = new Synapse(n2, n3, W23);
		n2.addOutputSynapse(s23);
		n3.addInputSynapse(s23);

		s14 = new Synapse(n1, n4, W14);
		n1.addOutputSynapse(s14);
		n4.addInputSynapse(s14);

		s24 = new Synapse(n2, n4, W24);
		n2.addOutputSynapse(s24);
		n4.addInputSynapse(s24);

		s35 = new Synapse(n3, n5, W35);
		n3.addOutputSynapse(s35);
		n5.addInputSynapse(s35);

		s45 = new Synapse(n4, n5, W45);
		n4.addOutputSynapse(s45);
		n5.addInputSynapse(s45);
	}

	@Test
	public void testSetup() {
		assertTrue(n3.getActivationFunction() instanceof Linear);
		assertTrue(n3.getInputFunction() instanceof WeightedSum);

		assertEquals(0, n3.getInducedLocalField(), 0);
		assertEquals(0, n4.getInducedLocalField(), 0);
		assertEquals(0, n5.getInducedLocalField(), 0);

		assertEquals(0, n1.getOutput(), 0);
		assertEquals(0, n2.getOutput(), 0);
		assertEquals(0, n3.getOutput(), 0);
		assertEquals(0, n4.getOutput(), 0);
		assertEquals(0, n5.getOutput(), 0);
	}

	@Test
	public void testConstruct() {
		IInputFunction inputFunction = new WeightedSum();
		ActivationFunction activationFunction = new Linear();

		ProcessorNeuron n = new ProcessorNeuron(inputFunction, activationFunction);

		assertEquals(inputFunction, n.getInputFunction());
		assertEquals(activationFunction, n.getActivationFunction());
	}

	@Test
	public void testActivate() {
		n1.setInput(2);
		n2.setInput(3);

		assertEquals(2, n1.getOutput(), 0);
		assertEquals(3, n2.getOutput(), 0);

		n3.activate();
		n4.activate();

		n5.activate();

		assertEquals(2 * W13 + 3 * W23, n3.getInducedLocalField(), 0);
		assertEquals(2 * W13 + 3 * W23, n3.getOutput(), 0);
		assertEquals(2 * W14 + 3 * W24, n4.getInducedLocalField(), 0);
		assertEquals(2 * W14 + 3 * W24, n4.getOutput(), 0);
		assertEquals((2 * W13 + 3 * W23) * W35 + (2 * W14 + 3 * W24) * W45, n5.getInducedLocalField(), 0);
		assertEquals((2 * W13 + 3 * W23) * W35 + (2 * W14 + 3 * W24) * W45, n5.getOutput(), 0);
	}

	@Test
	public void testReset() {
		n3.reset();

		assertEquals(0, n3.getInducedLocalField(), 0);
		assertEquals(0, n3.getOutput(), 0);
	}

	@Test
	public void testFunctions() {
		n3.setActivationFunction(new Linear());
		n3.setInputFunction(new WeightedSum());

		assertNotNull(n3.getActivationFunction());
		assertNotNull(n3.getInputFunction());

		ActivationFunction linear = new Linear();
		IInputFunction weightedSum = new WeightedSum();

		n5.setActivationFunction(linear);
		n5.setInputFunction(weightedSum);

		assertEquals(linear, n5.getActivationFunction());
		assertEquals(weightedSum, n5.getInputFunction());
	}

	@Test
	public void testConnections() {
		assertEquals(2, n3.getInputsNum());
		assertEquals(2, n4.getInputsNum());
		assertEquals(2, n5.getInputsNum());

		assertEquals(2, n1.getOutputsNum());
		assertEquals(2, n2.getOutputsNum());
		assertEquals(1, n3.getOutputsNum());
		assertEquals(1, n4.getOutputsNum());
		assertEquals(0, n5.getOutputsNum());

		assertTrue(n1.hasOutputConnection());
		assertTrue(n2.hasOutputConnection());
		assertTrue(n3.hasInputConnection());
		assertTrue(n4.hasInputConnection());
		assertTrue(n3.hasOutputConnection());
		assertTrue(n4.hasOutputConnection());
		assertTrue(n5.hasInputConnection());
		assertFalse(n5.hasOutputConnection());

		assertTrue(n1.hasConnectionTo(n3));
		assertTrue(n1.hasConnectionTo(n4));
		assertTrue(n2.hasConnectionTo(n3));
		assertTrue(n2.hasConnectionTo(n4));
		assertTrue(n3.hasConnectionTo(n5));
		assertTrue(n4.hasConnectionTo(n5));

		assertTrue(n3.hasConnectionFrom(n1));
		assertTrue(n3.hasConnectionFrom(n2));
		assertTrue(n4.hasConnectionFrom(n1));
		assertTrue(n4.hasConnectionFrom(n2));
		assertTrue(n5.hasConnectionFrom(n3));
		assertTrue(n5.hasConnectionFrom(n4));

		assertFalse(n1.hasConnectionTo(n5));
		assertFalse(n5.hasConnectionFrom(n1));

		assertEquals(n1.getOutputConnectionAt(0), s13);
		assertEquals(n3.getInputConnectionAt(0), s13);

		assertEquals(n1.getOutputConnectionAt(0), s13);
		assertEquals(n1.getOutputConnectionAt(1), s14);

		assertEquals(n3.getInputConnectionAt(0), s13);
		assertEquals(n3.getInputConnectionAt(1), s23);

		boolean resultRemoveOutput = n1.removeAllOutputConnections();
		boolean resultRemoveInput = n5.removeAllInputConnections();

		assertTrue(resultRemoveOutput);
		assertEquals(0, n1.getOutputsNum());

		assertTrue(resultRemoveInput);
		assertEquals(0, n5.getInputsNum());

		boolean resultRemoveAll = n3.removeAllConnections();

		assertTrue(resultRemoveAll);
		assertEquals(0, n3.getInputsNum());
		assertEquals(0, n3.getOutputsNum());

		assertFalse(n3.removeAllInputConnections());
		assertFalse(n3.removeAllOutputConnections());

		assertFalse(n5.removeAllConnections());
	}

	@Test
	public void testConnections2() {
		assertTrue(n1.hasConnectionTo(n3));
		assertFalse(n1.hasConnectionTo(n5));
		assertTrue(n3.hasConnectionFrom(n1));
		assertFalse(n5.hasConnectionFrom(n1));
	}

	@Test
	public void removeConnection() {
		assertTrue(n3.removeConnectionFrom(n1));
		assertFalse(n3.removeConnectionFrom(n1));

		assertTrue(n3.removeConnectionTo(n5));
		assertFalse(n3.removeConnectionTo(n5));
	}

	@Test
	public void getSynapse() {
		assertEquals(s13, n3.getSynapseFrom(n1));
		assertEquals(s13, n1.getSynapsenTo(n3));

		assertNull(n3.getSynapsenTo(n1));
	}

	@Test
	public void layerTest() {
		ProcessorLayer layer = new ProcessorLayer();
		n5.setParentLayer(layer);
		assertEquals(layer, n5.getParentLayer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void hasConnectionToNull() {
		n1.hasConnectionTo(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addInputConnectionNull() {
		n3.addInputSynapse(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addInputConnectionNotConnected() {
		n3.addInputSynapse(s35);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeInputConnectionConnectionNul() {
		n3.removeInputConnection(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeInputConnectionNotConnected() {
		n3.removeInputConnection(s14);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeConnectionFromNull() {
		n3.removeConnectionFrom(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addOutputSynapseNull() {
		n3.addOutputSynapse(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addOutputSynapseNotConnected() {
		n3.addOutputSynapse(s14);
	}

	public void removeOutputConnectionNull() {
		n3.removeOutputConnection(null);
	}

	public void removeOutputConnectionNotConnected() {
		n3.removeOutputConnection(s14);
	}

	public void removeConnectionToNull() {
		n3.removeConnectionTo(null);
	}
}