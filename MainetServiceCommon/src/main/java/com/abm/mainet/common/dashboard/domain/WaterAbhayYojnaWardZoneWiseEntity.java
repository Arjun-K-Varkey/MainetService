package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterAbhayYojnaWardZoneWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "Ward")
    private String ward;
	
	@Column(name = "zone")
    private String zone;
	
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "zone_id")
    private String zoneId;
	
	@Column(name = "no_of_beneficaries")
    private Double numberOfBenificiary;
	
	@Column(name = "Total_Due_Amount")
    private BigDecimal abhayYojnaAmount;
	
	@Column(name = "PenaltyWaveAmount")
    private BigDecimal penaltyWaveAmount;
	
	@Column(name = "ActualReceivedAmount")
    private BigDecimal actualReceivedAmount;
	


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

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Double getNumberOfBenificiary() {
		return numberOfBenificiary;
	}

	public void setNumberOfBenificiary(Double numberOfBenificiary) {
		this.numberOfBenificiary = numberOfBenificiary;
	}

	public BigDecimal getAbhayYojnaAmount() {
		return abhayYojnaAmount;
	}

	public void setAbhayYojnaAmount(BigDecimal abhayYojnaAmount) {
		this.abhayYojnaAmount = abhayYojnaAmount;
	}

	public BigDecimal getPenaltyWaveAmount() {
		return penaltyWaveAmount;
	}

	public void setPenaltyWaveAmount(BigDecimal penaltyWaveAmount) {
		this.penaltyWaveAmount = penaltyWaveAmount;
	}

	
	public BigDecimal getActualReceivedAmount() {
		return actualReceivedAmount;
	}

	public void setActualReceivedAmount(BigDecimal actualReceivedAmount) {
		this.actualReceivedAmount = actualReceivedAmount;
	}

	
	
}
