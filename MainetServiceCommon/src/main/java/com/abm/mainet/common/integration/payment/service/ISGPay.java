package com.abm.mainet.common.integration.payment.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.payment.dto.PaymentRequestDTO;
import com.abm.mainet.common.integration.payment.entity.PGBankParameter;
import com.abm.mainet.common.utility.CallBackResponseObj;
import com.abm.mainet.common.utility.Utility;

public class ISGPay implements PaymentStrategy {

	protected Logger LOG = Logger.getLogger(ISGPay.class);

	private static final String ALGO = "AES";

	private static Key generateKey(byte[] keyByte) {
		Key key = new SecretKeySpec(keyByte, ALGO);
		return key;
	}

	public static String encrypt(String sDataToEncrypt, String sEncryptionKey) {

		String encryptedValue = null;

		try {

			System.out.println("Encrypting data...");

			byte[] keyByte = sEncryptionKey.getBytes();
			Key key = generateKey(keyByte);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte encVal[] = c.doFinal(sDataToEncrypt.getBytes());
			byte encryptedByteValue[] = Base64.getEncoder().encode(encVal); // new Base64().encode(encVal);
			encryptedValue = new String(encryptedByteValue);

			System.out.println(" Encrypted value " + encryptedValue);

		} catch (Exception ex) {

			System.out.println("Exception while encrypting Data -> [" + ex + "]");
			ex.printStackTrace();

		}

		return encryptedValue;

	}

	public static String decrypt(String sDataToDecrypt, String sDecryptionKey) {

		String decryptedValue = null;

		try {

			byte[] keyByte = sDecryptionKey.getBytes();
			Key key = generateKey(keyByte);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte decryptedByteValue[] = Base64.getDecoder().decode(sDataToDecrypt.getBytes());// new
																								// Base64().decode(sDataToDecrypt.getBytes());
			byte decValue[] = c.doFinal(decryptedByteValue);
			decryptedValue = new String(decValue);

		} catch (Exception ex) {

			System.out.println("Exception while decrypting Data -> [" + ex + "]");
			ex.printStackTrace();

		}

		return decryptedValue;

	}

