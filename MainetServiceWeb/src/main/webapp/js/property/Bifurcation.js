$(function(){
	var minYear= $("#proAssAcqDate").val();
	//datepicker function for year of acquisition
	$('.lessthancurrdate').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		minDate: minYear,
		maxDate: '-0d',
		yearRange: "-100:-0",
	});
	$("#showFlatNo").hide();
	$("#serchBtnDirect").hide();

prepareDateTag();
if("Y" == $("#checklistApplicable").val()){
	$("#proceedBifu").show();
}else{
	$("#proceedBifu").hide();	
	$("#backButton2").hide();	
}
if("N" == $("#checklistApplicable").val()){
	$("#checkListBifu").show();
	$("#backButton").show();
}
	
});

function prepareDateTag() {
	var dateFields = $('.lessthancurrdate');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}

function SearchButton(element) {
	var errorList = [];
	errorList = ValidateInfo(errorList);	
	if (errorList.length == 0) {
		var data = $('#BifurcationFormSearch').serialize();
		var URL = 'BifurcationForm.html?searchProperty';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$(formDivName).html(returnData);
		reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');

		if($("#ownerTypeId").val()>0){
			var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			var data1 = {"ownershipType" : ownerType};
			var URL1 = 'BifurcationForm.html?getOwnershipTypeInfoDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
			}
		
	}
	else {
		displayError(errorList);
	}
}
function redirectToSearchPropNo(obj) {

	var errorList = [];
	var propNo = $("#assNo").val();
	if (propNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#assNo").val();
		var requestData = {
			"propNo" : propNo
		};
		var ajaxResponse = doAjaxLoading('BifurcationForm.html?getBillingMethod',
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
			$("#billingMethod").val('I');
		} else {
			SearchButton();
		}
	} else {
		displayError(errorList);
	}
}

function ValidateInfo(errorList){

	var parentProNo = $("#assNo").val();
	var parentOldPropNo=$("#assOldpropno").val();
	if((parentProNo === "" && parentOldPropNo==="")){
		errorList.push(getLocalMessage("prop.enter.valid.propertyNo.oldpropertyNo"));
	}
	return errorList;
}


function proceed(element){
	var errorList = [];
    errorList = ValidateBuildUpArea(errorList);
	//errorList = ValidateDetails(errorList);
    errorList = ValidateRVArea(errorList);
	errorList = validateBifurcationUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateBIFFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {
			
		var theForm	=	'#BifurcationFormSearch';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'BifurcationForm.html?proceed';
		
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			$(formDivName).html(returnData);
			reorderFirstRow();
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
			reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
			//occupancyTypeChange1();   
			yearLength();
			reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
			//var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			var ownerType = $("#ownershipId").val();

			//$(formDivName).html(returnData);
			var data1 = {"ownershipType" : ownerType};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			
			$("#owner").html(returnData1);
			$("#owner").show();
			$('#checkDetail').prop('checked', true);
			$("#proceedBifu").show();
			$("#checkListBifu").hide();	
			$("#backButton").hide();
		}
		yearLength();
		}	
	else {
		displayErrorsOnPage(errorList);
	}
}

//$("#smAppliChargeFlag").prop('checked') == false
$(function() {
	if ($('#assdBifurcateNo').prop('checked' == true)) {
		var a = $("#assdBifurcateNo").val("Y");
	} else if ($('#assdBifurcateNo').prop('checked' == false)) {
		var b=$("#assdBifurcateNo").val("N");
	}
});

function backButtons(element){

	var data = {};
	var URL = 'BifurcationForm.html?backToSearchPage';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);	
}

function getBuferCheckList(element){

	var errorList = [];
    errorList = ValidateBuildUpArea(errorList);
	//errorList = ValidateDetails(errorList);
    errorList = ValidateRVArea(errorList);
	errorList = validateBifurcationUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateBIFFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {
		var theForm	=	'#BifurcationFormSearch';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'BifurcationForm.html?getCheckList';
		
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			$(formDivName).html(returnData);
			reorderFirstRow();
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
			reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
			
			//occupancyTypeChange1();   
			yearLength();
			
			reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
			var ownerType = $("#ownershipId").val();

			//var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			var data1 = {"ownershipType" : ownerType};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			
			$("#owner").html(returnData1);
			$("#owner").show();
			$('#checkDetail').prop('checked', true);
			$("#proceedBifu").show();
			$("#checkListBifu").hide();
			$("#backButton").hide();
		}
		yearLength();
		}	
	else {
		displayErrorsOnPage(errorList);
	}
}

function compareBeforeAfterAuthBifurcation(element){
	var errorList = [];
	//errorList = ValidateDetails(errorList);	
	errorList = validateBifurcationUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateBIFFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {
			
		var theForm	=	'#BifurcationFormSearch';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'BifurcationForm.html?compareBeforeAfterAuth';
		
		var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			$(".content").html(returnData);
		}
		yearLength();
		}	
	else {
		displayErrorsOnPage(errorList);
	}
}


