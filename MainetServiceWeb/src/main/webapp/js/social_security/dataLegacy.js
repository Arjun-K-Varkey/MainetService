var url = "DataLegacyForm.html";
$(document).ready(function() {

	$("#lastpaymentdateId").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		
	});

	$("#lastpaymentdateId").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

$("#applicantId").datepicker({
		
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "-100:-0"
	});

	$("#applicantId").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

	$("#lastDateLifeCertiId").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		
	});
	$("#lastDateLifeCertiId").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

	$("#percenrofDis").prop("disabled", true);
	$("#bplinspectyr").prop("disabled", true);
	$("#bplfamily").prop("disabled", true);
	
	$("#resetform").on("click", function(){ 
		  window.location.reload("#dataLegacyFormId")
		});
});

function saveDataLegacy(element) {
	 debugger;
	var errorList = [];
	errorList = dataLegacyFormValidation(errorList);
	if (errorList.length == 0) {
		return saveOrUpdateForm(element, '', 'DataLegacyForm.html', 'saveform');
		$(divName).html(response);
	} else {
		displayErrorsOnPage(errorList);
	}
}

function dataLegacyFormValidation(errorList) {
    debugger;
	var schemenameId = $("#schemenameId").val();
	var nameofApplicant = $("#nameofApplicant").val();
	var applicationDobId = $("#applicantId").val();
	var ageason = $("#ageason").val();
	var genderId = $("#applicationformdto\\.genderId").val();
	var applicantAdress = $("#applicantAdress").val();
	var aadharCard = $("#aadharCard").val();
	var pinCode = $("#pinCode").val();
	var mobNum = $("#mobNum").val();
	var educationId = $("#applicationformdto\\.educationId").val();
	var maritalStatusId = $("#applicationformdto\\.maritalStatusId").val();
	var categoryId = $("#applicationformdto\\.categoryId").val();
	var annualIncome = $("#annualIncome").val();
	var typeofDisId = $("#applicationformdto\\.typeofDisId").val();
	var percenrofDis = $("#percenrofDis").val();
	var bplid = $("#applicationformdto\\.bplid").val();
	var bplinspectyr = $("#bplinspectyr").val();
	var bplfamily = $("#bplfamily").val();
	var banknameId = $("#banknameId").val();
	var accountNumber = $("#accountNumber").val();
	var nameofFather = $("#nameofFather").val();
	var nameofMother = $("#nameofMother").val();
	var contactNumber = $("#contactNumber").val();
	var detailsoffamIncomeSource = $("#detailsoffamIncomeSource").val();
	var annualIncomeoffam = $("#annualIncomeoffam").val();
	var lastpaymentdateId = $("#lastpaymentdateId").val();
	var referenceNo = $("#referenceNo").val();
	var lastDateLifeCertiId = $("#lastDateLifeCertiId").val();

	if (schemenameId == "0" || schemenameId == undefined || schemenameId == '') {
		errorList.push(getLocalMessage('social.sec.schemename.req'));
	}

	if (nameofApplicant == "0" || nameofApplicant == undefined
			|| nameofApplicant == '') {
		errorList.push(getLocalMessage('social.sec.nameofapp.req'));
	}

	if (applicationDobId == "0" || applicationDobId == undefined
			|| applicationDobId == '') {
		errorList.push(getLocalMessage('social.sec.appdob.req'));
	}

	if (ageason == "0" || ageason == undefined || ageason == '') {
		errorList.push(getLocalMessage('social.sec.ageason.req'));
	}

	if (genderId == "0" || genderId == undefined || genderId == '') {
		errorList.push(getLocalMessage('social.sec.gen.req'));
	}

	if (applicantAdress == "0" || applicantAdress == undefined
			|| applicantAdress == '') {
		errorList.push(getLocalMessage('social.sec.appadd.req'));
	}
	
	

	if (pinCode == "0" || pinCode == undefined || pinCode == '') {
		errorList.push(getLocalMessage('social.sec.pincode.req'));
	}
	if(pinCode.length < 6 || pinCode.length > 6)
	{
	errorList.push(getLocalMessage('social.sec.valid.pincode'));
	}
	
	if (aadharCard == "0" || aadharCard == undefined
			|| aadharCard == '') {
		errorList.push(getLocalMessage('social.aadharNo.required'));
	}

	if (mobNum == "0" || mobNum == undefined || mobNum == '') {
		errorList.push(getLocalMessage('social.sec.mobnum.req'));
	}

	

	if (maritalStatusId == "0" || maritalStatusId == undefined
			|| maritalStatusId == '') {
		errorList.push(getLocalMessage('social.sec.maritalstat.req'));
	}

	if (categoryId == "0" || categoryId == undefined || categoryId == '') {
		errorList.push(getLocalMessage('social.sec.cate.req'));
	}

	

	if (typeofDisId == "0" || typeofDisId == undefined || typeofDisId == '') {
		errorList.push(getLocalMessage('social.sec.typeofdis.req'));
	}

	let typeofdisvar = $('#applicationformdto\\.typeofDisId option:selected')
			.attr('code');
	if (typeofdisvar != "NA") {
		if (percenrofDis == "0" || percenrofDis == undefined
				|| percenrofDis == '') {
			errorList.push(getLocalMessage('social.sec.percentofdis.req'));
		}
	}

	if (bplid == "0" || bplid == undefined || bplid == '') {
		errorList.push(getLocalMessage('social.sec.bpl.req'));
	}
	let bplvar = $('#applicationformdto\\.bplid option:selected').attr('code');
	$("#isBplApplicableId").val("Y");
	if (bplvar != "NNO") {
		$("#isBplApplicableId").val("N");
		if (bplinspectyr == "0" || bplinspectyr == undefined
				|| bplinspectyr == '') {
			errorList.push(getLocalMessage('social.sec.bplyr.req'));
		}

		if (bplfamily == "0" || bplfamily == undefined || bplfamily == '') {
			errorList.push(getLocalMessage('social.sec.bplid.req'));
		}
	}

	if (banknameId == "0" || banknameId == undefined || banknameId == '') {
		errorList.push(getLocalMessage('social.sec.bankname.req'));
	}

	if (accountNumber == "0" || accountNumber == undefined
			|| accountNumber == '') {
		errorList.push(getLocalMessage('social.sec.accnum.req'));
	}

	if (nameofFather == "0" || nameofFather == undefined || nameofFather == '') {
		errorList.push(getLocalMessage('social.sec.namefather.req'));
	}

	if (nameofMother == "0" || nameofMother == undefined || nameofMother == '') {
		errorList.push(getLocalMessage('social.sec.namemother.req'));
	}

	if (contactNumber == "0" || contactNumber == undefined
			|| contactNumber == '') {
		errorList.push(getLocalMessage('social.sec.contactnum.req'));
	}

	if (detailsoffamIncomeSource == "0"
			|| detailsoffamIncomeSource == undefined
			|| detailsoffamIncomeSource == '') {
		errorList.push(getLocalMessage('social.sec.detailsfamincome.req'));
	}

	

	if (lastpaymentdateId == "0" || lastpaymentdateId == undefined
			|| lastpaymentdateId == '') {
		errorList.push(getLocalMessage('data.legacy.lastpaymentdate.req'));
	}

	if (referenceNo == "0" || referenceNo == undefined || referenceNo == '') {
		errorList.push(getLocalMessage('data.legacy.referencenum.req'));
	}

	if (lastDateLifeCertiId == "0" || lastDateLifeCertiId == undefined
			|| lastDateLifeCertiId == '') {
		errorList.push(getLocalMessage('social.sec.lastdtlifecerti.req'));
	}
	return errorList;
}

