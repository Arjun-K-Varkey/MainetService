/**
 *
 */
package com.abm.mainet.cfc.scrutiny.service;

import java.util.List;
import java.util.Map;

import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLabelDTO;
import com.abm.mainet.cfc.scrutiny.dto.ScrutinyLableValueDTO;
import com.abm.mainet.cfc.scrutiny.dto.ViewCFCScrutinyLabelValue;
import com.abm.mainet.common.integration.dms.domain.CFCAttachment;
import com.abm.mainet.common.integration.dto.CustomerInfoDTO;
import com.abm.mainet.common.utility.UserSession;

/**
 *
 * @author umashanker.kanaujiya
 *
 */
public interface ScrutinyService {

    ScrutinyLabelDTO populateScrutinyLabelData(long applicationId, long empId, long gmId, long orgId, long serviceId, int langId);

    boolean saveScrutinyValueBylabelId(ScrutinyLableValueDTO lableValueDTO);

    boolean saveCompleteScrutinyLabel(List<ViewCFCScrutinyLabelValue> list, UserSession userSession,
            ScrutinyLabelDTO scrutinyLabelDTO, boolean updateFlag, Long taskId, List<Long> attachmentId, String wokflowDecision);

    boolean saveAllScrutinyDoc(List<CFCAttachment> scrutinyDocs);

    String getValueByLabelQuery(String query);

	Map<String, Long> getApplicationDetails(Long applicatioId);
	Boolean checkLoiGeneratedOrNot(ScrutinyLabelDTO dto,Long desgId,List<ViewCFCScrutinyLabelValue> labellIST);
	
    String getAppDetailByAppId(Long applicationId);
    

	boolean updateStatusFlagByRefId(String refId, Long orgId, String deptCode,Long empId);

	Boolean checkConnectionDetailsPresentOrNot(ScrutinyLabelDTO scrutinyLabelDTO, Long legalScrDesg,
			List<ViewCFCScrutinyLabelValue> list);

	CustomerInfoDTO getCurrentApplicationsConnSize(Long applicationId, Long orgId);
}
