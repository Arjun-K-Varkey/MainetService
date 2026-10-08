
$(document).ready(function(){

	var assAddress= $("#assdFloorNo").val();
	if(assAddress=='0'){
		$('#assdFloorNo').find('option[code=OTH]').attr('selected','selected');
	}

	$("#Reset").hide();
	$("html, body").animate({ scrollTop: 0 }, "slow");
	$('#proAssAcqDate').attr('readonly', false);
	
	/*var landType = $("#assdNatureOfproperty1" + " option:selected").attr("code");
	if(landType=='LAND' || landType=='Land'){
		$('#survey').show();
	}else{
		$('#survey').hide();		
	}*/
	
	//View page: Amount to pay is copied on receipt Amount
	
	$('#partialAmt').change(function(){
		$('#amountToPay').val($('#partialAmt').val());
	});
	
	
	$("#backToSearch").on("click", function(){ 
		  window.location.reload("#propertyAssessmentType");
		});
	
	
	yearLength();
	
var minYear= $("#minFinancialYear").val();
//datepicker function for year of acquisition
$('.lessthancurrdate').datepicker({
	dateFormat: 'dd/mm/yy',	
	changeMonth: true,
	changeYear: true,
	minDate: minYear,
	maxDate: '-0d',
	yearRange: "-100:-0",
});

$(".datepicker-cpdate").datepicker({
    dateFormat: 'dd/mm/yy',		
	changeMonth: true,
	changeYear: true
});

//datepicker function for construction completion date
$(".datepicker2").datepicker({
    dateFormat: 'dd/mm/yy',		
	changeMonth: true,
	maxDate: '-0d',
	changeYear: true,
	   onSelect: function(selected) {
		   checkConstructionDate();
		 
	      } 
});

jQuery(function() {
    $('input.dateFrmt').datepicker({changeMonth: true, changeYear: true}).
          mask('99/99/9999');
});


var authLevel = $("#authLevel").val();
if(authLevel=="true"){
	$("#proceed").show();
	$("#btnCompare").hide();
	var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
	if(ownerType!=undefined){
		var data1 = {"ownershipType" : ownerType};
		var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		$("#owner").show();
		//$('#checkDetail').prop('checked', true);
		loadOccupierNameAtAuth();
	}
} else if("Y" == $("#checklistApplicable").val()){
	$("#proceed").show();
} else {
	if($("#editFlag").val()!="Y"){
	$("#proceed").hide();
	}
	if($("#draftApplication").val() == 'Y'){
		$("#checkList").hide();
		$("#proceed").show();
	}	
}
if($("#draftApplication").val() === 'Y' && $("#serviceShortCode").val() === 'CIA' && "Y" !== $("#checklistApplicable").val()){
	$("#checkList").show();
}
/*$('#yearOfConstrucId0').change(function(){	
	
	var yearOfAcquisition =  $("#proAssAcqDate").val();			
	if(yearOfAcquisition == '' || yearOfAcquisition == undefined){
			showAlertBoxForConstructionDate();
	}	
});*/

/*var locId=$("#locId").val();
if(locId!=null && locId!='' && locId!=undefined && locId>0){
	var result=	getOperationalWardZone($("#orgId").val(),$("#deptId").val(),$("#locId").val());
	$("#wardZone").html(result);
}*/
prepareDateTag();
//newly  added
//reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
$('.datepicker').datepicker({
	dateFormat: 'dd/mm/yy',	
	changeMonth: true,
	changeYear: true
});

/*$("#assdNatureOfproperty1").on('change', function(){
var landType = $("#assdNatureOfproperty1" + " option:selected").attr("code");
if(landType=='LAND' || landType=='Land'){
	$('#survey').show();
}else{
	$('#survey').hide();
}

});*/

});

function prepareDateTag() {
	var dateFields = $('.trimDateTime');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}

//If year of Acquisition date is not selected then show popUp message
function  checkConstructionDate()
{
	var yearOfAcquisition =  $("#proAssAcqDate").val();	
	var check = $("#yearOfConstruc0").val();
	if (check != '' && yearOfAcquisition!='') {
		$('.successErrorCheck').removeClass("has-error").addClass("has-success");
	}
	
	if(yearOfAcquisition == '' || yearOfAcquisition == undefined){
		showAlertBoxForConstructionDate();
	}	
}

function showAlertBoxForConstructionDate(){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = 'Ok';
	var warningMsg=getLocalMessage("property.assessment.date.msg");
	message	+='<h4 class=\"text-center text-blue-2 padding-10\">'+warningMsg+'</h4>';
	 message	+='<div class=\'text-center padding-bottom-10\'>'+	
	'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '+ 
	' onclick="closeAlertForm()"/>'+
	'</div>';
	
	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg'); 
	$(errMsgDiv).html(message);
	$('#yearOfConstruc0').val("");
	$(errMsgDiv).show();
	showModalBox(errMsgDiv);
}

//To close the PopUp message
function closeAlertForm()
{
	var childDivName	=	'.child-popup-dialog';
	$(childDivName).hide();
	$(childDivName).empty();
	disposeModalBox();
	$.fancybox.close();
}

