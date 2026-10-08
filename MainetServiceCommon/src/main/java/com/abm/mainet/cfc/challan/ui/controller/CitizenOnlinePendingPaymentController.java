package com.abm.mainet.cfc.challan.ui.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.cfc.challan.service.CitizenOnlinePendingPaymentScheduler;
import com.abm.mainet.cfc.challan.ui.model.CitizenOnlinePendingPayment;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.ScrutinyServiceDto;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.dto.ApplicationFormChallanDTO;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbScrutinyLabelsService;
import com.abm.mainet.common.ui.controller.AbstractController;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.UserSession;

@Controller
@RequestMapping(value = "/CitizenOnlinePendingPayment.html")
public class CitizenOnlinePendingPaymentController extends AbstractController<CitizenOnlinePendingPayment> {
	@Autowired
	private CitizenOnlinePendingPaymentScheduler citiPayScheduler;

	@Resource
	private TbScrutinyLabelsService serviceMasterService;

	@Autowired
	private DepartmentService departService;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index(final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		getModel().setChallanDTO(new ApplicationFormChallanDTO());
		Long deptId = departService.getDepartmentIdByDeptCode(MainetConstants.DEPT_SHORT_NAME.PROPERTY);
		Long wtDeptId = departService.getDepartmentIdByDeptCode(MainetConstants.DEPT_SHORT_NAME.WATER);
		Long bndDeptId = departService.getDepartmentIdByDeptCode(MainetConstants.DEPT_SHORT_NAME.BND);
		Long licDeptId = departService.getDepartmentIdByDeptCode(MainetConstants.Dms.TRADE_DEPT);
		Long mrmDeptId = departService.getDepartmentIdByDeptCode(MainetConstants.MRM.MRM_DEPT_CODE);
		Long rnlDeptId = departService.getDepartmentIdByDeptCode(MainetConstants.RnLCommon.RentLease);
		getModel().setDeptList(departService
				.getDepartments(UserSession.getCurrent().getOrganisation().getOrgid(), MainetConstants.FlagA).stream()
				.filter(d -> d.getDpDeptid() != null && (d.getDpDeptid().equals(deptId)
						|| d.getDpDeptid().equals(wtDeptId) || d.getDpDeptid().equals(bndDeptId)
						|| d.getDpDeptid().equals(licDeptId) || d.getDpDeptid().equals(mrmDeptId)||d.getDpDeptid().equals(rnlDeptId)))
				.collect(Collectors.toList()));
		return new ModelAndView("CitizenOnlinePendingPayment", MainetConstants.FORM_NAME, getModel());
	}

	@RequestMapping(params = "initiatePendPayment", method = RequestMethod.POST)
	public ModelAndView getChecklistAndServiceCharge(final HttpServletRequest httpServletRequest) {
		this.getModel().bind(httpServletRequest);
		CitizenOnlinePendingPayment model = this.getModel();
		model.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
		citiPayScheduler.runOnlinePendingPaymentDate(model);
		if (model.hasValidationErrors()) {
			this.getModel().addValidationError("No record Found for selected criteria");
			ModelAndView modelAndView = new ModelAndView("CitizenOnlinePendingPaymentValidn", MainetConstants.FORM_NAME,
					this.getModel());

			modelAndView.addObject(
					BindingResult.MODEL_KEY_PREFIX
							+ ApplicationSession.getInstance().getMessage(MainetConstants.FORM_NAME),
					model.getBindingResult());
			return modelAndView;
		}
		return jsonResult(JsonViewObject.successResult(model.getSuccessMessage()));

	}
	/**
	 * This method is used to return the list of services based on the department id
	 * 
	 * @param deptId
	 * @param request
	 * @return list of services
	 */
	@RequestMapping(params = MainetConstants.AdvertisingAndHoarding.SEARCH_SERVICES_BY_DEPTID, method = {
			RequestMethod.POST })
	@ResponseBody
	public List<ScrutinyServiceDto> searchServiceByDeptId(@RequestParam(MainetConstants.Common_Constant.DEPTID) Long deptId,
			HttpServletRequest request) {

		this.getModel().setServiceList(
				serviceMasterService.getAllServicesData(UserSession.getCurrent().getOrganisation().getOrgid()).stream()
				.filter(d -> d.getTriCod1()!=null && ((d.getTriCod1().equals(deptId)))).collect(Collectors.toList()));
		return this.getModel().getServiceList();

	}
}
