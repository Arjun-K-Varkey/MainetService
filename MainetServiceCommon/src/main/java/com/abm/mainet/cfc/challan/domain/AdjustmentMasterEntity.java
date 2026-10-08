package com.abm.mainet.cfc.challan.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_ADJUSTMENT_MAS")
public class AdjustmentMasterEntity implements Serializable {

    private static final long serialVersionUID = 2433297791472674452L;

    @Id
    @GenericGenerator(name = "MyCustomGenerator", strategy = "com.abm.mainet.common.utility.SequenceIdGenerator")
    @GeneratedValue(generator = "MyCustomGenerator")
    @Column(name = "adj_id", precision = 12, scale = 0, nullable = false)
    private long adjId;

    @Column(name = "dp_deptid", nullable = false, updatable = false)
    private Long dpDeptId;

    @Column(name = "adj_ref_no", nullable = true)
    private String adjRefNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "adj_date", nullable = true)
    private Date adjDate;

    @Column(name = "adj_type", nullable = true)
    private String adjType;

    @Column(name = "ORGID", nullable = false, updatable = false)
    private Long orgId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "LG_IP_MAC", length = 100)
    private String lgIpMac;

    @Column(name = "LG_IP_MAC_UPD", length = 100)
    private String lgIpMacUpd;
    
    @Column(name = "flat_no", length = 50)
    private String flatNo;
    
    @Column(name = "adj_advnce_amnt_flag")
    private String adjAdvnceAmntFlag;

    @Column(name = "APPLICATION_ID")
    private Long applicationId;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "adjId", cascade = CascadeType.ALL)
    private List<AdjustmentDetailEntity> adjDetail = new ArrayList<>(0);

    @Column(name = "OLD_FLAT_NO", length = 50)
    private String oldFlatNo;

    @Column(name = "OLD_FLAT_NO_UPDATE_DATE")
    private Date oldFlatNoUpdateDate;
    
    public String[] getPkValues() {
        return new String[] { "COM", "TB_ADJUSTMENT_MAS", "ADJ_ID" };
    }

    public long getAdjId() {
        return adjId;
    }

    public void setAdjId(final long adjId) {
        this.adjId = adjId;
    }

    public Long getDpDeptId() {
        return dpDeptId;
    }

    public void setDpDeptId(final Long dpDeptId) {
        this.dpDeptId = dpDeptId;
    }

    public String getAdjRefNo() {
        return adjRefNo;
    }

    public void setAdjRefNo(final String adjRefNo) {
        this.adjRefNo = adjRefNo;
    }

    public Date getAdjDate() {
        return adjDate;
    }

    public void setAdjDate(final Date adjDate) {
        this.adjDate = adjDate;
    }

    public String getAdjType() {
        return adjType;
    }

    public void setAdjType(final String adjType) {
        this.adjType = adjType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(final Long orgId) {
        this.orgId = orgId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(final Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLgIpMac() {
        return lgIpMac;
    }

    public void setLgIpMac(final String lgIpMac) {
        this.lgIpMac = lgIpMac;
    }

    public String getLgIpMacUpd() {
        return lgIpMacUpd;
    }

    public void setLgIpMacUpd(final String lgIpMacUpd) {
        this.lgIpMacUpd = lgIpMacUpd;
    }

    public List<AdjustmentDetailEntity> getAdjDetail() {
        return adjDetail;
    }

    public void setAdjDetail(final List<AdjustmentDetailEntity> adjDetail) {
        this.adjDetail = adjDetail;
    }
    

    public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}
	

	public String getAdjAdvnceAmntFlag() {
		return adjAdvnceAmntFlag;
	}

	public void setAdjAdvnceAmntFlag(String adjAdvnceAmntFlag) {
		this.adjAdvnceAmntFlag = adjAdvnceAmntFlag;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public String getOldFlatNo() {
		return oldFlatNo;
	}

	public void setOldFlatNo(String oldFlatNo) {
		this.oldFlatNo = oldFlatNo;
	}

	public Date getOldFlatNoUpdateDate() {
		return oldFlatNoUpdateDate;
	}

	public void setOldFlatNoUpdateDate(Date oldFlatNoUpdateDate) {
		this.oldFlatNoUpdateDate = oldFlatNoUpdateDate;
	}

	@Override
	public String toString() {
		return "AdjustmentMasterEntity [adjId=" + adjId + ", dpDeptId=" + dpDeptId + ", adjRefNo=" + adjRefNo
				+ ", adjDate=" + adjDate + ", adjType=" + adjType + ", orgId=" + orgId + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ ", lgIpMac=" + lgIpMac + ", lgIpMacUpd=" + lgIpMacUpd + ", flatNo=" + flatNo + ", adjAdvnceAmntFlag="
				+ adjAdvnceAmntFlag + ", applicationId=" + applicationId + ", adjDetail=" + adjDetail + ", oldFlatNo="
				+ oldFlatNo + ", oldFlatNoUpdateDate=" + oldFlatNoUpdateDate + "]";
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((adjDate == null) ? 0 : adjDate.hashCode());
        result = (prime * result) + ((adjDetail == null) ? 0 : adjDetail.hashCode());
        result = (prime * result) + (int) (adjId ^ (adjId >>> 32));
        result = (prime * result) + ((adjRefNo == null) ? 0 : adjRefNo.hashCode());
        result = (prime * result) + ((adjType == null) ? 0 : adjType.hashCode());
        result = (prime * result) + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = (prime * result) + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = (prime * result) + ((dpDeptId == null) ? 0 : dpDeptId.hashCode());
        result = (prime * result) + ((lgIpMac == null) ? 0 : lgIpMac.hashCode());
        result = (prime * result) + ((lgIpMacUpd == null) ? 0 : lgIpMacUpd.hashCode());
        result = (prime * result) + ((orgId == null) ? 0 : orgId.hashCode());
        result = (prime * result) + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        result = (prime * result) + ((updatedDate == null) ? 0 : updatedDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdjustmentMasterEntity other = (AdjustmentMasterEntity) obj;
        if (adjDate == null) {
            if (other.adjDate != null) {
                return false;
            }
        } else if (!adjDate.equals(other.adjDate)) {
            return false;
        }
        if (adjDetail == null) {
            if (other.adjDetail != null) {
                return false;
            }
        } else if (!adjDetail.equals(other.adjDetail)) {
            return false;
        }
        if (adjId != other.adjId) {
            return false;
        }
        if (adjRefNo == null) {
            if (other.adjRefNo != null) {
                return false;
            }
        } else if (!adjRefNo.equals(other.adjRefNo)) {
            return false;
        }
        if (adjType == null) {
            if (other.adjType != null) {
                return false;
            }
        } else if (!adjType.equals(other.adjType)) {
            return false;
        }
        if (createdBy == null) {
            if (other.createdBy != null) {
                return false;
            }
        } else if (!createdBy.equals(other.createdBy)) {
            return false;
        }
        if (createdDate == null) {
            if (other.createdDate != null) {
                return false;
            }
        } else if (!createdDate.equals(other.createdDate)) {
            return false;
        }
        if (dpDeptId == null) {
            if (other.dpDeptId != null) {
                return false;
            }
        } else if (!dpDeptId.equals(other.dpDeptId)) {
            return false;
        }
        if (lgIpMac == null) {
            if (other.lgIpMac != null) {
                return false;
            }
        } else if (!lgIpMac.equals(other.lgIpMac)) {
            return false;
        }
        if (lgIpMacUpd == null) {
            if (other.lgIpMacUpd != null) {
                return false;
            }
        } else if (!lgIpMacUpd.equals(other.lgIpMacUpd)) {
            return false;
        }
        if (orgId == null) {
            if (other.orgId != null) {
                return false;
            }
        } else if (!orgId.equals(other.orgId)) {
            return false;
        }
        if (updatedBy == null) {
            if (other.updatedBy != null) {
                return false;
            }
        } else if (!updatedBy.equals(other.updatedBy)) {
            return false;
        }
        if (updatedDate == null) {
            if (other.updatedDate != null) {
                return false;
            }
        } else if (!updatedDate.equals(other.updatedDate)) {
            return false;
        }
        return true;
    }

}
