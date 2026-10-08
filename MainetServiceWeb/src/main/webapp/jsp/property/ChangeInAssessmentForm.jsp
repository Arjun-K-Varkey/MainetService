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
<script src="js/mainet/jQueryMaskedInputPlugin.js"></script> 
<script type="text/javascript" src="js/property/changeInAssessmentForm.js" ></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/property/changeUnitDetails.js"></script>
<style>
.addColor{
	background-color: #fff !important
}
input[type='text'], textarea, #jointOwnerTable {
	text-transform: uppercase;
}
</style>
<c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
  <apptags:breadcrumb></apptags:breadcrumb>
</c:if>
<div class="content"> 
<div class="widget">
 <c:if test="${empty  command.provisionalAssesmentMstDto.assesssmentCategory}">
        <div class="widget-header"> 
          <h2><b><spring:message code="property.ChangeInAssessment"/></b></h2> 
          <%-- <div class="additional-btn"> <a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i><span class="hide"><spring:message code="property.Help"/></span></a></div> --%>
      	<apptags:helpDoc url="ChangeInAssessmentForm.html"></apptags:helpDoc>	
      
       </div> 
  </c:if>   
  
     
        <div class="widget-content padding">
          
          <!-- Start Form -->
			<form:form action="ChangeInAssessmentForm.html"
					class="form-horizontal form" name="frmChangeAssessmentForm"
					id="frmChangeAssessmentForm">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			<form:hidden path="assType" id="assType"/>
			<form:hidden path="authLevel" id="authLevel"/>
			<form:hidden path="serviceShortCode" id="serviceShortCode"/>
			<form:hidden path="draftApplication" id="draftApplication"/>
			<form:hidden path="checklistApplicable" id="checklistApplicable" />

				<spring:message code="property.changeInAss.note" />
				<c:if test="${'Y' eq command.draftApplication }">
					<b><i class="text-red-1"><spring:message code="property.save.draft.application" text="This application is already saved and drafted."/></i></b>
				</c:if>
				
				<div class="accordion-toggle ">
					<h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#propertyMaster"><spring:message
								code="property.propertyMaster" text="Property Information" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="propertyMaster">
						
						<div class="form-group">
							<apptags:radio 
								radioLabel="Yes,No" radioValue="Y,N"
								labelCode="property.auto.adj.flag" defaultCheckedValue="N"
								path="provisionalAssesmentMstDto.autoAdjustmentFlag"></apptags:radio>
						</div>
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="property.PropertyNo"
								path="provisionalAssesmentMstDto.assNo" maxlegnth="40" isDisabled="true"></apptags:input>
								
							<apptags:textArea cssClass="preventSpace"
								labelCode="property.remark"
								path="provisionalAssesmentMstDto.remarks" maxlegnth="1000" isDisabled="false" isMandatory="true"></apptags:textArea>
						</div>
						
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.refPropNo"
								path="provisionalAssesmentMstDto.refPropNo" maxlegnth="40" isDisabled="true"></apptags:input>

							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.gisRefNo"
								path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"></apptags:input>

						</div>

						<jsp:include page="/jsp/property/NewPropertyTaxZoneDetails.jsp"></jsp:include>

						<%-- <jsp:include
							page="/jsp/property/SelfAssessmentBuildingDetailForm.jsp"></jsp:include> --%>


						<div class="form-group">
							
							<label class="col-sm-2 control-label "><spring:message code="propertydetails.firstAssessmentDate"/></label>
							<div class="col-sm-4">
								<div class="input-group"> 
									<form:input path="provisionalAssesmentMstDto.assAcqDate"  readonly="true" class="lessthancurrdate form-control mandColorClass dateClass addColor" id="proAssAcqDate" onChange="getFinancialYear();" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" disabled="true"/>
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
									<form:hidden path="leastFinYear" id="minFinancialYear" />	
								</div>
							</div>
							<label class="col-sm-2 control-label required-control"><spring:message
									code="propertydetails.lastAssessmentDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
									<form:input
										path="provisionalAssesmentMstDto.lastAssessmentDate"
										class="  form-control dateClass addColor"
										id="lastAssessmentDate" onblur="isLastAssessmentDate();" placeholder="DD/MM/YYYY"
										autocomplete="off" disabled="false" maxlength="10"/>
									<span class="input-group-addon"><i
										class="fa fa-calendar"></i></span>
								</div>
								<span id="lastAssDate"
									style="padding-left: 5px; padding-bottom: 3px; font-size: 15px; font-family: serif;"></span>
							</div>
							
						</div>

						<div class="form-group">
						
						<label for="constructPermissionNo" class="col-sm-2 control-label">
							<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true"/>
						</div>		
									
						<label class="col-sm-2 control-label "><spring:message	code="unitdetails.constructPermissionDate" /></label>
						<div class="col-sm-4">
							<div class="input-group">
								<form:input path="provisionalAssesmentMstDto.constructPermissionDate" class=" form-control  dateClass addColor "	id="constructPermissionDate" 
												placeholder="DD/MM/YYYY" onblur="isCPDate();" autocomplete="off" disabled="false" maxlength="10"/>
											<span class="input-group-addon"><i class="fa fa-calendar"></i></span>											
							</div>
							<span id="cpDate"
									style="padding-left: 5px; padding-bottom: 3px; font-size: 15px; font-family: serif;"></span>
						</div>
						
						</div>	
						
						<div class="form-group">		
								
							<label for="permissionUseNo" class="col-sm-2 control-label"><spring:message	code="unitdetails.permissionUseNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" id="permissionUseNo" disabled="true"/>
							</div>								
									
							<label class="col-sm-2 control-label "><spring:message	code="unitdetails.ConstcompletionDate" /></label>
							<div class="col-sm-4">
								<div class="input-group">
								<form:input type="text" path="provisionalAssesmentMstDto.assdYearConstruction" class="form-control mandColorClass dateClass addColor" 
								id="yearOfConstruc" placeholder="DD/MM/YYYY" autocomplete="off" disabled="false" maxlength="10" onblur="isYCDate();"/>						
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>								
								</div>
								<span id="ycDate"
									style="padding-left: 5px; padding-bottom: 3px; font-size: 15px; font-family: serif;"></span>
							</div>
							
						</div>	
						
						<div class="form-group">						
									<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true"
									cssClass="form-control required-control" showAll="false"  columnWidth="10%"/>
						</div>
											
						<%-- <div class="form-group">
							<label class="col-sm-2 control-label"><spring:message
									code="property.landType" /> </label>
							<div class="col-sm-4">
								<c:set var="baseLookupCode" value="LDT" />
								<form:select path="provisionalAssesmentMstDto.assLandType"
									onchange="getLandTypeDetails()"
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
							</div>
						</div> --%>

					</div>
				</div>
				<!-- Start Each Section -->			
			<div class="accordion-toggle ">
				
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
					code="property.Ownershipdetail" /></a>
				</h4>
					<div class="panel-collapse collapse in" id="OwnshipDetail">	
					<%-- <c:if test="${command.noOfDaysEditableFlag ne 'Y'}">
					<div class="form-group">
								<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
								<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" readonly="${command.noOfDaysEditableFlag eq 'N' ? true : false }"/>
				 				<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
				 				<form:hidden  path="noOfDaysEditableFlag"  id="noOfDaysEditableFlag"/>
				 												
								</div>
						    </div>	
					</c:if>	 --%>					
						    
						    <%-- <c:if test="${command.noOfDaysEditableFlag eq 'Y'}"> --%>
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
							<%-- </c:if>	 --%>   
							
						    <jsp:include page="/jsp/property/changeInAssessmentOwnerDetails.jsp"></jsp:include> 					
					</div> 
				 
