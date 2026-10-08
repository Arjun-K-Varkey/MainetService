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
public class PlotDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String plot_no;
    private String property_UPIN;
   










	public String getPlot_no() {
		return plot_no;
	}











	public void setPlot_no(String plot_no) {
		this.plot_no = plot_no;
	}











	public String getProperty_UPIN() {
		return property_UPIN;
	}











	public void setProperty_UPIN(String property_UPIN) {
		this.property_UPIN = property_UPIN;
	}











	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
