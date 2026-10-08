<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
table.revenueDetails tr th{text-align:center !important}
.revenue p {font-size: 1em;}

.left-box{
border:1px solid #bde5cf ;
padding:0.5rem;
margin-top:10px;
background:#eee;
text-align:center;
}
#billperiod{
white-space: nowrap;
}
</style>


<div class="content">
<div class="widget invoice revenue" id="receipt">
	<div class="widget-content padding">

		<table class="table table-bordered revenueDetails">
			<tr>
				<td colspan="7">
					<div class="col-sm-2 col-xs-2">
						<img src="${userSession.orgLogoPath}" alt="Organisation Logo" class="not-logged-avatar pull-left">
					</div>
			
					<div class="col-sm-8 col-xs-8 text-center">
						<h4>${command.receiptDTO.orgNameMar}</h4>
						<%-- <c:if test="${userSession.languageId eq 2}"><h4>${command.receiptDTO.orgNameMar}</h4></c:if> --%>
					    <c:if test="${command.receiptDTO.envFlag eq 'D' }"><h5><spring:message code="skdcl.receipt.duplicate.revenue.copy" text="Duplicate Copy"/></h5></c:if>
						<c:if test="${command.receiptDTO.envFlag ne 'D' && command.receiptDTO.chkPmtMode ne 'Cheque'}"><h5><spring:message code="receipt.label.receipt" text="Receipt"/></h5></c:if>
						<c:if test="${command.receiptDTO.envFlag ne 'D' && command.receiptDTO.chkPmtMode eq 'Cheque'}"><h5><spring:message code="receipt.label.provisionalReceipt" text="Provisional Receipt"/></h5></c:if>
					</div>
				
				
					<c:if test="${command.receiptDTO.deptShortCode ne 'WT' && 'Y' eq command.illegal}">
						<div class="col-sm-2 col-xs-2 left-box"><spring:message code="illegal.logo"/></div>
					</c:if>
				</td>
			</tr>
			<tr>
				<th width="10%"><span><spring:message code="receipt.label.number"/></span></th>
				<th width="15%"><span><spring:message code="receipt.label.dateTime"/></span></th>
				<th colspan="2" align="center"><spring:message code="skdcl.receipt.accountHead"/></th>
				<th width="10%"><span><spring:message code="skdcl.receipt.facilityCenter"/></span></th>
				<th width="20%" colspan="2"><span><spring:message code="skdcl.receipt.windowNo"/></span></th>
			</tr>
			<tr class="text-center">
				<td>${command.receiptDTO.receiptNo}</td>
				<c:if test="${not empty command.receiptDTO.manualReceiptDate}">
				<td>${command.receiptDTO.manualReceiptDate} ${command.receiptDTO.receiptTime}</td></c:if>
				<c:if test="${empty command.receiptDTO.manualReceiptDate}">
				<td>${command.receiptDTO.receiptDate} ${command.receiptDTO.receiptTime}</td></c:if>
				<td colspan="2">${command.receiptDTO.subject}</td>
				<td>${command.receiptDTO.cfcCenter}</td>
				<td colspan="2">${command.receiptDTO.cfcCounterNo}</td>
			</tr>
			
		<%-- 	<tr>
			<th><spring:message code="" text="Manual Receipt No:"/></th>
			<td colspan="2">${command.receiptDTO.manualReceiptNo}</td>
			<th colspan="2"><spring:message code="" text="Manual Receipt Date:"/></th>
			<td colspan="3">${command.receiptDTO.manualReceiptDate}</td>
			</tr> --%>
			
			
			<tr>
				<th colspan="1"><spring:message code="receipt.label.recFrom" text="Receive From/Owner Name"/></th>
				<td colspan="6">${command.receiptDTO.payeeName}</td>
			</tr>
			
			<tr>
				<th colspan="1"><spring:message code="skdcl.receipt.description" text="Description"/></th>
				<c:if test="${empty command.receiptDTO.flatNo}">
				<td colspan="6">${command.receiptDTO.propNo_connNo_estateNo_L}: ${command.receiptDTO.propNo_connNo_estateN_V} and ${command.receiptDTO.receivedFrom}</td>
			    </c:if>
			   <c:if test="${not empty command.receiptDTO.flatNo}">
			   <td colspan="6">${command.receiptDTO.propNo_connNo_estateNo_L}: ${command.receiptDTO.propNo_connNo_estateN_V}</td></c:if>
			</tr>
			<!-- #146012 -->
			
			<c:if test="${not empty command.receiptDTO.occupierName}">
				<tr>
				<th colspan="1"><spring:message code="receipt.label.OccupierName" text="Occupier Name"/></th>
				<td colspan="6">${command.receiptDTO.occupierName}</td>
			</tr></c:if>
			
			<tr>
				<th><span><spring:message code="skdcl.receipt.label.payMode" text="Payment Mode"/></span></th>
				<th><spring:message code="skdcl.receipt.amount"/></th>
				<th><span><spring:message code="receipt.label.DrawnOn" text="Drawn On"/></span></th>
				<th><span><spring:message code="revenue.pay.option.account"/></span></th>
				<th><spring:message code="revenue.pay.option.accountDate"/></th>
				<%-- <th width="10%" class="text-right"><spring:message code="property.bill.payment.bank"/></th> --%>
				<th><spring:message code="revenue.payment.accountNo"/></th>
			</tr>

				<%-- <c:choose>
					<c:when
						test="${not empty command.receiptDTO.getMultiPayModeList()}">
				<c:forEach items="${command.receiptDTO.multiPayModeList}" var="multiPayModeList">
					<tr>
					<td class="text-center">${multiPayModeList.payModeDesc}</td>
					<fmt:formatNumber type="number"
						value="${multiPayModeList.amount}" groupingUsed="false"
						maxFractionDigits="2" minFractionDigits="2" var="sumOfRs" />
					<td class="text-right">${sumOfRs}</td>
					<td>${multiPayModeList.bmDrawOn}</td>
					<td>${multiPayModeList.bmChqDDNo}</td>
					<td>${multiPayModeList.bmChqDDDateTemp}</td>
					<td>${multiPayModeList.bankName}</td>
					<td>${multiPayModeList.bmBankAccountId}</td>
					</tr>
					</c:forEach>
					</c:when>
					<c:otherwise> --%>
					<tr>
						<td class="text-center">${command.receiptDTO.paymentMode}</td>
						<fmt:formatNumber type="number"
							value="${command.receiptDTO.amount}" groupingUsed="false"
							maxFractionDigits="2" minFractionDigits="2" var="sumOfRs" />
						<td>${sumOfRs}</td>
						<td>${command.receiptDTO.bankName}</td>
						<td>${command.receiptDTO.ddOrPpnumber}</td>
						<td>${command.receiptDTO.ddOrPpDate}</td>
						<td>${command.receiptDTO.bankAccountId}</td>
						</tr>
					<%-- </c:otherwise>
				</c:choose> --%>
			 
		</table>
		
		<table class="table table-bordered revenueDetails">
			<tr>
				<th width="10%" class="text-center"><spring:message code="receipt.label.billNumber" text="Bill Number"/></th>
				<th width="15%" class="text-center"><spring:message code="receipt.label.billDate" text="Bill Date"/></th>
				<th width="15%" class="text-center"><spring:message code="receipt.label.billPeriod" text="Bill Period"/></th>
				<th colspan="2" class="text-center"><spring:message code="receipt.label.detailsOfBill" text="Details of Bill"/></th>
				<th width="10%" class="text-center"><spring:message code="receipt.label.payableAmount" text="Payable amount"/></th>
				<th width="10%" colspan="2" class="text-center" ><spring:message code="receipt.label.receivedAmount" text="Received amount"/></th>
			</tr>
			
			<tr>
				<td class="text-center"><c:forEach items="${command.receiptDTO.getBillDetails()}" var="entry" varStatus="key">
				${entry.key}<br></c:forEach></td>
				<td class="text-center"><c:forEach items="${command.receiptDTO.getBillDetails()}" var="entry" varStatus="key">
				${entry.value}<br></c:forEach></td>
				<td class="text-center" id ="billperiod"><c:forEach items="${command.receiptDTO.getBillYearDetails()}" var="entry" varStatus="key">
				${entry.value}<br></c:forEach></td>
				<td class="text-left" colspan="2"><c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
				<c:if test="${collection.displaySeq ne null}">
				${collection.details}</c:if><br></c:forEach>
				<c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
				<c:if test="${collection.displaySeq eq null}">
				<br>
				${collection.details}</c:if></c:forEach>
				</td>
				
				<td class="text-right"><c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
				<c:if test="${collection.displaySeq ne null}">
				<fmt:formatNumber type="number" value="${collection.amountPayablArrearCurr}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amountPayablArrearCurr"/>
				
				${amountPayablArrearCurr}<br></c:if></c:forEach></td>
				<td class="text-right" colspan="2"><c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
				<c:if test="${collection.displaySeq ne null}">
				<fmt:formatNumber type="number" value="${collection.amountReceivedArrearCurr}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amountReceivedArrearCurr"/>
				
				${amountReceivedArrearCurr}<br></c:if></c:forEach></td>
			
			</tr>
			<tr>
				<td colspan="5" rowspan="3" class="text-left"><div style="float:left;width:95%;"><spring:message code="receipt.label.receipt.rupees" text="Rupees"/> : ${command.receiptDTO.amountInWords}</div>
				<div style="float:right;width:5%;"><spring:message code="skdcl.receipt.totalAmt" text="Total"/> </div>
				<br>
				<spring:message code="receipt.label.payment.note2"/>
				<br>
				<c:if test="${command.receiptDTO.deptShortCode ne 'WT' && 'Y' eq command.illegal}">
				<spring:message code="receipt.label.payment.note3"/>
				</c:if>
				<br>
				<c:if test="${command.receiptDTO.chkPmtMode eq 'Cheque'}">
					<p style="margin-top:5;">
						<spring:message code="skdcl.receipt.note" text="Note: Subject to realization of Cheque" />
					</p>
			     </c:if>
			   <br>
			   <c:if test="${command.receiptDTO.paymentMode ne 'WEB'}">
				<p>${command.receiptDTO.cfcCenter} / ${command.receiptDTO.cfcCounterNo} / ${command.receiptDTO.userName} /  ${command.receiptDTO.receiptDate} ${command.receiptDTO.receiptTime}
				</c:if><c:if test="${command.receiptDTO.paymentMode eq 'WEB'}">
				  ${command.receiptDTO.receiptDate} ${command.receiptDTO.receiptTime} &nbsp;&nbsp;${command.receiptDTO.paymentText}
					
				</c:if>
				</p>
				</td>
				 <fmt:formatNumber type="number" value="${command.receiptDTO.amountPayable}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amountPayable"/>
				 <td class="text-right">${amountPayable}</td>
					 <fmt:formatNumber type="number" value="${command.receiptDTO.amount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amount"/>			 					 
				 <td class="text-right">${amount}</td>
			</tr>
			<tr>
			
				<td colspan="3"> <br><br><br><br></td>
			</tr>
			<tr>
				<th colspan="3"><spring:message code="skdcl.receipt.signature" text="Signature" /> </th>
			</tr>
		</table>	
		
		<div class="text-center hidden-print padding-20">
		<c:choose>
		<c:when test="${command.receiptDTO.receiptType eq 'RB' || command.receiptDTO.receiptType eq 'AA'}">
		</c:when>
		<c:otherwise>
		<button onclick="printdiv('receipt')"
				class="btn btn-primary hidden-print">
				<i class="fa fa-print"></i>
				<spring:message code="receipt.label.print" text="Print" />
			</button>
		</c:otherwise>
		</c:choose>
			<button onClick="window.close();" type="button" class="btn btn-danger hidden-print"><spring:message code="receipt.label.close" text="Close" /></button>
		</div>
						
	</div>
</div>		    				        
<script type="text/javascript">
function printdiv(printpage) {
		$("#billperiod").css("white-space","nowrap");
		var headstr = "<html><head><title></title><link href='assets/css/style-responsive.css' rel='stylesheet' type='text/css' /></head><body>";
		var footstr = "</body>";
		var newstr = document.all.item(printpage).innerHTML;
		var oldstr = document.body.innerHTML;
		//document.body.innerHTML = headstr + newstr + footstr;
		document.body.innerHTML = newstr;
		window.print();
		document.body.innerHTML = oldstr;
		return false;
}
</script>