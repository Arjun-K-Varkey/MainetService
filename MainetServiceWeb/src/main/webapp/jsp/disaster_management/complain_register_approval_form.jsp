<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>


<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/disaster_management/departmentalScrutiny.js"></script>

<!-- <style>
	::-webkit-datetime-edit-year-field:not([aria-valuenow]),
	::-webkit-datetime-edit-month-field:not([aria-valuenow]),
	::-webkit-datetime-edit-day-field:not([aria-valuenow]) {
    	color: transparent;
	}
	
</style> -->

<%-- <apptags:breadcrumb></apptags:breadcrumb> --%>

	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="ComplainRegisterDTO.form.name" text="Complain Register" /></strong>
			</h2>
		</div> 

		<div class="widget-content padding">

			<form:form action="DepartmentalScrutiny.html" method="POST" commandName="command" class="form-horizontal form" name="frmdeptComplainRegister" id="frmdeptComplainRegister">

				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv" style="display: none;">
					<i class="fa fa-plus-circle"></i>
				</div>
				<form:hidden path="" id="createdDate"  value="${command.complainRegisterDTO.createdDate}"/>
				<div class="panel-group accordion-toggle" id="accordion_single_collapse"> 

					<div class="panel panel-default">
								
								<div class="form-group">
									<apptags:lookupFieldSet baseLookupCode="CMT" hasId="true" disabled="true"
										pathPrefix="complainRegisterDTO.complaintType" isMandatory="true"
										hasLookupAlphaNumericSort="true" 
										hasSubLookupAlphaNumericSort="true" cssClass="form-control"
										showAll="false" />
								</div>

								<div class="form-group">
									<apptags:input labelCode="Caller Name" isReadonly="true"
										path="complainRegisterDTO.complainerName" cssClass="hasNameClass" 
										isMandatory="true" maxlegnth="50" />
									<apptags:input labelCode="Caller Mobile" isReadonly="true"
										cssClass="hasMobileNo" maxlegnth="10" dataRuleMinlength="10"
										path="complainRegisterDTO.complainerMobile" isMandatory="true" />
								</div>

								<div class="form-group">
									<apptags:textArea
										labelCode="Caller Address" isReadonly="true"
										path="complainRegisterDTO.complainerAddress" cssClass="alphaNumeric"
										maxlegnth="100" isMandatory="true" />
									<apptags:textArea
										labelCode="Caller Description" isReadonly="true"
										path="complainRegisterDTO.complaintDescription" cssClass="alphaNumeric"
										maxlegnth="100" isMandatory="true" />
								</div>
								<div class="form-group">
									
									<label class="col-sm-2 control-label required-control"
										for="location"><spring:message
											code="ComplainRegisterDTO.location"
											text="ComplainRegisterDTO.location" /></label>
									<div class="col-sm-4">
										<form:select path="complainRegisterDTO.location" cssClass="form-control" disabled="true"
											id="location" data-rule-required="true">
											<form:option value="">
												<spring:message code="Select" text="Select" />
											</form:option>
											<c:forEach items="${locations}" var="loc">
												<form:option value="${loc.locId}">${loc.locNameEng}-${loc.locArea}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
											
		<%-- <c:if test="${command.atdFname ne null && command.atdFname ne ''}">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="ExcelFileUpload">
					<spring:message code="intranet.upldFileNm" text="Uploaded File Name" />
				</label>&nbsp
				<div class="col-sm-4">			
					<div class="form-group">
						&nbsp &nbsp
						<apptags:filedownload filename="${command.atdFname}"
							filePath="${command.atdPath}" actionUrl="DeathRegistration.html?Download" >
						</apptags:filedownload>
					</div>
				</div>
			</div>
		</c:if> --%>
		<c:if test="${command.attachDocs ne null && not empty command.attachDocs} ">
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
											code="" text="Uploaded File Name" /></label>
					<div class="col-sm-12 text-left">
							<div class="table-responsive">
								<table class="table table-bordered table-striped"
									id="attachDocument">
									<tr>
										<th><spring:message code="scheme.document.name" text="Document Name" /></th>
										<th><spring:message code="scheme.view.document" text="View Documents" /></th>																		
									</tr>
									<c:forEach items="${command.attachDocs}"
										var="lookUp">
										<tr>
											<td>${lookUp.attFname}</td>
											<td><apptags:filedownload filename="${lookUp.attFname}"  dmsDocId="${lookUp.dmsDocId}"
													filePath="${lookUp.attPath}"
													actionUrl="DepartmentalScrutiny.html?Download" /></td>										
										</tr>
									</c:forEach>
								</table>
							</div>
					</div>
				</div>
		</c:if>											 
		<div class="widget-header">
			<h2>
				<strong><spring:message code="" text="User Action" /></strong>
			</h2>
		</div>
		
		<br>
	
		<div class="form-group">		
			<label class="col-sm-2 control-label "><spring:message
							code="" text="Status" /></label>
						<div class="radio col-sm-4 margin-top-5">
							<label> <form:radiobutton
									path="complainRegisterDTO.complaintStatus" value=""
									id="approve" onclick="setStatusApproval()" /> <spring:message code=""
									text="Approve" /></label> <label> <form:radiobutton
									path="complainRegisterDTO.complaintStatus" value=""
									id="reject" onclick="setStatusRejection()"/> <spring:message code=""
									text="Reject" />
							</label>
						</div>
						<form:hidden path="" value="" id="hiddenStatus"/>
						<apptags:textArea
				labelCode="Remark" isMandatory="true"
				path="complainRegisterDTO.remark" cssClass="hasNumClass form-control"
				maxlegnth="100" />
		</div>
		<div class="widget-header">
			<h2>
				<strong><spring:message code="ComplainRegisterDTO.form.div.callAttedanceRecord" text="Call Attendance Record" /></strong>
			</h2>
		</div>
		
		<br>

					<div class="form-group">				

						<label class="col-sm-2 control-label required-control"><spring:message
								code="ComplainRegisterDTO.callAttendDate"
								text="Call Attend Date" /></label>
						<div class="col-sm-4">
							<div class="input-group">
								<form:input path="complainRegisterDTO.callAttendDate"
									cssClass="form-control mandColorClass" id="callAttendDate"
									readonly="${command.saveMode eq 'V'}" placeholder="dd/MM/yyyy"/>
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
							</div>
						</div>

						<label class="control-label col-sm-2 required-control" for="">
							<spring:message code="ComplainRegisterDTO.callAttendTime"
								text="Call Attend Time" />
						</label>
						<div class="col-sm-4">
							<form:input path="complainRegisterDTO.callAttendTime"
								class="form-control datetimepicker3 mandColorClass"
								maxlength="10" id="callAttendTime" placeholder="HH:mm"
								disabled="${command.saveMode eq 'V' ? true : false }" />
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2 required-control"
											for="callAttendEmployeeList"> <spring:message
												code="ComplainRegisterDTO.callAttendEmployee"
												text="Employee who Attend Call"  /></label>
										<div class="col-sm-4">
											<form:select id="callAttendEmployeeList"
												path="complainRegisterDTO.callAttendEmployee"
												cssClass="form-control chosen-select-no-results" 
												data-rule-required="false" multiple="true"
												disabled="${command.saveMode eq 'V'}" data-placeholder="Select Attendee Name">

												<c:forEach items="${secuDeptEmployee}" var="empl">
													<form:option value="${empl.empId}">${empl.fullName}(${empl.designName})</form:option>
												</c:forEach>
											</form:select>
										</div>
						<apptags:input labelCode="ComplainRegisterDTO.reasonForDelay"
											cssClass="alphaNumeric" maxlegnth="1000"
											path="complainRegisterDTO.reasonForDelay" isMandatory="false"
											isDisabled="${command.saveMode eq 'V'}" />
						</div>			
									
		
						<%-- <apptags:CheckerAction hideForward="true" hideSendback="true"/> --%>
						
						<div class="text-center clear padding-10">
							<%-- <button type="submit" class="button-input btn btn-success" onclick="confirmToProceed(this)" name="button-submit" style="" id="button-submit">
								<spring:message code="bt.save" text="Submit" />
							</button> --%>
							
							<input type="button" value="<spring:message code="bt.save"/>" onclick="confirmToProceed(this)" class="btn btn-success" id="Save">
							
							<input type="button" onclick="window.location.href='DepartmentalScrutiny.html'" class="btn btn-danger  hidden-print" value="Back">
						</div>

					</div>
				</div> 
			</form:form>
		</div> 
	</div>


