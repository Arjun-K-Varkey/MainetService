package com.abm.mainet.common.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.domain.TbCfcApplicationMstEntity;

/**
 * Repository : TbCfcApplicationMst.
 */
public interface TbCfcApplicationMstJpaRepository extends PagingAndSortingRepository<TbCfcApplicationMstEntity, Long> {

	@Query("select count(cfcApplicationMst.tbServicesMst.smServiceId) from TbCfcApplicationMstEntity cfcApplicationMst "
			+ " where cfcApplicationMst.tbServicesMst.smServiceId = :serviceId and cfcApplicationMst.tbOrganisation.orgid = :orgId")
	Long checkForTransactionExist(@Param("serviceId") Long serviceId, @Param("orgId") Long orgId);

	@Query("select appMst from TbCfcApplicationMstEntity appMst where appMst.tbOrganisation.orgid=:orgid")
	List<TbCfcApplicationMstEntity> getExistApplicationMstForDelete(@Param("orgid") Long orgid);

	@Query("select p.apmFname,p.apmMname,p.apmLname from  TbCfcApplicationMstEntity p where p.apmApplicationId=?1 and p.tbOrganisation.orgid=?2")
	List<Object[]> getApplicantInfo(Long applicationNo, Long orgId);

	@Query("select p.apmFname,p.apmMname,p.apmLname from  TbCfcApplicationMstEntity p where p.apmApplicationId=?1 and p.tbOrganisation.orgid=?2")
	List<TbCfcApplicationMstEntity> getApplicantAllInfo(Long applicationNo, Long orgId);

	@Query("select a from TbCfcApplicationMstEntity a join fetch a.tbServicesMst s where a.apmApplicationId=:apmapplicationid")
	Optional<TbCfcApplicationMstEntity> findByApmApplicationId(@Param("apmapplicationid") Long apmapplicationid);

	@Query("select a from TbCfcApplicationMstEntity a join fetch a.tbServicesMst s where a.tbOrganisation.orgid =:orgId AND s.smServiceId in (:serviceIds) and a.apmApplSuccessFlag !='C' "
			+ "AND a.apmApplicationId in (select ca.apmApplicationId from CFCApplicationAddressEntity ca where ca.apaMobilno =:mobileNo)")
	List<TbCfcApplicationMstEntity> fetchCfcApplicationsByIds(@Param("serviceIds") List<Long> serviceIds,
			@Param("orgId") Long orgId, @Param("mobileNo") String mobileNo);

	@Query("select a from TbCfcApplicationMstEntity a join fetch a.tbServicesMst s where a.tbOrganisation.orgid =:orgId AND s.smServiceId in (:serviceIds) order by a.apmApplicationId  desc")
	List<TbCfcApplicationMstEntity> fetchCfcApplicationsByServiceIds(@Param("serviceIds") List<Long> serviceIds,
			@Param("orgId") Long orgId);

	@Query("select p.apmPayStatFlag from  TbCfcApplicationMstEntity p where p.apmApplicationId=?1 and p.tbOrganisation.orgid=?2")
	String getApplicantPymentStatusByApplNo(Long applicationNo, Long orgId);

	@Query("select p.refNo from  TbCfcApplicationMstEntity p where p.apmApplicationId=:applicationId and p.tbOrganisation.orgid=:orgId ")
	String getLicenseNoByAppIdAndOrgId(@Param("applicationId") Long applicationId, @Param("orgId") Long orgId);

	@Query("select a from TbCfcApplicationMstEntity a where a.tbServicesMst.smServiceId =:serviceId and a.refNo=:refNo")
	TbCfcApplicationMstEntity fetchCfcApplicationsByServiceId(@Param("serviceId") Long serviceId,
			@Param("refNo") String refNo);
	
	@Query("select a.apmApplicationId from TbCfcApplicationMstEntity a where a.tbServicesMst.smServiceId =:serviceId")
	List<Long> getApplicationIdsByServiceId(@Param("serviceId") Long serviceId);
	
	@Query("select a.refNo from TbCfcApplicationMstEntity a where a.tbServicesMst.smServiceId =:serviceId")
	List<String> getrefNosByServiceId(@Param("serviceId") Long serviceId);
	
	@Query("select a.refNo from TbCfcApplicationMstEntity a where a.apmApplicationId =:apmApplicationId")
	String getrefNosByAppId(@Param("apmApplicationId") Long apmApplicationId);
	//D#125111
	@Query("select count(a) from TbCfcApplicationMstEntity a where (a.apmApplicationId =:apmApplicationId and a.rejctionNo is not null) or (a.apmApplicationId =:apmApplicationId and a.tbServicesMst.smServiceId=:serviceId))")
	int checkApplicationIsRejectedOrNot(@Param("apmApplicationId") Long apmApplicationId,@Param("serviceId") Long serviceId);
	
