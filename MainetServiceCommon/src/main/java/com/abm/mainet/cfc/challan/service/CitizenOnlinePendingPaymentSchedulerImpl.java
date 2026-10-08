package com.abm.mainet.cfc.challan.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.bill.service.AdjustmentEntryService;
import com.abm.mainet.cfc.challan.dto.ChallanReceiptPrintDTO;
import com.abm.mainet.cfc.challan.dto.VizpayRequestDto;
import com.abm.mainet.cfc.challan.ui.model.CitizenOnlinePendingPayment;
import com.abm.mainet.cfc.loi.domain.TbLoiMasEntity;
import com.abm.mainet.cfc.loi.repository.PaymentTransactionMasRepository;
import com.abm.mainet.cfc.loi.repository.TbLoiMasJpaRepository;
import com.abm.mainet.cfc.techprocess.dto.TechProcessRequest;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.dto.WardZoneBlockDTO;
import com.abm.mainet.common.integration.payment.dao.PaymentDAO;
import com.abm.mainet.common.integration.payment.dto.getpaydto.PaymentApi;
import com.abm.mainet.common.integration.payment.dto.getpaydto.RequestVo;
import com.abm.mainet.common.integration.payment.entity.PGBankParameter;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.integration.payment.service.ISGPay;
import com.abm.mainet.common.integration.payment.service.TechProcessPayment;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;
import com.abm.mainet.common.service.CitizenDashBoardService;
import com.abm.mainet.common.service.CommonService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CallBackResponseObj;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.service.IWorkflowTaskService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CitizenOnlinePendingPaymentSchedulerImpl implements CitizenOnlinePendingPaymentScheduler {
	private static final Logger log = LoggerFactory.getLogger(CitizenOnlinePendingPaymentSchedulerImpl.class);

	@Autowired
	private CitizenDashBoardService citizenDashBoardService;

	@Autowired
	private IChallanService iChallanService;

	@Autowired
	private ServiceMasterService serviceMasterService;

	@Autowired
	private EmployeeJpaRepository employeeJpaRepository;

	@Resource
	private TbLoiMasJpaRepository tbLoiMasJpaRepository;

	@Autowired
	private CommonService commonService;
	@Resource
	private PaymentDAO paymentDAO;
	@Autowired
	private PaymentTransactionMasRepository paymentTransactionMasRepository;
	@Autowired
	private AdjustmentEntryService adjustmentEntryService;
	
	@Autowired
	private IWorkflowTaskService iWorkflowTaskService;
    @Autowired
    private IOrganisationService organisationService;
    
    @Autowired
    IPushToPayService iPushToPayService;

	@Override
	@Transactional 
	public void runOnlinePendingPaymentDate(CitizenOnlinePendingPayment model) {
		//Employee emp = UserSession.getCurrent().getEmployee();
		String merchantCode = null;
		List<Integer> succsesCount = new ArrayList<>();
		//int failCount = 0;
		//Map<String, String> responseMap = new HashMap<String, String>();
		Map<Long, String> map = commonService.getAllPgBankWithoutStatus(model.getOrgId());
		for (Map.Entry<Long, String> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(MainetConstants.PG_SHORTNAME.TECH_PROCESS)) {
				Optional<PGBankParameter> opt = commonService
						.getMerchantMasterParamByPgId(entry.getKey(), model.getOrgId()).stream()
						.filter(r -> r.getParName() != null
								&& r.getParName().equalsIgnoreCase(MainetConstants.BankParam.PRODUCTION))
						.findAny();
				if (opt.isPresent()) {
					merchantCode = opt.get().getParValue();
				}
			}
		}
		final String techPmerchantCode=merchantCode;
		String date = Utility.dateToString(model.getChallanDTO().getValidDate(), MainetConstants.DATE_FORMATS);
		List<PaymentTransactionMas> dto = citizenDashBoardService.getCitizenPayPendingDataByDateAndStatus(
				model.getChallanDTO().getValidDate(),model.getChallanDTO().getTodate(), model.getServiceId(), model.getOfflineDTO().getFlatNo(),
				model.getOrgId(), model.getOfflineDTO().getReferenceNo(),model.getOfflineDTO().getDeptId());
		
		log.error("Pending Transaction total size is : " + dto.size());
		/*
		 * final ExecutorService executorService = Executors.newFixedThreadPool(
		 * Integer.valueOf(ApplicationSession.getInstance().getMessage(
		 * "bill.thread.pool.size")));
		 */

		if (CollectionUtils.isNotEmpty(dto)) {
			log.error("Pending Transaction  is started total size is : " + dto.size());
			
			String URL = ApplicationSession.getInstance().getMessage("ONLINE_SYNC_URL");//from serviceConfiguration.properties file
			String isgUrl = ApplicationSession.getInstance().getMessage("ONLINE_SYNC_URL_ISGPAY");
			String GETEPAY_URL = ApplicationSession.getInstance().getMessage("ONLINE_SYNC_URL_GETEPAY");
			String API_KEY_GETEPAY = ApplicationSession.getInstance().getMessage("API_KEY_GETEPAY");
			String MID_GETEPAY = ApplicationSession.getInstance().getMessage("MID_GETEPAY");

			for (PaymentTransactionMas rDTO : dto) {
				/*executorService.execute(new Runnable() {
					public void run() {*/
						CallBackResponseObj respObj=null;
				log.error("Pending Transaction  is started for ref no : " + rDTO.getReferenceId() + "  and Service Id "+ rDTO.getSmServiceId() + "Pg-Source" + rDTO.getPgSource());
				if(MainetConstants.PG_SHORTNAME.TECH_PROCESS.equalsIgnoreCase(rDTO.getPgSource())) {
				TechProcessRequest payment = new TechProcessRequest();
				payment.getMerchant().setIdentifier(techPmerchantCode);
				payment.getTransaction().setDateTime(date);
				payment.getTransaction().setIdentifier(MainetConstants.PG_REQUEST_PROPERTY.TXN + rDTO.getTranCmId());
				payment.getTransaction().setRequestType("O");
				TechProcessPayment techPayment=new TechProcessPayment();
				respObj=techPayment.getPaymentStatus(payment, URL);
				}
				else if(MainetConstants.PG_SHORTNAME.ISGPAY.equalsIgnoreCase(rDTO.getPgSource())) {
					ISGPay isgPay=new ISGPay();
					String bankId=ApplicationSession.getInstance().getMessage("isg.BankId");
					String merchantId=ApplicationSession.getInstance().getMessage("isg.MerchantId");
					String terminalId=ApplicationSession.getInstance().getMessage("isg.TerminalId");
					String passCode=ApplicationSession.getInstance().getMessage("isg.PassCode");
					String txnType=ApplicationSession.getInstance().getMessage("isg.TxnType");
					String mcc=ApplicationSession.getInstance().getMessage("isg.MCC");
					respObj=isgPay.getISGPaymentStatus(isgUrl, rDTO.getTranCmId(),bankId,merchantId,terminalId,passCode,txnType,mcc);
					}
				else if(MainetConstants.PG_SHORTNAME.Pine_Lab.equalsIgnoreCase(rDTO.getPgSource())) {
					
					respObj=iPushToPayService.getPineLabPaymentStatus(rDTO);
					}
				else if(MainetConstants.PG_SHORTNAME.GETEPAY.equalsIgnoreCase(rDTO.getPgSource())) {
					if(StringUtils.isEmpty(rDTO.getLgIpMac())) {
						rDTO.setLgIpMac(" ");
					}
					Map<String, String> mapGetepay = new HashMap<String, String>();
					HttpHeaders header = new HttpHeaders();    
			        header.add("Content-Type", "application/json");
			        header.add("apiKey", API_KEY_GETEPAY);
			        mapGetepay.put("mid", MID_GETEPAY);
			        mapGetepay.put("transaction_id", String.valueOf(rDTO.getTranCmId()));
			        
			        String etpRequstKey = null;
			        
					final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClientWithHeaders(mapGetepay, GETEPAY_URL,
							HttpMethod.POST, Object.class, header);
					log.error("GETEPAY Response : "+ responseEntity);
					if(responseEntity != null) {
						
						try {
							
							etpRequstKey = new ObjectMapper().writeValueAsString(responseEntity.getBody());
							
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Map<String, Object> etpJsonMap = new HashMap<String, Object>();
						Map<String, Object> etpJsonMap1 = new HashMap<String, Object>();
						if(etpRequstKey != null) {
							
							try {
								etpJsonMap = new ObjectMapper().readValue(etpRequstKey,
										new TypeReference<Map<String, Object>>() {	});
								if(etpJsonMap != null) {
									
									etpJsonMap1.putAll((Map<? extends String, ? extends Object>) etpJsonMap.get("data"));
									if (etpJsonMap1 != null && MainetConstants.BILLCLOUD_PARAM.SUCCESS_CODE.equals(etpJsonMap1.get("transaction_status"))) {
										respObj=new CallBackResponseObj();
										respObj.setStatus(MainetConstants.PAYMENT_STATUS.SUCCESS);
									}
								}
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					
				}
				else if(MainetConstants.PG_SHORTNAME.GETEPAY_IDBI.equalsIgnoreCase(rDTO.getPgSource())) {
					
					log.error("Start IDBI GETPAY METHOD.... " );
					RequestVo reqVo=new RequestVo();
					String bankId=ApplicationSession.getInstance().getMessage("idgetpay.BankId");
					String merchantId=ApplicationSession.getInstance().getMessage("idgetpay.MerchantId");
					String terminalId=ApplicationSession.getInstance().getMessage("idgetpay.TerminalId");
					String baseUrl=ApplicationSession.getInstance().getMessage("idgetpay.baseUrl");
					reqVo.setIv(rDTO.getSendSalt());
					reqVo.setKey(rDTO.getSendKey());
					reqVo.setTerminalId(terminalId);
					reqVo.setBaseUrl(baseUrl);
					reqVo.setMid(merchantId);
					reqVo.setMerchantTransactionId(rDTO.getRecvBankRefNum());
					reqVo.setBaseUrl(baseUrl);
					PaymentApi papi=new PaymentApi();
					try {
						respObj=papi.callStatusAPI(reqVo);
					} catch (Exception e) {
						log.error("Exception/Error Occured From IDBI RestCAll...... " + e );
						
					}
					
					
				} else if(MainetConstants.PG_SHORTNAME.ICICI_VIZPAY.equalsIgnoreCase(rDTO.getPgSource())) {
					log.error("Start ICICI_VIZPAY GETPAY METHOD.... " );
					respObj=iPushToPayService.getICICIVIZPAYPaymentStatus(rDTO);
				}
				if (respObj != null && (((StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.TECH_PROCESS,rDTO.getPgSource())||StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.Pine_Lab,	rDTO.getPgSource()))
						&& StringUtils.equalsIgnoreCase(MainetConstants.PAYMENT_STATUS.SUCCESS, respObj.getStatus()))
						|| (StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.ISGPAY, rDTO.getPgSource())
						&& StringUtils.equalsIgnoreCase(MainetConstants.ZERO_ZERO, respObj.getStatus()))
						|| (StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.GETEPAY, rDTO.getPgSource())
						&& StringUtils.equalsIgnoreCase(MainetConstants.PAYMENT_STATUS.SUCCESS, respObj.getStatus()))
						||(StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.GETEPAY_IDBI, rDTO.getPgSource())
						&& StringUtils.equalsIgnoreCase(MainetConstants.PAYMENT_STATUS.SUCCESS, respObj.getStatus()))
						||(StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.ICICI_VIZPAY,rDTO.getPgSource())
						&& StringUtils.equalsIgnoreCase(MainetConstants.PAYMENT_STATUS.SUCCESS, respObj.getStatus())))) {
								CommonChallanDTO offline = new CommonChallanDTO();
								offline.setPosTxnId(respObj.getPosTxnId());
								offline.setPosPayMode(respObj.getPosPayMode());
								setOfflineDto(offline, rDTO);
								try {
									ChallanReceiptPrintDTO challandDto = iChallanService
											.updateDataAfterPayment(offline);
									if (challandDto != null) {
										
										rDTO.setRecvStatus(MainetConstants.Req_Status.SUCCESS);
										rDTO.setUpdatedDate(new Date());
										if(respObj.getOrder_no()!=null&&StringUtils.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.TECH_PROCESS,rDTO.getPgSource())) {
											rDTO.setRecvMihpayid(respObj.getOrder_no());
										}
										paymentTransactionMasRepository.save(rDTO);
										saveMutationDataAfterLoiPayment(offline);
										log.error("Pending Transaction done for  : " + rDTO.getReferenceId());
										succsesCount.add(new Integer(1));
									}
								} catch (Exception e) {
									log.error("Error occur at the time of updating Reciept data and generate workflow");
								}
							} else {
					if (respObj != null) {

						if ((MainetConstants.PG_SHORTNAME.ISGPAY.equalsIgnoreCase(rDTO.getPgSource())
								&& !StringUtils.equalsIgnoreCase(MainetConstants.ZERO_ZERO, respObj.getStatus()))
								||((MainetConstants.PG_SHORTNAME.GETEPAY_IDBI.equalsIgnoreCase(rDTO.getPgSource())
								||MainetConstants.PG_SHORTNAME.Pine_Lab.equalsIgnoreCase(rDTO.getPgSource())
								||MainetConstants.PG_SHORTNAME.ICICI_VIZPAY.equalsIgnoreCase(rDTO.getPgSource()))
								&& !StringUtils.equalsIgnoreCase(MainetConstants.PAYMENT_STATUS.SUCCESS, respObj.getStatus())))
							rDTO.setRecvStatus(MainetConstants.COMMON_STATUS.FAILURE);
						else
							rDTO.setRecvStatus(respObj.getStatusMsg());
						
						rDTO.setRecvErrm(respObj.getStatusMsg());
					}
								rDTO.setUpdatedDate(new Date());
								if(StringUtils.isBlank(rDTO.getPgSource())&&StringUtils.equalsIgnoreCase(rDTO.getRecvStatus(), MainetConstants.PAYMENT_STATUS.PENDING)) {
									rDTO.setRecvStatus(MainetConstants.Not_Found);
								}
								paymentTransactionMasRepository.save(rDTO);
							
						}
				/* }}); */
			}
			/*
			 * executorService.shutdown(); while (!executorService.isTerminated()) {
			 * 
			 * }
			 */
			model.setSuccessMessage("Succsessfully upadate  citizen pending payment count " + succsesCount.size());

		} else {
			model.addValidationError("No record Found for selected criteria");
		}
	}

	@SuppressWarnings("deprecation")
	private void setOfflineDto(CommonChallanDTO offline, PaymentTransactionMas rDTO) {
		offline.setServiceId(rDTO.getSmServiceId());
		offline.setUniquePrimaryId(rDTO.getReferenceId());
		offline.setOrgId(rDTO.getOrgId());
		if (rDTO.getSmServiceId() != null) {
			ServiceMaster sm = serviceMasterService.getServiceMaster(rDTO.getSmServiceId(), rDTO.getOrgId());
			if(sm!=null && (StringUtils.equalsIgnoreCase(sm.getSmShortdesc(), MainetConstants.Property.PROPERTY_BILL_PAYMENT)||StringUtils.equalsIgnoreCase(sm.getSmShortdesc(), PrefixConstants.NewWaterServiceConstants.BPW))) {
				rDTO.setChallanServiceType(MainetConstants.CHALLAN_RECEIPT_TYPE.REVENUE_BASED);	
				offline.setChallanServiceType(MainetConstants.CHALLAN_RECEIPT_TYPE.REVENUE_BASED);
			}
			if (sm != null && sm.getTbDepartment() != null) {
				offline.setDeptId(sm.getTbDepartment().getDpDeptid());
				if(sm.getTbDepartment().getDpDeptcode().equals(MainetConstants.DEPT_SHORT_NAME.WATER)) {
				Long csidn=	adjustmentEntryService.getCsIdn(rDTO.getReferenceId(), rDTO.getOrgId());
				if(csidn!=null)
				offline.setUniquePrimaryId(csidn.toString());
				}
				if(StringUtils.equalsIgnoreCase(sm.getSmShortdesc(), MainetConstants.Property.PROPERTY_BILL_PAYMENT)){
					offline.setPaymentType(MainetConstants.WEB_PAYMENT);
					getPropertyWardZone(offline, rDTO);
				}
			}
		}
		offline.setApplicantFullName(rDTO.getSendFirstname());

		if (!rDTO.getReferenceId().isEmpty() && StringUtils.isNumeric(rDTO.getReferenceId()))
			offline.setApplNo(Long.valueOf(rDTO.getReferenceId()));
		
		offline.setOnlineOfflineCheck(MainetConstants.FlagY);

		if (rDTO.getSendAmount() != null) {
			offline.setAmountToPay(rDTO.getSendAmount().toString());
			offline.setAmountToShow(rDTO.getSendAmount().doubleValue());
		}
		offline.setPropNoConnNoEstateNoV(rDTO.getReferenceId());
		offline.setOfflinePaymentText(MainetConstants.PAYMENT_MODES.PCB_MODE);
		Long lookUpId=null;
		if(StringUtils.equalsIgnoreCase(rDTO.getPgSource(), MainetConstants.PG_SHORTNAME.Pine_Lab)
				|| StringUtils.equalsIgnoreCase(rDTO.getPgSource(), MainetConstants.PG_SHORTNAME.ICICI_VIZPAY)) {
			lookUpId = CommonMasterUtility.lookUpIdByLookUpCodeAndPrefix("POS", MainetConstants.PAY_PREFIX.PREFIX_VALUE,
					rDTO.getOrgId());
			offline.setPaymentType(null);// Defect #263359 in case of POS WEB rebate will not consider
		}else {
			lookUpId = CommonMasterUtility.lookUpIdByLookUpCodeAndPrefix("W", MainetConstants.PAY_PREFIX.PREFIX_VALUE,
				rDTO.getOrgId());
		}
		offline.setPayModeIn(lookUpId);
		offline.setPosPayApplicable(true);
		// offline.setPgRefId(rDTO.getPgType());
		offline.setPgRefId(rDTO.getTranCmId());
		offline.setFlatNo(rDTO.getFlatNo());
		offline.setChallanServiceType(rDTO.getChallanServiceType());
		offline.setUserId(rDTO.getUserId());
		// offline.setEmpType();
		offline.setLgIpMac(rDTO.getLgIpMac());
		offline.setApplicantName(rDTO.getSendFirstname());
		if (StringUtils.isNotEmpty(rDTO.getFeeIds()) && rDTO.getFeeIds().length() > 2)
			offline.setFeeIds(getFeeId(rDTO.getFeeIds(), rDTO.getSendAmount()));
		offline.setMobileNumber(rDTO.getSendPhone());
		if (null != rDTO.getReferenceDate()) {
		offline.setReferenceDate(rDTO.getReferenceDate());
		offline.setManualReeiptDate(rDTO.getReferenceDate());
		offline.setLoggedLocId(rDTO.getFieldId());
		}
		TbLoiMasEntity entity=tbLoiMasJpaRepository.findloiByApplicationIdAndOrgId(offline.getApplNo(),  rDTO.getOrgId());
		if(entity!=null) {
			offline.setLoiNo(entity.getLoiNo());
		}
		if(offline.getApplNo() != null) {
			offline.setTaskId(iWorkflowTaskService.getTaskIdByAppIdAndOrgId(offline.getApplNo(), rDTO.getOrgId()));			
		}

	}

	private void getPropertyWardZone(CommonChallanDTO offline, PaymentTransactionMas rDTO) {
		final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(null,
				ApplicationSession.getInstance().getMessage("GET_WARD_ZONE_BY_PROP_NO")+rDTO.getReferenceId());
		if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
			WardZoneBlockDTO wardZoneBlockDTO = (WardZoneBlockDTO) RestClient.castResponse(responseEntity, WardZoneBlockDTO.class);
			if(wardZoneBlockDTO != null) {
				WardZoneBlockDTO dwzDTO = new WardZoneBlockDTO();
			    if (wardZoneBlockDTO.getAreaDivision1() > 0) {
			    	dwzDTO.setAreaDivision1(wardZoneBlockDTO.getAreaDivision1());
			    }
			    if (wardZoneBlockDTO.getAreaDivision2() > 0) {
			    	dwzDTO.setAreaDivision2(wardZoneBlockDTO.getAreaDivision2());
			    }
			    if (wardZoneBlockDTO.getAreaDivision3() > 0) {
			    	dwzDTO.setAreaDivision3(wardZoneBlockDTO.getAreaDivision3());
			    }
			    if (wardZoneBlockDTO.getAreaDivision4() > 0) {
			    	dwzDTO.setAreaDivision4(wardZoneBlockDTO.getAreaDivision4());
			    }
			    if (wardZoneBlockDTO.getAreaDivision5() > 0) {
			    	dwzDTO.setAreaDivision5(wardZoneBlockDTO.getAreaDivision5());
			    }
			    offline.setDwzDTO(dwzDTO);
			}
		}
	}

	private Map<Long, Double> getFeeId(String feeIds, BigDecimal amt) {

		String feeIds1 = feeIds.replace("{", "").replace("}", "");
		String[] arr = feeIds1.split(",");
		Map<Long, Double> map = new HashMap();
		if (arr != null && arr.length > 0) {
			for (String s : arr) {
				String[] s1 = s.split("=");

				map.put(Long.valueOf(s1[0].toString().trim()), Double.valueOf(s1[1].toString().trim()));
			}
		} else if (amt != null) {
			map.put(1L, amt.doubleValue());
		}
		return map;
	}
	
	private void saveMutationDataAfterLoiPayment(final CommonChallanDTO requestDTO) {
		ServiceMaster serviceMst = serviceMasterService.getServiceMaster(requestDTO.getServiceId(), requestDTO.getOrgId());
		Organisation org = organisationService.getOrganisationById(requestDTO.getOrgId());
		if (Utility.isEnvPrefixAvailable(org, MainetConstants.Property.MUT)
		        && serviceMst.getSmShortdesc().equalsIgnoreCase(MainetConstants.Property.MUT)) {
			requestDTO.setApplNo(requestDTO.getApplNo());
			requestDTO.setOrgId(requestDTO.getOrgId());
			requestDTO.setUserId(requestDTO.getUserId());
			requestDTO.setLgIpMac(requestDTO.getLgIpMac());
			
			setPropertyNoForLOIPayment(requestDTO);

		    requestDTO.setServiceCode(serviceMst.getSmShortdesc());
		    requestDTO.setDeptCode(MainetConstants.Property.PROP_DEPT_SHORT_CODE);
		    if (StringUtils.isNotEmpty(org.getOrgAddressMar())) {
		    	requestDTO.setOrgAddressMar(org.getOrgAddressMar());
		    }
		    if (serviceMst.getSmServiceDuration() > 0) {
		    	requestDTO.setSlaSmDuration(serviceMst.getSmServiceDuration());
		    }
		    saveMutationAfterloiPayment(requestDTO);
		}
	}

    private void setPropertyNoForLOIPayment(CommonChallanDTO offline) {
        CommonChallanDTO commonChallanDTO = null;
        final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(offline,
                ServiceEndpoints.PROP_BY_APP_ID);
        if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
            commonChallanDTO = (CommonChallanDTO) RestClient.castResponse(responseEntity, CommonChallanDTO.class);
            if (commonChallanDTO != null) {
                offline.setPropNoConnNoEstateNoL(commonChallanDTO.getPropNoConnNoEstateNoL());
                offline.setFlatNo(commonChallanDTO.getFlatNo());
                offline.setTransferAddress(commonChallanDTO.getTransferAddress());
                offline.setTransferOwnerFullName(commonChallanDTO.getTransferOwnerFullName());
                offline.setTransferDate(commonChallanDTO.getTransferDate());
                offline.setTransferInitiatedDate(commonChallanDTO.getTransferInitiatedDate());
                offline.setRegNo(commonChallanDTO.getRegNo());
                offline.setCertificateNo(commonChallanDTO.getCertificateNo());
            }
        }
    }
    
    private Boolean saveMutationAfterloiPayment(CommonChallanDTO offline) {
        Boolean status = false;
        final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(offline,
                ServiceEndpoints.SAVE_MUTATION_AFTER_LOI_PAYMENT);
        if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
            status = (Boolean) responseEntity.getBody();
        }
        log.info("saveMutationAfterloiPayment status  : " + status);
        return status;
    }
}