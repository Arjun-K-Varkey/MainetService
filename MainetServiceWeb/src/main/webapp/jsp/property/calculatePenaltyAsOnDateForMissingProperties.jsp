
<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript"
	src="js/property/calculatePenaltyAsOnDate.js"></script>
<!-- End JSP Necessary Tags -->

<style>
.sectionSeperator {
	border-bottom: 1px solid #123456;
	border-top: 1px solid #123456;
}

.topbar {
	z-index: 500 !important;
}
</style>

<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="property.calculate.penalty" /></strong>
			</h2>

			<apptags:helpDoc
				url="CalculatePenaltyAsOnDateForMissingProperties.html"></apptags:helpDoc>

		</div>

		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">

			<!-- Start mand-label -->
			<div class="mand-label clearfix">
				<span><spring:message code="property.Fieldwith" /><i
					class="text-red-1">* </i> <spring:message
						code="property.ismandatory" /></span>
			</div>
			<!-- End mand-label -->

			<!-- Start Form -->
			<form:form action="CalculatePenaltyAsOnDateForMissingProperties.html"
				class="form-horizontal form"
				name="CalculatePenaltyAsOnDateForMissingProperties"
				id="CalculatePenaltyAsOnDateForMissingProperties">

				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

				<jsp:include page="/jsp/tiles/validationerror.jsp" />


				<!-- 			Button -->
				<div class="form-group searchBtn">
					<div class="text-center padding-bottom-10">
						<button type="button" class="btn btn-info" id="btn1"
							onclick="calculatePenalty(this);">
							<spring:message code="propertyBill.GenerateBill" />
						</button>

					</div>
				</div>

				<!-- 	End button -->

			</form:form>
			<!-- End Form -->
		</div>
		<!-- End Widget Content -->
	</div>
	<!-- End Main Page Heading -->
</div>
<!-- Start Content here -->

