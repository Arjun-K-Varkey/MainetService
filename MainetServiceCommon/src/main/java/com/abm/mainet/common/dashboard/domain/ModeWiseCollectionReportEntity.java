package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ModeWiseCollectionReportEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "mode")
    private String mode;
	
	@Column(name = "New_mode_value")
    private String newModeValue;
	
	@Column(name = "modeamt")
    private Double modeAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getNewModeValue() {
		return newModeValue;
	}

	public void setNewModeValue(String newModeValue) {
		this.newModeValue = newModeValue;
	}

	public Double getModeAmount() {
		return modeAmount;
	}

	public void setModeAmount(Double modeAmount) {
		this.modeAmount = modeAmount;
	}
	
	
}
