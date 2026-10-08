package com.abm.mainet.account.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.abm.mainet.account.rest.dto.ReceiptResponseDto;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.dto.ICICIOneWindowTransactionDTO;
import com.abm.mainet.common.integration.acccount.dto.TbServiceReceiptMasBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptFeesDetBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptModesDetBean;
import com.abm.mainet.common.integration.acccount.repository.SecondaryheadMasterJpaRepository;
import com.abm.mainet.common.integration.acccount.service.AccountFieldMasterService;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.service.OrganisationService;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.Utility;

@Service
public class ICICIOneWindowServiceImpl implements ICICIOneWindowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ICICIOneWindowServiceImpl.class);

	private static final String ICICI_SECRET_KEY = "935ed6b64de647e287dce76c2b112e79";

	@Autowired
	private AccountReceiptEntryService accountReceiptEntryService;

	@Autowired
	private IOrganisationService organisationService;

	@Autowired
	private DepartmentService departmentService;

	@Resource
	private AccountFieldMasterService tbAcFieldMasterService;

	@Autowired
	private SecondaryheadMasterJpaRepository accountHeadSecondaryAccountCodeMasterRepository;

	@Override
	public ResponseEntity<?> processCallback(ICICIOneWindowTransactionDTO transactionDTO, HttpServletRequest request) {
		try {
			TbServiceReceiptMasBean responseEntity = null;
			List<String> validData = validateExternalICICIRequestInput(transactionDTO);
			if (CollectionUtils.isNotEmpty(validData)) {
				return ResponseEntity.ok(ReceiptResponseDto.getResponse("", "secureHash is mandatory", validData,
						HttpStatus.BAD_REQUEST));
			}
			String generatedSecureHash = generateSecureHash(transactionDTO, ICICI_SECRET_KEY);
			LOGGER.info("Received ICICI secureHash = {}", transactionDTO.getSecureHash());
			LOGGER.info("Generated ICICI secureHash = {}", generatedSecureHash);
			if (!StringUtils.equalsIgnoreCase(generatedSecureHash, transactionDTO.getSecureHash())) {

				LOGGER.error("ICICI callback secureHash validation failed");

				return ResponseEntity.ok(ReceiptResponseDto.getResponse("", "Secure Hash is Wrong",
						Arrays.asList("Invalid secureHash"), HttpStatus.BAD_REQUEST));
			}
			LOGGER.info("ICICI transaction validated successfully");
			Organisation org = organisationService.getActiveOrgByUlbShortCode("KDMC");
			if (org == null) {
				LOGGER.error("ICICI callback: KDMC organisation not found");
				return ResponseEntity.ok(ReceiptResponseDto.getResponse("", "Organisation not found",
						Arrays.asList("KDMC organisation not found"), HttpStatus.BAD_REQUEST));
			}
			TbServiceReceiptMasBean receiptBean = new TbServiceReceiptMasBean();
			Long fieldId = tbAcFieldMasterService.getFieldIdByFieldCompositCode("01-01", org.getOrgid());
			receiptBean.setRecCategoryTypeId(CommonMasterUtility.lookUpIdByLookUpCodeAndPrefix("M",
					MainetConstants.BILL_MASTER_COMMON.PREFIX_CODE_RV, org.getOrgid()));
			receiptBean.setRecCategoryType(MainetConstants.FlagM);
			receiptBean.setOrgId(org.getOrgid());
			Date rcptDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(transactionDTO.getTxnDate());
			Date currentDate = new Date();
			receiptBean.setRmDate(rcptDate);
			receiptBean.setCreatedDate(currentDate);
			receiptBean.setCreatedBy(1L);
			receiptBean.setLangId(Utility.getDefaultLanguageId(org));
			receiptBean.setRmAmount(transactionDTO.getAmount());
			receiptBean.setRmReceivedfrom(
					StringUtils.isBlank(transactionDTO.getName()) ? "ICICI" : transactionDTO.getName());
			receiptBean.setRmNarration("ICICI One Window Payment");
			receiptBean.setTransactionDate(new SimpleDateFormat("dd/MM/yyyy").format(rcptDate));
			receiptBean.setDpDeptId(departmentService.getDepartmentIdByDeptCode("AC"));
			receiptBean.setLgIpMac(Utility.getClientIpAddress(request));
			receiptBean.setAdditionalRefNo(transactionDTO.getMerchantTxnNo());
			receiptBean.setRmV3(transactionDTO.getAddlParam1());
			TbSrcptFeesDetBean feeDetail = new TbSrcptFeesDetBean();
			
			BigDecimal amount = new BigDecimal(transactionDTO.getAmount().toString());
			feeDetail.setRfFeeamount(amount);
			feeDetail.setOrgid(org.getOrgid());
			feeDetail.setCreatedBy(1L);
			feeDetail.setCreatedDate(currentDate);
			feeDetail.setLangId((long) Utility.getDefaultLanguageId(org));
			feeDetail.setSacHeadId(getSacHeadIdByAdditionalInfo(transactionDTO.getAddlParam1(), org.getOrgid()));
			if (receiptBean.getReceiptFeeDetail() == null) {

				receiptBean.setReceiptFeeDetail(new ArrayList<TbSrcptFeesDetBean>());
			}
			receiptBean.getReceiptFeeDetail().add(feeDetail);
			TbSrcptModesDetBean modeDetail = new TbSrcptModesDetBean();
			Long lookupId = CommonMasterUtility.lookUpIdByLookUpCodeAndPrefix(PrefixConstants.PAY_PREFIX.WEB,
					PrefixConstants.PAY_PREFIX.PREFIX_VALUE, org.getOrgid());
			if (lookupId == null) {
				LOGGER.error("ICICI callback: WEB payment mode lookup not found");
				return ResponseEntity.ok(ReceiptResponseDto.getResponse("", "Payment mode not configured",
						Arrays.asList("WEB payment mode lookup not found"), HttpStatus.BAD_REQUEST));
			}
			modeDetail.setCpdFeemode(lookupId);
			modeDetail.setRdAmount(amount);
			modeDetail.setOrgid(org.getOrgid());
			receiptBean.setReceiptModeDetailList(modeDetail);
			responseEntity=accountReceiptEntryService.create(receiptBean, fieldId);
			LOGGER.info("ICICI receipt created successfully");
			return ResponseEntity.ok(ReceiptResponseDto.getResponse(responseEntity.getRmRcptno().toString(), "Receipt entry done successfully",
					Arrays.asList(""), HttpStatus.OK));
		} catch (Exception e) {
			LOGGER.error("Error while processing ICICI callback", e);
			return ResponseEntity.ok(ReceiptResponseDto.getResponse("", "Transaction processing failed",
					Arrays.asList("Transaction processing failed"), HttpStatus.BAD_REQUEST));
		}
	}

	private List<String> validateExternalICICIRequestInput(ICICIOneWindowTransactionDTO transactionDTO) {
		List<String> validateResponse = new ArrayList<String>();
		if (StringUtils.isBlank(transactionDTO.getSecureHash())) {
			validateResponse.add("secureHash is mandatory");
		}
		if (transactionDTO.getAmount() == null) {
			validateResponse.add("amount is mandatory");
		} else if (StringUtils.isBlank(transactionDTO.getAmount())
				|| new BigDecimal(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) <= 0) {
			validateResponse.add("amount must be greater than zero");
		}
		if (StringUtils.isBlank(transactionDTO.getAddlParam1())) {
			validateResponse.add("addlParam1 is mandatory");
		}
		if (StringUtils.isBlank(transactionDTO.getAddlParam2())) {
			validateResponse.add("addlParam2 is mandatory");
		}
		if (StringUtils.isBlank(transactionDTO.getTxnDate())) {
	        validateResponse.add("txnDate is mandatory");
	    }
		return validateResponse;
	}

	/**
	 * Generate ICICI HMAC-SHA256 secure hash.
	 */
	private String generateSecureHash(ICICIOneWindowTransactionDTO transactionDTO, String keyString) {
		StringBuilder msg = new StringBuilder();
		if (StringUtils.isNotBlank(transactionDTO.getName())) {
			msg.append(transactionDTO.getName());
		}
		if (StringUtils.isNotBlank(transactionDTO.getAddlParam1())) {
			msg.append(transactionDTO.getAddlParam1());
		}
		if (StringUtils.isNotBlank(transactionDTO.getAddlParam2())) {
			msg.append(transactionDTO.getAddlParam2());
		}
		try {
			SecretKeySpec key = new SecretKeySpec(keyString.getBytes("UTF-8"), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] bytes = mac.doFinal(msg.toString().getBytes("ASCII"));
			StringBuilder hash = new StringBuilder();
			for (byte b : bytes) {
				String hex = Integer.toHexString(0xFF & b);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			return hash.toString();
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8/ASCII encoding not supported", e);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException("Invalid ICICI HMAC secret key", e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("HmacSHA256 algorithm not available", e);
		}
	}

	private Long getSacHeadIdByAdditionalInfo(String additionalInfo, Long orgId) {
		if (StringUtils.isBlank(additionalInfo)) {
			return null;
		}
		String headCode = null;
		if ("drainage_noc_jal_cc".equalsIgnoreCase(additionalInfo)
				|| "drainage_noc_jal_oc".equalsIgnoreCase(additionalInfo)) {

			headCode = "5052 - 4122008 - 0001 - CRI020101-DRAINGAE DEPT OTHER RECOVERY";
		} else if ("drainage_noc_mal_cc".equalsIgnoreCase(additionalInfo)
				|| "drainage_noc_mal_oc".equalsIgnoreCase(additionalInfo)) {

			headCode = "5052 - 1404007 - 0001 - CRI030103-DRAINAGE CONNECTION FEES";
		} else if ("fire_final_noc".equalsIgnoreCase(additionalInfo)
				|| "fire_noc_provisional_new".equalsIgnoreCase(additionalInfo)
				|| "stepwise_fire_cc".equalsIgnoreCase(additionalInfo)
				|| "stepwise_fire_oc".equalsIgnoreCase(additionalInfo)) {

			headCode = "5053 - 1407007 - 0001 - ARI030203-CAPITATION & SUPERVISION CHARGES FOR TOWER BUILDINGS CERTIFICATES";
		} else if ("water_technical_noc_application_form".equalsIgnoreCase(additionalInfo)
				|| "water_technical_noc_application_form2".equalsIgnoreCase(additionalInfo)) {

			headCode = "5051 - 1404006 - 0001 - CRI010103-NEW WATER CONNECTION";
		}
		return accountHeadSecondaryAccountCodeMasterRepository.findSacHeadIdBySacHeadCode(headCode, orgId);
	}
}