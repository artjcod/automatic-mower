package org.mowit.setting;

import java.io.File;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLSocketFactory;

import org.mowit.init.Initializable;
import org.springframework.stereotype.Component;

/**
 * This is the default configuration loader if any custom one was found.
 * 
 * @author snaceur
 * @version V1.0
 */

@Component
public class DefaultSettingLoader implements SettingInterface, Initializable {

	private static DefaultSettingLoader instance = new DefaultSettingLoader();

	/**
	 * to avoid instantiation
	 */
	private DefaultSettingLoader() {
	}

	/**
	 * It will be initialized automatically when the application will start.
	 * 
	 * @return nothing
	 */
	public static boolean initiliaze() {
		try {
			SettingLoaderFactory.registerLoader(DefaultSettingLoader.class.getSimpleName(), instance);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@PostConstruct
	public void initProjectFolders() {
		File dir = new File(getSerFolder());
		if (!dir.exists()) {
			dir.mkdir();
		}
		StringBuilder br = new StringBuilder(getSerFolder());
		br.append("\\").append(getLawnFolderName());

		dir = new File(br.toString());
		if (!dir.exists()) {
			dir.mkdir();
		}

		br = new StringBuilder(getSerFolder());
		br.append("\\").append(getMowerFolderName());
		dir = new File(br.toString());
		if (!dir.exists()) {
			dir.mkdir();
		}

		br = new StringBuilder(getSerFolder());
		br.append("\\").append(getReportingFolder());
		dir = new File(br.toString());
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	/**
	 * Default lang is english (EN bundle will be used)
	 */
	@Override
	public Locale getLanguage() {
		return Locale.ENGLISH;
	}

	@Override
	public String getApplicationName() {
		return "Automatic-Mower";
	}

	@Override
	public String getBuildVersion() {
		return "V 1.0";
	}

	@Override
	public String getSerFolder() {
		return "D:\\AutomaticMower\\";
	}

	@Override
	public String getLawnFolderName() {
		return "lawns";
	}

	@Override
	public String getMowerFolderName() {
		return "Mowers";
	}

	@Override
	public String getSequenceFolderName() {
		return "Sequence";
	}

	@Override
	public String getSMTPServer() {
		return "smtp.gmail.com";
	}

	@Override
	public Integer getSMTPPort() {
		return 465;
	}

	@Override
	public String getSender() {
		return "monitoring@mowit.com";
	}

	@Override
	public String getUserName() {
		return "mowittest@gmail.com";
	}

	@Override
	public String getPassword() {
		return "WS+NwVHvA5TZEFDLrq9ZtLy3MIkwW1Ev4EHXMEmsjrY=";
	}

	@Override
	public Boolean isSSLEnabled() {
		return true;
	}

	@Override
	public Boolean isAuthenticationRequired() {
		return true;
	}

	@Override
	public String getSSLSocketFactory() {
		return SSLSocketFactory.class.getName();
	}

	@Override
	public String getAuthMechanisms() {
		return "DIGEST-MD5";
	}

	@Override
	public String getEncoding() {
		return "UTF-8";
	}

	@Override
	public String getReportingFolder() {
		return "Reports";
	}
}
