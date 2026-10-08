$(document).ready(function(){
	prepareDateTag();
	let totalPay= Number($('#totalPayable').val());
	$('#totalPayable').val(totalPay.toFixed(2));
	
	let payAmt= Number($('#payAmount').val());
	if(parseInt(payAmt) <= 0){
		payAmt = totalPay;
	}
	$('#payAmount').val(payAmt.toFixed(2));
	checkRebateAppl();
});
 
function backToFormURL() {
	$.fancybox.close();
}

 function savePropertyFrom(element,flag){
	 showloader(true);
		setTimeout(function () {
			 var collectionType= $("#payCounter").val();
			 var propBillSuccessMsg = getLocalMessage("propertyBill.SuccessMsg");
			 var AdvPaySuccessMsg = getLocalMessage("propertyBill.AdvancePaymentSuccessMsg");
			 var errorList = [];
		
				 $('.billDetails').each(function(i) {
					errorList = validatePayModeDetails(errorList,i);
			     });
				var partialAmt= $("#payAmount").val();
				var emailId = $("#emailId").val();
			    var mobileNo = $("#mobileNo").val();
				var a = $("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val();
				if(partialAmt=='' ||partialAmt == undefined || (partialAmt<=0))
				{
						errorList.push(getLocalMessage("property.billPayment.amount"));
				}
				/*if (emailId != "") {
					var emailRegex = new RegExp(/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
					var valid = emailRegex.test(emailId);
					if (!valid) {
					    errorList.push(getLocalMessage('update.invalid.email'));
					}
				    }*/
				/*if(mobileNo=='' || mobileNo == undefined){
					errorList.push(getLocalMessage('ownersdetail.MobileNumber'));
				}*/
				/*else if (mobileNo != '' && mobileNo.length < 10) {
				    errorList.push(getLocalMessage('ownersdetail.MobileNumber.validate'));
				}*/
				 if (errorList.length > 0) 
				{
						showError(errorList);
				}/*else if($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == '' || $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == undefined){
					errorList.push(getLocalMessage("Please select collection mode"));
					showError(errorList);
				}*/
				else{
			 	$("#errorDiv").hide();	
			 	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'Y') {
			 		 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'PropertyBillPayment.html?redirectToPay', 'saveform');
					 
			 		}
			 		else
			 			{
		
			 			validatePayAtUlbCounter(errorList);
			 			if (errorList.length > 0) {
			 				showError(errorList);
			 			}else{
			 				//return saveOrUpdateForm(element,"Bill Payment done successfully!", 'PropertyBillPayment.html?PrintReport', 'saveform');
			 				$.fancybox.close();
			 				 var payableAmount = $('#payAmount').val();
			 				var totalPayableAmount = $('#totalPayable').val();
			 				
			 				/*if(parseInt(payableAmount) > parseInt(totalPayableAmount)){
		 			    		showBoxForApproval(AdvPaySuccessMsg,element);
		 			    	}else{*/
		 			    		
		 			    			doPaymentProps(element);
		 			    		/*var divName = '.content-page';
		
		 			 			var formName = findClosestElementId(element, 'form');
		 			 			var theForm = '#' + formName;
		 			 			var requestData = __serializeForm(theForm);
		 			 			var object = __doAjaxRequest(
		 			 				"PropertyBillPayment.html?saveBillPayment", 'POST',
		 			 				requestData, false, 'json');*/
		
		 			 			/*if (object.error != null && object.error != 0) {
		 			 			    $.each(object.error, function(key, value) {
		 			 				$.each(value, function(key, value) {
		 			 				    if (value != null && value != '') {
		 			 					errorList.push(value);
		 			 				    }
		 			 				});
		 			 			    });
		 			 			    displayErrorsOnPage(errorList);
		 			 			} else {
		 			 			    if (object.successFlag != null && object.successFlag == 'Y') {
		 			 			    		
		 			 			    		showBoxForApproval(propBillSuccessMsg,element);
		 			 			    }
		 			 			}*/
		 			    	//}
				 			
			 			}
			 			 
			 			}
			 		
			 	
			 	}
				 showloader(false);
		},2000);
	 }
 


 function validatePayAtUlbCounter(errorList) {

	$('.billDetails').each(function(i) {

		var payModeCode = $('option:selected', $("#payModeIn"+i)).attr('code');		
		var payModeIn = $("#payModeIn" + i).val();
		var bankID = $("#bankID" + i).val();
		var bmChqDDDate = $("#bmChqDDDate" + i).val();
		var chqueNoId = $("#chqueNoId" + i).val();
		var amount = $("#amount" + i).val();
		if (payModeIn == '0' || payModeIn == null) {
			errorList.push(getLocalMessage("prop.validPayMode"));
		}
		if (payModeCode == 'D' || payModeCode == 'Q') {
			if (bankID == '0' || bankID == null) {
				errorList.push(getLocalMessage("prop.validDrwanOn"));
			}
			if (bmChqDDDate == '') {
				errorList.push(getLocalMessage("prop.validPayDate"));
			}
			if (chqueNoId == '') {
				errorList.push(getLocalMessage("prop.validPayCheqDdNo"));
			}
		}
		if (amount == '') {
			errorList.push(getLocalMessage("prop.validPayAmount"));
		}
		if (errorList.length > 0) {
			return errorList;
		}
	});
}
 
 function prepareDateTag() {
		var dateFields = $('.trimDateTime');
		dateFields.each(function () {
			var fieldValue = $(this).val();
			if (fieldValue.length > 10) {
				$(this).val(fieldValue.substr(0, 10));
			}
		});
	}

