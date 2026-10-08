<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>

<script>

$(document).ready(function() {

	$("#mappingsubmit").click(function() {
	var errorList = [];
	var receiptIds = [];
	var contIds = [];
	var receiptNos = [];
	var tdsAmounts = [];
	var receiptDates = [];
	var propertyNos = [];
	var tdsPercentage = [];
	var challanNos = [];
	
	var isValid = true;

	$(".receiptCheckbox:checked").each(function() {
	
		 var row = $(this).closest("tr");
		    var challanNo = $.trim(row.find(".challanNo").val());

		    if(challanNo == ""){
		        errorList.push("Please enter Challan No.");
		        displayErrorsOnPage(errorList);
		        isValid = false;
		        return false;
		    }

		    challanNos.push(challanNo);
		
		receiptIds.push($(this).data("receiptid"));
		contIds.push($(this).data("contid"));
		receiptNos.push($(this).data("receiptno"));
		tdsAmounts.push($(this).data("tdsamount"));
		receiptDates.push($(this).data("receiptdate"));
		propertyNos.push($(this).data("propertyno"));
		tdsPercentage.push($(this).data("tds-percentage"));

	});
	
	if (!isValid) {
	    return false;
	}
	

		if (receiptIds.length == 0) {

			errorList.push(getLocalMessage("Please select at least one checkbox"));
			displayErrorsOnPage(errorList);
			/* alert("Please select at least one checkbox"); */

			return false;
		}

		var requestData = {

			    receiptIds : receiptIds,
			    contIds : contIds,
			    receiptNos : receiptNos,
			    tdsAmounts : tdsAmounts,
			    receiptDates : receiptDates,
			    propertyNos : propertyNos,
			    tdsPercentage : tdsPercentage,
			    challanNos : challanNos

			};

		var ajaxResponse = __doAjaxRequest(
				'EstateContractBillPayment.html?updateTdsAmountZero',
				'POST',
				requestData,
				false,
				'text');
		
		ajaxResponse = ajaxResponse.replace(/"/g, '').trim();

		if (ajaxResponse == "true") {
			showBoxForApproval()

			/* alert("Selected Receipt IDs Updated Successfully");

			location.reload(); */

		} else {

			errorList.push(getLocalMessage("Failed To Update Data"));
			displayErrorsOnPage(errorList);
			/* alert("Failed To Update Data"); */
		}

	});

	function showBoxForApproval() {

		var childDivName = '.msg-dialog-box';
		var message = '';
		var Proceed = getLocalMessage("mrm.proceed");
		var succesMessage= 'TDS Challan Submitted Successfully';
		var no = 'No';
		message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
				+ '</p>';

		
			var redirectUrl = 'EstateContractBillPayment.html?PrintReport';
			message += '<div class=\'text-center padding-bottom-10\'>'
					+ '<input type=\'button\' value=\'' + 'Proceed'
					+ '\'  id=\'btnNo\' class=\'btn btn-success\'    '
					+ ' onclick="closebox(\'' + errMsgDiv + '\',\'' + redirectUrl
					+ '\')"/>' + '</div>';
			

		$(childDivName).addClass('ok-msg').removeClass('warn-msg');
		$(childDivName).html(message);
		$(childDivName).show();
		$('#Proceed').focus();
		showModalBoxWithoutClose(childDivName);
	}

	function closeApproval() {
		window.location.href = 'AdminHome.html';
		$.fancybox.close();
	}

});

</script>
<apptags:breadcrumb></apptags:breadcrumb>



<!-- <div class="content animated slideInDown"> -->
<div class="content animated">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="rnl.estateTds.heading" text="Estate Tds Challan"></spring:message>
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a>
			</div>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="rnl.book.field" text="Field with"></spring:message>
					<i class="text-red-1">*</i>
				<spring:message code="master.estate.field.mandatory.message"
						text="is mandatory"></spring:message> </span>
			</div>
			<div class="error-div alert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<ul>
					<li><label id="errorId"></label></li>
				</ul>
			</div>
			<form:form method="post" action="EstateContractBillPayment.html"
				class="form-horizontal" name="mappingForm" id="mappingForm">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="table-responsive margin-bottom-10">
					<table class="table table-striped table-bordered" id="datatables1">
						<thead>
							<tr>
							<th><spring:message code=""
										text="select Chebox"></spring:message></th>
								<th><spring:message code="rnl.estate.tds.contId"
										text="Contract Id."></spring:message></th>
								<th><spring:message code="rnl.estateTds.recptId" text="Receipt Id"></spring:message></th>
								<th><spring:message code="rnl.estateTds.receiptNo" text="Receipt No."></spring:message></th>
								<th><spring:message code="rnl.estateTds" text="Tds Amount"></spring:message></th>
								<th><spring:message code="rnl.estateTds.receiptDate" text="Receipt Date"></spring:message></th>
								<th><spring:message code="rnl.estateTds.percentage" text="Tds Percentage"></spring:message></th>
								<th><spring:message code="rnl.master.property" text="Property No."></spring:message></th>
								<th><spring:message code="" text="Challan No."></spring:message></th>

							</tr>
						</thead>
						<tbody id="propertyListId">
							<c:forEach items="${command.tdsHistoryDTO}" var="data"
								varStatus="count">
								<tr>
									<td class="text-center"><input type="checkbox"
										class="receiptCheckbox" data-receiptid="${data.receiptId}"
										data-contid="${data.contId}"
										data-receiptno="${data.receiptNo}"
										data-tdsamount="${data.tdsAmount}"
										data-receiptdate="${data.receiptDate}"
										data-propertyno="${data.propertyNo}"
										data-tds-percentage="${data.tdsPercentage}"></td>
									<td>${data.contId}</td>
									<td>${data.receiptId}</td>
									<td>${data.receiptNo}</td>
									<td>${data.tdsAmount}</td>
									<td>${data.receiptDate}</td>
									<td>${data.tdsPercentage}</td>
									<td>${data.propertyNo}</td>

									<td><input type="text" class="form-control challanNo"
										name="challanNo" maxlength="50" /></td>

								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>

				<div class="text-center padding-top-10">
					<div class="padding-top-10 text-center">
						<button type="button" class="btn btn-success btn-submit"
							id="mappingsubmit">
							<spring:message code="bt.save" text="Save" />
						</button>
						<input type="button" id="backBtn" class="btn btn-danger"
							onclick="back()" value="<spring:message code="bt.backBtn"/>" />
					</div>

				</div>
			</form:form>
		</div>
	</div>
	<!-- End of info box -->

	<!--Wedgets Start Here-->

	<!--Wedgets End Here-->
</div>