package com.abm.mainet.account.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.account.domain.AccountBillEntryMasterEnitity;
import com.abm.mainet.account.dto.VoucherReversalDTO;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.constant.MainetConstants.AccountBillEntry;
import com.abm.mainet.common.constant.QueryConstants;
import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.utility.UtilityService;

/**
 * @author tejas.kotekar
 *
 */
@Repository
public class AccountBillEntryDaoImpl extends AbstractDAO<AccountBillEntryMasterEnitity> implements AccountBillEntryDao {

    private static final Logger LOGGER = Logger.getLogger(AccountBillEntryDaoImpl.class);

    /*
     * (non-Javadoc)
     * @see com.abm.mainetservice.account.dao.AccountBillEntryDao#getBillEntryDetails(java.lang.String, java.lang.String,
     * java.math.BigDecimal, java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<AccountBillEntryMasterEnitity> getBillEntryDetails(final Long orgId, final String fromDate, final String toDate,
            final String billNo,
            final Long billType, final Long vendorId, final Long departmentId) {

        final StringBuilder builder = new StringBuilder();
        builder.append(QueryConstants.MASTERS.AccountBillEntryMaster.QUERY_TO_BILL_ENTRY);

        if (((fromDate != null) && !fromDate.equals(MainetConstants.BLANK))
                || ((toDate != null) && !fromDate.equals(MainetConstants.BLANK))) {

            builder.append(" and m.billEntryDate between :fromDate and :toDate");

        }

        if ((billNo != null) && !billNo.equals(MainetConstants.BLANK)) {

            builder.append(" and m.billNo =:billNo");

        }

        if (billType != null) {

            builder.append(" and m.billTypeId.cpdId =:billType");

        }
        if (vendorId != null) {

            builder.append(" and m.vendorId.vmVendorid =:vendorId");

        }

        if (departmentId != null) {

            builder.append(" and m.departmentId.dpDeptid =:departmentId");

        }

        builder.append(" order by m.id desc");

        final Query query = entityManager.createQuery(builder.toString());

        if (((fromDate != null) && !fromDate.equals(MainetConstants.BLANK))
                || ((toDate != null) && !fromDate.equals(MainetConstants.BLANK))) {

            query.setParameter(MainetConstants.FROM_DATE, UtilityService.convertStringDateToDateFormat(fromDate));
            query.setParameter(MainetConstants.TO_DATE, UtilityService.convertStringDateToDateFormat(toDate));

        }
        if ((billNo != null) && !billNo.equals(MainetConstants.BLANK)) {

            query.setParameter(AccountBillEntry.BILL_NO, billNo);

        }
        if (billType != null) {

            query.setParameter(AccountBillEntry.BILL_TYPE, billType);

        }
        if (vendorId != null) {

            query.setParameter(AccountBillEntry.VENDOR_ID, vendorId);

        }
        if (departmentId != null) {

            query.setParameter(AccountBillEntry.DEPARTMENT_ID, departmentId);

        }
        query.setParameter(MainetConstants.TABLE_COLUMN.ORGANISATION, orgId);
        // @SuppressWarnings(AccountBillEntry.UNCHECKED)
        List<AccountBillEntryMasterEnitity> resultList = null;
        try {
            resultList = query.getResultList();
        } catch (final Exception ex) {
            LOGGER.error("Vendor Id is Not Available", ex);
        }
        return resultList;
    }

    @Override
    public void reverseBillInvoice(final VoucherReversalDTO dto, final Long transactionId, final Long orgId,
            final String ipMacAddress) {
        final Query query = entityManager
                .createQuery(QueryConstants.MASTERS.AccountBillEntryMaster.QUERY_TO_UPDATE_BILLENTRY_MASTER);
        query.setParameter(AccountBillEntry.BILL_DELETION_FLAG, MainetConstants.MENU.Y);
        query.setParameter(AccountBillEntry.BILL_DELETION_ORDER_NO, dto.getApprovalOrderNo());
        query.setParameter(AccountBillEntry.BILL_DELETION_AUTHORIZED_BY, dto.getApprovedBy());
        query.setParameter(AccountBillEntry.BILL_DELETION_DATE, new Date());
        query.setParameter(AccountBillEntry.BILL_DELETION_REMARK, dto.getNarration());
        query.setParameter(AccountBillEntry.UPDATED_BY, dto.getUpdatedBy());
        query.setParameter(AccountBillEntry.UPDATED_DATE, new Date());
        query.setParameter(AccountBillEntry.LG_IP_MAC_ADDRESS_UPDATED, ipMacAddress);
        query.setParameter(AccountBillEntry.ID, transactionId);
        query.setParameter(MainetConstants.TABLE_COLUMN.ORGANISATION, orgId);
        query.setParameter(AccountBillEntry.DELETION_POSTING_DATE, new Date());
        query.executeUpdate();

    }

    @Override
    @Transactional
    public boolean isPaymentDateisExists(Long orgid, Date paymentDate) {
        // TODO Auto-generated method stub
        String queryString = "select te.checkerDate from AccountBillEntryMasterEnitity te where ";
        if (orgid != null) {
            queryString += " te.orgId =:orgid ";
        }
        if ((paymentDate != null)) {
            queryString += " and te.checkerDate <= :paymentDate";
        }
        final Query query = createQuery(queryString);

        if (orgid != null) {
            query.setParameter("orgid",
                    orgid);
        }
        if (paymentDate != null) {
            query.setParameter("paymentDate",
                    paymentDate);
        }
        if (query.getResultList().isEmpty()) {
            return true;
        }
        return false;
    }

	@Override
	public List<Object[]> getBillRegisterDetails(Long orgId, Date fromDates, Date toDates, Long accountHeadId,
			Long fieldId,Long billType) {
		 String accHeadQuery=" ";
		 String fieldQuery1=" ";
		 String accHeadQuery1=" ";
		 String fieldQuery2=" ";
		 String billTypeQry=" ";
		 String billTypeQry1=" ";
		/*
		 * if(accountHeadId!=null && accountHeadId>0) {
		 * accHeadQuery="  AND PD.BUDGETCODE_ID=:accountHeadId\r\n";
		 * accHeadQuery1="  AND TD.BUDGETCODE_ID=:accountHeadId\r\n"; }
		 */
           if(fieldId!=null && fieldId>0) {
           	fieldQuery1="  AND BM.FIELD_ID=:fieldId\r\n";
        	fieldQuery2="  AND A.FIELD_ID=:fieldId\r\n";
           }
           if(billType!=null&&billType>0) {
        	   billTypeQry="   AND BM.BM_BILLTYPE_CPD_ID=:billType\r\n";
        	   billTypeQry1="  AND A.BM_BILLTYPE_CPD_ID=:billType\r\n";
           }
      /* final StringBuilder queryString = new StringBuilder("SELECT Z.BM_BILLNO,\r\n" + 
       		"Z.BM_ENTRYDATE AS BILL_DATE,\r\n" + 
       		"       Z.VM_VENDORNAME,\r\n" + 
       		"       Z.DP_DEPTID,\r\n" + 
       		"       Z.BM_NARRATION,\r\n" + 
       		"       Z.BM_INVOICEVALUE AS BILL_AMOUNT,\r\n" + 
       		"       Z.DISALLOWED_AMT,\r\n" + 
       		"       Z.ACT_AMT AS SANCTIONED_AMT,\r\n" + 
       		"       X.DEDUCTION_AMT,\r\n" + 
       		"       Z.CHECKER_DATE AS SANCTIONED_DATE,\r\n" + 
       		"       Z.VOU_NO,\r\n" + 
       		"       Z.PAYMENT_AMT,\r\n" + 
       		"       Z.DISALLOWED_REMARK,\r\n" + 
       		"       Z.CHECKER_USER AS AUTHORIZED_USER,\r\n" + 
       		"     Z.BM_BILLTYPE_CPD_ID \r\n" + 
       		"  FROM (SELECT A.BM_ID,\r\n" + 
       		"  A.FIELD_ID,\r\n" + 
       		"               A.BM_ENTRYDATE,\r\n" + 
       		"               A.VM_VENDORNAME,\r\n" + 
       		"               A.DP_DEPTID,\r\n" + 
       		"               A.BM_BILLNO,\r\n" + 
       		"               A.BM_INVOICEVALUE,\r\n" + 
       		"               A.BM_NARRATION,\r\n" + 
       		"               A.ACT_AMT,\r\n" + 
       		"               A.CHECKER_DATE,\r\n" + 
       		"               A.VOU_NO,\r\n" + 
       		"               A.CHECKER_USER,\r\n" + 
       		"               B.PAYMENT_AMT,\r\n" + 
       		"               A.DISALLOWED_AMT,\r\n" + 
       		"               A.DISALLOWED_REMARK,\r\n" + 
       		"     A.BM_BILLTYPE_CPD_ID \r\n" + 
       		"          FROM (SELECT A.BM_ID,\r\n" + 
       		"         \r\n" + 
       		"                       A.BM_ENTRYDATE,\r\n" + 
       		"                       A.FIELD_ID,\r\n" + 
       		"                       A.VM_VENDORNAME,\r\n" + 
       		"                       A.DP_DEPTID,\r\n" + 
       		"                       A.BM_BILLNO,\r\n" + 
       		"                       A.BM_INVOICEVALUE,\r\n" + 
       		"                       (D.BCH_CHARGES_AMT) ACT_AMT,\r\n" + 
       		"                       A.BM_NARRATION,\r\n" + 
       		"                       A.CHECKER_DATE,\r\n" + 
       		"                       VH.VOU_NO,\r\n" + 
       		"                       A.CHECKER_USER,\r\n" + 
       		"                       SUM(D.DISALLOWED_AMT) DISALLOWED_AMT,\r\n" + 
       		"                       D.DISALLOWED_REMARK,\r\n" + 
       		"     A.BM_BILLTYPE_CPD_ID \r\n" + 
       		"                  FROM TB_AC_BILL_MAS A,\r\n" + 
       		"                       TB_AC_VOUCHER VH,\r\n" + 
    
       		"                       (SELECT SUM(D.DISALLOWED_AMT) DISALLOWED_AMT,\r\n" + 
       		"                               SUM(D.BCH_CHARGES_AMT) BCH_CHARGES_AMT,\r\n" + 
       		"                               D.BM_ID,\r\n" + 
       		"                               D.DISALLOWED_REMARK\r\n" + 
       		"                          FROM TB_AC_BILL_EXP_DETAIL D\r\n" + 
       		"                        GROUP BY D.BM_ID, D.DISALLOWED_REMARK) D,\r\n" + 
       		"                       TB_VENDORMASTER V\r\n" + 
       		"                 WHERE     A.VM_VENDORID = V.VM_VENDORID\r\n" + 
       		"                       AND A.BM_ID = D.BM_ID\r\n" + 
       		
       		"                       AND VH.VOU_DATE = A.CHECKER_DATE\r\n" + 
       		"                       AND VH.VOU_REFERENCE_NO = A.BM_BILLNO\r\n" + 
       		"                       AND VH.ORGID = A.ORGID\r\n" + 
       		"                       AND VH.DP_DEPTID = A.DP_DEPTID\r\n" + 
       		"                       AND VH.VOU_SUBTYPE_CPD_ID =\r\n" + 
       		"                              (SELECT CPD_ID\r\n" + 
       		"                                 FROM TB_COMPARAM_DET D\r\n" + 
       		"                                WHERE     D.CPM_ID IN\r\n" + 
       		"                                             (SELECT CPM_ID\r\n" + 
       		"                                                FROM TB_COMPARAM_MAS M\r\n" + 
       		"                                               WHERE M.CPM_PREFIX = 'TDP')\r\n" + 
       		"                                      AND CPD_VALUE = 'BI')\r\n" + 
       		"                       AND A.CHECKER_AUTHO = 'Y'\r\n" + 
       		"                       AND A.BM_DEL_FLAG IS NULL\r\n" + 
       		"                      AND A.BM_ENTRYDATE BETWEEN :fromDates \r\n" + 
       		"                                           AND :toDates\r\n" + 
       		"                     AND   A.ORGID =:orgId\r\n" + 
       		fieldQuery2 + 
       		billTypeQry1 +
       		"                GROUP BY A.VM_VENDORNAME,\r\n" + 
       		"                         A.BM_INVOICENUMBER,\r\n" + 
       		"                         A.BM_BILLNO,\r\n" + 
       		"                         A.BM_INVOICEVALUE,\r\n" + 
       		"                         A.BM_NARRATION,\r\n" + 
       		"                         A.CHECKER_DATE,\r\n" + 
       		"                         VH.VOU_NO,\r\n" + 
       		"                         D.DISALLOWED_REMARK,\r\n" + 
       		"                         A.BM_ENTRYDATE,\r\n" + 
       		"                         A.BM_ID,\r\n" + 
       		"                        A.BM_BILLTYPE_CPD_ID,\r\n" + 
       		"  A.FIELD_ID) A\r\n" + 
       		"               LEFT OUTER JOIN\r\n" + 
       		"               (SELECT SUM(PD.PAYMENT_AMT) PAYMENT_AMT, BM.BM_ID \r\n" + 
       		"               ,BM.FIELD_ID\r\n" + 
       		"                  FROM TB_AC_PAYMENT_MAS PM,\r\n" + 
       		"                       TB_AC_PAYMENT_DET PD,\r\n" + 
       		"                       TB_AC_BILL_MAS BM\r\n" + 
       		"                 WHERE     PM.ORGID =:orgId\r\n" + 
       		"                       AND PM.PAYMENT_ID = PD.PAYMENT_ID\r\n" + 
       		"                       AND PD.BM_ID = BM.BM_ID\r\n" + 
       		fieldQuery1+
       		billTypeQry+ 
       		"                       AND PM.PAYMENT_DEL_FLAG IS NULL\r\n" + 
       		"                       AND PM.PAYMENT_DATE  BETWEEN  :fromDates \r\n" + 
       		"                                           AND :toDates\r\n" + 
       		"                GROUP BY BM.BM_ID \r\n" + 
       		"                ,BM.FIELD_ID,BM.BM_BILLTYPE_CPD_ID) B\r\n" + 
       		"                  ON A.BM_ID = B.BM_ID AND  \r\n" + 
       		"  A.FIELD_ID=B.FIELD_ID) Z\r\n" + 
       		"       LEFT JOIN (SELECT SUM(C.DEDUCTION_AMT) DEDUCTION_AMT, C.BM_ID\r\n" + 
       		"                    FROM TB_AC_BILL_DEDUCTION_DETAIL C\r\n" + 
       		"                  GROUP BY C.BM_ID) X\r\n" + 
       		"          ON Z.BM_ID = X.BM_ID\r\n" + 
       		"ORDER BY Z.BM_ENTRYDATE, Z.BM_BILLNO");*/
           
           
           
           
           final StringBuilder queryString = new StringBuilder(
        		    "SELECT " +
        		    "BM.BM_BILLNO BILL_NUMBER, " +
        		    "BM.BM_ENTRYDATE BILL_DATE, " +
        		    "(CASE WHEN VDM.VM_VENDORCODE='V00002662' THEN BM.VM_VENDORNAME ELSE VDM.VM_VENDORNAME END) VM_VENDORNAME, " +
        		    "BM.DP_DEPTID, " +
        		    "BM.BM_NARRATION, " +
        		    "BM_INVOICEVALUE AS BILL_AMOUNT, " +
        		    "BE.DISALLOWED_AMT, " +
        		    "BE.ACT_AMT AS SANCTIONED_AMT, " +
        		    "IFNULL(BDD.DEDUCTION_AMT,0) DEDUCTION_AMT, " +
        		    "BM.CHECKER_DATE AS SANCTIONED_DATE, " +
        		    "VM.VOU_NO, " +
        		    "PM.PAYMENT_AMT, " +
        		    "BE.DISALLOWED_REMARK, " +
        		    "BM.CHECKER_USER AS AUTHORIZED_USER, " +
        		    "BM.BM_BILLTYPE_CPD_ID " +
        		    "FROM tb_ac_bill_mas BM " +
        		    "INNER JOIN lateral (" +
        		        "SELECT BE.BM_ID, BE.DISALLOWED_REMARK, " +
        		        "SUM(BE.BCH_CHARGES_AMT) BCH_CHARGES_AMT, " +
        		        "SUM(BE.DISALLOWED_AMT) DISALLOWED_AMT, " +
        		        "SUM(BE.ACT_AMT) ACT_AMT " +
        		        "FROM tb_ac_bill_exp_detail BE " +
        		        "WHERE BE.BM_ID=BM.BM_ID " +
        		        "GROUP BY BE.BM_ID, BE.DISALLOWED_REMARK" +
        		    ") BE ON BE.BM_ID=BM.BM_ID " +
        		    "LEFT JOIN lateral (" +
        		        "SELECT BDD.BM_ID, SUM(BDD.DEDUCTION_AMT) DEDUCTION_AMT " +
        		        "FROM tb_ac_bill_deduction_detail BDD " +
        		        "WHERE BDD.BM_ID=BM.BM_ID " +
        		        "GROUP BY BDD.BM_ID" +
        		    ") BDD ON BDD.BM_ID=BM.BM_ID " +
        		    "LEFT JOIN lateral (" +
        		        "SELECT PD.BM_ID, SUM(PD.PAYMENT_AMT) PAYMENT_AMT " +
        		        "FROM tb_ac_payment_det PD " +
        		        "INNER JOIN tb_ac_payment_mas PM ON PD.PAYMENT_ID=PM.PAYMENT_ID " +
        		        "WHERE PD.BM_ID=BM.BM_ID " +
        		        "AND PM.PAYMENT_DEL_FLAG IS NULL " +
        		        "GROUP BY PD.BM_ID" +
        		    ") PM ON PM.BM_ID=BM.BM_ID " +
        		    "INNER JOIN tb_ac_voucher VM ON VM.VOU_REFERENCE_NO=BM.BM_BILLNO " +
        		        "AND VM.VOU_DATE=BM.BM_ENTRYDATE " +
        		    "INNER JOIN TB_COMPARAM_DET CPD ON CPD.CPD_ID=VM.VOU_SUBTYPE_CPD_ID " +
        		        "AND CPD.CPD_VALUE = 'BI' " +
        		    "INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=CPD.CPM_ID " +
        		        "AND CPM.CPM_PREFIX = 'TDP' " +
        		    "INNER JOIN tb_vendormaster VDM ON VDM.VM_VENDORID=BM.VM_VENDORID " +
        		    "WHERE BM.BM_ENTRYDATE>=:fromDates " +
        		    "AND BM.BM_ENTRYDATE<DATE_ADD(:toDates, INTERVAL 1 DAY) " +
        		    "AND BM.ORGID=:orgId " +
        		    billTypeQry +
        		    fieldQuery1 +
        		    "AND BM.BM_DEL_FLAG IS NULL"
        		);

