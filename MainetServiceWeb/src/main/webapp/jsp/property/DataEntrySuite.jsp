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
<script type="text/javascript" src="js/property/dataEntrySuite.js"></script>
<script type="text/javascript" src="js/property/newOwnerReg.js"></script>
<script>
$('.datepicker').datepicker({
	dateFormat: 'dd/mm/yy',	
	changeMonth: true,
	changeYear: true
});
</script>
<div id="validationDiv">

	<!-- Start breadcrumb Tags -->
	<apptags:breadcrumb></apptags:breadcrumb>
	<!-- End breadcrumb Tags -->

	<!-- Start Content here -->
	<div class="content">
		<!-- Start Main Page Heading -->
		<div class="widget">
			<div class="widget-header">
				<h2>
					<strong><spring:message code="property.dataEntry" text="" /></strong>
				</h2>
				<%-- <div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message code="property.Help"/></span></i></a> --%>
				<!-- 		</div>   -->

				<apptags:helpDoc url="DataEntrySuite.html">
				</apptags:helpDoc>

			</div>

			<!-- End Main Page Heading -->

			<!-- Start Widget Content -->
			<div class="widget-content padding">


				<!-- Start mand-label -->
				<div class="mand-label clearfix">
					<span><spring:message code="property.Fieldwith" /><i
						class="text-red-1">* </i>
					<spring:message code="property.ismandatory" /> </span>
				</div>
				<!-- End mand-label -->

				<!-- Start Form -->
				<form:form action="DataEntrySuite.html" class="form-horizontal form"
					name="DataEntrySuite" id="DataEntrySuite">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
					<%-- 	<div class="padding-10"><strong><i><spring:message code="property.EnterDetailstosearchproperty" /></i></strong></div> --%>

					<input type="hidden" id="ownershipCode" />

					<div class="panel-group accordion-toggle"
						id="accordion_single_collapse">

						<div class="accordion-toggle ">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#propertyMaster"><spring:message
										code="property.propertyMaster" text="Property Information" /></a>
							</h4>
							<div class="panel-collapse collapse in" id="propertyMaster">

								<jsp:include
									page="/jsp/property/NewPropertyRegistrationLandDetails.jsp"></jsp:include>

								<div class="form-group">
									<apptags:input cssClass="alphaNumeric preventSpace"
										labelCode="propertydetails.refPropNo"
										path="provisionalAssesmentMstDto.refPropNo" maxlegnth="40"></apptags:input>

									<apptags:input cssClass="alphaNumeric preventSpace"
										labelCode="propertydetails.gisRefNo"
										path="provisionalAssesmentMstDto.assGisId" maxlegnth="40"></apptags:input>

								</div>


								<jsp:include page="/jsp/property/NewPropertyTaxZoneDetails.jsp"></jsp:include>
								<form:hidden path="billMethodDesc" value=""/>

								<!-- new fields Added -->
								<%-- <div class="form-group">
									<label for="surveyType"
										class="col-sm-2 control-label required-control"><spring:message
											code="property.billingMethod" text="Billing Method" /> </label>
									<c:set var="baseLookupCodess" value="BLM" />
									<apptags:lookupField
										items="${command.getLevelData(baseLookupCodess)}"
										path="provisionalAssesmentMstDto.billMethod"
										cssClass="form-control" hasChildLookup="false" hasId="true"
										showAll="false" selectOptionLabelCode="Select " />

									<label class="col-sm-2 control-label "
										for="billingMethodReason"><spring:message
											code="property.billingMethodReason"
											text="Billing Method Change Reason" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="provisionalAssesmentMstDto.billingMethodReason"
											id="billingMethodReason"></form:input>
									</div>
								</div> --%>
								<!-- new fields  end-->


								<jsp:include page="/jsp/property/NewPropertyBuildingDetails.jsp"></jsp:include>


								<div class="form-group">
									<label class="col-sm-2 control-label required-control"><spring:message
											code="propertydetails.firstAssessmentDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input
												path="provisionalAssesmentMstDto.firstAssessmentDate"
												class="datepicker  form-control mandColorClass dateClass addColor"
												id="firstAssessmentDate" data-rule-required="true"
												placeholder="DD/MM/YYYY" autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>
										</div>
									</div>

									<label class="col-sm-2 control-label "><spring:message
											code="propertydetails.lastAssessmentDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input
												path="provisionalAssesmentMstDto.lastAssessmentDate"
												class="datepicker  form-control  dateClass addColor"
												id="lastAssessmentDate"
												placeholder="DD/MM/YYYY" autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>
										</div>
									</div>
								</div>

								<div class="form-group">								
									<label class="col-sm-2 control-label "><spring:message
											code="propertydetails.reviseAssessmentDate" /></label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input path="provisionalAssesmentMstDto.reviseAssessmentDate"
												class="datepicker  form-control  dateClass addColor"												
												id="reviseAssessmentDate" 
												placeholder="DD/MM/YYYY"
												autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>											
										</div>
									</div>
								</div>

							</div>
						</div>
						

						<div class="accordion-toggle ">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
										code="property.Ownershipdetail" /></a>
							</h4>
							<div class="panel-collapse collapse in" id="OwnshipDetail">
								<div class="form-group">
									<label class="col-sm-2 control-label required-control"
										for="OwnershipDetail"><spring:message
											code="property.ownershiptype" /></label>
									<div class="col-sm-4">
										<c:set var="baseLookupCode" value="OWT" />
										<form:select path="provisionalAssesmentMstDto.assOwnerType"
											onchange="getOwnerTypeDetails()"
											class="form-control mandColorClass" id="ownerTypeId"
											data-rule-required="true">
											<form:option value="">
												<spring:message code="property.sel.optn.ownerType" />
											</form:option>
											<c:forEach items="${command.getLevelData(baseLookupCode)}"
												var="lookUp">
												<form:option value="${lookUp.lookUpId}"
													code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>
									</div>



									<!-- Start For GIS map -->
									<c:if test="${command.getAssType() eq 'E' && command.getGisValue() eq 'Y'}">
										<div class="text-center padding-top-10">
											 <button type="button" class="btn btn-success btn-submit"
												onclick=" window.open('${command.gisUri}&uniqueid=${command.provisionalAssesmentMstDto.assNo}')"
												id="">
												<spring:message text="Map Property on GIS map" code="" />
											</button>
											<button class="btn btn-blue-2" type="button"
												onclick=" window.open('${command.gisUri}&id=${command.provisionalAssesmentMstDto.assNo}')"
												id="">
												<spring:message text="View Property on GIS map" code="" />
											</button> 
										</div>
									</c:if>
									<!-- End For GIS map -->
								</div>

								<form:hidden path="selfAss" id="selfAss" />
								<form:hidden path="assType" id="assType" />
								<form:hidden path="serviceShortCode" id="serviceShortCode" />


								<div id="owner"></div>
							</div>
						</div>


						<jsp:include
							page="/jsp/property/NewPropertyPropertyAddressForm.jsp"></jsp:include>
												
						<jsp:include page="/jsp/property/NewPropRegUnit.jsp">
							<jsp:param name="formName" value="DES" />
						</jsp:include>
						

						<%-- <h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#taxCollEmp"><spring:message
									code="dataEntry.ulb.admin" /></a>
						</h4> --%>
						<%-- <div class="panel-collapse collapse in" id="taxCollEmp">
							<div class="form-group">
								<apptags:select cssClass="chosen-select-no-results"
									labelCode="dataEntry.taxColl.assign"
									items="${command.taxCollList}"
									path="provisionalAssesmentMstDto.taxCollEmp" isMandatory="true"
									isLookUpItem="true"
									selectOptionLabelCode="prop.SelectTaxCollector">
								</apptags:select>
							</div>
						</div> --%>

						<input type="hidden" value="${command.orgId}" id="orgId" /> <input
							type="hidden" value="${command.deptId}" id="deptId" />
					</div>

					<!-- Start button -->
					<div class="text-center padding-10">
						<%-- <c:if test="${command.getAssType() eq 'R'}" >   --%>
						<button type="button" class="btn btn-blue-2"
							onclick="nextToView(this)" id="btnSave">
							<spring:message code="dataEntry.toView" />
						</button>
						<button type="button" class="btn btn-success btn-submit wholeArrears"
							onclick="confirmToProceed(this)" id="arrearEntry">
							<i class="fa fa-plus-circle"></i>
							<spring:message code="dataEntry.ArrearsEntry" />
						</button>
						<c:if test="${command.getAssType() ne 'A'}">
							<button type="button" class="btn btn-danger"
								onclick="BackToSearch()" id="backGrid">
								<spring:message code="propertyBill.Back" />
							</button>
						</c:if>

					</div>

					<!--  End button -->


				</form:form>
				<!-- End Form -->
			</div>
			<!-- End Widget Content here -->
		</div>
		<!-- End Widget  here -->
	</div>
	<!-- End of Content -->
</div>
