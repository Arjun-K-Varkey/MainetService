package com.abm.mainet.common.master.ui.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abm.mainet.cfc.challan.dto.ChallanReceiptPrintDTO;
import com.abm.mainet.cfc.challan.dto.ChallanReportDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.dto.CFCSchedulingCounterDet;
import com.abm.mainet.common.integration.acccount.dto.TbServiceReceiptMasBean;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.integration.payment.dto.PaymentTransactionMasDTO;
import com.abm.mainet.common.integration.payment.service.PaymentService;
import com.abm.mainet.common.integration.property.dto.PropertyInputDto;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbCfcApplicationMstService;
import com.abm.mainet.common.master.service.TbDepartmentService;
import com.abm.mainet.common.master.ui.model.ReceiptFormModel;
import com.abm.mainet.common.service.IDuplicateReceiptService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.IReceiptEntryService;
import com.abm.mainet.common.service.ReceiptDetailService;
import com.abm.mainet.common.ui.controller.AbstractFormController;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;



@Controller
@RequestMapping("/ReceiptForm.html")
public class ReceiptFormController extends AbstractFormController<ReceiptFormModel>{

	private static final Logger LOGGER = Logger.getLogger(ReceiptFormController.class);
    @Autowired
	TbDepartmentService departmentService;
    
    @Autowired
    IReceiptEntryService recService;
    
    
    @Autowired
    private TbCfcApplicationMstService tbCfcservice;
    
    @Autowired
    private  IEmployeeService employeeServcie;
    
    @Autowired
    private ReceiptDetailService receiptDetailService;
    
    @Autowired
    private DepartmentService departmentservice;
    
    @Autowired
	private IDuplicateReceiptService duplicateReceiptService;
    @Autowired
    private PaymentService paymentService;
   
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index(HttpServletRequest request){
		sessionCleanup(request);
		this.getModel().setDepartmentList(departmentService.findAll());
		this.getModel().setLangId(UserSession.getCurrent().getLanguageId());
		return index();
	}
	
