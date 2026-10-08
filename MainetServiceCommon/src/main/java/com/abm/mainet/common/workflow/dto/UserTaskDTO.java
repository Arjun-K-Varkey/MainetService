package com.abm.mainet.common.workflow.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author kailash.agarwal
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long workflowTaskId;
    private Long taskId;
    private String taskName;
    private String taskStatus;
    private Long applicationId;
    private String referenceId;
    private Long workflowId;
    private Long workflowReqId;
    private Long orgId;
    private Long deptId;
    private String deptName;
    private String deptNameReg;
    private Long serviceId;
    private String serviceName;
    private String serviceNameReg;
    private Long serviceEventId;
    private String serviceEventName;
    private String serviceEventNameReg;
    private String serviceEventUrl;
    private String actorId;
    private String actorRole;
    private Long currentEscalationLevel;
    private Long taskSlaDurationInMS;
    private Long curentCheckerLevel;
    private Long currentCheckerGroup;
    private Date createdDate;
    private Long createdBy;
    private Date modifiedDate;
    private Long modifiedBy;
    private Date dateOfAssignment;
    private Date dateOfCompletion;
    private String requestDate;
    private String lastDecision;
    private String comments;
    private String taskStatusDesc;
    private String smShortCode;
    private String flag;
    private String type;
    private String issuedDate;
    private Date hearingDate;
    
    private String att1;//Current Activity(history)
    private String att2; //Empid to whom we have sent
    private String att3; //to whom we have sent employee name
    private String att4; //Default Activty(nect user ko jo bydefault)
    private String att5; //from whom application received(empid)
    private String att6; //from whom application received(emp name)
    private String att7;
    private String att8;
    private String att9;
    private String att10;

    public Long getWorkflowTaskId() {
        return workflowTaskId;
    }

    public void setWorkflowTaskId(Long workflowTaskId) {
        this.workflowTaskId = workflowTaskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getWorkflowReqId() {
        return workflowReqId;
    }

    public void setWorkflowReqId(Long workflowReqId) {
        this.workflowReqId = workflowReqId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNameReg() {
        return deptNameReg;
    }

    public void setDeptNameReg(String deptNameReg) {
        this.deptNameReg = deptNameReg;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameReg() {
        return serviceNameReg;
    }

    public void setServiceNameReg(String serviceNameReg) {
        this.serviceNameReg = serviceNameReg;
    }

    public Long getServiceEventId() {
        return serviceEventId;
    }

    public void setServiceEventId(Long serviceEventId) {
        this.serviceEventId = serviceEventId;
    }

    public String getServiceEventName() {
        return serviceEventName;
    }

    public void setServiceEventName(String serviceEventName) {
        this.serviceEventName = serviceEventName;
    }

    public String getServiceEventNameReg() {
        return serviceEventNameReg;
    }

    public void setServiceEventNameReg(String serviceEventNameReg) {
        this.serviceEventNameReg = serviceEventNameReg;
    }

    public String getServiceEventUrl() {
        return serviceEventUrl;
    }

    public void setServiceEventUrl(String serviceEventUrl) {
        this.serviceEventUrl = serviceEventUrl;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorRole() {
        return actorRole;
    }

    public void setActorRole(String actorRole) {
        this.actorRole = actorRole;
    }

    public Long getCurrentEscalationLevel() {
        return currentEscalationLevel;
    }

    public void setCurrentEscalationLevel(Long currentEscalationLevel) {
        this.currentEscalationLevel = currentEscalationLevel;
    }

    public Long getTaskSlaDurationInMS() {
        return taskSlaDurationInMS;
    }

    public void setTaskSlaDurationInMS(Long taskSlaDurationInMS) {
        this.taskSlaDurationInMS = taskSlaDurationInMS;
    }

    public Long getCurentCheckerLevel() {
        return curentCheckerLevel;
    }

    public void setCurentCheckerLevel(Long curentCheckerLevel) {
        this.curentCheckerLevel = curentCheckerLevel;
    }

    public Long getCurrentCheckerGroup() {
        return currentCheckerGroup;
    }

    public void setCurrentCheckerGroup(Long currentCheckerGroup) {
        this.currentCheckerGroup = currentCheckerGroup;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getDateOfAssignment() {
        return dateOfAssignment;
    }

    public void setDateOfAssignment(Date dateOfAssignment) {
        this.dateOfAssignment = dateOfAssignment;
    }

    public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getLastDecision() {
        return lastDecision;
    }

    public void setLastDecision(String lastDecision) {
        this.lastDecision = lastDecision;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTaskStatusDesc() {
        return taskStatusDesc;
    }

    public void setTaskStatusDesc(String taskStatusDesc) {
        this.taskStatusDesc = taskStatusDesc;
    }

    public String getSmShortCode() {
        return smShortCode;
    }

    public void setSmShortCode(String smShortCode) {
        this.smShortCode = smShortCode;
    }

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(Date hearingDate) {
		this.hearingDate = hearingDate;
	}

	public String getAtt1() {
		return att1;
	}

	public void setAtt1(String att1) {
		this.att1 = att1;
	}

	public String getAtt2() {
		return att2;
	}

	public void setAtt2(String att2) {
		this.att2 = att2;
	}

	public String getAtt3() {
		return att3;
	}

	public void setAtt3(String att3) {
		this.att3 = att3;
	}

	public String getAtt4() {
		return att4;
	}

	public void setAtt4(String att4) {
		this.att4 = att4;
	}

	public String getAtt5() {
		return att5;
	}

	public void setAtt5(String att5) {
		this.att5 = att5;
	}

	public String getAtt6() {
		return att6;
	}

	public void setAtt6(String att6) {
		this.att6 = att6;
	}

	public String getAtt7() {
		return att7;
	}

	public void setAtt7(String att7) {
		this.att7 = att7;
	}

	public String getAtt8() {
		return att8;
	}

	public void setAtt8(String att8) {
		this.att8 = att8;
	}

	public String getAtt9() {
		return att9;
	}

	public void setAtt9(String att9) {
		this.att9 = att9;
	}

	public String getAtt10() {
		return att10;
	}

	public void setAtt10(String att10) {
		this.att10 = att10;
	}
    
    
}
