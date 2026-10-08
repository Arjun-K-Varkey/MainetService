package com.abm.mainet.common.master.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.master.dto.LandRateMaterDTO;
@Repository
public class LandRateMasterDaoImpl extends AbstractDAO<Long> implements LandRateMasterDao {

	@Override
	public List<Object[]> fetchLandRateMasterData(String area, String surveyNo) {
		List<Object[]> entityList = new ArrayList<>();
		try {
			StringBuilder hql = new StringBuilder(
					"select a.area,a.surveyType,a.surveyNo,a.lmFromDate,a.lmToDate,a.amt,a.tbAreaLandId,a.orgId,a.createdBy,a.createdDate,a.lgIpMac from TbAreaLandRateEntity a  where  a.area =:areaName ");
			if (StringUtils.isNotBlank(surveyNo)) {
				hql.append("and  a.surveyNo like :surveyNo");
			}
			final Query query = createQuery(hql.toString());
			query.setParameter("areaName", area);
			if (StringUtils.isNotBlank(surveyNo)) {
				query.setParameter("surveyNo", surveyNo);
			}
			entityList = (List<Object[]>) query.getResultList();
			return entityList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
}
