package com.abm.mainet.common.workflow.service;


import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.mail.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import com.abm.mainet.cfc.objection.dto.ObjectionDetailsDto;
import com.abm.mainet.cfc.objection.service.IObjectionDetailsService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.rest.ui.controller.BpmsIntegrationController;
import com.abm.mainet.common.integration.dms.service.BpmsIntegrationServiceImpl;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;
import com.abm.mainet.common.service.EmployeeService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dao.IWorkflowTypeDAO;
import com.abm.mainet.common.workflow.domain.AapleSarkarStatusEntity;
import com.abm.mainet.common.workflow.domain.AapleSarkarSummaryEntity;
import com.abm.mainet.common.workflow.domain.WorkflowMas;
import com.abm.mainet.common.workflow.dto.AapleSarkarSummaryDTO;
import com.abm.mainet.common.workflow.dto.RowData;
import com.abm.mainet.common.workflow.dto.TaskSearchRequest;
import com.abm.mainet.common.workflow.dto.UserTaskDTO;
import com.abm.mainet.common.workflow.repository.AapleSarkarStatusRepository;
import com.abm.mainet.quartz.domain.QuartzSchedulerMaster;
import com.abm.mainet.smsemail.dto.SMSAndEmailDTO;
import com.abm.mainet.smsemail.service.ISMSAndEmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Jeetendra.Pal
 *
 */
@Service
public class WorkFlowJobSchedulerServiceImpl implements IWorkFlowJobSchedulerService {

    private static final Logger LOGGER = Logger.getLogger(WorkFlowJobSchedulerServiceImpl.class);

    @Autowired
    private IWorkflowTyepResolverService iWorkflowTyepResolverService;

    @Autowired
    private IOrganisationService iOrganisationService;

    @Autowired
    private IObjectionDetailsService iObjectionDetailsService;
    
    @Autowired
    private IEmployeeService employeeService;
    
    @Autowired
	private EmployeeJpaRepository employeeRepo;
    
    @Autowired
	private IOrganisationService organisationService;

    @Autowired
	private ISMSAndEmailService iSMSAndEmailService;
    
    @Autowired
	private IWorkflowActionService workflowActionService;
    
    @Autowired
    private AapleSarkarStatusRepository aapleSarkarStatusRepository;
    
    @Autowired
    @Resource
    private IWorkflowTypeDAO workflowTypeDAOImpl;
    

    
    @Autowired
    BpmsIntegrationServiceImpl bpmsIntegrationServiceImpl;

    @Override
    public void updateWorkflowObjectionTask(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

        LOGGER.info("Schedular for work flow objection task Started");

        Map<String, List<Long>> workflowDetails = new LinkedHashMap<String, List<Long>>();

        List<Long> applicationId = new ArrayList<>();

        List<ObjectionDetailsDto> list = iObjectionDetailsService.getAllInactiveObjectionList();

        if (list != null && !list.isEmpty()) {
            list.forEach(objection -> {

                applicationId.add(objection.getApmApplicationId());
            });

            workflowDetails.put("applicationId", applicationId);

            String url = ServiceEndpoints.WorkflowExecutionURLS.WORKFLOW_AUTO_OBJECTION_TASK;

            updateAllNewlyGenertaedTask(workflowDetails, url);

        }

        LOGGER.info("Schedular for work flow objection time completed ");

    }

    @Override
    public void updateWorkflowAutoEscalationTask(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

        LOGGER.info("Schedular for work flow Auto Escalation Task Started");

        Map<String, List<Long>> workflowDetails = new LinkedHashMap<String, List<Long>>();
        LOGGER.info("Organisation Id from updateWorkflowAutoEscalationTask method >>"+runtimeBean.getOrgId().getOrgid());
        final Organisation organisation = iOrganisationService.getOrganisationById(runtimeBean.getOrgId().getOrgid());

        LookUp lookup = CommonMasterUtility.getValueFromPrefixLookUp("AE", "WFM", organisation);
        LOGGER.info("Lookup code from updateWorkflowAutoEscalationTask method >>"+lookup.getLookUpId());
        List<WorkflowMas> list = iWorkflowTyepResolverService.resolveAutoEscalationWorkFlow(lookup.getLookUpId());

        if (list != null && !list.isEmpty()) {

            List<Long> ids = list.stream().map(WorkflowMas::getWfId).collect(Collectors.toList());

            workflowDetails.put("workFlowId", ids);

            String url = ServiceEndpoints.WorkflowExecutionURLS.WORKFLOW_AUTO_ESCALATION_TASK;

            updateAllNewlyGenertaedTask(workflowDetails, url);
        }

        LOGGER.info("Schedular for work flow Auto Escalation Task end ");

    }

