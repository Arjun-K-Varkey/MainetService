$(document).ready(function(){
	
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	
	// Select the billing method dropdown
	var $billMethod = $('#billMethod');

	// Check if the billing method dropdown exists
	if ($billMethod.length > 0) {
		// Log the selected index for debugging
		console.log('Selected Index:', $billMethod[0].selectedIndex);

		// Check if any option is selected
		if ($billMethod[0].selectedIndex >= 0) {
			// Trigger the change event to handle visibility
			$billMethod.trigger('change');
		} else {
			console.log('No option is currently selected.');
			// Handle the case where no option is selected, if needed
		}
	} else {
		console.error('Billing method dropdown not found.');
	}
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
					    'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?getBillingMethod',
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
					"flatNo" : flatNo
				    };
			var URL = 'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?searchForm';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			$(formDivName).html(returnData);
			
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			reorderfactor('.specFact','.eachFact');
			
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


function savePropertyEditForm(element) {
	var errorList = [];
	$.fancybox.close();
	var divName = '.content-page';

	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"NewRegistrationFormDraftApplicationWorkFlowInitiate.html?saveDraftForm", 'POST',
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
		showBoxForApproval(getLocalMessage("Property Edited Successfully"));
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
}

function handleBillingMethodChange(element) {

	try {

		// Ensure 'element' is a valid DOM element
		if (!element || !element.options || !element.options[element.selectedIndex]) {
			console.error('Invalid element or missing options property:', element);
			return;
		}
		var selectedBillingMethodCode = element.options[element.selectedIndex].getAttribute('code');

		// Store the selected billing method code in local storage
		localStorage.setItem('selectedBillingMethodCode', selectedBillingMethodCode);

		// Reset visibility or inclusion based on the selected billing method
		if (selectedBillingMethodCode === 'W') {
			$('#utilitySection').show();
			$('.electricDetailsColumn').hide();
		} else if (selectedBillingMethodCode === 'I') {
			$('#utilitySection').hide();
			$('.electricDetailsColumn').show();
		} else {
			// Handle other cases if needed
			$('#utilitySection').hide();
			$('.electricDetailsColumn').hide();
		}
	}
	catch (error) {
		console.error('Error in handleBillingMethodChange:', error);
	}
}

function getFlatValidation(count) {

	var errorList = [];
	var flatNo = $("#flatNo" + count).val();
	var data = {
		"flatNo": flatNo
	};
	var URL = 'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?getFlatValidation';
	var flag = __doAjaxRequest(URL, 'POST', data, false);
	if ('Y' == flag) {
		errorList.push(getLocalMessage("property.bif.flat.duplicate.valid")+" : "+flatNo);
		displayErrorsOnPage(errorList);
	}
}
