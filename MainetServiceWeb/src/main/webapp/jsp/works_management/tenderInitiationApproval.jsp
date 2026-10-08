<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript"
	src="js/works_management/tenderInitationApproval.js"></script>
<%
    response.setContentType("text/html; charset=utf-8");
%>

<!-- End JSP Necessary Tags -->
<!-- Defect #88777 -->
<ol class="breadcrumb">
	<li><a href="AdminHome.html"><i class="fa fa-home"></i></a></li>
	<li><a href="javascript:void(0);"><spring:message text="Public Works Department" code="public.works.dept" /></a></li>
	<li><a href="javascript:void(0);"><spring:message text="Transactions" code="works.dept.transaction" /></a></li>
	<li class="active"><spring:message text="Tender Approval" code="works.tender.initiation" /></li>
</ol>
<%-- <apptags:breadcrumb></apptags:breadcrumb> --%>

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
				<div class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv">
				</div>
		<div class="widget-header">
			<h2>
				<spring:message code="tender.init.approval"
					text="Tender/Quotation Initialization Approval" />
			</h2>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="leadlift.master.fieldmand"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="leadlift.master.ismand" text="is mandatory" /></span>
			</div>
			<form:form action="TenderInitiationApproval.html"
				class="form-horizontal" name="TenderInitiationApproval"
				id="TenderInitiationApproval" modelAttribute="command">

				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="project.master.dept" text="Department" /></label>
					<div class="col-sm-4">
						<form:select path="initiationDto.deptId" cssClass="form-control"
							id="deptId" onchange="getProjects(this);" disabled="true">
							<form:option value="">
								<spring:message code='work.management.select' />
							</form:option>
							<c:forEach items="${command.departmentList}" var="list">
								<form:option value="${list.dpDeptid}" code="${list.dpDeptid}">${list.dpDeptdesc}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<label class="col-sm-2 control-label "><spring:message
							code="project.master.projname" text="Project Name" /></label>
					<div class="col-sm-4">
						<form:input path="initiationDto.projectName" id="projName"
							class="form-control preventSpace" data-rule-required="true"
							readonly="true" />
					</div>
				</div>

				<div class="form-group">

					<label class="col-sm-2 control-label "><spring:message
							code="tender.resolutionno" text="Resolution No." /></label>
					<div class="col-sm-4">
						<form:input path="initiationDto.resolutionNo" id="resolutionNo"
							readonly="true" class="form-control preventSpace" maxlength="50" />
					</div>
					<label class="col-sm-2 control-label "><spring:message
							code="tender.resolutiondate" text="Resolution Date" /></label>
					<div class="col-sm-4">
						<form:input path="initiationDto.resolutionDateDesc"
							id="resolutionDate" class="form-control" value="" readonly="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label "><spring:message
							code="wms.PreBidMeetingDate" text="Pre-Bid Meeting Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.preBidMeetDateDesc"
								id="preBidMeetDate" class="form-control preBidMeetDatePicker"
								readonly="true" />
							<label class="input-group-addon" for="preBidMeetDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden" id=preBidMeetDate></label>
						</div>
					</div>
					<label class="col-sm-2 control-label "><spring:message
							code="wms.MeetingLocation" text="Meeting Location" /></label>
					<div class="col-sm-4">
						<form:input path="initiationDto.tenderMeetingLoc" id=""
							readonly="true" class="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label "><spring:message
							code="tender.issuefromdate" text="Issue From Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.issueFromDateDesc" id="issueFromDate"
								class="form-control issueFromDatePicker" readonly="true" />
							<label class="input-group-addon" for="issueFromDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden" id=issueFromDate></label>
						</div>
					</div>
					<label class="col-sm-2 control-label "><spring:message
							code="tender.issuetilldate" text="Issue Till Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.issueToDateDesc" id="issueToDate"
								class="form-control issueToDatePicker" readonly="true" />
							<label class="input-group-addon" for="issueToDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden" id=issueToDate></label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="tender.publishdate" text="Publish Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.publishDateDesc" id="publishDate"
								class="form-control publishDatePicker" readonly="true" />
							<label class="input-group-addon" for="publishDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden" id=publishDate></label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="tender.techbid.opendate" text="Technical Bid Open Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.technicalOpenDateDesc"
								id="technicalOpenDate"
								class="form-control technicalOpenDatePicker" readonly="true" />
							<label class="input-group-addon" for="technicalOpenDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden"
								id=technicalOpenDate></label>
						</div>
					</div>
					<label class="col-sm-2 control-label"><spring:message
							code="tender.finbid.opendate" text="Financial Bid Open Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="initiationDto.financialeOpenDateDesc"
								id="financialeOpenDate"
								class="form-control financialeOpenDatePicker" readonly="true" />
							<label class="input-group-addon" for="financialeOpenDate"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden"
								id=financialeOpenDate></label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-2 control-label required-control"><spring:message
							code="leadlift.master.category" text="Category" /> </label>
					<c:set var="CATlookUp" value="CAT" />
					<apptags:lookupField items="${command.getLevelData(CATlookUp)}"
						path="initiationDto.tenderCategory"
						cssClass="form-control chosen-select-no-results"
						hasChildLookup="false" hasId="true" showAll="false"
						selectOptionLabelCode="applicantinfo.label.select" disabled="true" />
					<label class="col-sm-2 control-label required-control"><spring:message
							code="tender.validity" text="valdity of tender" /></label>
					<div class="col-sm-4">
						<form:input path="initiationDto.tndValidityDay"
							id="tndValidityDay" class="form-control hasNumber text-right"
							readonly="true" />
					</div>
				</div>
				
								
				<div>
					<h4>
						<spring:message code=""
							text="Work Estimates" />
					</h4>
					<c:set var="c" value="0" scope="page" />
					<table class="table table-bordered table-striped" id="tendWorks">
						<thead>
							<tr>
								<th width="10%"><spring:message code='work.def.workCode'
										text="Work Code" /></th>
								<th width="15%"><spring:message code='work.def.workname'
										text="Work Name" /></th>
								<th width="8%"><spring:message code='tender.type'
										text="Tender Type" /></th>
								<th width="6%"><spring:message code='wms.TenderFees'
										text="Tender Fees" /></th>
								<th width="8%"><spring:message
										code='tender.contractorClass' text="Contractor Class" /></th>
								<th width="8%"><spring:message code="tender.emd.short"
										text="EMD" /></th>
								<th width="8%"><spring:message code='tender.WorkDuration'
										text="Time of Completion" /></th>
								<th width="8%"><spring:message code='wms.DurationUnit'
										text="Duration Unit" /></th>
								<th width="10%"><spring:message
										code='tender.estimated.cost' text="Estimated Cost" /></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${'SD' ne command.initiationDto.deptCode}">
									<c:forEach items="${command.workList}" var="mstDto">
										<tr class="tenderWork">
		
											<form:input type="hidden"
												path="initiationDto.workDto[${c}].workId"
												value="${mstDto.workId}" />
		
											<td align="center">${mstDto.workcode}</td>
											<td align="center">${mstDto.workName}</td>
		
											<td><form:select path="initiationDto.workDto[${c}].tenderType"
													cssClass="form-control mandColorClass " id="tenderType${c}"
													data-rule-required="true" readonly="true">
													<form:option value="">
														<spring:message code='work.management.select' />
													</form:option>
													<c:forEach items="${command.tenderTpyes}" var="payType">
																	<form:option value="${payType.lookUpId }"
																		code="${payType.lookUpCode}">${payType.lookUpDesc}</form:option>
																</c:forEach>
												</form:select></td>
											<td><form:input
													path="initiationDto.workDto[${c}].tenderFeeAmt"
													cssClass="form-control mandColorClass text-right"
													id="tenderFeeAmt${c}" readonly="true"
													onkeypress="return hasAmount(event, this, 6, 2)"
													onchange="getAmountFormatInDynamic((this),'tenderFeeAmt')"
													placeholder="999999.99" data-rule-required="true" /></td>
		
											<td><form:select path="initiationDto.workDto[${c}].venderClassId"
													cssClass="form-control mandColorClass" id="venderClassId${c}"
													data-rule-required="true" readonly="true">
													<form:option value="">
														<spring:message code="work.management.select" />
													</form:option>
													<c:forEach items="${command.venderCategoryList}"
																	var="vClass">
																	<form:option value="${vClass.lookUpId}"
																		code="${vClass.lookUpCode}">${vClass.lookUpDesc}</form:option>
																</c:forEach>
												</form:select></td>
											<td><form:input
													path="initiationDto.workDto[${c}].tenderSecAmt"
													cssClass="form-control mandColorClass text-right"
													id="tenderSecAmt${c}"
													onkeypress="return hasAmount(event, this, 6, 2)"
													onchange="getAmountFormatInDynamic((this),'tenderSecAmt')"
													placeholder="999999.99" data-rule-required="true"
													readonly="true" /></td>
		
											<td><form:input
													path="initiationDto.workDto[${c}].vendorWorkPeriod"
													cssClass="form-control mandColorClass" readonly="true"
													id="vendorWorkPeriod${c}" data-rule-required="true" /></td>
													
											<td><form:select path="initiationDto.workDto[${c}].vendorWorkPeriodUnit"
													cssClass="form-control mandColorClass" id="vendorWorkPeriodUnit${c}"
													data-rule-required="true" readonly="true">
													<form:option value="">
														<spring:message code="work.management.select" />
													</form:option>
													<c:forEach items="${command.workDurationUnit}"
																	var="duration">
																	<form:option value="${duration.lookUpId}"
																		code="${duration.lookUpCode}">${duration.lookUpDesc}</form:option>
																</c:forEach> 
												</form:select></td>
											<td align="right">${mstDto.workEstAmt}</td>
											<c:set var="c" value="${c+1}" scope="page" />
										</tr>
									</c:forEach>
								  
								</c:when>
								
								<c:otherwise>
									<c:forEach items="${command.initiationDto.workDto}" var="mstDto">
										<tr class="tenderWork">
		
											<form:input type="hidden"
												path="initiationDto.workDto[${c}].workId"
												value="${mstDto.workId}" />
		
											<td align="center">${mstDto.workCode}</td>
											<td>${mstDto.workName}</td>
		
											<td><form:select path="initiationDto.workDto[${c}].tenderType"
													cssClass="form-control mandColorClass " id="tenderType${c}"
													data-rule-required="true" disabled="true">
													<form:option value="">
														<spring:message code='work.management.select' />
													</form:option>
													<c:forEach items="${command.tenderTpyes}" var="payType">
																	<form:option value="${payType.lookUpId }"
																		code="${payType.lookUpCode}">${payType.lookUpDesc}</form:option>
																</c:forEach>
												</form:select></td>
											<td><form:input
													path="initiationDto.workDto[${c}].tenderFeeAmt"
													cssClass="form-control mandColorClass text-right"
													id="tenderFeeAmt${c}" readonly="true"
													onkeypress="return hasAmount(event, this, 6, 2)"
													onchange="getAmountFormatInDynamic((this),'tenderFeeAmt')"
													placeholder="999999.99" data-rule-required="true" /></td>
		
											<td><form:select path="initiationDto.workDto[${c}].venderClassId"
													cssClass="form-control mandColorClass" id="venderClassId${c}"
													data-rule-required="true" readonly="true">
													<form:option value="">
														<spring:message code="work.management.select" />
													</form:option>
													<c:forEach items="${command.venderCategoryList}"
																	var="vClass">
																	<form:option value="${vClass.lookUpId}"
																		code="${vClass.lookUpCode}">${vClass.lookUpDesc}</form:option>
																</c:forEach>
												</form:select></td>
											<td><form:input
													path="initiationDto.workDto[${c}].tenderSecAmt"
													cssClass="form-control mandColorClass text-right"
													id="tenderSecAmt${c}"
													onkeypress="return hasAmount(event, this, 6, 2)"
													onchange="getAmountFormatInDynamic((this),'tenderSecAmt')"
													placeholder="999999.99" data-rule-required="true"
													readonly="true" /></td>
		
											<td><form:input
													path="initiationDto.workDto[${c}].vendorWorkPeriod"
													cssClass="form-control mandColorClass" readonly="true"
													id="vendorWorkPeriod${c}" data-rule-required="true" /></td>
													
											<td><form:select path="initiationDto.workDto[${c}].vendorWorkPeriodUnit"
													cssClass="form-control mandColorClass" id="vendorWorkPeriodUnit${c}"
													data-rule-required="true" readonly="true">
													<form:option value="">
														<spring:message code="work.management.select" />
													</form:option>
													<c:forEach items="${command.workDurationUnit}"
																	var="duration">
																	<form:option value="${duration.lookUpId}"
																		code="${duration.lookUpCode}">${duration.lookUpDesc}</form:option>
																</c:forEach> 
												</form:select></td>
											<td align="right">${mstDto.workEstimateAmt}</td>
											<c:set var="c" value="${c+1}" scope="page" />
										</tr>
									</c:forEach>
								</c:otherwise>  
							</c:choose>
						</tbody>
						<tfoot>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td align="left"><b><spring:message
											code="work.estimate.total.amount" text="Total Amount" /></b></td>
								<td align="right"><form:input
										class="form-control text-right" type="input" readonly="true"
										id="totalAmt" path="initiationDto.tenderTotalEstiAmount" /></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<c:if test="${command.completedFlag ne 'Y' }">
				<apptags:CheckerAction hideForward="true" hideSendback="true"></apptags:CheckerAction>
				</c:if>
				<div class="text-center clear padding-10">
					<c:if test="${command.completedFlag ne 'Y' }">
					<button type="button" id="save" class="btn btn-success"
						onclick="saveTenderApprovalData(this);">
						<i class="fa fa-sign-out padding-right-5"></i>
						<spring:message code="mileStone.submit" text="" />
					</button>
					</c:if>
					<button type="button" class="button-input btn btn-danger"
						name="button-Cancel" value="Cancel"
						onclick="window.location.href='AdminHome.html'" id="button-Cancel">
						<i class="fa fa-chevron-circle-left padding-right-5"></i>
						<spring:message code="works.management.back" text="" />
					</button>
				</div>
			</form:form>
		</div>
	</div>
</div>

