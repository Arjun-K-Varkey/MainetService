package com.abm.mainet.account.service;

import java.util.List;

public interface AccountBillRegisterService {

    /**
     * @param fieldId 
     * @param accountHead 
     * @param billType 
     * @param FromDate
     * @param ToDate
     * @return
     */
    List<Object[]> getBillRegisterDetails(Long orgId, String fromDate, String toDate, Long accountHead, Long fieldId, Long billType);




	List<Object[]> getOutStandingBillRegisterDetails(Long orgId, String fromdate, Long dpDeptid, Long accountHeadId,
			String allHeads, Long fieldId, Long billType);

}
