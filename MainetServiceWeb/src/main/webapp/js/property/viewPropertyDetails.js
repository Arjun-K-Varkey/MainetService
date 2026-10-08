$(document).ready(function(){
	
	 $("#serchBtnDirect").hide();
	
});
function populateFlatNo(obj) {
	
		showloader(true);
		setTimeout(function () {
		 var propNo=$("#proertyNo").val();
		    var requestData = {
			"propNo" : propNo
		    };

		    var ajaxResponse = doAjaxLoading(
			    'ViewPropertyDetail.html?getBillingMethod',
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
				SearchButton();
		}
			showloader(false);
		},2000);
	}

function SearchButton(element) {
	showloader(true);
	setTimeout(function () {
		$(element).prop('disabled',true);
		var data = $('#ViewPropertyDetail').serialize();
		var URL = 'ViewPropertyDetail.html?searchData';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$(formDivName).html(returnData);
		$(element).prop('disabled',false);
		reloadGrid('gridViewPropertyDetail');
		showloader(false);
	},1000);
}

function displayError(errorList){
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
 
 function ViewAssDetails(bmid){
	 	var data={
	 		"bmIdNo":bmid
	 	};
		var URL='ViewPropertyDetail.html?viewPropDet';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		$(formDivName).html(returnData);
}
  function downoladBill(bmid){
	 	var data={
	 		"bmIdNo":bmid
	 	};
		var URL='ViewPropertyDetail.html?propertyBillDownload';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		//var filepath=$("#billFilePath").val();
		//alert("filepath"+returnData);
		window.open(returnData,'_blank' );
		//$(formDivName).html(returnData);
}
  
 function downoladReceipt(reciptId,receiptNo,receiptType){
	 	var data={
	 		"reciptId":reciptId,
	 		"receiptNo":receiptNo,
	 		"receiptType":receiptType
	 	};
		var URL='ViewPropertyDetail.html?receiptDownload';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		if(returnData!=""){
		var title='Revenue Receipt';
		var printWindow = window.open('', '_blank');
		printWindow.document.write('<html><head><title>' + title + '</title>');		
		printWindow.document.write(returnData);
		printWindow.document.write('</body></html>');
		printWindow.document.close();	
		}		
}
 
function ViewBillDetails(bmid){
	 	var data={
	 		"bmIdNo":bmid
	 	};
		var URL='ViewPropertyDetail.html?viewBillDet';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		$("#viewPropDet").html(returnData);
}

function backToViewProprtyDeatils(){
	 var data={};
			var URL='ViewPropertyDetail.html?backToViewProprtyDeatils';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$("#viewPropDet").html(returnData);
}
 
 function BackToDetails(){
	 var data={};
			var URL='ViewPropertyDetail.html?backToPropDet';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$(".content").html(returnData);
 }
 

 
 function BackToSearch(){
	 var data={};
			var URL='ViewPropertyDetail.html?backToSearch';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$(formDivName).html(returnData);
}

 function Reset(){
	 var data={};
			var URL='ViewPropertyDetail.html?resetViewProp';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$(formDivName).html(returnData);
 }
 
 function addDataEntryDetails(){
	 showloader(true);
	    setTimeout(function(){	
	 var data={};
		var URL='DataEntrySuite.html?addDataEntryDetails';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		$(formDivName).html(returnData);},2);	 
 }
 
 function SearchDESProperties(element) {
	 showloader(true);
	 setTimeout(function(){
		 codeSearchDESProperties(element)	
	 },2);
 }
 
 function codeSearchDESProperties(element){
	$(element).prop('disabled',true);
	var data = $('#DataEntrySuite').serialize();
	var URL = 'DataEntrySuite.html?searchData';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	 $(element).prop('disabled',false);
	reloadGrid('gridDataEntrySuite');
 }
 
 $(document).ready(function() {
		reloadGrid('gridViewPropertyDetail');
	});
 
//fetch API details on selection of button 
 function getLandApiDetails(obj){
 	var landTypePrefix=$(".landValue").val();
 	var data = {};
 	var URL = 'ViewPropertyDetail.html?getLandTypeApiDetails';
 	var returnData = __doAjaxRequest(URL, 'POST', data, false);		
 	$("#showAuthApiDetails").html(returnData);
 	$("#showAuthApiDetails").show();		
 }
 
 function downoladSpecialNotice(noticeNo){
	 	var data={
	 		noticeNo:noticeNo
	 	};
		var URL='ViewPropertyDetail.html?propertySpecialNoticeDownload';
		var returnData=__doAjaxRequest(URL, 'POST', data, false);
		window.open(returnData,'_blank' );
		/*var divName = formDivName;
		$('.content').removeClass('ajaxloader');
		$(divName).html(returnData);
		prepareTags();*/
		/*if(returnData!=""){
		var title='Notice';
		var printWindow = window.open('', '_blank');
		printWindow.document.write('<html><head><title>' + title + '</title>');		
		printWindow.document.write(returnData);
		printWindow.document.write('</body></html>');
		printWindow.document.close();	
		}*/		
}
 

 function openUpdateForm(formName, actionParam) {
	showloader(true);
	setTimeout(function() {
		codeopenUpdateForm(formName, actionParam)
	}, 2);
}
function codeopenUpdateForm(formName, actionParam) {
	
	//split in case of flat no 
	//var values=formName.split('_');
	let values = splitAtFirstUnderscore(formName);
	var one=values[0];
	var two=values[1];
	var len=one.length;
	var splitStr = one.substr(22, len);		
	var divName	=	".widget-content";
	if (!actionParam) {}
	else
	{
		url+='?'+actionParam;
	}
	var url="ViewPropertyDetail.html?edit";
	if(two!=undefined){
		var rowId = splitStr+'_'+two;
	}else{
		var rowId = splitStr;
	}
	var requestData = {"rowId" : rowId}
	//var theForm	=	'#ViewPropertyDetail';	
	//var requestData = __serializeForm(theForm);
	
	var flag = getBillMethodChangeFlag(rowId);
	if('Y' == flag){
		showBoxForApproval(rowId);
	} else{
		var ajaxResponse = doAjaxLoading(url, requestData, 'html', divName);
	
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
	
		prepareTags();
	
		$(divName).show();
	}
	

}

function splitAtFirstUnderscore(str) {
    let index = str.indexOf('_');
    if (index !== -1) {
        return [str.substring(0, index), str.substring(index + 1)];
    }
    return [str]; // Return original string if no underscore is found
}

function getBillMethodChangeFlag(rowId){
	var errorList = [];
	var requestData = {"rowId" : rowId}
	var object = __doAjaxRequest(
		"ViewPropertyDetail.html?getBillMethodChangeFlag", 'POST',
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
			return object.successFlag;
			//showBoxForApproval();
		}
	}
}

