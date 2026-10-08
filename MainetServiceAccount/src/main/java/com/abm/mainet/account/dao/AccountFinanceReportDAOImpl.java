package com.abm.mainet.account.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abm.mainet.account.domain.VoucherDetailViewEntity;
import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.utility.CommonMasterUtility;

@Repository
public class AccountFinanceReportDAOImpl extends AbstractDAO<Long> implements AccountFinanceReportDAO {
	private static final Logger LOGGER = Logger.getLogger(AccountFinanceReportDAOImpl.class);
	@Override
	public List<Object[]> getPaymentBookReportData(Long orgid, Date fromDateId, Date toDateId, Long drId, Long crId,
			String fieldId) {
		 String fieldQuery=" ";
		 String fieldQuery1=" ";
            if(fieldId!=null && !fieldId.isEmpty()) {
            	fieldQuery1="  AND CM.FIELD_ID =:fieldId\r\n";
            	fieldQuery="  AND VM.FIELD_ID =:fieldId\r\n";
            }
        final StringBuilder queryString = new StringBuilder("SELECT DT.VOU_ID,\r\n" + 
        		"       VM.VOU_POSTING_DATE,\r\n" + 
        		"       VM.VOU_NO,\r\n" + 
        		"       SM.AC_HEAD_CODE,\r\n" + 
        		"       VM.NARRATION,\r\n" + 
        		"       DT.VOUDET_AMT,\r\n" + 
        		"       VM.VOU_TYPE_CPD_ID,\r\n" + 
        		"       DT.DRCR_CPD_ID,\r\n" + 
        		"       sm.SAC_HEAD_ID as CR_SAC_HEAD_ID\r\n" + 
        		"  FROM TB_AC_VOUCHER_DET DT, TB_AC_VOUCHER VM, TB_AC_SECONDARYHEAD_MASTER SM\r\n" + 
        		"  WHERE DT.VOU_ID = VM.VOU_ID\r\n" + 
        		"  AND SM.SAC_HEAD_ID = DT.SAC_HEAD_ID\r\n" + 
        		"   AND VM.VOU_POSTING_DATE BETWEEN :fromDateId AND :toDateId\r\n" + 
        		fieldQuery+
        		"  AND DT.DRCR_CPD_ID  =:crId  \r\n" + 
        		"  AND DT.ORGID =:orgid\r\n" + 
        		"  UNION\r\n" + 
        		"  SELECT CT.VOU_ID,\r\n" + 
        		"       CM.VOU_POSTING_DATE,\r\n" + 
        		"       CM.VOU_NO,\r\n" + 
        		"       CSM.AC_HEAD_CODE,\r\n" + 
        		"       CM.NARRATION,\r\n" + 
        		"       CT.VOUDET_AMT,\r\n" + 
        		"       CM.VOU_TYPE_CPD_ID,\r\n" + 
        		"       CT.DRCR_CPD_ID,\r\n" + 
        		"       csm.SAC_HEAD_ID as CR_SAC_HEAD_ID\r\n" + 
        		"  FROM TB_AC_VOUCHER_DET CT, TB_AC_VOUCHER CM, TB_AC_SECONDARYHEAD_MASTER CSM\r\n" + 
        		"  WHERE CT.VOU_ID = CM.VOU_ID\r\n" + 
        		"  AND CSM.SAC_HEAD_ID = CT.SAC_HEAD_ID\r\n" + 
        		"  AND CM.VOU_POSTING_DATE BETWEEN :fromDateId AND :toDateId\r\n" + 
        		fieldQuery1 +
        		"  AND CT.DRCR_CPD_ID =:drId  \r\n" + 
        		"  AND CT.ORGID =:orgid\r\n" + 
        		"  ORDER BY  VOU_ID");

        final Query query = entityManager.createNativeQuery(queryString.toString());
        query.setParameter("fromDateId", fromDateId);
        query.setParameter("toDateId", toDateId);
        query.setParameter("drId", drId);
        query.setParameter("crId", crId);
        query.setParameter("orgid", orgid);
        
        if(fieldId!=null && !fieldId.isEmpty()) {
        	query.setParameter("fieldId", fieldId);
        }
        

        @SuppressWarnings("unchecked")
        final List<Object[]> listOfEntity = query.getResultList();
        return listOfEntity;
    
	}

