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
public class HierarchyDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	 private String level_1;
	    private String level_1_name;
	    private String level_2;
	    private String level_2_name;
   








	public String getLevel_1() {
			return level_1;
		}









		public void setLevel_1(String level_1) {
			this.level_1 = level_1;
		}









		public String getLevel_1_name() {
			return level_1_name;
		}









		public void setLevel_1_name(String level_1_name) {
			this.level_1_name = level_1_name;
		}









		public String getLevel_2() {
			return level_2;
		}









		public void setLevel_2(String level_2) {
			this.level_2 = level_2;
		}









		public String getLevel_2_name() {
			return level_2_name;
		}









		public void setLevel_2_name(String level_2_name) {
			this.level_2_name = level_2_name;
		}









	public static long getSerialversionuid() {
		return serialVersionUID;
	}
        
    
}
