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
<script type="text/javascript" src="js/property/Bifurcation.js" ></script>   
<script type="text/javascript" src="js/property/ownershipDetailsForm.js"></script>
<!-- <script src="js/property/selfAssessment.js" type="text/javascript"></script> -->


<div id="validationDiv">

<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
	<div class="widget-header">
				<h2><strong><spring:message code="property.Bifurcation" text="Property Bifurcation"/></strong></h2>				
				<%-- <div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message code="property.Help"/></span></i></a>
				</div> --%>
				
	<apptags:helpDoc url="BifurcationForm.html"></apptags:helpDoc>	
				
	</div>
	
	<!-- End Main Page Heading -->
		
<!-- Start Widget Content -->
	<div class="widget-content padding">
	
		
		<!-- Start mand-label -->
		<div class="mand-label clearfix">
			<span><spring:message code="property.changeInAss"/>
			</span>
		</div>
		<!-- End mand-label -->
		
		<!-- Start Form -->
		<form:form action="BifurcationForm.html"
					class="form-horizontal form" name="BifurcationFormSearch"
					id="BifurcationFormSearch">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			<input type="hidden" id="ownershipCode" />
            <form:hidden path="billingMethod" id="billingMethod" />
            <form:hidden path="checklistApplicable" id="checklistApplicable" />
			
			<c:if test="${command.provisionalAssesmentMstDto.assActive ne 'A' && command.getAssType() ne 'A'}">
