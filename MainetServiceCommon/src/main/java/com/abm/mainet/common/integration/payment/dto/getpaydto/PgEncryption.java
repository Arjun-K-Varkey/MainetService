package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class PgEncryption {
   public String iv;
   public String ivKey;

   public PgEncryption(String iv, String key) {
      this.iv = iv;
      this.ivKey = key;
   }

   public String encrypt(String string) {
      try {
         byte[] ivs = Base64.getDecoder().decode(this.iv);
         IvParameterSpec ivspec = new IvParameterSpec(ivs);
         SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(this.ivKey), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(1, keySpec, ivspec);
         byte[] encrypted = cipher.doFinal(string.getBytes("UTF-8"));
         return toHexString(encrypted);
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public String getDecrytedString(String requestString) {
      try {
         requestString = this.decrypt(requestString);
         return requestString;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public String decrypt(String message) {
      try {
         byte[] msg = toByteArray(message);
         SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(this.ivKey), "AES");
         byte[] ivs = Base64.getDecoder().decode(this.iv);
         IvParameterSpec ivspec = new IvParameterSpec(ivs);
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(2, keySpec, ivspec);
         return new String(cipher.doFinal(msg));
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public static String toHexString(byte[] array) {
      return DatatypeConverter.printHexBinary(array);
   }

   public static byte[] toByteArray(String s) {
      return DatatypeConverter.parseHexBinary(s);
   }
}
