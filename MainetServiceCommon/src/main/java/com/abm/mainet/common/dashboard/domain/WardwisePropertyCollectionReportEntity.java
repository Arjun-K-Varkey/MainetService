package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WardwisePropertyCollectionReportEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "WARD")
    private String ward;
	
	@Column(name = "ARREARS_COLLECTION")
    private Double arrears_collection;
	
	@Column(name = "CURRENT_COLLECTION")
    private Double current_collection;
	
	

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

	public Double getArrears_collection() {
		return arrears_collection;
	}

	public void setArrears_collection(Double arrears_collection) {
		this.arrears_collection = arrears_collection;
	}

	public Double getCurrent_collection() {
		return current_collection;
	}

	public void setCurrent_collection(Double current_collection) {
		this.current_collection = current_collection;
	}


	

	
	
	
	
}
