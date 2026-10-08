package com.abm.mainet.common.workflow.service;

import java.util.List;

import com.abm.mainet.common.workflow.dto.AapleSarkarSummaryDTO;
import com.abm.mainet.quartz.domain.QuartzSchedulerMaster;
import com.abm.mainet.smsemail.dto.SMSAndEmailDTO;

/**
 * 
 * @author Jeetendra.Pal
 *
 */
public interface IWorkFlowJobSchedulerService {

    /**
     * used to update status of objection task based on sla duration in workflow task table
     * @param runtimeBean
     * @param parameterList
     */
    void updateWorkflowObjectionTask(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);

    /**
     * used to add all auto escalation task in task table from newly generated task in KIE
     * @param runtimeBean
     * @param parameterList
     */
    void updateWorkflowAutoEscalationTask(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);
    
    void sendDailySMSForCare(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);
    
    void sendMessageAndEmail(SMSAndEmailDTO smsAndEmailDTO, Long orgId);
    
    void sendDailyStatusToAapleSarkar(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);
    
    public String getTokenaapleSarkarAPI();
    
    public boolean pushAapleSarkarData(String accessToken, AapleSarkarSummaryDTO aapleSarkarSummaryDTO);

	void estatePaymentMonthlySMS(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);

	void sendMessageAndEmailForRnl(SMSAndEmailDTO smsAndEmailDTO, Long orgId);
	
	public void getBpmsDailyPropsalId(QuartzSchedulerMaster runtimeBean, List<Object> parameterList);
}
