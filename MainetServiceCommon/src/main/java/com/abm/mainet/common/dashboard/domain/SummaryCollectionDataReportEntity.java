package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SummaryCollectionDataReportEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "Wards")
    private String Wards;
	
	@Column(name = "Zones")
    private String Zones;
	
	@Column(name = "TotalCollection")
    private Double TotalCollection;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWards() {
		return Wards;
	}

	public void setWards(String wards) {
		Wards = wards;
	}

	public String getZones() {
		return Zones;
	}

	public void setZones(String zones) {
		Zones = zones;
	}

	public Double getTotalCollection() {
		return TotalCollection;
	}

	public void setTotalCollection(Double totalCollection) {
		TotalCollection = totalCollection;
	}
	
	
	
}
