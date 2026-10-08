
package com.abm.mainet.cfc.scrutiny.ui.model;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import com.abm.mainet.cfc.checklist.service.IChecklistSearchService;
import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLabelDTO;
import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLableValueDTO;
import com.abm.mainet.cfc.scrutiny.dto.ViewCFCScrutinyLabelValue;
import com.abm.mainet.cfc.scrutiny.service.ScrutinyService;
import com.abm.mainet.common.audit.service.AuditService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Designation;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dms.client.FileNetApplicationClient;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.integration.dms.fileUpload.FileUploadClass;
import com.abm.mainet.common.integration.dms.fileUpload.FileUploadUtility;
import com.abm.mainet.common.integration.dms.service.IDmsService;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.DesignationService;
import com.abm.mainet.common.service.ICFCApplicationMasterService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.ui.model.AbstractModel;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.UserTaskDTO;
import com.abm.mainet.common.workflow.dto.WorkflowProcessParameter;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkFlowTypeService;
import com.abm.mainet.common.workflow.service.IWorkflowExecutionService;
import com.abm.mainet.common.workflow.service.IWorkflowTaskService;
import com.abm.mainet.smsemail.dto.SMSAndEmailDTO;
import com.abm.mainet.smsemail.service.ISMSAndEmailService;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriTemplateHandler;


/**
 * @author Rajendra.Bhujbal
 *
 */
@Component
@Scope("session")
public class ScrutinyLabelModel extends AbstractFormModel {

    private static final long serialVersionUID = -2765855207220786044L;

    private static final Logger logger = Logger.getLogger(ScrutinyLabelModel.class);

    @Resource
    private ScrutinyService scrutinyService;

    @Resource
    private IWorkflowExecutionService workflowExecutionService;

    @Resource
    private ServiceMasterService serviceMasterService;
    
    @Autowired
    private IWorkFlowTypeService iWorkFlowTypeService;
    
    @Autowired
    private ICFCApplicationMasterService cfcApplicationMasterService;
    


    @Autowired
    private IDmsService dmsService;

    private List<CFCAttachment> scrutinyDocs = new ArrayList<>(3);

    private ScrutinyLabelDTO scrutinyLabelDTO;

    private String queryString;

    private String errorMsg;

    private boolean flag;

    private final String jbpm = ApplicationSession.getInstance().getMessage("configuration.company.JBPM");

    private String wokflowDecision;
    
    private String serviceCode;
    
    private String dicScLevelChek;
    
    private List<Employee> executiveEmployeeList = new ArrayList<>();
    
    @Autowired
	private IChecklistSearchService checklistSearchService;
    
    @Autowired
	private ISMSAndEmailService iSMSAndEmailService;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
	private IEmployeeService employeeService;
    
    @Autowired
	DesignationService designationService;
    
    @Autowired
    DepartmentService departmentService;

