package com.abm.mainet.bill.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.abm.mainet.bill.repository.AdjustmentBillDetailMappingRepository;
import com.abm.mainet.bill.repository.AdjustmentEntryRepository;
import com.abm.mainet.cfc.challan.domain.AdjustmentBillDetailMappingEntity;
import com.abm.mainet.cfc.challan.domain.AdjustmentDetailEntity;
import com.abm.mainet.cfc.challan.domain.AdjustmentMasterEntity;
import com.abm.mainet.cfc.challan.dto.AdjustmentDetailDTO;
import com.abm.mainet.cfc.challan.dto.AdjustmentMasterDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Department;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.ApplicantDetailDTO;
import com.abm.mainet.common.dto.WardZoneBlockDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDTO;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDetailDTO;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostListDTO;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.integration.dto.TbBillDet;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.master.dto.TbLocationMas;
import com.abm.mainet.common.master.dto.TbTaxMas;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbCfcApplicationMstService;
import com.abm.mainet.common.master.service.TbTaxMasService;
import com.abm.mainet.common.service.CommonService;
import com.abm.mainet.common.service.ILocationMasService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.dto.ApplicationMetadata;
import com.google.common.util.concurrent.AtomicDouble;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Service
public class AdjustmentEntryServiceImpl implements AdjustmentEntryService {

    @Resource
    private AdjustmentEntryRepository adjustmentEntryRepository;

    @Resource
    private AdjustmentBillDetailMappingRepository adjustmentBillDetailMappingRepository;

    @Resource
    private DepartmentService departmentService;

    @Autowired
    private MessageSource messageSource;

    @Resource
    private TbTaxMasService tbTaxMasService;
    
    @Autowired
    private CommonService commonService;
    
    @Autowired
    private ILocationMasService iLocationMasService;
    
    @Autowired
    TbCfcApplicationMstService tbCfcAppService;
    
