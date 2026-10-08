package com.abm.mainet.common.master.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abm.mainet.common.master.dao.LandRateMasterDao;
import com.abm.mainet.common.master.dto.LandRateMasterBean;
import com.abm.mainet.common.master.dto.LandRateMaterDTO;
import com.abm.mainet.common.master.repository.LandRateMasterRepository;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.domain.TbAreaLandRateEntity;
@Service
public class LandRateMasterServiceImpl implements LandRateMasterService {
	@Autowired
	private LandRateMasterRepository landRateMasterRepository;

	@Autowired
	private LandRateMasterDao landRateMasterDao;
	@Override
	public List<Object[]> getLandRateDetailsList(String area, String surveyNo) {
		return landRateMasterDao.fetchLandRateMasterData(area, surveyNo);
	}
	@Override
	public boolean saveForm(LandRateMasterBean bean) {
		List<LandRateMaterDTO> masterList = bean.getLndRateList();
		List<TbAreaLandRateEntity> entList = new ArrayList<>();
		try {
			for (LandRateMaterDTO dto : masterList) {
				if (StringUtils.equals(dto.getIsUpdate(), "1")) {
					TbAreaLandRateEntity entity = new TbAreaLandRateEntity();
					BeanUtils.copyProperties(entity, dto);
					entity.setLmToDate(null);
					if (StringUtils.isNotBlank(dto.getFromDate()))
						entity.setLmFromDate(Utility.stringToDate(dto.getFromDate()));
					if (StringUtils.isNotBlank(dto.getToDate())) {
						entity.setLmToDate(Utility.stringToDate(dto.getToDate()));
					}
					entity.setOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
					if (entity.getArea() == null) {
						entity.setArea(bean.getRateMaterDTO().getArea());
					}
					if (dto.getTbAreaLandId() == null) {
						entity.setCreatedBy(UserSession.getCurrent().getEmployee().getEmpId());
						entity.setCreatedDate(new Date());
						entity.setLgIpMac(UserSession.getCurrent().getEmployee().getEmppiservername());
					} else {
						entity.setUpdatedBy(UserSession.getCurrent().getEmployee().getEmpId());
						entity.setUpdatedDate(new Date());
						entity.setLgIpMacUpd(UserSession.getCurrent().getEmployee().getEmppiservername());
					}
					landRateMasterRepository.save(entity);
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return true;
	}
	
}
