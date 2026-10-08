package com.abm.mainet.common.workflow.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_area_land_rate")
public class TbAreaLandRateEntity implements Serializable {

	private static final long serialVersionUID = 4589206275885772695L;

	/**
	 * 
	 */
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "TB_AREA_LAND_ID")
	private Long tbAreaLandId;
	@Column(name = "as_areadesc")
	private String area;
	@Column(name = "surveytype")
	private String surveyType;
	@Column(name = "PM_SURVEY_NUMBER")
	private String surveyNo;
	@Column(name = "LM_FROMDT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lmFromDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LM_TODT")
	private Date lmToDate;
	@Column(name = "lm_landrt")
	private double amt;
	@Column(name = "ORGID")
	private Long orgId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Column(name = "LG_IP_MAC")
	private String lgIpMac;

	@Column(name = "LG_IP_MAC_UPD")
	private String lgIpMacUpd;

	public Long getTbAreaLandId() {
		return tbAreaLandId;
	}

	public void setTbAreaLandId(Long tbAreaLandId) {
		this.tbAreaLandId = tbAreaLandId;
	}

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



	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String[] getPkValues() {
		return new String[] { "COMMON", "tb_area_land_rate", "TB_AREA_LAND_ID" };
	}

	public String getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
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
