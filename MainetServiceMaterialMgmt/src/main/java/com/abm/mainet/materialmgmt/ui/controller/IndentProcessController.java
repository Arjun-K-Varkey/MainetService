package com.abm.mainet.materialmgmt.ui.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.dms.domain.AttachDocs;
import com.abm.mainet.common.integration.dms.service.IAttachDocsService;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.integration.dto.DocumentDetailsVO;
import com.abm.mainet.common.master.mapper.DesignationServiceMapper;
import com.abm.mainet.common.master.repository.DesignationJpaRepository;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.DesignationService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.dto.WorkflowAction;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.service.IWorkFlowTypeService;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;
import com.abm.mainet.common.workflow.service.IWorkflowTaskService;
import com.abm.mainet.materialmgmt.dto.IndentIssueDto;
import com.abm.mainet.materialmgmt.dto.IndentProcessDTO;
import com.abm.mainet.materialmgmt.dto.IndentProcessItemDto;
import com.abm.mainet.materialmgmt.mapper.ItemMasterServiceMapper;
import com.abm.mainet.materialmgmt.service.IStoreMasterService;
import com.abm.mainet.materialmgmt.service.IndentProcessService;
import com.abm.mainet.materialmgmt.service.ItemMasterService;
import com.abm.mainet.materialmgmt.ui.model.GoodReceivedReceiptModel;
import com.abm.mainet.materialmgmt.ui.model.IndentProcessModel;

@Controller
@RequestMapping(value = { "/IndentProcess.html", "/IndentProcessApproval.html" })
public class IndentProcessController extends AbstractFormController<IndentProcessModel> {

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private IndentProcessService indentProcessService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	private IWorkFlowTypeService iWorkFlowTypeService;

	@Autowired
	private IWorkflowTaskService iWorkflowTaskService;

	@Resource
	private ItemMasterServiceMapper itemMasterServiceMapper;
	
	@Resource
	private ItemMasterService itemMasterService;

	@Autowired
	DesignationService designationService;

	@Resource
	private DesignationJpaRepository designationJpaRepository;

	@Resource
	private DesignationServiceMapper designationServiceMapper;
	
	@Autowired
	private IFileUploadService fileUpload;
	
	@Autowired
	private IAttachDocsService attachDocsService;

	@Resource
	private EmployeeJpaRepository employeeJpaRepository;

	@Resource
	private IStoreMasterService iStoreMasterService;
	
	@Autowired
	private IWorkflowRequestService workflowRequestService;
	
	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(GoodReceivedReceiptModel.class);
		
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index(HttpServletRequest request) throws Exception {
		sessionCleanup(request);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setListIndentProcessDTO(indentProcessService.searchIndentByStoreName(null, null, null, null, null, orgId));
		getSummaryFormLists(orgId);
		return defaultResult();
	}


