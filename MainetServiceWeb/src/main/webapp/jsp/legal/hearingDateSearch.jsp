<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script src="js/legal/caseHearing.js"></script>




<style>
.widthAction {
	width: 5% !important;
}

table.table thead .sorting_asc, table.table thead .sorting_desc {
	background-color: #157eb8 !important;
	color: white;
}
</style>
<!-- Start Content here -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start info box -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="CaseHearingDTO.hrDate" text="Hearing Date" />
			</h2>
			<!-- SET HELP DOC URL WHICH YOU SET IN YOU CONTROLLER INDEX METHOD -->
			<apptags:helpDoc url="HearingDate.html" />
		</div>
		<div class="widget-content padding">
			<form:form action="HearingDate.html" name="frmHearingDate"
				id="frmHearingDate" class="form-horizontal">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
						<div class="error-div alert alert-danger alert-dismissible"
							id="errorDiv" style="display: none;">
							<button type="button" class="close" onclick="closeOutErrBox()"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span id="errorId"></span>
						</div>
				<div class="form-group">
					<label class="control-label col-sm-2 required-control" for="Census"><spring:message
							code="caseEntryDTO.cseTypId" text="Case Type" /></label>
					<div class="col-sm-4">
						<c:set var="baseLookupCode" value="TOC" />
						<form:select path="caseEntryDTO.cseTypId"
							class="form-control chosen-select-no-results" id="cseTypId"
							onchange="displayJudgeFeesTable(this)"
							disabled="${command.saveMode eq 'V' ? true : false }">
							<form:option value="0">
								<spring:message code="lgl.select" text="Select" />
							</form:option>
							<c:forEach items="${command.getLevelData(baseLookupCode)}"
								var="lookUp">
								<form:option code="${lookUp.lookUpCode}"
									value="${lookUp.lookUpId}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>

						</form:select>
					</div>

					<label class="control-label col-sm-2 required-control" for="Census"><spring:message
							code="legal.case.courtType" text="Court Type" /></label>

					<div class="col-sm-4">
						<c:set var="baseLookupCode" value="CTP" />
						<form:select path="caseEntryDTO.crtType"
							class="form-control chosen-select-no-results" id="courtTypId"
							onchange="showBenchFiled()"
							disabled="${command.saveMode eq 'V' ? true : false }">
							<form:option value="0">
								<spring:message code="lgl.select" text="Select" />
							</form:option>
							<c:forEach items="${command.getLevelData(baseLookupCode)}"
								var="lookUp">
								<form:option code="${lookUp.lookUpCode}"
									value="${lookUp.lookUpId}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>

						</form:select>
					</div>

				</div>

				<div class="form-group">
					<label class="control-label col-sm-2 required-control" for="Census"><spring:message
							code="CaseHearingDTO.hrStatus" text="Status" /></label>

					<div class="col-sm-4">
						<c:set var="baseLookupCode" value="HSC" />
						<form:select path="caseEntryDTO.Status"
							class="form-control chosen-select-no-results" id="hearingStatus"
							onchange="showBenchFiled()"
							disabled="${command.saveMode eq 'V' ? true : false }">
							<form:option value="0">
								<spring:message code="lgl.select" text="Select" />
							</form:option>
							<c:forEach items="${command.getLevelData(baseLookupCode)}"
								var="lookUp">
								<form:option code="${lookUp.lookUpCode}"
									value="${lookUp.lookUpId}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>

						</form:select>
					</div>



					<%-- <div class="form-group">
						<apptags:lookupFieldSet pageId="CaseEntry"
							labelCode="caseEntryDTO.cseCatId" baseLookupCode="CCT"
							hasId="true" showOnlyLabel="false"
							pathPrefix="caseEntryDTO.cseCatId" isMandatory="false"
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control chosen-select-no-results"
							showAll="false" columnWidth="20%"
							disabled="${command.saveMode eq 'V' ? true : false }" />
					</div> --%>
					<label class="control-label col-sm-2 required-control" for="Census"><spring:message
							code="caseEntryDTO.cseCatId" text="Case Category" /></label>

					<div class="col-sm-4">
						<form:select path=""
							cssClass="form-control chosen-select-no-results mandColorClass"
							id="cseCatogary" data-rule-required="true" onchange=""
							disabled="">
							<form:option value="0">
								<spring:message code='material.management.select' text="Select" />
							</form:option>
							<c:forEach items="${command.usageType}" var="lookUp">
								<form:option value="${lookUp.lookUpId}"
									code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>
						</form:select>
					</div>


				</div>

				<div class="form-group">
					<apptags:date pageId="CaseEntry" labelCode="lgl.Fromdate"
						datePath="caseEntryDTO.cseDate" isMandatory="false"
						cssClass="form-control fromHearingDate" fieldclass="datepicker"
						mask="99/99/9999"
						isDisabled="${command.saveMode eq 'V' ? true : false }"></apptags:date>

					<apptags:date pageId="CaseEntry"
						labelCode="lgl.Todate"
						datePath="caseEntryDTO.cseEntryDt" isMandatory="false"
						cssClass="form-control toHearingDate" fieldclass="datepicker"
						mask="99/99/9999"
						isDisabled="${command.saveMode eq 'V' ? true : false }"></apptags:date>

				</div>

				<div class="text-center clear padding-10">
					<button type="button" class="btn btn-blue-2 search"
						onclick="searchHearingDate();return false;">
						<i class="fa fa-search"></i>
						<spring:message code="lgl.search" text="Search"></spring:message>
					</button>
					
					<button type="Reset" class="btn btn-warning"
							onclick="resetCaseEntryForm();" id="resetId">
							<spring:message code="lgl.reset" text="Reset"></spring:message>
						</button>
					<%-- <button type="button" class="btn btn-blue-2 search"
							onclick="searchPopulationMaster(this);">
							<i class="fa fa-search"></i>
							<spring:message code="solid.waste.search" text="Search"></spring:message>
						</button>
						<button type="button" class="btn btn-warning"
							onclick="resetPopulation();">
							<spring:message code="solid.waste.reset" text="Reset"></spring:message>
						</button> --%>
					<%-- <button type="button" class="btn btn-success add"
							onclick="openForm('HearingDate.html','ADD');">
							<strong class="fa fa-plus-circle"></strong>
							<spring:message code="bt.add" text="Add" />
						</button> --%>

					<apptags:backButton url="HearingDate.html"
						cssClass="btn btn-danger"></apptags:backButton>
			
					<button type="submit" class="button-input btn btn-success"
						onclick="addHearingDate('HearingDate.html','addHearingDate');"
						name="button-Add" style="" id="button-submit">
						<spring:message code="lgl.add" text="Add" />
				</div>


				<div class="table-responsive clear">
					<table summary="SUMMARY"
						class="table table-bordered table-striped pmId" id="hearingTable">
						<thead>
							<tr>
								<th><spring:message code="label.checklist.srno"
										text="Sr.No." /></th>
								<th><spring:message code="caseEntryDTO.cseName"
										text="Case Name" /></th>
								<th><spring:message code="caseEntryDTO.cseSuitNo"
										text="Case Number" /></th>
								<th><spring:message code="caseEntryDTO.cseTypId"
										text="Case Type" /></th>
								<th><spring:message code="legal.case.courtType"
										text="Court Type" /></th>
								<th><spring:message code="caseEntryDTO.cseCatId"
										text="Case Category" /></th>
								<th><spring:message code="caseEntryDTO.cseCaseStatusId"
										text="Case Status" /></th>
								<th><spring:message code="CaseHearingDTO.hrStatus"
										text="Hearing Status" /></th>
								<th><spring:message code="CaseHearingDTO.hrDate"
										text="Hearing Date" /></th>
								<th class="widthAction"><spring:message code="lgl.action"
										text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.hearingDateSearchDTOList}" var="data"
								varStatus="index">

								<tr>
									<td class="text-center" width="10%">${index.count}</td>
									<td class="text-center" width="10%">${data.caseName}</td>
									<td class="text-center" width="10%">${data.caseNo}</td>
									<td class="text-center" width="10%"><spring:eval
											expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.cseTypId)"
											var="lookup" />${lookup.lookUpDesc }</td>
									<td class="text-center" width="10%" bgcolor=""><spring:eval
											expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.courtType)"
											var="lookup" />${lookup.lookUpDesc }</td>

									<td class="text-center" width="10%"><spring:eval
											expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getHierarchicalLookUp(data.caseCatogary)"
											var="lookup" />${lookup.lookUpDesc }</td>

									<td class="text-center" width="10%"><spring:eval
											expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.cseCaseStatusId)"
											var="lookup" />${lookup.lookUpDesc }</td>

									<td class="text-center" width="10%"><spring:eval
											expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.hearingStatus)"
											var="lookup" />${lookup.lookUpDesc }</td>

									
											<td class="text-center" width="10%">${data.date}</td>


									<td class="text-center">
										<button type="button" class="btn btn-blue-2 btn-sm"
											onClick="getData(${data.caseId},'V','HearingDate.html?EDIT')"
											data-original-title="View" title="View">
											<strong class="fa fa-eye"></strong><span class="hide"><spring:message
													code="solid.waste.view" text="View"></spring:message></span>
										</button>
										<button type="button" class="btn btn-warning btn-sm"
											onClick="getData(${data.caseId},'E','HearingDate.html?EDIT')"
											data-original-title="Edit" title="Edit">
											<strong class="fa fa-pencil"></strong><span class="hide"><spring:message
													code="solid.waste.edit" text="Edit"></spring:message></span>
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