function ValidateDetails(errorList){

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


function editDetails(obj){
	
var formName	=	findClosestElementId(obj, 'form');
	
	var theForm	=	'#'+formName;
	
	var formAction	=	$(theForm).attr('action');	
	
	var data = {};
	var URL = formAction+'?editBifurcationUpdateDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
}

function displayError(errorList){
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
function ValidateBuildUpArea(errorList){
	var totalHiddenBuildupArea =  Number($("#totalBuildUpArea").val()).toFixed(2);
	
	var totalBuildUpArea=0;
	//var totalCarpetArea=0;
	$('.firstUnitRow').each(function(i) {
	
	 let buildup =Number($("#taxableArea"+i).val());
	 totalBuildUpArea  += buildup ;
	 
//	 let carpet = Number($("#carpetArea"+i).val());
//	 totalCarpetArea += carpet;
	 
	});
	var totBuildUpArea = Number(totalBuildUpArea).toFixed(2);	
	if(totalHiddenBuildupArea != totBuildUpArea){
		errorList.push(getLocalMessage("property.bif.builduparea.valid"));
	}
//	if(totalCarpetArea > totalHiddenBuildupArea){
//		errorList.push(getLocalMessage("property.bif.carpetarea.valid"));
//	}
	var unitNo = {};
	duplicateunitNo = false;
		$('.firstUnitRow').each(function(j){
			var tdContent = $("#flatNo"+j).val()
			if(unitNo[tdContent]){
				duplicateunitNo = true;
				return false;
			}
			unitNo[tdContent] = true;
		});
		if(duplicateunitNo){
			errorList.push(getLocalMessage("property.bif.flat.duplicate.valid"));
		}
		var srNo = {};
		duplicateSrNo = false;
		$('.firstUnitRow').each(function(j){
			var tdContent = $("#unitNo"+j).val()
			if(srNo[tdContent]){
				duplicateSrNo = true;
				return false;
			}
			srNo[tdContent] = true;
		});
		
		if(duplicateSrNo){
			errorList.push(getLocalMessage("property.bif.srno.duplicate.valid"));
		}
		
		return errorList;
}

function ValidateRVArea(errorList) {
	var totalHiddenrvArea= $("#totalrvArea").val();

	var totalrvArea = 0;
	$('.firstUnitRow').each(function(i) {

		let rvArea = Number($("#assdRv" + i).val());
		totalrvArea += rvArea;

	});
	if (totalHiddenrvArea != totalrvArea) {
		errorList.push(getLocalMessage("property.bif.buildrvarea.valid"));
	}
	return errorList;
}



function ValidateFormDetails(errorList){

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


function saveForm(element){
	 return saveOrUpdateForm(element,"Bifurcation done successfully!", 'AdminHome.html', 'saveform');
}



//reorder ID unit details fields on page load
function reorderUnitDetailsTable(classNameFirst,classNamesecond){
	
	$(classNameFirst).each(function(i) {

	$(this).find("select:eq(0)").attr("id", "year"+i);
//	$(this).find("select:eq(1)").attr("id", "assdUnitTypeId"+i);
	$(this).find("select:eq(1)").attr("id", "assdFloorNo"+i);
	$(this).find("select:eq(2)").attr("id", "assdConstruType"+i);
	
            var utp=i;
			if(i>0){
			utp=i*6;
			}
			$(this).find("select:eq(3)").attr("id", "assdUsagetype"+utp);
			$(this).find("select:eq(4)").attr("id", "assdUsagetype"+(utp+1));
			$(this).find("select:eq(5)").attr("id", "assdUsagetype"+(utp+2));
			$(this).find("select:eq(6)").attr("id", "assdUsagetype"+(utp+3));
			$(this).find("select:eq(7)").attr("id", "assdUsagetype"+(utp+4));

	});
	
	$(classNamesecond).each(function(j){
//		$(this).find("select:eq(0)").attr("id", "assdRoadFactor"+j);
//		$(this).find("select:eq(0)").attr("id", "assdOccupancyType"+j);	
//		$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(j)+")");		
	});
}

function reorderFirstRow(){
	
//	$(".firstUnitRow").find("select:eq(1)").attr("id", "assdUnitTypeId");
	$(".firstUnitRow").find("select:eq(1)").attr("id", "assdFloorNo");
	$(".firstUnitRow").find("select:eq(2)").attr("id", "assdOccupancyType");
	$(".firstUnitRow").find("select:eq(3)").attr("id", "legal");
	$(".firstUnitRow").find("select:eq(4)").attr("id", "areaOrRvBasedChange");
	$(".firstUnitRow").find("select:eq(5)").attr("id", "assdConstruType");
	$(".firstUnitRow").find("select:eq(6)").attr("id", "assdUsagetype1");
	$(".firstUnitRow").find("select:eq(7)").attr("id", "assdUsagetype2");
	$(".firstUnitRow").find("select:eq(8)").attr("id", "assdUsagetype3");
	$(".firstUnitRow").find("select:eq(9)").attr("id", "assdUsagetype4");
	$(".firstUnitRow").find("select:eq(10)").attr("id", "assdUsagetype5");
//	$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdRoadFactor");
	//$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdOccupancyType");
}

//fetch API details on selection of button 
function getLandApiDetails(obj){
	var landTypePrefix=$(".landValue").val();
	var data = {};
	var URL = 'ChangeInAssessmentForm.html?getLandTypeApiDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);		
	$("#showAuthApiDetails").html(returnData);
	$("#showAuthApiDetails").show();		
}



function editSelfAssBifurcationForm(obj){
	var ownerType = $("#ownershipId").val();
	var data = {};
	var URL = 'BifurcationForm.html?editBifurcationForm';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	reorderFirstRow();
	reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
	reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
	yearLength();
	reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
	$("#checkListBifu").hide();
	$("#backButton").hide();
	$("#proceedBifu").show();
	var data1 = {"ownershipType" : ownerType};
	var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
	var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
	$("#owner").html(returnData1);
	$("#owner").show();
	$('#checkDetail').prop('checked', true);
	
}

//To reorder ID and Path
function reOrderUnitTabIdSequence(classNameFirst,classNamesecond) {
	
	var assType = $("#assType").val();
	var ConstructFlag =  $("#ConstructFlag").val();
			$(classNameFirst).each(function(i) {
				
			// re-ordering id 	
			$(".datepicker2").removeClass("hasDatepicker");
			$(".lessthancurrdate").removeClass("hasDatepicker");
			var incr=i+1;

			$(this).find("select:eq(0)").attr("id", "year"+i);
			$(this).find("input:hidden:eq(0)").attr("id", "hiddenYear"+i);
			$(this).find("input:hidden:eq(0)").attr("onchange", "compareDate("+(i)+")");
			
			$(this).find("select:eq(0)").attr("onchange", "unitCount("+(i)+")");
			$(this).find("input:text:eq(0)").attr("id", "unitNo"+i);
//			$(this).find("select:eq(1)").attr("id", "assdUnitTypeId"+i);
			$(this).find("select:eq(1)").attr("id", "assdFloorNo"+i);
			$(this).find("select:eq(2)").attr("id", "assdOccupancyType"+i);
			$(this).find("select:eq(3)").attr("id", "legal"+i);
			$(this).find("select:eq(4)").attr("id", "areaOrRvBasedChange"+i);
			if(ConstructFlag != null && ConstructFlag == 'Y'){

				$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
				$(this).find("input:text:eq(2)").attr("id", "occupierName"+i);
				$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
				$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
				$(this).find("input:text:eq(5)").attr("id", "assdRv"+i);
				//$(this).find("input:text:eq(2)").attr("id", "yearOfConstruc"+i);
				if(assType == 'C' && assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i);
					$(this).find("input:hidden:eq(1)").attr("id", "hiddenFirstAssesmentDate"+i);
					
					//$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i).prop("readonly", true);
				}else if(assType != 'BIF'){					
					$(this).find("input:text:eq(3)").attr("id", "firstAssesmentDate"+i);
				}
				
				$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				$(this).find("input:text:eq(2)").attr("onchange", "successErrorCheck("+(i)+")");
				/*if(assType != 'BIF'){				
					$(this).find("input:text:eq(4)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(5)").attr("id", "taxableArea"+i);
					$(this).find("input:text:eq(6)").attr("id", "constructPermissionNo"+i);
					$(this).find("input:text:eq(7)").attr("id", "permissionUseNo"+i);
					$(this).find("input:text:eq(8)").attr("id", "assessmentRemark"+i);
				}else{
					$(this).find("input:text:eq(2)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
					//$(this).find("input:text:eq(5)").attr("id", "constructPermissionNo"+i);
					//$(this).find("input:text:eq(6)").attr("id", "permissionUseNo"+i);
					//$(this).find("input:text:eq(7)").attr("id", "assessmentRemark"+i);
				}*/
			}else{				
				//$(this).find("input:text:eq(1)").attr("id", "yearOfConstruc"+i);
				if(assType == 'C' && assType != 'BIF'){
					$(this).find("input:text:eq(1)").attr("id", "lastAssesmentDate"+i);
					$(this).find("input:hidden:eq(1)").attr("id", "hiddenFirstAssesmentDate"+i);
					
					//$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i).prop("readonly", true);
				}else if(assType != 'BIF'){				
					$(this).find("input:text:eq(1)").attr("id", "firstAssesmentDate"+i);//changed
				}
				
				//$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				if(assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
					$(this).find("input:text:eq(5)").attr("id", "constructPermissionNo"+i);
					$(this).find("input:text:eq(6)").attr("id", "permissionUseNo"+i);
					$(this).find("input:text:eq(7)").attr("id", "assessmentRemark"+i);
				}else{
					$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(2)").attr("id", "occupierName"+i);
					$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
					$(this).find("input:text:eq(5)").attr("id", "assdRv"+i);
					$(this).find("input:text:eq(6)").attr("id", "constructPermissionNo"+i);
					$(this).find("input:text:eq(7)").attr("id", "permissionUseNo"+i);
					$(this).find("input:text:eq(8)").attr("id", "assessmentRemark"+i);
				}
			}
			
			//$(this).find("input:text:eq(1)").attr("id", "yearOfConstruc"+i).prop("readonly", true);
			
			
			

			$(this).find("select:eq(5)").attr("id", "assdConstruType"+i);
			
			
			$(this).find("a:eq(0)").attr("data-target", "#group-of-rows-"+i);
			$(this).find("a:eq(0)").attr("aria-controls", "#group-of-rows-"+i);
			$(this).find("a:eq(1)").attr("id", "deleteRow_"+i);
			$(this).find("a:eq(2)").attr("id", "addRow_"+i);
			var utp=i;
			if(i>0){
			utp=i*6;
			}
			$(this).find("select:eq(6)").attr("id", "assdUsagetype"+utp);
			$(this).find("select:eq(7)").attr("id", "assdUsagetype"+(utp+1));
			$(this).find("select:eq(8)").attr("id", "assdUsagetype"+(utp+2));
			$(this).find("select:eq(9)").attr("id", "assdUsagetype"+(utp+3));
			$(this).find("select:eq(10)").attr("id", "assdUsagetype"+(utp+4));
			
			// re-ordering path binding
			//if($("#checkDetail").prop('checked')==false){
				$("#unitNo"+i).val(incr);
			//}
			$(this).find("select:eq(0)").attr("name","");
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitNo");
			$(this).find("input:hidden:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].faYearId");
//			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitTypeId");
			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdFloorNo");
			$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdOccupancyType");
			$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].legal");
			$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].areaOrRvBasedChange");
			if(ConstructFlag != null && ConstructFlag == 'Y'){
				
				
				//$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdYearConstruction");
				
				if(assType == 'C' && assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].lastAssesmentDate");
					$(this).find("input:hidden:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
				}else if(assType != 'BIF'){				
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
				}
				if(assType != 'BIF'){
					//newly chnaged
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					//$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].taxableArea");
					//$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					//$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					//$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}else{
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].occupierName");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					//$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].taxableArea");
					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdRv");
					//$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					//$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					//$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}
			} else{
	            // $(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdYearConstruction");
				
				if(assType == 'C' && assType != 'BIF'){
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].lastAssesmentDate");
					$(this).find("input:hidden:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
				}else if(assType != 'BIF'){
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
				}
				if(assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					//$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].taxableArea");
					//$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					//$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					//$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}else{
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].occupierName");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					//$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].taxableArea");
					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdRv");
					//$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					//$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					//$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}
			}
			
			
			$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdConstruType");
			$(this).find("select:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype1");
			$(this).find("select:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype2");
			$(this).find("select:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype3");
			$(this).find("select:eq(9)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype4");
			$(this).find("select:eq(10)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype5");
			
			//Newly Added	
			if(assType != 'BIF'){
				$(this).find("input:checkbox:eq(0)").attr("id", "legal" + i);
				$(this).find("input:checkbox:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].legal");
			}else{
				$(this).find("input:checkbox:eq(0)").attr("id", "assdBifurcateNo" + i);
				$(this).find("input:checkbox:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].assdBifurcateNo");
				//$(this).find("input:checkbox:eq(1)").attr("id", "legal" + i);
				//$(this).find("input:checkbox:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].legal");
			}			
			$(this).find('.addRoomClass').attr("id", "addRoom" + i);
			$(this).find(".addRoomClass").attr("onclick", "addRoomDetails("+(i)+")");
			//		
			
			/*if(assType != 'BIF'){
				$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
			}else{
				$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
			}*/
			
			if(i>0)
			{
				//if($("#checkDetail").prop('checked')==false){
					var a=i-1;
					$("#hiddenYear"+i).val($("#hiddenYear"+a).val());
					$("#year"+i).val($("#hiddenYear"+a).val());
				//}
			}
			
		
			var unitNoFact=$("#unitNo"+i).val();
			if($(".selectUnit option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined && unitNoFact==null){				
				$(".selectUnit").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
			}
			datePickerLogic();
			
		});
		
		$(classNamesecond).each(function(j) {
			var utp=j;
			if(j>0){
			utp=j*6;
			}
			
//			$(this).find("select:eq(0)").attr("id", "assdRoadFactor"+j);
			//$(this).find("select:eq(0)").attr("id", "assdOccupancyType"+j);
//			$(this).find("input:text:eq(0)").attr("id", "proAssdAnnualRent"+j);
			//$(this).find("input:text:eq(0)").attr("id", "occupierName"+j);		
//			$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(j)+")");
			//$(this).attr("id", "group-of-rows-"+j);
			
			///newly dded
			//$(this).find("input:text:eq(1)").attr("id", "occupierNameReg"+j);		
			////$(this).find("input:text:eq(2)").attr("id", "occupierMobNo"+j);		
			//$(this).find("input:text:eq(3)").attr("id", "occupierEmail"+j);
			//$(this).find("input:text:eq(4)").attr("id", "constructPermissionNo"+j);
			//$(this).find("input:text:eq(5)").attr("id", "permissionUseNo"+j);
			//$(this).find("input:text:eq(6)").attr("id", "assessmentRemark"+j);
			//$(this).find("input:text:eq(7)").attr("id", "actualRent"+j);
			/*if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
				$(".actualRent"+j).hide();
				$("#actualRent"+j).hide();
			}*/
			//
			
			//$(this).find("select:eq(1)").attr("id", "natureOfProperty"+utp);
			//$(this).find("select:eq(2)").attr("id", "natureOfProperty"+(utp+1));
			//$(this).find("select:eq(3)").attr("id", "natureOfProperty"+(utp+2));
			//$(this).find("select:eq(4)").attr("id", "natureOfProperty"+(utp+3));
			//$(this).find("select:eq(5)").attr("id", "natureOfProperty"+(utp+4));

			// re-ordering path binding
//			$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdRoadFactor");
			//$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdOccupancyType");
//			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdAnnualRent");
			//$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierName");
			
			//$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty1");
			//$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty2");
			//$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty3");
			//$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty4");
			//$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty5");
			
			//newly added
			//$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierNameReg");
			//$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierMobNo");
			//$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierEmail");
			//$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].constructPermissionNo");
			//$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].permissionUseNo");
			//$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assessmentRemark");
			//$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].actualRent");
			//	

		});
}

