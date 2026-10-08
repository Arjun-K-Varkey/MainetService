/**
 * Lalit Mohan Prusti
 */
$(document).ready(function() {

	showHideBPL($('#povertyLineId').val());
	$("#povertyLineId").change(function() {
		var isBelowPoverty = $('#povertyLineId').val();
		showHideBPL(isBelowPoverty);
	});

	$("input[name='ulbPlumber']").click(function() {

		if ($(this).attr('value') == 'U') {
			$("#ulbPlumber").show();
			$("#licensePlumber").hide();
			$("#licPlumber").attr("disabled", true);
			$("#plumber").attr("disabled", false);

		} else if ($(this).attr('value') == 'L') {
			$("#ulbPlumber").hide();
			$("#licensePlumber").show();
			$("#plumber").attr("disabled", true);
			$("#licPlumber").attr("disabled", false);
		}
	});

	if ($("input[name='ulbPlumber']").attr('value') == 'U') {

		$("#ulbPlumber").show();
		$("#licensePlumber").hide();
		$("#licPlumber").attr("disabled", true);
	} else {
		$("#ulbPlumber").hide();
		$("#licensePlumber").show();
		$("#plumber").attr("disabled", true);
	}

});
function showHideBPL(value) {
	if (value == 'Y') {
		$('#bpldiv').show();
	} else {
		$('#bpldiv').hide();
	}
}
function openViewModeForm(obj) {

	var rowId = obj.split("E")[1];
	var row = jQuery('#gridWaterDisconnection').jqGrid('getRowData', rowId);
	var colData;

	colData = row['csCcn'];
	$("#csIdn").val(colData);
	colData = row['consumerName'];
	$("#consumerName").val(colData);
	colData = row['csAdd'];
	$("#consumerAreaName").val(colData);

	$.fancybox.close();
}

function saveFormDisconnection(element) {

	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'N'
			|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
					":checked").val() == 'P') {
		return saveOrUpdateForm(
				element,
				"Your application for  Water Disconnection saved successfully!",
				'WaterDisconnectionForm.html?PrintReport', 'saveWaterForm');
	} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
			":checked").val() == 'Y') {

		return saveOrUpdateForm(
				element,
				"Your application for  Water Disconnection saved successfully!",
				'WaterDisconnectionForm.html?redirectToPay', 'saveWaterForm');
	} else {
		return saveOrUpdateForm(element,
				"Your application for Water Disconnection saved successfully!",
				'AdminHome.html', 'saveWaterForm');
	}

}

function tempConnection(val) {
	if ("T" === val) {

		$("#tempDisconnection").show();
		document.getElementById("fromdate").disabled = false;
		document.getElementById("todate").disabled = false;
	} else {

		$("#tempDisconnection").hide();
	}
}
function notUlbPlumber() {
	if ($("#No").is(':checked'))
		$('#plumId').attr('disabled', false);
	else
		$('#plumId').attr('disabled', true);
}

$(function() {
	notUlbPlumber();
	tempConnection($(' #discType option:selected').attr('code'));
	$("#discType").change(function() {

		tempConnection($('option:selected', this).attr('code'));
	});

	/*
	 * $("#datepicker1").datepicker({ dateFormat: 'dd/mm/yy', changeMonth: true,
	 * changeYear: true, onSelect: function(selected) {
	 * $("#datepicker2").datepicker("option","minDate", selected) } });
	 * 
	 * $("#datepicker2").datepicker({ dateFormat: 'dd/mm/yy', changeMonth: true,
	 * changeYear: true, onSelect: function(selected) {
	 * $("#datepicker1").datepicker("option","maxDate", selected) } });
	 */
	/*
	 * $(".datepicker").datepicker({ dateFormat : 'dd/mm/yy', changeMonth :
	 * true, changeYear : true });
	 */
	$(".Moredatepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		minDate : '0',
		onSelect : function(selected) {
			$(".Mostdatepicker").datepicker("option", "minDate", selected)
		}
	});
	$(".Mostdatepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		minDate : '0',
	});
	var dateFields = $('.Moredatepicker');
	dateFields.each(function() {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});

	var toFields = $('.Mostdatepicker');
	toFields.each(function() {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});

});


