package com.abm.mainet.account.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.account.dto.AccountBillRegisterBean;
import com.abm.mainet.account.dto.PaymentEntryDto;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.LookUp;

@Component
@Scope("session")
public class DeductionRegisterModel extends AbstractFormModel {
    private static final long serialVersionUID = 1L;
    private PaymentEntryDto paymentEntryDto = new PaymentEntryDto();
    AccountBillRegisterBean AccountBillRegisterBean = new AccountBillRegisterBean();
    private List<LookUp> tdsLookUpList = new ArrayList<>();
    private Map<Long, String>  fieldMaster=new HashMap<>();

    private Long tdsTypeId;
    private Long sacHeadid;

    @Override
    public boolean saveForm() {
        return super.saveForm();
    }

    public List<LookUp> getTdsLookUpList() {
        return tdsLookUpList;
    }

    public void setTdsLookUpList(List<LookUp> tdsLookUpList) {
        this.tdsLookUpList = tdsLookUpList;
    }

    public Long getTdsTypeId() {
        return tdsTypeId;
    }

    public void setTdsTypeId(Long tdsTypeId) {
        this.tdsTypeId = tdsTypeId;
    }

    public Long getSacHeadid() {
        return sacHeadid;
    }

    public void setSacHeadid(Long sacHeadid) {
        this.sacHeadid = sacHeadid;
    }

    public PaymentEntryDto getPaymentEntryDto() {
        return paymentEntryDto;
    }

    public void setPaymentEntryDto(PaymentEntryDto paymentEntryDto) {
        this.paymentEntryDto = paymentEntryDto;
    }

	public Map<Long, String> getFieldMaster() {
		return fieldMaster;
	}

	public void setFieldMaster(Map<Long, String> fieldMaster) {
		this.fieldMaster = fieldMaster;
	}

}
