package com.abm.mainet.account.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.account.dao.AccountBillEntryDao;
import com.abm.mainet.account.repository.BillEntryRepository;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.utility.Utility;

@Service
public class AccountBillServiceImpl implements AccountBillRegisterService {

    @Resource
    private BillEntryRepository billEntryServiceJpaRepository;
    
    @Autowired
    private AccountBillEntryDao accountBillEntryDao;

    @Transactional(readOnly = true)
    @Override
    public List<Object[]> getBillRegisterDetails(Long orgId, String fromDate, String toDate,Long accountHeadId,Long fieldId,Long billType) {

        Date fromDates = Utility.stringToDate(fromDate);

        Date toDates = Utility.stringToDate(toDate);

        return accountBillEntryDao.getBillRegisterDetails(orgId, fromDates, toDates,accountHeadId,fieldId,billType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object[]> getOutStandingBillRegisterDetails(Long orgId, String fromdate, Long dpDeptid,Long accountHeadId,String allHeads,Long fieldId,Long billType) {
        
        	 return accountBillEntryDao.getOutStandingBillRegisterDetailsnNewWithAccountHead(orgId, Utility.stringToDate(fromdate), dpDeptid,accountHeadId,fieldId,billType); 	 
    }

}
