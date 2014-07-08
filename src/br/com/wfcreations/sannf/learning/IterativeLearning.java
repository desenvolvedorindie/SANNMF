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
package br.com.wfcreations.sannf.learning;

import br.com.wfcreations.sannf.learning.stopcondition.IStopCondition;
import br.com.wfcreations.sannf.event.IterativeLearningEvent;
import br.com.wfcreations.sannf.event.LearningEvent;

public abstract class IterativeLearning extends LearningRule {

	private static final long serialVersionUID = 1L;

	protected int currentEpoch = 0;

	protected boolean batchMode;

	protected IStopCondition[] stopConditions;

	protected transient volatile boolean stopLearning;

	protected transient volatile boolean pausedLearning;

	public int getCurrentEpoch() {
		return currentEpoch;
	}

	public IterativeLearning pauseLearning() {
		this.pausedLearning = true;
		return this;
	}

	public IterativeLearning resumeLearning() {
		this.pausedLearning = false;
		synchronized (this) {
			notify();
		}
		return this;
	}

	public IterativeLearning stopLearning() {
		stopLearning = false;
		return this;
	}

	public boolean isStoppedLearning() {
		return this.stopLearning;
	}

	public boolean isPausedLearning() {
		return pausedLearning;
	}

	public boolean isInBatchMode() {
		return batchMode;
	}

	public IterativeLearning setBatchMode(boolean batchMode) {
		this.batchMode = batchMode;
		return this;
	}

	protected IterativeLearning startLearning() {
		this.stopLearning = false;
		eventDispatcher.dispatchEvent(new IterativeLearningEvent(LearningEvent.INIT_LEARNING, this));
		while (!isStoppedLearning()) {
			onBeforeEpoch();
			eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.START_EPOCH, this));
			doLearningEpoch();
			this.currentEpoch++;
			if (this.batchMode == true)
				doBatchWeightsUpdate();
			onAfterEpoch();
			eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.END_EPOCH, this));
			if (hasReachedStopCondition() || isStoppedLearning()) {
				onStopLearning();
				eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.STOP_LEARNING, this));
				break;
			} else if (this.currentEpoch == Integer.MAX_VALUE)
				this.currentEpoch = 1;
			if (this.pausedLearning) {
				eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.PAUSE_LEARNING, this));
				synchronized (this) {
					while (this.pausedLearning)
						try {
							this.wait();
						} catch (InterruptedException e) {
						}
				}
				eventDispatcher.dispatchEvent(new IterativeLearningEvent(IterativeLearningEvent.RESUME_LEARNING, this));
			}
		}
		eventDispatcher.dispatchEvent(new IterativeLearningEvent(LearningEvent.COMPLETE_LEARNING, this));
		return this;
	}

	protected boolean hasReachedStopCondition() {
		for (IStopCondition stop : stopConditions)
			if (stop.isReached())
				return true;
		return false;
	}

	protected abstract void onStartLearning();

	protected abstract void onStopLearning();

	protected abstract void onBeforeEpoch();

	protected abstract void doLearningEpoch();

	protected abstract void onAfterEpoch();

	protected abstract void doBatchWeightsUpdate();
}