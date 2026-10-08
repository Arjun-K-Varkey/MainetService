package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterTaxModeWardWiseData {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ward")
    private String ward;
		
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "Pay_Mode")
    private String payMode;
	
	@Column(name = "current_recovery")
    private BigDecimal currentRecovery;
	
	@Column(name = "Arreeas_recovery")
    private BigDecimal arreeasRecovery;
	

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

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public BigDecimal getCurrentRecovery() {
		return currentRecovery;
	}

	public void setCurrentRecovery(BigDecimal currentRecovery) {
		this.currentRecovery = currentRecovery;
	}

	public BigDecimal getArreeasRecovery() {
		return arreeasRecovery;
	}

	public void setArreeasRecovery(BigDecimal arreeasRecovery) {
		this.arreeasRecovery = arreeasRecovery;
	}
	
}