//saved form data and display preview Page 
function confirmToProceed(element){ 
	showloader(true);
	setTimeout(function () {
	var errorList = [];
	
	// Address Details
	if($('#isCorrespondenceAddressSame').is(":checked")){
		var assAddress= $("#assAddress").val();
		var assPincode=$("#assPincode").val();
		var assEmail=$("#assEmail").val();
		
		$("#assCorrAddress").val(assAddress);
		$("#assCorrPincode").val(assPincode);
		$("#assCorrEmail").val(assEmail);	
	}
	else{

		if($("#assCorrAddress").val()=='' || $("#assCorrPincode").val() == ''){			 
			 errorList.push("Please enter correspodence Address and correspodence Pincode "); 
		 }
	}
	
	// Unit Details
	//if($('#checkDetail').is(":checked")){    // Display validation msg for deleted year 
		var finYearId= $("#hiddenYear0").val();
		var data = {"finYearId" : finYearId};
		var URL = 'SelfAssessmentForm.html?getFinanceYearListFromGivenDate';
		var returnData2 = __doAjaxRequest(URL, 'POST', data, false);
		
	errorList = validateRefPropNo(errorList);
	errorList = ValidateMasterDetails(errorList);
	errorList = ValidateOwnerDetails(errorList);
	errorList = ValidateTick(errorList);	
	errorList = validateUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {

	//if($("#checkDetail").is(':checked')){
	
		var theForm	=	'#frmSelfAssessmentForm';
		var requestData = {};
		requestData = __serializeForm(theForm);

		var URL = 'SelfAssessmentForm.html?proceed';
		
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		if(returnData)
		{
		/*	var divName = '.content';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);*/
			$(formDivName).html(returnData);
			reorderFirstRow();
			/*reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	*/ // reordering Id and Path for unit details table
			reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
		//	occupancyTypeChange1();   
			yearLength();
			reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
			var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			//$(formDivName).html(returnData);
			
			if(ownerType!=undefined){
				var data1 = {"ownershipType" : ownerType};
				var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
				var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
				$("#owner").html(returnData1);
			}
			$("#owner").show();
			$("#proceed").show();
			$("#checkList").hide();			
			$("#ResetSelfAssForm").hide();
			//$('#checkDetail').prop('checked', true);		
			
		}
		yearLength();
	//}
	}
	else {
		showVendorError(errorList);
	}
	showloader(false);
	},6000);
}

function getCheckList(element){ 
	showloader(true);
	setTimeout(function () {
	var errorList = [];
	// Address Details
	if($('#isCorrespondenceAddressSame').is(":checked")){
		var assAddress= $("#assAddress").val();
		var assPincode=$("#assPincode").val();
		var assEmail=$("#assEmail").val();
		
		$("#assCorrAddress").val(assAddress);
		$("#assCorrPincode").val(assPincode);
		$("#assCorrEmail").val(assEmail);	
	}
	else{

		if($("#assCorrAddress").val()=='' || $("#assCorrPincode").val() == ''){			 
			 errorList.push("Please enter correspodence Address and correspodence Pincode "); 
		 }
	}
	
	var finYearId= $("#hiddenYear0").val();
		var data = {"finYearId" : finYearId};
		var URL = 'SelfAssessmentForm.html?getFinanceYearListFromGivenDate';
		var returnData2 = __doAjaxRequest(URL, 'POST', data, false);
	
	errorList = validateRefPropNo(errorList);
	errorList = ValidateMasterDetails(errorList);
	errorList = ValidateOwnerDetails(errorList);
	errorList = ValidateTick(errorList);	
	errorList = validateUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {

		var theForm	=	'#frmSelfAssessmentForm';
		var requestData = {};
		requestData = __serializeForm(theForm);

		var URL = 'SelfAssessmentForm.html?getCheckList';
		
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		if(returnData)
		{
			$(formDivName).html(returnData);
			reorderFirstRow();
			reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
			reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
			
		//	occupancyTypeChange1();   
			yearLength();
			
			reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
			
			var ownerType=null;
			 ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			if(ownerType ==undefined){
				ownerType=$("#ownershipId").val();
			}
			var data1 = {"ownershipType" : ownerType};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
			$("#proceed").show();
			$("#checkList").hide();
			$("#ResetSelfAssForm").hide();
			//$('#checkDetail').prop('checked', true);
		}
		yearLength();
	//}
	}
	else {
		showVendorError(errorList);
	}
	showloader(false);
	},1000);
}





