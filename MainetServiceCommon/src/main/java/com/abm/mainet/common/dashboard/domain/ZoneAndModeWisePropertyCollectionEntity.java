package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ZoneAndModeWisePropertyCollectionEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ZONE")
    private String zone;
	
	@Column(name = "MODE")
    private String mode;
	
	@Column(name = "ARREARS_COLLECTION")
    private Double arrearsCollection;
	
	@Column(name = "CURRENT_COLLECTION")
    private Double currentCollection;
	
	@Column(name = "ADVANCE_COLLECTION")
    private Double advanceCollection;


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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Double getArrearsCollection() {
		return arrearsCollection;
	}

	public void setArrearsCollection(Double arrearsCollection) {
		this.arrearsCollection = arrearsCollection;
	}

	public Double getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(Double currentCollection) {
		this.currentCollection = currentCollection;
	}

	public Double getAdvanceCollection() {
		return advanceCollection;
	}

	public void setAdvanceCollection(Double advanceCollection) {
		this.advanceCollection = advanceCollection;
	}

	
	
}
