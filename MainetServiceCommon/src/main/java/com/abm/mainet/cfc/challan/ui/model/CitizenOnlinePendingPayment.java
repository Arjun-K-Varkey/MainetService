package com.abm.mainet.cfc.challan.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.domain.Department;
import com.abm.mainet.common.domain.ScrutinyServiceDto;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.ui.model.AbstractFormModel;

@Component
@Scope(value = "session")
public class CitizenOnlinePendingPayment extends AbstractFormModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2836338737448006287L;
	private List<ScrutinyServiceDto> serviceList = new ArrayList<ScrutinyServiceDto>();
	private List<PaymentTransactionMas> payMasList = new ArrayList<PaymentTransactionMas>();
	private List<Department>  deptList=new ArrayList<Department>();
	public List<ScrutinyServiceDto> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ScrutinyServiceDto> serviceList) {
		this.serviceList = serviceList;
	}

	public List<PaymentTransactionMas> getPayMasList() {
		return payMasList;
	}

	public void setPayMasList(List<PaymentTransactionMas> payMasList) {
		this.payMasList = payMasList;
	}

	public List<Department> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Department> deptList) {
		this.deptList = deptList;
	}
}
