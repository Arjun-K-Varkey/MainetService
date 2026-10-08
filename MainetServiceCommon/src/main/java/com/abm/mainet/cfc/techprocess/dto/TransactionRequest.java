package com.abm.mainet.cfc.techprocess.dto;

import java.io.Serializable;

public class TransactionRequest implements Serializable {
	private String identifier;
	private String dateTime;
	private String requestType;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return "TransactionRequest [identifier=" + identifier + ", dateTime=" + dateTime + ", requestType="
				+ requestType + "]";
	}

}
