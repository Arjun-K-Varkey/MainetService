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
<script type="text/javascript" src="js/property/PropertyAuthorizationBeforeAfter.js"></script>
<script type="text/javascript" src="js/property/unitDetails.js"></script>

<style>
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

#companyDetailTable,#jointOwnerTable,#singleOwnerTable{    width: 780px !important;}

.box-shadow-right-1 { box-shadow: 1px 0px 0px #333;}
</style>



      <div class="widget">
        <div class="widget-header">
          <h2>Authorization Preview</h2>
          <div class="additional-btn"> <a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i><span class="hide">Help</span></a> </div>
        </div>
        <div class="widget-content padding" id="receipt">
        <form:form action="SelfAssessmentForm.html" class="form-horizontal form" name="frmSelfAssessmentForm" id="frmSelfAssessmentForm">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
          <div class="row">
            <div class="col-sm-6 box-shadow-right-1">
			<!-- Start Each Section -->

					<div class="accordion-toggle ">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyMaster"><spring:message
									code="property.propertyMaster" text="Property Master" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyMaster">
							<div class="form-group">
								<apptags:input labelCode="propertydetails.PropertyNo."
									path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
							</div>

							<c:if test="${command.getIntgrtionWithBP() eq 'Y'}">
								<div class="form-group">
									<apptags:input labelCode="property.buildingpermission" path=""
										isDisabled="true"></apptags:input>
									<apptags:input labelCode="propertydetails.PropertyNo."
										path="authCompBeforeDto.assNo" isDisabled="true"></apptags:input>
								</div>

								<div class="form-group">
									<apptags:input labelCode="property.LandPermissionNo." path=""
										isDisabled="true"></apptags:input>
								</div>
							</c:if>


							<div class="form-group">
								<apptags:input cssClass="alphaNumeric preventSpace"
									labelCode="propertydetails.refPropNo"
									path="authCompBeforeDto.refPropNo" maxlegnth="40"
									isDisabled="true"></apptags:input>

								<apptags:input cssClass="alphaNumeric preventSpace"
									labelCode="propertydetails.gisRefNo"
									path="authCompBeforeDto.assGisId" maxlegnth="40"
									isDisabled="true"></apptags:input>

							</div>

							<div class="form-group">

								<apptags:input labelCode="property.propertyZone"
									path="authCompBeforeDto.assWardDesc1"
									isDisabled="true"></apptags:input>

								<c:if
									test="${command.provisionalAssesmentMstDto.assWard2 ne null}">
									<apptags:input labelCode="property.propertyWard"
										path="authCompBeforeDto.assWardDesc2"
										isDisabled="true"></apptags:input>
								</c:if>
							</div>
							<div class="form-group">
								<c:if
									test="${command.provisionalAssesmentMstDto.assWard3 ne null}">

									<apptags:input labelCode="property.propertyWard"
										path="authCompBeforeDto.assWardDesc3"
										isDisabled="true"></apptags:input>
								</c:if>
								<c:if
									test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
									<apptags:input labelCode="property.propertyWard"
										path="authCompBeforeDto.assWardDesc4"
										isDisabled="true"></apptags:input>
								</c:if>
							</div>
							<div class="form-group">
								<c:if
									test="${command.provisionalAssesmentMstDto.assWard5 ne null}">

									<apptags:input labelCode="property.propertyWard"
										path="authCompBeforeDto.assWardDesc5"
										isDisabled="true"></apptags:input>
								</c:if>
							</div>

							<div class="form-group">
								<apptags:input labelCode="unitdetails.RoadType"
									path="authCompBeforeDto.proAssdRoadfactorDesc"
									isDisabled="true"></apptags:input>

								<label for="surveyType"
									class="col-sm-2 control-label"><spring:message
										code="property.billingMethod" text="Billing Method" /> </label>
								<c:set var="baseLookupCodess" value="BLM" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCodess)}"
									path="authCompBeforeDto.billMethod"
									cssClass="form-control" hasChildLookup="false" hasId="true"
									showAll="false" selectOptionLabelCode="Select " />
							</div>
							
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.authCompBeforeDto.firstAssessmentDate}"
								var="firstAssessDate" />
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.authCompBeforeDto.lastAssessmentDate}"
								var="lastAssessDate" />
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.authCompBeforeDto.reviseAssessmentDate}"
								var="reviseAssessDate" />
								
							<div class="form-group">

								<label for="proAssAcqDate" class="col-sm-2 control-label"><spring:message
										code="propertydetails.firstAssessmentDate" /></label>
								<div class="col-sm-4">
									<form:input
										path="authCompBeforeDto.proAssAcqDateFormat"
										type="text" id="proAssAcqDate" class="form-control"
										disabled="true" />
								</div>
								
								<label for="lastAssessmentDate" class="col-sm-2 control-label"><spring:message
										code="propertydetails.lastAssessmentDate" text="Last Assessment Date"/></label>
								<div class="col-sm-4">
									<form:input
										path=""  value="${lastAssessDate}"
										type="text" id="lastAssessmentDate" class="form-control"
										disabled="true" />								
								</div>
								
							</div>

						<div class="form-group">
							<label for="constructPermissionNo" class="col-sm-2 control-label">
									<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control" 
									path="authCompBeforeDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true"/>
							</div>		
							<label for="constructPermissionDate" class="col-sm-2 control-label">
									<spring:message	code="unitdetails.constructPermissionDate" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control" 
									path="authCompBeforeDto.constructPermissionDateFormat" id="constructPermissionDate" disabled="true" />
										</div>
						</div>
							

							<div class="form-group">
								<label for="permissionUseNo" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.permissionUseNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control" path="authCompBeforeDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
											id="permissionUseNo" disabled="true" />
								</div>	
									
								<label for="yearOfConstruc0" class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
								<div class="col-xs-4 col-sm-4">
									<form:input	path="authCompBeforeDto.AssdYearConstructionDateFormat"  value=""
									type="text" id="yearOfConstruc0" class="form-control" disabled="true" />
								</div>

							</div>
							
							<div class="form-group">
								<apptags:input labelCode="property.propertytype"
									path="authCompBeforeDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc1"
									isDisabled="true"></apptags:input>
							</div>
						</div>
					</div>
					<!----------------------------------------- Owner Details Before (First owner will be the primary owner)  ------------------------------->
           
            <div class="accordion-toggle ">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Ownershipdetail" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="OwnshipDetail">
			
			<div class="form-group">
				<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-sm-4"><form:input path="authCompBeforeDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
				<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
				
				</div>
		    </div>
		   
		    
		<c:choose>
		<c:when test="${command.getOwnershipPrefixBefore() eq 'SO'}">
		<div class="table-responsive">
	 	<table id="singleOwnerTable" class="table table-striped table-bordered " >
        <tbody>
        <tr>
                   
        <th width="25%"><spring:message code="ownersdetail.ownersname" /></th>
        <th width="20%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
		<th width="15%"><spring:message code="ownersdetail.mobileno" /></th>
		<th width="15%"><spring:message code="property.email" /></th>
		<th width="15%"><spring:message code="ownersdetail.adharno" /></th>
		<th width="15%"><spring:message code="ownersdetail.pancard" />                    	 
        </tr>
                		
      <tr>
		<td>
			<form:input id="assoOwnerName" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
		</td>
			
		<td>
			<form:input id="assoOwnerNameReg" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/>
		</td>						
	
		<td><form:input id="assoMobileno_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
		</td>
		
		<td><form:input id="emailId_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
		</td>
							
		<td class="ownerDetails">
		<form:input id="assoAddharno_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno" class="form-control "  disabled="true"/>
		</td>
		<td class="companyDetails">
		<form:input id="assoPanno_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control"  disabled="true"/>
		</td>
	</tr>
	</tbody>
    </table>
    </div>
	</c:when>
		 	 	
		 <c:when test="${command.getOwnershipPrefixBefore() eq 'JO'}">	
		 <c:set var="a" value='0'/>
		 <div class="table-responsive">
					<table id="jointOwnerTable" class="table text-left table-striped table-bordered">
						<tbody>
								<tr>
									<th width="25%"><spring:message code="ownersdetail.ownersname" /></th>
									<th width="20%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
									<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
									<th width="15%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="15%"><spring:message code="property.email" /></th>
									<th width="15%"><spring:message code="ownersdetail.adharno" /></th>
									<th width="15%"><spring:message code="ownersdetail.pancard" /></th>
								</tr>

						<c:forEach var="ownershipTypeList" items="${command.getAuthCompBeforeDto().getProvisionalAssesmentOwnerDtlDtoList()}">
            			<tr>
                		 <td>${ownershipTypeList.assoOwnerName}</td>
                		 <td>${ownershipTypeList.assoOwnerNameReg}</td>
                		 <td>${ownershipTypeList.propertyShare}</td>
                		 <td>${ownershipTypeList.assoMobileno}</td>
                		 <td>${ownershipTypeList.eMail}</td>					                		                 		 
                		 <td>${ownershipTypeList.assoAddharno}</td>
                		 <td>${ownershipTypeList.assoPanno}</td>
            			</tr>
                		 </c:forEach>
                		
						</tbody>
					</table></div>
					</c:when>
				<c:otherwise>
				 <div class="table-responsive">
			<table id="companyDetailTable" class="table text-left table-striped table-bordered">
					<tbody>					
						<tr>
									<th width="25%"><spring:message code="property.NameOf" /> ${command.getOwnershipTypeValue()}</th>
									<th width="25%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
									<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="15%"><spring:message code="property.email" /></th>
									<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						
 						<tr>
 						<td> 
 						<form:input id="assoOwnerName_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
 						</td> 
						
						<td> 
 						<form:input id="assoOwnerNameReg_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/> 
 						</td>
								
 						<td><form:input id="assoMobileno_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
 						</td> 
 						
 						<td><form:input id="emailId_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
						</td>
								
						<td class="companyDetails"> 
						<form:input id="assoPanno_${d}" path="authCompBeforeDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control" disabled="true" /> 
						</td> 
 						</tr> 								
					</tbody>
					</table>
					</div>				
					</c:otherwise>	
					</c:choose>
				 </div>
        
				
