package com.abm.mainet.common.integration.dms.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dashboard.domain.DuplicateReceiptBpmsEntity;
import com.abm.mainet.common.dashboard.service.CitizenDashboardGraphServiceImpl;
import com.abm.mainet.common.integration.dms.domain.BlockChainCertificateEntity;
import com.abm.mainet.common.integration.dms.domain.BpmsIntegrationEntity;
import com.abm.mainet.common.integration.dms.domain.DigilockerCertificateEntity;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;
import com.abm.mainet.common.integration.dms.dto.BpmsApiResponseDTO;
import com.abm.mainet.common.integration.dms.dto.BpmsRequestDto;
import com.abm.mainet.common.integration.dms.dto.BpmsResponseDto;
import com.abm.mainet.common.integration.dms.dto.BpmsSummaryPageDto;
import com.abm.mainet.common.integration.dms.dto.BpmsUpdateRequestDto;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.dto.OwnerDetailsDTO;
import com.abm.mainet.common.integration.dms.dto.ProjectDetailsDTO;
import com.abm.mainet.common.integration.dms.dto.PullURIRequest;
import com.abm.mainet.common.integration.dms.dto.PullURIResponse;
import com.abm.mainet.common.integration.dms.dto.ResponseDataDTO;
import com.abm.mainet.common.integration.dms.repository.BlockChainRepository;
import com.abm.mainet.common.integration.dms.repository.BpmsIntegrationRepository;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.SSLContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.integration.dms.dto.BPMSReceiptDto;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */

@Service
public class BpmsIntegrationServiceImpl implements IBpmsIntegrationService {

	private static final Logger LOGGER = Logger.getLogger(BpmsIntegrationServiceImpl.class);

	@Value("${bpms.api.url}")
	private String apiUrl;

	@Value("${bpms.api.username}")
	private String username;

	@Value("${bpms.api.password}")
	private String password;

	@Value("${bpms.updateApi.url}")
	private String updateApiUrl;

	@Autowired
	private BpmsIntegrationRepository repository;
	
	@Value("${bpms.updateReceipt.Api}")
	private String bpmsReceiptApi;
	
	@Autowired
	private CitizenDashboardGraphServiceImpl citizenDashboardGraphServiceImpl;

	@SuppressWarnings("unchecked")
	public void fetchAndStoreUlbData() {

		// 1. Prepare RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<?> Response = null;
		String response;
		StringBuilder propIdBuilder = new StringBuilder();

		// 2. Prepare Headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + getBPMSBasicAuthUnameAndPw());

		// 3. Prepare Request DTO with required fields
		BpmsRequestDto requestDTO = new BpmsRequestDto();
		requestDTO.setLoc_id("446"); // required by API
		requestDTO.setShort_code("KDMCC"); // required by API

		// 4. Call API and get response as String

		Response = (ResponseEntity<String>) RestClient.callRestTemplateClientWithHeaders(requestDTO, apiUrl,
				HttpMethod.POST, String.class, headers);

		Object responseBody = Response.getBody();

		if (responseBody == null) {
			LOGGER.error("API returned null body");
			return;
		}

		// Convert Object to String
		String json = responseBody.toString();

		ObjectMapper mapper = new ObjectMapper();
		BpmsResponseDto responseDTO;

		try {
			responseDTO = mapper.readValue(json, BpmsResponseDto.class);
		} catch (Exception e) {
			LOGGER.error("Failed to parse API response", e);
			return;
		}

		if ("Success".equalsIgnoreCase(responseDTO.getStatus()) && responseDTO.getResponseData() != null) {

			List<BpmsIntegrationEntity> entityList = new ArrayList<>();

			for (BpmsResponseDto.ResponseData data : responseDTO.getResponseData()) {
				BpmsIntegrationEntity entity = new BpmsIntegrationEntity();
				entity.setProposalNumber(data.getProposal_number());
				entity.setProjectCode(data.getProject_code());
				entity.setCreatedDate(new Date());
				entity.setSucessFlag("N");
				entity.setDraftMode("N");
				entity.setUpdatedDate(new Date());
				entity.setWorkFlowInitiated("N");

				entityList.add(entity);

				// build comma-separated proposal numbers
				if (propIdBuilder.length() > 0) {
					propIdBuilder.append(",");
				}
				propIdBuilder.append(data.getProposal_number());
			}

			// final comma-separated value
			String propId = propIdBuilder.toString();
			
			List<String> proposalNumbers = Arrays.stream(propId.split(","))
			        .map(String::trim)
			        .collect(Collectors.toList());

			List<BpmsIntegrationEntity> save = repository.save(entityList);

			if (save.size() > 0 && StringUtils.isNotBlank(propId)) {

				LOGGER.info("Successfully saved API data into DB.");

				// logic of 2nd API call
				LOGGER.info("Calling API2- updateProposalFlagForPropertyTax.... ");

				boolean updateProposalFlagForPropertyTax = updateProposalFlagForPropertyTax(propId);

				if (updateProposalFlagForPropertyTax) {

					int updatedRows = repository.updateSuccessFlag(proposalNumbers);

					LOGGER.error("Updated rows count: {} " + updatedRows);
					
					LOGGER.info("Successfully saved  and Update API data into DB.");

				}
			}

		} else {
			LOGGER.error("API returned failure or empty data: " + responseBody);
		}
		
		boolean sendDailyReceiptToBpms = sendDailyReceiptToBpms();
		LOGGER.info("Receipt call is completed: " + sendDailyReceiptToBpms);

	}

