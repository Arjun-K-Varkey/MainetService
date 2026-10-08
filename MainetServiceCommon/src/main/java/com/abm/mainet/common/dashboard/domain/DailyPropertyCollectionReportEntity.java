package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DailyPropertyCollectionReportEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "DAILY_COLLECTION")
    private String daily_collection;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDaily_collection() {
		return daily_collection;
	}

	public void setDaily_collection(String daily_collection) {
		this.daily_collection = daily_collection;
	}
	
	
	

	
	
	
	
}
