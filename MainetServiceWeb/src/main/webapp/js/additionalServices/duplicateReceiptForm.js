$(document).ready(function() {

	$("#rmDate").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true
	});

	$("#rmDate").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

	$("#receiptDatatables").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	$(".connectionNo").hide();
	$("#propNo").hide();
	$("#hideReceiptNo").show();
	$("#showReceiptNo").hide();
	$("#showFlatNo").hide();
	
	$("select#flatNo").on('change', function(){
		$('select#flatNo').chosen('destroy');  
		getReceiptId();
		$('select#flatNo').chosen();
	});
});

function validateForm() {
	var errorList = [];
	var titleId = $("#titleId").val();
	var firstName = $("#firstName").val();
	var lName = $("#lName").val();
	var gender = $("#gender").val();
	var mobNumber = $("#mobNumber").val();
	var apaAreanm = $("#apaAreanm").val();
	var deptId = $("#deptId").val();
	var rmRcptno = $("#rmRcptno").val();
	var loiNo = $("#loiNo").val();
	var pinCode =$("#pinCode").val();
	var finYearId =$("#finYearId").val();
	var receiptNoList = $(".receiptNoList").val();
	

	if (titleId == "0" || titleId == "" || titleId == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.title'));
	}
	if (firstName == "" || firstName == null || firstName == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.fname'));
	}
	if (lName == "" || lName == null || lName == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.lname'));
	}
	if (gender == "" || gender == 0 || gender == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.gender'));
	}
	if (mobNumber == "" || mobNumber == null || mobNumber == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.mbno'));
	}
	if (apaAreanm == "" || apaAreanm == null || apaAreanm == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.address'));
	}
	if (deptId == "" || deptId == 0 || deptId == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.deparment'));
	}
	if (finYearId == "" || finYearId == 0 || finYearId == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.finYearId'));
	}
	if (pinCode != ""){
	var pattern=/^(?!0{6})[A-Z0-9][0-9]{5}$/;
	if (!pattern.test(pinCode)) {
		errorList.push(getLocalMessage("NHP.validation.pinCode.valid"));
     }
	}
	/*if (rmRcptno == "" || rmRcptno == 0 || rmRcptno == undefined) {
		errorList
				.push(getLocalMessage('cfc.receipt.validation.deparment.recptNo'));
	}*/
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="WT" || deptCode=="AS"){
		if ((receiptNoList == "" || receiptNoList == "0" || receiptNoList == undefined) && (loiNo == "" || loiNo == "0" || loiNo == undefined) ) {
			errorList.push(getLocalMessage("cfc.receipt.validation.deparment.recptNo"));
			displayErrorsOnPage(errorList);
		}
	}else{
		if ((rmRcptno == "" || rmRcptno == "0" || rmRcptno == undefined) && (loiNo == "" || loiNo == "0" || loiNo == undefined) ) {
			errorList.push(getLocalMessage("cfc.receipt.validation.deparment.recptNo"));
			displayErrorsOnPage(errorList);
		}
	}

	if (errorList.length == 0){
	errorList = checkReceiptExists();
	}
	return errorList;
}

function ResetForm() {
	window.location.href = 'DuplicatePaymentReceipt.html';
}

function printReceipt(rmRcptid) {
	var URL = 'DuplicatePaymentReceipt.html?printReceipt';
	var requestdata = {
		"rmRcptid" : rmRcptid
	};
	var returnData = __doAjaxRequest(URL, 'POST', requestdata, false);

	var title = 'Duplicate Receipt Copy';
	var printWindow = window.open('', '_blank');
	printWindow.document.write('<html><head><title>' + title + '</title>');
	printWindow.document.write(returnData);
	printWindow.document.write('</body></html>');
	printWindow.document.close();

}

function printNewReceipt() {
	var URL = 'DuplicatePaymentReceipt.html?printNewReceipt';
	var requestdata = {};
	var returnData = __doAjaxRequest(URL, 'POST', requestdata, false);
	var title = 'Current Receipt Copy';
	var printWindow = window.open('', '_blank');
	printWindow.document.write('<html><head><title>' + title + '</title>');
	printWindow.document.write(returnData);
	printWindow.document.write('</body></html>');
	printWindow.document.close();

}
function back() {
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', 'DuplicatePaymentReceipt.html');
	$("#postMethodForm").submit();
}

