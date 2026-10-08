<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Payment Receipt</title>
<link href="assets/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script>

<style>
*::-moz-selection {background: none repeat scroll 0 0 #63b2f5; color: #fff; text-shadow: none;}
* {margin: 0; padding: 0; outline:0;}
a {text-decoration: none; color: inherit;}
img {border: 0;}
ul, ol {list-style-type: none;}
.clear:after, .clearfix:after {clear: both; display:table;	content: "";}
body{font-family:Arial, Helvetica, sans-serif; color:#000; font-weight:400; overflow-x: hidden;}

table.gridtable {width: 100%; font-size:11px; color:#000; border-collapse: collapse; overflow-x:auto;}
table.gridtable th {padding: 4px; background-color: rgba(81, 81, 81, 0.1); border:1px solid rgba(81, 81, 81, 0.4);  line-height:20px; text-align:center;}
table.gridtable td {font-size: 12px; line-height:20px; padding:4px; background-color: rgba(255, 255, 255, 0.5); border:1px solid rgba(81, 81, 81, 0.4);  vertical-align: top;}

#revenue_receipt{position:relative; width:900px; margin:auto; margin-top:30px; border-radius:4px; border:1px #ccc solid; overflow:hidden;margin-bottom: 35px;}
.header{ position:relative; clear:both; padding:10px; overflow:hidden;}
.header h1{ position:relative; clear:both; padding:10px 0px; text-align:center; font-size:20px; color:#000; text-transform:uppercase; font-weight:normal;}
.header ul{position:relative; clear:both; list-style:none;}
.header ul li{float:left; list-style:none; position:relative;}
.header h2{position:relative; clear:both; padding:3px 0px; text-align:center; line-height:13px; font-size:16px; color:#000; text-transform:uppercase; font-weight:bold;}
.header p{position:relative; clear:both; padding:3px 0px; text-align:center; line-height:13px; font-size:15px;}
.header .right-content {
	text-align: right;
	position: absolute;
	top: 50%;
	right: 0rem;
	-webkit-transform: translateY(-50%);
	-moz-transform: translateY(-50%);
	-ms-transform: translateY(-50%);
	-o-transform: translateY(-50%);
    transform: translateY(-50%);
    margin-top: -1rem;
}

.float-right{float:right !important;}

.header ul li:nth-child(01){width:80px; position:relative;}
.header ul li:nth-child(02){width:680px; text-align:center;}
.header ul li:nth-child(03){width:100px; text-align:center;}
.header img.logo{width:70px; height:70px; position:absolute; top:10px; left:10px;}

input[type="text"]{
	float: left;
	background-color: rgba(255, 255, 255, 0.9);
	border: 1px solid rgba(81, 81, 81, 0.5);
	width:98%;
	font-size: 11px;
	font-weight: 400;
	color: #000;
	padding: 5px;
	height: 25px;
	line-height:11px;
	box-sizing: border-box;
	font-family: inherit;
	white-space: pre-line;
}




textarea[type="text"] {float: left; background-color: rgba(255, 255, 255, 0.9); border: 1px solid rgba(81, 81, 81, 0.5); width:98%; font-size: 11px; font-weight: 400; color:#000; padding:4px; height: 100px; line-height:11px; box-sizing: border-box; font-family:inherit; white-space:pre-line;}
.border-left{border-left: 1px solid rgba(81, 81, 81, 0.5) !important;}
.border-top{border-top: 1px solid rgba(81, 81, 81, 0.5)  !important;}
.border-right{border-right: 1px solid rgba(81, 81, 81, 0.5)  !important;}
.border-bottom{border-bottom: 1px solid rgba(81, 81, 81, 0.5)  !important;}

page[size="A4"] {width:21cm; height:29.7cm; margin:10mm;}

.padding-20 {
	padding: 20px !important;
}
.btn-primary {
	background-color: #4A525F;
	border-color: #4A525F;
	color: #FFFFFF;
}
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open .dropdown-toggle.btn-primary {
	background-color: #3E444F;
	border-color: #3E444F;
	color: #FFFFFF;
}
 


@media print {
body {-webkit-print-color-adjust:exact; margin:0mm 10mm 0mm 10mm;}
#btnPrint, #btnCancel{display:none;}
#revenue_receipt {page-break-after: always;}



}

.col-sm-3{    width: 10%;    float: left;}
.col-sm-9{    width: 75%;float: left;}
.logo{margin-top:10px;}
.not-logged-avatar{width:75px;height:75px;} 


</style>

</head>

<body>
<%-- <input type='button' id='btnPrint' value='<spring:message code="master.PRINT" text="Print"/>'
		style='width: 100px; position: fixed; bottom: 10px; left: 47%; z-index: 111;'
		onclick='window.print()' />
	<input type='button' id='btnCancel'  value='<spring:message code="master.cancel" text="Cancel"/>'
		style='width: 100px; position: fixed; bottom: 10px; left: 55%; z-index: 111;'
		onclick='window.close()' /> --%>

<div id="revenue_receipt">
<table class="gridtable">
<tr>
	<td colspan="8">
		<div class="header">
			<div class="col-xs-2 col-sm-2">
         		<img src="${userSession.orgLogoPath}" alt="Organisation Logo" style="width: 100px; height: 100px;">
         	</div>
         	<div class="col-xs-8 col-sm-8">
				<h1><b>${command.receiptDTO.orgNameMar}</b></h1>
				<c:if test="${command.receiptDTO.envFlag eq 'D' }"><div class="text-center"><b><spring:message code="skdcl.receipt.duplicate.copy" /></b></div></c:if>
				<c:if test="${command.receiptDTO.envFlag ne 'D' && command.receiptDTO.chkPmtMode ne 'Cheque'}"><div class="text-center"><b><spring:message code="skdcl.receipt.receiptMar" /></b></div></c:if>
		    	<c:if test="${command.receiptDTO.envFlag ne 'D' && command.receiptDTO.chkPmtMode eq 'Cheque'}"><div class="text-center"><b><spring:message code="skdcl.label.provisionalReceipt" text="Provisional Receipt"/></b></div></c:if>
			</div>
		</div>
	</td>
</tr>


<tr>
	<th align="center" scope="col" width="150"><span><spring:message code="skdcl.receipt.receiptNo" /></span></th>
	<th align="center" scope="col" width="150"><span><spring:message code="skdcl.receipt.receiptDate" /></span></th>
	<th colspan="2" align="center"  scope="col" width="250"><span><spring:message code="skdcl.receipt.accountHead" /></span></th>
	<th align="center" scope="col" width="120"><span><spring:message code="skdcl.receipt.facilityCenter"/></span></th>
	<th align="center" scope="col" width="150"><span><spring:message code="skdcl.receipt.windowNo"/></span></th>
</tr>
<tr>
				<c:if test="${command.receiptDTO.serviceCode eq 'RLTDS'}">
					<td class="border-left  border-right" align="center">${command.receiptDTO.usageType5_V}</td>
					<td class="border-left  border-right" align="center">${command.receiptDTO.receiptDate}
				</c:if>
				<c:if test="${command.receiptDTO.serviceCode ne 'RLTDS'}">
					<td class="border-left  border-right" align="center">${command.receiptDTO.receiptNo}</td>
					<td class="border-left  border-right" align="center">${command.receiptDTO.receiptDate}</td>
				</c:if>
				<td colspan="2" class="border-left" align="center">
	<c:if test="${command.receiptDTO.deptShortCode eq 'WT' && command.receiptDTO.serviceCodeflag eq 'N'}">${command.receiptDTO.subject}
	</c:if>
	<c:if test="${command.receiptDTO.deptShortCode eq 'ML' && command.receiptDTO.serviceCodeflag eq 'N'}">${command.receiptDTO.accountHead}
	</c:if>
	<c:if test="${(command.receiptDTO.deptShortCode ne 'ML' && command.receiptDTO.deptShortCode ne 'WT')  && command.receiptDTO.serviceCodeflag eq 'N'}">${command.receiptDTO.serviceName}
	</c:if>
	<c:if test="${command.receiptDTO.serviceCodeflag eq 'Y'}"><spring:message code="skdcl.receipt.misReceipt" text="Miscellaneous Receipt"/>
	</c:if>

	</td>
	<td class="border-left  border-right" align="center">${command.cfcSchedulingCounterDet.collcntrno}</td>
	<td class="border-left  border-right" align="center">${command.cfcSchedulingCounterDet.counterno}</td>

</tr>

<tr>
<th align="center" scope="row"><span><spring:message code="skdcl.receipt.receivedFrom"/></span></th>
<td colspan="7">${command.receiptDTO.receivedFrom}  <c:if test="${command.receiptDTO.deptShortCode eq 'AS' && command.receiptDTO.serviceCode eq 'NDT'}">${command.receiptDTO.propNo_connNo_estateN_V}
	</c:if>
	<c:if test="${command.receiptDTO.deptShortCode eq 'AS' && command.receiptDTO.serviceCode eq 'DUB'}">${command.receiptDTO.propNo_connNo_estateN_V}
	</c:if>
	<c:if test="${command.receiptDTO.deptShortCode eq 'AS' && command.receiptDTO.serviceCode eq 'EXT'}">${command.receiptDTO.propNo_connNo_estateN_V}
	</c:if>
	<c:if test="${command.receiptDTO.deptShortCode eq 'AS' && command.receiptDTO.serviceCode eq 'ACG'}">${command.receiptDTO.propNo_connNo_estateN_V}
	</c:if></td>
</tr>

<c:if test="${not empty command.receiptDTO.newHouseNo && command.receiptDTO.newHouseNo ne null && command.receiptDTO.serviceCode eq 'CBP'}">
<tr>
	<th align="center" scope="row"><span><spring:message code="rnl.lengends.gstNo"/></span></th>
	<td colspan="7">${command.receiptDTO.newHouseNo}</td>
</tr></c:if>

<th align="center" scope="row"><span><spring:message code="skdcl.receipt.description" text=""/></span></th>
<td colspan="7" id="subject"><p><strong>
 <c:if test="${command.receiptDTO.deptShortCode ne 'WT'}">${command.receiptDTO.subject}</c:if>
 <c:if test="${command.receiptDTO.deptShortCode eq 'WT'}">
 <c:if test="${command.receiptDTO.serviceCode ne 'WDB'}"> Receipt for <%-- <spring:message code="skdcl.receipt.loi.no" text="LOI number: "/> --%>${command.receiptDTO.loiNo} <!-- for --> </c:if> ${command.receiptDTO.subject}
 <c:if test="${command.receiptDTO.serviceCode eq 'WDB'}">
 <spring:message code="water.report.ccnNo" text="Connection No.: "/>: ${command.receiptDTO.loiNo},
 <c:forEach items="${command.receiptDTO.billDetails}" var="entry" varStatus="status">
 <c:if test="${status.index == 0}">
 		<spring:message code="water.meterReadingViewDetails.BillNo." text="Bill No. "/>: ${entry.key},
        <spring:message code="water.waterBillGeneration.BillDate" text="Bill Date "/>: ${entry.value}
 </c:if>
 </c:forEach>
 </c:if>
 </c:if></strong></p></td>
</tr>

<tr>
<th align="center" width="16%"><span><spring:message code="skdcl.receipt.modeOfPay" text=""/></span></th>
<th align="center" width="16%"><span><spring:message code="skdcl.receipt.amount" text=""/></span></th>
<th align="center" width="16%"><span><spring:message code="skdcl.receipt.checkOrDdNo" text=""/></span></th>
<th align="center" width="16%"><span><spring:message code="skdcl.receipt.checkOrDdDate" text=""/></span></th>
<th align="center" width="18%"><span><spring:message code="skdcl.receipt.bankName" text=""/></span></th>
<th align="center" width="18%"><span><spring:message code="skdcl.receipt.accountNo" text=""/></span></th>
</tr>

 <tr>
<td align="center">${command.receiptDTO.paymentMode}</td>
<fmt:formatNumber type="number" value="${command.receiptDTO.amount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amt"/>
<td align="center">${amt}</td>
<td align="center">${command.receiptDTO.ddOrPpnumber}</td>
<td align="center">${command.receiptDTO.ddOrPpDate}</td>
<td align="center">${command.receiptDTO.bankName}</td>
<td align="center">${command.receiptDTO.bankAccountId}</td>
</tr>

</table>

<table class="gridtable">
			<tr>
				<th align="center" scope="col" width="16%"><span><spring:message
							code="skdcl.receipt.referenceNo" /></span></th>
				<th align="center" scope="col" width="16%"><span><spring:message
							code="skdcl.receipt.applicationDate" /></span></th>
							
				<c:if test="${command.receiptDTO.serviceCode eq 'CBP'}">
					<th align="center" scope="col" width="16%"><span><spring:message
							code="rnl.bill.payment.demand.year" /></span></th></c:if>						
				<c:if test="${command.receiptDTO.serviceCode eq 'CBP'}">
				<th align="center" scope="col" width="28%"><span><spring:message
							code="skdcl.receipt.details" /></span></th></c:if>
							
				<c:if test="${command.receiptDTO.serviceCode ne 'CBP'}">
				<th align="center" scope="col" width="32%"><span><spring:message
							code="skdcl.receipt.details" /></span></th></c:if>			
				<th align="center" scope="col" width="18%"><span><spring:message
							code="skdcl.receipt.payAmount" /></span></th>
				<th align="center" scope="col" width="18%"><span><spring:message
							code="skdcl.receipt.recAmount" /></span></th>
			</tr>

	<tr>
		<td class="border-left  border-right" height="150" align="center">${command.receiptDTO.applicationNumber}</td>
		<c:choose>
			<c:when test="${command.receiptDTO.deptShortCode eq 'AS' && command.receiptDTO.serviceCode eq 'MUT'}">
			<td class="border-left  border-right" height="150" align="center">${command.receiptDTO.rcptDate}</td>			
			</c:when>
			<c:otherwise>
			<td class="border-left  border-right" height="150" align="center">${command.receiptDTO.receiptDate}</td>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${command.receiptDTO.serviceCode eq 'CBP'}">
		<td class="border-left  border-right" height="150" align="left">
			 <c:forEach items="${command.receiptDTO.getDemandYearList()}" var="demandYear" varStatus="statusPayment">
		     <p>${demandYear}</p>
		  </c:forEach></td>
		 </td>
		</c:if>
		
		<td class="border-left  border-right" height="150" align="left">
		  <c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
		     <p>${collection.details}</p>
		  </c:forEach></td>
		<td class="border-left  border-right" height="150"><span class="float-right">
		  <c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
		     <fmt:formatNumber type="number" value="${collection.amountPayable}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amtpayble"/>
		   <p>${amtpayble}</p>
		   </c:forEach></span></td>
		<td class="border-left  border-right" height="150"><span class="float-right">
		   <c:forEach items="${command.receiptDTO.getPaymentList()}" var="collection" varStatus="statusPayment">
		     <fmt:formatNumber type="number" value="${collection.amountReceived}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="amtReceived"/>
		    <p> ${amtReceived}</p>
		</c:forEach></span></td>
	</tr>	



<tr>
	<td rowspan="3" colspan="${command.receiptDTO.serviceCode eq 'CBP' ? 4 : 3}" align="left" scope="row" width="156">
		<span class="margin-right-5"><spring:message code="skdcl.receipt.rupees" text="Amount in Words:-"/></span>${command.receiptDTO.amountInWords}
		<br> <br> <br>
					<p>
						<spring:message code="skdcl.receipt.noteMar" text="" />
					</p>
					<c:if test="${command.receiptDTO.chkPmtMode eq 'Cheque'}">
					<p>
						<spring:message code="skdcl.receipt.note" text="" />
					</p>
					</c:if>
					<c:if test="${not empty command.cfcSchedulingCounterDet.collcntrno}">
					<p>
						${command.cfcSchedulingCounterDet.collcntrno} / ${command.cfcSchedulingCounterDet.counterno} / ${command.receiptDTO.userName} / ${command.receiptDTO.cfcDate}  ${command.receiptDTO.receiptTime}  
					<%-- <c:if test="${command.receiptDTO.paymentMode eq 'WEB'}">
					
							${command.receiptDTO.paymentText}
						
					</c:if> --%>
					</p>
					</c:if>
					
					<p>
						<spring:message code="skdcl.receipt.gst" text="" />
						<c:if test="${command.receiptDTO.paymentMode eq 'WEB'}">
							&nbsp;&nbsp;${command.receiptDTO.paymentText}
						</c:if>
					</p>
					 <%-- <p>
						${command.cfcSchedulingCounterDet.collcntrno} / ${command.cfcSchedulingCounterDet.counterno} / ${command.receiptDTO.receiverName} / ${command.receiptDTO.cfcDate}  ${command.receiptDTO.receiptTime}  
					</p> --%>
				</td>
	<th class="border-left border-right border-top border-bottom" width="125" height="20"><b><spring:message code="skdcl.receipt.totalAmt"/></b></th>
	<fmt:formatNumber type="number" value="${command.receiptDTO.totalReceivedAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalamtReceived"/>
				<c:if test="${command.receiptDTO.serviceCode eq 'CBP'}">
					<fmt:formatNumber type="number"
						value="${command.receiptDTO.amount}" groupingUsed="false"
						maxFractionDigits="2" minFractionDigits="2" var="amt" />
					<td class="border-right border-top border-bottom"><span
						class="float-right"><b>${amt}</b></span></td>
				</c:if>
				<c:if test="${command.receiptDTO.serviceCode ne 'CBP'}">
					<td class="border-right border-top border-bottom"><span
						class="float-right"><b>${totalamtReceived}</b></span></td>
				</c:if>

			</tr>
<tr>
	<%-- <td colspan="2" class="height-4rem">${command.receiptDTO.receiverName}</td> --%>
	<c:if test="${command.receiptDTO.deptShortCode ne 'ML'}">
	<td colspan="2" class="height-4rem">${command.receiptDTO.receiverName}</td></c:if>
	<c:if test="${command.receiptDTO.deptShortCode eq 'ML'}"><td colspan="2" class="height-4rem"></td></c:if>
	
</tr>
<tr>
	<th colspan="2" height="20"><spring:message code="skdcl.receipt.sign" text=""/></th>
</tr>
<tr>
	<th colspan="6">
		<spring:message code="skdcl.receipt.comp.generate" text="computer generated" />
	</th>
</tr>
</table>
</div>
<div class="text-center hidden-print padding-20">
		<button id='btnPrint' onclick='window.print()' 
			class="btn btn-primary hidden-print">
			<i class="fa fa-print"></i>
			<spring:message code="master.PRINT" text="Print"/>
		</button>
		<button id='btnCancel' onclick='window.close()' type="button" class="btn btn-danger hidden-print">
		<spring:message code="master.cancel" text="Cancel"/></button>
	</div>
</body>
</html>
