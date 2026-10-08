
package com.abm.mainet.account.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.abm.mainet.account.domain.AccountBudgetProjectedExpenditureEntity;

/**
 * @author prasad.kancharla
 *
 */

public interface BudgetProjectedExpenditureRepository
        extends PagingAndSortingRepository<AccountBudgetProjectedExpenditureEntity, Long> {

    @Query("select e from AccountBudgetProjectedExpenditureEntity e  where e.orgid=:orgid and e.faYearid =:faYearid order by 1 desc")
    List<AccountBudgetProjectedExpenditureEntity> findByFinancialId(@Param("faYearid") Long faYearid,
            @Param("orgid") Long orgId);

    @Query("select e.revisedEstamt,e.orginalEstamt,e.expenditureAmt,e.prExpenditureid,e.tbDepartment.dpDeptid,e.expectedCurrentYearO,e.remark from AccountBudgetProjectedExpenditureEntity e where e.faYearid= :faYearid and e.tbAcBudgetCodeMaster.prBudgetCodeid =:budgCodeid and e.orgid=:orgid")
    List<Object[]> findByExpOrgAmount(@Param("faYearid") Long faYearid, @Param("budgCodeid") Long budgCodeid,
            @Param("orgid") Long orgId);

    @Modifying
    @Query("update AccountBudgetProjectedExpenditureEntity as te set te.revisedEstamt =:revisedEstamt where te.faYearid =:faYearid and te.prExpenditureid =:prExpenditureid and te.orgid =:orgid")
    void updateRevisedEstmtDataExpTable(@Param("faYearid") Long faYearid,
            @Param("prExpenditureid") Long prExpenditureid, @Param("revisedEstamt") String revisedEstamt,
            @Param("orgid") Long orgId);
    
    @Modifying
    @Query("update AccountBudgetProjectedExpenditureEntity as te set te.revisedEstamt =:revisedEstamt,te.expectedCurrentYearO =:expectedCurrentYearO where te.faYearid =:faYearid and te.prExpenditureid =:prExpenditureid and te.orgid =:orgid")
    void updateRevisedEstmtDataExpTable(@Param("faYearid") Long faYearid,
            @Param("prExpenditureid") Long prExpenditureid, @Param("revisedEstamt") String revisedEstamt,@Param("expectedCurrentYearO") BigDecimal expectedCurrentYearO,
            @Param("orgid") Long orgId);
    

    @Query("select s.tbAcBudgetCodeMaster.prBudgetCodeid from AccountBudgetProjectedExpenditureEntity s where s.faYearid= :faYearid and s.cpdBugsubtypeId=:cpdBugsubtypeId and s.tbDepartment.dpDeptid=:dpDeptid and s.orgid=:orgId ")
    List<Object[]> findBudgetCodeId(@Param("faYearid") Long faYearid, @Param("cpdBugsubtypeId") Long cpdBugsubtypeId,
            @Param("dpDeptid") Long dpDeptid, @Param("orgId") Long orgid);

    @Query("select s.tbDepartment.dpDeptid,s.tbDepartment.dpDeptdesc from AccountBudgetProjectedExpenditureEntity s where s.orgid=:orgId order by s.tbDepartment.dpDeptid,s.tbDepartment.dpDeptdesc desc")
    List<Object[]> getAllDepartmentIdsData(@Param("orgId") Long orgid);

    @Query("select distinct s.tbAcBudgetCodeMaster.prBudgetCodeid,s.tbAcBudgetCodeMaster.prBudgetCode from AccountBudgetProjectedExpenditureEntity s where s.tbDepartment.dpDeptid=:dpDeptid and s.orgid=:orgId")
    List<Object[]> findByAllExpBudgetHeads(@Param("dpDeptid") Long dpDeptid, @Param("orgId") Long orgId);

    @Query("select s.revisedEstamt,s.orginalEstamt,s.expenditureAmt from AccountBudgetProjectedExpenditureEntity s where s.tbAcBudgetCodeMaster.prBudgetCodeid=:budgCodeid and s.orgid=:orgId")
    List<Object[]> findByOrgAmount(@Param("budgCodeid") Long budgCodeid, @Param("orgId") Long orgId);

    @Query("select  s.tbAcBudgetCodeMaster.prBudgetCodeid,s.tbAcBudgetCodeMaster.prBudgetCode,s.tbAcBudgetCodeMaster.tbAcPrimaryheadMaster.cpdIdAcHeadTypes from AccountBudgetProjectedExpenditureEntity s where s.orgid=:orgId order by s.tbAcBudgetCodeMaster.prBudgetCode desc")
    List<Object[]> findByAllExpBudgetHeads(@Param("orgId") Long orgId);

    @Query("select e from AccountBudgetProjectedExpenditureEntity e  where e.orgid=:orgid order by 1 desc")
    List<AccountBudgetProjectedExpenditureEntity> findAllBudgetProjectedExpenditureByOrgId(@Param("orgid") Long orgId);

    @Query("select e from AccountBudgetProjectedExpenditureEntity e  where e.orgid=:orgId and e.faYearid =:finYearId and e.orginalEstamt !='0.0' ")
    List<AccountBudgetProjectedExpenditureEntity> findExpenditureDataByFinYearId(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId);

    @Query("select e.tbDepartment.dpDeptid from AccountBudgetProjectedExpenditureEntity e where e.orgid =:orgId and e.tbAcBudgetCodeMaster.prBudgetCodeid =:budgetCode and e.faYearid =:finYearId")
    Long getDepartmentByBudget(@Param("orgId") Long orgId, @Param("budgetCode") Long budgetCode,
            @Param("finYearId") Long finYearId);

    @Query(value = "select PE.PR_EXPENDITUREID,\r\n" + "       PE.ORGINAL_ESTAMT,\r\n" + "       PE.REVISED_ESTAMT,\r\n"
            + "       sum(BE.BCH_CHARGES_AMT), BMS.DP_DEPTID\r\n" + "  from TB_AC_PROJECTED_EXPENDITURE PE,\r\n"
            + "       TB_AC_BUDGETCODE_MAS        BM,\r\n" + "       TB_AC_SECONDARYHEAD_MASTER  SM,\r\n"
            + "       TB_AC_PRIMARYHEAD_MASTER    PM,\r\n" 
            + "       TB_AC_BILL_EXP_DETAIL       BE,\r\n" + "       TB_AC_BILL_MAS              BMS\r\n"
            + " where BE.BM_ID = BMS.BM_ID\r\n" + "   AND PE.BUDGETCODE_ID = BM.BUDGETCODE_ID\r\n"
            + "   AND PM.PAC_HEAD_ID = BM.PAC_HEAD_ID\r\n" 
            + "   AND SM.SAC_HEAD_ID = BE.SAC_HEAD_ID\r\n" + "   AND SM.PAC_HEAD_ID = PM.PAC_HEAD_ID\r\n"
            +"   AND SM.ORGID = BE.ORGID\r\n"
            + "   AND BMS.DP_DEPTID = PE.DP_DEPTID\r\n "
            + "   AND PE.ORGID = :orgId\r\n" + "   AND BMS.ORGID = :orgId\r\n" + "   AND PE.FA_YEARID =:finYearId\r\n"
            + "   AND PE.BUDGETCODE_ID =:budgetCodeId\r\n" + "   AND BMS.CHECKER_AUTHO = 'Y'\r\n"
            + "   AND BMS.BM_DEL_FLAG IS NULL\r\n" + "   AND (BMS.CHECKER_DATE between :fromDate and :toDate)\r\n"
            + " GROUP BY PE.PR_EXPENDITUREID,\r\n" + "          PE.ORGINAL_ESTAMT,\r\n"
            + "          PE.REVISED_ESTAMT, BMS.DP_DEPTID\r\n", nativeQuery = true)

    List<Object[]> getExpenditureDetailsForBillEntryFormView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeId") Long budgetCodeId,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT pr.pr_expenditureid,pr.orginal_estamt, pr.revised_estamt, SUM(pd.PAYMENT_AMT)\r\n"
            + "  FROM tb_ac_budgetcode_mas        bm,\r\n" + "       tb_ac_secondaryhead_master  sm,\r\n"
            + "       tb_ac_function_master       fm,\r\n" + "       tb_ac_primaryhead_master    pm,\r\n"
            + "       tb_ac_projected_expenditure pr,\r\n" + "       tb_ac_payment_mas           ps,\r\n"
            + "       tb_ac_payment_det           pd\r\n" + "   WHERE sm.PAC_HEAD_ID = pm.PAC_HEAD_ID\r\n"
            + "   AND sm.FUNCTION_ID = fm.FUNCTION_ID\r\n" + "   AND sm.SAC_HEAD_ID = pd.BUDGETCODE_ID\r\n"
            + "   AND ps.PAYMENT_ID = pd.PAYMENT_ID\r\n" + "   AND pm.PAC_HEAD_ID = bm.PAC_HEAD_ID\r\n"
            + "   AND fm.FUNCTION_ID = bm.FUNCTION_ID\r\n" + "   AND bm.BUDGETCODE_ID = pr.BUDGETCODE_ID\r\n"
            + "   AND ps.PAYMENT_DEL_FLAG IS NULL\r\n" + "   AND bm.ORGID = pr.ORGID\r\n"
            + "   AND ps.ORGID = pd.ORGID\r\n" + "   AND sm.ORGID = ps.ORGID\r\n" + "   and sm.ORGID = pd.ORGID\r\n"
            + "   and ps.payment_date between :fromDate and :toDate\r\n" + "   AND pr.BUDGETCODE_ID =:budgetCodeId\r\n"
            + "   AND pr.FA_YEARID =:finYearId\r\n" + "   AND pd.ORGID = :orgId\r\n"
            + "   GROUP BY pr.PR_EXPENDITUREID, pr.ORGINAL_ESTAMT, pr.REVISED_ESTAMT", nativeQuery = true)

    List<Object[]> getExpenditureDetailsForPaymentEntryFormView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeId") Long budgetCodeId,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("select e.prExpenditureid,e.orginalEstamt,e.revisedEstamt,e.expenditureAmt,e.tbDepartment.dpDeptid from AccountBudgetProjectedExpenditureEntity e,AccountBudgetCodeEntity bc "
            + "where e.tbAcBudgetCodeMaster.prBudgetCodeid=bc.prBudgetCodeid and e.orgid=:orgId and e.faYearid =:finYearId and e.tbAcBudgetCodeMaster.prBudgetCodeid =:budgetCodeId "
            + "group by e.prExpenditureid,e.orginalEstamt,e.revisedEstamt,e.expenditureAmt")
    List<Object[]> getExpenditureDetailsForPaymentEntryTransactionFormView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeId") Long budgetCodeId);

    @Query("select sum(e.orginalEstamt),sum(e.revisedEstamt) from AccountBudgetProjectedExpenditureEntity e where e.orgid=:orgId and e.faYearid =:finYearId and e.tbAcBudgetCodeMaster.prBudgetCodeid in :budgetCodeIdList "
            + "group by e.orginalEstamt,e.revisedEstamt")
    List<Object[]> getExpenditureDetailsForPaymentEntryFinalView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeIdList") List<Long> budgetCodeIdList);
    
    @Query("select sum(e.orginalEstamt),sum(e.revisedEstamt) from AccountBudgetProjectedExpenditureEntity e where e.orgid=:orgId and e.faYearid =:finYearId and e.tbAcBudgetCodeMaster.prBudgetCodeid in :budgetCodeIdList and e.tbDepartment.dpDeptid=:deptId "
            + "group by e.orginalEstamt,e.revisedEstamt")
    List<Object[]> getExpenditureDetailsForPaymentEntryFinalViewBasedOnDeptId(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeIdList") List<Long> budgetCodeIdList,@Param("deptId") Long deptId);

    @Query("select sum(pd.paymentAmt) from AccountPaymentMasterEntity pe, AccountPaymentDetEntity pd where pe.paymentDate between :fromDate and :toDate and pe.paymentId=pd.paymentMasterId.paymentId and pd.orgId=:orgId and pe.paymentDeletionFlag IS NULL and pd.budgetCodeId =:sacHeadIdList "
            + "group by pd.paymentAmt")
    List<BigDecimal> getExpenditureDetailsForPaymentEntryFinalPaymentAmt(@Param("orgId") Long orgId,
            @Param("sacHeadIdList") Long sacHeadIdList, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    
    @Query("select sum(pd.paymentAmt) from AccountPaymentMasterEntity pe, AccountPaymentDetEntity pd,AccountBillEntryMasterEnitity b\r\n " + 
    		" where pe.paymentDate between :fromDate and :toDate \r\n" + 
    		" and pe.paymentId=pd.paymentMasterId.paymentId \r\n" + 
    		" and pd.orgId=:orgId \r\n" + 
    		" and pe.paymentDeletionFlag IS NULL \r\n" + 
    		" and pd.budgetCodeId =:sacHeadIdList \r\n" + 
    		" and pd.orgId=b.orgId\r\n" + 
    		" and pd.billId=b.id\r\n" + 
    		" and b.departmentId.dpDeptid =:deptId \r\n"+
    		" and b.billDeletionFlag is null\r\n")
    List<BigDecimal> getExpenditureDetailsForPaymentEntryFinalPaymentAmtBasedOnDeparment(@Param("orgId") Long orgId,
            @Param("sacHeadIdList") Long sacHeadIdList, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,@Param("deptId") Long deptId);
    
    @Query("select sum(d.billChargesAmount) from AccountBillEntryMasterEnitity m ,AccountBillEntryExpenditureDetEntity d \r\n" + 
    		" where m.orgId=d.orgid\r\n" + 
    		" and m.id=d.billMasterId.id\r\n" + 
    		" and m.billDeletionFlag is null\r\n" + 
    		" and d.sacHeadId=:sacHeadIdList\r\n" + 
    		" and m.orgId=:orgId\r\n" + 
    		" and m.billEntryDate between :fromDate and :toDate\r\n" + 
    		" and m.checkerAuthorization='Y'\r\n" + 
    		" and m.departmentId.dpDeptid=:deptId")
    List<BigDecimal> getAccrualExpenditureDetails(@Param("orgId") Long orgId,
            @Param("sacHeadIdList") Long sacHeadIdList, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,@Param("deptId") Long deptId);

    @Query("select s.revisedEstamt,s.orginalEstamt,s.expenditureAmt from AccountBudgetProjectedExpenditureEntity s where s.tbAcBudgetCodeMaster.prBudgetCodeid=:budgCodeid and s.orgid=:orgId and s.faYearid=:yearId ")
    List<Object[]> findByOrgAmounts(@Param("budgCodeid") Long budgCodeid, @Param("orgId") Long orgId,
            @Param("yearId") Long yearId);

    @Query("select ae from AccountBudgetProjectedExpenditureEntity ae where ae.orgid=:orgId")
    List<AccountBudgetProjectedExpenditureEntity> getBudgetProjectedExpenditure(@Param("orgId") Long orgId);

    @Query("select distinct e.prExpenditureid from AccountBudgetProjectedExpenditureEntity e where e.faYearid=:faYearid and e.tbDepartment.dpDeptid=:dpDeptid and e.orgid=:orgid and e.tbAcBudgetCodeMaster.prBudgetCodeid=:prExpBudgetCode")
    Long getBudgetProjectedExpenditurePrimaryKeyId(@Param("faYearid") Long faYearid, @Param("dpDeptid") Long dpDeptid,
            @Param("orgid") Long orgid,@Param("prExpBudgetCode") Long prExpBudgetCode);

    @Modifying //update next year original estimate
    @Query("update AccountBudgetProjectedExpenditureEntity as te set te.orginalEstamt =:estimateForNextyearExp where te.prExpenditureid =:prExpenditureid and te.orgid =:orgid")
    void updateNextYearOriginalEstimateAmount(@Param("prExpenditureid") Long prExpenditureid,
            @Param("estimateForNextyearExp") BigDecimal estimateForNextyearExp, @Param("orgid") Long orgid);

    @Query("select e.prExpenditureid,e.orginalEstamt,e.revisedEstamt,e.expenditureAmt from AccountBudgetProjectedExpenditureEntity e,AccountBudgetCodeEntity bc "
            + "where e.tbAcBudgetCodeMaster.prBudgetCodeid=bc.prBudgetCodeid and e.orgid=:orgId and e.faYearid =:finYearId and e.tbAcBudgetCodeMaster.prBudgetCodeid =:budgetCodeId and e.tbDepartment.dpDeptid =:dpDeptid "
            + "group by e.prExpenditureid,e.orginalEstamt,e.revisedEstamt,e.expenditureAmt")
    List<Object[]> getExpDetailsForPayEntryTransFormInvoiceBillBudgetView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeId") Long budgetCodeId, @Param("dpDeptid") Long dpDeptid);

    @Query(value = "select PE.PR_EXPENDITUREID,\r\n" + "       PE.ORGINAL_ESTAMT,\r\n" + "       PE.REVISED_ESTAMT,\r\n"
            + "       sum(BE.BCH_CHARGES_AMT)\r\n" + "  from TB_AC_PROJECTED_EXPENDITURE PE,\r\n"
            + "       TB_AC_BUDGETCODE_MAS        BM,\r\n" + "       TB_AC_SECONDARYHEAD_MASTER  SM,\r\n"
            + "       TB_AC_PRIMARYHEAD_MASTER    PM,\r\n" + "       TB_AC_FUNCTION_MASTER       FM,\r\n"
            + "       TB_AC_BILL_EXP_DETAIL       BE,\r\n" + "       TB_AC_BILL_MAS              BMS\r\n"
            + " where BE.BM_ID = BMS.BM_ID\r\n" + "   AND PE.BUDGETCODE_ID = BM.BUDGETCODE_ID\r\n"
            + "   AND PM.PAC_HEAD_ID = BM.PAC_HEAD_ID\r\n" + "   AND FM.FUNCTION_ID = BM.FUNCTION_ID\r\n"
            + "   AND SM.SAC_HEAD_ID = BE.SAC_HEAD_ID\r\n" + "   AND SM.PAC_HEAD_ID = PM.PAC_HEAD_ID\r\n"
            + "   AND SM.FUNCTION_ID = FM.FUNCTION_ID\r\n" + "   AND SM.ORGID = BE.ORGID\r\n"
            + "   AND PE.ORGID = :orgId\r\n" + "   AND BMS.ORGID = :orgId\r\n" + "   AND PE.FA_YEARID =:finYearId\r\n"
            + "   AND PE.DP_DEPTID = :dpDeptid\r\n"
            + "   AND PE.BUDGETCODE_ID =:budgetCodeId\r\n" + "   AND BMS.CHECKER_AUTHO = 'Y'\r\n"
            + "   AND BMS.BM_DEL_FLAG IS NULL\r\n" + "   AND (BMS.CHECKER_DATE between :fromDate and :toDate)\r\n"
            + " GROUP BY PE.PR_EXPENDITUREID,\r\n" + "          PE.ORGINAL_ESTAMT,\r\n"
            + "          PE.REVISED_ESTAMT\r\n", nativeQuery = true)
    List<Object[]> getExpDetailsForInvoiceBillEntryFormBudgetView(@Param("orgId") Long orgId,
            @Param("finYearId") Long finYearId, @Param("budgetCodeId") Long budgetCodeId,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("dpDeptid") Long dpDeptid);

    @Query(value = "SELECT R.PAC_HEAD_COMPO_CODE, R.PAC_HEAD_DESC, SUM(P.AMOUNT)\r\n" +
            "  FROM (SELECT A.PAC_HEAD_COMPO_CODE,\r\n" +
            "               A.PAC_HEAD_DESC,\r\n" +
            "               A.PAC_HEAD_ID AS LEVEL2,\r\n" +
            "               B.PAC_HEAD_ID AS LEVEL1,\r\n" +
            "               SUM(AAA.RCPTAMT) AS AMOUNT\r\n" +
            "          FROM (SELECT AC_HEAD_CODE,\r\n" +
            "                       SAC_HEAD_ID,\r\n" +
            "                       RCPTAMT,\r\n" +
            "                       A.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT VD.PAC_HEAD_ID,\r\n" +
            "                               VD.SAC_HEAD_ID SAC_HEAD_ID,\r\n" +
            "                               VD.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               (SUM(VD.VAMT_CR) - SUM(VD.VAMT_DR)) RCPTAMT\r\n" +
            "                          FROM VW_VOUCHER_DETAIL VD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER SD\r\n" +
            "                         WHERE     VD.VOU_SUBTYPE_CPD_ID IN\r\n" +
            "                                      (SELECT CPD_ID\r\n" +
            "                                         FROM TB_COMPARAM_DET D\r\n" +
            "                                        WHERE     D.CPM_ID IN\r\n" +
            "                                                     (SELECT CPM_ID\r\n" +
            "                                                        FROM TB_COMPARAM_MAS\r\n" +
            "                                                             M\r\n" +
            "                                                       WHERE M.CPM_PREFIX =\r\n" +
            "                                                                'TDP')\r\n" +
            "                                              AND CPD_VALUE IN ('RRE',\r\n" +
            "                                                                'RV',\r\n" +
            "                                                                'RDC',\r\n" +
            "                                                                'ASE'))\r\n" +
            "                               AND VD.SAC_HEAD_ID = SD.SAC_HEAD_ID\r\n" +
            "                               AND SD.SAC_LED_TYPE IN\r\n" +
            "                                      (SELECT CPD_ID\r\n" +
            "                                         FROM TB_COMPARAM_DET D\r\n" +
            "                                        WHERE     D.CPM_ID IN\r\n" +
            "                                                     (SELECT CPM_ID\r\n" +
            "                                                        FROM TB_COMPARAM_MAS\r\n" +
            "                                                             M\r\n" +
            "                                                       WHERE M.CPM_PREFIX =\r\n" +
            "                                                                'FTY')\r\n" +
            "                                              AND CPD_VALUE IN ('VD', 'OT'))\r\n" +
            "                               AND VD.VOU_POSTING_DATE BETWEEN :fromDate\r\n" +
            "                                                           AND :toDate\r\n" +
            "                               AND VD.ORGID =:orgId\r\n" +
            "                        GROUP BY VD.AC_HEAD_CODE,\r\n" +
            "                                 VD.SAC_HEAD_ID,\r\n" +
            "                                 VD.PAC_HEAD_ID) A) AAA,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER A,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER B\r\n" +
            "         WHERE     AAA.PAC_HEAD_ID = A.PAC_HEAD_ID\r\n" +
            "               AND A.PAC_HEAD_PARENT_ID = B.PAC_HEAD_ID\r\n" +
            "        GROUP BY A.PAC_HEAD_ID,\r\n" +
            "                 A.PAC_HEAD_DESC,\r\n" +
            "                 B.PAC_HEAD_ID,\r\n" +
            "                 A.PAC_HEAD_COMPO_CODE) P,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER R\r\n" +
            " WHERE P.LEVEL2 = R.PAC_HEAD_ID AND P.PAC_HEAD_COMPO_CODE =:primaryAcHeadId\r\n" +
            "GROUP BY LEVEL2, R.PAC_HEAD_DESC, R.PAC_HEAD_COMPO_CODE\r\n" +
            "ORDER BY R.PAC_HEAD_COMPO_CODE ASC", nativeQuery = true)
    List<Object[]> queryIncomeReportData(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("orgId") Long orgId, @Param("primaryAcHeadId") Long primaryAcHeadId);

    @Query(value = "SELECT R.PAC_HEAD_COMPO_CODE, R.PAC_HEAD_DESC, SUM(P.AMOUNT)\r\n" +
            "  FROM (SELECT A.PAC_HEAD_COMPO_CODE,\r\n" +
            "               A.PAC_HEAD_DESC,\r\n" +
            "               A.PAC_HEAD_ID AS LEVEL2,\r\n" +
            "               B.PAC_HEAD_ID AS LEVEL1,\r\n" +
            "               SUM(AAA.PAYMTAMT) AS AMOUNT\r\n" +
            "          FROM (SELECT AC_HEAD_CODE,\r\n" +
            "                       SAC_HEAD_ID,\r\n" +
            "                       PAYMTAMT,\r\n" +
            "                       A.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT SM.PAC_HEAD_ID,\r\n" +
            "                               PD.BUDGETCODE_ID SAC_HEAD_ID,\r\n" +
            "                               SM.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               SUM(PD.PAYMENT_AMT) PAYMTAMT\r\n" +
            "                          FROM TB_AC_PAYMENT_MAS PMS,\r\n" +
            "                               TB_AC_PAYMENT_DET PD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER SM,\r\n" +
            "                               TB_AC_PRIMARYHEAD_MASTER PM\r\n" +
            "                         WHERE     PMS.PAYMENT_ID = PD.PAYMENT_ID\r\n" +
            "                               AND PMS.ORGID = PD.ORGID\r\n" +
            "                               AND SM.PAC_HEAD_ID = PM.PAC_HEAD_ID\r\n" +
            "                               AND SM.SAC_HEAD_ID = PD.BUDGETCODE_ID\r\n" +
            "                               AND PMS.PAYMENT_DEL_FLAG IS NULL\r\n" +
            "                               AND PMS.PAYMENT_DATE BETWEEN :fromDate\r\n" +
            "                                                        AND :toDate\r\n" +
            "                               AND PMS.ORGID =:orgId\r\n" +
            "                        GROUP BY SM.AC_HEAD_CODE,\r\n" +
            "                                 PD.BUDGETCODE_ID,\r\n" +
            "                                 SM.PAC_HEAD_ID) A) AAA,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER A,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER B\r\n" +
            "         WHERE     AAA.PAC_HEAD_ID = A.PAC_HEAD_ID\r\n" +
            "               AND A.PAC_HEAD_PARENT_ID = B.PAC_HEAD_ID\r\n" +
            "        GROUP BY A.PAC_HEAD_ID,\r\n" +
            "                 A.PAC_HEAD_DESC,\r\n" +
            "                 B.PAC_HEAD_ID,\r\n" +
            "                 A.PAC_HEAD_COMPO_CODE) P,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER R\r\n" +
            " WHERE P.LEVEL2 = R.PAC_HEAD_ID AND P.PAC_HEAD_COMPO_CODE =:primaryAcHeadId\r\n" +
            "GROUP BY LEVEL2, R.PAC_HEAD_DESC, R.PAC_HEAD_COMPO_CODE\r\n" +
            "ORDER BY R.PAC_HEAD_COMPO_CODE ASC", nativeQuery = true)
    List<Object[]> queryExpenditureReportData(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("orgId") Long orgId, @Param("primaryAcHeadId") Long primaryAcHeadId);

    @Query(value = "SELECT PM.PAC_HEAD_CODE,PM.PAC_HEAD_DESC,PM.CPD_ID_ACHEADTYPES,PM.orgid,pm.PAC_HEAD_ID FROM TB_AC_PRIMARYHEAD_MASTER PM WHERE PM.PAC_HEAD_PARENT_ID IS NULL", nativeQuery = true)
    List<Object[]> getPrimaryHeadId();

    @Query(value = "SELECT C.PAC_HEAD_COMPO_CODE PAC_HEAD_COMPO_CODE,\r\n" +
            "       C.PAC_HEAD_DESC HEAD_DESC,\r\n" +
            "       SUM(AAA.OPENBAL_AMT_CR) - SUM(AAA.OPENBAL_AMT_DR) AS OPENBAL_AMT_CR,\r\n" +
            "       SUM(AAA.VAMT_DR) AS VAMT_DR,\r\n" +
            "       SUM(AAA.VAMT_CR) AS VAMT_CR\r\n" +
            "  FROM (SELECT AC_HEAD_CODE,\r\n" +
            "               SAC_HEAD_ID,\r\n" +
            "               OPENBAL_AMT_DR,\r\n" +
            "               OPENBAL_AMT_CR,\r\n" +
            "               VAMT_CR,\r\n" +
            "               VAMT_DR,\r\n" +
            "               A.PAC_HEAD_ID\r\n" +
            "          FROM (SELECT AC_HEAD_CODE,\r\n" +
            "                       SAC_HEAD_ID,\r\n" +
            "                       OPENBAL_AMT_DR,\r\n" +
            "                       OPENBAL_AMT_CR,\r\n" +
            "                       VAMT_CR,\r\n" +
            "                       VAMT_DR,\r\n" +
            "                       A.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT VD.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               SUM(VD.VAMT_CR) VAMT_CR,\r\n" +
            "                               SUM(VD.VAMT_DR) VAMT_DR,\r\n" +
            "                               VD.SAC_HEAD_ID SAC_HEAD_ID,\r\n" +
            "                               PAC_HEAD_ID\r\n" +
            "                          FROM VW_VOUCHER_DETAIL VD\r\n" +
            "                         WHERE     VD.VOU_POSTING_DATE BETWEEN :fromDate\r\n" +
            "                                                           AND :toDate\r\n" +
            "                               AND VD.ORGID =:orgId\r\n" +
            "                        GROUP BY VD.AC_HEAD_CODE, VD.SAC_HEAD_ID, PAC_HEAD_ID)\r\n" +
            "                       A\r\n" +
            "                       LEFT JOIN\r\n" +
            "                       (SELECT BG.SAC_HEAD_ID SAC_HEAD_ID1,\r\n" +
            "                               B.AC_HEAD_CODE AC_HEAD_CODE1,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('DR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_DR,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('CR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_CR,\r\n" +
            "                               B.PAC_HEAD_ID\r\n" +
            "                          FROM TB_AC_BUGOPEN_BALANCE BG,\r\n" +
            "                               TB_COMPARAM_DET CD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER B\r\n" +
            "                         WHERE     BG.CPD_ID_DRCR = CD.CPD_ID\r\n" +
            "                               AND BG.ORGID =:orgId\r\n" +
            "                               AND BG.FA_YEARID =:faYearId\r\n" +
            "                               AND B.SAC_HEAD_ID = BG.SAC_HEAD_ID\r\n" +
            "                               AND B.ORGID = BG.ORGID) E\r\n" +
            "                          ON A.SAC_HEAD_ID = E.SAC_HEAD_ID1\r\n" +
            "                UNION\r\n" +
            "                SELECT E.AC_HEAD_CODE,\r\n" +
            "                       E.SAC_HEAD_ID,\r\n" +
            "                       OPENBAL_AMT_DR,\r\n" +
            "                       OPENBAL_AMT_CR,\r\n" +
            "                       VAMT_CR,\r\n" +
            "                       VAMT_DR,\r\n" +
            "                       E.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT VD.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               SUM(VD.VAMT_CR) VAMT_CR,\r\n" +
            "                               SUM(VD.VAMT_DR) VAMT_DR,\r\n" +
            "                               VD.SAC_HEAD_ID SAC_HEAD_ID\r\n" +
            "                          FROM VW_VOUCHER_DETAIL VD\r\n" +
            "                         WHERE     VD.VOU_POSTING_DATE BETWEEN :fromDate\r\n" +
            "                                                           AND :toDate\r\n" +
            "                               AND VD.ORGID =:orgId\r\n" +
            "                        GROUP BY VD.AC_HEAD_CODE, VD.SAC_HEAD_ID) A\r\n" +
            "                       RIGHT JOIN\r\n" +
            "                       (SELECT BG.SAC_HEAD_ID SAC_HEAD_ID1,\r\n" +
            "                               B.AC_HEAD_CODE AC_HEAD_CODE1,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('DR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_DR,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('CR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_CR,\r\n" +
            "                               B.AC_HEAD_CODE,\r\n" +
            "                               B.SAC_HEAD_ID,\r\n" +
            "                               B.PAC_HEAD_ID\r\n" +
            "                          FROM TB_AC_BUGOPEN_BALANCE BG,\r\n" +
            "                               TB_COMPARAM_DET CD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER B\r\n" +
            "                         WHERE     BG.CPD_ID_DRCR = CD.CPD_ID\r\n" +
            "                               AND BG.ORGID =:orgId\r\n" +
            "                               AND BG.FA_YEARID =:faYearId\r\n" +
            "                               AND B.SAC_HEAD_ID = BG.SAC_HEAD_ID\r\n" +
            "                               AND B.ORGID = BG.ORGID) E\r\n" +
            "                          ON A.SAC_HEAD_ID = E.SAC_HEAD_ID1) A,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER B\r\n" +
            "         WHERE A.PAC_HEAD_ID = B.PAC_HEAD_ID) AAA,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER C,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER D\r\n" +
            " WHERE     AAA.PAC_HEAD_ID = C.PAC_HEAD_ID\r\n" +
            "       AND C.PAC_HEAD_PARENT_ID = D.PAC_HEAD_ID\r\n" +
            "       AND C.PAC_HEAD_PARENT_ID =:primaryAcHeadId\r\n" +
            "GROUP BY C.PAC_HEAD_ID, D.PAC_HEAD_ID ", nativeQuery = true)
    List<Object[]> queryLiabilitiesReportData(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("orgId") Long orgId, @Param("primaryAcHeadId") Long primaryAcHeadId, @Param("faYearId") Long faYearId);

    @Query(value = "SELECT C.PAC_HEAD_COMPO_CODE PAC_HEAD_COMPO_CODE,\r\n" +
            "       C.PAC_HEAD_DESC HEAD_DESC,\r\n" +
            "       SUM(AAA.OPENBAL_AMT_DR) - SUM(AAA.OPENBAL_AMT_CR) AS OPENBAL_AMT_DR,\r\n" +
            "       SUM(AAA.VAMT_DR) AS VAMT_DR,\r\n" +
            "       SUM(AAA.VAMT_CR) AS VAMT_CR\r\n" +
            "  FROM (SELECT AC_HEAD_CODE,\r\n" +
            "               SAC_HEAD_ID,\r\n" +
            "               OPENBAL_AMT_DR,\r\n" +
            "               OPENBAL_AMT_CR,\r\n" +
            "               VAMT_CR,\r\n" +
            "               VAMT_DR,\r\n" +
            "               A.PAC_HEAD_ID\r\n" +
            "          FROM (SELECT AC_HEAD_CODE,\r\n" +
            "                       SAC_HEAD_ID,\r\n" +
            "                       OPENBAL_AMT_DR,\r\n" +
            "                       OPENBAL_AMT_CR,\r\n" +
            "                       VAMT_CR,\r\n" +
            "                       VAMT_DR,\r\n" +
            "                       A.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT VD.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               SUM(VD.VAMT_CR) VAMT_CR,\r\n" +
            "                               SUM(VD.VAMT_DR) VAMT_DR,\r\n" +
            "                               VD.SAC_HEAD_ID SAC_HEAD_ID,\r\n" +
            "                               PAC_HEAD_ID\r\n" +
            "                          FROM VW_VOUCHER_DETAIL VD\r\n" +
            "                         WHERE     VD.VOU_POSTING_DATE BETWEEN :fromDate\r\n" +
            "                                                           AND :toDate\r\n" +
            "                               AND VD.ORGID =:orgId\r\n" +
            "                        GROUP BY VD.AC_HEAD_CODE, VD.SAC_HEAD_ID, PAC_HEAD_ID)\r\n" +
            "                       A\r\n" +
            "                       LEFT JOIN\r\n" +
            "                       (SELECT BG.SAC_HEAD_ID SAC_HEAD_ID1,\r\n" +
            "                               B.AC_HEAD_CODE AC_HEAD_CODE1,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('DR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_DR,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('CR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_CR,\r\n" +
            "                               B.PAC_HEAD_ID\r\n" +
            "                          FROM TB_AC_BUGOPEN_BALANCE BG,\r\n" +
            "                               TB_COMPARAM_DET CD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER B\r\n" +
            "                         WHERE     BG.CPD_ID_DRCR = CD.CPD_ID\r\n" +
            "                               AND BG.ORGID =:orgId\r\n" +
            "                               AND BG.FA_YEARID =:faYearId\r\n" +
            "                               AND B.SAC_HEAD_ID = BG.SAC_HEAD_ID\r\n" +
            "                               AND B.ORGID = BG.ORGID) E\r\n" +
            "                          ON A.SAC_HEAD_ID = E.SAC_HEAD_ID1\r\n" +
            "                UNION\r\n" +
            "                SELECT E.AC_HEAD_CODE,\r\n" +
            "                       E.SAC_HEAD_ID,\r\n" +
            "                       OPENBAL_AMT_DR,\r\n" +
            "                       OPENBAL_AMT_CR,\r\n" +
            "                       VAMT_CR,\r\n" +
            "                       VAMT_DR,\r\n" +
            "                       E.PAC_HEAD_ID\r\n" +
            "                  FROM (SELECT VD.AC_HEAD_CODE AC_HEAD_CODE,\r\n" +
            "                               SUM(VD.VAMT_CR) VAMT_CR,\r\n" +
            "                               SUM(VD.VAMT_DR) VAMT_DR,\r\n" +
            "                               VD.SAC_HEAD_ID SAC_HEAD_ID\r\n" +
            "                          FROM VW_VOUCHER_DETAIL VD\r\n" +
            "                         WHERE     VD.VOU_POSTING_DATE BETWEEN :fromDate\r\n" +
            "                                                           AND :toDate\r\n" +
            "                               AND VD.ORGID =:orgId\r\n" +
            "                        GROUP BY VD.AC_HEAD_CODE, VD.SAC_HEAD_ID) A\r\n" +
            "                       RIGHT JOIN\r\n" +
            "                       (SELECT BG.SAC_HEAD_ID SAC_HEAD_ID1,\r\n" +
            "                               B.AC_HEAD_CODE AC_HEAD_CODE1,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('DR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_DR,\r\n" +
            "                               (CASE\r\n" +
            "                                   WHEN CD.CPD_VALUE IN ('CR')\r\n" +
            "                                   THEN\r\n" +
            "                                      BG.OPENBAL_AMT\r\n" +
            "                                   ELSE\r\n" +
            "                                      0\r\n" +
            "                                END)\r\n" +
            "                                  AS OPENBAL_AMT_CR,\r\n" +
            "                               B.AC_HEAD_CODE,\r\n" +
            "                               B.SAC_HEAD_ID,\r\n" +
            "                               B.PAC_HEAD_ID\r\n" +
            "                          FROM TB_AC_BUGOPEN_BALANCE BG,\r\n" +
            "                               TB_COMPARAM_DET CD,\r\n" +
            "                               TB_AC_SECONDARYHEAD_MASTER B\r\n" +
            "                         WHERE     BG.CPD_ID_DRCR = CD.CPD_ID\r\n" +
            "                               AND BG.ORGID =:orgId\r\n" +
            "                               AND BG.FA_YEARID =:faYearId\r\n" +
            "                               AND B.SAC_HEAD_ID = BG.SAC_HEAD_ID\r\n" +
            "                               AND B.ORGID = BG.ORGID) E\r\n" +
            "                          ON A.SAC_HEAD_ID = E.SAC_HEAD_ID1) A,\r\n" +
            "               TB_AC_PRIMARYHEAD_MASTER B\r\n" +
            "         WHERE A.PAC_HEAD_ID = B.PAC_HEAD_ID) AAA,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER C,\r\n" +
            "       TB_AC_PRIMARYHEAD_MASTER D\r\n" +
            " WHERE     AAA.PAC_HEAD_ID = C.PAC_HEAD_ID\r\n" +
            "       AND C.PAC_HEAD_PARENT_ID = D.PAC_HEAD_ID\r\n" +
            "       AND C.PAC_HEAD_PARENT_ID =:primaryAcHeadId\r\n" +
            "GROUP BY C.PAC_HEAD_ID, D.PAC_HEAD_ID\r\n" +
            "ORDER BY PAC_HEAD_COMPO_CODE", nativeQuery = true)
    List<Object[]> queryAssetsReportData(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("orgId") Long orgId, @Param("primaryAcHeadId") Long primaryAcHeadId, @Param("faYearId") Long faYearId);

}
