package com.abm.mainet.common.integration.dms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.integration.dms.domain.BlockChainCertificateEntity;
import com.abm.mainet.common.integration.dms.domain.SingleWindowIntegrationEntity;

@Repository
public interface SingleWindowIntegrationRepository extends JpaRepository<SingleWindowIntegrationEntity, Long> {
 
	

	@Query(value = 
	        "SELECT " +
	        "    AM.MN_ASS_NO AS PROPERTY_NUMBER, " +
	        "    AM.MN_FLAT_NO AS FLAT_NUMBER, " +
	        "    AM.OWNER_NAME AS OWNER_OCCUPIER_NAME, " +
	        "    (BM.CURRENT + BM.CURRENT_PENALTY + BM.ARREAR + BM.ARREAR_PENALTY) AS TOTAL_OUTSTANDING, " +
	        "    AM.ADDRESS, " +
	        "    AM.PROPERTY_TYPE, " +
	        "    BM.LAST_BILL_DATE, " +
	        "    AM.SMS_PAY_LINK " +
	        "FROM " +

	        "( " +
	        "    SELECT " +
	        "        BM.MN_PROP_NO, " +
	        "        BM.MN_BM_IDNO, " +
	        "        DATE(BM.MN_BILLDT) AS LAST_BILL_DATE, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID != 100000020 " +
	        "                        THEN BD.BD_CUR_BAL_TAXAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS CURRENT, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.BD_CUR_BAL_TAXAMT != 0 " +
	        "                             AND BD.BD_PRV_BAL_ARRAMT != 0 " +
	        "                        THEN IFNULL(BD.penalty_as_on_date, BD.BD_CUR_BAL_TAXAMT) " +
	        "                        ELSE 0 " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS CURRENT_PENALTY, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID != 100000020 " +
	        "                        THEN BD.BD_PRV_BAL_ARRAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS ARREAR, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID = 100000020 " +
	        "                        THEN BD.BD_PRV_BAL_ARRAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS ARREAR_PENALTY " +

	        "    FROM tb_as_bill_mas BM " +

	        "    INNER JOIN tb_as_bill_det BD " +
	        "        ON BM.MN_BM_IDNO = BD.BM_IDNO " +

	        "    INNER JOIN ( " +
	        "        SELECT " +
	        "            MN_PROP_NO, " +
	        "            MAX(MN_BM_IDNO) AS MN_BM_IDNO " +
	        "        FROM tb_as_bill_mas " +
	        "        WHERE MN_PROP_NO = :propNo " +
	        "          AND (PD_FLATNO IS NULL OR PD_FLATNO = '') " +
	        "        GROUP BY MN_PROP_NO " +
	        "    ) MAX_BM_ID " +
	        "        ON BM.MN_BM_IDNO = MAX_BM_ID.MN_BM_IDNO " +

	        "    WHERE BM.MN_PROP_NO = :propNo " +
	        "      AND (BM.PD_FLATNO IS NULL OR BM.PD_FLATNO = '') " +

	        "    GROUP BY " +
	        "        BM.MN_PROP_NO, " +
	        "        BM.MN_BM_IDNO, " +
	        "        LAST_BILL_DATE " +
	        ") AS BM " +

	        "INNER JOIN " +

	        "( " +
	        "    SELECT " +
	        "        AM.MN_ASS_NO, " +
	        "        NULL AS MN_FLAT_NO, " +
	        "        GROUP_CONCAT(DISTINCT AOW.MN_ASSO_OWNER_NAME) AS OWNER_NAME, " +
	        "        AM.MN_ASS_CORR_ADDRESS AS ADDRESS, " +

	        "        ( " +
	        "            SELECT CPD_DESC " +
	        "            FROM tb_comparam_det " +
	        "            WHERE CPD_ID = AM.MN_ASS_PROP_TYPE1 " +
	        "        ) AS PROPERTY_TYPE, " +

	        "        AM.SMS_PAY_LINK " +

	        "    FROM tb_as_assesment_mast AM " +

	        "    INNER JOIN tb_as_assesment_owner_dtl AOW " +
	        "        ON AM.MN_ASS_ID = AOW.MN_ASS_ID " +

	        "    WHERE AM.MN_ASS_ID IN ( " +
	        "        SELECT MAX(MN_ASS_ID) " +
	        "        FROM tb_as_assesment_mast " +
	        "        WHERE MN_ASS_NO = AM.MN_ASS_NO " +
	        "        GROUP BY MN_ASS_NO, MN_FLAT_NO " +
	        "    ) " +

	        "    AND AM.CPD_BILLMETH = 100000330 " +
	        "    AND AM.MN_ASS_NO = :propNo " +

	        "    GROUP BY " +
	        "        AM.MN_ASS_NO, " +
	        "        MN_FLAT_NO, " +
	        "        ADDRESS, " +
	        "        PROPERTY_TYPE, " +
	        "        AM.SMS_PAY_LINK " +
	        ") AS AM " +

	        "ON AM.MN_ASS_NO = BM.MN_PROP_NO",
	        nativeQuery = true)
	public List<Object[]> getWholePropertyDataForDues(
	        @Param("propNo") String propNo
	);








