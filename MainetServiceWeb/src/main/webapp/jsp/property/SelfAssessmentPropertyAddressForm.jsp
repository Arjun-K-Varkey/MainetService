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
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- End JSP Necessary Tags -->

<script src="js/property/propertyAddressDetails.js" type="text/javascript"></script>
<script>
$( document ).ready(function() {
	   
	   
	   var langFlag = getLocalMessage('admin.lang.translator.flag');
		if(langFlag ==='Y'){
			$("#assAddress").keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#assAddress").val().trim();
				if(no_spl_char!='')
				{
					commonlanguageTranslate(no_spl_char,'assAddressReg',event,'');
				}else{
					$("#assAddressReg").val('');
				}
			});
			$("#areaName").keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#areaName").val().trim();
				if(no_spl_char!='')
				{
					commonlanguageTranslate(no_spl_char,'areaNameReg',event,'');
				}else{
					$("#areaNameReg").val('');
				}
			});
			$("#occupierName0,#occupierName").keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#occupierName0,#occupierName").val().trim();
				if(no_spl_char!='')
				{
					commonlanguageTranslate(no_spl_char,'occupierNameReg0',event,'');
					commonlanguageTranslate(no_spl_char,'occupierNameReg',event,'');
				}else{
					$("#occupierNameReg0").val('');
					$("#occupierNameReg").val('');
				}
			});
		}
}); 
</script>

<style>
  #hideBillingDetails{display:none;}  
</style>


<div class="accordion-toggle">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="propertyAddress">
		<div class="form-group">		
			<apptags:textArea isMandatory="true" labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress" maxlegnth="500" cssClass="preventSpace"></apptags:textArea>
		 	<%-- <apptags:textArea labelCode="property.propertyAddressReg" path="provisionalAssesmentMstDto.assAddressReg" maxlegnth="500" cssClass="preventSpace"></apptags:textArea>								 --%>
		     <%-- <label class="col-sm-2 control-label " for="addiCityServeyNo"><spring:message
					code="property.addiCityServeyNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.addiCityServeyNo"
					id="addiCityServeyNo" maxlength="20"></form:input>
			</div> --%>
			<label class="col-sm-2 control-label " for="aaliNo"><spring:message
					code="property.aaliNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" maxlength="20"></form:input>
			</div>
		
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label required-control" for="areaName"><spring:message
					code="property.areaName" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.areaName" id="areaName"></form:input>
			</div>


			<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message
					code="property.chaaltaNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo" maxlength="20"></form:input>
			</div>
			<%-- <apptags:textArea labelCode="property.areaNameReg"
				path="provisionalAssesmentMstDto.areaNameReg" maxlegnth="100"
				cssClass="preventSpace"></apptags:textArea> --%>

			<%-- <label class="col-sm-2 control-label " for="areaNameReg"><spring:message
					code="property.areaNameReg" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.areaNameReg" id="areaNameReg"></form:input>
			</div> --%>
		</div>
				
		<%-- <div class="form-group">
			<apptags:select cssClass="chosen-select-no-results"
				labelCode="property.location" items="${command.location}"
				path="provisionalAssesmentMstDto.locId" isMandatory="true"
				isLookUpItem="true" selectOptionLabelCode="property.selectLocation">
			</apptags:select>
			
			<label class="col-sm-2 control-label " for="proAssPincode"><spring:message
					code="property.pincode" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control hasPincode"
					path="provisionalAssesmentMstDto.assPincode" id="assPincode"
					maxlength="6" data-rule-digits="true" data-rule-minlength="6"
					data-rule-maxlength="6"></form:input>
			</div>
		</div> --%>
			<div class="form-group">


			<label class="col-sm-2 control-label" for="clusterNo"><spring:message
					code="property.clusterNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control hasNumber"
					path="provisionalAssesmentMstDto.clusterNo" id="clusterNo" maxlength="20"></form:input>
			</div>
			<label class="col-sm-2 control-label " for="buildingNo"><spring:message
					code="property.buildingNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control hasNumber"
					path="provisionalAssesmentMstDto.buildingNo" id="buildingNo" maxlength="20"></form:input>
			</div>
		</div>
		
		
		<div id="wardZone">		
		</div>		

		<div class="form-group">
				<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
						code="property.plotNo" /></label>
				<div class="col-sm-4">
					<form:input type="text" class="form-control"
						path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo" maxlength="20"></form:input>
				</div>

			<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
					code="property.khata.no" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo" maxlength="20"></form:input>
			</div>

			
			</div>
			
		<!-- Additional Address Details -->
		<div class="form-group" id=survey>
			<label for="surveyType" class="col-sm-2 control-label "><spring:message
					code="property.surveyType" /> </label>
			<c:set var="baseLookupCode" value="SUT" />
			<apptags:lookupField items="${command.getLevelData(baseLookupCode)}"
				path="provisionalAssesmentMstDto.surveyType" cssClass="form-control"
				hasChildLookup="false" hasId="true" showAll="false"
				selectOptionLabelCode="Select " />
				
			<label class="col-sm-2 control-label required-control" for="tppSurveyNumber"><spring:message
					code="property.khasra.no" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control mandColorClass"
					path="provisionalAssesmentMstDto.tppSurveyNumber"
					id="tppSurveyNumber" data-rule-required="true" maxlength="50"></form:input>
			</div>

		</div>

		<%-- <div class="form-group">
			<label class="col-sm-2 control-label " for="addiSurveyNo"><spring:message
					code="property.addiSurveyNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.addiSurveyNo" id="addiSurveyNo" maxlength="20"></form:input>
			</div>
				<label class="col-sm-2 control-label " for="aaliNo"><spring:message
					code="property.aaliNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.aaliNo" id="aaliNo" maxlength="20"></form:input>
			</div>
			
		</div> --%>

		<!-- <div class="form-group"> -->

		
			
		<!-- </div> -->

	

		<%-- <div class="form-group">
	
			<apptags:radio radioLabel="property.yes,property.no"
										radioValue="Y,N" labelCode="Urban Location Group" 
										path="provisionalAssesmentMstDto.urbLocationGrp" defaultCheckedValue="Y"></apptags:radio> 
		</div> --%>
