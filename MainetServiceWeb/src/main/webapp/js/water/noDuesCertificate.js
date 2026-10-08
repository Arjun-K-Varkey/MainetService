function saveOrUpdateFormforwaternodue(obj, successMessage, successUrl, actionParam)
{
 
 
	
	if (!actionParam) {
		
		actionParam = "save";
	}
	
	//return doFormAction(obj,successMessage, actionParam, true , successUrl);
	return doFormActionForSaveforwaternodues(obj,successMessage, actionParam, true , successUrl);
}

function doFormActionForSaveforwaternodues(obj,successMessage, actionParam, sendFormData, successUrl)
{
	 
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	
	             var requestData = {};
	
				if (sendFormData) {
					
					requestData = __serializeForm(theForm);
				}
			
				var url	=	$(theForm).attr('action')+'?' + actionParam;
				 
				
				var returnData=__doAjaxRequestForSave(url, 'post',requestData, false,'',obj);
				if ($.isPlainObject(returnData))
				{
					var message = returnData.command.message;
					
					var hasError = returnData.command.hasValidationError;
					
					if (!message) {
						message = successMessage;
					}
					
					if(message && !hasError)
						{
						   	if(returnData.command.hiddenOtherVal == 'SERVERERROR')
						   		
						   		showSaveResultBoxwithcancel(returnData, message, 'AdminHome.html');
						   	
						   	else
						   		
						   		showSaveResultBoxwithcancel(returnData, message, successUrl);
						}
					else if(hasError)
					{
						$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');	
					}
					else
						return returnData;
					
				}
				else if (typeof(returnData) === "string")
				{
					$(formDivName).html(returnData);	
					prepareTags();
				}
				else 
				{
					alert("Invalid datatype received : " + returnData);
				}
				showloader(false);
				return false;
				
	
}



$(document).ready(function() {

    $('#consumerDetails').hide();
    $("#back").hide();
    $('#copies').hide();
    $('#chekListChargeId').hide();
    $("#chargesDiv").hide();
    $("#duelist").hide();
    //$("#NodivSubmit").hide();
    $("#checkListDetails").hide();
    $('#chekListChargeId').hide();
    if ($('#free').val() == 'F') {
	$("#payment").hide();
    } else {
	$("#payment").show();
    }
    if ($('#povertyLineId').val() == 'N') {
	$("#bpldiv").hide();
    }
    if ($('#povertyLineId').val() == 'Y') {
	$("#bpldiv").show();
    } else {
	$("#bpldiv").hide();
    }

    $("#povertyLineId").change(function(e) {

	if ($("#povertyLineId").val() == 'Y') {

	    $("#bpldiv").show();

	} else {
	    $("#bpldiv").hide();
	}

    });

});
function saveNoDuesCertificateForm(element) {
    var errorList = [];
    var errorList = validateApplicantInfo(errorList);

    if (errorList.length == 0) {
	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
		":checked").val() == 'N'
		|| $("input:radio[name='offlineDTO.onlineOfflineCheck']")
			.filter(":checked").val() == 'P') {

	    var status = generateReport(element,'NoDuesCertificateController.html','PrintReport');
	    //openNoDuesWindowTab(status);
	    return status

	} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']")

	.filter(":checked").val() == 'Y') {
		
		return saveOrUpdateFormforwaternodue(
		    element,
		    "Your application for No Dues Certificate saved successfully!",
		    'NoDuesCertificateController.html?redirectToPay', 'save');
	} else{ 
		var status = saveOrUpdateFormforwaternodue(
			    element,
			    "Your application for No Dues Certificate saved successfully!",
			    'AdminHome.html', 'save');
		    //openNoDuesWindowTab(status);
	}
    } else {
	$("#NodivSubmit").show();
	displayErrors(errorList);

    }

}

