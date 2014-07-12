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
package br.com.wfcreations.sannmf.learning;

import java.util.ArrayList;
import java.util.List;

import br.com.wfcreations.sannmf.event.IterativeLearningEvent;
import br.com.wfcreations.sannmf.event.LearningEvent;
import br.com.wfcreations.sannmf.learning.stopcondition.IStopCondition;

public abstract class IterativeLearning extends AbstractLearningRule {

	private static final long serialVersionUID = 1L;

	protected int currentEpoch = 0;

	protected boolean batchMode;

	protected List<IStopCondition> stopConditions = new ArrayList<>();

	protected transient volatile boolean stopped;

	protected transient volatile boolean paused;

	public int getCurrentEpoch() {
		return this.currentEpoch;
	}

	public IterativeLearning pause() {
		this.paused = true;
		return this;
	}

	public IterativeLearning resume() {
		this.paused = false;
		synchronized (this) {
			this.notify();
		}
		return this;
	}

	public IterativeLearning stop() {
		this.stopped = false;
		return this;
	}

	public boolean isStopped() {
		return this.stopped;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public boolean isBatchMode() {
		return this.batchMode;
	}

	public IterativeLearning setBatchMode(boolean batchMode) {
		this.batchMode = batchMode;
		return this;
	}

	protected IterativeLearning start() {
		this.stopped = false;
		this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(LearningEvent.INIT_LEARNING, this));
		while (!this.isStopped()) {
			this.onBeforeEpoch();
			this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.START_EPOCH, this));
			this.doLearningEpoch();
			this.currentEpoch++;
			if (this.batchMode == true)
				this.doBatchWeightsUpdate();
			this.onAfterEpoch();
			this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.END_EPOCH, this));
			if (this.hasReachedStopCondition() || this.isStopped()) {
				this.onStop();
				this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.STOP_LEARNING, this));
				break;
			} else if (this.currentEpoch == Integer.MAX_VALUE)
				this.currentEpoch = 1;
			if (this.paused) {
				this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.PAUSE_LEARNING, this));
				synchronized (this) {
					while (this.paused)
						try {
							this.wait();
						} catch (InterruptedException e) {
						}
				}
				this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.RESUME_LEARNING, this));
			}
		}
		this.eventDispatcher.dispatchEvent(new IterativeLearningEvent(LearningEvent.COMPLETE_LEARNING, this));
		return this;
	}

	protected boolean hasReachedStopCondition() {
		for (IStopCondition stop : stopConditions)
			if (stop.isReached())
				return true;
		return false;
	}

	protected abstract void onStart();

	protected abstract void onStop();

	protected abstract void onBeforeEpoch();

	protected abstract void doLearningEpoch();

	protected abstract void onAfterEpoch();

	protected abstract void doBatchWeightsUpdate();
}