<%-- 			<apptags:input cssClass="hasemailclass hasNoSpace" labelCode="property.email" path="provisionalAssesmentMstDto.assEmail" maxlegnth="254" dataRuleEmail="true" ></apptags:input> --%>
		<%-- <label for="road-type" class="col-sm-2 control-label required-control"><spring:message code="unitdetails.RoadType"/> </label>
				          <div>
				          <c:set var="baseLookupCode" value="RFT" />
						  <apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
				 							path="provisionalAssesmentMstDto.propLvlRoadType" cssClass="form-control" 
				 							hasChildLookup="false" hasId="true" showAll="false" 
											selectOptionLabelCode="Select Road Factor" isMandatory="true"/> 
						 </div> --%>
		     
		
	
			<div class="col-sm-6">		
				
				<label for="isCorrespondenceAddressSame">
				<form:checkbox path="provisionalAssesmentMstDto.proAssPropAddCheck" value=""
					id="isCorrespondenceAddressSame" onchange="checkForRequired()"/>
				<form:hidden path="provisionalAssesmentMstDto.proAsscheck" id="checkValue"/>
				<spring:message code="property.ifCorrespondingaddress"/>
				 </label>
			</div>
		<div class="clear"></div>
	</div>
</div>

<div id="hideBillingDetails">	
<div class="accordion-toggle">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#correspondenceAddress"><spring:message
				code="property.correspondenceaddress" /></a>
	</h4>

	<div class="panel-collapse collapse in" id="correspondenceAddress">
			
		<div class="form-group">
			<apptags:textArea labelCode="property.correspondenceaddress" path="provisionalAssesmentMstDto.assCorrAddress" maxlegnth="500" cssClass="preventSpace"></apptags:textArea>			
			
<%-- 			<label class="col-sm-2 control-label" for="proAssCorrPincode"><spring:message code="property.pincode"/></label> --%>
<!-- 			<div class="col-sm-4"> -->
<%-- 			<form:input  type="text" class="form-control hasPincode" --%>
<%-- 				 path="provisionalAssesmentMstDto.proAssCorrPincode" id="proAssCorrPincode" maxlength="6" data-rule-required="true" data-rule-digits="true" data-rule-minlength="6" data-rule-maxlength="6"></form:input> --%>
<!-- 			</div> -->
			<apptags:input labelCode="property.pincode" path="provisionalAssesmentMstDto.assCorrPincode" cssClass="hasPincode" maxlegnth="6"></apptags:input>	
		</div>
		<%-- <div class="form-group">
			<apptags:input labelCode="property.email" path="provisionalAssesmentMstDto.assCorrEmail" maxlegnth="254" dataRuleEmail="true" cssClass="preventSpace"></apptags:input>
		</div> --%>	
	</div>	
</div>
</div>
