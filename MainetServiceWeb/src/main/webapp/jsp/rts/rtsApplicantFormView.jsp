<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
<script type="text/javascript" src="js/rts/rtsApplicantForm.js"></script>
<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/rts/applicationFormView.js"></script>

<script>
	$(function() {

		$(".datepicker31").datepicker({
			dateFormat : 'dd/mm/yy',

		});
	});
</script>



<style>
textarea.form-control {
	resize: vertical !important;
	height: 2.3em;
}

.hide-calendar .ui-datepicker-calendar {
	display: none;
}
</style>

<div id="validationDiv">
	<!-- Start Content here -->
	<apptags:breadcrumb></apptags:breadcrumb>
	<div class="content">
		<div class="widget">
			<div class="widget-header">
				<h2>
					<b><spring:message code="DrainageConnectionDTO.apppDrainageConn"
							text="Application for Drainage Connection"></spring:message></b>

				</h2>

				<apptags:helpDoc url=""></apptags:helpDoc>
			</div>


			<div class="widget-content padding">
				<div class="mand-label clearfix">
					<span><spring:message code="leadlift.master.fieldmand"
							text="Field with" /> <i class="text-red-1">*</i> <spring:message
							code="leadlift.master.ismand" text="is mandatory" /></span>
				</div>
				<form:form action="drainageConnection.html" method="POST"
					name="rtsService" class="form-horizontal" id="rtsService"
					commandName="command">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />


					<div class="error-div alert alert-danger alert-dismissible"
						id="errorDivId" style="display: none;">
						<button type="button" class="close" onclick="closeOutErrBox()"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<span id="errorId"></span>
					</div>


					<div class="mand-label clearfix">
						<span>Field with <i class="text-red-1">*</i> is mandatory
						</span>
					</div>
					<div class="panel-group accordion-toggle"
						id="accordion_single_collapse">

						<div class="panel panel-default">
							<form:hidden path="saveMode" id="saveMode" />
							<!------------------------------------------------------------  -->
							<!--  Applicant Details starts here -->
							<!------------------------------------------------------------  -->
							<div class="panel panel-default">
								<h4 class="panel-title table" id="">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse" href="#a4"> <spring:message code="rts-applicantDetails"
											text="Applicant Details" /></a>
								</h4>
								<div id="a4" class="panel-collapse collapse in">
									<div class="panel-body">
										<div class="form-group">

											<label class="col-sm-2 control-label required-control"
												for="applicantTitle"><spring:message
													code="rts.title" /></label>
											<c:set var="baseLookupCode" value="TTL" />
											<apptags:lookupField
												items="${command.getLevelData(baseLookupCode)}"
												path="requestDTO.titleId"
												cssClass="form-control chosen-select-no-results"
												hasChildLookup="false" hasId="true" showAll="false"
												isMandatory="true" disabled="true" />

											<apptags:input labelCode="rti.firstName"
												cssClass="hasCharacter mandColorClass hasNoSpace"
												path="requestDTO.fName" isMandatory="true" maxlegnth="100"
												isDisabled="true"></apptags:input>
										</div>

										<div class="form-group">


											<apptags:input labelCode="rti.middleName"
												cssClass="hasCharacter mandColorClass hasNoSpace"
												path="requestDTO.mName" maxlegnth="100" isDisabled="true"></apptags:input>

											<apptags:input labelCode="rti.lastName"
												cssClass="hasCharacter mandColorClass hasNoSpace"
												path="requestDTO.lName" isMandatory="true" maxlegnth="100"
												isDisabled="true"></apptags:input>
										</div>

										<div class="form-group">

											<label class="col-sm-2 control-label required-control"><spring:message
													code="applicantinfo.label.gender" /></label>
											<c:set var="baseLookupCode" value="GEN" />
											<apptags:lookupField items="${command.getLevelData('GEN')}"
												path="requestDTO.gender"
												cssClass="form-control chosen-select-no-results"
												hasChildLookup="false" hasId="true" showAll="false"
												selectOptionLabelCode="applicantinfo.label.select"
												isMandatory="true" disabled="true" />
										</div>



									</div>
								</div>
							</div>
							<!------------------------------------------------------------  -->
							<!--  Applicant Details Ends here -->
							<!------------------------------------------------------------  -->


							<h4 class="panel-title table" id="">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse" href="#a1"> <spring:message code="rts-applicantAddress"
										text="Applicant Address" /></a>
							</h4>

							<!------------------------------------------------------------ -->
							<!--  Applicant Details form starts here -->
							<!------------------------------------------------------------  -->

							<div id="a1" class="panel-collapse collapse in">
								<div class="panel-body">

									<%-- 	<div class= "form-group">
											<label for="text-1"
												class="control-label col-sm-2 required-control">
												Date</label>
											<div class="col-sm-4">
												<div class="input-group">
													<form:input class="form-control datepicker" id="toDate"
														path="" maxlength="10" disabled="false"
														onkeydown="this.value=this.value.replace(/^(\d\d)(\d)$/g,'$1/$2').replace(/^(\d\d\/\d\d)(\d+)$/g,'$1/$2').replace(/[^\d\/]/g,'')" />
													<label class="input-group-addon"
														for="trasaction-date-icon30"><i
														class="fa fa-calendar"></i></label>

												</div>
											</div>
								</div> --%>

									<div class="form-group">
										<apptags:input labelCode="rti.buildingName"
											cssClass="hasCharacter mandColorClass  "
											path="requestDTO.bldgName" isMandatory="true" maxlegnth="100"
											isDisabled="true"></apptags:input>
										<apptags:input labelCode="rti.taluka"
											cssClass=" mandColorClass " path="requestDTO.blockName"
											 maxlegnth="100" isDisabled="true">
										</apptags:input>
									</div>

									<div class="form-group">
										<apptags:input labelCode="rts.roadName"
											cssClass="hasCharacter mandColorClass"
											path="requestDTO.roadName" maxlegnth="100"
											isDisabled="true">
										</apptags:input>
										<%-- <apptags:input labelCode="Ward Name"
											cssClass="mandColorClass " path="requestDTO.wardNo"
											isMandatory="true" maxlegnth="100">
										</apptags:input> --%>


										<label class="control-label col-sm-2  required-control"
											for="text-1"><spring:message code="rts.wardName" /></label>
										<div class="col-sm-4">
											<form:select path="requestDTO.wardNo"
												class="form-control mandColorClass chosen-select-no-results"
												label="Select Ward Name" disabled="true" id="wardNo">
												<form:option value="" selected="true">Select Ward Name</form:option>
												<c:forEach items="${command.wardList}" var="ward">
													<form:option value="${ward.key}" code="${ward.key}">${ward.value}</form:option>
												</c:forEach>

											</form:select>
										</div>



									</div>

									<div class="form-group">
										<apptags:input labelCode="rti.Try5"
											cssClass="hasCharacter mandColorClass "
											path="requestDTO.cityName" isMandatory="true" maxlegnth="100"
											isDisabled="true"></apptags:input>
										<apptags:input labelCode="rti.pinCode"
											cssClass="hasPincode hasNoSpace"
											path="requestDTO.pincodeNo" isMandatory="false"
											maxlegnth="100" isDisabled="true"></apptags:input>
									</div>

									<div class="form-group">
										<apptags:input labelCode="rti.mobile1"
											cssClass="hasMobileNo mandColorClass "
											path="requestDTO.mobileNo" isMandatory="true" maxlegnth="100"
											isDisabled="true"></apptags:input>
										<apptags:input labelCode="chn.lEmail"
											cssClass="hasemailclass hasNoSpace"
											isDisabled="true" path="requestDTO.email" isMandatory="false"
											maxlegnth="100"></apptags:input>
									</div>
								</div>



							</div>
							<!------------------------------------------------------------ -->
							<!--  Applicant Address ends here -->
							<!------------------------------------------------------------  -->






							<!------------------------------------------------------------  -->
							<!--  Service Form starts here -->
							<!------------------------------------------------------------  -->
							<div class="panel panel-default" id="accordion_single_collapse">
								<h4 class="panel-title table" id="">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse" href="#a9"> <spring:message
											code="rts.serviceDetails" text="Service Details" /></a>
								</h4>
								<div id="a9" class="panel-collapse collapse in">
									<div class="panel-body">
										<div class="form-group">
											<%-- 	<label class="control-label col-sm-2  required-control"
												for="text-1">Department Name </label>
											<div class="col-sm-4">
												<form:select path="requestDTO.deptId"
													class="form-control mandColorClass chosen-select-no-results"
													label="Select Department Name" disabled="false" id="deId"
													onchange="getServiceList(this,'rtsService.html','getServiceList')">
													<!-- Here the option is being loaded in the drop down list using forEach loop  -->
													<form:option value="" selected="true">Select Department Name</form:option>
													<c:forEach items="${command.depList}" var="dept">
														<form:option value="${dept.key}" code="${dept.key}">${dept.value}</form:option>
													</c:forEach>

												</form:select>
											</div> --%>
											<!-- onchange ="getServiceList(this,'rtsService.html','getServiceList')" -->



											<label class="control-label col-sm-2  required-control"
												for="text-1"><spring:message code="rts.serviceName" /> </label>
											<div class="col-sm-4">
												<form:select path="requestDTO.serviceId"
													class="form-control mandColorClass chosen-select-no-results"
													label="Select Department Name" disabled="true"
													id="serviceId">
													<!-- Here the option is being loaded in the drop down list using forEach loop  -->
													<form:option value="" selected="true">Select Service Name</form:option>
													<c:forEach items="${command.serviceList}" var="service">
														<form:option value="${service.key}" code="${service.key}">${service.value}</form:option>
													</c:forEach>
												</form:select>
											</div>
										</div>
										<div class="form-group"></div>
									</div>
								</div>
							</div>
							<!------------------------------------------------------------  -->
							<!--   Service Form  ends here -->
							<!------------------------------------------------------------  -->


							<div class="panel panel-default">

								<!------------------------------------------------------------  -->
								<!--  Drainage Connection Information -->
								<!------------------------------------------------------------  -->
								<div class="panel panel-default">
									<h4 class="panel-title table" id="">
										<a data-toggle="collapse" class=""
											data-parent="#accordion_single_collapse" href="#a4"> <spring:message code="rts.DraInfo"
												text="Drainage Connection Information" /></a>
									</h4>
									<div id="a4" class="panel-collapse collapse in">
										<div class="panel-body">
											<div class="form-group">
												<label class="col-sm-2 control-label required-control"
													for="address"><spring:message code="rti.address"
														text="Address" /></label>
												<div class="col-sm-4">
													<form:textarea class="form-control" id="rtiAddress"
														maxlength="1000" disabled="true"
														path="drainageConnectionDto.drainageAddress"></form:textarea>
												</div>

												<label class="col-sm-2 control-label required-control"><spring:message
														code="rts.applicantType" text="Applicant Type" /></label>
												<c:set var="baseLookupCode" value="APC" />
												<apptags:lookupField items="${command.getLevelData('APC')}"
													path="drainageConnectionDto.applicantType" disabled="true"
													cssClass="form-control chosen-select-no-results"
													hasChildLookup="false" hasId="true" showAll="false"
													selectOptionLabelCode="applicantinfo.label.select"
													isMandatory="true" />
											</div>


											<div class="form-group">
												<%-- <label class="col-sm-2 control-label required-control"><spring:message
														code="rts.sizeOfConnection" text="Size of Connection" /></label>
												<apptags:lookupField items="${command.getLevelData('CSZ')}"
													path="drainageConnectionDto.sizeOfConnection"
													cssClass="form-control chosen-select-no-results"
													hasChildLookup="false" hasId="true" showAll="false"
													selectOptionLabelCode="applicantinfo.label.select"
													isMandatory="true" disabled="true" />
 --%>
												<%-- <apptags:input labelCode="rts.typeOfConnection"
													cssClass="hasNumber mandColorClass "
													path="drainageConnectionDto.typeOfConnection"
													isMandatory="true" maxlegnth="100" isDisabled="true"></apptags:input> --%>

												<label class="col-sm-2 control-label required-control"
													for="applicantTitle"><spring:message
														code="rts.typeOfConnection" text="Type Of Connection" /></label>
												<c:set var="baseLookupCode" value="DCT" />
												<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}"
													path="drainageConnectionDto.typeOfConnection"
													cssClass="form-control chosen-select-no-results"
													hasChildLookup="false" hasId="true" showAll="false"
													selectOptionLabelCode="applicantinfo.label.select"
													isMandatory="true"
													disabled="true" />

											</div>

											<div class="form-group">
												<label class="control-label col-sm-2  required-control"
													for="text-1"><spring:message code="rts.wardName" /></label>
												<div class="col-sm-4">
													<form:select path="drainageConnectionDto.ward"
														class="form-control mandColorClass chosen-select-no-results"
														label="Select Ward Name" disabled="true" id="deId">
														<form:option value="" selected="true">Select Ward Name</form:option>
														<c:forEach items="${command.wardList}" var="ward">
															<form:option value="${ward.key}" code="${ward.key}">${ward.value}</form:option>
														</c:forEach>

													</form:select>
												</div>



												<apptags:input labelCode="rts.propertyIndexNo"
													cssClass=" mandColorClass " path="drainageConnectionDto.propertyIndexNo"
													 maxlegnth="100" isDisabled="true"></apptags:input>
											</div>
											<div class="form-group">
											<apptags:input labelCode="rts.noOfFlat" isReadonly=""
												path="drainageConnectionDto.noOfFlat" isMandatory="true"
												cssClass="hasNumber form-control" maxlegnth="3" isDisabled="true">
											</apptags:input>
										</div>

											<div class="form-group"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!------------------------------------------------------------  -->
					<!--  AttachDocuments Form starts here -->
					<!------------------------------------------------------------  -->
					<div class="panel panel-default" id="accordion_single_collapse">
						<h4 class="panel-title table" id="">
							<a data-toggle="collapse" class=""
								data-parent="#accordion_single_collapse" href="#a19"> <spring:message
									code="TbDeathregDTO.attacheddoc" text="Attached Documents" /></a>
						</h4>
						<div id="a19" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="form-group">
									<div class="col-sm-12 text-left">
										<div class="table-responsive">
											<table class="table table-bordered table-striped"
												id="attachDocs">
												<tr>
													<th><spring:message code="scheme.document.name"
															text="" /></th>
													<th><spring:message code="scheme.view.document"
															text="" /></th>
												</tr>
												<c:forEach items="${command.checkList}" var="lookUp">
													<tr>
														<%-- <td align="center">${lookUp.doc_DESC_Mar}</td> --%>
														<c:choose>
																<c:when
																	test="${userSession.getCurrent().getLanguageId() eq 1}">
																	<td align="center"><label>${lookUp.doc_DESC_ENGL}</label></td>
																</c:when>
																<c:otherwise>
																	<td align="center"><label>${lookUp.doc_DESC_Mar}</label></td>
																</c:otherwise>
															</c:choose>
														<td align="center"><apptags:filedownload
																filename="${lookUp.documentName}"
																filePath="${lookUp.uploadedDocumentPath}"
																actionUrl="drainageConnection.html?Download">
															</apptags:filedownload></td>
													</tr>
												</c:forEach>
											</table>
										</div>
									</div>
								</div>
								<div class="form-group"></div>
							</div>
						</div>
					</div>
					<!------------------------------------------------------------  -->
					<!--   AttachDocuments Form  ends here -->
					<!------------------------------------------------------------  -->


					<!--  -->
					<c:if test="${command.viewFlag ne 'Y'}">
					<div class="panel-group accordion-toggle" id="applicationFormId">
						<div class="panel panel-default">
							<div id="a2" class="panel-collapse collapse in">
								<div class="panel-body">
									<div>
										<div class="panel-heading">
											<h4 class="panel-title">
												<a data-toggle="collapse" class=""
													data-parent="#accordion_single_collapse"
													href="#CheckAction"><spring:message
														code="workflow.checkAct.userAct" /></a>
											</h4>
										</div>
										<%-- <div class="widget-content padding">
											<apptags:CheckerAction hideForward="true" hideSendback="true"></apptags:CheckerAction>
										</div> --%>
										<c:set var="radioButtonsRequired" value="Approve" />
										<c:set var="radioButtonsRequiredVal" value="APPROVED" />
										<%-- <c:if
											test="${not empty command.workflowActionDto.empGroupDescEng}"> --%>
											<c:set var="radioButtonsRequired"
												value="${radioButtonsRequired},Reject" />
											<c:set var="radioButtonsRequiredVal"
												value="${radioButtonsRequiredVal},REJECTED" />
										<%-- </c:if> --%>
										<div id="CheckAction" class="panel-collapse in">
											<div class="panel-body">
												<div class="form-group">
													<%-- <c:choose>
														<c:when
															test="${not empty command.workflowActionDto.empGroupDescEng}">
															<apptags:radio radioLabel="nac.approve,nac.reject"
																radioValue="APPROVED,REJECTED" isMandatory="true"
																labelCode="nac.status" path="workflowActionDto.decision"
																defaultCheckedValue="APPROVED">
															</apptags:radio>
															<br />
														</c:when>
														<c:otherwise>
															<apptags:radio radioLabel="nac.approve"
																radioValue="APPROVED" isMandatory="true"
																labelCode="nac.status" path="workflowActionDto.decision"
																defaultCheckedValue="APPROVED">
															</apptags:radio>
															<br />
														</c:otherwise>
													</c:choose> --%>
													<apptags:radio cssClass="addInfo"
														radioLabel="${radioButtonsRequired}"
														radioValue="${radioButtonsRequiredVal}"
														labelCode="work.estimate.approval.decision"
														path="workflowActionDto.decision" isMandatory="true"
														changeHandler="loadDataBasedOnDecision(this)"></apptags:radio>
												</div>
												
												
												<c:if test="${not empty command.objdesc}">
										
												<div class="panel panel-default"
													id="">
													<h4 class="panel-title table" id="">
															<spring:message code="workflow.pervious.remak" text="Previous Remark" />
													</h4>
													<div id="" class="">
														<div class="panel-body">
															<div class="form-group">
																<div class="col-sm-12 text-left">
																	<div class="table-responsive">
																		<table class="table table-bordered table-striped"
																			id="previousRemark">
																			<tr>
																				<th style="width: 50%;"><spring:message code="workflow.emp.name.desc" text="Emp. Name - Designation" /></th>
																				<th style="width: 50%;"><spring:message code="workflow.checkAct.remark" text="Remark" /></th>
																			</tr>
																			<c:forEach items="${command.objdesc}" var="lookUp">

																				<tr>
																					<td align="center">${lookUp[1]} - ${lookUp[3]} </td>
																					<td align="center">${lookUp[2]}</td>
																				</tr>
																			</c:forEach>


																		</table>
																	</div>
																</div>
															</div>
															<div class="form-group"></div>
														</div>
													</div>
												</div>
												</c:if>
												
												
												
											
												<c:if
													test="${not empty command.workflowActionDto.empGroupDescEng}">

													<div class="form-group">
														<label class="col-sm-2 control-label"
															for="ExcelFileUpload"><spring:message
																code="intranet.fileUpld" /><i class="text-red-1">*</i></label>


														<div class="col-sm-4">
															<div class="col-sm-4">
																<apptags:formField fieldType="7" fieldPath=""
																	showFileNameHTMLId="true"
																	fileSize="WORK_COMMON_MAX_SIZE" isMandatory="false"
																	maxFileCount="CHECK_LIST_MAX_COUNT"
																	validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
																	currentCount="0">
																</apptags:formField>
															</div>
														</div>
													</div>
												</c:if>
												
												<div class="form-group">
													<apptags:textArea labelCode="workflow.checkAct.remark"
														path="workflowActionDto.comments" isMandatory="true"
														cssClass="mandColorClass comment" />
												</div>
												
												<c:if test="${command.getLoiChargeApplFlag() eq 'Y'}">
													<div id="loiCharges">
														<h4>LOI Fees and Charges in Details</h4>
														<div class="table-responsive">
															<table class="table table-bordered table-striped">
																<tr>
																	<th scope="col" width="80">Sr. No</th>
																	<th scope="col">Charge Name</th>
																	<th scope="col">Amount</th>
																</tr>
																<c:forEach var="charges" items="${command.chargesList}"
																	varStatus="status">
																	<c:set value="${status.index}" var="d"></c:set>
																	<tr>
																		<td>${status.index + 1}</td>
																		<td><form:input
																				path="chargesList[${d}].mediaType" disabled="true"
																				class="form-control" id="mediaType${d}" /></td>
																		<c:if test="${charges.editableLoiFlag eq 'Y'}">
																		<fmt:formatNumber
																					value="${charges.chargeAmount}"
																					type="number" var="formattedChargeAmounts1"
																					minFractionDigits="2" maxFractionDigits="2"
																					groupingUsed="false" />
																			<td><form:input id="chargeAmount${d}"
																					path="chargesList[${d}].chargeAmount"
																					value="${formattedChargeAmounts1}"
																					onblur="calculateTotalLoi()" readonly="false"
																					class="form-control text-right amount"
																					onkeypress="return hasAmount(event, this, 10, 2)" /></td>
																		</c:if>
																		<c:if test="${charges.editableLoiFlag ne 'Y'}">
																		<fmt:formatNumber
																					value="${charges.chargeAmount}"
																					type="number" var="formattedChargeAmounts2"
																					minFractionDigits="2" maxFractionDigits="2"
																					groupingUsed="false" />
																			<td class="col-sm-2"><form:input
																					id="chargeAmount${d}"
																					path="chargesList[${d}].chargeAmount"
																					value="${formattedChargeAmounts2}"
																					disabled="true" readonly="false"
																					class="form-control text-right amount"
																					onkeypress="return hasAmount(event, this, 10, 2)" /></td>
																		</c:if>

																	</tr>
																</c:forEach>
																<tr>
																	<td colspan="2"><span class="pull-right"><b>Total
																				LOI Amount</b></span></td>
																	<td><fmt:formatNumber
																			value="${command.totalLoiAmount}" type="number"
																			var="totalAmount" minFractionDigits="2"
																			maxFractionDigits="2" groupingUsed="false" /> <form:input
																			path="" value="${totalAmount}"
																			cssClass="form-control text-right amount" id="totAmount"
																			readonly="true" /></td>

																</tr>
															</table>
														</div>
													</div>
												</c:if>
	
													<div class="text-center">
												<c:choose>
													<c:when test="${command.lastChecker}">
														<c:if test="${command.getLoiChargeApplFlag() ne 'Y'}">
															<button type="button" class="btn btn-success" id="loisubmit"
																onclick="generateLoiCharges(this)">
																<spring:message code="noc.generate.LOI.Charges"
																	text="Generate LOI Charges"></spring:message>
															</button>
															
															<button type="button" id="save"
															class="btn btn-success btn-submit"
															onclick="saveApplicationFormData(this);">
															<spring:message code="asset.transfer.save" text="Save" />
														</button>
															<apptags:backButton url="AdminHome.html"></apptags:backButton>
														</c:if>
													</c:when>
													<c:otherwise>
														<button type="button" id="save"
															class="btn btn-success btn-submit"
															onclick="saveApplicationFormData(this);">
															<spring:message code="asset.transfer.save" text="Save" />
														</button>
														<apptags:backButton url="AdminHome.html"></apptags:backButton>
													</c:otherwise>
												</c:choose>
											</div>

												<div class="text-center padding-top-10">
													<c:if test="${command.getLoiChargeApplFlag() eq 'Y'}">

														<button type="button" class="btn btn-green-3"
															title="Submit" onclick="saveApplicationFormData(this)  ">
															<!-- <i class="fa padding-left-4" aria-hidden="true"></i> -->
															<spring:message code="asset.transfer.save" text="Save" />
														</button>
														<apptags:backButton url="AdminHome.html"></apptags:backButton>
													</c:if>
												</div>
											</div>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
					</c:if>
					
					<c:if test="${command.viewFlag eq 'Y'}">
					<div class="text-center">
						<apptags:backButton url="AdminHome.html"></apptags:backButton>
					</div>
					</c:if>

				</form:form>

			</div>
		</div>
	</div>
</div>