	private void getSummaryFormLists(Long orgId) {
		this.getModel().setDepartmentIdNameList(departmentService.getAllDeptTypeNames());
		this.getModel().setStoreIdNameList(iStoreMasterService.getStoreIdAndNameList(orgId));
		this.getModel().setEmployees(employeeService.findAllEmpIntialInfoByOrg(orgId));
	}
	
	
	/**
	 * Add Indent form
	 * @param request
	 * @param storeid
	 * @param indentno
	 * @return
	 */
	@RequestMapping(params = "addIndent", method = RequestMethod.POST)
	public ModelAndView addStoreMaster(final HttpServletRequest request, final Model model) {
	    sessionCleanup(request);
	    this.getModel().setSaveMode(MainetConstants.WorksManagement.ADD);

	    Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
	    Organisation organisation = new Organisation();
	    organisation.setOrgid(orgId);

	    this.getModel().setUsageType(CommonMasterUtility.getLevelData("ITG", 1, organisation));
	    
	    this.getModel().setEmployees(employeeService.findAllEmpIntialInfoByOrg(orgId));
	    this.getModel().setStoreIdNameList(iStoreMasterService.getActiveStoreObjectListForAdd(orgId));
	    this.getModel().setItemIdNameList(itemMasterService.getActiveItemIdNameListByOrgId(orgId));
	    	    

	    // === Fetch employee details (same as getEmpDetailByLogin) ===
	    List<Object[]> empObjectList = employeeJpaRepository.getEmployeDetailsWithMobNoById(
	            UserSession.getCurrent().getEmployee().getEmpId(), orgId);

	    if (!empObjectList.isEmpty()) {
	        Object[] employee = empObjectList.get(0);
	        String locationName = employeeJpaRepository.getLocationNameByEmpIdOrgId(
	                UserSession.getCurrent().getEmployee().getEmpId(), orgId
	        );

	        IndentProcessDTO indentDTO = new IndentProcessDTO();
	        indentDTO.setIndenter(safeLong(employee[8]));
	        indentDTO.setEmpLogInFullName(safeString(employee[9]) + " " + safeString(employee[10]));

	        indentDTO.setDeptId(safeLong(employee[0]));
	        indentDTO.setDeptName(safeString(employee[1]));

	        indentDTO.setDesgId(safeLong(employee[2]));
	        indentDTO.setDesgName(safeString(employee[3]));

	        indentDTO.setReportingmgr(safeLong(employee[4]));
	        indentDTO.setReportingMgrName(safeString(employee[5]) + " " + safeString(employee[6]));

	        indentDTO.setBeneficiary(locationName);
	        indentDTO.setMobileNo(String.valueOf(safeLong(employee[7])));


	        this.getModel().setIndentProcessDTO(indentDTO);
	    }

	    return new ModelAndView("indentProcessForm", MainetConstants.FORM_NAME, this.getModel());
	}

		

	/**
	 * search Indent summary
	 * @param request
	 * @param storeid
	 * @param indentno
	 */
	@ResponseBody
	@RequestMapping(params = "searchIndentStore", method = RequestMethod.POST)
	public ModelAndView searchVehicleMast(final HttpServletRequest request, @RequestParam Long storeid, @RequestParam String indentno,
			@RequestParam Long deptId, @RequestParam Long indenter, @RequestParam String status) {
		sessionCleanup(request);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setListIndentProcessDTO(indentProcessService.searchIndentByStoreName(storeid, indentno, deptId,
				indenter, status, orgId));
		this.getModel().getIndentProcessDTO().setStoreid(storeid);
		this.getModel().getIndentProcessDTO().setIndentno(indentno);
		this.getModel().getIndentProcessDTO().setIndenter(indenter);
		this.getModel().getIndentProcessDTO().setStatus(status);
		getSummaryFormLists(orgId);
		return new ModelAndView("IndentProcessValidn", MainetConstants.FORM_NAME, this.getModel());
	}


	/** View Indent form
	 * @param indentid
	 */
	@RequestMapping(params = "viewDeptIndent", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView viewDeptIndent(Model model, @RequestParam("indentid") Long indentid, final HttpServletRequest httpServletRequest) {
		sessionCleanup(httpServletRequest);
		this.getModel().setSaveMode(MainetConstants.WorksManagement.VIEW);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setEmployees(employeeService.findAllEmpIntialInfoByOrg(orgId));
		this.getModel().setIndentProcessDTO(indentProcessService.getIndentDataByIndentId(indentid, orgId));
		Organisation organisation = new Organisation();
        organisation.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());
        this.getModel().setUsageType(CommonMasterUtility.getLevelData("ITG", 1, organisation));
				
		Map<Long, List<IndentIssueDto>> map = new LinkedHashMap<Long, List<IndentIssueDto>>();
		this.getModel().getIndentProcessDTO().getItem().forEach(issueItemList->{			
			List<IndentIssueDto> issueDtoList = new ArrayList<>();
			issueDtoList.addAll(issueItemList.getIndentIssueDtoList());
			map.put(issueItemList.getInditemid(), issueDtoList);	
		});
		this.getModel().getIndentIssueMap().putAll(map);
		
		return new ModelAndView("indentIssue", MainetConstants.FORM_NAME, this.getModel());
	}
	
	
	
