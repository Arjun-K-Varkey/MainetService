function fetchRentMasterData(element){

	 	/*alert("In search");*/
		
		$('.error-div').hide();

		var errorList = [];

		var areaId = $("#areaId").val();
		var surveyNo = $("#surveyNo").val();
		
		if(areaId===undefined||areaId==="")
		{
			errorList.push(getLocalMessage("Please select Area")); 
		}
		
		
		if(errorList.length>0){
	    	
	    	var errorMsg = '<ul>';
	    	$.each(errorList, function(index){
	    		errorMsg +='<li><i class="fa fa-exclamation-circle"></i>&nbsp;' + errorList[index] + '</li>';
	    		
	    	});
	    	errorMsg +='</ul>';
	    	
	    	$('#errorId').html(errorMsg);
	    	$('#errorDivId').show();
			$('html,body').animate({ scrollTop: 0 }, 'slow');
	    		    	
	    }
		 
		if (errorList.length == 0) 
		{
		var divName = '.content-page';
		var url ="LandRateMasterController.html?getjqGridsearch";
		var requestData = {
				"areaId" : areaId,
				"surveyNO":surveyNo
			};
		
		var result = __doAjaxRequest(url, 'POST', requestData, false, 'html');
		
		if (result != null && result != "") {
			
			$(divName).html(result);
			$('#areaId option:selected').val(areaId);
			$('#areaId').prop('disabled', true);
			
			prepareTags();
		} else {
			var errorList = [];
			
			errorList.push(getLocalMessage("account.norecord.criteria"));
			
			if(errorList.length>0){		    	
		    	var errorMsg = '<ul>';
		    	$.each(errorList, function(index){
		    		errorMsg +='<li><i class="fa fa-exclamation-circle"></i>&nbsp;' + errorList[index] + '</li>';		    		
		    	});
		    	errorMsg +='</ul>';		    	
		    	$('#errorId').html(errorMsg);
		    	$('#errorDivId').show();
				$('html,body').animate({ scrollTop: 0 }, 'slow');		    		    	
		    }
			$("#grid").jqGrid('setGridParam', {
				datatype : 'json'
			}).trigger('reloadGrid');
		}
	}
}



function addLandRow(){
    var rowCount = $('#landDetailTable tr').length;
    var chkNoneData= $('#landDetailTable tbody tr:first').attr('id');
    if(rowCount == 2 && chkNoneData=='forNoneData'){
    	/*$('#forNoneData').css('display','table-row !important');*/
    	$('#forNoneData').removeClass('hide');
    	$('#forNoneData').attr('id','');
    	rowCount= rowCount-2;
        
    }else{
    	$('#landDetailTable tbody tr:first').clone().insertAfter('#landDetailTable tbody tr:last');
    	rowCount= rowCount-1;
    }
    
   // $('#landDetailTable tbody tr:first').clone().insertAfter('#landDetailTable tbody tr:last');
    //console.log("rowCount"+rowCount);
    
    $('#landDetailTable tr:last input[id=editStatus0]').val('1');
    $('#landDetailTable tr:last input[id=editStatus0]').attr('name','lndRateList['+rowCount+'].isUpdate');
    $('#landDetailTable tr:last input[id=editStatus0]').attr('id','editStatus'+rowCount);
    $('#landDetailTable tr:last input[id=sequence0]').val(rowCount+1); 
    $('#landDetailTable tr:last input[id=surveyNo0]').val('');
    $('#landDetailTable tr:last select[id=survetType0]').val('');
    $('#landDetailTable tr:last input[id=fromDate0]').val('');
    $('#landDetailTable tr:last input[id=toDate0]').val('');
    $('#landDetailTable tr:last input[id=amt0]').val('');
    
    $('#landDetailTable tr:last input[id=surveyNo0]').removeClass('non-edited');
    $('#landDetailTable tr:last select[id=survetType0]').removeClass('non-edited');
    $('#landDetailTable tr:last input[id=fromDate0]').removeClass('non-edited');
    $('#landDetailTable tr:last input[id=toDate0]').removeClass('non-edited');
    $('#landDetailTable tr:last input[id=amt0]').removeClass('non-edited');

    
    $('#landDetailTable tr:last input[id=sequence0]').attr('id','sequence'+rowCount);
    
    $('#landDetailTable tr:last input[id=surveyNo0]').attr('name','lndRateList['+rowCount+'].surveyNo');
    $('#landDetailTable tr:last input[id=surveyNo0]').attr('id','surveyNo'+rowCount);

    $('#landDetailTable tr:last select[id=survetType0]').attr('name','lndRateList['+rowCount+'].surveyType');
    $('#landDetailTable tr:last select[id=survetType0]').attr('id','survetType'+rowCount);
    
    $('#landDetailTable tr:last input[id=fromDate0]').attr('name','lndRateList['+rowCount+'].fromDate');
    $('#landDetailTable tr:last input[id=fromDate0]').attr('id','fromDate'+rowCount);
    $('#landDetailTable tr:last input[id=fromDate'+rowCount+']').removeClass('hasDatepicker');
    $('#landDetailTable tr:last input[id=fromDate'+rowCount+']').datepicker({
    	dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		minDate : "10/12/1999",
		onClose: function( selectedDate ) {
	        $( ".toDate" ).datepicker( "option", "minDate", selectedDate );
	      },
		timeFormat : "HH:mm"
		
    });
    
    $('#landDetailTable tr:last input[id=toDate0]').attr('name','lndRateList['+rowCount+'].toDate');
    $('#landDetailTable tr:last input[id=toDate0]').attr('id','toDate'+rowCount);
    $('#landDetailTable tr:last input[id=toDate'+rowCount+']').removeClass('hasDatepicker');
    $('#landDetailTable tr:last input[id=toDate'+rowCount+']').datepicker({
    	dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		timeFormat : "HH:mm"
    });
    
    $('#landDetailTable tr:last input[id=amt0]').attr('name','lndRateList['+rowCount+'].amt');
    $('#landDetailTable tr:last input[id=amt0]').attr('id','amt'+rowCount);
    
    $('#landDetailTable tr:last a#editRow0').attr('onclick','deleteLandRow('+rowCount+')');
    $('#landDetailTable tr:last a#editRow0').attr('onclick','deleteLandRow('+rowCount+')');
    $('#landDetailTable tr:last a#editRow0').removeClass('btn-warning');
    $('#landDetailTable tr:last a#editRow0').addClass('btn-danger');
    $('#landDetailTable tr:last a#editRow0 strong').removeClass('fa-pencil');
    $('#landDetailTable tr:last a#editRow0 strong').addClass('fa-trash');
    $('#landDetailTable tr:last a#editRow0').attr('data-original-title', 'Delete');
    $('#landDetailTable tr:last a#editRow0').attr('id','removeRow'+rowCount);
    
    

    //$('#itemTable tr:last input[name=itemquanty0]').attr('id','itemquanty'+rowCount);
  }


