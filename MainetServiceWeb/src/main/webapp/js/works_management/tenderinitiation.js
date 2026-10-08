/**
 * @Author hiren.poriya
 */

var TENDER_URL = "TenderInitiation.html";
var WorkDefURL= "WmsWorkDefinationMaster.html";
var removeWorkIdArray=[];
$(document).ready(function(){
	
	$('#TenderInitiation').validate({
		onkeyup: function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout: function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}});
	
	 
	 $('[id^="tenderType"]').data('rule-required',true);
	 $('[id^="tenderValue"]').data('rule-required',true);
	 $('[id^="vendorId"]').data('rule-required',true);
	 $('[id^="vendorWorkPeriodUnit"]').data('rule-required',true);
	 $('[id^="venderClassId"]').data('rule-required',true);
	 
	$("#datatables").dataTable({
		"oLanguage": { "sSearch": "" } ,
		"aLengthMenu": [ [5, 10, 15, -1], [5, 10, 15, "All"] ],
	    "iDisplayLength" : 5, 
	    "bInfo" : true,
	    "lengthChange": true
	    });	 

	
	$("#prepareTender").click(function(){
		var requestData ={"type":"C"}
		var ajaxResponse = __doAjaxRequest(TENDER_URL+'?form', 'POST', requestData, false,'html');
		$('.content-page').html(ajaxResponse);	
	});
	
	
	// tender date
	
	$('.tenderDatePicker').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "-100:-0"
	});
	
	
	var tenderDatePicker = $('.tenderDatePicker');
	tenderDatePicker.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
	
	
	// resolution date picker
	$('.resolutionDatePicker').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "-100:-0",
	    onSelect: function(selected) {
			$(".issueFromDatePicker").datepicker("option","minDate", selected);
			$(".publishDatePicker").datetimepicker("option","minDate", selected);
			// $("#resolutionNo").addClass("mandColorClass");
		    // $("#resolutionNo").data('rule-required',true);
			
	      } 
	});
	
	
	var resolutionDate = $('#resolutionDate');
	resolutionDate.each(function () {
		var resolutionDates = $(this).val();
		if (resolutionDates.length > 10) {
			$(this).val(resolutionDates.substr(0, 10));
		}
	});
	var dateFields = $('.resolutionDatePicker');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
	
	
	// issue from date date picker
	$('.issueFromDatePicker').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		yearRange : "1900:2200",
		   onSelect: function(selectedDate) {
				$(".issueToDatePicker").datepicker("option","minDate", selectedDate);
				$(".publishDatePicker").datetimepicker("option","maxDate", selectedDate);
				issueFromDateValid();
		      } 
	});
	var datepickerEndDate = $('.issueFromDatePicker');
  	datepickerEndDate.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
  	
  	

  	// issue to date date picker
	$('.issueToDatePicker').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		yearRange : "1900:2200",
		 onSelect: function(selectedDate) {	 
				$(".technicalOpenDatePicker").datetimepicker("option","minDate", selectedDate);
				issueToDateValid();
		      } 
	});
	
	var issueToDatePicker = $('.issueToDatePicker');
	issueToDatePicker.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
  	
  	
	
	
	// technical open date
	$('.technicalOpenDatePicker').datetimepicker({
		dateFormat: 'dd/mm/yy',
		timeFormat: "HH:mm",
		changeMonth : true,
		changeYear : true,
		yearRange : "1900:2200",
		 onSelect: function(selectedDate) {
		   
				$(".financialeOpenDatePicker").datetimepicker("option","minDate", selectedDate);
				technicalOpenDateValid();
		      } 
	});
	
	/*
	 * var technicalOpenDatePicker = $('.technicalOpenDatePicker');
	 * technicalOpenDatePicker.each(function () { var fieldValue =
	 * $(this).val(); if (fieldValue.length > 16) {
	 * $(this).val(fieldValue.substr(0, 16)); } });
	 */
	
	
	// financial open date
	$('.financialeOpenDatePicker').datetimepicker({
		dateFormat: 'dd/mm/yy',
		timeFormat: "HH:mm",
		changeMonth : true,
		changeYear : true,
		yearRange : "1900:2200",
		 onSelect: function(selected) {
				financeDateValid();
			  } 
	});
	
	/*
	 * var financialeOpenDatePicker = $('.financialeOpenDatePicker');
	 * financialeOpenDatePicker.each(function () { var fieldValue =
	 * $(this).val(); if (fieldValue.length > 16) {
	 * $(this).val(fieldValue.substr(0, 16)); } });
	 */
		
	// publish date picker
	$('.publishDatePicker').datetimepicker({
		dateFormat: 'dd/mm/yy',
		timeFormat: "HH:mm",
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "1900:2200",
		onSelect: function(selected) {
			publishDateValid();
		  } 
	
	});
	
	
	/*
	 * var publishDatePicker = $('.publishDatePicker');
	 * publishDatePicker.each(function () { var fieldValue = $(this).val(); if
	 * (fieldValue.length > 16) { $(this).val(fieldValue.substr(0, 16)); } });
	 */
	
  	
	// financial open date
	$('.initiationDatePicker').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		yearRange : "1900:2200"
	});
	
	var initiationDatePicker = $('.initiationDatePicker');
	initiationDatePicker.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
	
	
	$('.preBidMeetDatePicker').datetimepicker({
		dateFormat: 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		timeFormat: "HH:mm"

	});
	/*
	 * var preBidMeetDateDatePicker = $('.preBidMeetDatePicker');
	 * preBidMeetDateDatePicker.each(function () { var fieldValue =
	 * $(this).val(); if (fieldValue.length > 16) {
	 * $(this).val(fieldValue.substr(0, 16)); } });
	 */
	
  	$("#resetTender").click(function(){
		var requestData ={"mode":"C"}
		var ajaxResponse = __doAjaxRequest(TENDER_URL+'?form', 'POST', requestData, false,'html');
		$('.content-page').html(ajaxResponse);
		$('.workProj').html("");
	});
  	
  	$("#searchTender").click(function() {
  		
  		var errorList=[];
  			var projId = $('#projId').val();
  			// var initiationNo = $('#initiationNo').val();
  			// var initiationDate = $('#initiationDate').val();
  			var tenderCategory = $('#tenderCategory').val();
  			if( projId!= '' ||tenderCategory != '0' ){
  				var requestData = 'projId='+projId +'&tenderCategory='+tenderCategory;
  				var table = $('#datatables').DataTable();
  				table.rows().remove().draw();
  				$(".warning-div").hide();
  				var ajaxResponse = __doAjaxRequest(TENDER_URL+'?filterTenderData', 'POST',requestData, false,'json');
  				var result = [];
  				$.each(ajaxResponse, function(index){
  					var obj = ajaxResponse[index];
  					if($('#serviceFlag').val() != 'A'){
  						
  						if(obj.initiationNo == null){
  	  						result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,'<td class="text-center">'+
  	  							  '<button type="button"  class="btn btn-green-3 btn-sm margin-right-5 " onClick="initiateTender(\''+obj.tndId+'\')" title="Initiate Tender"><i class="fa fa-share-square-o"></i></button>'+
  	  							 '<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
  	  							 '<button type="button" class="btn btn-warning btn-sm margin-right-5" onClick="editTender(\''+obj.tndId+'\')"  title="Edit"><i class="fa fa-pencil-square-o"></i></button>'+
  	  							 '<button type="button" class="btn btn-danger btn-sm margin-right-5" onClick="deleteTender(\''+obj.tndId+'\')"  title="Delete"><i class="fa fa-trash"></i></button>'+
  	  							  '</td>']);
  	  					}else {
  	  						if(obj.tenderNo== null){
  	  							result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,'<td class="text-center">'+
  	  							'<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
  	  							  '<button type="button"  class="btn btn-primary btn-sm margin-right-5 " onClick="updateTender(\''+obj.tndId+'\')" title="Update"><i class="fa fa-line-chart"></i></button>'+
  	  							 /*
									 * '<button type="button" class="btn
									 * btn-danger btn-sm margin-right-10"
									 * onClick="deleteTender(\''+obj.tndId+'\')"
									 * title="Delete"><i class="fa fa-trash"></i></button>'+
									 */  							   							  
  	  							  '</td>']);	
  	  						}else{
  	  							result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,'<td class="text-center">'+
  	  							'<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
  	  							  '<button type="button"  class="btn btn-primary btn-sm margin-right-5 " onClick="updateTender(\''+obj.tndId+'\')" title="Update"><i class="fa fa-line-chart"></i></button>'+
  	  							 /* '<button type="button" class="btn btn-warning btn-sm margin-right-10" onClick="printLoa(\''+obj.tndId+'\')"  title="Print LOA"><i class="fa fa-print"></i></button>'+*/
  	  							 '</td>']);
  	  						}  						
  						}
  				   }else{
  					   if(obj.status =="P" || obj.status =="R"){
  						 result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,obj.statusDesc,'<td class="text-center">'+
  							'<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
	  							 '</td>']);
  					   }else if(obj.status =="A" && obj.tenderNo== null) {
  						 result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,obj.statusDesc,'<td class="text-center">'+
  							'<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
 							 '<button type="button"  class="btn btn-primary btn-sm margin-right-5 " onClick="updateTender(\''+obj.tndId+'\')" title="Update"><i class="fa fa-line-chart"></i></button>'+
 							  '</td>']);
  					   }else{
  						 result.push([obj.initiationNo,obj.tenderNo,obj.projectName,obj.tenderCategoryDesc,obj.tenderTotalEstiAmount,obj.tndLOANo,obj.statusDesc,'<td class="text-center">'+
  							'<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 " onclick="tenderView(\''+obj.tndId+'\')" title="View"><i class="fa fa-eye"></i></button>'+
	  							  '<button type="button"  class="btn btn-primary btn-sm margin-right-5 " onClick="updateTender(\''+obj.tndId+'\')" title="Update"><i class="fa fa-line-chart"></i></button>'+
	  							 /* '<button type="button" class="btn btn-warning btn-sm margin-right-10" onClick="printLoa(\''+obj.tndId+'\')"  title="Print LOA"><i class="fa fa-print"></i></button>'+*/
	  							 '</td>']);
  					   }
  					   
  				   }  					
  		         });
  				table.rows.add(result);
  				table.draw();
  			}else{
  				errorList.push(getLocalMessage('tender.search.validation'));
  				displayErrorsOnPage(errorList);
  			}
  	   });
  	
  	
  	if( $("#mode").val()=="E" ||$("#mode").val()=="V" )	
	 {
  		
		// issue from date should be after resolution date.
		var resolutionDate =  $("#resolutionDate").val();
		$(".issueFromDatePicker ").datepicker("option","minDate", resolutionDate);
		
		if(resolutionDate!=null && resolutionDate!=""){
			$("#resolutionNo").addClass("mandColorClass");
		}
		
		// issue to date should be after issue from date.
		var issueFromDate =  $("#issueFromDate").val();
		$(".issueToDatePicker").datepicker("option","minDate", issueFromDate);
		
		// publish date should be between resolution date and issue from date.
		var pDate = $("#publishDate").val();
		$(".publishDatePicker").datetimepicker("option","minDate", resolutionDate);
		$(".publishDatePicker").datetimepicker("option","maxDate", issueFromDate);
		$("#publishDate").val(pDate);
		// technical open date should be after issue to date.
		var issueToDate =  $("#issueToDate").val();
		var tDate=$("#technicalOpenDate").val();
		$(".technicalOpenDatePicker").datetimepicker("option","minDate", issueToDate);
		$("#technicalOpenDate").val(tDate);
		
		// financial open date should be after technical open date.
		var fDate = $("#financialeOpenDate").val();
		var technicalOpenDate =  $("#technicalOpenDate").val();
		$(".financialeOpenDatePicker ").datetimepicker("option","minDate", technicalOpenDate);
		if(fDate!='')
		$("#financialeOpenDate").val(fDate);
	 }
  	if( $("#mode").val()=="C")	
	 {
  		$("#tndValidityDay").val(parseInt(120));
	 }
  	
  	
  	if("V" == $("#mode").val() && "SD" == $("#deptId").find("option:selected").attr('code')){   
  		$('select').attr("disabled", true);
  		$('input[type=text]').attr("disabled", true);
  		$('input[type="text"], textarea').attr("disabled", true);
  		$('select').prop('disabled', true).trigger("chosen:updated");
  		$('input[type=checkbox]').attr("disabled", true);
	}
  	
  	
});



