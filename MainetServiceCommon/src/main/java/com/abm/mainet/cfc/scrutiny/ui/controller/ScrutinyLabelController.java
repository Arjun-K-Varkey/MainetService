package com.abm.mainet.cfc.scrutiny.ui.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.cfc.scrutiny.ui.model.ScrutinyLabelModel;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.ui.controller.AbstractController;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

/**
 * @author Rajendra.Bhujbal
 */
@Controller
@RequestMapping("/ScrutinyLabelView.html")
public class ScrutinyLabelController extends AbstractController<ScrutinyLabelModel> {

    @Resource
    IFileUploadService fileUploadService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrutinyLabelController.class);

    @RequestMapping(params = "setViewData", method = RequestMethod.POST)
    public ModelAndView populateViewData(@RequestParam final long appId, @RequestParam final long serviceId,
            @RequestParam final long taskId,
            final HttpServletRequest httpServletRequest) {
        fileUploadService.sessionCleanUpForFileUpload();
        this.getModel().setWokflowDecision(null);
        try {
            this.getModel().setTaskId(taskId);
            if (UserSession.getCurrent() != null) {

                getModel().populateScrutinyViewData(appId, serviceId, UserSession.getCurrent());
                getModel().checkPropertyDues(serviceId, taskId, appId);
                return new ModelAndView("populateViewData", MainetConstants.CommonConstants.COMMAND, getModel());

            } else {
                return defaultExceptionView();
            }
        } catch (final RuntimeException e) {
            LOGGER.error("Exception Occur in populateViewData() ScrutinyLabelController ", e);
            return defaultExceptionView();
        }

    }

    @RequestMapping(params = "getLabelList", method = RequestMethod.POST)
    public @ResponseBody List<Long> getLabelList(@RequestParam final long labelId, final HttpServletRequest httpServletRequest) {
        return getModel().getLabelList(labelId);
    }

    @RequestMapping(params = "getLabelValueByQuery", method = RequestMethod.POST)
    public @ResponseBody Object getLabelValueByQuery(@RequestParam final long labelId, @RequestParam final String labelValue,
            final HttpServletRequest httpServletRequest) {
        return getModel().getLabelValueByQuery(labelId, labelValue);
    }

    @RequestMapping(params = "saveLableValue", method = RequestMethod.POST)
    public @ResponseBody Object saveLableValue(@RequestParam final long labelId, @RequestParam final String labelValue,
            @RequestParam final long level, final HttpServletRequest httpServletRequest) {
        return getModel().saveLableValue(labelId, labelValue, level);
    }

    @RequestMapping(params = "getOpenFormValue", method = RequestMethod.POST)
    public @ResponseBody String getOpenFormValue(@RequestParam final long labelId, @RequestParam final long level,
            final HttpServletRequest httpServletRequest) {
        return getModel().getOpenFormValue(labelId, level);
    }

    @RequestMapping(params = "saveScrutinyValue", method = RequestMethod.POST)
    public ModelAndView saveScrutinyValue(final HttpServletRequest httpServletRequest) {
        bindModel(httpServletRequest);

        final ScrutinyLabelModel model = getModel();
        try {
            if (model.saveScrutinyLabels()) {
                return jsonResult(JsonViewObject.successResult(getModel().getSuccessMessage()));
            } else {
                return jsonResult(JsonViewObject.failureResult(getModel().getSuccessMessage()));
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            LOGGER.error("Exception Occur in saveScrutinyValue() ScrutinyLabelController ", ex);
            return jsonResult(JsonViewObject.failureResult(ex));
        }
    }

    @RequestMapping(params = "setViewDataFromModule", method = RequestMethod.POST)
    public ModelAndView populateViewDataFromModule(final HttpServletRequest httpServletRequest, @RequestParam final long appId,
            @RequestParam("labelId") final long labelId, @RequestParam("serviceId") final long serviceId) {
        getModel().populateScrutinyViewData(appId, serviceId, UserSession.getCurrent());
        return new ModelAndView("populateViewData", MainetConstants.CommonConstants.COMMAND, getModel());
    }

    @RequestMapping(params = "submitScrutinyValue", method = RequestMethod.POST)
    public ModelAndView submitScrutinyValue(final HttpServletRequest httpServletRequest) {
        bindModel(httpServletRequest);

        final ScrutinyLabelModel model = getModel();
        try {
        	
        	//1st check LOI generated or not
        	if(model.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.APPROVED)) {
        		if(MainetConstants.WaterServiceShortCode.NEW_WATER_SERVICE.equals(model.getScrutinyLabelDTO().getServiceName())) {
    				if(!model.isSiteInspectionAndMeterTypeEntryDone() && model.hasValidationErrors()) {
	        			ModelAndView mv = defaultResult();
						mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
		
						return mv;
    				}
    				if(Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)){
    					if(!model.isConnectionDetailsPresent() && model.hasValidationErrors()){
        					ModelAndView mv = defaultResult();
        					mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
        					return mv;
        				}
    				}	
        		}
        		else if(!model.isScrutinyLoiGenerated() && model.hasValidationErrors()) {
        		
	        		ModelAndView mv = defaultResult();
					mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
					return mv;
        		}
        	}
        	boolean submitFlag = model.submitScrutinyLabels();
    		if (submitFlag && model.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.APPROVED)) {
                return jsonResult(JsonViewObject.successResult(getApplicationSession().getMessage("cfc.label.approved")));
        	    
            } else if (submitFlag && model.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.FORWARD_TO_EMPLOYEE)) {
        	    return jsonResult(JsonViewObject.successResult(getApplicationSession().getMessage("cfc.label.forward.to.employee")));
            } else if(submitFlag && model.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.SEND_BACK)) {
            	return jsonResult(JsonViewObject.successResult(getApplicationSession().getMessage("cfc.sendtoback.save")));
            }else if(submitFlag && model.getWokflowDecision().equals(MainetConstants.WorkFlow.Decision.SEND_BACK_TO_CHECKLIST_VERIFICATION)) {
            	return jsonResult(JsonViewObject.successResult(getApplicationSession().getMessage("cfc.sendback.to.verification")));
            }else { 
            return jsonResult(JsonViewObject.failureResult(getApplicationSession().getMessage("cfc.label.rejected")));
            }
        	
        } catch (final Exception ex) {
            ex.printStackTrace();
            LOGGER.error("Exception Occur in submitScrutinyValue() ScrutinyLabelController ", ex);
            return jsonResult(JsonViewObject.failureResult(ex));
        }
    }

    @RequestMapping(params = "sendToback", method = RequestMethod.POST)
    public ModelAndView sendToback(final HttpServletRequest httpServletRequest) {
        bindModel(httpServletRequest);
        httpServletRequest.getAttribute("level");
        final ScrutinyLabelModel model = getModel();
        try {
            if (model.sendTobackEmpl()) {
                return jsonResult(JsonViewObject.successResult(getModel().getSuccessMessage()));
            } else {
                return jsonResult(JsonViewObject.failureResult(getModel().getSuccessMessage()));
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            LOGGER.error("Exception Occur in sendToback() ScrutinyLabelController ", ex);
            return jsonResult(JsonViewObject.failureResult(ex));
        }
    }

}