function datePickerLogic(){
	var yearOfAcq =$("#proAssAcqDate").val();
	 $(".datepicker2").datepicker({
	        dateFormat: 'dd/mm/yy',		
			changeMonth: true,
			changeYear: true,
			maxDate:yearOfAcq
		});
	 
	 $('.lessthancurrdate').datepicker({
			dateFormat: 'dd/mm/yy',	
			changeMonth: true,
			changeYear: true,
			maxDate: '-0d',
			yearRange: "-100:-0"
		});
	//D#139215
	 $(".datepicker").datepicker({
	        dateFormat: 'dd/mm/yy',		
			changeMonth: true,
			changeYear: true,
		});
}

/*To add new Row into table*/ 
/*$("#unitDetailTable").on('click','.addBIF',function(){*/
function addBIF(){
	
		var errorList = [];			
		//var rowCount = $('#unitDetailTable tr:last').index()/2 ;				
		errorList = validateBifurcationUnitDetailTable(errorList);				
		if (errorList.length == 0) {	
			
		
			var content =	$("#unitDetailTable").find('tr:eq(1)').clone();
			//$('.secondUnitRow').removeClass('in');
			$("#unitDetailTable").append(content);
			content.find("input:text:eq(3)").val('');
			content.find("input:text:eq(4)").val('');
			content.find("input:text:eq(5)").val('');
			//$('.secondUnitRow:last').addClass('in');
//			content.find("input:text").val('');
//			content.find("select").val('0');
//			content.find("input:hidden:eq(0)").val('0');

			$('.error-div').hide();
			datePickerLogic();
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');  // reorder id and Path after Addition of new Row
//			if($("#checkDetail").prop('checked')==true){   // after tick unable year 
//					$("#unitDetailTable tr:last").prev().find("select:eq(0)").prop('disabled',false).removeClass("disabled").addClass("mandColorClass");
//			}
			if ($('#assdBifurcateNo').prop('checked' == true)) {
				$("#assdBifurcateNo").find("checkbox").prop("disabled", "disabled");
			}
//			var hiddenOther=$('#hiddenOther').val();
//			content.find("select:eq(1)").val(hiddenOther);				
			return false;
		}							
		else {
			displayErrorsOnPage(errorList);
	}	
}		
/*}); */

	//To delete Row From the table 
	$("#unitDetailTable").on('click','.remCF',function(){
		
		//To remove entry from dto
		var inputId=this.id;		
		var left_text = inputId.lastIndexOf("_");
		var deletedRowCount = inputId.substring(left_text+1);		
		var data = {"deletedRowCount" : deletedRowCount};
		var URL = 'BifurcationForm.html?deleteUnitTableRow';
		
		
		//To delete Unit No from unit specific additional information 
		var unitCount= $("#unitNo"+deletedRowCount).val();		
		var a=0; 
		var tableLength = $('#unitDetailTable tr').length;
		for(var j=1;j<tableLength;j++) // table tr length 
		{
			var unitNoFact=$("#unitNo"+j).val();   			
			if(unitCount==unitNoFact){
				a++;
			}
		}
		if(a==1){
			$(".selectUnit option[value="+unitCount+"]").remove();
		}

		
		//remove row From UI
		var index = $(this).closest("tr").index();
		//if(($("#unitDetailTable tr").length)>2 && (index!=1)) {
		if($("#unitDetailTable tr").length > 2){
			var tr = $(this).closest("tr");
		    tr.add(tr.next()).remove();
		    $('.secondUnitRow:last').addClass('in');
			var returnData = __doAjaxRequest(URL, 'POST', data, false);
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			
		}
		else {	
			var errorList = [];
			
			errorList.push(getLocalMessage("unit.Firstrowcannotberemove"));
			showVendorError(errorList);
		}	
	});
		
	$('body').on('click', 'tr.secondUnitRow', function() {
	      $('tr.in').removeClass('active');
	});
	
	$('body').on('click', 'a.click_advance', function() {	
		$("i", this).toggleClass("fa-caret-up fa-caret-down");
	}); 
	