<!-----------------------------------------------------------Property Address Details Before-------------------------------------------------------------->
			
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="propertyAddress">
			<div class="form-group">			                
	              	<label for="address" class="col-sm-2 control-label "><spring:message code="property.propertyaddress"/></label>
	              	<div class="col-sm-4">
	              		<form:input path="authCompBeforeDto.assAddress" type="text" id="address" class="form-control" disabled="true"/>
	              	</div>
	              	<label class="col-sm-2 control-label " for="areaName"><spring:message code="property.areaName" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control"	path="authCompBeforeDto.areaName" id="areaName"	disabled="true"></form:input>
					</div>
            </div> 
            
                      
							<div class="form-group">
								<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
										code="property.house.no" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.tppPlotNo" id="tppPlotNo"></form:input>
								</div>

								<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
										code="property.khata.no" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.tppKhataNo" id="tppKhataNo"
										disabled="true"></form:input>
								</div>

							</div>

							<div class="form-group">
								<label for="location" class="col-sm-2 control-label "><spring:message
										code="property.surveyType" /></label>
								<div class="col-sm-4">
									<c:choose>
										<c:when
											test="${command.authCompBeforeDto.surveyType eq '0'}">
											<form:input path="" type="text" id="surveyType"
												class="form-control" disabled="true" />
										</c:when>
										<c:otherwise>
											<form:input path="authCompBeforeDto.surveyTypeDesc"
												type="text" id="surveyType" class="form-control"
												disabled="true" />
										</c:otherwise>
									</c:choose>
								</div>

								<label class="col-sm-2 control-label " for="tppSurveyNumber"><spring:message
										code="property.khasra.no" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.tppSurveyNumber"
										id="tppSurveyNumber" disabled="true"></form:input>
								</div>
							</div>


							<div class="form-group">
								<label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message
										code="property.addiSurveyNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.addiSurveyNo"
										id="addiSurveyNo" disabled="true"></form:input>
								</div>
								<label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message
										code="property.addiCityServeyNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.addiCityServeyNo"
										id="addiCityServeyNo" disabled="true"></form:input>
								</div>
							</div>

							<div class="form-group">

								<label class="col-sm-2 control-label " for="aaliNo"><spring:message
										code="property.aaliNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.aaliNo" id="aaliNo"
										disabled="true"></form:input>
								</div>

								<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message
										code="property.chaaltaNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.chaaltaNo" id="chaaltaNo"
										disabled="true"></form:input>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label " for="clusterNo"><spring:message
										code="property.clusterNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control hasNumber"
										path="authCompBeforeDto.clusterNo" id="clusterNo"
										disabled="true"></form:input>
								</div>

								<label class="col-sm-2 control-label " for="buildingNo"><spring:message
										code="property.buildingNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control hasNumber"
										path="authCompBeforeDto.buildingNo" id="buildingNo"
										disabled="true"></form:input>
								</div>
							</div>

					</div>
                
