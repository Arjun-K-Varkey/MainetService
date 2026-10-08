package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterReceiptDataEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ADDITIONAL_REF_NO")
	private String additionalRefNo;

	@Column(name = "RM_RCPTID")
	private String rmRcptId;

	@Column(name = "RM_AMOUNT")
	private String rmAmount;

	@Column(name = "RM_DEMAND")
	private String rmDemand;

	@Column(name = "RM_RECEIVEDFROM")
	private String rmReceivedFrom;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdditionalRefNo() {
		return additionalRefNo;
	}

	public void setAdditionalRefNo(String additionalRefNo) {
		this.additionalRefNo = additionalRefNo;
	}

	public String getRmRcptId() {
		return rmRcptId;
	}

	public void setRmRcptId(String rmRcptId) {
		this.rmRcptId = rmRcptId;
	}

	public String getRmAmount() {
		return rmAmount;
	}

	public void setRmAmount(String rmAmount) {
		this.rmAmount = rmAmount;
	}

	public String getRmDemand() {
		return rmDemand;
	}

	public void setRmDemand(String rmDemand) {
		this.rmDemand = rmDemand;
	}

	public String getRmReceivedFrom() {
		return rmReceivedFrom;
	}

	public void setRmReceivedFrom(String rmReceivedFrom) {
		this.rmReceivedFrom = rmReceivedFrom;
	}
	
	
}