<!-------------------------------------------------------------Land Details------------------------------------------------------------------ -->
            
			
				<div class="panel-collapse collapse in margin-top-15" id="propertyDetail">
				
				
						<%-- <div class="form-group">
								<apptags:input cssClass="alphaNumeric preventSpace" labelCode="propertydetails.oldpropertyno" path="provisionalAssesmentMstDto.assOldpropno" maxlegnth="20" isReadonly="true"></apptags:input>				
								<apptags:input cssClass="alphaNumeric preventSpace" labelCode="property.landType" path="provisionalAssesmentMstDto.assLandTypeDesc"  isReadonly="true"></apptags:input>
			           			<form:hidden  path="landTypePrefix" class="form-control landValue"/> 
		
						</div> --%>
						
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
							<apptags:textArea labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress" isReadonly="true"></apptags:textArea>
							<%-- <apptags:input labelCode="property.location" path="provisionalAssesmentMstDto.locationName" ></apptags:input> --%>			                
			           
			          		<label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message
									code="property.addiCityServeyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiCityServeyNo"
									id="addiCityServeyNo"></form:input>
							</div>
			
             			</div>
                  
			            <div class="form-group">
			            	<%-- <apptags:input labelCode="property.pincode" path="provisionalAssesmentMstDto.assPincode" ></apptags:input>	 --%>

							<label class="col-sm-2 control-label " for="areaName"><spring:message
									code="property.areaName" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.areaName" id="areaName"></form:input>
							</div>
							
							<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message
									code="property.chaaltaNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo" disabled="true"></form:input>
							</div>
						</div>

						<div class="form-group">


							<label class="col-sm-2 control-label " for="clusterNo"><spring:message
									code="property.clusterNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.clusterNo" id="clusterNo" disabled="true"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="buildingNo"><spring:message
									code="property.buildingNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.buildingNo" id="buildingNo" disabled="true"></form:input>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
									code="property.plotNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo" disabled="true"></form:input>
							</div>

							<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
									code="property.khata.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo" disabled="true"></form:input>
							</div>


						</div>

						<!-- Additional Address Details -->
						<div class="form-group" id="surveyty">
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
							<%-- <label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message
									code="property.addiSurveyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiSurveyNo"
									id="addiSurveyNo"></form:input>
							</div> --%>
							<label class="col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" disabled="true"></form:input>
							</div>
						</div>

					</div>
                
