<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="js/account/accountStopPayment.js"></script>
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script>
	$(document).ready(function() {
		var table = $('.main1').DataTable({
			"oLanguage" : {
				"sSearch" : ""
			},
			"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
			"iDisplayLength" : 5,
			"bInfo" : true,
			"lengthChange" : true,
			"bPaginate" : true,
			"bFilter" : true,
			"ordering" : false,
			"order" : [ [ 1, "desc" ] ]
		});
	});
</script>

<apptags:breadcrumb></apptags:breadcrumb>
<div class="form-div margin-top-40" id="content">
	<!-- Start info box -->
	<div class="widget">
		<div class="widget-header">
			<h2>Stop Payment </h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i><span class="hide">Help</span></a>
			</div>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span>Field with <i class="text-red-1">*</i> is mandatory
				</span>
			</div>
			<form:form action="" modelAttribute="stopPayment"
				class="form-horizontal" id="tbChequeDishonour">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span id="errorId"></span>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-2 control-label">From Date<span
						class="mand">*</span></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="formDate" cssClass="datepicker form-control"
								id="fromDate" maxlength="10" />
							<label class="input-group-addon" for="toDate"><i
								class="fa fa-calendar"></i> <input type="hidden"
								id="trasaction-date-icon30"></label>
						</div>
					</div>
					<label for="" class="col-sm-2 control-label">To Date<span
						class="mand">*</span></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="toDate" cssClass="datepicker form-control"
								name="toDate" id="toDate" maxlength="10" />
							<label class="input-group-addon" for="toDate"><i
								class="fa fa-calendar"></i> <input type="hidden"
								id="trasaction-date-icon30"></label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-2 control-label">Bank Account<span
						class="mand">*</span>
					</label>
					<div class="col-sm-4">
						<form:select id="bankAccount" path="bankAccount"
							class="form-control chosen-select-no-results mandColorClass">
							<form:option value="">
								<spring:message code="" text="Select" />
							</form:option>
							<c:forEach items="${bankList}" varStatus="status"
								var="bankAccountMap">
								<form:option value="${bankAccountMap.key}"
									code="${bankAccountMap.key}">${bankAccountMap.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="text-center col-sm-12 margin-top-10 margin-bottom-10">
					<button type="button" class="btn btn-success padding-top-5"
						title="Search" onclick="searchBankReconcilationData();" id="searchId">
						<i class="fa fa-search padding-right-5" aria-hidden="true"></i>Search
					</button>
					<button type="button" class="btn btn-warning " title="Reset"
						onclick="resetReconcilation();">
						<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>Reset
					</button>
					<button type="button" class="btn btn-blue-2 " title="Add"
						onclick="addBankReconcilationForm('AccountStopPayment.html','AddForm');" id="addId">
						<i class="fa fa-plus-circle" aria-hidden="true"></i>Add
					</button>
					<%-- Defect #153407 --%>
					<apptags:backButton url="AdminHome.html" cssClass="btn btn-danger"></apptags:backButton>
				</div>
				<div class="table-responsive clear">
					<table class="table table-bordered table-condensed main1"
						id="main-table">
						<thead>
							<tr>
								<th class="text-center">Payment Date</th>
								<th class="text-center">Payment No</th>
								<th class="text-center">Payment Mode</th>
								<th class="text-center">Instrument Number</th>
								<th class="text-center">Instrument Date</th>
								<th class="text-center">Payment Amount</th>
								<th class="text-center">Stop Payment Date</th>
								<th class="text-center">Stop Payment Reason</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${stopPayment.stopPaymentDto}"
								var="rcltn" varStatus="status">
								<c:set value="${status.index}" var="count"></c:set>
								<tr>
									<td class="text-center">${rcltn.paymentDate}</td>
									<td>${rcltn.paymentNo}</td>
									<td>${rcltn.transMode}</td>
									<td>${rcltn.chequeddno}</td>
									<td class="text-center">${rcltn.chequedddate}</td>
									<td class="text-right">${rcltn.amount}</td>
									<td class="text-center">${rcltn.stopPaymentDate}</td>
									<td>${rcltn.stopPaymentReason}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form:form>
		</div>
	</div>
</div>