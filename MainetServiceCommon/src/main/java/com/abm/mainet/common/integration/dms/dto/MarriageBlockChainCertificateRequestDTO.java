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
public class MarriageBlockChainCertificateRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("certificateType")
    private String certificateType;

    @JsonProperty("appId")
    private String appId;

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

    
    
    
	
    
    
}
