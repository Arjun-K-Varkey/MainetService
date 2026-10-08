var isBillGenerationInProgress = false;
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
						
						
						var dateFields = $('.Moredatepicker');
						dateFields.each(function() {
							var fieldValue = $(this).val();
							if (fieldValue.length > 10) {
								$(this).val(fieldValue.substr(0, 10));
							}
						});
						
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
						
						
						
						$('#fromDate').datepicker({
							dateFormat : 'dd/mm/yy',
							changeMonth : true,
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
				});




function serachProperty() {
	
	var errorList = [];
	var billMethod = $("#billingMethod").val();
	if(billMethod == 'I'){
		var flatNo = $("#flatNo").val();
		if (flatNo == "" || flatNo == undefined || flatNo == null
				|| flatNo == '0') {
			errorList.push(getLocalMessage('Please select flat no'));
		}
	}
	if (errorList.length == 0)
	{
		/*  $('#datatables').DataTable().destroy();
		
		var requestData = $('#propertyBillGeneration').serialize();*/
		showloader(true);
		
	var formAction	=	$('#propertyBillGeneration').attr('action');
		var url=formAction+'?SearchPropNoForSkdcl';
		$('#propertyBillGeneration').attr('action', url);

		$('#propertyBillGeneration').submit();
		
		/*
		var ajaxResponse = __doAjaxRequest(
				formAction+'?SearchPropNo', 'POST',
				requestData, false, 'html');

		$(".content").html(ajaxResponse);
		return false;*/
	}else{
		displayErrorsOnPage(errorList);
	}
}

function generateBill(element){

	// Block double click
    if (isBillGenerationInProgress) {
        return false;
    }

    isBillGenerationInProgress = true;

    // Disable Generate Bill button
    $(element).prop("disabled", true).addClass("disabled").text("Generating...");

    // Optional: disable Reset also
    $("#resetBtn").prop("disabled", true);
    
	showloader(true);
	setTimeout(function(){startGenBill(element)},2);
}

function startGenBill(element){
	try {
	$('#datatables').DataTable().destroy();
	return saveOrUpdateForm(element,
			"Bill generation  done successfully",
			'PropertyBillGenerationLog.html', 'saveform');
	} catch (e) {
        // Failure → re-enable button
        resetGenerateBillButton(element);
        throw e;
    }		
}

function resetGenerateBillButton(btn) {
	isBillGenerationInProgress = false;

	$(btn)
		.prop("disabled", false)
		.removeClass("disabled")
		.text("Generate Bill");

	$("#resetBtn").prop("disabled", false);
}


/*function resetForm(element){
	$("#propertyBillGeneration").submit();

}*/
function resetFormDetails(resetBtn) {
	value = "PropertyBillGeneration.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();
}



function showSingleMultiple(){
	
	
	$('#selectall').prop('checked', true);
	$('.checkall').each(function() {
		this.checked = true;
	});

	if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="S" 
		|| $("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="MP"){
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
		var billMethod = $("#billingMethod").val();
		if(billMethod == 'I'){
			$("#showFlatNo").show();
		}else{
			$("#showFlatNo").hide();
		}
	}
	else if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="M"){
		$('.PropDetail').hide();
		$('.wardZone').show();
		$('.Loc').show();
		$('.usageType').show();
		$('.searchBtn').show();
		$('.sectionSeperator').show();
		$('.PropDetail').val('');

		$('#propertyNo').val('');
		$('#oldPropertyNo').val('');
		$('#flatNo').val('');
		$("#showFlatNo").hide();
	}else{
		$('.PropDetail').hide();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.propLable').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();
		$('#flatNo').val('');
		$("#showFlatNo").hide();
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
    var propNo=$("#propertyNo").val();
	var oldPropNo= $("#oldPropertyNo").val();
	if(propNo== "" && oldPropNo==""){
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0){
		 var propNo=$("#propertyNo").val();
		    var requestData = {
			"propNo" : propNo
		    };

		    var ajaxResponse = doAjaxLoading(
			    'PropertyBillGeneration.html?getBillingMethod',
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
		    	 // $("#serchBtn").hide();
		    	 // $("#serchBtnDirect").show();
		    	  $("#billingMethod").val('I');
		    }
	}else{
		showErrorOnPage(errorList);
	}
		
}

$(document).ready(function () {

    if ($("#errorDiv").is(":visible") || $(".error").length > 0) {

        isBillGenerationInProgress = false;

        $("#btn1")
            .prop("disabled", false)
            .removeClass("disabled")
            .text("Generate Bill");

        $("#resetBtn").prop("disabled", false);
    }
});

$(document).on("keydown", function (e) {
    if (e.key === "Enter" && isBillGenerationInProgress) {
        e.preventDefault();
        return false;
    }
});
