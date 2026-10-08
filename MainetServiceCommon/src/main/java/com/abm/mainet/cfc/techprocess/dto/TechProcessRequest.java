package com.abm.mainet.cfc.techprocess.dto;

public class TechProcessRequest {
	private IdentifierRequest merchant = new IdentifierRequest();
	private TransactionRequest transaction = new TransactionRequest();

	public IdentifierRequest getMerchant() {
		return merchant;
	}

	public void setMerchant(IdentifierRequest merchant) {
		this.merchant = merchant;
	}

	public TransactionRequest getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionRequest transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "TechProcessRequest [merchant=" + merchant + ", transaction=" + transaction + "]";
	}
}
