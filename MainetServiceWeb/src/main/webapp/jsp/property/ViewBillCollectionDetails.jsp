<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/property/viewPropertyDetails.js"></script>
<script type="text/javascript" src="js/mainet/login.js"></script>
<script type="text/javascript" src="js/mainet/script-library.js"></script> 
<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script> 
<!-- <script type="text/javascript" src="js/mainet/jquery.js"></script> -->

<style>
@media print {
	@page {
		size: A4 landscape;
		margin: 0.5cm;
	}
	#btnPrint {
		display: none;
	}
	#back {
		display: none;
	}
	
}
th {
  position: sticky;
  top: 0px;
}
</style>
<!-- End JSP Necessary Tags -->

<div class="widget">
     <%--   <div class="widget-header">
			<h2><strong><spring:message code="propertyView.SelfAssesmentView" /></strong></h2>	
		</div> --%>	
		
        <div class="widget-content padding">	
		<form:form action="ViewPropertyDetail.html"
					class="form-horizontal form" name="ViewPropertyDetail"
					id="ViewPropertyDetail">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<form:hidden path="flatNo" id="flatNo"/>
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>

			<!-- Start Each Section -->
			<c:if test="${command.getIntgrtionWithBP() eq 'Y'}">
			<div class="form-group">
				<apptags:input labelCode="property.buildingpermission" path="" isDisabled="true"></apptags:input> 
					<apptags:input labelCode="property.LandPermissionNo." path="" isDisabled="true"></apptags:input>
			</div>
			</c:if>
			<div class="form-group">
				<apptags:input labelCode="propertydetails.PropertyNo."
					path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
					
					<label for="surveyType"
					class="col-sm-2 control-label "><spring:message
						code="property.billingMethod" text="Billing Method" /> </label>
				<c:set var="baseLookupCodess" value="BLM" />
				<apptags:lookupField
					items="${command.getLevelData(baseLookupCodess)}"
					path="provisionalAssesmentMstDto.billMethod"
					cssClass="form-control" hasChildLookup="false" hasId="true"
					showAll="false" selectOptionLabelCode="Select " disabled="true"/>				
			</div>
			<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.assAutDate}"
					var="assAutDate" />
			<div class="form-group">
			<label for="assAutStatus" class="col-sm-2 control-label"><spring:message
							code="property.authorization.status"  /></label>
			<c:choose>
			<c:when test="${command.provisionalAssesmentMstDto.assAutStatus eq 'N'}">
				<div class="col-sm-4">				
					<form:input path="" value="Unauthorized" type="text" id="assAutStatus" class="form-control" disabled="true" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="col-sm-4">				
					<form:input path="" value="Authorized" type="text" id="assAutStatus" class="form-control" disabled="true" />
				</div>
			</c:otherwise>
			</c:choose>
			<label for="assAutDate" class="col-sm-2 control-label"><spring:message
							code="property.approvalDate" text="Approval date" /></label>
			<div class="col-sm-4">
				<form:input path="" value="${assAutDate}" type="text"
						id="assAutDate" class="form-control" disabled="true" />
			</div>		
			</div>
			
			<div class="form-group">
			<apptags:radio radioLabel="Active,Inactive,Cancelled" radioValue="A,I,C"
								labelCode="property.propertyStatus" disabled="true"
								path="provisionalAssesmentMstDto.assActive"></apptags:radio>
			<c:if test="${not empty command.provisionalAssesmentMstDto.remarks}">
			<%-- <c:if test="${command.getServiceShortCode() eq 'CIA'}"> --%>
				<apptags:input labelCode="property.change.remark"
						path="provisionalAssesmentMstDto.remarks" isDisabled="true"></apptags:input>
			</c:if>
			</div>
			
			<div class="form-group">
			<c:if test="${not empty command.oldRemarks}">
				<apptags:textArea labelCode="property.old.remarks" path="oldRemarks" isDisabled="true"></apptags:textArea>
			</c:if>
			</div>				
			<%-- <c:if
				test="${command.getProvisionalAssesmentMstDto().getFlatNo() ne null }">
				<div class="form-group">
					<apptags:input labelCode="unitdetails.flatNo"
						path="provisionalAssesmentMstDto.flatNo" isDisabled="true"></apptags:input>
				</div>
			</c:if> --%>
										<!-------------------------------------Property Info Details----------------------------------------------->
		<div class="accordion-toggle">    
		<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#PropertyDetails"><spring:message
				code="property.propertyMaster" text="Property Information"/></a>
			</h4>
			<div class="panel-collapse collapse in" id="PropertyDetails">
									<div class="form-group margin-top-15">	
											<apptags:input cssClass="alphaNumeric preventSpace"
									labelCode="propertydetails.gisRefNo"
									path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"
									isDisabled="true"></apptags:input>
									
									<apptags:input labelCode="property.propertytype"
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc1"
								isDisabled="true"></apptags:input>
			
				</div>
			<!----------------------------------------------------------Tax-Zone details------------------------------------------------------ --> 						
				<div class="form-group">
			
					<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc1" isDisabled="true"></apptags:input>
				
					<c:if test="${command.provisionalAssesmentMstDto.assWard2 ne null}">			
						<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc2" isDisabled="true"></apptags:input>
					</c:if>
				</div>
				<div class="form-group">
					<c:if test="${command.provisionalAssesmentMstDto.assWard3 ne null}">
						<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc3" isDisabled="true"></apptags:input>
					</c:if>
					<c:if test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
						<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc4" isDisabled="true"></apptags:input>
					</c:if>
				</div>
				
					<c:if test="${command.provisionalAssesmentMstDto.assWard5 ne null}">
					<div class="form-group">
						<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc5" isDisabled="true"></apptags:input>
					</div>
					</c:if>
				
				<div class="form-group">
					<apptags:input labelCode="unitdetails.RoadType" path="provisionalAssesmentMstDto.proAssdRoadfactorDesc" isDisabled="true"></apptags:input>
				</div>
				
				<div class="form-group">
					<c:if test="${command.provisionalAssesmentMstDto.disputedType1 ne null}">
						<apptags:input labelCode="property.disputedType" path="provisionalAssesmentMstDto.disputedTypeDesc1" isDisabled="true"></apptags:input>			
					</c:if>
					<c:if test="${command.provisionalAssesmentMstDto.disputedType2 ne null}">
						<apptags:input labelCode="property.disputed.subType" path="provisionalAssesmentMstDto.disputedTypeDesc2" isDisabled="true"></apptags:input>			
					</c:if>
				</div>
				<div class="form-group">
					<c:if test="${command.provisionalAssesmentMstDto.disputedType3 ne null}">
						<apptags:input labelCode="property.disputedType" path="provisionalAssesmentMstDto.disputedTypeDesc3" isDisabled="true"></apptags:input>			
					</c:if>
					<c:if test="${command.provisionalAssesmentMstDto.disputedType4 ne null}">
						<apptags:input labelCode="property.disputed.subType" path="provisionalAssesmentMstDto.disputedTypeDesc4" isDisabled="true"></apptags:input>			
					</c:if>
				</div>
				<div class="form-group">
					<c:if test="${command.provisionalAssesmentMstDto.disputedType5 ne null}">
						<apptags:input labelCode="property.disputedType" path="provisionalAssesmentMstDto.disputedTypeDesc5" isDisabled="true"></apptags:input>			
					</c:if>
				</div>
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.firstAssessmentDate}"
					var="firstAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.lastAssessmentDate}"
					var="lastAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.reviseAssessmentDate}"
					var="reviseAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.assAutDate}"
					var="assAutDate" />

				<!-- new fields  end-->
				<div class="form-group">
						
					<apptags:input labelCode="propertydetails.firstAssessmentDate" path="provisionalAssesmentMstDto.proAssAcqDateFormat" isDisabled="true"></apptags:input>

					<label for="lastAssessmentDate" class="col-sm-2 control-label"><spring:message
							code="propertydetails.lastAssessmentDate"
							text="Last Assessment Date" /></label>
					<div class="col-sm-4">
						<form:input path="" value="${lastAssessDate}" type="text"
							id="lastAssessmentDate" class="form-control" disabled="true" />
					</div>
				</div>

				<div class="form-group">
					<%-- 	<label for="reviseAssessmentDate" class="col-sm-2 control-label"><spring:message
									code="propertydetails"
									text="Construction Start Date" /></label>
							<div class="col-sm-4">
								<form:input path="" value="${reviseAssessDate}" type="text"
									id="reviseAssessmentDate" class="form-control" disabled="true" />
							</div>
				 --%>
				 						<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true" />
											</div>	
				 				
								<label for="constructPermissionDate" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.constructPermissionDate" /></label>
										<div class="col-sm-4">
											<form:input type="text" class="form-control" 
											 path="provisionalAssesmentMstDto.constructPermissionDate"
											 id="constructPermissionDate"  disabled="true" />
										</div>
										
					
					</div>				
					<div class="form-group">
						
								<label for="permissionUseNo" class="col-sm-2 control-label">
											<spring:message	code="unitdetails.permissionUseNo" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
												path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
												id="permissionUseNo" disabled="true" />
											</div>	
										
								
								<label for="yearOfConstruc0" class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
									<div class="col-xs-4 col-sm-4">
										<form:input
											path="provisionalAssesmentMstDto.AssdYearConstructionDateFormat"  value=""
											type="text" id="yearOfConstruc0" class="form-control"
											disabled="true" />
									</div>
										
									
							 												
									
						
					</div>
					<div class="form-group">
						<label for="goGreenReq" class="col-sm-2 control-label">
    						<spring:message code="go.green" />
						</label>
						<div class="col-sm-4">
						    <form:input type="text" class="form-control" id="goGreenReq"
						           path="provisionalAssesmentMstDto.goGreenReq"
						           disabled="true" />
						</div>
								
						<label for="goGreenReqDate" class="col-sm-2 control-label">
							<spring:message code="go.green.date" />
						</label>
						<div class="col-xs-4 col-sm-4">
							<form:input
								path="provisionalAssesmentMstDto.goGreenReqDateFormat"
								type="text" id="goGreenReqDate" class="form-control"
								disabled="true" />
						</div>
					</div>
				
								
			</div>