	@Override
	public List<Object[]> getReceiptBookReportData(Long orgid, Date fromDateId, Date toDateId, Long drId, Long crId,
			String fieldId) {

		 String fieldQuery=" ";
		 String fieldQuery1=" ";
           if(fieldId!=null && !fieldId.isEmpty()) {
           	fieldQuery="  AND CM.FIELD_ID =:fieldId\r\n";
           	fieldQuery1="  AND VM.FIELD_ID =:fieldId\r\n";
           }
       final StringBuilder queryString = new StringBuilder("SELECT DT.VOU_ID,\r\n" + 
       		"       VM.VOU_POSTING_DATE,\r\n" + 
       		"       VM.VOU_NO,\r\n" + 
       		"       SM.AC_HEAD_CODE,\r\n" + 
       		"       VM.NARRATION,\r\n" + 
       		"       DT.VOUDET_AMT,\r\n" + 
       		"       VM.VOU_TYPE_CPD_ID,\r\n" + 
       		"       DT.DRCR_CPD_ID,\r\n" + 
       		"       sm.SAC_HEAD_ID as CR_SAC_HEAD_ID\r\n" + 
       		"  FROM TB_AC_VOUCHER_DET DT, TB_AC_VOUCHER VM, TB_AC_SECONDARYHEAD_MASTER SM\r\n" + 
       		"  WHERE DT.VOU_ID = VM.VOU_ID\r\n" + 
       		"  AND SM.SAC_HEAD_ID = DT.SAC_HEAD_ID\r\n" + 
       		"   AND VM.VOU_POSTING_DATE BETWEEN :fromDateId AND :toDateId\r\n" +
       		fieldQuery1 +
       		"  AND DT.DRCR_CPD_ID  =:crId  \r\n" + 
       		"  AND DT.ORGID =:orgid\r\n" + 
       		"  UNION\r\n" + 
       		"  SELECT CT.VOU_ID,\r\n" + 
       		"       CM.VOU_POSTING_DATE,\r\n" + 
       		"       CM.VOU_NO,\r\n" + 
       		"       CSM.AC_HEAD_CODE,\r\n" + 
       		"       CM.NARRATION,\r\n" + 
       		"       CT.VOUDET_AMT,\r\n" + 
       		"       CM.VOU_TYPE_CPD_ID,\r\n" + 
       		"       CT.DRCR_CPD_ID,\r\n" + 
       		"       csm.SAC_HEAD_ID as CR_SAC_HEAD_ID\r\n" + 
       		"  FROM TB_AC_VOUCHER_DET CT, TB_AC_VOUCHER CM, TB_AC_SECONDARYHEAD_MASTER CSM\r\n" + 
       		"  WHERE CT.VOU_ID = CM.VOU_ID\r\n" + 
       		"  AND CSM.SAC_HEAD_ID = CT.SAC_HEAD_ID\r\n" + 
       		"  AND CM.VOU_POSTING_DATE BETWEEN :fromDateId AND :toDateId\r\n" + 
       		fieldQuery +
       		"  AND CT.DRCR_CPD_ID =:drId  \r\n" + 
       		"  AND CT.ORGID =:orgid\r\n" + 
       		"  ORDER BY  VOU_ID");

       final Query query = entityManager.createNativeQuery(queryString.toString());
       query.setParameter("fromDateId", fromDateId);
       query.setParameter("toDateId", toDateId);
       query.setParameter("drId", drId);
       query.setParameter("crId", crId);
       query.setParameter("orgid", orgid);
       
       if(fieldId!=null && !fieldId.isEmpty()) {
       	query.setParameter("fieldId", fieldId);
       }
       
	
	
       @SuppressWarnings("unchecked")
       final List<Object[]> listOfEntity = query.getResultList();
       return listOfEntity;
   
	}
	@Override
	public List<VoucherDetailViewEntity> queryReportDataFromViewGeneralBankBook(Date entryDate,Long orgId,Long sacHeadId,Date endDate,Long fieldId){


		List<VoucherDetailViewEntity> entityList = null;
		try {

			StringBuilder hql = new StringBuilder("SELECT v1 FROM VoucherDetailViewEntity v1 , VoucherDetailViewEntity v2 "
		            + " WHERE v1.vouNo = v2.vouNo and v1.sacHeadId !=:sacHeadId AND v2.sacHeadId=:sacHeadId AND v2.voPostingDate between  :entryDate and :endDate AND v1.voPostingDate between  :entryDate and :endDate"
		            + "  and v1.vouTypeCpdId not in (select cpdId from TbComparamDetEntity where cpdValue='JV') "
		            + "and v1.orgId=:orgId ");
			if(fieldId!=null && fieldId!=-1 && fieldId !=0) {
				hql.append("  and v1.fieldId=:fieldId ");
			}
           hql.append(" order by v1.voPostingDate,v1.vouId asc )");
			final Query query = createQuery(hql.toString());
			query.setParameter("sacHeadId", sacHeadId);
			query.setParameter("entryDate", entryDate);
			query.setParameter("endDate", endDate);
			query.setParameter("orgId", orgId);
			if(fieldId!=null && fieldId!=-1 && fieldId !=0) {
				query.setParameter("fieldId", fieldId);
			}
			entityList = (List<VoucherDetailViewEntity>) query.getResultList();
		} catch (Exception ex) {
			LOGGER.error("exception occure at the time of fetching queryReportDataFromViewGeneralBankBook");	
		}
		return entityList;
	
		
	}
	@Override
	public List<VoucherDetailViewEntity> queryReportDataFromView(Date entryDate, Long orgId,Long sacHeadId,Date outDate,Long fieldId) {

		List<VoucherDetailViewEntity> entityList = null;
		try {

			StringBuilder hql = new StringBuilder("SELECT v1 FROM VoucherDetailViewEntity v1 , VoucherDetailViewEntity v2 "
		            + " WHERE v1.vouNo = v2.vouNo and v1.sacHeadId !=:sacHeadId AND v2.sacHeadId=:sacHeadId AND v2.voPostingDate between :entryDate and :outDate AND v1.voPostingDate between :entryDate and :outDate"
		            + "  and v1.vouTypeCpdId not in (select cpdId from TbComparamDetEntity where cpdValue='JV') "
		            + "and v1.orgId=:orgId ");
			if(fieldId!=null && fieldId!=-1 && fieldId !=0) {
				hql.append("  and v1.fieldId=:fieldId ");
			}
           hql.append(" order by v1.voPostingDate , v1.vouId asc )");
			final Query query = createQuery(hql.toString());
			query.setParameter("sacHeadId", sacHeadId);
			query.setParameter("entryDate", entryDate);
			query.setParameter("outDate", outDate);
			query.setParameter("orgId", orgId);
			if(fieldId!=null && fieldId!=-1 && fieldId !=0) {
				query.setParameter("fieldId", fieldId);
			}
			entityList = (List<VoucherDetailViewEntity>) query.getResultList();
		} catch (Exception ex) {
			LOGGER.error("exception occure at the time of fetching queryReportDataFromView");	
		}
		return entityList;
	
	}

