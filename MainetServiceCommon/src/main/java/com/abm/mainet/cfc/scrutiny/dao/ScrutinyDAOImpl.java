/**
 *
 */
package com.abm.mainet.cfc.scrutiny.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.master.service.TbScrutinyLabelsService;
import com.abm.mainet.common.utility.ApplicationContextProvider;
import com.abm.mainet.common.utility.Utility;

/**
 *
 * @author umashanker.kanaujiya
 *
 */
@Repository
public class ScrutinyDAOImpl extends AbstractDAO<Long> implements ScrutinyDAO {

	private static Logger LOG = Logger.getLogger(ScrutinyDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abm.mainetservice.web.cfc.scrutiny.dao.ScrutinyDAO#getDesignationName(
	 * java.lang.Long)
	 */
	@Override
	public GroupMaster getDesignationName(final Long gmId) {

		LOG.info("Start the getDesignationName");

		final StringBuilder hql = new StringBuilder("SELECT gm FROM GroupMaster gm  where gm.gmId = :gmId");
		final Query query = createQuery(hql.toString());
		query.setParameter("gmId", gmId);

		try {
			final GroupMaster entity = (GroupMaster) query.getSingleResult();
			return entity;
		} catch (final Exception exception) {
			LOG.error("Exception occur in getDesignationName() ", exception);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.dao.ScrutinyDAO#
	 * getAllScrutinyDocDesgWise(long, long)
	 */
	@Override
	public List<CFCAttachment> getAllScrutinyDocDesgWise(final long applId, final long orgId) {

		LOG.info("Start the getAllScrutinyDocDesgWise");

		final StringBuilder hql = new StringBuilder(
				"SELECT gm FROM CFCAttachment gm  where gm.applicationId = :apmApplicationId and gm.orgid =:orgId and gm.smScrutinyId is not null ");
		final Query query = createQuery(hql.toString());
		query.setParameter("apmApplicationId", applId);
		query.setParameter("orgId", orgId);

		try {
			@SuppressWarnings("unchecked")
			final List<CFCAttachment> entity = query.getResultList();
			return entity;
		} catch (final Exception exception) {
			LOG.error("Exception occur in getAllScrutinyDocDesgWise() ", exception);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abm.mainetservice.web.cfc.scrutiny.dao.ScrutinyDAO#
	 * getfindAllscrutinyLabelValueList(long, long, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getfindAllscrutinyLabelValueList(final Long orgId, final Long serviceId,
			final Long smScrutinyId, final Long applicationId, Long triCod1) {
		LOG.info("Start the getScrutinyUserByWardZoneBlock");
		// US#113590
		String temp = "";
		String shortCode =null;
		if (serviceId != null) {
			 shortCode = ApplicationContextProvider.getApplicationContext().getBean(TbScrutinyLabelsService.class)
					.getServiceCode(serviceId, orgId);
			 Organisation org = new Organisation();
			 org.setOrgid(orgId);
			if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
					&& (shortCode != null && shortCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE))) {
				temp = " and l.TRI_COD1=:tricod1 ";
			}
		}

		List<Object> list = null;
		final StringBuilder nativeQuery = new StringBuilder(
				"SELECT SM_SERVICE_ID, SL_LABEL, SL_AUTHORISING, SL_DATATYPE, SL_DISPLAY_FLAG,SL_FORM_MODE, SL_FORM_NAME,SL_LABEL_ID, SL_LABEL_MAR, SL_PRE_VALIDATION, sl_validation_text,SL_TABLE_COLUMN, SL_WHERE_CLAUSE,GM_ID,LEVELS,  SV_VALUE, CFC_APPLICATION_ID, COM_V1 "
						+ "FROM ( SELECT l.sm_service_id,l.sl_Label,l.sl_authorising,l.sl_datatype,l.sl_display_flag,l.sl_Form_Mode,l.sl_Form_Name,l.sl_Label_Id,l.sl_Label_Mar ,l.sl_Pre_Validation,l.sl_validation_text,l.sl_Table_Column,l.sl_Where_Clause,l.gm_Id,l.levels,v.sv_Value,v.cfc_application_Id,l.com_V1 "
						+ " FROM TB_SCRUTINY_LABELS l, TB_SCRUTINY_VALUES v " + " WHERE v.sl_label_id = l.sl_Label_Id"
						+ " and l.orgid = v.orgid and l.sm_service_id =:smServiceId and l.SL_ACTIVE_STATUS='A'"
						+ " and v.cfc_application_id=:applicationId and l.orgid =:orgId  " + temp + " union "
						+ " select l.sm_service_id,l.sl_Label,l.sl_authorising,l.sl_datatype,l.sl_display_flag,l.sl_Form_Mode,l.sl_Form_Name,l.sl_Label_Id,l.sl_Label_Mar ,l.sl_Pre_Validation,l.sl_validation_text,l.sl_Table_Column,l.sl_Where_Clause,l.gm_Id,l.levels,null,null,l.com_V1 "
						+ " from TB_SCRUTINY_LABELS l"
						+ " LEFT JOIN TB_SCRUTINY_VALUES v ON l.SL_LABEL_ID=v.SL_LABEL_ID AND l.ORGID = v.ORGID"
						+ "  where l.SL_ACTIVE_STATUS='A' " + "  and l.sm_service_id =:smServiceId" + temp
						+ " and l.sl_label_id not in ( select l.sl_label_id from TB_SCRUTINY_LABELS l, TB_SCRUTINY_VALUES v where l.sl_label_id = v.sl_label_id and l.orgid = v.orgid "
						+ " and l.sm_service_id =:smServiceId  and v.cfc_application_id =:applicationId and l.orgid =:orgId"
						+ temp + ")) sc ORDER BY SL_LABEL_ID");

		final Query query = createNativeQuery(nativeQuery.toString());

		if ((null != serviceId) && (serviceId > 0)) {
			query.setParameter("smServiceId", serviceId);
		}
		if ((null != orgId) && (orgId > 0)) {
			query.setParameter("orgId", orgId);
		}
		if ((null != applicationId) && (applicationId > 0)) {
			query.setParameter("applicationId", applicationId);
		}
		// US#113590
		Organisation org = new Organisation();
		org.setOrgid(orgId);
		if (Utility.isEnvPrefixAvailable(org, MainetConstants.ENV_SKDCL)
				&& (shortCode != null && shortCode.equals(MainetConstants.TradeLicense.MARKET_LICENSE))) {
			query.setParameter("tricod1", triCod1);
		}

		try

		{

			list = query.getResultList();

		} catch (final Exception exception) {
			LOG.error("Exception occur in getScrutinyUserByWardZoneBlock() ", exception);
			return list;
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abm.mainetservice.web.cfc.scrutiny.dao.ScrutinyDAO#getValueByLabelQuery(
	 * java.lang.String)
	 */
	@Override
	public String getValueByLabelQuery(final String query) {

		String value = "";
		try {
			final Query nativeQuery = createNativeQuery(query);
			Object result = nativeQuery.getSingleResult();
			if (result != null) {
				value = result.toString();
			}

		} catch (final Exception exception) {
			LOG.error("Exception occur in getValueByLabelQuery() ", exception);
			return value;

		}
		return value;
	}

}
