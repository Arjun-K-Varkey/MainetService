<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>
<style>
.overflow-hidden {
	overflow: hidden;
}
.bill-report-table-header{
	width:100%;
	margin-left: 2rem;
	padding :0px;
}
.bill-report-table-header tr td, .bill-report-table-header tr th{
	padding :2px;
	background:none !important;
	text-align:left !important;
}
</style>

<!-- Start Content here -->
<div class="content">
	<div class="widget" id="receipt">
		<div class="widget-content padding">
		<form:form action="ViewPropertyDetail.html" class="form-horizontal"
				name="ViewPropertyDetail" id="ViewPropertyDetail">
				<div class="overflow-hidden">	
					<div class="margin-0">
						<div class="overflow-hidden padding-vertical-10">
							<div class="col-xs-2 col-sm-2">
								<img width="80" src="${userSession.orgLogoPath}">
							</div>
							<div class="col-xs-8 col-sm-8 text-center">
								<h3 class="margin-bottom-10 padding-bottom-10">${userSession.organisation.ONlsOrgname}</h3>
							</div>
						</div>
						<div class="col-xs-7 col-sm-8 col-xs-offset-1 col-sm-offset-2 text-center bill-heading">
							<h5><spring:message	code="" text="BILLS AND RECEIPTS DETAILS REPORT" /></h5>
						</div>
						<div class="col-xs-3 col-sm-2 text-right billDateTime">
							<p class="padding-vertical-10"><spring:message	code="" text="Date" /> : ${command.billHistoryDate}</p>
							<p><spring:message	code="" text="Time" /> : ${command.billHistoryTime}</p>
						</div>
					</div>
				
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 margin-top-20">
					 	<table class="bill-report-table-header">
							<tr>
								<th colspan="2"><spring:message code="propertydetails.PropertyNo." text="Property No"/></th>
								<td colspan="2">: ${command.provisionalAssesmentMstDto.assNo}</td>
								<th colspan="2"><spring:message code="property.FlatNo" text="Flat No"/></th>
								<td colspan="2">: ${command.provisionalAssesmentMstDto.flatNo}</td>
								<th colspan="2"><spring:message code="property.report.Ward" text="Ward"/></th>
								<td colspan="2">: ${command.provisionalAssesmentMstDto.assWardDesc1}</td>
								<th colspan="2"><spring:message code="property.report.Zone" text="Zone"/></th>
								<td colspan="2">: ${command.provisionalAssesmentMstDto.assWardDesc2}</td>
								
							</tr>
							<tr>
								<th colspan="2"><spring:message code="property.TotalDemand" text="Total Demand"/></th>
								<td colspan="2">: ${command.grandTotalDemand}</td>
								<th colspan="2"><spring:message code="" text="Total Recovery"/></th>
								<td colspan="2">: ${command.grandReceiptAmount}</td>
								<th colspan="2"><spring:message code="" text="Outstanding"/></th>
								<td colspan="2">: ${command.grandTotalOutstanding}</td>
							</tr>
						</table>
				</div>	
				<div class="clear overflow-hidden padding-10">
					<c:if test="${not empty  command.billMasList}">
						<table id="BillDetails" class="table table-striped table-bordered margin-top-10">
			                    <thead>
			                        <tr> 
				                        <th width="5%"><spring:message code="propertyTax.SrNo"/></th>
				                        <th width="10%"><spring:message code="property.bill.no" text="Bill No"/></th> 
				                        <th width="10%"><spring:message code="property.bill.date" text="Bill Date"/></th> 
		                        		<th width="10%"><spring:message code="property.Year(From-To)" text="Year(From-To)"/></th>
										<th width="8%"><spring:message code="property.TotalDemand" text="Total Demand"/></th>	
										<th width="8%"><spring:message code="property.receiptno" text="Receipt No"/></th>
										<th width="10%"><spring:message code="property.receiptdate" text="Receipt Date"/></th>
										<th width="8%"><spring:message code="property.receiptamount" text="Receipt Amount"/></th>
										<th width="8%"><spring:message code="property.bill.received.from" text="Received From"/></th>	
									</tr>	
								 </thead>	
								<tbody>
									<c:forEach var="billMasList" items="${command.billMasList}" varStatus="status">	
									 <tr>
									  	<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${status.count}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmNo}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmBilldtString}</td>
										<%-- <td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.billDistrDateString}</td> --%>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}">${billMasList.bmCcnOwner}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalAmount"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${bmTotalAmount}</td>
										<c:choose>
										<c:when test="${empty billMasList.billWiseReceiptList}">
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										</c:when>
										<c:otherwise>
										<c:forEach var="receipt" items="${billMasList.billWiseReceiptList}" varStatus="status">
										<tr>
										<c:choose>
										  <c:when test="${receipt.extraStringField1 eq 'Y'}">	
											    <td><i class="text-red-1">${receipt.lookUpParentId}</i></td>
												<td><i class="text-red-1">${receipt.lookUpCode}</i></td>
												<td><i class="text-red-1">${receipt.otherField}</i></td>
												<td><i class="text-red-1">${receipt.docDescription} (Cheque Dishour)</i></td>
												<td>${receipt.descLangFirst}</td>
										   </c:when>
										   <c:otherwise>
										       <td>${receipt.lookUpParentId}</td>
												<td>${receipt.lookUpCode}</td>
												<td>${receipt.otherField}</td>
												<td>${receipt.descLangFirst}</td>
										    </c:otherwise>
										</c:choose>
									</tr>
									</c:forEach>
										</c:otherwise>
										</c:choose>
									</tr>
									</c:forEach>
									<tr><th colspan="4"><spring:message code="taxdetails.total" text="Total"/></th>
									<th class="text-right">${command.grandTotalDemand}</th>
									<th></th>
									<th></th>
									<th class="text-right">${command.grandReceiptAmount}</th>
									<th></th>
									</tr>
								</tbody>
						</table>
						<div id="pageFooter"></div>
					</c:if>
				</div>
			</div>	
				<div class="col-xs-12 col-sm-12 padding-top-10 text-center hidden-print">
					<button onclick="printContent('receipt')"
						class="btn btn-primary hidden-print">
						<i class="icon-print-2"></i>
						<spring:message code="property.noDues.Print" text="Print"></spring:message>
					</button>
					<button type="button" class="btn btn-danger" id="back"
						onclick="backToPropDetailPage()">
						<spring:message code="property.Back" text="Back"></spring:message>
					</button>
				</div>
		</form:form>
					
		</div>
		<style type='text/css'>

        @media print
        thead {
        		display: table-header-group;
        	}
        </style>
	</div>
</div>
<script>
    function printContent(el) {
	var restorepage = document.body.innerHTML;
	var printcontent = document.getElementById(el).innerHTML;
	document.body.innerHTML = printcontent;
	window.print();
	document.body.innerHTML = restorepage;
    }
</script>