function backToMain(){	
	var data={};
	var URL='PropertyBillPayment.html?backToMainPage';
	var returnData=__doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
}
function showError(errorList){
	var errMsg = '<ul>';
	var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeOutErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
	$.each(errorList, function(index) {
		errMsg += '<li><i class="fa fa-exclamation-circle"></i> &nbsp;' + errorList[index] + '</li>';
	});

	errMsg += '</ul>';
	$("#errorDiv").html(errMsg);
	$("#errorDiv").show();
	$("html, body").animate({ scrollTop: 0 }, "slow");
	return false;
}



function checkRebateAppl(){
	var halRebate= Number($("#halfPaymentRebate").val());
	var fullOutStandingAmount= $("#fullOutstandingAmount").val();
	var halfOutStandingAmount= $("#halfOutstandingAmount").val();
	var receivedAmount = $("#totalPayable").val();
	var userPayingAmount = $("#payAmount").val();
	var rebateApplFlag= $("#rebateApplFlag").val();
	var displayRebate = Number($("#actualRebate").val());
	var PaidAmountInCurrentYear = $("#PaidAmountInCurrentYear").val();
	var actualFullOutstandingAmount = $("#actualFullOutstandingAmount").val();
	var actualHalfOutstandingAmount = $("#actualHalfOutstandingAmount").val();
	var penaltyDiscount = $("#penaltyDiscount").val();
	
	
	userPayingAmount = parseInt(userPayingAmount )+parseInt(PaidAmountInCurrentYear);
	if(rebateApplFlag == 'Y'){
		var minAmountPayForFullRebate = (fullOutStandingAmount) - (displayRebate);
		minAmountPayForFullRebate =  parseInt(minAmountPayForFullRebate) - parseInt(penaltyDiscount);
		
		var minActualAmountPayForFullRebate = parseInt(actualFullOutstandingAmount) - parseInt(displayRebate);
		 minActualAmountPayForFullRebate = parseInt(minActualAmountPayForFullRebate) - parseInt(penaltyDiscount);
		var minActualAmountPayForHalfRebate = parseInt(actualHalfOutstandingAmount) - parseInt(halRebate);
		var minAmountPayForHalfRebate = halfOutStandingAmount;
		if((parseInt(userPayingAmount)) >= (parseInt(minActualAmountPayForFullRebate))){
			let totalPay= Number(minAmountPayForFullRebate);
			$('#totalPayable').val(totalPay.toFixed(2));
			$('#rebateId').text(displayRebate.toFixed(2));
		}else if((parseInt(userPayingAmount)) >= (parseInt(minActualAmountPayForHalfRebate))){
			var receivedAmount = fullOutStandingAmount - halRebate;
			let totalPay= Number(receivedAmount);
			$('#totalPayable').val(totalPay.toFixed(2));
			$('#rebateId').text(halRebate.toFixed(2));
		}else{
			$("#rebateId").text('0.00');
			let totalPay= Number(fullOutStandingAmount);
			$('#totalPayable').val(totalPay.toFixed(2));
		}
		
	}
	$("#amount0").val($("#payAmount").val());
	// Defect id #147493
	$(".billDetails").each(function(i){
		$("#amount"+(i+1)).val("");
	});
	$("#multiAmount").val($("#payAmount").val());
}


