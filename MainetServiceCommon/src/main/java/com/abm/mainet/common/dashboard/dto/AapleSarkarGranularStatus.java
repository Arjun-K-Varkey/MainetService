package com.abm.mainet.common.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AapleSarkarGranularStatus {
	
	@JsonProperty("RecordStatus")
	boolean RecordStatus;
	
	/*@JsonProperty("Message")
	String message;*/
	
	@JsonProperty("ErrorCode")
	String ErrorCode;
	
	@JsonProperty("ApplicationStatus")
	ApplicationStatus applicationStatus;
	
	
	/*
	 * public boolean isRecordStatus() { return RecordStatus; }
	 */
	public void setRecordStatus(boolean recordStatus) {
		RecordStatus = recordStatus;
	}
	/*public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}*/
	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	
	@JsonProperty("ErrorCode")
	public String getErrorCode() {
		return ErrorCode;
	}
	
	@JsonProperty("ErrorCode")
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	
	
	
	
}
