package com.abm.mainet.common.workflow.dao;

import java.math.BigDecimal;
import java.util.List;

import com.abm.mainet.common.workflow.domain.AapleSarkarSummaryEntity;
import com.abm.mainet.common.workflow.domain.WorkflowMas;
import com.abm.mainet.common.workflow.dto.TaskSearchRequest;


public interface IWorkflowTypeDAO {

    /**
     * @param orgId
     * @param serviceId
     * @param compId
     * @return
     */
    public List<WorkflowMas> getDefinedActiveWorkFlows(Long orgId, Long deptId, Long serviceId, Long compId,Long schId);

    public List<WorkflowMas> getAllWorkFlows(Long orgId, Long deptId, Long serviceId);

    public WorkflowMas getWorkFlowTypeByOrgDepartmentAndComplaintTypeForWardZone(Long orgId, Long compId,
            Long codIdOperLevel1,
            Long codIdOperLevel2,
            Long codIdOperLevel3,
            Long codIdOperLevel4,
            Long codIdOperLevel5);

    public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long sourceOfFund);
    
    public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long sourceOfFund,Long getwmschcodeid2);
    

    public WorkflowMas getServiceWorkFlowForWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long codIdOperLevel1,
            Long codIdOperLevel2,
            Long codIdOperLevel3,
            Long codIdOperLevel4,
            Long codIdOperLevel5);

	public Long getTaskIdByAppIdAndOrgId(Long appId, Long orgId);
	
	List<Object[]> fetchClosedTaskList(TaskSearchRequest taskSearchRequest);
	
	List<Object[]> findComplaintTaskForEmployee(TaskSearchRequest taskSearchRequest);

	List<Object[]> fetchIssuedTaskList(TaskSearchRequest taskSearchRequest);

	/**
	 * This query is used to find workflow defined by the specific parameters
	 * @author akshata.bhat
	 * @param orgId
	 * @param deptId
	 * @param serviceId
	 * @param connectionSize
	 * @param division
	 * @param ward
	 * @return WorkflowMas
	 */
	public List<WorkflowMas> getWorkFlowByDivWardZoneAndConnSizeSpecification(Long orgId, Long deptId, Long serviceId,
			BigDecimal connectionSize, Long division, Long ward);
	
	WorkflowMas getWorkFlowTypeByOrgDepartmentAndWmsForWardZone(Long orgId, Long getwmschcodeid2, Long codIdOperLevel1,
			Long codIdOperLevel2, Long codIdOperLevel3, Long codIdOperLevel4, Long codIdOperLevel5, Long serviceId,BigDecimal amount);
	
	
	public List<AapleSarkarSummaryEntity> getAapleSarkarSummary();

	List<Object[]> findEstateOutstandingDetailsForSms(Long orgId);
	
	public WorkflowMas getServiceWorkFlowForAllWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
            Long sourceOfFund,Long getwmschcodeid2, Long propType);
	
	public WorkflowMas getServiceWorkFlowForWardZone(Long orgId, Long deptId, Long serviceId, BigDecimal amount,
    		Long codIdOperLevel1,
    		Long codIdOperLevel2,
    		Long codIdOperLevel3,
    		Long codIdOperLevel4,
    		Long codIdOperLevel5,
    		Long propType);
	
	public List<WorkflowMas> getDefinedActiveWorkFlows(Long orgId, Long deptId, Long serviceId, Long compId,Long schId, Long propType);
}
