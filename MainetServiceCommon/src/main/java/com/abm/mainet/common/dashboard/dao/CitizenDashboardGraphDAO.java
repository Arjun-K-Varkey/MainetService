package com.abm.mainet.common.dashboard.dao;

import static com.abm.mainet.common.constant.PrefixConstants.MobilePreFix.GENDER;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dashboard.domain.*;
import com.abm.mainet.common.domain.FinancialYear;
import com.abm.mainet.common.integration.dms.domain.BpmsIntegrationEntity;
import com.abm.mainet.common.utility.CommonMasterUtility;
import com.abm.mainet.common.utility.LookUp;
@Repository
public class CitizenDashboardGraphDAO {

	private static final Logger log = LoggerFactory.getLogger(CitizenDashboardGraphDAO.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public List<CitizenDashboardGraphEntity> getCitizenDashboardGraphEntityList() {
		List<CitizenDashboardGraphEntity> citizenDashboardGraphEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select\r\n"
				+ "sum(coalesce(y.totalRecieved,0)) as recieved_query,\r\n"
				+ "sum(coalesce(y.Pending,0)) as Pending_query,\r\n" + "sum(coalesce(y.closed,0)) as closed_query,\r\n"
				+ "sum(coalesce(y.expired,0)) as expired_query,\r\n" + "y.FY_Year as FY_Year\r\n" + "from\r\n"
				+ "(select\r\n" + "count(tb1.QUESTION_REG_ID) totalRecieved,\r\n"
				+ "(case when tb1.STATUS in ('OPEN','REOPEN')THEN COUNT(1) END) Pending,\r\n"
				+ "-- (case when tb1.STATUS ='OPEN'|| tb1.STATUS ='REOPEN' THEN COUNT(1) END) Pending,\r\n"
				+ "(case when tb1.STATUS='CONCLUDED' THEN COUNT(1) END) closed,\r\n"
				+ "(case when tb1.DEADLINE_DATE <= curdate() THEN COUNT(1) END) expired,\r\n"
				+ "(select concat(date_format(FA_FROMDATE,'%Y'),'-',date_format(FA_TODATE,'%Y')) from tb_financialyear\r\n"
				+ "where tb1.QUESTION_DATE between FA_FROMDATE and FA_TODATE) FY_Year\r\n" + "from\r\n"
				+ "tb_lqp_query_registration tb1\r\n"
				+ "-- where COALESCE(tb1.ORGID,0)=(case when COALESCE(13,0)=0 then COALESCE(tb1.ORGID,0) else COALESCE(13,0) end)\r\n"
				+ "group by tb1.STATUS,tb1.QUESTION_REG_ID,tb1.DEADLINE_DATE,tb1.QUESTION_DATE,FY_Year) y\r\n"
				+ "group by y.totalRecieved,FY_Year )\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");

		citizenDashboardGraphEntityList = (List<CitizenDashboardGraphEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CitizenDashboardGraphEntity.class).getResultList();

		return citizenDashboardGraphEntityList;
	}

	public List<DropdownEntity> getDropdownSevenDaysEntityList() {
		List<DropdownEntity> dropdownSevenDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select date_format(x.DATE_OF_REQUEST,'%d-%m-%Y') as Years,\r\n" + " sum(Closed) as Closed,\r\n"
				+ " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n" + " sum(Rejected) as Rejected,\r\n"
				+ " count(1) as counts\r\n" + " from\r\n" + " (select \r\n" + " C.DATE_OF_REQUEST,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c\r\n" + " where c.apm_application_id=b.apm_application_id) X\r\n"
				+ " where x.DATE_OF_REQUEST between DATE_SUB(CURDATE(), INTERVAL 7 DAY) and CURDATE()\r\n"
				+ " group by date_format(x.DATE_OF_REQUEST,'%d-%m-%Y')\r\n"
				+ " order by date_format(x.DATE_OF_REQUEST,'%d-%m-%Y')  desc)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownSevenDaysEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownSevenDaysEntityList;
	}

	public List<DropdownEntity> getDropdownSixMonthsEntity() {
		List<DropdownEntity> dropdownSevenDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select date_format(x.DATE_OF_REQUEST,'%b-%Y') as Years,\r\n" + " sum(Closed) as Closed,\r\n"
				+ " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n" + " sum(Rejected) as Rejected,\r\n"
				+ " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n" + " C.DATE_OF_REQUEST,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c\r\n" + " where c.apm_application_id=b.apm_application_id) X\r\n"
				+ " group by date_format(x.DATE_OF_REQUEST,'%b-%Y'),date_format(x.DATE_OF_REQUEST,'%Y%m')  \r\n"
				+ " order by date_format(x.DATE_OF_REQUEST,'%Y%m')   DESC LIMIT 6)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownSevenDaysEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownSevenDaysEntityList;
	}

	public List<DropdownEntity> getDropdownFinancialYrEntity() {
		List<DropdownEntity> dropdownFinancialYrEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(year(fa_fromdate),'-',year(fa_todate)) Year,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c,\r\n" + " tb_financialyear a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownFinancialYrEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownFinancialYrEntityList;
	}