//validation of unit details table
function validateBifurcationUnitDetailTable(errorList){
	
	var rowCount = $('#unitDetailTable tr').length;	
	if(rowCount==2){
		reorderFirstRow();
	}
	$('.firstUnitRow').each(function(i) {	
		if(rowCount<=2){			
			var year =$("#hiddenYear0").val();
			var floorNo=  $("#assdFloorNo").val();
			var occupancyType =$("#assdOccupancyType").val();
			var occupierName = $("#occupierName0").val();
			var yearOfConstruc=  $("#yearOfConstruc0").val();
			var usageType1=  $("#assdUsagetype1").val();
			var usageType2=  $("#assdUsagetype2").val();
			var usageType3=  $("#assdUsagetype3").val();
			var usageType4=  $("#assdUsagetype4").val();
			var usageType5=  $("#assdUsagetype5").val();
			var ConstruType=  $("#assdConstruType").val();
			var taxableArea=  $("#taxableArea0").val();
			var rv=  $("#assdRv0").val();	
			var flatNo=  $("#flatNo0").val();	
			var areaOrRvBasedChange = $("#areaOrRvBasedChange").val();	
			var level = 1;
		} else{
		var year =$("#hiddenYear"+i).val();	
		var floorNo=  $("#assdFloorNo"+i).val();
		var occupancyType =$("#assdOccupancyType"+i).val();
		var occupierName = $("#occupierName"+i).val();
		var yearOfConstruc=  $("#yearOfConstruc"+i).val();
		
		var utp=i;
		if(i>0){
		utp=i*6;
		}
		var usageType1=  $("#assdUsagetype"+utp).val();
		var usageType2=  $("#assdUsagetype"+(utp+1)).val();
		var usageType3=  $("#assdUsagetype"+(utp+2)).val();
		var usageType4=  $("#assdUsagetype"+(utp+3)).val();
		var usageType5=  $("#assdUsagetype"+(utp+4)).val();
		
		var ConstruType=  $("#assdConstruType"+i).val();
		var taxableArea=  $("#taxableArea"+i).val();
		var rv=  $("#assdRv"+i).val();	
		var flatNo=  $("#flatNo"+i).val();
		var areaOrRvBasedChange = $("#areaOrRvBasedChange"+i).val();

		var level = i+1;
		}
		 if(year=='' || year == undefined || year == '0'){			 
			 errorList.push(getLocalMessage('unitdetails.selectYearlevel')+" " +level); 
		 }
		 /*if(unitType=='0' || unitType == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectUnitType')+" " +level); 
		 } */		 
		 if(floorNo=='0' || floorNo == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectFloorNo')+ " " +level); 
		 } 			 
		/* if(yearOfConstruc=='' || yearOfConstruc == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectConstructionCompletionDate')+ " " +level); 
		 } 		*/ 	
		 if(usageType1=='0' || usageType1 == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectUsageType')+" " +level); 
		 } 
		 if((usageType2 != undefined) && usageType2=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 } 
		 if((usageType3 != undefined) && usageType3=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 } 
		 if((usageType4 != undefined) && usageType4=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 }
		 if((usageType5 != undefined) && usageType5=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 }
		 if(ConstruType=='0' || ConstruType == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectConstructionType')+" "+level); 
		 }
		
		/*if((taxableArea=='' || taxableArea == undefined || taxableArea<=0)  && (rv <= 0)){
			errorList.push(getLocalMessage('unitdetails.selectTaxableArea')+" " +level);
		}*/
		 
		 if (flatNo == '' || flatNo == undefined) {
				errorList.push(getLocalMessage('unitdetails.enter.flatNoValid')+ " " + level);
		 }
		 
		if(occupancyType=='0' || occupancyType == undefined){
					  errorList.push(getLocalMessage('unitdetails.selectOccupacyType')+" " +level); 
		}
		if(occupierName=='' || occupierName == undefined){
						  errorList.push(getLocalMessage('unitdetails.selectOccupierName')+ " " +level); 
		}
		if(areaOrRvBasedChange == '0' || areaOrRvBasedChange == '' || areaOrRvBasedChange == undefined || areaOrRvBasedChange == null){
			errorList.push(getLocalMessage('unitdetails.select.areaOrRv')+ " " +level); 
		}
		if((taxableArea=='' || taxableArea == undefined || taxableArea == null | taxableArea == '0.00') && 
				(rv == '' || rv == undefined || rv == null || rv == '0' || rv == '0.00')){
			  errorList.push(getLocalMessage('unitdetails.valid.areaOrRv')+" " +level);				
		} 			
	});
	
	return errorList;
}

