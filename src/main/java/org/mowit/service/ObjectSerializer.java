package org.mowit.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.mowit.exception.MowerException;
import org.mowit.model.AbstarctMower;
import org.mowit.model.Lawn;
import org.mowit.sequencer.SequencerFactory;
import org.mowit.service.factory.LawnFactory;
import org.mowit.service.factory.MowerFactory;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;
import org.springframework.stereotype.Component;

/**
 * This class aims to serialize/deserialize entities
 * 
 * @author snaceur
 *
 */


@Component("objectSerializer")
public class ObjectSerializer {

	private OutputStream stream;

	public ObjectSerializer() {
		super();
	}

	public ObjectSerializer(OutputStream stream) {
		this.stream = stream;
	}

	public OutputStream getStream() {
		return stream;
	}

	/**
	 * write an object to the disk I/O
	 * 
	 * @param obj
	 * @return true is without error otherwise false.
	 */
	public boolean writeValue(Object obj) {
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(stream);
			objStream.writeObject(obj);
			objStream.close();
			return true;
		} catch (Exception e) {
			throw new MowerException(e);
		}
	}

	/**
	 * Read a value from a stream.
	 * 
	 * @param input
	 * @return the object after deserialization
	 */
	public Object readValue(InputStream input) {
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(input);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			return object;
		} catch (ClassNotFoundException | IOException e) {
			throw new MowerException(e);
		}
	}

	/**
	 * Load all Lawns from disk and make them cached.
	 */
	public void loadLawns() {
		try {
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader("default");
			String root = settingLoader.getSerFolder();
			StringBuilder br = new StringBuilder(root).append("\\").append(settingLoader.getLawnFolderName());
			File dir = new File(br.toString());
			if (dir.exists() && dir.isDirectory()) {
				File[] listFiles = dir.listFiles();
				for (File f : listFiles) {
					Object value = readValue(new FileInputStream(f));
					if (value instanceof Lawn) {
						LawnFactory.registerLawn((Lawn) value);
					}
				SequencerFactory.addSequencer(Lawn.class, new AtomicInteger(listFiles.length));
				}
			}
		} catch (FileNotFoundException e) {
			throw new MowerException(e);
		}
	}

	/**
	 * Load all Mowers from disk and make them cached.
	 */
	public void loadMowers() {
		try {
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader("default");
			String root = settingLoader.getSerFolder();
			StringBuilder br = new StringBuilder(root).append("\\").append(settingLoader.getMowerFolderName());
			File dir = new File(br.toString());
			if (dir.exists() && dir.isDirectory()) {
				File[] listFiles = dir.listFiles();
				for (File f : listFiles) {
					Object value = readValue(new FileInputStream(f));
					if (value instanceof AbstarctMower) {
						MowerFactory.registerMower((AbstarctMower) value);
					}
             SequencerFactory.addSequencer(AbstarctMower.class, new AtomicInteger(listFiles.length));
				}
			}
		} catch (FileNotFoundException e) {
			throw new MowerException(e);
		}
	}
}