	@Override
	public List<Object[]> querypaymentChequeReport(Date stringToDate, long orgId, Date stringToDate2,
			Long accountHeadId, Long fieldId) {

		 String fieldQuery=" ";
		 String accountHeadIdstr=" ";
          if(fieldId!=null && fieldId>0) {
          	fieldQuery="  AND PM.FIELDID =:fieldId  \r\n";
          	
          }
          if(accountHeadId!=null && accountHeadId>0) {
        	  accountHeadIdstr="  AND PM.BA_ACCOUNTID=:accountHeadId \r\n";
            	
            }
      final StringBuilder queryString = new StringBuilder("SELECT A.PAYMENT_NO,\r\n" + 
      		"       A.PAYMENT_DATE,\r\n" + 
      		"       A.BM_BILLNO,\r\n" + 
      		"       A.VM_VENDORNAME,\r\n" + 
      		"       A.NARRATION,\r\n" + 
      		"       CASE\r\n" + 
      		"         WHEN (A.TRAN_REFNO IS NULL AND A.CHEQUE_NO IS NOT NULL) THEN\r\n" + 
      		"          A.CHEQUE_NO\r\n" + 
      		"         ELSE\r\n" + 
      		"          A.TRAN_REFNO\r\n" + 
      		"       END AS TRAN_REFNO,\r\n" + 
      		"       A.BM_ENTRYDATE,\r\n" + 
      		"       A.PAYMENT_AMT,\r\n" + 
      		"       A.INSTRUMENT_DATE,\r\n" + 
      		"       A.CHEQUE_CLEARANCE_DATE,\r\n" + 
      		"       A.CREATED_BY,\r\n" + 
      		"       A.VM_VENDORID,\r\n" + 
      		"	   A.BA_ACCOUNT_NO,\r\n" + 
      		"       A.BA_ACCOUNTNAME,\r\n" + 
      		"	   A.BANK BANK_NAME,A.VENDOR_DESC, A.PAYMENT_MODE\r\n" + 
      		"  FROM (SELECT Z.*, X.CHEQUE_NO, ISSUANCE_DATE\r\n" + 
      		"          FROM (SELECT A.PAYMENT_ID,\r\n" + 
      		"                       A.VM_VENDORID,\r\n" + 
      		"                       A.PAYMENT_NO,\r\n" + 
      		"                       A.PAYMENT_DATE,\r\n" + 
      		"                       B.BM_BILLNO,\r\n" + 
      		"                       A.NARRATION,\r\n" + 
      		"                       A.TRAN_REFNO,\r\n" + 
      		"                       B.BM_ENTRYDATE,\r\n" + 
      		"                       B.VM_VENDORNAME,\r\n" + 
      		"                       A.PAYMENT_AMT,\r\n" + 
      		"                       A.INSTRUMENT_DATE,\r\n" + 
      		"                       A.CHEQUE_CLEARANCE_DATE,\r\n" + 
      		"                       A.CREATED_BY,\r\n" + 
      		"					   A.BA_ACCOUNT_NO,\r\n" + 
      		"                       A.BA_ACCOUNTNAME,\r\n" + 
      		"					   A.BANK ,A.VENDOR_DESC ,\r\n" + 
      		"                       A.PAYMENT_MODE\r\n" + 
      		"                  FROM (SELECT PM.VM_VENDORID,\r\n" + 
      		"                               PD.BM_ID,\r\n" + 
      		"                               PM.PAYMENT_ID,\r\n" + 
      		"                               PM.PAYMENT_NO,\r\n" + 
      		"                               PAYMENT_DATE,\r\n" + 
      		"                               PM.NARRATION,\r\n" + 
      		"                               PM.TRAN_REFNO,\r\n" + 
      		"                               BA.BA_ACCOUNT_NO,\r\n" + 
      		"                               BA.BA_ACCOUNTNAME,\r\n" + 
      		"                               BM.BANK,\r\n" + 
      		"                               SUM(PD.PAYMENT_AMT) AS PAYMENT_AMT,\r\n" + 
      		"                               PM.INSTRUMENT_DATE,\r\n" + 
      		"                               PM.CHEQUE_CLEARANCE_DATE,\r\n" + 
      		"                               PM.CREATED_BY,\r\n" + 
      		"PM.VENDOR_DESC ,\r\n" + 
      		"CD.CPD_DESC PAYMENT_MODE\r\n" + 
      		"                                  FROM TB_AC_PAYMENT_MAS PM,TB_AC_PAYMENT_DET PD, tb_bank_account BA,  tb_bank_master BM, tb_comparam_det CD\r\n" + 
      		"                                 WHERE PM.PAYMENT_DEL_FLAG IS NULL\r\n" + 
      		"								   AND PM.ORGID =:orgId AND \r\n" + 
      		"                                   PM.PAYMENT_DATE BETWEEN  :fromDate AND :toDate \r\n" +accountHeadIdstr + fieldQuery +  
      		"                                     AND PM.PAYMENT_ID = PD.PAYMENT_ID\r\n" + 
      		"                                   AND PM.BA_ACCOUNTID=BA.BA_ACCOUNTID AND BA.BANKID=BM.BANKID AND PM.CPD_ID_PAYMENT_MODE=CD.CPD_ID\r\n" + 
      		"                                  GROUP BY PM.VM_VENDORID,\r\n" + 
      		"                                  PM.PAYMENT_ID,\r\n" + 
      		"                                  PM.PAYMENT_NO,\r\n" + 
      		"                                  PD.BM_ID,\r\n" + 
      		"                                  PAYMENT_DATE,\r\n" + 
      		"                                  PM.NARRATION,\r\n" + 
      		"                                  PM.TRAN_REFNO,\r\n" + 
      		"                                  PM.INSTRUMENT_DATE,\r\n" + 
      		"                                  PM.CHEQUE_CLEARANCE_DATE,\r\n" + 
      		"                                  PM.CREATED_BY,\r\n" + 
      		"                                  BA.BA_ACCOUNT_NO,\r\n" + 
      		"                                  BA.BA_ACCOUNTNAME,\r\n" + 
      		"                                  BM.BANK,\r\n" + 
      		"                                  PAYMENT_MODE) A\r\n" + 
      		"                  LEFT JOIN (SELECT BM.BM_ID, BM.BM_BILLNO, BM.BM_ENTRYDATE,BM.VM_VENDORNAME,BM.VM_VENDORID\r\n" + 
      		"                              FROM  TB_AC_BILL_MAS  BM,\r\n" + 
      		"                                   TB_AC_BILL_EXP_DETAIL BD\r\n" + 
      		"                             WHERE BM.BM_ID = BD.BM_ID\r\n" + 
      		"                             GROUP BY BM.BM_ID, BM.BM_BILLNO, BM.BM_ENTRYDATE) B\r\n" + 
      		"                    ON A.BM_ID = B.BM_ID \r\n" + 
      		"                    ) Z\r\n" + 
      		"          LEFT JOIN (SELECT CHEQUE_NO, PAYMENT_ID, ISSUANCE_DATE\r\n" + 
      		"                      FROM TB_AC_CHEQUEBOOKLEAF_DET\r\n" + 
      		"                     WHERE CANCELLATION_DATE IS NULL) X\r\n" + 
      		"            ON Z.PAYMENT_ID = X.PAYMENT_ID) A\r\n" + 
      		"ORDER BY A.PAYMENT_DATE ASC");

      final Query query = entityManager.createNativeQuery(queryString.toString());
     
      
      if(fieldId!=null && fieldId>0) {
      	query.setParameter("fieldId", fieldId);
      }
    
  	query.setParameter("fromDate", stringToDate);
  	query.setParameter("orgId", orgId);
	query.setParameter("toDate", stringToDate2);
	 if(accountHeadId!=null && accountHeadId>0) {
	query.setParameter("accountHeadId", accountHeadId);
	 }
      @SuppressWarnings("unchecked")
      final List<Object[]> listOfEntity = query.getResultList();
      return listOfEntity;
  
	}

