package com.abm.mainet.account.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Size;

public class BankReconclitionUploadDto {

	private String transactionDate;
	private String paymentDate;
	private String narrative;
	private String customerReferenceNo;
	private String chequeNo;
	private String runningBalance;
	private BigDecimal debit;
	private BigDecimal credit;
	public void setRunningBalance(String runningBalance) {
		this.runningBalance = runningBalance;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	private Long orgid;
	private Long userId;
	private Long langId;
	private Date lmoddate;
	@Size(max = 100)
	private String lgIpMac;

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getCustomerReferenceNo() {
		return customerReferenceNo;
	}

	public void setCustomerReferenceNo(String customerReferenceNo) {
		this.customerReferenceNo = customerReferenceNo;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}



	public Long getOrgid() {
		return orgid;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public Date getLmoddate() {
		return lmoddate;
	}

	public void setLmoddate(Date lmoddate) {
		this.lmoddate = lmoddate;
	}

	public String getLgIpMac() {
		return lgIpMac;
	}

	public void setLgIpMac(String lgIpMac) {
		this.lgIpMac = lgIpMac;
	}

	public String getRunningBalance() {
		return runningBalance;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chequeNo == null) ? 0 : chequeNo.hashCode());
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((customerReferenceNo == null) ? 0 : customerReferenceNo.hashCode());
		result = prime * result + ((debit == null) ? 0 : debit.hashCode());
		result = prime * result + ((langId == null) ? 0 : langId.hashCode());
		result = prime * result + ((lgIpMac == null) ? 0 : lgIpMac.hashCode());
		result = prime * result + ((lmoddate == null) ? 0 : lmoddate.hashCode());
		result = prime * result + ((narrative == null) ? 0 : narrative.hashCode());
		result = prime * result + ((orgid == null) ? 0 : orgid.hashCode());
		result = prime * result + ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + ((runningBalance == null) ? 0 : runningBalance.hashCode());
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankReconclitionUploadDto other = (BankReconclitionUploadDto) obj;
		if (chequeNo == null) {
			if (other.chequeNo != null)
				return false;
		} else if (!chequeNo.equals(other.chequeNo))
			return false;
		if (credit == null) {
			if (other.credit != null)
				return false;
		} else if (!credit.equals(other.credit))
			return false;
		if (customerReferenceNo == null) {
			if (other.customerReferenceNo != null)
				return false;
		} else if (!customerReferenceNo.equals(other.customerReferenceNo))
			return false;
		if (debit == null) {
			if (other.debit != null)
				return false;
		} else if (!debit.equals(other.debit))
			return false;
		if (langId == null) {
			if (other.langId != null)
				return false;
		} else if (!langId.equals(other.langId))
			return false;
		if (lgIpMac == null) {
			if (other.lgIpMac != null)
				return false;
		} else if (!lgIpMac.equals(other.lgIpMac))
			return false;
		if (lmoddate == null) {
			if (other.lmoddate != null)
				return false;
		} else if (!lmoddate.equals(other.lmoddate))
			return false;
		if (narrative == null) {
			if (other.narrative != null)
				return false;
		} else if (!narrative.equals(other.narrative))
			return false;
		if (orgid == null) {
			if (other.orgid != null)
				return false;
		} else if (!orgid.equals(other.orgid))
			return false;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
			return false;
		if (runningBalance == null) {
			if (other.runningBalance != null)
				return false;
		} else if (!runningBalance.equals(other.runningBalance))
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
