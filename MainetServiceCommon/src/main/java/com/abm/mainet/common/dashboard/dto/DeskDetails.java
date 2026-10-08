package com.abm.mainet.common.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeskDetails {
	@JsonProperty("DeskNumber")
	private String deskNumber;              // e.g. "Desk 1"
	
	@JsonProperty("ReviewActionBy")
    private String reviewActionBy;           // Officer designation / role
	
	@JsonProperty("ReviewActionDateTime")
    private String reviewActionDateTime;     // DD-MMM-YYYY,HH:mm:ss (24-hr)
	
	@JsonProperty("ReviewActionDetails")
    private String reviewActionDetails;      // Approved / Rejected / Sent back to Desk-2 / ""
	
	@JsonProperty("DeskRemark")
	private String deskRemark;
	
	/*private String currentDeskNumber;
	private String nextDeskNumber;*/
    
    public String getDeskNumber() {
		return deskNumber;
	}
	public void setDeskNumber(String deskNumber) {
		this.deskNumber = deskNumber;
	}
	public String getReviewActionBy() {
		return reviewActionBy;
	}
	public void setReviewActionBy(String reviewActionBy) {
		this.reviewActionBy = reviewActionBy;
	}
	public String getReviewActionDateTime() {
		return reviewActionDateTime;
	}
	public void setReviewActionDateTime(String reviewActionDateTime) {
		this.reviewActionDateTime = reviewActionDateTime;
	}
	public String getReviewActionDetails() {
		return reviewActionDetails;
	}
	public void setReviewActionDetails(String reviewActionDetails) {
		this.reviewActionDetails = reviewActionDetails;
	}
	public String getDeskRemark() {
		return deskRemark;
	}
	public void setDeskRemark(String deskRemark) {
		this.deskRemark = deskRemark;
	}
	/*public String getCurrentDeskNumber() {
		return currentDeskNumber;
	}
	public void setCurrentDeskNumber(String currentDeskNumber) {
		this.currentDeskNumber = currentDeskNumber;
	}
	public String getNextDeskNumber() {
		return nextDeskNumber;
	}
	public void setNextDeskNumber(String nextDeskNumber) {
		this.nextDeskNumber = nextDeskNumber;
	}*/
    
    
}
