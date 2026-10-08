package com.abm.mainet.common.integration.payment.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.payment.dto.PaymentRequestDTO;
import com.abm.mainet.common.integration.payment.dto.getpaydto.PaymentApi;
import com.abm.mainet.common.integration.payment.dto.getpaydto.PgEncryption;
import com.abm.mainet.common.integration.payment.dto.getpaydto.RequestVo;
import com.abm.mainet.common.integration.payment.dto.getpaydto.ResponseWrapper;
import com.abm.mainet.common.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetPay implements PaymentStrategy{

	protected Logger LOG = Logger.getLogger(GetPay.class);
	public static void main(String[] args) throws Exception {
		String url = "https://pay1.getepay.in:8443/getepayPortal/pg/generateInvoice";
		String mid = "108";
		String terminalId = "getepay.merchant61062@icici";
		String key = "JoYPd+qso9s7T+Ebj8pi4Wl8i+AHLv+5UNJxA3JkDgY=";
		String iv = "hlnuyA9b4YxDq6oJSZFl8g==";

		PaymentApi paymentApi = new PaymentApi();
		RequestVo requestVo = new RequestVo();
		requestVo.setBaseUrl(url);
		requestVo.setMid(mid);
		requestVo.setTerminalId(terminalId);
		requestVo.setIv(iv);
		requestVo.setKey(key);
		requestVo.setAmount("100");
		requestVo.setName("Test");
		requestVo.setMobileNumber("1234567890");
		requestVo.setEmailId("test@gmail.com");
		requestVo.setMerchantTransactionId("" + System.currentTimeMillis());
		requestVo.setRu("https://pay1.getepay.in:8443/getepayPortal/pg/pgPaymentResponse");

//		Optional
		requestVo.setCallbackUrl("");
//		Udf param udf4 to udf 10
		requestVo.setUdf4("");

		ResponseWrapper response = paymentApi.call(requestVo);
		System.out.println("final result ==>> " + response);
	}

	@Override
	public PaymentRequestDTO generatePaymentRequest(PaymentRequestDTO paymentRequestVO) throws FrameworkException {

		String url = "https://pay1.getepay.in:8443/getepayPortal/pg/generateInvoice";
		String mid = "108";
		String terminalId = "getepay.merchant61062@icici";
		String key = "JoYPd+qso9s7T+Ebj8pi4Wl8i+AHLv+5UNJxA3JkDgY=";
		String iv = "hlnuyA9b4YxDq6oJSZFl8g==";

		PaymentApi paymentApi = new PaymentApi();
		RequestVo requestVo = new RequestVo();
		requestVo.setBaseUrl(paymentRequestVO.getPgUrl());
		requestVo.setMid(paymentRequestVO.getMerchantId());
		requestVo.setTerminalId(paymentRequestVO.getTerminalId());
		requestVo.setIv(paymentRequestVO.getSalt());
		requestVo.setKey(paymentRequestVO.getKey());
		requestVo.setAmount(paymentRequestVO.getDueAmt().toString());
		requestVo.setName(paymentRequestVO.getApplicantName());
		requestVo.setMobileNumber(paymentRequestVO.getMobNo());
		requestVo.setEmailId(paymentRequestVO.getEmail());
		requestVo.setMerchantTransactionId(paymentRequestVO.getTxnid().toString());
		requestVo.setRu(paymentRequestVO.getSuccessUrl());
		requestVo.setUdf6(paymentRequestVO.getApplicationId());
		requestVo.setUdf7(paymentRequestVO.getSecurityId());
		requestVo.setUdf5(paymentRequestVO.getUdf5());
		

//		Optional
		requestVo.setCallbackUrl("");
//		Udf param udf4 to udf 10
		requestVo.setUdf4("");

		ResponseWrapper response = null;
		try {
			response = paymentApi.call(requestVo);
			LOG.info("final result ==>> " + response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response != null) {
			paymentRequestVO.setPayRequestMsg(response.getPaymentUrl());
			paymentRequestVO.setBankIdStr(response.getPaymentId());
		}
		return paymentRequestVO;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> generatePaymentResponse(String responseString, Map<String, String> responseMap)
			throws FrameworkException {
		ObjectMapper mapper=new ObjectMapper();
		if (responseMap.containsKey(MainetConstants.REQUIRED_PG_PARAM.KEY)) {
			String workingKey = responseMap.get(MainetConstants.REQUIRED_PG_PARAM.KEY);
			PgEncryption encryption = new PgEncryption(responseMap.get(MainetConstants.REQUIRED_PG_PARAM.IV), responseMap.get(MainetConstants.REQUIRED_PG_PARAM.KEY));
			String decVal = encryption.decrypt(responseMap.get("response"));
			LOG.info("Response Map >>> " + decVal);
			Map<String, String> map =new HashMap<>();
			try {
				   if (decVal.startsWith("\"") && decVal.endsWith("\"")) {
					   decVal = decVal.substring(1, decVal.length() - 1)
		                                       .replace("\\\"", "\""); // Unescape quotes
		            }
				map = mapper.readValue(decVal, Map.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (MapUtils.isNotEmpty(map)) {
				if (map.get("txnStatus") != null && map.get("txnStatus").equalsIgnoreCase(MainetConstants.PAYU_STATUS.SUCCESS)) {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.SUCCESS);
				} else {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.FAIL);
				}
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR, map.get("message"));
				responseMap.put(MainetConstants.BankParam.TXN_ID, map.get("merchantOrderNo"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.AMT,
						responseMap.get(MainetConstants.REQUIRED_PG_PARAM.SESSION_AMT));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.NET_AMT, map.get("totalAmount"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.BNK_REF_NO, map.get("getepayTxnId"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAYDATE, Utility.dateToString(new Date()));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.TRANS_ID, map.get("getepayTxnId"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF3, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF6));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF1, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF7));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF2, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF6));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF5, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF5));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAY_SRC, MainetConstants.PG_SHORTNAME.GETEPAY_IDBI);
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.MODE,  map.get("paymentMode"));
			}
		} else {

			LOG.error("Working key is not available for ISGPay response");
			throw new FrameworkException("Working key is not available for ISGPay response");

		}

		return responseMap;

	}
}
