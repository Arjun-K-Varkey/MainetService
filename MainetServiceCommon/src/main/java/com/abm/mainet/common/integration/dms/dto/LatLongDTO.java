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
public class LatLongDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String lat_of_plot;
    private String long_of_plot;
   







	public String getLat_of_plot() {
		return lat_of_plot;
	}








	public void setLat_of_plot(String lat_of_plot) {
		this.lat_of_plot = lat_of_plot;
	}








	public String getLong_of_plot() {
		return long_of_plot;
	}








	public void setLong_of_plot(String long_of_plot) {
		this.long_of_plot = long_of_plot;
	}








	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