	/**
	 * @return deptId, reportingmgr, desgId on selecting Indenter
	 */
	@ResponseBody
	@RequestMapping(params = "getEmpDetail", method = RequestMethod.POST)
	public Map<String, Object> getEmpDetail(@RequestParam("indenter") Long indenter, final HttpServletRequest request) {
		List<Object[]> empObjectList = employeeJpaRepository.getEmployeDetailsWithMobNoById(
		        UserSession.getCurrent().getEmployee().getEmpId(),
		        UserSession.getCurrent().getOrganisation().getOrgid()
		);
		
		
		String locationName = employeeJpaRepository.getLocationNameByEmpIdOrgId(indenter, UserSession.getCurrent().getOrganisation().getOrgid());
		Object[] employee = empObjectList.get(0);
		Map<String, Object> object = new LinkedHashMap<String, Object>();			
		object.put("deptId", new Object[] {Long.valueOf(employee[0].toString()), employee[1].toString()});
		object.put("desgId", new Object[] {Long.valueOf(employee[2].toString()), employee[3].toString()});
		object.put("beneficiaryLocName", locationName);
		object.put("mobileNo", Long.valueOf(employee[7].toString()));
		
		if(null !=  employee[5] && null != employee[6])
			object.put("reportingmgr", new Object[] {Long.valueOf(employee[4].toString()), employee[5].toString(), employee[6].toString()});
		return object;
	}
	
	
	
	private String safeString(Object value) {
	    return value != null ? value.toString() : "";
	}

	private Long safeLong(Object value) {
	    return value != null ? Long.valueOf(value.toString()) : 0L;
	}


	/**
	 * @return location on selecting storeid
	 */
	@ResponseBody
	@RequestMapping(params = "getLocation", method = RequestMethod.POST)
	public Map<Long, String> getLocation(@RequestParam("storeid") Long storeid, final HttpServletRequest request) {
		final Object[] storeObject = iStoreMasterService.getStoreDetailsByStore(storeid, UserSession.getCurrent().getOrganisation().getOrgid());
		Object[] location = (Object[]) storeObject[0];
		Map<Long, String>  locationMap = new LinkedHashMap<>();
		locationMap.put(Long.valueOf(location[0].toString()), location[1].toString());
		return locationMap;
	}

