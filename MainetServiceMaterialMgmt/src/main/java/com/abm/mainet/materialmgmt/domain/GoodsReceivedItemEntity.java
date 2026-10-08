package com.abm.mainet.materialmgmt.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "MM_Goods_Item")
public class GoodsReceivedItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "goodsId")
	private Long goodsId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indentid", nullable = false)
    private GoodsReceivedReceiptEntity indentid;
	
	@Column(name = "itemId")
	private Long itemId;

	@Column(name = "locationName")
	private String locationName;

	@Column(name = "itemGroup")
	private Long itemGroup;

	@Column(name = "uom")
	private String uom;
	
	@Column(name = "receQuantity")
	private Long receQuantity;
	
	@Column(name = "unPrice")
	private Long unPrice;
	
	@Column(name = "totalValue")
	private Long totalValue;

	@Column(name = "ORG_ID")
	private Long orgId;
	
	@Column(name = "USER_ID")
	private Long user_id;
	
	@Column(name = "LANGID")
	private Long langId;
	
	@Column(name = "LG_IP_MAC")
	private String lgIpMac;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LMODDATE")
	private Date lmoddate;
	
		
	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}


	public GoodsReceivedReceiptEntity getIndentid() {
		return indentid;
	}


	public void setIndentid(GoodsReceivedReceiptEntity indentid) {
		this.indentid = indentid;
	}


	public Long getItemId() {
		return itemId;
	}


	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}


	public Long getItemGroup() {
		return itemGroup;
	}






	public void setItemGroup(Long itemGroup) {
		this.itemGroup = itemGroup;
	}


	public String getUom() {
		return uom;
	}


	public void setUom(String uom) {
		this.uom = uom;
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

	


	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getLgIpMac() {
		return lgIpMac;
	}

	public void setLgIpMac(String lgIpMac) {
		this.lgIpMac = lgIpMac;
	}
	
	

	public Date getLmoddate() {
		return lmoddate;
	}

	public void setLmoddate(Date lmoddate) {
		this.lmoddate = lmoddate;
	}

	public String[] getPkValues() {
        return new String[] { "ITP", "MM_Goods_Item", "goodsId" };
    }
}