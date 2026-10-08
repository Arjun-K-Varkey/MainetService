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
	
	if ((rmRcptno == "" || rmRcptno == "0" || rmRcptno == undefined) && (loiNo == "" || loiNo == "0" || loiNo == undefined) ) {
		errorList.push(getLocalMessage("cfc.receipt.validation.deparment.recptNo"));
		displayErrorsOnPage(errorList);
	}
	

	if (errorList.length == 0){
	errorList = checkReceiptExists();
	}
	return errorList;
}

function ResetForm() {
	window.location.href = 'DuplicateBillReceipt.html';
}

function printReceipt(uri) {
	var requestdata = {};
	var url = uri + "?duplicateBillPrint";
	var returnData = __doAjaxRequest(url, 'POST', requestdata, false);
	window.open(returnData, '_blank');
	/*var title = 'Duplicate Bill Copy';
	var printWindow = window.open('', '_blank');
	printWindow.document.write('<html><head><title>' + title + '</title>');
	printWindow.document.write(returnData);
	printWindow.document.write('</body></html>');
	printWindow.document.close();*/

}

function printNewReceipt() {
	var URL = 'DuplicateBillReceipt.html?printNewBill';
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
	$("#postMethodForm").prop('action', 'DuplicateBillReceipt.html');
	$("#postMethodForm").submit();
}

function proceedToSaveDetails(obj,url) {
	var errorList = [];
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
		if (payModeIn != undefined && payModeIn != 'C') {
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
	
	if (errorList.length == 0) {
		/*var status = saveOrUpdateForm(obj,
			"Your application Data  saved successfully!!",
				'AdminHome.html', 'generateChallanAndPayement');*/	
		var status =  generateReport(obj , url, 'PrintReport') ;
		return status;

	}
	/*if (!$.trim($('#validationerrordiv').html()).length) {
		printNewReceipt();
	}
	if (!$.trim($('#validationerrordiv').html()).length) {
		printReceipt();
	}*/ else
		displayErrorsOnPage(errorList);
}

function getCharges() {
	var theForm = '#duplicateBillReceipt';
	var errorList = [];
	errorList = validateApplicantInfo(errorList);
	errorList = validateFormDetails(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	} else {
		var selectedBillText = $('#bill option:selected').text();
		var selectedBillValue = $('#bill option:selected').val();

		requestData = __serializeForm(theForm);
		var URL = 'DuplicateBillReceipt.html?getCharges';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
				'html'); 
		if (returnData) {
			$(formDivName).html(returnData);
			$('#bill option:selected').val(selectedBillValue);
			$('#bill option:selected').text(selectedBillText);
			prepareTags();
		}
	}
}

function backPage() {
	window.location.href = getLocalMessage("AdminHome.html");
}

