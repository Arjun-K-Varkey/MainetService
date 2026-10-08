package com.abm.mainet.common.master.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.master.dao.IGroupMasterDAO;

@Component
public class GroupMasterServiceImpl implements GroupMasterService {

    @Autowired
    IGroupMasterDAO groupMasterDAO;

    @Override
    @Transactional
    public GroupMaster createGroupMaster(final GroupMaster groupMaster,
            final Organisation organisation) {

        return groupMasterDAO.createGroupMaster(groupMaster, organisation);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupMaster findByGmId(final Long gmId, final Long orgId) {
        return groupMasterDAO.findByGmId(gmId, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupMaster getAdminGroupByOrg(Long orgId) {
        return groupMasterDAO.getAdminGroup(orgId);
    }

    @Override
    @Transactional
    public void update(Long gmId, String roleDescriptionEng, String roleDescriptionReg) {
        groupMasterDAO.update(gmId, roleDescriptionEng, roleDescriptionReg);
    }

}
