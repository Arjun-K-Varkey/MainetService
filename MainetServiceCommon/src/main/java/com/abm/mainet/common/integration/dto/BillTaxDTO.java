/**
 *
 */
package com.abm.mainet.common.integration.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rahul.Yadav
 *
 */
public class BillTaxDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2544340187609469844L;

    private Long taxSequence;

    private Long taxId;

    private Double tax;

    private Long ndays;

    private Double csmp;

    private Long mrdCpdIdWtp;

    private String cpdGap;

    private Date mrdFrom;

    private Date mrdTo;

    private Long taxCategory;

    private Long taxSubCategory;

    private String taxCategoryCodeValue;

    private String ruleId;

    private double baseRate;
    
    private double trRate;
    
    private double addCsmp;
    
    private String parentTaxCode;
    
    private double cyclelength;

    public Long getTaxSequence() {
        return taxSequence;
    }

    public void setTaxSequence(final Long taxSequence) {
        this.taxSequence = taxSequence;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(final Long taxId) {
        this.taxId = taxId;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(final Double tax) {
        this.tax = tax;
    }

    public Long getNdays() {
        return ndays;
    }

    public void setNdays(final Long ndays) {
        this.ndays = ndays;
    }

    public Double getCsmp() {
        return csmp;
    }

    public void setCsmp(final Double csmp) {
        this.csmp = csmp;
    }

    public Long getMrdCpdIdWtp() {
        return mrdCpdIdWtp;
    }

    public void setMrdCpdIdWtp(final Long mrdCpdIdWtp) {
        this.mrdCpdIdWtp = mrdCpdIdWtp;
    }

    public String getCpdGap() {
        return cpdGap;
    }

    public void setCpdGap(final String cpdGap) {
        this.cpdGap = cpdGap;
    }

    public Long getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(final Long taxCategory) {
        this.taxCategory = taxCategory;
    }

    public Long getTaxSubCategory() {
        return taxSubCategory;
    }

    public void setTaxSubCategory(final Long taxSubCategory) {
        this.taxSubCategory = taxSubCategory;
    }

    public String getTaxCategoryCodeValue() {
        return taxCategoryCodeValue;
    }

    public void setTaxCategoryCodeValue(final String taxCategoryCodeValue) {
        this.taxCategoryCodeValue = taxCategoryCodeValue;
    }

    public Date getMrdFrom() {
        return mrdFrom;
    }

    public void setMrdFrom(final Date mrdFrom) {
        this.mrdFrom = mrdFrom;
    }

    public Date getMrdTo() {
        return mrdTo;
    }

    public void setMrdTo(final Date mrdTo) {
        this.mrdTo = mrdTo;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

	public String getParentTaxCode() {
		return parentTaxCode;
	}

	public void setParentTaxCode(String parentTaxCode) {
		this.parentTaxCode = parentTaxCode;
	}

	public double getTrRate() {
		return trRate;
	}

	public void setTrRate(double trRate) {
		this.trRate = trRate;
	}

	public double getAddCsmp() {
		return addCsmp;
	}

	public void setAddCsmp(double addCsmp) {
		this.addCsmp = addCsmp;
	}

	public double getCyclelength() {
		return cyclelength;
	}

	public void setCyclelength(double cyclelength) {
		this.cyclelength = cyclelength;
	}

	

	

	
}