function checkReceiptExists() {
	var errorList = [];
	var rmRcptno = $("#rmRcptno").val();
	var deptId = $("#deptId").val();
	var loiNo = $("#loiNo").val();
	var rmAmount = $("#rmAmount").val();
	var appNo = $("#appNo").val();
	var refNo = $("#refNo").val();
	var rmReceivedfrom = $("#rmReceivedfrom").val();
	var finYearId = $("#finYearId").val();
	
	var requestData = {
		"rmRcptno" : rmRcptno,
		"deptId" : deptId,
		"loiNo" : loiNo,
		"rmAmount" :rmAmount,
		"appNo": appNo,
		"refNo" : refNo,
		"rmReceivedfrom":rmReceivedfrom,
		 "finYearId"   :finYearId
	};
	if (errorList.length == 0) {
		var URL = 'DuplicateBillReceipt.html?checkIfBillExists';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
				'json');
		if (returnData == false) {
			errorList.push(getLocalMessage("cfc.valid.receiptno"));
			displayErrorsOnPage(errorList);
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

function getBillList(){
	var errorList = [];
	var connNo = $("#conNum").val();
	if(document.getElementById("bill").length == 1){
		if(connNo != ''){
			var data = {
			"csCcn": connNo,
			};	
		}else{
			errorList.push(getLocalMessage("water.ccn.empty"));
			displayErrorsOnPage(errorList);
			return;
		}
		var URL = 'DuplicateBillReceipt.html?getBillList';
		var ajaxResponse = __doAjaxRequest(URL, 'POST', data, false, 'json');
		console.log(ajaxResponse);
		if (ajaxResponse && ajaxResponse.length != 0) {
			var select = document.getElementById("bill");
		    for (var i = 0; i < ajaxResponse.length; i++) {
		        var optn = ajaxResponse[i];
		        var el = document.createElement("option");
				el.textContent = optn.bmFromdt +' to ' + optn.bmTodt + " (" + optn.bmNo + ")";
		        el.value = optn.bmNo;
		        select.appendChild(el);
			}
		}else{ 
			errorList.push("Connection number is not active or bills are not generated");
			displayErrorsOnPage(errorList);
		}	
	}
} 

function validateFormDetails(errorList){
	var connNo = $("#conNum").val();
	var billNo = $("#bill").val();
	var noOfCopies = $('#noOfCopies').val();
	
	if(connNo == "" || connNo == null || connNo == undefined){
		errorList.push(getLocalMessage("water.ccn.empty"));
		displayErrorsOnPage(errorList);
	}
	else if(billNo == "" || billNo == null || billNo == undefined){
		errorList.push(getLocalMessage("water.billNo.empty"));
		displayErrorsOnPage(errorList);
	}else if(noOfCopies == "" || noOfCopies == null || noOfCopies == undefined){
		errorList.push(getLocalMessage("water.noOfCopies.empty"));
		displayErrorsOnPage(errorList);
	}
	return errorList;
}
function validateMobile(mobile) {
	var regexPattern = /^[789]\d{9}$/;
	return regexPattern.test(mobile);
}
function validateEmail(email) {
	var regexPattern = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return regexPattern.test(email);
}
function validateApplicantInfo(errorList) {

	var applicantTitle = $.trim($('#applicantTitle').val());
	var firstName = $.trim($('#firstName').val());
	var lastName = $.trim($('#lastName').val());
	var applicantMobileNo = $.trim($('#mobileNo').val());
	var applicantAreaName = $.trim($('#areaName').val());
	var applicantPinCode = $.trim($('#pinCode').val());
	var applicantAdharNo = $.trim($('#adharNo').val());
	var applicantEmailId = $.trim($('#emailId').val());
	var dwzid1=$("#dwzid1").val();
	var dwzid2=$("#dwzid2").val();
	var dwzid3=$("#dwzid3").val();
	

	if (applicantTitle == "" || applicantTitle == '0'
			|| applicantTitle == undefined) {
		errorList.push(getLocalMessage('water.validation.ApplicantNameTitle'));
	}
	if (firstName == "" || firstName == undefined) {
		errorList.push(getLocalMessage('water.validation.ApplicantFirstName'));
	}
	if (lastName == "" || lastName == undefined) {
		errorList.push(getLocalMessage('water.validation.ApplicantLastName'));
	}
	if (applicantMobileNo == '' || applicantMobileNo == null
			|| applicantMobileNo == undefined) {
		errorList.push(getLocalMessage('water.validation.applicantMobileNo'));
	} else {
		if (!validateMobile(applicantMobileNo)) {
			errorList
					.push(getLocalMessage('water.validation.ApplicantInvalidmobile'));
		}
	}
	if (applicantEmailId != '' && applicantEmailId != null
			&& applicantEmailId != undefined) {
		if (!validateEmail(applicantEmailId)) {
			errorList.push(getLocalMessage('emailId.invalid'));
		}
	}
	if (applicantAreaName == "" || applicantAreaName == undefined) {
		errorList.push(getLocalMessage('water.validation.applicantarea'));
	}

	if (applicantPinCode == "" || applicantPinCode == undefined) {
		errorList.push(getLocalMessage('water.validation.applicantPinCode'));
	}
	if(dwzid1 == "" || dwzid1 == undefined || dwzid1 == "0"){
		errorList.push(getLocalMessage('water.validn.selectDivision'));
	}
	if(dwzid2 == "" || dwzid2 == undefined || dwzid2 == "0"){
		errorList.push(getLocalMessage('water.validation.seleWard'));
	}
	if(dwzid3 == "" || dwzid3 == undefined || dwzid3 == "0"){
		errorList.push(getLocalMessage('water.validation.selZone'));
	}
	return errorList;
}
function generateReport(element,url,methodName) {	
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = {};
	requestData = __serializeForm(theForm);

	var ajaxResponse = __doAjaxRequest(url + '?generateChallanAndPayement', 'POST', requestData,
			false, '', element);
	if ($.isPlainObject(ajaxResponse)) {
		displayMessageOnSubmit(ajaxResponse.command.message, url, methodName);
	}else if (typeof(ajaxResponse) === "string") {
		$('.content-page').html(ajaxResponse);	
	}

}
function displayMessageOnSubmit(message ,url, methodName) {
	window.open(url + '?' + methodName,'_blank');
	var errMsgDiv = '.msg-dialog-box';
	var cls = getLocalMessage("unit.proceed");

	var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message
			+ '</h5>';
	d += '<div class=\'text-center\'><input type=\'button\' class= "btn btn-success" value=\''
			+ cls + '\'  id=\'btnNo\' onclick="printCertificate(\''+url+ '\')"/></div></br>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(d);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showPopUpMsg(errMsgDiv);
}
function showPopUpMsg(childDialog) {
	$.fancybox({
		type : 'inline',
		href : childDialog,
		openEffect : 'elastic',
		closeBtn : false,
		helpers : {
			overlay : {
				closeClick : false
			}
		},
		keys : {
			close : null
		}
	});
	return false;
}
function printCertificate(uri){
	var requestData = {};
	var url = uri + "?getAcknowledement";
	var ajaxResponse = __doAjaxRequest(url, 'POST', requestData,false);
	var divName = formDivName;
	$('.content').removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	$.fancybox.close();
	printReceipt(uri);
}