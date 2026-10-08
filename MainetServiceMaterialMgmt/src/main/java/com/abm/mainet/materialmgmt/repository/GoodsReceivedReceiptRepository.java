package com.abm.mainet.materialmgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abm.mainet.materialmgmt.domain.GoodsReceivedItemEntity;
import com.abm.mainet.materialmgmt.domain.GoodsReceivedReceiptEntity;
import com.abm.mainet.materialmgmt.domain.IndentIssueEntity;
import com.abm.mainet.materialmgmt.domain.IndentProcessEntity;
import com.abm.mainet.materialmgmt.domain.ItemOpeningBalanceEntity;

@Repository
public interface GoodsReceivedReceiptRepository extends JpaRepository<GoodsReceivedReceiptEntity, Long> {
	
	public List<GoodsReceivedReceiptEntity> findByOrgId(@Param("orgId") Long orgId);
	
	/*
	 * @Query(value =
	 * "SELECT indentid,requisitionNo,vendorId,vendorName,receivedReceiptDate,remarks FROM MM_Goods_Receipt where requisitionNo=:requisitionNo AND ORG_ID=:orgId"
	 * , nativeQuery = true) public GoodsReceivedReceiptEntity
	 * findByRequisitionNoAndOrgId(@Param("requisitionNo") String
	 * requisitionNo,@Param("orgId") Long orgId);
	 */
	
	@Query(value = "SELECT * FROM MM_GOODS_RECEIPT WHERE requisitionNo = :requisitionNo AND ORG_ID = :orgId", nativeQuery = true)
	public GoodsReceivedReceiptEntity findByRequisitionNoAndOrgId(@Param("requisitionNo") String requisitionNo,
			@Param("orgId") Long orgId);
	
	
	
	@Query(value = "SELECT indentid,itemGroup,locationName,uom,receQuantity,unPrice,totalValue,itemId FROM MM_Goods_Item where indentid=:indentId AND ORG_ID=:orgId", nativeQuery = true)
	public GoodsReceivedItemEntity findByindentIdAndOrgId(@Param("indentId") Long indentId,@Param("orgId") Long orgId);
	
	@Query(value = "SELECT VM_VENDORNAME FROM TB_VENDORMASTER where VM_VENDORID=:vendorId AND ORGID=:orgId", nativeQuery = true)
	public String fetchVendorNameByVendorIdOrgId(@Param("vendorId") Long vendorId,
			@Param("orgId") Long orgId);
	
	

}
