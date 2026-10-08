<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="js/property/viewHistory.js"></script>

<style>

input[type='text'], textarea, #jointOwnerTable  {
	text-transform: uppercase;
}

input[type="text"].form-control {
	border: 0;
	font-weight: 600;
	border-bottom: 1px dotted #333;
	padding-left: 0;
	background: none;
}
input[type="text"].form-control:focus {
	outline: none;
}
textarea.form-control {
	border: 0;
	font-weight: 600;
	border-bottom: 1px dotted #333;
	padding-left: 0;
	background: none;
}
textarea.form-control:focus {
	outline: none;
}
select.form-control {
	border: 0;
	font-weight: 600;
	border-bottom: 1px dotted #333;
	padding-left: 0;
	background: none;
}
select.form-control:focus {
	outline: none;
}

.stylenone{
border-bottom: none !important;
}

</style>

<div id="taskDiv">
 <div class="widget">
    <div class="widget-header">
		<h2><strong><spring:message code="property.AuthorizationHistoryView" text="Authorization History View"/></strong></h2>
	</div>	
		
        <div class="widget-content padding">
			<form:form action="PropertyShowHistory.html" class="form-horizontal form" name="PropertyShowHistory" id="PropertyShowHistory">
			<div class="accordion-toggle ">
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#propertyMaster"><spring:message
							code="property.propertyMaster" text="Property Master" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="propertyMaster">
				<div class="form-group propInfo">
					<c:if test="${not empty command.provisionalAssesmentMstDto.assNo}">
						<apptags:input labelCode="propertydetails.PropertyNo." path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
					</c:if>
					<c:if test="${not empty command.provisionalAssesmentMstDto.remarks}">
						<apptags:textArea labelCode="property.remark" path="provisionalAssesmentMstDto.remarks" isDisabled="true"></apptags:textArea>
					</c:if>		
				</div>

				<div class="form-group propInfo">
					<apptags:input cssClass="alphaNumeric preventSpace"
						labelCode="propertydetails.refPropNo"
						path="provisionalAssesmentMstDto.refPropNo" maxlegnth="40"
						isDisabled="true"></apptags:input>

					<apptags:input cssClass="alphaNumeric preventSpace"
						labelCode="propertydetails.gisRefNo"
						path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"
						isDisabled="true"></apptags:input>

				</div>
					
				<div class="form-group propInfo">
			
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc1" isDisabled="true"></apptags:input>
				
				<c:if test="${command.provisionalAssesmentMstDto.assWard2 ne null}">			
				<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc2" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group propInfo">
				<c:if test="${command.provisionalAssesmentMstDto.assWard3 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc3" isDisabled="true"></apptags:input>
				</c:if>
				<c:if test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc4" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group propInfo">
				<c:if test="${command.provisionalAssesmentMstDto.assWard5 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc5" isDisabled="true"></apptags:input>
				</c:if>
				</div>
			
						<div class="form-group propInfo">
							<apptags:input labelCode="unitdetails.RoadType" path="provisionalAssesmentMstDto.proAssdRoadfactorDesc" 
							isDisabled="true"></apptags:input>
							
							<label for="surveyType"
								class="col-xs-2 col-sm-2 control-label required-control"><spring:message
									code="property.billingMethod" text="Billing Method" /> </label>
							<c:set var="baseLookupCodess" value="BLM" />
							<apptags:lookupField
								items="${command.getLevelData(baseLookupCodess)}"
								path="provisionalAssesmentMstDto.billMethod"
								cssClass="form-control" hasChildLookup="false" hasId="true"
								showAll="false" selectOptionLabelCode="Select " disabled="true"/>

						</div>

						<div class="form-group">

							<label for="proAssAcqDate" class="col-xs-2 col-sm-2 control-label"><spring:message
									code="propertydetails.firstAssessmentDate" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input
									path="provisionalAssesmentMstDto.proAssAcqDateFormat"
									type="text" id="proAssAcqDate" class="form-control"
									disabled="true" />
							</div>

							 
							 <label for="lastAssessmentDate" class="col-xs-2 col-sm-2 control-label"><spring:message
							 		code="propertydetails.lastAssessmentDate" text="Last Assessment Date"/></label>
							 <div class="col-xs-4 col-sm-4">
							 	<form:input
							 		path="provisionalAssesmentMstDto.lastAssessmentDateFormat"  value=""
							 		type="text" id="lastAssessmentDate" class="form-control"
							 		disabled="true" />				
							 				
							 </div>

						</div>

						<fmt:formatDate pattern="dd-MMM-yyyy"
							value="${command.provisionalAssesmentMstDto.lastAssessmentDate}"
							var="lastAssessDate" />
						<fmt:formatDate pattern="dd-MMM-yyyy"
							value="${command.provisionalAssesmentMstDto.reviseAssessmentDate}"
							var="reviseAssessDate" />
							<div class="form-group">

							<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" />
											</div>		
							<label for="constructPermissionDate" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.constructPermissionDate" /></label>
										<div class="col-sm-4">
											<form:input type="text" class="form-control" 
											 path="provisionalAssesmentMstDto.constructPermissionDateFormat"
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
								
							<apptags:input labelCode="property.propertytype"
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc1"
								isDisabled="true"></apptags:input>
							<form:hidden path="landTypePrefix" class="form-control landValue" />
							<form:hidden class="form-control" id="khasraNo" path="knownKhaNo" />

						</div>
						
			</div>
           
            <div class="accordion-toggle">
            <h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Ownershipdetail" /></a>
			</h4>
     
	<div class="panel-collapse collapse in" id="OwnshipDetail">
			
			<div class="form-group">
				<label class="col-xs-2 col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-xs-4 col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
				<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
				
				</div>
		    </div>
		    
		<c:choose>
		<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
	 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
        <tbody>
        <tr>
                   
        <th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
		<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
		<th width="10%"><spring:message code="property.email" /></th>
		<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
		<th width="10%"><spring:message code="ownersdetail.pancard" /></th>                    	 
        </tr>
                		
      <tr>
		<td>
			<form:input id="assoOwnerName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
		</td>
			
		<td>
			<form:input id="assoMobileno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
		</td>
		
		<td><form:input id="emailId" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control preventSpace" disabled="true"/>   
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
						<tbody>
							<tr>
								<th width="17%"><spring:message code="ownersdetail.ownersname" /></th>
								<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
								<th width="11%"><spring:message code="ownersdetail.mobileno" /></th>
								<th width="10%"><spring:message code="property.email" /></th>
								<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
								<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
							</tr>

						<c:forEach var="ownershipTypeList" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
            			<tr>
                		 <td>${ownershipTypeList.assoOwnerName}</td>
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
							<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
							<th width="10%"><spring:message code="property.email" /></th>
							<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						
 						<tr>
 						<td> 
 						<form:input id="assoOwnerName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
 						</td> 
 						
 						<td><form:input id="assoMobileno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
 						</td> 
 						
 						<td><form:input id="emailId_${d}"
										path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control preventSpace" disabled="true"/>   
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
            	                    	
			
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="propertyAddress">
						<div class="form-group">
							<label for="address" class="col-xs-2 col-sm-2 control-label "><spring:message
									code="property.propertyaddress" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input path="provisionalAssesmentMstDto.assAddress"
									type="text" id="address" class="form-control" disabled="true" />
							</div>
								<label class="col-xs-2 col-sm-2 control-label " for="areaName"><spring:message
									code="property.areaName" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.areaName" id="areaName"
									disabled="true"></form:input>
							</div>
							
						</div>


						<div class="form-group">
							<label class="col-xs-2 col-sm-2 control-label " for="tppPlotNo"><spring:message
									code="property.house.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo" disabled="true"></form:input>
							</div>
							
							<label class="col-xs-2 col-sm-2 control-label " for="tppKhataNo"><spring:message
									code="property.khata.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo" disabled="true"></form:input>
							</div>
							
						</div>
						
						<div class="form-group">
							<label for="location" class="col-xs-2 col-sm-2 control-label "><spring:message
									code="property.surveyType" /></label>
							<div class="col-xs-4 col-sm-4">
								<c:choose>
									<c:when test="${command.provisionalAssesmentMstDto.surveyType eq '0'}">								
										<form:input path=""
											type="text" id="surveyType" class="form-control"
											disabled="true" />
									</c:when>
									<c:otherwise>								
										<form:input path="provisionalAssesmentMstDto.surveyTypeDesc"
											type="text" id="surveyType" class="form-control"
											disabled="true" />
									</c:otherwise>
								</c:choose>
							</div>							

							<label class="col-xs-2 col-sm-2 control-label " for="tppSurveyNumber"><spring:message
									code="property.khasra.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppSurveyNumber"
									id="tppSurveyNumber" disabled="true"></form:input>
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
							
							<label class="col-xs-2 col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" disabled="true"></form:input>
							</div>
							
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

							<label class="col-xs-2 col-sm-2 control-label " for="buildingNo"><spring:message
									code="property.buildingNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="provisionalAssesmentMstDto.buildingNo" id="buildingNo" disabled="true"></form:input>
							</div>
						</div>
			
          </div>
         
		
		    <form:hidden  path="" id="assType" value="${command.getAssType()}"/>
		    <form:hidden  path="" id="authLevel" value="${command.authLevel}"/>
		    
 			<h4 class="margin-top-10 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
 			</h4>
			<div class="panel-collapse collapse in" id="UnitDetail" ><!-- style="overflow:scroll" -->
			<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>
                        <tr>
 							<th width="6%"><spring:message code="unitdetails.year"/></th>
                        	<th><spring:message code="unitdetails.unitSrNo"/></th> 	
							<th class=""><spring:message code="unitdetails.flatNo"/></th>
                        	<th><spring:message code="unitdetails.floorno"/></th>
							<th><spring:message code="unitdetails.occupancy"/></th>		
				 			<th><spring:message code="unitdetails.OccupierName" /></th>
                        	<th><spring:message code="unitdetails.legal.illegal"/></th>
                        	<th><spring:message code="unitdetails.constructiontype"/></th>
                        	<th><spring:message code="unitdetails.usagefactor"/></th>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype2 ne null }">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype3 ne null}">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype4 ne null}">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype5 ne null}">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<th><label for="actualRent${status.count-1}"
														class="col-xs-2 col-sm-2 control-label"><spring:message
															code="unitdetails.actualRent" /></label></th>
                   	 	    <th width="" class=""><spring:message code="unitdetails.carpetArea" text="Carpet Area"/></th>
        	 				<th><spring:message code="unitdetails.taxdetail"/></th>
        	 				<th><spring:message code="unitdetails.rate"/></th>
             	 			<c:if test="${command.getAssMethod() eq 'ARV'}">
             	 			<th><spring:message code="unitdetails.ARV"/></th>
             	 			<th><spring:message code="unitdetails.deductionCharge"/></th>
							<th><spring:message code="unitdetails.RV"/></th>
							<c:if test="${command.getServiceShortCode() eq 'CIA'}">
								<th><spring:message code="unitdetails.areaOrRv" text="Area/RV Based"/></th>
							</c:if>
							<c:if test="${command.getServiceShortCode() eq 'BIF'}">
								<th><spring:message code="unitdetails.areaOrRv" text="Area/RV Based"/></th>
							</c:if>
							</c:if>
							<c:if test="${command.getAssMethod() eq 'CV'}">
							<th><spring:message code="unitdetails.CV"/></th>
							</c:if>
             	 			<th colspan="2"><spring:message code="unit.ViewMoreDetails"/></th>
                        </tr>
                        
                         
                  <c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                       	<tr>
	                        <td class="text-center">${unitDetails.proFaYearIdDesc}</td>                                                                                                          
	                        <td class="text-center">${unitDetails.assdUnitNo}</td>
							<td class="text-center">${unitDetails.flatNo}</td>	
	                        <td class="text-center">${unitDetails.proFloorNo} </td>
	                         <td class="text-center" ><c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control stylenone changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false" disabled="true"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/></td>
	                        
	                         <td class="text-center"><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
															type="text" class="form-control stylenone" id="occupierName"
															disabled="true" /></td>
                            <td class="text-center">${unitDetails.legal}</td>
	                        <td class="text-center">${unitDetails.proAssdConstruTypeDesc}</td>
	                        <td class="text-center">${unitDetails.proAssdUsagetypeDesc} </td>
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc2 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc2} </td>
	                        </c:if>
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc3 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc3} </td>
	                        </c:if>
	                        
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc4 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc4} </td>
	                        </c:if>
	                        
	                        <c:if test="${unitDetails.proAssdUsagetypeDesc5 ne null}">
	                          <td class="text-center">${unitDetails.proAssdUsagetypeDesc5} </td>
	                        </c:if>
	                        <td><form:input	path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
									type="text" class="form-control has2Decimal mandColorClass text-center stylenone"
									id="actualRent${status.count-1}" data-rule-required="true"
									onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" placeholder="0.0"/> </td>
	                        
	                        <fmt:formatNumber type="number" value="${unitDetails.carpetArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="carpetArea"/>
	                        <td class="text-center">${carpetArea}</td>
	                        <fmt:formatNumber type="number" value="${unitDetails.assdBuildupArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="assdBuildupArea"/>                        
	                    	<td class="text-center">${assdBuildupArea}</td>
	                    		                    	
	                    	<td class="text-center">${unitDetails.assdStdRate} </td>
	                    	<c:if test="${command.getAssMethod() eq 'ARV'}">
	                    	<fmt:formatNumber type="number" value="${unitDetails.assdAlv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="assdAlv"/>
	                    	<td class="text-center">${assdAlv} </td>
	                    	<td class="text-center">${unitDetails.maintainceCharge} </td>
	                    	<fmt:formatNumber type="number" value="${unitDetails.assdRv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="assdRv"/>
	                        <td class="text-center">${assdRv}</td>
	                        <c:if test="${command.getServiceShortCode() eq 'CIA'}">
	                        	<td class="text-center">${unitDetails.areaOrRvBasedChange} </td>
	                        </c:if>
	                        <c:if test="${command.getServiceShortCode() eq 'BIF'}">
	                        	<td class="text-center">${unitDetails.areaOrRvBasedChange} </td>
	                        </c:if>
	                        </c:if>
	                        <c:if test="${command.getAssMethod() eq 'CV'}">
	                    	<td class="text-center">${unitDetails.assdCv}</td>
	                    	</c:if>
	                    	<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="viewRoomHistory(${status.count-1},this)" value="<spring:message code="property.room" text="Room" />"></td>
    					</tr>

            
							<c:set var = "hiddenOccupancyTypeDesc" value = "${unitDetails.getProAssdOccupancyTypeDesc()}"/>										
							<c:if test="${fn:contains(hiddenOccupancyTypeDesc, 'Ten')}">
								<div class="form-group">
									<label for="actualRent${status.count-1}"
										class="col-xs-2 col-sm-2 control-label"><spring:message
											code="unitdetails.actualRent" /></label>
									<div class="col-xs-4 col-sm-4">
										<form:input
											path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
											type="text"
											class="form-control has2Decimal mandColorClass text-left stylenone"
											id="actualRent${status.count-1}"
											data-rule-required="true"
											onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" />
									</div>
								</div>
							</c:if>
				
								<!-- </div>
				         	</td>
				         </tr> -->
                   </c:forEach>

               </tbody>						
               	<tfoot>
               		<c:if test="${command.provisionalAssesmentMstDto.totalFloorCountResidential gt 0}">
				         <tr>
				             <td colspan="2"><spring:message code="unitdetails.totalResidentila"/></td>
				             <td colspan="1" class="text-center">${command.provisionalAssesmentMstDto.totalFloorCountResidential}</td> 
				             <td colspan="9"></td>
				             <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalBuildupAreaResidential}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildupAreaResidential"/>
				             <td colspan="1" class="text-center">${totalBuildupAreaResidential}</td> 
				             <td colspan="3"></td>
				             <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalRvResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvResidential"/>
				             <td colspan="1" class="text-center">${totalRvResidential}</td> 
				         </tr>
				     </c:if>
				     <c:if test="${command.provisionalAssesmentMstDto.totalFloorCountNonResidential gt 0}">    
				      <tr>
				        <td colspan="2"><spring:message code="unitdetails.totalNonResidential"/></td>
				        <td colspan="1" class="text-center">${command.provisionalAssesmentMstDto.totalFloorCountNonResidential}</td> 
							 <td colspan="9"></td>
							 <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalBuildupAreaNonResidential}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildupAreaNonResidential"/>
							 <td colspan="1" class="text-center">${totalBuildupAreaNonResidential}</td> 
							 <td colspan="3"></td>
							 <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalRvNonResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvNonResidential"/>
							 <td colspan="1" class="text-center">${totalRvNonResidential}</td> 
					  </tr>
						</c:if>
						<c:if test="${command.provisionalAssesmentMstDto.totalFlatCount gt 0}">    
						     <tr>
						        <td colspan="2"><spring:message	code="propertyTax.Total" text="Total"/></td>
						        <td colspan="1" class="text-center">${command.provisionalAssesmentMstDto.totalFlatCount}</td> 
						        <td colspan="9"></td>
						        <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalBuildUpArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildUpArea"/>
						        <td colspan="1" class="text-center">${totalBuildUpArea}</td> 
						        <td colspan="3"></td>
						        <fmt:formatNumber type="number" value="${command.provisionalAssesmentMstDto.totalRv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRv"/>
						        <td colspan="1" class="text-center">${totalRv}</td> 
						    </tr>
						</c:if>     
						</tfoot>
			</table>
      </div>
              
               
 <c:if test="${not empty command.provAsseFactDtlDto[0].assfFactorValueId}"> 
	         
		<h4 class="margin-top-10 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#unitSpecificInfo"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 		</h4>
		<div class="panel-collapse collapse in" id="unitSpecificInfo">      
 			
 			<table id="unitSpecificInfoTable" class="table table-striped table-bordered ">
            
            <tr>
            	 <th width="20%"><spring:message code="unit.Factor"/></th>
                 <th width="10%"><spring:message code="unit.ApplicableUnitNo"/></th>
                 <th width="30%"><spring:message code="unit.FactorValue"/></th>
                 <th width="" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                 <th width="" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                 <th width="" class=""><spring:message code="property.Amount" text="Amount"/></th>
            </tr>
            <tbody>
            <c:forEach var="factorDetails" items="${command.getProvAsseFactDtlDto()}" >
            <c:if test="${factorDetails.factorValueCode ne 'AMT'  && factorDetails.factorValueCode ne 'VCT'  && factorDetails.factorValueCode ne 'AGE'}">
            <tr>
              <td>${factorDetails.proAssfFactorIdDesc}</td>                                                                                                          
              <td>${factorDetails.unitNoFact}</td>
              <td>${factorDetails.proAssfFactorValueDesc}</td>
              <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${factorDetails.factorDate}" /></td>
              <td>${factorDetails.factorRemark}</td>
              <td>${factorDetails.factorAmount}</td>
             </tr>
             </c:if>		
			</c:forEach>
			 </tbody>
			</table>	
		</div>
 
  </c:if> 
  
       </div>      
      <div class="text-center padding-top-10 hidden-print">

		<apptags:backButton url="AdminHome.html"></apptags:backButton>									
		</div>
	</div>	
	</form:form>
</div>
 </div> 

</div>

