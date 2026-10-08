package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ComplaintsModeWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;

	@Column(name = "Mode")
	private String mode;

	@Column(name = "Pending_Sum")
	private int pendingSum;

	@Column(name = "Closed_Sum")
	private int closedSum;

	@Column(name = "Total_Pending_Closed")
	private int totalPendingClosed;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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