</div>


			<!-------------------------------------Owner Details (First owner will be the primary owner)----------------------------------------------->
           
    
    <div class="accordion-toggle">    
    <c:choose>
    
    <c:when test="${command.getOwnershipPrefix() eq 'JO'}">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Owner(s)detail" /></a>
	</h4>
	
	</c:when>
	<c:otherwise>
	
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Ownershipdetail" /></a>
	</h4>
	</c:otherwise>
	</c:choose>
	<div class="panel-collapse collapse in" id="OwnshipDetail">
			
			<div class="form-group">
				<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
				<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
				<form:hidden  path="filePath"  id="billFilePath" class="form-control"/>
				
				
				</div>
		    </div>
		   
		    
		<c:choose>
		<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
	 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
        <tbody>
        <tr>
                   
        <th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
		<%-- <th width="9%"><spring:message code="ownersdetail.gender" /></th>
		<th width="9%"><spring:message code="ownerdetails.relation" /></th>
		<th width="20%"><spring:message code="ownersdetails.GuardianName" /></th> --%>
		<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
		<th width="10%"><spring:message code="property.email" /></th>
		<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
		<th width="10%"><spring:message code="ownersdetail.pancard" />                    	 
        </tr>
                		
      <tr>
		<td>
			<form:input id="assoOwnerName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
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
									<%-- <th width="25%"><spring:message code="ownersdetail.contactpersonName" /></th> --%>
									<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="10%"><spring:message code="property.email" /></th>
									<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						
 						<tr>
 						<td> 
 						<form:input id="assoOwnerName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
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
        
			</div>	
 <!---------------------------------------------------------------Land Details----------------------------------------------------------------->
            
				<form:hidden path="assType" id="assType"/>
				 
				<%-- <h4 class="margin-top-10 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#propertyDetail"><spring:message
				code="property.LandDetail" /></a>
				</h4> --%>
				<div class="panel-collapse collapse in margin-top-15" id="propertyDetail">				
				
						<div class="form-group">
								<%-- <apptags:input cssClass="alphaNumeric preventSpace" labelCode="propertydetails.oldpropertyno" path="provisionalAssesmentMstDto.assOldpropno" maxlegnth="20" isReadonly="true"></apptags:input> --%>				
								<%-- <apptags:input cssClass="alphaNumeric preventSpace" labelCode="property.landType" path="provisionalAssesmentMstDto.assLandTypeDesc"  isReadonly="true"></apptags:input> --%>
			           			<form:hidden  path="landTypePrefix" class="form-control landValue"/> 		
						</div>
						
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
		<c:if test="${command.getAssType() eq 'VIW'}">  
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
		 	<c:if test="${command.getAssType() eq 'VIW'}">  
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
		 	<c:if test="${command.getAssType() eq 'VIW'}">  
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
           
            	
<!-------------------------------------------------------Property Address Details------------------------------------------->
			<div class="accordion-toggle">    
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="propertyAddress">
			<div class="form-group">
					<apptags:textArea labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress" isDisabled="true"></apptags:textArea>
					<%-- <apptags:input labelCode="property.location" path="provisionalAssesmentMstDto.locationName" isDisabled="true"></apptags:input>	 --%>
						<apptags:input labelCode="property.khata.no" path="provisionalAssesmentMstDto.tppKhataNo" isDisabled="true"></apptags:input>	
            </div> 
           
            <%-- <div class="form-group">
            		<apptags:input labelCode="property.pincode" path="provisionalAssesmentMstDto.assPincode" isDisabled="true"></apptags:input>
            		<apptags:input labelCode="property.email" path="provisionalAssesmentMstDto.assEmail" isDisabled="true"></apptags:input>
            		<apptags:input labelCode="unitdetails.RoadType" path="provisionalAssesmentMstDto.proAssdRoadfactorDesc" isDisabled="true"></apptags:input>  	
      
            </div> --%>
            <div class="form-group">
            	<apptags:input labelCode="propertydetails.village" path="provisionalAssesmentMstDto.areaName" isDisabled="true"></apptags:input>
            	
            	<label class="col-xs-2 col-sm-2 control-label " for="chaaltaNo"><spring:message
									code="property.chaaltaNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo" disabled="true"></form:input>
							</div>
						
            
            
          
            </div>
            
            <div class="form-group">
            
           <label class="col-xs-2 col-sm-2 control-label " for="clusterNo"><spring:message
									code="property.clusterNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.clusterNo" id="clusterNo" disabled="true"></form:input>
							</div>
            
            <label class="col-xs-2 col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" disabled="true"></form:input>
							</div>
				 </div>			
				  <div class="form-group">	
							
						<apptags:input labelCode="property.plotNo" path="provisionalAssesmentMstDto.tppPlotNo" isDisabled="true"></apptags:input>
						 
						 	<label class="col-xs-2 col-sm-2 control-label " for="buildingNo"><spring:message
									code="property.buildingNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.buildingNo" id="buildingNo" disabled="true"></form:input>
							</div>
					</div>
					<div class="form-group">
							<label class="col-xs-2 col-sm-2 control-label " for="addiSurveyNo"><spring:message
									code="property.addiSurveyNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiSurveyNo"
									id="addiSurveyNo" disabled="true"></form:input>
							</div>
							<label class="col-xs-2 col-sm-2 control-label " for="addiCityServeyNo"><spring:message
									code="property.addiCityServeyNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiCityServeyNo"
									id="addiCityServeyNo" disabled="true"></form:input>
							</div>
						</div>
						<div class="form-group">
						
						 <apptags:input labelCode="property.khasra.no" path="provisionalAssesmentMstDto.tppSurveyNumber" isDisabled="true"></apptags:input>
							
							<%-- <apptags:input labelCode="Balance amount to adjust" path="totalExcessAmntToAdjust" isDisabled="true"></apptags:input> --%>
							
            </div>
                </div>
