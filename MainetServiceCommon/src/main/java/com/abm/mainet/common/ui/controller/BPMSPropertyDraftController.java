package com.abm.mainet.common.ui.controller;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.integration.dms.repository.BpmsIntegrationRepository;
import com.abm.mainet.common.integration.dms.service.BpmsIntegrationServiceImpl;
import com.abm.mainet.common.integration.dms.service.IBpmsIntegrationService;
import com.abm.mainet.common.integration.dto.RequestDTO;
import com.abm.mainet.common.master.dto.EmployeeBean;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.ui.model.BPMSPropertyDraftModel;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.domain.ActionResponse;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;


/**
 * @author Saroj.Tambe
 *
 */
@Controller
@RequestMapping("/BPMSPropertyDraft.html")
public class BPMSPropertyDraftController extends AbstractFormController<BPMSPropertyDraftModel>{
	
	@Autowired
	private IEmployeeService employeeService;
	
	@Autowired
	private BpmsIntegrationRepository repository;
	
	@Autowired
	private BpmsIntegrationServiceImpl bpmsIntegrationServiceImpl;
	
	private static final Logger LOGGER = Logger.getLogger(BPMSPropertyDraftController.class);
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView index(Model uiModel, HttpServletRequest httpServletRequest) {
		
		LOGGER.info("index started of BPMSPropertyDraftController....");
		BPMSPropertyDraftModel bpmsPropertyDraftModel = this.getModel();
		Long currentOrgId = UserSession.getCurrent().getOrganisation().getOrgid();
		//List<EmployeeBean> list = employeeService.getAllEmployee(currentOrgId);
		
		List<Long> findPendingWorkflowProposalNumbersList = repository.findPendingWorkflowProposalNumbers();

		//bpmsPropertyDraftModel.setEmpList(findPendingWorkflowProposalNumbers);
		
		bpmsPropertyDraftModel.setProposalNoList(findPendingWorkflowProposalNumbersList);
		
		
		this.getModel().setBpmsSummaryPageDtoList(bpmsIntegrationServiceImpl.findBpmsData(MainetConstants.FlagN));
		bpmsPropertyDraftModel.setViewMode(MainetConstants.FlagA);
		
		LOGGER.info("index end of BPMSPropertyDraftController....");
		
		return new ModelAndView(MainetConstants.BPMS_PROPERTY_DRAFT.BPMS_PROPERTY_DRAFT_FORM, MainetConstants.FORM_NAME,
				bpmsPropertyDraftModel);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, params = "saveDetails")
	public @ResponseBody String submit(HttpServletRequest request, ModelMap modelMap)
			throws EntityNotFoundException, Exception {
		LOGGER.info("From Grievance Service Rest Controller submit Approving the Requests");

		getModel().bind(request);
		BPMSPropertyDraftModel model = getModel();
		String decisionResponse="BPMS property has been successfully drafed";
		return decisionResponse;
			
	}
	

}
