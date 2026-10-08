package com.abm.mainet.common.utility;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetePayPaymentEncyption {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetePayPaymentEncyption.class);
	
	String clientEncryptKey=ApplicationSession.getInstance().getMessage("gete.clientEncryptKey");
	String clientEncryptIV=ApplicationSession.getInstance().getMessage("getePay.clientEncryptIV");
	String clientCode=ApplicationSession.getInstance().getMessage("getePay.clientCode");
	String checkSumkey=ApplicationSession.getInstance().getMessage("getePay.checkSumkey");
	
	public String getEncValue(String data){
		String encKey=null; 
		try{
		String[] parts = data.split("\\|");		
		String propertyNumber = parts[0];
		String requestDateTime = parts[1];
		
		LOGGER.info("Seperated Request:" + " " + propertyNumber + " " +  requestDateTime);
		
		String response = String.format("%s|%s|%s|%s",propertyNumber, requestDateTime, checkSumkey,clientCode);
	
		String checksumvalue=generateCheckSumValue(response);
		
		String finalstring = String.format("%s|%s|%s|%s",clientCode, checksumvalue, propertyNumber, requestDateTime);
		
		
		LOGGER.info("finalstring:" + finalstring);
		
		

		try{
					encKey=simpleTripleDes(finalstring, clientEncryptKey, clientEncryptIV);
			}
					catch(Exception e){
						e.printStackTrace();
		}
		
		LOGGER.info("encKey:" + encKey);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return encKey;
		
	}

	private String generateCheckSumValue(String response) {
		    byte[] bytes = response.getBytes(StandardCharsets.US_ASCII);
		    CRC32 crc32 = new CRC32();
		    crc32.update(bytes);
		    long checksumvalue = crc32.getValue();
		    return Long.toString(checksumvalue);
		
	}
	
	public String simpleTripleDes(final String Data, final String strKey, final String striv)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		final byte[] cipherBytes = Data.getBytes("UTF-8");
		final byte[] iv = striv.getBytes("UTF-8");
		final byte[] keyBytes = strKey.getBytes("UTF-8");

		final SecretKey aesKey = new SecretKeySpec(keyBytes, "TripleDES");

		final Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));

		final byte[] result = cipher.doFinal(cipherBytes);
		return byteArrayToString(result);
	}
	
	private String byteArrayToString(final byte[] ba){
		final String hex = new String(bitConverterToString(ba));
		return hex.replace("-", "");
	}
	
	public static String bitConverterToString(final byte[] data) {
		final StringBuilder sb = new StringBuilder();
		for (final byte b : data) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}


}
