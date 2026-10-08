package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Saroj.Tambe
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BlockChainCertificateRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ULB_CODE")
    private String ulbCode;

    @JsonProperty("UID")
    private String uid;

    @JsonProperty("HOLDER_NAME")
    private String holderName;

    @JsonProperty("HOLDER_TYPE")
    private String holderType;

    @JsonProperty("DEPARTMENT")
    private String department;

    @JsonProperty("CERTIFICATE_TYPE")
    private String certificateType;

    @JsonProperty("METADATA")
    private Map<String, String> metadata;

    @JsonProperty("ISSUED_BY")
    private String issuedBy;

    @JsonProperty("ISSUED_ON")
    private String issuedOn;

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getHolderType() {
		return holderType;
	}

	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(String issuedOn) {
		this.issuedOn = issuedOn;
	}

    
    
	
    
    
}
