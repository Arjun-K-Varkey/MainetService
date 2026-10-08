<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="date" class="java.util.Date" scope="request" />
<script src="js/account/accountFinancialReport.js"
	type="text/javascript"></script>
<script
	src="assets/libs/excel-export/jquery.tablesorter.js"></script>
<script
	src="assets/libs/excel-export/jquery.tablesorter.pager.js"></script>
<script>
	$(function() {
		$(".table").tablesorter().tablesorterPager({
			container : $(".ts-pager"),
			cssGoto : ".pagenum",
			removeRows : false,
			size: 12
		});
		$(function() {
			$(".table").tablesorter({
				cssInfoBlock : "avoid-sort",
			});

		});
	});
</script>
<div id="content">
	<apptags:breadcrumb></apptags:breadcrumb>
	<div class="content">
		<!-- Start Content here -->
		<div class="widget">
			<div class="widget-header">
				<h2 class="excel-title">
					<spring:message code="receipt.payment.title"
						text="Receipts & Payments" />
				</h2>
			</div>
			<div class="widget-content padding">
			<div id="receipt">
				<form action="" method="get" class="form-horizontal">
					<h2 class="excel-title" style="display:none">
					<spring:message code="receipt.payment.title" text="Receipts & Payments" />
				   </h2>
						<div class="form-group">
							<div class="col-xs-9 col-sm-8 col-sm-offset-2  text-center"">
								<h3 class="text-extra-large margin-bottom-0 margin-top-0">
								<c:if test="${userSession.languageId eq 1}">
                                     ${ userSession.getCurrent().organisation.ONlsOrgname}<br>
								</c:if>
								<c:if test="${userSession.languageId eq 2}">
								             ${ userSession.getCurrent().organisation.ONlsOrgnameMar}<br>
								</c:if></h3>
								<strong><spring:message code="account.receipts.payment.period" text="Receipts and Payment Account for the period" />
									${reportData.fromDate} <spring:message code="account.to" text="To" /> ${reportData.toDate}</strong>
							</div>
							<div class="col-sm-2 col-xs-3">
								<p>
									<spring:message code="accounts.date" text="Date:" />
									<fmt:formatDate value="${date}" pattern="dd/MM/yyyy" />
									<br><spring:message code="acounts.time" text="Time:" />
									<fmt:formatDate value="${date}" pattern="hh:mm a" />
							</div>


						</div>
						<input type="hidden" value="${validationError}" id="errorId">
						<div id="export-excel">
							<table class="table table-bordered table-condensed  clear" id="importexcel">
								<div class="excel-title" id="tlExcel" style="display: none">
									<spring:message code="receipt.payment.title"
										text="Receipts & Payments" />
								</div>
								<thead>
								<tr>
									<th width="12%"><spring:message code="" text="Code No." /></th>
									<th width="48%"><spring:message code=""
											text="Head Of Account" /></th>
									<th width="20%"><spring:message code=""
											text="Current Period Amount (Rs.)" /></th>
									<th width="20%"><spring:message code=""
											text="Corresponding Previous Period Amount (Rs.)" /></th>
								</tr>
								</thead>
								<tfoot class="tfoot">
									<tr>
										<th colspan="15" class="ts-pager form-horizontal">
											<div class="btn-group">
												<button type="button" class="btn first">
													<i class="fa fa-step-backward" aria-hidden="true"></i>
												</button>
												<button type="button" class="btn prev">
													<i class="fa fa-arrow-left" aria-hidden="true"></i>
												</button>
											</div> <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
											<div class="btn-group">
												<button type="button" class="btn next">
													<i class="fa fa-arrow-right" aria-hidden="true"></i>
												</button>
												<button type="button" class="btn last">
													<i class="fa fa-step-forward" aria-hidden="true"></i>
												</button>
											</div> <select class="pagesize input-mini form-control"
											title="Select page size">
												<option selected="selected" value="12" class="form-control">12</option>
												<option value="20">20</option>
												<option value="30">30</option>
												<option value="all">All Records</option>
										</select> <select class="pagenum input-mini form-control"
											title="Select page number"></select>
										</th>
									</tr>
								</tfoot>
								<tbody>
								<tr>
									<th>&nbsp;</th>
									<th style="text-align: left"><spring:message code=""
											text="Opening Balance <br>Cash balances including Imprest<br>Balances with Banks/Treasury (including balances in designated bank accounts)" /></th>
									<th style="text-align: right">${reportData.openingBalanceIndianCurrency}<br>${reportData.openingBalance}<br>${reportData.bankBalance}</th>
									<th style="text-align: right">${reportData.totalBalance}<br>${reportData.previousOpeningCashAmt}<br>${reportData.previousOpeningBankAmt}</th>

								</tr>
								<tr>
									<th style="text-align: left" colspan="4"><spring:message
											code="" text="Operating Receipts" /></th>
								</tr>
								<c:forEach items="${openingReceiptSideList.listOfSum}"
									var="receiptSide" varStatus="row">
									<tr>
										<td>${receiptSide.accountCode}</td>
										<td>${receiptSide.accountHead}</td>
										<td align="right">${receiptSide.actualAmountReceivedIndianCurrency}</td>
										<td align="right">${receiptSide.balanceRecoverableIndianCurrency}</td>
									</tr>

								</c:forEach>
								<tr>
									<th style="text-align: left" colspan="4"><spring:message
											code="" text="Non-Operating Receipts" /></th>

								</tr>

								<c:forEach items="${nonOpeningReceiptSideList.listOfSum}"
									var="nonOpenReceiptSide" varStatus="row">

									<tr>
										<%-- <td>${receiptSide[0]}</td> --%>
										<td>${nonOpenReceiptSide.accountCode}</td>
										<td>${nonOpenReceiptSide.accountHead}</td>
										<td align="right">${nonOpenReceiptSide.actualAmountReceivedIndianCurrency}</td>
										<td align="right">${nonOpenReceiptSide.balanceRecoverableIndianCurrency}</td>
									</tr>

								</c:forEach>

								<tr>
									<th style="text-align: right" colspan="2"><spring:message
											code="" text="GRAND TOTAL" /></th>

									<th class="text-right">${reportData.actualAmountReceivedIndianCurrency}</th>
									<th class="text-right">${reportData.chequeAmountIndianCurrency}</th>
								</tr>

								<tr>
									<th style="text-align: left" colspan="4"><spring:message
											code="" text="Operating Payments" /></th>

								</tr>

								<c:forEach items="${openingPaymentSideList.listOfSum}"
									var="paymentSide">

									<tr>
										<td>${paymentSide.accountCode}</td>
										<td>${paymentSide.accountHead}</td>
										<td align="right">${paymentSide.actualAmountReceivedIndianCurrency}</td>
										<td align="right">${paymentSide.balanceRecoverableIndianCurrency}</td>
									</tr>
								</c:forEach>

								<tr>
									<th style="text-align: left" colspan="4"><spring:message
											code="" text="Non-Operating Payments" /></th>

								</tr>

								<c:forEach items="${nonOpeingPaymentSideList.listOfSum}"
									var="nonOpenPaymentSide">

									<tr>
										<td>${nonOpenPaymentSide.accountCode}</td>
										<td>${nonOpenPaymentSide.accountHead}</td>
										<td align="right">${nonOpenPaymentSide.actualAmountReceivedIndianCurrency}</td>
										<td align="right">${nonOpenPaymentSide.balanceRecoverableIndianCurrency}</td>
									</tr>
								</c:forEach>
								<tr>
									<th>&nbsp;</th>
									<th style="text-align: left"><spring:message code=""
											text="Closing Balances<br>Cash balances including Imprest <br>Balances with Banks/Treasury (including balances in designated bank accounts)" /></th>
									<th style="text-align: right">${reportData.closingCash}<br>${reportData.closingBalance}<br>${reportData.closingBankAmount}</th>
									<th style="text-align: right">${reportData.closingAmt}<br>${reportData.previousClosingCashAmt}<br>${reportData.previousClosingBankAmt}</th>

								</tr>
								<tr>
									<th style="text-align: right" colspan="2"><spring:message
											code="" text="GRAND TOTAL" /></th>
									<th class="text-right">${reportData.sumbalanceRecoverableIndianCurrency}</th>
									<th class="text-right">${reportData.chequeDepositIndianCurrency}</th>
								</tr>
							 </tbody>	
							</table>
						</div>
						<div class="row"></div>
				</form>
				</div>
				<div class="text-center hidden-print padding-10">
						<button onclick="PrintDiv('receipt');"
							class="btn btn-primary hidden-print" title="Print">
							<i class="fa fa-print"></i>
							<spring:message code="account.budgetestimationpreparation.print"
								text="Print" />
						</button>
						<button type="button" class="btn btn-danger"
							onclick="window.location.href='AccountFinancialReport.html'"
							title="Back">
							<i class="fa fa-chevron-circle-left" aria-hidden="true"></i>
							<spring:message code="account.bankmaster.back" text="Back" />
						</button>
				</div>
			</div>
		</div>
	</div>
</div>