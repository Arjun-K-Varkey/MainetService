package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.io.Serializable;

public class PgStatusRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mid;
	private String paymentId;
	private String referenceNo;
	private String status;
	private String terminalId;
	private String req;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

	@Override
	public String toString() {
		return "PgStatusRequest [mid=" + mid + ", paymentId=" + paymentId + ", referenceNo=" + referenceNo + ", status="
				+ status + ", terminalId=" + terminalId + ", req=" + req + "]";
	}
}
