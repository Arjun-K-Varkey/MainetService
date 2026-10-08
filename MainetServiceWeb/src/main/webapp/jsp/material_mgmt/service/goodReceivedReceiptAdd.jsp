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
	rel="stylesheet" type="text/css">
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/script-library.js"></script>
<script src="js/material_mgmt/master/indentProcess.js"
	type="text/javascript"></script>

<script>
$(function() {
	$(document).ready(
			function() {
				$("#id_itemOpeningBalance").dataTable({
					"oLanguage" : {
						"sSearch" : ""
					},
					"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
					"iDisplayLength" : 5,
					"bInfo" : true,
					"lengthChange" : true
				});
			});

 
});
</script>

<div id="searchAssetPage">
	<apptags:breadcrumb></apptags:breadcrumb>
	<div class="content">
		<div class="widget">
			<div class="widget-header">
				<h2>
					<spring:message code="" text="Good Received receipt" />
				</h2>
				<apptags:helpDoc url="GoodReceiveReceiptForm.html"></apptags:helpDoc>
			</div>
			<div class="pagediv">
				<div class="widget-content padding">
					<div class="mand-label clearfix">
						<span><spring:message code="solid.waste.mand"
								text="Field with"></spring:message><i class="text-red-1">*</i> <spring:message
								code="solid.waste.mand.field" text="is mandatory"></spring:message>
						</span>
					</div>

					<form:form id="indAddFrm" name="indentProcessForm"
						class="form-horizontal" action="IndentProcess.html" method="post">
						<jsp:include page="/jsp/tiles/validationerror.jsp" />
						<div class="error-div alert alert-danger alert-dismissible"
							id="errorDiv" style="display: none;">
							<button type="button" class="close" onclick="closeOutErrBox()"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span id="errorId"></span>
						</div>

						<div class="panel-group accordion-toggle"
							id="accordion_single_collapse">
							<div class="panel panel-default">
								<h4 class="panel-title">
									<a data-target="#a1" data-toggle="collapse" class="collapsed"
										data-parent="#accordion_single_collapse" href="#a1"><spring:message
											code="department.indent.indentor.details"
											text="Indenter Details" /></a>
								</h4>
								<div id="a1" class="panel-collapse collapse in">
									<div class="panel-body">

										<div class="form-group">

											<label class="control-label col-sm-2" for="Vendor Name"><spring:message
													code="purchase.requisition.vendorName" text="Vendor Name" /></label>
											<div class="col-sm-4">
												<form:select path="goodreceivedDto.vendorId"
													cssClass="form-control chosen-select-no-results"
													id="vendorName" data-rule-required="true">
													<form:option value="">
														<spring:message code="material.management.select"
															text="Select" />
													</form:option>
													<c:forEach items="${command.vendors}" var="vendor">
														<form:option value="${vendor.vmVendorid}" code="">${vendor.vmVendorname}</form:option>
													</c:forEach>
												</form:select>
											</div>

											<apptags:input labelCode="department.goodsReceived.orderNo"
												path="goodreceivedDto.requisitionNo" isMandatory="true"
												isDisabled="false" />
										</div>

									</div>
								</div>
							</div>
						</div>



						<div class="text-center padding-bottom-10">
							<button type="button" class="btn btn-blue-2 search"
								onclick="searchGoodsReceivedReceiptData(this);">
								<i class="fa fa-search"></i>
								<spring:message code="material.management.search" text="Search"></spring:message>
							</button>

							<%-- <button type="button" class="btn btn-warning"
								onclick="window.location.href='GoodReceiveReceiptForm.html'">
								<i class="fa fa-refresh" aria-hidden="true"></i>
								<spring:message code="material.management.reset" text="Reset"></spring:message>
							</button> --%>

							<button type="submit" class="button-input btn btn-success"
								name="button-Add" id="button-submit"
								onclick="addGoodReceiveForm('GoodReceiveReceiptForm.html','addGoodReceivedNote');">
								<strong class="fa fa-plus-circle"></strong>
								<spring:message code="material.management.add" text="Add" />
							</button>
						</div>

						<div class="table-responsive">
							<table id="id_itemOpeningBalance" summary="Store Master Data"
								class="table table-bordered table-striped rcm">
								<thead>
									<tr>
										<th><spring:message code="store.master.srno"
												text="Sr.No." /></th>
										<th><spring:message
												code="purchase.requisition.vendorName" text="Vendor Name" /></th>
										<th><spring:message code="material.mgmt.RequisitionNo"
												text="Requisition No." /></th>
										<th><spring:message code="department.indent.date"
												text="Goods Received Receipt Date" /></th>
										<th width="150"><spring:message
												code="material.management.action" text="Action" /></th>

									</tr>
								</thead>
								<tbody>
									<c:forEach items="${command.goodsReceivedReceiptDto}"
										var="data" varStatus="loop">
										<tr>
											<td class="text-center"><c:out value="${loop.index + 1}"></c:out></td>
											<td class="text-center">${data.vendorName}</td>
											<td class="text-center">${data.requisitionNo}</td>
											<td class="text-center">${data.receivedDate}</td>

											<td class="text-center">
												<button type="button" class="btn btn-blue-2 btn-sm"
												onClick="viewGoodsReceivedReceipt('GoodReceiveReceiptForm.html','viewGoodsReceivedReceiptForm','${data.requisitionNo}')"
													title="<spring:message code="material.management.view" text="View" />">
													<i class="fa fa-eye"></i>
												</button></td>										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>