<!-----------------------------------------------------------Unit Details-------------------------------------------------------------------->
		
 			<h4 class="margin-top-10 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
 			</h4>
		<div class="panel-collapse collapse in" id="UnitDetail">
		<div class="table-responsive">
			<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>
                        <tr>
 							<th width="7%"><spring:message code="unitdetails.year"/></th>
                        	<th width="4%"><spring:message code="unitdetails.unitno"/></th> 
                        	<th width="12%"><spring:message code="unitdetails.flatNo"/></th>	
                        	<th width="10%"><spring:message code="unitdetails.floorno"/></th>
                        	<th><spring:message code="unitdetails.occupancy"/></th>
                        	<th><spring:message code="unitdetails.OccupierName" /></th>
                        	<th><spring:message code="unitdetails.legal.illegal"/></th>
                        	
                        	<th width="20%"><spring:message code="unitdetails.constructiontype"/></th> 
                        	<th width="10%"><spring:message code="unitdetails.usagefactor"/></th>
                        	
                        	<c:if test="${command.authCompBeforeDto.provisionalAssesmentDetailDtoList[0].assdUsagetype2 ne null }">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.authCompBeforeDto.provisionalAssesmentDetailDtoList[0].assdUsagetype3 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.authCompBeforeDto.provisionalAssesmentDetailDtoList[0].assdUsagetype4 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.authCompBeforeDto.provisionalAssesmentDetailDtoList[0].assdUsagetype5 ne null}">
                        	<th width="12%"><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	<th><spring:message	code="unitdetails.actualRent" text="Actual Annual Rent"/></th>
                   	 	    <th><spring:message code="unitdetails.carpetArea" text="Assessable Area (Sq.Mtr)"/></th>                       	
             	 			<th width="12%"><spring:message code="unitdetails.taxdetail" text="Assessed Area (Sq.Mtr)"/></th>
             	 			<th width="11%"><spring:message code="unitdetails.rate"/></th>
             	 			<c:if test="${command.getAssMethod() eq 'ARV'}">
             	 			<th width="11%"><spring:message code="unitdetails.ARV"/></th>
             	 			<th width="11%"><spring:message code="unitdetails.deductionCharge"/></th>
							<th width="12%"><spring:message code="unitdetails.RV"/></th>
							</c:if>
							<c:if test="${command.getAssMethod() eq 'CV'}">
							<th width="10%"><spring:message code="unitdetails.CV"/></th>
							</c:if>
             	 			<%-- <th width="6%"><spring:message code="unit.ViewMoreDetails"/></th> --%>
                        </tr>
                        
                         
                        <c:forEach var="unitDetails" items="${command.getAuthCompBeforeDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                       	<tr>
                        <td class="text-center">${unitDetails.proFaYearIdDesc}</td>                                                                                                          
                        <td class="text-center">${unitDetails.assdUnitNo}</td>
 						<td class="text-center">${unitDetails.flatNo}</td>
                        <td class="text-center">${unitDetails.proFloorNo} </td>
                        <td class="text-center" ><c:set var="baseLookupCode" value="OCS" /> 
				 		<apptags:lookupField items="${command.getLevelData(baseLookupCode)}" 
								path="authCompBeforeDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control stylenone changeParameterClass " 
								hasChildLookup="false" hasId="true" showAll="false" disabled="true"
				 				selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" />
				 		</td>
				 		<td class="text-center"><form:input
								path="authCompBeforeDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
								type="text" class="form-control stylenone" id="occupierName" disabled="true" />
						</td>                        
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
	                        
	                    <td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
										type="text"
										class="form-control has2Decimal mandColorClass text-center stylenone"
										id="actualRent${status.count-1}"
										data-rule-required="true"
										onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" placeholder="0.0"/> 
						</td>
	                        
	                    <fmt:formatNumber type="number" value="${unitDetails.carpetArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="carpetArea"/>
	                    <td class="text-center">${carpetArea}</td>
	                    <fmt:formatNumber type="number" value="${unitDetails.assdBuildupArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="assdBuildupArea"/>                        
	                    <td class="text-center">${assdBuildupArea}</td>
                    	<td class="text-center">${unitDetails.assdStdRate} </td>
                    	<c:if test="${command.getAssMethod() eq 'ARV'}">
                    	<td class="text-center">${unitDetails.assdAlv} </td>
                    	<td class="text-center">${unitDetails.maintainceCharge} </td>
                        <td class="text-center">${unitDetails.assdRv}</td>
                        </c:if>
                        <c:if test="${command.getAssMethod() eq 'CV'}">
                    	<td class="text-center">${unitDetails.assdCv}</td>
                    	</c:if>
                    	<%-- <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-${status.count-1}" aria-expanded="false" aria-controls="group-of-rows-${status.count-1}"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td> --%>	                           
    					</tr>

                    </c:forEach>

               </tbody>
