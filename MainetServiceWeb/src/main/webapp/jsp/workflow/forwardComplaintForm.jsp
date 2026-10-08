<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/workflow/forwardComp.js"></script>
<link rel="stylesheet"
	href="assets/libs/bootstrap-multiselect/css/bootstrap-multiselect.css"
	type="text/css">
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script>



<apptags:breadcrumb></apptags:breadcrumb>
<div class="content animated slideInDown">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="forward.comp.form"
					text="Forward Multiple Complaint Form" />
			</h2>
			<apptags:helpDoc url="ForwardComplaint.html"></apptags:helpDoc>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="contract.breadcrumb.fieldwith"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="common.master.mandatory" text="is mandatory" /> </span>
			</div>
			<div class="error-div alert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<ul>
					<li><label id="errorId"></label></li>
				</ul>
			</div>
			<form:form action="ForwardComplaint.html" method="POST"
				name="ForwardComplaintForm" class="form-horizontal"
				id="ForwardComplaintForm">
				<form:hidden path="viewMode" id="viewMode" />
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="public.griev.fromEmployee" text="From Employee" /></label>
					<div class="col-sm-4">
						<form:select id="searchEmpId" path="empId"
							cssClass="form-control chosen-select-no-results">
							<form:option value="">
								<spring:message code="master.selectEmp" text="Select Employee" />
							</form:option>
							<c:forEach var="dto" items="${command.empList}"
								varStatus="status">

								<form:option value="${dto.empId}">${dto.fullName}</form:option>

							</c:forEach>
						</form:select>
					</div>


<%-- 					<label class="col-sm-2 control-label "><spring:message
							code="workflow.ward" text="Ward" /></label>
					<div class="col-sm-4">
						<form:select id="searchWard" path="ward"
							cssClass="form-control chosen-select-no-results">
							<form:option value="">
								<spring:message code="master.selectWard" text="Select Ward" />
							</form:option>
							<c:forEach items="${command.wardList}" var="lookUp">
								<form:option value="${lookUp.lookUpId}"
									code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>
						</form:select>
					</div> --%>

				</div>




				<div class="text-center padding-bottom-20">
					<button type="button" class="btn btn-blue-2" id="searchWorkFlow"
						onclick="searchForm(this)" title="Search">
						<spring:message code="master.search" text="Search" />
					</button>
					<button type="Reset" class="btn btn-warning" id="resetWorkFlow"
						onclick="back();" title="Reset">
						<spring:message code="bt.clear" />
					</button>
				</div>

				<br />

				<div id="compList">
					<div class="form-group">
						<label class="col-sm-2 control-label required-control"><spring:message
								code="master.complaints" text="Complaints" /></label>
						<div class="col-sm-8">
							<form:select id="searchComp" path="applicationIdWithTaskId"
								cssClass="form-control" multiple="multiple">
							<%-- 	<form:option value="">
									<spring:message code="master.selectComplaint"
										text="Select Complaint" />
								</form:option> --%>
								<c:forEach var="dto" items="${command.taskList}"
									varStatus="status">

									<form:option value="${dto.applicationId},${dto.taskId }">${dto.referenceId} ==> ${dto.serviceName}==> ${dto.deptName} ==> ${dto.serviceEventName} </form:option>

								</c:forEach>
							</form:select>
							
														<script>
													 $(document).ready(function(){

														  
														 multiSelComp();
														   $('#searchComp').multiselect("refresh");
													   });
								 </script>
						</div>




					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label required-control"
							for="ApplicationAction"><spring:message
								code="public.griev.actionComplaint" text="Action on Complaint" /></label>
						<div class="col-sm-4">
						<form:select id="ApplicationAction" path="decision"
								cssClass="form-control chosen-select-no-results">
	
						
								
								<option value=""><spring:message code="care.select"
										text="Select" /></option>


<%-- 								<c:set var="baseLookupCodeCDL" value="FMC" />

								<c:forEach items="${command.getLevelData(baseLookupCodeCDL)}"
									var="lookUp"> --%>


									<%-- <c:if test="${lookUp.lookUpCode eq 'FORWARD_TO_EMPLOYEE'}"> --%>
										<%-- <option value="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</option>
										 --%>
										<option value="FORWARD_TO_EMPLOYEE" code="FORWARD_TO_EMPLOYEE">FORWARD_TO_EMPLOYEE</option>
									<%-- </c:if> --%>

								<%-- </c:forEach> --%>
							</form:select>
						</div>


					</div>



					<div class="form-group">
						<label for="EmployeeName"
							class="col-sm-2 control-label required-control "><spring:message
								code="public.grievTo.empName" text="To Employee Name" /> </label>
						<div class="col-sm-4">
							<form:select class="form-control" multiple="multiple" data-mdb-filter="true"
								id="EmployeeName" path="empIds">
								<c:set var="loginEmpId" value="${command.empId}"></c:set>
								<c:forEach var="employee" items="${command.empList}">
									<c:if test="${loginEmpId ne employee.empId}">
										<form:option value="${employee.empId}">${employee.fullName} </form:option>
									</c:if>
								</c:forEach>
							</form:select>
							<script>
													 $(document).ready(function(){

														  
														 multiSelEmp();
														   $('#EmployeeName').multiselect("refresh");
													   });
								 </script>
						</div>
					</div>

					 <div class="form-group">
						<%-- <label class="col-sm-2 control-label" for="UploadDocuments"><spring:message
								code="care.upload" text="Upload Document" /></label>
						<div class="col-sm-4">
							<small class="text-blue-2"> <spring:message
									code="care.validator.fileUploadNote"
									text="(Upload File upto 5MB and only pdf,doc,docx,jpeg,jpg,png,gif)" />
							</small>
							<apptags:formField fieldType="7" labelCode="" hasId="true"
								fieldPath="" isMandatory="false" showFileNameHTMLId="true"
								fileSize="BND_COMMOM_MAX_SIZE"
								maxFileCount="CHECKLIST_MAX_UPLOAD_COUNT"
								validnFunction="CHECK_LIST_VALIDATION_EXTENSION"
								currentCount="0" />
						</div> --%>
						<label class="col-sm-2 control-label required-control"
							for="Remark"><spring:message code="care.remarks"
								text="Remark" /></label>
						<div class="col-sm-4">
							<form:textarea class="form-control" id="Remark" path="comments"
								maxlength="1000"
								onkeyup="countChar(this,1000,'descriptionCount')"
								onfocus="countChar(this,1000,'descriptionCount')"></form:textarea>

							<div class="pull-right">
								<spring:message code="charcter.remain"
									text="characters remaining " />
								<span id="descriptionCount">1000</span>
							</div>
						</div>
					</div> 

					<div class="text-center">
						<button type="button" class="btn btn-success btn-submit"
							id="btnSave">
							<spring:message code="care.submit" text="Submit" />
						</button>


						<button type="button" class="btn btn-danger"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="care.back" text="Back" />
						</button>
					</div>

				</div>


			</form:form>
		</div>
	</div>
</div>
