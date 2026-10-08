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
	src="js/property/propertyFlatNoUpdation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="" text="Property Flat No Updation Form" /></b>
			</h2>
			<apptags:helpDoc url="PropertyEditableForm.html"></apptags:helpDoc>

		</div>

		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="PropertyFlatNoUpdation.html"
				class="form-horizontal form" name="PropertyFlatNoUpdation"
				id="PropertyFlatNoUpdation">
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
								class="form-control mandColorClass" id="assNo"
								autocomplete="off" />
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

					<div class="form-group" id="showFlatNo">

						<label class="col-sm-2 control-label"><spring:message
								code="property.FlatNo" text="Flat No" /></label>
						<div class="col-sm-4">
							<form:select path="provisionalAssesmentMstDto.flatNo" id="flatNo"
								class="form-control mandColorClass chosen-select-no-results">
								<form:option value="">
									<spring:message code='property.select' text="select" />
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
						<c:if
							test="${not empty command.provisionalAssesmentMstDto.assesssmentCategory}">
							<button type="button" class="btn btn-danger" id="back"
								onclick="backToMain(this)">
								<spring:message code="property.Back" text="Back" />
							</button>
						</c:if>

					</div>
				</c:if>
				<c:if test="${command.getEditFlag() eq 'Y'}">
				
				<div class="form-group">
				<apptags:input labelCode="Property No" path="provisionalAssesmentMstDto.assNo"></apptags:input>
				<apptags:input labelCode="Owner Name" path="ownerName"></apptags:input>
				</div>
<div class="table-responsive">
		<c:set var="d" value="0" scope="page"></c:set>
		<table class="table table-hover table-bordered table-striped"
			id="proeprtyDetailTable">
			<thead>
				<tr>
					<th scope="col" width="5%"><spring:message code=""
							text="Sr.No." /></th>

					<th scope="col" width="10%" class="required-control" align="center">Occupier Name</th>
					<th scope="col" width="10%" class="required-control" align="center">Flat No</th>
					<th scope="col" width="10%" class="required-control" align="center">New Flat No</th>
					
				</tr>
			</thead>
			<tbody>
						<c:forEach var="sorDetailsList"
							items="${command.provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList}"
							varStatus="status">
							<tr>
								<td align="center" width="5%"><form:input path=""
										cssClass="form-control mandColorClass " id="sequence${d}"
										value="${d+1}" disabled="true" /></td>


								<td><form:textarea id="occupierName${d}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${d}].occupierName"
										class="form-control"  readonly="true"/></td>


								<td><form:input
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${d}].flatNo"
										class="form-control"
										id="occupierName${d}" readonly="true"/></td>

								<td><form:input
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${d}].newFlatNo"
										class="form-control" id="newFlatNo${d}" /></td>
										</tr>
								<c:set var="d" value="${d + 1}" scope="page" />
						</c:forEach>

			</tbody>
		</table>
	</div>
<div class="text-center padding-10">
				<button type="button" class="btn btn-success"
				onclick="updateFlatNo(this)" id="checkList"><spring:message code="" text="Submit"/></button>
				<%-- <button type="button" class="btn btn-danger"
				onclick="backToPropertyEditableForm(this)" id="backToSearchPage"><spring:message code="property.Back"/></button> --%>
		</div>
				</c:if>
				
				
			</form:form>
		</div>
	</div>
</div>
