$(document).ready(function() {
	$(function() {

		$("#id_customerTable").dataTable({
			"oLanguage" : {
				"sSearch" : ""
			},
			"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
			"iDisplayLength" : 5,
			"bInfo" : true,
			"lengthChange" : true,
			"ordering" : false,
			"order" : [ [ 1, "desc" ] ]
		});

	});
});

function openaddcustomerFrom(formUrl, actionParam) {
	
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, {}, 'html',
			divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function Proceed(element) {
	var errorList = [];
	errorList = ValidatecustomerMasterForm(errorList);
	if (errorList.length > 0) {
		$("#errorDiv").show();
		displayErrorsOnPage(errorList);

	} else {
		return saveOrUpdateForm(element,
				getLocalMessage('master.form.submit.success'),
				'CustomerMaster.html', 'saveform');
	}

}

function ValidatecustomerMasterForm(errorList) {
	
	var custType = $("#custType").val();
	var custName = $("#custName").val();
	var custAddress = $("#custAddress").val();
	var custMobNo = $("#custMobNo").val();
	var custGSTNo = $("#custGSTNo").val();
	var custTINNo = $("#custTINNo").val();
	var custUIDNo = $("#custUIDNo").val();
	var custEmailId = $("#custEmailId").val();

	if (custType == "" || custType == "0" || custType == null)
		errorList.push(getLocalMessage("accounts.customerMaster.error.custType"));
	if (custName == "" || custName == null)
		errorList.push(getLocalMessage("accounts.customerMaster.error.custName"));
	if (custAddress == "" || custAddress == null)
		errorList.push(getLocalMessage("accounts.customerMaster.error.custAddress"));
	if (custTINNo == "" || custTINNo == null)
		errorList.push(getLocalMessage("accounts.customerMaster.error.custTinNo"));

	if (custTINNo != "") {
		var regexPattern = /\d{10}$/;
		if (!regexPattern.test(custTINNo) && custTINNo != '') {
			errorList.push(getLocalMessage("accounts.customerMaster.error.validCustTinNo"));
		}
	}
	if (custMobNo != "") {
		if (!validateMobile(custMobNo)) {
			errorList.push(getLocalMessage('accounts.customerMaster.error.validCustMobNo'));
		}
	}
	if (custGSTNo != "") {
		var regExGst = /^([0-9]){2}([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}([0-9]){1}([Z]){1}([0-9|a-zA-Z]){1}?$/;
		if (!regExGst.test(custGSTNo) && custGSTNo != '') {
			errorList.push(getLocalMessage("accounts.customerMaster.error.validCustGstNo"));
		}
	}

	if (custUIDNo != "") {
		var regexPattern = /\d{12}$/;
		if (!regexPattern.test(custUIDNo) && custUIDNo != '') {
			errorList.push(getLocalMessage("accounts.customerMaster.error.validCustUidNo"));
		}
	}
	if (custEmailId != "") {
		var regexPattern = /\S+@\S+\.\S+/;;
		if (!regexPattern.test(custEmailId) && custEmailId != '') {
			errorList.push(getLocalMessage("accounts.customerMaster.error.validCustEmail"));
		}
	}
	if($("#viewMode").val() == "E"){
		if($("#remark").val()==""){
			errorList.push(getLocalMessage("accounts.customerMaster.validation.remark"));
		}
	}
	
	return errorList

}

function validateMobile(mobile) {
	var regexPattern = /^[789]\d{9}$/;
	return regexPattern.test(mobile);
}

function modifyCustomer(custId, formUrl, actionParam) {
	
	var divName = '.content-page';
	var requestData = {
		"id" : custId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
};
function searchCustomer(formUrl, actionParam) {
	
	var custName = $('#custName').val();
	var custAddress = $('#custAddress').val();

	var data = {
		"custName" : custName,
		"custAddress" : custAddress
	}
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, data, 'html',
			divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	$('#custAddress').val(custAddress);
	$('#custName').val(custName);
	prepareTags();
}

function fnValidatePAN(element) {
	
	$('.error-div').hide();
	var errorList = [];

	Obj = $('#custPANNo').val();

	var panPat = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
	var code = /([C,P,H,F,A,T,B,L,J,G])/;
	var code_chk = Obj.substring(3, 4);
	if (Obj.search(panPat) == -1) {
		errorList.push('Invaild PAN Number');
		$('#custPANNo').val("");
	}

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
};

// /////////////////////////////////////////////////////////////////////////////////////////////
// # FILE UPLOAD

var UPLOADURL = "CustomerMaster.html"

function downloadTamplate() {
	
	window.location.href = UPLOADURL + "?ExcelTemplateData";
}

function uploadExcelFile() {
	
	var errorLis = [];

	var fileName = $("#uploadFileName").val().replace(/C:\\fakepath\\/i, '');
	if (fileName == null || fileName == "") {
		errorLis.push(getLocalMessage("excel.upload.vldn.error"));
		showErr(errorLis);
		return false;
	}
	$("#filePath").val(fileName);
	var requestData = $.param($('#wmsMaterialMaster').serializeArray())
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading('CustomerMaster.html?' + "loadExcelData",
			{}, 'html');
	$('.content').removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	if ($("#validationerror_errorslist").size() == 0) {
		showSaveResultBox1("CustomerMaster.html");
	}
	
	prepareTags();

}
function showSaveResultBox1(redirectUrl) {
	var messageText = getLocalMessage("swm.population.excel.upload.success.message");
	var message = '';
	var cls = getLocalMessage('eip.page.process');
	message += '<p class="text-blue-2 text-center padding-15">' + messageText
			+ '</p>';
	message += '<div class=\'text-center padding-bottom-10\'>'
			+ '<input type=\'button\' value=\'' + cls
			+ '\'  id=\'btnNo\' class=\'btn btn-success\'    '
			+ ' onclick="closebox(\'' + errMsgDiv + '\',\'' + redirectUrl
			+ '\')"/>' + '</div>';
	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBox(errMsgDiv);
	return false;
}


function redirectToHomePage() {
	// $.fancybox.close();
	window.location.href = 'CustomerMaster.html';
}

