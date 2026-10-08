$(document).ready(function() {
	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true,
	});
	

});

function toCheck(element) {

	var errorList = [];
	var fromDate = $("#fromDate").val();
	var toDate = $("#toDate").val();
	var callType = $("#callType1").val();
	var subType=$("#callType2").val();
	var location = $("#location").val();
	var date=new Date();
	
	if (callType == null || callType == 0) {
		errorList.push(getLocalMessage("Please select Complaint Type "));
	}
	if (subType == null || subType == 0) {
		errorList.push(getLocalMessage("Please select Complaint Sub-type"));
	}
	if (location == "") {
		errorList.push(getLocalMessage("Please select Location"));
	} 
	if (fromDate == null || fromDate == "") {
		errorList.push(getLocalMessage("Please select From Date"));
	}
	if (toDate == null || toDate == "") {
		errorList.push(getLocalMessage("Please select To Date"));
	}

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
	else{
		var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;						//From script library
		var eDate = new Date(fromDate.replace(pattern, '$3-$2-$1'));
		var sDate = new Date(toDate.replace(pattern, '$3-$2-$1'));

		if (eDate > sDate) {
			errorList.push(getLocalMessage("To Date cannot be less than From Date"));
		}
		if (sDate >= date) {
			errorList.push(getLocalMessage("To Date cannot be greater than Current Date"));
		}

		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
		}
		else {
			
			var reqData= 'callType=' + callType+ '&location=' + location +  '&fromDate='+ fromDate+ '&toDate='+toDate + '&subType=' + subType;
			var URL = 'DisCallDetails.html?GetDisCallDetails';
			var resData = __doAjaxRequest(URL, 'POST', reqData, false);
			if(resData == "false") {
				errorList.push(getLocalMessage("Please enter valid input"));
				displayErrorsOnPage(errorList);
			}
			else {
				window.open(resData,'_blank' );
			}
			return true;		
		}
	} 

}




