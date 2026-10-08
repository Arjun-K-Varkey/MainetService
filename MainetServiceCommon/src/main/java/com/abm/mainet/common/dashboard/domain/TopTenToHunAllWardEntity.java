package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TopTenToHunAllWardEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
	
	@Column(name = "ZONE")
    private String zone;
	
	@Column(name = "PROPERTY_NUMBER")
    private String property_number;
	
	@Column(name = "FLAT_NUMBER")
    private String flat_number;
	
	@Column(name = "OWNER_NAME")
    private String owner_name;
	
	@Column(name = "OCCUPIER_NAME")
    private String occupier_name;
	
	@Column(name = "OUTSTANDING")
    private BigDecimal outstanding;
	
	

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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getProperty_number() {
		return property_number;
	}

	public void setProperty_number(String property_number) {
		this.property_number = property_number;
	}

	public String getFlat_number() {
		return flat_number;
	}

	public void setFlat_number(String flat_number) {
		this.flat_number = flat_number;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getOccupier_name() {
		return occupier_name;
	}

	public void setOccupier_name(String occupier_name) {
		this.occupier_name = occupier_name;
	}

	public BigDecimal getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(BigDecimal outstanding) {
		this.outstanding = outstanding;
	}

	

	
	
}
