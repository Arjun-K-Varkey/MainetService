<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript"
	src="js/property/defaulterRegisterReportForm.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="property.DefaulterRegisterReport"
					text="Defaulter Register Report" />
			</h2>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="" text="" /> <i
					class="text-red-1"></i> <spring:message
						code="property.fiels.mandatory.message" text="" /></span>
			</div>
			<form:form action="DefaulterReport.html" cssClass="form-horizontal"
				id="DefaulterRegisterForm">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>


				<div class="form-group">
					<c:set var="baseLookupCode" value="WZB" />
					<apptags:lookupFieldSet cssClass="form-control required-control"
						baseLookupCode="WZB" hasId="true"
						pathPrefix="propertydto.mnassward"
						hasLookupAlphaNumericSort="true"
						hasSubLookupAlphaNumericSort="true" showAll="true"
						isMandatory="true" />
						
						
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-2 control-label"><spring:message
								code="" text="Cluster No." /></label>
						<div class="col-sm-4">
							<form:select path="propertydto.clusterNo" id="clusterNo"
								class="form-control mandColorClass">
								<form:option value="">
									<spring:message code='adh.select' />
								</form:option>
								<c:forEach items="${command.clusterNoList}" var="clusterList">
									<form:option value="${clusterList}">${clusterList}</form:option>
								</c:forEach>
							</form:select>
						</div>
								
					 <apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="assesmentMstDto.assPropType"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true"
									cssClass="form-control required-control" showAll="true"  columnWidth="10%"/>		
				</div>
				<div class="form-group">
				<c:set var="baseLookupCode" value="USA" />
					<apptags:lookupFieldSet cssClass="form-control required-control"
						baseLookupCode="USA" hasId="true"
						pathPrefix="propertydto.usageType"
						hasLookupAlphaNumericSort="true"
						hasSubLookupAlphaNumericSort="true" showAll="true"
						isMandatory="true" />
						
				</div>

				<div class="form-group">
				<label for="propertyStatus"	class="col-sm-2 control-label required-control"><spring:message
											code="" text="Property Status" /> </label>
						<c:set var="baseLookupCodess" value="STAX" />
						<apptags:lookupField
										items="${command.getLevelData(baseLookupCodess)}"
										path="propertydto.propertyStatus" 
										cssClass="form-control" hasChildLookup="false" hasId="true"
										showAll="true" selectOptionLabelCode="Select " />
						<apptags:input labelCode="property.PropertyNo" path="assesmentMstDto.assNo"></apptags:input>
				</div> 
				
				<div class="padding-top-10 text-center">
					<button type="button" class="btn btn-success btn-submit" id="save"
						onclick="saveForm(this)">
						<spring:message code="property.btn.submit" />
					</button>
					<button type="Reset" class="btn btn-warning" id="resetForm"
						onclick="window.location.href='DefaulterReport.html'">
						<spring:message code="property.btn.reset" />
					</button>
					<a class="btn btn-danger" id="back" href="AdminHome.html"> <spring:message
							code="property.btn.back"></spring:message>
					</a>
				</div>
			</form:form>
		</div>
	</div>
</div>






