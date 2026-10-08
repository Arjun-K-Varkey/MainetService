<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script src="js/account/cashBookReport.js"></script>
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<script>
	$(function() {
		$("#fromDateId").datepicker({
			dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			maxDate : '0'
		});
				
		$("#fromDateId").keyup(function(e) {
			if (e.keyCode != 8) {
				if ($(this).val().length == 2) {
					$(this).val($(this).val() + "/");
				} else if ($(this).val().length == 5) {
					$(this).val($(this).val() + "/");
				}
			}
		});
		
		$("#toDateId").datepicker({
			dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			maxDate : '0'
		});
				
		$("#toDateId").keyup(function(e) {
			if (e.keyCode != 8) {
				if ($(this).val().length == 2) {
					$(this).val($(this).val() + "/");
				} else if ($(this).val().length == 5) {
					$(this).val($(this).val() + "/");
				}
			}
		});
		
	});
</script>
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2><spring:message code="account.cash.book.report" text="Cash Book Report" /></h2>
		</div>
		<apptags:helpDoc url="incomeAndExpenditureSheduleReport.html"
			helpDocRefURL="incomeAndExpenditureSheduleReport.html"></apptags:helpDoc>
		<div class="widget-content padding">
			<form:form action="" class="form-horizontal">
			   <div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span id="errorId"></span>
				</div>
			
				<div class="form-group">
					<label for="date-1493383113506" class="col-sm-2 control-label "><spring:message
							code="budget.reappropriation.authorization.fromdate" text="From Date" /><span class="required-control"></span>
					</label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="" id="fromDateId"
								cssClass="mandColorClass form-control" data-rule-required="true" maxlength="10" />
							<label class="input-group-addon mandColorClass" for="fromDateId"><i
								class="fa fa-calendar"></i> </label>
						</div>
					</div>
					
					<label for="date-1493383113506" class="col-sm-2 control-label "><spring:message
							code="budget.reappropriation.authorization.todate" text="To Date" /><span class="required-control"></span>
					</label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="" id="toDateId"
								cssClass="mandColorClass form-control" data-rule-required="true" maxlength="10" />
							<label class="input-group-addon mandColorClass" for="toDateId"><i
								class="fa fa-calendar"></i> </label>
						</div>
					</div>
					
				</div>
				<div class="form-group">
					<label for="Field" class="col-sm-2 control-label "><spring:message code="account.finance.field"
							text="Field" /></label>
					<div class="col-sm-4">
						<form:select path=""
							class="form-control mandColorClass chosen-select-no-results"
							name="field" id="fieldCode2">
							<option value=""><spring:message
									code="account.common.select" /></option>
							<c:forEach items="${command.fieldMasterLastLvls}" varStatus="status"
								var="fieldItem">
								<form:option value="${fieldItem.key}" code="${fieldItem.key}">${fieldItem.value}</form:option>
							</c:forEach>
						</form:select>
					</div>

				</div>
				<div class="text-center padding-top-10">
					<button type="button" class="btn btn-blue-2" onClick="cashBookReport('cashBookReport.html','cashBookReport')" title="View Report">
					<spring:message code="account.financial.view.report" text="View Report"></spring:message>
					</button>
					<button type="button" class="btn btn-warning" onClick="cashBookReset(this)" title="Reset"> <spring:message code="account.bankmaster.reset" text="Reset" />
					</button>
					<apptags:backButton url="AdminHome.html"></apptags:backButton>
				</div>
			</form:form>
		</div>
	</div>
</div>
