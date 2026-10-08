package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LicensedtlByNoAndAppIdEntity {
	@Id
	@Column(name = "num")
	private int id;
	@Column(name = "TRD_LICNO")
	private String licenseNo;
	@Column(name = "APM_APPLICATION_ID")
	private Long  applicationId;
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
