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

<script type="text/javascript" src="js/rnl/master/rl-file-upload.js"></script>
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
			<form:form method="POST" action="PropertyCitizenSlotAllowanceController.html"
				class="form-horizontal" name="propertyCitizenSlotAllowanceMastForm"
				id="propertyCitizenSlotAllowanceMastForm">

				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				</div>

				<div class="panel-group accordion-toggle"
					id="accordion_single_collapse">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse"
									href="#propertyCitizenSlotAllowanceDetails"><spring:message
										code="rnl.property.citizen.slot.allow.info"
										text="Property Citizen Slot Allowance Information" /> </a>
							</h4>
						</div>

						<div id="properCitizenSlotAllowMaster" class="panel-collapse collapse in">
							<div class="panel-body">

								<div class="form-group">
									<label for="" class="control-label col-sm-2 required-control"><spring:message code="estate.label.name"
										text="Estate Name"></spring:message></label>
									<div class="col-sm-4">
										<form:select path="propSlotDto.estateId"
											class="form-control mandColorClass" id="estateId" onchange="getPropertyList()"
											disabled="${command.viewMode eq 'V' ? true : false}" >
											<form:option value="0">
												<spring:message code="selectdropdown" />
											</form:option>
											<c:forEach items="${command.estateMasterList}" var="objArray">
												<form:option value="${objArray[0]}">
													<c:choose>
														<c:when test="${userSession.languageId eq 2}">${objArray[3]}</c:when>
														<c:otherwise>${objArray[2]}</c:otherwise>
													</c:choose>
												</form:option>
											</c:forEach>
										</form:select>
									</div>
									
									<label for="" class="control-label col-sm-2 required-control"><spring:message code="rl.property.label.name"
										text="Property Name"></spring:message></label>
									<div class="col-sm-4">
										<form:select path="propSlotDto.propertyId"
											class="form-control mandColorClass" id="propertyId"
											disabled="${command.viewMode eq 'V' ? true : false}" >
											<form:option value="0">
												<spring:message code="selectdropdown" />
											</form:option>
											<c:forEach items="${command.propMasterList}" var="objArray">
												<form:option value="${objArray[0]}">
													<c:choose>
														<c:when test="${userSession.languageId eq 2}">${objArray[1]}</c:when>
														<c:otherwise>${objArray[1]}</c:otherwise>
													</c:choose>
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="FromDate"><spring:message code="rnl.from.date"
											text="From Date"></spring:message></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input path="propSlotDto.fromDate" type="text"
												id="fromDate" class="lessthancurrdatefrom form-control"
												disabled="${command.viewMode eq 'V' ? true : false}" ></form:input>
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i><span class="hide"><spring:message
														code="rnl.from.date" text="From Date"></spring:message></span></span>
										</div>
									</div>
									<label class="control-label col-sm-2 required-control"
										for="ToDate"><spring:message code="rnl.to.date"
											text="To Date"></spring:message></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input path="propSlotDto.toDate" type="text"
												id="toDate" class="lessthancurrdateto form-control" 
												disabled="${command.viewMode eq 'V' ? true : false}" ></form:input>
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i><span class="hide"><spring:message
														code="rnl.to.date" text="To Date"></spring:message></span></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="Shift"><spring:message code="rnl.master.shift"
											text="Shift"></spring:message></label>
									<div class="col-sm-4">
										<c:set var="baseLookupCode" value="SHF" />
										<form:select path="propSlotDto.shiftId"
											class="shift form-control" id="shiftId" disabled="${command.viewMode eq 'V' ? true : false}">
											<form:option value="0">
												<spring:message code="rnl.master.select" text="Select"></spring:message>
											</form:option>
											<c:forEach items="${command.getLevelData(baseLookupCode)}"
												var="lookUp">
												<form:option value="${lookUp.lookUpId}"
													code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>
									</div>
									
									<label for="" class="control-label col-sm-2 required-control"><spring:message code="rnl.property.citizen.slot.allow.booking"
										text="Allow for Citizen Booking"></spring:message></label>
									<div class="col-sm-4">
										<form:select path="propSlotDto.citizenAllowanceStatus" disabled="${command.viewMode eq 'V' ? true : false}"
											class="form-control mandColorClass" id="citizenAllowanceStatusId">
											<form:option value="">
												<spring:message code="selectdropdown" />
											</form:option>
											<form:option value="Y">
												<spring:message code="rnl.master.yes" />
											</form:option>
											<form:option value="N">
												<spring:message code="rnl.master.no" />
											</form:option>									
										</form:select>
									</div>

								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="remark"><spring:message
											code='rnl.property.citizen.slot.allow.remark' text="Remark" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="propSlotDto.remark" id="remark"
											maxlength="500"
											disabled="${command.viewMode eq 'V' ? true : false}" />
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>

				<div class="text-center padding-bottom-10">
					<c:if test="${command.viewMode ne 'V'}">
						<button type="button" class="btn btn-success"
							title='<spring:message code="bt.save" text="Submit" />'
							onclick="savePropSlotAllowMastForm(this);">
							<spring:message code="bt.save" text="Submit" />
						</button>
						
						<a role="button" class="btn btn-warning"
						href="PropertyCitizenSlotAllowanceController.html"><spring:message
							code="bt.clear" text="Reset" /></a>
							
					</c:if>

					<apptags:backButton url="PropertyCitizenSlotAllowanceController.html"></apptags:backButton>
				</div>

			</form:form>
		</div>
	</div>
</div>
