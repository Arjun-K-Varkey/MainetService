package com.abm.mainet.bill.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptModesDetEntity;

/**
 * @author Rahul.Yadav
 *
 */
@Repository
public class ChequeDishonorDaoImpl extends AbstractDAO<Long> implements ChequeDishonorDao {

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.web.cfc.challan.dao.ChequeDishonorDao#fetchChequePaymentData(java.lang.Long, long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TbServiceReceiptMasEntity> fetchChequePaymentData(final List<Long> chequeId,
            final long orgid, final Long deptId, final Date fromDate, final Date toDate, final Long receiptNo,
            final String chequeNo, final Long bankId) {

        final StringBuilder qlString1 = new StringBuilder(
                "select s from  TbServiceReceiptMasEntity s where  s.dpDeptId=:dpDeptId and s.orgId=:orgId and s.receiptDelFlag is null"
                        + " and s.rmRcptid in (select mode.rmRcptid.rmRcptid from TbSrcptModesDetEntity mode where mode.orgId=:orgId and  mode.cpdFeemode"
                        + " in (:cpdFeemode) and mode.rmRcptid.rmRcptid in (select d.rmRcptid.rmRcptid from TbSrcptFeesDetEntity d "
                        + " where d.orgId=:orgId and d.depositeSlipId is not null  ) ");

        if (StringUtils.isNotBlank(chequeNo)) {
            qlString1.append(" and mode.rdChequeddno=:rdChequeddno ");
        }else {
        	qlString1.append(" and mode.rdSrChkDis is null ");
        }
        qlString1.append(")");

        if ((fromDate != null) && (toDate != null)) {
            qlString1.append(" and s.rmDate between :fromDate and :todate ");
        }

        if (receiptNo != null) {
            qlString1.append(" and s.rmRcptno=:rmRcptno ");
        }
        qlString1.append(" order by s.rmRcptno desc ");

        final Query query = entityManager.createQuery(qlString1.toString());
        query.setParameter("cpdFeemode", chequeId);
        query.setParameter("orgId", orgid);
        query.setParameter("dpDeptId", deptId);
        if ((fromDate != null) && (toDate != null)) {
            query.setParameter("fromDate", fromDate);
            query.setParameter("todate", toDate);
        }
        if (receiptNo != null) {
            query.setParameter("rmRcptno", receiptNo);
        }
        if (StringUtils.isNotBlank(chequeNo)) {
            query.setParameter("rdChequeddno", chequeNo);
        }
        final List<TbServiceReceiptMasEntity> result = query.getResultList();
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.web.cfc.challan.dao.ChequeDishonorDao#updateFeeDet(com.abm.mainetservice.core.entity.
     * TbSrcptModesDetEntity)
     */
    @Override
    public void updateFeeDet(final TbSrcptModesDetEntity feeDet) {

        entityManager.merge(feeDet);
    }

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.web.cfc.challan.dao.ChequeDishonorDao#fetchReceiptFeeDetails(java.lang.Long)
     */
    @Override
    public TbSrcptModesDetEntity fetchReceiptFeeDetails(final Long rdModesid, final Long orgId) {
        final Query query = entityManager
                .createQuery("select m from TbSrcptModesDetEntity m where m.rdModesid=:rdModesid and m.orgId=:orgId ");
        query.setParameter("rdModesid", rdModesid);
        query.setParameter("orgId", orgId);
        final TbSrcptModesDetEntity result = (TbSrcptModesDetEntity) query.getSingleResult();
        return result;
    }

    @Override
    public void updateLoiNotPaid(final String rmLoiNo, final Long orgid) {
        final Query query = entityManager.createQuery(
                "update TbLoiMasEntity m set m.loiPaid='N' where m.loiNo=:loiNo and m.compositePrimaryKey.orgid=:orgId ");
        query.setParameter("loiNo", rmLoiNo);
        query.setParameter("orgId", orgid);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TbServiceReceiptMasEntity> fetchChequePaymentDataWithAccount(final List<Long> chequeId, final long orgid,
            final Long deptId, final Date fromDate, final Date toDate, final Long receiptNo, final String chequeNo,
            final Long bankId) {
        final StringBuilder qlString1 = new StringBuilder(
                "select s from  TbServiceReceiptMasEntity s where  s.dpDeptId=:dpDeptId and s.orgId=:orgId and s.receiptDelFlag is null"
                        + " and s.rmRcptid in (select mode.rmRcptid.rmRcptid from TbSrcptModesDetEntity mode where mode.orgId=:orgId and  mode.cpdFeemode"
                        + " in (:cpdFeemode) and mode.rmRcptid.rmRcptid in (select d.rmRcptid.rmRcptid from TbSrcptFeesDetEntity d "
                        + " where d.orgId=:orgId and d.depositeSlipId is not null and d.depositeSlipId in ( select a.depositeSlipId from "
                        + " AccountBankDepositeSlipMasterEntity a where a.depositeBAAccountId=:cbBankid and a.orgid=:orgId ) ) ");

        if (StringUtils.isNotBlank(chequeNo)) {
            qlString1.append(" and mode.rdChequeddno=:rdChequeddno ");
         }else {
        	qlString1.append(" and mode.rdSrChkDis is null ");
        }
        qlString1.append(")");

        if ((fromDate != null) && (toDate != null)) {
            qlString1.append(" and s.rmDate between :fromDate and :todate ");
        }

        if (receiptNo != null) {
            qlString1.append(" and s.rmRcptno=:rmRcptno ");
        }
        qlString1.append(" order by s.rmRcptno desc ");

        final Query query = entityManager.createQuery(qlString1.toString());
        query.setParameter("cpdFeemode", chequeId);
        query.setParameter("orgId", orgid);
        query.setParameter("dpDeptId", deptId);
        if ((fromDate != null) && (toDate != null)) {
            query.setParameter("fromDate", fromDate);
            query.setParameter("todate", toDate);
        }
        if (receiptNo != null) {
            query.setParameter("rmRcptno", receiptNo);
        }
        if (StringUtils.isNotBlank(chequeNo)) {
            query.setParameter("rdChequeddno", chequeNo);
        }
        query.setParameter("cbBankid", bankId);
        final List<TbServiceReceiptMasEntity> result = query.getResultList();
        return result;
    }
    
    
    @Override
    public List<TbSrcptModesDetEntity> fetchReceiptFeeDetails(List<Long> rdModesId, Long orgId) {
        final Query query = entityManager
                .createQuery("select m from TbSrcptModesDetEntity m where m.rdModesid in (:rdModesid) and m.orgId=:orgId ");
        query.setParameter("rdModesid", rdModesId);
        query.setParameter("orgId", orgId);        
        return  (List<TbSrcptModesDetEntity>) query.getResultList();
    }
    @Override
    public Long getBankId(Long orgId,Long slipId) {
    	final Query query = entityManager
                .createQuery(" select a.depositeBAAccountId  from AccountBankDepositeSlipMasterEntity a where a.depositeSlipId=:depositeSlipId and a.orgid=:orgId");
        query.setParameter("depositeSlipId", slipId);
        query.setParameter("orgId", orgId);        
        return  (Long)query.getSingleResult();	
    }
}
