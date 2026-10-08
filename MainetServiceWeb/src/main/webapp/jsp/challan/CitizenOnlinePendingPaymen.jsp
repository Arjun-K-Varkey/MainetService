<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<script>
	$(function() {
		$("#fromDateId").datepicker({
			dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			maxDate : '0'
		});

		$("#fromDateId").keyup(function(e) {
			if (e.keyCode != 8) {
				if ($(this).val().length == 2) {
					$(this).val($(this).val() + "/");
				} else if ($(this).val().length == 5) {
					$(this).val($(this).val() + "/");
				}
			}
		});
		$("#ToDateId").datepicker({
			dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			maxDate : '0'
		});
		$("#ToDateId").keyup(function(e) {
			if (e.keyCode != 8) {
				if ($(this).val().length == 2) {
					$(this).val($(this).val() + "/");
				} else if ($(this).val().length == 5) {
					$(this).val($(this).val() + "/");
				}
			}
		});

	});

	function submitCitizenForm(obj) {
		debugger;
		var errorList = [];
		var fromDateId = $("#fromDateId").val();
		var toDateId = $("#ToDateId").val();
		var scrutinyId = $("#scrutinyId").val();
		var deptId = $("#deptId").val();
		if (fromDateId == '' || fromDateId == undefined) {
			errorList.push(getLocalMessage("Please select from date "));
		}
		if (toDateId == '' || toDateId == undefined) {
			errorList.push(getLocalMessage("Please select to date "));
		}
		if (scrutinyId == '' || scrutinyId == undefined || scrutinyId == 0) {
			errorList.push(getLocalMessage("Please select service name "));
		}if (deptId == '' || deptId == undefined || deptId == 0) {
			errorList.push(getLocalMessage("Please select Department  name "));
		}
		/* var URL = 'CitizenOnlinePendingPayment.html?initiatePendPayment';
		var formName = findClosestElementId(obj, 'form');

		var theForm = '#' + formName;

		$(theForm).attr('action', URL);

		$(theForm).submit(); */
		if (errorList.length == 0) {
			return saveOrUpdateForm(obj,
					"Citizen Pending Payment Update Succsessfully",
					'CitizenOnlinePendingPayment.html', 'initiatePendPayment');
		} else {
			displayErrorsOnPage(errorList);
		}

	}
	function searchServicesBydeptId() {
		var requestData = {
			"deptId" : $("#deptId").val()
		};
		$('#scrutinyId').html('');
		$('#scrutinyId').append(
				$("<option></option>").attr("value", "-1").text(
						getLocalMessage('All')));

		var ajaxResponse = doAjaxLoading(
				'CitizenOnlinePendingPayment.html?searchServicesBydeptId', requestData,
				'html');
		var prePopulate = JSON.parse(ajaxResponse);

		$.each(prePopulate, function(index, value) {
			$('#scrutinyId').append(
					$("<option></option>").attr("value", value.smServiceId).text(
							(value.smServiceName)));
		});
		$('#scrutinyId').trigger("chosen:updated");
	}
</script>
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>Citizen Pending Payment</h2>
		</div>
		<apptags:helpDoc url="CitizenOnlinePendingPayment.html"
			helpDocRefURL="CitizenOnlinePendingPayment.html"></apptags:helpDoc>
		<div class="widget-content padding">
			<form:form action="" class="form-horizontal" id="citizenForm"
				name="citizenForm">
				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span id="errorId"></span>
				</div>

				<div class="form-group">
					<label for="date-1493383113506" class="col-sm-2 control-label "><spring:message
							code="" text="From Date" /><span class="required-control"></span>
					</label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="challanDTO.validDate" id="fromDateId"
								cssClass="mandColorClass form-control" data-rule-required="true"
								maxlength="10" />
							<label class="input-group-addon mandColorClass" for="fromDateId"><i
								class="fa fa-calendar"></i> </label>
						</div>
					</div>
					<label for="date-1493383113507" class="col-sm-2 control-label "><spring:message
							code="" text="To Date" /><span class="required-control"></span>
					</label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="challanDTO.todate" id="ToDateId"
								cssClass="mandColorClass form-control" data-rule-required="true"
								maxlength="10" />
							<label class="input-group-addon mandColorClass" for="ToDateId"><i
								class="fa fa-calendar"></i> </label>
						</div>
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="common.master.dept.name" text="Department  Name" /></label>
					<div class="col-sm-4">
						<form:select path="offlineDTO.deptId"
							class="form-control chosen-select-no-results"
							data-rule-required="true" id="deptId"
							onchange="searchServicesBydeptId()">
							<form:option value="">
								<spring:message code="scrutiny.select" text="Select" />
							</form:option>
							<c:choose>
								<c:when test="${userSession.languageId eq 1}">
									<c:forEach items="${command.deptList}" var="serviceData">
										<form:option value="${serviceData.dpDeptid }">${serviceData.dpDeptdesc }</form:option>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:forEach items="${command.deptList}" var="serviceData">
										<form:option value="${serviceData.dpDeptid }">${serviceData.dpNameMar }</form:option>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</form:select>


					</div>
					<label class="col-sm-2 control-label required-control"><spring:message
							code="common.master.service.name" text="Service Name" /></label>
					<div class="col-sm-4">
						<form:select path="serviceId"
							class="form-control chosen-select-no-results"
							data-rule-required="true" id="scrutinyId">
							<form:option value="-1">
								<spring:message code="scrutiny.ALL" text="ALL" />
							</form:option>
							<c:choose>
								<c:when test="${userSession.languageId eq 1}">
									<c:forEach items="${command.serviceList}" var="serviceData">
										<form:option value="${serviceData.smServiceId }">${serviceData.smServiceName }</form:option>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:forEach items="${command.serviceList}" var="serviceData">
										<form:option value="${serviceData.smServiceId }">${serviceData.smServiceNameMar }</form:option>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</form:select>


					</div>






				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="Flat No"><spring:message
							code="Flat No" text="Reference No" /></label>
					<div class="col-sm-4">
						<form:input name="referenceNo" type="text" class="form-control "
							id="flatNo" path="offlineDTO.referenceNo"></form:input>
					</div>
					<label class="col-sm-2 control-label" for="Flat No"><spring:message
							code="Flat No" text="Flat No" /></label>
					<div class="col-sm-4">
						<form:input name="flatNo" type="text" class="form-control"
							id="flatNo" path="offlineDTO.flatNo"></form:input>
					</div>




				</div>
				<div class="text-center padding-top-10">
					<button type="button" class="btn btn-blue-2"
						onClick="submitCitizenForm(this)" title="View Report">
						<spring:message code="" text="Submit"></spring:message>
					</button>
					<button type="button" class="btn btn-warning"
						onClick="cashBookReset(this)" title="Reset">
						<spring:message code="" text="Reset" />
					</button>
					<apptags:backButton url="AdminHome.html"></apptags:backButton>
				</div>
			</form:form>
		</div>
	</div>
</div>
