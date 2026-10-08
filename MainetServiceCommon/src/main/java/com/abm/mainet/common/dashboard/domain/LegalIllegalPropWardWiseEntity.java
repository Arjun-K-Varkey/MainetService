package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LegalIllegalPropWardWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
	
	@Column(name = "TOTAL_PROPERTIES")
    private Double totalProperties;
	
	@Column(name = "LEGAL_PROPERTIES")
    private Double legalProperties;
	
	@Column(name = "ILLEGAL_PROPERTIES")
    private Double illegalProperties;
	
	@Column(name = "LEGAL_RESIDENTIAL")
    private Double legalResidential;
	
	@Column(name = "LEGAL_NON_RESIDENTIAL")
    private Double legalNonResidential;
	
	@Column(name = "ILLEGAL_RESIDENTIAL")
    private Double illegalResidential;
	
	@Column(name = "ILLEGAL_NON_RESIDENTIAL")
    private Double illegalNonResidential;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public Double getTotalProperties() {
		return totalProperties;
	}

	public void setTotalProperties(Double totalProperties) {
		this.totalProperties = totalProperties;
	}

	public Double getLegalProperties() {
		return legalProperties;
	}

	public void setLegalProperties(Double legalProperties) {
		this.legalProperties = legalProperties;
	}

	public Double getIllegalProperties() {
		return illegalProperties;
	}

	public void setIllegalProperties(Double illegalProperties) {
		this.illegalProperties = illegalProperties;
	}

	public Double getLegalResidential() {
		return legalResidential;
	}

	public void setLegalResidential(Double legalResidential) {
		this.legalResidential = legalResidential;
	}

	public Double getLegalNonResidential() {
		return legalNonResidential;
	}

	public void setLegalNonResidential(Double legalNonResidential) {
		this.legalNonResidential = legalNonResidential;
	}

	public Double getIllegalResidential() {
		return illegalResidential;
	}

	public void setIllegalResidential(Double illegalResidential) {
		this.illegalResidential = illegalResidential;
	}

	public Double getIllegalNonResidential() {
		return illegalNonResidential;
	}

	public void setIllegalNonResidential(Double illegalNonResidential) {
		this.illegalNonResidential = illegalNonResidential;
	}

	
	
}
