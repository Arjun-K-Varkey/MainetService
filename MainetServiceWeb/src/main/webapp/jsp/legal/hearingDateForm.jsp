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
<script src="js/mainet/file-upload.js"></script>
<script src="js/mainet/validation.js"></script>
<script src="js/legal/caseHearing.js"></script>
<script src="assets/libs/fullcalendar/moment.min.js"></script> 


<style>
.upload{
	width:15%;
}

</style>
<!-- Start Content here -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start info box -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="CaseHearingDTO.hrDate" text="Hearing Date"/>
			</h2>
			<!-- SET HELP DOC URL WHICH YOU SET IN YOU CONTROLLER INDEX METHOD -->
			<apptags:helpDoc url="HearingDate.html" />
		</div>
		<div class="widget-content padding">
			<form:form action="HearingDate.html" name="frmHearingDate" 	id="frmHearingDate" class="form-horizontal">
				<spring:message code="CaseHearingDTO.valid.status" text="Can not add new Hearing when Status is Schedule" var="validStatus"/>
				<spring:message code="CaseHearingDTO.valid.close" text="Can not add new Hearing when Status is Close" var="validClose"/>
				<spring:message code="CaseHearingDTO.valid.hrDate" text="Please Enter Hearing Date" var="validHrDate"/>
				<spring:message code="CaseHearingDTO.valid.hrStatus" text="Please Enter Status" var="validHrStatus"/>
				<spring:message code="CaseHearingDTO.valid.hrPreparation" text="Please Enter Preparation" var="validHrPreparation"/>
				<spring:message code="CaseHearingDTO.valid.validHrDate" text="Hearing Date Should be greater than previous Hearing Date" var="validHearingDate"/>
				<form:hidden path="" value="${validStatus}" id="validStatus"/>
				<form:hidden path="" value="${validClose}" id="validClose"/>
				<form:hidden path="" value="${validHrDate}" id="validHrDate"/>
				<form:hidden path="" value="${validHrStatus}" id="validHrStatus"/>
				<form:hidden path="" value="${validHrPreparation}" id="validHrPreparation"/>
				<form:hidden path="" value="${validHearingDate}" id="validHearingDate"/>
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv" style="display: none;"></div>
				<jsp:include page="/jsp/legal/caseEntryViewForm.jsp" />
				
					<div class="table-responsive clear">
