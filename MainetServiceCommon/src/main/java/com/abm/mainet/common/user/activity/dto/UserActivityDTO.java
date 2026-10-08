package com.abm.mainet.common.user.activity.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author AnwarulHasan
 * @since 29-Jan-2025
 */
public class UserActivityDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private long userActId;

	private Long applicationId;
	
	private Long serviceId;
	
	private Long orgId;

	private Long createdBy;

	private Date createdDate;

	private String lgIpMac;
	
	private String referenceNo;
	
	private String action;

	public long getUserActId() {
		return userActId;
	}

	public void setUserActId(long userActId) {
		this.userActId = userActId;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLgIpMac() {
		return lgIpMac;
	}

	public void setLgIpMac(String lgIpMac) {
		this.lgIpMac = lgIpMac;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


}
