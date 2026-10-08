package com.abm.mainet.common.master.service;

import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.domain.Organisation;

/**
 * @author Harsha.Ramachandran
 */

public interface GroupMasterService {

    public GroupMaster createGroupMaster(GroupMaster groupMaster, Organisation organisation);

    public GroupMaster findByGmId(Long gmId, Long orgId);
    
    public GroupMaster getAdminGroupByOrg(Long orgId);
    
    public void update(Long gmId,String roleDescriptionEng,String roleDescriptionReg);

}
