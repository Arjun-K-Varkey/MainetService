<!-- Start JSP Necessary Tags -->
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
<script type="text/javascript" src="js/property/changeInAssessmentForm.js" ></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/property/changeUnitDetails.js"></script>
<script type="text/javascript" src="js/property/propertyEditableForm.js"></script>

<c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
  <apptags:breadcrumb></apptags:breadcrumb>
</c:if>
<div class="content"> 
<div class="widget">
 <c:if test="${empty  command.provisionalAssesmentMstDto.assesssmentCategory}">
        <div class="widget-header"> 
          	<h2>
				<b><spring:message code="property.edit.note" text="Property Edit Form" /></b>
			</h2>
			<apptags:helpDoc url="PropertyEditableForm.html"></apptags:helpDoc>	
      
       </div> 
  </c:if>   
  
     
        <div class="widget-content padding">
          
          <!-- Start Form -->
			<form:form action="PropertyEditableForm.html"
					class="form-horizontal form" name="frmChangeAssessmentForm"
					id="frmChangeAssessmentForm">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			<form:hidden path="assType" id="assType"/>
			<form:hidden path="serviceShortCode" id="serviceShortCode"/>
				<spring:message code="property.changeInAss.note" />
				
				<div class="accordion-toggle ">
					<h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#propertyMaster"><spring:message
								code="property.propertyMaster" text="Property Information" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="propertyMaster">
						
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="property.PropertyNo"
								path="provisionalAssesmentMstDto.assNo" maxlegnth="40" isDisabled="true"></apptags:input>
								
							<apptags:radio 
								radioLabel="Active,Cancel" radioValue="A,C"
								labelCode="property.propertyStatus"
								path="provisionalAssesmentMstDto.assActive"></apptags:radio>
						</div>
						
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.refPropNo"
								path="provisionalAssesmentMstDto.refPropNo" maxlegnth="40" isReadonly="true"></apptags:input>

							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.gisRefNo"
								path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"></apptags:input>

						</div>

						<jsp:include page="/jsp/property/propertyEditZoneDetails.jsp"></jsp:include>

						<div class="form-group">
							<apptags:lookupFieldSet cssClass="form-control required-control"
							baseLookupCode="DST" hasId="true" pathPrefix="provisionalAssesmentMstDto.disputedType"  
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true" showAll="false" isMandatory="true"/>
		
						</div>
						<%-- <jsp:include
							page="/jsp/property/propertyEditBuildingDetailForm.jsp"></jsp:include> --%>
							
							<div class="form-group">
		<label class="col-sm-2 control-label required-control"><spring:message code="propertydetails.firstAssessmentDate"/></label>
		<div class="col-sm-4">
		<div class="input-group"> 
		<form:input path="provisionalAssesmentMstDto.assAcqDate"  readonly="true" class="lessthancurrdate form-control mandColorClass dateClass addColor" id="proAssAcqDate" onChange="getFinancialYear();" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" disabled="true"/>
		<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
		<form:hidden path="leastFinYear" id="minFinancialYear"/>	
		</div>
		</div>
		</div>


						<div class="form-group">

							<label class="col-sm-2 control-label "><spring:message
									code="propertydetails.lastAssessmentDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input
										path="provisionalAssesmentMstDto.lastAssessmentDate"
										class="datepicker  form-control  dateClass addColor"
										id="lastAssessmentDate" placeholder="DD/MM/YYYY"
										autocomplete="off" disabled="true"/>
									<span class="input-group-addon"><i
										class="fa fa-calendar"></i></span>
								</div>
							</div>
							
							<label class="col-sm-2 control-label "><spring:message
									code="propertydetails.reviseAssessmentDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input
										path="provisionalAssesmentMstDto.reviseAssessmentDate"
										class="datepicker  form-control  dateClass addColor"
										id="reviseAssessmentDate" placeholder="DD/MM/YYYY"
										autocomplete="off" disabled="true"/>
									<span class="input-group-addon"><i
										class="fa fa-calendar"></i></span>
								</div>
							</div>
						</div>


								<div class="form-group">
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
									<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true" disabled="true"
									cssClass="form-control" showAll="false"  columnWidth="10%"/>
						</div>			

					</div>
				</div>
				<!-- Start Each Section -->			
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
											onchange="getOwnerTypeDetails()" class="form-control mandColorClass" id="ownerTypeId" data-rule-required="true" disabled="true">
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
							
						    <jsp:include page="/jsp/property/changeInAssessmentOwnerDetails.jsp"></jsp:include> 					
					</div> 
				 
