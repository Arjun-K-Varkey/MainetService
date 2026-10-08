/**
 * 
 */

$(document).ready(function(e) {

		$('.hasTwoDecimal').change(function() {
			var $this = $(this);
			var value = parseFloat(Math.round($this.val())).toFixed(2);
			$this.val(value);
		});

		 $("input:radio[name='offlineDTO.onlineOfflineCheck']").prop('checked', true);
		 
		$("#payAmount").keyup(function() {
			$("#amountToPay").val($("#payAmount").val());
		});

		$("#amountToPay").val($("#payAmount").val());
		
		 $('#manualReceiptdate').datepicker({
		    	dateFormat : 'dd/mm/yy',
		    	changeMonth : true,
		    	changeYear : true,    	
		    	yearRange : "-100:-0",
		        });
		 
		var counterScheduleFlag  = $("#counterScheduleFlag").val();
		var errorList = [];
		if (counterScheduleFlag == 'Y'){
			errorList.push(getLocalMessage("Counter.ScheduledValidation"));
			displayErrorsOnPage(errorList);
			return false;		
			}
		var key1=$("#bill_csidn").val();
		var key2=$("#messge").val();
		
		if(key1 !="" || key2 !=""){
		$("#search_1").hide();
		$("#resetbt").hide();
		$("#backbt").hide();}
		
		
		var amounts = document.querySelectorAll(".format"); 
		 amounts.forEach(function(amount) {
		     var value = parseFloat(amount.textContent || amount.value);
		     
		     if (!isNaN(value)) {  
		         var formattedValue = value.toFixed(2);  
		         
		         if (amount.tagName === 'P') {
		             amount.textContent = formattedValue; 
		         } else if (amount.tagName === 'INPUT') {
		             amount.value = formattedValue;  
		         }
		     }
		 });
	});


	$('.hasTwoDecimal').keyup(function () {
		
		var regx	=   /^\d+(\.\d{0,2})?$/;
	
		var amount	=	$(this).val();
	
		if(!regx.test(amount))
		{
			amount = amount.substring(0, amount.length-1);
			
			$(this).val(amount);	
		}   
	});

