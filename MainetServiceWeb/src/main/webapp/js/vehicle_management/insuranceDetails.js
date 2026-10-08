$(document).ready(function() {
	
	prepareDateTag();
	$('#frmVehicleLogBook').validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}
	});
	$("#InsuranceDetailTable").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true,
		"ordering":  false,
	    "order": [[ 1, "desc" ]]
	});

	$(function() {
		$('.datetimepicker3').timepicker();
		
	});
	$(".fromDateClass").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		onSelect : function(selected) {
			$(".toDateClass").datepicker("option", "minDate", selected)
		}
	});
	$(".toDateClass").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		onSelect : function(selected) {
			$(".fromDateClass").datepicker("option", "maxDate", selected)
		}
	});

	
	
	

	function openForm(formUrl, actionParam) {

		var divName = '.content-page';
		var requestData = {

		};
		var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
				'html', false);
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);

	}	
	
	
});
function confirmToProceed(element) {
 
	var errorList = [];
	var date = new Date();
	var issueDate = $("#issueDate").val();
	var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
	var eDate = new Date(issueDate.replace(pattern, '$3-$2-$1'));
	if (eDate > date) {
		errorList
				.push("Insurance Issue Date cannot be greater than Current Date");
	}
	var department = $("#department").val();
	var vehicleType = $("#vehicleType").val();
	var veNo = $("#veid").val();
	var vePurSource = $("#vePurSource").val();
	var issueDate = $("#issueDate").val();
	var endDate = $("#endDate").val();
	var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
	var iDate = new Date(issueDate.replace(pattern, '$3-$2-$1'));
	var enDate = new Date(endDate.replace(pattern, '$3-$2-$1'));
	if (iDate > enDate) {
		errorList
				.push("Insurance Issue Date cannot be greater than Insurance End Date");
	}
	var insuredAmount = $("#insuredAmount").val();
	var insuredFees = $("#insuredFees").val();
	
	if (department == null || department == "") {
		errorList.push(getLocalMessage("Please Select Any Department"));
	}
	if (vehicleType == null || vehicleType == "0") {
		errorList.push(getLocalMessage("Please Select Any Vehicle Type"));
	}
	if (veNo == null || veNo == "0" || veNo == "") {
		errorList.push(getLocalMessage("Please Select Any Vehice Number"));
	}
	if (vePurSource == null || vePurSource == "" || vePurSource == "0") {
		errorList.push(getLocalMessage("Please Select Any Insured By"));
	}
	
	if (issueDate == null || issueDate == "") {
		errorList.push(getLocalMessage("Please Enter Insurance Issue Date"));
	
	
	if (endDate == null || endDate == "") 
		errorList.push(getLocalMessage("Please Enter Insurance End Date"));
	
	}
    if (insuredAmount == null || insuredAmount == "") {
		errorList.push(getLocalMessage("Please Enter Insured Amount"));
	}
	if (insuredAmount != null && insuredAmount != "" && insuredAmount == "0") {
		errorList
				.push(getLocalMessage("Insured Amount should be greater than 0"));
	}
	/*if (insuredFees == null || insuredFees == "") {
		errorList.push(getLocalMessage("Please Enter Insured Fees"));
	}*/

	if (insuredFees != null && insuredFees != "" && insuredFees == "0") {
		errorList
				.push(getLocalMessage("Insured Fees should be greater than 0"));
	}
	if (issueDate != "" && veNo!="0") {
		var reqData = {
			"veNo" : veNo,
			"issueDate" : issueDate
		}
		var URL = 'insuranceDetails.html?checkInsuranceDate';
		var retData = __doAjaxRequest(URL, 'POST', reqData, false, 'json');
		if (retData==false) {
			errorList
					.push(getLocalMessage("Please Enter Insurance Issue Date greater than Vehicle Purchase Date "));
		}
	}
	var requestData = {
			   "veNo"   : veNo,
			"issueDate" : issueDate,
			 "endDate"  : endDate
		}
	var URL = 'insuranceDetails.html?recordAlreadyExists';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'json');

	if(returnData) {
		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
			return false;
		} else {
			return saveOrUpdateForm(element, "", 'insuranceDetails.html', 'saveform');  
		}
	}
	else {
		errorList.push(getLocalMessage("Insurance already exists for selected vehicle"))
		displayErrorsOnPage(errorList);
	}
	
}

     function searchInsuranceDetails() {
	  
      var errorList = [];
	  var department = $('#department').val();
	  var vehicleType =$("#vehicleType").val();
	  var veid =$('#veid').val();
	 
	     if(department=="" || vehicleType=="0" || veid=="" || veid=="0"){
	              errorList.push("Please Select All Mandatory Fields");
	     }
	 

	    if(errorList.length ==0){
	    if (department != '' || vehicleType != '0' || veNo !='') {
	    	
		var requestData = '&department=' + department + '&veid=' + veid + '&vehicleType=' + vehicleType;
		var table = $('#InsuranceDetailTable').DataTable();
		table.rows().remove().draw();
		$(".warning-div").hide();
		var response = __doAjaxRequest('insuranceDetails.html?SearchInsuranceDetails', 'POST',requestData, false, 'json');
		var insuranceDetailsDTOs = response;
		if (insuranceDetailsDTOs.length == 0) {
			errorList.push(getLocalMessage("record not found"));
			displayErrorsOnPage(errorList);
			return false;
		}
		var result = [];
		$  
				.each(  insuranceDetailsDTOs,
						function(index) {
					    var obj = insuranceDetailsDTOs[index];
				    	let insuranceDetId = obj.insuranceDetId
				    	let department =obj.deptDesc;
				    	let vehicleType = obj.veDesc;
				    	var veid = obj.veNo;
				    	var insuredBy = obj.insuredName;
				    	let issueDate = getDateFormat(obj.issueDate);
				    	
					result
							.push([
									'<div class="text-center">' + (index + 1) + '</div>',
									'<div class="text-center">' + department + '</div>',
									'<div class="text-center">' + vehicleType + '</div>',
									'<div class="text-center">' + veid + '</div>',
									'<div class="text-center">' + insuredBy + '</div>',
									'<div class="text-center">' + issueDate + '</div>',
									
									'<div class="text-center">' 
									+ '<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5"  onclick="modifyInsuranceRequest(\''
									+ insuranceDetId
									+ '\',\'insuranceDetails.html\',\'viewInsuranceForm\',\'V\')" title="View"><i class="fa fa-eye"></i></button>'
									+ '<button type="button" class="btn btn-warning btn-sm "  onclick="modifyInsuranceRequest(\''
									+ insuranceDetId
									+ '\',\'insuranceDetails.html\',\'editInsuranceForm\',\'E\')"  title="Edit"><i class="fa fa-pencil"></i></button>'
									+ '</div>' ]);

				});
table.rows.add(result);
table.draw();
}
	    } 
	    
else {
//	errorList.push(getLocalMessage("Select At least One Criteria"));
	displayErrorsOnPage(errorList);
}
}


