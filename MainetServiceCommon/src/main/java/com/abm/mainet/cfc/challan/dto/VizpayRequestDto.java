package com.abm.mainet.cfc.challan.dto;

import java.io.Serializable;

public class VizpayRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mid;
	private String tid;
	private String tran_type;
	private String amount;
	private String bill_no;
	private String erp_tran_id;
	private String erp_client_id;
	private String source_id;
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getErp_tran_id() {
		return erp_tran_id;
	}
	public void setErp_tran_id(String erp_tran_id) {
		this.erp_tran_id = erp_tran_id;
	}
	public String getErp_client_id() {
		return erp_client_id;
	}
	public void setErp_client_id(String erp_client_id) {
		this.erp_client_id = erp_client_id;
	}
	public String getSource_id() {
		return source_id;
	}
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	
	
}