function ValidateTick(errorList){

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

	if($("input[name='provisionalAssesmentMstDto.proAssBillPayment']:checked").val()=="M"){
		var proAssLpReceiptAmt=$("#proAssLpReceiptAmt").val();
		var billAmount= $("#billAmount").val();
		var outstandingAmount= $("#outstandingAmount").val();
		var ReceiptNo= $("#assLpReceiptNo").val();		
		if(proAssLpReceiptAmt=='' || proAssLpReceiptAmt == undefined || (proAssLpReceiptAmt<=0)){			 
			 errorList.push(getLocalMessage("property.enterReceiptAmount")); 
		 }		
		if(billAmount=='' || billAmount == undefined || (billAmount<=0)){			 
			 errorList.push(getLocalMessage("property.enterBillAmount")); 
		 }		
		if(outstandingAmount=='' || outstandingAmount == undefined || (outstandingAmount<=0)){			 
			 errorList.push(getLocalMessage("property.enteroutstandingAmount")); 
		 }
		if(ReceiptNo=='' || ReceiptNo == undefined || (ReceiptNo<=0)){			 
			 errorList.push(getLocalMessage("property.enterReceiptNumber")); 
		 }
	}	
	/*if($("#checkDetail").prop('checked')==false){
		  errorList.push(getLocalMessage('unitdetails.selectCheckbox')); 
	}*/

	return errorList;
}
function validateFactorStatus(errorList){

	$('.factorQes').each(function(i){
		var level = i+1;
		var selectFactor = $.trim($('input[name="provisionalAssesmentMstDto.proAssfactor['+i+']"]:checked').val());
		if(selectFactor==0 || selectFactor=="")
		errorList.push(getLocalMessage("property.factorStatus")+" "+level);
		});
	
	return errorList;
}
// Method to calculate financial year from current date
function getChecklistAndCharges(element)
{
	var	formName =	"frmSelfAssessmentForm";
	var theForm	=	'#'+formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'SelfAssessmentForm.html?getCheckListAndCharges';
	var returnData =__doAjaxRequest(URL,'POST',requestData, false);
	var divName = '#validationDiv';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$(divName).show();
	var free=$('#free').val();
	if(free=='N'){
		$("#chargesDiv").show();
		$("#payment").show();
		$("#applicantDetails").show();
		$('#chekListChargeId').hide();
	}
	else
	{
		$('#chekListChargeId').show();
		$("#chargesDiv").show();
		$("#applicantDetails").hide();
		$('.required-control').next().children().addClass('mandColorClass');
	}
}

//function is used to return financial year based on year of Acquisition date  
function getFinancialYear(){
	$("#yearOfConstruc0").val('');
	var lastPaymentDetails= $("#lastpaymentMadeUpto").val();
	var yearOfAcq =$("#proAssAcqDate").val();
	if(lastPaymentDetails=='0' || lastPaymentDetails==null || lastPaymentDetails==undefined)		
	{
	var data = {"yearOfAcq" : yearOfAcq};
	var URL = 'SelfAssessmentForm.html?getFinancialYear'; 
	var returnData = __doAjaxRequest(URL, 'POST', data, false);	
	//clearTableData();		
	yearLimit(returnData);
	$('.firstUnitRow').each(function(i) {
		$("#year"+i).val(returnData);
		$("#hiddenYear"+i).val(returnData);
		
	});
	var data = {"finYearId" : returnData};
	var url = "SelfAssessmentForm.html?displayYear";
	var value=	__doAjaxRequest(url, 'post', data , false);
	$('.displayYearList').append('<option value="' + returnData + '" selected="selected">' + value  + '</option>');	
	}
	$(".datepicker2").datepicker("option","maxDate", yearOfAcq);
}

//Used to get Next Schedule cycle based on Last Payment made status
function getPaymentMadeDetails(){

	$("#yearOfConstruc0").val('');
	$("#proAssAcqDate").val('');

	var schDetId=	$("#lastpaymentMadeUpto option:selected").val();
	var data = {"schDetId" : schDetId};
	var URL = 'SelfAssessmentForm.html?getNextScheduleFromLastPayDet'; 
	
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	clearTableData();
	//yearLimit();
	yearLimit(returnData[1]);
	$("#year0").val(returnData[1]);
	$("#hiddenYear0").val(returnData[1]);
	$('#proAssAcqDate').datepicker('option', 'maxDate', new Date(returnData[0]));
}

/* Display unit detail YearList based on year of acquisition/last payment made status till current Year
 	e.g year of acquisition 2015 then
    Dropdown of year in unit details contains yearlist(2014-2015,2015-2016,2016-2017,2017-2018)*/
function yearLimit(finYearId){
	$('.displayYearList').html('');
	var data = {"finYearId" : finYearId};
	var url = "SelfAssessmentForm.html?displayYearListBasedOnDate";
	var returnData=	__doAjaxRequest(url, 'post', data , false);
	$('.displayYearList').append('<option value="0">select Year</option>');
	$.each( returnData, function( key, value ) {		
		 $('.displayYearList').append('<option value="' + key + '">' + value  + '</option>');	 
		});
}

//Method called when user change Year of acquisition Date/ Last Payment mode status
function clearTableData(){

	$("#unitDetailTable").find("tr:gt(2)").remove();
	$('.secondUnitRow:last').addClass('in');
	$("#unitDetailTable tr").find("input:text").val();
	$("#unitDetailTable tr").find("select").val();
	$("#unitNo0").val('1');
	
	$('.unitSpecificInfo').html('');
	$('.factorSet').prop('checked',false);  
	$('.factorStatus').prop('checked',false);
	//$(".selectUnit ").val('1');
	//$('#checkDetail').prop('checked', false)
	reorderFirstRow();
}

