package com.abm.mainet.cfc.challan.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.cfc.challan.dto.VizpayRequestDto;
import com.abm.mainet.cfc.challan.dto.VizpayResponseDto;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.payment.dao.PaymentDAO;
import com.abm.mainet.common.integration.payment.dto.PaymentRequestDTO;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.integration.payment.service.PaymentService;
import com.abm.mainet.common.master.dto.TbLocationMas;
import com.abm.mainet.common.service.ILocationMasService;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CallBackResponseObj;
import com.abm.mainet.common.utility.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Rahul.Yadav
 *
 */
@Service
public class PushToPayServiceImpl implements IPushToPayService {
	protected Logger LOG = Logger.getLogger(PushToPayServiceImpl.class);
	
	private static final int MAX_STATUS_RETRY = 7;
	private static final long FIRST_RETRY_DELAY_MS = 30000; // 30 sec
	private static final long NEXT_RETRY_DELAY_MS  = 10000; // 10 sec

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentDAO paymentDAO;
	 @Autowired
	   private ILocationMasService iLocationMasService;
	 
	 @Autowired
	 private VizpayTxnService vizpayTxnService;
	 
	public static void main(String args[]) throws IOException {
		PineLabRequestDto dto = new PineLabRequestDto();
		dto.setAllowedPaymentMode("1");
		dto.setAmount("2400");
		dto.setAutoCancelDurationInMinutes("3");
		dto.setClientID("1012601");
		dto.setMerchantID("29610");
		dto.setSecurityToken("a4c9741b-2889-47b8-be2f-ba42081a246e");
		dto.setSequenceNumber("122");
		dto.setStoreID("1221258");
		dto.setUserID("Test");
		dto.setTransactionNumber("122");
		String requstKey = null;
		ObjectMapper objectMapper = new ObjectMapper();

		/*
		 * ResponseEntity obj = RestClient.callRestTemplateClient(dto,
		 * "https://www.plutuscloudserviceuat.in:8201/API/CloudBasedIntegration/V1/UploadBilledTransaction"
		 * ); requstKey = new ObjectMapper().writeValueAsString(obj.getBody());
		 * PineLabResponseDto jsonMap = objectMapper.readValue(requstKey,
		 * PineLabResponseDto.class); System.out.println("Result >>>  " +
		 * jsonMap.toString()); if (jsonMap != null &&
		 * StringUtils.equalsIgnoreCase(jsonMap.getResponseCode(), "0")) {
		 */
			dto.setPlutusTransactionReferenceID("698266");
			ResponseEntity obj1 = RestClient.callRestTemplateClient(dto,
					"https://www.plutuscloudserviceuat.in:8201/API/CloudBasedIntegration/V1/GetCloudBasedTxnStatus");
			requstKey = new ObjectMapper().writeValueAsString(obj1.getBody());
			PineLabResponseDto jsonMap1 = objectMapper.readValue(requstKey, PineLabResponseDto.class);
			System.out.println(jsonMap1 + "    testt");
		
	}

