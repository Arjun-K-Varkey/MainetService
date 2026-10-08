package com.abm.mainet.common.master.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.ApplicationStatusDTO;
import com.abm.mainet.common.master.dto.TbServicesMst;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbDepartmentService;
import com.abm.mainet.common.master.service.TbServicesMstService;
import com.abm.mainet.common.master.ui.model.CFCApplicationStsModel;
import com.abm.mainet.common.service.ICFCApplicationMasterService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.dto.WorkflowTaskActionWithDocs;
import com.abm.mainet.common.workflow.service.IWorkflowActionService;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;

@Controller
@RequestMapping("CFCApplicationStatus.html")
public class CFCApplicationStsController extends AbstractFormController<CFCApplicationStsModel> {

	@Autowired
	private TbServicesMstService tbServicesMstService;

	@Autowired
	private ICFCApplicationMasterService cfcApplicationMastServie;

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping(method = { RequestMethod.POST })
	public ModelAndView index(final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		lodaData();
		
		return index();

	}

	private void lodaData() {
		this.getModel().setDepartmentList(
				ApplicationContextProvider.getApplicationContext().getBean(TbDepartmentService.class).findAll());
		this.getModel().setLangId(UserSession.getCurrent().getLanguageId());
	}

	@RequestMapping(params = "form", method = { RequestMethod.POST })
	public ModelAndView form(final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		lodaData();

		return new ModelAndView("CFCApplicationSts/form", MainetConstants.FORM_NAME, this.getModel());

	}

	@RequestMapping(params = "getServiceByDept", method = RequestMethod.POST)
	public @ResponseBody List<TbServicesMst> searchPrefiata(@RequestParam("deptId") final Long deptId) {
		return getServiceListByDeptId(deptId);
	}

	private List<TbServicesMst> getServiceListByDeptId(final Long deptId) {
		Organisation organisation = UserSession.getCurrent().getOrganisation();
		final List<TbServicesMst> serviceMstList = tbServicesMstService.findByDeptId(deptId, organisation.getOrgid());
		return serviceMstList;
	}