       final Query query = entityManager.createNativeQuery(queryString.toString());
      
		/*
		 * if(accountHeadId!=null && accountHeadId>0) {
		 * query.setParameter("accountHeadId", accountHeadId); }
		 */
       if(fieldId!=null && fieldId>0) {
       	query.setParameter("fieldId", fieldId);
       }
       if(billType!=null && billType>0) {
          	query.setParameter("billType", billType);
          }
       query.setParameter("fromDates", fromDates);
       query.setParameter("toDates", toDates);
       query.setParameter("orgId", orgId);
       @SuppressWarnings("unchecked")
       final List<Object[]> listOfEntity = query.getResultList();
       return listOfEntity;
   
	}

	@Override
	public List<Object[]> getOutStandingBillRegisterDetailsnNewWithAccountHead(Long orgId, Date stringToDate,
			Long dpDeptid, Long accountHeadId, Long fieldId,Long billType) {
		String dpDeptidQuery = " ";
		String accountHeadIdQry = " ";
		String fieldIdQry = " ";
		String billTypeQUery=" ";
		if (accountHeadId != null && accountHeadId > 0) {
			accountHeadIdQry = "      where D.SAC_HEAD_ID=:accountHeadId \r\n";
		}
		if (fieldId != null && fieldId > 0) {
			fieldIdQry = "                   AND A.FIELD_ID =:fieldId \r\n";
		}
		if (dpDeptid != null && dpDeptid > 0) {
			dpDeptidQuery = "                   AND A.DP_DEPTID =:dpDeptid \r\n";
		}
		if (billType != null && billType > 0) {
			billTypeQUery = "                   AND A.BM_BILLTYPE_CPD_ID =:billType \r\n";
		}
		final StringBuilder queryString = new StringBuilder("SELECT A.BM_BILLNO, \r\n"
				+ "                   A.VM_VENDORNAME , \r\n"
				+ "                   A.BM_NARRATION, \r\n" + "                   SM.AC_HEAD_CODE, \r\n"
				+ "                   A.CHECKER_DATE, \r\n" + "                   D.BCH_CHARGES_AMT, \r\n"
				+ "                    F.FUND_DESC, \r\n" + "                   A.CHECKER_REMARKS, \r\n"
				+ "                  A.BM_BILLTYPE_CPD_ID \r\n"
				+ "              FROM TB_AC_BILL_MAS A LEFT OUTER JOIN tb_ac_fund_master F ON  \r\n"
				+ "                     A.FUND_ID = F.FUND_ID, \r\n"
				+ "                   TB_AC_SECONDARYHEAD_MASTER SM, \r\n"
				+ "                   (SELECT SUM(D.BCH_CHARGES_AMT) BCH_CHARGES_AMT, D.BM_ID, D.SAC_HEAD_ID \r\n"
				+ "                      FROM TB_AC_BILL_EXP_DETAIL D " + accountHeadIdQry
				+ "                    GROUP BY D.BM_ID, D.SAC_HEAD_ID) D, \r\n"
				+ "                   TB_VENDORMASTER V \r\n"
				+ "             WHERE     A.VM_VENDORID = V.VM_VENDORID \r\n"
				+ "                   AND A.BM_ID = D.BM_ID \r\n"
				+ "                   AND D.SAC_HEAD_ID = SM.SAC_HEAD_ID \r\n"
				+ "                   AND A.CHECKER_AUTHO = 'Y' \r\n"
				+ "                   AND A.BM_PAY_STATUS = 'N' \r\n"
				+ "                   AND A.BM_DEL_FLAG IS NULL \r\n"
				+ "                   AND A.CHECKER_DATE <=:fromdate \r\n"
				+ "                   AND A.ORGID =:orgId \r\n" + dpDeptidQuery + fieldIdQry +billTypeQUery
				+ "            ORDER BY A.CHECKER_DATE, A.BM_BILLNO");

		final Query query = entityManager.createNativeQuery(queryString.toString());

		if (accountHeadId != null && accountHeadId > 0) {
			query.setParameter("accountHeadId", accountHeadId);
		}
		if (fieldId != null && fieldId > 0) {
			query.setParameter("fieldId", fieldId);
		}
		if (dpDeptid != null && dpDeptid > 0) {
			query.setParameter("dpDeptid", dpDeptid);
		}
		if (billType != null && billType > 0) {
			query.setParameter("billType", billType);
		}
		query.setParameter("fromdate", stringToDate);
		query.setParameter("orgId", orgId);
		@SuppressWarnings("unchecked")
		final List<Object[]> listOfEntity = query.getResultList();
		return listOfEntity;

	}

}
