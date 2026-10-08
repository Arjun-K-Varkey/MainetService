package com.abm.mainet.common.integration.dms.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_DIGILOCKERCERTIFICATE_DATA_MAS")
public class DigilockerCertificateEntity {
	
	
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "DIGI_ID", nullable = false)
	private Long digiId;

    @Column(name = "CERTIFICATE_TYPE")
    private String certificateType;


    @Column(name = "Digilocker_Request_Json")
    private String digilockerRequestJson;


    @Column(name = "CREATED_DATE")
	private Date lmodDate;
    
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
   
	
	public String[] getPkValues() {
        return new String[] { "DGI", "TB_DIGILOCKERCERTIFICATE_DATA_MAS", "DIGI_ID" };
    }

	public Long getDigiId() {
		return digiId;
	}

	public void setDigiId(Long digiId) {
		this.digiId = digiId;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getDigilockerRequestJson() {
		return digilockerRequestJson;
	}

	public void setDigilockerRequestJson(String digilockerRequestJson) {
		this.digilockerRequestJson = digilockerRequestJson;
	}

	public Date getLmodDate() {
		return lmodDate;
	}

	public void setLmodDate(Date lmodDate) {
		this.lmodDate = lmodDate;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
    
    
}
