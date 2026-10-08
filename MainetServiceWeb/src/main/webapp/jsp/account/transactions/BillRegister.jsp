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
<script src="js/account/accountBillRegister.js"></script>

<script src="js/mainet/validation.js"></script>
<script
	src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<script>
$(function() {
	$("#frmdate").keyup(function(e){
	    if (e.keyCode != 8){    
	        if ($(this).val().length == 2){
	            $(this).val($(this).val() + "/");
	        }else if ($(this).val().length == 5){
	            $(this).val($(this).val() + "/");
	        }
	     }
	    });
	$("#tocdate").keyup(function(e){
	    if (e.keyCode != 8){    
	        if ($(this).val().length == 2){
	            $(this).val($(this).val() + "/");
	        }else if ($(this).val().length == 5){
	            $(this).val($(this).val() + "/");
	        }
	     }
	    });
});
</script>
	
	
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content" id="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="account.bill.register" text="Bill Register" />
			</h2>
		</div>
		<div class="widget-content padding">
			<div class="error-div alert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<button type="button" class="close" onclick="closeOutErrBox()"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<span id="errorId"></span>
			</div>

			<form:form action="" modelAttribute="aBillRegisterBean"
				class="form-horizontal" id="BillReceiptPrintId">
				<div class="form-group">

					<label class="control-label col-sm-2 required-control" for="trTenderDate"><spring:message
							code="budget.reappropriation.authorization.fromdate" text="From Date" /></label>


					<div class="col-sm-4">


						<div class="input-group">
							<input class="form-control datepicker" id="frmdate" path=" "
								maxlength="10" > <label class="input-group-addon"
								for="trasaction-date-icon30"><i class="fa fa-calendar"></i></label>
						</div>
					</div>


					<label class="control-label col-sm-2 required-control" for="trTenderDate"><spring:message
							code="budget.reappropriation.authorization.todate" text="To Date" /></label>
					
					<div class="col-sm-4">

						<div class="input-group">
							<input class="form-control datepicker" id="tocdate" path=" "
							maxlength="10" > <label class="input-group-addon"
								for="trasaction-date-icon30"><i class="fa fa-calendar"></i></label>
						</div>

					</div>
					
					</div>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"
						for="accountHead"><spring:message code="account.deposit.accountHead"
							text="Account Head" /></label>
					<div class="col-sm-4">
						<form:select id="accountHead" path=""
							class="form-control chosen-select-no-results">
							<form:option value="">
								<spring:message code="account.common.select" />
							</form:option>
							<form:option value="0" code="ALL">All</form:option>
							<c:forEach items="${pacMap}" varStatus="status" var="pacItem">
								<form:option value="${pacItem.key}" code="${pacItem.key}">${pacItem.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<label for="Field" class="col-sm-2 control-label required-control"><spring:message
							code="account.common.field" text="Field" /></label>
					<div class="col-sm-4">
						<form:select path=""
							class="form-control mandColorClass chosen-select-no-results"
							name="field" id="fieldId">
							<option value=""><spring:message
									code="account.common.select" /></option>
							<c:forEach items="${fieldMasterLastLvls}" varStatus="status"
								var="fieldItem">
								<form:option value="${fieldItem.key}" code="${fieldItem.key}">${fieldItem.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>
					<div class="form-group">
				<label class="col-sm-2 control-label required-control" for="BillType"><spring:message
							code="bill.type" text="Bill Type" /></label>
					<div class="col-sm-4">		
					<form:select id="billType"
						path=""
						class="form-control chosen-select-no-results">
						<form:option value="">
							<spring:message code="account.common.select" />
						</form:option>
						<form:option value="0" code="ALL">All</form:option>
						<c:forEach items="${BillType}" varStatus="status" var="billType">
							<form:option value="${billType.lookUpId}" code="${billType.lookUpId}">${billType.lookUpDesc}</form:option>
						</c:forEach>
					</form:select>
					</div>
					
				</div>

				<div class="form-group"></div>

					<div class="text-center">
						<input type="button" id="PrintButn" class="btn btn-blue-2"
							value="<spring:message code="account.financial.view.report" text="View Report" />" title="View Report"/>
							
							
						<button type="button" class="btn btn-warning resetSearch"
							onclick="window.location.href ='BillRegister.html'" title="Reset">
							<spring:message code="account.bankmaster.reset" text="Reset" />
						</button>
						<apptags:backButton url="AdminHome.html"></apptags:backButton>
					</div>
			</form:form>
		</div>
	</div>
</div>
