package com.abm.mainet.common.integration.payment.dto.getpaydto;

public class ResponseWrapper {
   private String paymentId;
   private String paymentUrl;
   private String qrIntent;
   private String qr;

   public String getPaymentId() {
      return this.paymentId;
   }

   public void setPaymentId(String paymentId) {
      this.paymentId = paymentId;
   }

   public String getPaymentUrl() {
      return this.paymentUrl;
   }

   public void setPaymentUrl(String paymentUrl) {
      this.paymentUrl = paymentUrl;
   }

   public String getQrIntent() {
      return this.qrIntent;
   }

   public void setQrIntent(String qrIntent) {
      this.qrIntent = qrIntent;
   }

   public String getQr() {
      return this.qr;
   }

   public void setQr(String qr) {
      this.qr = qr;
   }

   public String toString() {
      return "ResponseWrapper [paymentId=" + this.paymentId + ", paymentUrl=" + this.paymentUrl + ", qrIntent=" + this.qrIntent + ", qr=" + this.qr + "]";
   }
}
