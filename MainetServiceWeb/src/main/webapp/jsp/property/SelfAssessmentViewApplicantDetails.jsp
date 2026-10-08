<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- End JSP Necessary Tags -->
 <script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/property/unitDetails.js"></script>
<script type="text/javascript" src="js/property/selfAssessment.js"></script>

<!-- Start Content here -->
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

@media print {
	@page {
		size: A4 landscape;
		margin: 0.5cm;
	}
	#btnPrint {
		display: none;
	}
	#removePrint {
		display: none;
	}
}

</style>

<c:if test="${command.getAssType() eq 'N' || command.getAssType() eq 'BIF'}">  
<apptags:breadcrumb></apptags:breadcrumb><br>
</c:if>

  <c:if test="${command.getAssType() eq 'A'}"> <%-- This breadcrumb is active only from dashboard view click --%>
<!-- PAGE BREADCRUM SECTION  -->
	<ol class="breadcrumb">
		<li><a href="AdminHome.html"><spring:message code="cfc.home" text="Home"/></a></li>
		<li><a href="javascript:void(0);"><spring:message code="property.property" text="Property tax"/></a></li>
		<li><a href="javascript:void(0);"><spring:message code="cfc.transaction" text="Transactions"/></a></li>
		<li class="active"><spring:message text="Property View" code="propety.Authorization" /></li>
	</ol><br>
 </c:if> 
<c:if test="${command.getAssType() eq 'A' || command.getAssType() eq 'N'}   ">  
<div class="content">
</c:if>

