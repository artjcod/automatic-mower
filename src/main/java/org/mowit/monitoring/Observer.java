package org.mowit.monitoring;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * Observer interface
 * @author snaceur 
 * @version V1.0
 */

public interface Observer {

	/**
	 * Send alert when the mower status become finished.
	 * 
	 * @param observable
	 * @throws MessagingException
	 * @throws AddressException
	 */
	void sendAlert(Observable observable) throws AddressException, MessagingException;
}