<table id="hearingDateTable" summary="HEARING DATA"  class="table table-bordered table-striped">
							<thead>
								<tr>
									<th><spring:message code="label.checklist.srno" text="Sr.No."/></th>
									<th><spring:message code="CaseHearingDTO.hrDate" text="Hearing Date"/><i class="text-red-1">*</i></th>
									<th><spring:message code="CaseHearingDTO.hrStatus" text="Status"/></th>
									<th><spring:message code="CaseHearingDTO.hrPreparation" text="Preparation"/><i class="text-red-1">*</i></th>
									<th class="upload"><spring:message code="lgl.commentsHearingDate.upload" text="Upload Documents"/><small class="text-blue-2 margin-left-5"> <spring:message
										code="legal.doc.msg" text="(Only .pdf, .png and .jpg is allowed upto 1 MB)" /></small></th>
									<c:if test="${command.saveMode ne 'V'}">
										<th width="50"><a href="#" data-toggle="tooltip"
														data-placement="top" class="btn btn-blue-2  btn-sm"
														data-original-title="Add" onclick="addData();"><strong
															class="fa fa-plus"></strong><span class="hide"></span></a></th>
									</c:if>
								</tr>
							</thead>
							<c:if test="${not empty command.hearingEntry}">
								<tbody>
									<c:forEach items="${command.hearingEntry}" var="data" varStatus="index">
										<tr class="hearingClass">
											<td class="text-center" ><form:input path=""
																	cssClass="form-control mandColorClass "
																	id="sequence${index.index}" value="${index.count}" disabled="true" /></td>
										<c:if test="${command.saveMode ne 'E' && command.saveMode ne 'C' }">

											<td class="text-center"><spring:eval
													expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.hrStatus)"
													var="lookup" /> <form:input
													path="hearingEntry[${index.index}].hrDate"
													class="form-control lessthancurrdate2 date"
													id="hrDate${index.index}"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V' }"
													readonly="${command.orgFlag eq 'Y'}" /></td>
										</c:if>
										
										
										<c:if test="${command.saveMode ne 'E' && command.saveMode eq 'C' }">

											<td class="text-center"><spring:eval
													expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.hrStatus)"
													var="lookup" /> <form:input
													path="hearingEntry[${index.index}].hrDate"
													class="form-control lessthancurrdate2 date"
													id="hrDate${index.index}"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V' }"
													readonly="${command.orgFlag ne 'Y'}" /></td>
										</c:if>

										<c:if test="${command.saveMode eq 'E' }">

											<td class="text-center"><spring:eval
													expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getNonHierarchicalLookUpObject(data.hrStatus)"
													var="lookup" /> <form:input
													path="hearingEntry[${index.index}].hrDate"
													class="form-control lessthancurrdate2 date"
													id="hrDate${index.index}"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V' }"
													readonly="${command.orgFlag ne 'Y'}" /></td>
										</c:if>


										<td class="text-center" >
											
													
												<c:set var="baseLookupCode" value="HSC" />  
												<form:select path="hearingEntry[${index.index}].hrStatus"
													cssClass="form-control mandColorClass" id="hrStatus${index.index}"
													onchange="" disabled="true"
													data-rule-required="true">
													<form:option value="0">
														<spring:message code="selectdropdown" text="select" />
													</form:option>
													<c:forEach items="${command.getSortedLevelData(baseLookupCode)}"
														var="lookUp">
														<form:option value="${lookUp.lookUpId}"
															code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
													</c:forEach>
												</form:select>
											</td>

										<c:if test="${command.saveMode ne 'E' && command.saveMode eq 'C' }">
											<td class="text-center"><form:input
													path="hearingEntry[${index.index}].hrPreparation"
													class="form-control" id="hrPreparation${index.index}"
													maxlength="1000"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V'}"
													readonly="${command.orgFlag ne 'Y'}" />
											</td>
										</c:if>
										
										<c:if test="${command.saveMode ne 'E' && command.saveMode ne 'C' }">
											<td class="text-center"><form:input
													path="hearingEntry[${index.index}].hrPreparation"
													class="form-control" id="hrPreparation${index.index}"
													maxlength="1000"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V'}"
													readonly="${command.orgFlag eq 'Y'}" />
											</td>
										</c:if>

										<c:if test="${command.saveMode eq 'E'}">
											<td class="text-center"><form:input
													path="hearingEntry[${index.index}].hrPreparation"
													class="form-control" id="hrPreparation${index.index}"
													maxlength="1000"
													disabled="${lookup.lookUpCode eq 'RSH' || command.saveMode eq 'V'}"
													readonly="${command.orgFlag ne 'Y'}" />
											</td>
										</c:if>										


										<td class="text-center" >
														
												<c:set var="count" value="${index.index}"/>	
												<c:set var="lookUp" value="${command.attachDocsList[count]}"/>	
												<c:if test="${lookUp ne null }">	
													<apptags:filedownload
															filename="${lookUp.attFname}"
															filePath="${lookUp.attPath}"
															actionUrl="HearingDate.html?Download" />
												</c:if>		
												<c:if test="${command.saveMode ne 'V'}">
												<c:if test="${lookUp eq null }">
													<apptags:formField fieldType="7" labelCode="" hasId="true"
														fieldPath="hearingEntry[${index.index}].file"
														isMandatory="false" showFileNameHTMLId="true"
														fileSize="CARE_COMMON_MAX_SIZE"
														maxFileCount="CHECK_LIST_MAX_COUNT"
														validnFunction="CARE_VALIDATION_EXTENSION"
														checkListMandatoryDoc="${lookUp.checkkMANDATORY}"
														checkListDesc="${docName}" currentCount="${index.index}" />
												</c:if>	
												</c:if>
											</td>
											<c:if test="${command.saveMode ne 'V'}">
												<td><%-- <a href="#" data-toggle="tooltip"
														data-placement="top" class="btn btn-danger btn-sm"
														data-original-title="Delete"
														onclick="deleteData($(this),'removedIds');"> <strong
															class="fa fa-trash"></strong> <span class="hide"><spring:message
																	code="lgl.delete" text="Delete" /></span>
													</a> --%></td>
											</c:if>
											
										</tr>
									</c:forEach>
									
								</tbody>
							</c:if>
							 
							<c:if test="${command.saveMode ne 'V' && empty command.hearingEntry }">
	
								<tbody>
										<tr class="hearingClass">
											<td class="text-center" >1</td>
											<c:if test="${command.saveMode ne 'E'}">
											<td class="text-center" >
												<form:input path="hearingEntry[0].hrDate" class="form-control lessthancurrdate2 date" id="hrDate0" disabled="${command.orgFlag eq 'Y'}"/>
											</td>
											</c:if>
											
											<c:if test="${command.saveMode eq 'E'}">
											<td class="text-center" >
												<form:input path="hearingEntry[0].hrDate" class="form-control lessthancurrdate2 date" id="hrDate0" disabled="${command.orgFlag ne 'Y'}"/>
											</td>
											</c:if>
											
										
											<td class="text-center" >
												<c:set var="baseLookupCode" value="HSC" />  
												<form:select path="hearingEntry[0].hrStatus" cssClass="form-control mandColorClass" id="hrStatus0" data-rule-required="true" disabled="true">
												<%-- 	<form:option value="0">
														<spring:message code="selectdropdown" text="select" />
													</form:option> --%>
													<c:forEach items="${command.getSortedLevelData(baseLookupCode)}" var="lookUp">
														<c:if test="${lookUp.lookUpCode eq 'SH'}">
															<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
															<form:hidden path="hearingEntry[0].hrStatus" value="${lookUp.lookUpId}"/>
														</c:if>
													</c:forEach>
												</form:select>
												
											</td>
											
											<c:if test="${command.saveMode ne 'E'}">
											<td class="text-center" >
												<form:input path="hearingEntry[0].hrPreparation" class="form-control" id="hrPreparation0" maxlength="1000" disabled="${command.orgFlag eq 'Y'}"/>
											</td>
											</c:if>
												<c:if test="${command.saveMode eq 'E'}">
											<td class="text-center" >
												<form:input path="hearingEntry[0].hrPreparation" class="form-control" id="hrPreparation0" maxlength="1000" disabled="${command.orgFlag ne 'Y'}"/>
											</td>
											</c:if>
											
											
																				
											<td class="text-center" >
												<apptags:formField fieldType="7" labelCode="" hasId="true"
													fieldPath="hearingEntry[0].file"
													isMandatory="false" showFileNameHTMLId="true"
													fileSize="CARE_COMMON_MAX_SIZE"
													maxFileCount="CHECK_LIST_MAX_COUNT"
													validnFunction="CARE_VALIDATION_EXTENSION"
													checkListMandatoryDoc="${lookUp.checkkMANDATORY}"
													checkListDesc="${docName}" currentCount="${0}" />
											</td>
											<c:if test="${command.saveMode ne 'V'}">
												<td><%-- <a href="#" data-toggle="tooltip"
														data-placement="top" class="btn btn-danger btn-sm"
														data-original-title="Delete"
														onclick="deleteData($(this),'removedIds');"> <strong
															class="fa fa-trash"></strong> <span class="hide"><spring:message
																	code="lgl.delete" text="Delete" /></span>
													</a> --%></td>
											</c:if>
										</tr>
								</tbody>
							</c:if>
						</table>
				</div>
				
				<div class="text-center margin-top-10">
				<c:if test="${command.saveMode eq 'E'}">
					<c:if test="${command.orgFlag eq 'Y'}">
						<input type="button" value="<spring:message code="bt.save" text="Save" />" onclick="return saveData(this);" class="css_btn btn btn-success">
					</c:if>
					</c:if>
				
					<c:if test="${command.saveMode ne 'V'}">
					<c:if test="${command.orgFlag ne 'Y'}">
						<input type="button" value="<spring:message code="bt.save" text="Save" />" onclick="return saveData(this);" class="css_btn btn btn-success">
					</c:if>
					</c:if>					
					<c:if test="${command.saveMode eq 'C'}">
					<c:if test="${command.orgFlag eq 'Y'}">
						<input type="button" value="<spring:message code="bt.save" text="Save" />" onclick="return saveData(this);" class="css_btn btn btn-success">
					</c:if>
					</c:if>
					
					<c:if test="${command.saveMode eq 'C'}">
						<input type="button" class="btn btn-warning " value="<spring:message code="bt.clear" text="Reset"/>" onclick="openForm('HearingDate.html','create')">
					</c:if>
					<apptags:backButton url="HearingDate.html"/>
			  </div>
			</form:form>
		</div>
	</div>
</div>