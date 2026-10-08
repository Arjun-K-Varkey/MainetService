$(document).ready(function() {

	$("#showFlatNo").hide();
	$("#serchBtnDirect").hide();
	
	let totalPay= Number($('#taxAmountVal0').val());
	$('#taxAmountVal0').val(totalPay.toFixed(2));
	
	let totalPay1= Number($('#taxAmountVal1').val());
	$('#taxAmountVal1').val(totalPay1.toFixed(2));
	
	let totalPay2= Number($('#taxAmountVal2').val());
	$('#taxAmountVal2').val(totalPay2.toFixed(2));
	
	let totalPay3= Number($('#taxAmountVal3').val());
	$('#taxAmountVal3').val(totalPay3.toFixed(2));
	
	let totalPay4= Number($('#taxAmountVal4').val());
	$('#taxAmountVal4').val(totalPay4.toFixed(2));
	
	let totalPay5= Number($('#taxAmountVal5').val());
	$('#taxAmountVal5').val(totalPay5.toFixed(2));
	
	let totalPay6= Number($('#taxAmountVal6').val());
	$('#taxAmountVal6').val(totalPay6.toFixed(2));
	
	
	let totalPay7= Number($('#taxAmountVal7').val());
	$('#taxAmountVal7').val(totalPay7.toFixed(2));
	
	let totalPay8= Number($('#taxAmountVal8').val());
	$('#taxAmountVal8').val(totalPay8.toFixed(2));
	
	let totalPay9= Number($('#taxAmountVal9').val());
	$('#taxAmountVal9').val(totalPay9.toFixed(2));
	
	let totalPay10= Number($('#taxAmountVal10').val());
	$('#taxAmountVal10').val(totalPay10.toFixed(2));
	
	let totalPay11= Number($('#taxAmountVal11').val());
	$('#taxAmountVal11').val(totalPay11.toFixed(2));
	
	let totalPay12= Number($('#taxAmountVal12').val());
	$('#taxAmountVal12').val(totalPay12.toFixed(2));
	
	let totalPay13= Number($('#taxAmountVal13').val());
	$('#taxAmountVal13').val(totalPay13.toFixed(2));
	
	let totalPay14= Number($('#taxAmountVal14').val());
	$('#taxAmountVal14').val(totalPay14.toFixed(2));
});

function searchAjustmentData(obj) {
	var errorList = [];
	var billMethod = $("#billingMethod").val();
	var methName = '';
	var requestData = {};
	if (billMethod == 'I') {
		var flatNo = $("#flatNo").val();
		if (flatNo == "" || flatNo == undefined || flatNo == null) {
			errorList.push(getLocalMessage('prop.select.flatNo'));
		}
		methName = 'serachAdjustmentDataIndividualBillMethod';
		requestData = {
			"deptId": $("#department").val(),
			"ccnOrPropNo": $("#ccnOrPropNo").val(),
			"oldNo": $("#oldccnOrPropNo").val(),
			"billingMeth": billMethod,
			"flatNo": $("#flatNo").val()
		}
	}
	else {
		methName = 'searchAdjustmentData';

		requestData = {
			"deptId": $("#department").val(),
			"ccnOrPropNo": $("#ccnOrPropNo").val(),
			"oldNo": $("#oldccnOrPropNo").val()
		}
	}

	if (errorList.length == 0) {
		var formName = findClosestElementId(obj, 'form');

		var theForm = '#' + formName;

		var url = 'AdjustmentEntry.html?' + methName;


		var returnData = __doAjaxRequest(url, 'POST', requestData, false, 'html');
		$("#adjustment").html(returnData);
		if (billMethod == 'I') {
			$("#showFlatNo").show();
			$("#serchBtn").show();
			$("#serchBtnDirect").hide();
		}
		return false;
	}

}

function saveData(element) {

	saveObj = element;
	var adjType = $("#adType").val();
	if (adjType != null && adjType != undefined && adjType == 'W') {
		var result = showConfirmBoxSave();
		return result;
	}
	var result = saveOrUpdateForm(element, "Adjustment done Successfully!", 'AdjustmentEntry.html', 'saveform');
	if (adjType == 'B')
		populateAdjAmt(element);
	return result;
}

