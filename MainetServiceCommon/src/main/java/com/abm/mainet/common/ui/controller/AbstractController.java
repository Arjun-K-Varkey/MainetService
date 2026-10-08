package com.abm.mainet.common.ui.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.CommonHelpDocs;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dms.fileUpload.FileUploadUtility;
import com.abm.mainet.common.integration.dms.service.IDmsService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.ui.model.AbstractModel;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.Filepaths;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

@Component
public abstract class AbstractController<TModel extends AbstractModel> {

    private final Class<TModel> modelClass;
    private final String viewName;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IDmsService iDmsService;

    @Autowired
    private Validator validator;

    @Autowired
    private IEmployeeService iEmployeeService;
    
    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        AbstractModel.registerCustomEditors(binder);
      
    }

    public <T> void validateConstraints(T bean, Class<T> beanClass, BindingResult bindResult) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (violations.size() == 0) {
            return;
        }
        for (ConstraintViolation<T> violation : violations) {
            bindResult.addError(new ObjectError(violation.getRootBeanClass().getName(), violation.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    public AbstractController() {
        this.modelClass = (Class<TModel>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        final String controllerType = this.getClass().getSimpleName();
        this.viewName = controllerType.substring(0, controllerType.lastIndexOf(MainetConstants.CONTROLLER));

    }

    protected String getViewName() {
        return viewName;
    }

    protected ModelAndView getScrutinyView() {
        final String sViewName = viewName + MainetConstants.Common_Constant.VIEW;

        return new ModelAndView(sViewName, MainetConstants.CommonConstants.COMMAND, this.getModel());
    }

    /**
     * @return
     */
    protected ApplicationSession getApplicationSession() {
        return (ApplicationSession) ApplicationContextProvider.getApplicationContext().getBean("applicationSession");
    }

    /**
     * @return
     */
    public TModel getModel() {
        return ApplicationContextProvider.getApplicationContext().getBean(this.modelClass);
    }

    public BindingResult bindModel(final HttpServletRequest request) {
        return getModel().bind(request);
    }

    /**
     * @return
     */
    public ModelAndView index() {
        return defaultResult();
    }

    protected ModelAndView jsonResult(final Object bean) {
        ModelAndView mv;

        if (bean == null) {
            mv = new ModelAndView(new MappingJackson2JsonView());
        } else {
            mv = new ModelAndView(new MappingJackson2JsonView(), MainetConstants.FORM_NAME, bean);
        }

        return mv;
    }

    protected ModelAndView getResubmissionView() {
        final String resubmissionName = viewName + MainetConstants.RESUBMISSION;

        return new ModelAndView(resubmissionName, MainetConstants.CommonConstants.COMMAND, this.getModel());
    }

    /**
     * @return
     */
    protected ModelAndView defaultResult() {
        final TModel model = getModel();

        model.getAppSession().setLangId(UserSession.getCurrent().getLanguageId());

        final ModelAndView mv = new ModelAndView(viewName, MainetConstants.FORM_NAME, model);

        final BindingResult bindingResult = model.getBindingResult();

        if (bindingResult != null) {
            mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, model.getBindingResult());
        }

        return mv;
    }

    protected ModelAndView customResult(final String viewName) {
        final TModel model = getModel();

        model.getAppSession().setLangId(UserSession.getCurrent().getLanguageId());

        final ModelAndView mv = new ModelAndView(viewName, MainetConstants.FORM_NAME, model);

        final BindingResult bindingResult = model.getBindingResult();

        if (bindingResult != null) {
            mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, model.getBindingResult());
        }

        return mv;
    }

    protected ModelAndView defaultMyResult() {
        final TModel model = getModel();

        model.getAppSession().setLangId(UserSession.getCurrent().getLanguageId());

        final ModelAndView mv = new ModelAndView(viewName + MainetConstants.VALIDN_SUFFIX, MainetConstants.FORM_NAME, model);

        final BindingResult bindingResult = model.getBindingResult();

        if (bindingResult != null) {
            mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, model.getBindingResult());
        }

        return mv;
    }

    protected ModelAndView customDefaultMyResult(final String viewName) {
        final TModel model = getModel();

        model.getAppSession().setLangId(UserSession.getCurrent().getLanguageId());

        final ModelAndView mv = new ModelAndView(viewName + MainetConstants.VALIDN_SUFFIX, MainetConstants.FORM_NAME, model);

        final BindingResult bindingResult = model.getBindingResult();

        if (bindingResult != null) {
            mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, model.getBindingResult());
        }

        return mv;
    }

    protected ModelAndView defaultServerResult() {

        final TModel model = getModel();

        model.getAppSession().setLangId(UserSession.getCurrent().getLanguageId());

        final ModelAndView mv = new ModelAndView(MainetConstants.SERVER_ERROR_URL, MainetConstants.FORM_NAME, model);

        final BindingResult bindingResult = model.getBindingResult();

        if (bindingResult != null) {
            mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, model.getBindingResult());
        }

        return mv;

    }

    protected final void sessionCleanup(final HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().removeAttribute(getModel().getBeanName());
        }

        UserSession.getCurrent().getOrganisation();
    }

    /**
     * This method get all the look up&acute;s sub information object&acute;s for the given parent code and id.
     * @param typeCode {@link String} literal containing parent code.
     * @param typeId {@link String} literal containing parent id.
     * @return {@link List} of {@link LookUp} objects.
     */
    @RequestMapping(params = "getSubInfo", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody final List<LookUp> getSubLookUpList(@RequestParam final String typeCode,
            @RequestParam final long typeId) {
        final List<LookUp> lookup = getApplicationSession().getChildLookUpsFromParentId(typeId);
        if ((lookup != null) && (lookup.size() > 0)) {
            Collections.sort(lookup);
        }
        return lookup;
    }

    @RequestMapping(params = "getSubInfoByLevel", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody final List<LookUp> getSubLookUpListByLevel(@RequestParam final String typeCode,
            @RequestParam final long typeId,
            @RequestParam final long level) {
        return getApplicationSession().getChildLookUpsFromParentIdForLevel(typeId, level);
    }

    @RequestMapping(params = "FileUpload", method = RequestMethod.POST)
    public @ResponseBody String uploadDocument(@RequestParam("files") final List<MultipartFile> multipartFiles) {
        for (final MultipartFile file : multipartFiles) {
            getModel().getMultipartFiles().add(file);
        }

        return getModel().getUploadedFiles();
    }

    @RequestMapping(params = "Download", method = RequestMethod.POST)
    public ModelAndView download(@RequestParam("downloadLink") final String downloadLink,
	    final HttpServletResponse httpServletResponse) {
	final String outputPath = MainetConstants.DirectoryTree.DEFAULT_CACHE_FOLDER
		+ MainetConstants.FILE_PATH_SEPARATOR + "SHOW_DOCS";
	String filePath = Utility.downloadedFileUrl(downloadLink, outputPath, this.getModel().getFileNetClient());
	if (StringUtils.isBlank(filePath)) {
	    throw new FrameworkException("File Not Found");
	}
	this.getModel().setFilePath(filePath);

	return new ModelAndView(MainetConstants.VIEW_HELP, MainetConstants.CommonConstants.COMMAND, this.getModel());
    }

    @RequestMapping(params = "previewDownload", method = RequestMethod.POST)
    public ModelAndView previewDownload(@RequestParam("downloadLink") final String downloadLink,
	    final HttpServletResponse httpServletResponse) {
	final String cacheRelativePath = MainetConstants.DirectoryTree.DEFAULT_CACHE_FOLDER
		+ MainetConstants.FILE_PATH_SEPARATOR + "SHOW_DOCS";
	String filePath = Utility.downloadedPreviewFileUrl(downloadLink, cacheRelativePath,
		this.getModel().getFileNetClient());
	if (StringUtils.isBlank(filePath)) {
	    throw new FrameworkException("File Not Found");
	}
	this.getModel().setFilePath(filePath);

	return new ModelAndView(MainetConstants.VIEW_HELP, MainetConstants.CommonConstants.COMMAND, this.getModel());
    }
    
    @RequestMapping(params = "DownloadFile", method = RequestMethod.POST)
    public ModelAndView download(@RequestParam("docId") final String docId, @RequestParam("docName") final String docName,
            final HttpServletResponse httpServletResponse) throws Exception {
        String outputPath = MainetConstants.DirectoryTree.DEFAULT_CACHE_FOLDER + MainetConstants.FILE_PATH_SEPARATOR + "SHOW_DOCS"
                + MainetConstants.FILE_PATH_SEPARATOR;
        final byte[] byteArray = iDmsService.getDocumentById(docId);
        if (byteArray == null) {
            return new ModelAndView("redirect:/404error.jsp.html");
        }
        Utility.createDirectory(Filepaths.getfilepath() + outputPath);
        outputPath = outputPath.replace(MainetConstants.FILE_PATH_SEPARATOR, MainetConstants.WINDOWS_SLASH);
        outputPath = outputPath + docName;
        final Path path = Paths.get(Filepaths.getfilepath() + outputPath);
        java.nio.file.Files.write(path, byteArray);
        this.getModel().setFilePath(outputPath);
        return new ModelAndView("viewHelp", MainetConstants.FORM_NAME, this.getModel());
    }

    /**
     * To get message text for given message template code.
     * @param msgTemplate the {@link String} literal containing message template code.
     * @return {@link String} object which containing actual text message.
     */
    public final String getMessageText(final String msgTemplate) {
        final TModel model = this.getModel();
        return model.getAppSession().getMessage(msgTemplate);
    }

    @RequestMapping(params = "locale", method = RequestMethod.GET)
    public String welcome(@RequestParam("lang") final String requestLang, @RequestParam("url") final String url,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        final LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(requestLang));
        UserSession.getCurrent().setLanguageId(getLanguage(requestLang));
        return "redirect:" + url;
    }

    private int getLanguage(final String language) {
        if (MainetConstants.REG.equals(language)) {
            return 2;
        } else if (MainetConstants.DEFAULT_LOCALE_STRING.equals(language)) {
            return 1;
        } else {
            return 1;
        }
    }

    @RequestMapping(params = "reset", method = RequestMethod.POST)
    protected @ResponseBody JsonViewObject resetForm(final HttpServletRequest httpServletRequest) {
        try {
            this.sessionCleanup(httpServletRequest);

            FileUploadUtility.getCurrent().getFileMap().clear();
            FileUploadUtility.getCurrent().getFileUploadSet().clear();
            FileUploadUtility.getCurrent().setFolderCreated(false);

            return JsonViewObject.successResult();
        } catch (final Throwable ex) {
            return JsonViewObject.failureResult(ex);
        }
    }

    @RequestMapping(params = "ResubmissionApplication", method = RequestMethod.POST)
    public ModelAndView ResubmissionApplication(@RequestParam("menuparams") final List<String> menuparams,
            @RequestParam("applId") final long applId,
            @RequestParam("filterType") final String filterType, final HttpServletRequest httpServletRequest) {

        bindModel(httpServletRequest);

        getModel().setApmApplicationId(applId);

        getModel().setFilterType(filterType);

        getModel().viewResubmissionApplication(applId, menuparams);

        getModel().getDataForResubmission(applId, menuparams.get(0));

        return this.getResubmissionView();

    }

    @RequestMapping(params = "getMessage", method = RequestMethod.POST)
    public @ResponseBody String getMessage(@RequestParam("key") final String key, final HttpServletRequest request,
            final HttpServletResponse response) {
        return getApplicationSession().getMessage(key);
    }

    @RequestMapping(method = RequestMethod.GET, params = "ShowPolicy")
    public ModelAndView GlobalPolicy(final HttpServletRequest httpServletRequest) {

        final String outputPath = MainetConstants.DirectoryTree.DEFAULT_CACHE_FOLDER + MainetConstants.FILE_PATH_SEPARATOR
                + MainetConstants.POLICY;
        this.getModel().setFilePath(Utility.downloadedFileUrl(UserSession.getCurrent().getOrganisation().getOrgid() + "\\POLICY"
                + MainetConstants.FILE_PATH_SEPARATOR + "Policy.pdf", outputPath, this.getModel().getFileNetClient()));
        return new ModelAndView(MainetConstants.VIEW_HELP, MainetConstants.CommonConstants.COMMAND, this.getModel());
    }

    /**
     * Common handler method for Scrutiny Process
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(params = "viewApplicationAndDoScrutiny", method = RequestMethod.GET)
    public ModelAndView viewApplicationAndDoScrutiny(final HttpServletRequest request) {
        try {
            bindModel(request);
            final String applicationId = UserSession.getCurrent().getScrutinyCommonParamMap()
                    .get(MainetConstants.SCRUTINY_COMMON_PARAM.APM_APPLICATION_ID);
            final String taskId = UserSession.getCurrent().getScrutinyCommonParamMap()
                    .get(MainetConstants.SCRUTINY_COMMON_PARAM.TASK_ID);
            getModel().setTaskId(Long.parseLong(taskId));
            getModel().populateApplicationData(Long.parseLong(applicationId));
        } catch (final Exception e) {
            logger.error("Problem occurred during fetching application related info from respective service:", e);
            return defaultExceptionFormView();
        }

        return this.getScrutinyView();
    }

    @RequestMapping(params = "getFilePath", method = RequestMethod.POST)
    public @ResponseBody String getFilePath(final HttpServletRequest httpServletRequest) {
        return this.getModel().getFilePath();
    }

    protected ModelAndView defaultExceptionView() {
        return new ModelAndView(MainetConstants.COMMON.DEFAULT_EXE_VIEWNAME, MainetConstants.CommonConstants.COMMAND,
                this.getModel());
    }

    protected ModelAndView defaultExceptionFormView() {
        return new ModelAndView(MainetConstants.DEFAULT_EXCEPTION_FORM_VIEW, MainetConstants.CommonConstants.COMMAND,
                this.getModel());
    }

    @RequestMapping(params = "getToplevelTryList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public LookUp getFirstAndSecondLevelList(@RequestParam(value = "level") final int level) {

        final List<LookUp> list = new ArrayList<>();
        final List<LookUp> lookUpList = CommonMasterUtility.getSecondLevelData(MainetConstants.TRY, level);
        for (final LookUp lookUp : lookUpList) {
            if (lookUp.getDefaultVal().equalsIgnoreCase(MainetConstants.MENU.Y)) {
                list.add(lookUp);
            }
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    @RequestMapping(params = "getTryList", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<LookUp> getDistrictList(@RequestParam(value = "term") final String term,
            @RequestParam(value = "parentId") final String parentId) {

        final List<LookUp> list = new ArrayList<>();
        try {
            final List<LookUp> lookupList = CommonMasterUtility.getChildLookUpsFromParentId(Long.parseLong(parentId));
            for (final LookUp lookUp : lookupList) {
                if (lookUp.getDescLangFirst().matches("(?i)^" + term + ".*$")) {
                    list.add(lookUp);
                }
            }
        } catch (final NumberFormatException e) {

            return null;
        }
        return list;
    }

    @RequestMapping(method = RequestMethod.POST, params = "encrypted")
    public @ResponseBody String getEncrypted(@RequestParam("plainText") final String plainText)
            throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalStateException, BadPaddingException {
        final String encryptedData = this.getModel().encryptData(plainText);
        return encryptedData;
    }

    @RequestMapping(method = RequestMethod.POST, params = "decrypted")
    public @ResponseBody Double getDecrypted(@RequestParam("plainText") final String plainText)
            throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalStateException, BadPaddingException {
        final String decryptedData = this.getModel().decryptData(plainText);
        return Double.parseDouble(decryptedData);
    }

    @RequestMapping(method = RequestMethod.POST, params = "doFileUpload")
    public @ResponseBody JsonViewObject uploadDocument(final HttpServletRequest httpServletRequest,
            final HttpServletResponse response,
            final String fileCode, @RequestParam final String browserType) {
        UserSession.getCurrent().setBrowserType(browserType);
        final MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
        final JsonViewObject jsonViewObject = FileUploadUtility.getCurrent().doFileUpload(request, fileCode, browserType);
        return jsonViewObject;
    }

    @RequestMapping(method = RequestMethod.POST, params = "doFileUploadValidatn")
    public @ResponseBody List<JsonViewObject> doFileUploadValidatn(final HttpServletRequest httpServletRequest,
            @RequestParam final String browserType) {
        UserSession.getCurrent().setBrowserType(browserType);
        final List<JsonViewObject> result = FileUploadUtility.getCurrent().getFileUploadList();
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params = "doFileDeletion")
    public @ResponseBody JsonViewObject doFileDeletion(@RequestParam final String fileId,
            final HttpServletRequest httpServletRequest,
            @RequestParam final String browserType, @RequestParam(name = "uniqueId", required = false) final Long uniqueId) {
        UserSession.getCurrent().setBrowserType(browserType);
        JsonViewObject jsonViewObject = JsonViewObject.successResult();
        jsonViewObject = FileUploadUtility.getCurrent().deleteFile(fileId);
        return jsonViewObject;
    }

    /**
     * This method get all the look up&acute;s sub information object&acute;s for the given parent code,id and isAlphaNumeric.
     * @param typeCode {@link String} literal containing parent code.
     * @param typeId {@link String} literal containing parent id.
     * @param isAlphaNumeric {@link String} literal containing alphanumericFlag.
     * @return {@link List} of {@link LookUp} objects.
     */
    @RequestMapping(params = "getSubAlphanumericSortInfo", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody final List<LookUp> getSubAlphanumericSortInfo(@RequestParam final String typeCode,
            @RequestParam final long typeId,
            @RequestParam final String isAlphaNumeric) {
        final List<LookUp> lookup = getApplicationSession().getChildLookUpsFromParentId(typeId);
        if ((lookup != null) && !lookup.isEmpty()) {
            if ((isAlphaNumeric != null) && isAlphaNumeric.equals(MainetConstants.MENU.Y)) {
                Collections.sort(lookup, LookUp.alphanumericComparator);
            } else {
                Collections.sort(lookup);
            }

        }
        return lookup;
    }

    @RequestMapping(params = "showErrorPage", method = RequestMethod.POST)
    public ModelAndView showErrorPage(final HttpServletRequest httpServletRequest, final Exception exception) {

        logger.error("Exception found : ", exception);

        ModelAndView modelAndView;
        modelAndView = new ModelAndView(
                MainetConstants.DEFAULT_EXCEPTION_FORM_VIEW,
                MainetConstants.FORM_NAME, getModel());

        return modelAndView;
    }

    @RequestMapping(params = "generatWorkOrderAbstract", method = RequestMethod.GET)
    public String generatWorkOrder(final HttpServletRequest request) {
        try {
            bindModel(request);
            final String applicationId = UserSession.getCurrent().getScrutinyCommonParamMap()
                    .get(MainetConstants.SCRUTINY_COMMON_PARAM.APM_APPLICATION_ID);
            final String serviceId = UserSession.getCurrent().getScrutinyCommonParamMap()
                    .get(MainetConstants.SCRUTINY_COMMON_PARAM.SM_SERVICE_ID);
            final String taskId = UserSession.getCurrent().getScrutinyCommonParamMap()
                    .get(MainetConstants.SCRUTINY_COMMON_PARAM.TASK_ID);

            request.getSession().setAttribute(MainetConstants.REQUIRED_PG_PARAM.APPLICATION_NO, applicationId);
            request.getSession().setAttribute(MainetConstants.REQUIRED_PG_PARAM.SERVICE_ID, serviceId);
            request.getSession().setAttribute(MainetConstants.SCRUTINY_COMMON_PARAM.TASK_ID, taskId);
           

        } catch (final Exception e) {
            logger.error("Problem occurred generatWorkOrder service:", e);
            e.printStackTrace();
        }
        return new String(MainetConstants.REDIRECT_WORK_ORDER_HTML);
    }

    @RequestMapping(method = RequestMethod.GET, params = "ShowHelpDoc")
    public ModelAndView globalHelpDocs(final HttpServletRequest httpServletRequest) {

        final String outputPath = MainetConstants.DirectoryTree.DEFAULT_CACHE_FOLDER + MainetConstants.FILE_PATH_SEPARATOR
                + MainetConstants.HelpDoc.HELPDOC;
        final CommonHelpDocs docs = this.getModel().getCommonHelpDoc();
        if ((docs != null) && (docs.getModuleName() != null) && !docs.getModuleName().equalsIgnoreCase(MainetConstants.BLANK)) {
            try {
                if (UserSession.getCurrent().getLanguageId() == 1) {
                    this.getModel()
                            .setFilePath(Utility.downloadedFileUrl(
                                    docs.getFilePath() + MainetConstants.FILE_PATH_SEPARATOR + docs.getFileNameEng(), outputPath,
                                    this.getModel().getFileNetClient()));
                } else if ((UserSession.getCurrent().getLanguageId() == 2) && (docs.getFileNameReg() != null)
                        && !docs.getFileNameReg().equalsIgnoreCase(MainetConstants.BLANK)) {
                    this.getModel()
                            .setFilePath(Utility.downloadedFileUrl(
                                    docs.getFilePathReg() + MainetConstants.FILE_PATH_SEPARATOR + docs.getFileNameReg(),
                                    outputPath, this.getModel().getFileNetClient()));
                } else {
                    this.getModel()
                            .setFilePath(Utility.downloadedFileUrl(
                                    docs.getFilePath() + MainetConstants.FILE_PATH_SEPARATOR + docs.getFileNameEng(), outputPath,
                                    this.getModel().getFileNetClient()));
                }
            } catch (final Exception f) {
                logger.error(MainetConstants.ERROR_OCCURED, f);
                return new ModelAndView("redirect:/404error.jsp.html");
            }
            return new ModelAndView("viewHelp", MainetConstants.FORM_NAME, this.getModel());
        } else {
            /*
             * if (this.getModel().getBeanName().equals(MainetConstants.ValidationMessageCode.FOR_CITI_REGISTRATION)) {
             * return new ModelAndView("redirect:/notFoundFile.html");
             * } else {
             */
            return new ModelAndView("viewHelpNotFound", MainetConstants.FORM_NAME, this.getModel());

            /* } */

        }

    }
    
    /**
     * Setting the user id and Pass in to DMSPath Var which we are using in filedownload.tag
     *
     *
     * @author  Shyam Ghodasra
     * @param  	null
     * @return	null
     * 
     */
    
     public void setDMSPath() {

    	 Employee emp = iEmployeeService.getEmployeeByLoginName(UserSession.getCurrent().getEmployee().getEmploginname(),UserSession.getCurrent().getOrganisation(),"0");
         String userName = emp.getEmploginname() + MainetConstants.operator.UNDER_SCORE + UserSession.getCurrent().getOrganisation().getOrgid() ;
         String pass =emp.getEmppassword() ;

    	 String DMS_BASE_URL = ServiceEndpoints.DMS_BASE_URL;
         DMS_BASE_URL = DMS_BASE_URL  + MainetConstants.Dms.DMS_DOCUMENT_DETAILS_BASE_URL +MainetConstants.Dms.DMS_NODEREF_PARAM + MainetConstants.Dms.DMS_DOC_ID + 
        		 MainetConstants.Dms.DMS_NODE_PARAM + MainetConstants.Dms.DMS_DOC_ID +  MainetConstants.Dms.DMS_USER_PARAM  + userName + MainetConstants.Dms.DMS_PASS_PARAM + pass;
    	 /*
    	  * At last URL will become something like this  
    	  * http://182.18.168.246:8080/share/page/document-details?nodeRef=workspace://SpacesStore/ID&node=workspace://SpacesStore/ID&user=testuser&pass=admin
    	  */

         this.getModel().setDmsPath(DMS_BASE_URL);
     }

	@RequestMapping(params = "viewRefNoDetails", method = RequestMethod.POST)
	public ModelAndView viewDetails(@RequestParam("appNo") final String applicationId,
			@RequestParam("taskId") final long serviceId, @RequestParam("actualTaskId") final long taskId,
			final HttpServletRequest httpServletRequest) throws Exception {
		return new ModelAndView("CommonViewDetails", MainetConstants.FORM_NAME, this.getModel());
	}
	
}