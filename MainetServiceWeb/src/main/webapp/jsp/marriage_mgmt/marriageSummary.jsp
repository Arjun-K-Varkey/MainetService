<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/marriage_mgmt/marriageSummary.js"></script>
<script type="text/javascript" src="js/mainet/mfs100-9.0.2.6.js"></script>
<div id="searchAssetPage">
	<apptags:breadcrumb></apptags:breadcrumb>
	<div class="content">
		<div class="widget">
			<div class="widget-header">
				<h2>
					<spring:message code="mrm.marriage.summary" />
				</h2>
				<apptags:helpDoc url="MarriageRegistration.html"></apptags:helpDoc>
			</div>
			<div class="pagediv">
				<div class="widget-content padding">

					<form:form id="marriageSearch" name="marriageSearch"
						class="form-horizontal" action="MarriageRegistration.html"
						method="post">

						<div class="compalint-error-div">
							<jsp:include page="/jsp/tiles/validationerror.jsp" />
							<div
								class="warning-div error-div alert alert-danger alert-dismissible"
								id="errorDiv"></div>
						</div>

						<div class="form-group">
							<%-- <apptags:date fieldclass="datepicker"
								labelCode="mrm.marriage.marriageDate"
								datePath="marriageDTO.marDate"></apptags:date> --%>
								
								<label for="marDate" class="control-label col-sm-2">
								<spring:message code="mrm.marriage.marriageDate" text="Marriage Date" /></label>
								<div class="col-sm-4">
									<div class="input-group">
										<form:input path="marriageDTO.marDate" cssClass="form-control datepicker" id="marDate" />
										<label class="input-group-addon mandColorClass"
										for="marDate">
										<i class="fa fa-calendar"></i>
										</label>
									</div>
								</div>

							<%-- <apptags:date fieldclass="datepicker"
								labelCode="mrm.marriage.applicationDate"
								datePath="marriageDTO.appDate"></apptags:date> --%>
								
								<label for="appDate" class="control-label col-sm-2">
								<spring:message code="mrm.marriage.applicationDate" text="Application Date" /></label>
								<div class="col-sm-4">
									<div class="input-group">
										<form:input path="marriageDTO.appDate" cssClass="form-control datepicker" id="appDate" />
										<label class="input-group-addon mandColorClass"
										for="appDate">
										<i class="fa fa-calendar"></i>
										</label>
									</div>
								</div>
						</div>
						
						
						<div class="form-group">

							<label class="col-sm-2 control-label" for="applicantId">
								<spring:message code="mrm.marriage.husbandName" />
							</label>

							<%-- <div class="col-sm-4 ">
								<form:select path="" id="husbandId"
									cssClass="form-control chosen-select-no-results"
									data-rule-required="true">
									<form:option value=""><spring:message code="mrm.select" /></form:option>
									<c:forEach items="${command.husbandList}" var="husband">
									<c:if test="${not empty husband.husbandId}">
										<form:option value="${husband.husbandId}">${husband.firstNameEng} ${husband.lastNameEng}</form:option>
									</c:if>
										
									</c:forEach>
								</form:select>
							</div> --%>
							
								<div class="col-sm-4">
									<div class="input-group">
										<form:input path="marriageDTO.husbandDTO.firstNameEng" cssClass="form-control" id="husFirstName" />
										<label class="input-group-addon mandColorClass"
										for="husFirstName">
										<i class=""></i>
										</label>
									</div>
								</div>

							<label class="col-sm-2 control-label "><spring:message
									code="mrm.status" text="Status" /></label>
							<div class="col-sm-4">
								<form:select path="" id="status"
									class="chosen-select-no-results" data-rule-required="true">
									<form:option value="">
										<spring:message code="mrm.select" />
									</form:option>
									<form:option value="D">
										<spring:message code="" text="DRAFT"></spring:message>
									</form:option>
									<form:option value="PENDING">
										<spring:message code="" text="PENDING"></spring:message>
									</form:option>
									<form:option value="APPROVED">
										<spring:message code="" text="APPROVED"></spring:message>
									</form:option>
									<form:option value="CONCLUDE">
										<spring:message code="" text="CONCLUDE"></spring:message>
									</form:option>
								</form:select>
							</div>
						</div>
						
						<div class="form-group">
							<apptags:lookupFieldSet cssClass="form-control required-control"
								baseLookupCode="HWZ" hasId="true" pathPrefix="marriageDTO.ward"
								hasLookupAlphaNumericSort="true"
								disabled="${command.modeType eq 'V'}"
								hasSubLookupAlphaNumericSort="true" showAll="false"
								 />
								 
								 
								 <label for="appDate" class="control-label col-sm-2">
								<spring:message code="" text="Application Id" /></label>
								<div class="col-sm-4">
									<div class="input-group">
										<form:input path="marriageDTO.applicationId" cssClass="form-control" id="appId" />
										<label class="input-group-addon mandColorClass"
										for="appId">
										<i class=""></i>
										</label>
									</div>
								</div>
						</div>

						<div class="text-center clear padding-10">
							<button class="btn btn-blue-2  search" id="searchMRM"
								type="button" title="<spring:message code="mrm.button.search" text="Search"/>">
								<i class="button-input"></i>
								<spring:message code="mrm.button.search" />
							</button>
							<button type="button"
								onclick="window.location.href='MarriageRegistration.html'"
								class="btn btn-warning" title="<spring:message code="mrm.button.reset" text="Reset" />">
								<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
								<spring:message code="mrm.button.reset" text="Reset" />
							</button>
							<button class="btn btn-success add" id="AddMarriageRegisration"
								type="button" title="<spring:message code="mrm.button.add" text="New Marriage registration"/>">
								<i class="button-input"></i>
								<spring:message code="mrm.button.add" />
							</button>
							


						</div>
						<!-- End button -->

						<div class="table-responsive clear">
							<table class="table table-striped table-bordered"
								id="searchMarriage">
								<thead>
									<tr>
										<%-- <td width="3%" align="center"><spring:message
												code="mrm.srno" text="Sr.No" /></td> --%>
										<th align="center"><spring:message
												code="mrm.marriage.appNo" text="Application No" /></th>
										<th align="center"><spring:message code="mrm.marriage.appName"
												text="Applicant Name" /></th>
										<th align="center"><spring:message code="mrm.marriage.appHusName"
												text="Husband Name" /></th>
										<th align="center" width="12%"><spring:message
												code="mrm.marriage.marriageDate" text="Marriage Date" /></th>
										<th align="center" width="12%"><spring:message
												code="mrm.marriage.applicationDate" text="Application Date" /></th>
										<th align="center" width="10%"><spring:message
												code="mrm.status" text="Status" /></th>
										<th align="center" width="20%"><spring:message code="mrm.action"
												text="Action"  /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${command.marriageDTOs}" var="summaryDTO"
										varStatus="index">
										<tr>
											<%-- <td class="text-center">${index.count}</td> --%>
											<td align="center">${summaryDTO.applicationId}</td>
											<td align="center">${summaryDTO.applicantName}</td>
											<td align="center">${summaryDTO.husbandDTO.firstNameEng} ${summaryDTO.husbandDTO.lastNameEng}</td>
											<td align="center"><fmt:formatDate
													value="${summaryDTO.marDate}" pattern="dd-MM-yyyy" /></td>
											<td align="center"><fmt:formatDate
													value="${summaryDTO.appDate}" pattern="dd-MM-yyyy" /></td>
											
											<c:choose>
											  <c:when test="${summaryDTO.status eq 'D'}">
												  <td align="center">DRAFT</td>
											  </c:when>
											  <c:otherwise>
											  		<td align="center">${summaryDTO.status}</td>
											  </c:otherwise>
											</c:choose>


											<td class="text-center"><c:if
													test="${summaryDTO.status eq 'D'}">
													<button type="button" class="btn btn-danger btn-sm btn-sm"
														onClick="draftMRM(${summaryDTO.marId})"
														title="<spring:message code="mrm.summary.draft" text="Draft"></spring:message>">
														<i class="fa fa-pencil-square-o" aria-hidden="true"></i>
													</button>
												</c:if>
												<button type="button" class="btn btn-blue-2 btn-sm"
													onClick="viewMRM(${summaryDTO.marId})"
													title="<spring:message code="mrm.summary.view"></spring:message>">
													<i class="fa fa-eye"></i>
												</button> <c:if
													test="${not empty summaryDTO.serialNo && summaryDTO.serialNo ne 'NA' }">
													<button type="button" class="btn btn-danger btn-sm btn-sm"
														onClick="rePrintCertificate(${summaryDTO.marId})"
														title="<spring:message code="" text="Re-Print"></spring:message>">
														<i class="fa fa-print" aria-hidden="true"></i>
													</button>
												</c:if></td>

										</tr>

									</c:forEach>
								</tbody>
							</table>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