function modifyInsuranceRequest(insuranceDetId, formUrl, actionParam, mode) {
    
	var divName = '.content-page';
	var requestData = {
		"mode" : mode,
		"id" : insuranceDetId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData, 'html', false);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);

 }


function getVehicleTypeByDept() {
	
	$('#fuelType').html('');
    $('#vehicleType').html('');
    $('#veid').html('');
    $('#fuelType').html('');
	var department = $("#department").val();
	var requestUrl = "insuranceDetails.html?getDepartmentType";
	var requestData = {
		"id" : department
	};
	var ajaxResponse = __doAjaxRequest(requestUrl, 'post', requestData, false,'json');
	 $('#vehicleType').append($("<option></option>").attr("value",0).attr("code",0).text("select"));
	 $.each(ajaxResponse, function(index, value) {
	$('#vehicleType').append($("<option></option>").attr("value",index).attr("code",index).text(value));
	});
	$('#vehicleType').trigger('chosen:updated');	

}

function showVehicleRegNo(obj, param) {

	$('#veid').html('');
	var crtElementId = $("#vehicleType").val();
	var mode = param;
	var requestUrl = "insuranceDetails.html?getVehicleNo";
	var requestData = {
		"id" : crtElementId,
		"mode" : mode
	};
	var ajaxResponse = __doAjaxRequest(requestUrl, 'post', requestData, false,
			'json');
	$('#veid').append(
			$("<option></option>").attr("value", 0).attr("code", 0).text(
					"select"));
	$.each(ajaxResponse, function(index, value) {
		$('#veid').append(
				$("<option></option>").attr("value", index).attr("code", index)
						.text(value));
	});
	$('#veid').trigger('chosen:updated');

}