function financeDateValid(){
	var technicalOpenDate =  $("#technicalOpenDate").val();	
	var resolutionDate =  $("#resolutionDate").val(); 
	if(resolutionDate ==''){
		showAlertBoxForForDateValid("financialeOpenDate",getLocalMessage("tender.resolutiondate.valid"));
	}else{
		if(technicalOpenDate=='')
		{
			showAlertBoxForForDateValid("financialeOpenDate",getLocalMessage("tender.techbiddate.valid"));
		}
	}
	
}

function technicalOpenDateValid(){
	var issueToDate =  $("#issueToDate").val();	
	var resolutionDate =  $("#resolutionDate").val(); 
	if(resolutionDate ==''){
		showAlertBoxForForDateValid("technicalOpenDate",getLocalMessage("tender.resolutiondate.valid"));
	}else{
	if(issueToDate=='')
		{
			showAlertBoxForForDateValid("technicalOpenDate",getLocalMessage("tender.issuetill.valid"));
		}
	}
}


function issueToDateValid(){
	var issueFromDate =  $("#issueFromDate").val();	
	if(issueFromDate=='')
		{
			showAlertBoxForForDateValid("issueToDate",getLocalMessage("tender.issuefrom.valid"));
		}
}

function issueFromDateValid(){
	var resolutionDate =  $("#resolutionDate").val();	
	if(resolutionDate=='')
		{
			showAlertBoxForForDateValid("issueFromDate",getLocalMessage("tender.resolutiondate.valid"));
		}
}


