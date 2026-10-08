package com.abm.mainet.common.master.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abm.mainet.common.integration.dto.ChargeDetailDTO;

/**
 * @author apurva.salgaonkar
 *
 */
public class ContractAgreementSummaryDTO implements Serializable {

    private static final long serialVersionUID = -1045129310751501377L;
    private long contId;
    private String contNo;
    private String contDate;
    private String contDept;
    private String contFromDate;
    private String contToDate;
    private String contp1Name;
    private String contp2Name;
    private String address;
    private String emailId;
    private String mobileNo;
    private BigDecimal contAmount;
    private String contTndNo;
    private String contLoaNo;
    private BigDecimal balanceAmount;
    private BigDecimal overdueAmount;
    private Long orgId;
    private String status;
    private String errorMsg;
    private Double inputAmount;
    private Long serviceId;
    private Long deptId;
    private Double billBalanceAmount;
    private String contDeptSc;
    private BigDecimal arrearsAmt;
    private BigDecimal sumOfCurrentAmt;
    private BigDecimal arrearsAndCurrentAmt;
    private BigDecimal oldGstAndPeneltyArrersAmt;
    // Tax List
    List<ChargeDetailDTO> chargeList;
    private List<Object[]> propertyDetails = new ArrayList<Object[]>();
   
    private Double arrearsSgstCgstTotalAmt;
    private Double currentSgstCgstTotalAmt;
    private Double totalArrCurrAmt;
    private Double  arrearCgstAmt;
    private Double  arrearSgstAmt;
    private Double  currentSgstAmt;
    private Double  currentCgstAmt;
    
    private Double  totalArrCurrSgstAmt;
    private Double  totalArrcurrCgstAmt;
    private String estateName;
    private String propertyNo;
    private BigDecimal penalty;
    private List<Object[]> estateDetails = new ArrayList<Object[]>();
    private List<Object[]> objPropertyDetList = new ArrayList<Object[]>();
    private List<Object[]> objEstateDetList = new ArrayList<Object[]>();
    private String usageId;
    private List<String> demandYearList = new ArrayList<>();
    
    private Double arrearReceivedAmount;
    private String vendorGstNo;
    private String propName;
    private BigDecimal arrearsCurrentAndPenaltyAmt;
    private Map<Long, Double> details_1 = new HashMap<>(0);
    private BigDecimal sumOldGstPenaltyAndArrAmt;
    private BigDecimal currAmtAndChequeDishCharges;
    private List<Object[]> objContractDetList = new ArrayList<Object[]>();
    private BigDecimal amountOnTds;
    private Double tdsAmount;
    private double arrTdsAmt;
    private double totalArrCurrTdsAmt;
    
    
    
    public BigDecimal getSumOldGstPenaltyAndArrAmt() {
		return sumOldGstPenaltyAndArrAmt;
	}

	public void setSumOldGstPenaltyAndArrAmt(BigDecimal sumOldGstPenaltyAndArrAmt) {
		this.sumOldGstPenaltyAndArrAmt = sumOldGstPenaltyAndArrAmt;
	}

	public Map<Long, Double> getDetails_1() {
		return details_1;
	}

	public void setDetails_1(Map<Long, Double> details_1) {
		this.details_1 = details_1;
	}

	public Double getArrearReceivedAmount() {
		return arrearReceivedAmount;
	}

	public void setArrearReceivedAmount(Double arrearReceivedAmount) {
		this.arrearReceivedAmount = arrearReceivedAmount;
	}

	public String getVendorGstNo() {
		return vendorGstNo;
	}