	@Override
	public List<Object[]> ReceiptsBudgetStatusReport(Date fromDates, Date todates, Long financialId, long orgId,
			Long deptId, Long functionId) {


		 String funcdQuery=" ";
		 String deptQuery=" ";
		 String deptQuery1=" ";
         if(functionId!=null && functionId>0) {
        	 funcdQuery="        and bmg.FUNCTION_ID =:functionId\r\n";
         	
         }
         if(deptId!=null && deptId>0) {
        	 deptQuery="            AND rm.DP_DEPTID=:deptId\r\n";
        	 deptQuery1="        and pr.DP_DEPTID =:deptId\r\n" ;
          	
          }
     final StringBuilder queryString = new StringBuilder("SELECT\r\n" + 
     		"bmg.budget_code, pr.ORGINAL_ESTAMT, pr.REVISED_ESTAMT, sum(mds.Collected_Balance) as Collected_Balance\r\n" + 
     		"FROM\r\n" + 
     		"tb_ac_budgetcode_mas bmg,\r\n" + 
     		"tb_financialyear fy,\r\n" + 
     		"tb_ac_projectedrevenue pr\r\n" + 
     		"LEFT OUTER JOIN\r\n" + 
     		"(SELECT\r\n" + 
     		"bm.budgetcode_id, bm.budget_code, md.Collected_Balance\r\n" + 
     		"FROM\r\n" + 
     		"tb_ac_budgetcode_mas bm, tb_ac_primaryhead_master pm, Tb_Ac_Function_Master fm, Tb_Ac_Secondaryhead_Master sm,\r\n" + 
     		"(SELECT\r\n" + 
     		"rd.SAC_HEAD_ID,\r\n" + 
     		"rm.orgid,\r\n" + 
     		"SUM(rd.RF_FEEAMOUNT) Collected_Balance\r\n" + 
     		"FROM\r\n" + 
     		"tb_receipt_mas rm, tb_receipt_det rd\r\n" + 
     		"WHERE\r\n" + 
     		"rm.RM_RCPTID = rd.RM_RCPTID\r\n" + 
     		" AND rm.orgid = rd.orgid\r\n" + 
     		"AND rm.ORGID =:orgId\r\n" +deptQuery+ 
     		"AND rm.RM_DATE BETWEEN :fromDate AND :toDate\r\n" + 
     		"AND rm.RECEIPT_DEL_FLAG IS NULL\r\n" + 
     		"GROUP BY rd.SAC_HEAD_ID , rm.orgid) md\r\n" + 
     		"WHERE\r\n" + 
     		"bm.function_id = fm.function_id\r\n" + 
     		"AND bm.pac_head_id = pm.pac_head_id\r\n" + 
     		"AND pm.pac_head_id = sm.pac_head_id\r\n" + 
     		"AND fm.function_id = sm.function_id\r\n" + 
     		"AND sm.sac_head_id = md.sac_head_id\r\n" + 
     		"And bm.SAC_HEAD_ID=Sm.SAC_HEAD_ID) mds ON pr.BUDGETCODE_ID = mds.BUDGETCODE_ID\r\n" + 
     		"WHERE\r\n" + 
     		"bmg.BUDGETCODE_ID = pr.BUDGETCODE_ID\r\n" + 
     		"and bmg.orgid=pr.ORGID\r\n" + 
     		"AND pr.FA_YEARID = fy.FA_YEARID\r\n" + 
     		"AND pr.FA_YEARID =:finYrId\r\n" +deptQuery1+funcdQuery + 
     		"         AND pr.orgid =:orgId\r\n" + 
     		"  AND CPD_ID_STATUS_FLG ='A'"+
     		"	and PR.PR_PROJECTIONID NOT IN(Select PR_PROJECTIONID from tb_ac_projectedrevenue where BUDGETCODE_ID IN\r\n" + 
     		" (Select BUDGETCODE_ID from tb_ac_projectedrevenue group by BUDGETCODE_ID,FA_YEARID having Count(PR_PROJECTIONID)>1) and REVISED_ESTAMT IS NULL)\r\n" + 
     		"         group by bmg.budget_code, pr.ORGINAL_ESTAMT, pr.REVISED_ESTAMT");

     final Query query = entityManager.createNativeQuery(queryString.toString());
    
     
     if(functionId!=null && functionId>0) {
    	 query.setParameter("functionId", functionId);
     	
     }
     if(deptId!=null && deptId>0) {
    	 query.setParameter("deptId", deptId);
      	
      }
   
 	query.setParameter("fromDate", fromDates);
 	query.setParameter("orgId", orgId);
	query.setParameter("toDate", todates);
	query.setParameter("finYrId", financialId);

     @SuppressWarnings("unchecked")
     final List<Object[]> listOfEntity = query.getResultList();
     return listOfEntity;
 
	
	}

