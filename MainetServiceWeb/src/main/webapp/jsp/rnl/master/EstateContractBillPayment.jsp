<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>

<script type="text/javascript"
	src="js/rnl/master/estateContractBillPayment.js"></script>

<script>
$(function() {						
	
	$("#fromDateId").datepicker({
	dateFormat : 'dd/mm/yy',
	changeMonth : true,
	changeYear : true,
	maxDate : '0'
});
$("#fromDateId").datepicker('setDate', new Date());
$('#fromDateId').change(function() {
	var check = $(this).val();
	if (check == '') {
		$(this).parent().switchClass("has-success", "has-error");
	} else {
		$(this).parent().switchClass("has-error", "has-success");
	}
});

$("#fromDateId").keyup(function(e) {
	if (e.keyCode != 8) {
		if ($(this).val().length == 2) {
			$(this).val($(this).val() + "/");
		} else if ($(this).val().length == 5) {
			$(this).val($(this).val() + "/");
		}
	}
})

});
</script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<!-- Bill Payment changes as per TSCL #220467 -->
 
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="rnl.master.bill.coll" text="Bill Collection"></spring:message>
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a>
			</div>
		</div>
		<div class="widget-content padding">
			<form:form action="EstateContractBillPayment.html" method="post"
				class="form-horizontal" name="contractBillPayment"
				id="contractBillPayment">
				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				</div>
				<form:hidden path="" id="counterScheduleFlag" value="${command.counterScheduleFlag}" />
				<div class="form-group">
					<label class="col-sm-2 control-label "><spring:message
							code="rnl.bill.contract.number" text="Contract Number"></spring:message></label>
					<%-- <div class="col-sm-4">
						<form:input path="contractNo" cssClass="form-control"
							id="contractNo" data-rule-required="true" />
					</div> --%>
					<div class="col-sm-4">
						<form:select id="contractNo"
							cssClass="form-control chosen-select-no-results"
							class="form-control required-control" path="contractNo"
							onchange="getPropertyDetail(this)"
							disabled="${command.formFlag eq 'Y' }">
							<form:option value="">
								<spring:message code='rnl.master.select' />
							</form:option>
							<c:forEach items="${command.contractAgreementList}"
								var="contract">
								<form:option value="${contract.contNo}">${contract.contNo} - ${contract.contp2Name}</form:option>
							</c:forEach>
						</form:select>
					</div>

					<%-- <label class="col-sm-2 control-label"><spring:message
							code="estate.label.name" text="Estate Name" /></label>

					<div class="col-sm-4">
						<form:select path="estateName" id="estateName"
							onchange="getPropertyDetail(this)"
							class="form-control chosen-select-no-results"
							disabled="${command.formFlag eq 'Y' }">
							<option value=""><spring:message
									code='rnl.master.select' text="Select" /></option>
							<c:forEach items="${command.estateDetails}" var="objArray">
								<form:option value="${objArray[3]}" code="${objArray[2]}">${objArray[3]} - ${objArray[4]}</form:option>
							</c:forEach>
						</form:select>
					</div> --%>
					
					<label class="col-sm-2 control-label"><spring:message
							code="rnl.bill.property.number" text="Property Number" /></label>
					<div class="col-sm-4">
						<form:select path="propertyContractNo" id="propertyNo"
							class="form-control chosen-select-no-results" onchange="getContractDetail(this)"
							disabled="${command.formFlag eq 'Y' }">
							<option value="0"><spring:message
									code='rnl.master.select' text="Select" /></option>
							<c:forEach items="${command.propertyDetails}" var="objvar">
								<form:option value="${objvar[3]}" code="${objvar[4]}">${objvar[3]} - ${objvar[4]}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>

				

				<%-- <div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="rnl.bill.property.number" text="Property Number" /></label>
					<div class="col-sm-4">
						<form:select path="propertyContractNo" id="propertyNo"
							class="form-control chosen-select-no-results"
							disabled="${command.formFlag eq 'Y' }">
							<option value="0"><spring:message
									code='rnl.master.select' text="Select" /></option>
							<c:forEach items="${command.propertyDetails}" var="objvar">
								<form:option value="${objvar[3]}" code="${objvar[4]}">${objvar[3]} - ${objvar[4]}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div> --%>

				<div class="form-group" align="center">
					<label><spring:message code="rnl.bill.or" text="OR" /></label>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="rnl.bill.mobile.no" text="Mobile No." /></label>
					<div class="col-sm-4">
						<form:input path="mobileNo" class="form-control hasMobileNo"
							id="mobileNo" maxlength="10"
							disabled="${command.formFlag eq 'Y' }" />
					</div>
				</div>
				
				<div class="form-group" align="center">
					<label><spring:message code="rnl.bill.or" text="OR" /></label>
				</div>
				
				<div class="form-group">

					<label class="col-sm-2 control-label"><spring:message
							code="rnl.bill.tds.details" text="Tds Details" /></label>


					<div class="col-sm-4">
						<form:select path="propertyContractNo" id="rnlTds"
							class="form-control chosen-select-no-results"
							onchange="getContractDetail(this)"
							disabled="${command.formFlag eq 'Y' }">
							<option value="0"><spring:message
									code='rnl.master.select' text="Select" /></option>
							<c:forEach items="${command.tdsDetailList}" var="objvar">
								<form:option value="${objvar[0]}" code="${objvar[1]}">${objvar[0]} - ${objvar[1]}</form:option>
							</c:forEach>
						</form:select>
					</div>

				</div>


				<div class="text-center clear padding-10">
					<button type="button" class="btn btn-info" id="btnsearch">
						<i class="fa fa-search"></i>
						<spring:message code="rnl.master.search" text="Search"></spring:message>
					</button>
					<button type="button" class="btn btn-info" id="btnSearchTds" onclick="searchTdsDetails(this)">
						<i class="fa fa-search"></i>
						<spring:message code="rnl.bill.tds.pay" text="Pay Tds"></spring:message>
					</button>
					<button type="button" class="btn btn-warning"
						onclick="resetbillPayment(this)">
						<spring:message code="rnl.master.reset" text="Reset"></spring:message>
					</button>
				</div>

				<%-- <div class="table-responsive clear">
					<table class="table table-striped table-bordered" id="searchStore">
						<thead>
							<tr>
								<th align="center"><spring:message
										code="estate.table.column.srno" text="Sr. No." /></th>
								<th align="center"><spring:message
										code="rnl.bill.contract.number" text="Contract Number" /></th>
								<th align="center"><spring:message code="estate.label.name"
										text="Estate Name" /></th>
								<th align="center"><spring:message
										code="rnl.bill.property.number" text="Property Number" /></th>
								<th align="center"><spring:message code="rnl.notice.tenant"
										text="Tenant Name" /></th>
								<th align="center"><spring:message
										code="rl.property.label.action" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.listContractAgreementDTO}"
								var="summaryDTO" varStatus="index">

								<tr>
									<td width=5% class="text-center">${index.count}</td>
									<td width=15% align="center"><strong>${summaryDTO.contNo}</strong></td>
									<td width=20% align="center"><strong>${summaryDTO.estateName}</strong></td>
									<td width=20% align="center"><strong>${summaryDTO.propertyNo}</strong></td>
									<td width=20% align="center"><strong>${summaryDTO.contp2Name}</strong></td>
									<td width=20% class="text-center">
										<button type="button" class="btn btn-warning btn-sm"
											onclick="getBillDetailforAction('${summaryDTO.contNo}','${summaryDTO.propertyNo}')"
											title="<spring:message code="rnl.bill.pay.proceed" text="Proceed"></spring:message>">
											<i class="fa fa-caret-square-o-right"></i>
										</button>
									</td>
								</tr>

							</c:forEach>
						</tbody>
					</table>
				</div> --%>

				<c:if test="${command.formFlag eq 'Y'}">

					<div class="panel-group accordion-toggle"
						id="accordion_single_collapse">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse" href="#first"><spring:message
											code="rnl.master.contractHeader" text="Contract Details" /></a>
								</h4>
							</div>
							<div id="first" class="panel-collapse collapse in">
								<div class="panel-body">

									<div class="form-group">
										<label class="control-label col-sm-2" for="ContractDate"><spring:message
												code="rnl.master.contract.date" text="Contract Date" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contDate"
												class="form-control" readonly="true"></form:input>

										</div>
										<label class="control-label col-sm-2" for="Department"><spring:message
												code="rnl.master.complaint.department" text="Department" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contDept"
												class="form-control" readonly="true"></form:input>
										</div>
									</div>

									<div class="form-group">
										<label class="control-label col-sm-2" for="ContractDate"><spring:message
												code="rnl.master.represented.by" text="Represented By" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contp1Name"
												class="form-control" readonly="true"></form:input>
										</div>
										<label class="control-label col-sm-2" for="Department"><spring:message
												code="rnl.notice.tenant" text="Tenant Name" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contp2Name"
												class="form-control" readonly="true"></form:input>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-2" for="ContractDate"><spring:message
												code="rnl.master.contract.from.date"
												text="Contract From Date" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contFromDate"
												class="form-control" readonly="true"></form:input>
										</div>
										<label class="control-label col-sm-2" for="Department"><spring:message
												code="rnl.master.contract.to.date" text="Contract To Date" /></label>
										<div class="col-sm-4">
											<form:input path="contractAgreementSummaryDTO.contToDate"
												class="form-control" readonly="true"></form:input>
										</div>
									</div>


								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse" href="#second"><spring:message
											code="rnl.master.receipt.amt.det"
											text="Receipt Amount Details" /></a>
								</h4>
							</div>
							<div id="second" class="panel-collapse collapse in">
								<div class="panel-body">

									<c:choose>
										<c:when
											test="${'RL' eq command.contractAgreementSummaryDTO.contDeptSc }">
											<div class="table-responsive">

												<%-- <table class="table table-bordered table-condensed">

										<tr>
											<th><spring:message code=""
													text="Agreement Amount" /></th>
											<th><spring:message code=""
													text="Balance Amount" /></th>
											<th><spring:message code=""
													text="Overdue Amount" /></th>
											<th><spring:message code=""
													text="Payment Amount" /></th>
										</tr>
										<tr>
											<td><p class="text-center">${command.contractAgreementSummaryDTO.contAmount}</p></td>
											<td><p class="text-center">${command.contractAgreementSummaryDTO.balanceAmount}</p></td>
											<td><p class="text-center">${command.contractAgreementSummaryDTO.overdueAmount}</p></td>
											<td>
												<form:input placeholder="Enter Amt" id="inputAmountId"
												path="inputAmount" class=" form-control hasNumber "
												onchange="getTaxCharges(this)" maxlength="10"
												onblur="getAmountFormatInDynamic((this),'inputAmountId')" />
											</td>
										</tr>
									</table> --%>
												<table
													class="table table-bordered receipt-amount-details-table">
													<tbody>
														<%-- <tr ><spring:message code="" text="Demand Details" /></tr> --%>
														<tr>
															<th class="text-center" colspan="6"><spring:message
																	code="rnl.bill.demandDetails" text="Demand Details" /></th>
														</tr>
														<tr>
															<th><spring:message code="rnl.bill.description"
																	text="Description" /></th>
															<th><spring:message code="rnl.bill.rent" text="Rent" />
															</th>
															<th><spring:message code="rnl.bill.sgst" text="SGST" />
															</th>
															<th><spring:message code="rnl.bill.cgst" text="CGST" />
															</th>
															<th><spring:message code="rnl.bill.serviceTax"
																	text="Service Tax" /></th>
															<%-- <th><spring:message code=""
																	text="Estate Tds" /></th> --%>

															<th><spring:message code="rnl.bill.total"
																	text="Total" /></th>

														</tr>
														<tr>

															<td><spring:message code="rnl.bill.arrears"
																	text="Arrears" /></td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.sumOldGstPenaltyAndArrAmt}</p></td>


															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.arrearSgstAmt}</p></td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.arrearCgstAmt}</p></td>


															<td class="text-right round-decimal-2-dig">0</td>
															<%-- <td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.arrTdsAmt}</p></td> --%>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.arrearsSgstCgstTotalAmt}</p></td>


														</tr>
														<tr>
															<td><spring:message code="rnl.bill.current"
																	text="Current" /></td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.currAmtAndChequeDishCharges}</td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.currentSgstAmt}</p></td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.currentCgstAmt}</p></td>

															<td class="text-right round-decimal-2-dig">0</td>
															<%-- <td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.tdsAmount}</p></td> --%>
															
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.currentSgstCgstTotalAmt}</td>

														</tr>

														<tr>
															<td><spring:message code="rnl.bill.total"
																	text="Total" /></td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.arrearsAndCurrentAmt}</td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.totalArrCurrSgstAmt}</td>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.totalArrcurrCgstAmt}</td>
															<td class="text-right round-decimal-2-dig">0</td>
															<%-- <td class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.totalArrCurrTdsAmt}</td> --%>
															<td><p class="text-right round-decimal-2-dig">${command.contractAgreementSummaryDTO.totalArrCurrAmt}</td>

														</tr>

													</tbody>
												</table>
												<table
													class="table table-bordered receipt-amount-details-table">
													<tbody>
														<tr>
															<%-- <th>
													<spring:message code="" text="Arrears" />
												</th>
												<th>
													<spring:message code="" text="Current" />
												</th>
												<th>
													<spring:message code="" text="Total Amount" />
												</th> --%>
															<th><spring:message code="rnl.bill.sgst" text="SGST" />
															</th>
															<th><spring:message code="rnl.bill.cgst" text="CGST" />
															</th>

															<th><spring:message code="rnl.bill.paymentAmount"
																	text="Payment Amount" /></th>
															<th><spring:message code="rnl.bill.totalAmountGST"
																	text="Total Amount Including GST" /></th>
														</tr>
														<tr>

															<%-- <td><p class="text-right">${command.contractAgreementSummaryDTO.arrearsAmt}</p></td>
												<td><p class="text-right">${command.contractAgreementSummaryDTO.sumOfCurrentAmt}</p></td>
												<td><p class="text-right">${command.contractAgreementSummaryDTO.arrearsAndCurrentAmt}</p></td> --%>
															<c:choose>
																<c:when
																	test="${not empty command.contractAgreementSummaryDTO.chargeList }">
																	<c:forEach
																		items="${command.contractAgreementSummaryDTO.chargeList}"
																		var="charge">
																		<%-- <td class="text-center">${index.count}</td> --%>
																		<td class="text-right round-decimal-2-dig">${charge.chargeAmount}</td>
																	</c:forEach>
																</c:when>
																<c:otherwise>
																	<td class="text-right">0.00</td>
																	<td class="text-right">0.00</td>
																</c:otherwise>
															</c:choose>

															<td>
																<%-- Defect #172219 --%> <form:input
																	placeholder="${rnlEnterAmountPlaceholder}"
																	id="inputAmountId" path="inputAmount"
																	class=" form-control hasNumber text-right"
																	onchange="getTaxCharges(this)" maxlength="10"
																	onblur="getAmountFormatInDynamic((this),'inputAmountId')" />
															</td>
															<td>
																<div class="input-group">
																	<form:input path="payAmount"
																		cssClass="form-control mandColorClass hasDecimal text-right"
																		maxlength="10" id="payAmount" readonly="true"
																		onkeypress="return hasAmount(event, this, 8, 2)"
																		onchange="getAmountFormatInDynamic((this),'payAmount')" />
																	<label class="input-group-addon"><i
																		class="fa fa-inr"></i></label>
																</div>
															</td>

														</tr>
													</tbody>
												</table>

												<%-- <br>
									<c:if test="${not empty command.contractAgreementSummaryDTO.chargeList }">
										<table class="table table-bordered table-condensed">
	
											<tr>
												<th><spring:message code="" text="Tax Name" /></th>
												<th><spring:message code="" text="Tax Amount" /></th>
											</tr>
											<tbody>
												<c:forEach items="${command.contractAgreementSummaryDTO.chargeList}" var="charge">
													<tr>
														<td class="text-center">${index.count}</td>
														<td align="center">${charge.chargeDescEng}</td>
														<td align="center">${charge.chargeAmount}</td>
													</tr>
			
												</c:forEach>
											</tbody>
										</table>
									</c:if> --%>

											</div>
										</c:when>
										<c:otherwise>
											<div class="table-responsive">
												<!-- Code Comment on 18-Oct ISRAT behalf of SAMADHAN SIR-->
												<%-- <table class="table table-bordered table-condensed">

										<tr>
											<th><spring:message code="rnl.master.tax.desc"
													text="Tax Description" /></th>
											<th><spring:message code="rnl.master.balance.interest"
													text="Balance Arrears with interest" /></th>
											<th><spring:message code="rnl.master.current.tax"
													text="Current  Tax" /></th>
											<th><p class="text-right">
													<spring:message code="rnl.master.current.bal.tax"
														text="Current Balance Tax" />
												</p></th>
											<th><p class="text-right">
													<spring:message code="rnl.master.total.bal"
														text="Total Balance" />
												</p></th>
										</tr>

										<tr>
											<td>${command.taxDesc}</td>
											<td><p class="text-right">${command.billMas.bmTotalArrears}</p></td>
											<td><p class="text-right">${command.bmTotalAmount}</p></td>
											<td><p class="text-right">${command.bmTotalBalAmount}</p></td>
											<td><p class="text-right">${command.billMas.bmTotalArrears + command.bmTotalAmount}</p></td>
											
										</tr>
									</table> --%>
												<table class="table table-bordered table-condensed">

													<tr>
														<th><spring:message
																code="rnl.master.agreement.amount"
																text="Agreement Amount" /></th>
														<th><spring:message code="rnl.master.balance.amount"
																text="Balance Amount" /></th>
														<th><spring:message code="rnl.master.overdue.amount"
																text="Overdue Amount" /></th>
														<th><spring:message code="rnl.master.payment.amount"
																text="Payment Amount" /></th>
													</tr>
													<tr>
														<td><p class="text-center">${command.contractAgreementSummaryDTO.contAmount}</p></td>
														<td><p class="text-center">${command.contractAgreementSummaryDTO.balanceAmount}</p></td>
														<td><p class="text-center">${command.contractAgreementSummaryDTO.overdueAmount}</p></td>
														<td><form:input placeholder="Enter Amt"
																id="inputAmountId" path="inputAmount"
																class=" form-control hasNumber "
																onchange="getTaxCharges(this)" maxlength="10"
																onblur="getAmountFormatInDynamic((this),'inputAmountId')" />
														</td>
													</tr>
												</table>

												<br>
												<c:if
													test="${not empty command.contractAgreementSummaryDTO.chargeList }">
													<table class="table table-bordered table-condensed">

														<tr>
															<th><spring:message code="rnl.master.tax.name"
																	text="Tax Name" /></th>
															<th><spring:message code="rnl.master.tax.amount"
																	text="Tax Amount" /></th>
														</tr>
														<tbody>
															<c:forEach
																items="${command.contractAgreementSummaryDTO.chargeList}"
																var="charge">
																<tr>
																	<%-- <td class="text-center">${index.count}</td> --%>
																	<td align="center">${charge.chargeDescEng}</td>
																	<td align="center">${charge.chargeAmount}</td>
																</tr>

															</c:forEach>
														</tbody>
													</table>
												</c:if>

											</div>


											<div class="form-group padding-top-10">
												<!-- Code Comment on 18-Oct ISRAT behalf of SAMADHAN SIR-->
												<%-- <label class="col-sm-2 control-label"><spring:message
											code="rnl.master.receivable" text="Total Receivable" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<input type="text" class="form-control" id="totalPayable"
												value="${command.billMas.bmTotalArrears + command.bmTotalAmount}"
												value="${command.billMas.bmTotalOutstanding}"
												readonly="readonly"> <label
												class="input-group-addon"><i class="fa fa-inr"></i></label>
										</div>
									</div> --%>
												<label class="col-sm-2 control-label"><spring:message
														code="rnl.master.receipt.amount" text="Receipt Amount" /></label>
												<div class="col-sm-4">
													<div class="input-group">
														<form:input path="payAmount"
															cssClass="form-control mandColorClass hasDecimal text-right"
															maxlength="10" id="payAmount" readonly="true"
															onkeypress="return hasAmount(event, this, 8, 2)"
															onchange="getAmountFormatInDynamic((this),'payAmount')" />
														<label class="input-group-addon"><i
															class="fa fa-inr"></i></label>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>

									<div class="form-group">
										<%-- <label for="fromDateId"
											class="col-sm-2 control-label required-control"><spring:message
												code="rnl.paymentpageBackdate" text="Date" /></label>
										<div class="col-sm-4">
											<div class="input-group">
												<form:input path="receiptBackDate" id="fromDateId"
													cssClass="mandColorClass form-control"
													data-rule-required="true" maxlength="10" />
												<label class="input-group-addon mandColorClass"
													for="fromDateId"><i class="fa fa-calendar"></i> </label>
											</div>
										</div> --%>

										<label class="col-sm-2 control-label"><spring:message
												code="rnl.narration" text="Narration" /></label>
										<div class="col-sm-4">
											<form:textarea path="narration" id="narration"
												cssClass=" form-control" maxlength="1000" />
										</div>
									</div>

									<%-- <div class="form-group">
										<label class="control-label col-sm-2"><spring:message
												code="rnl.manual.receipt.no" text="Manual Receipt No" /></label>
										<div class="col-sm-4">
											<form:input path="manualRecptNo" class="form-control"
												maxlength="50"></form:input>
										</div>
									</div> --%>

								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<jsp:include page="/jsp/cfc/Challan/offlinePay.jsp" />
						</div>
						<div class="text-center padding-top-10">
							<button type="button" id="submitBT"
								class="btn btn-success btn-submit" onclick="saveData(this)">
								<spring:message code="rnl.master.submit" text="Submit" />
							</button>
							<button type="button" class="btn btn-danger"
								onclick="window.location.href='EstateContractBillPayment.html'">
								<spring:message code="rnl.master.back" text="Back" />
							</button>
						</div>
					</div>
				</c:if>

			</form:form>
		</div>
	</div>
</div>
