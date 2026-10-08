$(document).ready(function() {
	
	var assType=$("#assType").val();
	var authLevel=$("#authLevel").val();
	if('A' == $("#assType").val() && 'true' == $("#authLevel").val()){
		$("#changeProceed").show();
	} else if("Y" == $("#checklistApplicable").val()){
		$("#changeProceed").show();
	}else{
		$("#changeProceed").hide();		
	}
	$("#backToSearch").hide();
		$("#backToSearch").on("click", function(){ 
			  window.location.reload("#propertyAssessmentType");
			});
		
		var locId=$("#locId").val();
		if(locId!=null && locId!='' && locId!=undefined && locId>0){
			var result=	getOperationalWardZone($("#orgId").val(),$("#deptId").val(),$("#locId").val());
			$("#wardZone").html(result);
		}
	yearLength();
	
	$('#checkDetail').prop('checked', true);
	if('EDIT' == assType){
		$("#changeProceed").show();
		$("#backToSearch").show();
		$("#checkList").hide();
		$("#backToSearchPage").hide();
	}
	//occupancyTypeChange1();
	
	/*var landType = $("#assdNatureOfproperty1" + " option:selected").attr("code");
	if(landType=='LAND' || landType=='Land'){
		$('#surveyty').show();
	}else{
		$('#surveyty').hide();		
	}
	$("#assdNatureOfproperty1").on('change', function(){
		var landTyp = $("#assdNatureOfproperty1" + " option:selected").attr("code");
		if(landTyp=='LAND' || landTyp=='Land'){
			$("#surveyty").show();
		}else{
			$("#surveyty").hide();
		}
	});*/
});

function backToSearchPageAssess(element){ 

	var theForm	=	'#frmChangeAssessmentForm';
	var requestData = {};
	var URL = 'ChangeInAssessmentForm.html?backToSearch';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).html(returnData);
}

function backToPropertyEditableForm(element){ 

	var theForm	=	'#frmChangeAssessmentForm';
	var requestData = {};
	var URL = 'PropertyEditableForm.html?backToSearch';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
	var divName = '.content-page';
	$(divName).html(returnData);
}
function yearLength(){	
	var dateFields = $('.dateClass');
    dateFields.each(function () {
    	
            var fieldValue = $(this).val();
            if (fieldValue.length > 10) {
                    $(this).val(fieldValue.substr(0, 10));
            }
    })
}

/*function occupancyTypeChange1(){
	$('.secondUnitRow').each(function(j){
		if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
			$("#proAssdAnnualRent"+j).prop('disabled', false);;
			$("#proAssdAnnualRent"+j).prop('required',true);
			
		}else{
			$("#proAssdAnnualRent"+j).val('');
			$("#proAssdAnnualRent"+j).prop('disabled', true);;
			$("#proAssdAnnualRent"+j).prop('required',false);
		}
	});
}*/
function proceed(element){
	var errorList = [];
	errorList = ValidateDetails(errorList);	
	errorList = validateUnitDetailTable(errorList);					
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table

	if (errorList.length == 0) {
			
		var theForm	=	'#frmChangeAssessmentForm';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'ChangeInAssessmentForm.html?proceed';
		
		var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			$(formDivName).html(returnData);
			reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
			//reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
			reorderfactor('.specFact','.eachFact'); // reordering Id and Path for unit specific Additional Info Table
			//occupancyTypeChange1();   
			
			$("#changeProceed").show();
			$("#backToSearch").show();
			$("#checkList").hide();			
			$("#backToSearchPage").hide();
		}
		yearLength();
		}	
	else {
		displayErrorsOnPage(errorList);
	}
}


function fetchCheckList(element) {
	showloader(true);
	setTimeout(function() {
		var errorList = [];
		errorList = validateMasterDetails(errorList);
		errorList = ValidateDetails(errorList);
		errorList = validateUnitDetailTable(errorList);
		errorList = validateUnitSpecificInfoTable(errorList); // validate unit
																// specific
																// additional
																// info table

		if (errorList.length == 0) {

			var theForm = '#frmChangeAssessmentForm';
			var requestData = {};
			requestData = __serializeForm(theForm);
			var URL = 'ChangeInAssessmentForm.html?getCheckList';

			var returnData = __doAjaxRequestValidationAccor(element, URL,
					'POST', requestData, false, 'html');
			if (returnData) {
				$(formDivName).html(returnData);
				reOrderChangeAssessUnitTabIdSequence('.firstUnitRow',
						'.secondUnitRow'); // reordering Id and Path for unit
											// details table
				// reOrderJointOwnerTableSequence('.jointOwner'); // reordering
				// Id and Path for joint owner table
				reorderfactor('.specFact', '.eachFact'); // reordering Id and
															// Path for unit
															// specific
															// Additional Info
															// Table
				// occupancyTypeChange1();
				$("#changeProceed").show();
				$("#backToSearch").show();
				var assType = $("#assType").val();
				if ('Y' == assType) {
					$("#checkList").show();
					$("#backToSearchPage").show();
				} else {
					$("#checkList").hide();
					$("#backToSearchPage").hide();
				}
			}
			yearLength();
		} else {
			displayErrorsOnPage(errorList);
		}
	}, 2000);
}