<!-----------------------------------------------------Correspondence Address Details------------------------------------------------->
			
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#correspondenceAddress"><spring:message
				code="property.correspondenceaddress" /></a>
			</h4>
            
			<div class="panel-collapse collapse in" id="correspondenceAddress">	
			<div class="form-group">    
					<apptags:textArea labelCode="property.correspondenceaddress" path="provisionalAssesmentMstDto.assCorrAddress" isDisabled="true"></apptags:textArea>
					<apptags:input labelCode="property.pincode" path="provisionalAssesmentMstDto.assCorrPincode" isDisabled="true"></apptags:input>
	
            </div>            
            <%-- <div class="form-group"> 
            		<apptags:input labelCode="property.email" path="provisionalAssesmentMstDto.assCorrEmail" isDisabled="true"></apptags:input>
            </div> --%>
          </div>
         
<!----------------------------------------------------Last Payment Details----------------------------------------------------------- -->
			
		<%-- 	<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#LastBillPayment"><spring:message
				code="property.lastpayment" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="LastBillPayment">			
			<div class="form-group">                 
	              	<label for="LastPayment" class="col-sm-2 control-label "><spring:message code="propertyView.LastPayment"/></label>
	              	<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssBillPaymentDesc" type="text" id="LastPayment" class="form-control" disabled="true"/></div>
				
            </div>
            
            <div class="form-group">        
            
            		<apptags:input labelCode="property.receiptno" path="provisionalAssesmentMstDto.assLpReceiptNo" isDisabled="true"></apptags:input>  
            		<apptags:input labelCode="property.receiptamount" path="provisionalAssesmentMstDto.assLpReceiptAmt" isDisabled="true"></apptags:input>       
	                  		
            </div>
			
			 <div class="form-group">
			 		<apptags:input labelCode="property.receiptdate" path="provisionalAssesmentMstDto.proAssLpReceiptDateFormat" isDisabled="true"></apptags:input>
			 		<apptags:input labelCode="property.lastpaymentupto" path="provisionalAssesmentMstDto.proAssLpYearDesc" isDisabled="true"></apptags:input>
		 	
			</div>
		
			<div class="form-group">      
			
					<apptags:input labelCode="property.billamount" path="provisionalAssesmentMstDto.billAmount" isDisabled="true"></apptags:input> 
					<apptags:input labelCode="property.OutStandingAmount" path="provisionalAssesmentMstDto.outstandingAmount" isDisabled="true"></apptags:input>           
	              	
            </div>
        	</div> --%>
        	
<!----------------------------------------------------------Tax-Zone details------------------------------------------------------ --> 
		<%-- 	<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#taxZone"><spring:message
				code="property.taxZoneDetails" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="taxZone">
			<div class="panel-body">
			
			</div>
			</div>
        	 --%>
