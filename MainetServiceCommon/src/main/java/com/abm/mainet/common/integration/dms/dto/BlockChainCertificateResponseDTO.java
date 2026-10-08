package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockChainCertificateResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int code;
    private String status;
    private String message;
    private Result result;
    
    public static class Result {
        
    	@JsonProperty("verificatioUrl")
        private String verificationLink;

		public String getVerificationLink() {
			return verificationLink;
		}

		public void setVerificationLink(String verificationLink) {
			this.verificationLink = verificationLink;
		}
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
