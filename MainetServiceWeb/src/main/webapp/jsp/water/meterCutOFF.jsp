
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('.lessthancurrdate').datepicker({
							dateFormat : 'dd/mm/yy',
							changeMonth : true,
							changeYear : true,
							maxDate : '-0d',
 							yearRange : "-100:-0",
						});

						$('input[type=text]').attr('disabled', true);
						$("#cutOffDate").attr('disabled', false);
						$("#connectionNo").attr('disabled', false);
						$("#meterStatus").prop("disabled", true);
						$("#cutOffReading").prop("disabled", false);
						$("#searchConnection").prop("disabled", true);
						$("#resetBt").prop("disabled", true);
						$("#billingActive").prop("checked", true);
						var meter = $("#meteredConnection").val();
						var error = $("#hasErrorId").val();
						if (error == "Y") {
							$("#previousMeterCutOff").prop("disabled", false);
							$("#searchConnection").prop("disabled", false);
							if (meter == "Y") {
								$("#viewMeterDetail").prop('disabled', false);
								$("#meterStatus").prop("disabled", false);
							}
						}

						$("#connectionNo").click(function() {
							$("#searchConnection").prop("disabled", false);
							$("#resetBt").prop("disabled", false);
						});

						$("#viewMeterDetail").click(
								function() {
									_openPopUpForm('MeterCutOFF.html',
											'getMeterDetails');
								});

						$("#previousMeterCutOff").click(
								function() {
									_openPopUpForm('MeterCutOFF.html',
											'getPreviousMeterCutOff');
								});

						$("#searchConnection")
								.click(
										function() {
										var errorList = [];
											$("#errorDivId").hide();

											if ($('#connectionNo').val() == "") {
												errorList.push(getLocalMessage("water.meterCutOff.validationMSG.connectionNo"));
												displayErrorsOnPage(errorList);
												return false;
											}
			
											var URL = 'MeterCutOFF.html?getConnectionDetails';
											var requestData = {
												'connectionNo' : $(
														'#connectionNo').val(),
											};
											var result = __doAjaxRequest(URL,
													'POST', requestData, false,
													'json');

											if (result == "N") {
												errorList.push(getLocalMessage("water.meter.noResultFoundMSG"));
												displayErrorsOnPage(errorList);
												$('#connectionNo').val("");
												$('#consumerName').val("");
												$('#conCategory').val("");
												$('#conPremiseType').val("");
												$('#conSize').val("");
												$("#metered").val("");
											} else if(result == "NM") {
													errorList.push(getLocalMessage('water.meter.nonMeterMSG'));
													displayErrorsOnPage(errorList);
													
													$('#consumerName').val("");
													$('#conCategory').val("");
													$('#conPremiseType').val("");
													$('#conSize').val("");
													$("#metered").val("");
													return false;
											}else if(result == "C") {
												errorList.push(getLocalMessage("water.meter.alreadyCutOff"));
												displayErrorsOnPage(errorList);
											$('#consumerName').val("");
											$('#conCategory').val("");
											$('#conPremiseType').val("");
											$('#conSize').val("");
											$("#metered").val("");
											return false;
											} else {
												var splitArray = [];
												splitArray = result.split("~");
												$('#consumerName').val(
														splitArray[0]);
												$('#conCategory').val(
														splitArray[1]);
												$('#conPremiseType').val(
														splitArray[2]);
												$('#conSize')
														.val(splitArray[3]);
												var meteredConn = splitArray[4];
												if (meteredConn == "Y") {
													$("#metered").val("Y");
													$("#viewMeterDetail").attr(
															'disabled', false);
													$("#meterStatus").prop(
															"disabled", true);
												} else {
													$("#metered").val("N");
													$("#viewMeterDetail").attr(
															'disabled', true);
													$("#meterStatus").prop(
															"disabled", true);
												}
												$("#cutOffReading").val(splitArray[5]);
												$("#meterStatus").val(splitArray[6]);
												$("#cutOffDate").val(splitArray[7]);
												$("#previousMeterCutOff").prop(
														"disabled", false);
											}

										});

						$("#resetBtnId").click(function() {
							$("#cutOffReading").val("");
							$("#remarks").val("");
							$("#meterStatus").val("");
							$("#cutOffDate").val("");
							$("#errorDivId").hide();
						});
						
						$("#resetBt").click(function() {		
							$("#connectionNo").val("");
							$("#consumerName").val("");
							$("#conCategory").val("");
							$("#conPremiseType").val("");
							$("#conSize").val("");
							$("#viewMeterDetail").prop('disabled', true);
							$("#previousMeterCutOff").prop("disabled", true);
							$("#errorDivId").hide();
						});

					});

	function saveMeterCutOffDetails(element) {

		return saveOrUpdateForm(element, "", 'AdminHome.html', 'saveform');
	}

	
