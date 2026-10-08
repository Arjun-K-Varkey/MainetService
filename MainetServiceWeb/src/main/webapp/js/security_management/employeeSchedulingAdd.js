$(document).ready(function() {
	
	prepareDateTag();
});
function confirmToProceed(element) {

	var errorList = [];
	errorList = validateEmployees(errorList);

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);

	} else {
		return saveOrUpdateForm(element, "", 'employeeCalendar.html',
				'saveform');
	}
}
function getStaffNameByVendorId(obj) {

	var errorList = [];
	var divName = '.content-page';
	var empTypeId = $("#empTypeId :selected").attr('code');
	if(empTypeId==null || empTypeId==undefined){
		errorList.push(getLocalMessage("EmployeeSchedulingDTO.Validation.empTypeId"));
	}
	if(errorList.length >0){
		displayErrorsOnPage(errorList);
	}
	else{
		var vendorId = $("#vmVendorid").val();
		var empTypeId = $("#empTypeId").val();
		var contStaffSchFrom = $("#contStaffSchFrom").val();
		var contStaffSchTo = $("#contStaffSchTo").val();
		var requestData = {
			'vendorId' : vendorId,
			'empTypeId' : empTypeId,
			'contStaffSchFrom' : contStaffSchFrom,
			'contStaffSchTo' : contStaffSchTo
		}
		var URL = "employeeCalendar.html?getStaffNameByVendorId";
		var response = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
		$(divName).removeClass('ajaxloader');
		$(divName).html(response);
		prepareTags();
	}

}

function resetMemberMaster(resetBtn) {
	resetForm(resetBtn);
}

function validateEmployees(errorList) {

	var errorList = [];
	var empTypeId = $("#empTypeId").val();
	var dayPrefixId = $('#employeeId').find(":selected").attr("code");
	$('#dayPrefId').val(dayPrefixId);
	var vendorId = $("#vmVendorid").val();
	var contStaffIdNo = $("#contStaffIdNo").val();
	var emplSchFrom = $("#contStaffSchFrom").val();
	var emplSchTo = $("#contStaffSchTo").val();
	var location = $("#location").val();
	var cpdShiftId = $("#cpdShiftId").val();
	if (empTypeId == "0" || empTypeId == null)
		errorList
				.push(getLocalMessage("EmployeeSchedulingDTO.Validation.empTypeId "));
	if (vendorId == "0" || vendorId == null)
		errorList
				.push(getLocalMessage("EmployeeSchedulingDTO.Validation.vendorId "));
	if (emplSchFrom == "" || emplSchFrom == null)
		errorList
				.push(getLocalMessage("EmployeeSchedulingDTO.Validation.contStaffSchFrom "));
	if (emplSchTo == "" || emplSchTo == null)
		errorList
				.push(getLocalMessage("EmployeeSchedulingDTO.Validation.contStaffSchTo "));
	/*if (emplSchFrom > emplSchTo) {
		errorList
				.push(getLocalMessage("EmployeeSchedulingDTO.Validation.fromDateToDate "));
	}*/
	var n = 0;
	var m = 0;
	var p = 0;
	var locationNew = [], cpdShiftIdNew = [], contStaffIdNoNew = [];
	$("#frmEmployeeSchedulingTbl tbody tr")
			.each(
					function(i) {

						var location = $("#location" + i).val();
						var cpdShiftId = $("#cpdShiftId" + i).val();
						var contStaffIdNo = $("#employeeId" + i).val();
						var rowCount = i + 1;

						if (rowCount == 1) {
							locationNew.push(location);
							contStaffIdNoNew.push(contStaffIdNo);
							cpdShiftIdNew.push(cpdShiftId);
						} else if (locationNew.includes(location)) {

							if (cpdShiftIdNew.includes(cpdShiftId)) {

								if (contStaffIdNoNew.includes(contStaffIdNo)) {
									errorList
											.push(getLocalMessage("EmployeeSchedulingDTO.Validation.duplicate "));
								} else {
									contStaffIdNoNew.push(contStaffIdNo);
								}
							} else {
								contStaffIdNoNew.push(contStaffIdNo);
								if (contStaffIdNoNew.includes(contStaffIdNo)) {
									cpdShiftIdNew.push(cpdShiftId);
								}
							}

						} else {
							locationNew.push(location);
							if (contStaffIdNoNew.includes(contStaffIdNo)) {
								if (cpdShiftIdNew.includes(cpdShiftId)) {
									errorList
											.push(getLocalMessage("EmployeeSchedulingDTO.Validation.duplicate "));
								} else {
									cpdShiftIdNew.push(cpdShiftId);
								}
								contStaffIdNoNew.push(contStaffIdNo);
								if (cpdShiftIdNew.includes(cpdShiftId)) {
									cpdShiftIdNew.push(cpdShiftId);
								}
							} else {

								contStaffIdNoNew.push(contStaffIdNo);
							}
						}

						if (location == 0 || location == null) {
							errorList
									.push(getLocalMessage("EmployeeSchedulingDTO.Validation.location"));
						}
						if (cpdShiftId == 0 || cpdShiftId == null) {
							errorList
									.push(getLocalMessage("EmployeeSchedulingDTO.Validation.cpdShiftId"));
						}
						if (contStaffIdNo == 0 || contStaffIdNo == null) {
							errorList
									.push(getLocalMessage("EmployeeSchedulingDTO.Validation.contStaffIdNo"));
						}
						return errorList;
					});

	return errorList;
}

