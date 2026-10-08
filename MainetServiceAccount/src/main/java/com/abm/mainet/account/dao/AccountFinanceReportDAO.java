
package com.abm.mainet.account.dao;

import java.util.Date;
import java.util.List;

import com.abm.mainet.account.domain.VoucherDetailViewEntity;


/**
 * @author prasant.sahu
 *
 */
public interface AccountFinanceReportDAO {
	List<Object[]> getPaymentBookReportData(Long orgid, Date fromDateId, Date toDateId, Long drId, Long crId,
			String fieldId);
	 List<Object[]> getReceiptBookReportData( Long orgid,  Date fromDateId, Date toDateId,
	            Long drId,  Long crId, String fieldId);
	List<VoucherDetailViewEntity> queryReportDataFromViewGeneralBankBook(Date entryDate, Long orgId, Long sacHeadId,
			Date endDate, Long fieldId);
	List<VoucherDetailViewEntity> queryReportDataFromView(Date entryDate, Long orgId, Long sacHeadId, Date outDate,
			Long fieldId);
	List<Object[]> querypaymentChequeReport(Date stringToDate, long orgId, Date stringToDate2, Long accountHeadId,
			Long fieldId);
	List<Object[]> ReceiptsBudgetStatusReport(Date fromDates, Date todates, Long financialId, long orgId, Long deptId,
			Long functionId);
	List<Object[]> queryBankAccountsSummaryReport(Date stringToDate, long orgId, Date stringToDate2,
			Long financialYearId, Long fieldId);
	List<Object[]> findCollectionDetailReportbyByTodateAndFromDateAndOrgIdAndCreatedById(Date toDates, Date fromDates,
			Long orgId, Long fieldId);
	List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgId(Date toDates, Date fromDates, long orgId,
			Long fieldId);
	List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgIdHeadWise(Date toDates, Date fromDates,
			long orgId, Long fieldId);
	List<Object[]> findCollectionSummaryReportByTodateAndFromDateAndOrgIdModeWise(Date toDates, Date fromDates,
			long orgId, Long fieldId);
	List<Object[]> chequeReceivedStatusReportByFieldId(Date stringToDate, Date stringToDate2, long orgId,
			String categoryId, Long fieldId, Long bankAccountId);
}
