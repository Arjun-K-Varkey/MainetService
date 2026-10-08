package com.abm.mainet.account.ui.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.account.dto.PaymentEntryDto;
import com.abm.mainet.account.service.DeductionRegisterService;
import com.abm.mainet.account.ui.model.DeductionRegisterModel;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.integration.acccount.service.AccountFieldMasterService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.UserSession;

/*
 * Object: Deduction Register(Story-99) Author By:- Ajay Kumar
 * Date: 10-03-2018
 */
@Controller
@RequestMapping("/DeductionRegister.html")
public class AccountDeductionRegisterController extends AbstractFormController<DeductionRegisterModel> {

    @Resource
    private DeductionRegisterService deductionRegisterService;
    
    @Resource
    private AccountFieldMasterService accountFieldMasterService;

    @RequestMapping(method = { RequestMethod.POST })
    public ModelAndView index(final HttpServletRequest httpServletRequest) {
        sessionCleanup(httpServletRequest);
        Map<Long, String> fieldMaster = accountFieldMasterService.getFieldMasterStatusLastLevels(UserSession.getCurrent().getOrganisation().getOrgid(),
        		UserSession.getCurrent().getOrganisation(), UserSession.getCurrent().getLanguageId());
        fieldMaster.put(-1L, "ALL");
       this.getModel().setFieldMaster(fieldMaster);
        this.getModel().setTdsLookUpList(
                CommonMasterUtility.getListLookup(PrefixConstants.TDS, UserSession.getCurrent().getOrganisation()));
        return index();
    }

    /*
     * Object: Deduction Register Report(Story-99) Author By:- Ajay Kumar Date: 10-03-2018
     */
    @ResponseBody
    @RequestMapping(params = "getAccountDeductionRegisterData", method = RequestMethod.POST)
    public ModelAndView viewAbstractreportSheet(@RequestParam("frmDate") String frmDate, @RequestParam("todate") String todate,
            @RequestParam("tdsTypeId") Long tdsTypeId, @RequestParam("fieldId") Long fieldId) {
        Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
        PaymentEntryDto paymentEntryDto;
        paymentEntryDto = deductionRegisterService.getDeductionRegisterData(frmDate, todate, orgId, tdsTypeId,fieldId);
        paymentEntryDto.setFromDate(frmDate);
        paymentEntryDto.setToDate(todate);
        String tdsTypeDesc = CommonMasterUtility.findLookUpDesc(PrefixConstants.TDS, orgId, tdsTypeId);
        paymentEntryDto.setTdsTypeDesc(tdsTypeDesc);
        // paymentEntryDto.setTdsTypeId(tdsTypeId);
        this.getModel().setPaymentEntryDto(paymentEntryDto);
        return new ModelAndView("AccountDeductionRegister/Report", MainetConstants.FORM_NAME, this.getModel());
    }

}
