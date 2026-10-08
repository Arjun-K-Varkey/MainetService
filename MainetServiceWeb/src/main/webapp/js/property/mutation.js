$(document).ready(function() {
	// datepicker function for date of effect
	$('.lessthancurrdate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "-100:-0",
	});
	yearLength();
	$("#showFlatNo").hide();
	$("#serchBtnDirect").hide();
	$("#showFlatNoForPrint").hide();
	$("#showSelectButton").hide();
	
	/*
	 * var locId=$("#locId").val(); if(locId!=null && locId!='' &&
	 * locId!=undefined && locId>0){ var result=
	 * getOperationalWardZone($("#orgId").val(),$("#deptId").val(),$("#locId").val());
	 * $("#wardZone").html(result); }
	 */
	var authLevel = $("#authLevel").val();
	if(authLevel=="true"){
		getOwnerTypeInfo();
		fetchNewCheckList();
		$("#serchBtnDirect").hide();
		$("#serchBtn").hide();
	}
	var mutationLevelFlag = $("#mutationLevelFlag").val();
	if('E' == mutationLevelFlag){
		$("#saveMutEdit").show();
	}else{
		$("#saveMutEdit").hide();
	}
	
	var returnLOIResult= $('#returnLOIResult').val();
	if(returnLOIResult == 'Y'){
		$('input:radio[value=APPROVED]').attr('checked', 'checked');
		$('input:radio[value=FORWARD_TO_EMPLOYEE]').attr('disabled','disabled');
		$('input:radio[value=REJECTED]').attr('disabled','disabled');
		$('input:radio[value=SEND_BACK]').attr('disabled','disabled');
		
	}
	var decision = $("#decision").val();
	if(decision == 'APPROVED'){
		$('input:radio[value=APPROVED]').attr('checked', 'checked');
	}
	if(decision == 'FORWARD_TO_EMPLOYEE'){
		$('input:radio[value=FORWARD_TO_EMPLOYEE]').attr('checked', 'checked');
	}
	if(decision == 'REJECTED'){
		$('input:radio[value=REJECTED]').attr('checked', 'checked');
	}
	if(decision == 'SEND_BACK'){
		$('input:radio[value=SEND_BACK]').attr('checked', 'checked');
	}
});

function yearLength() {
	var dateFields = $('.dateClass');
	dateFields.each(function() {

		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	})
}

function getOwnerTypeInfo() {
	var ownerType = $("#ownerInfo" + " option:selected").attr("code");
	if (ownerType != undefined) {
		var data = {
			"ownershipType" : ownerType
		};
		var URL = 'MutationForm.html?getOwnershipTypeDiv';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#ownerDetails").html(returnData);
		$("#ownerDetails").show();
		// checkListCharge
		$("#detailDiv").hide();
		$("#saveMut").hide();
		var mutationLevelFlag = $("#mutationLevelFlag").val();
		if('A' == mutationLevelFlag){
			$("#saveMutAut").show();
		}else{
			$("#saveMutAut").hide();
		}
		$("#checkListCharge").show();
	} else {
		$("#ownerDetails").html("");
	}
}

function fetchNewCheckList() {
	$('#marketValue').prop("disabled", false);
	$('#salesDeedValue').prop("disabled", false);
	tranferType = $('#propTransferDto\\.transferType').find(":selected").attr('code')
	if( 'TH'== tranferType){
		$('#marketValue').prop("disabled", true);
		$('#salesDeedValue').prop("disabled", true);
	}
	$("#detailDiv").hide();
	$("#saveMut").hide();
	$("#checkListCharge").show();
}

