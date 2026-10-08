package com.abm.mainet.common.workflow.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.dao.IOrganisationDAO;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.workflow.domain.AapleSarkarSummaryEntity;
import com.abm.mainet.common.workflow.domain.WorkflowMas;
import com.abm.mainet.common.workflow.dto.TaskSearchRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WorkflowTypeDAOImpl extends AbstractDAO<WorkflowMas> implements IWorkflowTypeDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowTypeDAOImpl.class);
    
    @Autowired
	 private IOrganisationDAO organisationDAO;
    @Override
    public List<WorkflowMas> getDefinedActiveWorkFlows(Long orgId, Long deptId, Long serviceId, Long compId,Long schId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select wt from WorkflowMas wt where wt.status = 'Y' and wt.organisation.orgid =" + orgId
                + " and wt.department.dpDeptid = " + deptId
                + " and wt.service.smServiceId = " + serviceId);
        if (compId != 0 && compId != null) {
            sb.append(" and wt.complaint.compId = " + compId);
        }
		//D89638
        if(schId!=null && schId!=0) {
        	sb.append(" and wt.wmSchCodeId2 = " + schId);
        }
        sb.append(" order by wt.fromAmount asc ");
        
        Query query = this.createQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        return list;
    }

    @Override
    public List<WorkflowMas> getAllWorkFlows(Long orgId, Long deptId, Long serviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select wt from WorkflowMas wt where wt.organisation.orgid = " + orgId);

        if (deptId != 0 && deptId != null) {
            sb.append(" and wt.department.dpDeptid = " + deptId);
        }
        if (serviceId != 0 && serviceId != null) {
            sb.append(" and wt.service.smServiceId = " + serviceId);
        }
        sb.append(" order by wt.wfId desc ");
        Query query = this.createQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        return list;
    }

    @Override
    public WorkflowMas getWorkFlowTypeByOrgDepartmentAndComplaintTypeForWardZone(Long orgId, Long compId,
            Long codIdOperLevel1, Long codIdOperLevel2, Long codIdOperLevel3, Long codIdOperLevel4,
            Long codIdOperLevel5) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT wt FROM WorkflowMas wt"
                + " where wt.organisation.orgid = " + orgId
                + " and wt.complaint != NULL"
                + " and wt.complaint.compId = " + compId
                + " and wt.type = 'N'"
                + " and wt.status = 'Y' ");
        if (codIdOperLevel1 != null)
            builder.append(" and wt.codIdOperLevel1 = " + codIdOperLevel1);
        if (codIdOperLevel2 != null)
            builder.append(" and wt.codIdOperLevel2 = " + codIdOperLevel2);
        if (codIdOperLevel3 != null)
            builder.append(" and wt.codIdOperLevel3 = " + codIdOperLevel3);
        if (codIdOperLevel4 != null)
            builder.append(" and wt.codIdOperLevel4 = " + codIdOperLevel4);
        if (codIdOperLevel5 != null)
            builder.append(" and wt.codIdOperLevel5 = " + codIdOperLevel5);

        Query query = this.createQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        if(list.isEmpty()) {
     	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
        }
        return list.isEmpty() == true ? null : list.get(0);
    }

    
	@Override
    public WorkflowMas getServiceWorkFlowForWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long codIdOperLevel1, Long codIdOperLevel2, Long codIdOperLevel3, Long codIdOperLevel4,
            Long codIdOperLevel5) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT wt FROM WorkflowMas wt"
                + " where wt.organisation.orgid = " + orgId
                + " and wt.department.dpDeptid = " + deptId
                + " and wt.complaint is NULL"
                + " and wt.service.smServiceId = " + serviceId
                + " and wt.type = 'N'"
                + " and wt.status = 'Y' ");
        if (amount != null)
            builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);
        if (codIdOperLevel1 != null && codIdOperLevel1 > 0d)
            builder.append(" and wt.codIdOperLevel1 = " + codIdOperLevel1);
        if (codIdOperLevel2 != null && codIdOperLevel2 > 0d)
            builder.append(" and wt.codIdOperLevel2 = " + codIdOperLevel2);
        if (codIdOperLevel3 != null && codIdOperLevel3 > 0d)
            builder.append(" and wt.codIdOperLevel3 = " + codIdOperLevel3);
        if (codIdOperLevel4 != null && codIdOperLevel4 > 0d)
            builder.append(" and wt.codIdOperLevel4 = " + codIdOperLevel4);
        if (codIdOperLevel5 != null && codIdOperLevel5 > 0d)
            builder.append(" and wt.codIdOperLevel5 = " + codIdOperLevel5);

        Query query = this.createQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
       if(list.isEmpty()) {
    	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
       }	     
        return list.isEmpty() == true ? null : list.get(0);
    }

    @Override
    public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long sourceOfFund) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT wt FROM WorkflowMas wt"
                + " where wt.organisation.orgid = " + orgId
                + " and wt.department.dpDeptid = " + deptId
                + " and wt.service.smServiceId = " + serviceId
                + " and wt.complaint is NULL"
                + " and wt.type = 'A'"
                + " and wt.status = 'Y'");

        if (sourceOfFund != null && sourceOfFund != 0)
            builder.append(" and wt.wmSchCodeId1=" + sourceOfFund);

        
        
        if (amount != null)
            builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);

        Query query = this.createQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        if(list.isEmpty()) {
     	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
        }
        return list.isEmpty() == true ? null : list.get(0);
    }

    
    
    // NEW METHOD ADDEDD FOR getwmschcodeid2
    @Override
    public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long sourceOfFund, Long getwmschcodeid2) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT wt FROM WorkflowMas wt"
                + " where wt.organisation.orgid = " + orgId
                + " and wt.department.dpDeptid = " + deptId
                + " and wt.service.smServiceId = " + serviceId
                + " and wt.wmSchCodeId2= " + getwmschcodeid2
                + " and wt.complaint is NULL"
                + " and wt.type = 'A'"
                + " and wt.status = 'Y'");

        if (sourceOfFund != null && sourceOfFund != 0)
            builder.append(" and wt.wmSchCodeId1=" + sourceOfFund);

        if (getwmschcodeid2 != null && getwmschcodeid2 != 0)
            builder.append(" and wt.wmSchCodeId2=" + getwmschcodeid2);

        
        if (amount != null)
            builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);

        Query query = this.createQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        if(list.isEmpty()) {
     	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
        }
        return list.isEmpty() == true ? null : list.get(0);
    }
    
	public Long getTaskIdByAppIdAndOrgId(Long appId, Long orgId) {
		BigInteger taskId = null;
		Organisation org = organisationDAO.getOrganisationById(orgId, MainetConstants.STATUS.ACTIVE);
		boolean moalFlag = false;
		final StringBuilder queryString = new StringBuilder();
		queryString.append(
				"SELECT wt.TASK_ID from tb_workflow_task wt where wt.SMFACTION='LoiPayment.html' AND wt.TASK_STATUS <> 'EXITED' AND wt.APM_APPLICATION_ID=:appId ");
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
			queryString.append("and wt.ORGID=:orgId ");
			moalFlag = true;
			LOGGER.error("Appending org id in query: " + orgId);
		}
		LOGGER.error("Final Query: " + queryString.toString());
		final Query query = createNativeQuery(queryString.toString());
		query.setParameter("appId", appId);
		if (moalFlag == true) {
			LOGGER.error("Setting orgId parameter in  Query setParameter field: " + queryString.toString());
			query.setParameter("orgId", orgId);
		}
		try {
			taskId = (BigInteger) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Loi payment task exited " + queryString.toString());
		}
		if (taskId != null) {
			return taskId.longValue();
		} else {
			return null;
		}

	}

    @Override
    public List<Object[]> fetchClosedTaskList(TaskSearchRequest taskSearchRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append(" Select A.* from (SELECT  distinct\r\n" + 
        		" cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,\r\n" + 
        		"  'Certificate' Type FROM tb_workflow_request e,\r\n" + 
        		"  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  tb_services_mst srv\r\n" + 
        		"  WHERE srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID\r\n" + 
        		"  AND srv.SM_PRINT_RESPONS = (SELECT CPD_ID FROM tb_comparam_det where CPM_ID = (SELECT CPM_ID FROM TB_COMPARAM_MAS WHERE CPM_PREFIX = 'PRN') AND CPD_VALUE = 'C')\r\n" + 
        		"  AND e.apm_application_id = cfc.APM_APPLICATION_ID\r\n" + 
        		"  AND cfc.ISSUED_BY IS NULL"+
        		"  AND e.Status= " +'"'+taskSearchRequest.getStatus()+'"' + 
        		"  AND cfc.orgId = "+taskSearchRequest.getOrgId()+ 
        		"  UNION\r\n" + 
        		"  SELECT  distinct\r\n" + 
        		"  cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,\r\n" + 
        		"  'LOI' Type  FROM \r\n" + 
        		"  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  TB_SERVICES_MST srv,\r\n" + 
        		"  TB_LOI_MAS lm\r\n" + 
        		"  WHERE  srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID \r\n" + 
        		"  AND lm.apm_application_id = cfc.APM_APPLICATION_ID\r\n" + 
        		"  AND lm.ISSUED_BY IS NULL  \r\n" + 
        		"  AND cfc.orgId = "+ taskSearchRequest.getOrgId() +
        		"  UNION\r\n" + 
        		"  SELECT  distinct\r\n" + 
        		"  cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,\r\n" + 
        		"  'WorkOrder' Type  FROM\r\n" + 
        		"  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  TB_SERVICES_MST srv,\r\n" + 
        		"  TB_WORK_ORDER wd\r\n" + 
        		"  WHERE  srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID \r\n" + 
        		"  AND wd.WO_APPLICATION_ID = cfc.APM_APPLICATION_ID\r\n" + 
        		"  AND wd.ISSUED_BY IS NULL  \r\n" + 
        		"  AND cfc.orgId = "+ taskSearchRequest.getOrgId() +")A order by APM_APPLICATION_DATE desc");

        Query query = this.createNativeQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> objList = query.getResultList();
        return objList;
    }
    
    
    @Override
    public List<Object[]> fetchIssuedTaskList(TaskSearchRequest taskSearchRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append(" Select A.* from (SELECT  distinct\r\n" + 
        		" cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,cfc.ISSUED_DATE,\r\n" + 
        		"  'Certificate' Type FROM \r\n" + 
        		"  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  tb_services_mst srv\r\n" + 
        		"  WHERE srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID\r\n" + 
        		"  AND srv.SM_PRINT_RESPONS = (SELECT CPD_ID FROM tb_comparam_det where CPM_ID = (SELECT CPM_ID FROM TB_COMPARAM_MAS WHERE CPM_PREFIX = 'PRN') AND CPD_VALUE = 'C')\r\n" + 
        		"  AND cfc.ISSUED_BY  IS NOT NULL   "
        		+ "AND cfc.orgId = "+ taskSearchRequest.getOrgId() +
        		"  UNION\r\n" + 
        		"  SELECT  distinct\r\n" + 
        		"  cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,lm.ISSUED_DATE,\r\n" + 
        		"  'LOI' Type FROM  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  TB_SERVICES_MST srv,\r\n" + 
        		"  TB_LOI_MAS lm\r\n" + 
        		"  WHERE  srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID \r\n" + 
        		"  AND lm.apm_application_id = cfc.APM_APPLICATION_ID\r\n" + 
        		"  AND lm.ISSUED_BY  IS NOT NULL  \r\n" + 
        		"  AND "+ taskSearchRequest.getOrgId() + 
        		"  UNION\r\n" + 
        		"  SELECT  distinct\r\n" + 
        		"  cfc.APM_APPLICATION_ID AS APM_APPLICATION_ID,  cfc.ORGID AS ORGID, srv.CDM_DEPT_ID AS DP_DEPTID,\r\n" + 
        		" (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_DEPTDESC,\r\n" + 
        		" (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = srv.CDM_DEPT_ID) AS DP_NAME_MAR,\r\n" + 
        		"  srv.SM_SERVICE_ID AS SM_SERVICE_ID,srv.SM_SERVICE_NAME, srv.SM_SERVICE_NAME_MAR, cfc.APM_FNAME, cfc.APM_MNAME,cfc.APM_LNAME, cfc.APM_APPLICATION_DATE,wd.ISSUED_DATE,\r\n" + 
        		"  'WorkOrder' Type FROM\r\n" + 
        		"  TB_CFC_APPLICATION_MST cfc,\r\n" + 
        		"  TB_SERVICES_MST srv,\r\n" + 
        		"  TB_WORK_ORDER wd WHERE  srv.SM_SERVICE_ID = cfc.SM_SERVICE_ID \r\n" + 
        		"  AND wd.WO_APPLICATION_ID = cfc.APM_APPLICATION_ID\r\n" + 
        		"  AND wd.ISSUED_BY IS NOT NULL  \r\n" + 
        		"  AND "+ taskSearchRequest.getOrgId() + ")A order by ISSUED_DATE desc");

        Query query = this.createNativeQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> objList = query.getResultList();
        return objList;
    }
    

   
    //D#127111
    @Override
    public List<Object[]> findComplaintTaskForEmployee(TaskSearchRequest taskSearchRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("  SELECT\r\n" + 
                "        a.APM_APPLICATION_ID AS APM_APPLICATION_ID,\r\n" + 
                "        a.ORGID AS ORGID,\r\n" + 
                "        c.DP_DEPTID AS DP_DEPTID,\r\n" + 
                "        (SELECT  DP_DEPTDESC FROM tb_department WHERE DP_DEPTID = c.DP_DEPTID) AS DP_DEPTDESC,\r\n" + 
                "        (SELECT  DP_NAME_MAR FROM tb_department WHERE DP_DEPTID = c.DP_DEPTID) AS DP_NAME_MAR,\r\n" + 
                "        c.SM_SERVICE_ID AS SM_SERVICE_ID,\r\n" + 
                "        (SELECT SM_SERVICE_NAME FROM tb_services_mst WHERE SM_SERVICE_ID = c.SM_SERVICE_ID) AS SM_SERVICE_NAME,\r\n" + 
                "        (SELECT SM_SERVICE_NAME_MAR FROM tb_services_mst WHERE SM_SERVICE_ID = c.SM_SERVICE_ID) AS SM_SERVICE_NAME_MAR,\r\n" + 
                "        c.EVENT_ID AS EVENT_ID,\r\n" + 
                "        (SELECT SMFNAME FROM tb_sysmodfunction WHERE SMFID = c.EVENT_ID) AS serviceEventName,\r\n" + 
        
        "        (SELECT SMFNAME_MAR FROM tb_sysmodfunction WHERE SMFID = c.EVENT_ID) AS serviceEventNameReg,\r\n" + 
                "        a.TASK_ID AS TASK_ID,\r\n" + 
                "        a.TASK_NAME AS TASK_NAME,\r\n" + 
                "        c.SMFACTION AS SMFACTION,\r\n" + 
                "        e.DATE_OF_REQUEST AS DATE_OF_REQUEST,\r\n" + 
                "        c.WORKFLOW_REQ_ID AS WORKFLOW_REQ_ID,\r\n" + 
                "        c.TASK_STATUS AS TASK_STATUS,\r\n" + 
                "        e.LAST_DECISION AS DECISION,\r\n" + 
                "        a.EMPID AS EMPID,\r\n" + 
                "        a.REFERENCE_ID AS REFERENCE_ID,\r\n" +
                "        e.WORFLOW_TYPE_ID AS WORFLOW_TYPE_ID,\r\n" + 
                "        c.TASK_SLA_DURATION AS TASK_SLA_DURATION,\r\n" +
                "            c.WFTASK_ASSIGDATE as WFTASK_ASSIGDATE,\r\n" +
                "            c.WFTASK_ASSIGDATE as WFTASK_COMPDATE\r\n" +
                "     FROM tb_workflow_request e,\r\n" + 
                "         tb_workflow_task c,\r\n" + 
                "         tb_workflow_action a,\r\n" + 
                        " tb_services_mst s  "+
                "    WHERE a.WFTASK_ID = c.WFTASK_ID\r\n" + 
                "            AND e.WORKFLOW_REQ_ID = c.WORKFLOW_REQ_ID \r\n"
                + "                     AND a.empId =:empId  and c.task_Status=:status \r\n"
                + "                     AND a.orgId =:orgId  AND s.SM_SERVICE_ID = c.SM_SERVICE_ID "+
             "           AND  s.SM_SHORTDESC = 'CARE' AND E.STATUS=:wfStatus order by c.WFTASK_ASSIGDATE desc \r\n ");

        
        final Query query = createNativeQuery(builder.toString());
        query.setParameter("empId", taskSearchRequest.getEmpId());
        query.setParameter("status", taskSearchRequest.getStatus());
        query.setParameter("orgId", taskSearchRequest.getOrgId());

		String wfStatus = StringUtils.equalsIgnoreCase("PENDING", taskSearchRequest.getStatus()) ? "PENDING"
				: StringUtils.equalsIgnoreCase("COMPLETED", taskSearchRequest.getStatus()) ? "CLOSED" : "PENDING";

        if (wfStatus != null) {
            query.setParameter("wfStatus", wfStatus);
        }
        
        @SuppressWarnings("unchecked")
        List<Object[]> objList = query.getResultList();
        return objList;
    }

	@Override
	public List<WorkflowMas> getWorkFlowByDivWardZoneAndConnSizeSpecification(Long orgId, Long deptId, Long serviceId,
			BigDecimal connectionSize, Long division, Long ward) {
		 StringBuilder builder = new StringBuilder();
	        builder.append("SELECT wt FROM WorkflowMas wt"
	                + " where wt.organisation.orgid = " + orgId
	                + " and wt.department.dpDeptid = " + deptId
	                + " and wt.service.smServiceId = " + serviceId
	                + " and wt.complaint is NULL"
	                + " and wt.status = 'Y'");

	        if (connectionSize != null) {
	            builder.append(" and wt.fromAmount <= " + connectionSize + " and wt.toAmount >= " + connectionSize);
	        }
	        if (division != null) {
	            builder.append(" and wt.codIdOperLevel1 = " + division);
	        }
	        if (ward != null) {
	            builder.append(" and wt.codIdOperLevel2 = " + ward);
	        }
	        Query query = this.createQuery(builder.toString());
	        
	        @SuppressWarnings("unchecked")
	        List<WorkflowMas> list = query.getResultList();
	        
	        if(list==null || list.isEmpty()) {
	     	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
	     	   throw new FrameworkException("Workflow not found for the specified combination");
	        }
	        return list;
	}
	
    @Override
    public WorkflowMas getWorkFlowTypeByOrgDepartmentAndWmsForWardZone(Long orgId, Long getwmschcodeid2,
            Long codIdOperLevel1, Long codIdOperLevel2, Long codIdOperLevel3, Long codIdOperLevel4,
            Long codIdOperLevel5, Long serviceId,BigDecimal amount) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT wt FROM WorkflowMas wt"
                + " where wt.organisation.orgid = " + orgId
                + " and wt.wmSchCodeId2= " + getwmschcodeid2
                + " and wt.type = 'N'"
                + " and wt.status = 'Y' ");
        if (codIdOperLevel1 != null)
            builder.append(" and wt.codIdOperLevel1 = " + codIdOperLevel1);
        if (codIdOperLevel2 != null)
            builder.append(" and wt.codIdOperLevel2 = " + codIdOperLevel2);
        if (codIdOperLevel3 != null)
            builder.append(" and wt.codIdOperLevel3 = " + codIdOperLevel3);
        if (codIdOperLevel4 != null)
            builder.append(" and wt.codIdOperLevel4 = " + codIdOperLevel4);
        if (codIdOperLevel5 != null)
            builder.append(" and wt.codIdOperLevel5 = " + codIdOperLevel5);
        if(serviceId !=null)
        	builder.append(  "and wt.service.smServiceId = " + serviceId);
        if (amount != null)
            builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);

        Query query = this.createQuery(builder.toString());
        @SuppressWarnings("unchecked")
        List<WorkflowMas> list = query.getResultList();
        if(list.isEmpty()) {
     	   LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
        }
        return list.isEmpty() == true ? null : list.get(0);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<AapleSarkarSummaryEntity> getAapleSarkarSummary() {
		List<AapleSarkarSummaryEntity> aapleSarkarSummaryEntity = null;
		
		StringBuilder queryBuilder = new StringBuilder();
		
		/*queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT 'KDMCDeptN' as Department,SUBSTR(AAPLE_SARKAR_ID,66,4) Service,'6' as Division,'520' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN  WR.APM_APPLICATION_ID END),0) Approved,'0' as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND  (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery,'0' as PendingatUser, IFNULL(COUNT(CASE WHEN  WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery,'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID WHERE AAPLE_SARKAR_ID IS NOT NULL GROUP BY Service) s,(SELECT @row_number\\:=0) AS rn");*/
		
		/*queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT 'KDMCDeptN' as Department,SUBSTR(AAPLE_SARKAR_ID,66,4) Service,'6' as Division,'520' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,'0' as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery,'0' as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery,'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID WHERE AAPLE_SARKAR_ID IS NOT NULL AND date(WR.DATE_OF_REQUEST) BETWEEN str_to_date( CONCAT(YEAR(CURDATE()),'-',(CASE WHEN MONTH(CURDATE()) BETWEEN 1 AND 9 THEN CONCAT('0', MONTH(CURDATE())) ELSE MONTH(CURDATE()) END),'-01'),'%Y-%m-%d') and LAST_DAY(CURDATE()) GROUP BY Service) s,(SELECT @row_number\\:=0) AS rn");*/
		
		/*
		 * queryBuilder.
		 * append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT 'KDMCDeptN' as Department,SUBSTR(AAPLE_SARKAR_ID,66,4) Service,'6' as Division,'520' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,'0' as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery,'0' as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID WHERE AAPLE_SARKAR_ID IS NOT NULL AND date(WR.DATE_OF_REQUEST) BETWEEN str_to_date( CONCAT(YEAR(CURDATE()),'-',(CASE WHEN MONTH(CURDATE()) BETWEEN 1 AND 9 THEN CONCAT('0', MONTH(CURDATE())) ELSE MONTH(CURDATE()) END),'-01'),'%Y-%m-%d') and LAST_DAY(CURDATE()) GROUP BY Service) s,(SELECT @row_number\\:=0) AS rn"
		 * );
		 */		
		/*queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT 'KDMCDeptN' as Department,SUBSTR(AAPLE_SARKAR_ID,66,4) Service,'6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,'0' as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery,'0' as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID WHERE AAPLE_SARKAR_ID IS NOT NULL AND WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) GROUP BY Service UNION ALL SELECT 'KDMCDeptN' as Department,SUBSTR(AAPLE_SARKAR_ID,66,4) Service,'6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CFC.APM_APPLICATION_ID),0) Approved,'0' as Rejected,IFNULL(COUNT(CFC.APM_APPLICATION_ID),0) OnTimeDelivery,'0' as PendingatUser, '0' PendingatDepartment, '0' NotOnTimeDelivery, '0' PendingatDepartmentWithinSLA, '0' PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC WHERE AAPLE_SARKAR_ID IS NOT NULL AND CFC.APM_APPLICATION_DATE>=DATE_FORMAT(NOW(), '%Y-%m-01') and CFC.APM_APPLICATION_DATE<=LAST_DAY(NOW()) AND NOT EXISTS(SELECT 1 FROM tb_workflow_request WR WHERE WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID) GROUP BY Service) s,(SELECT @row_number\\:=0) AS rn");*/
		
		//#286236
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(LAST_DATE_OF_ACTION,DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_comparent_det COD ON CFC.SM_SERVICE_ID=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' GROUP BY Service UNION ALL SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_care_request CR ON CR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_dep_complaint_subtype CSBT ON CSBT.COMP_ID=CR.COMP_SUBTYPE_ID INNER JOIN tb_comparent_det COD ON CSBT.COMP_ID=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' GROUP BY Service UNION ALL SELECT Department, Service, Division, District, Taluka, SUM(Approved) Approved, SUM(Rejected) Rejected, SUM(OnTimeDelivery) OnTimeDelivery, SUM(PendingatUser) PendingatUser, SUM(PendingatDepartment) PendingatDepartment, SUM(NotOnTimeDelivery) NotOnTimeDelivery, SUM(PendingatDepartmentWithinSLA) PendingatDepartmentWithinSLA, SUM(PendingatDepartmentAfterSLA) PendingatDepartmentAfterSLA, PaymentMode, ApplicationSource FROM ( SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_services_mst SM ON SM.SM_SERVICE_ID=CFC.SM_SERVICE_ID INNER JOIN tb_ml_trade_mast_hist LMH ON LMH.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN lateral(SELECT DISTINCT TRD_ID,TRI_COD1 FROM tb_ml_item_detail ID WHERE ID.TRD_ID=LMH.TRD_ID) ID ON ID.TRD_ID=LMH.TRD_ID INNER JOIN tb_comparent_det COD ON ID.TRI_COD1=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' AND SM.SM_SHORTDESC='NTL' AND WR.STATUS='CLOSED' GROUP BY Service UNION ALL SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_services_mst SM ON SM.SM_SERVICE_ID=CFC.SM_SERVICE_ID INNER JOIN tb_ml_trade_mast LM ON LM.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN lateral(SELECT DISTINCT TRD_ID,TRI_COD1 FROM tb_ml_item_detail ID WHERE ID.TRD_ID=LM.TRD_ID) ID ON ID.TRD_ID=LM.TRD_ID INNER JOIN tb_comparent_det COD ON ID.TRI_COD1=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' AND SM.SM_SHORTDESC='NTL' AND WR.STATUS='PENDING' GROUP BY Service) NTL GROUP BY Department, Service, Division, District, Taluka, PaymentMode, ApplicationSource UNION ALL SELECT Department, Service, Division, District, Taluka, SUM(Approved) Approved, SUM(Rejected) Rejected, SUM(OnTimeDelivery) OnTimeDelivery, SUM(PendingatUser) PendingatUser, SUM(PendingatDepartment) PendingatDepartment, SUM(NotOnTimeDelivery) NotOnTimeDelivery, SUM(PendingatDepartmentWithinSLA) PendingatDepartmentWithinSLA, SUM(PendingatDepartmentAfterSLA) PendingatDepartmentAfterSLA, PaymentMode, ApplicationSource FROM ( SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_services_mst SM ON SM.SM_SERVICE_ID=CFC.SM_SERVICE_ID INNER JOIN tb_ml_trade_mast_hist LMH ON LMH.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_comparent_det COD ON LMH.Trans_Mode_TYPE=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' AND SM.SM_SHORTDESC='TLA' AND WR.STATUS='CLOSED' GROUP BY Service UNION ALL SELECT 'KDMCDeptN' as Department, COD.COD_OTHERS Service, '6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='APPROVED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) Approved,IFNULL(COUNT(CASE WHEN WR.LAST_DECISION='REJECTED' AND WR.STATUS='CLOSED' THEN WR.APM_APPLICATION_ID END),0) as Rejected,IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) OnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) as PendingatUser, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' THEN WR.APM_APPLICATION_ID END),0) PendingatDepartment, IFNULL(COUNT(CASE WHEN WR.STATUS='CLOSED' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.LAST_DATE_OF_ACTION,WR.DATE_OF_REQUEST) THEN WR.APM_APPLICATION_ID END),0) NotOnTimeDelivery, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)>=datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentWithinSLA, IFNULL(COUNT(CASE WHEN WR.STATUS='PENDING' AND (SELECT SUM(WFD_SLA) FROM TB_WORKFLOW_DET WHERE WF_ID=WR.WORFLOW_TYPE_ID)<datediff(WR.DATE_OF_REQUEST,NOW()) THEN WR.APM_APPLICATION_ID END),0) PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_workflow_request WR ON WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_services_mst SM ON SM.SM_SERVICE_ID=CFC.SM_SERVICE_ID INNER JOIN tb_ml_trade_mast LM ON LM.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID INNER JOIN tb_comparent_det COD ON LM.Trans_Mode_TYPE=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE WR.LAST_DATE_OF_ACTION>=DATE_FORMAT(NOW(), '%Y-%m-01') and WR.LAST_DATE_OF_ACTION<=LAST_DAY(NOW()) AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' AND SM.SM_SHORTDESC='TLA' AND WR.STATUS='PENDING' GROUP BY Service) TLA GROUP BY Department, Service, Division, District, Taluka, PaymentMode, ApplicationSource UNION ALL SELECT 'KDMCDeptN' as Department,COD.COD_OTHERS Service,'6' as Division,'517' as District,'0' as Taluka, IFNULL(COUNT(CFC.APM_APPLICATION_ID),0) Approved,'0' as Rejected,IFNULL(COUNT(CFC.APM_APPLICATION_ID),0) OnTimeDelivery,'0' as PendingatUser, '0' PendingatDepartment, '0' NotOnTimeDelivery, '0' PendingatDepartmentWithinSLA, '0' PendingatDepartmentAfterSLA, 'PG' as PaymentMode,'D' as ApplicationSource FROM tb_cfc_application_mst CFC INNER JOIN tb_comparent_det COD ON CFC.SM_SERVICE_ID=COD.COD_VALUE INNER JOIN tb_comparent_mas COM ON COM.COM_ID=COD.COM_ID INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID=COM.CPM_ID WHERE CFC.APM_APPLICATION_DATE>=DATE_FORMAT(NOW(), '%Y-%m-01') and CFC.APM_APPLICATION_DATE<=LAST_DAY(NOW()) AND NOT EXISTS(SELECT 1 FROM tb_workflow_request WR WHERE WR.APM_APPLICATION_ID=CFC.APM_APPLICATION_ID)  AND COM.COM_VALUE='RTS' AND CPM.CPM_PREFIX='RTS' GROUP BY Service) s,(SELECT @row_number\\:=0) AS rn");
		
		aapleSarkarSummaryEntity = (List<AapleSarkarSummaryEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), AapleSarkarSummaryEntity.class).getResultList();
		
		return aapleSarkarSummaryEntity;
	}
	
	@Override
	public List<Object[]> findEstateOutstandingDetailsForSms(Long orgId) {
	    StringBuilder builder = new StringBuilder();

	    builder.append("SELECT EM.ES_NAME_ENG, ")
	           .append(" PM.PROP_NAME, ")
	           .append(" PM.PM_PROPNO, ")
	           .append(" PM.PM_OLDPROPNO AS OLD_PROPERTY_NUMBER, ")
	           .append(" CM.CONT_NO, ")
	           .append(" VM.VM_VENDORNAME, ")
	           .append(" IFNULL(VM.MOBILE_NO, '') AS MOBILE, ")
	           .append(" ROUND( ")
	           .append("   IFNULL(BM_OUTSTANDING.ESTATE_RENT, 0) + ")
	           .append("   IFNULL(BM_OUTSTANDING.OTHER_CHARGES, 0) + ")
	           .append("   ROUND(IFNULL(BM_OUTSTANDING.ESTATE_RENT, 0) * 0.18, 2) ")
	           .append(" ) AS TOTAL_OUTSTANDING ")
	           .append("FROM tb_rl_estate_mas EM ")
	           .append("INNER JOIN tb_rl_property_mas PM ON EM.ES_ID = PM.ES_ID ")
	           .append("INNER JOIN tb_rl_est_contract_mapping CMP ON CMP.ES_ID = EM.ES_ID AND CMP.prop_id = PM.prop_id ")
	           .append("INNER JOIN tb_contract_mast CM ON CM.CONT_ID = CMP.CONT_ID ")
	           .append("INNER JOIN tb_contract_part2_detail CP2 ON CMP.CONT_ID = CP2.CONT_ID ")
	           .append("INNER JOIN tb_vendormaster VM ON VM.VM_VENDORID = CP2.VM_VENDORID ")
	           .append("LEFT JOIN ( ")
	           .append("   SELECT BM.CONT_ID, ")
	           .append("          IFNULL(SUM(CASE WHEN TM.TAX_CODE = 'RL248' THEN BM.bm_balance_amt END), 0) AS ESTATE_RENT, ")
	           .append("          IFNULL(SUM(CASE WHEN TM.TAX_CODE IN ('RL251','RL252','RL253','RL254') THEN BM.bm_balance_amt END), 0) AS OTHER_CHARGES ")
	           .append("   FROM tb_rl_bill_mast BM ")
	           .append("   INNER JOIN TB_TAX_MAS TM ON TM.TAX_ID = BM.TAX_ID ")
	           .append("   WHERE DATE(BM.bm_due_date) <= DATE(CURRENT_DATE()) ")
	           .append("     AND TM.DP_DEPTID = (SELECT DP_DEPTID FROM tb_department WHERE DP_DEPTCODE = 'RL') ")
	           .append("   GROUP BY BM.CONT_ID ")
	           .append(") BM_OUTSTANDING ON BM_OUTSTANDING.CONT_ID = CM.CONT_ID ")
	           .append("WHERE CP2.CONTP2_TYPE = 'V' ")
	           .append("AND EM.ORGID = ").append(orgId).append(" ")
	           .append("AND ( ")
	           .append("   IFNULL(BM_OUTSTANDING.ESTATE_RENT, 0) + ")
	           .append("   IFNULL(BM_OUTSTANDING.OTHER_CHARGES, 0) + ")
	           .append("   ROUND(IFNULL(BM_OUTSTANDING.ESTATE_RENT, 0) * 0.18, 2) ")
	           .append(" ) > 0");

	    Query query = this.createNativeQuery(builder.toString());

	    @SuppressWarnings("unchecked")
	    List<Object[]> objList = query.getResultList();

	    return objList;
	}

	@Override
	public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
			Long sourceOfFund, Long getwmschcodeid2, Long propType) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT wt FROM WorkflowMas wt" + " where wt.organisation.orgid = " + orgId
				+ " and wt.department.dpDeptid = " + deptId + " and wt.service.smServiceId = " + serviceId
				+ " and wt.wmSchCodeId2= " + getwmschcodeid2 + " and wt.complaint is NULL" + " and wt.type = 'A'"
				+ " and wt.status = 'Y'");

		if (sourceOfFund != null && sourceOfFund != 0)
			builder.append(" and wt.wmSchCodeId1=" + sourceOfFund);

		if (getwmschcodeid2 != null && getwmschcodeid2 != 0)
			builder.append(" and wt.wmSchCodeId2=" + getwmschcodeid2);

		if (propType != null && propType != 0)
			builder.append(" and wt.propType = " + propType);

		if (amount != null)
			builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);

		Query query = this.createQuery(builder.toString());
		@SuppressWarnings("unchecked")
		List<WorkflowMas> list = query.getResultList();
		if (list.isEmpty()) {
			LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
		}
		return list.isEmpty() == true ? null : list.get(0);
	}

	@Override
	public WorkflowMas getServiceWorkFlowForWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
			Long codIdOperLevel1, Long codIdOperLevel2, Long codIdOperLevel3, Long codIdOperLevel4,
			Long codIdOperLevel5, Long propType) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT wt FROM WorkflowMas wt" + " where wt.organisation.orgid = " + orgId
				+ " and wt.department.dpDeptid = " + deptId + " and wt.complaint is NULL"
				+ " and wt.service.smServiceId = " + serviceId + " and wt.type = 'N'" + " and wt.status = 'Y' ");
		if (amount != null)
			builder.append(" and wt.fromAmount <= " + amount + " and wt.toAmount >= " + amount);
		if (codIdOperLevel1 != null && codIdOperLevel1 > 0d)
			builder.append(" and wt.codIdOperLevel1 = " + codIdOperLevel1);
		if (codIdOperLevel2 != null && codIdOperLevel2 > 0d)
			builder.append(" and wt.codIdOperLevel2 = " + codIdOperLevel2);
		if (codIdOperLevel3 != null && codIdOperLevel3 > 0d)
			builder.append(" and wt.codIdOperLevel3 = " + codIdOperLevel3);
		if (codIdOperLevel4 != null && codIdOperLevel4 > 0d)
			builder.append(" and wt.codIdOperLevel4 = " + codIdOperLevel4);
		if (codIdOperLevel5 != null && codIdOperLevel5 > 0d)
			builder.append(" and wt.codIdOperLevel5 = " + codIdOperLevel5);
		if (propType != null && propType != 0)
			builder.append(" and wt.propType = " + propType);

		Query query = this.createQuery(builder.toString());
		@SuppressWarnings("unchecked")
		List<WorkflowMas> list = query.getResultList();
		if (list.isEmpty()) {
			LOGGER.error("Workflow not found for given Parameters : " + builder.toString());
		}
		return list.isEmpty() == true ? null : list.get(0);
	}

	@Override
	public List<WorkflowMas> getDefinedActiveWorkFlows(Long orgId, Long deptId, Long serviceId, Long compId, Long schId,
			Long propType) {
		StringBuilder sb = new StringBuilder();
		sb.append("select wt from WorkflowMas wt where wt.status = 'Y' and wt.organisation.orgid =" + orgId
				+ " and wt.department.dpDeptid = " + deptId + " and wt.service.smServiceId = " + serviceId);
		if (compId != 0 && compId != null) {
			sb.append(" and wt.complaint.compId = " + compId);
		}
		if (schId != null && schId != 0) {
			sb.append(" and wt.wmSchCodeId2 = " + schId);
		}
		if (propType != null && propType != 0) {
			sb.append(" and wt.propType = " + propType);
		}
		sb.append(" order by wt.fromAmount asc ");

		Query query = this.createQuery(sb.toString());
		@SuppressWarnings("unchecked")
		List<WorkflowMas> list = query.getResultList();
		return list;
	}
	
    

}