function validateBIFFactorStatus(errorList){

	$('.factorQes').each(function(i){
		var level = i+1;
		var selectFactor = $.trim($('input[name="provisionalAssesmentMstDto.proAssfactor['+i+']"]:checked').val());
		if(selectFactor==0 || selectFactor=="")
		errorList.push(getLocalMessage("property.factorStatus")+" "+level);
		});
	
	return errorList;
}

$("#roomDetailstable").on('click','.addRoomDetails',function(){
	var countOfRow=$("#countOfRow").val();
	var content =	$("#roomDetailstable").find('tr:eq(1)').clone();
	$(this).closest("#roomDetailstable").append(content);
	content.find("select").attr("value", "");
	content.find("input:text").val("");
	reOrderRoomTableIdSequence(countOfRow);
	return false;
});

function addRoomDetails(count, element) {
	
	$('#countOfRow').val(count);
	var serviceShortCode = $('#serviceShortCode').val();
	var errorList = [];
	//errorList = validateUnitDetailTable(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		var requestData = {};
			var theForm = '#BifurcationFormSearch';
			var action = "BifurcationForm.html";
			requestData = __serializeForm(theForm);
			var URL = "BifurcationForm.html?addRoomDetails";
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
		
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		var totalAreaval = $("#totalAreaval").val();
		totalAreaval = parseFloat(totalAreaval).toFixed(2);
		$("#totalAreaval").text('0.00');
		$("#totalAreaval").val(totalAreaval);
	}
}

