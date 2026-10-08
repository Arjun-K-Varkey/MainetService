package com.abm.mainet.account.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;

public interface AccountCollectionSummaryReportJpaRepository extends PagingAndSortingRepository<TbServiceReceiptMasEntity, Long> {

    @Query(value = "SELECT  DP.DP_DEPTDESC,     d.SAC_HEAD_ID,\r\n" + 
    		"      sm.AC_HEAD_CODE, SUM(CASE WHEN cd.CPD_VALUE='C' THEN  m.RD_AMOUNT END) Cash_Amount,\r\n" + 
    		"SUM(CASE WHEN cd.CPD_VALUE NOT IN ('B','C') THEN  m.RD_AMOUNT END) Cheque_DD_Amount ,\r\n" + 
    		"SUM(CASE WHEN cd.CPD_VALUE='B' THEN  m.RD_AMOUNT END) Bank_Amount\r\n" + 
    		"  FROM tb_receipt_mas r,\r\n" + 
    		"       tb_receipt_det d,\r\n" + 
    		"       tb_receipt_mode m,\r\n" + 
    		"       tb_ac_secondaryhead_master sm,\r\n" + 
    		"       tb_comparam_mas cm,\r\n" + 
    		"       tb_comparam_det cd,\r\n" + 
    		"        TB_DEPARTMENT   DP\r\n" + 
    		"WHERE r.RM_RCPTID=d.RM_RCPTID\r\n" + 
    		"       AND r.ORGID=d.ORGID\r\n" + 
    		"       AND r.RM_RCPTID=m.RM_RCPTID\r\n" + 
    		"       AND r.ORGID=m.ORGID\r\n" + 
    		"       AND d.SAC_HEAD_ID=sm.SAC_HEAD_ID\r\n" + 
    		"       AND d.ORGID =sm.ORGID\r\n" + 
    		"       AND r.ORGID =sm.Orgid\r\n" + 
    		"       AND m.ORGID =sm.ORGID\r\n" + 
    		"       AND cm.CPM_ID =cd.CPM_ID\r\n" + 
    		"        AND R.DP_DEPTID = DP.DP_DEPTID\r\n" + 
    		"        -- AND cd.CPD_VALUE = 'C'\r\n" + 
    		"       AND cm.CPM_PREFIX = 'PAY'\r\n" + 
    		"       AND d.SAC_HEAD_ID IS NOT NULL\r\n" + 
    		"       AND cd.CPD_ID = m.CPD_FEEMODE\r\n" + 
    		"       AND r.RECEIPT_DEL_FLAG IS NULL\r\n" + 
    		"        AND r.RECEIPT_TYPE_FLAG in ('M','R','A','P')       \r\n" + 
    		"        AND r.ORGID =:orgId\r\n" + 
    		"           AND r.RM_DATE BETWEEN :fromDates AND :toDates  \r\n" + 
    		"       Group By d.SAC_HEAD_ID,sm.AC_HEAD_CODE,DP.DP_DEPTDESC", nativeQuery = true)
    List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgId(@Param("toDates") Date toDates,
            @Param("fromDates") Date fromDates, @Param("orgId") long orgId);

    @Query(value = "SELECT \r\n" +
            "    r.RM_RCPTID,\r\n" +
            "    r.RM_RCPTNO,\r\n" +
            "    r.rm_date,\r\n" +
            "    m.CPD_FEEMODE,\r\n" +
            "    r.RM_RECEIVEDFROM,\r\n" +
            "    m.RD_CHEQUEDDNO,\r\n" +
            "    SUM(d.RF_FEEAMOUNT) AS Receipt_Amount,\r\n" +
            "  r.RM_NARRATION,\r\n " +
            "  r.DP_DEPTID " +
            "FROM\r\n" +
            "    tb_receipt_mas r,\r\n" +
            "    tb_receipt_det d,\r\n" +
            "    tb_receipt_mode m,\r\n" +
            "    tb_comparam_mas cm,\r\n" +
            "    tb_comparam_det cd\r\n" +

            " " +
            "WHERE\r\n" +
            "    r.RM_RCPTID = d.RM_RCPTID\r\n" +
            "        AND r.RM_RCPTID = m.RM_RCPTID\r\n" +
            "        AND d.RM_RCPTID = m.RM_RCPTID\r\n" +
            "        AND cm.CPM_ID = cd.CPM_ID\r\n" +
            "        AND cd.CPD_ID = m.CPD_FEEMODE\r\n" +
            "        AND r.RECEIPT_DEL_FLAG is null" +
            "        AND r.RECEIPT_TYPE_FLAG in ('M','R','A','P','INV')\r\n" +
            "        AND r.ORGID =:orgId\r\n" +
            "        AND rm_date BETWEEN :fromDates AND :toDates\r\n" +
            "GROUP BY r.RM_RCPTID , r.RM_RCPTNO , r.rm_date , m.CPD_FEEMODE , r.RM_RECEIVEDFROM , m.RD_CHEQUEDDNO,r.RM_NARRATION,r.DP_DEPTID\r\n"
            +
            " order by r.rm_date,r.RM_RCPTNO asc ", nativeQuery = true)
    List<Object[]> findCollectionDetailReportbyByTodateAndFromDateAndOrgIdAndCreatedById(@Param("toDates") Date toDates,
            @Param("fromDates") Date fromDates, @Param("orgId") Long orgId);

    @Query("SELECT \r\n" +
            "    rm.rmRcptid,\r\n" +
            "    ba.baAccountNo,\r\n" +
            "    dm.depositeSlipDate,\r\n" +
            "    re.rdSrChkDate,\r\n" +
            "    re.checkStatus,\r\n" +
            "    rm.rmNarration\r\n" +
            "FROM\r\n" +
            "    TbServiceReceiptMasEntity rm,\r\n" +
            "    TbSrcptFeesDetEntity rd,\r\n" +
            "    TbSrcptModesDetEntity re,\r\n" +
            "    AccountBankDepositeSlipMasterEntity dm,\r\n" +
            "    BankAccountMasterEntity ba\r\n" +
            "WHERE\r\n" +
            "    rm.rmRcptid = rd.rmRcptid.rmRcptid\r\n" +
            "        AND rm.rmRcptid = re.rmRcptid.rmRcptid\r\n" +
            "        AND rd.rmRcptid.rmRcptid = re.rmRcptid.rmRcptid\r\n" +
            "        AND rd.depositeSlipId = dm.depositeSlipId\r\n" +
            "        AND dm.depositeBAAccountId = ba.baAccountId\r\n" +
            "        AND rm.orgId =:orgId \r\n" +
            "        AND rm.rmRcptid =:receiptId")
    List<Object[]> findBankDetailByReceiptHeadIdAndOrgId(@Param("receiptId") Long receiptId, @Param("orgId") Long orgId);

}
