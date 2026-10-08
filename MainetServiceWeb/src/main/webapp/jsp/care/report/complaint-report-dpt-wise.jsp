<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<script type="text/javascript" src="js/care/complaint-report.js"></script>
<script type="text/javascript" src="assets/libs/excel-export/jquery.tablesorter.js"></script>
<script type="text/javascript" src="assets/libs/excel-export/jquery.tablesorter.pager.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script>
$(function() {
	$(document).ready(
			function() {
				$("#report-table").dataTable({
					"oLanguage" : {
						"sSearch" : ""
					},
					"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
					"iDisplayLength" : 5,
					"bInfo" : true,
					"lengthChange" : true,
					"columnDefs": [
						  {targets: 'no-sort', orderable: false}
						]
				});
			});

	
/* 	$(".table").tablesorter().tablesorterPager({
		container: $(".ts-pager"),
		cssGoto  : ".pagenum",
		removeRows: false,
 	});

	var count = $("#report-table thead th").length;
	$('#report-table tfoot tr th.ts-pager').attr('colspan',count); */
  	
});
</script>
<style>
table#report-table thead tr th {
	/*	background-color: #f1f1f1 !important; */
	}
.row h3{
	font-size:20pt;
}
.row p{
	font-size: 10pt;
}
#img{
	background-image: none;
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
#wardapptag{
	display:none;
}
.add-colon::before {
	content: "\003A";
}
.add-colon:empty::before {
	content: '';
}
</style>
<script>
	$(document).ready(function(){
		
		var labWard1= $("label[for='careWardNo1']").text();
		var valWard1=$("label[for='careWardNo1'] + div ").text();
		var labWard2= $("label[for='careWardNo2']").text();
		var valWard2=$("label[for='careWardNo2'] + div").text();
		var labWard3= $("label[for='careWardNo3']").text();
		var valWard3=$("label[for='careWardNo3'] + div").text();

		$('#ward1label').text(labWard1);
		$('#ward1value').html(valWard1);
		$('#ward2label').text(labWard2);
		$('#ward2value').html(valWard2);
		$('#ward3label').text(labWard3);
		$('#ward3value').html(valWard3);
		
		var wardLabel = document.getElementById("ward2label");

		var wardValue = wardLabel ? wardLabel.innerText.trim() : "";
		
		if(!wardValue){
			$('#ward2labeltd').hide();
		}else{
			$('#ward2labeltd').show();
		}
	})
