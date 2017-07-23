package org.mowit.exception;

import org.mowit.messages.MessageServiceManager;
import org.mowit.messages.SourceSupplier;


/**
 * A Custom Exception that use the message supplier to get error message.
 * @author snaceur
 * @version V1.0
 */
public class NotFoundException extends Exception {

	private static final long serialVersionUID = -2971681852232011124L;
	private static final String RES = "org.mowit.res.messages_en";

	protected String errorCode;
	protected String[] params;

	public NotFoundException() {

	}

	public NotFoundException(String errorCode, String[] params) {
		this.errorCode = errorCode;
		this.params = params;
		handleFailure(errorCode, params);
	}

	/**
	 * Get error message from the bundle and throw the exception.
	 * @param errorCode
	 * @param params
	 */
	private void handleFailure(String errorCode, String[] params) {
		String message = MessageServiceManager.getString(new SourceSupplier() {
			@Override
			public String getResourceFile() {
				return RES;
			}
		}, new String[] { errorCode }, params);
		throw new MowerException(message);
	}

}
