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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/water/change-usage.js"></script>
<style>
	label[for="oldTrmGroup3"], label[for="newTrmGroup3"] {
		clear: both;
	}
	label[for="oldTrmGroup3"],
	label[for="oldTrmGroup3"] + div, label[for="newTrmGroup3"],label[for="newTrmGroup3"] + div {
		margin-top: 0.7rem;
	}
</style>
<script type="text/javascript">
	/* $(document).ready(function(){
	 $('#trmGroup1').attr('disabled',true);
	 $('#trmGroup2').attr('disabled',true); 
	 $('#conSize').attr('disabled',true);
	 $('#conName').attr('disabled',true);
	 $('#oldAdharNo').attr('disabled',true);
	
	
	 if ($('#bplNo').val() == '') {
	
	 $('#bpldiv').hide();
	 } else {
	 $('#bpldiv').show();
	 }
	
	 }); */

	function saveData(element) {
		if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
				":checked").val() == 'Y') {
			return saveOrUpdateForm(
					element,
					"Your application for change Of ownership saved successfully!",
					'ChangeOfOwnership.html?redirectToPay', 'saveform');
		} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']")
				.filter(":checked").val() == 'N') {
			return saveOrUpdateForm(
					element,
					"Your application for change Of ownership saved successfully!",
					'ChangeOfOwnership.html?PrintReport', 'saveform');
		}
	}

	
</script>
<%-- <c:if test="${command.hasValidationErrors()}">
	<script type="text/javascript">
		if ($('#conNum').val() !='' ) {
			$('#searchConnection').attr('disabled',true);
			$('#confirmToProceedId').attr('disabled',true);
		}
	
	</script>
</c:if> --%>

