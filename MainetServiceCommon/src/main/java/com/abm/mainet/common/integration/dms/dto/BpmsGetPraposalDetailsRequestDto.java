package com.abm.mainet.common.integration.dms.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BpmsGetPraposalDetailsRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
    private String proposal_id;
    private String loc_id;
   

   

	public String getProposal_id() {
		return proposal_id;
	}



	public void setProposal_id(String proposal_id) {
		this.proposal_id = proposal_id;
	}


	public String getLoc_id() {
		return loc_id;
	}


	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
