package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterWardAndZoneWiseUsageCountEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ward")
    private String ward;
	
	@Column(name = "zone")
    private String zone;
	
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "zone_id")
    private String zoneId;
	
	@Column(name = "Usage_Category")
    private String usageCategory;
	
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

	public String getUsageCategory() {
		return usageCategory;
	}

	public void setUsageCategory(String usageCategory) {
		this.usageCategory = usageCategory;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	
	
}
