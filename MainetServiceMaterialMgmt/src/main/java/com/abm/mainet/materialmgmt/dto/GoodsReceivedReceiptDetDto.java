package com.abm.mainet.materialmgmt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsReceivedReceiptDetDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long vendorId;  
	private String vendorName;
	private Long itemId;
	private Long itemGroup;
	private String uom;
	private String locationName;
	private Long receQuantity;
	private Long unPrice;
	private Long totalValue;
	private String itemName;
	private Long orgid;
	private Long user_id;
	private Long langId;
	private Date lmoddate;
	private String lgIpMac;
	private String itemGroupName;
	
	
	
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getItemGroup() {
		return itemGroup;
	}
	public void setItemGroup(Long itemGroup) {
		this.itemGroup = itemGroup;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public Long getReceQuantity() {
		return receQuantity;
	}
	public void setReceQuantity(Long receQuantity) {
		this.receQuantity = receQuantity;
	}
	public Long getUnPrice() {
		return unPrice;
	}
	public void setUnPrice(Long unPrice) {
		this.unPrice = unPrice;
	}
	public Long getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(Long totalValue) {
		this.totalValue = totalValue;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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
	public String getItemGroupName() {
		return itemGroupName;
	}
	public void setItemGroupName(String itemGroupName) {
		this.itemGroupName = itemGroupName;
	}
	 
	
	
	
}