function proceedToSaveDetails(obj) {
    $('#validationerrordiv').html('');
	$('#validationerrordiv').hide();
	$('#errorDivId').hide();
	var errorList = [];
	var paymentCheck = $("#paymentCheck").val();
	var deptId = $("#deptId").val();
	var loiNo = $("#loiNo").val();
	var rmAmount = $("#rmAmount").val();
	var appNo = $("#appNo").val();
	var refNo = $("#refNo").val();
	var rmReceivedfrom = $("#rmReceivedfrom").val();
	var finYearId = $("#finYearId").val();
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="WT" || deptCode=="AS"){
		var rmRcptno = $(".receiptNoList").val();
	}else{
		var rmRcptno = $("#rmRcptno").val();
	}
	var addRefNo=$("#propertyNo").val();	
	if (deptId !== "" && (rmRcptno != "" || loiNo !="")) {
		var requestData = {
			"rmRcptno" : rmRcptno,
			"deptId" : deptId,
			"loiNo" : loiNo,
			"rmAmount" :rmAmount,
			"appNo": appNo,
			"refNo" : refNo,
			"rmReceivedfrom":rmReceivedfrom,
             "finYearId"   :finYearId,
    		 "addRefNo":addRefNo
		};
		var URL = 'DuplicatePaymentReceipt.html?getReceiptId';
		var rmRcptid = __doAjaxRequest(URL, 'POST', requestData, false, 'json');
	}
	if (paymentCheck == 'Y') {
		if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
				":checked").val() != 'N'
				&& $("input:radio[name='offlineDTO.onlineOfflineCheck']")
						.filter(":checked").val() != 'Y'
				&& $("input:radio[name='offlineDTO.onlineOfflineCheck']")
						.filter(":checked").val() != 'P') {

			errorList.push(getLocalMessage("NHP.validation.payment.mode"));
			displayErrorsOnPage(errorList);
		}

		if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
				":checked").val() == 'P') {
			var payModeIn = $("#payModeIn").val();
			if (payModeIn == "0") {
				errorList
						.push(getLocalMessage("NHP.validation.colllection.mode"));
			}
			var payModeIn = $('#payModeIn option:selected').attr('code');
			if (payModeIn != undefined &&( payModeIn != 'C' && payModeIn!='POS')) {
				if (payModeIn == "D") {
					if (!validateChkNo($("#chqNo").val())) {
						errorList.push(getLocalMessage("NHP.validation.DD.no"));
					}
				} else {
					if (!validateChkNo($("#chqNo").val())) {
						errorList
								.push(getLocalMessage("NHP.validation.chk.no"));
					}
				}

				if (!validateAcNo($("#acNo").val())) {
					errorList
							.push(getLocalMessage("NHP.validation.account.no"));
				}
			}

		}
	}
	if (errorList.length == 0) {
		if (paymentCheck == 'Y')
			var status = saveOrUpdateForm(obj,
					"Your application Data  saved successfully!!",
					'AdminHome.html', 'generateChallanAndPayement');
		            dupPayReceiptAcknow();
	}
	if (!$.trim($('#validationerrordiv').html()).length) {
		printNewReceipt();
	}
	if (!$.trim($('#validationerrordiv').html()).length) {
		printReceipt(rmRcptid);
	} else
		displayErrorsOnPage(errorList);

}

function getCharges(obj) {
	var theForm = '#duplicatePaymentReceipt';
	var errorList = [];
	errorList = validateForm();
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	} else {
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'DuplicatePaymentReceipt.html?getCharges';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
				'html');
		if (returnData != null) {
			$(formDivName).html(returnData);
			prepareTags();
		}
	}
}

function backPage() {
	window.location.href = getLocalMessage("AdminHome.html");
}

function checkReceiptExists() {
	
	var errorList = [];
	var deptId = $("#deptId").val();
	var loiNo = $("#loiNo").val();
	var rmAmount = $("#rmAmount").val();
	var appNo = $("#appNo").val();
	var refNo = $("#refNo").val();
	var rmReceivedfrom = $("#rmReceivedfrom").val();
	var finYearId = $("#finYearId").val();
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="WT" || deptCode=="AS"){
		var rmRcptno = $(".receiptNoList").val();
	}else{
		var rmRcptno = $("#rmRcptno").val();
	}

	var addRefNo=$("#propertyNo").val();	
	
	var requestData = {
		"rmRcptno" : rmRcptno,
		"deptId" : deptId,
		"loiNo" : loiNo,
		"rmAmount" :rmAmount,
		"appNo": appNo,
		"refNo" : refNo,
		"rmReceivedfrom":rmReceivedfrom,
		 "finYearId":finYearId,
		 "addRefNo":addRefNo
	};
	if (errorList.length == 0) {
		var URL = 'DuplicatePaymentReceipt.html?checkReceiptExists';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
				'json');
		if (returnData == false) {
			errorList.push(getLocalMessage("cfc.valid.receiptno"));
			displayErrorsOnPage(errorList);
			$("#rmRcptno").val("");
			$("#loiNo").val("");
			
		}
	}
	return errorList;
}
function validateChkNo(chkNo) {
	
	var regexPattern = /^[0-9]\d{9}$/;
	return regexPattern.test(chkNo);
}