	public static String getBPMSBasicAuthUnameAndPw() {
		ApplicationSession applicationSession = ApplicationSession.getInstance();
		String userName = applicationSession.getMessage("bpms.api.username");
		String password = applicationSession.getMessage("bpms.api.password");
		String authStr = userName + ":" + password;
		String tarsBase64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		return tarsBase64Creds;
	}

	@SuppressWarnings("unchecked")
	public boolean updateProposalFlagForPropertyTax(String propId) {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<?> Response = null;
		String response;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + getBPMSBasicAuthUnameAndPw());

		BpmsUpdateRequestDto updateRequestDTO = new BpmsUpdateRequestDto();
		updateRequestDTO.setLoc_id("446"); // required by API
		updateRequestDTO.setProp_id(propId); // required by API

		try {

			Response = (ResponseEntity<String>) RestClient.callRestTemplateClientWithHeaders(updateRequestDTO,
					updateApiUrl, HttpMethod.POST, String.class, headers);

			Response.getStatusCode();
			String responseBody = Response.getBody().toString();


			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(responseBody);

			if (jsonNode.has("status") && "Success".equalsIgnoreCase(jsonNode.get("status").asText())) {
				LOGGER.error("Api 2 Call Ended..");
				return true;
			}

		} catch (Exception e) {
			LOGGER.error("Failed to parse API response", e);
			return false;
		}
		return false;

	}
	
	
	
	@Transactional(readOnly = true)
	public List<BpmsSummaryPageDto> findBpmsData(String draftFlag) {
		LOGGER.error("Start Find BPMS Data.......");
	    List<BpmsIntegrationEntity> entityList = repository.findByDraftMode(draftFlag);
	    List<BpmsSummaryPageDto> dtoList = new ArrayList<>();

	    if (entityList == null || entityList.isEmpty()) {
	    	LOGGER.error("BpmsIntegrationEntity entityList is empty.......");
	        return dtoList;
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	    for (BpmsIntegrationEntity bpmsIntegrationEntity : entityList) {

	        if (bpmsIntegrationEntity == null) {
	            continue;
	        }

	        BpmsSummaryPageDto bpmsSummaryPageDto = new BpmsSummaryPageDto();

	        try {
	            String resJson = (String) bpmsIntegrationEntity.getRequestJson();

	            if (resJson != null && !resJson.isEmpty()) {

	                BpmsApiResponseDTO bpmsApiResponseDTO =
	                        mapper.readValue(resJson, BpmsApiResponseDTO.class);

	                if (bpmsApiResponseDTO != null
	                        && bpmsApiResponseDTO.getResponseData() != null
	                        && !bpmsApiResponseDTO.getResponseData().isEmpty()) {

	                    ResponseDataDTO responseData =
	                            bpmsApiResponseDTO.getResponseData().get(0);

	                    if (responseData.getOwnerDetails() != null
	                            && !responseData.getOwnerDetails().isEmpty()) {
	                    	
	                    	 LOGGER.error("Responce Data Fetch Successfully....");

	                        OwnerDetailsDTO owner =
	                                responseData.getOwnerDetails().get(0);
	                        
	                        LOGGER.error("Owner Name: " + owner.getOwner_name());
	                        LOGGER.error("Owner Address: " + owner.getOwner_address());

	                        if (owner != null) {
	                        	LOGGER.error("Start to set ownerName And OwnerAddress on summary Page");
	                            bpmsSummaryPageDto.setOwnerName(owner.getOwner_name());
	                            bpmsSummaryPageDto.setOwnerAddress(owner.getOwner_address());
	                            LOGGER.error("End to set ownerName And OwnerAddress on summary Page");
	                        }else {
	                        	LOGGER.error("owner Details is empty.......");
	                        }
	                        
	                        ProjectDetailsDTO project = responseData.getProjectDetails().get(0);
	                        if (project != null) {
	                        	LOGGER.error("Start to set Ward on summary Page");
	                        	bpmsSummaryPageDto.setWard(project.getAdministrative_ward());
	                        	LOGGER.error("End to set Ward on summary Page");
	                        }else {
	                        	LOGGER.error("Project Details Are Empty....");
	                        }
	                        
	                    }
	                }
	            }
	        } catch (Exception e) {
	             //Log & continue processing remaining records
	        	LOGGER.error("Error while parsing BPMS JSON for proposalNumber : {}" +
	                   bpmsIntegrationEntity.getProposalNumber(), e);
	        }

	        bpmsSummaryPageDto.setProposalNumber(
	                bpmsIntegrationEntity.getProposalNumber());

	        bpmsSummaryPageDto.setPropertyNo(
	                bpmsIntegrationEntity.getPropertyNo());

	        Date updateDate = bpmsIntegrationEntity.getUpdatedDate();
	        if (updateDate != null) {
	            String formattedDate = sdf.format(updateDate);
	            bpmsSummaryPageDto.setUpdatedDate(formattedDate);
	        }

	        dtoList.add(bpmsSummaryPageDto);
	    }
	    LOGGER.error("End Find BPMS Data.......");
	    
	    return dtoList;
	}
	
	@SuppressWarnings("unchecked")
	public boolean sendDailyReceiptToBpms() {

	    LOGGER.info("API-4 Receipt Call Started");

	    ApplicationSession applicationSession = ApplicationSession.getInstance();
	    
	    List<DuplicateReceiptBpmsEntity> bpmsIntegrationEntity = citizenDashboardGraphServiceImpl.getBpmsIntegrationEntity();

	    if (bpmsIntegrationEntity == null || bpmsIntegrationEntity.isEmpty()) {
	        LOGGER.info("No receipts found for BPMS sync");
	        return true;
	    }

	    
	    for (Object receipt : bpmsIntegrationEntity) {
	        // process each receipt
	    	LOGGER.info("List:" +receipt);
	    }
	    HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + getBPMSBasicAuthUnameAndPw());
	    
	    ObjectMapper mapper = new ObjectMapper();

	    int successCount = 0;
	    int failureCount = 0;

	    for (DuplicateReceiptBpmsEntity receipt : bpmsIntegrationEntity) {

	        try {
	            BPMSReceiptDto request = new BPMSReceiptDto();
	            request.setProposal_id(receipt.getPROPOSAL_NUMBER());
	            request.setReceipt_name("Property");
	            request.setLoc_id("446");
	            
	            String property_NUMBER = receipt.getPROPERTY_NUMBER();
	            String receiptNumber = String.valueOf(receipt.getRM_RCPTNO());
	            String formattedDate = dateFromat(receipt.getRM_DATE().toString());
	            String bpmsBirtUrl = applicationSession.getMessage("bpms_birt_receiptApi_url");
	            
	          
	            String birtURL = bpmsBirtUrl + "="
						+ "ReceiptReprintingReport_PropertyNoFlatNoIntegration.rptdesign&ULBName=1"
						+ "&PropertyNo=" + property_NUMBER + "&ReceiptDate=" + formattedDate + "&ReceiptNumber=" + receiptNumber + "&__format=pdf";
	            
	            
	            /*String birtURL = ServiceEndpoints.BIRT_REPORT_URL + "="
						+ "ReceiptReprintingReport_PropertyNoFlatNoIntegration.rptdesign&ULBName=1"
						+ "&PropertyNo=" + "D04012980000" + "&ReceiptDate=" + "03/12/2024" + "&ReceiptNumber=" + "323230&__format=pdf";*/
	            
	            LOGGER.error("BIRT URL:  " + birtURL);
	            String fetchPdfFromBirtAsBase64 = fetchPdfFromBirtAsBase64(birtURL);
	            LOGGER.error("PDF Bytes base64: " + fetchPdfFromBirtAsBase64);
	            request.setReceipt_file(fetchPdfFromBirtAsBase64);
	            ResponseEntity<String> response =
	                (ResponseEntity<String>) RestClient.callRestTemplateClientWithHeaders(
	                    request,
	                    bpmsReceiptApi,
	                    HttpMethod.POST,
	                    String.class,
	                    headers
	                );

	            

	            JsonNode jsonNode = mapper.readTree(response.getBody());

	            if ("Success".equalsIgnoreCase(jsonNode.path("status").asText())) {

	            	successCount++;
	            	
	            	int updateReceiptUrl = repository.updateReceiptUrl(birtURL,receipt.getPROPOSAL_NUMBER());
	            	
	            	LOGGER.error("URL updated successfully : " + updateReceiptUrl);
	            	
	                // update DB status here
	                // repository.updateReceiptFlag(receipt.getPROPOSAL_NUMBER());

	            } else {
	                failureCount++;
	                LOGGER.error("BPMS receipt rejected for proposal {} | Response={}" + receipt.getPROPOSAL_NUMBER());
	            }

	        } catch (Exception ex) {
	            failureCount++;
	            LOGGER.error("Exception while pushing receipt for proposal {}" + receipt.getPROPOSAL_NUMBER(), ex);
	            return false;
	        }
	    }

	    LOGGER.error("API-4 Completed | Success={} | Failed={}" + successCount + " | " + failureCount);

	    LOGGER.info("API-4 Receipt Call Ended"); 
	   
	    return true;
	}

	private String fetchPdfFromBirtAsBase64(String birtUrl) throws IOException {

	    // 1. Reachability check (DNS + TCP level)
	    if (!isUrlReachable(birtUrl)) {
	        throw new RuntimeException("BIRT URL is not reachable: " + birtUrl);
	    }

	    // 2. RestTemplate with timeout
	    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	    
	    RestTemplate restTemplate = new RestTemplate(factory);

	    HttpHeaders headers = new HttpHeaders();

	    HttpEntity<Void> entity = new HttpEntity<>(headers);

	    ResponseEntity<byte[]> response =
	            restTemplate.exchange(
	                    birtUrl,
	                    HttpMethod.GET,
	                    entity,
	                    byte[].class
	            );

	    // 3. Response validation
	    if (response.getStatusCode() != HttpStatus.OK ||
	        response.getBody() == null ||
	        response.getBody().length == 0) {

	        throw new RuntimeException(
	                "Failed to fetch PDF from BIRT. HTTP Status: " + response.getStatusCode()
	        );
	    }

	    // 4. Convert PDF bytes → Base64
	    return Base64.getEncoder().encodeToString(response.getBody());
	}
	
	private boolean isUrlReachable(String urlStr) {
	    HttpURLConnection connection = null;
	    try {
	        URL url = new URL(urlStr);
	        connection = (HttpURLConnection) url.openConnection();

	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Range", "bytes=0-10"); // lightweight GET
	        connection.setInstanceFollowRedirects(true);

	        int responseCode = connection.getResponseCode();
	        String contentType = connection.getContentType();

	        return (responseCode >= 200 && responseCode < 400)
	                && contentType != null;

	    } catch (Exception ex) {
	        LOGGER.error("BIRT URL not reachable: {}"+ urlStr + " " +ex);
	        return false;
	    } finally {
	        if (connection != null) {
	            connection.disconnect();
	        }
	    }
	}

	public String dateFromat(String date){
		
		
        SimpleDateFormat inputFormat =
	            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	    SimpleDateFormat outputFormat =
	            new SimpleDateFormat("dd/MM/yyyy");
	   
	    try {
			Date rm_DATE = inputFormat.parse(date);
			
			String formattedDate = outputFormat.format(rm_DATE);

			return formattedDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    
	    
		return "";
	}


}