<div class="AuthContent">
<div class="widget">
       <div class="widget-header">

			<c:if test="${command.noOfDaysAuthEditFlag eq 'Y'}">
				<h3 align="center">This application is under period of editable
					form. This application cannot be approved</h3>
			</c:if>
			<c:if test="${command.getAssType() eq 'N'}">  
			<h2><strong><spring:message code="propertyView.SelfAssesmentView" /></strong></h2>	
		</c:if> 
		<c:if test="${command.getAssType() eq 'C'}">  			
			<h2><strong><spring:message code="property.ChangeInAssessmentView" text="View Page"/></strong></h2>			
		</c:if>
			
		<c:if test="${command.getAssType() eq 'NC'}">  
			<h2><strong><spring:message code="property.NoChangeInAssessmentView" text="No Change in Assessment View"/></strong></h2>
		</c:if>
		
		<c:if test="${command.getAssType() eq 'A'}">  
			<h2><strong><spring:message code="property.AuthorizationView" text="Authorization View"/></strong></h2>
		</c:if>
		
		<c:if test="${command.getAssType() eq 'BIF'}">  
			<h2><strong><spring:message code="property.BifurcationView" text="Bifurcation View"/></strong></h2>
		</c:if>
			<apptags:helpDoc url="SelfAssessmentForm.html"></apptags:helpDoc>	
		</div>	
		
        <div class="widget-content padding">
        <div id="printapplication">
        <div class="self-assessment">
		<form:form action="SelfAssessmentForm.html" class="form-horizontal form" name="frmSelfAssessmentForm" id="frmSelfAssessmentForm">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<form:hidden path="draftFlag" id="draftFlag"/>
			<form:hidden path="currentCheckerLevel" id="currentCheckerLevel"/>
			
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			
			<c:if test="${command.getIntgrtionWithBP() eq 'Y'}">
			<div class="form-group">
				<apptags:input labelCode="property.buildingpermission" path="" isDisabled="true"></apptags:input> 
				<apptags:input labelCode="property.LandPermissionNo." path="" isDisabled="true"></apptags:input>
				
			</div>
			</c:if>
			
			<div class="accordion-toggle ">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyMaster"><spring:message
									code="property.propertyMaster" text="Property Master" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyMaster">
						<c:if test="${'CIA' eq command.getServiceShortCode() }">
							<div class="form-group">
							<apptags:radio 
								radioLabel="Yes,No" radioValue="Y,N"
								labelCode="property.auto.adj.flag" disabled="true"
								path="viewApplicantAssMstDto.autoAdjustmentFlag"></apptags:radio>
						</div>
						</c:if>
						<div class="form-group propInfo">
							<c:if test="${not empty command.viewApplicantAssMstDto.assNo}">
								<apptags:input labelCode="propertydetails.PropertyNo." path="viewApplicantAssMstDto.assNo" isDisabled="true"></apptags:input>
							</c:if>
							<c:if test="${not empty command.viewApplicantAssMstDto.remarks}">
								<apptags:textArea labelCode="property.remark" path="viewApplicantAssMstDto.remarks" isDisabled="true"></apptags:textArea>
							</c:if>		
						</div>
			
     <c:choose>
	 <c:when test="${command.getLandTypePrefix() eq 'KPK'}">
		<div class="form-group">		
			<apptags:input labelCode="property.district" path="viewApplicantAssMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="viewApplicantAssMstDto.assTahasilDesc" isDisabled="true"></apptags:input>
		</div>
		<div class="form-group">						
 			<apptags:input labelCode="propertydetails.village" path="viewApplicantAssMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
			<label class="col-xs-2 col-sm-2 control-label"
									for="khasra"><spring:message code="propertydetails.csnno" text="Khasra No"/></label>
				<div class="col-xs-4 col-sm-4">									
				<form:input path="viewApplicantAssMstDto.tppPlotNoCs" type="text" class="form-control mandColorClass" id="displayEnteredKhaNo" />
				</div>
		</div>
		
		<div class="form-group">
		<div class="text-center padding-top-10">
		<c:if test="${command.getAssType() eq 'A' || command.getAssType() eq 'NC'}">  
		<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
				<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
		</c:if>	
		
		</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
		
	<c:if test="${command.getAssType() ne 'A' && command.getAssType() ne 'NC'}">  

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
			<apptags:input labelCode="property.district" path="viewApplicantAssMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="viewApplicantAssMstDto.assTahasilDesc" isDisabled="true"></apptags:input>		
		</div>
			
		<div class="form-group">			
				<apptags:input labelCode="propertydetails.village" path="viewApplicantAssMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
				<apptags:input labelCode="Mohalla" path="viewApplicantAssMstDto.mohallaDesc" isDisabled="true"></apptags:input> 								
		</div>	
		
		<div class="form-group">
				<apptags:input labelCode="propertydetails.streetno" path="viewApplicantAssMstDto.assStreetNoDesc" isDisabled="true"></apptags:input> 												
				<apptags:input labelCode="propertydetails.plotno" path="viewApplicantAssMstDto.tppPlotNo"></apptags:input>
		</div>		
		
		<div class="form-group">
			<div class="text-center padding-top-10">
			<c:if test="${command.getAssType() eq 'A' || command.getAssType() eq 'NC'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
					<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
			</c:if>	
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
	<c:if test="${command.getAssType() ne 'A' && command.getAssType() ne 'NC'}">  

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
			<apptags:input labelCode="property.district" path="viewApplicantAssMstDto.assDistrictDesc" isDisabled="true"></apptags:input>			
			<apptags:input labelCode="property.tahasil" path="viewApplicantAssMstDto.assTahasilDesc" isDisabled="true"></apptags:input>		
		</div>
			
		<div class="form-group">			
			<apptags:input labelCode="propertydetails.village" path="viewApplicantAssMstDto.tppVillageMaujaDesc" isDisabled="true"></apptags:input> 								
			<apptags:input labelCode="Mohalla" path="viewApplicantAssMstDto.mohallaDesc" isDisabled="true"></apptags:input> 								
		</div>	
		
		<div class="form-group">
			<apptags:input labelCode="propertydetails.streetno" path="viewApplicantAssMstDto.assStreetNoDesc" isDisabled="true"></apptags:input> 												
			<apptags:input labelCode="propertydetails.plotno" path="viewApplicantAssMstDto.tppPlotNo"></apptags:input>
		</div>	
		
		<div class="form-group">
			<div class="text-center padding-top-10">
			<c:if test="${command.getAssType() eq 'A' || command.getAssType() eq 'NC'}">  
			<button class="btn btn-blue-2" type="button"  onclick="getLandApiDetails(this)" id="getApiDetails">
					<spring:message code="property.fetchLandTypeDetails.button" text="Fetch land details"/></button> 
			</c:if>	
		
			</div>
		</div>
		<div id="showAuthApiDetails">
					
		</div>
	<c:if test="${command.getAssType() ne 'A' && command.getAssType() ne 'NC'}">	

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

				<div class="form-group propInfo">
					<apptags:input cssClass="alphaNumeric preventSpace"
						labelCode="propertydetails.refPropNo"
						path="viewApplicantAssMstDto.refPropNo" maxlegnth="40"
						isDisabled="true"></apptags:input>

					<apptags:input cssClass="alphaNumeric preventSpace"
						labelCode="propertydetails.gisRefNo"
						path="viewApplicantAssMstDto.assGisId" maxlegnth="40"
						isDisabled="true"></apptags:input>

				</div>	
		
				<div class="form-group propInfo">
			
				<apptags:input labelCode="property.propertyWard" path="viewApplicantAssMstDto.assWardDesc1" isDisabled="true"></apptags:input>
				
				<c:if test="${command.viewApplicantAssMstDto.assWard2 ne null}">			
				<apptags:input labelCode="property.propertyZone" path="viewApplicantAssMstDto.assWardDesc2" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group propInfo">
				<c:if test="${command.viewApplicantAssMstDto.assWard3 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="viewApplicantAssMstDto.assWardDesc3" isDisabled="true"></apptags:input>
				</c:if>
				<c:if test="${command.viewApplicantAssMstDto.assWard4 ne null}">
				<apptags:input labelCode="property.propertyWard" path="viewApplicantAssMstDto.assWardDesc4" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group propInfo">
				<c:if test="${command.viewApplicantAssMstDto.assWard5 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="viewApplicantAssMstDto.assWardDesc5" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				
						<div class="form-group propInfo">
							<apptags:input labelCode="unitdetails.RoadType" path="viewApplicantAssMstDto.proAssdRoadfactorDesc" 
							isDisabled="true"></apptags:input>
							
							<label for="surveyType"
								class="col-xs-2 col-sm-2 control-label required-control"><spring:message
									code="property.billingMethod" text="Billing Method" /> </label>
							<c:set var="baseLookupCodess" value="BLM" />
							<apptags:lookupField
								items="${command.getLevelData(baseLookupCodess)}"
								path="viewApplicantAssMstDto.billMethod"
								cssClass="form-control" hasChildLookup="false" hasId="true"
								showAll="false" selectOptionLabelCode="Select " disabled="true"/>

						</div>

						<div class="form-group">

							<label for="proAssAcqDate" class="col-xs-2 col-sm-2 control-label"><spring:message
									code="propertydetails.firstAssessmentDate" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input
									path="viewApplicantAssMstDto.proAssAcqDateFormat"
									type="text" id="proAssAcqDate" class="form-control"
									disabled="true" />
							</div>

							 <label for="lastAssessmentDate" class="col-xs-2 col-sm-2 control-label"><spring:message
										code="propertydetails.lastAssessmentDate" text="Last Assessment Date"/></label>
								<div class="col-xs-4 col-sm-4">
									<form:input
										path="viewApplicantAssMstDto.lastAssessmentDateFormat"  value=""
										type="text" id="lastAssessmentDate" class="form-control"
										disabled="true" />				
								</div>

						</div>

						<fmt:formatDate pattern="dd-MMM-yyyy"
							value="${command.viewApplicantAssMstDto.lastAssessmentDate}"
							var="lastAssessDate" />
						<fmt:formatDate pattern="dd-MMM-yyyy"
							value="${command.viewApplicantAssMstDto.reviseAssessmentDate}"
							var="reviseAssessDate" />
					
							<div class="form-group">
							
							<label for="constructPermissionNo" class="col-sm-2 control-label">
											<spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
											path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo" />
											</div>		
							<label for="constructPermissionDate" class="col-sm-2 control-label">
										<spring:message	code="unitdetails.constructPermissionDate" /></label>
										<div class="col-sm-4">
											<form:input type="text" class="form-control" 
											 path="viewApplicantAssMstDto.constructPermissionDateFormat"
											 id="constructPermissionDate"  disabled="true" />
										</div>
						
							</div>
							<div class="form-group">
									<label for="permissionUseNo" class="col-sm-2 control-label">
											<spring:message	code="unitdetails.permissionUseNo" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control" 
												path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" 
												id="permissionUseNo" disabled="true" />
											</div>	
									
									<label for="yearOfConstruc0" class="col-sm-2 control-label "><spring:message
											code="unitdetails.ConstcompletionDate" /></label>
									<div class="col-xs-4 col-sm-4">
								<form:input
									path="viewApplicantAssMstDto.AssdYearConstructionDateFormat"  value=""
									type="text" id="yearOfConstruc0" class="form-control"
									disabled="true" />
							</div>
						
					</div>

						<div class="form-group">
							<apptags:input labelCode="property.propertytype"
								path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfpropertyDesc1"
								isDisabled="true"></apptags:input>
							<form:hidden path="landTypePrefix" class="form-control landValue" />
							<form:hidden class="form-control" id="khasraNo" path="knownKhaNo" />

						</div>
						
			</div>
