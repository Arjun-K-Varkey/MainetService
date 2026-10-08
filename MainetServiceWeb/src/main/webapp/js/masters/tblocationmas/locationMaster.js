var availableLocaNameTags = [];
var availableLocaAreaTags = [];

$(document).ready(function() {
	$("input").on("keypress", function(e) {
		if (e.which === 32 && !this.value.length)
			e.preventDefault();
	});
	setViewImageMode();
	if ($("#operationalChkBxId").is(":checked")) {
		$(".operational-box").show();
	} else {
		$(".operational-box").hide();
	}

	if ($("#revenueChkBxId").is(":checked")) {
		$(".revenue-box").show();
	} else {
		$(".revenue-box").hide();
	}

	if ($("#electoralChkBxId").is(":checked")) {
		$(".electoral-box").show();
	} else {
		$(".electoral-box").hide();
	}
	if ($('#hiddeValue').val() == 'E') {
		$('input[type="text"],textarea').prop("disabled", true);
		$("#isDeptLoc").prop("disabled", true);
		$("#backBtn").removeProp("disabled");
		$('#dpDeptId').removeProp("disabled");
		$("#latitudeId").prop("disabled", false);
		$("#longitudeId").prop("disabled", false);
		$("#lacCodeId").prop("disabled", false);
	}

	if ($('#hiddeValue').val() == 'V') {
		
		$(".electoral-box :input").prop("disabled", true);
		$(".revenue-box :input").prop("disabled", true);
		$('#operationalChkBxId').attr('readonly', true);
		$("#disableInput :input").prop("disabled", true);
		$('.operational-box input:not(:first)').prop("disabled", true);
		$("#backBtn").removeProp("disabled");

	}

	var locNameList = $("#locNameList").val().split(',');
	var locAreaList = $("#locAreaList").val().split(',');

	$.each(locNameList, function(index, value) {
		availableLocaNameTags.push(value);
	});

	$.each(locAreaList, function(index, value) {
		availableLocaAreaTags.push(value);
	});

});

function resetLocationForm() {
	$('.warning-div').empty();
	 var ajaxResponse = __doAjaxRequest('LocationMas.html?form', 'POST', {}, false,'html');
		$('.content').html(ajaxResponse);

}

function saveData(obj) {
	var errorList = [];
	var address = $.trim($("#locAddressId").val());
	var addressReg = $.trim($("#locAddressRegId").val());
	var areaReg = $.trim($("#locationAreaRegId").val());
	var area = $.trim($("#locationAreaId").val());
	var locationName = $.trim($("#locationNameId").val());
	var locationNameReg = $.trim($("#locationNameRegId").val());
	var isDeptLoc = $("#isDeptLoc").val();
	var elecChkBx = $("#electoralChkBxId").is(':checked');
	var revChkBx = $("#revenueChkBxId").is(':checked');
	var operChkBx = $("#operationalChkBxId").is(':checked');
	var status = $('#locActive').val();
	var locCategory = $('#locCategory').val();
	
	

	if (locationName == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locNameEng'));
	}
	if (locationNameReg == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locNameReg'));
	}
	if (area == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locAreaEng'));
	}
	if (areaReg == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locAreaReg'));
	}
	if (address == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locAddressEng'));
	}
	if (addressReg == '') {
		errorList.push(getLocalMessage('locationMas.valMSG.locAddressReg'));
	}

	if (isDeptLoc == '0') {
		errorList.push(getLocalMessage('locationMas.valMSG.isDeptLoc'));
	}

	if (status == '0') {
		errorList.push(getLocalMessage('locationMas.valMSG.status'));
	}
	if (locCategory == '0') {
		errorList.push(getLocalMessage('master.select.location.category'));
	}
	errorList = validateElectoral(errorList);
	errorList = validateRevenue(errorList);
	errorList = validateOperational(errorList);

	if ($('#hiddeValue').val() == 'C') {
		if (locationName != '' && area != '') {
			var locationName = $.trim($("#locationNameId").val());
			var area = $.trim($("#locationAreaId").val());
			var url = "LocationMas.html?validateLocationNameAndArea";
			var requestData = {
				"locationName" : locationName,
				"locaArea" : area
			}
			var response = __doAjaxRequest(
					"LocationMas.html?validateLocationNameAndArea", 'post',
					requestData, false, 'html');
			if (response > 0) {
				errorList.push(getLocalMessage('location.error.duplicateLoc'));
			}
		}
	}
	if (errorList.length == 0) {
		saveOrUpdateForm(obj, "", 'LocationMas.html', 'saveform');
	} else {
		displayErrorsOnPage(errorList);
	}
	return false;
}

