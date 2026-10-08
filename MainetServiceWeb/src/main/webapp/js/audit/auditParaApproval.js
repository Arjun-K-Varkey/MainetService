var fileArray=[];
$(document).ready(function() {

	$('#frwdToDepFormGrp').hide();
	prepareDateTag();
});


function loadForwardData(obj){
	
	
	var auditDeptId=$('#audDepId').val();
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	var url	=	$(theForm).attr('action')+'?' + 'getAuditWorkflowAction';
	if($('input[name="workflowActionDto.decision"]:checked').val()=="FORWARD_TO_EMPLOYEE")
	{
		
		$('#frwdToDepFormGrp').show();
	}
	else{
		$('#serverEvent').val('');
		$('#empId').val('');
		$('#eventEmp').val('');
		$("#forward").hide();
		$("#sendBack").hide();
		$('#frwdToDepFormGrp').hide();
		}
	
}


function fetchEmpFromDepartment(obj)
{
	//alert("Here");
	
	var formName = findClosestElementId(obj, 'form');
	var theForm = '#' + formName; 
	var url	=	$(theForm).attr('action')+'?' + 'getAuditWorkflowAction';
	var deptId = $('#forwardToDept').val();
	var data = {
			"decision" : "FORWARD_TO_EMPLOYEE",
			"serEventId":null,
			"deptId":deptId
	};	
	var returnData = __doAjaxRequest(url, 'POST', data, false);
	$('#empId').html('');
	$.each( returnData, function( key, value ) {
		$('#empId').append('<option value="' + key + '">' + value  + '</option>');
	});

}


function showConfirmBoxForApproval(approvalData) {
	
	element = approvalData;
	var errMsgDiv = '.msg-dialog-box';
	var message = '';
		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(message);
		$(errMsgDiv).show();
		$('#btnNo').focus();
		saveAuditParaApprovalData();
}


function saveAuditParaApprovalData() {

	var errorList = [];	
	var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;	
	var minDates=$('#auditEntryDate').val();
	var max=new Date();
	var auditDate=$('#auditDate').val();
	var newAuditDate=$('#newAuditDate').val();
	var min = new Date(minDates.replace(pattern, '$3-$2-$1'));
	var current = new Date(auditDate.replace(pattern, '$3-$2-$1'));
	var newAuditDates = new Date(newAuditDate.replace(pattern, '$3-$2-$1'));
	if(auditDate==null || auditDate==""){
		errorList.push(getLocalMessage('audit.selectAuditDate'));
	}
	if(newAuditDate!=null && newAuditDate!=""){
		if(current<newAuditDates){
			errorList.push(getLocalMessage('audit.minDateLatest'));
		}
	}else{
		if(current<min){
			errorList.push(getLocalMessage('audit.minDate'));
		}		
	}
	if(current>max){
		errorList.push(getLocalMessage('audit.maxDate'));
	}	
	if(errorList.length > 0){
		displayErrorsOnPage(errorList);
	}
	
	else{
	$.fancybox.close();
	var removeFileById=$('#removeFileById').val();	
	var divName = '.content-page';
	 var formName = findClosestElementId(element, 'form');
	 var theForm = '#' + formName; 
	 var requestData = __serializeForm(theForm);
	 var object = __doAjaxRequest($(theForm).attr('action')+ '?saveAuditParaApproval', 'POST',requestData, false,'json');		
	 
	 if(object.error != null && object.error != 0  ){	
		 $.each(object.error, function(key, value){
			    $.each(value, function(key, value){
			    	if(value != null && value != ''){				    		
			    		errorList.push(value);
			    	}				        
			    });
			});
		 displayErrorsOnPage(errorList);
	 }else{
		if(object.auditWfStatus =='REJECTED'){
			 showBoxForApproval(getLocalMessage('audit.para.rejection'));
		 }else if(object.auditWfStatus =='SEND_BACK'){
			 showBoxForApproval(getLocalMessage('audit.para.sendBack'));
		 }else if(object.auditWfStatus =='FORWARD_TO'){
			 showBoxForApproval(getLocalMessage('audit.para.sendBack'));
		 }else{
			 showBoxForApproval(getLocalMessage('audit.para.creation'));
		 }
	 }
	}
}

function showBoxForApproval(succesMessage){
	
  	var childDivName = '.msg-dialog-box';
	var message = '';
	var Proceed =  'Proceed';
	var no = 'No';	
	message += '<p class="text-blue-2 text-center padding-15">'
		    + succesMessage +'</p>';
	
	message += '<div class=\'text-center padding-bottom-10\'>'
			+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''+Proceed+'\'  id=\'Proceed\' '
			+ ' onclick="closeApproval();"/>' + '</div>';
	$(childDivName).addClass('ok-msg').removeClass('warn-msg');
	$(childDivName).html(message);
	$(childDivName).show();
	$('#Proceed').focus();
	showModalBoxWithoutClose(childDivName);
}

function closeApproval(){
	window.location.href='AdminHome.html';
	$.fancybox.close();
}

$("#attachDocuments").on("click",'#deleteFile',function(e) {

	var errorList = [];
	if (errorList.length > 0) {
		$("#errorDiv").show();
		showErr(errorList);
		return false;
	} else {

		$(this).parent().parent().remove();
		var fileId = $(this).parent().parent().find(
				'input[type=hidden]:first').attr('value');
		if (fileId != '') {
			fileArray.push(fileId);
		}
		$('#removeFileById').val(fileArray);
	}
});
