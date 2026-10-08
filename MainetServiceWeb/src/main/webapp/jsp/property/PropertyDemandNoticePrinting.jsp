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
	src="js/property/PropertyDemandNoticePrinting.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<!-- End JSP Necessary Tags -->

<style>
.sectionSeperator {
	border-bottom: 1px solid #123456;
	border-top: 1px solid #123456;

	/* padding-bottom: 0px;
    padding-top: 10px;
    padding-left: 5px;
    padding-right: 5px;
    background-color: #f0feff; */
}
</style>
<!-- End JSP Necessary Tags -->

<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="property.demand.notice"/></strong>
			</h2>
			
			<apptags:helpDoc url="PropertyDemandNoticePrinting.html"></apptags:helpDoc>
			
		</div>

		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">

			<!-- Start mand-label -->
			<div class="mand-label clearfix">
				<span><spring:message code="property.Fieldwith" /><i
					class="text-red-1">* </i> <spring:message code="property.ismandatory" /></span>
			</div>
			<!-- End mand-label -->

			<!-- Start Form -->
			<form:form action="PropertyDemandNoticePrinting.html"
				class="form-horizontal form" name="propertyDemandNoticePrint"
				id="propertyDemandNoticePrint">
				<div class="warning-div error-div alert alert-danger alert-dismissible"	id="errorDiv"></div>
				<jsp:include page="/jsp/tiles/validationerror.jsp" />

				<form:hidden path="billingMethod" id="billingMethod" />
				
				<div class="accordion-toggle ">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" href="#a3"><spring:message code="property.demand.notice"/></a>
							</h4>
						</div>

						<div id="a3" class="panel-collapse collapse in">
							<br>
							<div class="form-group">

								<label class="col-sm-2 control-label"> <spring:message
										code="property.demand.notice.radio" text="Select Search Type" /> <span class="mand">*</span>
								</label>
								<div id="searchData" class="col-sm-4">
									<label for="SinMul" class="radio-inline "> <form:radiobutton
											path="specialNotGenSearchDto.specNotSearchType" value="SM"
											id="radioId" class="SinMul" onchange="showSingleMultiple()" />
										<spring:message code="property.SingleORMultiple" /></label> <label
										for="All" class="radio-inline "> <form:radiobutton
											path="specialNotGenSearchDto.specNotSearchType" value="AL"
											id="radioId" class="All" onchange="showSingleMultiple()" />
										<spring:message code="property.All" /></label>
								</div>

								<apptags:select cssClass="chosen-select-no-results"
									labelCode="Notice Type" items="${command.demandType}"
									path="specialNotGenSearchDto.noticeType" isMandatory="true"
									isLookUpItem="true" selectOptionLabelCode="select type">
								</apptags:select>
							</div>
						</div>

						<div class="form-group PropDetail ">
							<apptags:input labelCode="property.PropertyNo"
								path="searchDto.proertyNo"
								cssClass="mandColorClass hasPropNo alphaNumeric"
								isMandatory="true" onBlur="getFlatNo()"></apptags:input>

							<%-- <apptags:input labelCode="Old Property No"
								path="searchDto.oldPid" isMandatory="false"
								cssClass="mandColorClass" /> --%>

						</div>

						<div class="form-group" id="showFlatNo">
							<label class="col-sm-2 control-label"><spring:message
									code="" text="Flat No" /></label>
							<div class="col-sm-4">
								<form:select path="searchDto.flatNo" id="flatNo"
									class="form-control mandColorClass chosen-select-no-results">
									<form:option value="">
										<spring:message code='property.sel.optn' />
									</form:option>
									<c:forEach items="${command.flatNoList}" var="flatNoList">
										<form:option value="${flatNoList}">${flatNoList}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>

						<div class="form-group wardZone">
							<div class="form-group">
								<apptags:lookupFieldSet baseLookupCode="WZB" hasId="true"
									showOnlyLabel="false" pathPrefix="searchDto.assWard"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true"
									cssClass="form-control required-control " showAll="false"
									showData="true" />

							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label required-control"><spring:message
										code="property.financialyear" text="Financial Year" /></label>
								<div class="col-sm-4">
									<form:select path="searchDto.yearId"
										cssClass="form-control mandColorClass " id="finYearId"
										data-rule-required="true">
										<form:option value="0">
											<spring:message code="applicantinfo.label.select" text="select" />
										</form:option>
										<c:forEach items="${command.financialYearMap}" var="yearMap">
											<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</div>

					</div>

					</div>

				<!-- Button -->
				<div class="form-group searchBtn">
					<div class="text-center padding-bottom-10">
						<button type="button" class="btn btn-blue-2 " id="serchBtn"
							onclick="serachProperty()">
							<i class="fa fa-search"></i>
							<spring:message code="property.changeInAss.Search" />
						</button>
						<button type="button" class="btn btn-warning " id="resetBtn"
							onclick="resetForm1(this)">
							<spring:message code="property.reset" />
						</button>
						<button type="button" class="btn btn-danger" id="btn3"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="property.btn.back" />
						</button>
					</div>
				</div>
				<!-- End button -->
				
				<div class="table-responsive clear" id="PropDetails">
					<table id="datatables" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th width="15%"><spring:message	code="property.PropertyNo" /></th>
								<th width="15%"><spring:message	code="property.FlatNo" /></th>
								<th width="20%"><spring:message code="property.OwnerName" /></th>
								<th width="20%"><spring:message code="ownersdetail.mobileno" /></th>
								<th width="20%"><spring:message code="property.outstanding" /></th>
								<th width="20%"><spring:message code="property.finyear" /></th>
								<th width="20%"><spring:message code="" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="propList" items="${command.getNotGenSearchDtoList()}" varStatus="status" > 
								
									<tr>
										<td class="text-center">${propList.propertyNo}</td>
										<td class="text-center">${propList.flatNo}</td>
										<td class="text-center">${propList.ownerName}</td>
										<td class="text-center">${propList.mobileNo}</td>
										<td class="text-center">${propList.outstandingAmount}</td>
										<td class="text-center">${propList.finYear}</td>
										<td style="display:none">
    										<form:hidden path="notGenSearchDtoList[${status.index}].yearId" />
										</td>
										<td class="text-center">
											<button class="btn btn-primary btn-sm" type="button"
												onclick="printNotice(${propList.yearId})">Download</button>
										</td>
									</tr>
					                 	</c:forEach>  
								</tbody>
							</table>
							
						</div>
				
				<!-- 	Start button -->
				<div class="form-group printBtn">
					<div class="text-center padding-bottom-10">
						<button type="button" class="btn btn-info" id="btn1"
							onclick="printNotice(this);">Print</button>

						<button type="button" class="btn btn-warning " id="resetBtn"
							onclick="resetForm1(this)">
							<spring:message code="property.reset" />
						</button>
						<button type="button" class="btn btn-danger" id="btn3"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="property.btn.back" />
						</button>

					</div>
				</div>
				<!-- End button -->

		</form:form>
		</div>
		<!-- End Form -->
	</div>
	<!-- End Widget Content -->
</div>
<!-- End Main Page Heading -->
<!-- Start Content here -->
