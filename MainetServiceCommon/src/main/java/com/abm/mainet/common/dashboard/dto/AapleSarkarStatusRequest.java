package com.abm.mainet.common.dashboard.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
@JsonSerialize
@JsonDeserialize
public class AapleSarkarStatusRequest implements Serializable{
	
    private static final long serialVersionUID = 1L;

    @JsonProperty("AppID")
    private String AppID;
    
    @JsonProperty("ServiceID")
    private String ServiceID;

    @JsonProperty("DeptName")
    private String DeptName;

    @JsonProperty("Language")
    private String Language;

	public String getAppID() {
		return AppID;
	}

	public void setAppID(String appID) {
		AppID = appID;
	}

	public String getServiceID() {
		return ServiceID;
	}

	public void setServiceID(String serviceID) {
		ServiceID = serviceID;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	

}
