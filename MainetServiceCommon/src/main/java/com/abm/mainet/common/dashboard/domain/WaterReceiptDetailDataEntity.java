package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterReceiptDetailDataEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "RM_RCPTID")
	private String rmRcptId;

	@Column(name = "TAX_ID")
	private String taxId;

	@Column(name = "RM_DEMAND")
	private String rmDemand;

	@Column(name = "RF_FEEAMOUNT")
	private String rfFeeAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRmRcptId() {
		return rmRcptId;
	}

	public void setRmRcptId(String rmRcptId) {
		this.rmRcptId = rmRcptId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getRmDemand() {
		return rmDemand;
	}

	public void setRmDemand(String rmDemand) {
		this.rmDemand = rmDemand;
	}

	public String getRfFeeAmount() {
		return rfFeeAmount;
	}

	public void setRfFeeAmount(String rfFeeAmount) {
		this.rfFeeAmount = rfFeeAmount;
	}

}
