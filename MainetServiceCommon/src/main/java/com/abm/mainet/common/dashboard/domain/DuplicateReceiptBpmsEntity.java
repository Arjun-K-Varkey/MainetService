package com.abm.mainet.common.dashboard.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DuplicateReceiptBpmsEntity {
	
	
	@Id
	@Column(name = "num")
	private int id;
		
	@Column(name="PROPERTY_NUMBER")
	private String PROPERTY_NUMBER;

	@Column(name = "RM_RCPTNO")
	private int RM_RCPTNO;
	
	@Column(name = "RM_DATE")
	private Date RM_DATE;
	
	@Column(name = "PROPOSAL_NUMBER")
	private String PROPOSAL_NUMBER;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPROPERTY_NUMBER() {
		return PROPERTY_NUMBER;
	}

	public void setPROPERTY_NUMBER(String pROPERTY_NUMBER) {
		PROPERTY_NUMBER = pROPERTY_NUMBER;
	}

	public int getRM_RCPTNO() {
		return RM_RCPTNO;
	}

	public void setRM_RCPTNO(int rM_RCPTNO) {
		RM_RCPTNO = rM_RCPTNO;
	}

	public Date getRM_DATE() {
		return RM_DATE;
	}

	public void setRM_DATE(Date rM_DATE) {
		RM_DATE = rM_DATE;
	}

	public String getPROPOSAL_NUMBER() {
		return PROPOSAL_NUMBER;
	}

	public void setPROPOSAL_NUMBER(String pROPOSAL_NUMBER) {
		PROPOSAL_NUMBER = pROPOSAL_NUMBER;
	}

	

}