<!---------------------------------------- Owner Details (First owner will be the primary owner)-------------------------------->
           
            <div class="accordion-toggle">
            <h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
				code="property.Ownershipdetail" /></a>
			</h4>
     
	<div class="panel-collapse collapse in" id="OwnshipDetail">
			
			<div class="form-group">
				<label class="col-xs-2 col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
				<div class="col-xs-4 col-sm-4"><form:input path="viewApplicantAssMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
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
			<form:input id="assoOwnerName" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
		</td>
		
		<td>
			<form:input id="assoMobileno" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
		</td>
		
		<td><form:input id="emailId" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control preventSpace" disabled="true"/>   
		</td>
									
		<td class="ownerDetails">
			<form:input id="assoAddharno" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno" class="form-control "  disabled="true"/>
		</td>
		<td class="companyDetails">
			<form:input id="assoPanno" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control"  disabled="true"/>
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
									<th width="8%" ><spring:message code="ownerdetails.relation" /></th>
									<th width="17%"><spring:message code="ownersdetails.GuardianName" /></th> --%>
									<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
									<th width="11%"><spring:message code="ownersdetail.mobileno" /></th>
									<th width="10%"><spring:message code="property.email" /></th>
									<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
									<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
								</tr>

						<c:forEach var="ownershipTypeList" items="${command.getviewApplicantAssMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
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
 						<form:input id="assoOwnerName_${d}" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
 						</td> 
 						
 						<td><form:input id="assoMobileno_${d}" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
 						</td> 
 						
 						<td><form:input id="emailId_${d}"
										path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control preventSpace" disabled="true"/>   
						</td>
								
						<td class="companyDetails"> 
						<form:input id="assoPanno_${d}" path="viewApplicantAssMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control" disabled="true" /> 
						</td> 
 						</tr> 								
					</tbody>
					</table>				
					</c:otherwise>	
					</c:choose>
				
         </div>
				
            	                    	