	@Query(value = 
	        "SELECT " +
	        "    AM.MN_ASS_NO AS PROPERTY_NUMBER, " +
	        "    AM.MN_FLAT_NO AS FLAT_NUMBER, " +
	        "    AM.OCCUPIER_NAME AS OWNER_OCCUPIER_NAME, " +
	        "    (BM.CURRENT + BM.CURRENT_PENALTY + BM.ARREAR + BM.ARREAR_PENALTY) AS TOTAL_OUSTANDING, " +
	        "    AM.ADDRESS, " +
	        "    AM.PROPERTY_TYPE, " +
	        "    BM.LAST_BILL_DATE, " +
	        "    AM.SMS_PAY_LINK " +
	        "FROM " +
	        "( " +
	        "    SELECT " +
	        "        BM.MN_PROP_NO, " +
	        "        BM.PD_FLATNO, " +
	        "        BM.MN_BM_IDNO, " +
	        "        DATE(BM.MN_BILLDT) AS LAST_BILL_DATE, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID != 100000020 " +
	        "                        THEN BD.BD_CUR_BAL_TAXAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS CURRENT, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.BD_CUR_BAL_TAXAMT != 0 " +
	        "                             AND BD.BD_PRV_BAL_ARRAMT != 0 " +
	        "                        THEN IFNULL(BD.penalty_as_on_date, BD.BD_CUR_BAL_TAXAMT) " +
	        "                        ELSE 0 " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS CURRENT_PENALTY, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID != 100000020 " +
	        "                        THEN BD.BD_PRV_BAL_ARRAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS ARREAR, " +

	        "        IFNULL( " +
	        "            ROUND( " +
	        "                SUM( " +
	        "                    CASE " +
	        "                        WHEN BD.TAX_ID = 100000020 " +
	        "                        THEN BD.BD_PRV_BAL_ARRAMT " +
	        "                    END " +
	        "                ), 0 " +
	        "            ), 0 " +
	        "        ) AS ARREAR_PENALTY " +

	        "    FROM tb_as_bill_mas BM " +

	        "    INNER JOIN tb_as_bill_det BD " +
	        "        ON BM.MN_BM_IDNO = BD.BM_IDNO " +

	        "    INNER JOIN ( " +
	        "        SELECT " +
	        "            MN_PROP_NO, " +
	        "            IFNULL(PD_FLATNO, '') AS FLAT_NUMBER, " +
	        "            MAX(MN_BM_IDNO) AS MN_BM_IDNO " +
	        "        FROM tb_as_bill_mas BM " +
	        "        WHERE (PD_FLATNO IS NOT NULL AND PD_FLATNO != '') " +
	        "          AND BM.MN_PROP_NO = :propNo " +
	        "          AND BM.PD_FLATNO = :flatNo " +
	        "        GROUP BY MN_PROP_NO, FLAT_NUMBER " +
	        "    ) MAX_BM_ID " +
	        "        ON BM.MN_BM_IDNO = MAX_BM_ID.MN_BM_IDNO " +

	        "    WHERE BM.MN_PROP_NO = :propNo " +
	        "      AND BM.PD_FLATNO = :flatNo " +

	        "    GROUP BY " +
	        "        BM.MN_PROP_NO, " +
	        "        BM.PD_FLATNO, " +
	        "        BM.MN_BM_IDNO, " +
	        "        LAST_BILL_DATE " +
	        ") AS BM " +

	        "INNER JOIN ( " +
	        "    SELECT " +
	        "        AM.MN_ASS_NO, " +
	        "        AM.MN_FLAT_NO, " +
	        "        AD.MN_ASSD_OCCUPIER_NAME AS OCCUPIER_NAME, " +
	        "        AM.MN_ASS_CORR_ADDRESS AS ADDRESS, " +

	        "        ( " +
	        "            SELECT CPD_DESC " +
	        "            FROM tb_comparam_det " +
	        "            WHERE CPD_ID = AM.MN_ASS_PROP_TYPE1 " +
	        "        ) AS PROPERTY_TYPE, " +

	        "        AM.SMS_PAY_LINK " +

	        "    FROM tb_as_assesment_mast AM " +

	        "    INNER JOIN tb_as_assesment_detail AD " +
	        "        ON AM.MN_ASS_ID = AD.MN_ASS_ID " +

	        "    WHERE AM.MN_ASS_ID IN ( " +
	        "        SELECT MAX(MN_ASS_ID) " +
	        "        FROM tb_as_assesment_mast " +
	        "        WHERE MN_ASS_NO = AM.MN_ASS_NO " +
	        "        GROUP BY MN_ASS_NO, MN_FLAT_NO " +
	        "    ) " +

	        "    AND AD.MN_ASSD_ID IN ( " +
	        "        SELECT MAX(MN_ASSD_ID) " +
	        "        FROM tb_as_assesment_detail " +
	        "        WHERE MN_ASS_ID = AM.MN_ASS_ID " +
	        "        GROUP BY MN_ASS_ID, MN_FLAT_NO " +
	        "    ) " +

	        "    AND AM.CPD_BILLMETH = 100000332 " +
	        "    AND AM.MN_ASS_NO = :propNo " +
	        "    AND AM.MN_FLAT_NO = :flatNo " +
	        ") AS AM " +

	        "ON AM.MN_ASS_NO = BM.MN_PROP_NO " +
	        "AND AM.MN_FLAT_NO = BM.PD_FLATNO",
	        nativeQuery = true)
	public List<Object[]> getIndividualPropertyDataForDues(
	        @Param("propNo") String propNo,
	        @Param("flatNo") String flatNo
	);





@Query(value = 
"SELECT " +
"   TC.CS_CCN AS connection_no, " +
"   CONCAT(IFNULL(TC.CS_NAME, ''), ' ', IFNULL(TC.CS_MNAME, ''), '', IFNULL(TC.CS_LNAME, '')) AS owner_name, " +
"   CONCAT(IFNULL(TC.CS_FLATNO, ''), ' ', IFNULL(TC.CS_BLDPLT, ''), '', IFNULL(TC.CS_LANEAR, ''), '', IFNULL(TC.CS_RDCROSS, '')) AS address, " +
"   (SELECT CPD_DESC FROM tb_comparam_det WHERE CPD_ID = TC.CS_METEREDCCN) AS connection_type, " +
"   TC.SMS_PAY_LINK, " +
"   IFNULL(( " +
"       SELECT IFNULL(DATE(BM1.BM_BILLDT), '') " +
"       FROM tb_wt_bill_mas BM1 " +
"       WHERE BM1.CS_IDN = TC.CS_IDN " +
"         AND BM1.BM_IDNO IN ( " +
"             SELECT MAX(BM.BM_IDNO) " +
"             FROM tb_wt_bill_mas BM " +
"             WHERE BM.CS_IDN = TC.CS_IDN " +
"         ) " +
"   ), '') AS last_bill_date, " +
"   ( " +
"       IFNULL(( " +
"           SELECT IFNULL(SUM(BD.BD_PRV_BAL_ARRAMT), 0) " +
"           FROM tb_wt_bill_mas BM1 " +
"           INNER JOIN tb_wt_bill_det BD ON BM1.BM_IDNO = BD.BM_IDNO " +
"           WHERE BM1.CS_IDN = TC.CS_IDN " +
"             AND BM1.BM_IDNO IN ( " +
"                 SELECT MAX(BM.BM_IDNO) " +
"                 FROM tb_wt_bill_mas BM " +
"                 WHERE BM.CS_IDN = TC.CS_IDN " +
"             ) " +
"       ), 0) + " +
"       IFNULL(( " +
"           SELECT IFNULL(SUM(BD.BD_CUR_BAL_TAXAMT), 0) " +
"           FROM tb_wt_bill_mas BM1 " +
"           INNER JOIN tb_wt_bill_det BD ON BM1.BM_IDNO = BD.BM_IDNO " +
"           WHERE BM1.CS_IDN = TC.CS_IDN " +
"             AND BM1.BM_IDNO IN ( " +
"                 SELECT MAX(BM.BM_IDNO) " +
"                 FROM tb_wt_bill_mas BM " +
"                 WHERE BM.CS_IDN = TC.CS_IDN " +
"             ) " +
"       ), 0) + " +
"       IFNULL(( " +
"           SELECT IFNULL(SUM(WP.SUR_AMOUNT_AS_ON_DATE), 0) " +
"           FROM tb_wt_bill_mas BM1, tb_wt_penalty WP " +
"           WHERE BM1.CS_IDN = TC.CS_IDN " +
"             AND BM1.BM_IDNO = WP.BM_IDNO " +
"             AND DATE_FORMAT(WP.SUR_CAL_AS_ON_DATE, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') " +
"             AND BM1.BM_TOTAL_AMOUNT > 0 " +
"             AND IFNULL(BM1.BM_TOTAL_BAL_AMOUNT, 0) > 0 " +
"       ), 0) " +
"   ) AS outstanding " +
"FROM tb_csmr_info TC " +
"WHERE TC.CS_CCN IS NOT NULL " +
"  AND TC.CS_CCN = :ccnNo",
nativeQuery = true)
public List<Object[]> getWaterNoDuesData(@Param("ccnNo") String ccnNo);





@Query(value = "SELECT IFNULL(( " +
        "SELECT 'Y' " +
        "FROM tb_wt_nodues WND " +
        "INNER JOIN tb_csmr_info CSMR ON CSMR.CS_IDN = WND.CS_IDN " +
        "WHERE WND.LMODDATE BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
        "AND DATE(WND.LMODDATE) >= ( " +
        "    SELECT DATE(FA_FROMDATE) " +
        "    FROM tb_financialyear " +
        "    WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
        ") " +
        "AND CSMR.CS_CCN = :ccnNo " +
        "ORDER BY WND.CA_ID DESC " +
        "LIMIT 1 " +
        "), 'N') AS NO_DUES_STATUS",
    nativeQuery = true)
public String checkWaterNoDuesPaymentStatus(@Param("ccnNo") String ccnNo);





@Query(value = "SELECT " +
        "DATE_FORMAT(WND.LMODDATE, '%d/%m/%Y') AS CERTIFICATE_DATE, " +
        "WND.WT_V1 AS CERTIFICATE_NUMBER, " +
        "CONCAT(IFNULL(CSMR.CS_NAME, ''), ' ', IFNULL(CSMR.CS_MNAME, ''), ' ', IFNULL(CSMR.CS_LNAME, '')) AS OWNER_NAME, " +
        "WND.WT_V1 AS APPLICATION_ID, " +
        "DATE_FORMAT(WND.LMODDATE, '%d/%m/%Y') AS APPLICATION_DATE, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = CSMR.COD_DWZID1) AS DIVISION, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = CSMR.COD_DWZID2) AS WARD, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = CSMR.COD_DWZID3) AS ZONE, " +
        "CSMR.CS_CCN AS CONNECTION_NUMBER, " +
        "CONCAT(IFNULL(CSMR.CS_FLATNO, ''), ' ', IFNULL(CSMR.CS_BLDPLT, ''), ' ', IFNULL(CSMR.CS_LANEAR, ''), ' ', IFNULL(CSMR.CS_RDCROSS, '')) AS ADDRESS " +
        "FROM tb_wt_nodues WND " +
        "INNER JOIN tb_csmr_info CSMR ON CSMR.CS_IDN = WND.CS_IDN " +
        "WHERE CSMR.CS_CCN = :ccnNo " +
        "AND WND.LMODDATE BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
        "AND DATE(WND.LMODDATE) >= ( " +
        "    SELECT DATE(FA_FROMDATE) " +
        "    FROM tb_financialyear " +
        "    WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
        ") " +
        "ORDER BY WND.CA_ID DESC " +
        "LIMIT 1",
    nativeQuery = true)
public List<Object[]> getWaterCertificatePrint(@Param("ccnNo") String ccnNo);




@Query(value = "SELECT " +
        "PND.OUTWARD_NO AS CERTIFICATE_NUMBER, " +
        "DATE_FORMAT(PND.CREATED_DATE, '%d/%m/%Y') AS CERTIFICATE_DATE, " +
        "PND.APPLICATION_ID AS TOKEN_NUMBER, " +
        "PND.OWNER_NAME AS OCCUPIER_NAME, " +
        "AM.OWNER_NAME, " +
        "PND.PROP_NO AS PROPERTY_NUMBER, " +
        "PND.FLAT_NO AS FLAT_NUMBER, " +
        "PND.OWNER_ADDRESS AS PROPERTY_ADDRESS, " +
        "(SELECT COD_DESC FROM TB_COMPARENT_DET WHERE COD_ID = AM.MN_ASS_WARD1) AS WARD, " +
        "(SELECT COD_DESC FROM TB_COMPARENT_DET WHERE COD_ID = AM.MN_ASS_WARD2) AS ZONE, " +
        "CONCAT(CFC.APM_FNAME, ' ', CFC.APM_MNAME, ' ', CFC.APM_LNAME) AS APPLICANT_NAME, " +
        "(SELECT CPD_DESC FROM TB_COMPARAM_DET WHERE CPD_ID = AM.MN_ASS_PROP_TYPE1) AS PROPERTY_TYPE, " +
        "DATE(FY.FA_TODATE) AS BILL_TO_DATE " +
        "FROM tb_as_no_dues_prop_details PND " +
        "INNER JOIN tb_cfc_application_mst CFC ON CFC.APM_APPLICATION_ID = PND.APPLICATION_ID " +
        "INNER JOIN ( " +
        "    SELECT DISTINCT AM.MN_ASS_NO, MN_ASS_PROP_TYPE1, AM.MN_ASS_WARD1, AM.MN_ASS_WARD2, " +
        "           GROUP_CONCAT(DISTINCT AOW.MN_ASSO_OWNER_NAME) AS OWNER_NAME " +
        "    FROM tb_as_assesment_mast AM " +
        "    INNER JOIN tb_as_assesment_owner_dtl AOW ON AOW.MN_ASS_ID = AM.MN_ASS_ID " +
        "    WHERE AM.MN_ASS_NO = :propNo " +
        "      AND AM.MN_FLAT_NO = :flatNo " +
        "      AND AM.MN_ASS_ID IN ( " +
        "          SELECT MAX(MN_ASS_ID) FROM tb_as_assesment_mast " +
        "          WHERE MN_ASS_NO = AM.MN_ASS_NO AND MN_FLAT_NO = AM.MN_FLAT_NO " +
        "          GROUP BY MN_ASS_NO, MN_FLAT_NO " +
        "      ) " +
        "    GROUP BY AM.MN_ASS_NO, MN_ASS_PROP_TYPE1, AM.MN_ASS_WARD1, AM.MN_ASS_WARD2 " +
        ") AM ON AM.MN_ASS_NO = PND.PROP_NO " +
        "INNER JOIN tb_financialyear FY ON FY.FA_YEARID = PND.FY_ID " +
        "WHERE DATE(PND.CREATED_DATE) BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
        "  AND PND.FY_ID = ( " +
        "      SELECT FA_YEARID FROM tb_financialyear " +
        "      WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
        "  ) " +
        "  AND PND.PROP_NO = :propNo " +
        "  AND PND.FLAT_NO = :flatNo " +
        "ORDER BY PROPDET_ID DESC",
    nativeQuery = true)
public List<Object[]> getIndividualPropertyCertificatePrint(@Param("propNo") String propNo, @Param("flatNo") String flatNo);



@Query(value =
"SELECT " +
"IFNULL( " +
"   ( " +
"       SELECT 'Y' " +
"       FROM tb_as_no_dues_prop_details PND " +
"       WHERE CREATED_DATE BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
"         AND FY_ID = ( " +
"               SELECT FA_YEARID " +
"               FROM tb_financialyear " +
"               WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
"         ) " +
"         AND PND.PROP_NO = :propNo " +
"         AND PND.FLAT_NO = :flatNo " +
"   ), " +
"   'N' " +
") AS NO_DUES_STATUS",
nativeQuery = true)
public String getIndividualPropertyFlagCheck(@Param("propNo") String propNo,
                                     @Param("flatNo") String flatNo);



@Query(value =
"SELECT " +
"IFNULL( " +
"   ( " +
"       SELECT 'Y' " +
"       FROM tb_as_no_dues_prop_details PND " +
"       WHERE CREATED_DATE BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
"         AND FY_ID = ( " +
"               SELECT FA_YEARID " +
"               FROM tb_financialyear " +
"               WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
"         ) " +
"         AND PND.PROP_NO = :propNo " +
"         AND (PND.FLAT_NO IS NULL OR PND.FLAT_NO = '') " +
"       ORDER BY PROPDET_ID DESC " +
"       LIMIT 1 " +
"   ), " +
"   'N' " +
") AS NO_DUES_STATUS",
nativeQuery = true)
public String getWholePropertyFlagCheck(@Param("propNo") String propNo);






@Query(value =
"SELECT " +
"   PND.OUTWARD_NO AS CERTIFICATE_NUMBER, " +
"   DATE_FORMAT(PND.CREATED_DATE, '%d/%m/%Y') AS CERTIFICATE_DATE, " +
"   (SELECT COD_DESC FROM TB_COMPARENT_DET WHERE COD_ID = AM.MN_ASS_WARD1) AS WARD, " +
"   (SELECT COD_DESC FROM TB_COMPARENT_DET WHERE COD_ID = AM.MN_ASS_WARD2) AS ZONE, " +
"   PND.APPLICATION_ID AS TOKEN_NUMBER, " +
"   PND.OWNER_NAME, " +
"   PND.PROP_NO AS PROPERTY_NUMBER, " +
"   PND.OWNER_ADDRESS AS PROPERTY_ADDRESS, " +
"   (SELECT CPD_DESC FROM TB_COMPARAM_DET WHERE CPD_ID = AM.MN_ASS_PROP_TYPE1) AS PROPERTY_TYPE, " +
"   DATE(FY.FA_TODATE) AS BILL_TO_DATE, " +
"   CONCAT(CFC.APM_FNAME, ' ', CFC.APM_MNAME, ' ', CFC.APM_LNAME) AS APPLICANT_NAME " +
"FROM tb_as_no_dues_prop_details PND " +
"INNER JOIN ( " +
"   SELECT DISTINCT MN_ASS_NO, MN_ASS_PROP_TYPE1, MN_ASS_WARD1, MN_ASS_WARD2 " +
"   FROM tb_as_assesment_mast AM " +
"   WHERE AM.MN_ASS_NO = :propNo " +
") AM ON AM.MN_ASS_NO = PND.PROP_NO " +
"INNER JOIN tb_financialyear FY ON FY.FA_YEARID = PND.FY_ID " +
"INNER JOIN tb_cfc_application_mst CFC ON PND.APPLICATION_ID = CFC.APM_APPLICATION_ID " +
"WHERE DATE(PND.CREATED_DATE) BETWEEN DATE_SUB(NOW(), INTERVAL 90 DAY) AND NOW() " +
"  AND PND.FY_ID = ( " +
"      SELECT FA_YEARID FROM tb_financialyear " +
"      WHERE DATE(NOW()) BETWEEN DATE(FA_FROMDATE) AND DATE(FA_TODATE) " +
"  ) " +
"  AND (PND.FLAT_NO IS NULL OR PND.FLAT_NO = '') " +
"  AND PND.PROP_NO = :propNo " +
"ORDER BY PROPDET_ID DESC " +
"LIMIT 1",
nativeQuery = true)
public List<Object[]> getWholePropertyCertificatePrint(@Param("propNo") String propNo);


@Query(
	    value = "SELECT " +
	            "SUM(BD.BD_CUR_TAXAMT) AS CUR_DEMAND, " +
	            "SUM(BD.BD_CUR_BAL_TAXAMT) AS CUR_BALANCE " +
	            "FROM tb_as_bill_det BD " +
	            "WHERE BD.BM_IDNO = :bmIdno",
	    nativeQuery = true
	)
	public List<Object[]> getCurrYearAmtByBmIdno(@Param("bmIdno") Long bmIdno);








	
}
