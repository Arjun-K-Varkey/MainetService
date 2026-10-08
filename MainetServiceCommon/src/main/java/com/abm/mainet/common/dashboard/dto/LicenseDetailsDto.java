package com.abm.mainet.common.dashboard.dto;

public class LicenseDetailsDto {
	private int rowNumber;
	private String licenseNo;
	private String ward2;
	private String businessName;
	private String licenseCategory;
	private String licenseFromDate;
	private String licenseToDate;
	private String trdBusinessName;

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getWard2() {
		return ward2;
	}

	public void setWard2(String ward2) {
		this.ward2 = ward2;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getLicenseCategory() {
		return licenseCategory;
	}

	public void setLicenseCategory(String licenseCategory) {
		this.licenseCategory = licenseCategory;
	}

	public String getLicenseFromDate() {
		return licenseFromDate;
	}

	public void setLicenseFromDate(String licenseFromDate) {
		this.licenseFromDate = licenseFromDate;
	}

	public String getLicenseToDate() {
		return licenseToDate;
	}

	public void setLicenseToDate(String licenseToDate) {
		this.licenseToDate = licenseToDate;
	}

	public String getTrdBusinessName() {
		return trdBusinessName;
	}

	public void setTrdBusinessName(String trdBusinessName) {
		this.trdBusinessName = trdBusinessName;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
}
