package com.abm.mainet.common.integration.dms.rest.ui.controller;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.FinancialYear;
import com.abm.mainet.common.integration.dms.domain.BlockChainCertificateEntity;
import com.abm.mainet.common.integration.dms.domain.SingleWindowIntegrationEntity;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.dto.SingleWindowIntegrationRequestDTO;
import com.abm.mainet.common.integration.dms.dto.SingleWindowIntegrationResponseDTO;
import com.abm.mainet.common.integration.dms.repository.BlockChainRepository;
import com.abm.mainet.common.integration.dms.repository.SingleWindowIntegrationRepository;
import com.abm.mainet.common.integration.dms.service.BlockChainCertificateServiceImpl;
import com.abm.mainet.common.integration.dms.service.IBlockChainCertificateService;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.integration.property.dto.PropertyDetailDto;
import com.abm.mainet.common.integration.property.dto.PropertyInputDto;
import com.abm.mainet.common.service.IFinancialYearService;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.spi.Element;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author Pritam.Chaphekar
 *
 */
@RestController
@RequestMapping("/singleWindow/integration")
public class SingleWindowIntegrationController {

	@Autowired
	private SingleWindowIntegrationRepository singleWindowIntegrationRepository;
	
	@Autowired
    private IFinancialYearService financialYearService;
	
	private static final Logger LOGGER = Logger.getLogger(SingleWindowIntegrationController.class);

	private static final String API_KEY = "KDMC_802790";

	private static final String paymentForWater = ApplicationSession.getInstance()
			.getMessage("single.window.applyFor.waterPayment");

	private static final String applyPropertyNoDuesCertificate = ApplicationSession.getInstance()
			.getMessage("single.window.applyFor.propertyNoDues.noc");

	private static final String applyWaterNoDuesCertificate = ApplicationSession.getInstance()
			.getMessage("single.window.applyFor.waterNoDues.noc");
	
	
	private static final String paymentForProperty = ApplicationSession.getInstance()
			.getMessage("single.window.applyForproperty.paymentUrl");

	@Value("${upload.physicalPath}")
	private String filenetPath;

