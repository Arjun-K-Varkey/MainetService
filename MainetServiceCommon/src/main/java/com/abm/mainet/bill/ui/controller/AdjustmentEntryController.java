package com.abm.mainet.bill.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.bill.repository.AdjustmentEntryRepository;
import com.abm.mainet.bill.service.AdjustmentEntryService;
import com.abm.mainet.bill.service.BillMasterCommonService;
import com.abm.mainet.bill.ui.model.AdjustmentEntryModel;
import com.abm.mainet.cfc.challan.dto.AdjustmentDetailDTO;
import com.abm.mainet.cfc.challan.dto.AdjustmentMasterDTO;
import com.abm.mainet.cfc.checklist.service.IChecklistVerificationService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.integration.dto.TbBillDet;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.master.dto.TbTaxMas;
import com.abm.mainet.common.master.service.TbDepartmentService;
import com.abm.mainet.common.master.service.TbTaxMasService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.dto.WorkflowTaskActionWithDocs;
import com.abm.mainet.common.workflow.repository.IWorkflowRepository;
import com.abm.mainet.common.workflow.service.IWorkFlowTypeService;
import com.abm.mainet.common.workflow.service.IWorkflowActionService;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;
import com.google.common.util.concurrent.AtomicDouble;

/**
 * @author Rahul.Yadav
 *
 */
@Controller
@RequestMapping({"/AdjustmentEntry.html", "/AdjustmentEntryAuthorization.html"})
public class AdjustmentEntryController extends AbstractFormController<AdjustmentEntryModel> {

	private static final Logger LOGGER = Logger.getLogger(AdjustmentEntryController.class);
    @Resource
    private TbDepartmentService tbDepartmentService;

    @Resource
    private AdjustmentEntryService adjustmentEntryService;

    @Resource
    private TbTaxMasService tbTaxMasService;
    
    @Resource
    IFileUploadService fileUpload;
    
    @Resource
    private BillMasterCommonService billMasterCommonService;
    
    @Autowired
    private ServiceMasterService serviceMaster;
    
    @Autowired
    private IWorkFlowTypeService iWorkFlowTypeService;
    
    @Autowired
    private IWorkflowActionService workflowActionService;
    
    @Autowired
    private IChecklistVerificationService checklistVerificationService;
    
    @Resource
    private AdjustmentEntryRepository adjustmentEntryRepository;
    
    @Autowired
    private IWorkflowRequestService workflowReqService;
    
    @RequestMapping(method = { RequestMethod.POST })
    public ModelAndView index(final HttpServletRequest httpServletRequest) {
        sessionCleanup(httpServletRequest);
        fileUpload.sessionCleanUpForFileUpload();
        this.getModel().setCommonHelpDocs("AdjustmentEntry.html");
        getModel().setDepartment(tbDepartmentService.findAllActive(UserSession.getCurrent().getOrganisation().getOrgid()));
        return index();
    }

