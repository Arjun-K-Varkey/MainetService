<!-- Start JSP Necessary Tags -->
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
<jsp:useBean id="date" class="java.util.Date" scope="request" />
<script
	src="assets/libs/excel-export/excel-export.js"></script>
<script src="js/account/incomeAndExpeditureReport.js"
	type="text/javascript"></script>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<!-- Start Content here -->
<script language="javascript">
	function printdiv(printpage) {
		debugger;
		var headstr = "<html><head><title></title></head><body>";
		var footstr = "</body>";
		var newstr = document.all.item(printpage).innerHTML;
		var oldstr = document.body.innerHTML;
		document.body.innerHTML = headstr + newstr + footstr;
		window.print();
		document.body.innerHTML = oldstr;
		return false;
	}
</script>
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2 class="excel-title"> <spring:message code="account.income.and.expenditure" text="Income And Expenditure Schedule Report" /></h2>
		</div>
		<div class="widget-content padding">
			<form:form action="" id="incomeAndExpenditureReport"
				class="form-horizontal">
				<div id="receipt">
					<div class="form-group">
						<div
							class="ccol-xs-6 col-sm-8 col-sm-offset-2 col-xs-offset-1  text-center">
							<h3 class="text-extra-large margin-bottom-0 margin-top-0">
								<c:if test="${userSession.languageId eq 1}">
                                     ${ userSession.getCurrent().organisation.ONlsOrgname}<br>
								</c:if>
								<c:if test="${userSession.languageId eq 2}">
								             ${ userSession.getCurrent().organisation.ONlsOrgnameMar}<br>
								</c:if>
								 <u><spring:message code="account.schedule.code.report" text="Schedule Report : Code No." />${command.accountIEDto.primaryAcHeadDesc} </u>
								<p class="text-extra-large"><spring:message code="budget.consolidation.format.financial.year" text="Financial Year :" /> ${command.accountIEDto.financialYr}</p>
							</h3>


						</div>
						<div class="col-sm-2 col-xs-3 pull-right">
							<p>
								<spring:message code="accounts.date"
									text="Date" />
								<fmt:formatDate value="${date}" pattern="dd/MM/yyyy" />
								<br>
								<spring:message code="acounts.time"
									text="Time" />
								<fmt:formatDate value="${date}" pattern="hh:mm a" />
							</p>
						</div>
					</div>
					<!-- <div class="clearfix padding-10"></div> -->
					<div class="container">
						<div id="export-excel">
							<table class="table table-bordered  table-fixed" id="importexcel">
							<h2 class="excel-title" id="tlExcel" style="display: none"> <spring:message code="account.income.and.expenditure" text="Income And Expenditure Schedule Report" /></h2>
								<thead>
									<tr>
										<th class="text-center">Code No.</th>
										<th class="text-center">Particulars</th>
										<th class="text-center">Current Year Amount (Rs.)</th>
										<th class="text-center">Previous YearAmount (Rs.)</th>
									</tr>
								</thead>
								<c:set var="totalc" value="${0}" />
								<c:set var="totalp" value="${0}" />
								<tbody>
									<c:forEach items="${command.accountIEDto.listOfIE}" var="data"
										varStatus="index">
										<tr>
											<td style="text-align: center;">${data.primaryAcHeadId}</td>
											<td>${data.primaryAcHeadDesc}</td>
											<%-- <td class="text-right">${data.currentYearAmount}</td> --%>
											<c:choose>
												<c:when test="${data.currentYearAmount > '0'}">
													<td class="text-right">${data.currentYearAmount}</td>
												</c:when>
												<c:otherwise>
													<c:if test="${data.currentYearAmount ==null}">
														<td class="text-right">0.00</td>
													</c:if>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${data.previousYearAmount > '0'}">
													<td class="text-right">${data.previousYearAmount}</td>
												</c:when>
												<c:otherwise>
													<c:if test="${data.previousYearAmount ==null}">
														<td class="text-right">0.00</td>
													</c:if>
												</c:otherwise>
											</c:choose>
											<c:set var="totalc"
												value="${totalc + data.currentYearAmount}" />
											<c:set var="totalp"
												value="${totalp + data.previousYearAmount}" />
										</tr>
									</c:forEach>
									<tr>
										<td></td>
										<td><b>Total Other Income</b></td>
										<td class="text-right text-bold"><fmt:formatNumber
												type="number" value="${totalc}" minFractionDigits="2" /></td>
										<td class="text-right text-bold"><fmt:formatNumber
												type="number" value="${totalp}" minFractionDigits="2" /></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</form:form>
			<div class="text-center hidden-print padding-10">
				<button onClick="printdiv('receipt');"
					class="btn btn-primary hidden-print" data-toggle="tooltip" data-original-title="Print">
					<i class="fa fa-print"></i> <spring:message code="account.budgetestimationpreparation.print" text="Print" />
				</button>
				<button id="btnExport1" type="button"
					class="btn btn-blue-2 hidden-print" data-toggle="tooltip" data-original-title="Download">
					<i class="fa fa-file-excel-o"></i> <spring:message code="acounts.download" text="Download" />
				</button>
				<button type="button" class="btn btn-danger" onclick="back();" data-toggle="tooltip" data-original-title="Back">
				<i class="fa fa-chevron-circle-left" aria-hidden="true"></i>
					<spring:message code="account.bankmaster.back" text="Back" />
				</button>
			</div>

		</div>
	</div>
</div>