function reorderFirstRow(){
//	$(".firstUnitRow").find("select:eq(1)").attr("id", "assdUnitTypeId");
	$(".firstUnitRow").find("select:eq(1)").attr("id", "assdFloorNo");
	$(".firstUnitRow").find("select:eq(2)").attr("id", "assdOccupancyType");
	$(".firstUnitRow").find("select:eq(3)").attr("id", "legal");
	$(".firstUnitRow").find("select:eq(4)").attr("id", "userChargesType");
	$(".firstUnitRow").find("select:eq(5)").attr("id", "assdConstruType");
	$(".firstUnitRow").find("select:eq(6)").attr("id", "assdUsagetype1");
	$(".firstUnitRow").find("select:eq(7)").attr("id", "assdUsagetype2");
	$(".firstUnitRow").find("select:eq(8)").attr("id", "assdUsagetype3");
	$(".firstUnitRow").find("select:eq(9)").attr("id", "assdUsagetype4");
	$(".firstUnitRow").find("select:eq(10)").attr("id", "assdUsagetype5");
//	$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdRoadFactor");
	
	//$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdOccupancyType");
	$(".secondUnitRow").find("select:eq(1)").attr("id", "natureOfProperty1");
	$(".secondUnitRow").find("select:eq(2)").attr("id", "natureOfProperty2");
	$(".secondUnitRow").find("select:eq(3)").attr("id", "natureOfProperty3");
	$(".secondUnitRow").find("select:eq(4)").attr("id", "natureOfProperty4");
	$(".secondUnitRow").find("select:eq(5)").attr("id", "natureOfProperty5");
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
			if(ConstructFlag != null && ConstructFlag == 'Y'){

				$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
				$(this).find("input:text:eq(2)").attr("id", "electricBillUnitNo"+i);
				$(this).find("input:text:eq(3)").attr("id", "electricConsumerNo"+i);
				$(this).find("input:text:eq(4)").attr("id", "electricMeterNo"+i);
				$(this).find("input:text:eq(5)").attr("id", "occupierName"+i);
				if(assType == 'C' && assType != 'BIF'){
				//	$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i);
					//$(this).find("input:hidden:eq(1)").attr("id", "hiddenFirstAssesmentDate"+i);
					
					//$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i).prop("readonly", true);
				}else if(assType != 'BIF'){					
					$(this).find("input:text:eq(6)").attr("id", "actualRent"+i);
				}
				
				$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				$(this).find("input:text:eq(2)").attr("onchange", "successErrorCheck("+(i)+")");
				if(assType != 'BIF'){				
					$(this).find("input:text:eq(7)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(8)").attr("id", "taxableArea"+i);
//					$(this).find("input:text:eq(6)").attr("id", "constructPermissionNo"+i);
//					$(this).find("input:text:eq(7)").attr("id", "permissionUseNo"+i);
//					$(this).find("input:text:eq(8)").attr("id", "assessmentRemark"+i);
				}else{
					//$(this).find("input:text:eq(2)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
					$(this).find("input:text:eq(5)").attr("id", "constructPermissionNo"+i);
					$(this).find("input:text:eq(6)").attr("id", "permissionUseNo"+i);
					$(this).find("input:text:eq(7)").attr("id", "assessmentRemark"+i);
				}
			}else{				
				//$(this).find("input:text:eq(1)").attr("id", "yearOfConstruc"+i);
				if(assType == 'C' && assType != 'BIF'){
					//$(this).find("input:text:eq(1)").attr("id", "lastAssesmentDate"+i);
					//$(this).find("input:hidden:eq(1)").attr("id", "hiddenFirstAssesmentDate"+i);
					
					//$(this).find("input:text:eq(2)").attr("id", "lastAssesmentDate"+i).prop("readonly", true);
				}else if(assType != 'BIF'){				
					$(this).find("input:text:eq(1)").attr("id", "actualRent"+i);//changed
				}
				
				//$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
				if(assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
//					$(this).find("input:text:eq(5)").attr("id", "constructPermissionNo"+i);
//					$(this).find("input:text:eq(6)").attr("id", "permissionUseNo"+i);
//					$(this).find("input:text:eq(7)").attr("id", "assessmentRemark"+i);
				}else{
					$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
					$(this).find("input:text:eq(2)").attr("id", "carpetArea"+i);
					$(this).find("input:text:eq(3)").attr("id", "taxableArea"+i);
					$(this).find("input:text:eq(4)").attr("id", "constructPermissionNo"+i);
					$(this).find("input:text:eq(5)").attr("id", "permissionUseNo"+i);
					$(this).find("input:text:eq(6)").attr("id", "assessmentRemark"+i);
				}
			}
			
			//$(this).find("input:text:eq(1)").attr("id", "yearOfConstruc"+i).prop("readonly", true);
			
			
			
			$(this).find("select:eq(4)").attr("id", "userChargesType"+i);
			$(this).find("select:eq(5)").attr("id", "assdConstruType"+i);
			
			
			$(this).find("a:eq(0)").attr("data-target", "#group-of-rows-"+i);
			$(this).find("a:eq(0)").attr("aria-controls", "#group-of-rows-"+i);
			$(this).find("a:eq(1)").attr("id", "deleteRow_"+i);
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
			/*if($("#checkDetail").prop('checked')==false){
				$("#unitNo"+i).val(incr);
			}*/
			$("#unitNo"+i).val(incr);
			$(this).find("select:eq(0)").attr("name","");
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitNo");
			$(this).find("input:hidden:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].faYearId");
//			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitTypeId");
			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdFloorNo");
			$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdOccupancyType");
			$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].legal");
			if(ConstructFlag != null && ConstructFlag == 'Y'){
				
				$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
				$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricBillUnitNo");
				$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricConsumerNo");
				$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricMeterNo");
				$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].occupierName");
				
				if(assType == 'C' && assType != 'BIF'){
					//$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].lastAssesmentDate");
					$(this).find("input:hidden:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");
				}else if(assType != 'BIF'){				
					$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");
				}
				if(assType != 'BIF'){
					//newly chnaged
					$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
//					$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
//					$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
//					$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}else{
					//$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}
			} else{
	            // $(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdYearConstruction");
				
				if(assType == 'C' && assType != 'BIF'){
				//	$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].lastAssesmentDate");
					//$(this).find("input:hidden:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
				}else if(assType != 'BIF'){
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");
				}
				if(assType != 'BIF'){
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
//					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
//					$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
//					$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}else{
					$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
					$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
					$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
					$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
					$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
					$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				}
			}
			
			
			$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].userChargesType");
			$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdConstruType");
			$(this).find("select:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype1");
			$(this).find("select:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype2");
			$(this).find("select:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype3");
			$(this).find("select:eq(9)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype4");
			$(this).find("select:eq(10)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype5");
			
			//Newly Added	
			if(assType != 'BIF'){
//				$(this).find("input:checkbox:eq(0)").attr("id", "legal" + i);
//				$(this).find("input:checkbox:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].legal");
			}else{
				$(this).find("input:checkbox:eq(0)").attr("id", "assdBifurcateNo" + i);
				$(this).find("input:checkbox:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].assdBifurcateNo");
				$(this).find("input:checkbox:eq(1)").attr("id", "legal" + i);
				$(this).find("input:checkbox:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].legal");
			}			
			$(this).find('.addRoomClass').attr("id", "addRoom" + i);
			$(this).find(".addRoomClass").attr("onclick", "addRoomDetails("+(i)+")");
			
			$(this).find('.duppliquer').attr("id", "duppliquer" + i);
			$(this).find(".duppliquer").attr("onclick", "duppliquerDet("+(i)+")");
			
			if($('#draftApplication').val() === 'Y'){
				$(this).find('.dupFlatNo').attr("id", "flatNo" + i);
				$(this).find(".dupFlatNo").attr("onblur", "getFlatValidation("+(i)+")");				
			}
			//		
			$(this).find('.delete').attr("id", "delete_" + i);
			$(this).find(".delete").attr("onclick", "removeRow("+(i)+")");
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
			datePickerLogic();
			
		});
		
		$(classNamesecond).each(function(j) {
			var utp=j;
			if(j>0){
			utp=j*6;
			}
			$(".datepicker-cpdate").removeClass("hasDatepicker");
//			$(this).find("select:eq(0)").attr("id", "assdRoadFactor"+j);
			$(this).find("select:eq(0)").attr("id", "assdOccupancyType"+j);
//			$(this).find("input:text:eq(0)").attr("id", "proAssdAnnualRent"+j);
			$(this).find("input:text:eq(0)").attr("id", "occupierName"+j);		
//			$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(j)+")");
			$(this).attr("id", "group-of-rows-"+j);
			
			///newly dded
			$(this).find("input:text:eq(1)").attr("id", "occupierNameReg"+j);		
			$(this).find("input:text:eq(2)").attr("id", "occupierMobNo"+j);		
			$(this).find("input:text:eq(3)").attr("id", "occupierEmail"+j);
			$(this).find("input:text:eq(4)").attr("id", "constructPermissionNo"+j);
			$(this).find("input:text:eq(5)").attr("id", "permissionUseNo"+j);
			$(this).find("input:text:eq(6)").attr("id", "assessmentRemark"+j);
			$(this).find("input:text:eq(7)").attr("id", "constructPermissionDate"+j);
			$(this).find("input:text:eq(8)").attr("id", "actualRent"+j);
			/*if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
				$(".actualRent"+j).hide();
				$("#actualRent"+j).hide();
			}*/
			//
			
			$(this).find("select:eq(1)").attr("id", "natureOfProperty"+utp);
			$(this).find("select:eq(2)").attr("id", "natureOfProperty"+(utp+1));
			$(this).find("select:eq(3)").attr("id", "natureOfProperty"+(utp+2));
			$(this).find("select:eq(4)").attr("id", "natureOfProperty"+(utp+3));
			$(this).find("select:eq(5)").attr("id", "natureOfProperty"+(utp+4));

			// re-ordering path binding
//			$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdRoadFactor");
			$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdOccupancyType");
//			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdAnnualRent");
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierName");
			
			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty1");
			$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty2");
			$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty3");
			$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty4");
			$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty5");
			
			//newly added
			$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierNameReg");
			$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierMobNo");
			$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierEmail");
			$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].constructPermissionNo");
			$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].permissionUseNo");
			$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assessmentRemark");
			$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].constructPermissionDate");
			$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].actualRent");
			//	
			$(".datepicker-cpdate").datepicker({
		        dateFormat: 'dd/mm/yy',		
				changeMonth: true,
				changeYear: true
			});
			var langFlag = getLocalMessage('admin.lang.translator.flag');
			if(langFlag ==='Y'){
				$("#occupierName"+j).keyup(function(event){
					var no_spl_char;
					no_spl_char = $("#occupierName"+j).val().trim();
					if(no_spl_char!='')
					{
						commonlanguageTranslate(no_spl_char,'occupierNameReg'+j,event,'');
					}else{
						$("#occupierNameReg"+j).val('');
					}
				});
				
				}
		});
}




