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
public class BuildingDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String building_serial_no;
    private String building_no;
    private String building_name;
    private String building_type;
    private String building_sub_category;
    private String building_no_of_floors;
    private String building_base_height;
    private String building_plinth_level;
    private String building_buildup_area;
    private String building_tenements_units;
    private String building_commercial_area;
    private String building_industrial_area;
    private String building_height;
    private String building_front_margin;
    private String building_rear_margin;
    private String building_left_margin;
    private String building_right_margin;
    private String building_premium_buildup_area;

    @JsonProperty("floor_details")
    private List<FloorDetailsDTO> floorDetails;
   



	public String getBuilding_serial_no() {
		return building_serial_no;
	}




	public void setBuilding_serial_no(String building_serial_no) {
		this.building_serial_no = building_serial_no;
	}




	public String getBuilding_no() {
		return building_no;
	}




	public void setBuilding_no(String building_no) {
		this.building_no = building_no;
	}




	public String getBuilding_name() {
		return building_name;
	}




	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}




	public String getBuilding_type() {
		return building_type;
	}




	public void setBuilding_type(String building_type) {
		this.building_type = building_type;
	}




	public String getBuilding_sub_category() {
		return building_sub_category;
	}




	public void setBuilding_sub_category(String building_sub_category) {
		this.building_sub_category = building_sub_category;
	}




	public String getBuilding_no_of_floors() {
		return building_no_of_floors;
	}




	public void setBuilding_no_of_floors(String building_no_of_floors) {
		this.building_no_of_floors = building_no_of_floors;
	}




	public String getBuilding_base_height() {
		return building_base_height;
	}




	public void setBuilding_base_height(String building_base_height) {
		this.building_base_height = building_base_height;
	}




	public String getBuilding_plinth_level() {
		return building_plinth_level;
	}




	public void setBuilding_plinth_level(String building_plinth_level) {
		this.building_plinth_level = building_plinth_level;
	}




	public String getBuilding_buildup_area() {
		return building_buildup_area;
	}




	public void setBuilding_buildup_area(String building_buildup_area) {
		this.building_buildup_area = building_buildup_area;
	}




	public String getBuilding_tenements_units() {
		return building_tenements_units;
	}




	public void setBuilding_tenements_units(String building_tenements_units) {
		this.building_tenements_units = building_tenements_units;
	}




	public String getBuilding_commercial_area() {
		return building_commercial_area;
	}




	public void setBuilding_commercial_area(String building_commercial_area) {
		this.building_commercial_area = building_commercial_area;
	}




	public String getBuilding_industrial_area() {
		return building_industrial_area;
	}




	public void setBuilding_industrial_area(String building_industrial_area) {
		this.building_industrial_area = building_industrial_area;
	}




	public String getBuilding_height() {
		return building_height;
	}




	public void setBuilding_height(String building_height) {
		this.building_height = building_height;
	}




	public String getBuilding_front_margin() {
		return building_front_margin;
	}




	public void setBuilding_front_margin(String building_front_margin) {
		this.building_front_margin = building_front_margin;
	}




	public String getBuilding_rear_margin() {
		return building_rear_margin;
	}




	public void setBuilding_rear_margin(String building_rear_margin) {
		this.building_rear_margin = building_rear_margin;
	}




	public String getBuilding_left_margin() {
		return building_left_margin;
	}




	public void setBuilding_left_margin(String building_left_margin) {
		this.building_left_margin = building_left_margin;
	}




	public String getBuilding_right_margin() {
		return building_right_margin;
	}




	public void setBuilding_right_margin(String building_right_margin) {
		this.building_right_margin = building_right_margin;
	}




	public String getBuilding_premium_buildup_area() {
		return building_premium_buildup_area;
	}




	public void setBuilding_premium_buildup_area(String building_premium_buildup_area) {
		this.building_premium_buildup_area = building_premium_buildup_area;
	}




	public List<FloorDetailsDTO> getFloorDetails() {
		return floorDetails;
	}




	public void setFloorDetails(List<FloorDetailsDTO> floorDetails) {
		this.floorDetails = floorDetails;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
