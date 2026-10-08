package com.abm.mainet.common.service;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.abm.mainet.cfc.loi.service.TbLoiMasService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.ICFCApplicationAddressDAO;
import com.abm.mainet.common.dao.ICFCApplicationMasterDAO;
import com.abm.mainet.common.dao.ICFCApplicationStatusDao;
import com.abm.mainet.common.domain.CFCApplicationAddressEntity;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.dto.ApplicationStatusDTO;
import com.abm.mainet.common.dto.CFCApplicationStatusDto;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.master.repository.TbCfcApplicationMstJpaRepository;
import com.abm.mainet.common.repository.ServiceMasterRepository;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.domain.WorkflowMas;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.service.IWorkFlowTypeService;
import com.abm.mainet.common.workflow.service.IWorkflowActionService;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;

/**
 * @author Rajendra.Bhujbal
 *
 */
@Service

public class CFCApplicationMasterService implements ICFCApplicationMasterService {

	private static final Logger logger = Logger.getLogger(CFCApplicationMasterService.class);

	@Autowired
	private ICFCApplicationMasterDAO cfcApplicationMasterDAO;

	@Autowired
	private IWorkflowActionService workflowActionService;

	@Autowired
	private TbCfcApplicationMstJpaRepository cfcApplicationMstJpaRepository;

	@Autowired
	private ICFCApplicationStatusDao cfcAppllicationStatusDao;

	@Autowired
	private IWorkflowRequestService workflowRequestService;
	
	@Autowired
	private IWorkFlowTypeService iWorkFlowTypeService;
	
	@Resource
    private ServiceMasterRepository serviceMasterRepository;
	
	@Autowired
    private ICFCApplicationAddressDAO iCFCApplicationAddressDAO;
	@Autowired
	private TbLoiMasService tbLoiMasService;
	
	@Override
	@Transactional
	public long getServiceIdByApplicationId(final long apmApplicationId, final long orgId) {
		return cfcApplicationMasterDAO.getServiceIdByApplicationId(apmApplicationId, orgId);
	}

