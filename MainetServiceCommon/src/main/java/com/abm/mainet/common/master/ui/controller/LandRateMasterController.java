package com.abm.mainet.common.master.ui.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.integration.dms.service.IFileUploadService;
import com.abm.mainet.common.master.dto.LandRateMasterBean;
import com.abm.mainet.common.master.dto.LandRateMaterDTO;
import com.abm.mainet.common.master.service.LandRateMasterService;
import com.abm.mainet.common.master.service.TbCfcApplicationMstService;
import com.abm.mainet.common.ui.controller.telosys.AbstractController;
import com.abm.mainet.common.user.activity.dto.UserActivityDTO;
import com.abm.mainet.common.user.activity.service.UserActivityService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

@Controller
@RequestMapping("/LandRateMasterController.html")
public class LandRateMasterController extends AbstractController {

	

	@Autowired
	private IFileUploadService fileUpload;

	@Autowired
	private TbCfcApplicationMstService tbCfcApplicationMstService;
	
	@Autowired
	private LandRateMasterService landRateMasterService;

	private static final String JSP_FORM = "LandRateMaster/form";
	private static final String JSP_VIEW = "LandRateMaster/view";
	private static final String JSP_LIST = "LandRateMaster/list";
	private static final String MAIN_ENTITY_NAME = "LandRateMaster";
	
	public LandRateMasterController() {
		super(LandRateMasterController.class, "landRateMasterBean");
	
	}
	 @InitBinder
	    public void initBinder(final WebDataBinder binder) {
	    	binder.setAutoGrowCollectionLimit(5000);
	        final SimpleDateFormat dateFormat = new SimpleDateFormat(MainetConstants.DATE_FORMAT);
	        binder.registerCustomEditor(Date.class, MainetConstants.AccountDepositSlip.CLEARING_DATE,
	                new CustomDateEditor(dateFormat, true));
	        binder.registerCustomEditor(Date.class, MainetConstants.AccountDepositSlip.PAY_ORDER_DATE,
	                new CustomDateEditor(dateFormat, true));
	        
	    }

	@RequestMapping()
	public String index(HttpServletRequest request,final Model model) throws Exception {
		
		fileUpload.sessionCleanUpForFileUpload();
		LandRateMasterBean bean =new LandRateMasterBean();
		bean.setBtnShow("N") ;
		List<LookUp> areaList=CommonMasterUtility.getListLookup("RMA", UserSession.getCurrent().getOrganisation());
		List<LookUp> surveyType=CommonMasterUtility.getListLookup("SUT", UserSession.getCurrent().getOrganisation());
		model.addAttribute("area", areaList);
		model.addAttribute("surveyType", surveyType);
		model.addAttribute("landRateMasterBean", bean);
		return JSP_LIST;

	}

	@RequestMapping(params = "getjqGridsearch")
	public ModelAndView getCheqData(final HttpServletRequest request, final Model model,
			@RequestParam("areaId") final String areaId, @RequestParam("surveyNO") final String surveyNO) {
	
		List<LookUp> areaList=CommonMasterUtility.getListLookup("RMA", UserSession.getCurrent().getOrganisation());
		model.addAttribute("area", areaList);
		
		List<LookUp> surveyType=CommonMasterUtility.getListLookup("SUT", UserSession.getCurrent().getOrganisation());
		model.addAttribute("surveyType", surveyType);
		LandRateMasterBean bean =new LandRateMasterBean();
	
		bean.getRateMaterDTO().setArea(areaId);
		List<LandRateMaterDTO> masterList = new ArrayList();
		if (StringUtils.isNotBlank(areaId)) {
			List<Object[]> objList = landRateMasterService.getLandRateDetailsList(areaId, surveyNO);

			if (CollectionUtils.isNotEmpty(objList)) {
				for (Object[] arr : objList) {
					LandRateMaterDTO dto = new LandRateMaterDTO();
					if (arr[0] != null)
						dto.setArea(arr[0].toString());
					if (arr[2] != null)
						dto.setSurveyNo(arr[2].toString());
					if (arr[1] != null)
						dto.setSurveyType(arr[1].toString());
					if (arr[3] != null) {
						dto.setFromDate(Utility.dateToString((Date)arr[3]));
						dto.setLmFromDate((Date)arr[3]);
					}
					if (arr[4] != null) {
						dto.setToDate(Utility.dateToString((Date)arr[4]));
						dto.setLmToDate((Date)arr[4]);
						}
					if (arr[5] != null)
						dto.setAmt(arr[5].toString());
					if (arr[6] != null)
						dto.setTbAreaLandId(Long.valueOf(arr[6].toString()));
					if (arr[7] != null)
						dto.setOrgId(Long.valueOf(arr[7].toString()));
					if (arr[8] != null)
						dto.setCreatedBy(Long.valueOf(arr[8].toString()));
					if (arr[9] != null)
						dto.setCreatedDate((Date) arr[9]);
					if (arr[10] != null)
						dto.setLgIpMac(arr[10].toString());
					masterList.add(dto);

				}
				bean.setLndRateList(masterList);
			
			}else {
				bean.setLndRateList(new ArrayList<>());
			}
			bean.setBtnShow("Y");
		}
		model.addAttribute("landRateMasterBean", bean);
		return new ModelAndView("LandRateMaster/listvalidn", MainetConstants.FORM_NAME, bean);
	}

	@RequestMapping(params = "saveLandRate", method = RequestMethod.POST)
	@ResponseBody
	public String saveLandRate(LandRateMasterBean bean, final BindingResult bindingResult,
            final Model model, final HttpServletRequest httpServletRequest) throws IOException {
		
			if (landRateMasterService.saveForm(bean)) {
				saveUserActivity(UserSession.getCurrent(), bean);
				return ApplicationSession.getInstance().getMessage("prop.master.lr.save");

			} else
				return  ApplicationSession.getInstance().getMessage("prop.master.lr.save.error");
		}

	private void saveUserActivity(UserSession session, LandRateMasterBean bean) {
		UserActivityDTO dto = new UserActivityDTO();
		dto.setApplicationId(null);
		dto.setCreatedBy(session.getEmployee().getEmpId());
		dto.setCreatedDate(new Date());
		dto.setLgIpMac(session.getEmployee().getEmppiservername());
		dto.setOrgId(session.getOrganisation().getOrgid());
		dto.setServiceId(null);
		dto.setReferenceNo(bean.getRateMaterDTO().getArea());			
		dto.setAction("Land Rate Master");
		ApplicationContextProvider.getApplicationContext().getBean(UserActivityService.class).saveUserActivity(dto);
	}

}
