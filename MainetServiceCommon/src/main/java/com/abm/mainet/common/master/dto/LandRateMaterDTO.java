package com.abm.mainet.common.master.dto;

import java.io.Serializable;
import java.util.Date;


public class LandRateMaterDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 681602790395238997L;
	/**
	 * 
	 */
	private Long tbAreaLandId;
	private String area;
	private String surveyType;
	private String surveyNo;
	private String  fromDate;
	private String toDate;
	private Long orgId;
	private String amt;
	private String isUpdate;
	private Date createdDate;
	private Long createdBy;
	private Date updatedDate;
	private Long updatedBy;
	private String lgIpMac;
	private String lgIpMacUpd;
	private Date lmFromDate;
	private Date lmToDate;
	

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}


	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public Long getTbAreaLandId() {
		return tbAreaLandId;
	}

	public void setTbAreaLandId(Long tbAreaLandId) {
		this.tbAreaLandId = tbAreaLandId;
	}

	@Override
	public String toString() {
		return "LandRateMaterDTO [area=" + area + ", survetType=" + surveyType + ", surveyNo=" + surveyNo
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", amt=" + amt + "]";
	}

	public String getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getLgIpMac() {
		return lgIpMac;
	}

	public void setLgIpMac(String lgIpMac) {
		this.lgIpMac = lgIpMac;
	}

	public String getLgIpMacUpd() {
		return lgIpMacUpd;
	}

	public void setLgIpMacUpd(String lgIpMacUpd) {
		this.lgIpMacUpd = lgIpMacUpd;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Date getLmFromDate() {
		return lmFromDate;
	}

	public void setLmFromDate(Date lmFromDate) {
		this.lmFromDate = lmFromDate;
	}

	public Date getLmToDate() {
		return lmToDate;
	}

	public void setLmToDate(Date lmToDate) {
		this.lmToDate = lmToDate;
	}
}
