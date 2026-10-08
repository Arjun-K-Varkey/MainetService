package com.abm.mainet.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table to fetch legacy bill data from
 * @author akshata.bhat
 * @since 19-09-2022
 */
@Entity
@Table(name = "tb_wt_bill_det_legacy")
public class TbWtBillDetLegacy implements Serializable{

	private static final long serialVersionUID = 4039993095685442029L;

	@Id
    @Column(name = "BD_BILLDETID")
    private long bdBilldetid;

	@Column(name = "bm_idno")
    private Long bmIdNo;

    @Column(name = "MRD_ID")
    private Long mrdId;
    
    @Column(name = "CS_IDN")
    private Long csIdn;

    @Column(name = "TAX_ID")
    private Long taxId;
    
    @Column(name = "REBATE_ID")
    private Long rebateId;

    @Column(name = "ADJUSTMENT_ID")
    private Long adjustmentId;

    @Column(name = "BD_CUR_TAXAMT")
    private Double bdCurTaxamt;

    @Column(name = "BD_CUR_BAL_TAXAMT")
    private Double bdCurBalTaxamt;

    @Column(name = "BD_PRV_BAL_ARRAMT")
    private Double bdPrvBalArramt;

    @Column(name = "BD_PRV_ARRAMT")
    private Double bdPrvArramt;
    
    @Column(name = "BD_BILLFLAG")
    private String bdBillflag;

    @Column(name = "RULE_ID")
    private String ruleId;
    
    @Column(name ="BASE_RATE")
    private Double baseRate;
    
    @Column(name = "WBD_FLAT")
    private String wdbFlat;
    
    @Column(name= "WBD_APPROVED")
    private String wdbApproved;
    
    @Column(name = "WBD_STATUS")
    private String wdbStatus;
    
    @Column(name = "CONSUMPTION")
    private Double consumption;
    
    @Column(name = "DEM_ADJ")
    private Double demAdj;
    
    @Column(name = "WT_D1")
    private Date wtD1;

    @Column(name = "WT_LO1")
    private Double wtLo1;
    
    @Column(name = "orgid")
    private Long orgId;

    @Column(name = "CREATED_BY")
    private Long userId;

    @Column(name = "CREATED_DATE")
    private Date lmoddate;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "LG_IP_MAC")
    private String lgIpMac;

    @Column(name = "LG_IP_MAC_UPD")
    private String lgIpMacUpd;

    @Column(name = "TAX_CATEGORY")
    private Long taxCategory;

    @Column(name = "COLL_SEQ")
    private Long collSeq;

	public long getBdBilldetid() {
		return bdBilldetid;
	}

	public void setBdBilldetid(long bdBilldetid) {
		this.bdBilldetid = bdBilldetid;
	}

	public Long getBmIdNo() {
		return bmIdNo;
	}

	public void setBmIdNo(Long bmIdNo) {
		this.bmIdNo = bmIdNo;
	}

	public Long getMrdId() {
		return mrdId;
	}

	public void setMrdId(Long mrdId) {
		this.mrdId = mrdId;
	}

	public Long getCsIdn() {
		return csIdn;
	}

	public void setCsIdn(Long csIdn) {
		this.csIdn = csIdn;
	}

	public Long getTaxId() {
		return taxId;
	}

	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}

	public Long getRebateId() {
		return rebateId;
	}

	public void setRebateId(Long rebateId) {
		this.rebateId = rebateId;
	}

	public Long getAdjustmentId() {
		return adjustmentId;
	}

	public void setAdjustmentId(Long adjustmentId) {
		this.adjustmentId = adjustmentId;
	}

	public Double getBdCurTaxamt() {
		return bdCurTaxamt;
	}

	public void setBdCurTaxamt(Double bdCurTaxamt) {
		this.bdCurTaxamt = bdCurTaxamt;
	}

	public Double getBdCurBalTaxamt() {
		return bdCurBalTaxamt;
	}

	public void setBdCurBalTaxamt(Double bdCurBalTaxamt) {
		this.bdCurBalTaxamt = bdCurBalTaxamt;
	}

	public Double getBdPrvBalArramt() {
		return bdPrvBalArramt;
	}

	public void setBdPrvBalArramt(Double bdPrvBalArramt) {
		this.bdPrvBalArramt = bdPrvBalArramt;
	}

	public Double getBdPrvArramt() {
		return bdPrvArramt;
	}

	public void setBdPrvArramt(Double bdPrvArramt) {
		this.bdPrvArramt = bdPrvArramt;
	}

	public String getBdBillflag() {
		return bdBillflag;
	}

	public void setBdBillflag(String bdBillflag) {
		this.bdBillflag = bdBillflag;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Double getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(Double baseRate) {
		this.baseRate = baseRate;
	}

	public String getWdbFlat() {
		return wdbFlat;
	}

	public void setWdbFlat(String wdbFlat) {
		this.wdbFlat = wdbFlat;
	}

	public String getWdbApproved() {
		return wdbApproved;
	}

	public void setWdbApproved(String wdbApproved) {
		this.wdbApproved = wdbApproved;
	}

	public String getWdbStatus() {
		return wdbStatus;
	}

	public void setWdbStatus(String wdbStatus) {
		this.wdbStatus = wdbStatus;
	}

	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	public Double getDemAdj() {
		return demAdj;
	}

	public void setDemAdj(Double demAdj) {
		this.demAdj = demAdj;
	}

	public Date getWtD1() {
		return wtD1;
	}

	public void setWtD1(Date wtD1) {
		this.wtD1 = wtD1;
	}

	public Double getWtLo1() {
		return wtLo1;
	}

	public void setWtLo1(Double wtLo1) {
		this.wtLo1 = wtLo1;
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

	public Date getLmoddate() {
		return lmoddate;
	}

	public void setLmoddate(Date lmoddate) {
		this.lmoddate = lmoddate;
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

	public Long getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(Long taxCategory) {
		this.taxCategory = taxCategory;
	}

	public Long getCollSeq() {
		return collSeq;
	}

	public void setCollSeq(Long collSeq) {
		this.collSeq = collSeq;
	}
}
