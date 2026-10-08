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
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/mainet/validation.js"></script>
<!-- <script src="js/mainet/script-library.js" type="text/javascript"></script> -->
<script src="js/property/propertyEditableForm.js" type="text/javascript"></script>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<b><spring:message code="property.edit.note"
						text="Property Edit Form" /></b>
			</h2>
			<apptags:helpDoc url="PropertyEditableForm.html"></apptags:helpDoc>

		</div>

		<div class="widget-content padding">

			<!-- Start Form -->
			<form:form action="PropertyEditableForm.html"
				class="form-horizontal form" name="propertyEditableForm"
				id="propertyEditableForm">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

				<%-- <form:hidden path="singleOrAllSWMFactor" id="singleOrAllSWMFactor"/> --%>

				<div class="padding-15 unitSpecificInfo">
					<table id="unitSpecificInfoTable${command.getFactorPrefix()}"
						class="table table-striped table-bordered ">
						<tbody>
							<tr>
								<th style="width: 10%" class="required-control"><spring:message
										code="unit.ApplicableUnitNo" /></th>
								<th style="width: 25%" class="required-control"><spring:message
										code="unit.FactorValue" /></th>
								<th style="width: 25%" class=""><spring:message
										code="unit.factorDate" text="Date" /></th>
								<th style="width: 30%" class=""><spring:message
										code="unit.factoRemark" text="Remark" /></th>
								<th style="width: 30%" class=""><spring:message code=""
										text="Amount" /></th>
							</tr>

							<tr class="specFact">
								<td><form:select id="unitNoFact0"
										class="form-control mandColorClass selectUnit"
										path="provAsseFactDtlDto[0].unitNoFact">
										<form:option value="ALL">All</form:option>
									</form:select></td>


								<td><form:hidden path="provAsseFactDtlDto[0].assfFactorId"
										value="${command.getFactorId()}" /> <form:hidden
										value="${command.getFactorPrefix()}"
										path="provAsseFactDtlDto[0].factorValueCode" id="factPref" />
									<form:hidden path="provAsseFactDtlDto[0].proAssfId"
										id="proAssfId" /> <c:set var="baseLookupCode"
										value="${command.getFactorPrefix()}" /> <form:select
										path="provAsseFactDtlDto[0].assfFactorValueId"
										id="FactorValue0"
										cssClass="form-control changeParameterClass mandColorClass factor">
										<c:forEach items="${command.getLevelData(baseLookupCode)}"
											var="lookUp">
											<form:option value="${lookUp.lookUpId}"
												code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
										</c:forEach>
									</form:select></td>

								<td>
									<div class="input-group">
										<form:input path="provAsseFactDtlDto[0].factorDate"
											class="lessthancurrdate form-control mandColorClass dateClass"
											id="factorDate0" placeholder="DD/MM/YYYY" autocomplete="off" maxlength="10"/>
										<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
									</div>
								</td>

								<td><form:input path="provAsseFactDtlDto[0].factorRemark"
										type="text" class="form-control  preventSpace"
										id="factorRemark0" /></td>

								<td><form:input path="provAsseFactDtlDto[0].factorAmount"
										type="text" class="form-control  preventSpace"
										id="factorAmount0" onkeypress="return hasAmount(event, this, 15, 2)"/></td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="text-center padding-10">
					<button type="button" class="btn btn-success"
						onclick="saveSWMFactor(this)" id="">
						<spring:message code="" text="Submit" />
					</button>
					<button type="button" class="btn btn-danger"
						onclick="resetPropEditForm(this)" id="back">
						<spring:message code="property.Back" />
					</button>
				</div>
			</form:form>
		</div>
	</div>
</div>
