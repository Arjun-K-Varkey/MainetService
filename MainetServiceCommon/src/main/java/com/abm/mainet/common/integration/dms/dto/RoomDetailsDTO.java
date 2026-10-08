package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RoomDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String room_name;
    private String room_area;
    private String room_width;
    private String room_length;
   

   

	

	public String getRoom_name() {
		return room_name;
	}






	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}






	public String getRoom_area() {
		return room_area;
	}






	public void setRoom_area(String room_area) {
		this.room_area = room_area;
	}






	public String getRoom_width() {
		return room_width;
	}






	public void setRoom_width(String room_width) {
		this.room_width = room_width;
	}






	public String getRoom_length() {
		return room_length;
	}






	public void setRoom_length(String room_length) {
		this.room_length = room_length;
	}






	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
