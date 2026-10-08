<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript" src="js/mainet/validation.js"></script>
<jsp:useBean id="marathiConvert" class="com.abm.mainet.common.utility.Utility"></jsp:useBean> 
<style>
.cert-outer-div {
	border: 3px solid #000000;
}
.bnd-acknowledgement .border-bottom-black { 
     border-bottom: 3px solid #000000;
}
</style>
<script>
function printContent(el) {
	var restorepage = document.body.innerHTML;
	var printcontent = document.getElementById(el).innerHTML;
	document.body.innerHTML = printcontent;
	window.print();
	document.body.innerHTML = restorepage;
    }
$("html, body").animate({ scrollTop: 0 }, "slow");
</script>
<!-- Start Content here -->
<div class="content animated slideInDown">
	<!-- Your awesome content goes here -->
	<div class="widget" id="receipt">
		<div class="widget-content padding">
			<form action="" method="get" class="bnd-acknowledgement">
				<div class="cert-outer-div">
				<div class="row margin-0">
				
					<div class="border-bottom-black" style="margin: 10px 14px;">
						<div class="col-xs-2">
						<img width="80" src="${userSession.orgLogoPath}">
						</div>
						<div class="col-xs-8 text-center">
							<h2 class="margin-bottom-10 padding-bottom-10">${userSession.organisation.ONlsOrgnameMar}</h2>
						</div>
						<div class="col-xs-2"></div>
						<div style="clear:both"></div>
					</div> 
					
					<div class="col-xs-12 col-sm-12 text-center margin-bottom-10">
						<h4 class="margin-top-10">
							<span class="text-bold"><spring:message
								code="water.acknowledgement.applNo"  text="App no"></spring:message></span>
							<span class="text-bold margin-left-40"> 
							<!--D#129167  -->	
									${command.waterApplicationAcknowledgementDTO.applicationId}	
								<%-- <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${command.marriageDTO.applicationId}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",command.marriageDTO.applicationId)}
										</c:otherwise>
									</c:choose> --%>
							</span>
						</h4>
					</div>
				</div>

		 		<div class="row margin-bottom-10 margin-left-0 margin-right-0">
		 		<c:if test="${command.waterApplicationAcknowledgementDTO.connectionNo != null}">
					<div class="col-xs-3 col-sm-2 padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.applicantName" text="Applicant Name" /></span>
					</div>
					<div class="col-xs-2 col-sm-2">
						:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.applicantName}</span>
					</div>
					</c:if>
					
					<c:if test="${command.waterApplicationAcknowledgementDTO.connectionNo == null}">
					<div class="col-xs-3 col-sm-2 padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.applicantName" text="Applicant Name" /></span>
					</div>
					<div class="col-xs-4 col-sm-4">
						:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.applicantName}</span>
					</div>
					</c:if>
					
					
				<!--	<div class="col-xs-3 col-sm-2 text-right padding-top-5">
						<span class="text-bold"><spring:message
								code="water.ConnectionNo" text="Connection no" /></span>
					</div>
					-->
						<!--Applied if condition for not null connectionNo,-->
						<c:if test="${command.waterApplicationAcknowledgementDTO.connectionNo != null}">
							<div class="col-xs-3 col-sm-2 text-right padding-top-5">
								<span class="text-bold"> <spring:message
										code="water.ConnectionNo" text="Connection no" /></span>
							</div>
							<div class="col-xs-2 col-sm-2">
								:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.connectionNo}</span>
							</div>
						</c:if>


					</div>
				
				<div class="row margin-bottom-10 margin-left-0 margin-right-0">
					<div class="col-xs-3 col-sm-2 padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.serviceName" text="Service Name" /></span>
					</div>
					<div class="col-xs-9 col-sm-10">
						:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.serviceName}</span>
					</div>
				</div>
				
				<div class="row margin-bottom-10 margin-left-0 margin-right-0">
					<div class="col-xs-3 col-sm-2 padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.deptName" text="Dept Name" /></span>
					</div>
					<div class="col-xs-9 col-sm-10">
						:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.departmentName}</span>
					</div> 
				</div>
				
				<div class="row margin-bottom-10 margin-left-0 margin-right-0">
					<div class="col-xs-3 col-sm-2 padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.appDate" text="App Date"/></span>
					</div>
					<div class="col-sm-2 col-xs-2">
								<c:set var="now" value="<%=new java.util.Date()%>" />
								<fmt:formatDate pattern="dd/MM/yyyy" value="${now}" var="date" />
								:<span class="margin-left-20">${date}</span>

					</div>
					<%-- <div class="col-xs-2 col-sm-2">
						:<span class="margin-left-20"><fmt:formatDate pattern="dd/MM/yyyy" value="${command.marriageDTO.appDate}" /></span>
						
					</div> --%>
					<div class="col-xs-3 col-sm-2 text-right padding-top-5">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.appTime" text="App Time"/></span>
					</div>
					 <div class="col-sm-2 col-xs-2">
					 	:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.appTime}</span>
								<%-- <c:set var="now" value="<%=new java.util.Date()%>" />
								<fmt:formatDate pattern="HH:mm" value="${now}" var="time" />
								<span>${time}</span> --%>

					</div>  
					<%-- <div class="col-xs-2 col-sm-2">
						:<span class="margin-left-20">${command.marriageDTO.appTime}</span>
					</div>  --%>
				</div>

				 <div class="row padding-top-5 margin-bottom-10 margin-left-0 margin-right-0">
					<div class="col-xs-3 col-sm-2">
						<span class="text-bold"><spring:message
								code="water.acknowledgement.dueDate" text="Due Date" /></span>
					</div>
					<div class="col-xs-2 col-sm-2">
						:<span class="margin-left-20">${command.waterApplicationAcknowledgementDTO.wtConnDueDt}</span>
						
					</div>
					
				</div>
				
				
				  <c:if test="${not empty  command.checkList}">
					<div class="padding-horizontal-10">
						<h4><strong><i><spring:message code="water.acknowledgement.doc.note" text="Note" /></i></strong></h4>
						<table class="table table-striped table-condensed table-bordered" style=" width: 60%;">
							<tbody>

								<c:forEach items="${command.checkList}" var="checklist"
									varStatus="lk">

									<tr>
										<td>${checklist.documentSerialNo}</td>
										<td>${checklist.doc_DESC_ENGL}</td>
										<%-- <c:choose>
											<c:when
												test="${userSession.getCurrent().getLanguageId() eq 1}">
												<td>${checklist.doc_DESC_ENGL}</td>
											</c:when>
											<c:otherwise>
												<td>${checklist.doc_DESC_Mar}</td>
											</c:otherwise>
										</c:choose> --%>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>	
				</c:if>
				<br> 
				
				
				
				 <div class="row margin-bottom-10 margin-left-0 margin-right-0">
					<div class="col-xs-12 col-sm-12 padding-top-5">
						<span class="text-bold line-height-1rem"><spring:message
								code="water.rec.acknowledgement.comp.generate" text="computer generated" /></span>
					</div>
				</div>
				
				</div>
				
				<div class="text-center margin-top-20">
					<button onclick="printContent('receipt')"
						class="btn btn-primary hidden-print">
						<i class="icon-print-2"></i>
						<spring:message code="water.acknowledgement.print" text="Print"></spring:message>
					</button>

					

					<button type="button" class="btn btn-danger hidden-print"
						onclick="window.location.href='AdminHome.html'">

						<spring:message code="water.acknowledgement.close" text="Close"></spring:message>
					</button>
				</div> 
				
			</form>
		</div>
	</div>
	<!-- End of info box -->
</div>