	/**
	 * @return uom on selecting itemid
	 */
	@ResponseBody
	@RequestMapping(params = "getUom", method = RequestMethod.POST)
	public  Map<String, Object> getUom(@RequestParam("itemid") Long itemid, final HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		
		
		String uom= CommonMasterUtility.getNonHierarchicalLookUpObject(itemMasterService.getUomByitemCode(UserSession.getCurrent().getOrganisation().getOrgid(),
				itemid), UserSession.getCurrent().getOrganisation()).getLookUpDesc();
		response.put("uom", uom);
		
		List<Object[]> groupData = itemMasterService.getItemGroupIdAndNames(UserSession.getCurrent().getOrganisation().getOrgid(), itemid);
		Map<Long, String> groups = new LinkedHashMap<>();
		for (Object[] row : groupData) {
	        groups.put(((Number) row[0]).longValue(), row[1].toString());
	    }
		response.put("itemGroups", groups);
		
		return response;
		
	}
	
	
	@ResponseBody
	@RequestMapping(params = "submitIndentForm", method = RequestMethod.POST)
	public ModelAndView submitIndentForm(final HttpServletRequest request, final Model model) throws Exception {
		bindModel(request);
	
		if(this.getModel().saveForm())
			 return jsonResult(JsonViewObject.successResult(this.getModel().getSuccessMessage()));
		else {
			ModelAndView modelAndView = null;		
			modelAndView = new ModelAndView("indentIssue", MainetConstants.FORM_NAME, this.getModel());
			modelAndView.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
			return modelAndView;
		}
	}
	
	
	@RequestMapping(params = "printDepartmentalIndent", method = RequestMethod.POST)
	public ModelAndView printDepartmentalIndent(final HttpServletRequest request,
	                                            @RequestParam(required = false) String indentNo) {
	    long levelCheck = this.getModel().getLevelcheck();

	    // ---------- LEVEL 0 ----------
	    if (levelCheck == 0L) {
	        // Store Name
	        Object[] nameObject = this.getModel().getStoreIdNameList().stream()
	                .filter(k -> safeParseLong(((Object[]) k)[0]) != null &&
	                             safeParseLong(((Object[]) k)[0]).equals(this.getModel().getIndentProcessDTO().getStoreid()))
	                .findFirst().orElse(null);

	        if (nameObject != null && nameObject.length > 1 && nameObject[1] != null) {
	            this.getModel().getIndentProcessDTO().setStoreDesc(nameObject[1].toString());
	        }

	        // Item Names
	        Map<Long, String> itemObjectMap = new HashMap<>();
	        this.getModel().getItemIdNameList().forEach(
	                itemObj -> {
	                    Long key = safeParseLong(itemObj[0]);
	                    if (key != null && itemObj[1] != null) {
	                        itemObjectMap.put(key, itemObj[1].toString());
	                    }
	                }
	        );

	        this.getModel().getIndentProcessDTO().getItem().forEach(
	                itemDetail -> {
	                    if (itemDetail.getItemid() != null) {
	                        itemDetail.setItemName(itemObjectMap.get(itemDetail.getItemid()));
	                    }
	                }
	        );
	    }

	    // ---------- LEVEL 2 ----------
	    if (levelCheck == 2L) {
	        // Workflow Remarks
	        WorkflowRequest workflowRequest = workflowRequestService.getWorkflowRequestByAppIdOrRefId(
	                null,
	                this.getModel().getIndentProcessDTO().getIndentno(),
	                UserSession.getCurrent().getOrganisation().getOrgid()
	        );

	        if (workflowRequest != null &&
	            workflowRequest.getWorkFlowTaskList() != null &&
	            workflowRequest.getWorkFlowTaskList().size() > 1) {
	            List<WorkflowAction> collect = workflowRequest.getWorkFlowTaskList().get(1)
	                    .getWorkFlowActionList().stream()
	                    .filter(a -> a.getComments() != null)
	                    .collect(Collectors.toList());

	            if (!collect.isEmpty()) {
	                this.getModel().getIndentProcessDTO().setRemarks(collect.get(0).getComments());
	            }
	        }

	        // Department Name
	        if (UserSession.getCurrent() != null &&
	            UserSession.getCurrent().getEmployee() != null &&
	            UserSession.getCurrent().getEmployee().getTbDepartment() != null) {
	            String departmentName = UserSession.getCurrent().getEmployee().getTbDepartment().getDpDeptdesc();
	            this.getModel().setDeptName(departmentName);
	        }

	        // IssuedTo Employee Name
			if (null != this.getModel().getIndentProcessDTO().getIssuedTo()) {
				Long empId = safeParseLong(this.getModel().getIndentProcessDTO().getIssuedTo());
				if (empId != null) {
					Object[] empData = employeeJpaRepository.getEmpFullNameByEmpId(empId);
					if (empData != null && empData.length > 0 && empData[0] instanceof Object[]) {
						Object[] row = (Object[]) empData[0];
						String firstName = row[0] != null ? row[0].toString() : "";
						String lastName = row[1] != null ? row[1].toString() : "";
						String fullName = (firstName + " " + lastName).trim();
						this.getModel().getIndentProcessDTO().setReceivedBy(fullName);
					}
				}
			}

	        // Indenter Mobile No
	        Long indenterId = safeParseLong(this.getModel().getIndentProcessDTO().getIndenter());
	        if (indenterId != null) {
	            String indenterMobNo = employeeJpaRepository.getEmpMobNo(indenterId);
	            this.getModel().getIndentProcessDTO().setMobileNo(indenterMobNo);
	        }
	    }

	    // ---------- Common Employee (Indenter Name) ----------
	    Object[] employeeObject = this.getModel().getEmployees().stream()
	            .filter(k -> safeParseLong(((Object[]) k)[3]) != null &&
	                         safeParseLong(((Object[]) k)[3]).equals(this.getModel().getIndentProcessDTO().getIndenter()))
	            .findFirst().orElse(null);

	    if (employeeObject != null && employeeObject.length >= 3) {
	        String firstName = employeeObject[0] != null ? employeeObject[0].toString() : "";
	        String lastName  = employeeObject[2] != null ? employeeObject[2].toString() : "";
	        this.getModel().getIndentProcessDTO().setIndenterName((firstName + " " + lastName).trim());
	    }

	    // ---------- Return Views ----------
	    if (levelCheck == 0L) {
	        return new ModelAndView("DepartmentalIndentPrint", MainetConstants.FORM_NAME, this.getModel());
	    } else {
	        return new ModelAndView("DepartmentalIndentApprovalPrint", MainetConstants.FORM_NAME, this.getModel());
	    }
	}

	
	private Long safeParseLong(Object value) {
	    if (value == null) {
	        return null;
	    }
	    String str = value.toString().trim();
	    if (str.isEmpty()) { // handles "" or "   "
	        return null;
	    }
	    try {
	        return Long.valueOf(str);
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}


	
	@RequestMapping(params = "showDetails", method = RequestMethod.POST)
	public ModelAndView indentFormApproval(@RequestParam("appNo") final String appNo,
			@RequestParam("taskId") final String taskId,
			@RequestParam(value = "actualTaskId", required = false) final Long actualTaskId,
			@RequestParam(value = "taskName", required = false) final String taskName,
			final HttpServletRequest httpServletRequest, final Model model) {
		this.sessionCleanup(httpServletRequest);
		fileUpload.sessionCleanUpForFileUpload();
		this.bindModel(httpServletRequest);
		IndentProcessModel indentModel = this.getModel();
		indentModel.setTaskId(actualTaskId);
		Organisation organisation = new Organisation();
		
        organisation.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());
        this.getModel().setUsageType(CommonMasterUtility.getLevelData("ITG", 1, organisation));
        
		indentModel.getWorkflowActionDto().setReferenceId(appNo);
		indentModel.getWorkflowActionDto().setTaskId(actualTaskId);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		this.getModel().setEmployees(employeeService.findAllEmpIntialInfoByOrg(orgId));
		indentModel.setIndentProcessDTO(indentProcessService.getIndentDataByIndentNo(appNo, orgId));
		getModel().setLastChecker(iWorkFlowTypeService.isLastTaskInCheckerTaskList(actualTaskId));
		getModel().setLevelcheck(iWorkflowTaskService.findByTaskId(actualTaskId).getCurentCheckerLevel());
		final List<AttachDocs> attachDocs = attachDocsService.findByCode(this.getModel().getIndentProcessDTO().getOrgid(),
				(MainetConstants.DEPT_SHORT_NAME.STORE + MainetConstants.WINDOWS_SLASH + this.getModel().getIndentProcessDTO().getIndentno()));
		this.getModel().setAttachDocsList(attachDocs);
		
		this.getModel().getIndentProcessDTO().setItem(indentProcessService.findAvailableQty(getModel().getIndentProcessDTO()));
		
			
		return new ModelAndView("indentIssue", MainetConstants.FORM_NAME, indentModel);
	}

	
	@RequestMapping(params = "indentissueitem", method = RequestMethod.POST)
	public ModelAndView indentissueitem(Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {	
		getModel().bind(httpServletRequest);
		int index = this.getModel().getIndexCount();
		this.getModel().getIndentProcessItemDTO().setInditemid(this.getModel().getIndentProcessDTO().getItem().get(index).getInditemid());
		this.getModel().getIndentProcessItemDTO().setIndentid(this.getModel().getIndentProcessDTO().getItem().get(index).getIndentid());
		this.getModel().setManagementCode(this.getModel().getIndentProcessDTO().getItem().get(index).getManagementCode());
		this.getModel().setBinLocList(indentProcessService.findAllBinLocationByItemID(UserSession.getCurrent().getOrganisation().getOrgid(), 
				 this.getModel().getIndentProcessDTO().getStoreid(), this.getModel().getIndentProcessDTO().getItem().get(index).getItemid()));
		
		this.getModel().getListIndentIssueDtos().clear();		
		List<IndentIssueDto> indentIssueDtoList = this.getModel().getIndentIssueMap().get(this.getModel().getIndentProcessItemDTO().getInditemid());
		if(indentIssueDtoList != null) {
			this.getModel().getListIndentIssueDtos().addAll(indentIssueDtoList);
			this.getModel().getListIndentIssueDtos().forEach(detailDto->{
				detailDto.setItemNumberList(this.getModel().getItemNumbersMap().get(this.getModel().getIndentProcessDTO().getItem().get(index).getItemid().toString().concat(",").concat(detailDto.getBinlocation().toString())));	
			});	
		}
		return new ModelAndView("itemissue", MainetConstants.FORM_NAME, this.getModel());
	}


	@ResponseBody
	@RequestMapping(params = "getItemNumbersByBin", method = RequestMethod.POST)
	public List<String> getItemNumbersByBin(@RequestParam("binLocId") Long binLocId, @RequestParam("itemid") Long itemid, final HttpServletRequest request) {
		bindModel(request);
		this.getModel().setItemNumberList(indentProcessService.getItemNumberListByBinLoc( binLocId, itemid, this.getModel().getIndentProcessDTO().getStoreid(), UserSession.getCurrent().getOrganisation().getOrgid()));
		this.getModel().getItemNumbersMap().put(itemid.toString().concat(",").concat(binLocId.toString()),  this.getModel().getItemNumberList());	
		return this.getModel().getItemNumberList();
	}

	
	@ResponseBody
	@RequestMapping(params = "getAvailableQntyByBin", method = RequestMethod.POST)
	public Double getAvailableQntyByBin(@RequestParam("binLocId") Long binLocId, @RequestParam("itemid") Long itemid,  @RequestParam("itemNo") String itemNo, final HttpServletRequest request) {
		bindModel(request);	
		return indentProcessService.fetchBalanceQuantityForIndent(UserSession.getCurrent().getOrganisation().getOrgid(),
				this.getModel().getIndentProcessDTO().getStoreid(), itemid, binLocId, itemNo);
	}
	
	
	@ResponseBody
	@RequestMapping(params = "getNotInBatchAvailableQntyByBin", method = RequestMethod.POST)
	public Double getNotInBatchAvailableQntyByBin(@RequestParam("binLocId") Long binLocId, @RequestParam("itemid") Long itemid, final HttpServletRequest request) {
		bindModel(request);
		/*
		 * return
		 * indentProcessService.fetchBalanceQuantityForIndent(UserSession.getCurrent().
		 * getOrganisation().getOrgid(),
		 * this.getModel().getIndentProcessDTO().getStoreid(), itemid, binLocId, null);
		 */
		
		return indentProcessService.findAvailableQtyForIndent(UserSession.getCurrent().getOrganisation().getOrgid(),
				this.getModel().getIndentProcessDTO().getStoreid(), itemid, binLocId);
	}
	
	@ResponseBody
	@RequestMapping(params = "getAvailableQntyForCompare", method = RequestMethod.POST)
	public Double getAvailableQntyForCompare(@RequestParam("itemid") Long itemid, @RequestParam("storeid") Long storeid,
			final HttpServletRequest request) {
		bindModel(request);
		LOGGER.info("Start finding BinLocationId ");
		LOGGER.info("item Id & Store Id=" + itemid + "&" + storeid);
		Long binLocation = indentProcessService
				.fetchAvailabelQtyToCompare(itemid, storeid, UserSession.getCurrent().getOrganisation().getOrgid())
				.longValue();
		LOGGER.info("BinLocation Id=" + binLocation);
		LOGGER.info("Storeid=" + storeid);
		LOGGER.info("Itemid=" + itemid);
		LOGGER.info("Start call for find Available qty");
		Double availableQuantity = indentProcessService.findAvailableQtyForIndent(
				UserSession.getCurrent().getOrganisation().getOrgid(), storeid, itemid, binLocation);
		LOGGER.info("Available Quantity=" + availableQuantity);
		return availableQuantity;
	}
	
	@ResponseBody
	@RequestMapping(params = "saveIndentIssueForm", method = RequestMethod.POST)
	public ModelAndView saveIndentIssueForm(final HttpServletRequest request,final Model model) {
		bindModel(request);
		int index = this.getModel().getIndexCount();
		this.getModel().getListIndentIssueDtos().forEach(indentIssueDto->{
			indentIssueDto.setIndentid(this.getModel().getIndentProcessDTO().getItem().get(index).getIndentid());
			indentIssueDto.setInditemid(this.getModel().getIndentProcessDTO().getItem().get(index).getInditemid());
		});	
		List<IndentIssueDto> indentIssueHelperDTOs = new ArrayList<IndentIssueDto>();
		indentIssueHelperDTOs.addAll(this.getModel().getListIndentIssueDtos());
		this.getModel().getIndentIssueMap().put(this.getModel().getIndentProcessItemDTO().getInditemid(), indentIssueHelperDTOs);
		this.getModel().getListIndentIssueDtos().clear();
		return new ModelAndView("indentIssue", MainetConstants.FORM_NAME, this.getModel());
	}


	@RequestMapping(params = "backToMainForm", method = RequestMethod.POST)
	public ModelAndView backToMainForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Model model) {
		bindModel(httpServletRequest);
		return new ModelAndView("indentIssue", MainetConstants.FORM_NAME, this.getModel());
	}

