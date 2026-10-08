package com.abm.mainet.common.workflow.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dto.ComplaintTypeBean;
import com.abm.mainet.common.ui.model.AbstractFormModel;
import com.abm.mainet.common.utility.ApplicationSession;
import com.abm.mainet.common.utility.LookUp;
import com.abm.mainet.common.utility.UserSession;
import com.abm.mainet.common.workflow.dto.WorkflowMasDTO;
import com.abm.mainet.common.workflow.service.IWorkFlowTypeService;

/**
 * @author ritesh.patil
 *
 */
@Component
@Scope("session")
public class WorkFlowTypeModel extends AbstractFormModel {

	private static final long serialVersionUID = 1L;
	private String modeType;
	private List<Object[]> orgList;
	private String prefixName;
	private List<Object[]> eventList;
	private List<Object[]> deptList;
	private List<Object[]> serviceList;
	private List<ComplaintTypeBean> compList;
	private List<Object[]> empList;
	private List<Object[]> roleList;
	private String removeChildIds;
	private Long orgId;
	public WorkflowMasDTO workFlowMasDTO;

	private List<LookUp> sourceLookUps = new ArrayList<LookUp>();
	private List<LookUp> sourceLevelLookUps = new ArrayList<LookUp>();

	ApplicationSession appSession = ApplicationSession.getInstance();
	private String kdmcEnv;
	private String deptShortName;

	@Autowired
	private IWorkFlowTypeService iWorkFlowTypeService;

	public void initializeModelEdit(final String prefix) {
		initializeLookupFields(prefix);
	}

	@Override
	protected final String findPropertyPathPrefix(final String parentCode) {
		final String result = MainetConstants.WORKFLOWTYPE.WORK_FLOW_TYPE_DTO_COD_ID_OPER_LEVEL;
		return result;
	}

	@Override
	public boolean saveForm() {
		WorkflowMasDTO workFlowTypeDTO = this.getWorkFlowMasDTO();
		if (this.getModeType().equals(MainetConstants.WORKFLOWTYPE.MODE_CREATE)) {
			workFlowTypeDTO.setCurrentOrgId(UserSession.getCurrent().getOrganisation().getOrgid());
			workFlowTypeDTO.setCreatedBy(UserSession.getCurrent().getEmployee().getEmpId());
			workFlowTypeDTO.setCreateDate(new Date());
			iWorkFlowTypeService.saveForm(workFlowTypeDTO);
			this.setSuccessMessage(appSession.getMessage("workflow.type.create.success"));
		} else if (this.getModeType().equals(MainetConstants.WORKFLOWTYPE.MODE_EDIT)) {
			workFlowTypeDTO.setUpdatedBy(UserSession.getCurrent().getEmployee().getEmpId());
			List<Long> removeIds = new ArrayList<>();
			String ids = this.getRemoveChildIds();
			if (!ids.isEmpty()) {
				String array[] = ids.split(",");
				for (String id : array) {
					removeIds.add(Long.valueOf(id));
				}
			}
			//D#36831 validation for check removeIds task completed or not
			if(!removeIds.isEmpty()) {
			    boolean pendingTask = iWorkFlowTypeService.checkPendingTask(removeIds, orgId); 
			    if(pendingTask) {
			        this.setSuccessMessage(appSession.getMessage("workflow.type.val.pendingTask"));
			        return true;
			    }
			}
			iWorkFlowTypeService.saveEdit(workFlowTypeDTO, removeIds);
			this.setSuccessMessage(appSession.getMessage("workflow.type.edit.success"));
		}
		return true;

	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(final String prefixName) {
		this.prefixName = prefixName;
	}

	public List<Object[]> getOrgList() {
		return orgList;
	}

	public void setOrgList(final List<Object[]> orgList) {
		this.orgList = orgList;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(final String modeType) {
		this.modeType = modeType;
	}

	public List<Object[]> getEventList() {
		return eventList;
	}

	public void setEventList(final List<Object[]> eventList) {
		this.eventList = eventList;
	}

	public String getRemoveChildIds() {
		return removeChildIds;
	}

	public void setRemoveChildIds(final String removeChildIds) {
		this.removeChildIds = removeChildIds;
	}

	public List<Object[]> getDeptList() {
		return deptList;
	}

	public void setDeptList(final List<Object[]> deptList) {
		this.deptList = deptList;
	}

	public List<Object[]> getServiceList() {
		return serviceList;
	}

	public void setServiceList(final List<Object[]> serviceList) {
		this.serviceList = serviceList;
	}

	public List<ComplaintTypeBean> getCompList() {
		return compList;
	}

	public void setCompList(final List<ComplaintTypeBean> compList) {
		this.compList = compList;
	}

	public List<Object[]> getEmpList() {
		return empList;
	}

	public void setEmpList(final List<Object[]> empList) {
		this.empList = empList;
	}

	public List<Object[]> getRoleList() {
		return roleList;
	}

	public void setRoleList(final List<Object[]> roleList) {
		this.roleList = roleList;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public WorkflowMasDTO getWorkFlowMasDTO() {
		return workFlowMasDTO;
	}

	public void setWorkFlowMasDTO(WorkflowMasDTO workFlowMasDTO) {
		this.workFlowMasDTO = workFlowMasDTO;
	}

	public List<LookUp> getSourceLookUps() {
		return sourceLookUps;
	}

	public void setSourceLookUps(List<LookUp> sourceLookUps) {
		this.sourceLookUps = sourceLookUps;
	}

	public List<LookUp> getSourceLevelLookUps() {
		return sourceLevelLookUps;
	}

	public void setSourceLevelLookUps(List<LookUp> sourceLevelLookUps) {
		this.sourceLevelLookUps = sourceLevelLookUps;
	}

	public String getKdmcEnv() {
		return kdmcEnv;
	}

	public void setKdmcEnv(String kdmcEnv) {
		this.kdmcEnv = kdmcEnv;
	}

	public String getDeptShortName() {
		return deptShortName;
	}

	public void setDeptShortName(String deptShortName) {
		this.deptShortName = deptShortName;
	}

}
