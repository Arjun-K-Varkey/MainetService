function openPropertyDetailss() {
	showloader(true);
	setTimeout(function () {
	var errorList = [];
	var propNo = $("#assNo").val();

	if (propNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);

	} else {
		var data = {
			"propNo" : propNo
		};
		var URL = 'BillingMethodChange.html?getPropertyDetails';
		var returnData = __doAjaxRequest(URL, 'POST', data, false, 'html');
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}
	showloader(false);
	},2000);
}

function saveBillingMethodForm(element) {
	var errorList = [];
	errorList = validateUnitDetailTable(errorList);
	var billMethodCodePrev = $("#billMethodCode").val();
	var draftApplication = $("#draftApplication").val();
	var billMethodCurrent = $('#billMethod option:selected').attr('code');
	if(draftApplication != 'Y'){
		if (billMethodCodePrev == billMethodCurrent) {
			errorList.push(getLocalMessage("property.billMethodUpdate"));
		}
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		return saveOrUpdateForm(element, "", 'AdminHome.html', 'saveform');
	}
}

function backToSearchPage(element) {
	var requestData = {};
	var URL = 'BillingMethodChange.html?backToSearch';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).html(returnData);
}

function backToAferSearchPage(element) {
	var requestData = {};
	var URL = 'BillingMethodChange.html?backToAferSearchPage';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).html(returnData);
}

function saveForms(element) {
	showloader(true);
	setTimeout(function() {
	var theForm = '#BillingMethodChange';
	var appId = $("input[name='lableValueDTO.applicationId']").val();
	var labelId = $("input[name='lableValueDTO.lableId']").val();

	var serviceId = $("#serviceId").val();
	var requestData = __serializeForm(theForm);
	var URL = 'BillingMethodAuthorization.html?saveDataInProviosionals';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'json');
	if (returnData) {
		backToAferSearchPage(element);
		/*loadScrutinyLabel('ScrutinyLabelView.html?setViewDataFromModule',
				appId, labelId, serviceId);*/
	} else {
		var errorList = [];
		errorList.push(getLocalMessage("property.saveWorkflowError"));
		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
		}
	}

	showloader(false);
	}, 2000);
}

function addArrearDetailsForFlat(flatCount, element) {
	$("#flatNoOfRow").val(flatCount);
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'BillingMethodAuthorization.html?addArrears';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
	if (returnData) {
		var divName = '.content';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}
}

function backToFirstPage(obj) {
	var requestData = {};
	var URL = 'BillingMethodAuthorization.html?backToEditForm';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}

function confirmToNext(obj) {
	var requestData = {};
	var errorList = [];
	errorList = validateTaxEntryAdjustment(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var theForm = '#BillingArrearsAuthorization';
		requestData = __serializeForm(theForm);
		var URL = 'BillingMethodAuthorization.html?saveAdjustedArrears';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}
}

function displayMessageOnSubmit(element) {

	var Yes = getLocalMessage("unitdetails.Yes");
	var No = getLocalMessage("Unitdetails.No");
	var message = "";
	var errMsgDiv = '.msg-dialog-box';
	var cls = 'Proceed';
	$('#billSaveDraftFlag').val('N');
	message = getLocalMessage("property.arrearsChangesConfirmation");
	var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message
			+ '</h5>';
	d += '<div class=\'text-center\'><input type=\'button\' name="Yes" class= "btn btn-success" value=\''
			+ Yes
			+ '\'  id=\'btnNo\' onclick="confirmToProceed()"/>  <input type=\'button\' name="No" class= "btn btn-success" value=\''
			+ No + '\'  id=\'btnNo\' onclick="closeConfirmBoxForm()"/></div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(d);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutClose(errMsgDiv);
}

function displayMessageOnSubmitForDraft(element) {

	var Yes = getLocalMessage("unitdetails.Yes");
	var No = getLocalMessage("Unitdetails.No");
	var message = "";
	var errMsgDiv = '.msg-dialog-box';
	var cls = 'Proceed';
	$('#billSaveDraftFlag').val('Y');
	message = getLocalMessage("property.arrearsChangesConfirmation");
	var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message
			+ '</h5>';
	d += '<div class=\'text-center\'><input type=\'button\' name="Yes" class= "btn btn-success" value=\''
			+ Yes
			+ '\'  id=\'btnNo\' onclick="confirmToProceed()"/>  <input type=\'button\' name="No" class= "btn btn-success" value=\''
			+ No + '\'  id=\'btnNo\' onclick="closeConfirmBoxForm()"/></div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(d);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutClose(errMsgDiv);
}