function showBoxForApproval(formName) {
	var rowId = formName;
	$('#error-div').css('display', 'none');

			var yes = getLocalMessage('eip.commons.yes');
			var no = getLocalMessage('eip.commons.no');
			var warnMsg = getLocalMessage('display.old.bill.bmc');

			message = '<p class="text-blue-2 text-center padding-15">'
					+ warnMsg + '</p>';
			message += '<div class=\'text-center padding-bottom-10\'>'
					+ '<input class="btn btn-success" type=\'button\' value=\''
					+ yes
					+ '\'  id=\'yes\' '
					+ ' onclick="onYes(\'yes\')"/> <input type=\'hidden\' value=\''
					+ rowId
					+ '\' id=\'row\'/> &nbsp;'
					+ '<input class="btn btn-success " type=\'button\' value=\''
					+ no + '\'  id=\'no\' '
					+ ' onclick="onYes(\'no\')"/><input type=\'hidden\' value=\''
					+ rowId
					+ '\' id=\'row\'/>' + '</div>';

			$(childDivName).addClass('ok-msg').removeClass('warn-msg');
			$(childDivName).html(message);
			$(childDivName).show();
			$('#yes').focus();
			showModalBox(childDivName);
			return false;

}

function onYes(id) {

	var rowId = $("#row").val();
	$.fancybox.close();
	var requestData = {};
	if('yes'== id){
		var URL = 'ViewPropertyDetail.html?showOldBillBeforeBMC';
		__doAjaxRequest(URL, 'POST', requestData, false);
	}
	
	var url="ViewPropertyDetail.html?edit";
	
	var requestData = {"rowId" : rowId}
	var divName	=	".widget-content";
	var ajaxResponse = doAjaxLoading(url, requestData, 'html', divName);
	
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	
	prepareTags();
	
	$(divName).show();
}

