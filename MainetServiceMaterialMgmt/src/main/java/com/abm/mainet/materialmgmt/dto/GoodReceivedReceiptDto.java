package com.abm.mainet.materialmgmt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GoodReceivedReceiptDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long indentid;
	private Long vendorId;  
	private String vendorName;
	private String remarks;
	private Date receivedReceiptDate;
	private String requisitionNo;
	private Long orgId;
	GoodsReceivedReceiptDetDto GoodsProcessItemDto = new GoodsReceivedReceiptDetDto();
	private List<GoodsReceivedReceiptDetDto> goodsReceivedReceiptDetDto = new ArrayList<>();
	private String removeFileById;
	private Long user_id;
	private String receivedDate;
	private Long langId;
	private Date lmoddate;
	private String lgIpMac;
	private String challanNo;
	private Date challanDate;
	private Long storeId;
	private String storeName;
	
	
	
	
	public Long getIndentid() {
		return indentid;
	}
	public void setIndentid(Long indentid) {
		this.indentid = indentid;
	}
	public GoodsReceivedReceiptDetDto getGoodsProcessItemDto() {
		return GoodsProcessItemDto;
	}
	public void setGoodsProcessItemDto(GoodsReceivedReceiptDetDto goodsProcessItemDto) {
		GoodsProcessItemDto = goodsProcessItemDto;
	}
	public List<GoodsReceivedReceiptDetDto> getGoodsReceivedReceiptDetDto() {
		return goodsReceivedReceiptDetDto;
	}
	public void setGoodsReceivedReceiptDetDto(List<GoodsReceivedReceiptDetDto> goodsReceivedReceiptDetDto) {
		this.goodsReceivedReceiptDetDto = goodsReceivedReceiptDetDto;
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
	
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getReceivedReceiptDate() {
		return receivedReceiptDate;
	}
	public void setReceivedReceiptDate(Date receivedReceiptDate) {
		this.receivedReceiptDate = receivedReceiptDate;
	}
	public String getRequisitionNo() {
		return requisitionNo;
	}
	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getRemoveFileById() {
		return removeFileById;
	}
	public void setRemoveFileById(String removeFileById) {
		this.removeFileById = removeFileById;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
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
	public String getChallanNo() {
		return challanNo;
	}
	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}
	public Date getChallanDate() {
		return challanDate;
	}
	public void setChallanDate(Date challanDate) {
		this.challanDate = challanDate;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	
	
	 
	
}