function openNoDuesWindowTab(status) {
    if (!status) {
	var URL = 'NoDuesCertificateController.html?noDueCertificatePrint';
	var returnData = __doAjaxRequest(URL, 'POST', {}, false);

	var title = 'No Dues Certificate';
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
		.write('<script src="js/mainet/ui/jquery.min.js"></script>')
	printWindow.document
		.write('<script type="text/javascript" src="assets/libs/excel-export/excel-export.js"></script>')
	printWindow.document
		.write('<script>$(window).on("load",function() {$(".table-pagination, .remove-btn, .paging-nav, tfoot").remove(); $(".table thead tr th").removeClass("tablesorter-headerDesc tablesorter-headerAsc tablesorter-header"); $(".table tr").removeAttr("style");});</script>')
	printWindow.document.write('</head><body style="background:#fff;">');
	printWindow.document
		.write('<div style="position:fixed; width:100%; bottom:0px; z-index:1111;"><div class="text-center"><button onclick="window.print();" class="btn btn-success hidden-print" type="button"><i class="fa fa-print" aria-hidden="true"></i> Print</button>  <button onClick="window.close();" type="button" class="btn btn-blue-2 hidden-print">Close</button></div></div>')
	printWindow.document.write(returnData);
	printWindow.document.write('</body></html>');
	printWindow.document.close();

    }
}

function validateNoDueFormData(errorList) {

    var applicantTitle = $.trim($('#applicantTitle').val());
    var firstName = $.trim($('#firstName').val());
    var lastName = $.trim($('#lastName').val());
    var applicantMobileNo = $.trim($('#mobileNo').val());
    var villTownCity = $.trim($('#villTownCity').val());
    var blockName = $.trim($('#blockName').val());
    var applicantPinCode = $.trim($('#pinCode').val());
    var conNum = $.trim($('#consumerNo').val());
    // var finYear= $.trim($('#onYearSelect').val());
    var cooNoname = $.trim($('#consumerName').val());
    var isBPL = $.trim($('#povertyLineId').val());
    var isBPLNO = $.trim($('#bplNo').val());
    var free = $.trim($('#free').val());

    var payMode = $("input:radio[name='offlineDTO.onlineOfflineCheck']")
	    .filter(":checked").val();

    if (applicantTitle == "" || applicantTitle == '0'
	    || applicantTitle == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantNameTitle'));
    }
    if (firstName == "" || firstName == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantFirstName'));
    }
    if (lastName == "" || lastName == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantLastName'));
    }
    if (applicantMobileNo == "" || applicantMobileNo == undefined) {
	errorList.push(getLocalMessage('water.validation.applicantMobileNo'));
    }
    if (villTownCity == "" || villTownCity == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantcityVill'));
    }
    /*
     * if(blockName == "" || blockName == undefined){
     * errorList.push(getLocalMessage('water.validation.ApplicantBlockName')); }
     */
    if (applicantPinCode == "" || applicantPinCode == undefined) {
	errorList.push(getLocalMessage('water.validation.applicantPinCode'));
    }
    if (conNum == "" || conNum == undefined) {
	errorList.push(getLocalMessage('water.validation.connectionno'));
    }
    /*
     * if(finYear == "" || finYear =='0' || finYear == undefined){
     * errorList.push(getLocalMessage('water.validation.finYear')); }
     */
    if (cooNoname == "" || cooNoname == undefined) {
	errorList.push(getLocalMessage('water.validation.ownerfirstname'));
    }
    if (free != 'F') {
	if (payMode == "" || payMode == undefined) {
	    errorList.push(getLocalMessage('water.validation.paymode'));
	}
    }

    if (isBPL == "0" || isBPL == undefined) {

	errorList.push(getLocalMessage('water.validation.isabovepovertyline'));
    }
    if (isBPL == "Y" && isBPLNO == '') {

	errorList.push(getLocalMessage('water.validation.bplnocantempty'));
    }

    return errorList;

}

function saveNoDuesCertificateExeForm(element) {

    var errorList = [];
    errorList = validateNoDuesExFormData(errorList);
    if (errorList.length == 0) {
	return saveOrUpdateForm(element,
		"Your application for No Dues Certificate saved successfully!",
		'AdminHome.html', 'saveExecutionForm');

    } else {
	$("#NodivSubmit").show();
	displayErrors(errorList);
    }
}