function viewRoomDetails(count, element) {
	
		$('#countOfRow').val(count);
		var countOfRow = count;
		var URL = "ViewPropertyDetail.html?viewRoomDetails";
		var data = {"countOfRow" : countOfRow};
		var returnData = __doAjaxRequest(URL, 'POST', data, false, 'html');

		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		var totalAreaval = $("#totalAreaval").val();
		totalAreaval = parseFloat(totalAreaval).toFixed(2);
		if("NaN" == totalAreaval){
			totalAreaval = "";
		}
		$("#totalAreaval").val(totalAreaval);
}
function propertyBillPrint(){
	var data = {};
	var URL = 'ViewPropertyDetail.html?viewPropertyBillPrint';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	prepareDateTag();
}
function backToPropDetailPage() {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading("ViewPropertyDetail.html?backToPropDetailPage", {},
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();

}

function propertyBillPrint1(){
	var URL = 'ViewPropertyDetail.html?viewPropertyLedgerReport';
	var billingMethod = $("#billingMethod").val();
	var propNo=$("#assNo").val();
	var flatNo=$("#flatNo").val();
	
	if(billingMethod == 'I'){
		
	}
	
	var requestData = {
			"propNo" : propNo,
			"flatNo" : flatNo
		};

		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
			window.open(returnData, '_blank');
		
		
}

function propertyBillPrint(element) {

		$(element).prop('disabled',true);
		var data = $('#ViewPropertyDetail').serialize();
		var URL = 'ViewPropertyDetail.html?viewPropertyLedgerReport';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		window.open(returnData, '_blank');
	
}

function redirectToSearchPropNo(obj) {
	
	showloader(true);
	setTimeout(function () {
		
	    var errorList = [];
	    var propNo=$("#proertyNo").val();
		var oldPropNo= $("#oldPid").val();
		if(propNo== "" && oldPropNo==""){
			errorList.push(getLocalMessage("property.changeInAss"));
		}
		if (errorList.length == 0){
			 var propNo=$("#proertyNo").val();
			    var requestData = {
				"propNo" : propNo
			    };

			    var ajaxResponse = doAjaxLoading(
				    'ViewPropertyDetail.html?getBillingMethod',
				    requestData, 'html');

			    if (ajaxResponse != null && ajaxResponse != "") {
			    	var prePopulate = JSON.parse(ajaxResponse);

			    	$.each(prePopulate, function(index, value) {
			    		$('#flatNo').append(
			    				$("<option></option>").attr("value", value).text(
			    						(value)));
			    	});
			    	$('#flatNo').trigger("chosen:updated");
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

function checkViewPropertyDues(rowId) {
	showloader(true);
	setTimeout(function () {
		var divName = ".widget-content";
		var data = {
				"rowId" : rowId,
				"fromViewPropDetrails" : 'true'
	    };
		var ajaxResponse = __doAjaxRequestForSave('PropertyDuesCheck.html?edit', 'post', data, false,
			    '', '');
		

				var errMsg = ajaxResponse["errMsg"];
		if (errMsg != '' && errMsg != undefined) {
			var errorList = [];
			errorList.push(errMsg);
			showErr(errorList);
		} else {
			//var printWindow = window.open('', '_blank');
			$(divName).removeClass('ajaxloader');
			$(divName).html(ajaxResponse);
			prepareTags();
			$(divName).show();

		}
		
	 showloader(false);
		 },2000);
	    
	}

function fetchAdjustmentHistory(ele) {
	var requestData = {};
	var returnData = __doAjaxRequest(
		'ViewPropertyDetail.html?viewHistoryDetails', 'POST', requestData,
		false, 'html');
	openPopupWithResponse(returnData);
	$(".fancybox").fancybox();
}