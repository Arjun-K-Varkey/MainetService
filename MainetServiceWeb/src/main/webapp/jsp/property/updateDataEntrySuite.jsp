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
<script type="text/javascript" src="js/property/updateDataEntry.js"></script>


<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="edit.name.and.address" text="Edit Name and Address" /></strong>
			</h2>
		</div>
		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="UpdateDataEntrySuite.html"
				class="form-horizontal form" name="UpdateDataEntrySuite"
				id="UpdateDataEntrySuite">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<form:hidden path="billingMethod" id="billingMethod"/>
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>
				<!-- Start Each Section -->
				<div class="form-group">

					<apptags:input labelCode="property.ChangeInAss.EnterPropertyNo"
						path="provisionalAssesmentMstDto.assNo" maxlegnth="20"></apptags:input>
					<%-- <apptags:input labelCode="property.OldPropertyNo"
						path="provisionalAssesmentMstDto.assOldpropno" maxlegnth="20"></apptags:input> --%>

				</div>
				<div class="form-group" id="showFlatNo">
				
					<label class="col-sm-2 control-label"><spring:message
							code="property.FlatNo" text="Flat No" /></label>
					<div class="col-sm-4">
						<form:select path="provisionalAssesmentMstDto.flatNo" id="flatNo"
							class="form-control mandColorClass chosen-select-no-results">
							<form:option value="">
								<spring:message code='property.select' text="select"/>
							</form:option>
							<c:forEach items="${command.flatNoList}" var="flatNoList">
								<form:option value="${flatNoList}">${flatNoList}</form:option>
							</c:forEach>
						</form:select>
					</div>
					
				</div>
				
				<div class="form-group">
				<label class="col-sm-2 control-label"> <spring:message
							code="edit" text="Edit" /> <span class="mand">*</span>
					</label>
					<div class="col-sm-4">
						<div id="searchData col-sm-4">
							<label for="editType" class="radio-inline "> 
							<form:radiobutton path="editType" value="N"	class="editType" checked="true" id="editType"/> 
							<spring:message	code="edit.name" text="Name"/></label> 
						<label for="editType" class="radio-inline "> 
						<form:radiobutton path="editType" value="A" class="editType" id="editType"/> 
						<spring:message code="edit.address" text="Address"/></label>
	
						</div>
					</div>
				</div>
					
				<!--Search Button-->
				<div class="form-group">
			<div class="text-center padding-bottom-10">
			 <button type="button" class="btn btn-success" id="serchBtnDirect"
										onclick="SearchButton(this)">
										<i class="fa fa-search"></i><spring:message code="property.search"/>
			</button> 
				
			<button type="button" class="btn btn-success" id="serchBtn"
										onclick="redirectToSearchPropNo(this)">
										<i class="fa fa-search"></i><spring:message code="property.search"/>
			</button> 	
			
			<button type="button" class="btn btn-warning" data-original-title="Back"
							onclick="window.location.href='UpdateDataEntrySuite.html'">
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