<!-------------------------------------------------------------Land Details------------------------------------------------------------------ -->
            
			
				<div class="panel-collapse collapse in margin-top-15" id="propertyDetail">
				
				
<c:choose>
	 <c:when test="${command.getLandTypePrefix() eq 'KPK'}">
		<div class="form-group">		
		
			<apptags:input labelCode="property.district" path="provisionalAssesmentMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="provisionalAssesmentMstDto.assTahasilDesc" isDisabled="true"></apptags:input>
		</div>
		<div class="form-group">						
 			<apptags:input labelCode="propertydetails.village" path="provisionalAssesmentMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
			<label class="col-sm-2 control-label required-control"
									for="khasra"><spring:message code="propertydetails.csnno" text="Khasra No"/></label>
				<div class="col-sm-4">									
				<form:input path="provisionalAssesmentMstDto.tppPlotNoCs" type="text" class="form-control mandColorClass" id="khasara" disabled="true"/>
				</div>
		</div>
		
		<div class="form-group">
		<div class="text-center padding-top-10">
		<c:if test="${command.getAssType() eq 'C'}">  
		<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
		</c:if>	
		
		</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
			
		
	<c:if test="${command.getAssType() ne 'C'}">  

	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#khasra"><spring:message code="property.landDetails.KhasraDetails" text="Khasra Details"/></a>
	</h4>
	<div class="panel-collapse collapse in" id="khasra">
	
	<div class="form-group">		
	<apptags:input labelCode="property.landDetails.ownerFirstName" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].ocdname"></apptags:input>	
	<apptags:input labelCode="property.landDetails.fathersFirstName" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].ofather"></apptags:input>
	
	</div>					
			
	<div class="form-group">		
	<apptags:input labelCode="property.landDetails.GenderOfOwner" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].gender"></apptags:input>
	<apptags:input labelCode="property.landDetails.CasteOfOwner" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].ocastenm"></apptags:input>				
	</div>	
	
	<div class="form-group">		
	<apptags:input labelCode="property.landDetails.OwnerAddress" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].oaddr1"></apptags:input>
	<apptags:input labelCode="property.landDetails.OwnerMobileNo" path="arrayOfKhasraDetails.KhasraDetails[0].ownerdetails[0].OwnerDetails[0].mobileno"></apptags:input>				
	</div>	
	
	<div class="form-group">	
	 	<apptags:input labelCode="property.landDetails.LandArea" path="arrayOfKhasraDetails.KhasraDetails[0].Area"></apptags:input>
	 	<apptags:input labelCode="property.landDetails.OwnerType" path="arrayOfKhasraDetails.KhasraDetails[0].OwnerType"></apptags:input>
	
	</div>
	
 </div>
 </c:if>
</c:when>

<c:when test="${command.getLandTypePrefix() eq 'NZL'}">
		<div class="form-group">			
			<apptags:input labelCode="property.district" path="provisionalAssesmentMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="provisionalAssesmentMstDto.assTahasilDesc" isDisabled="true"></apptags:input>		
		</div>
			
		<div class="form-group">			
				<apptags:input labelCode="propertydetails.village" path="provisionalAssesmentMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
				<apptags:input labelCode="Mohalla" path="provisionalAssesmentMstDto.mohallaDesc" isDisabled="true"></apptags:input> 								
		</div>	
		
		<div class="form-group">
				<apptags:input labelCode="propertydetails.streetno" path="provisionalAssesmentMstDto.assStreetNoDesc" isDisabled="true"></apptags:input> 												
				<apptags:input labelCode="propertydetails.plotno" path="provisionalAssesmentMstDto.tppPlotNo" isDisabled="true"></apptags:input>
		</div>		
		
		<div class="form-group">
			<div class="text-center padding-top-10">
			<c:if test="${command.getAssType() eq 'C'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
			</c:if>	
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
	<c:if test="${command.getAssType() ne 'C'}">  

			<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#details"><spring:message code="property.landDetails.NajoolDetails" text="Najool Details"/></a>
			</h4>
			<div class="panel-collapse collapse in" id="details">
			<div class="form-group">		
						<apptags:input labelCode="property.landDetails.OwnerName" isDisabled="true" path="arrayOfPlotDetails.PlotDetails[0].najoolOwnerDetails[0].najoolOwnerDetailsList[0].OwnerName"></apptags:input>	
						<apptags:input labelCode="property.landDetails.Department" isDisabled="true" path="arrayOfPlotDetails.PlotDetails[0].Department"></apptags:input>				
			</div>					
					
			<div class="form-group">		
						<apptags:input labelCode="property.landDetails.Area" isDisabled="true" path="arrayOfPlotDetails.PlotDetails[0].area_foot"></apptags:input>
						<apptags:input labelCode="property.landDetails.rights_type_nm" isDisabled="true" path="arrayOfPlotDetails.PlotDetails[0].rights_type_nm"></apptags:input>				
			</div>	
			
			<div class="form-group">		
						<apptags:textArea labelCode="property.landDetails.Remark" path="arrayOfPlotDetails.PlotDetails[0].remark" isDisabled="true"></apptags:textArea>			
			</div>
				
		 	</div>
	</c:if>
