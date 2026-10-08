package com.abm.mainet.materialmgmt.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import com.abm.mainet.common.workflow.domain.WorkflowDet;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MM_Goods_Receipt")
public class GoodsReceivedReceiptEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "indentid")
	private Long indentid;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "indentid", referencedColumnName = "indentid")
	@Fetch(FetchMode.SUBSELECT)
	private List<GoodsReceivedItemEntity> goodsReceivedItemEntity = new ArrayList<GoodsReceivedItemEntity>();
	
	@Column(name = "requisitionNo")
	private String requisitionNo;


	@Column(name = "vendorId")
	private Long vendorId;

	@Column(name = "vendorName")
	private String vendorName;


	@Column(name = "ORG_ID")
	private Long orgId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "receivedReceiptDate")
	private Date receivedReceiptDate;
	
	@Column(name = "remarks")
	private String remarks;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "indentid", cascade = CascadeType.ALL)
	private List<GoodsReceivedItemEntity> goodsItem = new ArrayList<>();

	@Column(name = "USER_ID")
	private Long user_id;
	
	@Column(name = "LANGID")
	private Long langId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LMODDATE")
	private Date lmoddate;
	
	@Column(name = "LG_IP_MAC")
	private String lgIpMac;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "challanDate")
	private Date challanDate;
	
	@Column(name = "challanNo")
	private String challanNo;
	
	@Column(name = "storeId")
	private Long storeId;
	
	
	public List<GoodsReceivedItemEntity> getGoodsReceivedItemEntity() {
		return goodsReceivedItemEntity;
	}




	public void setGoodsReceivedItemEntity(List<GoodsReceivedItemEntity> goodsReceivedItemEntity) {
		this.goodsReceivedItemEntity = goodsReceivedItemEntity;
	}




	public Long getIndentid() {
		return indentid;
	}




	public void setIndentid(Long indentid) {
		this.indentid = indentid;
	}




	public String getRequisitionNo() {
		return requisitionNo;
	}




	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
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




	public Long getOrgId() {
		return orgId;
	}




	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}




	public Date getReceivedReceiptDate() {
		return receivedReceiptDate;
	}




	public void setReceivedReceiptDate(Date receivedReceiptDate) {
		this.receivedReceiptDate = receivedReceiptDate;
	}




	public String getRemarks() {
		return remarks;
	}




	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}




	public List<GoodsReceivedItemEntity> getGoodsItem() {
		return goodsItem;
	}




	public void setGoodsItem(List<GoodsReceivedItemEntity> goodsItem) {
		this.goodsItem = goodsItem;
	}




	public String[] getPkValues() {
		return new String[] { "ITP", "MM_Goods_Receipt", "indentid" };
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




	public Date getChallanDate() {
		return challanDate;
	}




	public void setChallanDate(Date challanDate) {
		this.challanDate = challanDate;
	}




	public String getChallanNo() {
		return challanNo;
	}




	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}


	public Long getStoreId() {
		return storeId;
	}


	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
		
}