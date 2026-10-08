package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ComplaintsDepartmentWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;

	@Column(name = "Complaint_Department")
	private String department;

	@Column(name = "Pending_Count")
	private int pendingSum;
	
	@Column(name = "Closed_Count")
	private int closedSum;
	
	@Column(name = "Received_Count")
	private int totalPendingClosed;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	public int getPendingSum() {
		return pendingSum;
	}

	public void setPendingSum(int pendingSum) {
		this.pendingSum = pendingSum;
	}

	public int getClosedSum() {
		return closedSum;
	}

	public void setClosedSum(int closedSum) {
		this.closedSum = closedSum;
	}

	public int getTotalPendingClosed() {
		return totalPendingClosed;
	}

	public void setTotalPendingClosed(int totalPendingClosed) {
		this.totalPendingClosed = totalPendingClosed;
	}
}
