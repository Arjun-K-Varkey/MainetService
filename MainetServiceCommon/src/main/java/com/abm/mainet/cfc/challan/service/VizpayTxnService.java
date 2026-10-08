package com.abm.mainet.cfc.challan.service;

import java.util.Date;

import com.abm.mainet.cfc.loi.repository.PaymentTransactionMasRepository;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.payment.dto.PaymentRequestDTO;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.utility.Utility;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author AnwarulHasan
 * @since 17/03/2026
 */

@Service
public class VizpayTxnService {

	@Autowired
	private PaymentTransactionMasRepository paymentTransactionMasRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaymentTransactionMas savePosTransactionMaster(PaymentRequestDTO payURequestDTO)
			throws FrameworkException {


        final PaymentTransactionMas paymentTransactionMas = new PaymentTransactionMas();
        paymentTransactionMas.setLmodDate(new Date());
        paymentTransactionMas.setLgIpMac(Utility.getMacAddress());
        paymentTransactionMas.setOrgId(payURequestDTO.getOrgId());
        paymentTransactionMas.setUserId(payURequestDTO.getEmpId());
        paymentTransactionMas.setReferenceDate(new Date());
        paymentTransactionMas.setSmServiceId(payURequestDTO.getServiceId());
        paymentTransactionMas.setSendKey(MainetConstants.BankParam.KEY);
        paymentTransactionMas.setSendAmount(payURequestDTO.getDueAmt());
        paymentTransactionMas.setSendProductinfo(payURequestDTO.getServiceName());
        paymentTransactionMas.setSendFirstname(payURequestDTO.getApplicantName());
        paymentTransactionMas.setSendEmail(payURequestDTO.getEmail());
        paymentTransactionMas.setSendPhone(payURequestDTO.getMobNo());
        paymentTransactionMas.setSendSurl(payURequestDTO.getCancelUrl());
        paymentTransactionMas.setSendFurl(payURequestDTO.getFailUrl());
        paymentTransactionMas.setSendSalt(MainetConstants.BankParam.SALT);
        paymentTransactionMas.setSendHash(MainetConstants.BankParam.HASH);
        paymentTransactionMas.setPgType(MainetConstants.BankParam.PG);
        paymentTransactionMas.setPgSource(payURequestDTO.getPgName());
        paymentTransactionMas.setReferenceId(payURequestDTO.getUdf2());
        if(StringUtils.isNotBlank(payURequestDTO.getUdf4()))
        	paymentTransactionMas.setFlatNo(payURequestDTO.getUdf4());
        paymentTransactionMas.setSendUrl(MainetConstants.BankParam.SURL);
        paymentTransactionMas.setRecvStatus(MainetConstants.PAYU_STATUS.PENDING);
        if(isValidFeeIds(payURequestDTO.getFeeIds()))
        paymentTransactionMas.setFeeIds(payURequestDTO.getFeeIds());
        paymentTransactionMas.setChallanServiceType(payURequestDTO.getChallanServiceType());
        paymentTransactionMas.setDocumentUploaded(payURequestDTO.getDocumentUploaded());
        paymentTransactionMas.setRecvBankRefNum(payURequestDTO.getOrderId());
        if(payURequestDTO.getLangId()!=null)
        paymentTransactionMas.setLangId(payURequestDTO.getLangId());
        else
        	paymentTransactionMas.setLangId(1);	

        if(StringUtils.isEmpty(paymentTransactionMas.getFlatNo())) {
        	paymentTransactionMas.setFlatNo(payURequestDTO.getFlatNo());
        }
        paymentTransactionMas.setFieldId(payURequestDTO.getFieldId());
        if(StringUtils.isNotBlank(payURequestDTO.getAddField7())) {
        	paymentTransactionMas.settDesc(payURequestDTO.getAddField7());//DeviceId for ICICI(VIZPAY)
        }
        paymentTransactionMasRepository.saveAndFlush(paymentTransactionMas);
        payURequestDTO.setTxnid(paymentTransactionMas.getTranCmId());
        return paymentTransactionMas;
    
	}
	
	private boolean isValidFeeIds(String regex) {
		if (StringUtils.isNotBlank(regex)) {
			
			if (regex.matches(".*[0-9].*")) {
				return true;
			}
			return false;
		}
		return false;
	}
	
}