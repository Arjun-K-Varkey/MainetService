<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/property/updatePropertyInfo.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="property.update.info" text="Update Property Info" /></b>
			</h2>
			<apptags:helpDoc url="UpdatePropertyInfo.html"></apptags:helpDoc>
		</div>

		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="UpdatePropertyInfo.html"
				class="form-horizontal form" name="UpdatePropertyInfo"
				id="UpdatePropertyInfo">
				
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			<form:hidden path="billingMethod" id="billingMethod"/>
			<div class="accordion-toggle ">
					<h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#propertyMaster"><spring:message
								code="property.propertyMaster" text="Property Information" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="propertyMaster">
						
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="property.PropertyNo"
								path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
								
							<apptags:radio 
								radioLabel="Active,Cancel" radioValue="A,C"
								labelCode="property.propertyStatus" disabled="true"
								path="provisionalAssesmentMstDto.assActive"></apptags:radio>
						</div>
						
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.refPropNo"
								path="provisionalAssesmentMstDto.refPropNo" isReadonly="true"></apptags:input>

							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.gisRefNo" isDisabled="true"
								path="provisionalAssesmentMstDto.assGisId" ></apptags:input>

						</div>

						<div class="form-group">
							<apptags:lookupFieldSet cssClass="form-control "
								baseLookupCode="WZB" hasId="true"
								pathPrefix="provisionalAssesmentMstDto.assWard"
								hasLookupAlphaNumericSort="true" disabled="true"
								hasSubLookupAlphaNumericSort="true" showAll="false"
								isMandatory="flase" />
						</div>
						
						<div class="form-group">
								<label for="road-type" class="col-sm-2 control-label "><spring:message code="unitdetails.RoadType"/> </label>
									 <div>
									     <c:set var="baseLookupCode" value="RFT" />
									  <apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
									 		path="provisionalAssesmentMstDto.propLvlRoadType" cssClass="form-control" 
									 		hasChildLookup="false" hasId="true" showAll="false" disabled="true"
											selectOptionLabelCode="prop.selectRoadFactor" isMandatory="false"/> 
									 </div>
									 
									<label for="surveyType"	class="col-sm-2 control-label "><spring:message
														code="property.billingMethod" text="Billing Method" /> </label>
									<c:set var="baseLookupCodess" value="BLM" />
									<apptags:lookupField
										items="${command.getLevelData(baseLookupCodess)}"
										path="provisionalAssesmentMstDto.billMethod" changeHandler="enableArrears(this)"
										cssClass="form-control" hasChildLookup="false" hasId="true"
										showAll="false" selectOptionLabelCode="Select " disabled="true"/>										
							</div>

							<div class="form-group">
								<apptags:lookupFieldSet cssClass="form-control "
								baseLookupCode="DST" hasId="true" pathPrefix="provisionalAssesmentMstDto.disputedType"  
								hasLookupAlphaNumericSort="true" disabled="true"
								hasSubLookupAlphaNumericSort="true" showAll="false" isMandatory="false"/>
			
							</div>
						
							<div class="form-group">
								<label class="col-sm-2 control-label "><spring:message code="propertydetails.firstAssessmentDate"/></label>
								<div class="col-sm-4">
										<div class="input-group"> 
										<form:input path="provisionalAssesmentMstDto.assAcqDate"  readonly="true" class="lessthancurrdate form-control mandColorClass dateClass addColor" id="proAssAcqDate" onChange="getFinancialYear();" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" disabled="true"/>
										<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
										<form:hidden path="leastFinYear" id="minFinancialYear"/>	
										</div>
								</div>
									
								<label class="col-sm-2 control-label "><spring:message code="propertydetails.lastAssessmentDate" /></label>
								<div class="col-sm-4">
									<div class="input-group">
										<form:input	path="provisionalAssesmentMstDto.lastAssessmentDate"
											class="datepicker  form-control  dateClass addColor"
											id="lastAssessmentDate" placeholder="DD/MM/YYYY"
											autocomplete="off" disabled="true"/>
										<span class="input-group-addon"><i	class="fa fa-calendar"></i></span>
									</div>
								</div>
							</div>


						<div class="form-group">
							<label class="col-sm-2 control-label "><spring:message	code="propertydetails.reviseAssessmentDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input
										path="provisionalAssesmentMstDto.reviseAssessmentDate"
										class="datepicker  form-control  dateClass addColor"
										id="reviseAssessmentDate" placeholder="DD/MM/YYYY"
										autocomplete="off" disabled="true"/>
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
								</div>
							</div>
							<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true"/>
											</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label "><spring:message	code="unitdetails.constructPermissionDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input path="provisionalAssesmentMstDto.constructPermissionDate"
										class="datepicker-cpdate  form-control  dateClass addColor dateFrmt"												
										id="constructPermissionDate" placeholder="DD/MM/YYYY"
										autocomplete="off" disabled="true"/>
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>											
								</div>
							</div>
							<label for="permissionUseNo" class="col-sm-2 control-label">
									<spring:message	code="unitdetails.permissionUseNo" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" 
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
										id="permissionUseNo" disabled="true"/>
									</div>
						</div>

						<div class="form-group">		
							<label class="col-sm-2 control-label "><spring:message	code="unitdetails.ConstcompletionDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input type="text" path="provisionalAssesmentMstDto.assdYearConstruction" disabled="true" class="form-control datepicker2 mandColorClass dateClass addColor dateFrmt" id="yearOfConstruc" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" />						
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>								
								</div>
							</div>
							
							<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
								showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
								isMandatory="false" hasLookupAlphaNumericSort="true"
								hasSubLookupAlphaNumericSort="true" disabled="true"
								cssClass="form-control" showAll="false"  columnWidth="10%"/>
							
						</div>
									
					</div>
				</div>
				<div class="accordion-toggle ">
				
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#OwnshipDetail"><spring:message	code="property.Ownershipdetail" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="OwnshipDetail">	
						    
						    <div class="form-group">
								<label class="col-sm-2 control-label "
									for="OwnershipDetail"><spring:message code="property.ownershiptype" /></label>
									<div class="col-sm-4">
									<c:set var="baseLookupCode" value="OWT" /> <form:select path="provisionalAssesmentMstDto.assOwnerType"
											onchange="getOwnerTypeDetails()" class="form-control mandColorClass" id="ownerTypeId" data-rule-required="true" disabled="true">
												<form:option value="">
													<spring:message code="property.sel.optn.ownerType" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
													<form:option value="${lookUp.lookUpId}"	code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
									</form:select>
									</div>		
							</div>	
						    <jsp:include page="/jsp/property/changeInAssessmentOwnerDetails.jsp"></jsp:include> 					
					</div>
				</div>
				
				<div class="accordion-toggle ">
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#propertyAddress"><spring:message	code="property.Propertyaddress" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="propertyAddress">
					<div class="form-group">
							<apptags:input labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress"></apptags:input>
							<apptags:input labelCode="property.location" path="provisionalAssesmentMstDto.locationName"></apptags:input>			                
             		</div>
             		<div class="form-group">
			            	<apptags:input cssClass="hasNumber" labelCode="property.pincode" path="provisionalAssesmentMstDto.assPincode" maxlegnth="6"></apptags:input>	
							<label class="col-sm-2 control-label " for="areaName"><spring:message code="property.areaName" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.areaName" id="areaName"></form:input>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message	code="property.plotNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo"></form:input>
							</div>

							<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message	code="property.khata.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo"></form:input>
							</div>
						</div>

						<!-- Additional Address Details -->
						<div class="form-group">
							<label for="surveyType" class="col-sm-2 control-label "><spring:message	code="property.surveyType" /> </label>
							<c:set var="baseLookupCode" value="SUT" />
							<apptags:lookupField
								items="${command.getLevelData(baseLookupCode)}"
								path="provisionalAssesmentMstDto.surveyType"
								cssClass="form-control" hasChildLookup="false" hasId="true"
								showAll="false" selectOptionLabelCode="Select " />

							<label class="col-sm-2 control-label " for="tppSurveyNumber"><spring:message code="property.khasra.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppSurveyNumber"
									id="tppSurveyNumber"></form:input>
							</div>

						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message code="property.addiSurveyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiSurveyNo"
									id="addiSurveyNo"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message code="property.addiCityServeyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiCityServeyNo"
									id="addiCityServeyNo"></form:input>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="aaliNo"><spring:message	code="property.aaliNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" maxlength="20"></form:input>
							</div>

							<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message	code="property.chaaltaNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo"></form:input>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="clusterNo"><spring:message	code="property.clusterNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.clusterNo" id="clusterNo"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="buildingNo"><spring:message	code="property.buildingNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.buildingNo" id="buildingNo"></form:input>
							</div>
						</div>
						<c:if test="${command.getBillingMethod() ne 'I' }">
							<div class="accordion-toggle">
					    <h4 class="margin-top-0 margin-bottom-10 panel-title" id="scrollToDiv">
					        <a data-toggle="collapse" href="#UtilitySection"><spring:message code="utility.section" text="Utility Section"/></a>
					    </h4>
					    <div class="panel-collapse collapse in" id="UtilitySection">
					        <div class="table-responsive">
					            <c:set var="d" value="0" scope="page" />
					            <table id="utilitySectionTable" class="table table-striped table-bordered appendableClass">
					                <tbody>
					                    <tr>
					                        <th><spring:message code="property.electno" text="Electricity Billing Unit Number"/></th>
					                        <th><spring:message code="property.electConsNo" text="Electricity Consumer Number"/></th>
					                        <th><spring:message code="property.electMeterNo" text="Electricity Meter Number"/></th>
					                    </tr>
					                    	<tr>
					                            <td>
								                    <form:input path="provisionalAssesmentMstDto.electricBillUnitNo" type="text" class="form-control text-center" id="newElectricBillUnitNo${status.count-1}"/>
								                </td>
								                <td>
								                    <form:input path="provisionalAssesmentMstDto.electricConsumerNo" type="text" class="form-control text-center" id="newElectricConsumerNo${status.count-1}"/>
								                </td>
								                <td>
								                    <form:input path="provisionalAssesmentMstDto.electricMeterNo" type="text" class="form-control text-center" id="newElectricMeterNo${status.count-1}"/>
								                </td>
					                        </tr>
					                </tbody>
					            </table>
					        </div>
					    </div>
					</div>
					</c:if>

						<c:choose>
							<c:when test="${command.flatCountMax eq 'Y'}">
								<jsp:include page="/jsp/property/propertyEditUnitDetailsForMoreFlatsForm.jsp"></jsp:include>
							</c:when>
							<c:otherwise>
								<jsp:include page="/jsp/property/propertyEditUnitDetailsForm.jsp"></jsp:include>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				
				<div class="accordion-toggle ">
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#updateDetail"><spring:message	code="" text="Update Below Details"/></a>
				</h4>
				<div class="panel-collapse collapse in" id="updateDetail">	
				<div class="form-group">
					<label class="col-sm-2 control-label " for="clusterNo"><spring:message	code="property.clusterNo" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control hasNumber"
							path="provisionalAssesmentMstDto.clusterNo" id="clusterNo"></form:input>
					</div>
					<label class="col-sm-2 control-label " for="buildingNo"><spring:message	code="property.buildingNo" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control hasNumber"
							path="provisionalAssesmentMstDto.buildingNo" id="buildingNo"></form:input>
					</div>
				</div>
				<c:choose>
				<c:when test="${command.getBillingMethod() eq 'I' }">
				  <div class="form-group">
					<label class="col-sm-2 control-label " for="occupierMobNo"><spring:message	code="unitdetails.occupierMobNo" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control "
							path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierMobNo" id="occupierMobNo" maxlength="10"></form:input>
					</div>
					<label class="col-sm-2 control-label " for="occupierEmail"><spring:message	code="unitdetails.occupierEmail" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control "
							path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierEmail" id="emailId" onblur="isValidEmail();"></form:input>
							<span id="email" style="padding-left: 5px; padding-bottom: 3px; font-size: 15px; font-family: serif;"></span>
					</div>
				</div>
				</c:when>
				<c:otherwise>
					<div class="form-group">
						<label class="col-sm-2 control-label " for="mobileNo"><spring:message code="ownersdetail.mobileno" /></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control "
								path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" id="mobileNo" maxlength="10"></form:input>
						</div>
						<label class="col-sm-2 control-label " for="emailId"><spring:message code="property.email" /></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control "
								path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" id="emailId" onblur="isValidEmail();"></form:input>
								<span id="email" style="padding-left: 5px; padding-bottom: 3px; font-size: 15px; font-family: serif;"></span>
						</div>
					</div>
				</c:otherwise>
				</c:choose>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control" for="remarks"><spring:message code="property.enter.remark" /></label>
					<div class="col-sm-4">
						<form:textarea type="text" class="form-control "
							path="provisionalAssesmentMstDto.remarks" maxlength="1000" id="remarks"></form:textarea>
					</div>
				</div>
				</div>
				</div>
				<!--------------------------------Upload Documents------------------------------>
					<div class="panel-group accordion-toggle" id="accordion_single_collapse">
						<div class="panel panel-default">
						<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="" class=""
								data-parent="#accordion_single_collapse" href="#a1"> <spring:message
									code="property.documentsupload" text="Documents Upload" /></a>
						</h4>
						</div>
					
					<div id="a1" class="panel-collapse collapse in">
						<div class="panel-body">
							<div id="doCommonFileAttachment">
								<div class="table-responsive">
									<c:set var="cd" value="0" scope="page" />
										<table class="table table-bordered table-striped" id="attachCommonDoc">
											<tr>
												<th><spring:message	code="work.estimate.document.description"
														text="Document Description" /></th>
												<th><spring:message code="work.estimate.upload"
														text="Upload Document" /></th>
											</tr>
									<c:choose>
									<c:when test="${empty command.getCommonFileAttachment()}"> 
											<tr class="appendableUploadClass">
												<td><form:input	path="commonFileAttachment[${cd}].doc_DESC_ENGL"
														class=" form-control" /></td>
												<td class="text-center"><apptags:formField fieldType="7"
														fieldPath="commonFileAttachment[${cd}].doc_DESC_ENGL"
														currentCount="${cd}" showFileNameHTMLId="true"
														fileSize="WORK_COMMON_MAX_SIZE" isMandatory="false"
														maxFileCount="CHECK_LIST_MAX_COUNT"
														validnFunction="CARE_VALIDATION_EXTENSION">
													</apptags:formField>
													<div class="col-sm-12">
															<small class="text-blue-2 "> <spring:message
															code="property.file.upload.validator"
															text="Documents/Images up to 20 MB can be upload.only pdf,png,jpg is allowed." />
															</small>
					  								</div>
													</td>
												
											</tr>
									</c:when>
											<c:otherwise>
											
											<c:forEach items="${command.getCommonFileAttachment()}" var="lookUp">
											<tr class="appendableUploadClass">
												<c:set var="cd" value="0" scope="page" />
												<td><form:input	path="commonFileAttachment[${cd}].doc_DESC_ENGL"
														class=" form-control" /></td>
												<td class="text-center">
													<apptags:formField	fieldType="7"
														fieldPath="commonFileAttachment[${cd}].doc_DESC_ENGL"
														currentCount="${cd}" showFileNameHTMLId="true"
														fileSize="WORK_COMMON_MAX_SIZE" isMandatory="true"
														maxFileCount="CHECK_LIST_MAX_COUNT"
														validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
														callbackOtherTask="callbackOtherTask()">
													</apptags:formField></td>
													<c:set var="cd" value="${cd+1}" scope="page" />
											</tr>
											</c:forEach>
											</c:otherwise>
											</c:choose>
										</table>
								</div>
							</div>
						</div>
						</div>
						</div>
					</div>
				
					<!--------------------------------End Upload Documents----------------------------->
				<div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success" id="save"
						onclick="updatePropertyInfo(this)">
						<spring:message code="property.btn.submit" text="Submit"/>
					</button>
					
					<apptags:backButton url="UpdatePropertyInfo.html"></apptags:backButton>

				</div>
			</form:form>
		</div>
	</div>
</div>
