package com.abm.mainet.common.master.service;

import javax.jws.WebService;

/**
 * @author cherupelli.srikanth
 *@since 22 Jan 2020
 */

@WebService
public interface ICommonEncryptionAndDecryption {

	String commonEncrypt(Object obj);
	
	Object commonDecryption(String encrypt);
	
	String commonCheckSum(String input);
}
