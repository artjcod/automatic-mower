package org.mowit.messages;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * Bundle getter abstract class
 * @author snaceur
 * @version V1.0
 */
public abstract class MowerRessourceBundle {

	private MowerRessourceBundle() {
		
	}
	
	public static final ResourceBundle getBundle(String resFile) {
		ResourceBundle genericBundle = ResourceBundle.getBundle(resFile);
		return getBundle(genericBundle);
	}

	public static final ResourceBundle getBundle(String resFile, Locale locale) {
		ResourceBundle genericBundle = ResourceBundle.getBundle(resFile, locale);
		return getBundle(genericBundle);
	}

	private static ResourceBundle getBundle(ResourceBundle genericBundle) {
		if (!(genericBundle instanceof PropertyResourceBundle)) {
			return genericBundle;
		} else {
			return new CustomPropertyResourceBundle((PropertyResourceBundle) genericBundle);
		}
	}
}
