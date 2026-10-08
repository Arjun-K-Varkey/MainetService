package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Pritam.Chaphekar
 *
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "HUSBAND_NAME",
    "HUSBAND_ADDRESS",
    "HUSBAND_AADHAR",
    "WIFE_NAME",
    "WIFE_ADDRESS",
    "WIFE_AADHAR",
    "HOLDER_NAME",
    "MARRIAGE_DATE",
    "MARRIAGE_ISSUE_DATE",
    "LICENSE_NO",
    "OWNER_NAME",
    "BUSSINESS_NAME",
    "BUSSINESS_ADDRESS",
    "LICENSE_DATE",
    "LICENSE_CATEGORY",
    "LINCENSE_SUB_CATEGORY",
    "MEASURMENT_TYPE",
    "MEASURMENT_UNIT",
    "LICENSE_PERIOD",
    "BR_CHILD_NAME",
    "BR_DOB",
    "BR_FATHER_NAME",
    "BR_MOTHER_NAME",
    "BR_PARENTS_ADD",
    "BR_BIRTH_PLACE",
    "BR_REG_NO",
    "BR_CERTIFICATE_NO",
    "DR_DECEASED_NAME",
    "DR_DOD",
    "DR_FATHER_NAME_OR_HUSBAND_NAME",
    "DR_MOTHER_NAME",
    "DR_CERTIFICATE_NO",
    "DR_REG_NO",
    "DR_DEATH_PLACE",
    "DR_DECEASED_ADDRESS",
    "STATUS"
})
public class MarriageBlockChainCertificateResponceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("HUSBAND_NAME")
    private String hubandName;
    
    @JsonProperty("HUSBAND_ADDRESS")
    private String husbandAddress;
    
    @JsonProperty("HUSBAND_AADHAR")
    private String husbandAadhar;

    @JsonProperty("WIFE_NAME")
    private String wifeName;
    
    @JsonProperty("WIFE_ADDRESS")
    private String wifeAddress;

    @JsonProperty("HOLDER_NAME")
    private String holderName;

    @JsonProperty("WIFE_AADHAR")
    private String wifeAadhar;

    @JsonProperty("MARRIAGE_DATE")
    private String marriageDate;

    @JsonProperty("MARRIAGE_ISSUE_DATE")
    private String marriageRegistrationDate;


    @JsonProperty("LICENSE_NO")
    private String licenseNo;

    @JsonProperty("OWNER_NAME")
    private String licOwnerName;

    @JsonProperty("BUSSINESS_NAME")
    private String licBussinessName;

    @JsonProperty("BUSSINESS_ADDRESS")
    private String licBussinessAddress;

    @JsonProperty("LICENSE_DATE")
    private String licenseDate;

    @JsonProperty("LICENSE_CATEGORY")
    private String licenseCategory;

    @JsonProperty("LINCENSE_SUB_CATEGORY")
    private String licenseSubCategory;

    @JsonProperty("MEASURMENT_TYPE")
    private String licenseMeasurementType;

    @JsonProperty("MEASURMENT_UNIT")
    private String licenseMeasurmentUnit;

    @JsonProperty("LICENSE_PERIOD")
    private String licensePeriod;

    @JsonProperty("BR_CHILD_NAME")
    private String brChildName;

    @JsonProperty("BR_DOB")
    private String brDob;

    @JsonProperty("BR_FATHER_NAME")
    private String brFatherNameEng;

    @JsonProperty("BR_MOTHER_NAME")
    private String brMotherNameEng;

    @JsonProperty("BR_PARENTS_ADD")
    private String brParentsPermanantAdd;

    @JsonProperty("BR_BIRTH_PLACE")
    private String brBirthPlace;

    @JsonProperty("BR_REG_NO")
    private String brRegNo;

    @JsonProperty("BR_CERTIFICATE_NO")
    private String brCertificateNo;

    @JsonProperty("DR_DECEASED_NAME")
    private String drDeceasedName;

    @JsonProperty("DR_DOD")
    private String drDod;

    @JsonProperty("DR_FATHER_NAME_OR_HUSBAND_NAME")
    private String drFatherNameOrHusbandName;

    @JsonProperty("DR_MOTHER_NAME")
    private String drMotherName;

    @JsonProperty("DR_CERTIFICATE_NO")
    private String drCertificateNo;

    @JsonProperty("DR_REG_NO")
    private String drRegNo;

    @JsonProperty("DR_DEATH_PLACE")
    private String drDeathPlace;

    @JsonProperty("DR_DECEASED_ADDRESS")
    private String drDeceasedAddress;
    
    @JsonProperty("STATUS")
    private String status;

	public String getHubandName() {
		return hubandName;
	}

	public void setHubandName(String hubandName) {
		this.hubandName = hubandName;
	}

	public String getWifeName() {
		return wifeName;
	}

	public void setWifeName(String wifeName) {
		this.wifeName = wifeName;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getHusbandAadhar() {
		return husbandAadhar;
	}

	public void setHusbandAadhar(String husbandAadhar) {
		this.husbandAadhar = husbandAadhar;
	}

	public String getWifeAadhar() {
		return wifeAadhar;
	}

	public void setWifeAadhar(String wifeAadhar) {
		this.wifeAadhar = wifeAadhar;
	}

	public String getHusbandAddress() {
		return husbandAddress;
	}

	public void setHusbandAddress(String husbandAddress) {
		this.husbandAddress = husbandAddress;
	}

	public String getWifeAddress() {
		return wifeAddress;
	}

	public void setWifeAddress(String wifeAddress) {
		this.wifeAddress = wifeAddress;
	}

	public String getMarriageRegistrationDate() {
		return marriageRegistrationDate;
	}

	public void setMarriageRegistrationDate(String marriageRegistrationDate) {
		this.marriageRegistrationDate = marriageRegistrationDate;
	}

	public String getMarriageDate() {
		return marriageDate;
	}

	public void setMarriageDate(String marriageDate) {
		this.marriageDate = marriageDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicOwnerName() {
		return licOwnerName;
	}

	public void setLicOwnerName(String licOwnerName) {
		this.licOwnerName = licOwnerName;
	}

	public String getLicBussinessName() {
		return licBussinessName;
	}

	public void setLicBussinessName(String licBussinessName) {
		this.licBussinessName = licBussinessName;
	}

	public String getLicBussinessAddress() {
		return licBussinessAddress;
	}

	public void setLicBussinessAddress(String licBussinessAddress) {
		this.licBussinessAddress = licBussinessAddress;
	}

	public String getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}

	public String getLicenseCategory() {
		return licenseCategory;
	}

	public void setLicenseCategory(String licenseCategory) {
		this.licenseCategory = licenseCategory;
	}

	public String getLicenseSubCategory() {
		return licenseSubCategory;
	}

	public void setLicenseSubCategory(String licenseSubCategory) {
		this.licenseSubCategory = licenseSubCategory;
	}

	public String getLicenseMeasurementType() {
		return licenseMeasurementType;
	}

	public void setLicenseMeasurementType(String licenseMeasurementType) {
		this.licenseMeasurementType = licenseMeasurementType;
	}

	public String getLicenseMeasurmentUnit() {
		return licenseMeasurmentUnit;
	}

	public void setLicenseMeasurmentUnit(String licenseMeasurmentUnit) {
		this.licenseMeasurmentUnit = licenseMeasurmentUnit;
	}

	public String getLicensePeriod() {
		return licensePeriod;
	}

	public void setLicensePeriod(String licensePeriod) {
		this.licensePeriod = licensePeriod;
	}

	public String getBrChildName() {
		return brChildName;
	}

	public void setBrChildName(String brChildName) {
		this.brChildName = brChildName;
	}

	public String getBrDob() {
		return brDob;
	}

	public void setBrDob(String brDob) {
		this.brDob = brDob;
	}

	public String getBrFatherNameEng() {
		return brFatherNameEng;
	}

	public void setBrFatherNameEng(String brFatherNameEng) {
		this.brFatherNameEng = brFatherNameEng;
	}

	public String getBrMotherNameEng() {
		return brMotherNameEng;
	}

	public void setBrMotherNameEng(String brMotherNameEng) {
		this.brMotherNameEng = brMotherNameEng;
	}

	public String getBrParentsPermanantAdd() {
		return brParentsPermanantAdd;
	}

	public void setBrParentsPermanantAdd(String brParentsPermanantAdd) {
		this.brParentsPermanantAdd = brParentsPermanantAdd;
	}

	public String getBrBirthPlace() {
		return brBirthPlace;
	}

	public void setBrBirthPlace(String brBirthPlace) {
		this.brBirthPlace = brBirthPlace;
	}

	public String getBrRegNo() {
		return brRegNo;
	}

	public void setBrRegNo(String brRegNo) {
		this.brRegNo = brRegNo;
	}

	public String getBrCertificateNo() {
		return brCertificateNo;
	}

	public void setBrCertificateNo(String brCertificateNo) {
		this.brCertificateNo = brCertificateNo;
	}

	public String getDrDeceasedName() {
		return drDeceasedName;
	}

	public void setDrDeceasedName(String drDeceasedName) {
		this.drDeceasedName = drDeceasedName;
	}

	public String getDrDod() {
		return drDod;
	}

	public void setDrDod(String drDod) {
		this.drDod = drDod;
	}

	public String getDrFatherNameOrHusbandName() {
		return drFatherNameOrHusbandName;
	}

	public void setDrFatherNameOrHusbandName(String drFatherNameOrHusbandName) {
		this.drFatherNameOrHusbandName = drFatherNameOrHusbandName;
	}

	public String getDrMotherName() {
		return drMotherName;
	}

	public void setDrMotherName(String drMotherName) {
		this.drMotherName = drMotherName;
	}

	public String getDrCertificateNo() {
		return drCertificateNo;
	}

	public void setDrCertificateNo(String drCertificateNo) {
		this.drCertificateNo = drCertificateNo;
	}

	public String getDrRegNo() {
		return drRegNo;
	}

	public void setDrRegNo(String drRegNo) {
		this.drRegNo = drRegNo;
	}

	public String getDrDeathPlace() {
		return drDeathPlace;
	}

	public void setDrDeathPlace(String drDeathPlace) {
		this.drDeathPlace = drDeathPlace;
	}

	public String getDrDeceasedAddress() {
		return drDeceasedAddress;
	}

	public void setDrDeceasedAddress(String drDeceasedAddress) {
		this.drDeceasedAddress = drDeceasedAddress;
	}

    // Getters and Setters
    
    
}
	

    
    
	
    
    