	@RequestMapping(method = RequestMethod.POST, params = { "doItemDetailsDeletion" })
	public @ResponseBody boolean doItemDetailsDeletion(final Model model, final HttpServletRequest request,
			@RequestParam("rowIndex") int rowIndex) {
		if (this.getModel().getListIndentIssueDtos().size() > rowIndex)
			this.getModel().getListIndentIssueDtos().remove(rowIndex);
		return true;
	}
	
	@RequestMapping(params = "getItemGroupDetById", method = RequestMethod.POST)
	public @ResponseBody Map<Long, String> getItemGroupDetById(@RequestParam("itemgroup") Long itemgroup,@RequestParam("storeid") Long storeid, final HttpServletRequest request) {
		Map<Long, String> storeDetailMap = new HashMap<>();
		List<Object[]> storeOnject=  indentProcessService.getGroupItemName(itemgroup,storeid,
				UserSession.getCurrent().getOrganisation().getOrgid());
		
		Map<Long, String> resultMap = new LinkedHashMap<>();
		 
        for (Object obj : storeOnject) {
            
            Object[] keyValuePair = (Object[]) obj;
            if (keyValuePair.length == 2) {
                
                Long key = ((Number) keyValuePair[0]).longValue();
                String value = (String) keyValuePair[1];
 
      
                resultMap.put(key, value);
            }
		
		/*storeDetailMap.put("grpId", storeOnject[0]));
		storeDetailMap.put("grpNames", storeOnject[1].toString());*/
		
		
	}
        return resultMap;
	}
	
	@RequestMapping(params = "getIndentProcessItemNames", method = RequestMethod.POST)
	public @ResponseBody Map<Long, String> getIndentProcessItemNames(@RequestParam("storeid") Long storeid , final HttpServletRequest request) {
		/*
		 * Map<Long, String> storeDetailMap = new HashMap<>(); List<Object[]>
		 * storeObject
		 * =indentProcessService.getGroupNames(UserSession.getCurrent().getOrganisation(
		 * ).getOrgid(), storeid);
		 */
		Map<Long, String> itemResutMap = new LinkedHashMap<>();
		
		 List<Object[]> rawItemList = itemMasterService.getActiveItemIdNameListByOrgId(UserSession.getCurrent().getOrganisation().getOrgid(), storeid);
		
		 
		 
		
		
		Map<Long, String> itemGroupresultMap = new LinkedHashMap<>();
		 
        for (Object obj : rawItemList) {
            
            Object[] keyValuePair = (Object[]) obj;
            if (keyValuePair.length == 2) {
                
                Long key = ((Number) keyValuePair[0]).longValue();
                String value = (String) keyValuePair[1];
 
      
                itemResutMap.put(key, value);
            }
		
		
		
	}
        return itemResutMap;
	}	

}
