package com.abm.mainet.materialmgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abm.mainet.materialmgmt.domain.CommonTransactionEntity;

public interface ICommonTransactionRepository extends JpaRepository<CommonTransactionEntity, Long> {

	@Query(value = "SELECT BINLOCID, BINLOCATION from MM_BIN_LOCATION_MAS b where b.ORGID=:orgId and b.BINLOCID in "
			+ " (SELECT distinct a.binlocation FROM (SELECT storeid, itemid, binlocation, itemno, "
			+ " COALESCE(SUM(creditqty), 0) AS crdty,  MAX(CASE WHEN itemno IS NOT NULL THEN expirydate END) AS "
			+ " expirydate_check FROM mm_transactions WHERE transactiontype IN ('P', 'S', 'O', 'R') AND "
			+ " disposalstatus = 'N' AND ORGID=:orgId GROUP BY storeid, itemid, binlocation, itemno) a "
			+ " LEFT JOIN "
			+ " (SELECT storeid, itemid, binlocation, itemno, COALESCE(SUM(debitqty), 0) AS drdty FROM "
			+ " mm_transactions WHERE transactiontype IN ('M', 'I') AND disposalstatus = 'N' AND ORGID=:orgId "
			+ " GROUP BY storeid, itemid, binlocation, itemno) b ON a.storeid = b.storeid AND a.itemid = b.itemid "
			+ " AND a.binlocation = b.binlocation AND a.itemno = b.itemno WHERE a.storeid=:storeId "
			+ " AND a.itemid=:itemId and (ifnull(a.crdty,0) - ifnull(b.drdty,0)) > 0 AND (null IS NULL OR a.itemno=null) "
			+ " AND (a.expirydate_check IS NULL OR if(a.itemno is not null,a.expirydate_check > CURRENT_DATE(),1=1)))", nativeQuery = true)
	public List<Object[]> fetchBinLocationListForIndent(@Param("orgId") Long orgId, @Param("storeId") Long storeId,
			@Param("itemId") Long itemId);