function saveMutationWithoutEdit(element) {
	var appNo = $("#submit").val();
	var popupMsg = "This application has been rejected";
	if ($("input:radio[name='workflowActionDto.decision']").filter(":checked")
			.val() == 'REJECTED') {
		// return saveOrUpdateForm(element, popupMsg,'AdminHome.html',
		// 'saveMutationWithoutEdit');
		return saveOrUpdateForm(element, popupMsg,
				'CommonRejectionLetter.html?printCommonRejectionLetter&appNo='
						+ appNo, 'saveMutationWithoutEdit');
	} else if ($("input:radio[name='workflowActionDto.decision']").filter(
			":checked").val() == 'FORWARD_TO_EMPLOYEE'
			|| $("input:radio[name='workflowActionDto.decision']").filter(
					":checked").val() == 'SEND_BACK') {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'AdminHome.html', 'saveMutationWithoutEdit');
	}
	var isLastAuth = $("#isLastAuth").val();
	var isBeforeLastAuth = $("#isBeforeLastAuth").val();
	var bypassPublicNotice = $("#bypassPublicNotice").val();
	if (isLastAuth == "false" && isBeforeLastAuth == "false") {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'AdminHome.html', 'saveMutationWithoutEdit');
	} else if (isLastAuth == "true") {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
		'MutationAuthorization.html?PrintReport',
		'saveMutationWithoutEdit');
	} else if (isBeforeLastAuth == "true" && !bypassPublicNotice == "Y"
			|| bypassPublicNotice == "") {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'MutationAuthorization.html?printPublicNoticeReport',
				'saveMutationWithoutEdit');
	} else if (isBeforeLastAuth == "true" && bypassPublicNotice == "Y") {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'AdminHome.html', 'saveMutationWithoutEdit');
	}
}

function saveMutationWithEdit(element) {
	var theForm = '#MutationForm';
	var requestData = {};
	var returnData;
	var isLastAuth = $("#isLastAuth").val();
	var isBeforeLastAuth = $("#isBeforeLastAuth").val();
	var bypassPublicNotice = $("#bypassPublicNotice").val();
	requestData = __serializeForm(theForm);
	var URL = 'MutationAuthorization.html?saveMutationWithEdit';
	if (isLastAuth == "false" && isBeforeLastAuth == "false"){
		returnData = doFormActionForSaveProperty(element, "Saved Successfully",
				URL, true, 'AdminHome.html');
	}else if (isLastAuth == "true"){
		returnData = doFormActionForSaveProperty(element, "Saved Successfully",
				URL, true, 'AdminHome.html');
	}else if (isBeforeLastAuth == "true" && bypassPublicNotice == "Y") {
		returnData = doFormActionForSaveProperty(element, "Saved Successfully",
				URL, true, 'AdminHome.html');
	} else if (isBeforeLastAuth == "true" && !bypassPublicNotice == "Y"
		|| bypassPublicNotice == ""){
		returnData = doFormActionForSaveProperty(element, "Saved Successfully",
				URL, true, 'MutationAuthorization.html?printPublicNoticeReport');
	}
}

function editMutation(element) {
	var theForm = '#MutationForm';
	var data = {};
	data = __serializeForm(theForm);
	var URL = 'MutationAuthorization.html?editMutation';
	returnData = __doAjaxRequest(URL, 'POST', data, false);
	if (returnData) {
		$(formDivName).html(returnData);
		// var ownerType = $("#ownershipNewId").val();
		var ownerType = $("#ownerInfo" + " option:selected").attr("code");
		var data1 = {
			"ownershipType" : ownerType
		};
		var URL1 = 'MutationForm.html?getOwnershipTypeDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);

		$("#ownerDetails").html(returnData1);
		$("#ownerDetails").show();
	}
}

function confirmToProceed(element) {

	var errorList = [];
	// errorList = ValidateTick(errorList);
	if (errorList.length == 0) {
	} else {
		showErrorOnPage(errorList);
	}
}

