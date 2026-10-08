/**
 *
 */
package com.abm.mainet.cfc.scrutiny.dao;

import java.util.List;

import com.abm.mainet.common.domain.GroupMaster;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;

/**
 * @author Lalit.Prusti
 *
 */
public interface ScrutinyDAO {

    GroupMaster getDesignationName(Long slDsgid);

    List<CFCAttachment> getAllScrutinyDocDesgWise(long applId, long orgId);

    List<Object> getfindAllscrutinyLabelValueList(Long orgId, Long serviceId, Long scrutinyId, Long applicationId, Long tricod1);

    String getValueByLabelQuery(String query);

}
