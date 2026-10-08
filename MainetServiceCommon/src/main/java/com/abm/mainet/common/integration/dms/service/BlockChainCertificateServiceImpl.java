package com.abm.mainet.common.integration.dms.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.abm.mainet.common.integration.dms.domain.BlockChainCertificateEntity;
import com.abm.mainet.common.integration.dms.domain.DigilockerCertificateEntity;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.dto.PullURIRequest;
import com.abm.mainet.common.integration.dms.dto.PullURIResponse;
import com.abm.mainet.common.integration.dms.repository.BlockChainRepository;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.UserSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.SSLContext;

/**
 * @author Saroj.Tambe
 *
 */
@Service
public class BlockChainCertificateServiceImpl implements IBlockChainCertificateService{
	
	private static final Logger LOGGER = Logger.getLogger(BlockChainCertificateServiceImpl.class);

	private static final String apiKey = ApplicationSession.getInstance().getMessage("blockchain.api.key"); //AKIAIOSFODNN7
	
	private static final String apiSecret= ApplicationSession.getInstance().getMessage("blockchain.api.secret"); //wJalrXUtnFEMI/K7MDENG/bPxRfiCY
	
	private static final String blockChainIssueURL=ApplicationSession.getInstance().getMessage("blockchain.issue.url");
	
	private static final String blockChainReissueURL=ApplicationSession.getInstance().getMessage("blockchain.reissue.url");
	
