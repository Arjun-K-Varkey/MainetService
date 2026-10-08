/* Date Field */
$(document).ready(function() {

	var date=new Date($('#createdDate').val());

	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true,
	});
	
	$(".timepicker").timepicker({
		changeMonth : true,
		changeYear : true,
		minDate : '0',
	});

	$("#callAttendDate").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		yearRange: "-200:+200",
		minDate : date,
		maxDate: new Date(),  
	});
	

	$(function() {
		$('.datetimepicker3').timepicker();
	});
	
	$("#complainRegDataTable").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	
});


/* Search Criteria */
function searchDeptScrutiny() {
	
	var errorList = [];
	var complainNo = $('#complainNo').val();
	var frmDate = $('#frmDate').val();
	var toDate = $('#toDate').val();
	var date = new Date();

	if (frmDate == "" &&  toDate == "" && complainNo == "") {
		//errorList.push(getLocalMessage("Please enter at least one search criteria"));
	} 
	else if (complainNo != "" ) {
		//errorList.push("Please enter complain number");
	} 
	else if (frmDate != "" && toDate != "") {
		//errorList.push("Please enter valid from date and to date");
	} 
	else {
		errorList.push(getLocalMessage("Please enter from date and to date"));
	}
	
	
	/*if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}*/	
		
	/*if(complainNo == null && complainNo == "") {
		errorList.push(getLocalMessage("Please enter complain number"));
	}
	
    if((frmDate == null && frmDate == "") || (toDate == null && toDate == "")) {	
		if (frmDate == null || frmDate == "") {
			errorList.push(getLocalMessage("Please select From Date "));
		}
		if (toDate == null || toDate == "") {
			errorList.push(getLocalMessage("Please select To Date"));
		}
		errorList.push(getLocalMessage("Please select From Date and To Date"));
    }*/
    
	
	if (errorList.length > 0) {
		checkDate(errorList);
	} else {
		var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
		var eDate = new Date(frmDate.replace(pattern, '$3-$2-$1'));
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
	}
	
	if ((complainNo != null && complainNo != "") || (frmDate!=null && frmDate!="" && toDate!=null && toDate!="")) {
		var requestData = "complainNo=" + $('#complainNo').val() + "&frmDate=" + $('#frmDate').val() + "&toDate=" + $('#toDate').val();
		var table = $('#complainRegDataTable').DataTable();
		table.rows().remove().draw();
		$(".warning-div").hide();
		var response = __doAjaxRequest('DepartmentalScrutiny.html?searchComplain', 'POST', requestData, false, 'json');
		
		var complainRegisterDTOList = response;
		if (complainRegisterDTOList.length == 0) {
			errorList.push(getLocalMessage("record not found / enter valid search criteria"));
			displayErrorsOnPage(errorList);
			return false;
		}
		var result = [];
		$
				.each(
						complainRegisterDTOList,
						function(index) {
							
							var obj = complainRegisterDTOList[index];
							let complainNo = obj.complainNo;
							let createdDate = obj.createdDate;
							let complaintType1 = obj.complaintType1Desc;
							let complaintDescription = obj.complaintDescription;
							let complainId = obj.complainId;

							result
									.push([
											'<div class="text-center">'
													+ (index + 1) + '</div>',
											'<div class="text-center">'
													+ complainNo + '</div>',
											'<div class="text-center">'
													+ getDateFormat(createdDate) + '</div>',
											'<div class="text-center">'
													+ complaintType1 + '</div>',
											'<div class="text-center">'
													+ complaintDescription + '</div>',
											'<div class="text-center">'
													+ '<button type="button" class="btn btn-warning btn-sm "  onclick="modifyComplainReg(\''
													+ complainId
													+ '\',\'DepartmentalScrutiny.html\',\'editComplainReg\',\'E\')"  title="Edit"><i class="fa fa-pencil"></i></button>'
													+ '</div>' ]);
						});
		table.rows.add(result);
		table.draw();
	} else {
		errorList.push(getLocalMessage("Please enter valid search criteria"));
		displayErrorsOnPage(errorList);
	}
}


/* Open Form On Edit Action */
function modifyComplainReg(complainId, formUrl, actionParam, mode) {
	
	var divName = '.content';
	var requestData = {
	"mode" : mode,
	"id"   : complainId
	};
	
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData, 'html', false);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
}

function setStatusApproval(){
	$("#approve").val('A');
	$("#reject").val('');
	$("#hiddenStatus").val('Y');
}
function setStatusRejection(){
	$("#reject").val('R');
	$("#approve").val('');
	$("#hiddenStatus").val('Y');
}
/* save Complaint Registration Approval */
function confirmToProceed(element) {
	var errorList = [];
	var remark = $("#remark").val();
	var hiddenStatus = $("#hiddenStatus").val();
    var callAttendDate = $("#callAttendDate").val();
    var callAttendTime = $("#callAttendTime").val();
    var callAttendEmployeeList = $("#callAttendEmployeeList").val();
	if (remark == null || remark == "") {
		errorList.push(getLocalMessage("Please Enter Remark "));
	}
	if(hiddenStatus==null || hiddenStatus==""){
		errorList.push(getLocalMessage("Please Select Status"));
	}
	if (callAttendDate == null || callAttendDate == "" || callAttendDate == undefined) {
		errorList.push(getLocalMessage("validation.complainRegisterDTO.callAttendDate"));
		
	}
	if (callAttendTime == null || callAttendTime == "" || callAttendTime == undefined) {
		errorList.push(getLocalMessage("validation.complainRegisterDTO.callAttendTime"));
		
	}
	if (callAttendEmployeeList == null || callAttendEmployeeList == "" || callAttendEmployeeList == undefined) {
		errorList.push(getLocalMessage("validation.complainRegisterDTO.callAttendEmployee"));
		
	}	
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
		//checkError(errorList);
		
	}
	else {
		return saveOrUpdateForm(element, "", 'DepartmentalScrutiny.html', 'saveform');
	} 
	
}

/* show error on page */
function checkDate(errorList) {
	var errMsg = '<ul>';
	$.each(errorList, function(index) {
		errMsg += '<li>' + errorList[index] + '</li>';
	});
	errMsg += '</ul>';
	$('#errorId').html(errMsg);
	$('#errorDivId').show();
	return false;
}

function checkError(errorList) {
	
	var errMsg = '<ul>';
	$.each(errorList, function(index) {
		errMsg += '<li>' + errorList[index] + '</li>';
	});
	errMsg += '</ul>';
	$('#errorId').html(errMsg);
	$('#errorDivId').show();
	return false;
}


/* save Complaint Registration Approval */
/*$(document).ready(function() {

	$("#hospitalDataTable").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
});*/


/* Reset Form */
/*function resetForm(element){
	$("#frmDepartmentalScrutiny").submit();
}*/




