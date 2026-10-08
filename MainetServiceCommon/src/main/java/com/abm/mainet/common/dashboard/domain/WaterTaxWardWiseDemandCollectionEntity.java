package com.abm.mainet.common.dashboard.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterTaxWardWiseDemandCollectionEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "ward")
    private String ward;
		
	@Column(name = "ward_id")
    private String wardId;
	
	@Column(name = "Total_Demand")
    private BigDecimal totalDemand;
	
	@Column(name = "Total_Recovery")
    private BigDecimal totalRecovery;
	
	@Column(name = "Balance_Arreas")
    private BigDecimal balanceArreas;
	
	@Column(name = "Per_Recovery")
    private BigDecimal perRecovery;
	
	


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

	public BigDecimal getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}

	public BigDecimal getTotalRecovery() {
		return totalRecovery;
	}

	public void setTotalRecovery(BigDecimal totalRecovery) {
		this.totalRecovery = totalRecovery;
	}

	public BigDecimal getBalanceArreas() {
		return balanceArreas;
	}

	public void setBalanceArreas(BigDecimal balanceArreas) {
		this.balanceArreas = balanceArreas;
	}

	public BigDecimal getPerRecovery() {
		return perRecovery;
	}

	public void setPerRecovery(BigDecimal perRecovery) {
		this.perRecovery = perRecovery;
	}


	
	
}