<!-------------------------------------------------------Land / Building Details----------------------------------------------------- -->
			
		<%-- 		<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#BuildingDetails"><spring:message
				code="property.buildingdetails" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="BuildingDetails">
		 --%>
		<%-- 	<div class="form-group">
				<apptags:input labelCode="propertydetails.firstAssessmentDate" path="provisionalAssesmentMstDto.proAssAcqDateFormat" isDisabled="true"></apptags:input>
				<apptags:input labelCode="property.totalplot" path="provisionalAssesmentMstDto.assPlotArea" isDisabled="true"></apptags:input>								
			</div>

				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.firstAssessmentDate}"
					var="firstAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.lastAssessmentDate}"
					var="lastAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.reviseAssessmentDate}"
					var="reviseAssessDate" />
				<fmt:formatDate pattern="dd-MMM-yyyy"
					value="${command.provisionalAssesmentMstDto.assAutDate}"
					var="assAutDate" />

				<!-- new fields  end-->
				<div class="form-group">
					<label for="firstAssessmentDate" class="col-sm-2 control-label"><spring:message
							code="propertydetails.firstAssessmentDate"
							text="First Assessment Date" /></label>
					<div class="col-sm-4">
						<form:input path="" value="${firstAssessDate}" type="text"
							id="firstAssessmentDate" class="form-control" disabled="true" />
					</div>

					<label for="lastAssessmentDate" class="col-sm-2 control-label"><spring:message
							code="propertydetails.lastAssessmentDate"
							text="Last Assessment Date" /></label>
					<div class="col-sm-4">
						<form:input path="" value="${lastAssessDate}" type="text"
							id="lastAssessmentDate" class="form-control" disabled="true" />
					</div>
					<label for="reviseAssessmentDate" class="col-sm-2 control-label"><spring:message
							code="propertydetails.reviseAssessmentDate"
							text="Revise Assessment Date" /></label>
					<div class="col-sm-4">
						<form:input path="" value="${reviseAssessDate}" type="text"
							id="reviseAssessmentDate" class="form-control" disabled="true" />
					</div>
				</div>

				<div class="form-group">

					<label for="assAutDate" class="col-sm-2 control-label"><spring:message
							code="property.approvalDate" text="Approval date" /></label>
					<div class="col-sm-4">
						<form:input path="" value="${assAutDate}" type="text"
							id="assAutDate" class="form-control" disabled="true" />
					</div>
				</div>
 --%>
					<%-- <div class="form-group">
			<c:if test="${command.provisionalAssesmentMstDto.assPropType2 ne null}">
				<apptags:input labelCode="property.propertytype" path="provisionalAssesmentMstDto.proAssPropType2" isDisabled="true"></apptags:input>
		
			</c:if>
			
			<c:if test="${command.provisionalAssesmentMstDto.assPropType3 ne null}">
				<apptags:input labelCode="property.propertytype" path="provisionalAssesmentMstDto.proAssPropType3" isDisabled="true"></apptags:input>
			
		
			</c:if>
			</div> --%>
			
			<%-- <div class="form-group">
			<c:if test="${command.provisionalAssesmentMstDto.assPropType4 ne null}">
				<apptags:input labelCode="property.propertytype" path="provisionalAssesmentMstDto.proAssPropType4" isDisabled="true"></apptags:input>
			
			</c:if>
			
			<c:if test="${command.provisionalAssesmentMstDto.assPropType5 ne null}">
				<apptags:input labelCode="property.propertytype" path="provisionalAssesmentMstDto.proAssPropType5" isDisabled="true"></apptags:input>
			
			</c:if>
			</div>
			 --%>
			
			<%-- <div class="form-group">
					<apptags:input labelCode="property.totalplot" path="provisionalAssesmentMstDto.assPlotArea" isDisabled="true"></apptags:input>
					<apptags:input labelCode="property.buildup" path="provisionalAssesmentMstDto.assBuitAreaGr" isDisabled="true"></apptags:input>
					
			</div> --%>
		<!-- </div> -->

<!-- --------------------------------------------------- Property Address Details--------------------------------------------------------------->

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
					                                <form:input path="provisionalAssesmentMstDto.electricBillUnitNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                            <td>
					                                <form:input path="provisionalAssesmentMstDto.electricConsumerNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                            <td>
					                                <form:input path="provisionalAssesmentMstDto.electricMeterNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                        </tr>
					                </tbody>
					            </table>
					        </div>
					    </div>
					</div>
		</c:if>
