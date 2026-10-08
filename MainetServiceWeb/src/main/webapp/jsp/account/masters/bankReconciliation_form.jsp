<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script src="js/account/bankReconciliation.js"></script>
  <script type="text/javascript" src="js/mainet/file-upload.js"></script>  

<script>
	$(document).ready(function(){
		var val = $('#keyTest').val();
		if (val != '' && val != undefined) {
			displayMessageOnSubmit(val);
		
			
	}	
		var table = $('.main1').DataTable({
			"oLanguage": { "sSearch": "" } ,
			"aLengthMenu": [ [5, 10, 15, -1], [5, 10, 15, "All"] ],
		    "iDisplayLength" : 5, 
		    "bInfo" : true,
		    "lengthChange": true,
		    "bPaginate": true,
		    "bFilter": true,
		    "ordering":  false,
		    "order": [[ 1, "desc" ]]
		    });
		
		
	});
	
</script>
 <%-- <c:if test="${tbBankReconciliation.hasError =='true'}">
	<apptags:breadcrumb></apptags:breadcrumb>
</c:if> --%>

<script>
 $('#sacHeadId${count}').val($('#secondaryId').val());
</script>
<div class="content">
	<form:form class="form-horizontal"
		modelAttribute="tbBankReconciliation" method="POST"
		action="BankReconciliation.html">

		<form:hidden path="index" id="index" />
		<form:hidden path="" value="${keyTest}" id="keyTest" />
		<form:hidden path="" value="${categoryType}" id="categoryType" />
		<form:hidden path="" value="${categoryTypeDesc}" id="categoryTypeDesc" />

		<div class="mand-label clearfix">
			<span><spring:message code="account.common.mandmsg" /> <i
				class="text-red-1">*</i> <spring:message
					code="account.common.mandmsg1" /></span>
		</div>
		<form:hidden path="hasError" />
		<form:hidden path="alreadyExists" id="alreadyExists"></form:hidden>
		<form:hidden path="" id="hiddenFinYear"></form:hidden>
		<form:hidden path="" id="cpdBugtypeIdHidden" />
		<div class="warning-div alert alert-danger alert-dismissible hide"
			id="clientSideErrorDivScrutiny"></div>
		<div class="error-div alert alert-danger alert-dismissible"
			id="errorDivId" style="display: none;">
			<button type="button" class="close" onclick="closeOutErrBox()"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<span id="errorId"></span>
		</div>

		<jsp:include page="/jsp/tiles/validationerror.jsp" />
		<c:set var="count" value="0" scope="page" />
		<ul id="ulId">
			<li>

				<fieldset id="divId" class="clear"></fieldset> <input type="hidden"
				value="${viewMode}" id="test" />

				<div class="panel-group accordion-toggle" id="ledgerDetailIds">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 type="h4" class="panel-title table" id="">
								<a data-toggle="collapse" class="collapsed"
									data-parent="#accordion_single_collapse" href="#a2"><spring:message code="account.banck.reconciliation" text="Bank
									Reconciliation Details" />: ${categoryTypeDesc} : ${bankTypeDesc}
								</a>
							</h4>
						</div>
						<div class="table-responsive clear">
							<table class="table table-bordered table-striped main1"
								id="bankrec">
								<thead>
									<tr class="bankRecTable">
										<th><spring:message code="account.transaction.date" text="Transaction Date" /></th>
										<th><spring:message code="account.cheque.cash.transaction" text="Transaction No." /></th>
										<th><spring:message code="account.transaction.mode" text="Transaction Mode" /></th>
										<th><spring:message code="account.transaction.type" text="Transaction Type" /></th>
										<th><spring:message code="account.instrument.number" text="Instrument Number" /></th>
										<th><spring:message code="account.instrument.date" text="Instrument Date" /></th>
										<th><spring:message code="account.amount" text="Amount" /></th>
										<th class="clearance" style="display: none;"><spring:message text="Clearance Date" /><span class="mand">*</span></th>
										<th class="deposite" style="display: none;"><spring:message text="Deposite Date" /><span class="mand">*</span></th>
										<th class="dishonored" style="display: none;"><spring:message text="Dishonored Date" /><span class="mand">*</span></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach
										items="${tbBankReconciliation.bankReconciliationDTO}"
										var="funMasterLevel" varStatus="status">
										<c:set value="${status.index}" var="count"></c:set>
										<tr class="chequeTr">
											<form:hidden
												path="bankReconciliationDTO[${count}].transactionMode"
												id="transactionMode${count}" />
											<form:hidden path="bankReconciliationDTO[${count}].serchType"
												id="serchType${count}" />
											<form:hidden
												path="bankReconciliationDTO[${count}].depositSlipId"
												id="depositSlipId${count}" />
											<form:hidden path="bankReconciliationDTO[${count}].id"
												id="id${count}" />
											<form:hidden
												path="bankReconciliationDTO[${count}].previousDate"
												id="previousDate${count}" />
											<td><form:input
													path="bankReconciliationDTO[${count}].transactionDate"
													id="transactionDate${count}" class=" form-control "
													readonly="true" name="text-01" /></td>
											<td><form:input
													path="bankReconciliationDTO[${count}].transactionNo"
													id="transactionNo${count}" class=" form-control "
													readonly="true" name="text-01" /></td>
											<td><form:input
													path="bankReconciliationDTO[${count}].transMode"
													id="transMode${count}" class=" form-control "
													readonly="true" name="text-01" /></td>
											<td><form:input
													path="bankReconciliationDTO[${count}].transType"
													id="transType${count}" class=" form-control "
													readonly="true" name="text-01" /></td>
											<td><form:input
													path="bankReconciliationDTO[${count}].chequeddno"
													id="transactionNo${count}" class=" form-control "
													readonly="true" name="text-01" /></td>

											<td><form:input
													path="bankReconciliationDTO[${count}].chequedddate"
													id="transactionNo${count}" class=" form-control "
													readonly="true" name="text-01" /></td>

											<td class="text-right"><form:input type="text"
													cssClass="form-control text-right"
													path="bankReconciliationDTO[${count}].amount"
													id="amount${count}" readonly="true" /></td>

											<td><form:input
													path="bankReconciliationDTO[${count}].date"
													id="date${count}"
													cssClass="form-control mandColorClass datepicker"
													onchange="changeValidClearanceDate(${count})"
													maxlength="10" /></td>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<c:if test="${tbBankReconciliation.balanceAsUlb ne '0.00' }">
										<tr>
											<th colspan="2" style="text-align: left"><spring:message code="account.balance.as.per.ulb" text="Balance As Per ULB Book"></spring:message></th>
											<th colspan="2"><form:input class="form-control"
													path="balanceAsUlb" id="balanceUlbId" readonly="true"
													style="text-align:right;" /></th>
											<th colspan="2" style="text-align: left"><spring:message code="account.balance.as.per.bank" text="Bank Balance As Per Bank Statement"></spring:message></th>
											<th colspan="2"><form:input class="form-control" path="bankBalanceAsperStatement" id="statementId" readonly="true" style="text-align:right" /></th>
										</tr>
									</c:if>
								</tfoot>
							</table>
						</div>
					</div>
				</div>

			</li>
		</ul>
		<input type="hidden" id="count" value="0" />
		
			<div class="form-group text-center" id="reload">
					<label class="col-sm-3 control-label" for="ExcelFileUpload"><spring:message
							code="excel.file.upload" text="Excel File Upload" /></label>
					<div class="col-sm-3 text-left">
						<apptags:formField fieldPath="uploadFileName"
							showFileNameHTMLId="true" fileSize="WORK_COMMON_MAX_SIZE"
							maxFileCount="CHECK_LIST_MAX_COUNT"
							validnFunction="EXCEL_IMPORT_VALIDATION_EXTENSION"
							currentCount="0" fieldType="7">
						</apptags:formField>
						<small class="text-blue-2">	<spring:message code="account.secondaryupload.UploadExcelmb" text="(Upload Excel upto 5MB )" /></small>
					</div>
					<label class="col-sm-3 control-label" for="ExportDocument"><spring:message
							code="excel.template" text="Excel Template" /></label>
					<div class="col-sm-3 text-left">
						<button type="button" class="btn btn-success save"
							name="button-Cancel" value="export" onclick="downloadTamplate();"
							id="import">
							<spring:message code="account.secondaryupload.dwntmplate" text="Download Template" />
						</button>
					</div>
					</div>
		<div class="text-center padding-top-10">
			<c:if test="${categoryType == 'N'}">
				<input type="button" id="saveBtn" class="btn btn-success btn-submit"
					onclick="saveLeveledData(this)" value="<spring:message
					code="accounts.receipt.save" text="Save" />"> </input>
			</c:if>
			<spring:url var="cancelButtonURL" value="BankReconciliation.html" />
			<a role="button" class="btn btn-danger" href="${cancelButtonURL}"><spring:message
					code="account.bankmaster.back" text="Back" /></a>
		</div>
	</form:form>
</div>

