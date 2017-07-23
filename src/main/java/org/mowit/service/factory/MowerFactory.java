package org.mowit.service.factory;

import java.util.Collection;
import java.util.HashMap;

import org.mowit.exception.NotFoundException;
import org.mowit.model.AbstarctMower;
import org.springframework.cache.annotation.Cacheable;


@Cacheable
public class MowerFactory {

	private static HashMap<String, AbstarctMower> mowers = new HashMap<>();

	private MowerFactory() {
	}


	public static void registerMower(AbstarctMower mower) {
		String identifier = mower.getIdentifier();
		if (!mowers.containsKey(identifier)) {
			mowers.put(identifier, mower);
		}
	}

	public static AbstarctMower getMower(String identifier) throws NotFoundException {
		AbstarctMower mower = mowers.get(identifier);
		return mower;
	}

	public static int getNumberOfMowers() {
		return mowers.size();
	}
	public static Collection<AbstarctMower> getMowers() {
		return mowers.values();
	}
}
