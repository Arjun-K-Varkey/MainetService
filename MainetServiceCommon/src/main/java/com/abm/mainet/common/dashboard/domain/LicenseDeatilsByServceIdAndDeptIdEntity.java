package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LicenseDeatilsByServceIdAndDeptIdEntity {
	@Id
	@Column(name = "num")
	private int rowNumber;
	@Column(name = "RM_RCPTID")
	private String receiptId;
	@Column(name = "APM_APPLICATION_ID")
	private String applicationId;
	@Column(name = "RM_AMOUNT")
	private double amount;
	@Column(name = "RM_NARRATION")
	private String narration;
	@Column(name = "SM_SERVICE_ID")
	private Long serviceId;
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
}