function saveAuthorization(element) {

	return saveOrUpdateForm(element, "Adjustment done Successfully!",'AdminHome.html','saveAuthorization');
	
}

function showConfirmBoxSave(){
	
	var msg;
	var propNo=$("#ccnOrPropNo").val();
	var oldPropNo= $("#oldccnOrPropNo").val();
	var flatNo = $("#flatNo").val();
 	if (propNo != null && propNo != "" && propNo != undefined)
			msg = 'Property No.'+propNo;
		else
			msg = 'Property No.'+oldPropNo;
 	if (flatNo != "" && flatNo != undefined && flatNo != null
			&& flatNo != '0') {
 		msg=msg+'/Flat No.'+flatNo;
 	}

		var saveorAproveMsg = msg+" "+"will be Cancelled. Do you really want to Proceed?"
		var errMsgDiv = '.msg-dialog-box';
		var message = '';
		var cls = getLocalMessage("account.btn.save.yes");
		var no = getLocalMessage("account.btn.save.no");

		message += '<h4 class="text-center text-blue-2">' + saveorAproveMsg
				+ '</h4>';
		message += '<div class=\'text-center padding-bottom-10\'>  '
				+ '<input type=\'button\' value=\''
				+ cls
				+ '\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '
				+ ' onclick="saveDataAndShowSuccessMsg()"/>   '
				+ '<input type=\'button\' value=\''
				+ no
				+ '\' tabindex=\'0\' id=\'btnNo\' class=\'btn btn-blue-2 autofocus\'    '
				+ ' onclick="closeConfirmBoxForm()"/>' + '</div>';

		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(message);
		$(errMsgDiv).show();
		$('#btnNo').focus();
		showModalBoxWithoutCloseaccount(errMsgDiv);

		return false;

	}

function saveDataAndShowSuccessMsg() {

	var status = saveOrUpdateForm(saveObj, "Adjustment done Successfully!",
		'AdjustmentEntry.html', 'saveform');
	//$.fancybox.close();
	return status;

}

function resetAdjustmentForm(element) {
	$("#adjustmentEntryForm").submit();
}

function fetchAdjustmentHistory(ele) {
	var requestData = {};
	var returnData = __doAjaxRequest(
		'AdjustmentEntry.html?viewHistoryDetails', 'POST', requestData,
		false, 'html');
	openPopupWithResponse(returnData);
}

function getDeptWiseDocumentReady() {
	var requestData = {
		"deptId": $("#department").val()
	};
	var returnData = __doAjaxRequest('AdjustmentEntry.html?getDeptCode',
		'POST', requestData, false, 'json');
	if (returnData == 'AS') {
		$("#serchBtn").show();
		$("#serchBtnDirect").hide();
	} else {
		$("#showFlatNo").hide();
		$("#serchBtn").hide();
		$("#serchBtnDirect").show();
	}
}

