package com.abm.mainet.common.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.dto.ReceiptSummaryDTO;
import com.abm.mainet.common.integration.acccount.domain.TbServiceReceiptMasEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptFeesDetEntity;
import com.abm.mainet.common.integration.acccount.domain.TbSrcptModesDetEntity;

@Repository
public interface ReceiptRepository extends JpaRepository<TbServiceReceiptMasEntity, Long> {

    TbServiceReceiptMasEntity findByRmRcptidAndOrgId(Long refId, long orgid);

    @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='R'")
    BigDecimal getPaidAmountByAppNo(@Param("orgId") Long orgId, @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);

    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='RB' ")
    List<TbServiceReceiptMasEntity> getAllActiveRebetReceiptByAppNo(@Param("orgId") Long orgId,
            @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);

    @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='AR'")
    BigDecimal getAjustedAdvancedAmountByAppNo(@Param("orgId") Long orgId, @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);

    @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo")
    BigDecimal getPaidAmountByAdditionalRefNo(@Param("orgId") Long orgId, @Param("additionalRefNo") String additionalRefNo,
            @Param("deptId") Long deptId);

    @Query("select r from TbServiceReceiptMasEntity r where r.receiptDelFlag is null and r.rmRcptid = (select max(rec.rmRcptid) from TbServiceReceiptMasEntity rec where"
            +
            " rec.orgId =:orgId  and rec.additionalRefNo =:additionalRefNo and receiptTypeFlag='R') ")
    TbServiceReceiptMasEntity getLatestReceiptDetailByAddRefNo(@Param("orgId") Long orgId,
            @Param("additionalRefNo") String additionalRefNo);

    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='RB' and r.receiptDelFlag is null")
    List<TbServiceReceiptMasEntity> getRebateByAppNo(@Param("orgId") Long orgId, @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);

    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.receiptDelFlag is null")
    List<TbServiceReceiptMasEntity> getCollectionDetails(@Param("additionalRefNo") String additionalRefNo,
            @Param("deptId") Long deptId, @Param("orgId") long orgid);
    
    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.receiptDelFlag='Y' ")
    List<TbServiceReceiptMasEntity> getCollectionDetailsInactive(@Param("additionalRefNo") String additionalRefNo,
            @Param("deptId") Long deptId, @Param("orgId") long orgid);

    @Query("select distinct rm.rmReceivedfrom from TbServiceReceiptMasEntity rm where rm.orgId =:orgId and rm.dpDeptId =:deptId ")
    List<String> getPayeeNames(@Param("orgId") Long orgId, @Param("deptId") Long deptId);

    @Query("select rm from TbServiceReceiptMasEntity rm where rm.dpDeptId =:deptId and  rm.orgId =:orgId and rm.apmApplicationId is null and rm.smServiceId is null ORDER BY rmRcptno DESC")
    List<TbServiceReceiptMasEntity> getAllReceiptsByOrgId(@Param("deptId") Long deptId, @Param("orgId") Long orgId);

    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.rmRcptno =:rcptNo and r.orgId =:orgId")
    TbServiceReceiptMasEntity getReceiptDetailByRcptNoAndOrgId(@Param("rcptNo") long rcptNo, @Param("orgId") Long orgId);

    @Query("select count(r) from TbServiceReceiptMasEntity r where " +
            "r.additionalRefNo =:additionalRefNo and r.orgId =:orgId")
    int getReceiptDetailByRcptNoAndOrgId(@Param("additionalRefNo") String additionalRefNo,
            @Param("orgId") Long orgId);

    @Query("FROM TbServiceReceiptMasEntity r WHERE r.rmLoiNo =:rmLoiNo AND r.orgId =:orgId")
    TbServiceReceiptMasEntity getReceiptNoByLoiNoAndOrgId(@Param("rmLoiNo") String rmLoiNo, @Param("orgId") Long orgId);

    @Query("SELECT r FROM TbServiceReceiptMasEntity r WHERE " +
            "r.dpDeptId =:deptId AND r.orgId =:orgId  AND r.rmRcptno =:receiptNo AND r.rmDate=:receiptDate AND (r.receiptDelFlag IS NULL OR r.receiptDelFlag <> 'Y')")
    List<TbServiceReceiptMasEntity> getReceiptByDeptAndDate(@Param("receiptNo") Long receiptNo, @Param("orgId") Long orgId,
            @Param("receiptDate") Date receiptDate,
            @Param("deptId") Long deptId);

