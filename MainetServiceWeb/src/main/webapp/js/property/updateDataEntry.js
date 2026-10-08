 $(document).ready(function(){
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	 /*var flatNo = $("#flatNoo").val();
	 if(flatNo=="ALL"){
		 $("#unitDetailTable tbody tr").each(function(i) {
			 $("#occupierName"+i).prop("disabled",true);
		 });
	 }*/
});
 
 function redirectToSearchPropNo(obj) {
	
		showloader(true);
		setTimeout(function () {
		    var errorList = [];
		    var propNo=$("#assNo").val();
			var oldPropNo= $("#assOldpropno").val();
			if(propNo== "" && oldPropNo==""){
				errorList.push(getLocalMessage("property.changeInAss"));
			}
			if (errorList.length == 0){
				 var propNo=$("#assNo").val();
				    var requestData = {
					"propNo" : propNo
				    };

				    var ajaxResponse = doAjaxLoading(
					    'UpdateDataEntrySuite.html?getBillingMethod',
					    requestData, 'html');

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
				    }else{
				    	SearchButton(obj);
				    } 
			}else{
				displayErrorsOnPage(errorList);
			}
			showloader(false);
		},2000);
	}

function SearchButton(obj) {
	
	showloader(true);
	setTimeout(function () {
		var errorList = [];
		var billMethod = $("#billingMethod").val();
		if(billMethod == 'I'){
			var flatNo = $("#flatNo").val();
			if (flatNo == "" || flatNo == undefined || flatNo == null
					|| flatNo == '0') {
				errorList.push(getLocalMessage('prop.select.flatNo'));
			}
			if($(".editType:checked").val() == "N" && flatNo == "ALL"){
				errorList.push(getLocalMessage('prop.select.single.flatNo'));
			}
		}
			
		errorList = ValidateDetails(errorList);	
		if (errorList.length == 0)
		{
			var theForm	=	'#UpdateDataEntrySuite';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'UpdateDataEntrySuite.html?getAssessmentDetail';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
			$(formDivName).html(returnData);
			if(billMethod == 'I'){
				 $("#showFlatNo").show();
		    	  $("#serchBtn").show();
		    	  $("#serchBtnDirect").hide();
			}
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }

function ValidateDetails(errorList){
		
		var propNo=$("#assNo").val();
		var oldPropNo= $("#assOldpropno").val();
		if(propNo== "" && oldPropNo==""){
			errorList.push(getLocalMessage("update.enter.propNo.oldPropNo"));
		}
		return errorList;
}


function updateDataEntry(element) {
	var singleOwnerTable = $("#singleOwnerTable").closest('table').attr('id');
	var ownerTable = $("#ownerTable").closest('table').attr('id');
	var jointOwnerTable = $("#jointOwnerTable").val();
	var errorList = [];
	if (singleOwnerTable || ownerTable) {
		errorList = validateForm(errorList);
	} else {
		errorList = validateForm1(errorList);
	}
	errorList = validateUnitDetailTable(errorList);
	if (errorList == 0) {
		var theForm = '#UpdateDataEntrySuite';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var returnData = saveOrUpdateForm(element,
				"Bill Payment done successfully!",
				'UpdateDataEntrySuite.html?PrintReport', 'saveform');
	} else {
		displayErrorsOnPage(errorList);
	}
}

function savePropertyFrom(element){
	showloader(true);
	setTimeout(function () {
				
				var decision = $("input[name='workflowActionDto.decision']:checked").val();
        		var errorList = validateSendBack(decision);
		        if (errorList.length > 0) {
		            showloader(false);
		            displayErrorsOnPage(errorList);
		            return;
		        }
		        
				var theForm	=	'#EditNameAddressAuthorization';
				requestData = __serializeForm(theForm);
				var URL = 'EditNameAddressAuthorization.html?saveAuthorization';
				var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
				if(returnData){
					var divName = '.AuthContent';
					$(divName).html(returnData);
				}
			
			var a ="String";
			showloader(false);
	},1000);
}

function validateSendBack(decision) {
    var errorList = [];

    if (decision == "SEND_BACK") {
        var serverEvent = $("#serverEvent").val();
        var eventEmp = $("#eventEmp").val();

        if (!serverEvent || serverEvent == "0") {
            errorList.push(getLocalMessage("workflow.checkAct.sendBack.event"));
        }

        if (!eventEmp || eventEmp.length == 0) {
            errorList.push(getLocalMessage("workflow.checkAct.sendBack.emp"));
        }
    }

    return errorList;
}

function doFormActionForSaveProperty(obj, successMessage, URL, sendFormData, successUrl) {

	var formName = findClosestElementId(obj, 'form');
	var theForm = '#' + formName;
	var requestData = {};

	if (sendFormData) {
		requestData = __serializeForm(theForm);
	}
	var url = URL;
	var returnData = __doAjaxRequestForSave(url, 'post', requestData, false, '', obj);
	if ($.isPlainObject(returnData)) {
		var message = returnData.command.message;
		var hasError = returnData.command.hasValidationError;
		if (!message) {
			message = successMessage;
		}
		if (message && !hasError) {
			if (returnData.command.hiddenOtherVal == 'SERVERERROR')

				showSaveResultBox(returnData, message, 'AdminHome.html');
			else
				showSaveResultBox(returnData, message, successUrl);
		}
		else if (hasError) {
			$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');
		}
		else
			return returnData;
	}
	else if (typeof (returnData) === "string") {
		$(formDivName).html(returnData);
		prepareTags();
	}
	else {
		alert("Invalid datatype received : " + returnData);
	}
	return false;
}

function validateMobileNumber() {
	var mobileNumber = $("#mobileNo").val();
	var filter = /[1-9]{1}[0-9]{9}/;
	if (mobileNumber != '') {
		if (filter.test(mobileNumber)) {
			$('#mobNumber').hide();
		} else {
			$('#mobNumber').show();
			$('#mobNumber').html('Invalid Mobile Number');
			$('#mobNumber').css('color', 'red');
		}
	} else {
		$('#mobNumber').hide();
	}
}

function popUpMsg(returnData) {
	successUrl = 'UpdateDataEntrySuite.html';
	if ($.isPlainObject(returnData)) {
		var message = returnData.command.message;
		var hasError = returnData.command.hasValidationError;
		if (!message) {
			message = successMessage;
		}
		if (message && !hasError) {
			if (returnData.command.hiddenOtherVal == 'SERVERERROR')
				showSaveResultBox(returnData, message, 'AdminHome.html');
			else
				showSaveResultBox(returnData, message, successUrl);
		} else if (hasError) {
			$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');
		} else
			return returnData;
	} else if (typeof (returnData) === "string") {
		$('.content-page').html(returnData);
		prepareTags();
	} else {
		alert("Invalid datatype received : " + returnData);
	}
	return false;
}

function validateForm1(errorList) {
	var editType = $("#editType").val();
	{
		if('N' == editType){
		$("#jointOwnerTable tbody tr")
				.each(
						function(i) {
							var assoOwnerName = $("#assoOwnerName" + i).val();
							var assoMobileno = $("#assoMobileno" + i).val();
							var assoAddharno = $("#assoAddharno" + i).val();
							var rowCount = i + 1;
							var pattern = /^[a-zA-Z\s-, ]+$/;
							if (assoOwnerName == "" || assoOwnerName == null
									|| assoOwnerName == undefined) {
								if (!pattern.test(assoOwnerName)) {
									errorList
											.push(getLocalMessage("update.ownerName ")
													+ rowCount);
								}
							}
							var pattern = /^[a-zA-Z\s-, ]+$/;
							var filter = /[1-9]{1}[0-9]{9}/;
							if (assoMobileno == undefined) {

							} else if (assoMobileno != '') {
								if (!filter.test(assoMobileno)) {
									errorList
											.push(getLocalMessage("update.invalid.mobileNo ")
													+ rowCount);
								}
							} else {
								errorList
										.push(getLocalMessage("update.mobileNo  ")
												+ rowCount);
							}
							var pattern = /^\d{12}$/;
							if (assoAddharno == undefined) {

							} else if (assoAddharno != "") {
								if (!pattern.test(assoAddharno)) {
									errorList
											.push(getLocalMessage("update.adhaarNo ")
													+ rowCount);
								}
							}
						});
			}
	if('A' == editType){				
		var pincode = $("#assPincode").val();
		var filter = /[0-9]{6}/;
		if (pincode != '') {
			if (!filter.test(pincode)) {
				errorList.push(getLocalMessage("update.invalid.pincode"));
			}
		} 
		/*else {
			errorList.push(getLocalMessage("update.pincode"));
		}*/
		var assAddress = $("#assAddress").val();
		if (assAddress == '' || assAddress == null || assAddress == undefined) {
			errorList.push(getLocalMessage("update.location.address"));
		}
	}	
		var remarks = $("#remarks").val();
		if (remarks == '' || remarks == null || remarks == undefined) {
			errorList.push(getLocalMessage("property.enter.remark"));
		}
		return errorList;

	}
}
function validateForm(errorList) {
	
	var editType = $("#editType").val();
	if('N' == editType){
	
		var adhaarNo = $("#assoAddharno").val();
		var pattern = /^\d{12}$/;
		if (adhaarNo == undefined) {
	
		} else if (adhaarNo != "") {
			if (!pattern.test(adhaarNo)) {
				errorList.push(getLocalMessage("update.adhaarNumber"));
			}
		}
		var mobileNumber = $("#assoMobileno").val();
		var filter = /[1-9]{1}[0-9]{9}/;
		if (mobileNumber != '') {
			if (!filter.test(mobileNumber)) {
				errorList.push(getLocalMessage("update.invalidNumber"));
			}
		}
	
		var singleOwnerTable = $("#singleOwnerTable").closest('table').attr('id');
		var ownerName = $("#assoOwnerName").val();
		var ownershiptype = $("#ownershiptype").val();
	
		// var pattern = /^[a-zA-Z]/;
		var pattern = /^[a-zA-Z\s-, ]+$/;
		if (singleOwnerTable) {
			if (ownerName == "" || ownerName == null || ownerName == undefined) {
				if (!pattern.test(ownerName)) {
					errorList.push(getLocalMessage("update.ownerName.empty"));
				}
			}
		} else {
			if (ownerName == "" || ownerName == null || ownerName == undefined) {
				if (!pattern.test(ownerName)) {
					errorList.push(getLocalMessage("update.nameOf " + ownershiptype
							+ " update.empty"));
				}
			}
		}
		
		var applicantEmailId = $.trim($('#eMail').val());
		if (applicantEmailId != '' && applicantEmailId != null
				&& applicantEmailId != undefined) {
			if (!validateEmail(applicantEmailId)) {
				errorList.push(getLocalMessage('update.invalid.email'));
			}
		}
		
		var applicantEmailId = $.trim($('#occupierEmail').val());
		if (applicantEmailId != '' && applicantEmailId != null
				&& applicantEmailId != undefined) {
			if (!validateEmail(applicantEmailId)) {
				errorList.push(getLocalMessage('update.invalid.occupier.email'));
			}
		}
	}

	if('A' == editType){
		var pincode = $("#assPincode").val();
		var filter = /[0-9]{6}/;
		if (pincode != '') {
			if (!filter.test(pincode)) {
				errorList.push(getLocalMessage("update.invalid.pincode"));
			}
		} 
		/*else {
			errorList.push(getLocalMessage("update.pincode"));
		}*/
		var assAddress = $("#assAddress").val();
		if (assAddress == '' || assAddress == null || assAddress == undefined) {
			errorList.push(getLocalMessage("update.location.address"));
		}
	}
	
	var remarks = $("#remarks").val();
		if (remarks == '' || remarks == null || remarks == undefined) {
			errorList.push(getLocalMessage("property.enter.remark"));
		}
		
	return errorList;
}

function validateEmail(email) {
	var regexPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	return regexPattern.test(email);
}

function emailValidation() {

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

function validateAdharNumber() {
	var adhaarNumber = $("#adharNumber").val();
	var pattern = /^\d{12}$/;
	if (adhaarNumber != '') {
		if (pattern.test(adhaarNumber)) {
			$('#"addharno"').hide();
		} else {
			$('#"addharno"').show();
			$('#"addharno"').html('Invalid Adhaar Number');
			$('#"addharno"').css('color', 'red');
		}
	} else {
		$('#"addharno"').hide();
	}
}

function validatePAN(Obj) {

	$('.error-div').hide();
	var errorList = [];
	if (Obj == null)
		Obj = $('#pannumber').val();
	if (Obj.value != "") {
		ObjVal = Obj.value;
		var panPat = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
		var code = /([C,P,H,F,A,T,B,L,J,G])/;
		var code_chk = ObjVal.substring(3, 4);
		if (ObjVal.search(panPat) == -1) {
			errorList.push('Invaild PAN Number');
			$('#pannumber').val("");
		} else if (code.test(code_chk) == false) {
			errorList.push('Invaild PAN Number');
			$('#pannumber').val("");
		}
	}

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
}

//validation of unit details table
function validateUnitDetailTable(errorList) {

	var a = a;
	var rowCount = $('#unitDetailTable tr').length;
	var duplicateFlat = {};
	var duplicateFlatNo = false;
	/*if(rowCount==2){
		if('CIA' == serviceShortCode){
			reorderFirstRowCIA();			
		}else{
			reorderFirstRow();			
		}
	}*/
	$('.firstUnitRow').each(function(i) {

		if (rowCount <= 2) {
			var occupierName = $("#occupierName0").val();
			var flatNo = $("#flatNo0").val();
			var level = 1;
		} else {
			var occupierName = $("#occupierName" + i).val();
			var flatNo = $("#flatNo" + i).val();
			var level = i + 1;
		}
		if (duplicateFlat[flatNo]) {
			duplicateFlatNo = true;
			return false;
		}
		duplicateFlat[flatNo] = true;

		if (flatNo == '' || flatNo == undefined) {
			errorList.push(getLocalMessage('unitdetails.enter.flatNoValid') + " " + level);
		}
		if (occupierName == '' || occupierName == undefined) {
			errorList.push(getLocalMessage('unitdetails.selectOccupierName') + " " + level);
		}

	});
	if (duplicateFlatNo) {
		errorList.push(getLocalMessage("property.bif.flat.duplicate.valid"));
	}
	return errorList;
}


function editData(){
	if($(".editType:checked").val() == "ON"){
		 $("#ownerName").show();
		 $("#ownerAddress").hide();
	 }else{
		 $("#ownerAddress").show()
		 $("#ownerName").hide();
		 	 
	 }
}