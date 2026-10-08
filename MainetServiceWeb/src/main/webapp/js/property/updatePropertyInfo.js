
$(document).ready(function() {

	$('#propertyAddress')
		.find('input, select, textarea, button')
		.attr('disabled', true);
	$('#owner')
		.find('input, select, textarea, button')
		.attr('disabled', true);

	$("#showFlatNo").hide();
	$("#serchBtnDirect").hide();
	yearLength();

});

function redirectToSearchPropNo(obj) {

	showloader(true);
	setTimeout(function() {
		var errorList = [];
		var propNo = $("#assNo").val();
		if (propNo == "") {
			errorList.push(getLocalMessage("property.changeInAss"));
		}
		if (errorList.length == 0) {
			var propNo = $("#assNo").val();
			var requestData = { "propNo": propNo };

			var ajaxResponse = doAjaxLoading(
				'UpdatePropertyInfo.html?getBillingMethod',
				requestData, 'html');

			if (ajaxResponse != null && ajaxResponse != "") {
				var prePopulate = JSON.parse(ajaxResponse);

				$.each(prePopulate, function(index, value) {
					$('#flatNo').append(
						$("<option></option>").attr("value", value).text((value)));
				});
				$('#flatNo').trigger("chosen:updated");
				$("#serchBtn").hide();
				$("#showFlatNo").show();
				$("#serchBtnDirect").show();
				$("#billingMethod").val('I');
			} else {
				SearchButton(obj);
			}
		} else {
			displayErrorsOnPage(errorList);
		}
		showloader(false);
	}, 2000);
}

function SearchButton(obj) {

	showloader(true);
	setTimeout(function() {
		var errorList = [];
		var billMethod = $("#billingMethod").val();
		if (billMethod == 'I') {
			var flatNo = $("#flatNo").val();
			if (flatNo == "" || flatNo == undefined || flatNo == null) {
				errorList.push(getLocalMessage('prop.select.flatNo'));
			}
		}
		if (errorList.length == 0) {
			var theForm = '#UpdatePropertyInfo';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'UpdatePropertyInfo.html?searchPropertyDetails';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			$(formDivName).html(returnData);

			if (billMethod == 'I') {
				$("#showFlatNo").show();
				$("#serchBtn").show();
				$("#serchBtnDirect").hide();
			}
			return false;
		}
		else {
			displayErrorsOnPage(errorList);
		}
		showloader(false);
	}, 2000);
}


function yearLength() {
	var dateFields = $('.dateClass');
	dateFields.each(function() {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}

function updatePropertyInfo(element) {
	var errorList = [];
	$.fancybox.close();
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"UpdatePropertyInfo.html?updatePropertyInfo", 'POST',
		requestData, false, 'json');

	if (object.error != null && object.error != 0) {
		$.each(object.error, function(key, value) {
			$.each(value, function(key, value) {
				if (value != null && value != '') {
					errorList.push(value);
				}
			});
		});
		displayErrorsOnPage(errorList);
	} else {
		if (object.successFlag == 'Y') {
			showBoxForApproval(getLocalMessage("property.update.success"));
		}
	}
}

function showBoxForApproval(succesMessage) {

	var childDivName = '.msg-dialog-box';
	var message = '';
	var Proceed = getLocalMessage("Proceed");
	message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
		+ '</p>';

	message += '<div class=\'text-center padding-bottom-10\'>'
		+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''
		+ Proceed + '\'  id=\'Proceed\' '
		+ ' onclick="closeApproval();"/>' + '</div>';


	$(childDivName).addClass('ok-msg').removeClass('warn-msg');
	$(childDivName).html(message);
	$(childDivName).show();
	$('#Proceed').focus();
	showModalBoxWithoutClose(childDivName);
}

function closeApproval() {
	window.location.href = 'UpdatePropertyInfo.html';
	$.fancybox.close();
}

function isValidEmail1(email) {
	const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	return regex.test(email);
}

function isValidEmail() {
debugger
var billMethod = $("#emailId").val();
	var emailId = $("#emailId").val();
	var filter = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (emailId != "") {
		if (filter.test(emailId)) {
			$('#email').hide();
		} else {
			$('#email').show();
			$('#email').html(getLocalMessage("update.invalid.email"));
			$('#email').css('color', 'red');
		}
	} else {
		$('#email').hide();
		return false;
	}
}