    @Query("select  count(1) from TbServiceReceiptMasEntity a where a.additionalRefNo=:additionalRefNo and  a.orgId=:orgId and a.dpDeptId =:deptId  and (a.receiptDelFlag <> 'Y' or a.receiptDelFlag is null) and rm_date >(select r.rmDate from TbServiceReceiptMasEntity r where r.rmRcptid =:rmRcptid and  (a.receiptDelFlag <> 'Y' or a.receiptDelFlag is null))")
    int getCountAllNextGeneratedReceipt(@Param("additionalRefNo") String additionalRefNo,
            @Param("orgId") long orgId, @Param("rmRcptid") long rmRcptid, @Param("deptId") Long deptId);

    @Modifying
    @Query("update TbServiceReceiptMasEntity u set u.receiptDelFlag =:status,u.receiptDelRemark =:receiptDelRemark,u.updatedBy=:userId,u.receiptDelDate=:deleteDate,u.updatedDate=:deleteDate,u.lgIpMacUpd=:updatedIPAddress,u.receipt_del_auth_by=:userId where u.rmRcptid =:rmRcptid and u.orgId=:orgId and u.dpDeptId =:deptId")
    int updateSetForReceiptDelFlag(@Param("status") String status,
            @Param("rmRcptid") long rmRcptid, @Param("orgId") long orgId, @Param("receiptDelRemark") String receiptDelRemark,
            @Param("userId") long userId, @Param("deptId") Long deptId, @Param("updatedIPAddress") String updatedIPAddress,
            @Param("deleteDate") Date deleteDate);

    @Query("select count(d) from TbSrcptFeesDetEntity d where d.taxId=:taxId and rmRcptid in(select r.rmRcptid  from TbServiceReceiptMasEntity r where "
            +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='R') ")
    int getcountOfTaxExistAgainstAppId(@Param("orgId") Long orgId, @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId, @Param("taxId") Long taxId);
    
    
    @Query("select r from TbServiceReceiptMasEntity r where r.apmApplicationId=:applicationNo  and r.orgId=:orgId")
    TbServiceReceiptMasEntity getReceiptDetailsByAppId(@Param("applicationNo") Long applicationNo,@Param("orgId") Long orgId);

    @Query("SELECT SUM(rcp.rmAmount) FROM TbServiceReceiptMasEntity rcp WHERE rcp.refId=:refId AND rcp.receiptTypeFlag=:receiptType AND rcp.orgId=:orgId AND rcp.receiptDelFlag is null")
    Long getTotalReceiptAmountByRefIdAndReceiptType(@Param("refId") Long refId, @Param("receiptType") String receiptType, @Param("orgId") Long orgId);
    
    @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='R' and r.receiptDelFlag is null")
    BigDecimal getActivePaidAmountByAppNo(@Param("orgId") Long orgId, @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);
    
    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.apmApplicationId =:applicationNo and r.receiptTypeFlag='R' ")
    List<TbServiceReceiptMasEntity> getAllActiveDemandReceiptByAppNo(@Param("orgId") Long orgId,
            @Param("applicationNo") Long applicationNo,
            @Param("deptId") Long deptId);
    
    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.refId =:refId and r.rmDate=:receiptDate and r.receiptTypeFlag=:receiptType and r.receiptDelFlag is null ")
    List<TbServiceReceiptMasEntity> getReceiptByDeptAndDateAndReceiptType(@Param("refId") Long refId, @Param("orgId") Long orgId,
            @Param("receiptDate") Date receiptDate,
            @Param("deptId") Long deptId, @Param("receiptType") String receiptType);
    
    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.rmDate=:receiptDate and r.receiptDelFlag is null ")
    List<TbServiceReceiptMasEntity> getReceiptByDeptAndDateAddRefNo(@Param("additionalRefNo") String additionalRefNo, @Param("orgId") Long orgId,
            @Param("receiptDate") Date receiptDate,
            @Param("deptId") Long deptId);
    
