package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterTaxWardWiseCollectionEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
			
	@Column(name = "ARREAR_COLLECTION")
    private BigDecimal arreeasRecovery;
	
	@Column(name = "CURRENT_COLLECTION")
    private BigDecimal currentRecovery;
	
	

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
