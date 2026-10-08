
function searchRecords(ele){
	var errorList = [];
	var csCcn = $('#csCcn').val();
	var billNo = $('#billNo').val();
     var requestData = {"csCcn":csCcn,"billNo":billNo}
     errorList = ValidateInfo(errorList);	
     if (errorList.length == 0) {
     var URL = "ArrearEntryDeletion.html?search"
     var returnData =__doAjaxRequest(URL, 'POST', requestData, false); 
     $(formDivName).html(returnData);
     }
     else {
    	displayErrorsOnPage(errorList);
 		return false;
 	}
   
}
function ValidateInfo(errorList){
	var csCcn = $('#csCcn').val();
	
	if (csCcn == "" || csCcn == 0 || csCcn === undefined) {
		errorList.push(getLocalMessage("water.arrear.validconnNo"));
	}
	return errorList;
}

function backActionForm() {

	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', 'ArrearEntryDeletion.html');
	$("#postMethodForm").submit();
}


function deleteArrears(element){
	var errorList = [];
	var billRemark = $('#billRemark').val();
	if (billRemark == "" || billRemark == undefined) {
		errorList.push(getLocalMessage("sel.remark"));
	}
	if (errorList.length == 0) {
		var theForm	=	'#ArrearEntryDeletion';
		var requestData = $('#csIdns').val();
		
		saveOrUpdateForm(element,
					"Arrears Deleted Successfully!",
					'AdminHome.html', 'saveform');
	}
	else {
    	displayErrorsOnPage(errorList);
 		return false;
 	}
}