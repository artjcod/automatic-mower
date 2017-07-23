package org.mowit.messages;

import org.mowit.exception.MowerConst;

/**
 * This message get a message from a bundle file.
 * @author snaceur
 * @version V1.0
 */
public final class MessageGetter {

	public static String getMessage(String key, String[] params) {
		String message = MessageServiceManager.getString(new SourceSupplier() {
			@Override
			public String getResourceFile() {
				return MowerConst.RES;
			}
		}, new String[] { key }, params);
		return message;
	}

	public static String getMessage(String key) {
		String message = MessageServiceManager.getString(new SourceSupplier() {
			@Override
			public String getResourceFile() {
				return MowerConst.RES;
			}
		}, new String[] { key }, new String[] {});
		return message;
	}

}
