package com.abm.mainet.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.cfc.challan.domain.AdjustmentMasterEntity;

public interface AdjustmentEntryRepository extends JpaRepository<AdjustmentMasterEntity, Long> {

    @Query("select a from AdjustmentMasterEntity a where  a.adjRefNo=:refNo and a.dpDeptId=:dpDeptId")
    List<AdjustmentMasterEntity> fetchHistory(@Param("refNo") String refNo, @Param("dpDeptId") Long dpDeptId);

    @Query("select a from AdjustmentMasterEntity a where a.adjRefNo in (:refNo) and a.dpDeptId=:dpDeptId and "
            + " a.orgId=:orgId ")
    List<AdjustmentMasterEntity> fetchModuleWiseAdjustmentByUniqueIds(@Param("dpDeptId") Long deptId,
            @Param("refNo") List<String> uniqueIds, @Param("orgId") long orgid);

    @Query("select a from AdjustmentMasterEntity a where a.adjRefNo =:refNo and a.dpDeptId=:dpDeptId and "
            + " a.orgId=:orgId")
    List<AdjustmentMasterEntity> fetchAdjustmentByForPaidBill(@Param("dpDeptId") Long deptId,
            @Param("refNo") String csIdn, @Param("orgId") long orgid);
    
    @Query("select a from AdjustmentMasterEntity a where  a.adjRefNo=:refNo and a.dpDeptId=:dpDeptId and a.flatNo=:flatNo")
    List<AdjustmentMasterEntity> fetchHistoryByFlatNo(@Param("refNo") String refNo, @Param("dpDeptId") Long dpDeptId, @Param("flatNo") String flatNo);
    
    @Query("select a from AdjustmentMasterEntity a where  a.adjRefNo=:refNo and a.dpDeptId=:dpDeptId and a.adjType=:adjType")
    List<AdjustmentMasterEntity> getAdjustmentDetailsByAdjType(@Param("refNo") String refNo, @Param("dpDeptId") Long dpDeptId,@Param("adjType") String adjType);
    
    @Query("select a from AdjustmentMasterEntity a where  a.adjRefNo=:refNo and a.dpDeptId=:dpDeptId and a.adjType=:adjType and a.flatNo=:flatNo")
    List<AdjustmentMasterEntity> getAdjustmentDetailsByAdjTypeAndFlatNo(@Param("refNo") String refNo, @Param("dpDeptId") Long dpDeptId,@Param("adjType") String adjType,@Param("flatNo") String flatNo);

	 @Modifying
	 @Transactional
	 @Query("update AdjustmentDetailEntity m set m.bdBilldetid=:bdBilldetid where m.adjId.adjId=:adjId and m.orgId=:orgId") 
	  void updateBmIdnoInAdjustedAdjustmentDet(@Param("adjId")long adjId, @Param("bdBilldetid")Long bdBilldetid, @Param("orgId") Long orgId);
	 
	@Query("select a from AdjustmentMasterEntity a where a.applicationId=:applicationId and a.orgId=:orgId")
	AdjustmentMasterEntity getAdjustmentDetailsByApplicationId(@Param("applicationId") Long applicationId, @Param("orgId") Long orgId);
	
    @Query("select a.applicationId from AdjustmentMasterEntity a where  a.adjId in (select max(a.adjId) from AdjustmentMasterEntity a where a.adjRefNo=:refNo)")
	Long getApplicationIdByAdjRefNo(@Param("refNo") String refNo);
    
    @Query("select a.applicationId from AdjustmentMasterEntity a where  a.adjId in (select max(a.adjId) from AdjustmentMasterEntity a where a.adjRefNo=:refNo and a.flatNo=:flatNo)")
	Long getApplicationIdByAdjRefNoAndFlatNo(@Param("refNo") String refNo, @Param("flatNo") String flatNo);
    
}
