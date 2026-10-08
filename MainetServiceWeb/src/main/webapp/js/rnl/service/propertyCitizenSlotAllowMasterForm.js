$(document).ready(function(){

	$("#propCitizenSlotAllowTableId").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});

	$('.lessthancurrdateto').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		/*yearRange: "-100:-0",*/
		numberOfMonths: 1,
		minDate: 0,
		maxDate: '+6m',
		onSelect: checkBookedDate

	});

	$('.lessthancurrdatefrom').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		/*yearRange: "-100:-0",*/
		numberOfMonths: 1,
		minDate: 0,
		maxDate: '+6m',
		onSelect: function(selected) {
			$(".lessthancurrdateto").datepicker("option","minDate", selected)	
			checkBookedDate();		
		} 
	});
	
	datePickerLength();
});

function formForCreate() {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading('PropertyCitizenSlotAllowanceController.html?formForCreate',
			{}, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse)
	$('.error-div').hide();
}

function validatePropertyCitizenAllowanceData(errorList) {

	var estateId = $("#estateId").val();
	var propertyId = $("#propertyId").val();
	var fromDate = $("#fromDate").val();
	var toDate = $("#toDate").val();
	var shiftId = $("#shiftId").val();
	var citizenAllowanceStatusId = $("#citizenAllowanceStatusId").val();
	var remark = $("#remark").val();

	if(estateId == undefined || estateId == null || estateId == "0")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.estateId"));

	if(propertyId == undefined || propertyId == null || propertyId == "0")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.propertyId"));

	if(fromDate == undefined || fromDate == null || fromDate == "")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.fromDate"));

	if(toDate == undefined || toDate == null || toDate == "")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.toDate"));

	if(shiftId == undefined || shiftId == null || shiftId == "0")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.shiftId"));

	if(citizenAllowanceStatusId == undefined || citizenAllowanceStatusId == null || citizenAllowanceStatusId == "0")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.citizenAllowanceStatusId"));

	if(remark == undefined || remark == null || remark == "")
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.remark"));

	return errorList;

}

function modifyCase(pcSlotAllowId, formUrl, actionParam, mode) {
	var divName = '.content-page';
	var requestData = {
			"mode" : mode,
			"pcSlotAllowId" : pcSlotAllowId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function savePropSlotAllowMastForm(obj){

	var errorList = [];
	errorList = validatePropertyCitizenAllowanceData(errorList);
	errorList = bookingSlotCheck(errorList);

	if (errorList.length == 0) {
		return saveOrUpdateForm(obj, "Property Citizen Slot Allowance Information Saved Successfully!", 'PropertyCitizenSlotAllowanceController.html', 'saveform');
	} else {
		displayErrorsOnPage(errorList);
	}
}

function bookingSlotCheck(errorList){

	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var shiftId = $('#shiftId').val();

	if(fromDate !="" && toDate!="" && shiftId!="0"){
		var fromDateArr = fromDate.split('/');
		var ToDateArr = toDate.split('/');

		var fromDay = new Date(fromDateArr[2],fromDateArr[1]-1,fromDateArr[0]).getDay();
		var toDay = new Date(ToDateArr[2],ToDateArr[1]-1,ToDateArr[0]).getDay();
		var shiftCode = $("#shiftId").find("option:selected").attr('code');

		if(!((fromDay==6 || fromDay==0) && (toDay == 6 || toDay==0)))
			errorList.push(getLocalMessage('rnl.property.citizen.slot.booking.sat.sun.open.allowed.validation1'));
		else
		{
			if(shiftCode != 'SHIFT2' && shiftCode != 'SHIFT3'){
				$('#shiftId').val('0');
				errorList.push(getLocalMessage('rnl.property.citizen.slot.booking.sat.sun.open.allowed.validation2'));
			}
			else{
				var reqData = {
						"fromDate" : fromDate,
						"toDate" : toDate,
						"shiftId" : shiftId
				};

				var responseData =__doAjaxRequest("EstateBooking.html?checkPropertyBookings",'POST',reqData,false,'json'); 
				if(responseData == 'Y')
					errorList.push(getLocalMessage('rnl.property.booking.already.exist'));
			}
		}

	}

	return errorList;
}

function checkBookedDate(toDate){

	var fromDate =  $("#fromDate").val();
	var toDate =  $("#toDate").val();	
	if(toDate!='')
	{
		if(fromDate==null || fromDate==''){
			showAlertBoxForFfromdate();	
		}		
	}
}

function showAlertBoxForFfromdate(){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = 'Ok';

	message	+='<h4 class=\"text-center text-blue-2 padding-10\">Please select From Date first</h4>';
	message	+='<div class=\'text-center padding-bottom-10\'>'+	
	'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '+ 
	' onclick="closeAlertForm()"/>'+
	'</div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg'); 
	$(errMsgDiv).html(message);
	$('#toDate').val("");
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBox(errMsgDiv);
}

function closeAlertForm()
{
	var childDivName	=	'.child-popup-dialog';
	$(childDivName).hide();
	$(childDivName).empty();
	disposeModalBox();
	$.fancybox.close();
}

function getPropertyList(){

	var estateId = $("#estateId").val();

	var requestData = {"estateId": estateId};
	var result = __doAjaxRequest("PropertyCitizenSlotAllowanceController.html?propList",'post',requestData,false,'json');
	$('#propertyId').html('');
	$('#propertyId').append($("<option></option>").attr("value","0").text('Select'));
	$.each(result, function(index, value) {
		$('#propertyId').append($("<option></option>").attr("value",value[0]).text(value[1]));
	});

}

function datePickerLength(){
	
	$(".lessthancurrdateto").each(function(){
		var fieldValue = $(this).val();
		if(fieldValue.length > 10){
			$(this).val(fieldValue.substr(0,10));
		}
	});
	
	$(".lessthancurrdatefrom").each(function(){
		var fieldValue = $(this).val();
		if(fieldValue.length > 10){
			$(this).val(fieldValue.substr(0,10));
		}
	});
	
}

function searchCitizenBookingOpenSlot(obj) {
	var errorList = [];
	var divName = '.content-page';
	var propertyId = $("#propertyId").val();

	if(propertyId == undefined || propertyId == null || propertyId == '0')
		errorList.push(getLocalMessage("rnl.property.citizen.allow.validation.propertyId"));

	if (errorList.length == 0) {
		
		var requestData = {
				"propertyId" : propertyId	
		};
		var ajaxResponse = doAjaxLoading('PropertyCitizenSlotAllowanceController.html?searchCitizenBookingOpenSlot', requestData, 'html');
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
		prepareTags();
		
	} else {
		displayErrorsOnPage(errorList);
	}
}