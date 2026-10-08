package com.abm.mainet.common.integration.dms.rest.ui.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.dashboard.service.CitizenDashboardGraphServiceImpl;
import com.abm.mainet.common.integration.dms.domain.DigilockerCertificateEntity;
import com.abm.mainet.common.integration.dms.dto.DocDetailsRequest;
import com.abm.mainet.common.integration.dms.dto.DocDetailsResponse;
import com.abm.mainet.common.integration.dms.dto.IssuedTo;
import com.abm.mainet.common.integration.dms.dto.Person;
import com.abm.mainet.common.integration.dms.dto.Photo;
import com.abm.mainet.common.integration.dms.dto.PullURIRequest;
import com.abm.mainet.common.integration.dms.dto.PullURIResponse;
import com.abm.mainet.common.integration.dms.dto.ResponseStatus;
import com.abm.mainet.common.integration.dms.repository.DigilockerCertificateRepository;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.Filepaths;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

import io.swagger.annotations.Api;

import static com.abm.mainet.common.constant.PrefixConstants.MobilePreFix.GENDER;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

/**
 * @author Saroj.Tambe
 *
 */
@RestController
@RequestMapping("/api/digilocker")
@WebService
@Api(value = "/api/digilocker")
@Path("/api/digilocker")
public class DigiLockerController {

	private static final Logger LOGGER = Logger.getLogger(DigiLockerController.class);
	// private static final String API_KEY = "KTY6BuMhxbCAIYxjz1iSsRaCXylCPZDt";

	private static final String API_KEY = ApplicationSession.getInstance().getMessage("digilocker.secret.key");

	@Autowired
	CitizenDashboardGraphServiceImpl citizenDashboardGraphServiceImpl;
	
	@Autowired
	private DigilockerCertificateRepository digilockerCertificateRepository;
	
	@Value("${upload.physicalPath}")
    private String filenetPath;

	
	
