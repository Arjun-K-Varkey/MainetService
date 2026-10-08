
$(document).ready(function(){
	
	 $("#showFlatNo").hide();
	 $("#serchBtnDirect").hide();
	yearLength();
});

function redirectToSearchPropNo(obj) {
	
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
					    'PropertyEditableForm.html?getBillingMethod',
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
				    	  $("#showSWMRadioButton").show();
				    	  $("#billingMethod").val('I');
				    }else{
				    	$("#serchBtnDirect").hide();
				    } 
			}else{
				displayErrorsOnPage(errorList);
			}
			showloader(false);
		},2000);
	}

function SearchButton(obj) {
	
	showloader(true);
	setTimeout(function () {
		var errorList = [];
		var billMethod = $("#billingMethod").val();
		var singleOrAllSWMFactor = $("input[name='singleOrAllSWMFactor']:checked").val();
		if(billMethod == 'I'){
		if(singleOrAllSWMFactor == null || singleOrAllSWMFactor == '' || singleOrAllSWMFactor == undefined){
			errorList.push(getLocalMessage('property.edit.swm.option'));
			displayErrorsOnPage(errorList);
			return false;
		}
			var flatNo = $("#flatNo").val();
			if (flatNo == "" || flatNo == undefined || flatNo == null) {
				errorList.push(getLocalMessage('prop.select.flatNo'));
			}
		}
			
		if (errorList.length == 0)
		{
			var theForm	=	'#propertyEditableForm';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'PropertyEditableForm.html?propertyEditForm';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			$(formDivName).html(returnData);
			
			reOrderPropertyEditUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
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

function searchSWMFactor(obj){
	var theForm = '#propertyEditableForm';
	var requestData = {};
	requestData = __serializeForm(theForm);
	var url = 'PropertyEditableForm.html?propertyEditSWMFactor';
	var returnData = __doAjaxRequest(url, 'POST', requestData, false, 'html');
	$(formDivName).html(returnData);
}

function redirectToSingleOrAllSWMFactor(){
	if ($("input[name='singleOrAllSWMFactor']:checked")
			.val() == "ALL") {
		$("#showFlatNo").hide();
		$("#serchBtnDirect").hide();
		$("#searchSWMFactorButton").show();
		$("#billingMethod").val('I');
	}else{
		$("#showFlatNo").show();
		$("#serchBtnDirect").show();
		$("#searchSWMFactorButton").hide();
		$("#billingMethod").val('I');
	}
}


function resetPropEditForm(){
	var data={};
	var URL='PropertyEditableForm.html?resetToSearch';
	var returnData=__doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
 }
 
function saveSWMFactor(element) {
	var errorList = [];
	$.fancybox.close();

	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"PropertyEditableForm.html?saveSWMFactor", 'POST',
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
			showBoxForApproval(getLocalMessage("property.edit.add.swm"));
		}
	}
} 

function yearLength(){	
	var dateFields = $('.dateClass');
    dateFields.each(function () {
    	
            var fieldValue = $(this).val();
            if (fieldValue.length > 10) {
                    $(this).val(fieldValue.substr(0, 10));
            }
    });
}
function savePropertyEditForm(element) {
	var errorList = [];
	$.fancybox.close();
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"PropertyEditableForm.html?saveEditForm", 'POST',
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
		showBoxForApproval(getLocalMessage("property.edit.success"));
	    }
	}
}

	function showBoxForApproval(succesMessage) {

	    var childDivName = '.msg-dialog-box';
	    var message = '';
	    var Proceed = getLocalMessage("proceed");
	    var no = 'No';
	    message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
		    + '</p>';
	  
		message += '<div class=\'text-center padding-bottom-10\'>'
			+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''
			+ Proceed + '\'  id=\'Proceed\' '
			+ ' onclick="closeApproval();"/>' + '</div>';
	    

	    $(childDivName).addClass('ok-msg').removeClass('warn-msg');
	    $(childDivName).html(message);
	    $(childDivName).show();
	    $('#Proceed').focus();
	    showModalBoxWithoutClose(childDivName);
	}

	

