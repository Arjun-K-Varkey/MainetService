package com.abm.mainet.cfc.loi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.cfc.loi.domain.DishonourChargeEntity;

@Repository
public interface DishonurChargeEntryRepository extends JpaRepository<DishonourChargeEntity, Long> {

	@Query("select dis from DishonourChargeEntity dis  where   dis.apmApplicationId=:apmApplicationId and isDishnrChgPaid='N'")
	DishonourChargeEntity getDishonourChargeData(@Param("apmApplicationId") Long apmApplicationId);
	
	@Query(value = "SELECT DPS_SLIPNO from TB_AC_BANK_DEPOSITSLIP_MASTER where DPS_SLIPID=:depId and ORGID=:orgId" ,nativeQuery = true)
    String findAccountDepositNoByDepositId(@Param("depId") Long depId,@Param("orgId") long orgId);
	

	@Query(
		    value = "SELECT \r\n" +
		            "    o.MN_asso_owner_name    AS ownerName,\r\n" +
		            "    d.MN_ASSD_OCCUPIER_NAME AS occupierName,\r\n" +
		            "    d.MN_ASSE_MOBILENO     AS mobileNo,\r\n" +
		            "    d.MN_OCCUPIER_EMAIL         AS email,\r\n" +
		            "    m.MN_ASS_address       AS address,\r\n" +
		            "    m.GO_GREEN_REQ         AS goGreenFlag,\r\n" +
		            "    m.MN_ASS_ID         AS assId\r\n" +
		            "FROM tb_as_assesment_mast m\r\n" +
		            "JOIN tb_as_assesment_owner_dtl o \r\n" +
		            "       ON m.MN_ASS_id = o.MN_ASS_id\r\n" +
		            "JOIN tb_as_assesment_detail d\r\n" +
		            "       ON d.MN_ASS_id = o.MN_ASS_id\r\n" +
		            "WHERE m.MN_ASS_no  = :propNo \r\n" +
		            "  AND m.MN_FLAT_NO = :flatNo\r\n" +
		            "  AND m.orgid     = :orgId",
		    nativeQuery = true
		)
		Object[] getGoGreenPropertyDetails(
		    @Param("propNo") String propNo,
		    @Param("flatNo") String flatNo,
		    @Param("orgId") Long orgId
		);


	@Query(
			  value = "SELECT \r\n"
			        + "    o.MN_asso_owner_name      AS ownerName,\r\n"
			        + "    o.MN_asso_guardian_name   AS occupierName,\r\n"
			        + "    o.MN_ASSO_MOBILENO        AS mobileNo,\r\n"
			        + "    o.MN_ASS_EMAIL            AS email,\r\n"
			        + "    m.MN_ASS_address          AS address,\r\n"
			        + "    m.GO_GREEN_REQ            AS goGreenFlag,\r\n"
			        + "    m.MN_ASS_ID            AS assId\r\n"
			        + "FROM tb_as_assesment_mast m\r\n"
			        + "JOIN tb_as_assesment_owner_dtl o\r\n"
			        + "      ON m.MN_ASS_id = o.MN_ass_id\r\n"
			        + "WHERE m.MN_ASS_no = :propNo\r\n"
			        + "AND m.orgid = :orgId",
			  nativeQuery = true
			)
			Object[] getGoGreenPropertyDetailsWithoutFlat(
			    @Param("propNo") String propNo,
			    @Param("orgId") Long orgId
			);

    @Query(value = "SELECT \r\n" + 
    		"CAST(\r\n" + 
    		"   NULLIF(\r\n" + 
    		"      TRIM(\r\n" + 
    		"         CONCAT_WS(' ',\r\n" + 
    		"            NULLIF(CS_NAME,''),\r\n" + 
    		"            NULLIF(CS_MNAME,''),\r\n" + 
    		"            NULLIF(CS_LNAME,'')\r\n" + 
    		"         )\r\n" + 
    		"      ),\r\n" + 
    		"   '')\r\n" + 
    		"AS CHAR\r\n" + 
    		") AS full_name,\r\n" + 
    		"\r\n" + 
    		"COALESCE(CS_OCONTACTNO, CS_CONTACTNO) AS mobile_no,\r\n" + 
    		"COALESCE(CS_EMAIL, EMAIL_BILLING) AS email,\r\n" + 
    		"COALESCE(CS_OADD, CS_ADD) AS address,\r\n" +
    		"GO_GREEN_REQ AS goGreenFlag,\r\n" +
    		"CS_ID AS waterId\r\n" +
    		"\r\n" + 
    		"FROM TB_CSMR_INFO\r\n" + 
    		"WHERE CS_CCN =:ccnNo\r\n" + 
    		"AND ORGID = :orgId",nativeQuery = true)
	Object[] getGoGreenWaterDetails(@Param("ccnNo")String ccnNo,@Param("orgId") Long orgId);