	@Override
	public List<Object[]> queryBankAccountsSummaryReport(Date fromDates, long orgId, Date todates,
			Long financialYearId, Long fieldId) {


		 String fieldQuery=" ";
		 String fieldQuery1=" ";
		 String fieldQuery2=" ";
		 String fieldQuery3=" ";
		
       if(fieldId!=null) {
       	fieldQuery="    AND    ba.FIELD_ID=:fieldId \r\n";
       	fieldQuery1="    AND    b.FIELD_ID=:fieldId \r\n";
       	fieldQuery2="    AND    vd.FIELD_ID=:fieldId \r\n";
       	fieldQuery3="    AND    BA.FIELD_ID=:fieldId \r\n";
       	
       }
       
       final StringBuilder queryString = new StringBuilder(
    		    "SELECT BM.BANK, " +
    		    "       BA.BA_ACCOUNT_NO, " +
    		    "       ACCT.CPD_DESC ACCOUNT_TYPE, " +
    		    "       (CASE " +
    		    "            WHEN DRCR.CPD_VALUE='CR' THEN -OP.OPENING_BALANCE " +
    		    "            WHEN DRCR.CPD_VALUE='DR' THEN OP.OPENING_BALANCE " +
    		    "            ELSE 0 " +
    		    "        END) + IFNULL(TR.PT_DR,0) - IFNULL(TR.PT_CR,0) OPENING_BALANCE, " +
    		    "       IFNULL(TR.CT_DR,0) RECEIPTS, " +
    		    "       IFNULL(TR.CT_CR,0) PAYMENTS, " +
    		    "       BA.BA_ACCOUNTNAME " +
    		    "FROM tb_bank_master BM " +
    		    "INNER JOIN tb_bank_account BA " +
    		    "    ON BM.BANKID = BA.BANKID " +
    		    "INNER JOIN tb_comparam_det ACCT " +
    		    "    ON ACCT.CPD_ID = BA.CPD_ACCOUNTTYPE " +
    		    "INNER JOIN tb_ac_secondaryhead_master SM " +
    		    "    ON BA.BA_ACCOUNTID = SM.BA_ACCOUNTID " +
    		    "LEFT JOIN LATERAL ( " +
    		    "    SELECT BOP.SAC_HEAD_ID, " +
    		    "           BOP.CPD_ID_DRCR, " +
    		    "           SUM(BOP.OPENBAL_AMT) OPENING_BALANCE " +
    		    "    FROM tb_ac_bugopen_balance BOP " +
    		    "    INNER JOIN LATERAL ( " +
    		    "        SELECT A.SAC_HEAD_ID, " +
    		    "               MIN(FA_YEARID) FA_YEARID " +
    		    "        FROM tb_ac_bugopen_balance A " +
    		    "        WHERE A.SAC_HEAD_ID = BOP.SAC_HEAD_ID " +
    		    "        GROUP BY A.SAC_HEAD_ID " +
    		    "    ) MIN_YEARID " +
    		    "        ON MIN_YEARID.FA_YEARID = BOP.FA_YEARID " +
    		    "       AND MIN_YEARID.SAC_HEAD_ID = BOP.SAC_HEAD_ID " +
    		    "    WHERE BOP.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "    GROUP BY BOP.SAC_HEAD_ID, BOP.CPD_ID_DRCR " +
    		    ") AS OP " +
    		    "    ON OP.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "LEFT JOIN tb_comparam_det DRCR " +
    		    "    ON DRCR.CPD_ID = OP.CPD_ID_DRCR " +
    		    "LEFT JOIN LATERAL ( " +
    		    "    SELECT VD.SAC_HEAD_ID, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='CR' AND VM.VOU_POSTING_DATE<:fromDate THEN VD.VOUDET_AMT ELSE 0 END) PT_CR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='DR' AND VM.VOU_POSTING_DATE<:fromDate THEN VD.VOUDET_AMT ELSE 0 END) PT_DR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='CR' AND VM.VOU_POSTING_DATE>=:fromDate AND VM.VOU_POSTING_DATE<=:toDate THEN VD.VOUDET_AMT ELSE 0 END) CT_CR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='DR' AND VM.VOU_POSTING_DATE>=:fromDate AND VM.VOU_POSTING_DATE<=:toDate THEN VD.VOUDET_AMT ELSE 0 END) CT_DR " +
    		    "    FROM tb_ac_voucher_det VD " +
    		    "    INNER JOIN tb_ac_voucher VM " +
    		    "        ON VM.VOU_ID = VD.VOU_ID " +
    		    "    INNER JOIN tb_comparam_det DRCR_CPD " +
    		    "        ON VD.DRCR_CPD_ID = DRCR_CPD.CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas DRCR_CPM " +
    		    "        ON DRCR_CPM.CPM_ID = DRCR_CPD.CPM_ID " +
    		    "       AND DRCR_CPM.CPM_PREFIX = 'DCR' " +
    		    "    INNER JOIN tb_comparam_det VT_CPD " +
    		    "        ON VT_CPD.CPD_ID = VM.VOU_TYPE_CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas VT_CPM " +
    		    "        ON VT_CPM.CPM_ID = VT_CPM.CPM_ID " +
    		    "       AND VT_CPM.CPM_PREFIX = 'VOT' " +
    		    "    INNER JOIN tb_comparam_det VST_CPD " +
    		    "        ON VST_CPD.CPD_ID = VM.VOU_SUBTYPE_CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas VST_CPM " +
    		    "        ON VST_CPM.CPM_ID = VST_CPD.CPM_ID " +
    		    "       AND VST_CPM.CPM_PREFIX = 'TDP' " +
    		    "    WHERE VD.ORGID = :orgId " +
    		    "      AND VD.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "      AND VM.VOU_POSTING_DATE <= :toDate " +
    		    "      AND (CASE " +
    		    "               WHEN VT_CPD.CPD_VALUE='CV' " +
    		    "                AND VST_CPD.CPD_VALUE IN ('DS','DSR','BTE') " +
    		    "               THEN NOT EXISTS ( " +
    		    "                        SELECT 1 " +
    		    "                        FROM tb_ac_bank_depositslip_master BDM " +
    		    "                        WHERE VM.VOU_REFERENCE_NO = BDM.DPS_SLIPNO " +
    		    "                          AND VM.VOU_DATE = BDM.DPS_SLIPDATE " +
    		    "                          AND BDM.DPS_DEL_FLAG IS NOT NULL " +
    		    "                    ) " +
    		    "               ELSE VT_CPD.CPD_VALUE IN ('CV','JV','PV','RV') " +
    		    "           END) " +
    		    "    GROUP BY VD.SAC_HEAD_ID " +
    		    ") AS TR " +
    		    "    ON TR.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "WHERE 0 >= :fieldId " +

    		    "UNION ALL " +

    		    "SELECT BM.BANK, " +
    		    "       BA.BA_ACCOUNT_NO, " +
    		    "       ACCT.CPD_DESC ACCOUNT_TYPE, " +
    		    "       (CASE " +
    		    "            WHEN DRCR.CPD_VALUE='CR' THEN -OP.OPENING_BALANCE " +
    		    "            WHEN DRCR.CPD_VALUE='DR' THEN OP.OPENING_BALANCE " +
    		    "            ELSE 0 " +
    		    "        END) + IFNULL(TR.PT_DR,0) - IFNULL(TR.PT_CR,0) OPENING_BALANCE, " +
    		    "       IFNULL(TR.CT_DR,0) RECEIPTS, " +
    		    "       IFNULL(TR.CT_CR,0) PAYMENTS, " +
    		    "       BA.BA_ACCOUNTNAME " +
    		    "FROM tb_bank_master BM " +
    		    "INNER JOIN tb_bank_account BA " +
    		    "    ON BM.BANKID = BA.BANKID " +
    		    "INNER JOIN tb_comparam_det ACCT " +
    		    "    ON ACCT.CPD_ID = BA.CPD_ACCOUNTTYPE " +
    		    "INNER JOIN tb_ac_secondaryhead_master SM " +
    		    "    ON BA.BA_ACCOUNTID = SM.BA_ACCOUNTID " +
    		    "LEFT JOIN LATERAL ( " +
    		    "    SELECT BOP.SAC_HEAD_ID, " +
    		    "           BOP.CPD_ID_DRCR, " +
    		    "           SUM(BOP.OPENBAL_AMT) OPENING_BALANCE " +
    		    "    FROM tb_ac_bugopen_balance BOP " +
    		    "    INNER JOIN LATERAL ( " +
    		    "        SELECT A.SAC_HEAD_ID, " +
    		    "               MIN(FA_YEARID) FA_YEARID " +
    		    "        FROM tb_ac_bugopen_balance A " +
    		    "        WHERE A.SAC_HEAD_ID = BOP.SAC_HEAD_ID " +
    		    "        GROUP BY A.SAC_HEAD_ID " +
    		    "    ) MIN_YEARID " +
    		    "        ON MIN_YEARID.FA_YEARID = BOP.FA_YEARID " +
    		    "       AND MIN_YEARID.SAC_HEAD_ID = BOP.SAC_HEAD_ID " +
    		    "    WHERE BOP.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "    GROUP BY BOP.SAC_HEAD_ID, BOP.CPD_ID_DRCR " +
    		    ") AS OP " +
    		    "    ON OP.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "LEFT JOIN tb_comparam_det DRCR " +
    		    "    ON DRCR.CPD_ID = OP.CPD_ID_DRCR " +
    		    "LEFT JOIN LATERAL ( " +
    		    "    SELECT VD.SAC_HEAD_ID, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='CR' AND VM.VOU_POSTING_DATE<:fromDate THEN VD.VOUDET_AMT ELSE 0 END) PT_CR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='DR' AND VM.VOU_POSTING_DATE<:fromDate THEN VD.VOUDET_AMT ELSE 0 END) PT_DR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='CR' AND VM.VOU_POSTING_DATE>=:fromDate AND VM.VOU_POSTING_DATE<=:toDate THEN VD.VOUDET_AMT ELSE 0 END) CT_CR, " +
    		    "           SUM(CASE WHEN DRCR_CPD.CPD_VALUE='DR' AND VM.VOU_POSTING_DATE>=:fromDate AND VM.VOU_POSTING_DATE<=:toDate THEN VD.VOUDET_AMT ELSE 0 END) CT_DR " +
    		    "    FROM tb_ac_voucher_det VD " +
    		    "    INNER JOIN tb_ac_voucher VM " +
    		    "        ON VM.VOU_ID = VD.VOU_ID " +
    		    "    INNER JOIN tb_comparam_det DRCR_CPD " +
    		    "        ON VD.DRCR_CPD_ID = DRCR_CPD.CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas DRCR_CPM " +
    		    "        ON DRCR_CPM.CPM_ID = DRCR_CPD.CPM_ID " +
    		    "       AND DRCR_CPM.CPM_PREFIX = 'DCR' " +
    		    "    INNER JOIN tb_comparam_det VT_CPD " +
    		    "        ON VT_CPD.CPD_ID = VM.VOU_TYPE_CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas VT_CPM " +
    		    "        ON VT_CPM.CPM_ID = VT_CPM.CPM_ID " +
    		    "       AND VT_CPM.CPM_PREFIX = 'VOT' " +
    		    "    INNER JOIN tb_comparam_det VST_CPD " +
    		    "        ON VST_CPD.CPD_ID = VM.VOU_SUBTYPE_CPD_ID " +
    		    "    INNER JOIN tb_comparam_mas VST_CPM " +
    		    "        ON VST_CPM.CPM_ID = VST_CPD.CPM_ID " +
    		    "       AND VST_CPM.CPM_PREFIX = 'TDP' " +
    		    "    WHERE VD.ORGID = :orgId " +
    		    "      AND VD.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "      AND VM.VOU_POSTING_DATE <= :toDate " +
    		    "      AND (CASE " +
    		    "               WHEN VT_CPD.CPD_VALUE='CV' " +
    		    "                AND VST_CPD.CPD_VALUE IN ('DS','DSR','BTE') " +
    		    "               THEN NOT EXISTS ( " +
    		    "                        SELECT 1 " +
    		    "                        FROM tb_ac_bank_depositslip_master BDM " +
    		    "                        WHERE VM.VOU_REFERENCE_NO = BDM.DPS_SLIPNO " +
    		    "                          AND VM.VOU_DATE = BDM.DPS_SLIPDATE " +
    		    "                          AND BDM.DPS_DEL_FLAG IS NOT NULL " +
    		    "                    ) " +
    		    "               ELSE VT_CPD.CPD_VALUE IN ('CV','JV','PV','RV') " +
    		    "           END) " +
    		    "    GROUP BY VD.SAC_HEAD_ID " +
    		    ") AS TR " +
    		    "    ON TR.SAC_HEAD_ID = SM.SAC_HEAD_ID " +
    		    "WHERE BA.FIELD_ID = :fieldId ORDER BY BANK"
    		);

   final Query query = entityManager.createNativeQuery(queryString.toString());
  
   
   
 
	query.setParameter("fromDate", fromDates);
	query.setParameter("orgId", orgId);
	query.setParameter("toDate", todates);
	//query.setParameter("finYrId", financialYearId);
	if(fieldId!=null)
		query.setParameter("fieldId", fieldId);

   @SuppressWarnings("unchecked")
   final List<Object[]> listOfEntity = query.getResultList();
   return listOfEntity;

	
	}

