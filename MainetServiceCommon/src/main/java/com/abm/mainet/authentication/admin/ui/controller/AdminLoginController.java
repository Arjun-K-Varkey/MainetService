package com.abm.mainet.authentication.admin.ui.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.authentication.admin.ui.model.AdminLoginModel;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.domain.LocationMasEntity;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.master.dto.TbOrganisation;
import com.abm.mainet.common.master.mapper.TbOrganisationServiceMapper;
import com.abm.mainet.common.master.service.GroupMasterService;
import com.abm.mainet.common.master.service.TbOrganisationService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.ILocationMasService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.PasswordManager;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

@Controller
@RequestMapping("/AdminLogin.html")
public class AdminLoginController extends AbstractFormController<AdminLoginModel> {
	 private static Logger log = Logger.getLogger(AdminLoginController.class);
    @Autowired
    private IEmployeeService iEmployeeService;

    @Resource
    private ILocationMasService iLocationMasService;

    @Autowired
    private TbOrganisationServiceMapper tbOrganisationServiceMapper;

    @Autowired
    private TbOrganisationService organisationService;
    
    @Autowired
	private GroupMasterService IgroupMaster;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(final HttpServletRequest request, final HttpServletResponse response) {
        sessionCleanup(request);

        return new ModelAndView(MainetConstants.AdminHome.ADMMIN_LOGIN, MainetConstants.FORM_NAME, new AdminLoginModel());
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String authenticatAdminEmployee(final HttpServletRequest request) {
        final BindingResult bindingResult = bindModel(request);
        String result = "";
        final AdminLoginModel model = getModel();
        final Employee adminEmployee = model.getAdminEmployee();
        final UserSession userSession = UserSession.getCurrent();
        userSession.setOrganisation(adminEmployee.getOrganisation());
        userSession.setLoggedLocId(model.getLoggedLocId());

        if (!bindingResult.hasErrors()) {
        	if(!Utility.passwordLengthValidationCheck(adminEmployee.getEmppassword()).isEmpty()) {
        		return "longPassDenailOfService";
        	}
            if ((adminEmployee.getOrganisation().getOrgid() != 1) || (adminEmployee.getOrganisation().getOrgid() != 0)) {

                final Employee loggedEmployee = model.validateEmployee(adminEmployee.getEmploginname(),
                        adminEmployee.getEmppassword());
                
                if(bindingResult.getAllErrors().size()>0){
                	if(bindingResult.getAllErrors().get(0).getDefaultMessage().equals(MainetConstants.ACCOUNT_LOCK))
                	result=bindingResult.getAllErrors().get(0).getDefaultMessage();     
                }
                	
                if (loggedEmployee != null) {
                    userSession.setContextName(request.getContextPath());
                    if(null != loggedEmployee.getLockUnlock() && loggedEmployee.getLockUnlock().equals("L")){
                    	return "accountLock";
					}
                    if(PasswordManager.isPasswordExpired(loggedEmployee)){
    					return "passwordExpired";
    				}
              
                    if(null!=loggedEmployee.getLoggedIn() && loggedEmployee.getLoggedIn().equals(MainetConstants.Common_Constant.FIRST)) {
                    	return "FirstLogin";
                    }
                    else if (!model.isCitizenEmployee(loggedEmployee) && !model.isAgencyEmployee(loggedEmployee)) {      
                    	
                        final boolean isOTPVerifed = model.isOTPVerificationDone(loggedEmployee);
                        if (isOTPVerifed) {
                        	try {
                        		LookUp lookUp = CommonMasterUtility.getValueFromPrefixLookUp("OTP", "OTP",
    					        		userSession.getOrganisation());
    					        if(lookUp.getOtherField().equals(MainetConstants.FlagY)){
    					        	model.getAdminEmployee().setEmpmobno(loggedEmployee.getEmpmobno());	
    					        	model.setAdminEmployee(loggedEmployee);
    					        	if((adminEmployee.getEmpmobno() != null) && !adminEmployee.getEmpmobno().equalsIgnoreCase("")){
    						        	if(isRegisteredEmployee(request)){	
    						        		try {
    						        			LookUp otpTimerLookup = CommonMasterUtility.getValueFromPrefixLookUp("TIMER", "OTP",
    									        		userSession.getOrganisation());
    							        		if(otpTimerLookup!=null){
    							        			model.setOtpTimer(Long.valueOf(otpTimerLookup.getOtherField()));
    								        		model.startSession(model.getOtpTimer());
    							        		}	
    										} catch (Exception e) {
    											model.setOTPSessionActive(true);
    								            model.setResendOTPFlag(true);
    											logger.info("prefix not found for otp timer");
    										}					        		
    						        		return "otpVerification";
    						        	}
    						        	else{
    						        		return "otpError";}
    					        	}				      				        	
                                }	
							} catch (Exception e) {
								logger.info("prefix not found for otp");
							}

                            result = model.redirectTo(loggedEmployee);
                            loggedEmployee.setEmppiservername(Utility.getClientIpAddress(request));
                            loggedEmployee.setEmpisecuritykey(request.getRequestedSessionId());
                            model.setUserSession(loggedEmployee);
                            final IOrganisationService iOrganisationService = ApplicationContextProvider.getApplicationContext()
                                    .getBean(IOrganisationService.class);
                            final Organisation organisation = iOrganisationService
                                    .getOrganisationById(userSession.getOrganisation().getOrgid());
                            userSession.setOrganisation(organisation);
                            iEmployeeService.setEmployeeLoggedInFlag(loggedEmployee);
                            model.accessListAndMenuForAdmin(loggedEmployee);
                            userSession.setPaymentMode(model.getPaymentModes());
                            userSession.setPosUser(model.getPosUser(loggedEmployee));
                            iEmployeeService.updateEmployeeOtp(loggedEmployee.getEmpId(), MainetConstants.BLANK);
							// User Story #108649
                            //code for setting employee role code
                            if(loggedEmployee!=null) {
                            	GroupMaster gmas=IgroupMaster.findByGmId(loggedEmployee.getGmid(), organisation.getOrgid());
                            	if(gmas!=null)
                            		userSession.setRoleCode(gmas.getGrCode());
                            }
							
							try {
								boolean flag = model.saveEmployeeSession();
								if (flag) {
									log.info(" Successfully save  employee session data at the time of citizen login");
								}
							} catch (Exception e) {
								log.error("Exception occured at the time of saving employee session data ");
							}

                            // userSession.setBankList(ChallanBankDetailsService.getBankList(UserSession.getCurrent().getOrganisation()));                  
                        } else {

                            if ((loggedEmployee.getEmpmobno() != null) && !loggedEmployee.getEmpmobno().equals("")) {
                                model.sendOTPAgain(loggedEmployee);
                                result = model.redirectToOTP();
                                UserSession.getCurrent().setMobileNoToValidate(loggedEmployee.getEmpmobno());
                            }
                        }
                    }
                }

            } else {

                return ApplicationSession.getInstance().getMessage(MainetConstants.AdminHome.ADMIN_LOG_ORG_SELECTED);
            }
        } else {

            return MainetConstants.ERROR;
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params = "passEncrypt")
    public @ResponseBody String getEncrypted(final HttpServletRequest httpServletRequest) {
        bindModel(httpServletRequest);
        final AdminLoginModel model = getModel();
        return model.getEncryptData();
    }

    @RequestMapping(params = "locationList", method = RequestMethod.POST)
    public @ResponseBody List<LocationMasEntity> getLocationList(@RequestParam("orgId") final Long orgId) {
        final List<LocationMasEntity> locationList = iLocationMasService.getlocationByOrgId(orgId);
        final TbOrganisation orgBean = organisationService.findById(orgId);
        final Organisation orgEntity = new Organisation();
        tbOrganisationServiceMapper.mapTbOrganisationToTbOrganisationEntity(orgBean, orgEntity);
        UserSession.getCurrent().setOrganisation(orgEntity); 
        return locationList;
    }
    @RequestMapping(params = "locationsList", method = RequestMethod.POST)
    public @ResponseBody List<Object[]> getLocationsList(@RequestParam("employeeName") final String empName, @RequestParam("orgId") final Long orgId) {
        final List<Object[]> locationList = iLocationMasService.getdefaultLocWithOtherLocByOrgId(empName, orgId);
        final TbOrganisation orgBean = organisationService.findById(orgId);
        final Organisation orgEntity = new Organisation();
        tbOrganisationServiceMapper.mapTbOrganisationToTbOrganisationEntity(orgBean, orgEntity);
        UserSession.getCurrent().setOrganisation(orgEntity); 
        return locationList;
    }
    @RequestMapping(params = "getEmployeeLocation", method = RequestMethod.POST)
    public @ResponseBody LocationMasEntity getEmployeeLocation(@RequestParam("employeeName") final String empName,
            @RequestParam("orgId") final Long orgId) {
        return iEmployeeService.findEmployeeLocation(empName, orgId);
    }
   
  
	@RequestMapping(method = RequestMethod.POST, params = "getChlidList")
	public ModelAndView getChlidList(final HttpServletRequest request, @RequestParam("smfId") Long smfId,
			 @RequestParam("parentId") Long parentId) {
		AdminLoginModel model = getModel();

		final Organisation organisation = UserSession.getCurrent().getOrganisation();
		
		model.accessListAndMenuForChild(UserSession.getCurrent().getEmployee().getGmid(), organisation.getOrgid(),
				smfId, parentId);
		 
		 
		return new ModelAndView("ChildListDetails", MainetConstants.FORM_NAME, getModel());
	}
	
    public @ResponseBody boolean isRegisteredEmployee(final HttpServletRequest request) {
        final BindingResult bindingResult = bindModel(request);
        final AdminLoginModel model = getModel();
        final Employee adminEmployee = model.getAdminEmployee();
        boolean result = false;
        if (!bindingResult.hasErrors()) {
            result = model.ifRegisteredEmployeeThenSendOTP(adminEmployee);           
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.POST, params="verifyOtp")
    public @ResponseBody String verifyOtp(final HttpServletRequest request) {
    	bindModel(request);
        String result = "";
        final AdminLoginModel model = getModel();
        if (model.isOTPSessionActive()) {
        final Employee adminEmployee = model.getAdminEmployee();
        final UserSession userSession = UserSession.getCurrent();
        userSession.setOrganisation(adminEmployee.getOrganisation());
        userSession.setLoggedLocId(model.getLoggedLocId());  	        
        if (adminEmployee != null) {
            userSession.setContextName(request.getContextPath());
            if (!model.isCitizenEmployee(adminEmployee) && !model.isAgencyEmployee(adminEmployee)) {                    	
                model.setResult(model.verficationOfOTP(adminEmployee));
                if (model.getResult().equals("success")) {
                	adminEmployee.setEmppiservername(Utility.getClientIpAddress(request));
                	adminEmployee.setEmpisecuritykey(request.getRequestedSessionId());
                    model.setUserSession(adminEmployee);
                    final IOrganisationService iOrganisationService = ApplicationContextProvider.getApplicationContext()
                            .getBean(IOrganisationService.class);
                    final Organisation organisation = iOrganisationService
                            .getOrganisationById(userSession.getOrganisation().getOrgid());
                    userSession.setOrganisation(organisation);
                    iEmployeeService.updateEmpDetails(adminEmployee.getEmpId(), MainetConstants.FlagY, adminEmployee.getEmpisecuritykey(), adminEmployee.getEmppiservername());
                    model.accessListAndMenuForAdmin(adminEmployee);
                    userSession.setPaymentMode(model.getPaymentModes());
                    iEmployeeService.updateEmployeeOtp(adminEmployee.getEmpId(), MainetConstants.BLANK);
                    if(adminEmployee!=null) {
                    	GroupMaster gmas=IgroupMaster.findByGmId(adminEmployee.getGmid(), organisation.getOrgid());
                    	if(gmas!=null)
                    		userSession.setRoleCode(gmas.getGrCode());
                    }							
					try {
						boolean flag = model.saveEmployeeSession();
						if (flag) {
							log.info(" Successfully save  employee session data at the time of citizen login");
						}
					} catch (Exception e) {
						log.error("Exception occured at the time of saving employee session data ");
					}  
					result = model.redirectTo(adminEmployee);
                } else {
                	result=model.getResult();
                }
            }
        }
        }
        else{
        	result = ApplicationSession.getInstance().getMessage("OTPExpired");
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.GET, params="otpVerificationfrm")
    public ModelAndView otpVerificationForm(final HttpServletRequest request) {
    	 bindModel(request);
        return new ModelAndView("AdminValidateOTP", MainetConstants.FORM_NAME,
                getModel());
    }
    
    @RequestMapping(params = "ResendLoginOTP", method = RequestMethod.POST)
    public @ResponseBody String ResendLoginOTP(final HttpServletRequest request) {
        bindModel(request);
        final AdminLoginModel model = getModel();
        String result = null;
        Employee adminEmployee = model.getAdminEmployee();
        if(model.isResendOTPFlag()){
	        if (adminEmployee != null) {
	            final boolean isValidPeriod = model.ifRegisteredEmployeeThenSendOTP(adminEmployee);
	            if (isValidPeriod) {
	                result = MainetConstants.TRUE;
	                if(model.getOtpTimer()!= null){
	                	 model.startSession(model.getOtpTimer());
	                }	               
	            } else {
	                result = getApplicationSession().getMessage(MainetConstants.AdminForgotpassword.ADMIN_CITIZEN_OTP_SEND_FAIL);
	            }
	        }
        }
        else{
            result = ApplicationSession.getInstance().getMessage("eip.admin.resend.otp.validation");
        }
        return result;
    }
}
