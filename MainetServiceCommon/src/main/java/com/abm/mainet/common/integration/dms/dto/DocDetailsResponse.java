package com.abm.mainet.common.integration.dms.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class DocDetailsResponse {
	
	@XmlElement(name = "IssuedTo", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private IssuedTo issuedTo;

    @XmlElement(name = "URI", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String uri;

    @XmlElement(name = "DocContent", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String docContent;

    @XmlElement(name = "DataContent", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private String dataContent;

	public IssuedTo getIssuedTo() {
		return issuedTo;
	}

	public void setIssuedTo(IssuedTo issuedTo) {
		this.issuedTo = issuedTo;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDocContent() {
		return docContent;
	}

	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

}