	@ResponseBody
	@RequestMapping(params = "searchForm", method = RequestMethod.POST)
	public ModelAndView searchData(final HttpServletRequest request, final Model model) {
		bindModel(request);
		this.getModel().setLangId(UserSession.getCurrent().getLanguageId());
		this.getModel().getApplicationStatusDto().setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
		List<ApplicationStatusDTO> applicationStatusDtos = cfcApplicationMastServie
				.getApplicationStatusDtos(this.getModel().getApplicationStatusDto(),UserSession.getCurrent().getLanguageId());
		this.getModel()
				.setServiceMstList(getServiceListByDeptId(this.getModel().getApplicationStatusDto().getDeptId()));
		List<ApplicationStatusDTO> applicationStatusDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(applicationStatusDtos)) {
			for (ApplicationStatusDTO statusDTO : applicationStatusDtos) {
				int count = cfcApplicationMastServie.existsByApplicationId(statusDTO.getApplicationId());
				if (count > 0) {
					applicationStatusDtoList.add(statusDTO);
				}
			}
		}
		if(CollectionUtils.isEmpty(applicationStatusDtoList)) {
			ModelAndView mv = new ModelAndView("CFCApplicationSts/form", MainetConstants.FORM_NAME, this.getModel());
	        mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
			this.getModel().addValidationError(getApplicationSession().getMessage("work.management.vldn.grid.nodatafound"));
			return mv;
		}
		this.getModel().setApplicationStatusDTOs(applicationStatusDtoList);
		if(CollectionUtils.isNotEmpty(applicationStatusDtos) && applicationStatusDtos.get(0).getDeptId() != null) {
			this.getModel().setDepartmentCode(departmentService.getDeptCode(applicationStatusDtos.get(0).getDeptId()));
		}
		return new ModelAndView("CFCApplicationSts/form", MainetConstants.FORM_NAME, this.getModel());
	}

	@ResponseBody
	@RequestMapping(params = "isDataExist", method = RequestMethod.POST)
	public boolean isDataExist(final HttpServletRequest request, final Model model) {
		bindModel(request);
		this.getModel().getApplicationStatusDto().setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
		return cfcApplicationMastServie.isDataExist(this.getModel().getApplicationStatusDto());
	}

	@RequestMapping(params = "getWorkFlowHistory", method = RequestMethod.POST)
	public @ResponseBody ModelAndView getWorkFlowHistory(
			@RequestParam(name = MainetConstants.Common_Constant.APPLICATION_ID) final Long applicationId,
			@RequestParam(name = MainetConstants.WorksManagement.MODE) final String mode, ModelMap modelMap) {

		Long appId = cfcApplicationMastServie.getCFCApplicationOnlyByApplicationId(applicationId).getApmApplicationId();
		List<WorkflowTaskActionWithDocs> historyList = new ArrayList<WorkflowTaskActionWithDocs>();
		WorkflowRequest workflowRequest = ApplicationContextProvider.getApplicationContext()
				.getBean(IWorkflowRequestService.class)
				.getWorkflowRequestByAppIdOrRefId(appId, null, UserSession.getCurrent().getOrganisation().getOrgid());
		if (workflowRequest != null) {
			List<WorkflowTaskActionWithDocs> actionHistory = ApplicationContextProvider.getApplicationContext()
					.getBean(IWorkflowActionService.class).getActionLogByUuidAndWorkflowId(appId.toString(),
							workflowRequest.getId(), (short) UserSession.getCurrent().getLanguageId());
			for (WorkflowTaskActionWithDocs workflowTaskAction : actionHistory) {
				Employee employee = employeeService.findEmployeeByIdAndOrgId(workflowTaskAction.getEmpId(),
						workflowTaskAction.getOrgId());
				Employee compEmployee =employeeService.findCompletedEmployeeByIdDate(workflowTaskAction.getEmpId(), workflowTaskAction.getDateOfAction());
				if (employee != null) {
                //D#160153 Employee full name should display in track your application functionality 
					if((workflowTaskAction.getDecision().equals("Pending"))) {
						StringBuilder empName = new StringBuilder();
						if (employee.getEmpname() != null)
							empName.append(employee.getEmpname());
						if (employee.getEmpmname() != null)
							empName.append(MainetConstants.WHITE_SPACE + employee.getEmpmname());
						if (employee.getEmplname() != null)
							empName.append(MainetConstants.WHITE_SPACE + employee.getEmplname());
						workflowTaskAction.setEmpName(empName.toString());
						workflowTaskAction.setEmpEmail(employee.getEmpemail());
						if (employee.getDesignation() != null) {
							workflowTaskAction.setEmpGroupDescEng(employee.getDesignation().getDsgname());
							workflowTaskAction.setEmpGroupDescReg(employee.getDesignation().getDsgnameReg());
						}
					}else{
						if(compEmployee!=null) {
						workflowTaskAction.setEmpName(compEmployee.getEmploginname());
						workflowTaskAction.setEmpEmail(compEmployee.getEmpemail());
						if (compEmployee.getDesignation() != null) {
						workflowTaskAction.setEmpGroupDescEng(employee.getDesignation().getDsgname());
						}}
					}
					
					
				}
               // #133533
				if (workflowRequest.getStatus().equals("CLOSED")
						&& workflowTaskAction.getDecision().equalsIgnoreCase("Pending")) {

				} else {
					historyList.add(workflowTaskAction);
				}
				modelMap.addAllAttributes(actionHistory);
			}

			modelMap.put(MainetConstants.WorkFlow.ACTIONS, historyList);
		}
		return new ModelAndView(MainetConstants.WorksManagement.WORK_WORKFLOW_HISTORY, MainetConstants.FORM_NAME,
				modelMap);
	}
}