function validateNoDuesExFormData(errorList) {

    var approveBy = $.trim($('#approveBy').val());
    var approveDate = $.trim($('#datepicker').val());
    var noDuesCertiDate = $.trim($('#datepicker2').val());

    if (approveBy == "" || approveBy == undefined) {
	errorList.push(getLocalMessage('water.valid.approveBy'));
    }
    if (approveDate == "" || approveDate == undefined) {
	errorList.push(getLocalMessage('water.valid.approveDate'));
    }
    if (noDuesCertiDate == "" || noDuesCertiDate == undefined) {
	errorList.push(getLocalMessage('water.valid.dueDate'));
    }

    return errorList;

}
function getNODuesChecklistAndCharges(element) {
     
    var errorList = [];
    $("#applicantTitle").prop("disabled", false);
    $("#firstName").prop("disabled", false);
    $("#middleName").prop("disabled", false);
    $("#lastName").prop("disabled", false);
    $("#mobileNo").prop("disabled", false);
    $("#emailId").prop("disabled", false);
    $("#gender").prop("disabled", false);
    var isBPL = $.trim($('#povertyLineId').val());
    var isBPLNO = $.trim($('#bplNo').val());
    // var fin =$('select#onYearSelect').val();
    var noOfCopies = $("#noOfCopies").val();

    // var validateOtp = $("#validateOtp").val();
    /*
     * if( fin==null || fin[0] =="-1" ) {
     * errorList.push(getLocalMessage('water.validation.finYear')); }
     */
    if (noOfCopies == '' || noOfCopies == 'undefined') {
	errorList.push(getLocalMessage('water.validation.noOfCopies'));
    }

    if (isBPL == "0" || isBPL == undefined) {

	errorList.push(getLocalMessage('water.validation.isabovepovertyline'));
    }
    if (isBPL == "Y" && isBPLNO == '') {
	errorList.push(getLocalMessage('water.validation.bplnocantempty'));
    }

    if (errorList.length == 0) {

	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'NoDuesCertificateController.html?getCharges';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);

	var errMsg = returnData["errMsg"];
	if (errMsg != '' && errMsg != undefined) {
	    var errorList = [];
	    errorList.push(errMsg);
	    displayErrors(errorList);
	}
	var divName = '#NoDuesCertiForm';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$(divName).show();
	var free = $('#free').val();

	if (free == 'F') {
	    $('#checkListDetails').show();
	    //$('#chekListChargeId').show();
	    $('.required-control').next().children().addClass('mandColorClass');
	} else {

	    $("#chargesDiv").show();
	    $('#checkListDetails').show();
	    $("#payment").show();
	    $('#chekListChargeId').hide();
	    $("#NodivSubmit").show();
	    document.getElementById('consumerNo').readOnly = true;
	}

	$("#checkListDetails").show();
	$('#consumerDetails').show();
	//$("#checkListDetails").show();
	$('#copies').show();
	$("#noOfCopies").prop("readonly", true);
	$("#NodivSubmit").show();
	$("#viewdues").prop('disabled', true);
	$("#resetConnection").prop('disabled', true);
    } else {

	displayErrors(errorList);
    }
}