    private void updateAllNewlyGenertaedTask(Map<String, List<Long>> requestParam, String url) {
        ResponseEntity<?> responseEntity = null;
        DefaultUriTemplateHandler dd = new DefaultUriTemplateHandler();
        dd.setParsePath(true);
        try {
            responseEntity = RestClient.callRestTemplateClient(requestParam, url);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                LOGGER.info("Schedular for work flow run successfully ");
            }
        } catch (Exception ex) {
            throw new FrameworkException(
                    "Exception occured while calling Schedular :" + requestParam, ex);
        }
    }

	@Override
	public void sendDailySMSForCare(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

		LOGGER.info("Schedular for sendDailySMSForCare Started");
		final ApplicationSession session = ApplicationSession.getInstance();
		List<Object> employeeList = employeeService.findAllEmployeeFromCareWorkflow();
		Long orgId = runtimeBean.getOrgId().getOrgid();
		List<UserTaskDTO> list = new ArrayList<>();
		Long empId = 1l;
		for (Object employeeList1 : employeeList) {
			SMSAndEmailDTO smsAndEmailDto = new SMSAndEmailDTO();
			Employee getEmployee = employeeRepo.getEmployeeByEmpId(Long.valueOf(String.valueOf(employeeList1)), orgId);

			LOGGER.info("Employee id:" + getEmployee.getEmpId());

			TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
			taskSearchRequest.setEmpId(getEmployee.getEmpId());
			taskSearchRequest.setLangId(1);
			taskSearchRequest.setStatus("PENDING");
			taskSearchRequest.setOrgId(orgId);

			list = workflowActionService.findComplaintTaskForEmployee(taskSearchRequest);
			LOGGER.info("Complaint LIST SIZE " + list.size());

			smsAndEmailDto.setMobnumber(getEmployee.getEmpmobno());
			smsAndEmailDto.setUserId(empId);
			if (session.getLangId() == MainetConstants.DEFAULT_LANGUAGE_ID) {
			smsAndEmailDto.setAppName(session.getMessage("adh.sir.madam", "adh.sir.madam",new Locale(MainetConstants.REG_ENG.ENGLISH)));
			//smsAndEmailDto.setAppName("Alert message");
			}else {
				smsAndEmailDto.setAppName(session.getMessage("adh.sir.madam", "adh.sir.madam",new Locale(MainetConstants.REG_ENG.REGIONAL)));
			}
			smsAndEmailDto.setEmail(getEmployee.getEmpemail());
			smsAndEmailDto.setOrgId(getEmployee.getOrganisation().getOrgid());
			smsAndEmailDto.setAppNo(String.valueOf(list.size()));
			smsAndEmailDto.setServName(MainetConstants.DEPT_SHORT_NAME.CFC_CENTER);
			LOGGER.info("Size of List: " + list.size());

			if (list.size() > 0 && list.size() != 0) {
				LOGGER.info("Inside of list: " + list.size());
				sendMessageAndEmail(smsAndEmailDto, orgId);
			}
		}

		LOGGER.info("Schedular sendDailySMSForCare End");

	}
	
	
	
	@Override
	public void estatePaymentMonthlySMS(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

		LOGGER.info("Schedular for sendMonthlySMSForRNL Started");
		
		Long orgId = runtimeBean.getOrgId().getOrgid();
		
		List<Object[]> estateBillPaymentSmsList = workflowTypeDAOImpl.findEstateOutstandingDetailsForSms(orgId);

		
		for (Object[] smsDataList : estateBillPaymentSmsList) {
			SMSAndEmailDTO smsAndEmailDto = new SMSAndEmailDTO();
			
			//Estate Name
			smsAndEmailDto.setSubject(smsDataList.length > 0 && smsDataList[0] != null ? smsDataList[0].toString() : "");
			
			//property No
			smsAndEmailDto.setPropertyNo(smsDataList.length > 2 && smsDataList[2] != null ? smsDataList[2].toString() : "");
			
			//Old property No
			smsAndEmailDto.setObjectionNo(smsDataList.length > 3 && smsDataList[3] != null ? smsDataList[3].toString() : "");
			
			//Mobile No
			smsAndEmailDto.setMobnumber(smsDataList.length > 6 && smsDataList[6] != null ? smsDataList[6].toString() : "");

			 // Monthly Installment + arrears with tax(248) Amount
			
			try {
			    smsAndEmailDto.setAmount(
			        smsDataList.length > 7 && smsDataList[7] != null
			            ? Double.parseDouble(smsDataList[7].toString())
			            : 0.0
			    );
			} catch (NumberFormatException nfe) {
			    smsAndEmailDto.setAmount(0.0);
			}
			



			sendMessageAndEmailForRnl(smsAndEmailDto, orgId);
        }

		LOGGER.info("Schedular sendMonthlySMSForRNL End");

	}
	

	
	@Override
	public void sendMessageAndEmail(SMSAndEmailDTO smsAndEmailDTO, Long orgId) {
		Organisation org = organisationService.getOrganisationById(orgId);
		//int langId = Utility.getDefaultLanguageId(org);
		final ApplicationSession session = ApplicationSession.getInstance();
		boolean status = iSMSAndEmailService.sendEmailSMS("COM", MainetConstants.SMS_EMAIL_URL.QUARTZ_SCHEDULE_MASTER,
				PrefixConstants.SMS_EMAIL_ALERT_TYPE.TASK_NOTIFICATION, smsAndEmailDTO, org, session.getLangId());
		LOGGER.info("Is SMS sent successfully: " + status);
	}
	
	
	
	@Override
	public void sendMessageAndEmailForRnl(SMSAndEmailDTO smsAndEmailDTO, Long orgId) {
		Organisation org = organisationService.getOrganisationById(orgId);
		//int langId = Utility.getDefaultLanguageId(org);
		boolean status = iSMSAndEmailService.sendEmailSMS("RL", MainetConstants.SMS_EMAIL_URL.QUARTZ_SCHEDULE_MASTER,
				PrefixConstants.SMS_EMAIL_ALERT_TYPE.TASK_NOTIFICATION, smsAndEmailDTO, org, 1);
		LOGGER.info("Is SMS sent successfully: " + status);
	}


	@Override
	public void sendDailyStatusToAapleSarkar(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

	    LOGGER.info("Scheduler for sendDailyStatusToAapleSarkar started...");

	    String accessToken = getTokenaapleSarkarAPI();

	    if (accessToken == null) {
	        LOGGER.error("Access token is null. Scheduler exiting...");
	        return;
	    }

	    LOGGER.error("Access token: " + accessToken);

	    List<AapleSarkarSummaryEntity> summaryEntities = workflowTypeDAOImpl.getAapleSarkarSummary();

	    if (CollectionUtils.isEmpty(summaryEntities)) {
	        LOGGER.error("Aaple Sarkar API not called: No data present for current month.");
	        AapleSarkarStatusEntity faildStatusEntity = new AapleSarkarStatusEntity();
			faildStatusEntity.setCreatedDate(new Date());
			faildStatusEntity.setUpdateStatus("No data present for current month.");
			aapleSarkarStatusRepository.save(faildStatusEntity);
	        LOGGER.error("Scheduler for sendDailyStatusToAapleSarkar ended.");
	        return;
	    }

	    List<RowData> rowDataList = summaryEntities.stream().map(entity -> {
	        RowData rowData = new RowData();
	        rowData.setDepartment(entity.getDepartment());
	        rowData.setService(entity.getService());
	        rowData.setDivision(entity.getDivision());
	        rowData.setDistrict(entity.getDistrict());
	        rowData.setTaluka(entity.getTaluka());
	        rowData.setApproved(entity.getApproved());
	        rowData.setRejected(entity.getRejected());
	        rowData.setOnTimeDelivery(entity.getOnTimeDelivery());
	        rowData.setPendingatUser(entity.getPendingatUser());
	        rowData.setPendingatDepartment(entity.getPendingatDepartment());
	        rowData.setNotOnTimeDelivery(entity.getNotOnTimeDelivery());
	        rowData.setPaymentMode(entity.getPaymentMode());
	        rowData.setApplicationSource(entity.getApplicationSource());
	        rowData.setPendingatDepartmentWithinSLA(entity.getPendingatDepartmentWithinSLA());
	        rowData.setPendingatDepartmentAfterSLA(entity.getPendingatDepartmentAfterSLA());
	        return rowData;
	    }).collect(Collectors.toList());

	    AapleSarkarSummaryDTO summaryDTO = new AapleSarkarSummaryDTO();
	    summaryDTO.setRowData(rowDataList);
	    summaryDTO.setMonth(LocalDate.now().getMonthValue());
	    summaryDTO.setYear(String.valueOf(LocalDate.now().getYear()));

	    if (pushAapleSarkarData(accessToken, summaryDTO)) {
	        AapleSarkarStatusEntity statusEntity = new AapleSarkarStatusEntity();
	        statusEntity.setCreatedDate(new Date());
	        statusEntity.setUpdateStatus("Success");
	        aapleSarkarStatusRepository.save(statusEntity);
	        LOGGER.error("Aaple Sarkar data pushed and status saved successfully.");
	    } else {
	    	LOGGER.error("Start Saving Failed Status");
			AapleSarkarStatusEntity faildStatusEntity = new AapleSarkarStatusEntity();
			faildStatusEntity.setCreatedDate(new Date());
			faildStatusEntity.setUpdateStatus("Failed to Push Data");
			aapleSarkarStatusRepository.save(faildStatusEntity);
			LOGGER.error("Ended Saving Failed Status...");
	    	
	        LOGGER.error("Failed to push data to Aaple Sarkar.");
	    }

	    LOGGER.error("Scheduler for sendDailyStatusToAapleSarkar ended.");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getTokenaapleSarkarAPI() {

		String departmentCode = ApplicationSession.getInstance().getMessage("aaple.sarkar.clientCode"); // "KDMCDeptN"
		String clientSecretKey = ApplicationSession.getInstance().getMessage("aaple.sarkar.clientSecretKey"); // 2DDAAFEA-0A6F-4B6B-9434-E908B4D8C092
		String url = ApplicationSession.getInstance().getMessage("aaple.sarkar.dashboard.token.url"); // https://rtsdashboarddeptapi.mahaitgov.in/api/Token/GetToken

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.set("DepartmentCode", departmentCode);
		headers.set("ClientSecretKey", clientSecretKey);
		String tokenValue = null;

		try {
			ResponseEntity<?> response = RestClient.callRestTemplateClientWithHeaders(null, url, HttpMethod.GET,
					String.class, headers);

			if (response != null && response.getStatusCode() == HttpStatus.OK) {
				String responseBody = (String) response.getBody();

				if (StringUtils.isNotBlank(responseBody)) {
					Map<String, Object> responseMap = Utility.getMapper().readValue(responseBody, Map.class);

					Object data = responseMap.get("data");
					if (data != null) {
						tokenValue = data.toString();
						LOGGER.error("Token data: " + tokenValue);
					} else {
						LOGGER.error("Response received but 'data' field is null.");
					}
				} else {
					LOGGER.error("Received empty response body.");
				}
			} else {
				LOGGER.error("Non-OK HTTP status received: {}/ null response");
			}

			LOGGER.error("Access Token Response: " + tokenValue);

		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching token", e);
		}

		return tokenValue;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean pushAapleSarkarData(String accessToken, AapleSarkarSummaryDTO aapleSarkarSummaryDTO) {
		try {
			ResponseEntity<?> Response = null;
			LOGGER.error(" Aaple Sarkar Push Data API Started ---------------->" ); 
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			headers.add("Authorization", "Bearer " + accessToken);
			
			ObjectMapper mapper = Utility.getMapper();
			
			LOGGER.error("Aaple Sarkar URL: {}" +  ApplicationSession.getInstance().getMessage("aaple.sarkar.dashboard.push.url"));
			LOGGER.error("HTTP Method: {}"+ HttpMethod.POST);
			LOGGER.error("Request Headers: {}"+ headers);
			
			 try {
		            LOGGER.error("Request DTO: {}"+ mapper.writeValueAsString(aapleSarkarSummaryDTO));
		        } catch (JsonProcessingException e) {
		            LOGGER.error("Failed to serialize DTO for logging"+ e);
		        }
			
			Response = RestClient.callRestTemplateClientWithHeaders(aapleSarkarSummaryDTO,
					ApplicationSession.getInstance().getMessage("aaple.sarkar.dashboard.push.url"), HttpMethod.POST,
					String.class, headers);
			//https://rtsdashboarddeptapi.mahaitgov.in/api/Dashboard/PushDepartmentDetails
			LOGGER.error("Aaple Sarkar Push Data Response ---------------->" + Response.getStatusCode());
			LOGGER.error("Aaple sarkar Push Data Body ---------------->" + Response.getBody());
			if ((Response != null) && (Response.getStatusCode() == HttpStatus.OK)) {
				Map<String, Object> saveDesponse = null;
				
				saveDesponse = mapper.readValue((String) Response.getBody(), Map.class);
				if ((boolean) saveDesponse.containsKey("status")) {
					boolean status = (boolean) saveDesponse.get("status");
					if (status) {
						return true;
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception while Aaple sarkar PUSH Data ------------->" + ex);
			return false;
		}

		return false;

	}
	
	@Override
	public void getBpmsDailyPropsalId(QuartzSchedulerMaster runtimeBean, List<Object> parameterList) {

		LOGGER.info("getBpmsDailyPropsalId strated... ");

		bpmsIntegrationServiceImpl.fetchAndStoreUlbData();

		LOGGER.info("getBpmsDailyPropsalId ended... ");
	}
	
	public void updateLicRenAmount(QuartzSchedulerMaster runtimeBean, List<Object> parameterList)
			throws ClassNotFoundException, LinkageError {

		LOGGER.info("updateLicRenAmount strated... ");
		

		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.tradeLicense.service.RenewalLicenseApplicationServiceImpl";
		Long orgId = 1L;

		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);

		final Method method = ReflectionUtils.findMethod(clazz, "getTradeLicChargesAtApplicationFromBrmsRule",
				new Class[] { Long.class });
		

		ReflectionUtils.invokeMethod(method, dynamicServiceInstance, new Object[] {orgId});
		
	

		LOGGER.info("updateLicRenAmount ended... ");
	}
	
	private String getSafeString(Object[] row, int index) {
	    return (row != null && row.length > index && row[index] != null)
	            ? row[index].toString()
	            : ""; // Changed from null to ""
	}
	
	private Long getSafeLong(Object[] row, int index) {
	    if (row != null && row.length > index && row[index] != null) {
	        try {
	            return Long.valueOf(row[index].toString());
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	    return null;
	}
	
	
	public void getDataFromCore(QuartzSchedulerMaster runtimeBean, List<Object> parameterList)
			throws ClassNotFoundException, LinkageError {
		
		LOGGER.error("getDataFromCore strated... ");
		
		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.property.service.NewPropertyRegistrationServiceImpl";
		Long orgId = 1L;
		
		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);
		
		final Method method = ReflectionUtils.findMethod(clazz, "saveCorePropertyInDraft",
				new Class[] { Long.class });
		
		ReflectionUtils.invokeMethod(method, dynamicServiceInstance, new Object[] {orgId});
		
		LOGGER.error("getDataFromCore ended... ");
		
	}
}