</table></div>
               </div>
              
               
<!--------------------------------------------------- unitSpecific Additional Info Before ----------------------------------------------------->
<c:if test="${not empty command.authCompFactDto}"> 
	         
		<h4 class="margin-top-10 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#unitSpecificInfo"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 		</h4>
		<div class="panel-collapse collapse in" id="unitSpecificInfo">      
 			
 			<table id="unitSpecificInfoTable" class="table table-striped table-bordered ">
            <tbody>
            <tr>
            	 <th width="20%"><spring:message code="unit.Factor"/></th>
                 <th width="10%"><spring:message code="unit.ApplicableUnitNo"/></th>
                 <th width="30%"><spring:message code="unit.FactorValue"/></th>
                 <th width="" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                 <th width="" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                 <th width="" class=""><spring:message code="property.Amount" text="Amount"/></th>
            </tr>
            
            <c:forEach var="factorDetails" items="${command.getAuthCompFactDto()}" >
            <tr>
              <td>${factorDetails.proAssfFactorIdDesc}</td>                                                                                                          
              <td>${factorDetails.unitNoFact}</td>
              <td>${factorDetails.proAssfFactorValueDesc}</td>
              <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${factorDetails.factorDate}" /></td>
              <td>${factorDetails.factorRemark}</td>
              <td>${factorDetails.factorAmount}</td>
             </tr>				
			</c:forEach>
			 </tbody>
			</table>	
		</div>
 
  </c:if>
  
