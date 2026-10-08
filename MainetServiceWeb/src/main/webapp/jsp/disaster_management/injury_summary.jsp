<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/disaster_management/injusrydetails.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">

	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="ComplainRegisterDTO.form.call" text="Close Call" /></strong>
			</h2>
		</div>
		

		<div class="widget-content padding">

			<form:form action="InjuryDetails.html" method="POST"
				commandName="command" class="form-horizontal form"
				name="frmInjuryDetails" id="frmInjuryDetails">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				
			<!-- 	<div
					class="warning-div error-div aaaaaalert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;">
					<i class="fa fa-plus-circle"></i>
				</div> -->
				
				<div class="error-div alert alert-danger alert-dismissible" id="errorDivId" style="display: none;">
<button type="button" class="close" onclick="closeOutErrBox()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	 	<span id="errorId"></span>
</div>
				

				<div class="form-group">
					<apptags:lookupFieldSet baseLookupCode="CMT" hasId="true"
										pathPrefix="complainRegisterDTO.complaintType" 
										hasLookupAlphaNumericSort="true"
										hasSubLookupAlphaNumericSort="true" cssClass="form-control"
										showAll="false" />
				
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label  "
										for="location">
					<spring:message code="ComplainRegisterDTO.location" text="Location" />
					</label>
									<div class="col-sm-4">
										<form:select path="" cssClass="form-control"
											id="location"  data-rule-required="true"  >
											<form:option value="0">
												<spring:message code="Select" text="Select" />
											</form:option>
											<c:forEach items="${locations}" var="loc">
												<form:option value="${loc.locId}">${loc.locNameEng}-${loc.locArea}</form:option>
											</c:forEach>
										</form:select>
									</div>
									
									<apptags:input labelCode="Call No"
										path="complainRegisterDTO.complainNo" cssClass="hasAlphaNumeric"
									 	maxlegnth="50" />
				</div>
				
				
				
				
			<!-- Buttons start -->

		<div class="text-center margin-top-10">
			<%-- <input type="button" onClick="searchHospitalData(this);" value="<spring:message code="bt.search"/>" class="btn btn-success" id="Search">
	 --%>		
 				<button type="button" id="search" class="btn btn-blue-2" onclick = "searchDisasterData()" title="Search">
						<i class="fa fa-search padding-right-5" aria-hidden="true"></i>
						<spring:message code="" text="Search" />
				</button>
			<input type="button" onclick="window.location.href='AdminHome.html'" class="btn btn-danger  hidden-print" value="Back">
		</div>
	
		
			<!-- Table Grid Start  -->
        
         <div class="table-responsive clear">
						<table class="table table-striped table-bordered"
							id="disasterDataTable">
							<thead>
								<tr>
									<th width="5%" align="center"><spring:message
											code="ComplainRegisterDTO.srNo" text="Sr.No" /></th>
									<th width="15%" align="center"><spring:message
											code="ComplainRegisterDTO.callNo" text="Call No" /></th>
									<th width="20%" align="center"><spring:message
											code="ComplainRegisterDTO.callType" text="Call Type" /></th>
									<th width="20%" align="center"><spring:message
											code="ComplainRegisterDTO.callSubtype" text="Call Sub Type" /></th>
									<th width="30%" align="center">
										<spring:message code="ComplainRegisterDTO.description" text="Description" /></th>
									<th width="20%" align="center">
										<spring:message code="ComplainRegisterDTO.status" text="Status" /></th>
									<th width="20%" align="center"><spring:message
											code="ComplainRegisterDTO.action" text="Action" /></th>
										
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${complaints}" var="comp" varStatus="item">
									
								<tr>
								 	<td class="text-center">${item.count}</td>
								   	<td >${comp.complainNo}</td>
									<td>${comp.codDesc}</td>
									<td>${comp.codDesc1}</td>			
									<td>${comp.complaintDescription}</td>
									<td>Open</td>
									<td class="text-center">
											<button type="button" class="btn btn-warning btn-sm"
												title="Edit"
												onclick="modifyInjury('${comp.complainId}','InjuryDetails.html','editDisInjury','E')">
												<i class="fa fa-pencil"></i>
											</button> 
									</td>

														
								</tr>
							</c:forEach>
							</tbody>
							
							</table>
			</div> 
							
			

			</form:form>


		</div>
	</div>
</div>