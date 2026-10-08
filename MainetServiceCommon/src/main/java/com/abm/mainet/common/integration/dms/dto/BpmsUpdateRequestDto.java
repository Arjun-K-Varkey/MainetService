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
public class BpmsUpdateRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String loc_id;
    private String prop_id;
   

   
	public String getProp_id() {
		return prop_id;
	}



	public void setProp_id(String prop_id) {
		this.prop_id = prop_id;
	}



	public String getLoc_id() {
		return loc_id;
	}



	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}



	



	



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    

    
    
	
    
    
}
