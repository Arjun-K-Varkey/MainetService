
$(document).ready(function() {

	$('#assWard1').prop("disabled", true);
	
	var selectobject = document.getElementById("assWard2");
	for (var i=0; i<selectobject.length; i++) {
	    if (selectobject.options[i].text == 'ALL')
	        selectobject.remove(i);
	} 

});

function SearchButton(obj) {

	showloader(true);
	setTimeout(function() {
		var errorList = [];
		var propNo = $("#assNo").val();
		if (propNo == "" || propNo == undefined || propNo == null) {
			errorList.push(getLocalMessage('property.validMsg.validPropNo'));
		}

		if (errorList.length == 0) {
			var theForm = '#PropertyZoneUpdation';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'PropertyZoneUpdation.html?searchZoneUpdationData';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			$(formDivName).html(returnData);
			//return false;
		} else {
			displayErrorsOnPage(errorList);
		}
		showloader(false);
	}, 2000);
}


function updateZone(element) {
	var errorList = [];
	$.fancybox.close();
	var zone = $("#assWard2").val();
	if (zone == '0' || zone == null || zone == undefined) {
		errorList.push(getLocalMessage('property.selectWordZone'));
		displayErrorsOnPage(errorList);
		return false;
	}
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"PropertyZoneUpdation.html?updateZone", 'POST',
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
			showBoxForApproval(getLocalMessage("property.update.zone.updated")+ " : "+object.propNo);
		}
	}
}

function showBoxForApproval(succesMessage) {

	var childDivName = '.msg-dialog-box';
	var message = '';
	var Proceed = getLocalMessage("proceed");
	var no = 'No';
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
	window.location.href = 'AdminHome.html';
	$.fancybox.close();
}

function resetZoneForm(){
	value = "PropertyZoneUpdation.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();
}

function backToSearchPage(element){ 

	var requestData = {};
	var URL = 'PropertyZoneUpdation.html?backToSearch';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).html(returnData);
}