<%-- 			<div class="padding-10"><strong><i><spring:message code="property.EnterDetailstosearchproperty" /></i></strong></div> --%>
			<!-- Start Each Section -->
			<div class="form-group">
					<apptags:input labelCode="property.NewPropertyNumber"  path="provisionalAssesmentMstDto.assNo"></apptags:input>
                   <%-- <apptags:input labelCode="property.OldPropertyNumber"   path="provisionalAssesmentMstDto.assOldpropno"></apptags:input> --%>
            </div>
			<div class="form-group" id="showFlatNo">
						<label class="col-sm-2 control-label"><spring:message
								code="" text="Flat No" /></label>
						<div class="col-sm-4">
							<form:select path="provisionalAssesmentMstDto.flatNo" id="flatNo"
								class="form-control mandColorClass chosen-select-no-results">
								<form:option value="">
									<spring:message code='adh.select' />
								</form:option>
								<c:forEach items="${command.flatNoList}" var="flatNoList">
									<form:option value="${flatNoList}">${flatNoList}</form:option>
								</c:forEach>
							</form:select>
						</div>
			</div>
                   	<!-- Start button -->
                   	<div class="text-center padding-15 clear">                   	
                   	<button type="button" class="btn btn-warning" id="serchBtnDirect" onclick="SearchButton(this)">
                   	<spring:message code="property.search" text="Search" /></button>
                   	
                   	<button type="button" class="btn btn-success" id="serchBtn" onclick="redirectToSearchPropNo(this)">
								<i class="fa fa-search"></i>
								<spring:message code="property.search" text="Search"/>
							</button>
                   	</div>
                   	<!-- End button -->  
			</c:if>
			<!-- End Each Section -->
			<c:if test="${command.provisionalAssesmentMstDto.assActive eq 'A' }">
			
			<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="property.ParentPropertyNumber" text="Parent Property Number"/></label>
		<div class="col-sm-4">
			<form:input path="" class="form-control"  value="${command.provisionalAssesmentMstDto.parentProp}" data-rule-required="true" readonly="true" />
			</div>					
			</div>
						<div class="form-group">
							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.refPropNo"
								path="provisionalAssesmentMstDto.refPropNo" maxlegnth="40"
								isDisabled="true"></apptags:input>

							<apptags:input cssClass="alphaNumeric preventSpace"
								labelCode="propertydetails.gisRefNo"
								path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"
								isDisabled="true"></apptags:input>

						</div>
						<div class="form-group">

							<apptags:input labelCode="property.propertyZone"
								path="provisionalAssesmentMstDto.assWardDesc1" isDisabled="true"></apptags:input>

							<c:if
								test="${command.provisionalAssesmentMstDto.assWard2 ne null}">
								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc2"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard3 ne null}">

								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc3"
									isDisabled="true"></apptags:input>
							</c:if>
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc4"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard5 ne null}">

								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc5"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<apptags:input labelCode="unitdetails.RoadType"
								path="provisionalAssesmentMstDto.proAssdRoadfactorDesc"
								isDisabled="true"></apptags:input>

							<label for="surveyType"
								class="col-sm-2 control-label required-control"><spring:message
									code="property.billingMethod" text="Billing Method" /> </label>
							<c:set var="baseLookupCodess" value="BLM" />
							<apptags:lookupField
								items="${command.getLevelData(baseLookupCodess)}"
								path="provisionalAssesmentMstDto.billMethod"
								cssClass="form-control" hasChildLookup="false" hasId="true"
								showAll="false" selectOptionLabelCode="Select " disabled="true"/>
							
						</div>

						<div class="form-group">

							<label for="proAssAcqDate" class="col-sm-2 control-label"><spring:message
									code="propertydetails.firstAssessmentDate" /></label>
							<div class="col-sm-4">
								<form:input
									path="provisionalAssesmentMstDto.proAssAcqDateFormat"
									type="text" id="proAssAcqDate" class="form-control"
									disabled="true" />
							</div>
							
							<label for="lastAssessmentDate" class="col-sm-2 control-label"><spring:message
									code="propertydetails.lastAssessmentDate"
									text="Last Assessment Date" /></label>
							<div class="col-sm-4">
								<form:input path="provisionalAssesmentMstDto.lastAssessmentDate"
									type="text" id="lastAssessmentDate" class="form-control"
									disabled="true" />
							</div>
							


						</div>
						<!-- new fields  end-->
						<div class="form-group">
							

							
							
							<label for="reviseAssessmentDate" class="col-sm-2 control-label"><spring:message
									code="propertydetails"
									text="Construction Start Date" /></label>
							<div class="col-sm-4">
								<form:input
									path="provisionalAssesmentMstDto.reviseAssessmentDate"
									type="text" id="reviseAssessmentDate" class="form-control"
									disabled="true" />
							</div>
							
							<label class="col-sm-2 control-label "><spring:message
											code="unitdetails.constructPermissionDate" /></label>
									<div class="col-sm-4">
											<form:input path="provisionalAssesmentMstDto.constructPermissionDate"
												class="datepicker-cpdate  form-control  dateClass addColor dateFrmt"												
												id="constructPermissionDate" 
												autocomplete="off" disabled="true"/>
									</div>
						</div>

						
						<div class="form-group">
						
							<label class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
									<div class="col-sm-4">
											
										<form:input type="text" path="provisionalAssesmentMstDto.assdYearConstruction" 
										class="form-control datepicker2 mandColorClass dateClass addColor dateFrmt" 
										id="yearOfConstruc" data-rule-required="true"  autocomplete="off" disabled="true"/>						
																				
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
									<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true"/>
											</div>
									<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
									isMandatory="false" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true" disabled="true"
									cssClass="form-control required-control" showAll="false"  columnWidth="10%"/>
								</div>


						<!-------------------------------------Owner Details (First owner will be the primary owner)----------------------------------------------->
           
    <div class="accordion-toggle ">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Ownershipdetail" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="OwnshipDetail">
			
			<div class="form-group">
				<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
				<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
				
				</div>
		    </div>
		   
		    
		<c:choose>
		<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
	 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
        <tbody>
        <tr>
                   
        <th width="25%"><spring:message code="ownersdetail.ownersname" /></th>
        <th width="25%" class=""><spring:message code="ownersdetail.ownersnameReg" /></th>
		<%-- <th width="9%"><spring:message code="ownersdetail.gender" /></th>
		<th width="9%"><spring:message code="ownerdetails.relation" /></th>
		<th width="20%"><spring:message code="ownersdetails.GuardianName" /></th> --%>
		<th width="15%"><spring:message code="ownersdetail.mobileno" /></th>
		<th width="15%"><spring:message code="property.email" /></th>
		<th width="10%"><spring:message code="ownersdetail.adharno" /></th>
		<th width="10%"><spring:message code="ownersdetail.pancard" />                    	 
        </tr>
                		
      <tr>
		<td>
			<form:input id="assoOwnerName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
		</td>
			
		<td>
			<form:input id="assoOwnerNameReg" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/>
		</td>						
		<%-- <td class="ownerDetails">
				<form:input id="ownerGender" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].proAssGenderId"   class="form-control" disabled="true"/>				
		</td>
									
		<td class="ownerDetails">		
			<form:input id="ownerRelation" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].proAssRelationId"  class="form-control" disabled="true"/>
		</td>
							
		<td class="mand">
			<form:input id="assoGuardianName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoGuardianName" class="form-control" disabled="true" />   
		</td> --%>
	
		<td>
			<form:input id="assoMobileno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
		</td>
		
		<td><form:input id="emailId" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control preventSpace" disabled="true" />   
		</td>
									
		<td class="ownerDetails">
			<form:input id="assoAddharno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno" class="form-control "  disabled="true"/>
		</td>
		<td class="companyDetails">
			<form:input id="assoPanno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control"  disabled="true"/>
		</td>
	</tr>
	</tbody>
    </table>
	</c:when>
		 	 	
		 <c:when test="${command.getOwnershipPrefix() eq 'JO'}">	
		 <c:set var="a" value='0'/>
		 
					<table id="jointOwnerTable" class="table text-left table-striped table-bordered">
						
								<tr>
									<th width="17%"><spring:message code="ownersdetail.ownersname" /></th>
									<th width="20%" ><spring:message code="ownersdetail.ownersnameReg" /></th>
									<%-- <th width="10%"><spring:message code="ownersdetail.gender" /></th>
									<th width="8%" ><spring:message code="ownerdetails.relation" /></th>
									<th width="17%"><spring:message code="ownersdetails.GuardianName" /></th> --%>
									<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
									<th width="11%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="10%"><spring:message code="property.email" /></th>
									<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
									<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
								</tr>
						<tbody>
						<c:forEach var="ownershipTypeList" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
            			<tr>
                		 <td>${ownershipTypeList.assoOwnerName}</td>
                		 <td>${ownershipTypeList.assoOwnerNameReg}</td>
                		 <%-- <td>${ownershipTypeList.proAssGenderId}</td>
                		 <td>${ownershipTypeList.proAssRelationId}</td>
                		 <td>${ownershipTypeList.assoGuardianName}</td> --%>
                		 <td>${ownershipTypeList.propertyShare}</td>
                		 <td>${ownershipTypeList.assoMobileno}</td>
                		 <td>${ownershipTypeList.eMail}</td>                		 
                		 <td>${ownershipTypeList.assoAddharno}</td>
                		 <td>${ownershipTypeList.assoPanno}</td>
            			</tr>
                		 </c:forEach>
						</tbody>
					</table>
					</c:when>
				<c:otherwise>
				<table id="companyDetailTable" class="table text-left table-striped table-bordered">
					<tbody>					
						<tr>
									<th width="25%"><spring:message code="property.NameOf" /> ${command.getOwnershipTypeValue()}</th>
									<th width="25%" class=""><spring:message code="ownersdetail.ownersnameReg" /></th>
									<%-- <th width="25%"><spring:message code="ownersdetail.contactpersonName" /></th> --%>
									<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="10%"><spring:message code="property.email" /></th>
									<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						
 						<tr>
 						<td> 
 						<form:input id="assoOwnerName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
 						</td> 
						<td> 
 						<form:input id="assoOwnerNameReg_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/> 
 						</td>
 						<%-- <td> 
 						<form:input id="assoGuardianName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoGuardianName" class="form-control " disabled="true" />  
						</td> --%> 
								
 						<td><form:input id="assoMobileno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
 						</td> 
 						
 						<td><form:input id="emailId_${d}"
										path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
						</td>
								
						<td class="companyDetails"> 
						<form:input id="assoPanno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control" disabled="true" /> 
						</td> 
 						</tr> 								
					</tbody>
					</table>				
					</c:otherwise>	
					</c:choose>
				 </div>
						

 
  <!---------------------------------------------------------------Land Details----------------------------------------------------------------->
            
				<form:hidden path="assType" id="assType"/>
				 
				<%-- <h4 class="margin-top-10 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#propertyDetail"><spring:message
				code="property.LandDetail" /></a>
				</h4> --%>
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
		<c:if test="${command.getAssType() eq 'BIF' || command.getAssType() eq 'A' }">  
		<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch Land Details"/></button> 
		</c:if>
		
		</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
		
	
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
		 	<c:if test="${command.getAssType() eq 'BIF' || command.getAssType() eq 'A'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch Land Details"/></button> 
			</c:if>	 
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>

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
		 	<c:if test="${command.getAssType() eq 'BIF' || command.getAssType() eq 'A'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
										<spring:message code="property.fetchLandTypeDetails.button" text="Fetch Land Details"/></button> 
			</c:if>	 
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
 
