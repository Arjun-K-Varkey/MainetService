/**
 *
 */
package com.abm.mainet.cfc.scrutiny.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.jws.WebMethod;

import com.abm.mainet.cfc.loi.dto.TbLoiMas;
import com.abm.mainet.cfc.loi.service.TbLoiMasService;
import com.abm.mainet.cfc.scrutiny.dao.ScrutinyDAO;
import com.abm.mainet.cfc.scrutiny.domain.TbScrutinyLabelValueEntity;
import com.abm.mainet.cfc.scrutiny.domain.TbScrutinyLabelValueEntityKey;
import com.abm.mainet.cfc.scrutiny.domain.TbScrutinyLabelsEntity;
import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLabelDTO;
import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLableValueDTO;
import com.abm.mainet.cfc.scrutiny.dto.ViewCFCScrutinyLabelValue;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.CFCApplicationAddressEntity;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;
import com.abm.mainet.common.master.repository.TbScrutinyDocJpaRepository;
import com.abm.mainet.common.master.repository.TbScrutinyLabelValueEntityJpaRepository;
import com.abm.mainet.common.master.repository.TbScrutinyLabelsJpaRepository;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.repository.ServiceMasterRepository;
import com.abm.mainet.common.service.ICFCApplicationAddressService;
import com.abm.mainet.common.service.ICFCApplicationMasterService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.WorkflowProcessParameter;
import com.abm.mainet.common.workflow.dto.WorkflowTaskAction;
import com.abm.mainet.common.workflow.service.IWorkflowExecutionService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * The Class ScrutinyServiceImpl.
 *
 * @author Lalit.Prusti
 */
@Service
public class ScrutinyServiceImpl implements ScrutinyService {

	private static Logger LOG = Logger.getLogger(ScrutinyServiceImpl.class);

	@Resource
	private ScrutinyDAO scrutinyDAO;

	@Resource
	EmployeeJpaRepository employeeJpaRepository;

	@Resource
	ServiceMasterRepository serviceMasterRepository;

	@Resource
	TbScrutinyLabelsJpaRepository labelsJpaRepository;

	@Resource
	TbScrutinyLabelValueEntityJpaRepository labelValueEntityJpaRepository;

	@Resource
	private TbScrutinyDocJpaRepository scrutinyDocJpaRepository;

	@Resource
	private ICFCApplicationMasterService icfcApplicationMasterService;

	@Resource
	private ServiceMasterService serviceMasterService;

	@Resource
	private TbLoiMasService loiMasService;

