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
<script type="text/javascript" src="js/material_mgmt/service/purRequisition.js"></script>
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
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="purchase.requisition.purOrder" text="Purchase Order" />
			</h2>
			<apptags:helpDoc url="PurchaseRequisition.html" />
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="material.management.mand"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="material.management.mand.field" text="is mandatory" /> </span>
			</div>
			<form:form action="PurchaseRequisition.html" name="PurchaseRequisition" id="PurchaseRequisitionId" class="form-horizontal">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>
				<div class="form-group">
					<apptags:input labelCode="purchase.requisition.purOrderNo"
						path="purchaseOrderDto.poNo" isMandatory="true"
						cssClass="form control mandcolour" isReadonly="false">
					</apptags:input>

					<apptags:input labelCode="purchase.requisition.purOrderDate"
						cssClass="fromDateClass purDate"
						path="purchaseOrderDto.poDate" isReadonly="false"
						isMandatory="" />
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" for="Store Name"><spring:message
							code="store.master.name" text="Store Name" /></label>
					<div class="col-sm-4">
						<form:select path="purchaseOrderDto.storeId"
							cssClass="form-control   mandColorClass" id="storeNameId"
							data-rule-required="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<c:forEach items="${command.storeNameList}" var="storeData">
								<form:option value="${storeData.storeId}" code="">${storeData.storeName}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<apptags:input labelCode="purchase.requisition.wordOrderNo"
						path="purchaseOrderDto.workOrderId" isMandatory="true"
						cssClass="form control mandcolour" isReadonly="false">
					</apptags:input>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" for="Vendor Name"><spring:message
							code="purchase.requisition.vendorName" text="Vendor Name" /></label>
					<div class="col-sm-4">
						<form:select path="purchaseOrderDto.vendorId"
							cssClass="form-control   mandColorClass" id="storeNameId"
							data-rule-required="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							
							<c:forEach items="${command.vendorCodeMap}" var="entry">
								<form:option value="${entry.key}" code="">${entry.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<apptags:input labelCode="purchase.requisition.expectDeliveryDate"
						cssClass="fromDateClass expectedDate"
						path="purchaseOrderDto.expectedDeliveryDate" isReadonly="false"
						isMandatory="" />
				</div>
				<h4>
					<spring:message code="store.master.details1" text="Purchase Requisition Details" />
				</h4>
				<div class="form-group">
					<label class="control-label col-sm-2" for="PR Number"><spring:message
							code="" text="PR Number" /></label>
					<div class="col-sm-4">
						<form:select path="purchaseRequistionDto.prNo"
							cssClass="form-control   mandColorClass" id="storeNameId"
							data-rule-required="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<c:forEach items="${command.storeNameList}" var="storeData">
								<form:option value="${storeData.storeId}" code="">${storeData.storeName}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<button type="button" class="btn btn-success add"
						onclick="searchPurchaseRequisition(this);">
						<i class="fa fa-plus-circle"></i>
						<spring:message code="" text="purchase.requisition.getItemDetails"></spring:message>
					</button>
				</div>
				<div class="panel-default">
					<div class="panel-collapse collapse in" id="UnitDetail">
						<div class=" clear padding-10">
							<c:set var="d" value="0" scope="page" />
							<table id="unitDetailTable"
								class="table table-striped table-bordered appendableClass unitDetail">
								<thead>
									<tr>
										<th><spring:message code="store.master.srno" text="Sr.No." /></th>
										<th><spring:message code="purchase.requisition.prno" text="PR.No" /></th>
										<th><spring:message code="purchase.requisition.prDate" text="PR Date" /></th>
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
												value="${d+1}" />
										<td></td>
										<td></td>
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
										<th><spring:message code="store.master.srno" text="Item" /></th>
										<th><spring:message code="store.master.uom" text="Uom" /></th>
										<th><spring:message code="store.master.quantity" text="Quantity" /></th>
										<th><spring:message code="purchase.requisition.unitPrice" text="Unit Price" /></th>
										<th><spring:message code="purchase.requisition.txAmount" text="Tax Amount" /></th>
										<th><spring:message code="purchase.requisition.totValue" text="Total Value" /></th>
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
												value="${d+1}"  />
										<td style="width: 20%">
											<div class="col-sm-4" class="text-center">
												<form:select
													path="purchaseOrderDto.purchaseOrderDetDto[${d}].itemId"
													cssClass="form-control   mandColorClass" id="itemId${d}"
													data-rule-required="true" style="width:400%"
													>
													<form:option value="">
														<spring:message code="material.management.select"
															text="Select" />
													</form:option>
													<c:forEach items="${command.itemNameList}" var="itemname">
													  <form:option value="${itemname.itemId}" code="${itemname.uom}" >${itemname.name}</form:option>
													</c:forEach>
												</form:select>
											</div>
										</td>
										<td><form:input name="name"
												path="purchaseOrderDto.PurchaseOrderDetDto[${d}].uonName"
												id="uomId${d}" type="text" class="form-control"
												disabled="true"></form:input></td>
										<td><form:input name="name"
												path="purchaseOrderDto.PurchaseOrderDetDto[${d}].quantity"
												id="quantity${d}" type="text" class="form-control"></form:input></td>
										<td><form:input name="name"
												path="purchaseOrderDto.PurchaseOrderDetDto[${d}].unitPrice"
												id="taxId${d}" type="text" class="form-control"
												></form:input></td>

										<td><form:input name="name"
												path="purchaseOrderDto.PurchaseOrderDetDto[${d}].tax"
												id="taxId${d}" type="text" class="form-control"
												></form:input></td>
										<td><form:input name="name"
												path="purchaseOrderDto.PurchaseOrderDetDto[${d}].totalAmt"
												id="taxId${d}" type="text" class="form-control"
												></form:input></td>
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
							<div class="form-group">
								<apptags:input labelCode="purchase.requisition.totItemAmount"
									path="purchaseRequistionDto.departmentName" isMandatory="true"
									cssClass="form control mandcolour" isReadonly="false">
								</apptags:input>
							</div>
						</div>
					</div>
				</div>
				<h4>
					<spring:message code="purchase.requisition.overheads" text="Overheads" />
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
										<th><spring:message code="purchase.requisition.overHeads1" text="Over Heads" /></th>
										<th><spring:message code="purchase.requisition.addDeduct" text="Add/Deduct" /></th>
										<th><spring:message code="purchase.requisition.Amt" text="Amount" /></th>
										<th><a href="javascript:void(0);" data-toggle="tooltip"
											data-original-title="Add"
											class="addPurReq btn btn-success btn-sm unit" id="addUnitRow"><i
												class="fa fa-plus-circle"></i> </a></th>

									</tr>
								</thead>
								<tbody>
									<tr class="firstUnitRow">
										<td align="center" width="5%"><form:input path=""
												cssClass="form-control mandColorClass " id="sequence${d}"
												value="${d+1}"  />
										<td width="55%"><form:input name="name"
												path="purchaseOrderDto.purchaseOrderOverheadsDto[${d}].description"
												id="uomId${d}" type="text" class="form-control"
												></form:input></td>
										<td width="20%">
											<div class="col-sm-4" class="text-center">
												<form:select
													path="purchaseOrderDto.purchaseOrderOverheadsDto[${d}].overHeadType"
													cssClass="form-control   mandColorClass" id="itemId${d}"
													data-rule-required="true" style="width:400%"
													>
													<form:option value="">
														<spring:message code="material.management.select"
															text="Select" />
													</form:option>
													<c:forEach items="${command.itemNameList}" var="itemname">
														<form:option value="${itemname.itemId}"
															code="${itemname.uom}">${itemname.name}</form:option>
													</c:forEach>
												</form:select>
											</div>
										</td>
										<td width="15%"><form:input name="name"
												path="purchaseOrderDto.purchaseOrderOverheadsDto[${d}].amount"
												id="quantity${d}" type="text" class="form-control"></form:input></td>

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
							<div class="form-group">
								<apptags:input labelCode="purchase.requisition.totItemAmount"
									path="purchaseRequistionDto.departmentName" isMandatory="true"
									cssClass="form control mandcolour" isReadonly="false">
								</apptags:input><br>
								<apptags:input labelCode="purchase.requisition.totItemAmount"
									path="purchaseRequistionDto.departmentName" isMandatory="true"
									cssClass="form control mandcolour" isReadonly="false">
								</apptags:input>
							</div>
						</div>
					</div>
				</div>

				<div class="panel-default">
					<div class="panel-collapse collapse in" id="UnitDetail">
						<div class=" clear padding-10">
							<c:set var="d" value="0" scope="page" />
							<table id="unitDetailTable"
								class="table table-striped table-bordered appendableClass unitDetail">
								<thead>
									<tr>
										<th><spring:message code="store.master.srno" text="Sr.No." /></th>
										<th><spring:message code="purchase.requisition.termNCondition" text="Term & Condition" /></th>
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
												value="${d+1}"  />
										<td><form:input name="name"
												path="purchaseOrderDto.purchaseorderTncDto[${d}].description"
												id="uomId${d}" type="text" class="form-control"
												></form:input></td>
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
				<h4>
					<spring:message code="purchase.requisition.enclosure" text="Enclosure" />
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
										<th><spring:message code="material.item.master.description" text="Description" /></th>
										<th><spring:message code="purchase.requisition.uploadDoc" text="Upload Document" /></th>
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
												value="${d+1}" />
										<td><form:input name="name"
												path="purchaseOrderDto.purchaseorderAttachmentDto[${d}].description"
												id="uomId${d}" type="text" class="form-control"
												></form:input></td>
										<td width="20%" class="text-center">
										<apptags:formField fieldType="7" fieldPath=""
											showFileNameHTMLId="true" fileSize="WORK_COMMON_MAX_SIZE"
											isMandatory="false" maxFileCount="CHECKLIST_MAX_UPLOAD_COUNT"
											validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
											currentCount="0">
										</apptags:formField></td>
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
				<div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success btn-submit"
						onclick="savePurchaseOrderForm(this);">
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

