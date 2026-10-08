package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BpmsSummaryPageDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String proposalNumber;
	private String projectCode;
	private Date createdDate;
	private String sucessFlag;
	private String workFlowInitiated;
	private String updatedDate;
	private String draftMode;
	private String propertyNo;
	private String requestJson;
	private String ownerName;
	private String ownerAddress;
	private String ward;
	
	


	public String getWard() {
		return ward;
	}




	public void setWard(String ward) {
		this.ward = ward;
	}




	public String getProposalNumber() {
		return proposalNumber;
	}




	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}




	public String getProjectCode() {
		return projectCode;
	}




	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}




	public Date getCreatedDate() {
		return createdDate;
	}




	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}




	public String getSucessFlag() {
		return sucessFlag;
	}




	public void setSucessFlag(String sucessFlag) {
		this.sucessFlag = sucessFlag;
	}




	public String getWorkFlowInitiated() {
		return workFlowInitiated;
	}




	public void setWorkFlowInitiated(String workFlowInitiated) {
		this.workFlowInitiated = workFlowInitiated;
	}




	public String getUpdatedDate() {
		return updatedDate;
	}




	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}




	public String getDraftMode() {
		return draftMode;
	}




	public void setDraftMode(String draftMode) {
		this.draftMode = draftMode;
	}




	public String getPropertyNo() {
		return propertyNo;
	}




	public void setPropertyNo(String propertyNo) {
		this.propertyNo = propertyNo;
	}




	public String getRequestJson() {
		return requestJson;
	}




	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}




	public String getOwnerName() {
		return ownerName;
	}




	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}




	public String getOwnerAddress() {
		return ownerAddress;
	}




	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    

    
    
	
    
    
}
