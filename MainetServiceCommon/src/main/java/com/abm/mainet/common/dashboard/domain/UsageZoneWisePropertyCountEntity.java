package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UsageZoneWisePropertyCountEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "zone")
    private String zone;
	
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

	
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
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
