package com.abm.mainet.common.integration.dms.rest.ui.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dashboard.domain.ApplicationGranularStatusEntity;
import com.abm.mainet.common.dashboard.dto.ApplicationStatus;
import com.abm.mainet.common.dashboard.dto.AapleSarkarGranularStatus;
import com.abm.mainet.common.dashboard.dto.AapleSarkarStatusRequest;
import com.abm.mainet.common.dashboard.dto.DeskDetails;
import com.abm.mainet.common.dashboard.service.CitizenDashboardGraphServiceImpl;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.repository.BlockChainRepository;
import com.abm.mainet.common.integration.dms.service.BlockChainCertificateServiceImpl;
import com.abm.mainet.common.integration.dms.service.IBlockChainCertificateService;
import com.abm.mainet.common.master.dto.EmployeeBean;
import com.abm.mainet.common.service.ICFCApplicationMasterService;
import com.abm.mainet.common.service.IEmployeeService;
import com.abm.mainet.common.service.ServiceMasterService;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.EncryptionAndDecryptionAapleSarkar;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.WorkflowRequest;
import com.abm.mainet.common.workflow.service.IWorkflowRequestService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author Saroj.Tambe
 *
 */
@RestController
@RequestMapping("/blockchain/certificates")
public class BlockChainCertificateController {

    @Autowired
    private IBlockChainCertificateService certificateService;
    
    @Autowired
	private BlockChainRepository blockChainRepository;
    
    @Resource
	private ICFCApplicationMasterService icfcApplicationMasterService;
	
	@Resource
	private IEmployeeService employeeService;
	
	@Autowired
    private ServiceMasterService serviceMaster;
	
	@Autowired
    private IWorkflowRequestService workflowRequestService;
	
	@Autowired
	CitizenDashboardGraphServiceImpl citizenDashboardGraphServiceImpl;
	
	String clientEncryptKey =ApplicationSession.getInstance().getMessage("aaple.sarkar.clientEncryptKey"); //  "@pn@kdm@m@h@0nl!ne@30489";  
	String clientEncryptIV =ApplicationSession.getInstance().getMessage("aaple.sarkar.clientEncryptIV");  // "KDM@02@3";
    
    private static final Logger LOGGER = Logger.getLogger(BlockChainCertificateController.class);

    
	@RequestMapping(value = "/issue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BlockChainCertificateResponseDTO> issueCertificate(
			@RequestBody BlockChainCertificateRequestDTO requestDTO) {
		
		BlockChainCertificateResponseDTO response = certificateService.issueCertificate(requestDTO);
		return ResponseEntity.ok(response);
		
	}
	
