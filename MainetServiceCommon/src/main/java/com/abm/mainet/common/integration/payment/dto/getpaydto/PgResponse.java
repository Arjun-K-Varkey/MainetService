package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PgResponse implements Serializable {
   private static final long serialVersionUID = 1L;
   private String status;
   private String message;
   private String mid;
   private String terminalId;
   private String response;
   private String ru;
   private Map<String, Object> redirectParams;

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getResponse() {
      return this.response;
   }

   public void setResponse(String response) {
      this.response = response;
   }

   public String getMid() {
      return this.mid;
   }

   public void setMid(String mid) {
      this.mid = mid;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getTerminalId() {
      return this.terminalId;
   }

   public void setTerminalId(String terminalId) {
      this.terminalId = terminalId;
   }

   public String getRu() {
      return this.ru;
   }

   public void setRu(String ru) {
      this.ru = ru;
   }

   public Map<String, Object> getRedirectParams() {
	return redirectParams;
}

public void setRedirectParams(Map<String, Object> redirectParams) {
	this.redirectParams = redirectParams;
}

public String toString() {
      return "PgResponse [status=" + this.status + ", message=" + this.message + ", mid=" + this.mid + ", terminalId=" + this.terminalId + ", response=" + this.response + ", ru=" + this.ru + ", redirectParams=" + this.redirectParams+"]";
   }
}