function searchConnectionForSkdcl(element) {              			
	var errorList = [];
    errorList = validateApplicantInfo(errorList);
    var connectionNo = $("#consumerNo").val();

    if (connectionNo == '' || connectionNo == undefined) {
		errorList.push(getLocalMessage('water.meterCutOff.validationMSG.connectionNo'));
    }

	if (errorList.length == 0) {

		var formName = findClosestElementId(element, 'form');
		var divName = '.content';
		
		var requestData = {};
		var theForm = '#' + formName;
		requestData = __serializeForm(theForm);
		
		var URL = 'NoDuesCertificateController.html?getConnectionDetailForSKDCL';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		/*Added conditions for Property Dues is not null*/ 
		if(returnData ==null || returnData.duesAmount==null || returnData.duesAmount==0 ||returnData.ccnDuesList != null || returnData.ccnDuesList == 0){
			
			var errorMsg = [];
			if(returnData.chequeClearanceStatus == 'Y' && returnData.isBPL!==null){
		    	errorList.push(returnData.isBPL);
		    	displayErrors(errorList);
		    	return false;
		    }
			// First priority showing error
			if(returnData && returnData.errorMsg){
			    var errorList = [];
			    errorList.push(returnData.errorMsg);
			    displayErrors(errorList);
			    return false;
			}
		
			$('#copies').show();
			$("#consumerName").val(returnData.consumerName);
			$("#consumerAddress").val(returnData.consumerAddress);
			$('#consumerDetails').show();
			$("#propertyNo").val(returnData.propertyNo);
			if (returnData.propertyNo) {
			    $("#propertyNo").closest('#propNo').show();
			} else {
			    $("#propertyNo").closest('#propNo').hide();
			}

			$('#chekListChargeId').show();
			$("#consumerNo").prop("readonly", true);
			$('.ccnDuesList').html('');
			$("#back").hide();
		    
		    
		    if ($('#free').val() == 'F') {
			$("#payment").hide();
		    } else {
			$("#payment").show();
		    }
			    
		    if ($('#povertyLineId').val() == 'Y') {
				$("#bpldiv").show();
			} else {
				$("#bpldiv").hide();
			}
		    $('#duesAmount').val(0);
		    var errorMsg;
		    if(returnData ==null){
		    	errorMsg = ['No Property Linked Wtih Connection'];
		    	displayErrors(errorMsg);
			}
			return;
		}
		
		$('#duesAmount').val(returnData.duesAmount);
		
		 var htmlString = '';
	/*	
	 if(returnData !=null && returnData.ccnDuesList != null && (Object.keys(returnData.ccnDuesList).length > 0)) {			
			for (var [key, value] of Object.entries(returnData.ccnDuesList)) {
	  			
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
		*/
		if (returnData != null && returnData.waterDues != null||returnData.ccnDuesList==null  ) {
		    htmlString += '<div class="form-group">';
		    htmlString += '<label class="col-sm-2 control-label ">Water Dues Amount</label>';
		    htmlString += '<div class="col-sm-4">';
		    htmlString += '<input type="text" value="' + returnData.duesAmount + '" class="form-control text-right" readonly="true"></input>';
		    htmlString += '</div>';
		    htmlString += '</div>';
		} 
		else if (returnData != null && returnData.waterDues == "waterDues") {
		    $('#duesAmount').val(''); 
		    htmlString += '<div class="form-group"> ';
		    htmlString += '<label class="col-sm-2 control-label ">Water Dues Amount</label> ';
		    htmlString += '<div class="col-sm-4"> ';
		    htmlString += '<input type="text" value="' + returnData.duesAmount + '" class="form-control text-right" readonly="true"></input>';
		    htmlString += '</div>';
		    htmlString += '</div>';
		}

		$('.ccnDuesList').html(htmlString);

		 
		 /*if (returnData != null && returnData.ccnDuesList != null) {
			    var key = Object.keys(returnData.ccnDuesList)[0]; 
			    var value = returnData.ccnDuesList[key]; 

			    htmlString += '<div class="form-group">';

			    htmlString += '<label class="col-sm-2 control-label ">Water Dues Amount</label>';
			    htmlString += '<div class="col-sm-4">';
			    htmlString += '<input type="text" value="' + value + '" class="form-control text-right" readonly="true"></input>';
			    htmlString += '</div>';

			    htmlString += '</div>';
			}

		 
		else if(returnData !=null && returnData.waterDues == "waterDues"){
			$('#duesAmount').val('');
			htmlString+='<div class="form-group"> ';
			htmlString+='<label class="col-sm-2 control-label ">Water Dues Amount</label> ';
  			htmlString+='<div class="col-sm-4"> ';
  			htmlString+='<input type="text"  value="' + returnData.duesAmount + '" class="form-control text-right" readonly="true">'
  			+ '</input>';
  			htmlString+='</div>';
  			
  			htmlString+='</div>';
		}
		
		$('.ccnDuesList').html(htmlString);
		 
		*/
		
		if(returnData.dues){
			$("#consumerName").val(returnData.consumerName);
			$("#consumerAddress").val(returnData.consumerAddress);
			$('#consumerDetails').show();
			$("#propertyNo").val(returnData.propertyNo);
			if (returnData.propertyNo) {
			    $("#propertyNo").closest('#propNo').show();
			} else {
			    $("#propertyNo").closest('#propNo').hide();
			}
			$("#NodivSubmit").hide();
		    $("#checkListDetails").hide();
		    $('#chekListChargeId').hide();
		    $("#back").show();
		    var errorMsg = ['Bill generation is not up to date for connection no. ' +connectionNo+
                ' Please generate the bills and pay any dues to proceed with no dues certificate '];
		    displayErrors(errorMsg);
		}else{
			$("#consumerName").val(returnData.consumerName);
			$("#consumerAddress").val(returnData.consumerAddress);
			$("#propertyNo").val(returnData.propertyNo);
			if (returnData.propertyNo) {
			    $("#propertyNo").closest('#propNo').show();
			} else {
			    $("#propertyNo").closest('#propNo').hide();
			}
			$('#copies').show();
			
			$('#consumerDetails').show();
			$("#back").hide();
			//$("#NodivSubmit").show();
			$('#chekListChargeId').show();
			$("#consumerNo").prop("readonly", true);
			$("#back").hide();
			
		}
		
		if ($('#free').val() == 'F') {
		$("#payment").hide();
	    } else {
		$("#payment").show();
	    }
	    
	    if ($('#povertyLineId').val() == 'Y') {
			$("#bpldiv").show();
		} else {
			$("#bpldiv").hide();
		}
   	}else{
    displayErrors(errorList);
    }
    
}
    
    