<!-------------------------------------------------------     Uploaded Document     ------------------------------------------------------->

                         <c:if test="${not empty command.documentList}">	
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
        
       
           </div>      
            </div>
            <div class="col-sm-6">
			<!-- Start Each Section -->
			
					<div class="accordion-toggle ">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyMaster"><spring:message
									code="property.propertyMaster" text="Property Master" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyMaster">
							<div class="form-group">
								<apptags:input labelCode="propertydetails.PropertyNo."
									path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
							</div>

							<c:if test="${command.getIntgrtionWithBP() eq 'Y'}">
								<div class="form-group">
									<apptags:input labelCode="property.buildingpermission" path=""
										isDisabled="true"></apptags:input>
									<apptags:input labelCode="propertydetails.PropertyNo."
										path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
								</div>

								<div class="form-group">
									<apptags:input labelCode="property.LandPermissionNo." path=""
										isDisabled="true"></apptags:input>
								</div>
							</c:if>


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
									path="authCompBeforeDto.assWardDesc1"
									isDisabled="true"></apptags:input>

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
									class="col-sm-2 control-label"><spring:message
										code="property.billingMethod" text="Billing Method" /> </label>
								<c:set var="baseLookupCodess" value="BLM" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCodess)}"
									path="provisionalAssesmentMstDto.billMethod"
									cssClass="form-control" hasChildLookup="false" hasId="true"
									showAll="false" selectOptionLabelCode="Select " />
							</div>
							
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.provisionalAssesmentMstDto.firstAssessmentDate}"
								var="firstAssessmentDate" />
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.provisionalAssesmentMstDto.lastAssessmentDate}"
								var="lastAssessmentDate" />
							<fmt:formatDate pattern="dd-MMM-yyyy"
								value="${command.provisionalAssesmentMstDto.reviseAssessmentDate}"
								var="reviseAssessmentDate" />
								
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
										code="propertydetails.lastAssessmentDate" text="Last Assessment Date"/></label>
								<div class="col-sm-4">
									<form:input
										path=""  value="${lastAssessDate}"
										type="text" id="lastAssessmentDate" class="form-control"
										disabled="true" />								
								</div>

							</div>
							<div class="form-group">
								<label for="constructPermissionNo" class="col-sm-2 control-label">
										<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control" 
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" disabled="true"/>
								</div>		
								<label for="constructPermissionDate" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.constructPermissionDate" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control" 
										path="provisionalAssesmentMstDto.constructPermissionDateFormat" id="constructPermissionDate" disabled="true" />
											</div>
									
							</div>
						
							<div class="form-group">
								<label for="permissionUseNo" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.permissionUseNo" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
											id="permissionUseNo" disabled="true" />
								</div>	
									
								<label for="yearOfConstruc0" class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
								<div class="col-xs-4 col-sm-4">
									<form:input	path="provisionalAssesmentMstDto.AssdYearConstructionDateFormat"  value=""
									type="text" id="yearOfConstruc0" class="form-control" disabled="true" />
								</div>

							</div>
							
							<div class="form-group">
								<apptags:input labelCode="property.propertytype"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc1"
									isDisabled="true"></apptags:input>
							</div>
							
						</div>
					</div>
       