function serachWaterBillPaymentData(obj) {
		var formName = findClosestElementId(obj, 'form');
		var theForm = '#' + formName;
		var errorList = [];
	    var connum = $("#connum").val();
	    var counterScheduleFlag = $("#counterScheduleFlag").val();
	if (counterScheduleFlag !='Y' &&  connum == "") {
		errorList.push(getLocalMessage("water.billPayment.search"));
	}
	if (counterScheduleFlag == 'Y') {
		errorList.push(getLocalMessage("Counter.ScheduledValidation"));
	}
	if (errorList.length == 0) {
		var url = 'WaterBillPayment.html?serachWaterBillPayment';
		$(theForm).attr('action', url);
		$(theForm).submit();
	} else {
		displayErrorsOnPage(errorList);
	}
}

	function resetbillPayment(element) {
		$("#WaterBillPaymentId").submit();
	}

	jQuery('.maxLength30').keyup(function() {
		$(this).attr('maxlength', '60');

	});
	
	function disposeModalBox1()
	{	
		$('#'+modalDivName).hide();
		return false;
	}
	
	function resetManualForm() {
		$("#connum").val('');
		$("#manualReceiptdate").val('');
	}
	function resetCommonForm() {
		value = "CommonManualReceiptEntry.html";
		$("#postMethodForm").prop('action', '');
		$("#postMethodForm").prop('action', value);
		$("#postMethodForm").submit();
	}
	 function collectionModeValidation(errorList) {
		 
			$(".billDetails").each(function(i) {
			//	errorList = validatePayModeDetails(errorList, i);
				
			});
			if(errorList.length==0){

				 var payAmount = $("#payAmount").val();
				if(payAmount=='' ||payAmount == undefined || (payAmount<=0))
				{
					errorList.push(getLocalMessage("property.billPayment.amount"));
				}
				 var multiAmount = $("#multiAmount").val();
				 if(payAmount!=undefined && payAmount!=""&& payAmount!=multiAmount){
					 errorList.push("Payble amount and total amount paying by multiple payment mode should be same.");
				 }
				 }
			return errorList;
		}
	 function payAmoutFun(){
		 $("#amount0").val($("#payAmount").val());
			// Defect id #147493
			$(".billDetails").each(function(i){
				$("#amount"+(i+1)).val("");
			});
			$("#multiAmount").val($("#payAmount").val())
	 }
	 
	 
	 
	 function saveData(element) {
			showloader(true);
			setTimeout(function () {
				var errorList = [];	
				
				var validationFlag= $("#validationFlag").val();
				if(validationFlag=='M'){
					var manualReceiptNo= $("#manualReceiptNo").val();
					if (manualReceiptNo == '' || manualReceiptNo == null || manualReceiptNo == undefined) {
						errorList.push(getLocalMessage('prop.select.manual.recipt.number'));
					}
				}
				var collectionMode=$("#payModeIn").val();
				if(collectionMode == "0"){
					errorList.push(getLocalMessage('prop.select.collection.mode'));
				}else{
					var payMode = $('#payModeIn option[value="' + $("#payModeIn").val() + '"]').attr('code')
					if(payMode != "C" && payMode != "POS"){
						errorList = validateBill(errorList);
					}
				}				
				
				
				
				var collectionType= $("#payCounter").val();			
				//errorList = validationForCollectionType(errorList); 
				/*if (errorList.length == 0
						&& $("input:radio[name='offlineDTO.onlineOfflineCheck']")
								.filter(":checked").val() != 'POS') {
					errorList = collectionModeValidation(errorList);
				}*/
				
				var mobileNumber = $("#mobileNo").val();
				var filter = /^[789]\d{9}$/;
				if (mobileNumber != '') {
					if (!filter.test(mobileNumber)) {
						errorList.push(getLocalMessage("update.invalidNumber"));
					}
				}
				
				var applicantEmailId = $.trim($('#emailId').val());
				if (applicantEmailId != '' && applicantEmailId != null
						&& applicantEmailId != undefined) {
					if (!validateEmail(applicantEmailId)) {
						errorList.push(getLocalMessage('update.invalid.email'));
					}
				} 
				
				if (errorList.length == 0) {
		
					if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
							":checked").val() == 'Y') {
						return saveOrUpdateFormWat(element,
								"Bill Payment done successfully!",
								'WaterBillPayment.html?redirectToPay', 'saveform');
					} else {
						return saveOrUpdateFormWat(element,
								"Bill Payment done successfully!",
								'WaterBillPayment.html?PrintReport', 'saveform');
					} 
				} else {
					displayErrorsOnPage(errorList);
				}
				showloader(false);
			},2000);
		}
	 
	 
	 function saveOrUpdateFormWat(obj, successMessage, successUrl, actionParam)
	 {
	  
	  
	 	
	 	if (!actionParam) {
	 		
	 		actionParam = "save";
	 	}
	 	
	 	//return doFormAction(obj,successMessage, actionParam, true , successUrl);
	 	return doFormActionForSaveWat(obj,successMessage, actionParam, true , successUrl);
	 }

	 function doFormActionForSaveWat(obj,successMessage, actionParam, sendFormData, successUrl)
	 {
	 	var	formName =	findClosestElementId(obj, 'form');
	 	var theForm	=	'#'+formName;
	 	
	 	             var requestData = {};
	 	
	 				if (sendFormData) {
	 					
	 					requestData = __serializeForm(theForm);
	 				}
	 			
	 				var url	=	$(theForm).attr('action')+'?' + actionParam;
	 				 
	 				
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
	 						   		
	 						   		showSaveResultBoxwt(returnData, message, 'AdminHome.html');
	 						   	
	 						   	else
	 						   		
	 						   		showSaveResultBoxwt(returnData, message, successUrl);
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
	 				showloader(false);
	 				return false;
	 				
	 	
	 }
	 
	 function showSaveResultBoxwt(returnData, successMessage, redirectUrl) {
			
			var success = returnData['command']['status'];
			var commandMessageText = returnData['command']['message'];
			
			var messageText = success ? successMessage : commandMessageText;
			
			var message='';
			var cls = getLocalMessage('bt.proceed');
			
			 if(redirectUrl == 'AdminHome.html'){
				 message	+='<p class="text-blue-2 text-center padding-15">'+ messageText+'</p>';
				 message	+='<div class=\'text-center padding-bottom-10\'>'+	
				'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-success\'    '+ 
				' onclick="goToDashBoard()"/>'+	
				'</div>';
		    	 
		    } 
			else if(redirectUrl=="" || redirectUrl==null){
			
				message	+='<p class="text-blue-2 text-center padding-15">'+ messageText+'</p>';
				message	+='<div class=\'class="text-center padding-bottom-10"\'>'+	
				
				'</p>';
				
			}
			else{
		    
			 message	+='<p class="text-blue-2 text-center padding-15">'+ messageText+'</p>';
			 message	+='<div class=\'text-center padding-bottom-10\'>'+	
			'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-success\'    '+ 
			' onclick="closeboxwat(\''+errMsgDiv+'\',\''+redirectUrl+'\')"/>'+	
			'</div>';
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
	 
	 function closeboxwat(divName,redirectUrl){
			if(typeof redirectUrl!== undefined && redirectUrl!=='undefined' && redirectUrl!=null && (redirectUrl!=''))
			{	
				try
				{
				 var lastIndex = redirectUrl.lastIndexOf("?");
				 
			     var lastchar = redirectUrl.substring(lastIndex	+	1);	
			     	   	   
				if(lastchar == 'PrintReport' || lastchar == 'PrintChallanOnline' || lastchar == 'PrintCounterReceipt' || lastchar == 'PrintChecklistReport')
					{
					window.open(redirectUrl,'_blank');	//print loi and challan receipt		
					 //change for blank receipt issue
					
					setTimeout(function(){
						value = "WaterBillPayment.html";
		                $("#postMethodForm").prop('action', '');
		                $("#postMethodForm").prop('action', value);
		                $("#postMethodForm").submit();
					}, 3000);
					}
				else if(lastchar == 'PrintULBCounterReceipt' )
				     {
						 window.open(redirectUrl,'_blank');			
						 window.location.href='ChallanAtULBCounter.html?clean';
					}
				else if(lastchar == 'PrintRegenerateChallan'){
					 window.open(redirectUrl,'_blank');			
					 window.location.href='ChallanAtULBCounter.html?search';
				}
				else
					{ 
					//printchecklistreport
						$("#postMethodFormSuccess").prop('action', '');
						$("#postMethodFormSuccess").prop('action', redirectUrl);
						$("#postMethodFormSuccess").submit();			
					}
				}
				catch(e)
				{
					//window.location.href=redirectUrl;
					$("#postMethodFormSuccess").prop('action', '');
					$("#postMethodFormSuccess").prop('action', redirectUrl);
					$("#postMethodFormSuccess").submit();
				}
				   //this code snippet is moved to bottom to increase the time window between window popup and redirection.

			}
			
			disposeModalBox();
			
			return false;
		}

		function validateEmail(email) {
			var regexPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			return regexPattern.test(email);
		}
		
		function validateBill(errorList){
			var bankId = $("#bankID").val();
			var accNo = $("#acNo").val();
			var chqNo = $("#chqNo").val();
			var chqDate = $(".chqDate").val();
			 var payMode = $('#payModeIn option[value="' + $("#payModeIn").val() + '"]').attr('code');
			if(bankId == 0){
				errorList.push(getLocalMessage("water.chalan.validation.bankid"));
			}
			if(accNo == "" || accNo == undefined){
				/*errorList.push(getLocalMessage("water.chalan.validation.acc.no"));*/
		        if (payMode !="D") {
		            errorList.push(getLocalMessage("water.chalan.validation.acc.no"));
		        }
			}
			if(chqNo == "" || chqNo == undefined){
				errorList.push(getLocalMessage("water.chalan.validation.chq.no"));
			}else if (!/^\d{6}$/.test(chqNo)) {
			    /*errorList.push(getLocalMessage("water.chalan.validation.chq.no.digit"));*/
		        if (payMode == "Q") {
		            errorList.push(getLocalMessage("water.chalan.validation.chq.no.digit"));
		        } else if (payMode == "D") {
		            errorList.push(getLocalMessage("water.chalan.validation.dd.no.digit"));
		        }
			}
			if(chqDate == "" || chqDate == undefined){
				errorList.push(getLocalMessage("water.chalan.validation.chq.date"));
			}
			 return errorList;
			
		}
		function displayErrorsOnPage(errorList) {
			var errMsg = '<button type="button" class="close" onclick="closeOutErrBox()" aria-label="Close"><span aria-hidden="true">&times;</span></button>';

			errMsg += '<ul>';

			$.each(errorList, function(index) {
				errMsg += '<li>' + errorList[index] + '</li>';
			});

			errMsg += '</ul>';

			$('html,body').animate({
				scrollTop : 0	
			}, 'slow');
			$('.error-div').html(errMsg);
			$(".error-div").show();
			return false;
		}
