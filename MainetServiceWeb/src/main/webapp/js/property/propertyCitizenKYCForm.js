function approvePropertyKYCApplication(element){
	
	
	
	var requestData = {};
	var theForm	=	'#PropertyCitizenKYCApproval';
	requestData = __serializeForm(theForm);
	var URL = 'PropertyCitizenKYCApproval.html?approvePropertyKYCApplication';
	var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
	if(returnData)
	{
		var divName = '.content';
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