function viewRoomDetails(count, element) {
	
	$('#countOfRow').val(count);
		var assType = $("#assType").val();
		var countOfRow = count;
		var URL = "SelfAssessmentAuthorization.html?viewRoomDetails";
		var data = {"countOfRow" : countOfRow};
		var returnData = __doAjaxRequest(URL, 'POST', data, false);

		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
}

function saveRoomData(element){
	
	var errorList = [];
	errorList=validateRoomDetails(element,errorList);
//	var totalBuildUpArea = populateBuildUpArea();
//	var totalCarpetArea = populateCarpetArea();
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;	
	}else{
		//var countOfRow=$("#countOfRow").val();
		var unitChecked=$('#unitChecked').val();
		var ownerType=$('#ownerType').val();
		var theForm = '#frmSelfAssessmentFormRoom';
		var action="BifurcationForm.html";
		}
		var requestData = __serializeForm(theForm);
		var URL = action+'?saveRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		
		reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
		reOrderJointOwnerTableSequence('.jointOwner'); 
		reorderfactor('.specFact','.eachFact');
		//set owner details
		if('CIA' != serviceShortCode){
			if (ownerType != "") {
			var data1 = {
				"ownershipType" : ownerType
			};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
		}
		
	}	
}

// to delete row
$("#roomDetailstable").on("click", '.delButton', function(e) {

	var errorList = [];
	var countOfRow=$("#countOfRow").val();
	var rowCount = $('#roomDetailstable tr').length;
	if (rowCount <= 2) {
		
		errorList.push(getLocalMessage('property.valid.firstRow'));
		displayErrorsOnPage(errorList);		
		return false;
	} else {

		var index= $(this).closest('#roomDetailstable tr').index();				
		var action="BifurcationForm.html";						
		var requestData = {
			"countOfRow" : countOfRow,
			"index":index
		};	
		$(this).closest('#roomDetailstable tr').remove();
		var URL = action+'?removeRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json');			
		reOrderRoomTableIdSequence(countOfRow);
	}
	//areaCalculate();
});