<!-- --------------------------------------------------- Property Address Details--------------------------------------------------------------->
			
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
			<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="propertyAddress">
						<div class="form-group">
							<label for="address" class="col-xs-2 col-sm-2 control-label "><spring:message
									code="property.propertyaddress" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input path="viewApplicantAssMstDto.assAddress"
									type="text" id="address" class="form-control" disabled="true" />
							</div>
								<label class="col-xs-2 col-sm-2 control-label " for="areaName"><spring:message
									code="property.areaName" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.areaName" id="areaName"
									disabled="true"></form:input>
							</div>
						
						</div>

						<div class="form-group">
							<label class="col-xs-2 col-sm-2 control-label " for="tppPlotNo"><spring:message
									code="property.house.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.tppPlotNo" id="tppPlotNo" disabled="true"></form:input>
							</div>
							
							<label class="col-xs-2 col-sm-2 control-label " for="tppKhataNo"><spring:message
									code="property.khata.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.tppKhataNo" id="tppKhataNo" disabled="true"></form:input>
							</div>
							
						</div>
						
						<div class="form-group">
							<label for="location" class="col-xs-2 col-sm-2 control-label "><spring:message
									code="property.surveyType" /></label>
							<div class="col-xs-4 col-sm-4">
								<c:choose>
									<c:when test="${command.viewApplicantAssMstDto.surveyType eq '0'}">								
										<form:input path=""
											type="text" id="surveyType" class="form-control"
											disabled="true" />
									</c:when>
									<c:otherwise>								
										<form:input path="viewApplicantAssMstDto.surveyTypeDesc"
											type="text" id="surveyType" class="form-control"
											disabled="true" />
									</c:otherwise>
								</c:choose>
								
							</div>							

							<label class="col-xs-2 col-sm-2 control-label " for="tppSurveyNumber"><spring:message
									code="property.khasra.no" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.tppSurveyNumber"
									id="tppSurveyNumber" disabled="true"></form:input>
							</div>
						</div>


						<div class="form-group">
							<label class="col-xs-2 col-sm-2 control-label " for="addiSurveyNo"><spring:message
									code="property.addiSurveyNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.addiSurveyNo"
									id="addiSurveyNo" disabled="true"></form:input>
							</div>
							<label class="col-xs-2 col-sm-2 control-label " for="addiCityServeyNo"><spring:message
									code="property.addiCityServeyNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.addiCityServeyNo"
									id="addiCityServeyNo" disabled="true"></form:input>
							</div>
						</div>

						<div class="form-group">
							
							<label class="col-xs-2 col-sm-2 control-label " for="aaliNo"><spring:message
									code="property.aaliNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.aaliNo" id="aaliNo" disabled="true"></form:input>
							</div>
							
							<label class="col-xs-2 col-sm-2 control-label " for="chaaltaNo"><spring:message
									code="property.chaaltaNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control"
									path="viewApplicantAssMstDto.chaaltaNo" id="chaaltaNo" disabled="true"></form:input>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 col-sm-2 control-label " for="clusterNo"><spring:message
									code="property.clusterNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="viewApplicantAssMstDto.clusterNo" id="clusterNo" disabled="true"></form:input>
							</div>

							<label class="col-xs-2 col-sm-2 control-label " for="buildingNo"><spring:message
									code="property.buildingNo" /></label>
							<div class="col-xs-4 col-sm-4">
								<form:input type="text" class="form-control hasNumber"
									path="viewApplicantAssMstDto.buildingNo" id="buildingNo" disabled="true"></form:input>
							</div>
						</div>
						
          </div>
        	
