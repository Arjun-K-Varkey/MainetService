package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WholePropertyDataEntity {

	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name="MN_PROP_NO")
	private String mnPropNo;
	
	@Column(name="TOTAL_DEMAND")
	private String toatalDemand;
	
	@Column(name="ARREARS")
	private String arrears;
	
	@Column(name="TOTAL_OUTSTANDING")
	private String totalOutstanding;
	
	@Column(name="MN_ASS_NO")
	private String mnAssNo;
	
	@Column(name="MN_FLAT_NO")
	private String mnFlatNo;
	
	@Column(name="MN_VILLAGE_MAUJA")
	private String mnVillageMuja;
	
	@Column(name="WARD")
	private String ward;
	
	@Column(name="ZONE")
	private String zone;
	
	@Column(name="SERVICE_STATUS")
	private String serviceStatus;
	
	@Column(name="MN_SURVEY_NUMBER")
	private String mnSurveyNumber;
	
	@Column(name="MN_ASSD_FLOOR_NO")
	private String mnAssdFloorNo;
	
	@Column(name="MN_ASSD_BUILDUP_AREA")
	private String mnAssdBuilupArea;
	
	@Column(name="MN_ASSD_RV")
	private String mnAssdRv;
	
	@Column(name="PROP_TYPE")
	private String propType;
	
	@Column(name="MN_ASSD_USAGETYPE1")
	private String mnAssdUsageType1;
	
	@Column(name="MN_ASSD_USAGETYPE2")
	private String mnAssdUsageType2;
	
	@Column(name="MN_LEGAL")
	private String mnLegal;
	
	@Column(name="MN_ASS_OWNER_TYPE")
	private String mnAssOwnerType;
	
	@Column(name="MN_PROP_LVL_ROAD_TYPE")
	private String mnPropLvlRoadType;
	
	@Column(name="MN_ASS_ACQ_DATE")
	private String mnAssAcqDate;
	
	@Column(name="MN_LSTASSNTDT")
	private String mnLstAssntDt;
	
	@Column(name="MN_AREA_NAME")
	private String mnAreaName;
	
	@Column(name="MN_ASS_ADDRESS")
	private String mnAssAddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMnPropNo() {
		return mnPropNo;
	}

	public void setMnPropNo(String mnPropNo) {
		this.mnPropNo = mnPropNo;
	}

	public String getToatalDemand() {
		return toatalDemand;
	}

	public void setToatalDemand(String toatalDemand) {
		this.toatalDemand = toatalDemand;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getTotalOutstanding() {
		return totalOutstanding;
	}

	public void setTotalOutstanding(String totalOutstanding) {
		this.totalOutstanding = totalOutstanding;
	}

	public String getMnAssNo() {
		return mnAssNo;
	}

	public void setMnAssNo(String mnAssNo) {
		this.mnAssNo = mnAssNo;
	}

	public String getMnFlatNo() {
		return mnFlatNo;
	}

	public void setMnFlatNo(String mnFlatNo) {
		this.mnFlatNo = mnFlatNo;
	}

	public String getMnVillageMuja() {
		return mnVillageMuja;
	}

	public void setMnVillageMuja(String mnVillageMuja) {
		this.mnVillageMuja = mnVillageMuja;
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

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getMnSurveyNumber() {
		return mnSurveyNumber;
	}

	public void setMnSurveyNumber(String mnSurveyNumber) {
		this.mnSurveyNumber = mnSurveyNumber;
	}

	public String getMnAssdFloorNo() {
		return mnAssdFloorNo;
	}

	public void setMnAssdFloorNo(String mnAssdFloorNo) {
		this.mnAssdFloorNo = mnAssdFloorNo;
	}

	public String getMnAssdBuilupArea() {
		return mnAssdBuilupArea;
	}

	public void setMnAssdBuilupArea(String mnAssdBuilupArea) {
		this.mnAssdBuilupArea = mnAssdBuilupArea;
	}

	public String getMnAssdRv() {
		return mnAssdRv;
	}

	public void setMnAssdRv(String mnAssdRv) {
		this.mnAssdRv = mnAssdRv;
	}

	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	public String getMnAssdUsageType1() {
		return mnAssdUsageType1;
	}

	public void setMnAssdUsageType1(String mnAssdUsageType1) {
		this.mnAssdUsageType1 = mnAssdUsageType1;
	}

	public String getMnAssdUsageType2() {
		return mnAssdUsageType2;
	}

	public void setMnAssdUsageType2(String mnAssdUsageType2) {
		this.mnAssdUsageType2 = mnAssdUsageType2;
	}

	public String getMnLegal() {
		return mnLegal;
	}

	public void setMnLegal(String mnLegal) {
		this.mnLegal = mnLegal;
	}

	public String getMnAssOwnerType() {
		return mnAssOwnerType;
	}

	public void setMnAssOwnerType(String mnAssOwnerType) {
		this.mnAssOwnerType = mnAssOwnerType;
	}

	public String getMnPropLvlRoadType() {
		return mnPropLvlRoadType;
	}

	public void setMnPropLvlRoadType(String mnPropLvlRoadType) {
		this.mnPropLvlRoadType = mnPropLvlRoadType;
	}

	public String getMnAssAcqDate() {
		return mnAssAcqDate;
	}

	public void setMnAssAcqDate(String mnAssAcqDate) {
		this.mnAssAcqDate = mnAssAcqDate;
	}

	public String getMnLstAssntDt() {
		return mnLstAssntDt;
	}

	public void setMnLstAssntDt(String mnLstAssntDt) {
		this.mnLstAssntDt = mnLstAssntDt;
	}

	public String getMnAreaName() {
		return mnAreaName;
	}

	public void setMnAreaName(String mnAreaName) {
		this.mnAreaName = mnAreaName;
	}

	public String getMnAssAddress() {
		return mnAssAddress;
	}

	public void setMnAssAddress(String mnAssAddress) {
		this.mnAssAddress = mnAssAddress;
	}
	
}
