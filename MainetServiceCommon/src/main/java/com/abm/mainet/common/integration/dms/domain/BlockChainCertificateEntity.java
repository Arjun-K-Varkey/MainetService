package com.abm.mainet.common.integration.dms.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_BLOCKCHAIN_DATA")
public class BlockChainCertificateEntity {
	
	
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "BC_ID", nullable = false)
	private Long bcId;

    @Column(name = "UID")
    private String uid;

    @Column(name = "AppId")
    private String appId;

    @Column(name = "Request_Json")
    private String requestJson;

    @Column(name = "Issue", nullable=true)
    private String issue;

    @Column(name = "Reissue", nullable=true)
    private String reissue;

    @Column(name = "CREATED_DATE")
	private Date lmodDate;

    @Column(name = "UPDATED_DATE")
	private Date updatedDate;
    
    @Column(name = "orgId", nullable=true )
	private Long orgId;
    
    @Column(name = "UDF1", nullable=true)
    private String uDF1;

    @Column(name = "UDF2", nullable=true)
    private String uDF2;

    @Column(name = "UDF3", nullable=true)
    private String uDF3;

    @Column(name = "UDF4", nullable=true)
    private String uDF4;

    // ===== Getters and Setters =====
    public Long getBcId() {
        return bcId;
    }

    public void setBcId(Long bcId) {
        this.bcId = bcId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getReissue() {
        return reissue;
    }

    public void setReissue(String reissue) {
        this.reissue = reissue;
    }

    public Date getLmodDate() {
        return lmodDate;
    }

    public void setLmodDate(Date lmodDate) {
        this.lmodDate = lmodDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getuDF1() {
        return uDF1;
    }

    public void setuDF1(String uDF1) {
        this.uDF1 = uDF1;
    }

    public String getuDF2() {
        return uDF2;
    }

    public void setuDF2(String uDF2) {
        this.uDF2 = uDF2;
    }

    public String getuDF3() {
        return uDF3;
    }

    public void setuDF3(String uDF3) {
        this.uDF3 = uDF3;
    }

    public String getuDF4() {
        return uDF4;
    }

    public void setuDF4(String uDF4) {
        this.uDF4 = uDF4;
    }

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String[] getPkValues() {
        return new String[] { "BCI", "TB_BLOCKCHAIN_DATA", "BC_ID" };
    }
    
    
}
