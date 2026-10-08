package com.abm.mainet.common.ui.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.integration.payment.dto.PaymentTransactionMasDTO;
import com.abm.mainet.common.service.CommonService;
import com.abm.mainet.common.ui.model.OnlineTransactionStatusModel;
import com.abm.mainet.common.utility.UserSession;

@Controller
@RequestMapping(value = "/OnlineTransactionStatus.html")
public class OnlineTransactionStatusController extends AbstractFormController<OnlineTransactionStatusModel>{

	@Autowired
	private CommonService IcommonService;
	
	 @RequestMapping(method = { RequestMethod.POST })
	    public ModelAndView index(final HttpServletRequest request) {
	        sessionCleanup(request);
	        getModel().bind(request);
	        return new ModelAndView("onlineTransactionStatus", MainetConstants.FORM_NAME, this.getModel());

	    }
	 
	    @ResponseBody
		@RequestMapping(params = "searchTransactionData", method = RequestMethod.POST)
		public ModelAndView searchTransactionData(@RequestParam("transactionId") Long transactionId, @RequestParam("status") String status,
				@RequestParam("appDate") Date appDate, HttpServletRequest request)
				throws IllegalAccessException, InvocationTargetException {
			bindModel(request);
			ModelAndView modelAndView = null;
			OnlineTransactionStatusModel model = this.getModel();
			Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
			List<PaymentTransactionMasDTO> transactionDetail = IcommonService.getTransactionDetail(transactionId, status, appDate, orgId);
			model.setTransactionMas(transactionDetail);
			modelAndView = new ModelAndView("onlineTransactionStatusValidn",MainetConstants.CommonConstants.COMMAND, model);
			return modelAndView;
		}
	
}
