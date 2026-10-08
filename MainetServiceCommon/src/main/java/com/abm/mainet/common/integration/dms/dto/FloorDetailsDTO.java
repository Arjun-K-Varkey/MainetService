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
public class FloorDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String floor_serial_no;
    private String floor_name;
    private String floor_height;
    private String floor_use;
    private String stilt_floor;
    private String ground_floor;
    private String parking_floor;
    private String floor_area;
    private String floor_area_used;
    private List<CarpetDetailsDTO> carpet_details;
   









	public String getFloor_serial_no() {
		return floor_serial_no;
	}










	public void setFloor_serial_no(String floor_serial_no) {
		this.floor_serial_no = floor_serial_no;
	}










	public String getFloor_name() {
		return floor_name;
	}










	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}










	public String getFloor_height() {
		return floor_height;
	}










	public void setFloor_height(String floor_height) {
		this.floor_height = floor_height;
	}










	public String getFloor_use() {
		return floor_use;
	}










	public void setFloor_use(String floor_use) {
		this.floor_use = floor_use;
	}










	public String getStilt_floor() {
		return stilt_floor;
	}










	public void setStilt_floor(String stilt_floor) {
		this.stilt_floor = stilt_floor;
	}










	public String getGround_floor() {
		return ground_floor;
	}










	public void setGround_floor(String ground_floor) {
		this.ground_floor = ground_floor;
	}










	public String getParking_floor() {
		return parking_floor;
	}










	public void setParking_floor(String parking_floor) {
		this.parking_floor = parking_floor;
	}










	public String getFloor_area() {
		return floor_area;
	}










	public void setFloor_area(String floor_area) {
		this.floor_area = floor_area;
	}










	public String getFloor_area_used() {
		return floor_area_used;
	}










	public void setFloor_area_used(String floor_area_used) {
		this.floor_area_used = floor_area_used;
	}










	public List<CarpetDetailsDTO> getCarpet_details() {
		return carpet_details;
	}










	public void setCarpet_details(List<CarpetDetailsDTO> carpet_details) {
		this.carpet_details = carpet_details;
	}










	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
