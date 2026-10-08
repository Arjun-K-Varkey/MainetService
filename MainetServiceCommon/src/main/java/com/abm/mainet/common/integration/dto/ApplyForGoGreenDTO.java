package com.abm.mainet.common.integration.dto;

import java.io.Serializable;

public class ApplyForGoGreenDTO implements Serializable {
	private static final long serialVersionUID = 3900381921060186290L;
	private String deptCode;
	private String refNo;
	private String flatNo;
	private String ownerName;
	private String occupierName;
	private boolean agree;
	private String emailId;
	private String mobNo;
	private String address;
	private String userOtp;
	private Long orgId;
	private String goGreenFlag;
	private String assId;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOccupierName() {
		return occupierName;
	}

	public void setOccupierName(String occupierName) {
		this.occupierName = occupierName;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	@Override
	public String toString() {
		return "ApplyForGoGreenDTO [deptCode=" + deptCode + ", refNo=" + refNo + ", flatNo=" + flatNo + ", ownerName="
				+ ownerName + ", occupierName=" + occupierName + ", agree=" + agree + ", emailId=" + emailId
				+ ", mobNo=" + mobNo + "]";
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserOtp() {
		return userOtp;
	}

	public void setUserOtp(String userOtp) {
		this.userOtp = userOtp;
	}

	public String getGoGreenFlag() {
		return goGreenFlag;
	}

	public void setGoGreenFlag(String goGreenFlag) {
		this.goGreenFlag = goGreenFlag;
	}

	public String getAssId() {
		return assId;
	}

	public void setAssId(String assId) {
		this.assId = assId;
	}
}