	@Override
	public List<Object[]> findCollectionDetailReportbyByTodateAndFromDateAndOrgIdAndCreatedById(Date toDates,
			Date fromDates, Long orgId, Long fieldId) {


		 String fieldQuery=" ";
		
		
       if(fieldId!=null && fieldId>0) {
       	fieldQuery="       AND r.FIELD_ID=:fieldId \r\n";
       	
       }
       
   final StringBuilder queryString = new StringBuilder("SELECT \r\n" + 
   		"    r.RM_RCPTID,\r\n" + 
   		"    r.RM_RCPTNO,\r\n" + 
   		"    r.rm_date,\r\n" + 
   		"    m.CPD_FEEMODE,\r\n" + 
   		"    r.RM_RECEIVEDFROM,\r\n" + 
   		"    m.RD_CHEQUEDDNO,\r\n" + 
   		"    SUM(d.RF_FEEAMOUNT) AS Receipt_Amount,\r\n" + 
   		"    r.RM_NARRATION,\r\n" + 
   		"   r.DP_DEPTID,\r\n" + 
   		"   s.DPS_SLIPNO FROM\r\n" + 
   		"    tb_receipt_mas r,\r\n" + 
   		"    tb_receipt_det d,\r\n" + 
   		"    tb_receipt_mode m,\r\n" + 
   		"    tb_comparam_mas cm,\r\n" + 
   		"    tb_comparam_det cd,\r\n" +
   		"    tb_ac_bank_depositslip_master s\r\n" + 
   		" WHERE\r\n" + 
   		"    r.RM_RCPTID = d.RM_RCPTID\r\n" + 
   		"        AND r.RM_RCPTID = m.RM_RCPTID\r\n" + 
   		"        AND d.RM_RCPTID = m.RM_RCPTID\r\n" + 
   		"        AND cm.CPM_ID = cd.CPM_ID\r\n" + 
   		"        AND cd.CPD_ID = m.CPD_FEEMODE" + 
        "        AND s.DPS_SLIPID = d.DPS_SLIPID\r\n" + 
   		"        AND r.RECEIPT_DEL_FLAG is null        AND r.RECEIPT_TYPE_FLAG in ('M','R','A','P','INV')\r\n" + 
   		"        AND r.ORGID=:orgId\r\n" + 
   		fieldQuery + 
   		"       AND date(rm_date) BETWEEN :fromDate AND :toDate\r\n" + 
   		"GROUP BY r.RM_RCPTID , r.RM_RCPTNO , r.rm_date , m.CPD_FEEMODE , r.RM_RECEIVEDFROM , m.RD_CHEQUEDDNO,r.RM_NARRATION,r.DP_DEPTID, s.DPS_SLIPNO\r\n" + 
   		" order by r.rm_date,r.RM_RCPTNO asc");

   final Query query = entityManager.createNativeQuery(queryString.toString());
  
   
   
 
	query.setParameter("fromDate", fromDates);
	query.setParameter("orgId", orgId);
	query.setParameter("toDate", toDates);
	if(fieldId!=null && fieldId>0)
		query.setParameter("fieldId", fieldId);

   @SuppressWarnings("unchecked")
   final List<Object[]> listOfEntity = query.getResultList();
   return listOfEntity;

	
	}