	@Query("select a.rejctionNo from TbCfcApplicationMstEntity a where a.apmApplicationId =:apmApplicationId and a.tbOrganisation.orgid=:orgId")
	long getRejctionNoByAppId(@Param("apmApplicationId") Long apmApplicationId, @Param("orgId") Long orgId);
	
	@Modifying
	@Transactional
	@Query("UPDATE TbCfcApplicationMstEntity e SET e.apmChklstVrfyFlag = :status WHERE e.apmApplicationId = :apmApplicationId")
	void updateCheklistVerificationFlag(@Param("apmApplicationId") long apmApplicationId ,@Param("status") String status);
	
	  @Query(value = "select a.as_areadesc,a.surveytype,a.PM_SURVEY_NUMBER,a.LM_FROMDT,a.LM_TODT,a.LM_LANDRT from tb_area_land_rate a  where  a.AS_AREADESC =:areaName", nativeQuery = true)
	    List<Object[]> fetchLandRateList( @Param("areaName") String areaName);

	    @Query(value = "SELECT COALESCE((SELECT p.PRO_ASS_NO FROM tb_as_pro_assesment_mast p WHERE p.APM_APPLICATION_ID = :applicationId"
	            + " AND p.PRO_ASS_ID = (SELECT MAX(p2.PRO_ASS_ID) FROM tb_as_pro_assesment_mast p2 WHERE p2.APM_APPLICATION_ID = :applicationId)),"
	            + " (SELECT a.MN_ASS_NO FROM tb_as_assesment_mast a WHERE a.APM_APPLICATION_ID = :applicationId"
	            + " AND a.MN_ASS_ID = (SELECT MAX(a2.MN_ASS_ID) FROM tb_as_assesment_mast a2 WHERE a2.APM_APPLICATION_ID = :applicationId)))"
	            + " AS ASS_NO", nativeQuery = true)
	    String getPropertyNoAsRef(@Param("applicationId") Long applicationId);
	    
	    @Query(value = "select MN_PROP_NO from tb_as_bill_mas where APM_APPLICATION_ID=:applicationId and MN_BM_IDNO in "
	    		+ "(select max(MN_BM_IDNO) from tb_as_bill_mas where APM_APPLICATION_ID=:applicationId) ", nativeQuery = true)
	    String getPropertyNoAsRefFromBill(@Param("applicationId") Long applicationId);
	    
	    @Query(value = "SELECT COALESCE((SELECT a.MN_ASS_ward1 FROM tb_as_assesment_mast a WHERE a.MN_ASS_no = :propNo "
	            + "AND a.MN_ASS_ID = (SELECT MAX(a2.MN_ASS_ID) FROM tb_as_assesment_mast a2 WHERE a2.MN_ASS_no = :propNo)), "
	            + "(SELECT p.PRO_ASS_WARD1 FROM tb_as_pro_assesment_mast p WHERE p.PRO_ASS_no = :propNo "
	            + "AND p.PRO_ASS_ID = (SELECT MAX(p2.PRO_ASS_ID) FROM tb_as_pro_assesment_mast p2 WHERE p2.PRO_ASS_no = :propNo))), "
	            
	            + "COALESCE((SELECT a.MN_ASS_ward2 FROM tb_as_assesment_mast a WHERE a.MN_ASS_no = :propNo "
	            + "AND a.MN_ASS_ID = (SELECT MAX(a2.MN_ASS_ID) FROM tb_as_assesment_mast a2 WHERE a2.MN_ASS_no = :propNo)), "
	            + "(SELECT p.PRO_ASS_WARD2 FROM tb_as_pro_assesment_mast p WHERE p.PRO_ASS_no = :propNo "
	            + "AND p.PRO_ASS_ID = (SELECT MAX(p2.PRO_ASS_ID) FROM tb_as_pro_assesment_mast p2 WHERE p2.PRO_ASS_no = :propNo)))",
	            nativeQuery = true)
	    Object[] fetchWardZoneId(@Param("propNo") String propNo);
	    
	    @Query("select a.apmApplicationId from TbCfcApplicationMstEntity a where a.refNo=:refNo")
		List<Long> getApplicationIdsByRefNo(@Param("refNo") String refNo);
	    
		@Query(value = "SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END FROM tb_workflow_request WHERE APM_APPLICATION_ID = :applicationId", nativeQuery = true)
		int existsByApplicationId(@Param("applicationId") Long applicationId);
}
