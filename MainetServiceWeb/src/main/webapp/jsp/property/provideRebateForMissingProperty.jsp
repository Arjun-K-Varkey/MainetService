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
<script type="text/javascript"
	src="js/property/changeInAssessmentForm.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/property/changeUnitDetails.js"></script>
<script type="text/javascript"
	src="js/property/provideRebateForMissingProperty.js"></script>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="" text="Rebate For Missing Property" /></b>
			</h2>
			<apptags:helpDoc url="ProvideRebateForMissingProperty.html"></apptags:helpDoc>

		</div>


		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="ProvideRebateForMissingProperty.html"
				class="form-horizontal form" name="ProvideRebateForMissingProperty"
				id="ProvideRebateForMissingProperty">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

				<div class="form-group">

					<label class="col-sm-2 control-label required-control" for="assNo"><spring:message
							code="property.ChangeInAss.EnterPropertyNo" /></label>
					<div class="col-sm-4">
						<form:input path="assmtDto.assNo"
							class="form-control mandColorClass" id="assNo" autocomplete="off" />
					</div>




					<label class="col-sm-2 control-label"><spring:message
							code="property.FlatNo" text="Flat No" /></label>
					<div class="col-sm-4">
						<form:input path="assmtDto.flatNo"
							class="form-control mandColorClass" id="flatNo" autocomplete="off" />
					</div>
				</div>
				
				<div class="form-group">

					<label class="col-sm-2 control-label required-control" for="assNo"><spring:message
							code="" text="Rebate Type" /></label>
					<div class="col-sm-4">
						<form:input path="rebateType"
							class="form-control mandColorClass" id="rebateType" autocomplete="off" />
					</div>

				</div>
				
				
				<div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success" id="submit"
						onclick="saveRebate(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.search" />
					</button>


				</div>
			</form:form>
		</div>
	</div>
</div>