<!-- ---------------------------------------------Unit Details --------------------------------------------------------------------------->
		
 <c:if test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"> 
		
 			<h4 class="margin-top-10 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
 			</h4>
			<div class="panel-collapse collapse in" id="UnitDetail">
			<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    
                        <tr>
 							<th width="7%"><spring:message code="unitdetails.year"/></th>
                        	<th width="4%"><spring:message code="unitdetails.unitno"/></th> 	
                         	<th width="7%"><spring:message code="unitdetails.flatNo"/></th> 
                        	<th width="10%"><spring:message code="unitdetails.floorno"/></th>
                        	<c:if test="${command.provisionalAssesmentMstDto.billMethod eq '100000332'}">
                        	<th class="electricDetailsColumn"><spring:message text="Electricity Billing Unit Number"/></th>
				            <th class="electricDetailsColumn"><spring:message text="Electricity Consumer Number"/></th>
				            <th class="electricDetailsColumn"><spring:message text="Electricity Meter Number"/></th>
				            </c:if>
                        	<%-- <th width="10%"><spring:message code="unitdetails.dateofConstruction"/></th> --%>
                            <th width="7%"><spring:message code="unitdetails.legal.illegal"/></th>
                            <th width="10%"><spring:message code="unitdetails.user.charges" text="User Charges Category"/></th>
                        	<th width="20%"><spring:message code="unitdetails.constructiontype"/></th>
                        	<th width="12%"><spring:message code="property.Usagetype"/></th>
                        	   	
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype2 ne null }">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype3 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype4 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype5 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<th width="12%"><spring:message code="unitdetails.actualRent"/></th>
        	 				<th width="12%"><spring:message code="unitdetails.taxable"/></th>
        	 				<c:if test="${command.propertyType eq 'LAND'}">
        	 					<th><spring:message code="unitdetails.rate"/></th>
        	 				</c:if> 
             	 			<c:if test="${command.getAssMethod() eq 'ARV'}">
             	 			<th width="11%"><spring:message code="unitdetails.ARV"/><br><spring:message code="unitdetails.ARVFormula"/></th>
             	 			<th width="12%"><spring:message code="unitdetails.maintCharge"/><br><spring:message code="unitdetails.maintChargeFormula"/></th>            	 			
							<th width="12%"><spring:message code="unitdetails.RV"/><br><spring:message code="unitdetails.RVFormula"/></th>
							</c:if>
							<c:if test="${command.getAssMethod() eq 'CV'}">
							<th width="10%"><spring:message code="unitdetails.CV"/></th>
							</c:if>
             	 			<th width="6%" colspan="2"><spring:message code="unit.ViewMoreDetails"/></th>
             	 			
                        </tr>
                        
                  <tbody>       
                  <c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                       	<tr>
	                        <td class="text-center">${unitDetails.proFaYearIdDesc}</td>                                                                                                          
	                        <td class="text-center">${unitDetails.assdUnitNo}</td>
 	                        <td class="text-center">${unitDetails.flatNo}</td> 
	                        <td class="text-center">${unitDetails.proFloorNo} </td>
	                        <c:if test="${command.provisionalAssesmentMstDto.billMethod eq '100000332'}">
	                        <td class="text-center">${unitDetails.electricBillUnitNo}</td>
				            <td class="text-center">${unitDetails.electricConsumerNo}</td>
				            <td class="text-center">${unitDetails.electricMeterNo}</td>
	                        </c:if>
	                        <%-- <td class="text-center">${unitDetails.proAssdConstructionDate}</td> --%>
                            <td class="text-center">${unitDetails.legal}</td>
                            <td class="text-center">${unitDetails.userChargesTypeDesc}</td>
	                        <td class="text-center">${unitDetails.proAssdConstruTypeDesc}</td>
	                        <td class="text-center">${unitDetails.proAssdUsagetypeDesc} </td>
	                        <%-- <c:if test="${unitDetails.proAssdUsagetypeDesc2 ne null}"> --%>
	                        <td class="text-center">${unitDetails.proAssdUsagetypeDesc2} </td>
	                      <%--   </c:if> --%>
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc3 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc3} </td>
	                        </c:if>
	                        
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc4 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc4} </td>
	                        </c:if>
	                        
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc5 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc5} </td>
	                        </c:if>
	                        
	                                              	                   
	                               <td class="text-center">
	                                              <form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
															type="text"
															class="form-control has2Decimal mandColorClass text-center stylenone"
															id="actualRent${status.count-1}"
															data-rule-required="true"
															onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" placeholder="0.0"/> </td>
							<fmt:formatNumber type="number" value="${unitDetails.assdBuildupArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="assdBuildupArea"/>
	                    	<td class="text-center">${assdBuildupArea}</td>
	                    	<c:if test="${command.propertyType eq 'LAND'}">
	                    		<td class="text-center">${unitDetails.assdStdRate} </td>
	                    	</c:if>								   
	                    	<c:if test="${command.getAssMethod() eq 'ARV'}">
	                    	<fmt:formatNumber type="number" value="${unitDetails.assdAlv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="assdAlv"/>
	                    	<td class="text-center">${assdAlv} </td>
	                    	<td class="text-center">${unitDetails.maintainceCharge} </td>	                    	
	                        <fmt:formatNumber type="number" value="${unitDetails.assdRv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="assdRv"/>
	                        <td class="text-center">${assdRv}</td>
	                        </c:if>
	                        <c:if test="${command.getAssMethod() eq 'CV'}">
	                    	<td class="text-center">${unitDetails.assdCv}</td>
	                    	</c:if>
	                <%--     	<td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-${status.count-1}" aria-expanded="false" aria-controls="group-of-rows-${status.count-1}"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td>	         --%>                   
	                    	<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="viewRoomDetails(${status.count-1},this)" value="Room"></td>
    					</tr>

            
				         <tr class="secondUnitRow " id="group-of-rows-${status.count-1}">
				            <td colspan="15">
				
				               <legend class="text-left"><spring:message code="unitdetails.AdditionalUnitDetails" />
								</legend>
				            
				             	<div class="addunitdetails0 col-xs-6 col-xs-offset-0 col-md-12">
					             	<div class="form-group" >   					           			
					           			<%-- <label for="proAssdRoadfactor" class="col-sm-2 control-label"><spring:message code="unitdetails.RoadType"/> </label>
										<div class="col-sm-2"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].proAssdRoadfactorDesc"  id="proAssdRoadfactor" class="form-control" disabled="true"/></div> --%>					           		
					
										<label for="proAssdOccupancyType" class="col-sm-2 control-label"><spring:message code="unitdetails.occupancy"/> </label>
										<div class="col-sm-4""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].proAssdOccupancyTypeDesc"  id="proAssdOccupancyType" class="form-control" disabled="true"/></div>
										
										<label for="occupierName" class="col-sm-2 control-label"><spring:message code="unitdetails.OccupierName"/></label>
			          		    		<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" type="text" class="form-control" id="occupierName" disabled="true"/></div>
			          		    		
			          		    	<%-- 	<c:if test="${unitDetails.getProAssdOccupancyTypeDesc() eq 'Tanent'}"> --%>
										<%-- <label for="actualRent" class="col-sm-2 control-label"><spring:message code="unitdetails.actualRent"/> </label>
										<div class="col-sm-2"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent" class="form-control" disabled="true"/></div> --%>		
										<%-- </c:if> --%>
									</div>
									
									<div class="form-group" >
										<label for="occupierMobNo" class="col-sm-2 control-label"><spring:message code="unitdetails.occupierMobNo"/></label>
			          		    		<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierMobNo" type="text" class="form-control" id="occupierName" disabled="true"/></div>
									</div>
									<%-- <div class="form-group" >
										<label for="assLandTypeDesc" class="col-sm-2 control-label"><spring:message code="property.landType"/> </label>	
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.assLandTypeDesc" class="form-control" disabled="true"/>
										</div>

										<label for="natureOfProperty" class="col-sm-2 control-label"><spring:message code="property.selectNatureOfProperty"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfpropertyDesc1"  id="natureOfProperty" class="form-control" disabled="true"/>
										</div>
										
										<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc2 ne null}">
										<label for="natureOfProperty" class="col-sm-2 control-label"><spring:message code="property.selectNatureOfProperty"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfpropertyDesc2"  id="natureOfProperty" class="form-control" disabled="true"/>
										</div>
										</c:if>
									</div>	
									<div class="form-group" >	
										<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc3 ne null}">
										<label for="natureOfProperty" class="col-sm-2 control-label"><spring:message code="property.selectNatureOfProperty"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfpropertyDesc3"  id="natureOfProperty" class="form-control" disabled="true"/>
										</div>
										</c:if>
										
										<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc4 ne null}">
										<label for="natureOfProperty" class="col-sm-2 control-label"><spring:message code="property.selectNatureOfProperty"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfpropertyDesc4"  id="natureOfProperty" class="form-control" disabled="true"/>
										</div>
										</c:if>

										<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc5 ne null}">
										<label for="natureOfProperty" class="col-sm-2 control-label"><spring:message code="property.selectNatureOfProperty"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfpropertyDesc5"  id="natureOfProperty" class="form-control" disabled="true"/>
										</div>
										</c:if>
									</div>
                                    <!-- New fields added -->
									<div class="form-group" > 	
										<label for="constructPermissionNo" class="col-sm-2 control-label"><spring:message code="unitdetails.constructPermissionNo"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].constructPermissionNo"  id="constructPermissionNo" class="form-control" disabled="true"/>
										</div>
										
										<label for="permissionUseNo" class="col-sm-2 control-label"><spring:message code="unitdetails.permissionUseNo"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].permissionUseNo"  id="permissionUseNo" class="form-control" disabled="true"/>
										</div>
										
										<label for="assessmentRemark" class="col-sm-2 control-label"><spring:message code="unitdetails.assessmentRemark"/> </label>
										<div class="col-sm-2">
										<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assessmentRemark"  id="assessmentRemark" class="form-control" disabled="true"/>
										</div>
									</div>	 --%>
                                    
									<%-- <div class="form-group">	  					           			
					           			<label for="occupierName" class="col-sm-2 control-label"><spring:message code="unitdetails.OccupierName"/></label>
			          		    		<div class="col-sm-2"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" type="text" class="form-control" id="occupierName" disabled="true"/></div>         			
					          		</div> --%>				
								</div>
				         	</td>
				         </tr>
                   </c:forEach>

               </tbody>
			</table>
      </div>
 </c:if>              
               
