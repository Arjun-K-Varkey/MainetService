package com.abm.mainet.bill.ui.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.bill.repository.AdjustmentBillDetailMappingRepository;
import com.abm.mainet.bill.repository.AdjustmentEntryRepository;
import com.abm.mainet.bill.service.AdjustmentEntryService;
import com.abm.mainet.bill.service.BillMasterCommonService;
import com.abm.mainet.cfc.challan.domain.AdjustmentBillDetailMappingEntity;
import com.abm.mainet.cfc.challan.domain.AdjustmentMasterEntity;
import com.abm.mainet.cfc.challan.dto.AdjustmentDetailDTO;
import com.abm.mainet.cfc.challan.dto.AdjustmentMasterDTO;
import com.abm.mainet.cfc.checklist.service.IChecklistVerificationService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.integration.dms.fileUpload.FileUploadUtility;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.integration.dto.DocumentDetailsVO;
import com.abm.mainet.common.integration.dto.RequestDTO;
import com.abm.mainet.common.integration.dto.TbBillDet;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.master.dto.TbDepartment;
import com.abm.mainet.common.master.dto.TbTaxMas;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbTaxMasService;
import com.abm.mainet.common.service.ApplicationService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.user.activity.dto.UserActivityDTO;
import com.abm.mainet.common.user.activity.service.UserActivityService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkflowActionService;
import com.abm.mainet.common.workflow.ui.validator.CheckerActionValidator;

/**
 * @author Rahul.Yadav
 *
 */
@Component
@Scope("session")
public class AdjustmentEntryModel extends AbstractFormModel {

    private static final long serialVersionUID = -4255932371514311593L;

    private static final Logger LOGGER = Logger.getLogger(AdjustmentEntryModel.class);
    
    private List<TbBillMas> billList = null;

    private AdjustmentMasterDTO adjustmentDto = null;

    private List<TbDepartment> department = null;

    private List<AdjustmentMasterDTO> adjustmentHistoryDto = null;

    private String historyMsg = null;

    private TbBillMas bill;
    
    private String totalAdjValue;
    
    private String billingMethod;
    
    private List<String> flatNoList;

    @Resource
    private AdjustmentEntryService adjustmentEntryService;
    
    @Resource
    private IFileUploadService fileUpload;
    @Resource
	private TbTaxMasService tbTaxMasService;
    
    @Resource
    private BillMasterCommonService billMasterCommonService;

    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private IWorkflowActionService iWorkflowActionService;
    
    @Autowired
    private IChecklistVerificationService iChecklistVerificationService;
    
    @Autowired
    private AdjustmentEntryRepository adjustmentEntryRepository;
    
    @Autowired
    private AdjustmentBillDetailMappingRepository adjustmentBillDetailMappingRepository;
    
    private String address;

    private String bmCcnOwner;
    
    private double totalBalanceAmount;
    
    private double totalAmountToAdjust;
    
    private String flatNo;
    
    private boolean lastChecker;
    
    private String remarks;
    
    private List<CFCAttachment> documentList = new ArrayList<>();
    
