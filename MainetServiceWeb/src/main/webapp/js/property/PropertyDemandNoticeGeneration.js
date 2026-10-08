$(document)
		.ready(
				function() {
					
					var billMethod = $("#billingMethod").val();
					if(billMethod == 'I'){
						$("#showFlatNo").show();
					}else{
						$("#showFlatNo").hide();
					}
						showSingleMultiple();
						
						
						
						$('#datatables').DataTable({
				    		"oLanguage": { "sSearch": "" } ,
				    		"aLengthMenu": [ [5, 10, 15, -1], [5, 10, 15, "All"] ],
				    	    "iDisplayLength" : 5, 
				    	    "bInfo" : true,
				    	    "lengthChange": true,
				    	    "bPaginate": true,
				    	    "bFilter": true
				    	    });
						
						$('#selectall').click(function(event) {
							if (this.checked) {
								$('.checkall').each(function() {
									this.checked = true;
								});
							} else {
								$('.checkall').each(function() {
									this.checked = false;

								});
							}
						});
				});




function serachProperty() {
	showloader(true);
		/*  $('#datatables').DataTable().destroy();
	
		var requestData = $('#propertyBillGeneration').serialize();*/
		
	var formAction	=	$('#propertyDemandNotice').attr('action');
		var url=formAction+'?SearchPropNo';
		$('#propertyDemandNotice').attr('action', url);

		$('#propertyDemandNotice').submit();
		
		/*
		var ajaxResponse = __doAjaxRequest(
				formAction+'?SearchPropNo', 'POST',
				requestData, false, 'html');

		$(".content").html(ajaxResponse);
		return false;*/
}

function generateNotice(element) {

    $("#errorDiv").hide();
    var errorList = [];

    var billMethod = $("#billingMethod").val();
    var flatNo = $("#flatNo").val();
    var propNo = $("#proertyNo").val();
    var noticeTypeId = $("#noticeType").val();
    var noticeType = $("#noticeType option:selected").attr("code");
    
    if (!noticeTypeId || noticeTypeId === '0') {
        errorList.push(getLocalMessage('Please select notice type'));
    }

    // ================= JW VALIDATION =================
    if (noticeType === 'JW') {

        var finYearId = $("#finYearId").val();
        if (!finYearId || finYearId === '0') {
            errorList.push(getLocalMessage('Please select financial year'));
        }

        var assWard1 = $("#assWard1").val();
        var assWard2 = $("#assWard2").val();
        
        var isPropertyMode = !!propNo;
        var isWardMode = !!assWard1;

        // At least one mode must be selected
        if (!isPropertyMode && !isWardMode) {
            errorList.push(getLocalMessage('Enter property number OR select ward and zone'));
        }

        // If property mode → validate flat only if bill method Individual
        if (isPropertyMode) {
            if (billMethod === 'I' && !flatNo) {
                errorList.push(getLocalMessage('prop.select.flatNo'));
            }
        }

        // If ward mode → no need to validate property/flat
        if (isWardMode) {
            if (!assWard1) {
                errorList.push(getLocalMessage('Please select ward'));
            }
            if (!assWard2 || assWard2 === "0") {
                errorList.push(getLocalMessage('Please select zone'));
            }
        }

    } else {

        // ============ SM / AL VALIDATION (Skip if JW) ============
        var searchType = $("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val();

        if (searchType === "SM") {

            var propNo = $("#proertyNo").val();
            if (!propNo) {
                errorList.push(getLocalMessage('Please enter property number'));
            }
            if (billMethod === 'I' && !flatNo) {
                errorList.push(getLocalMessage('prop.select.flatNo'));
            }

        } else if (searchType === "AL") {

            var assWard1 = $("#assWard1").val();
            var assWard2 = $("#assWard2").val();

            if (!assWard1) {
                errorList.push(getLocalMessage('Please select ward'));
            }

            if (!assWard2 || assWard2 == "0") {
                errorList.push(getLocalMessage('Please select zone'));
            }
        }
    }

    // ================= FINAL SUBMIT =================
    if (errorList.length === 0) {

        $(element).prop('disabled', true);

        var data = $('#propertyDemandNotice').serialize();
        var URL = 'PropertyDemandNoticeGeneration.html?saveDemandNotice';

        var returnData = __doAjaxRequestForSave(URL, 'POST', data, false);

        popUpMsg(returnData);

    } else {

        displayErrorsOnPage(errorList);
    }
}

function popUpMsg(returnData) {
	successUrl = 'PropertyDemandNoticeGeneration.html';
	if ($.isPlainObject(returnData)) {
		var message = returnData.command.message;
		var hasError = returnData.command.hasValidationError;
		if (!message) {
			message = successMessage;
		}
		if (message && !hasError) {
			if (returnData.command.hiddenOtherVal == 'SERVERERROR')
				showSaveResultBox(returnData, message, 'AdminHome.html');
			else
				showSaveResultBox(returnData, message, successUrl);
		} else if (hasError) {
			$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');
		} else
			return returnData;
	} else if (typeof (returnData) === "string") {
		$('.content-page').html(returnData);
		prepareTags();
	} else {
		alert("Invalid datatype received : " + returnData);
	}
	return false;
}

function resetSearchForm(element) {

	var theForm = '#propertyDemandNotice';
	var requestData = {};	
	var URL = 'PropertyDemandNoticeGeneration.html?resetForm';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}



function showSingleMultiple(){
	
	
	$('#selectall').prop('checked', true);
	$('.checkall').each(function() {
		this.checked = true;
	});

	if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="SM"){
		$('.PropDetail').show();
		$('.propLable').show();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();
		
		$('#assWard1').val('');
		$('#assWard2').val('');
		$('#locId').val('');
		
		var noticeType = $("#noticeType option:selected").attr("code");
		if(noticeType === 'JW'){
			$('.wardZone').show();
			$('.outstanding').show();
			$('.finYear').show();
			
		} else {
			$('.outstanding').hide();
			$('.finYear').hide();
		}
		
	}
	else if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="AL"){
		$('.PropDetail').hide();
		$('.wardZone').show();
		$('.Loc').show();
		$('.usageType').show();
		$('.searchBtn').show();
		$('.sectionSeperator').show();
		$('.PropDetail').val('');
		$('#propertyNo').val('');
		$('#oldPropertyNo').val('');
		$("#billingMethod").val('');
		$("#showFlatNo").hide();
	}else{
		$('.PropDetail').hide();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.propLable').hide();
		$('.usageType').hide();
		$('.searchBtn').hide();
		$('.sectionSeperator').hide();
		$('.outstanding').hide();
	}
	
}

function showErrorOnPage(errorList){
	var errMsg = '<ul>';
	var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeOutErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
	$.each(errorList, function(index) {
		errMsg += '<li><i class="fa fa-exclamation-circle"></i> &nbsp;' + errorList[index] + '</li>';
	});

	errMsg += '</ul>';
	$("#errorDiv").html(errMsg);
	$("#errorDiv").show();
	$("html, body").animate({ scrollTop: 0 }, "slow");
	return false;
}

function getFlatNo() {

    var errorList = [];
    var propNo=$("#proertyNo").val();
	var oldPropNo= $("#oldPid").val();
	if(propNo== "" && oldPropNo==""){
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0){
		 var propNo=$("#proertyNo").val();
		    var requestData = {
			"propNo" : propNo
		    };

		    var ajaxResponse = doAjaxLoading(
			    'PropertyDemandNoticeGeneration.html?getBillingMethod',
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
		    	  $("#billingMethod").val('I');
		    }
	}else{
		showErrorOnPage(errorList);
	}
		
}