<!-- --------------------------------------------------- Property Address Details--------------------------------------------------------------->
<c:if test="${command.viewApplicantAssMstDto.billMethod eq '100000330'}">
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
					                                <form:input path="viewApplicantAssMstDto.electricBillUnitNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                            <td>
					                                <form:input path="viewApplicantAssMstDto.electricConsumerNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                            <td>
					                                <form:input path="viewApplicantAssMstDto.electricMeterNo" type="text" class="form-control text-center" disabled="true"/>
					                            </td>
					                        </tr>
					                </tbody>
					            </table>
					        </div>
					    </div>
					</div>
		</c:if>
<!-- ---------------------------------------------Unit Details --------------------------------------------------------------------------->
		
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
                        	<c:if test="${command.viewApplicantAssMstDto.billMethod eq '100000332'}">
                        	<th class="electricDetailsColumn"><spring:message text="Electricity Billing Unit Number"/></th>
				            <th class="electricDetailsColumn"><spring:message text="Electricity Consumer Number"/></th>
				            <th class="electricDetailsColumn"><spring:message text="Electricity Meter Number"/></th>
				            </c:if>
							<th><spring:message code="unitdetails.occupancy"/></th>		
				 			<th><spring:message	code="unitdetails.OccupierName" /></th>
                        	<th><spring:message code="unitdetails.legal.illegal"/></th>
                        	<th><spring:message code="unitdetails.user.charges" text="User Charges Category"/></th>
                        	<th><spring:message code="unitdetails.constructiontype"/></th>
                        	<th><spring:message code="unitdetails.usagefactor"/></th>
                        	
                        	<c:if test="${command.viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype2 ne null }">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype3 ne null}">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype4 ne null}">
                        	<th><spring:message code="property.Usagesubtype" text="Usage sub type"/></th>   
                        	</c:if>
                        	
                        	<c:if test="${command.viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype5 ne null}">
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
							<c:if test="${command.getServiceShortCode() eq 'CIA'  && command.getAssType() ne 'A'  && command.draftApplication ne 'Y'}">
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
                        
                         
                  <c:forEach var="unitDetails" items="${command.getViewApplicantAssMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                       	<tr>
	                        <td class="text-center">${unitDetails.proFaYearIdDesc}</td>                                                                                                          
	                        <td class="text-center">${unitDetails.assdUnitNo}</td>
							<td class="text-center">${unitDetails.flatNo}</td>	
	                        <td class="text-center">${unitDetails.proFloorNo} </td>
	                        <c:if test="${command.viewApplicantAssMstDto.billMethod eq '100000332'}">
	                        <td class="text-center">${unitDetails.electricBillUnitNo}</td>
				            <td class="text-center">${unitDetails.electricConsumerNo}</td>
				            <td class="text-center">${unitDetails.electricMeterNo}</td>
	                        </c:if>
	                      
	                         <td class="text-center" ><c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control stylenone changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false" disabled="true"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/></td>
	                        
	                         <td class="text-center"><form:input
															path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
															type="text" class="form-control stylenone" id="occupierName"
															disabled="true" /></td>
	                   
                            <td class="text-center">${unitDetails.legal}</td>
                            <td class="text-center">${unitDetails.userChargesTypeDesc}</td>
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
	                        <td><form:input
															path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
															type="text"
															class="form-control has2Decimal mandColorClass text-center stylenone"
															id="actualRent${status.count-1}"
															data-rule-required="true"
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
	                        <c:if test="${command.getServiceShortCode() eq 'CIA' && command.getAssType() ne 'A' && command.draftApplication ne 'Y'}">
	                        	<td class="text-center">${unitDetails.areaOrRvBasedChange} </td>
	                        </c:if>
	                        <c:if test="${command.getServiceShortCode() eq 'BIF'}">
	                        	<td class="text-center">${unitDetails.areaOrRvBasedChange} </td>
	                        </c:if>
	                        </c:if>
	                        <c:if test="${command.getAssMethod() eq 'CV'}">
	                    	<td class="text-center">${unitDetails.assdCv}</td>
	                    	</c:if>
	                    	<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="viewApplicantRoomDetails(${status.count-1},this)" value="<spring:message code="property.room" text="Room" />"></td>
    					</tr>
				     
										<c:set var = "hiddenOccupancyTypeDesc" value = "${unitDetails.getProAssdOccupancyTypeDesc()}"/>										
										<c:if test="${fn:contains(hiddenOccupancyTypeDesc, 'Ten')}">
												<div class="form-group">
													<label for="actualRent${status.count-1}"
														class="col-xs-2 col-sm-2 control-label"><spring:message
															code="unitdetails.actualRent" /></label>
													<div class="col-xs-4 col-sm-4">
														<form:input
															path="viewApplicantAssMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
															type="text"
															class="form-control has2Decimal mandColorClass text-left stylenone"
															id="actualRent${status.count-1}"
															data-rule-required="true"
															onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" />
													</div>
												</div>
										</c:if>
				
								</div>
				         	</td>
				         </tr>
                   </c:forEach>

               </tbody>						
               		<tfoot>
               			<c:if test="${command.viewApplicantAssMstDto.totalFloorCountResidential gt 0}">
					          <tr>
					              <td colspan="2"><spring:message code="unitdetails.totalResidentila"/></td>
					              <td colspan="1" class="text-center">${command.viewApplicantAssMstDto.totalFloorCountResidential}</td> 
					              <td colspan="10"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalBuildupAreaResidential}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildupAreaResidential"/>
					              <td colspan="1" class="text-center">${totalBuildupAreaResidential}</td> 
					              <td colspan="3"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalRvResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvResidential"/>
					              <td colspan="1" class="text-center">${totalRvResidential}</td> 
					          </tr>
					      </c:if>
					      <c:if test="${command.viewApplicantAssMstDto.totalFloorCountNonResidential gt 0}">    
					           <tr>
					              <td colspan="2"><spring:message code="unitdetails.totalNonResidential"/></td>
					              <td colspan="1" class="text-center">${command.viewApplicantAssMstDto.totalFloorCountNonResidential}</td> 
					              <td colspan="10"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalBuildupAreaNonResidential}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildupAreaNonResidential"/>
					              <td colspan="1" class="text-center">${totalBuildupAreaNonResidential}</td> 
					              <td colspan="3"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalRvNonResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvNonResidential"/>
					              <td colspan="1" class="text-center">${totalRvNonResidential}</td> 
					          </tr>
					      </c:if>
					      <c:if test="${command.viewApplicantAssMstDto.totalFlatCount gt 0}">    
					           <tr>
					              <td colspan="2"><spring:message	code="propertyTax.Total" text="Total"/></td>
					              <td colspan="1" class="text-center">${command.viewApplicantAssMstDto.totalFlatCount}</td> 
					              <td colspan="10"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalBuildUpArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="totalBuildUpArea"/>
					              <td colspan="1" class="text-center">${totalBuildUpArea}</td> 
					              <td colspan="3"></td>
					              <fmt:formatNumber type="number" value="${command.viewApplicantAssMstDto.totalRv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRv"/>
					              <td colspan="1" class="text-center">${totalRv}</td> 
					          </tr>
					      </c:if>     
					      </tfoot>
			</table>
      </div>
              
               
