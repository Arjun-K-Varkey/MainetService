package com.abm.mainet.bill.ui.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.bill.service.BillPaymentService;
import com.abm.mainet.bill.service.ChequeDishonorService;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.PrefixConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.JsonViewObject;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptModesDetEntity;
import com.abm.mainet.common.integration.acccount.dto.TbServiceReceiptMasBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptFeesDetBean;
import com.abm.mainet.common.integration.acccount.dto.TbSrcptModesDetBean;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDTO;
import com.abm.mainet.common.integration.acccount.dto.VoucherPostDetailDTO;
import com.abm.mainet.common.integration.dto.TbBillMas;
import com.abm.mainet.common.master.dto.TbDepartment;
import com.abm.mainet.common.master.dto.TbTaxMas;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.master.service.ICommonReversalEntry;
import com.abm.mainet.common.master.service.TbTaxMasService;
import com.abm.mainet.common.repository.ReceiptRepository;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.service.IReceiptEntryService;
import com.abm.mainet.common.ui.controller.ReceivableDemandEntryController;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.user.activity.dto.UserActivityDTO;
import com.abm.mainet.common.user.activity.service.UserActivityService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;

/**
 * @author Rahul.Yadav
 *
 */
@Component
@Scope("session")
public class ChequeDishonorModel extends AbstractFormModel {

    private static final long serialVersionUID = 9216878570227670869L;
    
    private static final Logger logger = Logger.getLogger(ChequeDishonorModel.class);

    @Autowired
    private ChequeDishonorService chequeDishonorService;
    
    @Autowired
	private IOrganisationService iOrganisationService;
    
    @Autowired
   	private TbTaxMasService tbTaxMasService;
    
    @Autowired
	private IReceiptEntryService iReceiptEntryService;
    
    @Autowired
	private ICommonReversalEntry iCommonReversalEntry;


    @Resource
    private DepartmentService departmentService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private  ReceiptRepository receiptRepository;
    
    
    private List<TbDepartment> department = null;

    private List<TbServiceReceiptMasBean> feeDetail = null;

    private Long deptId;

    private Date fromDate;

    private Date toDate;

    private Long receiptNo;

    private String chequeNo;
    
    private double dishonorCharge;

    private Long bankId;

    private String accountActive;

    public String getAccountActive() {
        return accountActive;
    }

    public void setAccountActive(final String accountActive) {
        this.accountActive = accountActive;
    }

    private Map<Long, String> banks = new HashMap<>(0);

    public Map<Long, String> getBanks() {
        return banks;
    }

    public void setBanks(final Map<Long, String> banks) {
        this.banks = banks;
    }

    public List<TbDepartment> getDepartment() {
        return department;
    }

    public void setDepartment(final List<TbDepartment> department) {
        this.department = department;
    }

