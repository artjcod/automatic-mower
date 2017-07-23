package org.mowit.monitoring.impl;

import java.util.ArrayList;
import java.util.Properties;

import org.mowit.encryption.PasswordDecrypt;
import org.mowit.messages.MessageGetter;
import org.mowit.model.AbstarctMower;
import org.mowit.model.MowerPosition;
import org.mowit.monitoring.Observable;
import org.mowit.monitoring.Observer;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.mowit.exception.MowerConst.EMAIL_ALERT_MESSAGE;
import static org.mowit.exception.MowerConst.BR;
import static org.mowit.exception.MowerConst.EMAIL_ALERT_SUBJECT;
import static org.mowit.exception.MowerConst.N_A;
import static org.mowit.exception.MowerConst.CONTENT_TYPE;
import static org.mowit.exception.MowerConst.DEFAULT;



/**
 * Send an email alert when the mower has finished the work.
 * @author snaceur
 * @version V1.0
 */
public class MowerStatusChangedEmailAlert implements Observer {


	@Override
	public void sendAlert(Observable observable) throws AddressException, MessagingException {
		if (observable instanceof AbstarctMower) {
			AbstarctMower mower = (AbstarctMower) observable;
			ArrayList<MowerPosition> positionHistory = mower.getPositionHistory();
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
			Properties props = new Properties();
			props.put("mail.smtp.host", settingLoader.getSMTPServer());
			props.put("mail.smtp.socketFactory.port", String.valueOf(settingLoader.getSMTPPort()));
			props.put("mail.smtp.socketFactory.class", settingLoader.getSSLSocketFactory());
			props.put("mail.smtp.auth", settingLoader.isAuthenticationRequired());
			props.put("mail.smtp.port", String.valueOf(settingLoader.getSMTPPort()));

			String password = PasswordDecrypt.getInstance().decrypt("HA%HYDG1;FT8K@7F", "84m6BrC?T^P*!#bz",
					settingLoader.getPassword());

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(settingLoader.getUserName(), password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(settingLoader.getSender()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(settingLoader.getUserName()));

			String key0 = mower.getIdentifier();

			String subject = MessageGetter.getMessage(EMAIL_ALERT_SUBJECT, new String[] { key0 });

			message.setSubject(subject);
			StringBuilder body = new StringBuilder();

			for (int i = 1; i < positionHistory.size(); i++) {
				MowerPosition mowerPosition = positionHistory.get(i);
				body.append(mowerPosition.toString()).append(BR).append(BR);
			}
			String key1 = mower.getUpdateDate() != null ? mower.getUpdateDate().toString() : N_A;
			String key2 = positionHistory.get(0) != null ? positionHistory.get(0).toString() : N_A;
			String key3 = body.toString();

			String bodyMessage = MessageGetter.getMessage(EMAIL_ALERT_MESSAGE, new String[] { key0, key1, key2, key3 });

			message.setContent(bodyMessage, CONTENT_TYPE);
			Transport.send(message);
		}
	}
}
