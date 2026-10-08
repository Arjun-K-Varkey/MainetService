package com.abm.mainet.common.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.abm.mainet.cfc.checklist.service.IChecklistVerificationService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.dao.IServiceMasterDAO;
import com.abm.mainet.common.domain.CFCApplicationAddressEntity;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.dto.ApplicantDetailDTO;
import com.abm.mainet.common.dto.WardZoneBlockDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dto.CfcApplicationMstDTO;
import com.abm.mainet.common.integration.dto.CommonServiceRequestDTO;
import com.abm.mainet.common.integration.payment.dao.PaymentDAO;
import com.abm.mainet.common.integration.payment.dto.PaymentTransactionMasDTO;
import com.abm.mainet.common.integration.payment.entity.PGBankDetail;
import com.abm.mainet.common.integration.payment.entity.PGBankParameter;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.master.repository.TbCfcApplicationMstJpaRepository;
import com.abm.mainet.common.master.repository.TbDepartmentJpaRepository;
import com.abm.mainet.common.model.mapping.CommonModelMapper;
import com.abm.mainet.common.repository.ServiceMasterRepository;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.EncryptionAndDecryptionAapleSarkar;
import com.abm.mainet.common.utility.GetePayPaymentEncyption;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.SeqGenFunctionUtility;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.domain.WorkflowMas;
import com.abm.mainet.common.workflow.dto.ApplicationMetadata;
import com.abm.mainet.common.workflow.dto.TaskAssignment;
import com.abm.mainet.common.workflow.dto.WorkflowProcessParameter;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkflowExecutionService;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;
import com.abm.mainet.common.workflow.service.IWorkflowTyepResolverService;