	@RequestMapping(value = "/pull-uri", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
	public ResponseEntity<PullURIResponse> pullUri(HttpServletRequest servletRequest,
			@RequestHeader("x-digilocker-hmac") String receivedHmac) throws JAXBException {
		String xmlString = "";
		boolean isValid = false;
		PullURIRequest request = null;
		String rawXml = null;

		try {
			// 1. Read raw XML as String
			StringBuilder xmlBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					xmlBuilder.append(line).append("\n");
				}
			}
			rawXml = xmlBuilder.toString();

			// 3. Unmarshal XML into Java object
			JAXBContext jaxbContext = JAXBContext.newInstance(PullURIRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			request = (PullURIRequest) unmarshaller.unmarshal(new StringReader(rawXml));

			LOGGER.info("Incoming MRG Certificate hamac from API: " + xmlString);

			// 5. Validate HMAC
			isValid = validateHMAC(rawXml, receivedHmac, API_KEY);

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("Incoming hamac from API: " + receivedHmac);

		if (!isValid) {
			LOGGER.info("Unauthorized Request... ");
			PullURIResponse pullURIResponse = new PullURIResponse();
			return new ResponseEntity<>(pullURIResponse, HttpStatus.UNAUTHORIZED);
		}

		// 6. Process business logic
		PullURIResponse response = processRequest(request);
		
		if (StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "400")) { 
			
			return ResponseEntity.badRequest().body(response);
		}
		
		try {
		    if (response != null 
		            && response.getResponseStatus() != null 
		            && StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "200")) {

		        LOGGER.info("Started Saving Marriage Certificate Data For Digilocker..");

		        DigilockerCertificateEntity digilockerCertificateEntity = new DigilockerCertificateEntity();

		        // Store current timestamp (yyyy-MM-dd HH:mm:ss)
		        digilockerCertificateEntity.setLmodDate(new java.sql.Timestamp(System.currentTimeMillis()));

		        digilockerCertificateEntity.setCertificateType("MARRIAGE CERTIFICATE");
		        digilockerCertificateEntity.setOrgId(1L);

		        // Save request JSON if present
		        if (StringUtils.isNotBlank(rawXml)) {
		            digilockerCertificateEntity.setDigilockerRequestJson(rawXml);
		        }

		        // Save URI safely if available
		        if (response.getDocDetails() != null 
		                && StringUtils.isNotBlank(response.getDocDetails().getUri())) {
		            digilockerCertificateEntity.setuDF1(response.getDocDetails().getUri());
		        }

		        digilockerCertificateRepository.save(digilockerCertificateEntity);

		        LOGGER.info("End Saving Marriage Certificate Data For Digilocker..");

		    } else {
		        LOGGER.warn("Response is null or response code is not 200. Skipping Marriage Certificate save.");
		    }
		} catch (Exception e) {
		    LOGGER.error("Exception while saving Marriage Certificate Data For Digilocker", e);
		}

		
		return ResponseEntity.ok(response);
	}

	
	@RequestMapping(value = "/birthCertificate-pull-uri", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
	public ResponseEntity<PullURIResponse> birthCertificatePullUri(HttpServletRequest servletRequest,
			@RequestHeader("x-digilocker-hmac") String receivedHmac) throws JAXBException {
		String xmlString = "";
		boolean isValid = false;
		PullURIRequest request = null;
		String rawXml = null;

		try {
			// 1. Read raw XML as String
			StringBuilder xmlBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					xmlBuilder.append(line).append("\n");
				}
			}
			 rawXml = xmlBuilder.toString();
			LOGGER.info("Raw XML: " + rawXml);

			// 3. Unmarshal XML into Java object
			JAXBContext jaxbContext = JAXBContext.newInstance(PullURIRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			request = (PullURIRequest) unmarshaller.unmarshal(new StringReader(rawXml));

			// 5. Validate HMAC
			isValid = validateHMAC(rawXml, receivedHmac, API_KEY);

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("Incoming hamac from API: " + receivedHmac);

		if (!isValid) {
			LOGGER.info("Unauthorized Request... ");
			PullURIResponse pullURIResponse = new PullURIResponse();
			return new ResponseEntity<>(pullURIResponse, HttpStatus.UNAUTHORIZED);
		}

		// 6. Process business logic
		PullURIResponse response = birthProcessRequest(request);
		if (StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "400")) { 
			
			return ResponseEntity.badRequest().body(response);
		}
		
		try {
		    if (response != null 
		            && response.getResponseStatus() != null 
		            && StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "200")) {

		        LOGGER.info("Started Saving Birth Certificate Data For Digilocker..");

		        DigilockerCertificateEntity digilockerCertificateEntity = new DigilockerCertificateEntity();

		        // Store current timestamp (yyyy-MM-dd HH:mm:ss)
		        digilockerCertificateEntity.setLmodDate(new java.sql.Timestamp(System.currentTimeMillis()));

		        digilockerCertificateEntity.setCertificateType("BIRTH CERTIFICATE");
		        digilockerCertificateEntity.setOrgId(1L);

		        // Save request JSON if present
		        if (StringUtils.isNotBlank(rawXml)) {
		            digilockerCertificateEntity.setDigilockerRequestJson(rawXml);
		        }

		        // Save URI safely if available
		        if (response.getDocDetails() != null 
		                && StringUtils.isNotBlank(response.getDocDetails().getUri())) {
		            digilockerCertificateEntity.setuDF1(response.getDocDetails().getUri());
		        }

		        digilockerCertificateRepository.save(digilockerCertificateEntity);

		        LOGGER.info("End Saving Birth Certificate Data For Digilocker..");

		    } else {
		        LOGGER.warn("Response is null or response code is not 200. Skipping Digilocker save.");
		    }
		} catch (Exception e) {
		    LOGGER.error("Exception while saving Birth Certificate Data For Digilocker", e);
		}

		
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(value = "/deathCertificate-pull-uri", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
	public ResponseEntity<PullURIResponse> deathCertificatePullUri(HttpServletRequest servletRequest,
			@RequestHeader("x-digilocker-hmac") String receivedHmac) throws JAXBException {
		String xmlString = "";
		boolean isValid = false;
		PullURIRequest request = null;
		String rawXml = null;

		try {
			// 1. Read raw XML as String
			StringBuilder xmlBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					xmlBuilder.append(line).append("\n");
				}
			}
			 rawXml = xmlBuilder.toString();
			LOGGER.info("Raw XML: " + rawXml);

			// 3. Unmarshal XML into Java object
			JAXBContext jaxbContext = JAXBContext.newInstance(PullURIRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			request = (PullURIRequest) unmarshaller.unmarshal(new StringReader(rawXml));

			// 5. Validate HMAC
			isValid = validateHMAC(rawXml, receivedHmac, API_KEY);

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("Incoming hamac from API: " + receivedHmac);

		if (!isValid) {
			LOGGER.info("Unauthorized Request... ");
			PullURIResponse pullURIResponse = new PullURIResponse();
			return new ResponseEntity<>(pullURIResponse, HttpStatus.UNAUTHORIZED);
		}

		// 6. Process business logic
		PullURIResponse response = DeathProcessRequest(request);
		
		if (response != null 
		        && response.getResponseStatus() != null 
		        && StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "200")) {

		    LOGGER.info("Started Saving Birth Certificate Data For Digilocker..");

		    DigilockerCertificateEntity digilockerCertificateEntity = new DigilockerCertificateEntity();

		    // store current timestamp with date+time
		    digilockerCertificateEntity.setLmodDate(new java.sql.Timestamp(System.currentTimeMillis()));

		    digilockerCertificateEntity.setCertificateType("DEATH CERTIFICATE");
		    digilockerCertificateEntity.setOrgId(1L);

		    // set request JSON if available
		    if (StringUtils.isNotBlank(rawXml)) {
		        digilockerCertificateEntity.setDigilockerRequestJson(rawXml);
		    }

		    // set UDF1 safely
		    if (response.getDocDetails() != null 
		            && StringUtils.isNotBlank(response.getDocDetails().getUri())) {
		        digilockerCertificateEntity.setuDF1(response.getDocDetails().getUri());
		    }

		    try {
		        digilockerCertificateRepository.save(digilockerCertificateEntity);
		        LOGGER.info("End Saving Birth Certificate Data For Digilocker..");
		    } catch (Exception e) {
		        LOGGER.error("Error while saving Birth Certificate Data For Digilocker", e);
		    }
		}

			
		
		return ResponseEntity.ok(response);
	}

	public boolean validateHMAC(String requestBody, String receivedHmac, String apiKey) {
		try {
			 
			 String javaFormattedXml = convertToSingleLine(requestBody);

			String hmacHex = computeHmacSHA256Hex(javaFormattedXml, apiKey);
			System.out.println("Computed HMAC (Hex): " + hmacHex);
			byte[] bytes = hmacHex.getBytes(StandardCharsets.UTF_8);
			String computedHmac = Base64.getEncoder().encodeToString(bytes);
			LOGGER.info("computed HMAC : " + computedHmac);
			return computedHmac.equals(receivedHmac);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String computeHmacSHA256Hex(String message, String secretKey) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKeySpec);

			byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

			// Convert byte[] to hex string
			StringBuilder hexString = new StringBuilder(2 * hmacBytes.length);
			for (byte b : hmacBytes) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error computing HMAC-SHA256 (Hex)", e);
		}
	}

	public static String marshalRequestToXml(PullURIRequest request) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PullURIRequest.class);
		StringWriter writer = new StringWriter();
		context.createMarshaller().marshal(request, writer);
		return writer.toString();
	}
	

	public PullURIResponse processRequest(PullURIRequest request) {
	    LOGGER.info("Method Started marriageProcessRequest....");

	    PullURIResponse response = new PullURIResponse();
	    ResponseStatus status = new ResponseStatus();
	    DocDetailsResponse docDetails = new DocDetailsResponse();

	    // Default fallback (failure 400)
	    status.setStatus("0");
	    status.setValue("0");
	    status.setResponseCode("400");
	    status.setTs(request != null ? request.getTs() : null);
	    status.setTxn(request != null ? request.getTxn() : null);

	    docDetails.setDocContent("Document Not Found");
	    docDetails.setDataContent("");

	    try {
	        // Basic null safety
	        if (request == null || request.getDocDetails() == null) {
	            LOGGER.warn("Request or DocDetails is null; returning fallback.");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            return response;
	        }

	        String mrgDocType = request.getDocDetails().getDocType();
	        String udf1 = request.getDocDetails().getUdf1();
	        if (udf1 == null) {
	            LOGGER.warn("Missing UDF1 (marriage id). Returning fallback.");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            return response;
	        }

	        Object[] marriageData = citizenDashboardGraphServiceImpl.getMarriageData(udf1);
	        if (marriageData == null || marriageData.length == 0) {
	            LOGGER.warn("No marriage data found for UDF1: " + udf1);
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            return response;
	        }

	        // Prepare persons
	        Person person1 = new Person();
	        Person person2 = new Person();

	        String hFname = safeValue(marriageData[4]);
	        String hMname = safeValue(marriageData[5]);
	        String hLname = safeValue(marriageData[6]);
	        String wFname = safeValue(marriageData[8]);
	        String wMname = safeValue(marriageData[9]);
	        String wLname = safeValue(marriageData[10]);

	        person1.setName((hFname + " " + hMname + " " + hLname).trim());
	        person1.setGender("Male");
	        person1.setPhone(request.getDocDetails().getUdf2() != null ? request.getDocDetails().getUdf2() : "");

	        person2.setName((wFname + " " + wMname + " " + wLname).trim());
	        person2.setGender("Female");
	        person2.setPhone(request.getDocDetails().getUdf2() != null ? request.getDocDetails().getUdf2() : "");

	        // Format dates safely
	        String formattedMrgDate = safeFormatDate(marriageData[2], "yyyy-MM-dd HH:mm:ss.S", "dd-MM-yyyy");
	        String formattedIssueDate = safeFormatDate(marriageData[20], "yyyy-MM-dd", "dd-MM-yyyy");
	        String formattedHusbandDob = safeFormatDate(marriageData[18], "yyyy-MM-dd HH:mm:ss.S", "dd-MM-yyyy");
	        String formattedWifeDob = safeFormatDate(marriageData[19], "yyyy-MM-dd HH:mm:ss.S", "dd-MM-yyyy");

	        person1.setDob(formattedHusbandDob);
	        person2.setDob(formattedWifeDob);

	        IssuedTo issuedTo = new IssuedTo();
	        issuedTo.setPersons(Arrays.asList(person1, person2));
	        docDetails.setIssuedTo(issuedTo);

	        // Safe serial for URI
	        String serialForUri = safeString(marriageData[3]);
	        docDetails.setUri("in.gov.kdmc-" + (mrgDocType != null ? mrgDocType : "") + "-" + serialForUri);

	        // Fetch certificate
	        byte[] marriageCertificate = null;
	        if (marriageData[1] != null) {
	            try {
	                marriageCertificate = getMarriageCertificate(((BigInteger) marriageData[1]).longValue());
	            } catch (Exception ex) {
	                LOGGER.error("Error fetching marriage certificate bytes", ex);
	            }
	        }

	        if (marriageCertificate == null || marriageCertificate.length == 0) {
	            LOGGER.warn("Marriage certificate bytes are empty/null.");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            return response;
	        }

	        // Success path
	        String base64String = Base64.getEncoder().encodeToString(marriageCertificate);
	        docDetails.setDocContent(base64String);

	      	        
	        String metadataXml =
					"<Certificate " + "language=\"\" " + "name=\"Marriage Certificate\" " + "type=\"RMCER\" "
							+ "number=\"\" " + "prevNumber=\"\" " + "serialNumber=\"" + safeString(marriageData[3])
							+ "\" " + "issuedAt=\"\" " + "issueDate=\"" + safeString(formattedIssueDate) + "\" "
							+ "validFromDate=\"\" " + "expiryDate=\"\" " + "status=\"\">" +

							"<IssuedBy>" + "<Organization name=\"KDMC\" code=\"1\" tin=\"\" uid=\"\" type=\"\">"
							+ "<Address type=\"Official\" "
							+ "line1=\"Kalyan Dombivli Municipal Corporation, Shankarrao Chowk\" "
							+ "line2=\"Waidhavni Kalyan West\" " + "house=\"\" " + "landmark=\"Shivaji Chowk\" "
							+ "locality=\"Kalyan,west\" " + "vtc=\"\" " + "district=\"Thane\" " + "pin=\"421301\" "
							+ "state=\"Maharashtra\" " + "country=\"India\"/>" + "</Organization>" + "</IssuedBy>" +

	        	        "<IssuedTo>" +
							"<Person " + "uid=\"" + safeValue(marriageData[22]) + "\" " + "title=\"Mr\" " + "name=\""
							+ safeValue(person1.getName()) + "\" " + "dob=\"" + safeValue(formattedHusbandDob) + "\" "
							+ "age=\"\" " + "swd=\"\" " + "swdIndicator=\"\" " + "motherName=\"\" " + "gender=\"Male\" "
							+ "maritalStatus=\"Married\" " + "relationWithHof=\"\" " + "disabilityStatus=\"\" "
							+ "category=\"\" " + "religion=\"\" " + "phone=\"\" " + "email=\"\">"
							+ "<Address type=\"Permanent\" " + "line1=\"" + safeValue(marriageData[24]) + "\" "
							+ "line2=\"\" " + "house=\"\" " + "landmark=\"\" " + "locality=\"\" " + "vtc=\"\" "
							+ "district=\"\" " + "pin=\"\" " + "state=\"\" " + "country=\"India\"/>"
							+ "<Photo format=\"\"></Photo>" + "</Person>" +

							"<Person " + "uid=\"" + safeValue(marriageData[23]) + "\" " + "title=\"Mrs\" " + "name=\""
							+ safeValue(person2.getName()) + "\" " + "dob=\"" + safeValue(formattedWifeDob) + "\" "
							+ "age=\"\" " + "swd=\"\" " + "swdIndicator=\"\" " + "motherName=\"\" "
							+ "gender=\"Female\" " + "maritalStatus=\"Married\" " + "relationWithHof=\"\" "
							+ "disabilityStatus=\"\" " + "category=\"\" " + "religion=\"\" " + "phone=\"\" "
							+ "email=\"\">" + "<Address type=\"Permanent\" " + "line1=\"" + safeValue(marriageData[25])
							+ "\" " + "line2=\"\" " + "house=\"\" " + "landmark=\"\" " + "locality=\"\" " + "vtc=\"\" "
							+ "district=\"\" " + "pin=\"\" " + "state=\"\" " + "country=\"India\"/>"
							+ "<Photo format=\"\"></Photo>" + "</Person>" +
	        	        "</IssuedTo>" +

	        	        "<CertificateData " +
	        	            "placeOfMarriage=\"" + safeValue(marriageData[21]) + "\" " +
	        	            "marriageDate=\"" + safeValue(formattedMrgDate) + "\"/>" +
	        	    "</Certificate>";

	        

	        String base64Metadata = Base64.getEncoder()
	                .encodeToString(metadataXml.getBytes(StandardCharsets.UTF_8));
	        docDetails.setDataContent(base64Metadata);

	        // Mark success
	        status.setStatus("1");
	        status.setValue("1");
	        status.setResponseCode("200");

	        response.setResponseStatus(status);
	        response.setDocDetails(docDetails);

	        LOGGER.info("Method End marriageProcessRequest (success).");
	        return response;

	    } catch (Exception e) {
	        LOGGER.error("Error occurred while processing marriageProcessRequest", e);
	        response.setResponseStatus(status);
	        response.setDocDetails(docDetails);
	        return response;
	    }
	}

	/**
	 * Helper to format date safely.
	 */
	private String safeFormatDate(Object dateObj, String inPattern, String outPattern) {
	    if (dateObj == null) return null;
	    try {
	        SimpleDateFormat in = new SimpleDateFormat(inPattern);
	        SimpleDateFormat out = new SimpleDateFormat(outPattern);
	        return out.format(in.parse(dateObj.toString()));
	    } catch (Exception e) {
	        LOGGER.warn("Date parse/format failed for: " + dateObj, e);
	        return null;
	    }
	}


	public PullURIResponse birthProcessRequest(PullURIRequest request) {
	    LOGGER.info("Method Started processRequest....");

	    PullURIResponse response = new PullURIResponse();
	    ResponseStatus status = new ResponseStatus();
	    DocDetailsResponse docDetails = new DocDetailsResponse();

	    // Default to FAILURE (400) and plain-text fallback. We'll overwrite on success.
	    status.setStatus("0");
	    status.setValue("0");
	    status.setResponseCode("400"); // generic client-side/input error by default
	    status.setTs(request != null ? request.getTs() : null);
	    status.setTxn(request != null ? request.getTxn() : null);

	    // Always prepare a default fallback payload
	    docDetails.setDocContent("Document Not Found"); // plain text, as requested
	    docDetails.setDataContent("");                  // empty string

	    try {
	        // Basic null-safety: if request/docDetails missing, return fallback (400)
	        if (request == null || request.getDocDetails() == null) {
	            LOGGER.warn("Request or DocDetails is null; returning 400 fallback.");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            LOGGER.info("Method End processRequest (fallback due to null request/docdetails).");
	            return response;
	        }

	        String docType = request.getDocDetails().getDocType();

	        String formattedUdf2 = null;
	        String formattedUdf3 = null;
	        String formattedDob = null;
	        String formattedBrRegDate = null;
	        String base64String = null;

	        Object[] birthCertificateData = null;

	        // Person Info (never null)
	        Person person1 = new Person();
	        Person person2 = new Person();

	        // Validate mandatory UDFs
	        String udf1 = request.getDocDetails().getUdf1();
	        String udf2 = request.getDocDetails().getUdf2();
	        String udf3 = request.getDocDetails().getUdf3();
	        if (udf1 == null || udf2 == null || udf3 == null) {
	            LOGGER.warn("Missing UDFs (udf1/udf2/udf3). Returning 400 fallback.");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            LOGGER.info("Method End processRequest (fallback due to missing UDFs).");
	            return response;
	        }

	        // Reformat dates dd-MM-yyyy -> yyyy-MM-dd
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        formattedUdf2 = LocalDate.parse(udf2, inputFormatter).format(outputFormatter);
	        formattedUdf3 = LocalDate.parse(udf3, inputFormatter).format(outputFormatter);

	        LOGGER.info("formattedRegDate: " + formattedUdf2 + " | formattedDateOfBirth: " + formattedUdf3);

	        // Fetch data
	        birthCertificateData = citizenDashboardGraphServiceImpl
	                .getBirthRegCertificateData(udf1, formattedUdf2, formattedUdf3);

	        if (birthCertificateData == null || birthCertificateData.length == 0) {
	            // No record => 400
	            LOGGER.warn("No birth certificate data found for provided inputs. Returning 404 fallback.");
	            status.setResponseCode("400");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            LOGGER.info("Method End processRequest (fallback 404 no data).");
	            return response;
	        }

	        // Build person details safely
	        String genderCode = (birthCertificateData.length > 5 && birthCertificateData[5] != null)
	                ? birthCertificateData[5].toString() : null;

	        String genderDescEng = null;
	        if (genderCode != null) {
	            try {
	                LookUp lookup = CommonMasterUtility
	                        .getNonHierarchicalLookUpObjectByPrefix(Long.valueOf(genderCode), 1L, GENDER);
	                if (lookup != null) {
	                    genderDescEng = lookup.getDescLangFirst();
	                }
	            } catch (Exception ex) {
	                LOGGER.warn("Failed to resolve gender lookup: " + genderCode, ex);
	            }
	        }

	        String brChildName = (birthCertificateData.length > 3 && birthCertificateData[3] != null)
	                ? birthCertificateData[3].toString() : null;
	        person1.setName(brChildName);
	        person1.setGender(genderDescEng);

	        String rawDob = (birthCertificateData.length > 4 && birthCertificateData[4] != null)
	                ? birthCertificateData[4].toString() : null;
	        String rawBirthRegDate = (birthCertificateData.length > 2 && birthCertificateData[2] != null)
	                ? birthCertificateData[2].toString() : null;

	        if (rawBirthRegDate != null) {
	            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
	            formattedBrRegDate = out.format(in.parse(rawBirthRegDate));
	        }

	        if (rawDob != null) {
	            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
	            formattedDob = out.format(in.parse(rawDob));
	        }
	        person1.setDob(formattedDob);

	        person2.setPdFatherName((birthCertificateData.length > 9 && birthCertificateData[9] != null)
	                ? birthCertificateData[9].toString() : null);
	        person2.setPdMotherName((birthCertificateData.length > 10 && birthCertificateData[10] != null)
	                ? birthCertificateData[10].toString() : null);
	        person2.setPdAddress((birthCertificateData.length > 6 && birthCertificateData[6] != null)
	                ? birthCertificateData[6].toString() : null);

	        IssuedTo issuedTo = new IssuedTo();
	        issuedTo.setPersons(Arrays.asList(person1, person2));
	        docDetails.setIssuedTo(issuedTo);

	        // Safe serial for URI
	        String serialForUri = (birthCertificateData.length > 8) ? safeString(birthCertificateData[8]) : "";
	        docDetails.setUri("in.gov.kdmc-" + (docType != null ? docType : "") + "-" + serialForUri);

	        // Fetch the actual certificate bytes
	        LOGGER.error("UDF1 from digilocker: " + udf1);
	        byte[] digiBirthCertificate = getBirthCertificate(udf1, formattedUdf2, formattedUdf3);

	        if (digiBirthCertificate == null || digiBirthCertificate.length == 0) {
	            // No certificate bytes => 404
	            LOGGER.warn("Certificate bytes are empty/null. Returning 404 fallback.");
	            status.setResponseCode("404");
	            response.setResponseStatus(status);
	            response.setDocDetails(docDetails);
	            LOGGER.info("Method End processRequest (fallback 404 empty bytes).");
	            return response;
	        }

	        // Success path: set Base64 content
	        base64String = Base64.getEncoder().encodeToString(digiBirthCertificate);
	        docDetails.setDocContent(base64String);

	        // Build metadata XML (uses your existing helpers safeString/safeValue)
	        String metadataXml =
	        		"<Certificate " +
	        		        "language=\"\" " + "name=\"Birth Certificate\" " + "type=\"BTCER\" " + "number=\"\" " +
	        		        "prevNumber=\"\" " + "SerialNumber=\"" + safeString(birthCertificateData.length > 8 ? birthCertificateData[8] : null) + "\" " +
	        		        "issuedAt=\"\" " +  "issueDate=\"\" " + "validFromDate=\"\" " + "expiryDate=\"\" " + "status=\"\">" +
	                "<IssuedBy>" +
							"<Organization name=\"KDMC\" code=\"1\" tin=\"\" uid=\"\" type=\"\">" +
	                "<Address " + "type=\"Official\" "
							+ "line1=\"Kalyan Dombivli Municipal Corporation, Shankarrao Chowk\" "
							+ "line2=\"Waidhavni Kalyan West\" " + "house=\"\" " + "landmark=\"Shivaji Chowk\" "
							+ "locality=\"Kalyan,west\" " + "vtc=\"\" " + "district=\"Thane\" " + "pin=\"421301\" "
							+ "state=\"Maharashtra\" " + "country=\"India\" />" + "</Organization>" +
	                "</IssuedBy>" +

	                "<IssuedTo>" +

							"<Person " + "uid=\"\" " + "title=\"\" " + "name=\"" + safeValue(person1.getName()) + "\" "
							+ "dob=\"" + safeValue(formattedDob) + "\" " + "age=\"\" " + "swd=\"\" "
							+ "swdIndicator=\"\" " + "motherName=\""
							+ safeValue(birthCertificateData.length > 10 ? birthCertificateData[10] : null) + "\" "
							+ "gender=\"" + safeValue(person1.getGender()) + "\" " + "maritalStatus=\"\" "
							+ "relationWithHof=\"\" " + "disabilityStatus=\"\" " + "category=\"\" " + "religion=\"\" "
							+ "phone=\"\" " + "email=\"\">"

							+ "<Address " + "type=\"Permanent\" " + "line1=\""
							+ safeValue(birthCertificateData.length > 11 ? birthCertificateData[11] : null) + "\" "
							+ "line2=\"\" " + "house=\"\" " + "landmark=\"\" " + "locality=\"\" " + "vtc=\"\" "
							+ "district=\"Thane\" " + "pin=\"\" " + "state=\"Maharashtra\" " + "country=\"India\"/>"

							+ "<Photo format=\"\"></Photo>"

							+ "</Person>" +

	                "</IssuedTo>" +

							"<CertificateData " + "registrationNo=\""
							+ safeValue(birthCertificateData.length > 1 ? birthCertificateData[1] : null) + "\" "
							+ "registrationDate=\"" + safeValue(formattedBrRegDate) + "\" " + "placeOfBirth=\""
							+ safeValue(birthCertificateData.length > 6 ? birthCertificateData[6] : null) + "\" "
							+ "birthAddress=\""
							+ safeValue(birthCertificateData.length > 7 ? birthCertificateData[7] : null) + "\" "
							+ "fatherName=\""
							+ safeValue(birthCertificateData.length > 9 ? birthCertificateData[9] : null) + "\">"
							+ "</CertificateData>" +
	                "</Certificate>";

	        String base64Metadata = Base64.getEncoder()
	                .encodeToString(metadataXml.getBytes(StandardCharsets.UTF_8));
	        docDetails.setDataContent(base64Metadata);

	        // Mark overall success
	        status.setStatus("1");
	        status.setValue("1");
	        // Optional: set a success response code in your payload (if you use it)
	         status.setResponseCode("200");

	        response.setResponseStatus(status);
	        response.setDocDetails(docDetails);

	        LOGGER.info("Method End processRequest (success).");
	        return response;

	    } catch (Exception e) {
	        LOGGER.error("Error occurred while processing birthProcessRequest", e);
	        // Keep pre-set fallback (400) and payload
	        response.setResponseStatus(status);
	        response.setDocDetails(docDetails);
	        LOGGER.info("Method End processRequest (fallback due to exception).");
	        return response;
	    }
	}



	private static byte[] getMarriageCertificate(final Long applicationNo) throws ClassNotFoundException, LinkageError {
		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.mrm.service.MarriageServiceImpl";
		Long orgId = 1L;
		/*
		 * String srcPath = Filepaths.getfilepath() + MainetConstants.PDFFOLDERNAME +
		 * MainetConstants.FILE_PATH_SEPARATOR + applicationNo +
		 * MainetConstants.PDF_EXTENSION;
		 */
		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);
		final Method method = ReflectionUtils.findMethod(clazz, "generateCertificateForDigiLocker",
				new Class[] { Long.class, Long.class });
		Object obj = (Object) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
				new Object[] { applicationNo, orgId });

		// Safely cast and return if it's a byte[]
		if (obj instanceof byte[]) {
			return (byte[]) obj;
		} else {
			throw new IllegalStateException("Expected byte[] but got " + (obj != null ? obj.getClass() : "null"));
		}
	}

	private static byte[] getBirthCertificate(final String regNo, final String regDate, final String brDob)
			throws ClassNotFoundException, LinkageError {
		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.bnd.service.PrintBNDCertificateServiceImpl";
		Long orgId = 1L;
		/*
		 * String srcPath = Filepaths.getfilepath() + MainetConstants.PDFFOLDERNAME +
		 * MainetConstants.FILE_PATH_SEPARATOR + applicationNo +
		 * MainetConstants.PDF_EXTENSION;
		 */
		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);
		final Method method = ReflectionUtils.findMethod(clazz, "generateDigiLockerBirthCertificate",
				new Class[] { String.class, String.class, String.class, Long.class });
		Object obj = (Object) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
				new Object[] { regNo, regDate, brDob, orgId });

		// Safely cast and return if it's a byte[]
		if (obj instanceof byte[]) {
			return (byte[]) obj;
		} else {
			throw new IllegalStateException("Expected byte[] but got " + (obj != null ? obj.getClass() : "null"));
		}
	}
		


	public static String convertToSingleLine(String xml) {

		// Remove all line breaks and extra whitespaces between tags
		xml = xml.replaceAll("\\s*\\n\\s*", "").trim();

		// Return the final one-line Java string
		return xml;
	}
	
	public PullURIResponse DeathProcessRequest(PullURIRequest request) {
	    LOGGER.info("Method Started processRequest....");

	    PullURIResponse response = new PullURIResponse();
	    ResponseStatus status = new ResponseStatus();
	    DocDetailsResponse docDetails = new DocDetailsResponse();
	    
	    
	    status.setStatus("1"); // Default success
	    status.setValue("1");
	    status.setTs(request.getTs());
	    status.setTxn(request.getTxn());

	    try {
	        String deathDocType = request.getDocDetails().getDocType();
	        String base64String = null;
	        String formattedUdf2 = null;
	        String formattedUdf3 = null;
	        String formattedDrDod = null;
	        String formattedDrRegDate = null;
	        Object[] deathCertificateData = null;
	        String age=null;

	        Person person1 = new Person();
	        Person person2 = new Person();

	        // Validate and format UDF fields
	        if (request.getDocDetails().getUdf1() != null &&
	            request.getDocDetails().getUdf2() != null &&
	            request.getDocDetails().getUdf3() != null) {

	            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	            formattedUdf2 = LocalDate.parse(request.getDocDetails().getUdf2(), inputFormatter).format(outputFormatter);
	            formattedUdf3 = LocalDate.parse(request.getDocDetails().getUdf3(), inputFormatter).format(outputFormatter);

	            deathCertificateData = citizenDashboardGraphServiceImpl.getDeathRegCertificateData(
	                    request.getDocDetails().getUdf1(), formattedUdf2, formattedUdf3);
	        } else {
	            throw new IllegalArgumentException("UDF fields missing.");
	        }

	        if (deathCertificateData == null || deathCertificateData.length == 0) {
	            throw new IllegalStateException("No death certificate data found.");
	        }

	        // Populate Person and metadata
	        String gender = safeString(deathCertificateData[6]);
	        LookUp lookup = CommonMasterUtility.getNonHierarchicalLookUpObjectByPrefix(Long.valueOf(gender), 1L, GENDER);
	        person1.setName(safeString(deathCertificateData[3]));
	        person1.setGender(lookup.getDescLangFirst());

	        String rawDod = safeString(deathCertificateData[4]);
	        String rawRegDate = safeString(deathCertificateData[2]);

	        formattedDrDod = formatDate(rawDod);
	        formattedDrRegDate = formatDate(rawRegDate);

	        person1.setDod(formattedDrDod);
	        person2.setDeceasedRelativeName(safeString(deathCertificateData[12]));
	        person2.setDeathPlace(safeString(deathCertificateData[7]));

	        IssuedTo issuedTo = new IssuedTo();
	        issuedTo.setPersons(Arrays.asList(person1, person2));
	        docDetails.setIssuedTo(issuedTo);
	        docDetails.setUri("in.gov.kdmc-" + deathDocType + "-" + safeString(deathCertificateData[5]));

	        // Get death certificate file
	        LOGGER.error("UDF1 from digilocker: " + request.getDocDetails().getUdf1());
	        byte[] digiDeathCertificate = getDeathCertificate(
	                request.getDocDetails().getUdf1(), formattedUdf2, formattedUdf3);
	        base64String = Base64.getEncoder().encodeToString(digiDeathCertificate);
	        docDetails.setDocContent(base64String);
	        if (deathCertificateData[11] != null) {
	            age = String.valueOf(((Number) deathCertificateData[11]).intValue());
	        }

	        // Create metadata
	        String metadataXml = "<Certificate "
	                + "language=\"\" " + "name=\"Death Certificate\" " + "type=\"DTCER\" " + "number=\"\" "
	                + "prevNumber=\"\" " + "serialNumber=\"" + safeString(deathCertificateData[5]) + "\" "
	                + "issuedAt=\"\" " + "issueDate=\"\" " + "validFromDate=\"\" " + "expiryDate=\"\" "
	                + "status=\"\">"

	                + "<IssuedBy>"
	                + "<Organization name=\"KDMC\" code=\"1\" tin=\"\" uid=\"\" type=\"\">"

	                + "<Address type=\"Official\" "
	                + "line1=\"Kalyan Dombivli Municipal Corporation, Shankarrao Chowk\" "
	                + "line2=\"Waidhavni Kalyan West\" house=\"\" landmark=\"Shivaji Chowk\" "
	                + "locality=\"Kalyan,west\" vtc=\"\" district=\"Thane\" pin=\"421301\" "
	                + "state=\"Maharashtra\" country=\"India\"/>"

	                + "</Organization>"
	                + "</IssuedBy>"

	                + "<IssuedTo>"

	                + "<Person uid=\"\" title=\"\" name=\"" + safeValue(person1.getName()) + "\" "
	                + " dod=\"" + safeValue(person1.getDod()) + "\" "
	                + "age=\"" + safeValue(age) + "\" "
	                + "swd=\"\" swdIndicator=\"\" motherName=\"" + safeValue(deathCertificateData[13]) + "\" "
	                + "gender=\"" + safeValue(person1.getGender()) + "\" "
	                + "maritalStatus=\"\" relationWithHof=\"\" disabilityStatus=\"\" category=\"\" religion=\"\" "
	                + "phone=\"\" email=\"\">"

	                + "<Address type=\"Permanent\" "
	                + "line1=\"" + safeValue(deathCertificateData[14]) + "\" "
	                + "line2=\"\" house=\"\" landmark=\"\" locality=\"\" vtc=\"\" "
	                + "district=\"Thane\" pin=\"\" state=\"Maharashtra\" country=\"India\"/>"

	                + "<Photo format=\"\"></Photo>"

	                + "</Person>"
	                + "</IssuedTo>"

	                + "<CertificateData registrationNo=\"" + safeValue(deathCertificateData[1]) + "\" "
	                + "registrationDate=\"" + safeValue(formattedDrRegDate) + "\" "
	                + "placeOfDeath=\"" + safeValue(deathCertificateData[7]) + "\" "
	                + "deathAddress=\"" + safeValue(deathCertificateData[8]) + "\" "
	                + "fatherOrHusbandName=\"" + safeValue(person2.getDeceasedRelativeName()) + "\">"
	                + "</CertificateData>"

	                + "</Certificate>";


	        String base64Metadata = Base64.getEncoder().encodeToString(metadataXml.getBytes(StandardCharsets.UTF_8));
	        docDetails.setDataContent(base64Metadata);
	        
	        status.setResponseCode("200");

	    } catch (Exception e) {
	        LOGGER.error("Exception occurred while processing DeathProcessRequest: ", e);
	        
	        // Set failure response
	        status.setStatus("0");
	        status.setValue("0");
	        status.setResponseCode("400");
	        

	        String fallbackText = "Document Not Found";
	        docDetails.setDocContent(fallbackText);
	        docDetails.setDataContent(Base64.getEncoder().encodeToString("".getBytes(StandardCharsets.UTF_8)));

	        response.setDocDetails(docDetails);
	        response.setResponseStatus(status);
	        

	        // Instead of returning 200 OK, throw exception with 400 BAD_REQUEST
	        return response;
	    }

	    // Final success response
	    response.setDocDetails(docDetails);
	    response.setResponseStatus(status);
	    LOGGER.info("Method End processRequest....");
	    return response;

	}

	private String formatDate(String rawDate) {
	    if (rawDate == null || rawDate.isEmpty()) return "";
	    try {
	        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
	        return output.format(input.parse(rawDate));
	    } catch (Exception e) {
	        LOGGER.error("Date formatting error: ", e);
	        return "";
	    }
	}
	
	private String safeString(Object obj) {
	    return obj != null ? obj.toString() : "";
	}
	
	private static String safeValue(Object obj) {
	    return obj == null ? "NA" : obj.toString();
	}
	
	private static byte[] getDeathCertificate(final String regNo, final String regDate, final String brDod)
			throws ClassNotFoundException, LinkageError {
		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.bnd.service.PrintBNDCertificateServiceImpl";
		Long orgId = 1L;
		/*
		 * String srcPath = Filepaths.getfilepath() + MainetConstants.PDFFOLDERNAME +
		 * MainetConstants.FILE_PATH_SEPARATOR + applicationNo +
		 * MainetConstants.PDF_EXTENSION;
		 */
		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);
		final Method method = ReflectionUtils.findMethod(clazz, "generateDigiLockerDeathCertificate",
				new Class[] { String.class, String.class, String.class, Long.class });
		Object obj = (Object) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
				new Object[] { regNo, regDate, brDod, orgId });

		// Safely cast and return if it's a byte[]
		if (obj instanceof byte[]) {
			return (byte[]) obj;
		} else {
			throw new IllegalStateException("Expected byte[] but got " + (obj != null ? obj.getClass() : "null"));
		}
	}
	
	@RequestMapping(value = "/propertyCertiPull-uri", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
	public ResponseEntity<PullURIResponse> propertyCertiPullUri(HttpServletRequest servletRequest,
			@RequestHeader("x-digilocker-hmac") String receivedHmac) throws JAXBException {
		String xmlString = "";
		boolean isValid = false;
		PullURIRequest request = null;
		String rawXml = null;

		try {
			// 1. Read raw XML as String
			StringBuilder xmlBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					xmlBuilder.append(line).append("\n");
				}
			}
			rawXml = xmlBuilder.toString();

			// 3. Unmarshal XML into Java object
			JAXBContext jaxbContext = JAXBContext.newInstance(PullURIRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			request = (PullURIRequest) unmarshaller.unmarshal(new StringReader(rawXml));

			LOGGER.info("Incoming Property Certificate hamac from API: " + xmlString);

			// 5. Validate HMAC
			isValid = validateHMAC(rawXml, receivedHmac, API_KEY);

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("Incoming hamac from API: " + receivedHmac);

		if (!isValid) {
			LOGGER.info("Unauthorized Request... ");
			PullURIResponse pullURIResponse = new PullURIResponse();
			return new ResponseEntity<>(pullURIResponse, HttpStatus.UNAUTHORIZED);
		}

		// 6. Process business logic
		PullURIResponse response = propertyProcessRequest(request);

		if (StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "400")) {

			return ResponseEntity.badRequest().body(response);
		}
		
		try {
		    if (response != null 
		            && response.getResponseStatus() != null 
		            && StringUtils.equalsIgnoreCase(response.getResponseStatus().getResponseCode(), "200")) {

		        LOGGER.info("Started Saving Property Certificate Data For Digilocker..");

		        DigilockerCertificateEntity digilockerCertificateEntity = new DigilockerCertificateEntity();

		        // Store current timestamp in DB-friendly format (yyyy-MM-dd HH:mm:ss)
		        digilockerCertificateEntity.setLmodDate(new java.sql.Timestamp(System.currentTimeMillis()));

		        digilockerCertificateEntity.setCertificateType("PROPERTY ASSESSMENT CERTIFICATE");
		        digilockerCertificateEntity.setOrgId(1L);

		        // Save request JSON if present
		        if (StringUtils.isNotBlank(rawXml)) {
		            digilockerCertificateEntity.setDigilockerRequestJson(rawXml);
		        }

		        // Save URI safely if available
		        if (response.getDocDetails() != null 
		                && StringUtils.isNotBlank(response.getDocDetails().getUri())) {
		            digilockerCertificateEntity.setuDF1(response.getDocDetails().getUri());
		        }

		        digilockerCertificateRepository.save(digilockerCertificateEntity);

		        LOGGER.info("End Saving Property Certificate Data For Digilocker..");

		    } else {
		        LOGGER.warn("Response is null or response code is not 200. Skipping Property Certificate save.");
		    }
		} catch (Exception e) {
		    LOGGER.error("Exception while saving Property Certificate Data For Digilocker", e);
		}

		
		return ResponseEntity.ok(response);
	}
	
	public PullURIResponse propertyProcessRequest(PullURIRequest request) {
	    LOGGER.info("Method Started propertyProcessRequest....");

	    PullURIResponse response = new PullURIResponse();
	    ResponseStatus status = new ResponseStatus();
	    DocDetailsResponse docDetails = new DocDetailsResponse();
	    Boolean isWholeProperty= false;

	    status.setStatus("1"); // Default success
	    status.setValue("1");
	    status.setTs(request.getTs());
	    status.setTxn(request.getTxn());

	    try {
	        String propertyDocType = request.getDocDetails().getDocType();
	        String digiPropertyCertificate = null;
	        Object[] propertyCertificateData = null;

	        Person person1 = new Person();

	        // Handle UDF2 defaulting
	        if (request.getDocDetails().getUdf2() == null 
	            || request.getDocDetails().getUdf2().equals(StringUtils.EMPTY)
	            || StringUtils.equals(request.getDocDetails().getUdf2(), MainetConstants.ZERO)) {
	            request.getDocDetails().setUdf2("X");
	            isWholeProperty=true;
	        }

	        // Fetch certificate data
	        if (request.getDocDetails().getUdf1() != null && request.getDocDetails().getUdf2() != null) {
	            propertyCertificateData = citizenDashboardGraphServiceImpl.getDigilockerPropertyCertificate(
	                    request.getDocDetails().getUdf1(),
	                    request.getDocDetails().getUdf2()
	            );
	        } else {
	            throw new IllegalArgumentException("UDF fields missing.");
	        }

	        // Validate fetched data
	        if (propertyCertificateData == null || propertyCertificateData.length == 0) {
	            throw new IllegalStateException("No property certificate data found.");
	        }

	        // Populate Person object
	        String applicantName = propertyCertificateData[2] != null ? propertyCertificateData[2].toString() : null;
	        person1.setApplicantName(applicantName);

	        String rawAppliDate = propertyCertificateData[1] != null ? propertyCertificateData[1].toString() : null;
	        person1.setApplicationDate(rawAppliDate);

	        person1.setApplicationNo(
	                propertyCertificateData[11] != null ? propertyCertificateData[11].toString() : null
	        );

	        IssuedTo issuedTo = new IssuedTo();
	        issuedTo.setPersons(Arrays.asList(person1));
	        docDetails.setIssuedTo(issuedTo);
	        docDetails.setUri("in.gov.kdmc-" + propertyDocType + "-" + safeValue(propertyCertificateData[12]));

	        // Fetch actual certificate file
	        LOGGER.error("Property No(UDF1) from digilocker: " + request.getDocDetails().getUdf1());
	        LOGGER.error("Flat No(UDF2) from digilocker: " + request.getDocDetails().getUdf2());

	        /*digiPropertyCertificate = fetchPdf(
	                request.getDocDetails().getUdf1(),
	                request.getDocDetails().getUdf2()
	        );*/
	        
	        byte[] pdfBytes = generateAssessmentCertificate(propertyCertificateData,isWholeProperty);
			String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);
	        
	        digiPropertyCertificate = pdfBase64;

	        if (digiPropertyCertificate == null) {
	            throw new IllegalStateException("Property certificate file not found.");
	        }
	        
	        docDetails.setDocContent(digiPropertyCertificate);

	        // Metadata XML
	        String metadataXml = "<Certificate "
	        	    + "language=\"\" name=\"Property Assessment Certificate\" type=\"PRCER\" number=\"\" "
	        	    + "prevNumber=\"\" serialNumber=\"" + safeString(propertyCertificateData[12]) + "\" "
	        	    + "issuedAt=\"\" issueDate=\"\" validFromDate=\"\" expiryDate=\"\" status=\"\">"

	        	    + "<IssuedBy>"
	        	    + "<Organization name=\"KDMC\" code=\"1\" tin=\"\" uid=\"\" type=\"\">"

	        	    + "<Address type=\"Official\" "
	        	    + "line1=\"Kalyan Dombivli Municipal Corporation, Shankarrao Chowk\" "
	        	    + "line2=\"Waidhavni Kalyan West\" house=\"\" landmark=\"Shivaji Chowk\" "
	        	    + "locality=\"Kalyan,west\" vtc=\"\" district=\"Thane\" pin=\"421301\" "
	        	    + "state=\"Maharashtra\" country=\"India\"/>"

	        	    + "</Organization>"
	        	    + "</IssuedBy>"

	        	    + "<IssuedTo>"

	        	    + "<Person uid=\"\" title=\"\" name=\"" + safeValue(propertyCertificateData[7]) + "\" "
	        	    + "dob=\"\" age=\"\" swd=\"\" swdIndicator=\"\" motherName=\"\" "
	        	    + "gender=\"\" maritalStatus=\"\" relationWithHof=\"\" disabilityStatus=\"\" category=\"\" religion=\"\" "
	        	    + "phone=\"" + safeValue(propertyCertificateData[17]) + "\" email=\"\">"

	        	    + "<Address type=\"Permanent\" "
	        	    + "line1=\"" + safeValue(propertyCertificateData[10]) + "\" "
	        	    + "line2=\"\" house=\"\" landmark=\"\" locality=\"\" vtc=\"\" "
	        	    + "district=\"Thane\" pin=\"\" state=\"Maharashtra\" country=\"India\"/>"

	        	    + "<Photo format=\"\"></Photo>"

	        	    + "</Person>"
	        	    + "</IssuedTo>"

	        	    + "<CertificateData applicationNo=\"" + safeValue(propertyCertificateData[11]) + "\" "
	        	    + "applicantName=\"" + safeValue(propertyCertificateData[2]) + "\" "
	        	    + "applicationDate=\"" + safeValue(propertyCertificateData[1]) + "\" "
	        	    + "usageType=\"" + safeValue(propertyCertificateData[15]) + "\">"
	        	    + "</CertificateData>"

	        	    + "</Certificate>";



	        String base64Metadata = Base64.getEncoder()
	                .encodeToString(metadataXml.getBytes(StandardCharsets.UTF_8));
	        docDetails.setDataContent(base64Metadata);

	    } catch (Exception e) {
	        LOGGER.error("Exception occurred while processing propertyProcessRequest: ", e);

	        // Failure response
	        status.setStatus("0");
	        status.setValue("0");
	        status.setResponseCode("400");

	        docDetails.setDocContent("Document Not Found");
	        docDetails.setDataContent(Base64.getEncoder()
	                .encodeToString("".getBytes(StandardCharsets.UTF_8)));

	        response.setDocDetails(docDetails);
	        response.setResponseStatus(status);
	        return response;
	    }

	    // Success response
	    status.setResponseCode("200");
	    response.setDocDetails(docDetails);
	    response.setResponseStatus(status);

	    LOGGER.info("Method End propertyProcessRequest....");
	    return response;
	}
	
	private static String getPropertyAssCertificate(final String appId, final String flatNo)
	        throws ClassNotFoundException, LinkageError {
		LOGGER.error("getPropertyAssCertificate method started....");
	    Long orgId = 1L;
	    String url = null;

	    if (flatNo != null || StringUtils.isNotEmpty(flatNo) || !StringUtils.equals(flatNo, MainetConstants.ZERO)) {
	        url = ServiceEndpoints.Prop_Assessemnt_Report_Digilocker_Url
	                + "=RP_AssessmentCertificate_SKDCL_English.rptdesign&RP_OrgID=" + orgId
	                + "&RP_AppID=" + appId
	                + "&RP_FlatNo=" + flatNo
	                + "&__format=pdf";

	        LOGGER.info("Property URL For Individual:- " + url);
	    } 

	    String base64EncodedPdf = null;

	    try {
	        URL reportUrl = new URL(url);

	        if (reportUrl.openConnection().getContentType() != null &&
	            reportUrl.openConnection().getContentType().equalsIgnoreCase("application/pdf")) {

	            byte[] pdfBytes = IOUtils.toByteArray(reportUrl.openConnection().getInputStream());
	            base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
	        }

	    } catch (FileNotFoundException e) {
	        LOGGER.info("PDF not found at URL: " + e);
	    } catch (IOException ioe) {
	        LOGGER.info("IOException while reading the PDF: " + ioe);
	    } catch (Exception ex) {
	    	LOGGER.info("IOException while reading the PDF: " + ex);
	    }

	    LOGGER.info("Property Base64 PDF: " + base64EncodedPdf);
	    LOGGER.error("getPropertyAssCertificate method End.... ");
	    return base64EncodedPdf;
	}
	
	public String fetchPdf(final String appId, final String flatNo) {
         
	LOGGER.error("fetchPdf method started....");
	Long orgId = 1L;
	String apiUrl=null;	
	if (StringUtils.isNotEmpty(flatNo) && !StringUtils.equals(flatNo, MainetConstants.ZERO)) {
		apiUrl = ServiceEndpoints.Prop_Assessemnt_Report_Digilocker_Url
					+ "=RP_AssessmentCertificate_SKDCL_English.rptdesign&RP_OrgID=" + orgId + "&RP_AppID=" + appId
					+ "&RP_FlatNo=" + flatNo + "&__format=pdf";
 
			LOGGER.info("Property URL For Individual:- " + apiUrl);
		}

	    LOGGER.error("url started to creating the pdf file..");
 
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            LOGGER.error("Started to api call connection.setRequestMethod(\"GET\");");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/pdf");
            String base64EncodedPdf = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream in = connection.getInputStream();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
 
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

					byte[] pdfBytes = out.toByteArray();

					LOGGER.error("Started Encode as Base64 String" + pdfBytes);
					base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

					System.out.println("Downloaded PDF size: " + pdfBytes.length + " bytes");
					
					LOGGER.error("ByteCode: " + base64EncodedPdf);
					LOGGER.error("fetchPdf method Ended successfully....");
					return base64EncodedPdf;
                }
            } else {
            	LOGGER.error("HTTP Connection/Conectivity Issue..");
                throw new RuntimeException("Failed: HTTP " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching PDF", e);
        }
    }


	
	/*@RequestMapping(value = "/propertyAssessmentCertficate-PDF",consumes = MediaType.ALL_VALUE, method = RequestMethod.POST)
	public ResponseEntity<PullURIResponse> propertyAssessmentPDFCertificate(HttpServletRequest servletRequest,
			@RequestHeader("x-digilocker-hmac") String receivedHmac) throws JAXBException{
		
		PullURIResponse p1= new PullURIResponse();
		
		byte[] pdfBytes = generateAssessmentCertificate();
		String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);
		
	    DocDetailsResponse docDetails = new DocDetailsResponse();
	    
	    docDetails.setDocContent(pdfBase64);
	    
		p1.setDocDetails(docDetails);
		
		return ResponseEntity.ok(p1);
		
	}*/
	
	
	public byte[] generateAssessmentCertificate(Object[] propertyCertificateData,boolean isWholeProperty) {
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        // Initialize PDF writer and document
	    	
	    	
	    	LOGGER.error("Method started generateAssessmentCertificate.......");
	    	
	    	String ward = propertyCertificateData[5] != null ? propertyCertificateData[5].toString() : "";
	    	
	    	String zone = propertyCertificateData[6] != null ? propertyCertificateData[6]+" ".toString(): "";
	    	
	    	String serialNo = propertyCertificateData[12] != null ? propertyCertificateData[12].toString() : "";
	    	
			String contatenatedSerialNo = MainetConstants.SLASH + ward + MainetConstants.SLASH + zone
					+ MainetConstants.SLASH + MainetConstants.DEPT_SHORT_NAME.CFC_CENTER +MainetConstants.SLASH + serialNo;
	    	
	    	String toDayDate = propertyCertificateData[4] != null ? ": "+propertyCertificateData[4].toString() : "";
	    	
	    	String appId = propertyCertificateData[11] != null ? ": "+propertyCertificateData[11].toString() : "";
	    	
	    	String applicantName = propertyCertificateData[2] !=null ? propertyCertificateData[2].toString() : "";
	    	
	    	String applicationDate = propertyCertificateData[1] != null ? propertyCertificateData[1].toString() : "";
	    	
	    	String onwnerName  = propertyCertificateData[7] != null ? propertyCertificateData[7].toString() : "";
	    	
	    	String address = propertyCertificateData[10] != null ? propertyCertificateData[10].toString() : "";
	    	
	    	String propertyNo = propertyCertificateData[0] != null ? propertyCertificateData[0].toString() : "";
	    	
	    	String arv = propertyCertificateData[16] != null ? propertyCertificateData[16].toString() : "";
	    	
	    	String usageType = propertyCertificateData[15] != null ? propertyCertificateData[15].toString() : "";
	    	
	    	String flatNo = propertyCertificateData[13] != null ? propertyCertificateData[13].toString() : "";
	    	
	    	LOGGER.error("All dynamic values assigned.......");
	    	
	        PdfWriter writer = new PdfWriter(outputStream);
	        PdfDocument pdfDoc = new PdfDocument(writer);
	        Document document = new Document(pdfDoc);

	        // Load ARIALUNICODE font
	        String ARIALUNICODE = ApplicationSession.getInstance().getMessage("arialUniCode.path");
	        FontProgram fontProgram = FontProgramFactory.createFont(ARIALUNICODE);
	        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
	        document.setFont(font);
	        document.setMargins(40f, 40f, 40f, 40f); // increase margins to avoid overlapping border
	       
	        // ------------------- HEADER -------------------
	        Paragraph header = new Paragraph("Kalyan Dombivli Municipal Corporation")
	                .setFont(font)
	                .setFontSize(14f)
	                .setBold()
	                .setTextAlignment(TextAlignment.CENTER);
	        document.add(header);


	        document.add(new Paragraph("\n")); // spacer
	        
	        
	     // ------------------- OUTWARD / DATE / APP NO BLOCK -------------------
        
	        Table table1 = new Table(new float[]{1, 2});
	        table1.setWidth(UnitValue.createPercentValue(30));

	        PdfFont font1 = PdfFontFactory.createFont(StandardFonts.HELVETICA);
	        float fontSize = 10f;
	        
	        // Add rows to the table - first column right-aligned, second column left-aligned
	        table1.addCell(new Cell().add(new Paragraph(new Text("Outward No.").setBold())).setFontSize(fontSize)
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.RIGHT));

	        table1.addCell(new Cell().add(new Paragraph(contatenatedSerialNo).setFontSize(fontSize))
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.LEFT));

	        table1.addCell(new Cell().add(new Paragraph(new Text("Date").setBold())).setFontSize(fontSize)
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.RIGHT));

	        table1.addCell(new Cell().add(new Paragraph(toDayDate).setFontSize(fontSize))
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.LEFT));

	        table1.addCell(new Cell().add(new Paragraph(new Text("Application No").setBold())).setFontSize(fontSize)
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.RIGHT));

	        table1.addCell(new Cell().add(new Paragraph(appId).setFontSize(fontSize))
	                   .setBorder(Border.NO_BORDER)
	                   .setTextAlignment(TextAlignment.LEFT));

	        // To position the entire table on the right side of the page
	        table1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
	        document.add(table1);
	        
