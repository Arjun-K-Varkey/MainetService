package com.abm.mainet.authentication.admin.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.abm.mainet.authentication.ldap.IAuthenticationManager;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.EmployeeSession;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.CFCSchedulingCounterDet;
import com.abm.mainet.common.entitlement.domain.RoleEntitlement;
import com.abm.mainet.common.entitlement.service.IEntitlementService;
import com.abm.mainet.common.entitlement.service.MenuRoleEntitlement;
import com.abm.mainet.common.master.service.TbCfcApplicationMstService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.utility.UtilityService;
import com.abm.mainet.smsemail.dto.SMSAndEmailDTO;
import com.abm.mainet.smsemail.service.ISMSAndEmailService;

@Component
@Scope(value = "session")
public class AdminLoginModel extends AbstractFormModel implements Serializable {
	private static final long serialVersionUID = -5566262411904099203L;

	@Autowired
	@Qualifier("authManager")
	private IAuthenticationManager authManager;

	private Employee adminEmployee;

	@Autowired
	private IEmployeeService iEmployeeService;

	@Autowired
	private ISMSAndEmailService ismsAndEmailService;

	@Autowired
	private IEntitlementService iEntitlementService;

	@Autowired
	private IOrganisationService organisationService;
	
	@Autowired
	private TbCfcApplicationMstService tbCfcservice;
	
	private Long loggedLocId;
	
	private boolean accountLock;
	
	private boolean isTemplateAvailable = true;

	private static Logger logger = Logger.getLogger(AdminLoginModel.class);
	
	private String result;
			
	private boolean isOTPSessionActive = false;
	
	private boolean resendOTPFlag = false;
	
	private Long otpTimer;
	

	/**
	 *
	 * @param empLoginString
	 * @param empPassword
	 * @return Employee Object if user provide valid credential
	 */
	public Employee validateEmployee(final String empLoginString, String empPassword) {
		Employee validateEmployee = null;
		empPassword = new String(Base64.getDecoder().decode(empPassword));
		empPassword = Utility.encryptPassword(empLoginString, empPassword);

		final boolean isLdapAuthenticated = authManager.authenticateUser(adminEmployee.getEmploginname() +"_"+getUserSession().getOrganisation().getOrgid(),
				empPassword);
		if (isLdapAuthenticated) {
			
		if (Pattern.compile(MainetConstants.EMAIL_PATTERN).matcher(empLoginString).matches()) {
			final List<Employee> employeeListByEmpEMail = iEmployeeService.getEmployeeByEmpEMailAndType(empLoginString,
					getUserSession().getEmployee().getEmplType(), getUserSession().getOrganisation(),
					MainetConstants.IsDeleted.ZERO, false);
			if (employeeListByEmpEMail.size() == 1) {
				validateEmployee = employeeListByEmpEMail.get(0);
			} else {
				for (final Employee employee : employeeListByEmpEMail) {
					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}

				}
			}

		} else if (Pattern.compile(MainetConstants.MOB_PATTERN).matcher(empLoginString).matches()) {

			final List<Employee> employeeListByEmpMob = iEmployeeService.getEmployeeByEmpMobileNoAndType(empLoginString,
					getUserSession().getEmployee().getEmplType(), getUserSession().getOrganisation(),
					MainetConstants.IsDeleted.ZERO, false);
			if (employeeListByEmpMob.size() == 1) {
				validateEmployee = employeeListByEmpMob.get(0);
			} else {
				for (final Employee employee : employeeListByEmpMob) {

					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}
				}
			}

		}