@Service
public class CommonServiceImpl implements CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Resource
    private CommonModelMapper serviceMapper;

    @Resource
    private ServiceMasterRepository serviceMasterRepository;

    @Resource
    private ServiceMasterService serviceMasterService;

    @Autowired
    ICFCApplicationMasterService cfcService;

    @Resource
    private SeqGenFunctionUtility seqGenFunctionUtility;

    @Resource
    private IWorkflowExecutionService workflowExecutionService;

    @Resource
    private IWorkflowTyepResolverService iWorkflowTyepResolverService;

    @Autowired
    private IChecklistVerificationService iChecklistVerificationService;

    @Autowired
    private MessageSource messageSource;

    @Resource
    private PaymentDAO paymentDAO;
    
    @Resource
	private IServiceMasterDAO iServiceMasterDAO;
    
    @Resource
    private TbDepartmentJpaRepository departmentRepository;
    
    @Autowired
    private IServiceMasterDAO serviceMasterDAO;
    
    @Autowired
	private TbCfcApplicationMstJpaRepository tbCfcRepo;
	@Resource
	private ICFCApplicationMasterService icfcApplicationMasterService;
	@Autowired
	private IWorkflowRequestService workflowRequestService;

    /*
     * (non-Javadoc)
     * @see com.abm.mainet.common.service.CommonService#initiateWorkflowfreeService(com.abm.mainet.common.workflow.dto.
     * ApplicationMetadata, com.abm.mainet.common.dto.ApplicantDetailDTO)
     */
    @Override
    public void initiateWorkflowfreeService(ApplicationMetadata applicationData,
            ApplicantDetailDTO applicantDetailsDto) {
        String checklistFlag = null;

        String processName = serviceMasterService.getProcessName(applicantDetailsDto.getServiceId(),
                applicationData.getOrgId());
        if (processName != null) {
            Organisation org = new Organisation();
            org.setOrgid(applicationData.getOrgId());
            ServiceMaster serviceMaster = serviceMasterService.getServiceMaster(applicantDetailsDto.getServiceId(),
                    applicationData.getOrgId());
            WorkflowTaskAction workflowAction = new WorkflowTaskAction();
            if (applicationData.getIsCheckListApplicable() != null && applicationData.getIsCheckListApplicable().equals(true)) {
                List<Long> attachmentId = iChecklistVerificationService.fetchAttachmentIdByAppid(
                        applicationData.getApplicationId(),
                        applicationData.getOrgId());
                workflowAction.setAttachementId(attachmentId);
                final List<LookUp> lookUps = CommonMasterUtility.getLookUps("APL",
                        org);
                for (final LookUp lookUp : lookUps) {
                    if (serviceMaster.getSmChklstVerify() != null
                            && lookUp.getLookUpId() == serviceMaster.getSmChklstVerify()) {
                        checklistFlag = lookUp.getLookUpCode();
                        break;
                    }
                }
                if (MainetConstants.Common_Constant.ACTIVE_FLAG.equalsIgnoreCase(checklistFlag)
                        && MainetConstants.FlagY.equalsIgnoreCase(serviceMaster.getSmCheckListReq())) {
                    applicationData.setIsCheckListApplicable(true);
                } else if (MainetConstants.Common_Constant.ACTIVE_FLAG.equalsIgnoreCase(checklistFlag)
                        && MainetConstants.FlagN.equalsIgnoreCase(serviceMaster.getSmCheckListReq())) {
                    applicationData.setIsCheckListApplicable(false);
                } else if (MainetConstants.Common_Constant.NO.equalsIgnoreCase(checklistFlag)) {
                    applicationData.setIsCheckListApplicable(false);
                } else if (MainetConstants.CommonConstants.NA.equalsIgnoreCase(checklistFlag)) {
                    applicationData.setIsCheckListApplicable(false);
                }
            }
            boolean autoescalate = false;
         
            WorkflowMas mas=null;
            if(MainetConstants.WaterServiceShortCode.WATER_NEW_CONNECION.equals(serviceMaster.getSmShortdesc())) {
            	 mas = ApplicationContextProvider.getApplicationContext()
 	                    .getBean(IWorkflowTyepResolverService.class).resolveServiceWorkflowType(org.getOrgid(),
                    		applicantDetailsDto.getDepartmentId(), applicantDetailsDto.getServiceId(),applicantDetailsDto.getConnectonSize(),
                            null,  applicantDetailsDto.getDwzid1(), applicantDetailsDto.getDwzid2(), applicantDetailsDto.getDwzid3(),
                            applicantDetailsDto.getDwzid4(), applicantDetailsDto.getDwzid5());
            	 
            }else if(applicantDetailsDto.getConnectonSize() != null) {
				 mas = ApplicationContextProvider.getApplicationContext()
		                    .getBean(IWorkflowTyepResolverService.class).resolveServiceWorkflowType(org.getOrgid(),
		                    		applicantDetailsDto.getDepartmentId(), applicantDetailsDto.getServiceId(),applicantDetailsDto.getConnectonSize(),
		                            null, null, null, null, null, null);
            }else {
				 mas = iWorkflowTyepResolverService.resolveServiceWorkflowType(applicationData.getOrgId(),
	                    applicantDetailsDto.getDepartmentId(), applicantDetailsDto.getServiceId(),
	                    applicantDetailsDto.getDwzid1(), applicantDetailsDto.getDwzid2(), applicantDetailsDto.getDwzid3(),
	                    applicantDetailsDto.getDwzid4(), applicantDetailsDto.getDwzid5());
            }
  
            if (mas != null) {
                String code = CommonMasterUtility
                        .getNonHierarchicalLookUpObject(mas.getWorkflowMode(), mas.getOrganisation())
                        .getLookUpCode();
                if (code.equals("AE")) {
                    autoescalate = true;
                }
                applicationData.setWorkflowId(mas.getWfId());
            }
            WorkflowProcessParameter workflowdto = new WorkflowProcessParameter();
            applicationData.setIsAutoEscalate(autoescalate);
            applicationData.setIsFreeService(true);
            if (MainetConstants.Y_FLAG.equalsIgnoreCase(serviceMaster.getSmScrutinyApplicableFlag())) {
                applicationData.setIsScrutinyApplicable(true);
            } else {
                applicationData.setIsScrutinyApplicable(false);
            }
            TaskAssignment requesterTaskAssignment = setRequesterTask(applicationData,
                    applicantDetailsDto);
            
            if(serviceMaster != null && StringUtils.equals(MainetConstants.Property.MUT, serviceMaster.getSmShortdesc())) {
            	if(serviceMaster.getTbDepartment() != null && serviceMaster.getTbDepartment().getDpDeptid() != null) {
            		requesterTaskAssignment.setDeptId(serviceMaster.getTbDepartment().getDpDeptid());            		
            	}
            	if(serviceMaster.getSmServiceId() != null) {
            		requesterTaskAssignment.setServiceId(serviceMaster.getSmServiceId());            		
            	}
            	requesterTaskAssignment.setUrl(MainetConstants.Property.MUTATION_AUTHORIZATION_URL);
            }
            workflowdto.setRequesterTaskAssignment(requesterTaskAssignment);
            applicationData.setPaymentMode(MainetConstants.PAYMENT.FREE);
            workflowdto.setApplicationMetadata(applicationData);
            workflowdto
                    .setProcessName(processName);
    
            if(serviceMaster != null && StringUtils.equals(MainetConstants.MRM.MRM_DEPT_CODE,
                    serviceMaster.getTbDepartment().getDpDeptcode())){
                workflowAction.setApplicationId(applicationData.getApplicationId());
            }
            else{

                if(applicationData.getApplicationId() !=null && applicationData.getReferenceId() ==null) {

                 workflowAction.setApplicationId(applicationData.getApplicationId());
                 workflowAction.setReferenceId(applicationData.getApplicationId().toString());
                }else {

                workflowAction.setApplicationId(applicationData.getApplicationId());
                 workflowAction.setReferenceId(applicationData.getReferenceId());

                }
            }
            workflowAction.setDateOfAction(new Date());
            workflowAction.setDecision(MainetConstants.WorkFlow.Decision.SUBMITTED);
            workflowAction.setOrgId(applicationData.getOrgId());
            workflowAction.setEmpId(applicantDetailsDto.getUserId());
            workflowAction.setCreatedBy(applicantDetailsDto.getUserId());
            workflowAction.setCreatedDate(new Date());
            workflowdto.setWorkflowTaskAction(workflowAction);
            try {
                workflowExecutionService.initiateWorkflow(workflowdto);
            } catch (Exception e) {
                throw new FrameworkException("Exception while creating workflow for free services", e);
            }

        }
    }

    private TaskAssignment setRequesterTask(final ApplicationMetadata applicationData,
            final ApplicantDetailDTO applicantDetailsDto) {
        TaskAssignment assignment = new TaskAssignment();
        Set<String> actorIds = new HashSet<>();
        assignment
                .setActorId(applicantDetailsDto.getUserId() + MainetConstants.operator.COMMA + applicantDetailsDto.getMobileNo());
        actorIds.add(Long.toString(applicantDetailsDto.getUserId()));
        actorIds.add(applicantDetailsDto.getMobileNo());
        assignment.setActorIds(actorIds);
        assignment.setOrgId(applicationData.getOrgId());
        return assignment;
    }

    @Override
    public String findTitleDesc(final long titleId, final Organisation org) {

        String title = StringUtils.EMPTY;
        final List<LookUp> lookUps = CommonMasterUtility.getLookUps(PrefixConstants.LookUp.TITLE, org);
        if ((lookUps != null) && !lookUps.isEmpty()) {
            for (final LookUp lookUp : lookUps) {
                if (lookUp.getLookUpId() == titleId) {
                    title = lookUp.getLookUpDesc();
                    break;
                }
            }
        } else {
            throw new FrameworkException("No LookUps found for TTL Prefix for OrganizationId=" + org.getOrgid());
        }

        return title == null ? MainetConstants.BLANK : title;
    }

    @Override
    @Transactional
    public ApplicantDetailDTO populateApplicantInfo(ApplicantDetailDTO dto, final TbCfcApplicationMstEntity cfcEntity) {

        if (dto == null) {
            dto = new ApplicantDetailDTO();
        }
        final long applicationId = cfcEntity.getApmApplicationId();

        return initializeApplicantAddressDetail(initializeApplicationDetail(dto, cfcEntity),
                cfcService.getApplicantsDetails(applicationId));
    }

    private ApplicantDetailDTO initializeApplicationDetail(final ApplicantDetailDTO dto, final TbCfcApplicationMstEntity entity) {

        dto.setApplicantTitle(entity.getApmTitle());
        dto.setApplicantFirstName(entity.getApmFname());
        dto.setApplicantMiddleName(entity.getApmMname());
        dto.setApplicantLastName(entity.getApmLname());

        return dto;
    }

    /**
     * 
     * @param dto
     * @param addressEntity
     * @return
     */
    private ApplicantDetailDTO initializeApplicantAddressDetail(final ApplicantDetailDTO dto,
            final CFCApplicationAddressEntity addressEntity) {

        dto.setMobileNo(addressEntity.getApaMobilno());
        dto.setEmailId(addressEntity.getApaEmail());
        dto.setFloorNo(addressEntity.getApaFloor());
        dto.setBuildingName(addressEntity.getApaBldgnm());
        dto.setRoadName(addressEntity.getApaRoadnm());
        dto.setAreaName(addressEntity.getApaAreanm());
        if (addressEntity.getApaPincode() != null) {
            dto.setPinCode(Long.toString(addressEntity.getApaPincode()));
        }
        return dto;

    }

    @Override
    public WardZoneBlockDTO getWordZoneBlockByApplicationId(final Long applicationId,
            final Long serviceId, final Long orgId) {
        WardZoneBlockDTO wardZoneBlockDTO = new WardZoneBlockDTO();
        try {
            Class<?> clazz = null;
            Object dynamicServiceInstance = null;
            String serviceClassName = null;
            final ServiceMaster service = serviceMasterService.getServiceMaster(serviceId, orgId);
            serviceClassName = messageSource.getMessage(
                    service.getSmShortdesc(), new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);

            if (serviceClassName != null && !(MainetConstants.BLANK.equalsIgnoreCase(serviceClassName))) {
                clazz = ClassUtils.forName(serviceClassName,
                        ApplicationContextProvider.getApplicationContext()
                                .getClassLoader());

                dynamicServiceInstance = ApplicationContextProvider
                        .getApplicationContext().getAutowireCapableBeanFactory()
                        .autowire(clazz, 4, false);
                final Method method = ReflectionUtils.findMethod(clazz,
                        "getWordZoneBlockByApplicationId", new Class[] { Long.class,
                                Long.class, Long.class });
                wardZoneBlockDTO = (WardZoneBlockDTO) ReflectionUtils.invokeMethod(
                        method, dynamicServiceInstance, new Object[] { applicationId,
                                serviceId, orgId });
            }
        } catch (LinkageError | Exception e) {
            LOGGER.error("Exception in getWordZoneBlockByApplicationId for applicationId:" + applicationId, e);
        }
        return wardZoneBlockDTO;
    }

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.web.cfc.scrutiny.service.ScrutinyService# findServiceActionUrl(long)
     */
    @Override
    @Transactional
    public List<String> findServiceActionUrl(final long applicationId, final long orgId) {

        final long smServiceId = cfcService.getServiceIdByApplicationId(applicationId, orgId);
        List<String> paramList = null;
        paramList = serviceMasterService.getServiceActionUrlParams(smServiceId, orgId);
        paramList.add(MainetConstants.INDEX.ONE, Long.toString(smServiceId));
        return paramList;
    }

    @Override
    @Transactional
    public Map<Long, String> getAllPgBank(final long orgid) throws FrameworkException {
        return paymentDAO.getAllPgBank(orgid);
    }
    @Override
    @Transactional
    public Map<Long, String> getAllPgBankWithoutStatus(final long orgid) throws FrameworkException {
        return paymentDAO.getAllPgBankWithoutStatus(orgid);
    }
    @Override
    @Transactional(readOnly = true)
    public PaymentTransactionMas getOnlineTransactionMasByTrackId(final Long trackId) {
        return paymentDAO.getOnlineTransactionMasByTrackId(trackId);
    }

    @Override
    @Transactional(readOnly = true)
    public PGBankDetail getBankDetailByBankName(String cbBankName, Long orgId) {
        return paymentDAO.getBankDetailByBankName(cbBankName, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PGBankParameter> getMerchantMasterParamByPgId(Long PgId, long orgId) {
        return paymentDAO.getMerchantMasterParamByBankId(PgId, orgId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionMasDTO> getTransactionDetail(Long transactionId, String status, Date appDate, Long orgId){
    	List<PaymentTransactionMasDTO> dtoList = new ArrayList<PaymentTransactionMasDTO>();
    	List<Object[]> list = paymentDAO.getOnlineTransactionStatus(transactionId, status, appDate, orgId);
    	for (Object[] obj : list) {
			PaymentTransactionMasDTO dto = new PaymentTransactionMasDTO();
			if(obj[0] != null)
				dto.setTranCmId(Long.valueOf(obj[0].toString()));			
			if (obj[1] != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dto.setRefDate(dateFormat.format((Date)obj[1]));
			}
			if (obj[2] != null)
				dto.setReferenceId(obj[2].toString());
			if(obj[3] != null)
				dto.setFlatNo(obj[3].toString());
			if(obj[4] != null)
				dto.setRecvStatus(obj[4].toString());
			if(obj[5] != null)
				dto.setSendAmount((BigDecimal) (obj[5]));
			BeanUtils.copyProperties(dto, list);
			dtoList.add(dto);
		}
		return dtoList;
    }
	@Override
	public Boolean checkRejectedInTaskflow( Long applicationId,
			Long orgId, String shortCode) {
		Long serviceId = serviceMasterService.getServiceIdByShortName(orgId, shortCode);
		int count = tbCfcRepo.checkApplicationIsRejectedOrNot(applicationId, serviceId);
		if (count > 0) {
			return true;
		}
		return false;
	}
	@Override
	public void updateAapleSarakarAfterApproval(String appId, Long orgId) {
		
		
		TbCfcApplicationMstEntity tbCfcApplicationMstEntity = null;
		
		
		if(StringUtils.isNumeric(appId))
		{		
		tbCfcApplicationMstEntity= icfcApplicationMasterService.getCFCApplicationByApplicationId(Long.valueOf(appId),orgId);
		}
		else{
			tbCfcApplicationMstEntity= icfcApplicationMasterService.getCFCApplicationByRefNoOrAppNo(appId,null,orgId);
		}
		
		if (null != appId && null != tbCfcApplicationMstEntity.getAapleSarkarId()) {

			try {
				WorkflowRequest workflowRequests = workflowRequestService
						.getWorkflowRequestByAppIdOrRefId(tbCfcApplicationMstEntity.getApmApplicationId(), null, orgId);

				if (null == workflowRequests || workflowRequests.getStatus().equalsIgnoreCase("CLOSED") ) {
					String request = tbCfcApplicationMstEntity.getAapleSarkarId();
					String replace = request.replace("appId", appId.toString());
					String replace1 = replace.replace("appStatus", MainetConstants.Common_Constant.NUMBER.FOUR);
					String replace2 = replace1.replace("remark", MainetConstants.WorkFlow.Decision.APPROVED);
					String regexText = "\\bN\\b";
					String replaceAll = replace2.replaceAll(regexText, "Y");
					EncryptionAndDecryptionAapleSarkar encryptDecrypt = new EncryptionAndDecryptionAapleSarkar();
					encryptDecrypt.getUpdateStatus(replaceAll);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}}

	@Override
	public String getEncyptedKey(String data) {
		
		GetePayPaymentEncyption g1 = new GetePayPaymentEncyption();
		return g1.getEncValue(data);	
	}

	@Override
	public CfcApplicationMstDTO getcommonServiceRequestDTOByTokenNumber(String tokenNumber,Long orgId) {
		// TODO Auto-generated method stub
		
		CfcApplicationMstDTO obj1= new CfcApplicationMstDTO();
		//StringBuilder fullName = new StringBuilder();
		
		TbCfcApplicationMstEntity tbCfcApplicationMstEntity = null;
		
		
		tbCfcApplicationMstEntity = icfcApplicationMasterService
					.getCFCApplicationByApplicationId(Long.valueOf(tokenNumber), orgId);
		
		obj1.setApplicationId(tbCfcApplicationMstEntity.getApmApplicationId().toString());
		obj1.setApplicationDate(tbCfcApplicationMstEntity.getApmApplicationDate().toString());
		
		if(null!=tbCfcApplicationMstEntity.getApmApplicationId()){
			
		}
		
		
		if(null!=tbCfcApplicationMstEntity.getTbServicesMst()){
			
			ServiceMaster serviceMasterByServiceId = iServiceMasterDAO.getServiceMasterByServiceId(tbCfcApplicationMstEntity.getTbServicesMst().getSmServiceId(), orgId);
			
			String fetchDepartmentDescById = departmentRepository.fetchDepartmentDescById(serviceMasterByServiceId.getTbDepartment().getDpDeptid());
			String serviceNameByServiceIdLangId = serviceMasterDAO.getServiceNameByServiceIdLangId(tbCfcApplicationMstEntity.getTbServicesMst().getSmServiceId(),1);
			
			
			if(StringUtils.equalsIgnoreCase(MainetConstants.WorkFlow.Process.CARE, serviceMasterByServiceId.getSmShortdesc())){
				obj1.setApplicationId(tbCfcApplicationMstEntity.getRefNo());
			}
			
			obj1.setDeptName(fetchDepartmentDescById);
			obj1.setServiceName(serviceNameByServiceIdLangId);
		}
		
		if(null!=tbCfcApplicationMstEntity.getAapleSarkarId()){
			String param[] = tbCfcApplicationMstEntity.getAapleSarkarId().split("\\|");
			obj1.setMobileNo(param[16]);
			obj1.setApplicantName(param[17]);
		}
		
		return obj1;
	}
	
}
