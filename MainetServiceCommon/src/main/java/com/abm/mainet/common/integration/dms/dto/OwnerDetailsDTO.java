package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class OwnerDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String owner_name;
    private String owner_address;
    private String owner_adhar_no;
    private String owner_email;
    private String owner_phone;
    private String owner_marathi_name;
    private String owner_marathi_address;
    private String owner_power_of_attorney;
    private String owner_is_applicant;
    private String owner_type;
   








	public String getOwner_name() {
		return owner_name;
	}









	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}









	public String getOwner_address() {
		return owner_address;
	}









	public void setOwner_address(String owner_address) {
		this.owner_address = owner_address;
	}









	public String getOwner_adhar_no() {
		return owner_adhar_no;
	}









	public void setOwner_adhar_no(String owner_adhar_no) {
		this.owner_adhar_no = owner_adhar_no;
	}









	public String getOwner_email() {
		return owner_email;
	}









	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}









	public String getOwner_phone() {
		return owner_phone;
	}









	public void setOwner_phone(String owner_phone) {
		this.owner_phone = owner_phone;
	}









	public String getOwner_marathi_name() {
		return owner_marathi_name;
	}









	public void setOwner_marathi_name(String owner_marathi_name) {
		this.owner_marathi_name = owner_marathi_name;
	}









	public String getOwner_marathi_address() {
		return owner_marathi_address;
	}









	public void setOwner_marathi_address(String owner_marathi_address) {
		this.owner_marathi_address = owner_marathi_address;
	}









	public String getOwner_power_of_attorney() {
		return owner_power_of_attorney;
	}









	public void setOwner_power_of_attorney(String owner_power_of_attorney) {
		this.owner_power_of_attorney = owner_power_of_attorney;
	}









	public String getOwner_is_applicant() {
		return owner_is_applicant;
	}









	public void setOwner_is_applicant(String owner_is_applicant) {
		this.owner_is_applicant = owner_is_applicant;
	}









	public String getOwner_type() {
		return owner_type;
	}









	public void setOwner_type(String owner_type) {
		this.owner_type = owner_type;
	}









	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
