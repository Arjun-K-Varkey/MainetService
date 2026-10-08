package com.abm.mainet.common.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.integration.payment.dto.PaymentTransactionMasDTO;
import com.abm.mainet.common.ui.model.AbstractFormModel;

@Component
@Scope("session")
public class OnlineTransactionStatusModel extends AbstractFormModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<PaymentTransactionMasDTO> transactionMas = new ArrayList<>();

	public List<PaymentTransactionMasDTO> getTransactionMas() {
		return transactionMas;
	}

	public void setTransactionMas(List<PaymentTransactionMasDTO> transactionMas) {
		this.transactionMas = transactionMas;
	}

}
