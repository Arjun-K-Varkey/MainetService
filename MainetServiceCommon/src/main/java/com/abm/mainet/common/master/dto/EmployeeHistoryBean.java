package com.abm.mainet.common.master.dto;

public class EmployeeHistoryBean {

    private Long empId;
    private String empmobno;
    private String empname;
    private String empmname;
    private String emplname;
    private String empRoleFlag;
    private String empRemark;
    private String autEmail;
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getEmpmobno() {
		return empmobno;
	}
	public void setEmpmobno(String empmobno) {
		this.empmobno = empmobno;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getEmpmname() {
		return empmname;
	}
	public void setEmpmname(String empmname) {
		this.empmname = empmname;
	}
	public String getEmplname() {
		return emplname;
	}
	public void setEmplname(String emplname) {
		this.emplname = emplname;
	}
	public String getEmpRoleFlag() {
		return empRoleFlag;
	}
	public void setEmpRoleFlag(String empRoleFlag) {
		this.empRoleFlag = empRoleFlag;
	}
	public String getEmpRemark() {
		return empRemark;
	}
	public void setEmpRemark(String empRemark) {
		this.empRemark = empRemark;
	}
	public String getAutEmail() {
		return autEmail;
	}
	public void setAutEmail(String autEmail) {
		this.autEmail = autEmail;
	}
	@Override
	public String toString() {
		return "EmployeeHistoryBean [empId=" + empId + ", empmobno=" + empmobno + ", empname=" + empname + ", empmname="
				+ empmname + ", emplname=" + emplname + ", empRoleFlag=" + empRoleFlag + ", empRemark=" + empRemark
				+ ", autEmail=" + autEmail + "]";
	}

    // getters and setters
}