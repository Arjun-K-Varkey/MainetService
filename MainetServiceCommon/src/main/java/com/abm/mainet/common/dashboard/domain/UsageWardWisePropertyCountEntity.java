package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UsageWardWisePropertyCountEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
	
	@Column(name = "RESIDENTIAL_PROPERTY_COUNT")
    private Double residentialPropertyCount;
	
	@Column(name = "NON_RESIDENTIAL_PROPERTY_COUNT")
    private Double nonResidentialPropertyCount;
	
	@Column(name = "TOTAL_PROPERTY_COUNT")
    private Double totalPropertyCount;
	


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

	public Double getResidentialPropertyCount() {
		return residentialPropertyCount;
	}

	public void setResidentialPropertyCount(Double residentialPropertyCount) {
		this.residentialPropertyCount = residentialPropertyCount;
	}

	public Double getNonResidentialPropertyCount() {
		return nonResidentialPropertyCount;
	}

	public void setNonResidentialPropertyCount(Double nonResidentialPropertyCount) {
		this.nonResidentialPropertyCount = nonResidentialPropertyCount;
	}

	public Double getTotalPropertyCount() {
		return totalPropertyCount;
	}

	public void setTotalPropertyCount(Double totalPropertyCount) {
		this.totalPropertyCount = totalPropertyCount;
	}

	
	
	
}
