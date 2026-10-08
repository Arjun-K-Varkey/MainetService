package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LicensedtlByLicNoAndDatesEntity {
	@Id
	@Column(name = "num")
	private int rowNumber;
	@Column(name = "TRD_LICNO")
	private String licenseNo;
	@Column(name = "TRD_WARD2")
	private String ward2;
	@Column(name = "TRD_BUSNM")
	private String businessName;
	@Column(name = "lic_catg")
	private String licenseCategory;
	@Column(name = "lic_sub_Catg")
	private String licenseSubCategory;
	@Column(name = "TRD_LICFROM_DATE")
	private String licenseFromDate;
	@Column(name = "TRD_LICTO_DATE")
	private String licenseToDate;
	@Column(name = "TRD_BUSADD")
	private String businessAddress;
	

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

	public String getLicenseSubCategory() {
		return licenseSubCategory;
	}

	public void setLicenseSubCategory(String licenseSubCategory) {
		this.licenseSubCategory = licenseSubCategory;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	

}