	@RequestMapping(
		    value = "/getBlockChainData",
		    produces = MediaType.APPLICATION_JSON_VALUE,
		    method = RequestMethod.GET
		)
		public ResponseEntity<List<MarriageBlockChainCertificateResponceDTO>> getBlockChainData(
		        MarriageBlockChainCertificateRequestDTO requestDto) {

		    List<MarriageBlockChainCertificateResponceDTO> responseList = new ArrayList<>();
		    String certiType = requestDto.getCertificateType();
		    String applicationId = requestDto.getAppId();
		    String orgId = "1";

		    // Validate input early
		    if (StringUtils.isBlank(certiType) || StringUtils.isBlank(applicationId)) {
		        MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
		        dto.setStatus("Invalid Request: certificateType or appId is missing");
		        responseList.add(dto);
		        return ResponseEntity.badRequest().body(responseList);
		    }

		    try {
		        if (StringUtils.equals(certiType, MainetConstants.MRM.SERVICE_CODE.MRG)) {
		            // Fetch raw data from repository
		            List<Object[]> rawDataList = blockChainRepository.getMrmData(applicationId, orgId);

		            if (CollectionUtils.isNotEmpty(rawDataList)) {
		                for (Object[] row : rawDataList) {
		                    MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();

		                    dto.setMarriageRegistrationDate(getSafeString(row, 0));
		                    dto.setMarriageDate(getSafeString(row, 1));
		                    dto.setHubandName(getSafeString(row, 2));
		                    dto.setWifeName(getSafeString(row, 3));
		                    dto.setHusbandAadhar(getSafeString(row, 4));
		                    dto.setWifeAadhar(getSafeString(row, 5));
		                    dto.setHusbandAddress(getSafeString(row, 6));
		                    dto.setWifeAddress(getSafeString(row, 7));
		                    dto.setStatus("SUCCESS");

		                    responseList.add(dto);
		                }
		            } else {
		                // No data found
		                MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
		                dto.setStatus("Data Not Found for Marriage Certificate for appId: " + applicationId);
		                responseList.add(dto);
		            }
		        } else if(StringUtils.equals(certiType, "DLC")) {
		        	
		        		 
		        		 List<Object[]> duplicateLicDataList = blockChainRepository.getBlockChainDuplicateLicenseCertificateData(applicationId, orgId);
		        		 if (CollectionUtils.isNotEmpty(duplicateLicDataList)) {
				                for (Object[] row : duplicateLicDataList) {
				                    MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();

				                    dto.setLicenseNo(getSafeString(row, 0));
				                    dto.setLicOwnerName(getSafeString(row, 1));
				                    dto.setLicBussinessName(getSafeString(row, 2));
				                    dto.setLicBussinessAddress(getSafeString(row, 3));
				                    dto.setLicenseDate(getSafeString(row, 4));
				                    dto.setLicenseCategory(getSafeString(row, 5));
				                    dto.setLicenseSubCategory(getSafeString(row, 6));
				                    dto.setLicenseMeasurementType(getSafeString(row, 7));
				                    dto.setLicenseMeasurmentUnit(getSafeString(row, 8));
				                    dto.setLicensePeriod(getSafeString(row, 9));
				                    dto.setStatus("SUCCESS");

				                    responseList.add(dto);
				                }
				            } else {
				                // No data found
				                MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
				                dto.setStatus("Data Not Found for Duplicate License Certificate for appId: " + applicationId);
				                responseList.add(dto);
				            }
		        	 
		        		 
		        	
		        } else if(StringUtils.equals(certiType, "IBC")) {
		        	
	        		 
	        		 List<Object[]> birthCertificateDataList = blockChainRepository.getBlockChainBirthCertificateData(applicationId, orgId);
	        		 if (CollectionUtils.isNotEmpty(birthCertificateDataList)) {
			                for (Object[] row : birthCertificateDataList) {
			                    MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();

			                    dto.setBrChildName(getSafeString(row, 0));
			                    dto.setBrDob(getSafeString(row, 1));
			                    dto.setBrFatherNameEng(getSafeString(row, 2));
			                    dto.setBrMotherNameEng(getSafeString(row, 3));
			                    dto.setBrParentsPermanantAdd(getSafeString(row, 4));
			                    dto.setBrBirthPlace(getSafeString(row, 5));
			                    dto.setBrRegNo(getSafeString(row, 6));
			                    dto.setBrCertificateNo(getSafeString(row, 7));
			                    dto.setStatus("SUCCESS");

			                    responseList.add(dto);
			                }
			            } else {
			                // No data found
			                MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
			                dto.setStatus("Data Not Found for Birth Certificate for BRID: " + applicationId);
			                responseList.add(dto);
			            }
	        	 
	        		 
	        	
	        }else if(StringUtils.equals(certiType, "IDC")) {
	        	
       		 
       		 List<Object[]> deathCertificateDataList = blockChainRepository.getBlockChainDeathCertificateData(applicationId, orgId);
       		 if (CollectionUtils.isNotEmpty(deathCertificateDataList)) {
		                for (Object[] row : deathCertificateDataList) {
		                    MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();

		                    dto.setDrDeceasedName(getSafeString(row, 0));
		                    dto.setDrDod(getSafeString(row, 1));
		                    dto.setDrFatherNameOrHusbandName(getSafeString(row, 2));
		                    dto.setDrMotherName(getSafeString(row, 3));
		                    dto.setDrDeathPlace(getSafeString(row, 4));
		                    dto.setDrRegNo(getSafeString(row, 5));
		                    dto.setDrDeceasedAddress(getSafeString(row, 6));
		                    dto.setDrCertificateNo(getSafeString(row, 7));
		                   
		                    dto.setStatus("SUCCESS");

		                    responseList.add(dto);
		                }
		            } else {
		                // No data found
		                MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
		                dto.setStatus("Data Not Found for Death Certificate for DRID: " + applicationId);
		                responseList.add(dto);
		            }
       	 
       		 
       	
       }
		    } catch (Exception e) {
		        // Log the error properly
		        LOGGER.error("Error while fetching blockchain data for appId:" + applicationId + "Certificate Type: " + certiType, e);

		        MarriageBlockChainCertificateResponceDTO dto = new MarriageBlockChainCertificateResponceDTO();
		        dto.setStatus("FAILURE: " + e.getMessage());
		        responseList.add(dto);
		    }

		    return ResponseEntity.ok(responseList);
		}