function showBoxForApproval(succesMessage,element) {
	
	var propBillSuccessMsg = getLocalMessage("propertyBill.SuccessMsg");
	var AdvPaySuccessMsg = getLocalMessage("propertyBill.AdvancePaymentSuccessMsg");
    var childDivName = '.msg-dialog-box';
    var message = '';
    var Proceed = getLocalMessage("unit.proceed");
    var no = 'No';
    var Yes = getLocalMessage("propertyBill.Yes");
    var No = getLocalMessage("propertyBill.No");
    var uStatus ="";
    message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
	    + '</p>';

	/*var redirectUrl = 'PropertyBillPayment.html?PrintReport';
	if(succesMessage == AdvPaySuccessMsg){*/
		message += '<div class=\'text-center padding-top-15 padding-bottom-15\'>' + '<p>'
		+ '<input class="btn btn-success" id="btnNo" style="margin-right:10px" type=\'button\' value=\''+Yes+'\' '
		+ ' onclick="doPayment(\'' + element + '\')"/>'
		+ '<input class="btn btn-danger" style="margin-right:10px" type=\'button\' value=\''+No+'\' '
		+ ' onclick="backToFormURL();"/>'
		+ '</p>' + '</div>';
		
	/*}
	else{
			message += '<div class=\'text-center padding-bottom-10\'>'
				+ '<input type=\'button\' value=\''+ Proceed +'\' '
				+ '\'  id=\'btnNo\' class=\'btn btn-success\'    '
				+ ' onclick="closeboxProperty(\'' + errMsgDiv + '\',\'' + redirectUrl
				+ '\')"/>' + '</div>';
		}*/
    $(childDivName).addClass('ok-msg').removeClass('warn-msg');
    $(childDivName).html(message);
    $(childDivName).show();
    $('#btnNo').attr('onclick','doPaymentProps('+element+')')
     showModalBoxWithoutClose(childDivName);
    $('#btnNo').focus();
}

function closeApproval() {
    window.location.href = 'PropertyBillPayment.html';
    $.fancybox.close();
}

function closebox(divName,redirectUrl){
	
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
						value = "PropertyBillPayment.html";
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
	
		disposeModalBox();
		
	}
	
	
	return false;
}

function disposeModalBox1()
{	
	$('#'+modalDivName).hide();
	return false;
}


function doPayment(element){
	$.fancybox.close();
	var divName = '.content-page';
	 showloader(true);
		setTimeout(function () {
				var propBillSuccessMsg = getLocalMessage("propertyBill.SuccessMsg");
					/*var formName = findClosestElementId(element, 'form');
					var theForm = '#' + formName;*/
					var theForm	=	'#PropertyBillPayment';
					var requestData = __serializeForm(theForm);
					var object = __doAjaxRequest(
						"PropertyBillPayment.html?saveBillPayment", 'POST',
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
					    if (object.successFlag != null && object.successFlag == 'Y') {
					    	
					    		showBoxForApproval(propBillSuccessMsg,element);
					    	
					    }
					}
			showloader(false);
		},50);
}


function doPaymentProps(elements){
	var successMessage ="Bill Payment Done";
	var successUrl ="PropertyBillPayment.html?PrintReport";
	var actionParam ="saveform";
		return saveOrUpdateForm(elements,"Bill Payment done successfully!", 'PropertyBillPayment.html?PrintReport', 'saveform');
	
}