	public List<DropdownEntity> getDropdownHalfYearlyEntity() {
		List<DropdownEntity> dropdownHalfYearlyEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) Year,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c,\r\n" + " tb_financialhalfy a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year DESC LIMIT 2)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownHalfYearlyEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownHalfYearlyEntityList;
	}

	public List<DropdownEntity> getDropdownQuarterlyEntity() {
		List<DropdownEntity> dropdownQuarterlyEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) Year,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c,\r\n" + " tb_financialquarty a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year DESC LIMIT 4)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownQuarterlyEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownQuarterlyEntityList;
	}

	public List<TotalCollectionSWMEntity> getTotalCollectionSWMEntityList() {
		List<TotalCollectionSWMEntity> totalCollectionSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select c.O_NLS_ORGNAME,sum(a.rm_amount) as SUM_AMOUNT\r\n" + " from \r\n" + " tb_receipt_mas a,\r\n"
				+ " tb_organisation c,\r\n" + " (select Distinct RM_RCPTID from tb_receipt_det b,\r\n"
				+ " tb_tax_mas d,\r\n" + " tb_comparent_det e,\r\n" + " tb_comparent_mas f,\r\n"
				+ " tb_comparam_mas g\r\n" + " where \r\n" + " b.TAX_ID=d.TAX_ID and\r\n"
				+ " d.TAX_CATEGORY1=e.COD_ID and\r\n" + " g.cpm_id=f.CPM_ID and\r\n" + " f.COM_ID=e.COM_ID and\r\n"
				+ " g.CPM_PREFIX='TAC' and\r\n" + " e.COD_VALUE='SC') x\r\n" + " where \r\n"
				+ " a.orgid=c.orgid and\r\n" + " x.RM_RCPTID=a.RM_RCPTID and\r\n"
				+ " c.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORGID=(case when COALESCE(@x,0)=0 then COALESCE(a.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ " group by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR\r\n"
				+ " order by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		totalCollectionSWMEntityList = (List<TotalCollectionSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TotalCollectionSWMEntity.class).getResultList();
		return totalCollectionSWMEntityList;
	}

	public List<TotalServiceCntSWMEntity> getTotalServiceCntSWMEntityList() {
		List<TotalServiceCntSWMEntity> totalServiceCntSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select c.O_NLS_ORGNAME,\r\n"
				+ " c.O_NLS_ORGNAME_MAR,\r\n" + " count(a.APM_APPLICATION_ID) ServiceCount from \r\n"
				+ " tb_cfc_application_mst a,\r\n" + " tb_services_mst b,\r\n" + " tb_organisation c\r\n"
				+ " where a.SM_SERVICE_ID=b.SM_SERVICE_ID and\r\n" + " a.ORGID=b.ORGID and\r\n"
				+ " a.ORGID=c.orgid and\r\n" + " \r\n"
				+ " c.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORGID=(case when COALESCE(@x,0)=0 then COALESCE(a.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ " group by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR\r\n"
				+ " order by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		totalServiceCntSWMEntityList = (List<TotalServiceCntSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TotalServiceCntSWMEntity.class).getResultList();
		return totalServiceCntSWMEntityList;
	}

	public List<TotalTransCntSWMEntity> getTotalTransCntSWMEntityList() {
		List<TotalTransCntSWMEntity> totalTransCntSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select c.O_NLS_ORGNAME,\r\n"
				+ " c.O_NLS_ORGNAME_MAR,\r\n" + " count(a.APM_APPLICATION_ID) as COUNT_RM_RCPTNO from \r\n"
				+ " tb_cfc_application_mst a,\r\n" + " tb_services_mst b,\r\n" + " tb_organisation c\r\n"
				+ " where a.SM_SERVICE_ID=b.SM_SERVICE_ID and\r\n" + " a.ORGID=b.ORGID and\r\n"
				+ " a.ORGID=c.orgid and\r\n" + " \r\n"
				+ " c.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORGID=(case when COALESCE(@x,0)=0 then COALESCE(a.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ " group by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR\r\n"
				+ " order by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		totalTransCntSWMEntityList = (List<TotalTransCntSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TotalTransCntSWMEntity.class).getResultList();
		return totalTransCntSWMEntityList;
	}

	public List<TotalCollectionAmtSWMEntity> getTotalCollectionAmtSWMEntityList() {
		List<TotalCollectionAmtSWMEntity> totalCollectionAmtSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select c.O_NLS_ORGNAME,sum(a.rm_amount) as SUM_RM_AMT\r\n" + " from \r\n" + " tb_receipt_mas a,\r\n"
				+ " tb_organisation c,\r\n" + " (select Distinct RM_RCPTID from tb_receipt_det b,\r\n"
				+ " tb_tax_mas d,\r\n" + " tb_comparent_det e,\r\n" + " tb_comparent_mas f,\r\n"
				+ " tb_comparam_mas g\r\n" + " where \r\n" + " b.TAX_ID=d.TAX_ID and\r\n"
				+ " d.TAX_CATEGORY1=e.COD_ID and\r\n" + " g.cpm_id=f.CPM_ID and\r\n" + " f.COM_ID=e.COM_ID and\r\n"
				+ " g.CPM_PREFIX='TAC' and\r\n" + " e.COD_VALUE!='SC') x\r\n" + " where \r\n"
				+ " a.orgid=c.orgid and\r\n" + " x.RM_RCPTID=a.RM_RCPTID and\r\n"
				+ " c.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(c.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) and\r\n"
				+ " c.ORGID=(case when COALESCE(@x,0)=0 then COALESCE(a.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ " group by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR\r\n"
				+ " order by c.O_NLS_ORGNAME,c.O_NLS_ORGNAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		totalCollectionAmtSWMEntityList = (List<TotalCollectionAmtSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TotalCollectionAmtSWMEntity.class).getResultList();
		return totalCollectionAmtSWMEntityList;
	}

	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetEntityList() {
		List<VehicleTypeDryWetEntity> vehicleTypeDryWetEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=Z.ORG_CPD_ID_STATE) State,\r\n"
				+ " z.O_NLS_ORGNAME OrganisationName,z.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " z.ORG_LATITUDE,\r\n" + " z.ORG_LONGITUDE,\r\n" + " y.CPD_DESC VechicleType,\r\n"
				+ " y.CPD_DESC_MAR HVechicleType,\r\n" + " sum(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(HAZ) HAZ\r\n" + " from (SELECT f.VE_VETYPE,\r\n" + " a.ORGID, \r\n" + " SUM(case  \r\n"
				+ " when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY, \r\n"
				+ " SUM(case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,\r\n"
				+ " SUM(case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ\r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e,\r\n"
				+ " tb_sw_vehicle_mast f\r\n" + " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID\r\n"
				+ " AND a.VE_ID=f.ve_id\r\n" + " group by f.VE_VETYPE,a.ORGID) x,\r\n" + " tb_comparam_det y,\r\n"
				+ " tb_organisation z\r\n" + " where x.VE_VETYPE=y.cpd_id \r\n" + " and x.orgid=z.orgid\r\n"
				+ " group by Z.ORG_CPD_ID_STATE,z.O_NLS_ORGNAME,z.O_NLS_ORGNAME_MAR,z.ORG_CPD_ID_DIS,z.ORG_CPD_ID_DIV,z.ORG_LATITUDE,z.ORG_LONGITUDE,x.VE_VETYPE,y.CPD_DESC,y.CPD_DESC_MAR)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		vehicleTypeDryWetEntityList = (List<VehicleTypeDryWetEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), VehicleTypeDryWetEntity.class).getResultList();
		return vehicleTypeDryWetEntityList;
	}

	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetFinancialDurationWiseEntityList(int noOfDays,
			String toDate) {
		List<VehicleTypeDryWetEntity> vehicleTypeDryWetEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=Z.ORG_CPD_ID_STATE) State,\r\n"
				+ " z.O_NLS_ORGNAME OrganisationName,z.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " z.ORG_LATITUDE,\r\n" + " z.ORG_LONGITUDE,\r\n" + " y.CPD_DESC VechicleType,\r\n"
				+ " y.CPD_DESC_MAR HVechicleType,\r\n" + " sum(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(HAZ) HAZ\r\n" + " from (SELECT f.VE_VETYPE,\r\n" + " a.ORGID, \r\n" + " SUM(case  \r\n"
				+ " when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY, \r\n"
				+ " SUM(case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,\r\n"
				+ " SUM(case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ\r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e,\r\n"
				+ " tb_sw_vehicle_mast f\r\n" + " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID\r\n"
				+ " AND a.VE_ID=f.ve_id\r\n" + "AND x.CREATED_DATE BETWEEN DATE_SUB('" + toDate + "', INTERVAL "
				+ noOfDays + " day) and '" + toDate + "'" + " group by f.VE_VETYPE,a.ORGID) x,\r\n"
				+ " tb_comparam_det y,\r\n" + " tb_organisation z\r\n" + " where x.VE_VETYPE=y.cpd_id \r\n"
				+ " and x.orgid=z.orgid\r\n"
				+ " group by Z.ORG_CPD_ID_STATE,z.O_NLS_ORGNAME,z.O_NLS_ORGNAME_MAR,z.ORG_CPD_ID_DIS,z.ORG_CPD_ID_DIV,z.ORG_LATITUDE,z.ORG_LONGITUDE,x.VE_VETYPE,y.CPD_DESC,y.CPD_DESC_MAR)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		vehicleTypeDryWetEntityList = (List<VehicleTypeDryWetEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), VehicleTypeDryWetEntity.class).getResultList();
		return vehicleTypeDryWetEntityList;
	}

	public List<VehicleAndWasteTypeByDaysEntity> getVehicleAndWasteByDaysEntityList(String vehicleType,
			String wasteType, int noOfDays, int orgId, String toDate) {
		List<VehicleAndWasteTypeByDaysEntity> vehicleTypeDryWetEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ " select (select cpd_desc from tb_comparam_det where cpd_id=Z.ORG_CPD_ID_STATE) State,\r\n"
				+ " z.O_NLS_ORGNAME OrganisationName,z.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=z.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " z.ORG_LATITUDE,\r\n" + " z.ORG_LONGITUDE,\r\n" + " y.CPD_DESC VechicleType,\r\n"
				+ " y.CPD_DESC_MAR HVechicleType,\r\n" + " x.TRIP_VOLUME, x.CREATED_DATE\r\n"
				+ " from (SELECT f.VE_VETYPE,\r\n" + " a.ORGID, x.TRIP_VOLUME, x.CREATED_DATE\r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e,\r\n"
				+ " tb_sw_vehicle_mast f\r\n" + " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID\r\n"
				+ " AND a.VE_ID=f.ve_id\r\n");
		if (wasteType != null)
			queryBuilder.append(" AND e.COD_VALUE = '" + wasteType + "'\r\n");
		queryBuilder.append(" -- AND a.ORGID = " + orgId + "\r\n" + " AND x.CREATED_DATE between DATE_SUB('" + toDate
				+ "', INTERVAL " + noOfDays + " day) and '" + toDate + "') x,\r\n" + " tb_comparam_det y,\r\n"
				+ " tb_organisation z\r\n" + " where x.VE_VETYPE=y.cpd_id \r\n" + " and x.orgid=z.orgid\r\n");
		if (vehicleType != null)
			queryBuilder.append(" AND y.CPD_DESC = '" + vehicleType + "'");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		vehicleTypeDryWetEntityList = (List<VehicleAndWasteTypeByDaysEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), VehicleAndWasteTypeByDaysEntity.class).getResultList();
		return vehicleTypeDryWetEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphDryWasteEntityList() {
		List<DryWasteHazSWMEntity> graphDryWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC ,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='DW'\r\n"
				+ " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.O_NLS_ORGNAME_MAR,  y.ORG_CPD_ID_DIS,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphDryWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphDryWasteEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphDryWasteFinancialDurationWiseEntityList(int noOfDays, String toDate) {
		List<DryWasteHazSWMEntity> graphDryWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC ,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='DW'\r\n"
				+ " and a.CREATED_DATE BETWEEN DATE_SUB('" + toDate + "', INTERVAL " + noOfDays + " day) and '" + toDate
				+ "'" + " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.O_NLS_ORGNAME_MAR,  y.ORG_CPD_ID_DIS,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphDryWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphDryWasteEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphWetWasteEntityList() {
		List<DryWasteHazSWMEntity> graphWetWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='WW'\r\n"
				+ " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.ORG_CPD_ID_DIS,y.O_NLS_ORGNAME_MAR,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR  )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphWetWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphWetWasteEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphWetWasteFinancialDurationWiseEntityList(int noOfDays, String toDate) {
		List<DryWasteHazSWMEntity> graphWetWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='WW'\r\n"
				+ " and a.CREATED_DATE BETWEEN DATE_SUB('" + toDate + "', INTERVAL " + noOfDays + " day) and '" + toDate
				+ "'" + " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.ORG_CPD_ID_DIS,y.O_NLS_ORGNAME_MAR,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR  )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphWetWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphWetWasteEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphHazWasteEntityList() {
		List<DryWasteHazSWMEntity> graphHazWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='HZ'\r\n"
				+ " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.O_NLS_ORGNAME_MAR,y.ORG_CPD_ID_DIS,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR  )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphHazWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphHazWasteEntityList;
	}

	public List<DryWasteHazSWMEntity> getGraphHazWasteFinancialDurationWiseEntityList(int noOfDays, String toDate) {
		List<DryWasteHazSWMEntity> graphHazWasteEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName,y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC,\r\n" + " COD_DESC_MAR,\r\n"
				+ " sum(Dry) DryWaste\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " sum(TRIP_VOLUME) Dry\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id and\r\n" + " b.cod_value='HZ'\r\n"
				+ " and a.CREATED_DATE BETWEEN DATE_SUB('" + toDate + "', INTERVAL " + noOfDays + " day) and '" + toDate
				+ "'" + " group by a.orgid,a.COD_WAST2) x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id \r\n"
				+ " group by y.ORG_CPD_ID_STATE,y.O_NLS_ORGNAME,y.O_NLS_ORGNAME_MAR,y.ORG_CPD_ID_DIS,y.ORG_CPD_ID_DIV,y.ORG_LATITUDE,y.ORG_LONGITUDE,COD_DESC,COD_DESC_MAR  )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		graphHazWasteEntityList = (List<DryWasteHazSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DryWasteHazSWMEntity.class).getResultList();
		return graphHazWasteEntityList;
	}

	public List<DrillDownWasteTypeWiseEntity> getDrillDownDryWasteTypeWiseEntityList(String wasteType, String codDesc,
			int noOfDays, String toDate) {
		List<DrillDownWasteTypeWiseEntity> drillDownWasteTypeWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_STATE) State,\r\n"
				+ " y.O_NLS_ORGNAME OrganisationName, y.O_NLS_ORGNAME_MAR HCity,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) DistrinctName,\r\n"
				+ " (select cpd_desc from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) DivisonName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIS) HDistrinctName,\r\n"
				+ " (select cpd_desc_mar from tb_comparam_det where cpd_id=y.ORG_CPD_ID_DIV) HDivisonName,\r\n"
				+ " y.ORG_LATITUDE,\r\n" + " y.ORG_LONGITUDE,\r\n" + " COD_DESC ,\r\n" + " COD_DESC_MAR,\r\n"
				+ " x.TRIP_VOLUME, x.CREATED_DATE\r\n" + " from\r\n" + " (select a.ORGID,\r\n" + " a.COD_WAST2,\r\n"
				+ " TRIP_VOLUME, a.CREATED_DATE\r\n" + " from tb_sw_wasteseg_det a,\r\n" + " tb_comparent_det b\r\n"
				+ " where a.COD_WAST1=cod_id");
		if (wasteType != null)
			queryBuilder.append(" and\r\n b.cod_value='" + wasteType + "'\r\n");
		queryBuilder.append(" AND a.CREATED_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
				+ " day) and '" + toDate + "') x,\r\n" + " tb_organisation y,\r\n" + " tb_comparent_det z\r\n"
				+ " where x.ORGID=y.orgid and\r\n" + " x.COD_WAST2=cod_id ");
		if (codDesc != null)
			queryBuilder.append(" and\r\n COD_DESC = '" + codDesc + "'\r\n");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		drillDownWasteTypeWiseEntityList = (List<DrillDownWasteTypeWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DrillDownWasteTypeWiseEntity.class).getResultList();
		return drillDownWasteTypeWiseEntityList;
	}

	public List<FinancialYearSWMEntity> getFinancialYrSWMEntityList() {
		List<FinancialYearSWMEntity> financialYrSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " concat(year(fa_fromdate),'-',year(fa_todate)) year,\r\n" + " sum(DRY) DRY,\r\n"
				+ " SUM(WATE) WATE,\r\n" + " SUM(HAZ) HAZ\r\n" + " from\r\n" + " (SELECT a.TRIP_DATE AS TRIP_DATE,\r\n"
				+ " (case  when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY,\r\n"
				+ " (case  when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,      \r\n"
				+ " (case   when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ      \r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e\r\n"
				+ " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID) X,\r\n" + " tb_financialyear y\r\n"
				+ " where TRIP_DATE between y.FA_FROMDATE and y.FA_TODATE AND\r\n"
				+ " CURDATE() > FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 5 YEAR) < FA_FROMDATE\r\n"
				+ " group by concat(year(fa_fromdate),'-',year(fa_todate)) )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		financialYrSWMEntityList = (List<FinancialYearSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYearSWMEntity.class).getResultList();
		return financialYrSWMEntityList;
	}

	public List<FinancialYearSWMEntity> getHalfYrSWMEntityList() {
		List<FinancialYearSWMEntity> halfYrSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) year,\r\n"
				+ " sum(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n" + " SUM(HAZ) HAZ\r\n" + " from\r\n"
				+ " (SELECT a.TRIP_DATE AS TRIP_DATE,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,      \r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ      \r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e\r\n"
				+ " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID) X,\r\n"
				+ " tb_financialhalfy y\r\n" + " where TRIP_DATE between y.FA_FROMDATE and y.FA_TODATE AND\r\n"
				+ " CURDATE() > FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 2 YEAR) < FA_FROMDATE\r\n"
				+ " group by concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		halfYrSWMEntityList = (List<FinancialYearSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYearSWMEntity.class).getResultList();
		return halfYrSWMEntityList;
	}

	public List<FinancialYearSWMEntity> getLastFourQuarterSWMEntityList() {
		List<FinancialYearSWMEntity> lastFourQuarterSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) year,\r\n"
				+ " sum(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n" + " SUM(HAZ) HAZ\r\n" + " from\r\n"
				+ " (SELECT a.TRIP_DATE AS TRIP_DATE,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,      \r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ      \r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e\r\n"
				+ " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID) X,\r\n"
				+ " tb_financialquarty y\r\n" + " where TRIP_DATE between y.FA_FROMDATE and y.FA_TODATE AND\r\n"
				+ " CURDATE() > FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 4 YEAR) < FA_FROMDATE\r\n"
				+ " group by concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) )\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		lastFourQuarterSWMEntityList = (List<FinancialYearSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYearSWMEntity.class).getResultList();
		return lastFourQuarterSWMEntityList;
	}

	public List<FinancialYearSWMEntity> getLastSevenDaysSWMEntityList() {
		List<FinancialYearSWMEntity> lastSevenDaysSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " date_format(x.TRIP_DATE,'%d-%m-%Y') year,\r\n" + " sum(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(HAZ) HAZ\r\n" + " from\r\n" + " (SELECT a.TRIP_DATE AS TRIP_DATE,\r\n"
				+ " (case when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) AS DRY,\r\n"
				+ " (case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) AS WATE,\r\n"
				+ " (case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) AS HAZ\r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e\r\n"
				+ " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID) X\r\n"
				+ " where x.TRIP_DATE between DATE_SUB(CURDATE(), INTERVAL 7 DAY) and CURDATE()            \r\n"
				+ " group by date_format(x.TRIP_DATE,'%d-%m-%Y') )\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		lastSevenDaysSWMEntityList = (List<FinancialYearSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYearSWMEntity.class).getResultList();
		return lastSevenDaysSWMEntityList;
	}

	public List<MonthlySWMEntity> getMonthlySWMEntityList() {
		List<MonthlySWMEntity> monthlySWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select \r\n"
				+ " date_format(x.TRIP_DATE,'%b-%Y') as year,\r\n"
				+ " date_format(x.TRIP_DATE,'%Y%m') as DATE_STRING, \r\n" + " sum(DRY) as DRY,\r\n"
				+ " SUM(WATE) as WATE,\r\n" + " SUM(HAZ) as HAZ\r\n" + " from\r\n"
				+ " (SELECT a.TRIP_DATE AS TRIP_DATE,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY,\r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,      \r\n" + " (case  \r\n"
				+ " when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) HAZ\r\n" + " FROM\r\n"
				+ " tb_sw_tripsheet a,\r\n" + " tb_sw_tripsheet_gdet x,\r\n" + " tb_comparent_det e\r\n"
				+ " WHERE x.WAST_TYPE = e.COD_ID\r\n" + " AND x.TRIP_ID = a.TRIP_ID) X\r\n"
				+ " group by date_format(x.TRIP_DATE,'%b-%Y'),date_format(x.TRIP_DATE,'%Y%m')  \r\n"
				+ " order by date_format(x.TRIP_DATE,'%Y%m')   DESC LIMIT 6)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		monthlySWMEntityList = (List<MonthlySWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), MonthlySWMEntity.class).getResultList();
		return monthlySWMEntityList;
	}

	public List<FirstTableSWMEntity> getFirstTableSWMEntityList() {
		List<FirstTableSWMEntity> firstTableSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select\r\n"
				+ " x.ORG_CPD_ID_DIV,\r\n" + " Division,\r\n" + " DivisionReg,\r\n" + " SUM(DRY) as DRY,\r\n"
				+ " SUM(WATE) as WATE,\r\n" + " SUM(Haz) as Haz\r\n" + " from\r\n" + " (select  \r\n" + " y.orgid,\r\n"
				+ " Y.O_NLS_ORGNAME,\r\n" + " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.ORG_CPD_ID_STATE,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateReg,\r\n"
				+ " y.ORG_CPD_ID_DIV,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) Division,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) DivisionReg,\r\n"
				+ " y.ORG_CPD_ID_DIS,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictReg,\r\n"
				+ " Y.ORG_LATITUDE,\r\n" + " Y.ORG_LONGITUDE,\r\n" + " SUM(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(Haz) Haz\r\n" + " from\r\n" + " (SELECT a.ORGID,\r\n" + "       SUM(case  \r\n"
				+ "          when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY, \r\n"
				+ "        SUM(case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,\r\n"
				+ "        SUM(case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) Haz\r\n" + "        FROM\r\n"
				+ "            tb_sw_tripsheet a,\r\n" + "            tb_sw_tripsheet_gdet x,\r\n"
				+ "            tb_comparent_det e\r\n" + "        WHERE x.WAST_TYPE = e.COD_ID\r\n"
				+ "            AND x.TRIP_ID = a.TRIP_ID\r\n" + " group by a.ORGID) x,\r\n" + " tb_organisation y\r\n"
				+ " where x.orgid=y.ORGID and\r\n"
				+ " y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end)  and\r\n"
				+ " y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ " group by \r\n" + " y.ORG_CPD_ID_DIV,\r\n" + " y.ORG_CPD_ID_DIS,\r\n" + " Y.O_NLS_ORGNAME,\r\n"
				+ " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.orgid,\r\n" + " Y.ORG_LATITUDE,\r\n" + " Y.ORG_LONGITUDE) x\r\n"
				+ " group by x.ORG_CPD_ID_DIV)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		firstTableSWMEntityList = (List<FirstTableSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FirstTableSWMEntity.class).getResultList();
		return firstTableSWMEntityList;
	}

	public List<FirstTableYearwiseSWMEntity> getFirstTableYearlySWMEntityList(int orgId) {
		List<FirstTableYearwiseSWMEntity> firstTableSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select x.SW_YEAR as YEAR,\r\n" + " x.ORG_CPD_ID_DIV,\r\n" + " Division,\r\n" + " DivisionReg,\r\n"
				+ " SUM(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n" + " SUM(Haz) Haz\r\n" + " from\r\n" + " (select  \r\n"
				+ " y.orgid,\r\n" + " Y.O_NLS_ORGNAME,\r\n" + " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.ORG_CPD_ID_STATE,\r\n"
				+ " x.SW_YEAR,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateReg,\r\n"
				+ " y.ORG_CPD_ID_DIV,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) Division,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) DivisionReg,\r\n"
				+ " y.ORG_CPD_ID_DIS,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictReg,\r\n"
				+ " Y.ORG_LATITUDE,\r\n" + " Y.ORG_LONGITUDE,\r\n" + " SUM(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(Haz) Haz\r\n" + " from\r\n" + " (SELECT year(x.CREATED_DATE) as SW_YEAR,\r\n"
				+ "       SUM(case  \r\n" + "          when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY, \r\n"
				+ "        SUM(case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,\r\n"
				+ "        SUM(case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) Haz\r\n" + "        FROM\r\n"
				+ "            tb_sw_tripsheet a,\r\n" + "            tb_sw_tripsheet_gdet x,\r\n"
				+ "            tb_comparent_det e\r\n" + "        WHERE x.WAST_TYPE = e.COD_ID\r\n"
				+ "            AND x.TRIP_ID = a.TRIP_ID \r\n "
				+ "-- AND a.ORGID = " + orgId + "\r\n" 
				+ " group by SW_YEAR) x,\r\n"
				+ " tb_organisation y\r\n" + " where \r\n "
				+ "-- y.ORGID = " + orgId + " and\r\n"
				+ " y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end)  and\r\n"
				+ " y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ " group by \r\n" + " x.SW_YEAR,\r\n" + " y.ORG_CPD_ID_DIV,\r\n" + " y.ORG_CPD_ID_DIS,\r\n"
				+ " Y.O_NLS_ORGNAME,\r\n" + " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.orgid,\r\n" + " Y.ORG_LATITUDE,\r\n"
				+ " Y.ORG_LONGITUDE) x\r\n"
				+ " group by SW_YEAR, x.ORG_CPD_ID_DIV ) as t, (SELECT @row_number\\:=0) AS rn");
		firstTableSWMEntityList = (List<FirstTableYearwiseSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FirstTableYearwiseSWMEntity.class).getResultList();
		return firstTableSWMEntityList;
	}

	public List<FirstTableYearwiseSWMEntity> getFirstTableCurrYearSWMEntityList(int orgId) {
		List<FirstTableYearwiseSWMEntity> firstTableSWMEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ " select x.SW_YEAR as YEAR,\r\n" + " x.ORG_CPD_ID_DIV,\r\n" + " Division,\r\n" + " DivisionReg,\r\n"
				+ " SUM(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n" + " SUM(Haz) Haz\r\n" + " from\r\n" + " (select  \r\n"
				+ " y.orgid,\r\n" + " Y.O_NLS_ORGNAME,\r\n" + " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.ORG_CPD_ID_STATE,\r\n"
				+ " x.SW_YEAR,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_STATE) StateReg,\r\n"
				+ " y.ORG_CPD_ID_DIV,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) Division,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIV) DivisionReg,\r\n"
				+ " y.ORG_CPD_ID_DIS,\r\n"
				+ " (select CPD_DESC from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictEng,\r\n"
				+ " (select CPD_DESC_MAR from tb_comparam_det where CPD_ID=y.ORG_CPD_ID_DIS) DistrictReg,\r\n"
				+ " Y.ORG_LATITUDE,\r\n" + " Y.ORG_LONGITUDE,\r\n" + " SUM(DRY) DRY,\r\n" + " SUM(WATE) WATE,\r\n"
				+ " SUM(Haz) Haz\r\n" + " from\r\n" + " (SELECT year(x.CREATED_DATE) as SW_YEAR,\r\n"
				+ "       SUM(case  \r\n" + "          when e.COD_VALUE = 'DW' then x.TRIP_VOLUME else 0 end) DRY, \r\n"
				+ "        SUM(case when e.COD_VALUE = 'WW' then x.TRIP_VOLUME else 0 end) WATE,\r\n"
				+ "        SUM(case when e.COD_VALUE = 'HZ' then x.TRIP_VOLUME else 0 end) Haz\r\n" + "        FROM\r\n"
				+ "            tb_sw_tripsheet a,\r\n" + "            tb_sw_tripsheet_gdet x,\r\n"
				+ "            tb_comparent_det e\r\n" + "        WHERE x.WAST_TYPE = e.COD_ID\r\n"
				+ "            AND x.TRIP_ID = a.TRIP_ID \r\n"
				+ "-- AND a.ORGID = " + orgId + "\r\n"
				+ " group by SW_YEAR) x,\r\n"
				+ " tb_organisation y\r\n" + " where \r\n "
				+ "-- y.ORGID = " + orgId + " and\r\n"
				+ " y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end)  and\r\n"
				+ " y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ " group by \r\n" + " x.SW_YEAR,\r\n" + " y.ORG_CPD_ID_DIV,\r\n" + " y.ORG_CPD_ID_DIS,\r\n"
				+ " Y.O_NLS_ORGNAME,\r\n" + " Y.O_NLS_ORGNAME_MAR,\r\n" + " y.orgid,\r\n" + " Y.ORG_LATITUDE,\r\n"
				+ " Y.ORG_LONGITUDE) x\r\n" + " where year(SW_YEAR) = year(now())\r\n"
				+ " group by SW_YEAR, x.ORG_CPD_ID_DIV ) as t, (SELECT @row_number\\:=0) AS rn");
		firstTableSWMEntityList = (List<FirstTableYearwiseSWMEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), FirstTableYearwiseSWMEntity.class).getResultList();
		return firstTableSWMEntityList;
	}

	public List<DeptComplaintsEntity> getDeptComplaintsEntityList() {
		List<DeptComplaintsEntity> deptComplaintsEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select z.DP_DEPTDESC,\r\n"
				+ "        z.DP_NAME_MAR,\r\n" + "        sum(Closed) as Closed,\r\n"
				+ "        sum(Pending) as Pending,\r\n" + "        sum(Rejected) as Rejected,\r\n"
				+ "        sum(Hold) as Hold,\r\n" + "        count(1) as Received\r\n" + "        from\r\n"
				+ "        (select C.DEPT_COMP_ID,\r\n" + "        c.ORGID,\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_care_request c\r\n" + "        where c.apm_application_id=b.apm_application_id\r\n"
				+ "        ) x,\r\n" + "        tb_dept_complaint_type y,\r\n" + "        tb_department z,\r\n"
				+ "        tb_organisation u\r\n" + "        where y.DEPT_ID=X.DEPT_COMP_ID and\r\n"
				+ "        y.DEPT_ID=z.DP_DEPTID and\r\n" + "        x.orgid=u.orgid and\r\n"
				+ "        u.orgid=(case when COALESCE(@x,0)=0 then COALESCE(u.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ "        group by z.DP_DEPTDESC,z.DP_NAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsEntityList = (List<DeptComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptComplaintsEntity.class).getResultList();
		return deptComplaintsEntityList;
	}

	public List<ModeComplaintsEntity> getModeComplaintsEntityList() {
		List<ModeComplaintsEntity> modeComplaintsEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select x.REFERENCE_MODE, \r\n" + "			z.cpd_desc,\r\n" + "			z.cpd_desc_mar,\r\n"
				+ "			sum(Closed) as Closed,\r\n" + "			sum(Pending) as Pending,\r\n"
				+ "			sum(Rejected) as Rejected,\r\n" + "			sum(Hold) as Hold,\r\n"
				+ "			count(1) as Received\r\n" + "			from\r\n" + "			(select c.ORGID,\r\n"
				+ "			 c.REFERENCE_MODE,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold\r\n" + "			from tb_workflow_request b,\r\n"
				+ "			tb_care_request c\r\n" + "			where c.apm_application_id=b.apm_application_id) x,\r\n"
				+ "			tb_organisation y,\r\n" + "			tb_comparam_det Z \r\n"
				+ "			where x.orgid=y.ORGID and\r\n" + "			x.REFERENCE_MODE=z.cpd_id and\r\n"
				+ "			y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ "			group by x.REFERENCE_MODE)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsEntityList = (List<ModeComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ModeComplaintsEntity.class).getResultList();
		return modeComplaintsEntityList;
	}

	public List<DeptComplaintsEntity> getDeptComplaintsByDaysEntityList(int noOfDays) {
		List<DeptComplaintsEntity> deptComplaintsByDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select z.DP_DEPTDESC,\r\n"
				+ "        z.DP_NAME_MAR,\r\n" + "        sum(Closed) Closed,\r\n" + "        sum(Pending) Pending,\r\n"
				+ "        sum(Rejected) Rejected,\r\n" + "        sum(Hold) Hold,\r\n"
				+ "        count(1) Received\r\n" + "\r\n" + "        from\r\n" + "        (select C.DEPT_COMP_ID,\r\n"
				+ "        c.ORGID,\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_care_request c\r\n" + "        where c.apm_application_id=b.apm_application_id\r\n"
				+ "		and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now()\r\n"
				+ "        ) x,\r\n" + "  tb_department z,\r\n"
				+ "        tb_organisation u\r\n" + "        where x.DEPT_COMP_ID=z.DP_DEPTID and\r\n"
				+ "        x.orgid=u.orgid and\r\n"
				+ "        u.orgid=(case when COALESCE(@x,0)=0 then COALESCE(u.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ "        group by z.DP_DEPTDESC,z.DP_NAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysEntityList = (List<DeptComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptComplaintsEntity.class).getResultList();
		return deptComplaintsByDaysEntityList;
	}

	public List<ModeComplaintsEntity> getModeComplaintsByDaysEntityList(int noOfDays) {
		List<ModeComplaintsEntity> modeComplaintsByDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select x.REFERENCE_MODE, \r\n" + "			z.cpd_desc,\r\n" + "			z.cpd_desc_mar,\r\n"
				+ "			sum(Closed) Closed,\r\n" + "			sum(Pending) Pending,\r\n"
				+ "			sum(Rejected) Rejected,\r\n" + "			sum(Hold) Hold,\r\n"
				+ "			count(1) Received\r\n" + "			from\r\n" + "			(select c.ORGID,\r\n"
				+ "			 c.REFERENCE_MODE,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold\r\n" + "			from tb_workflow_request b,\r\n"
				+ "			tb_care_request c\r\n" + "			where c.apm_application_id=b.apm_application_id\r\n"
				+ "			and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays
				+ " day) and now()) x,\r\n" + "			tb_organisation y,\r\n" + "			tb_comparam_det Z \r\n"
				+ "			where x.orgid=y.ORGID and\r\n" + "			x.REFERENCE_MODE=z.cpd_id and\r\n"
				+ "			y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ "			group by x.REFERENCE_MODE)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsByDaysEntityList = (List<ModeComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ModeComplaintsEntity.class).getResultList();
		return modeComplaintsByDaysEntityList;
	}

	public List<SLAAnalysisEntity> getSLAAnalysisEntityList() {
		List<SLAAnalysisEntity> slaAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select sum(BeyondRejectSLA) as BeyondRejectSLA,\r\n"
				+ " sum(WithinRejectSLA) as WithinRejectSLA,\r\n" + " sum(BeyondCloseSLA) as BeyondCloseSLA,\r\n"
				+ " sum(WithinCloseSLA) as WithinCloseSLA,\r\n" + " sum(BeyondPendingSLA) as BeyondPendingSLA,\r\n"
				+ " sum(WithinPendingSLA) as WithinPendingSLA,\r\n" + " sum(BeyondSLA) as BeyondSLA,\r\n"
				+ " sum(WithinSLA) as WithinSLA\r\n" + " from\r\n" + " (select c.ORGID,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondRejectSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinRejectSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondCloseSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinCloseSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondPendingSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinPendingSLA,\r\n" + " (CASE WHEN \r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondSLA,\r\n" + " (CASE WHEN \r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinSLA\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_care_request c,\r\n" + " tb_workflow_mas a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n" + " a.WF_ID =b.WORFLOW_TYPE_ID) x,\r\n"
				+ " tb_organisation y,\r\n" + " tb_comparam_det Z \r\n" + " where x.orgid=y.ORGID and\r\n"
				+ " y.ORG_CPD_ID_DIV=z.cpd_id and\r\n"
				+ " y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end)  and\r\n"
				+ " y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end))\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		slaAnalysisEntityList = (List<SLAAnalysisEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), SLAAnalysisEntity.class).getResultList();
		return slaAnalysisEntityList;
	}

	public List<OrganizationEntity> getOrgEntityList() {
		List<OrganizationEntity> organizationEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select ORGID,O_NLS_ORGNAME,O_NLS_ORGNAME_MAR from tb_organisation a where a.ORG_STATUS='A' order by O_NLS_ORGNAME");
		organizationEntityList = (List<OrganizationEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), OrganizationEntity.class).getResultList();
		return organizationEntityList;
	}

	public List<DurationEntity> getYearlyDurationEntityList() {
		List<DurationEntity> durationEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select concat(date_format(fa_fromdate,'%Y'),'-',date_format(fa_todate,'%Y')) as Duration\r\n"
				+ "from tb_financialyear c\r\n"
				+ "where c.FA_TODATE AND CURDATE() > c.FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 5 YEAR) < c.FA_FROMDATE\r\n"
				+ "order by Duration asc)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		durationEntityList = (List<DurationEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DurationEntity.class).getResultList();
		return durationEntityList;
	}

	public List<DurationEntity> getHalfYearlyDurationEntityList() {
		List<DurationEntity> durationEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) as Duration\r\n"
				+ "from tb_financialhalfy c\r\n"
				+ "where c.FA_TODATE AND CURDATE() > c.FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 1 YEAR) < c.FA_FROMDATE\r\n"
				+ "order by Duration asc)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		durationEntityList = (List<DurationEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DurationEntity.class).getResultList();
		return durationEntityList;
	}

	public List<DurationEntity> getQuarterlyDurationEntityList() {
		List<DurationEntity> durationEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) as Duration\r\n"
				+ "from tb_financialquarty c\r\n"
				+ "where c.FA_TODATE AND CURDATE() > c.FA_FROMDATE  AND DATE_SUB(CURDATE(), INTERVAL 1 YEAR) < c.FA_FROMDATE)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		durationEntityList = (List<DurationEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DurationEntity.class).getResultList();
		return durationEntityList;
	}

	public DashboardAllCountsEntity getDashboardAllCounts() {
		DashboardAllCountsEntity dashboardAllCountsEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select count(*) from tb_rti_application) as rtiCount, (select count(*) from tb_care_request) as grievancesCount,\r\n"
				+ "(select count(*) from TB_SW_CONSTDEMO_GARBAGECOLL) as solidWasteCount, (select count(*) from tb_department where status = 'A') as deptCount,\r\n"
				+ "(select count(*) from tb_services_mst where SM_SERV_ACTIVE = 39) as serviceCount, ");
		queryBuilder.append("(select (crCnt + rtiCnt + lglCnt + swmCnt + lqpQCnt) from (\r\n" + "select \r\n"
				+ "(select count(*) from tb_care_request) as crCnt, \r\n"
				+ "(select count(*) from tb_rti_application) as rtiCnt,\r\n"
				+ "(select count(*) from tb_lgl_case_mas) as lglCnt,\r\n"
				+ "(select count(*) from TB_SW_CONSTDEMO_GARBAGECOLL) as swmCnt,\r\n"
				+ "(select count(*) from tb_lqp_query_registration) as lqpQCnt\r\n"
				+ "from dual) as cnts) as applReceivedCount,\r\n");
		queryBuilder.append("(select (crCnt + rtiCnt + lglCnt + swmCnt + lqpQCnt) from (\r\n" + "select \r\n"
				+ "(select sum(Closed) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END)  Closed\r\n" + "from tb_workflow_request b,\r\n"
				+ "tb_care_request c\r\n" + "where c.apm_application_id=b.apm_application_id) x) as crCnt, \r\n"
				+ "(select sum(Closed) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END)  Closed\r\n" + "from tb_workflow_request b,\r\n"
				+ "tb_rti_application c\r\n" + "where c.apm_application_id=b.apm_application_id) x) as rtiCnt,\r\n"
				+ "(select count(*) as CNT from tb_lgl_case_mas l,tb_lgl_hearing h,tb_comparam_det cd \r\n"
				+ "where h.cse_id=l.cse_id \r\n"
				+ "and  cd.CPD_ID = h.hr_status and cd.CPD_DESC='Concluded') as lglCnt,\r\n"
				+ "(select count(*) from TB_SW_CONSTDEMO_GARBAGECOLL sw,tb_workflow_request wf where \r\n"
				+ "sw.APM_APPLICATION_ID=wf.APM_APPLICATION_ID and wf.status='CLOSED') as swmCnt,\r\n"
				+ "(select count(*) from tb_lqp_query_registration where STATUS='CONCLUDED') as lqpQCnt\r\n"
				+ "from dual) as cnts) as applDisposedCount,\r\n");
		queryBuilder.append("(select count(*) from tb_lgl_case_mas) as legalCount,\r\n"
				+ "(select count(*) from tb_lqp_query_registration) as legisQueryCount, "
				+ "(select count(*) from tb_cmt_council_proposal_mast) as councilMgmtCount,\r\n"
				+ "(select 0) as hrmsCount, (select 0) as storeMgmtCount,\r\n"
				+ "(select 0) as propertyCount, (select 0) as waterCount,\r\n"
				+ "(select 0) as financialAccCount, (select 0) as advertiseTaxCount,\r\n"
				+ "(select 0) as landEstateCount, (select 0) as birthDeathCount,\r\n"
				+ "(select 0) as workMgmtSysCount\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		dashboardAllCountsEntity = (DashboardAllCountsEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), DashboardAllCountsEntity.class).getSingleResult();
		return dashboardAllCountsEntity;
	}

	public HRMSStoreCountEntity getDashboardHRMSCount() {
		HRMSStoreCountEntity hrmsCount = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + ""
				+ ") as t, (SELECT @row_number\\:=0) AS rn");

		try {
			hrmsCount = (HRMSStoreCountEntity) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSStoreCountEntity.class).getSingleResult();
			return hrmsCount;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public HRMSStoreCountEntity getDashboardStoreCount() {
		HRMSStoreCountEntity storeCount = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + ""
				+ ") as t, (SELECT @row_number\\:=0) AS rn");

		try {
			storeCount = (HRMSStoreCountEntity) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSStoreCountEntity.class).getSingleResult();
			return storeCount;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<DeptByDaysAndDescEntity> getDeptComplaintsByDaysAndDescEntityList(int noOfDays, String desc) {
		List<DeptByDaysAndDescEntity> deptComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select distinct c.APM_APPLICATION_ID, c.REFERENCE_MODE, (select EMPNAME from employee where EMPID = c.CREATED_BY) as CREATED_BY, c.COMPLAINT_NO, c.COMPLAINT_DESC, c.DATE_OF_REQUEST, z.DP_DEPTDESC as cpd_desc,\r\n"
				+ "			z.DP_NAME_MAR as cpd_desc_mar, c.LANDMARK as ADDRESS, c.LATITUDE, c.LONGITUDE, (select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO) as Division,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO1) as Ward,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO2) as Zone,\r\n" + "(CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_care_request c,\r\n" + "        tb_dept_complaint_type y,\r\n"
				+ "        tb_department z,\r\n" + "        tb_organisation u\r\n" + "        where \r\n"
				+ "		c.apm_application_id=b.apm_application_id and\r\n"
				+ "		b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now() and\r\n"
				+ "        z.DP_DEPTID=c.DEPT_COMP_ID and\r\n"
				+ "        c.orgid=u.orgid ");
		if (desc != null)
			queryBuilder.append(" and z.DP_DEPTDESC = '" + desc + "' \r\n");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return deptComplaintsByDaysAndDescEntityList;
	}

	public List<DeptByDaysAndDescEntity> getModeComplaintsByDaysAndDescEntityList(int noOfDays, String desc) {
		List<DeptByDaysAndDescEntity> modeComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select c.APM_APPLICATION_ID, c.REFERENCE_MODE, (select EMPNAME from employee where EMPID = c.CREATED_BY) as CREATED_BY, c.COMPLAINT_NO, c.COMPLAINT_DESC, c.DATE_OF_REQUEST, z.cpd_desc,\r\n"
				+ "			z.cpd_desc_mar, c.LANDMARK as ADDRESS, c.LATITUDE, c.LONGITUDE,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO) as Ward1,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO1) as Ward2,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO2) as Ward3, \r\n" + "(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold \r\n" + "from tb_workflow_request b,\r\n"
				+ "			tb_care_request c,\r\n" + "			tb_organisation y,\r\n"
				+ "			tb_comparam_det Z \r\n" + "			where c.orgid=y.ORGID and\r\n"
				+ "			c.REFERENCE_MODE=z.cpd_id and\r\n"
				+ "            c.apm_application_id=b.apm_application_id\r\n"
				+ "			and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays
				+ " day) and now() ");
		if (desc != null)
			queryBuilder.append("and z.cpd_desc = '" + desc + "' \r\n");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return modeComplaintsByDaysAndDescEntityList;
	}

	public List<YearWiseGrievanceGraphEntity> getYearWiseGraphEntityList() {
		List<YearWiseGrievanceGraphEntity> yrWiseTrendAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select       years as FY_Year,\r\n" + "            sum(Closed) as closed_query,\r\n"
				+ "            sum(Pending) as Pending_query,\r\n" + "            sum(Rejected) as expired_query,\r\n"
				+ "            sum(Hold) as Hold,\r\n" + "            count(1) as recieved_query from (\r\n"
				+ "select year(b.DATE_OF_REQUEST) as years,\r\n" + "(CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('HOLD') then 1 else 0 END)  Hold \r\n"
				+ "             from tb_workflow_request b,\r\n" + "             tb_care_request c\r\n"
				+ "             where c.apm_application_id=b.apm_application_id) x \r\n"
				+ "             group by x.years ) as t, (SELECT @row_number\\:=0) AS rn");
		yrWiseTrendAnalysisEntityList = (List<YearWiseGrievanceGraphEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), YearWiseGrievanceGraphEntity.class).getResultList();
		return yrWiseTrendAnalysisEntityList;
	}

	/*
	 * RTI Module APIs
	 */

	public List<SLAAnalysisEntity> getSLAAnalysisEntityRTIList() {
		List<SLAAnalysisEntity> slaAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select sum(BeyondRejectSLA) as BeyondRejectSLA,\r\n"
				+ " sum(WithinRejectSLA) as WithinRejectSLA,\r\n" + " sum(BeyondCloseSLA) as BeyondCloseSLA,\r\n"
				+ " sum(WithinCloseSLA) as WithinCloseSLA,\r\n" + " sum(BeyondPendingSLA) as BeyondPendingSLA,\r\n"
				+ " sum(WithinPendingSLA) as WithinPendingSLA,\r\n" + " sum(BeyondSLA) as BeyondSLA,\r\n"
				+ " sum(WithinSLA) as WithinSLA\r\n" + " from\r\n" + " (select c.ORGID,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondRejectSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinRejectSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondCloseSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinCloseSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondPendingSLA,\r\n" + " (CASE WHEN \r\n"
				+ " ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) and\r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinPendingSLA,\r\n" + " (CASE WHEN \r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) >= \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID =a.WF_ID)/ 1000) <> 0) \r\n"
				+ " THEN\r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE WF_ID = a.WF_ID) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID=a.SM_SERVICE_ID) \r\n"
				+ " END)) then 1 else 0 END)  BeyondSLA,\r\n" + " (CASE WHEN \r\n"
				+ " (TIMESTAMPDIFF(SECOND,b.DATE_OF_REQUEST,NOW()) < \r\n"
				+ " (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det\r\n"
				+ " WHERE (WF_ID = a.WF_ID)) / 1000) <> 0) \r\n" + " THEN \r\n"
				+ " ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID =a.WF_ID)) / 1000) \r\n"
				+ " ELSE \r\n"
				+ " (SELECT (SM_SERDUR/ 1000) FROM tb_services_mst WHERE SM_SERVICE_ID =a.SM_SERVICE_ID) \r\n"
				+ " END)) \r\n" + " then 1 else 0 END)  WithinSLA\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c,\r\n" + " tb_workflow_mas a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n" + " a.WF_ID =b.WORFLOW_TYPE_ID) x,\r\n"
				+ " tb_organisation y,\r\n" + " tb_comparam_det Z \r\n" + " where x.orgid=y.ORGID and\r\n"
				+ " y.ORG_CPD_ID_DIV=z.cpd_id and\r\n"
				+ " y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end)  and\r\n"
				+ " y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ " y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end))\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		slaAnalysisEntityList = (List<SLAAnalysisEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), SLAAnalysisEntity.class).getResultList();
		return slaAnalysisEntityList;
	}

	public List<DropdownEntity> getDropdownSevenDaysRTIEntityList() {
		List<DropdownEntity> dropdownSevenDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select date_format(x.APM_APPLICATION_DATE,'%d-%m-%Y') as Years,\r\n" + " sum(Closed) as Closed,\r\n"
				+ " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n" + " sum(Rejected) as Rejected,\r\n"
				+ " count(1) as counts\r\n" + " from\r\n" + " (select \r\n" + " C.APM_APPLICATION_DATE,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c\r\n" + " where c.apm_application_id=b.apm_application_id) X\r\n"
				+ " where x.APM_APPLICATION_DATE between DATE_SUB(CURDATE(), INTERVAL 7 DAY) and CURDATE()\r\n"
				+ " group by date_format(x.APM_APPLICATION_DATE,'%d-%m-%Y')\r\n"
				+ " order by date_format(x.APM_APPLICATION_DATE,'%d-%m-%Y')  desc)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownSevenDaysEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownSevenDaysEntityList;
	}

	public List<DropdownEntity> getDropdownSixMonthsRTIEntity() {
		List<DropdownEntity> dropdownSevenDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select date_format(x.APM_APPLICATION_DATE,'%b-%Y') as Years,\r\n" + " sum(Closed) as Closed,\r\n"
				+ " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n" + " sum(Rejected) as Rejected,\r\n"
				+ " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n" + " C.APM_APPLICATION_DATE,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c\r\n" + " where c.apm_application_id=b.apm_application_id) X\r\n"
				+ " group by date_format(x.APM_APPLICATION_DATE,'%b-%Y'),date_format(x.APM_APPLICATION_DATE,'%Y%m')  \r\n"
				+ " order by date_format(x.APM_APPLICATION_DATE,'%Y%m')   DESC LIMIT 6)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownSevenDaysEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownSevenDaysEntityList;
	}

	public List<DropdownEntity> getDropdownFinancialYrRTIEntity() {
		List<DropdownEntity> dropdownFinancialYrEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(year(fa_fromdate),'-',year(fa_todate)) Year,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c,\r\n" + " tb_financialyear a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownFinancialYrEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownFinancialYrEntityList;
	}

	public List<DropdownEntity> getDropdownHalfYearlyRTIEntity() {
		List<DropdownEntity> dropdownHalfYearlyEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) Year,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c,\r\n" + " tb_financialhalfy a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year DESC LIMIT 2)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownHalfYearlyEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownHalfYearlyEntityList;
	}

	public List<DropdownEntity> getDropdownQuarterlyRTIEntity() {
		List<DropdownEntity> dropdownQuarterlyEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select year as Years,\r\n"
				+ " sum(Closed) as Closed,\r\n" + " sum(Pending) as Pending,\r\n" + " sum(Hold) as Hold,\r\n"
				+ " sum(Rejected) as Rejected,\r\n" + " count(1) as Counts\r\n" + " from\r\n" + " (select \r\n"
				+ " concat(date_format(fa_fromdate,'%b%Y'),'-',date_format(fa_todate,'%b%Y')) Year,\r\n"
				+ " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + " (CASE WHEN \r\n"
				+ " (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ " WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ " IN ('HOLD') then 1 else 0 END)  Hold\r\n" + " from tb_workflow_request b,\r\n"
				+ " tb_rti_application c,\r\n" + " tb_financialquarty a\r\n"
				+ " where c.apm_application_id=b.apm_application_id and\r\n"
				+ " b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b\r\n" + " group by year DESC LIMIT 4)\r\n"
				+ " as t, (SELECT @row_number\\:=0) AS rn");
		dropdownQuarterlyEntityList = (List<DropdownEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DropdownEntity.class).getResultList();
		return dropdownQuarterlyEntityList;
	}

	public List<DeptComplaintsEntity> getDeptComplaintsRTIEntityList() {
		List<DeptComplaintsEntity> deptComplaintsEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select z.DP_DEPTDESC,\r\n"
				+ "        z.DP_NAME_MAR,\r\n" + "        sum(Closed) as Closed,\r\n"
				+ "        sum(Pending) as Pending,\r\n" + "        sum(Rejected) as Rejected,\r\n"
				+ "        sum(Hold) as Hold,\r\n" + "        count(1) as Received\r\n" + "        from\r\n"
				+ "        (select C.RTI_DEPTID,\r\n" + "        c.ORGID,\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_rti_application c\r\n" + "        where c.apm_application_id=b.apm_application_id\r\n"
				+ "        ) x,\r\n" + "        tb_dept_complaint_type y,\r\n" + "        tb_department z,\r\n"
				+ "        tb_organisation u\r\n" + "        where y.DEPT_ID=X.RTI_DEPTID and\r\n"
				+ "        y.DEPT_ID=z.DP_DEPTID and\r\n" + "        x.orgid=u.orgid and\r\n"
				+ "        u.orgid=(case when COALESCE(@x,0)=0 then COALESCE(u.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ "        group by z.DP_DEPTDESC,z.DP_NAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsEntityList = (List<DeptComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptComplaintsEntity.class).getResultList();
		return deptComplaintsEntityList;
	}

	public List<ModeComplaintsEntity> getModeComplaintsRTIEntityList() {
		List<ModeComplaintsEntity> modeComplaintsEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select x.APPL_REFERENCE_MODE as REFERENCE_MODE, \r\n" + "			z.cpd_desc,\r\n"
				+ "			z.cpd_desc_mar,\r\n" + "			sum(Closed) as Closed,\r\n"
				+ "			sum(Pending) as Pending,\r\n" + "			sum(Rejected) as Rejected,\r\n"
				+ "			sum(Hold) as Hold,\r\n" + "			count(1) as Received\r\n" + "			from\r\n"
				+ "			(select c.ORGID,\r\n" + "			 c.APPL_REFERENCE_MODE,\r\n"
				+ "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold\r\n" + "			from tb_workflow_request b,\r\n"
				+ "			tb_rti_application c\r\n"
				+ "			where c.apm_application_id=b.apm_application_id) x,\r\n"
				+ "			tb_organisation y,\r\n" + "			tb_comparam_det Z \r\n"
				+ "			where x.orgid=y.ORGID and\r\n" + "			x.APPL_REFERENCE_MODE=z.cpd_id and\r\n"
				+ "			y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ "			group by x.APPL_REFERENCE_MODE)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsEntityList = (List<ModeComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ModeComplaintsEntity.class).getResultList();
		return modeComplaintsEntityList;
	}

	public List<DeptComplaintsEntity> getDeptComplaintsByDaysRTIEntityList(int noOfDays) {
		List<DeptComplaintsEntity> deptComplaintsByDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select z.DP_DEPTDESC,\r\n"
				+ "        z.DP_NAME_MAR,\r\n" + "        sum(Closed) Closed,\r\n" + "        sum(Pending) Pending,\r\n"
				+ "        sum(Rejected) Rejected,\r\n" + "        sum(Hold) Hold,\r\n"
				+ "        count(1) Received\r\n" + "\r\n" + "        from\r\n" + "        (select C.RTI_DEPTID,\r\n"
				+ "        c.ORGID,\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_rti_application c\r\n" + "        where c.apm_application_id=b.apm_application_id\r\n"
				+ "		and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now()\r\n"
				+ "        ) x,\r\n" + "        tb_dept_complaint_type y,\r\n" + "        tb_department z,\r\n"
				+ "        tb_organisation u\r\n" + "        where y.DEPT_ID=X.RTI_DEPTID and\r\n"
				+ "        y.DEPT_ID=z.DP_DEPTID and\r\n" + "        x.orgid=u.orgid and\r\n"
				+ "        u.orgid=(case when COALESCE(@x,0)=0 then COALESCE(u.orgid,0) else COALESCE(@x,0) end)\r\n"
				+ "        group by z.DP_DEPTDESC,z.DP_NAME_MAR)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysEntityList = (List<DeptComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptComplaintsEntity.class).getResultList();
		return deptComplaintsByDaysEntityList;
	}

	public List<ModeComplaintsEntity> getModeComplaintsByDaysRTIEntityList(int noOfDays) {
		List<ModeComplaintsEntity> modeComplaintsByDaysEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select x.APPL_REFERENCE_MODE as REFERENCE_MODE, \r\n" + "	z.cpd_desc,\r\n"
				+ "			z.cpd_desc_mar,\r\n" + "	sum(Closed) Closed,\r\n"
				+ "			sum(Pending) Pending,\r\n" + "		sum(Rejected) Rejected,\r\n"
				+ "			sum(Hold) Hold,\r\n" + "		count(1) Received\r\n" + "			from\r\n"
				+ "			(select c.ORGID,\r\n" + "	c.APPL_REFERENCE_MODE,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND (`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND (`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold\r\n" + "			from tb_workflow_request b,\r\n"
				+ "			tb_rti_application c\r\n" + "			where c.apm_application_id=b.apm_application_id\r\n"
				+ "			and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays
				+ " day) and now()) x,\r\n" + "			tb_organisation y,\r\n" + "			tb_comparam_det Z \r\n"
				+ "			where x.orgid=y.ORGID and\r\n" + "			x.APPL_REFERENCE_MODE=z.cpd_id and\r\n"
				+ "			y.orgid=(case when COALESCE(@x,0)=0 then COALESCE(y.orgid,0) else COALESCE(@x,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_STATE=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_STATE,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIV=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIV,0) else COALESCE(0,0) end) and\r\n"
				+ "			y.ORG_CPD_ID_DIS=(case when COALESCE(0,0)=0 then COALESCE(y.ORG_CPD_ID_DIS,0) else COALESCE(0,0) end) \r\n"
				+ "			group by x.APPL_REFERENCE_MODE)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsByDaysEntityList = (List<ModeComplaintsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ModeComplaintsEntity.class).getResultList();
		return modeComplaintsByDaysEntityList;
	}

	public List<DeptByDaysAndDescEntity> getDeptComplaintsByDaysAndDescRTIEntityList(int noOfDays, String desc) {
		List<DeptByDaysAndDescEntity> deptComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select c.APM_APPLICATION_ID, c.APPL_REFERENCE_MODE as REFERENCE_MODE, (select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY, c.RTI_NO as COMPLAINT_NO, c.RTI_SUBJECT as COMPLAINT_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST, z.DP_DEPTDESC as cpd_desc,\r\n"
				+ "			z.DP_NAME_MAR as cpd_desc_mar, \r\n" + "(CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_rti_application c,\r\n" + "        tb_dept_complaint_type y,\r\n"
				+ "        tb_department z,\r\n" + "        tb_organisation u\r\n" + "        where \r\n"
				+ "		c.apm_application_id=b.apm_application_id and\r\n"
				+ "		b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now() and\r\n"
				+ "        y.DEPT_ID=c.RTI_DEPTID and\r\n" + "        y.DEPT_ID=z.DP_DEPTID and\r\n"
				+ "        c.orgid=u.orgid");
		if (desc != null)
			queryBuilder.append(" and z.DP_DEPTDESC = '" + desc + "' \r\n");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return deptComplaintsByDaysAndDescEntityList;
	}

	public List<DeptByDaysAndDescEntity> getModeComplaintsByDaysAndDescRTIEntityList(int noOfDays, String desc) {
		List<DeptByDaysAndDescEntity> modeComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select c.APM_APPLICATION_ID, c.APPL_REFERENCE_MODE as REFERENCE_MODE, \r\n"
				+ "(select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY, \r\n"
				+ "c.RTI_NO as COMPLAINT_NO, c.RTI_SUBJECT as COMPLAINT_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST, z.cpd_desc,\r\n"
				+ "			z.cpd_desc_mar, \r\n" + "(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "			(CASE WHEN \r\n"
				+ "			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) \r\n"
				+ "			IN ('HOLD') then 1 else 0 END)  Hold \r\n" + "from tb_workflow_request b,\r\n"
				+ "			tb_rti_application c,\r\n" + "			tb_organisation y,\r\n"
				+ "			tb_comparam_det Z \r\n" + "			where c.orgid=y.ORGID and\r\n"
				+ "			c.APPL_REFERENCE_MODE=z.cpd_id and\r\n"
				+ "            c.apm_application_id=b.apm_application_id\r\n"
				+ "			and b.date_of_request between DATE_SUB(CURDATE(), INTERVAL " + noOfDays
				+ " day) and now() ");
		if (desc != null)
			queryBuilder.append("and z.cpd_desc = '" + desc + "' \r\n");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		modeComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return modeComplaintsByDaysAndDescEntityList;
	}

	public List<YearWiseGrievanceGraphEntity> getYearWiseGraphRTIEntityList() {
		List<YearWiseGrievanceGraphEntity> yrWiseTrendAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select       years as FY_Year,\r\n" + "    sum(Closed) as closed_query,\r\n"
				+ "            sum(Pending) as Pending_query,\r\n" + "            sum(Rejected) as expired_query,\r\n"
				+ "            sum(Hold) as Hold,\r\n" + "            count(1) as recieved_query from (\r\n"
				+ "select year(b.DATE_OF_REQUEST) as years,\r\n" + "(CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('CLOSED') then 1 else 0 END)  Closed,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('PENDING') then 1 else 0 END)  Pending,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('REJECTED') then 1 else 0 END)  Rejected,\r\n" + "            (CASE WHEN\r\n"
				+ "             (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "                   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "            IN ('HOLD') then 1 else 0 END)  Hold \r\n"
				+ "             from tb_workflow_request b,\r\n" + "  tb_rti_application c\r\n"
				+ "             where c.apm_application_id=b.apm_application_id) x \r\n"
				+ "             group by x.years ) as t, (SELECT @row_number\\:=0) AS rn");
		yrWiseTrendAnalysisEntityList = (List<YearWiseGrievanceGraphEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), YearWiseGrievanceGraphEntity.class).getResultList();
		return yrWiseTrendAnalysisEntityList;
	}

	public RTIApplicationCountEntity getRTIFirstAppealCount() {
		RTIApplicationCountEntity rtiApplicationCountEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (SELECT count(*) as COUNT FROM tb_objection_mast tom,tb_rti_application tra where tom.OBJ_STATUS ='"
						+ MainetConstants.TASK_STATUS_PENDING
						+ "' and tom.APM_APPLICATION_ID=tra.APM_APPLICATION_ID) as t, (SELECT @row_number\\:=0) AS rn");
		rtiApplicationCountEntity = (RTIApplicationCountEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationCountEntity.class).getSingleResult();
		return rtiApplicationCountEntity;
	}

	public RTIApplicationCountEntity getRTISecondAppealCount() {
		RTIApplicationCountEntity rtiApplicationCountEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (SELECT count(*) as COUNT FROM tb_objection_mast tom,tb_rti_application tra where tom.OBJ_STATUS ='"
						+ MainetConstants.TASK_STATUS_COMPLETED
						+ "' and tom.APM_APPLICATION_ID=tra.APM_APPLICATION_ID) as t, (SELECT @row_number\\:=0) AS rn");
		rtiApplicationCountEntity = (RTIApplicationCountEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationCountEntity.class).getSingleResult();
		return rtiApplicationCountEntity;
	}

	public RTIApplicationCountEntity getRTIOpenFirstAndSecondAppealCount() {
		RTIApplicationCountEntity rtiApplicationCountEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (SELECT count(*) as COUNT FROM tb_objection_mast tom,tb_rti_application tra where tom.OBJ_STATUS in ('"
						+ MainetConstants.TASK_STATUS_COMPLETED + "','" + MainetConstants.TASK_STATUS_PENDING
						+ "') and tom.APM_APPLICATION_ID=tra.APM_APPLICATION_ID) as t, (SELECT @row_number\\:=0) AS rn");
		rtiApplicationCountEntity = (RTIApplicationCountEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationCountEntity.class).getSingleResult();
		return rtiApplicationCountEntity;
	}

	public RTIApplicationCountEntity getRTIClosedFirstAndSecondAppealCount() {
		RTIApplicationCountEntity rtiApplicationCountEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (SELECT count(*) as COUNT FROM tb_objection_mast tom,tb_rti_application tra where tom.OBJ_STATUS in ('"
						+ MainetConstants.TASK_STATUS_COMPLETED + "','"
						+ MainetConstants.RTISERVICE.TASK_STATUS_SCNDAPPL_COMP
						+ "') and  tom.APM_APPLICATION_ID=tra.APM_APPLICATION_ID) as t, (SELECT @row_number\\:=0) AS rn");
		rtiApplicationCountEntity = (RTIApplicationCountEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationCountEntity.class).getSingleResult();
		return rtiApplicationCountEntity;
	}

	public RTIApplicationCountEntity getTotalReceivedRTIApplications() {
		RTIApplicationCountEntity rtiApplicationCountEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (SELECT count(*) as COUNT FROM tb_rti_application) as t, (SELECT @row_number\\:=0) AS rn");
		rtiApplicationCountEntity = (RTIApplicationCountEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationCountEntity.class).getSingleResult();
		return rtiApplicationCountEntity;
	}

	/*
	 * Legal Module APIs
	 */

	public List<LegalCountsEntity> getLegalReceivedYrWiseEntityList() {
		List<LegalCountsEntity> legalCountEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, year(created_date) as YR from tb_lgl_case_mas\r\n"
				+ "group by year(created_date) ) as t, (SELECT @row_number\\:=0) AS rn");
		legalCountEntityList = (List<LegalCountsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), LegalCountsEntity.class).getResultList();
		return legalCountEntityList;
	}

	public List<LegalCountsEntity> getLegalClosedYrWisetEntityList() {
		List<LegalCountsEntity> legalCountEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, year(l.created_date) as YR from tb_lgl_case_mas l,tb_lgl_hearing h,tb_comparam_det cd \r\n"
				+ "where h.cse_id=l.cse_id \r\n" + "and  cd.CPD_ID = h.hr_status and cd.CPD_DESC='Concluded'\r\n"
				+ "group by year(l.created_date) ) as t, (SELECT @row_number\\:=0) AS rn");
		entityManager.clear();
		legalCountEntityList = (List<LegalCountsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), LegalCountsEntity.class).getResultList();
		return legalCountEntityList;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseReceivedLegalCount() {
		List<DeptAndCourtWiseLegalCntEntity> deptAndCourtWiseLegalCntEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, d.DP_DEPTCODE as NAME from tb_lgl_case_mas l, tb_department d\r\n"
				+ "where  l.cse_deptid= d.DP_DEPTID\r\n"
				+ "and l.created_date between DATE_SUB(CURDATE(), INTERVAL 365 day) and now()\r\n"
				+ "group by d.DP_DEPTCODE ) as t, (SELECT @row_number\\:=0) AS rn");
		deptAndCourtWiseLegalCntEntityList = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptAndCourtWiseLegalCntEntityList;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseReceivedLegalCount() {
		List<DeptAndCourtWiseLegalCntEntity> deptAndCourtWiseLegalCntEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, ct.CRT_NAME as NAME from tb_lgl_case_mas cs,tb_lgl_court_mast ct \r\n"
				+ "where cs.crt_id = ct.crt_id\r\n"
				+ "and cs.created_date between DATE_SUB(CURDATE(), INTERVAL 365 day) and now()\r\n"
				+ "group by ct.CRT_NAME ) as t, (SELECT @row_number\\:=0) AS rn");
		deptAndCourtWiseLegalCntEntityList = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptAndCourtWiseLegalCntEntityList;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseReceivedLegalCount(int noOfDays) {
		List<DeptAndCourtWiseLegalCntEntity> deptAndCourtWiseLegalCntEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, d.DP_DEPTCODE as NAME from tb_lgl_case_mas l, tb_department d\r\n"
				+ "where  l.cse_deptid= d.DP_DEPTID\r\n" + "and l.created_date between DATE_SUB(CURDATE(), INTERVAL "
				+ noOfDays + " day) and now()\r\n" + "group by d.DP_DEPTCODE ) as t, (SELECT @row_number\\:=0) AS rn");
		deptAndCourtWiseLegalCntEntityList = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptAndCourtWiseLegalCntEntityList;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseReceivedLegalCount(int noOfDays) {
		List<DeptAndCourtWiseLegalCntEntity> deptAndCourtWiseLegalCntEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select count(*) as CNT, ct.CRT_NAME as NAME from tb_lgl_case_mas cs,tb_lgl_court_mast ct \r\n"
				+ "where cs.crt_id = ct.crt_id\r\n" + "and cs.created_date between DATE_SUB(CURDATE(), INTERVAL "
				+ noOfDays + " day) and now()\r\n" + "group by ct.CRT_NAME ) as t, (SELECT @row_number\\:=0) AS rn");
		deptAndCourtWiseLegalCntEntityList = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptAndCourtWiseLegalCntEntityList;
	}

	public List<DeptAndCourtWiseAllLegalDataEntity> getDeptWiseAllLegalData(String deptName, int noOfDays) {
		List<DeptAndCourtWiseAllLegalDataEntity> deptWiseAllLegalDataEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select COD_DESC from tb_comparent_det where COD_ID=l.cse_cat_id) as cse_category,\r\n"
				+ "l.cse_name as cse_name,l.cse_suit_no as cse_suit_no,l.cse_date as cse_date,\r\n"
				+ "l.cse_matdet_1 as cse_matdet_1,\r\n" + "d.DP_DEPTDESC as cse_dept,\r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.CPD_ID=l.cse_case_status_id) as cse_status,\r\n"
				+ "(select pd.csed_name from tb_lgl_case_pddetails pd where pd.cse_id= l.cse_id and pd.csed_flag = 'P') as case_filed_by,\r\n"
				+ "(select pd.csed_name from tb_lgl_case_pddetails pd where pd.cse_id= l.cse_id and pd.csed_flag = 'D') as case_filed_on,\r\n"
				+ "ct.CRT_NAME as crt_name\r\n" + "from tb_lgl_case_mas l, tb_department d, tb_lgl_court_mast ct \r\n"
				+ "where l.cse_deptid= d.DP_DEPTID AND\r\n" + "l.crt_id = ct.crt_id\r\n"
				+ "AND l.created_date between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now()\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTCODE = '" + deptName + "'");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseAllLegalDataEntityList = (List<DeptAndCourtWiseAllLegalDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseAllLegalDataEntity.class).getResultList();
		return deptWiseAllLegalDataEntityList;
	}

	public List<DeptAndCourtWiseAllLegalDataEntity> getCourtWiseAllLegalData(String courtName, int noOfDays) {
		List<DeptAndCourtWiseAllLegalDataEntity> deptCourtAllLegalDataEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select COD_DESC from tb_comparent_det where COD_ID=l.cse_cat_id) as cse_category,\r\n"
				+ "l.cse_name as cse_name,l.cse_suit_no as cse_suit_no,l.cse_date as cse_date,\r\n"
				+ "l.cse_matdet_1 as cse_matdet_1,\r\n" + "d.DP_DEPTDESC as cse_dept,\r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.CPD_ID=l.cse_case_status_id) as cse_status,\r\n"
				+ "(select pd.csed_name from tb_lgl_case_pddetails pd where pd.cse_id= l.cse_id and pd.csed_flag = 'P') as case_filed_by,\r\n"
				+ "(select pd.csed_name from tb_lgl_case_pddetails pd where pd.cse_id= l.cse_id and pd.csed_flag = 'D') as case_filed_on,\r\n"
				+ "ct.CRT_NAME as crt_name\r\n" + "from tb_lgl_case_mas l, tb_department d, tb_lgl_court_mast ct \r\n"
				+ "where l.cse_deptid= d.DP_DEPTID AND\r\n" + "l.crt_id = ct.crt_id\r\n"
				+ "AND l.created_date between DATE_SUB(CURDATE(), INTERVAL " + noOfDays + " day) and now()\r\n");
		if (courtName != null)
			queryBuilder.append("and ct.CRT_NAME = '" + courtName + "'");
		queryBuilder.append(" ) as t, (SELECT @row_number\\:=0) AS rn");
		deptCourtAllLegalDataEntityList = (List<DeptAndCourtWiseAllLegalDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseAllLegalDataEntity.class).getResultList();
		return deptCourtAllLegalDataEntityList;
	}

	/*
	 * Legislative Query Management module APIs
	 */

	public List<YearwiseTrendAnalysisEntity> getYrWiseTrendAnalysisEntityList() {
		List<YearwiseTrendAnalysisEntity> yrWiseTrendAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select\r\n"
				+ "sum(coalesce(y.totalRecieved,0)) as recieved_query,\r\n"
				+ "sum(coalesce(y.Pending,0)) as Pending_query,\r\n" + "sum(coalesce(y.closed,0)) as closed_query,\r\n"
				+ "sum(coalesce(y.expired,0)) as expired_query,\r\n" + "y.FY_Year\r\n" + "from\r\n" + "(select\r\n"
				+ "count(tb1.QUESTION_REG_ID) totalRecieved,\r\n"
				+ "(case when tb1.STATUS in ('OPEN','REOPEN')THEN COUNT(1) END) Pending,\r\n"
				+ "-- (case when tb1.STATUS ='OPEN'|| tb1.STATUS ='REOPEN' THEN COUNT(1) END) Pending,\r\n"
				+ "(case when tb1.STATUS='CONCLUDED' THEN COUNT(1) END) closed,\r\n"
				+ "(case when tb1.DEADLINE_DATE <= curdate() THEN COUNT(1) END) expired,\r\n"
				+ "(select concat(date_format(FA_FROMDATE,'%Y'),'-',date_format(FA_TODATE,'%Y')) from tb_financialyear\r\n"
				+ "where tb1.QUESTION_DATE between FA_FROMDATE and FA_TODATE) FY_Year\r\n" + "from\r\n"
				+ "tb_lqp_query_registration tb1\r\n"
				+ "-- where COALESCE(tb1.ORGID,0)=(case when COALESCE(13,0)=0 then COALESCE(tb1.ORGID,0) else COALESCE(13,0) end)\r\n"
				+ "group by tb1.STATUS,tb1.QUESTION_REG_ID,tb1.DEADLINE_DATE,tb1.QUESTION_DATE,FY_Year) y\r\n"
				+ "group by y.totalRecieved,FY_Year)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		yrWiseTrendAnalysisEntityList = (List<YearwiseTrendAnalysisEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), YearwiseTrendAnalysisEntity.class).getResultList();
		return yrWiseTrendAnalysisEntityList;
	}

	public List<DeptWiseTrendAnalysisEntity> getDeptWiseTrendAnalysisEntityList() {
		List<DeptWiseTrendAnalysisEntity> deptWiseTrendAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "Select\r\n"
				+ "count(QUESTION_REG_ID) as totalRecieved,\r\n"
				+ "(select d.DP_DEPTDESC from tb_department d where d.DP_DEPTID=tb1.DEPT_ID) as DepartmentName\r\n"
				+ "from\r\n" + "tb_lqp_query_registration tb1\r\n"
				+ "-- where COALESCE(tb1.ORGID,0)=(case when COALESCE(13,0)=0 then COALESCE(tb1.ORGID,0) else COALESCE(13,0) end)\r\n"
				+ "group by DepartmentName)\r\n" + " as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseTrendAnalysisEntityList = (List<DeptWiseTrendAnalysisEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptWiseTrendAnalysisEntity.class).getResultList();
		return deptWiseTrendAnalysisEntityList;
	}

	public List<YearwiseTrendAnalysisEntity> getCurrYrTrendAnalysisEntityList() {
		List<YearwiseTrendAnalysisEntity> yrWiseTrendAnalysisEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "select\r\n"
				+ "sum(coalesce(y.totalRecieved,0)) recieved_query,\r\n"
				+ "sum(coalesce(y.Pending,0)) Pending_query,\r\n" + "sum(coalesce(y.closed,0)) closed_query,\r\n"
				+ "sum(coalesce(y.expired,0)) expired_query,\r\n" + "y.FY_Year\r\n" + "from\r\n" + "(select\r\n"
				+ "count(tb1.QUESTION_REG_ID) totalRecieved,\r\n"
				+ "(case when tb1.STATUS in ('OPEN','REOPEN')THEN COUNT(1) END) Pending,\r\n"
				+ "-- (case when tb1.STATUS ='OPEN'|| tb1.STATUS ='REOPEN' THEN COUNT(1) END) Pending,\r\n"
				+ "(case when tb1.STATUS='CONCLUDED' THEN COUNT(1) END) closed,\r\n"
				+ "(case when tb1.DEADLINE_DATE <= curdate() THEN COUNT(1) END) expired,\r\n"
				+ "(select concat(date_format(FA_FROMDATE,'%Y'),'-',date_format(FA_TODATE,'%Y')) from tb_financialyear\r\n"
				+ "where FA_YEARID = (select max(FA_YEARID) from tb_financialyear) and\r\n"
				+ "tb1.QUESTION_DATE between FA_FROMDATE and FA_TODATE) FY_Year\r\n" + "from\r\n"
				+ "tb_lqp_query_registration tb1\r\n"
				+ "-- where COALESCE(tb1.ORGID,0)=(case when COALESCE(13,0)=0 then COALESCE(tb1.ORGID,0) else COALESCE(13,0) end)\r\n"
				+ "group by tb1.STATUS,tb1.QUESTION_REG_ID,tb1.DEADLINE_DATE,tb1.QUESTION_DATE,FY_Year) y\r\n"
				+ "group by y.totalRecieved,FY_Year ) as t, (SELECT @row_number\\:=0) AS rn");
		yrWiseTrendAnalysisEntityList = (List<YearwiseTrendAnalysisEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), YearwiseTrendAnalysisEntity.class).getResultList();
		return yrWiseTrendAnalysisEntityList;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPQuestionCount(String deptName, int noOfDays,
			String toDate) {
		List<DeptAndCourtWiseLegalCntEntity> deptWiseLQPQuestionCounts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select d.DP_DEPTDESC as NAME, count(*) as CNT from tb_lqp_query_registration lq, tb_department d\r\n"
				+ "where d.DP_DEPTID=lq.DEPT_ID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		queryBuilder.append("and QUESTION_DATE BETWEEN \r\n" + "DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
				+ " day) and '" + toDate + "'\r\n");
		if (deptName == null)
			queryBuilder.append("group by d.DP_DEPTDESC");
		queryBuilder.append(") as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseLQPQuestionCounts = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptWiseLQPQuestionCounts;
	}

	public List<DeptWiseQuestionDataLQPEntity> getDeptWiseLQPQuestionData(String deptName, int noOfDays,
			String toDate) {
		List<DeptWiseQuestionDataLQPEntity> deptWiseLQPQuestionData = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select lq.question_id, lq.QUESTION_DATE, lq.INWARD_NO, \r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.CPD_ID = lq.QUESTION_TYPE) as QUESTION_TYPE, \r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.cpd_id = lq.RAISED_BY_ASSEMBLY) as RAISED_BY_ASSEMBLY, \r\n"
				+ "d.DP_DEPTDESC, lq.QUESTION_SUBJECT, \r\n"
				+ "lq.MLA_NAME, lq.QUESTION, lq.MEETING_DATE, lq.DEADLINE_DATE, lq.STATUS, \r\n"
				+ "(select e.EMPNAME from employee e where e.EMPID = lq.CREATED_BY) as Question_CREATED_BY , \r\n"
				+ "(select e.EMPNAME from employee e where e.EMPID = lq.EMP_ID) as Questionee, \r\n"
				+ "lq.REOPEN_DATE\r\n" + "from tb_lqp_query_registration lq, tb_department d\r\n"
				+ "where d.DP_DEPTID=lq.DEPT_ID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		queryBuilder.append("and QUESTION_DATE BETWEEN \r\n" + "DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
				+ " day) and '" + toDate + "'\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseLQPQuestionData = (List<DeptWiseQuestionDataLQPEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptWiseQuestionDataLQPEntity.class).getResultList();
		return deptWiseLQPQuestionData;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPAnswerCount(String deptName, int noOfDays,
			String toDate) {
		List<DeptAndCourtWiseLegalCntEntity> deptWiseLQPAnswerCounts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select d.DP_DEPTDESC as NAME, count(*) as CNT from tb_lqp_query_answer lqa, tb_lqp_query_registration lq, tb_department d\r\n"
				+ "where lqa.QUESTION_REG_ID = lq.QUESTION_REG_ID\r\n" + "and d.DP_DEPTID=lq.DEPT_ID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		queryBuilder.append("and lqa.ANSWER_DATE BETWEEN \r\n" + "DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
				+ " day) and '" + toDate + "'\r\n");
		if (deptName == null)
			queryBuilder.append("group by d.DP_DEPTDESC");
		queryBuilder.append(") as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseLQPAnswerCounts = (List<DeptAndCourtWiseLegalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptAndCourtWiseLegalCntEntity.class).getResultList();
		return deptWiseLQPAnswerCounts;
	}

	public List<DeptWiseAnswerDataLQPEntity> getDeptWiseLQPAnswerData(String deptName, int noOfDays, String toDate) {
		List<DeptWiseAnswerDataLQPEntity> deptWiseLQPAnswerData = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select lq.question_id, lq.QUESTION_DATE, lq.INWARD_NO, \r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.CPD_ID = lq.QUESTION_TYPE) as QUESTION_TYPE, \r\n"
				+ "(select tcd.CPD_DESC from tb_comparam_det tcd where tcd.cpd_id = lq.RAISED_BY_ASSEMBLY) as RAISED_BY_ASSEMBLY, \r\n"
				+ "d.DP_DEPTDESC, lq.QUESTION_SUBJECT, \r\n"
				+ "lq.MLA_NAME, lq.QUESTION, lq.MEETING_DATE, lq.DEADLINE_DATE, lq.STATUS, \r\n"
				+ "(select e.EMPNAME from employee e where e.EMPID = lq.CREATED_BY) as Question_CREATED_BY , \r\n"
				+ "(select e.EMPNAME from employee e where e.EMPID = lq.EMP_ID) as Questionee, \r\n"
				+ "lq.REOPEN_DATE,\r\n" + "lqa.ANSWER_DATE, lqa.ANSWER, lqa.REMARK as ANSWER_REMARK,\r\n"
				+ "(select e.EMPNAME from employee e where e.EMPID = lqa.CREATED_BY) as Question_Answered_BY\r\n"
				+ "from tb_lqp_query_answer lqa, tb_lqp_query_registration lq, tb_department d\r\n"
				+ "where lqa.QUESTION_REG_ID = lq.QUESTION_REG_ID\r\n" + "and d.DP_DEPTID=lq.DEPT_ID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		queryBuilder.append("and lqa.ANSWER_DATE BETWEEN \r\n" + "DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
				+ " day) and '" + toDate + "'\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		deptWiseLQPAnswerData = (List<DeptWiseAnswerDataLQPEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptWiseAnswerDataLQPEntity.class).getResultList();
		return deptWiseLQPAnswerData;
	}

	/*
	 * Council Module APIs
	 */

	public List<CouncilProposalCntEntity> getCouncilProposalCount() {
		List<CouncilProposalCntEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select count(*) from tb_cmt_council_proposal_mast) as Received,\r\n"
				+ "(select count(*) from tb_cmt_council_proposal_mast where PROPOSAL_STATUS = 'R') as Rejected,\r\n"
				+ "(select count(*) from tb_cmt_council_proposal_mast where PROPOSAL_STATUS = 'A' ) as Approved,\r\n"
				+ "(select count(*) from tb_cmt_council_proposal_mast where PROPOSAL_STATUS in( 'P','D')) as Pending \r\n"
				+ "from dual ) as t, (SELECT @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilProposalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilProposalCntEntity.class).getResultList();
		return councilProposalList;
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilProposalCount(String deptName, int noOfDays,
			String toDate) {
		List<CouncilDeptYearWiseCntEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select  x.DP_DEPTDESC as DEPT_OR_YEAR,\r\n" + "sum(Pending) as Pending,\r\n"
				+ "sum(Rejected) as Rejected,\r\n" + "sum(Approved) as Approved,\r\n"
				+ "count(1) as Received from (\r\n" + "select d.DP_DEPTDESC, p.PROPOSAL_DATE,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (p.PROPOSAL_STATUS in ('P', 'D')) THEN 'Pending' END)\r\n"
				+ "IN ('Pending') then 1 else 0 END)  Pending,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (p.PROPOSAL_STATUS = 'R') THEN 'Rejected' END)\r\n"
				+ "IN ('Rejected') then 1 else 0 END)  Rejected,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (p.PROPOSAL_STATUS = 'A') THEN 'Approved' END)\r\n"
				+ "IN ('Approved') then 1 else 0 END)  Approved\r\n" + "from tb_cmt_council_proposal_mast p,\r\n"
				+ "tb_department d\r\n" + "where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '"
					+ toDate + "'\r\n");
		queryBuilder.append(") x group by x.DP_DEPTDESC ) as t, (SELECT @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilProposalList;
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilProposalCount(String deptName, int noOfDays,
			String toDate) {
		List<CouncilDeptYearWiseCntEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select year(x.PROPOSAL_DATE) as DEPT_OR_YEAR,\r\n" + "sum(Pending) as Pending,\r\n"
				+ "sum(Rejected) as Rejected,\r\n" + "sum(Approved) as Approved,\r\n"
				+ "count(1) as Received from (\r\n" + "select d.DP_DEPTDESC, p.PROPOSAL_DATE,\r\n" + "(CASE WHEN\r\n"
				+ "	(CASE WHEN (p.PROPOSAL_STATUS in ('P', 'D')) THEN 'Pending' END)\r\n"
				+ "	IN ('Pending') then 1 else 0 END) Pending,\r\n" + "	(CASE WHEN\r\n"
				+ "	(CASE WHEN (p.PROPOSAL_STATUS = 'R') THEN 'Rejected' END)\r\n"
				+ "	IN ('Rejected') then 1 else 0 END) Rejected,\r\n" + "	(CASE WHEN\r\n"
				+ "	(CASE WHEN (p.PROPOSAL_STATUS = 'A') THEN 'Approved' END)\r\n"
				+ "	IN ('Approved') then 1 else 0 END) Approved \r\n" + "from tb_cmt_council_proposal_mast p,\r\n"
				+ "tb_department d\r\n" + "where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '"
					+ toDate + "'\r\n");
		queryBuilder.append(") x group by year(x.PROPOSAL_DATE) ) as t, (SELECT @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilProposalList;
	}

	public List<CouncilDeptWiseProposalDataEntity> getDeptWiseProposalGridData(String deptName, int noOfDays,
			String toDate, int orgId) {
		List<CouncilDeptWiseProposalDataEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select  p.PROPOSAL_NUMBER, d.DP_DEPTDESC, p.PROPOSAL_DATE, p.PROPOSAL_DETAILS,\r\n"
				+ "p.PROPOSAL_AMOUNT, \r\n" + "( CASE \r\n" + "	WHEN (p.PROPOSAL_STATUS = 'A') THEN 'Approved'\r\n"
				+ "	WHEN (p.PROPOSAL_STATUS = 'R') THEN 'Rejected'\r\n"
				+ "	WHEN (p.PROPOSAL_STATUS in ('P', 'D')) THEN 'Pending' END\r\n" + "  ) as PROPOSAL_STATUS\r\n"
				+ ", p.purpose_remark, e.EMPNAME as CREATED_BY\r\n" + " from tb_cmt_council_proposal_mast p,\r\n"
				+ " tb_department d,\r\n" + " employee e\r\n" + " where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n"
				+ " and e.EMPID = p.CREATED_BY\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append("and p.PROPOSAL_DATE between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '"
					+ toDate + "'\r\n");
		queryBuilder.append("-- and p.ORGID = " + orgId + " \r\n "
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilDeptWiseProposalDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptWiseProposalDataEntity.class).getResultList();
		return councilProposalList;
	}

	public List<CouncilProposalCntEntity> getCouncilAgendaCount() {
		List<CouncilProposalCntEntity> councilAgendaList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select t.Received, t.Approved, (t.Received - t.Approved) as Pending, 0 as Rejected from (\r\n"
				+ "select\r\n" + "(select count(*) from tb_cmt_council_agenda_mast) as Received,\r\n"
				+ "(select count(*) from tb_cmt_council_agenda_mast a, TB_CMT_COUNCIL_MEETING_MAST m\r\n"
				+ "where a.AGENDA_ID = m.AGENDA_ID) as Approved ) t\r\n" + ") as ta, (SELECT @row_number\\:=0) AS rn");
		councilAgendaList = (List<CouncilProposalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilProposalCntEntity.class).getResultList();
		return councilAgendaList;
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilAgendaCount() {
		List<CouncilDeptYearWiseCntEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select t.DEPT_OR_YEAR, t.Received, t.Rejected, t.Approved,\r\n"
				+ "(t.Received - t.Approved) as Pending from (\r\n" + "select year(x.AGENDA_DATE) as DEPT_OR_YEAR,\r\n"
				+ "sum(0) as Rejected,\r\n" + "sum(Approved) as Approved,\r\n" + "count(1) as Received from (\r\n"
				+ "select a.AGENDA_DATE,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (a.AGENDA_ID <> m.AGENDA_ID) THEN 'PENDING' END)\r\n"
				+ "IN ('PENDING') then 1 else 0 END)  PENDING,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (a.AGENDA_ID = m.AGENDA_ID) THEN 'Approved' END)\r\n"
				+ "IN ('Approved') then 1 else 0 END)  Approved \r\n" + "from tb_cmt_council_agenda_mast a,\r\n"
				+ "TB_CMT_COUNCIL_MEETING_MAST m\r\n" + "where a.AGENDA_ID = m.AGENDA_ID ) x \r\n"
				+ "group by year(x.AGENDA_DATE) ) as t ) as ta, (SELECT @row_number\\:=0) AS rn");
		// queryBuilder.append(") x group by year(x.PROPOSAL_DATE) ) as t, (SELECT
		// @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilProposalList;
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilAgendaCount(String deptName, int noOfDays,
			String toDate) {
		List<CouncilDeptYearWiseCntEntity> councilAgendaList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select t.DEPT_OR_YEAR, t.Received, t.Rejected, t.Approved,\r\n"
				+ "(t.Received - t.Approved) as Pending from (\r\n" + "select x.DP_DEPTDESC as DEPT_OR_YEAR,\r\n"
				+ "sum(0) as Rejected,\r\n" + "sum(Approved) as Approved,\r\n" + "count(1) as Received from (\r\n"
				+ "select d.DP_DEPTDESC,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (a.AGENDA_ID <> m.AGENDA_ID) THEN 'PENDING' END)\r\n"
				+ "IN ('PENDING') then 1 else 0 END)  PENDING,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (a.AGENDA_ID = m.AGENDA_ID) THEN 'Approved' END)\r\n"
				+ "IN ('Approved') then 1 else 0 END)  Approved \r\n" + "from tb_cmt_council_agenda_mast a,\r\n"
				+ "TB_CMT_COUNCIL_MEETING_MAST m,\r\n" + "tb_cmt_council_proposal_mast p,\r\n" + "tb_department d\r\n"
				+ "where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n" + "and a.AGENDA_ID = m.AGENDA_ID\r\n"
				+ "and p.AGENDA_ID = a.AGENDA_ID\r\n" + "and p.ISMOMPROPOSAL = 'N'\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and a.AGENDA_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append(
					"and a.AGENDA_DATE between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '" + toDate + "'\r\n");
		queryBuilder.append(") x group by x.DP_DEPTDESC ) as t ) as ta, (SELECT @row_number\\:=0) AS rn");
		councilAgendaList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilAgendaList;
	}

	public List<CouncilDeptWiseAgendaDataEntity> getDeptWiseAgendaGridData(String deptName, int noOfDays, String toDate,
			int orgId) {
		List<CouncilDeptWiseAgendaDataEntity> councilAgendaList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select  p.PROPOSAL_NUMBER, d.DP_DEPTDESC, p.PROPOSAL_DATE, a.AGENDA_DATE, com.CPD_DESC as COMMITTEE_TYPE, p.PROPOSAL_DETAILS,\r\n"
				+ "p.PROPOSAL_AMOUNT, \r\n" + "( CASE \r\n" + "	WHEN (a.AGENDA_ID = m.AGENDA_ID) THEN 'Approved'\r\n"
				+ "	WHEN (a.AGENDA_ID <> m.AGENDA_ID) THEN 'Pending' END\r\n" + "  ) as AGENDA_STATUS\r\n"
				+ ", p.purpose_remark, e.EMPNAME as CREATED_BY\r\n" + "from tb_cmt_council_agenda_mast a,\r\n"
				+ "TB_CMT_COUNCIL_MEETING_MAST m,\r\n" + "tb_cmt_council_proposal_mast p,\r\n"
				+ "tb_department d, employee e,\r\n" + "tb_comparam_det com\r\n"
				+ "where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n" + "and e.EMPID = p.CREATED_BY\r\n"
				+ "and a.AGENDA_ID = m.AGENDA_ID\r\n" + "and p.AGENDA_ID = a.AGENDA_ID\r\n"
				+ "and a.COMMITTEE_TYPE_ID = com.CPD_ID\r\n" + "and p.ISMOMPROPOSAL = 'N'\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and a.AGENDA_DATE between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append(
					"and a.AGENDA_DATE between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '" + toDate + "'\r\n");
		queryBuilder.append("-- and p.ORGID = " + orgId + " \r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		councilAgendaList = (List<CouncilDeptWiseAgendaDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptWiseAgendaDataEntity.class).getResultList();
		return councilAgendaList;
	}

	public List<CouncilProposalCntEntity> getCouncilMeetingCount() {
		List<CouncilProposalCntEntity> councilAgendaList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select RECEIVED, APPROVED, (RECEIVED - APPROVED) as Pending, 0 as Rejected from (\r\n" + "select\r\n"
				+ "(select count(*) from TB_CMT_COUNCIL_MEETING_MAST) as RECEIVED,\r\n"
				+ "(select count(*) from tb_cmt_council_resolution) as APPROVED\r\n" + ") as t\r\n"
				+ ") as ta, (SELECT @row_number\\:=0) AS rn");
		councilAgendaList = (List<CouncilProposalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilProposalCntEntity.class).getResultList();
		return councilAgendaList;
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilMeetingCount() {
		List<CouncilDeptYearWiseCntEntity> councilMeetingCntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select t.DEPT_OR_YEAR, t.Received, t.Rejected, t.Approved,\r\n"
				+ "(t.Received - t.Approved) as Pending from (\r\n"
				+ "select year(x.MEETING_DATE_TIME) as DEPT_OR_YEAR,\r\n" + "sum(0) as Rejected,\r\n"
				+ "sum(Approved) as Approved,\r\n" + "count(1) as Received from (\r\n"
				+ "select r.reso_id, m.MEETING_ID, m.MEETING_DATE_TIME,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (m.MEETING_ID <> r.MEETING_ID) THEN 'Pending' END)\r\n"
				+ "IN ('Pending') then 1 else 0 END) as Pending,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (m.MEETING_ID = r.MEETING_ID) THEN 'Approved' END)\r\n"
				+ "IN ('Approved') then 1 else 0 END) as Approved\r\n" + "from TB_CMT_COUNCIL_MEETING_MAST m\r\n"
				+ "left outer join\r\n" + "tb_cmt_council_resolution r\r\n" + "on m.MEETING_ID = r.MEETING_ID) as x\r\n"
				+ "group by year(x.MEETING_DATE_TIME) ) as t\r\n" + ") as ta, (SELECT @row_number\\:=0) AS rn");

		councilMeetingCntList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilMeetingCntList;
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilMeetingCount(String deptName, int noOfDays,
			String toDate) {
		List<CouncilDeptYearWiseCntEntity> councilMeetingList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, ta.* from (\r\n"
				+ "select t.DEPT_OR_YEAR, t.Received, t.Rejected, t.Approved,\r\n"
				+ "(t.Received - t.Approved) as Pending from (\r\n" + "select x.DP_DEPTDESC as DEPT_OR_YEAR,\r\n"
				+ "sum(0) as Rejected,\r\n" + "sum(Approved) as Approved,\r\n" + "count(1) as Received from (\r\n"
				+ "select r.reso_id, m.MEETING_ID, m.MEETING_DATE_TIME, d.DP_DEPTDESC,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (m.MEETING_ID <> r.MEETING_ID) THEN 'Pending' END)\r\n"
				+ "IN ('Pending') then 1 else 0 END) as Pending,\r\n" + "(CASE WHEN\r\n"
				+ "(CASE WHEN (m.MEETING_ID = r.MEETING_ID) THEN 'Approved' END)\r\n"
				+ "IN ('Approved') then 1 else 0 END) as Approved\r\n" + "from TB_CMT_COUNCIL_MEETING_MAST m,\r\n"
				+ "tb_cmt_council_resolution r,\r\n" + "tb_cmt_council_agenda_mast a,\r\n"
				+ "tb_cmt_council_proposal_mast p,\r\n" + "tb_department d\r\n"
				+ "where m.MEETING_ID = r.MEETING_ID\r\n" + "and m.AGENDA_ID = a.AGENDA_ID\r\n"
				+ "and a.AGENDA_ID = p.AGENDA_ID\r\n" + "and p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n"
				+ "and p.ISMOMPROPOSAL = 'N'\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and m.MEETING_DATE_TIME between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append("and m.MEETING_DATE_TIME between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '"
					+ toDate + "'\r\n");
		queryBuilder.append(") x group by x.DP_DEPTDESC ) as t ) as ta, (SELECT @row_number\\:=0) AS rn");
		councilMeetingList = (List<CouncilDeptYearWiseCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptYearWiseCntEntity.class).getResultList();
		return councilMeetingList;
	}

	public List<CouncilDeptWiseMeetingDataEntity> getDeptWiseMeetingGridData(String deptName, int noOfDays,
			String toDate, int orgId) {
		List<CouncilDeptWiseMeetingDataEntity> councilMeetingList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select  r.reso_id as RESOLUTION_ID, m.MEETING_ID, m.MEETING_NUMBER, m.MEETING_DATE_TIME, com.CPD_DESC as MEETING_TYPE, m.MEETING_PLACE,\r\n"
				+ "m.MEETING_INVITATION_MSG, d.DP_DEPTDESC, p.PROPOSAL_NUMBER,\r\n"
				+ "p.PROPOSAL_DATE, a.AGENDA_DATE, p.PROPOSAL_DETAILS, p.PROPOSAL_AMOUNT,\r\n" + "( CASE \r\n"
				+ "	WHEN (m.MEETING_ID = r.MEETING_ID) THEN 'Approved'\r\n"
				+ "	WHEN (m.MEETING_ID <> r.MEETING_ID) THEN 'Pending' END\r\n" + "  ) as MEETING_STATUS\r\n"
				+ ", p.purpose_remark, e.EMPNAME as CREATED_BY\r\n" + "from TB_CMT_COUNCIL_MEETING_MAST m,\r\n"
				+ "tb_cmt_council_resolution r,\r\n" + "tb_cmt_council_agenda_mast a,\r\n"
				+ "tb_cmt_council_proposal_mast p,\r\n" + "tb_department d, employee e,\r\n" + "tb_comparam_det com\r\n"
				+ "where m.MEETING_ID = r.MEETING_ID\r\n" + "and m.AGENDA_ID = a.AGENDA_ID\r\n"
				+ "and a.AGENDA_ID = p.AGENDA_ID\r\n" + "and p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n"
				+ "and m.MEETING_TYPE_ID = com.CPD_ID\r\n" + "and e.EMPID = m.CREATED_BY\r\n"
				+ "and p.ISMOMPROPOSAL = 'N'\r\n");
		if (deptName != null)
			queryBuilder.append("and d.DP_DEPTDESC = '" + deptName + "'\r\n");
		if (noOfDays != 0)
			queryBuilder.append("and m.MEETING_DATE_TIME between DATE_SUB('" + toDate + "', INTERVAL " + noOfDays
					+ " day) and '" + toDate + "'\r\n");
		else
			queryBuilder.append("and m.MEETING_DATE_TIME between DATE_SUB('" + toDate + "', INTERVAL 365 day) and '"
					+ toDate + "'\r\n");
		queryBuilder.append("-- and m.ORGID = " + orgId + " \r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		councilMeetingList = (List<CouncilDeptWiseMeetingDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptWiseMeetingDataEntity.class).getResultList();
		return councilMeetingList;
	}

	public FinancialYear getFinancialYear() {
		List<FinancialYear> financialYears = null;
		StringBuilder queryBuilder = new StringBuilder();
		FinancialYear financialYear = new FinancialYear();
		queryBuilder.append(
				"select * from tb_financialyear where FA_YEARID = (select max(FA_YEARID) from tb_financialyear)");
		financialYears = (List<FinancialYear>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYear.class).getResultList();
		if (financialYears != null && !financialYears.isEmpty()) {
			financialYear = financialYears.get(0);
		}
		return financialYear;
	}
	

	/*
	 * SKDCL common APIs
	 */

	public SKDCLDashboardAllCountsEntity getSKDCLDashboardAllCounts() {
		SKDCLDashboardAllCountsEntity dashboardAllCountsEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select (select count(*) from tb_department where status = 'A') as deptCount,\r\n"
				+ "(select count(*) from tb_services_mst where SM_SERV_ACTIVE = 39) as serviceCount,\r\n");
		queryBuilder.append("(select (crCnt + propCnt + wtCnt + bndCnt + mrmCnt + licCnt) from (\r\n" + "select\r\n"
				+ "(select count(*) from tb_care_request) as crCnt, \r\n"
				+ "(select count(*) from tb_as_assesment_mast where MN_ASS_aut_status = 'Y') as propCnt, \r\n"
				+ "(select count(*) as cnt from tb_csmr_info where PC_FLG = 'Y') as wtCnt,\r\n"
				+ "(select count(*) from tb_bd_cfc_interface) as bndCnt,\r\n"
				+ "(select count(*) from tb_mrm_marriage) as mrmCnt,\r\n"
				+ "(select count(*) FROM tb_ml_trade_mast) as licCnt\r\n"
				+ "from dual) as cnts) as applReceivedCount,\r\n");
		queryBuilder.append("(select (crCnt + propCnt + mrmCnt + bndCnt + licCnt) from (\r\n" + "select \r\n"
				+ "(select COALESCE(sum(Closed), 0) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END)  Closed\r\n" + "from tb_workflow_request b,\r\n"
				+ "tb_care_request c\r\n" + "where c.apm_application_id=b.apm_application_id) x) as crCnt,\r\n"
				+ "(select COALESCE(sum(Closed), 0) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END)  Closed\r\n" + "from tb_workflow_request b,\r\n"
				+ "tb_as_assesment_mast a\r\n" + "where a.apm_application_id=b.apm_application_id) x) as propCnt,\r\n"
				+ "(select COALESCE(sum(Closed), 0) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END) Closed\r\n" + "from tb_mrm_marriage m,\r\n"
				+ "tb_workflow_request b\r\n" + "where m.APPLICATION_ID = b.APM_APPLICATION_ID) x) as mrmCnt,\r\n"
				+ "(select COALESCE(sum(Closed), 0) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END) Closed\r\n" + "from tb_bd_cfc_interface c,\r\n"
				+ "tb_workflow_request b\r\n" + "where c.apm_application_id=b.apm_application_id) x) as bndCnt,\r\n"
				+ "(select COALESCE(sum(Closed), 0) from (\r\n" + "select (CASE WHEN\r\n"
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED' END)\r\n"
				+ "IN ('CLOSED') then 1 else 0 END) Closed\r\n" + "from tb_ml_trade_mast c,\r\n"
				+ "tb_workflow_request b\r\n" + "where c.apm_application_id=b.apm_application_id) x) as licCnt\r\n"
				+ "from dual) as cnts) as applDisposedCount,\r\n");
		queryBuilder.append("(select count(*) from tb_care_request) as grievancesCount,\r\n"
				+ "(select count(*) from tb_as_assesment_mast where MN_ASS_aut_status = 'Y') as propertyCount,\r\n"
				+ "(select count(*) as cnt from tb_csmr_info where PC_FLG = 'Y') as waterCount, (select count(*) from tb_bd_cfc_interface) as birthDeathCount,\r\n"
				+ "(select count(*) from tb_mrm_marriage) as marriageRegistCount, (select count(*) FROM tb_ml_trade_mast) as tradeLicenCount,\r\n"
				+ "(select 0) as financialAccountCount\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		dashboardAllCountsEntity = (SKDCLDashboardAllCountsEntity) entityManager
				.createNativeQuery(queryBuilder.toString(), SKDCLDashboardAllCountsEntity.class).getSingleResult();
		return dashboardAllCountsEntity;
	}

	/*
	 * HRMS Module APIs
	 */

	public List<HRMSCategoryWiseEmpCntEntity> getStatusWiseEmployeeCount() {
		List<HRMSCategoryWiseEmpCntEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select Old_Emp_Status as CATEGORY, count(Old_Emp_Status) as CNT from tblemployeemaster where Pay_Processing_Status='Active' and Transfer_Status='Yes' group by Old_Emp_Status\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSCategoryWiseEmpCntEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSCategoryWiseEmpCntEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSCategoryWiseEmpCntEntity> getCategoryWiseEmployeeCount() {
		List<HRMSCategoryWiseEmpCntEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select Employee_Category as CATEGORY, count(Employee_Category) as CNT from tblemployeemaster where employee_category is not null group by Employee_Category\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSCategoryWiseEmpCntEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSCategoryWiseEmpCntEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSCategoryWiseEmpCntEntity> getDepartmentWiseEmployeeCount() {
		List<HRMSCategoryWiseEmpCntEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select Department as CATEGORY, count(Employee_id) as CNT from tblemployeemaster where Pay_Processing_Status='Active' and Transfer_Status='Yes' group by Department\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSCategoryWiseEmpCntEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSCategoryWiseEmpCntEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSGenderRatioEntity> getGenderWiseEmployeeCount() {
		List<HRMSGenderRatioEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select gender, count(gender) as CNT,(cast(count(gender) as decimal) / (select count(gender) from tblemployeemaster)) * 100 as Percent from tblemployeemaster  \r\n"
				+ "where gender is not null group by gender\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSGenderRatioEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSGenderRatioEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSSalaryBreakdownEntity> getSalaryRangeWiseEmployeeCount() {
		List<HRMSSalaryBreakdownEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + "SELECT CASE\r\n"
				+ "         WHEN Gross_pay <= 10000 THEN '0-10000'\r\n"
				+ "         WHEN Gross_pay <= 20000 THEN '10001-20000'\r\n"
				+ "         WHEN Gross_pay <= 30000 THEN '20001-30000'\r\n"
				+ "         WHEN Gross_pay <= 40000 THEN '30001-40000'\r\n"
				+ "         WHEN Gross_pay <= 50000 THEN '40001-50000'\r\n"
				+ "         WHEN Gross_pay <= 60000 THEN '50001-60000'\r\n"
				+ "         WHEN Gross_pay <= 70000 THEN '60001-70000'\r\n"
				+ "         WHEN Gross_pay <= 80000 THEN '70001-80000'\r\n"
				+ "         WHEN Gross_pay <= 90000 THEN '80001-90000'\r\n"
				+ "         WHEN Gross_pay <= 100000 THEN '90001-100000'\r\n" + "         ELSE '>100000'\r\n"
				+ "       END AS Gross_pay_range,\r\n" + "       COUNT(*) AS no_of_employees,department\r\n"
				+ "FROM tblmonthlyemployeepay \r\n" + "where paymonth_name='May' \r\n"
				+ "and WF_Status = 'Approved'\r\n" + "GROUP BY department, CASE\r\n"
				+ "         WHEN Gross_pay <= 10000 THEN '0-10000'\r\n"
				+ "         WHEN Gross_pay <= 20000 THEN '10001-20000'\r\n"
				+ "         WHEN Gross_pay <= 30000 THEN '20001-30000'\r\n"
				+ "         WHEN Gross_pay <= 40000 THEN '30001-40000'\r\n"
				+ "         WHEN Gross_pay <= 50000 THEN '40001-50000'\r\n"
				+ "         WHEN Gross_pay <= 60000 THEN '50001-60000'\r\n"
				+ "         WHEN Gross_pay <= 70000 THEN '60001-70000'\r\n"
				+ "         WHEN Gross_pay <= 80000 THEN '70001-80000'\r\n"
				+ "         WHEN Gross_pay <= 90000 THEN '80001-90000'\r\n"
				+ "         WHEN Gross_pay <= 100000 THEN '90001-100000'\r\n" + "         ELSE '>100000'\r\n"
				+ "         END order by Gross_pay_range\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSSalaryBreakdownEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSSalaryBreakdownEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSEmployeeGridDataEntity> getSalaryRangeDeptWiseEmployeeData(String dept, String salaryRange) {
		List<HRMSEmployeeGridDataEntity> empData = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder = new StringBuilder("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select em.Employee_ID, em.Employee_Name, em.Department, em.Age, em.Date_of_Birth, em.Mobile_No, em.Email_ID, em.Date_of_Joining, em.Employee_Status\r\n"
				+ "from tblemployeemaster em, tblmonthlyemployeepay ep\r\n"
				+ "where em.Employee_ID = ep.Employee_ID\r\n" + "and ep.paymonth_name='May'\r\n"
				+ "and ep.WF_Status = 'Approved'\r\n");
		if (salaryRange != null) {
			String[] salRange = salaryRange.split("-");
			if (salRange.length != 0) {
				queryBuilder.append(
						"and ep.Gross_Pay >= '" + salRange[0] + "' and ep.Gross_Pay <= '" + salRange[1] + "'\r\n");
			}
		}
		if (dept != null) {
			queryBuilder.append("and ep.Department = '" + dept + "'\r\n");
		}
		queryBuilder.append(") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			empData = (List<HRMSEmployeeGridDataEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSEmployeeGridDataEntity.class).getResultList();
			return empData;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSCategoryWiseEmpCntEntity> getJoiningYearWiseEmployeeCount() {
		List<HRMSCategoryWiseEmpCntEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select extract(year from e.DATE_OF_JOINING) as CATEGORY, count(e.EmployeeMaster_Id) as CNT from tblemployeemaster e\r\n"
				+ "where extract(year from e.DATE_OF_JOINING) is not null\r\n" + "group by CATEGORY\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<HRMSCategoryWiseEmpCntEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSCategoryWiseEmpCntEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSEmployeeGridDataEntity> getEmployeeData(String criteria, String criteriaType) {
		List<HRMSEmployeeGridDataEntity> empData = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select Employee_ID, Employee_Name, Department, Age, Date_of_Birth, Mobile_No, Email_ID, Date_of_Joining, Employee_Status from tblemployeemaster\r\n");
		if (criteriaType.equals("DEPARTMENT")) {
			queryBuilder.append("where Pay_Processing_Status='Active' and Transfer_Status='Yes'\r\n");
			if (criteria != null) {
				queryBuilder.append("and Department = '" + criteria + "'\r\n");
			}
		}
		if (criteriaType.equals("CATEGORY") && criteria != null) {
			queryBuilder.append("where Employee_Category = '" + criteria + "'\r\n");
		}
		if (criteriaType.equals("STATUS")) {
			queryBuilder.append("where Pay_Processing_Status='Active' and Transfer_Status='Yes'\r\n");
			if (criteria != null) {
				queryBuilder.append("and Old_Emp_Status = '" + criteria + "'\r\n");
			}
		}
		if (criteriaType.equals("GENDER") && criteria != null) {
			queryBuilder.append("where gender = '" + criteria + "'\r\n");
		}
		if (criteriaType.equals("JOINING_YEAR") && criteria != null) {
			queryBuilder.append("where year(Date_of_Joining) = '" + criteria + "'");
		}

		queryBuilder.append(") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			empData = (List<HRMSEmployeeGridDataEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSEmployeeGridDataEntity.class).getResultList();
			return empData;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<HRMSRecruitmentPostsEntity> getVacantPostInfo() {
		List<HRMSRecruitmentPostsEntity> info = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select Designation_Name as Name_Of_Post,cadre_name as Cadre_Name, total_no_of_quotas as Sanctioned,\r\n"
				+ "(select count(employee_id) from tblemployeemaster a where a.Designation=b.designation_ref and a.ulb_code=b.ulb_code\r\n"
				+ " and payhistory_fromdate_max<='2017-12-18') as Filled,(total_no_of_quotas-(select count(employee_id) from tblemployeemaster a where a.Designation=b.designation_ref and a.ulb_code=b.ulb_code\r\n"
				+ " and payhistory_fromdate_max<='2017-12-18')) as Vacant,\r\n"
				+ " (select  sum(total_no_of_vacancy)  from tbljobvacancydetails c where c.designation=b.designation_ref and c.cadre=b.cadre_ref and Jvstatus='Processed' and c.ulb_code=b.ulb_code and c.indent_date<='2019-12-18') as Recruitment_Total_Posts,\r\n"
				+ " (select count(jobapplicationform_id) from tbljobapplicationform d where d.Designation_value=b.designation_name and d.cader_value=b.cadre_name and d.ulb_code=b.ulb_code and d.offer_accepted_yes_no='Not Accepted' and d.application_date<='2019-12-18'\r\n"
				+ "  ) as Filled2,(((select  sum(total_no_of_vacancy)  from tbljobvacancydetails c where c.designation=b.designation_ref and c.cadre=b.cadre_ref and Jvstatus='Processed' and c.ulb_code=b.ulb_code and c.indent_date<='2019-12-18'))-\r\n"
				+ "  ((select count(jobapplicationform_id) from tbljobapplicationform d where d.Designation_value=b.designation_name and d.cader_value=b.cadre_name and d.ulb_code=b.ulb_code and d.offer_accepted_yes_no='Not Accepted' and d.application_date<='2019-12-18'\r\n"
				+ "  ) )) as Vacant2\r\n" + "from tblsanctionedpost b where Active_Status='Active'\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			info = (List<HRMSRecruitmentPostsEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), HRMSRecruitmentPostsEntity.class).getResultList();
			return info;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	/*
	 * Store Module APIs
	 */

	public List<StoreItemCountEntity> getItemGroupWiseInventoryValue() {
		List<StoreItemCountEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select b.item_group_name as Name, sum(a.av_quantity) as CNT from tblbinlocation a join tblitemmaster b where a.item_description=b.Item_Description and a.ULB_Code = b.ULB_Code group by b.item_group_name\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<StoreItemCountEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), StoreItemCountEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<StoreItemWiseInventoryCntEntity> getItemWiseInventoryValue() {
		List<StoreItemWiseInventoryCntEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select b.item_group_name, a.item_description, sum(a.av_quantity) as CNT from tblbinlocation a join tblitemmaster b where a.item_description=b.Item_Description \r\n"
				+ "and a.ULB_Code = b.ULB_Code\r\n" + "group by b.item_group_name,a.item_description\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<StoreItemWiseInventoryCntEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), StoreItemWiseInventoryCntEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<StoreItemCountEntity> getItemGroupWiseItemCount() {
		List<StoreItemCountEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select item_group_name as Name, count(item_description) as CNT from tblitemmaster where Item_Status ='Active' and Item_Group_Name is not null group by item_group_name\r\n"
				+ ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<StoreItemCountEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), StoreItemCountEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<StoreItemCountEntity> getTopNMovingItemCounts(int limit) {
		List<StoreItemCountEntity> counts = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select item_description as Name, count(item_description) as CNT from tblbinlocation where item_description is not null group by item_description order by CNT desc limit "
				+ limit + "\r\n" + ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			counts = (List<StoreItemCountEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), StoreItemCountEntity.class).getResultList();
			return counts;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}

	public List<StoreItemDataEntity> getItemDataForGroup(String itemGroup) {
		List<StoreItemDataEntity> itemData = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select b.Item_Code, b.item_description, b.Item_Status, b.Item_Group_Name, b.Item_belongs_to, b.Item_Valuation_Method, b.Purpose,\r\n"
				+ "a.av_quantity as Available_Quantity\r\n"
				+ "from tblbinlocation a join tblitemmaster b where a.item_description=b.Item_Description \r\n"
				+ "and a.ULB_Code = b.ULB_Code\r\n");
		if (itemGroup != null) {
			queryBuilder.append("and b.Item_Group_Name = '" + itemGroup + "'\r\n");
		}
		queryBuilder.append("order by b.item_group_name" + ") as t, (SELECT @row_number\\:=0) AS rn");
		try {
			itemData = (List<StoreItemDataEntity>) entityManager
					.createNativeQuery(queryBuilder.toString(), StoreItemDataEntity.class).getResultList();
			return itemData;
		} catch (Exception e) {
			log.error("Exception occured while getting dashboard data :: ", e);
		}
		return null;
	}
 
	// #120768
	@SuppressWarnings("unchecked")
	public FinancialYear getCurrentFinancialYear() {
		List<FinancialYear> financialYears = null;
		StringBuilder queryBuilder = new StringBuilder();
		FinancialYear financialYear = new FinancialYear();
		queryBuilder.append(
				"select * from tb_financialyear where FA_YEARID = (select max(FA_YEARID) from tb_financialyear where now() between FA_FROMDATE and FA_TODATE)");
		financialYears = (List<FinancialYear>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialYear.class).getResultList();
		if (financialYears != null && !financialYears.isEmpty()) {
			financialYear = financialYears.get(0);
		}
		return financialYear;
	}

	@SuppressWarnings("unchecked")
	public List<CouncilDeptWiseProposalCntEntity> getOrgIdWiseCouncilProposalCount(Long orgId, String fromDate,
			String toDate) {
		List<CouncilDeptWiseProposalCntEntity> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.DEPT_NAME, t.PROPOSAL_COUNT as PROPOSAL_COUNT from (\r\n"
						+ "select x.DP_DEPTDESC as DEPT_NAME," + "count(1) as PROPOSAL_COUNT from ("
						+ "select d.DP_DEPTDESC, p.PROPOSAL_DATE from tb_cmt_council_proposal_mast p, tb_department d "
						+ "where p.PROPOSAL_DEP_ID = d.DP_DEPTID\r\n");
		if (orgId != null)
			queryBuilder.append("and p.orgId ='" + orgId + "'\r\n");
		if (StringUtils.isNotEmpty(fromDate) && StringUtils.isNotEmpty(toDate))
			queryBuilder.append("and p.PROPOSAL_DATE between '" + fromDate + "' and '" + toDate + "'\r\n");
		queryBuilder.append(") x group by x.DP_DEPTDESC ) as t, (SELECT @row_number\\:=0) AS rn");
		councilProposalList = (List<CouncilDeptWiseProposalCntEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilDeptWiseProposalCntEntity.class).getResultList();
		return councilProposalList;
	}
	@SuppressWarnings("unchecked")
	public List<RTIApplicationByApplicantType> getRTIApplicationByApplicantType() {
		List<RTIApplicationByApplicantType> rtiAppltypeList=null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + 
				"				 select        (Select cpd_desc from tb_comparam_det where cpd_id=RTI_APPLICANT_TYPYE) as applicantType , sum(Closed) as closed_query,\r\n" + 
				"				             sum(Pending) as Pending_query,              sum(Rejected) as expired_query,\r\n" + 
				"				             sum(Hold) as Hold,              count(1) as recieved_query from (\r\n" + 
				"				 select RTI_APPLICANT_TYPYE, (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('CLOSED') then 1 else 0 END)  Closed,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('PENDING') then 1 else 0 END)  Pending,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('REJECTED') then 1 else 0 END)  Rejected,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('HOLD') then 1 else 0 END)  Hold \r\n" + 
				"				              from tb_workflow_request b,    tb_rti_application c\r\n" + 
				"				              where c.apm_application_id=b.apm_application_id) x \r\n" + 
				"				              group by x.RTI_APPLICANT_TYPYE) as t, (SELECT @row_number\\:=0) AS rn\r\n" + 
				"                              \r\n" + 
				"                        \r\n" + 
				"                              ");
		rtiAppltypeList = (List<RTIApplicationByApplicantType>) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationByApplicantType.class).getResultList();
		return rtiAppltypeList;
		
	}
	@SuppressWarnings("unchecked")
	public List<RTIApplicationyBplFlagEntity> getRTIApplicationDetailByBplFactorWise() {
		List<RTIApplicationyBplFlagEntity> rtiBplltypeList=null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n" + 
				"				 select      CASE WHEN RTI_BPLFLAG='N' then 'NO'\r\n" + 
				"                 When RTI_BPLFLAG='Y' then 'YES' END as BPL_Flag , sum(Closed) as closed_query,\r\n" + 
				"				             sum(Pending) as Pending_query,              sum(Rejected) as expired_query,\r\n" + 
				"				             sum(Hold) as Hold,              count(1) as recieved_query from (\r\n" + 
				"				 select  RTI_BPLFLAG, (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('CLOSED') then 1 else 0 END)  Closed,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('PENDING') then 1 else 0 END)  Pending,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('REJECTED') then 1 else 0 END)  Rejected,              (CASE WHEN\r\n" + 
				"				              (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"				                    WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				"				             IN ('HOLD') then 1 else 0 END)  Hold \r\n" + 
				"				              from tb_workflow_request b,    tb_rti_application c\r\n" + 
				"				              where c.apm_application_id=b.apm_application_id) x \r\n" + 
				"				              group by x.RTI_BPLFLAG ) as t, (SELECT @row_number\\:=0) AS rn\r\n" + 
				"                              \r\n" + 
				"                        \r\n" + 
				"                              ");
		rtiBplltypeList = (List<RTIApplicationyBplFlagEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIApplicationyBplFlagEntity.class).getResultList();
		return rtiBplltypeList;
	}

	@SuppressWarnings("unchecked")
	public List<CouncilMeetingCountYrwise> getYearWiseCouncilMeetingCountByOrgId(Long orgId) {
		List<CouncilMeetingCountYrwise> councilMeetingList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, YEAR(MEETING_DATE_TIME) Years,Count(MEETING_ID) TOTOL_COUNT\r\n" + 
				"From TB_CMT_COUNCIL_MEETING_MAST,(SELECT @row_number\\:=0) AS rn\r\n" + 
				"Where orgId='" + orgId + "' Group By Years\r\n");
		councilMeetingList = (List<CouncilMeetingCountYrwise>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilMeetingCountYrwise.class).getResultList();
		return councilMeetingList;
	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<CouncilMeetingCountYrwise> getYearWiseAgendaCountByOrgId(Long orgId) {
		List<CouncilMeetingCountYrwise> councilAgendaList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, YEAR(AGENDA_DATE) Years,Count(AGENDA_ID) Totol_Count \r\n" + 
				"From TB_CMT_COUNCIL_AGENDA_MAST,(SELECT @row_number\\:=0) AS rn\r\n" + 
				"Where orgId='" + orgId + "' Group By Years");
		councilAgendaList = (List<CouncilMeetingCountYrwise>) entityManager
				.createNativeQuery(queryBuilder.toString(), CouncilMeetingCountYrwise.class).getResultList();
		return councilAgendaList;
	
	}

	@SuppressWarnings("unchecked")
	public List<FinancialAndNonFinacialProposalCount> getFinancialAndNonFiancialProposalCountByOrgId(Long orgId) {
		List<FinancialAndNonFinacialProposalCount> councilProposalList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, (CASE WHEN PROPOSAL_TYPE='F' Then 'Financial' ELSE 'Non_Financial' END) ProposalType,\r\n" + 
				"Count(PROPOSAL_ID) ProposalCount\r\n" + 
				"From TB_CMT_COUNCIL_PROPOSAL_MAST,(SELECT @row_number\\:=0) AS rn\r\n" + 
				"Where orgId='" + orgId + "' Group By ProposalType");
		councilProposalList = (List<FinancialAndNonFinacialProposalCount>) entityManager
				.createNativeQuery(queryBuilder.toString(), FinancialAndNonFinacialProposalCount.class).getResultList();
		return councilProposalList;
	}

	@SuppressWarnings("unchecked")
	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByBplType(String bplType) {
		List<RtiApplicationDtoByCondEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (select c.APM_APPLICATION_ID,c.RTI_NO, CASE WHEN RTI_BPLFLAG='N' then 'NO'\r\n" + 
				"						                  When RTI_BPLFLAG='Y' then 'YES' END as BPL_Flag , (select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY,\r\n" + 
				"						 (Select CPD_DESC from tb_comparam_det where CPD_ID=c.INWARD_TYPE) as INWORD_TYPE, c.RTI_SUBJECT as RTI_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST, \r\n" + 
				"						 				 			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n" + 
				"						 				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				"						 				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"						 				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) as Status \r\n" + 
				"						 								from tb_workflow_request b,tb_rti_application c,tb_organisation y  where c.orgid=y.ORGID and \r\n" + 
				"						 				             c.apm_application_id=b.apm_application_id ) as t, (SELECT @row_number\\:=0) AS rn\r\n" + 
				"                                                     where  t.BPL_Flag like "+"'"+bplType+"'");
		rtiEntList = (List<RtiApplicationDtoByCondEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RtiApplicationDtoByCondEntity.class).getResultList();
		return rtiEntList;
	}

	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByApplicantType(String applicantType) {
		List<RtiApplicationDtoByCondEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"SELECT @row_number\\:=@row_number+1 AS num, t.* from (select c.APM_APPLICATION_ID,c.RTI_NO, CASE WHEN RTI_BPLFLAG='N' then 'NO'\r\n"
						+ "                 When RTI_BPLFLAG='Y' then 'YES' END as BPL_Flag , (select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY,\r\n"
						+ "(Select CPD_DESC from tb_comparam_det where CPD_ID=c.INWARD_TYPE) as INWORD_TYPE, c.RTI_DESC as RTI_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST, \r\n"
						+ "				 			 (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION = 'REJECTED'))  THEN 'REJECTED'\r\n"
						+ "				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED')  AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
						+ "				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
						+ "				 				   WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) as Status \r\n"
						+ "								from tb_workflow_request b,tb_rti_application c,tb_organisation y  where c.orgid=y.ORGID and \r\n"
						+ "				             c.apm_application_id=b.apm_application_id and (Select CPD_DESC from tb_comparam_det where CPD_ID=c.RTI_APPLICANT_TYPYE) like '%"
						+ applicantType + "%') as t, (SELECT @row_number\\:=0) AS rn;");
		rtiEntList = (List<RtiApplicationDtoByCondEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RtiApplicationDtoByCondEntity.class).getResultList();
		return rtiEntList;
	}


	public List<RtiApplicationDtoByCondEntity> getRSAPendingClosedDrillDownData() {
		List<RtiApplicationDtoByCondEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* \r\n"
				+ "from (SELECT  c.APM_APPLICATION_ID,c.RTI_NO, CASE WHEN RTI_BPLFLAG='N' then 'NO'\r\n"
				+ "                 When RTI_BPLFLAG='Y' then 'YES' END as BPL_Flag , \r\n"
				+ "                 (select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY,\r\n"
				+ "(Select CPD_DESC from tb_comparam_det where CPD_ID=c.INWARD_TYPE) as INWORD_TYPE, \r\n"
				+ "c.RTI_DESC as RTI_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST,  RTI_SCND_APEAL_STATUS as status \r\n"
				+ "				 			 FROM tb_rti_application c\r\n"
				+ " where RTI_SCND_APEAL_STATUS in ('Completed','Pending')  ) as t, (SELECT @row_number\\:=0) AS rn");
		rtiEntList = (List<RtiApplicationDtoByCondEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RtiApplicationDtoByCondEntity.class).getResultList();
		return rtiEntList;
	}

	public List<RtiApplicationDtoByCondEntity> getRFAOpenClosedDrillDownData() {
		List<RtiApplicationDtoByCondEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* \r\n"
				+ "from (SELECT  c.APM_APPLICATION_ID,c.RTI_NO, CASE WHEN RTI_BPLFLAG='N' then 'NO'\r\n"
				+ "                 When RTI_BPLFLAG='Y' then 'YES' END as BPL_Flag , \r\n"
				+ "                 (select EMPNAME from employee where EMPID = c.UPDATED_BY) as CREATED_BY,\r\n"
				+ "(Select CPD_DESC from tb_comparam_det where CPD_ID=c.INWARD_TYPE) as INWORD_TYPE, \r\n"
				+ "c.RTI_DESC as RTI_DESC, c.APM_APPLICATION_DATE as DATE_OF_REQUEST,  tom.OBJ_STATUS as Status\r\n"
				+ "				 			 FROM tb_objection_mast tom,tb_rti_application c\r\n"
				+ " where tom.OBJ_STATUS in ('Completed','Pending') \r\n"
				+ " and tom.APM_APPLICATION_ID=c.APM_APPLICATION_ID ) as t, (SELECT @row_number\\:=0) AS rn;");
		rtiEntList = (List<RtiApplicationDtoByCondEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RtiApplicationDtoByCondEntity.class).getResultList();
		return rtiEntList;
	}

	

	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRfaOpenClosedData() {
		List<RTIDasBoardAppealStatusEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, \r\n"
				+ "Sum(Coalesce(Pending,0)) Pending,\r\n" + "Sum(Coalesce(Closed,0)) Closed\r\n"
				+ "from \r\n" + "(SELECT Case when tom.OBJ_STATUS='Pending' then count(1) end as Pending,\r\n"
				+ "Case when tom.OBJ_STATUS='Completed' then count(1) end as Closed\r\n"
				+ "FROM tb_objection_mast tom,tb_rti_application tra where \r\n"
				+ "tom.OBJ_STATUS in ('Pending','Completed') and\r\n"
				+ " tom.APM_APPLICATION_ID=tra.APM_APPLICATION_ID group by tom.OBJ_STATUS) as t, (SELECT @row_number\\:=0) AS rn \r\n"
				+ " ");
		rtiEntList = (List<RTIDasBoardAppealStatusEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIDasBoardAppealStatusEntity.class).getResultList();
		return rtiEntList;
	}

	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRSAOpenClosedData() {
		List<RTIDasBoardAppealStatusEntity> rtiEntList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, \r\n" + 
				"SUM(Coalesce(Pending,0)) Pending,\r\n" + 
				"SUM(Coalesce(Completed,0)) Closed\r\n" + 
				"				 from (SELECT RTI_SCND_APEAL_STATUS as status1, \r\n" + 
				"                 Case when RTI_SCND_APEAL_STATUS='Pending' then count(1) end as Pending,\r\n" + 
				"                 Case when RTI_SCND_APEAL_STATUS='Completed' then count(1) end as Completed\r\n" + 
				"                  \r\n" + 
				"                 FROM tb_rti_application tra\r\n" + 
				"				  where RTI_SCND_APEAL_STATUS in ('Completed','Pending') group by RTI_SCND_APEAL_STATUS) as t, (SELECT @row_number\\:=0) AS rn");
		rtiEntList = (List<RTIDasBoardAppealStatusEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), RTIDasBoardAppealStatusEntity.class).getResultList();
		return rtiEntList;
	}

	
	public List<AdvocateMastRegEntity> getAdvocateMstRegCount() {
		List<AdvocateMastRegEntity> advocateMastRegEntity = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT  p.* from (\r\n" + 
				"Select sum(t.PENDING) PENDING , sum(t.REJECTED) REJECTED , sum(t.CLOSED) CLOSED,\r\n" + 
				"sum(Total) NoOfRequests \r\n" + 
				"from (select count(1) Total,\r\n" + 
				"Case when Status='PENDING' then count(1) end PENDING,\r\n" + 
				"Case when Status='REJECTED' then count(1) end REJECTED,\r\n" + 
				"Case when Status='CLOSED' then count(1) end CLOSED\r\n" + 
				"from (select c.apm_application_id,\r\n" + 
				"( (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n" + 
				"WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n" + 
				" WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n" + 
				"WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n" + 
				") Status from tb_workflow_request b, tb_lgl_advocate_mas c\r\n" + 
				"where c.apm_application_id=b.apm_application_id and c.apm_application_id is not null and c.orgid=b.ORGID) x group by Status\r\n" + 
				") as t)p ");
		advocateMastRegEntity = (List<AdvocateMastRegEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), AdvocateMastRegEntity.class).getResultList();
		return advocateMastRegEntity;
	}
	
	
	public List<DeptByDaysAndDescEntity> getDeptComplaintsForTodayEntityList() {
		List<DeptByDaysAndDescEntity> deptComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select distinct c.APM_APPLICATION_ID, c.REFERENCE_MODE, (select EMPNAME from employee where EMPID = c.CREATED_BY) as CREATED_BY, c.COMPLAINT_NO, c.COMPLAINT_DESC, c.DATE_OF_REQUEST, z.DP_DEPTDESC as cpd_desc,\r\n"
				+ "			z.DP_NAME_MAR as cpd_desc_mar, c.LANDMARK as ADDRESS, c.LATITUDE, c.LONGITUDE, (select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO) as Division,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO1) as Ward,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO2) as Zone,\r\n" + "(CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_care_request c,\r\n" + "        tb_dept_complaint_type y,\r\n"
				+ "        tb_department z,\r\n" + "        tb_organisation u\r\n" + "        where \r\n"
				+ "		c.apm_application_id=b.apm_application_id and\r\n"
				+ "		b.date_of_request>=CURRENT_DATE() and\r\n"
				+ "        z.DP_DEPTID=c.DEPT_COMP_ID and\r\n"
				+ "        c.orgid=u.orgid) as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return deptComplaintsByDaysAndDescEntityList;
	}

	public List<DeptByDaysAndDescEntity> getDeptComplaintsFinancialYearWiseList() {
		List<DeptByDaysAndDescEntity> deptComplaintsByDaysAndDescEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, t.* from (\r\n"
				+ "select distinct c.APM_APPLICATION_ID, c.REFERENCE_MODE, (select EMPNAME from employee where EMPID = c.CREATED_BY) as CREATED_BY, c.COMPLAINT_NO, c.COMPLAINT_DESC, c.DATE_OF_REQUEST, z.DP_DEPTDESC as cpd_desc,\r\n"
				+ "			z.DP_NAME_MAR as cpd_desc_mar, c.LANDMARK as ADDRESS, c.LATITUDE, c.LONGITUDE, (select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO) as Division,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO1) as Ward,(select COD_DESC from tb_comparent_det where COD_ID=c.CARE_WARD_NO2) as Zone,\r\n" + "(CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('CLOSED') then 1 else 0 END) Closed,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('PENDING') then 1 else 0 END) Pending,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('REJECTED') then 1 else 0 END) Rejected,\r\n" + "\r\n" + "        (CASE WHEN\r\n"
				+ "        (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION = 'REJECTED')) THEN 'REJECTED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION = 'HOLD')) THEN 'HOLD'\r\n"
				+ "        WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END)\r\n"
				+ "        IN ('HOLD') then 1 else 0 END) Hold\r\n" + "        from tb_workflow_request b,\r\n"
				+ "        tb_care_request c,\r\n" + "        tb_dept_complaint_type y,\r\n"
				+ "        tb_department z,\r\n" + "        tb_organisation u\r\n" + "        where \r\n"
				+ "		c.apm_application_id=b.apm_application_id and\r\n"
				+ "		b.date_of_request>=CURRENT_DATE() and\r\n"
				+ "        z.DP_DEPTID=c.DEPT_COMP_ID and\r\n"
				+ "        c.orgid=u.orgid) as t, (SELECT @row_number\\:=0) AS rn");
		deptComplaintsByDaysAndDescEntityList = (List<DeptByDaysAndDescEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeptByDaysAndDescEntity.class).getResultList();
		return deptComplaintsByDaysAndDescEntityList;
	}

	public List<ComplaintFinancialYearEntity> getComplaintFinancialYearEntityList() {
		List<ComplaintFinancialYearEntity> citizenDashboardGraphEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, t.* from (select year as year,"
				+ "sum(Closed) as Closed_Sum,"
				+ "sum(Pending) as Pending_Sum,"
				+ "(sum(Closed)+sum(Pending)) as Total_Pending_Closed "
				+ "from (select concat(year(fa_fromdate),'-',year(fa_todate)) Year,"
				+ " (CASE WHEN (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END)  IN ('CLOSED') then 1 else 0 END)  Closed, "
				+ "(CASE WHEN  (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END)  IN ('PENDING') then 1 else 0 END)  Pending, "
				+ "(CASE WHEN  (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END)  IN ('REJECTED') then 1 else 0 END)  Rejected, "
				+ "(CASE WHEN  (CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` = 'REJECTED'))  THEN 'REJECTED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'CLOSED' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` = 'HOLD')) THEN 'HOLD' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` <> 'HOLD')) THEN 'PENDING' END) IN ('HOLD') then 1 else 0 END)  Hold from tb_workflow_request b, "
				+ "tb_care_request c,tb_financialyear a where c.apm_application_id=b.apm_application_id and b.DATE_OF_REQUEST between fa_fromdate and fa_todate)b group by year) as t, (SELECT @row_number\\:=0) AS rn");
		
		citizenDashboardGraphEntityList = (List<ComplaintFinancialYearEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ComplaintFinancialYearEntity.class).getResultList();

		return citizenDashboardGraphEntityList;
	}

	public List<ComplaintsModeWiseEntity> getComplaintModeWiseEntityList() {
		List<ComplaintsModeWiseEntity> citizenDashboardGraphEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("Select @row_number\\:=@row_number+1 AS num,"
				+ "Reference_Mode as Mode,"
				+ "ifnull(SUM(CASE WHEN Status='Closed' THEN 1 END),0) as Closed_Sum,"
				+ "ifnull(SUM(CASE WHEN Status='Pending' THEN 1 END),0) as Pending_Sum,"
				+ "ifnull(SUM(CASE WHEN Status='Received' THEN 1 END),0) as Total_Pending_Closed From (SELECT t.*  from "
				+ "(select (Select cpd_desc from tb_comparam_det where cpd_id=REFERENCE_MODE) Modes,"
				+ "(Select cpd_desc from tb_comparam_det where cpd_id=REFERENCE_MODE) as Reference_Mode,  "
				+ "date(b.DATE_OF_REQUEST) as 'CREATED_DATE',(CASE WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'CLOSED')  AND "
				+ "(`b`.`LAST_DECISION` <> 'REJECTED')) THEN 'Closed' WHEN ((REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') = 'PENDING') AND "
				+ "(`b`.`LAST_DECISION` <> 'HOLD')) THEN 'Pending' END) Status from tb_workflow_request b, tb_care_request c where c.apm_application_id=b.apm_application_id UNION ALL "
				+ "select (Select cpd_desc from tb_comparam_det where cpd_id=REFERENCE_MODE) Modes, (Select cpd_desc from tb_comparam_det where cpd_id=REFERENCE_MODE) as 'Reference Mode', date(b.DATE_OF_REQUEST) as 'CREATED_DATE',	"
				+ "(CASE WHEN (REPLACE(`b`.`STATUS`,'EXPIRED','CLOSED') in   ('PENDING','CLOSED'))  AND "
				+ "(`b`.`LAST_DECISION` NOT IN ('REJECTED','HOLD')) THEN 'Received' END)  Status from tb_workflow_request b,"
				+ "tb_care_request c where c.apm_application_id=b.apm_application_id) as t) As A, "
				+ "(SELECT @row_number\\:=0) AS rn group by Reference_Mode");
		
		citizenDashboardGraphEntityList = (List<ComplaintsModeWiseEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), ComplaintsModeWiseEntity.class).getResultList();

	return citizenDashboardGraphEntityList;
	}
	
	
	public List<ComplaintsDepartmentWiseEntity> getDepartmentComplaintsDeparmentWise() {

		List<ComplaintsDepartmentWiseEntity> complaintsDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from "
				+ "(select Complaint_Department As 'Complaint_Department',ifnull(SUM(CASE WHEN Status='Closed' THEN 1 END),0) as 'Closed_Count',ifnull(SUM(CASE WHEN Status='Pending' THEN 1 END),0) as 'Pending_Count',ifnull(SUM(CASE WHEN Status='Received' THEN 1 END),0) as 'Received_Count' From( SELECT t.*from (select DP_DEPTDESC,date(b.DATE_OF_REQUEST) as 'CREATED_DATE', DP_DEPTDESC as Complaint_Department, "
				+ "(CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (b.LAST_DECISION <> 'REJECTED')) THEN 'Closed' WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (b.LAST_DECISION <> 'HOLD')) THEN 'Pending' END) Status from tb_workflow_request b, tb_care_request c ,tb_department z where c.apm_application_id=b.apm_application_id and c.DEPT_COMP_ID=z.DP_DEPTID union all select DP_DEPTDESC,date(b.DATE_OF_REQUEST) as 'CREATED_DATE', "
				+ "DP_DEPTDESC as 'Complaint Department', (CASE WHEN ((REPLACE(b.STATUS,'EXPIRED','CLOSED') in ('CLOSED','PENDING')) AND (b.LAST_DECISION NOT IN ('REJECTED','HOLD'))) THEN 'Received' END ) Status from tb_workflow_request b, tb_care_request c ,tb_department z where c.apm_application_id=b.apm_application_id and c.DEPT_COMP_ID=z.DP_DEPTID )t) as A Group by Complaint_Department) s, "
				+ "(SELECT @row_number\\:=0) AS rn");
		
		complaintsDepartmentWiseEntityList = (List<ComplaintsDepartmentWiseEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), ComplaintsDepartmentWiseEntity.class).getResultList();

	return complaintsDepartmentWiseEntityList;
	
	}

	public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailBylicNofDatetDate(Long ward2, String fDate,
			String tDate) {

		List<LicensedtlByLicNoAndDatesEntity> licenseDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (select TRD_LICNO,(select COD_DESC from tb_comparent_det where COD_ID=TRD_WARD2)TRD_WARD2,TRD_BUSNM,(select COD_DESC from tb_comparent_det where COD_ID=TRI_COD1)lic_catg,(select COD_DESC from tb_comparent_det where COD_ID=TRI_COD2)lic_sub_Catg,TRD_LICFROM_DATE,TRD_LICTO_DATE,TRD_BUSADD from tb_ml_trade_mast a,tb_ml_item_detail b where a.TRD_ID=b.TRD_ID  and "+ 
				" a.TRD_WARD2='"+ward2+"' and Date(TRD_LICFROM_DATE)='"+fDate+"' and Date(TRD_LICTO_DATE)='"+tDate+"')s, (SELECT @row_number\\:=0) AS rn");
		
		licenseDepartmentWiseEntityList = (List<LicensedtlByLicNoAndDatesEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), LicensedtlByLicNoAndDatesEntity.class).getResultList();

	return licenseDepartmentWiseEntityList;
	
	}
	
	public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailByYear( String fDate,
			String tDate) {

		List<LicensedtlByLicNoAndDatesEntity> licenseDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(
                "select  @row_number\\:=@row_number+1 AS num, s.* from (select TRD_LICNO, (select COD_DESC from tb_comparent_det where COD_ID=TRD_WARD2)TRD_WARD2,  (select COD_DESC from tb_comparent_det where COD_ID=TRD_WARD1) Division , TRD_BUSNM,(select COD_DESC from tb_comparent_det where COD_ID=TRI_COD1)lic_catg, Group_concat(COD_DESC separator ' , ') lic_sub_Catg ,TRD_LICFROM_DATE,TRD_LICTO_DATE,TRD_BUSADD from tb_ml_trade_mast a,tb_ml_item_detail b, tb_comparent_det CD where a.TRD_ID=b.TRD_ID and CD.COD_ID=b.TRI_COD2 and TRD_LICNO IS NOT NULL  and DATE(TRD_LICISDATE) between '"+fDate+"' and '"+tDate+"' group by TRD_LICNO, TRD_WARD2, Division, TRD_BUSNM, lic_catg, TRD_LICFROM_DATE, TRD_LICTO_DATE, TRD_BUSADD  )s , (SELECT @row_number\\:=0) AS rn;\r\n");
		
		licenseDepartmentWiseEntityList = (List<LicensedtlByLicNoAndDatesEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), LicensedtlByLicNoAndDatesEntity.class).getResultList();

	return licenseDepartmentWiseEntityList;
	
	}

	public List<LicensedtlByNoAndAppIdEntity> getLicenseByNoAndAppId(Long ward2, String fDate, String tDate) {

		List<LicensedtlByNoAndAppIdEntity> licenseDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from(Select  TRD_LICNO,APM_APPLICATION_ID  from Tb_ml_trade_mast WHERE "
				+ " TRD_WARD2="+ward2+" and Date(TRD_LICFROM_DATE)='"+fDate+"' and Date(TRD_LICTO_DATE)='"+tDate+"') s, (SELECT @row_number\\:=0) AS rn");
		
		licenseDepartmentWiseEntityList = (List<LicensedtlByNoAndAppIdEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), LicensedtlByNoAndAppIdEntity.class).getResultList();

	return licenseDepartmentWiseEntityList;
	
	}

	public List<LicenseDeatilsByServceIdAndDeptIdEntity> getLicenseDeatilsByServceIdAndDeptId(Long orgId, Long deptId,
			Long serviceId, Long trdWard2, String chequeStatus, String formDate, String toDate) {

		List<LicenseDeatilsByServceIdAndDeptIdEntity> licenseDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select DISTINCT RM_RCPTID, RM.APM_APPLICATION_ID, RM_AMOUNT, RM_NARRATION, SM_SERVICE_ID from tb_receipt_mas RM,tb_ml_trade_mast TM where"
				+ "  TM.APM_APPLICATION_ID=RM.APM_APPLICATION_ID AND DP_DEPTID="+deptId+" and RM.ORGID="+orgId+" AND RM.SM_SERVICE_ID="+serviceId+" AND TRD_WARD2="+trdWard2+" AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='"+chequeStatus+"' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL and Date(RM_DATE) between '"+formDate+"' and '"+toDate+"')s, (SELECT @row_number\\:=0) AS rn");
		
		licenseDepartmentWiseEntityList = (List<LicenseDeatilsByServceIdAndDeptIdEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), LicenseDeatilsByServceIdAndDeptIdEntity.class).getResultList();

	return licenseDepartmentWiseEntityList;
	
	}

	public List<LicenseRecieptDeatilsByServceIdAndDeptIdEntity> getLicenseRecieptDeatilsByServceIdAndDeptIdEntity(
			Long orgId, Long deptId, Long serviceId, Long trdWard2, String chequeStatus, String formDate,
			String toDate) {

		List<LicenseRecieptDeatilsByServceIdAndDeptIdEntity> licenseDepartmentWiseEntityList = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select DISTINCT RD.RM_RCPTID, RD.TAX_ID,  RD.RF_FEEAMOUNT from tb_receipt_det RD, tb_receipt_mas RM,tb_ml_trade_mast TM where "
				+ "RM.RM_RCPTID=RD.RM_RCPTID AND TM.APM_APPLICATION_ID=RM.APM_APPLICATION_ID  and DP_DEPTID="+deptId+" and RM.ORGID="+orgId+" AND RM.SM_SERVICE_ID="+serviceId+" AND TRD_WARD2="+trdWard2+" AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='"+chequeStatus+"' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL and Date(RM_DATE) between '"+formDate+"' and '"+toDate+"' )s, (SELECT @row_number\\:=0) AS rn");
		
		licenseDepartmentWiseEntityList = (List<LicenseRecieptDeatilsByServceIdAndDeptIdEntity>) entityManager
			.createNativeQuery(queryBuilder.toString(), LicenseRecieptDeatilsByServceIdAndDeptIdEntity.class).getResultList();

	return licenseDepartmentWiseEntityList;
	
	}

	public List<WaterDataWardZoneWiseEntity> getWaterDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		List<WaterDataWardZoneWiseEntity> waterDataWardZoneWiseEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getWaterDataWardZoneWise");
		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (select c.cs_idn as CS_IDN,CS_CCn as CS_CCN,CS_NAME as CS_NAME, (select COD_DESC from tb_comparent_det where COD_ID=COD_DWZID2 ) as WARD,(select COD_DESC from tb_comparent_det where COD_ID=COD_DWZID3 ) as ZONE, (select CPD_DESC from tb_comparam_det where CPD_ID=cs_meteredccn) as METER_STATUS,PC_FLG as PC_FLG,(select CPD_DESC from tb_comparam_det where CPD_ID=CS_CCNSTATUS) as CS_CCNSTATUS,(select CPD_DESC from tb_comparam_det where CPD_ID=CS_CCNSIZE) as CS_CCNSIZE,(select COD_DESC from tb_comparent_det where COD_ID=TRM_GROUP1) as CONNECTION_TYPE,  (sum(BD_CUR_TAXAMT)+SUM(BD_PRV_ARRAMT)) as TOTAL_DEMAND, sum(BD_PRV_ARRAMT) as ARREAR, (sum(BD_CUR_BAL_TAXAMT)+sum(BD_PRV_BAL_ARRAMT)+Penalty) as OUTSTANDING from  tb_csmr_info c ,  tb_wt_bill_det d, tb_wt_bill_mas e, (Select CS_IDN, SUM(SUR_AMOUNT_AS_ON_DATE) Penalty from tb_wt_penalty Group by CS_IDN) WP where c.cs_idn=d.cs_idn and C.cs_idn=WP.CS_IDN and d.BM_IDNO=e.BM_IDNO and e.BM_IDNO in (select max(BM_IDNO) from  tb_wt_bill_mas where CS_IDN=C.CS_IDN group by cs_idn) AND COD_DWZID2='"
						+ wardId + "' AND COD_DWZID3='" + zoneId + "' and date(BM_BILLDT) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "' group by d.cs_idn) s, (SELECT @row_number\\:=0) AS rn");
		waterDataWardZoneWiseEntity = (List<WaterDataWardZoneWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterDataWardZoneWiseEntity.class).getResultList();
		log.error("Qeury execution ended getWaterDataWardZoneWise");
		return waterDataWardZoneWiseEntity;
	}

	public List<WaterReceiptDataEntity> getWaterReceiptDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		List<WaterReceiptDataEntity> waterReceiptDataEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getWaterReceiptDataWardZoneWise");
		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select  ADDITIONAL_REF_NO, RM_RCPTID, RM_AMOUNT, RM_DEMAND, RM_RECEIVEDFROM from tb_receipt_mas RM,tb_csmr_info CS  where CAST(CS.CS_IDN AS CHAR)=RM.ADDITIONAL_REF_NO AND RM.SM_SERVICE_ID=18 and DP_DEPTID=5 and RM.ORGID=1 AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='Y' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL AND CS.COD_DWZID2='"
						+ wardId + "' AND CS.COD_DWZID3='" + zoneId + "' and Date(RM_DATE) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "') s, (SELECT @row_number\\:=0) AS rn");
		waterReceiptDataEntity = (List<WaterReceiptDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterReceiptDataEntity.class).getResultList();
		log.error("Qeury execution ended getWaterReceiptDataWardZoneWise");

		return waterReceiptDataEntity;
	}

	public List<WaterReceiptDetailDataEntity> getWaterReceiptDetailDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		List<WaterReceiptDetailDataEntity> waterReceiptDetailDataEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getWaterReceiptDetailDataWardZoneWise");
		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select RD.RM_RCPTID as RM_RCPTID, RD.TAX_ID as TAX_ID, RD.RM_DEMAND as RM_DEMAND, RD.RF_FEEAMOUNT as RF_FEEAMOUNT from tb_receipt_det RD, tb_receipt_mas RM, tb_csmr_info CS where CAST(CS.CS_IDN AS CHAR)=RM.ADDITIONAL_REF_NO AND RM.RM_RCPTID=RD.RM_RCPTID and RM.SM_SERVICE_ID=18 and DP_DEPTID=5 and RM.ORGID=1 AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='Y' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL AND CS.COD_DWZID2='"
						+ wardId + "' AND CS.COD_DWZID3='" + zoneId + "' and Date(RM_DATE) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "') s, (SELECT @row_number\\:=0) AS rn");
		waterReceiptDetailDataEntity = (List<WaterReceiptDetailDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterReceiptDetailDataEntity.class).getResultList();
		log.error("Qeury execution ended getWaterReceiptDetailDataWardZoneWise");

		return waterReceiptDetailDataEntity;
	}

	public List<WholePropertyDataEntity> getWholePropertyDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		List<WholePropertyDataEntity> wholePropertyDataEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getWholePropertyDataWardZoneWise");
		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select BM.*, AM.* from (Select MN_PROP_NO, (SUM(BD_CUR_TAXAMT) +SUM(BD_PRV_ARRAMT)) as TOTAL_DEMAND, SUM(BD_PRV_ARRAMT) as ARREARS, (SUM(CASE WHEN TAX_ID!=100000020 THEN BD_CUR_BAL_TAXAMT ELSE ifnull(penalty_as_on_date,BD_CUR_BAL_TAXAMT) END)+ SUM(BD_PRV_BAL_ARRAMT)) As TOTAL_OUTSTANDING  from tb_as_bill_mas BM, tb_as_bill_det BD, (select max(mn_BM_IDNO) as BM_ID from  tb_as_bill_mas BM where PD_FLATNO IS NULL OR PD_FLATNO='' Group by MN_PROP_NO) as Max_BMID where BM.MN_BM_IDNO=BD.BM_IDNO and (PD_FLATNO IS NULL OR PD_FLATNO='') and Max_BMID.BM_ID=BM.MN_BM_IDNO and date(MN_BILLDT) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "'  Group by MN_PROP_NO) As BM,(Select Distinct AM.MN_ASS_NO, AD.MN_FLAT_NO, MN_VILLAGE_MAUJA, (select COD_DESC from tb_comparent_det where COD_ID=MN_ASS_WARD1 )  WARD, (select COD_DESC from tb_comparent_det where COD_ID=MN_ASS_WARD2 ) ZONE,(Select CPD_DESC from tb_comparam_det where CPD_ID=PM_SERVICE_STATUS) as SERVICE_STATUS, MN_SURVEY_NUMBER, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASSD_FLOOR_NO) MN_ASSD_FLOOR_NO, MN_ASSD_BUILDUP_AREA, MN_ASSD_RV,  (select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASS_PROP_TYPE1)PROP_TYPE, (Select COD_DESC from tb_comparent_det where COD_ID=MN_ASSD_USAGETYPE1) as  MN_ASSD_USAGETYPE1 ,  (Select COD_DESC from tb_comparent_det where COD_ID=MN_ASSD_USAGETYPE2) MN_ASSD_USAGETYPE2, MN_LEGAL, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASS_OWNER_TYPE) MN_ASS_OWNER_TYPE, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_PROP_LVL_ROAD_TYPE) MN_PROP_LVL_ROAD_TYPE,MN_ASS_ACQ_DATE,MN_LSTASSNTDT,MN_AREA_NAME,MN_ASS_ADDRESS from tb_as_assesment_mast AM,tb_as_assesment_detail AD where AM.mn_ass_id=AD.mn_ass_id and  AM.CPD_BILLMETH=100000330  and  AM.MN_ASS_ID IN(Select MAX(MN_ASS_ID) from tb_as_assesment_mast where MN_ASS_NO=AM.MN_ASS_NO group by MN_ASS_NO,MN_FLAT_NO) AND AD.MN_ASSD_ID IN(SELECT MAX(MN_ASSD_ID) FROM tb_as_assesment_detail WHERE MN_ASS_ID=AM.MN_ASS_ID  Group by MN_FLAT_NO,MN_ASS_ID) AND MN_ASS_WARD1='"
						+ wardId + "' AND MN_ASS_WARD2='" + zoneId
						+ "' group by AM.MN_ASS_NO ,AD.MN_FLAT_NO, MN_VILLAGE_MAUJA,ward,zone,SERVICE_STATUS, MN_SURVEY_NUMBER,MN_ASSD_FLOOR_NO,MN_ASSD_BUILDUP_AREA, MN_ASSD_RV, PROP_TYPE,MN_ASSD_USAGETYPE1,MN_ASSD_USAGETYPE2,MN_LEGAL,MN_ASS_OWNER_TYPE, MN_PROP_LVL_ROAD_TYPE,MN_ASS_ACQ_DATE,MN_LSTASSNTDT,MN_AREA_NAME,MN_ASS_ADDRESS) As AM where MN_PROP_NO=MN_ASS_NO) s, (SELECT @row_number\\:=0) AS rn");
		wholePropertyDataEntity = (List<WholePropertyDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WholePropertyDataEntity.class).getResultList();
		log.error("Qeury execution ended getWholePropertyDataWardZoneWise");

		return wholePropertyDataEntity;
	}

	public List<IndividualPropertyDataEntity> getIndividualPropertyDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		List<IndividualPropertyDataEntity> individualPropertyDataEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getIndividualPropertyDataWardZoneWise");

		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (select MN_ASS_NO, (SUM(BD_CUR_TAXAMT) +SUM(BD_PRV_ARRAMT)) as TOTAL_DEMAND, SUM(BD_PRV_ARRAMT) as ARREARS, (SUM(CASE WHEN TAX_ID!=100000020 THEN BD_CUR_BAL_TAXAMT ELSE ifnull(penalty_as_on_date,BD_CUR_BAL_TAXAMT) END)+ SUM(BD_PRV_BAL_ARRAMT)) As TOTAL_OUTSTANDING, a.MN_FLAT_NO,MN_VILLAGE_MAUJA, (select COD_DESC from tb_comparent_det where COD_ID=MN_ASS_WARD1 ) WARD, (select COD_DESC from tb_comparent_det where COD_ID=MN_ASS_WARD2 ) ZONE,(Select CPD_DESC from tb_comparam_det where CPD_ID=PM_SERVICE_STATUS) as SERVICE_STATUS,MN_SURVEY_NUMBER, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASSD_FLOOR_NO)MN_ASSD_FLOOR_NO,MN_ASSD_BUILDUP_AREA,MN_ASSD_RV, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASS_PROP_TYPE1)PROP_TYPE,(select COD_DESC from tb_comparent_det where CoD_ID=MN_ASSD_USAGETYPE1)MN_ASSD_USAGETYPE1,(select COD_DESC from tb_comparent_det where CoD_ID=MN_ASSD_USAGETYPE2)MN_ASSD_USAGETYPE2,MN_LEGAL,(select CPD_DESC from tb_comparam_det where CPD_ID=MN_ASS_OWNER_TYPE) MN_ASS_OWNER_TYPE, (select CPD_DESC from tb_comparam_det where CPD_ID=MN_PROP_LVL_ROAD_TYPE) MN_PROP_LVL_ROAD_TYPE,MN_ASS_ACQ_DATE,MN_LSTASSNTDT,MN_AREA_NAME,MN_ASS_ADDRESS from tb_as_assesment_mast a,tb_as_assesment_detail b , tb_as_bill_mas bm, tb_as_bill_det bd, (select max(mn_BM_IDNO) as BM_ID from tb_as_bill_mas group by MN_PROP_NO,PD_FLATNO) as Max_BMID where a.mn_ass_id=b.mn_ass_id and A.MN_ASS_NO=BM.MN_PROP_NO and BM.PD_FLATNO=a.MN_FLAT_NO and A.CPD_BILLMETH=100000332 and bm.MN_BM_IDNO=bd.BM_IDNO and Max_BMID.BM_ID=BM.MN_BM_IDNO and a.MN_ASS_ID IN(Select MAX(MN_ASS_ID) from tb_as_assesment_mast where MN_ASS_NO=a.MN_ASS_NO group by MN_ASS_NO,MN_FLAT_NO) AND b.MN_ASSD_ID IN(SELECT MAX(MN_ASSD_ID) FROM tb_as_assesment_detail WHERE MN_ASS_ID=a.MN_ASS_ID Group by MN_FLAT_NO,MN_ASS_ID) and (PD_FLATNO IS NOT NULL OR PD_FLATNO!='') AND MN_ASS_WARD1='"
						+ wardId + "' AND MN_ASS_WARD2='" + zoneId + "' and date(MN_BILLDT) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "' group by A.MN_ASS_NO ,a.MN_FLAT_NO,MN_VILLAGE_MAUJA,ward,zone,SERVICE_STATUS, MN_SURVEY_NUMBER,MN_ASSD_FLOOR_NO,MN_ASSD_BUILDUP_AREA,MN_ASSD_RV,PROP_TYPE,MN_ASSD_USAGETYPE1,MN_ASSD_USAGETYPE2,MN_LEGAL,MN_ASS_OWNER_TYPE, MN_PROP_LVL_ROAD_TYPE,MN_ASS_ACQ_DATE,MN_LSTASSNTDT,MN_AREA_NAME,MN_ASS_ADDRESS) s ,(SELECT @row_number\\:=0) AS rn");
		individualPropertyDataEntity = (List<IndividualPropertyDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), IndividualPropertyDataEntity.class).getResultList();

		log.error("Qeury execution ended getIndividualPropertyDataWardZoneWise");

		return individualPropertyDataEntity;
	}

	public List<PropertyReceiptDataWiseEntity> getPropertyReceiptDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		List<PropertyReceiptDataWiseEntity> propertyReceiptDataWiseEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getPropertyReceiptDataWardZoneWise");

		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from ( Select DISTINCT ADDITIONAL_REF_NO, RM_RCPTID, RM_AMOUNT, RM_DEMAND, RM_RECEIVEDFROM from tb_receipt_mas RM, tb_as_assesment_mast AM where AM.MN_ASS_NO=RM.ADDITIONAL_REF_NO AND RM.SM_SERVICE_ID IN(179,30) and DP_DEPTID=6 and RM.ORGID=1 AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='Y' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL AND MN_ASS_WARD1='"
						+ wardId + "' AND MN_ASS_WARD2='" + zoneId + "' and Date(RM_DATE) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "') s, (SELECT @row_number\\:=0) AS rn");
		propertyReceiptDataWiseEntity = (List<PropertyReceiptDataWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), PropertyReceiptDataWiseEntity.class).getResultList();

		log.error("Qeury execution ended getPropertyReceiptDataWardZoneWise");

		return propertyReceiptDataWiseEntity;
	}

	public List<PropertyReceiptDetailDataEntity> getPropertyReceiptDetailDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		List<PropertyReceiptDetailDataEntity> propertyReceiptDetailDataEntity = null;
		StringBuilder queryBuilder = new StringBuilder();

		log.error("Qeury execution started getPropertyReceiptDetailDataWardZoneWise");

		queryBuilder
				.append("select @row_number\\:=@row_number+1 AS num, s.* from (Select DISTINCT RD.RM_RCPTID, RD.TAX_ID, RD.RM_DEMAND, RD.RF_FEEAMOUNT from tb_receipt_det RD, tb_receipt_mas RM, tb_as_assesment_mast AM where AM.MN_ASS_NO=RM.ADDITIONAL_REF_NO AND RM.RM_RCPTID=RD.RM_RCPTID and RM.SM_SERVICE_ID IN(179,30) and DP_DEPTID=6 and RM.ORGID=1 AND RM.RM_RCPTID not in (select distinct rm_rcptid from tb_receipt_mode where RD_SR_CHK_DIS='Y' and RM_RCPTID=RM.RM_RCPTID) and RM.RECEIPT_DEL_FLAG IS NULL AND MN_ASS_WARD1='"
						+ wardId + "' AND MN_ASS_WARD2='" + zoneId + "' and Date(RM_DATE) between '"
						+ fromDate.toString() + "' and '" + toDate.toString()
						+ "') s, (SELECT @row_number\\:=0) AS rn");
		propertyReceiptDetailDataEntity = (List<PropertyReceiptDetailDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), PropertyReceiptDetailDataEntity.class).getResultList();

		log.error("Qeury execution ended getPropertyReceiptDetailDataWardZoneWise");

		return propertyReceiptDetailDataEntity;
	}


	public Object[] getMarriageData(String regNo) {
	    log.error("Query execution started: getMarriageData");

	    StringBuilder queryBuilder = new StringBuilder();

	    queryBuilder.append(
	    	    "SELECT fn.MAR_ID, fn.APPLICATION_ID, fn.MAR_DATE, fn.SERIAL_NO, fn.hsFirst, fn.hsMiddle, fn.hsLast, ")
	    	    .append("fn.HUSBAND_ID, fn.wiFirst, fn.wiMiddle, fn.wiLast, fn.WIFE_ID, fn.STATUS, fn.ORGID, fn.APP_DATE, ")
	    	    .append("fn.firstname, fn.middlename, fn.lastname, fn.husDOB, fn.wiDOB, fn.IssueDate, fn.placeOfMarriage, ")
	    	    .append("fn.husbandUID, fn.wifeUID, fn.husbandAddress, fn.wifeAddress ") // ✅ Added here
	    	    .append("FROM ( ")
	    	    .append("SELECT mr.MAR_ID, mr.APPLICATION_ID, mr.MAR_DATE, mr.SERIAL_NO, mr.STATUS, mr.ORGID, mr.APP_DATE, mr.WARD1, ")
	    	    .append("hs.FIRST_NAME_E AS hsFirst, hs.MIDDLE_NAME_E AS hsMiddle, hs.LAST_NAME_E AS hsLast, hs.HUSBAND_ID, hs.UID_NO AS husbandUID, ")
	    	    .append("wi.FIRST_NAME_E AS wiFirst, wi.MIDDLE_NAME_E AS wiMiddle, wi.LAST_NAME_E AS wiLast, wi.WIFE_ID, wi.UID_NO AS wifeUID, ")
	    	    .append("ap.APM_FNAME AS firstname, ap.APM_MNAME AS middlename, ap.APM_FNAME AS lastname, ")
	    	    .append("hs.DOB as husDOB, wi.DOB as wiDOB, mr.REG_DATE as IssueDate, mr.PLACE_MAR_E as placeOfMarriage, ")
	    	    .append("hs.FULL_ADDR_E AS husbandAddress, wi.FULL_ADDR_E AS wifeAddress ") // ✅ Added here
	    	    .append("FROM TB_MRM_MARRIAGE mr ")
	    	    .append("LEFT JOIN TB_MRM_HUSBAND hs ON mr.MAR_ID = hs.MAR_ID ")
	    	    .append("LEFT JOIN TB_MRM_WIFE wi ON mr.MAR_ID = wi.MAR_ID ")
	    	    .append("LEFT JOIN TB_CFC_APPLICATION_MST ap ON mr.APPLICATION_ID = ap.APM_APPLICATION_ID ")
	    	    .append(") fn ")
	    	    .append("WHERE fn.SERIAL_NO = :regNo");



	    try {
	        Query query = entityManager.createNativeQuery(queryBuilder.toString());
	        query.setParameter("regNo", regNo);

	        List<Object[]> resultList = query.getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);

	    } catch (Exception e) {
	        log.error("Error executing getMarriageData query", e);
	        return null;
	    }
	 }
	
	public Object[] getBirthCertificateData(String regNo, String regDate, String dateOfBirth) {
	    log.error("Query execution started: getBirthCertificateData");

	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("SELECT ")
	                .append("BR.BR_ID, ")
	                .append("BR.BR_REGNO, ")
	                .append("BR.BR_REGDATE, ")
	                .append("BR.BR_CHILDNAME, ")
	                .append("BR.BR_DOB, ")
	                .append("BR.BR_SEX, ")
	                .append("BR.BR_BIRTHPLACE, ")
	                .append("BR.BR_BIRTHADDR, ")
	                .append("BR.BR_CERT_NO, ")
	                .append("PD.PD_FATHERNAME, ")
	                .append("PD.PD_MOTHERNAME, ")
	                .append("PD.PD_ADDRESS ")
	                .append("FROM tb_birthreg BR, TB_PARENTDETAIL PD ")
	                .append("WHERE BR.BR_ID = PD.BR_ID ")
	                .append("AND BR.ORGID = :orgId ")
	                .append("AND BR.BR_REGNO = :regNo ")
	                .append("AND BR.BR_REGDATE = :regDate ")
	                .append("AND BR.BR_DOB = :dateOfBirth");


	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        Date utilRegDate = sdf.parse(regDate);
	        java.sql.Date sqlRegDate = new java.sql.Date(utilRegDate.getTime());

	        Date utilDob = sdf.parse(dateOfBirth);
	        java.sql.Date sqlDob = new java.sql.Date(utilDob.getTime());

	        Query query = entityManager.createNativeQuery(queryBuilder.toString());
	        query.setParameter("orgId", 1L);  // You can change this to be dynamic
	        query.setParameter("regNo", regNo);
	        query.setParameter("regDate", sqlRegDate);
	        query.setParameter("dateOfBirth", sqlDob);

	        List<Object[]> resultList = query.getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);

	    } catch (Exception e) {
	        log.error("Error executing getBirthCertificateData query", e);
	        return null;
	    }
	}
	
	public Object[] getDeathCertiDataList(String regNo, String regDate, String drDod) {
	    log.error("Query execution started: getDeathCertiDataList");

	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("SELECT ")
	                .append("DR_ID, ")
	                .append("DR_REGNO, ")
	                .append("DR_REGDATE, ")
	                .append("DR_DECEASEDNAME, ")
	                .append("DR_DOD, ")
	                .append("DR_CERT_NO, ")
	                .append("dr_sex, ")
	                .append("DR_DEATHPLACE, ")
	                .append("DR_DEATHADDR, ")
	                .append("DR_INFORMANT_NAME, ")
	                .append("DR_INFORMANT_ADDR, ")
	                .append("DR_DECEASEDAGE, ")
	                .append("DR_RELATIVE_NAME, ")
	                .append("DR_MOTHER_NAME, ")
	                .append("DR_DECEASEDADDR, ")
	                .append("CPD_MARITAL_STAT_ID, ")
	                .append("DR_STATUS, ")
	                .append("LMODDATE ")
	                .append("FROM tb_deathreg ")
	                .append("WHERE ORGID = :orgId ")
	                .append("AND DR_REGNO = :regNo ")
	                .append("AND DR_REGDATE = :regDate ")
	                .append("AND DR_DOD = :drDod");

	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        Date utilRegDate = sdf.parse(regDate);
	        java.sql.Date sqlRegDate = new java.sql.Date(utilRegDate.getTime());

	        Date utilDod = sdf.parse(drDod);
	        java.sql.Date sqlDod = new java.sql.Date(utilDod.getTime());

	        Query query = entityManager.createNativeQuery(queryBuilder.toString());
	        query.setParameter("orgId", 1L); // You can make this dynamic if needed
	        query.setParameter("regNo", regNo);
	        query.setParameter("regDate", sqlRegDate);
	        query.setParameter("drDod", sqlDod);

	        List<Object[]> resultList = query.getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);

	    } catch (Exception e) {
	        log.error("Error executing getDeathCertiDataList query", e);
	        return null;
	    }
	}

	
	
	@SuppressWarnings("unchecked")
	public List<PropertyStatisticsServiceDepartmentEntity> getPropertyStatisticsServiceWise(String startDate, String endDate) {
	    
	    List<PropertyStatisticsServiceDepartmentEntity> propertyStatisticsServiceDepartmentEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (select department_desc_new,sdd.SM_SERVICE_NAME as newServiceName,yy.* from (select SM_SERVICE_NAME,(select dep.DP_DEPTDESC from tb_department dep where dep.DP_DEPTID=CDM_DEPT_ID) department_desc_new from tb_services_mst sm join tb_department dp on dp.DP_DEPTID=sm.CDM_DEPT_ID where sm.SM_SERV_ACTIVE=40 and sm.SM_SHORTDESC not in ('DES','MARS','MRC','CIB','PBP','BPW') and dp.DP_DEPTCODE in ('CFC','BND','WT','AS','ML','MRM') )sdd left join (Select z.department_desc,z.ServiceName,(SUM(COALESCE(z.BeyondCLOSED,0))+SUM(COALESCE(z.WithinCLOSED,0))+SUM(COALESCE(z.HOLD,0))+SUM(COALESCE(z.BeyondPending,0))+SUM(COALESCE(z.WithinPending,0))+SUM(COALESCE(z.REJECTED,0))) AS Recieved,(SUM(COALESCE(z.BeyondCLOSED,0))+SUM(COALESCE(z.WithinCLOSED,0))) AS accepted,SUM(COALESCE(z.BeyondCLOSED,0)) AS BeyondCLOSED,SUM(COALESCE(z.WithinCLOSED,0)) AS WithinCLOSED,SUM(COALESCE(z.BeyondPending,0)) AS BeyondPending,(SUM(COALESCE(z.WithinPending,0))+SUM(COALESCE(z.HOLD,0))) AS WithinPending,(SUM(COALESCE(z.REJECTED,0))) AS UnSolvable from (select department_desc,SM_SERVICE_NAME as  ServiceName,cr.STATUS status, COUNT(CASE WHEN cr.STATUS='CLOSED' and cr.SLA='B' THEN APM_APPLICATION_ID END) AS BeyondCLOSED,count( CASE WHEN cr.STATUS='CLOSED' and cr.SLA='W' THEN APM_APPLICATION_ID END ) AS WithinCLOSED,count(CASE WHEN cr.STATUS='HOLD' THEN APM_APPLICATION_ID END ) AS HOLD,count( CASE WHEN cr.STATUS='PENDING' and cr.SLA='B' THEN APM_APPLICATION_ID END ) AS BeyondPending,count( CASE WHEN cr.STATUS='PENDING' and cr.SLA='W' THEN APM_APPLICATION_ID END) AS WithinPending,count( CASE WHEN cr.STATUS='REJECTED' THEN APM_APPLICATION_ID END) AS REJECTED from(select DISTINCT tb2.APM_APPLICATION_ID,tb7.SM_SERVICE_NAME,dp.DP_DEPTDESC as department_desc,(CASE WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (tb4.LAST_DECISION = 'REJECTED')) THEN 'REJECTED' WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') = 'CLOSED') AND (tb4.LAST_DECISION <> 'REJECTED')) THEN 'CLOSED'WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (tb4.LAST_DECISION = 'HOLD')) THEN 'HOLD' WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') = 'PENDING') AND (tb4.LAST_DECISION <> 'HOLD')) THEN 'PENDING' END) AS STATUS,(CASE WHEN((tb4.STATUS = 'PENDING') AND (TIMESTAMPDIFF(SECOND,tb4.DATE_OF_REQUEST,NOW()) >=(CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000) <> 0) THEN ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000)ELSE (SELECT (SM_SERDUR / 1000) FROM tb_services_mst WHERE (SM_SERVICE_ID = tb5.SM_SERVICE_ID)) END))) THEN 'B'WHEN ((tb4.STATUS = 'PENDING') AND (TIMESTAMPDIFF(SECOND,tb4.DATE_OF_REQUEST,NOW()) <(CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000) <> 0)THEN ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000)ELSE (SELECT (SM_SERDUR / 1000) FROM tb_services_mst WHERE (SM_SERVICE_ID = tb5.SM_SERVICE_ID)) END)))THEN 'W'WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') IN ('CLOSED' , 'EXPIRED')) AND (TIMESTAMPDIFF(SECOND,tb4.DATE_OF_REQUEST,tb4.LAST_DATE_OF_ACTION) >(CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000) <> 0)THEN ((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000)ELSE (SELECT (SM_SERDUR / 1000) FROM tb_services_mst WHERE (SM_SERVICE_ID = tb5.SM_SERVICE_ID)) END))) THEN 'B' WHEN ((REPLACE(tb4.STATUS,'EXPIRED','CLOSED') IN ('CLOSED' , 'EXPIRED')) AND(TIMESTAMPDIFF(SECOND,tb4.DATE_OF_REQUEST,tb4.LAST_DATE_OF_ACTION) <= (CASE WHEN (((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.wf_id)) / 1000) <> 0) THEN((SELECT SUM(COALESCE(SLA_CAL, 0)) FROM tb_workflow_det WHERE (WF_ID = tb5.WF_ID)) / 1000)ELSE (SELECT (SM_SERDUR / 1000) FROM tb_services_mst WHERE (SM_SERVICE_ID = tb5.SM_SERVICE_ID)) END)))THEN 'W' END) AS SLA from tb_cfc_application_mst tb2 JOIN tb_services_mst tb7 ON tb2.SM_SERVICE_ID =tb7.SM_SERVICE_ID join  tb_department dp on dp.DP_DEPTID=tb7.CDM_DEPT_ID JOIN TB_WORKFLOW_REQUEST tb4 ON tb2.APM_APPLICATION_ID= tb4.APM_APPLICATION_ID LEFT JOIN tb_workflow_mas tb5 ON  tb5.WF_ID = tb4.WORFLOW_TYPE_ID where tb7.SM_SERV_ACTIVE=40 and tb7.SM_SHORTDESC not in ('IDC','IBC','DUB','WND','DES','MARS','MRC','CIB','PBP','BPW', 'ADJ','PBD') and dp.DP_DEPTCODE in ('CFC','BND','WT','AS','ML','MRM')and tb4.ORGID=1 and (tb4.DATE_OF_REQUEST) between concat(str_to_date('"+startDate+"', '%d-%m-%Y'),' ','00:00:00') AND concat(str_to_date('"+endDate+"' , '%d-%m-%Y'),' ','23:59:59')AND tb4.WORKFLOW_REQ_ID NOT IN (select C.WORKFLOW_REQ_ID FROM tb_workflow_request C, tb_workflow_task D WHERE C.WORKFLOW_REQ_ID=D.WORKFLOW_REQ_ID AND D.SM_SERVICE_ID=32 AND C.STATUS='PENDING' and D.TASK_STATUS='PENDING' AND D.TASK_NAME='LOI PAYMENT' AND D.TASK_ID IN(SELECT MAX(TASK_ID) FROM tb_workflow_task WHERE WORKFLOW_REQ_ID=D.WORKFLOW_REQ_ID GROUP BY WORKFLOW_REQ_ID))) cr Group BY department_desc, ServiceName,Status,SLA)z GROUP BY z.department_desc,z.ServiceName UNION ALL Select z.department_desc,z.ServiceName,(SUM(COALESCE(z.WithinCLOSED,0))) AS Recieved,(SUM(COALESCE(z.WithinCLOSED,0))) AS accepted,0 AS BeyondCLOSED,(SUM(COALESCE(z.WithinCLOSED,0))) AS WithinCLOSED,0 AS BeyondPending,0 AS WithinPending,0 AS UnSolvable from(select distinct dp.DP_DEPTDESC department_desc,tb7.SM_SERVICE_NAME as ServiceName,'' as status,0 AS BeyondCLOSED,count(APM_APPLICATION_ID) WithinCLOSED, 0 AS HOLD,0 AS BeyondPending,0 AS WithinPending,0 AS REJECTED from tb_cfc_application_mst tb2 join tb_services_mst tb7 on  tb2.SM_SERVICE_ID =tb7.SM_SERVICE_ID JOIN tb_department DP ON DP.DP_DEPTID=tb7.CDM_DEPT_ID where tb7.SM_SHORTDESC in ('EXT','NDT','IDC','WND','IBC','NDT','ACG','DUB','PNDT','WDB','DRP') and DP_DEPTCODE in ('CFC','BND','WT','AS','ML','MRM')and tb2.ORGID=1 and (tb2.APM_APPLICATION_DATE) between concat(str_to_date('"+startDate+"', '%d-%m-%Y'),' ','00:00:00') AND concat(str_to_date('"+endDate+"', '%d-%m-%Y'),' ','23:59:59')group by  dp.DP_DEPTDESC ,tb7.SM_SERVICE_NAME) z GROUP BY z.department_desc,z.ServiceName)yy on sdd.SM_SERVICE_NAME=yy.ServiceName)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
	    propertyStatisticsServiceDepartmentEntity = (List<PropertyStatisticsServiceDepartmentEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), PropertyStatisticsServiceDepartmentEntity.class).getResultList();

	    log.error("Qeury execution ended getPropertyStatisticsServiceWise");
	    
	    return propertyStatisticsServiceDepartmentEntity;
	}

	
	@SuppressWarnings("unchecked")
	public List<ModeWiseCollectionReportEntity> getmodeWiseCollectionReport(String startDate, String endDate) {
	    
	    List<ModeWiseCollectionReportEntity> modeWiseCollectionReportEntityList = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT Z.New_mode AS mode,New_mode_value, sum(ifnull(Z.modeamt,0)) modeamt FROM (Select c.cpd_desc as mode, sum(ifnull(b.RD_AMOUNT,0)) modeamt, (CASE WHEN C.cpd_value='W' and OT.PG_SOURCE='ICICI(TECHPROCESS)' then 'WEB ICICI(TECHPROCESS)' when C.cpd_value='W' and OT.PG_SOURCE='IDBI(ISGPay)' then 'WEB IDBI(ISGPay)' when C.cpd_value='W' and OT.PG_SOURCE='GETEPAY' THEN 'WEB GETEPAY' when C.cpd_value='W' and OT.PG_SOURCE='IDBI(GetePay)' THEN 'WEB IDBI(GetePay)' ELSE C.cpd_Desc END ) AS New_mode, (CASE WHEN C.cpd_value='W' and OT.PG_SOURCE='ICICI(TECHPROCESS)' then 'ICICI(TECHPROCESS)' when C.cpd_value='W' and OT.PG_SOURCE='IDBI(ISGPay)' then 'IDBI(ISGPay)' when C.cpd_value='W' and OT.PG_SOURCE='GETEPAY' THEN 'GETEPAY' when C.cpd_value='W' and OT.PG_SOURCE='IDBI(GetePay)' THEN 'IDBI(GetePay)' ELSE C.cpd_value END ) AS New_mode_value from tb_receipt_mas A INNER JOIN tb_receipt_mode B ON A.RM_RCPTID = B.RM_RCPTID INNER JOIN tb_comparam_det c ON c.cpd_id=B.CPD_FEEMODE INNER JOIN tb_department D ON D.DP_DEPTID = A.DP_DEPTID LEFT JOIN tb_onl_tran_mas_service OT ON OT.TRAN_CM_ID=A.PG_REFID Where A.OrgID=1 and COALESCE(D.DP_DEPTID,0)=(case when COALESCE(0,0)=0 then COALESCE(D.DP_DEPTID,0) else COALESCE(0,0) end) AND (A.RM_DATE) between str_to_date('"+startDate+"','%d-%m-%Y') and str_to_date('"+endDate+"','%d-%m-%Y') AND coalesce(a.RECEIPT_DEL_FLAG,'N')='N' AND a.RECEIPT_TYPE_FLAG in ('M','R','A','P') and c.cpd_value in ('B','C','W','POS','D','Q','P','PC') group by c.cpd_desc ,A.PG_REFID,OT.PG_SOURCE,C.cpd_value)Z WHERE COALESCE(Z.New_mode_value,'X')=(case when COALESCE('X','X')='X' then COALESCE(Z.New_mode_value,'X') else COALESCE('X','X') end) and New_mode!='WEB' GROUP BY New_mode ,New_mode_value)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
	    modeWiseCollectionReportEntityList = (List<ModeWiseCollectionReportEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ModeWiseCollectionReportEntity.class).getResultList();

	    log.error("Qeury execution ended getPropertyStatisticsServiceWise");
	    
	    return modeWiseCollectionReportEntityList;
	}
	

	@SuppressWarnings("unchecked")
	public List<SummaryCollectionDataReportEntity> getSummaryCollectionList(String startDate, String endDate, String ward, String zone, String tarrifType1,String tarrifType2, String tarrifType3, String meterType, String conSize) {
	    
	    List<SummaryCollectionDataReportEntity> summaryCollectionDataReportEntityList = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    log.error("Qeury execution Started for getSummaryCollectionReportList");
	    
	    queryBuilder.append("SELECT @row_number\\:=@row_number+1 AS num, s.* FROM (SELECT (SELECT cod_Desc FROM tb_comparent_det WHERE cod_id = CSMR.COD_DWZID2) AS Wards, (SELECT cod_value FROM tb_comparent_det WHERE cod_id = CSMR.COD_DWZID3) AS Zones, SUM(IFNULL(RF_FEEAMOUNT, 0)) AS TotalCollection FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID INNER JOIN tb_csmr_info CSMR ON CSMR.CS_IDN = RM.ADDITIONAL_REF_NO INNER JOIN tb_department DP ON DP.DP_DEPTID = RM.DP_DEPTID AND DP.DP_DEPTCODE = 'WT' WHERE CSMR.ORGID = 1 AND COALESCE(CSMR.COD_DWZID2,0) = (CASE WHEN COALESCE('" + ward + "',0) = 0 THEN COALESCE(CSMR.COD_DWZID2,0) ELSE COALESCE('" + ward + "',0) END) AND COALESCE(CSMR.COD_DWZID3,0) = (CASE WHEN COALESCE('" + zone + "',0) = 0 THEN COALESCE(CSMR.COD_DWZID3,0) ELSE COALESCE('" + zone + "',0) END) AND COALESCE(CSMR.TRM_GROUP1,0) = (CASE WHEN COALESCE('" + tarrifType1 + "',0) = 0 THEN COALESCE(CSMR.TRM_GROUP1,0) ELSE COALESCE('" + tarrifType1 + "',0) END) AND COALESCE(CSMR.TRM_GROUP2,0) = (CASE WHEN COALESCE('" + tarrifType2 + "',0) = 0 THEN COALESCE(CSMR.TRM_GROUP2,0) ELSE COALESCE('" + tarrifType2 + "',0) END) AND COALESCE(CSMR.TRM_GROUP3,0) = (CASE WHEN COALESCE('" + tarrifType3 + "',0) = 0 THEN COALESCE(CSMR.TRM_GROUP3,0) ELSE COALESCE('" + tarrifType3 + "',0) END) AND COALESCE(CSMR.cs_meteredccn,0) = (CASE WHEN COALESCE('" + meterType + "',0) = 0 THEN COALESCE(CSMR.cs_meteredccn,0) ELSE COALESCE('" + meterType + "',0) END) AND COALESCE(CSMR.cs_ccnsize,0) = (CASE WHEN COALESCE('" + conSize + "',0) = 0 THEN COALESCE(CSMR.cs_ccnsize,0) ELSE COALESCE('" + conSize + "',0) END) AND RM.RM_DATE BETWEEN STR_TO_DATE('" + startDate + "', '%d-%m-%Y') AND STR_TO_DATE('" + endDate + "', '%d-%m-%Y') AND RM.RECEIPT_DEL_FLAG IS NULL GROUP BY CSMR.COD_DWZID2, CSMR.COD_DWZID3)"
	    		+ " s, (SELECT @row_number\\:=0) AS rn");

	    
	    summaryCollectionDataReportEntityList = (List<SummaryCollectionDataReportEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), SummaryCollectionDataReportEntity.class).getResultList();

	    log.error("Qeury execution ended getSummaryCollectionReportList");
	    
	    return summaryCollectionDataReportEntityList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<WardwisePropertyCollectionReportEntity> getwardWiseCollectionPropertyList(String startDate, String endDate, String wardId, String zoneId) {
	    
	    List<WardwisePropertyCollectionReportEntity> wardwisePropertyCollectionReportEntityList = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT COD.COD_DESC AS WARD, SUM(CASE WHEN BM.MN_BM_IDNO IS NULL AND TM.TAX_ID IS NULL THEN RD.RF_FEEAMOUNT ELSE 0 END) AS ARREARS_COLLECTION, SUM((CASE WHEN BM.MN_BM_IDNO IS NOT NULL AND TM.TAX_ID IS NULL THEN RD.RF_FEEAMOUNT ELSE 0 END) + (CASE WHEN BM.MN_BM_IDNO IS NULL AND TM.TAX_ID IS NOT NULL THEN RD.RF_FEEAMOUNT ELSE 0 END)) AS CURRENT_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID INNER JOIN tb_receipt_mode RMO ON RMO.RM_RCPTID = RM.RM_RCPTID INNER JOIN LATEST_ASSESSMENT_ID AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO INNER JOIN tb_comparent_det COD ON COD.COD_ID = AM.WARD LEFT JOIN tb_tax_mas TM ON TM.TAX_ID = RD.TAX_ID AND TM.TAX_CODE = 'AS89' LEFT JOIN tb_as_bill_mas BM ON BM.MN_BM_IDNO = RD.BM_IDNO AND BM.MN_BM_YEAR = (SELECT FA_YEARID FROM tb_financialyear WHERE FA_FROMDATE <= '" + startDate + "' AND FA_TODATE >= '" + endDate + "') WHERE RM.ORGID = '1' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.RECEIPT_TYPE_FLAG IN ('R','A') AND RM.RM_DATE >= '" + startDate + "' AND RM.RM_DATE < DATE_ADD('" + endDate + "', INTERVAL 1 DAY) AND RD.TAX_ID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM tb_comparam_det CPD INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID = CPD.CPM_ID WHERE CPD.CPD_ID = RMO.RD_CHEQUE_STATUS AND CPD.CPD_VALUE = 'DSH' AND CPM.CPM_PREFIX = 'CLR') AND ('" + wardId + "' =0 OR AM.WARD= '" + wardId + "') AND ('" + zoneId + "' =0 OR AM.ZONE= '" + zoneId + "') GROUP BY COD.COD_DESC ORDER BY COD.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");	    
	    wardwisePropertyCollectionReportEntityList = (List<WardwisePropertyCollectionReportEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WardwisePropertyCollectionReportEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return wardwisePropertyCollectionReportEntityList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ZoneWisePropertyCollectionReportEntity> getZoneWiseCollectionPropertyList(String startDate, String endDate, String wardId, String zoneId) {
	    
	    List<ZoneWisePropertyCollectionReportEntity> zoneWisePropertyCollectionReportEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT CODZ.COD_DESC AS ZONE, SUM(CASE WHEN BM.MN_BM_IDNO IS NULL AND TM.TAX_ID IS NULL THEN RD.RF_FEEAMOUNT ELSE 0 END) AS ARREARS_COLLECTION, SUM((CASE WHEN BM.MN_BM_IDNO IS NOT NULL AND TM.TAX_ID IS NULL THEN RD.RF_FEEAMOUNT ELSE 0 END) + (CASE WHEN BM.MN_BM_IDNO IS NULL AND TM.TAX_ID IS NOT NULL THEN RD.RF_FEEAMOUNT ELSE 0 END)) AS CURRENT_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID INNER JOIN tb_receipt_mode RMO ON RMO.RM_RCPTID = RM.RM_RCPTID INNER JOIN LATEST_ASSESSMENT_ID AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO INNER JOIN tb_comparent_det CODW ON CODW.COD_ID = AM.WARD INNER JOIN tb_comparent_det CODZ ON CODZ.COD_ID = AM.ZONE AND CODZ.PARENT_ID = CODW.COD_ID LEFT JOIN tb_tax_mas TM ON TM.TAX_ID = RD.TAX_ID AND TM.TAX_CODE = 'AS89' LEFT JOIN tb_as_bill_mas BM ON BM.MN_BM_IDNO = RD.BM_IDNO AND BM.MN_BM_YEAR = (SELECT FA_YEARID FROM tb_financialyear WHERE FA_FROMDATE <= '" + startDate + "' AND FA_TODATE >= '" + endDate + "') WHERE RM.ORGID = '1' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.RECEIPT_TYPE_FLAG IN ('R','A') AND RM.RM_DATE >= '" + startDate + "' AND RM.RM_DATE < DATE_ADD('" + endDate + "', INTERVAL 1 DAY) AND RD.TAX_ID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM tb_comparam_det CPD INNER JOIN tb_comparam_mas CPM ON CPM.CPM_ID = CPD.CPM_ID WHERE CPD.CPD_ID = RMO.RD_CHEQUE_STATUS AND CPD.CPD_VALUE = 'DSH' AND CPM.CPM_PREFIX = 'CLR') AND AM.WARD = '" + wardId + "' AND ('" + zoneId + "' = 0 OR AM.ZONE = '" + zoneId + "') GROUP BY CODZ.COD_DESC ORDER BY CODZ.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");	    
	    zoneWisePropertyCollectionReportEntity = (List<ZoneWisePropertyCollectionReportEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ZoneWisePropertyCollectionReportEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return zoneWisePropertyCollectionReportEntity;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DailyPropertyCollectionReportEntity> getDailyPropertyCollectionList() {
	    
	    List<DailyPropertyCollectionReportEntity> dailyPropertyCollectionReportEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT SUM(IFNULL(RD.RF_FEEAMOUNT, 0)) AS DAILY_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN tb_department DP ON DP.DP_DEPTID=RM.DP_DEPTID AND DP_DEPTCODE='AS' WHERE RM.ORGID=1 AND RM.RM_DATE=CURRENT_DATE() AND RM.RECEIPT_DEL_FLAG IS NULL AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RD.TAX_ID IS NOT NULL AND (RM.RECEIPT_TYPE_FLAG IN ('R','A') OR RM.RECEIPT_TYPE_FLAG IS NULL))"
				+ "s, (SELECT @row_number\\:=0) AS rn");	    
	    dailyPropertyCollectionReportEntity = (List<DailyPropertyCollectionReportEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DailyPropertyCollectionReportEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return dailyPropertyCollectionReportEntity;
	}
	
	public Object[] getPropAssessmentCertifiacteData(String appId, String flatNo) {
	    log.info("Query execution started: getPropAssessmentCertifiacteData");

	    StringBuilder queryBuilder = new StringBuilder();

	    queryBuilder.append("SELECT ")
	    .append("PROP_NO, APPLICATION_DATE, APPLICANT_NAME, SM_SERVICE_NAME, TODAYS_DATE, Ward, zones, ")
	    .append("(CASE WHEN CPD_BILLMETH=100000330 THEN OWNER_NAME ELSE Flat_Ownername END) OWNER_NAME, ")
	    .append("CONSTRUCTION_TYPE, O_NLS_ORGNAME, ASSESSMENT_ADDRESS, APM_APPLICATION_ID_sk, certificateNo, MN_FLAT_NO, CPD_BILLMETH, ")
	    .append("(CASE WHEN COUNT(DISTINCT USAGE_TYPE)!=1 THEN 'MixUsage' ELSE GROUP_CONCAT(DISTINCT USAGE_TYPE) END) USAGE_TYPE, ")
	    .append("SUM(IFNULL(ARV,0)) AS ARV, MOBILE_NUMBER ")
	    .append("FROM ( ")
	    .append("SELECT AM.MN_ASS_no AS PROP_NO, ")
	    .append("DATE_FORMAT(APM.APM_APPLICATION_DATE, '%d/%m/%Y') AS APPLICATION_DATE, ")
	    .append("CONCAT(IFNULL(APM.APM_FNAME,''),' ',IFNULL(APM.APM_MNAME,''),' ',IFNULL(APM.APM_LNAME,'')) AS APPLICANT_NAME, ")
	    .append("SM.SM_SERVICE_NAME, DATE_FORMAT(CURRENT_DATE(), '%d/%m/%Y') AS TODAYS_DATE, ")
	    .append("(SELECT GROUP_CONCAT(DISTINCT IFNULL(MN_asso_owner_name, '') SEPARATOR '\\n') FROM tb_as_assesment_owner_dtl WHERE AM.MN_ASS_ID=MN_ASS_ID) AS OWNER_NAME, ")
	    .append("(CASE WHEN AM.CPD_BILLMETH=100000332 THEN GROUP_CONCAT(DISTINCT AD.MN_ASSD_OCCUPIER_NAME) END) AS Flat_Ownername, ")
	    .append("(CASE WHEN AM.CPD_BILLMETH=100000332 THEN AD.MN_ASSE_MOBILENO ELSE (SELECT GROUP_CONCAT(DISTINCT IFNULL(MN_ASSO_MOBILENO, '') SEPARATOR '\\n') FROM tb_as_assesment_owner_dtl WHERE AM.MN_ASS_ID=MN_ASS_ID) END) AS MOBILE_NUMBER, ")
	    .append("D1.COD_DESC AS USAGE_TYPE, D2.CPD_DESC AS CONSTRUCTION_TYPE, ")
	    .append("SUM(COALESCE(AD.MN_assd_rv, 0)) AS ARV, ORG.O_NLS_ORGNAME, AM.MN_ASS_address AS ASSESSMENT_ADDRESS, ")
	    .append("APM.APM_APPLICATION_ID AS APM_APPLICATION_ID_sk, ND.OUTWARD_NO AS certificateNo, ")
	    .append("(CASE WHEN AM.CPD_BILLMETH=100000332 THEN AM.MN_FLAT_NO ELSE '' END) AS MN_FLAT_NO, AM.CPD_BILLMETH, ")
	    .append("(SELECT cod_desc FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD1) AS Ward, ")
	    .append("(SELECT cod_desc FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD2) AS zones ")
	    .append("FROM tb_as_assesment_mast AM ")
	    .append("INNER JOIN tb_as_assesment_detail AD ON AM.MN_ASS_id = AD.MN_ass_id AND AM.ORGID = AD.orgid ")
	    .append("INNER JOIN tb_as_no_dues_prop_details ND ON ND.PROP_NO = AM.MN_ASS_no ")
	    .append("INNER JOIN tb_cfc_application_mst APM ON APM.APM_APPLICATION_ID = ND.APPLICATION_ID ")
	    .append("INNER JOIN tb_services_mst SM ON SM.SM_SERVICE_ID = APM.SM_SERVICE_ID AND SM.SM_SHORTDESC = 'ACG' ")
	    .append("INNER JOIN tb_comparent_det D1 ON D1.COD_ID = AD.MN_assd_usagetype1 ")
	    .append("INNER JOIN tb_comparam_det D2 ON D2.CPD_ID = AD.NATUREOFPROPERTY1 ")
	    .append("INNER JOIN tb_organisation ORG ON ORG.orgid = AM.orgid ")
	    .append("WHERE AM.ORGID = :orgId ")
	    .append("AND APM.APM_APPLICATION_ID = :appId ")
	    .append("AND COALESCE(AM.MN_FLAT_NO, 'X') = (CASE WHEN COALESCE(:flatNo, 'X') = 'X' THEN COALESCE(AM.MN_FLAT_NO, 'X') ELSE COALESCE(:flatNo, 'X') END) ")
	    .append("AND AM.MN_ASS_ID IN ( ")
	    .append("SELECT MAX(MN_ASS_ID) FROM tb_as_assesment_mast ")
	    .append("WHERE MN_ASS_no = AM.MN_ASS_no AND ")
	    .append("CASE WHEN AM.CPD_BILLMETH = 100000332 THEN MN_FLAT_NO = AM.MN_FLAT_NO ELSE CPD_BILLMETH = 100000330 END ")
	    .append("GROUP BY MN_ASS_NO, MN_FLAT_NO ")
	    .append(") ")
	    .append("AND EXISTS ( ")
	    .append("SELECT MAX(WORKFLOW_REQ_ID) FROM tb_workflow_request ")
	    .append("WHERE LAST_DECISION = 'APPROVED' AND STATUS = 'CLOSED' AND APM_APPLICATION_ID = APM.APM_APPLICATION_ID ")
	    .append(") ")
	    .append("AND AD.MN_ASSD_ID IN ( ")
	    .append("SELECT MAX(MN_ASSD_ID) FROM tb_as_assesment_detail WHERE MN_ASS_ID = AM.MN_ASS_ID GROUP BY MN_FLAT_NO, MN_ASS_ID ")
	    .append(") ")
	    .append("GROUP BY AM.MN_ASS_no, APM.APM_APPLICATION_DATE, APM.APM_FNAME, APM.APM_MNAME, APM.APM_LNAME, SM.SM_SERVICE_NAME, ")
	    .append("TODAYS_DATE, AM.MN_ASS_no, D1.COD_DESC, D2.CPD_DESC, ORG.O_NLS_ORGNAME, MOBILE_NUMBER, AM.MN_ASS_address, ")
	    .append("APM.APM_APPLICATION_ID, ND.OUTWARD_NO, AM.MN_FLAT_NO, AM.CPD_BILLMETH, Ward, zones, AM.MN_ASS_ID, AD.MN_ASSD_OCCUPIER_NAME ")
	    .append(") Z ")
	    .append("GROUP BY PROP_NO, APPLICATION_DATE, APPLICANT_NAME, SM_SERVICE_NAME, TODAYS_DATE, OWNER_NAME, Flat_Ownername, ")
	    .append("CONSTRUCTION_TYPE, O_NLS_ORGNAME, ASSESSMENT_ADDRESS, APM_APPLICATION_ID_sk, certificateNo, MN_FLAT_NO, CPD_BILLMETH, Ward, zones, MOBILE_NUMBER");


	    try {
	        Query query = entityManager.createNativeQuery(queryBuilder.toString());
	        query.setParameter("orgId", 1L); // You can make this dynamic if required
	        query.setParameter("appId", appId);
	        query.setParameter("flatNo", flatNo);

	        List<Object[]> resultList = query.getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);

	    } catch (Exception e) {
	        log.error("Error executing getPropAssessmentCertifiacteData query", e);
	        return null;
	    }
	}
	
	public List<SlaWiseGrievanceSumaaryEntity> getGrieveanceSummryReport(
	        String fromDateStr, String toDateStr, String deptIdStr, String subTypeIdStr,
	        String codIdLevel1, String codIdLevel2, String codIdLevel3, String codIdLevel4, String codIdLevel5, String status) {

	    List<SlaWiseGrievanceSumaaryEntity> slaWiseGrievanceSumaaryEntity;

	    StringBuilder builder = new StringBuilder();
	    builder.append("SELECT ROW_NUMBER() OVER (ORDER BY department_descrption, COMPLAINT_SUB_TYPE) AS num, s.* FROM (")
	           .append("SELECT department_descrption, COMPLAINT_SUB_TYPE, ")
	           .append("BEYOND_SLA_CLOSED AS BeyoundCLOSED, WITHIN_SLA_CLOSED AS WithinCLOSED, ")
	           .append("BEYOND_SLA_PENDING AS BeyoundPending, WITHIN_SLA_PENDING AS WithinPending, ")
	           .append("(BEYOND_SLA_CLOSED + WITHIN_SLA_CLOSED + BEYOND_SLA_PENDING + WITHIN_SLA_PENDING) AS TotalRecieved ")
	           .append("FROM (")
	           .append("SELECT ")
	           .append("(SELECT dept.DP_DEPTDESC FROM tb_department dept WHERE dept.DP_DEPTID = B.dept_comp_id) AS department_descrption, ")
	           .append("(SELECT DEPTS.COMP_TYPE_DESC FROM tb_dep_complaint_subtype DEPTS WHERE DEPTS.COMP_ID = B.COMP_SUBTYPE_ID) AS COMPLAINT_SUB_TYPE, ")

	           .append("COUNT(CASE WHEN (REPLACE(A.STATUS, 'EXPIRED', 'CLOSED') = 'PENDING') ")
	           .append("AND A.LAST_DECISION <> 'HOLD' ")
	           .append("AND WT.TASK_SLA_DURATION IS NOT NULL AND WT.TASK_SLA_DURATION != 0 ")
	           .append("THEN A.APM_APPLICATION_ID END) AS WITHIN_SLA_PENDING, ")

	           .append("COUNT(CASE WHEN (REPLACE(A.STATUS, 'EXPIRED', 'CLOSED') = 'PENDING') ")
	           .append("AND A.LAST_DECISION <> 'HOLD' ")
	           .append("AND (WT.TASK_SLA_DURATION = 0 OR WT.TASK_SLA_DURATION IS NULL) ")
	           .append("THEN A.APM_APPLICATION_ID END) AS BEYOND_SLA_PENDING, ")

	           .append("COUNT(CASE WHEN (REPLACE(A.STATUS, 'EXPIRED', 'CLOSED') = 'CLOSED') ")
	           .append("AND A.LAST_DECISION <> 'HOLD' ")
	           .append("AND WT.TASK_SLA_DURATION IS NOT NULL AND WT.TASK_SLA_DURATION != 0 ")
	           .append("THEN A.APM_APPLICATION_ID END) AS WITHIN_SLA_CLOSED, ")

	           .append("COUNT(CASE WHEN (REPLACE(A.STATUS, 'EXPIRED', 'CLOSED') = 'CLOSED') ")
	           .append("AND A.LAST_DECISION <> 'HOLD' ")
	           .append("AND (WT.TASK_SLA_DURATION = 0 OR WT.TASK_SLA_DURATION IS NULL) ")
	           .append("THEN A.APM_APPLICATION_ID END) AS BEYOND_SLA_CLOSED ")

	           .append("FROM tb_care_request B ")
	           .append("JOIN tb_organisation C ON B.orgid = C.orgid ")
	           .append("JOIN TB_WORKFLOW_REQUEST A ON B.apm_application_id = A.APM_APPLICATION_ID ")
	           .append("JOIN tb_workflow_mas tb5 ON tb5.WF_ID = A.WORFLOW_TYPE_ID ")
	           .append("JOIN tb_workflow_task WT ON A.WORKFLOW_REQ_ID = WT.WORKFLOW_REQ_ID ")

	           .append("WHERE B.SM_SERVICE_ID IS NULL ")
	           .append("AND B.orgId = :orgId ");

	    if (deptIdStr != null && !deptIdStr.trim().isEmpty() && Long.parseLong(deptIdStr) > 0) {
	        builder.append("AND B.dept_comp_id = :deptId ");
	    }
	    if (subTypeIdStr != null && !subTypeIdStr.trim().isEmpty()  && Long.parseLong(subTypeIdStr) > 0) {
	        builder.append("AND B.COMP_SUBTYPE_ID = :subTypeId ");
	    }
	    if (fromDateStr != null && toDateStr != null && !fromDateStr.isEmpty() && !toDateStr.isEmpty()) {
	        builder.append("AND DATE(B.DATE_OF_REQUEST) BETWEEN :fromDate AND :toDate ");
	    }
	    if (status != null && !status.equals("0")) {
	        if (status.equalsIgnoreCase("PENDING")) {
	            builder.append("AND A.LAST_DECISION IN ('SUBMITTED','REOPENED','FORWARD_TO_DEPARTMENT','FORWARD_TO_EMPLOYEE','FOLLOWUP','SEND_FOR_FEEDBACK') ");
	        } else {
	            builder.append("AND A.LAST_DECISION = :status ");
	        }
	    }
	    if (codIdLevel1 != null && !codIdLevel1.trim().isEmpty() && Long.parseLong(codIdLevel1) > 0) {
	        builder.append("AND B.CARE_WARD_NO = :codIdLevel1 ");
	    }
	    if (codIdLevel2 != null && !codIdLevel2.trim().isEmpty() && Long.parseLong(codIdLevel2) > 0) {
	        builder.append("AND B.CARE_WARD_NO1 = :codIdLevel2 ");
	    }
	    
	    if (codIdLevel3 != null && !codIdLevel3.trim().isEmpty() && Long.parseLong(codIdLevel3) > 0) {
	        builder.append("AND B.CARE_WARD_NO2 = :codIdLevel3 ");
	    }
	    
	    if (codIdLevel4 != null && !codIdLevel4.trim().isEmpty() && Long.parseLong(codIdLevel4) > 0) {
	        builder.append("AND B.CARE_WARD_NO3 = :codIdLevel4 ");
	    }
	    
	    if (codIdLevel5 != null && !codIdLevel5.trim().isEmpty() && Long.parseLong(codIdLevel5) > 0) {
	        builder.append("AND B.CARE_WARD_NO4 = :codIdLevel5 ");
	    }

	    builder.append("AND WT.WFTASK_ID IN (")
	           .append("SELECT MAX(WFTASK_ID) FROM tb_workflow_task ")
	           .append("WHERE WORKFLOW_REQ_ID = A.WORKFLOW_REQ_ID GROUP BY WORKFLOW_REQ_ID) ")
	           .append("GROUP BY department_descrption, COMPLAINT_SUB_TYPE) AS A) s");

	    Query query = entityManager.createNativeQuery(builder.toString(), SlaWiseGrievanceSumaaryEntity.class);
	    query.setParameter("orgId", 1L); // Hardcoded orgId

	    if (deptIdStr != null && !deptIdStr.trim().isEmpty() && Long.parseLong(deptIdStr) > 0) {
	        query.setParameter("deptId", Long.parseLong(deptIdStr));
	    }
	    if (subTypeIdStr != null && !subTypeIdStr.trim().isEmpty() && Long.parseLong(subTypeIdStr) > 0) {
	        query.setParameter("subTypeId", Long.parseLong(subTypeIdStr));
	    }
	    if (fromDateStr != null && toDateStr != null && !fromDateStr.isEmpty() && !toDateStr.isEmpty()) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
	            Date fromDate = inputFormat.parse(fromDateStr);
	            Date toDate = inputFormat.parse(toDateStr);

	            Calendar cal = Calendar.getInstance();
	            cal.setTime(toDate);
	            cal.add(Calendar.DATE, 1); // include full day
	            Date toDatePlusOne = cal.getTime();

	            query.setParameter("fromDate", fromDate);
	            query.setParameter("toDate", toDatePlusOne);
	        } catch (ParseException e) {
	            throw new RuntimeException("Invalid date format. Please use dd-MM-yyyy.", e);
	        }
	    }
	    if (status != null && !status.equals("0") && !status.equalsIgnoreCase("PENDING")) {
	        query.setParameter("status", status);
	    }
	    if (codIdLevel1 != null && !codIdLevel1.trim().isEmpty() && Long.parseLong(codIdLevel1) > 0) {
	        query.setParameter("codIdLevel1", Long.parseLong(codIdLevel1));
	    }
	    if (codIdLevel2 != null && !codIdLevel2.trim().isEmpty() && Long.parseLong(codIdLevel2) > 0) {
	        query.setParameter("codIdLevel2", Long.parseLong(codIdLevel2));
	    }
	    
	    if (codIdLevel3 != null && !codIdLevel3.trim().isEmpty() && Long.parseLong(codIdLevel3) > 0) {
	        query.setParameter("codIdLevel3", Long.parseLong(codIdLevel3));
	    }
	    
	    if (codIdLevel4 != null && !codIdLevel4.trim().isEmpty() && Long.parseLong(codIdLevel4) > 0) {
	        query.setParameter("codIdLevel4", Long.parseLong(codIdLevel4));
	    }
	    
	    if (codIdLevel5 != null && !codIdLevel5.trim().isEmpty() && Long.parseLong(codIdLevel5) > 0 ) {
	        query.setParameter("codIdLevel5", Long.parseLong(codIdLevel5));
	    }

	    slaWiseGrievanceSumaaryEntity = query.getResultList();
	    log.info("Query executed: getGrieveanceSummryReport");

	    return slaWiseGrievanceSumaaryEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<BirthCertificateEntityForMobile> getBirthCertificateDataForMobile(String appId, String orgId) {
	    
	    List<BirthCertificateEntityForMobile> birthCertificateEntityForMobile = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT BDR.BR_CHILDNAME, (SELECT CPD_DESC FROM tb_comparam_det WHERE CPD_ID=BDR.BR_SEX) SEX, date_format(BDR.BR_DOB,'%d-%m-%Y') DOB, BDR.BR_BIRTHPLACE BIRTH_PLACE, BDR.BR_CERT_NO, BDP.PD_MOTHERNAME MOTHER_NAME_ENG, BDP.PD_MOTHERNAME_MAR MOTHER_NAME_MAR, BDP.PD_FATHERNAME FATHER_NAME_ENG, BDP.PD_FATHERNAME_MAR FATHER_NAME_MAR, BDP.PD_PARADDRESS PARENT_ADD_AT_TIME_OF_BIRTH_ENG, BDP.PD_PARADDRESS_MAR PARENT_ADD_AT_TIME_OF_BIRTH_MAR, BDP.PD_ADDRESS PARENT_PER_ADDRESS_ENG, BDP.PD_ADDRESS_MAR PARENT_PER_ADDRESS_MAR, BDR.BR_REGNO REG_NO, date_format(BDR.BR_REGDATE,'%d-%m-%Y') REG_DATE, date_format(current_date(),'%d-%m-%Y') DATE_OF_ISSUE FROM tb_receipt_mas RM INNER JOIN tb_bd_cert_copy BDC ON BDC.APMApplicationId = RM.APM_APPLICATION_ID INNER JOIN tb_birthreg BDR ON BDR.BR_ID = BDC.BR_ID INNER JOIN tb_parentdetail BDP ON BDP.BR_ID = BDR.BR_ID WHERE RM.ORGID ='" + orgId + "' AND RM.APM_APPLICATION_ID ='" + appId + "')"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
	    birthCertificateEntityForMobile = (List<BirthCertificateEntityForMobile>) entityManager
				.createNativeQuery(queryBuilder.toString(), BirthCertificateEntityForMobile.class).getResultList();

	    log.error("Qeury execution ended getPropertyStatisticsServiceWise");
	    
	    return birthCertificateEntityForMobile;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeathCertificateEntityForMobile> getDeathCertificateDataForMobile(String appId, String orgId) {
	    
	    List<DeathCertificateEntityForMobile> deathCertificateEntityForMobile = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT DR.DR_DECEASEDNAME DETAH_PERSON_NAME,(SELECT CPD_DESC FROM tb_comparam_det WHERE CPD_ID=DR.dr_sex) SEX,date_format(DR.DR_DOD,'%d-%m-%Y') DOD,DR.DR_DEATHPLACE DEATH_PLACE,DR.DR_CERT_NO,DR.DR_MOTHER_NAME MOTHER_NAME_ENG,DR.DR_MAR_MOTHER_NAME MOTHER_NAME_MAR,DR.DR_RELATIVE_NAME FATHER_HUSBAND_NAME_ENG,DR.DR_MAR_RELATIVE_NAME FATHER_HUSBAND_NAME_MAR,DR.DR_DCADDR_ATDEATH ADD_AT_TIME_OF_DEATH_ENG,DR.DR_DCADDR_ATDEATH_MAR ADD_AT_TIME_OF_DEATH_MAR,DR.DR_DECEASEDADDR PER_ADDRESS_ENG,DR.DR_MAR_DECEASEDADDR PER_ADDRESS_MAR,DR.DR_REGNO REG_NO,date_format(DR.DR_REGDATE,'%d-%m-%Y') REG_DATE,date_format(current_date(),'%d-%m-%Y') DATE_OF_ISSUE FROM tb_receipt_mas RM INNER JOIN tb_bd_cert_copy BDC ON BDC.APMApplicationId=RM.APM_APPLICATION_ID INNER JOIN tb_deathreg DR ON DR.DR_ID=BDC.DR_ID WHERE RM.ORGID ='" + orgId + "' AND RM.APM_APPLICATION_ID ='" + appId + "')"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
	    deathCertificateEntityForMobile = (List<DeathCertificateEntityForMobile>) entityManager
				.createNativeQuery(queryBuilder.toString(), DeathCertificateEntityForMobile.class).getResultList();

	    log.error("Qeury execution ended getPropertyStatisticsServiceWise");
	    
	    return deathCertificateEntityForMobile;
	}
	
	@SuppressWarnings("unchecked")
	public List<TopTenToHunAllWardEntity> getTopTenToHunAllWardDetails(String number) {
	    
	    List<TopTenToHunAllWardEntity> topTenToHunAllWardEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	      

	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT WARD, ZONE, PROPERTY_NUMBER, FLAT_NUMBER, OWNER_NAME, OCCUPIER_NAME, OUTSTANDING FROM PROPERTY_DEFAULTER_LIST ORDER BY OUTSTANDING DESC LIMIT " + number + ")"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
	    topTenToHunAllWardEntity = (List<TopTenToHunAllWardEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TopTenToHunAllWardEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return topTenToHunAllWardEntity;
	}

	@SuppressWarnings("unchecked")
	public List<TopTenToHunAllWardEntity> getTopTenToHunWardWiseDetails(String wardId, String number) {
	    
	    List<TopTenToHunAllWardEntity> topTenToHunAllWardEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT WARD, ZONE, PROPERTY_NUMBER, FLAT_NUMBER, OWNER_NAME, OCCUPIER_NAME, OUTSTANDING FROM PROPERTY_DEFAULTER_LIST WHERE MN_ASS_WARD1 = '" + wardId + "' ORDER BY OUTSTANDING DESC LIMIT " + number + ")"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	        
	    
	    topTenToHunAllWardEntity = (List<TopTenToHunAllWardEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TopTenToHunAllWardEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return topTenToHunAllWardEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<TopTenToHunAllWardEntity> getTopTenToHunWardZoneWiseDetails(String number, String wardId, String zoneId ) {
	    
	    List<TopTenToHunAllWardEntity> topTenToHunAllWardEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT WARD, ZONE, PROPERTY_NUMBER, FLAT_NUMBER, OWNER_NAME, OCCUPIER_NAME, OUTSTANDING FROM PROPERTY_DEFAULTER_LIST WHERE MN_ASS_WARD1 = '" + wardId + "' AND MN_ASS_WARD2 = '" + zoneId + "' ORDER BY OUTSTANDING DESC LIMIT " + number + ")"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    topTenToHunAllWardEntity = (List<TopTenToHunAllWardEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), TopTenToHunAllWardEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return topTenToHunAllWardEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<LegalIllegalPropWardWiseEntity> getLegalIllegalPropWardWiseData() {
	    
	    List<LegalIllegalPropWardWiseEntity> legalIllegalPropWardWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (WITH latest_assessment AS (SELECT am.MN_ASS_ID, am.MN_ASS_NO, am.MN_ASS_WARD1, am.MN_ASS_WARD2, ad.MN_FLAT_NO, ad.MN_ASSD_USAGETYPE1, ad.MN_LEGAL, ROW_NUMBER() OVER (PARTITION BY am.MN_ASS_NO, CASE WHEN am.CPD_BILLMETH = 100000332 THEN ad.MN_FLAT_NO ELSE '' END ORDER BY ad.MN_ASSD_ID DESC) AS rn FROM tb_as_assesment_mast am JOIN tb_as_assesment_detail ad ON ad.MN_ASS_ID = am.MN_ASS_ID) SELECT w.COD_DESC AS WARD, COUNT(*) AS TOTAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' THEN 1 ELSE 0 END) AS LEGAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' THEN 1 ELSE 0 END) AS ILLEGAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' AND la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS LEGAL_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' AND la.MN_ASSD_USAGETYPE1 <> 100000361 THEN 1 ELSE 0 END) AS LEGAL_NON_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' AND la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS ILLEGAL_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' AND la.MN_ASSD_USAGETYPE1 <> 100000361 THEN 1 ELSE 0 END) AS ILLEGAL_NON_RESIDENTIAL FROM latest_assessment la JOIN tb_comparent_det w ON w.COD_ID = la.MN_ASS_WARD1 JOIN tb_comparent_det z ON z.COD_ID = la.MN_ASS_WARD2 WHERE la.rn = 1 AND la.MN_ASS_WARD1 != 100000007 GROUP BY w.COD_DESC ORDER BY w.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    legalIllegalPropWardWiseEntity = (List<LegalIllegalPropWardWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), LegalIllegalPropWardWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getwardWiseCollectionPropertyList");
	    
	    return legalIllegalPropWardWiseEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<LegalIllegalPropZoneWiseEntity> getLegalIllegalPropZoneWiseData(String wardId) {
	    
	    List<LegalIllegalPropZoneWiseEntity> legalIllegalPropZoneWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (WITH latest_assessment AS (SELECT am.MN_ASS_ID, am.MN_ASS_NO, am.MN_ASS_WARD1, am.MN_ASS_WARD2, ad.MN_FLAT_NO, ad.MN_ASSD_USAGETYPE1, ad.MN_LEGAL, ROW_NUMBER() OVER (PARTITION BY am.MN_ASS_NO, CASE WHEN am.CPD_BILLMETH = 100000332 THEN ad.MN_FLAT_NO ELSE '' END ORDER BY ad.MN_ASSD_ID DESC) AS rn FROM tb_as_assesment_mast am JOIN tb_as_assesment_detail ad ON ad.MN_ASS_ID = am.MN_ASS_ID) SELECT z.COD_DESC AS ZONE, COUNT(*) AS TOTAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' THEN 1 ELSE 0 END) AS LEGAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' THEN 1 ELSE 0 END) AS ILLEGAL_PROPERTIES, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' AND la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS LEGAL_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'LEGAL' AND la.MN_ASSD_USAGETYPE1 <> 100000361 THEN 1 ELSE 0 END) AS LEGAL_NON_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' AND la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS ILLEGAL_RESIDENTIAL, SUM(CASE WHEN la.MN_LEGAL = 'ILLEGAL' AND la.MN_ASSD_USAGETYPE1 <> 100000361 THEN 1 ELSE 0 END) AS ILLEGAL_NON_RESIDENTIAL FROM latest_assessment la JOIN tb_comparent_det w ON w.COD_ID = la.MN_ASS_WARD1 JOIN tb_comparent_det z ON z.COD_ID = la.MN_ASS_WARD2 WHERE la.rn = 1 AND la.MN_ASS_WARD1 != 100000007 AND la.MN_ASS_WARD1 = '" + wardId + "' GROUP BY z.COD_DESC ORDER BY z.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    legalIllegalPropZoneWiseEntity = (List<LegalIllegalPropZoneWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), LegalIllegalPropZoneWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getLegalIllegalPropZoneWiseData");
	    
	    return legalIllegalPropZoneWiseEntity;
	}

	
	
	@SuppressWarnings("unchecked")
	public List<UsageWardWisePropertyCountEntity> getUsageWardWisePropCount() {
	    
	    List<UsageWardWisePropertyCountEntity> usageWardWisePropertyCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (WITH latest_assessment AS (SELECT am.MN_ASS_ID, am.MN_ASS_NO, am.MN_ASS_WARD1, am.MN_ASS_WARD2, ad.MN_FLAT_NO, ad.MN_ASSD_USAGETYPE1, ROW_NUMBER() OVER (PARTITION BY am.MN_ASS_NO, CASE WHEN am.CPD_BILLMETH = 100000332 THEN ad.MN_FLAT_NO ELSE '' END ORDER BY ad.MN_ASSD_ID DESC) AS rn FROM tb_as_assesment_mast am JOIN tb_as_assesment_detail ad ON ad.MN_ASS_ID = am.MN_ASS_ID) SELECT w.COD_DESC AS WARD, SUM(CASE WHEN la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS RESIDENTIAL_PROPERTY_COUNT, SUM(CASE WHEN la.MN_ASSD_USAGETYPE1 != 100000361 THEN 1 ELSE 0 END) AS NON_RESIDENTIAL_PROPERTY_COUNT, COUNT(*) TOTAL_PROPERTY_COUNT FROM latest_assessment la JOIN tb_comparent_det w ON w.COD_ID = la.MN_ASS_WARD1 JOIN tb_comparent_det z ON z.COD_ID = la.MN_ASS_WARD2 WHERE la.rn = 1 AND la.MN_ASS_WARD1 != 100000007 GROUP BY w.COD_DESC ORDER BY w.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    usageWardWisePropertyCountEntity = (List<UsageWardWisePropertyCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), UsageWardWisePropertyCountEntity.class).getResultList();

	    log.error("Qeury execution ended getUsageWardWisePropCount");
	    
	    return usageWardWisePropertyCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<UsageZoneWisePropertyCountEntity> getUsageZoneWisePropCount(String wardId) {
	    
	    List<UsageZoneWisePropertyCountEntity> usageZoneWisePropertyCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (WITH latest_assessment AS (SELECT am.MN_ASS_ID, am.MN_ASS_NO, am.MN_ASS_WARD1, am.MN_ASS_WARD2, ad.MN_FLAT_NO, ad.MN_ASSD_USAGETYPE1, ROW_NUMBER() OVER (PARTITION BY am.MN_ASS_NO, CASE WHEN am.CPD_BILLMETH = 100000332 THEN ad.MN_FLAT_NO ELSE '' END ORDER BY ad.MN_ASSD_ID DESC) AS rn FROM tb_as_assesment_mast am JOIN tb_as_assesment_detail ad ON ad.MN_ASS_ID = am.MN_ASS_ID) SELECT z.COD_DESC AS zone, SUM(CASE WHEN la.MN_ASSD_USAGETYPE1 = 100000361 THEN 1 ELSE 0 END) AS RESIDENTIAL_PROPERTY_COUNT, SUM(CASE WHEN la.MN_ASSD_USAGETYPE1 != 100000361 THEN 1 ELSE 0 END) AS NON_RESIDENTIAL_PROPERTY_COUNT, COUNT(*) TOTAL_PROPERTY_COUNT FROM latest_assessment la JOIN tb_comparent_det w ON w.COD_ID = la.MN_ASS_WARD1 JOIN tb_comparent_det z ON z.COD_ID = la.MN_ASS_WARD2 WHERE la.rn = 1 AND la.MN_ASS_WARD1 != 100000007 AND la.MN_ASS_WARD1 = '" + wardId + "' GROUP BY z.COD_DESC ORDER BY z.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    usageZoneWisePropertyCountEntity = (List<UsageZoneWisePropertyCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), UsageZoneWisePropertyCountEntity.class).getResultList();

	    log.error("Qeury execution ended getUsageZoneWisePropCount");
	    
	    return usageZoneWisePropertyCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WardAndModeWisePropertyCollectionEntity> getPropWardAndModeWiseCollection(String wardId,String zoneId, String startDate, String endDate) {
	    
	    List<WardAndModeWisePropertyCollectionEntity> wardAndModeWisePropertyCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT (SELECT COD_DESC FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD1) WARD, CPD.CPD_DESC MODE, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NULL AND RD.TAX_ID!=(SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='AS89') THEN RD.RF_FEEAMOUNT END,0)) AS ARREARS_COLLECTION, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NOT NULL THEN RD.RF_FEEAMOUNT END,0)) AS CURRENT_COLLECTION, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NULL AND RD.TAX_ID=(SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='AS89') THEN RD.RF_FEEAMOUNT END,0)) AS ADVANCE_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID LEFT JOIN tb_as_bill_mas BM ON RD.BM_IDNO=BM.MN_BM_IDNO AND BM.MN_BM_YEAR=(SELECT MAX(FA_YEARID) FROM tb_financialyear) INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN TB_COMPARAM_DET CPD ON CPD.CPD_ID=RMO.CPD_FEEMODE INNER JOIN (SELECT DISTINCT MN_ASS_NO, MN_ASS_WARD1, MN_ASS_WARD2 FROM tb_as_assesment_mast AM WHERE AM.MN_ASS_WARD1!=100000007 AND AM.MN_ASS_WARD1 LIKE CASE WHEN '" + wardId + "' = 0 THEN '%' ELSE '" + wardId + "' END AND AM.MN_ASS_WARD2 LIKE CASE WHEN '" + zoneId + "' = 0 THEN '%' ELSE '" + zoneId + "' END) AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO WHERE RM.ORGID = 1 AND RM.RM_DATE>= '" + startDate + "' AND RM.RM_DATE<= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID = (SELECT DP_DEPTID FROM TB_DEPARTMENT WHERE DP_DEPTCODE = 'AS') AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RD.TAX_ID IS NOT NULL AND RM.RECEIPT_TYPE_FLAG IN ('R','A') AND AM.MN_ASS_WARD1 <> 100000007 AND CPD.CPD_VALUE IN('D','C','Q','P','B','W','POS') GROUP BY AM.MN_ASS_WARD1, MODE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    wardAndModeWisePropertyCollectionEntity = (List<WardAndModeWisePropertyCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WardAndModeWisePropertyCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getPropWardAndModeWiseCollection");
	    
	    return wardAndModeWisePropertyCollectionEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<ZoneAndModeWisePropertyCollectionEntity> getPropZoneAndModeWiseCollection(String wardId,String zoneId, String startDate, String endDate) {
	    
	    List<ZoneAndModeWisePropertyCollectionEntity> zoneAndModeWisePropertyCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT (SELECT COD_DESC FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD2) ZONE, CPD.CPD_DESC MODE, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NULL AND RD.TAX_ID!=(SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='AS89') THEN RD.RF_FEEAMOUNT END,0)) AS ARREARS_COLLECTION, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NOT NULL THEN RD.RF_FEEAMOUNT END,0)) AS CURRENT_COLLECTION, SUM(IFNULL(CASE WHEN BM.MN_BM_IDNO IS NULL AND RD.TAX_ID=(SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='AS89') THEN RD.RF_FEEAMOUNT END,0)) AS ADVANCE_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RD.RM_RCPTID = RM.RM_RCPTID LEFT JOIN tb_as_bill_mas BM ON RD.BM_IDNO=BM.MN_BM_IDNO AND BM.MN_BM_YEAR=(SELECT MAX(FA_YEARID) FROM tb_financialyear) INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN TB_COMPARAM_DET CPD ON CPD.CPD_ID=RMO.CPD_FEEMODE INNER JOIN (SELECT DISTINCT MN_ASS_NO, MN_ASS_WARD1, MN_ASS_WARD2 FROM tb_as_assesment_mast AM WHERE AM.MN_ASS_WARD1= '" + wardId + "' AND AM.MN_ASS_WARD1!=100000007 AND AM.MN_ASS_WARD2 LIKE CASE WHEN '" + zoneId + "' = 0 THEN '%' ELSE '" + zoneId + "' END) AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO WHERE RM.ORGID = 1 AND RM.RM_DATE>= '" + startDate + "' AND RM.RM_DATE<= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID=(SELECT DP_DEPTID FROM TB_DEPARTMENT WHERE DP_DEPTCODE='AS') AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RD.TAX_ID IS NOT NULL AND RM.RECEIPT_TYPE_FLAG IN ('R','A') AND AM.MN_ASS_WARD1<>100000007 AND CPD.CPD_VALUE IN('D','C','Q','P','B','W','POS') GROUP BY AM.MN_ASS_WARD2, MODE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    zoneAndModeWisePropertyCollectionEntity = (List<ZoneAndModeWisePropertyCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), ZoneAndModeWisePropertyCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getPropZoneAndModeWiseCollection");
	    
	    return zoneAndModeWisePropertyCollectionEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<AbhayYojnaCollectionWardWiseEntity> getWardWiseAbhayYojnaCollection(String wardId, String startDate, String endDate) {
	    
	    List<AbhayYojnaCollectionWardWiseEntity> abhayYojnaCollectionWardWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT (SELECT COD_DESC FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD1) WARD, SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN 1 ELSE 0 END) 'NUMBER OF BENIFICIARY', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN RMO.RD_AMOUNT ELSE 0 END) 'ABHAY YOJANA AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='WRB' THEN RMO.RD_AMOUNT ELSE 0 END) 'WEB REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='RB' THEN RMO.RD_AMOUNT ELSE 0 END) 'EARLY REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('ABY','WRB','RB') THEN RMO.RD_AMOUNT ELSE 0 END) 'TOTAL REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('R','A') THEN RMO.RD_AMOUNT ELSE 0 END) 'ACTUAL RECEIPT AMOUNT' FROM tb_receipt_mas RM INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN (SELECT DISTINCT MN_ASS_NO, MN_ASS_WARD1, MN_ASS_WARD2 FROM tb_as_assesment_mast AM WHERE AM.MN_ASS_WARD1!=100000007 AND AM.MN_ASS_WARD1 LIKE CASE WHEN '" + wardId + "' = 0 THEN '%' ELSE '" + wardId + "' END) AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO WHERE RM.ORGID = 1 AND RM.RM_DATE>= '" + startDate + "' AND RM.RM_DATE <= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID = (SELECT DP_DEPTID FROM TB_DEPARTMENT WHERE DP_DEPTCODE = 'AS') AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','RB','WRB','ABY') AND AM.MN_ASS_WARD1 <> 100000007 GROUP BY AM.MN_ASS_WARD1 ORDER BY WARD)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    abhayYojnaCollectionWardWiseEntity = (List<AbhayYojnaCollectionWardWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), AbhayYojnaCollectionWardWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getWardWiseAbhayYojnaCollection");
	    
	    return abhayYojnaCollectionWardWiseEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<AbhayYojnaCollectionZoneWiseEntity> getZoneWiseAbhayYojnaCollection(String wardId,String zoneId, String startDate, String endDate) {
	    
	    List<AbhayYojnaCollectionZoneWiseEntity> abhayYojnaCollectionZoneWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT (SELECT COD_DESC FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD2) ZONE, SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN 1 ELSE 0 END) 'NUMBER OF BENIFICIARY', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN RMO.RD_AMOUNT ELSE 0 END) 'ABHAY YOJANA AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='WRB' THEN RMO.RD_AMOUNT ELSE 0 END) 'WEB REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='RB' THEN RMO.RD_AMOUNT ELSE 0 END) 'EARLY REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('ABY','WRB','RB') THEN RMO.RD_AMOUNT ELSE 0 END) 'TOTAL REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('R','A') THEN RMO.RD_AMOUNT ELSE 0 END) 'ACTUAL RECEIPT AMOUNT' FROM tb_receipt_mas RM INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN (SELECT DISTINCT MN_ASS_NO, MN_ASS_WARD1, MN_ASS_WARD2 FROM tb_as_assesment_mast AM WHERE AM.MN_ASS_WARD1!=100000007 AND AM.MN_ASS_WARD1= '" + wardId + "' AND AM.MN_ASS_WARD2 LIKE CASE WHEN '" + zoneId + "' = 0 THEN '%' ELSE '" + zoneId + "' END) AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO WHERE RM.ORGID = 1 AND RM.RM_DATE>= '" + startDate + "' AND RM.RM_DATE<= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID=(SELECT DP_DEPTID FROM TB_DEPARTMENT WHERE DP_DEPTCODE='AS') AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','RB','WRB','ABY') AND AM.MN_ASS_WARD1<>100000007 GROUP BY AM.MN_ASS_WARD2 ORDER BY ZONE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    abhayYojnaCollectionZoneWiseEntity = (List<AbhayYojnaCollectionZoneWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), AbhayYojnaCollectionZoneWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getZoneWiseAbhayYojnaCollection");
	    
	    return abhayYojnaCollectionZoneWiseEntity;
	}
	
	
	public List<DuplicateReceiptBpmsEntity> getBpmsIntegrationEntity() {
	    
	    List<DuplicateReceiptBpmsEntity> bpmsIntegrationEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
	    /*queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT (SELECT COD_DESC FROM tb_comparent_det WHERE cod_id = AM.MN_ASS_WARD2) ZONE, SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN 1 ELSE 0 END) 'NUMBER OF BENIFICIARY', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='ABY' THEN RMO.RD_AMOUNT ELSE 0 END) 'ABHAY YOJANA AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='WRB' THEN RMO.RD_AMOUNT ELSE 0 END) 'WEB REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG='RB' THEN RMO.RD_AMOUNT ELSE 0 END) 'EARLY REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('ABY','WRB','RB') THEN RMO.RD_AMOUNT ELSE 0 END) 'TOTAL REBATE AMOUNT', SUM(CASE WHEN RM.RECEIPT_TYPE_FLAG IN('R','A') THEN RMO.RD_AMOUNT ELSE 0 END) 'ACTUAL RECEIPT AMOUNT' FROM tb_receipt_mas RM INNER JOIN tb_receipt_mode RMO ON RM.RM_RCPTID=RMO.RM_RCPTID INNER JOIN (SELECT DISTINCT MN_ASS_NO, MN_ASS_WARD1, MN_ASS_WARD2 FROM tb_as_assesment_mast AM WHERE AM.MN_ASS_WARD1!=100000007 AND AM.MN_ASS_WARD1= '" + wardId + "' AND AM.MN_ASS_WARD2 LIKE CASE WHEN '" + zoneId + "' = 0 THEN '%' ELSE '" + zoneId + "' END) AM ON AM.MN_ASS_NO = RM.ADDITIONAL_REF_NO WHERE RM.ORGID = 1 AND RM.RM_DATE>= '" + startDate + "' AND RM.RM_DATE<= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID=(SELECT DP_DEPTID FROM TB_DEPARTMENT WHERE DP_DEPTCODE='AS') AND (RMO.RD_SR_CHK_DIS IS NULL OR RMO.RD_SR_CHK_DIS='N') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','RB','WRB','ABY') AND AM.MN_ASS_WARD1<>100000007 GROUP BY AM.MN_ASS_WARD2 ORDER BY ZONE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    */
		queryBuilder.append(
				"select @row_number\\:=@row_number+1 AS num, s.* from (SELECT DR.ADDITIONAL_REF_NO PROPERTY_NUMBER, DR.RM_RCPTNO, DR.RM_DATE, BID.proposal_number FROM tb_dup_receipt DR INNER JOIN tb_bpms_integration_details BID ON BID.Property_No=DR.ADDITIONAL_REF_NO INNER JOIN lateral(SELECT MAX(DUP_RCID) DUP_RCID FROM tb_dup_receipt DRI WHERE DRI.DP_DEPTCODE='AS' AND DRI.ADDITIONAL_REF_NO=DR.ADDITIONAL_REF_NO AND RM_DATE=date(date_sub(NOW(),INTERVAL 1 DAY)) GROUP BY DRI.ADDITIONAL_REF_NO) DRI ON DRI.DUP_RCID=DR.DUP_RCID WHERE DR.DP_DEPTCODE='AS' AND RM_DATE=date(date_sub(NOW(),INTERVAL 1 DAY))) s, (SELECT @row_number\\:=0) AS rn");
    
	    bpmsIntegrationEntity = (List<DuplicateReceiptBpmsEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), DuplicateReceiptBpmsEntity.class).getResultList();

	    log.error("Qeury execution ended getBpmsIntegrationEntity");
	    
	    return bpmsIntegrationEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterAbhayYojnaWardZoneWiseEntity> getWaterWardZoneWiseAbhayYojnaCollection(String startDate, String endDate, String wardId) {
	    
	    List<WaterAbhayYojnaWardZoneWiseEntity> waterAbhayYojnaWardZoneWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (select Ward,zone,ward_id,zone_id,sum(no_of_beneficaries) no_of_beneficaries,sum(Total_Due_Amount) Total_Due_Amount,sum(PenaltyWaveAmount) PenaltyWaveAmount,sum(ActualReceivedAmount) ActualReceivedAmount from ( select cd1.COD_Value Ward,cd2.COD_Value zone,tc.COD_DWZID2 ward_id,tc.COD_DWZID3 zone_id,(select count(rm1.RM_RCPTID) from tb_receipt_mas rm1 where rm1.REF_ID=rm.RM_RCPTID and rm1.RECEIPT_TYPE_FLAG='ABY') no_of_beneficaries,((case when RM.RECEIPT_TYPE_FLAG='R' and RM.RM_RCPTID in (select REF_ID from tb_receipt_mas where REF_ID=RM.RM_RCPTID and RECEIPT_TYPE_FLAG='ABY') then RM.RM_AMOUNT else 0 end)+(case when RM.RECEIPT_TYPE_FLAG='ABY' then RM.RM_AMOUNT else 0 end)) Total_Due_Amount,(select sum(rm2.RM_AMOUNT) from tb_receipt_mas rm2 where rm2.REF_ID=RM.RM_RCPTID and rm2.RECEIPT_TYPE_FLAG='ABY') PenaltyWaveAmount,(rm.RM_AMOUNT) ActualReceivedAmount from tb_receipt_mas RM,tb_comparent_det cd1,tb_comparent_det cd2,tb_csmr_info tc,tb_receipt_mode rd where cd1.cOD_ID=tc.COD_DWZID2 and cd2.cod_id=tc.COD_DWZID3 and RM.RECEIPT_DEL_FLAG is null and rm.RM_RCPTID=rd.RM_RCPTID and ifnull(rd.RD_SR_CHK_DIS,'N')='N' and RM.RECEIPT_TYPE_FLag='R' and RM.ORGID=1 and RM.DP_DEPTID=5 and tc.CS_IDN=cast(RM.ADDITIONAL_REF_NO as unsigned) and date(rm.RM_DATE) between '" + startDate + "' and '" + endDate + "' and exists (select rm1.RM_RCPTID from tb_receipt_mas rm1 where rm1.REF_ID=rm.RM_RCPTID and rm1.RECEIPT_TYPE_FLAG='ABY') ) tab where tab.PenaltyWaveAmount>0 and tab.ward_id= '" + wardId + "' group by tab.Ward,tab.zone,tab.ward_id,tab.zone_id order by tab.zone)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterAbhayYojnaWardZoneWiseEntity = (List<WaterAbhayYojnaWardZoneWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterAbhayYojnaWardZoneWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardZoneWiseAbhayYojnaCollection");
	    
	    return waterAbhayYojnaWardZoneWiseEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterAbhayYojnaAllWardWiseEntity> getWaterAllWardWiseAbhayYojnaCollection(String startDate, String endDate) {
	    
	    List<WaterAbhayYojnaAllWardWiseEntity> waterAbhayYojnaAllWardWiseEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (select Ward,ward_id,sum(no_of_beneficaries) no_of_beneficaries,sum(Total_Due_Amount) Total_Due_Amount,sum(PenaltyWaveAmount) PenaltyWaveAmount,sum(ActualReceivedAmount) ActualReceivedAmount from ( select cd1.COD_Value Ward,tc.COD_DWZID2 ward_id,(select count(rm1.RM_RCPTID) from tb_receipt_mas rm1 where rm1.REF_ID=rm.RM_RCPTID and rm1.RECEIPT_TYPE_FLAG='ABY') no_of_beneficaries,((case when RM.RECEIPT_TYPE_FLAG='R' and RM.RM_RCPTID in (select REF_ID from tb_receipt_mas where REF_ID=RM.RM_RCPTID and RECEIPT_TYPE_FLAG='ABY') then RM.RM_AMOUNT else 0 end)+(case when RM.RECEIPT_TYPE_FLAG='ABY' then RM.RM_AMOUNT else 0 end)) Total_Due_Amount,(select sum(rm2.RM_AMOUNT) from tb_receipt_mas rm2 where rm2.REF_ID=RM.RM_RCPTID and rm2.RECEIPT_TYPE_FLAG='ABY') PenaltyWaveAmount,(rm.RM_AMOUNT) ActualReceivedAmount from tb_receipt_mas RM,tb_comparent_det cd1,tb_csmr_info tc,tb_receipt_mode rd where cd1.cOD_ID=tc.COD_DWZID2 and RM.RECEIPT_DEL_FLAG is null and rm.RM_RCPTID=rd.RM_RCPTID and ifnull(rd.RD_SR_CHK_DIS,'N')='N' and RM.RECEIPT_TYPE_FLag='R' and RM.ORGID=1 and RM.DP_DEPTID=5 and tc.CS_IDN=cast(RM.ADDITIONAL_REF_NO as unsigned) and date(rm.RM_DATE) between '" + startDate + "' and '" + endDate + "' and exists (select rm1.RM_RCPTID from tb_receipt_mas rm1 where rm1.REF_ID=rm.RM_RCPTID and rm1.RECEIPT_TYPE_FLAG='ABY') ) tab where tab.PenaltyWaveAmount>0 group by tab.Ward,tab.ward_id)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterAbhayYojnaAllWardWiseEntity = (List<WaterAbhayYojnaAllWardWiseEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterAbhayYojnaAllWardWiseEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterAllWardWiseAbhayYojnaCollection");
	    
	    return waterAbhayYojnaAllWardWiseEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterWardWiseUsageCountEntity> getWaterWardWiseUsageCount() {
	    
	    List<WaterWardWiseUsageCountEntity> waterWardWiseUsageCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT cd4.COD_DESC ward, tc.COD_DWZID2 ward_id, cd5.COD_DESC Usage_Category, COUNT(TC.CS_IDN) COUNT FROM tb_csmr_info TC, tb_comparent_det cd4, tb_comparent_det cd5 WHERE cd5.COD_ID = TC.TRM_GROUP1 AND cd4.COD_ID = tc.COD_DWZID2 AND tc.CS_IS_BILLING_ACTIVE = 'A' AND TC.CS_CCN IS NOT NULL GROUP BY cd4.COD_DESC, tc.COD_DWZID2, cd5.COD_DESC ORDER BY cd4.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterWardWiseUsageCountEntity = (List<WaterWardWiseUsageCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterWardWiseUsageCountEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardWiseUsageCount");
	    
	    return waterWardWiseUsageCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterWardAndZoneWiseUsageCountEntity> getWaterWardZoneWiseUsageCount(String wardId) {
	    
	    List<WaterWardAndZoneWiseUsageCountEntity> waterWardAndZoneWiseUsageCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT cd4.COD_DESC ward, cd6.COD_VALUE zone, tc.COD_DWZID2 ward_id, tc.COD_DWZID3 zone_id, cd5.COD_DESC Usage_Category, COUNT(TC.CS_IDN) COUNT FROM tb_csmr_info TC, tb_comparent_det cd4, tb_comparent_det cd5, tb_comparent_det cd6 WHERE cd5.COD_ID = TC.TRM_GROUP1 AND cd4.COD_ID = tc.COD_DWZID2 AND cd6.COD_ID = tc.COD_DWZID3 AND tc.CS_IS_BILLING_ACTIVE = 'A' AND tc.COD_DWZID2 = '" + wardId + "' AND TC.CS_CCN IS NOT NULL GROUP BY cd4.COD_DESC, tc.COD_DWZID2, tc.COD_DWZID3, cd6.COD_VALUE, cd5.COD_DESC ORDER BY cd4.COD_DESC, cd6.COD_VALUE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterWardAndZoneWiseUsageCountEntity = (List<WaterWardAndZoneWiseUsageCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterWardAndZoneWiseUsageCountEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardWiseUsageCount");
	    
	    return waterWardAndZoneWiseUsageCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxWardWiseAuthoUnathoCountEntity> getWaterWardeWiseAuthorUnauthoCount() {
	    
	    List<WaterTaxWardWiseAuthoUnathoCountEntity> waterWardAndZoneWiseUsageCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT cd4.COD_DESC ward, tc.COD_DWZID2 ward_id, IF(SUBSTRING(CD2.COD_DESC,1,2) = 'D4', 'Unauthorised', 'Authorised') CCN_Category, COUNT(TC.CS_IDN) COUNT FROM tb_csmr_info TC, TB_COMPARENT_DET CD2, tb_comparent_det cd4 WHERE CD2.COD_ID = TC.TRM_GROUP2 AND cd4.COD_ID = tc.COD_DWZID2 AND tc.CS_IS_BILLING_ACTIVE = 'A' AND TC.CS_CCN IS NOT NULL GROUP BY cd4.COD_DESC, tc.COD_DWZID2, IF(SUBSTRING(CD2.COD_DESC,1,2) = 'D4', 'Unauthorised', 'Authorised') ORDER BY cd4.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterWardAndZoneWiseUsageCountEntity = (List<WaterTaxWardWiseAuthoUnathoCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxWardWiseAuthoUnathoCountEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardWiseUsageCount");
	    
	    return waterWardAndZoneWiseUsageCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxWardZoneWiseAuthoUnathoCountEntity> getWaterWardeAndZoneWiseAuthorUnauthoCount(String wardId) {
	    
	    List<WaterTaxWardZoneWiseAuthoUnathoCountEntity> waterTaxWardZoneWiseAuthoUnathoCountEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT cd4.COD_DESC ward, cd5.COD_VALUE zone, tc.COD_DWZID2 ward_id, tc.COD_DWZID3 zone_id, IF(SUBSTRING(CD2.COD_DESC,1,2) = 'D4', 'Unauthorised', 'Authorised') CCN_Category, COUNT(TC.CS_IDN) COUNT FROM tb_csmr_info TC, TB_COMPARENT_DET CD2, tb_comparent_det cd4, tb_comparent_det cd5 WHERE CD2.COD_ID = TC.TRM_GROUP2 AND cd4.COD_ID = tc.COD_DWZID2 AND cd5.COD_ID = tc.COD_DWZID3 AND tc.CS_IS_BILLING_ACTIVE = 'A' AND TC.CS_CCN IS NOT NULL AND tc.COD_DWZID2 = '" + wardId + "' GROUP BY cd4.COD_DESC, cd5.COD_VALUE, tc.COD_DWZID2, tc.COD_DWZID3, IF(SUBSTRING(CD2.COD_DESC,1,2) = 'D4', 'Unauthorised', 'Authorised') ORDER BY cd4.COD_DESC, cd5.COD_VALUE)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxWardZoneWiseAuthoUnathoCountEntity = (List<WaterTaxWardZoneWiseAuthoUnathoCountEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxWardZoneWiseAuthoUnathoCountEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardeAndZoneWiseAuthorUnauthoCount");
	    
	    return waterTaxWardZoneWiseAuthoUnathoCountEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxWardWiseDemandCollectionEntity> getWaterWardWiseDemandCollection(String startDate) {
	    
	    List<WaterTaxWardWiseDemandCollectionEntity> waterTaxWardWiseDemandCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, ward_id, SUM(Total_Demand) Total_Demand, SUM(Total_Recovery) Total_Recovery, SUM(Balance_Arreas) Balance_Arreas, SUM(Per_Recovery) Per_Recovery FROM tb_wt_rep_arr_zonewise_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' GROUP BY ward, ward_id ORDER BY ward)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxWardWiseDemandCollectionEntity = (List<WaterTaxWardWiseDemandCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxWardWiseDemandCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterWardeAndZoneWiseAuthorUnauthoCount");
	    
	    return waterTaxWardWiseDemandCollectionEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxZoneWiseDemandCollectionEntity> getWaterZoneWiseDemandCollection(String startDate, String wardId) {
	    
	    List<WaterTaxZoneWiseDemandCollectionEntity> waterTaxZoneWiseDemandCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, zone, ward_id, zone_id, SUM(Total_Demand) Total_Demand, SUM(Total_Recovery) Total_Recovery, SUM(Balance_Arreas) Balance_Arreas, SUM(Per_Recovery) Per_Recovery FROM tb_wt_rep_arr_zonewise_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' AND ward_id = '" + wardId + "' GROUP BY ward, zone, ward_id, zone_id ORDER BY ward, zone)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxZoneWiseDemandCollectionEntity = (List<WaterTaxZoneWiseDemandCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxZoneWiseDemandCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterZoneWiseDemandCollection");
	    
	    return waterTaxZoneWiseDemandCollectionEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxModeWardWiseData> getWaterModeWiseWardData(String startDate) {
	    
	    List<WaterTaxModeWardWiseData> waterTaxModeWardWiseData = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, ward_id, Pay_Mode, SUM(current_recovery) current_recovery, SUM(Arreeas_recovery) Arreeas_recovery FROM tb_wt_rep_zonewise_mode_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' GROUP BY ward, ward_id, Pay_Mode)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxModeWardWiseData = (List<WaterTaxModeWardWiseData>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxModeWardWiseData.class).getResultList();

	    log.error("Qeury execution ended getWaterModeWiseWardData");
	    
	    return waterTaxModeWardWiseData;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxModeWardAndZoneWiseDataEntity> getWaterModeWiseWardAndZoneData(String startDate, String wardId) {
	    
	    List<WaterTaxModeWardAndZoneWiseDataEntity> waterTaxModeWardAndZoneWiseDataEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, zone, ward_id, zone_id, Pay_Mode, SUM(current_recovery) current_recovery, SUM(Arreeas_recovery) Arreeas_recovery FROM tb_wt_rep_zonewise_mode_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' AND ward_id = '" + wardId + "' GROUP BY ward, zone, ward_id, zone_id, Pay_Mode)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxModeWardAndZoneWiseDataEntity = (List<WaterTaxModeWardAndZoneWiseDataEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxModeWardAndZoneWiseDataEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterModeWiseWardAndZoneData");
	    
	    return waterTaxModeWardAndZoneWiseDataEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterAllWardWiseTopTenToHundredDefaulterEntity> getWaterTopTenToHunAllWardWiseData(String startDate, String num) {
	    
	    List<WaterAllWardWiseTopTenToHundredDefaulterEntity> waterAllWardWiseTopTenToHundredDefaulterEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, zone, ward_id, connection_no, outstanding_with_penalty outstanding FROM tb_wt_rep_wtr_defter_zonewise_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' ORDER BY outstanding_with_penalty DESC LIMIT " + num + ")"	
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterAllWardWiseTopTenToHundredDefaulterEntity = (List<WaterAllWardWiseTopTenToHundredDefaulterEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterAllWardWiseTopTenToHundredDefaulterEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterTopTenToHunAllWardWiseData");
	    
	    return waterAllWardWiseTopTenToHundredDefaulterEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTenHunWardZoneEntity> getWaterTopTenToHunWardData(String startDate, String wardId, String num) {
	    
	    List<WaterTenHunWardZoneEntity> waterTenHunWardZoneEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT ward, zone, ward_id, connection_no, outstanding_with_penalty outstanding FROM tb_wt_rep_wtr_defter_zonewise_rec_as_on_date WHERE DATE(created_date) = '" + startDate + "' AND ward_id = '" + wardId + "' ORDER BY outstanding_with_penalty DESC LIMIT " + num + ")"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTenHunWardZoneEntity = (List<WaterTenHunWardZoneEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTenHunWardZoneEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterModeWiseWardAndZoneData");
	    
	    return waterTenHunWardZoneEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTenHunWardZoneEntity> getWaterTopTenToHunWardZoneWiseData(String startDate, String wardId, String zoneId, String num) {
	    
	    List<WaterTenHunWardZoneEntity> waterTenHunWardZoneEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (select ward, zone, ward_id, zone_id, connection_no, outstanding_with_penalty outstanding from tb_wt_rep_wtr_defter_zonewise_rec_as_on_date where date(created_date) = '" + startDate + "' and ward_id = '" + wardId + "' and zone_id = '" + zoneId + "' order by outstanding_with_penalty desc limit " + num + ")"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTenHunWardZoneEntity = (List<WaterTenHunWardZoneEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTenHunWardZoneEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterModeWiseWardAndZoneData");
	    
	    return waterTenHunWardZoneEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxWardWiseCollectionEntity> getWaterTaxWardWiseCollectionData(String startDate, String endDate) {
	    
	    List<WaterTaxWardWiseCollectionEntity> waterTaxWardWiseCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT COD.COD_DESC WARD, SUM(CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) ARREAR_COLLECTION, SUM((CASE WHEN BM.BM_IDNO IS NOT NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) + (CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID = (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END)) CURRENT_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RM.RM_RCPTID = RD.RM_RCPTID LEFT JOIN tb_wt_bill_mas BM ON RD.BM_IDNO = BM.BM_IDNO AND BM.BM_YEAR = (SELECT FA_YEARID FROM tb_financialyear WHERE '" + startDate + "' BETWEEN FA_FROMDATE AND FA_TODATE) INNER JOIN tb_csmr_info CSMR ON CSMR.cs_idn = CAST(RM.ADDITIONAL_REF_NO AS UNSIGNED) INNER JOIN tb_comparent_det COD ON COD.COD_ID = CSMR.COD_DWZID2 WHERE RM.RECEIPT_DEL_FLAG IS NULL AND RM.RM_DATE >= '" + startDate + "' AND RM.RM_DATE <= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND RM.DP_DEPTID = (SELECT DP_DEPTID FROM tb_department WHERE DP_DEPTCODE = 'WT') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','ABY') GROUP BY COD.COD_DESC ORDER BY COD.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
    
	    waterTaxWardWiseCollectionEntity = (List<WaterTaxWardWiseCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxWardWiseCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterTaxWardWiseCollectionData");
	    
	    return waterTaxWardWiseCollectionEntity;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<WaterWardWiseCollectionEntity> getWaterOnlyWardWiseCollectionData(String startDate, String endDate, String wardId) {
	    
	    List<WaterWardWiseCollectionEntity> waterWardWiseCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT CODW.COD_DESC WARD, CODZ.COD_DESC ZONE, SUM(CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) ARREAR_COLLECTION, SUM((CASE WHEN BM.BM_IDNO IS NOT NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) + (CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID = (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE = 'WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END)) CURRENT_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RM.RM_RCPTID = RD.RM_RCPTID LEFT JOIN tb_wt_bill_mas BM ON RD.BM_IDNO = BM.BM_IDNO AND BM.BM_YEAR = (SELECT FA_YEARID FROM tb_financialyear WHERE '" + startDate + "' BETWEEN FA_FROMDATE AND FA_TODATE) INNER JOIN tb_csmr_info CSMR ON CSMR.cs_idn = CAST(RM.ADDITIONAL_REF_NO AS UNSIGNED) INNER JOIN tb_comparent_det CODW ON CODW.COD_ID = CSMR.COD_DWZID2 INNER JOIN tb_comparent_det CODZ ON CODZ.COD_ID = CSMR.COD_DWZID3 WHERE RM.RECEIPT_DEL_FLAG IS NULL AND RM.RM_DATE >= '" + startDate + "' AND RM.RM_DATE <= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND CSMR.COD_DWZID2 = '" + wardId + "' AND RM.DP_DEPTID = (SELECT DP_DEPTID FROM tb_department WHERE DP_DEPTCODE = 'WT') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','ABY') GROUP BY CODW.COD_DESC, CODZ.COD_DESC ORDER BY CODZ.COD_DESC, CODZ.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
	    
    
	    waterWardWiseCollectionEntity = (List<WaterWardWiseCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterWardWiseCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterOnlyWardWiseCollectionData");
	    
	    return waterWardWiseCollectionEntity;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<WaterTaxWardAndZoneWiseCollectionEntity> getWaterTaxWardAndZoneWiseCollectionData(String startDate, String endDate, String wardId, String zoneId) {
	    
	    List<WaterTaxWardAndZoneWiseCollectionEntity> waterTaxWardAndZoneWiseCollectionEntity = null;

	    StringBuilder queryBuilder = new StringBuilder();
	    
		
	    queryBuilder
		.append("select @row_number\\:=@row_number+1 AS num, s.* from (SELECT CODW.COD_DESC WARD, CODZ.COD_DESC ZONE, SUM(CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) ARREAR_COLLECTION, SUM((CASE WHEN BM.BM_IDNO IS NOT NULL AND RD.TAX_ID != (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END) + (CASE WHEN BM.BM_IDNO IS NULL AND RD.TAX_ID = (SELECT TAX_ID FROM tb_tax_mas WHERE TAX_CODE='WT114') THEN RD.RF_FEEAMOUNT ELSE 0 END)) CURRENT_COLLECTION FROM tb_receipt_mas RM INNER JOIN tb_receipt_det RD ON RM.RM_RCPTID = RD.RM_RCPTID LEFT JOIN tb_wt_bill_mas BM ON RD.BM_IDNO = BM.BM_IDNO AND BM.BM_YEAR = (SELECT FA_YEARID FROM tb_financialyear WHERE '" + startDate + "' BETWEEN FA_FROMDATE AND FA_TODATE) INNER JOIN tb_csmr_info CSMR ON CSMR.cs_idn = CAST(RM.ADDITIONAL_REF_NO AS UNSIGNED) INNER JOIN tb_comparent_det CODW ON CODW.COD_ID = CSMR.COD_DWZID2 INNER JOIN tb_comparent_det CODZ ON CODZ.COD_ID = CSMR.COD_DWZID3 WHERE RM.RECEIPT_DEL_FLAG IS NULL AND RM.RM_DATE >= '" + startDate + "' AND RM.RM_DATE <= '" + endDate + "' AND RM.RECEIPT_DEL_FLAG IS NULL AND CSMR.COD_DWZID2= '" + wardId + "' AND CSMR.COD_DWZID3= '" + zoneId + "' AND RM.DP_DEPTID = (SELECT DP_DEPTID FROM tb_department WHERE DP_DEPTCODE='WT') AND RM.RECEIPT_TYPE_FLAG IN ('R','A','ABY') GROUP BY CODW.COD_DESC, CODZ.COD_DESC ORDER BY CODZ.COD_DESC, CODZ.COD_DESC)"
				+ "s, (SELECT @row_number\\:=0) AS rn");
    
	    waterTaxWardAndZoneWiseCollectionEntity = (List<WaterTaxWardAndZoneWiseCollectionEntity>) entityManager
				.createNativeQuery(queryBuilder.toString(), WaterTaxWardAndZoneWiseCollectionEntity.class).getResultList();

	    log.error("Qeury execution ended getWaterTaxWardAndZoneWiseCollectionData");
	    
	    return waterTaxWardAndZoneWiseCollectionEntity;
	}
	
	
	public List<ApplicationGranularStatusEntity> getApplicationGranularStatusUsingNumeric(String applicationId) {
		
		List<ApplicationGranularStatusEntity> applicationGranularStatusEntity=null;
		
		String sql =
		        "WITH filtered_tasks AS ( " +
		        "    SELECT t.WFTASK_ID, t.WORKFLOW_REQ_ID, t.TASK_ID, t.TASK_NAME, " +
		        "           t.TASK_STATUS, t.WFTASK_ASSIGDATE, t.WFTASK_COMPDATE, t.WFTASK_ACTORID " +
		        "    FROM tb_workflow_task t " +
		        "    WHERE t.APM_APPLICATION_ID = :applicationId " +
		        "      AND t.TASK_NAME <> 'Start' " +
		        "), " +

		        "ordered_tasks AS ( " +
		        "    SELECT ft.*, " +
		        "           ROW_NUMBER() OVER ( " +
		        "               PARTITION BY ft.WORKFLOW_REQ_ID " +
		        "               ORDER BY ft.WFTASK_ASSIGDATE, ft.WFTASK_ID " +
		        "           ) AS DESK_SEQUENCE " +
		        "    FROM filtered_tasks ft " +
		        "), " +

		        "latest_valid_action AS ( " +
		        "    SELECT wa.WFTASK_ID, wa.DECISION, wa.DATE_OF_ACTION, wa.EMPID, wa.COMMENTS " +
		        "    FROM ( " +
		        "        SELECT wa.*, " +
		        "               ROW_NUMBER() OVER ( " +
		        "                   PARTITION BY wa.WFTASK_ID " +
		        "                   ORDER BY wa.DATE_OF_ACTION DESC " +
		        "               ) AS rn " +
		        "        FROM tb_workflow_action wa " +
		        "        WHERE wa.DECISION IN ('APPROVED', 'REJECTED', 'SEND_BACK') " +
		        "          AND wa.APM_APPLICATION_ID = :applicationId " +
		        "    ) wa " +
		        "    WHERE wa.rn = 1 " +
		        ") " +

		        "SELECT ot.DESK_SEQUENCE AS DeskSequence, " +
		        "       ot.TASK_NAME AS DeskName, " +
		        "       ot.TASK_STATUS AS Status, " +
		        "       la.DECISION AS Decision, " +
		        "       ot.WFTASK_ASSIGDATE AS AssignDateTime, " +
		        "       ot.WFTASK_COMPDATE AS CompletionDateTime, " +
		        "       CASE " +
		        "           WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.EMPID " +
		        "           WHEN ot.TASK_STATUS = 'PENDING' THEN ot.WFTASK_ACTORID " +
		        "       END AS ActionEmployeeId, " +
		        "		CASE " + 
				"		WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.COMMENTS " +
				"			ELSE NULL " +
				"		END AS ActionComments,"+
		        "       'Desk ' || ot.DESK_SEQUENCE AS Meaning " +
		        "FROM ordered_tasks ot " +
		        "LEFT JOIN latest_valid_action la " +
		        "       ON la.WFTASK_ID = ot.WFTASK_ID " +
		        "ORDER BY ot.DESK_SEQUENCE";

		    Query query = entityManager.createNativeQuery(
		            sql,
		            ApplicationGranularStatusEntity.class
		    ); 
		    query.setParameter("applicationId", applicationId);
 
		    /*entityManager.createNativeQuery(sql);
		    query.setParameter("applicationId", applicationId);*/
		    
/*		    applicationGranularStatusEntity = (List<ApplicationGranularStatusEntity>) entityManager
					.createNativeQuery(query.toString(), ApplicationGranularStatusEntity.class).getResultList();
*/
		    applicationGranularStatusEntity= query.getResultList();
		    log.error("Qeury execution ended getApplicationGranularStatusUsingNumeric");
		    
		    return applicationGranularStatusEntity;
		    
	}
	
	
	public List<ApplicationGranularStatusEntity> getApplicationGranularStatusUsingReferenceId(String referenceId) {
		
		List<ApplicationGranularStatusEntity> applicationGranularStatusEntity=null;
		
		/*String sql="WITH filtered_tasks AS ( SELECT t.WFTASK_ID, t.WORKFLOW_REQ_ID, t.TASK_ID, t.TASK_NAME, t.TASK_STATUS, t.WFTASK_ASSIGDATE, t.WFTASK_COMPDATE, t.WFTASK_ACTORID FROM tb_workflow_task t WHERE t.REFERENCE_ID =:referenceId AND t.TASK_NAME <> 'Start' ), ordered_tasks AS ( SELECT ft.*, ROW_NUMBER() OVER ( PARTITION BY ft.WORKFLOW_REQ_ID ORDER BY ft.WFTASK_ASSIGDATE, ft.WFTASK_ID ) AS DESK_SEQUENCE FROM filtered_tasks ft ), latest_valid_action AS ( SELECT wa.WFTASK_ID, wa.DECISION, wa.DATE_OF_ACTION, wa.EMPID, wa.COMMENTS FROM ( SELECT wa.*, ROW_NUMBER() OVER ( PARTITION BY wa.WFTASK_ID ORDER BY wa.DATE_OF_ACTION DESC ) AS rn FROM tb_workflow_action wa WHERE wa.DECISION IN ('APPROVED', 'REJECTED', 'SEND_BACK') AND wa.REFERENCE_ID =:referenceId ) wa WHERE wa.rn = 1 ) SELECT ot.DESK_SEQUENCE AS DeskSequence, ot.TASK_NAME AS DeskName, ot.TASK_STATUS AS Status, la.DECISION AS Decision, ot.WFTASK_ASSIGDATE AS AssignDateTime, ot.WFTASK_COMPDATE AS CompletionDateTime,COALESCE( CASE WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.EMPID ELSE ot.WFTASK_ACTORID END, ot.WFTASK_ACTORID ) AS ActionEmployeeId,CASE WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.COMMENTS ELSE NULL END AS ActionComments, 'Desk ' || ot.DESK_SEQUENCE AS Meaning FROM ordered_tasks ot LEFT JOIN latest_valid_action la ON la.WFTASK_ID = ot.WFTASK_ID ORDER BY ot.DESK_SEQUENCE";*/

		String sql="WITH filtered_tasks AS ( SELECT t.WFTASK_ID, t.WORKFLOW_REQ_ID, t.TASK_ID, t.TASK_NAME, t.TASK_STATUS, t.WFTASK_ASSIGDATE, t.WFTASK_COMPDATE, t.WFTASK_ACTORID FROM tb_workflow_task t WHERE t.REFERENCE_ID =:referenceId AND t.TASK_NAME <> 'Start' and t.TASK_NAME<>'Hidden_Task Requester' AND T.TASK_STATUS<>'PENDING'), ordered_tasks AS ( SELECT ft.*, ROW_NUMBER() OVER ( PARTITION BY ft.WORKFLOW_REQ_ID ORDER BY ft.WFTASK_ASSIGDATE, ft.WFTASK_ID ) AS DESK_SEQUENCE FROM filtered_tasks ft ), latest_valid_action AS ( SELECT wa.WFTASK_ID, wa.DECISION, wa.DATE_OF_ACTION, wa.EMPID, wa.COMMENTS FROM ( SELECT wa.*, ROW_NUMBER() OVER ( PARTITION BY wa.WFTASK_ID ORDER BY wa.DATE_OF_ACTION DESC ) AS rn FROM tb_workflow_action wa WHERE wa.DECISION IN ('APPROVED', 'REJECTED', 'SEND_BACK') AND wa.REFERENCE_ID =:referenceId ) wa WHERE wa.rn = 1 ) SELECT ot.DESK_SEQUENCE AS DeskSequence, ot.TASK_NAME AS DeskName, ot.TASK_STATUS AS Status, la.DECISION AS Decision, ot.WFTASK_ASSIGDATE AS AssignDateTime, ot.WFTASK_COMPDATE AS CompletionDateTime, COALESCE( CASE WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.EMPID ELSE ot.WFTASK_ACTORID END, ot.WFTASK_ACTORID ) AS ActionEmployeeId, CASE WHEN ot.TASK_STATUS = 'COMPLETED' THEN la.COMMENTS ELSE NULL END AS ActionComments, 'Desk ' || ot.DESK_SEQUENCE AS Meaning FROM ordered_tasks ot LEFT JOIN latest_valid_action la ON la.WFTASK_ID = ot.WFTASK_ID ORDER BY ot.DESK_SEQUENCE";

		
		
		    Query query = entityManager.createNativeQuery(
		            sql,
		            ApplicationGranularStatusEntity.class
		    ); 
		    query.setParameter("referenceId", referenceId);
 
		    
		    applicationGranularStatusEntity= query.getResultList();
		    log.error("Qeury execution ended getApplicationGranularStatusUsingReferenceId");
		    
		    return applicationGranularStatusEntity;
		    
		    
	}

	}