<!------------------------------------------------Correspondence Address Details --------------------------------------------------------------------->
			
<%-- 				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#correspondenceAddress"><spring:message
					code="property.correspondenceaddress" /></a>
				</h4>
            
				<div class="panel-collapse collapse in" id="correspondenceAddress">	
					<div class="form-group">     
							<apptags:input labelCode="property.correspondenceaddress" path="provisionalAssesmentMstDto.assCorrAddress" ></apptags:input>            
			              	<apptags:input labelCode="property.pincode" path="provisionalAssesmentMstDto.assCorrPincode" ></apptags:input>
			              
		            </div>            
		           
	            </div> --%>
         
<!------------------------------------------------------------Last Payment Details------------------------------------------------------------------------>
			
				<%-- <h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#LastBillPayment"><spring:message
					code="property.lastpayment" /></a>
				</h4>
				
				<div class="panel-collapse collapse in" id="LastBillPayment">			
					<div class="form-group">
					
						<label class="col-sm-2 control-label">
						<spring:message code="property.billpayment" text=""/>
			<!-- 			<span class="mand">*</span> -->
						</label>
						<div class="col-sm-4">
						<label for="notApplicaple" class="radio-inline">
						<form:radiobutton path="provisionalAssesmentMstDto.proAssBillPayment" value="N" name="billPayment" id="notApplicaple"  disabled="true"/>
						<spring:message code="property.na"/></label>
						<label for="manual" class="radio-inline">
						<form:radiobutton path="provisionalAssesmentMstDto.proAssBillPayment" value="M" name="billPayment"  id="manual" disabled="true"/>
						<spring:message code="property.manual"/></label>
						<label for="computerized" class="radio-inline">
						<form:radiobutton path="provisionalAssesmentMstDto.proAssBillPayment" value="C" name="billPayment" id="computerized"  disabled="true"/>
						<spring:message code="property.computerized"/></label> 
						</div>	
				    </div>
				
		            
		            <div class="form-group"> 
		            	<apptags:input labelCode="property.receiptno" path="provisionalAssesmentMstDto.assLpReceiptNo" isReadonly="true"></apptags:input> 
		            	<apptags:input labelCode="property.receiptamount" path="provisionalAssesmentMstDto.assLpReceiptAmt" isReadonly="true"></apptags:input>               	              	          		           		
		            </div>
					
					 <div class="form-group">
					 	<apptags:input labelCode="property.receiptdate" path="provisionalAssesmentMstDto.assLpReceiptDate" isReadonly="true" cssClass="dateClass"></apptags:input>
					 	<apptags:input labelCode="property.lastpaymentupto" path="provisionalAssesmentMstDto.proAssLpYearDesc" isReadonly="true"></apptags:input>
					 	
					 </div>
				
					<div class="form-group"> 
						<apptags:input labelCode="property.billamount" path="provisionalAssesmentMstDto.billAmount" isReadonly="true"></apptags:input>                 
			             <apptags:input labelCode="property.OutStandingAmount" path="provisionalAssesmentMstDto.outstandingAmount" isReadonly="true"></apptags:input> 
			              	
		            </div>
	        	</div> --%>
	        	
	        	
