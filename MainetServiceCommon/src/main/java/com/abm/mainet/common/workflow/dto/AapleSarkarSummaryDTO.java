package com.abm.mainet.common.workflow.dto;

import java.util.List;

public class AapleSarkarSummaryDTO {
	
	private List<RowData> RowData;
    private String Year;
    private int Month;
    
    
	public List<RowData> getRowData() {
		return RowData;
	}
	public void setRowData(List<RowData> rowData) {
		RowData = rowData;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
    
    

}
