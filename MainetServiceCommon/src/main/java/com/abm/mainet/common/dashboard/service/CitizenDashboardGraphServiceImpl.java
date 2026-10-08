package com.abm.mainet.common.dashboard.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriTemplateHandler;

import com.abm.mainet.common.constant.ServiceEndpoints;
import com.abm.mainet.common.dashboard.dao.CitizenDashboardGraphDAO;
import com.abm.mainet.common.dashboard.dao.ComplaintDashBoardDao;
import com.abm.mainet.common.dashboard.domain.*;
import com.abm.mainet.common.dashboard.dto.DeptByDaysAndDescDTO;
import com.abm.mainet.common.dashboard.dto.LegalCountsDTO;
import com.abm.mainet.common.domain.FinancialYear;
import com.abm.mainet.common.exception.FrameworkException;
import com.abm.mainet.common.utility.RestClient;

@Service
@Transactional(readOnly = true)
public class CitizenDashboardGraphServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(CitizenDashboardGraphServiceImpl.class);

	private static final String HOLD = "Hold";
	private static final String PENDING = "Pending";
	private static final String REJECTED = "Rejected";
	private static final String CLOSED = "Closed";

	@Autowired
	CitizenDashboardGraphDAO citizenDashboardGraphDAO;
	@Autowired
	ComplaintDashBoardDao complaintDashBoardDao;


	public List<CitizenDashboardGraphEntity> getCitizenDashboardGraph() {
		return citizenDashboardGraphDAO.getCitizenDashboardGraphEntityList();
	}

	public List<DropdownEntity> getdropdownSevenDaysEntityList() {
		return citizenDashboardGraphDAO.getDropdownSevenDaysEntityList();
	}

	public List<DropdownEntity> getDropdownSixMonthsEntityList() {
		return citizenDashboardGraphDAO.getDropdownSixMonthsEntity();
	}

	public List<DropdownEntity> getDropdownFinancialYrEntityList() {
		return citizenDashboardGraphDAO.getDropdownFinancialYrEntity();
	}

	public List<DropdownEntity> getDropdownHalfYearlyEntityList() {
		return citizenDashboardGraphDAO.getDropdownHalfYearlyEntity();
	}

	public List<DropdownEntity> getDropdownQuarterlyEntityList() {
		return citizenDashboardGraphDAO.getDropdownQuarterlyEntity();
	}

	public List<TotalCollectionSWMEntity> getTotalCollectionSWMEntityList() {
		return citizenDashboardGraphDAO.getTotalCollectionSWMEntityList();
	}

	public List<TotalServiceCntSWMEntity> getTotalServiceCntSWMEntityList() {
		return citizenDashboardGraphDAO.getTotalServiceCntSWMEntityList();
	}

	public List<TotalTransCntSWMEntity> getTotalTransCntSWMEntityList() {
		return citizenDashboardGraphDAO.getTotalTransCntSWMEntityList();
	}

	public List<TotalCollectionAmtSWMEntity> getTotalCollectionAmtSWMEntityList() {
		return citizenDashboardGraphDAO.getTotalCollectionAmtSWMEntityList();
	}

	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetEntityList() {
		return citizenDashboardGraphDAO.getVehicleTypeDryWetEntityList();
	}

	public List<VehicleAndWasteTypeByDaysEntity> getVehicleAndWasteByDaysEntityList(String vehicleType,
			String wasteType, int noOfDays, int orgId) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		if (vehicleType == null || vehicleType.trim().equalsIgnoreCase(""))
			vehicleType = null;
		if (wasteType == null || wasteType.trim().equalsIgnoreCase(""))
			wasteType = null;
		return citizenDashboardGraphDAO.getVehicleAndWasteByDaysEntityList(vehicleType, wasteType, noOfDays, orgId,
				toDate.toString());
	}

	public List<DryWasteHazSWMEntity> getGraphDryWasteEntityList() {
		return citizenDashboardGraphDAO.getGraphDryWasteEntityList();
	}

	public List<DryWasteHazSWMEntity> getGraphWetWasteEntityList() {
		return citizenDashboardGraphDAO.getGraphWetWasteEntityList();
	}

	public List<DryWasteHazSWMEntity> getGraphHazWasteEntityList() {
		return citizenDashboardGraphDAO.getGraphHazWasteEntityList();
	}

	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetFinancialDurationWiseEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getVehicleTypeDryWetFinancialDurationWiseEntityList(noOfDays,
				toDate.toString());
	}

	public List<DryWasteHazSWMEntity> getGraphDryWasteFinancialDurationWiseEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getGraphDryWasteFinancialDurationWiseEntityList(noOfDays, toDate.toString());
	}

	public List<DryWasteHazSWMEntity> getGraphWetWasteFinancialDurationWiseEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getGraphWetWasteFinancialDurationWiseEntityList(noOfDays, toDate.toString());
	}

	public List<DryWasteHazSWMEntity> getGraphHazWasteFinancialDurationWiseEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getGraphHazWasteFinancialDurationWiseEntityList(noOfDays, toDate.toString());
	}

	public List<DrillDownWasteTypeWiseEntity> getDrillDownDryWasteTypeWiseEntityList(String wasteType, String codDesc,
			int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		if (codDesc == null || codDesc.trim().equalsIgnoreCase(""))
			codDesc = null;
		if (wasteType == null || wasteType.trim().equalsIgnoreCase(""))
			wasteType = null;
		return citizenDashboardGraphDAO.getDrillDownDryWasteTypeWiseEntityList(wasteType, codDesc, noOfDays,
				toDate.toString());
	}

	public List<FinancialYearSWMEntity> getFinancialYrSWMEntityList() {
		return citizenDashboardGraphDAO.getFinancialYrSWMEntityList();
	}

	public List<FinancialYearSWMEntity> getHalfYrSWMEntityList() {
		return citizenDashboardGraphDAO.getHalfYrSWMEntityList();
	}

	public List<FinancialYearSWMEntity> getLastFourQuarterSWMEntityList() {
		return citizenDashboardGraphDAO.getLastFourQuarterSWMEntityList();
	}

	public List<FinancialYearSWMEntity> getLastSevenDaysSWMEntityList() {
		return citizenDashboardGraphDAO.getLastSevenDaysSWMEntityList();
	}

	public List<MonthlySWMEntity> getMonthlySWMEntityList() {
		return citizenDashboardGraphDAO.getMonthlySWMEntityList();
	}

	public List<FirstTableSWMEntity> getFirstTableSWMEntityList() {
		return citizenDashboardGraphDAO.getFirstTableSWMEntityList();
	}

	public List<FirstTableYearwiseSWMEntity> getFirstTableYearlySWMEntityList(int orgId) {
		return citizenDashboardGraphDAO.getFirstTableYearlySWMEntityList(orgId);
	}

	public List<FirstTableYearwiseSWMEntity> getFirstTableCurrYearSWMEntityList(int orgId) {
		return citizenDashboardGraphDAO.getFirstTableCurrYearSWMEntityList(orgId);
	}

	public List<DeptComplaintsEntity> getDeptComplaintsEntityList() {
		return citizenDashboardGraphDAO.getDeptComplaintsEntityList();
	}

	public List<ModeComplaintsEntity> getModeComplaintsEntityList() {
		return citizenDashboardGraphDAO.getModeComplaintsEntityList();
	}

	public List<DeptComplaintsEntity> getDeptComplaintsByDaysEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getDeptComplaintsByDaysEntityList(noOfDays);
	}

	public int getNoOfDays(int noOfDays) {
		if (noOfDays == 5) {
			noOfDays = 365;
		} else if (noOfDays == 4) {
			noOfDays = 180;
		} else if (noOfDays == 3) {
			noOfDays = 90;
		} else if (noOfDays == 2) {
			noOfDays = 30;
		} else {
			noOfDays = 1;
		}
		return noOfDays;
	}

	public List<ModeComplaintsEntity> getModeComplaintsByDaysEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getModeComplaintsByDaysEntityList(noOfDays);
	}

	public List<SLAAnalysisEntity> getSLAAnalysisEntityList() {
		return citizenDashboardGraphDAO.getSLAAnalysisEntityList();
	}

	public List<OrganizationEntity> getOrgEntityList() {
		return citizenDashboardGraphDAO.getOrgEntityList();
	}

	public List<DurationEntity> getYearlyDurationEntityList() {
		return citizenDashboardGraphDAO.getYearlyDurationEntityList();
	}

	public List<DurationEntity> getHalfYearlyDurationEntityList() {
		return citizenDashboardGraphDAO.getHalfYearlyDurationEntityList();
	}

	public List<DurationEntity> getQuarterlyDurationEntityList() {
		return citizenDashboardGraphDAO.getQuarterlyDurationEntityList();
	}

	public DashboardAllCountsEntity getDashboardAllCounts() {
		return citizenDashboardGraphDAO.getDashboardAllCounts();
	}

	public HRMSStoreCountEntity getDashboardHRMSCount() {
		return citizenDashboardGraphDAO.getDashboardHRMSCount();
	}

	public HRMSStoreCountEntity getDashboardStoreCount() {
		return citizenDashboardGraphDAO.getDashboardStoreCount();
	}

	public List<DeptByDaysAndDescDTO> getDeptComplaintsByDaysAndDescEntityList(int noOfDays, String desc) {
		noOfDays = getNoOfDays(noOfDays);
		if (desc == null || desc.trim().equalsIgnoreCase(""))
			desc = null;
		List<DeptByDaysAndDescDTO> deptByDaysAndDescList = new ArrayList<DeptByDaysAndDescDTO>();
		List<DeptByDaysAndDescEntity> list = citizenDashboardGraphDAO.getDeptComplaintsByDaysAndDescEntityList(noOfDays,
				desc);
		if (list != null) {
			DeptByDaysAndDescDTO dto = null;
			for (DeptByDaysAndDescEntity dept : list) {
				dto = new DeptByDaysAndDescDTO();
				BeanUtils.copyProperties(dept, dto);
				if (dept.getHold() == 1) {
					dto.setStatus(HOLD);
				} else if (dept.getPending() == 1) {
					dto.setStatus(PENDING);
				} else if (dept.getRejected() == 1) {
					dto.setStatus(REJECTED);
				} else if (dept.getClosed() == 1) {
					dto.setStatus(CLOSED);
				}
				deptByDaysAndDescList.add(dto);
			}
		}
		return deptByDaysAndDescList;
	}

	public List<DeptByDaysAndDescDTO> getDeptComplaintsForTodayEntityList() {
		List<DeptByDaysAndDescDTO> deptByDaysAndDescList = new ArrayList<DeptByDaysAndDescDTO>();
		List<DeptByDaysAndDescEntity> list = citizenDashboardGraphDAO.getDeptComplaintsForTodayEntityList();
		if (list != null) {
			DeptByDaysAndDescDTO dto = null;
			for (DeptByDaysAndDescEntity dept : list) {
				dto = new DeptByDaysAndDescDTO();
				BeanUtils.copyProperties(dept, dto);
				if (dept.getHold() == 1) {
					dto.setStatus(HOLD);
				} else if (dept.getPending() == 1) {
					dto.setStatus(PENDING);
				} else if (dept.getRejected() == 1) {
					dto.setStatus(REJECTED);
				} else if (dept.getClosed() == 1) {
					dto.setStatus(CLOSED);
				}
				deptByDaysAndDescList.add(dto);
			}
		}
		return deptByDaysAndDescList;
	}
	
	public List<DeptByDaysAndDescDTO> getModeComplaintsByDaysAndDescEntityList(int noOfDays, String desc) {
		noOfDays = getNoOfDays(noOfDays);
		if (desc == null || desc.trim().equalsIgnoreCase(""))
			desc = null;
		List<DeptByDaysAndDescDTO> modeByDaysAndDescList = new ArrayList<DeptByDaysAndDescDTO>();
		List<DeptByDaysAndDescEntity> list = citizenDashboardGraphDAO.getModeComplaintsByDaysAndDescEntityList(noOfDays,
				desc);
		if (list != null) {
			DeptByDaysAndDescDTO dto = null;
			for (DeptByDaysAndDescEntity dept : list) {
				dto = new DeptByDaysAndDescDTO();
				BeanUtils.copyProperties(dept, dto);
				if (dept.getHold() == 1) {
					dto.setStatus(HOLD);
				} else if (dept.getPending() == 1) {
					dto.setStatus(PENDING);
				} else if (dept.getRejected() == 1) {
					dto.setStatus(REJECTED);
				} else if (dept.getClosed() == 1) {
					dto.setStatus(CLOSED);
				}
				modeByDaysAndDescList.add(dto);
			}
		}
		return modeByDaysAndDescList;
	}

	public List<YearWiseGrievanceGraphEntity> getYearWiseGraphEntityList() {
		return citizenDashboardGraphDAO.getYearWiseGraphEntityList();
	}

	/*
	 * RTI Module APIs
	 */

	public List<SLAAnalysisEntity> getSLAAnalysisEntityRTIList() {
		return citizenDashboardGraphDAO.getSLAAnalysisEntityRTIList();
	}

	public List<DropdownEntity> getdropdownSevenDaysRTIEntityList() {
		return citizenDashboardGraphDAO.getDropdownSevenDaysRTIEntityList();
	}

	public List<DropdownEntity> getDropdownSixMonthsRTIEntityList() {
		return citizenDashboardGraphDAO.getDropdownSixMonthsRTIEntity();
	}

	public List<DropdownEntity> getDropdownFinancialYrRTIEntityList() {
		return citizenDashboardGraphDAO.getDropdownFinancialYrRTIEntity();
	}

	public List<DropdownEntity> getDropdownHalfYearlyRTIEntityList() {
		return citizenDashboardGraphDAO.getDropdownHalfYearlyRTIEntity();
	}

	public List<DropdownEntity> getDropdownQuarterlyRTIEntityList() {
		return citizenDashboardGraphDAO.getDropdownQuarterlyRTIEntity();
	}

	public List<DeptComplaintsEntity> getDeptComplaintsRTIEntityList() {
		return citizenDashboardGraphDAO.getDeptComplaintsRTIEntityList();
	}

	public List<ModeComplaintsEntity> getModeComplaintsRTIEntityList() {
		return citizenDashboardGraphDAO.getModeComplaintsRTIEntityList();
	}

	public List<DeptComplaintsEntity> getDeptComplaintsByDaysRTIEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getDeptComplaintsByDaysRTIEntityList(noOfDays);
	}

	public List<ModeComplaintsEntity> getModeComplaintsByDaysRTIEntityList(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getModeComplaintsByDaysRTIEntityList(noOfDays);
	}

	public List<DeptByDaysAndDescDTO> getDeptComplaintsByDaysAndDescRTIEntityList(int noOfDays, String desc) {
		noOfDays = getNoOfDays(noOfDays);
		if (desc == null || desc.trim().equalsIgnoreCase(""))
			desc = null;
		List<DeptByDaysAndDescDTO> deptByDaysAndDescList = new ArrayList<DeptByDaysAndDescDTO>();
		List<DeptByDaysAndDescEntity> list = citizenDashboardGraphDAO
				.getDeptComplaintsByDaysAndDescRTIEntityList(noOfDays, desc);
		if (list != null) {
			DeptByDaysAndDescDTO dto = null;
			for (DeptByDaysAndDescEntity dept : list) {
				dto = new DeptByDaysAndDescDTO();
				BeanUtils.copyProperties(dept, dto);
				if (dept.getHold() == 1) {
					dto.setStatus(HOLD);
				} else if (dept.getPending() == 1) {
					dto.setStatus(PENDING);
				} else if (dept.getRejected() == 1) {
					dto.setStatus(REJECTED);
				} else if (dept.getClosed() == 1) {
					dto.setStatus(CLOSED);
				}
				deptByDaysAndDescList.add(dto);
			}
		}
		return deptByDaysAndDescList;
	}

	public List<DeptByDaysAndDescDTO> getModeComplaintsByDaysAndDescRTIEntityList(int noOfDays, String desc) {
		noOfDays = getNoOfDays(noOfDays);
		if (desc == null || desc.trim().equalsIgnoreCase(""))
			desc = null;
		List<DeptByDaysAndDescDTO> modeByDaysAndDescList = new ArrayList<DeptByDaysAndDescDTO>();
		List<DeptByDaysAndDescEntity> list = citizenDashboardGraphDAO
				.getModeComplaintsByDaysAndDescRTIEntityList(noOfDays, desc);
		if (list != null) {
			DeptByDaysAndDescDTO dto = null;
			for (DeptByDaysAndDescEntity dept : list) {
				dto = new DeptByDaysAndDescDTO();
				BeanUtils.copyProperties(dept, dto);
				if (dept.getHold() == 1) {
					dto.setStatus(HOLD);
				} else if (dept.getPending() == 1) {
					dto.setStatus(PENDING);
				} else if (dept.getRejected() == 1) {
					dto.setStatus(REJECTED);
				} else if (dept.getClosed() == 1) {
					dto.setStatus(CLOSED);
				}
				modeByDaysAndDescList.add(dto);
			}
		}
		return modeByDaysAndDescList;
	}

	public List<YearWiseGrievanceGraphEntity> getYearWiseGraphRTIEntityList() {
		return citizenDashboardGraphDAO.getYearWiseGraphRTIEntityList();
	}

	public RTIApplicationCountEntity getRTIFirstAppealCount() {
		return citizenDashboardGraphDAO.getRTIFirstAppealCount();
	}

	public RTIApplicationCountEntity getRTISecondAppealCount() {
		return citizenDashboardGraphDAO.getRTISecondAppealCount();
	}

	public RTIApplicationCountEntity getRTIOpenFirstAndSecondAppealCount() {
		return citizenDashboardGraphDAO.getRTIOpenFirstAndSecondAppealCount();
	}

	public RTIApplicationCountEntity getRTIClosedFirstAndSecondAppealCount() {
		return citizenDashboardGraphDAO.getRTIClosedFirstAndSecondAppealCount();
	}

	public RTIApplicationCountEntity getTotalReceivedRTIApplications() {
		return citizenDashboardGraphDAO.getTotalReceivedRTIApplications();
	}

	/*
	 * Legal Module APIs
	 */

	public List<LegalCountsDTO> getLegalCountsEntityList() {
		List<LegalCountsDTO> legalCountsDTOs = new ArrayList<LegalCountsDTO>();
		List<LegalCountsEntity> legalReceivedCounts = citizenDashboardGraphDAO.getLegalReceivedYrWiseEntityList();
		List<LegalCountsEntity> legalClosedCounts = citizenDashboardGraphDAO.getLegalClosedYrWisetEntityList();
		if (legalReceivedCounts != null && legalClosedCounts != null) {

			for (LegalCountsEntity legalEntity : legalReceivedCounts) {
				LegalCountsDTO legalDto = new LegalCountsDTO();
				legalDto.setReceived(legalEntity.getCount());
				legalDto.setYear(legalEntity.getYear());

				for (LegalCountsEntity legalClEntity : legalClosedCounts) {
					if (legalEntity.getYear().equals(legalClEntity.getYear())) {
						legalDto.setClosed(legalClEntity.getCount());
						legalDto.setPending(legalEntity.getCount() - legalClEntity.getCount());
					}
				}
				legalCountsDTOs.add(legalDto);
			}
		}
		return legalCountsDTOs;
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseReceivedLegalCount() {
		return citizenDashboardGraphDAO.getDeptWiseReceivedLegalCount();
	}

	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseReceivedLegalCount() {
		return citizenDashboardGraphDAO.getCourtWiseReceivedLegalCount();
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseYearlyReceivedLegalCount(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getDeptWiseReceivedLegalCount(noOfDays);
	}

	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseYearlyReceivedLegalCount(int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		return citizenDashboardGraphDAO.getCourtWiseReceivedLegalCount(noOfDays);
	}

	public List<DeptAndCourtWiseAllLegalDataEntity> getDeptWiseAllLegalData(String deptName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		return citizenDashboardGraphDAO.getDeptWiseAllLegalData(deptName, noOfDays);
	}

	public List<DeptAndCourtWiseAllLegalDataEntity> getCourtWiseAllLegalData(String courtName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (courtName == null || courtName.trim().equalsIgnoreCase(""))
			courtName = null;
		return citizenDashboardGraphDAO.getCourtWiseAllLegalData(courtName, noOfDays);
	}

	/*
	 * Legislative Query Management module APIs
	 */

	public List<YearwiseTrendAnalysisEntity> getYrWiseTrendAnalysisEntityList() {
		return citizenDashboardGraphDAO.getYrWiseTrendAnalysisEntityList();
	}

	public List<DeptWiseTrendAnalysisEntity> getDeptWiseTrendAnalysisEntityList() {
		return citizenDashboardGraphDAO.getDeptWiseTrendAnalysisEntityList();
	}

	public List<YearwiseTrendAnalysisEntity> getCurrYrTrendAnalysisEntityList() {
		return citizenDashboardGraphDAO.getCurrYrTrendAnalysisEntityList();
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPQuestionCount(String deptName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseLQPQuestionCount(deptName, noOfDays, toDate.toString());
	}

	public List<DeptWiseQuestionDataLQPEntity> getDeptWiseLQPQuestionData(String deptName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseLQPQuestionData(deptName, noOfDays, toDate.toString());
	}

	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPAnswerCount(String deptName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseLQPAnswerCount(deptName, noOfDays, toDate.toString());
	}

	public List<DeptWiseAnswerDataLQPEntity> getDeptWiseLQPAnswerData(String deptName, int noOfDays) {
		noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseLQPAnswerData(deptName, noOfDays, toDate.toString());
	}

	/*
	 * Council Module APIs
	 */

	public List<CouncilProposalCntEntity> getCouncilProposalCount() {
		return citizenDashboardGraphDAO.getCouncilProposalCount();
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilProposalCount(String deptName,
			int noOfDays) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptAndDurationWiseCouncilProposalCount(deptName, noOfDays,
				toDate.toString());
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilProposalCount(String deptName, int noOfDays) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getYearWiseCouncilProposalCount(deptName, noOfDays, toDate.toString());
	}

	public List<CouncilDeptWiseProposalDataEntity> getDeptWiseProposalGridData(String deptName, int noOfDays,
			int orgId) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseProposalGridData(deptName, noOfDays, toDate.toString(), orgId);
	}

	public List<CouncilProposalCntEntity> getCouncilAgendaCount() {
		return citizenDashboardGraphDAO.getCouncilAgendaCount();
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilAgendaCount() {
		return citizenDashboardGraphDAO.getYearWiseCouncilAgendaCount();
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilAgendaCount(String deptName, int noOfDays) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptAndDurationWiseCouncilAgendaCount(deptName, noOfDays, toDate.toString());
	}

	public List<CouncilDeptWiseAgendaDataEntity> getDeptWiseAgendaGridData(String deptName, int noOfDays, int orgId) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseAgendaGridData(deptName, noOfDays, toDate.toString(), orgId);
	}

	public List<CouncilProposalCntEntity> getCouncilMeetingCount() {
		return citizenDashboardGraphDAO.getCouncilMeetingCount();
	}

	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilMeetingCount() {
		return citizenDashboardGraphDAO.getYearWiseCouncilMeetingCount();
	}

	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilMeetingCount(String deptName, int noOfDays) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptAndDurationWiseCouncilMeetingCount(deptName, noOfDays,
				toDate.toString());
	}

	public List<CouncilDeptWiseMeetingDataEntity> getDeptWiseMeetingGridData(String deptName, int noOfDays, int orgId) {
		if (noOfDays != 0)
			noOfDays = getNoOfDays(noOfDays);
		if (deptName == null || deptName.trim().equalsIgnoreCase(""))
			deptName = null;
		FinancialYear financialYear = getFinancialYear();
		Date toDate = financialYear.getFaToDate();
		return citizenDashboardGraphDAO.getDeptWiseMeetingGridData(deptName, noOfDays, toDate.toString(), orgId);
	}

	private FinancialYear getFinancialYear() {
		return citizenDashboardGraphDAO.getFinancialYear();
	}

	
	private FinancialYear getCurrentFinancialYear() {
		return citizenDashboardGraphDAO.getCurrentFinancialYear();
	}
	/*
	 * SKDCL common APIs
	 */

	public SKDCLDashboardAllCountsEntity getSKDCLDashboardAllCounts() {
		return citizenDashboardGraphDAO.getSKDCLDashboardAllCounts();
	}

	/*
	 * HRMS Module APIs
	 */

	public List<HRMSCategoryWiseEmpCntEntity> getStatusWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getStatusWiseEmployeeCount();
	}

	public List<HRMSEmployeeGridDataEntity> getStatusWiseEmployeeData(String status) {
		if (status == null || status.trim().equalsIgnoreCase(""))
			status = null;
		return citizenDashboardGraphDAO.getEmployeeData(status, "STATUS");
	}

	public List<HRMSCategoryWiseEmpCntEntity> getCategoryWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getCategoryWiseEmployeeCount();
	}

	public List<HRMSEmployeeGridDataEntity> getCategoryWiseEmployeeData(String category) {
		if (category == null || category.trim().equalsIgnoreCase(""))
			category = null;
		return citizenDashboardGraphDAO.getEmployeeData(category, "CATEGORY");
	}

	public List<HRMSCategoryWiseEmpCntEntity> getDepartmentWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getDepartmentWiseEmployeeCount();
	}

	public List<HRMSEmployeeGridDataEntity> getDepartmentWiseEmployeeData(String dept) {
		if (dept == null || dept.trim().equalsIgnoreCase(""))
			dept = null;
		return citizenDashboardGraphDAO.getEmployeeData(dept, "DEPARTMENT");
	}

	public List<HRMSGenderRatioEntity> getGenderWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getGenderWiseEmployeeCount();
	}

	public List<HRMSEmployeeGridDataEntity> getGenderWiseEmployeeData(String gender) {
		if (gender == null || gender.trim().equalsIgnoreCase(""))
			gender = null;
		return citizenDashboardGraphDAO.getEmployeeData(gender, "GENDER");
	}

	public List<HRMSSalaryBreakdownEntity> getSalaryRangeWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getSalaryRangeWiseEmployeeCount();
	}

	public List<HRMSEmployeeGridDataEntity> getSalaryRangeDeptWiseEmployeeData(String dept, String salaryRange) {
		if (dept == null || dept.trim().equalsIgnoreCase(""))
			dept = null;
		if (salaryRange == null || salaryRange.trim().equalsIgnoreCase(""))
			salaryRange = null;
		return citizenDashboardGraphDAO.getSalaryRangeDeptWiseEmployeeData(dept, salaryRange);
	}

	public List<HRMSCategoryWiseEmpCntEntity> getJoiningYearWiseEmployeeCount() {
		return citizenDashboardGraphDAO.getJoiningYearWiseEmployeeCount();
	}
	
	public List<HRMSEmployeeGridDataEntity> getJoiningYearWiseEmployeeData(String year) {
		if (year == null || year.trim().equalsIgnoreCase(""))
			year = null;
		return citizenDashboardGraphDAO.getEmployeeData(year, "JOINING_YEAR");
	}

	public List<HRMSRecruitmentPostsEntity> getVacantPostInfo() {
		return citizenDashboardGraphDAO.getVacantPostInfo();
	}

	/*
	 * Store Module APIs
	 */

	public List<StoreItemCountEntity> getItemGroupWiseInventoryValue() {
		return citizenDashboardGraphDAO.getItemGroupWiseInventoryValue();
	}

	public List<StoreItemWiseInventoryCntEntity> getItemWiseInventoryValue() {
		return citizenDashboardGraphDAO.getItemWiseInventoryValue();
	}

	public List<StoreItemCountEntity> getItemGroupWiseItemCount() {
		return citizenDashboardGraphDAO.getItemGroupWiseItemCount();
	}

	public List<StoreItemCountEntity> getTopNMovingItemCounts(int limit) {
		return citizenDashboardGraphDAO.getTopNMovingItemCounts(limit);
	}

	public List<StoreItemDataEntity> getItemDataForGroup(String itemGroup) {
		if (itemGroup == null || itemGroup.trim().equalsIgnoreCase(""))
			itemGroup = null;
		return citizenDashboardGraphDAO.getItemDataForGroup(itemGroup);
	}

	// #120768
	public List<CouncilDeptWiseProposalCntEntity> getDeptWiseCouncilProposalCount(Long orgId) {
		FinancialYear financialYear = getCurrentFinancialYear();
		Date toDate = financialYear.getFaToDate();
		Date fromDate = financialYear.getFaFromDate();
		log.info("deptwise countil proposal count method is called");
		return citizenDashboardGraphDAO.getOrgIdWiseCouncilProposalCount(orgId, fromDate.toString(), toDate.toString());

	}

	public List<RTIApplicationByApplicantType> getRTIApplicationByApplicantType() {
		
		return  citizenDashboardGraphDAO.getRTIApplicationByApplicantType();
	}

	public List<RTIApplicationyBplFlagEntity> getRTIApplicationDetailByBplFactorWise() {
		
		return citizenDashboardGraphDAO.getRTIApplicationDetailByBplFactorWise();
	}

	public List<CouncilMeetingCountYrwise> getYearWiseCouncilMeetingCountByOrgId(Long orgId) {
		log.info("year wise countil meeting count method is called");
		return citizenDashboardGraphDAO.getYearWiseCouncilMeetingCountByOrgId(orgId);
	}

	public List<FinancialAndNonFinacialProposalCount> getFinancialAndNonFiancialProposalCountByOrgId(Long orgId) {
		log.info("year wise countil meeting count method is called");
		return citizenDashboardGraphDAO.getFinancialAndNonFiancialProposalCountByOrgId(orgId);
	}

	
	public List<CouncilMeetingCountYrwise> getYearWiseAgendaCountByOrgId(Long orgId) {
		log.info("year wise countil meeting count method is called");
		return citizenDashboardGraphDAO.getYearWiseAgendaCountByOrgId(orgId);
	}

	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByBplType(String bplType) {
		return citizenDashboardGraphDAO.getRtiDrillDownDataByBplType(bplType);
	}

	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByApplicantType(String applicantType) {
		return citizenDashboardGraphDAO.getRtiDrillDownDataByApplicantType(applicantType);
	}

	public List<RtiApplicationDtoByCondEntity> getRFAOpenClosedDrillDownData() {
		return citizenDashboardGraphDAO.getRFAOpenClosedDrillDownData();
	}

	public List<RtiApplicationDtoByCondEntity> getRSAPendingClosedDrillDownData() {
		return citizenDashboardGraphDAO.getRSAPendingClosedDrillDownData();
	}

	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRfaOpenClosedData() {
		return citizenDashboardGraphDAO.getIndvidualRtiRfaOpenClosedData();
	}

	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRSAOpenClosedData() {
		return citizenDashboardGraphDAO.getIndvidualRtiRSAOpenClosedData();
	}

	public List<AdvocateMastRegEntity> getAdvocateMstRegCount() {
		return citizenDashboardGraphDAO.getAdvocateMstRegCount();
	}
	
	/* For fetching Complaint drilldown details with Parameter 
	D#152479
	 * */

	public List<ComplaintEntity> getTotComplaintAndStatus() {
		return complaintDashBoardDao.getTotComplaintAndStatus();
	}

	public List<CommonComplaintEntity> getTotComplaintAndStatusByCategory(String category) {
		return complaintDashBoardDao.getTotComplaintAndStatusByCategory(category);
	}


	public List<CommonComplaintEntity> getTotComplaintAndStatusByWard(String ward) {
		
		return complaintDashBoardDao.getgetTotComplaintAndStatusByWard(ward);
	}

	public List<CommonComplaintEntity> getTotComplaintAndStatusByZone(String zone) {
		
		return complaintDashBoardDao.getTotComplaintAndStatusByZone(zone);
	}

	public List<CommonComplaintEntity> getTotComplaintAndStatusByDept(String deptId) {
		return complaintDashBoardDao.getTotComplaintAndStatusByDept(deptId);
	}

	public List<CommonComplaintEntity> getTotComplaintAndStatusBySubCategory(String subCategory) {
		return complaintDashBoardDao.getTotComplaintAndStatusBySubCategory(subCategory);
	}

	public List<ComplaintFinancialYearEntity> getComplaintFinancialYearEntity() {
		return citizenDashboardGraphDAO.getComplaintFinancialYearEntityList();
	}

	public List<ComplaintsModeWiseEntity> getDepartmentComplaintsModeWiseEntity() {
		return citizenDashboardGraphDAO.getComplaintModeWiseEntityList();
	}
	
	public List<ComplaintsDepartmentWiseEntity> getDepartmentComplaintsDeparmentWise() {
		return citizenDashboardGraphDAO.getDepartmentComplaintsDeparmentWise();
	}

	public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailBylicNofDatetDate(Long ward2, String fDate, String tDate) {
	
		 return citizenDashboardGraphDAO.getLicenseDetailBylicNofDatetDate(ward2,fDate,tDate);
	}
	public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailByYear(String fDate, String tDate) {
		
		 return citizenDashboardGraphDAO.getLicenseDetailByYear(fDate,tDate);
	}
	public List<LicensedtlByNoAndAppIdEntity> getLicenseByNoAndAppId(Long ward2, String fDate, String tDate) {
	
		return citizenDashboardGraphDAO.getLicenseByNoAndAppId(ward2,fDate,tDate);
	}

	public List<LicenseDeatilsByServceIdAndDeptIdEntity> getLicenseDeatilsByServceIdAndDeptId(Long orgId, Long deptId,
			Long serviceId, Long trdWard2, String chequeStatus, String formDate, String toDate) {
		
		 return citizenDashboardGraphDAO.getLicenseDeatilsByServceIdAndDeptId( orgId,  deptId,
				 serviceId,  trdWard2,  chequeStatus,  formDate,  toDate);
	}

	public List<LicenseRecieptDeatilsByServceIdAndDeptIdEntity> getLicenseRecieptDeatilsByServceIdAndDeptIdEntity(
			Long orgId, Long deptId, Long serviceId, Long trdWard2, String chequeStatus, String formDate,
			String toDate) {
		
		 return citizenDashboardGraphDAO.getLicenseRecieptDeatilsByServceIdAndDeptIdEntity( orgId,  deptId,
				 serviceId,  trdWard2,  chequeStatus,  formDate,  toDate);
	}
	
	public List<WaterDataWardZoneWiseEntity> getWaterDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		return citizenDashboardGraphDAO.getWaterDataWardZoneWise(wardId, zoneId, fromDate, toDate);
	}

	public List<WaterReceiptDataEntity> getWaterReceiptDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		return citizenDashboardGraphDAO.getWaterReceiptDataWardZoneWise(wardId, zoneId, fromDate, toDate);
	}

	public List<WaterReceiptDetailDataEntity> getWaterReceiptDetailDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		return citizenDashboardGraphDAO.getWaterReceiptDetailDataWardZoneWise(wardId, zoneId, fromDate, toDate);

	}

	public List<WholePropertyDataEntity> getWholePropertyDataWardZoneWise(String wardId, String zoneId, String fromDate,
			String toDate) {
		return citizenDashboardGraphDAO.getWholePropertyDataWardZoneWise(wardId, zoneId, fromDate, toDate);

	}

	public List<IndividualPropertyDataEntity> getIndividualPropertyDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		return citizenDashboardGraphDAO.getIndividualPropertyDataWardZoneWise(wardId, zoneId, fromDate, toDate);

	}

	public List<PropertyReceiptDataWiseEntity> getPropertyReceiptDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		return citizenDashboardGraphDAO.getPropertyReceiptDataWardZoneWise(wardId, zoneId, fromDate, toDate);

	}

	public List<PropertyReceiptDetailDataEntity> getPropertyReceiptDetailDataWardZoneWise(String wardId, String zoneId,
			String fromDate, String toDate) {
		return citizenDashboardGraphDAO.getPropertyReceiptDetailDataWardZoneWise(wardId, zoneId, fromDate, toDate);
	}
	
	public void updateWorkflowAutoEscalationTaskWrtAppId(Long appId) {

		log.info("Schedular for updateWorkflowAutoEscalationTaskWrtAppId Task Started");

        
        Map<String, List<Long>> appIdDetails = new LinkedHashMap<String, List<Long>>();
        
        try{
            appIdDetails.put("applicationId", Arrays.asList(appId));

            String url = ServiceEndpoints.WorkflowExecutionURLS.WORKFLOW_AUTO_ESCALATION_TASK_WRT_APPID;

            updateAllNewlyGenertaedTask(appIdDetails, url);
        }catch (Exception ex) {
            throw new FrameworkException(
                    "Exception occured while calling updateWorkflowAutoEscalationTaskWrtAppId :" + ex);
        }

        log.info("Schedular for updateWorkflowAutoEscalationTaskWrtAppId end ");

    }
	
	private void updateAllNewlyGenertaedTask(Map<String, List<Long>> requestParam, String url) {
        ResponseEntity<?> responseEntity = null;
        DefaultUriTemplateHandler dd = new DefaultUriTemplateHandler();
        dd.setParsePath(true);
        try {
            responseEntity = RestClient.callRestTemplateClient(requestParam, url);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode == HttpStatus.OK) {
            	log.info("Schedular for work flow run successfully ");
            }
        } catch (Exception ex) {
            throw new FrameworkException(
                    "Exception occured while calling Schedular :" + requestParam, ex);
        }
    }
	
	
	public Object[] getMarriageData(String regNo){
		return citizenDashboardGraphDAO.getMarriageData(regNo); 
	}
	
	public Object[] getBirthRegCertificateData(String regNo, String regDate, String dob){
		return citizenDashboardGraphDAO.getBirthCertificateData(regNo,regDate,dob); 
	}
	
	public List<PropertyStatisticsServiceDepartmentEntity> getPropertyStatisticsServiceWise(String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getPropertyStatisticsServiceWise(startDate, endDate);
	}

	public List<ModeWiseCollectionReportEntity> getModeWiseData(String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getmodeWiseCollectionReport(startDate, endDate);
	}
	
	public List<SummaryCollectionDataReportEntity> getSummaryWiseData(String startDate, String endDate, String ward, String zone, String tarrifType1,
			String tarrifType2, String tarrifType3, String meterType, String conSize) {
	    return citizenDashboardGraphDAO.getSummaryCollectionList(startDate, endDate, ward, zone, tarrifType1, tarrifType2, tarrifType3, meterType, conSize);
	}
	
	public List<WardwisePropertyCollectionReportEntity> getWardWisePropertyData(String startDate, String endDate, String wardId, String zoneId) {
	    return citizenDashboardGraphDAO.getwardWiseCollectionPropertyList(startDate, endDate, wardId, zoneId);
	}
	
	
	public List<DailyPropertyCollectionReportEntity> getDailyPropertyData() {
	    return citizenDashboardGraphDAO.getDailyPropertyCollectionList();
	}
	
	public List<ZoneWisePropertyCollectionReportEntity> getZoneWiseProperty(String startDate, String endDate, String wardId, String zoneId) {
	    return citizenDashboardGraphDAO.getZoneWiseCollectionPropertyList(startDate, endDate, wardId, zoneId);
	}
	
	public Object[] getDeathRegCertificateData(String regNo, String regDate, String drDod){
		return citizenDashboardGraphDAO.getDeathCertiDataList(regNo,regDate,drDod); 
	}
	
	public Object[] getDigilockerPropertyCertificate(String appId, String flatNO){
		return citizenDashboardGraphDAO.getPropAssessmentCertifiacteData(appId,flatNO); 
	}
	
	public List<SlaWiseGrievanceSumaaryEntity> getGreaveanceSummaryReportData(String fromDateStr, String toDateStr, String deptIdStr, String subTypeIdStr,
	        String codIdLevel1, String codIdLevel2, String codIdLevel3, String codIdLevel4, String codIdLevel5, String status) {
	    return citizenDashboardGraphDAO.getGrieveanceSummryReport(fromDateStr, toDateStr, deptIdStr, subTypeIdStr, codIdLevel1, codIdLevel2, codIdLevel3,
	    		codIdLevel4, codIdLevel5, status);
	}
	
	public List<BirthCertificateEntityForMobile> getBirthCertificateDatas(String appId, String orgId) {
	    return citizenDashboardGraphDAO.getBirthCertificateDataForMobile(appId, orgId);
	}
	
	public List<DeathCertificateEntityForMobile> getDeathCertificateDatas(String appId, String orgId) {
	    return citizenDashboardGraphDAO.getDeathCertificateDataForMobile(appId, orgId);
	}
	
	public List<TopTenToHunAllWardEntity> getTopTenToHundredAllWardWiseData(String number) {
	    return citizenDashboardGraphDAO.getTopTenToHunAllWardDetails(number);
	}
	
	public List<TopTenToHunAllWardEntity> getTopTenToHundredWardWiseData(String wardId, String number) {
	    return citizenDashboardGraphDAO.getTopTenToHunWardWiseDetails(wardId, number);
	}
	
	
	public List<TopTenToHunAllWardEntity> getTopTenToHundredWardZoneData(String number, String wardId, String zoneId) {
	    return citizenDashboardGraphDAO.getTopTenToHunWardZoneWiseDetails(number, wardId, zoneId);
	}
	
	public List<LegalIllegalPropWardWiseEntity> getLegalIlleProWardWiseData() {
	    return citizenDashboardGraphDAO.getLegalIllegalPropWardWiseData();
	}
	
	public List<LegalIllegalPropZoneWiseEntity> getLegalIlleProZoneWiseData(String wardId) {
	    return citizenDashboardGraphDAO.getLegalIllegalPropZoneWiseData(wardId);
	}
	
	public List<UsageWardWisePropertyCountEntity> getUsageWardWisePropCount() {
	    return citizenDashboardGraphDAO.getUsageWardWisePropCount();
	}
	
	
	public List<UsageZoneWisePropertyCountEntity> getUsageZoneWisePropCount(String wardId) {
	    return citizenDashboardGraphDAO.getUsageZoneWisePropCount(wardId);
	}
	
	
	public List<WardAndModeWisePropertyCollectionEntity> getwardAndModeWisePropData(String wardId, String zoneId, String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getPropWardAndModeWiseCollection(wardId, zoneId, startDate, endDate);
	}
	
	public List<ZoneAndModeWisePropertyCollectionEntity> getzoneAndModeWisePropData(String wardId, String zoneId, String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getPropZoneAndModeWiseCollection(wardId, zoneId, startDate, endDate);
	}
	
	
	public List<AbhayYojnaCollectionWardWiseEntity> getWardWiseAbhayYojnaData(String wardId, String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getWardWiseAbhayYojnaCollection(wardId, startDate, endDate);
	}
	
	public List<AbhayYojnaCollectionZoneWiseEntity> getZoneWiseAbhayYojnaData(String wardId, String zoneId, String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getZoneWiseAbhayYojnaCollection(wardId, zoneId, startDate, endDate);
	}
	
	public List<DuplicateReceiptBpmsEntity> getBpmsIntegrationEntity() {
	    return citizenDashboardGraphDAO.getBpmsIntegrationEntity();
	}
	
	public List<WaterAbhayYojnaWardZoneWiseEntity> getWaterWardWiseAbhayYojnaData(String startDate, String endDate, String wardId) {
	    return citizenDashboardGraphDAO.getWaterWardZoneWiseAbhayYojnaCollection(startDate, endDate, wardId);
	}
	
	public List<WaterAbhayYojnaAllWardWiseEntity> getWaterAllWardWiseAbhayYojnaData(String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getWaterAllWardWiseAbhayYojnaCollection(startDate, endDate);
	}
	
	public List<WaterWardWiseUsageCountEntity> getWaterWardWiseUsageCountData() {
	    return citizenDashboardGraphDAO.getWaterWardWiseUsageCount();
	}
	
	public List<WaterWardAndZoneWiseUsageCountEntity> getWaterWardZoneWiseUsageCountData(String wardId) {
	    return citizenDashboardGraphDAO.getWaterWardZoneWiseUsageCount(wardId);
	}
	
	public List<WaterTaxWardWiseAuthoUnathoCountEntity> getWaterWardWiseAuthoAndUnauthoCountData() {
	    return citizenDashboardGraphDAO.getWaterWardeWiseAuthorUnauthoCount();
	}
	
	public List<WaterTaxWardZoneWiseAuthoUnathoCountEntity> getWaterWardZoneWiseAuthoAndUnauthoCountData(String wardId) {
	    return citizenDashboardGraphDAO.getWaterWardeAndZoneWiseAuthorUnauthoCount(wardId);
	}
	
	public List<WaterTaxWardWiseDemandCollectionEntity> getWaterWardWiseDemandCollectionData(String startDate) {
	    return citizenDashboardGraphDAO.getWaterWardWiseDemandCollection(startDate);
	}
	
	public List<WaterTaxZoneWiseDemandCollectionEntity> getWaterWiseZoneDemandCollectionData(String startDate, String wardId) {
	    return citizenDashboardGraphDAO.getWaterZoneWiseDemandCollection(startDate, wardId);
	}
	
	public List<WaterTaxModeWardWiseData> getWaterModeWiseWardsDataList(String startDate) {
	    return citizenDashboardGraphDAO.getWaterModeWiseWardData(startDate);
	}
	
	public List<WaterTaxModeWardAndZoneWiseDataEntity> getWaterModeWiseWardAndZoneDataList(String startDate,String wardId) {
	    return citizenDashboardGraphDAO.getWaterModeWiseWardAndZoneData(startDate, wardId);
	}
	
	public List<WaterAllWardWiseTopTenToHundredDefaulterEntity> getWaterTopTenToHunAllWardWiseDataList(String startDate,String num) {
	    return citizenDashboardGraphDAO.getWaterTopTenToHunAllWardWiseData(startDate, num);
	}
	
	public List<WaterTenHunWardZoneEntity> getWaterTenToHunWardWiseDataList(String startDate, String wardId,String num) {
	    return citizenDashboardGraphDAO.getWaterTopTenToHunWardData(startDate, wardId, num);
	}
	
	public List<WaterTenHunWardZoneEntity> getWaterTenToHunWardZoneWiseDataList(String startDate, String wardId, String zoneId,String num) {
	    return citizenDashboardGraphDAO.getWaterTopTenToHunWardZoneWiseData(startDate, wardId, zoneId, num);
	}
	
	public List<WaterTaxWardWiseCollectionEntity> getWaterWardWiseCollectionDataList(String startDate, String endDate) {
	    return citizenDashboardGraphDAO.getWaterTaxWardWiseCollectionData(startDate, endDate);
	}
	
	public List<WaterWardWiseCollectionEntity> getWardWiseCollectionDataList(String startDate, String endDate, String wardId) {
	    return citizenDashboardGraphDAO.getWaterOnlyWardWiseCollectionData(startDate, endDate, wardId);
	}
	
	public List<WaterTaxWardAndZoneWiseCollectionEntity> getWaterWardAndZoneWiseCollectionDataList(String startDate, String endDate, String wardId, String zoneId) {
	    return citizenDashboardGraphDAO.getWaterTaxWardAndZoneWiseCollectionData(startDate, endDate, wardId, zoneId);
	}
	
	public List<ApplicationGranularStatusEntity> getApplicationGranularStatusUsingNumeric(String applicationId){
		return citizenDashboardGraphDAO.getApplicationGranularStatusUsingNumeric(applicationId);
	}
	
	public List<ApplicationGranularStatusEntity> getApplicationGranularStatusUsingReferenceId(String referenceId){
		return citizenDashboardGraphDAO.getApplicationGranularStatusUsingReferenceId(referenceId);
	}
	
}
