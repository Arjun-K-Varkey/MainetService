package com.abm.mainet.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.abm.mainet.bill.service.BillMasterCommonServiceImpl;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.CitizenDashboardView;
import com.abm.mainet.common.domain.CitizenDashboardViewEntity;
import com.abm.mainet.common.domain.Department;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.dto.CitizenDashBoardReqDTO;
import com.abm.mainet.common.integration.payment.entity.PaymentTransactionMas;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;

@Repository
public class CitizenDashBoardDAO extends AbstractDAO<Department> implements ICitizenDashBoardDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CitizenDashBoardDAO.class);

	@Autowired
	private IOrganisationDAO organisationDAO;

	@Override
	public List<CitizenDashboardView> getAllApplicationsOfCitizen(CitizenDashBoardReqDTO request) {

		Organisation org = organisationDAO.getOrganisationById(request.getOrgId(), MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		final StringBuilder queryString = new StringBuilder();
		queryString.append(
				"select c " + "from CitizenDashboardView c where " + " ((c.ServiceType ='NW' and c.empId =:empId) "
						+ " or ((c.ServiceType ='W' or c.ServiceType ='D') " + " and (c.apmMobileNo = :mobile)))  ");

		LookUp multiOrgDataList = null;
		try {
			LOGGER.error("Fetching MOAL prefix");
			multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL, MainetConstants.ENV,
					org);
			LOGGER.error("MOAL prefix is available");
		} catch (Exception e) {
			LOGGER.error("No Prefix found for ENV(MOAL)");
		}

		if (!(multiOrgDataList != null && StringUtils.isNotBlank(multiOrgDataList.getOtherField())
				&& StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY))) {
			queryString.append("and c.orgId=:orgId ");
			moalFlag = true;
			LOGGER.error("Appending org id in query: " + request.getOrgId());
		}

		queryString.append("order by c.apmApplicationDate DESC");
		LOGGER.error("Final Query: " + queryString.toString());
		final Query query = createQuery(queryString.toString());
		query.setParameter("empId", request.getEmpId());
		if (moalFlag == true) {
			LOGGER.error("Setting orgId parameter in  Query setParameter field: " + queryString.toString());
			query.setParameter("orgId", request.getOrgId());
		}
		query.setParameter("mobile", request.getMobileNo());
		@SuppressWarnings("unchecked")
		final List<CitizenDashboardView> allApplications = query.getResultList();
		LOGGER.error("Result Size: " + allApplications.size());
		return allApplications;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllFaliuredOrCancelledOnlineList(String mobNo, Long orgId) {
		List<Object[]> entityList = new ArrayList<>();

		Organisation org = organisationDAO.getOrganisationById(orgId, MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		
		LookUp multiOrgDataList = null;
		try {
			LOGGER.error("Fetching MOAL prefix");
			multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL,
					MainetConstants.ENV, org);
			LOGGER.error("MOAL prefix is available");
		} catch (Exception e) {
			LOGGER.error("No Prefix found for ENV(MOAL)");
		}

		if (!(multiOrgDataList != null && StringUtils.isNotBlank(multiOrgDataList.getOtherField())
				&& StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY))) {
			moalFlag = true;
			LOGGER.error("Appending org id in query: " + orgId);
		}
		try {
			final StringBuilder queryString = new StringBuilder();
			queryString.append("SELECT \r\n" + 
					"    s.SM_SERVICE_NAME AS col_0_0_,\r\n" + 
					"    s.SM_SERVICE_NAME_MAR AS col_1_0_,\r\n" + 
					"    s.SM_SHORTDESC AS col_2_0_,\r\n" + 
					"    d.DP_DEPTDESC AS col_3_0_,\r\n" + 
					"    d.DP_NAME_MAR AS col_4_0_,\r\n" + 
					"    t.TRAN_CM_ID AS col_5_0_,\r\n" + 
					"    t.REFERENCE_ID AS col_6_0_,\r\n" + 
					"    t.REFERENCE_DATE AS col_7_0_,\r\n" + 
					"    t.RECV_STATUS AS col_8_0_,\r\n" + 
					"    t.ORGID AS col_9_0_\r\n" + 
					"FROM (\r\n" + 
					"    SELECT \r\n" + 
					"        t.*,\r\n" + 
					"        ROW_NUMBER() OVER (\r\n" + 
					"            PARTITION BY t.REFERENCE_ID, IFNULL(t.PROP_FLAT_NO,'') \r\n" + 
					"            ORDER BY t.REFERENCE_DATE, IFNULL(t.PROP_FLAT_NO,'')  DESC\r\n" + 
					"        ) AS rn\r\n" + 
					"    FROM  TB_ONL_TRAN_MAS_SERVICE t\r\n" + 
					"    WHERE \r\n" + 
					"       \r\n" + 
					"         t.RECV_STATUS <> 'success'\r\n");
			
			
			
			queryString.append(" AND t.SEND_PHONE = '" + mobNo + "'");
			if (moalFlag == true) {
				queryString.append("AND t.ORGID = '" + orgId + "'");
			}
			
			
			queryString.append(") t\r\n" + 
					"JOIN TB_SERVICES_MST s \r\n" + 
					"    ON t.SM_SERVICE_ID = s.SM_SERVICE_ID\r\n" + 
					"JOIN TB_DEPARTMENT d \r\n" + 
					"    ON s.CDM_DEPT_ID = d.DP_DEPTID\r\n" + 
					"WHERE t.rn = 1\r\n" + 
					"ORDER BY t.REFERENCE_DATE DESC");
			
			
			
			
			LOGGER.error("Final Query: " + queryString.toString());
			
			final Query query = createNativeQuery(queryString.toString());
			
			entityList = (List<Object[]>) query.getResultList();

		} catch (final Exception exception) {
			LOGGER.error("Exception occur in  getAllFaliuredOrCancelledOnlineList() ", exception);

		}
		return entityList;

	}

	@Override
	public List<PaymentTransactionMas> getCitizenPayPendingDataByDateAndStatus(Date validDate,Date toDate ,Long serviceId,
			String flatNo, Long orgId,String refNo,Long deptId) {
		List<PaymentTransactionMas> entityList = new ArrayList<>();
		try {
			final StringBuilder queryString = new StringBuilder();
			queryString.append(
					"select b from PaymentTransactionMas b where ( (( b.pgSource = 'IDBI(ISGPay)' and b.recvStatus not in ( 'REFUND'))\r\n" + 
					"        or ((b.pgSource != 'IDBI(ISGPay)' and b.recvStatus not in ('REFUND')))   or b.recvStatus is null )) "
							+ " and b.pgSource is not null and b.tranCmId not in (select distinct r.pgRefId     from TbServiceReceiptMasEntity r     where r.receiptDelFlag is null  "
							+ "   and r.pgRefId = b.tranCmId ) and date(b.referenceDate) between :fromDate and :toDate  and b.orgId=:orgId ");

			if (serviceId != null && serviceId>1) {
				queryString.append(" and b.smServiceId=:smServiceId");
			}else {
				queryString.append(" and b.smServiceId in (select s.smServiceId from ServiceMaster s where s.tbDepartment.dpDeptid=:deptId)");
			}
            
			if (StringUtils.isNotBlank(flatNo)) {
				queryString.append(" and b.flatNo=:flatNo");
			}
			if (StringUtils.isNotBlank(refNo)) {
				queryString.append(" and b.referenceId=:referenceId");
			}

			// Defect #125484

			queryString.append(" order by  b.referenceDate DESC");
			LOGGER.error("Final Query: " + queryString.toString());
			final Query query = createQuery(queryString.toString());

			query.setParameter("fromDate", validDate);
			query.setParameter("toDate", toDate);

			if (serviceId != null&&serviceId>1) {
				query.setParameter("smServiceId", serviceId);
			}
			if (deptId != null && (serviceId != null && serviceId<1)) {
				query.setParameter("deptId", deptId);
			}
			if (StringUtils.isNotBlank(flatNo)) {
				query.setParameter("flatNo", flatNo);

			}
			if (StringUtils.isNotBlank(refNo)) {
				query.setParameter("referenceId", refNo);

			}
		
			query.setParameter("orgId", orgId);
			entityList = (List<PaymentTransactionMas>) query.getResultList();

		} catch (final Exception exception) {
			LOGGER.error("Exception occur in  getAllFaliuredOrCancelledOnlineList() ", exception);

		}
		return entityList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CitizenDashboardViewEntity> getAllApplicationsOfCitizenByNative(CitizenDashBoardReqDTO request) {
		
		String mobileNo="";
		Long orgId = null;
		if(null!=request.getMobileNo() && null!=request.getMobileNo()){
			mobileNo= request.getMobileNo();
			orgId= request.getOrgId();
		}
		Organisation org = organisationDAO.getOrganisationById(request.getOrgId(), MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		final StringBuilder queryString = new StringBuilder();
		/*queryString.append("Select * from ( select `c`.`APM_APPLICATION_ID` AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,`w`.`STATUS` AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,`w`.`LAST_DECISION` AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`,"
						+ "`dp`.`DP_NAME_MAR` AS `dp_name_mar`,`c`.`ORGID` AS `orgid`,`w`.`EMPID` AS `empid`,`t`.`TASK_ID` AS `TASKID`,`t`.`TASK_SLA_DURATION` AS `TASK_SLA_DURATION`,`t`.`TASK_STATUS` AS `TASK_STATUS`,`t`.`WFTASK_ACTORID` AS `WFTASK_ACTORID`,`t`.`EVENT_ID` AS `EVENT_ID`,`sy`.`SMFNAME` AS `SMFNAME`,`sy`.`SMFNAME_MAR` AS `SMFNAME_MAR`,`t`.`SMFACTION` AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'W' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`,"
						+ "`s`.`SM_SERDUR` AS `SM_SERDUR` from ((((((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_workflow_request` `w`) join `tb_workflow_task` `t`) join `tb_sysmodfunction` `sy`) join `tb_cfc_application_address` `d`) where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`c`.`APM_APPLICATION_ID` = `w`.`APM_APPLICATION_ID`) "
						+ "and (`w`.`WORKFLOW_REQ_ID` = `t`.`WORKFLOW_REQ_ID`) and (`sy`.`SMFID` = `t`.`EVENT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and `t`.`WFTASK_ID` in (select max(`tb_workflow_task`.`WFTASK_ID`) from `tb_workflow_task` where (`tb_workflow_task`.`EVENT_ID` <> -(1)) group by `tb_workflow_task`.`APM_APPLICATION_ID`) and (`d`.`APA_MOBILNO`="+mobileNo+")) union select `c`.`APM_APPLICATION_ID` "
						+ "AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,(case when (`c`.`APM_MODE` = 'D') then 'DRAFT' else 'PENDING' end) AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,NULL AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`,`dp`.`DP_NAME_MAR` AS `dp_name_mar`,"
						+ "`c`.`ORGID` AS `orgid`,`c`.`USER_ID` AS `user_id`,NULL AS `TASKID`,NULL AS `TASK_SLA_DURATION`,NULL AS `TASK_STATUS`,NULL AS `WFTASK_ACTORID`,NULL AS `EVENT_ID`,NULL AS `SMFNAME`,NULL AS `SMFNAME_MAR`,NULL AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'D' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`,`s`.`SM_SERDUR` AS `SM_SERDUR` from (((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_cfc_application_address` `d`) "
						+ "where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and (`c`.`APM_MODE` = 'D') "
						+ "and (`d`.`APA_MOBILNO`="+mobileNo+")) ) as a where ServiceType IN('W','D')");
		*/
		
		/*queryString.append(
				"Select * from ( select `c`.`APM_APPLICATION_ID` AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,`w`.`STATUS` AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,`w`.`LAST_DECISION` AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`, `dp`.`DP_NAME_MAR` AS `dp_name_mar`,`c`.`ORGID` AS `orgid`,`w`.`EMPID` AS `empid`,`t`.`TASK_ID` AS `TASKID`,`t`.`TASK_SLA_DURATION` AS `TASK_SLA_DURATION`,`t`.`TASK_STATUS` AS `TASK_STATUS`,`t`.`WFTASK_ACTORID` AS `WFTASK_ACTORID`,`t`.`EVENT_ID` AS `EVENT_ID`,`sy`.`SMFNAME` AS `SMFNAME`,`sy`.`SMFNAME_MAR` AS `SMFNAME_MAR`,`t`.`SMFACTION` AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'W' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`, `s`.`SM_SERDUR` AS `SM_SERDUR` from ((((((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_workflow_request` `w`) join `tb_workflow_task` `t`) join `tb_sysmodfunction` `sy`) join `tb_cfc_application_address` `d` JOIN (select max(`A`.`WFTASK_ID`) `WFTASK_ID` from `tb_workflow_task` `A` JOIN `tb_cfc_application_address` `CFCA` where (`A`.`EVENT_ID` <> -(1)) AND `CFCA`.`APM_APPLICATION_ID`=`A`.`APM_APPLICATION_ID` AND `APA_MOBILNO`="+mobileNo+" group by `A`.`APM_APPLICATION_ID` ) `MAX_WFTASK_ID`) where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`c`.`APM_APPLICATION_ID` = `w`.`APM_APPLICATION_ID`) and (`w`.`WORKFLOW_REQ_ID` = `t`.`WORKFLOW_REQ_ID`) and (`sy`.`SMFID` = `t`.`EVENT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and (`MAX_WFTASK_ID`.`WFTASK_ID`=`t`.`WFTASK_ID`) and (`d`.`APA_MOBILNO`="+mobileNo+")) union select `c`.`APM_APPLICATION_ID` AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,(case when (`c`.`APM_MODE` = 'D') then 'DRAFT' else 'PENDING' end) AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,NULL AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`,`dp`.`DP_NAME_MAR` AS `dp_name_mar`, `c`.`ORGID` AS `orgid`,`c`.`USER_ID` AS `user_id`,NULL AS `TASKID`,NULL AS `TASK_SLA_DURATION`,NULL AS `TASK_STATUS`,NULL AS `WFTASK_ACTORID`,NULL AS `EVENT_ID`,NULL AS `SMFNAME`,NULL AS `SMFNAME_MAR`,NULL AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'D' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`,`s`.`SM_SERDUR` AS `SM_SERDUR` from (((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_cfc_application_address` `d`) where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and (`c`.`APM_MODE` = 'D') and (`d`.`APA_MOBILNO`="+"'"+mobileNo+"'"+")) ) as a where ServiceType IN('W','D')");
*/
		
		
		//Updated query given by yatin wagh
		
		queryString.append(
			    "SELECT * FROM (select `c`.`APM_APPLICATION_ID` AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,`w`.`STATUS` AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,`w`.`LAST_DECISION` AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`,`dp`.`DP_NAME_MAR` AS `dp_name_mar`,`c`.`ORGID` AS `orgid`,`w`.`EMPID` AS `empid`,`t`.`TASK_ID` AS `TASKID`,`t`.`TASK_SLA_DURATION` AS `TASK_SLA_DURATION`,`t`.`TASK_STATUS` AS `TASK_STATUS`,`t`.`WFTASK_ACTORID` AS `WFTASK_ACTORID`,`t`.`EVENT_ID` AS `EVENT_ID`,`sy`.`SMFNAME` AS `SMFNAME`,`sy`.`SMFNAME_MAR` AS `SMFNAME_MAR`,`t`.`SMFACTION` AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'W' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`,`s`.`SM_SERDUR` AS `SM_SERDUR` from ((((((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_workflow_request` `w`) join `tb_workflow_task` `t`) join `tb_sysmodfunction` `sy`) join `tb_cfc_application_address` `d` JOIN (select max(`A`.`WFTASK_ID`) `WFTASK_ID` from `tb_workflow_task` `A` JOIN `tb_cfc_application_address` `CFCA` where (`A`.`EVENT_ID` <> -(1)) AND `CFCA`.`APM_APPLICATION_ID`=`A`.`APM_APPLICATION_ID` AND `APA_MOBILNO`="+mobileNo+" group by `A`.`APM_APPLICATION_ID`) `MAX_WFTASK_ID`) where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`c`.`APM_APPLICATION_ID` = `w`.`APM_APPLICATION_ID`) and (`w`.`WORKFLOW_REQ_ID` = `t`.`WORKFLOW_REQ_ID`) and (`sy`.`SMFID` = `t`.`EVENT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and (`MAX_WFTASK_ID`.`WFTASK_ID`=`t`.`WFTASK_ID`) and (`d`.`APA_MOBILNO`="+mobileNo+")) union select `c`.`APM_APPLICATION_ID` AS `apm_Application_Id`,`c`.`REF_NO` AS `Reference_Id`,`s`.`SM_SERVICE_NAME` AS `sm_Service_Name`,`s`.`SM_SERVICE_NAME_MAR` AS `sm_Service_Name_Mar`,(case when (`c`.`APM_MODE` = 'D') then 'DRAFT' when (select 1 from tb_receipt_mas RM where RM.APM_APPLICATION_ID=C.APM_APPLICATION_ID AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.RECEIPT_TYPE_FLAG='M') IS NOT NULL THEN 'CLOSED' else 'PENDING' end) AS `status`,`s`.`SM_SHORTDESC` AS `sm_Shortdesc`,NULL AS `last_Decision`,`c`.`APM_APPLICATION_DATE` AS `apm_application_date`,`dp`.`DP_DEPTDESC` AS `dp_deptdesc`,`dp`.`DP_NAME_MAR` AS `dp_name_mar`,`c`.`ORGID` AS `orgid`,`c`.`USER_ID` AS `user_id`,NULL AS `TASKID`,NULL AS `TASK_SLA_DURATION`,NULL AS `TASK_STATUS`,NULL AS `WFTASK_ACTORID`,NULL AS `EVENT_ID`,NULL AS `SMFNAME`,NULL AS `SMFNAME_MAR`,NULL AS `SMFACTION`,`d`.`APA_MOBILNO` AS `APA_MOBILNO`,'D' AS `ServiceType`,`dp`.`DP_DEPTCODE` AS `DP_DEPTCODE`,`s`.`SM_SERDUR` AS `SM_SERDUR` from (((`tb_services_mst` `s` join `tb_department` `dp`) join `tb_cfc_application_mst` `c`) join `tb_cfc_application_address` `d`) where ((`s`.`SM_SERVICE_ID` = `c`.`SM_SERVICE_ID`) and (`dp`.`DP_DEPTID` = `s`.`CDM_DEPT_ID`) and (`d`.`APM_APPLICATION_ID` = `c`.`APM_APPLICATION_ID`) and (`d`.`APA_MOBILNO`="+"'"+mobileNo+"'"+") AND NOT EXISTS(SELECT 1 FROM tb_workflow_request WR WHERE WR.APM_APPLICATION_ID=C.APM_APPLICATION_ID) )) as a where ServiceType IN('W','D')");
		
		
		

		
		
		LookUp multiOrgDataList = null;
		try {
			LOGGER.error("Fetching MOAL prefix");
			multiOrgDataList = CommonMasterUtility.getValueFromPrefixLookUp(MainetConstants.MOAL, MainetConstants.ENV,
					org);
			LOGGER.error("MOAL prefix is available");
		} catch (Exception e) {
			LOGGER.error("No Prefix found for ENV(MOAL)");
		}

		if (!(multiOrgDataList != null && StringUtils.isNotBlank(multiOrgDataList.getOtherField())
				&& StringUtils.equals(multiOrgDataList.getOtherField(), MainetConstants.FlagY))) {
			if(null!=orgId){
				queryString.append(" and orgId="+orgId);
				moalFlag = true;
				LOGGER.error("Appending org id in query: " + request.getOrgId());
			}
		}

		queryString.append(" order by apm_application_date DESC");
		LOGGER.error("Final Query: " + queryString.toString());
		if (moalFlag == true) {
			LOGGER.error("Setting orgId parameter in  Query setParameter field: " + queryString.toString());
		}
		
		List<CitizenDashboardViewEntity> allApplications = null;
		
		allApplications = (List<CitizenDashboardViewEntity>) entityManager.createNativeQuery(queryString.toString(), CitizenDashboardViewEntity.class).getResultList();
		
		LOGGER.error("Result Size: " + allApplications.size());
		
		return allApplications;
		
		
	}

}
