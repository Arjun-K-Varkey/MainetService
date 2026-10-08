/**
 * 
 */
$(document).ready(function() {
	/*$('#mnassward1').val(-1);
	$('#mnassward2').val(-1);
	$('#mnassward3').val(-1);
	$('#mnassward4').val(-1);
	$('#mnassward5').val(-1);*/
	$('#mnDefaulterCount').val();
	$('#mnAmount').val();
	$('#mntoAmount').val();

	$('#clusterNo').val(-1);
	$('#assPropType1').val(-1);
	$('#usageType1').val(-1);
	$('#usageType2').val(-1);
	$('#propertyStatus').val(-1);
});

function saveForm(obj) {
	showloader(true);
	setTimeout(function() {
	var errorList = [];
	var wardZone1 = $('#mnassward1').val();
	var wardZone2 = $("#mnassward2").val();
	var wardZone3 = $("#mnassward3").val();
	var wardZone4 = $("#mnassward4").val();
	var wardZone5 = $("#mnassward5").val();
	var mnDefaulterCount = $("#mnDefaulterCount").val();
	var mnAmount = $("#mnAmount").val();
	var mntoAmount =$('#mntoAmount').val();
	var clusterNo = $('#clusterNo').val();
	var assPropType1 = $('#assPropType1').val();
	var usageType1 = $('#usageType1').val();
	var usageType2 = $('#usageType2').val();
	var propertyStatus = $('#propertyStatus').val();
	var propertyNo = $('#assNo').val();

	if (wardZone1 == 0 || wardZone1 == "" ) {
		errorList.push(getLocalMessage("property.validation.wardZone1"));
	}
	if (wardZone2 == 0 || wardZone2 == "") {
		errorList.push(getLocalMessage("property.validation.wardZone2"));
	}
	if (wardZone3 == 0 || wardZone3 == "") {
		errorList.push(getLocalMessage("property.validation.wardZone3"));
	}
	if (wardZone4 == 0 || wardZone4 == "") {
		errorList.push(getLocalMessage("property.validation.wardZone4"));
	}
	if (wardZone5 == 0 || wardZone5 == "") {
		errorList.push(getLocalMessage("property.validation.wardZone5"));
	}

	/*if (mnAmount == "" || mnAmount == null || mnAmount == undefined) {
		errorList
				.push(getLocalMessage("property.validation.defaulter.amount.report"));
	}

	if (mntoAmount == "" || mntoAmount == null || mntoAmount == undefined) {
		errorList
				.push(getLocalMessage("property.validation.defaulter.to.amount.report"));
	}
	
	if(mnAmount > mntoAmount){
		errorList
		.push(getLocalMessage("property.validation.defaulter.from.to.amount.report"));
	}
	if (mnDefaulterCount == "" || mnDefaulterCount == null
			|| mnDefaulterCount == undefined) {
		errorList.push(getLocalMessage("property.validation.defaulter.report"));
	}*/

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var divName = '.content-page';
		$("#errorDiv").hide();

		var wardZ1;
		var wardZ2;
		var wardZ3;
		var wardZ4;
		var wardZ5;

		if ($("#mnassward1").val() == undefined) {
			wardZ1 = 0;
		} else {
			wardZ1 = $("#mnassward1").val();
		}
		if ($("#mnassward2").val() == undefined) {
			wardZ2 = 0;
		} else {
			wardZ2 = $("#mnassward2").val();
		}
		if ($("#mnassward3").val() == undefined) {
			wardZ3 = 0;
		} else {
			wardZ3 = $("#mnassward3").val();
		}
		if ($("#mnassward4").val() == undefined) {
			wardZ4 = 0;
		} else {
			wardZ4 = $("#mnassward4").val();
		}
		if ($("#mnassward5").val() == undefined) {
			wardZ5 = 0;
		} else {
			wardZ5 = $("#mnassward5").val();
		}

		if (wardZ1 == -1) {
			wardZ1 = 0;
		}
		if (wardZ2 == -1) {
			wardZ2 = 0;
		}
		if (wardZ3 == -1) {
			wardZ3 = 0;
		}
		if (wardZ4 == -1) {
			wardZ4 = 0;
		}
		if (wardZ5 == -1) {
			wardZ5 = 0;
		}
		
		var requestData = {
			"wardZone1" : wardZ1,
			"wardZone2" : wardZ2,
			"wardZone3" : wardZ3,
			"wardZone4" : wardZ4,
			"wardZone5" : wardZ5,
			"mnDefaulterCount" : mnDefaulterCount,
			"mnAmount" : mnAmount,
			"mntoAmount" :mntoAmount,
			"clusterNo" : clusterNo,
			"assPropType1" : assPropType1,
			"usageType1" : usageType1,
			"usageType2" : usageType2,
			"propertyStatus" : propertyStatus,
			"propertyNo" : propertyNo
		};
		var URL = 'DefaulterReport.html?GetDefaulterReport';
		var returnCount = __doAjaxRequest(URL, 'POST', requestData, false);
		
		var Ok = getLocalMessage("bt.ok");
		var message = "";
		var errMsgDiv = '.msg-dialog-box';
		message = getLocalMessage("Total SMS URLs Generated Successfully : " + returnCount);
		var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message	+ '</h5>';
		d += '<div class=\'text-center\'><input type=\'button\' name="No" class= "btn btn-success" value=\''
			+ Ok + '\'  id=\'btnNo\' onclick="closeConfirmBoxForm()"/></div>';

		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(d).show();
		$('#btnNo').focus();
		showModalBoxWithoutClose(errMsgDiv);

	}
	showloader(false);
	}, 2000);
}
$(document).ready(function() {
	$("#mnassward2").change(function() {
		
		$('#clusterNo').empty().append('<option value="">All</option>');
		var wardZone1 = $('#mnassward1').val();
		var wardZone2 = $("#mnassward2").val();

		var requestData = {
			"wardZone1" : wardZone1,
			"wardZone2" : wardZone2
		};
		var URL = 'DefaulterReport.html?getClusterList';
		var ajaxResponse = doAjaxLoading(URL, requestData, 'html');
		//var ajaxResponse = __doAjaxRequest(URL, 'POST', requestData, false);
		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#clusterNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#clusterNo').trigger("chosen:updated");
		}		
	});
});
/*$("#resetForm").on("click", function() {
	window.location.reload("#DefaulterRegisterForm")
});*/