function downloadSheet(){
		
		window.location.href='SelfAssessmentForm.html?downloadSheet';

}


function resetFormData(resetBtn) {
    
$("#postMethodForm").prop('action', '');
$("#postMethodForm").prop('action', 'SelfAssessmentForm.html');
$("#postMethodForm").submit();
	
	/*cleareFile(resetBtn);  
    
    if (resetBtn && resetBtn.form) {
            $(resetBtn.form)[0].reset();
            $('.alert').html('');
        	$("#unitDetailTable").find("tr:gt(2)").remove();
        	$('.secondUnitRow:last').addClass('in');        	      
        	$('.unitSpecificInfo').html('');       
        	$('#isCorrespondenceAddressSame').prop('checked', true);
        	reorderFirstRow();
        	yearLength();
    };*/
 }
   



function backToFirstLevelAuth(obj){
	var data = {};
	var URL = 'SelfAssessmentAuthorization.html?backToFirstLevelAuth';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	var divName = '.AuthContent';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}

function compareBeforeAfterAuth(){
	var errorList = [];
	errorList = validateRefPropNo(errorList);
	errorList = ValidateMasterDetails(errorList);
	errorList = ValidateOwnerDetails(errorList);
	errorList = validateUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateFactorStatus(errorList);
	errorList = validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	if (errorList.length == 0) {
		var data = {};
		var URL = 'SelfAssessmentAuthorization.html?compareBeforeAfterAuth';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		var divName = '.content';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
	} else {
		showVendorError(errorList);
	}
	
}

