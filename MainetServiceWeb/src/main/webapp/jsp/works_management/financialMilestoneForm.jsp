<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript"
	src="js/works_management/financialMilestone.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<!-- <script>
jQuery(document).ready(function(){
	jQuery("#projId").chosen();
});
</script> -->

<%
    response.setContentType("text/html; charset=utf-8");
%>

<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="mileStone.financial"
					text="Financial  Milestones" />
			</h2>
			<div class="additional-btn">
				 <apptags:helpDoc url="FinancialMilestone.html"></apptags:helpDoc>

			</div>
		</div>

		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="" text="" /> <i
					class="text-red-1"></i> <spring:message
						code="works.fiels.mandatory.message" text="" /></span>
			</div>
			<form:form action="FinancialMilestone.html" class="form-horizontal"
				name="financialMilestone" id="financialMilestone">
				<!-- Start Validation include tag -->
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>
				<form:hidden path="saveMode" id="mode" />
				<form:hidden path="removedIds" id="removedIds" />
				<form:hidden path="" id="checkMileStone" />
				<c:if test="${command.saveMode ne 'C'}">
					<form:hidden path="mileStoneDTO.projId" id="editProjId" />
					<form:hidden path="mileStoneDTO.workId" id="workId" />
				</c:if>
				<div class="panel-group accordion-toggle"
					id="accordion_single_collapse">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-target="#a1" data-toggle="collapse" class="collapsed"
									data-parent="#accordion_single_collapse" href="#a1"><spring:message
										code="wms.details" text="Details" /></a>
							</h4>
						</div>
						<div id="a1" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="form-group">
									<c:if test="${command.saveMode eq 'C'}">
										<label class="col-sm-2 control-label  required-control"><spring:message
												code="project.master.projname" /></label>
										<div class="col-sm-4">
									<form:select path="mileStoneDTO.projId" id="projId"
												class="form-control chosen-select-no-results"
												onchange="getCreateworkNameByProjId(this);"
												disabled="${command.saveMode ne 'C' ? true : false }">
												<form:option value="">
													<spring:message code='work.management.select' />
												</form:option>
												<c:if
													test="${userSession.getCurrent().getLanguageId() ne '1'}">
													<c:forEach items="${command.projectMasterList}"
														var="activeProjName">
														<form:option value="${activeProjName.projId }"
															code="${activeProjName.projId }">${activeProjName.projNameReg}</form:option>
													</c:forEach>
												</c:if>
												<c:if
													test="${userSession.getCurrent().getLanguageId() eq '1'}">
													<c:forEach items="${command.projectMasterList}"
														var="activeProjName">
														<form:option value="${activeProjName.projId }"
															code="${activeProjName.projId }">${activeProjName.projNameEng}</form:option>
													</c:forEach>
												</c:if>
											</form:select>
										</div>
									</c:if>
									<c:if test="${command.saveMode ne 'C'}">
										<label class="col-sm-2 control-label "><spring:message
												code="project.master.projname" /></label>
										<div class="col-sm-4">
											<form:input path="mileStoneDTO.projName" id="projId"
												class="form-control" readonly="true" />
										</div>

									</c:if>
									<c:if test="${command.saveMode eq 'C'}">
										<label class="col-sm-2 control-label"><spring:message
												code="work.def.workname" /></label>
										<div class="col-sm-4">
											<form:select path="mileStoneDTO.workId" id="workId"
												class="form-control chosen-select-no-results"
												disabled="${command.saveMode ne 'C' ? true : false }">
												<form:option value="">
													<spring:message code='work.management.select' />
												</form:option>
											</form:select>
										</div>
									</c:if>
									<c:if test="${command.saveMode ne 'C'}">
										<label class="col-sm-2 control-label"><spring:message
												code="work.def.workname" /></label>
										<div class="col-sm-4">
											<form:input path="mileStoneDTO.workName" id="workName"
												class="form-control" readonly="true" />
										</div>
									</c:if>
								</div>
								<%-- <div class="form-group">

									<label class="col-sm-2 control-label"><spring:message
											code="" text="Milestone" /></label>
									<div class="col-sm-4">
										<form:input path="mileStoneDTO.mileStoneName" id="milestone"
											class="form-control" disabled="${command.saveMode ne 'C' ? true : false }"/>
									</div>
								</div> --%>
							</div>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-target="#a2" data-toggle="collapse" class="collapsed"
									data-parent="#accordion_single_collapse" href="#a2"><spring:message
										code="wms.AddFinancialMilestone"
										text="Add Financial Milestone Details" /></a>
							</h4>
						</div>
						<div id="a2" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="table-responsive">
									<c:set var="d" value="0" scope="page"></c:set>
									<div class="table-responsive">
										<table class="table table-bordered table-striped"
											id="milestoneTbl">
											<thead>
												<tr>
													<th scope="col" width="3%"><spring:message
															code="ser.no" text="Sr.No." /></th>
													<th scope="col" width="10%"><spring:message
															code="work.management.description" text="Description" /><span
														class="mand">*</span></th>
													<th scope="col" width="10%"><spring:message
															code="mileStone.weightage" text="Weightage" /><span
														class="mand">*</span></th>
													<th scope="col" width="10%"><spring:message
															code="material.master.startdate" text="Start Date" /><span
														class="mand">*</span></th>
													<th scope="col" width="10%"><spring:message
															code="material.master.enddate" text="End Date" /><span
														class="mand">*</span></th>
													<c:if test="${command.saveMode eq 'F'}">
														<th scope="col" width="10%"><spring:message
																code="mileStone.percentComplete" text="% Completed" /><span
															class="mand">*</span></th>
													</c:if>
													<c:if
														test="${command.saveMode eq 'C' || command.saveMode eq 'E'}">
														<th scope="col" width="5%"><a
															href="javascript:void(0);" data-toggle="tooltip"
															data-placement="top" onclick="addEntryData();"
															class=" btn btn-success btn-sm" title="<spring:message code="works.management.add"></spring:message>"><i
																class="fa fa-plus-circle"></i></a></th>
													</c:if>
													<!-- D#131858 -->
													<c:if
														test="${command.saveMode eq 'F' || command.saveMode eq 'V'}">
														<th scope="col" width="5%"><spring:message
																code="works.management.action" text="Action" /></th>
													</c:if>
												</tr>
											</thead>
											<tfoot>
												<tr>
													<th colspan="2" style="text-align: right"><spring:message
															code="wms.TotalWeightage" text="Total Weightage :" /></th>
													<th><form:input id="totalWeightage"
															class=" form-control text-right" maxlength="500" path=""
															readonly="true" /></th>
													<th></th>
													<th></th>
													<c:if test="${command.saveMode ne 'V'}">
														<th></th>
													</c:if>
												</tr>
											</tfoot>
											<tbody>
												<c:choose>
													<c:when
														test="${fn:length(command.milestoneDtoList)>0}">
														<c:forEach var="milestoneData"
															items="${command.milestoneDtoList}">
															<tr class="appendableClass">
																<form:hidden path="milestoneDtoList[${d}].mileId"
																	id="mileId${d}" />
																<td align="center"><form:input path=""
																		cssClass="form-control mandColorClass "
																		id="sequence${d}" value="${d+1}" disabled="true" /></td>
																<td align="center" id="from"><form:input
																		path="milestoneDtoList[${d}].mileStoneDesc"
																		cssClass="form-control mandColorClass"
																		id="mileStoneDesc${d}"
																		disabled="${command.saveMode eq 'V' ? true : false }" /></td>
																<td align="right"><form:input
																		path="milestoneDtoList[${d}].mileStoneWeight"
																		cssClass="form-control mandColorClass text-right"
																		onkeypress="return hasAmount(event, this, 3, 2)"
																		onchange="getAmountFormatInDynamic((this),'mileStoneWeight')"
																		id="mileStoneWeight${d}" onblur="getValue();"
																		disabled="${command.saveMode eq 'V' ? true : false }" /></td>
																<td align="center"><div class="input-group"><form:input
																		path="milestoneDtoList[${d}].msStartDate" 
																		cssClass="form-control mandColorClass datepicker"
																		id="msStartDate${d}" readonly="true"
																		disabled="${command.saveMode eq 'V' ? true : false }" />
																		<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
																		</div></td>
																<td align="center"><div class="input-group"><form:input
																		path="milestoneDtoList[${d}].msEndDate"
																		cssClass="form-control mandColorClass datepicker "
																		id="msEndDate${d}" readonly="true"
																		disabled="${command.saveMode eq 'V' ? true : false }" />
																			<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
																		</div></td>
																<c:if test="${command.saveMode eq 'F'}">
																	<td align="center" id="percentage"><form:input
																			path="milestoneDtoList[${d}].msPercent"
																			cssClass="form-control mandColorClass text-right"
																			onkeypress="return hasAmount(event, this, 3, 2)"
																			onchange="getAmountFormatInDynamic((this),'msPercent')"
																			id="msPercent${d}" /></td>
																</c:if>
																<%-- <c:if test="${command.saveMode eq 'E'}"> --%>
																	<td align="center"><a href='#'
																		data-toggle="tooltip" data-placement="top"
																		class="btn btn-danger btn-sm delButton"
																		onclick="deleteEntry($(this),'removedIds');" title="<spring:message code="works.management.delete"></spring:message>"><i
																			class="fa fa-minus"></i></a></td>
																<%-- </c:if> --%>
																<c:set var="d" value="${d + 1}" scope="page" />
															</tr>
														</c:forEach>
													</c:when>

													<c:when test="${command.saveMode eq 'F'}">
														<c:forEach var="milestoneData"
															items="${command.milestoneDtoList}">
															<tr class="appendableClass">
																<form:hidden path="milestoneDtoList[${d}].mileId"
																	id="mileId${d}" />
																<td align="center"><form:input path=""
																		cssClass="form-control mandColorClass "
																		id="sequence${d}" value="${d+1}" disabled="true" /></td>
																<td align="center" id="from"><form:input
																		path="milestoneDtoList[${d}].mileStoneDesc"
																		cssClass="form-control mandColorClass"
																		id="mileStoneDesc${d}" readonly="true" /></td>
																<td align="right"><form:input
																		path="milestoneDtoList[${d}].mileStoneWeight"
																		cssClass="form-control mandColorClass text-right"
																		onkeypress="return hasAmount(event, this, 3, 2)"
																		id="mileStoneWeight${d}" readonly="true" /></td>
																<td align="center"><div class="input-group"><form:input
																		path="milestoneDtoList[${d}].msStartDate"
																		cssClass="form-control mandColorClass "
																		id="msStartDate${d}" readonly="true" />
																	<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
																		</div></td>
																<td align="center"><div class="input-group"> <form:input
																		path="milestoneDtoList[${d}].msEndDate"
																		cssClass="form-control mandColorClass "
																		id="msEndDate${d}" readonly="true" />
																<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
																		</div></td>
																<td align="center" id="percentage"><form:input
																		path="milestoneDtoList[${d}].msPercent"
																		cssClass="form-control mandColorClass text-right"
																		onkeypress="return hasAmount(event, this, 3, 2)"
																		onchange="getAmountFormatInDynamic((this),'msPercent')"
																		id="msPercent${d}" /></td>
																<c:if test="${command.saveMode eq 'E'}">
																	<td align="center"><a href='#'
																		data-toggle="tooltip" data-placement="top"
																		class="btn btn-danger btn-sm delButton" title="<spring:message code="works.management.delete"></spring:message>"
																		onclick="deleteEntry($(this),'removedIds');"><i
																			class="fa fa-minus"></i></a></td>
																</c:if>
																<c:set var="d" value="${d + 1}" scope="page" />
															</tr>
														</c:forEach>
													</c:when>

													<c:otherwise>
														<tr class="appendableClass">
															<form:hidden path="milestoneDtoList[${d}].mileId"
																id="mileId${d}" />
															<td align="center"><form:input path=""
																	cssClass="form-control mandColorClass "
																	id="sequence${d}" value="${d+1}" disabled="true" /></td>
															<td align="center" id="from"><form:input
																	path="milestoneDtoList[${d}].mileStoneDesc"
																	cssClass="form-control mandColorClass"
																	id="mileStoneDesc${d}" /></td>
															<td align="right"><form:input
																	path="milestoneDtoList[${d}].mileStoneWeight"
																	cssClass="form-control mandColorClass text-right"
																	onkeypress="return hasAmount(event, this, 3, 2)"
																	onchange="getAmountFormatInDynamic((this),'mileStoneWeight')"
																	onblur="getValue();" id="mileStoneWeight${d}" /></td>
															<td align="center"><div class="input-group"><form:input
																	path="milestoneDtoList[${d}].msStartDate"
																	cssClass="form-control mandColorClass datepicker"
																	id="msStartDate${d}" readonly="true" />
															   	<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
																</div></td>
															<td align="center"><div class="input-group"><form:input
																	path="milestoneDtoList[${d}].msEndDate"
																	cssClass="form-control mandColorClass datepicker "
																	id="msEndDate${d}" readonly="true" />
																		<label class="input-group-addon"><i	class="fa fa-calendar"></i><span class="hide"> <spring:message
														           	code="" text="icon" /></span><input type="hidden"></label>
															</div></td>
															<c:if test="${command.saveMode eq 'F'}">
																<td align="center" id="percentage"><form:input
																		path="milestoneDtoList[${d}].msPercent"
																		cssClass="form-control mandColorClass text-right"
																		onkeypress="return hasAmount(event, this, 3, 2)"
																		onchange="getAmountFormatInDynamic((this),'msPercent')"
																		id="msPercent${d}" /></td>
															</c:if>
															<td align="center"><a href='#' data-toggle="tooltip"
																data-placement="top"
																class="btn btn-danger btn-sm delButton" title="<spring:message code="works.management.delete"></spring:message>"
																onclick="deleteEntry($(this),'removedIds');"><i
																	class="fa fa-minus"></i></a></td>
															<c:set var="d" value="${d + 1}" scope="page" />
														</tr>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Start button -->
				<div class="text-center clear padding-10">
					<c:if test="${command.saveMode eq 'E' || command.saveMode eq 'C'}">
						<button type="button" id="save" class="btn btn-success btn-submit"
							onclick="saveData(this);">
							<i class="fa fa-sign-out padding-right-5"></i>
							<spring:message code="works.management.save" text="Save" />
						</button>
					</c:if>

					<c:if test="${command.saveMode eq 'F'}">
						<button type="button" id="save" class="btn btn-success btn-submit"
							onclick="savePercentage(this);">
							<i class="fa fa-sign-out padding-right-5"></i>
							<spring:message code="works.management.save" text="Save" />
						</button>
					</c:if>
					<c:if test="${command.saveMode eq 'C'}">
						<button class="btn btn-warning" type="button" id=""
							onclick="resetFinancialMilestone();">
							<i class="fa fa-undo padding-right-5"></i>
							<spring:message code="works.management.reset" text="Reset" />
						</button>
					</c:if>

					<button type="button" class="button-input btn btn-danger"
						name="button-Cancel" value="Cancel" style=""
						onclick="window.location.href='FinancialMilestone.html'"
						id="button-Cancel">
						<i class="fa fa-chevron-circle-left padding-right-5"></i>
						<spring:message code="works.management.back" text="" />
					</button>

				</div>
			</form:form>
		</div>
	</div>
</div>
