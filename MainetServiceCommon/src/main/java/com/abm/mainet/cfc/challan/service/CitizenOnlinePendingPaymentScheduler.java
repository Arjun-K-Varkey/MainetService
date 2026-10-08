package com.abm.mainet.cfc.challan.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.abm.mainet.cfc.challan.ui.model.CitizenOnlinePendingPayment;
import com.abm.mainet.common.repository.CitizenDashBoardRepository;
import com.abm.mainet.common.service.CitizenDashBoardServiceImpl;

public interface CitizenOnlinePendingPaymentScheduler {


	

	void runOnlinePendingPaymentDate(CitizenOnlinePendingPayment model);
		
	

}
