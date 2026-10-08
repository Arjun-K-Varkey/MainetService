<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript"
	src="js/material_mgmt/service/purRequisition.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<%
	response.setContentType("text/html; charset=utf-8");
%>

<style>
.input-group-field {
	display: table-cell;
	width: 114px;
}
</style>
<!-- Start Content here -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start info box -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="purchase.requisition.purReqApprovalForm"
					text="Purchase Requisition Approval Form" />
			</h2>
			<apptags:helpDoc url="PurchaseRequisitionApproval.html" />
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="material.management.mand"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="material.management.mand.field" text="is mandatory" /> </span>
			</div>
			<form:form action="PurchaseRequisitionApproval.html"
				name="PurchaseRequisitionAppForm" id="PurchaseRequisitionAppId"
				class="form-horizontal">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>
				<h4>
					<spring:message code="store.master.details1"
						text="Purchase Requisition Details" />
				</h4>
				<div class="form-group">
					<label class="control-label col-sm-2" for="Store Name"><spring:message
							code="store.master.name" text="Store Name" /></label>
					<div class="col-sm-4">
						<form:select path="purchaseRequistionDto.storeId"
							onchange="getStoreData()"
							cssClass="form-control   mandColorClass" id="storeNameId"
							data-rule-required="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<%-- 	<c:forEach items="${command.storeNameList}" var="storeData">
								<form:option value="${storeData.storeId}" code="">${storeData.storeName}</form:option>
							</c:forEach> --%>
						</form:select>
					</div>
					<apptags:date labelCode="purchase.requisition.reqDate"
						fieldclass="fromDateClass" datePath="purchaseRequistionDto.prDate"
						cssClass="fromDateClass fromDate" isMandatory="true">
					</apptags:date>
				</div>
				<div class="form-group">
					<apptags:input labelCode="store.master.store.incharge"
						path="purchaseRequistionDto.departmentName" isMandatory="true"
						cssClass="form control mandcolour" isReadonly="false">
					</apptags:input>
					<apptags:input labelCode="store.master.location"
						path="purchaseRequistionDto.requestedName" isMandatory="true"
						cssClass="form control mandcolour hasNameClass" isReadonly="true">
					</apptags:input>
				</div>
				<h4>
					<spring:message code="material.item.master.itemDetails" text="Item Details" />
				</h4>
				<div class="panel-default">
					<div class="panel-collapse collapse in" id="UnitDetail">
						<div class=" clear padding-10">
							<c:set var="d" value="0" scope="page" />
							<table id="unitDetailTable"
								class="table table-striped table-bordered appendableClass unitDetail">
								<thead>
									<tr>
										<th><spring:message code="store.master.srno" text="Sr.No." /></th>
										<th><spring:message code="store.master.item" text="Item" /></th>
										<th><spring:message code="store.master.uom" text="Uom" /></th>
										<th><spring:message code="store.master.quantity" text="Quantity" /></th>
										<th><spring:message code="material.item.master.tax" text="Tax" /></th>
										<th><a href="javascript:void(0);" data-toggle="tooltip"
											data-original-title="Add"
											class="addPurReq btn btn-success btn-sm unit" id="addUnitRow"><i
												class="fa fa-plus-circle"></i> </a></th>

									</tr>
								</thead>
								<tbody>
									<tr class="firstUnitRow">
										<td align="center" width="10%"><form:input path=""
												cssClass="form-control mandColorClass " id="sequence${d}"
												value="${d+1}" disabled="true" />
										<td style="width: 20%">
											<div class="col-sm-4" class="text-center">
												<form:select
													path="purchaseRequistionDto.purchaseRequistionDetDtoList[${d}].itemId"
													cssClass="form-control   mandColorClass" id="itemId${d}"
													data-rule-required="true" style="width:400%"
													onchange="getUomName()">
													<form:option value="">
														<spring:message code="material.management.select"
															text="Select" />
													</form:option>
													<%-- <c:forEach items="${command.itemNameList}" var="itemname">
													  <form:option value="${itemname.itemId}" code="${itemname.uom}" >${itemname.name}</form:option>
													</c:forEach> --%>
												</form:select>
											</div>
										</td>
										<td><form:input name="name"
												path="purchaseRequistionDto.purchaseRequistionDetDtoList[${d}].uonName"
												id="uomId${d}" type="text" class="form-control"
												disabled="true"></form:input></td>
										<td><form:input name="name"
												path="purchaseRequistionDto.purchaseRequistionDetDtoList[${d}].quantity"
												id="quantity${d}" type="text" class="form-control"></form:input></td>
										<td><form:input name="name"
												path="purchaseRequistionDto.purchaseRequistionDetDtoList[${d}].userId"
												id="taxId${d}" type="text" class="form-control"
												disabled="true"></form:input></td>
										<td class="text-center" width="10%"><a href="#"
											data-toggle="tooltip" data-placement="top"
											class="btn btn-danger btn-sm"
											onclick="deleteEntry($(this),'removedIds');"> <strong
												class="fa fa-trash"></strong> <span class="hide"><spring:message
														code="material.management.delete" text="Delete" /></span>
										</a></td>
									</tr>
									<c:set var="d" value="${d + 1}" scope="page" />
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="form-group" id="authMode">
					<label for="" class="col-sm-2 control-label required-control"><spring:message
							code="purchase.requisition.final.decision" text="Final Decision" /></label>
					<div class="col-sm-4">
						<label class="radio-inline"><form:radiobutton
								checked="checked" path=""
								name="checkerAuthorization" cssClass="set-radio" value="Y" /> <spring:message
								code="purchase.requisition.sanctioned" text="Sanctioned" /></label> <label class="radio-inline"><form:radiobutton
								path="" name="checkerAuthorization"
								cssClass="set-radio" value="R" /> <spring:message code="purchase.requisition.rejected"
								text="Rejected" /></label>
					</div>
				</div>
				<div class="form-group">
					<label for="checkerRemarks"
						class="col-sm-2 control-label required-control"><spring:message
							code="purchase.requisition.bill.remarks" text="Remarks" /></label>
					<div class="col-sm-4">
						<form:textarea id="checkerRemarks" path=""
							disabled="" class="form-control" />
					</div>
				</div>
				<div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success btn-submit"
						onclick="savePurchaseRequisitionForm(this);">
						<spring:message code="material.management.submit" text="Submit"></spring:message>
					</button>
					<button type="button" class="btn btn-warning"
						onclick="resetPurReqForm();">
						<spring:message code="material.management.reset" text="Reset"></spring:message>
					</button>
					<button type="button" class="button-input btn btn-danger"
						name="button-Cancel" value="Cancel" style=""
						onclick="backPurReqForm();" id="button-Cancel">
						<spring:message code="material.management.back" text="Back" />
					</button>
				</div>
			</form:form>
		</div>
	</div>
</div>

