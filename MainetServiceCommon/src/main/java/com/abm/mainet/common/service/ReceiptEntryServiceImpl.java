package com.abm.mainet.common.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.abm.mainet.bill.service.BillPaymentService;
import com.abm.mainet.cfc.challan.dto.ChallanReceiptPrintDTO;
import com.abm.mainet.cfc.challan.dto.ChallanReportDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.FinancialYear;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.ServiceMaster;
import com.abm.mainet.common.dto.BillReceiptPostingDTO;
import com.abm.mainet.common.dto.BillReceiptPrintingDTO;
import com.abm.mainet.common.dto.CFCSchedulingCounterDet;
import com.abm.mainet.common.dto.CommonChallanDTO;
import com.abm.mainet.common.dto.CommonChallanPayModeDTO;
import com.abm.mainet.common.dto.WardZoneBlockDTO;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptFeesDetEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptModesDetEntity;
import com.abm.mainet.common.integration.acccount.dto.TbServiceReceiptMasBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptFeesDetBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptModesDetBean;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDTO;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDetailDTO;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostListDTO;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.master.dao.IReceiptEntryDao;
import com.abm.mainet.common.master.dto.BankMasterDTO;
import com.abm.mainet.common.master.dto.TbLocationMas;
import com.abm.mainet.common.master.dto.TbTaxMas;
import com.abm.mainet.common.master.service.BankMasterService;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.TbCfcApplicationMstService;
import com.abm.mainet.common.master.service.TbServicesMstService;
import com.abm.mainet.common.master.service.TbTaxMasService;
import com.abm.mainet.common.repository.ReceiptRepository;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.RestClient;
import com.abm.mainet.common.utility.SeqGenFunctionUtility;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.utility.UtilityService;
import com.google.common.util.concurrent.AtomicDouble;

