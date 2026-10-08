package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SlaWiseGrievanceSumaaryEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "department_descrption")
    private String departmentDescrption;
	
	@Column(name = "COMPLAINT_SUB_TYPE")
    private String complaintSubType;
	
	@Column(name = "BeyoundCLOSED")
    private Double beyoundCLOSED;
    
    @Column(name = "WithinCLOSED")
    private Double withinCLOSED;
	
	@Column(name = "BeyoundPending")
    private Double beyoundPending;
	
	@Column(name = "WithinPending")
    private Double withinPending;
	
	@Column(name = "TotalRecieved")
    private Double totalRecieved;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartmentDescrption() {
		return departmentDescrption;
	}

	public void setDepartmentDescrption(String departmentDescrption) {
		this.departmentDescrption = departmentDescrption;
	}

	public String getComplaintSubType() {
		return complaintSubType;
	}

	public void setComplaintSubType(String complaintSubType) {
		this.complaintSubType = complaintSubType;
	}

	public Double getBeyoundCLOSED() {
		return beyoundCLOSED;
	}

	public void setBeyoundCLOSED(Double beyoundCLOSED) {
		this.beyoundCLOSED = beyoundCLOSED;
	}

	public Double getWithinCLOSED() {
		return withinCLOSED;
	}

	public void setWithinCLOSED(Double withinCLOSED) {
		this.withinCLOSED = withinCLOSED;
	}

	public Double getBeyoundPending() {
		return beyoundPending;
	}

	public void setBeyoundPending(Double beyoundPending) {
		this.beyoundPending = beyoundPending;
	}

	public Double getWithinPending() {
		return withinPending;
	}

	public void setWithinPending(Double withinPending) {
		this.withinPending = withinPending;
	}

	public Double getTotalRecieved() {
		return totalRecieved;
	}

	public void setTotalRecieved(Double totalRecieved) {
		this.totalRecieved = totalRecieved;
	}

	

	
	
	
	
}
