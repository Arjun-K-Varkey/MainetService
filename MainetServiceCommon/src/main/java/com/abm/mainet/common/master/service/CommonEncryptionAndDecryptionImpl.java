package com.abm.mainet.common.master.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.utility.EncryptionAndDecryption;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cherupelli.srikanth
 * @since 22 Jan 2020
 */

@Produces("application/json")
@WebService(endpointInterface = "com.abm.mainet.common.master.service.ICommonEncryptionAndDecryption")
@Api(value = "/commonEncryptAndDecrypt")
@Path("/commonEncryptAndDecrypt")
@Service
public class CommonEncryptionAndDecryptionImpl implements ICommonEncryptionAndDecryption {

	@Override
	@Consumes({"application/json", "text/plain"})
	@POST
	@ApiOperation(value = "commonEncrypt", notes = "Encypt the data", response = Object.class)
	@Path("/commonEncrypt")
	public String commonEncrypt(@RequestBody Object obj) {
		try {
			byte[] objectToByteArray = ObjectToByteArray(obj);
			String encodedString = java.util.Base64.getEncoder().encodeToString(objectToByteArray);
			return EncryptionAndDecryption.encrypt(encodedString);
		} catch (Exception exception) {
			throw new FrameworkException("Error while encrypting: ", exception);
		}
	}

	@Override
	@Consumes({"application/json", "text/plain"})
	@POST
	@ApiOperation(value = "commonDecrypt", notes = "Decrypt the data", response = Object.class)
	@Path("/commonDecrypt")
	public Object commonDecryption(@RequestBody String encrypt) {
		try {
			String decrypt = EncryptionAndDecryption.decrypt(encrypt);
			byte[] decode = java.util.Base64.getDecoder().decode(decrypt);
			ByteArrayInputStream in = new ByteArrayInputStream(decode);
			ObjectInputStream is = new ObjectInputStream(in);
			Object object = is.readObject();
			return object;
		} catch (Exception exception) {
			throw new FrameworkException("Error while encrypting: ", exception);
		}

	}

	private byte[] ObjectToByteArray(Object obj) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		byte[] data = bos.toByteArray();
		return data;
	}
	
	@Override
	@Consumes({"application/json", "text/plain"})
	@POST
	@ApiOperation(value = "commonCheckSum", notes = "CheckSum the data", response = Object.class)
	@Path("/commonCheckSum")
	public String commonCheckSum(String input) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
