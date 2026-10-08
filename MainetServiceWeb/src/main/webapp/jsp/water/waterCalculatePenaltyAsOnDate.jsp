
<!-- Start JSP Necessary Tags -->
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
	src="js/water/waterCalculatePenaltyAsOnDate.js"></script>

<style>
.sectionSeperator {
	border-bottom: 1px solid #123456;
	border-top: 1px solid #123456;
}

.topbar{
	z-index:500 !important;
}
</style>
<script>
$(document).ready(function(e) {
	$("#single").prop("checked", true);
	var radioValue = $("input[name='connectionType']:checked").val();
	if (radioValue === 'S') {
		$('#connNumber').show();
		$('#multiSearch').hide();
	} else {
		$('#connNumber').hide();
		$('#ccnNumber').val('');
		$('#multiSearch').show();
	}
	
	$(".conSelect").click(function() {
		var radioValueSel = $("input[name='connectionType']:checked").val();
		if (radioValueSel === 'S') {
		    $('#codDwzid1, #codDwzid2, #codDwzid3').val('0');
			$('#connNumber').show();
			$('#multiSearch').hide();
		} else {
			$('#connNumber').hide();
			$('#ccnNumber').val('');
			$('#multiSearch').show();
		}
	});
});


</script>
<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="water.penalty.asondate" text="Penalty As On Date"/></strong>
			</h2>
			
				<apptags:helpDoc url=""></apptags:helpDoc>		
			
		</div>

		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">

			<!-- Start mand-label -->
			<div class="mand-label clearfix">
				<span><spring:message code="water.fieldwith" /><i
					class="text-red-1">* </i> <spring:message
						code="water.ismandtry" /></span>
			</div>
			<!-- End mand-label -->

			<!-- Start Form -->
			<form:form action="WaterCalculatePenaltyAsOnDate.html"
				class="form-horizontal form" name="CalculatePenaltyAsOnDate"
				id="CalculatePenaltyAsOnDate">
				
				<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
				
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="form-group">			
					<label class="control-label col-sm-2"><spring:message
							code="water.billingtype" /><span class="mand">*</span></label>
					<div class="col-sm-4">
						<label class="radio-inline"><form:radiobutton
								path="connectionType" value="S" id="single" class="conSelect" /> <spring:message
								code="water.meterReading.Single" /></label> <label class="radio-inline"><form:radiobutton
								path="connectionType" value="M" id="All" class="conSelect" /> <spring:message
								code="water.bill.Multiple" /></label>
					</div>

					<div id='connNumber'>
						<label class="control-label col-sm-2 required-control"><spring:message
								code="water.ConnectionNo" /></label>
						<div class="col-sm-4">
							<form:input path="csmrInfo.csCcn" type="text" class="form-control"
								id="ccnNumber"></form:input>
						</div>
					</div>
				</div>
				<div id='multiSearch'>
					<div class="panel-group accordion-toggle margin-bottom-0"
						id="accordion_single_collapse">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse" href="#multiple">
										<spring:message code="water.meterReading.Multiple" />
									</a>
								</h4>
							</div>
							<div id="multiple" class="panel-collapse collapse in">
								<div class="panel-body">
									<div class="form-group  margin-bottom-0">
										<apptags:lookupFieldSet
											cssClass="form-control required-control"
											baseLookupCode="WWZ" hasId="true"
											pathPrefix="csmrInfo.codDwzid" isMandatory="true"
											hasLookupAlphaNumericSort="true"
											hasSubLookupAlphaNumericSort="true" showAll="true" />
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
					
				<div class="form-group searchBtn">
					<div class="text-center padding-bottom-10">
						<button type="button" class="btn btn-info" id="btn1"
							onclick="calculatePenalty(this);">
							<spring:message code="water.btn.submit" text="Submit"/>
						</button>
						<button type="button" class="btn btn-warning " id="resetBtn"
							onclick="resetFormDetails(this)">
							<spring:message code="water.btn.reset" text="Reset" />
						</button>
	
					</div>
				</div>
		
		<!-- 	End button -->

		</form:form>
		<!-- End Form -->
	</div>
	<!-- End Widget Content -->
</div>
<!-- End Main Page Heading -->
</div>
<!-- Start Content here -->

