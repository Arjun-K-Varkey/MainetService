package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BirthCertificateEntityForMobile {
	@Id
	@Column(name = "num")
	private int id;
	
	@Column(name = "BR_CHILDNAME")
	private String brChildName;
	
	@Column(name = "SEX")
	private String sex;
	
	@Column(name = "DOB")
	private String dob;
	
	@Column(name = "BIRTH_PLACE")
	private String birthPlace;
	
	@Column(name = "BR_CERT_NO")
	private String certificateNo;
	
	@Column(name = "MOTHER_NAME_ENG")
	private String motherNameEng;
	
	@Column(name = "MOTHER_NAME_MAR")
	private String motherNameMar;
	
	@Column(name = "FATHER_NAME_ENG")
	private String fatherNameEng;
	
	@Column(name = "FATHER_NAME_MAR")
	private String fatherNameMar;
	
	@Column(name = "PARENT_ADD_AT_TIME_OF_BIRTH_ENG")
	private String parentaddAtTimeOfBirthEng;
	
	@Column(name = "PARENT_ADD_AT_TIME_OF_BIRTH_MAR")
	private String parentaddAtTimeOfBirthMar;
	
	@Column(name = "PARENT_PER_ADDRESS_ENG")
	private String parentPerAddEng;
	
	@Column(name = "PARENT_PER_ADDRESS_MAR")
	private String parentPerAddMar;
	
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

	public String getBrChildName() {
		return brChildName;
	}

	public void setBrChildName(String brChildName) {
		this.brChildName = brChildName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
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

	public String getFatherNameEng() {
		return fatherNameEng;
	}

	public void setFatherNameEng(String fatherNameEng) {
		this.fatherNameEng = fatherNameEng;
	}

	public String getFatherNameMar() {
		return fatherNameMar;
	}

	public void setFatherNameMar(String fatherNameMar) {
		this.fatherNameMar = fatherNameMar;
	}

	public String getParentaddAtTimeOfBirthEng() {
		return parentaddAtTimeOfBirthEng;
	}

	public void setParentaddAtTimeOfBirthEng(String parentaddAtTimeOfBirthEng) {
		this.parentaddAtTimeOfBirthEng = parentaddAtTimeOfBirthEng;
	}

	public String getParentaddAtTimeOfBirthMar() {
		return parentaddAtTimeOfBirthMar;
	}

	public void setParentaddAtTimeOfBirthMar(String parentaddAtTimeOfBirthMar) {
		this.parentaddAtTimeOfBirthMar = parentaddAtTimeOfBirthMar;
	}

	public String getParentPerAddEng() {
		return parentPerAddEng;
	}

	public void setParentPerAddEng(String parentPerAddEng) {
		this.parentPerAddEng = parentPerAddEng;
	}

	public String getParentPerAddMar() {
		return parentPerAddMar;
	}

	public void setParentPerAddMar(String parentPerAddMar) {
		this.parentPerAddMar = parentPerAddMar;
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