function validateMasterDetails(errorList){

	var remarks=$("#remarks").val();
	var ward=$('#assWard1 option:selected').attr('code');
	var zone=$('#assWard2 option:selected').attr('code');
	var roadType=$('#propLvlRoadType option:selected').attr('code');
	var billMethod=$('#billMethod option:selected').attr('code');
	var proAssAcqDate=$("#proAssAcqDate").val();
	var totalplot=$("#totalplot").val();
	var propAddress=$("#assAddress").val();
	var propVillage=$('#areaName').val();
	var clusterNo=$('#clusterNo').val();
	var assLandType=$('#assLandType').val();
	var natureOfproperty=$('#assdNatureOfproperty1').val();
	var surveyType=$("#surveyType").val();
	var tppSurveyNumber=$("#tppSurveyNumber").val();
	var draftApplication=$("#draftApplication").val();
	
	var lastAssessmentDate=$("#lastAssessmentDate").val();
	if((draftApplication != 'Y') && (remarks == "" || remarks == null || remarks == undefined)){
		errorList.push(getLocalMessage("property.enter.remark"));
	}
	if(roadType=="" || roadType=='0' || roadType == undefined){
		errorList.push(getLocalMessage("property.valid.roadType"));
	}
	var landType = $("#assdNatureOfproperty1" + " option:selected").attr("code");
	if(landType=='LAND' || landType=='Land'){
		if(surveyType=="" || surveyType=='0' || surveyType == undefined){
			errorList.push(getLocalMessage("property.valid.surveyType"));
		}
		if(tppSurveyNumber == '' || tppSurveyNumber == null || tppSurveyNumber == undefined){
			errorList.push(getLocalMessage("property.valid.survey.citiSurveyNo"));
		} 
	}
	if((draftApplication != 'Y') && (lastAssessmentDate=="" || lastAssessmentDate=='0' || lastAssessmentDate == undefined)){
		errorList.push(getLocalMessage("propertydetails.valid.lastAssessmentDate"));
	}
	
	if(FutureDate(lastAssessmentDate)){
		errorList.push(getLocalMessage("propertydetails.valid.futureDate"));
	}
	if(billMethod=="" || billMethod=='0' || billMethod == undefined){
		errorList.push(getLocalMessage("property.valid.billMethod"));
	}
	/*if(zone=="" || zone=='0' || zone == undefined){
		errorList.push(getLocalMessage("property.valid.zone"));
	}
	if(proAssAcqDate=="" || proAssAcqDate=='0' || proAssAcqDate == undefined){
		errorList.push(getLocalMessage("property.valid.firstAssessmentDate"));
	}
	if(propAddress=="" || propAddress==null || propAddress == undefined){
		errorList.push(getLocalMessage("property.valid.propAddress"));
	}
	if(propVillage=="" || propVillage=='0' || propVillage == undefined){
		errorList.push(getLocalMessage("property.valid.proparea"));
	}*/
	if(natureOfproperty=="" || natureOfproperty=='0' || natureOfproperty == undefined){
		errorList.push(getLocalMessage("property.valid.propertyType"));
	}
	
	return errorList;
}

function FutureDate(userDate){
 	var today = new Date().getTime(),
	userDate = userDate.split("/");
	userDate = new Date(userDate [2], userDate [1] - 1, userDate [0]).getTime();
 	return (today - userDate) < 0;
}

