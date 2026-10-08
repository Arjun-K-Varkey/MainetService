package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.io.Serializable;

public class PgRequestWrapper implements Serializable {
   private static final long serialVersionUID = 1L;
   private String mid;
   private String terminalId;
   private String req;

   public String getMid() {
      return this.mid;
   }

   public void setMid(String mid) {
      this.mid = mid;
   }

   public String getTerminalId() {
      return this.terminalId;
   }

   public void setTerminalId(String terminalId) {
      this.terminalId = terminalId;
   }

   public String getReq() {
      return this.req;
   }

   public void setReq(String req) {
      this.req = req;
   }
}
