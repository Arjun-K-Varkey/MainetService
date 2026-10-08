package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Saroj.Tambe
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BPMSReceiptDto implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	
	private String proposal_id;

    private String receipt_name;

    private String loc_id;

    private String receipt_file;
    
    private String propertyNo;
    
    private String receiptDate;
    
    private String receiptNumber;
    
    

	public String getProposal_id() {
		return proposal_id;
	}

	public void setProposal_id(String proposal_id) {
		this.proposal_id = proposal_id;
	}

	public String getReceipt_name() {
		return receipt_name;
	}

	public void setReceipt_name(String receipt_name) {
		this.receipt_name = receipt_name;
	}

	public String getLoc_id() {
		return loc_id;
	}

	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}

	public String getReceipt_file() {
		return receipt_file;
	}

	public void setReceipt_file(String receipt_file) {
		this.receipt_file = receipt_file;
	}

	public String getPropertyNo() {
		return propertyNo;
	}

	public void setPropertyNo(String propertyNo) {
		this.propertyNo = propertyNo;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	
}
