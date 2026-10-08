package com.abm.mainet.common.integration.rest.ui.controller;


import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.dto.WardZoneBlockDTO;
import com.abm.mainet.common.integration.dto.CfcApplicationMstDTO;
import com.abm.mainet.common.integration.dto.CommonServiceRequestDTO;
import com.abm.mainet.common.service.CommonService;
import com.abm.mainet.common.service.ICFCApplicationMasterService;


/**
 * @author Rahul.Yadav
 *
 */
@ServletSecurity(httpMethodConstraints = {
        @HttpMethodConstraint(value = "POST", transportGuarantee = TransportGuarantee.CONFIDENTIAL) })
@RestController
@RequestMapping("/CommonServiceDataRestController")
public class CommonServiceDataRestController {

    @Resource
    private CommonService commonService;
    
    @Resource
	private ICFCApplicationMasterService icfcApplicationMasterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceDataRestController.class);

    @RequestMapping(value = "/fetchWardZoneId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Object fetchWardZoneId(@RequestBody final CommonServiceRequestDTO requestDTO,
            final HttpServletRequest request, final BindingResult bindingResult) {
        Assert.notNull(requestDTO, "Request DTO Can not be null");
        WardZoneBlockDTO result = null;
        try {
            if (!bindingResult.hasErrors()) {
                result = commonService.getWordZoneBlockByApplicationId(requestDTO.getApplicationId(),
                        requestDTO.getServiceId(), requestDTO.getOrgId());
            }
        } catch (final Exception e) {
            LOGGER.error("error while fetching wardZone" + e);
        }
        return result;
    }
    
    
    @RequestMapping(value = "/getApplicationReceipt/{tokenNumber}/{orgId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CfcApplicationMstDTO getApplicationFeedback(@PathVariable("tokenNumber") String tokenNumber,@PathVariable("orgId") Long orgId,
            HttpServletRequest request, HttpServletResponse response) {
    	CfcApplicationMstDTO cfcApplicationMstDTO = null;
    	try {
			if (StringUtils.isNumeric(tokenNumber)) {
				cfcApplicationMstDTO = commonService.getcommonServiceRequestDTOByTokenNumber(tokenNumber, orgId);
			} else {
				TbCfcApplicationMstEntity tbCfcApplicationMstEntity = icfcApplicationMasterService
						.getCFCApplicationByRefNoOrAppNo(tokenNumber, null, orgId);
				cfcApplicationMstDTO = commonService.getcommonServiceRequestDTOByTokenNumber(
						tbCfcApplicationMstEntity.getApmApplicationId().toString(), orgId);
			}
    	} catch (Exception e) {
			LOGGER.error("error while fetching data" + e);
		}
    	
        return cfcApplicationMstDTO;
    }
    
    
    @RequestMapping(value = "/updateTransactionIdUsingAppId/{tokenNumber}/{transactionId}/{orgId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean updateTransIdUsingAppId(@PathVariable("tokenNumber") String tokenNumber,@PathVariable("transactionId") Long transactionId,
			@PathVariable("orgId") Long orgId, HttpServletRequest request) throws EntityNotFoundException, Exception {
		boolean updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar = false;
		try {
			if (StringUtils.isNumeric(tokenNumber)) {
				updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar = icfcApplicationMasterService
						.updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(Long.valueOf(tokenNumber),
								transactionId, orgId);
			} else {
				TbCfcApplicationMstEntity tbCfcApplicationMstEntity = icfcApplicationMasterService
						.getCFCApplicationByRefNoOrAppNo(tokenNumber, null, orgId);

				updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar = icfcApplicationMasterService
						.updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(
								tbCfcApplicationMstEntity.getApmApplicationId(), transactionId, orgId);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing updateTransIdUsingAppId" + e);
		}
		return updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar;
	}
    
    @RequestMapping(value = "/aapleSarkarPortalEntryToComplete/{tokenNumber}/{orgId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean aapleSarkarPortalEntryToComplete(@PathVariable("tokenNumber") String tokenNumber,
			@PathVariable("orgId") Long orgId, HttpServletRequest request) throws EntityNotFoundException, Exception {
		boolean aapleSarkarPortalEntryToComplete = false;
		try {
				commonService.updateAapleSarakarAfterApproval(tokenNumber, orgId);	
				aapleSarkarPortalEntryToComplete=true;
		} catch (Exception e) {
			LOGGER.error("error while executing updateTransIdUsingAppId" + e);
		}
		return aapleSarkarPortalEntryToComplete;
	}

}
