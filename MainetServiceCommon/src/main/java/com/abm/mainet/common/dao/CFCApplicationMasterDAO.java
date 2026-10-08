package com.abm.mainet.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.CFCApplicationAddressEntity;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;

/**
 * @author Rajendra.Bhujbal
 *
 */

@Repository
public class CFCApplicationMasterDAO extends AbstractDAO<TbCfcApplicationMstEntity> implements ICFCApplicationMasterDAO {

    private static final Logger logger = LoggerFactory.getLogger(CFCApplicationMasterDAO.class);

    @Autowired
    private IOrganisationDAO organisationDAO;

    /*
     * (non-Javadoc)
     * @see com.abm.mainet.cfc.DAO.ICFCApplicationMasterDAO#getCFCApplicationMasterByApplicationId(long)
     */
    @Override
    public TbCfcApplicationMstEntity getCFCApplicationMasterByApplicationId(final long applicationId, final long orgId) {

        final Query query = entityManager.createQuery(
                "SELECT am FROM TbCfcApplicationMstEntity am WHERE am.apmApplicationId=?1 AND am.tbOrganisation.orgid=?2");
        query.setParameter(1, applicationId);
        query.setParameter(2, orgId);
        return (TbCfcApplicationMstEntity) query.getSingleResult();
    }

    @Override
    public long getServiceIdByApplicationId(final long applicationId, final long orgId) {

        final Query query = entityManager.createQuery(
                "SELECT am.tbServicesMst.smServiceId FROM TbCfcApplicationMstEntity am WHERE am.apmApplicationId=?1 AND am.tbOrganisation.orgid=?2");
        query.setParameter(1, applicationId);
        query.setParameter(2, orgId);

        final Object serviceID = query.getSingleResult();

        return (long) serviceID;
    }

    @Override
    public CFCApplicationAddressEntity getApplicantsDetailsDao(final long apmApplicationId) {
    	CFCApplicationAddressEntity entity = null;
    	final Query query = entityManager.createQuery("FROM CFCApplicationAddressEntity aa WHERE aa.apmApplicationId=?1 ");
        query.setParameter(1, apmApplicationId);

        try{
        	entity = (CFCApplicationAddressEntity) query.getSingleResult();
        }
        catch (NoResultException nre){
        	
        }
        return entity;
    }

    @Override
    public TbCfcApplicationMstEntity getCFCApplicationByApplicationId(final long applicationId, final long orgId) {
        TbCfcApplicationMstEntity entity = null;
        Organisation org = organisationDAO.getOrganisationById(orgId, MainetConstants.STATUS.ACTIVE);
        boolean moalFlag = false;
        final StringBuilder query = new StringBuilder(
                "SELECT am FROM TbCfcApplicationMstEntity am WHERE am.apmApplicationId=?1 ");
        LookUp multiOrgDataList = null;
        try {
            logger.error("Fetching MOAL prefix");
            multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL, MainetConstants.ENV, org);
            logger.error("MOAL prefix is available");
        } catch (Exception e) {
            logger.error("No Prefix found for ENV(MOAL)");
        }