<!---------------------------------------------------------------------Tax-Zone Details---------------------------------------------------------------->
<%-- 			<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#taxZoneDetails"><spring:message
					code="property.taxZoneDetails" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="taxZoneDetails">
					<div class="form-group">
						<apptags:lookupFieldSet cssClass="form-control required-control"
								baseLookupCode="WZB" hasId="true" pathPrefix="provisionalAssesmentMstDto.assWard"  
								hasLookupAlphaNumericSort="true"
								hasSubLookupAlphaNumericSort="true" showAll="false" isMandatory="true" disabled="${command.noOfDaysEditableFlag eq 'N' ? true : false }"/>
					
					</div>
					<div class="form-group">
						<label for="road-type" class="col-sm-2 control-label required-control"><spring:message code="unitdetails.RoadType"/> </label>
							          <div>
							          <c:set var="baseLookupCode" value="RFT" />
									  <apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
							 							path="provisionalAssesmentMstDto.propLvlRoadType" cssClass="form-control" 
							 							hasChildLookup="false" hasId="true" showAll="false" 
														selectOptionLabelCode="Select Road Factor" isMandatory="true" disabled="${command.noOfDaysEditableFlag eq 'N' ? true : false }"/> 
									 </div>
									 
									
					</div>
			
				</div> --%>

        	
<!-------------------------------------------------Land / Building Details ------------------------------------------------------------>
			
<%-- 				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#BuildingDetails"><spring:message
					code="property.buildingdetails" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="BuildingDetails">
				
					<div class="form-group">
							<c:if test="${command.noOfDaysEditableFlag ne 'Y'}">
								<apptags:input labelCode="property.yearofacquisition"
									path="provisionalAssesmentMstDto.assAcqDate"
									isReadonly="${command.noOfDaysEditableFlag eq 'N' ? true : false }"
									cssClass="dateClass"></apptags:input>


							</c:if>
							<c:if test="${command.noOfDaysEditableFlag eq 'Y'}">
								<label class="col-sm-2 control-label required-control"><spring:message
										code="property.yearofacquisition" /></label>
								<div class="col-sm-4">
									<div class="input-group">
										<form:input path="provisionalAssesmentMstDto.assAcqDate"
											readonly="false"
											class="lessthancurrdate form-control mandColorClass dateClass addColor"
											id="proAssAcqDate" onChange="getFinancialYear();"
											data-rule-required="true" placeholder="DD/MM/YYYY"
											autocomplete="off" />
										<span class="input-group-addon"><i
											class="fa fa-calendar"></i></span>
										<form:hidden path="leastFinYear" id="minFinancialYear" />
									</div>
								</div>
							</c:if>

							<label class="col-sm-2 control-label required-control"><spring:message
										code="property.totalplot" text="" /></label>
							<div class="col-sm-4">						
							<form:input cssClass="form-control mandColorClass text-right"
												onkeypress="return hasAmount(event, this, 15, 2)"
												id="totalplot" path="provisionalAssesmentMstDto.assPlotArea"
												placeholder="999999.99"
												onchange="getAmountFormatInDynamic((this),'totalplot')"
												data-rule-required="true" ></form:input>
												disabled="${command.noOfDaysEditableFlag eq 'N' ? true : false }"
							</div>													
					</div>							
				</div> --%>

<jsp:include page="/jsp/property/ChangeInAssessmentUnitDetailsForm.jsp"></jsp:include>


		<div class="text-center padding-10">
			<c:choose>
				<c:when test="${command.getAssType() eq 'A' && command.authLevel eq true}">
					<button type="button" class="btn btn-success"
						onclick="proceed(this)" id="changeProceed"><spring:message code="unit.proceed"/></button>
					<apptags:backButton url="AdminHome.html"></apptags:backButton>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-success"
						onclick="fetchCheckList(this)" id="checkList"><spring:message code="unit.proceed"/></button>
					<button type="button" class="btn btn-danger"
						onclick="backToSearchPageAssess(this)" id="backToSearchPage"><spring:message code="property.Back"/></button>
				</c:otherwise>
			</c:choose>
				
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
			</div>	
			<!-- Start button -->
			<c:if test="${command.getAssType() ne 'A' && command.authLevel eq false}">
		 	<div class="text-center padding-10">
				<button type="button" class="btn btn-success"
				onclick="proceed(this)" id="changeProceed"><spring:message code="unit.proceed"/></button>
				
				<c:if test="${command.getSelfAss() eq 'Y'}">
				<input 	type="button" class="btn btn-warning" value="Reset" onclick="resetFormData(this)" />
				</c:if>
				
				<button type="button" class="btn btn-danger"
				onclick="backToSearch()" id="backToSearch"><spring:message code="property.Back" text="Back"/></button>
				
					
			</div> 		
			</c:if>
			<!--  End button -->
</form:form>

<!--  End form -->
</div>
</div>
<c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
 </div> 
</c:if>
