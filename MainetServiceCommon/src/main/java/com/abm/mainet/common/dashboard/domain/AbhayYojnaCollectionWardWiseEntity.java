package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AbhayYojnaCollectionWardWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
	
	@Column(name = "NUMBER OF BENIFICIARY")
    private Double numberOfBenificiary;
	
	@Column(name = "ABHAY YOJANA AMOUNT")
    private Double abhayYojnaAmount;
	
	@Column(name = "WEB REBATE AMOUNT")
    private Double webRebateAmount;
	
	@Column(name = "EARLY REBATE AMOUNT")
    private Double earlyRebateAmount;
	
	@Column(name = "TOTAL REBATE AMOUNT")
    private Double totalRebateAmount;
	
	@Column(name = "ACTUAL RECEIPT AMOUNT")
    private Double actualReceiptAmount;


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

	public Double getNumberOfBenificiary() {
		return numberOfBenificiary;
	}

	public void setNumberOfBenificiary(Double numberOfBenificiary) {
		this.numberOfBenificiary = numberOfBenificiary;
	}

	public Double getAbhayYojnaAmount() {
		return abhayYojnaAmount;
	}

	public void setAbhayYojnaAmount(Double abhayYojnaAmount) {
		this.abhayYojnaAmount = abhayYojnaAmount;
	}

	public Double getWebRebateAmount() {
		return webRebateAmount;
	}

	public void setWebRebateAmount(Double webRebateAmount) {
		this.webRebateAmount = webRebateAmount;
	}

	public Double getEarlyRebateAmount() {
		return earlyRebateAmount;
	}

	public void setEarlyRebateAmount(Double earlyRebateAmount) {
		this.earlyRebateAmount = earlyRebateAmount;
	}

	public Double getTotalRebateAmount() {
		return totalRebateAmount;
	}

	public void setTotalRebateAmount(Double totalRebateAmount) {
		this.totalRebateAmount = totalRebateAmount;
	}

	public Double getActualReceiptAmount() {
		return actualReceiptAmount;
	}

	public void setActualReceiptAmount(Double actualReceiptAmount) {
		this.actualReceiptAmount = actualReceiptAmount;
	}

	
	
}