function deleteLandRow(rNum){
	var rowNum = rNum+1;
	var rowCount = $('#landDetailTable tr').length;
      $("#landDetailTable tr:eq("+rowNum+")").remove();
      reOrderTable();
}

function editLandRow(rNum){
	$('#editStatus'+rNum).val('1');	
	$('#landDetailTable tr td input[id=surveyNo'+rNum+']').removeClass('non-edited');
	$('#landDetailTable tr td select[id=survetType'+rNum+']').removeClass('non-edited');
	$('#landDetailTable tr td input[id=fromDate'+rNum+']').removeClass('non-edited');
	$('#landDetailTable tr td input[id=fromDate'+rNum+']').removeClass('hasDatepicker');
    $('#landDetailTable tr td input[id=fromDate'+rNum+']').datepicker({
	    	dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			minDate : "10/12/1999",
			onClose: function( selectedDate ) {
		        $( ".toDate" ).datepicker( "option", "minDate", selectedDate );
		      },
			timeFormat : "HH:mm"
			
	    });
	$('#landDetailTable tr td input[id=toDate'+rNum+']').removeClass('non-edited');
	$('#landDetailTable tr td input[id=toDate'+rNum+']').removeClass('hasDatepicker');
	   $('#landDetailTable tr td input[id=toDate'+rNum+']').datepicker({
	    	dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true,
			timeFormat : "HH:mm"
	    });
	    
	$('#landDetailTable tr td input[id=amt'+rNum+']').removeClass('non-edited');

}
function reOrderTable(){
	      var rowCount = $('#landDetailTable tr').length;
	      for (var i=0; i<=rowCount; i++){
	    	 	    	    
	    	  $('#landDetailTable tr').eq(i+1).find('input[id^=sequence]').val(i+1);  
	    	  $('#landDetailTable tr').eq(i+1).find('input[id^=sequence]').attr('id','sequence'+i);
	    	    
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=surveyNo]').attr('name','lndRateList['+i+'].surveyNo');
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=surveyNo]').attr('id','surveyNo'+i);

	    	    $('#landDetailTable tr').eq(i+1).find('select[id^=survetType]').attr('name','lndRateList['+i+'].survetType');
	    	    $('#landDetailTable tr').eq(i+1).find('select[id^=survetType]').attr('id','survetType'+i);
	    	    	    	    
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=fromDate]').attr('name','lndRateList['+i+'].fromDate');
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=fromDate]').attr('id','fromDate'+i);
	    	    
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=toDate]').attr('name','lndRateList['+i+'].toDate');
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=toDate]').attr('id','toDate'+i);
	    	    
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=amt]').attr('name','lndRateList['+i+'].amt');
	    	    $('#landDetailTable tr').eq(i+1).find('input[id^=amt]').attr('id','amt'+i);
	    	    
	    	    $('#landDetailTable tr').eq(i+1).find('a[id^=removeRow]').attr('onclick','deleteLandRow('+i+')');
	    	    $('#landDetailTable tr').eq(i+1).find('a[id^=removeRow]').attr('id','removeRow'+i);

	      }
 }


