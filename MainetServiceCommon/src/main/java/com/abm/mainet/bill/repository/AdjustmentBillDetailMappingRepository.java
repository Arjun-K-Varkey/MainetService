package com.abm.mainet.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.cfc.challan.domain.AdjustmentBillDetailMappingEntity;

public interface AdjustmentBillDetailMappingRepository extends JpaRepository<AdjustmentBillDetailMappingEntity, Long> {
	
	 @Query("select a from AdjustmentBillDetailMappingEntity a where a.adjbmId =:adjbmId and a.orgId=:orgId")
	 List<AdjustmentBillDetailMappingEntity> getAdjustmentBillDetailsByBillDetId(@Param("adjbmId")Long adjbmId,
			@Param("orgId") Long orgId);

	 @Modifying
     @Transactional
	 @Query("update AdjustmentBillDetailMappingEntity m set m.adjbmId=:adjbmId where m.adjId=:adjId and m.orgId=:orgId")
	 void update(@Param("adjId")Long adjId, @Param("adjbmId")Long adjbmId, @Param("orgId") Long orgId);

	 @Query("select a from AdjustmentBillDetailMappingEntity a where a.adjbmId =:adjbmId and a.orgId=:orgId and a.dpDeptId=:dpDeptId")
	 List<AdjustmentBillDetailMappingEntity> getAdjustmentBillDetailsByBillDetIdDeptIdOrgId(@Param("adjbmId")Long adjbmId,
			@Param("orgId") Long orgId, @Param("dpDeptId") Long dpDeptId);
	 
	 @Modifying
     @Transactional
	 @Query("update AdjustmentBillDetailMappingEntity m set m.adjbmId=:adjbmId, m.adjAmount=:adjAmount where m.adjId=:adjId and m.orgId=:orgId")
	 void updateAdjBillDetailForWater(@Param("adjId")Long adjId, @Param("adjbmId")Long adjbmId, @Param("adjAmount")Double adjAmount, @Param("orgId") Long orgId);

	 @Query("select a from AdjustmentBillDetailMappingEntity a where a.adjId in :adjId and a.orgId=:orgId")
	 List<AdjustmentBillDetailMappingEntity> getAdjustmentDetailsByAdjId(@Param("adjId") List<Long> adjId,
			@Param("orgId") Long orgId);
}