function savePropertyFrom(element) {
	var kdmcMutFlag = $("#kdmcMutFlag").val();
	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'Y') {
		var returnData = saveOrUpdateForm(element,
				"Bill Payment done successfully!",
				'MutationForm.html?redirectToPay', 'saveform');
		if (returnData == false) {
			setOwner(returnData);
		}
	} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'N'
			|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
					":checked").val() == 'P') {
		var returnData = saveOrUpdateForm(element,
				"Bill Payment done successfully!",
				'MutationForm.html?PrintReport', 'saveform');
		if (returnData == false) {
			setOwner(returnData);
		}
	} else if (kdmcMutFlag == 'Y') {
		var returnData = saveOrUpdateForm(element,
				"Bill Payment done successfully!",
				'MutationForm.html?PrintReport', 'saveform');
		if (returnData == false) {
			setOwner(returnData);
		}
	} else {
		var returnData = saveOrUpdateForm(element,
				"Bill Payment done successfully!", 'MutationForm.html',
				'saveform');
		if (returnData == false) {
			setOwner(returnData);
		}
	}

}

function getMutationDetail() {

	var errorList = [];
	$("#saveMut").hide();
	var propNo = $("#assNo").val();
	var flatNo = $("#flatNo").val();
	var billMethod = $("#billingMethod").val();
	var flatOrParent = $("input[name='propTransferDto.flatOrParentLevelChange']:checked").val();
	if(flatOrParent == undefined){
		flatOrParent = '';
	}
	if (billMethod == 'I') {
		if (flatNo == "" || flatNo == undefined || flatNo == null
				|| flatNo == '0') {
			errorList.push(getLocalMessage('prop.select.flatNo'));
		}
	}
	if (propNo == "" || propNo == null || propNo == undefined) {
		errorList.push(getLocalMessage("mutation.input.validation"));
	}
	if (errorList.length > 0) {
		return showErrorOnPage(errorList);
	} else {
		var data = {
			"propNo" : propNo,
			"flatNo" : flatNo,
			"flatOrParent":flatOrParent
		};
		var URL = 'MutationForm.html?getMutationDetailWithFlat';	
		var returnData = __doAjaxRequest(URL, 'POST', data, false);	
		$(formDivName).html(returnData);		
		var errorMsg = $("#errorMsg").val();
		if (errorMsg == "Y") {
			$("#serchBtnDirect").hide();
			$("#serchBtn").show();
			$("#resetform").show();
		} else {
			$("#serchBtnDirect").hide();
			$("#serchBtn").hide();
			$("#resetform").hide();
			
		}
	}
}

function getCheckListAndCharges(element) {
	var errorList = [];
	validateTableInfo(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	}
	else{
		var theForm = '#MutationForm';
		var requestData = {};
		requestData = __serializeForm(theForm);
	
		var URL = 'MutationForm.html?getCheckListAndCharges';
		var returnData = __doAjaxRequestValidationAccor(element, URL, 'POST',
				requestData, false, 'html');
		if (returnData) {
			$(formDivName).html(returnData);
			var ownerType = $("#ownershipNewId").val();
			var data1 = {
				"ownershipType" : ownerType
			};
			var URL1 = 'MutationForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#ownerDetails").html(returnData1);
			$("#ownerDetails").find("select").prop("disabled", "disabled");
			$("#ownerDetails").find("input").prop("disabled", "disabled");
			$("#ownerDetails").show();
			$("#checkListCharge").hide();
			$("#saveMut").show();
			$("#saveMutAut").show();
		}
		$("#serchBtnDirect").hide();
		$("#serchBtn").hide();
		}

}

function validateTableInfo(errorList){
	var owner = $('#ownerInfo option:selected').attr('code');
	if (owner == "JO")
	{
		var rowCount = $('#joTable tr').length;
		if(rowCount < 3){
			errorList.push(getLocalMessage('property.mutation.propertyOwnerShipTransfer'));
		}
	}
}

