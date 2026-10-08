package com.abm.mainet.materialmgmt.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.materialmgmt.ui.model.PurchaseRequistionApprovalModel;

@Controller
@RequestMapping("PurchaseRequisitionApproval.html")
public class PurchaseRequisitionApprovalController extends AbstractFormController<PurchaseRequistionApprovalModel> {

	@ResponseBody
	@RequestMapping(params = "PurchaseRequisitionApprovalForm", method = RequestMethod.POST)
	public ModelAndView workorder(final HttpServletRequest httpServletRequest, final Model model) {
		sessionCleanup(httpServletRequest);
		return new ModelAndView("ShowPurchaseRequisitionApprovalForm", MainetConstants.FORM_NAME, this.getModel());
	}

}
