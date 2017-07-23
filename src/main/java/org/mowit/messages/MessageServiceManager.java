package org.mowit.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.mowit.exception.MowerException;
import org.mowit.setting.SettingLoaderFactory;

import static org.mowit.exception.MowerConst.DEFAULT;

/**
 * This is part for the mechanism that help to get message from a resource
 * bundle.
 * 
 * @author snaceur
 * @version V1.0
 */
public class MessageServiceManager {

	private MessageServiceManager() {
	}

	/**
	 * get message with parameters
	 * 
	 * @param messageSourceSupplier
	 * @param keys
	 * @param keysValues
	 * @return message
	 */
	public static String getString(SourceSupplier messageSourceSupplier, String[] keys, String[] keysValues) {
		return getString(messageSourceSupplier, keys, keysValues,
				SettingLoaderFactory.getSettingLoader(DEFAULT).getLanguage());
	}

	/**
	 * @param messageSourceSupplier
	 * @param keys
	 * @param keysValues
	 * @param locale
	 * @return
	 */
	public static String getString(SourceSupplier messageSourceSupplier, String[] keys, String[] keysValues,
			String locale) {
		return getString(messageSourceSupplier, keys, keysValues, new Locale(locale));
	}

	/**
	 * get message from a resource file (.properties).
	 * 
	 * @param messageSourceSupplier
	 * @param keys
	 * @param keysValues
	 * @param locale
	 * @return
	 */
	public static String getString(SourceSupplier messageSourceSupplier, String[] keys, String[] keysValues,
			Locale locale) {
		try {
			if (keys == null) {
				throw new IllegalArgumentException("No keys have been specified and they're mandatory.");
			}
			String resFile = messageSourceSupplier.getResourceFile();
			String message = null;
			ResourceBundle bundle = MowerRessourceBundle.getBundle(resFile, locale);
			if (bundle == null) {
				message = keys[(keys.length - 1)];
			} else {
				message = bundle.getString(copmoseKey(keys));
				MessageFormat msgFormat = new MessageFormat((String) message, locale);
				message = msgFormat.format(keysValues == null ? new String[] { "''" } : keysValues);
			}
			return message;
		} catch (Exception e) {
			throw new MowerException(e);
		}
	}

	/**
	 * get unified key
	 * 
	 * @param keys
	 * @return
	 */
	protected static String copmoseKey(String[] keys) {
		if (keys.length == 1)
			return keys[0];
		StringBuilder chainedKeys = new StringBuilder();
		for (int i = 0; i < keys.length; i++)
			chainedKeys.append(keys[i] + "/");
		return chainedKeys.toString();
	}
}