function searchConnectionForSkdcl(element) {     
	var errorList = [];
    var connectionNo = $("#csIdn").val();

    if (connectionNo == '' || connectionNo == undefined) {
		errorList.push(getLocalMessage('water.meterCutOff.validationMSG.connectionNo'));
    }

	if (errorList.length == 0) {

		var formName = findClosestElementId(element, 'form');
		
		var requestData = {};
		var theForm = '#' + formName;
		requestData = __serializeForm(theForm);
		
		var URL = 'WaterDisconnectionForm.html?getDuesForConnNoSKDCL';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		
		if(returnData ==null || (returnData.duesAmount==null && returnData.propertyNo != null) ){
			$('.ccnDuesList').html('');
			$('#confirmToProceedId').attr('disabled', false);
			var errorMsg = ['No Property Linked Wtih Connection'];
		    displayErrorsOnPageView(errorMsg);
			return;
		}
		var htmlString = '';
		htmlString+='<div class="form-group"> ';
		htmlString+='<label class="col-sm-2 control-label ">Property Dues Amount</label> ';
		htmlString+='<div class="col-sm-4"> ';
		htmlString+='<input type="text"  value="' + returnData.duesAmount + '" class="form-control text-right" readonly="true">'
		+ '</input>';
		htmlString+='</div>';
		
		htmlString+='</div>';
		if(returnData !=null && returnData.ccnDuesList != null && (Object.keys(returnData.ccnDuesList).length > 0)) {
			for (var [key, value] of Object.entries(returnData.ccnDuesList)) {
	  			console.log(`${key}: ${value}`);
	  			
	  			htmlString+='<div class="form-group"> ';
	  			htmlString+='<label class="col-sm-2 control-label ">CCN</label> ';
	  			htmlString+='<div class="col-sm-4"> ';
	  			htmlString+='<input type="text" class="form-control" value="' + key + '" readonly="true"></input>';
	  			htmlString+='</div>';

				htmlString+='<label class="col-sm-2 control-label ">Water Dues Amount</label> ';
	  			htmlString+='<div class="col-sm-4"> ';
	  			htmlString+='<input type="text"  value="' + value + '" class="form-control text-right" readonly="true">'
	  			+ '</input>';
	  			htmlString+='</div>';
	  			
	  			htmlString+='</div>';
			}
		}
		
		$('.ccnDuesList').html(htmlString);
		 if(returnData.dues){
		    $('#confirmToProceedId').attr('disabled', true);
		    var errorMsg = ['Dues are pending!'];
		    displayErrorsOnPageView(errorMsg);
		}else{
			$('.ccnDuesList').html('');
			$('#confirmToProceedId').attr('disabled', false);
		}
   	}
	    
}

function search(obj) {

	var formName = "#" + findClosestElementId(obj, 'form');
	var requestData = __serializeForm(formName);
	// console.log(requestData);
	var url = "WaterDisconnectionForm.html?searchConnectionDetails";
	var returnData = __doAjaxRequestForSave(url, 'post', requestData, false,
			'', obj);
	$(formDivName).html(returnData);
	if($('#validationerrordiv').length === 0) {
		searchConnectionForSkdcl(obj);
		$('#confirmToProceedId').attr('disabled', false);
	}else{
		$('#confirmToProceedId').attr('disabled', true);
	}
	
	
}

function getChecklistAndCharges(obj) {

	var errorList = [];
	/*
	 * errorList = validateApplicantInfo(errorList); errorList =
	 * validateOldOwnerInfo(errorList); errorList =
	 * validateNewOwnerInfo(errorList);
	 */
	var isBPL = $('#povertyLineId').val();

	$('.appendableClass').each(function(i) {
		row = i + 1;
		if (isAdditionalOwnerDetailRequired(i) == 'Y') {
			errorList = validateAdditionalOwnerTableData(errorList, i);
		}
	});

	if (errorList.length == 0) {
		var formName = findClosestElementId(obj, 'form');
		var theForm = '#' + formName;
		var requestData = {
			"isBPL" : isBPL
		};

		requestData = __serializeForm(theForm);

		var url = 'WaterDisconnectionForm.html?getCheckListAndCharges';
		var returnData = __doAjaxRequest(url, 'post', requestData, false, '',
				obj);

		$(formDivName).html(returnData);
		$('#povertyLineId').val(isBPL);
		$('#searchConnection').attr('disabled', true);
		/* $('#confirmToProceedId').attr('disabled',true); */

	} else {
		displayErrorsOnPage(errorList);
	}
}

function displayErrorsOnPageView(errorList) {
	var errMsg = '<button type="button" class="close" onclick="closeOutErrBox()" aria-label="Close"><span aria-hidden="true">&times;</span></button>';

	errMsg += '<ul>';

	$.each(errorList, function(index) {
		errMsg += '<li>' + errorList[index] + '</li>';
	});

	errMsg += '</ul>';

	$('html,body').animate({
		scrollTop : 0
	}, 'slow');
	$('.error-div').html(errMsg);
	$(".error-div").show();

	return false;
}

function resetDisconnection() {
	$("#csIdn").val("").attr("readonly", false);
	$("#consumerName").val("");
	$("#consumerAreaName").val("");
	$("#discType").val("");
	$("#discReason").val("");
	$("#fromdate").val("");
	$("#todate").val("");
	$(".error-div").hide();
	$("#consumerName").val('');
	$("#csIdn").val('');

}

/*
 * $(".datepicker").datepicker({ dateFormat: 'dd/mm/yy', changeMonth: true,
 * changeYear: true });
 */
 
 function getConnectionDetails(){
	var errorList = [];
	var connNo = $("#csIdn").val();
	if(connNo != ''){
		var data = {
		"csCcn": connNo,
		};	
	}else{
		errorList.push(getLocalMessage('water.ccn.empty'));
		displayErrorsOnPage(errorList);
		return;
	}
	var URL = 'WaterDisconnectionForm.html?getConnectionDetails';
	var ajaxResponse = __doAjaxRequest(URL, 'POST', data, false, 'json');
	if (ajaxResponse && ajaxResponse.length != 0) {
	    $("#consumerName").val(ajaxResponse);
	    errorList = [];
	    $('#confirmToProceedId').attr('disabled', false);
	}else{ 
		errorList.push("Connection number is not valid or its billing may not be active");
		displayErrorsOnPage(errorList);
		$('#confirmToProceedId').attr('disabled', true);
	}	
}

