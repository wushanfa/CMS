package com.gentlehealthcare.mobilecare.tool;

import android.util.Base64;

import java.nio.charset.Charset;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES256加密
 * @ClassName AESCrypt
 * @Description 加密本地私密信息
 * @author ZYY
 */
public  class AESCrypt {
	private static final String ENCRYPT_PASSWORD = "s2d312kj3234jlh4l4hkjk32l4j3lk3e";

	public static String encryptPwd(String content, String password) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(
					password.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
			return Base64.encodeToString(encrypted, Base64.DEFAULT);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String decryptPwd(String content, String password) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(
					password.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encrypted = Base64.decode(content.getBytes("UTF-8"),
					Base64.DEFAULT);
			byte[] original = cipher.doFinal(encrypted);
			String originalString = new String(original,
					Charset.forName("UTF-8"));
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static String encrypt(String content) {
		return encryptPwd(content, ENCRYPT_PASSWORD);
	}

	public static String decrypt(String content) {
		return decryptPwd(content, ENCRYPT_PASSWORD);
	}

	public static String getRandomKey() {
		int length = 32;
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b) {
				str += (char) (97 + random.nextInt(26));
			} else {
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}

}