function ValidateLandTable(){
	
	var rowCount = $('#landDetailTable tr').length;
	var errorList=[];
	var surNo=[];
	var SurType=[];
	var frmDate=[];
	var tDate=[];
	var surAmt=[];
    for (var i=0; i<=rowCount-2; i++){
    	var surveyNo=$('#surveyNo'+i).val();
    	var survetType=$('#survetType'+i).val();
    	var fromDate=$('#fromDate'+i).val();
    	var toDate=$('#toDate'+i).val();
    	var amt=$('#amt'+i).val();
    	
    	
    	if(surveyNo =="" || surveyNo == undefined || surveyNo == null){
    		errorList.push("Please Enter Survey No for Line No"+ (i+1));
    	}
    	else{
    		surNo.push(surveyNo);
    	}
    	if(survetType =="" || survetType == undefined || survetType == null){
    		errorList.push("Please Enter Survey Type for Line No"+ (i+1));
    	}
    	else{
    		SurType.push(survetType);
    	}
    	if(fromDate =="" || fromDate == undefined || fromDate == null){
    		errorList.push("Please Enter From Date for Line No"+ (i+1));
    	}
    	else{
    		frmDate.push(fromDate);
    	}/*
    	if(toDate =="" || toDate == undefined || toDate == null){
    		errorList.push("Please Enter to Date for Line No"+ (i+1));
    	}
    	else{
    		tDate.push(toDate);
    	}*/
    	if(amt =="" || amt == undefined || amt == null){
    		errorList.push("Please Enter Amount for Line No"+ (i+1));
    	}else{
    		surAmt.push(amt);
    	}
    	
    }
    
    //var sNo=chkDuplicateValueinArr(surNo);
  //  var sType=chkDuplicateValueinArr(SurType);
  //  var fDate=chkDuplicateValueinArr(frmDate);
  //  var ttDate=chkDuplicateValueinArr(tDate);
   // var samt=chkDuplicateValueinArr(surAmt);
    
    /*if(sNo ==0){
    	errorList.push("Survey No Has Duplicate Value");
    }*/
   /* if(sType ==0){
    	errorList.push("Survey Type Has Duplicate Value");
    }*/
   /* if(fDate ==0){
    	errorList.push("From Date Has Duplicate Value");
    }
    if(ttDate ==0){
    	errorList.push("To Date Has Duplicate Value");
    }
    if(samt ==0){
    	errorList.push("Survey Amount Has Duplicate Value");
    }*/
	return errorList;
}





function chkDuplicateValueinArr(arr){
	debugger;
	var chklen=arr.length;
	var chkArr=$.unique(arr);
	if(chkArr.length == chklen){
		return 1;
	}else{
		return 0;
	}
}


function saveRentForm(element){
	
	var errList = ValidateLandTable();
	if(errList.length == 0){

		
		
		var url = "LandRateMasterController.html?saveLandRate";
		//var	formName =	findClosestElementId(obj, 'form');
		//var theForm	=	'#'+formName;
		var requestData = $('#LandRateMaster').serialize();
		//var requestData = $('#frmMaster').serialize();
		
	    
		var returnData =__doAjaxRequest(url,'post',requestData,false);
		
		
		
		if(returnData!=undefined && returnData!=null)
			{
			showConfirmBox(returnData);

			}
		else
			{
			$('.content').html(returnData);
			$('.content').html(returnData);
			}

	}else{
		showLandErrorList(errList);
	}
	
	
function showLandErrorList(errorList){
		var errMsg = '<ul>';
		var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeOutErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
		$.each(errorList, function(index) {
			errMsg += '<li><i class="fa fa-exclamation-circle"></i> &nbsp;' + errorList[index] + '</li>';
		});

		errMsg += '</ul>';
		$("#errorDivId").html(errMsg);
		$("#errorDivId").show();
		$("html, body").animate({ scrollTop: 0 }, "slow");
		return false;
	}

}
function showConfirmBox(msg)
{
		
		var errMsgDiv = '.msg-dialog-box';
		var message = '';
		var cls = 'Proceed';
    	message	+='<h5 class=\'text-center text-blue-2 padding-5\'>'+msg+'</h5>';
	    message	+='<div class=\'text-center\'><input type=\'button\' class= "btn btn-success" value=\''+cls+'\'  id=\'btnNo\' onclick="resetForm()"/></div>';
		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(message);
		$(errMsgDiv).show();
		$('#btnNo').focus();
		//showModalBoxWithoutClose(errMsgDiv);
		showModalBoxWithoutCloseaccount(errMsgDiv);
}
function resetForm(element){
	window.location.href='LandRateMasterController.html'
}