<div id="fomDivId">

	<apptags:breadcrumb></apptags:breadcrumb>


	<div class="content">
		<!-- Start info box -->
		<div class="widget">
			<div class="widget-header">
				<h2>
					<spring:message code="water.head.changeUsage"
						text="Change of Usage" />
				</h2>
				<div class="additional-btn">
					<apptags:helpDoc url="ChangeOfUsage.html"></apptags:helpDoc>
				</div>
			</div>
			<div class="widget-content padding">
				<div class="mand-label clearfix">
					<span><spring:message code="water.fieldwith" /><i
						class="text-red-1">*</i> <spring:message code="water.ismandtry" />
					</span>
				</div>
				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<ul>
						<li><label id="errorId"></label></li>
					</ul>


				</div>

				<form:form action="ChangeOfUsage.html" method="POST"
					class="form-horizontal" id="changeOfUsageId">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<form:hidden path="" id="enableSubmit"
						value="${command.enableSubmit}" />
					<div class="form-group">
						<label class="col-sm-2 control-label required-control">
						<spring:message code="water.ConnectionNo"></spring:message></label>
						<div class="col-sm-4">
							<form:input path="requestDTO.connectionNo" type="text"
								class="form-control disablefield " id="conNum"></form:input>
						</div>
						<div class="col-sm-6">
							<%-- <button type="button" class="btn btn-success"
								onclick="searchConnectionForSkdcl(this)" id="viewdues">
								<strong class="fa fa-search"></strong>
								<spring:message code="water.view.dues" />
							</button> --%>
							<button type="button" value="Search" class="btn btn-success" id="searchConnection">
								<i class="fa fa-search"></i>
								<spring:message code="searchBtn" />
							</button>
							<input type="button" class="btn btn-warning"
							id="resetConnection" value=<spring:message code="rstBtn"/> />
							<input type="button" class="btn btn-danger" id="back"
								onclick="window.location.href='AdminHome.html'" value=<spring:message code="bckBtn"/> 
							</input>
						</div>	
						
					</div>
					<div id="ccnDuesListId" class="ccnDuesList"></div>
					<div>
						 <h4 >
							 <%-- <a data-toggle="collapse" class=""data-parent="#accordion_single_collapse"
							 href="#old_details"><spring:message code="water.changeUsage.details.old" text="Old Details" />
							 </a>  --%>
 							<spring:message code="water.changeUsage.details.old" text="Old Details" />
 							 		
						</h4>	 		
					<%-- <div class="panel-group accordion-toggle" id="accordion_single_collapse">
						<div class="panel panel-default">
							<div id="old_details" class="panel-collapse in collapse">
								<div class="panel-body">
									
					<c:if test="${command.resultFound eq true }"> 
 						<div id="closeDiv"> --%>
										
							<div class="form-group">
								<label class="col-sm-2 control-label">
								<spring:message code="water.consumerName"></spring:message>
								</label>
							<div class="col-sm-4">
								<form:input path="" cssClass="form-control disablefield" id="conName" disabled="true"
									value="${command.customerInfoDTO.consumerName}" />
							</div>
							
							<label class="col-sm-2 control-label"><spring:message
								code="water.ConnectionSize" /></label>
								
							<div class="col-sm-4">
						 		<div class="input-group">
									<c:set var="baseLookupCode" value="CSZ" />
										<form:select path="requestDTO.connectionSize" class="form-control" id="conSize" disabled="true">
											<form:option value="0"><spring:message code="water.select" /></form:option>
											<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
											<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>	
									<span class="input-group-addon"><spring:message code="water.inch" /></span>
								</div>		
							</div> 
						</div>
						
						<div class="form-group">
							<apptags:lookupFieldSet cssClass="form-control required-control" baseLookupCode="TRF"
							hasId="true" pathPrefix="requestDTO.changeOfUsage.oldTrmGroup" isMandatory="true"
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true" showAll="true" disabled="true"/>
						</div>
						
						<div class="form-group">
						
							<label class="control-label col-sm-2 required-control">
							<spring:message code="MeterReadingDTO.meterType" /></label>
							<div>
								<c:set var="baseLookupCode" value="WMN" />
								<apptags:lookupField items="${command.getLevelData(baseLookupCode)}"
								path="entity.csMeteredccn" cssClass="form-control"
								hasChildLookup="false" hasId="true" showAll="false"
								selectOptionLabelCode="eip.select" isMandatory="true" disabled="true"/>
							</div>
						</div>
						<%--  </c:if>
								</div>
							</div>
						</div>
						
						</div> --%>
					</div>	
						<h4>
							<spring:message code="water.changeUsage.details.new" text="New Details" />
						</h4>
						<div id="newType">
							<div class="form-group margin-bottom-0">
								<apptags:lookupFieldSet baseLookupCode="TRF" hasId="true"
								pathPrefix="requestDTO.changeOfUsage.newTrmGroup"
								isMandatory="true" hasLookupAlphaNumericSort="true"
								hasSubLookupAlphaNumericSort="true"
								cssClass="form-control mandColorClass" />
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2 required-control">
								<spring:message code="MeterReadingDTO.meterType" /></label>
								<div>
									<c:set var="baseLookupCode" value="WMN" />
									<apptags:lookupField items="${command.getLevelData(baseLookupCode)}"
									path="requestDTO.changeOfUsage.newCsMeteredccn"
									cssClass="form-control"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="eip.select" isMandatory="true" />
								</div>	
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label required-control"><spring:message
								code="water.remark" /></label>
								<div class="col-sm-10">
									<form:textarea type="text" class="form-control"
										path="requestDTO.changeOfUsage.remark" id="remark"
										maxlength="200"></form:textarea>
								</div>
							</div>					

						</div>	
										
						<c:if test="${command.resultFound eq true }">
							<jsp:include page="/jsp/water/ChargesAndChecklist.jsp" />

						</c:if>
						<c:if test="${command.enableSubmit eq true}">
							<div class="text-center padding-top-10">
								<input type="button" class="btn btn-success btn-submit"
									onclick="saveChangeOfUsage(this);"
									value=<spring:message code="saveBtn"/> /> <input type="button"
									class="btn btn-warning"
									onclick="window.location.href='ChangeOfUsage.html'"
									value=<spring:message code="rstBtn"/> /> <input type="button"
									class="btn btn-danger"
									onclick="window.location.href='AdminHome.html'"
									value=<spring:message code="bckBtn"/> />
							</div>
						</c:if>
					<!-- </div> -->
				</form:form>
			</div>
		</div>
		<!-- End of info box -->
	</div>
</div>