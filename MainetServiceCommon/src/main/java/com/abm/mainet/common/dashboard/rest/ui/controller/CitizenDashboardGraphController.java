package com.abm.mainet.common.dashboard.rest.ui.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


import javax.jws.WebService;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abm.mainet.common.constant.MainetConstants;
import com.abm.mainet.common.dashboard.domain.*;
import com.abm.mainet.common.dashboard.dto.DeptByDaysAndDescDTO;
import com.abm.mainet.common.dashboard.dto.LegalCountsDTO;
import com.abm.mainet.common.dashboard.dto.LicenseDeatilsByServceIdAndDeptId;
import com.abm.mainet.common.dashboard.dto.LicenseDetailsDto;
import com.abm.mainet.common.dashboard.dto.LicenseRecieptDeatilsByServceIdAndDeptId;
import com.abm.mainet.common.dashboard.dto.LicensedtlByNoAndAppId;
import com.abm.mainet.common.dashboard.service.CitizenDashboardGraphServiceImpl;
import com.abm.mainet.common.utility.MainetTenantUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@ServletSecurity(httpMethodConstraints = {
		@HttpMethodConstraint(value = "POST", transportGuarantee = TransportGuarantee.CONFIDENTIAL) })
@RestController
@RequestMapping("/citizenDashboardGraph")
@Produces("application/json")
@WebService
@Api(value = "/citizenDashboardGraph")
@Path("/citizenDashboardGraph")
public class CitizenDashboardGraphController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CitizenDashboardGraphController.class);

	@Autowired
	CitizenDashboardGraphServiceImpl citizenDashboardGraphServiceImpl;
	
	
	@RequestMapping(value = "/getDashBoardGraphData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDashBoardGraphData")
	@ApiOperation(value = "Get Dashboard Graph Data", notes = "Get Dashboard Graph Data", response = CitizenDashboardGraphEntity.class, responseContainer = "List")
	public List<CitizenDashboardGraphEntity> getAllGrievances() {

		try {
			return citizenDashboardGraphServiceImpl.getCitizenDashboardGraph();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDropdownSevenDaysData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDropdownSevenDaysData")
	@ApiOperation(value = "Get Dropdown 7 days", notes = "Get Dropdown 7 days", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getDropdownSevenDays() {

		try {
			return citizenDashboardGraphServiceImpl.getdropdownSevenDaysEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDropdownSixMonthsData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDropdownSixMonthsData")
	@ApiOperation(value = "Get Dropdown 6 months", notes = "Get Dropdown 6 months", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getDropdownSixMonths() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownSixMonthsEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDropdownFinancialYearData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDropdownFinancialYearData")
	@ApiOperation(value = "Get Dropdown Financial Year", notes = "Get Dropdown Financial Year", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getDropdownFinancialYearData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownFinancialYrEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDropdownHalfYearlyData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDropdownHalfYearlyData")
	@ApiOperation(value = "Get Dropdown Half Year", notes = "Get Dropdown Half Year", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getDropdownHalfYearlyData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownHalfYearlyEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDropdownQuarterlyData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDropdownQuarterlyData")
	@ApiOperation(value = "Get Dropdown Quarterly", notes = "Get Dropdown Quarterly", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getDropdownQuarterlyData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownQuarterlyEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTotalCollectionSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTotalCollectionSWM")
	@ApiOperation(value = "Get Total Collection SWM", notes = "Get Total Collection SWM", response = TotalCollectionSWMEntity.class, responseContainer = "List")
	public List<TotalCollectionSWMEntity> getTotalCollectionSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getTotalCollectionSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTotalServiceCountSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTotalServiceCountSWM")
	@ApiOperation(value = "Get Total Service Count SWM", notes = "Get Total Service Count SWM", response = TotalServiceCntSWMEntity.class, responseContainer = "List")
	public List<TotalServiceCntSWMEntity> getTotalServiceCountSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getTotalServiceCntSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTotalTransactionCountSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTotalTransactionCountSWM")
	@ApiOperation(value = "Get Total Transaction Count SWM", notes = "Get Total Transaction Count SWM", response = TotalTransCntSWMEntity.class, responseContainer = "List")
	public List<TotalTransCntSWMEntity> getTotalTransCountSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getTotalTransCntSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTotalCollectionAmountSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTotalCollectionAmountSWM")
	@ApiOperation(value = "Get Total Collection Amount SWM", notes = "Get Total Collection Amount SWM", response = TotalCollectionAmtSWMEntity.class, responseContainer = "List")
	public List<TotalCollectionAmtSWMEntity> getTotalCollectionAmountSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getTotalCollectionAmtSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getVehicleTypeDryWetSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getVehicleTypeDryWetSWM")
	@ApiOperation(value = "Get Vehicle Type Dry Wet SWM", notes = "Get Vehicle Type Dry Wet SWM", response = VehicleTypeDryWetEntity.class, responseContainer = "List")
	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getVehicleTypeDryWetEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getVehicleAndWasteByTypeAndDaysSWM/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getVehicleAndWasteByTypeAndDaysSWM/{noOfDays}")
	@ApiOperation(value = "Get Vehicle And Waste By Type And Days SWM", notes = "Get Vehicle And Waste By Type And Days SWM", response = VehicleAndWasteTypeByDaysEntity.class, responseContainer = "List")
	public List<VehicleAndWasteTypeByDaysEntity> getVehicleAndWasteByTypeAndDaysSWM(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "vehicleType") @QueryParam(value = "vehicleType") String vehicleType,
			@RequestParam(name = "wasteType") @QueryParam(value = "wasteType") String wasteType) {

		try {
			return citizenDashboardGraphServiceImpl.getVehicleAndWasteByDaysEntityList(vehicleType, wasteType, noOfDays,
					13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphDryWasteSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphDryWasteSWM")
	@ApiOperation(value = "Get Graph Dry Waste SWM", notes = "Get Graph Dry Waste SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphDryWasteSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getGraphDryWasteEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphWetWasteSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphWetWasteSWM")
	@ApiOperation(value = "Get Graph Wet Waste SWM", notes = "Get Graph Wet Waste SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphWetWasteSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getGraphWetWasteEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphHazardousWasteSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphHazardousWasteSWM")
	@ApiOperation(value = "Get Graph Hazardous Waste SWM", notes = "Get Graph Hazardous Waste SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphHazardousWasteSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getGraphHazWasteEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getVehicleTypeDryWetFinancialDurationWiseEntityList/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getVehicleTypeDryWetFinancialDurationWiseEntityList/{noOfDays}")
	@ApiOperation(value = "Get Vehicle Type Financial Duration Wise SWM", notes = "Get Vehicle Type Financial Duration Wise SWM", response = VehicleTypeDryWetEntity.class, responseContainer = "List")
	public List<VehicleTypeDryWetEntity> getVehicleTypeDryWetFinancialDurationWiseEntityList(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getVehicleTypeDryWetFinancialDurationWiseEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphDryWasteFinancialDurationWiseEntityList/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphDryWasteFinancialDurationWiseEntityList/{noOfDays}")
	@ApiOperation(value = "Get Graph Dry Waste Duration Wise SWM", notes = "Get Graph Dry Waste Duration Wise SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphDryWasteFinancialDurationWiseEntityList(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getGraphDryWasteFinancialDurationWiseEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphWetWasteFinancialDurationWiseEntityList/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphWetWasteFinancialDurationWiseEntityList/{noOfDays}")
	@ApiOperation(value = "Get Graph Wet Waste Duration Wise SWM", notes = "Get Graph Wet Waste Duration Wise SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphWetWasteFinancialDurationWiseEntityList(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getGraphWetWasteFinancialDurationWiseEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGraphHazWasteFinancialDurationWiseEntityList/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGraphHazWasteFinancialDurationWiseEntityList/{noOfDays}")
	@ApiOperation(value = "Get Graph Hazardous Waste Duration Wise SWM", notes = "Get Graph Hazardous Waste Duration Wise SWM", response = DryWasteHazSWMEntity.class, responseContainer = "List")
	public List<DryWasteHazSWMEntity> getGraphHazWasteFinancialDurationWiseEntityList(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getGraphHazWasteFinancialDurationWiseEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDrillDownWasteTypeDayWiseSWM/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDrillDownWasteTypeDayWiseSWM/{noOfDays}")
	@ApiOperation(value = "Get Drilldown By Waste Type Duration Wise SWM", notes = "Get Drilldown By Waste Type Duration Wise SWM", response = DrillDownWasteTypeWiseEntity.class, responseContainer = "List")
	public List<DrillDownWasteTypeWiseEntity> getDrillDownDryWasteTypeWiseEntityList(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "codDesc") @QueryParam(value = "codDesc") String codDesc,
			@RequestParam(name = "wasteType") @QueryParam(value = "wasteType") String wasteType) {

		try {
			return citizenDashboardGraphServiceImpl.getDrillDownDryWasteTypeWiseEntityList(wasteType, codDesc,
					noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getFinancialYearsSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getFinancialYearsSWM")
	@ApiOperation(value = "Get Financial Years SWM", notes = "Get Financial Years SWM", response = FinancialYearSWMEntity.class, responseContainer = "List")
	public List<FinancialYearSWMEntity> getFinancialYearsSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getFinancialYrSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getHalfYearSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getHalfYearSWM")
	@ApiOperation(value = "Get Half Year SWM", notes = "Get Half Year SWM", response = FinancialYearSWMEntity.class, responseContainer = "List")
	public List<FinancialYearSWMEntity> getHalfYearSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getHalfYrSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getLastFourQuarterSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getLastFourQuarterSWM")
	@ApiOperation(value = "Get last 4 quarter SWM", notes = "Get last 4 quarter SWM", response = FinancialYearSWMEntity.class, responseContainer = "List")
	public List<FinancialYearSWMEntity> getLastFourQuarterSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getLastFourQuarterSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getLastSevenDaysSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getLastSevenDaysSWM")
	@ApiOperation(value = "Get last 7 days SWM", notes = "Get last 7 days SWM", response = FinancialYearSWMEntity.class, responseContainer = "List")
	public List<FinancialYearSWMEntity> getLastSevenDaysSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getLastSevenDaysSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getMonthlySixMonthsSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getMonthlySixMonthsSWM")
	@ApiOperation(value = "Get monthly 6 months SWM", notes = "Get monthly 6 months SWM", response = MonthlySWMEntity.class, responseContainer = "List")
	public List<MonthlySWMEntity> getMonthlySixMonthsSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getMonthlySWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getFirstTableSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getFirstTableSWM")
	@ApiOperation(value = "Get First Table SWM", notes = "Get First Table SWM", response = FirstTableSWMEntity.class, responseContainer = "List")
	public List<FirstTableSWMEntity> getFirstTableSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getFirstTableSWMEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getFirstTableYearlySWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getFirstTableYearlySWM")
	@ApiOperation(value = "Get First Table Yearly SWM", notes = "Get First Table Yearly SWM", response = FirstTableYearwiseSWMEntity.class, responseContainer = "List")
	public List<FirstTableYearwiseSWMEntity> getFirstTableYearlySWM() {

		try {
			return citizenDashboardGraphServiceImpl.getFirstTableYearlySWMEntityList(13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getFirstTableCurrYearSWM", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getFirstTableCurrYearSWM")
	@ApiOperation(value = "Get First Table Current Year SWM", notes = "Get First Table Current Year SWM", response = FirstTableYearwiseSWMEntity.class, responseContainer = "List")
	public List<FirstTableYearwiseSWMEntity> getFirstTableCurrYearSWM() {

		try {
			return citizenDashboardGraphServiceImpl.getFirstTableCurrYearSWMEntityList(13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentComplaints")
	@ApiOperation(value = "Get Department Complaints", notes = "Get Department Complaints", response = DeptComplaintsEntity.class, responseContainer = "List")
	public List<DeptComplaintsEntity> getDepartmentComplaints() {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getModeComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getModeComplaints")
	@ApiOperation(value = "Get Mode Complaints", notes = "Get Mode Complaints", response = ModeComplaintsEntity.class, responseContainer = "List")
	public List<ModeComplaintsEntity> getModeComplaints() {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentComplaintsByDays/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentComplaintsByDays/{noOfDays}")
	@ApiOperation(value = "Get Department Complaints By Duration", notes = "Get Department Complaints By Duration", response = DeptComplaintsEntity.class, responseContainer = "List")
	public List<DeptComplaintsEntity> getDepartmentComplaintsByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsByDaysEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getModeComplaintsByDays/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getModeComplaintsByDays/{noOfDays}")
	@ApiOperation(value = "Get Mode Complaints By Duration", notes = "Get Mode Complaints By Duration", response = ModeComplaintsEntity.class, responseContainer = "List")
	public List<ModeComplaintsEntity> getModeComplaintsByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsByDaysEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getSLAAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getSLAAnalysis")
	@ApiOperation(value = "Get SLA Analysis", notes = "Get SLA Analysis", response = SLAAnalysisEntity.class, responseContainer = "List")
	public List<SLAAnalysisEntity> getSLAAnalysis() {

		try {
			return citizenDashboardGraphServiceImpl.getSLAAnalysisEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getOrganisations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getOrganisations")
	@ApiOperation(value = "Get Organisations", notes = "Get Organisations", response = OrganizationEntity.class, responseContainer = "List")
	public List<OrganizationEntity> getOrganisations() {

		try {
			return citizenDashboardGraphServiceImpl.getOrgEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearlyDurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearlyDurations")
	@ApiOperation(value = "Get Yearly Durations", notes = "Get Yearly Durations", response = DurationEntity.class, responseContainer = "List")
	public List<DurationEntity> getYearlyDurations() {

		try {
			return citizenDashboardGraphServiceImpl.getYearlyDurationEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getHalfYearlyDurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getHalfYearlyDurations")
	@ApiOperation(value = "Get Half Yearly Durations", notes = "Get Half Yearly Durations", response = DurationEntity.class, responseContainer = "List")
	public List<DurationEntity> getHalfYearlyDurations() {

		try {
			return citizenDashboardGraphServiceImpl.getHalfYearlyDurationEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getQuarterlyDurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getQuarterlyDurations")
	@ApiOperation(value = "Get Quarterly Durations", notes = "Get Quarterly Durations", response = DurationEntity.class, responseContainer = "List")
	public List<DurationEntity> getQuarterlyDurations() {

		try {
			return citizenDashboardGraphServiceImpl.getQuarterlyDurationEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDashboardAllCounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDashboardAllCounts")
	@ApiOperation(value = "Get Dashboard All Counts", notes = "Get Dashboard All Counts", response = DashboardAllCountsEntity.class)
	public DashboardAllCountsEntity getDashboardAllCounts() {

		try {
			return citizenDashboardGraphServiceImpl.getDashboardAllCounts();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDashboardHRMSCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDashboardHRMSCount")
	@ApiOperation(value = "Get Dashboard HRMS Count", notes = "Get Dashboard HRMS Count", response = HRMSStoreCountEntity.class)
	public HRMSStoreCountEntity getDashboardHRMSCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getDashboardHRMSCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDashboardStoreCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDashboardStoreCount")
	@ApiOperation(value = "Get Dashboard Store Count", notes = "Get Dashboard Store Count", response = HRMSStoreCountEntity.class)
	public HRMSStoreCountEntity getDashboardStoreCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getDashboardStoreCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentComplaintsByDaysAndDesc/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentComplaintsByDaysAndDesc/{noOfDays}")
	@ApiOperation(value = "Get Department Complaints By Duration And Description", notes = "Get Department Complaints By Duration And Description", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
	public List<DeptByDaysAndDescDTO> getDepartmentComplaintsByDaysAndDesc(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "desc") @QueryParam(value = "desc") String desc) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsByDaysAndDescEntityList(noOfDays, desc);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getModeComplaintsByDaysAndDesc/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getModeComplaintsByDaysAndDesc/{noOfDays}")
	@ApiOperation(value = "Get Mode Complaints By Duration And Description", notes = "Get Mode Complaints By Duration And Description", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
	public List<DeptByDaysAndDescDTO> getModeComplaintsByDaysAndDesc(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "desc") @QueryParam(value = "desc") String desc) {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsByDaysAndDescEntityList(noOfDays, desc);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearwiseGrievanceStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearwiseGrievanceStatus")
	@ApiOperation(value = "Get Year Wise Grievance Status", notes = "Get Year Wise Grievance Status", response = YearWiseGrievanceGraphEntity.class, responseContainer = "List")
	public List<YearWiseGrievanceGraphEntity> getYearwiseGrievanceStatus() {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseGraphEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * RTI Module APIs
	 */

	@RequestMapping(value = "/getRTISLAAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTISLAAnalysis")
	@ApiOperation(value = "Get RTI SLA Analysis", notes = "Get RTI SLA Analysis", response = SLAAnalysisEntity.class, responseContainer = "List")
	public List<SLAAnalysisEntity> getRTISLAAnalysis() {

		try {
			return citizenDashboardGraphServiceImpl.getSLAAnalysisEntityRTIList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDropdownSevenDaysData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDropdownSevenDaysData")
	@ApiOperation(value = "Get RTI Dropdown 7 Days Data", notes = "Get RTI Dropdown 7 Days Data", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getRTIDropdownSevenDays() {

		try {
			return citizenDashboardGraphServiceImpl.getdropdownSevenDaysRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDropdownSixMonthsData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDropdownSixMonthsData")
	@ApiOperation(value = "Get RTI Dropdown 6 Months Data", notes = "Get RTI Dropdown 6 Months Data", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getRTIDropdownSixMonths() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownSixMonthsRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDropdownFinancialYearData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDropdownFinancialYearData")
	@ApiOperation(value = "Get RTI Dropdown Financial Year Data", notes = "Get RTI Dropdown Financial Year Data", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getRTIDropdownFinancialYearData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownFinancialYrRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDropdownHalfYearlyData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDropdownHalfYearlyData")
	@ApiOperation(value = "Get RTI Dropdown Half Yearly Data", notes = "Get RTI Dropdown Half Yearly Data", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getRTIDropdownHalfYearlyData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownHalfYearlyRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDropdownQuarterlyData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDropdownQuarterlyData")
	@ApiOperation(value = "Get RTI Dropdown Quarterly Data", notes = "Get RTI Dropdown Quarterly Data", response = DropdownEntity.class, responseContainer = "List")
	public List<DropdownEntity> getRTIDropdownQuarterlyData() {

		try {
			return citizenDashboardGraphServiceImpl.getDropdownQuarterlyRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDepartmentComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDepartmentComplaints")
	@ApiOperation(value = "Get RTI Department Complaints", notes = "Get RTI Department Complaints", response = DeptComplaintsEntity.class, responseContainer = "List")
	public List<DeptComplaintsEntity> getRTIDepartmentComplaints() {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIModeComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIModeComplaints")
	@ApiOperation(value = "Get RTI Mode Complaints", notes = "Get RTI Mode Complaints", response = ModeComplaintsEntity.class, responseContainer = "List")
	public List<ModeComplaintsEntity> getRTIModeComplaints() {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDepartmentComplaintsByDays/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDepartmentComplaintsByDays/{noOfDays}")
	@ApiOperation(value = "Get RTI Department Complaints By Duration", notes = "Get RTI Department Complaints By Duration", response = DeptComplaintsEntity.class, responseContainer = "List")
	public List<DeptComplaintsEntity> getRTIDepartmentComplaintsByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsByDaysRTIEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIModeComplaintsByDays/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIModeComplaintsByDays/{noOfDays}")
	@ApiOperation(value = "Get RTI Mode Complaints By Duration", notes = "Get RTI Mode Complaints By Duration", response = ModeComplaintsEntity.class, responseContainer = "List")
	public List<ModeComplaintsEntity> getRTIModeComplaintsByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsByDaysRTIEntityList(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIDepartmentComplaintsByDaysAndDesc/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIDepartmentComplaintsByDaysAndDesc/{noOfDays}")
	@ApiOperation(value = "Get RTI Department Complaints By Duration", notes = "Get RTI Department Complaints By Duration", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
	public List<DeptByDaysAndDescDTO> getRTIDepartmentComplaintsByDaysAndDesc(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "desc") @QueryParam(value = "desc") String desc) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsByDaysAndDescRTIEntityList(noOfDays, desc);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIModeComplaintsByDaysAndDesc/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIModeComplaintsByDaysAndDesc/{noOfDays}")
	@ApiOperation(value = "Get RTI Mode Complaints By Duration", notes = "Get RTI Mode Complaints By Duration", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
	public List<DeptByDaysAndDescDTO> getRTIModeComplaintsByDaysAndDesc(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "desc") @QueryParam(value = "desc") String desc) {

		try {
			return citizenDashboardGraphServiceImpl.getModeComplaintsByDaysAndDescRTIEntityList(noOfDays, desc);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearwiseRTIStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearwiseRTIStatus")
	@ApiOperation(value = "Get Yearwise RTI Status", notes = "Get Yearwise RTI Status", response = YearWiseGrievanceGraphEntity.class, responseContainer = "List")
	public List<YearWiseGrievanceGraphEntity> getYearwiseRTIStatus() {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseGraphRTIEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIFirstAppealCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIFirstAppealCount")
	@ApiOperation(value = "Get RTI First Appeal Count", notes = "Get RTI First Appeal Count", response = RTIApplicationCountEntity.class)
	public RTIApplicationCountEntity getRTIFirstAppealCount() {

		try {
			return citizenDashboardGraphServiceImpl.getRTIFirstAppealCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTISecondAppealCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTISecondAppealCount")
	@ApiOperation(value = "Get RTI Second Appeal Count", notes = "Get RTI Second Appeal Count", response = RTIApplicationCountEntity.class)
	public RTIApplicationCountEntity getRTISecondAppealCount() {

		try {
			return citizenDashboardGraphServiceImpl.getRTISecondAppealCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIOpenFirstAndSecondAppealCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIOpenFirstAndSecondAppealCount")
	@ApiOperation(value = "Get RTI Open First And Second Appeal Count", notes = "Get RTI Open First And Second Appeal Count", response = RTIApplicationCountEntity.class)
	public RTIApplicationCountEntity getRTIOpenFirstAndSecondAppealCount() {

		try {
			return citizenDashboardGraphServiceImpl.getRTIOpenFirstAndSecondAppealCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIClosedFirstAndSecondAppealCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIClosedFirstAndSecondAppealCount")
	@ApiOperation(value = "Get RTI Closed First And Second Appeal Count", notes = "Get RTI Closed First And Second Appeal Count", response = RTIApplicationCountEntity.class)
	public RTIApplicationCountEntity getRTIClosedFirstAndSecondAppealCount() {

		try {
			return citizenDashboardGraphServiceImpl.getRTIClosedFirstAndSecondAppealCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTotalReceivedRTIApplications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTotalReceivedRTIApplications")
	@ApiOperation(value = "Get Total Received RTI Applications", notes = "Get Total Received RTI Applications", response = RTIApplicationCountEntity.class)
	public RTIApplicationCountEntity getTotalReceivedRTIApplications() {

		try {
			return citizenDashboardGraphServiceImpl.getTotalReceivedRTIApplications();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIApplicationByApplicantType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIApplicationByApplicantType")
	@ApiOperation(value = "Get Total Received RTI Applications By Applicant Type", notes = "Get Total Received RTI Applications By Applicant Type", response = RTIApplicationByApplicantType.class)
	public List<RTIApplicationByApplicantType> getRTIApplicationByApplicantType() {

		try {
			return citizenDashboardGraphServiceImpl.getRTIApplicationByApplicantType();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getRTIApplicationDetailByBplFactorWise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRTIApplicationDetailByBplFactorWise")
	@ApiOperation(value = "Get  Received RTI Applications By BPL factor wise", notes = "Get  Received RTI Applications By BPL factor wise", response = RTIApplicationyBplFlagEntity.class)
	public List<RTIApplicationyBplFlagEntity> getRTIApplicationDetailByBplFactorWise() {

		try {
			return (List<RTIApplicationyBplFlagEntity>) citizenDashboardGraphServiceImpl.getRTIApplicationDetailByBplFactorWise();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}
	/*
	 * Legal Module APIs
	 */

	@RequestMapping(value = "/getLegalAllCounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getLegalAllCounts")
	@ApiOperation(value = "Get Legal All Counts", notes = "Get Legal All Counts", response = LegalCountsDTO.class, responseContainer = "List")
	public List<LegalCountsDTO> getLegalAllCounts() {

		try {
			return citizenDashboardGraphServiceImpl.getLegalCountsEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseReceivedLegalCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseReceivedLegalCount")
	@ApiOperation(value = "Get Department Wise Received Legal Count", notes = "Get Department Wise Received Legal Count", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseReceivedLegalCount() {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseReceivedLegalCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCourtWiseReceivedLegalCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCourtWiseReceivedLegalCount")
	@ApiOperation(value = "Get Court Wise Received Legal Count", notes = "Get Court Wise Received Legal Count", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseReceivedLegalCount() {

		try {
			return citizenDashboardGraphServiceImpl.getCourtWiseReceivedLegalCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseReceivedLegalCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseReceivedLegalCount/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Received Legal Count By Duration", notes = "Get Department Wise Received Legal Count By Duration", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseYearlyReceivedLegalCount(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseYearlyReceivedLegalCount(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCourtWiseReceivedLegalCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCourtWiseReceivedLegalCount/{noOfDays}")
	@ApiOperation(value = "Get Court Wise Received Legal Count By Duration", notes = "Get Court Wise Received Legal Count By Duration", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getCourtWiseYearlyReceivedLegalCount(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays) {

		try {
			return citizenDashboardGraphServiceImpl.getCourtWiseYearlyReceivedLegalCount(noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseAllLegalData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseAllLegalData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Legal Data", notes = "Get Department Wise Legal Data", response = DeptAndCourtWiseAllLegalDataEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseAllLegalDataEntity> getDeptWiseAllLegalData(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseAllLegalData(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCourtWiseAllLegalData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCourtWiseAllLegalData/{noOfDays}")
	@ApiOperation(value = "Get Court Wise Legal Data", notes = "Get Court Wise Legal Data", response = DeptAndCourtWiseAllLegalDataEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseAllLegalDataEntity> getCourtWiseAllLegalData(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "courtName") @QueryParam(value = "courtName") String courtName) {

		try {
			return citizenDashboardGraphServiceImpl.getCourtWiseAllLegalData(courtName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * Legislative Query Management module APIs
	 */

	@RequestMapping(value = "/getYearwiseTrendAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearwiseTrendAnalysis")
	@ApiOperation(value = "Get Yearwise Trend Analysis LQP", notes = "Get Yearwise Trend Analysis LQP", response = YearwiseTrendAnalysisEntity.class, responseContainer = "List")
	public List<YearwiseTrendAnalysisEntity> getYearwiseTrendAnalysis() {

		try {
			return citizenDashboardGraphServiceImpl.getYrWiseTrendAnalysisEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentwiseTrendAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentwiseTrendAnalysis")
	@ApiOperation(value = "Get Department Wise Trend Analysis LQP", notes = "Get Department Wise Trend Analysis LQP", response = DeptWiseTrendAnalysisEntity.class, responseContainer = "List")
	public List<DeptWiseTrendAnalysisEntity> getDepartmentwiseTrendAnalysis() {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseTrendAnalysisEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCurrentYearTrendAnalysisLQP", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCurrentYearTrendAnalysisLQP")
	@ApiOperation(value = "Get Current Year Trend Analysis LQP", notes = "Get Current Year Trend Analysis LQP", response = YearwiseTrendAnalysisEntity.class, responseContainer = "List")
	public List<YearwiseTrendAnalysisEntity> getCurrentYearTrendAnalysisLQP() {

		try {
			return citizenDashboardGraphServiceImpl.getCurrYrTrendAnalysisEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseLQPQuestionCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseLQPQuestionCount/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Question Count LQP", notes = "Get Department Wise Question Count LQP", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPQuestionCount(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseLQPQuestionCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseLQPQuestionData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseLQPQuestionData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Question Data LQP", notes = "Get Department Wise Question Data LQP", response = DeptWiseQuestionDataLQPEntity.class, responseContainer = "List")
	public List<DeptWiseQuestionDataLQPEntity> getDeptWiseLQPQuestionData(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseLQPQuestionData(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseLQPAnswerCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseLQPAnswerCount/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Answer Count LQP", notes = "Get Department Wise Answer Count LQP", response = DeptAndCourtWiseLegalCntEntity.class, responseContainer = "List")
	public List<DeptAndCourtWiseLegalCntEntity> getDeptWiseLQPAnswerCount(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseLQPAnswerCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseLQPAnswerData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseLQPAnswerData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Answer Data LQP", notes = "Get Department Wise Answer Data LQP", response = DeptWiseAnswerDataLQPEntity.class, responseContainer = "List")
	public List<DeptWiseAnswerDataLQPEntity> getDeptWiseLQPAnswerData(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseLQPAnswerData(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * Council Module APIs
	 */

	@RequestMapping(value = "/getCouncilProposalCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCouncilProposalCount")
	@ApiOperation(value = "Get Council Proposal Count", notes = "Get Council Proposal Count", response = CouncilProposalCntEntity.class, responseContainer = "List")
	public List<CouncilProposalCntEntity> getCouncilProposalCount() {

		try {
			return citizenDashboardGraphServiceImpl.getCouncilProposalCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilProposalCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilProposalCount")
	@ApiOperation(value = "Get Department Wise Council Proposal Count", notes = "Get Department Wise Council Proposal Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilProposalCount(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilProposalCount(deptName, 0);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilProposalCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilProposalCount/{noOfDays}")
	@ApiOperation(value = "Get Department And Duration Wise Council Proposal Count", notes = "Get Department And Duration Wise Council Proposal Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilProposalCountByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilProposalCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearWiseCouncilProposalCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilProposalCount")
	@ApiOperation(value = "Get Year Wise Council Proposal Count", notes = "Get Year Wise Council Proposal Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilProposalCount(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseCouncilProposalCount(deptName, 0);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearWiseCouncilProposalCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilProposalCount/{noOfDays}")
	@ApiOperation(value = "Get Year Wise Council Proposal Count By Duration", notes = "Get Year Wise Council Proposal Count By Duration", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilProposalCountByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseCouncilProposalCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseProposalGridData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseProposalGridData")
	@ApiOperation(value = "Get Department Wise Proposal Grid Data", notes = "Get Department Wise Proposal Grid Data", response = CouncilDeptWiseProposalDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseProposalDataEntity> getDeptWiseProposalGridData(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseProposalGridData(deptName, 0, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseProposalGridData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseProposalGridData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Proposal Grid Data By Duration", notes = "Get Department Wise Proposal Grid Data By Duration", response = CouncilDeptWiseProposalDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseProposalDataEntity> getDeptWiseProposalGridDataByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseProposalGridData(deptName, noOfDays, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCouncilAgendaCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCouncilAgendaCount")
	@ApiOperation(value = "Get Council Agenda Count", notes = "Get Council Agenda Count", response = CouncilProposalCntEntity.class, responseContainer = "List")
	public List<CouncilProposalCntEntity> getCouncilAgendaCount() {

		try {
			return citizenDashboardGraphServiceImpl.getCouncilAgendaCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearWiseCouncilAgendaCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilAgendaCount")
	@ApiOperation(value = "Get Year wise Council Agenda Count", notes = "Get Year Wise Council Agenda Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilAgendaCount() {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseCouncilAgendaCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilAgendaCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilAgendaCount")
	@ApiOperation(value = "Get Department Wise Council Agenda Count", notes = "Get Department Wise Council Agenda Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilAgendaCount(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilAgendaCount(deptName, 0);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilAgendaCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilAgendaCount/{noOfDays}")
	@ApiOperation(value = "Get Department And Duration Wise Council Agenda Count", notes = "Get Department And Duration Wise Council Agenda Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilAgendaCountByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilAgendaCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseAgendaGridData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseAgendaGridData")
	@ApiOperation(value = "Get Department Wise Agenda Grid Data", notes = "Get Department Wise Agenda Grid Data", response = CouncilDeptWiseAgendaDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseAgendaDataEntity> getDeptWiseAgendaGridData(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseAgendaGridData(deptName, 0, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseAgendaGridData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseAgendaGridData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Agenda Grid Data By Duration", notes = "Get Department Wise Agenda Grid Data By Duration", response = CouncilDeptWiseAgendaDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseAgendaDataEntity> getDeptWiseAgendaGridDataByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseAgendaGridData(deptName, noOfDays, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCouncilMeetingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCouncilMeetingCount")
	@ApiOperation(value = "Get Council Meeting Count", notes = "Get Council Meeting Count", response = CouncilProposalCntEntity.class, responseContainer = "List")
	public List<CouncilProposalCntEntity> getCouncilMeetingCount() {

		try {
			return citizenDashboardGraphServiceImpl.getCouncilMeetingCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getYearWiseCouncilMeetingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilMeetingCount")
	@ApiOperation(value = "Get Year wise Council Meeting Count", notes = "Get Year wise Council Meeting Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getYearWiseCouncilMeetingCount() {

		try {
			return citizenDashboardGraphServiceImpl.getYearWiseCouncilMeetingCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilMeetingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilMeetingCount")
	@ApiOperation(value = "Get Department wise Council Meeting Count", notes = "Get Department wise Council Meeting Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilMeetingCount(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilMeetingCount(deptName, 0);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptAndDurationWiseCouncilMeetingCount/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptAndDurationWiseCouncilMeetingCount/{noOfDays}")
	@ApiOperation(value = "Get Department And Duration wise Council Meeting Count", notes = "Get Department And Duration wise Council Meeting Count", response = CouncilDeptYearWiseCntEntity.class, responseContainer = "List")
	public List<CouncilDeptYearWiseCntEntity> getDeptAndDurationWiseCouncilMeetingCountByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptAndDurationWiseCouncilMeetingCount(deptName, noOfDays);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseMeetingGridData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseMeetingGridData")
	@ApiOperation(value = "Get Department Wise Meeting Grid Data", notes = "Get Department Wise Meeting Grid Data", response = CouncilDeptWiseMeetingDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseMeetingDataEntity> getDeptWiseMeetingGridData(
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseMeetingGridData(deptName, 0, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDeptWiseMeetingGridData/{noOfDays}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseMeetingGridData/{noOfDays}")
	@ApiOperation(value = "Get Department Wise Meeting Grid Data By Duration", notes = "Get Department Wise Meeting Grid Data By Duration", response = CouncilDeptWiseMeetingDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseMeetingDataEntity> getDeptWiseMeetingGridDataByDays(
			@PathVariable("noOfDays") @PathParam("noOfDays") int noOfDays,
			@RequestParam(name = "deptName") @QueryParam(value = "deptName") String deptName) {

		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseMeetingGridData(deptName, noOfDays, 13);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * SKDCL common APIs
	 */

	@RequestMapping(value = "/getSKDCLDashboardAllCounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getSKDCLDashboardAllCounts")
	@ApiOperation(value = "Get SKDCL Dashboard All Counts", notes = "Get SKDCL Dashboard All Counts", response = SKDCLDashboardAllCountsEntity.class)
	public SKDCLDashboardAllCountsEntity getSKDCLDashboardAllCounts() {

		try {
			return citizenDashboardGraphServiceImpl.getSKDCLDashboardAllCounts();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * HRMS Module APIs
	 */

	@RequestMapping(value = "/getStatusWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getStatusWiseEmployeeCount")
	@ApiOperation(value = "Get Status Wise Employee Count", notes = "Get Status Wise Employee Count", response = HRMSCategoryWiseEmpCntEntity.class, responseContainer = "List")
	public List<HRMSCategoryWiseEmpCntEntity> getStatusWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getStatusWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getStatusWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getStatusWiseEmployeeData")
	@ApiOperation(value = "Get Status Wise Employee Data", notes = "Get Status Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getStatusWiseEmployeeData(
			@RequestParam(name = "status") @QueryParam(value = "status") String status) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getStatusWiseEmployeeData(status);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCategoryWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCategoryWiseEmployeeCount")
	@ApiOperation(value = "Get Category Wise Employee Count", notes = "Get Category Wise Employee Count", response = HRMSCategoryWiseEmpCntEntity.class, responseContainer = "List")
	public List<HRMSCategoryWiseEmpCntEntity> getCategoryWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getCategoryWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getCategoryWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getCategoryWiseEmployeeData")
	@ApiOperation(value = "Get Category Wise Employee Data", notes = "Get Category Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getCategoryWiseEmployeeData(
			@RequestParam(name = "category") @QueryParam(value = "category") String category) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getCategoryWiseEmployeeData(category);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentWiseEmployeeCount")
	@ApiOperation(value = "Get Department Wise Employee Count", notes = "Get Department Wise Employee Count", response = HRMSCategoryWiseEmpCntEntity.class, responseContainer = "List")
	public List<HRMSCategoryWiseEmpCntEntity> getDepartmentWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getDepartmentWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getDepartmentWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentWiseEmployeeData")
	@ApiOperation(value = "Get Department Wise Employee Data", notes = "Get Department Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getDepartmentWiseEmployeeData(
			@RequestParam(name = "dept") @QueryParam(value = "dept") String dept) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getDepartmentWiseEmployeeData(dept);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGenderWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGenderWiseEmployeeCount")
	@ApiOperation(value = "Get Gender Wise Employee Count", notes = "Get Gender Wise Employee Count", response = HRMSGenderRatioEntity.class, responseContainer = "List")
	public List<HRMSGenderRatioEntity> getGenderWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getGenderWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getGenderWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getGenderWiseEmployeeData")
	@ApiOperation(value = "Get Gender Wise Employee Data", notes = "Get Gender Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getGenderWiseEmployeeData(
			@RequestParam(name = "gender") @QueryParam(value = "gender") String gender) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getGenderWiseEmployeeData(gender);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getSalaryRangeWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getSalaryRangeWiseEmployeeCount")
	@ApiOperation(value = "Get Salary Range Wise Employee Count", notes = "Get Salary Range Wise Employee Count", response = HRMSSalaryBreakdownEntity.class, responseContainer = "List")
	public List<HRMSSalaryBreakdownEntity> getSalaryRangeWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getSalaryRangeWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getSalaryRangeDeptWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getSalaryRangeDeptWiseEmployeeData")
	@ApiOperation(value = "Get Salary Range And Department Wise Employee Data", notes = "Get Salary Range And Department Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getSalaryRangeDeptWiseEmployeeData(
			@RequestParam(name = "dept") @QueryParam(value = "dept") String dept,
			@RequestParam(name = "salaryRange") @QueryParam(value = "salaryRange") String salaryRange) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getSalaryRangeDeptWiseEmployeeData(dept, salaryRange);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getJoiningYearWiseEmployeeCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getJoiningYearWiseEmployeeCount")
	@ApiOperation(value = "Get Joining Year Wise Employee Count", notes = "Get Joining Year Wise Employee Count", response = HRMSCategoryWiseEmpCntEntity.class, responseContainer = "List")
	public List<HRMSCategoryWiseEmpCntEntity> getJoiningYearWiseEmployeeCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getJoiningYearWiseEmployeeCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getJoiningYearWiseEmployeeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getJoiningYearWiseEmployeeData")
	@ApiOperation(value = "Get Joining Year Wise Employee Data", notes = "Get Joining Year Wise Employee Data", response = HRMSEmployeeGridDataEntity.class, responseContainer = "List")
	public List<HRMSEmployeeGridDataEntity> getJoiningYearWiseEmployeeData(
			@RequestParam(name = "year") @QueryParam(value = "year") String year) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getJoiningYearWiseEmployeeData(year);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getVacantPostInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getVacantPostInfo")
	@ApiOperation(value = "Get Information About Vacant Posts", notes = "Get Information About Vacant Posts", response = HRMSRecruitmentPostsEntity.class, responseContainer = "List")
	public List<HRMSRecruitmentPostsEntity> getVacantPostInfo() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.HRMS_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getVacantPostInfo();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	/*
	 * Store Module APIs
	 */

	@RequestMapping(value = "/getItemGroupWiseInventoryValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getItemGroupWiseInventoryValue")
	@ApiOperation(value = "Get Item Group Wise Inventory Value", notes = "Get Item Group Wise Inventory Value", response = StoreItemCountEntity.class, responseContainer = "List")
	public List<StoreItemCountEntity> getItemGroupWiseInventoryValue() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getItemGroupWiseInventoryValue();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getItemWiseInventoryValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getItemWiseInventoryValue")
	@ApiOperation(value = "Get Item Wise Inventory Value", notes = "Get Item Wise Inventory Value", response = StoreItemWiseInventoryCntEntity.class, responseContainer = "List")
	public List<StoreItemWiseInventoryCntEntity> getItemWiseInventoryValue() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getItemWiseInventoryValue();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getItemGroupWiseItemCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getItemGroupWiseItemCount")
	@ApiOperation(value = "Get Item Group Wise Item Count", notes = "Get Item Group Wise Item Count", response = StoreItemCountEntity.class, responseContainer = "List")
	public List<StoreItemCountEntity> getItemGroupWiseItemCount() {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getItemGroupWiseItemCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getTopNMovingItemCounts/{limit}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTopNMovingItemCounts/{limit}")
	@ApiOperation(value = "Get Top n Moving Item Counts", notes = "Get Top n Moving Item Counts", response = StoreItemCountEntity.class, responseContainer = "List")
	public List<StoreItemCountEntity> getTopNMovingItemCounts(@PathVariable("limit") @PathParam("limit") int limit) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getTopNMovingItemCounts(limit);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getItemDataForGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getItemDataForGroup")
	@ApiOperation(value = "Get Item Data For Group", notes = "Get Item Data For Group", response = StoreItemDataEntity.class, responseContainer = "List")
	public List<StoreItemDataEntity> getItemDataForGroup(
			@RequestParam(name = "itemGroup") @QueryParam(value = "itemGroup") String itemGroup) {

		try {
			MainetTenantUtility.setThreadLocal(MainetConstants.STORE_DATA_SOURCE);
			return citizenDashboardGraphServiceImpl.getItemDataForGroup(itemGroup);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}

	// #120768
	@RequestMapping(value = "/getDeptWiseCouncilProposalCount/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeptWiseCouncilProposalCount/{orgId}")
	@ApiOperation(value = "Get Department And Duration Wise Council Proposal Count", notes = "Get Department Wise Council Proposal Count", response = CouncilDeptWiseProposalDataEntity.class, responseContainer = "List")
	public List<CouncilDeptWiseProposalCntEntity> getCouncilProposalCountByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return citizenDashboardGraphServiceImpl.getDeptWiseCouncilProposalCount(orgId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting CouncilProposalCount:", ex);
		}
		return null;
	}

	// #139298
	@RequestMapping(value = "/getYearWiseCouncilAgendaCount/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilAgendaCount/{orgId}")
	@ApiOperation(value = "Get Year  Wise Agenda Count", notes = "Get Year  Wise Agenda Count", response = CouncilMeetingCountYrwise.class, responseContainer = "List")
	public List<CouncilMeetingCountYrwise> getYearWiseAgendaCountByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return citizenDashboardGraphServiceImpl.getYearWiseAgendaCountByOrgId(orgId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getYearWiseCouncilAgendaCount:", ex);
		}
		return null;
	}

	@RequestMapping(value = "/getFinancialAndNonFiancialProposalCount/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getFinancialAndNonFiancialProposalCount/{orgId}")
	@ApiOperation(value = "Get Finacial and Non-financial proposal Count", notes = "Get Finacial and Non-financial proposal Count", response = FinancialAndNonFinacialProposalCount.class, responseContainer = "List")
	public List<FinancialAndNonFinacialProposalCount> getFinancialAndNonFiancialProposalCountByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return citizenDashboardGraphServiceImpl.getFinancialAndNonFiancialProposalCountByOrgId(orgId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getFinancialAndNonFiancialProposalCountByOrgId:", ex);
		}
		return null;
	}

	@RequestMapping(value = "/getYearWiseCouncilMeetingCount/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getYearWiseCouncilMeetingCount/{orgId}")
	@ApiOperation(value = "Get Year  Wise Council Meeting Count", notes = "Get Year  Wise Council Meeting Count", response = CouncilMeetingCountYrwise.class, responseContainer = "List")
	public List<CouncilMeetingCountYrwise> getYearWiseCouncilMeetingCountByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return citizenDashboardGraphServiceImpl.getYearWiseCouncilMeetingCountByOrgId(orgId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting CouncilProposalCount:", ex);
		}
		return null;
	}

	@RequestMapping(value = "/getRtiDrillDownDataByBplType/{bplType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRtiDrillDownDataByBplType/{bplType}")
	@ApiOperation(value = "Get RTI Drill down data by BPL Type", notes = "Get RTI Drill down data by BPL Type", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByBplType(@PathParam("bplType") String bplType) {
		try {
			return citizenDashboardGraphServiceImpl.getRtiDrillDownDataByBplType(bplType);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting RtiDrillDownDataByBplType:", ex);
		}
		return null;
	}

	@RequestMapping(value = "/getRtiDrillDownDataByApplicantType/{applicantType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRtiDrillDownDataByApplicantType/{applicantType}")
	@ApiOperation(value = "Get RTI Drill down data by Applicant Type", notes = "Get RTI Drill down data by Applicant Type", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RtiApplicationDtoByCondEntity> getRtiDrillDownDataByApplicantType(
			@PathParam("applicantType") String applicantType) {
		try {
			return citizenDashboardGraphServiceImpl.getRtiDrillDownDataByApplicantType(applicantType);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting RtiDrillDownDataByApplicantType:", ex);
		}
		return null;
	}


	

	@RequestMapping(value = "/getRFAOpenClosedDrillDownData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRFAOpenClosedDrillDownData")
	@ApiOperation(value = "Get RTI Drill down data closed RTI first Appeal", notes = "Get RTI Drill down data closed RTI first Appeal", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RtiApplicationDtoByCondEntity> getRFAOpenClosedDrillDownData() {
		try {
			return citizenDashboardGraphServiceImpl.getRFAOpenClosedDrillDownData();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting get RFAClosedDrillDownData:", ex);
		}
		return null;

	}

	@RequestMapping(value = "/getRSAPendingClosedDrillDownData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getRSAPendingClosedDrillDownData")
	@ApiOperation(value = "Get RTI Drill down closed RTI second Appeal", notes = "Get RTI Drill down closed RTI second Appeal", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RtiApplicationDtoByCondEntity> getRSAPendingClosedDrillDownData() {
		try {
			return citizenDashboardGraphServiceImpl.getRSAPendingClosedDrillDownData();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting get RSAClosedDrillDownData:", ex);
		}
		return null;

	}
	
	@RequestMapping(value = "/getIndvidualRtiRfaOpenClosedData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getIndvidualRtiRfaOpenClosedData")
	@ApiOperation(value = "Get RTI open Individual data for First and Second appeal", notes = "Get RTI open Individual data for First and Second appeal", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRFAOpenClosedData() {
		try {
			return citizenDashboardGraphServiceImpl.getIndvidualRtiRfaOpenClosedData();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting IndvidualRtiRfaOpenClosedData:", ex);
		}
		return null;

	}
	@RequestMapping(value = "/getIndvidualRtiRSAOpenClosedData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getIndvidualRtiRSAOpenClosedData")
	@ApiOperation(value = "Get RTI closed Individual data for First and Second appeal", notes = "Get RTI closed Individual data for First and Second appeal", response = RtiApplicationDtoByCondEntity.class, responseContainer = "List")
	public List<RTIDasBoardAppealStatusEntity> getIndvidualRtiRSAOpenClosedData() {
		try {
			return citizenDashboardGraphServiceImpl.getIndvidualRtiRSAOpenClosedData();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting IndvidualRtiRSAOpenClosedData:", ex);
		}
		return null;

	}
	
	@RequestMapping(value = "/getAdvocateMstRegCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getAdvocateMstRegCount")
	@ApiOperation(value = "Get Council Proposal Count", notes = "Get Council Proposal Count", response = CouncilProposalCntEntity.class, responseContainer = "List")
	public List<AdvocateMastRegEntity> getAdvocateMstRegCount() {

		try {
			return citizenDashboardGraphServiceImpl.getAdvocateMstRegCount();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for advocate registration  data:", ex);
		}

		return null;

	}
	
	/* For fetching Complaint drilldown details with Parameter 
	D#152479
	 * */
	
		@RequestMapping(value = "/getATotComplaintAndStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatus")
	@ApiOperation(value = "Get Total  COmplaint and STatus", notes = "Get Total  COmplaint and STatus", response = ComplaintEntity.class, responseContainer = "List")
	public List<ComplaintEntity> getTotComplaintAndStatus() {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatus();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusn  data:", ex);
		}

		return null;

	}
	
	

	@RequestMapping(value = "/getATotComplaintAndStatusByDept/{deptId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatusByDept/{deptId}")
	@ApiOperation(value = "Get Total  COmplaint and STatus Department wise", notes = "Get Total  COmplaint and STatus Department wise", response = CommonComplaintEntity.class, responseContainer = "List")
	public List<CommonComplaintEntity> getTotComplaintAndStatusByDept(@PathParam("deptId") String deptId) {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatusByDept(deptId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusByDept  data:", ex);
		}

		return null;

	}
	

	@RequestMapping(value = "/getATotComplaintAndStatusByZone/{zone}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatusByZone/{zone}")
	@ApiOperation(value = "Get Total  COmplaint and STatus By Zone", notes = "Get Total  COmplaint and STatus By Zone", response = CommonComplaintEntity.class, responseContainer = "List")
	public List<CommonComplaintEntity> getTotComplaintAndStatusByZone(@PathParam("zone") String zone) {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatusByZone(zone);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusByZone  data:", ex);
		}

		return null;

	}
	

	@RequestMapping(value = "/getATotComplaintAndStatusByWard/{ward}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatusByWard/{ward}")
	@ApiOperation(value = "Get Total  COmplaint and STatus", notes = "Get Total  COmplaint and STatus", response = CommonComplaintEntity.class, responseContainer = "List")
	public List<CommonComplaintEntity> getTotComplaintAndStatusByWard(@PathParam("ward") String ward) {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatusByWard(ward);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusByWard  data:", ex);
		}

		return null;

	}
	

	@RequestMapping(value = "/getATotComplaintAndStatusByCategory/{category}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatusByCategory/{category}")
	@ApiOperation(value = "Get Total  COmplaint and STatus By Category", notes = "Get Total  COmplaint and STatus By Category", response = CommonComplaintEntity.class, responseContainer = "List")
	public List<CommonComplaintEntity> getTotComplaintAndStatusByCategory(@PathParam("category") String category) {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatusByCategory(category);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusByCategory  data:", ex);
		}

		return null;

	}
	

	@RequestMapping(value = "/getATotComplaintAndStatusBySubCategory/{subCategory}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getATotComplaintAndStatusBySubCategory/{subCategory}")
	@ApiOperation(value = "Get Total  COmplaint and STatus By SubCategory", notes = "Get Total  COmplaint and STatus By SubCategory", response = CommonComplaintEntity.class, responseContainer = "List")
	public List<CommonComplaintEntity> getTotComplaintAndStatusBySubCategory(@PathParam("subCategory") String subCategory) {

		try {
			return citizenDashboardGraphServiceImpl.getTotComplaintAndStatusBySubCategory(subCategory);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getTotComplaintAndStatusBySubCategory  data:", ex);
		}

		return null;

	}
	
	
	@RequestMapping(value = "/getDepartmentComplaintsForToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDepartmentComplaintsForToday")
	@ApiOperation(value = "Get Department Complaints For Today", notes = "Get Department Complaints For Today", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
	public List<DeptByDaysAndDescDTO> getDepartmentComplaintsForToday() {

		try {
			return citizenDashboardGraphServiceImpl.getDeptComplaintsForTodayEntityList();
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting citizen app data:", ex);
		}

		return null;

	}
	
	

	 	
	 	@RequestMapping(value = "/getDepartmentComplaintsFinancialYearWise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getDepartmentComplaintsFinancialYearWise")
		@ApiOperation(value = "Get Department Complaints Financial Year Wise", notes = "Get Department Complaints Financial Year Wise", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
		public List<ComplaintFinancialYearEntity> getDepartmentComplaintsFinancialYearWise() {

			try {
				return citizenDashboardGraphServiceImpl.getComplaintFinancialYearEntity();
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
	 	
	 	
	 	@RequestMapping(value = "/getDepartmentComplaintsModeWise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getDepartmentComplaintsModeWise")
		@ApiOperation(value = "Get Department Complaints Financial Year Wise", notes = "Get Department Complaints Financial Year Wise", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
		public List<ComplaintsModeWiseEntity> getDepartmentComplaintsModeWise() {

			try {
				return citizenDashboardGraphServiceImpl.getDepartmentComplaintsModeWiseEntity();
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
	 	
	 	
	 	@RequestMapping(value = "/getDepartmentComplaintsDeparmentWise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getDepartmentComplaintsDeparmentWise")
		@ApiOperation(value = "Get Department Complaints Mode Wise", notes = "Get Department Complaints Mode Wise", response = DeptByDaysAndDescDTO.class, responseContainer = "List")
		public List<ComplaintsDepartmentWiseEntity> getDepartmentComplaintsDeparmentWise() {

			try {
				return citizenDashboardGraphServiceImpl.getDepartmentComplaintsDeparmentWise();
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
	 	@RequestMapping(value = "/getLicenseDetailBylicNofDatetDate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getLicenseDetailBylicNofDatetDate/{ward2}/{fDate}/{tDate}")
		@ApiOperation(value = "Get License Details by License No ,From Date and To Date", notes = "Get License Details by License No ,From Date and To Date", response = LicenseDetailsDto.class, responseContainer = "List")
		public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailBylicNofDatetDate(@PathParam("ward2") Long ward2,@PathParam("fDate") String fDate,@PathParam("tDate") String tDate) {

			try {
				return citizenDashboardGraphServiceImpl.getLicenseDetailBylicNofDatetDate(ward2,fDate,tDate);
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
	 	@RequestMapping(value = "/getLicenseDetailByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
			@ResponseStatus(HttpStatus.OK)
			@ResponseBody
			@GET
			@Path("/getLicenseDetailByYear/{year}")
			@ApiOperation(value = "Get License Details by License No ,From Date and To Date", notes = "Get License Details by License No ,From Date and To Date", response = LicenseDetailsDto.class, responseContainer = "List")
			public List<LicensedtlByLicNoAndDatesEntity> getLicenseDetailByYear(@PathParam("year") int year) {

				try {
					  LocalDate startDate = LocalDate.of(year, 1, 1);
				        LocalDate endDate = LocalDate.of(year,12, 31);
					return citizenDashboardGraphServiceImpl.getLicenseDetailByYear(startDate.toString(),endDate.toString());
				} catch (final Exception ex) {
					LOGGER.error("problem occurred while request for getting citizen app data:", ex);
				}

				return null;

			}
	 	@RequestMapping(value = "/getLicenseByNoAndAppId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getLicenseByNoAndAppId/{ward2}/{formDate}/{toDate}")
		@ApiOperation(value = "Get License no and application Id by License No ,From Date and To Date", notes = "Get License Details by License No ,From Date and To Date", response = LicensedtlByNoAndAppId.class, responseContainer = "List")
		public List<LicensedtlByNoAndAppIdEntity> getLicenseByNoAndAppId(@PathParam("ward2") Long ward2,@PathParam("formDate") String fDate,@PathParam("toDate") String tDate) {

			try {
				return citizenDashboardGraphServiceImpl.getLicenseByNoAndAppId(ward2,fDate,tDate);
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
		@RequestMapping(value = "/getLicenseRecieptMasDeatilsByServceIdAndDeptId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getLicenseRecieptMasDeatilsByServceIdAndDeptId")
		@ApiOperation(value = "Get License Reciept Mas Details by deptartment , Service ,ward,", notes = "Get License Reciept Mas Details by deptartment , Service ,ward,", response = LicenseDeatilsByServceIdAndDeptId.class, responseContainer = "List")
		public List<LicenseDeatilsByServceIdAndDeptIdEntity> getLicenseDeatilsByServceIdAndDeptId(
				@RequestParam(name = "orgId") @QueryParam(value = "orgId") Long   orgId,
				@RequestParam(name = "deptId") @QueryParam(value = "deptId") Long deptId,
				@RequestParam(name = "serviceId") @QueryParam(value = "serviceId") Long serviceId,
				@RequestParam(name = "trdWard2") @QueryParam(value = "trdWard2") Long trdWard2,
				@RequestParam(name = "chequeStatus") @QueryParam(value = "chequeStatus") String chequeStatus,
				@RequestParam(name = "formDate") @QueryParam(value = "formDate") String formDate,
				@RequestParam(name = "toDate") @QueryParam(value = "toDate") String toDate) {

			try {
				return citizenDashboardGraphServiceImpl.getLicenseDeatilsByServceIdAndDeptId(orgId, deptId, serviceId,trdWard2,chequeStatus,formDate,toDate
						);
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
		@RequestMapping(value = "/getLicenseRecieptDeatilsByServceIdAndDeptId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		@GET
		@Path("/getLicenseRecieptDeatilsByServceIdAndDeptId")
		@ApiOperation(value = "Get License Reciept  Details by deptartment , Service ,ward,", notes = "Get License Reciept  Details by deptartment , Service ,ward,", response = LicenseRecieptDeatilsByServceIdAndDeptId.class, responseContainer = "List")
		public List<LicenseRecieptDeatilsByServceIdAndDeptIdEntity> getLicenseRecieptDeatilsByServceIdAndDeptId(
				@RequestParam(name = "orgId") @QueryParam(value = "orgId") Long   orgId,
				@RequestParam(name = "deptId") @QueryParam(value = "deptId") Long deptId,
				@RequestParam(name = "serviceId") @QueryParam(value = "serviceId") Long serviceId,
				@RequestParam(name = "trdWard2") @QueryParam(value = "trdWard2") Long trdWard2,
				@RequestParam(name = "chequeStatus") @QueryParam(value = "chequeStatus") String chequeStatus,
				@RequestParam(name = "formDate") @QueryParam(value = "formDate") String formDate,
				@RequestParam(name = "toDate") @QueryParam(value = "toDate") String toDate) {

			try {
				return citizenDashboardGraphServiceImpl.getLicenseRecieptDeatilsByServceIdAndDeptIdEntity(orgId, deptId, serviceId,trdWard2,chequeStatus,formDate,toDate
						);
			} catch (final Exception ex) {
				LOGGER.error("problem occurred while request for getting citizen app data:", ex);
			}

			return null;

		}
		
		
	@RequestMapping(value = "/getWaterDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get water data ward zone wise", notes = "Get water data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<WaterDataWardZoneWiseEntity> getWaterDataWardZoneWise(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getWaterDataWardZoneWise(wardId, zoneId, fromDate, toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getWaterReceiptDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterReceiptDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get water receipt data ward zone wise", notes = "Get water receipt data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<WaterReceiptDataEntity> getWaterReceiptData(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getWaterReceiptDataWardZoneWise(wardId, zoneId, fromDate, toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getWaterReceiptDetailDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterReceiptDetailDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get water receipt detail data ward zone wise", notes = "Get water receipt detail data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<WaterReceiptDetailDataEntity> getWaterReceiptDetailData(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getWaterReceiptDetailDataWardZoneWise(wardId, zoneId, fromDate,
					toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getWholePropertyDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWholePropertyDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get whole property data ward zone wise", notes = "Get whole property data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<WholePropertyDataEntity> getWholePropertyDataWardZoneWise(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getWholePropertyDataWardZoneWise(wardId, zoneId, fromDate, toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getIndividualPropertyDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getIndividualPropertyDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get individual property data ward zone wise", notes = "Get individual property data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<IndividualPropertyDataEntity> getIndividualPropertyDataWardZoneWise(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getIndividualPropertyDataWardZoneWise(wardId, zoneId, fromDate,
					toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getPropertyReceiptDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getPropertyReceiptDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get property receipt data ward zone wise", notes = "Get property receipt data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<PropertyReceiptDataWiseEntity> getPropertyReceiptDataWardZoneWise(@PathParam("wardId") String wardId,
			@PathParam("zoneId") String zoneId, @PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getPropertyReceiptDataWardZoneWise(wardId, zoneId, fromDate,
					toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}

	@RequestMapping(value = "/getPropertyReceiptDetailDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getPropertyReceiptDetailDataWardZoneWise/{wardId}/{zoneId}/{fromDate}/{toDate}")
	@ApiOperation(value = "Get property receipt detail data ward zone wise", notes = "Get property receipt detail data ward zone wise", response = WaterDataWardZoneWiseEntity.class, responseContainer = "List")
	public List<PropertyReceiptDetailDataEntity> getPropertyReceiptDetailDataWardZoneWise(
			@PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId,
			@PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate) {

		try {
			return citizenDashboardGraphServiceImpl.getPropertyReceiptDetailDataWardZoneWise(wardId, zoneId, fromDate,
					toDate);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}

		return null;

	}
	
	@RequestMapping(value = "/updateWorkflowTaskForAppId/{appId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@POST
	@Path("/updateWorkflowTaskForAppId/{appId}")
	@ApiOperation(value = "Get appId and resolve task inconsistency", notes = "Get appId and resolve task inconsistency")
	public void updateWorkflowTaskForAppId(@PathParam("appId") Long appId) {

		try {
			citizenDashboardGraphServiceImpl.updateWorkflowAutoEscalationTaskWrtAppId(appId);
		} catch (final Exception ex) {
			LOGGER.error("problem occurred while request for getting getSumOfCurBalTaxAmtAndPrvBalArrAmt data:", ex);
		}


	}
	
	
	@RequestMapping(value = "/getPropertyStatisticsServiceWise/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getPropertyStatisticsServiceWise/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get property statistics service-wise",
	    notes = "Returns statistics grouped by department and service for the given date range",
	    response = PropertyStatisticsServiceDepartmentEntity.class,
	    responseContainer = "List"
	)
	public List<PropertyStatisticsServiceDepartmentEntity> getPropertyStatisticsServiceWise(
	        @PathParam("startDate") String startDate,
	        @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getPropertyStatisticsServiceWise(startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getModeWiseCollectionDataReport/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getModeWiseCollectionDataReport/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Mode Wise Data",
	    notes = "Returns Mode Wise Data",
	    response = ModeWiseCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<ModeWiseCollectionReportEntity> getModeWiseCollectionDataReport(
	        @PathParam("startDate") String startDate,
	        @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getModeWiseData(startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	


	@RequestMapping(value = "/getSummaryCollectionDataReportList/{startDate}/{endDate}/{ward}/{zone}/{tarrifType1}/{tarrifType2}/{tarrifType3}/{meterType}/{conSize}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getSummaryCollectionDataReportList/{startDate}/{endDate}/{ward}/{zone}/{tarrifType1}/{tarrifType2}/{tarrifType3}/{meterType}/{conSize}")
	@ApiOperation(
	    value = "Get Mode Wise Data",
	    notes = "Returns Mode Wise Data",
	    response = SummaryCollectionDataReportEntity.class,
	    responseContainer = "List"
	)
	public List<SummaryCollectionDataReportEntity> getSummaryCollectionDataReportList(
	        @PathParam("startDate") String startDate,@PathParam("endDate") String endDate,@PathParam("ward") String ward,@PathParam("zone") String zone,
	        @PathParam("tarrifType1") String tarrifType1, @PathParam("tarrifType2") String tarrifType2,@PathParam("tarrifType3") String tarrifType3,
	        @PathParam("meterType") String meterType,@PathParam("conSize") String conSize) {

	    try {
	        return citizenDashboardGraphServiceImpl.getSummaryWiseData(startDate, endDate, ward, zone, tarrifType1, tarrifType2, tarrifType3, meterType, conSize);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getWardWiseCollectionDataReportList/{startDate}/{endDate}/{wardId}/{zoneId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWardWiseCollectionDataReportList/{startDate}/{endDate}/{wardId}/{zoneId}")
	@ApiOperation(
	    value = "Get Ward Wise Data",
	    notes = "Returns Ward Wise Property Data Collection Data List",
	    response = WardwisePropertyCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<WardwisePropertyCollectionReportEntity> getWardWiseList(
	        @PathParam("startDate") String startDate,@PathParam("endDate") String endDate,@PathParam("wardId") String wardId,@PathParam("zoneId") String zoneId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWardWisePropertyData(startDate, endDate, wardId, zoneId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getSummaryWiseGreavienceReportList/{fromDate}/{toDate}/{deptId}/{subTypId}/{codIdLevel1}/{codIdLevel2}/{codIdLevel3}/{codIdLevel4}/{codIdLevel5}/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)

	@ResponseStatus(HttpStatus.OK)

	@ResponseBody

	@GET

	@Path("/getSummaryWiseGreavienceReportList/{fromDate}/{toDate}/{deptId}/{subTypId}/{codIdLevel1}/{codIdLevel2}/{codIdLevel3}/{codIdLevel4}/{codIdLevel5}/{status}")

	@ApiOperation(value = "Get Summary Wise Greavience Data", notes = "Returns Ward Wise Greavience Data Collection Data List", response = WardwisePropertyCollectionReportEntity.class, responseContainer = "List")
	public List<SlaWiseGrievanceSumaaryEntity> getSummaryGreavienceList(

			@PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate,
			@PathParam("deptId") String deptId, @PathParam("subTypId") String subTypId,

			@PathParam("codIdLevel1") String codIdLevel1, @PathParam("codIdLevel2") String codIdLevel2, @PathParam("codIdLevel3") String codIdLevel3,
			@PathParam("codIdLevel4") String codIdLevel4, @PathParam("codIdLevel5") String codIdLevel5, @PathParam("status") String status) {

		try {
			return citizenDashboardGraphServiceImpl.getGreaveanceSummaryReportData(fromDate, toDate, deptId, subTypId, codIdLevel1, codIdLevel2, codIdLevel3,
					codIdLevel4, codIdLevel5, status);
		} catch (final Exception ex) {
			LOGGER.error("Error occurred while fetching Greaviance Summary data :", ex);
		}

		return null;
	}
	
	@RequestMapping(value = "/getBirthCertificateForMobile/{appId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getBirthCertificateForMobile/{appId}")
	@ApiOperation(
	    value = "Get Birth Data",
	    notes = "Returns Birth Certificate Data",
	    response = WardwisePropertyCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<BirthCertificateEntityForMobile> getBirthCertificate(
	        @PathParam("startDate") String startDate,@PathParam("appId") String appId,@PathParam("serviceName") String serviceName) {

	    try {
			
				return citizenDashboardGraphServiceImpl.getBirthCertificateDatas(appId, "1");
			
			
			
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getDeathCertificateForMobile/{appId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDeathCertificateForMobile/{appId}")
	@ApiOperation(
	    value = "Get Death Data",
	    notes = "Returns Death Certificate Data",
	    response = WardwisePropertyCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<DeathCertificateEntityForMobile> getDeathCertificate(
	        @PathParam("startDate") String startDate,@PathParam("appId") String appId,@PathParam("serviceName") String serviceName) {

		try {

			return citizenDashboardGraphServiceImpl.getDeathCertificateDatas(appId, "1");

		} catch (final Exception ex) {
			LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
		}

		return null;
	}
	
	
	@RequestMapping(value = "/getDailyPropertyCollectionData", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getDailyPropertyCollectionData")
	@ApiOperation(
	    value = "Get Property Daily Collection Data",
	    notes = "Returns Daily Property Collcetion Data",
	    response = DailyPropertyCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<DailyPropertyCollectionReportEntity> getDailyPropertyCollectionData() {

	    try {
	        return citizenDashboardGraphServiceImpl.getDailyPropertyData();
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getZoneWisePropertyCollectionDataReportList/{startDate}/{endDate}/{wardId}/{zoneId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getZoneWisePropertyCollectionDataReportList/{startDate}/{endDate}/{wardId}/{zoneId}")
	@ApiOperation(
	    value = "Get Zone Wise Property Data",
	    notes = "Returns Zone Wise Property  Data",
	    response = ZoneWisePropertyCollectionReportEntity.class,
	    responseContainer = "List"
	)
	public List<ZoneWisePropertyCollectionReportEntity> getZoneWisePropertyList(@PathParam("startDate") String startDate,@PathParam("endDate") String endDate,@PathParam("wardId") String wardId,@PathParam("zoneId") String zoneId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getZoneWiseProperty(startDate, endDate, wardId, zoneId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching property statistics service-wise:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getTopTenToHunAllWardWiseData/{number}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTopTenToHunAllWardWiseData/{number}")
	@ApiOperation(
	    value = "Get All 10 to 100 All Data",
	    notes = "Returns Top 10 To 100 All Ward Wise Data",
	    response = TopTenToHunAllWardEntity.class,
	    responseContainer = "List"
	)
	public List<TopTenToHunAllWardEntity> getTopTenToHunAllData(@PathParam("number") String number) {

	    try {
	        return citizenDashboardGraphServiceImpl.getTopTenToHundredAllWardWiseData(number);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getTopTenToHunAllWardWiseData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getTopTenToHunWardWiseData/{number}/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTopTenToHunWardWiseData/{number}/{wardId}")
	@ApiOperation(
	    value = "Get All 10 to 100  Data ward wise Data",
	    notes = "Returns Top 10 To 100  Ward Wise Data",
	    response = TopTenToHunAllWardEntity.class,
	    responseContainer = "List"
	)
	public List<TopTenToHunAllWardEntity> getTopTenToHunWardData(@PathParam("number") String number, @PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getTopTenToHundredWardWiseData(wardId, number);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getTopTenToHunWardWiseData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getTopTenToHunWardZoneWiseData/{number}/{wardId}/{zoneId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getTopTenToHunWardZoneWiseData/{number}/{wardId}/{zoneId}")
	@ApiOperation(
	    value = "Get All 10 to 100  Data ward-zone wise data",
	    notes = "Returns Top 10 To 100  Ward zone Wise Data",
	    response = TopTenToHunAllWardEntity.class,
	    responseContainer = "List"
	)
	public List<TopTenToHunAllWardEntity> getTopTenToHunWardZoneData(@PathParam("number") String number, @PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getTopTenToHundredWardZoneData(number, wardId, zoneId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getTopTenToHunWardZoneWiseData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getLegalIllegalPropertiesWardWise", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getLegalIllegalPropertiesWardWise")
	@ApiOperation(
	    value = "Get Leg-Illegal Property Data Ward Wise",
	    notes = "Returns Legal Illegal Properties Ward Wise",
	    response = LegalIllegalPropWardWiseEntity.class,
	    responseContainer = "List"
	)
	public List<LegalIllegalPropWardWiseEntity> getLegalIllegalPropertiesWardWise() {

	    try {
	        return citizenDashboardGraphServiceImpl.getLegalIlleProWardWiseData();
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getLegalIllegalPropertiesWardWise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getLegalIllegalPropertiesZoneWise/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getLegalIllegalPropertiesZoneWise/{wardId}")
	@ApiOperation(
	    value = "Get Leg-Illegal Property Data Zone Wise",
	    notes = "Returns Legal Illegal Properties Zone Wise",
	    response = LegalIllegalPropZoneWiseEntity.class,
	    responseContainer = "List"
	)
	public List<LegalIllegalPropZoneWiseEntity> getLegalIllegalPropertiesZoneWise(@PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getLegalIlleProZoneWiseData(wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getLegalIllegalPropertiesZoneWise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getUsageWardWisePropertyCount", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getUsageWardWisePropertyCount")
	@ApiOperation(
	    value = "Get Usage Property Data Ward Wise",
	    notes = "Returns Usage Properties Ward Wise",
	    response = UsageWardWisePropertyCountEntity.class,
	    responseContainer = "List"
	)
	public List<UsageWardWisePropertyCountEntity> getUsageWardWisePropertyCount() {

	    try {
	        return citizenDashboardGraphServiceImpl.getUsageWardWisePropCount();
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getLegalIllegalPropertiesWardWise:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getUsageZoneWisePropertyCount/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getUsageZoneWisePropertyCount/{wardId}")
	@ApiOperation(
	    value = "Get Usage Property Data Zone Wise",
	    notes = "Returns Usage Properties Zone Wise",
	    response = UsageZoneWisePropertyCountEntity.class,
	    responseContainer = "List"
	)
	public List<UsageZoneWisePropertyCountEntity> getUsageZoneWisePropertyCount(@PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getUsageZoneWisePropCount(wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getUsageZoneWisePropertyCount:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWardAndModeWisePropertyCollection/{wardId}/{zoneId}/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWardAndModeWisePropertyCollection/{wardId}/{zoneId}/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Property Ward And Mode Wise Collection",
	    notes = "Returns Properties Ward And Zone Wise",
	    response = WardAndModeWisePropertyCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WardAndModeWisePropertyCollectionEntity> getWardAndModeWisePropertyCollection(@PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId,
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getwardAndModeWisePropData(wardId, zoneId, startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWardAndModeWisePropertyCollection:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getZoneAndModeWisePropertyCollection/{wardId}/{zoneId}/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getZoneAndModeWisePropertyCollection/{wardId}/{zoneId}/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Property Zone And Mode Wise Collection",
	    notes = "Returns Properties Zone And Zone Wise",
	    response = ZoneAndModeWisePropertyCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<ZoneAndModeWisePropertyCollectionEntity> getZoneAndModeWisePropertyCollection(@PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId,
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getzoneAndModeWisePropData(wardId, zoneId, startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getZoneAndModeWisePropertyCollection:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getWardWiseAbhayYojnaCollection/{wardId}/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWardWiseAbhayYojnaCollection/{wardId}/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Ward Wise Abhay Yojna Collection Collection",
	    notes = "Returns Ward Wise Abhay Yojna Collection",
	    response = AbhayYojnaCollectionWardWiseEntity.class,
	    responseContainer = "List"
	)
	public List<AbhayYojnaCollectionWardWiseEntity> getWardWiseAbhayYojnaCollection(@PathParam("wardId") String wardId,
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWardWiseAbhayYojnaData(wardId, startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWardWiseAbhayYojnaCollection:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getZoneWiseAbhayYojnaCollection/{wardId}/{zoneId}/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getZoneWiseAbhayYojnaCollection/{wardId}/{zoneId}/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Zone Wise Abhay Yojna Collection Collection",
	    notes = "Returns Zone Wise Abhay Yojna Collection",
	    response = AbhayYojnaCollectionZoneWiseEntity.class,
	    responseContainer = "List"
	)
	public List<AbhayYojnaCollectionZoneWiseEntity> getZoneWiseAbhayYojnaCollection(@PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId,
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getZoneWiseAbhayYojnaData(wardId, zoneId, startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getZoneWiseAbhayYojnaCollection:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardWiseAbhayYojnaCollection/{startDate}/{endDate}/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardWiseAbhayYojnaCollection/{startDate}/{endDate}/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax Dept Ward Wise Abhay Yojna Collection Collection",
	    notes = "Returns Water Tax Dept Ward Wise Abhay Yojna Collection",
	    response = WaterAbhayYojnaWardZoneWiseEntity.class,
	    responseContainer = "List"
	)
	public List<WaterAbhayYojnaWardZoneWiseEntity> getWaterWardWiseAbhayYojnaCollection(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardWiseAbhayYojnaData(startDate, endDate, wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardWiseAbhayYojnaCollection:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterAllWardWiseAbhayYojnaCollection/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterAllWardWiseAbhayYojnaCollection/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Water Tax Dept All Ward Wise Abhay Yojna Collection Collection",
	    notes = "Returns Water Tax Dept All Ward Wise Abhay Yojna Collection",
	    response = WaterAbhayYojnaAllWardWiseEntity.class,
	    responseContainer = "List"
	)
	public List<WaterAbhayYojnaAllWardWiseEntity> getWaterAllWardWiseAbhayYojnaCollection(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterAllWardWiseAbhayYojnaData(startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterAllWardWiseAbhayYojnaCollection:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardWiseUsageCountData", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardWiseUsageCountData")
	@ApiOperation(
	    value = "Get Water Tax Dept  Ward Wise Usage Count",
	    notes = "Returns Water Tax Dept  Ward Wise Usage Count",
	    response = WaterWardWiseUsageCountEntity.class,
	    responseContainer = "List"
	)
	public List<WaterWardWiseUsageCountEntity> getWaterWardWiseUsageCountData() {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardWiseUsageCountData();
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardWiseUsageCountData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardAndZoneWiseUsageCountData/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardAndZoneWiseUsageCountData/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax Dept  Ward And Zone Wise Usage Count",
	    notes = "Returns Water Tax Dept  Ward And Zone Wise Usage Count",
	    response = WaterWardAndZoneWiseUsageCountEntity.class,
	    responseContainer = "List"
	)
	public List<WaterWardAndZoneWiseUsageCountEntity> getWaterWardAndZoneWiseUsageCountData(@PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardZoneWiseUsageCountData(wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardAndZoneWiseUsageCountData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardWiseAuthoAndUnauthoCountData", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardWiseAuthoAndUnauthoCountData")
	@ApiOperation(
	    value = "Get Water Tax Dept  Ward Wise Authorisation And Unauthorisation Count",
	    notes = "Returns Water Tax Dept  Ward Wise Authorisation And Unauthorisation Count",
	    response = WaterTaxWardWiseAuthoUnathoCountEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxWardWiseAuthoUnathoCountEntity> getWaterWardWiseAuthoAndUnauthoCountData() {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardWiseAuthoAndUnauthoCountData();
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardWiseAuthoAndUnauthoCountData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardAndZoneWiseAuthoAndUnauthoCountData/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardAndZoneWiseAuthoAndUnauthoCountData/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax Dept  Ward Zone Wise Authorisation And Unauthorisation Count",
	    notes = "Returns Water Tax Dept  Ward Zone Wise Authorisation And Unauthorisation Count",
	    response = WaterTaxWardZoneWiseAuthoUnathoCountEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxWardZoneWiseAuthoUnathoCountEntity> getWaterWardAndZoneWiseAuthoAndUnauthoCountData(@PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardZoneWiseAuthoAndUnauthoCountData(wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardAndZoneWiseAuthoAndUnauthoCountData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterWardWiseDemandCollectiontData/{startDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterWardWiseDemandCollectiontData/{startDate}")
	@ApiOperation(
	    value = "Get Water Tax Dept  Ward Wise Demand Collection",
	    notes = "Returns Water Tax Dept  Ward Wise Demand Collection Data",
	    response = WaterTaxWardWiseDemandCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxWardWiseDemandCollectionEntity> getWaterWardWiseDemandCollectiontData(@PathParam("startDate") String startDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardWiseDemandCollectionData(startDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterWardWiseDemandCollectiontData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterZoneWiseDemandCollectiontData/{startDate}/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterZoneWiseDemandCollectiontData/{startDate}/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax Dept  Zone Wise Demand Collection",
	    notes = "Returns Water Tax Dept  Zone Wise Demand Collection Data",
	    response = WaterTaxZoneWiseDemandCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxZoneWiseDemandCollectionEntity> getWaterZoneWiseDemandCollectiontData(@PathParam("startDate") String startDate, @PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWiseZoneDemandCollectionData(startDate, wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterZoneWiseDemandCollectiontData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterTaxModeWiseWardData/{startDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTaxModeWiseWardData/{startDate}")
	@ApiOperation(
	    value = "Get Water Tax Mode Wise Ward Data",
	    notes = "Returns Water Tax Dept  Mode Wise Ward Data",
	    response = WaterTaxModeWardWiseData.class,
	    responseContainer = "List"
	)
	public List<WaterTaxModeWardWiseData> getWaterTaxModeWiseWardData(@PathParam("startDate") String startDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterModeWiseWardsDataList(startDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTaxModeWiseWardData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterTaxModeWiseWardAndZoneData/{startDate}/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTaxModeWiseWardAndZoneData/{startDate}/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax Mode Wise Ward And Zone Data",
	    notes = "Returns Water Tax Mode Wise Ward And Zone Data",
	    response = WaterTaxModeWardAndZoneWiseDataEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxModeWardAndZoneWiseDataEntity> getWaterTaxModeWiseWardAndZoneData(@PathParam("startDate") String startDate, @PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterModeWiseWardAndZoneDataList(startDate, wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTaxModeWiseWardAndZoneData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterTopTenToHunAllWardWiseData/{startDate}/{num}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTopTenToHunAllWardWiseData/{startDate}/{num}")
	@ApiOperation(
	    value = "Get Water Tax Top 10 to 100 All Ward-Wise Data",
	    notes = "Returns Water Tax Top 10 to 100 All Ward-Wise Data",
	    response = WaterAllWardWiseTopTenToHundredDefaulterEntity.class,
	    responseContainer = "List"
	)
	public List<WaterAllWardWiseTopTenToHundredDefaulterEntity> getWaterTopTenToHunAllWardWiseData(@PathParam("startDate") String startDate, @PathParam("num") String num) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterTopTenToHunAllWardWiseDataList(startDate, num);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTopTenToHunAllWardWiseData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterTopTenToHunWardWiseData/{startDate}/{wardId}/{num}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTopTenToHunWardWiseData/{startDate}/{wardId}/{num}")
	@ApiOperation(
	    value = "Get Water Tax Top 10 to 100 All Ward Wise Data",
	    notes = "Returns Water Tax Top 10 to 100 All Ward Wise Data",
	    response = WaterTenHunWardZoneEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTenHunWardZoneEntity> getWaterTopTenToHunWardWiseData(@PathParam("startDate") String startDate, @PathParam("wardId") String wardId, @PathParam("num") String num) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterTenToHunWardWiseDataList(startDate, wardId, num);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTopTenToHunWardWiseData:", ex);
	    }

	    return null;
	}
	
	@RequestMapping(value = "/getWaterTopTenToHunWardZoneWiseData/{startDate}/{wardId}/{zoneId}/{num}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTopTenToHunWardZoneWiseData/{startDate}/{wardId}/{zoneId}/{num}")
	@ApiOperation(
	    value = "Get Water Tax Top 10 to 100 All Ward- Zone Wise Data",
	    notes = "Returns Water Tax Top 10 to 100 All Ward-Zone Wise Data",
	    response = WaterTenHunWardZoneEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTenHunWardZoneEntity> getWaterTopTenToHunWardZoneWiseData(@PathParam("startDate") String startDate, @PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId, @PathParam("num") String num) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterTenToHunWardZoneWiseDataList(startDate, wardId, zoneId, num);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTopTenToHunWardZoneWiseData:", ex);
	    }

	    return null;
	}
	
		
	@RequestMapping(value = "/getWaterTaxAllWardWiseCollectionData/{startDate}/{endDate}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTaxAllWardWiseCollectionData/{startDate}/{endDate}")
	@ApiOperation(
	    value = "Get Water Tax All Ward Wise Collection Data",
	    notes = "Returns Water Tax Dept  All Ward Wise Collection Data",
	    response = WaterTaxWardWiseCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxWardWiseCollectionEntity> getWaterTaxAllWardWiseCollectionData(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardWiseCollectionDataList(startDate, endDate);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTaxAllWardWiseCollectionData:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getWaterTaxWardWiseCollectionData/{startDate}/{endDate}/{wardId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTaxWardWiseCollectionData/{startDate}/{endDate}/{wardId}")
	@ApiOperation(
	    value = "Get Water Tax  Ward Wise Collection Data",
	    notes = "Returns Water Tax Dept Ward Wise Collection Data",
	    response = WaterTaxWardWiseCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WaterWardWiseCollectionEntity> getWaterTaxWardWiseCollectionData(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("wardId") String wardId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWardWiseCollectionDataList(startDate, endDate, wardId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTaxWardWiseCollectionData:", ex);
	    }

	    return null;
	}
	
	
	@RequestMapping(value = "/getWaterTaxWardAndZoneWiseCollectionData/{startDate}/{endDate}/{wardId}/{zoneId}", 
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GET
	@Path("/getWaterTaxWardAndZoneWiseCollectionData/{startDate}/{endDate}/{wardId}/{zoneId}")
	@ApiOperation(
	    value = "Get Water Tax Ward-Zone Wise Collection Data",
	    notes = "Returns Water Tax Dept  Ward-Zone Wise Collection Data",
	    response = WaterTaxWardAndZoneWiseCollectionEntity.class,
	    responseContainer = "List"
	)
	public List<WaterTaxWardAndZoneWiseCollectionEntity> getWaterTaxWardAndZoneWiseCollectionData(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("wardId") String wardId, @PathParam("zoneId") String zoneId) {

	    try {
	        return citizenDashboardGraphServiceImpl.getWaterWardAndZoneWiseCollectionDataList(startDate, endDate, wardId, zoneId);
	    } catch (final Exception ex) {
	        LOGGER.error("Error occurred while fetching getWaterTaxWardAndZoneWiseCollectionData:", ex);
	    }

	    return null;
	}
	 
}