<!-----------------------------------------Owner Details After (First owner will be the primary owner)  ------------------------------------->
           
            <div class="accordion-toggle ">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#OwnshipDetailAfter"><spring:message
				code="property.Ownershipdetail" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="OwnshipDetailAfter">
			
			<div class="form-group">
				<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
 				<form:hidden  path="ownershipPrefixBefore"  id="ownershipIdAfter" class="form-control"/>
 			
				</div>
		    </div>
		   
		    
		<c:choose>
		<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
		<div class="table-responsive">		
	 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
        <tbody>
        <tr>
                   
        <th width="25%"><spring:message code="ownersdetail.ownersname" /></th>
        <th width="20%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
		<th width="15%"><spring:message code="ownersdetail.mobileno" /></th>
		<th width="15%"><spring:message code="property.email" /></th>
		<th width="15%"><spring:message code="ownersdetail.adharno" /></th>
		<th width="15%"><spring:message code="ownersdetail.pancard" />                    	 
        </tr>
                		
      <tr>
		<td>
			<form:input id="assoOwnerName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
		</td>
		
		<td>
			<form:input id="assoOwnerNameReg" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/>
		</td>
									
	
		<td><form:input id="assoMobileno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
		</td>
		
		<td><form:input id="emailId_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
		</td>
									
		<td class="ownerDetails">
		<form:input id="assoAddharno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno" class="form-control "  disabled="true"/>
		</td>
		<td class="companyDetails">
		<form:input id="assoPanno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control"  disabled="true"/>
		</td>
	</tr>
	</tbody>
    </table>
    </div>
	</c:when>
		 	 	
		 <c:when test="${command.getOwnershipPrefix() eq 'JO'}">	
		 <c:set var="a" value='0'/>
		 		 <div class="table-responsive">
					<table id="jointOwnerTable" class="table text-left table-striped table-bordered">
						<tbody>
								<tr>
									<th width="25%"><spring:message code="ownersdetail.ownersname" /></th>
									<th width="20%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
									<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
									<th width="15%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="15%"><spring:message code="property.email" /></th>
									<th width="15%"><spring:message code="ownersdetail.adharno" /></th>
									<th width="15%"><spring:message code="ownersdetail.pancard" /></th>
								</tr>

						<c:forEach var="ownershipTypeList" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
            			<tr>
                		 <td>${ownershipTypeList.assoOwnerName}</td>
                		 <td>${ownershipTypeList.assoOwnerNameReg}</td>
                		 <td>${ownershipTypeList.propertyShare}</td>
                		 <td>${ownershipTypeList.assoMobileno}</td>
                		 <td>${ownershipTypeList.eMail}</td>					                		 
                		 <td>${ownershipTypeList.assoAddharno}</td>
                		 <td>${ownershipTypeList.assoPanno}</td>
            			</tr>
                		 </c:forEach>
						</tbody>
						
					</table>
					</div>
					</c:when>
				<c:otherwise>
				 <div class="table-responsive">
			<table id="companyDetailTable" class="table text-left table-striped table-bordered">
					<tbody>					
						<tr>
							<th width="25%"><spring:message code="property.NameOf" /> ${command.getOwnershipTypeValue()}</th>
							<th width="25%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
							<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
							<th width="15%"><spring:message code="property.email"/></th>
							<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						
 						<tr>
 						<td> 
 						<form:input id="assoOwnerName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
 						</td> 
						
						<td> 
 						<form:input id="assoOwnerNameReg_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/> 
 						</td> 
								
 						<td><form:input id="assoMobileno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
 						</td> 
						
						<td><form:input id="emailId_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
						</td>
						<td class="companyDetails"> 
						<form:input id="assoPanno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control" disabled="true" /> 
						</td> 
 						</tr> 								
					</tbody>
					</table>
					</div>				
					</c:otherwise>	
					</c:choose>
				 </div>
        
        	