/*function compareBeforeAfterAuth(element){ 
	var errorList = [];
	//if($('#checkDetail').is(":checked")){  
		
		// Display validation msg for deleted year  
		
		var finYearId= $("#hiddenYear0").val();
		var data = {"finYearId" : finYearId};
		var URL = 'SelfAssessmentForm.html?getFinanceYearListFromGivenDate';
		var returnData2 = __doAjaxRequest(URL, 'POST', data, false);
		yearList=returnData2;
		var returnDataLength =yearList.length;
		var tableLength=$("#unitDetailTable tr").length;	
	
		for(var i=0; i<returnDataLength;i++){
		var firstYearList=yearList[i];   
		var yearDelete=	$('#year0').find("option[value = '" + firstYearList + "']").text();
		var count=0;
		
		for(var j=0;j<tableLength;j++) {
			if(j%2!=0)
			{
				var secondYearList=$("#unitDetailTable").find('tr:eq(\"'+j+'\")').find("select:eq(0)").val();			
				if(firstYearList==secondYearList)
					{				
					count++;
					}	
			}
		}
		if(count==0)
		{
		errorList.push(getLocalMessage("unitdetails.Enterdetailsfor ")+" "+yearDelete); 
		}
	}
//	}
	
	errorList = ValidateTick(errorList);	
	if (errorList.length == 0) {

	//if($("#checkDetail").is(':checked')){
	
		var theForm	=	'#frmSelfAssessmentForm';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'SelfAssessmentAuthorization.html?compareBeforeAfterAuth';
		var URL = 'SelfAssessmentForm.html?proceed';

		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		if(returnData)
		{
			var divName = '.content';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
		}

	//}
	}
	else {
		showVendorError(errorList);
	}
}*/



function yearLength(){
	
	var dateFields = $('.dateClass');
    dateFields.each(function () {
            var fieldValue = $(this).val();
            if (fieldValue.length > 10) {
                    $(this).val(fieldValue.substr(0, 10));
            }
    })
}

