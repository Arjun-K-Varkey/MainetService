package com.abm.mainet.common.dashboard.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WaterDataWardZoneWiseEntity {
	
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "CS_IDN")
	private String cs_idn;

	@Column(name = "CS_CCN")
	private String CS_CCn;

	@Column(name = "CS_NAME")
	private String CS_NAME;

	@Column(name = "WARD")
	private String ward;

	@Column(name = "ZONE")
	private String zone;

	@Column(name = "METER_STATUS")
	private String meter_status;

	@Column(name = "PC_FLG")
	private String PC_FLG;

	@Column(name = "CS_CCNSTATUS")
	private String CS_CCNSTATUS;

	@Column(name = "CS_CCNSIZE")
	private String CS_CCNSIZE;
	
	@Column(name = "CONNECTION_TYPE")
	private String connection_type;

	@Column(name = "TOTAL_DEMAND")
	private String Total_Demand;

	@Column(name = "ARREAR")
	private String Arrear;

	@Column(name = "OUTSTANDING")
	private String outstanding;

	public String getCs_idn() {
		return cs_idn;
	}

	public void setCs_idn(String cs_idn) {
		this.cs_idn = cs_idn;
	}

	public String getCS_CCn() {
		return CS_CCn;
	}

	public void setCS_CCn(String cS_CCn) {
		CS_CCn = cS_CCn;
	}

	public String getCS_NAME() {
		return CS_NAME;
	}

	public void setCS_NAME(String cS_NAME) {
		CS_NAME = cS_NAME;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getMeter_status() {
		return meter_status;
	}

	public void setMeter_status(String meter_status) {
		this.meter_status = meter_status;
	}

	public String getPC_FLG() {
		return PC_FLG;
	}

	public void setPC_FLG(String pC_FLG) {
		PC_FLG = pC_FLG;
	}

	public String getCS_CCNSTATUS() {
		return CS_CCNSTATUS;
	}

	public void setCS_CCNSTATUS(String cS_CCNSTATUS) {
		CS_CCNSTATUS = cS_CCNSTATUS;
	}

	public String getCS_CCNSIZE() {
		return CS_CCNSIZE;
	}

	public void setCS_CCNSIZE(String cS_CCNSIZE) {
		CS_CCNSIZE = cS_CCNSIZE;
	}

	public String getConnection_type() {
		return connection_type;
	}

	public void setConnection_type(String connection_type) {
		this.connection_type = connection_type;
	}

	public String getTotal_Demand() {
		return Total_Demand;
	}

	public void setTotal_Demand(String total_Demand) {
		Total_Demand = total_Demand;
	}

	public String getArrear() {
		return Arrear;
	}

	public void setArrear(String arrear) {
		Arrear = arrear;
	}

	public String getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(String outstanding) {
		this.outstanding = outstanding;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