<!---------------------------------------------------unitSpecific Additional Info------------------------------------------------------- -->
<c:if test="${not empty command.provAsseFactDtlDto}"> 
	     <div class="accordion-toggle">    
		<h4 class="margin-top-10 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#unitSpecificInfo"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 		</h4>
		<div class="panel-collapse collapse" id="unitSpecificInfo">
 			
 			<table id="unitSpecificInfoTable" class="table table-striped table-bordered ">
            
            <tr>
            	 <th width="20%"><spring:message code="unit.Factor"/></th>
                 <th width="10%"><spring:message code="unit.ApplicableUnitNo"/></th>
                 <th width="30%"><spring:message code="unit.FactorValue"/></th>
                 <th width="30%"><spring:message code="property.Amount" text="Amount"/></th>
            </tr>
            <tbody>
            <c:forEach var="factorDetails" items="${command.getProvAsseFactDtlDto()}" >
            <c:if test="${factorDetails.factorValueCode ne 'AMT'  && factorDetails.factorValueCode ne 'VCT'  && factorDetails.factorValueCode ne 'AGE'}">
            <tr>
              <td>${factorDetails.proAssfFactorIdDesc}</td>                                                                                                          
              <td>${factorDetails.unitNoFact}</td>
              <td>${factorDetails.proAssfFactorValueDesc}</td>
              <td>${factorDetails.factorAmount}</td>
             </tr>
             </c:if>				
			</c:forEach>
			 </tbody>
			</table>	
		</div>
 </div>
  </c:if>
<c:if test="${not empty  command.appDocument}">
<h4 class="panel-title table margin-top-10">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a3"><spring:message code="property.ApprovedDocuments" text="Approved Documents"/></a>
					</h4>
					<div id="a3" class="panel-collapse collapse in">
							<table id="docs" class="table table-striped table-bordered margin-top-10">
				                    
				                        <tr>    
				                         
				                        <th width="10%"><spring:message code="propertyTax.SrNo"/></th>                   
				                        	<th width="20%"><spring:message code="property.ApplicationNo" text="Application No"/></th>
											<th width="20%"><spring:message code="property.ServiceName" text="Service Name"/></th>
											<th width="20%"><spring:message code="property.Documents" text="Documents"/></th>
										</tr>	
									
										<tbody>
										<c:forEach var="doc" items="${command.appDocument}" varStatus="status">	
										<c:set var="service" value="${doc.value[0].descriptionType}"></c:set>
										 <tr> 
										 <td>${status.count}</td>
											<td>${doc.key}</td>
											<td>${service}</td>
											<td>
											<table>
											<c:forEach var="entry" items="${doc.value}">
											<tr>
											 <td >
											<apptags:filedownload filename="${entry.documentName}" filePath="${entry.uploadedDocumentPath}" dmsDocId="${entry.documentType}" actionUrl="ViewPropertyDetail.html?Download"></apptags:filedownload>
											 </td>
											</tr>
											</c:forEach>
											</table>
											</td>
											
											
											</tr>
											</c:forEach>
										</tbody>
							</table>
					</div>
					
</c:if>
<!-------------------------------------------- Updated data entry suite documents --------------------------------------------------------->
<c:if test="${not empty  command.updateDataEntryDocs}">
<h4 class="panel-title table margin-top-10">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a3"><spring:message code="propertyTax.desDocument" text="Attachments"/></a>
					</h4>
							<table id="docs" class="table table-striped table-bordered margin-top-10">
				                        <tr>    
				                        <th width="10%"><spring:message code="propertyTax.SrNo"/></th>                   
											<th width="20%"><spring:message code="propertyTax.documentDescrition" text="Document Description"/></th>
											<th width="20%"><spring:message code="property.Documents" text="Documents"/></th>
										</tr>	
									
										<tbody>
										<c:forEach var="doc" items="${command.updateDataEntryDocs}" varStatus="status">	
										<%-- <c:set var="service" value="${doc.value[0].descriptionType}"></c:set> --%>
										 <tr> 
										 <td>${status.count}</td>
											<%-- <td>${doc.key}</td> --%>
											<td>${doc.doc_DESC_ENGL}</td>
											<td>
											<apptags:filedownload filename="${doc.documentName}" filePath="${doc.uploadedDocumentPath}" dmsDocId="${doc.documentType}" actionUrl="ViewPropertyDetail.html?Download"></apptags:filedownload>
											 </td>
											</tr>
											</c:forEach>
										</tbody>
							</table>
						</c:if>
					<!-- </div> -->
<!-------------------------------------------- End Updated data entry suite documents ----------------------------------------------------->
<!-------------------------------------------------- Bill Details-------------------------------------------------------------------------->
<%-- 	<c:if test="${not empty  command.billMasList}">
					<h4 class="panel-title table margin-top-10" id="">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a1"><spring:message code="property.BillHistory" text="Bill History"/></a>
					</h4>
					<!-- <div id="a1" class="panel-collapse collapse in"> -->
                     <div style="height:250px; overflow:auto">
						<table id="BillDetails" class="table table-striped table-bordered margin-top-10">
			                    
			                        <tr> 
			                        <th width="10%"><spring:message code="propertyTax.SrNo"/></th>
			                        <th width="10%"><spring:message code="property.bill.no" text="Bill No"/></th> 
			                        <th width="10%"><spring:message code="property.bill.date" text="Bill Date"/></th> 
			                        <th width="10%"><spring:message code="property.bill.serve.date" text="Bill Serve Date"/></th>                  
	                        		<th width="10%"><spring:message code="property.Year(From-To)" text="Year(From-To)"/></th>
										<th width="12%"><spring:message code="prop.bill.print.due" text="Due Date"/></th>
										<th width="12%"><spring:message code="property.bill.init.to.date" text="Int To Date"/></th>
										<th width="12%"><spring:message code="property.TotalDemand" text="Total Demand"/></th>
										<th width="12%"><spring:message code="property.BalanceAmount" text="Balance Amount"/></th>	
										<th width="12%"><spring:message code="property.Rebate.If.Any" text="Rebate(If Any)"/></th>			
										<th colspan="2"><spring:message code="property.action" text="Action"/></th>

									</tr>	
									<tbody>
									<div id="a3" class="panel-collapse collapse in">
									<c:forEach var="billMasList" items="${command.billMasList}" varStatus="status">	
									 <tr> 	
									 <td>${status.count}</td>
									  <td>${billMasList.bmNo}</td>
									   <td>${billMasList.bmBilldtString}</td>
									    <td>${billMasList.billDistrDateString}</td>
										<td>${billMasList.bmCcnOwner}</td>
										<td>${billMasList.bmRemarks}</td>
										<td>${billMasList.currentBillFlag}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalAmount"/>
										<td class="text-right">${bmTotalAmount}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalOutstanding + billMasList.totalPenalty}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalOutstanding"/>
										<td class="text-right">${bmTotalOutstanding}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmToatlRebate}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmToatlRebate"/>	
										<td class="text-right">${bmToatlRebate}</td>			
				                    	<td class="text-center"><button class="btn btn-primary btn-sm" type="button" onclick="ViewAssDetails(${billMasList.bmIdno})"><spring:message code="property.ViewTaxDetails" text="View Tax Details"/></button></td>	                           
										<td class="text-center"><button class="btn btn-primary btn-sm" type="button" onclick="downoladBill(${billMasList.bmIdno})">Download Bill</button></td>	                           
									
									 </tr> 
									</c:forEach>
									</div>
									</tbody>
								
						</table>
			   		</div>
				</c:if>