    public List<TbServiceReceiptMasBean> getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(final List<TbServiceReceiptMasBean> feeDetail) {
        this.feeDetail = feeDetail;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(final Long deptId) {
        this.deptId = deptId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Long getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(final Long receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(final String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String constantNo = MainetConstants.Common_Constant.NO;
    public String constantYes = MainetConstants.Common_Constant.YES;

    /**
     * @param model
     * @return
     */
    public boolean validateSearchData(final ChequeDishonorModel model) {
        boolean result = true;
        if ((model.getDeptId() == null) | (model.getDeptId() <= 0)) {
            addValidationError(ApplicationSession.getInstance().getMessage("adjustment.department.select"));
        }
        if ((model.getFromDate() != null) || (model.getToDate() != null)) {
            if ((model.getFromDate() == null) || (model.getToDate() == null)) {
                addValidationError(ApplicationSession.getInstance().getMessage("cheque.date.select"));
            }
        }
        if (hasValidationErrors()) {
            result = false;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.web.core.ui.model.AbstractFormModel#saveForm()
     */
    @Override
    public boolean saveForm() {
        final String deptCode = departmentService.getDeptCode(deptId);
        int count = 0;
        for (final TbServiceReceiptMasBean feedetailDto : getFeeDetail()) {
        	TbSrcptModesDetBean modeDto = feedetailDto.getReceiptModeList().get(0);
        	if(constantYes.equals(modeDto.getRdSrChkDis()) && checkLatestBillExist(deptCode, feedetailDto)) {
				addValidationError(ApplicationSession.getInstance().getMessage("Property.commonrev.CurrentBillExists"));
				return false;
			}
			for (final TbSrcptModesDetBean modeDetails : feedetailDto.getReceiptModeList()) {
				if (constantYes.equals(modeDetails.getRdSrChkDis())) {
					if (modeDetails.getRdSrChkDate() == null || StringUtils.isEmpty(modeDetails.getRd_dishonor_remark())) {
						addValidationError(ApplicationSession.getInstance().getMessage("cheque.dishonor.date.charge.rod.enter")
								+ modeDetails.getRdChequeddno());						
					}
					if(modeDetails.getRdSrChkDisChg() == null) {
						addValidationError(ApplicationSession.getInstance().getMessage("cheque.dishonor.charge.not.found")
								+ modeDetails.getRdChequeddno());
					}
					++count;
				} else if (constantNo.equals(modeDetails.getRdSrChkDis())) {
					if (modeDetails.getRdSrChkDate() == null) {
						addValidationError(ApplicationSession.getInstance().getMessage("cheque.doc.enter")
								+ modeDetails.getRdChequeddno());
					}
					++count;
				}
				modeDetails.setAddRefNo(feedetailDto.getAdditionalRefNo());
			}
			
		}
        if (count <= 0) {
            addValidationError(ApplicationSession.getInstance().getMessage("cheque.dishonour.select.one"));
            return false;
        }
        else if(hasValidationErrors()){
        	 return false;
        	
        }else {
        	for (final TbServiceReceiptMasBean feedetailDto : getFeeDetail()) {
			for (final TbSrcptModesDetBean modeDetails : feedetailDto.getReceiptModeList()) {
				if (constantYes.equals(modeDetails.getRdSrChkDis())) {
					if ((feedetailDto.getRmLoiNo() != null) && !feedetailDto.getRmLoiNo().isEmpty()) {
						chequeDishonorService.updateLoiNotPaid(feedetailDto.getRmLoiNo(), modeDetails.getOrgid());
					}
					List<TbSrcptFeesDetBean> recDet = feedetailDto.getReceiptFeeDetail();
					if(Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), MainetConstants.ENV_SKDCL)) {
						for(TbSrcptFeesDetBean det :  recDet) {
							TbTaxMas taxMas = tbTaxMasService.findById(det.getTaxId(), UserSession.getCurrent().getOrganisation().getOrgid());
							 if(PrefixConstants.NewWaterServiceConstants.PENALTY.equals(taxMas.getTaxDesc()))  {
								 modeDetails.getPenaltyWiseBillNoAndAmountMap().put(det.getBmIdNo(), det.getRfFeeamount().doubleValue());
							 }else if(!MainetConstants.TAX_TYPE.ADVANCE_PAYMENT.equals(taxMas.getTaxDesc())) {
								 if(modeDetails.getBillNoAndAmountMap().get(det.getBmIdNo()) != null) {
									 Double existingVal = modeDetails.getBillNoAndAmountMap().get(det.getBmIdNo());
									 modeDetails.getBillNoAndAmountMap().put(det.getBmIdNo(), 
											 (existingVal + det.getRfFeeamount().doubleValue()));
								 }else {
									 modeDetails.getBillNoAndAmountMap().put(det.getBmIdNo(), det.getRfFeeamount().doubleValue());
								 }
							 }
						}
						
					}
						
					
							BillPaymentService dynamicServiceInstance = null;
							String serviceClassName = null;

							Long empId = UserSession.getCurrent().getEmployee().getEmpId();
							String ipAddress = UserSession.getCurrent().getEmployee().getEmppiservername();

							long orgid = UserSession.getCurrent().getOrganisation().getOrgid();

							deptId = feedetailDto.getDpDeptId();
							
							serviceClassName = messageSource.getMessage(MainetConstants.CHALLAN_BILL + deptCode,
									new Object[] {}, StringUtils.EMPTY, Locale.ENGLISH);
							dynamicServiceInstance = ApplicationContextProvider.getApplicationContext()
									.getBean(serviceClassName, BillPaymentService.class);

							VoucherPostDTO receiptReversalDTO = null;
							feedetailDto.setRmV1("Y");
							if (MainetConstants.WATER_DEPT.equals(departmentService.getDeptCode(deptId))) {
								receiptReversalDTO = dynamicServiceInstance.reverseBillAndPenalty(feedetailDto, orgid,
										empId, ipAddress);
							} else {
								receiptReversalDTO = dynamicServiceInstance.reverseBill(feedetailDto, orgid, empId,
										ipAddress);
							}

							List<TbServiceReceiptMasBean> ReceiptMasBeandtoList = new ArrayList<TbServiceReceiptMasBean>();
							List<TbServiceReceiptMasBean> rebateReceiptList = iReceiptEntryService
									.findReceiptByReceiptDateType(feedetailDto.getRmRcptid(), feedetailDto.getOrgId(),
											feedetailDto.getRmDate(), feedetailDto.getDpDeptId(), "RB");
							List<TbServiceReceiptMasBean> abhayYojanaReceiptList = iReceiptEntryService
									.findReceiptByReceiptDateType(feedetailDto.getRmRcptid(), orgid,
											feedetailDto.getRmDate(), feedetailDto.getDpDeptId(), "ABY");
							ReceiptMasBeandtoList.add(feedetailDto);
							ReceiptMasBeandtoList.addAll(rebateReceiptList);
							ReceiptMasBeandtoList.addAll(abhayYojanaReceiptList);
							ReceiptMasBeandtoList.forEach(ReceiptMasBeandto -> {
								ReceiptMasBeandto.setReceiptDelRemark("Receipt Reversed by cheque dishonour Cheque No.");
								ReceiptMasBeandto.setLgIpMacUpd(getClientIpAddress());
								iCommonReversalEntry.updateReceipt(ReceiptMasBeandto, feedetailDto.getOrgId(),
										UserSession.getCurrent().getEmployee().getEmpId());
							});
							String sliActiveFlag = "N";
							final LookUp accountActiveLookUp = CommonMasterUtility.getValueFromPrefixLookUp(
									MainetConstants.FlagL,
									MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE,
									UserSession.getCurrent().getOrganisation());
							if (accountActiveLookUp != null) {
								sliActiveFlag = accountActiveLookUp.getDefaultVal();
							}
							if (StringUtils.equals(sliActiveFlag, "Y")) {
								doVochourPosting(receiptReversalDTO, feedetailDto);
							}

						} else if (constantNo.equals(modeDetails.getRdSrChkDis())) {					
					final List<TbSrcptModesDetEntity> feeDetList = chequeDishonorService
							.fetchReceiptFeeDetails(modeDetails.getModeKeyList(), modeDetails.getOrgid());
						if (feeDetList != null && !feeDetList.isEmpty()) {
							for (TbSrcptModesDetEntity det : feeDetList) {
								det.setRdSrChkDate(modeDetails.getRdSrChkDate());
								det.setRdSrChkDis(modeDetails.getRdSrChkDis());
								det.setUpdatedBy((UserSession.getCurrent().getEmployee().getEmpId()!=null)?UserSession.getCurrent().getEmployee().getEmpId():modeDetails.getCreatedBy());
								det.setUpdatedDate(new Date());
								// Defect #119702
								LookUp CLRPrefix = CommonMasterUtility.getValueFromPrefixLookUp(
										MainetConstants.Property.CLEARED, MainetConstants.Property.CLR,
										UserSession.getCurrent().getOrganisation());
								if (CLRPrefix != null)
									det.setCheckStatus(CLRPrefix.getLookUpId());
							}
							chequeDishonorService.saveReceiptModeDetails(feeDetList);
						}
					
				}
				
			}
			saveUserActivity(UserSession.getCurrent(), feedetailDto);
		}
        setSuccessMessage(ApplicationSession.getInstance().getMessage("cheque.reconciliation.success.msg"));
        return true;}
    }
    
    private void saveUserActivity(UserSession session, TbServiceReceiptMasBean bean) {
		UserActivityDTO dto = new UserActivityDTO();
		dto.setApplicationId(null);
		dto.setCreatedBy(session.getEmployee().getEmpId());
		dto.setCreatedDate(new Date());
		dto.setLgIpMac(session.getEmployee().getEmppiservername());
		dto.setOrgId(session.getOrganisation().getOrgid());
		dto.setServiceId(bean.getSmServiceId());
		dto.setReferenceNo(String.valueOf(bean.getRmRcptid()));			
		dto.setAction("Cheque Dishonored Entry");
		ApplicationContextProvider.getApplicationContext().getBean(UserActivityService.class).saveUserActivity(dto);
	}
    
    private boolean checkLatestBillExist(String deptCode, TbServiceReceiptMasBean receiptMasBean) {
		boolean flag = false;
		if (Utility.isEnvPrefixAvailable(UserSession.getCurrent().getOrganisation(), "SKDCL")
				&& (org.apache.commons.lang.StringUtils.equals(MainetConstants.WATER_DEPT, deptCode)
						|| org.apache.commons.lang.StringUtils.equals(MainetConstants.Property.PROP_DEPT_SHORT_CODE,
								deptCode))) {
			BillPaymentService dynamicServiceInstance = null;
            String serviceClassName = null;
            serviceClassName =messageSource.getMessage(
                    MainetConstants.CHALLAN_BILL + deptCode, new Object[] {},
                    StringUtils.EMPTY, Locale.ENGLISH);
            dynamicServiceInstance = ApplicationContextProvider.getApplicationContext().getBean(serviceClassName,
                    BillPaymentService.class);
            
			TbBillMas latestYearBill = dynamicServiceInstance.getLatestYearBill(receiptMasBean.getAdditionalRefNo(), UserSession.getCurrent().getOrganisation().getOrgid(),receiptMasBean.getFlatNo());
			if(latestYearBill != null) {
				// receiptMasBean.getRmDate().after(latestYearBill.getBmBilldt())
				// rmdate is replacing with created date because , when bill and payment done on the same day, not comparing properly. In rmdate timestamp is not maintained.
				
				List<TbServiceReceiptMasEntity> getbillPaidBetweenCurFinYear = null;
	        	if (StringUtils.isBlank(receiptMasBean.getFlatNo())) {
	        		getbillPaidBetweenCurFinYear = receiptRepository.getbillPaidBetweenCurFinYear(receiptMasBean.getAdditionalRefNo(), latestYearBill.getBmFromdt(), latestYearBill.getBmTodt(), UserSession.getCurrent().getOrganisation().getOrgid());
	        	}else {
	        		getbillPaidBetweenCurFinYear = receiptRepository.getbillPaidBetweenCurFinYearByFlatNo(receiptMasBean.getAdditionalRefNo(), latestYearBill.getBmFromdt(), latestYearBill.getBmTodt(), UserSession.getCurrent().getOrganisation().getOrgid(), receiptMasBean.getFlatNo());
	        	}
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        	String billDate = null;
	        	String receiptDate = null;

	        	receiptDate = sdf.format(receiptMasBean.getRmDate());
				billDate = sdf.format(latestYearBill.getBmBilldt());

				if(isSecondDateBeforeFirst(receiptDate, billDate)) {
				    if (CollectionUtils.isNotEmpty(getbillPaidBetweenCurFinYear)) {
				        flag = true;
				    }
				} else {
				    logger.info("Receipt date is before bill date: returning false");
				}
			}
		}
		
		return flag;
	}

    public static boolean isSecondDateBeforeFirst(String firstDateStr, String secondDateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date firstDate = sdf.parse(firstDateStr);
			Date secondDate = sdf.parse(secondDateStr);

			if(secondDate.equals(firstDate)) {
				return false;
			}
			if (secondDate.before(firstDate)) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			System.out.println("Invalid date format.");
			return false;
		}
	}

    private void doVochourPosting(VoucherPostDTO receiptReversalDTO,TbServiceReceiptMasBean ReceiptMasBean) {
    	List<VoucherPostDetailDTO> voucherDetails=null;
    	if(receiptReversalDTO!=null) {
    	 voucherDetails = receiptReversalDTO.getVoucherDetails();
    	}
    	
    	if(voucherDetails != null)
        {
        	
        	
        	
        	
          VoucherPostDTO voucherPostDTO =new VoucherPostDTO();
        	
          Organisation org = UserSession.getCurrent().getOrganisation();
        	
          LookUp PrefixLookUp = CommonMasterUtility.getValueFromPrefixLookUp(
                    MainetConstants.BILL_MASTER_COMMON.TAX_BILL_RECEIPT_REVERSAL,
                    MainetConstants.BILL_MASTER_COMMON.ACCOUNT_TEMPLATE_FOR_PREFIX, org);
        	
        	
        	voucherPostDTO.setVoucherDate(new Date());
        	
        	voucherPostDTO.setVoucherType(null);
        	
        	voucherPostDTO.setVoucherSubTypeId(PrefixLookUp.getLookUpId());
        	
        	voucherPostDTO.setDepartmentId(deptId);
        	
        	voucherPostDTO.setVoucherReferenceNo(String.valueOf(ReceiptMasBean.getRmRcptno()));
        	
        	
        	voucherPostDTO.setVoucherReferenceDate(ReceiptMasBean.getRmDate());
        	
        	voucherPostDTO.setNarration(ReceiptMasBean.getReceiptDelRemark());
        	
        	
        	voucherPostDTO.setPayerOrPayee(ReceiptMasBean.getRmReceivedfrom());
        	
        	voucherPostDTO.setFieldId(ReceiptMasBean.getTbAcFieldMaster().getFieldId());
        	
        	voucherPostDTO.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
        	
        	voucherPostDTO.setCreatedBy(ReceiptMasBean.getCreatedBy());
        	
        	voucherPostDTO.setCreatedDate(new Date());
        	
        	
        	voucherPostDTO.setLangId(ReceiptMasBean.getLangId());
        	
        	voucherPostDTO.setLgIpMac(ReceiptMasBean.getLgIpMac());
        	
        	voucherPostDTO.setEntryType(MainetConstants.BILL_MASTER_COMMON.ENTRY_TYPE);
        	
        	voucherPostDTO.setBillVouPostingDate(null);
        	
        	voucherPostDTO.setPayEntryMakerFlag(null);
        	
        	voucherPostDTO.setAuthFlag(null);
        	
        	voucherPostDTO.setTemplateType(null);
        	
        	voucherPostDTO.setFinancialYearId(null);
        	
        	voucherPostDTO.setPayModeId(ReceiptMasBean.getModeId());
        	
        	voucherPostDTO.setEntryFlag(null);
        	
        	List<VoucherPostDetailDTO> voucherPostDetails = new ArrayList<>();
        	
        	
        	for (VoucherPostDetailDTO voucherPostDetail : voucherDetails) 
        	{
				
        		
        		VoucherPostDetailDTO VoucherPostDetaildto=new VoucherPostDetailDTO();
        		
        		VoucherPostDetaildto.setVoucherAmount(voucherPostDetail.getVoucherAmount());
        		
        		VoucherPostDetaildto.setSacHeadId(voucherPostDetail.getSacHeadId());
        		
        		VoucherPostDetaildto.setYearId(voucherPostDetail.getYearId());
        		
        		VoucherPostDetaildto.setDemandTypeId(voucherPostDetail.getDemandTypeId());
        		
        		VoucherPostDetaildto.setDrCrId(null);
        		
        		VoucherPostDetaildto.setPayModeId(null);
        		
        		VoucherPostDetaildto.setAccountHeadFlag(null);
        		
        		voucherPostDetails.add(VoucherPostDetaildto);
        		
			}
        	
        	
        	 voucherPostDTO.setVoucherDetails(voucherPostDetails);
        	
        	 List<VoucherPostDTO> accountPostingList = new ArrayList<>();
        	 
        	 
        	 String activeFlag = null;
             final LookUp accountActive = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.FlagL,
                     MainetConstants.BUG_HEAD_OPENING_BALANCE_MASTER.SLI_PREFIX_VALUE, org);
             if (accountActive != null) {
                 activeFlag = accountActive.getDefaultVal();
             }
        	 
        	try {
        		iReceiptEntryService.doAccountVoucherPosting(ReceiptMasBean.getReceiptDelRemark(), accountPostingList, voucherPostDTO, activeFlag);
        	}catch (Exception e) {
        		logger.error("Exception occured while receipt reversal account posting" + e);
			}
        	
        }
    }
    public Long getBankId() {
        return bankId;
    }

    public void setBankId(final Long bankId) {
        this.bankId = bankId;
    }

	public double getDishonorCharge() {
		return dishonorCharge;
	}

	public void setDishonorCharge(double dishonorCharge) {
		this.dishonorCharge = dishonorCharge;
	}

}
