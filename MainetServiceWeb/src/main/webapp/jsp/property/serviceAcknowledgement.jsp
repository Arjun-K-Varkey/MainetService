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
<title>Service Acknowledgement</title>

<style>
/* *::-moz-selection {background: none repeat scroll 0 0 #63b2f5; color: #fff; text-shadow: none;}
* {margin: 0; padding: 0; outline:0;} */
a {text-decoration: none; color: inherit;}
img {border: 0;}
ul, ol {list-style-type: none;}
.clear:after, .clearfix:after {clear: both; display:table;	content: "";}
body{font-family:Arial, Helvetica, sans-serif; color:#000; font-weight:400; overflow-x: hidden;}

table.gridtable {width: 100%; font-size:11px; color:#000; border-collapse: collapse; overflow-x:auto;}
table.gridtable th {padding: 6px; background-color: rgba(81, 81, 81, 0.1); border:1px solid rgba(81, 81, 81, 0.4);  line-height:24px;}
table.gridtable td {font-size: 12px; line-height:20px; padding:4px; background-color: rgba(255, 255, 255, 0.5); border:1px solid rgba(81, 81, 81, 0.4);  vertical-align: top;}

#revenue_receipt{position:relative; width:900px; margin:auto; margin-top:30px; border-radius:4px; border:1px #ccc solid; overflow:hidden;margin-bottom: 35px;}
.header{ position:relative; clear:both; padding:10px; overflow:hidden;}
.header h1{ position:relative; clear:both; padding:10px 0px; text-align:center; font-size:20px; color:#000; text-transform:uppercase; font-weight:normal;}
.header ul{position:relative; clear:both; list-style:none;}
.header ul li{float:left; list-style:none; position:relative;}
.header h2{position:relative; clear:both; padding:3px 0px; text-align:center; line-height:13px; font-size:16px; color:#000; text-transform:uppercase; font-weight:bold;}
.header p{position:relative; clear:both; padding:3px 0px; text-align:center; line-height:13px; font-size:15px;}

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

@media print {
body {-webkit-print-color-adjust:exact; margin:0mm 10mm 0mm 10mm;}
#btnPrint, #btnCancel{display:none;}
/* #revenue_receipt {page-break-after: always;} */

}
/* .col-sm-3{    width: 10%;    float: left;}
.col-sm-9{    width: 75%;float: left;}
.logo{margin-top:10px;}
.not-logged-avatar{width:75px;height:75px;}  */
.center{text-align: center}
.right{text-align: right}
</style>
</head>
<body>


<div id="revenue_receipt">
<table class="gridtable">

<tr>
<td colspan="3"></td>
<td colspan="4"> <p><h2 align="center"><spring:message code="mutation.certificate.cfc"/></h2></p>
<p><h3 align="center">${command.offlineDTO.orgNameMar}</h3></p> </td>
<td colspan="3" class="center"><p><spring:message code="mutation.service.token.no" /></p><p>${command.offlineDTO.referenceNo}</p></td>
</tr>
<tr>
<td colspan="3"><span><spring:message code="mutation.service.official.ref"/></span></td>
<td colspan="7"></td>
</tr>
<tr>
<td colspan="3"><span><spring:message code="mutation.service.applicant.name"/></span></td>
<td colspan="7">${command.offlineDTO.applicantName}</td>
</tr>
<tr>
<td colspan="3"><span><spring:message  code="mutation.service"/></span></td>
<td colspan="7">${command.offlineDTO.serviceNameMar}</td>
</tr>
<tr>
<td colspan="3"><span><spring:message code="mutation.submit.date"/></span></td>
<td colspan="2">${command.offlineDTO.fromDateStr}</td>
<td colspan="3"><spring:message code="mutation.time"/></td>
<td colspan="2">${command.offlineDTO.time}</td>
</tr>
<tr>
<td colspan="3"><span><spring:message code="mutation.due.date"/></span></td>
<td colspan="2">${command.offlineDTO.toDateStr}</td>
<td colspan="3"><spring:message code="mutation.time"/></td>
<td colspan="2">${command.offlineDTO.time}</td>
</tr>
<tr>
<td scope="row" width="50" colspan="10" class="right"><span><spring:message code="mutation.signature"/></span></td>
</tr>
</table>
<input type='button' id='btnPrint' value='Print'
		style='width: 100px; position: fixed; bottom: 10px; left: 47%; z-index: 111;'
		onclick='window.print()' />
	<input type='button' id='btnCancel' value='Cancel'
		style='width: 100px; position: fixed; bottom: 10px; left: 55%; z-index: 111;'
		onclick='window.close()' />
</div>
</body>
</html>
