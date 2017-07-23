package org.mowit.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.mowit.exception.MowerException;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;
import org.springframework.stereotype.Service;

import static org.mowit.exception.MowerConst.WRONG_PASSWORD;


/**
 * This service is to decode password.
 * @author snaceur
 * @version V1.0
 */
@Service
public class PasswordDecrypt {

	private static PasswordDecrypt instance = new PasswordDecrypt();

	private PasswordDecrypt() {
	}

	public static PasswordDecrypt getInstance() {
		return instance;
	}

	/**
	 * Descrypt a password using a random key
	 * @param key
	 * @param secureRandom
	 * @param encrypted
	 * @return descrypted password
	 */
	public String decrypt(String key, String secureRandom, String encrypted) {
		try {
			SettingInterface defaultSettingLoader = SettingLoaderFactory.getSettingLoader("default");
			IvParameterSpec ivParam = new IvParameterSpec(secureRandom.getBytes(defaultSettingLoader.getEncoding()));
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(defaultSettingLoader.getEncoding()), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

			byte[] passsword = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(passsword);
		} catch (Exception ex) {
			throw new MowerException(WRONG_PASSWORD);
		}
	}
}