<!---------------------------------------------------- Collection Details------------------------------------------------------------------>
			
			<c:if test="${not empty  command.collectionDetails}">
						<h4 class="panel-title table margin-top-10" id="">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a2"><spring:message code="property.PaymentHistory" text="Payment History"/></a>
					</h4>
					<div id="a2" class="panel-collapse collapse in">
					<div style="height:250px; overflow:auto">
							<table id="CollectionDetails" class="table table-striped table-bordered margin-top-10">
				                    
				                        <tr>    
				                         
				                        <th width="8%"><spring:message code="propertyTax.SrNo"/></th>                   
				                        	<th width="8%"><spring:message code="property.receiptno" text="Receipt No"/></th>
											<th width="10%"><spring:message code="property.receiptdate" text="Receipt Date"/></th>
											<th width="10%"><spring:message code="" text="Bill Nos"/></th>
											<th width="10%"><spring:message code="" text="Narration"/></th>
											<th width="12%"><spring:message code="prop.rec.print.receipt.mode" text="Receipt Mode"/></th>
											<th width="12%"><spring:message code="property.Amount" text="Amount"/></th>
											<th width="18%"><spring:message code="property.bill.received.from" text="Received From"/></th>
											<th width="12%"><spring:message code="property.action" text="Action"/></th>
										</tr>	
									
										<tbody>
										<c:forEach var="collection" items="${command.collectionDetails}" varStatus="status">	
										 <tr> 
										 <td>${status.count}</td>
											<td>${collection.lookUpParentId}</td>
											<td>${collection.lookUpCode}</td>
											<td>${collection.extraStringField1}</td>
											<td>${collection.docDescription}</td>
											<td>${collection.lookUpType}</td>
											<td class="text-right">${collection.otherField}</td>
											<td>${collection.descLangFirst}</td>
											<td class="text-center"><button class="btn btn-primary btn-sm" type="button" onclick="downoladReceipt(${collection.lookUpId},${collection.lookUpParentId},'${collection.extraStringField2}')">Download Receipt</button></td>	                           
											
											</tr>
											</c:forEach>
										</tbody>
							</table>
							</div>
					</div>				
					
					</c:if> --%>
					
					
					
					<c:if test="${not empty  command.billMasList}">
					<h4 class="panel-title table margin-top-10" id="">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a1"><spring:message code="property.BillHistory" text="Bill History"/></a>
					</h4>
					<!-- <div id="a1" class="panel-collapse collapse in"> -->
                     <div style="height:250px; overflow:auto">
						<table id="BillDetails" class="table table-striped table-bordered margin-top-10">
			                    
			                        <tr> 
			                        <th width="5%"><spring:message code="propertyTax.SrNo"/></th>
			                        <th width="10%"><spring:message code="property.bill.no" text="Bill No"/></th> 
			                        <th width="10%"><spring:message code="property.bill.date" text="Bill Date"/></th> 
			                        <th width="10%"><spring:message code="property.bill.serve.date" text="Bill Serve Date"/></th>                  
	                        		<th width="10%"><spring:message code="property.Year(From-To)" text="Year(From-To)"/></th>
										<th width="8%"><spring:message code="property.TotalDemand" text="Total Demand"/></th>
										<th width="8%"><spring:message code="" text="Total Balance"/></th>
										
										<th width="8%"><spring:message code="property.receiptno" text="Receipt No"/></th>
										<th width="10%"><spring:message code="property.receiptdate" text="Receipt Date"/></th>
											<th width="8%"><spring:message code="" text="Receipt Amount"/></th>
											<th width="24%"><spring:message code="" text="Narration"/></th>
											
											
										<th><spring:message code="property.action" text="Action"/></th>

									</tr>	
									<tbody>
									<div id="a9" class="panel-collapse collapse in">
									<c:forEach var="billMasList" items="${command.billMasList}" varStatus="status">	
									
									
									 <tr>
									  	<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${status.count}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmNo}
										<a href="javascript:void(0);" onclick="downoladBill(${billMasList.bmIdno})"><spring:message code="" text="Download Bill"/></a>
										</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmBilldtString}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.billDistrDateString}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}">${billMasList.bmCcnOwner}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalAmount"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${bmTotalAmount}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalBalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalBalAmount"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${bmTotalBalAmount}</td>
										
										<c:choose>
										<c:when test="${empty billMasList.billWiseReceiptList}">
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										</c:when>
										<c:otherwise>
										<c:forEach var="receipt" items="${billMasList.billWiseReceiptList}" varStatus="status">
									<tr>
									
									
											<c:choose>
											  <c:when test="${receipt.extraStringField1 eq 'Y'}">
											      
									    <td><i class="text-red-1">${receipt.lookUpParentId}</i></td>
										<td><i class="text-red-1">${receipt.lookUpCode}</i></td>
										<td><i class="text-red-1">${receipt.otherField}</i></td>
										<td><i class="text-red-1">${receipt.docDescription} (Cheque Dishour)</i></td>
										
										
											   </c:when>
											   <c:otherwise>
											       <td>${receipt.lookUpParentId}</td>
										<td>${receipt.lookUpCode}</td>
										<td>${receipt.otherField}</td>
										<td>${receipt.docDescription}</td>
											    </c:otherwise>
											</c:choose>
	
										
									
									    
										
										
										
										<td class="text-center">
												<button class="btn btn-primary btn-sm" type="button"
																onclick="downoladReceipt(${receipt.lookUpId},${receipt.lookUpParentId},'${receipt.extraStringField2}')">Download
																Receipt</button>
														</td>
									</tr>
									</c:forEach>
										
										</c:otherwise>
										</c:choose>
										
										
									</tr>
									
									</c:forEach>
									</div>
									</tbody>
									<tfoot>
									<tr><th colspan="5"><spring:message code="taxdetails.total" text="Total"/></th>
									<th class="text-right">${command.grandTotalDemand}						
									</th>
									<th class="text-right">${command.grandTotalBalance}						
									</th>
									<th></th>
									<th></th>
									<th class="text-left">${command.grandReceiptAmount}</th>
									<th></th>
									<th></th>
									</tr>
									</tfoot>
								
						</table>
			   		</div>

				<div class="text-center padding-top-10">
					<button class="btn btn-blue-2 padding-top-10" id='btnPrint' onclick='propertyBillPrint()'><spring:message code="property.bill.print" text="Print Bill Detail"/></button>

					
						<c:if test="${not empty command.billMasList}">
							<button type="button" class="btn btn-info" id=""
								onclick="checkViewPropertyDues('${command.billMasList[0].propNo}_${command.billMasList[0].flatNo}')">
								<spring:message code="property.view.checkPropDues"
									text="Check Property Dues" />
							</button>
						</c:if>
						
					</div>
				</c:if>
				<c:if test="${not empty command.documentList}">
					<h4 class="panel-title table margin-top-10">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a3"> <spring:message
								code="" text="Adjustment Entry" />
						</a>
					</h4>
					<div id="a3" class="panel-collapse collapse in">
						<table id="docs"
							class="table table-striped table-bordered margin-top-10">
							<tr>
								<th width="10%"><spring:message code="propertyTax.SrNo"
										text="srno" /></th>
								<th width="20%"><spring:message code="documnts.name"
										text="Document Name" /></th>		
								<th width="20%"><spring:message code="property.Documents"
										text="Documents" /></th>
								<th width="20%"><a data-toggle="tooltip"
									onclick="fetchAdjustmentHistory(this)" data-placement="top"
									title="" class="btn btn-blue-1 btn-sm"
									data-original-title="History"> <i class="fa fa-history"></i>
								</a></th>
							</tr>
							<tbody>
								<c:forEach items="${command.documentList}" var="lookUp"
									varStatus="lk">
									<tr>
										<td><label>${lookUp.clmSrNo}</label></td>
										<c:choose>
											<c:when	test="${userSession.getCurrent().getLanguageId() eq 1}">
												<td><label>${lookUp.clmDescEngl}</label></td>
											</c:when>
											<c:otherwise>
												<td><label>${lookUp.clmDesc}</label></td>
											</c:otherwise>
										</c:choose>
										<td style="text-align: center;"><c:set var="links"
													value="${fn:substringBefore(lookUp.attPath, lookUp.attFname)}" />
												
													<apptags:filedownload filename="${lookUp.attFname}"
														filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}"
														actionUrl="ViewPropertyDetail.html?Download"></apptags:filedownload>
												 </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				
				<c:if test="${not empty command.billDeletionDocumentList}">
					<h4 class="panel-title table margin-top-10">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a3"> <spring:message
								code="" text="Bill Deletion" />
						</a>
					</h4>
					<div id="a3" class="panel-collapse collapse in">
						<table id="docs"
							class="table table-striped table-bordered margin-top-10">
							<tr>
								<th width="10%"><spring:message code="propertyTax.SrNo"
										text="srno" /></th>
								<th width="20%"><spring:message code="property.Documents"
										text="Documents" /></th>
								<!-- <th width="20%"><a data-toggle="tooltip"
									onclick="fetchAdjustmentHistory(this)" data-placement="top"
									title="" class="btn btn-blue-1 btn-sm"
									data-original-title="History"> <i class="fa fa-history"></i>
								</a></th> -->
							</tr>
							<tbody>
								<c:forEach items="${command.billDeletionDocumentList}" var="lookUp"
									varStatus="lk">
									<tr>
										<td><label>${lookUp.clmSrNo}</label></td>
										<td style="text-align: center;"><c:set var="links"
													value="${fn:substringBefore(lookUp.attPath, lookUp.attFname)}" />
												
													<apptags:filedownload filename="${lookUp.attFname}"
														filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}"
														actionUrl="ViewPropertyDetail.html?Download"></apptags:filedownload>
												 </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				
				<!-- Notice /Other Document History -->
				<%-- <c:if test="${not empty  command.noticeDetails}">
					<h4 class="panel-title table margin-top-10" id="">
						<a data-toggle="collapse" class=""
							data-parent="#accordion_single_collapse" href="#a2">Notice /Other Document
							History</a>
					</h4>
					<div id="a2" class="panel-collapse collapse in">
						<table id="CollectionDetails"
							class="table table-striped table-bordered margin-top-10">

							<tr>

								<th width="10%"><spring:message code="propertyTax.SrNo" /></th>
								<th width="12%">Document Name</th>
								<th width="10%">Document No</th>
								<th width="12%">Document Date</th>
								<th width="12%">Download</th>
							</tr>

							<tbody>
							
								<c:forEach var="notice" items="${command.noticeDetails}"
									varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>${notice.noticeTypeDesc}</td>
										<td>${notice.notNo}</td>
										<td><fmt:formatDate value="${notice.notDate}" pattern="dd/MM/yyyy" /></td>
										<td class="text-center"><button
												class="btn btn-primary btn-sm" type="button"
												onclick="downoladSpecialNotice('${notice.notNo}')">Download</button></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</c:if> --%> 
					
	</div>	
	
	<div class="margin-top-20">
	<apptags:input labelCode="Balance amount to adjust" path="totalExcessAmntToAdjust" isDisabled="true"></apptags:input>
	</div>
	
	<div class="col-sm-12 text-center padding-top-10">
					
						<%-- <button type="button" class="btn btn-blue-2"  id="back"
									onclick="BackToSearch()"><spring:message code="property.Back" text="Back"/>
						</button>  --%>
						
						<input type='button' class="btn btn-primary" id='btnPrint' value='Print' onclick='window.print()' />
						
						 <button type="button" class="btn btn-info"  id="back"
									onclick="propertyBillPrint()"><spring:message code="property.view.ledger" text="View Ledger Report"/>
						</button>
						
						<button type="button" class="btn btn-danger"  id="back"
									onclick="window.location.href='ViewPropertyDetail.html'"><spring:message code="property.Back" text="Back"/>
						</button>
						<!-- <input type='button' id='btnPrint' value='Print'
		style='width: 100px; position: fixed; bottom: 10px; left: 47%; z-index: 111;'
		onclick='window.print()' /> -->
	</div>
	</form:form>
	</div>
</div>