function resetDueWaterForm() {
    $('input[type=text]').val('');
}

function searchConnection(element) {
    var errorList = [];
    var errorList = validateApplicantInfo(errorList);
    var connectionNo = $("#consumerNo").val();

    if (connectionNo == '' || connectionNo == undefined) {
	errorList
		.push(getLocalMessage('water.meterCutOff.validationMSG.connectionNo'));
    }
    if (errorList.length == 0) {
	var theForm = '#noDuesCertificate';
	var requestData = __serializeForm(theForm);
	var URL = 'NoDuesCertificateController.html?' + 'getConnectionDetail';

	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);

	if (returnData == "" || returnData == null
		|| returnData.errorMsg == 'exception') {
	    var URL = $("form").attr('action');
	    _openPopUpForm(URL, "showErrorPage");
	}
	if (returnData.error) {

	    if(returnData.errorMsg != null && returnData.errorMsg != ''){
	        errorList.push(returnData.errorMsg);
	    } else {
	        errorList.push(getLocalMessage('water.nodues.no.record.found')
	                + " " + connectionNo);
	    }

	    $("#consumerNo").val('');
	    $("#connectionSize").val(0);
	    displayErrors(errorList);

	} else {
	    
	    if (returnData.billGenerated) {
		errorList.push(getLocalMessage('water.nodues.bill.generate ')
			+ " " + connectionNo + " "
			+ getLocalMessage('water.nodues.eligible'));
		displayErrors(errorList);
	    } else if (returnData.dues) {
		errorList.push(getLocalMessage('water.nodues.exist') + " "
			+ connectionNo+ " "
			+ getLocalMessage('water.nodues.eligible'));
		$("#consumerNo").val('');
		$("#connectionSize").val(0);
		displayErrors(errorList);
		$("#back").show();
	    } else {
		$("#consumerName").val(returnData.consumerName);
		$("#consumerAddress").val(returnData.consumerAddress);
		$("#waterDues").val(returnData.waterDues);
		$("#duesAmount").val(returnData.duesAmount);
		$('#consumerDetails').show();
		$("#duelist").show();
		$('#copies').show();
		$('#chekListChargeId').show();
		$("#consumerNo").prop("readonly", true);
		$("#back").hide();
	    }

	}

    } else {
	displayErrors(errorList);
    }
}

