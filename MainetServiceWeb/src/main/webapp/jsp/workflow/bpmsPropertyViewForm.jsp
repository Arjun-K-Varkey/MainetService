<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>

<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript"
	src="js/workflow/bpmsPropertyDraftForm.js"></script>
<link rel="stylesheet"
	href="assets/libs/bootstrap-multiselect/css/bootstrap-multiselect.css"
	type="text/css">
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content animated slideInDown">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message text="BPMS Property View" />
			</h2>
			<apptags:helpDoc url="BPMSPropertyDraft.html"></apptags:helpDoc>
		</div>

		<div class="widget-content padding">

			<div class="mand-label clearfix">
				<span> Field with <i class="text-red-1">*</i> is mandatory
				</span>
			</div>

			<div class="error-div alert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<ul>
					<li><label id="errorId"></label></li>
				</ul>
			</div>

			<form:form method="POST" class="form-horizontal"
				modelAttribute="model">

				<!-- ===== STATUS ===== -->
				<c:if test="${not empty model and not empty model.responseDataDTO}">
					<div class="form-group">
						<label class="col-sm-2 control-label">Status</label>
						<div class="col-sm-4">
							<input type="text" readonly class="form-control"
								value="${model.responseDataDTO.status}" />
						</div>
					</div>
				</c:if>

				<!-- ===== RESPONSE DATA ===== -->
				<c:if test="${not empty command.responseDataDTO.responseData}">
					<c:forEach var="data"
						items="${command.responseDataDTO.responseData}" varStatus="s">

						<hr style="border: 1px solid #ccc;" />

						<!-- ===== ULB DETAILS ===== -->
						<c:if test="${not empty data.ulbDetails}">
							<c:forEach var="ulb" items="${data.ulbDetails}">
								<div class="form-group">
									<label class="col-sm-2 control-label">ULB Name</label>
									<div class="col-sm-4">
										<input type="text" readonly class="form-control"
											value="${ulb.ulbName}" />
									</div>

									<label class="col-sm-2 control-label">ULB Code</label>
									<div class="col-sm-4">
										<input type="text" readonly class="form-control"
											value="${ulb.ulbCode}" />
									</div>
								</div>
							</c:forEach>
						</c:if>

						<br />

						<!-- ===== PROJECT DETAILS ===== -->
						<c:if test="${not empty data.projectDetails}">
							<h4 class="text-dark">
								<strong>Project Details</strong>
							</h4>
							<table class="table table-bordered table-striped table-hover">
								<thead
									style="background-color: #f2f2f2; font-weight: bold; text-align: center;">
									<tr>
										<th>Project Code</th>
										<th>Project Name</th>
										<th>Project Risk Flag</th>
										<th>Proposal ID</th>
										<th>Site Address</th>
										<th>Residential Plot Area</th>
										<th>Total Plot Area</th>
										<th>Application Date</th>
										<th>Approval Date</th>
										<th>Construction Type</th>
										<th>Ward</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="p" items="${data.projectDetails}"
										varStatus="status">
										<tr
											style="background-color:${status.index % 2 == 0 ? '#ffffff' : '#f9f9f9'};">
											<td style="text-align: center;">${p.project_code}</td>
											<td>${p.project_name}</td>
											<td style="text-align: center;">${p.project_risk_flag}</td>
											<td style="text-align: center;">${p.proposal_id}</td>
											<td>${p.site_address}</td>
											<td style="text-align: right;">${p.residential_plot_area}</td>
											<td style="text-align: right;">${p.total_plot_area}</td>
											<td style="text-align: center;">${p.application_date}</td>
											<td style="text-align: center;">${p.approval_date}</td>
											<td>${p.construction_type}</td>
											<td>${p.administrative_ward}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<br />

						<!-- ===== OWNER DETAILS ===== -->
						<c:if test="${not empty data.ownerDetails}">
							<h4 class="text-dark">
								<strong>Owner Details</strong>
							</h4>
							<table class="table table-bordered table-striped table-hover">
								<thead
									style="background-color: #f2f2f2; font-weight: bold; text-align: center;">
									<tr>
										<th>Owner Name</th>
										<th>Phone</th>
										<th>Email</th>
										<th>Address</th>
										<th>Power Of Attorney</th>
										<th>Owner Type</th>
										<th>Is Applicant</th>
										<th>Aadhar No.</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="o" items="${data.ownerDetails}"
										varStatus="status">
										<tr
											style="background-color:${status.index % 2 == 0 ? '#ffffff' : '#f9f9f9'};">
											<td>${o.owner_name}</td>
											<td>${o.owner_phone}</td>
											<td>${o.owner_email}</td>
											<td>${o.owner_address}</td>
											<td>${o.owner_power_of_attorney}</td>
											<td>${o.owner_type}</td>
											<td>${o.owner_is_applicant}</td>
											<td>${o.owner_adhar_no}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<br />

						<!-- ===== BUILDING DETAILS ===== -->
						<c:if test="${not empty data.buildingDetails}">
							<h4 class="text-dark">
								<strong>Building Details</strong>
							</h4>

							<table class="table table-bordered table-striped table-hover">
								<thead
									style="background-color: #f2f2f2; font-weight: bold; text-align: center;">
									<tr>
										<th>Serial No.</th>
										<th>Building No</th>
										<th>Name</th>
										<th>Type</th>
										<th>Sub-Category</th>
										<th>No of Floors</th>
										<th>Base Height</th>
										<th>Plinth Level</th>
										<th>Buildup Area</th>
										<th>Tenements Units</th>
										<th>Commercial Area</th>
										<th>Industrial Area</th>
										<th>Height</th>
										<th>Front Margin</th>
										<th>Rear Margin</th>
										<th>Left Margin</th>
										<th>Right Margin</th>
										<th>Premium Buildup Area</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="b" items="${data.buildingDetails}"
										varStatus="status">
										<tr
											style="background-color:${status.index % 2 == 0 ? '#ffffff' : '#f9f9f9'};">
											<td>${b.building_serial_no}</td>
											<td>${b.building_no}</td>
											<td>${b.building_name}</td>
											<td>${b.building_type}</td>
											<td>${b.building_sub_category}</td>
											<td>${b.building_no_of_floors}</td>
											<td>${b.building_base_height}</td>
											<td>${b.building_plinth_level}</td>
											<td>${b.building_buildup_area}</td>
											<td>${b.building_tenements_units}</td>
											<td>${b.building_commercial_area}</td>
											<td>${b.building_industrial_area}</td>
											<td>${b.building_height}</td>
											<td>${b.building_front_margin}</td>
											<td>${b.building_rear_margin}</td>
											<td>${b.building_left_margin}</td>
											<td>${b.building_right_margin}</td>
											<td>${b.building_premium_buildup_area}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

							<!-- ================== FLOORS + ROOMS (All Buildings) ================== -->

							<h4 class="text-dark">
								<strong>Floors & Rooms</strong>
							</h4>

							<table class="table table-bordered table-striped table-hover">
								<thead
									style="background-color: #f2f2f2; font-weight: bold; text-align: center;">
									<tr>
										<th>Building Name</th>
										<th>Floor Serial No</th>
										<th>Floor Name</th>
										<th>Height</th>
										<th>Use</th>
										<th>Stilt Floor</th>
										<th>Ground Floor</th>
										<th>Parking Floor</th>
										<th>Floor Area</th>
										<th>Floor Area Used</th>
										<th>Room Name</th>
										<th>Area</th>
										<th>Width</th>
										<th>Length</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="b" items="${data.buildingDetails}">
										<c:forEach var="f" items="${b.floorDetails}">
											<c:set var="roomCount" value="0" />
											<c:forEach var="cpt" items="${f.carpet_details}">
												<c:forEach var="room" items="${cpt.room_details}">
													<c:set var="roomCount" value="${roomCount + 1}" />
												</c:forEach>
											</c:forEach>

											<c:if test="${roomCount == 0}">
												<tr>
													<td>${b.building_name}</td>
													<td>${f.floor_serial_no}</td>
													<td>${f.floor_name}</td>
													<td>${f.floor_height}</td>
													<td>${f.floor_use}</td>
													<td>${f.stilt_floor}</td>
													<td>${f.ground_floor}</td>
													<td>${f.parking_floor}</td>
													<td>${f.floor_area}</td>
													<td>${f.floor_area_used}</td>
													<td colspan="4" style="text-align: center;">No Room
														Data</td>
												</tr>
											</c:if>

											<c:if test="${roomCount > 0}">
												<c:set var="rowPrinted" value="false" />
												<c:forEach var="cpt" items="${f.carpet_details}">
													<c:forEach var="room" items="${cpt.room_details}"
														varStatus="status">
														<tr
															style="background-color:${status.index % 2 == 0 ? '#ffffff' : '#f9f9f9'};">
															<c:if test="${!rowPrinted}">
																<td rowspan="${roomCount}">${b.building_name}</td>
																<td rowspan="${roomCount}">${f.floor_serial_no}</td>
																<td rowspan="${roomCount}">${f.floor_name}</td>
																<td rowspan="${roomCount}">${f.floor_height}</td>
																<td rowspan="${roomCount}">${f.floor_use}</td>
																<td rowspan="${roomCount}">${f.stilt_floor}</td>
																<td rowspan="${roomCount}">${f.ground_floor}</td>
																<td rowspan="${roomCount}">${f.parking_floor}</td>
																<td rowspan="${roomCount}">${f.floor_area}</td>
																<td rowspan="${roomCount}">${f.floor_area_used}</td>
																<c:set var="rowPrinted" value="true" />
															</c:if>
															<td>${room.room_name}</td>
															<td>${room.room_area}</td>
															<td>${room.room_width}</td>
															<td>${room.room_length}</td>
														</tr>
													</c:forEach>
												</c:forEach>
											</c:if>

										</c:forEach>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<br />
						<br />

						<!-- ===== LAT LONG ===== -->
						<c:if test="${not empty data.latLongDetail}">
							<h4 class="text-dark">
								<strong>Latitude / Longitude</strong>
							</h4>
							<table class="table table-bordered table-striped table-hover">
								<thead
									style="background-color: #f2f2f2; font-weight: bold; text-align: center;">
									<tr>
										<th>Sr. No</th>
										<th>Latitude</th>
										<th>Longitude</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="lat" items="${data.latLongDetail}"
										varStatus="loop">
										<tr
											style="background-color:${loop.index % 2 == 0 ? '#ffffff' : '#f9f9f9'};">
											<td style="text-align: center;">${loop.index + 1}</td>
											<td>${lat.lat_of_plot}</td>
											<td>${lat.long_of_plot}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

					</c:forEach>
				</c:if>

				<br>

				<div class="text-center">
					<button type="button" class="btn btn-danger"
						onclick="window.location.href='BPMSPropertyDraft.html'">
						<spring:message code="care.back" text="Back" />
					</button>
				</div>

			</form:form>

		</div>
	</div>
</div>
