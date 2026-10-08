 var noOfFam = 1;
 var meterType;
 $(document).ready(function(){
	noOfFam = $("#noOfFamilies").val();
	var meteredCcn = $('#csMeteredccn option:selected').attr('code');
	if(meteredCcn == "MTR"){
		$(".MeterDetailsDiv").show();
	}else{
		$(".MeterDetailsDiv").hide();
	}
	
	$('.pcDateCal').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		 minDate: $("#appldate").val(),
		maxDate: '-0d',
		yearRange: "-100:-0",
		onClose: function( selectedDate ) {
	        $( ".lessthancurrdate" ).datepicker( "option", "minDate", selectedDate );
	      }
	});
	
	$('.lessthancurrdate').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		minDate: $("#pcDate").val(),
		maxDate: '-0d',
		yearRange: "-100:-0"
	});
	var checkPhysicalEntryFlag =$("#checkPhysicalEntryFlag").val()
	if(checkPhysicalEntryFlag=="Y"){
		$("#pcDate").prop("disabled", false);
	}else{
		$("#pcDate").prop("disabled", true);
	}
	
	var checkMeterDetailsFlag =$("#checkMeterDetailsFlag").val()
	if(checkMeterDetailsFlag=="Y"){
		$("#meterInstallationDate").prop("disabled", false);
		$("#csMeteredccn").prop("disabled", false);
		$("#meterMake").prop("disabled", false);
		$("#meterNumber").prop("disabled", false);
		$("#initialMeterReading").prop("disabled", false);
		$("#meterMaxReading").prop("disabled", false);
		$("#meterOwnerShip").prop("disabled", false);
		$("#meterCost").prop("disabled", false);
	}else{
		$("#meterInstallationDate").prop("disabled", true);
		if($("#checkFirstBillGeneratedFlag").val() == "Y"){
			$("#csMeteredccn").prop("disabled", false);
		}else{
			$("#csMeteredccn").prop("disabled", true);
		}
		$("#meterMake").prop("disabled", true);
		if($("#checkMeterActiveFlag").val() == "A"){
			$("#meterNumber").prop("disabled", false);
		}else{
			$("#meterNumber").prop("disabled", true);
		}
		$("#initialMeterReading").prop("disabled", true);
		$("#meterMaxReading").prop("disabled", true);
		$("#meterOwnerShip").prop("disabled", true);
		$("#meterCost").prop("disabled", true);
	}
	var checkMeterInstallDateFlag =$("#checkMeterInstallDateFlag").val()
	var checkFirstBilling = $("#checkFirstBilling").val();
	if(checkMeterInstallDateFlag=="Y" || checkFirstBilling=="Y"){
		$("#meterInstallationDate").prop("disabled", false);
	}
	else{
		$("#meterInstallationDate").prop("disabled", true);
	}
	if(checkFirstBilling=="Y"){
		$("#meterNumber").prop("disabled", false);
	}
	
	meterType = $('#csMeteredccn option:selected').attr('code');
	$("#csMeteredccn").change(function(){
		var errorList = [];
		var meteredCcn = $('#csMeteredccn option:selected').attr('code');
		var checkFirstBillGenerated = $("#checkFirstBillGeneratedFlag").val();
		if(meterType == "MTR" && checkFirstBillGenerated != "Y"){
			if(meteredCcn == "NMR"){
				errorList.push(getLocalMessage('can not update meter type meter to non meter'));
				displayErrorsOnPage(errorList);
			}
		}
	});
});

