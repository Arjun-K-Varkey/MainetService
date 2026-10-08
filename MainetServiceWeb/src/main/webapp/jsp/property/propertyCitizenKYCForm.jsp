<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script src="js/mainet/file-upload.js"></script>
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/script-library.js"></script>
<script src="js/property/propertyCitizenKYCForm.js"></script>


<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="proeprty.citizen.kyc.form" />
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a>
			</div>
		</div>
		<div class="widget-content padding">
			<form:form action="PropertyCitizenKYCApproval.html"
				class="form-horizontal" name="PropertyCitizenKYCApproval"
				id="PropertyCitizenKYCApproval">

				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>

				<form:hidden path="billingMethod" id="billingMethod" />

					<c:if test="${command.billingMethod eq 'I' }">
						<div class="form-group">
							<apptags:input labelCode="propertydetails.PropertyNo."
								path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
							<apptags:input labelCode="property.FlatNo"
								path="provisionalAssesmentMstDto.flatNo" isDisabled="true"></apptags:input>
						</div>

						<div class="form-group">
							<apptags:input labelCode="property.OwnerName" path="ownerName"
								isDisabled="true"></apptags:input>
							<apptags:input labelCode="ownerdetails.Occupier.Name"
								path="occupierName" isDisabled="true"></apptags:input>
						</div>

					</c:if>

					<c:if
						test="${empty provisionalAssesmentMstDto.flatNo || provisionalAssesmentMstDto.flatNo eq null }">

						<div class="form-group">
							<apptags:input labelCode="propertydetails.PropertyNo."
								path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
							<apptags:input labelCode="property.OwnerName" path="ownerName"
								isDisabled="true"></apptags:input>
						</div>

					</c:if>
					<div class="form-group">
						<apptags:input labelCode="proprty.witness.Address"
							path="provisionalAssesmentMstDto.assAddress" isDisabled="true"></apptags:input>
					</div>

					<div class="form-group">


						<label class="col-sm-2 control-label required-control"><spring:message
								code="ownersdetail.mobileno" /></label>
						<div class="col-sm-4">
							<form:input path="mobileNo" class="form-control" id="mobileNo"
								maxlength="10" disabled="true"/>
						</div>



						<apptags:input labelCode="property.billPayment.emailId"
							path="emailId" isDisabled="true"></apptags:input>
					


					</div>


					<%-- <div class="form-group">
						<apptags:input labelCode="property.billPayment.emailId"
							path="emailId"></apptags:input>

					</div> --%>

					<div class="form-group">

						<apptags:input labelCode="property.Electric.Billing.Unit.Number"
							path="provisionalAssesmentMstDto.electricBillUnitNo"
							maxlegnth="4" isDisabled="true"></apptags:input>
						<apptags:input labelCode="property.Electric.Consumer.Number"
							path="provisionalAssesmentMstDto.electricConsumerNo"
							maxlegnth="12" isDisabled="true"></apptags:input>

					</div>

					<div class="form-group">

						<apptags:input labelCode="property.Electric.Meter.Number"
							path="provisionalAssesmentMstDto.electricMeterNo" maxlegnth="12"  isDisabled="true"></apptags:input>
					</div>



					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message
								code="property.Is.Underground.Drainage.Connection.Available" /></label>

						<div class="col-sm-4">
							<form:select
								path="provisionalAssesmentMstDto.isUnderGroundDrainConAvail"
								id="isUnderGroundDrainConAvail"
								class="form-control mandColorClass chosen-select-no-results"
								onchange="showConnectionDate()" disabled="true">
								<form:option value="">
									<spring:message code='property.select' text="select" />
								</form:option>
								<form:option value="Yes">
									<spring:message code='' text="Yes" />
								</form:option>
								<form:option value="No">
									<spring:message code='' text="No" />
								</form:option>
							</form:select>
						</div>
						<div id="conDate">
							<label class="col-sm-2 control-label"><spring:message
									code="proeprty.Connection.Date" /></label>
							<div class="col-sm-4">
								<div class=" input-group">

									<form:input class="form-control datepicker"
										path="provisionalAssesmentMstDto.connectionDate"
										isMandatory="true" id="connectionDate" disabled="true"/>
									<span class="input-group-addon"> <i
										class="fa fa-calendar"></i>
									</span>
								</div>
							</div>
						</div>

					</div>


					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message
								code="property.Is.Underground.Septic.Tank.Available" /></label>
						<div class="col-sm-4">
							<form:select
								path="provisionalAssesmentMstDto.isUnderGroundSpecTankAvail"
								id="isUnderGroundSpecTankAvail"
								class="form-control mandColorClass chosen-select-no-results" disabled="true">
								<form:option value="">
									<spring:message code='property.select' text="select" />
								</form:option>
								<form:option value="Yes">
									<spring:message code='' text="Yes" />
								</form:option>
								<form:option value="No">
									<spring:message code='' text="No" />
								</form:option>
							</form:select>
						</div>


					</div>

					<%-- <c:if test="${not empty command.checkList}">

						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#DocumentUpload"><spring:message
									code="propertyTax.DocumentsUpload" /></a>
						</h4>

						<div id="DocumentUpload" class="panel-collapse collapse in">
							<div class="panel-body">

								<div class="overflow margin-top-10">
									<div class="table-responsive">
										<table class="table table-hover table-bordered table-striped">
											<tbody>
												<tr>
													<th><spring:message code="water.serialNo" text="Sr No" />
													</th>
													<th><spring:message code="water.docName"
															text="Document Name" /></th>
													<th><spring:message code="water.status" text="Status" />
													</th>
													<th><spring:message code="water.uploadText"
															text="Upload" /></th>
												</tr>
												<c:forEach items="${command.checkList}" var="lookUp"
													varStatus="lk">
													<tr>
														<td>${lookUp.documentSerialNo}</td>
														<c:choose>
															<c:when
																test="${userSession.getCurrent().getLanguageId() eq 1}">
																<td>${lookUp.doc_DESC_ENGL}</td>
															</c:when>
															<c:otherwise>
																<td>${lookUp.doc_DESC_Mar}</td>
															</c:otherwise>
														</c:choose>
														<c:if test="${lookUp.checkkMANDATORY eq 'Y'}">
															<td><spring:message code="water.doc.mand" /></td>
														</c:if>
														<c:if test="${lookUp.checkkMANDATORY eq 'N'}">
															<td><spring:message code="water.doc.opt" /></td>
														</c:if>
														<td><div id="docs_${lk}" class="">
																<apptags:formField fieldType="7" labelCode=""
																	hasId="true" fieldPath="checkList[${lk.index}]"
																	isMandatory="false" showFileNameHTMLId="true"
																	fileSize="COMMOM_MAX_SIZE"
																	maxFileCount="CHECK_LIST_MAX_COUNT"
																	validnFunction="CARE_VALIDATION_EXTENSION"
																	currentCount="${lk.index}" />
															</div>
															<div class="col-sm-12">
																<small class="text-blue-2 "> <spring:message
																		code="property.file.upload.validator"
																		text="Documents/Images up to 20 MB can be upload.only pdf,png,jpg is allowed." />
																</small>
															</div></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>


					</c:if> --%>

