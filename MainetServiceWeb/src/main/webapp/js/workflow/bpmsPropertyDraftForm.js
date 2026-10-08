$(document)
		.ready(
				function() {

					$("#btnSave")
							.click(
									function(e) {
										var errorList = [];
										var empId = $("#EmployeeName").val();

										if (empId === null || empId === '') {

											errorList
													.push(getLocalMessage("care.error.employee"));
										}

										if (errorList.length == 0) {
											var data = new FormData(
													document
															.getElementById("ForwardComplaintForm"));
											$.ajax({
														url : "NewRegistrationFormDraftApplicationWorkFlowInitiate.html?saveDetails",
														type : "POST",
														data : data,
														enctype : 'multipart/form-data',
														processData : false,
														contentType : false,
														async : false,
														success : function(
																response) {
															if (response != null) {
																displayMessageOnSubmit(response);
															}
														},
														error : function(
																response) {
															displayMessageOnError(response);
														}
													});

										} else {
											displayErrorsOnPage(errorList);
											$('html,body').animate({
												scrollTop : 0
											}, 'slow');
										}
										return true;

									});
					
					$("#jointOwnerTable").show();
				});

function displayMessageOnSubmit(Msg) {
	var errMsgDiv = '.msg-dialog-box';
	var message = Msg;
	
	message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0"></h4>';
	
	message += '<div class="text-center">'
		+ '<a href="AdminHome.html" class=\'btn btn-blue-2 margin-bottom-10\'>'+getLocalMessage("care.close")+'</a>'
		+ '</div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutClose(errMsgDiv);
}


function BpmsSearchButton(obj) {
	
	showloader(true);
	setTimeout(function () {
		
		var errorList = [];
		var praposalNo = $("#propId").val();
		if(praposalNo == null || praposalNo == '' || praposalNo == '0'){
		errorList.push(getLocalMessage("bpms.select.proNo"));
		}
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			var requestData = {
					"praposalNo" : praposalNo
				    };
			var URL = 'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?searchBpmsPropId';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			
			$(formDivName).html(returnData);
			
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			reorderfactor('.specFact','.eachFact');
			
			
				 $("#showFlatNo").show();
		    	  $("#serchBtn").show();
		    	  $("#serchBtnDirect").hide();
		    	  $("#OwnshipDetail").show();
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }


function BpmsView(proId, bpmsViewFlg) {
	
	showloader(true);
	setTimeout(function () {
		
		var errorList = [];
		var praposalNo = $("#propId").val();
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			var requestData = {
					"praposalNo" : proId,
					"bpmsViewFlg" : bpmsViewFlg
				    };
			var URL = 'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?viewBpmsPropId';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			
			$(formDivName).html(returnData);
			
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			reorderfactor('.specFact','.eachFact');
			
			
				 $("#showFlatNo").show();
		    	  $("#serchBtn").show();
		    	  $("#serchBtnDirect").hide();
		    	  $("#OwnshipDetail").show();
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }

function editBpmsForm(proId, bpmsEditFlg) {
	
	showloader(true);
	setTimeout(function () {
		
		var errorList = [];
		var praposalNo = $("#propId").val();
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			var requestData = {
					"praposalNo" : proId
					
				    };
			var URL = 'NewRegistrationFormDraftApplicationWorkFlowInitiate.html?editBpmsForm';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			
			$(formDivName).html(returnData);
			
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			reorderfactor('.specFact','.eachFact');
			
			
				 $("#showFlatNo").show();
		    	  $("#serchBtn").show();
		    	  $("#serchBtnDirect").hide();
		    	  $("#OwnshipDetail").show();
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }




