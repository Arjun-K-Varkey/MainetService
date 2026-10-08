/**
 * 
 */
package com.abm.mainet.bill.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anwarul.Hassan
 *
 * @since 28-Apr-2020
 */
public class PropertyBillGenerationMap {

    private Map<Long, BillGenErrorMapDTO> errorListMap = new ConcurrentHashMap<>();
    private Long noOfBillsForGeneration;
    private Long noOfBillsSelectedForGeneration;
    private Long noOfBillsPending;
    private Long noOfBillsGeneratedSuccessfull;
    private Long noOfBillsGotErrors;
    private Long approximatelyTakenTimeForBillGeneration;
    private Long orgId;
    private Long independentTaxFlats;
    private Long dependentTaxFlats;
    private Long totalFlats;

    public Map<Long, BillGenErrorMapDTO> getErrorListMap() {
        return errorListMap;
    }

    public void setErrorListMap(Map<Long, BillGenErrorMapDTO> errorListMap) {
        this.errorListMap = errorListMap;
    }

    public Long getNoOfBillsForGeneration() {
        return noOfBillsForGeneration;
    }

    public void setNoOfBillsForGeneration(Long noOfBillsForGeneration) {
        this.noOfBillsForGeneration = noOfBillsForGeneration;
    }

    public Long getNoOfBillsSelectedForGeneration() {
        return noOfBillsSelectedForGeneration;
    }

    public void setNoOfBillsSelectedForGeneration(Long noOfBillsSelectedForGeneration) {
        this.noOfBillsSelectedForGeneration = noOfBillsSelectedForGeneration;
    }

    public Long getNoOfBillsPending() {
        return noOfBillsPending;
    }

    public void setNoOfBillsPending(Long noOfBillsPending) {
        this.noOfBillsPending = noOfBillsPending;
    }

    public Long getNoOfBillsGeneratedSuccessfull() {
        return noOfBillsGeneratedSuccessfull;
    }

    public void setNoOfBillsGeneratedSuccessfull(Long noOfBillsGeneratedSuccessfull) {
        this.noOfBillsGeneratedSuccessfull = noOfBillsGeneratedSuccessfull;
    }

    public Long getNoOfBillsGotErrors() {
        return noOfBillsGotErrors;
    }

    public void setNoOfBillsGotErrors(Long noOfBillsGotErrors) {
        this.noOfBillsGotErrors = noOfBillsGotErrors;
    }

    public Long getApproximatelyTakenTimeForBillGeneration() {
        return approximatelyTakenTimeForBillGeneration;
    }

    public void setApproximatelyTakenTimeForBillGeneration(Long approximatelyTakenTimeForBillGeneration) {
        this.approximatelyTakenTimeForBillGeneration = approximatelyTakenTimeForBillGeneration;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

	public Long getIndependentTaxFlats() {
		return independentTaxFlats;
	}

	public void setIndependentTaxFlats(Long independentTaxFlats) {
		this.independentTaxFlats = independentTaxFlats;
	}

	public Long getDependentTaxFlats() {
		return dependentTaxFlats;
	}

	public void setDependentTaxFlats(Long dependentTaxFlats) {
		this.dependentTaxFlats = dependentTaxFlats;
	}

	public Long getTotalFlats() {
		return totalFlats;
	}

	public void setTotalFlats(Long totalFlats) {
		this.totalFlats = totalFlats;
	}
    
}
