package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Pritam.Chaphekar
 *
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "PROPERTY_NUMBER",
    "CONNECTION_NUMBER",
    "FLAT_NUMBER",
    "OWNER_NAME",
    "ADDRESS",
    "TYPE_OF_PROPERTY",
    "TYPE_OF_CONNECTION",
    "LASTEST_BILL_GENERATION_DATE",
    "CURRENT_AMOUNT_TO_BE_PAID",
    "PDF_OF_NODUES_CERTIFICATE",
    "LINK_FOR_PAYMENT",
    "LINK_FOR_APPLY_NOC",
    "STATUS",
    "ERROR_MESSAGE"
    
})
public class SingleWindowIntegrationResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("PROPERTY_NUMBER")
    private String propertyNumber;
    
    @JsonProperty("CONNECTION_NUMBER")
    private String connectionNumber;
    
    @JsonProperty("FLAT_NUMBER")
    private String flatNumber;
    
    @JsonProperty("OWNER_NAME")
    private String ownerName;

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("TYPE_OF_PROPERTY")
    private String typeOfProperty;
    
    @JsonProperty("TYPE_OF_CONNECTION")
    private String typeOfConnection;

    @JsonProperty("LASTEST_BILL_GENERATION_DATE")
    private String lastestBillGenerationDate;
    
    @JsonProperty("CURRENT_AMOUNT_TO_BE_PAID")
    private String currentAmountToBePaid;
    
    @JsonProperty("PDF_OF_NODUES_CERTIFICATE")
    private String pdfOfNoDuesCertificate;
    
    @JsonProperty("LINK_FOR_PAYMENT")
    private String linkForPayment;
    
    @JsonProperty("LINK_FOR_APPLY_NOC")
    private String linkForApplyNoc;
    
    @JsonProperty("STATUS")
    private String status;
    
    @JsonProperty("ERROR_MESSAGE")
    private String errorMessage;
    
    

	public String getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(String propertyNumber) {
		this.propertyNumber = propertyNumber;
	}
	

	public String getConnectionNumber() {
		return connectionNumber;
	}

	public void setConnectionNumber(String connectionNumber) {
		this.connectionNumber = connectionNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTypeOfProperty() {
		return typeOfProperty;
	}

	public void setTypeOfProperty(String typeOfProperty) {
		this.typeOfProperty = typeOfProperty;
	}

	
	public String getTypeOfConnection() {
		return typeOfConnection;
	}

	public void setTypeOfConnection(String typeOfConnection) {
		this.typeOfConnection = typeOfConnection;
	}

	public String getLastestBillGenerationDate() {
		return lastestBillGenerationDate;
	}

	public void setLastestBillGenerationDate(String lastestBillGenerationDate) {
		this.lastestBillGenerationDate = lastestBillGenerationDate;
	}

	public String getCurrentAmountToBePaid() {
		return currentAmountToBePaid;
	}

	public void setCurrentAmountToBePaid(String currentAmountToBePaid) {
		this.currentAmountToBePaid = currentAmountToBePaid;
	}
	
	public String getPdfOfNoDuesCertificate() {
		return pdfOfNoDuesCertificate;
	}

	public void setPdfOfNoDuesCertificate(String pdfOfNoDuesCertificate) {
		this.pdfOfNoDuesCertificate = pdfOfNoDuesCertificate;
	}

	public String getLinkForPayment() {
		return linkForPayment;
	}

	public void setLinkForPayment(String linkForPayment) {
		this.linkForPayment = linkForPayment;
	}

	public String getLinkForApplyNoc() {
		return linkForApplyNoc;
	}

	public void setLinkForApplyNoc(String linkForApplyNoc) {
		this.linkForApplyNoc = linkForApplyNoc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
   
    
    
}
	

    
    
	
    
    

