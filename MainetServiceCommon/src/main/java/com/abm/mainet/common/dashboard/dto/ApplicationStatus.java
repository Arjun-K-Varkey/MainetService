package com.abm.mainet.common.dashboard.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationStatus {
	
	    @JsonProperty("ApplicationID")
		private String applicationID;
	    
	    @JsonProperty("ApplicantName")
	    private String applicantName;
	    
	    @JsonProperty("ServiceName")
	    private String serviceName;
	    
	    
	    @JsonProperty("EstimatedDisbursalDays")
	    private Long estimatedDisbursalDays;
	    
	    @JsonProperty("NextActionRequiredDetails")
	    private String nextActionRequiredDetails;
	    
	    @JsonProperty("FinalDecision")
	    private String finalDecision;
	    
	    @JsonProperty("DepartmentRedirectionURL")
	    private String DepartmentRedirectionURL;
	    
	    @JsonProperty("ApplicationSubmissionDate")
	    private String applicationSubmissionDate;
	    
	    @JsonProperty("ApplicationPaymentDate")
	    private String applicationPaymentDate;
	    
	   
	    // Desk summary
	    @JsonProperty("TotalNumberOfDesks")
	    private Integer totalNumberOfDesks;
	    
	    @JsonProperty("CurrentDeskNumber")
	    private Integer currentDeskNumber;
	    
	    @JsonProperty("NextDeskNumber")
	    private Integer nextDeskNumber;
	    
	    
	    // Desk detailed logs
	    @JsonProperty("DeskDetails")
	    private List<DeskDetails> deskDetails;

		public String getApplicationID() {
			return applicationID;
		}

		public void setApplicationID(String applicationID) {
			this.applicationID = applicationID;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		/*public String getApplicantName() {
			return applicantName;
		}

		public void setApplicantName(String applicantName) {
			this.applicantName = applicantName;
		}*/

	/*
	 * public Long getEstimatedSubstantialDays() { return estimatedDisbursalDays; }
	 */

		

		public void setApplicationSubmissionDate(String applicationSubmissionDate) {
			this.applicationSubmissionDate = applicationSubmissionDate;
		}

		public String getFinalDecision() {
			return finalDecision;
		}

		public void setFinalDecision(String finalDecision) {
			this.finalDecision = finalDecision;
		}

		public Integer getTotalNumberOfDesks() {
			return totalNumberOfDesks;
		}

		public void setTotalNumberOfDesks(Integer totalNumberOfDesks) {
			this.totalNumberOfDesks = totalNumberOfDesks;
		}

		public Integer getCurrentDeskNumber() {
			return currentDeskNumber;
		}

		public void setCurrentDeskNumber(Integer currentDeskNumber) {
			this.currentDeskNumber = currentDeskNumber;
		}

		public Integer getNextDeskNumber() {
			return nextDeskNumber;
		}

		public void setNextDeskNumber(Integer nextDeskNumber) {
			this.nextDeskNumber = nextDeskNumber;
		}

		public List<DeskDetails> getDeskDetails() {
			return deskDetails;
		}

		public void setDeskDetails(List<DeskDetails> deskDetails) {
			this.deskDetails = deskDetails;
		}

		public Long getEstimatedDisbursalDays() {
			return estimatedDisbursalDays;
		}

		public void setEstimatedDisbursalDays(Long estimatedDisbursalDays) {
			this.estimatedDisbursalDays = estimatedDisbursalDays;
		}

		public String getApplicantName() {
			return applicantName;
		}

		public void setApplicantName(String applicantName) {
			this.applicantName = applicantName;
		}

		public String getApplicationSubmissionDate() {
			return applicationSubmissionDate;
		}

		public String getNextActionRequiredDetails() {
			return nextActionRequiredDetails;
		}

		public void setNextActionRequiredDetails(String nextActionRequiredDetails) {
			this.nextActionRequiredDetails = nextActionRequiredDetails;
		}

		public String getApplicationPaymentDate() {
			return applicationPaymentDate;
		}

		public void setApplicationPaymentDate(String applicationPaymentDate) {
			this.applicationPaymentDate = applicationPaymentDate;
		}

		@JsonProperty("DepartmentRedirectionURL")
		public String getDepartmentRedirectionURL() {
			return DepartmentRedirectionURL;
		}
		@JsonProperty("DepartmentRedirectionURL")
		public void setDepartmentRedirectionURL(String departmentRedirectionURL) {
			DepartmentRedirectionURL = departmentRedirectionURL;
		}

	


}