		else {

			final List<Employee> empListByLoginName = iEmployeeService.getEmployeeListByLoginName(empLoginString,
					getUserSession().getOrganisation(), MainetConstants.IsDeleted.ZERO);
			if (empListByLoginName.size() == 1) {
				validateEmployee = empListByLoginName.get(0);
			} else {
				for (final Employee employee : empListByLoginName) {
					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}

				}
			}

		}

		if (validateEmployee != null) {
			final Long emplType = validateEmployee.getEmplType();
			final String empLoginName = validateEmployee.getEmploginname();
			boolean loginStatus = false;
			if (validateEmployee.getAutMob().equals("Y")) {
				if (empLoginName != null) {
					// Query to get user details
					validateEmployee = iEmployeeService.getAdminEncryptAuthenticatedEmployee(empLoginName, null,
							emplType, validateEmployee.getEmpId(), getUserSession().getOrganisation(),
							MainetConstants.IsDeleted.ZERO);
					// if user password got matches with entered encrypted password
					if (validateEmployee.getEmppassword().equals(empPassword)) {
						validateEmployee.setLoggedInAttempt(null);
						loginStatus = true;

					} else {
						// if password not match increment login attempt
						Integer maxLoginAttempts = null;
						try {
							maxLoginAttempts = Integer.valueOf(getAppSession().getMessage("admin.max.attempts").trim());
						} catch (NumberFormatException e) {
							maxLoginAttempts = 3;
						}
						if (null == validateEmployee.getLoggedInAttempt()
								|| validateEmployee.getLoggedInAttempt() < maxLoginAttempts) {
							validateEmployee.setLoggedInAttempt(validateEmployee.getLoggedInAttempt() != null
									? validateEmployee.getLoggedInAttempt() + 1
									: 1);
							loginStatus = false;
							if(validateEmployee.getLoggedInAttempt()==maxLoginAttempts){
								addValidationError(MainetConstants.ACCOUNT_LOCK);
							}
						} else {
							// lock account if max attempted reached.
							if (StringUtils.isEmpty( validateEmployee.getLockUnlock())) {
								validateEmployee.setLockUnlock("L");
								validateEmployee.setLockDate(new Date());
							}
							loginStatus = true;
						}

					}

					// UPdate Employee attempt count and lock unlock status
					iEmployeeService.updateEmpDetails(validateEmployee);

					// Login status is false return null as expected in case of failed attempted
					if (loginStatus == false) {
						validateEmployee = null;
					}
				}
			}
		}
		return validateEmployee;
		}else {
			return validateEmployee;
		}
	}

	/**
	 * To check valid citizen.
	 * 
	 * @param citizen
	 * @return true/false
	 */
	public boolean isCitizenEmployee(final Employee citizen) {
		if ((citizen.getEmplType() != null) && (citizen.getEmplType() != 0l) && checkNECType(citizen.getEmplType())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAgencyEmployee(final Employee agency) {
		if ((agency.getEmplType() != null) && (agency.getEmplType() != 0l) && checkNECType(agency.getEmplType())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * From citizen login only NEC type employee can able to login. Method will
	 * check login request is given by an NEC type employee.
	 * 
	 * @param emplTypeId
	 * @return true/false
	 * @since Add on 14 Feb 2014 as per instruction given.
	 */
	public boolean checkNECType(final long emplTypeId) {
		final List<LookUp> lookUpList = super.getLookUpList(PrefixConstants.NEC.PARENT);

		for (final LookUp lookUp : lookUpList) {
			if (lookUp.getLookUpId() == emplTypeId) {
				return true;
			}
		}
		return false;
	}

	public boolean isOTPVerificationDone(final Employee validEmployee) {
		if ((validEmployee.getAutMob() != null) && !validEmployee.getAutMob().equalsIgnoreCase("N")) {
			return true;
		} else {
			getBindingResult().addError(new ObjectError("", "OTP Verification Not Done."));
			return false;
		}
	}

	/**
	 *
	 * @param loggedEmployee
	 */
	public void sendOTPAgain(final Employee loggedEmployee) {

		final String newOTPPassword = iEmployeeService.generateNewPassword(MainetConstants.OTP_PRASSWORD_LENGTH);

		final List<Employee> employeeList = iEmployeeService.getEmployeeByEmpMobileNo(loggedEmployee.getEmpmobno(),
				null, MainetConstants.IsDeleted.ZERO);
		int count = 0;
		for (final Employee employee : employeeList) {
			if (employee.getEmplType() == null) {
				if (employee.getAutMob().equals(MainetConstants.Common_Constant.YES)) {
					employee.setAutMob(MainetConstants.Common_Constant.NO);
				}
				employee.setMobNoOtp(Utility.encryptPassword(employee.getEmploginname(), newOTPPassword));
					iEmployeeService.updateEmployeeDetails(employee, getUserSession().getEmployee());
				UserSession.getCurrent().setMobileNoToValidate(loggedEmployee.getEmpmobno());
				if (count == 0) {
					sendSMSandEMail(employee, newOTPPassword);
					count++;
				}
			}
		}

		/*
		 * //loggedEmployee.setEmppassword(Utility.encryptPassword(loggedEmployee.
		 * getEmploginname(), newOTPPassword)); final Employee savedEmployee =
		 * iEmployeeService.updateEmployeeDetails(loggedEmployee,
		 * UserSession.getCurrent().getEmployee()); if ((savedEmployee != null) &&
		 * savedEmployee.getAutMob().equalsIgnoreCase("N")) {
		 * sendSMSandEMail(loggedEmployee, newOTPPassword);
		 * UserSession.getCurrent().setMobileNoToValidate(loggedEmployee.getEmpmobno());
		 * }
		 */

	}

	/**
	 *
	 * @param registeredEmployee
	 * @param newAutoGeneratePwd
	 */
	public void sendSMSandEMail(final Employee registeredEmployee, final String newAutoGeneratePwd) {

		final SMSAndEmailDTO dto = new SMSAndEmailDTO();
		dto.setEmail(registeredEmployee.getEmpemail());
		dto.setMobnumber(registeredEmployee.getEmpmobno());
		dto.setOneTimePassword(newAutoGeneratePwd);
		dto.setAppName(registeredEmployee.getFullName());
		dto.setEmpName(registeredEmployee.getFullName());
		// Added Changes As per told by Rajesh Sir For Sms and Email
		dto.setUserId(registeredEmployee.getEmpId());
		if (UserSession.getCurrent().getLanguageId() == MainetConstants.ENGLISH) {
			dto.setV_muncipality_name(UserSession.getCurrent().getOrganisation().getONlsOrgname());
		} else {
			dto.setV_muncipality_name(UserSession.getCurrent().getOrganisation().getONlsOrgnameMar());
		}
		dto.setOrganizationName(registeredEmployee.getOrganisation().getONlsOrgname());

		int langId = Utility.getDefaultLanguageId(registeredEmployee.getOrganisation());
		ismsAndEmailService.sendEmailSMS(MainetConstants.DEPT_SHORT_NAME.CFC_CENTER,
				MainetConstants.SMS_EMAIL_URL.REGISTRATION, PrefixConstants.SMS_EMAIL_ALERT_TYPE.OTP_MSG, dto,
				organisationService.getSuperUserOrganisation(), langId);

		getBindingResult().addError(new ObjectError("", getAppSession().getMessage("Employee.registrationDone")));
	}

	public String redirectToOTP() {
		final String redirectTo = getAppSession().getMessage("admin.OTPVerification.URL");

		return redirectTo.trim();
	}

	public void setUserSession(final Employee loggedEmployee) {
		final UserSession userSession = getUserSession();
		userSession.setEmployee(loggedEmployee);
	}

	public String redirectTo(final Employee loggedEmployee) {
		String redirectTo = null;
		if(loggedEmployee.getDesignation().getDsgshortname().equals(MainetConstants.WorkFlow.Designation.DSGN_SHORTNAME)) {	
			 redirectTo = getAppSession().getMessage("operator.login.success.redirectURL");
		}else
			 redirectTo = getAppSession().getMessage("admin.login.success.redirectURL");
		
		return redirectTo.trim();
	}

	/**
	 * @return the adminEmployee
	 */
	public Employee getAdminEmployee() {
		return adminEmployee;
	}

	/**
	 * @param adminEmployee
	 *            the adminEmployee to set
	 */
	public void setAdminEmployee(final Employee adminEmployee) {
		this.adminEmployee = adminEmployee;
	}

	public String getEncryptData() {

		final String userId = getAdminEmployee().getEmploginname();
		final String empPassword = getAdminEmployee().getEmppassword();
		Employee validateEmployee = null;

		if (Pattern.compile(MainetConstants.EMAIL_PATTERN).matcher(userId).matches()) {
			final List<Employee> employeeListByEmpEMail = iEmployeeService.getEmployeeByEmpEMailAndType(userId,
					getUserSession().getEmployee().getEmplType(), getUserSession().getOrganisation(),
					MainetConstants.IsDeleted.ZERO, false);
			if (employeeListByEmpEMail.size() == 1) {
				validateEmployee = employeeListByEmpEMail.get(0);
			} else {
				for (final Employee employee : employeeListByEmpEMail) {
					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}

				}
			}

		} else if (Pattern.compile(MainetConstants.MOB_PATTERN).matcher(userId).matches()) {

			final List<Employee> employeeListByEmpMob = iEmployeeService.getEmployeeByEmpMobileNoAndType(userId,
					getUserSession().getEmployee().getEmplType(), getUserSession().getOrganisation(),
					MainetConstants.IsDeleted.ZERO, false);
			if (employeeListByEmpMob.size() == 1) {
				validateEmployee = employeeListByEmpMob.get(0);
			} else {
				for (final Employee employee : employeeListByEmpMob) {

					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}
				}
			}

		}

		else {
			final List<Employee> empListByLoginName = iEmployeeService.getEmployeeListByLoginName(userId,
					getUserSession().getOrganisation(), MainetConstants.IsDeleted.ZERO);
			if (empListByLoginName.size() == 1) {
				validateEmployee = empListByLoginName.get(0);
			} else {
				for (final Employee employee : empListByLoginName) {
					if (employee.getEmplType() == null) {
						validateEmployee = employee;
					}

				}
			}

		}
		if (validateEmployee != null) {
			return Utility.encryptPassword(validateEmployee.getEmploginname(), empPassword);
		} else {
			return "";
		}

	}

	public void accessListAndMenuForAdmin(final Employee loggedEmployee) {
		final Organisation organisation = UserSession.getCurrent().getOrganisation();
		if (loggedEmployee.getGmid() != null) {
			MenuRoleEntitlement.getCurrentMenuRoleManager().getMenuList(loggedEmployee.getGmid(),
					organisation.getOrgid());
		} else {
			final Long groupId = iEntitlementService.getGroupIdByName(MainetConstants.MENU.PORTAL_LOGIN,
					organisation.getOrgid());
			MenuRoleEntitlement.getCurrentMenuRoleManager().getSpecificMenuList(groupId, organisation.getOrgid());
		}
	}

	/**
	 * @return
	 */
	public List<LookUp> getPaymentModes() {
		final List<LookUp> finalLookUp = new ArrayList<>();
		try {
			final List<LookUp> lookUps = this.getLevelData(PrefixConstants.LookUpPrefix.PAY_AT_COUNTER);
			for (final LookUp lookUp : lookUps) {
				if ("Y".equals(lookUp.getOtherField())) {
					finalLookUp.add(lookUp);
				}
			}

		} catch (final Exception e) {
			logger.info("Payment mode didnt get loaded on startup");
		}
		return finalLookUp;
	}

	/**
	 * @return the loggedLocId
	 */
	public Long getLoggedLocId() {
		return loggedLocId;
	}

	/**
	 * @param loggedLocId
	 *            the loggedLocId to set
	 */
	public void setLoggedLocId(final Long loggedLocId) {
		this.loggedLocId = loggedLocId;
	}
	
	public boolean isAccountLock() {
		return accountLock;
	}

	public void setAccountLock(boolean accountLock) {
		this.accountLock = accountLock;
	}

	public boolean isTemplateAvailable() {
		return isTemplateAvailable;
	}

	public void setTemplateAvailable(boolean isTemplateAvailable) {
		this.isTemplateAvailable = isTemplateAvailable;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public boolean isOTPSessionActive() {
		return isOTPSessionActive;
	}

	public void setOTPSessionActive(boolean isOTPSessionActive) {
		this.isOTPSessionActive = isOTPSessionActive;
	}
	
	public boolean isResendOTPFlag() {
		return resendOTPFlag;
	}

	public void setResendOTPFlag(boolean resendOTPFlag) {
		this.resendOTPFlag = resendOTPFlag;
	}
	
	public Long getOtpTimer() {
		return otpTimer;
	}

	public void setOtpTimer(Long otpTimer) {
		this.otpTimer = otpTimer;
	}
	
	public boolean saveEmployeeSession() {
		EmployeeSession empSession = new EmployeeSession();
		empSession.setDateOfAction(new Date());
		empSession.setLoginDate(new Date());
		// empSession.setLogOutDate(new Date());
		if (UserSession.getCurrent().getOrganisation() != null)
			empSession.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
		empSession.setTransMode(MainetConstants.FlagS);
		if (UserSession.getCurrent().getEmployee() != null)
			empSession.setEmpId(UserSession.getCurrent().getEmployee().getEmpId());
		return iEmployeeService.saveEmployeeSession(empSession);
	}

	public Set<RoleEntitlement> accessListAndMenuForChild(Long roleId, Long orgId, Long smfId, Long parentId) {
		if (UserSession.getCurrent().getEmployee().getGmid() != null)
			return (MenuRoleEntitlement.getCurrentMenuRoleManager().getChildListBySmfId(roleId, orgId, smfId,
					parentId));
		return null;
 
	}
	public boolean ifRegisteredEmployeeThenSendOTP(Employee adminEmployee) {
		boolean result = false;
		if (adminEmployee != null && adminEmployee.getEmpmobno()!= null) {
			final String otpNo = UtilityService.generateRandomNumericCode(MainetConstants.OTP_PRASSWORD_LENGTH);
			if (adminEmployee.getEmplType() == null) {
				String empOTP = Utility.encryptPassword(adminEmployee.getEmploginname(), otpNo);
				iEmployeeService.updateEmployeeOtp(Long.valueOf(adminEmployee.getEmpId()), empOTP);
					if (!sendSMSandEMailOTP(adminEmployee, otpNo)) {
						setTemplateAvailable(false);
						return false;
					}
				result = true;
			}
		}
		return result;
	}
	
	private boolean sendSMSandEMailOTP(final Employee employee, final String otp) {
		final SMSAndEmailDTO dto = new SMSAndEmailDTO();
		dto.setEmail(employee.getEmpemail());
		dto.setMobnumber(employee.getEmpmobno());
		dto.setServDur(employee.getEmpname());
		dto.setEmpName(employee.getEmpname());
		dto.setOrgId(employee.getOrganisation().getOrgid());
		dto.setOneTimePassword(otp);
		// Added Changes As per told by Rajesh Sir For Sms and Email
		dto.setUserId(employee.getEmpId());
		if (employee.getOrganisation() != null)
			dto.setOrganizationName(employee.getOrganisation().getONlsOrgname());
		int langId = Utility.getDefaultLanguageId(employee.getOrganisation());
		dto.setLangId(langId);
		return ismsAndEmailService.sendEmailSMS(MainetConstants.DEPT_SHORT_NAME.CFC_CENTER,
				MainetConstants.SMS_EMAIL_URL.REGISTRATION, PrefixConstants.SMS_EMAIL_ALERT_TYPE.OTP_MSG, dto,
				employee.getOrganisation(), langId);
	}
	
	public String verficationOfOTP(Employee adminEmployee) {
		String result = getAppSession().getMessage("eip.citizen.forgotPassword.otpVerificationFailed");
		if (adminEmployee != null) {
			byte[] decode = Base64.getDecoder().decode(getAdminEmployee().getMobNoOtp()); 
			String otpNo = new String(decode);
			if (adminEmployee.getEmplType() == null) {
				final Employee authEmployee = iEmployeeService.getAuthenticatedEmployee(
						adminEmployee.getEmploginname(), otpNo, adminEmployee.getEmplType(),
						null, MainetConstants.IsDeleted.ZERO);
				if (authEmployee != null) {
					result = "success";
				} else {
					result = getAppSession().getMessage("eip.admin.invalidOTP");
					this.addValidationError(
							getAppSession().getMessage("eip.admin.invalidOTP"));
				}
			}
		}
		return result;
	}
	
	public void startSession(Long duration) {
		final long sessionDuration = duration * 60 * 1000;
		setOTPSessionActive(true);
		setResendOTPFlag(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               // Session expired
               setOTPSessionActive(false);
               setResendOTPFlag(true);
               iEmployeeService.updateEmployeeOtp(adminEmployee.getEmpId(), MainetConstants.BLANK);
               logger.info("session expired");
            }
            }, sessionDuration);
	}

	public String getPosUser(Employee loggedEmployee) {
		try {
			LookUp lookUp = CommonMasterUtility.getValueFromPrefixLookUp("CST", MainetConstants.ENV,
					UserSession.getCurrent().getOrganisation());
			CFCSchedulingCounterDet counterDet = null;
			if (lookUp != null && lookUp.getOtherField().equals(MainetConstants.FlagY))
				counterDet = tbCfcservice.getCounterDetByEmpId(loggedEmployee.getEmpId(),
						loggedEmployee.getOrganisation().getOrgid());
			if ((counterDet != null && !counterDet.getScheduleSts().equals(MainetConstants.FlagI))) {
				return MainetConstants.FlagY;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
