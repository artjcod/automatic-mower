package org.mowit.service.factory;

import java.util.Collection;
import java.util.HashMap;

import org.mowit.exception.NotFoundException;
import org.mowit.model.Lawn;
import org.springframework.cache.annotation.Cacheable;


@Cacheable
public class LawnFactory {

	private static HashMap<Integer, Lawn> lawns = new HashMap<>();

	private LawnFactory() {
	}

	public static void registerLawn(Lawn lawn) {
		Integer id = lawn.getId();
		if (!lawns.containsKey(id)) {
			lawns.put(id, lawn);
		}
	}

	public static Lawn getLawn(Integer identifier) throws NotFoundException {
		Lawn lawn = lawns.get(identifier);
		return lawn;
	}

	public static int getNumberOfLawns() {
		return lawns.size();
	}

	public static Collection<Lawn> getLanws() {
		return lawns.values();
	}
}