function validateAcNo(acNo) {
	
	var regexPattern = /^[0-9]\d{15}$/;
	return regexPattern.test(acNo);
}

function dupPayReceiptAcknow() {
	
	if (!$.trim($('#validationerrordiv').html()).length) {
	var URL = 'DuplicatePaymentReceipt.html?printCFCAckRcpt';
	var returnData = __doAjaxRequest(URL, 'POST', {}, false);
	
	var appId = $($.parseHTML(returnData)).find("#applicationId").html();
	var title = appId;
	prepareTags();
	var printWindow = window.open('', '_blank');
	printWindow.document.write('<html><head><title>' + title + '</title>');
	printWindow.document
			.write('<html><link href="assets/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<link href="assets/libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" />')
	printWindow.document
			.write('<link href="assets/css/style.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<link href="assets/css/style-responsive.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>')
	printWindow.document
			.write('<script src="js/mainet/ui/jquery.min.js"></script>')
	printWindow.document
			.write('<script type="text/javascript" src="assets/libs/excel-export/excel-export.js"></script>')
	printWindow.document
			.write('<script>$(window).on("load",function() {$(".table-pagination, .remove-btn, .paging-nav, tfoot").remove(); $(".table thead tr th").removeClass("tablesorter-headerDesc tablesorter-headerAsc tablesorter-header"); $(".table tr").removeAttr("style");});</script>')
	printWindow.document.write('</head><body style="background:#fff;">');
	printWindow.document.write(returnData);
	printWindow.document.write('</body></html>');
	printWindow.document.close();
}
}

function fetchDept(){
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="WT"){
		$(".connectionNo").show();
		$("#hideReceiptNo").hide();
		$("#showReceiptNo").show();
	}
	if(deptCode=="AS"){
		$("#propNo").show();
		$("#hideReceiptNo").hide();
		$("#showReceiptNo").show();
	}
}
function getReceiptId(){
	var errorList = [];
	var deptId = $("#deptId").val();
	var finYear = $("#finYearId").val();
	var connectionNo = $("#connectionNo").val();
	var propertyNo = $("#propertyNo").val();
	var flatNo = $("#flatNo").val();
	if (finYearId == "" || finYearId == 0 || finYearId == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.finYearId'));
	}
	if (deptId == "" || deptId == 0 || deptId == undefined) {
		errorList.push(getLocalMessage('cfc.receipt.validation.deparment'));
	}
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="WT"){
		if (connectionNo == "" || connectionNo == null || connectionNo == undefined) {
			errorList.push(getLocalMessage('connection no must not be empty'));
		}
	}	
	if(deptCode=="AS"){
		if (propertyNo == "" || propertyNo == null || propertyNo == undefined) {
			errorList.push(getLocalMessage('Property no must not be empty'));
		}
	}	
	if (errorList.length == 0){
		var requestData = {
				"finYear" : finYear,
				"connectionNo": connectionNo,
				"propertyNo":propertyNo,
				"deptCode":deptCode,
				"flatNo":flatNo
			    };
	
		var ajaxResponse = __doAjaxRequest('DuplicatePaymentReceipt.html?getReceiptIdList', 'POST', requestData, false,
		'html');
		if (ajaxResponse != null && ajaxResponse != "") {
	    	var prePopulate = JSON.parse(ajaxResponse);
	    	$.each(prePopulate, function(index, value) {
	    		$('.receiptNoList').append(
	    				$("<option></option>").attr("value", value).text(
	    						(value)));
	    	});
	    	$('.receiptNoList').trigger("chosen:updated");
	    }
	}
	else{
		displayErrorsOnPage(errorList);
		return false;
	}
	 
	
}
function getFlatNo(){
	var errorList = [];
	var propertyNo = $("#propertyNo").val();
	var deptCode = $('#deptId option:selected').attr('code');
	if(deptCode=="AS"){
		if (propertyNo == "" || propertyNo == null || propertyNo == undefined) {
			errorList.push(getLocalMessage('Property no must not be empty'));
		}
	}
	if (errorList.length == 0){
		    var requestData = {
			"propertyNo" : propertyNo
		    };
		    var ajaxResponse = doAjaxLoading(
			    'DuplicatePaymentReceipt.html?getBillingMethod',
			    requestData, 'html');

		    if (ajaxResponse != null && ajaxResponse != "") {
		    	var prePopulate = JSON.parse(ajaxResponse);

		    	$.each(prePopulate, function(index, value) {
		    		$('#flatNo').append(
		    				$("<option></option>").attr("value", value).text(
		    						(value)));
		    	});
		    	$('#flatNo').trigger("chosen:updated");
		    	  $("#showFlatNo").show();
		    	  $("#billingMethod").val('I');
		    }else{
		    	getReceiptId();
		    } 
	}else{
		displayErrorsOnPage(errorList);
		return false;
	}
}