function resetConnDetails() {
    
    $('#consumerDetails').hide();
    $('#generateOtp').hide();
    $("#consumerNo").prop("readonly", false);
    $("#consumerNo").val('');
    $("#noOfCopies").val('');
    $('#copies').hide();
    $('#chekListChargeId').hide();
	$('#ccnDuesListId').hide();

}
function displayErrors(errorList) {
    var errMsg = '<button type="button" class="close" onclick="closeErrBox()" aria-label="Close"><span aria-hidden="true">&times;</span></button>';

    errMsg += '<ul>';

    $.each(errorList, function(index) {
	errMsg += '<li>' + errorList[index] + '</li>';
    });

    errMsg += '</ul>';
    $('.msg-error').html(errMsg);
    $(".msg-error").show();
    $("#divSubmit").show();
    return false;
}

function closeErrBox() {
    $('.msg-error').hide();

}

/*
 * function generateOTP(element) {
 * 
 * var theForm = '#noDuesCertificate'; var requestData =
 * __serializeForm(theForm); var URL = 'NoDuesCertificateController.html?' +
 * 'generateOtp'; var returnData = __doAjaxRequest(URL, 'POST', requestData,
 * false); }
 */

function validateApplicantInfo(errorList) {
    var applicantTitle = $.trim($('#applicantTitle').val());
    var firstName = $.trim($('#firstName').val());
    var lastName = $.trim($('#lastName').val());
    var mobileNo = $.trim($('#mobileNo').val());
    var areaName = $.trim($('#areaName').val());
    var pinCode = $.trim($('#pinCode').val());
    var povertyLineId = $.trim($('#povertyLineId').val());

    if (applicantTitle == "" || applicantTitle == '0'
	    || applicantTitle == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantNameTitle'));
    }
    if (firstName == "" || firstName == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantFirstName'));
    }
    if (lastName == "" || lastName == undefined) {
	errorList.push(getLocalMessage('water.validation.ApplicantLastName'));
    }
    if (mobileNo == "" || mobileNo == undefined) {
	errorList.push(getLocalMessage('water.validation.applicantMobileNo'));
    }
    if (areaName == "" || areaName == undefined) {
	errorList.push(getLocalMessage('water.validation.applicantarea'));
    }

    if (pinCode == "" || pinCode == undefined) {
	errorList.push(getLocalMessage('water.validation.applicantPinCode'));
    }

    if (povertyLineId == "" || applicantTitle == '0'
	    || povertyLineId == undefined) {
	errorList.push(getLocalMessage('water.validation.isabovepovertyline'));
    }
    return errorList;
}
function printContent(el) {
    
    var restorepage = document.body.innerHTML;
    var printcontent = document.getElementById(el).innerHTML;
    document.body.innerHTML = printcontent;
    window.print();
    document.body.innerHTML = restorepage;
}

function generateReport(element,url,methodName) {
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = {};
	requestData = __serializeForm(theForm);

	var ajaxResponse = __doAjaxRequest(url + '?save', 'POST', requestData,
			false, '', element);
	if ($.isPlainObject(ajaxResponse)) {
		displayMessageOnSubmit(ajaxResponse.command.message, url, methodName);
	}else if (typeof(ajaxResponse) === "string") {
		$('.content-page').html(ajaxResponse);	
	}
}

function displayMessageOnSubmit(message ,url, methodName) {
	window.open(url + '?' + methodName,'_blank');
	var errMsgDiv = '.msg-dialog-box';
	var cls = getLocalMessage("unit.proceed");

	var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message
			+ '</h5>';
	d += '<div class=\'text-center\'><input type=\'button\' class= "btn btn-success" value=\''
			+ cls + '\'  id=\'btnNo\' onclick="generateCertificate(\''+url+ '\')"/></div></br>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(d);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showPopUpMsg(errMsgDiv);
}
function generateCertificate(uri){
	var requestData = {};
	var url = uri + "?noDueCertificatePrint";
	var ajaxResponse = __doAjaxRequest(url, 'POST', requestData,false);	
	var divName = formDivName;
	$('.content').removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	$.fancybox.close();
}
function showPopUpMsg(childDialog) {
	$.fancybox({
		type : 'inline',
		href : childDialog,
		openEffect : 'elastic',
		closeBtn : false,
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