	@Query("select  count(a) from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and a.receiptTypeFlag='R' and  a.orgId=:orgId and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
	Long getCountOfbillPaidBetweenCurFinYear(@Param("propNo") String propNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId);
	
	@Query("select a from TbServiceReceiptMasEntity a where a.rmRcptid =:rmRcptId and a.additionalRefNo =:propNo and (a.receiptTypeFlag='A' or a.receiptTypeFlag='R') and  a.orgId=:orgId and a.manualReceiptNo is not null and a.receiptDelFlag is null")
	TbServiceReceiptMasEntity getmanualReceiptAdvanceAmountByAddRefNo(@Param("rmRcptId") Long receiptId,@Param("propNo") String propNo,@Param("orgId") Long orgId);
	
	@Query("select  count(a) from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and (a.receiptTypeFlag='R' or a.receiptTypeFlag='A') and a.manualReceiptNo is not null and a.orgId=:orgId and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
	Long getCountOfbillPaidBetweenCurFinYearAdvance(@Param("propNo") String propNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId);
	
	@Query("select  a from TbServiceReceiptMasEntity a where a.additionalRefNo =:addRefNo and  a.orgId=:orgId and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
	 List<TbServiceReceiptMasEntity> getbillPaidBetweenCurFinYear(@Param("addRefNo") String addRefNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId);

	@Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where "
			+ "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.receiptDelFlag is null")
	BigDecimal getPaidAmountByAdditionalRefNoIncRebate(@Param("orgId") Long orgId,
			@Param("additionalRefNo") String additionalRefNo, @Param("deptId") Long deptId);
	
	@Query("select r from TbServiceReceiptMasEntity r where "
			+ " r.orgId =:orgId  and r.additionalRefNo =:propNo and r.receiptTypeFlag='RB'  and r.rmDate >= :fromDate and r.rmDate <= :toDate  and r.refId is null  and r.receiptDelFlag is null")
	List<TbServiceReceiptMasEntity> getRebateByAdditionalRefNo(@Param("orgId") Long orgId,
			@Param("propNo") String propNo , @Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	@Query("select r from TbServiceReceiptMasEntity r where r.receiptDelFlag is null and r.rmRcptid = (select max(rec.rmRcptid) from TbServiceReceiptMasEntity rec where"
            +
            " rec.orgId =:orgId  and rec.additionalRefNo =:additionalRefNo  and (receiptTypeFlag='R' or receiptTypeFlag='A') and rec.flatNo=:flatNo ) ")
    TbServiceReceiptMasEntity getLatestReceiptDetailByAddRefNoAndFlatNo(@Param("orgId") Long orgId,
            @Param("additionalRefNo") String additionalRefNo,@Param("flatNo") String flatNo);
	 
	 @Query("select r from TbServiceReceiptMasEntity r where r.apmApplicationId=:applicationId and r.smServiceId=:smServiceId and r.orgId=:orgId")
		TbServiceReceiptMasEntity getReceiptDetByAppIdAndServiceId(@Param("applicationId") Long applicationId, @Param("smServiceId") Long smServiceId, @Param("orgId") Long orgId);

	 @Query("select  count(a) from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and a.receiptTypeFlag='RB' and  a.orgId=:orgId and a.rmNarration=:narration and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
		Long getCountOfbillPaidBetweenSelectedPeriod(@Param("propNo") String propNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId,@Param("narration") String narration);
	 
	 @Query("select  count(a) from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and a.receiptTypeFlag='RB' and  a.orgId=:orgId and a.rmNarration=:narration and a.flatNo=:flatNo and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
		Long getCountOfbillPaidBetweenSelectedPeriodByFlatNo(@Param("propNo") String propNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId,@Param("narration") String narration,@Param("flatNo") String flatNo);
	 
	 @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where "
				+ "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.receiptTypeFlag in('R','RB') and r.receiptDelFlag is null")
		BigDecimal getPaidAmountByAdditionalRefNoExcludRebate(@Param("orgId") Long orgId,
				@Param("additionalRefNo") String additionalRefNo, @Param("deptId") Long deptId);
	 @Query("select sum(rmAmount) from TbServiceReceiptMasEntity r where "
				+ "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.receiptTypeFlag in('R','RB') and r.flatNo=:flatNo and r.receiptDelFlag is null")
		BigDecimal getPaidAmountByAdditionalRefNoAndFlatNoIncludeRebate(@Param("orgId") Long orgId,
				@Param("additionalRefNo") String additionalRefNo, @Param("deptId") Long deptId, @Param("flatNo") String flatNo);
	 @Query("select  count(a) from TbSrcptModesDetEntity a where a.cbBankid =:bankId and a.rdChequeddno=:chequeNo")
	 Long getDuplicateChequeNoCount(@Param("bankId") Long bankId, @Param("chequeNo") Long chequeNo);

	 @Query("select r1 from TbServiceReceiptMasEntity r1 where r1.rmRcptid=(select max(rmRcptid) from TbServiceReceiptMasEntity r where r.rmRcptno =:rcptNo and r.orgId =:orgId and r.dpDeptId =:deptId )")
	TbServiceReceiptMasEntity getReceiptDetailByRcptNoAndDeptId(@Param("rcptNo") Long rcptNo, @Param("orgId") Long orgId,@Param("deptId")  Long deptId);
	 
	
	@Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where  b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where orgId =:orgId  and additionalRefNo =:additionalRefNo and receiptTypeFlag='AA' and rmDate=:receiptDate and receiptDelFlag is null)")
		BigDecimal getPaidAmountByAdditionalRefNoDemandAmount(@Param("orgId") Long orgId,
				@Param("additionalRefNo") String additionalRefNo, @Param("receiptDate") Date receiptDate);
	
	@Query("select r from TbServiceReceiptMasEntity r where "
			+ "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.flatNo=:flatNo and r.receiptDelFlag is null")
	List<TbServiceReceiptMasEntity> getCollectionDetailsWithFlatNo(@Param("additionalRefNo") String additionalRefNo,
			@Param("flatNo") String flatNo, @Param("deptId") Long deptId, @Param("orgId") Long orgid);
	
	 @Query("SELECT  r FROM TbServiceReceiptMasEntity r WHERE r.rmRcptid = (SELECT MAX(srm.rmRcptid) FROM TbServiceReceiptMasEntity srm  WHERE "
			 + "srm.additionalRefNo = :additionalRefNo AND (srm.receiptTypeFlag = 'R' OR srm.receiptTypeFlag = 'A') AND srm.receiptDelFlag IS NULL)")
	 TbServiceReceiptMasEntity getMaxReceiptIdByAdditinalRefNo(@Param("additionalRefNo") String additionalRefNo);
	 
	 @Query("select  r from TbServiceReceiptMasEntity r where r.additionalRefNo =:additionalRefNo and r.orgId =:orgId")
	 List<TbServiceReceiptMasEntity> getPaymentHistoryByAdditinalRefNo(@Param("additionalRefNo") String additionalRefNo, @Param("orgId") Long orgId) ;
	 
	@Modifying
	@Query("delete from TbSrcptFeesDetEntity ad where ad.rfFeeid in(:feeIds)")
	void deleteReceiptdetailByIds(@Param("feeIds") List<Long> feeIdList);

    @Query("select r from TbServiceReceiptMasEntity r where r.rmRcptno =:rcptNo and r.additionalRefNo =:additionalRefNo and r.orgId =:orgId")
    TbServiceReceiptMasEntity getReceiptDetailByCsCcnRcptNoOrgId(@Param("additionalRefNo") String conNo, @Param("rcptNo") Long rcptNo,
            @Param("orgId") Long orgId);
    
    @Query("select r from TbServiceReceiptMasEntity r where r.apmApplicationId=:applicationNo  and r.orgId=:orgId")
    List<TbServiceReceiptMasEntity> getReceiptDetailsByAppIdList(@Param("applicationNo") Long applicationNo,@Param("orgId") Long orgId);
    
    @Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where   b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where orgId =:orgId  and additionalRefNo =:additionalRefNo and flatNo=:flatNo and receiptTypeFlag='AA' and rmDate=:receiptDate and receiptDelFlag is null)")
	BigDecimal getPaidAmountByAdditionalRefNoAndFlatNoDemandAmount(@Param("orgId") Long orgId,
			@Param("additionalRefNo") String additionalRefNo, @Param("flatNo") String flatNo, @Param("receiptDate") Date receiptDate);
    
    @Query("select r from TbServiceReceiptMasEntity r where "
			+ " r.orgId =:orgId  and r.additionalRefNo =:propNo and r.flatNo=:flatNo and r.receiptTypeFlag='RB' and r.rmDate >= :fromDate and r.rmDate <= :toDate and r.refId is null  and r.receiptDelFlag is null")
	List<TbServiceReceiptMasEntity> getRebateByAdditionalRefNoAndFlatNo(@Param("orgId") Long orgId,
			@Param("propNo") String propNo,@Param("flatNo") String flatNo , @Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

    @Query("select r from TbServiceReceiptMasEntity r where " +
            "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo")
    List<TbServiceReceiptMasEntity> getCollectionDetailsActiveInactive(@Param("additionalRefNo") String additionalRefNo,
            @Param("deptId") Long deptId, @Param("orgId") long orgid);
    
    @Query("select r from TbServiceReceiptMasEntity r where "
			+ "r.dpDeptId =:deptId and r.orgId =:orgId  and r.additionalRefNo =:additionalRefNo and r.flatNo=:flatNo")
	List<TbServiceReceiptMasEntity> getCollectionDetailsWithFlatNoActiveInactive(@Param("additionalRefNo") String additionalRefNo,
			@Param("flatNo") String flatNo, @Param("deptId") Long deptId, @Param("orgId") Long orgid);
    
    @Query("select r from TbServiceReceiptMasEntity r where "
			+ " r.orgId =:orgId  and r.additionalRefNo =:propNo and r.receiptTypeFlag='RB'  and r.rmDate =:billDate and r.receiptDelFlag is null")
	List<TbServiceReceiptMasEntity> getRebateByAdditionalRefNoAndBillDate(@Param("orgId") Long orgId,
			@Param("propNo") String propNo , @Param("billDate") Date billDate);
    
    @Query("select r from TbServiceReceiptMasEntity r where "
			+ " r.orgId =:orgId  and r.additionalRefNo =:propNo and r.flatNo=:flatNo and r.receiptTypeFlag='RB' and r.rmDate =:billDate and r.receiptDelFlag is null")
	List<TbServiceReceiptMasEntity> getRebateByAdditionalRefNoAndBillDateAndFlatNo(@Param("orgId") Long orgId,
			@Param("propNo") String propNo,@Param("flatNo") String flatNo , @Param("billDate") Date billDate);
    
    @Query("select r.rmRcptno from TbServiceReceiptMasEntity r where r.additionalRefNo=:csIdn and r.orgId =:orgId and "
    		+ " r.rmDate between :fromDt and :toDt")
    List<Long> getReceiptNoList(@Param("csIdn") String csIdn, @Param("orgId") Long orgId, @Param("fromDt") Date fromDt, @Param("toDt") Date toDt);

    @Query("select r.rmRcptno from TbServiceReceiptMasEntity r where r.additionalRefNo=:propertyNo and r.orgId =:orgId and "
    		+ " r.rmDate between :fromDt and :toDt")
	List<Long> getPropReceiptNoList(@Param("propertyNo") String propertyNo, @Param("orgId") Long orgId, @Param("fromDt") Date fromDt, @Param("toDt") Date toDt);
    
    @Query("select r.rmRcptno from TbServiceReceiptMasEntity r where r.additionalRefNo=:propertyNo and r.flatNo=:flatNo and r.orgId =:orgId and "
    		+ " r.rmDate between :fromDt and :toDt")
	List<Long> getPropReceiptNoListByFlatNo(@Param("propertyNo") String propertyNo, @Param("flatNo") String flatNo, @Param("orgId") Long orgId, @Param("fromDt") Date fromDt, @Param("toDt") Date toDt);
    
    @Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where orgId =:orgId  and additionalRefNo =:additionalRefNo and flatNo=:flatNo and rmN3=:bmIdNo and receiptTypeFlag='AA' and rmDate=:receiptDate and receiptDelFlag is null)")
	BigDecimal getPaidAmountByAdditionalRefNoAndFlatNoBmIdNoDemandAmount(@Param("orgId") Long orgId,
			@Param("additionalRefNo") String additionalRefNo, @Param("flatNo") String flatNo, @Param("receiptDate") Date receiptDate,@Param("bmIdNo") Long bmIdno);
    
    @Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where   b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where orgId =:orgId  and additionalRefNo =:additionalRefNo and rmN3=:bmIdNo and receiptTypeFlag='AA' and rmDate=:receiptDate and receiptDelFlag is null)")
	BigDecimal getPaidAmountByAdditionalRefNoAndBmIdNoDemandAmount(@Param("orgId") Long orgId,
			@Param("additionalRefNo") String additionalRefNo, @Param("receiptDate") Date receiptDate,@Param("bmIdNo") Long bmIdno);
    
    @Query("select  a from TbServiceReceiptMasEntity a where a.additionalRefNo =:addRefNo and  a.orgId=:orgId and a.flatNo=:flatNo and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null")
	 List<TbServiceReceiptMasEntity> getbillPaidBetweenCurFinYearByFlatNo(@Param("addRefNo") String addRefNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate ,@Param("orgId") Long orgId, @Param("flatNo") String flatNo);
    
    
    @Query("select b from TbSrcptFeesDetEntity b where b.rmRcptid in (select  a.rmRcptid from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and a.receiptTypeFlag='RB' and  a.orgId=:orgId and a.rmNarration=:narration and a.flatNo=:flatNo and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null)")
	List<TbSrcptFeesDetEntity> getRebateAmntByPorpNoAndFlatNo(@Param("propNo") String propNo, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("orgId") Long orgId, @Param("narration") String narration,
			@Param("flatNo") String flatNo);

	
    @Query("select b from TbSrcptFeesDetEntity b where b.rmRcptid in (select  a.rmRcptid from TbServiceReceiptMasEntity a where a.additionalRefNo =:propNo and a.receiptTypeFlag='RB' and  a.orgId=:orgId and a.rmNarration=:narration and (a.rmDate between :fromDate and :toDate) and a.receiptDelFlag is null)")
	List<TbSrcptFeesDetEntity> getRebateAmntByPorpNo(@Param("propNo") String propNo, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("orgId") Long orgId, @Param("narration") String narration);
    
    @Query("select  a from TbServiceReceiptMasEntity a where a.additionalRefNo =:addRefNo and a.rmDate =:rmDate and a.receiptDelFlag is null")
    List<TbServiceReceiptMasEntity> getReceiptAmountByRecDateAndAdditionalRefNo(@Param("addRefNo") String additionalRefNo ,@Param("rmDate") Date rmDate);
    
    @Query("select  a from TbServiceReceiptMasEntity a where a.additionalRefNo =:addRefNo and a.flatNo=:flatNo and a.rmDate =:rmDate and a.receiptDelFlag is null")
    List<TbServiceReceiptMasEntity> getReceiptAmountByRecDateAndAdditionalRefNoAndFlatNo(@Param("addRefNo") String additionalRefNo, @Param("flatNo") String flatNo ,@Param("rmDate") Date rmDate);
	 
    @Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where b.billdetId=:billDetailId and  b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where additionalRefNo =:additionalRefNo and receiptDelFlag is null)")
	BigDecimal getPaidAmountByAdditionalRefNoAndBillDetIdDemandAmount(@Param("billDetailId") Long billdetId,@Param("additionalRefNo") String additionalRefNo);
    
