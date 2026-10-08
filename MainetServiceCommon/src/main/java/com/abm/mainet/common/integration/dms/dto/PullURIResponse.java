package com.abm.mainet.common.integration.dms.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "PullURIResponse", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PullURIResponse {

	@XmlElement(name = "ResponseStatus", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private ResponseStatus responseStatus;

    @XmlElement(name = "DocDetails", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private DocDetailsResponse docDetails;

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public DocDetailsResponse getDocDetails() {
		return docDetails;
	}

	public void setDocDetails(DocDetailsResponse docDetails) {
		this.docDetails = docDetails;
	}
}