function validateElectoral(errorList) {

	var electoralChkBx = $("#electoralChkBxId").is(':checked');
	if (electoralChkBx == true) {

		var elecLevel1 = $("#codIdElecLevel1").val();
		var elecLevel2 = $("#codIdElecLevel2").val();
		var elecLevel3 = $("#codIdElecLevel3").val();
		var elecLevel4 = $("#codIdElecLevel4").val();
		var elecLevel5 = $("#codIdElecLevel5").val();

		if (elecLevel1 != undefined && elecLevel1 == '0') {
			errorList.push("Please Select Electoral Ward Zone Level 1 ");
		}
		if (elecLevel2 != undefined && elecLevel2 == '0') {
			errorList.push("Please Select Electoral Ward Zone Level 2 ");
		}

		if (elecLevel3 != undefined && elecLevel3 == '0') {
			errorList.push("Please Select Electoral Ward Zone Level 3 ");
		}

		if (elecLevel4 != undefined && elecLevel4 == '0') {
			errorList.push("Please Select Electoral Ward Zone Level 4 ");
		}

		if (elecLevel5 != undefined && elecLevel5 == '0') {
			errorList.push("Please Select Electoral Ward Zone Level 5 ");
		}
	}
	return errorList;

}

function validateRevenue(errorList) {
	var revenueChkBx = $("#revenueChkBxId").is(':checked');
	if (revenueChkBx == true) {

		var revLevel1 = $("#codIdRevLevel1").val();

		if (revLevel1 == '0') {
			errorList.push("Please Select Revenue Field Mapping. ");
		}
	}
	return errorList;
}

function validateOperational(errorList) {
	var operationChkBx = $("#operationalChkBxId").is(':checked');
	var dptId = $('#dpDeptId').val();
	if (operationChkBx == true) {

		if (dptId != '0') {
			var OperLevel1 = $("#codIdOperLevel1").val();
			var OperLevel2 = $("#codIdOperLevel2").val();
			var OperLevel3 = $("#codIdOperLevel3").val();
			var OperLevel4 = $("#codIdOperLevel4").val();
			var OperLevel5 = $("#codIdOperLevel5").val();

			if (OperLevel1 != undefined && OperLevel1 == '0') {
				errorList.push("Please Select Operational Ward Zone Level 1 ");
			}
			if (OperLevel2 != undefined && OperLevel2 == '0') {
				errorList.push("Please Select Operational Ward Zone Level 2 ");
			}

			if (OperLevel3 != undefined && OperLevel3 == '0') {
				errorList.push("Please Select Operational Ward Zone Level 3 ");
			}

			if (OperLevel4 != undefined && OperLevel4 == '0') {
				errorList.push("Please Select Level Operational Ward Zone 4 ");
			}

			if (OperLevel5 != undefined && OperLevel5 == '0') {
				errorList.push("Please Select Level Operational Ward Zone 5 ");
			}
		} else {
			errorList
					.push("Please Select Department for Operational Ward Zone ");
		}
	}
	return errorList;
}

function displayErrorsOnPage(errorList) {
	var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
	$.each(errorList, function(index) {
		errMsg += '<li><i class="fa fa-exclamation-circle"></i>&nbsp;'
				+ errorList[index] + '</li>';
	});
	errMsg += '</ul>';
	$(".warning-div").html(errMsg);
	$(".warning-div").removeClass('hide')
	$('html,body').animate({
		scrollTop : 0
	}, 'slow');
	errorList = [];
	return false;
}

function closeErrBox() {
	$('.warning-div').addClass('hide');
}

function showConfirmBox() {
	var errMsgDiv = '.msg-dialog-box';
	var message = '';
	var cls = 'Proceed';

	message += '<p>Form Submitted Successfully</p>';
	message += '<p style=\'text-align:center;margin: 5px;\'>'
			+ '<br/><input type=\'button\' value=\'' + cls
			+ '\'  id=\'btnNo\' class=\'css_btn \'    '
			+ ' onclick="proceed()"/>' + '</p>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBox(errMsgDiv);
	return false;
}

function proceed() {
	window.location.href = 'LocationMas.html';
}
function goToDashBoard() {
	window.location.href = 'LocationMas.html';
}

function addMoreDepartment() {
	$("#deptName").clone(true, true).insertAfter("div.exportClass:last");
	return false;
}

function deleteLocationMasterData() {
	$("#Submit").hide();
	var url = 'LocationMas.html?deleteLocationMaster';
	var requestData = "selectedId=" + selectedIds;
	$
			.ajax({
				url : url,
				data : requestData,
				type : 'POST',
				datatype : "json",
				success : function(response) {
					$("#locationmastergrid").jqGrid('setGridParam', {
						datatype : 'json'
					}).trigger('reloadGrid');
				},
				error : function(xhr, ajaxOptions, thrownError) {
					var errorList = [];
					errorList
							.push(getLocalMessage("admin.login.internal.server.error"));
					showError(errorList);
				}
			});

}

