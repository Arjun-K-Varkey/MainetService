package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ProjectDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	 private String project_code;
	    private String project_name;
	    private String project_risk_flag;
	    private String proposal_id;
	    private String site_address;
	    private String residential_plot_area;
	    private String total_plot_area;
	    private String application_date;
	    private String approval_date;
	    private String construction_type;

	    @JsonProperty("Administrative_ward")
	    private String administrative_ward;

	    @JsonProperty("hierarchy_details")
	    private List<HierarchyDetailsDTO> hierarchyDetails;
   







	public String getProject_code() {
			return project_code;
		}








		public void setProject_code(String project_code) {
			this.project_code = project_code;
		}








		public String getProject_name() {
			return project_name;
		}








		public void setProject_name(String project_name) {
			this.project_name = project_name;
		}








		public String getProject_risk_flag() {
			return project_risk_flag;
		}








		public void setProject_risk_flag(String project_risk_flag) {
			this.project_risk_flag = project_risk_flag;
		}








		public String getProposal_id() {
			return proposal_id;
		}








		public void setProposal_id(String proposal_id) {
			this.proposal_id = proposal_id;
		}








		public String getSite_address() {
			return site_address;
		}








		public void setSite_address(String site_address) {
			this.site_address = site_address;
		}








		public String getResidential_plot_area() {
			return residential_plot_area;
		}








		public void setResidential_plot_area(String residential_plot_area) {
			this.residential_plot_area = residential_plot_area;
		}








		public String getTotal_plot_area() {
			return total_plot_area;
		}








		public void setTotal_plot_area(String total_plot_area) {
			this.total_plot_area = total_plot_area;
		}








		public String getApplication_date() {
			return application_date;
		}








		public void setApplication_date(String application_date) {
			this.application_date = application_date;
		}








		public String getApproval_date() {
			return approval_date;
		}








		public void setApproval_date(String approval_date) {
			this.approval_date = approval_date;
		}








		public String getConstruction_type() {
			return construction_type;
		}








		public void setConstruction_type(String construction_type) {
			this.construction_type = construction_type;
		}








		public String getAdministrative_ward() {
			return administrative_ward;
		}








		public void setAdministrative_ward(String administrative_ward) {
			this.administrative_ward = administrative_ward;
		}








		public List<HierarchyDetailsDTO> getHierarchyDetails() {
			return hierarchyDetails;
		}








		public void setHierarchyDetails(List<HierarchyDetailsDTO> hierarchyDetails) {
			this.hierarchyDetails = hierarchyDetails;
		}








	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