/*	        document.add(details);
*/	        
	        document.add(new Paragraph("\n"));

	       
	        
	     // Subject (whole bold)
	        Paragraph subject = new Paragraph("Subject :- Property Assessment Certificate")
	                .setFont(font)
	                .setFontSize(10f)
	                .setBold()
	                .setTextAlignment(TextAlignment.CENTER);

	        // Reference (only word bold)
	        Text refLabel = new Text("Reference :- ").setFont(font).setFontSize(10f).setBold();
	        Text refValue = new Text(applicantName+" Date of " + applicationDate +" of Application.")
	                .setFont(font).setFontSize(10f);
	        Paragraph reference = new Paragraph().add(refLabel).add(refValue)
	                .setTextAlignment(TextAlignment.CENTER);

	        // Add blank line
	        document.add(new Paragraph("\n"));

	        // Certificate (bold + underline + center)
	        Paragraph certificate = new Paragraph("Certificate")
	                .setFont(font)
	                .setFontSize(12f)
	                .setBold()
	                .setUnderline()
	                .setTextAlignment(TextAlignment.CENTER)
	                .setMarginTop(10f);   // extra space before

	        // Add to document in order
	        document.add(subject);
	        document.add(reference);
	        document.add(new Paragraph("\n"));
	        document.add(certificate);
	        
	     // Now add the logo at top-left corner
	        PdfPage firstPage = pdfDoc.getFirstPage();
	        PdfCanvas pdfCanvas = new PdfCanvas(firstPage);
	        ImageData leftLogo = ImageDataFactory.create(
	                filenetPath + ApplicationSession.getInstance().getMessage("leftlogo")
	        );
	        Image logo = new Image(leftLogo).scaleAbsolute(70f, 95f);

	        // Position relative to top-left of the page
	        float x = firstPage.getPageSize().getLeft() + 40;
	        float y = firstPage.getPageSize().getTop() - 120;
	        new Canvas(pdfCanvas, pdfDoc, firstPage.getPageSize())
	                .add(logo.setFixedPosition(x, y));
	        

	        // ------------------- CERTIFICATE BODY -------------------
			
	        Paragraph certPara = new Paragraph()
	                .setFont(font)
	                .setFontSize(10f)
	                .setTextAlignment(TextAlignment.LEFT)
	                .setFirstLineIndent(30f);

	        if(isWholeProperty){
	        certPara.add(new Text("It is hereby certified that the Municipal Corporation Property No. "));

	        certPara.add(new Text(propertyNo).setBold());

	        certPara.add(new Text(" registered in the names of Shri./Mrs. "));

	        certPara.add(new Text(onwnerName).setBold());

	        certPara.add(new Text(" and the annual taxable value of the said property is Rs. "));

	        certPara.add(new Text(arv).setBold());

	        certPara.add(new Text(" and the Usage type is "));

	        certPara.add(new Text(usageType).setBold());

	        certPara.add(new Text(". The said certificate is being issued as per your application dated "));

	        certPara.add(new Text(applicationDate).setBold());

	        certPara.add(new Text(". It is being given according to the application."));

	        }else{
	        	certPara.add(new Text("It is hereby certified that the Municipal Corporation Property No. "));

		        certPara.add(new Text(propertyNo).setBold());

		        certPara.add(new Text(",Flat no. / Shop no. "));

		        certPara.add(new Text(flatNo).setBold());
		        
		        certPara.add(new Text(" This income belongs to Mr./Ms. "));
		        
		        certPara.add(new Text(onwnerName).setBold());
		        
		        certPara.add(new Text("Their names are registered and the annual taxable value of the said property is Rs. "));

		        certPara.add(new Text(arv).setBold());
		        
		        certPara.add(new Text(" and the Usage type is "));
		        
		        certPara.add(new Text(usageType).setBold());

		        certPara.add(new Text(" recorded.The certificate is dated "));

		        certPara.add(new Text(applicationDate).setBold());

		        certPara.add(new Text(".It is being given according to the application."));
	        }
	        document.add(certPara);

		
	        document.add(new Paragraph("\n\n"));

	        // ------------------- FOOTER -------------------
	     // Add spacing before the bottom section
	        document.add(new Paragraph("\n\n"));

	        // Add "sd/-" and position details
	        /*Paragraph signatory = new Paragraph()
	            .setFont(font)
	            .setFontSize(10f)
	            .setTextAlignment(TextAlignment.RIGHT)
	            .add("sd/-\n\n\n")
	            .add("Tax assessor and collector\n")
	            .add("Kalyan Dombivli Municipal Corporation\n\n");
	        document.add(signatory);*/
	        
	        
	        Paragraph signatureLine = new Paragraph("sd/-")
	        	    .setFont(font)
	        	    .setFontSize(10f)
	        	    .setTextAlignment(TextAlignment.RIGHT)
	        	    .setMarginRight(40f);  // Adjust this value for more/less space

	        	Paragraph designation = new Paragraph("Tax assessor and collector")
	        	    .setFont(font)
	        	    .setFontSize(10f)
	        	    .setTextAlignment(TextAlignment.RIGHT);

	        	Paragraph department = new Paragraph("Kalyan Dombivli Municipal Corporation")
	        	    .setFont(font)
	        	    .setFontSize(10f)
	        	    .setTextAlignment(TextAlignment.RIGHT);

	        	document.add(signatureLine);
	        	document.add(designation);
	        	document.add(department);
	        
	     

	        // Add "From" section
	        Paragraph fromSection = new Paragraph()
	            .setFont(font)
	            .setFontSize(10f)
	            .setTextAlignment(TextAlignment.LEFT)
	            .add("From,\n")
	            .add(onwnerName+".\n")
	            .add(address+"\n\n");
	        document.add(fromSection);
	        
	        // Add computer generated notice
	        Paragraph computerGenerated = new Paragraph()
	            .setFont(font)
	            .setFontSize(8f) // Smaller font size for this line
	            .setTextAlignment(TextAlignment.CENTER)
	            .setFontColor(ColorConstants.GRAY)
	            .add("This is computer generated document, hence requires no signature");
	        document.add(computerGenerated);

	        // ------------------- BORDER AROUND PAGE -------------------
	        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
	        Rectangle pageSize = pdfDoc.getFirstPage().getPageSize();
	        canvas.setStrokeColor(ColorConstants.BLACK)
	              .setLineWidth(2f) // border thickness
	              .rectangle(
	                  pageSize.getLeft() + 20,   // left margin
	                  pageSize.getBottom() + 20, // bottom margin
	                  pageSize.getWidth() - 40,  // width
	                  pageSize.getHeight() - 40  // height
	              )
	              .stroke();

	        document.close();
	        LOGGER.error("Method ended generateAssessmentCertificate.......");
	        return outputStream.toByteArray();

	    } catch (Exception e) {
	        LOGGER.error("Error generating Assessment Certificate PDF: ", e);
	        return new byte[0];
	    }
	}


}