	@Query(value = "SELECT distinct a.itemno FROM (SELECT storeid, itemid, binlocation, itemno, COALESCE(SUM(creditqty), 0) AS crdty, "
			+ " MAX(CASE WHEN itemno IS NOT NULL THEN expirydate END) AS expirydate_check FROM mm_transactions WHERE transactiontype "
			+ " IN ('P', 'S', 'O', 'R') AND disposalstatus = 'N' AND ORGID=:orgId GROUP BY storeid, itemid, binlocation, itemno) a "
			+ " LEFT JOIN "
			+ " (SELECT storeid, itemid, binlocation, itemno, COALESCE(SUM(debitqty), 0) AS drdty FROM mm_transactions WHERE "
			+ " transactiontype IN ('M', 'I') AND disposalstatus = 'N' AND ORGID=:orgId GROUP BY storeid, itemid, binlocation, itemno) b  "
			+ " ON a.storeid = b.storeid AND a.itemid = b.itemid AND a.binlocation = b.binlocation AND a.itemno = b.itemno "
			+ " WHERE a.storeid=:storeId AND a.itemid=:itemId AND a.binlocation=:binLocation and (ifnull(a.crdty,0) - ifnull(b.drdty,0)) > 0  "
			+ " AND (null IS NULL OR a.itemno=null) AND (a.expirydate_check IS NULL OR if(a.itemno is not null,a.expirydate_check > CURRENT_DATE(),1=1));", nativeQuery = true)
	public List<String> fetchItemNoListForIndent(@Param("orgId") Long orgId, @Param("storeId") Long storeId,
			@Param("itemId") Long itemId, @Param("binLocation") Long binLocation);

	
	@Query(value = "SELECT IFNULL(diffTbl.difference, 0) AS difference FROM ( " +
	        "    SELECT (IFNULL(a.receQuantity, 0) + IFNULL(a.crdty, 0) - IFNULL(b.drdty, 0) + IFNULL(C.RETURN_QTY, 0)) AS difference " +
	        "    FROM ( " +
	        "        SELECT storeid, itemid, binlocation, IFNULL(itemno, '') AS itemno, " +
	        "               COALESCE((SELECT SUM(GI.receQuantity) " +
	        "                         FROM mm_goods_item GI " +
	        "                         JOIN mm_goods_receipt GR ON GR.indentid = GI.indentid " +
	        "                         WHERE GR.storeId = MMT.storeid AND GI.itemId = MMT.itemId), 0) AS receQuantity, " +
	        "               COALESCE(SUM(creditqty), 0) AS crdty, " +
	        "               MAX(CASE WHEN itemno IS NOT NULL THEN expirydate END) AS expirydate_check " +
	        "        FROM mm_transactions MMT " +
	        "        WHERE transactiontype IN ('P', 'S', 'O', 'R') " +
	        "          AND disposalstatus = 'N' " +
	        "          AND ORGID = :orgId " +
	        "        GROUP BY storeid, itemid, binlocation, IFNULL(itemno, '') " +
	        "    ) a " +
	        "    LEFT JOIN ( " +
	        "        SELECT storeid, itemid, binlocation, IFNULL(itemno, '') AS itemno, " +
	        "               COALESCE(SUM(debitqty), 0) AS drdty " +
	        "        FROM mm_transactions " +
	        "        WHERE transactiontype IN ('M', 'I') " +
	        "          AND disposalstatus = 'N' " +
	        "          AND ORGID = :orgId " +
	        "        GROUP BY storeid, itemid, binlocation, IFNULL(itemno, '') " +
	        "    ) b ON a.storeid = b.storeid AND a.itemid = b.itemid AND a.binlocation = b.binlocation AND a.itemno = b.itemno " +
	        "    LEFT JOIN ( " +
	        "        SELECT storeid, itemid, SUM(returnqty) RETURN_QTY " +
	        "        FROM mm_deptreturn_det " +
	        "        WHERE disposalflag = 'N' " +
	        "        GROUP BY storeid, itemid " +
	        "    ) C ON a.storeid = C.storeid AND a.itemid = C.itemid " +
	        "    WHERE a.storeid = :storeId " +
	        "      AND a.itemid = :itemId " +
	        "      AND a.binlocation = :binLocation " +
	        "      AND (:itemNo IS NULL OR a.itemno = :itemNo) " +
	        "      AND ((IFNULL(a.receQuantity, 0) + IFNULL(a.crdty, 0) - IFNULL(b.drdty, 0) + IFNULL(C.RETURN_QTY, 0))) > 0 " +
	        "      AND (a.expirydate_check IS NULL OR (a.itemno IS NOT NULL AND a.expirydate_check > CURRENT_DATE())) " +
	        ") AS diffTbl", nativeQuery = true)

	public Double fetchBalanceQuantityForIndent(@Param("orgId") Long orgId, @Param("storeId") Long storeId, @Param("itemId") Long itemId, @Param("binLocation") Long binLocation,@Param("itemNo") String itemNo);
	 	 	
	
	@Query(value = "SELECT binlocation FROM mm_transactions where itemid=:itemId AND storeid=:storeId AND ORGID=:orgId", nativeQuery = true)
	public Double fetchBinLocationByItmIdAndStrId(@Param("itemId") Long itemId,@Param("storeId") Long storeId,
			@Param("orgId") Long orgId);
	
	@Query(value = "SELECT Itemid,name FROM mm_itemmaster WHERE itemgroup=:itemgroup AND storeId=:storeId  AND ORGID=:orgId", nativeQuery = true)
	public List<Object[]> fetchItemGroupNameById(@Param("itemgroup") Long itemgroup,@Param("storeId") Long storeId,@Param("orgId") Long orgId);
	
	@Query(value = "SELECT name FROM mm_itemmaster WHERE itemid=:itemId AND ORGID=:orgId", nativeQuery = true)
	public String fetchItemNameByItemId(@Param("itemId") Long itemId,@Param("orgId") Long orgId);
	
	
	@Query(value = "SELECT binlocation FROM mm_transactions where itemid=:itemId AND ORGID=:orgId", nativeQuery = true)
	public Double fetchBinLocaByItemId(@Param("itemId") Long itemId,
			@Param("orgId") Long orgId);
	
	@Query(value = "SELECT BINLOCATION FROM MM_BIN_LOCATION_MAS where BINLOCID=:binLocation AND ORGID=:orgId", nativeQuery = true)
	public String fetchBinLocationName(@Param("binLocation") Long binLocation,
			@Param("orgId") Long orgId);
	
