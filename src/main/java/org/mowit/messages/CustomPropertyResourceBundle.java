package org.mowit.messages;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * This class is used to override the default bundle to encode message in UTF-8
 * @author snaceur
 * @version V1..0
 */
 final class CustomPropertyResourceBundle extends ResourceBundle {

	PropertyResourceBundle propertyResourceBundle;
    CustomPropertyResourceBundle(PropertyResourceBundle propertyResourceBundle) {
		this.propertyResourceBundle = propertyResourceBundle;
	}

	@Override
	public Enumeration<String> getKeys() {

		return this.propertyResourceBundle.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		String str = (String) this.propertyResourceBundle.handleGetObject(key);
		try {
			return str != null ? new String(str.getBytes("ISO-8859-1"), "UTF-8") : null;
		} catch (Exception execption) {
		}
		return null;
	}

}