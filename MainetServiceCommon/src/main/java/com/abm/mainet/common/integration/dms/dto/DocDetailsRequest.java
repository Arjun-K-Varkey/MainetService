package com.abm.mainet.common.integration.dms.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DocDetailsRequest {

    @XmlElement(name = "DocType", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String docType;

    @XmlElement(name = "DigiLockerId", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String digiLockerId;

    @XmlElement(name = "UID", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String uid;
    
    @XmlElement(name = "FullName", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String fullName;

    @XmlElement(name = "DOB", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String dob;

    @XmlElement(name = "GENDER",namespace="https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String gender;
    
    // Service Name RBC(Birth Certificate)  RDC(Death Certificate)  SAC(Self Assessment Certificate)  MRG(Marriage)
    @XmlElement(name = "UDF1", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String udf1;
    
    //phone number of user 
    @XmlElement(name = "UDF2", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String udf2;
    
    //Phone number of owner
    @XmlElement(name = "UDF3", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String udf3;
    
    

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDigiLockerId() {
		return digiLockerId;
	}

	public void setDigiLockerId(String digiLockerId) {
		this.digiLockerId = digiLockerId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getUdf1() {
		return udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public String getUdf2() {
		return udf2;
	}

	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	public String getUdf3() {
		return udf3;
	}

	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	
}

