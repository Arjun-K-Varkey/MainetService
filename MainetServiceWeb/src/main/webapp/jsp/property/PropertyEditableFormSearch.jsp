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
<script type="text/javascript" src="js/property/propertyEditableForm.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="property.edit.note" text="Property Edit Form" /></b>
			</h2>
			<apptags:helpDoc url="PropertyEditableForm.html"></apptags:helpDoc>

		</div>

		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="PropertyEditableForm.html"
				class="form-horizontal form" name="propertyEditableForm"
				id="propertyEditableForm">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
			<form:hidden path="billingMethod" id="billingMethod"/>
				<div class="form-group">

					<label class="col-sm-2 control-label required-control" for="assNo"><spring:message
							code="property.ChangeInAss.EnterPropertyNo" /></label>
					<div class="col-sm-4">
						<form:input path="provisionalAssesmentMstDto.assNo"
							class="form-control mandColorClass" id="assNo" autocomplete="off"/>
					</div>

					<%-- <div class="col-sm-2 text-center">
						<i class="text-red-1"><spring:message code="property.OR" /></i>
					</div>
					<label class="col-sm-2 control-label required-control"
						for="assOldpropno"><spring:message
							code="property.ChangeInAss.oldpid" /></label>

					<div class="col-sm-3">
						<form:input path="provisionalAssesmentMstDto.assOldpropno"
							class="form-control mandColorClass" id="assOldpropno" />
					</div> --%>

				</div>
				
				<div class="form-group" >
				
				<div id="showSWMRadioButton">
					<label class="col-sm-2 control-label"> <spring:message
							code="" text="Select To Add SWM Factor for All Flat" /> <span class="mand">*</span>
					</label>
					<div class="col-sm-4">
						<label for="SinMul" class="radio-inline "> 
							<form:radiobutton path="singleOrAllSWMFactor" value="ALL"
								class="SinMul" onchange="redirectToSingleOrAllSWMFactor()" /> 
								<spring:message code="" text="Yes"/>
						</label> 
						<label for="SinMul"	class="radio-inline "> 
							<form:radiobutton path="singleOrAllSWMFactor" value="S"
								class="SinMul" onchange="redirectToSingleOrAllSWMFactor()" />
						 	<spring:message code="" text="No"/>
						</label>
						<div class="col-sm-12">
							<small class="text-blue-2 ">
								<i> <spring:message	code="property.edit.swm.option.note"
								text="Note : Yes option is given only for to add SWM factor for All flat" /></i>
							</small>
					  	</div>		
					</div>
				</div>
				</div>
				
				<div class="form-group" id="showFlatNo">
					<label class="col-sm-2 control-label"><spring:message
							code="property.FlatNo" text="Flat No" /></label>
					<div class="col-sm-3">
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

				<div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success" id="serchBtnDirect"
						onclick="SearchButton(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.search" />
					</button>
					
					<button type="button" class="btn btn-success" id="serchBtn"
						onclick="redirectToSearchPropNo(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.search" />
					</button>
					
					<button type="button" class="btn btn-blue-2" id="searchSWMFactorButton"
						onclick="searchSWMFactor(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.search" />
					</button>
					
					<button type="button" class="btn btn-warning" onclick="resetPropEditForm();">
							<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>	
							<spring:message code="property.reset" text="Reset"/>
					</button>
					<c:if
						test="${not empty command.provisionalAssesmentMstDto.assesssmentCategory}">
						<button type="button" class="btn btn-danger" id="back"
							onclick="backToMain(this)">
							<spring:message code="property.Back" text="Back" />
						</button>
					</c:if>



				</div>
			</form:form>
		</div>
	</div>
</div>