		/**
		 * Utility method to safely extract string from Object array
		 */
	private String getSafeString(Object[] row, int index) {
	    return (row != null && row.length > index && row[index] != null)
	            ? row[index].toString()
	            : ""; // Changed from null to ""
	}

	@RequestMapping(value="/getAapleSarkarGranularStatus",method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
	public String getAapleSarkarGranularResponse(HttpServletRequest request1) throws Exception{
		
		LOGGER.info("Method started getAapleSarkarGranularResponse ....");
		
		AapleSarkarGranularStatus aapleSarkarGranularStatus = new AapleSarkarGranularStatus();
	    aapleSarkarGranularStatus.setRecordStatus(true);
	    
	    ApplicationStatus aapleSarkarGranularStatusDTO = new ApplicationStatus();
		
		String encryptedRequest = null;
		
		 LOGGER.info("encryptedRequest: " + encryptedRequest);
		 LOGGER.error("encryptedRequest: " + encryptedRequest);
			
		// ─── Step 1: Try standard query param (?payload=...) ───────────────────
		    encryptedRequest = request1.getParameter("payload");
		    LOGGER.error("Step1 - getParameter payload: " + encryptedRequest);
		 
		 // ─── Step 2: Try semicolon-separated query string (;payload=...) ────────
		    if (encryptedRequest == null || encryptedRequest.isEmpty()) {
		        String queryString = request1.getQueryString();
		        LOGGER.error("Step2 - raw queryString: " + queryString);
		        if (queryString != null) {
		            encryptedRequest = Arrays.stream(queryString.split(";"))
		                .filter(p -> p.startsWith("payload="))
		                .map(p -> p.replace("payload=", "").replace("\"", "").trim())
		                .findFirst()
		                .orElse(null);
		            LOGGER.error("Step2 - extracted from queryString: " + encryptedRequest);
		        }
		    }
		
		 // ─── Step 3: Try POST body (plain text or JSON) ──────────────────────────
		    if (encryptedRequest == null || encryptedRequest.isEmpty()) {
		        String rawBody = request1.getReader()
		            .lines()
		            .collect(Collectors.joining())
		            .trim();  // ← only trim, NO quote stripping yet

		        LOGGER.error("Step3 - raw body: " + rawBody);

		        if (rawBody != null && !rawBody.isEmpty()) {
		            if (rawBody.startsWith("{")) {
		                // JSON body → extract payload field
		                try {
		                    ObjectMapper bodyMapper = new ObjectMapper();
		                    JsonNode node = bodyMapper.readTree(rawBody);
		                    if (node.has("payload")) {
		                        encryptedRequest = node.get("payload").asText().trim();
		                        LOGGER.info("Step3 - extracted from JSON body: " + encryptedRequest);
		                    } else {
		                        LOGGER.error("Step3 - JSON body has no 'payload' field");
		                    }
		                } catch (Exception e) {
		                    LOGGER.error("Step3 - JSON parse failed: " + e.getMessage());
		                }
		            } else {
		                // Plain text body → safe to strip quotes now
		                encryptedRequest = rawBody.replace("\"", "").trim();
		                LOGGER.info("Step3 - plain text body: " + encryptedRequest);
		            }
		        }
		    }
	    
		    // ─── Step 4: Guard — bail out if still no payload ───────────────────────
		    if (encryptedRequest == null || encryptedRequest.isEmpty()) {
		        LOGGER.error("No payload found in request — returning error");
		        return "{\"error\": \"Missing payload\"}";
		    }

		   LOGGER.info("Final encryptedRequest before decrypt: " + encryptedRequest);
	    
	 // Fallback: try reading from POST body
	    if (encryptedRequest == null || encryptedRequest.isEmpty()) {
	        LOGGER.error("No payload found in request");
	        return "{\"error\": \"Missing payload\"}";
	    }
		
		
		String decryptedJson;
		ObjectMapper mapper = new ObjectMapper();
		AapleSarkarStatusRequest request;
		LOGGER.error("encryptedRequest: " + encryptedRequest);
		decryptedJson = simpleTripleDesDecrypt(encryptedRequest, clientEncryptKey, clientEncryptIV);
		
		LOGGER.error("decryptedJson: " + decryptedJson);
		request = mapper.readValue(decryptedJson, AapleSarkarStatusRequest.class);
		LOGGER.error("ApplicationId: " + request.getAppID());
		

		int orgId=1;
		
		WorkflowRequest workflowRequest =null;
/*		List<WorkflowTaskActionWithDocs> acHistory =null;
*/
		TbCfcApplicationMstEntity tbCfcApplicationMstEntity = null;
		
		if(StringUtils.isNumeric(request.getAppID()))
		{	
			LOGGER.error("inside if of getAapleSarkarGranularResponse ...");

			tbCfcApplicationMstEntity= icfcApplicationMasterService.getCFCApplicationByApplicationId(Long.valueOf(request.getAppID()),orgId);
		
		    workflowRequest=workflowRequestService.getWorkflowRequestByAppIdOrRefId(Long.valueOf(request.getAppID()), null, 1L);
		    
		    /*acHistory=workflowActionService.getActionLogByAppIdOrRefIdAndWorkflowId(
					String.valueOf(workflowRequest.getApplicationId()), "null", workflowRequest.getId(), (short) orgId);*/
		    
			aapleSarkarGranularStatusDTO.setApplicationID(String.valueOf(workflowRequest.getApplicationId()));
			
			if(null!=tbCfcApplicationMstEntity.getAapleSarkarId()){
				
				String[] split2 = tbCfcApplicationMstEntity.getAapleSarkarId().split("\\|");
				String string = split2[17];
				aapleSarkarGranularStatusDTO.setApplicantName(string);
			}else{
				aapleSarkarGranularStatusDTO.setApplicantName("GUEST USER");
			}
			List<ApplicationGranularStatusEntity> applicationGranularStatusUsingNumeric = citizenDashboardGraphServiceImpl.getApplicationGranularStatusUsingNumeric(String.valueOf(workflowRequest.getApplicationId()));
			
			
			/*int currentDeskNumber=0;
			int nextDeskNumber=0;*/
			Integer maxDesk = applicationGranularStatusUsingNumeric.stream()
		            .map(ApplicationGranularStatusEntity::getDeskSequence)
		            .filter(Objects::nonNull)
		            .max(Integer::compareTo)
		            .orElse(0);
			List<DeskDetails> deskDetails2 = new ArrayList<>();
			for(ApplicationGranularStatusEntity a1: applicationGranularStatusUsingNumeric){
				LOGGER.error(
			            "DeskSequence={}, DeskName={}, Status={}, Decision={}, " +
			            "AssignDateTime={}, CompletionDateTime={}, ActionEmployeeId={}, Meaning={}" +
			            a1.getDeskSequence() + " "+
			            a1.getDeskName()  + " " +
			            a1.getStatus() + " " +
			            a1.getDecision() + " " +
			            a1.getAssignDateTime() + " " +
			            a1.getCompletionDateTime() + " " +
			            a1.getActionEmployeeId()+ " " +
			            a1.getMeaning() + " " +
			            a1.getActionComments()
			        );
				DeskDetails deskDetails = new DeskDetails();

				deskDetails.setDeskNumber("Desk " + a1.getDeskSequence());
				
				EmployeeBean employee = null;
				if (null != a1.getActionEmployeeId()) {
					String[] split = a1.getActionEmployeeId().split(",");
					employee = employeeService.findById(Long.valueOf(split[0]));
				}
				deskDetails.setReviewActionBy(employee.getDesignName()!=null ? employee.getDesignName() : MainetConstants.BLANK);
				
				deskDetails.setDeskRemark(a1.getActionComments()!=null ? a1.getActionComments(): MainetConstants.BLANK);
				
				if (StringUtils.equalsIgnoreCase("PENDING", a1.getStatus())) {
					deskDetails.setReviewActionDateTime(Utility.formatDate(a1.getAssignDateTime()));
					deskDetails.setReviewActionDetails("Application Pending");
					/*deskDetails.setReviewActionDetails("PENDING");*/
				} else {
					
					if(null!=a1.getCompletionDateTime()){
					deskDetails.setReviewActionDateTime(Utility.formatDate(a1.getCompletionDateTime()));
					}else{
						deskDetails.setReviewActionDateTime(Utility.formatDate(a1.getAssignDateTime()));
					}
					
					if(StringUtils.equalsIgnoreCase("REJECTED", workflowRequest.getLastDecision())){
                     LOGGER.error("setReviewActionDetails Is Rejected");
						deskDetails.setReviewActionDetails("Application Rejected");
					}
					else if(StringUtils.equalsIgnoreCase("APPROVED", workflowRequest.getLastDecision())){
						LOGGER.error("setReviewActionDetails Is Approved");
						
						deskDetails.setReviewActionDetails("Application Approved");
					}else{
						LOGGER.error("setReviewActionDetails Is "+ a1.getDecision());
						deskDetails.setReviewActionDetails(a1.getDecision());
					}
					
					/*deskDetails.setReviewActionDetails(a1.getDecision());*/
				}
				
				
				/*if ((currentDeskNumber < maxDesk - 1)) {
					nextDeskNumber = currentDeskNumber + 1;
				}else{
					nextDeskNumber=0;
				}
				deskDetails.setCurrentDeskNumber(String.valueOf(a1.getDeskSequence()-1));
				deskDetails.setNextDeskNumber(String.valueOf(nextDeskNumber));*/
				deskDetails2.add(deskDetails);
/*				currentDeskNumber++;
*/
			}
			
			aapleSarkarGranularStatusDTO.setDeskDetails(deskDetails2);
			
			
			
			Integer currentDesk =
				    applicationGranularStatusUsingNumeric.stream()
				        .map(ApplicationGranularStatusEntity::getDeskSequence)
				        .filter(Objects::nonNull)
				        .sorted(Comparator.reverseOrder()) 
				        .skip(1)
				        .findFirst()
				        .orElse(0);

			
			
			
			aapleSarkarGranularStatusDTO.setTotalNumberOfDesks(maxDesk);
			
			if(StringUtils.equalsIgnoreCase("CLOSED", workflowRequest.getStatus())){
				aapleSarkarGranularStatusDTO.setNextDeskNumber(0);
				aapleSarkarGranularStatusDTO.setCurrentDeskNumber(maxDesk);
				
				if(StringUtils.equalsIgnoreCase("APPROVED", workflowRequest.getLastDecision())){
					/*aapleSarkarGranularStatusDTO.setFinalDecision(0);*/ //APPROVED and CLOSED
					aapleSarkarGranularStatusDTO.setFinalDecision("Approved");
				}
				if(StringUtils.equalsIgnoreCase("REJECTED", workflowRequest.getLastDecision())){
					/*aapleSarkarGranularStatusDTO.setFinalDecision(1);*/  // REJECTED and CLOSED
					aapleSarkarGranularStatusDTO.setFinalDecision("Rejected");
				}
			}else{
			aapleSarkarGranularStatusDTO.setNextDeskNumber(maxDesk);
			aapleSarkarGranularStatusDTO.setCurrentDeskNumber(currentDesk);
				
			/*aapleSarkarGranularStatusDTO.setFinalDecision(2);*/ // PENDING
			aapleSarkarGranularStatusDTO.setFinalDecision("PendingAtDepartment");
			//HOLD, SEND_BACK
			}
			
			
		    aapleSarkarGranularStatusDTO.setServiceName(tbCfcApplicationMstEntity.getTbServicesMst().getSmServiceName());
		    aapleSarkarGranularStatusDTO.setEstimatedDisbursalDays(tbCfcApplicationMstEntity.getTbServicesMst().getSmServiceDuration());
		    aapleSarkarGranularStatusDTO.setApplicationSubmissionDate(Utility.formatDate(String.valueOf(tbCfcApplicationMstEntity.getApmApplicationDate())));
		    aapleSarkarGranularStatusDTO.setApplicationPaymentDate(Utility.formatDate(String.valueOf(tbCfcApplicationMstEntity.getApmApplicationDate())));
		}
		else{
			
			LOGGER.error("inside else of getAapleSarkarGranularResponse ...");

			tbCfcApplicationMstEntity= icfcApplicationMasterService.getCFCApplicationByRefNoOrAppNo(request.getAppID(),null,1L);
			workflowRequest=workflowRequestService.getWorkflowRequestByAppIdOrRefId(null, request.getAppID(), 1L);
			/*acHistory=workflowActionService.getActionLogByAppIdOrRefIdAndWorkflowId(
						"null", workflowRequest.getReferenceId(), workflowRequest.getId(), (short) orgId);*/
			aapleSarkarGranularStatusDTO.setApplicationID(String.valueOf(workflowRequest.getReferenceId()));
			
			if (null != tbCfcApplicationMstEntity.getAapleSarkarId()) {

				String[] split2 = tbCfcApplicationMstEntity.getAapleSarkarId().split("\\|");
				String string = split2[17];
				aapleSarkarGranularStatusDTO.setApplicantName(string);
			} else {
				aapleSarkarGranularStatusDTO.setApplicantName("GUEST USER");
			}
			
			List<ApplicationGranularStatusEntity> applicationGranularStatusUsingNumeric = citizenDashboardGraphServiceImpl.getApplicationGranularStatusUsingReferenceId(workflowRequest.getReferenceId());
			
			List<DeskDetails> deskDetails2 = new ArrayList<>();
			/*int currentDeskNumber=0;
			int nextDeskNumber=0;*/
			Integer maxDesk = applicationGranularStatusUsingNumeric.stream()
		            .map(ApplicationGranularStatusEntity::getDeskSequence)
		            .filter(Objects::nonNull)
		            .max(Integer::compareTo)
		            .orElse(0);
			for(ApplicationGranularStatusEntity a1: applicationGranularStatusUsingNumeric){
				LOGGER.error(
			            "DeskSequence={}, DeskName={}, Status={}, Decision={}, " +
			            "AssignDateTime={}, CompletionDateTime={}, ActionEmployeeId={}, Meaning={}" +
			            a1.getDeskSequence() + " "+
			            a1.getDeskName()  + " " +
			            a1.getStatus() + " " +
			            a1.getDecision() + " " +
			            a1.getAssignDateTime() + " " +
			            a1.getCompletionDateTime() + " " +
			            a1.getActionEmployeeId()+ " " +
			            a1.getMeaning()+ " " +
			            a1.getActionComments()
			        );
				DeskDetails deskDetails = new DeskDetails();

				deskDetails.setDeskNumber("Desk " + a1.getDeskSequence());
				
				EmployeeBean employee = null;
				if (null != a1.getActionEmployeeId()) {
					String[] split = a1.getActionEmployeeId().split(",");
					employee = employeeService.findById(Long.valueOf(split[0]));
				}
				deskDetails.setReviewActionBy(employee.getDesignName()!=null ? employee.getDesignName() : MainetConstants.BLANK);
				deskDetails.setDeskRemark(a1.getActionComments()!=null ? a1.getActionComments(): MainetConstants.BLANK);

				if (StringUtils.equalsIgnoreCase("PENDING", a1.getStatus()) || StringUtils.equalsIgnoreCase("EXITED", a1.getStatus())) {
					deskDetails.setReviewActionDateTime(a1.getAssignDateTime());
					if (StringUtils.equalsIgnoreCase("EXITED", a1.getStatus())) {

						deskDetails.setReviewActionDetails("ESCALATED");
					}
					if (StringUtils.equalsIgnoreCase("PENDING", a1.getStatus())) {

						deskDetails.setReviewActionDetails("PendingAtDepartment");
						/*deskDetails.setReviewActionDetails("PENDING");*/
						
					}
				} else {
					
					if(null!=a1.getCompletionDateTime()){
					deskDetails.setReviewActionDateTime(Utility.formatDate(a1.getCompletionDateTime()));
					}else{
						deskDetails.setReviewActionDateTime(Utility.formatDate(a1.getAssignDateTime()));
					}
					
					//as refer by Granular Tracking -testing evidance doc
					if(StringUtils.equalsIgnoreCase("REJECTED", workflowRequest.getLastDecision())){
						deskDetails.setReviewActionDetails("Application Rejected");
					}
					else if(StringUtils.equalsIgnoreCase("APPROVED", workflowRequest.getLastDecision())){
						deskDetails.setReviewActionDetails("Application Approved");
					}else{
						
						deskDetails.setReviewActionDetails(a1.getDecision());
					}
					
					//as refer by cidco
					/*deskDetails.setReviewActionDetails(a1.getDecision());*/
				}
				/*if (currentDeskNumber < maxDesk - 1) {
					nextDeskNumber = currentDeskNumber + 1;
				}else{
					nextDeskNumber=0;
				}
				/*deskDetails.setCurrentDeskNumber(String.valueOf(a1.getDeskSequence()-1));
				deskDetails.setNextDeskNumber(String.valueOf(nextDeskNumber));*/
				
				deskDetails2.add(deskDetails);
				

			}
			
			aapleSarkarGranularStatusDTO.setDeskDetails(deskDetails2);
			
			
			
			Integer currentDesk =
				    applicationGranularStatusUsingNumeric.stream()
				        .map(ApplicationGranularStatusEntity::getDeskSequence)
				        .filter(Objects::nonNull)
				        .sorted(Comparator.reverseOrder()) 
				        .skip(1)
				        .findFirst()
				        .orElse(0);

			
			
			
			aapleSarkarGranularStatusDTO.setTotalNumberOfDesks(maxDesk);
			
			if(StringUtils.equalsIgnoreCase("CLOSED", workflowRequest.getStatus())){
				aapleSarkarGranularStatusDTO.setNextDeskNumber(0);
				aapleSarkarGranularStatusDTO.setCurrentDeskNumber(maxDesk);
				
				if(StringUtils.equalsIgnoreCase("APPROVED", workflowRequest.getLastDecision())){
					/*aapleSarkarGranularStatusDTO.setFinalDecision(0);*/ //APPROVED and CLOSED
					aapleSarkarGranularStatusDTO.setFinalDecision("Approved");
				}
				if(StringUtils.equalsIgnoreCase("REJECTED", workflowRequest.getLastDecision())){
					/*aapleSarkarGranularStatusDTO.setFinalDecision(1);*/  // REJECTED and CLOSED
					
					aapleSarkarGranularStatusDTO.setFinalDecision("Rejected");
				}
				
			}else{
			aapleSarkarGranularStatusDTO.setNextDeskNumber(maxDesk);
			aapleSarkarGranularStatusDTO.setCurrentDeskNumber(currentDesk);
			/*aapleSarkarGranularStatusDTO.setFinalDecision(2);*/ // PENDING
			aapleSarkarGranularStatusDTO.setFinalDecision("PendingAtDepartment");
			
			}
			String serviceName="";
			Long smServiceDuration=null;
			ServiceMaster serMaster = serviceMaster.getServiceMaster(workflowRequest.getWorkFlowTaskList().get(1).getServiceId(), Long.valueOf(orgId));
			if(null != serMaster) {
				 serviceName = serMaster.getSmServiceName();
				 smServiceDuration = serMaster.getSmServiceDuration();
			}
			
			if(null!= smServiceDuration) {
			    aapleSarkarGranularStatusDTO.setEstimatedDisbursalDays(smServiceDuration);

			}
		    aapleSarkarGranularStatusDTO.setServiceName(serviceName);

		    aapleSarkarGranularStatusDTO.setApplicationPaymentDate(Utility.formatDate(String.valueOf(tbCfcApplicationMstEntity.getApmApplicationDate())));
		}
		
	    aapleSarkarGranularStatusDTO.setApplicationSubmissionDate(Utility.formatDate(String.valueOf(tbCfcApplicationMstEntity.getApmApplicationDate())));
	    //aapleSarkarGranularStatusDTO.setFinalDecision(workflowRequest.getLastDecision());
	    aapleSarkarGranularStatus.setApplicationStatus(aapleSarkarGranularStatusDTO);
	    aapleSarkarGranularStatus.setErrorCode("");
	    /*aapleSarkarGranularStatus.setMessage("");*/
	    //aapleSarkarGranularStatusDTO.setApplicationPaymentDate(String.valueOf(tbCfcApplicationMstEntity.getApmApplicationDate()));
	    aapleSarkarGranularStatusDTO.setDepartmentRedirectionURL("");
	    aapleSarkarGranularStatusDTO.setNextActionRequiredDetails("");
	    
	    
	    
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // Convert object to JSON
	    
	   String aapleSarkarGranularStatusResponseJson = objectMapper.writeValueAsString(aapleSarkarGranularStatus);
	   
	   LOGGER.error("aapleSarkarGranularStatus json {} " + aapleSarkarGranularStatusResponseJson);
	   
	   String aapleSarkarGranularStatusEncKey= simpleTripleDes(aapleSarkarGranularStatusResponseJson,clientEncryptKey,clientEncryptIV);
	    
	    LOGGER.error("End of  aapleSarkarGranularStatusEncKey  and Key is " + aapleSarkarGranularStatusEncKey);

	    return aapleSarkarGranularStatusEncKey;
	} 
	
	/*
	 * public static String simpleTripleDes(String Data, String strKey, String
	 * striv) { LOGGER.info("Start Genrate the Enc Key..."); byte[] key =
	 * strKey.getBytes(StandardCharsets.UTF_8); byte[] iv =
	 * striv.getBytes(StandardCharsets.UTF_8); byte[] data =
	 * Data.getBytes(StandardCharsets.UTF_8); byte[] enc; try { Cipher cipher =
	 * Cipher.getInstance("DESede/CBC/PKCS5Padding"); SecretKeySpec secretKeySpec =
	 * new SecretKeySpec(key, "DESede"); IvParameterSpec ivParameterSpec = new
	 * IvParameterSpec(iv); cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,
	 * ivParameterSpec); enc = cipher.doFinal(data); } catch (Exception e) {
	 * e.printStackTrace(); return null; }
	 * LOGGER.info("End Genrate the Enc Key..."); return
	 * Base64.getEncoder().encodeToString(enc); }
	 */
	
	
	/*
	 * public static String simpleTripleDes(String data, String strKey, String
	 * striv) {
	 * 
	 * LOGGER.info("Start Generate the Enc Key...");
	 * 
	 * try { byte[] key = strKey.getBytes(StandardCharsets.UTF_8); byte[] iv =
	 * striv.getBytes(StandardCharsets.UTF_8); byte[] inputData =
	 * data.getBytes(StandardCharsets.UTF_8);
	 * 
	 * Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding"); SecretKeySpec
	 * secretKeySpec = new SecretKeySpec(key, "DESede"); IvParameterSpec
	 * ivParameterSpec = new IvParameterSpec(iv);
	 * 
	 * cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
	 * 
	 * // ✅ Encrypt only once byte[] encryptedBytes = cipher.doFinal(inputData);
	 * 
	 * LOGGER.info("End Generate the Enc Key...");
	 * 
	 * // ✅ Return HEX string return bytesToHex(encryptedBytes);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return null; } }
	 */
	
	
	public static String simpleTripleDes(String data, String strKey, String striv) {
		
		LOGGER.info("Start Generate the Enc Key...");

	    try {
	        byte[] key = strKey.getBytes(StandardCharsets.UTF_8);
	        byte[] iv = striv.getBytes(StandardCharsets.UTF_8);

	        byte[] inputData = data.getBytes(StandardCharsets.UTF_8);

	        //  Manual Zero Padding (to multiple of 8)
	        int blockSize = 8;
	        int paddingLength = blockSize - (inputData.length % blockSize);

	        if (paddingLength != blockSize) {
	            byte[] padded = new byte[inputData.length + paddingLength];
	            System.arraycopy(inputData, 0, padded, 0, inputData.length);
	            inputData = padded;  // remaining bytes auto-filled with 0
	        }

	        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
	        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

	        byte[] encryptedBytes = cipher.doFinal(inputData);
	        
	        LOGGER.info("End Generate the Enc Key...");
	        
	        return bytesToHex(encryptedBytes);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	
	private static String bytesToHex(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02X", b));
	    }
	    return sb.toString();
	}
	
	 
		public static String simpleTripleDesDecrypt(String Data, String strKey, String striv)
		{
			byte[] key = strKey.getBytes(StandardCharsets.UTF_8);
		    byte[] iv = striv.getBytes(StandardCharsets.UTF_8);
		    byte[] data = stringToByteArray(Data);
		    byte[] enc = new byte[0];
		    try {
		        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
		        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		        enc = cipher.doFinal(data);      
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		  return new String(enc, StandardCharsets.UTF_8).trim().replaceAll("[" + "\0" + "]+$", "").replaceAll("[" + "|" + "]+$", "");
		}
		
		private static byte[] stringToByteArray(String hex) {
			int numberChars = hex.length();
        
			byte[] bytes = new byte[numberChars / 2];
			for (int i = 0; i < numberChars; i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        	}
        
        return bytes;
		}
		
	
}
