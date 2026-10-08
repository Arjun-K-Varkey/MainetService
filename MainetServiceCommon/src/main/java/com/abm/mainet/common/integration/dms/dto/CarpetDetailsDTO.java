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
public class CarpetDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String carpet_area;
    private String carpet_name;
    private List<RoomDetailsDTO> room_details;
   


	public String getCarpet_area() {
		return carpet_area;
	}






	public void setCarpet_area(String carpet_area) {
		this.carpet_area = carpet_area;
	}






	public String getCarpet_name() {
		return carpet_name;
	}






	public void setCarpet_name(String carpet_name) {
		this.carpet_name = carpet_name;
	}






	public List<RoomDetailsDTO> getRoom_details() {
		return room_details;
	}






	public void setRoom_details(List<RoomDetailsDTO> room_details) {
		this.room_details = room_details;
	}






	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