function ValidateDetails(errorList){
var noOfDaysEditableFlag = $("#noOfDaysEditableFlag").val();
if(noOfDaysEditableFlag != '' && noOfDaysEditableFlag == 'Y'){
	if($("#checkDetail").prop('checked')==false){
		  errorList.push(getLocalMessage('prop.toProceed.check.unitDetails')); 
	}
}
	var totalPlot =parseFloat($("#totalplot").val());
	var buildup =parseFloat($("#buildup").val());

	if(totalPlot=='' || totalPlot == undefined || (totalPlot<=0)){			 
		 errorList.push(getLocalMessage("property.enterTotalPlotArea")); 
	}
	if(buildup=='' || buildup == undefined || (buildup<=0)){			 
		 errorList.push(getLocalMessage("property.enterBuildUpArea")); 
	}
	if(buildup > totalPlot){	
		errorList.push(getLocalMessage("property.totalPloatArea"));
	}
	return errorList;
}


function editChangeAssForm(obj){

	//var ownerType = $("#ownershipId").val();
	var data = {};
	var URL = 'ChangeInAssessmentForm.html?editChangeAssForm';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	
	// For Assessment Type
	if(($('option:selected', $("#selectedAssType")).attr('code')) == 'C'){
	$(".content").html(returnData);
	}	
	$(formDivName).html(returnData);
	
	reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
	//reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table

	reorderfactor('.specFact','.eachFact'); // reordering Id and Path for unit specific Additional Info Table
	//occupancyTypeChange1();   
	yearLength();
	$("#changeProceed").show();
	$("#backToSearch").show();
	$("#checkList").hide();
	$("#backToSearchPage").hide();
	
}

// fetch API details on selection of button 
function getLandApiDetails(obj){
	var landTypePrefix=$(".landValue").val();
	var data = {};
	var URL = 'ChangeInAssessmentForm.html?getLandTypeApiDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);		
	$("#showAuthApiDetails").html(returnData);
	$("#showAuthApiDetails").show();		
}

function loadOccupierName(element){
	
	 var occupancyTypeId = $(element).attr('id');
	 var index = occupancyTypeId.charAt(occupancyTypeId.length - 1);
	 
	var value = null;
	if((index != 'e') && (index > '0' || index == '0')){
		value = $("#assdOccupancyType" + index).find("option:selected").attr('code');
	}else{
		value = $("#assdOccupancyType").find("option:selected").attr('code');
	}
	
	var ownerTypeCode = $("#ownerTypeId").find("option:selected").attr('code');
	
	if(value == 'OWN'){
		var ownerName = null;
		if(ownerTypeCode == 'JO'){
			ownerName = $("#assoOwnerName_0").val();
		}else{
			ownerName = $("#assoOwnerName").val();
		}
		
		
		if((index != 'e') && (index > '0' || index == '0')){
			$("#occupierName" + index).val(ownerName);
		}else{
			$("#occupierName").val(ownerName);
		}
	}else{
		if((index != 'e') && (index > '0' || index == '0')){
			$("#occupierName" + index).val('');
		}else{
			$("#occupierName").val('');
		}
		
	}
}

function getOwnerTypeDetails() {

	var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
	if (ownerType != undefined) {
		var data = {
			"ownershipType" : ownerType
		};
		var URL = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		clearOccupancyDetails();
		$("#owner").html(returnData);
		$("#owner").show();
		$("#proceed").hide();
		$("#checkList").show();
		$("#checkListDiv").hide();
	} else {
		$("#owner").html("");
	}
}

function clearOccupancyDetails() {

	var rowCount = $('#unitDetailTable tr').length;
	if (rowCount > 3) {
		for (var i = 0; i < rowCount; i++) {
			$("#occupierName" + i).val('');
			$('#assdOccupancyType' + i).val('0').trigger('chosen:updated');
		}
	} else {
		$("#occupierName").val('');
		$('#assdOccupancyType').val('0').trigger('chosen:updated');
	}

}
jQuery(function() {
    $('input.dateFrmt').datepicker({changeMonth: true, changeYear: true}).
          mask('99/99/9999');
});


function getDataForNextPage(obj,pageNo) {
	$(".error-msg").hide();
		var errorList = [];
			
		if (errorList.length == 0)
		{
			
			var data = {"pageNo" : pageNo};
			var returnData1 = __doAjaxRequest('ChangeInAssessmentForm.html?getPageCount', 'POST', data, false);	
			var action = "ChangeInAssessmentForm.html";
			var theForm = '#frmChangeAssessmentForm';
			requestData = __serializeForm(theForm);
			var URL = 'ChangeInAssessmentForm.html?getDataForNextPage';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			//$(formDivName).html(returnData);
			var divName = '.content-page';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
			reorderfactor('.specFact','.eachFact');
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
 }