<c:if test="${not empty command.documentList}">	
                         <h4 class="margin-top-0 margin-bottom-10 panel-title"> <a data-toggle="collapse" href="#DocumentUpload"><spring:message code="propertyTax.UploadDocumnet"/></a></h4>
                         
							<fieldset class="fieldRound">
								<div class="overflow">
									<div class="table-responsive">
										<table class="table table-hover table-bordered table-striped">
											<tbody>
												<tr>
													<th><label class="tbold"><spring:message
																code="water.serialNo" text="Sr No" /></label></th>
													<th><label class="tbold"><spring:message
																code="water.docName" text="Document Name" /></label></th>
												     <th><label class="tbold"><spring:message
																code="water.download"/></label></th>
												</tr>

												<c:forEach items="${command.documentList}" var="lookUp"
													varStatus="lk">
													<tr>
														<td><label>${lookUp.clmSrNo}</label></td>
														<c:choose>
															<c:when
																test="${userSession.getCurrent().getLanguageId() eq 1}">
																<td><label>${lookUp.clmDescEngl}</label></td>
															</c:when>
															<c:otherwise>
																<td><label>${lookUp.clmDesc}</label></td>
															</c:otherwise>
														</c:choose>
														  <td>
														  <c:set var="links" value="${fn:substringBefore(lookUp.attPath, lookUp.attFname)}" />
															<apptags:filedownload filename="${lookUp.attFname}" filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}" actionUrl="SelfAssessmentForm.html?Download"></apptags:filedownload>
					                                    </td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</fieldset>
						
					 </c:if> 
					 
					

					<apptags:CheckerAction hideSendback="true" hideForward="true"></apptags:CheckerAction>
					
					
					<div class="text-center padding-10">
			<button type="button" class="btn btn-success btn-submit"
					 onclick="approvePropertyKYCApplication(this)" id="deleteBill"><spring:message code="" text="Approve"/></button>
					 
			<button type="button" class="btn btn-danger"  id="back"
									onclick="window.location.href='AdminHome.html'"><i class="fa fa-chevron-circle-left padding-right-5"
							aria-hidden="true"></i><spring:message code="property.Back" text="Back"/>
						</button> 
			
				  
				   
			</div>
			
			

			</form:form>
		</div>
	</div>
</div>
