$(document).ready(function() {
	$("#showFlatNo").hide();
});
function searchPropetryForDelete(){
	var errorList = [];
	var propNo=$("#propNo").val();
	var oldPropNo=$("#oldPropNo").val();
	var finId=$("#finId").val();
	var flatNo = $("#flatNo").val();
	if((propNo !=undefined && propNo!="") || (oldPropNo !=undefined && oldPropNo!="")){
		var data = {"propNo" : propNo,"oldPropNo":oldPropNo,"finId":finId,"flatNo":flatNo};
		var url = 'DataEntrySuiteDeleteBill.html?searchPropetryForDelete';
		var returnData = __doAjaxRequest(url, 'POST', data, false);
		var divName = '.content';
		$(divName).html(returnData);
		 $(formDivName).html(returnData);
	}else{
		errorList
				.push(getLocalMessage("property.changeInAss"));
		displayErrorsOnPage(errorList);
		return false;
	}
}

function approveBillDeletionApplication(element){
			var requestData = {};
			var theForm	=	'#DataEntryOutStandingView';
			requestData = __serializeForm(theForm);
			var URL = 'PropertyBillDeletionAuthorization.html?approveApplication';
			var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
			if(returnData)
			{
				var divName = '.AuthContent';
		/*			$(divName).removeClass('ajaxloader');
		*/			$(divName).html(returnData);
			}
		
}


function doFormActionForSaveProperty(obj,successMessage, URL, sendFormData, successUrl)
{
	
	
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	
	             var requestData = {};
	
				if (sendFormData) {
					
					requestData = __serializeForm(theForm);
				}
			
				var url	=URL;
				
				
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
						   		
						   		showSaveResultBox(returnData, message, 'AdminHome.html');
						   	
						   	else
						   		
						   		showSaveResultBox(returnData, message, successUrl);
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
				
				return false;
				
	
}


function deletePropertyBill(element){
	var errorList = [];
	var remarks = $("#remarks").val();
	if(remarks == '' || remarks == null || remarks == undefined){
		errorList.push(getLocalMessage("property.enter.remark"));
		displayErrorsOnPage(errorList);
		return false;
	}
	var theForm	=	'#DataEntryOutStandingView';
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'DataEntrySuiteDeleteBill.html?deleteBill'; 
	
	saveOrUpdateForm(element,
			"Your application for contract Agreement saved successfully!",
			'DataEntrySuiteDeleteBill.html', 'deleteBill');

}

function backToDeleteSearchPage(){
	 var data={};
		var URL='DataEntrySuiteDeleteBill.html?backToDeleteSearchPage';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		$(formDivName).html(returnData);
}

function getFlatNo() {

	var errorList = [];
	var propNo = $("#propNo").val();
	var oldPropNo = $("#oldPropNo").val();
	if (propNo == "" && oldPropNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#propNo").val();
		var requestData = {
			"propNo" : propNo
		};

		var ajaxResponse = doAjaxLoading(
				'DataEntrySuiteDeleteBill.html?getBillingMethod', requestData,
				'html');

		if (ajaxResponse != null && ajaxResponse != "") {
			var prePopulate = JSON.parse(ajaxResponse);

			$.each(prePopulate, function(index, value) {
				$('#flatNo').append(
						$("<option></option>").attr("value", value).text(
								(value)));
			});
			$('#flatNo').trigger("chosen:updated");
			$("#showFlatNo").show();
			// $("#serchBtn").hide();
			// $("#serchBtnDirect").show();
			$("#billingMethod").val('I');
		}
	} else {
		showErrorOnPage(errorList);
	}

}