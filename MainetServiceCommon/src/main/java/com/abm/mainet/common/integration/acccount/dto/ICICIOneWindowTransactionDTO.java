package com.abm.mainet.common.integration.acccount.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ICICIOneWindowTransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String merchantId;
    private String merchantTxnNo;
    private String transactionId;
    private String amount;
    private String currencyCode;
    private String payType;
    private String customerEmailID;
    private String transactionType;
    private String returnURL;
    private String txnDate;
    private String secureHash;
    private String customerMobileNo;
    private String addlParam1;
    private String addlParam2;
    private String name;
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantTxnNo() {
        return merchantTxnNo;
    }

    public void setMerchantTxnNo(String merchantTxnNo) {
        this.merchantTxnNo = merchantTxnNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCustomerEmailID() {
        return customerEmailID;
    }

    public void setCustomerEmailID(String customerEmailID) {
        this.customerEmailID = customerEmailID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getSecureHash() {
        return secureHash;
    }

    public void setSecureHash(String secureHash) {
        this.secureHash = secureHash;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public String getAddlParam1() {
        return addlParam1;
    }

    public void setAddlParam1(String addlParam1) {
        this.addlParam1 = addlParam1;
    }

    public String getAddlParam2() {
        return addlParam2;
    }

    public void setAddlParam2(String addlParam2) {
        this.addlParam2 = addlParam2;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ICICIOneWindowTransactionDTO [merchantId=" + merchantId + ", merchantTxnNo=" + merchantTxnNo
				+ ", transactionId=" + transactionId + ", amount=" + amount + ", currencyCode=" + currencyCode
				+ ", payType=" + payType + ", customerEmailID=" + customerEmailID + ", transactionType="
				+ transactionType + ", returnURL=" + returnURL + ", txnDate=" + txnDate + ", secureHash=" + secureHash
				+ ", customerMobileNo=" + customerMobileNo + ", addlParam1=" + addlParam1 + ", addlParam2=" + addlParam2
				+ "]";
	}
}