function showVendorError(errorList){
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

function fetchPropertyDetilsByMobileNo() {
	var mobNoLen=$("#assoMobileno").length;	
	var mobileNo=$("#assoMobileno").val();	
	if($('#assoMobileno').val().length==10){
	
	var requestData = {
		'mobileNo' : mobileNo,
	};
	var url = "SelfAssessmentForm.html?fetchPropertyDetilsByMobileNo";
	$
			.ajax({
				url : url,
				data : requestData,
				type : 'POST',
				async : false,
				dataType : '',
				success : function(response) {
					if(response!=""){
					var divName = '.child-popup-dialog';

					$(divName).removeClass('ajaxloader');
					$(divName).html(response);
					prepareTags();
					$(divName).removeClass('fancybox-close');
					showModalBoxWithoutClose(divName);
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					var errorList = [];
					errorList
							.push(getLocalMessage("admin.login.internal.server.error"));
					showError(errorList);
				}
			});
	}
}

function fetchPropertyDetilsByMobileNoWithId(id) {
	var mobileNo=$("#assoMobileno_"+id).val();	
	var mobileNoLen=$("#assoMobileno_"+id).val().length;	
	if(mobileNoLen==10){
	
	var requestData = {
		'mobileNo' : mobileNo,
	};
	var url = "SelfAssessmentForm.html?fetchPropertyDetilsByMobileNo";
	$
			.ajax({
				url : url,
				data : requestData,
				type : 'POST',
				async : false,
				dataType : '',
				success : function(response) {
					if(response!=""){
					var divName = '.child-popup-dialog';

					$(divName).removeClass('ajaxloader');
					$(divName).html(response);
					prepareTags();
					$(divName).removeClass('fancybox-close');
					showModalBoxWithoutClose(divName);
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					var errorList = [];
					errorList
							.push(getLocalMessage("admin.login.internal.server.error"));
					showError(errorList);
				}
			});
	}
}





function closePopup() {
	$('.child-popup-dialog').hide();
	disposeModalBox();
	$.fancybox.close();
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
	
	var billMethod = $("#billMethod").find("option:selected").attr('code');
	
	if(value == 'OWN'){
		var ownerName = null;
		if(ownerTypeCode == 'JO'){
			ownerName = $("#assoOwnerName_0").val();
			//Newly added
			//assoOwnerNameReg = $("#assoOwnerNameReg_0").val();
			assoMobileno = $("#assoMobileno_0").val();
			emailId = $("#emailId_0").val();			
			//
		}else{
			ownerName = $("#assoOwnerName").val();
			//Newly added
			//assoOwnerNameReg = $("#assoOwnerNameReg").val();
			assoMobileno = $("#assoMobileno").val();
			emailId = $("#emailId").val();			
			//
		}
		
		if(billMethod != 'I'){
			if((index != 'e') && (index > '0' || index == '0')){
				$("#occupierName" + index).val('');
				//Newly added
				$("#occupierNameReg"+ index).val('');
				$("#occupierMobNo"+ index).val(assoMobileno);
				$("#occupierEmail"+ index).val(emailId);
			}else{
				//$("#occupierName").val(ownerName);
				$("#occupierName").val('');
				//Newly added
				//$("#occupierNameReg").val(assoOwnerNameReg);
				$("#occupierNameReg").val('');
				$("#occupierMobNo").val(assoMobileno);
				$("#occupierEmail").val(emailId);
			}
		}else{
			$("#occupierName").val('');
			$("#occupierNameReg").val('');
			$("#occupierMobNo").val('');
			$("#occupierEmail").val('');
		}
		
		//newly added
		$('.actualRentClass').hide();
		$('#actualRent'+index).val('');
	}else{
		if((index != 'e') && (index > '0' || index == '0')){
			$("#occupierName" + index).val('');
			$("#occupierNameReg" + index).val('');
		}else{
			$("#occupierName").val('');
			$("#occupierNameReg").val('');
		}
		//newly added
		$('.actualRentClass').show();
	}
}

function ValidateMasterDetails(errorList){

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
	
	//var firstAssessmentDate=$("#firstAssessmentDate").val();
	if(ward=="" || ward=='0' || ward == undefined){
		errorList.push(getLocalMessage("property.valid.ward"));
	}
	if(zone=="" || zone=='0' || zone == undefined){
		errorList.push(getLocalMessage("property.valid.zone"));
	}
	if(roadType=="" || roadType=='0' || roadType == undefined){
		errorList.push(getLocalMessage("property.valid.roadType"));
	}
	if(billMethod=="" || billMethod=='0' || billMethod == undefined){
		errorList.push(getLocalMessage("property.valid.billMethod"));
	}
	if(proAssAcqDate=="" || proAssAcqDate=='0' || proAssAcqDate == undefined){
		errorList.push(getLocalMessage("property.valid.firstAssessmentDate"));
	}
	/*if(totalplot==""  || totalplot == undefined){
		errorList.push(getLocalMessage("property.valid.totalplot"));
	}
	if (totalplot == '0.00') {
		errorList.push(getLocalMessage("property.valid.validTotalplot"));
	}*/
	if(propAddress=="" || propAddress==null || propAddress == undefined){
		errorList.push(getLocalMessage("property.valid.propAddress"));
	}
	if(propVillage=="" || propVillage=='0' || propVillage == undefined){
		errorList.push(getLocalMessage("property.valid.proparea"));
	}
	/*if(clusterNo=="" || clusterNo=='0' || clusterNo == undefined){
		errorList.push(getLocalMessage("property.valid.clusterNo"));
	}*/
	/*if(assLandType=="" || assLandType=='0' || assLandType == undefined){
		errorList.push(getLocalMessage("property.valid.surveyNo"));
	}*/
	if(natureOfproperty=="" || natureOfproperty=='0' || natureOfproperty == undefined){
		errorList.push(getLocalMessage("property.valid.propertyType"));
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
	
//	if(firstAssessmentDate=="" || firstAssessmentDate=='0' || firstAssessmentDate == undefined){
//		errorList.push(getLocalMessage("property.valid.firstAssessmentDate"));
//	}
	return errorList;
}

function ValidateOwnerDetails(errorList) {
	
	var ownerTypeId = $('#ownerTypeId option:selected').attr('code');
	if (ownerTypeId == "" || ownerTypeId == undefined) {
		errorList.push(getLocalMessage("property.valid.ownerTypeId"));
	}

	if (ownerTypeId == "JO") {
		$(".jointOwner")
				.each(
						function(i) {
													
							var assoOwnerName = $("#assoOwnerName_" + i).val();
						//	var assoOwnerNameReg = $("#assoOwnerNameReg_" + i)
							//		.val();
							var assoMobileno = $("#assoMobileno_" + i).val();
							var emailId = $("#emailId_" + i).val();
							if (assoOwnerName == ""
									|| assoOwnerName == undefined) {
								errorList
										.push(getLocalMessage("property.valid.assoOwnerName"));
							}
							/*if (assoOwnerNameReg == ""
									|| assoOwnerNameReg == undefined) {
								errorList
										.push(getLocalMessage("property.valid.assoOwnerNameReg"));
							}*/
							/*if (assoMobileno == "" || assoMobileno == undefined) {
								errorList
										.push(getLocalMessage("property.valid.assoMobileno"));
							}*/
							
							if (emailId != ""  && emailId != undefined) {
								var emailRegex = new RegExp(
										/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
								var valid = emailRegex.test(emailId);
								if (!valid) {
									errorList
											.push(getLocalMessage("property.valid.emailId"));
								}
							}
							/*if (emailId == "" || emailId == undefined) {
								errorList
										.push(getLocalMessage("property.valid.emailId"));
							}*/
						});
	} else if (ownerTypeId == "SO" || ownerTypeId == "O") {
		
		var assoOwnerName = $("#assoOwnerName").val();
		//var assoOwnerNameReg = $("#assoOwnerNameReg").val();
		var assoMobileno = $("#assoMobileno").val();
		var emailId = $("#emailId").val();
		if (assoOwnerName == "" || assoOwnerName == undefined) {
			errorList.push(getLocalMessage("property.valid.assoOwnerName"));
		}
		/*if (assoOwnerNameReg == "" || assoOwnerNameReg == undefined) {
			errorList.push(getLocalMessage("property.valid.assoOwnerNameReg"));
		}*/
	/*	if (assoMobileno == "" || assoMobileno == undefined) {
			errorList.push(getLocalMessage("property.valid.assoMobileno"));
		}*/
		if (emailId != "" && emailId != undefined) {
			var emailRegex = new RegExp(
					/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
			var valid = emailRegex.test(emailId);
			if (!valid) {
				errorList
						.push(getLocalMessage("property.valid.emailId"));
			}
		}
		/*if (emailId == "" || emailId == undefined) {
			errorList.push(getLocalMessage("property.valid.emailId"));
		}*/
	}

	return errorList;
}

function validateRefPropNo(errorList){
	var errorList = [];
	var refPropNo =$("#refPropNo").val();
	var data = {"propNo" : refPropNo};
	var URL = 'SelfAssessmentForm.html?validateRefPropNo'; 
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	if(returnData){
		return errorList;
	} else {
		errorList.push(getLocalMessage("ref.propno.exist.validation"));
		showVendorError(errorList);
	}	
}

function viewApplicantDetails(){
	showloader(true);
	setTimeout(function () {
		var requestData = { showApplicantDetails: true };
		var URL = 'SelfAssessmentAuthorization.html?viewApplicantDetails';
		
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		if(returnData)
		{
			$(formDivName).html(returnData);
			reorderFirstRow();
			reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
			reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
			
		//	occupancyTypeChange1();   
			yearLength();
			
			reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
			
			var ownerType=null;
			 ownerType = $("#ownerTypeId" + " option:selected").attr("code");
			if(ownerType ==undefined){
				ownerType=$("#ownershipId").val();
			}
			var data1 = {"ownershipType" : ownerType};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
			$("#proceed").show();
			$("#checkList").hide();
			$("#ResetSelfAssForm").hide();
			//$('#checkDetail').prop('checked', true);
			window.open(returnData,'_blank' );
			
		}
		//yearLength();
		showloader(false);
	},1000);
}

function viewApplicantDetailsInNewTab() {
	showloader(true);

	setTimeout(function () {
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "SelfAssessmentAuthorization.html";
		form.target = "_blank";

		// Append Spring expected param
		const input = document.createElement("input");
		input.type = "hidden";
		input.name = "viewApplicantDetails";
		input.value = "true";
		form.appendChild(input);

		 //Optional: add CSRF token if Spring Security is used
		 const csrfToken = document.querySelector("input[name='_csrf']");
		 if (csrfToken) {
		     const csrf = document.createElement("input");
		     csrf.type = "hidden";
		     csrf.name = csrfToken.name;
		     csrf.value = csrfToken.value;
		     form.appendChild(csrf);
		 }

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);

		showloader(false);
	}, 1000);
}
	