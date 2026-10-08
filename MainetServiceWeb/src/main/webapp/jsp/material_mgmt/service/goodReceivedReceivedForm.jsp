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
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript"
	src="js/material_mgmt/service/indentProcess.js"></script>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<style>
.input-group-field {
	display: table-cell;
	width: 114px;
}
</style>

<script>
$(document).ready(function () {
    // Destroy the datepicker for both fields individually
    $('#receivedReceiptDate').datepicker("destroy");
    $('#challanDate').datepicker("destroy");

    // Use event delegation to handle dynamically added elements
    $(document).on('focus', '#receivedReceiptDate, #challanDate', function () {
        if (!$(this).hasClass('hasDatepicker')) {
            // Common options
            const options = {
                dateFormat: 'dd/mm/yy',
                changeMonth: true,
                changeYear: true,
                yearRange: "1900:2200",
                maxDate: 0
            };

            if (this.id === "receivedReceiptDate") {
                // Standard config for receivedReceiptDate
                $(this).datepicker(options);
            } else if (this.id === "challanDate") {
                // Custom config for challanDate to allow only today and yesterday
                const today = new Date();
                const yesterday = new Date();
                yesterday.setDate(today.getDate() - 1);

                $(this).datepicker({
                    ...options,
                    beforeShowDay: function (date) {
                        const d = date.getDate();
                        const m = date.getMonth();
                        const y = date.getFullYear();

                        const isToday =
                            d === today.getDate() &&
                            m === today.getMonth() &&
                            y === today.getFullYear();

                        const isYesterday =
                            d === yesterday.getDate() &&
                            m === yesterday.getMonth() &&
                            y === yesterday.getFullYear();

                        if (isToday || isYesterday) {
                            return [true, '']; 
                        } else {
                            return [false, '']; 
                        }
                    }
                });
            }
        }
    });
});

 
</script>
<!-- Start Content here -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start info box -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>

				<spring:message code="material.goodReceived.receiptForm"
					text="Good Received Receipt" />

			</h2>
			<apptags:helpDoc url="GoodReceiveReceiptForm.html" />
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="material.management.mand"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="material.management.mand.field" text="is mandatory" /> </span>
			</div>


			<form:form action="GoodReceiveReceiptForm.html"
				name="goodsReceivedReceiptForm" id="goodsReceipt" method="POST"
				class="form-horizontal" commandName="command">

				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>






				<!--------------------------------- Issuing Store Details --------------------------------------------------->
				<c:if test="${command.saveMode ne 'V'}">
					<div class="form-group">

						<apptags:date fieldclass="datepicker"
							labelCode="department.goodsOrder.date"
							datePath="goodreceivedDto.receivedReceiptDate" isMandatory="true"
							readonly="false" cssClass=" mandColorClass recptDate">
						</apptags:date>


						<apptags:input labelCode="department.goodsReceived.orderNo"
							path="goodreceivedDto.requisitionNo"  isMandatory="true"
							isDisabled="false" />
					</div>
				</c:if>

				<c:if test="${command.saveMode eq 'V'}">
					<div class="form-group">

						<apptags:date fieldclass="datepicker"
							labelCode="department.goodsOrder.date"
							datePath="goodreceivedDto.receivedDate" isMandatory="true"
							readonly="true" cssClass="mandColorClass recptDate">
						</apptags:date>


						<apptags:input labelCode="department.goodsReceived.orderNo"
							path="goodreceivedDto.requisitionNo" isMandatory="true"
							isDisabled="true" />
					</div>

				</c:if>

				<c:if test="${command.saveMode ne 'V'}">

					<div class="form-group">

						<label class="control-label col-sm-2" for="Vendor Name"><spring:message
								code="purchase.requisition.vendorName" text="Vendor Name" /></label>
						<div class="col-sm-4">
							<form:select path="goodreceivedDto.vendorId"
								cssClass="form-control chosen-select-no-results" id="vendorName"
								data-rule-required="true" readonly="">
								<form:option value="">
									<spring:message code="material.management.select" text="Select" />
								</form:option>
								<c:forEach items="${command.vendors}" var="vendor">
									<form:option value="${vendor.vmVendorid}" code="">${vendor.vmVendorname}</form:option>
								</c:forEach>
							</form:select>
						</div>

						<%-- <label class="control-label col-sm-2 required-control"><spring:message
								code="purchase.requisition.bill.remarks" text='Remarks' /></label>
						<div class="col-sm-4">
							<form:input type="text" path="goodreceivedDto.remarks"
								id="remark" onkeypress="" cssClass="form-control" readonly="" />
						</div> --%>
						
						<label class="control-label col-sm-2 required-control"><spring:message
							code="store.master.name" text="Store Name" /></label>
					<div class="col-sm-4">
						<form:select path="goodreceivedDto.storeId" id="storeId" onchange="getGoodsReceiptItemGroupsNames()"
							class="form-control chosen-select-no-results" disabled="">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<c:forEach items="${command.storeObjectList}" var="storeObject">
								<form:option value="${storeObject[0]}" code="${storeObject[2]}">${storeObject[1]}</form:option>
							</c:forEach>
						</form:select>
					</div>

					</div>
				</c:if>

				<c:if test="${command.saveMode ne 'V'}">

					<div class="form-group">

						<apptags:date fieldclass="datepicker"
							labelCode="department.goodsReceived.challanDate"
							datePath="goodreceivedDto.challanDate" isMandatory="true"
							readonly="false" cssClass="mandColorClass recptDate">
						</apptags:date>

						<apptags:input labelCode="department.goodsReceived.challanNo"
							path="goodreceivedDto.challanNo" isMandatory="true"
							isDisabled="false" />
					</div>


				</c:if>

				<c:if test="${command.saveMode eq 'V'}">

					<div class="form-group">
					
						<apptags:input labelCode="purchase.requisition.vendorName"
							path="goodreceivedDto.vendorName" isMandatory="true"
							isDisabled="true" />

						<%-- <label class="control-label col-sm-2 required-control"><spring:message
								code="purchase.requisition.bill.remarks" text='Remarks' /></label>
						<div class="col-sm-4">
							<form:input type="text" path="goodreceivedDto.remarks"
								id="remark" onkeypress="" cssClass="form-control"
								readonly="true" />
						</div> --%>

						
						<apptags:input labelCode="store.master.name"
							path="goodreceivedDto.storeName" isMandatory="true"
							isDisabled="true" />

					</div>
				</c:if>
				
				<c:if test="${command.saveMode eq 'V'}">

					<div class="form-group">

						<apptags:date fieldclass="datepicker"
							labelCode="department.goodsReceived.challanDate"
							datePath="goodreceivedDto.challanDate" isMandatory="true"
							readonly="true" cssClass=" mandColorClass recptDate">
						</apptags:date>

						<apptags:input labelCode="department.goodsReceived.challanNo"
							path="goodreceivedDto.challanNo" isMandatory="true"
							isDisabled="true" />
					</div>


				</c:if>




				<!-- <br> -->
				<div id="initialDataTable">

					<c:set var="d" value="0" scope="page"></c:set>
					<table class="table table-bordered margin-bottom-10"
						id="initialTable">

						<thead>
							<tr>
								<th width=5% class="text-center"><spring:message
										code="store.master.srno" text="Sr.No." /></th>
								
								<th width=15% class="text-center"><spring:message
										code="department.indent.item.name" text="Item Name" /><i
									class="text-red-1">*</i></th>
									<th width=15% class="text-center"><spring:message
										code="material.item.master.group" text="Group" /><i
									class="text-red-1">*</i></th>
								<th width=10% class="text-center"><spring:message
										code="material.management.UoM" text="UoM" /></th>

								<th width=15% class="text-center"><spring:message
										code="binLocMasDto.binLocation" text="Bin Location" /></th>

								<th width=10% class="text-center"><spring:message
										code="material.mgmt.receivedQuantity" text="Received Quantity" /><i
									class="text-red-1">*</i></th>

								<th width=10% class="text-center"><spring:message
										code="material.item.unitPrice" text="Unit Price" /><i
									class="text-red-1">*</i></th>
								<th width=10% class="text-center"><spring:message
										code="purchase.requisition.totValue" text="Total Value" /><i
									class="text-red-1">*</i></th>

								<c:if test="${command.saveMode ne 'V'}">
									<th width="10%" class="text-center"><spring:message
											code="store.master.action" text="Action" /></th>
								</c:if>
							</tr>
						</thead>
						<c:if test="${command.saveMode ne 'V'}">
							<tbody>
								<tr class="withHeld">
									<td><form:input path="" id="sNo${d}" value="${d + 1}"
											readonly="true" cssClass="form-control text-center" /></td>
							



									<td><form:select
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].itemId"
											id="itmId${d}"
											class="form-control mandColorClass chosen-select-no-results"
											onchange="getGoodReceiptUom(${d})">
											<form:option value="">
												<spring:message code="material.management.select"
													text="Select" />
											</form:option>
											<c:forEach items="${findGoodsItemNames}" var="item">
												<form:option value="${item.key}">${item.value}</form:option>
											</c:forEach>
										</form:select></td>
										

									<td><input type="text" class="form-control"
										id="itemGroupName${d}" readonly="true" />
										
										 <form:input
											type="hidden" id="itemGroupId${d}"
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].itemGroup" />
									</td>



									<td><form:input
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].uom"
											class="form-control hasNameClass valid" id="uom${d}"
											readonly="true" /></td>

									<td><form:input
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].locationName"
											class="form-control hasNameClass valid"
											id="binLocationName${d}" readonly="true" /></td>


									<td><form:input
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].receQuantity"
											id="receivedQuantity${d}"
											class="form-control text-right receivedQuantity"
											placeholder="" /></td>

									<td><form:input
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].unPrice"
											id="unitPrice${d}" class="form-control text-right unitPrice"
											placeholder="" onBlur="calculateTotalValue(${d})" /></td>

									<td><form:input
											path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].totalValue"
											id="totalValue${d}"
											class="form-control text-right totalValue" readonly="true" />
									</td>


									<td class="text-center"><a href="javascript:void(0);"
										onclick="addGoodReceivedIndentDetailsWith(this);"
										class="btn btn-success addtable btn-sm"
										title="<spring:message code="material.management.add" text="Add" />">
											<i class="fa fa-plus-circle"> </i>
									</a> <a href="javascript:void(0);"
										class="btn btn-danger btn-sm delButton"
										onclick="deleteEntry($(this));"
										title="<spring:message code="material.management.delete" text="Delete" />">
											<i class="fa fa-trash-o"></i>
									</a></td>
								</tr>
								<c:set var="d" value="${d + 1}" scope="page" />
							</tbody>
						</c:if>

						<c:if test="${command.saveMode eq 'V'}">

							<tbody>
								<c:forEach var="d" begin="0" end="${command.fileSize - 1}">
									<tr class="withHeld">
										<td><form:input path="" id="sNo${d}" value="${d + 1}"
												readonly="true" cssClass="form-control text-center" /></td>

										<%-- <td><form:select
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].itemGroup"
												cssClass="form-control chosen-select-no-results mandColorClass"
												id="itemGroupId${d}" data-rule-required="true"
												onchange="getItemNames(${d})" disabled="true">
												<form:option value="0">
													<spring:message code='material.management.select'
														text="Select" />
												</form:option>
												<c:forEach items="${command.usageType}" var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">
                                                        ${lookUp.lookUpDesc}
                                                  </form:option>
												</c:forEach>
											</form:select></td> --%>

                                         

										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].itemName"
												class="form-control hasNameClass valid" id="itmId${d}"
												readonly="true" /></td>

										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].itemGroupName"
												class="form-control hasNameClass valid" id="itemGroupId${d}"
												readonly="true" /></td>


										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].uom"
												class="form-control hasNameClass valid" id="uom${d}"
												readonly="true" /></td>

										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].locationName"
												class="form-control hasNameClass valid"
												id="binLocationName${d}" readonly="true" /></td>



										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].receQuantity"
												id="receivedQuantity${d}" class="form-control text-right"
												placeholder="" readonly="true" /></td>

										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].unPrice"
												id="unitPrice${d}" class="form-control text-right"
												placeholder="" onBlur="calculateTotalValue(${d})"
												readonly="true" /></td>

										<td><form:input
												path="goodreceivedDto.goodsReceivedReceiptDetDto[${d}].totalValue"
												id="totalValue${d}" class="form-control text-right"
												readonly="true" /></td>

										<c:if test="${command.saveMode ne 'V'}">
											<td class="text-center"><a href="javascript:void(0);"
												onclick="addGoodReceivedIndentDetailsWith(this);"
												class="btn btn-success addtable btn-sm" readonly="true"
												title="<spring:message code="material.management.add" text="Add"  />">
													<i class="fa fa-plus-circle"> </i>
											</a> <a href="javascript:void(0);"
												class="btn btn-danger btn-sm delButton" readonly="true"
												onclick="deleteEntry($(this));"
												title="<spring:message code="material.management.delete" text="Delete" />">
													<i class="fa fa-trash-o"></i>
											</a></td>
										</c:if>


									</tr>
								</c:forEach>
							</tbody>

						</c:if>

					</table>

				</div>


				<br />
				<c:if test="${command.saveMode ne 'V'}">
					<apptags:CheckerAction hideComments="true" hideDecision="true"
						hideApprove="true" hideReject="true" hideForward="true"
						hideSendback="true"></apptags:CheckerAction>
				</c:if>

				<c:if test="${command.saveMode eq 'V'}">

					<div class=" form-group ">
						<div class=" col-sm-12 text-center ">
							<c:if test="${! command.attachDocsList.isEmpty()}">
								<div class="table-responsive">
									<table class="table table-bordered margin-bottom-10"
										id="deleteDoc">
										<tr>
											<th class="text-center" width="15%"><spring:message
													code="population.master.srno" text="Sr. No." /></th>
											<th><spring:message code="scheme.view.document" text="" /></th>
										</tr>
										<c:set var="e" value="0" scope="page" />
										<c:forEach items="${command.attachDocsList}" var="lookUp">
											<tr>
												<td class="text-center">${e+1}</td>
												<td class="text-center"><apptags:filedownload
														filename="${lookUp.attFname}" filePath="${lookUp.attPath}"
														actionUrl="GoodReceiveReceiptForm.html?Download" /></td>
											</tr>
											<c:set var="e" value="${e + 1}" scope="page" />
										</c:forEach>
									</table>
								</div>
							</c:if>
						</div>
					</div>
				</c:if>


				<div class="text-center clear padding-10">
					<c:if test="${command.saveMode ne 'V'}">
						<button type="button" class="btn btn-success btn-submit"
							title="Submit" onclick="submitGoodsReceivedForm(this)">
							<spring:message code="material.management.submit" text="Submit" />
						</button>
						<button type="button" class="btn btn-warning" title="Reset"
							onclick="addGoodReceiveForm('GoodReceiveReceiptForm.html','addGoodReceivedNote');">
							<spring:message code="material.management.reset" text="Reset" />
						</button>
					</c:if>
					<c:if test="${command.saveMode eq 'V'}">
						<apptags:backButton url="GoodReceiveReceiptForm.html"></apptags:backButton>
					</c:if>
					<c:if test="${command.saveMode ne 'V'}">
						<apptags:backButton url="GoodReceiveReceiptForm.html"></apptags:backButton>
					</c:if>
				</div>

				<%-- <form:hidden path="dispatchNoteDTO.issueStoreId" id="issueStoreId" /> --%>










				<!--------------------------------- Item Details --------------------------------------------------->



				<!-------------------------------------- Buttons --------------------------------------------->


			</form:form>
		</div>
	</div>
</div>
