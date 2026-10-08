package com.abm.mainet.common.integration.payment.dto.getpaydto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jfree.util.Log;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.utility.CallBackResponseObj;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentApi {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentApi.class);
	
	public ResponseWrapper call(RequestVo requestVo) throws Exception {
		PgRequest pgRequest = new PgRequest();
		pgRequest.setMid(requestVo.getMid());
		pgRequest.setAmount(requestVo.getAmount());
		pgRequest.setMerchantTransactionId(requestVo.getMerchantTransactionId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		pgRequest.setTransactionDate(sdf.format(new Date()));
		pgRequest.setTerminalId(requestVo.getTerminalId());
		pgRequest.setVpa(requestVo.getTerminalId());

		pgRequest.setUdf1(requestVo.getMobileNumber());
		pgRequest.setUdf2(requestVo.getEmailId());
		pgRequest.setUdf3(requestVo.getName());

		pgRequest.setUdf4(requestVo.getUdf4());
		pgRequest.setUdf5(requestVo.getUdf5());
		pgRequest.setUdf6(requestVo.getUdf6());
		pgRequest.setUdf7(requestVo.getUdf7());
		pgRequest.setUdf8(requestVo.getUdf8());
		pgRequest.setUdf9(requestVo.getUdf9());
		pgRequest.setUdf10(requestVo.getUdf10());

		pgRequest.setRu(requestVo.getRu());
		pgRequest.setCallbackUrl(requestVo.getCallbackUrl());
		pgRequest.setCurrency("INR");
		pgRequest.setPaymentMode("ALL");
		pgRequest.setProductType("IPG");
		pgRequest.setTxnNote("Dynamic QR");
		pgRequest.setTxnType(requestVo.getTxnType());

		ObjectMapper objectMapper = new ObjectMapper();
		String data = objectMapper.writeValueAsString(pgRequest);
		log.error("GETEPAY DATA WITHOUT ENCRYPTION : " + data);
		log.error("Required Key And Iv For Encryption: " + " KEY: " + requestVo.getIv() +" IV: "+ requestVo.getKey());
		PgEncryption encryption = new PgEncryption(requestVo.getIv(), requestVo.getKey());
		log.error("Starting Encrypting Data....");
		String encryptedData = encryption.encrypt(data);
		log.error("GETEPAY IDBI ENCRYPTED DATA: " + encryptedData);
		log.error("End Encrypting Data....");
		PgRequestWrapper pgRequestWrapper = new PgRequestWrapper();
		pgRequestWrapper.setMid(requestVo.getMid());
		pgRequestWrapper.setTerminalId(requestVo.getTerminalId());
		pgRequestWrapper.setReq(encryptedData);
		String pgRequestStr = objectMapper.writeValueAsString(pgRequestWrapper);
		log.error("GETEPAY IDBI pgRequestStr: " + pgRequestStr);
		log.error("GETEPAY IDBI pgRequestUrl: " + requestVo.getBaseUrl());
		String encryptedResponse = callApi(pgRequestStr, requestVo.getBaseUrl());
		log.error("GETEPAY IDBI encryptedResponse: " + encryptedResponse);
		PgResponse pgResponse = (PgResponse) objectMapper.readValue(encryptedResponse, PgResponse.class);
		log.error("GETEPAY IDBI pgResponse: " + pgResponse);
		
		if (pgResponse.getResponse() != null) {
			String output = encryption.getDecrytedString(pgResponse.getResponse());
			//ResponseWrapper decryptResponse = (ResponseWrapper) objectMapper.readValue(output, ResponseWrapper.class);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			ResponseWrapper decryptResponse = objectMapper.readValue(output, ResponseWrapper.class);
			return decryptResponse;
		} else {
			return null;
		}
	}

	public static String callApi(String data, String baseUrl) throws Exception {
		URL url = new URL(baseUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		String jsonInputString = data;
		

		
		Throwable var5 = null;
		Object var6 = null;

		try {
			OutputStream os = con.getOutputStream();
			
			

			try {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			} finally {
				if (os != null) {
					os.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		var5 = null;
		var6 = null;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

			try {
				StringBuilder response = new StringBuilder();
				String responseLine = null;

				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				String var10000 = response.toString();
				return var10000;
			} finally {
				if (br != null) {
					br.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public CallBackResponseObj callStatusAPI(RequestVo requestVo) throws Exception {
		PgStatusRequest psr = new PgStatusRequest();
		psr.setMid(requestVo.getMid());
		psr.setPaymentId(requestVo.getMerchantTransactionId());
		psr.setReferenceNo("");
		psr.setTerminalId(requestVo.getTerminalId());
		ObjectMapper objectMapper = new ObjectMapper();
		String data = objectMapper.writeValueAsString(psr);
		PgEncryption encryption = new PgEncryption(requestVo.getIv(), requestVo.getKey());
		String encryptedData = encryption.encrypt(data);
		PgRequestWrapper pgRequestWrapper = new PgRequestWrapper();
		pgRequestWrapper.setMid(requestVo.getMid());
		pgRequestWrapper.setTerminalId(requestVo.getTerminalId());
		pgRequestWrapper.setReq(encryptedData);
		String pgRequestStr = objectMapper.writeValueAsString(pgRequestWrapper);
		Log.error("Online Sync Request Body: " + pgRequestStr );
		Log.error("Online Sync Api Url : " + requestVo.getBaseUrl());
		String encryptedResponse = callApi(pgRequestStr, requestVo.getBaseUrl());
		PgResponse pgResponse = (PgResponse) objectMapper.readValue(encryptedResponse, PgResponse.class);
		CallBackResponseObj callObj = new CallBackResponseObj();
		if (pgResponse != null
				&& StringUtils.equalsIgnoreCase(pgResponse.getStatus(), MainetConstants.PAYMENT_STATUS.SUCCESS)) {
			callObj.setStatus(MainetConstants.PAYMENT_STATUS.SUCCESS);
			return callObj;
		} else if (pgResponse.getStatus() != null) {
			callObj.setStatus(pgResponse.getStatus());
			callObj.setStatusMsg(pgResponse.getMessage());
			return callObj;
		}
		return callObj;}
	public static void main(String args[]) {
		PaymentApi papi=new PaymentApi();
		try {
			System.out.println(papi.callStatusAPI(new RequestVo()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