    public void populateScrutinyViewData(final long applicationId, final long serviceId, final UserSession userSession) {

        scrutinyDocs.clear();
        final Long gmId = UserSession.getCurrent().getEmployee().getGmid();
        scrutinyLabelDTO = scrutinyService.populateScrutinyLabelData(applicationId,
                UserSession.getCurrent().getEmployee().getEmpId(), gmId, UserSession.getCurrent().getOrganisation().getOrgid(),
                serviceId, UserSession.getCurrent().getLanguageId());
        getUserSession().getScrutinyCommonParamMap().put(MainetConstants.SCRUTINY_COMMON_PARAM.FINYEAR,
                getUserSession().getFinancialPeriodShortForm());
        getUserSession().getScrutinyCommonParamMap().put(MainetConstants.SCRUTINY_COMMON_PARAM.LANGID,
                getUserSession().getLanguageId() + MainetConstants.BLANK);
        getUserSession().getScrutinyCommonParamMap().put(MainetConstants.SCRUTINY_COMMON_PARAM.USERID,
                getUserSession().getEmployee().getEmpId() + MainetConstants.BLANK);
        getUserSession().getScrutinyCommonParamMap().put(MainetConstants.ORGID,
                UserSession.getCurrent().getOrganisation().getOrgid() + MainetConstants.BLANK);
        
        final ServiceMaster servicename = serviceMasterService.getServiceMaster(serviceId, UserSession.getCurrent().getOrganisation().getOrgid());
        setServiceCode(servicename.getSmShortdesc());

        if(MainetConstants.WaterServiceShortCode.NEW_WATER_SERVICE.equals(scrutinyLabelDTO.getServiceName())){
    		UserTaskDTO userTaskDTO = ApplicationContextProvider.getApplicationContext().getBean(IWorkflowTaskService.class).findByTaskId(this.getTaskId());
			CustomerInfoDTO csmrInfo = scrutinyService.getCurrentApplicationsConnSize(
					Long.valueOf(scrutinyLabelDTO.getApplicationId()), UserSession.getCurrent().getOrganisation().getOrgid());
			Double conSize = Double.valueOf(csmrInfo.getConnectionSize());
			if(csmrInfo.getConnectionSize()!=null && conSize>=1.0 && userTaskDTO.getTaskName().equals("Scrutiny 2") && UserSession.getCurrent().getRoleCode().equals("DE_WT")) {
				scrutinyLabelDTO.setNewWaterConnDecision(MainetConstants.FlagY);
				List<Employee> empList = getEmpList(UserSession.getCurrent().getOrganisation().getOrgid());
                if(empList!=null)
                	setExecutiveEmployeeList(empList);
			}
		}	
        
        if(MainetConstants.WaterServiceShortCode.WATER_DISCONNECTION.equals(servicename.getSmShortdesc())) {
        	scrutinyLabelDTO.setNewWaterConnDecision(MainetConstants.FlagY);
        	UserTaskDTO userTaskDTO = ApplicationContextProvider.getApplicationContext().getBean(IWorkflowTaskService.class).findByTaskId(this.getTaskId());
        	if(null != userTaskDTO && userTaskDTO.getTaskName().contains(MainetConstants.WorkFlow.Remarks.SCRUTINY_LEVEL_1) && 1 == userTaskDTO.getCurrentEscalationLevel()) {
        		setDicScLevelChek(MainetConstants.FlagY);
        	}
        }
        
        getUserSession().getScrutinyCommonParamMap().put("SERVICE_CODE",
        		servicename.getSmShortdesc());
        
      

    }

    public List<Long> getLabelList(final long labelId) {

        final List<Long> labelList = new ArrayList<>(0);

        final Long desgId = getLegalScrDesg();

        if (!scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().isEmpty() && (desgId > 0L)) {
            for (final ViewCFCScrutinyLabelValue viewCFCScrutinyLabelValue : scrutinyLabelDTO.getDesgWiseScrutinyLabelMap()
                    .get(desgId)) {
                if (viewCFCScrutinyLabelValue.getSlLabelId() > labelId) {
                    labelList.add(viewCFCScrutinyLabelValue.getSlLabelId());
                }
            }
        }

        return labelList;
    }

    /**
     * @param labelId
     * @param labelValue
     * @return
     */
    public Object getLabelValueByQuery(final long labelId, final String labelValue) {

        String query = getTableClauseQuery(labelId);

        Object obj = MainetConstants.BLANK;

        if (query == null) {
            return labelValue;
        }

        for (final Map.Entry<String, String> paramMap : getUserSession().getScrutinyCommonParamMap().entrySet()) {
            if (query.contains(":PARAMETER." + paramMap.getKey())) {
                query = query.replace(":PARAMETER." + paramMap.getKey(), paramMap.getValue());
            }
        }

        final String dbval = scrutinyService.getValueByLabelQuery(query);
        if ((dbval != null) && !dbval.isEmpty()) {
            obj = dbval;

        } else {
            obj = labelValue;
        }

        return obj;
    }

