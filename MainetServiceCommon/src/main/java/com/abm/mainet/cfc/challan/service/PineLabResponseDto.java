package com.abm.mainet.cfc.challan.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PineLabResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166666636666666666L;
	@JsonProperty("ResponseCode")
	private String responseCode;
	@JsonProperty("ResponseMessage")
	private String responseMessage;
	@JsonProperty("PlutusTransactionReferenceID")
	private String plutusTransactionReferenceID;
	@JsonProperty("TransactionData")
	private List<TransactionData> transactionData;
	@JsonProperty("AdditionalInfo")
	private String additionalInfo;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getPlutusTransactionReferenceID() {
		return plutusTransactionReferenceID;
	}

	public void setPlutusTransactionReferenceID(String plutusTransactionReferenceID) {
		this.plutusTransactionReferenceID = plutusTransactionReferenceID;
	}

	public List<TransactionData> getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(List<TransactionData> transactionData) {
		this.transactionData = transactionData;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String toString() {
		return "PineLabResponseDto [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", plutusTransactionReferenceID=" + plutusTransactionReferenceID + ", transactionData="
				+ transactionData + ", additionalInfo=" + additionalInfo + "]";
	}
}
