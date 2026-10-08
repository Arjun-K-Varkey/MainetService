package com.abm.mainet.common.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiptSummaryDTO {

	private Date rmDate;
    private BigDecimal rmAmount;

    public ReceiptSummaryDTO(Date rmDate, BigDecimal rmAmount) {
        this.rmDate = rmDate;
        this.rmAmount = rmAmount;
    }

	public Date getRmDate() {
		return rmDate;
	}

	public void setRmDate(Date rmDate) {
		this.rmDate = rmDate;
	}

	public BigDecimal getRmAmount() {
		return rmAmount;
	}

	public void setRmAmount(BigDecimal rmAmount) {
		this.rmAmount = rmAmount;
	}
    
}