    private String getTableClauseQuery(final long lableId) {

        final Long desgId = getLegalScrDesg();

        if (!scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().isEmpty() && (desgId > 0L)) {
            for (final ViewCFCScrutinyLabelValue viewCFCScrutinyLabelValue : scrutinyLabelDTO.getDesgWiseScrutinyLabelMap()
                    .get(desgId)) {
                if (viewCFCScrutinyLabelValue.getSlLabelId() == lableId) {
                    getUserSession().getScrutinyCommonParamMap().put(MainetConstants.SCRUTINY_COMMON_PARAM.APM_APPLICATION_ID,
                            viewCFCScrutinyLabelValue.getApplicationId() + MainetConstants.BLANK);

                    return viewCFCScrutinyLabelValue.getSlPreValidation();
                }
            }
        }

        return null;
    }

    private Long getLegalScrDesg() {
        final String gmId = scrutinyLabelDTO.getRoleId();

        final Long dsgId = Long.valueOf(gmId);

        if (scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().containsKey(dsgId)) {
            return dsgId;
        }
        return 0L;
    }

    public boolean submitScrutinyLabels() {
        List<Long> attachmentId = new ArrayList<>(0);
        List<CFCAttachment> scrutinyDocumnet = uploadScrutinyDoc(getDirectry(), scrutinyLabelDTO, getFileNetClient());
        if (scrutinyDocumnet != null && !scrutinyDocumnet.isEmpty()) {
            for (CFCAttachment docs : scrutinyDocumnet) {
                attachmentId.add(docs.getAttId());
            }
        }
        
        
        final boolean saveflag = saveScrutinyLabelValue(true, attachmentId);
        if (saveflag) {
            setSuccessMessage(
                    ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.PROCCESS_SUBMIT));
            return true;
        } else {
            setSuccessMessage(
                    ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.PROCCESS_FAIL));
            return false;
        }
        

    }

    public boolean saveScrutinyLabels() {
        List<Long> attachmentId = new ArrayList<>(0);
        List<CFCAttachment> scrutinyDocumnet = uploadScrutinyDoc(getDirectry(), scrutinyLabelDTO, getFileNetClient());
        if (scrutinyDocumnet != null && !scrutinyDocumnet.isEmpty()) {
            for (CFCAttachment docs : scrutinyDocumnet) {
                attachmentId.add(docs.getAttId());
            }
        }
        final boolean saveflag = saveScrutinyLabelValue(false, null);

        if (!saveflag) {
            setSuccessMessage(
                    ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.PROCCESS_SAVE));
            return true;
        } else {
            setSuccessMessage(
                    ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.PROCCESS_FAIL));
            return false;
        }

    }

    /**
     * @param labelId
     * @param labelValue
     * @param appId
     * @return
     */
    public boolean saveLableValue(final long labelId, final String labelValue, final long level) {

        boolean saveFlag = false;
        if ((labelId != 0) && (level != 0)) {
            final ScrutinyLableValueDTO dto = new ScrutinyLableValueDTO();
            dto.setApplicationId(Long.valueOf(getScrutinyLabelDTO().getApplicationId()));
            dto.setLableId(labelId);
            dto.setLableValue(labelValue);
            dto.setLangId(Long.valueOf(UserSession.getCurrent().getLanguageId()));
            dto.setLevel(level);
            dto.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
            dto.setUserId(UserSession.getCurrent().getEmployee().getEmpId());

            saveFlag = scrutinyService.saveScrutinyValueBylabelId(dto);
        }
        return saveFlag;
    }

    public boolean sendTobackEmpl() {

        try {
            final ScrutinyLabelDTO labelDTO = scrutinyLabelDTO;
            if (labelDTO != null) {
                final Organisation orgid = UserSession.getCurrent().getOrganisation();
                final Employee empId = UserSession.getCurrent().getEmployee();

                String processName = serviceMasterService.getProcessName(Long.valueOf(scrutinyLabelDTO.getSmServiceId()),
                        orgid.getOrgid());
                if (processName != null) {
                    WorkflowProcessParameter workflowdto = new WorkflowProcessParameter();
                    WorkflowTaskAction workflowAction = new WorkflowTaskAction();
                    workflowAction.setTaskId(this.getTaskId());
                    workflowAction.setApplicationId(Long.valueOf(scrutinyLabelDTO.getApplicationId()));
                    workflowAction.setDateOfAction(new Date());
                    workflowAction.setDecision(MainetConstants.WorkFlow.Decision.SEND_BACK);
                    workflowAction.setOrgId(orgid.getOrgid());
                    workflowAction.setEmpId(empId.getEmpId());
                    workflowAction.setModifiedBy(empId.getEmpId());
                    workflowAction.setEmpType(empId.getEmplType());
                    workflowAction.setEmpName(empId.getEmpname());
                    workflowAction.setCreatedBy(empId.getEmpId());
                    workflowAction.setCreatedDate(new Date());
                    workflowdto
                            .setProcessName(processName);
                    try {
                        workflowdto.setWorkflowTaskAction(workflowAction);
                        workflowExecutionService.updateWorkflow(workflowdto);
                    } catch (final Exception e) {
                        throw new FrameworkException("Exception in Scrutiny for jbpm workflow : " + e.getMessage(),
                                e);
                    }
                }

            }
            setSuccessMessage(ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.SENDTOBACK));
            return true;
        } catch (final Exception exception) {

            logger.error("Exception Occur in sendTobackEmpl ()", exception);

        }
        setSuccessMessage(ApplicationSession.getInstance().getMessage(MainetConstants.SCRUTINY_COMMON_PARAM.PROCCESS_FAIL));
        return false;
    }

    private boolean saveScrutinyLabelValue(final boolean updateFlag, List<Long> attachmentId) {

        boolean saveFlag = false;
        ServiceMaster sm=null;
		if (scrutinyLabelDTO.getSmServiceId() != null) {
			sm = serviceMasterService.getServiceMaster(scrutinyLabelDTO.getSmServiceId(),
					UserSession.getCurrent().getOrganisation().getOrgid());
		}
        try {
            final Long desgId = getLegalScrDesg();

            final List<ViewCFCScrutinyLabelValue> list = scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().get(desgId);

            if ((list != null) && (!list.isEmpty())) {
            	
				if (sm != null && MainetConstants.Property.BMC.equals(sm.getSmShortdesc())) {
					UserTaskDTO userTaskDTO = ApplicationContextProvider.getApplicationContext().getBean(IWorkflowTaskService.class).findByTaskId(this.getTaskId());
					boolean lastCheckerTask = iWorkFlowTypeService.isLastTaskInScritinyTaskList(this.getTaskId(),userTaskDTO.getCurrentEscalationLevel());
					if(lastCheckerTask && StringUtils.equals("APPROVED", getWokflowDecision())) {
						
						/*
						 * auditService.saveDataForProperty(scrutinyLabelDTO.getApplicationId(),
						 * scrutinyLabelDTO.getSmServiceId(), getWokflowDecision(),
						 * UserSession.getCurrent().getOrganisation().getOrgid(), emp.getEmpId(),
						 * emp.getLgIpMac(), list.get(list.size() - 1).getLevels());
						 */	
						
						UserSession userSession = UserSession.getCurrent();
						Map<String, String> requestParam = new HashMap<>();
						DefaultUriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();
						uriTemplateHandler.setParsePath(true);
						requestParam.put("appId",scrutinyLabelDTO.getApplicationId());
						requestParam.put("serviceId",String.valueOf(scrutinyLabelDTO.getSmServiceId()));
						requestParam.put("workflowDecision",wokflowDecision);
						requestParam.put("orgId",String.valueOf(userSession.getOrganisation().getOrgid()));
						requestParam.put("empId",String.valueOf(userSession.getEmployee().getEmpId()));
						requestParam.put("lgIpMac",userSession.getEmployee().getLgIpMac());
						requestParam.put("level",String.valueOf(list.get(list.size() - 1).getLevels()));
						
						DefaultUriTemplateHandler dd = new DefaultUriTemplateHandler();
						dd.setParsePath(true);
						URI uri = dd.expand(ServiceEndpoints.Property.SAVE_BILL_METHOD, requestParam);
						ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(requestParam, uri.toString());
					        if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
					        	Boolean status = (Boolean) responseEntity.getBody();
					            if(!status) {
					            	return false;
					            }
					        }else {
					        	return false;
					        }
					
					}
				}						
                saveFlag = scrutinyService.saveCompleteScrutinyLabel(list, getUserSession(), scrutinyLabelDTO, updateFlag,
                        this.getTaskId(), attachmentId, getWokflowDecision());

            }
        } catch (final Exception exception) {

            logger.error("Exception Occur in saveScrutinyLabelValue()", exception);

        }
        
        if(null != sm && sm.getSmShortdesc().equalsIgnoreCase(MainetConstants.WaterServiceShortCode.WATER_DISCONNECTION) ) {
        	updateDataForReVerification();
        }
        // 126164 to trigger sms and email on scrutiny approval
		if (saveFlag == true && scrutinyLabelDTO.getSmServiceId() != null) {			
			String deptcode = sm.getTbDepartment().getDpDeptcode();
			if (deptcode.equals(MainetConstants.TradeLicense.MARKET_LICENSE)) {
				if(this.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.REJECTED)){
					scrutinyService.updateStatusFlagByRefId(scrutinyLabelDTO.getApplicationId(), UserSession.getCurrent().getOrganisation().getOrgid(), deptcode, UserSession.getCurrent().getEmployee().getEmpId());
				}
				sendSmsEmail(scrutinyLabelDTO);
			}
		}
        
        return saveFlag;
    }
    
	// 126164
	private void sendSmsEmail(ScrutinyLabelDTO dto) {
		final SMSAndEmailDTO smsDto = new SMSAndEmailDTO();
		Organisation org = UserSession.getCurrent().getOrganisation();
		smsDto.setMobnumber(dto.getMobNo());
		smsDto.setAppNo(dto.getApplicationId());
		smsDto.setAppName(dto.getApplicantName());
		smsDto.setServName(dto.getServiceName());
		smsDto.setEmail(dto.getEmail());
		String url = "ApplicationAuthorization.html";
		org.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());
		int langId = UserSession.getCurrent().getLanguageId();
		smsDto.setUserId(UserSession.getCurrent().getEmployee().getEmpId());
		if (this.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.APPROVED)) {
			smsDto.setDecision(this.getWokflowDecision());
			iSMSAndEmailService.sendEmailSMS(MainetConstants.DEPT_SHORT_NAME.CFC_CENTER, url,
					PrefixConstants.SMS_EMAIL_ALERT_TYPE.APPROVAL, smsDto, org, langId);
		} else if (this.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.REJECTED)) {
			smsDto.setDecision(this.getWokflowDecision());
			iSMSAndEmailService.sendEmailSMS(MainetConstants.DEPT_SHORT_NAME.CFC_CENTER, url,
					PrefixConstants.SMS_EMAIL_ALERT_TYPE.REJECTED_MSG, smsDto, org, langId);
		}
	}

    public String getDirectry() {
        return UserSession.getCurrent().getOrganisation().getOrgid() + File.separator + "SCRUTINY" + File.separator
                + scrutinyLabelDTO.getServiceName() + File.separator + scrutinyLabelDTO.getApplicationId()
                + File.separator + UserSession.getCurrent().getEmployee().getEmpId();
    }

    private List<CFCAttachment> uploadScrutinyDoc(final String directoryTree, final ScrutinyLabelDTO dto,
            final FileNetApplicationClient fileNetApplicationClient) {

        String directoryPath = null;
        Map<String, String> fileMap = null;
        boolean isDMS = false;
        boolean isFolderExist = false;
        if (MainetConstants.Common_Constant.YES
                .equals(ApplicationSession.getInstance().getMessage("dms.configure"))) {
            isDMS = true;
        }

        final List<CFCAttachment> scrutinyDocs = new ArrayList<>(0);

        CFCAttachment scrutinyDoc = null;
        final Date date = new Date();

        List<File> list = null;
        File file = null;
        String tempDirPath = MainetConstants.BLANK;
        Iterator<File> setFilesItr = null;

        for (final Map.Entry<Long, Set<File>> entry : FileUploadUtility.getCurrent().getFileMap().entrySet()) {
            list = new ArrayList<>(entry.getValue());

            setFilesItr = entry.getValue().iterator();

            while (setFilesItr.hasNext()) {
                tempDirPath = MainetConstants.operator.EMPTY;

                file = setFilesItr.next();

                scrutinyDoc = new CFCAttachment();

                scrutinyDoc.setAttDate(new Date());
                scrutinyDoc.setApplicationId(Long.parseLong(dto.getApplicationId()));

                scrutinyDoc.setLgIpMac(Utility.getMacAddress());

                scrutinyDoc.setLmodate(date);

                scrutinyDoc.setOrgid(UserSession.getCurrent().getOrganisation().getOrgid());

                if ((this.scrutinyDocs != null)
                        && (this.scrutinyDocs.size() >= FileUploadUtility.getCurrent().getFileMap().size())) {
                    scrutinyDoc.setClmRemark(this.scrutinyDocs.get(entry.getKey().intValue()).getClmRemark());
                }

                final Long desgId = getLegalScrDesg();
                final List<ViewCFCScrutinyLabelValue> viewCFCScrutinyLabelValue = scrutinyLabelDTO.getDesgWiseScrutinyLabelMap()
                        .get(desgId);
                if ((viewCFCScrutinyLabelValue != null) && (viewCFCScrutinyLabelValue.size() != 0)) {
                    scrutinyDoc.setSmScrutinyId(viewCFCScrutinyLabelValue.get(0).getScrutinyId());
                    scrutinyDoc.setSmScrutinyLevel(viewCFCScrutinyLabelValue.get(0).getLevels());
                }

                scrutinyDoc.setServiceId(dto.getSmServiceId());

                scrutinyDoc.setUpdatedBy(UserSession.getCurrent().getEmployee().getEmpId());

                scrutinyDoc.setUpdatedDate(date);

                scrutinyDoc.setAttBy(UserSession.getCurrent().getEmployee().getEmpname());

                scrutinyDoc.setAttFname(file.getName());

                scrutinyDoc.setUserId(UserSession.getCurrent().getEmployee().getEmpId());

                scrutinyDoc.setGmId(UserSession.getCurrent().getEmployee().getGmid());

                scrutinyDoc.setClmAprStatus(MainetConstants.Common_Constant.YES);

                for (final FileUploadClass fileUploadClass : FileUploadUtility.getCurrent().getFileUploadSet()) {
                    if (entry.getKey().toString().equals(fileUploadClass.getCurrentCount().toString())) {
                        boolean pathStatus = true;

                        if (pathStatus) {
                            tempDirPath = directoryTree + MainetConstants.FILE_PATH_SEPARATOR + fileUploadClass.getFolderName();

                            scrutinyDoc.setAttPath(tempDirPath);

                            pathStatus = false;
                        }

                        scrutinyDocs.add(scrutinyDoc);
                    }
                }
                try {
                    if (!isDMS) {
                        fileNetApplicationClient.uploadFileList(list, tempDirPath);
                    } else {
                        if (directoryPath == null) {
                            directoryPath = tempDirPath;
                        }
                        final Base64 base64 = new Base64();
                        final String bytestring = base64.encodeToString(FileUtils.readFileToByteArray(file));
                        fileMap = dmsService.createDocument(directoryPath, bytestring,
                                file.getName(), isFolderExist,null,null);
                        if (fileMap != null) {
                            isFolderExist = true;
                            directoryPath = fileMap.get("FOLDER_PATH");
                            scrutinyDoc.setDmsFolderPath(fileMap.get("FOLDER_PATH"));
                            scrutinyDoc.setDmsDocId(fileMap.get("DOC_ID"));
                            scrutinyDoc.setDmsDocName(fileMap.get("FILE_NAME"));
                            if ((scrutinyDoc.getDmsDocVersion() != null) && !scrutinyDoc.getDmsDocVersion().isEmpty()) {
                                Integer ver = Integer.valueOf(scrutinyDoc.getDmsDocVersion());
                                ver = ver + 1;
                                scrutinyDoc.setDmsDocVersion(String.valueOf(ver));
                            } else {
                                scrutinyDoc.setDmsDocVersion("1");
                            }
                        } else {
                            throw new RuntimeException();
                        }
                    }
                } catch (final Exception e) {
                    logger.error(MainetConstants.BLANK, e);
                }
            }
        }
        scrutinyService.saveAllScrutinyDoc(scrutinyDocs);
        try {
            final String path = FileUploadUtility.getCurrent().getExistingFolderPath();
            if (path != null) {
                final File cacheFolderStructure = new File(FileUploadUtility.getCurrent().getExistingFolderPath());

                FileUtils.deleteDirectory(cacheFolderStructure);
            }
        } catch (final Exception e) {
            logger.error(MainetConstants.BLANK, e);
        }

        return scrutinyDocs;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(final String queryString) {
        this.queryString = queryString;
    }

    public ScrutinyLabelDTO getScrutinyLabelDTO() {
        return scrutinyLabelDTO;
    }

    public void setScrutinyLabelDTO(final ScrutinyLabelDTO scrutinyLabelDTO) {
        this.scrutinyLabelDTO = scrutinyLabelDTO;
    }

    public List<CFCAttachment> getScrutinyDocs() {
        return scrutinyDocs;
    }

    public void setScrutinyDocs(final List<CFCAttachment> scrutinyDocs) {
        this.scrutinyDocs = scrutinyDocs;
    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(final boolean flag) {
        this.flag = flag;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public List<Employee> getExecutiveEmployeeList() {
		return executiveEmployeeList;
	}

	public void setExecutiveEmployeeList(List<Employee> executiveEmployeeList) {
		this.executiveEmployeeList = executiveEmployeeList;
	}

    /**
     * @param labelId
     * @param level
     * @return
     */
    public String getOpenFormValue(final long labelId, final long level) {

        final Long desgId = getLegalScrDesg();

        if (!scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().isEmpty() && (desgId > 0L)) {
            for (final ViewCFCScrutinyLabelValue viewCFCScrutinyLabelValue : scrutinyLabelDTO.getDesgWiseScrutinyLabelMap()
                    .get(desgId)) {
                if (viewCFCScrutinyLabelValue.getSlLabelId() == labelId) {
                    final String value = viewCFCScrutinyLabelValue.getSlValidationText();
                    return value;

                }
            }
        }

        return null;
    }

    public String getWokflowDecision() {
        return wokflowDecision;
    }

    public void setWokflowDecision(String wokflowDecision) {
        this.wokflowDecision = wokflowDecision;
    }
    
    public Boolean isScrutinyLoiGenerated() {
    	final List<ViewCFCScrutinyLabelValue> list = scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().get(getLegalScrDesg());
    	Boolean flag=scrutinyService.checkLoiGeneratedOrNot(scrutinyLabelDTO, getLegalScrDesg(), list);
    	if(!flag) {
    		addValidationError(getAppSession().getMessage("loi.paid.validation"));
    		return false;
    	}
    	return true;
    }

	public boolean isSiteInspectionAndMeterTypeEntryDone() {
		final List<ViewCFCScrutinyLabelValue> list = scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().get(getLegalScrDesg());
    	if(CollectionUtils.isEmpty(list)) {
    		return false;
    	}
    	
		Optional<ViewCFCScrutinyLabelValue> siteNotInspected = list.stream().filter(
				label -> MainetConstants.SMS_EMAIL_URL.SITE_INSPECTION_URL.equals(label.getSlFormName()) &&
					MainetConstants.NO.equalsIgnoreCase(label.getSvValue())).findAny();
		if(siteNotInspected.isPresent()) {
			addValidationError(getAppSession().getMessage("site.inspection.validation"));
			return false;
		}
		Optional<ViewCFCScrutinyLabelValue> connectionDetails = list.stream().filter(
				label -> MainetConstants.SMS_EMAIL_URL.CONNECTION_DETAIL_URL.equals(label.getSlFormName()) &&
					MainetConstants.NO.equalsIgnoreCase(label.getSvValue())).findAny();
		if(connectionDetails.isPresent()) {
			addValidationError(getAppSession().getMessage("connection.details.validation"));
			return false;
		}
		
		Optional<ViewCFCScrutinyLabelValue> meterEntryNotDone = list.stream().filter(
				label -> MainetConstants.SMS_EMAIL_URL.METER_TYE_URL.equals(label.getSlFormName()) &&
				MainetConstants.NO.equalsIgnoreCase(label.getSvValue())).findAny();
		
    	if(meterEntryNotDone.isPresent()) {
    		addValidationError(getAppSession().getMessage("meter.type.validation"));
    		return false;
    	}
    	return true;
	}
	 public Boolean isConnectionDetailsPresent() {
	    	final List<ViewCFCScrutinyLabelValue> list = scrutinyLabelDTO.getDesgWiseScrutinyLabelMap().get(getLegalScrDesg());
	    	Boolean flag = scrutinyService.checkConnectionDetailsPresentOrNot(scrutinyLabelDTO, getLegalScrDesg(), list);
	    	if(!flag) {
	    		addValidationError(getAppSession().getMessage("connection.details.check.validation"));
	    		return false;
	    	}
	    	return true;
	    }

	public void checkPropertyDues(long serviceId, long taskId, long appId) {
		ServiceMaster serviceMaster = serviceMasterService.getServiceMaster(serviceId,
				UserSession.getCurrent().getOrganisation().getOrgid());
		if(serviceMaster != null && StringUtils.equals(MainetConstants.Property.BMC, serviceMaster.getSmShortdesc())) {
			UserTaskDTO userTaskDTO = ApplicationContextProvider.getApplicationContext().getBean(IWorkflowTaskService.class).findByTaskId(taskId);
			boolean lastCheckerTask = iWorkFlowTypeService.isLastTaskInScritinyTaskList(taskId,userTaskDTO.getCurrentEscalationLevel());
			if(lastCheckerTask) {
				final ResponseEntity<?> response = RestClient.callRestTemplateClient(appId,
						ServiceEndpoints.GET_NO_DUES_BILL_METHOD + appId);
				if ((response != null) && (response.getBody().equals(true))) {
					scrutinyLabelDTO.setDuesAdjusted(true);
				}				
			}
			checkListDecentralized(appId, serviceId, UserSession.getCurrent().getOrganisation().getOrgid());
		}
		
	}
	
	public List<Employee> getEmpList(Long orgId){
		Designation dsg = designationService.findByShortname("EE");
		Long deptId = departmentService.getDepartmentIdByDeptCode(MainetConstants.WATER_DEPT);
		List<Employee> allEmpByDesignationAndDeptId = employeeService.getAllEmpByDesignationAndDeptId(dsg.getDsgid(), deptId, orgId);
		return allEmpByDesignationAndDeptId;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getDicScLevelChek() {
		return dicScLevelChek;
	}

	public void setDicScLevelChek(String dicScLevelChek) {
		this.dicScLevelChek = dicScLevelChek;
	}
	
	public void updateDataForReVerification() {
		cfcApplicationMasterService.updateCFCApplicationCheklistVerificationFlag(Long.valueOf(scrutinyLabelDTO.getApplicationId()),
				UserSession.getCurrent().getOrganisation().getOrgid(), MainetConstants.FlagP);
	}
}