function redirectToSearchPropNo(obj) {
	var errorList = [];
	var propNo = $("#ccnOrPropNo").val();
	var oldPropNo = $("#oldccnOrPropNo").val();
	if (propNo == "" && oldPropNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#ccnOrPropNo").val();
		var requestData = {
			"propNo": propNo,
			"oldNo": $("#oldccnOrPropNo").val(),
			"deptId": $("#department").val()
		};

		var ajaxResponse = doAjaxLoading(
			'AdjustmentEntry.html?getBillingMethod', requestData,
			'html');

		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
					$("<option></option>").attr("value", value).text(
						(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			$("#showFlatNo").show();
			$("#serchBtn").hide();
			$("#serchBtnDirect").show();
			$("#billingMethod").val('I');
		} else {
			searchAjustmentData(obj);
		}
	} else {
		displayErrorsOnPage(errorList);
	}

}

function populateAdjAmt(obj) {


	var taxDescArr = [];
	var adjType = $("#adType").val();

	var tableLength = $('#adjDetailTable tr').length;
	for (var j = 0; j < tableLength; j++) // table tr length 
	{

		taxDescArr.push($("#taxDesc" + j).val());
		if (adjType != null && adjType != undefined && adjType == 'W') {
			var adjAmt = $("#taxAMT" + j).val();
			$("#adjAmount" + j).val(adjAmt).prop('readonly', true);
		} else {
			$("#adjAmount" + j).val('0.0').prop('readonly', false);
		}
	}
	addTotalAdjAmount(obj);
	if (adjType == 'B') {

		var rowCount = tableLength - 1;
		if ($.inArray('Boja', taxDescArr) == -1) {
			$('#adjDetailTable tbody tr:last').clone().insertAfter('#adjDetailTable tbody tr:last');
			$('#adjDetailTable tr:last').find('td:first').text(tableLength);
			$('#adjDetailTable tr:last input[id^=taxDesc]').val('Boja');
			$('#adjDetailTable tr:last input[id^=taxDesc]').attr('id', 'taxDesc' + rowCount);

			$('#adjDetailTable tr:last').find('[type="hidden"]:first').val('0');
			$('#adjDetailTable tr:last').find('[type="hidden"]:first').attr('name', 'adjustmentDto.adjDetailDto[' + rowCount + '].taxId');
			$('#adjDetailTable tr:last').find('[type="hidden"]:first').attr('id', 'adjustmentDto.adjDetailDto' + rowCount + '.taxId');

			$('#adjDetailTable tr:last input[id^=taxAmountVal]').val('0.0');
			$('#adjDetailTable tr:last input[id^=taxAmountVal]').attr('id', 'taxAmountVal' + rowCount);


			$('#adjDetailTable tr:last input[id^=taxAMT]').val('0.0');
			$('#adjDetailTable tr:last input[id^=taxAMT]').attr('id', 'taxAMT' + rowCount);

			$('#adjDetailTable tr:last input[id^=adjAmount]').val('0.0');
			$('#adjDetailTable tr:last input[id^=adjAmount]').attr('name', 'adjustmentDto.adjDetailDto[' + rowCount + '].adjAmount');
			$('#adjDetailTable tr:last input[id^=adjAmount]').attr('id', 'adjAmount' + rowCount);

			$('#adjDetailTable tr:last input[id^=adjRemark]').val('');
			$('#adjDetailTable tr:last input[id^=adjRemark]').attr('name', 'adjustmentDto.adjDetailDto[' + rowCount + '].adjRemark');
			$('#adjDetailTable tr:last input[id^=adjRemark]').attr('id', 'adjRemark' + rowCount);
			taxDescArr.push('Boja');
		}
	}
	else {

		var bozaId = $("#BozaId").val();
		if ($.inArray('Boja', taxDescArr) != -1 && bozaId != 'Y') {
			$('#adjDetailTable tr:last').remove();
			taxDescArr.splice($.inArray('Boja', taxDescArr), 1);
		}
	}
	if (adjType != 'B') {
		addTotalAdjAmount(obj);
	}
	if (adjType == 'B') {
		$("#rebateId").text('0.00');
	}
}

function addTotalAdjAmount(obj) {
    debugger;
    var errorList = [];
    var adjType = $("#adType").val();
    var sum = 0; // Initialize sum here

    if (adjType == 'B') {
        var i = 0;
        if ($.fn.DataTable.isDataTable('#adjDetailTable')) {
            $('#adjDetailTable').DataTable().destroy();
        }

        $("#adjDetailTable tbody tr").each(function (i) {
            var taxDesc = $("#taxDesc" + i).val();
            if (taxDesc == 'Boja') {
                var adjAmount = $("#adjAmount" + i).val();
                $("#rebateId").text(adjAmount);
            }
        });
    } else {
        if (adjType == "") {
            errorList.push(getLocalMessage("prop.adjusted.type"));
        }
        if (errorList.length == 0) {
            $("#adjDetailTable tbody tr").each(function (i) {
                var adjAmountStr = $("#adjAmount" + i).val();

                // Check if adjAmountStr is not undefined
                if (adjAmountStr !== undefined) {
                    var adjAmount = parseFloat(adjAmountStr.replace(/,/g, ''));

                    // Check if adjAmount is a valid number
                    if (!isNaN(adjAmount)) {
                        sum += adjAmount;
                    }
                }
            });

            // Update the total in the rebateId element
            $("#rebateId").text(sum.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        } else {
            $("#adjDetailTable tbody tr").each(function (i) {
                $("#adjAmount" + i).val('0.0');
            });
            displayErrorsOnPage(errorList);
            return false;
        }
    }
}