	public void setVendorGstNo(String vendorGstNo) {
		this.vendorGstNo = vendorGstNo;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public BigDecimal getArrearsCurrentAndPenaltyAmt() {
		return arrearsCurrentAndPenaltyAmt;
	}

	public void setArrearsCurrentAndPenaltyAmt(BigDecimal arrearsCurrentAndPenaltyAmt) {
		this.arrearsCurrentAndPenaltyAmt = arrearsCurrentAndPenaltyAmt;
	}

	public List<String> getDemandYearList() {
		return demandYearList;
	}

	public void setDemandYearList(List<String> demandYearList) {
		this.demandYearList = demandYearList;
	}

	public String getUsageId() {
		return usageId;
	}

	public void setUsageId(String usageId) {
		this.usageId = usageId;
	}

	
	public List<Object[]> getObjPropertyDetList() {
		return objPropertyDetList;
	}

	public void setObjPropertyDetList(List<Object[]> objPropertyDetList) {
		this.objPropertyDetList = objPropertyDetList;
	}

	public List<Object[]> getObjEstateDetList() {
		return objEstateDetList;
	}

	public void setObjEstateDetList(List<Object[]> objEstateDetList) {
		this.objEstateDetList = objEstateDetList;
	}

    public List<Object[]> getEstateDetails() {
		return estateDetails;
	}

	public void setEstateDetails(List<Object[]> estateDetails) {
		this.estateDetails = estateDetails;
	}

	public BigDecimal getPenalty() {
		return penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	public String getEstateName() {
		return estateName;
	}

	public void setEstateName(String estateName) {
		this.estateName = estateName;
	}

	public String getPropertyNo() {
		return propertyNo;
	}

	public void setPropertyNo(String propertyNo) {
		this.propertyNo = propertyNo;
	}

	public String getContLoaNo() {
        return contLoaNo;
    }

    public void setContLoaNo(String contLoaNo) {
        this.contLoaNo = contLoaNo;
    }

    public String getContTndNo() {
        return contTndNo;
    }

    public void setContTndNo(String contTndNo) {
        this.contTndNo = contTndNo;
    }

    public String getContFromDate() {
        return contFromDate;
    }

    public void setContFromDate(final String contFromDate) {
        this.contFromDate = contFromDate;
    }

    public String getContToDate() {
        return contToDate;
    }

    public void setContToDate(final String contToDate) {
        this.contToDate = contToDate;
    }

    public long getContId() {
        return contId;
    }

    public void setContId(final long contId) {
        this.contId = contId;
    }

    public String getContNo() {
        return contNo;
    }

    public void setContNo(final String contNo) {
        this.contNo = contNo;
    }

    public String getContp1Name() {
        return contp1Name;
    }

    public void setContp1Name(final String contp1Name) {
        this.contp1Name = contp1Name;
    }

    public String getContp2Name() {
        return contp2Name;
    }

    public void setContp2Name(final String contp2Name) {
        this.contp2Name = contp2Name;
    }

    public String getContDept() {
        return contDept;
    }

    public void setContDept(final String contDept) {
        this.contDept = contDept;
    }

    public String getContDate() {
        return contDate;
    }

    public void setContDate(final String contDate) {
        this.contDate = contDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BigDecimal getContAmount() {
        return contAmount;
    }

    public void setContAmount(BigDecimal contAmount) {
        this.contAmount = contAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<ChargeDetailDTO> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<ChargeDetailDTO> chargeList) {
        this.chargeList = chargeList;
    }

	public List<Object[]> getPropertyDetails() {
		return propertyDetails;
	}

	public void setPropertyDetails(List<Object[]> propertyDetails) {
		this.propertyDetails = propertyDetails;
	}

	public Double getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(Double inputAmount) {
		this.inputAmount = inputAmount;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Double getBillBalanceAmount() {
		return billBalanceAmount;
	}

	public void setBillBalanceAmount(Double billBalanceAmount) {
		this.billBalanceAmount = billBalanceAmount;
	}

	public String getContDeptSc() {
		return contDeptSc;
	}

	public void setContDeptSc(String contDeptSc) {
		this.contDeptSc = contDeptSc;
	}

	public BigDecimal getArrearsAmt() {
		return arrearsAmt;
	}

	public void setArrearsAmt(BigDecimal arrearsAmt) {
		this.arrearsAmt = arrearsAmt;
	}

	public BigDecimal getSumOfCurrentAmt() {
		return sumOfCurrentAmt;
	}

	public void setSumOfCurrentAmt(BigDecimal sumOfCurrentAmt) {
		this.sumOfCurrentAmt = sumOfCurrentAmt;
	}

	public BigDecimal getArrearsAndCurrentAmt() {
		return arrearsAndCurrentAmt;
	}

	public void setArrearsAndCurrentAmt(BigDecimal arrearsAndCurrentAmt) {
		this.arrearsAndCurrentAmt = arrearsAndCurrentAmt;
	}

	
	public Double getArrearsSgstCgstTotalAmt() {
		return arrearsSgstCgstTotalAmt;
	}

	public void setArrearsSgstCgstTotalAmt(Double arrearsSgstCgstTotalAmt) {
		this.arrearsSgstCgstTotalAmt = arrearsSgstCgstTotalAmt;
	}

	public Double getCurrentSgstCgstTotalAmt() {
		return currentSgstCgstTotalAmt;
	}

	public void setCurrentSgstCgstTotalAmt(Double currentSgstCgstTotalAmt) {
		this.currentSgstCgstTotalAmt = currentSgstCgstTotalAmt;
	}

	public Double getTotalArrCurrAmt() {
		return totalArrCurrAmt;
	}

	public void setTotalArrCurrAmt(Double totalArrCurrAmt) {
		this.totalArrCurrAmt = totalArrCurrAmt;
	}

	public Double getArrearCgstAmt() {
		return arrearCgstAmt;
	}

	public void setArrearCgstAmt(Double arrearCgstAmt) {
		this.arrearCgstAmt = arrearCgstAmt;
	}

	public Double getArrearSgstAmt() {
		return arrearSgstAmt;
	}

	public void setArrearSgstAmt(Double arrearSgstAmt) {
		this.arrearSgstAmt = arrearSgstAmt;
	}

	public Double getCurrentSgstAmt() {
		return currentSgstAmt;
	}

	public void setCurrentSgstAmt(Double currentSgstAmt) {
		this.currentSgstAmt = currentSgstAmt;
	}

	public Double getCurrentCgstAmt() {
		return currentCgstAmt;
	}

	public void setCurrentCgstAmt(Double currentCgstAmt) {
		this.currentCgstAmt = currentCgstAmt;
	}

	public Double getTotalArrCurrSgstAmt() {
		return totalArrCurrSgstAmt;
	}

	public void setTotalArrCurrSgstAmt(Double totalArrCurrSgstAmt) {
		this.totalArrCurrSgstAmt = totalArrCurrSgstAmt;
	}

	public Double getTotalArrcurrCgstAmt() {
		return totalArrcurrCgstAmt;
	}

	public void setTotalArrcurrCgstAmt(Double totalArrcurrCgstAmt) {
		this.totalArrcurrCgstAmt = totalArrcurrCgstAmt;
	}

	public BigDecimal getOldGstAndPeneltyArrersAmt() {
		return oldGstAndPeneltyArrersAmt;
	}

	public void setOldGstAndPeneltyArrersAmt(BigDecimal oldGstAndPeneltyArrersAmt) {
		this.oldGstAndPeneltyArrersAmt = oldGstAndPeneltyArrersAmt;
	}

	public BigDecimal getCurrAmtAndChequeDishCharges() {
		return currAmtAndChequeDishCharges;
	}

	public void setCurrAmtAndChequeDishCharges(BigDecimal currAmtAndChequeDishCharges) {
		this.currAmtAndChequeDishCharges = currAmtAndChequeDishCharges;
	}

	public List<Object[]> getObjContractDetList() {
		return objContractDetList;
	}

	public void setObjContractDetList(List<Object[]> objContractDetList) {
		this.objContractDetList = objContractDetList;
	}

	public BigDecimal getAmountOnTds() {
		return amountOnTds;
	}

	public void setAmountOnTds(BigDecimal amountOnTds) {
		this.amountOnTds = amountOnTds;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public double getArrTdsAmt() {
		return arrTdsAmt;
	}

	public void setArrTdsAmt(double arrTdsAmt) {
		this.arrTdsAmt = arrTdsAmt;
	}

	public double getTotalArrCurrTdsAmt() {
		return totalArrCurrTdsAmt;
	}

	public void setTotalArrCurrTdsAmt(double totalArrCurrTdsAmt) {
		this.totalArrCurrTdsAmt = totalArrCurrTdsAmt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
	

}
