package com.abm.mainet.cfc.challan.service;

import java.io.Serializable;
import java.util.Map;

public class PineLabRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 16666666666666666L;
	private String transactionNumber;
	private String sequenceNumber;
	private String allowedPaymentMode;
	private String amount;
	private String userID;
	private String merchantID;
	private String securityToken;
	private String clientID;
	private String storeID;
	private String autoCancelDurationInMinutes;
	private String plutusTransactionReferenceID;

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getAllowedPaymentMode() {
		return allowedPaymentMode;
	}

	public void setAllowedPaymentMode(String allowedPaymentMode) {
		this.allowedPaymentMode = allowedPaymentMode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getAutoCancelDurationInMinutes() {
		return autoCancelDurationInMinutes;
	}

	public void setAutoCancelDurationInMinutes(String autoCancelDurationInMinutes) {
		this.autoCancelDurationInMinutes = autoCancelDurationInMinutes;
	}

	public String getPlutusTransactionReferenceID() {
		return plutusTransactionReferenceID;
	}

	public void setPlutusTransactionReferenceID(String plutusTransactionReferenceID) {
		this.plutusTransactionReferenceID = plutusTransactionReferenceID;
	}

}
