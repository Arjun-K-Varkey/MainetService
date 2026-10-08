package com.abm.mainet.cfc.loi.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.cfc.loi.domain.TbLoiDetEntity;
import com.abm.mainet.cfc.loi.domain.TbLoiMasEntity;
import com.abm.mainet.cfc.loi.dto.LoiPaymentSearchDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.dao.IOrganisationDAO;
import com.abm.mainet.common.dashboard.domain.skdcl.LicenseDataEntity;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.master.dto.TbLocationMas;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;

/**
 * @author Rahul.Yadav
 *
 */
@Repository
public class LoiMasterPaymentDAO extends AbstractDAO<TbLoiMasEntity> implements ILoiMasterPaymentDAO {

	private static final Logger LOGGER = Logger.getLogger(LoiMasterPaymentDAO.class);

	@Autowired
	private IOrganisationDAO organisationDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.loi.repository.ILoiMasterPaymentDAO#
	 * findLoiMasBySearchDTO(com.abm.mainetservice.web.loi.bean.
	 * LoiPaymentSearchDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TbLoiMasEntity findLoiMasBySearchDTO(final LoiPaymentSearchDTO searchDto, final String status) {

		Organisation org = organisationDAO.getOrganisationById(searchDto.getOrgId(), MainetConstants.STATUS.ACTIVE);

		boolean moalFlag = false;

		StringBuilder nativeQuery = new StringBuilder();
		nativeQuery.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select distinct LM.* from TB_LOI_MAS LM INNER JOIN "
				+ "TB_SERVICES_MST SM ON  LM.SM_SERVICE_ID=SM.SM_SERVICE_ID INNER JOIN tb_cfc_application_address CFCA ON CFCA.APM_APPLICATION_ID=LM.APM_APPLICATION_ID where LM.LOI_PAID='N' and"
				+ " LM.LOI_STATUS='A'\r\n");

		LookUp multiOrgDataList = null;
		try {
			LOGGER.error("Fetching MOAL prefix");
			multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL, MainetConstants.ENV,
					org);
			LOGGER.error("MOAL prefix is available");
		} catch (Exception e) {
			LOGGER.error("No Prefix found for ENV(MOAL)");
		}

		/*
		 * if (!(multiOrgDataList != null &&
		 * StringUtils.isNotBlank(multiOrgDataList.getOtherField()) &&
		 * StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY)))
		 * { nativeQuery.append("and LM.TbLoiMasEntityKey.ORGID = '" +
		 * searchDto.getOrgId() + "'"); moalFlag = true;
		 * LOGGER.error("Appending org id in query: " + searchDto.getOrgId()); }
		 */

		if (searchDto.getApplicationId() != null) {
			nativeQuery.append("AND LM.APM_APPLICATION_ID = '" + searchDto.getApplicationId() + "'");
		}

		if ((searchDto.getLoiNo() != null) && !searchDto.getLoiNo().isEmpty()) {
			nativeQuery.append("AND LM.LOI_NO = '" + searchDto.getLoiNo() + "'");
		}
		/*if (searchDto.getEmpId() != null) {
			nativeQuery.append("AND CFCA.USER_ID = '" + searchDto.getEmpId() + "'");
		
		}
		if (searchDto.getMobileNo() != null) {
			nativeQuery.append("OR CFCA.APA_MOBILNO = '" + searchDto.getMobileNo() + "'");
		}*/
		if (searchDto.getEmpId() != null || StringUtils.isNotBlank(searchDto.getMobileNo())) {

			nativeQuery.append("AND (");

			boolean conditionAdded = false;

			if (searchDto.getEmpId() != null) {
				nativeQuery.append("CFCA.USER_ID = '").append(searchDto.getEmpId()).append("' ");
				conditionAdded = true;
			}

			if (StringUtils.isNotBlank(searchDto.getMobileNo())) {
				if (conditionAdded) {
					nativeQuery.append("OR ");
				}
				nativeQuery.append("CFCA.APA_MOBILNO = '").append(searchDto.getMobileNo()).append("' ");
			}

			nativeQuery.append(") ");
		}

		nativeQuery.append(") as t, (SELECT @row_number\\:=0) AS rn");

		List<TbLoiMasEntity> resultList = (List<TbLoiMasEntity>) entityManager
				.createNativeQuery(nativeQuery.toString(), TbLoiMasEntity.class).getResultList();

