package org.mowit.setting;

import java.util.Locale;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.mowit.init.Initializable;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.mowit.exception.MowerConst.*;


/**
 * This class aims to get the application configurations from 
 * an xml file if found otherwise the system will use 
 * the default configuration loader
 * @author snaceur
 * @version V1.0
 */
@Component
public class CustomSettingLoader implements SettingInterface, Initializable {

	private static CustomSettingLoader instance = new CustomSettingLoader();

	private XMLConfiguration configuration;

	/**
	 * It will be initialized automatically when the application will start.
	 * @return nothing
	 */
	public static boolean initiliaze() {
		try {
			SettingLoaderFactory.registerLoader(CustomSettingLoader.class.getSimpleName(), instance);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * A private constructor to avoid the instantiation
	 */
	private CustomSettingLoader() {
		try {
			this.configuration = new XMLConfiguration("");
		} catch (ConfigurationException e) {
			LoggerFactory.getLogger(CustomSettingLoader.class).warn(e.getMessage());
		}
	}

	/**
	 * get the language.
	 */
	public Locale getLanguage() {
		return new Locale(configuration.getString(LANG));
	}

	/**
	 * get the application name
	 */
	public String getApplicationName() {
		return configuration.getString(APP_NAME);
	}

	/**
	 * get the build version
	 */
	public String getBuildVersion() {
		return configuration.getString(BUILD);
	}

	/**
	 * get the serialization folder
	 */
	@Override
	public String getSerFolder() {
		return configuration.getString(SER_FOLDER);

	}

	/**
	 * get where lawns are serialized
	 */
	@Override
	public String getLawnFolderName() {
		return configuration.getString(LAWNS_FOLDER);

	}
	/**
	 * get where Mowers are serialized
	 */
	@Override
	public String getMowerFolderName() {
		return configuration.getString(MOWERS_FOLDER);

	}
	
	@Override
	public String getSequenceFolderName() {
		return configuration.getString(SEQURENCE_FOLDER);
	}

	@Override
	public String getSMTPServer() {
		return configuration.getString(SMTP_SERVER);
	}

	@Override
	public Integer getSMTPPort() {
		return configuration.getInteger(SMTP_PORT,25);
	}

	@Override
	public String getSender() {
		return configuration.getString(SENDER);
	}

	@Override
	public String getUserName() {
		return configuration.getString(USERNAME);
	}

	@Override
	public String getPassword() {
		return configuration.getString(PASSWORD);
	}

	@Override
	public Boolean isSSLEnabled() {
		return configuration.getBoolean(SSL_ENABLED, false);
	}

	@Override
	public Boolean isAuthenticationRequired() {
		return configuration.getBoolean(AUTH_REQUIRED, false);
	}

	@Override
	public String getSSLSocketFactory() {
		return configuration.getString(SSL_SOCKET_FACTORY);
	}

	@Override
	public String getAuthMechanisms() {
		return configuration.getString(AUTH_MECHANISM);
	}

	@Override
	public String getEncoding() {
		return configuration.getString(ENCODING);
	}

	@Override
	public String getReportingFolder() {
		return configuration.getString(REPORT_FOLDER);
	}
}
