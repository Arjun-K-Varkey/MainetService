package com.abm.mainet.common.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApplicationGranularStatusEntity {

	@Id
    @Column(name = "DeskSequence")
    private Integer deskSequence;

    @Column(name = "DeskName")
    private String deskName;

    @Column(name = "Status")
    private String status;

    @Column(name = "Decision")
    private String decision;

    @Column(name = "AssignDateTime")
    private String assignDateTime;

    @Column(name = "CompletionDateTime")
    private String completionDateTime;

    @Column(name = "ActionEmployeeId")
    private String actionEmployeeId;
    
    @Column(name = "ActionComments")
    private String actionComments;

    @Column(name = "Meaning")
    private String meaning;

	public Integer getDeskSequence() {
		return deskSequence;
	}

	public void setDeskSequence(Integer deskSequence) {
		this.deskSequence = deskSequence;
	}

	public String getDeskName() {
		return deskName;
	}

	public void setDeskName(String deskName) {
		this.deskName = deskName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getAssignDateTime() {
		return assignDateTime;
	}

	public void setAssignDateTime(String assignDateTime) {
		this.assignDateTime = assignDateTime;
	}

	public String getCompletionDateTime() {
		return completionDateTime;
	}

	public void setCompletionDateTime(String completionDateTime) {
		this.completionDateTime = completionDateTime;
	}

	public String getActionEmployeeId() {
		return actionEmployeeId;
	}

	public void setActionEmployeeId(String actionEmployeeId) {
		this.actionEmployeeId = actionEmployeeId;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getActionComments() {
		return actionComments;
	}

	public void setActionComments(String actionComments) {
		this.actionComments = actionComments;
	}
    
    
}
