<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/property/propertyDuesCheck.js"></script>


<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="property.dues.check.titile" text="Property Dues Check" />
			</h2>
		</div>
		<div class="widget-content padding">
			<form:form action="PropertyDuesCheck.html" class="form-horizontal"
				name="PropertyDuesCheck" id="PropertyDuesCheck">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				
				<form:hidden path="billingMethod" id="billingMethod"/>
				
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message
								code="property.duesCheck.property.number"
								text="Property Number" /></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control preventSpace"
								path="searchDto.proertyNo" id="proertyNo" maxlength="20" 
								onchange="redirectToSearchPropNo(this)" autocomplete="off"></form:input>
						</div>

						<label class="col-sm-2 control-label" for="ownerName"><spring:message
								code="property.duesCheck.owner.name" text="Owner Name" /></label>
						<div class="col-sm-4">
							<form:input name="" type="text" class="form-control preventSpace hasCharacter"
								path="searchDto.ownerName" id="ownerName"></form:input>
						</div>

					</div>
					
					<div class="form-group" id="showFlatNo">
						<label class="col-sm-2 control-label"><spring:message
									code="property.FlatNo" text="Flat No" /></label>
							<div class="col-sm-4">
								<form:select path="searchDto.flatNo" id="flatNo"
									class="form-control mandColorClass chosen-select-no-results">
									<form:option value="">
										<spring:message code='property.select' />
									</form:option>
									<c:forEach items="${command.flatNoList}" var="flatNoList">
										<form:option value="${flatNoList}">${flatNoList}</form:option>
									</c:forEach>
								</form:select>
							</div>
					
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><spring:message
								code="property.duesCheck.occupier.name"
								text="Occupier Name" /></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control preventSpace hasCharacter"
								path="searchDto.occupierName" id="occupierName"></form:input>
						</div>

						<label class="col-sm-2 control-label" for="address"><spring:message
								code="property.duesCheck.address" text="Address" /></label>
						<div class="col-sm-4">
							<form:input name="" type="text" class="form-control preventSpace"
								path="searchDto.address" id="address"></form:input>
						</div>

					</div>

					<div class="form-group">
						<apptags:lookupFieldSet baseLookupCode="WZB" hasId="true"
							showOnlyLabel="false" pathPrefix="searchDto.assWard" isMandatory="false"
							hasLookupAlphaNumericSort="true" hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control " showAll="false" showData="true" />
					</div>
								
					<div class="form-group">
						<div class="padding-top-10 text-center">
							<button type="button" class="btn btn-info"
								onclick="searchPropertyDetail(this);">
								<i class="fa fa-search"></i>
								<spring:message code="property.search" />
							</button>

							<button type="button" class="btn btn-warning"
								onclick="resetForm(this)">
								<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
								<spring:message code="property.reset" />
							</button>

						<button type="button" class="btn btn-danger" id="back"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="property.Back" text="Back"></spring:message>
						</button>
					</div>

					</div>
					<!-- <div id="" align="center">
						<table id="grid"></table>
						<div id="pagered"></div>
					</div> -->

				<div class="table-responsive">
					<div class="table-responsive margin-top-10">
						<table class="table table-striped table-condensed table-bordered"
							id="propDuesCheck">
							<thead>
								<tr>
								<th><spring:message code="property.duesCheck.property.number" text="Property No." /></th>
								<th><spring:message code="property.FlatNo" text="Flat No" /></th>
								<th><spring:message code="property.duesCheck.owner.name" text="Owner Name" /></th>
								<th><spring:message code="property.occupierName" text="Occupier Name" /></th>
								<th><spring:message code="property.mobile" text="Mobile Number" /></th>
								<th><spring:message code="property.duesCheck.address" text="Address" /></th>
								<th><spring:message code="property.totalDuesAmt" text="Total Dues Amount" /></th>
								<th><spring:message code="view.dues" text="View Dues" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${command.searchDtoResult}" var="searchDto">
									<tr>
									<td>${searchDto.proertyNo}</td>
									<td>${searchDto.flatNo}</td>
									<td>${searchDto.ownerName}</td>
									<td>${searchDto.occupierName}</td>
									<td>${searchDto.mobileno}</td>
									<td>${searchDto.address}</td>
									<td>${searchDto.outstandingAmt}</td>
									<td align="center">
									  <button type="button" class="btn btn-blue-2 btn-sm"
												title="View Dues"
												onclick="viewProperty('${searchDto.proertyNo}_${searchDto.flatNo}_${searchDto.mobileno}')">
												<i class="fa fa-eye"></i>
									  </button>
									</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				

			</form:form>
		</div>
	</div>
</div>
