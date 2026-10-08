package com.abm.mainet.common.workflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class AapleSarkarSummaryEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "Department")
	private String Department;
	
	@Column(name = "Service")
	private String Service;
	
	@Column(name = "Division")
	private String Division;
	
	@Column(name = "District")
	private String District;
	
	@Column(name = "Taluka")
	private String Taluka;	
	
	@Column(name = "Approved")
	private String Approved;
	
	@Column(name = "Rejected")
	private String Rejected;

	@Column(name = "OnTimeDelivery")
	private String OnTimeDelivery;
	
	@Column(name = "PendingatUser")
	private String PendingatUser;
	
	@Column(name = "PendingatDepartment")
	private String PendingatDepartment;

	@Column(name = "NotOnTimeDelivery")
	private String NotOnTimeDelivery;
	
	@Column(name = "PaymentMode")
	private String PaymentMode;
	
	@Column(name = "PendingatDepartmentWithinSLA")
	private String PendingatDepartmentWithinSLA;
	
	@Column(name = "PendingatDepartmentAfterSLA")
	private String PendingatDepartmentAfterSLA;
	
	@Column(name = "ApplicationSource")
	private String ApplicationSource;

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getService() {
		return Service;
	}

	public void setService(String service) {
		Service = service;
	}

	public String getDivision() {
		return Division;
	}

	public void setDivision(String division) {
		Division = division;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getTaluka() {
		return Taluka;
	}

	public void setTaluka(String taluka) {
		Taluka = taluka;
	}

	public String getApproved() {
		return Approved;
	}

	public void setApproved(String approved) {
		Approved = approved;
	}

	public String getRejected() {
		return Rejected;
	}

	public void setRejected(String rejected) {
		Rejected = rejected;
	}

	public String getOnTimeDelivery() {
		return OnTimeDelivery;
	}

	public void setOnTimeDelivery(String onTimeDelivery) {
		OnTimeDelivery = onTimeDelivery;
	}

	public String getPendingatUser() {
		return PendingatUser;
	}

	public void setPendingatUser(String pendingatUser) {
		PendingatUser = pendingatUser;
	}

	public String getPendingatDepartment() {
		return PendingatDepartment;
	}

	public void setPendingatDepartment(String pendingatDepartment) {
		PendingatDepartment = pendingatDepartment;
	}

	public String getNotOnTimeDelivery() {
		return NotOnTimeDelivery;
	}

	public void setNotOnTimeDelivery(String notOnTimeDelivery) {
		NotOnTimeDelivery = notOnTimeDelivery;
	}

	public String getPaymentMode() {
		return PaymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		PaymentMode = paymentMode;
	}

	public String getApplicationSource() {
		return ApplicationSource;
	}

	public void setApplicationSource(String applicationSource) {
		ApplicationSource = applicationSource;
	}

	public String getPendingatDepartmentWithinSLA() {
		return PendingatDepartmentWithinSLA;
	}

	public void setPendingatDepartmentWithinSLA(String pendingatDepartmentWithinSLA) {
		PendingatDepartmentWithinSLA = pendingatDepartmentWithinSLA;
	}

	public String getPendingatDepartmentAfterSLA() {
		return PendingatDepartmentAfterSLA;
	}

	public void setPendingatDepartmentAfterSLA(String pendingatDepartmentAfterSLA) {
		PendingatDepartmentAfterSLA = pendingatDepartmentAfterSLA;
	}
	
}
