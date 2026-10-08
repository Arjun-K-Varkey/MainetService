$(document).ready(function() {
	prepareDateTag();
	$("#petrolReqFormDataTable").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	
	
	$('#fromDate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true
	});
	$("#fromDate").keyup(function(e) {

		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

	$('#toDate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true
	});

	$("#toDate").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});	
	
	$('#date').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true
	});
	$("#date").keyup(function(e) {

		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});
	
});


function searchPetrolRequest() {
      var errorList = [];
	  var fromDate = $('#fromDate').val();
	  var toDate = $('#toDate').val();
	  var date = new Date();
	  var department = $('#department').val();
	  var veNo =$('#veNo').val();

	  if(fromDate=="" && toDate=="" && veNo=="" && department=="" ){
		  errorList.push("Please select Any Field");
	  }
		    	 
   	if (errorList.length > 0) {
	 	checkDate(errorList);
	    }
	    else {
		var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
		var eDate = new Date(fromDate.replace(pattern, '$3-$2-$1'));
		var sDate = new Date(toDate.replace(pattern, '$3-$2-$1'));

		if (eDate > date) {
			errorList
					.push("From date cannot be greater than Current Date");
		}
		
		if (eDate > sDate) {
			errorList
					.push("To Date cannot be less than From Date");
		}
		if (sDate >= date) {
			errorList
					.push("To Date cannot be greater than Current Date");
		}

		if (errorList.length > 0) {
			checkDate(errorList);
		}
	    }
	    if(errorList.length ==0){
	    if (fromDate != '' || toDate != '' || department !='' || veNo !='') {
	    	
		var requestData = '&fromDate=' + fromDate + '&toDate=' + toDate + '&department=' + department + '&veNo=' + veNo;
		var table = $('#petrolReqFormDataTable').DataTable();
		table.rows().remove().draw();
		$(".warning-div").hide();
		var response = __doAjaxRequest(
				'petrolRequisitionForm.html?SearchPetrol', 'POST',
				requestData, false, 'json');
		var petrolRequisitionDTOs = response;
		if (petrolRequisitionDTOs.length == 0) {
			errorList.push(getLocalMessage("record not found"));
			displayErrorsOnPage(errorList);
			return false;
		}
		var result = [];
		$
				.each(  petrolRequisitionDTOs,
						function(index) {
					var obj = petrolRequisitionDTOs[index];
					let requestId = obj.requestId
					let date = getDateFormat(obj.date);
					let department = obj.deptDesc;
					var veNo = obj.veNo;
					var driverName = obj.driverDesc;
					let requestStatus = obj.requestStatus;
			
					result
							.push([
									'<div class="text-center">' + (index + 1) + '</div>',
									'<div class="text-center">' + date + '</div>',
									'<div class="text-center">' + department + '</div>',
									'<div class="text-center">' + veNo + '</div>',
									'<div class="text-center">' + driverName + '</div>',
									'<div class="text-center">' + requestStatus + '</div>',
									
									'<div class="text-center">' 
									+ '<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5"  onclick="modifyPetrolRequest(\''
									+ requestId
									+ '\',\'petrolRequisitionForm.html\',\'viewPETROL\',\'V\')" title="View"><i class="fa fa-eye"></i></button>'
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

function modifyPetrolRequest(requestId, formUrl, actionParam, mode) {

	var divName = '.content-page';
	var requestData = {
		"mode" : mode,
		"id" : requestId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData, 'html', false);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);

 }

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


function confirmToProceed(element) {
	var errorList = [];
	var date = new Date();
	var date1 = $("#date").val();
	var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
	var eDate = new Date(date1.replace(pattern, '$3-$2-$1'));
	if (eDate > date) {
		errorList
				.push("Date cannot be greater than Current Date");
	}
	var time = $("#time").val();
	var department = $("#department").val();
	var vehicleType = $("#vehicleType").val();
	var veNo = $("#veid").val();
    var fuelType = $("#fuelType").val();
    var vehicleMeterRead =$("#vehicleMeterRead").val();
	var fuelQuantity = $("#fuelQuant").val();
	var fuelQuantityUnit = $("#fuelQuantityUnit").val();
	var driverName = $("#driverName").val();
	
	if (date1 == null || date1 == "") {
		errorList.push(getLocalMessage("Please Enter Date"));
	}
	if (time == null || time == "") {
		errorList.push(getLocalMessage("Please Enter time"));
	}
	if (department == null || department == "") {
		errorList.push(getLocalMessage("Please Select Any Department"));
	}
	if (vehicleType == null || vehicleType == "0") {
		errorList.push(getLocalMessage("Please Select Any Vehicle Type"));
	}
	if (veNo == null || veNo == "") {
		errorList.push(getLocalMessage("Please Select Any Vehice Number"));
	}
	if (fuelType == null || fuelType == "0") {
		errorList.push(getLocalMessage("Please Select Any Fuel Type"));
	}
	
	if (vehicleMeterRead == null || vehicleMeterRead == "") {
		errorList.push(getLocalMessage("Please Enter Vehicle Current KM"));
	}
	
	if (fuelQuantity == null || fuelQuantity == "") {
		errorList.push(getLocalMessage("Please Enter Fuel Quantity"));
	}
	if (fuelQuantityUnit == null || fuelQuantityUnit == "0") {
		errorList.push(getLocalMessage("Please Select fuel Quantity Unit"));
	}
	
	if (driverName == null || driverName == "") {
		errorList.push(getLocalMessage("Please Select Driver Name"));
	}

	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	} else {
		var requestData = {
			"veNo" : veNo
		};
		var ajaxResponse = doAjaxLoading('petrolRequisitionForm.html?save',
				requestData, 'json');
		if (!ajaxResponse) {
			errorList
					.push(getLocalMessage("Fuel Request for slected vehicle number is In Progress Please select another Vehicle Number"));
		}
		if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
			return false;
		} else {
			return saveOrUpdateForm(element, "", 'petrolRequisitionForm.html',
					'saveform');
		}
	}
}




$("#frmPetrolRequestForm").validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
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


 function getVehicleNos() {
	var deptId = $("#deptId").find("option:selected").val();
	var vehicleType =$('#vehicleType').find("option:selected").val();
	$('#veNo').html('');
	$('#veNo').append($("<option></option>").attr("value","").text(getLocalMessage('selectdropdown'))).trigger('chosen:updated');
	
	if(deptId!="" &&  deptId!=0 && vehicleType!="" && vehicleType!=0){
		var requestData = {
			    "deptId" : $("#deptId").val(),
				"vehicleType" : $("#veVetype").val()
			}
		var meetingNoList = __doAjaxRequest(petrolRequestForm.html+'?getMeetingNo', 'POST', requestData, false,'json');
		$.each(meetingNoList, function(index, value) {
			 $('#veNo').append($("<option></option>").attr("value",value.veId).attr("code",value.veNo).text(value.veNo));
		});
		 $('#veNo').trigger('chosen:updated');	
	   }
	
	
}

 function showVehicleRegNo() {
		
	    $('#veid').html('');
		var crtElementId = $("#vehicleType").val();
		var deptId = $("#department").val();
		var requestUrl = "petrolRequisitionForm.html?getVehicleNo";
		var requestData = {
			"id" : crtElementId,
			"deptId" : deptId
		};
		var ajaxResponse = __doAjaxRequest(requestUrl, 'post', requestData, false,'json');
		 $('#veid').append($("<option></option>").attr("value",0).attr("code",0).text("select"));
		 $.each(ajaxResponse, function(index, value) {
		$('#veid').append($("<option></option>").attr("value",index).attr("code",index).text(value));
		});
		$('#veid').trigger('chosen:updated');	

}


 function showFuelType() {
		//fuel type
	    $('#fuelType').html('');
		var veid = $("#veid").val();
		var requestUrl = "petrolRequisitionForm.html?getFuelType";
		var requestData = {
			"id" : veid
		};
		var ajaxResponse = __doAjaxRequest(requestUrl, 'post', requestData, false,'json');
		 $('#fuelType').append($("<option></option>").attr("value",0).attr("code",0).text("select"));
		 $.each(ajaxResponse, function(index, value) {
		$('#fuelType').append($("<option></option>").attr("value",index).attr("code",index).text(value));
		});
		$('#fuelType').trigger('chosen:updated');	

}

function getVehicleTypeByDept() {
		
		$('#fuelType').html('');
	    $('#vehicleType').html('');
	    $('#veid').html('');
	    $('#fuelType').html('');
		var department = $("#department").val();
		var requestUrl = "petrolRequisitionForm.html?getDepartmentType";
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
 
function searchVeNo() {
    var requestData = {
	"veid" : $("#veNo").val()
    };
    
    $('#veNo').html('');
    $('#veNo').append(
	    $("<option></option>").attr("value", "0").text(
		    getLocalMessage('selectdropdown')));
    
    var ajaxResponse = doAjaxLoading(
	    'petrolRequisitionForm.html?searchVeNo', requestData,
	    'json');
   
	var veNodata='<option value="'+ajaxResponse.veId+'">'+ajaxResponse.veNo+'</option>'
	$("#veNo").html(veNodata);
    
}

function getVehicleNoByDept() {
	
	var department = $("#department").val();
	var requestUrl = "petrolRequisitionForm.html?getVehicleNoByDept";
	var requestData = {
		"department" : department
	};
	
	$('#veNo').html('');
	$('#veNo').append($("<option></option>").attr("value","0").text(getLocalMessage('selectdropdown')));
	
	var ajaxResponse = __doAjaxRequest(requestUrl, 'post', requestData, false,'json');
	
	$.each(ajaxResponse, function(index, value) {
		$('#veNo').append($("<option></option>").attr("value",index).attr("code",index).text(value));
	});
	$('#veNo').trigger('chosen:updated');	

}

 
