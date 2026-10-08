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

@Repository
public interface BlockChainRepository extends JpaRepository<BlockChainCertificateEntity, Long> {
    // No code needed, JpaRepository provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.
	
	@Query(value = "select AppId from tb_blockchain_data where AppId=:appId AND orgId=:orgId", nativeQuery = true)
	public String findBlockChainBrId(@Param("appId") Long appId, @Param("orgId") Long orgId);
	
	@Query(value = "select UDF1 from tb_blockchain_data where AppId=:appId AND orgId=:orgId", nativeQuery = true)
	public String findBlockChainVerificationUrl(@Param("appId") Long appId, @Param("orgId") Long orgId);
	
	@Query(value = "select UID from tb_blockchain_data where AppId=:appId AND orgId=:orgId", nativeQuery = true)
	public String fetchBlockChainUid(@Param("appId") Long appId, @Param("orgId") Long orgId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE TB_BLOCKCHAIN_DATA " +
	               "SET Reissue = :reissueFlag, " +
	               "    UDF2 = :jsonRequest, " +
	               "    UPDATED_DATE = :sysDate " +
	               "WHERE AppId = :appId " +
	               "AND orgId = :orgId", 
	       nativeQuery = true)
	int updateReissueFlag(@Param("reissueFlag") String reissueFlag,
	                      @Param("jsonRequest") String jsonRequest,
	                      @Param("appId") Long appId,
	                      @Param("orgId") Long orgId,
	                      @Param("sysDate") Date sysDate);
	
	
	@Query(value = "SELECT " +
	        "DATE(MR.UPDATED_DATE) AS MARRIAGE_REGISTRATION_DATE, " +
	        "DATE(MR.MAR_DATE) AS MARRIAGE_DATE, " +
	        "CONCAT_WS(' ',NULLIF(MRH.FIRST_NAME_E, ''),NULLIF(MRH.MIDDLE_NAME_E, ''),NULLIF(MRH.LAST_NAME_E, '')) AS HUSBAND_NAME, " +
	        "CONCAT_WS(' ',NULLIF(MRW.FIRST_NAME_E,''),NULLIF(MRW.MIDDLE_NAME_E,''),NULLIF(MRW.LAST_NAME_E,'')) AS WIFE_NAME, " +
	        "MRH.UID_NO AS HUSBAND_AADHAR, " +
	        "MRW.UID_NO AS WIFE_AADHAR, " +
	        "MRH.FULL_ADDR_E AS HUSBAND_ADDRESS, " +
	        "MRW.FULL_ADDR_E AS WIFE_ADDRESS " +
	        "FROM tb_mrm_marriage MR " +
	        "INNER JOIN tb_mrm_husband MRH ON MRH.MAR_ID = MR.MAR_ID " +
	        "INNER JOIN tb_mrm_wife MRW ON MRW.MAR_ID = MR.MAR_ID " +
	        "INNER JOIN tb_receipt_mas RM ON RM.APM_APPLICATION_ID = MR.APPLICATION_ID " +
	        "WHERE MR.APPLICATION_ID = :appId " +
	        "AND MR.ORGID = :orgId " +
	        "AND RM.RECEIPT_DEL_FLAG IS NULL",
	       nativeQuery = true)
	public List<Object[]> getMrmData(@Param("appId") String appId,
	                                 @Param("orgId") String orgId);




@Query(value = "SELECT LM.TRD_LICNO AS LICENSE_NUMBER, " +
        "LMO.TRO_NAME AS OWNER_NAME, " +
        "LM.TRD_BUSNM AS BUSSINESS_NAME, " +
        "LM.TRD_BUSADD AS BUSSINESS_ADDRESS, " +
        "LM.TRD_LICISDATE AS LICENSE_DATE, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD1) AS CATEGORY, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD2) AS SUB_CATEGORY, " +
        "(SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD4) AS MEASURMENT_TYPE, " +
        "LMI.TRD_QUANTITY AS MEASURMENT_UNIT, " +
        "CONCAT(LM.TRD_LICFROM_DATE, ' TO ', LM.TRD_LICTO_DATE) AS LICENSE_PERIOD " +
        "FROM tb_ml_trade_mast LM " +
        "INNER JOIN tb_ml_owner_detail LMO ON LM.TRD_ID = LMO.TRD_ID " +
        "INNER JOIN tb_ml_item_detail LMI ON LM.TRD_ID = LMI.TRD_ID " +
        "INNER JOIN tb_ml_trade_mast_hist LMH ON LMH.TRD_ID = LM.TRD_ID " +
        "INNER JOIN tb_receipt_mas RM ON RM.APM_APPLICATION_ID = LMH.APM_APPLICATION_ID " +
        "WHERE RM.RECEIPT_DEL_FLAG IS NULL " +
        "AND RM.SM_SERVICE_ID = (SELECT SM_SERVICE_ID FROM tb_services_mst WHERE SM_SHORTDESC = 'DTL') " +
        "AND LMH.APM_APPLICATION_ID = :appId " +
        "AND LM.ORGID = :orgId",
nativeQuery = true)
public List<Object[]> getDuplicateLicCertificateData(@Param("appId") String appId, 
                                                 @Param("orgId") String orgId);




@Query(value = "SELECT " +
        "BDR.BR_CHILDNAME AS CHILD_NAME, " +
        "DATE_FORMAT(BDR.BR_DOB, '%d-%m-%Y') AS DOB, " +
        "BDP.PD_FATHERNAME AS FATHER_NAME_ENG, " +
        "BDP.PD_MOTHERNAME AS MOTHER_NAME_ENG, " +
        "BDP.PD_ADDRESS AS PARENT_PER_ADDRESS_ENG, " +
        "BDR.BR_BIRTHPLACE AS BIRTH_PLACE, " +
        "BDR.BR_REGNO AS REG_NO, " +   
        "BDR.BR_CERT_NO AS CERTIFICATE_NO " +
        "FROM tb_birthreg BDR " +
        "INNER JOIN tb_parentdetail BDP ON BDP.BR_ID = BDR.BR_ID " +
        "WHERE BDR.BR_ID = :appId " +
        "AND BDR.ORGID = :orgId",
       nativeQuery = true)
public List<Object[]> getBlockChainBirthCertificateData(@Param("appId") String appId,
                                                       @Param("orgId") String orgId);


@Query(value = "SELECT " +
        "IFNULL(DR.DR_DECEASEDNAME, '') AS DEATH_PERSON_NAME, " +
        "IFNULL(DATE_FORMAT(DR.DR_DOD, '%d-%m-%Y'), '') AS DOD, " +
        "IFNULL(DR.DR_RELATIVE_NAME, '') AS FATHER_HUSBAND_NAME_ENG, " +
        "IFNULL(DR.DR_MOTHER_NAME, '') AS MOTHER_NAME_ENG, " +
        "IFNULL(DR.DR_DEATHPLACE, '') AS DEATH_PLACE, " +
        "IFNULL(DR.DR_REGNO, '') AS REG_NO, " +
        "IFNULL(DR.DR_DECEASEDADDR, '') AS PER_ADDRESS_ENG, " +
        "IFNULL(DR.DR_CERT_NO, '') AS DR_CERTIFICATE_NO " +
        "FROM tb_deathreg DR " +
        "WHERE DR.DR_ID = :appId " +
        "AND DR.ORGID = :orgId",
       nativeQuery = true)
public List<Object[]> getBlockChainDeathCertificateData(@Param("appId") String appId,
                                                       @Param("orgId") String orgId);



@Query(value = "SELECT " +
        "LM.TRD_LICNO AS LICENSE_NUMBER, " +
        "LMO.TRO_NAME AS OWNER_NAME, " +
        "LM.TRD_BUSNM AS BUSSINESS_NAME, " +
        "LM.TRD_BUSADD AS BUSSINESS_ADDRESS, " +
        "LM.TRD_LICISDATE AS LICENSE_DATE, " +
        "GROUP_CONCAT(DISTINCT (SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD1)) AS CATEGORY, " +
        "GROUP_CONCAT((SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD2)) AS SUB_CATEGORY, " +
        "GROUP_CONCAT((SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD4)) AS MEASURMENT_TYPE, " +
        "GROUP_CONCAT(LMI.TRD_QUANTITY) AS MEASURMENT_UNIT, " +
        "CONCAT(LM.TRD_LICFROM_DATE, '-', LM.TRD_LICTO_DATE) AS LICENSE_PERIOD " +
        "FROM tb_ml_trade_mast LM " +
        "INNER JOIN tb_ml_owner_detail LMO ON LM.TRD_ID = LMO.TRD_ID " +
        "INNER JOIN tb_ml_item_detail LMI ON LM.TRD_ID = LMI.TRD_ID " +
        "INNER JOIN tb_ml_trade_mast_hist LMH ON LMH.TRD_ID = LM.TRD_ID " +
        "INNER JOIN tb_receipt_mas RM ON RM.APM_APPLICATION_ID = LMH.APM_APPLICATION_ID " +
        "WHERE RM.RECEIPT_DEL_FLAG IS NULL " +
        "AND RM.SM_SERVICE_ID = (SELECT SM_SERVICE_ID FROM tb_services_mst WHERE SM_SHORTDESC = 'DTL') " +
        "AND LMH.APM_APPLICATION_ID = :appId " +
        "AND LM.ORGID = :orgId " +
        "GROUP BY LICENSE_NUMBER, OWNER_NAME, BUSSINESS_NAME, BUSSINESS_ADDRESS, LICENSE_DATE, LICENSE_PERIOD",
       nativeQuery = true)
public List<Object[]> getBlockChainDuplicateLicenseCertificateData(@Param("appId") String appId,
                                                                   @Param("orgId") String orgId);


@Query(value = "SELECT " +
        "LM.TRD_LICNO AS LICENSE_NUMBER, " +
        "LMO.TRO_NAME AS OWNER_NAME, " +
        "LM.TRD_BUSNM AS BUSSINESS_NAME, " +
        "LM.TRD_BUSADD AS BUSSINESS_ADDRESS, " +
        "LM.TRD_LICISDATE AS LICENSE_DATE, " +
        "GROUP_CONCAT(DISTINCT (SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD1)) AS CATEGORY, " +
        "GROUP_CONCAT((SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD2)) AS SUB_CATEGORY, " +
        "GROUP_CONCAT((SELECT COD_DESC FROM tb_comparent_det WHERE COD_ID = LMI.TRI_COD4)) AS MEASURMENT_TYPE, " +
        "GROUP_CONCAT(LMI.TRD_QUANTITY) AS MEASURMENT_UNIT, " +
        "CONCAT(LM.TRD_LICFROM_DATE, ' TO ', LM.TRD_LICTO_DATE) AS LICENSE_PERIOD " +
        "FROM tb_ml_trade_mast LM " +
        "INNER JOIN tb_ml_owner_detail LMO ON LM.TRD_ID = LMO.TRD_ID " +
        "INNER JOIN tb_ml_item_detail LMI ON LM.TRD_ID = LMI.TRD_ID " +
        "INNER JOIN tb_ml_trade_mast_hist LMH ON LMH.TRD_ID = LM.TRD_ID " +
        "INNER JOIN tb_receipt_mas RM ON RM.APM_APPLICATION_ID = LMH.APM_APPLICATION_ID " +
        "WHERE RM.RECEIPT_DEL_FLAG IS NULL " +
        "AND RM.SM_SERVICE_ID = (SELECT SM_SERVICE_ID FROM tb_services_mst WHERE SM_SHORTDESC = 'NTL') " +
        "AND LMH.APM_APPLICATION_ID = :appId " +
        "AND LM.ORGID = :orgId " +
        "GROUP BY LICENSE_NUMBER, OWNER_NAME, BUSSINESS_NAME, BUSSINESS_ADDRESS, LICENSE_DATE, LICENSE_PERIOD",
       nativeQuery = true)
public List<Object[]> getBlockChainNewLicenseCertificateData(@Param("appId") String appId,
                                                             @Param("orgId") String orgId);





	
}