		if (CollectionUtils.isNotEmpty(resultList)) {
			return resultList.get(0);
		} else {
			return null;
		}
	}
   

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.loi.repository.ILoiMasterPaymentDAO#
	 * findLoiMasBySearchDTO(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void updateLoiMaster(final Long loiId, final Long orgid) {
		final Query query = entityManager.createQuery(
				"update  TbLoiMasEntity l set l.loiPaid='Y' where l.compositePrimaryKey.loiId=?1 and l.compositePrimaryKey.orgid=?2");
		query.setParameter(1, loiId);
		query.setParameter(2, orgid);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.loi.repository.ILoiMasterPaymentDAO#
	 * updateLoiPaidByLoiNo(java.lang.String, long)
	 */
	@Override
	public void updateLoiPaidByLoiNo(final String loiNo, final long orgid) {
		final Query query = entityManager.createQuery(
				"update  TbLoiMasEntity l set l.loiPaid='Y' where l.loiNo=?1 and l.compositePrimaryKey.orgid=?2");
		query.setParameter(1, loiNo);
		query.setParameter(2, orgid);
		query.executeUpdate();
	}
	
	@Override
	public void rollbackLoiPaidByLoiNo(final String loiNo, final long orgid) {
		final Query query = entityManager.createQuery(
				"update  TbLoiMasEntity l set l.loiPaid='N' where l.loiNo=?1 and l.compositePrimaryKey.orgid=?2");
		query.setParameter(1, loiNo);
		query.setParameter(2, orgid);
		query.executeUpdate();
	}

	@Override
	public void updateLoiMasterStatus(final String loiNo, final long orgId, final Long empId,
			final String deleteRemark) {
		final Query query = entityManager.createQuery("update  TbLoiMasEntity l set l.loiStatus='I',l.loiDelRemark=?5"
				+ ",l.updatedBy=?3,l.updatedDate=?4 where l.loiNo=?1 and l.compositePrimaryKey.orgid=?2 ");
		query.setParameter(1, loiNo);
		query.setParameter(2, orgId);
		query.setParameter(3, empId);
		query.setParameter(4, new Date());
		query.setParameter(5, deleteRemark);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findLoiInformation(Long orgId, String mobileNo, Long empId) {
		List<Object[]> entityList = new ArrayList<>();
		Organisation org = organisationDAO.getOrganisationById(orgId, MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		try {
			final StringBuilder queryString = new StringBuilder();
			queryString.append(
					"select LM.APM_APPLICATION_ID as col_0_0_, SM.SM_SERVICE_NAME as col_1_0_, SM.SM_SERVICE_NAME_MAR as col_2_0_, SM.SM_SHORTDESC as col_3_0_, LM.LOI_NO as col_4_0_, LM.LOI_REF_ID as col_5_0_, LM.LOI_AMOUNT as col_6_0_, LM.LOI_PAID as col_7_0_, LM.SM_SERVICE_ID as col_8_0_, LM.LOI_DATE as col_9_0_, CFCM.ORGID as col_10_0_ from TB_LOI_MAS LM INNER JOIN TB_SERVICES_MST SM ON LM.SM_SERVICE_ID=SM.SM_SERVICE_ID INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=LM.APM_APPLICATION_ID INNER JOIN tb_cfc_application_mst CFCM ON CFCM.APM_APPLICATION_ID=LM.APM_APPLICATION_ID INNER JOIN tb_cfc_application_address CFCA ON CFCA.APM_APPLICATION_ID=LM.APM_APPLICATION_ID WHERE  LM.LOI_PAID='N' and LM.LOI_STATUS='A' and (WR.CREATED_BY=:CREATED_BY or CFCA.APA_MOBILNO=:mobileNo) ");
			LookUp multiOrgDataList = null;
			try {
				LOGGER.error("Fetching MOAL prefix");
				multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL,
						MainetConstants.ENV, org);
				LOGGER.error("MOAL prefix is available");
			} catch (Exception e) {
				LOGGER.error("No Prefix found for ENV(MOAL)");
			}

			if (!(multiOrgDataList != null && StringUtils.isNotBlank(multiOrgDataList.getOtherField())
					&& StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY))) {
				queryString.append("and CFCM.ORGID=:ORGID");
				moalFlag = true;
				LOGGER.error("Appending org id in query: " + orgId);
			}

			queryString.append(" order by CFCM.APM_APPLICATION_DATE DESC");
			LOGGER.error("Final Query: " + queryString.toString());
			final Query query = createNativeQuery(queryString.toString());
			query.setParameter("CREATED_BY", empId);
			if (moalFlag == true) {
				LOGGER.error("Setting orgId parameter in  Query setParameter field: " + queryString.toString());
				query.setParameter("ORGID", orgId);
			}
			query.setParameter("mobileNo", mobileNo);

			entityList = (List<Object[]>) query.getResultList();

		} catch (final Exception exception) {
			LOGGER.error("Exception occur in  getFilteredNewTradeLicenceList() ", exception);

		}
		return entityList;

	}

	@Override
	public List<TbLoiDetEntity> findLoiDetailsByLoiMasAndOrgId(Long loiId, Long orgId) {
		Organisation org = organisationDAO.getOrganisationById(orgId, MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		List<TbLoiDetEntity> entityList = new ArrayList<>();
		final StringBuilder queryString = new StringBuilder();
		//Defect #135030 change request dto orgid to master orgid
		queryString.append(
				"select l from TbLoiDetEntity l where l.loiMasId=:loiId and l.compositePrimaryKey.orgid=:orgId");

		final Query query = createQuery(queryString.toString());

		query.setParameter("loiId", loiId);
		query.setParameter("orgId", orgId);
		entityList = query.getResultList();
		return entityList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLoiMasData(Long orgid, Long deptId, Long serviceId, Long appId, String loiNo,String apmFname, String apmLname) {
		List<Object[]> entityList = new ArrayList<>();
		final StringBuilder queryString = new StringBuilder();
		try {
		queryString.append(
				"select l.compositePrimaryKey.loiId,l.loiNo,l.loiDate,l.loiServiceId,l.loiApplicationId,l.loiRefId,l.loiAmount,b.smServiceName,b.smServiceNameMar,c.apmFname,c.apmMname,c.apmLname,c.apmApplicationDate from TbLoiMasEntity l, ServiceMaster b,  TbCfcApplicationMstEntity c where l.compositePrimaryKey.orgid=:orgid  and b.smServiceId=l.loiServiceId  and c.apmApplicationId=l.loiApplicationId and l.loiPaid='N' ");

		if (deptId != null && deptId != 0) {
			queryString.append(" and b.tbDepartment.dpDeptid=:deptId");
		} 

		if (serviceId != null && serviceId != 0) {
			queryString.append(" and b.smServiceId=:serviceId");
		}

		if (appId != null && appId != 0) {
			queryString.append(" and l.loiApplicationId=:appId");
		}
		
		if ((loiNo != null) && !loiNo.isEmpty() && !loiNo.equals("0")) {
			queryString.append(" and l.loiNo=:loiNo");
		}
		if (StringUtils.isNotEmpty(apmFname)) {
			queryString.append(" and c.apmFname like '%" + apmFname + "%'");
		}
	
		if (StringUtils.isNotEmpty(apmLname)) {
			queryString.append(" and c.apmLname like '%" + apmLname + "%'");
		}
		queryString.append(" order by l.loiApplicationId desc");
		
		Query query = this.createQuery(queryString.toString());
		query.setParameter("orgid", orgid);

		if (deptId != null && deptId != 0) {
			query.setParameter("deptId", deptId);
		}
		if (serviceId != null && serviceId != 0) {
			query.setParameter("serviceId", serviceId);
		}
		if (appId != null && appId != 0) {
			query.setParameter("appId", appId);
		}
		if ((loiNo != null) && !loiNo.isEmpty() && !loiNo.equals("0")) {
			query.setParameter("loiNo", loiNo);
		}
	
		entityList = (List<Object[]>) query.getResultList();
		}
		catch (Exception e) {
			LOGGER.error("Exception occur while fetchig LOI Details in  getLoiMasData() ", e);

		}
		return entityList;
	}

     //#139419
	 @Override
	 @Transactional
	 public void updateIssuanceDataInLoiMas(Long applicationId, Long issuedBy, Long orgId) {
        final Query query = createQuery("UPDATE TbLoiMasEntity c SET  c.issuedDate=CURRENT_TIMESTAMP, c.issuedBy=?1  where c.loiApplicationId=?2 and c.compositePrimaryKey.orgid=?3");
        query.setParameter(1, issuedBy);
        query.setParameter(2, applicationId);
        query.setParameter(3, orgId);
        query.executeUpdate();
	}
   
}