</c:when>
</c:choose>
</div> 
						<jsp:include page="/jsp/property/BifurcationAddressForm.jsp"></jsp:include>
						
						<%-- <jsp:include page="/jsp/property/BifurcationTaxZoneDetails.jsp"></jsp:include> --%>
						
			
<%-- <div class="accordion-toggle">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#BuildingDetails"><spring:message
				code="property.buildingdetails" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="BuildingDetails">
		<div class="form-group">
		<label class="col-sm-2 control-label required-control"><spring:message code="property.DateOfEffect"/></label>
		<div class="col-sm-4">
		<div class="input-group"> 
		<form:input path="provisionalAssesmentMstDto.assAcqDate" class="lessthancurrdate form-control mandColorClass dateClass" id="proAssAcqDate" placeholder="DD/MM/YYYY" autocomplete="off" />
		<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
		<form:hidden path="leastFinYear" id="minFinancialYear"/>	
		</div>
		</div>
		
			<label class="col-sm-2 control-label required-control"><spring:message
									code="property.totalplot" text="" /></label>
			<div class="col-sm-4">						
			<form:input cssClass="form-control mandColorClass text-right"
									onkeypress="return hasAmount(event, this, 15, 2)"
									id="totalplot" path="provisionalAssesmentMstDto.assPlotArea"
									placeholder="999999.99"
									onchange="getAmountFormatInDynamic((this),'totalplot')"
									data-rule-required="true"></form:input>
			</div>		
		</div>
	</div>
