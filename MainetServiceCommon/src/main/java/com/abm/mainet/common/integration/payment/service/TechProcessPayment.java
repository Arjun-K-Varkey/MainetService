package com.abm.mainet.common.integration.payment.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.abm.mainet.cfc.challan.dto.ChallanReceiptPrintDTO;
import com.abm.mainet.cfc.techprocess.dto.TechProcessRequest;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.payment.dto.PaymentRequestDTO;
import com.abm.mainet.common.utility.CallBackResponseObj;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.pg.util.TransactionRequestBean;
import com.tp.pg.util.TransactionResponseBean;

public class TechProcessPayment implements PaymentStrategy {

	protected Logger LOG = Logger.getLogger(TechProcessPayment.class);

	@Override
	public PaymentRequestDTO generatePaymentRequest(final PaymentRequestDTO paymentRequestVO)
			throws FrameworkException {

		LOG.error("Start the genreatePaymentRequest() in TechProcessPayment class ");
		try {

			if ((paymentRequestVO.getMerchantId() != null) && (paymentRequestVO.getKey() != null)
					&& (paymentRequestVO.getSalt() != null) && (paymentRequestVO.getSchemeCode() != null)
					&& (paymentRequestVO.getPgUrl() != null)) {
				final UUID customerId = UUID.randomUUID();
				String ShoppingCartDetails = null;
				final TransactionRequestBean transactionRequestBean = new TransactionRequestBean();
				transactionRequestBean.setStrRequestType(paymentRequestVO.getRequestType());
				transactionRequestBean.setStrMerchantCode(paymentRequestVO.getMerchantId());
				transactionRequestBean.setMerchantTxnRefNumber(
						MainetConstants.PG_REQUEST_PROPERTY.TXN + paymentRequestVO.getTxnid().toString());

				if ((paymentRequestVO.getProduction() != null)
						&& paymentRequestVO.getProduction().equalsIgnoreCase(MainetConstants.Common_Constant.NO)) {
					transactionRequestBean.setStrAmount("1.0");
					transactionRequestBean.setStrBankCode("470");
					ShoppingCartDetails = paymentRequestVO.getSchemeCode().concat(MainetConstants.operator.UNDER_SCORE)
							.concat("1.0").concat("_0.0");
				} else {
					transactionRequestBean.setStrAmount(paymentRequestVO.getDueAmt().toString());
					ShoppingCartDetails = paymentRequestVO.getSchemeCode().concat(MainetConstants.operator.UNDER_SCORE)
							.concat(paymentRequestVO.getDueAmt().toString()).concat("_0.0");
					transactionRequestBean.setStrBankCode(MainetConstants.BLANK);
				}
				transactionRequestBean.setStrCurrencyCode(MainetConstants.PG_REQUEST_PROPERTY.CRN);
				transactionRequestBean.setStrITC(paymentRequestVO.getTxnid() + "~" + paymentRequestVO.getServiceId()
						+ "~" + paymentRequestVO.getApplicationId() + "~" + paymentRequestVO.getUdf3() + "~"
						+ paymentRequestVO.getUdf5() + "~" + paymentRequestVO.getUdf6() + "~"
						+ paymentRequestVO.getUdf7());
				transactionRequestBean.setStrReturnURL(paymentRequestVO.getSuccessUrl());
				transactionRequestBean.setStrShoppingCartDetails(ShoppingCartDetails);
				transactionRequestBean.setTxnDate(getCurrentDate());
				transactionRequestBean.setWebServiceLocator(paymentRequestVO.getPgUrl());
				transactionRequestBean.setCustID(customerId.toString());
				transactionRequestBean.setStrTPSLTxnID(MainetConstants.BLANK);
				transactionRequestBean.setStrMobileNumber(paymentRequestVO.getMobNo());
				transactionRequestBean.setKey(paymentRequestVO.getKey().getBytes());
				transactionRequestBean.setIv(paymentRequestVO.getSalt().getBytes());
				transactionRequestBean.setStrCustomerName(paymentRequestVO.getApplicantName());
				if ((paymentRequestVO.getEmail() != null) && !paymentRequestVO.getEmail().isEmpty()) {
					transactionRequestBean.setStrEmail(paymentRequestVO.getEmail());
				}
				transactionRequestBean.setStrTimeOut(MainetConstants.PG_REQUEST_PROPERTY.SERVERTIMEOUT);
				final String tokenNo = transactionRequestBean.getTransactionToken();
				paymentRequestVO.setPayRequestMsg(tokenNo);
				LOG.error("Single Token request String" + tokenNo);
				LOG.error("End  the genreatePaymentRequest() in TechProcessPayment class ");
			} else {
				LOG.error("Payment Required parameter are not fetch from tables");
				throw new FrameworkException("Payment Required parameter are not fetch from tables");
			}
		} catch (final Exception exception) {
			LOG.error(" Exception occur in genreatePaymentRequest in TechProcessPayment", exception);
			throw new FrameworkException("Exception occur in genreatePaymentRequest ...", exception);
		}
		return paymentRequestVO;
	}

