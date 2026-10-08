/**
 * 
 */
$(document).ready(function() {
	$('#showFlatNo').hide();
	$("#proertyNo").val();
	$("#flatNo").val();
	

	
	 /* $("#showFlatNo").hide();
	  $('#flatNo').val('').trigger('chosen:updated');
	  $('#aioConceptName').find(":selected").text();
	 */

	$('#flatNo').trigger("chosen:updated");

	$("#resetform").on("click", function() {
		window.location.reload("#propertyBillPrintingReport")
	})
});

function getFlatNo() {

	var errorList = [];
	var propNo = $("#proertyNo").val();

	if (propNo == "") {
		errorList
				.push(getLocalMessage("property.validation.detail.property.wise"));
	}
	if (errorList.length == 0) {
		var propNo = $("#proertyNo").val();
		var requestData = {
			"propNo" : propNo

		};

		var ajaxResponse = doAjaxLoading(
				'propertyBillReport.html?getBillingMethod', requestData,
				'html');

		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			/* $('#flatNo').val('').trigger('chosen:updated'); */
			$("#showFlatNo").show();
			$("#billingMethod").val('I');
		}
	} else {
		showErrorOnPage(errorList);
	}

}

function saveReportForm(obj) {

	var errorList = [];
	var propNumber = $("#proertyNo").val();
	var flatNo = $("#flatNo").val();
	var financialYear = $("#financialYear").val();

	if (propNumber == "" || propNumber == null || propNumber == undefined) {
		errorList
				.push(getLocalMessage("property.validation.detail.property.wise"));
	}
	/*
	 * if (flatNo == "" || flatNo == null || flatNo == undefined) {
	 * errorList.push(getLocalMessage("enter flatNo.")); }
	 */
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var divName = '.content-page';
		$("#errorDiv").hide();

		var requestData1 = "&propNumber=" + propNumber + "&flatNo=" + flatNo + "&financialYear=" + financialYear;

		var URL = 'propertyBillReport.html?getBillReport';
		var returnData1 = __doAjaxRequest(URL, 'POST', requestData1, false);
		window.open(returnData1, '_blank');

	}
}


	function getAdjustmentDetailFlatNo() {
	var errorList = [];
	var propNo = $("#proertyNo").val();

	if (propNo == "" || propNo == null || propNo ==''|| propNo== undefined ) {
		errorList.push(getLocalMessage("property.validation.detail.property.wise"));
	}
	if (errorList.length == 0) {
		var propNo = $("#proertyNo").val();
		var requestData = {
			"propNo" : propNo

		};

		var ajaxResponse = doAjaxLoading(
				'propertyAdjustmentReport.html?getAdjutmentDetailBillingMethod',
				requestData, 'html');

		if (ajaxResponse != null && ajaxResponse != "" && ajaxResponse != "[]") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			/* $('#flatNo').val('').trigger('chosen:updated'); */
			$('#showFlatNo').show();
			$("#billingMethod").val('I');
		} else if (ajaxResponse == null || ajaxResponse == ""
				|| ajaxResponse == "[]") {
			$('#showFlatNo').hide();

		} else {
			//showErrorOnPage(errorList);
			displayError(errorList);
		}

	}
}


	function saveAdjustmentDetailForm(obj) {
	
		var errorList = [];
		var propNumber = $("#proertyNo").val();
		var flatNo = $("#flatNo").val();
		// var financialYear = $("#financialYear").val();
	
		if (propNumber == "" || propNumber == null || propNumber == undefined) {
			errorList
					.push(getLocalMessage("property.validation.detail.property.wise"));
		}
	
		/*
		 * if (flatNo == "" || flatNo == null || flatNo == undefined) {
		 * errorList.push(getLocalMessage("property.adjustmentFlatNo.validation")); }
		 */
	
		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
		} else {
			var divName = '.content-page';
			$("#errorDiv").hide();
	
			var data = {
				"propNumber" : propNumber,
				"flatNo" : flatNo
	
			};
	
			var URL = 'propertyAdjustmentReport.html?getAdjustmentDetailBillReport';
			var returnData = __doAjaxRequest(URL, 'POST', data, false);
			window.open(returnData, '_blank');
	
		}
	}