	@Autowired
	private BlockChainRepository blockChainRepository;
	
	
	private RestTemplate getInsecureRestTemplate() {
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(null, (certificate, authType) -> true) // Trust all
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // Ignore hostname check
                    .build();

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return new RestTemplate(factory);
        } catch (Exception e) {
            LOGGER.error("Error creating insecure RestTemplate, falling back to default", e);
            return new RestTemplate();
        }
    }
	
	
	
	 
	@Override
	public BlockChainCertificateResponseDTO issueCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			BlockChainCertificateEntity blockChainCertificateEntity = new BlockChainCertificateEntity();
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response =
            		restReq.exchange(new URI(blockChainIssueURL), HttpMethod.POST, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	
            	String jsonResponse= mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    			Date sysDate = new Date();

    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Populate entity
    			blockChainCertificateEntity.setUid(uid);
    			blockChainCertificateEntity.setAppId(appId);
    			blockChainCertificateEntity.setRequestJson(jsonRequest);
    			blockChainCertificateEntity.setLmodDate(sysDate);
    			blockChainCertificateEntity.setUpdatedDate(sysDate);
    			blockChainCertificateEntity.setIssue(MainetConstants.Y_FLAG);
    			blockChainCertificateEntity.setReissue(MainetConstants.N_FLAG);
    			
    			blockChainCertificateEntity.setOrgId(orgId);
    			blockChainCertificateEntity.setuDF1(response.getBody().getResult().getVerificationLink());
    			
    			
    			// Save entity
    			blockChainRepository.save(blockChainCertificateEntity);	

                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to issue certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Transactional
	@Override
	public BlockChainCertificateResponseDTO reissueCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response =
            		restReq.exchange(new URI(blockChainReissueURL), HttpMethod.PUT, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	
            	String jsonResponse= mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    		
    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Update tb_blockchain_data table.
    			
    			LOGGER.error("Birth Reissue Update Start for ApplicationId/BrId: " + appId + "And" + uid );
    			Date sysDate = new Date();
    			
    			blockChainRepository.updateReissueFlag(MainetConstants.Y_FLAG, jsonRequest, Long.parseLong(appId), orgId, sysDate);
    			
    			
    			LOGGER.error("Birth Reissue Update Completed.. ");
    			
                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to issue certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public BlockChainCertificateResponseDTO issueMarriageMCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			BlockChainCertificateEntity blockChainCertificateEntity = new BlockChainCertificateEntity();
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response = restReq.exchange(new URI(blockChainIssueURL), HttpMethod.POST, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getResult().getVerificationLink() != null) {
            	
            	String jsonResponse = mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    			Date sysDate = new Date();

    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Populate entity
    			blockChainCertificateEntity.setUid(uid);
    			blockChainCertificateEntity.setAppId(appId);
    			blockChainCertificateEntity.setRequestJson(jsonRequest);
    			blockChainCertificateEntity.setLmodDate(sysDate);
    			blockChainCertificateEntity.setUpdatedDate(sysDate);
    			blockChainCertificateEntity.setIssue(MainetConstants.Y_FLAG);
    			blockChainCertificateEntity.setReissue(MainetConstants.N_FLAG);
    			
    			blockChainCertificateEntity.setOrgId(orgId);
    			blockChainCertificateEntity.setuDF1(response.getBody().getResult().getVerificationLink());
    			
    			
    			// Save entity
    			blockChainRepository.save(blockChainCertificateEntity);	

                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to issue certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	//Block-Chain Integration Print Duplicate License Issue Certificate
	@Override
	public BlockChainCertificateResponseDTO issueDuplicateLicenseCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			BlockChainCertificateEntity blockChainCertificateEntity = new BlockChainCertificateEntity();
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response = restReq.exchange(new URI(blockChainIssueURL), HttpMethod.POST, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getResult().getVerificationLink() != null) {
            	
            	String jsonResponse = mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    			Date sysDate = new Date();

    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Populate entity
    			blockChainCertificateEntity.setUid(uid);
    			blockChainCertificateEntity.setAppId(appId);
    			blockChainCertificateEntity.setRequestJson(jsonRequest);
    			blockChainCertificateEntity.setLmodDate(sysDate);
    			blockChainCertificateEntity.setUpdatedDate(sysDate);
    			blockChainCertificateEntity.setIssue(MainetConstants.Y_FLAG);
    			blockChainCertificateEntity.setReissue(MainetConstants.N_FLAG);
    			
    			blockChainCertificateEntity.setOrgId(orgId);
    			blockChainCertificateEntity.setuDF1(response.getBody().getResult().getVerificationLink());
    			
    			
    			// Save entity
    			blockChainRepository.save(blockChainCertificateEntity);	

                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to Duplicate License Certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public BlockChainCertificateResponseDTO issueDeathCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			BlockChainCertificateEntity blockChainCertificateEntity = new BlockChainCertificateEntity();
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response =
            		restReq.exchange(new URI(blockChainIssueURL), HttpMethod.POST, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getResult().getVerificationLink() != null) {
            	
            	String jsonResponse= mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    			Date sysDate = new Date();

    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Populate entity
    			blockChainCertificateEntity.setUid(uid);
    			blockChainCertificateEntity.setAppId(appId);
    			blockChainCertificateEntity.setRequestJson(jsonRequest);
    			blockChainCertificateEntity.setLmodDate(sysDate);
    			blockChainCertificateEntity.setUpdatedDate(sysDate);
    			blockChainCertificateEntity.setIssue(MainetConstants.Y_FLAG);
    			blockChainCertificateEntity.setReissue(MainetConstants.N_FLAG);
    			
    			blockChainCertificateEntity.setOrgId(orgId);
    			blockChainCertificateEntity.setuDF1(response.getBody().getResult().getVerificationLink());
    			
    			
    			// Save entity
    			blockChainRepository.save(blockChainCertificateEntity);	

                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to issue Death certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Transactional
	@Override
	public BlockChainCertificateResponseDTO reissueDeathCertificate(BlockChainCertificateRequestDTO requestDTO) {

		//RestTemplate restReq = new RestTemplate();
		RestTemplate restReq = getInsecureRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response =
            		restReq.exchange(new URI(blockChainReissueURL), HttpMethod.PUT, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	
            	String jsonResponse= mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    		
    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Update tb_blockchain_data table.
    			
    			LOGGER.error("Death Reissue Update Start for ApplicationId/BrId: " + appId + "And" + uid );
    			Date sysDate = new Date();
    			
    			blockChainRepository.updateReissueFlag(MainetConstants.Y_FLAG, jsonRequest, Long.parseLong(appId), orgId, sysDate);
    			
    			
    			LOGGER.error("Death Reissue Update Completed.. ");
    			
                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to issue certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public BlockChainCertificateResponseDTO issueNewLicenseCertificate(BlockChainCertificateRequestDTO requestDTO) {

		RestTemplate restReq = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-api-secret", apiSecret);
		headers.add("Content-Type", "application/json");
		HttpEntity<BlockChainCertificateRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

		try {
			Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
			ObjectMapper mapper = new ObjectMapper();
			BlockChainCertificateEntity blockChainCertificateEntity = new BlockChainCertificateEntity();
			String jsonRequest;
			jsonRequest = mapper.writeValueAsString(entity.getBody());
			LOGGER.error("Request JSON: " + jsonRequest);
            ResponseEntity<BlockChainCertificateResponseDTO> response = restReq.exchange(new URI(blockChainIssueURL), HttpMethod.POST, entity, BlockChainCertificateResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getResult().getVerificationLink() != null) {
            	
            	String jsonResponse = mapper.writeValueAsString(response.getBody());
    			LOGGER.error("Response JSON: " + jsonResponse);
    			
    			Date sysDate = new Date();

    			// Safely extract AppId from UID
    			String uid = requestDTO.getUid();
    			String appId = null;
    			if (uid != null && uid.contains("-")) {
    			    String[] parts = uid.split("-", 2); // limit=2 → avoids extra splits
    			    if (parts.length > 1) {
    			        appId = parts[1];
    			    }
    			}

    			// Populate entity
    			blockChainCertificateEntity.setUid(uid);
    			blockChainCertificateEntity.setAppId(appId);
    			blockChainCertificateEntity.setRequestJson(jsonRequest);
    			blockChainCertificateEntity.setLmodDate(sysDate);
    			blockChainCertificateEntity.setUpdatedDate(sysDate);
    			blockChainCertificateEntity.setIssue(MainetConstants.Y_FLAG);
    			blockChainCertificateEntity.setReissue(MainetConstants.N_FLAG);
    			
    			blockChainCertificateEntity.setOrgId(orgId);
    			blockChainCertificateEntity.setuDF1(response.getBody().getResult().getVerificationLink());
    			
    			
    			// Save entity
    			blockChainRepository.save(blockChainCertificateEntity);	

                return response.getBody();
            } else {
            	LOGGER.error("Blockchain API returned non-success status: {} | Body: {}"); 
                              
                throw new RuntimeException("Failed to New License Certificate. Status: " + response.getStatusCode() + "Response Body:" + response.getBody());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx responses
        	LOGGER.error("Blockchain API error: Status {} | Response: {}", e);
            throw new RuntimeException("Blockchain API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (RestClientException e) {
            LOGGER.error("Rest client error while calling Blockchain API", e);
            throw new RuntimeException("Error while calling Blockchain API", e);

        } catch (URISyntaxException e) {
            LOGGER.error("Invalid Blockchain API URL: {}", e);
            throw new RuntimeException("Invalid Blockchain API URL", e);
        }catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	

	
	

}