	@ResponseBody
	@RequestMapping(params = "searchForm", method = RequestMethod.POST)
	public ModelAndView searchData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		    this.getModel().bind(httpServletRequest);
		    ReceiptFormModel model = this.getModel();		
		    Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
		    this.getModel().getReceiptMasBean().setOrgId(orgId);
		    this.getModel().getReceiptMasBean().setLangId(UserSession.getCurrent().getLanguageId());
		    List<TbServiceReceiptMasBean> tbServiceReceiptMasBean = new ArrayList<>();
		     String rmdate = Utility.dateToString(this.getModel().getReceiptMasBean().getRmDate(), MainetConstants.DATE_FORMATS);
		    if (model.validateInputs()) {
		     this.getModel().getReceiptMasBean().setRmDatetemp(rmdate);
		     tbServiceReceiptMasBean = receiptDetailService.findReceiptDet(this.getModel().getReceiptMasBean());
		     if (CollectionUtils.isNotEmpty(tbServiceReceiptMasBean)) {
		     this.getModel().setReceiptMasBeanList(tbServiceReceiptMasBean);
		     }else {
					model.addValidationError(ApplicationSession.getInstance().getMessage("receipt.noRecords"));
		     }
		    }
		ModelAndView mv = null;
		mv = new ModelAndView("ReceiptFormValidn", MainetConstants.FORM_NAME, this.getModel());
		mv.addObject(BindingResult.MODEL_KEY_PREFIX + MainetConstants.FORM_NAME, getModel().getBindingResult());
		return mv;
	}
	
	

	
	@ResponseBody
	@RequestMapping(params = "viewReceipt", method = RequestMethod.POST)
	public ModelAndView viewReceiptDetails(final HttpServletRequest request, @RequestParam("rmRcptid") final Long rmRcptid) {
		Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
		int langId = UserSession.getCurrent().getLanguageId();
		Map<Long, ChallanReportDTO> taxdto = new HashMap<>();
		TbServiceReceiptMasBean  serviceReceiptMasBean = receiptDetailService.findReceiptById(rmRcptid, orgId,langId);	
		if(serviceReceiptMasBean != null)
		  this.getModel().setReceiptMasBean(serviceReceiptMasBean);
		return new ModelAndView("ViewReceiptForm", MainetConstants.FORM_NAME, this.getModel());
	}
	
	@ResponseBody
	@RequestMapping(params = "printReceipt")
	public  ModelAndView printReceiptData(final HttpServletRequest request, @RequestParam("rmRcptid") final Long rmRcptid) {
		bindModel(request);
		Long orgId = UserSession.getCurrent().getOrganisation().getOrgid();
		int langId = UserSession.getCurrent().getLanguageId();
		ChallanReceiptPrintDTO printDTO = new ChallanReceiptPrintDTO();
		try {
			printDTO = receiptDetailService.setValuesAndPrintReport(rmRcptid, orgId, langId);
			LookUp lookUp = CommonMasterUtility.getValueFromPrefixLookUp("CST", MainetConstants.ENV,
					UserSession.getCurrent().getOrganisation());
			
			CFCSchedulingCounterDet counterDet = new CFCSchedulingCounterDet();
			String departmentShortCode = departmentservice.getDeptCode(printDTO.getDeptId());
			printDTO.setDeptShortCode(departmentShortCode);
			if (lookUp != null && lookUp.getOtherField().equals(MainetConstants.FlagY)) {
				counterDet = tbCfcservice.getCounterDetByEmpId(printDTO.getRecptCreatedBy(),
						UserSession.getCurrent().getOrganisation().getOrgid());
				if (printDTO.getRecptCreatedBy() != null) {
					String Name = "";	
					Name=employeeServcie.getAllEmpLoyeeNamebyRecipetId(rmRcptid);
				Employee empDto = employeeServcie.findEmployeeById(printDTO.getRecptCreatedBy());
				if (empDto != null &&StringUtils.isBlank(Name)) {
					StringBuilder empName = new StringBuilder();
					if (StringUtils.isNotBlank(empDto.getEmpname()))
						empName.append(empDto.getEmpname() + " ");
					if (StringUtils.isNotBlank(empDto.getEmpmname()))
						empName.append(empDto.getEmpmname() + " ");
					if (StringUtils.isNotBlank(empDto.getEmplname()))
						empName.append(empDto.getEmplname());
					if (StringUtils.isNotBlank(empName.toString()))
						Name = empName.toString();
				}
				if (null != Name)
					printDTO.setUserName(Name);
			}
				//D#147490
				final SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
				String cfcDate = sd.format(printDTO.getRecptCreatedDate());
				printDTO.setCfcDate(cfcDate);
				/*
				 * final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm"); final
				 * String time = localDateFormat.format(printDTO.getRecptCreatedDate());
				 * printDTO.setReceiptTime(time);
				 */
               //D#147476
				if (StringUtils.isNotEmpty(printDTO.getNarration()) && null != printDTO.getServiceCodeflag()  && printDTO.getServiceCodeflag().equals(MainetConstants.FlagY)) {
					printDTO.setSubject(printDTO.getNarration());
				}
			}
			//to get counter details with new format
			 String receiptNo = null;
				if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
					receiptNo = printDTO.getReceiptNo();
				if (printDTO != null && StringUtils.isNotEmpty(printDTO.getCfcColCenterNo())) {
					if (null != counterDet)
						counterDet.setCollcntrno(printDTO.getCfcColCenterNo());
					else
						this.getModel().getCfcSchedulingCounterDet().setCollcntrno(printDTO.getCfcColCenterNo());
				}
				if (printDTO != null && StringUtils.isNotEmpty(printDTO.getCfcColCounterNo())) {
					if (null != counterDet)
						counterDet.setCounterno(printDTO.getCfcColCounterNo());
					else
						this.getModel().getCfcSchedulingCounterDet().setCounterno(printDTO.getCfcColCounterNo());
				}
			}
				if (printDTO.getTotalReceivedAmount() == 0) {
					printDTO.setTotalReceivedAmount(printDTO.getAmount());
				}
			
			 // #148057 and #147528 for water and property payment receipt 
			if (printDTO.getServiceCode() != null && (printDTO.getServiceCode().equals("PBP") || printDTO.getServiceCode().equals("BPW") || printDTO.getServiceCode().equals("DES"))) {
				printDTO = duplicateReceiptService.getRevenueReceiptDetails(rmRcptid, Utility.getReceiptIdFromCustomRcptNO(printDTO.getReceiptNo()),printDTO.getReferenceNo(),UserSession.getCurrent().getOrganisation().getOrgid(),
						UserSession.getCurrent().getLanguageId(), null, null);
				Boolean displaySeq = true;
				if (printDTO != null) {
					for (ChallanReportDTO dto : printDTO.getPaymentList()) {
						if (dto.getDisplaySeq() == null) {
							displaySeq = false;
							break;
						}
					}
					if (displaySeq)
						printDTO.getPaymentList().sort(Comparator.comparing(ChallanReportDTO::getDisplaySeq, Comparator.nullsLast(Comparator.naturalOrder())));
				}
				if (printDTO != null && printDTO.getCfcColCounterNo() != null)
					printDTO.setCfcCounterNo(printDTO.getCfcColCounterNo());
				if (printDTO != null && printDTO.getCfcColCenterNo() != null)
					printDTO.setCfcCenter(printDTO.getCfcColCenterNo());
				if (printDTO.getRecptCreatedBy() != null) {
					String Name = "";	
					Name=employeeServcie.getAllEmpLoyeeNamebyRecipetId(rmRcptid);
				Employee empDto = employeeServcie.findEmployeeById(printDTO.getRecptCreatedBy());
				if (empDto != null &&StringUtils.isBlank(Name)) {
					StringBuilder empName = new StringBuilder();
					if (StringUtils.isNotBlank(empDto.getEmpname()))
						empName.append(empDto.getEmpname() + " ");
					if (StringUtils.isNotBlank(empDto.getEmpmname()))
						empName.append(empDto.getEmpmname() + " ");
					if (StringUtils.isNotBlank(empDto.getEmplname()))
						empName.append(empDto.getEmplname());
					if (StringUtils.isNotBlank(empName.toString()))
						Name = empName.toString();
				}
				if (null != Name)
					printDTO.setUserName(Name);
			}
				
				if(MapUtils.isEmpty(printDTO.getBillDetails()) || MapUtils.isEmpty(printDTO.getBillYearDetails())) {
					Map<String, String> billDetails = new HashMap<String, String>();
					Map<String, String> billYearDetails = new HashMap<String, String>();
					if(CollectionUtils.isNotEmpty(printDTO.getPaymentList())) {
						for (ChallanReportDTO pay : printDTO.getPaymentList()) {
							if (CollectionUtils.isNotEmpty(pay.getBmIdNoList())) {
								for (Long billNo : pay.getBmIdNoList()) {

									TbBillMas billMas = recService.getBillMasByIdNo(billNo,
											UserSession.getCurrent().getOrganisation().getOrgid(),
											printDTO.getDeptId());
									if (billMas != null) {
										String billDate = billDetails.get(billMas.getBmIdno());
										if (StringUtils.isBlank(billDate)) {
											billDetails.put(String.valueOf(billMas.getBmIdno()),
													new SimpleDateFormat(MainetConstants.DATE_FORMAT)
															.format(billMas.getBmBilldt()));
											StringBuilder billPeriod = new StringBuilder();

											String fromDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
													.format(billMas.getBmFromdt());
											String toDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
													.format(billMas.getBmTodt());
											billPeriod.append(fromDate);
											billPeriod.append(" ");
											billPeriod.append("to");
											billPeriod.append(" ");
											billPeriod.append(toDate);
											billYearDetails.put(String.valueOf(billMas.getBmIdno()),
													billPeriod.toString());
										}
									}
								}
							}
						}
				}
					printDTO.setBillDetails(billDetails);
					printDTO.setBillYearDetails(billYearDetails);
				}
				printDTO.getPaymentList().sort(Comparator.comparing(ChallanReportDTO::getDisplaySeq, Comparator.nullsLast(Comparator.naturalOrder())));
				
                //to get counter details with new format
				if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(),MainetConstants.ENV_SKDCL)) {
					if (receiptNo != null && printDTO.getReceiptNo() != null)
						printDTO.setReceiptNo(receiptNo);
						printDTO.setReceiptDate(printDTO.getRcptDate());
						//printDTO.setReceiptTime(printDTO.getRcptTime());
				}
			}
			if(StringUtils.equalsIgnoreCase(printDTO.getServiceCode(), MainetConstants.TradeLicense.RENEWAL_SERVICE_SHORT_CODE)&&printDTO.getReferenceNo()!=null) {
				ChallanReceiptPrintDTO chDto=duplicateReceiptService.getReceiptDetails(rmRcptid,  Utility.getReceiptIdFromCustomRcptNO(printDTO.getReceiptNo()),Long.valueOf(printDTO.getReferenceNo()));
				if(chDto!=null)
					printDTO.setSubject(chDto.getSubject());
			}
            if (null != counterDet)
			this.getModel().setCfcSchedulingCounterDet(counterDet);
			this.getModel().setReceiptDTO(printDTO);
		} catch (Exception e) {
			LOGGER.info("Exception occure while Fetching receipt details:" + e);
		}

		if(StringUtils.equalsIgnoreCase(printDTO.getPaymentMode(), MainetConstants.web_mode)) {
			String refNo=printDTO.getReferenceNo();
			if(StringUtils.isBlank(printDTO.getReferenceNo())&&StringUtils.isNotBlank(printDTO.getPropNo_connNo_estateN_V())) {
				refNo=printDTO.getPropNo_connNo_estateN_V().split("/")[0];
			}if(refNo!=null) {
			PaymentTransactionMasDTO dto=paymentService.getPaymentStatusByReferenceId(refNo.trim());
			if(dto!=null)
			printDTO.setPaymentText(dto.getPgSource());
		}else
			printDTO.setPaymentText(MainetConstants.BLANK);
			}

		Boolean flag = checkWhetherPropertyIsIllegal(printDTO);
		if (printDTO.getServiceCode() != null && (printDTO.getServiceCode().equals("PBP") || printDTO.getServiceCode().equals("BPW") || printDTO.getServiceCode().equals("DES"))) {
			if(flag) {
				this.getModel().setIllegal(MainetConstants.Y_FLAG);
			}
			return new ModelAndView("revenueReceiptPrint", MainetConstants.FORM_NAME, this.getModel());
		} else {
		return new ModelAndView(MainetConstants.CHALLAN_AT_ULB_RECEIPT_PRINT, MainetConstants.FORM_NAME,
				this.getModel());
		}
	}
	
	private Boolean checkWhetherPropertyIsIllegal(ChallanReceiptPrintDTO printDTO) {
        Boolean status = false;
        if(StringUtils.equals(MainetConstants.Property.PROP_DEPT_SHORT_CODE, printDTO.getDeptShortCode()) && StringUtils.isNotBlank(printDTO.getPropNo_connNo_estateN_V())) {
        	
        	PropertyInputDto inputDto = new PropertyInputDto();
        	String propertyNo=printDTO.getPropNo_connNo_estateN_V();
        	String flatNo="";  
            if(printDTO.getPropNo_connNo_estateN_V().contains(MainetConstants.WINDOWS_SLASH_WITH_SPACE)) {
            	String[] splitStr = printDTO.getPropNo_connNo_estateN_V().split(MainetConstants.WINDOWS_SLASH_WITH_SPACE);
            	if(splitStr != null && splitStr.length > 0)
            		propertyNo=splitStr[0].trim();
            	if(splitStr != null && splitStr.length > 1)
            		flatNo=splitStr[1].trim(); 
            }
        	inputDto.setPropertyNo(propertyNo);
        	inputDto.setFlatNo(flatNo);
        	inputDto.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
			
			final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(inputDto,
					ServiceEndpoints.CHECK_WHETHER_PROPERTY_ILLEGAL);
			if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
				status = (Boolean) responseEntity.getBody();
			}
			 
        }
        return status;
    }
}