function showConfirmBoxEmployee() {
	var errMsgDiv = '.msg-dialog-box';
	var message = '';
	var cls = 'Yes';

	message += '<p class="padding-top-10">Are you sure want to delete ?</p>';
	message += '<p style=\'text-align:center;margin: 5px;\'>'
			+ '<br/><input type=\'button\' value=\'' + cls
			+ '\'  id=\'btnNo\' class=\'btn btn-default \'    '
			+ ' onclick="deleteLocationMasterData()"/>' + '</p>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBox(errMsgDiv);
}

function addDepartment(obj) {
	var requestData = 'deptId=' + $('#dpDeptId').val();
	if ($('#dpDeptId').val() != '0') {
		var response = __doAjaxRequest("LocationMas.html?areaMapping", 'post',
				requestData, false, 'html');
		$('#areaMappingId').html(response);
	} else {
		$('#areaMappingId').html('');
	}
}

function addDepartmentEdit(obj) {
	var requestData = 'deptId=' + $('#dpDeptId').val() + '&locId='
			+ $('#locId').val();
	if ($('#dpDeptId').val() != '0') {
		var response = __doAjaxRequest("LocationMas.html?areaMappingEdit",
				'post', requestData, false, 'html');
		$('#areaMappingId').html(response);
		if ($('#hiddeValue').val() == 'V') {
			$('#areaMappingId :input').prop("disabled", true);
			$("#backBtn").removeProp("disabled");
			$('#dpDeptId').removeProp("disabled");
		}
	} else {
		$('#areaMappingId').html('');
	}
}

$(function() {
	$("#locationNameId").autocomplete({
		highlightClass : "bold-text",
		source : availableLocaNameTags
	});
});

$(function() {
	$("#locationAreaId").autocomplete({
		source : availableLocaAreaTags
	});
});

function checkIfEmployeeExists() {
	var errorList = [];
	var isDeptLoc = $('#isDeptLoc').val();
	if (isDeptLoc == 'Y') {
		var locId = $('#locId').val();

		var url = "LocationMas.html?checkIfDeptAndEmpMapped";
		var requestData = {
			"locId" : locId
		}
		var response = __doAjaxRequest(url, 'post', requestData, false, '');
		if (!$.isEmptyObject(response) && response != null && response != "") {
			errorList.push(response);
			$('#locActive').val('Y');
		}
		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
		}
	}
}

function showMsgModalBoxLoc(childDialog) {
	$.fancybox({
		type : 'inline',
		href : childDialog,
		openEffect : 'elastic',
		helpers : {
			overlay : {
				closeClick : false
			}
		},
		keys : {
			close : null
		}
	});
	return false;
}
$('#electoralChkBxId').click(function() {
	if ($(this).is(":checked")) {
		$(".electoral-box").show();
	} else {
		$(".electoral-box").hide();
	}
});

$('#revenueChkBxId').click(function() {
	if ($(this).is(":checked")) {
		$(".revenue-box").show();
	} else {
		$(".revenue-box").hide();
	}
});

$('#operationalChkBxId').click(function() {
	if ($(this).is(":checked")) {
		$(".operational-box").show();
	} else {
		$(".operational-box").hide();
	}
});




function otherTask() {
		var URL = 'LocationMas.html?getCoordinates';
		var data = {};
		var returnData = __doAjaxRequest(URL, 'post', data, false, 'json');
		if(returnData[0]!=0){
			$("#latitudeId").val(returnData[1]!=0?returnData[1]:null);
			$("#longitudeId").val(returnData[0]!=0?returnData[0]:null);
			 $("#locChnage").val('Y');
		}else{
			$("#latitudeId").val(null);
			$("#longitudeId").val(null);
			$("#locChnage").val('N');
		}
	}

function setViewImageMode(){
		var modeVal=$('#hiddeValue').val();
		var imagePath=$('#hiddenlocPath').val();
		if(imagePath != "" && imagePath != null){
			if(modeVal==='V'){
			$('#uploadPreview ul')
			.append(
					'<li id="0_file_0_t"><img src="'+imagePath+'" width="150" height="150" class=""></li>');
			$('#viewDoc').show();
			}
			else if(modeVal==='E'){
				$('#uploadDoc').hide();
				$('#uploadPreview ul')
				.append(
						'<li id="0_file_0_t"><img src="'+imagePath+'" width="150" height="150" class=""><a  href="#" onclick="doFileDeletephoto(this);" id="0_file_0" title="Delete"><i class="fa fa-trash fa-lg text-danger btn-lg"></i></a></li>');
		$('#viewDoc').show();
			}
		}
}


	function doFileDeletephoto(obj) {
		$('#viewDoc').hide();
		$('#uploadDoc').show();
		
	}
