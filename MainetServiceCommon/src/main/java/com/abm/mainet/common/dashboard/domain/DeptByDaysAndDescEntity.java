package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DeptByDaysAndDescEntity {

	@Id
	@Column(name = "num")
	private int id;

	@Column(name = "APM_APPLICATION_ID")
	private String applId;

	@Column(name = "REFERENCE_MODE")
	private String refMode;

	@Column(name = "COMPLAINT_NO")
	private String complaintNo;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "DATE_OF_REQUEST")
	private String dateOfRequest;

	@Column(name = "Complaint_DESC")
	private String complaintDesc;

	@Column(name = "cpd_desc")
	private String cpdDesc;

	@Column(name = "cpd_desc_mar")
	private String cpdDescMar;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "Latitude")
	private String latitude;
	
	@Column(name = "Longitude")
	private String longitude;
	
	@Column(name = "Division")
	private String division;
	
	@Column(name = "Ward")
	private String ward;
	
	@Column(name = "Zone")
	private String zone;

	@Column(name = "Closed")
	private int closed;

	@Column(name = "Pending")
	private int pending;

	@Column(name = "Rejected")
	private int rejected;

	@Column(name = "Hold")
	private int hold;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplId() {
		return applId;
	}

	public void setApplId(String applId) {
		this.applId = applId;
	}

	public String getRefMode() {
		return refMode;
	}

	public void setRefMode(String refMode) {
		this.refMode = refMode;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public String getComplaintDesc() {
		return complaintDesc;
	}

	public void setComplaintDesc(String complaintDesc) {
		this.complaintDesc = complaintDesc;
	}

	public String getCpdDesc() {
		return cpdDesc;
	}

	public void setCpdDesc(String cpdDesc) {
		this.cpdDesc = cpdDesc;
	}

	public String getCpdDescMar() {
		return cpdDescMar;
	}

	public void setCpdDescMar(String cpdDescMar) {
		this.cpdDescMar = cpdDescMar;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getHold() {
		return hold;
	}

	public void setHold(int hold) {
		this.hold = hold;
	}

	@Override
	public String toString() {
		return "DeptByDaysAndDescEntity [id=" + id + ", applId=" + applId + ", refMode=" + refMode + ", complaintNo="
				+ complaintNo + ", createdBy=" + createdBy + ", dateOfRequest=" + dateOfRequest + ", complaintDesc="
				+ complaintDesc + ", cpdDesc=" + cpdDesc + ", cpdDescMar=" + cpdDescMar + ", address=" + address
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", division=" + division + ", ward=" + ward
				+ ", zone=" + zone + ", closed=" + closed + ", pending=" + pending + ", rejected=" + rejected
				+ ", hold=" + hold + "]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

}
