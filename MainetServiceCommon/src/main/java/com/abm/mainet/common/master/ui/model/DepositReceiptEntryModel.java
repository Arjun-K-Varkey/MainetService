package com.abm.mainet.common.master.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.integration.acccount.dto.TbServiceReceiptMasBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptFeesDetBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptModesDetBean;
import com.abm.mainet.common.master.service.IDepositReceiptService;
import com.abm.mainet.common.service.IReceiptEntryService;
import com.abm.mainet.common.service.TbBankmasterService;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

/**
 * @author Saiprasad.Vengurlekar
 * @modified By Vishwajeet.kumar
 */

@Component
@Scope("session")
public class DepositReceiptEntryModel extends AbstractFormModel {

    private static final long serialVersionUID = 492644288141757589L;

    @Autowired
    private IDepositReceiptService depositReceiptService;
    
    @Autowired
	IReceiptEntryService receiptEntryService;
    
	@Resource
	private TbBankmasterService tbbankAccountService;

    private List<TbServiceReceiptMasBean> receiptBeanList = new ArrayList<>();
    private List<TbServiceReceiptMasBean> receiptMasBeanList = new ArrayList<>();
    private String saveMode;
    private String envFlag;
    private String counterScheduleFlag;

    public TbServiceReceiptMasBean prepareAndSaveReceiptEntity(TbServiceReceiptMasBean receiptBean) {
        Long empId = UserSession.getCurrent().getEmployee().getEmpId();
        CommonChallanDTO requestDto = new CommonChallanDTO();
        Map<Long, Double> detValues = new HashMap<>(0);
        requestDto.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
        requestDto.setDeptId(receiptBean.getDpDeptId());
        requestDto.setLoggedLocId(UserSession.getCurrent().getLoggedLocId());
        requestDto.setAmountToPay(receiptBean.getRmAmount());
        requestDto.setApplicantName(receiptBean.getRmReceivedfrom());
        requestDto.setNarration(receiptBean.getRmNarration());
        requestDto.setUniquePrimaryId(receiptBean.getAdditionalRefNo());
        requestDto.setReceiptcategoryId(receiptBean.getRecCategoryTypeId());
        requestDto.setUserId(empId);
        requestDto.setManualReceiptNo(receiptBean.getManualReceiptNo());
        requestDto.setVendorId(receiptBean.getVmVendorId());
        requestDto.setManualReeiptDate(Utility.stringToDate(receiptBean.getTransactionDate()));
        requestDto.setPaymentCategory(CommonMasterUtility.findLookUpCode(MainetConstants.BILL_MASTER_COMMON.PREFIX_CODE_RV,
                UserSession.getCurrent().getOrganisation().getOrgid(), receiptBean.getRecCategoryTypeId()));
        requestDto.setChallanServiceType(MainetConstants.FlagN);
        requestDto.setEmailId(receiptBean.getEmailId());
        requestDto.setMobileNumber(receiptBean.getMobileNumber());
        TbSrcptModesDetBean receiptModeDet = receiptBean.getReceiptModeDetailList();
        requestDto.setPayModeIn(receiptModeDet.getCpdFeemode());
        requestDto.setCbBankId(receiptModeDet.getCbBankid());
        requestDto.setBmChqDDNo(receiptModeDet.getRdChequeddno());
        requestDto.setTranRefDate(Utility.stringToDate(receiptModeDet.getTranRefDatetemp()));
        requestDto.setTranRefNumber(receiptModeDet.getTranRefNumber());
        if((StringUtils.isNotBlank(receiptModeDet.getTranRefNumber())&& StringUtils.isNumeric(receiptModeDet.getTranRefNumber())))
        	requestDto.setBmChqDDNo(receiptModeDet.getTranRefNumber());
        requestDto.setBmChqDDDate(Utility.stringToDate(receiptModeDet.getRdChequedddatetemp()));
        requestDto.setBmDrawOn(receiptModeDet.getRdDrawnon());
        requestDto.setBankaAccId(receiptModeDet.getBaAccountid());
        requestDto.setLgIpMac(UserSession.getCurrent().getEmployee().getEmppiservername());
        try {
			LookUp lkp=CommonMasterUtility.getNonHierarchicalLookUpObject(receiptModeDet.getCpdFeemode());
			if(lkp!=null && (lkp.getLookUpCode()!=null && (lkp.getLookUpCode().equalsIgnoreCase(MainetConstants.FlagB)||lkp.getLookUpCode().equalsIgnoreCase(MainetConstants.MENU.POS)||lkp.getLookUpCode().equalsIgnoreCase(MainetConstants.PAYMODE.WEB)))) {
				List<Object[]> objList=tbbankAccountService.getBankAccountPayment(requestDto.getOrgId(), receiptModeDet.getBaAccountid());
				if(CollectionUtils.isNotEmpty(objList)&&objList.get(0)[6]!=null) {
					requestDto.setCbBankId(Long.valueOf(objList.get(0)[6].toString()));
					
				}
			}}
			catch (Exception e) {
				
			}
       // #149536
        if (receiptModeDet.getAccountNumber() != null)
        requestDto.setBmBankAccountId(receiptModeDet.getAccountNumber());
        if (receiptBean.getReceiptFeeDetail() != null) {
            for (TbSrcptFeesDetBean feesDetBean : receiptBean.getReceiptFeeDetail()) {
                detValues.put(feesDetBean.getSacHeadId(), feesDetBean.getRfFeeamount().doubleValue());
            }
            requestDto.setFeeIds(detValues);
        }
        TbServiceReceiptMasBean tbServiceReceiptMasBean = depositReceiptService.saveReceiptEntry(requestDto);
        if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
         if (tbServiceReceiptMasBean.getRmRcptno() != null && tbServiceReceiptMasBean.getDpDeptId() != null)
           tbServiceReceiptMasBean.setRmReceiptNo(receiptEntryService.getCustomReceiptNo(tbServiceReceiptMasBean.getDpDeptId(),Long.valueOf(tbServiceReceiptMasBean.getRmRcptno())));
        }
        else {
        	if (tbServiceReceiptMasBean.getRmRcptno() != null) 
        	tbServiceReceiptMasBean.setRmReceiptNo(tbServiceReceiptMasBean.getRmRcptno().toString());
        }
        return tbServiceReceiptMasBean;
    }

    public List<TbServiceReceiptMasBean> getReceiptBeanList() {
        return receiptBeanList;
    }

    public void setReceiptBeanList(List<TbServiceReceiptMasBean> receiptBeanList) {
        this.receiptBeanList = receiptBeanList;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public List<TbServiceReceiptMasBean> getReceiptMasBeanList() {
        return receiptMasBeanList;
    }

    public void setReceiptMasBeanList(List<TbServiceReceiptMasBean> receiptMasBeanList) {
        this.receiptMasBeanList = receiptMasBeanList;
    }

	public String getEnvFlag() {
		return envFlag;
	}

	public void setEnvFlag(String envFlag) {
		this.envFlag = envFlag;
	}

	public String getCounterScheduleFlag() {
		return counterScheduleFlag;
	}

	public void setCounterScheduleFlag(String counterScheduleFlag) {
		this.counterScheduleFlag = counterScheduleFlag;
	}
}