function searchWaterDetail(obj) {

	showloader(true);
	setTimeout(function () {
		var errorList = [];
		errorList = ValidateDetails(errorList);	

		if (errorList.length == 0) {
			
			var theForm	= '#UpdateWaterDataEntrySuite';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URLone = 'UpdateWaterDataEntrySuite.html?getWaterDetails';
			var returnData = __doAjaxRequest(URLone, 'POST', requestData, false);
			$(formDivName).html(returnData);
			$("#serchBtn").show();
			return false;
		}
		else{
 			displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }

function updateDataEntry(element) {
	var errorList = [];
	errorList = validateForm(errorList);
	if (errorList.length == 0) {
		var theForm = '#UpdateWaterDataEntrySuite';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var returnData = __doAjaxRequestForSave('UpdateWaterDataEntrySuite.html?updateDataEntry', 'post', requestData, false);
		popUpMsg(returnData);
	} else {
		displayErrorsOnPage(errorList);
	}
}


function popUpMsg(returnData) {
	successUrl = 'UpdateWaterDataEntrySuite.html';
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
		$('.content-page').html(returnData);
		prepareTags();
	} else {
		alert("Invalid datatype received : " + returnData);
	}
	return false;
}

function ValidateDetails(errorList){
	
	var connNo = $("#conNum").val();
	if(connNo == ""){
		errorList.push(getLocalMessage("water.invalid.connectionNo"));
	}
	return errorList;
}

function validateEmail(email) {
	var regexPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	return regexPattern.test(email);
}

function validateForm(errorList) {
	var propNo = $("#propNo").val();

    if (propNo.length < 12) {
        errorList.push(getLocalMessage("water.invalid.propertyNo.minLength"));
    }
	if(parseInt($("#noOfFamilies").val()) > parseInt(noOfFam)){
		errorList.push(getLocalMessage("water.noOfFamilies.greater"));
	}
	var mobileNumber = $("#occupierMobNo").val();
	var filter = /[1-9]{1}[0-9]{9}/;
	if (mobileNumber != '') {
		if (!filter.test(mobileNumber)) {
			errorList.push(getLocalMessage("update.invalidNumber"));
		}
	}
	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();

	var pattern = /^[a-zA-Z\s-, ]+$/;
	if (firstName == "" || firstName == null || firstName == undefined ) {
		if (!pattern.test(firstName)) {
			errorList.push(getLocalMessage("water.firstName.empty"));
		}
	}
	/*if(lastName == "" || lastName == null || lastName == undefined){
		if (!pattern.test(lastName)) {
			errorList.push(getLocalMessage("water.lastName.empty"));
		}
	}
	var pincode = $("#pincode").val();
	var filter = /[0-9]{6}/;
	if (pincode != '') {
		if (!filter.test(pincode)) {
			errorList.push(getLocalMessage("update.invalid.pincode"));
		}
	}else{
		errorList.push(getLocalMessage("update.invalid.pincode"));
	}  
	var address = $("#address").val();
	if (address == '' || address == null || address == undefined) {
		errorList.push(getLocalMessage("update.location.address"));
	}*/
	var applicantEmailId = $.trim($('#emailId').val());
	if (applicantEmailId != '' && applicantEmailId != null
			&& applicantEmailId != undefined) {
		if (!validateEmail(applicantEmailId)) {
			errorList.push(getLocalMessage('update.invalid.email'));
		}
	} 
	var remark = $("#remark").val();
	if(remark == '' || remark == null || remark == undefined){
		errorList.push(getLocalMessage("water.enter.remark"));
	}
	var meteredCcn = $('#csMeteredccn option:selected').attr('code');
	if(meteredCcn == "MTR"){
		var meterNumber = $("#meterNumber").val();
		var initialMeterReading = $("#initialMeterReading").val();
		var meterMaxReading = $("#meterMaxReading").val();
		var meterInstallationDate = $("#meterInstallationDate").val();
		var meterOwnerShip = $("#meterOwnerShip").val();
		
		if($('#meterNumber').prop('disabled')==false){
			if (meterNumber == '' || meterNumber == null || meterNumber == undefined) {
				errorList.push(getLocalMessage("water.valid.meterNumber"));
			}
		}
		if($('#initialMeterReading').prop('disabled')==false){
			if (initialMeterReading == '' || initialMeterReading == null || initialMeterReading == undefined) {
				errorList.push(getLocalMessage("water.valid.meterRead"));
			}
		}
		if($('#meterMaxReading').prop('disabled')==false){
			if (meterMaxReading == '' || meterMaxReading == null || meterMaxReading == undefined) {
				errorList.push(getLocalMessage("water.valid.metMaxRead"));
			}
		}	
		if($('#meterInstallationDate').prop('disabled')==false){
			if (meterInstallationDate == '' || meterInstallationDate == null || meterInstallationDate == undefined) {
				errorList.push(getLocalMessage("water.valid.metInsDt"));
			}
		}	
		if($('#meterOwnerShip').prop('disabled')==false){
			if (meterOwnerShip == '' || meterOwnerShip == null || meterOwnerShip == undefined) {
				errorList.push(getLocalMessage("water.valid.ownShip"));
			}
		}	
	}
	if(meterType == "MTR" && $("#checkFirstBillGeneratedFlag").val() != "Y"){
		if(meteredCcn == "NMR"){
			errorList.push(getLocalMessage('can not update meter type meter to non meter'));
			displayErrorsOnPage(errorList);
		}
	}
	return errorList;
}

$("#csMeteredccn").change(function(){
	var meteredCcn = $('#csMeteredccn option:selected').attr('code');
	if(meteredCcn == "MTR"){
		$(".MeterDetailsDiv").show();
	}else{
		$(".MeterDetailsDiv").hide();
	}
	var checkMeterDetailsFlag =$("#checkMeterDetailsFlag").val()
	if(checkMeterDetailsFlag=="Y"){
		$("#meterInstallationDate").prop("disabled", false);
	}else{
		$("#meterInstallationDate").prop("disabled", true);
	}
});