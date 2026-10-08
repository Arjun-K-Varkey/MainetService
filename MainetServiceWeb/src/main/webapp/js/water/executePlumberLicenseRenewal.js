 function updatedPlumberLicenseRenewalExecutionDetailByDept(element){
	return saveOrUpdateForm(element,"Plumber License Execute Successfully! ", 'ExecutePlumberLicenseRenewal.html?printPlumberLicenseRenewal', 'saveform');
 }
 
 function showSaveResultBox(returnData, message,redirectUrl) {
 	var messageText ='Do you want to Print Plumber License Renewal Report';
	var message='';
	var cls = getLocalMessage('eip.page.process');
	var msgText1 = getLocalMessage("Yes");
	var msgText2 = getLocalMessage("No");
	if(redirectUrl=="" || redirectUrl==null){
		message	+='<p class=\"text-blue-2 text-center padding-10\">'+ messageText+'</p>';
		message	+='<div class=\'class="text-center padding-bottom-10"\'>'+'</div>';
			
	}else{
	    message	+='<p class="text-blue-2 text-center padding-15">'+ messageText +'</p>';
		 message	+='<div class=\'text-center padding-bottom-10\'>'+	
		'<br/><input type=\'button\' value=\''+msgText1+'\'  id=\'btnNo\' class=\' btn btn-success \'    '+ 
		' onclick="printPlumberLicenseRenewal(\''+redirectUrl+'\')"/>&nbsp;&nbsp;&nbsp<input type=\'button\' value=\''+msgText2+'\'  id=\'btnNo1\' class=\' btn btn-danger \'    '+ 
		' onclick="closePlumberLicenseRenewalPrintBox()"/>'+	
		'</div>&nbsp;&nbsp;&nbsp';
	}
	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
    if(redirectUrl=="" || redirectUrl==null){
	      showModalBox(errMsgDiv);
	 }else{
		  showModalBoxWithoutClose(errMsgDiv);
	 }
	return false;
 }
 
 function printPlumberLicenseRenewal(element){
	 var  URL =element;
	 var data = {};
	 var returnData=__doAjaxRequest(URL,'post',data,false,'html');
	 $('#plumRenewalExecuteDiv').html(returnData);
	 $.fancybox.close();
 }
 
 function closePlumberLicenseRenewalPrintBox(){
		$.fancybox.close();
 }

 function checkedExecutionBox(){
	$("#executePlmLicId").prop("checked", true)
 }

 $('#executePlmLicId').change(function() {
	$("#executionDate").val("");
 });