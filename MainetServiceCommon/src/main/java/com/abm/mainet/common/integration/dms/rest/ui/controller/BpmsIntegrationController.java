package com.abm.mainet.common.integration.dms.rest.ui.controller;

import java.util.ArrayList;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Path;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.BlockChainCertificateResponseDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateRequestDTO;
import com.abm.mainet.common.integration.dms.dto.MarriageBlockChainCertificateResponceDTO;
import com.abm.mainet.common.integration.dms.repository.BlockChainRepository;
import com.abm.mainet.common.integration.dms.service.BlockChainCertificateServiceImpl;
import com.abm.mainet.common.integration.dms.service.BpmsIntegrationServiceImpl;
import com.abm.mainet.common.integration.dms.service.IBlockChainCertificateService;
import com.abm.mainet.common.utility.UserSession;

import io.swagger.annotations.Api;

/**
 * 
 * @author Pritam.Chaphekar
 *
 */


@RestController
@RequestMapping("/bpms/integration")
@WebService
@Api(value = "/bpms/integration")
@Path("/bpms/integration")
public class BpmsIntegrationController {

   
    
    @Autowired
	private BpmsIntegrationServiceImpl bpmsIntegrationServiceImpl;
    
    private static final Logger LOGGER = Logger.getLogger(BpmsIntegrationController.class);

    
	
	
    @RequestMapping(value = "/push-approved-proposals", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<String> pushApprovedProposals() {
		LOGGER.error("Start Call BPMS Integration..");
		bpmsIntegrationServiceImpl.fetchAndStoreUlbData();
        return ResponseEntity.ok("Ulb proposal data fetched and stored successfully");
		
	}
	
	

	
}
