package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PropertyStatisticsServiceDepartmentEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name="department_desc_new")
	private String departmentDescNew;
	
	@Column(name="newServiceName")
	private String newServiceName;
	
	@Column(name="department_desc")
	private String departmentDesc;
	
	@Column(name="ServiceName")
	private String serviceName;
	
	@Column(name="Recieved")
	private Long received;
	
	@Column(name="accepted")
	private Long accepted;
	
	@Column(name="BeyondCLOSED")
	private Long beyondClosed;
	
	@Column(name="WithinCLOSED")
	private Long withinClosed;
	
	@Column(name="BeyondPending")
	private Long beyond_pending;
	
	@Column(name = "WithinPending")
    private Long withinPending;
	
	@Column(name = "UnSolvable")
    private Long unSolvable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartmentDescNew() {
		return departmentDescNew;
	}

	public void setDepartmentDescNew(String departmentDescNew) {
		this.departmentDescNew = departmentDescNew;
	}

	public String getNewServiceName() {
		return newServiceName;
	}

	public void setNewServiceName(String newServiceName) {
		this.newServiceName = newServiceName;
	}

	public String getDepartmentDesc() {
		return departmentDesc;
	}

	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getReceived() {
		return received;
	}

	public void setReceived(Long received) {
		this.received = received;
	}

	public Long getAccepted() {
		return accepted;
	}

	public void setAccepted(Long accepted) {
		this.accepted = accepted;
	}

	public Long getBeyondClosed() {
		return beyondClosed;
	}

	public void setBeyondClosed(Long beyondClosed) {
		this.beyondClosed = beyondClosed;
	}

	public Long getWithinClosed() {
		return withinClosed;
	}

	public void setWithinClosed(Long withinClosed) {
		this.withinClosed = withinClosed;
	}

	public Long getBeyond_pending() {
		return beyond_pending;
	}

	public void setBeyond_pending(Long beyond_pending) {
		this.beyond_pending = beyond_pending;
	}

	public Long getWithinPending() {
		return withinPending;
	}

	public void setWithinPending(Long withinPending) {
		this.withinPending = withinPending;
	}

	public Long getUnSolvable() {
		return unSolvable;
	}

	public void setUnSolvable(Long unSolvable) {
		this.unSolvable = unSolvable;
	}
	
	
	
	
}