function addEmployeeScheduling(formUrl, actionParam, mode) {

	var divName = '.content-page';
	var requestData = {
		"mode" : mode,
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();

}

function addEntryData() {

	$("#errorDiv").hide();
	var errorList = [];
	if (errorList.length == 0) {
		addTableRow('frmEmployeeSchedulingTbl');
	} else {
		$('#frmEmployeeSchedulingTbl').DataTable();
		displayErrorsOnPage(errorList);
	}

}

function deleteEntry(obj, ids) {

	var rowCount = $('#frmEmployeeSchedulingTbl >tbody >tr').length;
	let errorList = [];
	if (rowCount == 1) {
		errorList.push(getLocalMessage("Cannot delete"));
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	}
	deleteTableRow('frmEmployeeSchedulingTbl', obj, ids);
	$('#frmEmployeeSchedulingTbl').DataTable().destroy();
	triggerTable();
}

function searchEmployeeForAddtendance(obj) {
	var errorList = [];
	var divName = '.empAttendance';
	var empTypeId = $("#empTypeId").val();
	var dayPrefixId = $('#empTypeId').attr('code');
	var vendorId = $("#vmVendorid").val();
	var locId = $("#location").val();
	var cpdShiftId = $("#cpdShiftId").val();
	var contStaffSchFrom = $("#contStaffSchFrom").val();
	var contStaffSchTo = $("#contStaffSchTo").val();
	var contStaffName = "";
	var contStaffIdNo = "";
	
	if (empTypeId == 0 || vendorId == 0 || locId == "" || cpdShiftId == 0
			|| contStaffSchFrom == "" || contStaffSchTo == "") {
		errorList
				.push(getLocalMessage("Field with * is mandatory"));
		displayErrorsOnPage(errorList);
	}
	if (contStaffSchFrom != "" && contStaffSchTo == "") {
		errorList
				.push(getLocalMessage("securityManagement.select.scheduled.toDate"));
		displayErrorsOnPage(errorList);
	}
	if (contStaffSchTo != "" && contStaffSchFrom == "") {
		errorList
				.push(getLocalMessage("securityManagement.select.scheduled.fromDate"));
		displayErrorsOnPage(errorList);
	}
	 if (errorList.length > 0) {
		displayErrorsOnPage(errorList);

	} else {
		var data = {
			"empTypeId" : empTypeId,
			"vendorId" : vendorId,
			"locId" : locId,
			"cpdShiftId" : cpdShiftId,
			"contStaffSchFrom" : contStaffSchFrom,
			"contStaffSchTo" : contStaffSchTo,
			"contStaffName" : contStaffName,
			"contStaffIdNo" : contStaffIdNo,
		};

			var ajaxResponse = __doAjaxRequest('EmployeeAttendanceEntry.html'
					+ '?getEmpForAttendance', 'POST', data, false, 'html');
			$(divName).removeClass('ajaxloader');
			$(divName).html(ajaxResponse);
			
	}

}

$(document).ready(function() {
	$("#frmEmployeeSchedulingTbl").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true,
		"ordering" : false,
		"order" : [ [ 1, "desc" ] ]
	});
	/*$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
	});*/
	
	$(".fromDateClass").datepicker({		     	
        dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
        onSelect: function(selected) {
          $(".toDateClass").datepicker("option","minDate", selected)	
        }	
    });

	$(".toDateClass").datepicker({			      	
        dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
        onSelect: function(selected) {	
           $(".fromDateClass").datepicker("option","maxDate", selected)	
        }	
    }); 
	
	$(function() {
		$('.datetimepicker3').timepicker();
	});
	$(function() {
		$('.datetimepicker3').timepicker();

	});
	//runCalendar();
});
function confirmToSave(element) {debugger;
	
		return saveOrUpdateForm(element, "", 'EmployeeAttendanceEntry.html',
				'saveform');

}