<!----------------------------------------Property Address Details After------------------------------------------------------------------------------>
			
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#propertyAddressAfter"><spring:message
				code="property.Propertyaddress" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="propertyAddressAfter">
							<div class="form-group">
								<label for="address" class="col-sm-2 control-label "><spring:message
										code="property.propertyaddress" /></label>
								<div class="col-sm-4">
									<form:input path="provisionalAssesmentMstDto.assAddress"
										type="text" id="address" class="form-control" disabled="true" />
								</div>
								<label class="col-sm-2 control-label " for="areaName"><spring:message
										code="property.areaName" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="provisionalAssesmentMstDto.areaName" id="areaName"
										disabled="true"></form:input>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
										code="property.house.no" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.tppPlotNo" id="tppPlotNo"></form:input>
								</div>

								<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
										code="property.khata.no" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control"
										path="authCompBeforeDto.tppKhataNo" id="tppKhataNo"
										disabled="true"></form:input>
								</div>

							</div>
						
						<div class="form-group">
							<label for="location" class="col-sm-2 control-label "><spring:message
									code="property.surveyType" /></label>
							<div class="col-sm-4">
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

							<label class="col-sm-2 control-label " for="tppSurveyNumber"><spring:message
									code="property.khasra.no" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.tppSurveyNumber"
									id="tppSurveyNumber" disabled="true"></form:input>
							</div>
						</div>


						<div class="form-group">
							<label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message
									code="property.addiSurveyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiSurveyNo"
									id="addiSurveyNo" disabled="true"></form:input>
							</div>
							<label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message
									code="property.addiCityServeyNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.addiCityServeyNo"
									id="addiCityServeyNo" disabled="true"></form:input>
							</div>
						</div>

						<div class="form-group">
							
							<label class="col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" disabled="true"></form:input>
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
						
            </div>
                
        	
<!---------------------------------------------------Unit Details-------------------------------------------------------- -->
		
 			<h4 class="margin-top-10 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#UnitDetails"><spring:message code="property.unitdetails"/></a>
 			</h4>
		<div class="panel-collapse collapse in" id="UnitDetails">
		<div class="table-responsive">
			<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    
                        <tr>
 							<th width="7%"><spring:message code="unitdetails.year"/></th>
                        	<th width="4%"><spring:message code="unitdetails.unitno"/></th> 
                        	<th width="12%"><spring:message code="unitdetails.flatNo"/></th>	
                        	<th width="10%"><spring:message code="unitdetails.floorno"/></th>
                        	<th><spring:message code="unitdetails.occupancy"/></th>
                        	<th><spring:message code="unitdetails.OccupierName" /></th>
                        	<th><spring:message code="unitdetails.legal.illegal"/></th>
                        	                      	
                        	<th width="20%"><spring:message code="unitdetails.constructiontype"/></th>
                        	<th width="12%"><spring:message code="unitdetails.usagefactor"/></th>
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
                        	<th><spring:message	code="unitdetails.actualRent" text="Actual Annual Rent"/></th>
                        	<th><spring:message code="unitdetails.carpetArea" text="Assessable Area (Sq.Mtr)"/></th>
             	 			<th width="12%"><spring:message code="unitdetails.taxdetail" text="Assessed Area (Sq.Mtr)"/></th>
             	 			<th width="11%"><spring:message code="unitdetails.rate"/></th>
             	 			<c:if test="${command.getAssMethod() eq 'ARV'}">
             	 			<th width="11%"><spring:message code="unitdetails.ARV"/></th>
             	 			<th width="11%"><spring:message code="unitdetails.deductionCharge"/></th>
							<th width="12%"><spring:message code="unitdetails.RV"/></th>
							</c:if>
							<c:if test="${command.getAssMethod() eq 'CV'}">
							<th width="10%"><spring:message code="unitdetails.CV"/></th>
							</c:if>
             	 			<%-- <th width="6%"><spring:message code="unit.ViewMoreDetails"/></th> --%>
                        </tr>
                        
                     <tbody>    
                        <c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                       	<tr>
                        <td class="text-center">${unitDetails.proFaYearIdDesc}</td>                                                                                                          
                        <td class="text-center">${unitDetails.assdUnitNo}</td>
                        <td class="text-center">${unitDetails.flatNo}</td>	
                        <td class="text-center">${unitDetails.proFloorNo} </td>
                        <td class="text-center" ><c:set var="baseLookupCode" value="OCS" /> 
				 		<apptags:lookupField items="${command.getLevelData(baseLookupCode)}" 
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control stylenone changeParameterClass " 
								hasChildLookup="false" hasId="true" showAll="false" disabled="true"
				 				selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" />
				 		</td>
				 		<td class="text-center"><form:input
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
								type="text" class="form-control stylenone" id="occupierName" disabled="true" />
						</td>                        
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
	                        
                    	<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
										type="text"
										class="form-control has2Decimal mandColorClass text-center stylenone"
										id="actualRent${status.count-1}"
										data-rule-required="true"
										onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" placeholder="0.0"/> 
						</td>
	                        
	                    <fmt:formatNumber type="number" value="${unitDetails.carpetArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="carpetArea"/>
	                    <td class="text-center">${carpetArea}</td>
	                    <fmt:formatNumber type="number" value="${unitDetails.assdBuildupArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="assdBuildupArea"/>                        
	                    <td class="text-center">${assdBuildupArea}</td>
                    	
                    	<td class="text-center">${unitDetails.assdStdRate} </td>
                    	<c:if test="${command.getAssMethod() eq 'ARV'}">
                    	<td class="text-center">${unitDetails.assdAlv} </td>
                    	<td class="text-center">${unitDetails.maintainceCharge} </td>
                        <td class="text-center">${unitDetails.assdRv}</td>
                        </c:if>
                        <c:if test="${command.getAssMethod() eq 'CV'}">
                    	<td class="text-center">${unitDetails.assdCv}</td>
                    	</c:if>
                    	<%-- <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-after-${status.count-1}" aria-expanded="false" aria-controls="group-of-rows-after-${status.count-1}"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td> --%>	                           
    					</tr>
            
           </c:forEach>
           </tbody>
