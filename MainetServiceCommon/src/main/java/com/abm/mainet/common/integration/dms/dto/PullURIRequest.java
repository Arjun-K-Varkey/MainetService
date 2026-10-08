package com.abm.mainet.common.integration.dms.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PullURIRequest", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PullURIRequest {
	
    @XmlAttribute(name = "ver")
    private String ver;

    @XmlAttribute(name = "ts")
    private String ts;
    
    @XmlAttribute(name = "txn")
    private String txn;

    @XmlAttribute(name = "orgId")
    private String orgId;

    @XmlAttribute(name = "keyhash")
    private String keyhash;

    @XmlAttribute(name = "format")
    private String format;

    @XmlElement(name = "DocDetails", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private DocDetailsRequest docDetails;

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getTxn() {
		return txn;
	}

	public void setTxn(String txn) {
		this.txn = txn;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getKeyhash() {
		return keyhash;
	}

	public void setKeyhash(String keyhash) {
		this.keyhash = keyhash;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public DocDetailsRequest getDocDetails() {
		return docDetails;
	}

	public void setDocDetails(DocDetailsRequest docDetails) {
		this.docDetails = docDetails;
	}
}
