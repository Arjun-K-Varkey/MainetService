package com.abm.mainet.cfc.challan.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.abm.mainet.cfc.challan.ui.model.CitizenOnlinePendingPayment;
import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.domain.Organisation;
import com.abm.mainet.common.integration.dto.ApplicationFormChallanDTO;
import com.abm.mainet.common.master.service.DepartmentService;
import com.abm.mainet.common.service.IOrganisationService;
import com.abm.mainet.common.utility.UserSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author AnwarulHasan
 * @since 27/07/2027
 */

@Service
public class CitizenOnlinePendingPaymentJobScheduler {

	private static final Logger log = Logger.getLogger(CitizenOnlinePendingPaymentJobScheduler.class);

	private static final List<String> DEPT_CODES = Arrays.asList(
	        MainetConstants.DEPT_SHORT_NAME.PROPERTY,
	        MainetConstants.DEPT_SHORT_NAME.WATER,
	        MainetConstants.DEPT_SHORT_NAME.BND,
	        MainetConstants.DEPT_SHORT_NAME.LICENSE,
	        MainetConstants.DEPT_SHORT_NAME.MARRIAGE_REGISTRATION,
	        MainetConstants.DEPT_SHORT_NAME.RNL
	);

	@Autowired
	private CitizenOnlinePendingPaymentScheduler citizenOnlinePendingPaymentScheduler;

	@Autowired
	private IOrganisationService organisationService;

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 8:00 AM run — covers yesterday 00:00 through "now" (~08:00 today).
	 */
	@Scheduled(cron = "0 0 8 * * *")
	public void runMorningSync() {
		LocalDateTime from = LocalDate.now().minusDays(1).atStartOfDay();
		LocalDateTime to = LocalDateTime.now();
		log.info("[MORNING_8AM] Job triggered. Window: " + from + " to " + to);
		runForAllOrgs("MORNING_8AM", from, to);
	}

	/**
	 * 9:00 PM run — covers yesterday 00:00 through "now" (~21:00 today).
	 */
	@Scheduled(cron = "0 0 21 * * *")
	public void runNightSync() {
		LocalDateTime from = LocalDate.now().minusDays(1).atStartOfDay();
		LocalDateTime to = LocalDateTime.now();
		log.info("[NIGHT_9PM] Job triggered. Window: " + from + " to " + to);
		runForAllOrgs("NIGHT_9PM", from, to);
	}

	/**
	 * 12:30 AM run — sweeps day-before-yesterday's data only.
	 */
	@Scheduled(cron = "0 30 0 * * *")
	public void runMidnightSweep() {
		LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
		LocalDateTime from = dayBeforeYesterday.atStartOfDay();
		LocalDateTime to = LocalDateTime.of(dayBeforeYesterday, LocalTime.of(23, 59, 59));
		log.info("[MIDNIGHT_SWEEP] Job triggered. Window: " + from + " to " + to);
		runForAllOrgs("MIDNIGHT_SWEEP", from, to);
	}

	private void runForAllOrgs(String jobName, LocalDateTime from, LocalDateTime to) {
		Date fromDate = toDate(from);
		Date toDate = toDate(to);
		log.info("[" + jobName + "] Converted window to Date: from=" + fromDate + ", to=" + toDate);

		Organisation org = organisationService.getActiveOrgByUlbShortCode(MainetConstants.KDMC);

		if (org == null) {
			log.error("[" + jobName + "] No active organisation found for ULB short code KDMC — aborting run");
			return;
		}
		log.info("[" + jobName + "] Resolved organisation orgId=" + org.getOrgid());

		// Bind a fake request/session context for this thread so session-scoped
		// beans like UserSession resolve normally instead of throwing
		// BeanCreationException ("Scope 'session' is not active for the current
		// thread"). Everything downstream (ChallanService, DepartmentServiceImpl, etc.)
		
		String sessionId = "scheduler-" + jobName + "-" + Thread.currentThread().getId();
		RequestContextHolder.setRequestAttributes(new ThreadBoundRequestAttributes(sessionId));
		try {
			UserSession systemSession = UserSession.getCurrent();
			systemSession.setOrganisation(org);
			systemSession.setLanguageId(1); // default to English
			
			log.info("[" + jobName + "] Bound system UserSession for orgId=" + org.getOrgid());

			for (String deptCode : DEPT_CODES) {
				log.info("[" + jobName + "] Starting sync for orgId=" + org.getOrgid() + ", deptCode=" + deptCode);
				try {
					Long deptId = departmentService.getDepartmentIdByDeptCode(deptCode);
					if (deptId == null) {
						log.error("[" + jobName + "] No department found for deptCode=" + deptCode + " — skipping");
						continue;
					}
					log.info("[" + jobName + "] Resolved deptId=" + deptId + " for deptCode=" + deptCode);

					CitizenOnlinePendingPayment model = buildModel(org.getOrgid(), deptId, fromDate, toDate);
					
					log.info("[" + jobName + "] Built model for orgId=" + org.getOrgid() + ", deptCode=" + deptCode
							+ ", deptId=" + deptId + " — invoking runOnlinePendingPaymentDate");

					citizenOnlinePendingPaymentScheduler.runOnlinePendingPaymentDate(model);

					log.info("[" + jobName + "] Completed sync for orgId=" + org.getOrgid() + ", deptCode=" + deptCode
							+ ", message=" + model.getSuccessMessage());
				} catch (Exception e) {
					// Don't let one department's failure abort the rest of the batch
					log.error("[" + jobName + "] Sync FAILED for orgId=" + org.getOrgid() + ", deptCode=" + deptCode, e);
				}
			}
		} finally {
			RequestContextHolder.resetRequestAttributes();
			log.info("[" + jobName + "] Cleared bound request context");
		}
		log.info("[" + jobName + "] Job finished for orgId=" + org.getOrgid());
	}

	private CitizenOnlinePendingPayment buildModel(Long orgId, Long deptId, Date fromDate, Date toDate) {
		CitizenOnlinePendingPayment model = new CitizenOnlinePendingPayment();
		ApplicationFormChallanDTO challanDTO = new ApplicationFormChallanDTO();
		model.setChallanDTO(challanDTO);
		model.setOrgId(orgId);
		model.setServiceId(0L); // TODO: 0 means "all services" (Property/Water/LOI/Application)

		model.getChallanDTO().setValidDate(fromDate);
		model.getChallanDTO().setTodate(toDate);

		model.getOfflineDTO().setDeptId(deptId);

		log.debug("Model built: orgId=" + orgId + ", deptId=" + deptId + ", validDate=" + fromDate + ", toDate=" + toDate);
		return model;
	}

	private Date toDate(LocalDateTime ldt) {
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
}