	@Query(value = "SELECT IFNULL((\r\n" + 
			"    SELECT (IFNULL(receQuantity, 0) + IFNULL(a.crdty, 0) - IFNULL(b.drdty, 0) + IFNULL(C.RETURN_QTY, 0)) AS difference\r\n" + 
			"    FROM (\r\n" + 
			"        SELECT storeid, itemid, binlocation,\r\n" + 
			"               COALESCE((\r\n" + 
			"                   SELECT SUM(receQuantity)\r\n" + 
			"                   FROM mm_goods_item GI\r\n" + 
			"                   JOIN mm_goods_receipt GR ON GR.indentid = GI.indentid\r\n" + 
			"                   WHERE GR.storeId = MMT.storeid AND GI.itemId = MMT.itemId\r\n" + 
			"               ), 0) AS receQuantity,\r\n" + 
			"               COALESCE(SUM(creditqty), 0) AS crdty,\r\n" + 
			"               MAX(CASE WHEN itemno IS NOT NULL THEN expirydate END) AS expirydate_check\r\n" + 
			"        FROM mm_transactions MMT\r\n" + 
			"        WHERE transactiontype IN ('P', 'S', 'O', 'R')\r\n" + 
			"          AND disposalstatus = 'N'\r\n" + 
			"          AND ORGID = :orgId\r\n" + 
			"        GROUP BY storeid, itemid, binlocation\r\n" + 
			"    ) a\r\n" + 
			"    LEFT JOIN (\r\n" + 
			"        SELECT storeid, itemid, binlocation,\r\n" + 
			"               COALESCE(SUM(debitqty), 0) AS drdty\r\n" + 
			"        FROM mm_transactions\r\n" + 
			"        WHERE transactiontype IN ('M', 'I')\r\n" + 
			"          AND disposalstatus = 'N'\r\n" + 
			"          AND ORGID = :orgId\r\n" + 
			"        GROUP BY storeid, itemid, binlocation\r\n" + 
			"    ) b ON a.storeid = b.storeid\r\n" + 
			"        AND a.itemid = b.itemid\r\n" + 
			"        AND a.binlocation = b.binlocation\r\n" + 
			"    LEFT JOIN (\r\n" + 
			"        SELECT storeid, itemid, SUM(returnqty) RETURN_QTY\r\n" + 
			"        FROM mm_deptreturn_det\r\n" + 
			"        WHERE disposalflag = 'N'\r\n" + 
			"        GROUP BY storeid, itemid\r\n" + 
			"    ) C ON a.storeid = C.storeid AND a.itemid = C.itemid\r\n" + 
			"    WHERE a.storeid = :storeId\r\n" + 
			"      AND a.itemid = :itemId\r\n" + 
			"      AND a.binlocation = :binLocation\r\n" + 
			"      AND (IFNULL(receQuantity, 0) + IFNULL(a.crdty, 0) - IFNULL(b.drdty, 0) + IFNULL(C.RETURN_QTY, 0)) > 0\r\n" + 
			"), 0) AS difference", nativeQuery = true)
	public Double fetchAvailableQuantity(@Param("orgId") Long orgId, @Param("storeId") Long storeId,@Param("itemId") Long itemId, 
			 @Param("binLocation") Long binLocation);
	
	@Query(value = "SELECT IFNULL((SELECT DISTINCT requisitionNo FROM mm_goods_receipt WHERE requisitionNo=:requisitionNo AND ORG_ID=:orgId),0)", nativeQuery = true)
	public Double findGoodsDuplicateRecordByOrderNoAndOrgId(@Param("requisitionNo") String requisitionNo,@Param("orgId") Long orgId);
	
	@Query(value = "SELECT itemgroupid,(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = itemgroupid) FROM MM_GROUPMAPPING WHERE ORGID=:orgId AND storeid=:storeId", nativeQuery = true)
	public List<Object[]> fetchItemGrpIdbyOrgIdAndStoreId(@Param("orgId") Long orgId, @Param("storeId") Long storeId);
	
	@Query(value = "select COD_DESC from tb_comparent_det where COD_ID=:itemGrpId AND ORGID=:orgId", nativeQuery = true)
	public String findItemGrpNamesBygrpIdAndOrgId(@Param("itemGrpId") Long itemGrpId, @Param("orgId") Long orgId);
	
	@Query(value = "select storename from mm_storemaster where storeid=:storeId AND ORGID=:orgId", nativeQuery = true)
	public String findStoreNmeByStoreIdAndOrgId(@Param("storeId") Long storeId, @Param("orgId") Long orgId);

}
