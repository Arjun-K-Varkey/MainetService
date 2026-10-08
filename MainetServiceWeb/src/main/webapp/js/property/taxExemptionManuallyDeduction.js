
$(document).ready(function(){
	
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	
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
					    'ChangeInAssessmentForm.html?getBillingMethod',
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
				    	SearchButtonManually(obj);
				    } 
			}else{
				displayErrorsOnPage(errorList);
			}
			showloader(false);
		},2000);
	}



function savePropertyEditForm(element) {
	var errorList = [];
	$.fancybox.close();
	var divName = '.content-page';

	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"TaxExemptionManuallyDeduction.html?saveEditForm", 'POST',
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
		showBoxForApproval(getLocalMessage("Tax exempted successfully"));
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

}

function showAddInfoEdit(factorCode,factRadioVal){
	
	if($('input[name="provisionalAssesmentMstDto.proAssfactor['+factRadioVal+']"]:checked').val()=="Y"){
		
		var data = {"factorCode" : factorCode};		
		var URL = 'PropertyEditableForm.html?getFactorValueDiv';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html(returnData);
		
		$("#"+factorCode).show();
		reorderfactor('.specFact','.eachFact');
		var a=0;
		var tableLength = $('#unitDetailTable tr').length;
		for(var j=1;j<tableLength;j++) // table tr length 
		{
			var unitNoFact=$("#unitNo"+a).val();
			var unitSelectId=".factQuestion"+factRadioVal;
			if($(""+unitSelectId+" option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined){
				$(""+unitSelectId+"").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
			}	
			a++;
		}
	}else{	 
		var data = {"factorCode" : factorCode};
		var URL = 'SelfAssessmentForm.html?deleteFactorStatus';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html('');
		reorderfactor('.specFact','.eachFact');
	}
}

function addEntryData(tableId) {
    
	$("#errorDiv").hide();
	addTableRow(tableId, false);

}

function SearchButtonManually(obj) {
		debugger;
		var errorList = [];
		var billMethod = $("#billingMethod").val();
		var propNo=$("#assNo").val();
		var finId=$("#finId").val();
		var flatNo = $("#flatNo").val();
		if(billMethod == 'I'){
			
			if (flatNo == "" || flatNo == undefined || flatNo == null) {
				errorList.push(getLocalMessage('prop.select.flatNo'));
			}
		}
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			var requestData = {
					"propNo" : propNo,
					"flatNo" :flatNo,
					"finId" :finId
				    };
			var URL = 'TaxExemptionManuallyDeduction.html?searchForm';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
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
	
 }
function closeApproval() {
    window.location.href = 'AdminHome.html';
    $.fancybox.close();
}