	@Resource
	ICFCApplicationMasterService cfcService;
	@Resource
	private IWorkflowExecutionService workflowExecutionService;
	@Autowired
	private ICFCApplicationAddressService iCFCApplicationAddressService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	private MessageSource messageSource;
	@Override
	@Transactional(readOnly = true)
	public ScrutinyLabelDTO populateScrutinyLabelData(final long applicationId, final long empId, final long gmId,
			final long orgId, final long serviceId, final int langId) {

		LOG.info("Start the populateScrutinyLabelData()");
		final ScrutinyLabelDTO scrutinyLabelDTO = new ScrutinyLabelDTO();
		Long tricod1 = 0L;
		final ServiceMaster servicename = serviceMasterService.getServiceMaster(serviceId, orgId);
		try {
				
			scrutinyLabelDTO.setApplicationId(applicationId + MainetConstants.BLANK);

			final TbCfcApplicationMstEntity cfcApplicationMaster = icfcApplicationMasterService
					.getCFCApplicationByApplicationId(applicationId, orgId);
			final CFCApplicationAddressEntity address = iCFCApplicationAddressService
					.getApplicationAddressByAppId(applicationId, orgId);
			Organisation org = new Organisation();
			if (cfcApplicationMaster != null) {
				String userName = (cfcApplicationMaster.getApmFname() == null ? MainetConstants.BLANK
						: cfcApplicationMaster.getApmFname() + MainetConstants.WHITE_SPACE);
				userName += cfcApplicationMaster.getApmMname() == null ? MainetConstants.BLANK
						: cfcApplicationMaster.getApmMname() + MainetConstants.WHITE_SPACE;
				userName += cfcApplicationMaster.getApmLname() == null ? MainetConstants.BLANK
						: cfcApplicationMaster.getApmLname();
				scrutinyLabelDTO.setApplicantName(userName);
				//code added for set email and mobile no for Defect#121682
				if(address!=null) {
					scrutinyLabelDTO.setEmail(address.getApaEmail());
					scrutinyLabelDTO.setMobNo(address.getApaMobilno());
				}

				scrutinyLabelDTO.setServiceName(servicename.getSmServiceName());
				scrutinyLabelDTO.setSmServiceId(serviceId);
				// US#113590
				
				org.setOrgid(orgId);
				try {
					if (ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class)
							.getDeptCode(servicename.getTbDepartment().getDpDeptid())
							.equals(MainetConstants.TradeLicense.MARKET_LICENSE)
							&& Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)) {
						Map<String, Long> itemMap = getApplicationDetails(applicationId);

						if (itemMap.get(MainetConstants.TRI_COD1) != null
								&& itemMap.get(MainetConstants.TRI_COD1) > 0) {
							List<TbScrutinyLabelsEntity> list = ApplicationContextProvider.getApplicationContext()
									.getBean(TbScrutinyLabelsJpaRepository.class).checkScrutinyLabelExistOrNot(
											serviceId, orgId, itemMap.get(MainetConstants.TRI_COD1));
							if (list != null && !list.isEmpty()) {
								tricod1 = itemMap.get(MainetConstants.TRI_COD1);
							}
						}
					}
				} catch (Exception e) {
					LOG.error("Exception occur  when fetching Item Category and SubCategory");
				}
			}
              //  105334 - to get license end date by ApplicationId
			try {
				if (departmentService.getDeptCode(servicename.getTbDepartment().getDpDeptid())
						.equals(MainetConstants.TradeLicense.MARKET_LICENSE)) {
					String liceseEndDate = getAppDetailByAppId(applicationId);
					if (StringUtils.isNotBlank(liceseEndDate))
						scrutinyLabelDTO.setLicsenseDate(liceseEndDate);
				}
			} catch (Exception e) {
				LOG.error("Exception occur  when fetching licenes detail");
			}
             //#140388
			try {
				if (servicename.getTbDepartment() != null && servicename.getTbDepartment().getDpDeptid() != null) {
					String deptCode = departmentService.getDeptCode(servicename.getTbDepartment().getDpDeptid());
					if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SUDA)
							&& MainetConstants.TradeLicense.MARKET_LICENSE.equals(deptCode)) {
						scrutinyLabelDTO.setReamrkValidFlag(MainetConstants.FlagY);
					}
				}
			} catch (Exception e) {
				LOG.error("Exception occur  when setting remark flag  for suda" + e);
			}
			// US#113590 end
			final List<ViewCFCScrutinyLabelValue> scrutinyLabelValues = new ArrayList<>();
			final List<Object> fndAllscrutinyLabelValue = scrutinyDAO.getfindAllscrutinyLabelValueList(orgId, serviceId,
					null, applicationId, tricod1);

			if (fndAllscrutinyLabelValue != null) {

				final int listSize = fndAllscrutinyLabelValue.size();

				for (int iCounter = 0; iCounter < listSize; iCounter++) {

					final Object[] obj = (Object[]) fndAllscrutinyLabelValue.get(iCounter);
					ViewCFCScrutinyLabelValue labelValue = null;

					labelValue = new ViewCFCScrutinyLabelValue();

					labelValue.setScrutinyId(Long.valueOf(obj[0].toString()));

					if ((obj[1].toString() != null) && !obj[1].toString().isEmpty()) {
						labelValue.setSlLabel((obj[1].toString()));
					}

					if ((obj[2] != null) && !obj[2].toString().isEmpty()) {
						labelValue.setSlAuthorising(obj[2].toString());
					}

					if ((obj[3] != null) && !obj[3].toString().isEmpty()) {
						labelValue.setSlDatatype(obj[3].toString());
					}

					if ((obj[4] != null) && !obj[4].toString().isEmpty()) {
						labelValue.setSlDisplayFlag(obj[4].toString());
					}

					if ((obj[5] != null) && !obj[5].toString().isEmpty()) {
						labelValue.setSlFormMode(obj[5].toString());
					}

					if ((obj[6] != null) && !obj[6].toString().isEmpty()) {
						labelValue.setSlFormName(obj[6].toString());
					}

					if (obj[7] != null) {
						labelValue.setSlLabelId(Long.valueOf(obj[7].toString()));
					}

					if ((obj[8] != null) && !obj[8].toString().isEmpty()) {
						labelValue.setSlLabelMar(obj[8].toString());
					}

					if ((obj[9] != null) && !obj[9].toString().isEmpty()) {
						labelValue.setSlPreValidation(obj[9].toString());
					}

					if ((obj[10] != null) && !obj[10].toString().isEmpty()) {
						labelValue.setSlValidationText(obj[10].toString());
					}

					if ((obj[11] != null) && !obj[11].toString().isEmpty()) {
						labelValue.setSlTableColumn(obj[11].toString());
					}

					if ((obj[12] != null) && !obj[12].toString().isEmpty()) {
						labelValue.setSlWhereClause(obj[12].toString());
					}

					if ((obj[13] != null) && !obj[13].toString().isEmpty()) {
						labelValue.setSlDsgid(Long.valueOf(obj[13].toString()));
					}

					if ((obj[14] != null) && !obj[14].toString().isEmpty()) {
						labelValue.setLevels(Long.valueOf(obj[14].toString()));
					}

					if ((obj[15] != null) && !obj[15].toString().isEmpty()) {
						labelValue.setSvValue(obj[15].toString());
					}
					if ((obj[16] != null) && !obj[16].toString().isEmpty()) {
						labelValue.setApplicationId(Long.valueOf(obj[16].toString()));
					} else {

						labelValue.setApplicationId(applicationId);
					}
					
					if ((obj[17] != null) && !obj[17].toString().isEmpty()) {
						labelValue.setComV1(obj[17].toString());
					} 

					labelValue.setOrgId(orgId);
					scrutinyLabelValues.add(labelValue);
				}
			}

			scrutinyLabelDTO.setRoleId(String.valueOf(gmId));
			if ((scrutinyLabelValues != null) && (scrutinyLabelValues.size() > 0)) {
				final Map<Long, List<ViewCFCScrutinyLabelValue>> desgLabelMap = filterListAsPerDesignation(
						scrutinyLabelDTO, scrutinyLabelValues, langId, orgId);
				scrutinyLabelDTO.setDesgWiseScrutinyLabelMap(desgLabelMap);
				
			}
			TbLoiMas mas=loiMasService.getloiByApplicationIdForDeletion(applicationId, orgId);
			if(mas!=null)
				scrutinyLabelDTO.setApprovedFlag(MainetConstants.FlagY);

		} catch (final Exception exception) {
			LOG.error("Exception occur in populateScrutinyLabelData() ", exception);

		}
		return scrutinyLabelDTO;
	}

	private Map<Long, List<ViewCFCScrutinyLabelValue>> filterListAsPerDesignation(
			final ScrutinyLabelDTO scrutinyLabelDTO, final List<ViewCFCScrutinyLabelValue> cfcScrutinyLabelValues,
			final int langId, final long orgId) {

		final Map<Long, String> desgNameMap = new HashMap<>(0);

		final Map<Long, List<ViewCFCScrutinyLabelValue>> desgLabelMap = new LinkedHashMap<>(0);

		List<ViewCFCScrutinyLabelValue> viewCFCScrutinyLabelValues = null;

		for (final ViewCFCScrutinyLabelValue viewCFCScrutinyLabelValue : cfcScrutinyLabelValues) {
			viewCFCScrutinyLabelValues = null;
			if (desgLabelMap.containsKey(viewCFCScrutinyLabelValue.getSlDsgid())) {
				viewCFCScrutinyLabelValues = desgLabelMap.get(viewCFCScrutinyLabelValue.getSlDsgid());
			}
			if (viewCFCScrutinyLabelValues == null) {
				viewCFCScrutinyLabelValues = new ArrayList<>();
			}
			if(viewCFCScrutinyLabelValue.getComV1()!=null && !viewCFCScrutinyLabelValue.getComV1().isEmpty()) {
				List<String> configuredConnSizesList = Arrays.stream(viewCFCScrutinyLabelValue.getComV1().split(MainetConstants.operator.COMMA))
						.map(str -> str.trim()).collect(Collectors.toList());
				Long currentDesgnId = UserSession.getCurrent().getEmployee().getGmid();
				CustomerInfoDTO csmrInfo = getCurrentApplicationsConnSize(
						viewCFCScrutinyLabelValue.getApplicationId(), viewCFCScrutinyLabelValue.getOrgId());
				if(currentDesgnId.equals(viewCFCScrutinyLabelValue.getSlDsgid())) {
					if(csmrInfo.getConnectionSize()!=null) {
						Optional<String> applicableConnSizeApplication = configuredConnSizesList.stream().filter
								(size-> csmrInfo.getConnectionSize().equals(size)).findFirst();
						if(applicableConnSizeApplication!=null && applicableConnSizeApplication.isPresent()) {
							viewCFCScrutinyLabelValues.add(viewCFCScrutinyLabelValue);
							desgLabelMap.put(viewCFCScrutinyLabelValue.getSlDsgid(), viewCFCScrutinyLabelValues);
						}
					}
				}
			}else {
				viewCFCScrutinyLabelValues.add(viewCFCScrutinyLabelValue);
				desgLabelMap.put(viewCFCScrutinyLabelValue.getSlDsgid(), viewCFCScrutinyLabelValues);

			}
			if ((viewCFCScrutinyLabelValue.getSlDsgid() != null)
					&& !desgNameMap.containsKey(viewCFCScrutinyLabelValue.getSlDsgid())) {

				final GroupMaster gmMaster = scrutinyDAO.getDesignationName(viewCFCScrutinyLabelValue.getSlDsgid());
				if (gmMaster != null) {
					if (langId == 1) {
						desgNameMap.put(viewCFCScrutinyLabelValue.getSlDsgid(), gmMaster.getGrDescEng());
					} else {
						desgNameMap.put(viewCFCScrutinyLabelValue.getSlDsgid(), gmMaster.getGrDescReg());
					}
				}
			}
		}
		scrutinyLabelDTO.setDesgNameMap(desgNameMap);

		scrutinyLabelDTO.setDsgWiseScrutinyDocMap(
				getAllDesgWiseScrutinyDoc(Long.parseLong(scrutinyLabelDTO.getApplicationId()), orgId));

		final List<String> list = new ArrayList<>();
		list.add(MainetConstants.YESL);
		list.add(MainetConstants.NOL);
		scrutinyLabelDTO.setDislist(list);

		return desgLabelMap;
	}

	@Transactional(readOnly = true)
	public Map<Long, List<LookUp>> getAllDesgWiseScrutinyDoc(final long applId, final long orgId) {
		final Map<Long, List<LookUp>> desWiseScrutinyDocMap = new HashMap<>(0);

		LookUp lookUp = null;

		List<LookUp> lookUps = null;

		final List<CFCAttachment> docs = scrutinyDAO.getAllScrutinyDocDesgWise(applId, orgId);
		if (docs != null) {
			for (final CFCAttachment doc : docs) {
				lookUp = new LookUp();

				lookUp.setDescLangFirst(doc.getAttPath());
				lookUp.setDescLangSecond(doc.getAttPath());
				lookUp.setLookUpCode(doc.getAttFname());
				lookUp.setLookUpId(doc.getAttId());
				lookUp.setOtherField(doc.getClmRemark());
				lookUp.setLookUpType(doc.getDmsDocId());

				if (desWiseScrutinyDocMap.containsKey(doc.getGmId())) {
					lookUps = desWiseScrutinyDocMap.get(doc.getGmId());

					lookUps.add(lookUp);

					desWiseScrutinyDocMap.put(doc.getGmId(), lookUps);
				} else {
					lookUps = new ArrayList<>(0);

					lookUps.add(lookUp);

					desWiseScrutinyDocMap.put(doc.getGmId(), lookUps);
				}
			}
		}
		return desWiseScrutinyDocMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.service.ScrutinyService#
	 * saveScrutinyValueBylabelId(com.abm.mainetservice.web.common.bean.
	 * ScrutinyLableValueDTO)
	 */
	@Override
	@Transactional
	public boolean saveScrutinyValueBylabelId(final ScrutinyLableValueDTO lableValueDTO) {
		boolean flag = false;
		LOG.info("Start the saveScrutinyValueBylabelId ");
		try {

			if ((lableValueDTO.getApplicationId() != null) && (lableValueDTO.getApplicationId() != 0)
					&& (lableValueDTO.getLableId() != null) && (lableValueDTO.getLableId() != 0)) {
				TbScrutinyLabelValueEntity entity = null;
				final TbScrutinyLabelValueEntityKey key = new TbScrutinyLabelValueEntityKey();
				final Employee employee = new Employee();
				employee.setEmpId(lableValueDTO.getUserId());
				final Organisation organisation = new Organisation();
				organisation.setOrgid(lableValueDTO.getOrgId());
				entity = labelValueEntityJpaRepository.findScrutinyLabelValueData(lableValueDTO.getLableId(),
						lableValueDTO.getApplicationId(), lableValueDTO.getOrgId());

				if (entity == null) {
					entity = new TbScrutinyLabelValueEntity();
					key.setSaApplicationId(lableValueDTO.getApplicationId());
					key.setSlLabelId(lableValueDTO.getLableId());
					entity.setLabelValueKey(key);
					entity.setLangId(lableValueDTO.getLangId().intValue());
					entity.setOrgId(organisation);
					entity.setUserId(employee);
					entity.setLevels(lableValueDTO.getLevel());
					entity.setSvValue(lableValueDTO.getLableValue());
					entity.setLmodDate(new Date());
				} else {
					entity.setSvValue(lableValueDTO.getLableValue());
					entity.setUpdatedBy(employee);
					entity.setUpdatedDate(new Date());
				}

				final TbScrutinyLabelValueEntity upentity = labelValueEntityJpaRepository.save(entity);

				if (upentity != null) {
					if (upentity.getSlLabelId() != null) {

						flag = true;
					}
				}

			}

		} catch (final Exception exception) {
			LOG.error("Exception occur in saveScrutinyValueBylabelId() ", exception);
			return flag;
		}
		return flag;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.service.ScrutinyService#
	 * saveCompleteScrutinyLabel(com.abm.mainetservice.web.common.entity.
	 * TbScrutinyLabelValueEntity)
	 */
	@Override
	@Transactional
	public boolean saveCompleteScrutinyLabel(final List<ViewCFCScrutinyLabelValue> list, final UserSession userSession,
			final ScrutinyLabelDTO scrutinyLabelDTO, final boolean updateFlag, Long taskId, List<Long> attachmentId,
			String wokflowDecision) {
		boolean flag = false;
		LOG.info("Start the saveScrutinyValueBylabelId ");
		TbScrutinyLabelValueEntity scrutinyLabelValue = null;
		TbScrutinyLabelValueEntityKey scrutinyLabelValueKey = null;
		final Date date = new Date();

		try {
			for (final ViewCFCScrutinyLabelValue viewCFCScrutinyLabelValue : list) {
				scrutinyLabelValue = new TbScrutinyLabelValueEntity();

				scrutinyLabelValueKey = new TbScrutinyLabelValueEntityKey();

				if ((viewCFCScrutinyLabelValue.getSaApplicationId() != null)
						&& (viewCFCScrutinyLabelValue.getSaApplicationId() != 0)) {
					scrutinyLabelValueKey.setSaApplicationId(viewCFCScrutinyLabelValue.getSaApplicationId());

				} else {
					scrutinyLabelValueKey.setSaApplicationId(viewCFCScrutinyLabelValue.getApplicationId());

				}

				scrutinyLabelValueKey.setSlLabelId(viewCFCScrutinyLabelValue.getSlLabelId());
				scrutinyLabelValue.setLabelValueKey(scrutinyLabelValueKey);
				scrutinyLabelValue.setLevels(viewCFCScrutinyLabelValue.getLevels());
				scrutinyLabelValue.setSvValue(viewCFCScrutinyLabelValue.getSvValue());
				scrutinyLabelValue.setLangId(userSession.getLanguageId());
				scrutinyLabelValue.setLmodDate(date);
				scrutinyLabelValue.setOrgId(userSession.getOrganisation());
				scrutinyLabelValue.setUpdatedBy(userSession.getEmployee());
				scrutinyLabelValue.setUpdatedDate(date);
				scrutinyLabelValue.setUserId(userSession.getEmployee());
				labelValueEntityJpaRepository.save(scrutinyLabelValue);

			}

			if (updateFlag) {
				final Organisation orgid = UserSession.getCurrent().getOrganisation();

				String processName = serviceMasterService
						.getProcessName(Long.valueOf(scrutinyLabelDTO.getSmServiceId()), orgid.getOrgid());
				if (processName != null) {
					final List<TbLoiMas> mst = loiMasService.getloiByApplicationId(
							Long.valueOf(scrutinyLabelDTO.getApplicationId()),
							Long.valueOf(scrutinyLabelDTO.getSmServiceId()), userSession.getOrganisation().getOrgid());

					final Employee empId = UserSession.getCurrent().getEmployee();

					WorkflowProcessParameter workflowdto = new WorkflowProcessParameter();
					WorkflowTaskAction workflowAction = new WorkflowTaskAction();
					workflowAction.setIsLoiGenerated(false);
					if (mst != null && !mst.isEmpty()) {
						workflowAction.setIsLoiGenerated(true);
						workflowAction.setLoiAmount(mst.get(0).getLoiAmount().doubleValue());
					}
					workflowAction.setTaskId(taskId);
					workflowAction.setApplicationId(Long.valueOf(scrutinyLabelDTO.getApplicationId()));
					workflowAction.setDateOfAction(new Date());
					workflowAction.setDecision(wokflowDecision);
					workflowAction.setOrgId(orgid.getOrgid());
					workflowAction.setEmpId(empId.getEmpId());
					workflowAction.setModifiedBy(empId.getEmpId());
					workflowAction.setEmpType(empId.getEmplType());
					workflowAction.setEmpName(empId.getEmpname());
					workflowAction.setCreatedBy(empId.getEmpId());
					workflowAction.setCreatedDate(new Date());
					workflowAction.setAttachementId(attachmentId);
					if(wokflowDecision.equals("FORWARD_TO_EMPLOYEE") && scrutinyLabelDTO.getForwardToEmployee() != null
							&& !scrutinyLabelDTO.getForwardToEmployee().isEmpty() && scrutinyLabelDTO.getRemark() != null){
					workflowAction.setForwardToEmployee(scrutinyLabelDTO.getForwardToEmployee());
					workflowAction.setComments(scrutinyLabelDTO.getRemark());
					}
					workflowdto.setProcessName(processName);					
					workflowdto.setWorkflowTaskAction(workflowAction);
					workflowExecutionService.updateWorkflow(workflowdto);
					flag = true;

				}
			}

		}

		catch (final Exception exception) {
			LOG.error("Exception occur while calling jbpm workflow in saveCompleteScrutinyLabel() ", exception);
			return flag;
		}
		return flag;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.service.ScrutinyService#
	 * saveAllScrutinyDoc(java.util.List)
	 */
	@Override
	@Transactional
	public boolean saveAllScrutinyDoc(final List<CFCAttachment> scrutinyDocs) {
		boolean flag = false;
		LOG.info("Start the saveAllScrutinyDoc ");
		try {

			for (final CFCAttachment entity : scrutinyDocs) {
				scrutinyDocJpaRepository.save(entity);
			}
			flag = true;
		} catch (final Exception exception) {
			LOG.error("Exception occur in saveAllScrutinyDoc() ", exception);
			return flag;
		}
		return flag;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.service.ScrutinyService#
	 * getValueByLabelQuery(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public String getValueByLabelQuery(final String query) {

		return scrutinyDAO.getValueByLabelQuery(query);
	}

	// US#113590
	@Override
	@WebMethod(exclude = true)
	public Map<String, Long> getApplicationDetails(Long applicatioId) {
		Map<String, Long> result = null;
		Class<?> clazz = null;
		String serviceClassName = null;
		String deptCode = null;
		Object dynamicServiceInstance = null;
		try {
			serviceClassName = "com.abm.mainet.tradeLicense.service.TradeLicenseApplicationServiceImpl";
			if (serviceClassName != null && !MainetConstants.BLANK.equals(serviceClassName)) {

				clazz = ClassUtils.forName(serviceClassName,
						ApplicationContextProvider.getApplicationContext().getClassLoader());
				dynamicServiceInstance = ApplicationContextProvider.getApplicationContext()
						.getAutowireCapableBeanFactory().autowire(clazz, 4, false);
				final Method method = ReflectionUtils.findMethod(clazz, "getItemDetailsByApplicationId",
						new Class[] { Long.class });
				result = (Map<String, Long>) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
						new Object[] { applicatioId });
			}
		} catch (Exception e) {
			throw new FrameworkException("Exception in Objection for finding reference Number  :" + applicatioId, e);
		}
		return result;
	}
	

	@Override
	public Boolean checkLoiGeneratedOrNot(ScrutinyLabelDTO dto, Long desgId,
			List<ViewCFCScrutinyLabelValue> labelList) {
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		TbScrutinyLabelsEntity tbScrtEnt = null;
		if (!CollectionUtils.isEmpty(labelList)) {
			Boolean flag = false;
			for (ViewCFCScrutinyLabelValue vwScrLblVal : labelList) {
				if (vwScrLblVal.getSlFormName() != null
						&& vwScrLblVal.getSlFormName().equals(MainetConstants.SMS_EMAIL_URL.LOI_GENERATION)) {
					flag = true;
					final ServiceMaster servicename = serviceMasterService.getServiceMaster(dto.getSmServiceId(),
							orgId);
					if (dto != null) {
						if (ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class)
								.getDeptCode(servicename.getTbDepartment().getDpDeptid())
								.equals(MainetConstants.TradeLicense.MARKET_LICENSE)
								&& Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(),
										MainetConstants.ENV_SKDCL)) {
							Map<String, Long> result = getApplicationDetails(Long.valueOf(dto.getApplicationId()));
							if (result != null && result.get(MainetConstants.TRI_COD1) != null) {
								List<TbScrutinyLabelsEntity> scrEntList = labelsJpaRepository
										.checkScrutinyLabelExistOrNot(dto.getSmServiceId(), orgId,
												result.get(MainetConstants.TRI_COD1));
								if (scrEntList != null && !scrEntList.isEmpty()) {
									tbScrtEnt = labelsJpaRepository.checkLoiGenerationExistOrNotForSkdcl(
											dto.getSmServiceId(), orgId, desgId, result.get(MainetConstants.TRI_COD1));
								} else {
									tbScrtEnt = labelsJpaRepository.checkLoiGenerationExistOrNotForSkdcl(
											dto.getSmServiceId(), orgId, desgId, 0L);
								}
							}
						} else {
							tbScrtEnt = labelsJpaRepository.checkLoiGenerationExistOrNot(dto.getSmServiceId(), orgId,
									desgId);
						}
						if (tbScrtEnt != null) {
							TbLoiMas mas = loiMasService.getloiByApplicationIdForDeletion(
									Long.valueOf(dto.getApplicationId()), orgId.longValue());
							if (mas != null) {
								return true;
							}
						}
					}
				}
			}
			if (!flag) {
				return true;
			}
		}
		return false;
	}

	// 105334 - to get license end date by ApplicationId
	@Override
	@WebMethod(exclude = true)
	public String getAppDetailByAppId(Long applicationId) {

		String result = null;
		Class<?> clazz = null;
		String serviceClassName = null;
		Object dynamicServiceInstance = null;
		try {
			serviceClassName = "com.abm.mainet.tradeLicense.service.TradeLicenseApplicationServiceImpl";
			if (serviceClassName != null && !MainetConstants.BLANK.equals(serviceClassName)) {

				clazz = ClassUtils.forName(serviceClassName,
						ApplicationContextProvider.getApplicationContext().getClassLoader());
				dynamicServiceInstance = ApplicationContextProvider.getApplicationContext()
						.getAutowireCapableBeanFactory().autowire(clazz, 4, false);
				final Method method = ReflectionUtils.findMethod(clazz, "getLicenseDetailsByApplId",
						new Class[] { Long.class });
				result = (String) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
						new Object[] { applicationId });
			}
		} catch (Exception e) {
			throw new FrameworkException("Exception in Objection for finding reference Number  :" + applicationId, e);
		}
		return result;

	}

	@Override
	public CustomerInfoDTO getCurrentApplicationsConnSize(Long applicationId, Long orgId) {
		CustomerInfoDTO csmrInfo = new CustomerInfoDTO();
		try{
        	Class<?> clazz = null;
            Object dynamicServiceInstance = null;
        	String serviceClassName = "com.abm.mainet.water.service.NewWaterConnectionServiceImpl";
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz, "getCsmrInfoFromApplicationId",
					new Class[] {Long.class, Long.class });
            
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            
            csmrInfo = (CustomerInfoDTO) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { applicationId, orgId });
        } catch (LinkageError | Exception e) {
        	LOG.info("Exception occured while fetching csIdn from applicationId " + e.getMessage());
        }
        return csmrInfo;
	}

	@Override
	@WebMethod(exclude = true)
	@Transactional
	public boolean updateStatusFlagByRefId(String refId, Long orgId,String deptCode,Long empId) {
		boolean flag=false;
		try{
        	Class<?> clazz = null;
            Object dynamicServiceInstance = null;
        	String serviceClassName =  messageSource.getMessage(
					ApplicationSession.getInstance().getMessage("OBJECTION.") + deptCode, new Object[] {},
					StringUtils.EMPTY, Locale.ENGLISH);
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz, "updateStatusFlagByRefId",
					new Class[] {String.class, Long.class,Long.class });
            
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            
            flag =  (boolean) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { refId, orgId ,empId});
        } catch (LinkageError | Exception e) {
        	LOG.info("Exception occured while fetching csIdn from applicationId " + e.getMessage());
        }
		return flag;
	}
	
	@Override
	public Boolean checkConnectionDetailsPresentOrNot(ScrutinyLabelDTO dto, Long desgId,
			List<ViewCFCScrutinyLabelValue> labelList){
		if (!CollectionUtils.isEmpty(labelList)) {
			Boolean flag = false;
			for (ViewCFCScrutinyLabelValue vwScrLblVal : labelList) {
				if (vwScrLblVal.getSlFormName() != null
						&& vwScrLblVal.getSlFormName().equals(MainetConstants.SMS_EMAIL_URL.CONNECTION_DETAIL_URL) &&
						MainetConstants.YES.equalsIgnoreCase(vwScrLblVal.getSvValue())) {
					flag = true;
					CustomerInfoDTO csmrInfo = getCurrentApplicationsConnSize(
							vwScrLblVal.getApplicationId(), vwScrLblVal.getOrgId());
					if (csmrInfo != null) {
						if(csmrInfo.getCsListatus()!= null && !csmrInfo.getCsCcntype().isEmpty()){
							return true;
						}
					}
				}
			}
			if (!flag) {
				return true;
			}
		}
		return false;
	}
}
