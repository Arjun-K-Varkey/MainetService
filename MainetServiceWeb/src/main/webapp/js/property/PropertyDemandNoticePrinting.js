$(document)
		.ready(
				function() {
					
						
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
						
						$("#showFlatNo").hide();
				});

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


function serachProperty() {
	showloader(true);
		/*  $('#datatables').DataTable().destroy();
	
		var requestData = $('#propertyBillGeneration').serialize();*/
		
	var formAction	=	$('#propertyDemandNoticePrint').attr('action');
		var url=formAction+'?SearchPropNo';
		$('#propertyDemandNoticePrint').attr('action', url);

		$('#propertyDemandNoticePrint').submit();
		
		/*
		var ajaxResponse = __doAjaxRequest(
				formAction+'?SearchPropNo', 'POST',
				requestData, false, 'html');

		$(".content").html(ajaxResponse);
		return false;*/
}

function printNotice(year) {

    $("#errorDiv").hide();

    var errorList = [];

    // ---------- Read values ----------
    var noticeType  = $("#noticeType").val();
    var flatNo      = $("#flatNo").val();
    var billMethod  = $("#billingMethod").val();
    var searchType  = $("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val();
    var propNo      = $("#proertyNo").val(); 
    var yearId      = $("#finYearId").val();
    var assWard1    = $("#assWard1").val();
    var assWard2    = $("#assWard2").val();

    // ---------- Helper ----------
    function isEmpty(val) {
        return val === null || val === undefined || $.trim(val) === "";
    }

    // ---------- Validations ----------
    if (billMethod === 'I' && isEmpty(flatNo)) {
        errorList.push(getLocalMessage('prop.select.flatNo'));
    }

    if (isEmpty(noticeType) || noticeType === '0') {
        errorList.push(getLocalMessage('Demand.errpleaseselectnoticetype'));
    }

    if (searchType === "SM") {

        if (isEmpty(propNo)) {
            errorList.push(getLocalMessage('property.please.enterprop'));
        }
		yearId = year;
    	
    } else if (searchType === "AL") {

        if (isEmpty(assWard1)) {
            errorList.push(getLocalMessage('property.valid.ward'));
        }

        if (isEmpty(assWard2)) {
            errorList.push(getLocalMessage('property.valid.zone'));
        }
        yearId = $("#finYearId").val();
        if (isEmpty(yearId) || yearId === '0') {
            errorList.push(getLocalMessage('property.valid.finyear'));
        }
    }

    // ---------- Stop if validation fails ----------
    if (errorList.length > 0) {
        displayErrorsOnPage(errorList);
        return;
    }

    // ---------- Prepare request ----------
    var data = {
        propNo : propNo || "",
        flatNo : flatNo || "",
        yearId : yearId,
        ward1 : assWard1,
        ward2 : assWard2,
        noticeType : noticeType,
        searchType : searchType
    };

    var URL = 'PropertyDemandNoticePrinting.html?PropertyNoticePrinting';

    // ---------- AJAX call ----------
    var returnData = __doAjaxRequest(URL, 'POST', data, false);

    if (returnData) {
        window.open(returnData, '_blank');
    } else {
        errorList.push(getLocalMessage('Unable to generate notice'));
        displayErrorsOnPage(errorList);
    }
}

function resetForm1(element){
	value = "PropertyDemandNoticePrinting.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();
	//$("#propertyDemandNoticePrint").submit();

}



function showSingleMultiple(){
	
	$("#errorDiv").hide();
	$('#selectall').prop('checked', true);
	$('.checkall').each(function() {
		this.checked = true;
	});

	if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="SM"){
		$('.PropDetail').show();
		$('#PropDetails').show();
		$('.propLable').show();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();
		
		$('#assWard1').val('');
		$('#assWard2').val('');
		$('#locId').val('');
		$('.printBtn').hide();
		
	}
	else if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="AL"){
		$('.PropDetail').hide();
		$('#PropDetails').hide();
		$('.wardZone').show();
		$('.Loc').show();
		$('.usageType').show();
		$('.searchBtn').hide();
		$('.sectionSeperator').show();
		$('.PropDetail').val('');
		$('#propertyNo').val('');
		$('#oldPropertyNo').val('');
		$("#billingMethod").val('');
		$("#showFlatNo").hide();
		$('.printBtn').show();
	}else{
		$('.PropDetail').hide();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.propLable').hide();
		$('.usageType').hide();
		$('.searchBtn').hide();
		$('.sectionSeperator').hide();
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