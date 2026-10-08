package com.abm.mainet.common.service;

import java.util.List;

import com.abm.mainet.common.domain.CFCApplicationAddressEntity;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.dto.ApplicationStatusDTO;
import com.abm.mainet.common.dto.CFCApplicationStatusDto;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;

/**
 * @author Rajendra.Bhujbal
 *
 */
public interface ICFCApplicationMasterService {

    long getServiceIdByApplicationId(long apmApplicationId, long orgId);

    public CFCApplicationAddressEntity getApplicantsDetails(long apmApplicationId);

    TbCfcApplicationMstEntity getCFCApplicationByApplicationId(long applicationId, long orgid);

    public void updateCFCApplicationStatus(String status, long applicationId);

    TbCfcApplicationMstEntity getCFCApplicationByRefNoOrAppNo(String refNumber, Long applicationId, Long orgid);

    List<TbCfcApplicationMstEntity> fetchCfcApplicationByServiceIdandMobileNo(List<Long> serviceIds, Long orgId, String mobileNo);

    List<TbCfcApplicationMstEntity> fetchCfcApplicationsByServiceIds(List<Long> serviceIds, Long orgId);

    List<ApplicationStatusDTO> getApplicationStatusDtos(CFCApplicationStatusDto cfcApplicationStatusDto,int LangId);

	boolean isDataExist(CFCApplicationStatusDto cfcApplicationStatusDto);

	TbCfcApplicationMstEntity getCFCApplicationOnlyByApplicationId(long applicationId);

	List<TbCfcApplicationMstEntity> getCFCApplData(Long orgid);
	
	public CustomerInfoDTO getReferenceNoForPayment(Long applicationId, Long orgId);

	TbCfcApplicationMstEntity saveApplicationData(TbCfcApplicationMstEntity entity);
	
	boolean updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(long applicationId,long transactionId,long orgId);
	
	void updateCFCApplicationCheklistVerificationFlag(long applicationNo, long orgid, String status);

	int existsByApplicationId(Long applicationId);
}
