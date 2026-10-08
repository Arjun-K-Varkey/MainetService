package com.abm.mainet.common.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.domain.ContractDetailEntity;
import com.abm.mainet.common.domain.ContractInstalmentDetailEntity;
import com.abm.mainet.common.domain.ContractMastEntity;
import com.abm.mainet.common.domain.ContractPart1DetailEntity;
import com.abm.mainet.common.domain.ContractPart2DetailEntity;
import com.abm.mainet.common.domain.TbAcVendormasterEntity;

/**
 * @author apurva.salgaonkar
 *
 */
public interface ContractAgreementRepository extends JpaRepository<ContractMastEntity, Long> {

	@Query("select v.vmVendorid,v.vmVendorname from TbAcVendormasterEntity v where v.orgid =?1 and v.cpdVendortype = ?2 and v.vmCpdStatus =?3")
	List<Object[]> getVenderNameOnVenderType(Long orgId, Long venTypeId, Long statusId);

	@Query("select v.vmVendorname from TbAcVendormasterEntity v where v.orgid =?1 and v.vmVendorid = ?2")
	String getVenderNameOnVenderId(Long orgId, Long venTypeId);

	@Query("select v.vmVendoradd,v.vmPanNumber from TbAcVendormasterEntity v where v.orgid =?1 and v.vmVendorid = ?2")
	List<Object[]> getVenderDetailOnVenderId(Long orgId, Long venTypeId);

	@Query("select v from TbAcVendormasterEntity v where v.orgid =?1 and v.vmVendorid = ?2")
	List<TbAcVendormasterEntity> getVenderTypeOnVenderId(Long orgId, Long venderId);

	@Query("select d.dpDeptid,d.dpDeptdesc,d.dpNameMar from Department d where d.dpDeptid in( select p1.dpDeptid from  ContractPart1DetailEntity p1 where p1.orgId=?1)")
	List<Object[]> getDepartmentList(Long orgId);

	@Query("select  v.vmVendorid,v.vmVendorname from TbAcVendormasterEntity v where v.vmVendorid in( select p2.vmVendorid from  ContractPart2DetailEntity p2 where p2.orgId=?1)")
	List<Object[]> getVenderList(Long orgId);

	@Query("select c.contId,c.contNo,c.contDept ,c.contDate,d.contFromDate,d.contToDate, p1.contp1Name,p2.contp2Name from  ContractMastEntity c ,ContractDetailEntity d,ContractPart1DetailEntity p1 ,ContractPart2DetailEntity p2 "
			+ " where c.contId=d.contId and c.contId=p1.contId and c.contId=p2.contId and p1.contp1Type='U' and p2.contp2Type='V' and c.orgId=?1)")
	List<Object[]> getContractAgreementSummary(Long orgId);

	@Query("from ContractInstalmentDetailEntity i where i.contId.contId =?1 and i.conttActive =?2 order by i.conitId asc")
	List<ContractInstalmentDetailEntity> finAllRecords(Long contId, String flag);

	@Query("from ContractMastEntity c where c.orgId =?1 and c.contId = ?2 ")
	List<ContractMastEntity> findContractsView(Long orgId, Long contId);

	@Query("select cm from ContractMastEntity cm where cm.contId=?1 and cm.orgId =?2")
	ContractMastEntity getContractByContId(Long contId, Long orgId);

	@Modifying
	@Query("update ContractMastEntity c set c.contMapFlag ='Y', c.updatedBy = ?2,c.updatedDate = CURRENT_DATE where c.contId = ?1")
	int updateContractMapFlag(Long contId, Long empId);

	@Query("select c.contMapFlag from ContractMastEntity c where c.orgId =?1 and c.contId =?2")
	String findContractMapedOrNot(Long orgId, Long contId);

	@Modifying
	@Transactional
	@Query("delete from ContractPart1DetailEntity p1 where p1.contp1Id =?1")
	void deletePart1Row(Long contp1Id);

	@Modifying
	@Transactional
	@Query("delete from ContractPart2DetailEntity p2 where p2.contp2Id =?1")
	void deletePart2Row(Long contp2Id);

	@Modifying
	@Transactional
	@Query("delete from ContractTermsDetailEntity t where t.conttId =?1")
	void deleteTermsRow(Long contId);

	@Modifying
	@Transactional
	@Query("delete from ContractInstalmentDetailEntity i where i.conitId =?1")
	void deleteInstalmentRow(Long contId);

	@Query("from ContractDetailEntity i where i.contId.contId =?1 and i.contdActive =?2")
	ContractDetailEntity findContractDetail(Long contId, String flag);

	/**
	 * used to get Contract Master Entity by loa number
	 * 
	 * @param LOANo
	 * @param orgid
	 * @return
	 */
	@Query("select cm from ContractMastEntity cm where cm.loaNo=:loaNo and cm.orgId=:orgId")
	ContractMastEntity getContractByLoaNo(@Param("loaNo") String loaNo, @Param("orgId") Long orgId);

	@Query("from ContractMastEntity c where c.contDept =?1 and c.orgId=?2")
	List<ContractMastEntity> findAllContractUnderDept(Long contDept, Long orgId);

	@Query("select c.contMapFlag from ContractMastEntity c where c.contDept =?1 and c.contId =?2")
	String findContractMapedOrNotUnderDept(Long deptId, Long contId);

	@Query("from TbAcVendormasterEntity v where v.orgid =?1 and v.vmVendorid = ?2")
	TbAcVendormasterEntity getVenderbyId(Long orgId, Long venTypeId);

	@Query("from ContractDetailEntity cm where cm.contId=?1 and cm.orgId=?2")
	List<ContractDetailEntity> getContractDetail( ContractMastEntity contId, Long orgId);

	@Query("from ContractPart1DetailEntity cm where cm.contId=?1 and cm.orgId=?2")
	List<ContractPart1DetailEntity> getContractParty1Detail(ContractMastEntity contId, Long orgId);

	@Query("from ContractPart2DetailEntity cm where cm.contId=?1 and cm.orgId=?2")
	List<ContractPart2DetailEntity> getContractParty2Detail(ContractMastEntity contId, Long orgId);
	
	@Query("from ContractMastEntity c where c.contDept =?1 and c.orgId=?2 and c.apprApplicable=?3 order by c.contId desc")
	List<ContractMastEntity> findAllContractUnder(Long contDept, Long orgId ,Long apprApplicable);
	
	@Query("from ContractInstalmentDetailEntity i where i.contId.contId =?1 and i.conttActive =?2 order by i.conitId desc")
	List<ContractInstalmentDetailEntity> finAllRecord(Long contId, String flag);
	
	@Query(value = "select distinct  dsg.DSGID,dsg.DSGNAME,dsg.DSGNAME_REG from designation dsg\r\n" + 
			"join employee emp on dsg.DSGID = emp.DSGID\r\n" + 
			"join tb_department dpt on emp.DP_DEPTID = dpt.DP_DEPTID\r\n" + 
			"where dpt.DP_DEPTID =:deptId and emp.ORGID=:orgId", nativeQuery = true)
	List<Object[]> getAllDesgBasedOnDept(@Param("deptId") Long deptId, @Param("orgId") Long orgId);

}
