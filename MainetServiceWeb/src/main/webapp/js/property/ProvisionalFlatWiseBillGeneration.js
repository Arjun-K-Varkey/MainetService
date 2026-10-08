$(document).ready(function(){
	
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	
});

function redirectToSearchPropNo(obj) {
	
		showloader(true);
		setTimeout(function () {
			debugger;
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
				    	saveButton(obj);
				    } 
			}else{
				displayErrorsOnPage(errorList);
			}
			showloader(false);
		},2000);
	}

function saveButton(obj) {
	debugger;
	showloader(true);
	setTimeout(function () {
		debugger;
		var errorList = [];
		var billMethod = $("#billingMethod").val();
		if(billMethod == 'I'){
			var flatNo = $("#flatNo").val();
			if (flatNo == "" || flatNo == undefined || flatNo == null) {
				errorList.push(getLocalMessage('prop.select.flatNo'));
			}
		}
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			 var propNo=$("#assNo").val();
			 var oldPropNo=$("#assOldpropno").val();
			 var flatNo=$("#flatNo").val();
			    var requestData = {
				"propNo" : propNo,
				"oldPropNo":oldPropNo,
				"flatNo":flatNo
			    };
			var URL = 'SelfAssessmentForm.html?saveProvisionalBills';
			var ajaxResponse = __doAjaxRequest(URL, 'POST', requestData, false, 'json');
			
			if (ajaxResponse.successFlag == 'Y') {
				showBoxForApproval(getLocalMessage("Bill inserted succesfully"));
				    
			}else{
				showBoxForApproval(getLocalMessage("exception occured while inserting bill"));
				    
			}
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }


function showBoxForApproval(succesMessage) {

    var childDivName = '.msg-dialog-box';
    var message = '';
    var Proceed = getLocalMessage("proceed");
    var no = 'No';
    message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
	    + '</p>';

    var payableFlag = $("#payableFlag").val();
  
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