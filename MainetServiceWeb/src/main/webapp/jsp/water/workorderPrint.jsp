<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>

<%
	response.setContentType("text/html; charset=utf-8");
%>
<script type="text/javascript" src="js/water/workorderPrintGrid.js">
<!-- <script src="js/siteinspection/siteinspection.js"></script> -->
<script type="text/javascript">
	HTMLElementObject.contentEditable = false
</script>
<style>
#workprintnwc .cert-outer-div {
	border: 3px solid #000000;
	margin: 2rem !important;
}
#signnwc{
	padding-top:50px !important;
}
@media print{

.page-border {

height:2px;

border-top:1px solid #999;

margin-bottom:13px;
}

}
</style>
<!--  <ol class="breadcrumb">
      <li><a href="../index.html"><i class="fa fa-home"></i></a></li>
      <li>Work Order Printing</li>
    </ol> -->
<!-- ============================================================== -->
<!-- Start Content here -->
<!-- ============================================================== -->
<div class="animated slideInDown">
	<!-- Start info box -->
	<div class="widget" id="letter">
		<div class="widget-content"  id="workprintnwc">
			<div class="form-group clearfix workprintmargin">
				<div class="cert-outer-div page-border">
				
				<div class="col-xs-3 text-left">
					<!-- <img src="assets/img/logo.png"> -->
					<img src="${userSession.orgLogoPath}" width="80">
				</div>
				<div class="col-xs-6 text-center margin-top-10 margin-bottom-10">
					<h4 class="margin-bottom-0">${userSession.ULBName.lookUpDesc}</h4></br>
					<h4 class="margin-bottom-0"><span>(</span>${department.dpDeptdesc}<span>)</span></h4>
					<h4 class="margin-bottom-0"><span>(</span>${ServiceName}<span>)</span></h4>
				</div>
				
				
			<div class="form-group clearfix margin-top-10">
				<div class="col-xs-6 text-left">
					<p><spring:message code="water.workorder.content24" text=""/>${WorkOrderNumber}</p>
					<input type="hidden" value="${WorkOrderNumber}"
						id="WorkOrderNumber" />
				</div>
				<div class="col-xs-6 text-right">
					<p class="text-bold"><spring:message code="water.workorder.content23" text=""/> ${NewDate}</p>
				</div>
			</div>
			
			<%-- <div class="form-group clearfix margin-top-20">
				<div class="col-xs-1"><spring:message code="demand.from" text="From"/></div>
				<div class="col-xs-11">
					<p><spring:message code="water.orderr.munc.eng" text="Municipal Engineer"/></p>
					<p>${userSession.ULBName.lookUpDesc}</p>
				</div>
			</div> --%>
			<div class="form-group clearfix margin-top-10">
				<div class="col-xs-1"><spring:message code="water.workorder.content25" text=""/></div>
				<div class="col-xs-12"><td style="text-align: left"><p><spring:message code="water.workorder.mr" text="Mr / Mrs "/> <span class="text-bold"> ${ApplicantFullName}</span></p></td>
				
				<%-- 	<p>${ApplicantFullName}</p>
				 --%>	<div class="col-xs-15"><p>
						<spring:message code="water.meterReadingViewDetails.Address" /> :
						${applicantAddress}
					</p></div>
				</div>
			</div>
			<div class="form-group clearfix margin-top-5 margin-left-15">
				<div class="col-xs-1"></div>
				<div class="col-xs-11 padding-left-30">
					<p class="margin-top-10 text-bold"><spring:message code="water.workorder.subject" text="Subject:"/> ${ServiceName}</p>
					<p class="margin-top-10">
						<b></b><spring:message code="water.workorder.content18" text=""/>${ServiceName}</b> <br>
						<spring:message code="water.workorder.content19" text=""/><b>${ApplicationID}</b>
						<spring:message code="water.workorder.content20" text=""/><b>${ApplicationDate} </b>
						<spring:message code="water.workorder.content21" text=""/>
						<br>
						<spring:message code="water.dis.print.sub" text=""/><b>${LoiNo}</b>
						<spring:message code="water.dis.print5" text=""/><b>${LoiDate}</b><br>
						<c:if test="${ServiceCode  eq 'WNC' }">
						<spring:message code="water.work.order.receipt.no" text="Receipt No. "/><b>${recptno}</b>
						<spring:message code="water.dis.print5" text=""/><b>${recptDate}</b>
						</c:if>
						<%-- and your
						connection no.is ${ConnectionNo} --%>
					</p>
				</div>
			</div>
			<div class="form-group clearfix margin-left-15">
				<p>
					<spring:message code="water.workorder.content" text="Sir/Madam,"/><br>
					
					</p>
					<c:choose>
					<c:when test="${ServiceCode  ne 'WCC' }">
					<c:if test="${ServiceCode  ne 'WNC' }">
				<p>	<br><spring:message code="water.workorder.content1" text=""/>&nbsp;${chargestotal}
					&nbsp;<spring:message code="water.workorder.contenta1" text=""/>
					<spring:message code="water.workorder.contenta1a" text="For Receipt No:"/>
					${recptno}<br>
					<spring:message code="water.workorder.content2" text=""/>${recptDate}
					<spring:message code="water.workorder.content3" text=""/>
					<spring:message code="water.workorder.content4" text=""/><br>
					<spring:message code="water.workorder.content5" text=""/>
				
				</p>
				</c:if>
				<div class="col-sm-1"></div>
				<div class="col-sm-11">
				<c:if test="${ServiceCode  eq 'WNC' }">
				<p><spring:message code="water.work.order.new.refrence" text="As per above references, the details of new water connection sanctioned are below,"/></p>
				</c:if>
				<table class="table" id="connectionInfo" >
					<tr style="font-size: 1.2em !important;">
						<td width="50">1.</td><td><spring:message code="water.workorder.content14" text="Approved Water Connection No :- "/> <span class="text-bold">${ConnectionNo} </span></td></tr>
					<tr style="font-size: 1.2em !important;">	<td width="50">2.</td><td><spring:message code="water.workorder.content15" text="Connection Size :- "/>${connectionSize} <spring:message code="water.work.inch" text=" inch"/></td></tr>
					<tr style="font-size: 1.2em !important;">	<td width="50">3.</td><td><spring:message code="water.workorder.content27" text="Approved Connection category"/>${connectionCategory}</td></tr>
					<tr style="font-size: 1.2em !important;">	<td width="50">4.</td><td><spring:message code="water.workorder.content28" text="Approved Connection type"/>${connectionType}</td></tr>
					<tr style="font-size: 1.2em !important;">	<td width="50">5.</td><td><spring:message code="water.workorder.content26" text="Hole Man Name :- "/>${holeMan}</td></tr>
					<tr style="font-size: 1.2em !important;">	<td width="50">6.</td><td><spring:message code="water.workorder.content16" text="Plumber Name and No :- "/>${PlumName} ${PLumLicNO}</td></tr>
						
				</table>
				</div>
				
					<p>
					<spring:message code="water.workorder.content6" text=""/>
					<%-- <spring:message code="water.workorder.content7" text=""/><br> --%>
				</p>
				</c:when>
				<c:otherwise>
				<p><spring:message code="water.dis.print1" text=""/>&nbsp;${ConnectionNo}  
					&nbsp;<spring:message code="water.dis.print2" text=""/>${LoiNo}
					<spring:message code="water.dis.print3" text="For Receipt No:"/>
					<spring:message code="water.dis.print4" text=""/><b>${recptno}</b>
					<spring:message code="water.dis.print5" text=""/><b>${recptDate}</b>
					<spring:message code="water.dis.print6" text=""/><b>${ConnectionNo}</b>
					<spring:message code="water.dis.print7" text=""/><b>${connectionSize}</b>
					<spring:message code="water.dis.print8" text=""/><b>${recptDate}</b>
				
				
				</p>
				
				
				
				</c:otherwise>
				</c:choose>
				<br>
				<c:if test="${ServiceCode  ne 'WNC' }">
				<li style="font-size: 0.7em !important;"><spring:message code="water.workorder.content13" text="This is computer generated receipt and does not require physical signature"/></li>
				</c:if>
				<!-- <p class="margin-bottom-20 ">
				</p> -->
					<c:choose>
					<c:when test="${ServiceCode  eq 'WCC' }">
				<p class="margin-top-10 ">
							<spring:message code="water.dis.print.copy" text="Copy To:"/><br><b>${department.dpDeptdesc}</b>
							</p>
				</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
				
				<div class="form-group clearfix">
					<table class="table table-message">
						<%-- <table class="table table-border">
							<tr>
								<th class="text-left-imp"><spring:message code="water.nodues.service" text="Service"/></th>
								<th class="text-left-imp"><spring:message code="water.order.rupees" text="Rupees"/></th>
							</tr>

							<c:forEach items="${Chargedescruption}" var="singleDoc"
								varStatus="count">
								<tr>
									<td><strong> ${singleDoc.key}</strong></td>
									<td><strong>${singleDoc.value}</strong></td>
								</tr>
							</c:forEach>
							<tr>
								<th><p class="text-right"><spring:message code="water.nonMeterBillprint.Total" text="Total"/></p></th>
								<th><p class="text-left">${chargestotal}</p></th>
							</tr>

						</table> --%>
						</div>
						
						<div class="form-group clearfix padding-vertical-10">

							<%-- <c:if test="${PrintReprintFlg == 'Y'}">
								<div class="col-xs-4">
									<p class="padding-top-30">Copy To: Plumber Name</p>
									<div class="barcode padding-top-10">${PlumName}</div>
								</div>
								<div class="col-xs-4 text-center" id="Duplicat">
									<i class="fa fa-files-o fa-2x red" aria-hidden="true"></i> <strong><spring:message code="water.order.duplicate.copy" /> </strong>
								</div>

							</c:if>

							<c:if test="${PrintReprintFlg == null}">
								<div class="col-xs-8">
									<p class="padding-top-30"><spring:message code="water.order.copy.plumber.name" text="Copy To: Plumber Name"/></p>
									<div class="barcode padding-top-10 bold">${PlumName}</div>
								</div>
							</c:if> --%>
							<div class="col-xs-4"></div><div class="col-xs-4"></div>
							<div class="col-xs-4 text-center" id="signnwc">
								<p><spring:message code="water.workorder.content22" text=""/></p>
								<p class="margin-top-10">
								<c:choose>
									<c:when test="${ServiceCode  ne 'WCC' }">
									<spring:message code="water.orderr.munc.eng" text=""/>
								</c:when>
								<c:otherwise>
								</c:otherwise>
								</c:choose>
									<c:if test="${ServiceCode ne 'WCC' }"><spring:message code="water.workorder.content9" text="Municipal Engineer"/></c:if>
									 <c:if test="${ServiceCode eq 'WCC' }"><b><p>Deputy Engineer</p></b></c:if>
								
								</p>
								<p>${userSession.ULBName.lookUpDesc}</p>
							</div>
						</div>
						<c:choose>
				<c:when test="${ServiceCode  ne 'WCC' }">
						<p>
							<spring:message code="water.workorder.content8" text=""/>:<br>
							<p class="padding-left-30"><spring:message code="water.workorder.content9" text=""/><br>
							<spring:message code="water.workorder.content10" text=""/><br>
							<spring:message code="water.workorder.content11" text=""/><br>
							<spring:message code="water.workorder.content12" text=""/><br><br><br>
						</p>
						
						<c:if test="${ServiceCode  eq 'WNC' }">
						<li style="font-size: 0.7em !important;"><spring:message
													code="water.workorder.content13"
													text="This is a computer-generated receipt and does not require a physical signature" />
											</li>
						
						</c:if>
									</table>
				</div>
						<div>
						
							<%-- <p class="margin-bottom-10 margin-top-10"><spring:message code="trutirejection.rejctionremark"/></p> --%>
						<p class="padding-vertical-10">
							<strong><spring:message code="water.workorder.content17" text=""/><br> </strong>
						</p>
						<table class="table">

							<c:forEach items="${TermsConditon}" var="singleDoc"
								varStatus="count">
								<tr>
									<td width="50">${count.index+1}</td>
									<td>${singleDoc.artRemarks}</td>
								</tr>
							</c:forEach>

						</table>

			
						</div>
						</c:when>
					</c:choose>
					</div>

					</div>
						<div class="text-center clearfix margin-top-5"
							id="hideprintbuttton">
							<c:if test="${PrintReprintFlg == null}">
								<button onclick="printContent2('letter')"
									class="btn btn-primary hidden-print printClass">
									<i class="icon-print-2"></i>
									<spring:message code="siteinspection.print" />
								</button>
							</c:if>

							<c:if test="${PrintReprintFlg == 'Y'}">
								<button onclick="printContent2('letter')"
									class="btn btn-primary hidden-print printClass">
									<i class="icon-print-2"></i><spring:message code="water.order.reprint" text="Reprint"/> 
								</button>
							</c:if>
							<!--   <input type="button" class="btn btn-default" value="Back" onclick="window.location.href='WorkOrder.html'"> -->
							<!--  <input type="button"
						onclick="window.location.href='WorkOrder.html'"
						class="btn btn-default hidden-print" value="Back"> -->
							<input type="button"
								onclick="window.location.href='WaterWorkOrderPrinting.html'"
								class="btn btn-danger hidden-print" value="Back">
						</div>

						</div>
						</div>
						</div>
<!-- ------------letter end------ -->

						</div>