	@Override
	public List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgId(Date toDates, Date fromDates,
			long orgId, Long fieldId) {



		 String fieldQuery=" ";
		
		
      if(fieldId!=null && fieldId>0) {
      	fieldQuery="       AND r.FIELD_ID=:fieldId \r\n";
      	
      }
      
  final StringBuilder queryString = new StringBuilder("SELECT  DP.DP_DEPTDESC,     d.SAC_HEAD_ID,\r\n" + 
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
  		fieldQuery+
  		"           AND r.RM_DATE BETWEEN :fromDate AND :toDate  \r\n" + 
  		"       Group By d.SAC_HEAD_ID,sm.AC_HEAD_CODE,DP.DP_DEPTDESC");

  final Query query = entityManager.createNativeQuery(queryString.toString());
 
  
  

	query.setParameter("fromDate", fromDates);
	query.setParameter("orgId", orgId);
	query.setParameter("toDate", toDates);
	if(fieldId!=null && fieldId>0)
		query.setParameter("fieldId", fieldId);

  @SuppressWarnings("unchecked")
  final List<Object[]> listOfEntity = query.getResultList();
  return listOfEntity;

	
	
	}

	@Override
	public List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgIdModeWise(Date toDates, Date fromDates,
			long orgId, Long fieldId) {

		String fieldQuery = " ";

		if (fieldId != null && fieldId > 0) {
			fieldQuery = "       AND RM.FIELD_ID=:fieldId \r\n";

		}

		final StringBuilder queryString = new StringBuilder(
				"Select (select cpd_desc from tb_comparam_det where cpd_id=cpd_feemode) mode, sum(RD_AMOUNT) from tb_receipt_mas RM,tb_receipt_mode RMO where  RM.RM_RCPTID=RMO.RM_RCPTID and  coalesce(RM.RECEIPT_DEL_FLAG,'N')='N' "+fieldQuery+"AND RM.RECEIPT_TYPE_FLAG in ('M','R','A','P') AND RM.RM_DATE BETWEEN :fromDate AND :toDate GROUP BY mode;");

		final Query query = entityManager.createNativeQuery(queryString.toString());

		query.setParameter("fromDate", fromDates);
		// query.setParameter("orgId", orgId);
		query.setParameter("toDate", toDates);
		if (fieldId != null && fieldId > 0)
			query.setParameter("fieldId", fieldId);

		@SuppressWarnings("unchecked")
		final List<Object[]> listOfEntity = query.getResultList();
		return listOfEntity;

	}

