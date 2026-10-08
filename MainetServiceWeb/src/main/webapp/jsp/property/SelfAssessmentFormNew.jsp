<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/property/selfAssessment.js" ></script>   
<script type="text/javascript" src="js/property/ownershipDetailsForm.js"></script>
<script type="text/javascript" src="js/property/dataEntrySuite.js"></script>
<script src="js/property/unitDetails.js" type="text/javascript"></script>
<script src="js/property/unitSpecificAdditionalInfo.js" type="text/javascript"></script>
<script type="text/javascript" src="js/property/ownershipDetailsForm.js"></script>
<script src="js/mainet/jQueryMaskedInputPlugin.js"></script> 
<div id="validationDiv">
<style>
input[type='text'], textarea {
	text-transform: uppercase;
}


</style>
<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
	<div class="widget-header">
				<h2><strong><spring:message code="property.selfassessment" text=" Property Registration"/></strong></h2>				
				
					<apptags:helpDoc url="SelfAssessmentForm.html"></apptags:helpDoc>	
				
	</div>
	
	<!-- End Main Page Heading -->
		
<!-- Start Widget Content -->
	<div class="widget-content padding">
	
		
		<!-- Start mand-label -->
		<div class="mand-label clearfix">
			<span><spring:message code="property.Fieldwith"/><i class="text-red-1">* </i><spring:message code="property.ismandatory"/>
			</span>
		</div>
		<!-- End mand-label -->
		
		<!-- Start Form -->
		<form:form action="SelfAssessmentForm.html"
					class="form-horizontal form" name="frmSelfAssessmentForm"
					id="frmSelfAssessmentForm">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			
			
			<input type="hidden" id="ownershipCode" />
			<input type="hidden" value="${command.orgId}" id="orgId"/>
			<input type="hidden" value="${command.deptId}" id="deptId"/>
			<input type="hidden" value="${command.authLevel}" id="authLevel"/>
			<form:hidden path="draftFlag" id="draftFlag"/>
			<form:hidden path="checklistApplicable" id="checklistApplicable" />
			
			<c:if test="${command.isHideUserAction() eq true }">
			<div class="form-group">
	  			<apptags:textArea labelCode="workflow.checkAct.remark" path="workflowActionDto.comments" isDisabled="true" cssClass="mandColorClass comment" />
			</div>
			</c:if>

			<c:if test="${command.getIntgrtionWithBP() eq 'Y'}">
			<div class="padding-10"><strong><i><spring:message code="property.EnterDetailstosearchproperty" /></i></strong></div>
			<!-- Start Each Section -->
			<div class="form-group">
					
					<label class="col-sm-2 control-label"><spring:message code="property.searchType" />
					</label>
				   
				   <c:set var="baseLookupCode" value="SRT" />
							<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="" cssClass="form-control changeParameterClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="unit.select"/>
					
                   <apptags:input labelCode="property.searchNumber" path=""></apptags:input>
                   	
                   	<!-- Start button -->
                   	<div class="text-center padding-15 clear">                   	
                   	<button type="button" class="btn btn-warning"><spring:message code="property.search" text="" /></button>	
                   	</div>
                   	<!-- End button -->	
                   	
			</div> 
			</c:if>
			<!-- End Each Section -->
			
			<div class="panel-group accordion-toggle"
						id="accordion_single_collapse">
						
						<div class="accordion-toggle ">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#propertyMaster"><spring:message
										code="property.propertyMaster" text="Property Information" /></a>
							</h4>
							<div class="panel-collapse collapse in" id="propertyMaster">

								<%-- <jsp:include
									page="/jsp/property/NewPropertyRegistrationLandDetails.jsp"></jsp:include> --%>

								<div class="form-group">
									<apptags:input cssClass="alphaNumeric preventSpace"
										labelCode="propertydetails.refPropNo"
										path="provisionalAssesmentMstDto.refPropNo" maxlegnth="20" onChange="validateRefPropNo()"></apptags:input>

									<apptags:input cssClass="alphaNumeric preventSpace"
										labelCode="propertydetails.gisRefNo"
										path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"></apptags:input>

								</div>


								<jsp:include page="/jsp/property/NewPropertyTaxZoneDetails.jsp"></jsp:include>


								<!-- new fields Added -->
								<%-- <div class="form-group">
									<label for="surveyType"
										class="col-sm-2 control-label required-control"><spring:message
											code="property.billingMethod" text="Billing Method" /> </label>
									<c:set var="baseLookupCodess" value="BLM" />
									<apptags:lookupField
										items="${command.getLevelData(baseLookupCodess)}"
										path="provisionalAssesmentMstDto.billMethod"
										cssClass="form-control" hasChildLookup="false" hasId="true"
										showAll="false" selectOptionLabelCode="Select " />

									<label class="col-sm-2 control-label "
										for="billingMethodReason"><spring:message
											code="property.billingMethodReason"
											text="Billing Method Change Reason" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="provisionalAssesmentMstDto.billingMethodReason"
											id="billingMethodReason"></form:input>
									</div> 
								</div> --%>
								<!-- new fields  end-->


							<%-- 	<jsp:include page="/jsp/property/SelfAssessmentBuildingDetailForm.jsp"></jsp:include> --%>

										<div class="accordion-toggle">
										
											<div class="panel-collapse collapse in" id="BuildingDetails">
												<div class="form-group">
												<label class="col-sm-2 control-label required-control"><spring:message code="propertydetails.firstAssessmentDate"/></label>
												<div class="col-sm-4">
												<div class="input-group"> 
												<form:input path="provisionalAssesmentMstDto.assAcqDate"  readonly="${command.getAssType() eq 'A' ? true : false}" class="lessthancurrdate form-control mandColorClass dateClass addColor dateFrmt" id="proAssAcqDate" onChange="getFinancialYear();" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off"/>
												<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
												<form:hidden path="leastFinYear" id="minFinancialYear"/>	
												</div>
												</div>
												<label class="col-sm-2 control-label "><spring:message
														code="propertydetails.lastAssessmentDate" /></label>
												<div class="col-sm-4">
													<div class="input-group">
														<form:input
															path="provisionalAssesmentMstDto.lastAssessmentDate"
															class="datepicker  form-control  dateClass addColor dateFrmt"
															id="lastAssessmentDate"
															placeholder="DD/MM/YYYY" autocomplete="off" />
														<span class="input-group-addon"><i
															class="fa fa-calendar"></i></span>
													</div>
												</div>
												</div>
																				
										
											</div>
									</div>
							
								<div class="form-group">
									<%-- <label class="col-sm-2 control-label required-control"><spring:message
											code="propertydetails.firstAssessmentDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input
												path="provisionalAssesmentMstDto.firstAssessmentDate"
												class="datepicker  form-control mandColorClass dateClass addColor"
												id="firstAssessmentDate" data-rule-required="true"
												placeholder="DD/MM/YYYY" autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>
										</div>
									</div> --%>
									<%-- <label class="col-sm-2 control-label "><spring:message
											code="propertydetails" text="Construction Start Date" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input path="provisionalAssesmentMstDto.reviseAssessmentDate"
												class="datepicker  form-control  dateClass addColor dateFrmt"										
												id="reviseAssessmentDate" 
												placeholder="DD/MM/YYYY"
												autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>											
										</div>
									</div> --%>
									<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" />
											</div>		
									
									<label class="col-sm-2 control-label "><spring:message
											code="unitdetails.constructPermissionDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input path="provisionalAssesmentMstDto.constructPermissionDate"
												class="datepicker-cpdate  form-control  dateClass addColor dateFrmt"												
												id="constructPermissionDate" 
												placeholder="DD/MM/YYYY"
												autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>											
										</div>
									</div>
									
										<%-- <c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
									<c:if test="${command.getConstructFlag() eq 'Y' }"> --%>
                             		
                             			
                             					
                                   
												
