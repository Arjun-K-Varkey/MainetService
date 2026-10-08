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
public class BpmsResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;
	private List<ResponseData> responseData;

	public static class ResponseData {
		private String proposal_number;
		private String project_code;

		public String getProposal_number() {
			return proposal_number;
		}

		public void setProposal_number(String proposal_number) {
			this.proposal_number = proposal_number;
		}

		public String getProject_code() {
			return project_code;
		}

		public void setProject_code(String project_code) {
			this.project_code = project_code;
		}

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ResponseData> getResponseData() {
		return responseData;
	}

	public void setResponseData(List<ResponseData> responseData) {
		this.responseData = responseData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