	@Override
	@Transactional
	public CFCApplicationAddressEntity getApplicantsDetails(final long apmApplicationId) {
		return cfcApplicationMasterDAO.getApplicantsDetailsDao(apmApplicationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.service.ICFCApplicationMasterService#
	 * getCFCApplicationByApplicationId(long, long)
	 */
	@Override
	@Transactional
	public TbCfcApplicationMstEntity getCFCApplicationByApplicationId(final long applicationId, final long orgid) {
		final TbCfcApplicationMstEntity cfcMaster = cfcApplicationMasterDAO
				.getCFCApplicationByApplicationId(applicationId, orgid);
		if (cfcMaster != null) {
			cfcMaster.getTbServicesMst().getSmServiceId();
			cfcMaster.getTbOrganisation().getOrgid();
		}
		return cfcMaster;
	}

	@Override
	@Transactional
	public void updateCFCApplicationStatus(final String status, final long applicationId) {
		cfcApplicationMasterDAO.updateCFCApplicationStatus(status, applicationId);
	}

	@Override
	public TbCfcApplicationMstEntity getCFCApplicationByRefNoOrAppNo(String refNumber, Long applicationId, Long orgid) {
		TbCfcApplicationMstEntity entity = new TbCfcApplicationMstEntity();
		try {
			entity = cfcApplicationMasterDAO.getCFCApplicationByRefNoOrAppNo(refNumber, applicationId, orgid);
		} catch (Exception e) {
			logger.error("Exception Occur while fetching Data of Cfc Application Master :" + e);
		}
		return entity;
	}

	@Override
	public List<TbCfcApplicationMstEntity> fetchCfcApplicationByServiceIdandMobileNo(List<Long> serviceIds, Long orgId,
			String mobileNo) {
		return cfcApplicationMstJpaRepository.fetchCfcApplicationsByIds(serviceIds, orgId, mobileNo);
	}

	@Override
	public List<TbCfcApplicationMstEntity> fetchCfcApplicationsByServiceIds(List<Long> serviceIds, Long orgId) {
		return cfcApplicationMstJpaRepository.fetchCfcApplicationsByServiceIds(serviceIds, orgId);
	}

	@Override
	@Transactional
	public List<ApplicationStatusDTO> getApplicationStatusDtos(CFCApplicationStatusDto cfcApplicationStatusDto,int langId) {
		
		List<ApplicationStatusDTO> applicationStatusDTOs = new ArrayList<ApplicationStatusDTO>();
		
		List<TbCfcApplicationMstEntity> result = cfcAppllicationStatusDao
				.getCFCApplicationEtites(cfcApplicationStatusDto);
		if(CollectionUtils.isNotEmpty(result)){
			CFCApplicationAddressEntity cfcAddressEntity = iCFCApplicationAddressDAO.getApplicationAddressByAppId(result.get(0).getApmApplicationId(),UserSession.getCurrent().getOrganisation().getOrgid());
			 String serviceCode = serviceMasterRepository.getServiceShortCode(result.get(0).getTbServicesMst().getSmServiceId(),
						UserSession.getCurrent().getOrganisation().getOrgid());
		

		for (TbCfcApplicationMstEntity application : result) {
			ApplicationStatusDTO applicationStatusDTO = new ApplicationStatusDTO(); //
			if (application.getTbServicesMst() != null) {
				if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
					applicationStatusDTO.setServiceName(application.getTbServicesMst().getSmServiceName());
				else 
				    applicationStatusDTO.setServiceNameReg(application.getTbServicesMst().getSmServiceNameMar());
			//D#127888	7
			if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
					applicationStatusDTO.setDeptName(application.getTbServicesMst().getTbDepartment().getDpDeptdesc());
				else
					applicationStatusDTO.setDeptName(application.getTbServicesMst().getTbDepartment().getDpNameMar());
			applicationStatusDTO.setDeptId(application.getTbServicesMst().getTbDepartment().getDpDeptid());
			}
			if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
				applicationStatusDTO.setOrganizationName(application.getTbOrganisation().getONlsOrgname());
			else
		    	applicationStatusDTO.setOrganizationNameReg(application.getTbOrganisation().getONlsOrgnameMar());
			//applicationStatusDTO.setDate(application.getApmApplicationDate());
			applicationStatusDTO.setApplicationId(application.getApmApplicationId());
			applicationStatusDTO.setFormattedDate(
					Utility.dateToString(application.getApmApplicationDate(), MainetConstants.DATE_FORMAT));
		  if(StringUtils.isNotEmpty(application.getApmFname()) && StringUtils.isNotEmpty(application.getApmLname()))
			applicationStatusDTO.setEmpName(application.getApmFname().concat(" ").concat(application.getApmLname()));
		  else if(StringUtils.isNotEmpty(application.getApmFname()))
		  {
			  applicationStatusDTO.setEmpName(application.getApmFname());
		  }
		  
		  if(application.getRefNo()!=null && StringUtils.isNotEmpty(application.getRefNo()))
		  {
			  applicationStatusDTO.setReferenceNo(application.getRefNo());
		  }
		  if(StringUtils.equalsIgnoreCase(application.getTbServicesMst().getTbDepartment().getDpDeptcode(), MainetConstants.Dms.TRADE_DEPT)) {
			  List<String> trdLicNo = tbLoiMasService
		                .getLicNumber(application.getApmApplicationId());
		        // adding code for handling AIOB exception
		        if (CollectionUtils.isNotEmpty(trdLicNo)) {
		        	String licenseNo = trdLicNo.get(0);
		            if (licenseNo != null) {
		                applicationStatusDTO.setReferenceNo(licenseNo);
		            }
		        }
		  }
		  
		  try {
				WorkflowRequest workflowRequest = workflowRequestService
						.findByApplicationId(application.getApmApplicationId());
				if (workflowRequest != null) {
					//120225
					if(langId == MainetConstants.DEFAULT_LANGUAGE_ID)
						applicationStatusDTO.setStatus(workflowRequest.getStatus());
					else
						if(workflowRequest.getStatus().equals(MainetConstants.WorkFlow.Status.PENDING))					
							applicationStatusDTO.setStatus(ApplicationSession.getInstance().getMessage("CFC.app.status.pending"));
						else 
						    applicationStatusDTO.setStatus(ApplicationSession.getInstance().getMessage("CFC.app.status.closed"));
					     //#151629	
					 	if (workflowRequest.getStatus().equals(MainetConstants.WorkFlow.Status.PENDING)) {
					 		if (application.getTbServicesMst() != null && application.getTbServicesMst().getSmServiceDuration() != null) {
							  Date dueDate = Utility.getAddedDateBy2(application.getApmApplicationDate(), application.getTbServicesMst().getSmServiceDuration().intValue());
							  applicationStatusDTO.setDueDate(Utility.dateToString(dueDate, MainetConstants.DATE_FORMAT));
					 		  }else
					 			 applicationStatusDTO.setDueDate(Utility.dateToString(application.getApmApplicationDate(), MainetConstants.DATE_FORMAT));  
							  }
					 	else if(workflowRequest.getStatus().equals(MainetConstants.WorkFlow.Status.CLOSED) && workflowRequest.getLastDateOfAction() != null ) {
					 		applicationStatusDTO.setDueDate(Utility.dateToString(workflowRequest.getLastDateOfAction(), MainetConstants.DATE_FORMAT));
					 	}
					 	
			    	//applicationStatusDTO.setStatus(workflowRequest.getStatus());
					applicationStatusDTO.setLastDecision(workflowRequest.getLastDecision());

				} else {
					if (application.getApmApplClosedFlag() != null
							&& application.getApmApplClosedFlag().equals(MainetConstants.Common_Constant.YES))
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID)
							applicationStatusDTO.setStatus(MainetConstants.WorkFlow.Status.CLOSED);
						else
							applicationStatusDTO.setStatus(ApplicationSession.getInstance().getMessage("CFC.app.status.closed"));
					else
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID)
							applicationStatusDTO.setStatus(MainetConstants.WorkFlow.Status.PENDING);
						else
							applicationStatusDTO.setStatus(ApplicationSession.getInstance().getMessage("CFC.app.status.pending"));
					if (application.getTbServicesMst() != null && application.getTbServicesMst().getSmServiceDuration() != null) {
						  Date dueDate = Utility.getAddedDateBy2(application.getApmApplicationDate(), application.getTbServicesMst().getSmServiceDuration().intValue());
						  applicationStatusDTO.setDueDate(Utility.dateToString(dueDate, MainetConstants.DATE_FORMAT));
						  }else 
							  applicationStatusDTO.setDueDate(Utility.dateToString(application.getApmApplicationDate(), MainetConstants.DATE_FORMAT));
						  
				}
				if(!(StringUtils.equalsIgnoreCase(MainetConstants.Property.SAS, serviceCode) 
					      || StringUtils.equalsIgnoreCase(MainetConstants.Property.MUT, serviceCode) 
					      || StringUtils.equalsIgnoreCase(MainetConstants.Property.NDT, serviceCode))) {
					if(null != workflowRequest ){

						WorkflowMas wokflowMas = iWorkFlowTypeService.getWorkFlowById(workflowRequest.getWorkflowTypeId());

						if(null!=wokflowMas.getCodIdOperLevel1()){

							if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
								applicationStatusDTO.setWard1(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel1(), UserSession.getCurrent().getOrganisation())
										.getDescLangFirst());
							else 
								applicationStatusDTO.setWard1(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel1(), UserSession.getCurrent().getOrganisation())
										.getDescLangSecond());
						}
						if(null!=wokflowMas.getCodIdOperLevel2()){
							if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
								applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel2(), UserSession.getCurrent().getOrganisation())
										.getDescLangFirst());
							else 
								applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel2(), UserSession.getCurrent().getOrganisation())
										.getDescLangSecond());
						}
						if(null!=wokflowMas.getCodIdOperLevel3()){
							if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
								applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel3(), UserSession.getCurrent().getOrganisation())
										.getDescLangFirst());
							else 
								applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(wokflowMas.getCodIdOperLevel3(), UserSession.getCurrent().getOrganisation())
										.getDescLangSecond());
						}	
					}
				}
				if(StringUtils.equalsIgnoreCase(MainetConstants.Property.SAS, serviceCode)) {
					if(null!=cfcAddressEntity.getApaWardNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
					if(null!=cfcAddressEntity.getApaZoneNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
				}
				if(StringUtils.equalsIgnoreCase(MainetConstants.Property.MUT, serviceCode)) {
					if(null!=cfcAddressEntity.getApaWardNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
					if(null!=cfcAddressEntity.getApaZoneNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
				}
				if(StringUtils.equalsIgnoreCase(MainetConstants.Property.NDT, serviceCode)) {
					if(null!=cfcAddressEntity.getApaWardNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard1(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard1(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
					if(null!=cfcAddressEntity.getApaZoneNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
				}
				if(StringUtils.equalsIgnoreCase(MainetConstants.Property.CHANGE_IN_ASSESSMENT, serviceCode)) {
					if(null!=cfcAddressEntity.getApaWardNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard2(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaWardNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
					if(null!=cfcAddressEntity.getApaZoneNo()){
						if(langId == MainetConstants.DEFAULT_LANGUAGE_ID) 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangFirst());
						else 
							applicationStatusDTO.setWard3(CommonMasterUtility.getHierarchicalLookUp(cfcAddressEntity.getApaZoneNo(), UserSession.getCurrent().getOrganisation())
									.getDescLangSecond());
					}
				}
				
				setRefNoAndWardZoneDescForProperty(langId, serviceCode, application, applicationStatusDTO);
				
		  } catch (Exception e) {
				logger.info("Exception occure while calling method getApplicationStatusDtos() "+e);
			}
		  
				applicationStatusDTOs.add(applicationStatusDTO);
		}
	}
		return applicationStatusDTOs;
	}

	private void setRefNoAndWardZoneDescForProperty(int langId, String serviceCode,
			TbCfcApplicationMstEntity application, ApplicationStatusDTO applicationStatusDTO) {
		if (StringUtils.isEmpty(application.getRefNo()) && StringUtils.equalsIgnoreCase(
				application.getTbServicesMst().getTbDepartment().getDpDeptcode(),
				MainetConstants.Property.PROP_DEPT_SHORT_CODE)) {
			if ((StringUtils.equalsIgnoreCase(MainetConstants.Property.EDIT_NAME_ADDRESS, serviceCode)
					|| StringUtils.equalsIgnoreCase(MainetConstants.Property.Bifurcation, serviceCode)
					|| StringUtils.equalsIgnoreCase(MainetConstants.Property.SAS, serviceCode)
					|| StringUtils.equalsIgnoreCase(MainetConstants.Property.NPR, serviceCode)
					|| StringUtils.equalsIgnoreCase(MainetConstants.Property.PROPERTY_KYC, serviceCode)
					|| StringUtils.equalsIgnoreCase(MainetConstants.Property.BMC, serviceCode))) {
				
				applicationStatusDTO.setReferenceNo(cfcApplicationMstJpaRepository.getPropertyNoAsRef(application.getApmApplicationId()));
			}
			if (StringUtils.equalsIgnoreCase(MainetConstants.Property.BILL_DELETION, serviceCode)) {
				applicationStatusDTO.setReferenceNo(cfcApplicationMstJpaRepository.getPropertyNoAsRefFromBill(application.getApmApplicationId()));
			}
		}

		Object[] wardZoneResult = cfcApplicationMstJpaRepository.fetchWardZoneId(applicationStatusDTO.getReferenceNo());

		if (ArrayUtils.isNotEmpty(wardZoneResult)) {

			Object[] wardRow = (Object[]) wardZoneResult[0];
		    Object assWard1 = wardRow.length > 0 ? wardRow[0] : null;
		    Object assWard2 = wardRow.length > 1 ? wardRow[1] : null;

		    boolean isDefaultLanguage = langId == MainetConstants.DEFAULT_LANGUAGE_ID;
		    Organisation organisation = UserSession.getCurrent().getOrganisation();

		    if (assWard1 != null) {
		        applicationStatusDTO.setWard1(setWardZoneDesc(Long.parseLong(assWard1.toString()), organisation, isDefaultLanguage));
		    }
		    if (assWard2 != null) {
		        applicationStatusDTO.setWard2(setWardZoneDesc(Long.parseLong(assWard2.toString()), organisation, isDefaultLanguage));
		    }
		}
	}

	private String setWardZoneDesc(Long wardId, Organisation organisation, boolean isDefaultLanguage) {
	    
	    if (wardId == null) {
	        return null;
	    }
	    LookUp lookup = CommonMasterUtility.getHierarchicalLookUp(wardId, organisation);
	    return isDefaultLanguage ? lookup.getDescLangFirst() : lookup.getDescLangSecond();
	}
	
	@Override
	@Transactional
	public boolean isDataExist(CFCApplicationStatusDto cfcApplicationStatusDto) {
		
		List<TbCfcApplicationMstEntity> result = cfcAppllicationStatusDao
				.getCFCApplicationEtites(cfcApplicationStatusDto);
		
		if(result.isEmpty())
			return true;
		return false;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.service.ICFCApplicationMasterService#
	 * getCFCApplicationOnlyByApplicationId(long)
	 */
	@Override
	@Transactional
	public TbCfcApplicationMstEntity getCFCApplicationOnlyByApplicationId(final long applicationId) {
		final TbCfcApplicationMstEntity cfcMaster = cfcApplicationMasterDAO
				.getCFCApplicationByOnlyApplicationId(applicationId);
		
		return cfcMaster;
	}
	
	@Override
	@Transactional
	public List<TbCfcApplicationMstEntity> getCFCApplData(Long orgid) {
	    List<TbCfcApplicationMstEntity> cfcMaster = new ArrayList<>();
		cfcMaster = cfcApplicationMasterDAO.getApplicantList(orgid);
		return cfcMaster;
	}

	@Override
	public CustomerInfoDTO getReferenceNoForPayment(Long applicationId, Long orgId) {
		CustomerInfoDTO result = null;
        try {
        	Class<?> clazz = null;
            Object dynamicServiceInstance = null;
        	String serviceClassName = "com.abm.mainet.water.service.NewWaterConnectionServiceImpl";
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz,
					ApplicationSession.getInstance().getMessage("common.water.csIdn"),
					new Class[] {Long.class, Long.class });
            
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            
            result = (CustomerInfoDTO) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { applicationId, orgId });
        } catch (LinkageError | Exception e) {
        	logger.info("Exception occured while fetching csIdn from applicationId " + e.getMessage());
        }
        return result;
	}
	@Override
	public TbCfcApplicationMstEntity saveApplicationData(final TbCfcApplicationMstEntity entity) {

		TbCfcApplicationMstEntity appEntity=cfcApplicationMasterDAO.saveApplicationData(entity);
		
		return appEntity;
	}
	
	
	@Override
	@Transactional
	public boolean updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(final long applicationId,final long transactionId,long orgId) {
		return cfcApplicationMasterDAO.updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(applicationId, transactionId,orgId);
	}
	
	@Override
	@Transactional
	public void updateCFCApplicationCheklistVerificationFlag(final long applicationId, long orgId, String status) {
		cfcApplicationMstJpaRepository.updateCheklistVerificationFlag(applicationId, status);
	}
	
	@Override
	public int existsByApplicationId(Long applicationId) {
		return cfcApplicationMstJpaRepository.existsByApplicationId(applicationId);
	}
}