    @Override
    public boolean saveForm() {
    	
    	/*
    	 * User Story #130566
    	 */
    
    	
    	List<DocumentDetailsVO> docs = new ArrayList<>(0);
    	
    	 if ((FileUploadUtility.getCurrent().getFileMap() != null)
                 && !FileUploadUtility.getCurrent().getFileMap().isEmpty()) {
             for (final Map.Entry<Long, Set<File>> entry : FileUploadUtility.getCurrent().getFileMap().entrySet()) {
                 final List<File> list = new ArrayList<>(entry.getValue());
                 for (final File file : list) {
                     try {
                         DocumentDetailsVO d = new DocumentDetailsVO();
                         final Base64 base64 = new Base64();
                         final String bytestring = base64.encodeToString(FileUtils.readFileToByteArray(file));
                         d.setDocumentByteCode(bytestring);
                         d.setDocumentName(file.getName());
                         docs.add(d);
                     } catch (final IOException e) {
                     }
                 }
             }
         } else {
             addValidationError(ApplicationSession.getInstance().getMessage("adjustment.entry.documentup.msg"));
             return false;
         }
    	 
    	 
        if (!validateData(getAdjustmentDto())) {
        	if (getAdjustmentDto().getAdjType().equals(MainetConstants.FlagB)) 
    			setDataForBozaEntry();
        	
        	String lastBillFlag = null;
        	LookUp lastBillActive = null;
        	try {
        		lastBillActive = CommonMasterUtility.getValueFromPrefixLookUp("LB",
                        "PAE", UserSession.getCurrent().getOrganisation());
        	}catch (Exception e) {
        		
			}
        	if(lastBillActive != null) {
        		lastBillFlag = lastBillActive.getDefaultVal();
        	}
			if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, getDepartmentCode())
					&& StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagN)) {
				//adjustPnealtyInexistRow();
				getBillList().forEach(billMas -> {
					billMas.getTbWtBillDet()
							.removeIf(detail -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detail.getTaxId()));
				});
				double totalPenalty = billMasterCommonService.calculatePenaltyInterest(getBillList(),
						UserSession.getCurrent().getOrganisation(), getAdjustmentDto().getDpDeptId(), "Y", null, "N",
						"Y", UserSession.getCurrent().getEmployee().getEmpId());
			}else {
				getBillList().forEach(billMas -> {
					billMas.getTbWtBillDet()
							.removeIf(detail -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detail.getTaxId()));
				});
			}
			RequestDTO dto = new RequestDTO();
			Long propertyPenaltIntId = getPropertyPenaltIntId(UserSession.getCurrent().getOrganisation());
			if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, getDepartmentCode())) {
				//RequestDTO reqDto = new RequestDTO();
				setRequestApplicantDetails(dto, billList, adjustmentDto);
				if(StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagN) && getBillList().get(getBillList().size() - 1).getBmTotalOutstanding() == 0){
					List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
					lastBillList.add(getBillList().get(getBillList().size() - 1));
					setBillList(lastBillList);
				}
			}
			if (PrefixConstants.IsLookUp.STATUS.YES.equals(lastBillFlag)
					&& StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagP)) {
				List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
				lastBillList.add(getBillList().get(getBillList().size() - 1));
				adjustmentEntryService.saveAdjustmentEntryData(lastBillList, getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        	}else if(StringUtils.equals(getAdjustmentDto().getAdjType(), "E")) {
        		List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
				lastBillList.add(getBillList().get(getBillList().size() - 1));
        		adjustmentEntryService.saveAdjustmentEntryData(lastBillList, getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        	}
			else {
        		adjustmentEntryService.saveAdjustmentEntryData(getBillList(), getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        	}
			
            
			dto.setDeptId(ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class)
					.getDepartmentIdByDeptCode("COM"));
			dto.setServiceId(ApplicationContextProvider.getApplicationContext().getBean(ServiceMasterService.class)
					.getServiceIdByShortName(UserSession.getCurrent().getOrganisation().getOrgid(), "MTD"));
			if(getAdjustmentDto().getFlatNo() != null && getAdjustmentDto().getFlatNo() !="" && StringUtils.equals(getBillingMethod(), "I") ) {
			dto.setReferenceId(getAdjustmentDto().getAdjRefNo()+"_"+getAdjustmentDto().getFlatNo());
			}
			else
			{
			dto.setReferenceId(getAdjustmentDto().getAdjRefNo());
			}
			
            dto.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
            dto.setUserId(UserSession.getCurrent().getEmployee().getEmpId());
            dto.setLangId(Long.valueOf(UserSession.getCurrent().getLanguageId()));
            dto.setApplicationId(adjustmentDto.getApplicationId());
            fileUpload.doFileUpload(docs, dto);
            if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, getDepartmentCode())) {
            	saveUserActivity(UserSession.getCurrent());
            	setSuccessMessage(ApplicationSession.getInstance().getMessage("adjustment.entry.success.msg.appNo")+""+adjustmentDto.getApplicationId());
            } else {
            	setSuccessMessage(ApplicationSession.getInstance().getMessage("adjustment.entry.success.msg"));
            }
            
            return true;
        }
        return false;
    }

    private void saveUserActivity(UserSession session) {
		UserActivityDTO dto = new UserActivityDTO();
		dto.setApplicationId(adjustmentDto.getApplicationId());
		dto.setCreatedBy(session.getEmployee().getEmpId());
		dto.setCreatedDate(new Date());
		dto.setLgIpMac(session.getEmployee().getEmppiservername());
		dto.setOrgId(session.getOrganisation().getOrgid());
		dto.setServiceId(adjustmentDto.getServiceId());
		ApplicationContextProvider.getApplicationContext().getBean(UserActivityService.class).saveUserActivity(dto);
	}
    
    private void setRequestApplicantDetails(RequestDTO reqDto, List<TbBillMas> billlist, AdjustmentMasterDTO adjustmentDto) {
		reqDto.setfName(billlist.get(0).getBmCcnOwner());
    	
    	reqDto.setDeptId(adjustmentDto.getDpDeptId());
    	reqDto.setPayStatus(MainetConstants.PAYMENT.FREE);
    	
    	reqDto.setOrgId(adjustmentDto.getOrgId());
    	reqDto.setServiceId(adjustmentDto.getServiceId());
    	reqDto.setLangId(Long.valueOf(adjustmentDto.getLangId()));
    	reqDto.setUserId(adjustmentDto.getCreatedBy());
    	
    	reqDto.setReferenceId(adjustmentDto.getAdjRefNo());
    	final Long applicationNo = applicationService.createApplication(reqDto);
    	adjustmentDto.setApplicationId(applicationNo);
	}
    
    private boolean validateData(final AdjustmentMasterDTO adjustmentEntryDto) {
        int count = 0;
        if ((adjustmentEntryDto.getAdjType() == null) || adjustmentEntryDto.getAdjType().isEmpty()) {
            addValidationError(ApplicationSession.getInstance().getMessage("adjustment.type.select"));
            return true;
        } else {
            for (final AdjustmentDetailDTO dto : adjustmentEntryDto.getAdjDetailDto()) {
                if (dto.getAdjAmount() > 0d) {
                    count++;
                    if (!adjustmentEntryDto.getAdjType().equals(MainetConstants.FlagW)&&((dto.getAdjRemark() == null) || dto.getAdjRemark().isEmpty())) {
                        addValidationError(ApplicationSession.getInstance().getMessage("adjustment.remark.enter"));
                        return true;
                    }
                    
					/*
					 * List<TbBillDet> billDetList = getBill().getTbWtBillDet().stream()
					 * .filter(billDet ->
					 * billDet.getTaxId().equals(dto.getTaxId())).collect(Collectors.toList());
					 */
					/*
					 * if(StringUtils.equals(MainetConstants.FlagN,adjustmentEntryDto.getAdjType())
					 * && dto.getAdjAmount() >
					 * (billDetList.get(0).getBdCurBalTaxamt()+billDetList.get(0).getBdPrvBalArramt(
					 * ))) { addValidationError(ApplicationSession.getInstance().getMessage(
					 * "adjustment.greater.than.balance.amount")); return true; }
					 */
                }
            }
            if (count <= 0) {
                addValidationError(ApplicationSession.getInstance().getMessage("adjustment.tax.select.one"));
                return true;
            }
        }
        return false;
    }

    public List<TbDepartment> getDepartment() {
        return department;
    }

    public void setDepartment(final List<TbDepartment> department) {
        this.department = department;
    }

    public AdjustmentMasterDTO getAdjustmentDto() {
        return adjustmentDto;
    }

    public void setAdjustmentDto(final AdjustmentMasterDTO adjustmentDto) {
        this.adjustmentDto = adjustmentDto;
    }

    public List<AdjustmentMasterDTO> getAdjustmentHistoryDto() {
        return adjustmentHistoryDto;
    }

    public void setAdjustmentHistoryDto(final List<AdjustmentMasterDTO> adjustmentHistoryDto) {
        this.adjustmentHistoryDto = adjustmentHistoryDto;
    }

    public boolean validateData() {
        boolean result = true;
        if ((getAdjustmentDto().getDpDeptId() == null) || (getAdjustmentDto().getDpDeptId() <= 0)) {
            addValidationError(ApplicationSession.getInstance().getMessage("adjustment.department.select"));
            result = false;
        }
        if ((getAdjustmentDto().getAdjRefNo() == null) || getAdjustmentDto().getAdjRefNo().isEmpty()) {
            addValidationError(ApplicationSession.getInstance().getMessage("adjustment.conno.enter"));
            result = false;
        }
        return result;
    }

    public String getHistoryMsg() {
        return historyMsg;
    }

    public void setHistoryMsg(final String historyMsg) {
        this.historyMsg = historyMsg;
    }

    public List<TbBillMas> getBillList() {
        return billList;
    }

    public void setBillList(final List<TbBillMas> billList) {
        this.billList = billList;
    }

    public TbBillMas getBill() {
        return bill;
    }

    public void setBill(final TbBillMas bill) {
        this.bill = bill;
    }

	public String getTotalAdjValue() {
		return totalAdjValue;
	}

	public void setTotalAdjValue(String totalAdjValue) {
		this.totalAdjValue = totalAdjValue;
	}
	public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }
    
    public List<String> getFlatNoList() {
        return flatNoList;
    }

    public void setFlatNoList(List<String> flatNoList) {
        this.flatNoList = flatNoList;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBmCcnOwner() {
		return bmCcnOwner;
	}

	public void setBmCcnOwner(String bmCcnOwner) {
		this.bmCcnOwner = bmCcnOwner;
	}

	
	public double getTotalBalanceAmount() {
		return totalBalanceAmount;
	}

	public void setTotalBalanceAmount(double totalBalanceAmount) {
		this.totalBalanceAmount = totalBalanceAmount;
	}

	public double getTotalAmountToAdjust() {
		return totalAmountToAdjust;
	}

	public void setTotalAmountToAdjust(double totalAmountToAdjust) {
		this.totalAmountToAdjust = totalAmountToAdjust;
	}
	
	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public boolean isLastChecker() {
		return lastChecker;
	}

	public void setLastChecker(boolean lastChecker) {
		this.lastChecker = lastChecker;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<CFCAttachment> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<CFCAttachment> documentList) {
		this.documentList = documentList;
	}

	private void setDataForBozaEntry() {
		List<TbTaxMas> taxList = new ArrayList<>();
		Long taxId = null;
		AdjustmentMasterDTO adjDto = getAdjustmentDto();
		TbBillDet billdet = new TbBillDet();
		taxList = tbTaxMasService.findTaxesByDescAndOrgId("Boja",
				UserSession.getCurrent().getOrganisation().getOrgid());
		if (CollectionUtils.isNotEmpty(taxList))
			taxId = taxList.get(0).getTaxId();
		;
		if (CollectionUtils.isNotEmpty(taxList)) {
			billdet.setTaxId(taxList.get(0).getTaxId());
			billdet.setTaxDesc(taxList.get(0).getTaxDesc());
			billdet.setBmIdno(getBillList().get(getBillList().size() - 1).getTbWtBillDet().get(0).getBmIdno());
			billdet.setCollSeq(taxList.get(0).getCollSeq());
			billdet.setTaxCategory(taxList.get(0).getTaxCategory1());
			billdet.setTaxSubCategory(taxList.get(0).getTaxCategory2());
		}
		if (CollectionUtils.isNotEmpty(getBillList()))
			getBillList().get(getBillList().size() - 1).getTbWtBillDet().add(billdet);

		for (final AdjustmentDetailDTO dto : adjDto.getAdjDetailDto()) {
			if ((adjDto.getAdjType().equals(MainetConstants.FlagB)) && (dto.getTaxId() == null || dto.getTaxId() == 0l))
				dto.setTaxId(taxId);
		}

	}
	
	private void adjustPnealtyInexistRow() {
		Organisation org = UserSession.getCurrent().getOrganisation();
		List<AdjustmentDetailDTO> asOnDatePenalty = getAdjustmentDto().getAdjDetailDto().stream()
				.filter(detailDto -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detailDto.getTaxId()))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(asOnDatePenalty)) {
			double adjAmount = asOnDatePenalty.get(0).getAdjAmount();
			if(adjAmount > 0) {
				Long intTaxId = getPropertyPenaltIntId(org);
				Optional<AdjustmentDetailDTO> interestTaxExist = getAdjustmentDto().getAdjDetailDto().stream().filter(detailDto -> detailDto.getTaxId().equals(intTaxId)).findAny();
				if(interestTaxExist.isPresent()) {
					getAdjustmentDto().getAdjDetailDto().forEach(detail ->{
						if(detail.getTaxId() != null && detail.getTaxId().equals(intTaxId)) {
							detail.setAdjAmount(detail.getAdjAmount() + adjAmount);
						}
					});
				}else {
					AdjustmentDetailDTO detailDto = new AdjustmentDetailDTO();
					detailDto.setTaxId(intTaxId);
					detailDto.setAdjAmount(adjAmount);
					detailDto.setAdjRemark(asOnDatePenalty.get(0).getAdjRemark());
					getAdjustmentDto().getAdjDetailDto().add(detailDto);
				}
			}
		}
		
	}

	private Long getPropertyPenaltIntId(Organisation org) {
		final LookUp chargeApplicableAt = CommonMasterUtility.getValueFromPrefixLookUp(
				PrefixConstants.NewWaterServiceConstants.BILL_RECEIPT, PrefixConstants.NewWaterServiceConstants.CAA,
				org);
		LookUp taxCategoryLookUp = CommonMasterUtility.getHieLookupByLookupCode("I", PrefixConstants.LookUpPrefix.TAC,
				1, org.getOrgid());
		LookUp taxSubCategoryLookUp = CommonMasterUtility.getHieLookupByLookupCode("MI",
				PrefixConstants.LookUpPrefix.TAC, 2, org.getOrgid());
		Long intTaxId = tbTaxMasService.getTaxId(chargeApplicableAt.getLookUpId(), org.getOrgid(),
				getAdjustmentDto().getDpDeptId(), taxCategoryLookUp.getLookUpId(), taxSubCategoryLookUp.getLookUpId());
		return intTaxId;
	}

	public boolean saveAuthorization() {
		
		LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" saveAuthorization() method");
		WorkflowTaskAction workFlowActionDto = getWorkflowActionDto();
        validateBean(workFlowActionDto, CheckerActionValidator.class);
        if (hasValidationErrors()) {
            return false;
        }
        try {
        	List<DocumentDetailsVO> docs = new ArrayList<>();
            DocumentDetailsVO document = new DocumentDetailsVO();
            docs.add(document);
            docs = fileUpload.prepareFileUpload(docs);
            RequestDTO reqDto = new RequestDTO();
            reqDto.setApplicationId((workFlowActionDto.getApplicationId()));
            if(StringUtils.isNotBlank(getAdjustmentDto().getFlatNo())) {
            	reqDto.setReferenceId(getAdjustmentDto().getAdjRefNo()+"_"+getAdjustmentDto().getFlatNo());
    		} else {
    			reqDto.setReferenceId(getAdjustmentDto().getAdjRefNo());    			
    		}
            reqDto.setServiceId(ApplicationContextProvider.getApplicationContext().getBean(ServiceMasterService.class)
					.getServiceIdByShortName(UserSession.getCurrent().getOrganisation().getOrgid(), "MTD"));
            reqDto.setDeptId(ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class)
					.getDepartmentIdByDeptCode("COM"));
            reqDto.setOrgId(workFlowActionDto.getOrgId());
            reqDto.setUserId(UserSession.getCurrent().getEmployee().getEmpId());
            fileUpload.doFileUpload(docs, reqDto);
            
        	if (StringUtils.equals(workFlowActionDto.getDecision(), MainetConstants.WorkFlow.Decision.REJECTED)) {
        		//delete adjustment data in case of rejection
        		AdjustmentMasterEntity adjustmentMasterEntity = adjustmentEntryRepository.getAdjustmentDetailsByApplicationId(workFlowActionDto.getApplicationId(),
        				workFlowActionDto.getOrgId());
        		if(adjustmentMasterEntity != null) {
        			List<Long> adjdIdList = adjustmentMasterEntity.getAdjDetail().stream().map(det -> det.getAdjDetId()).collect(Collectors.toList());
        			List<AdjustmentBillDetailMappingEntity> mappingEntities = adjustmentBillDetailMappingRepository.getAdjustmentDetailsByAdjId(adjdIdList,
        					workFlowActionDto.getOrgId());
        			adjustmentBillDetailMappingRepository.delete(mappingEntities);
        			adjustmentEntryRepository.delete(adjustmentMasterEntity);
        		}
        	} else if(isLastChecker()){
        		if (getAdjustmentDto().getAdjType().equals(MainetConstants.FlagB)) 
        			setDataForBozaEntry();
        		
        		String lastBillFlag = null;
        		LookUp lastBillActive = null;
        		try {
        			lastBillActive = CommonMasterUtility.getValueFromPrefixLookUp("LB",
        					"PAE", UserSession.getCurrent().getOrganisation());
        		}catch (Exception e) {
        			
        		}
        		if(lastBillActive != null) {
        			lastBillFlag = lastBillActive.getDefaultVal();
        		}
        		if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, getDepartmentCode())
        				&& StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagN)) {
        			getBillList().forEach(billMas -> {
        				billMas.getTbWtBillDet()
        				.removeIf(detail -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detail.getTaxId()));
        			});
					/*double totalPenalty = billMasterCommonService.calculatePenaltyInterest(getBillList(),
							UserSession.getCurrent().getOrganisation(), getAdjustmentDto().getDpDeptId(), "Y", null, "N",
							"Y", UserSession.getCurrent().getEmployee().getEmpId());*/
        		}else {
        			getBillList().forEach(billMas -> {
        				billMas.getTbWtBillDet()
        				.removeIf(detail -> Long.valueOf(MainetConstants.Property.dummyPenaltyId).equals(detail.getTaxId()));
        			});
        		}
        		
        		Long propertyPenaltIntId = getPropertyPenaltIntId(UserSession.getCurrent().getOrganisation());
        		
        		if(StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagN) 
        				&& getBillList().get(getBillList().size() - 1).getBmTotalOutstanding() == 0){
					List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
					lastBillList.add(getBillList().get(getBillList().size() - 1));
					setBillList(lastBillList);
				}
        		
        		LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" saveAdjustmentEntryData() method");
        		if (PrefixConstants.IsLookUp.STATUS.YES.equals(lastBillFlag)
        				&& StringUtils.equals(getAdjustmentDto().getAdjType(), MainetConstants.FlagP)) {
        			List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
        			lastBillList.add(getBillList().get(getBillList().size() - 1));
        			adjustmentEntryService.saveAdjustmentEntryData(lastBillList, getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),
        					propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        		}else if(StringUtils.equals(getAdjustmentDto().getAdjType(), "E")) {
        			List<TbBillMas> lastBillList = new ArrayList<TbBillMas>();
        			lastBillList.add(getBillList().get(getBillList().size() - 1));
        			adjustmentEntryService.saveAdjustmentEntryData(lastBillList, getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),
        					propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        		}
        		else {
        			List<Long> taxIdList = getAdjustmentDto().getAdjDetailDto().stream().map(adjust -> adjust.getTaxId()).distinct().collect(Collectors.toList());
        			Map<Long, AdjustmentDetailDTO> taxWiseMap = new HashMap<Long, AdjustmentDetailDTO>();
        			taxIdList.forEach(taxId ->{
        				OptionalLong adjDetId = getAdjustmentDto().getAdjDetailDto().stream().filter(detail -> detail.getTaxId().equals(taxId)).collect(Collectors.toList()).stream().mapToLong(AdjustmentDetailDTO::getAdjDetId).min();
        				List<AdjustmentDetailDTO> adjustDet = getAdjustmentDto().getAdjDetailDto().stream().filter(detail -> detail.getAdjDetId() == (adjDetId.getAsLong())).collect(Collectors.toList());
        				if(CollectionUtils.isNotEmpty(adjustDet)) {
        					taxWiseMap.put(taxId, adjustDet.get(0));	
        				}
        			});
        			getAdjustmentDto().getAdjDetailDto().clear();
        			
        			for (Map.Entry<Long, AdjustmentDetailDTO> adjustDetailMap : taxWiseMap.entrySet()) {
        				getAdjustmentDto().getAdjDetailDto().add(adjustDetailMap.getValue());
					}
        			
        			
        			adjustmentEntryService.saveAdjustmentEntryData(getBillList(), getAdjustmentDto(),Long.valueOf(MainetConstants.Property.dummyPenaltyId),
        					propertyPenaltIntId,UserSession.getCurrent().getOrganisation(),UserSession.getCurrent().getEmployee().getEmpId());
        		}
        		LOGGER.info("End -> "+this.getClass().getSimpleName()+" saveAdjustmentEntryData() method");
        	}
        	
        	updateWorkFlow(workFlowActionDto, UserSession.getCurrent().getEmployee());
        	
        	LOGGER.info("End -> "+this.getClass().getSimpleName()+" saveAuthorization() method");
        	return true;
        	
		} catch (Exception e) {
			throw new FrameworkException("Exception occured while save Authorization entry : ", e);
		}
    }
	
	private void updateWorkFlow(WorkflowTaskAction workFlowActionDto, Employee emp) {
		LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" updateWorkFlow() method");
		List<Long> attachementId = iChecklistVerificationService.fetchAttachmentIdByAppid(workFlowActionDto.getApplicationId(), workFlowActionDto.getOrgId());
        workFlowActionDto.setAttachementId(attachementId);
		iWorkflowActionService.updateWorkFlow(workFlowActionDto, emp, workFlowActionDto.getOrgId(), workFlowActionDto.getServiceId());
		LOGGER.info("End -> "+this.getClass().getSimpleName()+" updateWorkFlow() method");
	}

}
