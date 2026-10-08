/**
 * Author : Niraj.Kumar Created Date : 20 Aug, 2019
 */
$(document).ready(function() {
	
		prepareDateTag();
		$("#logBookDataTable").dataTable({
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
	
	$('#outDate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true
	});

	$("#outDate").keyup(function(e) {

		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});
	
	
	$('#inDate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true
	});

	$("#inDate").keyup(function(e) {

		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});
	
});	
	
	
/*	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		maxDate : '-0d',
		changeYear : true,
	});
*/
	$(".timepicker").timepicker({
		// s dateFormat: 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		minDate : '0',
	});

	$("#time").timepicker({

	});

	$("#frmVehicleLogBook").validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}
	});

//	prepareDateTag();


   function SearchLogBookDetails() {

		  var errorList = [];
		  var fromDate = $('#fromDate').val();
		  var toDate = $('#toDate').val();
		  var date = new Date();
		  var veNo =$('#veNo').val();

		
		     if(veNo==""){
		     if (fromDate == null || fromDate == "") {
				errorList.push("Please select From Date ");
			 }
			 if (toDate == null || toDate == "") {
				errorList.push("Please select To Date");
			 }
		     }
		 
	     	if (errorList.length > 0) {
		 	checkDate(errorList);
		    }
		    else {
			var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
			var eDate = new Date(fromDate.replace(pattern, '$3-$2-$1'));
			var sDate = new Date(toDate.replace(pattern, '$3-$2-$1'));

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
		    if (fromDate != '' || toDate != '' || veNo !='') {
			var requestData = '&fromDate=' + fromDate + '&toDate=' + toDate + '&veNo=' + veNo;
			var table = $('#logBookDataTable').DataTable();
			table.rows().remove().draw();
			$(".warning-div").hide();
			var response = __doAjaxRequest(
					'VehicleLogBook.html?SearchLogBook', 'POST',
					requestData, false, 'json');
			var veLogBoookDtos = response;
			if (veLogBoookDtos.length == 0) {
				errorList.push(getLocalMessage("record not found"));
				displayErrorsOnPage(errorList);
				return false;
			}
			var result = [];
			$
					.each(  veLogBoookDtos,
							function(index) {
						var obj = veLogBoookDtos[index];
						let veID = obj.veID
						var veNo = obj.veNo;
						var driverEmpID = obj.driverName;
						let outDate = getDateFormat(obj.outDate);
						let vehicleOutTime = obj.vehicleOutTime;
						let inDate = getDateFormat(obj.inDate);
						let vehicleInTime = obj.vehicleInTime;

						result
								.push([
										'<div class="text-center">' + (index + 1) + '</div>',
										'<div class="text-center">' + veNo + '</div>',
										'<div class="text-center">' + driverEmpID + '</div>',
										'<div class="text-center">' + outDate + '</div>',
										'<div class="text-center">' + vehicleOutTime + '</div>',
										'<div class="text-center">' + inDate + '</div>',
										'<div class="text-center">' + vehicleInTime + '</div>',
										'<div class="text-center">' 
										+ '<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5"  onclick="modifyLogBook(\''
										+ veID
										+ '\',\'VehicleLogBook.html\',\'viewVLB\',\'V\')" title="View"><i class="fa fa-eye"></i></button>'
										+ '<button type="button" class="btn btn-warning btn-sm "  onclick="modifyLogBook(\''
										+ veID
										+ '\',\'VehicleLogBook.html\',\'editVLB\',\'E\')"  title="Edit"><i class="fa fa-pencil"></i></button>'
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
	
	function modifyLogBook(veID, formUrl, actionParam, mode) {

		var divName = '.content-page';
		var requestData = {
			"mode" : mode,
			"id" : veID
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
	    var veNo = $("#veNo").val();
		if (veNo == null || veNo == "") {
	  		errorList.push(getLocalMessage("Please Enter vehicle Number"));
	  	}
		
	  	var driverName = $("#driverName").val();
	  	if (driverName == null || driverName == "") {
	  		errorList.push(getLocalMessage("Please Enter Driver Name"));
	  	}
	  	var outDate = $("#outDate").val();
	  	var inDate= $("#inDate").val();
		var vehicleOutTime = $("#vehicleOutTime").val();
	  	var vehicleInTime= $("#vehicleInTime").val();
	  	if (outDate==inDate && outDate!="" && inDate!=""){
	  		if (vehicleInTime < vehicleOutTime)
	  		errorList.push(getLocalMessage("Please Enter Valid Vehicle In Time"));
	  	}
	  	var vehicleJourneyFrom = $("#vehicleJourneyFrom").val();
	  	var dayStartMeterReading = $("#dayStartMeterReading").val();
	  	var dayEndMeterReading = $("#dayEndMeterReading").val();
	  	if(dayEndMeterReading != "" ){
			if (dayEndMeterReading < dayStartMeterReading) {
				errorList.push("Day End Meter Reading cannot be less than Day Start Meter Reading");
			}
			}

	  	var dayVisitDescription = $("#dayVisitDescription").val();
	  	var reason = $("#reason").val();
	
	  	
	  	if (outDate==null || outDate=="") {
	  		errorList.push(getLocalMessage("Please Enter Out Date"));
	  	}
	  	
	  	var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
		var outDate = new Date(outDate.replace(pattern, '$3-$2-$1'));
		var inDate = new Date(inDate.replace(pattern, '$3-$2-$1'));
	    var date = new Date();

		if (inDate <  outDate) {
			errorList.push("In Date cannot be less than Out Date");
		}
	

	  /*	if (veNo == null || veNo == "") {
	  		errorList.push(getLocalMessage("Please Enter vehicle Number"));
	  	}
	  	if (driverName == null || driverName == "") {
	  		errorList.push(getLocalMessage("Please Enter Driver Name"));
	  	}*/

	  	if (vehicleOutTime == null || vehicleOutTime == "") {
	  		errorList.push(getLocalMessage("Please Enter Out Time"));
	  	}
	  	if (vehicleJourneyFrom == null || vehicleJourneyFrom == "") {
	  		errorList.push(getLocalMessage("Please Enter Vehicle Journey From"));
	  	}
	  	if (dayStartMeterReading == null || dayStartMeterReading == "") {
	  		errorList.push(getLocalMessage("Please Enter Start Meter Reading"));
	  	}
	  	if (dayVisitDescription == null || dayVisitDescription == "") {
	  		errorList.push(getLocalMessage("Please Provide Some visit description"));
	  	}
	  	if (reason == null || reason == "") {
	  		errorList.push(getLocalMessage("Please Enter Reason"));
	  	}
	  	
	  	if (errorList.length > 0) {
			displayErrorsOnPage(errorList);
			checkDate(errorList);
			return false;
		} 
	  	else{  	 
		 return saveOrUpdateForm(element, "", 'VehicleLogBook.html', 'saveform');
	     }
	 	
		}



function openForm(formUrl, actionParam) {

	var divName = '.content-page';
	var requestData = {

	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', false);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);

}	