    private final String constantNO = MainetConstants.Common_Constant.NO;
    private final String constantYES = MainetConstants.Common_Constant.YES;
    private final String positive = MainetConstants.MENU.P;
    private final String constantWO = MainetConstants.FlagW;
    private final String constantBoja = MainetConstants.FlagB;
    private final String palavaExemption = MainetConstants.FlagE;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdjustmentEntryServiceImpl.class);

    @Override
    @Transactional
    public List<TbBillMas> fetchModuleWiseBills(final Long deptId, final String ccnNoOrPropNo, final Long orgId) {
        List<TbBillMas> result = null;
        try {
            BillGenerationService dynamicServiceInstance = null;
            String serviceClassName = null;
            final String deptCode = departmentService.getDeptCode(deptId);
            serviceClassName = messageSource.getMessage(
                    ApplicationSession.getInstance().getMessage("bill.lbl.Bills") + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillGenerationService.class);
            result = dynamicServiceInstance.fetchCurrentBill(ccnNoOrPropNo, orgId);
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in fetching bill for Adjustment entry for Department: " + deptId, e);
        }
        return result;
    }

    @Override
    public List<TbTaxMas> fetchAllModulewiseTaxes(final Long deptId, final Organisation orgId) {
        final LookUp chargeApplicableAt = CommonMasterUtility.getValueFromPrefixLookUp(
                PrefixConstants.NewWaterServiceConstants.BILL,
                PrefixConstants.NewWaterServiceConstants.CAA, orgId);
        return tbTaxMasService.findAllTaxesForBillGeneration(orgId.getOrgid(), deptId, chargeApplicableAt.getLookUpId(), null);
    }

    @Override
    @Transactional
    public boolean saveAdjustmentEntryData(final List<TbBillMas> billlist, final AdjustmentMasterDTO adjustmentDto,Long penaltyDummyId,Long penaltyTaxId,Organisation org, Long empId) {
        final AdjustmentMasterEntity entity = new AdjustmentMasterEntity();
        BeanUtils.copyProperties(adjustmentDto, entity);
        AtomicDouble advanceAmount = new AtomicDouble(0);
		if (billlist.get(0).getCsIdn() != null) {
			entity.setAdjRefNo(billlist.get(0).getCsIdn().toString());
		} else if (StringUtils.isNotBlank(billlist.get(0).getPropNo())) {
			entity.setAdjRefNo(billlist.get(0).getPropNo());
		}
		final String adjType=adjustmentDto.getAdjType();
		
		Long lastBmIdNo = billlist.stream().max(Comparator.comparing(TbBillMas::getBmIdno)).get().getBmIdno();

		final String deptCode = departmentService.getDeptCode(adjustmentDto.getDpDeptId());
		
		Long orgid=org.getOrgid();
        entity.setAdjDate(new Date());
        entity.setOrgId(orgid);
        entity.setCreatedBy(empId);
        entity.setCreatedDate(new Date());
        entity.setLgIpMac(Utility.getMacAddress());
        entity.setAdjAdvnceAmntFlag(MainetConstants.FlagY);
        AdjustmentDetailEntity detailsEntity = null;
        final List<AdjustmentBillDetailMappingEntity> mappingList = new ArrayList<>(0);
        final List<TbBillDet> adjustedBills = new ArrayList<>(0);
        for (final TbBillMas bill : billlist) {
            Double amount = 0d;
            List<TbBillDet> previous = null;
            for (final AdjustmentDetailDTO detail : adjustmentDto.getAdjDetailDto()) {
                if (detail.getAdjAmount() > 0d) {
                    detailsEntity = new AdjustmentDetailEntity();
                    BeanUtils.copyProperties(detail, detailsEntity);
                    detailsEntity.setAdvanceAdjFlag(MainetConstants.FlagY);
                    detailsEntity.setAdjId(entity);
                    if ((bill.getTbWtBillDet() != null) && !bill.getTbWtBillDet().isEmpty()) {
                        AdjustmentBillDetailMappingEntity mapping = null;
                        for (final TbBillDet det : bill.getTbWtBillDet()) {
                            if (det.getTaxId().equals(detail.getTaxId())) {
                                if (constantNO.equals(adjustmentDto.getAdjType())||constantWO.equals(adjustmentDto.getAdjType()) || "E".equals(adjustmentDto.getAdjType())) {
                                    negativeOrWaiveOffAdjustmentLogic(adjustmentDto, advanceAmount, lastBmIdNo, detailsEntity,
											adjustedBills, bill, detail, det,entity, deptCode);
								}else {
                                    positiveAdjustmentLogic(adjustmentDto, detailsEntity, detail, det,org, deptCode);
                                }

                                mapping = new AdjustmentBillDetailMappingEntity();
                                mapping.setAdjAmount(detailsEntity.getAdjAdjustedAmount());
                                mapping.setAdjbmId(det.getBdBilldetid());
                                mapping.setOrgId(entity.getOrgId());
                                mapping.setDpDeptId(entity.getDpDeptId());
                                mapping.setLgIpMac(entity.getLgIpMac());
                                mapping.setCreatedBy(entity.getCreatedBy());
                                mapping.setCreatedDate(new Date());
                                mapping.setTaxId(detail.getTaxId());
                                mappingList.add(mapping);
                                detailsEntity.setOrgId(org.getOrgid());
                                detailsEntity.setCreatedBy(empId);
                                detailsEntity.setCreatedDate(new Date());
                                detailsEntity.setLgIpMac(Utility.getMacAddress());
                                detailsEntity.setBdBilldetid(det.getBdBilldetid());
                                detailsEntity.setBmIdNo(det.getBmIdno());
                                amount += detailsEntity.getAdjAdjustedAmount();
                                entity.getAdjDetail().add(detailsEntity);

							} else if (MainetConstants.DEPT_SHORT_NAME.PROPERTY
									.equals(deptCode)
									&& constantNO.equals(adjustmentDto.getAdjType())
									&& detail.getTaxId().equals(Long.valueOf(MainetConstants.Property.dummyPenaltyId))
									&& penaltyTaxId != null && det.getTaxId().equals(penaltyTaxId) && lastBmIdNo.equals(bill.getBmIdno())) {
								
								negativeOrWaiveOffAdjustmentLogic(adjustmentDto, advanceAmount, lastBmIdNo, detailsEntity,
										adjustedBills, bill, detail, det,entity, deptCode);
								 mapping = new AdjustmentBillDetailMappingEntity();
	                                mapping.setAdjAmount(detailsEntity.getAdjAdjustedAmount());
	                                mapping.setAdjbmId(det.getBdBilldetid());
	                                mapping.setOrgId(entity.getOrgId());
	                                mapping.setDpDeptId(entity.getDpDeptId());
	                                mapping.setLgIpMac(entity.getLgIpMac());
	                                mapping.setCreatedBy(entity.getCreatedBy());
	                                mapping.setCreatedDate(new Date());
	                                mapping.setTaxId(det.getTaxId());
	                                mappingList.add(mapping);
	                                detailsEntity.setOrgId(org.getOrgid());
	                                detailsEntity.setCreatedBy(empId);
	                                detailsEntity.setCreatedDate(new Date());
	                                detailsEntity.setLgIpMac(Utility.getMacAddress());
	                                detailsEntity.setBdBilldetid(det.getBdBilldetid());
	                                detailsEntity.setBmIdNo(det.getBmIdno());
	                                detailsEntity.setTaxId(det.getTaxId());
	                                amount += detailsEntity.getAdjAdjustedAmount();
	                                entity.getAdjDetail().add(detailsEntity);
							}
                        }
					}  else if(MainetConstants.WATER_DEPT.equals(deptCode)){
						if(Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL) && 
						   (MainetConstants.WATER_DEPT.equals(deptCode))){
						AdjustmentBillDetailMappingEntity mapping = null;
	                    //	if (det.getTaxId().equals(detail.getTaxId())) {
                             TbBillDet det = null;
						if(positive.equals(adjustmentDto.getAdjType())||constantBoja.equals(adjustmentDto.getAdjType())) {	
							positiveAdjustmentLogic(adjustmentDto, detailsEntity, detail, det,org, deptCode);						
						}else if(constantNO.equals(adjustmentDto.getAdjType())||constantWO.equals(adjustmentDto.getAdjType())) {	
							  negativeOrWaiveOffAdjustmentLogic(adjustmentDto, advanceAmount, lastBmIdNo, detailsEntity,
										adjustedBills, bill, detail, det,entity, deptCode);
							  detailsEntity.setAdjAdjustedFlag(constantNO);
							  detailsEntity.setAdjBalanceAmount(detail.getAdjAmount());
						}
                         mapping = new AdjustmentBillDetailMappingEntity();
                         mapping.setAdjAmount(detailsEntity.getAdjAdjustedAmount());
                         mapping.setAdjbmId(det!=null?det.getBdBilldetid():0);
                         mapping.setOrgId(entity.getOrgId());
                         mapping.setDpDeptId(entity.getDpDeptId());
                         mapping.setLgIpMac(entity.getLgIpMac());
                         mapping.setCreatedBy(entity.getCreatedBy());
                         mapping.setCreatedDate(new Date());
                         mapping.setTaxId(detail.getTaxId());
                         mappingList.add(mapping);
                         detailsEntity.setOrgId(org.getOrgid());
                         detailsEntity.setCreatedBy(empId);
                         detailsEntity.setCreatedDate(new Date());
                         detailsEntity.setLgIpMac(Utility.getMacAddress());
                         detailsEntity.setBdBilldetid(det!=null?det.getBdBilldetid():0);
                         detailsEntity.setBmIdNo(det!=null?det.getBmIdno():0);
                         amount += detailsEntity.getAdjAdjustedAmount();
                         entity.getAdjDetail().add(detailsEntity);
				   //  }
						}
                    }/*
						 * else { detailsEntity.setAdjAdjustedFlag(constantNO);
						 * detailsEntity.setAdjBalanceAmount(detailsEntity.getAdjAmount());
						 * detailsEntity.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid())
						 * ;
						 * detailsEntity.setCreatedBy(UserSession.getCurrent().getEmployee().getEmpId())
						 * ; detailsEntity.setCreatedDate(new Date());
						 * detailsEntity.setLgIpMac(Utility.getMacAddress()); amount +=
						 * detailsEntity.getAdjAdjustedAmount();
						 * entity.getAdjDetail().add(detailsEntity);
						 * 
						 * }
						 */
                }
            }
            final List<TbBillDet> current = bill.getTbWtBillDet();
            if ((current != null) && !current.isEmpty()) {
                for (final TbBillDet curdet : current) {
                    if (previous != null) {
                        for (final TbBillDet arrdet : previous) {
                            if (curdet.getTaxId().longValue() == arrdet.getTaxId().longValue()) {
                                curdet.setBdPrvBalArramt(arrdet.getBdPrvBalArramt() + arrdet.getBdCurBalTaxamt());
//                                curdet.setBdPrvArramt(arrdet.getBdPrvArramt() + arrdet.getBdCurTaxamt());
                            }
                        }
                    }
                }
                previous = current;
            }
            if (amount > 0d) {
                if (constantNO.equals(adjustmentDto.getAdjType())||constantWO.equals(adjustmentDto.getAdjType())) {
                    final double adjAmt = bill.getBmTotalBalAmount() - amount;
                    if (adjAmt >= 0d) {
                    	if(MainetConstants.WATER_DEPT.equals(deptCode)){
                    		bill.setBmTotalAmount(adjAmt);
                    	}
                        bill.setBmTotalBalAmount(adjAmt);
                    } else {
                    	if(MainetConstants.WATER_DEPT.equals(deptCode)){
                    		bill.setBmTotalAmount(0d);
                    	}
                        bill.setBmTotalBalAmount(0d);
                    }
                } else {
                	if(MainetConstants.WATER_DEPT.equals(deptCode)){
                		bill.setBmTotalAmount(bill.getBmTotalBalAmount() + amount);
                	}
                    bill.setBmTotalBalAmount(bill.getBmTotalBalAmount() + amount);
                }
            }
        }

        if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL) && 
    		(MainetConstants.WATER_DEPT.equals(deptCode)) &&
        		(positive.equals(adjustmentDto.getAdjType())||constantBoja.equals(adjustmentDto.getAdjType()))) {
        	if (billlist.get(0).getBmIdno() > 0d) {
   			 updateAdjustedBillModuleWise(billlist, adjustmentDto.getDpDeptId(), adjustedBills, entity,advanceAmount.doubleValue());
       	 }
        	// do nothing
        }else if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_ASCL) && 
    		(MainetConstants.WATER_DEPT.equals(deptCode)) &&
        		(positive.equals(adjustmentDto.getAdjType())||constantBoja.equals(adjustmentDto.getAdjType()))) {
        	// do nothing
        	
        }else {
        	
        	 if (billlist.get(0).getBmIdno() > 0d && !StringUtils.equals(MainetConstants.FlagA, adjustmentDto.getServiceFlag())) {
    			 updateAdjustedBillModuleWise(billlist, adjustmentDto.getDpDeptId(), adjustedBills, entity,advanceAmount.doubleValue());
        	 }
        }
        if((constantNO.equals(adjType) || constantWO.equals(adjType)) && !StringUtils.equals(MainetConstants.FlagA, adjustmentDto.getServiceFlag()) 
        		&& MainetConstants.DEPT_SHORT_NAME.PROPERTY.equals(deptCode)){
        	
        	List<AdjustmentDetailEntity> detailEntityList = new ArrayList<AdjustmentDetailEntity>();
        	List<Long> detailPkList = new ArrayList<Long>();
        	entity.getAdjDetail().forEach(det ->{
        		if(!detailPkList.contains(det.getAdjDetId())) {
        			AdjustmentDetailEntity detailEntity = new AdjustmentDetailEntity();
        			BeanUtils.copyProperties(det, detailEntity);
        		 	detailEntityList.add(detailEntity);
        			detailPkList.add(det.getAdjDetId());        			
        		}
        	});
        	entity.setAdjDetail(null);
        	entity.setAdjDetail(detailEntityList);
        }
        
        adjustmentEntryRepository.save(entity);
        if (!mappingList.isEmpty()) {
            for (final AdjustmentBillDetailMappingEntity mapping : mappingList) {
                for (final AdjustmentDetailEntity adjDetail : entity.getAdjDetail()) {
                    if (mapping.getTaxId().equals(adjDetail.getTaxId()) && mapping.getAdjId() == null) {
						List<AdjustmentBillDetailMappingEntity> ajdjList = mappingList.stream().filter(map -> (map.getAdjId() !=null && map.getAdjId().equals(adjDetail.getAdjDetId())))
								.collect(Collectors.toList());
                    	if(CollectionUtils.isEmpty(ajdjList)) {
                    		 mapping.setAdjId(adjDetail.getAdjDetId());
                    	}
                    }
                }
            }
            adjustmentBillDetailMappingRepository.save(mappingList);
        }
        if(constantWO.equals(adjType) && MainetConstants.DEPT_SHORT_NAME.PROPERTY.equals(deptCode)
        		&& !StringUtils.equals(MainetConstants.FlagA, adjustmentDto.getServiceFlag())){
        	updatePropertyActiveFlag(adjustmentDto.getAdjRefNo(), MainetConstants.FlagC, orgid,adjustmentDto.getFlatNo());
        }
        if(StringUtils.equals(MainetConstants.FlagA, adjustmentDto.getServiceFlag())) {
        	initiateWorkflow(adjustmentDto);
        }
		if (MainetConstants.DEPT_SHORT_NAME.PROPERTY.equals(deptCode)
				&& (positive.equals(adjustmentDto.getAdjType()) || constantNO.equals(adjustmentDto.getAdjType()))
				&& !StringUtils.equals(MainetConstants.FlagA, adjustmentDto.getServiceFlag())) {
			try {
				accountVoucherPosting(entity, org, deptCode, empId, lastBmIdNo);
			} catch (Exception e) {
				LOGGER.error("Error in background account voucher posting. BM ID: {}", lastBmIdNo, e);
			}
		}
		
        return true;
    }
    
	public void accountVoucherPosting(AdjustmentMasterEntity entity, Organisation org, String deptShortName, Long empId,
			long lastBmIdNo) {
		LOGGER.info(
				"accountVoucherPosting - Account voucher posting started. "
						+ "BM ID: {}, Department: {}, Employee ID: {}, Org ID: {}",
				lastBmIdNo, deptShortName, empId, org != null ? org.getOrgid() : null);
		try {
			if (entity == null || org == null) {
				return;
			}
			final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
					MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);

			if (accountActive == null || !PrefixConstants.IsLookUp.STATUS.YES.equals(accountActive.getDefaultVal())) {
				return;
			}

			final Department dept = departmentService.getDepartment(deptShortName, MainetConstants.STATUS.ACTIVE);
			if (dept == null) {
				LOGGER.warn("Department not found: {}", deptShortName);
				return;
			}
			
			final LookUp currDemandLookup = CommonMasterUtility.getValueFromPrefixLookUp("CYR", "DMC", org);
			final LookUp liabilityLookup = CommonMasterUtility.getValueFromPrefixLookUp("LBT", "DMC", org);
			final LookUp govtTaxGroupLookup = CommonMasterUtility.getValueFromPrefixLookUp("GVT", "TAG", org);
			final LookUp payModeLookup = CommonMasterUtility.getValueFromPrefixLookUp("T", MainetConstants.PAY_PREFIX.PREFIX_VALUE, org);

			if (currDemandLookup == null || liabilityLookup == null || govtTaxGroupLookup == null
					|| payModeLookup == null) {

				LOGGER.error("Required lookup configuration missing. Department: {}", deptShortName);
				return;
			}

			final Long currDemandId = currDemandLookup.getLookUpId();
			final Long liabilityId = liabilityLookup.getLookUpId();
			final Long govtTaxGrp = govtTaxGroupLookup.getLookUpId();
			final Long payModeId = payModeLookup.getLookUpId();

			final List<Long> govtTaxIdList = tbTaxMasService.fetchTaxIdByDeptIdForTaxGroup(govtTaxGrp, org.getOrgid(),
					dept.getDpDeptid());
			Long locationId = null;
			final TbLocationMas locMas = iLocationMasService.findByLocationName(
					ApplicationSession.getInstance().getMessage("location.LocNameEng"), org.getOrgid());
			if (locMas != null && CollectionUtils.isNotEmpty(locMas.getLocRevenueWZMappingDto())) {
				locationId = locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1();
			}
			
			List<VoucherPostDTO> accountPostingList = new ArrayList<>();
			final Map<Long, Long> sacHeadCache = new HashMap<>();

			for (AdjustmentDetailEntity detailEntity : entity.getAdjDetail()) {

				if (detailEntity != null && detailEntity.getAdjAdjustedAmount() > 0d) {
					accountPostingList.add(prepareVoucherDTO(org, empId, locationId, dept, detailEntity, lastBmIdNo,
							currDemandId, liabilityId, govtTaxGrp, govtTaxIdList, sacHeadCache, payModeId));
				}
			}
			if (CollectionUtils.isNotEmpty(accountPostingList)) {
				doVoucherPosting(accountPostingList, Arrays.asList(lastBmIdNo), deptShortName);
				LOGGER.info(
						"accountVoucherPosting - Account voucher posting completed successfully. "
								+ "BM ID: {}, Department: {}, Voucher Entries: {}",
						lastBmIdNo, deptShortName, accountPostingList.size());
			}

		} catch (Exception e) {
			LOGGER.error("error while account posting for adjustment entry of department " + deptShortName, e);
		}
	}

	private VoucherPostDTO prepareVoucherDTO(Organisation org, Long empId, Long locationId, Department dept,
			AdjustmentDetailEntity detailEntity, long bmIdNo, Long currDemandId, Long liabilityId, Long govtTaxGrp,
			List<Long> govtTaxIdList, Map<Long, Long> sacHeadCache, Long payModeId) {

		final VoucherPostDTO accountPosting = new VoucherPostDTO();
	    final Date postingDate = new Date();
	    final String referenceNo = String.valueOf(bmIdNo);
	    accountPosting.setVoucherDetails(new ArrayList<>());
	    accountPosting.setTemplateType("PN");
	    
	    final LookUp dmpPrefix = CommonMasterUtility.getValueFromPrefixLookUp(
	            MainetConstants.BILL_MASTER_COMMON.DMP,
	            MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX,
	            org);

	    if (dmpPrefix != null) {
	        accountPosting.setVoucherSubTypeId(dmpPrefix.getLookUpId());
	    }
	    accountPosting.setFieldId(locationId);
	    accountPosting.setCreatedDate(postingDate);
	    accountPosting.setVoucherDate(postingDate);
	    accountPosting.setVoucherReferenceDate(postingDate);
	    accountPosting.setDepartmentId(dept.getDpDeptid());
	    accountPosting.setVoucherReferenceNo(referenceNo);
	    
		accountPosting.setNarration(dept.getDpDeptdesc() + " "
				+ ApplicationSession.getInstance().getMessage("bill.lbl.taxes") + referenceNo);
		accountPosting.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
		accountPosting.setOrgId(org.getOrgid());
		accountPosting.setCreatedBy(empId);
		accountPosting.setLangId(MainetConstants.ENGLISH);
		accountPosting.setLgIpMac(Utility.getMacAddress());
		accountPosting.setPayModeId(payModeId);
		
		if (detailEntity.getAdjAdjustedAmount() > 0d) {
			VoucherPostDetailDTO voucherDetail = new VoucherPostDetailDTO();

			voucherDetail.setVoucherAmount(BigDecimal.valueOf(detailEntity.getAdjAdjustedAmount()));

			final Long taxId = detailEntity.getTaxId();
			Long sacHeadId;

			if (sacHeadCache.containsKey(taxId)) {
				sacHeadId = sacHeadCache.get(taxId);
			} else {
				sacHeadId = getSacHeadId(org, taxId, liabilityId, currDemandId, govtTaxGrp, govtTaxIdList);

				sacHeadCache.put(taxId, sacHeadId);
			}

			voucherDetail.setSacHeadId(sacHeadId);
			voucherDetail.setPayModeId(payModeId);
			accountPosting.getVoucherDetails().add(voucherDetail);
		}
		return accountPosting;
	}

	private Long getSacHeadId(Organisation org, Long taxId, Long liabilityId, Long currDemandId, Long govtTaxGrp,
			List<Long> govtTaxIdList) {

		final Long demandClassId = CollectionUtils.isNotEmpty(govtTaxIdList) && govtTaxIdList.contains(taxId)
				? liabilityId
				: currDemandId;

		return tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(), taxId,
				MainetConstants.STATUS.ACTIVE, demandClassId);
	}
	
	private void doVoucherPosting(List<VoucherPostDTO> accountPostingList, List<Long> billIds, String deptShortName) {
		LOGGER.info("AdjustmentEntryServiceImpl.doVoucherPosting >> Account Voucher Posting Begin");
		if (accountPostingList != null && !accountPostingList.isEmpty()) {
			VoucherPostListDTO dto = new VoucherPostListDTO();
			dto.setVoucherdto(accountPostingList);

			final ResponseEntity<?> responseValidate = RestClient.callRestTemplateClient(dto,
					ServiceEndpoints.ACCOUNT_POSTING_VALIDATE);
			if ((responseValidate != null) && (responseValidate.getStatusCode() == HttpStatus.OK)) {
				LOGGER.info("Account Voucher Posting validated successfully");
				final ResponseEntity<?> response = RestClient.callRestTemplateClient(dto,
						ServiceEndpoints.ACCOUNT_POSTING);
				if ((response != null) && (response.getStatusCode() == HttpStatus.OK)) {
					updateAccountPostingFlag(billIds, constantYES, deptShortName);
				} else {
					LOGGER.error(ApplicationSession.getInstance().getMessage("account.voucher.posting.failmsg")
							+ "dept " + deptShortName + " for Bill no:" + billIds.toString() + " "
							+ (response != null ? response.getBody() : ""));
				}
			} else {
				LOGGER.error("Account Voucher Posting Validation Failed due to :"
						+ (responseValidate != null ? responseValidate.getBody() : MainetConstants.BLANK));
			}
		}
	}

	private boolean updateAccountPostingFlag(final List<Long> billIds, final String flag, final String deptShortName) {
		boolean result = false;
		try {
			BillGenerationService dynamicServiceInstance = null;
			String serviceClassName = null;
			final ApplicationSession appSession = ApplicationSession.getInstance();
			serviceClassName = messageSource.getMessage(appSession.getMessage("bill.lbl.Bills") + deptShortName,
					new Object[] {}, StringUtils.EMPTY, Locale.ENGLISH);
			dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
					BillGenerationService.class);
			result = dynamicServiceInstance.updateAccountPostingFlag(billIds, flag);
		} catch (LinkageError | Exception e) {
			throw new FrameworkException(
					"Exception in updating jv post flag in bill for account posting for department :" + deptShortName
							+ " and bill id's are:" + billIds.toString(), e);
		}
		return result;
	}

	private void initiateWorkflow(AdjustmentMasterDTO adjustmentDto) {
		
		LOGGER.info("Begin -> "+this.getClass().getSimpleName()+" initiateWorkflow() method");
		
		ApplicationMetadata applicationData = new ApplicationMetadata();
        final ApplicantDetailDTO applicantDetailDto = new ApplicantDetailDTO();
        
        applicationData.setApplicationId(adjustmentDto.getApplicationId());
        applicationData.setOrgId(adjustmentDto.getOrgId());
        applicantDetailDto.setServiceId(adjustmentDto.getServiceId());
        applicantDetailDto.setDepartmentId(ApplicationContextProvider.getApplicationContext().getBean(DepartmentService.class)
				.getDepartmentIdByDeptCode("AS"));
        applicantDetailDto.setUserId(adjustmentDto.getCreatedBy());
        applicantDetailDto.setOrgId(adjustmentDto.getOrgId());
		applicationData.setIsCheckListApplicable(false);
		
		final ResponseEntity<?> responseEntity = RestClient.callRestTemplateClient(null,
				ApplicationSession.getInstance().getMessage("GET_WARD_ZONE_BY_PROP_NO")+adjustmentDto.getAdjRefNo());
		if ((responseEntity != null) && (responseEntity.getStatusCode() == HttpStatus.OK)) {
			WardZoneBlockDTO wardZoneBlockDTO = (WardZoneBlockDTO) RestClient.castResponse(responseEntity, WardZoneBlockDTO.class);
			if(wardZoneBlockDTO != null) {
			    if (wardZoneBlockDTO.getAreaDivision1() > 0) {
			        applicantDetailDto.setDwzid1(wardZoneBlockDTO.getAreaDivision1());
			    }
			    if (wardZoneBlockDTO.getAreaDivision2() > 0) {
			        applicantDetailDto.setDwzid2(wardZoneBlockDTO.getAreaDivision2());
			    }
			    if (wardZoneBlockDTO.getAreaDivision3() > 0) {
			        applicantDetailDto.setDwzid3(wardZoneBlockDTO.getAreaDivision3());
			    }
			    if (wardZoneBlockDTO.getAreaDivision4() > 0) {
			        applicantDetailDto.setDwzid4(wardZoneBlockDTO.getAreaDivision4());
			    }
			    if (wardZoneBlockDTO.getAreaDivision5() > 0) {
			        applicantDetailDto.setDwzid5(wardZoneBlockDTO.getAreaDivision5());
			    }
			}
		}
		commonService.initiateWorkflowfreeService(applicationData, applicantDetailDto);
		
		LOGGER.info("End -> "+this.getClass().getSimpleName()+" initiateWorkflow() method");
	}

	private void positiveAdjustmentLogic(final AdjustmentMasterDTO adjustmentDto, AdjustmentDetailEntity detailsEntity,
			final AdjustmentDetailDTO detail, final TbBillDet det,Organisation org, String deptCode) {
		if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL) && 
			(MainetConstants.WATER_DEPT.equals(deptCode))) {
			//do not adjust in current bill for positive adjustment for water department SKDCL
		    detailsEntity.setAdjAdjustedFlag(constantNO);
		    detailsEntity.setAdjBalanceAmount(detail.getAdjAmount());
		    detailsEntity.setAdjAdjustedAmount(0d);
		 //   if(det!=null) {
		    //det.setBdCurBalTaxamt(detail.getAdjAmount() + det.getBdCurBalTaxamt());
		//	det.setBdCurTaxamt(detail.getAdjAmount() + det.getBdCurTaxamt());
		 //   }
		}else if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_ASCL) && 
			(MainetConstants.WATER_DEPT.equals(deptCode))) {
			
			detailsEntity.setAdjAdjustedFlag(constantNO);
		    detailsEntity.setAdjBalanceAmount(detail.getAdjAmount());
		    detailsEntity.setAdjAdjustedAmount(0d);
		}else {
			det.setBdCurBalTaxamt(detail.getAdjAmount() + det.getBdCurBalTaxamt());
			det.setBdCurTaxamt(detail.getAdjAmount() + det.getBdCurTaxamt());
		    detailsEntity.setAdjAdjustedFlag(constantYES);
		    detailsEntity.setAdjBalanceAmount(0d);
		    detailsEntity.setAdjAdjustedAmount(detail.getAdjAmount());
		}
		detail.setAdjAmount(0);
	}

	private void negativeOrWaiveOffAdjustmentLogic(final AdjustmentMasterDTO adjustmentDto, AtomicDouble advanceAmount,
			Long lastBmIdNo, AdjustmentDetailEntity detailsEntity, final List<TbBillDet> adjustedBills,
			final TbBillMas bill, final AdjustmentDetailDTO detail, final TbBillDet det,AdjustmentMasterEntity entity, String deptCode) {
        if(det !=null) {
			if(detail.getAdjAmount() > det.getBdCurBalTaxamt()) {
				det.setNegAdjAmnt(det.getBdCurBalTaxamt() + det.getNegAdjAmnt());
			}else {
				det.setNegAdjAmnt(detail.getAdjAmount() + det.getNegAdjAmnt());
			}
		final double adjAmount = det.getBdCurBalTaxamt() - detail.getAdjAmount();
		if (adjAmount >= 0d) {
			  if(MainetConstants.WATER_DEPT.equals(deptCode))
			    {  det.setBdCurTaxamt(adjAmount);}
		    det.setBdCurBalTaxamt(adjAmount);
		    detailsEntity.setAdjAdjustedAmount(detail.getAdjAmount());
		    detailsEntity.setAdjBalanceAmount(0d);
		    detailsEntity.setAdjAdjustedFlag(constantYES);
		    detail.setAdjAmount(0);
		} 
                           
		else {
		    detailsEntity.setAdjAdjustedAmount(det.getBdCurBalTaxamt());
		    if(MainetConstants.WATER_DEPT.equals(deptCode))
		    {  
		    	det.setBdCurTaxamt(0d);
		    }
		    det.setBdCurBalTaxamt(0d);
		    detailsEntity.setAdjBalanceAmount(Math.abs(adjAmount));
		    detailsEntity.setAdjAdjustedFlag(MainetConstants.WATER_DEPT.equals(
		    		deptCode) ? 
					constantYES: constantNO);
		    detail.setAdjAmount(Math.abs(adjAmount));
		}
		if(MainetConstants.WATER_DEPT.equals(deptCode)) {
			adjustedBills.add(det);
		}
        }
		if(lastBmIdNo.equals(bill.getBmIdno())) {
			if(detail.getAdjAmount() > 0) {
				advanceAmount.addAndGet(detail.getAdjAmount());
				detailsEntity.setAdvanceAmount(detail.getAdjAmount());
				detailsEntity.setAdvanceAdjFlag(MainetConstants.FlagN);
				entity.setAdjAdvnceAmntFlag(MainetConstants.FlagN);
			}
			
		}
	}

    private List<TbBillMas> updateAdjustedBillModuleWise(final List<TbBillMas> billlist, final Long dpDeptId,
		List<TbBillDet> adjustedBills, AdjustmentMasterEntity adjustmentEntity,double advanceAmount) {
        List<TbBillMas> result = null;
        try {
            BillGenerationService dynamicServiceInstance = null;
            String serviceClassName = null;
            final String deptCode = departmentService.getDeptCode(dpDeptId);
            serviceClassName = messageSource.getMessage(
                    "Bill." + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillGenerationService.class);
            result = dynamicServiceInstance.updateAdjustedCurrentBill(billlist, adjustedBills, adjustmentEntity, dpDeptId,advanceAmount);
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in updating bill for Adjustment entry for Department: " + dpDeptId, e);
        }
        return result;

    }

    @Override
    @Transactional
    public List<AdjustmentMasterDTO> fetchHistory(final String refNo, final Long dpDeptId,String flatNo) {
    	 List<AdjustmentMasterEntity> adj = null;
    	 if(StringUtils.isNotBlank(flatNo)) {
    		 adj = adjustmentEntryRepository.fetchHistoryByFlatNo(refNo, dpDeptId,flatNo);
    	 }else {
    		 adj = adjustmentEntryRepository.fetchHistory(refNo, dpDeptId);
    	 }
        final List<AdjustmentMasterDTO> result = new ArrayList<>(0);
        List<AdjustmentDetailDTO> detailresult = null;
        AdjustmentMasterDTO dto = null;
        AdjustmentDetailDTO detalDto = null;
        if ((adj != null) && !adj.isEmpty()) {
            for (final AdjustmentMasterEntity entity : adj) {
                dto = new AdjustmentMasterDTO();
                detailresult = new ArrayList<>(0);
                BeanUtils.copyProperties(entity, dto);
                for (final AdjustmentDetailEntity detail : entity.getAdjDetail()) {
                    detalDto = new AdjustmentDetailDTO();
                    BeanUtils.copyProperties(detail, detalDto);
                    detalDto.setTaxDesc(tbTaxMasService.findTaxDescByTaxIdAndOrgId(detalDto.getTaxId(), detalDto.getOrgId()));
                    detailresult.add(detalDto);
                }
                dto.setAdjDetailDto(detailresult);
                if (positive.equals(dto.getAdjType())||constantBoja.equals(dto.getAdjType())) {
                    dto.setAdjType(MainetConstants.AdjustmentType.POSITIVE);
                } else if(palavaExemption.equals(dto.getAdjType())) {
                	dto.setAdjType(MainetConstants.AdjustmentType.PALAVA_EXEMPTION);
                } else {
                    dto.setAdjType(MainetConstants.AdjustmentType.NEGATIVE);
                }
                result.add(dto);
            }
        }
        return result;
    }

    private List<AdjustmentMasterEntity> fetchModuleWiseAdjustmentByUniqueIds(final Long deptId, final List<String> uniqueIds,
            final long orgid) {
        final List<AdjustmentMasterEntity> adj = adjustmentEntryRepository.fetchModuleWiseAdjustmentByUniqueIds(deptId, uniqueIds,
                orgid);
        return adj;
    }

    @Override
    @Transactional
    public void updateAdjustedAdjustmentData(final List<AdjustmentMasterEntity> adjustmentEntity) {
        adjustmentEntryRepository.save(adjustmentEntity);
    }
    
    @Transactional
	@Override
	public void updateBmIdnoInAdjustedAdjustmentDet(Long adjId, Long bdBilldetid, Long orgId) {
		  adjustmentEntryRepository.updateBmIdnoInAdjustedAdjustmentDet(adjId, bdBilldetid, orgId);
	}

    @Override
    public Map<Long, List<AdjustmentMasterEntity>> fetchAllAdjustmentEntry(final List<String> uniqueId, final long orgid,
            final Long deptId) {
        final List<AdjustmentMasterEntity> entity = fetchModuleWiseAdjustmentByUniqueIds(deptId, uniqueId,
                orgid);
        final Map<Long, List<AdjustmentMasterEntity>> adjustment = new HashMap<>(0);
        List<AdjustmentMasterEntity> beans = null;
        if ((entity != null) && !entity.isEmpty()) {
            for (final AdjustmentMasterEntity adjEntity : entity) {
                beans = adjustment.get(Long.valueOf(adjEntity.getAdjRefNo()));
                if (beans == null) {
                    beans = new ArrayList<>(0);
                }
                beans.add(adjEntity);
                adjustment.put(Long.valueOf(adjEntity.getAdjRefNo()), beans);
            }
        }
        return adjustment;
    }
    
    @Override
    @Transactional
    public String getPropNoByOldPropNo(final String oldPropNo, final Long orgId,Long deptId) {
        String result = null;
        try {
            BillGenerationService dynamicServiceInstance = null;
            String serviceClassName = null;
            final String deptCode = departmentService.getDeptCode(deptId);
            serviceClassName = messageSource.getMessage(
                    ApplicationSession.getInstance().getMessage("bill.lbl.Bills") + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillGenerationService.class);
            result = dynamicServiceInstance.getPropNoByOldPropNo(oldPropNo, orgId);
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in fetching property nu,ber by old prop no " + deptId, e);
        }
        return result;
    }
    
    @Override
    @Transactional
    public Long getBillMethodIdByPropNo(final String oldPropNo, final Long orgId, final String deptCode) {
        Long result = null;
        try {
        	Class<?> clazz = null;
            Object dynamicServiceInstance = null;
            String serviceClassName = "com.abm.mainet.property.service.PrimaryPropertyServiceImpl";
            
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz,
                    "getBillMethodIdByPropNo",
                    new Class[] { String.class, Long.class });
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            result = (Long) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { oldPropNo, orgId });
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in fetching billing method by property number " + deptCode, e);
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<String>  getFlatNoIdByPropNo(final String oldPropNo, final Long orgId, final String deptCode) {
    	List<String>  result = null;
        try {
        	 Class<?> clazz = null;
             Object dynamicServiceInstance = null;
             String serviceClassName = "com.abm.mainet.property.service.PrimaryPropertyServiceImpl";
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz,
                    "getFlatNoIdByPropNo",
                    new Class[] { String.class, Long.class });
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            
            result = (List<String>) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { oldPropNo, orgId });
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in fetching flat no by property number " + deptCode, e);
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<TbBillMas> fetchAllBillByPropNoAndFlatNo(final Long deptId, final String ccnNoOrPropNo, final String flatNo, final Long orgId) {
        List<TbBillMas> result = null;
        try {
        	 Class<?> clazz = null;
             Object dynamicServiceInstance = null;
        	String serviceClassName = "com.abm.mainet.property.service.PropertyMainBillServiceImpl";
            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
                    .getClassLoader());
            final Method method = ReflectionUtils.findMethod(clazz,
                    "fetchAllBillByPropNoAndFlatNo",
                    new Class[] { String.class, String.class, long.class });
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
                    .autowire(clazz, 4, false);
            
            result = (List<TbBillMas>) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
                    new Object[] { ccnNoOrPropNo , flatNo, orgId });
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in fetching bill for Adjustment entry for Department: " + deptId, e);
        }
        return result;
    }

	@Override
	public CustomerInfoDTO getCsmrInfoDetails(String ccnNoOrPropNo, Long orgId) {
		CustomerInfoDTO result = null;
	        try {
	        	Class<?> clazz = null;
	            Object dynamicServiceInstance = null;
	        	String serviceClassName = "com.abm.mainet.water.service.NewWaterConnectionServiceImpl";
	            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
	                    .getClassLoader());
	            final Method method = ReflectionUtils.findMethod(clazz,
						ApplicationSession.getInstance().getMessage("common.water.csmrinfo"),
						new Class[] {String.class, Long.class });
	            
	            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
	                    .autowire(clazz, 4, false);
	            
	            result = (CustomerInfoDTO) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
	                    new Object[] { ccnNoOrPropNo, orgId });
	        } catch (LinkageError | Exception e) {
	        	LOGGER.error("Exception in fetching csmr info for conNo: " + ccnNoOrPropNo + " " + e.getMessage());
	        }
	        return result;
	}
	
	@Override
	public Long getCsIdn(String ccnNoOrPropNo, Long orgId) {
		Long result = null;
	        try {
	        	Class<?> clazz = null;
	            Object dynamicServiceInstance = null;
	        	String serviceClassName = "com.abm.mainet.water.service.NewWaterConnectionServiceImpl";
	            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
	                    .getClassLoader());
	            final Method method = ReflectionUtils.findMethod(clazz,"getCsIdn",
						new Class[] {String.class, Long.class });
	            
	            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
	                    .autowire(clazz, 4, false);
	            
	            result = (Long) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
	                    new Object[] { ccnNoOrPropNo, orgId });
	        } catch (LinkageError | Exception e) {
	        	LOGGER.error("Exception in fetching csmr info for conNo: " + ccnNoOrPropNo + " " + e.getMessage());
	        }
	        return result;
	}
	@Override
	public void updatePropertyActiveFlag(String ccnNoOrPropNo,String status ,Long orgId,String flatNo) {
		
	        try {
	        	Class<?> clazz = null;
	            Object dynamicServiceInstance = null;
	            String serviceClassName = "com.abm.mainet.property.service.PropertyServiceImpl";
	            
	            clazz = ClassUtils.forName(serviceClassName, ApplicationContextProvider.getApplicationContext()
	                    .getClassLoader());
	            final Method method = ReflectionUtils.findMethod(clazz,
	                    "updatePropertyActiveFlag",
	                    new Class[] { String.class,String.class, Long.class,String.class });
	            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
	                    .autowire(clazz, 4, false);
	          ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
	                    new Object[] { ccnNoOrPropNo,status, orgId ,flatNo});
	        } catch (LinkageError | Exception e) {
	            throw new FrameworkException("Exception in updatePropertyActiveFlag by property number "+ e);
	        }
	     
	}

	@Override
	@Transactional(readOnly = true)
	public AdjustmentMasterDTO getAdjustmentDetailsByApplicationId(Long applicationId, Long orgId) {
		AdjustmentMasterDTO adjustmentMasterDTO = null;
        AdjustmentDetailDTO adjustmentDetailDTO = null;
        List<AdjustmentDetailDTO> adjustmentDetailDTOList = new ArrayList<AdjustmentDetailDTO>();
		AdjustmentMasterEntity adjustmentMasterEntity = adjustmentEntryRepository.getAdjustmentDetailsByApplicationId(applicationId, orgId);
		if(null != adjustmentMasterEntity) {
			adjustmentMasterDTO = new AdjustmentMasterDTO();
			BeanUtils.copyProperties(adjustmentMasterEntity, adjustmentMasterDTO);
			for(AdjustmentDetailEntity adjustmentDetailEntity : adjustmentMasterEntity.getAdjDetail()) {
				adjustmentDetailDTO = new AdjustmentDetailDTO();
				BeanUtils.copyProperties(adjustmentDetailEntity, adjustmentDetailDTO);
				adjustmentDetailDTOList.add(adjustmentDetailDTO);
			}
			adjustmentMasterDTO.setAdjDetailDto(adjustmentDetailDTOList);
		}
		
		return adjustmentMasterDTO;
	}
    
}