function showAddInfoEdit(factorCode,factRadioVal){
	
	if($('input[name="provisionalAssesmentMstDto.proAssfactor['+factRadioVal+']"]:checked').val()=="Y"){
		
		var data = {"factorCode" : factorCode};		
		var URL = 'PropertyEditableForm.html?getFactorValueDiv';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html(returnData);
		
		$("#"+factorCode).show();
		reorderfactor('.specFact','.eachFact');
		var a=0;
		var tableLength = $('#unitDetailTable tr').length;
		for(var j=1;j<tableLength;j++) // table tr length 
		{
			var unitNoFact=$("#unitNo"+a).val();
			var unitSelectId=".factQuestion"+factRadioVal;
			if($(""+unitSelectId+" option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined){
				$(""+unitSelectId+"").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
			}	
			a++;
		}
	}else{	 
		var data = {"factorCode" : factorCode};
		var URL = 'SelfAssessmentForm.html?deleteFactorStatus';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html('');
		reorderfactor('.specFact','.eachFact');
	}
}

//reorder id and path after click on checkbox  - change in assessment service
function reOrderPropertyEditUnitTabIdSequence(classNameFirst,classNamesecond){	
	$(classNameFirst).each(function(i) {
	var incr=i+1;
	$(this).find("select:eq(0)").attr("id", "year"+i);
	$(this).find("input:hidden:eq(0)").attr("id", "hiddenYear"+i);
	$(this).find("input:hidden:eq(1)").attr("id", "hiddenFirstAssesmentDate"+i);
	
	$(this).find("input:text:eq(0)").attr("id", "unitNo"+i);	
	$(this).find("select:eq(1)").attr("id", "assdFloorNo"+i);
	
	$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
	$(this).find("select:eq(2)").attr("id", "assdOccupancyType"+i);
	$(this).find("input:text:eq(2)").attr("id", "occupierName"+i);
	$(this).find("select:eq(3)").attr("id", "legal"+i);
	$(this).find("select:eq(4)").attr("id", "assdConstruType"+i);
	$(this).find("input:text:eq(3)").attr("id", "actualRent"+i);	
	$(this).find("input:text:eq(4)").attr("id", "carpetArea"+i);
	$(this).find("input:text:eq(5)").attr("id", "taxableArea"+i);
	$(this).find("input:text:eq(6)").attr("id", "assdRv"+i);
	
	$(this).find("a:eq(0)").attr("data-target", "#group-of-rows-"+i);
	$(this).find("a:eq(0)").attr("aria-controls", "#group-of-rows-"+i);
	$(this).find("a:eq(1)").attr("id", "deleteRow_"+i);
	var utp=i;
	if(i>0){
	utp=i*6;
	}
	$(this).find("select:eq(5)").attr("id", "assdUsagetype"+utp);
	$(this).find("select:eq(6)").attr("id", "assdUsagetype"+(utp+1));
	$(this).find("select:eq(7)").attr("id", "assdUsagetype"+(utp+2));
	$(this).find("select:eq(8)").attr("id", "assdUsagetype"+(utp+3));
	$(this).find("select:eq(9)").attr("id", "assdUsagetype"+(utp+4));
	
	$("#unitNo"+i).val(incr);
	$(this).find("select:eq(0)").attr("name","");
	$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitNo");
	$(this).find("input:hidden:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].faYearId");
	
	$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdFloorNo");
	$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
	$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdOccupancyType");
	$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].occupierName");
	$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].legal");
	$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdConstruType");
	$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");
	$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
	$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
	$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdRv");
	
	$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype1");
	$(this).find("select:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype2");
	$(this).find("select:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype3");
	$(this).find("select:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype4");
	$(this).find("select:eq(9)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype5");

	$(this).find('.delete').attr("id", "delete_" + i);
	$(this).find(".delete").attr("onclick", "removeRowCIA("+(i)+")");
	$(this).find('.duppliquer').attr("id","duppliquer"+i);
	$(this).find('.duppliquer').attr("onclick", "duppliquerDet("+(i)+")");
	$(this).find('.addRoomClass').attr("id", "addRoom" + i);
	$(this).find(".addRoomClass").attr("onclick", "addRoomDetails("+(i)+")");
	
	if(i>0){
		var a=i-1;
		$("#hiddenYear"+i).val($("#hiddenYear"+a).val());
		$("#year"+i).val($("#hiddenYear"+a).val());
	}
	
	var carpetArea = $("#carpetArea"+i).val();
	carpetArea = parseFloat(carpetArea).toFixed(2);
	if('NaN' != carpetArea)
	$("#carpetArea"+i).val(carpetArea);
	var buildupArea = $("#taxableArea"+i).val();
	buildupArea = parseFloat(buildupArea).toFixed(2);
	if('NaN' != buildupArea)
	$("#taxableArea"+i).val(buildupArea);
	
	var unitNoFact=$("#unitNo"+i).val();

	if($(".selectUnit option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined && unitNoFact==null){
	
		$(".selectUnit").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
	}
	
	});
	
}




function saveRebate(element) {
    var errorList = [];
    if (errorList.length > 0) {
	displayErrorsOnPage(errorList);
    }

    else {
	$.fancybox.close();
	var divName = '.content-page';

	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"ProvideRebateForMissingProperty.html?saveRebateReceiptForMissingProperties", 'POST',
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

	    if (object.successFlag = "Y") {
		showBoxForApproval(getLocalMessage("Receipt inserted successfully"));
	    }
	}

    }

}


function showBoxForApproval(succesMessage) {

    var childDivName = '.msg-dialog-box';
    var message = '';
    var Proceed = getLocalMessage("proceed");
    var no = 'No';
    message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
	    + '</p>';
	message += '<div class=\'text-center padding-bottom-10\'>'
		+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''
		+ Proceed + '\'  id=\'Proceed\' '
		+ ' onclick="closeApproval();"/>' + '</div>';

    $(childDivName).addClass('ok-msg').removeClass('warn-msg');
    $(childDivName).html(message);
    $(childDivName).show();
    $('#Proceed').focus();
    showModalBoxWithoutClose(childDivName);
}

function closeApproval() {
    $("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', 'ProvideRebateForMissingProperty.html');
	$("#postMethodForm").submit();
}