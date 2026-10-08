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
public class BpmsRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String loc_id;
    private String short_code;
    

   
        
    public BpmsRequestDto() {
        this.loc_id = "446";
        this.short_code = "KDMCC";
    }



	public String getShort_code() {
		return short_code;
	}



	public void setShort_code(String short_code) {
		this.short_code = short_code;
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
