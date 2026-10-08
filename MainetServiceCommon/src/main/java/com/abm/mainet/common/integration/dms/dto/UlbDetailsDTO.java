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
public class UlbDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	    @JsonProperty("ulb_name")
	    private String ulbName;

	    @JsonProperty("ulb_code")
	    private String ulbCode;








	public String getUlbName() {
			return ulbName;
		}








		public void setUlbName(String ulbName) {
			this.ulbName = ulbName;
		}








		public String getUlbCode() {
			return ulbCode;
		}








		public void setUlbCode(String ulbCode) {
			this.ulbCode = ulbCode;
		}








	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