function setOwner(returnData) {
	var ownerType = $("#ownershipNewId").val();
	var data1 = {
		"ownershipType" : ownerType
	};
	var URL1 = 'MutationForm.html?getOwnershipTypeDiv';
	var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);

	$("#ownerDetails").html(returnData1);
	$("#ownerDetails").show();

}

function showErrorOnPage(errorList) {
	var errMsg = '<ul>';
	var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeOutErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
	$.each(errorList, function(index) {
		errMsg += '<li><i class="fa fa-exclamation-circle"></i> &nbsp;'
				+ errorList[index] + '</li>';
	});

	errMsg += '</ul>';
	$("#errorDiv").html(errMsg);
	$("#errorDiv").show();
	$("html, body").animate({
		scrollTop : 0
	}, "slow");
	return false;
}

function doFormActionForSaveProperty(obj, successMessage, URL, sendFormData,
		successUrl) {
	var formName = findClosestElementId(obj, 'form');
	var theForm = '#' + formName;

	var requestData = {};

	if (sendFormData) {

		requestData = __serializeForm(theForm);
	}

	var url = URL;

	var returnData = __doAjaxRequestForSave(url, 'post', requestData, false,
			'', obj);
	if ($.isPlainObject(returnData)) {
		var message = returnData.command.message;

		var hasError = returnData.command.hasValidationError;

		if (!message) {
			message = successMessage;
		}

		if (message && !hasError) {
			if (returnData.command.hiddenOtherVal == 'SERVERERROR')

				showSaveResultBox(returnData, message, 'AdminHome.html');

			else

				showSaveResultBox(returnData, message, successUrl);
		} else if (hasError) {
			$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');
		} else
			return returnData;

	} else if (typeof (returnData) === "string") {
		$(formDivName).html(returnData);
		prepareTags();
	} else {
		alert("Invalid datatype received : " + returnData);
	}

	return false;

}

// fetch API details on selection of button
function getLandApiDetails(obj) {
	var landTypePrefix = $(".landValue").val();
	var data = {};
	var URL = 'MutationForm.html?getLandTypeApiDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$("#showAuthApiDetails").html(returnData);
	$("#showAuthApiDetails").show();
}

function generateLoiCharges(element) {
	
	$('#error-div').css('display', 'none');
			var result;

			loiSaveObj = element;
			var yes = getLocalMessage('eip.commons.yes');
			var no = getLocalMessage('eip.commons.no');
			var warnMsg = getLocalMessage('mutation.generate.loi.popup');

			message = '<p class="text-blue-2 text-center padding-15">'
					+ warnMsg + '</p>';
			message += '<div class=\'text-center padding-bottom-10\'>'
					+ '<input class="btn btn-success" type=\'button\' value=\''
					+ yes
					+ '\'  id=\'yes\' '
					+ ' onclick="onGenerateLoiChargesYes(this)"/> &nbsp;'
					+ '<input class="btn btn-success " type=\'button\' value=\''
					+ no + '\'  id=\'no\' '
					+ ' onclick="closeConfirmBoxForm()"/>' + '</div>';

			$(childDivName).addClass('ok-msg').removeClass('warn-msg');
			$(childDivName).html(message);
			$(childDivName).show();
			$('#yes').focus();
			showModalBox(childDivName);
			return false;

}

function onGenerateLoiChargesYes(element) {
	
	$.fancybox.close();
	var divName = '.content-page';
	var URL = 'MutationAuthorization.html?generateLoiCharges';
	var theForm = '#MutationAuthorization';
	var requestData = {};
	requestData = __serializeForm(theForm);
	var returnData = __doAjaxRequest(URL, 'Post', requestData, false, 'html');

	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	prepareTags();

}

