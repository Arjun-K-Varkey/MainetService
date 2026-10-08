package com.abm.mainet.common.master.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.domain.GroupMaster;

/**
 * Repository : Employee.
 */
public interface EmployeeJpaRepository extends PagingAndSortingRepository<Employee, Long> {

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ " and e.designation.dsgid =:designId  and e.tbLocationMas.locId =:locId and e.isDeleted='0' ")
	List<Employee> findEmployeeData(@Param("deptId") Long deptId, @Param("locId") Long locId,
			@Param("designId") Long designId, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId and e.isDeleted='0' ")
	List<Employee> findEmployeeData(@Param("deptId") Long deptId, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ " and e.designation.dsgid =:designId and e.isDeleted='0'")
	List<Employee> findEmployeeDatabyDesign(@Param("deptId") Long deptId, @Param("designId") Long designId,
			@Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ "  and e.tbLocationMas.locId =:locId and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByLocID(@Param("deptId") Long deptId, @Param("locId") Long locId,
			@Param("orgid") Long orgid);

	@Modifying
	@Transactional
	@Query("UPDATE Employee e set e.isDeleted='1' where e.organisation.orgid=:orgid and e.empId=:empid")
	void deleteEmployee(@Param("empid") Long empid, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.empmobno=:empmobno "
			+ "  and e.isDeleted='0' and e.emplType is null ")
	List<Employee> getRegisterdMobile(@Param("empmobno") String empmobno, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.empemail=:empemail "
			+ " and e.isDeleted='0' and e.emplType is null ")
	List<Employee> getRegisterdEmail(@Param("empemail") String empemail, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.emploginname=:emploginname "
			+ " and e.isDeleted='0' and e.emplType is null ")
	List<Employee> getRegisterdLoginName(@Param("emploginname") String emploginname, @Param("orgid") Long orgId);

	@Query("select rl from GroupMaster rl where rl.orgId.orgid=:orgid and rl.grStatus='A'")
	List<GroupMaster> getGroupList(@Param("orgid") long orgid);

	// ********************************************
	@Query("select bd from Employee bd where bd.organisation.orgid=:orgId") // and bd.isDeleted ='0'
	List<Employee> getAllEmployee(@Param("orgId") Long orgId);

	@Query("select count(bd.empId) from Employee bd where bd.emploginname = :emploginname and bd.organisation.orgid=:orgId and bd.isDeleted ='0'")
	Long validateEmployee(@Param("emploginname") String emploginname, @Param("orgId") Long orgid);

	@Query("select e.empId,e.empname from Employee e  where e.organisation.orgid=:orgId")
	List<Object[]> getAllEmployeeNames(@Param("orgId") Long orgId);

	@Query("SELECT e.empname,e.empmname,e.emplname,e.empId,e.designation.dsgname "
			+ "FROM Employee e  WHERE e.organisation.orgid=?1 and e.isDeleted = '0' order by e.empname asc")
	List<Object[]> findAllEmpByOrg(Long orgId);

	@Query("SELECT employee"
			+ " FROM Employee employee WHERE employee.emploginname=:empName and employee.organisation.orgid=:orgid and  employee.isDeleted = '0'")
	Employee findEmployeeByName(@Param("empName") String empName, @Param("orgid") Long orgId);

	@Query("select e.empId,e.empname,e.empmname,e.emplname from Employee e  where e.organisation.orgid=:orgId and e.designation.dsgid=:desgId")
	List<Object[]> getAllEmpByDesignation(@Param("desgId") Long desgId, @Param("orgId") Long orgId);

	@Query("SELECT e FROM Employee e  WHERE e.organisation.orgid=?1 and  e.tbDepartment.dpDeptid =?2 and e.isDeleted = '0' order by e.empId asc")
	List<Employee> findAllEmployeeByDept(Long orgId, Long deptId);

	@Query("SELECT e FROM Employee e  WHERE e.tbLocationMas.locId =:locId")
	List<Employee> findAllEmployeeByLocation(@Param("locId") Long locId);

	/**
	 * This method will return all employees belonging in given list of locations
	 * 
	 * @param locIds Set of location ID
	 * @return
	 */
	@Query("SELECT distinct e.empId FROM Employee e  WHERE e.tbLocationMas.locId in (:locIds) order by e.empId asc")
	List<Long> findAllEmployeeByLocation(@Param("locIds") Set<Long> locIds);

	@Query("select bd from Employee bd where bd.organisation.orgid=:orgId and bd.isDeleted ='0' and bd.empId not in (:empId)")
	List<Employee> getAllEmployeeWithLoggedInEmployeeNotPresent(@Param("orgId") Long orgId, @Param("empId") Long empId);

	@Query("SELECT employee" + " FROM Employee employee WHERE employee.gmid=:gmid")
	List<Employee> getByGmId(@Param("gmid") Long gmid);

	@Query("SELECT employee" + " FROM Employee employee WHERE employee.gmid in (:gmids)")
	List<Employee> getByGmId(@Param("gmids") List<Long> gmids);

	@Query("SELECT e.empname,e.empmname,e.emplname,e.empId "
			+ "FROM Employee e  WHERE e.organisation.orgid=?1 and e.tbDepartment.dpDeptid =?2 and e.tbLocationMas.locId in (?3) and e.isDeleted = '0' order by e.empname asc")
	List<Object[]> findAllEmpByLocation(Long orgId, Long deptId, List<Long> locationId);

	@Query("SELECT distinct g.gmId,g.grDescEng,g.grDescReg "
			+ "FROM Employee e , GroupMaster g  WHERE e.gmid = g.gmId and e.organisation.orgid=?1 and e.tbDepartment.dpDeptid =?2 and e.tbLocationMas.locId in (?3) and e.isDeleted = '0' order by e.empname asc")
	List<Object[]> findAllRoleByLocation(Long orgId, Long deptId, List<Long> locationId);

	@Query("SELECT employee FROM Employee employee "
			+ "WHERE employee.empmobno=:mobileNo and employee.organisation.orgid=:orgid")
	List<Employee> validateEmpMobileNo(@Param("mobileNo") String mobileNo, @Param("orgid") Long orgid);

	@Query("SELECT employee FROM Employee employee "
			+ "WHERE employee.empuid=:empuid and employee.organisation.orgid=:orgid")
	List<Employee> validateEmpUid(@Param("empuid") String empuid, @Param("orgid") Long orgid);

	@Query("SELECT employee FROM Employee employee "
			+ "WHERE employee.panNo=:panNo and employee.organisation.orgid=:orgid")
	List<Employee> validateEmpPancardNo(@Param("panNo") String panNo, @Param("orgid") Long orgid);

	@Query("SELECT e.empId, e.empname,e.empmname, e.emplname FROM Employee e WHERE e.tbDepartment.dpDeptid=:dpDeptid AND e.organisation.orgid=:orgId AND e.authStatus='A' ORDER BY e.empname ASC")
	List<Object[]> findActiveEmployeeByDeptId(@Param("dpDeptid") Long dpDeptid, @Param("orgId") Long orgId);

	@Query("select COUNT(e) from Employee e  where e.organisation.orgid=:orgId and e.tbLocationMas.locId=:locId")
	int getEmpByLocation(@Param("orgId") Long orgId, @Param("locId") Long locId);

	@Query("select emp from Employee emp where emp.organisation.orgid = :orgId ")
	List<Employee> getEmpList(@Param("orgId") Long orgId);

	@Query("select emp from Employee emp where emp.organisation.orgid = :orgId and emp.empId in :empIds ")
	List<Employee> getEmpDetailListByEmpIdList(@Param("orgId") Long orgId, @Param("empIds") List<Long> empId);

	@Query("select emp from Employee emp where emp.isDeleted='0' ")
	List<Employee> getAllActiveEmployee();

	@Query("select emp from Employee emp where emp.isDeleted='0' and emp.empmobno=:mobileNo")
	List<Employee> getActiveEmployeeByEmpMobileNo(@Param("mobileNo") String empMobileNo);
	
	@Query("SELECT emp.empId, cm.cuCountcentreno, col.cmCollncentreno "
			+ "FROM Employee emp, CFCCounterScheduleEntity cs, CFCCounterMasterEntity cm, CFCCollectionMasterEntity col "
			+ "WHERE emp.empId = cs.csUserId " + "AND cs.csStatus = 'A' " + "AND cs.cfcCounterMasterEntity = cm "
			+ "AND cm.cfcCollectionMasterEntity = col " + "AND emp.empId = :empId")
		Object[] getActiveEmployeeCounterDetails(@Param("empId") Long empId);

	@Query("select dept.dpDeptdesc, desg.dsgname, loc.locNameEng, org.ONlsOrgname, gp.grCode from Department dept, Designation desg, LocationMasEntity loc, "
			+ " Organisation org, GroupMaster gp where dept.dpDeptid=:deptId and desg.dsgid=:desigId and loc.locId=:locId and org.orgid=:orgId and gp.gmId=:gmId")
	List<Object[]> getDescriptionDetailsByIds(@Param("deptId") Long deptId, @Param("desigId") Long desigId,
			@Param("locId") Long locId, @Param("orgId") Long orgId, @Param("gmId") Long gmId);

	@Query("select emp from Employee emp where emp.organisation.orgid = :orgId and emp.empId = :empId ")
	Employee findEmployeeByIdAndOrgId(@Param("empId") Long empId, @Param("orgId") Long orgId);

	@Query("select rl from GroupMaster rl where rl.orgId.orgid=:orgid and rl.dpDeptId=:dpDeptId and rl.grStatus='A'")
	List<GroupMaster> getGroupList(@Param("orgid") long orgid, @Param("dpDeptId") long dpDeptId);

	@Query("select COUNT(e) from Employee e  where e.organisation.orgid=:orgId and e.gmid=:gmid")
	int getEmpCountByGmIdAndOrgId(@Param("orgId") Long orgId, @Param("gmid") Long gmid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ " and e.designation.dsgid =:designId  and e.tbLocationMas.locId =:locId and e.gmid=:gmid and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByIds(@Param("deptId") Long deptId, @Param("locId") Long locId,
			@Param("designId") Long designId, @Param("orgid") Long orgid, @Param("gmid") Long gmid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ "  and e.gmid=:gmid and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByGmId(@Param("deptId") Long deptId, @Param("gmid") Long gmid,
			@Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ " and e.designation.dsgid =:designId and e.gmid=:gmid and e.isDeleted='0'")
	List<Employee> findEmployeeDatabyDesignAndGmId(@Param("deptId") Long deptId, @Param("designId") Long designId,
			@Param("orgid") Long orgid, @Param("gmid") Long gmid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbDepartment.dpDeptid=:deptId "
			+ "  and e.tbLocationMas.locId =:locId and e.gmid=:gmid and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByLocIDAndGmId(@Param("deptId") Long deptId, @Param("locId") Long locId,
			@Param("orgid") Long orgid, @Param("gmid") Long gmid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.tbLocationMas.locId =:locId and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByLocId(@Param("locId") Long locId, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.designation.dsgid =:designId and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByDesgId(@Param("designId") Long designId, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.gmid=:gmid and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByGmId(@Param("gmid") Long gmid, @Param("orgid") Long orgid);

	@Query("select e from Employee e  where e.organisation.orgid=:orgid and e.designation.dsgid =:designId and e.tbLocationMas.locId =:locId  and e.isDeleted='0' ")
	List<Employee> findEmployeeDataByLocIdAndDesgId(@Param("designId") Long designId, @Param("locId") Long locId,
			@Param("orgid") Long orgid);

	@Query("SELECT m.empId FROM Employee m WHERE m.empisecuritykey =:sessionId ")
	Long getEmpIdByEMpSession(@Param("sessionId") String sessionId);

	@Query("select e.empname from Employee e  where e.empId=:empId and e.isDeleted='0' ")
	String getEmpNameByEmpId(@Param("empId") Long empId);

	@Query("select count(e)  from Employee e  where e.empId=:empId and e.updatedDate>:date and e.organisation.orgid=:orgid")
	int isEmployeeDataUpdated(@Param("empId") Long empId, @Param("orgid") Long orgid,@Param("date") Date date);
	
	@Query("select bd from Employee bd where bd.organisation.orgid=:orgId and bd.isDeleted='0'") // and bd.isDeleted ='0'
	List<Employee> getAllActiveEmployee(@Param("orgId") Long orgId);

	@Query("select e from Employee e where e.designation.dsgid=:desgId and e.tbDepartment.dpDeptid=:deptId and "
			+ "e.organisation.orgid=:orgId")
	List<Employee> getAllEmpByDesignationAndDeptId(@Param("desgId") Long desgId, @Param("deptId") Long deptId, @Param("orgId") Long orgId);

	@Query(value = "SELECT  EMP.EMPLOYEE_NAME FROM  \n" + 
			"tb_receipt_mas RM INNER JOIN\n" + 
			"(SELECT EMPH.EMPID, CONCAT(IFNULL(EMPH.PREV_EMPNAME,EMPH.EMPNAME),' ',IFNULL(EMPH.PREV_EMPMNAME,EMPH.EMPMNAME),' ',IFNULL(EMPH.PREV_EMPLNAME,EMPH.EMPLNAME),'') EMPLOYEE_NAME, IFNULL(LAG(EMPH.CREATED_DATE) OVER (PARTITION BY EMPID ORDER BY EMPID_H),'2022-04-01 00:00:00') PREVIOUS_DATE , EMPH.CREATED_DATE FROM  EMPLOYEE_HIST EMPH WHERE EMPH.EMPID IN(SELECT DISTINCT CREATED_BY FROM tb_receipt_mas WHERE RM_RCPTID=:recieptId)\n" + 
			"UNION ALL\n" + 
			"SELECT EMP.EMPID, CONCAT(IFNULL(EMP.EMPNAME,''),' ',IFNULL(EMP.EMPMNAME,''),' ',IFNULL(EMP.EMPLNAME,''),'') EMPLOYEE_NAME, IFNULL((SELECT MAX(CREATED_DATE) FROM EMPLOYEE_HIST EMPH WHERE EMPH.EMPID=EMP.EMPID),'2022-04-01 00:00:00') PREVIOUS_DATE, NOW() FROM EMPLOYEE EMP WHERE EMP.EMPID IN(SELECT DISTINCT CREATED_BY FROM tb_receipt_mas WHERE RM_RCPTID=:recieptId)) as EMP ON RM.CREATED_BY=EMP.EMPID and RM.CREATED_DATE BETWEEN EMP.PREVIOUS_DATE AND EMP.CREATED_DATE and RM.RM_RCPTID=:recieptId", nativeQuery = true)
	String getAllEmpLoyeeNamebyRecipetId(@Param("recieptId") Long recieptId);
	
	@Query("SELECT employee FROM Employee employee WHERE employee.emploginname=:emploginname")
	Employee findEmployeeByLoginName(@Param("emploginname") String emploginname);
	
	@Query("SELECT employee FROM Employee employee WHERE employee.emploginname=:emploginname and employee.emppassword=:emppassword and  employee.isDeleted = '0'")
	Employee authenticateEmployee(@Param("emploginname") String emploginname, @Param("emppassword") String emppassword);
	
	@Query("SELECT e.empname,e.empmname,e.emplname,e.empId,e.designation.dsgname FROM Employee e  WHERE  "
			+ "e.tbLocationMas.locId =:locId and e.isDeleted = '0'")
	List<Object[]> findAllActiveEmployeeByLocation(@Param("locId") Long locId);
	

	@Query("SELECT e.empname,e.empmname,e.emplname,e.empId"
			+ " FROM Employee e  WHERE e.organisation.orgid=?1 and e.isDeleted = '0' order by e.empname asc")
	List<Object[]> findAllEmpIntialInfoByOrg(Long orgId);
	
	@Query(value = "SELECT e1.DP_DEPTID, dept.DP_DEPTDESC, e1.dsgid, des.dsgname, e1.REPORTING_EMPID, e2.empname, e2.emplname FROM "
			+ "Employee e1 LEFT JOIN Employee e2 ON e1.REPORTING_EMPID = e2.empId  JOIN TB_DEPARTMENT dept ON e1.DP_DEPTID = dept.DP_DEPTID " + 
			"JOIN DESIGNATION des ON e1.DSGID = des.DSGID WHERE e1.empId=:empId AND e1.orgid=:orgid", nativeQuery = true)
	Object[] getEmployeDetailsById(@Param("empId") Long empId, @Param("orgid") Long orgid);
	
	@Query(value = "SELECT e1.DP_DEPTID, dept.DP_DEPTDESC, e1.dsgid, des.dsgname, e1.REPORTING_EMPID, " +
	        "e2.empname AS reportingEmpName, e2.emplname AS reportingEmpLastName, " +
	        "e1.empmobno, e1.empId, e1.empname AS empName, e1.emplname AS empLastName " +
	        "FROM Employee e1 " +
	        "LEFT JOIN Employee e2 ON e1.REPORTING_EMPID = e2.empId " +
	        "JOIN TB_DEPARTMENT dept ON e1.DP_DEPTID = dept.DP_DEPTID " +
	        "JOIN DESIGNATION des ON e1.DSGID = des.DSGID " +
	        "WHERE e1.empId = :empId AND e1.orgid = :orgid",
	        nativeQuery = true)
	List<Object[]> getEmployeDetailsWithMobNoById(@Param("empId") Long empId, @Param("orgid") Long orgid);


	@Query("select e.empname, e.emplname from Employee e  where e.empId=:empId and e.isDeleted='0' ")
	Object[] getEmpFullNameByEmpId(@Param("empId") Long empId);
	
	
	@Query(value = "select distinct(empid) from employee where  "
			+ "find_in_SET(empid, (select group_concat(DISTINCT WFD_EMPROLE) EMP_ROLE from tb_workflow_det "
			+ "where wf_id in (select WF_ID from tb_workflow_mas where COMP_ID is not null and wf_status='Y') and wfd_status='Y'))", nativeQuery = true)
	List<Object> findAllEmployeeFromCareWorkflow();
	
	@Query("SELECT m FROM Employee m WHERE m.empId =:empId and m.organisation.orgid=:orgId")
	Employee getEmployeeByEmpId(@Param("empId") Long empId, @Param("orgId") Long orgId);
	
	@Query(value = "SELECT  LM.LOC_NAME_ENG FROM Employee e1  JOIN  tb_location_mas LM ON LM.LOC_ID=E1.LOCID WHERE e1.empId=:empId AND e1.orgid=:orgid", nativeQuery = true)
	public String getLocationNameByEmpIdOrgId(@Param("empId") Long empId, @Param("orgid") Long orgid);
	
	@Query(value = "SELECT EMPLOYEE_NAME FROM (\n" + 
			"SELECT EMPH.EMPID, CONCAT(IFNULL(EMPH.PREV_EMPNAME,EMPH.EMPNAME),' ',IFNULL(EMPH.PREV_EMPMNAME,EMPH.EMPMNAME),' ',IFNULL(EMPH.PREV_EMPLNAME,EMPH.EMPLNAME),'') EMPLOYEE_NAME, IFNULL(LAG(EMPH.CREATED_DATE) OVER (PARTITION BY EMPID ORDER BY EMPID_H),'2022-04-01 00:00:00') PREVIOUS_DATE , EMPH.CREATED_DATE FROM EMPLOYEE_HIST EMPH WHERE EMPH.EMPID=:empId\n" + 
			"UNION ALL\n" + 
			"SELECT EMP.EMPID, CONCAT(IFNULL(EMP.EMPNAME,''),' ',IFNULL(EMP.EMPMNAME,''),' ',IFNULL(EMP.EMPLNAME,''),'') EMPLOYEE_NAME, IFNULL((SELECT MAX(CREATED_DATE) FROM EMPLOYEE_HIST EMPH WHERE EMPH.EMPID=EMP.EMPID),'2022-04-01 00:00:00') PREVIOUS_DATE, NOW() FROM EMPLOYEE EMP WHERE EMP.EMPID=:empId ) AS EMP WHERE EMPID=:empId AND :appDate  BETWEEN PREVIOUS_DATE AND CREATED_DATE", nativeQuery = true)
	String getAllEmpLoyeeNamebyempIdAndDate(@Param("empId") Long empId,@Param("appDate")Date appDate);
	
	@Query("select e.empmobno from Employee e  where e.empId=:empId and e.isDeleted='0' ")
	String getEmpMobNo(@Param("empId") Long empId);
	@Query(value = "SELECT * FROM (\n" + 
			"SELECT EMPH.EMPID, CONCAT(IFNULL(EMPH.PREV_EMPNAME,EMPH.EMPNAME),' ',IFNULL(EMPH.PREV_EMPMNAME,EMPH.EMPMNAME),' ',IFNULL(EMPH.PREV_EMPLNAME,EMPH.EMPLNAME),'') EMPLOYEE_NAME, EMPH.EMPEMAIL, EMPH.EMPMOBNO, (SELECT DSGNAME FROM designation DSG WHERE DSG.DSGID=EMPH.DSGID) DESIGNATION, IFNULL(LAG(EMPH.CREATED_DATE) OVER (PARTITION BY EMPID ORDER BY EMPID_H),'2022-04-01 00:00:00') PREVIOUS_DATE , EMPH.CREATED_DATE FROM EMPLOYEE_HIST EMPH WHERE EMPH.EMPID=:empId\n" + 
			"UNION ALL\n" + 
			"SELECT EMP.EMPID, CONCAT(IFNULL(EMP.EMPNAME,''),' ',IFNULL(EMP.EMPMNAME,''),' ',IFNULL(EMP.EMPLNAME,''),'') EMPLOYEE_NAME, EMP.EMPEMAIL, EMP.EMPMOBNO, (SELECT DSGNAME FROM designation DSG WHERE DSG.DSGID=EMP.DSGID) DESIGNATION, IFNULL((SELECT MAX(CREATED_DATE) FROM EMPLOYEE_HIST EMPH WHERE EMPH.EMPID=EMP.EMPID),'2022-04-01 00:00:00') PREVIOUS_DATE, NOW() FROM EMPLOYEE EMP WHERE EMP.EMPID=:empId) AS EMP WHERE EMPID=:empId AND :appDate  BETWEEN PREVIOUS_DATE AND CREATED_DATE", nativeQuery = true)
	Object[] findCompletedEmployeeByIdDate(@Param("empId") Long empId,@Param("appDate")Date appDate);
	
}
