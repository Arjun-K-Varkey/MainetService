<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/water/updateWaterDataEntry.js"></script>


<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="" text="Update Water Data Entry" /></strong>
			</h2>
		</div>
		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="UpdateWaterDataEntrySuite.html"
				class="form-horizontal form" name="UpdateWaterDataEntrySuite"
				id="UpdateWaterDataEntrySuite">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>
				<!-- Start Each Section -->
				<div class="form-group">

					<label class="col-sm-2 control-label required-control">
						<spring:message code="water.ConnectionNo"></spring:message></label>
					<div class="col-sm-4">
						<form:input path="waterConnectionDetailsDto.connectionNo" type="text" class="form-control" id="conNum"></form:input>
					</div>
				</div>
				<!--Search Button-->
				<div class="form-group">
					<div class="text-center padding-bottom-10">
						<button type="button" class="btn btn-success" id="serchBtnDirect"
							onclick="searchWaterDetail(this)">
							<i class="fa fa-search"></i><spring:message code="property.search"/>
						</button> 
							<%-- <button type="button" class="btn btn-success" id="searchBtn"
								onclick="searchWaterDetailRedirect(this)">
									<i class="fa fa-search"></i><spring:message code="property.search"/>
							</button> 	 --%>
						
						<button type="button" class="btn btn-warning" data-original-title="Back"
							onclick="window.location.href='UpdateWaterDataEntrySuite.html'">
							<i class="fa fa-undo"></i>
							<spring:message code="property.reset" text="Reset"></spring:message>
						</button>
			        </div>
        		</div>

			</form:form>
			<!-- End Form -->
		</div>
		<!-- End Widget Content here -->
	</div>
	<!-- End Widget  here -->
</div>
<!-- End of Content -->

