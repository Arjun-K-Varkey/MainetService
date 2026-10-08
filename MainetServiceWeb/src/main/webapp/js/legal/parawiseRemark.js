$(document).ready(function() {
	/*$('#ParawiseRemarkForm').validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}
	});*/

	$("#id_parawiseRemarkTbl").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	
	$( ".uadParComment" ).each(function() {
		  if($( this ).val()==""){
			  $( this ).prop("disabled", false);
		  }else{
			  $( this ).prop("disabled", true);
		  }
		});
		 
	});


function Proceed(element) {
	var errorList = [];
	errorList = validatePara(errorList);
	errorList = validate(errorList);	
	if (errorList.length > 0) {
		$("#errorDiv").show();
		displayErrorsOnPage(errorList);

	} else {
		$("#errorDiv").hide();
		return saveOrUpdateForm(element,
				getLocalMessage('lgl.saveCaseEntry'), 'ParawiseRemark.html',
		'saveform');
	}
}

function searchParawiseRemark(formUrl, actionParam) {
	var data = {
			"cseSuitNo" : $("#cseSuitNo").val(),
			"cseDeptid" : $("#cseDeptid").val(),
			"cseDate" : $("#cseDate").val(),
			"cseCatId1" : replaceZero($("#cseCatId1").val()),
			"cseCatId2" : replaceZero($("#cseCatId2").val()),
			"cseTypId" : replaceZero($("#cseTypId").val()),
			"crtId" : replaceZero($("#crtId").val()),

		};
	
	
	var divName = '.content-page';
	var formUrl = "ParawiseRemark.html?searchParawiseRemark";
	var ajaxResponse = doAjaxLoading(formUrl, data, 'html',divName);
	$('.content').removeClass('ajaxloader');		
	$(divName).html(ajaxResponse);
	prepareTags();
}
function replaceZero(value) {
	return value != 0 ? value : undefined;
}
function modifyRemark(cseId, formUrl, actionParam, mode) {
	var divName = '.content-page';
	var requestData = {
			"mode" : mode,
			"id" : cseId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
};

function validate() {
	var errorList = [];

	$(".appendableClass").each(function(i) {
		
		var parPagno=$("#parPagno"+i).val();
		var parSectionno=$("#parSectionno"+i).val();
			
		var rowCount = i + 1;
		
		if(parPagno == "" || parPagno == null || parPagno == 'undefined')
			errorList.push(getLocalMessage("lgl.validation.parUadpagno")+ rowCount);
		
		if(parSectionno == "" || parSectionno == null || parSectionno == 'undefined')
			errorList.push(getLocalMessage("lgl.validation.parUadSectionno")+ rowCount);
		
	
	});
	return errorList;
}


function addData() {
	$("#errorDiv").hide();
	var errorList = [];
	errorList = validate(errorList);
	if (errorList.length == 0) {
		if (errorList.length == 0){
			addTableRow('parawiseRemarkDetails');
		}
	} 
	if (errorList.length > 0) {
		//$('#parawiseRemarkDetails').DataTable();
		displayErrorsOnPage(errorList);
	}
}

// #136459
function deleteEntry(obj, ids) {
	var totalWeight = 0;
	deleteTableRow('parawiseRemarkDetails', obj, ids);
	$('#parawiseRemarkDetails').DataTable().destroy();
	triggerTable();
	
}

function addDataPara() {
	$("#errorDiv").hide();
	var errorList = [];
	errorList = validatePara(errorList);
	if (errorList.length == 0) {
		
		
		if (errorList.length == 0){
			addTableRow('parawiseRemarkDetailsuad');
			
		}
	} 	
	if (errorList.length > 0) {
		$('#parawiseRemarkDetailsuad').DataTable();
		displayErrorsOnPage(errorList);
	}
	
}

function validatePara(){
	var errorList = [];

	$("#parawiseRemarkDetailsuad tbody .appendableClassPara").each(function(i) {
			
		var parPagno=$("#parUadPagno"+i).val();
		var parSectionno=$("#parUadSectionno"+i).val();
		
		var rowCount = i + 1;
		
		if(parPagno == "" || parPagno == null || parPagno == 'undefined')
			errorList.push(getLocalMessage("lgl.validation.parPagno")+ rowCount);
		
		if(parSectionno == "" || parSectionno == null || parSectionno == 'undefined')
			errorList.push(getLocalMessage("lgl.validation.parSectionno")+ rowCount);
		
	
	});
	return errorList;
	
}