@Service
public class ReceiptEntryServiceImpl implements IReceiptEntryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptEntryServiceImpl.class);

    @Resource
    private SeqGenFunctionUtility seqGenFunctionUtility;

    @Autowired
    private TbTaxMasService tbTaxMasService;

    @Autowired
    private ServiceMasterService serviceMasterService;

    @Autowired
    private ILocationMasService iLocationMasService;
    
    @Resource
    private DepartmentService departmentService;

    @Resource
    private CommonService commonService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private IFinancialYearService iFinancialYear;

    @Autowired
    private IReceiptEntryDao receiptEntryDao;
    
    @Autowired
	private IOrganisationService iOrganisationService;
    
    @Autowired
	private TbServicesMstService iTbServicesMstService;
    
    @Resource
   	private BankMasterService bankMasterService;
      

    @Autowired
    TbCfcApplicationMstService tbCfcAppService;
    
    @Autowired
    private MessageSource messageSource;
    
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
    public TbServiceReceiptMasEntity insertInReceiptMaster(final CommonChallanDTO requestDTO,
            List<BillReceiptPostingDTO> billTaxes) {
        final TbServiceReceiptMasEntity receiptMasEntity = new TbServiceReceiptMasEntity();
        TbServiceReceiptMasEntity receiptMasEntityRebate = null;
        TbServiceReceiptMasEntity receiptMasEntityAbhayYojana = null;
        TbServiceReceiptMasEntity receiptMasEntityWebRebate = null;
        final List<TbSrcptFeesDetEntity> receiptFeeDetail = new CopyOnWriteArrayList<>();
        final ApplicationSession appSession = ApplicationSession.getInstance();
        //User Story #147721 for setting POS Payment mode
        receiptMasEntity.setPosPayMode(requestDTO.getPosPayMode());
        receiptMasEntity.setPosTxnId(requestDTO.getPosTxnId());
        receiptMasEntity.setRmN3(requestDTO.getRmN3());
        String payMode=MainetConstants.BLANK;
        if(requestDTO.getPayModeIn()!=null) {
   		  payMode=CommonMasterUtility.getCPDDescription(requestDTO.getPayModeIn(), MainetConstants.FlagV,requestDTO.getOrgId());
   	   }
        if (requestDTO != null) {
            final List<VoucherPostDTO> accountPostingList = new CopyOnWriteArrayList<>();
            final Organisation org = iOrganisationService.getOrganisationById(requestDTO.getOrgId());
            //org.setOrgid(requestDTO.getOrgId());
            //D#145761
           if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)) {
                try {
             	   CFCSchedulingCounterDet  entry= tbCfcAppService.getCounterDetByEmpId(requestDTO.getUserId(),requestDTO.getOrgId());
             	 
             	//#148057- to get counter no. and center no. details with count
             	  if(entry!=null) {
             		  if((StringUtils.isBlank(requestDTO.getPaymentCategory()) || !StringUtils.equals("AA", requestDTO.getPaymentCategory()))&& !StringUtils.equals(MainetConstants.FlagW,payMode)) {
             			 tbCfcAppService.setRecieptCfcAndCounterCount(entry);
                		  receiptMasEntity.setRmCFfcCntrNo(entry.getDwzId1());
                		  receiptMasEntity.setCfcColCenterNo(entry.getCollcntrno());
                		  receiptMasEntity.setCfcColCounterNo(entry.getCounterno());
             		  }
             	   }
                }catch (Exception e) {
                	 LOGGER.info("Exception occured at the time of fetching CFC counter details data");
         	}	
           }
            final VoucherPostDTO accountPosting = new VoucherPostDTO();
            List<VoucherPostDetailDTO> voucherDetails=new CopyOnWriteArrayList<VoucherPostDetailDTO>();
            accountPosting.setVoucherDetails(voucherDetails);
            Long locationId = null;
            String activeFlag = null;

            final Long govtTaxGrp = CommonMasterUtility.getValueFromPrefixLookUp("GVT", "TAG", org).getLookUpId();
            List<Long> govtTaxId = tbTaxMasService.fetchTaxIdByDeptIdForTaxGroup(govtTaxGrp, requestDTO.getOrgId(),
                    requestDTO.getDeptId());

            final Long currDemandId = CommonMasterUtility.getValueFromPrefixLookUp("CYR", "DMC", org).getLookUpId();
            final Long liability = CommonMasterUtility.getValueFromPrefixLookUp("LBT", "DMC", org).getLookUpId();

            final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
                    MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
            Date SLIDate = null;
            if (accountActive != null) {
                activeFlag = accountActive.getDefaultVal();
                if(accountActive.getOtherField() != null) {
                	String sliDate = accountActive.getOtherField();
                    try {
        				SLIDate = new SimpleDateFormat("dd/MM/yyyy").parse(sliDate);
        			} catch (ParseException exception) {
        				LOGGER.error("Problem while converting string to date", exception.getMessage());
        			}
                }
            }
            WardZoneBlockDTO dwzDTO = null;
            if (requestDTO.getApplNo() != null && requestDTO.getApplNo() != 0) {
                dwzDTO = commonService.getWordZoneBlockByApplicationId(
                        requestDTO.getApplNo(), requestDTO.getServiceId(),
                        requestDTO.getOrgId());
            } else {
                dwzDTO = requestDTO.getDwzDTO(); // in case of data entry suite where appId is null #12209
            }
            locationId = requestDTO.getLoggedLocId();
            if (locationId != null) {
                final TbLocationMas locMas = iLocationMasService.findById(locationId);
                if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
                    locationId = locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1();
                }
            } else {
                final TbLocationMas locMas = iLocationMasService
                        .findByLocationName(ApplicationSession.getInstance().getMessage("location.LocNameEng"), org.getOrgid());
                if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
                    locationId = locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1();
                }
            }
            //List<CommonChallanPayModeDTO> multiModeList = new CopyOnWriteArrayList<CommonChallanPayModeDTO>();
            //multiModeList.addAll(requestDTO.getMultiModeList());
            prepareReceiptMasAndMode(requestDTO, receiptMasEntity, appSession, dwzDTO, locationId,billTaxes);
            prepareVoucherDtoAndMode(requestDTO, receiptMasEntity, org, accountPosting, locationId, activeFlag);
            Long receiptNumberRebate = null;
            List<Long> advance = new ArrayList<>();
            List<Long> interest = new ArrayList<>();
            List<Long> rebate = new ArrayList<>();
            List<Long> penalty = new ArrayList<>();
            List<Long> demand = new ArrayList<>();
            List<Long> exemption = new ArrayList<>();
            List<Long> webRebate = new ArrayList<>();
            Map<Long, Long> taxHeadId = new HashMap<>();
            if (MainetConstants.CHALLAN_RECEIPT_TYPE.NON_REVENUE_BASED
                    .equals(requestDTO.getChallanServiceType())) {
                setFeeDetailAndVoucherDetail(requestDTO, receiptMasEntity, accountPosting, activeFlag, receiptFeeDetail,
                        currDemandId);
                
            } else if (MainetConstants.CHALLAN_RECEIPT_TYPE.OBJECTION
                    .equals(requestDTO.getChallanServiceType())) {
                setFeeDetailAndVoucherDetail(requestDTO, receiptMasEntity, accountPosting, activeFlag, receiptFeeDetail,
                        currDemandId);
            } else {
                receiptMasEntity.setRmDemand(BigDecimal.valueOf(billTaxes.get(0).getTotalDemand()));
                List<BillReceiptPrintingDTO> printDtoList = getBillReceiptPrintingDtoList(billTaxes);
                requestDTO.setPrintDtoList(printDtoList);
                prepareTaxCategory(org, advance, interest, rebate, penalty, demand,exemption, webRebate);
                double earlyPayAmnt = 0;
                double webEarlyPayAmnt = 0;
                for (BillReceiptPostingDTO billDef : billTaxes) {
                    if (billDef.getTaxAmount() > 0) {
                        if (billDef.getBmIdNo() != null && billDef.getBillDate() != null) {
                            requestDTO.getBillDetails().put(billDef.getBmIdNo(),
                                    new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(billDef.getBillDate()));
                        }
                        if (rebate.contains(billDef.getTaxCategory())) {
                        	earlyPayAmnt = earlyPayAmnt + billDef.getTaxAmount();
                            receiptMasEntityRebate = saveRebateReceipt(requestDTO, receiptMasEntity, org,
                                    taxHeadId, billDef, currDemandId,earlyPayAmnt);
                            receiptNumberRebate = receiptMasEntityRebate.getRmRcptno();

                        }else if(exemption.contains(billDef.getTaxCategory())) {
                        	receiptMasEntityAbhayYojana = saveAbhayYojanaReceipt(requestDTO, receiptMasEntity, org,
                                    taxHeadId, billTaxes, currDemandId,billDef.getPayableAmount(),exemption);
                           // receiptNumberRebate = receiptMasEntityRebate.getRmRcptno();
                        }else if(webRebate.contains(billDef.getTaxCategory())) {
                        	webEarlyPayAmnt = webEarlyPayAmnt + billDef.getTaxAmount();
                        	receiptMasEntityWebRebate = saveWebRebateReceipt(requestDTO, receiptMasEntity, org,
                                    taxHeadId, billDef, currDemandId, webEarlyPayAmnt, webRebate);	
                        } else {
                            setReceiptDetailForBill(requestDTO, receiptMasEntity, taxHeadId, billDef, receiptFeeDetail,
                                    currDemandId,
                                    govtTaxId, liability, org, activeFlag);
                        }
                    }
                }
			
            }

            receiptMasEntity.setVmVendorId(requestDTO.getVendorId());
            receiptMasEntity.setRmReceiptcategoryId(requestDTO.getReceiptcategoryId());
            receiptMasEntity.setReceiptFeeDetail(receiptFeeDetail);
            // receiptMasEntity.setRmReceivedfrom(requestDTO.getApplicantFullName());
            // D#81402
            receiptMasEntity.setPgRefId(requestDTO.getPgRefId());
            final Long receiptNumber = seqGenFunctionUtility.generateSequenceNo(
                    MainetConstants.RECEIPT_MASTER.Module,
                    MainetConstants.RECEIPT_MASTER.Table,
                    MainetConstants.RECEIPT_MASTER.Column, requestDTO.getOrgId(),
                    MainetConstants.RECEIPT_MASTER.Reset_Type,
                    requestDTO.getDeptId());
            receiptMasEntity.setRmRcptno(receiptNumber);
            accountPosting.setVoucherReferenceNo(receiptMasEntity.getRmRcptno().toString());
            if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)) {
				
            	if (receiptMasEntity != null && (CollectionUtils.isEmpty(receiptMasEntity.getReceiptFeeDetail())
						|| CollectionUtils.isEmpty(receiptMasEntity.getReceiptModeDetail()))) {
					LOGGER.error("Parameters   >>>>>>>>> Reciept No "+receiptNumber+"  Service Id"+requestDTO.getServiceId()+" DeptId "+requestDTO.getDeptId()+" ApplicationId"+requestDTO.getApplNo());
					throw new FrameworkException("Receipt Fee or Receipt Mode is empty");
				}
            	
				/*
				 * if ((CollectionUtils.isNotEmpty(multiModeList) && !(multiModeList.size() >
				 * 1))) { AtomicBoolean duplicateFlag = new AtomicBoolean(false);
				 * receiptMasEntity.getReceiptModeDetail().forEach(mode -> {
				 * List<TbSrcptModesDetEntity> modeList =
				 * receiptMasEntity.getReceiptModeDetail().stream() .filter(recMode ->
				 * recMode.getCpdFeemode().equals(mode.getCpdFeemode()) &&
				 * recMode.getRdAmount().equals(mode.getRdAmount()))
				 * .collect(Collectors.toList());
				 * 
				 * if(CollectionUtils.isNotEmpty(modeList) && modeList.size() > 1) {
				 * duplicateFlag.getAndSet(true); } }); if (duplicateFlag.get()) { throw new
				 * FrameworkException("Duplicate Mode"); } }
				 */
				
//				if(MainetConstants.WATER_DEPT.equals(departmentService.getDeptCode(receiptMasEntity.getDpDeptId()))
//						&& (receiptMasEntity.getAdditionalRefNo() == null || receiptMasEntity.getAdditionalRefNo().isEmpty())) {
//					receiptMasEntity.setAdditionalRefNo(requestDTO.getReferenceNo());
//				}
			
            }
            LOGGER.error("Receipt saving data -> "+ receiptMasEntity.toString());
            setRemittanceId(receiptMasEntity);
            if(StringUtils.equals(MainetConstants.FlagB, receiptMasEntity.getReceiptTypeFlag()) && StringUtils.equals(MainetConstants.CHALLAN_RECEIPT_TYPE.REVENUE_BASED, requestDTO.getChallanServiceType())) {
            	receiptMasEntity.setReceiptTypeFlag("R");
            }
			//setting aple sarkar application id for payment services.
            if(null != requestDTO.getApleSarkarId()) {
            	receiptMasEntity.setAapleSarkarId(requestDTO.getApleSarkarId());
            }
            receiptRepository.saveAndFlush(receiptMasEntity);
            if (receiptMasEntityRebate != null) {
                receiptMasEntityRebate.setRefId(receiptMasEntity.getRmRcptid());
                receiptMasEntityRebate.setPgRefId(requestDTO.getPgRefId());
				if (CollectionUtils.isNotEmpty(receiptMasEntityRebate.getReceiptFeeDetail())) {
					removeDuplicateEntryIfExist(receiptMasEntityRebate, requestDTO, org, "WR");
					double rebateAmount = receiptMasEntityRebate.getReceiptFeeDetail().stream()
							.filter(det1 -> det1.getRfFeeamount() != null)
							.mapToDouble(det -> det.getRfFeeamount().doubleValue()).sum();
					receiptMasEntityRebate.setRmAmount(new BigDecimal(Math.ceil(rebateAmount)));
				}
                receiptRepository.save(receiptMasEntityRebate);
                receiptMasEntity.setRefId(receiptMasEntityRebate.getRmRcptid());
            }
            if (receiptMasEntityAbhayYojana != null) {
            	receiptMasEntityAbhayYojana.setRefId(receiptMasEntity.getRmRcptid());
                receiptRepository.save(receiptMasEntityAbhayYojana);
            }
            if (receiptMasEntityWebRebate != null) {
            	receiptMasEntityWebRebate.setRefId(receiptMasEntity.getRmRcptid());
            	receiptMasEntityWebRebate.setPgRefId(requestDTO.getPgRefId());
            	if (CollectionUtils.isNotEmpty(receiptMasEntityWebRebate.getReceiptFeeDetail())) {
            		removeDuplicateEntryIfExist(receiptMasEntityWebRebate, requestDTO, org, "R");
					double rebateAmount = receiptMasEntityWebRebate.getReceiptFeeDetail().stream()
							.filter(det1 -> det1.getRfFeeamount() != null)
							.mapToDouble(det -> det.getRfFeeamount().doubleValue()).sum();
					receiptMasEntityWebRebate.setRmAmount(new BigDecimal(Math.ceil(rebateAmount)));
					if (CollectionUtils.isNotEmpty(receiptMasEntityWebRebate.getReceiptModeDetail())) {
						receiptMasEntityWebRebate.getReceiptModeDetail().get(0).setRdAmount(new BigDecimal(Math.ceil(rebateAmount)));						
					}
				}
                receiptRepository.save(receiptMasEntityWebRebate);
            }
			if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)
					&& (SLIDate != null && (Utility.compareDate(SLIDate, receiptMasEntity.getRmDate())
							|| Utility.comapreDates(SLIDate, receiptMasEntity.getRmDate())))) {
				try {
					  
			            if(!MainetConstants.CHALLAN_RECEIPT_TYPE.NON_REVENUE_BASED
			                    .equals(requestDTO.getChallanServiceType()) && !MainetConstants.CHALLAN_RECEIPT_TYPE.OBJECTION
			                    .equals(requestDTO.getChallanServiceType())) {
			                    List<Long> excludetax = new ArrayList<>();
			                    excludetax.addAll(advance);
			                    excludetax.addAll(penalty);
			                    excludetax.addAll(interest);
			                    excludetax.addAll(rebate);

			                    List<BillReceiptPostingDTO> billTaxesAccPost = billTaxes.stream().filter(bill -> bill.getTaxAmount() > 0)
			                            .collect(Collectors.toList());

			                    setDemandPostingDetails(billTaxesAccPost, org, accountPosting, demand, excludetax);// for demand tax
			                    setTaxesPostingDetails(billTaxesAccPost, accountPosting, advance, taxHeadId);// for advance tax
			                    setTaxesPostingDetails(billTaxesAccPost, accountPosting, interest, taxHeadId);// for interest tax
			                    setTaxesPostingDetails(billTaxesAccPost, accountPosting, penalty, taxHeadId);// for penalty tax
			                    setAndPostRebateDetails(requestDTO, billTaxesAccPost, receiptMasEntity, accountPostingList, org,
			                            receiptNumberRebate, rebate, taxHeadId);
			                    /*
			                     * commented due to posting is diffrent for govt setAndPostGovtTax(requestDTO, billTaxes, receiptMasEntity,
			                     * accountPostingList, org, taxHeadId, govtTaxId);
			                     */
			                
			            }
					doAccountVoucherPosting(receiptMasEntity.getRmNarration(), accountPostingList, accountPosting,
							activeFlag);
				} catch (Exception exception) {
					LOGGER.error("Exception occured while account posting"+exception);
				}
			}
        }
		
		
		/*
		 * if (StringUtils.equals(MainetConstants.FlagW, payMode)) { try {
		 * bankDepositEntry(receiptMasEntity, payMode); } catch (Exception e) {
		 * LOGGER.error("Exception occured while Bank deposit Entry posting" + e); } }
		 */

        return receiptMasEntity;
    }

    private void removeDuplicateEntryIfExist(TbServiceReceiptMasEntity receiptMasEntityRebate,
			CommonChallanDTO requestDTO, Organisation org, String rebateType) {
    	if(StringUtils.equals(MainetConstants.Property.PROP_DEPT_SHORT_CODE, requestDTO.getDeptCode())
    			&& StringUtils.equals(MainetConstants.WEB_PAYMENT, requestDTO.getPaymentType())) {
    		final List<LookUp> taxCategory = CommonMasterUtility.getLevelData(PrefixConstants.LookUpPrefix.TAC,
                    MainetConstants.NUMBERS.ONE, org);
            List<LookUp> lookUp =  taxCategory.stream().filter(tax -> tax.getLookUpCode().equals(rebateType)).collect(Collectors.toList());
            List<TbSrcptFeesDetEntity> feesDetEntityList = new ArrayList<TbSrcptFeesDetEntity>();
    		for(TbSrcptFeesDetEntity feesDetEntity : receiptMasEntityRebate.getReceiptFeeDetail()) {
    			Long taxCategoryId = tbTaxMasService.getTaxCategoryIdByTaxId(feesDetEntity.getTaxId(), requestDTO.getOrgId());
    			if(taxCategoryId != lookUp.get(0).getLookUpId()) {
    				feesDetEntityList.add(feesDetEntity);
    			}
    		}
    		receiptMasEntityRebate.getReceiptFeeDetail().clear();
    		receiptMasEntityRebate.setReceiptFeeDetail(feesDetEntityList);
    	}
	}

	private List<BillReceiptPrintingDTO> getBillReceiptPrintingDtoList(List<BillReceiptPostingDTO> billTaxes) {
        List<BillReceiptPrintingDTO> printDtoList = new ArrayList<>();
        billTaxes.forEach(billPostDto -> {
            BillReceiptPrintingDTO printDto = new BillReceiptPrintingDTO();
            BeanUtils.copyProperties(billPostDto, printDto);
            printDtoList.add(printDto);
        });
        printDtoList.stream().sorted(
                Comparator.comparing(BillReceiptPrintingDTO::getDisplaySeq, Comparator.nullsLast(Comparator.naturalOrder())));
        return printDtoList;
    }

    private void setAndPostGovtTax(CommonChallanDTO requestDTO, List<BillReceiptPostingDTO> billTaxes,
            TbServiceReceiptMasEntity receiptMasEntity, List<VoucherPostDTO> accountPostingList, Organisation org,
            Map<Long, Long> taxHeadId, List<Long> govtTaxId) {
        Map<Long, Double> govtTax = billTaxes.stream().filter(bill -> govtTaxId.contains(bill.getTaxId()))
                .collect(Collectors.toMap(BillReceiptPostingDTO::getTaxId, BillReceiptPostingDTO::getTaxAmount,
                        (oldValue, newValue) -> (oldValue + newValue)));
        if (!govtTax.isEmpty()) {
            final LookUp govt = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.GOVT_LIABILITY,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
            final VoucherPostDTO accountPostingGovTax = new VoucherPostDTO();
            accountPostingGovTax.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
            accountPostingGovTax.setVoucherSubTypeId(govt.getLookUpId());
            accountPostingGovTax.setVoucherDate(receiptMasEntity.getRmDate());
            accountPostingGovTax.setDepartmentId(requestDTO.getDeptId());
            accountPostingGovTax.setVoucherReferenceNo(receiptMasEntity.toString());
            accountPostingGovTax.setNarration(receiptMasEntity.getRmNarration());
            accountPostingGovTax.setPayerOrPayee(receiptMasEntity.getRmReceivedfrom());
            accountPostingGovTax.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
            accountPostingGovTax.setFieldId(receiptMasEntity.getFieldId());
            accountPostingGovTax.setOrgId(receiptMasEntity.getOrgId());
            accountPostingGovTax.setCreatedBy(receiptMasEntity.getCreatedBy());
            accountPostingGovTax.setCreatedDate(new Date());
            accountPostingGovTax.setLgIpMac(receiptMasEntity.getLgIpMac());

            VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO();
            voucherDetailRebate
                    .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp("T",
                            PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
            accountPostingGovTax.getVoucherDetails().add(voucherDetailRebate);
            AtomicDouble govtAmount = new AtomicDouble();
            govtTax.forEach((k, v) -> {
                VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                voucherDetails.setSacHeadId(taxHeadId.get(k));
                voucherDetails.setVoucherAmount(BigDecimal.valueOf(v));
                govtAmount.addAndGet(v);
                accountPostingGovTax.getVoucherDetails().add(voucherDetails);
            });
            voucherDetailRebate.setVoucherAmount(BigDecimal.valueOf(govtAmount.get()));
            accountPostingList.add(accountPostingGovTax);
        }
    }

    private void prepareTaxCategory(final Organisation org, List<Long> advance, List<Long> interest, List<Long> rebate,
            List<Long> penalty, List<Long> demand,List<Long> exemption, List<Long> webRebate) {
        final List<LookUp> taxCategory = CommonMasterUtility.getLevelData(PrefixConstants.LookUpPrefix.TAC,
                MainetConstants.NUMBERS.ONE, org);
        for (LookUp tax : taxCategory) {
            if (PrefixConstants.TAX_CATEGORY.ADVANCE.equals(tax.getLookUpCode())) {
                advance.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.PENALTY.equals(tax.getLookUpCode())) {
                penalty.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.INTERST.equals(tax.getLookUpCode())) {
                interest.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.REBATE.equals(tax.getLookUpCode())) {
                rebate.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.DEMAND.equals(tax.getLookUpCode())) {
                demand.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.EXEMPTION.equals(tax.getLookUpCode())) {
            	exemption.add(tax.getLookUpId());
            }
            if (PrefixConstants.TAX_CATEGORY.WEB_REBATE.equals(tax.getLookUpCode())) {
            	webRebate.add(tax.getLookUpId());
            }
        }
    }

    @Override
    @Transactional
    public void doAccountVoucherPosting(final String narration,
            final List<VoucherPostDTO> accountPostingList, final VoucherPostDTO accountPosting, String activeFlag) {
        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
            if (accountPosting != null)
                accountPostingList.add(accountPosting);
            VoucherPostListDTO dto = new VoucherPostListDTO();
            dto.setVoucherdto(accountPostingList);
            final ResponseEntity<?> responseValidate = RestClient.callRestTemplateClient(dto,
                    ServiceEndpoints.ACCOUNT_POSTING_VALIDATE);
            if ((responseValidate != null) && (responseValidate.getStatusCode() == HttpStatus.OK)) {
                LOGGER.info("Account Voucher Posting validated successfully");
                final ResponseEntity<?> response = RestClient.callRestTemplateClient(dto,
                        ServiceEndpoints.ACCOUNT_POSTING);
                if ((response != null) && (response.getStatusCode() == HttpStatus.OK)) {
                    LOGGER.info("Account Voucher Posting done successfully");
                } else {
                    LOGGER.error("Account Voucher Posting failed due to :"
                            + (response != null ? response.getBody() : MainetConstants.BLANK));

                    throw new FrameworkException("Check account integration for tax master: NO itegration found for " +
                            narration);
                }
            } else {
                LOGGER.error("Account Voucher Posting Validation Failed due to :"
                        + (responseValidate != null ? responseValidate.getBody() : MainetConstants.BLANK));

                throw new FrameworkException("Check input values for account posting data for:" +
                        narration);
            }

        }
    }

    private void setAndPostRebateDetails(final CommonChallanDTO requestDTO, List<BillReceiptPostingDTO> billTaxes,
            final TbServiceReceiptMasEntity receiptMasEntity, final List<VoucherPostDTO> accountPostingList,
            final Organisation org, Long receiptNumberRebate, List<Long> rebate, Map<Long, Long> taxHeadId) {
        Long finYearId = iFinancialYear.getFinanceYearId(receiptMasEntity.getRmDate());
        Map<Long, Double> rebateTax = billTaxes.stream().filter(bill -> rebate.contains(bill.getTaxCategory()))
                .collect(Collectors.toMap(BillReceiptPostingDTO::getTaxId, BillReceiptPostingDTO::getTaxAmount,
                        (oldValue, newValue) -> (oldValue + newValue)));
        if (!rebateTax.isEmpty()) {
            final LookUp rebateRRT = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.REBATE_COLLECTION,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
            // D#36743
            final Long currDemandId = CommonMasterUtility.getValueFromPrefixLookUp("DMD", "TDP", org).getLookUpId();

            final VoucherPostDTO accountPostingRebate = new VoucherPostDTO();
            accountPostingRebate.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
            accountPostingRebate.setVoucherSubTypeId(rebateRRT.getLookUpId());
            accountPostingRebate.setVoucherDate(receiptMasEntity.getRmDate());
            accountPostingRebate.setDepartmentId(requestDTO.getDeptId());
            accountPostingRebate.setVoucherReferenceNo(receiptNumberRebate.toString());
            accountPostingRebate.setVoucherReferenceDate(new Date());
            accountPostingRebate.setNarration(receiptMasEntity.getRmNarration());
            accountPostingRebate.setPayerOrPayee(receiptMasEntity.getRmReceivedfrom());
            accountPostingRebate.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
            accountPostingRebate.setFieldId(receiptMasEntity.getFieldId());
            accountPostingRebate.setOrgId(receiptMasEntity.getOrgId());
            accountPostingRebate.setCreatedBy(receiptMasEntity.getCreatedBy());
            accountPostingRebate.setCreatedDate(new Date());
            accountPostingRebate.setLgIpMac(receiptMasEntity.getLgIpMac());
            // accountPostingRebate.setFinancialYearId(finYearId); creating problem
            accountPostingRebate
                    .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                            PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
            /*
             * VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO(); voucherDetailRebate
             * .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
             * PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
             * accountPostingRebate.getVoucherDetails().add(voucherDetailRebate);
             */
            AtomicDouble rebateAmount = new AtomicDouble();
            rebateTax.forEach((k, v) -> {
                VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                voucherDetails.setSacHeadId(taxHeadId.get(k));
                voucherDetails.setYearId(finYearId);
                voucherDetails.setDemandTypeId(currDemandId);
                voucherDetails.setVoucherAmount(BigDecimal.valueOf(v));
                rebateAmount.addAndGet(v);
                accountPostingRebate.getVoucherDetails().add(voucherDetails);
            });
            // voucherDetailRebate.setVoucherAmount(BigDecimal.valueOf(rebateAmount.get()));
            accountPostingList.add(accountPostingRebate);

        }

    }

    private void setTaxesPostingDetails(List<BillReceiptPostingDTO> billTaxes, final VoucherPostDTO accountPosting,
            List<Long> taxCategory, Map<Long, Long> taxHeadId) {
        Map<Long, Double> taxCategoryPaid = billTaxes.stream().filter(bill -> taxCategory.contains(bill.getTaxCategory()))
                .collect(Collectors.toMap(BillReceiptPostingDTO::getTaxId, BillReceiptPostingDTO::getTaxAmount,
                        (oldValue, newValue) -> (oldValue + newValue)));
        if (!taxCategoryPaid.isEmpty()) {
            taxCategoryPaid.forEach((k, v) -> {
                VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                voucherDetails.setSacHeadId(taxHeadId.get(k));
                voucherDetails.setVoucherAmount(BigDecimal.valueOf(v));
                accountPosting.getVoucherDetails().add(voucherDetails);
            });
        }
    }

    private void setDemandPostingDetails(List<BillReceiptPostingDTO> billTaxes, final Organisation org,
            final VoucherPostDTO accountPosting, List<Long> demand, List<Long> excludetax) {
        Map<Long, Double> demandTaxesSorted = new LinkedHashMap<Long, Double>();

        Map<Long, Double> demandTaxes = billTaxes.stream().filter(bill -> !excludetax.contains(bill.getTaxCategory()))
                .collect(Collectors.toMap(BillReceiptPostingDTO::getYearId, BillReceiptPostingDTO::getTaxAmount,
                        (oldValue, newValue) -> (oldValue + newValue)));

        final List<FinancialYear> finYearAll = iFinancialYear.getAllFinincialYear();
        // Collections.reverse(finYearAll);
        finYearAll.forEach(finyear -> {
            demandTaxes.forEach((yearId, value) -> {
                if (finyear.getFaYear() == yearId.longValue()) {
                    demandTaxesSorted.put(yearId, value);
                }
            });
        });
        if (!demandTaxesSorted.isEmpty()) {
            final LookUp dmd = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.DMD_VALUE,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);

            final LookUp dmp = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.DMP,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
            AtomicInteger count = new AtomicInteger(0);
            AtomicDouble priorPeriodAmount = new AtomicDouble(0);
            demandTaxesSorted.forEach((k, v) -> {
                if (count.intValue() < MainetConstants.NUMBERS.FIVE) {
                    VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                    voucherDetails.setVoucherAmount(BigDecimal.valueOf(v));
                    if (v != null) {
                        voucherDetails.setYearId(k);
                        voucherDetails.setDemandTypeId(dmd.getLookUpId());
                    } else {
                        voucherDetails.setDemandTypeId(dmp.getLookUpId());
                    }
                    accountPosting.getVoucherDetails().add(voucherDetails);
                    count.incrementAndGet();
                } else {
                    priorPeriodAmount.addAndGet(v);
                }
            });
            if (priorPeriodAmount.doubleValue() > 0d) {
                VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                voucherDetails.setVoucherAmount(BigDecimal.valueOf(priorPeriodAmount.doubleValue()));
                voucherDetails.setDemandTypeId(dmp.getLookUpId());
                accountPosting.getVoucherDetails().add(voucherDetails);
            }
        }
    }

    @Override
    public TbServiceReceiptMasEntity saveRebateReceipt(final CommonChallanDTO requestDTO,
            final TbServiceReceiptMasEntity receiptMasEntity,
            Organisation org,
            Map<Long, Long> taxHeadId, BillReceiptPostingDTO billDef, Long currDemandId,double totalEarlyDiscAmnt) {
        Long receiptNumberRebate;
        final TbServiceReceiptMasEntity receiptMasEntityRebate = new TbServiceReceiptMasEntity();
        final List<TbSrcptFeesDetEntity> receiptFeeDetailrebate = new ArrayList<>();
        List<TbSrcptModesDetEntity> receiptModeList = new ArrayList<TbSrcptModesDetEntity>(); 
		/*
		 * receiptNumberRebate = seqGenFunctionUtility.generateSequenceNo(
		 * MainetConstants.RECEIPT_MASTER.Module, MainetConstants.RECEIPT_MASTER.Table,
		 * MainetConstants.RECEIPT_MASTER.Column, requestDTO.getOrgId(),
		 * MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
		 */
        
        receiptNumberRebate = seqGenFunctionUtility.generateJavaSequenceNo(MainetConstants.RECEIPT_MASTER.Module,
				MainetConstants.RECEIPT_MASTER.Table, MainetConstants.RECEIPT_MASTER.Column,
				MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
        receiptMasEntityRebate.setRmRcptno(receiptNumberRebate);
        receiptMasEntityRebate.setApmApplicationId(requestDTO.getApplNo());
        receiptMasEntityRebate.setRmDate(receiptMasEntity.getRmDate());
        receiptMasEntityRebate.setRmReceivedfrom(requestDTO.getApplicantName());
        receiptMasEntityRebate.setRmAmount(new BigDecimal(0));
        receiptMasEntityRebate.setSmServiceId(requestDTO.getServiceId());
        if (requestDTO.getUniquePrimaryId() != null) {
            receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
            String departDesc = null;
            if(requestDTO.getLangId() > 0) {
            	departDesc = departmentService.fetchDepartmentDescEngById(requestDTO.getDeptId(),requestDTO.getLangId());
            }else {
            	departDesc = departmentService.fetchDepartmentDescById(requestDTO.getDeptId());
            }
            receiptMasEntityRebate.setRmNarration(ApplicationSession.getInstance().getMessage("receipt.message.rmNarration3") +
            		departDesc);
            /*
             * receiptMasEntity.setRmNarration(ApplicationSession.getInstance().getMessage("receipt.message.rmNarration3") +
             * departmentService.fetchDepartmentDescById(requestDTO.getDeptId()));
             */
        }
        if(StringUtils.isNotBlank(requestDTO.getParentPropNo())) {
        	receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getParentPropNo());
        }
        
        receiptMasEntityRebate.setOrgId(requestDTO.getOrgId());
        receiptMasEntityRebate.setCreatedBy(requestDTO.getUserId());
        receiptMasEntityRebate.setLgIpMac(requestDTO.getLgIpMac());
        receiptMasEntityRebate.setCreatedDate(new Date());
        final LookUp rebateReceipt = CommonMasterUtility.getValueFromPrefixLookUp("RB",
                "RV", org);
        receiptMasEntityRebate.setReceiptTypeFlag(rebateReceipt.getLookUpCode());
        receiptMasEntityRebate.setCoddwzId1(receiptMasEntity.getCoddwzId1());
        receiptMasEntityRebate.setCoddwzId2(receiptMasEntity.getCoddwzId2());
        receiptMasEntityRebate.setCoddwzId3(receiptMasEntity.getCoddwzId3());
        receiptMasEntityRebate.setCoddwzId4(receiptMasEntity.getCoddwzId4());
        receiptMasEntityRebate.setCoddwzId5(receiptMasEntity.getCoddwzId5());
        receiptMasEntityRebate.setDpDeptId(requestDTO.getDeptId());
        if(StringUtils.isNotBlank(requestDTO.getFlatNo())) {
        	receiptMasEntityRebate.setFlatNo(requestDTO.getFlatNo());
        }
		/*
		 * if(CollectionUtils.isNotEmpty(requestDTO.getMultiModeList())) {
		 * receiptModeList = prepareReceiptMode(receiptMasEntity, requestDTO, null);
		 * }else {
		 */
        	 final TbSrcptModesDetEntity receiptModeRebate = new TbSrcptModesDetEntity();
             receiptModeRebate.setRmRcptid(receiptMasEntityRebate);
             receiptModeRebate
                     .setCpdFeemode(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                             PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
             receiptModeRebate.setRdAmount(BigDecimal.valueOf(totalEarlyDiscAmnt));
             receiptModeRebate.setOrgId(requestDTO.getOrgId());
             receiptModeRebate.setCreatedBy(requestDTO.getUserId());
             receiptModeRebate.setLgIpMac(requestDTO.getLgIpMac());
             receiptModeRebate.setCreatedDate(new Date());
             receiptModeList.add(receiptModeRebate);
       /* }*/
             final List<LookUp> taxCategory = CommonMasterUtility.getLevelData(PrefixConstants.LookUpPrefix.TAC,
                     MainetConstants.NUMBERS.ONE, org);
             List<LookUp> lookUp =  taxCategory.stream().filter(tax -> tax.getLookUpCode().equals("WR")).collect(Collectors.toList());
             if(StringUtils.equals(MainetConstants.Property.PROP_DEPT_SHORT_CODE, requestDTO.getDeptCode())) {
            	 billDef.getRebateDetails().forEach(rebatemap -> {
                     rebatemap.forEach((k, v) -> {
                    	 if(lookUp.get(0).getLookUpId()!=billDef.getTaxCategory()) {
                         TbSrcptFeesDetEntity detailRebate = new TbSrcptFeesDetEntity();
                         detailRebate.setRmRcptid(receiptMasEntityRebate);
                         	if (v.size() > 1) {
                                 detailRebate.setTaxId(k);
                             }
                             detailRebate.setRfFeeamount(BigDecimal.valueOf(Utility.round(v.get(0), 2)));
         					receiptMasEntityRebate.setRmAmount(BigDecimal
         							.valueOf(Math.round(receiptMasEntityRebate.getRmAmount().doubleValue() + v.get(0))));
         					detailRebate.setBilldetId(v.get(1).longValue());
                         detailRebate.setOrgId(requestDTO.getOrgId());
                         detailRebate.setCreatedBy(requestDTO.getUserId());
                         detailRebate.setLgIpMac(requestDTO.getLgIpMac());
                         detailRebate.setCreatedDate(new Date());
                         detailRebate.setBmIdNo(billDef.getBillMasId());
                         final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
                                 billDef.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
                         detailRebate.setSacHeadId(sacHeadId);
                         receiptFeeDetailrebate.add(detailRebate);
                         taxHeadId.put(billDef.getTaxId(), sacHeadId);
                    	 }
                     });
                 });
             } else {
		        billDef.getRebateDetails().forEach(rebatemap -> {
		            rebatemap.forEach((k, v) -> {
		                TbSrcptFeesDetEntity detailRebate = new TbSrcptFeesDetEntity();
		                detailRebate.setRmRcptid(receiptMasEntityRebate);
		                if (v.size() > 1) {
		                    detailRebate.setTaxId(v.get(1).longValue());
		                }
		                detailRebate.setRfFeeamount(BigDecimal.valueOf(Utility.round(v.get(0), 2)));
		                receiptMasEntityRebate
		                        .setRmAmount(
		                                BigDecimal.valueOf(Math.round(receiptMasEntityRebate.getRmAmount().doubleValue() + v.get(0))));
		                detailRebate.setBilldetId(k);
		                detailRebate.setOrgId(requestDTO.getOrgId());
		                detailRebate.setCreatedBy(requestDTO.getUserId());
		                detailRebate.setLgIpMac(requestDTO.getLgIpMac());
		                detailRebate.setCreatedDate(new Date());
		                detailRebate.setBmIdNo(billDef.getBillMasId());
		                final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
		                        billDef.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
		                detailRebate.setSacHeadId(sacHeadId);
		                receiptFeeDetailrebate.add(detailRebate);
		                taxHeadId.put(billDef.getTaxId(), sacHeadId);
		            });
		        });
             }
        receiptMasEntityRebate.setReceiptModeDetail(receiptModeList);
        receiptMasEntityRebate.setReceiptFeeDetail(receiptFeeDetailrebate);
        receiptMasEntityRebate.setFieldId(receiptMasEntity.getFieldId());
        return receiptMasEntityRebate;
    }

    /*
     * Changes in saveDemandLevelRebateAndAccountPosting for property: Json input request changes Task #27617: Property Self
     * Occupied Rebate posting issues when account is in live mode
     */
    @Override
    public Long saveDemandLevelRebateAndAccountPosting(final CommonChallanDTO requestDTO,
            Organisation org, List<BillReceiptPostingDTO> billRebateDto) {
        Long receiptNumberRebate;
        Map<Long, Long> taxHeadId = new HashMap<>();
        final Long currDemandId = CommonMasterUtility.getValueFromPrefixLookUp("DMD", "TDP", org).getLookUpId();
        final Long conDemandId = CommonMasterUtility.getValueFromPrefixLookUp("DMP", "TDP", org).getLookUpId(); // more than 5
                                                                                                                // year year
        final TbServiceReceiptMasEntity receiptMasEntityRebate = new TbServiceReceiptMasEntity();
        final List<TbSrcptFeesDetEntity> receiptFeeDetailrebate = new ArrayList<>();
        List<TbSrcptModesDetEntity> receiptModeList = new ArrayList<TbSrcptModesDetEntity>();
		/*
		 * receiptNumberRebate = seqGenFunctionUtility.generateSequenceNo(
		 * MainetConstants.RECEIPT_MASTER.Module, MainetConstants.RECEIPT_MASTER.Table,
		 * MainetConstants.RECEIPT_MASTER.Column, requestDTO.getOrgId(),
		 * MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
		 */
        
        receiptNumberRebate = seqGenFunctionUtility.generateJavaSequenceNo(MainetConstants.RECEIPT_MASTER.Module,
				MainetConstants.RECEIPT_MASTER.Table, MainetConstants.RECEIPT_MASTER.Column,
				MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
        receiptMasEntityRebate.setRmRcptno(receiptNumberRebate);
        receiptMasEntityRebate.setApmApplicationId(requestDTO.getApplNo());
        if(StringUtils.isNotBlank(requestDTO.getFlatNo())) {
        	receiptMasEntityRebate.setFlatNo(requestDTO.getFlatNo());        	
        }
        receiptMasEntityRebate.setRmDate(new Date());
        AtomicDouble totAmt = new AtomicDouble(0);
        billRebateDto.forEach(rebate -> {
            totAmt.addAndGet(rebate.getTaxAmount());
        });
        receiptMasEntityRebate.setRmAmount(BigDecimal.valueOf(totAmt.doubleValue()));
        receiptMasEntityRebate.setRmReceivedfrom(requestDTO.getApplicantName());
        receiptMasEntityRebate.setSmServiceId(requestDTO.getServiceId());
        if (requestDTO.getUniquePrimaryId() != null) {
            receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
        }
        receiptMasEntityRebate.setOrgId(requestDTO.getOrgId());
        receiptMasEntityRebate.setCreatedBy(requestDTO.getUserId());
        receiptMasEntityRebate.setLgIpMac(requestDTO.getLgIpMac());
        receiptMasEntityRebate.setCreatedDate(new Date());
        receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
        final LookUp rebateReceipt = CommonMasterUtility.getValueFromPrefixLookUp("RB",
                "RV", org);
        receiptMasEntityRebate.setReceiptTypeFlag(rebateReceipt.getLookUpCode());
        final WardZoneBlockDTO dwzDTO = commonService.getWordZoneBlockByApplicationId(
                requestDTO.getApplNo(), requestDTO.getServiceId(),
                requestDTO.getOrgId());
        receiptMasEntityRebate.setCoddwzId1(dwzDTO.getAreaDivision1());
        receiptMasEntityRebate.setCoddwzId2(dwzDTO.getAreaDivision2());
        receiptMasEntityRebate.setCoddwzId3(dwzDTO.getAreaDivision3());
        receiptMasEntityRebate.setCoddwzId4(dwzDTO.getAreaDivision4());

        receiptMasEntityRebate.setCoddwzId5(dwzDTO.getAreaDivision5());
        receiptMasEntityRebate.setDpDeptId(requestDTO.getDeptId());

		/*
		 * if(CollectionUtils.isNotEmpty(requestDTO.getMultiModeList())) {
		 * receiptModeList = prepareReceiptMode(receiptMasEntityRebate, requestDTO,
		 * billRebateDto); }else {
		 */
        	   final TbSrcptModesDetEntity receiptModeRebate = new TbSrcptModesDetEntity();
               receiptModeRebate.setRmRcptid(receiptMasEntityRebate);
               receiptModeRebate
                       .setCpdFeemode(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                               PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
               receiptModeRebate.setRdAmount(BigDecimal.valueOf(totAmt.doubleValue()));
               receiptModeRebate.setOrgId(requestDTO.getOrgId());
               receiptModeRebate.setCreatedBy(requestDTO.getUserId());
               receiptModeRebate.setLgIpMac(requestDTO.getLgIpMac());
               receiptModeRebate.setCreatedDate(new Date());
               receiptModeList.add(receiptModeRebate);
       /* }*/
        billRebateDto.forEach(billDef -> {
            if (billDef.getTaxAmount() > 0) {
                TbSrcptFeesDetEntity detailRebate = new TbSrcptFeesDetEntity();
                detailRebate.setRmRcptid(receiptMasEntityRebate);
                detailRebate.setTaxId(billDef.getTaxId());
                detailRebate.setRfFeeamount(BigDecimal.valueOf(Utility.round(billDef.getTaxAmount(), 2)));
                detailRebate.setOrgId(requestDTO.getOrgId());
                detailRebate.setCreatedBy(requestDTO.getUserId());
                detailRebate.setLgIpMac(requestDTO.getLgIpMac());
                detailRebate.setCreatedDate(new Date());
                detailRebate.setBilldetId(billDef.getBillDetId());
                detailRebate.setBmIdNo(billDef.getBillMasId());
                /* if (receiptMasEntityRebate.getRmNarration() == null) { */
                receiptMasEntityRebate.setRmNarration(billDef.getRmNarration());
                /*
                 * } else { receiptMasEntityRebate.setRmNarration(receiptMasEntityRebate.getRmNarration() + "," +
                 * billDef.getRmNarration()); }
                 */
                receiptMasEntityRebate.setRmNarration(receiptMasEntityRebate.getRmNarration() + "," + billDef.getRmNarration());
                if("Go Green Exemption".equalsIgnoreCase(billDef.getRmNarration())) {
                	receiptMasEntityRebate.setRmNarration(billDef.getRmNarration());
                }
                final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
                        billDef.getTaxId(),
                        MainetConstants.STATUS.ACTIVE, currDemandId);
                detailRebate.setSacHeadId(sacHeadId);
                receiptFeeDetailrebate.add(detailRebate);
                taxHeadId.put(billDef.getTaxId(), sacHeadId);
            }

        });
        Long locationId = requestDTO.getLoggedLocId();
        if (locationId != null) {
            final TbLocationMas locMas = iLocationMasService.findById(locationId);
            if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
                locationId = locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1();
            }
        } else {
            final TbLocationMas locMas = iLocationMasService
                    .findByLocationName(ApplicationSession.getInstance().getMessage("location.LocNameEng"), org.getOrgid());
            if ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) {
                locationId = locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1();
            }
        }
        receiptMasEntityRebate.setReceiptModeDetail(receiptModeList);
        receiptMasEntityRebate.setReceiptFeeDetail(receiptFeeDetailrebate);
        receiptMasEntityRebate.setRefId(null);
        receiptMasEntityRebate.setFieldId(locationId);
        receiptRepository.save(receiptMasEntityRebate);

        String activeFlag = null;
        final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
                MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
        if (accountActive != null) {
            activeFlag = accountActive.getDefaultVal();
        }
        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
            final List<VoucherPostDTO> accountPostingList = new ArrayList<>();
            final LookUp rebateRRT = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.REBATE_COLLECTION,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);

            Map<Long, Double> rebateTaxes = billRebateDto.stream().collect(Collectors.toMap(BillReceiptPostingDTO::getYearId,
                    BillReceiptPostingDTO::getTaxAmount, (oldValue, newValue) -> (oldValue + newValue)));
            final List<FinancialYear> finYearAll = iFinancialYear.getAllFinincialYear();
            Map<Long, Double> rebateTaxSortedMap = new LinkedHashMap<>(0);
            finYearAll.forEach(finyear -> {
                rebateTaxes.forEach((yearId, value) -> {
                    if (finyear.getFaYear() == yearId.longValue()) {
                        rebateTaxSortedMap.put(yearId, value);
                    }
                });
            });
            final Long revenueLocId = locationId;
            VoucherPostDTO consolidatePostingRebate = new VoucherPostDTO();
            consolidatePostingRebate.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
            AtomicDouble consolidateRebate = new AtomicDouble(0);
            AtomicInteger count = new AtomicInteger(0);
            rebateTaxSortedMap.forEach((k, v) -> {
                if (count.intValue() < MainetConstants.NUMBERS.FIVE) {
                    VoucherPostDTO accountPostingRebate = new VoucherPostDTO();
                    VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO();
                    accountPostingRebate.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
                    accountPostingRebate.setVoucherSubTypeId(rebateRRT.getLookUpId());
                    accountPostingRebate.setVoucherDate(new Date());
                    accountPostingRebate.setDepartmentId(requestDTO.getDeptId());
                    accountPostingRebate.setVoucherReferenceNo(receiptNumberRebate.toString());
                    accountPostingRebate.setVoucherReferenceDate(new Date());
                    accountPostingRebate.setNarration(receiptMasEntityRebate.getRmNarration());
                    accountPostingRebate.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
                    accountPostingRebate.setFieldId(revenueLocId);
                    accountPostingRebate.setOrgId(requestDTO.getOrgId());
                    accountPostingRebate.setCreatedBy(requestDTO.getUserId());
                    accountPostingRebate.setCreatedDate(new Date());
                    accountPostingRebate.setLgIpMac(requestDTO.getLgIpMac());
                    accountPostingRebate
                            .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                                    PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
                    voucherDetailRebate.setVoucherAmount(BigDecimal.valueOf(v));
                    billRebateDto.forEach(rebate -> {
                        if (rebate.getYearId().equals(k)) {
                            VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();

                            voucherDetails.setVoucherAmount(BigDecimal.valueOf(rebate.getTaxAmount()));
                            voucherDetails.setYearId(k);
                            voucherDetails.setDemandTypeId(currDemandId);
                            accountPostingRebate.getVoucherDetails().add(voucherDetails);
                        }
                    });
                    count.incrementAndGet();
                    accountPostingList.add(accountPostingRebate);
                } else {
                    consolidateRebate.addAndGet(v);
                    billRebateDto.forEach(rebate -> {
                        if (rebate.getYearId().equals(k)) {
                            VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO();
                            voucherDetails.setSacHeadId(taxHeadId.get(rebate.getTaxId()));
                            voucherDetails.setVoucherAmount(BigDecimal.valueOf(rebate.getTaxAmount()));
                            voucherDetails.setDemandTypeId(conDemandId);
                            consolidatePostingRebate.getVoucherDetails().add(voucherDetails);
                        }
                    });
                }
            });
            if (consolidateRebate.get() > 0d) {
                // VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO();
                consolidatePostingRebate.setVoucherSubTypeId(rebateRRT.getLookUpId());
                consolidatePostingRebate.setVoucherDate(new Date());
                consolidatePostingRebate.setDepartmentId(requestDTO.getDeptId());
                consolidatePostingRebate.setVoucherReferenceNo(receiptNumberRebate.toString());
                consolidatePostingRebate.setVoucherReferenceDate(new Date());
                consolidatePostingRebate.setNarration(receiptMasEntityRebate.getRmNarration());
                consolidatePostingRebate.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
                consolidatePostingRebate.setFieldId(revenueLocId);
                consolidatePostingRebate.setOrgId(requestDTO.getOrgId());
                consolidatePostingRebate.setCreatedBy(requestDTO.getUserId());
                consolidatePostingRebate.setCreatedDate(new Date());
                consolidatePostingRebate.setLgIpMac(requestDTO.getLgIpMac());
                consolidatePostingRebate.setFinancialYearId(null);
                consolidatePostingRebate
                        .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                                PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
                accountPostingList.add(consolidatePostingRebate);
            }
            
            try {
            	doAccountVoucherPosting(receiptMasEntityRebate.getRmNarration(), accountPostingList, null, activeFlag);
			} catch (Exception exception) {
				LOGGER.error("Exception occured while demand level account posting"+exception);
			}
            
        }
        /*
         * Map<Long, Double> rebateTaxes = billRebateDto.stream() .collect(Collectors.toMap(BillReceiptPostingDTO::getTaxId,
         * BillReceiptPostingDTO::getTaxAmount, (oldValue, newValue) -> (oldValue + newValue))); final LookUp rebateRRT =
         * CommonMasterUtility.getValueFromPrefixLookUp( MainetConstants.BILL_MASTER_COMMON.REBATE_COLLECTION,
         * MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org); final LookUp dmd =
         * CommonMasterUtility.getValueFromPrefixLookUp( MainetConstants.BILL_MASTER_COMMON.DMD_VALUE,
         * MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org); final LookUp dmp =
         * CommonMasterUtility.getValueFromPrefixLookUp( MainetConstants.BILL_MASTER_COMMON.DMP,
         * MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org); rebateTaxes.forEach((k, v) -> { VoucherPostDTO
         * accountPostingRebate = new VoucherPostDTO(); VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO();
         * accountPostingRebate.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
         * accountPostingRebate.setVoucherSubTypeId(rebateRRT.getLookUpId()); accountPostingRebate.setVoucherDate(new Date());
         * accountPostingRebate.setDepartmentId(requestDTO.getDeptId());
         * accountPostingRebate.setVoucherReferenceNo(receiptNumberRebate.toString());
         * accountPostingRebate.setNarration(receiptMasEntityRebate.getRmNarration());
         * accountPostingRebate.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
         * accountPostingRebate.setFieldId(revenueLocId); accountPostingRebate.setOrgId(requestDTO.getOrgId());
         * accountPostingRebate.setCreatedBy(requestDTO.getUserId()); accountPostingRebate.setCreatedDate(new Date());
         * accountPostingRebate.setLgIpMac(requestDTO.getLgIpMac()); accountPostingRebate.setFinancialYearId(financialYearId);
         * voucherDetailRebate .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
         * PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
         * voucherDetailRebate.setVoucherAmount(BigDecimal.valueOf(totAmt.doubleValue()));
         * accountPostingRebate.getVoucherDetails().add(voucherDetailRebate); AtomicDouble consolidateRebate = new
         * AtomicDouble(0); AtomicInteger count = new AtomicInteger(0); billRebateDto.forEach(rebate -> { if
         * (rebate.getTaxId().equals(k)) { if (count.intValue() < MainetConstants.NUMBERS.FIVE) { VoucherPostDetailDTO
         * voucherDetails = new VoucherPostDetailDTO(); voucherDetails.setSacHeadId(taxHeadId.get(rebate.getTaxId()));
         * voucherDetails.setVoucherAmount(BigDecimal.valueOf(rebate.getTaxAmount()));
         * voucherDetails.setYearId(rebate.getYearId()); voucherDetails.setDemandTypeId(dmd.getLookUpId());
         * accountPostingRebate.getVoucherDetails().add(voucherDetails); count.incrementAndGet(); } else {
         * consolidateRebate.addAndGet(rebate.getTaxAmount()); } } }); if (consolidateRebate.doubleValue() > 0) {
         * VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO(); voucherDetails.setSacHeadId(taxHeadId.get(k));
         * voucherDetails.setVoucherAmount(BigDecimal.valueOf(consolidateRebate.doubleValue()));
         * voucherDetails.setDemandTypeId(dmp.getLookUpId()); voucherDetails.setYearId(null);
         * accountPostingRebate.getVoucherDetails().add(voucherDetails); } accountPostingList.add(accountPostingRebate); });
         */
        return receiptNumberRebate;

    }

    private void setReceiptDetailForBill(final CommonChallanDTO requestDTO, final TbServiceReceiptMasEntity receiptMasEntity,
            Map<Long, Long> taxHeadId, BillReceiptPostingDTO billDef, List<TbSrcptFeesDetEntity> receiptFeeDetail,
            Long currDemandId, List<Long> govtTaxId, Long liability, Organisation org, String activeFlag) {
        TbSrcptFeesDetEntity feeEntity = new TbSrcptFeesDetEntity();
        feeEntity.setRmRcptid(receiptMasEntity);
        feeEntity.setTaxId(billDef.getTaxId());
        feeEntity.setRfFeeamount(BigDecimal.valueOf(Utility.round(billDef.getTaxAmount(), 2)));
        feeEntity.setRmDemand(BigDecimal.valueOf(billDef.getPayableAmount()));
        feeEntity.setOrgId(requestDTO.getOrgId());
        feeEntity.setCreatedBy(requestDTO.getUserId());
        feeEntity.setLgIpMac(requestDTO.getLgIpMac());
        feeEntity.setCreatedDate(new Date());
        feeEntity.setBilldetId(billDef.getBillDetId());
        feeEntity.setBmIdNo(billDef.getBillMasId());
        if (billDef.getTotalDetAmount() != null) {
            feeEntity.setBalAmount(BigDecimal.valueOf(billDef.getTotalDetAmount()));
        }
        if (billDef.getArrearAmount() != null) {
            feeEntity.setArrearAmount(BigDecimal.valueOf(billDef.getArrearAmount()));
        }
        if (billDef.getTaxCategory() != null && billDef.getTaxCategory() > 0) {
            feeEntity.setTaxCatCode(CommonMasterUtility.getHierarchicalLookUp(billDef.getTaxCategory(), org).getLookUpCode());

        }
        Long sacHeadId = null;
        if (govtTaxId != null && govtTaxId.contains(billDef.getTaxId())) {
            sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(feeEntity.getOrgId(),
                    feeEntity.getTaxId(), MainetConstants.STATUS.ACTIVE, liability);
        } else {
            sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(feeEntity.getOrgId(),
                    feeEntity.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
        }
        // D#81058
        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
        	// Due to throw exception receipt is dependent on account posting to make independent added logger.
            if (sacHeadId == null) {
            	LOGGER.error("Error Occured sacHead Id getting null for taxId " + feeEntity.getTaxId());
            }
            feeEntity.setSacHeadId(sacHeadId);
        }
        receiptFeeDetail.add(feeEntity);
        taxHeadId.put(feeEntity.getTaxId(), sacHeadId);
    }

    private void setFeeDetailAndVoucherDetail(final CommonChallanDTO requestDTO, final TbServiceReceiptMasEntity receiptMasEntity,
            final VoucherPostDTO accountPosting, String activeFlag, List<TbSrcptFeesDetEntity> receiptFeeDetail,
            Long currDemandId) {
        VoucherPostDetailDTO voucherDetail;
        TbSrcptFeesDetEntity feeEntity;
        if ((requestDTO.getFeeIds() != null)
                && (requestDTO.getFeeIds().keySet() != null) && !requestDTO.getFeeIds().keySet().isEmpty()) {
            /*
             * final Iterator<Long> iterator = requestDTO.getFeeIds().keySet() .iterator(); while (iterator.hasNext()) { final
             * Long feeId = iterator.next(); feeEntity = new TbSrcptFeesDetEntity(); feeEntity.setRmRcptid(receiptMasEntity);
             * feeEntity.setTaxId(feeId); feeEntity.setRfFeeamount(new BigDecimal(requestDTO.getFeeIds().get(feeId).toString()));
             * feeEntity.setOrgId(requestDTO.getOrgId()); feeEntity.setCreatedBy(requestDTO.getUserId());
             * feeEntity.setLgIpMac(requestDTO.getLgIpMac()); feeEntity.setCreatedDate(new Date());
             * feeEntity.setBalAmount(feeEntity.getRfFeeamount()); if ((requestDTO.getBillDetIds() != null) &&
             * !requestDTO.getBillDetIds().isEmpty()) { feeEntity.setBilldetId(requestDTO.getBillDetIds().get(feeId)); } if
             * (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) { voucherDetail = new VoucherPostDetailDTO(); final Long
             * sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(feeEntity.getOrgId(), feeEntity.getTaxId(),
             * MainetConstants.STATUS.ACTIVE, currDemandId); feeEntity.setSacHeadId(sacHeadId);
             * voucherDetail.setSacHeadId(sacHeadId); voucherDetail.setVoucherAmount(feeEntity.getRfFeeamount());
             * accountPosting.getVoucherDetails().add(voucherDetail); } receiptFeeDetail.add(feeEntity); }
             */

            for (Long value : requestDTO.getFeeIds().keySet()) {
                Long feeId = value;
                feeEntity = new TbSrcptFeesDetEntity();
                feeEntity.setRmRcptid(receiptMasEntity);
                feeEntity.setTaxId(feeId);
                feeEntity
                        .setRfFeeamount(new BigDecimal(Utility.round(Double.valueOf(requestDTO.getFeeIds().get(feeId).toString()), 2)));
                if (requestDTO.getSupplimentryBillIdMap() != null && !requestDTO.getSupplimentryBillIdMap().isEmpty()) {
                    feeEntity.setRfLo2(requestDTO.getSupplimentryBillIdMap().get(feeId).toString());
                    feeEntity.setBmIdNo(requestDTO.getSupplimentryBillIdMap().get(feeId));
                }
                feeEntity.setOrgId(requestDTO.getOrgId());
                feeEntity.setCreatedBy(requestDTO.getUserId());
                feeEntity.setLgIpMac(requestDTO.getLgIpMac());
                feeEntity.setCreatedDate(new Date());
                feeEntity.setBalAmount(feeEntity.getRfFeeamount());
                if ((requestDTO.getBillDetIds() != null) && !requestDTO.getBillDetIds().isEmpty()) {
                    feeEntity.setBilldetId(requestDTO.getBillDetIds().get(feeId));
                }

                if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
                    voucherDetail = new VoucherPostDetailDTO();
                    /*
                     * avoid 0 voucherAmount changes by suhel and code reveived By Rajesh Sir Defect #36702
                     */
                    BigDecimal voucherAmount = feeEntity.getRfFeeamount();
                    double checked = 0d;
                    if (voucherAmount != null) {
                        checked = voucherAmount.doubleValue();
                    }
                    if (checked > 0.0d) {
                        voucherDetail.setVoucherAmount(feeEntity.getRfFeeamount());

                        final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(feeEntity.getOrgId(),
                                feeEntity.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
                        // D#81058
                        if (sacHeadId != null) {
                            feeEntity.setSacHeadId(sacHeadId);
                            voucherDetail.setSacHeadId(sacHeadId);
                        } else {
//                            throw new FrameworkException(
//                                    "Error Occured sacHead Id getting null for taxId " + feeEntity.getTaxId());
                        }

                        accountPosting.getVoucherDetails().add(voucherDetail);
                    }
                }
                receiptFeeDetail.add(feeEntity);
            }

        }
    }

    @Override
    public void inActiveAllRebetReceiptByAppNo(Long orgId, Long applicationNo, Long deptId, String deleteRemark, Long empId) {
        List<TbServiceReceiptMasEntity> recList = receiptRepository.getAllActiveRebetReceiptByAppNo(orgId, applicationNo, deptId);
        recList.forEach(receipt -> {
            receipt.setReceiptDelFlag("Y");
            receipt.setReceiptDelDate(new Date());
            receipt.setReceiptDelRemark(deleteRemark);
            receiptRepository.save(receipt);
        });
        String activeFlag = null;
        Organisation org = new Organisation();
        org.setOrgid(orgId);
        final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
                MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
        if (accountActive != null) {
            activeFlag = accountActive.getDefaultVal();
        }
        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
            /*
             * final LookUp rebateRRT = CommonMasterUtility.getValueFromPrefixLookUp(
             * MainetConstants.BILL_MASTER_COMMON.REBATE_REVERSAL, MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX,
             * org); Long locationId = null; final TbLocationMas locMas = iLocationMasService
             * .findByLocationName(ApplicationSession.getInstance().getMessage("location.LocNameEng"), org.getOrgid()); if
             * ((locMas.getLocRevenueWZMappingDto() != null) && !locMas.getLocRevenueWZMappingDto().isEmpty()) { locationId =
             * locMas.getLocRevenueWZMappingDto().get(0).getCodIdRevLevel1(); } final Long revenueLocId = locationId; final
             * List<VoucherPostDTO> accountPostingList = new ArrayList<>(); final Long currDemandId =
             * CommonMasterUtility.getValueFromPrefixLookUp("CYR", "DMC", org).getLookUpId(); List<TbSrcptFeesDetEntity>
             * receiptFeeDetail = new ArrayList<>(0); recList.forEach(receiptMas -> {
             * receiptFeeDetail.addAll(receiptMas.getReceiptFeeDetail()); }); final TbServiceReceiptMasEntity receiptMas =
             * recList.get(0); Map<Long, BigDecimal> rebateTaxes = receiptFeeDetail.stream()
             * .collect(Collectors.toMap(TbSrcptFeesDetEntity::getTaxId, TbSrcptFeesDetEntity::getRfFeeamount, (oldValue,
             * newValue) -> oldValue.add(newValue))); rebateTaxes.forEach((k, v) -> { VoucherPostDTO accountPostingRebate = new
             * VoucherPostDTO(); VoucherPostDetailDTO voucherDetailRebate = new VoucherPostDetailDTO();
             * accountPostingRebate.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
             * accountPostingRebate.setVoucherSubTypeId(rebateRRT.getLookUpId()); accountPostingRebate.setVoucherDate(new Date());
             * accountPostingRebate.setDepartmentId(receiptMas.getDpDeptId());
             * accountPostingRebate.setVoucherReferenceNo(receiptMas.getRmRcptno().toString());
             * accountPostingRebate.setNarration(receiptMas.getRmNarration());
             * accountPostingRebate.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
             * accountPostingRebate.setFieldId(revenueLocId); accountPostingRebate.setOrgId(receiptMas.getOrgId());
             * accountPostingRebate.setCreatedBy(empId); accountPostingRebate.setCreatedDate(new Date());
             * accountPostingRebate.setLgIpMac(receiptMas.getLgIpMac()); voucherDetailRebate
             * .setPayModeId(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
             * PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
             * voucherDetailRebate.setVoucherAmount(receiptMas.getRmAmount());
             * accountPostingRebate.getVoucherDetails().add(voucherDetailRebate); AtomicDouble consolidateRebate = new
             * AtomicDouble(0); AtomicInteger count = new AtomicInteger(0); receiptFeeDetail.forEach(feeDet -> { if
             * (feeDet.getTaxId().equals(k)) { if (count.intValue() < MainetConstants.NUMBERS.FIVE) { VoucherPostDetailDTO
             * voucherDetails = new VoucherPostDetailDTO(); final Long sacHeadId =
             * tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(receiptMas.getOrgId(), feeDet.getTaxId(),
             * MainetConstants.STATUS.ACTIVE, currDemandId); voucherDetails.setSacHeadId(sacHeadId);
             * voucherDetails.setVoucherAmount(feeDet.getRfFeeamount()); // voucherDetails.setYearId(rebate.getYearId());
             * accountPostingRebate.getVoucherDetails().add(voucherDetails); count.incrementAndGet(); } else {
             * consolidateRebate.addAndGet(feeDet.getRfFeeamount().doubleValue()); } } }); if (consolidateRebate.doubleValue() >
             * 0) { VoucherPostDetailDTO voucherDetails = new VoucherPostDetailDTO(); final Long sacHeadId =
             * tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(receiptMas.getOrgId(), k, MainetConstants.STATUS.ACTIVE,
             * currDemandId); voucherDetails.setSacHeadId(sacHeadId);
             * voucherDetails.setVoucherAmount(BigDecimal.valueOf(consolidateRebate.doubleValue())); //
             * voucherDetails.setYearId(null); accountPostingRebate.getVoucherDetails().add(voucherDetails); } });
             * doAccountVoucherReversalPosting(accountPostingList);
             */}
    }

    private void doAccountVoucherReversalPosting(List<VoucherPostDTO> accountPostingList) {// need to change
        VoucherPostListDTO dto = new VoucherPostListDTO();
        dto.setVoucherdto(accountPostingList);
        final ResponseEntity<?> response = RestClient.callRestTemplateClient(dto,
                ServiceEndpoints.ACCOUNT_REVERSE_POSTING);
        if ((response != null) && (response.getStatusCode() == HttpStatus.OK)) {
            LOGGER.info("Account Voucher Reversal done successfully");
        } else {
            LOGGER.error("Account Voucher Posting failed due to :"
                    + (response != null ? response.getBody() : MainetConstants.BLANK));
            throw new FrameworkException("Check account integration for Reversal of Receipt");
        }
    }

    private VoucherPostDTO prepareVoucherDtoAndMode(final CommonChallanDTO requestDTO,
            final TbServiceReceiptMasEntity receiptMasEntity,
            final Organisation org, final VoucherPostDTO accountPosting, Long locationId, String activeFlag) {
        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
            VoucherPostDetailDTO voucherDetail;
            LookUp tdpPrefix;
            accountPosting.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
            if (MainetConstants.CHALLAN_RECEIPT_TYPE.NON_REVENUE_BASED
                    .equals(requestDTO.getChallanServiceType())
                    || MainetConstants.CHALLAN_RECEIPT_TYPE.OBJECTION.equals(requestDTO.getChallanServiceType())) {
                tdpPrefix = CommonMasterUtility.getValueFromPrefixLookUp(
                        MainetConstants.BILL_MASTER_COMMON.PREFIX_CODE_RV,
                        MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
            } else {
                tdpPrefix = CommonMasterUtility.getValueFromPrefixLookUp(
                        MainetConstants.BILL_MASTER_COMMON.DEMAND_COLLECTION,
                        MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
            }
            if (tdpPrefix != null && requestDTO.getTdpPrefixId() == null) {
                accountPosting.setVoucherSubTypeId(tdpPrefix.getLookUpId());
            } else if (requestDTO.getTdpPrefixId() != null) {
                accountPosting.setVoucherSubTypeId(requestDTO.getTdpPrefixId());
            }
            accountPosting.setFieldId(locationId);
            accountPosting.setVoucherDate(receiptMasEntity.getRmDate());
            accountPosting.setDepartmentId(requestDTO.getDeptId());
            //accountPosting.setVoucherReferenceNo(receiptMasEntity.getRmRcptno().toString());
            accountPosting.setVoucherReferenceDate(receiptMasEntity.getRmDate());
            accountPosting.setPayModeId(receiptMasEntity.getReceiptModeDetail().get(0).getCpdFeemode());
            accountPosting.setNarration(receiptMasEntity.getRmNarration());
            accountPosting.setPayerOrPayee(receiptMasEntity.getRmReceivedfrom());
            accountPosting.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
            accountPosting.setOrgId(receiptMasEntity.getOrgId());
            accountPosting.setCreatedBy(receiptMasEntity.getCreatedBy());
            accountPosting.setCreatedDate(new Date());
            accountPosting.setLgIpMac(receiptMasEntity.getLgIpMac());

            /*
             * voucherDetail = new VoucherPostDetailDTO();
             * voucherDetail.setVoucherAmount(receiptMasEntity.getReceiptModeDetail().getRdAmount());
             * voucherDetail.setPayModeId(receiptMasEntity.getReceiptModeDetail().getCpdFeemode());
             * accountPosting.getVoucherDetails().add(voucherDetail);
             */
        }
        return accountPosting;
    }

    private void prepareReceiptMasAndMode(final CommonChallanDTO requestDTO, final TbServiceReceiptMasEntity receiptMasEntity,
            final ApplicationSession appSession, final WardZoneBlockDTO dwzDTO, Long locationId,List<BillReceiptPostingDTO> billTaxes) {
    	List<TbSrcptModesDetEntity> receiptModeList = new CopyOnWriteArrayList<TbSrcptModesDetEntity>();
		/*
		 * final Long receiptNumber = seqGenFunctionUtility.generateSequenceNo(
		 * MainetConstants.RECEIPT_MASTER.Module, MainetConstants.RECEIPT_MASTER.Table,
		 * MainetConstants.RECEIPT_MASTER.Column, requestDTO.getOrgId(),
		 * MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
		 */
        if (dwzDTO != null) {
            receiptMasEntity.setCoddwzId1(dwzDTO.getAreaDivision1());
            receiptMasEntity.setCoddwzId2(dwzDTO.getAreaDivision2());
            receiptMasEntity.setCoddwzId3(dwzDTO.getAreaDivision3());
            receiptMasEntity.setCoddwzId4(dwzDTO.getAreaDivision4());
            receiptMasEntity.setCoddwzId5(dwzDTO.getAreaDivision5());
        }
        //receiptMasEntity.setRmRcptno(receiptNumber);
        receiptMasEntity.setApmApplicationId(requestDTO.getApplNo());
        if (null != requestDTO.getReferenceDate())
        	receiptMasEntity.setRmDate(requestDTO.getReferenceDate());
        else
        receiptMasEntity.setRmDate(new Date());
        if (!StringUtils.isEmpty(requestDTO.getManualReceiptNo()) || requestDTO.getManualReeiptDate() != null) {
            receiptMasEntity.setRmDate(requestDTO.getManualReeiptDate());
            receiptMasEntity.setManualReceiptNo(requestDTO.getManualReceiptNo());
        }
		if (requestDTO.getAmountToPay() != null) {
			if (StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, requestDTO.getDeptCode())) {
				receiptMasEntity.setRmAmount(new BigDecimal(Double.valueOf(requestDTO.getAmountToPay())).setScale(2,
						RoundingMode.HALF_DOWN));
			} else if (StringUtils.equals(PrefixConstants.NewWaterServiceConstants.BPW, requestDTO.getServiceCode())) {
				receiptMasEntity.setRmAmount(new BigDecimal(Double.valueOf(requestDTO.getAmountToPay())));
			} else if (StringUtils.equals(MainetConstants.RnLCommon.RL_SHORT_CODE, requestDTO.getDeptCode())) {
				receiptMasEntity.setRmAmount(new BigDecimal(Math.round(Double.valueOf(requestDTO.getCurrentAmount()))));

			} else {

				receiptMasEntity.setRmAmount(new BigDecimal(Math.round(Double.valueOf(requestDTO.getAmountToPay()))));
			}
		}
        if (requestDTO.getServiceId() != null) {
            final ServiceMaster service = serviceMasterService.getServiceMaster(
                    requestDTO.getServiceId(), requestDTO.getOrgId());
            requestDTO.setDeptCode(service.getTbDepartment().getDpDeptcode());
            requestDTO.setServiceCode(service.getSmShortdesc());
            receiptMasEntity.setRmNarration(appSession.getMessage("receipt.message.rmNarration") + service.getSmServiceName());
            if ((requestDTO.getLoiNo() != null) && !requestDTO.getLoiNo().isEmpty()) {
                receiptMasEntity.setRmLoiNo(requestDTO.getLoiNo());
                receiptMasEntity
                        .setRmNarration(appSession.getMessage("receipt.message.rmNarration1") + service.getSmServiceName());
            }
        }
        receiptMasEntity.setRmReceivedfrom(requestDTO.getApplicantName());
        receiptMasEntity.setSmServiceId(requestDTO.getServiceId());
        if (requestDTO.getUniquePrimaryId() != null) {
            receiptMasEntity.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
			if (StringUtils.isNotBlank(requestDTO.getParentPropNo())) {
				receiptMasEntity.setAdditionalRefNo(requestDTO.getParentPropNo());
			}
			if(StringUtils.isNotBlank(requestDTO.getFlatNo())) {
				receiptMasEntity.setFlatNo(requestDTO.getFlatNo());				
			}
            String departDesc = null;
            if(requestDTO.getLangId() > 0) {
            	departDesc = departmentService.fetchDepartmentDescEngById(requestDTO.getDeptId(),requestDTO.getLangId());
            }else {
            	departDesc = departmentService.fetchDepartmentDescById(requestDTO.getDeptId());
            }
            receiptMasEntity.setRmNarration(
                    appSession.getMessage("receipt.message.rmNarration2") + " "
                            + departDesc + " "
                            + requestDTO.getUniquePrimaryId().toString());
        }
        if (requestDTO.getNarration() != null && !requestDTO.getNarration().isEmpty()) {
            receiptMasEntity.setRmNarration(requestDTO.getNarration());
        }
        receiptMasEntity.setOrgId(requestDTO.getOrgId());
        if (requestDTO.getReceiptcategoryId() != null)
        receiptMasEntity.setRmReceiptcategoryId(requestDTO.getReceiptcategoryId());
        receiptMasEntity.setCreatedBy(requestDTO.getUserId());
        receiptMasEntity.setLgIpMac(requestDTO.getLgIpMac());
        receiptMasEntity.setCreatedDate(new Date());
        receiptMasEntity.setReceiptTypeFlag(requestDTO.getPaymentCategory());
        receiptMasEntity.setDpDeptId(requestDTO.getDeptId());
        receiptMasEntity.setFieldId(locationId);
        receiptMasEntity.setMobileNumber(requestDTO.getMobileNumber());
        receiptMasEntity.setEmailId(requestDTO.getEmailId());
		/*
		 * if(CollectionUtils.isNotEmpty(requestDTO.getMultiModeList()) &&
		 * requestDTO.getMultiModeList().size() > 1) { receiptModeList =
		 * prepareReceiptMode(receiptMasEntity, requestDTO, billTaxes); }else {
		 */
        	  final TbSrcptModesDetEntity receiptMode = new TbSrcptModesDetEntity();
              receiptMode.setRmRcptid(receiptMasEntity);
              receiptMode.setCpdFeemode(requestDTO.getPayModeIn());
              receiptMode.setRdChequeddno(requestDTO.getBmChqDDNo());
              receiptMode.setRdChequedddate(requestDTO.getBmChqDDDate());
              if(requestDTO.getTranRefDate()!=null)
            	  receiptMode.setTranRefDate(requestDTO.getTranRefDate());
              if(StringUtils.isNotBlank(requestDTO.getTranRefNumber()))
            	  receiptMode.setTranRefNumber(requestDTO.getTranRefNumber());
              receiptMode.setRdDrawnon(requestDTO.getBmDrawOn());
              if ((requestDTO.getCbBankId() != null) && (requestDTO.getCbBankId() > 0d)) {
                  receiptMode.setCbBankid(requestDTO.getCbBankId());
              }
              if ((requestDTO.getBankaAccId() != null) && (requestDTO.getBankaAccId() > 0d)) {
                  receiptMode.setBaAccountid(requestDTO.getBankaAccId());
              }
              //#129193 - to get account no on receipt
              if (requestDTO.getBmBankAccountId() != null && requestDTO.getBmBankAccountId() > 0) {
              receiptMode.setRdActNo(requestDTO.getBmBankAccountId().toString());
              }
              Organisation org=new Organisation();
              org.setOrgid(requestDTO.getOrgId());
               if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, requestDTO.getDeptCode())||StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.WATER, requestDTO.getDeptCode())) {
            	   try {
					Long checkStatusId= CommonMasterUtility.getValueFromPrefixLookUp("RND", "CLR", org).getLookUpId();
					   receiptMode.setCheckStatus(checkStatusId);
				} catch (Exception e) {
					LOGGER.error("Exception occure at the time of setting check status  "+e);
				}
               } 
              if (requestDTO.getAmountToPay() != null)
              
			if (StringUtils.equals(MainetConstants.RnLCommon.RentLease, requestDTO.getDeptCode())) {
				receiptMode.setRdAmount(new BigDecimal(Math.round(Double.valueOf(requestDTO.getCurrentAmount()))));

			} else {
				receiptMode.setRdAmount(new BigDecimal(requestDTO.getAmountToPay()));
			}
              receiptMode.setOrgId(requestDTO.getOrgId());
              receiptMode.setCreatedBy(requestDTO.getUserId());
              receiptMode.setLgIpMac(requestDTO.getLgIpMac());
              receiptMode.setCreatedDate(new Date());
              receiptModeList.add(receiptMode);
        /*}*/
        receiptMasEntity.setReceiptModeDetail(receiptModeList);
    }

    @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasEntity findByRmRcptidAndOrgId(Long refId, long orgid) {
        return receiptRepository.findByRmRcptidAndOrgId(refId, orgid);
    }

    @Override
    public TbServiceReceiptMasEntity getLatestReceiptDetailByAddRefNo(Long orgId,
            String additionalRefNo) {
        return receiptRepository.getLatestReceiptDetailByAddRefNo(orgId, additionalRefNo);
    }

    @Override
    public BigDecimal getPaidAmountByAppNo(Long orgId,
            Long applicationNo, Long deptId) {
        return receiptRepository.getPaidAmountByAppNo(orgId, applicationNo, deptId);
    }

    @Override
    public BigDecimal getPaidAmountByAdditionalRefNo(Long orgId,
            String additionalRefNo, Long deptId) {
        return receiptRepository.getPaidAmountByAdditionalRefNo(orgId, additionalRefNo, deptId);
    }

    @Override
    public List<TbServiceReceiptMasEntity> getRebateByAppNo(Long orgId,
            Long applicationNo, Long deptId) {
        return receiptRepository.getRebateByAppNo(orgId, applicationNo, deptId);
    }

    @Override
    public List<TbServiceReceiptMasEntity> getCollectionDetails(String additionalRefNo, Long deptId, long orgid) {
        return receiptRepository.getCollectionDetails(additionalRefNo, deptId, orgid);
    }
    
    @Override
    public List<TbServiceReceiptMasEntity> getAllCollectionDetails(String additionalRefNo, Long deptId, Long orgid){
    	return receiptRepository.getAllReceiptsByAddRefNo(additionalRefNo,deptId,orgid);
    }

    @Override
    public List<String> getPayeeNames(final Long orgId, Long deptId) {
        final List<String> payeeList = new ArrayList<>(0);
        if ((orgId != null) && (orgId != 0L)) {
            final List<String> payeeNewList = receiptRepository.getPayeeNames(orgId, deptId);
            if ((payeeNewList != null) && !payeeNewList.isEmpty()) {
                for (final String payeeString : payeeNewList) {
                    if ((payeeString != null) && !payeeString.isEmpty()) {
                        payeeList.add(payeeString.replaceAll(MainetConstants.StandardAccountHeadMapping.NULL,
                                MainetConstants.CommonConstants.BLANK));
                    }
                }
            }
        }
        return payeeList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbServiceReceiptMasBean> getAllReceiptsByOrgId(Long deptId, Long orgId) {
        List<TbServiceReceiptMasBean> beans = new ArrayList<>();
        List<TbSrcptFeesDetBean> feeBeans = null;
        List<TbServiceReceiptMasEntity> entities = receiptRepository.getAllReceiptsByOrgId(deptId, orgId);
        TbServiceReceiptMasBean bean = null;
        TbSrcptFeesDetBean feeBean = null;
        if (entities != null) {
            for (TbServiceReceiptMasEntity receiptMasEntity : entities) {
                bean = new TbServiceReceiptMasBean();
                receiptMasEntity.getReceiptFeeDetail();
                BeanUtils.copyProperties(receiptMasEntity, bean);
                if (receiptMasEntity.getReceiptFeeDetail() != null) {
                    feeBeans = new ArrayList<>();
                    for (TbSrcptFeesDetEntity detEntity : receiptMasEntity.getReceiptFeeDetail()) {
                        feeBean = new TbSrcptFeesDetBean();
                        BeanUtils.copyProperties(detEntity, feeBean);
                        feeBeans.add(feeBean);
                    }
                }
                bean.setRmAmount(receiptMasEntity.getRmAmount().toString());
                bean.setRmDatetemp(UtilityService.convertDateToDDMMYYYY(receiptMasEntity.getRmDate()));
                bean.setReceiptFeeDetail(feeBeans); 
                //US#134797
                if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
                	bean.setRmReceiptNo(getCustomReceiptNo(receiptMasEntity.getDpDeptId(),receiptMasEntity.getRmRcptno()));
                   }
                else {
                	if (receiptMasEntity.getRmRcptno() != null)
                	LOGGER.info("Error occured in  getAllReceiptsByOrgId method while setting receipt number");
                	bean.setRmReceiptNo(receiptMasEntity.getRmRcptno().toString());	
                }
                beans.add(bean);
            }

        }
        return beans;
    }

    @Override
    @Transactional
    public List<TbServiceReceiptMasBean> findAll(Long orgId, BigDecimal rmAmount, Long rmRcptno, String rmReceivedfrom,
            Date rmDate, Long deptId) {
        final Iterable<TbServiceReceiptMasEntity> entities = receiptEntryDao.getReceiptDetail(orgId, rmAmount, rmRcptno,
                rmReceivedfrom, rmDate, deptId);

        List<TbServiceReceiptMasBean> beans = null;
        if (entities != null) {
            beans = new ArrayList<>();
            for (final TbServiceReceiptMasEntity tbServiceReceiptMasEntity : entities) {
                if ((tbServiceReceiptMasEntity.getReceiptDelFlag() == null
                        || tbServiceReceiptMasEntity.getReceiptDelFlag().isEmpty())) {
                    if ((tbServiceReceiptMasEntity.getReceiptTypeFlag() != null
                            && !tbServiceReceiptMasEntity.getReceiptTypeFlag().isEmpty()))
                        if (tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("M")
                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("R")
                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("A")
                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("P")) {
                            TbServiceReceiptMasBean tbServiceReceiptMasBean = new TbServiceReceiptMasBean();
                            BeanUtils.copyProperties(tbServiceReceiptMasEntity, tbServiceReceiptMasBean);
                            tbServiceReceiptMasBean.setRmAmount(tbServiceReceiptMasEntity.getRmAmount().toString());
                            tbServiceReceiptMasBean.setRmDatetemp(
                                    UtilityService.convertDateToDDMMYYYY(tbServiceReceiptMasEntity.getRmDate()));
                            //US#134797
                            if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
                            	tbServiceReceiptMasBean.setRmReceiptNo(getCustomReceiptNo(tbServiceReceiptMasEntity.getDpDeptId(),tbServiceReceiptMasEntity.getRmRcptno()));
                             }else {
                                if (tbServiceReceiptMasEntity.getRmRcptno() != null)	
                                tbServiceReceiptMasBean.setRmReceiptNo(tbServiceReceiptMasEntity.getRmRcptno().toString());	
                                  LOGGER.info("Error occured in findAll() method whilesetting receipt number");
                                }
                                  beans.add(tbServiceReceiptMasBean);
                        }
                }
            }
        }
        return beans;
    }

    @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasBean findReceiptById(Long rmRcptid, Long orgId) {
        TbServiceReceiptMasBean bean = null;
        TbServiceReceiptMasEntity masEntity = receiptRepository.findByRmRcptidAndOrgId(rmRcptid, orgId);
        if (masEntity != null) {
            bean = new TbServiceReceiptMasBean();
            BeanUtils.copyProperties(masEntity, bean);
            if (!masEntity.getReceiptFeeDetail().isEmpty()) {
                List<TbSrcptFeesDetBean> receiptFeeDetail = new ArrayList<>();
                TbSrcptFeesDetBean feesDetBean = null;
                for (TbSrcptFeesDetEntity tbSrcptFeesDetEntity : masEntity.getReceiptFeeDetail()) {
                    feesDetBean = new TbSrcptFeesDetBean();
                    BeanUtils.copyProperties(tbSrcptFeesDetEntity, feesDetBean);
                    receiptFeeDetail.add(feesDetBean);
                }
                bean.setReceiptFeeDetail(receiptFeeDetail);
            }
            if (masEntity.getReceiptModeDetail() != null) {
                TbSrcptModesDetBean tbSrcptModesDetBean = new TbSrcptModesDetBean();
                BeanUtils.copyProperties(masEntity.getReceiptModeDetail().get(0), tbSrcptModesDetBean);
                bean.setReceiptModeDetailList(tbSrcptModesDetBean);
            }
            //US#134797
            if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
             bean.setRmReceiptNo(getCustomReceiptNo(masEntity.getDpDeptId(),masEntity.getRmRcptno()));
             }else {
             if (masEntity.getRmRcptno() != null)
             LOGGER.info("Error occured in findReceiptById() method while setting receipt number");
             bean.setRmReceiptNo(masEntity.getRmRcptno().toString());
            }

        }
        return bean;
    }

    @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasEntity getReceiptDetailByRcptNoAndOrgId(long rcptNo, Long orgId) {
        return receiptRepository.getReceiptDetailByRcptNoAndOrgId(rcptNo, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountOfReceiptByRefNo(String additionalRefNo, Long orgId) {
        return receiptRepository.getReceiptDetailByRcptNoAndOrgId(additionalRefNo, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasEntity getReceiptNoByLoiNoAndOrgId(String rmLoiNo, Long orgId) {
        return receiptRepository.getReceiptNoByLoiNoAndOrgId(rmLoiNo, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAjustedAdvancedAmountByAppNo(Long orgId, Long applicationNo, Long deptId) {
        return receiptRepository.getAjustedAdvancedAmountByAppNo(orgId, applicationNo, deptId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getcountOfTaxExistAgainstAppId(Long orgId, Long applicationNo, Long deptId, Long taxId) {
        return receiptRepository.getcountOfTaxExistAgainstAppId(orgId, applicationNo, deptId, taxId);
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherPostDTO getAccountPostingDtoForBillReversal(List<BillReceiptPostingDTO> billTaxes, Organisation org) {
        String activeFlag = null;

        final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
                MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
        if (accountActive != null) {
            activeFlag = accountActive.getDefaultVal();
        }
        final VoucherPostDTO accountPosting = new VoucherPostDTO();

        if (PrefixConstants.IsLookUp.STATUS.YES.equals(activeFlag)) {
            accountPosting.setVoucherDetails(new ArrayList<VoucherPostDetailDTO>(0));
            List<Long> advance = new ArrayList<>();
            List<Long> interest = new ArrayList<>();
            List<Long> rebate = new ArrayList<>();
            List<Long> penalty = new ArrayList<>();
            List<Long> demand = new ArrayList<>();
            List<Long> exemption = new ArrayList<>();
            List<Long> webRebate = new ArrayList<>();
            prepareTaxCategory(org, advance, interest, rebate, penalty, demand,exemption, webRebate);
            final Long currDemandId = CommonMasterUtility
                    .getValueFromPrefixLookUp(MainetConstants.ReceivableDemandEntry.CURRENT_YEAR,
                            MainetConstants.ReceivableDemandEntry.DEMAND_CLASSIFICATION, org)
                    .getLookUpId();
            List<Long> excludetax = new ArrayList<>();
            excludetax.addAll(advance);
            excludetax.addAll(penalty);
            excludetax.addAll(interest);
            excludetax.addAll(rebate);
            excludetax.addAll(webRebate);
            Map<Long, Long> taxHeadId = new HashMap<>();
            prepareTaxCategory(org, advance, interest, rebate, penalty, demand,exemption, webRebate);
            for (BillReceiptPostingDTO billDef : billTaxes) {
                Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
                        billDef.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
                taxHeadId.put(billDef.getTaxId(), sacHeadId);

            }
            setDemandPostingDetails(billTaxes, org, accountPosting, demand, excludetax);// for demand tax
            setTaxesPostingDetails(billTaxes, accountPosting, advance, taxHeadId);// for advance tax
            setTaxesPostingDetails(billTaxes, accountPosting, interest, taxHeadId);// for interest tax
            setTaxesPostingDetails(billTaxes, accountPosting, penalty, taxHeadId);// for penalty tax

        }
        return accountPosting;
    }

    @Override
    public TbServiceReceiptMasEntity getReceiptDetailsByAppId(Long applicationNo, Long orgId) {

        return receiptRepository.getReceiptDetailsByAppId(applicationNo, orgId);
    }

    @Override
    public Long getTotalReceiptAmountByRefIdAndReceiptType(Long refId, String receiptType, Long orgId) {
        return receiptRepository.getTotalReceiptAmountByRefIdAndReceiptType(refId, receiptType, orgId);
    }

    @Override
    public BigDecimal getActicePaidAmountByAppNo(Long orgId, Long applicationNo, Long deptId) {
        return receiptRepository.getActivePaidAmountByAppNo(orgId, applicationNo, deptId);
    }

    @Override
    public void inActiveAllDemadReceiptByAppNo(Long orgId, Long applicationNo, Long deptId, String deleteRemark,
            Long empId) {
        List<TbServiceReceiptMasEntity> recList = receiptRepository.getAllActiveDemandReceiptByAppNo(orgId, applicationNo,
                deptId);
        recList.forEach(receipt -> {
            receipt.setReceiptDelFlag("Y");
            receipt.setReceiptDelDate(new Date());
            receipt.setReceiptDelRemark(deleteRemark);
            receiptRepository.save(receipt);
        });

    }

    @Override
    @Transactional(readOnly = true)
    public List<TbServiceReceiptMasBean> findReceiptByReceiptDateType(Long refId, Long orgId,
            Date receiptDate, Long deptId, String receiptType) {
        List<TbServiceReceiptMasBean> beans = new ArrayList<>();
        List<TbSrcptFeesDetBean> feeBeans = null;
        List<TbServiceReceiptMasEntity> entities = receiptRepository
                .getReceiptByDeptAndDateAndReceiptType(refId, orgId, receiptDate, deptId, receiptType);
        TbServiceReceiptMasBean bean = null;
        TbSrcptFeesDetBean feeBean = null;
        if (entities != null) {
            for (TbServiceReceiptMasEntity receiptMasEntity : entities) {
                bean = new TbServiceReceiptMasBean();
                receiptMasEntity.getReceiptFeeDetail();
                BeanUtils.copyProperties(receiptMasEntity, bean);
                if (receiptMasEntity.getReceiptFeeDetail() != null) {
                    feeBeans = new ArrayList<>();
                    for (TbSrcptFeesDetEntity detEntity : receiptMasEntity.getReceiptFeeDetail()) {
                        feeBean = new TbSrcptFeesDetBean();
                        BeanUtils.copyProperties(detEntity, feeBean);
                        feeBeans.add(feeBean);
                    }
                }
                bean.setRmAmount(receiptMasEntity.getRmAmount().toString());
                bean.setRmDatetemp(UtilityService.convertDateToDDMMYYYY(receiptMasEntity.getRmDate()));
                bean.setReceiptFeeDetail(feeBeans);
                beans.add(bean);
            }

        }
        return beans;
    }

    @Override
    public List<TbServiceReceiptMasBean> findReceiptByReceiptDateAddRefNo(String additionalRefNo, Long orgId,
            Date receiptDate, Long deptId) {

        List<TbServiceReceiptMasBean> beans = new ArrayList<>();
        List<TbSrcptFeesDetBean> feeBeans = null;
        List<TbServiceReceiptMasEntity> entities = receiptRepository
                .getReceiptByDeptAndDateAddRefNo(additionalRefNo, orgId, receiptDate, deptId);
        TbServiceReceiptMasBean bean = null;
        TbSrcptFeesDetBean feeBean = null;
        if (entities != null) {
            for (TbServiceReceiptMasEntity receiptMasEntity : entities) {
                bean = new TbServiceReceiptMasBean();
                receiptMasEntity.getReceiptFeeDetail();
                BeanUtils.copyProperties(receiptMasEntity, bean);
                if (receiptMasEntity.getReceiptFeeDetail() != null) {
                    feeBeans = new ArrayList<>();
                    for (TbSrcptFeesDetEntity detEntity : receiptMasEntity.getReceiptFeeDetail()) {
                        feeBean = new TbSrcptFeesDetBean();
                        BeanUtils.copyProperties(detEntity, feeBean);
                        feeBeans.add(feeBean);
                    }
                }
                bean.setRmAmount(receiptMasEntity.getRmAmount().toString());
                bean.setRmDatetemp(UtilityService.convertDateToDDMMYYYY(receiptMasEntity.getRmDate()));
                bean.setReceiptFeeDetail(feeBeans);
                beans.add(bean);
            }

        }
        return beans;

    }
    @Override
	public void inActiveAllRebetReceiptByAdditionalRefNo(Long orgId, String additionalRefNo, Long deptId,
			String deleteRemark, Long empId) {
		List<TbServiceReceiptMasEntity> recList = receiptRepository.getCollectionDetails(additionalRefNo, deptId,
				orgId);
		List<TbServiceReceiptMasEntity> rebateReceiptList = new ArrayList<TbServiceReceiptMasEntity>();
		if (CollectionUtils.isNotEmpty(recList)) {
			rebateReceiptList = recList.stream().filter(rec -> rec.getReceiptTypeFlag().equals("RB"))
					.collect(Collectors.toList());
		}
		if (CollectionUtils.isNotEmpty(rebateReceiptList)) {
			rebateReceiptList.forEach(receipt -> {
				receipt.setReceiptDelFlag("Y");
				receipt.setReceiptDelDate(new Date());
				receipt.setReceiptDelRemark(deleteRemark);
				receiptRepository.save(receipt);
			});
			String activeFlag = null;
			Organisation org = new Organisation();
			org.setOrgid(orgId);
			final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
					MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
			if (accountActive != null) {
				activeFlag = accountActive.getDefaultVal();
			}
		}

	}

	@Override
	public BigDecimal getPaidAmountByAdditionalRefNoIncRebate(Long orgId, String additionalRefNo, Long deptId) {
		return receiptRepository.getPaidAmountByAdditionalRefNoIncRebate(orgId, additionalRefNo, deptId);
	}
	
	
	
    private void setReceiptModeDetail(List<CommonChallanPayModeDTO> multiModeList, double payModeAmt,
            CommonChallanPayModeDTO payDto, Map<Long, String> billNoMap, Map.Entry<Long, Double> entry) {
        CommonChallanPayModeDTO payModeDto = new CommonChallanPayModeDTO();
        BeanUtils.copyProperties(payDto, payModeDto);
        payModeDto.setBillMasId(entry.getKey());
        payModeDto.setBillMasNo(billNoMap.get(entry.getKey()));
        payModeDto.setAmount(payModeAmt);
        multiModeList.add(payModeDto);
    }

	@Override
	public TbServiceReceiptMasEntity getLatestReceiptDetailByAddRefNoAndFlatNo(Long orgId, String additionalRefNo,
			String flatNo) {
		  return receiptRepository.getLatestReceiptDetailByAddRefNoAndFlatNo(orgId, additionalRefNo, flatNo);
	}
	
	//#99721
	@Override
	public TbServiceReceiptMasEntity getReceiptDetByAppIdAndServiceId(Long applicationId, Long smServiceId,
			Long orgId) {		
		 return receiptRepository.getReceiptDetByAppIdAndServiceId(applicationId,smServiceId, orgId);
	}
	
	@Override
	public BigDecimal getReceiptAmountPaidByPropNoOrFlatNo(String propNo, String flatNo, Organisation org,
			Long deptId) {
		if(!StringUtils.isEmpty(flatNo)) {
			return receiptRepository.getPaidAmountByAdditionalRefNoAndFlatNoIncludeRebate(org.getOrgid(), propNo, deptId, flatNo);
		}else {
			return receiptRepository.getPaidAmountByAdditionalRefNoExcludRebate(org.getOrgid(), propNo, deptId);
		}
	}

	@Override
	public Long getDuplicateChequeNoCount(Long bankId, Long chequeNo) {
		return receiptRepository.getDuplicateChequeNoCount(bankId, chequeNo);
				
	}
	
	 @Override
		public void inActiveReceiptByReceiptList(Long orgId, List<TbServiceReceiptMasEntity> rebateReceiptList,
				String deleteRemark, Long empId) {
			if (CollectionUtils.isNotEmpty(rebateReceiptList)) {
				rebateReceiptList.forEach(receipt -> {
					receipt.setReceiptDelFlag("Y");
					receipt.setReceiptDelDate(new Date());
					receipt.setReceiptDelRemark(deleteRemark);
					receiptRepository.save(receipt);
				});
				String activeFlag = null;
				Organisation org = new Organisation();
				org.setOrgid(orgId);
				final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
						MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
				if (accountActive != null) {
					activeFlag = accountActive.getDefaultVal();
				}
			}

		}


	   @Override
	
	    public List<TbServiceReceiptMasBean> findReceiptDet(@RequestBody TbServiceReceiptMasBean receiptMasBean) {
		//US#134797
           if (StringUtils.isNotEmpty(receiptMasBean.getRmReceiptNo())) {
			  Long deptId = departmentService
					.getDepartmentIdByDeptCode(Utility.getDeptCodeFromCustomRcptNO(receiptMasBean.getRmReceiptNo()));
			  if (deptId != null)
				receiptMasBean.setDpDeptId(deptId);
		   }
		final Iterable<TbServiceReceiptMasEntity> entities = receiptEntryDao.getReceiptDet(receiptMasBean);
		 List<TbServiceReceiptMasBean> beans = null;
	        if (entities != null) {
	            beans = new ArrayList<>();
	            for (final TbServiceReceiptMasEntity tbServiceReceiptMasEntity : entities) {
	                if ((tbServiceReceiptMasEntity.getReceiptDelFlag() == null
	                        || tbServiceReceiptMasEntity.getReceiptDelFlag().isEmpty())) {
	                    if ((tbServiceReceiptMasEntity.getReceiptTypeFlag() != null
	                            && !tbServiceReceiptMasEntity.getReceiptTypeFlag().isEmpty()))
	                        if (tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("M")
	                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("R")
	                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("A")
	                                || tbServiceReceiptMasEntity.getReceiptTypeFlag().trim().equals("P")) {
	                            TbServiceReceiptMasBean tbServiceReceiptMasBean = new TbServiceReceiptMasBean();
	                            BeanUtils.copyProperties(tbServiceReceiptMasEntity, tbServiceReceiptMasBean);
	                            tbServiceReceiptMasBean.setRmAmount(tbServiceReceiptMasEntity.getRmAmount().toString());
	                            tbServiceReceiptMasBean.setRmDatetemp(
	                                    UtilityService.convertDateToDDMMYYYY(tbServiceReceiptMasEntity.getRmDate()));
	                            tbServiceReceiptMasBean.setApmApplicationId(tbServiceReceiptMasEntity.getApmApplicationId());
	                            tbServiceReceiptMasBean.setRefId(tbServiceReceiptMasEntity.getRefId());
	                            tbServiceReceiptMasBean.setRmLoiNo(tbServiceReceiptMasEntity.getRmLoiNo());
	                            tbServiceReceiptMasBean.setRmRcptno(tbServiceReceiptMasEntity.getRmRcptno());
	                            String departDesc = departmentService.fetchDepartmentDescEngById(tbServiceReceiptMasEntity.getDpDeptId(),receiptMasBean.getLangId());
	                            tbServiceReceiptMasBean.setDeptName(departDesc);
	                           
	                            //US#134797
	                            if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
	                            	tbServiceReceiptMasBean.setRmReceiptNo(getCustomReceiptNo(tbServiceReceiptMasEntity.getDpDeptId(),tbServiceReceiptMasEntity.getRmRcptno()));
	                              }else {
	                            	if (tbServiceReceiptMasEntity.getRmRcptno() != null)
	                                tbServiceReceiptMasBean.setRmReceiptNo(tbServiceReceiptMasEntity.getRmRcptno().toString());
	                                LOGGER.info("Error occured in findReceiptDet() method while setting receipt number");
	                              }
	                                beans.add(tbServiceReceiptMasBean);
	                        }
	                }
	            }
	        }
		
		return beans;
	}

	@Override
	public ChallanReceiptPrintDTO setValuesAndPrintReport(Long rmRcptid, Long orgId, int langId) {
		TbServiceReceiptMasBean bean = null;
		Map<Long, ChallanReportDTO> taxdto = new HashMap<>();
		final ChallanReceiptPrintDTO printDTO = new ChallanReceiptPrintDTO();
		Organisation org = iOrganisationService.getOrganisationById(orgId);
		printDTO.setOrgName(org.getONlsOrgname());
		printDTO.setOrgNameMar(org.getONlsOrgnameMar());
		TbServiceReceiptMasEntity masEntity = receiptRepository.findByRmRcptidAndOrgId(rmRcptid, orgId);
		 List<TbServiceReceiptMasEntity> rebateReceiptList = receiptRepository
	                .getReceiptByDeptAndDateAndReceiptType(masEntity.getRmRcptid(), orgId, masEntity.getRmDate(), masEntity.getDpDeptId(), "RB");
		 if(CollectionUtils.isNotEmpty(rebateReceiptList)) {
			 rebateReceiptList.forEach(receipt ->{
				 masEntity.getReceiptFeeDetail().addAll(receipt.getReceiptFeeDetail());
			 });
		 }
		if (masEntity != null) {
			bean = new TbServiceReceiptMasBean();
			BeanUtils.copyProperties(masEntity, bean);
			printDTO.setReceiptNo(masEntity.getRmRcptno().toString());
			printDTO.setReceiptId(masEntity.getRmRcptid());
			printDTO.setManualReceiptNo(masEntity.getManualReceiptNo());
			printDTO.setPayeeName(masEntity.getRmReceivedfrom());
			printDTO.setCfcCenter(masEntity.getCfcColCenterNo());
			printDTO.setCfcCounterNo(masEntity.getCfcColCounterNo());
			
			//D#147490-to get date an time for counter scheduled
			printDTO.setRecptCreatedDate(masEntity.getCreatedDate());
			printDTO.setRecptCreatedBy(masEntity.getCreatedBy());
			//#145267
			if (StringUtils.isNotEmpty(masEntity.getRmNarration()))
			printDTO.setNarration(masEntity.getRmNarration());
			if (masEntity.getRmDate() != null) {
				printDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			}
			final String date = Utility.dateToString(masEntity.getRmDate());
			printDTO.setReceiptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			final SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			String cfcDate = sd.format(new Date());
			printDTO.setCfcDate(cfcDate);
			/*
			 * final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm"); final
			 * String time = localDateFormat.format(new Date());
			 * printDTO.setReceiptTime(time);
			 */
			String serviceName = null;
			if(masEntity.getSmServiceId() != null) {
			if (langId > 0) {
				serviceName = iTbServicesMstService.getServiceNameByServiceIdLangId(masEntity.getSmServiceId(), langId);
			} else {
				serviceName = iTbServicesMstService.getServiceNameByServiceId(masEntity.getSmServiceId());
			}
			printDTO.setSubject(ApplicationSession.getInstance().getMessage("receipt.label.receipt.subject")
					+ MainetConstants.WHITE_SPACE + serviceName);
		  }
           //#134426
		   if (masEntity.getRmReceiptcategoryId() != null)
				printDTO.setRmReceiptcategoryId(masEntity.getRmReceiptcategoryId());
			String departDesc = null;
			if (langId > 0) {
				departDesc = departmentService.fetchDepartmentDescEngById(masEntity.getDpDeptId(), langId);
			} else {
				departDesc = departmentService.fetchDepartmentDescById(masEntity.getDpDeptId());
			}
			
			printDTO.setDeptName(departDesc);
			printDTO.setDeptId(masEntity.getDpDeptId());
			printDTO.setReceivedFrom(masEntity.getRmReceivedfrom());
			printDTO.setApplicationNumber(masEntity.getApmApplicationId());
			printDTO.setPaymentMode(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? CommonMasterUtility
				.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getLookUpDesc() : "");
			printDTO.setChkPmtMode(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getDescLangFirst() : "");
			printDTO.setAmount(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? 
					Double.valueOf(masEntity.getRmAmount().toString()) : 0);
			printDTO.setDdOrPpnumber(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ?
					(masEntity.getReceiptModeDetail().get(0).getRdChequeddno() !=null ? 
						masEntity.getReceiptModeDetail().get(0).getRdChequeddno() : "0"): "0");
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getRdChequedddate() != null) {
				printDTO.setDdOrPpDate(UtilityService
						.convertDateToDDMMYYYY(masEntity.getReceiptModeDetail().get(0).getRdChequedddate()));
			}
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getCbBankid() != null) {
				// #134943-to get newly added bank name also
				 BankMasterDTO dto = new BankMasterDTO();
				 dto.setBankId(masEntity.getReceiptModeDetail().get(0).getCbBankid());
				 dto = bankMasterService.getDetailsUsingBankId(dto);
				if (StringUtils.isNotEmpty(dto.getBank()) && StringUtils.isNotEmpty(dto.getBranch())) {
					final String bankName = dto.getBank() + " :: " + dto.getBranch();
					printDTO.setBankName(bankName);
				}
			}
			printDTO.setReferenceNo(masEntity.getRmLoiNo());
			printDTO.setDate(UtilityService.convertDateToDDMMYYYY(new Date()));
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getRdActNo() != null) {
				printDTO.setBankAccountId(masEntity.getReceiptModeDetail().get(0).getRdActNo());
			}
			ChallanReportDTO challanReport = null;
			String taxDesc = null;
			double totalPayable = 0d;
			double totalArrearCurrPayable = 0d;
			double totalReceived = 0d;
			double earlyPaymentDiscount = 0d;
			 Map<String, String> billDetails = null;
			 Long earlyPaymentTaxId = 0L;
			 
			for (TbSrcptFeesDetEntity rebateReceipt : masEntity.getReceiptFeeDetail()) {
				TbTaxMas taxMaster=null;
				LookUp earlyPaymentLookUp=null;
				LookUp advancePaymentLookUp=null;
				if(rebateReceipt.getTaxId()!=null) {
					taxMaster = tbTaxMasService.findById(rebateReceipt.getTaxId(), masEntity.getOrgId());
				}
				if(taxMaster!=null) {
					earlyPaymentLookUp = CommonMasterUtility.getHierarchicalLookUp(taxMaster.getTaxCategory2(),
						taxMaster.getOrgid());
					advancePaymentLookUp = CommonMasterUtility.getHierarchicalLookUp(taxMaster.getTaxCategory1(),
							taxMaster.getOrgid());
				}
				if (earlyPaymentLookUp != null && !StringUtils.equals(earlyPaymentLookUp.getLookUpCode(), "EPD")) {
					challanReport = taxdto.get(rebateReceipt.getTaxId());
					if (challanReport == null) {
						challanReport = new ChallanReportDTO();
					}
					if(rebateReceipt.getBmIdNo() != null) {
						challanReport.getBmIdNoList().add(rebateReceipt.getBmIdNo());
					}
					challanReport.setDisplaySeq(taxMaster.getTaxDisplaySeq());
					
					if(rebateReceipt.getRmDemand() != null && rebateReceipt.getRmDemand().doubleValue() > 0 ){
						if (advancePaymentLookUp != null && StringUtils.equals(advancePaymentLookUp.getLookUpCode(), "A")) {
						}else {
							challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
									+ Double.valueOf(rebateReceipt.getRmDemand().toString())));
							challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
									+ Double.valueOf(rebateReceipt.getRmDemand().toString())));
							totalArrearCurrPayable += rebateReceipt.getRmDemand().doubleValue();
							totalPayable += rebateReceipt.getRmDemand().doubleValue();
						}
					}else {
						if (advancePaymentLookUp != null && StringUtils.equals(advancePaymentLookUp.getLookUpCode(), "A")) {
						}else {
							challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
									+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
							challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
									+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
							totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
							totalPayable += rebateReceipt.getRfFeeamount().doubleValue();
						}
					}

					challanReport.setAmountReceived(Math.round(challanReport.getAmountReceived()
							+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
					challanReport.setAmountReceivedArrearCurr(Math.round(challanReport.getAmountReceivedArrearCurr()
							+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
					totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					if (langId > 0 && langId == MainetConstants.REGIONAL_LANGUAGE_ID) {
						taxDesc = tbTaxMasService.findTaxDescRegByTaxIdAndOrgId(rebateReceipt.getTaxId(), masEntity.getOrgId());
					} else
						taxDesc = tbTaxMasService.findTaxDescByTaxIdAndOrgId(rebateReceipt.getTaxId(),
								masEntity.getOrgId());
					challanReport.setDetails(taxDesc);
					taxdto.put(rebateReceipt.getTaxId(), challanReport);
				}
				
				if (earlyPaymentLookUp != null && StringUtils.equals(earlyPaymentLookUp.getLookUpCode(), "EPD") && rebateReceipt.getRfFeeamount() != null) {
                    earlyPaymentDiscount = earlyPaymentDiscount + rebateReceipt.getRfFeeamount().doubleValue();
                    earlyPaymentTaxId = rebateReceipt.getTaxId();
                }
			}
			printDTO.getPaymentList().addAll(taxdto.values().stream().collect(Collectors.toList()));
			printDTO.setTotalAmountPayable(Math.round(totalPayable));
			printDTO.setTotalReceivedAmount(Math.round(totalReceived));
			printDTO.setAmountPayable(Double.valueOf(Math.round(totalArrearCurrPayable)));
			final String amountInWords = Utility.convertBigNumberToWord(masEntity.getRmAmount());
			printDTO.setAmountInWords(amountInWords);
			if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
					&& MainetConstants.DEPT_SHORT_NAME.WATER.equals(departmentService.getDeptCode(masEntity.getDpDeptId()))) {
				printDTO.setLoiNo(masEntity.getRmLoiNo());
				printDTO.setAmountPayable(Double.valueOf((totalArrearCurrPayable)));
			}
			//D#147476
			if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)){
               //D#147528
				if (masEntity != null && masEntity.getSmServiceId() != null) {
				String serviceshortCode = iTbServicesMstService.getServiceShortDescByServiceId(masEntity.getSmServiceId());
				if (StringUtils.isNotEmpty(serviceshortCode))
				printDTO.setServiceCode(serviceshortCode);
				if (masEntity.getDpDeptId() != null) {
				String deptCode = departmentService.getDeptCode(masEntity.getDpDeptId());
						if (StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {
							printDTO.setPropNo_connNo_estateNo_L("Property No");
							printDTO.setPropNo_connNo_estateN_V(masEntity.getAdditionalRefNo());
							if (StringUtils.isBlank(masEntity.getAdditionalRefNo()) && masEntity.getApmApplicationId() != null) {
								printDTO.setPropNo_connNo_estateN_V(String.valueOf(masEntity.getApmApplicationId()));
							}
							if (StringUtils.isNotBlank(masEntity.getFlatNo())) {
								StringBuilder propertyNo = new StringBuilder();
								propertyNo.append(printDTO.getPropNo_connNo_estateN_V());
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(MainetConstants.WINDOWS_SLASH_WITH_SPACE);
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(masEntity.getFlatNo());
								printDTO.setPropNo_connNo_estateN_V(propertyNo.toString());
								printDTO.setFlatNo(masEntity.getFlatNo());
								String occpierName = getOccupierNameOrCsNo(printDTO.getPropNo_connNo_estateN_V(),
										masEntity.getFlatNo(), 0L, orgId, masEntity.getDpDeptId());
								if (occpierName != null) {
									printDTO.setOccupierName(occpierName);
								}
							}

						}else if(StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.WATER)) {
							printDTO.setPropNo_connNo_estateNo_L("Connection No");
							try {
								String connectionNo = getOccupierNameOrCsNo(null,
										null, Long.valueOf(masEntity.getAdditionalRefNo()), orgId, masEntity.getDpDeptId());
								if (connectionNo != null) {
									printDTO.setPropNo_connNo_estateN_V(connectionNo);
								}
							}catch (Exception e) {
								LOGGER.error("Exception while fetching connection no:" +e);
							}
						}
						Map<String, Date> billFromDetails = new HashMap<String, Date>();
						Map<String, Date> billToDetails = new HashMap<String, Date>();
							if(MapUtils.isEmpty(printDTO.getBillDetails()) || MapUtils.isEmpty(printDTO.getBillYearDetails())) {
								//Map<String, String> billDetail = new HashMap<String, String>();
								SortedMap<String, String> billDetail = new TreeMap<String, String>();
								//Map<String, String> billYearDetails = new HashMap<String, String>();
								SortedMap<String, String> billYearDetails = new TreeMap<String, String>();
								
								if(CollectionUtils.isNotEmpty(printDTO.getPaymentList())) {
									for (ChallanReportDTO pay : printDTO.getPaymentList()) {
										if (CollectionUtils.isNotEmpty(pay.getBmIdNoList())) {
											for (Long billNo : pay.getBmIdNoList()) {
												TbBillMas billMas = getBillMasByIdNo(billNo,
														orgId,
														printDTO.getDeptId());
												if (billMas != null) {
													String billDate = billDetail.get(billMas.getBmNo());
													if (StringUtils.isBlank(billDate)) {
														billDetail.put(String.valueOf(billMas.getBmNo()),
																new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																		.format(billMas.getBmBilldt()));
														StringBuilder billPeriod = new StringBuilder();

														String fromDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmFromdt());
														String toDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmTodt());
														billPeriod.append(fromDate);
														billPeriod.append(" ");
														billPeriod.append("to");
														billPeriod.append(" ");
														billPeriod.append(toDate);
														billYearDetails.put(String.valueOf(billMas.getBmIdno()),
																billPeriod.toString());
														billFromDetails.put(String.valueOf(billMas.getBmIdno()), billMas.getBmFromdt());
														billToDetails.put(String.valueOf(billMas.getBmIdno()), billMas.getBmTodt());
													}
												}
											}
										}
									}
							}
								printDTO.setBillDetails(billDetail);
								printDTO.setBillYearDetails(billYearDetails);
							}
							
							String lastKey = null;
							if(MapUtils.isNotEmpty(printDTO.getBillDetails())) {
								TreeMap<String, String> billDetailsMap = new TreeMap<String, String>();
								billDetailsMap.putAll(printDTO.getBillDetails());
				                lastKey = billDetailsMap.lastKey();
				                
							}
							
						if (MapUtils.isNotEmpty(printDTO.getBillYearDetails())
								&& MapUtils.isNotEmpty(billFromDetails)
								&& MapUtils.isNotEmpty(billToDetails)) {
								try {
									String lastValue = null;
									TreeMap<String, String> billYearDetailsMap = new TreeMap<String, String>();
					                billYearDetailsMap.putAll(printDTO.getBillYearDetails());
					                lastValue = billYearDetailsMap.lastKey();
					                String string = billYearDetailsMap.get(lastValue);
					                final SimpleDateFormat sdf1 = new SimpleDateFormat(MainetConstants.YEAR_FORMAT);
					                final String startYear = sdf1.format(billFromDetails.get(lastValue));
					                final SimpleDateFormat sdf2 = new SimpleDateFormat(MainetConstants.YEAR_FORMAT1);
					                final String endYaer = sdf2.format(billToDetails.get(lastValue));
					                printDTO.setFinYear(startYear + MainetConstants.HYPHEN + endYaer);
								}catch (Exception e) {
									LOGGER.error("exception while creating rebate year bill");
								}
								
							}
							DecimalFormat df = new DecimalFormat("0.00");
			                if (earlyPaymentDiscount > 0 ) {
			                	earlyPaymentDiscount = Math.ceil(earlyPaymentDiscount);
			                    challanReport = new ChallanReportDTO();
			                    challanReport
			                            .setDetails(ApplicationSession.getInstance().getMessage("property.bill.receipt.rebate.note")
			                                    + MainetConstants.WHITE_SPACE + df.format(earlyPaymentDiscount));

			                    taxdto.put(earlyPaymentTaxId, challanReport);

			                    printDTO.getPaymentList().add(challanReport);
						
						}
			                
				if (StringUtils.isNotEmpty(serviceshortCode) && (StringUtils.isNotBlank(deptCode) && deptCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE)))
		        	printDTO.setAccountHead(ApplicationSession.getInstance().getMessage("skdcl.receipt.accounthead"+"."+serviceshortCode));
				    if (StringUtils.isNotEmpty(deptCode))
				    printDTO.setDeptShortCode(deptCode);
				}
				if (StringUtils.isNotEmpty(masEntity.getAdditionalRefNo()))
				printDTO.setReferenceNo(masEntity.getAdditionalRefNo());
				if (masEntity.getSmServiceId() != null)
					printDTO.setServiceCodeflag(MainetConstants.FlagN);

				else 
					printDTO.setServiceCodeflag(MainetConstants.FlagY);
		
			}
			}
			//#148057- to get counter no. and center no. details with count
			if (StringUtils.isNotEmpty(masEntity.getCfcColCenterNo()))
			printDTO.setCfcColCenterNo(masEntity.getCfcColCenterNo());
			if (StringUtils.isNotEmpty(masEntity.getCfcColCounterNo()))
			printDTO.setCfcColCounterNo(masEntity.getCfcColCounterNo());
			
		}
		return printDTO;
	}
	
	@Override
    public List<TbServiceReceiptMasEntity> getCollectionDetailsInactive(String additionalRefNo, Long deptId, long orgid) {
        return receiptRepository.getCollectionDetailsInactive(additionalRefNo, deptId, orgid);
    }
   //#134426
   @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasEntity getReceiptDetailByRcptNoAndDeptId(Long rcptNo, Long orgId, Long deptId) {
        return receiptRepository.getReceiptDetailByRcptNoAndDeptId(rcptNo,orgId,deptId);
    }

	@Override
	public String getCustomReceiptNo(Long deptId, Long rmRcptno) {
		LOGGER.info("getCustomReceiptNo method called ");
		String deptCode = departmentService.getDeptCode(deptId);
		String rcptNo = deptCode.concat(String.valueOf((rmRcptno)));
		return rcptNo;
	}
	
	@Override
	@Transactional
	public List<TbServiceReceiptMasEntity> getCollectionDetailsWithFlatNo(String additionalRefNo, String flatNo,
			Long deptId, Long orgid) {
		return receiptRepository.getCollectionDetailsWithFlatNo(additionalRefNo, flatNo, deptId, orgid);
	}
	
	
	@Override
	@Transactional
	public TbServiceReceiptMasEntity getMaxReceiptIdByAdditinalRefNo(String additionalRefNo) {
		return receiptRepository.getMaxReceiptIdByAdditinalRefNo(additionalRefNo);
	}

	@Override
	public List<TbServiceReceiptMasEntity> getPaymentHistoryByAdditinalRefNo(String additionalRefNo, Long orgId) {
		return receiptRepository.getPaymentHistoryByAdditinalRefNo(additionalRefNo, orgId);
	}

   //#141730
   @Override
    @Transactional(readOnly = true)
    public TbServiceReceiptMasEntity getReceiptDetailByIds(Long rcptNo, Long orgId, Long deptId,String loiNo,Date rmDate,String rmAmount,Long appNo,Long refNo,String rmReceivedfrom,String date,String addRefno) {
        return receiptEntryDao.getReceiptDetailByIds(rcptNo,orgId,deptId,loiNo,rmDate,rmAmount,appNo,refNo,rmReceivedfrom, date,addRefno);
    }
   
	@Override
	public TbServiceReceiptMasEntity getReceiptDetailByCsCcnRcptNoAndOrgId(String conNo, Long rcptNo, Long orgId) {
	    return receiptRepository.getReceiptDetailByCsCcnRcptNoOrgId(conNo,rcptNo,orgId);
	}
  
	@Override
	public TbBillMas getBillMasByIdNo(Long bmIdNo, Long orgId,Long deptId) {
		TbBillMas result = null;
        try {
            BillPaymentService dynamicServiceInstance = null;
            String serviceClassName = null;
            final String deptCode = departmentService.getDeptCode(deptId);
            serviceClassName = messageSource.getMessage(MainetConstants.CHALLAN_BILL + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillPaymentService.class);
          
            	 result = dynamicServiceInstance.getBillMasByBmIdno(bmIdNo, orgId);
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in getting bill master for department Id :" + deptId, e);
        }
        return result;
	}
	
	@Override
	public String getOccupierNameOrCsNo(String propNo,String flatNo,Long csIdn, Long orgId, Long deptId) {
		String result = null;
        try {
            BillPaymentService dynamicServiceInstance = null;
            String serviceClassName = null;
            final String deptCode = departmentService.getDeptCode(deptId);
            serviceClassName = messageSource.getMessage(MainetConstants.CHALLAN_BILL + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillPaymentService.class);
          
            	 result = dynamicServiceInstance.getOccupierNameOrCsNo( propNo, flatNo, csIdn,  orgId);
        } catch (LinkageError | Exception e) {
            throw new FrameworkException("Exception in getting occupier name for department Id :" + deptId, e);
        }
        return result;
	}
	
	@Override
	public ChallanReceiptPrintDTO setValuesAndPrintReportForRebateReceipts(Long rmRcptid, Long orgId, int langId) {
		TbServiceReceiptMasBean bean = null;
		Map<Long, ChallanReportDTO> taxdto = new HashMap<>();
		final ChallanReceiptPrintDTO printDTO = new ChallanReceiptPrintDTO();
		Organisation org = iOrganisationService.getOrganisationById(orgId);
		printDTO.setOrgName(org.getONlsOrgname());
		printDTO.setOrgNameMar(org.getONlsOrgnameMar());
		TbServiceReceiptMasEntity masEntity = receiptRepository.findByRmRcptidAndOrgId(rmRcptid, orgId);
		
	
		if (masEntity != null) {
			bean = new TbServiceReceiptMasBean();
			BeanUtils.copyProperties(masEntity, bean);
			printDTO.setReceiptNo(masEntity.getRmRcptno().toString());
			printDTO.setReceiptId(masEntity.getRmRcptid());
			printDTO.setManualReceiptNo(masEntity.getManualReceiptNo());
			printDTO.setPayeeName(masEntity.getRmReceivedfrom());
			printDTO.setCfcCenter(masEntity.getCfcColCenterNo());
			printDTO.setCfcCounterNo(masEntity.getCfcColCounterNo());
			
			//D#147490-to get date an time for counter scheduled
			printDTO.setRecptCreatedDate(masEntity.getCreatedDate());
			printDTO.setRecptCreatedBy(masEntity.getCreatedBy());
			//#145267
			if (StringUtils.isNotEmpty(masEntity.getRmNarration()))
			printDTO.setNarration(masEntity.getRmNarration());
			if (masEntity.getRmDate() != null) {
				printDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			}
			final String date = Utility.dateToString(masEntity.getRmDate());
			printDTO.setReceiptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			final SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			String cfcDate = sd.format(new Date());
			printDTO.setCfcDate(cfcDate);
			/*
			 * final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm"); final
			 * String time = localDateFormat.format(new Date());
			 * printDTO.setReceiptTime(time);
			 */
			String serviceName = null;
			if(masEntity.getSmServiceId() != null) {
			if (langId > 0) {
				serviceName = iTbServicesMstService.getServiceNameByServiceIdLangId(masEntity.getSmServiceId(), langId);
			} else {
				serviceName = iTbServicesMstService.getServiceNameByServiceId(masEntity.getSmServiceId());
			}
			printDTO.setSubject(ApplicationSession.getInstance().getMessage("receipt.label.receipt.subject")
					+ MainetConstants.WHITE_SPACE + serviceName);
		  }
           //#134426
		   if (masEntity.getRmReceiptcategoryId() != null)
				printDTO.setRmReceiptcategoryId(masEntity.getRmReceiptcategoryId());
			String departDesc = null;
			if (langId > 0) {
				departDesc = departmentService.fetchDepartmentDescEngById(masEntity.getDpDeptId(), langId);
			} else {
				departDesc = departmentService.fetchDepartmentDescById(masEntity.getDpDeptId());
			}
			String deptCode = departmentService.getDeptCode(masEntity.getDpDeptId());
			
			printDTO.setDeptName(departDesc);
			printDTO.setDeptId(masEntity.getDpDeptId());
			printDTO.setReceivedFrom(masEntity.getRmReceivedfrom());
			printDTO.setApplicationNumber(masEntity.getApmApplicationId());
			printDTO.setPaymentMode(CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getLookUpDesc());
			printDTO.setChkPmtMode(CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getDescLangFirst());
			printDTO.setAmount(Double.valueOf(masEntity.getRmAmount().toString()));
			printDTO.setDdOrPpnumber(masEntity.getReceiptModeDetail().get(0).getRdChequeddno());
			if (masEntity.getReceiptModeDetail().get(0).getRdChequedddate() != null) {
				printDTO.setDdOrPpDate(UtilityService
						.convertDateToDDMMYYYY(masEntity.getReceiptModeDetail().get(0).getRdChequedddate()));
			}
			if (masEntity.getReceiptModeDetail().get(0).getCbBankid() != null) {
				// #134943-to get newly added bank name also
				 BankMasterDTO dto = new BankMasterDTO();
				 dto.setBankId(masEntity.getReceiptModeDetail().get(0).getCbBankid());
				 dto = bankMasterService.getDetailsUsingBankId(dto);
				if (StringUtils.isNotEmpty(dto.getBank()) && StringUtils.isNotEmpty(dto.getBranch())) {
					final String bankName = dto.getBank() + " :: " + dto.getBranch();
					printDTO.setBankName(bankName);
				}
			}
			printDTO.setReferenceNo(masEntity.getRmLoiNo());
			printDTO.setDate(UtilityService.convertDateToDDMMYYYY(new Date()));
			if (masEntity.getReceiptModeDetail().get(0).getRdActNo() != null) {
				printDTO.setBankAccountId(masEntity.getReceiptModeDetail().get(0).getRdActNo());
			}
			ChallanReportDTO challanReport = null;
			String taxDesc = null;
			double totalPayable = 0d;
			double totalArrearCurrPayable = 0d;
			double totalReceived = 0d;
			 Map<String, String> billDetails = null;
			 
			for (TbSrcptFeesDetEntity rebateReceipt : masEntity.getReceiptFeeDetail()) {
				TbTaxMas taxMaster=null;
				LookUp earlyPaymentLookUp=null;
				if(rebateReceipt.getTaxId()!=null) {
					taxMaster = tbTaxMasService.findById(rebateReceipt.getTaxId(), masEntity.getOrgId());
				}
					challanReport = taxdto.get(rebateReceipt.getTaxId());
					if (challanReport == null) {
						challanReport = new ChallanReportDTO();
					}
					if(rebateReceipt.getBmIdNo() != null) {
						challanReport.getBmIdNoList().add(rebateReceipt.getBmIdNo());
					}
					challanReport.setDisplaySeq(taxMaster.getTaxDisplaySeq());
					if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, deptCode)) {
						challanReport.setAmountPayable(challanReport.getAmountPayable()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						challanReport.setAmountPayablArrearCurr(challanReport.getAmountPayablArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
						totalPayable += rebateReceipt.getRfFeeamount().doubleValue();

						challanReport.setAmountReceived(challanReport.getAmountReceived()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						challanReport.setAmountReceivedArrearCurr(challanReport.getAmountReceivedArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					}else {
						challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
						totalPayable += rebateReceipt.getRfFeeamount().doubleValue();

						challanReport.setAmountReceived(Math.round(challanReport.getAmountReceived()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						challanReport.setAmountReceivedArrearCurr(Math.round(challanReport.getAmountReceivedArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					}
					if (langId > 0 && langId == MainetConstants.REGIONAL_LANGUAGE_ID) {
						taxDesc = tbTaxMasService.findTaxDescRegByTaxIdAndOrgId(rebateReceipt.getTaxId(), masEntity.getOrgId());
					} else
						taxDesc = tbTaxMasService.findTaxDescByTaxIdAndOrgId(rebateReceipt.getTaxId(),
								masEntity.getOrgId());
					challanReport.setDetails(taxDesc);
					taxdto.put(rebateReceipt.getTaxId(), challanReport);
				
			}
			printDTO.getPaymentList().addAll(taxdto.values().stream().collect(Collectors.toList()));
			printDTO.setTotalAmountPayable(Math.round(totalPayable));
			printDTO.setTotalReceivedAmount(Math.round(totalReceived));
			if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, deptCode)) {
				printDTO.setAmountPayable(Double.valueOf(Math.ceil(totalArrearCurrPayable)));
			}else {
				printDTO.setAmountPayable(Double.valueOf(Math.round(totalArrearCurrPayable)));
			}
			final String amountInWords = Utility.convertBigNumberToWord(masEntity.getRmAmount());
			printDTO.setAmountInWords(amountInWords);
			if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
					&& MainetConstants.DEPT_SHORT_NAME.WATER.equals(departmentService.getDeptCode(masEntity.getDpDeptId()))) {
				printDTO.setLoiNo(masEntity.getRmLoiNo());
				printDTO.setAmountPayable(Double.valueOf((totalArrearCurrPayable)));
			}
			//D#147476
			if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)){
               //D#147528
				if (masEntity != null && masEntity.getSmServiceId() != null) {
				String serviceshortCode = iTbServicesMstService.getServiceShortDescByServiceId(masEntity.getSmServiceId());
				if (StringUtils.isNotEmpty(serviceshortCode))
				printDTO.setServiceCode(serviceshortCode);
				if (masEntity.getDpDeptId() != null) {
						if (StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {
							printDTO.setPropNo_connNo_estateNo_L("Property No");
							printDTO.setPropNo_connNo_estateN_V(masEntity.getAdditionalRefNo());

							if (StringUtils.isNotBlank(masEntity.getFlatNo())) {
								StringBuilder propertyNo = new StringBuilder();
								propertyNo.append(printDTO.getPropNo_connNo_estateN_V());
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(MainetConstants.WINDOWS_SLASH_WITH_SPACE);
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(masEntity.getFlatNo());
								printDTO.setPropNo_connNo_estateN_V(propertyNo.toString());
								printDTO.setFlatNo(masEntity.getFlatNo());
								String occpierName = getOccupierNameOrCsNo(printDTO.getPropNo_connNo_estateN_V(),
										masEntity.getFlatNo(), 0L, orgId, masEntity.getDpDeptId());
								if (occpierName != null) {
									printDTO.setOccupierName(occpierName);
								}
							}

						}else if(StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.WATER)) {
							printDTO.setPropNo_connNo_estateNo_L("Connection No");
							try {
								String connectionNo = getOccupierNameOrCsNo(null,
										null, Long.valueOf(masEntity.getAdditionalRefNo()), orgId, masEntity.getDpDeptId());
								if (connectionNo != null) {
									printDTO.setPropNo_connNo_estateN_V(connectionNo);
								}
							}catch (Exception e) {
								LOGGER.error("Exception while fetching connection no:" +e);
							}
							
							if(MapUtils.isEmpty(printDTO.getBillDetails()) || MapUtils.isEmpty(printDTO.getBillYearDetails())) {
								Map<String, String> billDetail = new HashMap<String, String>();
								Map<String, String> billYearDetails = new HashMap<String, String>();
								if(CollectionUtils.isNotEmpty(printDTO.getPaymentList())) {
									for (ChallanReportDTO pay : printDTO.getPaymentList()) {
										if (CollectionUtils.isNotEmpty(pay.getBmIdNoList())) {
											for (Long billNo : pay.getBmIdNoList()) {

												TbBillMas billMas = getBillMasByIdNo(billNo,
														orgId,
														printDTO.getDeptId());
												if (billMas != null) {
													String billDate = billDetail.get(billMas.getBmIdno());
													if (StringUtils.isBlank(billDate)) {
														billDetail.put(String.valueOf(billMas.getBmIdno()),
																new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																		.format(billMas.getBmBilldt()));
														StringBuilder billPeriod = new StringBuilder();

														String fromDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmFromdt());
														String toDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmTodt());
														billPeriod.append(fromDate);
														billPeriod.append(" ");
														billPeriod.append("to");
														billPeriod.append(" ");
														billPeriod.append(toDate);
														billYearDetails.put(String.valueOf(billMas.getBmIdno()),
																billPeriod.toString());
													}
												}
											}
										}
									}
							}
								printDTO.setBillDetails(billDetail);
								printDTO.setBillYearDetails(billYearDetails);
							}
						}
				if (StringUtils.isNotEmpty(serviceshortCode) && (StringUtils.isNotBlank(deptCode) && deptCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE)))
		        	printDTO.setAccountHead(ApplicationSession.getInstance().getMessage("skdcl.receipt.accounthead"+"."+serviceshortCode));
				    if (StringUtils.isNotEmpty(deptCode))
				    printDTO.setDeptShortCode(deptCode);
				}
				if (StringUtils.isNotEmpty(masEntity.getAdditionalRefNo()))
				printDTO.setReferenceNo(masEntity.getAdditionalRefNo());
				if (masEntity.getSmServiceId() != null)
					printDTO.setServiceCodeflag(MainetConstants.FlagN);

				else 
					printDTO.setServiceCodeflag(MainetConstants.FlagY);
		
			}
			}
			//#148057- to get counter no. and center no. details with count
			if (StringUtils.isNotEmpty(masEntity.getCfcColCenterNo()))
			printDTO.setCfcColCenterNo(masEntity.getCfcColCenterNo());
			if (StringUtils.isNotEmpty(masEntity.getCfcColCounterNo()))
			printDTO.setCfcColCounterNo(masEntity.getCfcColCounterNo());
			
		}
		return printDTO;
	}
	
	@Override
	@Transactional
	public List<TbServiceReceiptMasEntity> getCollectionDetailsWithFlatNoActiveInactive(String additionalRefNo, String flatNo,
			Long deptId, Long orgid) {
		return receiptRepository.getCollectionDetailsWithFlatNoActiveInactive(additionalRefNo, flatNo, deptId, orgid);
	}
	
	@Override
    public List<TbServiceReceiptMasEntity> getCollectionDetailsActiveInactive(String additionalRefNo, Long deptId, long orgid) {
        return receiptRepository.getCollectionDetailsActiveInactive(additionalRefNo, deptId, orgid);
    }
	
	@Override
	public List<Long> getReceiptNoList(Date fromDt, Date toDt, String connectionNo, Long orgId){
		Long csIdn =0L;
		try {
			csIdn = getCsidnByConnectionNo(connectionNo);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return receiptRepository.getReceiptNoList(csIdn.toString(), orgId, fromDt, toDt);	
	}
	
	@Override
	public List<Long> getPropReceiptNoList(Date fromDt, Date toDt, String propertyNo, Long orgId){
		return receiptRepository.getPropReceiptNoList(propertyNo, orgId, fromDt, toDt);	
	}
	

	private Long getCsidnByConnectionNo(final String connectionNo) throws ClassNotFoundException, LinkageError {
		Long csIdn = null;
		Class<?> clazz = null;
		Object dynamicServiceInstance = null;
		String serviceClassName = null;
		serviceClassName = "com.abm.mainet.water.dao.NewWaterRepositoryImpl";
		Long orgId= UserSession.getCurrent().getOrganisation().getOrgid();
		clazz = ClassUtils.forName(serviceClassName,
				ApplicationContextProvider.getApplicationContext().getClassLoader());
		dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
				.autowire(clazz, 2, false);
		final Method method = ReflectionUtils.findMethod(clazz,"getconnIdByConnNo",
				new Class[] { String.class, Long.class });
		csIdn = (Long) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
				new Object[] { connectionNo, orgId });
		
		return csIdn;
	}
	
	@Override
	public String getBillMethodIdByPropNo(String propertyNo, Long orgId) {
		String billMethodId = null;
		try {
			Class<?> clazz = null;
			Object dynamicServiceInstance = null;
			String serviceClassName = null;
			serviceClassName = "com.abm.mainet.property.service.PropertyNoDuesCertificateServiceImpl";
			clazz = ClassUtils.forName(serviceClassName,
					ApplicationContextProvider.getApplicationContext().getClassLoader());
			dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
					.autowire(clazz, 2, false);
			final Method method = ReflectionUtils.findMethod(clazz,MainetConstants.Property.GET_PROPERTY_BILLING_METHOD,
					new Class[] { String.class, Long.class });
			Object obj = (Object) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
					new Object[] { propertyNo, orgId });

			if (obj != null) {
				billMethodId = (String) new JSONObject(obj.toString()).get("billingMethod");
			}
		} catch (LinkageError | Exception e) {
        	LOGGER.info("Exception occured while fetching csIdn from applicationId " + e.getMessage());
        }
		return billMethodId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFlatNoIdByPropNo(String propertyNo, Long orgId) {
		List<String> propertyFlatList = null;
		try {
			Class<?> clazz = null;
			 Object dynamicServiceInstance = null;
			 String serviceClassName = null;
			 serviceClassName = "com.abm.mainet.property.service.PropertyNoDuesCertificateServiceImpl";
			 clazz = ClassUtils.forName(serviceClassName,
					 ApplicationContextProvider.getApplicationContext().getClassLoader());
			 dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory()
					 .autowire(clazz, 2, false);
			 final Method method = ReflectionUtils.findMethod(clazz,MainetConstants.Property.GET_PROPERTY_FLAT_LIST,
					 new Class[] { String.class, String.class });
			 List<String> list = (List<String>)ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
					 new Object[] { propertyNo, String.valueOf(orgId) });
			 if (list != null && !list.isEmpty())
				 propertyFlatList = list;
		} catch (LinkageError | Exception e) {
        	LOGGER.info("Exception occured while fetching csIdn from applicationId " + e.getMessage());
        }	
		return propertyFlatList;
	}
	
	@Override
	public List<Long> getPropReceiptNoListByFlatNo(Date fromDt, Date toDt, String propertyNo, String flatNo, Long orgId){
		return receiptRepository.getPropReceiptNoListByFlatNo(propertyNo, flatNo, orgId, fromDt, toDt);	
	}
	
	public TbServiceReceiptMasEntity saveAbhayYojanaReceipt(final CommonChallanDTO requestDTO,
            final TbServiceReceiptMasEntity receiptMasEntity,
            Organisation org,
            Map<Long, Long> taxHeadId, List<BillReceiptPostingDTO> billDef, Long currDemandId,double totalEarlyDiscAmnt,List<Long> exemption) {
        Long receiptNumberRebate;
        final TbServiceReceiptMasEntity receiptMasEntityRebate = new TbServiceReceiptMasEntity();
        final List<TbSrcptFeesDetEntity> receiptFeeDetailrebate = new ArrayList<>();
        List<TbSrcptModesDetEntity> receiptModeList = new ArrayList<TbSrcptModesDetEntity>(); 
		/*
		 * receiptNumberRebate = seqGenFunctionUtility.generateSequenceNo(
		 * MainetConstants.RECEIPT_MASTER.Module, MainetConstants.RECEIPT_MASTER.Table,
		 * MainetConstants.RECEIPT_MASTER.Column, requestDTO.getOrgId(),
		 * MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
		 */
        
        receiptNumberRebate = seqGenFunctionUtility.generateJavaSequenceNo(MainetConstants.RECEIPT_MASTER.Module,
				MainetConstants.RECEIPT_MASTER.Table, MainetConstants.RECEIPT_MASTER.Column,
				MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
        receiptMasEntityRebate.setRmRcptno(receiptNumberRebate);
        receiptMasEntityRebate.setApmApplicationId(requestDTO.getApplNo());
        receiptMasEntityRebate.setRmDate(receiptMasEntity.getRmDate());
        receiptMasEntityRebate.setRmReceivedfrom(requestDTO.getApplicantName());
        receiptMasEntityRebate.setRmAmount(new BigDecimal(0));
        receiptMasEntityRebate.setSmServiceId(requestDTO.getServiceId());
        if (requestDTO.getUniquePrimaryId() != null) {
            receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
            String departDesc = null;
            if(requestDTO.getLangId() > 0) {
            	departDesc = departmentService.fetchDepartmentDescEngById(requestDTO.getDeptId(),requestDTO.getLangId());
            }else {
            	departDesc = departmentService.fetchDepartmentDescById(requestDTO.getDeptId());
            }
            receiptMasEntityRebate.setRmNarration("Exemption on Abhay Yojana");
            /*
             * receiptMasEntity.setRmNarration(ApplicationSession.getInstance().getMessage("receipt.message.rmNarration3") +
             * departmentService.fetchDepartmentDescById(requestDTO.getDeptId()));
             */
        }
        if(StringUtils.isNotBlank(requestDTO.getParentPropNo())) {
        	receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getParentPropNo());
        }
        
        receiptMasEntityRebate.setOrgId(requestDTO.getOrgId());
        receiptMasEntityRebate.setCreatedBy(requestDTO.getUserId());
        receiptMasEntityRebate.setLgIpMac(requestDTO.getLgIpMac());
        receiptMasEntityRebate.setCreatedDate(new Date());
        receiptMasEntityRebate.setReceiptTypeFlag("ABY");
        receiptMasEntityRebate.setCoddwzId1(receiptMasEntity.getCoddwzId1());
        receiptMasEntityRebate.setCoddwzId2(receiptMasEntity.getCoddwzId2());
        receiptMasEntityRebate.setCoddwzId3(receiptMasEntity.getCoddwzId3());
        receiptMasEntityRebate.setCoddwzId4(receiptMasEntity.getCoddwzId4());
        receiptMasEntityRebate.setCoddwzId5(receiptMasEntity.getCoddwzId5());
        receiptMasEntityRebate.setDpDeptId(requestDTO.getDeptId());
        if(StringUtils.isNotBlank(requestDTO.getFlatNo())) {
        	receiptMasEntityRebate.setFlatNo(requestDTO.getFlatNo());
        }
        	 final TbSrcptModesDetEntity receiptModeRebate = new TbSrcptModesDetEntity();
             receiptModeRebate.setRmRcptid(receiptMasEntityRebate);
             receiptModeRebate
                     .setCpdFeemode(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
                             PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
             receiptModeRebate.setRdAmount(BigDecimal.valueOf(totalEarlyDiscAmnt));
             receiptModeRebate.setOrgId(requestDTO.getOrgId());
             receiptModeRebate.setCreatedBy(requestDTO.getUserId());
             receiptModeRebate.setLgIpMac(requestDTO.getLgIpMac());
             receiptModeRebate.setCreatedDate(new Date());
             receiptModeList.add(receiptModeRebate);
        billDef.forEach(rebatemap -> {
        	if(exemption.contains(rebatemap.getTaxCategory())) {
        		TbSrcptFeesDetEntity detailRebate = new TbSrcptFeesDetEntity();
                detailRebate.setRmRcptid(receiptMasEntityRebate);
                    detailRebate.setTaxId(rebatemap.getTaxId());
                detailRebate.setRfFeeamount(BigDecimal.valueOf(rebatemap.getTaxAmount()));
                receiptMasEntityRebate
                        .setRmAmount(
                                BigDecimal.valueOf(Math.round(receiptMasEntityRebate.getRmAmount().doubleValue() + rebatemap.getTaxAmount())));
                detailRebate.setOrgId(requestDTO.getOrgId());
                detailRebate.setCreatedBy(requestDTO.getUserId());
                detailRebate.setLgIpMac(requestDTO.getLgIpMac());
                detailRebate.setCreatedDate(new Date());
                detailRebate.setBilldetId(rebatemap.getBillDetId());
                detailRebate.setBmIdNo(rebatemap.getBillMasId());
                final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
                		rebatemap.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
                detailRebate.setSacHeadId(sacHeadId);
                receiptFeeDetailrebate.add(detailRebate);
                taxHeadId.put(rebatemap.getTaxId(), sacHeadId);
        	}
        });
        receiptMasEntityRebate.setReceiptModeDetail(receiptModeList);
        receiptMasEntityRebate.setReceiptFeeDetail(receiptFeeDetailrebate);
        receiptMasEntityRebate.setFieldId(receiptMasEntity.getFieldId());
        return receiptMasEntityRebate;
    }

	@Override
	public List<TbServiceReceiptMasEntity> getReceiptAmountByRecDateAndAdditionalRefNoAndFlatNo(String additionalRefNo, String flatNo,
			Date rmDate) {
		List<TbServiceReceiptMasEntity> rmAmountList = new ArrayList<TbServiceReceiptMasEntity>();
		if(StringUtils.isNotBlank(flatNo)) {
			rmAmountList = receiptRepository.getReceiptAmountByRecDateAndAdditionalRefNoAndFlatNo(additionalRefNo, flatNo, rmDate);
		}else {
			rmAmountList = receiptRepository.getReceiptAmountByRecDateAndAdditionalRefNo(additionalRefNo, rmDate);
		}
		return rmAmountList;
	}
	
	private void setRemittanceId(TbServiceReceiptMasEntity entity) {
		try {
			Object[] resultArray = null;
			if(StringUtils.isNotBlank(entity.getAdditionalRefNo())) {
				if(StringUtils.isNotBlank(entity.getFlatNo())) {
					resultArray = receiptRepository.getchquDisHonFlagByAdditionalRefNoAndFlatNo(entity.getAdditionalRefNo(), entity.getFlatNo());
				}else {
					resultArray = receiptRepository.getchquDisHonFlagAdditionalRefNo(entity.getAdditionalRefNo());
				}
			}
			if(resultArray != null) {
				if(resultArray[0] != null) {
					String outPut = resultArray[0].toString();
					String[] result = outPut.split(",");
					if(result[0] != null) {
						String cheqDisFlat = result[0].toString();
						if(StringUtils.isNotBlank(cheqDisFlat) && StringUtils.equals(MainetConstants.FlagY, cheqDisFlat) && result[1] != null) {
							Long remittanceId = Long.valueOf(result[1].toString());
							entity.setRemittanceId(remittanceId);
						}
					}
				}
			}
		}catch (Exception e) {
			LOGGER.error("Exception ehile executing remittance method" + e);
		}
		
	}
	
	@Override
	@Transactional
	public boolean bankDepositEntry(TbServiceReceiptMasEntity receiptMasEntity, String payMode) {

    	TbServiceReceiptMasBean bean=new TbServiceReceiptMasBean();
    	TbSrcptModesDetBean modeBean=new TbSrcptModesDetBean();
		BeanUtils.copyProperties(receiptMasEntity, bean);
		if(CollectionUtils.isNotEmpty(receiptMasEntity.getReceiptModeDetail())) {
			BeanUtils.copyProperties(receiptMasEntity.getReceiptModeDetail().get(0), modeBean);
			bean.getReceiptModeDetailList().setCpdFeemodeCode(payMode);
          bean.setReceiptModeDetailList(modeBean);
		}
		bean.setPgRefId(receiptMasEntity.getPgRefId());
		bean.setRmDate(receiptMasEntity.getRmDate());
		boolean flag=false;
		Class<?> clazz = null;
		String serviceClassName = null;
		String deptCode = null;
		Object dynamicServiceInstance = null;
		try {
			serviceClassName = messageSource.getMessage("ARE", new Object[] {},
					StringUtils.EMPTY, Locale.ENGLISH);
			if (serviceClassName != null && !MainetConstants.BLANK.equals(serviceClassName)) {
				clazz = ClassUtils.forName(serviceClassName,
						ApplicationContextProvider.getApplicationContext().getClassLoader());
				dynamicServiceInstance = ApplicationContextProvider.getApplicationContext()
						.getAutowireCapableBeanFactory().autowire(clazz, 1, false);
				final Method method = ReflectionUtils.findMethod(clazz, "bankDepositEntryWeb",
						new Class[] { TbServiceReceiptMasBean.class});
				flag = (boolean) ReflectionUtils.invokeMethod(method, dynamicServiceInstance,
						new Object[] { bean });
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing bankDepositEntry method" + e);
		}
		return flag;
	}

	@Override
	public void updateMarketLicenseStatusAfterPayment(Long applNo, Long orgId,Long userId) {
		Organisation org=new Organisation();
		org.setOrgid(orgId);
		LookUp lookUp = CommonMasterUtility.getValueFromPrefixLookUp("T", PrefixConstants.LookUp.LIS, org);
		if(lookUp!=null)
		receiptRepository.updateMarketLicenseStatusAfterPayment(applNo,orgId,lookUp.getLookUpId(),new Date(),userId);
	}
	
	public TbServiceReceiptMasEntity saveWebRebateReceipt(final CommonChallanDTO requestDTO,
			final TbServiceReceiptMasEntity receiptMasEntity, Organisation org, Map<Long, Long> taxHeadId,
			BillReceiptPostingDTO billDef, Long currDemandId, double totalEarlyDiscAmnt, List<Long> webRebateList) {
		Long receiptNumberRebate;
		final TbServiceReceiptMasEntity receiptMasEntityRebate = new TbServiceReceiptMasEntity();
		final List<TbSrcptFeesDetEntity> receiptFeeDetailrebate = new ArrayList<>();
		List<TbSrcptModesDetEntity> receiptModeList = new ArrayList<TbSrcptModesDetEntity>();

		receiptNumberRebate = seqGenFunctionUtility.generateJavaSequenceNo(MainetConstants.RECEIPT_MASTER.Module,
				MainetConstants.RECEIPT_MASTER.Table, MainetConstants.RECEIPT_MASTER.Column,
				MainetConstants.RECEIPT_MASTER.Reset_Type, requestDTO.getDeptId());
		receiptMasEntityRebate.setRmRcptno(receiptNumberRebate);
		receiptMasEntityRebate.setApmApplicationId(requestDTO.getApplNo());
		receiptMasEntityRebate.setRmDate(receiptMasEntity.getRmDate());
		receiptMasEntityRebate.setRmReceivedfrom(requestDTO.getApplicantName());
		receiptMasEntityRebate.setRmAmount(new BigDecimal(0));
		receiptMasEntityRebate.setSmServiceId(requestDTO.getServiceId());
		if (requestDTO.getUniquePrimaryId() != null) {
			receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getUniquePrimaryId().toString());
			String departDesc = null;
			if (requestDTO.getLangId() > 0) {
				departDesc = departmentService.fetchDepartmentDescEngById(requestDTO.getDeptId(),
						requestDTO.getLangId());
			} else {
				departDesc = departmentService.fetchDepartmentDescById(requestDTO.getDeptId());
			}
			receiptMasEntityRebate.setRmNarration(ApplicationSession.getInstance().getMessage("receipt.message.rmNarration4") +
            		departDesc);
		}
		if (StringUtils.isNotBlank(requestDTO.getParentPropNo())) {
			receiptMasEntityRebate.setAdditionalRefNo(requestDTO.getParentPropNo());
		}

		receiptMasEntityRebate.setOrgId(requestDTO.getOrgId());
		receiptMasEntityRebate.setCreatedBy(requestDTO.getUserId());
		receiptMasEntityRebate.setLgIpMac(requestDTO.getLgIpMac());
		receiptMasEntityRebate.setCreatedDate(new Date());
		receiptMasEntityRebate.setReceiptTypeFlag("WRB");
		receiptMasEntityRebate.setCoddwzId1(receiptMasEntity.getCoddwzId1());
		receiptMasEntityRebate.setCoddwzId2(receiptMasEntity.getCoddwzId2());
		receiptMasEntityRebate.setCoddwzId3(receiptMasEntity.getCoddwzId3());
		receiptMasEntityRebate.setCoddwzId4(receiptMasEntity.getCoddwzId4());
		receiptMasEntityRebate.setCoddwzId5(receiptMasEntity.getCoddwzId5());
		receiptMasEntityRebate.setDpDeptId(requestDTO.getDeptId());
		if (StringUtils.isNotBlank(requestDTO.getFlatNo())) {
			receiptMasEntityRebate.setFlatNo(requestDTO.getFlatNo());
		}
		final TbSrcptModesDetEntity receiptModeRebate = new TbSrcptModesDetEntity();
		receiptModeRebate.setRmRcptid(receiptMasEntityRebate);
		receiptModeRebate
				.setCpdFeemode(CommonMasterUtility.getValueFromPrefixLookUp(PrefixConstants.TAX_CATEGORY.REBATE,
						PrefixConstants.LookUpPrefix.PAY_AT_COUNTER, org).getLookUpId());
		receiptModeRebate.setRdAmount(BigDecimal.valueOf(totalEarlyDiscAmnt));
		receiptModeRebate.setOrgId(requestDTO.getOrgId());
		receiptModeRebate.setCreatedBy(requestDTO.getUserId());
		receiptModeRebate.setLgIpMac(requestDTO.getLgIpMac());
		receiptModeRebate.setCreatedDate(new Date());
		receiptModeList.add(receiptModeRebate);
		billDef.getRebateDetails().forEach(rebatemap -> {
			rebatemap.forEach((k, v) -> {
				TbSrcptFeesDetEntity detailRebate = new TbSrcptFeesDetEntity();
				detailRebate.setRmRcptid(receiptMasEntityRebate);
				if (v.size() > 1) {
					detailRebate.setTaxId(k);
				}
				detailRebate.setRfFeeamount(BigDecimal.valueOf(Utility.round(v.get(0), 2)));
				receiptMasEntityRebate.setRmAmount(BigDecimal.valueOf(Math.round(receiptMasEntityRebate.getRmAmount().doubleValue() + v.get(0))));
				receiptModeRebate.setRdAmount(BigDecimal.valueOf(Math.round(receiptMasEntityRebate.getRmAmount().doubleValue() + v.get(0))));
				detailRebate.setBilldetId(v.get(1).longValue());
				detailRebate.setOrgId(requestDTO.getOrgId());
				detailRebate.setCreatedBy(requestDTO.getUserId());
				detailRebate.setLgIpMac(requestDTO.getLgIpMac());
				detailRebate.setCreatedDate(new Date());
				detailRebate.setBmIdNo(billDef.getBillMasId());
				final Long sacHeadId = tbTaxMasService.fetchSacHeadIdForReceiptDetByDemandClass(org.getOrgid(),
						billDef.getTaxId(), MainetConstants.STATUS.ACTIVE, currDemandId);
				detailRebate.setSacHeadId(sacHeadId);
				receiptFeeDetailrebate.add(detailRebate);
				taxHeadId.put(billDef.getTaxId(), sacHeadId);
			});
        });
		receiptMasEntityRebate.setReceiptModeDetail(receiptModeList);
		receiptMasEntityRebate.setReceiptFeeDetail(receiptFeeDetailrebate);
		receiptMasEntityRebate.setFieldId(receiptMasEntity.getFieldId());
		return receiptMasEntityRebate;
	}
	@Override
	public TbServiceReceiptMasEntity getMaxReceiptIdByAdditinalRefNoAndDeptId(String additionalRefNo, Long deptId) {
		return receiptRepository.getMaxReceiptIdByAdditinalRefNoAndDeptId(additionalRefNo, deptId);
	}
	
	@Override
	public boolean findDuplicateManualReceiptExist(String manualReceiptNo, Long smServiceId, Long dpDeptid, Long orgId) {
		return receiptRepository.findDuplicateManualReceiptExist(manualReceiptNo, smServiceId, dpDeptid, orgId);
	}
	
	
	@Override
	public ChallanReceiptPrintDTO setValuesAndPrintReport(TbServiceReceiptMasEntity masEntity, Long orgId, int langId) {
		TbServiceReceiptMasBean bean = null;
		Map<Long, ChallanReportDTO> taxdto = new HashMap<>();
		final ChallanReceiptPrintDTO printDTO = new ChallanReceiptPrintDTO();
		Organisation org = iOrganisationService.getOrganisationById(orgId);
		printDTO.setOrgName(org.getONlsOrgname());
		printDTO.setOrgNameMar(org.getONlsOrgnameMar());
		
		if (masEntity != null) {
			bean = new TbServiceReceiptMasBean();
			BeanUtils.copyProperties(masEntity, bean);
			printDTO.setReceiptNo(masEntity.getRmRcptno().toString());
			printDTO.setReceiptId(masEntity.getRmRcptid());
			printDTO.setManualReceiptNo(masEntity.getManualReceiptNo());
			printDTO.setPayeeName(masEntity.getRmReceivedfrom());
			printDTO.setCfcCenter(masEntity.getCfcColCenterNo());
			printDTO.setCfcCounterNo(masEntity.getCfcColCounterNo());
			
			//D#147490-to get date an time for counter scheduled
			printDTO.setRecptCreatedDate(masEntity.getCreatedDate());
			printDTO.setRecptCreatedBy(masEntity.getCreatedBy());
			//#145267
			if (StringUtils.isNotEmpty(masEntity.getRmNarration()))
			printDTO.setNarration(masEntity.getRmNarration());
			if (masEntity.getRmDate() != null) {
				printDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			}
			final String date = Utility.dateToString(masEntity.getRmDate());
			printDTO.setReceiptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			final SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			String cfcDate = sd.format(new Date());
			printDTO.setCfcDate(cfcDate);
			/*
			 * final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm"); final
			 * String time = localDateFormat.format(new Date());
			 * printDTO.setReceiptTime(time);
			 */
			String serviceName = null;
			if(masEntity.getSmServiceId() != null) {
			if (langId > 0) {
				serviceName = iTbServicesMstService.getServiceNameByServiceIdLangId(masEntity.getSmServiceId(), langId);
			} else {
				serviceName = iTbServicesMstService.getServiceNameByServiceId(masEntity.getSmServiceId());
			}
			printDTO.setSubject(ApplicationSession.getInstance().getMessage("receipt.label.receipt.subject")
					+ MainetConstants.WHITE_SPACE + serviceName);
		  }
           //#134426
		   if (masEntity.getRmReceiptcategoryId() != null)
				printDTO.setRmReceiptcategoryId(masEntity.getRmReceiptcategoryId());
			String departDesc = null;
			if (langId > 0) {
				departDesc = departmentService.fetchDepartmentDescEngById(masEntity.getDpDeptId(), langId);
			} else {
				departDesc = departmentService.fetchDepartmentDescById(masEntity.getDpDeptId());
			}
			
			printDTO.setDeptName(departDesc);
			printDTO.setDeptId(masEntity.getDpDeptId());
			printDTO.setReceivedFrom(masEntity.getRmReceivedfrom());
			printDTO.setApplicationNumber(masEntity.getApmApplicationId());
			printDTO.setPaymentMode(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? CommonMasterUtility
				.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getLookUpDesc() : "");
			printDTO.setChkPmtMode(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getDescLangFirst() : "");
			printDTO.setAmount(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ? 
					Double.valueOf(masEntity.getRmAmount().toString()) : 0);
			printDTO.setDdOrPpnumber(CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) ?
					(masEntity.getReceiptModeDetail().get(0).getRdChequeddno() !=null ? 
						masEntity.getReceiptModeDetail().get(0).getRdChequeddno() : "0"): "0");
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getRdChequedddate() != null) {
				printDTO.setDdOrPpDate(UtilityService
						.convertDateToDDMMYYYY(masEntity.getReceiptModeDetail().get(0).getRdChequedddate()));
			}
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getCbBankid() != null) {
				// #134943-to get newly added bank name also
				 BankMasterDTO dto = new BankMasterDTO();
				 dto.setBankId(masEntity.getReceiptModeDetail().get(0).getCbBankid());
				 dto = bankMasterService.getDetailsUsingBankId(dto);
				if (StringUtils.isNotEmpty(dto.getBank()) && StringUtils.isNotEmpty(dto.getBranch())) {
					final String bankName = dto.getBank() + " :: " + dto.getBranch();
					printDTO.setBankName(bankName);
				}
			}
			printDTO.setReferenceNo(masEntity.getRmLoiNo());
			printDTO.setDate(UtilityService.convertDateToDDMMYYYY(new Date()));
			if (CollectionUtils.isNotEmpty(masEntity.getReceiptModeDetail()) && masEntity.getReceiptModeDetail().get(0).getRdActNo() != null) {
				printDTO.setBankAccountId(masEntity.getReceiptModeDetail().get(0).getRdActNo());
			}
			ChallanReportDTO challanReport = null;
			String taxDesc = null;
			double totalPayable = 0d;
			double totalArrearCurrPayable = 0d;
			double totalReceived = 0d;
			double earlyPaymentDiscount = 0d;
			 Map<String, String> billDetails = null;
			 Long earlyPaymentTaxId = 0L;
			 
			for (TbSrcptFeesDetEntity rebateReceipt : masEntity.getReceiptFeeDetail()) {
				TbTaxMas taxMaster=null;
				LookUp earlyPaymentLookUp=null;
				LookUp advancePaymentLookUp=null;
				if(rebateReceipt.getTaxId()!=null) {
					taxMaster = tbTaxMasService.findById(rebateReceipt.getTaxId(), masEntity.getOrgId());
				}
				if(taxMaster!=null) {
					earlyPaymentLookUp = CommonMasterUtility.getHierarchicalLookUp(taxMaster.getTaxCategory2(),
						taxMaster.getOrgid());
					advancePaymentLookUp = CommonMasterUtility.getHierarchicalLookUp(taxMaster.getTaxCategory1(),
							taxMaster.getOrgid());
				}
				if (earlyPaymentLookUp != null && !StringUtils.equals(earlyPaymentLookUp.getLookUpCode(), "EPD")) {
					challanReport = taxdto.get(rebateReceipt.getTaxId());
					if (challanReport == null) {
						challanReport = new ChallanReportDTO();
					}
					if(rebateReceipt.getBmIdNo() != null) {
						challanReport.getBmIdNoList().add(rebateReceipt.getBmIdNo());
					}
					challanReport.setDisplaySeq(taxMaster.getTaxDisplaySeq());
					
					if(rebateReceipt.getRmDemand() != null && rebateReceipt.getRmDemand().doubleValue() > 0 ){
						if (advancePaymentLookUp != null && StringUtils.equals(advancePaymentLookUp.getLookUpCode(), "A")) {
						}else {
							challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
									+ Double.valueOf(rebateReceipt.getRmDemand().toString())));
							challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
									+ Double.valueOf(rebateReceipt.getRmDemand().toString())));
							totalArrearCurrPayable += rebateReceipt.getRmDemand().doubleValue();
							totalPayable += rebateReceipt.getRmDemand().doubleValue();
						}
					}else {
						if (advancePaymentLookUp != null && StringUtils.equals(advancePaymentLookUp.getLookUpCode(), "A")) {
						}else {
							challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
									+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
							challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
									+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
							totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
							totalPayable += rebateReceipt.getRfFeeamount().doubleValue();
						}
					}

					challanReport.setAmountReceived(Math.round(challanReport.getAmountReceived()
							+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
					challanReport.setAmountReceivedArrearCurr(Math.round(challanReport.getAmountReceivedArrearCurr()
							+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
					totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					if (langId > 0 && langId == MainetConstants.REGIONAL_LANGUAGE_ID) {
						taxDesc = tbTaxMasService.findTaxDescRegByTaxIdAndOrgId(rebateReceipt.getTaxId(), masEntity.getOrgId());
					} else
						taxDesc = tbTaxMasService.findTaxDescByTaxIdAndOrgId(rebateReceipt.getTaxId(),
								masEntity.getOrgId());
					challanReport.setDetails(taxDesc);
					taxdto.put(rebateReceipt.getTaxId(), challanReport);
				}
				
				if (earlyPaymentLookUp != null && StringUtils.equals(earlyPaymentLookUp.getLookUpCode(), "EPD") && rebateReceipt.getRfFeeamount() != null) {
                    earlyPaymentDiscount = earlyPaymentDiscount + rebateReceipt.getRfFeeamount().doubleValue();
                    earlyPaymentTaxId = rebateReceipt.getTaxId();
                }
			}
			printDTO.getPaymentList().addAll(taxdto.values().stream().collect(Collectors.toList()));
			printDTO.setTotalAmountPayable(Math.round(totalPayable));
			printDTO.setTotalReceivedAmount(Math.round(totalReceived));
			printDTO.setAmountPayable(Double.valueOf(Math.round(totalArrearCurrPayable)));
			final String amountInWords = Utility.convertBigNumberToWord(masEntity.getRmAmount());
			printDTO.setAmountInWords(amountInWords);
			if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
					&& MainetConstants.DEPT_SHORT_NAME.WATER.equals(departmentService.getDeptCode(masEntity.getDpDeptId()))) {
				printDTO.setLoiNo(masEntity.getRmLoiNo());
				printDTO.setAmountPayable(Double.valueOf((totalArrearCurrPayable)));
			}
			//D#147476
			if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)){
               //D#147528
				if (masEntity != null && masEntity.getSmServiceId() != null) {
				String serviceshortCode = iTbServicesMstService.getServiceShortDescByServiceId(masEntity.getSmServiceId());
				if (StringUtils.isNotEmpty(serviceshortCode))
				printDTO.setServiceCode(serviceshortCode);
				if (masEntity.getDpDeptId() != null) {
				String deptCode = departmentService.getDeptCode(masEntity.getDpDeptId());
						if (StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {
							printDTO.setPropNo_connNo_estateNo_L("Property No");
							printDTO.setPropNo_connNo_estateN_V(masEntity.getAdditionalRefNo());
							if (StringUtils.isBlank(masEntity.getAdditionalRefNo()) && masEntity.getApmApplicationId() != null) {
								printDTO.setPropNo_connNo_estateN_V(String.valueOf(masEntity.getApmApplicationId()));
							}
							if (StringUtils.isNotBlank(masEntity.getFlatNo())) {
								StringBuilder propertyNo = new StringBuilder();
								propertyNo.append(printDTO.getPropNo_connNo_estateN_V());
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(MainetConstants.WINDOWS_SLASH_WITH_SPACE);
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(masEntity.getFlatNo());
								printDTO.setPropNo_connNo_estateN_V(propertyNo.toString());
								printDTO.setFlatNo(masEntity.getFlatNo());
								String occpierName = getOccupierNameOrCsNo(printDTO.getPropNo_connNo_estateN_V(),
										masEntity.getFlatNo(), 0L, orgId, masEntity.getDpDeptId());
								if (occpierName != null) {
									printDTO.setOccupierName(occpierName);
								}
							}

						}else if(StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.WATER)) {
							printDTO.setPropNo_connNo_estateNo_L("Connection No");
							try {
								String connectionNo = getOccupierNameOrCsNo(null,
										null, Long.valueOf(masEntity.getAdditionalRefNo()), orgId, masEntity.getDpDeptId());
								if (connectionNo != null) {
									printDTO.setPropNo_connNo_estateN_V(connectionNo);
								}
							}catch (Exception e) {
								LOGGER.error("Exception while fetching connection no:" +e);
							}
						}
						Map<String, Date> billFromDetails = new HashMap<String, Date>();
						Map<String, Date> billToDetails = new HashMap<String, Date>();
							if(MapUtils.isEmpty(printDTO.getBillDetails()) || MapUtils.isEmpty(printDTO.getBillYearDetails())) {
								//Map<String, String> billDetail = new HashMap<String, String>();
								SortedMap<String, String> billDetail = new TreeMap<String, String>();
								//Map<String, String> billYearDetails = new HashMap<String, String>();
								SortedMap<String, String> billYearDetails = new TreeMap<String, String>();
								
								if(CollectionUtils.isNotEmpty(printDTO.getPaymentList())) {
									for (ChallanReportDTO pay : printDTO.getPaymentList()) {
										if (CollectionUtils.isNotEmpty(pay.getBmIdNoList())) {
											for (Long billNo : pay.getBmIdNoList()) {
												TbBillMas billMas = getBillMasByIdNo(billNo,
														orgId,
														printDTO.getDeptId());
												if (billMas != null) {
													String billDate = billDetail.get(billMas.getBmNo());
													if (StringUtils.isBlank(billDate)) {
														billDetail.put(String.valueOf(billMas.getBmNo()),
																new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																		.format(billMas.getBmBilldt()));
														StringBuilder billPeriod = new StringBuilder();

														String fromDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmFromdt());
														String toDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmTodt());
														billPeriod.append(fromDate);
														billPeriod.append(" ");
														billPeriod.append("to");
														billPeriod.append(" ");
														billPeriod.append(toDate);
														billYearDetails.put(String.valueOf(billMas.getBmIdno()),
																billPeriod.toString());
														billFromDetails.put(String.valueOf(billMas.getBmIdno()), billMas.getBmFromdt());
														billToDetails.put(String.valueOf(billMas.getBmIdno()), billMas.getBmTodt());
													}
												}
											}
										}
									}
							}
								printDTO.setBillDetails(billDetail);
								printDTO.setBillYearDetails(billYearDetails);
							}
							
							String lastKey = null;
							if(MapUtils.isNotEmpty(printDTO.getBillDetails())) {
								TreeMap<String, String> billDetailsMap = new TreeMap<String, String>();
								billDetailsMap.putAll(printDTO.getBillDetails());
				                lastKey = billDetailsMap.lastKey();
				                
							}
							
						if (MapUtils.isNotEmpty(printDTO.getBillYearDetails())
								&& MapUtils.isNotEmpty(billFromDetails)
								&& MapUtils.isNotEmpty(billToDetails)) {
								try {
									String lastValue = null;
									TreeMap<String, String> billYearDetailsMap = new TreeMap<String, String>();
					                billYearDetailsMap.putAll(printDTO.getBillYearDetails());
					                lastValue = billYearDetailsMap.lastKey();
					                String string = billYearDetailsMap.get(lastValue);
					                final SimpleDateFormat sdf1 = new SimpleDateFormat(MainetConstants.YEAR_FORMAT);
					                final String startYear = sdf1.format(billFromDetails.get(lastValue));
					                final SimpleDateFormat sdf2 = new SimpleDateFormat(MainetConstants.YEAR_FORMAT1);
					                final String endYaer = sdf2.format(billToDetails.get(lastValue));
					                printDTO.setFinYear(startYear + MainetConstants.HYPHEN + endYaer);
								}catch (Exception e) {
									LOGGER.error("exception while creating rebate year bill");
								}
								
							}
							DecimalFormat df = new DecimalFormat("0.00");
			                if (earlyPaymentDiscount > 0 ) {
			                	earlyPaymentDiscount = Math.ceil(earlyPaymentDiscount);
			                    challanReport = new ChallanReportDTO();
			                    challanReport
			                            .setDetails(ApplicationSession.getInstance().getMessage("property.bill.receipt.rebate.note")
			                                    + MainetConstants.WHITE_SPACE + df.format(earlyPaymentDiscount));

			                    taxdto.put(earlyPaymentTaxId, challanReport);

			                    printDTO.getPaymentList().add(challanReport);
						
						}
			                
				if (StringUtils.isNotEmpty(serviceshortCode) && (StringUtils.isNotBlank(deptCode) && deptCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE)))
		        	printDTO.setAccountHead(ApplicationSession.getInstance().getMessage("skdcl.receipt.accounthead"+"."+serviceshortCode));
				    if (StringUtils.isNotEmpty(deptCode))
				    printDTO.setDeptShortCode(deptCode);
				}
				if (StringUtils.isNotEmpty(masEntity.getAdditionalRefNo()))
				printDTO.setReferenceNo(masEntity.getAdditionalRefNo());
				if (masEntity.getSmServiceId() != null)
					printDTO.setServiceCodeflag(MainetConstants.FlagN);

				else 
					printDTO.setServiceCodeflag(MainetConstants.FlagY);
		
			}
			}
			//#148057- to get counter no. and center no. details with count
			if (StringUtils.isNotEmpty(masEntity.getCfcColCenterNo()))
			printDTO.setCfcColCenterNo(masEntity.getCfcColCenterNo());
			if (StringUtils.isNotEmpty(masEntity.getCfcColCounterNo()))
			printDTO.setCfcColCounterNo(masEntity.getCfcColCounterNo());
			
		}
		return printDTO;
	}
	
	@Override
	public ChallanReceiptPrintDTO setValuesAndPrintReportForRebateReceipts(TbServiceReceiptMasEntity masEntity, Long orgId, int langId) {
		TbServiceReceiptMasBean bean = null;
		Map<Long, ChallanReportDTO> taxdto = new HashMap<>();
		final ChallanReceiptPrintDTO printDTO = new ChallanReceiptPrintDTO();
		Organisation org = iOrganisationService.getOrganisationById(orgId);
		printDTO.setOrgName(org.getONlsOrgname());
		printDTO.setOrgNameMar(org.getONlsOrgnameMar());
		
	
		if (masEntity != null) {
			bean = new TbServiceReceiptMasBean();
			BeanUtils.copyProperties(masEntity, bean);
			printDTO.setReceiptNo(masEntity.getRmRcptno().toString());
			printDTO.setReceiptId(masEntity.getRmRcptid());
			printDTO.setManualReceiptNo(masEntity.getManualReceiptNo());
			printDTO.setPayeeName(masEntity.getRmReceivedfrom());
			printDTO.setCfcCenter(masEntity.getCfcColCenterNo());
			printDTO.setCfcCounterNo(masEntity.getCfcColCounterNo());
			
			//D#147490-to get date an time for counter scheduled
			printDTO.setRecptCreatedDate(masEntity.getCreatedDate());
			printDTO.setRecptCreatedBy(masEntity.getCreatedBy());
			//#145267
			if (StringUtils.isNotEmpty(masEntity.getRmNarration()))
			printDTO.setNarration(masEntity.getRmNarration());
			if (masEntity.getRmDate() != null) {
				printDTO.setRcptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			}
			final String date = Utility.dateToString(masEntity.getRmDate());
			printDTO.setReceiptDate(new SimpleDateFormat(MainetConstants.DATE_FORMAT).format(masEntity.getRmDate()));
			final SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			String cfcDate = sd.format(new Date());
			printDTO.setCfcDate(cfcDate);
			/*
			 * final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm"); final
			 * String time = localDateFormat.format(new Date());
			 * printDTO.setReceiptTime(time);
			 */
			String serviceName = null;
			if(masEntity.getSmServiceId() != null) {
			if (langId > 0) {
				serviceName = iTbServicesMstService.getServiceNameByServiceIdLangId(masEntity.getSmServiceId(), langId);
			} else {
				serviceName = iTbServicesMstService.getServiceNameByServiceId(masEntity.getSmServiceId());
			}
			printDTO.setSubject(ApplicationSession.getInstance().getMessage("receipt.label.receipt.subject")
					+ MainetConstants.WHITE_SPACE + serviceName);
		  }
           //#134426
		   if (masEntity.getRmReceiptcategoryId() != null)
				printDTO.setRmReceiptcategoryId(masEntity.getRmReceiptcategoryId());
			String departDesc = null;
			if (langId > 0) {
				departDesc = departmentService.fetchDepartmentDescEngById(masEntity.getDpDeptId(), langId);
			} else {
				departDesc = departmentService.fetchDepartmentDescById(masEntity.getDpDeptId());
			}
			String deptCode = departmentService.getDeptCode(masEntity.getDpDeptId());
			
			printDTO.setDeptName(departDesc);
			printDTO.setDeptId(masEntity.getDpDeptId());
			printDTO.setReceivedFrom(masEntity.getRmReceivedfrom());
			printDTO.setApplicationNumber(masEntity.getApmApplicationId());
			printDTO.setPaymentMode(CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getLookUpDesc());
			printDTO.setChkPmtMode(CommonMasterUtility
					.getNonHierarchicalLookUpObject(masEntity.getReceiptModeDetail().get(0).getCpdFeemode(), org)
					.getDescLangFirst());
			printDTO.setAmount(Double.valueOf(masEntity.getRmAmount().toString()));
			printDTO.setDdOrPpnumber(masEntity.getReceiptModeDetail().get(0).getRdChequeddno());
			if (masEntity.getReceiptModeDetail().get(0).getRdChequedddate() != null) {
				printDTO.setDdOrPpDate(UtilityService
						.convertDateToDDMMYYYY(masEntity.getReceiptModeDetail().get(0).getRdChequedddate()));
			}
			if (masEntity.getReceiptModeDetail().get(0).getCbBankid() != null) {
				// #134943-to get newly added bank name also
				 BankMasterDTO dto = new BankMasterDTO();
				 dto.setBankId(masEntity.getReceiptModeDetail().get(0).getCbBankid());
				 dto = bankMasterService.getDetailsUsingBankId(dto);
				if (StringUtils.isNotEmpty(dto.getBank()) && StringUtils.isNotEmpty(dto.getBranch())) {
					final String bankName = dto.getBank() + " :: " + dto.getBranch();
					printDTO.setBankName(bankName);
				}
			}
			printDTO.setReferenceNo(masEntity.getRmLoiNo());
			printDTO.setDate(UtilityService.convertDateToDDMMYYYY(new Date()));
			if (masEntity.getReceiptModeDetail().get(0).getRdActNo() != null) {
				printDTO.setBankAccountId(masEntity.getReceiptModeDetail().get(0).getRdActNo());
			}
			ChallanReportDTO challanReport = null;
			String taxDesc = null;
			double totalPayable = 0d;
			double totalArrearCurrPayable = 0d;
			double totalReceived = 0d;
			 Map<String, String> billDetails = null;
			 
			for (TbSrcptFeesDetEntity rebateReceipt : masEntity.getReceiptFeeDetail()) {
				TbTaxMas taxMaster=null;
				LookUp earlyPaymentLookUp=null;
				if(rebateReceipt.getTaxId()!=null) {
					taxMaster = tbTaxMasService.findById(rebateReceipt.getTaxId(), masEntity.getOrgId());
				}
					challanReport = taxdto.get(rebateReceipt.getTaxId());
					if (challanReport == null) {
						challanReport = new ChallanReportDTO();
					}
					if(rebateReceipt.getBmIdNo() != null) {
						challanReport.getBmIdNoList().add(rebateReceipt.getBmIdNo());
					}
					challanReport.setDisplaySeq(taxMaster.getTaxDisplaySeq());
					if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, deptCode)) {
						challanReport.setAmountPayable(challanReport.getAmountPayable()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						challanReport.setAmountPayablArrearCurr(challanReport.getAmountPayablArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
						totalPayable += rebateReceipt.getRfFeeamount().doubleValue();

						challanReport.setAmountReceived(challanReport.getAmountReceived()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						challanReport.setAmountReceivedArrearCurr(challanReport.getAmountReceivedArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString()));
						totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					}else {
						challanReport.setAmountPayable(Math.round(challanReport.getAmountPayable()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						challanReport.setAmountPayablArrearCurr(Math.round(challanReport.getAmountPayablArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						totalArrearCurrPayable += rebateReceipt.getRfFeeamount().doubleValue();
						totalPayable += rebateReceipt.getRfFeeamount().doubleValue();

						challanReport.setAmountReceived(Math.round(challanReport.getAmountReceived()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						challanReport.setAmountReceivedArrearCurr(Math.round(challanReport.getAmountReceivedArrearCurr()
								+ Double.valueOf(rebateReceipt.getRfFeeamount().toString())));
						totalReceived += rebateReceipt.getRfFeeamount().doubleValue();
					}
					if (langId > 0 && langId == MainetConstants.REGIONAL_LANGUAGE_ID) {
						taxDesc = tbTaxMasService.findTaxDescRegByTaxIdAndOrgId(rebateReceipt.getTaxId(), masEntity.getOrgId());
					} else
						taxDesc = tbTaxMasService.findTaxDescByTaxIdAndOrgId(rebateReceipt.getTaxId(),
								masEntity.getOrgId());
					challanReport.setDetails(taxDesc);
					taxdto.put(rebateReceipt.getTaxId(), challanReport);
				
			}
			printDTO.getPaymentList().addAll(taxdto.values().stream().collect(Collectors.toList()));
			printDTO.setTotalAmountPayable(Math.round(totalPayable));
			printDTO.setTotalReceivedAmount(Math.round(totalReceived));
			if(StringUtils.equals(MainetConstants.DEPT_SHORT_NAME.PROPERTY, deptCode)) {
				printDTO.setAmountPayable(Double.valueOf(Math.ceil(totalArrearCurrPayable)));
			}else {
				printDTO.setAmountPayable(Double.valueOf(Math.round(totalArrearCurrPayable)));
			}
			final String amountInWords = Utility.convertBigNumberToWord(masEntity.getRmAmount());
			printDTO.setAmountInWords(amountInWords);
			if(Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
					&& MainetConstants.DEPT_SHORT_NAME.WATER.equals(departmentService.getDeptCode(masEntity.getDpDeptId()))) {
				printDTO.setLoiNo(masEntity.getRmLoiNo());
				printDTO.setAmountPayable(Double.valueOf((totalArrearCurrPayable)));
			}
			//D#147476
			if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)){
               //D#147528
				if (masEntity != null && masEntity.getSmServiceId() != null) {
				String serviceshortCode = iTbServicesMstService.getServiceShortDescByServiceId(masEntity.getSmServiceId());
				if (StringUtils.isNotEmpty(serviceshortCode))
				printDTO.setServiceCode(serviceshortCode);
				if (masEntity.getDpDeptId() != null) {
						if (StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {
							printDTO.setPropNo_connNo_estateNo_L("Property No");
							printDTO.setPropNo_connNo_estateN_V(masEntity.getAdditionalRefNo());

							if (StringUtils.isNotBlank(masEntity.getFlatNo())) {
								StringBuilder propertyNo = new StringBuilder();
								propertyNo.append(printDTO.getPropNo_connNo_estateN_V());
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(MainetConstants.WINDOWS_SLASH_WITH_SPACE);
								propertyNo.append(MainetConstants.WHITE_SPACE);
								propertyNo.append(masEntity.getFlatNo());
								printDTO.setPropNo_connNo_estateN_V(propertyNo.toString());
								printDTO.setFlatNo(masEntity.getFlatNo());
								String occpierName = getOccupierNameOrCsNo(printDTO.getPropNo_connNo_estateN_V(),
										masEntity.getFlatNo(), 0L, orgId, masEntity.getDpDeptId());
								if (occpierName != null) {
									printDTO.setOccupierName(occpierName);
								}
							}

						}else if(StringUtils.equals(deptCode, MainetConstants.DEPT_SHORT_NAME.WATER)) {
							printDTO.setPropNo_connNo_estateNo_L("Connection No");
							try {
								String connectionNo = getOccupierNameOrCsNo(null,
										null, Long.valueOf(masEntity.getAdditionalRefNo()), orgId, masEntity.getDpDeptId());
								if (connectionNo != null) {
									printDTO.setPropNo_connNo_estateN_V(connectionNo);
								}
							}catch (Exception e) {
								LOGGER.error("Exception while fetching connection no:" +e);
							}
							
							if(MapUtils.isEmpty(printDTO.getBillDetails()) || MapUtils.isEmpty(printDTO.getBillYearDetails())) {
								Map<String, String> billDetail = new HashMap<String, String>();
								Map<String, String> billYearDetails = new HashMap<String, String>();
								if(CollectionUtils.isNotEmpty(printDTO.getPaymentList())) {
									for (ChallanReportDTO pay : printDTO.getPaymentList()) {
										if (CollectionUtils.isNotEmpty(pay.getBmIdNoList())) {
											for (Long billNo : pay.getBmIdNoList()) {

												TbBillMas billMas = getBillMasByIdNo(billNo,
														orgId,
														printDTO.getDeptId());
												if (billMas != null) {
													String billDate = billDetail.get(billMas.getBmIdno());
													if (StringUtils.isBlank(billDate)) {
														billDetail.put(String.valueOf(billMas.getBmIdno()),
																new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																		.format(billMas.getBmBilldt()));
														StringBuilder billPeriod = new StringBuilder();

														String fromDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmFromdt());
														String toDate = new SimpleDateFormat(MainetConstants.DATE_FORMAT)
																.format(billMas.getBmTodt());
														billPeriod.append(fromDate);
														billPeriod.append(" ");
														billPeriod.append("to");
														billPeriod.append(" ");
														billPeriod.append(toDate);
														billYearDetails.put(String.valueOf(billMas.getBmIdno()),
																billPeriod.toString());
													}
												}
											}
										}
									}
							}
								printDTO.setBillDetails(billDetail);
								printDTO.setBillYearDetails(billYearDetails);
							}
						}
				if (StringUtils.isNotEmpty(serviceshortCode) && (StringUtils.isNotBlank(deptCode) && deptCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE)))
		        	printDTO.setAccountHead(ApplicationSession.getInstance().getMessage("skdcl.receipt.accounthead"+"."+serviceshortCode));
				    if (StringUtils.isNotEmpty(deptCode))
				    printDTO.setDeptShortCode(deptCode);
				}
				if (StringUtils.isNotEmpty(masEntity.getAdditionalRefNo()))
				printDTO.setReferenceNo(masEntity.getAdditionalRefNo());
				if (masEntity.getSmServiceId() != null)
					printDTO.setServiceCodeflag(MainetConstants.FlagN);

				else 
					printDTO.setServiceCodeflag(MainetConstants.FlagY);
		
			}
			}
			//#148057- to get counter no. and center no. details with count
			if (StringUtils.isNotEmpty(masEntity.getCfcColCenterNo()))
			printDTO.setCfcColCenterNo(masEntity.getCfcColCenterNo());
			if (StringUtils.isNotEmpty(masEntity.getCfcColCounterNo()))
			printDTO.setCfcColCounterNo(masEntity.getCfcColCounterNo());
			
		}
		return printDTO;
	}
}