    @Query("SELECT sum(b.rfFeeamount) from TbSrcptFeesDetEntity b  where b.billdetId=:billDetailId and  b.rmRcptid in (select rmRcptid from TbServiceReceiptMasEntity  where additionalRefNo =:additionalRefNo and flatNo=:flatNum and receiptDelFlag is null)")
	BigDecimal getPaidAmountByAdditionalRefNoAndFlatNoAndBillDetIdDemandAmount(@Param("billDetailId") Long billdetId,@Param("additionalRefNo") String additionalRefNo,@Param("flatNum") String flatNo);
    
    @Query("SELECT b.rdSrChkDis,b.rmRcptid.rmRcptid from TbSrcptModesDetEntity b  where  b.rmRcptid = (select max(rmRcptid) from TbServiceReceiptMasEntity  where additionalRefNo =:additionalRefNo and (receiptTypeFlag='A' or receiptTypeFlag='R') )")
	String[] getchquDisHonFlagAdditionalRefNo(@Param("additionalRefNo") String additionalRefNo);
    
    @Query("SELECT b.rdSrChkDis,b.rmRcptid.rmRcptid from TbSrcptModesDetEntity b  where  b.rmRcptid = (select max(rmRcptid) from TbServiceReceiptMasEntity  where additionalRefNo =:additionalRefNo and flatNo=:flatNum and (receiptTypeFlag='A' or receiptTypeFlag='R'))")
    String[] getchquDisHonFlagByAdditionalRefNoAndFlatNo(@Param("additionalRefNo") String additionalRefNo,@Param("flatNum") String flatNo);
    @Query("select count(r) from TbServiceReceiptMasEntity r where " +
            "r.pgRefId =:pgRefId and r.orgId =:orgId")
    int checkRecieptDataExistAgainstPgRefIdOrnot(@Param("pgRefId") Long additionalRefNo,
            @Param("orgId") Long orgId);
    @Modifying
    @Query( value = "update tb_ml_trade_mast set TRD_STATUS=:lookUpId ,UPDATED_DATE=:updatetDate,UPDATED_BY=:userId  where APM_APPLICATION_ID=:applNo and ORGID=:orgId",nativeQuery = true)
	int updateMarketLicenseStatusAfterPayment(@Param("applNo")Long applNo,@Param("orgId") Long orgId, @Param("lookUpId")long lookUpId, @Param("updatetDate") Date updatetDate,@Param("userId") Long userId);
    