<%-- 												provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdYearConstruction --%>
							 						
										
								<%-- 	</c:if>
								 --%>	
								</div>

								<div class="form-group">		
								
									<label for="permissionUseNo" class="col-sm-2 control-label">
											<spring:message	code="unitdetails.permissionUseNo" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
												path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
												id="permissionUseNo" />
											</div>								
									
									<label class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											
													<form:input type="text" path="provisionalAssesmentMstDto.assdYearConstruction" class="form-control datepicker2 mandColorClass dateClass addColor dateFrmt" id="yearOfConstruc" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" />						
													<span class="input-group-addon"><i class="fa fa-calendar"></i></span>								
										</div>
									</div>
									
									
									
									</div>
									<div class="form-group">						
									
									
									<%-- <label class="col-sm-2 control-label required-control"><spring:message
											code="property"  text="Property Type"/> </label>
									<div class="col-sm-4">
										<c:set var="baseLookupCode" value="LDT" />
										<form:select path="provisionalAssesmentMstDto.assLandType"
											onchange="showSur()"
											class="form-control mandColorClass" id="assLandType">
											<form:option value="">
												<spring:message code="property.landDetails.select"
													text="Select" />
											</form:option>
											<c:forEach items="${command.getLevelData(baseLookupCode)}"
												var="lookUp">
												<form:option value="${lookUp.lookUpId}"
													code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>
									</div> --%>
									<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true"
									cssClass="form-control required-control" showAll="false"  columnWidth="10%"/>
								</div>

							</div>
						</div>
						
					
					
					
						
					<div class="accordion-toggle ">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
									code="property.Ownershipdetail" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="OwnshipDetail">
							<div class="form-group">
								<label class="col-sm-2 control-label required-control"
									for="OwnershipDetail"><spring:message
										code="property.ownershiptype" /></label>
									<div class="col-sm-4">
									<c:set var="baseLookupCode" value="OWT" /> <form:select path="provisionalAssesmentMstDto.assOwnerType"
											onchange="getOwnerTypeDetails()" class="form-control mandColorClass" id="ownerTypeId" data-rule-required="true">
												<form:option value="">
													<spring:message code="property.sel.optn.ownerType" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
									</form:select>
									</div>		
							</div>
						
						
						<form:hidden path="selfAss" id="selfAss"/>
						<form:hidden path="assType" id="assType"/>
						<form:hidden path="serviceShortCode" id="serviceShortCode"/>
						
					
						<div id="owner">
						
						</div>					
					</div>
					</div>
						<%-- <jsp:include page="/jsp/property/NewPropertyRegistrationLandDetails.jsp"></jsp:include> --%>
						
						<jsp:include page="/jsp/property/SelfAssessmentPropertyAddressForm.jsp"></jsp:include>
						
						<div id="utilitySection" style="display: none;">
						
						<jsp:include page="/jsp/property/SelfAssessmentUtilitySection.jsp"></jsp:include>
						
						</div>
																							
						<jsp:include page="/jsp/property/SelfAssessmentUnitDetailForm.jsp"></jsp:include>
			
			
			<!-- Start button -->

			
			<div class="text-center padding-10">
			<c:if test="${command.getAssType() eq 'N'}">  
				<button type="button" class="btn btn-success"
				onclick="getCheckList(this)" id="checkList"><spring:message code="unit.proceed"/></button>
			</c:if>
			
			<c:if test="${command.getSelfAss() eq 'Y'}">  
				<input 	type="button" class="btn btn-warning" value="<spring:message code="property.reset" text="Reset" />" onclick="resetFormData(this)" id="ResetSelfAssForm"/>	
			</c:if>
				
			</div>

			<div id="checkListDiv">
				<c:if test="${not empty command.checkList}"> 

				<h4 class="margin-top-0 margin-bottom-10 panel-title"> <a data-toggle="collapse" href="#DocumentUpload"><spring:message code="propertyTax.DocumentsUpload"/></a></h4>
                
                <div id="DocumentUpload" class="panel-collapse collapse in">
                  <div class="panel-body">
                    
										<div class="overflow margin-top-10">
											<div class="table-responsive">
												<table
													class="table table-hover table-bordered table-striped">
													<tbody>
														<tr>
															<th> <spring:message
																		code="water.serialNo" text="Sr No" />
															</th>
															<th> <spring:message
																		code="water.docName" text="Document Name" />
															</th>
															<th> <spring:message
																		code="water.status" text="Status" />
															</th>
															<th> <spring:message
																		code="water.uploadText" text="Upload" />
															</th>
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
																	<td> <spring:message
																				code="water.doc.mand" />
																	</td>
																</c:if>
																<c:if test="${lookUp.checkkMANDATORY eq 'N'}">
																	<td> <spring:message
																				code="water.doc.opt" />
																	</td>
																</c:if>
																<td><div id="docs_${lk}" class="">
																		<apptags:formField fieldType="7" labelCode=""
																			hasId="true" fieldPath="checkList[${lk.index}]"
																			isMandatory="false" showFileNameHTMLId="true"
																			fileSize="BND_COMMOM_MAX_SIZE"
																			maxFileCount="CHECK_LIST_MAX_COUNT"
																			validnFunction="ALL_UPLOAD_VALID_EXTENSION"
																			currentCount="${lk.index}" />
																	</div>
																	</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>									
                  </div>
                </div>
    
          
        </c:if>    
				</div>
			
			
			
			<!--  End button -->
			</div>
						<div class="text-center padding-10">
			<c:if test="${command.getAssType() eq 'N'}">  
				<button type="button" class="btn btn-success btn-submit"
				onclick="confirmToProceed(this)" id="proceed"><spring:message code="unit.proceed"/></button>
				
				<c:if test="${command.getSelfAss() eq 'Y'}">  
				<input 	type="button" class="btn btn-warning" value="<spring:message code="property.reset" text="Reset" />" onclick="resetFormData(this)" id="Reset"/>	
				</c:if>			
				
			</c:if>
			
			<form:hidden path="editFlag" value="${command.editFlag}" id="editFlag"/>
			<c:if test="${command.isHideUserAction() eq true || command.isAuthLevel() eq true}">  
				<button type="button" class="btn btn-success btn-submit"
				onclick="confirmToProceed(this)" id="proceed"><spring:message code="unit.proceed"/></button>
			</c:if>
			
			<c:if test="${command.getAssType() eq 'A' && command.isHideUserAction() eq false}">  
			<button type="button" class="btn btn-success btn-submit"
				onclick="compareBeforeAfterAuth(this)" id="btnCompare">Proceed to Compare</button>
			</c:if>
			
			
			</div>
			
		</form:form>
		<!-- End Form -->
		</div>
		<!-- End Widget Content here -->
		</div>
		<!-- End Widget  here -->							
</div>
<!-- End of Content -->
</div>