function confirmToProceed(element) {

	var theForm = '#BillingMethodChangeAuth';
	var requestData = __serializeForm(theForm);
	var URL = 'BillingMethodAuthorization.html?confirmToProceed';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$.fancybox.close();
}

function validateTaxEntryAdjustment(errorList) {

	$('.firstUnitRow')
			.each(
					function(i) {

						var bdBalAmtToTransfer = $("#bdBalAmtToTransfer" + i)
								.val();
						var arrear = $("#arrear" + i).val();
						if (arrear != '') {
							arrear = parseFloat(arrear);
							if (arrear > bdBalAmtToTransfer) {
								errorList
										.push(getLocalMessage("property.adjustedArrearsCheck")
												+ " " + (i + 1));
							}
						}

					});
	return errorList;
}


function saveWorkOrder(element) {
	return saveOrUpdateForm(element, "Record saved successfully!",
			'AdminHome.html', 'saveWorkOrder');
}

//validation of unit details table
function validateUnitDetailTable(errorList){
	
	var rowCount = $('#unitDetailTable tr').length;
	var duplicateFlat = {};
	var duplicateFlatNo = false;
	if(rowCount==2){
		reorderFirstRowBMC();
	}
	$('.firstUnitRow').each(function(i) {

		if(rowCount<=2){			
				var flatNo=  $("#flatNo0").val();	
				var floorNo=  $("#assdFloorNo").val();
				var occupierName = $("#newOccupierName0").val();
	            var occupierMobNo = $("#occupierMobNo0").val();	
				var occupieremail = $("#occupierEmail0").val();
				var level = 1;
			}else{
				var flatNo=  $("#flatNo"+i).val();
				var floorNo=  $("#assdFloorNo"+i).val();
				var occupierName = $("#newOccupierName"+i).val();
				var occupierMobNo = $("#occupierMobNo"+i).val();
				var occupieremail = $("#occupierEmail"+i).val();	
				var level = i+1;
			}
		
		 	if(duplicateFlat[flatNo]){
				duplicateFlatNo = true;
				return false;
			}
			duplicateFlat[flatNo] = true; 
						
			if (flatNo == '' || flatNo == undefined) {
				errorList.push(getLocalMessage('unitdetails.enter.flatNoValid')+ " " + level);
		 	}
			/*if(occupierName=='' || occupierName == undefined){
				  errorList.push(getLocalMessage('unitdetails.new.selectOccupierName')+ " " +level); 
			}*/ 
		 	/*if(floorNo=='0' || floorNo == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectFloorNo')+ " " +level); 
		 	}*/
		 	var filter = /[1-9]{1}[0-9]{9}/;
		 	if (occupierMobNo == undefined) {

			} else if (occupierMobNo != '' && occupierMobNo != '0') {
				if (!filter.test(occupierMobNo)) {
					errorList.push(getLocalMessage('property.valid.occupiermobile')+" " +level);
				}
			}
		 	if (occupieremail != ""  && occupieremail != undefined) {
			  var emailRegex = new RegExp(/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
			  var valid = emailRegex.test(occupieremail);
			  if (!valid) {
				errorList.push(getLocalMessage("property.valid.occupieremail")+" " +level);
			 }
			}
	});
	if(duplicateFlatNo){
			errorList.push(getLocalMessage("property.bif.flat.duplicate.valid"));
		}
	return errorList;
}

function reorderFirstRowBMC(){
	$(".firstUnitRow").find("select:eq(0)").attr("id", "assdFloorNo");
}

function saveBillingMethodFormForDrat(element) {
	showloader(true);
	setTimeout(function() {
	var errorList = [];
	errorList = validateUnitDetailTable(errorList);
	var billMethodCodePrev = $("#billMethodCode").val();
	var draftApplication = $("#draftApplication").val();
	
	var billMethodCurrent = $('#billMethod option:selected').attr('code');
	if(draftApplication != 'Y'){
		if (billMethodCodePrev == billMethodCurrent) {
			errorList.push(getLocalMessage("property.billMethodUpdate"));
		}
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		$('#draftFlag').val('Y');
		return saveOrUpdateForm(element, "", 'AdminHome.html', 'saveform');
	}
	showloader(false);
	}, 2000);
}

function blankReqToMaintainSession() {
	var requestData = {};
	var URL = 'BillingMethodChange.html?blankReqToMaintainSession';
	__doAjaxRequest(URL, 'POST', requestData, false);
	
}

function clearFutureTaxOfAllBills(taxId,id,bmYear,billMasId, element) {
	var theForm = '#BillingMethodChangeAuth';
	var amount = $("#arrear"+id).val();
	if(amount == "" || amount == " "){
		amount = 0.00;
	}
	var requestData = {
			"taxId" : taxId,
			"amount" : amount,
			"bmYear" : bmYear
		    };

		    var returnData = doAjaxLoading(
			    'BillingMethodAuthorization.html?clearFutureTaxOfAllBills',
			    requestData, 'html');
		    
		    $("#arrear"+id).html(amount);
		    $("#totalAdjAmount"+billMasId).html(returnData);
	/*if (returnData) {
		var divName = '.content';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}*/
}

function addAdjustedAmount(bmYear,id, billMasId,taxId,flatNo,element) {
	var theForm = '#BillingMethodChangeAuth';
	var amount = $("#arrear"+id).val();
	if(amount == "" || amount == " "){
		amount = 0.00;
	}
	var requestData = {
			"bmYear" : bmYear,
			"amount" : amount,
			"taxId" : taxId,
			"flatNo" : flatNo
		    };

	var returnData = doAjaxLoading(
		    'BillingMethodAuthorization.html?addAdjustedAmount',
		    requestData, 'html');
	$("#totalAdjAmount"+billMasId).html(returnData);
}


function addArrears(element) {

	var errorList = [];
	errorList = validateUnitDetailTable(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);

	} else {
		var data = __serializeForm(theForm);
		var URL = 'BillingMethodChange.html?addArrears';
		var returnData = __doAjaxRequest(URL, 'POST', data, false, 'html');
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}
}


