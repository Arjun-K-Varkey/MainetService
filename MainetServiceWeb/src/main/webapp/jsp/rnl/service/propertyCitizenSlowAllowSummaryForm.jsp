<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>

<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript"
	src="js/rnl/service/propertyCitizenSlotAllowMasterForm.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content animated slideInDown">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="rnl.property.citizen.slot.allow.master"
					text="Property Citizen Slot Allowance Master"></spring:message>
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i><span class="hide"><spring:message
							code="rnl.book.help" text="Help"></spring:message></span></a>
			</div>
		</div>
		<div class="widget-content padding">
			<form:form method="POST"
				action="PropertyCitizenSlotAllowanceController.html"
				class="form-horizontal"
				name="propertyCitizenSlotAllowanceMastSummaryForm"
				id="propertyCitizenSlotAllowanceMastSummaryForm">

				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				</div>

				<div class="form-group">
					<label for="" class="control-label col-sm-2 required-control"><spring:message
							code="rl.property.label.name" text="Property Name"></spring:message></label>
					<div class="col-sm-4">
						<form:select path="propSlotDto.propertyId"
							class="form-control mandColorClass" id="propertyId"
							disabled="${command.viewMode eq 'V' ? true : false}">
							<form:option value="0">
								<spring:message code="selectdropdown" />
							</form:option>
							<c:forEach items="${command.propMastList}" var="propMastDto">
								<form:option value="${propMastDto.propId}"
									code="${propMastDto.name}">${propMastDto.name}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>

				<div class="text-center padding-bottom-10">
					<button type="button" class="btn btn-success"
						onclick="searchCitizenBookingOpenSlot(this)">
						<i class="fa fa-search"></i>&nbsp;
						<spring:message code="bt.search" text="Search" />
					</button>
					<a role="button" class="btn btn-warning"
						href="PropertyCitizenSlotAllowanceController.html"><spring:message
							code="bt.clear" text="Reset" /></a>
					<button type="button" class="btn btn-blue-2"
						onclick="formForCreate(this);">
						<i class="fa fa-plus-circle"></i>&nbsp;
						<spring:message code="bt.add" />
					</button>
				</div>

				<div class="table-responsive clear">
					<table class="table table-striped table-bordered"
						id="propCitizenSlotAllowTableId">
						<thead>
							<tr>
								<th width="5%"><spring:message
										code="estate.table.column.srno" text="Sr. No."></spring:message></th>
								<%-- <th width=""><spring:message code="estate.label.name"
										text="Estate name"></spring:message></th> --%>
								<th width=""><spring:message code="rl.property.label.name"
										text="Property Name"></spring:message></th>
								<th width=""><spring:message code="rnl.from.date"
										text="From Date"></spring:message></th>
								<th width=""><spring:message code="rnl.to.date"
										text="To Date"></spring:message></th>
								<th width=""><spring:message
										code="rnl.property.citizen.slot.allow.remark" text="Remark"></spring:message></th>
								<th width=""><spring:message
										code="estate.grid.column.action" text="Action"></spring:message></th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${command.propCitizenSlotAllowList}" var="data"
								varStatus="count">
								<tr>
									<td class="text-center">${count.index+1}</td>
									<%-- <td class="text-center" >${data.estateName}</td> --%>
									<td class="text-center">${data.propertyName}</td>
									<td class="text-center">${data.fromDate}</td>
									<td class="text-center">${data.toDate}</td>
									<td class="text-center">${data.remark}</td>
									<td class="text-center">
										<button type="button" class="btn btn-blue-2 btn-sm"
											title="View"
											onclick="modifyCase(${data.pcSlotAllowId},'PropertyCitizenSlotAllowanceController.html','EDIT','V')">
											<i class="fa fa-eye"></i>
										</button>
										<button type="button" class="btn btn-warning btn-sm"
											title="Edit"
											onclick="modifyCase(${data.pcSlotAllowId},'PropertyCitizenSlotAllowanceController.html','EDIT','E')">
											<i class="fa fa-pencil"></i>
										</button>
									</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>

			</form:form>
		</div>
	</div>
</div>