</c:when>
	


<c:when test="${command.getLandTypePrefix() eq 'DIV'}">

		<div class="form-group">			
			<apptags:input labelCode="property.district" path="provisionalAssesmentMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="provisionalAssesmentMstDto.assTahasilDesc" isDisabled="true"></apptags:input>		
		</div>
			
		<div class="form-group">			
				<apptags:input labelCode="propertydetails.village" path="provisionalAssesmentMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
				<apptags:input labelCode="Mohalla" path="provisionalAssesmentMstDto.mohallaDesc" isDisabled="true"></apptags:input> 								
		</div>	
		
		<div class="form-group">
				<apptags:input labelCode="propertydetails.streetno" path="provisionalAssesmentMstDto.assStreetNoDesc" isDisabled="true"></apptags:input> 												
				<apptags:input labelCode="propertydetails.plotno" path="provisionalAssesmentMstDto.tppPlotNo" isDisabled="true"></apptags:input>
		</div>	
		
		<div class="form-group">
			<div class="text-center padding-top-10">
			<c:if test="${command.getAssType() eq 'C'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
			</c:if>	
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
	<c:if test="${command.getAssType() ne 'C'}">	

			<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#details"><spring:message code="property.landDetails.DiversionDetails" text="Diversion Details"/></a>
			</h4>
			<div class="panel-collapse collapse in" id="details">
			<div class="form-group">		
					<apptags:input labelCode="property.landDetails.OwnerName" isDisabled="true" path="arrayOfDiversionPlotDetails.DiversionPlotDetails[0].diversionOwnerDetails[0].diversionOwnerDetailsList[0].OwnerName"></apptags:input>	
					<apptags:input labelCode="property.landDetails.Department" isDisabled="true" path="arrayOfDiversionPlotDetails.DiversionPlotDetails[0].Department"></apptags:input>				
			</div>					
					
			<div class="form-group">		
					<apptags:input labelCode="property.landDetails.Area" isDisabled="true" path="arrayOfDiversionPlotDetails.DiversionPlotDetails[0].area_foot"></apptags:input>
					<apptags:input labelCode="property.landDetails.rights_type_nm" isDisabled="true" path="arrayOfDiversionPlotDetails.DiversionPlotDetails[0].rights_type_nm"></apptags:input>				
			</div>	
			<div class="form-group">		
					<apptags:textArea labelCode="property.landDetails.Remark" path="arrayOfDiversionPlotDetails.DiversionPlotDetails[0].remark" isDisabled="true"></apptags:textArea>			
			</div>
				
		 	</div>
	</c:if>
</c:when>
</c:choose>
				
	           			
	            </div> 
            
            
<!------------------------------------------------------------Property Address Details------------------------------------------------------->
			
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#propertyAddress"><spring:message
					code="property.Propertyaddress" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="propertyAddress">
						<div class="form-group">
							<%-- isReadonly="${command.noOfDaysEditableFlag eq 'N' ? true : false }" --%>
							<apptags:input labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress" isReadonly="true"></apptags:input>
							<apptags:input labelCode="property.location" path="provisionalAssesmentMstDto.locationName" ></apptags:input>			                
			           
			          
			
             			</div>
                  
			            <div class="form-group">
			            	<apptags:input cssClass="hasNumber" labelCode="property.pincode" path="provisionalAssesmentMstDto.assPincode" maxlegnth="6" ></apptags:input>	

							<label class="col-sm-2 control-label " for="areaName"><spring:message
									code="property.areaName" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.areaName" id="areaName"></form:input>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
									code="property.plotNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo"></form:input>
							</div>

							<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
									code="property.khata.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo"></form:input>
							</div>


						</div>

						<!-- Additional Address Details -->
						<div class="form-group">
							<label for="surveyType" class="col-sm-2 control-label "><spring:message
									code="property.surveyType" /> </label>
							<c:set var="baseLookupCode" value="SUT" />
							<apptags:lookupField
								items="${command.getLevelData(baseLookupCode)}"
								path="provisionalAssesmentMstDto.surveyType"
								cssClass="form-control" hasChildLookup="false" hasId="true"
								showAll="false" selectOptionLabelCode="Select " />

							<label class="col-sm-2 control-label " for="tppSurveyNumber"><spring:message
									code="property.khasra.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppSurveyNumber"
									id="tppSurveyNumber"></form:input>
							</div>

						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message
									code="property.addiSurveyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiSurveyNo"
									id="addiSurveyNo"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message
									code="property.addiCityServeyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiCityServeyNo"
									id="addiCityServeyNo"></form:input>
							</div>
						</div>

						<div class="form-group">

							<label class="col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" maxlength="20"></form:input>
							</div>

							<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message
									code="property.chaaltaNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo"></form:input>
							</div>
						</div>

						<div class="form-group">


							<label class="col-sm-2 control-label " for="clusterNo"><spring:message
									code="property.clusterNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.clusterNo" id="clusterNo"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="buildingNo"><spring:message
									code="property.buildingNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.buildingNo" id="buildingNo"></form:input>
							</div>
						</div>

