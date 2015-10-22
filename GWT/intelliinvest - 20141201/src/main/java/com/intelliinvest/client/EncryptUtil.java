package com.intelliinvest.client;

import com.googlecode.gwt.crypto.client.TripleDesCipher;

public class EncryptUtil {
	static final byte[] SECRET_KEY ={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	
	public static String encrypt(String value) throws Exception{
		 TripleDesCipher cipher = new TripleDesCipher();
		 cipher.setKey(SECRET_KEY);
		 return cipher.encrypt(value);
	}
	
	public static String decrypt(String value) throws Exception{
		 TripleDesCipher cipher = new TripleDesCipher();
		 cipher.setKey(SECRET_KEY);
		 return cipher.decrypt(value);
	}
	
}