package com.abm.mainet.common.integration.dms.domain;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@Entity
@Table(name = "tb_bpms_integration_details")
public class BpmsIntegrationEntity {
	
	
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
    @GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "proposal_number")
    private String proposalNumber;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "created_date")
    private Date createdDate;
    
    //Need to saving success of BPMS updateProposalFlag for Property Tax Y/N.
    @Column(name = "sucess_flag")
    private String sucessFlag;
    
    
  //Need to save flag as Y workFlow is initiated   and N workFlow is Not-initiated.
    @Column(name = "Workflow_Initiated")
    private String workFlowInitiated;
    

    @Column(name = "Updated_Date")
    private Date updatedDate;
    
  //Need to save flag as Y if property save as Draft Mode.
    @Column(name = "Draft_Mode")
    private String draftMode;

    @Column(name = "Property_No")
    private String propertyNo;
    
    @Column(name = "Request_Json")
    private String requestJson;
    
    @Column(name ="receipt_url")
    private String receiptUrl;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
    
    
	public String getSucessFlag() {
		return sucessFlag;
	}

	public void setSucessFlag(String sucessFlag) {
		this.sucessFlag = sucessFlag;
	}

	public String getWorkFlowInitiated() {
		return workFlowInitiated;
	}

	public void setWorkFlowInitiated(String workFlowInitiated) {
		this.workFlowInitiated = workFlowInitiated;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	

	public String getDraftMode() {
		return draftMode;
	}

	public void setDraftMode(String draftMode) {
		this.draftMode = draftMode;
	}
	
	

	public String getPropertyNo() {
		return propertyNo;
	}

	public void setPropertyNo(String propertyNo) {
		this.propertyNo = propertyNo;
	}
	
	

	public String getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	public String[] getPkValues() {
        return new String[] { "BPI", "tb_bpms_integration_details", "id" };
    }

	public String getReceiptUrl() {
		return receiptUrl;
	}

	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}
    
}
