package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DeathCertificateEntityForMobile {
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "DETAH_PERSON_NAME")
	private String drDeceasedName;
	
	@Column(name = "SEX")
	private String sex;
	
	@Column(name = "DOD")
	private String dod;
	
	@Column(name = "DEATH_PLACE")
	private String deathPlace;
	
	@Column(name = "DR_CERT_NO")
	private String drCertificateNo;
	
	@Column(name = "MOTHER_NAME_ENG")
	private String motherNameEng;
	
	@Column(name = "MOTHER_NAME_MAR")
	private String motherNameMar;
	
	@Column(name = "FATHER_HUSBAND_NAME_ENG")
	private String fatherHusbandNameEng;
	
	@Column(name = "FATHER_HUSBAND_NAME_MAR")
	private String fatherHusbandNameMar;
	
	@Column(name = "ADD_AT_TIME_OF_DEATH_ENG")
	private String addAtTimeOfDeathEng;
	
	@Column(name = "ADD_AT_TIME_OF_DEATH_MAR")
	private String addAtTimeOfDeathMar;
	
	@Column(name = "PER_ADDRESS_ENG")
	private String peraddressEng;
	
	@Column(name = "PER_ADDRESS_MAR")
	private String peraddressMar;
	
	@Column(name = "REG_NO")
	private String regNo;

	@Column(name = "REG_DATE")
	private String regDate;
	
	@Column(name = "DATE_OF_ISSUE")
	private String dateOfIssue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDrDeceasedName() {
		return drDeceasedName;
	}

	public void setDrDeceasedName(String drDeceasedName) {
		this.drDeceasedName = drDeceasedName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDod() {
		return dod;
	}

	public void setDod(String dod) {
		this.dod = dod;
	}

	public String getDeathPlace() {
		return deathPlace;
	}

	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;
	}

	public String getDrCertificateNo() {
		return drCertificateNo;
	}

	public void setDrCertificateNo(String drCertificateNo) {
		this.drCertificateNo = drCertificateNo;
	}

	public String getMotherNameEng() {
		return motherNameEng;
	}

	public void setMotherNameEng(String motherNameEng) {
		this.motherNameEng = motherNameEng;
	}

	public String getMotherNameMar() {
		return motherNameMar;
	}

	public void setMotherNameMar(String motherNameMar) {
		this.motherNameMar = motherNameMar;
	}

	public String getFatherHusbandNameEng() {
		return fatherHusbandNameEng;
	}

	public void setFatherHusbandNameEng(String fatherHusbandNameEng) {
		this.fatherHusbandNameEng = fatherHusbandNameEng;
	}

	public String getFatherHusbandNameMar() {
		return fatherHusbandNameMar;
	}

	public void setFatherHusbandNameMar(String fatherHusbandNameMar) {
		this.fatherHusbandNameMar = fatherHusbandNameMar;
	}

	public String getAddAtTimeOfDeathEng() {
		return addAtTimeOfDeathEng;
	}

	public void setAddAtTimeOfDeathEng(String addAtTimeOfDeathEng) {
		this.addAtTimeOfDeathEng = addAtTimeOfDeathEng;
	}

	public String getAddAtTimeOfDeathMar() {
		return addAtTimeOfDeathMar;
	}

	public void setAddAtTimeOfDeathMar(String addAtTimeOfDeathMar) {
		this.addAtTimeOfDeathMar = addAtTimeOfDeathMar;
	}

	public String getPeraddressEng() {
		return peraddressEng;
	}

	public void setPeraddressEng(String peraddressEng) {
		this.peraddressEng = peraddressEng;
	}

	public String getPeraddressMar() {
		return peraddressMar;
	}

	public void setPeraddressMar(String peraddressMar) {
		this.peraddressMar = peraddressMar;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getDateOfIssue() {
		return dateOfIssue;
	}

	public void setDateOfIssue(String dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	

}
