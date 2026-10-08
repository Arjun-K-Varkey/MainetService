package com.abm.mainet.common.dashboard.dto;

public class DeptByDaysAndDescDTO {

	private int id;
	private String applId;
	private String refMode;
	private String complaintNo;
	private String createdBy;
	private String dateOfRequest;
	private String complaintDesc;
	private String cpdDesc;
	private String cpdDescMar;
	private String address;
	private String latitude;
	private String longitude;
	private String division;
	private String ward;
	private String zone;
	private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DeptByDaysAndDescDTO [id=" + id + ", applId=" + applId + ", refMode=" + refMode + ", complaintNo="
				+ complaintNo + ", createdBy=" + createdBy + ", dateOfRequest=" + dateOfRequest + ", complaintDesc="
				+ complaintDesc + ", cpdDesc=" + cpdDesc + ", cpdDescMar=" + cpdDescMar + ", address=" + address
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", division=" + division + ", ward=" + ward
				+ ", zone=" + zone + ", status=" + status + "]";
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