    @Query("select r.rmRcptno from TbServiceReceiptMasEntity r where r.pgRefId=:pgRefId and r.receiptTypeFlag in('R','A') and r.receiptDelFlag is null")
	Long getReceiptNoByPgRefId(@Param("pgRefId") Long pgRefId);

    @Query("select  r from TbServiceReceiptMasEntity r where r.rmRcptid in (select max(rmRcptid) from TbServiceReceiptMasEntity where additionalRefNo=:additionalRefNo and dpDeptId=:dpDeptId and receiptTypeFlag <> 'RB'  and receiptDelFlag is null)")
	 TbServiceReceiptMasEntity getMaxReceiptIdByAdditinalRefNoAndDeptId(@Param("additionalRefNo") String additionalRefNo, @Param("dpDeptId") Long dpDeptId);

    @Query("select  a from TbSrcptModesDetEntity a where a.rmRcptid.rmRcptid =:rmRcptid")
	List<TbSrcptModesDetEntity> getModeDetails(@Param("rmRcptid") Long rmRcptid);
	
    @Query("SELECT am.rmAmount FROM  TbServiceReceiptMasEntity am WHERE am.additionalRefNo=:additionalRefNo AND am.rmDate=:receiptDate and am.receiptTypeFlag='RB'")
    Double getRebateAmountByPropNo(@Param("additionalRefNo") String propertyNo, @Param("receiptDate") Date rmDate);
    
