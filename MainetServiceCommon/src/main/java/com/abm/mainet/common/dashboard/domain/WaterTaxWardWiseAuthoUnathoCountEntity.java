package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterTaxWardWiseAuthoUnathoCountEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ward")
    private String ward;
		
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "CCN_Category")
    private String ccnCategory;
	
	@Column(name = "COUNT")
    private BigDecimal count;
	
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public String getCcnCategory() {
		return ccnCategory;
	}

	public void setCcnCategory(String ccnCategory) {
		this.ccnCategory = ccnCategory;
	}


	
	
}
