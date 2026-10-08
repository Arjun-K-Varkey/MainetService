package com.abm.mainet.common.integration.dms.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_SINGLEWINDOW_INTEGRATION")
public class SingleWindowIntegrationEntity {
	
	
	@Id
	@GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
	@GeneratedValue(generator = "MyCustomGenerator")
	@Column(name = "Sw_Id", nullable = false)
	private Long swId;

    @Column(name = "PropertyNoOrConnectionNo")
    private String propertyNoOrConnectionNo;
    
    @Column(name = "FlatNo", nullable=true)
    private String flatNo;    

    @Column(name = "userId", nullable=true)
    private String userId;

    @Column(name = "value", nullable=true)
    private String value;

    @Column(name = "SwiDate", nullable=true)
    private String swidate;

    @Column(name = "MacId", nullable=true)
    private String macId;

    @Column(name = "CREATED_DATE")
	private Date lmodDate;

    
    @Column(name = "UDF1", nullable=true)
    private String uDF1;

    @Column(name = "UDF2", nullable=true)
    private String uDF2;

    @Column(name = "UDF3", nullable=true)
    private String uDF3;

    @Column(name = "UDF4", nullable=true)
    private String uDF4;

    // ===== Getters and Setters =====
   
	public Long getSwId() {
		return swId;
	}

	public void setSwId(Long swId) {
		this.swId = swId;
	}

	public String getPropertyNoOrConnectionNo() {
		return propertyNoOrConnectionNo;
	}

	public void setPropertyNoOrConnectionNo(String propertyNoOrConnectionNo) {
		this.propertyNoOrConnectionNo = propertyNoOrConnectionNo;
	}
	
	
	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSwidate() {
		return swidate;
	}

	public void setSwidate(String swidate) {
		this.swidate = swidate;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	public Date getLmodDate() {
		return lmodDate;
	}

	public void setLmodDate(Date lmodDate) {
		this.lmodDate = lmodDate;
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
    
	public String[] getPkValues() {
        return new String[] { "SWI", "TB_SINGLEWINDOW_INTEGRATION", "Sw_Id" };
    }

    
}
