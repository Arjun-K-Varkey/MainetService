
$(document).ready(function() {
	
 $("#resetform").on("click", function(){ 
		  window.location.reload("#cancellationLicenseForm")
		});

});

function getLicenseDetails() {
	var errorList = [];
	var theForm = '#cancellationLicenseForm';
	var trdLicno = $("#trdLicno").val();

	errorList = validateCancellationLicenseNumber(errorList);

	if (errorList.length == 0) {

		var requestData = {
			'trdLicno' : trdLicno
		}
		var response = __doAjaxRequest(
				'CancellationLicenseForm.html?getLicenseDetails', 'POST',
				requestData, false, 'html');

		$(formDivName).html(response);
		var licHide = $("#licHide").val();
		if (licHide == 'Y') {
			$("#trdLicno").hide();
		}
	} else {
		displayErrorsOnPage(errorList);
	}
}

function backPage() {

	window.location.href = getLocalMessage("AdminHome.html");
}

function saveCancellationLicense(obj){
	
	var errorList = [];
	
	errorList = validateCancellationLicenseForm(errorList);
	if (errorList.length == 0) {
		
		return saveOrUpdateForm(obj,
				"Cancellation Of License Submitted Successfully", "AdminHome.html",
				'saveCancellationForm');
	}
	else{
		displayErrorsOnPage(errorList);
	}
}

function getChecklistAndCharges(obj) {
	var errorList = [];
	errorList = validateCancellationLicenseForm(errorList);
	if (errorList.length == 0) {
	var theForm = '#cancellationLicenseForm';
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'CancellationLicenseForm.html?getCheckListAndCharges';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
	if (returnData != null) {

		$(formDivName).html(returnData);
		prepareTags();
	}
	}
	else{
		displayErrorsOnPage(errorList);
	}
}

function generateChallanAndPayment(element) {
	var errorList = [];
	if (errorList.length == 0) {
	
	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'N'
			|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
					":checked").val() == 'P') {
		return saveOrUpdateForm(element,
				"Your application cancel successfully!",
				'CancellationLicenseForm.html?PrintReport', 'generateChallanAndPayement');
	} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'Y') {
		return saveOrUpdateForm(element,
				"Your application cancel successfully!!",
				'CancellationLicenseForm.html?redirectToPay', 'generateChallanAndPayement');
	}
	else{
		
		var status =  saveOrUpdateForm(element,
		"Your application cancel successfully!!",
		'AdminHome.html', 'generateChallanAndPayement');
	}
	
	//#129938
	  if (!$.trim($('#validationerrordiv').html()).length) {
				agencyRegAcknow();
			}
	} else {
		
		displayErrorsOnPage(errorList);
	}
}


function validateCancellationLicenseForm(errorList) {
	var errorList = [];
	var remark = $("#remarks").val();

	if (remark == "" || remark == undefined || remark == "") {
		errorList.push(getLocalMessage("trade.validation.remark"));
	}

	return errorList;
}


function validateCancellationLicenseNumber(errorList) {
	var errorList = [];
	var trdLicno = $("#trdLicno").val();

	if (trdLicno == "" || trdLicno == undefined || trdLicno == "") {
		errorList.push(getLocalMessage("trade.ValidateLicenseNo"));
	}

	return errorList;
}

function saveCancellation(obj) {
	var errorList = [];
	//errorList = validateMarketLicenseForm(errorList);
	//errorList = errorList.concat(validateOwnerDetailsTable());
	// = errorList.concat(validateitemDetailsTable());
	
	
	if (errorList.length == 0) {
		
	
		
		return saveOrUpdateForm(obj,
				"Cancellation Form Submitted Successfully", "CancellationLicenseForm.html",
				'saveCancellationLicenseForm');
		
	}else {
		displayErrorsOnPage(errorList);
	}
	}

function editForm(element){
	return saveOrUpdateForm(element,
			"Your application Data  saved successfully!!",
			'AdminHome.html', 'saveCancellationLicenseForm');	
}

//#129938
function agencyRegAcknow() {

	var URL = 'CancellationLicenseForm.html?printAgencyRegAckw';
	var returnData = __doAjaxRequest(URL, 'POST', {}, false);

	var appId = $($.parseHTML(returnData)).find("#applicationId").html();
	var title = appId;
	var printWindow = window.open('', '_blank');

	printWindow.document.write('<html><head><title>' + title + '</title>');
	printWindow.document
			.write('<link href="assets/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />')
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