    @Modifying
    @Query(value = "UPDATE tb_as_assesment_mast m " +
            "JOIN tb_as_assesment_owner_dtl o ON m.MN_ASS_id = o.MN_ASS_id " +
            "SET m.MN_ASS_EMAIL = :email, " +
            "    o.MN_asso_mobileno = :mobileNo ,m.GO_GREEN_REQ='Y', m.GO_GREEN_REQ_DATE=CURRENT_TIMESTAMP " +
            "WHERE m.MN_ASS_no = :propNo " +
            "AND m.MN_FLAT_NO = :flatNo " +
            "AND m.orgid = :orgId", nativeQuery = true)
    int updateGoGreenPropertyDetails(@Param("email") String email,
                                      @Param("mobileNo") String mobileNo,
                                      @Param("propNo") String propNo,
                                      @Param("flatNo") String flatNo,
                                      @Param("orgId") Long orgId);
    @Modifying
    @Query(value = "UPDATE tb_as_assesment_mast m " +
            "JOIN tb_as_assesment_owner_dtl o ON m.MN_ASS_id = o.MN_ASS_id " +
            "SET m.MN_ASS_EMAIL = :email, " +
            "    o.MN_asso_mobileno = :mobileNo,m.GO_GREEN_REQ='Y', m.GO_GREEN_REQ_DATE=CURRENT_TIMESTAMP " +
            "WHERE m.MN_ASS_no = :propNo " +
            "AND m.orgid = :orgId", nativeQuery = true)
    int updateGoGreenPropertyWithoutFlat(@Param("email") String email,
                                          @Param("mobileNo") String mobileNo,
                                          @Param("propNo") String propNo,
                                          @Param("orgId") Long orgId);
    @Modifying
    @Query(value = "UPDATE TB_CSMR_INFO " +
            "SET CS_EMAIL = :email, " +
            "    CS_OCONTACTNO = :mobileNo ,GO_GREEN_REQ='Y', GO_GREEN_REQ_DATE=CURRENT_TIMESTAMP " +
            "WHERE CS_CCN = :ccnNo " +
            "AND ORGID = :orgId", nativeQuery = true)
    int updateGoGreenWaterDetails(@Param("email") String email,
                                   @Param("mobileNo") String mobileNo,
                                   @Param("ccnNo") String ccnNo,
                                   @Param("orgId") Long orgId);
    
    
    @Modifying
    @Transactional
    @Query(value =
           "UPDATE TB_AS_ASSESMENT_MAST " +
           "SET GO_GREEN_REQ = :goGreenReq, " +
           "    GO_GREEN_REQ_DATE = CURRENT_TIMESTAMP " +
           "WHERE MN_ASS_ID = :assId",
           nativeQuery = true)
    int updatePropGoGreenFlag(@Param("goGreenReq") String goGreenReq,
                              @Param("assId") String assId);
    
    
    
    @Modifying
    @Transactional
    @Query(value =
           "UPDATE tb_csmr_info " +
           "SET GO_GREEN_REQ = :goGreenReq, " +
           "    GO_GREEN_REQ_DATE = CURRENT_TIMESTAMP " +
           "WHERE CS_ID = :assId",
           nativeQuery = true)
    int updateWaterGoGreenFlag(@Param("goGreenReq") String goGreenReq,
                               @Param("assId") String assId);
    
    
    @Modifying
    @Query(value = "UPDATE tb_as_assesment_detail AD " +
            "INNER JOIN tb_as_assesment_mast AM ON AM.MN_ASS_ID = AD.MN_ASS_ID " +
            "SET AD.MN_OCCUPIER_EMAIL = :email, " +
            "AD.MN_ASSE_MOBILENO = :mobileNo " +
            "WHERE AM.MN_ASS_NO = :propNo " +
            "AND AM.MN_FLAT_NO = :flatNo",
            nativeQuery = true)
    int updateGoGreenIndividualPropertyDetails(
            @Param("email") String email,
            @Param("mobileNo") String mobileNo,
            @Param("propNo") String propNo,
            @Param("flatNo") String flatNo);
    
    
    @Modifying
    @Query(value = "UPDATE tb_as_assesment_owner_dtl AOW " +
            "INNER JOIN tb_as_assesment_mast AM ON AM.MN_ASS_ID = AOW.MN_ASS_ID " +
            "SET AOW.MN_ASS_EMAIL = :email, " +
            "AOW.MN_ASSO_MOBILENO = :mobileNo " +
            "WHERE AM.MN_ASS_NO = :propNo",
            nativeQuery = true)
    int updateGoGreenWholePropertyDetails(
            @Param("email") String email,
            @Param("mobileNo") String mobileNo,
            @Param("propNo") String propNo);
    
    @Transactional
    @Modifying
    @Query(
        value = "UPDATE tb_as_assesment_mast m " +
                "SET m.GO_GREEN_REQ = 'Y', m.GO_GREEN_REQ_DATE=CURRENT_TIMESTAMP " +
                "WHERE m.MN_ASS_NO = :propNo " +
                "AND m.MN_FLAT_NO = :flatNo " +
                "AND m.ORGID = :orgId",
        nativeQuery = true
    )
    int updateGoGreenFlagForIndividualProperty(
            @Param("propNo") String propNo,
            @Param("flatNo") String flatNo,
            @Param("orgId") Long orgId
    );
    
    
    @Transactional
    @Modifying
    @Query(value =
            "UPDATE tb_as_assesment_mast " +
            "SET GO_GREEN_REQ = 'Y', " +
            "    GO_GREEN_REQ_DATE = CURRENT_TIMESTAMP " +
            "WHERE MN_ASS_no = :propNo " +
            "  AND orgid = :orgId",
            nativeQuery = true)
    int updateGoGreenFlagForWholeProp(
            @Param("propNo") String propNo,
            @Param("orgId") Long orgId);


}