<!-- unitSpecific Additional Info -->
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
  
   <div id="removePrint">    
            
            <div class="panel-collapse collapse in" id="Payment">      
            <div class="form-group hidden-print">
            </div>
            </div>
				<c:if test="${not empty command.getAssesmentManualDate()}"> 
					<h4 class="margin-top-10 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#manualReceipt"> Manual
							Receipt Details</a>
					</h4>
					<div class="panel-collapse collapse in" id="manualReceipt">
						<div class="form-group hidden-print">
							<div class="form-group">
								<apptags:input labelCode="Manual Receipt No" path="assesmentManualNo"></apptags:input>
								<apptags:input labelCode="Manual Receipt Date"
									path="assesmentManualDate" isReadonly="true"
									cssClass="trimDateTime"></apptags:input>
							</div>
							<div class="form-group">
								<label for="partialAmt" class="col-xs-2 col-sm-2 control-label"><spring:message
										code="" text="Manual Receipt Amount"/></label>
								<div class="col-xs-4 col-sm-4">
									<form:input path="viewApplicantAssMstDto.billPartialAmt"
										type="text" id="partialAmt" class="form-control hasNumber" />
								</div>
							</div>
						</div>
					</div>
				</c:if>
		
					
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
															<apptags:filedownload filename="${lookUp.attFname}" filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}" actionUrl="SelfAssessmentForm.html?previewDownload"></apptags:filedownload>
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
        <!-- Start button -->

      <div class="text-center padding-top-10 hidden-print">

			<input type='button' class="btn btn-danger" id='btnPrint' value="<spring:message code="property.noDues.Print" text="Print"/>" onclick="printAppliactionDiv('printapplication')" />
			
			<button type="button" class="btn btn-blue-2" onclick="window.close();">
				<spring:message code="property.report.close" text="Close" />
			</button>			
	   </div>
	
 </form:form> 
 </div> 
 </div>
		</div>
		</div>
 </div>
 	</div>
<c:if test="${command.getAssType() eq 'A'}">  
</div>
</c:if>

<script>
	function printAppliactionDiv(divName) {
		var printContents = document.getElementById(divName).innerHTML;
		var originalContents = document.body.innerHTML;
		document.body.innerHTML = printContents;
		window.print();
		document.body.innerHTML = originalContents;
	}
</script>