//function to validate contact number
function validateContactNum() {
	var errorList = [];
	var contactNumber = $("#contactNumber").val();
	errorList = validateMobNo(contactNumber);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
}

//function to validate mobile number
function validateMobNum() {
	var errorList = [];
	var mobNum = $("#mobNum").val();
	errorList = validateMobNo(mobNum);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
}

function geteducationdetails() {
	let educationId = $("#applicationformdto\\.educationId").val();
	if (educationId == undefined || educationId == '' || educationId == '0') {
		$("#classs").prop("disabled", true);
		//$("#classs").val(156);
	} else {

		$("#classs").prop("disabled", false);
	}
}

//function to hide and show type of disability textbox
function disabilitydetails() {
	let typeofdisvar = $('#applicationformdto\\.typeofDisId option:selected')
			.attr('code');
	if (typeofdisvar == "NA") {
		$("#percenrofDis").prop("disabled", true);
	} else {
		$("#percenrofDis").prop("disabled", false);
		//$("#classs").val(156);
	}
}

//function to hide and show type of disability bpl
function bpldetails() {
	let bplvar = $('#applicationformdto\\.bplid option:selected').attr('code');

	if (bplvar == "YES") {
		$("#bplinspectyr").prop("disabled", false);
		$("#bplfamily").prop("disabled", false);
	} else {
		$("#bplinspectyr").prop("disabled", true);
		$("#bplfamily").prop("disabled", true);
	}
}

function dobdetails() {
	
	
	var dateString = $("#applicantId").val(); 

	/*#29786 by Priti*/
	var dateParts = dateString.split("/");

	// month is 0-based, that's why we need dataParts[1] - 1
	var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 

	//document.body.innerHTML = dateObject.toString();
	if (dateString != undefined || dateString != ''
			|| dateString != '0') {

		var dates = new Date(dateObject.toString());
		var age = getAge(dates);
		$("#ageason").val(age);
	} 
}
// function to calculate age on entry of dob
function getAge(DOB) {

	var today = new Date();
	var birthDate = new Date(DOB);
	var age = today.getFullYear() - birthDate.getFullYear();
	var m = today.getMonth() - birthDate.getMonth();
	if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
		age = age - 1;
	}

	return age;
}

function resetDataLegacy()
{
	
	$('input[type=text]').val('');  
	$(".alert-danger").hide();
	$("#dataLegacyFormId").validate().resetForm();
}