function publishDateValid(){
	
	var resolutionDate =  $("#resolutionDate").val();	
	var issueFromDate =  $("#issueFromDate").val();	
	if(resolutionDate=='' || issueFromDate=='')
		{
			showAlertBoxForForDateValid("publishDate",getLocalMessage("tender.resolutiondate.issuefrom.valid"));
		}
}



function showAlertBoxForForDateValid(id,msg){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = 'Ok';
	message	+='<h4 class=\"text-center text-blue-2 padding-10\">'+msg+'</h4>';
	 message	+='<div class=\'text-center padding-bottom-10\'>'+	
	'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '+ 
	' onclick="closeAlertForm()"/>'+
	'</div>';
	
	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg'); 
	$(errMsgDiv).html(message);
	$('#'+id).val("");
	$(errMsgDiv).show();
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
/*function getProjects(){
// var deptId = $(obj).val();
	var deptId =	$('#deptId').val();
	$("#projectDetailsDiv").show();
	var deptCode = $("#deptId").find("option:selected").attr('code');
	$("#deptCode").val(deptCode);
	if("SD" != deptCode){
		
		$('#projId').html('');
		$('#projId').append($("<option></option>").attr("value","").text(getLocalMessage('selectdropdown'))).trigger('chosen:updated');
		$('.workClass').hide();
		if(deptId!="" &&  deptId!=0){
					var requestData ={"deptId":deptId}
					var projectList = __doAjaxRequest(TENDER_URL+'?getProjectList', 'POST', requestData, false,'json');
					$.each(projectList, function(index, value) {
						$('#projId').append($("<option></option>").attr("value",value.projId).attr("code",value.rsoNumber+","+value.rsoDate).text(value.projNameEng));
					});
					$('#projId').trigger('chosen:updated');	
				}
	}else{
		$("#projectDetailsDiv").hide();
		showPurchaseRequistionGrid(deptId);
		
	}
}*/

function getProjects(){
	// var deptId = $(obj).val();
		var deptId =	$('#deptId').val();
		var deptCode = $("#deptId").find("option:selected").attr('code');
		$("#deptCode").val(deptCode);
		$('#projId').html('');
		$('#projId').append($("<option></option>").attr("value","").text(getLocalMessage('selectdropdown'))).trigger('chosen:updated');
		$('.workClass').hide();
		if(deptId!="" &&  deptId!=0){
			var requestData ={"deptId":deptId}
			var projectList = __doAjaxRequest(TENDER_URL+'?getProjectList', 'POST', requestData, false,'json');
			$.each(projectList, function(index, value) {
				if("SD" == deptCode){
					$('#projId').append($("<option></option>").attr("value",value.projId).attr("code",value.projCode).text(value.projNameEng));
				}else{
					$('#projId').append($("<option></option>").attr("value",value.projId).attr("code",value.rsoNumber+","+value.rsoDate).text(value.projNameEng));
				}
				
			});
			$('#projId').trigger('chosen:updated');	
		}
		
	}

function showPurchaseRequistionGrid(deptId){
	var requestData = {
			"deptId" : deptId
		}
	var response = __doAjaxRequest(TENDER_URL + '?getEstimatedWork',
			'POST', requestData, false, 'html');
	//$('#projId').val("").trigger('chosen:updated');
	$('.workClass').html(response);
	$('.workClass').show();

	$('[id^="initiated"]').prop('checked', false);

	// enable estimate button for view estimate
	$('.viewEstimate').prop("disabled", false);

	// disabled all fields
	$('[id^="tenderFeeAmt"]').prop('disabled', true);
	$('[id^="venderClassId"]').prop('disabled', true);
	$('[id^="tenderSecAmt"]').prop('disabled', true);
	$('[id^="vendorWorkPeriodUnit"]').prop('disabled', true);
	$('[id^="vendorWorkPeriod"]').prop('disabled', true);
	$('[id^="tenderType"]').prop('disabled', true);
   return false;
 } 

function editTender(tdId){
	var requestData = 'tdId='+tdId+'&mode=E';
	var ajaxResponse = __doAjaxRequest(TENDER_URL+'?form', 'POST',requestData, false,'html');
	$('.content-page').html(ajaxResponse);
}

function tenderView(tdId){

	var requestData = 'tdId='+tdId+'&mode=V';
	var ajaxResponse = __doAjaxRequest(TENDER_URL+'?form', 'POST',requestData, false,'html');
	$('.content-page').html(ajaxResponse);	
	$("#TenderInitiation :input").prop("disabled", true); 
	$("select").prop("disabled", true).trigger("chosen:updated");
	$(".workClass").prop("disabled",false);
	$(".viewEstimate").prop("disabled",false);
	$(".viewNIT").prop("disabled", false);
	$(".viewFormA").prop("disabled", false);
	$(".viewPQR").prop("disabled", false);
	$(".viewFormF").prop("disabled", false);
	$("#button-Cancel").prop("disabled", false);
	
}
function initiateTender(tenderId){

	 var requestData = 'tenderId='+tenderId;
	 var response = __doAjaxRequest(TENDER_URL+'?initiateTender', 'POST', requestData, false,'json');
	 if(response=="Y"){
	  showConfirmBox(getLocalMessage("tender.initated.success"));
	}else{
		showConfirmBox(response);
	 }
	}


function deleteTender(tndId){
	 var requestData = 'tndId='+tndId;
	 var response = __doAjaxRequest(TENDER_URL+'?deleteTender', 'POST', requestData, false,'json');
	 if(response){
	  showConfirmBox(getLocalMessage("tender.delete.success"));
	}else{
	    $(childDivName).html("Internal errors");
		showModalBox(childDivName);
	  }
	}


function showConfirmBox(msg){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = getLocalMessage("works.management.proceed");
	message	+='<h4 class=\"text-center text-blue-2 padding-10\">'+msg+'</h4>';
	 message	+='<div class=\'text-center padding-bottom-10\'>'+	
	'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '+ 
	' onclick="BackTenderHome()"/>'+
	'</div>';
	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg'); 
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutClose(errMsgDiv);
	return false;
}

function getApprovedWork(Object){

	var projId = $(Object).val();
	$('.workClass').hide();
	$('.workProj').html('');
	$("#resolutionNo").val('');
	$("#resolutionDate").val('');
	//var deptId =	;
	//var deptCode = $("#deptId").find("option:selected").attr('code');
	if(projId!="" &&  projId!="0"){
		$("#projectCode").val($("#projId").find("option:selected").attr('code'));
		var requestData ={"projId":projId,"deptId":$('#deptId').val(),"deptCode":$("#deptCode").val(),"projCode":
			$("#projId").find("option:selected").attr('code')}
		var response = __doAjaxRequest(TENDER_URL+'?getEstimatedWork', 'POST', requestData, false,'html');
		  if(response=="") {
			 $('#projId').val("").trigger('chosen:updated');
	  		var warnMsg=getLocalMessage("tender.noaprrovedwork.valid");
	  		var message	='<p class="text-blue-2 text-center padding-15">'+ warnMsg+'</p>';
	  		$(childDivName).addClass('ok-msg').removeClass('warn-msg');
	  		$(childDivName).html(message);
	  		$(childDivName).show();
	  		$('#yes').focus();
	  		showModalBox(childDivName);
	  		return false;
		} else{
			$('.workClass').html(response);	
			$('.workClass').show();
			
		    $('[id^="initiated"]').prop('checked',false);
		    
		    // enable estimate button for view estimate
		    $('.viewEstimate').prop("disabled",false);
		  
		    
		    // disabled all fields
		     $('[id^="tenderFeeAmt"]').prop('disabled',true);
		     $('[id^="venderClassId"]').prop('disabled',true);
		     $('[id^="tenderSecAmt"]').prop('disabled',true);
		     $('[id^="vendorWorkPeriodUnit"]').prop('disabled',true);
		     $('[id^="vendorWorkPeriod"]').prop('disabled',true);
		     $('[id^="tenderType"]').prop('disabled',true);
					     
		     var projectDetails = $("#projId").find("option:selected")
					.attr('code').split(",");
			/*
			 * $("#resolutionNo").val(projectDetails[0]);
			 * $("#resolutionDate").val(formatDate(projectDetails[1]));
			 */
			$(".issueFromDatePicker").datepicker("option","minDate", formatDate(projectDetails[1]));
			$(".publishDatePicker").datetimepicker("option","minDate", formatDate(projectDetails[1]));
		} 	
	}
}

function saveTender(formObj){

	var errorList=[];
	  errorList  = validateTenderDetails(errorList);
	if(errorList.length == 0){
	return saveOrUpdateForm(formObj,"", TENDER_URL, 'saveform');
	}else{
			displayErrorsOnPage(errorList);
	}
}

function validateTenderDetails(errorList){

	var deptId = $("#deptId").val();
	var projId = $("#projId").val();
	var tenderCategory = $("#tenderCategory").val();
	
	var resolutionDate = $("#resolutionDate").val();
	var resolutionNo = $("#resolutionNo").val();
	
	var issueFromDate = $("#issueFromDate").val();
	var issueToDate = $("#issueToDate").val();
	var publishDate = $("#publishDate").val();
	var technicalOpenDate = $("#technicalOpenDate").val();
	// var financialeOpenDate = $("#financialeOpenDate").val();
	var tndValidityDay = $("#tndValidityDay").val();
	
	
	
	if(deptId == null ||deptId == ""){
		errorList.push(getLocalMessage('tender.select.dept'));
	}
	var deptCode = $("#deptId").find("option:selected").attr('code');
	if("SD" != deptCode){
		if (projId == null || projId == "") {
			errorList.push(getLocalMessage('work.Def.valid.select.projName'));
		}
	}
	if(tenderCategory == null ||tenderCategory == "0"){
		errorList.push(getLocalMessage('tender.select.category'));
	}
	
	/*if(resolutionNo == null ||resolutionNo == ""){
		errorList.push(getLocalMessage('tender.enter.resolutionNo'));
	}*/
	
	if(resolutionDate == null || resolutionDate == ""){
		errorList.push(getLocalMessage('tender.please.select.resolution.date'));
	}
	
	if(issueFromDate == null ||issueFromDate == ""){
		errorList.push(getLocalMessage('tender.please.select.issue.from.date'));
	}
	
	if(issueToDate == null ||issueToDate == ""){
		errorList.push(getLocalMessage('tender.please.select.issue.todate'));
	}
	
	if(publishDate == null ||publishDate == ""){
		errorList.push(getLocalMessage('tender.please.select.publish.date'));
	}
	
	if(technicalOpenDate == null ||technicalOpenDate == ""){
		errorList.push(getLocalMessage('tender.please.select.technical.open.date'));
	}
	
	/*
	 * if(financialeOpenDate == null ||financialeOpenDate == ""){
	 * errorList.push(getLocalMessage('wms.Pleaseselectfinancialopendate')); }
	 */
	
	if(tndValidityDay == null ||tndValidityDay == ""){
		errorList.push(getLocalMessage('tender.validity.vldn'));
	}
	
  if(errorList.length == 0){
	  errorList  = validateTenderWork(errorList);
  }
	return errorList;
}

function validateTenderWork(errorList){
	var workSeleted = false;
	$(".tenderWork").each(function(i){
		var initiated = $("#initiated"+i).is(':checked');
	  if(initiated){
		  workSeleted= true;
		   var tenderFeeAmt= $("#tenderFeeAmt"+i).val();
		   var venderClassId= $("#venderClassId"+i).val();
		   var tenderSecAmt= $("#tenderSecAmt"+i).val();
		   var vendorWorkPeriodUnit= $("#vendorWorkPeriodUnit"+i).val();
		   var vendorWorkPeriod= $("#vendorWorkPeriod"+i).val();
		   var tenderType = $("#tenderType"+i).val();
			var c = i+1;
			
			if(tenderType == null || tenderType == ""){
				errorList.push(getLocalMessage('wms.please.select.tender.type')  + "  "+c);
			}			
			if(tenderFeeAmt == null ||tenderFeeAmt == ""){
				errorList.push(getLocalMessage('wms.please.enter.tender.fee.amount')  + "   "+c);
			}
			if(venderClassId == null ||venderClassId == ""){
				errorList.push(getLocalMessage('wms.please.select.vendor.class') + "   "+c);
			}
			if(tenderSecAmt == null ||tenderSecAmt == ""){
				errorList.push(getLocalMessage('wms.please.enter.tender.security.deposit.amount') +"   "+c);
			}
			if(vendorWorkPeriodUnit == null ||vendorWorkPeriodUnit == ""){
				errorList.push(getLocalMessage('wms.please.select.work.duration.unit') + "   "+c);
			}
			if(vendorWorkPeriod == null ||vendorWorkPeriod == ""){
				errorList.push(getLocalMessage('wms.please.enter.work.duration') + "   "+c);
			}
	  }else{
		  if($("#mode").val()=='E'){
		  var tndWId = $("#tndWId"+i).val();
		  if(tndWId!= null && tndWId!=""){
			  removeWorkIdArray.push(tndWId);
		  }
		}
	  }
	});
	
	$('#removeWorkIdArray').val(removeWorkIdArray);
	if(!workSeleted){
		errorList.push(getLocalMessage('tender.select.anywork.valid'));
	}
	return errorList;
}



function setAmount(index){
	if($("#initiated"+index).is(':checked')){
		$("#tenderFeeAmt"+index).prop("disabled",false);
		$("#venderClassId"+index).prop("disabled",false);
		$("#tenderSecAmt"+index).prop("disabled",false);
		$("#vendorWorkPeriodUnit"+index).prop("disabled",false);
		$("#vendorWorkPeriod"+index).prop("disabled",false);
		$("#tenderType"+index).prop("disabled",false);
		var deptCode = $("#deptId").find("option:selected").attr('code');
		if("SD" != deptCode){
		 filterVendorClass($("#workEstimateAmt" + index).val(), index); // used
		}														// to
																		// filter
																		// vendor
																		// class
																		// based
																		// on
																		// amount
	}else{
		$("#tenderFeeAmt"+index).val("").prop("disabled",true);
		$("#venderClassId"+index).val("").prop("disabled",true);
		$("#tenderSecAmt"+index).val("").prop("disabled",true);
		$("#vendorWorkPeriodUnit"+index).val("").prop("disabled",true);
		$("#vendorWorkPeriod"+index).val("").prop("disabled",true);
		$("#tenderType"+index).val("").prop("disabled",true);
	};
	
	$("#totalAmt").val("0.00");
	
	 var amount=0; 
	$(".tenderWork").each(function(i){
	var initiated = $("#initiated"+i).is(':checked');
	  if(initiated){
		  var  n= parseFloat(parseFloat($("#workEstimateAmt" + i).val()));
		    if (isNaN(n)) {
		        return n=0;
		    }
		    amount += n ;
    	var result = amount.toFixed(2);
	   $("#totalAmt").val(result);
	  }
	});
}



function updateTender(tndId){
	 var requestData = 'tndId='+tndId;
	 var response = __doAjaxRequest(TENDER_URL+'?updateTender', 'POST', requestData, false,'html');
	 $('.content-page').html(response);	
	
}


function saveupdatedTender(formObj){

	var errorList=[];
	 errorList  = validateUpdateTenderDetails(errorList);
	if(errorList.length == 0){
	return saveOrUpdateForm(formObj,"", TENDER_URL, 'saveUpdateTender');
	}else{
		displayErrorsOnPage(errorList);
	}
}


function validateUpdateTenderDetails(errorList){

	var projId = $("#projId").val();
	var tenderNo = $("#tenderNo").val();
	var issueFromDate =  $("#issueFromDate").val().split('/');
	var tenderDate = $("#tenderDate").val().split('/');
	var tenderSubDate = $("#tenderSubDate").val().split('/');
	var date1 = new Date(tenderDate[2], tenderDate[1] - 1, tenderDate[0]);
	var date2 = new Date(tenderSubDate[2], tenderSubDate[1] - 1, tenderSubDate[0]);
	
	if (tenderSubDate < issueFromDate) {
		errorList.push(getLocalMessage("work.Def.valid.loadate.issue"));
	}
	
	if (date1 < date2) {
		errorList.push(getLocalMessage("work.Def.valid.loadate.sub"));
	}
	
	var deptCode = $("#deptId").find("option:selected").attr('code');
	if("SD" != deptCode){
		if (projId == null || projId == "") {
			errorList.push(getLocalMessage('work.Def.valid.select.projName'));
		}
	}
	if(tenderNo == null ||tenderNo == ""){
		errorList.push(getLocalMessage('tender.enter.tenderNo'));
	}
	if(tenderDate == null ||tenderDate == ""){
		errorList.push(getLocalMessage('tender.select.tenderdate'));
	}
	var count=0;
	$(".tenderWorkUpdate").each(function(i){
		
		var tenderType = $("#tenderType"+i).val();
		var tenderValue = $("#tenderValue"+i).val();
		var vendorId = $("#vendorId"+i).val();
		/*
		 * Removed Field AS per SUDA UAT perspective 27/10/2018
		 * 
		 */
		// var tenderStampFee = $("#tenderStampFee"+i).val();
		var tenderNoOfDayAggremnt = $("#tenderNoOfDayAggremnt"+i).val();
		
		var c= i+1;
		if(tenderType == null ||tenderType == ""){
			errorList.push(getLocalMessage('wms.Pleaseselecttendertype')+c);
		}
		if(tenderValue == null ||tenderValue == ""){
			errorList.push(getLocalMessage('wms.PleaseselecttenderValue')+c);
		}
		if(vendorId == null ||vendorId == ""){
			errorList.push(getLocalMessage('wms.Pleaseselectvendor')+c);
		}
		/*
		 * if(tenderStampFee == null ||tenderStampFee == ""){
		 * errorList.push(getLocalMessage('Stamp Paper Fee Must not be
		 * Empty-'+c)); }
		 */
		if(tenderNoOfDayAggremnt == null ||tenderNoOfDayAggremnt == ""){
			errorList.push(getLocalMessage('wms.NoOfDaysForAggrementNotEmpty')+c);
		}
		
	});
	return errorList;
}


function openVendorMas(){
	var VendorURL='Vendormaster.html'
		$("#postMethodForm").prop('action', '');
		$("#postMethodForm").prop('action',VendorURL);
		$("#postMethodForm").submit();
}
function BackTenderHome(){
		$("#postMethodForm").prop('action', '');
		$("#postMethodForm").prop('action',TENDER_URL);
		$("#postMethodForm").submit();
}


function viewWorkEstimate(workId){
	var divName = '.content-page';
	var url = TENDER_URL+"?openEstimation";
	var requestData = $('#TenderInitiation').serialize()+"&workId="+workId;
	var ajaxResponse = __doAjaxRequest(url, 'POST', requestData, false, 'html');
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	getSorByValue();
}

function getSorByValue() {
	var actionParam = {
		'actionParam' : $("#sorList").val(),
	    'sorId' : $("#sorId").val()
	}
	var url = "WorkEstimate.html?selectAllSorData";
	var ajaxResponse = __doAjaxRequest(url, 'POST', actionParam, false, 'html');
	$("#estimationTagDiv").html(ajaxResponse);
	prepareTags();
}


/*function printLoa(tndId){
	//#81911
	 var requestData = {
		'tndId' : tndId,
		"loaMode" : "P",
		"mode" : "V"	
	}
	var response = __doAjaxRequest(TENDER_URL + '?updateTender', 'POST',
			requestData, false, 'html');
	$('.content-page').html(response);	
}*/

/*function printLoaByTenderDetislId(tndId){

	var requestData = {
			'tndId' : tndId,
	}
	var response = __doAjaxRequest(TENDER_URL + '?generateAndPrintLOA', 'POST',
			requestData, false, 'html');
	$('.content-page').html(response);	
}*/

function calculateTotalAmount(index) {
	var tenderType = $("#tenderType" + index).find("option:selected").attr('code');
	var tenderValue = $("#tenderValue" + index).val();
	var workEstimateAmt = $("#workEstimateAmt" + index).val();
	var tendTypePercent = $("#tendTypePercent" + index).find("option:selected").attr("code");
	var totalTenderAmount="";
	
	 if(tenderType == "PER"){	
		  $("#tendTypePercent" + index).prop("disabled", false);	
		  }else{ $("#tendTypePercent" + index).prop("disabled", true);
		  $('#tendTypePercent'+ index).prop('selectedIndex',0);}
	 
	if (tenderType != "" && tenderValue != "" && workEstimateAmt != "") {
		if (tenderType == "PER") {	
			if (tendTypePercent == "B") {
			totalTenderAmount = (Number(workEstimateAmt * Number(100 - Number(tenderValue)) / 100)).toFixed(2);
			$("#totalTenderAmount" + index).val(totalTenderAmount);
			}
			else{
				totalTenderAmount = (Number(workEstimateAmt * Number(100 + Number(tenderValue)) / 100)).toFixed(2);
				$("#totalTenderAmount" + index).val(totalTenderAmount);
			}
		 }else if (tenderType == "AMT") {
			$("#totalTenderAmount" + index).val(tenderValue);
		} else if (tenderType == "MUL") {
			$("#totalTenderAmount" + index).val((workEstimateAmt*tenderValue).toFixed(2));
		}
	}

}

function formatDate(date) {
	var parts = date.split("-");
	var formattedDate=parts[2]+"/"+parts[1]+"/"+parts[0];
	return formattedDate;
}

function printNoticeInvitingTender(tndAndWorkId){
	
	var requestData = {
			'tndAndWorkId' : tndAndWorkId,
	}
	var response = __doAjaxRequest(TENDER_URL + '?printNoticeInvitingTender', 'POST',
			requestData, false, 'html');
	$('.content-page').html(response);	
}
function preQualificationDocument(tndAndWorkId){
	
	var requestData = {
			'tndAndWorkId' : tndAndWorkId,
	}
	var response = __doAjaxRequest(TENDER_URL + '?preQualificationDocument', 'POST',
			requestData, false, 'html');
	$('.content-page').html(response);	
}

/*
 * function printFormAandB(workId ,flagForms){
 * 
 * var requestData = { 'workId' : workId, 'flagForms' : flagForms, } var
 * response = __doAjaxRequest(TENDER_URL + '?printFormAandB', 'POST',
 * requestData, false, 'html'); $('.content-page').html(response); }
 */

function filterVendorClass(estimatedAmount, index) {

	$('#venderClassId' + index).html('');
	$('#venderClassId' + index).append(
			$("<option></option>").attr("value", "").text(
					getLocalMessage('selectdropdown'))).trigger(
			'chosen:updated');

	var requestData = {
		"estimatedAmount" : estimatedAmount
	}
	var vendorClassList = __doAjaxRequest(TENDER_URL + '?filterVendorClass',
			'POST', requestData, false, 'json');
	$.each(vendorClassList, function(i, value) {
		$('#venderClassId' + index).append(
				$("<option></option>").attr("value", value.lookUpId).attr("code",
						value.lookUpCode).text(
						value.descLangFirst));
	});
	$('#venderClassId' + index).trigger('chosen:updated');
	
	
	/*
	 * var rateMasterModel = __doAjaxRequest(TENDER_URL + '?getEmdAmount',
	 * 'POST', requestData, false, 'json'); if (rateMasterModel != null &&
	 * rateMasterModel.emdValue!="") { $('#tenderSecAmt' +
	 * index).val((rateMasterModel.emdValue).toFixed(2)); $('#tenderSecAmt' +
	 * index).prop("readOnly", true); } else { var errorList = [];
	 * errorList.push('wms.tender.emd.vldn'); displayErrorsOnPage(errorList); }
	 */
	
    var urlTndFees = "TenderInitiation.html?getTndFees";   
    var onSuccessCallback = function(responsetndFees){
    	
    	
			
    	if (responsetndFees != null && responsetndFees.flatRate!="" && responsetndFees.flatRate!="0") {
			$('#tenderFeeAmt' + index).val((responsetndFees.flatRate).toFixed(2));
			$('#tenderFeeAmt' + index).prop("readOnly", true);
		} else {
			//defect #78115
			if(responsetndFees != null && responsetndFees.flatRate=="0"){
				$('#tenderFeeAmt' + index).val("0.00");
				$('#tenderFeeAmt' + index).prop("readOnly", true);	
			}else{
				var errorList = [];
				errorList.push('wms.tender.fees.vldn');
				displayErrorsOnPage(errorList);
			}
			
		}
    };
    
    var onErrorCallback = function(responsetndFees) {
		
		var errMsgDiv = '.msg-dialog-box';
		var message = '';
		var cls = '';
		var sMsg = '';
		var Proceed = getLocalMessage("works.management.proceed");
		sMsg = getLocalMessage("Rule Sheet is Not Defined for tender fees on this Organisation");
		sMsg1 = getLocalMessage("Please Contact Administrator.");
		message += '<div class="text-center padding-top-25">' +'<p class="text-center red padding-12" style="margin-right:10px; margin-left:10px;">' + sMsg + '</p>'
					+'<p class="text-center red padding-12 padding-top-5" style="margin-right:10px; margin-left:10px;"  >' + sMsg1 + '</p>'+'</div>';
		
		message += '<div class="text-center padding-bottom-10 padding-top-10">'
				+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''+Proceed+'\'  id="Proceed" '
				+ 'onclick="redirectHome();"/>' + '</div>';
		
		$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
		$(errMsgDiv).html(message);
		$(errMsgDiv).show();
		$('#btnNo').focus();
		showModalBoxWithoutClose(errMsgDiv);		
		return false;
	};
	
	var responsetndFees = __doAjaxRequestWithCallback(urlTndFees, 'POST', requestData, false, 'json',onSuccessCallback ,onErrorCallback);
	
	if($('#tenderFeeAmt' + index).val() != null && $('#tenderFeeAmt' + index).val() != ''){		
		var url = "TenderInitiation.html?getEmdAmount";
		var onSuccessCallback = function(response) {
			
			if (response != null && response.emdValue!="") {
				$('#tenderSecAmt' + index).val((response.emdValue).toFixed(2));
				$('#tenderSecAmt' + index).prop("readOnly", true);
			} else {
				var errorList = [];
				errorList.push('wms.tender.emd.vldn');
				displayErrorsOnPage(errorList);
			}
		};

		var onErrorCallback = function(response) {
			
			var errMsgDiv = '.msg-dialog-box';
			var message = '';
			var cls = '';
			var sMsg = '';
			var Proceed = getLocalMessage("works.management.proceed");
			sMsg = getLocalMessage("Rule Sheet is Not Defined on this Organisation");
			sMsg1 = getLocalMessage("Please Contact Administrator.");
			message += '<div class="text-center padding-top-25">' +'<p class="text-center red padding-12" style="margin-right:10px; margin-left:10px;">' + sMsg + '</p>'
						+'<p class="text-center red padding-12 padding-top-5" style="margin-right:10px; margin-left:10px;"  >' + sMsg1 + '</p>'+'</div>';
			
			message += '<div class="text-center padding-bottom-10 padding-top-10">'
					+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''+Proceed+'\'  id="Proceed" '
					+ 'onclick="redirectHome();"/>' + '</div>';
			
			$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
			$(errMsgDiv).html(message);
			$(errMsgDiv).show();
			$('#btnNo').focus();
			showModalBoxWithoutClose(errMsgDiv);		
			return false;
		};	
		var response = __doAjaxRequestWithCallback(url, 'POST', requestData, false, 'json',onSuccessCallback ,onErrorCallback);	
	}
	
}

function redirectHome(){
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', 'TenderInitiation.html');
	$("#postMethodForm").submit();
}

function printTenderFormReport(workId ,flagForms){
	
	var requestData = {
			'workId' : workId,
			'flagForms'   : flagForms,
	}
	var response = __doAjaxRequest(TENDER_URL + '?printFormAandB', 'POST',
			requestData, false, 'html');
	$('.content-page').html(response);	
}

function showApplicableChecklist(){
	
	var theForm = '#TenderInitiation';
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = TENDER_URL+'?getApplicableCheckListAndCharges';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
			'html');
	if (returnData != null) {
		$('.content-page').html(returnData);
		getProjects();
		if ($('#project').val() != '') {
			$("#projId option[value='" + $('#project').val() + "']").prop(
					'selected', 'selected');
			$('#projId').trigger("chosen:updated");
		}
	}
}

function closeSend(){
	$.fancybox.close();
}
