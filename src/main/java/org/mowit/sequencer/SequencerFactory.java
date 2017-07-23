package org.mowit.sequencer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SequencerFactory {

	private static HashMap<Class<? extends Serializable>, AtomicInteger> sequencer = new HashMap<>();

	public static void addSequencer(Class<? extends Serializable> classZ, AtomicInteger counter) {
		if (!sequencer.containsKey(classZ)) {
			sequencer.put(classZ, counter);
		}
	}

	public static void removeSequencer(Class<? extends Serializable> classZ) {
		sequencer.remove(classZ);
	}

	public static int getSequence(Class<? extends Serializable> classZ) {
		if (sequencer.containsKey(classZ)) {
			return sequencer.get(classZ).incrementAndGet();
		} else {
			AtomicInteger counter = new AtomicInteger();
			addSequencer(classZ, counter);
			return counter.incrementAndGet();
		}
	}
}
