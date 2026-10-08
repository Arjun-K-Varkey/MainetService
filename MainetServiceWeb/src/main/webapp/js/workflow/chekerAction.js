$(document)
.ready(
		function() {
			$('#forward').hide();
			$('#sendBack').hide();
			$('#approvalBTFlow').hide();
			
			if($('input[name="workflowActionDto.decision"]:checked').val()=="FORWARD_TO_EMPLOYEE"){
				$("#forward").show();
			}else if($('input[name="workflowActionDto.decision"]:checked').val()=="SEND_BACK"){
				$("#sendBack").show();
			}
			var selectedServerEvent = $('#selectedServerEvent').val();
			if(selectedServerEvent && selectedServerEvent!= null){
				$('#serverEvent').val(selectedServerEvent);
			}
		});

function loadDataBasedOnDecision(obj){
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	var url	=	$(theForm).attr('action')+'?' + 'getWorkFlowActionDetail';
	if($('input[name="workflowActionDto.decision"]:checked').val()=="FORWARD_TO_EMPLOYEE"){
		loadAllEmp(url);
		if("MutationAuthorization" == formName){
			hideAndShowButton();
		}
	}else if($('input[name="workflowActionDto.decision"]:checked').val()=="SEND_BACK"){
		loadAllEvent(url);
		if("MutationAuthorization" == formName){
			hideAndShowButton();
		}
	}else if($('input[name="workflowActionDto.decision"]:checked').val()=="APPROVED"){
		if("MutationAuthorization" == formName){
			hideAndShowButton();
		}
		$('#approvalBTFlow').hide();
		$('#normalFlow').show();
		$('#serverEvent').val('');
		$('#empId').val('');
		$('#eventEmp').val('');
		$("#forward").hide();
		$("#sendBack").hide();
	}else{
		if("MutationAuthorization" == formName){
			hideAndShowButton();
		}
		$('#serverEvent').val('');
		$('#empId').val('');
		$('#eventEmp').val('');
		$("#forward").hide();
		$("#sendBack").hide();
		$('#approvalBTFlow').show();
		$('#normalFlow').hide();
	}
}

function loadAllEvent(url){
	$("#forward").hide();
	$("#sendBack").show();
	$('#serverEvent').html('');
	$('#eventEmp').html('');
	$('#empId').val('');
	//specific for MRM
	$('#approvalBTFlow').show();
	$('#normalFlow').hide();
	var data = {
			"decision" : "SEND_BACK",
			"serEventId":null
	};		
	var returnData = __doAjaxRequest(url, 'POST', data, false);
	if(returnData!=null){
		$('#serverEvent').append('<option value="0">select</option>');

		$.each( returnData, function( key, value ) {
			
			if($('#showInitiator').val() == 'true'){
				$('#serverEvent').append('<option value="' + key + '">' + value  + '</option>');
			}else{
				if(key !== '0_0')
					$('#serverEvent').append('<option value="' + key + '">' + value  + '</option>');
				
			}
			
		});
		if(returnData['0_0']=="Initiator" && Object.keys(returnData).length==1){
		 $("#sendBack").hide();
	 }
	}
}

function loadAllEmp(url){
	$("#forward").show();
	$("#sendBack").hide();
	$('#serverEvent').val('');
	$('#eventEmp').val('');
	$('#empId').html('');
	var data = {"decision" : "FORWARD_TO_EMPLOYEE",
			"serEventId":null};		
	$('#empId').html('');
	var returnData = __doAjaxRequest(url, 'POST', data, false);
	$.each( returnData, function( key, value ) {
		$('#empId').append('<option value="' + key + '">' + value  + '</option>');
	});
}

function loadAllEmpBasedOnEvent(obj){
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	var url	=	$(theForm).attr('action')+'?' + 'getWorkFlowActionDetail';
	/*	var url	='WorkFlowType.html?getWorkFlowActionDetail';
	 */	var servEventId=$("#serverEvent").val();
	 $('#eventEmp').html('');
	 var data = {"decision" : "SEND_BACK",
			 "serEventId":servEventId};		
	 var returnData = __doAjaxRequest(url, 'POST', data, false);
	 $.each( returnData, function( key, value ) {
		 $('#eventEmp').append('<option value="' + key + '">' + value  + '</option>');
	 });
	 if(servEventId=="0_0"){
		 //$("#sendBackEmp").hide();
	 }else{
		 $("#sendBackEmp").show();
	 }

}

function hideAndShowButton(){
	var url = 'MutationAuthorization.html'+'?'+'hideAndShowButton';
	var decision = $('input[name="workflowActionDto.decision"]:checked').val();
	var data = {"decision" : decision};		
	var returnData = __doAjaxRequest(url, 'POST', data, false);
	$(formDivName).html(returnData);
}