	@Override
	public CommonChallanDTO callPushToPayAPIForPineLab(String seq, CommonChallanDTO offline)
			throws InterruptedException {
		CommonChallanDTO challanDto = new CommonChallanDTO();
		String initiateurl = ApplicationSession.getInstance().getMessage("challan.pinelab.initiateurl");
		String statusUrl = ApplicationSession.getInstance().getMessage("challan.pinelab.stsurl");
		String timeout = ApplicationSession.getInstance().getMessage("challan.pinelab.timeout");
		String sleepTIme = ApplicationSession.getInstance().getMessage("challan.pinelab.sleepTime");
		String paymode = ApplicationSession.getInstance().getMessage("challan.pinelab.paymode");
		String clientId = ApplicationSession.getInstance().getMessage("challan.pinelab.clientId");
		String mid = ApplicationSession.getInstance().getMessage("challan.pinelab.mid");
		String sToken = ApplicationSession.getInstance().getMessage("challan.pinelab.sToken");
		String storeId = ApplicationSession.getInstance().getMessage("challan.pinelab.storeId");
		String userId = ApplicationSession.getInstance().getMessage("challan.pinelab.userId");
		String autoCnclDur = ApplicationSession.getInstance().getMessage("challan.pinelab.autoCnclDur");
		
		PineLabRequestDto dto = new PineLabRequestDto();
		PaymentTransactionMas payMas=new PaymentTransactionMas();
		dto.setAllowedPaymentMode(paymode);
		if (offline.getAmountToPay() != null) {
			dto.setAmount(String.valueOf(((long) Double.parseDouble(offline.getAmountToPay()) * 100)));
		}
		dto.setAutoCancelDurationInMinutes(autoCnclDur);
		if(offline.getDeviceId()!=null) {
			dto.setClientID(offline.getDeviceId().toString());	
		}else {
		dto.setClientID(clientId);
		}dto.setMerchantID(mid);
		dto.setSecurityToken(sToken);
		dto.setSequenceNumber(seq);
		dto.setStoreID(storeId);
		dto.setUserID(userId);
		dto.setTransactionNumber(seq);
		String requstKey = null;
		ResponseEntity obj = null;
		PineLabResponseDto jsonMap = null;
		ResponseEntity obj1 = null;
		PineLabResponseDto jsonMap1 = null;
		LOG.info("Start the callPushToPayAPIForPineLab() in  Method "+dto);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			obj = RestClient.callRestTemplateClient(dto, initiateurl);
			requstKey = new ObjectMapper().writeValueAsString(obj.getBody());
			jsonMap = objectMapper.readValue(requstKey, PineLabResponseDto.class);
			offline.setPosTxnId(jsonMap!=null?jsonMap.getPlutusTransactionReferenceID():null);
			payMas = saveInOnlinePaymentTable(offline, null);
			offline.setPgRefId(payMas.getTranCmId());
			LOG.info("Start the response1 in  callPushToPayAPIForPineLab() in  Method "+jsonMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean flag = false;
		long sTime = System.currentTimeMillis();
		if (jsonMap != null && StringUtils.equalsIgnoreCase(jsonMap.getResponseCode(), "0")) {
			dto.setPlutusTransactionReferenceID(jsonMap.getPlutusTransactionReferenceID());
			while (!flag) {

				try {
					Thread.currentThread().sleep(Long.valueOf(sleepTIme));
					obj1 = RestClient.callRestTemplateClient(dto, statusUrl);
					requstKey = new ObjectMapper().writeValueAsString(obj1.getBody());
					jsonMap1 = objectMapper.readValue(requstKey, PineLabResponseDto.class);
					LOG.info("Start the response2 in  callPushToPayAPIForPineLab() in  Method "+jsonMap1);
					if (jsonMap1 != null && StringUtils.equalsIgnoreCase(jsonMap1.getResponseCode(), "0")) {
						flag = true;
						challanDto.setPushToPayErrMsg(null);
						if(jsonMap1.getTransactionData()!=null) {
						for (TransactionData pdto : jsonMap1.getTransactionData()) {
							if (pdto.getTag() != null && StringUtils.equals(pdto.getTag(), "PaymentMode")) {
								challanDto.setPosPayMode(pdto.getValue());
								offline.setPosPayMode(pdto.getValue());

							}
							if (jsonMap1.getPlutusTransactionReferenceID() != null) {
								challanDto.setPosTxnId(jsonMap1.getPlutusTransactionReferenceID());
								offline.setPosTxnId(jsonMap1.getPlutusTransactionReferenceID());
							}
							
							
						}
						
						}
						payMas.setRecvBankRefNum(jsonMap1.getPlutusTransactionReferenceID());
						payMas.setRecvMihpayid(jsonMap1.getPlutusTransactionReferenceID());
						payMas.setRecvStatus(MainetConstants.PAYU_STATUS.SUCCESS);
						payMas.setRecvMode(offline.getPosPayMode());
						updateInOnlinePaymentTable(payMas);
						return challanDto;
					
					} else {
						challanDto.setPushToPayErrMsg(jsonMap1 != null ? jsonMap1.getResponseMessage() : null);
					}

					long lTime = System.currentTimeMillis();
					if ((lTime - sTime) > Long.valueOf(timeout)) {
						challanDto.setPushToPayErrMsg(
								ApplicationSession.getInstance().getMessage("challan.easyTap.timeoutMsg"));
						flag = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			challanDto.setPushToPayErrMsg(jsonMap != null ? jsonMap.getResponseMessage() : null);
		}

		return challanDto;
	}
    @Transactional
	private PaymentTransactionMas saveInOnlinePaymentTable(CommonChallanDTO offline, String deviceId) {
		PaymentRequestDTO paymentTransactionMas=new PaymentRequestDTO();
		paymentTransactionMas.setApplicantName(offline.getApplicantName()!=null?offline.getApplicantName():MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setMobNo(offline.getMobileNumber());
		paymentTransactionMas.setEmail(offline.getEmailId());
		if(StringUtils.equals(MainetConstants.PG_SHORTNAME.ICICI_VIZPAY, offline.getPaymentGatewayName())) {
			paymentTransactionMas.setPgUrl(ApplicationSession.getInstance().getMessage("challan.erp.pushTxn"));			
			paymentTransactionMas.setPgName(MainetConstants.PG_SHORTNAME.ICICI_VIZPAY);
			paymentTransactionMas.setAddField7(deviceId);
		} else {
			paymentTransactionMas.setPgUrl(ApplicationSession.getInstance().getMessage("challan.pinelab.initiateurl"));
			paymentTransactionMas.setSuccessUrl(ApplicationSession.getInstance().getMessage("challan.pinelab.stsurl"));			
			paymentTransactionMas.setPgName("Pine Lab");
		}
		paymentTransactionMas.setKey(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setSalt(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setHash(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setFailUrl(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setBankId(1L);
		paymentTransactionMas.setOrderId(offline.getPosTxnId());
		PaymentTransactionMas pMas=new PaymentTransactionMas();
		paymentTransactionMas.setServiceId(offline.getServiceId());
		paymentTransactionMas.setServiceName(offline.getServiceName()!=null?offline.getServiceName():MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setEmpId(offline.getUserId());
		paymentTransactionMas.setUdf2(offline.getApplNo()!=null?offline.getApplNo().toString():offline.getPropNoConnNoEstateNoV());
		paymentTransactionMas.setOrgId(offline.getOrgId());
		paymentTransactionMas.setFlatNo(offline.getFlatNo());
		paymentTransactionMas.setOrderId(offline.getPosTxnId());
		paymentTransactionMas.setDocumentUploaded(offline.isDocumentUploaded()?"Y":"N");
		paymentTransactionMas.setChallanServiceType(offline.getChallanServiceType());
		if(StringUtils.isNotBlank(offline.getAmountToPay()))
			paymentTransactionMas.setCancelUrl(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setDueAmt(new BigDecimal(offline.getAmountToPay()));
		 if (offline.getLoggedLocId() != null) {
             final TbLocationMas locMas = iLocationMasService.findById(offline.getLoggedLocId());
             if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
            	 paymentTransactionMas.setFieldId(locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1());
             }
         } 
	     try {
		return	paymentService.savePosTransactionMaster(paymentTransactionMas);
		} catch (FrameworkException e) {
			e.printStackTrace();
		}
	     return pMas;
	}
	@Transactional
	public void updateInOnlinePaymentTable(PaymentTransactionMas paymentTransactionMas) {
		
		paymentService.updateOnlinePOSTransactionMas(paymentTransactionMas);
	}

	@Override
	public CallBackResponseObj getPineLabPaymentStatus(PaymentTransactionMas rDTO) {
		CallBackResponseObj responseObj = new CallBackResponseObj();
		String mid = ApplicationSession.getInstance().getMessage("challan.pinelab.mid");
		String sToken = ApplicationSession.getInstance().getMessage("challan.pinelab.sToken");
		String storeId = ApplicationSession.getInstance().getMessage("challan.pinelab.storeId");
		String statusUrl = ApplicationSession.getInstance().getMessage("challan.pinelab.stsurl");
		PineLabRequestDto dto = new PineLabRequestDto();
		dto.setSecurityToken(sToken);
		dto.setMerchantID(mid);
        dto.setStoreID(storeId);
        dto.setPlutusTransactionReferenceID(rDTO.getRecvBankRefNum());
        PineLabResponseDto jsonMap1 = null;
		String requstKey = null;
		ResponseEntity obj1 = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			obj1 = RestClient.callRestTemplateClient(dto, statusUrl);
			requstKey = new ObjectMapper().writeValueAsString(obj1.getBody());
			jsonMap1 = objectMapper.readValue(requstKey, PineLabResponseDto.class);
			if(jsonMap1 != null && StringUtils.equalsIgnoreCase(jsonMap1.getResponseCode(), "0")&&StringUtils.equalsIgnoreCase(jsonMap1.getResponseMessage(), "TXN APPROVED")) {
			responseObj.setStatus(MainetConstants.PAYMENT_STATUS.SUCCESS);
			responseObj.setStatusMsg(jsonMap1.getResponseMessage());
				if (CollectionUtils.isNotEmpty(jsonMap1.getTransactionData())) {
					for (TransactionData pdto : jsonMap1.getTransactionData()) {
						if (pdto.getTag() != null && StringUtils.equals(pdto.getTag(), "PaymentMode")) {
							responseObj.setPosPayMode(pdto.getValue());
						}
						if (jsonMap1.getPlutusTransactionReferenceID() != null) {
							responseObj.setPosTxnId(jsonMap1.getPlutusTransactionReferenceID());
						}
					}
				}
			return responseObj;
			}else {
				responseObj.setStatus(MainetConstants.PAYMENT_STATUS.FAILURE);
				responseObj.setStatusMsg(jsonMap1.getResponseMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseObj;
	}
	
	@Override
	@Transactional
	public CommonChallanDTO callPushToPayAPIFoICICIVIZPAY(String deviceId, CommonChallanDTO challan) {

	    // ---------------- Load Configurations ---------------- //
	    String mid                 = ApplicationSession.getInstance().getMessage("challan.erp.mid");
	    String clientId            = ApplicationSession.getInstance().getMessage("challan.erp.clientId");
	    String pushTxnURL          = ApplicationSession.getInstance().getMessage("challan.erp.pushTxn");
	    String callbackCheckStatus = ApplicationSession.getInstance().getMessage("challan.erp.callback.check.status");
	    String sourceId            = ApplicationSession.getInstance().getMessage("challan.erp.sourceId");
	    String checkStatus 		   = ApplicationSession.getInstance().getMessage("challan.erp.check.status");

	    // ---------------- Save into Payment Transaction Table ---------------- //

		PaymentRequestDTO paymentTransactionMas = new PaymentRequestDTO();
		paymentTransactionMas.setApplicantName(
				challan.getApplicantName() != null ? challan.getApplicantName() : MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setMobNo(challan.getMobileNumber());
		paymentTransactionMas.setEmail(challan.getEmailId());
		paymentTransactionMas.setPgUrl(ApplicationSession.getInstance().getMessage("challan.erp.pushTxn"));
		paymentTransactionMas.setPgName(MainetConstants.PG_SHORTNAME.ICICI_VIZPAY);
		paymentTransactionMas.setAddField7(deviceId);
		paymentTransactionMas.setKey(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setSalt(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setHash(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setFailUrl(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setBankId(1L);
		paymentTransactionMas.setOrderId(challan.getPosTxnId());
		paymentTransactionMas.setServiceId(challan.getServiceId());
		paymentTransactionMas.setServiceName(
				challan.getServiceName() != null ? challan.getServiceName() : MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setEmpId(challan.getUserId());
		paymentTransactionMas.setUdf2(
				challan.getApplNo() != null ? challan.getApplNo().toString() : challan.getPropNoConnNoEstateNoV());
		paymentTransactionMas.setOrgId(challan.getOrgId());
		paymentTransactionMas.setFlatNo(challan.getFlatNo());
		paymentTransactionMas.setOrderId(challan.getPosTxnId());
		paymentTransactionMas.setDocumentUploaded(challan.isDocumentUploaded() ? "Y" : "N");
		paymentTransactionMas.setChallanServiceType(challan.getChallanServiceType());
		if (StringUtils.isNotBlank(challan.getAmountToPay()))
			paymentTransactionMas.setCancelUrl(MainetConstants.CommonConstants.NA);
		paymentTransactionMas.setDueAmt(new BigDecimal(challan.getAmountToPay()));
		if (challan.getLoggedLocId() != null) {
			final TbLocationMas locMas = iLocationMasService.findById(challan.getLoggedLocId());
			if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
				paymentTransactionMas.setFieldId(locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1());
			}
		}

		PaymentTransactionMas payMas = vizpayTxnService.savePosTransactionMaster(paymentTransactionMas);

	    // ---------------- Prepare Request DTO ---------------- //
	    VizpayRequestDto dto = new VizpayRequestDto();
	    dto.setMid(mid);
	    dto.setTid(deviceId);
	    dto.setTran_type("0");
	    dto.setAmount(challan.getAmountToPay());
	    dto.setBill_no(String.valueOf(payMas.getTranCmId()));
	    dto.setErp_tran_id(new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()));
	    dto.setErp_client_id(clientId);
	    dto.setSource_id(sourceId);

	    ObjectMapper mapper = new ObjectMapper();

	    // ---------------- 1. Call Push Transaction API ---------------- //
	    try {
	        ResponseEntity<?> pushResp = RestClient.callRestTemplateClient(dto, pushTxnURL);
	        LOG.info("PushTxn Response: "+ pushResp.getBody());
	    } catch (Exception e) {
	        LOG.error("Error calling PushTxn API: ", e);
	        challan.setPushToPayErrMsg(e.getMessage());
	        return challan; // do not proceed further
	    }

	    // ---------------- 2. Sleep Before Status Check ---------------- //
	    boolean success = false;

	    for (int attempt = 1; attempt <= MAX_STATUS_RETRY; attempt++) {

	        try {
	            // Delay BEFORE calling status API
	            if (attempt == 1) {
	                TimeUnit.MILLISECONDS.sleep(FIRST_RETRY_DELAY_MS);
	            } else {
	                TimeUnit.MILLISECONDS.sleep(NEXT_RETRY_DELAY_MS);
	            }

	            LOG.info("Checking payment status | Attempt " + attempt + " | tranCmId=" + payMas.getTranCmId());

				ResponseEntity<?> checkStatusResp = RestClient.callRestTemplateClient(dto, checkStatus);
	            
	            String checkStatusJson = mapper.writeValueAsString(checkStatusResp.getBody());
	            JsonNode checkStatusJsonNode = mapper.readTree(checkStatusJson);
	            String checkStatusResponseCode = checkStatusJsonNode.path("ResponseCode").asText("");
	            String checkStatusResponseDesc = checkStatusJsonNode.path("ResponseDesc").asText("");

	            LOG.info("Status API Response: Code=" + checkStatusResponseCode + " Desc=" + checkStatusResponseDesc);
	            
				if (StringUtils.equalsIgnoreCase(checkStatusResponseDesc, "Transaction cancelled")
						|| StringUtils.equalsIgnoreCase(checkStatusResponseDesc, "Transaction Timeout")) {
					payMas.setRecvStatus(MainetConstants.PAYU_STATUS.FAIL);
					payMas.setRecvErrm(checkStatusResponseDesc);
					challan.setPushToPayErrMsg(checkStatusResponseDesc);
					updateInOnlinePaymentTable(payMas);
					return challan;
				}
				ResponseEntity<?> callBackStatusResp = RestClient.callRestTemplateClient(dto, callbackCheckStatus);

	            String json = mapper.writeValueAsString(callBackStatusResp.getBody());
	            JsonNode jsonNode = mapper.readTree(json);

	            String responseCode = jsonNode.path("ResponseCode").asText("");
	            String responseDesc = jsonNode.path("ResponseDesc").asText("");

	            LOG.info("Status API Response: Code=" + responseCode + " Desc=" + responseDesc);

	            
	            if ("00".equals(responseCode)) {
	                // SUCCESS
	                JsonNode rspData = jsonNode.path("RspData");

	                payMas.setRecvBankRefNum(rspData.path("RRN").asText());
	                payMas.setRecvMihpayid(rspData.path("AuthCode").asText());
	                payMas.setRecvMode(rspData.path("TranType").asText());
	                payMas.setRecvStatus(MainetConstants.PAYU_STATUS.SUCCESS);

	                challan.setPosTxnId(rspData.path("Stan").asText());
	                challan.setPosPayMode(rspData.path("TranType").asText());
	                challan.setPushToPayErrMsg(null);
	                challan.setPgRefId(payMas.getTranCmId());

	                updateInOnlinePaymentTable(payMas);

	                success = true;
	                break; // Stop retry loop
	            }

	        } catch (InterruptedException ie) {
	            Thread.currentThread().interrupt();
	            LOG.error("Status retry interrupted", ie);
	            break;

	        } catch (Exception ex) {
	            LOG.error("Error during status check attempt "+ attempt, ex);
	        }
	    }

	    // After all retries failed
	    if (!success) {
	        payMas.setRecvStatus(MainetConstants.PAYU_STATUS.FAIL);
	        payMas.setRecvErrm("Payment status not confirmed after retries");
	        challan.setPushToPayErrMsg("Payment status not confirmed after retries");
	        updateInOnlinePaymentTable(payMas);
	    }

	    return challan;
	}

	@Override
	public CallBackResponseObj getICICIVIZPAYPaymentStatus(PaymentTransactionMas rDTO) {

		// ---------------- Load Configurations ---------------- //
	    String mid                 = ApplicationSession.getInstance().getMessage("challan.erp.mid");
	    String clientId            = ApplicationSession.getInstance().getMessage("challan.erp.clientId");
	    String sourceId            = ApplicationSession.getInstance().getMessage("challan.erp.sourceId");
	    String callbackCheckStatus = ApplicationSession.getInstance().getMessage("challan.erp.callback.check.status");

	    CallBackResponseObj respObj = null;
	    ObjectMapper mapper = new ObjectMapper();
	    // ---------------- Prepare Request DTO ---------------- //
	    VizpayRequestDto vizpayRequestDto = new VizpayRequestDto();
	    vizpayRequestDto.setMid(mid);
	    vizpayRequestDto.setTid(rDTO.gettDesc());// deviceID
	    vizpayRequestDto.setTran_type("0");
	    vizpayRequestDto.setBill_no(String.valueOf(rDTO.getTranCmId()));
	    vizpayRequestDto.setErp_client_id(clientId);
	    vizpayRequestDto.setSource_id(sourceId);
	    
	    // ---------------- 3. Call Status API ---------------- //
	    try {
	        ResponseEntity<?> statusResp = RestClient.callRestTemplateClient(vizpayRequestDto, callbackCheckStatus);
	        String json = mapper.writeValueAsString(statusResp.getBody());
	        JsonNode jsonNode = mapper.readTree(json);

	        String responseCode = jsonNode.path("ResponseCode").asText("");
	        String responseDesc = jsonNode.path("ResponseDesc").asText("");

			LOG.info("Status Response: Code=" + responseCode + " Desc=" + responseDesc);

	        if ("00".equals(responseCode)) {
	        	// Success
	        	JsonNode rspData = jsonNode.path("RspData");
	        	respObj=new CallBackResponseObj();
	        	respObj.setPosTxnId(rspData.path("Stan").asText());
	        	rDTO.setRecvBankRefNum(rspData.path("RRN").asText());
	        	rDTO.setRecvMihpayid(rspData.path("AuthCode").asText());
	        	respObj.setPosPayMode(rDTO.getRecvMode());
	        	respObj.setStatus(MainetConstants.PAYMENT_STATUS.SUCCESS);
	        	respObj.setStatusMsg(responseDesc);
	        } else {
	            // Failure
	        	respObj=new CallBackResponseObj();
	        	respObj.setStatus(MainetConstants.PAYMENT_STATUS.FAILURE);
	        	respObj.setStatusMsg(responseDesc);
	        }

	    } catch (Exception e) {
	        LOG.error("Error parsing Status API Response: ", e);
	    }
		return respObj;
	
	}
	
}
