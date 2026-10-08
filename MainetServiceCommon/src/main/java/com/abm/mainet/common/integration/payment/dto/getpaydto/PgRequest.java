package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.io.Serializable;

public class PgRequest implements Serializable {
   private static final long serialVersionUID = 1L;
   private String mid;
   private String amount;
   private String merchantTransactionId;
   private String transactionDate;
   private String terminalId;
   private String vpa;
   private String udf1;
   private String udf2;
   private String udf3;
   private String udf4;
   private String udf5;
   private String udf6;
   private String udf7;
   private String udf8;
   private String udf9;
   private String udf10;
   private String ru;
   private String callbackUrl;
   private String currency;
   private String paymentMode;
   private String bankId;
   private String txnType;
   private String productType;
   private String txnNote;
   private String paymentId;

   public String getAmount() {
      return this.amount;
   }

   public void setAmount(String amount) {
      this.amount = amount;
   }

   public String getMerchantTransactionId() {
      return this.merchantTransactionId;
   }

   public void setMerchantTransactionId(String merchantTransactionId) {
      this.merchantTransactionId = merchantTransactionId;
   }

   public String getTransactionDate() {
      return this.transactionDate;
   }

   public void setTransactionDate(String transactionDate) {
      this.transactionDate = transactionDate;
   }

   public String getTerminalId() {
      return this.terminalId;
   }

   public void setTerminalId(String terminalId) {
      this.terminalId = terminalId;
   }

   public String getUdf1() {
      return this.udf1;
   }

   public void setUdf1(String udf1) {
      this.udf1 = udf1;
   }

   public String getUdf2() {
      return this.udf2;
   }

   public void setUdf2(String udf2) {
      this.udf2 = udf2;
   }

   public String getUdf3() {
      return this.udf3;
   }

   public void setUdf3(String udf3) {
      this.udf3 = udf3;
   }

   public String getUdf4() {
      return this.udf4;
   }

   public void setUdf4(String udf4) {
      this.udf4 = udf4;
   }

   public String getUdf5() {
      return this.udf5;
   }

   public void setUdf5(String udf5) {
      this.udf5 = udf5;
   }

   public String getUdf6() {
      return this.udf6;
   }

   public void setUdf6(String udf6) {
      this.udf6 = udf6;
   }

   public String getUdf7() {
      return this.udf7;
   }

   public void setUdf7(String udf7) {
      this.udf7 = udf7;
   }

   public String getUdf8() {
      return this.udf8;
   }

   public void setUdf8(String udf8) {
      this.udf8 = udf8;
   }

   public String getUdf9() {
      return this.udf9;
   }

   public void setUdf9(String udf9) {
      this.udf9 = udf9;
   }

   public String getUdf10() {
      return this.udf10;
   }

   public void setUdf10(String udf10) {
      this.udf10 = udf10;
   }

   public String getRu() {
      return this.ru;
   }

   public void setRu(String ru) {
      this.ru = ru;
   }

   public String getCallbackUrl() {
      return this.callbackUrl;
   }

   public void setCallbackUrl(String callbackUrl) {
      this.callbackUrl = callbackUrl;
   }

   public String getCurrency() {
      return this.currency;
   }

   public void setCurrency(String currency) {
      this.currency = currency;
   }

   public String getPaymentMode() {
      return this.paymentMode;
   }

   public void setPaymentMode(String paymentMode) {
      this.paymentMode = paymentMode;
   }

   public String getBankId() {
      return this.bankId;
   }

   public void setBankId(String bankId) {
      this.bankId = bankId;
   }

   public String getTxnType() {
      return this.txnType;
   }

   public void setTxnType(String txnType) {
      this.txnType = txnType;
   }

   public String getProductType() {
      return this.productType;
   }

   public void setProductType(String productType) {
      this.productType = productType;
   }

   public String getTxnNote() {
      return this.txnNote;
   }

   public void setTxnNote(String txnNote) {
      this.txnNote = txnNote;
   }

   public String getMid() {
      return this.mid;
   }

   public void setMid(String mid) {
      this.mid = mid;
   }

   public String getVpa() {
      return this.vpa;
   }

   public void setVpa(String vpa) {
      this.vpa = vpa;
   }

public String getPaymentId() {
	return paymentId;
}

public void setPaymentId(String paymentId) {
	this.paymentId = paymentId;
}
}
