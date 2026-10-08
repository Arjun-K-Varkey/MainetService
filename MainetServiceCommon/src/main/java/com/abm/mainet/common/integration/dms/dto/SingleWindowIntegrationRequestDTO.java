package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Pritam.Chaphekar
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class SingleWindowIntegrationRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("propertyOrWaterConnectionNo")
    private String propertyOrWaterConnectionNo;
	
	@JsonProperty("flatNo")
    private String flatNo;

    @JsonProperty("value")
    private String value;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("dateInTimestamp")
    private String dateInTimestamp;
    
    @JsonProperty("macId")
    private String macId;

	public String getPropertyOrWaterConnectionNo() {
		return propertyOrWaterConnectionNo;
	}

	public void setPropertyOrWaterConnectionNo(String propertyOrWaterConnectionNo) {
		this.propertyOrWaterConnectionNo = propertyOrWaterConnectionNo;
	}
	

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateInTimestamp() {
		return dateInTimestamp;
	}

	public void setDateInTimestamp(String dateInTimestamp) {
		this.dateInTimestamp = dateInTimestamp;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}
    
    
    

	

    
    
    
	
    
    
}
