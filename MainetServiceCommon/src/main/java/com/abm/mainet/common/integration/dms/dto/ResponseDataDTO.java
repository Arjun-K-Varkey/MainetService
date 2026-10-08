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
public class ResponseDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	 @JsonProperty("ulb_details")
	    private List<UlbDetailsDTO> ulbDetails;

	    @JsonProperty("project_details")
	    private List<ProjectDetailsDTO> projectDetails;

	    @JsonProperty("owner_details")
	    private List<OwnerDetailsDTO> ownerDetails;

	    @JsonProperty("building_details")
	    private List<BuildingDetailsDTO> buildingDetails;

	    @JsonProperty("lat_long_detail")
	    private List<LatLongDTO> latLongDetail;

	    @JsonProperty("plot_details")
	    private List<PlotDetailsDTO> plotDetails;

	


	public List<UlbDetailsDTO> getUlbDetails() {
			return ulbDetails;
		}




		public void setUlbDetails(List<UlbDetailsDTO> ulbDetails) {
			this.ulbDetails = ulbDetails;
		}




		public List<ProjectDetailsDTO> getProjectDetails() {
			return projectDetails;
		}




		public void setProjectDetails(List<ProjectDetailsDTO> projectDetails) {
			this.projectDetails = projectDetails;
		}




		public List<OwnerDetailsDTO> getOwnerDetails() {
			return ownerDetails;
		}




		public void setOwnerDetails(List<OwnerDetailsDTO> ownerDetails) {
			this.ownerDetails = ownerDetails;
		}




		public List<BuildingDetailsDTO> getBuildingDetails() {
			return buildingDetails;
		}




		public void setBuildingDetails(List<BuildingDetailsDTO> buildingDetails) {
			this.buildingDetails = buildingDetails;
		}




		public List<LatLongDTO> getLatLongDetail() {
			return latLongDetail;
		}




		public void setLatLongDetail(List<LatLongDTO> latLongDetail) {
			this.latLongDetail = latLongDetail;
		}




		public List<PlotDetailsDTO> getPlotDetails() {
			return plotDetails;
		}




		public void setPlotDetails(List<PlotDetailsDTO> plotDetails) {
			this.plotDetails = plotDetails;
		}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