	@Override
	public Map<String, String> generatePaymentResponse(final String responseString,
			final Map<String, String> responseMap) throws FrameworkException {

		LOG.error("Starts  the genratePaymentResponse() in Techprocess class ");

		if ((responseMap.get(MainetConstants.REQUIRED_PG_PARAM.KEY) != null)
				&& (responseMap.get(MainetConstants.REQUIRED_PG_PARAM.IV) != null) && (responseString != null)) {

			LOG.error("Response message from the techprocess Payment in HashCode  " + responseString);
			final String key = responseMap.get(MainetConstants.REQUIRED_PG_PARAM.KEY);
			final String iv = responseMap.get(MainetConstants.REQUIRED_PG_PARAM.IV);
			final TransactionResponseBean responseBean = new TransactionResponseBean();
			responseBean.setIv(iv.getBytes());
			responseBean.setKey(key.getBytes());
			responseBean.setResponsePayload(responseString);
			final String responseMsg = responseBean.getResponsePayload();
			final List<String> strings = Arrays.asList(responseMsg.split("\\|"));
			LOG.error("Response message after convert into string " + strings + " response String  Size"
					+ strings.size());
			
			if (strings.size() > 0) {

				if (strings.get(0).substring(11).equals(MainetConstants.PG_REQUEST_PROPERTY.PAYMNETSTATUS)) {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.SUCCESS);
				} else {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.FAIL);
				}
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.SUCEESS_MSG, strings.get(1).substring(8));
				LOG.error(MainetConstants.REQUIRED_PG_PARAM.SUCEESS_MSG+"   "+strings.get(1).substring(8));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR, strings.get(2).substring(12));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.BNK_REF_NO, strings.get(3).substring(13));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.BANKID, strings.get(4).substring(13));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.TRANS_ID, strings.get(5).substring(12));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.AMT, strings.get(6).substring(8));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.DUE_AMT, strings.get(6).substring(8));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.NET_AMT, strings.get(6).substring(8));

				if (!strings.get(7).substring(15).isEmpty() && !strings.get(7).substring(15).equalsIgnoreCase("NA")) {

					final String[] param = strings.get(7).substring(15).split("[{}]" + "[{}]");

					if (param.length == 4) {

						final String itc = param[0].substring(5);
						final String email = param[1].substring(6);
						final String mob = param[2].substring(4);
						final String name = param[3].substring(9).replace("}", MainetConstants.BLANK);
						responseMap.put(MainetConstants.BankParam.EMAIL, email);
						responseMap.put(MainetConstants.BankParam.FNAME, name);
						responseMap.put(MainetConstants.BankParam.PHONE, mob);
						final String strArray[] = itc.split("\\~");
						responseMap.put(MainetConstants.REQUIRED_PG_PARAM.TRACK_ID, strArray[0]);
						responseMap.put(MainetConstants.BankParam.UDF1, strArray[1]);
						responseMap.put(MainetConstants.BankParam.UDF2, strArray[2]);
						responseMap.put(MainetConstants.BankParam.UDF3, strArray[3]);
						responseMap.put(MainetConstants.BankParam.UDF5, strArray[4]);
						responseMap.put(MainetConstants.BankParam.UDF6, strArray[5]);
						responseMap.put(MainetConstants.BankParam.UDF7, strArray[6]);
					} else {
						final String itc = param[0].substring(5);
						final String mob = param[1].substring(4);
						final String name = param[2].substring(9).replace("}", MainetConstants.BLANK);
						responseMap.put(MainetConstants.BankParam.FNAME, name);
						responseMap.put(MainetConstants.BankParam.PHONE, mob);
						final String strArray[] = itc.split("\\~");
						responseMap.put(MainetConstants.REQUIRED_PG_PARAM.TRACK_ID, strArray[0]);
						responseMap.put(MainetConstants.BankParam.UDF1, strArray[1]);
						responseMap.put(MainetConstants.BankParam.UDF2, strArray[2]);
						responseMap.put(MainetConstants.BankParam.UDF3, strArray[3]);
						responseMap.put(MainetConstants.BankParam.UDF5, strArray[4]);
						responseMap.put(MainetConstants.BankParam.UDF6, strArray[5]);
						responseMap.put(MainetConstants.BankParam.UDF7, strArray[6]);

					}
				}

				else {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR,
							"Failed from fetch the data from payment gateway");
				}
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAY_SRC, MainetConstants.PG_SHORTNAME.TECH_PROCESS);
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAYDATE, strings.get(8).substring(14));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.BNK_REF_NO, strings.get(9).substring(13));
				responseMap.put(MainetConstants.BankParam.HASH, strings.get(12).substring(5));
			} else {
				LOG.error("Payment Required parameter are not fetch from tables");
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR, "CheckSum not Match");
				throw new FrameworkException("additional  data are not fetch in response send by techprocess");
			}
		} else {
			LOG.error("Response message are not convert into string");
			responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR, "CheckSum not Match");
			throw new FrameworkException("Response message are not match send by techprocess");
		}
		LOG.error("End  the genratePaymentResponse() in TechProcessPayment class ");

		return responseMap;
	}

	public String getCurrentDate() {

		final DateFormat inputformat = new SimpleDateFormat("dd-MM-yyyy");
		final String currentDate = inputformat.format(new Date());
		return currentDate;
	}

	public String synchPayment(final PaymentRequestDTO paymentRequestVO) throws Exception{
		TransactionRequestBean objTransactionRequestBean = new TransactionRequestBean();

		objTransactionRequestBean.setStrRequestType("O");
		objTransactionRequestBean.setStrMerchantCode(paymentRequestVO.getMerchantId());
		
		objTransactionRequestBean.setTxnDate(paymentRequestVO.getAddField1());
		objTransactionRequestBean.setMerchantTxnRefNumber(
				MainetConstants.PG_REQUEST_PROPERTY.TXN + paymentRequestVO.getTxnid().toString());
		
		objTransactionRequestBean.setWebServiceLocator("https://www.tpsl- india.in/PaymentGateway/services/TransactionDetailsNew");
		return objTransactionRequestBean.getTransactionToken();

	}

	public CallBackResponseObj getPaymentStatus(TechProcessRequest payment, String URL) {
		CallBackResponseObj respObj=new CallBackResponseObj();
		Map<String, Object> etpJsonMap = new HashMap<String, Object>();
		String statusMsg=MainetConstants.BLANK;
		try {
			LOG.info("Request Parameter to calling status api " + payment.toString());
			ResponseEntity etpObj = RestClient.callRestTemplateClient(payment, URL);
			String etpRequstKey = null;
			try {

				etpRequstKey = new ObjectMapper().writeValueAsString(etpObj.getBody());
				etpJsonMap = new ObjectMapper().readValue(etpRequstKey, new TypeReference<Map<String, Object>>() {
				});
			} catch (JsonProcessingException e1) {
				LOG.error("Error  occure at the time of fetching Payment response data" + e1);
			}

		} catch (Exception e) {
			LOG.error("Error  occure at the time of Payment status API" + e);

		}
		if (etpJsonMap != null && etpJsonMap.get(MainetConstants.PaymentMethod) != null) {
			Map<String, Object> etpStatusMap = new HashMap<String, Object>();
			String status = null;
			try {
				status = new ObjectMapper().writeValueAsString(etpJsonMap.get(MainetConstants.PaymentMethod));
				etpStatusMap = etpJsonMap = new ObjectMapper().readValue(status,
						new TypeReference<Map<String, Object>>() {
						});

			} catch (Exception e1) {
				LOG.error("Error  occure at the time of paymentMethod data" + e1);

			}
			if (etpStatusMap != null && etpStatusMap.get(MainetConstants.PaymentTransaction) != null) {
				Map<String, Object> etpStatusMap1 = new HashMap<String, Object>();
				String status1 = null;
				try {
					status1 = new ObjectMapper().writeValueAsString(etpJsonMap.get(MainetConstants.PaymentTransaction));
					etpStatusMap1 = etpJsonMap = new ObjectMapper().readValue(status1,
							new TypeReference<Map<String, Object>>() {
							});
				} catch (Exception e) {
					LOG.error("Error  occure at the time of paymentTransaction data" + e);
				}
				if ((etpStatusMap1 != null && etpStatusMap1.get(MainetConstants.StatusMessage) != null)) {
					 statusMsg = etpStatusMap1.get(MainetConstants.StatusMessage).toString();
					 respObj.setStatus(statusMsg);
					 respObj.setStatusMsg(statusMsg);
				}
				if((etpStatusMap1 != null && etpStatusMap1.get(MainetConstants.Identifier) != null)) {
					respObj.setOrder_no(etpStatusMap1.get(MainetConstants.Identifier).toString());
				}
			}
		}
		
		return respObj;
		}
	
	public static void main(String args[]) throws ParseException {
		TechProcessRequest payment = new TechProcessRequest();
		payment.getMerchant().setIdentifier("L50222");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse("2026-05-07 00:28:56");
		String date1 = Utility.dateToString(date, MainetConstants.DATE_FORMATS);
		payment.getTransaction().setDateTime(date1);
		payment.getTransaction().setIdentifier(MainetConstants.PG_REQUEST_PROPERTY.TXN+"1498197");
		payment.getTransaction().setRequestType("O");
		TechProcessPayment techPayment=new TechProcessPayment();
		System.out.println(techPayment.getPaymentStatus(payment, "https://www.paynimo.com/api/paynimoV2.req"));
	}
}