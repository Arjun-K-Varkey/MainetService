/**
 * 
 */
package com.abm.mainet.common.integration.dto;

/**
 * @author akshata.bhat
 *
 */
public class CustomerInfoDTO {

	private String name;
	private String address;
	private Long csIdn;
	private String connectionSize;
	private Long csListatus;
	private String csCcntype;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCsIdn() {
		return csIdn;
	}
	public void setCsIdn(Long csIdn) {
		this.csIdn = csIdn;
	}
	public String getConnectionSize() {
		return connectionSize;
	}
	public void setConnectionSize(String connectionSize) {
		this.connectionSize = connectionSize;
	}
	public Long getCsListatus() {
		return csListatus;
	}
	public void setCsListatus(Long csListatus) {
		this.csListatus = csListatus;
	}
	public String getCsCcntype() {
		return csCcntype;
	}
	public void setCsCcntype(String csCcntype) {
		this.csCcntype = csCcntype;
	}
}
