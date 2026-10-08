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
<script type="text/javascript" src="js/care/complaint-reopen-feedback.js"></script>

<jsp:useBean id="marathiConvert" class="com.abm.mainet.common.utility.Utility"></jsp:useBean>
<style>
#receipt .table tr td, #receipt .table tr th {
	font-size: 1em !important;
	padding: 10px 10px !important;
	text-align: left !important
}
.cert-outer-div {
	border: 3px solid #000000;
	padding: 1.5rem;
}
.width-15 {
	width: 15rem !important;
}
.width-25 {
	width: 25rem !important;
}
</style>
<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content animated slideInDown">
	<!-- Your awesome content goes here -->
	<div class="widget" id="receipt">
		<div class="widget-content padding">

			<form action="" method="get" class="complaintReceipt">
				<div id="receipt">
					<%-- <div id="background"
						style="position: absolute; z-index: 1; background: transparent; display: block; min-height: 50%; min-width: 50%; color: yellow; top: 390px; left: 250px;">
						<p id="bg-text"
							style="color: lightgrey; font-size: 50px; transform: rotate(300deg); -webkit-transform: rotate(300deg);">
							<spring:message code="care.cm"
								text="Mukhyamantri Ward Kaaryalaya" />
						</p>
					</div> --%>
					
				<!-- Department copy -->
				<div class="cert-outer-div complaint-receipt-ack margin-top-10">
				<table class="table table-bordered">
						<tr>
				<div class="row">
						<div class="col-xs-12 text-right">
							<p>
								<spring:message code="care.departmentCopy"
										text="Department Copy" />
							</p>
						</div>
								<div class="col-xs-12 margin-top-20">
									<div class="col-xs-2 text-left">
									  <img width="80" src="${userSession.orgLogoPath}">
									</div>
									<div class="col-xs-8 text-center">
									<label class="margin-bottom-0"
										style="font-size: 24px; font-weight: 400;">${complaintAcknowledgementModel.organizationName}</label>
									<p>
										<spring:message code="care.complaint.receipt"
											text="Complaint Acknowledgement Receipt" />
									</p>
									</div>
										<div class="col-xs-2 text-right">
									   
									</div>
								</div>

					</div>
					</tr>
					<tr>
								<div class="row margin-top-30">
								<div class="col-xs-3 text-right">
									<spring:message code="care.receipt.tokenno" text="Complaint Number :" />
								</div>
								<div class="col-xs-3">
									${complaintAcknowledgementModel.tokenNumber}
									<%--D#126901 <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.tokenNumber}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.tokenNumber)}
										</c:otherwise>
									</c:choose> --%>
								</div>
								
								
								<c:if test="${not empty complaintAcknowledgementModel.extReferenceNo && complaintAcknowledgementModel.complaintDeptCode eq 'AS'}">
									<div class="col-xs-3 text-right">
										<spring:message code="care.propertyLbl" text="Property No :" />
									</div> 
									<div class="col-xs-3">${complaintAcknowledgementModel.extReferenceNo}</div>
								</c:if>
								
								<c:if test="${not empty complaintAcknowledgementModel.extReferenceNo && complaintAcknowledgementModel.complaintDeptCode eq 'WT'}">
									<div class="col-xs-3 text-right">
										<spring:message code="care.waterLbl" text="Connection No :" />
									</div> 
									<div class="col-xs-3">${complaintAcknowledgementModel.extReferenceNo}</div>
								</c:if>
								
								
								
								<%-- <div class="col-xs-3">${complaintAcknowledgementModel.tokenNumber}</div> --%>
								<%-- <div class="col-xs-3 text-right">
									<spring:message code="care.appNo" text="Application No :" />
								</div>
								<div class="col-xs-3">${applicationId}</div> --%>
							</div>
							<div class="row margin-top-10">

								<div class="col-xs-3 text-right">
									<spring:message code="care.dateAndTiem" text="Date & Time :" />
								</div>
								<div class="col-xs-3">
									${complaintAcknowledgementModel.formattedDate}
									<%--D#126901 <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.formattedDate}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.formattedDate)}
										</c:otherwise>
									</c:choose> --%>
								</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.applicant.name"
										text="Applicant Name :" />
								</div>
								<div class="col-xs-3">
									${complaintAcknowledgementModel.complainantName}
									<%--D#126901 <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.complainantName}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.complainantName)}
										</c:otherwise>
									</c:choose> --%>
								</div>
							</div>
							<div class="row margin-top-10">

								<div class="col-xs-3 text-right">
									<spring:message code="care.receipt.mobilenumber"
										text="Applicant Mobile Number :" />
								</div>
								<div class="col-xs-3">
									${complaintAcknowledgementModel.complainantMobileNo}
									<%--D#126901 <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.complainantMobileNo}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.complainantMobileNo)}
										</c:otherwise>
									</c:choose> --%>
								</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.label.complaintType"
										text="Complaint Type :" />
								</div>
								<div class="col-xs-3">${complaintAcknowledgementModel.department}</div>
							</div>
							<div class="row margin-top-10">

								<div class="col-xs-3 text-right">
									<spring:message code="care.complaint.subtype"
										text="Complaint Sub Type :" />
								</div>
								<div class="col-xs-3">${complaintAcknowledgementModel.complaintSubType}</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.complaint.location"
										text="Location :" />
								</div>
								<%-- <div class="col-xs-3">${complaintAcknowledgementModel.ward}</div> --%>
								<c:choose>
									<c:when test="${kdmcEnv eq 'Y'}">
										<div class="col-xs-3">${complaintAcknowledgementModel.landmark}</div>
									</c:when>
									<c:otherwise>
										<div class="col-xs-3">${complaintAcknowledgementModel.ward}</div>
									</c:otherwise>
								</c:choose>


							</div>
							<div class="row margin-top-10">
								<div class="col-xs-3 text-right">
									<spring:message code="care.complaint.description"
										text="Description :" />
								</div>
								<div class="col-xs-3">
									<c:out value="${complaintAcknowledgementModel.description}" />
								</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.label.status" text="Status :" />
								</div>
								<div class="col-xs-3">
									<c:if
										test="${complaintAcknowledgementModel.status eq 'CLOSED'}">
										<span class="text-green-1"> <spring:message
												code="care.status.closed" text="Closed" />
										</span>
									</c:if>
									<c:if
										test="${complaintAcknowledgementModel.status eq 'EXPIRED'}">
										<span class="text-red-1"> <spring:message
												code="care.status.expired" text="Expired" />
										</span>
									</c:if>
									<c:if
										test="${complaintAcknowledgementModel.status eq 'PENDING'}">
										<span class="text-orange-1"> <spring:message
												code="care.status.pending" text="Pending" />
										</span>
									</c:if>
								</div>
							</div>
						
							<div class="row margin-top-10">
								<c:if test="${complaintAcknowledgementModel.wardValue1 ne null}">
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward1Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward1Desc}</div>		
								</c:if>
								
								<c:if test="${complaintAcknowledgementModel.wardValue2 ne null}">
									
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward2Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward2Desc}</div>		
								</c:if>
							</div>

							
							<div class="row margin-top-10">
								<c:if test="${complaintAcknowledgementModel.wardValue3 ne null}">
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward3Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward3Desc}</div>		
								</c:if>
							</div>
							
							<h5>
								<spring:message code="care.escalation.detail"
									text="Escalation Details" />
							</h5>
							<div class="table-responsive margin-top-10">
								<table class="table table-bordered table-condensed">
									<tr>
										<th><spring:message code="care.level" text="Level" /></th>
										<th class="width-15"><spring:message code="care.duration"
												text="Duration (Day)" /></th>
										<th><spring:message code="care.employee.name"
												text="Employee Name" /></th>
										<th><spring:message code="care.designation"
												text="Designation" /></th>
										<th class="width-25"><spring:message code="care.receipt.department2"
												text="Department" /></th>
										<%-- <th><spring:message code="care.email" text="Email" /></th> --%>
									</tr>
									<c:forEach
										items="${complaintAcknowledgementModel.escalationDetailsList}"
										var="requestLists" varStatus="status">
										<tr>
											<td>${requestLists.level}</td>
											<%--D#126901 <td>${marathiConvert.convertToRegional("marathi",requestLists.level)}</td> --%>
											<td>
												${requestLists.sla}
												<%-- <c:choose>
													<c:when test="${userSession.languageId eq 1}">
														${requestLists.sla}
													</c:when>
													<c:otherwise>
														${marathiConvert.convertToRegional("marathi",requestLists.sla)}
													</c:otherwise>
												</c:choose> --%>
											</td>
											<td><c:out value="${requestLists.empName}">
												</c:out></td>
											<td><c:out value="${requestLists.designation}">
												</c:out></td>
											<td><c:out value="${requestLists.department}">
												</c:out></td>
											<%-- <td><c:out value="${requestLists.email}">
												</c:out></td> --%>
										</tr>
									</c:forEach>
								</table>

							</div>
							</tr>
							</table>
						</div>
	
				<%-- Applicant copy  --%>
				<div class="cert-outer-div complaint-receipt-ack margin-top-10">
				<div class="row margin-top-20">
					<div class="col-xs-12 text-right">
							<p>
								<spring:message code="care.applicantCopy" text="Applicant Copy" />
							</p>
					</div>
								<div class="col-xs-12 margin-top-20">
									<div class="col-xs-2 text-left">
									  <img width="80" src="${userSession.orgLogoPath}">
									</div>
									<div class="col-xs-8 text-center">
									<label class="margin-bottom-0"
										style="font-size: 24px; font-weight: 400;">${complaintAcknowledgementModel.organizationName}</label>
									<p>
										<spring:message code="care.complaint.receipt"
											text="Complaint Acknowledgement Receipt" />
									</p>
									</div>
									<div class="col-xs-2 text-right">
									   
									</div>
								</div>

					</div>
					
					<div class="row margin-top-30">
								<div class="col-xs-3 text-right">
									<spring:message code="care.receipt.tokenno" text="Complaint Number :" />
								</div>
						<div class="col-xs-3">
							${complaintAcknowledgementModel.tokenNumber}
							<%--D#126901 <c:choose>
								<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.tokenNumber}
										</c:when>
								<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.tokenNumber)}
										</c:otherwise>
							</c:choose> --%>
						</div>
						<%-- <div class="col-xs-3">${complaintAcknowledgementModel.tokenNumber}</div> --%>
								<%-- <div class="col-xs-3 text-right">
									<spring:message code="care.appNo" text="Application No :" />
								</div>
								<div class="col-xs-3">${applicationId}</div> --%>
								
							<c:if test="${not empty complaintAcknowledgementModel.extReferenceNo && complaintAcknowledgementModel.complaintDeptCode eq 'AS'}">
									<div class="col-xs-3 text-right">
										<spring:message code="care.propertyLbl" text="Property No :" />
									</div> 
									<div class="col-xs-3">${complaintAcknowledgementModel.extReferenceNo}</div>
								</c:if>
								
								<c:if test="${not empty complaintAcknowledgementModel.extReferenceNo && complaintAcknowledgementModel.complaintDeptCode eq 'WT'}">
									<div class="col-xs-3 text-right">
										<spring:message code="care.waterLbl" text="Connection No :" />
									</div> 
									<div class="col-xs-3">${complaintAcknowledgementModel.extReferenceNo}</div>
								</c:if>
									
							</div>
							
							
								
							<div class="row margin-top-10">

								<div class="col-xs-3 text-right">
									<spring:message code="care.dateAndTiem" text="Date & Time :" />
								</div>
							<div class="col-xs-3">
								${complaintAcknowledgementModel.formattedDate}
								<%-- <c:choose>
									<c:when test="${userSession.languageId eq 1}">
										${complaintAcknowledgementModel.formattedDate}
									</c:when>
									<c:otherwise>
										${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.formattedDate)}
									</c:otherwise>
								</c:choose> --%>
							</div>
						<div class="col-xs-3 text-right">
									<spring:message code="care.applicant.name"
										text="Applicant Name :" />
								</div>
						<div class="col-xs-3">
							${complaintAcknowledgementModel.complainantName}
							<%-- <c:choose>
								<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.complainantName}
										</c:when>
								<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.complainantName)}
										</c:otherwise>
							</c:choose> --%>
						</div>
						</div>
							<div class="row margin-top-10">

								<div class="col-xs-3 text-right">
									<spring:message code="care.receipt.mobilenumber"
										text="Applicant Mobile Number :" />
								</div>
								<div class="col-xs-3">
									${complaintAcknowledgementModel.complainantMobileNo}
									<%-- <c:choose>
										<c:when test="${userSession.languageId eq 1}">
											${complaintAcknowledgementModel.complainantMobileNo}
										</c:when>
										<c:otherwise>
											${marathiConvert.convertToRegional("marathi",complaintAcknowledgementModel.complainantMobileNo)}
										</c:otherwise>
									</c:choose> --%>
								</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.label.complaintType"
										text="Complaint Type :" />
								</div>
								<div class="col-xs-3">${complaintAcknowledgementModel.department}</div>
							</div>
					<div class="row margin-top-10">

						<div class="col-xs-3 text-right">
							<spring:message code="care.complaint.subtype"
								text="Complaint Sub Type :" />
						</div>
						<div class="col-xs-3">${complaintAcknowledgementModel.complaintSubType}</div>
						<div class="col-xs-3 text-right">
							<spring:message code="care.complaint.location" text="Location :" />
						</div>
						<%-- <div class="col-xs-3">${complaintAcknowledgementModel.ward}</div> --%>
						<c:choose>
							<c:when test="${kdmcEnv eq 'Y'}">
								<div class="col-xs-3">${complaintAcknowledgementModel.landmark}</div>
							</c:when>
							<c:otherwise>
								<div class="col-xs-3">${complaintAcknowledgementModel.ward}</div>
							</c:otherwise>
						</c:choose>


					</div>
					<div class="row margin-top-10">
								<div class="col-xs-3 text-right">
									<spring:message code="care.complaint.description"
										text="Description :" />
								</div>
								<div class="col-xs-3">
									<c:out value="${complaintAcknowledgementModel.description}" />
								</div>
								<div class="col-xs-3 text-right">
									<spring:message code="care.label.status" text="Status :" />
								</div>
								<div class="col-xs-3">
									<c:if
										test="${complaintAcknowledgementModel.status eq 'CLOSED'}">
										<span class="text-green-1"> <spring:message
												code="care.status.closed" text="Closed" />
										</span>
									</c:if>
									<c:if
										test="${complaintAcknowledgementModel.status eq 'EXPIRED'}">
										<span class="text-red-1"> <spring:message
												code="care.status.expired" text="Expired" />
										</span>
									</c:if>
									<c:if
										test="${complaintAcknowledgementModel.status eq 'PENDING'}">
										<span class="text-orange-1"> <spring:message
												code="care.status.pending" text="Pending" />
										</span>
									</c:if>
								</div>



							</div>
							
							<div class="row margin-top-10">
								<c:if test="${complaintAcknowledgementModel.wardValue1 ne null}">
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward1Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward1Desc}</div>		
								</c:if>
								
								<c:if test="${complaintAcknowledgementModel.wardValue2 ne null}">
									
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward2Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward2Desc}</div>		
								</c:if>
							</div>

							
							<div class="row margin-top-10">
								<c:if test="${complaintAcknowledgementModel.wardValue3 ne null}">
									
									<div class="col-xs-3 text-right">
										<c:out value="${complaintAcknowledgementModel.ward3Label} " />:
									</div>
									<div class="col-xs-3">${complaintAcknowledgementModel.ward3Desc}</div>		
								</c:if>
							</div>

							<h5>
								<spring:message code="care.escalation.detail"
									text="Escalation Details" />
							</h5>
							<div class="table-responsive margin-top-10">
								<table class="table table-bordered table-condensed">
									<tr>
										<th><spring:message code="care.level" text="Level" /></th>
										<th class="width-15"><spring:message code="care.duration"
												text="Duration (Day)" /></th>
										<th><spring:message code="care.employee.name"
												text="Employee Name" /></th>
										<th><spring:message code="care.designation"
												text="Designation" /></th>
										<th class="width-25"><spring:message code="care.receipt.department2"
												text="Department" /></th>
										<%-- <th><spring:message code="care.email" text="Email" /></th> --%>
									</tr>
									<c:forEach
										items="${complaintAcknowledgementModel.escalationDetailsList}"
										var="requestLists" varStatus="status">
										<tr>
											<td><c:out value="${requestLists.level}">
												</c:out></td>
											<td>
												${requestLists.sla}
												<%-- <c:choose>
													<c:when test="${userSession.languageId eq 1}">
														${requestLists.sla}
													</c:when>
													<c:otherwise>
														${marathiConvert.convertToRegional("marathi",requestLists.sla)}
													</c:otherwise>
												</c:choose> --%>
											</td>
											<td><c:out value="${requestLists.empName}">
												</c:out></td>
											<td><c:out value="${requestLists.designation}">
												</c:out></td>
											<td><c:out value="${requestLists.department}">
												</c:out></td>
											<%-- <td><c:out value="${requestLists.email}">
												</c:out></td> --%>
										</tr>
									</c:forEach>
								</table>

							</div>
						</div>
						<style>
							@media print {
							.complaint-receipt-ack {
								page-break-after: always;	
							}
							@page {
								  margin-top: 10px;
								}
							}
						</style>
				</div>

				<div class="text-center margin-top-10 hidden-print">
					<button onclick="printContent('${complaintAcknowledgementModel.tokenNumber}')"
						class="btn btn-primary hidden-print">
						<i class="fa fa-print"></i>
						<spring:message code="care.receipt.print" text="Print" />
					</button>
					<c:choose>
						<c:when
							test="${userSession.employee.designation.dsgshortname eq 'OPR'}">
							<button type="button" class="btn btn-danger hidden-print"
								onclick="window.location.href='OperatorDashboardView.html'">
								<spring:message code="care.receipt.back" text="Back" />
							</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn btn-danger hidden-print"
								onclick="window.location.href='AdminHome.html'">
								<spring:message code="care.receipt.back" text="Back" />
							</button>
						</c:otherwise>
					</c:choose>
				</div>

			</form>
		</div>
	</div>
</div>

<!-- <script>
	function printContent(el) {
		var restorepage = document.body.innerHTML;
		var printcontent = document.getElementById(el).innerHTML;
		document.body.innerHTML = printcontent;
		window.print();
		document.body.innerHTML = restorepage;
	}
	$("html, body").animate({
		scrollTop : 0
	}, "slow");
</script> -->