</div> --%>

						
			<jsp:include page="/jsp/property/BifurcationUnitDetailsForm.jsp"></jsp:include>
			
			
			<c:if test="${empty command.checkList}">
				<div class="text-center padding-10">
				<button type="button" class="btn btn-success btn-submit"
				onclick="getBuferCheckList(this)" id="checkListBifu"><spring:message code="unit.proceed"/></button>
				
				<button type="button" class="btn btn-danger"
				onclick="backButtons(this)" id="backButton"><spring:message code="bt.backBtn"/></button>
				</div>
			</c:if>
			
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
			<div class="text-center padding-10">
			<c:if test="${command.getAssType() eq 'BIF'}">  
				<button type="button" class="btn btn-success btn-submit"
				onclick="proceed(this)" id="proceedBifu"><spring:message code="unit.proceed"/></button>
				<button type="button" class="btn btn-danger"
				onclick="backButtons(this)" id="backButton2"><spring:message code="bt.backBtn"/></button>
			</c:if>
			<c:if test="${command.getAssType() eq 'A'}">  
			<button type="button" class="btn btn-success btn-submit"
				onclick="compareBeforeAfterAuthBifurcation(this)" id="btnCompare"><spring:message code="property.propProceed" text="Proceed to Compare"/></button>
			</c:if>
			</div>
			</c:if>
			<!--  End button -->
			
			
		</form:form>
		<!-- End Form -->
		</div>
		<!-- End Widget Content here -->
		</div>
		<!-- End Widget  here -->							
</div>
<!-- End of Content -->
</div>

