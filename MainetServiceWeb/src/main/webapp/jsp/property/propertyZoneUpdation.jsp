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
	src="js/property/propertyZoneUpdation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="property.update.zone.note" text="Property Zone Updation Form" /></b>
			</h2>
			<apptags:helpDoc url="PropertyZoneUpdation.html"></apptags:helpDoc>

		</div>

		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="PropertyZoneUpdation.html"
				class="form-horizontal form" name="PropertyZoneUpdation"
				id="PropertyZoneUpdation">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>
				<c:if test="${command.getEditFlag() eq 'N'}">

					<div class="form-group">

						<label class="col-sm-2 control-label required-control" for="assNo"><spring:message
								code="property.ChangeInAss.EnterPropertyNo" /></label>
						<div class="col-sm-4">
							<form:input path="provisionalAssesmentMstDto.assNo"
								class="form-control mandColorClass preventSpace" id="assNo" maxlength="12"
								autocomplete="off" />
						</div>

					</div>

					<div class="text-center padding-bottom-10">

						<button type="button" class="btn btn-success" id="serchBtnDirect"
							onclick="SearchButton(this)">
							<i class="fa fa-search"></i>
							<spring:message code="property.search" />
						</button>
						<button type="button" id="resetForm" class="btn btn-info" onclick="resetZoneForm()">
								<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
								<spring:message code="property.reset" text="Reset" />
							</button>

					</div>
				</c:if>
				<c:if test="${command.getEditFlag() eq 'Y'}">

					<div class="form-group">
						<apptags:input labelCode="property.PropertyNo"
							path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
					</div>

					<div class="form-group">
						<apptags:lookupFieldSet cssClass="form-control required-control"
							baseLookupCode="WZB" hasId="true"
							pathPrefix="provisionalAssesmentMstDto.assWard"
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true" showAll="false"
							isMandatory="true" />
					</div>
					
					<div class="text-center padding-10">
						<button type="button" class="btn btn-success"
							onclick="updateZone(this)" id="update">
							<spring:message code="property.update" text="Update" />
						</button>
						
						<button type="button" class="btn btn-danger"
							onclick="backToSearchPage(this)" id="back">
							<spring:message code="property.Back" text="Back" />
						</button>
					</div>
				</c:if>


			</form:form>
		</div>
	</div>
</div>