	@Override
	public List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgIdHeadWise(Date toDates, Date fromDates,
			long orgId, Long fieldId) {



		 String fieldQuery=" ";
		
		
      if(fieldId!=null && fieldId>0) {
      	fieldQuery="       AND RM.FIELD_ID=:fieldId \r\n";
      	
      }
      
		final StringBuilder queryString = new StringBuilder(
				"Select SM.SAC_HEAD_DESC,Sum(RF_FEEAMOUNT) from tb_receipt_mas RM,tb_receipt_det RD, tb_ac_secondaryhead_master SM where RM.RM_RCPTID=RD.RM_RCPTID" +fieldQuery
						+ "and SM.SAC_HEAD_ID=RD.SAC_HEAD_ID and RECEIPT_DEL_FLAG IS NULL and RM_DATE between :fromDate and :toDate  Group by SM.SAC_HEAD_DESC;");

  final Query query = entityManager.createNativeQuery(queryString.toString());
 
	query.setParameter("fromDate", fromDates);
	//query.setParameter("orgId", orgId);
	query.setParameter("toDate", toDates);
	if(fieldId!=null && fieldId>0)
		query.setParameter("fieldId", fieldId);

  @SuppressWarnings("unchecked")
  final List<Object[]> listOfEntity = query.getResultList();
  return listOfEntity;

	
	
	}

	@Override
	public List<Object[]> chequeReceivedStatusReportByFieldId(Date fromDates, Date toDates, long orgId,
			String categoryId, Long fieldId, Long bankAccountId) {

       Long dishnrId=CommonMasterUtility.lookUpIdByLookUpCodeAndPrefix("DSH", "CLR", orgId);

		 String fieldQuery=" ";
		 String bankAccountIdQry=" ";
		
     if(fieldId!=null && fieldId>0) {
     	fieldQuery="       AND RM.FIELD_ID=:fieldId \r\n";
     	
     }
     if(bankAccountId!=null && bankAccountId>0) {
    	 bankAccountIdQry="       AND RMO.BANKID=:bankAccountId \r\n";
     }
     
		final StringBuilder queryString = new StringBuilder("SELECT \r\n" + 
				"    RM.RM_RCPTID,\r\n" + 
				"    RM.ORGID,\r\n" + 
				"    RM.RM_RCPTNO RECEIPT_NO,\r\n" + 
				"    RM.RM_DATE RECEIPT_DATE,\r\n" + 
				"    RM.RM_RECEIVEDFROM,\r\n" + 
				"    RMO.RD_CHEQUEDDNO CHEQUE_NO,\r\n" + 
				"    RMO.RD_CHEQUEDDDATE,\r\n" + 
				"    RMO.RD_CHEQUE_STATUS,\r\n" + 
				"    RMO.RD_AMOUNT,\r\n" + 
				"    BM.BANK,\r\n" + 
				"    (SELECT \r\n" + 
				"            CPD_DESC\r\n" + 
				"        FROM\r\n" + 
				"            TB_COMPARAM_DET A,\r\n" + 
				"            TB_COMPARAM_MAS B\r\n" + 
				"        WHERE\r\n" + 
				"            B.CPM_ID = A.CPM_ID\r\n" + 
				"                AND CPM_PREFIX = 'CLR'\r\n" + 
				"                AND CPD_ID = RMO.RD_CHEQUE_STATUS) AS Status,\r\n" + 
				"    RM.DP_DEPTID,\r\n" + 
				"    RMO.RD_SR_CHK_DATE,\r\n" + 
				"    BA.BA_ACCOUNT_NO\r\n" + 
				"FROM\r\n" + 
				"    TB_RECEIPT_MAS RM,\r\n" + 
				"    TB_RECEIPT_MODE RMO,\r\n" + 
				"    TB_BANK_MASTER BM,\r\n" + 
				"    tb_ac_bank_depositslip_master BDM,\r\n" + 
				"    tb_bank_account BA\r\n" + 
				"WHERE\r\n" + 
				"    RM.RM_RCPTID = RMO.RM_RCPTID\r\n" + 
				"        AND BDM.BA_ACCOUNTID = BA.BA_ACCOUNTID\r\n" + 
				"        AND BM.BANKID = BA.BANKID\r\n" + 
				"        AND RM.ORGID = RMO.ORGID\r\n" +  bankAccountIdQry+ 
				"        AND RM.ORGID =:orgId\r\n" + 
				"        AND RMO.RD_CHEQUE_STATUS =:categoryId \r\n" +  fieldQuery+ 
				"        AND (CASE\r\n" + 
				"        WHEN\r\n" + 
				"            RMO.RD_CHEQUE_STATUS!="+dishnrId+"\r\n" + 
				"        THEN\r\n" + 
				"            (RM.RECEIPT_DEL_FLAG IS NULL\r\n" + 
				"                AND (RMO.RD_SR_CHK_DIS = 'N'\r\n" + 
				"                OR RD_SR_CHK_DIS IS NULL))\r\n" + 
				"        ELSE (RM.RECEIPT_DEL_FLAG = 'Y'\r\n" + 
				"            OR RMO.RD_SR_CHK_DIS = 'Y')\r\n" + 
				"    END)\r\n" + 
				"        AND BDM.DPS_SLIPID IN (SELECT DISTINCT\r\n" + 
				"            DPS_SLIPID\r\n" + 
				"        FROM\r\n" + 
				"            tb_receipt_det\r\n" + 
				"        WHERE\r\n" + 
				"            RM_RCPTID = RM.RM_RCPTID)\r\n" + 
				"        AND RM.RM_DATE BETWEEN :fromDates AND :todates");

 final Query query = entityManager.createNativeQuery(queryString.toString());

	query.setParameter("fromDates", fromDates);
	query.setParameter("orgId", orgId);
	query.setParameter("todates", toDates);
	query.setParameter("categoryId", categoryId);
	if(fieldId!=null && fieldId>0)
		query.setParameter("fieldId", fieldId);
	if(bankAccountId!=null&&bankAccountId>0)
		query.setParameter("bankAccountId", bankAccountId);
 @SuppressWarnings("unchecked")
 final List<Object[]> listOfEntity = query.getResultList();
 return listOfEntity;

	
	
	}
}