</script>

    <apptags:breadcrumb></apptags:breadcrumb>
    <!-- Start Content here -->
    <div class="content animated slideInDown">
      <div class="widget invoice" id="report-print">
        <div class="widget-content padding detail-summary-reports">
          <form:form action="" method="get">
          
          	<div class="row margin-top-5 clear">
					<div class="col-sm-12 col-xs-12 text-right">
					<%-- <p>
					<spring:message code="care.reports.generatedDate" text="Generated Date"/>: ${command.complaintReport.dateTime}</p> --%>
					</div>
					<div id="wardapptag" style="display:none">
						<apptags:lookupFieldSet baseLookupCode="${command.prefixName}" hasId="true" pathPrefix="careReportRequest.careWardNo" showOnlyLabel="true"/>
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
	                 <%-- <p><spring:message code="care.reports.fromDate" text="From Date"/>: ${command.complaintReport.fromDate} &nbsp;&nbsp;<spring:message code="care.reports.toDate" text="To Date"/>: ${command.complaintReport.toDate}</p>
	                 <p><spring:message code="care.reports.department" text="Department"/>: ${command.complaintReport.departmentName}</p> --%>
	              </div>
			  <!-- D#127292 -->
					<c:choose>
						<c:when test="${command.kdmcEnv eq 'Y'}">
							<%--<div class="col-xs-3 text-right"></div>--%>
						</c:when>
						<c:otherwise>
							<div class="col-xs-3 text-right"><img src="${userSession.orgLogoPath}" width="80"></div>
						</c:otherwise>
					</c:choose>
            
			<%--  <div class="row margin-top-30 padding-left-10-per">
            	
            	<div class="col-xs-3 col-sm-3 control-label"><spring:message code="care.reports.fromDate" text="From Date"/></div>
            	<div class="col-xs-3 col-sm-3">${command.complaintReport.fromDate}</div>
            	<div class="col-xs-3 col-sm-3 control-label"><spring:message code="care.reports.toDate" text="To Date"/></div>
            	<div class="col-xs-3 col-sm-2">${command.complaintReport.toDate}</div>
            	
            </div>
            <div class="row margin-top-10 padding-left-10-per">
            	
            	<div class="col-xs-3 col-sm-3 control-label"><spring:message code="care.reports.generatedBy" text="Generated By"/></div>
            	<div class="col-xs-3 col-sm-3">${userSession.employee.empname} ${userSession.employee.empmname} ${userSession.employee.emplname}</div>
            	<div class="col-xs-3 col-sm-3 control-label"><spring:message code="care.reports.generatedDate" text="Generated Date"/></div>
            	<div class="col-xs-3 col-sm-2">${command.complaintReport.dateTime}</div>
            	
            </div>
            <div class="row margin-top-10 padding-left-10-per">
            	
            	<div class="col-xs-3 col-sm-3 control-label"><spring:message code="care.reports.department" text="Department"/></div>
            	<div class="col-xs-3 col-sm-3">${command.complaintReport.departmentName}</div>
            	
            	
            </div>  --%>
     				<div class="col-xs-12 col-sm-12 label-text">
		            	<table class="care-report-table-header care-exl-export margin-left-25" align="center">
							<tr>
								<th colspan="4"><spring:message code="care.reports.fromDate" text="From Date"/></th>
								<td colspan="3" class="add-colon"> ${command.complaintReport.fromDate}</td>
								<th colspan="3"><spring:message code="care.reports.toDate" text="To Date"/></th>
								<td colspan="3" class="add-colon"> ${command.complaintReport.toDate}</td>
								
								<c:if test="${command.complaintReport.empName ne null}">								
								<th colspan="3"><spring:message code="care.employeename" text="Employee Name"/></th>
								<td colspan="3" class="add-colon"> ${command.complaintReport.empName}</td>
								</c:if>
								
							</tr>
							<tr>
								<th colspan="4"><spring:message code="care.reports.generatedBy" text="Generated By"/></th>
								<td colspan="3" class="add-colon"> ${userSession.employee.empname} ${userSession.employee.empmname} ${userSession.employee.emplname}</td>
								<th colspan="3"><spring:message code="care.reports.generatedDate" text="Generated Date"/></th>
								<td colspan="3" class="add-colon"> ${command.complaintReport.dateTime}</td>
							</tr>
							<tr>
								<th colspan="4"><spring:message code="care.reports.department" text="Department"/></th>
								<td colspan="3" class="add-colon"> ${command.complaintReport.departmentName}</td>
								<th colspan="3"><spring:message code="care.reports.status" text="Status"/></th>
								<td colspan="3" class="add-colon">
								<c:choose>
					              			<c:when test="${command.complaintReport.showStatus}">
					              				<c:out value="${command.complaintReport.status}"></c:out>
					              			</c:when>
					              			<c:otherwise>
					              				<c:set var = "statusString" value = "${command.complaintReport.status}"/>
					              				<spring:message code="care.status.${fn:toLowerCase(statusString)}"/>
					              			</c:otherwise>
					              		</c:choose>
					             </td>
							</tr>
							<tr>
									<c:if test="${not empty command.complaintReport.division}">
										<th colspan="4" id="ward1label"></th>
										<td colspan="3" class="add-colon">&nbsp${command.complaintReport.division}</td>
									</c:if>
									
									<c:if test="${empty command.complaintReport.division}">
										<th colspan="4" id="ward1label"></th>
										<td colspan="3" class="add-colon">&nbspAll</td>
									</c:if>

									<c:if test="${not empty command.complaintReport.ward}">
										<th colspan="3" id="ward2label"></th>
										<td colspan="3" class="add-colon">&nbsp${command.complaintReport.ward}</td>
									</c:if>
									
									<c:if test="${empty command.complaintReport.ward}">
										<th colspan="3" id="ward2label"></th>
										<td colspan="3" id="ward2labeltd" class="add-colon">&nbspAll</td>
									</c:if>
									
							</tr> 	
							<tr>
							<c:choose>
										<c:when test="${not empty command.complaintReport.zone}">
											<th colspan="4" id="ward3label"></th>
											<td colspan="3" class="add-colon">&nbsp${command.complaintReport.zone}</td>
										</c:when>
										<c:otherwise>
											<th colspan="4" id="ward3label"></th>
											<td colspan="3" id="ward3value" class="add-colon"></td>
										</c:otherwise>

							</c:choose>	
							</tr>
						</table>
					</div>	
				</div>
       		</div>	
            <div id="export-excel">
            <div class="table-responsive margin-top-10">
              <table class="table table-bordered margin-bottom-10 care-exl-export" id="report-table">
              <div class="excel-title text-center margin-top-10" id='tlExcel'>${command.complaintReport.title}</div>
              		<thead>
	               		<tr>
	               		<th  class="text-center no-sort" width="3%" style="vertical-align: middle;" id="img"><spring:message code="care.reports.SrNo" text="Sr. No."/></th>
	               		 <%-- <c:if test="${command.complaintReport.showDepartment}"> 
		                  	<th class="text-center" width="8%"  data-sortinitialorder="asc"><spring:message code="care.reports.department" text="Department"/></th>
		                  </c:if>  --%>
		                  
		                 <%-- <c:set var="prefix" value="${command.prefixName}"></c:set>
		                  <c:set var="prefixlookUps" value="${command.getLookupLabel(prefix)}"/> 
		                  <c:set var="prefixlookUpsLength" value="${fn:length(prefixlookUps)}"></c:set> --%>
		                  <%-- <apptags:lookupFieldSet cssClass="form-control required-control"
							baseLookupCode="${prefix}" hasId="true" pathPrefix="careReportRequest.codIdOperLevel" 
							showAll="false"  hasTableForm="true" showData="false"/> 
		                  --%>
		                 <th class="text-center"  width="8%"  data-sortinitialorder="asc"><spring:message code="care.skdclCompType" text="Complaint Type"/></th>
		                 <!-- D#111801 -->
		                  <%-- <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.zone" text="Ward karyalaya"/></th>
		                  <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.ward" text="Ward "/></th> --%>
		                  <!-- D#111801 -->
		                  <c:set var="prefix" value="${command.prefixName}"></c:set>
		                  <c:set var="prefixlookUps" value="${command.getLookupLabel(prefix)}"/> 
		                  <c:set var="prefixlookUpsLength" value="${fn:length(prefixlookUps)}"></c:set>
		                  <%-- <apptags:lookupFieldSet cssClass="form-control required-control"
							baseLookupCode="${prefix}" hasId="true" pathPrefix="careReportRequest.codIdOperLevel" 
							showAll="false"  hasTableForm="true" showData="false"/>
		                   --%>
		                  <th class="text-center" width="9%"><spring:message code="care.reports.division" text="Division"/></th>
						  <th class="text-center" width="9%"><spring:message code="care.reports.ward" text="Ward"/></th>
                          <th class="text-center" width="9%"><spring:message code="care.reports.zone" text="zone"/></th> 
		                  <th class="text-center"  width="10%" data-sortinitialorder="asc"><spring:message code="care.reports.complaintSubType" text="Complaint Sub Type"/></th>
		                   <th class="text-center no-sort"  width="8%"  data-sortinitialorder="asc"><spring:message code="care.reports.complaintDesc" text="Complaint Description"/></th>
		                   <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="care.reports.noOfDaysTaken" text="No of days taken"/></th>
		                 <th class="text-center"  width="8%" data-sortinitialorder="asc"><spring:message code="care.reports.tokenNo" text="Complaint Number"/></th>
		                  <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="care.reports.complaintRegistrationDate" text="Complaint Registration Date"/></th>
		                 <th class ="text-center no-sort" width="8%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.applicantName" text="Applicant Name"/></th>
		                 <th class="text-center no-sort"  width="8%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.address" text="Address"/></br>
		                 																		<spring:message code="care.Dept.reports.mobileNumber" text="Mobile Number"/></br>
		                 																		<spring:message code="care.Dept.reports.email" text="Email Id"/></th>
		                 <%--  <th class ="text-center no-sort" width="8%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.mobileNumber" text="Mobile Number"/></th>
		                  <th class ="text-center no-sort" width="5%" data-sortinitialorder="asc"><spring:message code="care.Dept.reports.email" text="Email Id"/></th> --%>
		                   <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="care.reports.status" text="Complaint Status"/></th>
		                   <th class="text-center" width="8%" data-sortinitialorder="asc"><spring:message code="" text="Dept Remark"/></th>
		                  
		                  
		                  
		                  
		                </tr>
	                </thead>
	              <!--   <tfoot>
						<tr class="print-remove">
							<th class="ts-pager form-horizontal">
								<div class="btn-group">
									<button type="button" class="btn first"><i class="fa fa-step-backward" aria-hidden="true"></i></button>
									<button type="button" class="btn prev"><i class="fa fa-arrow-left" aria-hidden="true"></i></button>
								</div>
								<span class="pagedisplay"></span>  <!-- this can be any element, including an input -->
							<!-- 	<div class="btn-group">
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
					</tfoot>  -->
	                <tbody>
     				<c:forEach items="${command.complaintReport.complaints}" var="complaint" varStatus="status">
     					<tr>
     				
     					
     		
     					 	
     					 	<%-- <c:if test="${command.complaintReport.showDepartment}"> 
     					 		<td ><c:out value="${complaint.departmentName}"></c:out></td> 	</c:if> --%>
     					 		<td class="text-center">${status.index + 1}</td>
     					 	<td><c:out value="${complaint.departmentName}"></c:out></td>
     						<td><c:out value="${complaint.division}"></c:out></td>
							<td><c:out value="${complaint.ward}"></c:out></td>
							<td><c:out value="${complaint.zone}"></c:out></td>
                           <%-- <td style="text-align: center;"><c:out value="${complaint.careWardNoEng}"></c:out></td>
     					 	<td><c:out value="${complaint.careWardNoEng1}"></c:out></td> --%>
     		<%-- 			 	<c:if test="${not empty prefix}">
     					 		<c:if test="${prefixlookUpsLength >= 1}">
	     					 		<td>
	     					 			<c:if test="${not empty complaint.careWardNoEng}">
		     					 			<c:out value="${complaint.careWardNoEng}"></c:out>
		     					 		</c:if>
		     					 		<c:if test="${empty complaint.careWardNoEng}">
		     					 			<spring:message code="common.all" text="All" />
		     					 		</c:if>
	     					 		</td>
     					 		</c:if>
	     					 	<c:if test="${prefixlookUpsLength >= 2}">
	     					 		<td>
	     					 			<c:if test="${not empty complaint.careWardNoEng1}">
		     					 			<c:out value="${complaint.careWardNoEng1}"></c:out>
		     					 		</c:if>
		     					 		<c:if test="${empty complaint.careWardNoEng1}">
		     					 			<spring:message code="common.all" text="All" />
		     					 		</c:if>
	     					 		</td>
     					 		</c:if>
	     					 	<c:if test="${prefixlookUpsLength >= 3}">
	     					 		<td>
	     					 			<c:if test="${not empty complaint.codIdOperLevel3}">
		     					 			<c:out value="${complaint.codIdOperLevel3}"></c:out>
		     					 		</c:if>
		     					 		<c:if test="${empty complaint.codIdOperLevel3}">
		     					 			<spring:message code="common.all" text="All" />
		     					 		</c:if>
	     					 		</td>
     					 		</c:if>
     					 		<c:if test="${prefixlookUpsLength >= 4}">
	     					 		<td>
	     					 			<c:if test="${not empty complaint.codIdOperLevel4}">
		     					 			<c:out value="${complaint.codIdOperLevel4}"></c:out>
		     					 		</c:if>
		     					 		<c:if test="${empty complaint.codIdOperLevel4}">
		     					 			<spring:message code="common.all" text="All" />
		     					 		</c:if>
	     					 		</td>
     					 		</c:if>
     					 		<c:if test="${prefixlookUpsLength >= 5}">
	     					 		<td>
	     					 			<c:if test="${not empty complaint.codIdOperLevel5}">
		     					 			<c:out value="${complaint.codIdOperLevel5}"></c:out>
		     					 		</c:if>
		     					 		<c:if test="${empty complaint.codIdOperLevel5}">
		     					 			<spring:message code="common.all" text="All" />
		     					 		</c:if>
	     					 		</td>
     					 		</c:if>
     					 	</c:if> --%>
						
     					 	<td><c:out value="${complaint.comlaintSubType}"></c:out></td>
     					     <td><c:out value="${complaint.complaintDesc}"></c:out></td> 
     					 <%-- 	<td style="text-align: center;"><c:out value="${complaint.slaDuration}"></c:out></td> --%>
     					   <td style="text-align: center;"><c:out value="${complaint.numberOfDay}"></c:out></td>
     					 	<td data-sortinitialorder="desc"><c:out value="${complaint.complaintId}"></c:out></td>
     					 	<td style="text-align: center;">
     					 		<fmt:parseDate pattern="dd/mm/yyyy" value="${complaint.dateOfRequest}" var="date" />
				     			<fmt:formatDate type="date"   var="marDate"  value="${date}" pattern="yyyymmdd" />
				     			<span style="display:none"> ${marDate} </span>
     					 		<c:out value="${complaint.dateOfRequest}"></c:out>
     					 	</td>
     					    <td><c:out value="${complaint.apmName}"></c:out></td>
     					 	
     					 	<%-- <td style="text-align: center;">
     					 	<c:out value="${complaint.apaAreaName}"></c:out>
     					 	<c:out value="${complaint.pincode}"></c:out>
     					 	</td>
     					 	
     					 	<td style="text-align: center;"><c:out value="${complaint.apaMobilno}"></c:out></td>
     					   <td style="text-align: center;"><c:out value="${complaint.apaEmail}"></c:out></td> --%>
     					   <%-- <td>
     					 	<c:out value="${complaint.apaAreaName}"></c:out>
     					 	<c:out value="${complaint.pincode}"></c:out></br>     					 	
     					 	<c:out value="${complaint.apaMobilno}"></c:out></br>
     					    <c:out value="${complaint.apaEmail}"></c:out></td> --%>

										<td><c:out
												value="${complaint.apaAreaName}  ${complaint.pincode}  ${complaint.apaMobilno}  ${complaint.apaEmail}"></c:out>
										</td>

							<td style="text-align: center;"><c:out value="${complaint.status}"></c:out></td>
     					    <td><c:out value="${complaint.deptRemark}"></c:out></td>
     					 	</tr>
      				</c:forEach>
     				</tbody>
     				
               </table>
                
            </div>
            </div>
             <div class="text-center margin-top-10 remove-btn">
              <button onclick="PrintDiv('${command.complaintReport.title}');" class="btn btn-success hidden-print" type="button"><i class="fa fa-print"></i>
              	<spring:message code="care.receipt.print" text="Print"/>
              </button>
              <button type="button" class="btn btn-warning hidden-print" onclick="javascript:openRelatedForm('GrievanceReport.html?grievanceDeptWise','this');"><spring:message code="care.back" text="Back"/></button>
            </div> 
          </form:form>
          <style>
				@media print {
					@page {
						size: A4 landscape;
						margin-top: 1rem;
					}
					.table-responsive {
					    overflow-x: unset !important;
					    margin-left: -2rem !important;
					}
					.table>thead>tr>th{
					    font-size: 0.8em;
					}
					table.table>tbody>tr>td{
						font-size: 0.65em;
					}
				}
			</style>
        </div>
      </div>
    </div>    