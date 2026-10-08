<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/workflow/workFlowType/form.js"></script>
<link rel="stylesheet"
	href="assets/libs/bootstrap-multiselect/css/bootstrap-multiselect.css"
	type="text/css">
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script>
<script type="text/javascript">
	
</script>

<div class="widget">
	<div class="widget-header">
		<h2>
			<spring:message code="workflow.grid.workflowmaster"
				text="Work Flow Master" />
		</h2>
		<apptags:helpDoc url="WorkFlowType.html"></apptags:helpDoc>
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
		<form:form action="WorkFlowType.html" method="POST"
			name="workFlowTypeForm" class="form-horizontal" id="workFlowTypeForm">
			<form:hidden path="workFlowMasDTO.hiddeValue" id="hiddeValue" />
			<form:hidden path="workFlowMasDTO.currentOrgId" id="onOrgSelect" />
			<form:hidden path="workFlowMasDTO.currentOrgId" id="onOrg_0" />
			<form:hidden path="" id="kdmcEnv" value="${command.kdmcEnv}" />
			<div class="panel-group accordion-toggle"
				id="accordion_single_collapse">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse"
								data-parent="#accordion_single_collapse" href="#a1"><spring:message
									code="workflow.select" text="Select Workflow" /></a>
						</h4>
					</div>
					<div id="a1" class="panel-collapse collapse in">
						<div class="panel-body">

							<div class="form-group">
								<label class="col-sm-2 control-label required-control"><spring:message
										code="workflow.form.field.dep" text="Department" /></label>
								<div class="col-sm-4">
									<form:select path="workFlowMasDTO.deptId" id="departmentId"
										class="form-control chosen-select-no-results mandColorClass">
										<form:option value="0">
											<spring:message code="selectdropdown" text="select" />
										</form:option>
										<c:forEach items="${command.deptList}" var="objArray">
											<c:if test="${userSession.languageId eq 1}">
												<form:option value="${objArray[0]}" label="${objArray[1]}"></form:option>
											</c:if>
											<c:if test="${userSession.languageId eq 2}">
												<form:option value="${objArray[0]}" label="${objArray[2]}"></form:option>
											</c:if>
										</c:forEach>
									</form:select>
								</div>
								<label class="col-sm-2 control-label required-control"><spring:message
										code="workflow.form.field.service" text="Service" /></label>
								<div class="col-sm-4">
									<form:select path="workFlowMasDTO.serviceId" id="serviceId"
										class="form-control chosen-select-no-results mandColorClass"
										onchange="getServiceCode();">
										<form:option value="0">
											<spring:message code="selectdropdown" text="select" />
										</form:option>
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label required-control"><spring:message
										code="workflow.mode" text="Work Flow Mode" /> </label>
								<apptags:lookupField items="${command.getLevelData('WFM')}"
									path="workFlowMasDTO.workflowMode"
									cssClass="form-control chosen-select-no-results"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="selectdropdown" isMandatory="true" />


								<div class="form-group" id="propertyTypeDiv"
									style="display: none;">
									<label class="col-sm-2 control-label required-control">
										<spring:message code="" text="Property Type" />
									</label>
									<div class="col-sm-4">
										<form:select class="form-control"
											path="workFlowMasDTO.propType" id="propTypeId">
											<form:option value="">
												<spring:message code="" text="Select"></spring:message>
											</form:option>
											<form:option value="1">Legal</form:option>
											<form:option value="2">Illegal</form:option>
											<form:option value="3">Illegal(Core)</form:option>
										</form:select>
									</div>
								</div>

								<div id="complaintTypeDiv">
									<label class="col-sm-2 control-label required-control"><spring:message
											code="workflow.comptType" text="Complaint Type" /></label>
									<div class="col-sm-4">
										<form:select name="complaintId"
											path="workFlowMasDTO.complaintId" id="complaintId"
											class="form-control chosen-select-no-results mandColorClass">
											<form:option value="0">
												<spring:message code="selectdropdown" text="select" />
											</form:option>
										</form:select>
									</div>
								</div>
								<!------------  On Selection of serviceCode == 'LQE'  ------------------------------------------------------------------>
								<div id="workflowBaseDeptDiv">
									<label class="col-sm-2 control-label required-control"><spring:message
											code="" text="Base Department" /></label>
									<div class="col-sm-4">
										<form:select path="workFlowMasDTO.wmSchCodeId1"
											class="form-control chosen-select-no-results" id="baseDeptId">
											<form:option value="0">
												<spring:message code='master.selectDropDwn' />
											</form:option>
											<c:forEach items="${command.deptList}" var="objArray">
											<c:if test="${userSession.languageId eq 1}">
												<form:option value="${objArray[0]}" label="${objArray[1]}"></form:option>
											</c:if>
											<c:if test="${userSession.languageId eq 2}">
												<form:option value="${objArray[0]}" label="${objArray[2]}"></form:option>
											</c:if>
										</c:forEach>
										</form:select>
									</div>

								</div>

								<!------------  On Selection of serviceCode == 'WOA'  ------------------------------------------------------------------>
								<div id="sourceOfFundDiv">
									<label class="col-sm-2 control-label required-control"><spring:message
											code="wms.SourceofFund" text="Source of Fund" /></label>
									<div class="col-sm-4">
										<form:select path="workFlowMasDTO.wmSchCodeId1"
											class="form-control chosen-select-no-results" id="sourceCode"
											onchange="getSchemeNames(this);">
											<form:option value="0">
												<spring:message code='master.selectDropDwn' />
											</form:option>
											<c:forEach items="${command.sourceLookUps}" var="lookUp">
												<form:option value="${lookUp.lookUpId}"
													code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>
									</div>

								</div>

								<!---------------------------------------------------------------------------------------------------------------------->
							</div>
							<div class="form-group" id="schemeNames">
								<label class="col-sm-2 control-label required-control"><spring:message
										code="" text="Scheme Name" /></label>
								<div class="col-sm-4">
									<form:select path="workFlowMasDTO.wmSchCodeId2" id="schemeId"
										class="form-control chosen-select-no-results">
										<form:option value="0">
											<spring:message code='master.selectDropDwn' />
										</form:option>
									</form:select>
								</div>
							</div>



							<div class="form-group">
								<apptags:input labelCode="workflow.form.field.label.fromAmount"
									path="workFlowMasDTO.fromAmount" cssClass="hasAmount"></apptags:input>
								<apptags:input labelCode="workflow.form.field.label.toAmount"
									path="workFlowMasDTO.toAmount" cssClass="hasAmount"></apptags:input>
							</div>
							
							<div id="wardZOneSelectionDiv">
								<div class="form-group clear">
									<label class="col-sm-2 control-label"><spring:message
											code="workflow.locationType" text="Location Type" /><span
										class="mand">*</span></label>
									<div class="col-sm-4">
										<span> <label class="radio-inline"> <form:radiobutton
													value="A" path="workFlowMasDTO.type" /> <spring:message
													code="workflow.form.field.label.all" /></label> <label
											class="radio-inline"> <form:radiobutton value="N"
													path="workFlowMasDTO.type" /> <spring:message
													code="workflow.form.field.label.wardzone" /></label>
										</span>
									</div>
								</div>
							</div>
							<div id="zone-ward"></div>
						</div>
					</div>
				</div>
				<div class="panel panel-default" id="mappingDiv">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" class=""
								data-parent="#accordion_single_collapse" href="#a2"><spring:message
									code="workflow.step" text="Add Work Flow Steps" /> </a>
						</h4>
					</div>
					<div id="a2" class="panel-collapse collapse in">
						<div class="panel-body">
							<div id="table-responsive" class="table-overflow-sm"></div>
						</div>
					</div>
				</div>
				<div class="text-center padding-top-10">
					<c:if test="${command.modeType ne 'V'}">
						<button type="button" class="btn btn-success btn-submit"
							id="submitFlow">
							<spring:message code="bt.save" />
						</button>
					</c:if>
					<c:if test="${command.modeType eq 'C'}">
						<button type="button" class="btn btn-warning" id="resetFlow">
							<spring:message code="bt.clear" />
						</button>
					</c:if>
					<input type="button" id="backBtn" class="btn btn-danger"
						onclick="back()" value="<spring:message code="bt.backBtn"/>" />
				</div>
				<form:hidden path="removeChildIds" />
				<input type="hidden" id="langId" value="${userSession.languageId}" />
			</div>
		</form:form>
	</div>
</div>