	@Override
	public PaymentRequestDTO generatePaymentRequest(PaymentRequestDTO paymentRequestVO) throws FrameworkException {
		JSONObject orderRequest = new JSONObject();
		if (StringUtils.isNotBlank(paymentRequestVO.getKey())) {
			BigDecimal amount = paymentRequestVO.getDueAmt().multiply(new BigDecimal(100));
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.Amount, amount);
			orderRequest.put(MainetConstants.BankParam.BankId, paymentRequestVO.getBankIdStr());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.Currency, paymentRequestVO.getTrnCurrency());
			orderRequest.put(MainetConstants.BankParam.MCC, paymentRequestVO.getMcc());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.MerchantId, paymentRequestVO.getMerchantId());
			orderRequest.put(MainetConstants.BankParam.PassCode, paymentRequestVO.getPassword());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.ReturnURL, paymentRequestVO.getResponseUrl());
			orderRequest.put(MainetConstants.BankParam.TerminalId, paymentRequestVO.getTerminalId());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.TxnRefNo, paymentRequestVO.getTxnid());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.TxnType, paymentRequestVO.getRequestType());
			orderRequest.put("FirstName", paymentRequestVO.getApplicantName());
			orderRequest.put("Email", paymentRequestVO.getEmail());
			orderRequest.put("Phone", paymentRequestVO.getMobNo());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.UDF01, paymentRequestVO.getServiceId());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.UDF02, paymentRequestVO.getApplicationId());
			orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.UDF05, paymentRequestVO.getUdf5());
			String flatNo=MainetConstants.BLANK; 
			if(StringUtils.isNotBlank(paymentRequestVO.getFlatNo())) {
			orderRequest.put("UDF06", paymentRequestVO.getFlatNo());
			flatNo="::UDF06||"+paymentRequestVO.getFlatNo();
			}
			
			
			String sha256Key = Utility.passWordInHasH(orderRequest.toString());
			paymentRequestVO.setKey(paymentRequestVO.getKey());
			String request = "Amount||" + amount + "::BankId||" + paymentRequestVO.getBankIdStr() + "::Currency||"
					+ paymentRequestVO.getTrnCurrency() + "::MCC||" + paymentRequestVO.getMcc() + "::MerchantId||"
					+ paymentRequestVO.getMerchantId() + "::PassCode||" + paymentRequestVO.getPassword()
					+ "::ReturnURL||" + paymentRequestVO.getResponseUrl() + "::TerminalId||"
					+ paymentRequestVO.getTerminalId() + "::TxnRefNo||" + paymentRequestVO.getTxnid() + "::TxnType||"
					+ paymentRequestVO.getRequestType() + "::FirstName||"+paymentRequestVO.getApplicantName()+"::Email||"+paymentRequestVO.getEmail()+"::Phone||"+paymentRequestVO.getMobNo()+"::UDF01||" + paymentRequestVO.getServiceId() + "::UDF02||"
					+ paymentRequestVO.getApplicationId() + "::UDF05||"+paymentRequestVO.getUdf5()+flatNo+"::SecureHash||" + sha256Key;
			LOG.info("Request Data>>  " + request);
			String encData = encrypt(request, paymentRequestVO.getKey());
			paymentRequestVO.setPayRequestMsg(encData);
		} else {
			LOG.error("Working key is not available for ISGPay request");
			throw new FrameworkException("Working key is not available for ISGPay response");
		}
		return paymentRequestVO;
	}

	@Override
	public Map<String, String> generatePaymentResponse(String responseString, Map<String, String> responseMap)
			throws FrameworkException {
		if (responseMap.containsKey(MainetConstants.REQUIRED_PG_PARAM.KEY)) {
			String workingKey = responseMap.get(MainetConstants.REQUIRED_PG_PARAM.KEY);
			String decVal = decrypt(responseMap.get("EncData"), workingKey);
			LOG.info("Response Map >>> " + decVal);
			String[] strArr = decVal.split("::");
			Map<String, String> map = new HashMap<String, String>();
			for (String str : strArr) {
				String[] arr = str.split("\\|\\|");
				map.put(arr[0], arr[1]);
			}
			if (MapUtils.isNotEmpty(map)) {
				if (map.get("ResponseCode") != null && map.get("ResponseCode").equals(MainetConstants.ZERO_ZERO)) {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.SUCCESS);
				} else {
					responseMap.put(MainetConstants.REQUIRED_PG_PARAM.STATUS, MainetConstants.PAYU_STATUS.FAIL);
				}
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.ERR_STR, map.get("Message"));
				responseMap.put(MainetConstants.BankParam.TXN_ID, map.get("TxnRefNo"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.AMT,
						responseMap.get(MainetConstants.REQUIRED_PG_PARAM.SESSION_AMT));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.NET_AMT, map.get(MainetConstants.REQUIRED_PG_PARAM.Amount));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.BNK_REF_NO, map.get("pgTxnId"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAYDATE, Utility.dateToString(new Date()));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.TRANS_ID, map.get("RetRefNo"));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF3, MainetConstants.RAZORYPAY_PARAM.UDF3_VALUE);
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF1, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF01));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF2, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF02));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.UDF5, map.get(MainetConstants.REQUIRED_PG_PARAM.UDF05));
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.PAY_SRC, MainetConstants.PG_SHORTNAME.ISGPAY);
				responseMap.put(MainetConstants.REQUIRED_PG_PARAM.MODE,  map.get("PaymentOption"));
			}
		} else {

			LOG.error("Working key is not available for ISGPay response");
			throw new FrameworkException("Working key is not available for ISGPay response");

		}

		return responseMap;

	}

	public CallBackResponseObj getISGPaymentStatus(String url, long tranCmId, String bankId, String merchantId,
			String terminalId, String passCode, String txnType, String mcc) {
		CallBackResponseObj responseObj = new CallBackResponseObj();
		JSONObject orderRequest = new JSONObject();
		orderRequest.put(MainetConstants.BankParam.BankId, bankId);
		orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.MerchantId, merchantId);
		orderRequest.put(MainetConstants.BankParam.TerminalId, terminalId);
		orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.TxnRefNo, tranCmId);
		orderRequest.put(MainetConstants.BankParam.PassCode, passCode);
		orderRequest.put(MainetConstants.REQUIRED_PG_PARAM.TxnType, txnType);
		orderRequest.put(MainetConstants.BankParam.MCC, mcc);
		LOG.info("Order Request " + orderRequest);
		String sha256Key = Utility.passWordInHasH(orderRequest.toString());
		System.out.println("sha256Key  " + sha256Key);

		try {
			// String url = "https://sandbox.isgpay.com/ISGPay-Genius/Status";
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			// add header
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair(MainetConstants.BankParam.BankId, bankId));
			urlParameters.add(new BasicNameValuePair(MainetConstants.REQUIRED_PG_PARAM.MerchantId, merchantId));
			urlParameters.add(new BasicNameValuePair(MainetConstants.BankParam.TerminalId, terminalId));
			urlParameters.add(new BasicNameValuePair(MainetConstants.REQUIRED_PG_PARAM.TxnRefNo, String.valueOf(tranCmId)));
			urlParameters.add(new BasicNameValuePair(MainetConstants.BankParam.PassCode, passCode));
			urlParameters.add(new BasicNameValuePair(MainetConstants.REQUIRED_PG_PARAM.TxnType, txnType));
			urlParameters.add(new BasicNameValuePair(MainetConstants.BankParam.MCC, mcc));
			urlParameters.add(new BasicNameValuePair("SecureHash", sha256Key));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			Map<String, String> map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(result.toString())) {
				String[] strArr = result.toString().split("&");
				for (String str : strArr) {
					String[] arr = str.split("=");
					if (arr.length > 1) {
						map.put(arr[0], arr[1]);
					}
				}
			}
			responseObj.setOrder_no(map.get(MainetConstants.REQUIRED_PG_PARAM.TxnRefNo));
			responseObj.setOrder_amt(map.get(MainetConstants.REQUIRED_PG_PARAM.Amount));
			responseObj.setReference_no(map.get("RetRefNo"));
			responseObj.setStatus(map.get("ResponseCode"));
			responseObj.setStatusMsg(map.get("Message"));
			LOG.info("Isg Pay response status "+responseObj);
		} catch (Exception e) {
			LOG.error("Excetion at the time of fetching Status of "+tranCmId,e);

		}
		return responseObj;
	}

}
