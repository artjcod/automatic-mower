package org.mowit.exception;

import org.mowit.messages.MessageServiceManager;
import org.mowit.messages.SourceSupplier;

/**
 * A Custom Exception that use the message supplier to get error message.
 * @author snaceur
 * @version V1.0
 */
public class MowerException extends RuntimeException {

	private static final long serialVersionUID = -3515409528332350126L;

	protected String errorCode;
	protected String[] params;

	public MowerException() {
	}

	public MowerException(String errorCode, String[] params) {
		this.errorCode = errorCode;
		this.params = params;
		handleFailure(errorCode, params);
	}
	
	public MowerException(String message, Throwable cause) {
		super(message, cause);
	}

	public MowerException(String message) {
		super(message);
	}

	public MowerException(Throwable cause) {
		super(cause);
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
				return MowerConst.RES;
			}
		}, new String[] { errorCode }, params);
		throw new MowerException(message);
	}
}
