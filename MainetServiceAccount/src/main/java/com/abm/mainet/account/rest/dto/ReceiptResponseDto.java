package com.abm.mainet.account.rest.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;


public class ReceiptResponseDto implements Serializable {

	private String receiptNo;
	private String successMsg;
	private List<String> errorMsg;
	private HttpStatus statusCode;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6286383135666640815L;

	public ReceiptResponseDto(String receiptNo, String successMsg, List<String> errorMsg, HttpStatus statusCode) {
		super();
		this.receiptNo = receiptNo;
		this.successMsg = successMsg;
		this.errorMsg = errorMsg;
		this.statusCode = statusCode;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public static ReceiptResponseDto getResponse(String receiptNo, String successMsg, List<String> errorMsg,
			HttpStatus statusCode) {
		return new ReceiptResponseDto(receiptNo, successMsg, errorMsg, statusCode);
	}
}