</script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- ============================================================== -->
<!-- Start Content here -->
<!-- ============================================================== -->
<div class="content">
	<!-- Start info box -->
	<div class="row">
		<div class="col-md-12">
			<div class="widget">
				<div class="widget-header">

					<h2>
						<spring:message code="water.meter.cutOff" />
					</h2>
					
					<apptags:helpDoc url="MeterCutOFF.html"></apptags:helpDoc>
				</div>

				<div class="widget-content padding">
					<div class="mand-label clearfix">
						<span><spring:message code="water.fieldwith" /><i
							class="text-red-1">*</i> <spring:message code="water.ismandtry" />
						</span>
					</div>

					<!-- <div class="alert alert-danger alert-dismissible" id="errorDivId"
						style="display: none;">
						<ul>
							<li></li>
						</ul>
						<button type="button" class="close" onclick="closeOutErrBox()"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div> -->
 					<div class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
					<form:form action="MeterCutOFF.html" class="form-horizontal"
						name="frmMeterCutOFF" id="meterCutOFF">

						<jsp:include page="/jsp/tiles/validationerror.jsp" />

						<form:hidden path="" id="hasErrorId" value="${command.hasError}" />
						<form:hidden path="meteredFlag" id="metered" />
						<%-- <form:hidden path="" id="meteredConnection"
							value="${command.meteredFlag}" /> --%>


						<div class="panel-group accordion-toggle"
							id="accordion_single_collapse">

							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a data-toggle="collapse" class=""
											
											href="#meter-details"><spring:message
												code="water.dataentry.meter.details" text="Meter Details"/></a>
									</h4>
								</div>
								<div id="meter-details" class="panel-collapse collapse in">
									<div class="panel-body">


										<div class="form-group">
											<label class="col-sm-2 control-label required-control"><spring:message
													code="water.ConnectionNo"></spring:message></label>
											<div class="col-sm-4">
												<form:input path="meterCutOffResDTO.connectionNo"
													type="text" class="form-control" id="connectionNo"></form:input>
											</div>
											<div class="col-sm-6">
												<button type="button" value="Search"
													class="btn btn-success" id="searchConnection">
													<i class="fa fa-search"> </i> <spring:message
												code="water.nodues.search" text="Search"/>
												</button>
												<button type="button" class="btn btn-warning" id="resetBt">
													<spring:message code="water.btn.reset" />
												</button>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label "><spring:message
													code="water.consumerName" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="consumerName" id="consumerName"></form:input>
											</div>
											<label class="col-sm-2 control-label"><spring:message
													code="water.connectiondetails.tariffcategory" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="tarrifCategory" id="conCategory"></form:input>
											</div>
										</div>

										<div class="form-group">
											<label class="col-sm-2 control-label"><spring:message
													code="water.connectiondetails.premisetype" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="premiseType" id="conPremiseType"></form:input>
											</div>
											<label class="col-sm-2 control-label"><spring:message
													code="water.ConnectionSize" /></label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="connectionSize" id="conSize"></form:input>
											</div>
										</div>


										<%-- <div class="form-group">
											<div class="col-sm-6">
												<label class="checkbox-inline"> <form:checkbox
														path="meteredFlag" value="Y" id="metered" disabled="true" />
													<spring:message code="water.meter.meteredCheckBox" />
												</label>
											</div>
										</div> --%>

									</div>
									<div class="text-center">
										<button type="button" value=View class="btn btn-blue-2"
											id="viewMeterDetail" disabled="disabled">
											<spring:message code="water.met.det" />
										</button>
										<button type="button" value=View class="btn btn-blue-2"
											id="previousMeterCutOff" disabled="disabled">
											<spring:message code="water.meter.cututOffPreviousDetails" />
										</button>
									</div>
								</div>

							</div>






							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a data-toggle="collapse" class="collapsed"
											 href="#cut-off"><spring:message
												code="water.meter.cutOffDetails" /></a>
									</h4>
								</div>
								<div id="cut-off" class="panel-collapse collapse">
									<div class="panel-body">


										<div class="form-group">
											<label class="col-sm-2 control-label required-control"><spring:message
													code="water.meter.cutOffReason" /></label>
											
											<div class="col-sm-4">
												<form:textarea type="textarea"
													class="form-control maxLength100 "
													path="meterCutOffResDTO.cutResRemark" id="remarks"></form:textarea>
											</div>
											<label class="col-sm-2 control-label required-control "><spring:message
													code="water.met.status" /></label>
											<div class="col-sm-4">
												<c:set var="baseLookupCode" value="MST" />
												<form:select path="meterCutOffResDTO.meterStatus"
													class="form-control" id="meterStatus" disabled="disabled">
													<form:option value="0">
														<spring:message code="water.select" />
													</form:option>
													<c:forEach items="${command.getLevelData(baseLookupCode)}"
														var="lookUp">
														<form:option value="${lookUp.lookUpId}"
															code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
													</c:forEach>
												</form:select>
											</div>
										</div>

										<div class="form-group">
											<label class="col-sm-2 control-label required-control"><spring:message
													code="water.meter.cutOffDate" /></label>
											<div class="col-sm-4">
												<div class="input-group">
													<form:input path="meterCutOffResDTO.cutResDate"
														class="form-control lessthancurrdate" id="cutOffDate" />
													<label class="input-group-addon" for="cutOffDate"><i
														class="fa fa-calendar"></i></label>
												</div>
											</div>
											<label
												class="col-sm-2 control-label required-control hasNumber"><spring:message
													code="water.meter.cutOffReading" /></label>
											<div class="col-sm-4">
												<form:input path="meterCutOffResDTO.cutResRead"
													class="form-control hasNumber" maxlength="12"
													id="cutOffReading" disabled="false"/>
											</div>
										</div>
										<div class="form-group">
											<div class="col-sm-6">
												<label class="checkbox-inline"> <form:checkbox
														path="billingActiveFlag" value="Y" id="billingActive" />
													<spring:message
														code="water.meterCutOff.field.billingActive" />
												</label>
											</div>
										</div>

									</div>
								</div>
							</div>
						</div>


						<div class="text-center padding-top-10">
							<button type="button" class="btn btn-success btn-submit"
								id="submitBtn" onclick="saveMeterCutOffDetails(this);">
								<spring:message code="water.btn.submit" />
							</button>
							<button type="button" class="btn btn-warning" id="resetBtnId">
								<spring:message code="water.btn.reset" />
							</button>
							<button type="button"
								onclick="window.location.href='AdminHome.html'"
								class="btn btn-danger">
								<spring:message code="water.btn.back" text="Back" />
							</button>

						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