    @Query("SELECT rec FROM TbServiceReceiptMasEntity rec where rec.additionalRefNo=:additionalRefNo AND rec.dpDeptId=:deptId AND rec.orgId=:orgId")
    List<TbServiceReceiptMasEntity> getAllReceiptsByAddRefNo(@Param("additionalRefNo") String additionalRefNo,@Param("deptId") Long deptId, @Param("orgId") Long orgId);

    @Query("SELECT new com.abm.mainet.common.dto.ReceiptSummaryDTO(rec.rmDate, rec.rmAmount) FROM TbServiceReceiptMasEntity rec " +
    	       "WHERE rec.coddwzId1 = :coddwzId1 AND rec.coddwzId2 = :coddwzId2 AND rec.dpDeptId = :deptId AND rec.orgId = :orgId " +
    	       "AND rec.receiptTypeFlag in('A','R') AND rec.receiptDelFlag is null")
    List<ReceiptSummaryDTO> getCollectionSummaryByWardZone(@Param("coddwzId1") Long coddwzId1, @Param("coddwzId2") Long coddwzId2,
    	    @Param("deptId") Long deptId, @Param("orgId") Long orgId);
    
    @Query("SELECT sum(rec.rmAmount) FROM TbServiceReceiptMasEntity rec " +
 	       "WHERE rec.dpDeptId = :deptId AND rec.orgId = :orgId AND rec.receiptTypeFlag in('A','R') AND rec.receiptDelFlag is null")
    BigDecimal getCollectionSummaryByAllWard(@Param("deptId") Long deptId, @Param("orgId") Long orgId);
    
    @Query("SELECT CASE WHEN COUNT(am) > 0 THEN true ELSE false END FROM TbServiceReceiptMasEntity am WHERE am.manualReceiptNo = :manualReceiptNo"
			+ " AND am.smServiceId = :smServiceId AND am.dpDeptId = :dpDeptid AND am.orgId = :orgId")
	boolean findDuplicateManualReceiptExist(@Param("manualReceiptNo") String manualReceiptNo,
			@Param("smServiceId") Long smServiceId, @Param("dpDeptid") Long dpDeptid, @Param("orgId") Long orgId);
}

