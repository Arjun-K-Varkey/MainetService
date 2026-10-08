<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<%-- <script type="text/javascript" src="assets/libs/excel-export/jquery.tablesorter.js"></script>
<script type="text/javascript" src="assets/libs/excel-export/jquery.tablesorter.pager.js"></script> --%>

<script>
$(function() {
	/* $(".table").tablesorter().tablesorterPager({
		container: $(".ts-pager"),
		cssGoto  : ".pagenum",
		removeRows: false,
 	});

	var count = $("#report-table thead th").length;
	$('#report-table tfoot tr th.ts-pager').attr('colspan',count); */
	
	$('#report-table').DataTable({
		"language": { "search": "" }, 
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"aaSorting": [],
		columnDefs: [
		  {bSortable: false, targets: [0,3,4,5]}
		]
	});
});
</script>
<style>
	table#report-table thead tr th {
		/* background-color: #f1f1f1 !important; */
	}
	table#report-table thead tr th.sorting,
	table#report-table thead tr th.sorting_asc,
	table#report-table thead tr th.sorting_desc {
		padding-right: 1rem;
	}
.row h3{
	font-size:20pt;
}
.row p{
	font-size: 10pt;
}

.padding-left-10-per {
	padding-left:10%;
		
	
}
.care-report-table-header{
	width:100%;
	margin-left: 2rem;
	padding :0px;
}
.care-report-table-header tr td, .care-report-table-header tr th{
	padding :2px;
	background:none !important;
	text-align:left !important;
}
.logo_heading h3{
	text-align:center;
	padding-bottom:3rem;
}
.widget-content.padding.detail-summary-reports {
	padding: 2rem;
}
.width-6{
	width:6% !important;
}
.width-8{
	width:8% !important;
}
</style>

    <apptags:breadcrumb></apptags:breadcrumb>
    <!-- Start Content here -->
    <div class="content animated slideInDown">
      <div class="widget invoice" id="report-print">
        <div class="widget-content padding detail-summary-reports">
          <form action="" method="get">
          
           <div class="row margin-top-5 clear">
					<div class="col-sm-12 col-xs-12 text-right">
					<%-- <p>
							<spring:message code="care.reports.generatedDate" text="Generated Date " />: ${command.complaintReport.dateTime}</p> --%>
					</div>
				</div>
            <div class="row">
              	<div class="col-xs-1 col-sm-1 org-logo-div"><img src="${userSession.orgLogoPath}"></div> 
              	<div class="col-xs-11 col-sm-11"> 
		              <div class="text-center org-name-div">
		                <c:choose>
			                <c:when test="${userSession.languageId eq 1}">
			                	<h3 class="margin-bottom-0">${userSession.organisation.ONlsOrgname}</h3>
			                </c:when>
			                <c:otherwise>
			                	<h3 class="margin-bottom-0">${userSession.organisation.oNlsOrgnameMar}</h3>
			                </c:otherwise>
		               </c:choose> 
		                <p class="excel-title" style="font-size: 12pt;">${command.complaintReport.title}</p>
		                <%-- <p><spring:message code="care.reports.fromDate" text="From Date"/>: ${command.complaintReport.fromDate} &nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="care.reports.toDate" text="To Date"/>: ${command.complaintReport.toDate}</p> --%>
		                 
		              </div>
             <!-- D#127292 -->
					<c:choose>
						<c:when test="${command.kdmcEnv eq 'Y'}">
							<%-- <div class="col-xs-3 text-right"></div>--%>
						</c:when>
						<c:otherwise>
							<div class="col-xs-3 text-right"><img src="${userSession.orgLogoPath}" width="80"></div>
						</c:otherwise>
					</c:choose>
            
     		<%--   <div class="row margin-top-30 padding-left-10-per">
            	
            	<div class="col-xs-2 col-sm-2 control-label"><spring:message code="care.reports.fromDate" text="From Date"/></div>
            	<div class="col-xs-3 col-sm-3"> ${command.complaintReport.fromDate}</div>
            	<div class="col-xs-3 col-sm-2 control-label"><spring:message code="care.reports.toDate" text="To Date"/></div>
            	<div class="col-xs-4 col-sm-2"> ${command.complaintReport.toDate}</div>
            	
            </div>
            <div class="row margin-top-10 padding-left-10-per">
            	
            	<div class="col-xs-2 col-sm-2 control-label"><spring:message code="care.reports.generatedBy" text="Generated By"/></div>
            	<div class="col-xs-3 col-sm-3"> ${userSession.employee.empname} ${userSession.employee.empmname} ${userSession.employee.emplname}</div>
            	<div class="col-xs-3 col-sm-2 control-label"><spring:message code="care.reports.generatedDate" text="Generated Date"/></div>
            	<div class="col-xs-4 col-sm-2"> ${command.complaintReport.dateTime}</div>
            	
            </div> --%>
           			 <div class="col-xs-12 col-sm-12 label-text">
				 		<table class="care-report-table-header care-exl-export margin-left-25" align="center">
							<tr>
								<th colspan="2"><spring:message code="care.reports.fromDate" text="From Date"/></th>
								<td colspan="2">  : &nbsp ${command.complaintReport.fromDate}</td>
								<th colspan="2"><spring:message code="care.reports.toDate" text="To Date"/></th>
								<td colspan="2">  : &nbsp  ${command.complaintReport.toDate}</td>
							</tr>
							<tr>
								<th colspan="2"><spring:message code="care.reports.generatedBy" text="Generated By"/></th>
								<td colspan="2">  : &nbsp ${userSession.employee.empname} ${userSession.employee.empmname} ${userSession.employee.emplname}</td>
								<th colspan="2"><spring:message code="care.reports.generatedDate" text="Generated Date"/></th>
								<td colspan="2">  : &nbsp ${command.complaintReport.dateTime}</td>
							</tr>
							<tr>
								<th colspan="2"><spring:message code="care.reports.applicantFeedback" text="Applicant Feedback"/></th>
								<td colspan="2">  : &nbsp 
								<c:choose>
									<c:when test="${command.careReportRequest.feedbackRating == 1}">
										<spring:message code="care.reports.feedback.dissatisfied" text="Dissatisfied"/>
									</c:when>
									<c:when test="${command.careReportRequest.feedbackRating == 2}">
										<spring:message code="care.reports.feedback.dislike" text="Dislike"/>
									</c:when>
									<c:when test="${command.careReportRequest.feedbackRating == 3}">
										<spring:message code="care.reports.feedback.itsOk" text="Its Ok"/>
									</c:when>
									<c:when test="${command.careReportRequest.feedbackRating == 4}">
										<spring:message code="care.reports.feedback.likedIt" text="Liked It"/>
									</c:when>
									<c:when test="${command.careReportRequest.feedbackRating == 5}">
										<spring:message code="care.reports.feedback.satisfied" text="Satisfied"/>
									</c:when>
									<c:otherwise>
										<spring:message code="care.all=All" text="All"/>
									</c:otherwise>
								</c:choose>
								</td>
								<th colspan="2"></th>
								<td colspan="2"></td>
							</tr>
						</table>
					</div>	
				</div>
			</div>	
            <div id="export-excel">
            <div class="table-responsive margin-top-10">
              <table class="table table-bordered care-exl-export" id="report-table">
              <div class="excel-title text-center margin-top-10" id='tlExcel' style="display:none;">${command.complaintReport.title}</div>
              		<thead>
	              		<tr >
	              		  <th class="text-center width-5" style="vertical-align: middle;"><spring:message code="care.reports.SrNo" text="Sr. No."/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.tokenNo" text="Token No."/></th>
		                  <th class="text-center width-8%"><spring:message code="care.reports.feedbackDate" text="Feedback Date"/></th>
		                  <th class="text-center" width="8%"><spring:message code="care.reports.complaintType" text="Complaint Type"/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.complaintSubType" text="Complaint SubType"/></th>
		                  <th class="text-center width-8%"><spring:message code="care.reports.applicantFeedbackRatings" text="Applicant Feedback Ratings"/></th>
		                  <th class="text-center" width="8%"><spring:message code="care.reports.applicantFeedback" text="Applicant Feedback"/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.ratingsContent" text="Applicant Feedback Comments"/></th>
		                   <th class="text-center" width="9%"><spring:message code="care.reports.division" text="Division"/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.ward" text="Ward"/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.zone" text="zone"/></th>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.applicantEmail" text="Applicant Email"/></th>
		                  <th class="text-center width-8"><spring:message code="care.reports.applicantMobileNo" text="Applicant Mobile No."/></th>  
		                  <th class="text-center" width="9%"><spring:message code="care.reports.resolvedBy" text="Resolved By"/></th>
		                  <c:if test="${command.kdmcEnv ne 'Y'}">
							<th class="text-center" width="9%"><spring:message code="care.reports.totalfeedback" text="total feedback in (%)"/></th>
						  </c:if>
		                </tr>
	                </thead>
	                 <%-- <tfoot>
						<tr class="print-remove">
							<th class="ts-pager form-horizontal">
								<div class="btn-group">
									<button type="button" class="btn first"><i class="fa fa-step-backward" aria-hidden="true"></i></button>
									<button type="button" class="btn prev"><i class="fa fa-arrow-left" aria-hidden="true"></i></button>
								</div>
								<span class="pagedisplay"></span> this can be any element, including an input
								<div class="btn-group">
									<button type="button" class="btn next"><i class="fa fa-arrow-right" aria-hidden="true"></i></button>
									<button type="button" class="btn last"><i class="fa fa-step-forward" aria-hidden="true"></i></button>
								</div>
								<select class="pagesize input-mini form-control" title="Select page size">
									<option selected="selected" value="10" class="form-control">10</option>
									<option value="20">20</option>
									<option value="30">30</option>
									<option value="all">All Records</option>
								</select>
								<select class="pagenum input-mini form-control" title="Select page number"></select>
							</th>
						</tr>
					</tfoot> --%>
	                
	                <tbody>
							<c:set var="totalApplicantFeedback" value="0"></c:set>
							<c:set var="totalfeedback_" value="0"></c:set>
     				<c:forEach items="${command.complaintReport.complaints}" var="complaint" varStatus="status" >
     					<tr>
     					    <td style="text-align: center;">${status.index + 1}</td>
     					    <td style="text-align: center;"><c:out value="${complaint.complaintId}"></c:out></td>
     					 	<td style="text-align: center;">
     					 	 <fmt:parseDate pattern="dd/mm/yyyy" value="${complaint.feedbackDate}" var="date" />
     					 		 <fmt:formatDate type="date"   var="marDate"  value="${date}" pattern="yyyymmdd" />
     					 		<span style="display:none"> ${marDate} </span>
     					 		<c:out value="${complaint.feedbackDate}"></c:out>
     					 	</td>
     					 	<td style="text-align: center;"><c:out value="${complaint.complaintType}"></c:out></td>
     					 	<td style="text-align: center;"><c:out value="${complaint.complaintSubType}"></c:out></td>
     					 	<td style="text-align: center;"><c:out value="${complaint.feedback}"></c:out></td>
							<td><c:if test="${complaint.feedback == 1}">
												<span class="star"
													style="font-size: 14px; font-weight: bold;">&#9733;&nbsp;</span>
											</c:if> <c:if test="${complaint.feedback == 2}">
												<span class="star"
													style="font-size: 14px; font-weight: bold;">&#9733;&#9733;&nbsp;</span>
											</c:if> <c:if test="${complaint.feedback == 3}">
												<span class="star"
													style="font-size: 14px; font-weight: bold;">&#9733;&#9733;&#9733;&nbsp;</span>
											</c:if> <c:if test="${complaint.feedback == 4}">
												<span class="star"
													style="font-size: 14px; font-weight: bold;">&#9733;&#9733;&#9733;&#9733;&nbsp;</span>
											</c:if> <c:if test="${complaint.feedback == 5}">
												<span class="star"
													style="font-size: 14px; font-weight: bold;">&#9733;&#9733;&#9733;&#9733;&#9733;&nbsp;</span>
											</c:if> <c:out value="${complaint.feedbackRatings}"></c:out></td>
										<td style="text-align: center;"><c:out value="${complaint.ratingsContent}"></c:out></td>
							<td style="text-align: center;"><c:out value="${complaint.division}"></c:out></td>
							<td style="text-align: center;"><c:out value="${complaint.ward}"></c:out></td>
							<td style="text-align: center;"><c:out value="${complaint.zone}"></c:out></td>
     					 	<td><c:out value="${complaint.apaEmail}"></c:out></td>
     					 	<td style="text-align: center;"><c:out value="${complaint.apaMobilno}"></c:out></td>
							<td style="text-align: center;"><c:out value="${complaint.resolvedBy}"></c:out></td>
							
     					 	<c:set var="totalApplicantFeedback" value="${totalApplicantFeedback + complaint.feedback}"></c:set></td>
     					   <c:if test="${command.kdmcEnv ne 'Y'}">
							<td class="text-center"><c:out value="${complaint.totalfeedback}"/>
     					   <c:set var="totalfeedback_" value="${totalfeedback_ + complaint.totalfeedback}"></c:set></td> 
						  </c:if>
  					   </tr>
     				</c:forEach>
     				</tbody>
     				<c:if test="${command.kdmcEnv ne 'Y'}">
						<tfoot>
	                   		<tr class="text-bold totalCount">
									<th ></th>
									<th></th>
									<th></th>
									<th></th>
									<th class="text-center"><spring:message code="care.reports.total" text="Total"/></th>
									<th class="text-center"><c:out value="${totalApplicantFeedback}"/></th>
									<th></th>
							</tr>  
						</tfoot>
					</c:if>
	                
              </table>
            </div>
            </div>
            <div class="text-center margin-top-10 remove-btn">
              <button onclick="PrintDiv('${command.complaintReport.title}');" class="btn btn-success hidden-print" type="button"><i class="fa fa-print"></i>
              	<spring:message code="care.receipt.print" text="Print"/>
              </button>
              <button type="button" class="btn btn-warning hidden-print" onclick="javascript:openRelatedForm('GrievanceReport.html?grievanceFeedback','this');"><spring:message code="care.back" text="Back"/></button>
            </div> 
          </form>
        </div>
      </div>
    </div>