	@RequestMapping(value = "/getPropertyAndWaterData", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<SingleWindowIntegrationResponseDTO>> getDetails(
			@RequestHeader(value = "X-API-KEY", required = false) String apiKey,
			@RequestBody SingleWindowIntegrationRequestDTO request, HttpServletRequest servletRequest) {
		
		boolean noduesFlag =false;

		long startTime = System.nanoTime();
		LOGGER.info("Request received for getPropertyAndWaterData - value: {}, propertyOrWaterConnectionNo: {}"
				+ request == null ? "null"
						: request.getValue() + request == null ? "null" : request.getPropertyOrWaterConnectionNo());

		// Validate API key
		if (StringUtils.isBlank(apiKey) || !API_KEY.equals(apiKey)) {
			LOGGER.warn("Invalid or missing API key from {}"+ servletRequest.getRemoteAddr());
			SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
			resp.setStatus("ERROR");
			resp.setErrorMessage("Invalid or missing API key");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(resp));
		}

		// Basic payload validation
		if (request == null || StringUtils.isBlank(request.getPropertyOrWaterConnectionNo())
				|| StringUtils.isBlank(request.getValue()) || StringUtils.isBlank(request.getDateInTimestamp())
				|| StringUtils.isBlank(request.getUserId()) || StringUtils.isBlank(request.getMacId())) {

			SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
			resp.setStatus("ERROR");
			resp.setErrorMessage(
					"Missing mandatory request fields (propertyOrWaterConnectionNo, value, userId, dateInTimestamp, macId)");
			LOGGER.warn("Bad request payload");
			return ResponseEntity.badRequest().body(Collections.singletonList(resp));
		}

		List<SingleWindowIntegrationResponseDTO> responseList = new ArrayList<>();
		boolean anySuccess = false;
		Date sysDate = new Date();

		try {
			String value = request.getValue().trim();

			// ---------- PROPERTY (PNC) - WHOLE ----------
			if ("PNC".equalsIgnoreCase(value) && StringUtils.isBlank(request.getFlatNo())) {
				LOGGER.info("Processing PNC for whole property: {}" + request.getPropertyOrWaterConnectionNo());
				List<Object[]> wholePropData = singleWindowIntegrationRepository
						.getWholePropertyDataForDues(request.getPropertyOrWaterConnectionNo());

				if (CollectionUtils.isEmpty(wholePropData)) {
					return badRecordNotFound();
				}

				// Check aggregated dues value from first row (index 3 used in your original
				// code)
				boolean halfAmt=false;
				Object[] firstRow = wholePropData.get(0);
				String duesStr = getSafeString(firstRow, 3);

				if (!safeEqualsZero(duesStr)) {
					 halfAmt = checkOutstandingStatus(request.getPropertyOrWaterConnectionNo(),
							null);
				}
				
				
				String wholePropFlag=null;

				// If zero dues, check flag to decide whether to generate PDF
				if (safeEqualsZero(duesStr)) {
					
					
					 wholePropFlag = singleWindowIntegrationRepository
							.getWholePropertyFlagCheck(request.getPropertyOrWaterConnectionNo());
					

					if (StringUtils.equalsIgnoreCase(wholePropFlag, "Y")) {
						// Generate PDF once for whole property and return single response (preserve
						// your behavior)
						byte[] pdfBytes = generateWholePropertyNoDuesCertificate(
								request.getPropertyOrWaterConnectionNo());
						String pdfBase64 = (pdfBytes != null && pdfBytes.length > 0)
								? Base64.getEncoder().encodeToString(pdfBytes)
								: "";

						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate(pdfBase64);
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");

						responseList.add(resp);
						if(pdfBytes != null && pdfBytes.length > 0) {
							noduesFlag=true;
						}
						anySuccess = true;
					} else {
						// Return rows with links (no PDF generation)
						for (Object[] row : wholePropData) {
							SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
							resp.setPropertyNumber(getSafeString(row, 0));
							resp.setFlatNumber(getSafeString(row, 1));
							resp.setOwnerName(getSafeString(row, 2));
							resp.setAddress(getSafeString(row, 4));
							resp.setTypeOfProperty(getSafeString(row, 5));
							resp.setLastestBillGenerationDate(getSafeString(row, 6));
							resp.setCurrentAmountToBePaid(getSafeString(row, 3));
							resp.setLinkForPayment("");
							resp.setLinkForApplyNoc(
									applyPropertyNoDuesCertificate + "?noDuePropertyNo=" + getSafeString(row, 0));
							resp.setStatus("SUCCESS");
							responseList.add(resp);
							anySuccess = true;
							LOGGER.info("Whole property row processed: {}" + resp.getPropertyNumber());
						}
					}
				} else if (halfAmt) {

					wholePropFlag = singleWindowIntegrationRepository
							.getWholePropertyFlagCheck(request.getPropertyOrWaterConnectionNo());

					if (StringUtils.equalsIgnoreCase(wholePropFlag, "Y")) {
						// Generate PDF once for whole property and return single response (preserve
						// your behavior)
						byte[] pdfBytes = generateWholePropertyNoDuesCertificate(
								request.getPropertyOrWaterConnectionNo());
						String pdfBase64 = (pdfBytes != null && pdfBytes.length > 0)
								? Base64.getEncoder().encodeToString(pdfBytes)
								: "";

						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate(pdfBase64);
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");

						responseList.add(resp);
						if (pdfBytes != null && pdfBytes.length > 0) {
							noduesFlag = true;
						}
						anySuccess = true;
					} else {
						// Return rows with links (no PDF generation)
						for (Object[] row : wholePropData) {
							SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
							resp.setPropertyNumber(getSafeString(row, 0));
							resp.setFlatNumber(getSafeString(row, 1));
							resp.setOwnerName(getSafeString(row, 2));
							resp.setAddress(getSafeString(row, 4));
							resp.setTypeOfProperty(getSafeString(row, 5));
							resp.setLastestBillGenerationDate(getSafeString(row, 6));
							resp.setCurrentAmountToBePaid(getSafeString(row, 3));
							resp.setLinkForPayment("");
							resp.setLinkForApplyNoc(
									applyPropertyNoDuesCertificate + "?noDuePropertyNo=" + getSafeString(row, 0));
							resp.setStatus("SUCCESS");
							responseList.add(resp);
							anySuccess = true;
							LOGGER.info("Whole property row processed: {}" + resp.getPropertyNumber());
						}

					}
				} else {
				
					// Dues exist - return rows with payment links
					for (Object[] row : wholePropData) {
						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(row, 0));
						resp.setFlatNumber(getSafeString(row, 1));
						resp.setOwnerName(getSafeString(row, 2));
						resp.setAddress(getSafeString(row, 4));
						resp.setTypeOfProperty(getSafeString(row, 5));
						resp.setLastestBillGenerationDate(getSafeString(row, 6));
						resp.setCurrentAmountToBePaid(getSafeString(row, 3));
						resp.setLinkForPayment(paymentForProperty);
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						anySuccess = true;
						LOGGER.error("Whole property with dues processed: {}" + resp.getPropertyNumber());
					}
				}

				// ---------- PROPERTY (PNC) - INDIVIDUAL ----------
			} else if ("PNC".equalsIgnoreCase(value) && StringUtils.isNotBlank(request.getFlatNo())) {
				LOGGER.error("Processing PNC for individual property: {} , flat: {}"
						+ request.getPropertyOrWaterConnectionNo() + request.getFlatNo());
				List<Object[]> individualPropWiseData = singleWindowIntegrationRepository
						.getIndividualPropertyDataForDues(request.getPropertyOrWaterConnectionNo(),
								request.getFlatNo());

				if (CollectionUtils.isEmpty(individualPropWiseData)) {
					return badRecordNotFound();
				}

				boolean individualHalfAmt = false;
				Object[] firstRow = individualPropWiseData.get(0);
				String duesStr = getSafeString(firstRow, 3);
				
				if (!safeEqualsZero(duesStr)) {
					individualHalfAmt = checkOutstandingStatus(request.getPropertyOrWaterConnectionNo(), request.getFlatNo());
				}

				if (safeEqualsZero(duesStr)) {
					String individualPropFlag = singleWindowIntegrationRepository.getIndividualPropertyFlagCheck(
							request.getPropertyOrWaterConnectionNo(), request.getFlatNo());

					if (StringUtils.equalsIgnoreCase(individualPropFlag, "Y")) {
						
						byte[] pdfBytes = generatePdfIndividualPropertyNoDuesCertificate(getSafeString(firstRow, 0),
								getSafeString(firstRow, 1));
						String pdfBase64 = (pdfBytes != null && pdfBytes.length > 0)
								? Base64.getEncoder().encodeToString(pdfBytes)
								: "";

						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate(pdfBase64);
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						if(pdfBytes != null && pdfBytes.length > 0) {
							noduesFlag=true;
						}
						anySuccess = true;
					} else {
						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate("");
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc(
								applyPropertyNoDuesCertificate + "?noDuePropertyNo=" + getSafeString(firstRow, 0));
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						anySuccess = true;
					}
				} else if (individualHalfAmt) {

					String individualPropFlag = singleWindowIntegrationRepository.getIndividualPropertyFlagCheck(
							request.getPropertyOrWaterConnectionNo(), request.getFlatNo());

					if (StringUtils.equalsIgnoreCase(individualPropFlag, "Y")) {

						byte[] pdfBytes = generatePdfIndividualPropertyNoDuesCertificate(getSafeString(firstRow, 0),
								getSafeString(firstRow, 1));
						String pdfBase64 = (pdfBytes != null && pdfBytes.length > 0)
								? Base64.getEncoder().encodeToString(pdfBytes)
								: "";

						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate(pdfBase64);
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						if (pdfBytes != null && pdfBytes.length > 0) {
							noduesFlag = true;
						}
						anySuccess = true;
					} else {
						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(firstRow, 0));
						resp.setFlatNumber(getSafeString(firstRow, 1));
						resp.setOwnerName(getSafeString(firstRow, 2));
						resp.setAddress(getSafeString(firstRow, 4));
						resp.setTypeOfProperty(getSafeString(firstRow, 5));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 6));
						resp.setCurrentAmountToBePaid(duesStr);
						resp.setPdfOfNoDuesCertificate("");
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc(
								applyPropertyNoDuesCertificate + "?noDuePropertyNo=" + getSafeString(firstRow, 0));
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						anySuccess = true;
					}

				} else {
					// Dues exist - return rows (though normally it's single row for a flat)
					for (Object[] row : individualPropWiseData) {
						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setPropertyNumber(getSafeString(row, 0));
						resp.setFlatNumber(getSafeString(row, 1));
						resp.setOwnerName(getSafeString(row, 2));
						resp.setAddress(getSafeString(row, 4));
						resp.setTypeOfProperty(getSafeString(row, 5));
						resp.setLastestBillGenerationDate(getSafeString(row, 6));
						resp.setCurrentAmountToBePaid(getSafeString(row, 3));
						resp.setLinkForPayment(paymentForProperty);
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						anySuccess = true;
						LOGGER.error("Individual property with dues processed: {}" + resp.getPropertyNumber());
					}
				}

				// ---------- WATER (WNC) ----------
			} else if ("WNC".equalsIgnoreCase(value)) {
				LOGGER.error("Processing WNC for connection: {}" + request.getPropertyOrWaterConnectionNo());
				List<Object[]> waterDataList = singleWindowIntegrationRepository
						.getWaterNoDuesData(request.getPropertyOrWaterConnectionNo());

				if (CollectionUtils.isEmpty(waterDataList)) {
					return badRecordNotFound();
				}

				Object[] firstRow = waterDataList.get(0);
				String waterDues = getSafeString(firstRow, 6); // your original used index 6

				if (safeEqualsZero(waterDues)) {
					String waterFlag = singleWindowIntegrationRepository
							.checkWaterNoDuesPaymentStatus(getSafeString(firstRow, 0));
					if (StringUtils.equalsIgnoreCase(waterFlag, "Y")) {
						 
						byte[] pdfBytes = generateWaterNoDuesCertificate(getSafeString(firstRow, 0));
						String pdfBase64 = (pdfBytes != null && pdfBytes.length > 0)
								? Base64.getEncoder().encodeToString(pdfBytes)
								: "";

						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setConnectionNumber(getSafeString(firstRow, 0));
						resp.setOwnerName(getSafeString(firstRow, 1));
						resp.setAddress(getSafeString(firstRow, 2));
						resp.setTypeOfConnection(getSafeString(firstRow, 3));
						resp.setLastestBillGenerationDate(getSafeString(firstRow, 5));
						resp.setCurrentAmountToBePaid(waterDues);
						resp.setPdfOfNoDuesCertificate(pdfBase64);
						resp.setLinkForPayment("");
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						if(pdfBytes != null && pdfBytes.length > 0) {
							noduesFlag=true;
						}
						
						anySuccess = true;
					} else {
						for (Object[] row : waterDataList) {
							SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
							resp.setConnectionNumber(getSafeString(row, 0));
							resp.setOwnerName(getSafeString(row, 1));
							resp.setAddress(getSafeString(row, 2));
							resp.setTypeOfConnection(getSafeString(row, 3));
							resp.setLastestBillGenerationDate(getSafeString(row, 5));
							resp.setCurrentAmountToBePaid(getSafeString(row, 6));
							resp.setPdfOfNoDuesCertificate("");
							resp.setLinkForPayment("");
							resp.setLinkForApplyNoc(
									applyWaterNoDuesCertificate + "?noDueConnectionNo=" + getSafeString(row, 0));
							resp.setStatus("SUCCESS");
							responseList.add(resp);
							anySuccess = true;
							LOGGER.error("Water row processed: {}" + resp.getConnectionNumber());
						}
					}
				} else {
					// Dues exist - return rows with payment link
					for (Object[] row : waterDataList) {
						SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
						resp.setConnectionNumber(getSafeString(row, 0));
						resp.setOwnerName(getSafeString(row, 1));
						resp.setAddress(getSafeString(row, 2));
						resp.setTypeOfConnection(getSafeString(row, 3));
						resp.setLastestBillGenerationDate(getSafeString(row, 5));
						resp.setCurrentAmountToBePaid(getSafeString(row, 6));
						resp.setPdfOfNoDuesCertificate("");
						resp.setLinkForPayment(paymentForWater);
						resp.setLinkForApplyNoc("");
						resp.setStatus("SUCCESS");
						responseList.add(resp);
						anySuccess = true;
					}
				}

			} else {
				SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
				resp.setStatus("ERROR");
				resp.setErrorMessage("Unsupported value parameter. Allowed: PNC, WNC");
				return ResponseEntity.badRequest().body(Collections.singletonList(resp));
			}

			// Save audit/audit-entity once for successful calls
			if (anySuccess) {
				try {
					SingleWindowIntegrationEntity audit = new SingleWindowIntegrationEntity();
					audit.setPropertyNoOrConnectionNo(request.getPropertyOrWaterConnectionNo());
					audit.setValue(request.getValue());
					audit.setSwidate(request.getDateInTimestamp());
					audit.setLmodDate(sysDate);
					audit.setUserId(request.getUserId());
					audit.setMacId(request.getMacId());
					audit.setFlatNo(request.getFlatNo());
					if(noduesFlag) {
					audit.setuDF1("Y");
					}
					singleWindowIntegrationRepository.save(audit);
				} catch (Exception e) {
					LOGGER.error("Failed to save audit entity (non-fatal): {}" + e.getMessage(), e);
					// Do not fail entire request if audit save fails
				}
			}

			long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;
			LOGGER.error("getPropertyAndWaterData processed successfully in {} ms, results: {}" + elapsedMs
					+ responseList.size());
			return ResponseEntity.ok(responseList);

		} catch (Exception ex) {
			LOGGER.error("Unexpected error in getPropertyAndWaterData: {}" + ex.getMessage(), ex);
			SingleWindowIntegrationResponseDTO err = new SingleWindowIntegrationResponseDTO();
			err.setStatus("ERROR");
			err.setErrorMessage("Internal server error. Please contact support.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(err));
		}
	}

	/**
	 * Utility method to safely extract string from Object array
	 */
	 private ResponseEntity<List<SingleWindowIntegrationResponseDTO>> badRecordNotFound() {
	        SingleWindowIntegrationResponseDTO resp = new SingleWindowIntegrationResponseDTO();
	        resp.setStatus("ERROR");
	        resp.setErrorMessage("No record found for given number and type.");
	        return ResponseEntity.badRequest().body(Collections.singletonList(resp));
	    }

	    private String getSafeString(Object[] row, int index) {
	        if (row == null || index < 0 || index >= row.length) {
	            return "";
	        }
	        Object val = row[index];
	        return val == null ? "" : String.valueOf(val).trim();
	    }

	    private boolean safeEqualsZero(String s) {
	        if (StringUtils.isBlank(s)) return false;
	        // Normalize and check numeric zero values like "0", "0.00"
	        try {
	            double d = Double.parseDouble(s.replaceAll(",", ""));
	            return Double.compare(d, 0.0) == 0;
	        } catch (NumberFormatException e) {
	            return "0".equals(s) || "0.00".equals(s);
	        }
	    }

	    public byte[] generateWaterNoDuesCertificate(String ccnNo) {
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            LOGGER.error("Method started generateWaterNoDuesCertificate.......");

	            List<Object[]> waterCertificateList = singleWindowIntegrationRepository.getWaterCertificatePrint(ccnNo);
	            if (waterCertificateList == null || waterCertificateList.isEmpty()) {
	                return new byte[0];
	            }

	            Object[] res = waterCertificateList.get(0);

	            String certificateDate = res[0] != null ? res[0].toString() : "";
	            String certificateNumber = res[1] != null ? res[1].toString() : "";
	            String ownerName = res[2] != null ? res[2].toString() : "";
	            String applicationId = res[3] != null ? res[3].toString() : "";
	            String applicationDate = res[4] != null ? res[4].toString() : "";
	            String division = res[5] != null ? res[5].toString() : "";
	            String ward = res[6] != null ? res[6].toString() : "";
	            String zone = res[7] != null ? res[7].toString() : "";
	            String connectionNumber = res[8] != null ? res[8].toString() : "";
	            String address = res[9] != null ? res[9].toString() : "";

	            // ---------------- PDF SETUP ----------------
	            PdfWriter writer = new PdfWriter(outputStream);
	            PdfDocument pdfDoc = new PdfDocument(writer);
	            pdfDoc.addNewPage();

	            Document document = new Document(pdfDoc);
	            // Reduced margins slightly to fit everything on one page
	            document.setMargins(30f, 40f, 25f, 40f);
	            document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.95f)); // Slightly tighter line spacing

	            // ---------------- FONT SETUP ----------------
	            //mangalFont.path
	            String MANGAL = ApplicationSession.getInstance().getMessage("mangalFont.path");
	            LOGGER.error("MANGAL PATH FOR WATER " + MANGAL);
	            String ARIALUNICODE = ApplicationSession.getInstance().getMessage("arialUniCode.path");
	            LOGGER.error("ARIALUNICODE PATH FOR WATER " + ARIALUNICODE);

	            PdfFont englishFont = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(ARIALUNICODE), PdfEncodings.IDENTITY_H, true);
	            PdfFont marathiFont = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(MANGAL), PdfEncodings.IDENTITY_H, true);

	            float fontSize = 11f;

	            // ---------------- PAGE BORDER ----------------
	            float borderMargin = 20f;
	            Rectangle pageSize = pdfDoc.getDefaultPageSize();
	            PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
	            canvas.setLineWidth(0.8f);
	            canvas.rectangle(
	                    pageSize.getLeft() + borderMargin,
	                    pageSize.getBottom() + borderMargin,
	                    pageSize.getWidth() - 2 * borderMargin,
	                    pageSize.getHeight() - 2 * borderMargin
	            );
	            canvas.stroke();

	            // ---------------- HEADER (Logo + Title) ----------------
	            String logoPath = filenetPath + ApplicationSession.getInstance().getMessage("leftlogo");
	            ImageData logoData = ImageDataFactory.create(logoPath);
	            Image logo = new Image(logoData).scaleAbsolute(70f, 85f);
	            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            logo.setMarginBottom(4f);
	            document.add(logo);

	            // Municipal name
	            document.add(new Paragraph("Kalyan Dombivli Municipal Corporation")
	                    .setFont(englishFont)
	                    .setFontSize(13f)
	                    .setBold()
	                    .setTextAlignment(TextAlignment.CENTER)
	                    .setMarginBottom(3f));

	            // Sub-header
	            document.add(new Paragraph("Water Department")
	                    .setFont(englishFont)
	                    .setFontSize(12f)
	                    .setBold()
	                    .setTextAlignment(TextAlignment.CENTER)
	                    .setMarginBottom(3f));

	            document.add(new Paragraph("Water No Due Certificate")
	                    .setFont(englishFont)
	                    .setFontSize(12f)
	                    .setBold()
	                    .setTextAlignment(TextAlignment.CENTER)
	                    .setMarginBottom(8f));

	            // ---------------- CERTIFICATE DETAILS ----------------
	            document.add(new Paragraph("Date : " + certificateDate)
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph("Certificate No. : " + certificateNumber)
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(4f));

	            // ---------------- SUBJECT & REFERENCE ----------------
	            document.add(new Paragraph("To,")
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(2f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("Name: " + ownerName)
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(2f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("Subject: Water No Due Certificate")
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(2f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("Reference:")
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(2f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("1) Application No. " + applicationId + " Dated " + applicationDate)
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(1f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("2) Water Connection Inspection")
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(1f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            document.add(new Paragraph("Division: " + division + "     Ward: " + ward + "     Zone: " + zone)
	                    .setFont(englishFont).setFontSize(fontSize)
	                    .setMarginBottom(6f)
	                    .setTextAlignment(TextAlignment.LEFT));

	            // ---------------- BODY ----------------
	            String certBody = "Sir/Madam,\n\n" +
	                    "        This is to certify that the water connection No. " + connectionNumber +
	                    " located at " + address + " stands in the name of " + ownerName + ". " +
	                    "The water tax for the said premises has been paid for the year " + certificateDate +
	                    " as per records maintained by the Municipal Corporation.";

	            document.add(new Paragraph(certBody)
	                    .setFont(englishFont)
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.JUSTIFIED)
	                    .setFirstLineIndent(30f)
	                    .setMarginBottom(6f));

	            // ---------------- SIGNATURE BLOCK ----------------
	            document.add(new Paragraph("Municipal Engineer")
	                    .setFont(englishFont)
	                    .setFontSize(fontSize)
	                    .setMarginBottom(0f)
	                    .setTextAlignment(TextAlignment.RIGHT));

	            document.add(new Paragraph("Kalyan Dombivli Municipal Corporation")
	                    .setFont(englishFont)
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(4f));

	            // ---------------- FOOTNOTE ----------------
	            document.add(new Paragraph("This is a computer-generated document, no signature required.")
	                    .setFont(englishFont)
	                    .setFontSize(10f)
	                    .setItalic()
	                    .setTextAlignment(TextAlignment.CENTER)
	                    .setMarginTop(-2f) // pull closer to the previous line
	                    .setMarginBottom(0f));

	            document.close();
	            LOGGER.info("Method ended generateWaterNoDuesCertificate.......");
	            return outputStream.toByteArray();

	        } catch (Exception e) {
	            LOGGER.error("Error generating Water No Dues Certificate PDF: ", e);
	            return new byte[0];
	        }
	    }


	/*
	 * public byte[] generatePdfIndividualPropertyNoDuesCertificate(String propNo,
	 * String flatNo) { List<Object[]> individualCertificateList =
	 * singleWindowIntegrationRepository
	 * .getIndividualPropertyCertificatePrint(propNo, flatNo);
	 * 
	 * if (individualCertificateList == null || individualCertificateList.isEmpty())
	 * { return new byte[0]; }
	 * 
	 * Object[] indiPropPdf = individualCertificateList.get(0);
	 * 
	 * String certificateNumber = indiPropPdf[0] != null ? indiPropPdf[0].toString()
	 * : ""; String appDate = indiPropPdf[1] != null ? indiPropPdf[1].toString() :
	 * ""; String tokenNo = indiPropPdf[2] != null ? indiPropPdf[2].toString() : "";
	 * String flatOwnerName = indiPropPdf[3] != null ? indiPropPdf[3].toString() :
	 * ""; String builderName = indiPropPdf[4] != null ? indiPropPdf[4].toString() :
	 * ""; String propertyNo = indiPropPdf[5] != null ? indiPropPdf[5].toString() :
	 * ""; String fltNo = indiPropPdf[6] != null ? indiPropPdf[6].toString() : "";
	 * String address = indiPropPdf[7] != null ? indiPropPdf[7].toString() : "";
	 * String ward = indiPropPdf[8] != null ? indiPropPdf[8].toString() : ""; String
	 * zone = indiPropPdf[9] != null ? indiPropPdf[9].toString() : ""; String
	 * applicantName = indiPropPdf[10] != null ? indiPropPdf[10].toString() : "";
	 * String typeOfProp = indiPropPdf[11] != null ? indiPropPdf[11].toString() :
	 * ""; String duesDate = indiPropPdf[12] != null ? indiPropPdf[12].toString() :
	 * "";
	 * 
	 * try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	 * 
	 * PdfWriter writer = new PdfWriter(outputStream); PdfDocument pdfDoc = new
	 * PdfDocument(writer); Document document = new Document(pdfDoc);
	 * document.setMargins(50f, 50f, 50f, 50f);
	 * 
	 * // Load Unicode font String ARIALUNICODE =
	 * ApplicationSession.getInstance().getMessage("arialUniCode.path"); FontProgram
	 * fontProgram = FontProgramFactory.createFont(ARIALUNICODE); PdfFont font =
	 * PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
	 * document.setFont(font); float fontSize = 11f;
	 * 
	 * // ---------------- HEADER (Logo + Title) ---------------- float logoSize =
	 * 80f; String logoPath = filenetPath +
	 * ApplicationSession.getInstance().getMessage("leftlogo"); ImageData logoData =
	 * ImageDataFactory.create(logoPath); Image logo = new
	 * Image(logoData).scaleAbsolute(logoSize, logoSize);
	 * 
	 * // Shift logo left and slightly down
	 * logo.setHorizontalAlignment(HorizontalAlignment.LEFT);
	 * logo.setMarginTop(10f); // push logo slightly down logo.setMarginLeft(0f); //
	 * push logo fully to left
	 * 
	 * // Title paragraphs Paragraph title = new
	 * Paragraph("कल्याण डोंबिवली महानगरपालिका")
	 * .setFont(font).setFontSize(14f).setBold()
	 * .setTextAlignment(TextAlignment.CENTER); Paragraph subtitle = new
	 * Paragraph("ना देय प्रमाणपत्र") .setFont(font).setFontSize(13f).setBold()
	 * .setTextAlignment(TextAlignment.CENTER);
	 * 
	 * // Add logo and title manually using a table float[] headerColWidth = { 100f,
	 * 350f }; Table headerTable = new
	 * Table(headerColWidth).useAllAvailableWidth().setBorder(Border.NO_BORDER);
	 * 
	 * Cell logoCell = new Cell().add(logo) .setBorder(Border.NO_BORDER)
	 * .setVerticalAlignment(VerticalAlignment.MIDDLE);
	 * headerTable.addCell(logoCell);
	 * 
	 * Cell titleCell = new Cell().add(title).add(new Paragraph("\n")).add(subtitle)
	 * .setBorder(Border.NO_BORDER) .setVerticalAlignment(VerticalAlignment.MIDDLE)
	 * .setTextAlignment(TextAlignment.CENTER); headerTable.addCell(titleCell);
	 * 
	 * document.add(headerTable); document.add(new Paragraph("\n"));
	 * 
	 * // ---------------- CERTIFICATE DETAILS ---------------- document.add(new
	 * Paragraph("जा.क्र.कर्डोमपा/" + ward + "/" + zone + "/ना.सुके/" +
	 * certificateNumber)
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT));
	 * document.add(new Paragraph("दिनांक: " + appDate)
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT));
	 * document.add(new Paragraph("टोकन क्रमांक : " + tokenNo)
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT));
	 * document.add(new Paragraph("\n"));
	 * 
	 * // ---------------- SUBJECT & REFERENCE ---------------- document.add(new
	 * Paragraph("विषय:- ना देय प्रमाणपत्र")
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.LEFT));
	 * document.add(new Paragraph("संदर्भ:- " + applicantName + " यांचा दि. " +
	 * appDate + " चा अर्ज")
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.LEFT));
	 * document.add(new Paragraph("\n"));
	 * 
	 * // ---------------- CERTIFICATE TITLE ---------------- document.add(new
	 * Paragraph("प्रमाणपत्र") .setFont(font).setFontSize(13f).setBold()
	 * .setTextAlignment(TextAlignment.CENTER)); document.add(new Paragraph("\n"));
	 * 
	 * // ---------------- MAIN BODY ---------------- String certText =
	 * "उपरोक्त संदर्भिय विषयान्वये, महापालिका दप्तरी मिळकत क्र. " + propertyNo +
	 * " (मालमत्तेचा प्रकार-" + typeOfProp + ") हा " + builderName +
	 * " यांचे नावे आहे. " + "त्यातील सदनिका क्र. " + fltNo + " ही " + flatOwnerName
	 * + " यांचे नावे आहे. " + "या सदनिकेवरील दि. " + duesDate +
	 * " पर्यंतच्या कराची देयके वसुल आहेत.\n\n";
	 * 
	 * document.add(new
	 * Paragraph(certText).setFont(font).setFontSize(fontSize).setTextAlignment(
	 * TextAlignment.JUSTIFIED));
	 * 
	 * // ---------------- SIGNATURE BLOCK ---------------- document.add(new
	 * Paragraph("sd/-").setFont(font).setFontSize(fontSize).setTextAlignment(
	 * TextAlignment.RIGHT)); document.add(new Paragraph("सहाय्यक आयुक्त (प्रभाग - "
	 * + ward + ")\nकल्याण डोंबिवली महानगरपालिका,\nकल्याण")
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT));
	 * 
	 * // ---------------- RECIPIENT ---------------- document.add(new
	 * Paragraph("\nप्रति,\n" + applicantName + "\n" + address)
	 * .setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.LEFT));
	 * 
	 * // ---------------- FOOTER ---------------- document.add(new
	 * Paragraph("\nThis is computer generated document, hence requires no signature"
	 * ) .setFont(font).setFontSize(10f).setItalic()
	 * .setTextAlignment(TextAlignment.CENTER));
	 * 
	 * document.close(); return outputStream.toByteArray();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return new byte[0]; } }
	 */


	    public byte[] generatePdfIndividualPropertyNoDuesCertificate(String propNo, String flatNo) {
	        LOGGER.info("Method started generatePdfIndividualPropertyNoDuesCertificate.......");

	        List<Object[]> individualCertificateList = singleWindowIntegrationRepository
	                .getIndividualPropertyCertificatePrint(propNo, flatNo);

	        if (individualCertificateList == null || individualCertificateList.isEmpty()) {
	            return new byte[0];
	        }

	        Object[] indiPropPdf = individualCertificateList.get(0);

	        String certificateNumber = indiPropPdf[0] != null ? indiPropPdf[0].toString() : "";
	        String appDate = indiPropPdf[1] != null ? indiPropPdf[1].toString() : "";
	        String tokenNo = indiPropPdf[2] != null ? indiPropPdf[2].toString() : "";
	        String flatOwnerName = indiPropPdf[3] != null ? indiPropPdf[3].toString() : "";
	        String builderName = indiPropPdf[4] != null ? indiPropPdf[4].toString() : "";
	        String propertyNo = indiPropPdf[5] != null ? indiPropPdf[5].toString() : "";
	        String fltNo = indiPropPdf[6] != null ? indiPropPdf[6].toString() : "";
	        String address = indiPropPdf[7] != null ? indiPropPdf[7].toString() : "";
	        String ward = indiPropPdf[8] != null ? indiPropPdf[8].toString() : "";
	        String zone = indiPropPdf[9] != null ? indiPropPdf[9].toString() : "";
	        String applicantName = indiPropPdf[10] != null ? indiPropPdf[10].toString() : "";
	        String typeOfProp = indiPropPdf[11] != null ? indiPropPdf[11].toString() : "";
	        
		/*
		 * String duesDate = indiPropPdf[12] != null ? indiPropPdf[12].toString() : "";
		 */
	        
	        String duesDate = "";

	        if (indiPropPdf[12] != null) {
	            try {
	                String dateStr = indiPropPdf[12].toString(); // e.g. "2025-10-28"
	                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	                duesDate = outputFormat.format(inputFormat.parse(dateStr));
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }
	        }


	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

	            PdfWriter writer = new PdfWriter(outputStream);
	            PdfDocument pdfDoc = new PdfDocument(writer);
	            pdfDoc.addNewPage();

	            Document document = new Document(pdfDoc);
	            document.setMargins(25f, 35f, 20f, 35f);
	            document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.90f));

	            // ---------------- FONTS ----------------
	            //mangalFont.path
	            String MANGAL = ApplicationSession.getInstance().getMessage("mangalFont.path");
	            String ARIALUNICODE = ApplicationSession.getInstance().getMessage("arialUniCode.path");
	            PdfFont fontMangal = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(MANGAL), PdfEncodings.IDENTITY_H, true);
	            PdfFont fontArial = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(ARIALUNICODE), PdfEncodings.IDENTITY_H, true);
	            float fontSize = 10.5f;

	            // Helper to choose font based on language
	            java.util.function.Function<String, PdfFont> chooseFont = text ->
	                    (text != null && text.matches(".*[\\u0900-\\u097F].*")) ? fontMangal : fontArial;

	            // ---------------- BORDER ----------------
	            float borderMargin = 18f;
	            Rectangle pageSize = pdfDoc.getDefaultPageSize();
	            PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
	            canvas.setLineWidth(0.8f);
	            canvas.rectangle(pageSize.getLeft() + borderMargin, pageSize.getBottom() + borderMargin,
	                    pageSize.getWidth() - 2 * borderMargin, pageSize.getHeight() - 2 * borderMargin);
	            canvas.stroke();

	            // ---------------- HEADER ----------------
	            String logoPath = filenetPath + ApplicationSession.getInstance().getMessage("leftlogo");
	            ImageData logoData = ImageDataFactory.create(logoPath);
	            Image logo = new Image(logoData).scaleAbsolute(65f, 80f);
	            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(logo);

	            document.add(new Paragraph("Kalyan Dombivli Municipal Corporation")
	                    .setFont(fontArial).setFontSize(12.5f).setBold()
	                    .setTextAlignment(TextAlignment.CENTER).setMarginBottom(2f));

	            // ---------------- CERTIFICATE DETAILS ----------------
	            document.add(new Paragraph()
	                    .add(new Text("Outward No.KDMC/").setFont(fontArial))
	                    .add(new Text(ward).setFont(chooseFont.apply(ward)))
	                    .add(new Text("/").setFont(fontArial))
	                    .add(new Text(zone).setFont(chooseFont.apply(zone)))
	                    .add(new Text("/CFC/").setFont(fontArial))
	                    .add(new Text(certificateNumber).setFont(chooseFont.apply(certificateNumber)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text("Date : ").setFont(fontArial))
	                    .add(new Text(appDate).setFont(chooseFont.apply(appDate)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text("Application No. : ").setFont(fontArial))
	                    .add(new Text(tokenNo).setFont(chooseFont.apply(tokenNo)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(4f));

	            // ---------------- SUBJECT & REFERENCE ----------------
	            document.add(new Paragraph("Subject : Property No Dues Certificate.")
	                    .setFont(fontArial).setFontSize(fontSize).setBold()
	                    .setTextAlignment(TextAlignment.LEFT).setMarginBottom(2f));

	            document.add(new Paragraph()
	                    .add(new Text("Referance : ").setFont(fontArial))
	                    .add(new Text(applicantName).setFont(chooseFont.apply(applicantName)))
	                    .add(new Text(" Application ").setFont(fontArial))
	                    .add(new Text("Dated ").setFont(fontArial))
	                    .add(new Text(appDate).setFont(chooseFont.apply(appDate)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(6f));
	            

	            // ---------------- TITLE ----------------
	            document.add(new Paragraph("Certificate")
	                    .setFont(fontArial).setFontSize(12.5f).setBold()
	                    .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6f));

	            // ---------------- BODY ----------------
	            Paragraph para = new Paragraph()
	                    .setFontSize(fontSize)
	                    .setMultipliedLeading(1.4f) // Smaller line spacing (1.0 = single, 1.1 slightly more)
	                    .setTextAlignment(TextAlignment.JUSTIFIED)
	                    .setFirstLineIndent(25f)
	                    .setMarginBottom(6f);

	            para.add(new Text("As per the above reference subject, Municipal Corporation, Property No. ").setFont(fontArial));
	            para.add(new Text(propertyNo).setFont(chooseFont.apply(propertyNo)));
	            para.add(new Text(" (Type of property - ").setFont(fontArial));
	            para.add(new Text(typeOfProp).setFont(chooseFont.apply(typeOfProp)));
	            para.add(new Text("). This property belongs to ").setFont(fontArial));
	            para.add(new Text(builderName).setFont(chooseFont.apply(builderName)));
	            para.add(new Text(", ").setFont(fontArial));
	            para.add(new Text(address).setFont(chooseFont.apply(address)));
	            para.add(new Text(". Their names On Flat No. ").setFont(fontArial));
	            para.add(new Text(fltNo).setFont(chooseFont.apply(fltNo)));
	            para.add(new Text(" and occupier name ").setFont(fontArial));
	            para.add(new Text(flatOwnerName).setFont(chooseFont.apply(flatOwnerName)));
	            para.add(new Text(". The Property tax for the said premises has been paid for the year ").setFont(fontArial));
	            para.add(new Text(duesDate).setFont(chooseFont.apply(duesDate)));
	            para.add(new Text(".").setFont(fontArial));
	            document.add(para);


	            // ---------------- SIGNATURE ----------------
	            document.add(new Paragraph("sd/-")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setMultipliedLeading(1.0f)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            Paragraph sign = new Paragraph()
	                    .add(new Text("Assistant Commissioner (Ward - ").setFont(fontArial))
	                    .add(new Text(ward).setFont(chooseFont.apply(ward)))
	                    .add(new Text(")").setFont(fontArial))
	                    .setMultipliedLeading(1.0f)
	                    .setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT);
	            document.add(sign);

	            document.add(new Paragraph("Kalyan Dombivli Municiple Corporation,\nKalyan.")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setMultipliedLeading(1.0f)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(6f));

	            // ---------------- RECIPIENT ----------------
	            document.add(new Paragraph("To,")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text(applicantName).setFont(chooseFont.apply(applicantName)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text(address).setFont(chooseFont.apply(address)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(4f));

	            // ---------------- FOOTNOTE ----------------
	            document.add(new Paragraph("This is computer generated document, hence requires no signature.")
	                    .setFont(fontArial).setFontSize(9.5f).setItalic()
	                    .setMultipliedLeading(2.0f)
	                    .setTextAlignment(TextAlignment.CENTER));

	            document.close();
	            LOGGER.info("Method ended generatePdfIndividualPropertyNoDuesCertificate.......");

	            return outputStream.toByteArray();

	        } catch (Exception e) {
	            LOGGER.error("Error generating Individual Property No Dues Certificate PDF: ", e);
	            return new byte[0];
	        }
	    }





	    public byte[] generateWholePropertyNoDuesCertificate(String propNo) {
	        List<Object[]> wholeCertificateList = singleWindowIntegrationRepository
	                .getWholePropertyCertificatePrint(propNo);
	        if (wholeCertificateList == null || wholeCertificateList.isEmpty()) {
	            return new byte[0];
	        }

	        Object[] wholyPropertcer = wholeCertificateList.get(0);

	        String certificateNo = wholyPropertcer[0] != null ? wholyPropertcer[0].toString() : "";
	        String appDate = wholyPropertcer[1] != null ? wholyPropertcer[1].toString() : "";
	        String ward = wholyPropertcer[2] != null ? wholyPropertcer[2].toString() : "";
	        String zone = wholyPropertcer[3] != null ? wholyPropertcer[3].toString() : "";
	        String tokenNo = wholyPropertcer[4] != null ? wholyPropertcer[4].toString() : "";
	        String ownerName = wholyPropertcer[5] != null ? wholyPropertcer[5].toString() : "";
	        String propertyNo = wholyPropertcer[6] != null ? wholyPropertcer[6].toString() : "";
	        String address = wholyPropertcer[7] != null ? wholyPropertcer[7].toString() : "";
	        String typeOfProp = wholyPropertcer[8] != null ? wholyPropertcer[8].toString() : "";
	        String duesDate = "";

	        if (wholyPropertcer[9] != null) {
	            try {
	                String dateStr = wholyPropertcer[9].toString(); // e.g. "2025-10-28"
	                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	                duesDate = outputFormat.format(inputFormat.parse(dateStr));
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }
	        }
	        String applicantName = wholyPropertcer[10] != null ? wholyPropertcer[10].toString() : "";

	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            LOGGER.error("Method started generateWholePropertyNoDuesCertificate.......");

	            PdfWriter writer = new PdfWriter(outputStream);
	            PdfDocument pdfDoc = new PdfDocument(writer);
	            pdfDoc.addNewPage();

	            Document document = new Document(pdfDoc);
	            document.setMargins(25f, 35f, 20f, 35f);
	            document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.90f));

	            // Fonts
	           // mangalFont.path
	            String MANGAL = ApplicationSession.getInstance().getMessage("mangalFont.path");
	            String ARIALUNICODE = ApplicationSession.getInstance().getMessage("arialUniCode.path");
	            PdfFont fontMangal = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(MANGAL), PdfEncodings.IDENTITY_H, true);
	            PdfFont fontArial = PdfFontFactory.createFont(
	                    FontProgramFactory.createFont(ARIALUNICODE), PdfEncodings.IDENTITY_H, true);
	            float fontSize = 10.5f;

	            // Helper to choose font based on language
	            java.util.function.Function<String, PdfFont> chooseFont = text ->
	                    (text != null && text.matches(".*[\\u0900-\\u097F].*")) ? fontMangal : fontArial;

	            // ---------------- BORDER ----------------
	            float borderMargin = 18f;
	            Rectangle pageSize = pdfDoc.getDefaultPageSize();
	            PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
	            canvas.setLineWidth(0.8f);
	            canvas.rectangle(pageSize.getLeft() + borderMargin, pageSize.getBottom() + borderMargin,
	                    pageSize.getWidth() - 2 * borderMargin, pageSize.getHeight() - 2 * borderMargin);
	            canvas.stroke();

	            // ---------------- HEADER ----------------
	            String leftLogoPath = filenetPath + ApplicationSession.getInstance().getMessage("leftlogo");
	            ImageData leftLogo = ImageDataFactory.create(leftLogoPath);
	            Image logo = new Image(leftLogo).scaleAbsolute(65f, 80f);
	            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(logo);

	            document.add(new Paragraph("Kalyan Dombivli Municipal Corporation")
	                    .setFont(fontArial).setFontSize(12.5f).setBold()
	                    .setTextAlignment(TextAlignment.CENTER).setMarginBottom(2f));

	            // ---------------- DETAILS ----------------
	            document.add(new Paragraph()
	                    .add(new Text("Outward No.KDMC/").setFont(fontArial))
	                    .add(new Text(ward).setFont(chooseFont.apply(ward)))
	                    .add(new Text("/").setFont(fontArial))
	                    .add(new Text(zone).setFont(chooseFont.apply(zone)))
	                    .add(new Text("/CFC/").setFont(fontArial))
	                    .add(new Text(certificateNo).setFont(chooseFont.apply(certificateNo)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text("Date : ").setFont(fontArial))
	                    .add(new Text(appDate).setFont(chooseFont.apply(appDate)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));


	            document.add(new Paragraph()
	                    .add(new Text("Application No. : ").setFont(fontArial))
	                    .add(new Text(tokenNo).setFont(chooseFont.apply(tokenNo)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(4f));


	            // ---------------- SUBJECT & REFERENCE ----------------
	            document.add(new Paragraph("Subject : Property No Dues Certificate ")
	                    .setFont(fontArial).setFontSize(fontSize).setBold()
	                    .setTextAlignment(TextAlignment.LEFT).setMarginBottom(2f));

	            document.add(new Paragraph()
	                    .add(new Text("Reference : ").setFont(fontArial))
	                    .add(new Text(applicantName).setFont(chooseFont.apply(applicantName)))
	                    .add(new Text(" Application").setFont(fontArial))
	                    .add(new Text(" Dated ").setFont(fontArial))
	                    .add(new Text(appDate).setFont(chooseFont.apply(appDate))) 
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(6f));


	            // ---------------- TITLE ----------------
	            document.add(new Paragraph("Certificate")
	                    .setFont(fontArial).setFontSize(12.5f).setBold()
	                    .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6f));

	            // ---------------- BODY ----------------
	            Paragraph para = new Paragraph()
	                    .setFontSize(fontSize)
	                    .setMultipliedLeading(1.4f) // Smaller line spacing
	                    .setTextAlignment(TextAlignment.JUSTIFIED)
	                    .setFirstLineIndent(25f)
	                    .setMarginBottom(6f);

	            para.add(new Text("As per the above reference subject, Municipal Corporation, Property No. ").setFont(fontArial));
	            para.add(new Text(propertyNo).setFont(chooseFont.apply(propertyNo)));
	            para.add(new Text(" (Type of Property - ").setFont(fontArial));
	            para.add(new Text(typeOfProp).setFont(chooseFont.apply(typeOfProp)));
	            para.add(new Text("). This property belongs to ").setFont(fontArial));
	            para.add(new Text(ownerName).setFont(chooseFont.apply(ownerName)));
	            para.add(new Text(", ").setFont(fontArial));
	            para.add(new Text(address).setFont(chooseFont.apply(address)));
	            para.add(new Text(".").setFont(fontArial));
	            para.add(new Text(" The Property tax for the said premises has been paid for the year ").setFont(fontArial));
	            para.add(new Text(duesDate).setFont(chooseFont.apply(duesDate)));
	            para.add(new Text(".").setFont(fontArial));
	            document.add(para);


	            // ---------------- SIGNATURE ----------------
	            document.add(new Paragraph("sd/-")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setMultipliedLeading(1.0f)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMarginBottom(0f));

	            Paragraph sign = new Paragraph()
	                    .add(new Text("Assistant Commissioner (Ward - ").setFont(fontArial))
	                    .add(new Text(ward).setFont(chooseFont.apply(ward)))
	                    .add(new Text(")").setFont(fontArial))
	                    .setMultipliedLeading(0.8f)
	                    .setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT);
	            document.add(sign);

	            document.add(new Paragraph("Kalyan Dombivli Municipal Corporation,\nKalyan.")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.RIGHT)
	                    .setMultipliedLeading(1.0f)
	                    .setMarginBottom(6f));

	            // ---------------- RECIPIENT ----------------
	            document.add(new Paragraph("To,")
	                    .setFont(fontArial).setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text(applicantName).setFont(chooseFont.apply(applicantName)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(0f));

	            document.add(new Paragraph()
	                    .add(new Text(address).setFont(chooseFont.apply(address)))
	                    .setFontSize(fontSize)
	                    .setTextAlignment(TextAlignment.LEFT)
	                    .setMarginBottom(4f));

	            // ---------------- FOOTNOTE ----------------
	            document.add(new Paragraph("This is computer generated document, hence requires no signature.")
	                    .setFont(fontArial).setFontSize(9.5f).setItalic()
	                    .setMultipliedLeading(2.0f)
	                    .setTextAlignment(TextAlignment.CENTER));

	            document.close();
	            LOGGER.error("Method ended generateWholePropertyNoDuesCertificate.......");

	            return outputStream.toByteArray();

	        } catch (Exception e) {
	            LOGGER.error("Error generating Property No Dues Certificate PDF: ", e);
	            return new byte[0];
	        }
	    }


	public boolean checkOutstandingStatus(@RequestParam(value = "propNo") String propNo,
			@RequestParam(value = "flatNo") String flatNo) {

		PropertyDetailDto detailDTO = null;
		TbBillMas tbBillMas = null;
		PropertyInputDto propInputDTO = new PropertyInputDto();
		propInputDTO.setPropertyNo(propNo);
		propInputDTO.setFlatNo(flatNo);
		propInputDTO.setOrgId(1L);

		// Call REST client to fetch bills
		List<TbBillMas> billMasList = new ArrayList<>();
		
		RestTemplate restReq = new RestTemplate();
		ResponseEntity<List<TbBillMas>> responseEntity =
				restReq.exchange(
			        ServiceEndpoints.Single_Window_Bill_Details,
			        HttpMethod.POST,
			        new HttpEntity<>(propInputDTO),
			        new ParameterizedTypeReference<List<TbBillMas>>() {}
			    );

			if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
				LOGGER.error("responseEntity Is Null Or Coming empty with (ServiceEndpoints.Single_Window_Bill_Details)....");
			    return false;
			}

			List<TbBillMas> responseBillMasList = responseEntity.getBody();
			
		

		if (CollectionUtils.isEmpty(responseBillMasList)) {
			return false; // No bills found
		}

		billMasList = responseBillMasList;

		Long currentYear = financialYearService.getFinanceYearId(new Date());
		Date firstSemEndDate = getFirstSemEndDate(currentYear);

		// Filter bills for the current year

		List<TbBillMas> currentYearBills = billMasList.stream()
				.filter(b -> b != null && currentYear.equals(b.getBmYear())).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(currentYearBills)) {
			return false; // No bills for current year
		}

		if (currentYearBills.get(currentYearBills.size() - 1).getBmYear().equals(currentYear)
				&& (Utility.compareDate(new Date(), firstSemEndDate)
						|| Utility.comapreDates(new Date(), firstSemEndDate))
				&& CollectionUtils.isNotEmpty(currentYearBills)) {

			// Half
			if (currentYearBills.get(currentYearBills.size() - 1).getBmToatlRebate() <= 0) {
				boolean paidFlag = checkHalfAMountPaid(currentYearBills);
				if (!paidFlag) {

					return false;
				}
			}
		}

		return true;
	}

	private boolean checkHalfAMountPaid(List<TbBillMas> selectYearBillMas) {
		LOGGER.error("Method Started checkHalfAMountPaid.......");
		boolean paidFlag = true;
		AtomicDouble totalCurBill = new AtomicDouble(0);
		AtomicDouble totalCurBillBal = new AtomicDouble(0);

		Long bmIdno = selectYearBillMas.get(0).getBmIdno();

		List<Object[]> currYearBillAmt = singleWindowIntegrationRepository.getCurrYearAmtByBmIdno(bmIdno);

		if (currYearBillAmt != null && !currYearBillAmt.isEmpty()) {

			Object[] firstRow = currYearBillAmt.get(0);

			totalCurBill.set(getSafeDouble(firstRow, 0));
			totalCurBillBal.set(getSafeDouble(firstRow, 1));
		}else {
			LOGGER.error("currYearBillAmt Is Coming Empty");
		}
		LOGGER.error("Total Current Balance Amount: " + totalCurBillBal);
		LOGGER.error("Total Current Bill Amount: " + totalCurBill);
		if (Math.round(totalCurBillBal.doubleValue()) > (totalCurBill.doubleValue() / 2)) {

			LOGGER.error("Method Ended checkHalfAMountPaid....... With Paid flag: " + paidFlag);
			paidFlag = false;

		}
		LOGGER.error("Method Ended checkHalfAMountPaid....... With Paid flag: " + paidFlag);
		return paidFlag;

	}

	private Date getFirstSemEndDate(Long currentYear) {
		FinancialYear currentFinYear = financialYearService.getFinincialYearsById(currentYear,
				1L);
		Timestamp currFinYearTimeStamp = new Timestamp(currentFinYear.getFaFromDate().getTime());
		Date currentFinYearDate = new Date(currFinYearTimeStamp.getTime());
		LocalDate currLocalDate = currentFinYearDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate monthEnd = currLocalDate.plusMonths(6).withDayOfMonth(1).minusDays(1);
		Date firstSemEndDate = Date.from(monthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return firstSemEndDate;
	}
	
	private double getSafeDouble(Object[] row, int index) {
	    if (row != null && row.length > index && row[index] != null) {
	        return ((Number) row[index]).doubleValue();
	    }
	    return 0.0;
	}


}
