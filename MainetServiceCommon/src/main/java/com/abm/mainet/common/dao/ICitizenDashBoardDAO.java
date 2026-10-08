package com.abm.mainet.common.dao;

import java.util.Date;
import java.util.List;

import com.abm.mainet.common.domain.CitizenDashboardView;
import com.abm.mainet.common.domain.CitizenDashboardViewEntity;
import com.abm.mainet.common.dto.CitizenDashBoardReqDTO;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;

public interface ICitizenDashBoardDAO {

    List<CitizenDashboardView> getAllApplicationsOfCitizen(CitizenDashBoardReqDTO request);
    
    List<Object[]> getAllFaliuredOrCancelledOnlineList(String mobNo, Long orgId);

	List<PaymentTransactionMas> getCitizenPayPendingDataByDateAndStatus(Date validDate, Date toDate, Long serviceId,
			String flatNo, Long orgId, String refNo, Long deptId);

	List<CitizenDashboardViewEntity> getAllApplicationsOfCitizenByNative(CitizenDashBoardReqDTO request);
}
