package com.abm.mainet.account.dto;

import java.util.Date;

import javax.validation.constraints.Size;

public class AccountDepositSlipUploadDto {

    private String ward;
    private String bankName;
    private String accountNumber;
    private String modeID;
    private String drawnOnBank;
    private String chequeNo;
    private String chequeDate;
    private String amount;
    private String status;
    private String chequeClearingDate;
    private String dishonorRemark;
    private Long orgid;
    private Long userId;
    private Long langId;
    private Date lmoddate;
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((chequeClearingDate == null) ? 0 : chequeClearingDate.hashCode());
		result = prime * result + ((chequeDate == null) ? 0 : chequeDate.hashCode());
		result = prime * result + ((chequeNo == null) ? 0 : chequeNo.hashCode());
		result = prime * result + ((dishonorRemark == null) ? 0 : dishonorRemark.hashCode());
		result = prime * result + ((drawnOnBank == null) ? 0 : drawnOnBank.hashCode());
		result = prime * result + ((langId == null) ? 0 : langId.hashCode());
		result = prime * result + ((lmoddate == null) ? 0 : lmoddate.hashCode());
		result = prime * result + ((modeID == null) ? 0 : modeID.hashCode());
		result = prime * result + ((orgid == null) ? 0 : orgid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((ward == null) ? 0 : ward.hashCode());
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
		AccountDepositSlipUploadDto other = (AccountDepositSlipUploadDto) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (chequeClearingDate == null) {
			if (other.chequeClearingDate != null)
				return false;
		} else if (!chequeClearingDate.equals(other.chequeClearingDate))
			return false;
		if (chequeDate == null) {
			if (other.chequeDate != null)
				return false;
		} else if (!chequeDate.equals(other.chequeDate))
			return false;
		if (chequeNo == null) {
			if (other.chequeNo != null)
				return false;
		} else if (!chequeNo.equals(other.chequeNo))
			return false;
		if (dishonorRemark == null) {
			if (other.dishonorRemark != null)
				return false;
		} else if (!dishonorRemark.equals(other.dishonorRemark))
			return false;
		if (drawnOnBank == null) {
			if (other.drawnOnBank != null)
				return false;
		} else if (!drawnOnBank.equals(other.drawnOnBank))
			return false;
		if (langId == null) {
			if (other.langId != null)
				return false;
		} else if (!langId.equals(other.langId))
			return false;
		if (lmoddate == null) {
			if (other.lmoddate != null)
				return false;
		} else if (!lmoddate.equals(other.lmoddate))
			return false;
		if (modeID == null) {
			if (other.modeID != null)
				return false;
		} else if (!modeID.equals(other.modeID))
			return false;
		if (orgid == null) {
			if (other.orgid != null)
				return false;
		} else if (!orgid.equals(other.orgid))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (ward == null) {
			if (other.ward != null)
				return false;
		} else if (!ward.equals(other.ward))
			return false;
		return true;
	}
	
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getModeID() {
		return modeID;
	}
	public void setModeID(String modeID) {
		this.modeID = modeID;
	}
	public String getDrawnOnBank() {
		return drawnOnBank;
	}
	public void setDrawnOnBank(String drawnOnBank) {
		this.drawnOnBank = drawnOnBank;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChequeClearingDate() {
		return chequeClearingDate;
	}
	public void setChequeClearingDate(String chequeClearingDate) {
		this.chequeClearingDate = chequeClearingDate;
	}
	public String getDishonorRemark() {
		return dishonorRemark;
	}
	public void setDishonorRemark(String dishonorRemark) {
		this.dishonorRemark = dishonorRemark;
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
   
}
