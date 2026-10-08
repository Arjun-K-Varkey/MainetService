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



<div class="accordion-toggle">
	<h4 class="margin-top-0 margin-bottom-10 panel-title">
		<a data-toggle="collapse" href="#propertyAddress"><spring:message
				code="property.Propertyaddress" /></a>
	</h4>
	<div class="panel-collapse collapse in" id="propertyAddress">
		<div class="form-group">
			
			<apptags:textArea labelCode="property.propertyaddress" path="provisionalAssesmentMstDto.assAddress" maxlegnth="1000" cssClass="preventSpace" isReadonly="true"></apptags:textArea>
			<apptags:input cssClass="" labelCode="property.location" path="provisionalAssesmentMstDto.locationName" maxlegnth="254" isReadonly="true" ></apptags:input>
		
        </div> 
         
		
		
		<div class="form-group">
		<label class="col-sm-2 control-label " for="proAssPincode"><spring:message code="property.pincode"/></label>
		<div class="col-sm-4">
			<%-- <form:input  type="text" class="form-control"
				 path="provisionalAssesmentMstDto.assPincode" id="proAssPincode" maxlength="6"  data-rule-digits="true" data-rule-minlength="6" data-rule-maxlength="6" readOnly="true"></form:input> --%>
				 <form:input  type="text" class="form-control"
				 path="provisionalAssesmentMstDto.assPincode" id="proAssPincode"   readOnly="true"></form:input>
		</div>
			<label class="col-sm-2 control-label " for="areaName"><spring:message
					code="property.areaName" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.areaName" id="areaName"
					readOnly="true"></form:input>
			</div>

		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label " for="tppPlotNo"><spring:message
					code="property.house.no" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.tppPlotNo" id="tppPlotNo" disabled="true"></form:input>
			</div>

			<label class="col-sm-2 control-label " for="tppKhataNo"><spring:message
					code="property.khata.no" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.tppKhataNo" id="tppKhataNo"
					disabled="true"></form:input>
			</div>

		</div>

		<div class="form-group">
			<label for="location" class="col-sm-2 control-label "><spring:message
					code="property.surveyType" /></label>
			<div class="col-sm-4">
				<c:choose>
					<c:when
						test="${command.provisionalAssesmentMstDto.surveyType eq '0'}">
						<form:input path="" type="text" id="surveyType"
							class="form-control" disabled="true" />
					</c:when>
					<c:otherwise>
						<form:input path="provisionalAssesmentMstDto.surveyTypeDesc"
							type="text" id="surveyType" class="form-control" disabled="true" />
					</c:otherwise>
				</c:choose>
				<%-- <form:input path="provisionalAssesmentMstDto.surveyType"
									type="text" id="surveyType" class="form-control"
									disabled="true" /> --%>
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
					path="provisionalAssesmentMstDto.addiSurveyNo" id="addiSurveyNo"
					disabled="true"></form:input>
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
					path="provisionalAssesmentMstDto.aaliNo" id="aaliNo"
					disabled="true"></form:input>
			</div>

			<label class="col-sm-2 control-label " for="chaaltaNo"><spring:message
					code="property.chaaltaNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control"
					path="provisionalAssesmentMstDto.chaaltaNo" id="chaaltaNo"
					disabled="true"></form:input>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label " for="clusterNo"><spring:message
					code="property.clusterNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control hasNumber"
					path="provisionalAssesmentMstDto.clusterNo" id="clusterNo"
					disabled="true"></form:input>
			</div>

			<label class="col-sm-2 control-label " for="buildingNo"><spring:message
					code="property.buildingNo" /></label>
			<div class="col-sm-4">
				<form:input type="text" class="form-control hasNumber"
					path="provisionalAssesmentMstDto.buildingNo" id="buildingNo"
					disabled="true"></form:input>
			</div>
		</div>

	</div>
</div>


