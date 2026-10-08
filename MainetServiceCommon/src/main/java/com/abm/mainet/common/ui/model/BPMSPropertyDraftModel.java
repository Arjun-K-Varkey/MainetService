package com.abm.mainet.common.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.integration.dms.dto.BpmsSummaryPageDto;
import com.abm.mainet.common.integration.dto.DocumentDetailsVO;
import com.abm.mainet.common.master.dto.EmployeeBean;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.workflow.dto.UserTaskDTO;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;


/**
 * @author Saroj.Tambe
 *
 */

@Component
@Scope("session")
public class BPMSPropertyDraftModel extends AbstractFormModel{
	
	private static final long serialVersionUID = -5686442082255443447L;
	
	private List<EmployeeBean> empList = new ArrayList<>();
	
	private String viewMode;
	
	
	private List<LookUp> wardList = new ArrayList<LookUp>();
	private List<UserTaskDTO> taskList = new ArrayList<>();
	private Long empId;
	private Long ward;
	private List<Long> empIds;
	private List<EmployeeBean> forwardEmpList = new ArrayList<>();
	private List<DocumentDetailsVO> attachments = new ArrayList<>();
	private String comments;
	private WorkflowTaskAction careDepartmentAction = new WorkflowTaskAction();
	private List<String> applicationIdWithTaskId;
	private String decision;
	private List<Long> proposalNoList;
	private Long proposalNo;
	List<BpmsSummaryPageDto> bpmsSummaryPageDtoList;

	

	public List<EmployeeBean> getEmpList() {
		return empList;
	}

	public void setEmpList(List<EmployeeBean> empList) {
		this.empList = empList;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public List<LookUp> getWardList() {
		return wardList;
	}

	public void setWardList(List<LookUp> wardList) {
		this.wardList = wardList;
	}

	public List<UserTaskDTO> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<UserTaskDTO> taskList) {
		this.taskList = taskList;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Long getWard() {
		return ward;
	}

	public void setWard(Long ward) {
		this.ward = ward;
	}

	public List<Long> getEmpIds() {
		return empIds;
	}

	public void setEmpIds(List<Long> empIds) {
		this.empIds = empIds;
	}

	public List<EmployeeBean> getForwardEmpList() {
		return forwardEmpList;
	}

	public void setForwardEmpList(List<EmployeeBean> forwardEmpList) {
		this.forwardEmpList = forwardEmpList;
	}

	public List<DocumentDetailsVO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DocumentDetailsVO> attachments) {
		this.attachments = attachments;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public WorkflowTaskAction getCareDepartmentAction() {
		return careDepartmentAction;
	}

	public void setCareDepartmentAction(WorkflowTaskAction careDepartmentAction) {
		this.careDepartmentAction = careDepartmentAction;
	}

	public List<String> getApplicationIdWithTaskId() {
		return applicationIdWithTaskId;
	}

	public void setApplicationIdWithTaskId(List<String> applicationIdWithTaskId) {
		this.applicationIdWithTaskId = applicationIdWithTaskId;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public List<Long> getProposalNoList() {
		return proposalNoList;
	}

	public void setProposalNoList(List<Long> proposalNoList) {
		this.proposalNoList = proposalNoList;
	}

	public Long getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(Long proposalNo) {
		this.proposalNo = proposalNo;
	}

	public List<BpmsSummaryPageDto> getBpmsSummaryPageDtoList() {
		return bpmsSummaryPageDtoList;
	}

	public void setBpmsSummaryPageDtoList(List<BpmsSummaryPageDto> bpmsSummaryPageDtoList) {
		this.bpmsSummaryPageDtoList = bpmsSummaryPageDtoList;
	}
	
	
	
}