function redirectToSearchPropNo(obj) {

	var errorList = [];
	var propNo = $("#assNo").val();
	if (propNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#assNo").val();
		var requestData = {
			"propNo" : propNo
		};
		var ajaxResponse = doAjaxLoading('MutationForm.html?getBillingMethod',
				requestData, 'html');

		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			$("#showSelectButton").show();			
			//$("#showFlatNo").show();
			$("#serchBtn").hide();
			//$("#serchBtnDirect").show();
			//$("#billingMethod").val('I');
		} else {
			getMutationDetail();
		}
	} else {
		showErrorOnPage(errorList);
	}
}

function SearchMutation(element) {

	var errorList = [];
	var divName = '.content-page';
	var propNo = $("#assNo").val();
	var oldPropNo = $("#assOldpropno").val();
	var applicationNo = $("#apmApplicationId").val();
	if (applicationNo == undefined) {
		applicationNo = "";
	}
	var flatNo = $("#flatNo").val();
	var billMethod = $("#billingMethod").val();
	if (billMethod == 'I') {
		if (flatNo == "" || flatNo == undefined || flatNo == null
				|| flatNo == '0') {
			errorList.push(getLocalMessage('prop.select.flatNo'));
		}
	} else {
		flatNo = "";
	}
	if (applicationNo == "" && propNo == "" && oldPropNo == "") {
		errorList.push(getLocalMessage("property.valid.mutation.enter"));
	}

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var data = {
			"propNo" : propNo,
			"oldPropNo" : oldPropNo,
			"applicationNo" : applicationNo,
			"flatNo" : flatNo
		};

		var table = $('#MutationPrintCertificateTable').DataTable();
		var URL = 'MutationPrintCertificate.html?getMutationDetails';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);

		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		if (billMethod == 'I') {
			$("#showFlatNoForPrint").show();
		}
		prepareTags();
	}
}

function printCertificate(proAssNo, apmApplicationId, flatNo) {

	var errorList = [];
	var requestData = 'proAssNo=' + proAssNo + '&apmApplicationId='
			+ apmApplicationId + '&flatNo=' + flatNo;
	var URL = 'MutationPrintCertificate.html?MutationPrintCertificateSkdcl';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
	if (returnData == "f") {
		errorList.push('Invalid');
		displayErrorsOnPage(errorList);
	} else {
		window.open(returnData, '_blank');
	}
}

function redirectToSearchPropNoPrintCertificate(obj) {
	showloader(true);
	setTimeout(function () {
	var errorList = [];
	var propNo = $("#assNo").val();
	var oldPropNo = $("#assOldpropno").val();
	if (propNo == "" && oldPropNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#assNo").val();
		var requestData = {
			"propNo" : propNo
		};
		var ajaxResponse = doAjaxLoading('MutationForm.html?getBillingMethod',
				requestData, 'html');
	
		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			$("#showFlatNoForPrint").show();
			$("#serchBtn").hide();
			$("#serchBtnDirect").show();
			$("#billingMethod").val('I');
		} else {
			SearchMutation();
		}
	} else {
		displayErrorsOnPage(errorList);
	}
	showloader(false);
	},1000);
}

function saveMutationAfterApproval(element) {
	var errorList = [];
	var decision = $("input[name='workflowActionDto.decision']:checked").val();
	var remark = $("#comments").val();
	if (decision == '' || decision == undefined) {
		errorList.push(getLocalMessage("property.select.atleast.oneDecision"));
	}
	if (remark == "") {
		errorList.push(getLocalMessage("property.enter.remark"));
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		return saveOrUpdateForm(element, "", 'AdminHome.html',
				'saveAfterApproval');
	}
	
}

function redirectToFlatOrParent(){
	if ($("input[name='propTransferDto.flatOrParentLevelChange']:checked")
			.val() == "P") {
		$("#showFlatNo").hide();
		$("#serchBtnDirect").show();
		$("#billingMethod").val('');
	}else{
		$("#showFlatNo").show();
		$("#serchBtnDirect").show();
		$("#billingMethod").val('I');
	}
} 

function resetMutForm(){
	$("#errorDiv").hide();
	value = "MutationForm.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();
}
