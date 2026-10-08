package com.abm.mainet.cfc.challan.service;

import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.utility.CallBackResponseObj;

/**
 *
 * @author Rahul.Yadav
 *
 */
public interface IPushToPayService {

	CommonChallanDTO callPushToPayAPIForPineLab(String seq, CommonChallanDTO offline) throws InterruptedException;

	CallBackResponseObj getPineLabPaymentStatus(PaymentTransactionMas rDTO);
	
	CommonChallanDTO callPushToPayAPIFoICICIVIZPAY(String seq, CommonChallanDTO offline) throws InterruptedException;
	
	CallBackResponseObj getICICIVIZPAYPaymentStatus(PaymentTransactionMas rDTO);
}