function validateRoomDetails(element, errorList) {
	
	var area=0.0;	
	var roomType = "";
	$('.thirdRowRoomDetails').each(function(i) {

		var rowCount = $('#roomDetailstable tr').length;
		if (rowCount <= 2) {
			roomType = $('#roomType' + i).val();			
		}else{
			roomType = $('#roomType' + i).val();			
		}		
		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		
		var level = i+1;
		
		if (errorList.length <= 0) {
			if (roomType == "" || roomType == undefined || roomType == null || roomType == '0') {
				errorList.push(getLocalMessage("property.valid.roomType") +" "+ level);
			}
			if (roomLength == "" || roomLength == undefined || roomLength == null) {
				errorList.push(getLocalMessage("property.valid.roomLength") +" "+ level);
			}
			if (roomWidth == "" || roomWidth == undefined || roomWidth == null) {
				errorList.push(getLocalMessage("property.valid.roomWidth") +" "+ level);
			}
		}else{
			return errorList;
		}
	});
	if (errorList.length == 0) {
		var carpetArea =parseFloat($('#carpetArea').val());
		$('.thirdRowRoomDetails').each(function(i) {
			var roomArea = $('#roomArea' + i).val();
			area += roomArea;
		});
		area=parseFloat(area);
//		if (area > carpetArea) {
//			errorList.push(getLocalMessage("property.valid.validroomArea"));
//		}
	}
	return errorList;
}
function backFromRoomdetails(element){
	
	var ownerType=$('#ownerType').val();
	var unitChecked=$('#unitChecked').val();
	var serviceShortCode = $('#serviceShortCode').val();
	var action="BifurcationForm.html";
		
	var requestData = {};
	var URL = action+'?saveRoomDetails';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');

	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	if (ownerType != "") {
		var data1 = {
			"ownershipType" : ownerType
		};
		var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		$("#owner").show();
	}
	reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
	reOrderJointOwnerTableSequence('.jointOwner'); 
	reorderfactor('.specFact','.eachFact');
}


function addUnitRow(count){
	var errorList = [];
	errorList = validateUnitSpecificInfoTable(errorList);
	
		if (errorList.length == 0) {
			var factPrex=$("#factPref"+count).val();
			var content =	$("#unitSpecificInfoTable"+factPrex).find('tr:eq(1)').clone();
 			$("#unitSpecificInfoTable"+factPrex).append(content);	
			content.find("select").val('0');
			content.find("input:hidden:eq(2)").val('0');						
			///Added new
			//content.find("input:text:eq(0)").val('');
			//content.find("input:text:eq(1)").val('');
			//
			reorderfactor('.specFact','.eachFact');
			//count.preventDefault();
			return false;
		}
		else 
		{
		displayErrorsOnPage(errorList);
		}
	
}

function calculateArea() {
	
	$('.thirdRowRoomDetails').each(function(i) {

		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		if (roomLength != "" && roomWidth != "") {
			var num =roomLength * roomWidth;
			var result = num.toFixed(2);
			$('#roomArea' + i).val(result);
			
		} else {
			$('#roomArea' + i).val();
		}
	});
}

function areaCalculate() {
	var finalTotal = 0;
	var roomlen = $('#roomDetailstable tr').length;
	//var roomlen=4;
	for (var i = 0; i < (roomlen - 2); i++) {
		var are = $('#roomArea' + i).val();
		/*if(are=='' || are==undefined || are==null){
			are=0;
		}*/
		finalTotal = finalTotal + parseFloat(are);
		//console.log(finalTotal);
	}
	finalTotal = parseFloat(finalTotal).toFixed(2);
	if (finalTotal == 'NaN') {
		finalTotal = '';
	}
	$("#totalAreaval").text('0.00');
	$('#totalAreaval').val(finalTotal);
}

$(document).on('click', '.unitSpecificRem', function () {
	
	//To remove entry from database
	var inputId=this.id;	
	var left_text = inputId.lastIndexOf("_");
	var deletedUnitRow = inputId.substring(left_text+1);
	var data = {"deletedUnitRow" : deletedUnitRow};
	var URL = 'SelfAssessmentForm.html?deleteUnitSpecificInfo';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	reorderfactor('.specFact','.eachFact');
	var id=$(this).closest('table').attr('id');	
	if($("#"+id+" tr").length >2) {
    $(this).closest('tr').remove();
    reorderfactor('.specFact','.eachFact');
	}
	
});

function validateUnitSpecificInfoTable(errorList){
	
		$('.specFact').each(function(i) {
	
			var unit=  $("#unitNoFact"+i).val();			
			var factor=  $("#assfFactorValueId"+i).val();
			var level = i+1;
			
			 if(unit=='0' || unit == undefined){
				  errorList.push(getLocalMessage('unit.selectUnitNo')+" " +level); 
			 } 
			 
			 if(factor=='0' || factor == undefined){
				  errorList.push(getLocalMessage('unit.selectfactor')+" " +level); 
			 } 
		});
		/*$('.factorQes').each(function(i){
			var level = i+1;
			var selectFactor = $.trim($('input[name="provisionalAssesmentMstDto.proAssfactor['+i+']"]:checked').val());
			if(selectFactor==0 || selectFactor=="")
			errorList.push(getLocalMessage("property.factorStatus")+" "+level);
			});*/
		
		return errorList;
}


