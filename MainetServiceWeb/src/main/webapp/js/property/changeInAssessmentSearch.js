$(document).ready(function(){
	
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	 
	 $("#resetform").on("click", function() {
		window.location.reload("#changeAssessmentSearch")
	});
});

function redirectToSearchPropNo(obj) {
	$(".error-msg").hide();
		showloader(true);
		setTimeout(function () {
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
				    	SearchButton(obj);
				    } 
			}else{
				displayErrorsOnPage(errorList);
			}
			showloader(false);
		},2000);
	}

function SearchButton(obj) {
	$(".error-msg").hide();
	showloader(true);
	setTimeout(function () {
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
			var theForm	=	'#changeAssessmentSearch';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'ChangeInAssessmentForm.html?displayChangeInAssessmentForm';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			$(formDivName).html(returnData);
			
			reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
			reorderfactor('.specFact','.eachFact');
			
			if(billMethod == 'I'){
				 $("#showFlatNo").show();
		    	  $("#serchBtn").show();
		    	  $("#serchBtnDirect").hide();
			}
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
	},2000);
 }

/*function SearchButton1(element) {
	
	var theForm	=	'#changeAssessmentSearch';
	var requestData = {};
	requestData = __serializeForm(theForm);

	var ajaxResponse = __doAjaxRequest(
			'ChangeInAssessmentForm.html?displayChangeInAssessmentForm', 'POST',
			requestData, false, 'html');
	
	// for Assessment Type  
	if(($('option:selected', $("#selectedAssType")).attr('code')) == 'C'){
	$(".dataDiv").html(ajaxResponse);
	}
	$("#dataDiv").html(ajaxResponse);
	reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table

	reorderfactor('.specFact','.eachFact');
	
	return false;
	
}*/

function ValidateInfoDetails(errorList){

	if ($("#proertyNo").val() == "" && $("#oldPid").val() == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	return errorList;
}

function backToMain(){
	var data={};
	var URL='PropertyAssessmentType.html?backToMainPage';
	var returnData=__doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
}