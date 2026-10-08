package com.abm.mainet.common.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.integration.dto.ApplyForGoGreenDTO;
import com.abm.mainet.cfc.loi.repository.DishonurChargeEntryRepository;

@Service
@Transactional(readOnly = true)
public class GoGreenApplicationServiceImpl implements GoGreenApplicationService {

    @Autowired
    private DishonurChargeEntryRepository applyForGoGreenRepository;

    @Override
    public ApplyForGoGreenDTO getGoGreenApplicationDetails(final ApplyForGoGreenDTO dto) {

        Object[] rows = null;

        if (StringUtils.equalsIgnoreCase(dto.getDeptCode(), MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {

            if (StringUtils.isNotBlank(dto.getFlatNo())) {
                rows = applyForGoGreenRepository.getGoGreenPropertyDetails(
                        dto.getRefNo(), dto.getFlatNo(), dto.getOrgId());
            } else {
                rows = applyForGoGreenRepository.getGoGreenPropertyDetailsWithoutFlat(
                        dto.getRefNo(), dto.getOrgId());
            }

            if (rows != null) {
                Object[] objArr = (Object[]) rows[0];
                if (objArr[0] != null) dto.setOwnerName(objArr[0].toString());
                if (objArr[1] != null) dto.setOccupierName(objArr[1].toString());
                if (StringUtils.isBlank(dto.getFlatNo())) dto.setOccupierName(objArr[0].toString());
                if (objArr[2] != null) dto.setMobNo(objArr[2].toString());
                if (objArr[3] != null) dto.setEmailId(objArr[3].toString());
                if (objArr[4] != null) dto.setAddress(objArr[4].toString());
                if (objArr[5] != null) dto.setGoGreenFlag(objArr[5].toString());
                if (objArr[6] != null) dto.setAssId(objArr[6].toString());
                
            }

        } else if (StringUtils.equalsIgnoreCase(dto.getDeptCode(),
                MainetConstants.DEPT_SHORT_NAME.WATER)) {

            rows = applyForGoGreenRepository.getGoGreenWaterDetails(
                    dto.getRefNo(), dto.getOrgId());

            if (rows != null) {
                Object[] objArr = (Object[]) rows[0];
                if (objArr[0] != null) dto.setOwnerName(objArr[0].toString());
                if (objArr[1] != null) dto.setMobNo(objArr[1].toString());
                if (objArr[2] != null) dto.setEmailId(objArr[2].toString());
                if (objArr[3] != null) dto.setAddress(objArr[3].toString());
                if (objArr[4] != null) dto.setGoGreenFlag(objArr[4].toString());
                if (objArr[5] != null) dto.setAssId(objArr[5].toString());
            }
        }

        return dto;
    }

    @Override
    @Transactional
    public ApplyForGoGreenDTO updateGoGreenApplicationDetails(final ApplyForGoGreenDTO dto) {

        if (StringUtils.equalsIgnoreCase(dto.getDeptCode(), MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {

            if (StringUtils.isNotBlank(dto.getFlatNo())) {
            	// #270117 (data update in wrong table, changes done as per discussed with pramod sir
				/*
				 * applyForGoGreenRepository.updateGoGreenPropertyDetails( dto.getEmailId(),
				 * dto.getMobNo(), dto.getRefNo(), dto.getFlatNo(), dto.getOrgId());
				 */
                
                applyForGoGreenRepository.updateGoGreenIndividualPropertyDetails(
                        dto.getEmailId(), dto.getMobNo(), dto.getRefNo(),
                        dto.getFlatNo());
                
                applyForGoGreenRepository.updateGoGreenFlagForIndividualProperty(dto.getRefNo(), dto.getFlatNo(), dto.getOrgId());

            } else {
            	 // #270117 (data update in wrong table, changes done as per discussed with pramod sir
				/*
				 * applyForGoGreenRepository.updateGoGreenPropertyWithoutFlat( dto.getEmailId(),
				 * dto.getMobNo(), dto.getRefNo(), dto.getOrgId());
				 */
                
                applyForGoGreenRepository.updateGoGreenWholePropertyDetails(
                        dto.getEmailId(), dto.getMobNo(), dto.getRefNo());
                
                applyForGoGreenRepository.updateGoGreenFlagForWholeProp(dto.getRefNo(), dto.getOrgId());
            }

        } else if (StringUtils.equalsIgnoreCase(dto.getDeptCode(),
                MainetConstants.DEPT_SHORT_NAME.WATER)) {

            applyForGoGreenRepository.updateGoGreenWaterDetails(
                    dto.getEmailId(), dto.getMobNo(), dto.getRefNo(), dto.getOrgId());
        }

        return dto;
    }

	@Override
	@Transactional
	public boolean unsubscribePropGoGreenData(final ApplyForGoGreenDTO dto) {
		if (dto == null || StringUtils.isBlank(dto.getAssId())) {
			return false;
		}
		int rows = 0;
		if (StringUtils.equalsIgnoreCase(dto.getDeptCode(), MainetConstants.DEPT_SHORT_NAME.PROPERTY)) {

			rows = applyForGoGreenRepository.updatePropGoGreenFlag("N", dto.getAssId());
		} else if (StringUtils.equalsIgnoreCase(dto.getDeptCode(), MainetConstants.DEPT_SHORT_NAME.WATER)) {
			
			rows = applyForGoGreenRepository.updateWaterGoGreenFlag("N", dto.getAssId());

		}

		if (rows == 0) {
			throw new FrameworkException("No property updated for assId: " + dto.getAssId());
		}

		return true;
	}
}