</table></div>
               </div>
              
               
<!------------------------------------------------------------ unitSpecific Additional Info After ------------------------------------------------------>
<c:if test="${not empty command.provAsseFactDtlDto}"> 
	         
		<h4 class="margin-top-10 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#unitSpecificInfoAfter"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 		</h4>
		<div class="panel-collapse collapse in" id="unitSpecificInfoAfter">      
 			
 			<table id="unitSpecificInfoTable" class="table table-striped table-bordered ">
            <tbody>
            <tr>
            	 <th width="20%"><spring:message code="unit.Factor"/></th>
                 <th width="10%"><spring:message code="unit.ApplicableUnitNo"/></th>
                 <th width="30%"><spring:message code="unit.FactorValue"/></th>
                 <th width="" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                 <th width="" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                 <th width="" class=""><spring:message code="property.Amount" text="Amount"/></th>
            </tr>
            
            <c:forEach var="factorDetails" items="${command.getProvAsseFactDtlDto()}" >
            <tr>
              <td>${factorDetails.proAssfFactorIdDesc}</td>                                                                                                          
              <td>${factorDetails.unitNoFact}</td>
              <td>${factorDetails.proAssfFactorValueDesc}</td>
              <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${factorDetails.factorDate}" /></td>
              <td>${factorDetails.factorRemark}</td>
              <td>${factorDetails.factorAmount}</td>
             </tr>				
			</c:forEach>
			 </tbody>
			</table>	
		</div>
 
  </c:if>
  
<!--------------------------------------------------------------Uploaded Document--------------------------------------------------->  
                         <c:if test="${not empty command.documentList}">	
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
           </div>      
            </div>
          </div>
  <c:if test="${command.getAssType() eq 'A' && command.approvalFlag ne 'APR'}">  
  <div class="accordion-toggle ">        
<apptags:CheckerAction hideSendback="true"></apptags:CheckerAction>
</div>
</c:if>
			<div class="text-center padding-top-10">
			<c:set var="assType" value="${command.getAssType()}"></c:set>
			              			<button type="button" class="btn btn-success btn-submit"
										onclick="saveAuthorizationWithEdit(this,'${assType}')" id="submit">
										<spring:message code="property.SaveProperty"/>
									</button>
									<c:if test="${command.getSelfAss() ne 'BIF'}">
			              			<button class="btn btn-blue-2" type="button"  onclick="backToMainPage(this)" id="submit">
													<spring:message code="property.Back" text="Back"/></button> 
									</c:if>
									<c:if test="${command.getSelfAss() eq 'BIF'}">
									<button class="btn btn-blue-2" type="button"  onclick="editSelfAssBifurcationForm(this)" id="edit">
										<spring:message code="property.Back" text="Back"/></button> 
									</c:if>
			</div>  
          </form:form>
		  </div>
        </div>