function addArrearDetails(element) {
	
	var errorList = [];
	var billMethodCodePrev = $("#billMethodCode").val();
	var draftApplication = $("#draftApplication").val();
	var billMethodCurrent = $('#billMethod option:selected').attr('code');
	if(draftApplication != 'Y'){
		if (billMethodCodePrev == billMethodCurrent) {
			errorList.push(getLocalMessage("property.billMethodUpdate"));
		}
	}
	
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var theForm = '#BillingMethodChange';
		var billMethodCurrent = $('#billMethod option:selected').attr('code');
		
		var formName = findClosestElementId(element, 'form');
		var theForm = '#' + formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
		
		var URL = 'BillingMethodChange.html?addArrears';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
		if (returnData) {
			var divName = '.content-page';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
		}
	}
	
}

function generateFlatWiseBill(element) {
	showloader(true);
	setTimeout(function() {

		var requestData = {};
		var URL = 'BillingMethodChange.html?generateFlatWiseBill';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		var divName = '.content-page';
		$(divName).html(returnData);
		if ($("#validationerror_errorslist li").length > 0) {
			return false;
		}
		var Ok = getLocalMessage("bt.ok");
		var message = "";
		var errMsgDiv = '.msg-dialog-box';
		//message = getLocalMessage("bill.method.auto.split.arrear.done");
		message = getLocalMessage("bill.method.auto.adjust.arrears");
		var d = '<h5 class=\'text-blue-2 text-center padding-15\'>' + message	+ '</h5>';
		d += '<div class=\'text-center\'><input type=\'button\' name="No" class= "btn btn-success" value=\''
			+ Ok + '\'  id=\'btnNo\' onclick="goToDashBoard()"/></div>';

		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(d);
		$(errMsgDiv).show();
		$('#btnNo').focus();
		showModalBoxWithoutClose(errMsgDiv);

		showloader(false);
	}, 2000);
}

function viewFlatWiseArrears(flatCount, element) {
	showloader(true);
	setTimeout(function() {
	$("#flatNoOfRow").val(flatCount);
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'BillingMethodAuthorization.html?viewFlatWiseArrears';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
	if (returnData) {
		var divName = '.content';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	}
	showloader(false);
	}, 2000);
}