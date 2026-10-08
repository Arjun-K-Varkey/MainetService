package com.abm.mainet.materialmgmt.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author divya.marshettiwar
 *
 */

public class InspectionItemsDTO implements Serializable{
	
	private static final long serialVersionUID = 1326705884215245016L;
	
	private Long grnitementryid;
	private Long grnid;
	private Long storeid;
	private Long itemid;
	private Long grnitemid;
	private String Itemno;
	private Double quantity;
	private String decision;
	private Date mfgdate;
	private Date expirydate;
	private Long rejectionreason;
	private Long binlocation;
	private Long returnid;
	private String Status;
	private String itemDesc;
	private double receivedqty; 
	private String serialNumFrom;
	private String serialNumTo;
	private String management;
	private Long orgId;
	private Long userId;
	private Long langId;
	private Date lModDate;
	private Long updatedBy;
	private Date updatedDate;
	private String lgIpMac;
	private String lgIpMacUpd;
	
	public Long getGrnitementryid() {
		return grnitementryid;
	}
	public void setGrnitementryid(Long grnitementryid) {
		this.grnitementryid = grnitementryid;
	}
	public Long getGrnid() {
		return grnid;
	}
	public void setGrnid(Long grnid) {
		this.grnid = grnid;
	}
	public Long getStoreid() {
		return storeid;
	}
	public void setStoreid(Long storeid) {
		this.storeid = storeid;
	}
	public Long getItemid() {
		return itemid;
	}
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	public Long getGrnitemid() {
		return grnitemid;
	}
	public void setGrnitemid(Long grnitemid) {
		this.grnitemid = grnitemid;
	}
	public String getItemno() {
		return Itemno;
	}
	public void setItemno(String itemno) {
		Itemno = itemno;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public Date getMfgdate() {
		return mfgdate;
	}
	public void setMfgdate(Date mfgdate) {
		this.mfgdate = mfgdate;
	}
	public Date getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}
	public Long getRejectionreason() {
		return rejectionreason;
	}
	public void setRejectionreason(Long rejectionreason) {
		this.rejectionreason = rejectionreason;
	}
	public Long getBinlocation() {
		return binlocation;
	}
	public void setBinlocation(Long binlocation) {
		this.binlocation = binlocation;
	}
	public Long getReturnid() {
		return returnid;
	}
	public void setReturnid(Long returnid) {
		this.returnid = returnid;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
	public Date getlModDate() {
		return lModDate;
	}
	public void setlModDate(Date lModDate) {
		this.lModDate = lModDate;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getLgIpMac() {
		return lgIpMac;
	}
	public void setLgIpMac(String lgIpMac) {
		this.lgIpMac = lgIpMac;
	}
	public String getLgIpMacUpd() {
		return lgIpMacUpd;
	}
	public void setLgIpMacUpd(String lgIpMacUpd) {
		this.lgIpMacUpd = lgIpMacUpd;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public double getReceivedqty() {
		return receivedqty;
	}
	public void setReceivedqty(double receivedqty) {
		this.receivedqty = receivedqty;
	}
	public String getSerialNumFrom() {
		return serialNumFrom;
	}
	public void setSerialNumFrom(String serialNumFrom) {
		this.serialNumFrom = serialNumFrom;
	}
	public String getSerialNumTo() {
		return serialNumTo;
	}
	public void setSerialNumTo(String serialNumTo) {
		this.serialNumTo = serialNumTo;
	}
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	
	
}
