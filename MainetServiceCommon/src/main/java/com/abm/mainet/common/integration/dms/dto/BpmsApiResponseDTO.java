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
public class BpmsApiResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	 private String status;
	 private List<ResponseDataDTO> responseData;
   







	public String getStatus() {
		return status;
	}








	public void setStatus(String status) {
		this.status = status;
	}








	public List<ResponseDataDTO> getResponseData() {
		return responseData;
	}








	public void setResponseData(List<ResponseDataDTO> responseData) {
		this.responseData = responseData;
	}








	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