function enabledisable(event, currentRow){
	
	 $(".error-div").hide();
	var errorList = [];
	$('.specFact').each(function(i) {
	    			var unit=  $("#unitNoFact"+i).val();
	    			var factor=  $("#assfFactorValueId"+i).val();
	    			var level = i+1;
	    			
	    			 if(unit=='0' || unit == undefined){
	    				  errorList.push(getLocalMessage('unit.selectUnitNo')+" " +level); 
	    			 } 
	    			 
	    			 if(factor=='0' || factor == undefined){
	    				  errorList.push(getLocalMessage('unit.selectfactor')+" " +level); 
	    			 } 
	    			
	    			 if(errorList.length == 0) {
	    				 
	    				if(currentRow != i && (event.value ==factor && $("#unitNoFact"+currentRow).val() == unit))
		  	    		{	
		  	    				errorList.push(getLocalMessage('unit.Duplicatefactorvalue'));
		  	    				$("#assfFactorValueId"+currentRow).val("0");		
		  	    				displayErrorsOnPage(errorList);
		  	    				return false;
		  	    		} 
	    		      }else{
	    		    	  $("#assfFactorValueId"+i).val('0');
	    		    	  displayErrorsOnPage(errorList);
	    					 return false;  
	    		      }
	});
}

function resetFactorValue(obj,index){
	 $("#assfFactorValueId"+index).val("0");	
}


function reOrderRoomTableIdSequence(countOfRow) {

	$('.thirdRowRoomDetails').each(function(i) {

			var j=countOfRow;
						
			$(this).find("input:text:eq(0)").attr("id", "roomNo" + i);
			$(this).find("select:eq(0)").attr("id", "roomType" + i);
			$(this).find("input:text:eq(1)").attr("id", "roomLength" + i);
			$(this).find("input:text:eq(2)").attr("id", "roomWidth" + i);
			$(this).find("input:text:eq(3)").attr("id", "roomArea" + i);
			
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomNo");
			$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomType");
			$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomLength");
			$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomWidth");
			$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomArea");
			
			var incr=i+1;
			$("#roomNo"+i).val(incr);
			
		});
}

// reorder id and path for unit specific addition information table
function reorderfactor(specFact,eachFact)
{
	$(eachFact).each(function(j) {
		$(specFact).each(function(i) {
			$(this).find("select:eq(0)").attr("id", "unitNoFact"+i).attr('onchange','resetFactorValue(this,'+i+')');
			$(this).find("select:eq(1)").attr("id", "assfFactorValueId"+i).attr('onchange','enabledisable(this,'+i+')');
			$(this).find("input:hidden:eq(0)").attr("name","provAsseFactDtlDto["+i+"].assfFactorId");
			$(this).find("input:hidden:eq(1)").attr("id","factPref"+i);
			$(this).find("input:hidden:eq(1)").attr("name","provAsseFactDtlDto["+i+"].factorValueCode");
			$(this).find("input:hidden:eq(2)").attr("id","proAssfId"+i);
			$(this).find("input:hidden:eq(2)").attr("name","provAsseFactDtlDto["+i+"].proAssfId");
			$(this).find("select:eq(0)").attr("name","provAsseFactDtlDto["+i+"].unitNoFact");
			$(this).find("select:eq(1)").attr("name","provAsseFactDtlDto["+i+"].assfFactorValueId");
			
			///newly added
			$(".datepicker2").removeClass("hasDatepicker");
			$(".lessthancurrdate").removeClass("hasDatepicker");
			datePickerLogic();
			
			$(this).find("input:text:eq(0)").attr("id","factorDate"+i);
			$(this).find("input:text:eq(1)").attr("id","factorRemark"+i);
			$(this).find("input:text:eq(0)").attr("name", "provAsseFactDtlDto["+i+"].factorDate");
			$(this).find("input:text:eq(1)").attr("name", "provAsseFactDtlDto["+i+"].factorRemark");
			//
			
			$(this).find("a:eq(0)").attr("onclick", "addUnitRow("+(i)+")");
			$(this).find("a:eq(1)").attr("id", "deleteFactorTableRow_"+i);			
		 });
		$(this).find("select:eq(0)").attr("class","form-control mandColorClass selectUnit factQuestion"+j);

		});
}

function showAddInfo(factorCode,factRadioVal){
	
	if($('input[name="provisionalAssesmentMstDto.proAssfactor['+factRadioVal+']"]:checked').val()=="Y"){
		
		var data = {"factorCode" : factorCode};		
		var URL = 'BifurcationForm.html?getFactorValueDiv';
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
		var URL = 'BifurcationForm.html?deleteFactorStatus';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html('');
		reorderfactor('.specFact','.eachFact');
	}
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

function getDataForNextPage(obj,pageNo) {
	$(".error-msg").hide();
		var errorList = [];
			
		if (errorList.length == 0)
		{
			
			var data = {"pageNo" : pageNo};
			var returnData1 = __doAjaxRequest('BifurcationForm.html?getPageCount', 'POST', data, false);	
			var action = "BifurcationForm.html";
			var theForm = '#BifurcationFormSearch';
			requestData = __serializeForm(theForm);
			var URL = 'BifurcationForm.html?getDataForNextPage';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			//$(formDivName).html(returnData);
			var divName = '.content-page';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table
			reorderfactor('.specFact','.eachFact');
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
 }