<c:if test="${command.provisionalAssesmentMstDto.billMethod eq '100000330'}">
<div class="accordion-toggle">
					    <h4 class="margin-top-0 margin-bottom-10 panel-title" id="scrollToDiv">
					        <a data-toggle="collapse" href="#UtilitySection"><spring:message text="Utility Section"/></a>
					    </h4>
					    <div class="panel-collapse collapse in" id="UtilitySection">
					        <div class="table-responsive">
					            <c:set var="d" value="0" scope="page" />
					            <table id="utilitySectionTable" class="table table-striped table-bordered appendableClass">
					                <tbody>
					                    <tr>
					                        <th><spring:message text="Electricity Billing Unit Number"/></th>
					                        <th><spring:message text="Electricity Consumer Number"/></th>
					                        <th><spring:message text="Electricity Meter Number"/></th>
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
												<th><spring:message
														code="work.estimate.document.description"
														text="Document Description" /></th>
												<th><spring:message code="work.estimate.upload"
														text="Upload Document" /></th>
												<!-- <th scope="col" width="8%"><a
													onclick='doCommonFileAttachment(this);'
													class="btn btn-blue-2 btn-sm addButton"><i
														class="fa fa-plus-circle"></i></a></th> -->
											</tr>

									<c:choose>
									
									<c:when test="${empty command.getCommonFileAttachment()}"> 
											<tr class="appendableUploadClass">
												<td><form:input
														path="commonFileAttachment[${cd}].doc_DESC_ENGL"
														class=" form-control" /></td>
												<td class="text-center"><apptags:formField
														fieldType="7"
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
												<%-- <td class="text-center"><a href='#' id="0_file_${cd}"
													onclick="doFileDelete(this)"
													class='btn btn-danger btn-sm delButton'><i
														class="fa fa-trash"></i></a></td> --%>
											</tr>
											</c:when>
											<c:otherwise>
											
											<c:forEach items="${command.getCommonFileAttachment()}" var="lookUp">
											<tr class="appendableUploadClass">
												<c:set var="cd" value="0" scope="page" />
												<td><form:input
														path="commonFileAttachment[${cd}].doc_DESC_ENGL"
														class=" form-control" /></td>
												<td class="text-center"><apptags:formField
														fieldType="7"
														fieldPath="commonFileAttachment[${cd}].doc_DESC_ENGL"
														currentCount="${cd}" showFileNameHTMLId="true"
														fileSize="WORK_COMMON_MAX_SIZE" isMandatory="true"
														maxFileCount="CHECK_LIST_MAX_COUNT"
														validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
														callbackOtherTask="callbackOtherTask()">
													</apptags:formField></td>
												<%-- <td class="text-center"><a href='#' id="0_file_${cd}"
													onclick="doFileDelete(this)"
													class='btn btn-danger btn-sm delButton'><i
														class="fa fa-trash"></i></a></td> --%>
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
			
		<div class="form-group">
			<apptags:textArea labelCode="property.landDetails.Remark" path="provisionalAssesmentMstDto.remarks" maxlegnth="1000" cssClass="preventSpace" isMandatory="true"></apptags:textArea>
		</div>
					
			<!-- Start button -->
		<div class="text-center padding-10">
				<button type="button" class="btn btn-success"
				onclick="savePropertyEditForm(this)" id="checkList"><spring:message code="" text="Submit"/></button>
				<button type="button" class="btn btn-danger"
				onclick="backToPropertyEditableForm(this)" id="backToSearchPage"><spring:message code="property.Back"/></button>
		</div>
			
			<!--  End button -->
</form:form>

<!--  End form -->
</div>
</div>
<c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
 </div> 
</c:if>