    @RequestMapping(method = RequestMethod.POST, params = "searchAdjustmentData")
    public ModelAndView serachAdjustmentData(final HttpServletRequest request, @RequestParam("deptId") final Long deptId,
            @RequestParam("ccnOrPropNo")  String ccnNoOrPropNo, @RequestParam("oldNo")  String oldNo) {
        bindModel(request);
        final AdjustmentEntryModel model = getModel();
        model.setBill(null);
        model.setBillList(null);
        model.getAdjustmentDto().setDpDeptId(deptId);
        final Organisation orgId = UserSession.getCurrent().getOrganisation();
        String deptCode = tbDepartmentService.findDepartmentShortCodeByDeptId(deptId, orgId.getOrgid());
        model.setDepartmentCode(deptCode);
        
        if(StringUtils.equals(deptCode, "AS") && StringUtils.isBlank(ccnNoOrPropNo) && StringUtils.isNotBlank(oldNo)) {
        	String propNo = adjustmentEntryService.getPropNoByOldPropNo(oldNo, orgId.getOrgid(), deptId);
        	ccnNoOrPropNo = propNo;
        }
        
        Long applicationId = adjustmentEntryRepository.getApplicationIdByAdjRefNo(ccnNoOrPropNo);
        WorkflowRequest workflowRequest = null;
        if(applicationId != null) {
        	 workflowRequest = workflowReqService
    				.getWorkflowRequestByAppIdOrRefId(applicationId, null, orgId.getOrgid());
        	
        }
        	if (workflowRequest != null && MainetConstants.WorkFlow.Status.PENDING.equalsIgnoreCase(workflowRequest.getStatus())) {
                model.addValidationError(ApplicationSession.getInstance().getMessage("adjustment.entry.process"));
    		}else {
    			model.getAdjustmentDto().setAdjRefNo(ccnNoOrPropNo);
    	        model.getAdjustmentDto().setLangId(UserSession.getCurrent().getLanguageId());
    	        model.getAdjustmentDto().setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
    	        model.getAdjustmentDto().setCreatedBy(UserSession.getCurrent().getEmployee().getEmpId());
    	        if (model.validateData()) {
    	            model.setBillList(adjustmentEntryService.fetchModuleWiseBills(deptId, ccnNoOrPropNo,
    	                    orgId.getOrgid()));
    				if (CollectionUtils.isEmpty(model.getBillList())) {
    	                model.addValidationError(ApplicationSession.getInstance().getMessage("adjustment.entry.invalid"));
    	            }else {
    	            	
    	            	if ((model.getBillList().get(0).getBmIdno() <= 0) && (model.getBillList().size() == 1)) {
    	                    List<TbTaxMas> taxes = adjustmentEntryService.fetchAllModulewiseTaxes(deptId, orgId);
    	                	if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.WATER, model.getDepartmentCode())) {
    	                		taxes=taxes.stream().filter(tax ->!tax.getTaxDesc().equals(MainetConstants.TAX_TYPE.ADVANCE_PAYMENT)).collect(Collectors.toList());
    	                	}
    	                    List<TbBillDet> billdet = null;
    	                    TbBillDet det = null;
    	                    if ((taxes != null) && !taxes.isEmpty()) {
    	                        billdet = new ArrayList<>();
    	                        for (final TbTaxMas tax : taxes) {
    	                            det = new TbBillDet();
    	                            det.setTaxDesc(tax.getTaxDesc());
    	                            det.setTaxId(tax.getTaxId());
    	                            billdet.add(det);
    	                        }
    	                        model.setBill(new TbBillMas());
    	                        model.getBill().setTbWtBillDet(billdet);
    	                    }
    	                } else {
    	                	model.setTotalBalanceAmount(0.0);
    	                	model.setBill(model.getBillList().get(model.getBillList().size() - 1));
    	                    if ((model.getBill().getTbWtBillDet() != null) && !model.getBill().getTbWtBillDet().isEmpty()) {
    	                        for (final TbBillDet det : model.getBill().getTbWtBillDet()) {
    	                            det.setTaxDesc(tbTaxMasService.findTaxDescByTaxIdAndOrgId(det.getTaxId(), orgId.getOrgid()));
    	                            model.setTotalBalanceAmount(model.getTotalBalanceAmount() + det.getBdCurBalTaxamt() + det.getBdPrvBalArramt());
    	                        }
    	                    }
    	                }
    	            	if(deptCode.equals(MainetConstants.WATER_DEPT)) {
    	            		CustomerInfoDTO csmrInfoDetails = adjustmentEntryService.getCsmrInfoDetails(ccnNoOrPropNo, orgId.getOrgid());
    	            		if(csmrInfoDetails!=null) {
    	                		model.getBill().setBmCcnOwner(csmrInfoDetails.getName());
    	                		model.getBill().setAddress(csmrInfoDetails.getAddress());
    	            		}
    	            	}
    	            	if (MainetConstants.DEPT_SHORT_NAME.PROPERTY.equals(deptCode)){
    	            		model.getAdjustmentDto().setServiceId(serviceMaster.getServiceIdByShortName(orgId.getOrgid(),
    	            				MainetConstants.ServiceShortCode.ADJUSTMENT_ENTRY));
    	            		model.getAdjustmentDto().setServiceFlag(MainetConstants.FlagA);//Application Service
    	        		}
    				}
    		}
		
        
        }
		  model.setTotalBalanceAmount(Math.round(model.getTotalBalanceAmount()));
        return defaultMyResult();
    }

    @RequestMapping(method = RequestMethod.POST, params = "viewHistoryDetails")
    public ModelAndView serachAdjustmentData(final HttpServletRequest request) {
        bindModel(request);
        final AdjustmentEntryModel model = getModel();
        model.setHistoryMsg(null);
        final List<AdjustmentMasterDTO> adjustmentHistory;
        if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, model.getDepartmentCode())) {
        	adjustmentHistory = adjustmentEntryService.fetchHistory(
        			model.getAdjustmentDto().getAdjRefNo(),
                    model.getAdjustmentDto().getDpDeptId(),model.getAdjustmentDto().getFlatNo());
        }else {
        	adjustmentHistory = adjustmentEntryService.fetchHistory(
                    model.getBill().getCsIdn().toString(),
                    model.getAdjustmentDto().getDpDeptId(),model.getAdjustmentDto().getFlatNo());
        }
        model.setAdjustmentHistoryDto(adjustmentHistory);
        if ((adjustmentHistory == null) || adjustmentHistory.isEmpty()) {
            model.setHistoryMsg(ApplicationSession.getInstance().getMessage("adjustment.history.invalid"));
        }
        return new ModelAndView("AdjustmentHistoryView", MainetConstants.FORM_NAME, model);
    }
    
    @RequestMapping(params = "getDeptCode", method = {RequestMethod.POST})
    public @ResponseBody String getDeptCode(@RequestParam("deptId") final Long deptId, HttpServletRequest request) {
    	String deptCode = "";
    	try {
    		final Organisation orgId = UserSession.getCurrent().getOrganisation();
            deptCode = tbDepartmentService.findDepartmentShortCodeByDeptId(deptId, orgId.getOrgid());
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return deptCode;
    }
    
    @ResponseBody
    @RequestMapping(params = "getBillingMethod", method = { RequestMethod.POST })
    public List<String> getBillingMethodAndFlatList(@RequestParam("propNo") String propNo, @RequestParam("deptId") final Long deptId,
    		@RequestParam("oldNo")  String oldNo, HttpServletRequest request) {
    	this.getModel().bind(request);
    	AdjustmentEntryModel model = this.getModel();
    	List<String> flatNoList = null;
    	String billingMethod = null;
    	final Organisation orgId = UserSession.getCurrent().getOrganisation();
        String deptCode = tbDepartmentService.findDepartmentShortCodeByDeptId(deptId, orgId.getOrgid());
        model.setDepartmentCode(deptCode);
        if(StringUtils.equals(deptCode, "AS") && StringUtils.isBlank(propNo) && StringUtils.isNotBlank(oldNo)) {
        	propNo = adjustmentEntryService.getPropNoByOldPropNo(oldNo, orgId.getOrgid(), deptId);
        }
    	Long billingMethodId = adjustmentEntryService.getBillMethodIdByPropNo(propNo, UserSession.getCurrent().getOrganisation().getOrgid(), deptCode);
    	LookUp billingMethodLookUp  = null;
    	try {
    		 billingMethodLookUp = CommonMasterUtility.getNonHierarchicalLookUpObject(billingMethodId, UserSession.getCurrent().getOrganisation());
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	if(billingMethodLookUp != null) {
    		billingMethod = billingMethodLookUp.getLookUpCode();
    	}
    	this.getModel().setBillingMethod(billingMethod);
    	if(StringUtils.isNotBlank(billingMethod) && StringUtils.equals(billingMethod, MainetConstants.FlagI)) {
    		flatNoList = new ArrayList<String>();
    		flatNoList = adjustmentEntryService.getFlatNoIdByPropNo(propNo, UserSession.getCurrent().getOrganisation().getOrgid(), deptCode);
    	}
    	model.setFlatNoList(flatNoList);
    	return flatNoList;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "serachAdjustmentDataIndividualBillMethod")
    public ModelAndView serachAdjustmentDataIndividualBillMethod(final HttpServletRequest request, @RequestParam("deptId") final Long deptId,
            @RequestParam("ccnOrPropNo")  String ccnNoOrPropNo, @RequestParam("oldNo")  String oldNo
            , @RequestParam("billingMeth")  String billingMeth , @RequestParam("flatNo")  String flatNo) {
        bindModel(request);
        final AdjustmentEntryModel model = getModel();
        model.setBill(null);
        model.setBillList(null);
        model.getAdjustmentDto().setDpDeptId(deptId);
        model.getAdjustmentDto().setLangId(UserSession.getCurrent().getLanguageId());
        model.getAdjustmentDto().setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
        model.getAdjustmentDto().setCreatedBy(UserSession.getCurrent().getEmployee().getEmpId());
        final Organisation orgId = UserSession.getCurrent().getOrganisation();
        String deptCode = tbDepartmentService.findDepartmentShortCodeByDeptId(deptId, orgId.getOrgid());
        model.setDepartmentCode(deptCode);
        if(StringUtils.equals(deptCode, "AS") && StringUtils.isBlank(ccnNoOrPropNo) && StringUtils.isNotBlank(oldNo)) {
        	String propNo = adjustmentEntryService.getPropNoByOldPropNo(oldNo, orgId.getOrgid(), deptId);
        	ccnNoOrPropNo = propNo;
        }
        model.getAdjustmentDto().setAdjRefNo(ccnNoOrPropNo);
        
        
        Long applicationId = adjustmentEntryRepository.getApplicationIdByAdjRefNoAndFlatNo(ccnNoOrPropNo, flatNo);
        
        
        WorkflowRequest workflowRequest = null;
        if(applicationId != null) {
        	 workflowRequest = workflowReqService
    				.getWorkflowRequestByAppIdOrRefId(applicationId, null, orgId.getOrgid());
        	
        }
        	if (workflowRequest != null && MainetConstants.WorkFlow.Status.PENDING.equalsIgnoreCase(workflowRequest.getStatus())) {
                model.addValidationError(ApplicationSession.getInstance().getMessage("adjustment.entry.process"));
    		}else {
    			  if(flatNo != null && flatNo != "" ) {
    		        	model.getAdjustmentDto().setFlatNo(flatNo);
    		        }
    		        if (model.validateData()) {
    		        	if(StringUtils.isNotBlank(billingMeth) && StringUtils.equals(billingMeth, MainetConstants.FlagI)) {
    		        		model.getAdjustmentDto().setFlatNo(flatNo);
    		        		model.setBillList(adjustmentEntryService.fetchAllBillByPropNoAndFlatNo(deptId, ccnNoOrPropNo, flatNo, orgId.getOrgid()));
    		        	} else {
    		        		model.setBillList(adjustmentEntryService.fetchModuleWiseBills(deptId, ccnNoOrPropNo,
    		                        orgId.getOrgid()));
    		        	}
    		            
    					if (CollectionUtils.isEmpty(model.getBillList())) {
    		                model.addValidationError(ApplicationSession.getInstance().getMessage("adjustment.entry.invalid"));
    		            } else if ((model.getBillList().get(0).getBmIdno() <= 0) && (model.getBillList().size() == 1)) {
    		                final List<TbTaxMas> taxes = adjustmentEntryService.fetchAllModulewiseTaxes(deptId, orgId);
    		                List<TbBillDet> billdet = null;
    		                TbBillDet det = null;
    		                if ((taxes != null) && !taxes.isEmpty()) {
    		                    billdet = new ArrayList<>();
    		                    for (final TbTaxMas tax : taxes) {
    		                        det = new TbBillDet();
    		                        det.setTaxDesc(tax.getTaxDesc());
    		                        det.setTaxId(tax.getTaxId());
    		                        billdet.add(det);
    		                    }
    		                    model.setBill(new TbBillMas());
    		                    model.getBill().setTbWtBillDet(billdet);
    		                }
    		            } else {
    		            	model.setBill(model.getBillList().get(model.getBillList().size() - 1));
    		            	model.getAdjustmentDto().setFlatNo(model.getBill().getFlatNo());
    		            	model.setTotalBalanceAmount(0.0);
    		            	double totalBalance = 0.0;
    		                if ((model.getBill().getTbWtBillDet() != null) && !model.getBill().getTbWtBillDet().isEmpty()) {
    		                    for (final TbBillDet det : model.getBill().getTbWtBillDet()) {
    		                        det.setTaxDesc(tbTaxMasService.findTaxDescByTaxIdAndOrgId(det.getTaxId(), orgId.getOrgid()));
    		                        totalBalance += det.getBdCurBalTaxamt() + det.getBdPrvBalArramt();
    		                    }
    		                    model.setTotalBalanceAmount(Math.round(totalBalance * 100.0) / 100.0);
    		                }
    		            }
    					if (MainetConstants.DEPT_SHORT_NAME.PROPERTY.equals(deptCode)){
    		        		model.getAdjustmentDto().setServiceId(serviceMaster.getServiceIdByShortName(orgId.getOrgid(),
    		        				MainetConstants.ServiceShortCode.ADJUSTMENT_ENTRY));
    		        		model.getAdjustmentDto().setServiceFlag(MainetConstants.FlagA);//Application Service
    		    		}
    		        }
    		}
        	
		model.setTotalBalanceAmount(model.getTotalBalanceAmount());
        return defaultMyResult();
    }
    
    
    @RequestMapping(method = RequestMethod.POST, params = "addAmount")
    public ModelAndView addAdjutedAmount(final HttpServletRequest request) {
        bindModel(request);
        final AdjustmentEntryModel model = getModel();
        AtomicDouble totalAmount = new AtomicDouble(0);
        if(model.getAdjustmentDto() != null && CollectionUtils.isNotEmpty(model.getAdjustmentDto().getAdjDetailDto())) { 
        	model.getAdjustmentDto().getAdjDetailDto().forEach(detail ->{
        		totalAmount.addAndGet(detail.getAdjAmount());
        	});        	
        }
        model.setTotalAmountToAdjust(totalAmount.doubleValue());
		if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, model.getDepartmentCode())
				&& StringUtils.equals(model.getAdjustmentDto().getAdjType(), MainetConstants.FlagN)) {
			Optional<TbBillDet> asOnDatePenaltyExist = model.getBill().getTbWtBillDet().stream().filter(detailDto -> detailDto.getTaxId().equals(Long.valueOf(MainetConstants.Property.dummyPenaltyId))).findAny();
			if(!asOnDatePenaltyExist.isPresent()) {
				double totalPenalty = billMasterCommonService.calculatePenaltyInterest(model.getBillList(),
						UserSession.getCurrent().getOrganisation(), model.getAdjustmentDto().getDpDeptId(), "Y", null, "N",
						null, UserSession.getCurrent().getEmployee().getEmpId());
				if (totalPenalty > 0) {
					TbBillDet det = new TbBillDet();
					det.setTaxDesc(MainetConstants.Property.As_on_date_penalty);
					det.setBdCurBalTaxamt(totalPenalty);
					det.setTaxId(Long.valueOf(MainetConstants.Property.dummyPenaltyId));
					model.getBill().getTbWtBillDet().add(det);
					model.setTotalBalanceAmount(model.getTotalBalanceAmount() + totalPenalty);
				}
			}
		} else {
			model.getBill().getTbWtBillDet().removeIf(
					detail -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detail.getTaxId()));
			model.setTotalBalanceAmount(0.0);
			for (final TbBillDet det : model.getBill().getTbWtBillDet()) {
                model.setTotalBalanceAmount(model.getTotalBalanceAmount() + det.getBdCurBalTaxamt() + det.getBdPrvBalArramt());
            }
		}
		
        model.setTotalBalanceAmount(Math.round(model.getTotalBalanceAmount()));
        return defaultMyResult();
    }
    
    @RequestMapping(params = "showDetails", method = RequestMethod.POST)
    public ModelAndView defaultLoad(@RequestParam("appNo") final long applicationId, @RequestParam("taskId") final long serviceId,
            @RequestParam("actualTaskId") final long taskId, final HttpServletRequest httpServletRequest) throws Exception {
    	
    	LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" defaultLoad() method");
        sessionCleanup(httpServletRequest);
        fileUpload.sessionCleanUpForFileUpload();
        
        final AdjustmentEntryModel model = getModel();
        long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
        getModel().getWorkflowActionDto().setApplicationId(applicationId);
        getModel().getWorkflowActionDto().setTaskId(taskId);
        getModel().getWorkflowActionDto().setServiceId(serviceId);
        getModel().getWorkflowActionDto().setOrgId(orgId);
        
        getModel().setLastChecker(iWorkFlowTypeService.isLastTaskInCheckerTaskList(taskId));
        
        LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" getAdjustmentDetailsByApplicationId() method");
        AdjustmentMasterDTO adjMasterDTO = adjustmentEntryService.getAdjustmentDetailsByApplicationId(applicationId,orgId);
        LOGGER.info("End -> "+this.getClass().getSimpleName()+" getAdjustmentDetailsByApplicationId() method");
        
        if(adjMasterDTO != null) {
        	LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" fetchModuleWiseBills() method");
        	List<TbBillMas> billMasList = null;
        	if(StringUtils.isNotBlank(adjMasterDTO.getFlatNo())) {
        		billMasList = adjustmentEntryService.fetchAllBillByPropNoAndFlatNo(adjMasterDTO.getDpDeptId(), adjMasterDTO.getAdjRefNo(), adjMasterDTO.getFlatNo(), orgId);
        	} else {
        		billMasList = adjustmentEntryService.fetchModuleWiseBills(adjMasterDTO.getDpDeptId(), adjMasterDTO.getAdjRefNo(), orgId);        		
        	}
        	LOGGER.info("End -> "+this.getClass().getSimpleName()+" fetchModuleWiseBills() method");
        	if(CollectionUtils.isNotEmpty(billMasList)) {
        		
        		model.setBillList(billMasList);
        		model.setTotalBalanceAmount(0.0);
        		model.setBill(model.getBillList().get(model.getBillList().size() - 1));
        		if ((model.getBill().getTbWtBillDet() != null) && !model.getBill().getTbWtBillDet().isEmpty()) {
        			double totalBalance = 0.0;
        			for (final TbBillDet det : model.getBill().getTbWtBillDet()) {
        				det.setTaxDesc(tbTaxMasService.findTaxDescByTaxIdAndOrgId(det.getTaxId(), orgId));
        				totalBalance += det.getBdCurBalTaxamt() + det.getBdPrvBalArramt();
        			}
        			model.setTotalBalanceAmount(Math.round(totalBalance * 100.0) / 100.0);
        		}
        	}
        	model.setTotalAmountToAdjust(0.0);
        	
        	List<Long> taxIdList = adjMasterDTO.getAdjDetailDto().stream().map(adjust -> adjust.getTaxId()).distinct().collect(Collectors.toList());
			Map<Long, AdjustmentDetailDTO> taxWiseMap = new HashMap<Long, AdjustmentDetailDTO>();
			taxIdList.forEach(taxId ->{
				OptionalLong adjDetId = adjMasterDTO.getAdjDetailDto().stream().filter(detail -> detail.getTaxId().equals(taxId)).collect(Collectors.toList()).stream().mapToLong(AdjustmentDetailDTO::getAdjDetId).min();
				List<AdjustmentDetailDTO> adjustDet = adjMasterDTO.getAdjDetailDto().stream().filter(detail -> detail.getAdjDetId() == (adjDetId.getAsLong())).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(adjustDet)) {
					taxWiseMap.put(taxId, adjustDet.get(0));	
				}
			});
			
			
			for (Map.Entry<Long, AdjustmentDetailDTO> adjustDetailMap : taxWiseMap.entrySet()) {
				AdjustmentDetailDTO value = adjustDetailMap.getValue();
				model.setTotalAmountToAdjust(model.getTotalAmountToAdjust() + value.getAdjAmount());
			}
			
			/*
			 * for(AdjustmentDetailDTO adjDetailDto : adjMasterDTO.getAdjDetailDto()) {
			 * if(StringUtils.equals(MainetConstants.FlagY,
			 * adjDetailDto.getAdjAdjustedFlag())) {
			 * model.setTotalAmountToAdjust(model.getTotalAmountToAdjust() +
			 * adjDetailDto.getAdjAmount()); } }
			 */
            model.setAdjustmentDto(adjMasterDTO);
        }
        approvedRemarks(applicationId, orgId);
        model.setDocumentList(checklistVerificationService.getDocumentUploadedForAppId(applicationId, orgId));
        AtomicLong increment = new AtomicLong(1);
        if(CollectionUtils.isNotEmpty(model.getDocumentList())) {
        	model.getDocumentList().forEach(doc ->{
        		doc.setClmSrNo(increment.longValue());
        		if(UserSession.getCurrent().getLanguageId() == 1)
        			doc.setClmDescEngl("Adjustment Entry");
        		else
        			doc.setClmDesc(ApplicationSession.getInstance().getMessage("adjustment.entry.document.reg"));
             	increment.addAndGet(1);
             });
        }
        LOGGER.info("End -> "+this.getClass().getSimpleName()+" defaultLoad() method");
		return new ModelAndView("AdjustmentEntryAuth", MainetConstants.FORM_NAME, model);
        
        
    }
    
    private void approvedRemarks(final long applicationId, Long orgId) {
    	LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" approvedRemarks() method");
    	List<String> remarkList = ApplicationContextProvider.getApplicationContext().getBean(IWorkflowRepository.class)
    			.findWorkflowActionCommentsByAppIdAndOrgId(applicationId, orgId);
    	if(CollectionUtils.isNotEmpty(remarkList)) {
			List<String> remarks = remarkList.stream().filter(remark -> StringUtils.isNotBlank(remark)).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(remarks)) {
				getModel().setRemarks(StringUtils.join(remarks, ","));
			}
		}
    	LOGGER.info("End -> "+this.getClass().getSimpleName()+" approvedRemarks() method");
	}
    
    @RequestMapping(params = "saveAuthorization", method = RequestMethod.POST)
    public ModelAndView saveAuthorization(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        getModel().bind(httpServletRequest);
        final AdjustmentEntryModel model = getModel();
        WorkflowTaskAction workFlowActionDto = model.getWorkflowActionDto();
        if (model.saveAuthorization() ) {
        	if (StringUtils.equals(workFlowActionDto.getDecision(), MainetConstants.WorkFlow.Decision.REJECTED)) {
        		return jsonResult(
						JsonViewObject.successResult(ApplicationSession.getInstance().getMessage("property.reject")));
        	}
        	return jsonResult(
					JsonViewObject.successResult(ApplicationSession.getInstance().getMessage("common.auth.save")));
        }
		return customDefaultMyResult("AdjustmentEntryAuth");
        
        
    }
}
