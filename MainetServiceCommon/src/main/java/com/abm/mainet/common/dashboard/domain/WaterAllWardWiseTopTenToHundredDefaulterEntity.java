package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterAllWardWiseTopTenToHundredDefaulterEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ward")
    private String ward;
	
	@Column(name = "zone")
    private String zone;
		
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "connection_no")
    private String connectionNo;
	
	@Column(name = "outstanding")
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

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getConnectionNo() {
		return connectionNo;
	}

	public void setConnectionNo(String connectionNo) {
		this.connectionNo = connectionNo;
	}

	public BigDecimal getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(BigDecimal outstanding) {
		this.outstanding = outstanding;
	}
	
}
