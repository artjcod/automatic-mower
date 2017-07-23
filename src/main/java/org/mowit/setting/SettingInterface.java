package org.mowit.setting;

import java.util.Locale;

/**
 * This interface contains all the configuration services.
 * @author snaceur
 * @version V1.0
 */
public interface SettingInterface {
	Locale getLanguage();
	String getApplicationName();
	String getBuildVersion();
	String getSerFolder();
	String getLawnFolderName();
	String getMowerFolderName();
	String getSequenceFolderName();
	String getSMTPServer();
	Integer getSMTPPort();
	String  getSender();
	String  getUserName();
	String getPassword();
	Boolean isSSLEnabled();
	Boolean isAuthenticationRequired();
	String getSSLSocketFactory();
	String getAuthMechanisms();
	String getEncoding();
	String getReportingFolder();
}