        if (!(multiOrgDataList != null && StringUtils.isNotBlank(multiOrgDataList.getOtherField())
                && StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY))) {
            query.append(" AND am.tbOrganisation.orgid=:orgId");
            moalFlag = true;
            logger.error("Appending org id in query: " + orgId);
        }
        final Query queryData = entityManager.createQuery(query.toString());
        queryData.setParameter(1, applicationId);
        if (moalFlag == true) {
            logger.error("Setting orgId parameter in  Query setParameter field: " + query.toString());
            queryData.setParameter("orgId", orgId);
        }

        try {
            entity = (TbCfcApplicationMstEntity) queryData.getSingleResult();
        } catch (final NoResultException nre) {
            // Ignore this because as per logic this is ok!
        }
        return entity;
    }

    /**
     *
     */
    @Override
    public boolean updateApprovedBy(final TbCfcApplicationMstEntity entity) {

        final Query query = entityManager.createQuery(
                "UPDATE TbCfcApplicationMstEntity am SET am.apmApprovedBy=:apmApprovedBy, am.apmApprovedDate=:apmApprovedDate,"
                        + "am.updatedBy=:updatedBy, am.updatedDate=:updatedDate, am.lgIpMacUpd=:lgIpMacUpd, am.apmApplClosedFlag=:apmApplClosedFlag WHERE am.apmApplicationId=:apmApplicationId");

        query.setParameter("apmApprovedBy", entity.getApmApprovedBy());
        query.setParameter("apmApprovedDate", entity.getApmApprovedDate());
        query.setParameter("updatedBy", entity.getUpdatedBy());
        query.setParameter("updatedDate", new Date());
        query.setParameter("lgIpMacUpd", entity.getLgIpMacUpd());
        query.setParameter("apmApplClosedFlag", "Y");
        query.setParameter("apmApplicationId", entity.getApmApplicationId());

        return query.executeUpdate() == 1 ? true : false;
    }

    @Override
    public boolean updateCFCApplicationMasterPaymentStatus(final long applicationId, final String paid, final long orgid) {
        TbCfcApplicationMstEntity applicationStatus = null;
        final Query query = entityManager.createQuery(
                "select s from TbCfcApplicationMstEntity s where s.tbOrganisation.orgid=?1 and s.apmApplicationId=?2");
        query.setParameter(1, orgid);
        query.setParameter(2, applicationId);
        try {
            applicationStatus = (TbCfcApplicationMstEntity) query.getSingleResult();
        } catch (final NoResultException nre) {
        }
        if (applicationStatus != null) {
            applicationStatus.setApmPayStatFlag(paid);
            try {
                entityManager.merge(applicationStatus);
                return true;
            } catch (final Exception e) {
                logger.info("Norecord avilable in TbCfcApplicationMstEntity for update:" + applicationId);
                return false;
            }
        }
        return false;
    }

    @Override
    public TbCfcApplicationMstEntity getApplicationMaster(final long applicationId, final Organisation orgnisation) {
        TbCfcApplicationMstEntity master = null;
        final Query query = createQuery(
                "select s from TbCfcApplicationMstEntity s where s.tbOrganisation = ?1 and s.apmApplicationId = ?2 ");
        query.setParameter(1, orgnisation);
        query.setParameter(2, applicationId);
        try {
            master = (TbCfcApplicationMstEntity) query.getSingleResult();
        } catch (final NoResultException nre) {
            logger.info("Norecord avilable in TbCfcApplicationMstEntity for:" + applicationId);
        }
        return master;
    }

    @Override
    public void updateCFCApplicationStatus(final String status, final long applicationId) {
        final String HQL = "UPDATE TbCfcApplicationMstEntity as s set s.apmChklstVrfyFlag = :apmChklstVrfyFlag, s.apmApplSuccessFlag = :apmApplSuccessFlag, s.apmApplClosedFlag = :apmApplClosedFlag   WHERE s.apmApplicationId = :apmApplicationId ";
        final Query query = createQuery(HQL);

        query.setParameter("apmApplicationId", applicationId);

        if (status.equals("A")) {
            query.setParameter("apmChklstVrfyFlag", "Y");
            query.setParameter("apmApplSuccessFlag", "");
            query.setParameter("apmApplClosedFlag", "");
        }

        if (status.equals("R")) {
            query.setParameter("apmChklstVrfyFlag", "N");
            query.setParameter("apmApplSuccessFlag", "N");
            query.setParameter("apmApplClosedFlag", "Y");
        }

        if (status.equals("H")) {
            query.setParameter("apmChklstVrfyFlag", "H");
            query.setParameter("apmApplSuccessFlag", "N");
            query.setParameter("apmApplClosedFlag", "");
        }

        query.executeUpdate();

    }

    @Override
    public TbCfcApplicationMstEntity getCFCApplicationByRefNoOrAppNo(String refNumber, Long applicationId, Long orgid) {
        final Query query = entityManager.createNativeQuery(
                "SELECT * FROM TB_CFC_APPLICATION_MST  WHERE (REF_NO=?1 OR APM_APPLICATION_ID=?2) AND ORGID=?3",
                TbCfcApplicationMstEntity.class);

        query.setParameter(1, refNumber);
        query.setParameter(2, applicationId);
        query.setParameter(3, orgid);
        return (TbCfcApplicationMstEntity) query.getSingleResult();
    }

    //#139419
    @Transactional
    public void updateIssuanceDataInCFCApplication(Long applicationId, Long issuedBy, Long orgId) {
        final Query query = createQuery(
                "UPDATE TbCfcApplicationMstEntity c SET  c.issuedDate=CURRENT_TIMESTAMP, c.issuedBy=?1  where c.apmApplicationId=?2 and c.tbOrganisation.orgid=?3");
        query.setParameter(1, issuedBy);
        query.setParameter(2, applicationId);
        query.setParameter(3, orgId);
        query.executeUpdate();
    }
    @Override
    public TbCfcApplicationMstEntity getCFCApplicationByOnlyApplicationId(final long applicationId) {
        TbCfcApplicationMstEntity entity = null;
        
        final StringBuilder query = new StringBuilder(
                "SELECT am FROM TbCfcApplicationMstEntity am WHERE am.apmApplicationId=?1 ");
       

        final Query queryData = entityManager.createQuery(query.toString());
        queryData.setParameter(1, applicationId);

        try {
            entity = (TbCfcApplicationMstEntity) queryData.getSingleResult();
        } catch (final NoResultException nre) {
            // Ignore this because as per logic this is ok!
        }
        return entity;
    }
    
    @Override
   	public List<TbCfcApplicationMstEntity> getApplicantList(Long orgid) {

   		final StringBuilder queryString = new StringBuilder();
   		queryString.append("select c from TbCfcApplicationMstEntity c, TbLoiMasEntity l  where c.tbOrganisation.orgid=:orgid and c.apmApplicationId=l.loiApplicationId");

   		Query query = this.createQuery(queryString.toString());
   		query.setParameter("orgid", orgid);
   		
   		
   		@SuppressWarnings("unchecked")
   		List<TbCfcApplicationMstEntity> entityList= query.getResultList();
   		return entityList;
   	}
    @Override
	public TbCfcApplicationMstEntity saveApplicationData(final TbCfcApplicationMstEntity entity) {

		logger.info("start the saveApplicationData()....for Application Id  "+entity.getApmApplicationId());
		try {
			entityManager.persist(entity);

		} catch (final Exception exception) {
			logger.error("Current Application Id already generated "+entity.getApmApplicationId(), exception);

		}
		return entity;
	}

	@Override
	public boolean updateCFCApplicationPayStatusAndTranscnIdForAapleSarkar(long applicationId, long transactionId,
			long orgId) {

		final String HQL = "UPDATE TbCfcApplicationMstEntity as s set s.apmPayStatFlag = 'Y', s.apmRemark = :transactionId  WHERE s.apmApplicationId = :apmApplicationId and s.tbOrganisation.orgid=:orgId";
		final Query query = createQuery(HQL);

		String aapleSarkarTansId = "AAPLE_SARKAR_TRANSCN_ID_" + transactionId;
		query.setParameter("transactionId", aapleSarkarTansId);
		query.setParameter("apmApplicationId", applicationId);
		query.setParameter("orgId", orgId);

		return query.executeUpdate() == 1 ? true : false;
	}
}
