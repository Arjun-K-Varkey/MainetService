package com.abm.mainet.common.master.ui.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.master.dao.LandRateMasterDao;
import com.abm.mainet.common.master.dto.LandRateMaterDTO;
import com.abm.mainet.common.master.repository.LandRateMasterRepository;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.utility.Utility;
import com.abm.mainet.common.workflow.domain.TbAreaLandRateEntity;

@Component
@Scope("session")
public class LandRateMasterModel extends AbstractFormModel {

	private static final long serialVersionUID = -12506026553212504L;
	private LandRateMaterDTO rateMaterDTO = new LandRateMaterDTO();
	private List<LandRateMaterDTO> lndRateList = new ArrayList<LandRateMaterDTO>();
	private String btnShow;

	@Autowired
	private LandRateMasterRepository landRateMasterRepository;

	@Autowired
	private LandRateMasterDao landRateMasterDao;

	public List<LandRateMaterDTO> getLndRateList() {
		return lndRateList;
	}

	public void setLndRateList(List<LandRateMaterDTO> lndRateList) {
		this.lndRateList = lndRateList;
	}

	public LandRateMaterDTO getRateMaterDTO() {
		return rateMaterDTO;
	}

	public void setRateMaterDTO(LandRateMaterDTO rateMaterDTO) {
		this.rateMaterDTO = rateMaterDTO;
	}

	@Override
	public boolean saveForm() throws FrameworkException {
		List<LandRateMaterDTO> masterList = getLndRateList();
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
						entity.setArea(this.getRateMaterDTO().getArea());
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
		this.setSuccessMessage("Land Rate Master Data  Successfully Saved ");
		return true;
	}

	public List<Object[]> getLandRateDetailsList(String area, String surveyNo) {
		return landRateMasterDao.fetchLandRateMasterData(area, surveyNo);
	}

	public String getBtnShow() {
		return btnShow;
	}

	public void setBtnShow(String btnShow) {
		this.btnShow = btnShow;
	}
}
