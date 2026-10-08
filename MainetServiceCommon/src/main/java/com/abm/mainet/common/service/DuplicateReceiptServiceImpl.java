/**
 * 
 */
package com.abm.mainet.common.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abm.mainet.cfc.challan.dto.ChallanReceiptPrintDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.TbReceiptDuplicateEntity;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.dto.TbReceiptDuplicateDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptModesDetEntity;
import com.abm.mainet.common.mapper.TbReceiptduplicateMapper;
import com.abm.mainet.common.repository.DuplicateReceiptRepository;
import com.abm.mainet.common.repository.ReceiptRepository;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * @author sarojkumar.yadav
 *
 */
@Service
public class DuplicateReceiptServiceImpl implements IDuplicateReceiptService {

    @Autowired
    private TbReceiptduplicateMapper mapper;
    @Autowired
    private DuplicateReceiptRepository repository;
    @Autowired
    private ReceiptDetailService receiptDetailService;
    
    @Autowired
    IReceiptEntryService receiptEntryService;
    @Autowired
    private ReceiptRepository receiptRepository;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateReceiptServiceImpl.class);


    /*
     * (non-Javadoc)
     * @see com.abm.mainet.common.service.IDuplicateReceiptService#save(com.abm.mainet.
     * common.integration.dto.TbReceiptDuplicateDTO)
     */

    @Override
    public void saveDuplicateReceipt(final ChallanReceiptPrintDTO receiptDto, final TbServiceReceiptMasEntity receiptMaster,
            final CommonChallanDTO offline) {
        TbReceiptDuplicateDTO dto = new TbReceiptDuplicateDTO();
        dto.setRmRcpId(receiptMaster.getRmRcptid());
        dto.setRmRcpNo(receiptMaster.getRmRcptno());
        dto.setApplicationId(receiptMaster.getApmApplicationId());
        dto.setAdditionalRefNo(receiptMaster.getAdditionalRefNo());
        dto.setRmDate(receiptMaster.getRmDate());
        dto.setCreatedBy(receiptMaster.getCreatedBy());
        dto.setCreatedDate(new Date());
        dto.setLgIpMac(receiptMaster.getLgIpMac());
        dto.setDeptCode(offline.getDeptCode());
        dto.setServiceCode(offline.getServiceCode());
        dto.setDupReceiptData(receiptMaster.getRmDate().toString());
        dto.setOrgId(receiptMaster.getOrgId());
        try {
            dto.setDupReceiptData(new ObjectMapper().writeValueAsString(receiptDto));
        } catch (JsonProcessingException e) {
            throw new FrameworkException("Error While Casting Object in String for receciptDuplicateBill() " + e);
        }
        save(dto);
    }

    @Override
    public void save(TbReceiptDuplicateDTO dto) {
        TbReceiptDuplicateEntity entity = mapper.mapTbReceiptDuplicateDTOToTbReceiptDuplicateEntity(dto);
        repository.save(entity);
    }

	public ChallanReceiptPrintDTO getRevenueReceiptDetails(Long receiptId, Long receiptNo, String assNo,Long orgId, int langId,String receiptType, String serviceCode) {		
		ChallanReceiptPrintDTO receiptDTO = null;
		try {
			List<TbReceiptDuplicateEntity> entityList=repository.findMulByDupRcptByRcptIdAndrcptNoAndRefNo(receiptId,receiptNo,assNo);
			
			if (CollectionUtils.isNotEmpty(entityList)) {
				boolean isDuplicateReceiptService = "DRP".equals(serviceCode);
				TbReceiptDuplicateEntity entity = isDuplicateReceiptService
				    ? entityList.get(entityList.size() - 1)
				    : entityList.get(0);
				
	            TbReceiptDuplicateDTO receiptDuplicateDTO = new TbReceiptDuplicateDTO();
	            BeanUtils.copyProperties(entity, receiptDuplicateDTO);

	            String json = receiptDuplicateDTO.getDupReceiptData();

	            if (!isDuplicateReceiptService) {
	                receiptDTO = new ObjectMapper().readValue(json, ChallanReceiptPrintDTO.class);
	            } else {
	                TbServiceReceiptMasEntity receiptMas = new ObjectMapper().readValue(json, TbServiceReceiptMasEntity.class);

	                if (receiptMas.getRmRcptno() != null) {
	                    receiptDTO = isRebate(receiptType)
	                            ? receiptEntryService.setValuesAndPrintReportForRebateReceipts(receiptMas, orgId, langId)
	                            : receiptEntryService.setValuesAndPrintReport(receiptMas, orgId, langId);
	                } else {
	                    receiptDTO = isRebate(receiptType)
	                            ? receiptEntryService.setValuesAndPrintReportForRebateReceipts(receiptId, orgId, langId)
	                            : receiptDetailService.setValuesAndPrintReport(receiptId, orgId, langId);
	                }
	            }

	            if (entity.getRmDate() != null && receiptDTO != null) {
	                SimpleDateFormat dateFormat = new SimpleDateFormat(MainetConstants.DATE_FORMAT);
	                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	                String formattedDate = dateFormat.format(entity.getRmDate());
	                String formattedTime = timeFormat.format(entity.getCreatedDate());

	                receiptDTO.setRcptDate(formattedDate);
	                receiptDTO.setRcptTime(formattedTime);
	                receiptDTO.setReceiptDate(formattedDate);
	                receiptDTO.setReceiptTime(formattedTime);
	            }

	            if (entity.getServiceCode() != null && receiptDTO != null) {
	                receiptDTO.setServiceCode(entity.getServiceCode());
	            }

	            if (entity.getCreatedBy() != null && receiptDTO != null) {
	                receiptDTO.setRecptCreatedBy(entity.getCreatedBy());
	            }
	        } else {
	        	receiptDTO = isRebate(receiptType)
	                    ? receiptEntryService.setValuesAndPrintReportForRebateReceipts(receiptId, orgId, langId)
	                    : receiptDetailService.setValuesAndPrintReport(receiptId, orgId, langId);
	        }
		}
		catch (IOException e) {
			LOGGER.error("Problem while getting respose of duplicate receipt");
			throw new FrameworkException(e);
		}
		return receiptDTO;
	}

	private boolean isRebate(String receiptType) {
	    return StringUtils.isNotBlank(receiptType) && "RB".equals(receiptType);
	}
	//#99721
	public ChallanReceiptPrintDTO getReceiptDetails(Long receiptId, Long receiptNo, Long applcationId) {		
		ChallanReceiptPrintDTO dataDTO=null;
		try {
		TbReceiptDuplicateEntity entity = repository.findByDupRcptByRcptIdAndrcptNoAndApplId(receiptId,receiptNo,applcationId);		
		if(entity!=null) {
		TbReceiptDuplicateDTO rcptPrintDto= new TbReceiptDuplicateDTO();
		BeanUtils.copyProperties(entity,rcptPrintDto);	
		dataDTO = new ObjectMapper().readValue(rcptPrintDto.getDupReceiptData(),
					  ChallanReceiptPrintDTO.class);
		if(entity.getRmDate()!=null) {
		dataDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(entity.getRmDate()));
				}
			}
		}
		catch (IOException e) {
			LOGGER.error("Problem while getting respose of duplicate receipt");
			throw new FrameworkException(e);
		}
		return dataDTO;
	}

	@Override
	public ChallanReceiptPrintDTO getRevenueReceiptDetails(Long reciptId, Long receiptNo, String assNo,Long orgId, int langId) {
		ChallanReceiptPrintDTO dataDTO = null;
		Organisation org = new Organisation();
		org.setOrgid(orgId);
		try {
			TbReceiptDuplicateEntity entity = repository.findByDupRcptByRcptIdAndrcptNoAndRefNo(reciptId, receiptNo,
					assNo);
			if (entity != null) {
				TbReceiptDuplicateDTO rcptPrintDto = new TbReceiptDuplicateDTO();
				BeanUtils.copyProperties(entity, rcptPrintDto);
				dataDTO = new ObjectMapper().readValue(rcptPrintDto.getDupReceiptData(), ChallanReceiptPrintDTO.class);
				if (entity.getRmDate() != null) {
					dataDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(entity.getRmDate()));
					// #148057
					if (StringUtils.isNotEmpty(entity.getServiceCode()))
						dataDTO.setServiceCode(entity.getServiceCode());
				}
				if(reciptId!=null) {
					List<TbSrcptModesDetEntity> modeDet=receiptRepository.getModeDetails(reciptId);
					if (langId > 0 && langId == MainetConstants.REGIONAL_LANGUAGE_ID) {
						dataDTO.setPaymentMode(CommonMasterUtility
								.getNonHierarchicalLookUpObject(modeDet.get(0).getCpdFeemode(), org)
								.getDescLangSecond());
					}else {
						dataDTO.setPaymentMode(CommonMasterUtility
								.getNonHierarchicalLookUpObject(modeDet.get(0).getCpdFeemode(), org)
								.getDescLangFirst());
					}
					
					if(CollectionUtils.isNotEmpty(modeDet)) {
						if(modeDet.get(0).getCheckStatus()!=null) {
						LookUp lookUp = CommonMasterUtility.getNonHierarchicalLookUpObject(modeDet.get(0).getCheckStatus());
						if(lookUp!=null&&StringUtils.equalsIgnoreCase(lookUp.getLookUpCode(), MainetConstants.Property.CLEARED)) {
							dataDTO.setCheckClr(true);
						}}
					}
				}
				dataDTO.setRecptCreatedBy(entity.getCreatedBy());
				if (dataDTO.getEarlyPaymentDiscount() <= 0 && StringUtils.equals(entity.getDeptCode(), "AS")) {
					Double rebateAmountByPropNo = ApplicationContextProvider.getApplicationContext().getBean(ReceiptRepository.class).getRebateAmountByPropNo(assNo, entity.getRmDate());
					if(rebateAmountByPropNo != null && rebateAmountByPropNo > 0) {
						dataDTO.setEarlyPaymentDiscount(rebateAmountByPropNo);
					}
				}
			}else {
					dataDTO = receiptEntryService.setValuesAndPrintReport(reciptId, orgId, langId);
			}
		} catch (IOException e) {
			LOGGER.error("Problem while getting respose of duplicate receipt");
			throw new FrameworkException(e);
		}
		return dataDTO;
	}

}
