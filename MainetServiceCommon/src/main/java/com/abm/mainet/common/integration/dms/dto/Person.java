package com.abm.mainet.common.integration.dms.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
	
	@XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "dob")
    private String dob;

    @XmlAttribute(name = "gender")
    private String gender;

    @XmlAttribute(name = "phone")
    private String phone;
    
    @XmlElement(name = "Photo", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private Photo photo;
    
    @XmlAttribute(name = "fatherName")
    private String pdFatherName;
    
    @XmlAttribute(name = "motherName")
    private String pdMotherName;
    
    @XmlAttribute(name = "parentAddress")
    private String pdAddress;
    
    @XmlAttribute(name = "dod")
    private String dod;
    
    @XmlAttribute(name = "deceasedRelativeName")
    private String deceasedRelativeName;
    
    @XmlAttribute(name = "deathPlace")
    private String deathPlace;
    
    @XmlAttribute(name = "applicantName")
    private String applicantName;
    
    @XmlAttribute(name = "applicationDate")
    private String applicationDate;
    
    @XmlAttribute(name = "applicationNo")
    private String applicationNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public String getPdFatherName() {
		return pdFatherName;
	}

	public void setPdFatherName(String pdFatherName) {
		this.pdFatherName = pdFatherName;
	}

	public String getPdMotherName() {
		return pdMotherName;
	}

	public void setPdMotherName(String pdMotherName) {
		this.pdMotherName = pdMotherName;
	}

	public String getPdAddress() {
		return pdAddress;
	}

	public void setPdAddress(String pdAddress) {
		this.pdAddress = pdAddress;
	}

	public String getDod() {
		return dod;
	}

	public void setDod(String dod) {
		this.dod = dod;
	}

	public String getDeceasedRelativeName() {
		return deceasedRelativeName;
	}

	public void setDeceasedRelativeName(String deceasedRelativeName) {
		this.deceasedRelativeName = deceasedRelativeName;
	}

	public String getDeathPlace() {
		return deathPlace;
	}

	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	
	

    

}
