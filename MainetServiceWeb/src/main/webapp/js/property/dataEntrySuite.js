var yearList;
var fileArray = [];

$(document).ready(function(){
//Newly added
//$('.actualRentClass').hide();
$("html, body").animate({ scrollTop: 0 }, "slow");
$('.secondUnitRow').removeClass('in');
$('.secondUnitRow:last').addClass('in');
$('#proAssAcqDate').attr('readonly', true);

// Select the billing method dropdown
var $billMethod = $('#billMethod');

// Check if the billing method dropdown exists
if ($billMethod.length > 0) {
    // Log the selected index for debugging
    console.log('Selected Index:', $billMethod[0].selectedIndex);

    // Check if any option is selected
    if ($billMethod[0].selectedIndex >= 0) {
        // Trigger the change event to handle visibility
        $billMethod.trigger('change');
    } else {
        console.log('No option is currently selected.');
        // Handle the case where no option is selected, if needed
    }
} else {
    console.error('Billing method dropdown not found.');
}

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

//datepicker function for construction completion date
$(".datepicker2").datepicker({
    dateFormat: 'dd/mm/yy',		
	changeMonth: true,
	maxDate: '-0d',
	changeYear: true,
	 /*  onSelect: function(selected) {
		   checkConstructionDate();
	    } */
});

// authorization page

/*var locId=$("#locId").val();
if(locId!=null && locId!='' && locId!=undefined && locId>0){
	var result=	getOperationalWardZone($("#orgId").val(),$("#deptId").val(),$("#locId").val());
	$("#wardZone").html(result);
}*/

$('#yearOfConstruc0').change(function(){	
	var yearOfAcquisition =  $("#proAssAcqDate").val();			
	if(yearOfAcquisition == '' || yearOfAcquisition == undefined){
			showAlertBoxForConstructionDate();
	}	
});

	var code = $('#billMethodDesc').val();
	if (code == "W") {
		$('.wholeArrears').show();
		$('.addArrearClass').prop("disabled", true);
	} else if (code == "I") {
		$('.wholeArrears').hide();
		$('.addArrearClass').prop("disabled", false);
	}

});

//If year of Acquisition date is not selected then show popUp message
function  checkConstructionDate()
{
	var yearOfAcquisition =  $("#proAssAcqDate").val();	
	if(yearOfAcquisition == '' || yearOfAcquisition == undefined){
		showAlertBoxForConstructionDate();
	}	
}

function showAlertBoxForConstructionDate(){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = 'Ok';
	
	message	+='<h4 class=\"text-center text-blue-2 padding-10\">Please select Year of Acquisition first</h4>';
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
	setTimeout(function(){
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
	
	var landType = $("#assLandType" + " option:selected").attr("code");
	if(landType!=undefined){
	errorList = validateLandDetails(errorList,landType);	
	}
	errorList = ValidateTick(errorList);	
	errorList = validateUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	errorList=validateUnitYear(errorList);
	if (errorList.length == 0) {
	/*if($("#checkDetail").is(':checked')){*/	
		var theForm	=	'#DataEntrySuite';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'DataEntrySuite.html?proceed';
		var returnData	=	__doAjaxRequest(URL, 'POST',requestData,false, 'html');
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			var divName = '.content';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
		}
		yearLength();
	}
	/*}*/
	else {
		showVendorError(errorList);
	}
	showloader(false);
	},2);
}

function nextToView(element){

	showloader(true);
	setTimeout(function(){

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


	var landType = $("#assLandType" + " option:selected").attr("code");
	if(landType!=undefined){
	errorList = validateLandDetails(errorList,landType);	
	}
	errorList = ValidateTick(errorList);
	errorList = ValidateMasterDetails(errorList);
	errorList = validateUnitDetailTable(errorList);	 // validate Unit detail table	
	errorList = validateFactorStatus(errorList);
	errorList=validateUnitSpecificInfoTable(errorList); // validate unit specific additional info table
	errorList=validateUnitYear(errorList);
	if (errorList.length == 0) {
		var theForm	=	'#DataEntrySuite';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'DataEntrySuite.html?nextToView';	
		var returnData	=	__doAjaxRequest(URL, 'POST',requestData,false, 'html');
		//var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			var divName = '.content';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
		}
		yearLength();
	}
	else {
		showVendorError(errorList);		
	}
	showloader(false);
	},2); 
}

function validateLandDetails(errorList,landType){
	var district=$("#assDistrict").val();
	var tehsil=$("#assTahasil").val();
	var village=$("#tppVillageMauja").val();
	var khasraNo=$("#displayKhaNo").val();
	var plotNo=$("#tppPlotNo").val();
	if(landType=='KPK'){
		if(district=="" || district=='0' || district == undefined){
			errorList.push(getLocalMessage("property.valid.selectDistrict"));
		}
		if(tehsil=="" || tehsil=='0' || tehsil == undefined){
			errorList.push(getLocalMessage("property.valid.selectTehsil"));
		}
		if(village=="" || village=='0' || village == undefined){
			errorList.push(getLocalMessage("property.valid.selectVillage"));
		}	
		if(khasraNo=="" || khasraNo=='0' || khasraNo == undefined){
			errorList.push(getLocalMessage("property.valid.selectKhasraNo"));
		}
	}
	if(landType=='NZL' || landType=='DIV'){
		/*if(district=="" || district=='0' || district == undefined){
		errorList.push(getLocalMessage("property.valid.selectDistrict"));
		}*/
		if(plotNo=="" || plotNo=='0' || plotNo == undefined){
			errorList.push(getLocalMessage("property.valid.selectPlotNo"));	
		}
	}
	return errorList;
}

//validation of unit details table
function validateUnitYear(errorList){
	$('.firstUnitRow').each(function(i) {	
		var year =$("#hiddenYear"+i).val();
		$('.firstUnitRow').each(function(j) {	
			var yearNext =$("#hiddenYear"+j).val();	
			 if(year!='' ||yearNext !=''  || year != '0' || yearNext !='0' ){	
				 if(year!=yearNext){
				 errorList.push(getLocalMessage('unitdetails.year.same')); 
				 }
			 }
		});	 
	});

	return errorList;
}
 
function nextToViewAfterArrear(element){ 

		var theForm	=	'#DataEntrySuite';
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'DataEntrySuite.html?nextToViewAfterArrear';
		var returnData = __doAjaxRequestValidationAccor(element,URL,'POST',requestData, false,'html');
		if(returnData)
		{
			var divName = '.content';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
		}
		yearLength();
}

 
function editDataEntry(obj){
	var ownerType = $("#ownershipId").val();
	var landTypePrefix=$(".landValue").val();
	var khasara=$('#displayEnteredKhaNo').val();
	var plot=$("#tppPlotNo").val();
	var	formName =	"DataEntrySuiteView";
	var theForm	=	'#'+formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'DataEntrySuite.html?editDataEntryForm';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
	$(formDivName).html(returnData);
	reorderFirstRow();
	reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
	//occupancyTypeChange1();   
	yearLength();
	reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
	var data1 = {"ownershipType" : ownerType};
	var URL1 = 'DataEntrySuite.html?getOwnershipTypesDiv';
	var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
	$("#owner").html(returnData1);
	reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
	$("#owner").show();
	
	/*
	var landTypePrefix = $("#landTypePrefix").val();*/
	if(landTypePrefix !=undefined && landTypePrefix!=""){

	var landData = {"landType" : landTypePrefix};
	var landURL = 'DataEntrySuite.html?getLandTypeDetails';
	var returnLandData = __doAjaxRequest(landURL, 'POST', landData, false);
	$("#landType").html(returnLandData);
	$("#landType").show();	
	}

	if(landTypePrefix=='KPK'){
		
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		$('#khasraNo').val(khasara);
		
		var khasraData = {"landType" : landTypePrefix,"khasara":khasara,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#displayKhaNo').val()!=""){				

		var khasraURL = 'DataEntrySuite.html?getKhasaraDetails';
		var returnkhasraData = __doAjaxRequest(khasraURL, 'POST', khasraData, false);		
		$("#showApiDetails").html(returnkhasraData);
		$("#showApiDetails").show();
		}		
	}
	else if(landTypePrefix=='NZL' || landTypePrefix=='DIV' ){
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		var mohallaId=$('#mohalla').val();
		var streetNo=$('#assStreetNo').val();	
		//var plotNo=$('#tppPlotNo').val();	
		$('#plotNo').val(plot);
		var data = {"landType" : landTypePrefix,"plotNo" : plot,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId,"mohallaId" : mohallaId,"streetNo" : streetNo};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#mohalla').val()!=undefined && $('#assStreetNo').val()!=undefined){				

		var URL = 'DataEntrySuite.html?getNajoolAndDiversionDetails';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);		
		$("#showApiDetails").html(returnData);
		$("#showApiDetails").show();
		}	
	}
	
	var yearOfAcq =$("#proAssAcqDate").val();	
	$(".datepicker2").datepicker("option","maxDate", yearOfAcq);
	//$('#checkDetail').prop('checked', true);
	//reOrderTaxTabIdSequence('.firstUnitRow');		//Tax Detail	
	
}

function backToFirstPage(obj){

	var data = {};
	var URL = 'DataEntrySuite.html?editDataEntryForm';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	reorderFirstRow();
	reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
	reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
	//occupancyTypeChange1();   
	yearLength();
	reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
	var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
	if (ownerType != undefined && ownerType != "") {
		var data1 = {
			"ownershipType" : ownerType
		};
		var URL1 = 'DataEntrySuite.html?getOwnershipTypesDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		$("#owner").show();
	}

	var landTypePrefix=$("#assLandType" + " option:selected").attr("code");
	if(landTypePrefix !=undefined && landTypePrefix!=""){

		var landData = {"landType" : landTypePrefix};
		var landURL = 'DataEntrySuite.html?getLandTypeDetails';
		var returnLandData = __doAjaxRequest(landURL, 'POST', landData, false);
		$("#landType").html(returnLandData);
		$("#landType").show();	
		}
	
	if(landTypePrefix=='KPK'){
		
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		var khasara=$("#tppPlotNoCs").val();
		//$('#khasraNo').val(khasara);
		
		var khasraData = {"landType" : landTypePrefix,"khasara":khasara,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#tppPlotNoCs').val()!=undefined){				

		var khasraURL = 'DataEntrySuite.html?getKhasaraDetails';
		var returnkhasraData = __doAjaxRequest(khasraURL, 'POST', khasraData, false);		
		$("#showApiDetails").html(returnkhasraData);
		$("#showApiDetails").show();
		}		
	}
	else if(landTypePrefix=='NZL' || landTypePrefix=='DIV' ){
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		var mohallaId=$('#mohalla').val();
		var streetNo=$('#assStreetNo').val();	
		var plot=$('#tppPlotNo').val();	
		//$('#plotNo').val(plot);
		var data = {"landType" : landTypePrefix,"plotNo" : plot,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId,"mohallaId" : mohallaId,"streetNo" : streetNo};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#mohalla').val()!=undefined && $('#assStreetNo').val()!=undefined && $('#tppPlotNo').val()!=undefined){				

		var URL = 'DataEntrySuite.html?getNajoolAndDiversionDetails';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);		
		$("#showApiDetails").html(returnData);
		$("#showApiDetails").show();
		}	
	}
	var yearOfAcq =$("#proAssAcqDate").val();	
	$(".datepicker2").datepicker("option","maxDate", yearOfAcq);
	//$('#checkDetail').prop('checked', true);
	//reOrderTaxTabIdSequence('.firstUnitRow');		//Tax Detail	
	
}

function ValidateTick(errorList){
	/*var landType = $("#assLandType" + " option:selected").attr("code");
	if($("#assLandType").val()!=""){
		if(landType=='KPK'){
			
			if($('#assDistrict').val()=="" || $('#assTahasil').val()=="" || $('#tppVillageMauja').val()=="" || $('#displayKhaNo').val()==''){
				errorList.push(getLocalMessage("Please select District,Tehsil,Village and khasra number")); 
			}			
		}
		else if (landType=='NZL' || landType=='DIV'){
			if($('#assDistrict').val()=="" || $('#assTahasil').val()=="" || $('#tppVillageMauja').val()=="" || $('#mohalla').val()=="" || $('#assStreetNo').val()=="" || $('#displayPlotNoList').val()==''){
				errorList.push(getLocalMessage("Please select District,Tehsil,Village,mohalla,sheet number and plot number")); 
			}
		}		
	}*/
	
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
	
	
/*	if($("#checkDetail").prop('checked')==false){
		  errorList.push(getLocalMessage('In order to proceed You need to Checked the checkbox inside Unit Details')); 
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
	var	formName =	"DataEntrySuite";
	var theForm	=	'#'+formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = 'DataEntrySuite.html?getCheckListAndCharges';
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
	var yearOfAcq =$("#proAssAcqDate").val();
	$(".datepicker2").datepicker("option","maxDate", yearOfAcq);
}

//Used to get Next Schedule cycle based on Last Payment made status
function getPaymentMadeDetails(){
	
	$("#yearOfConstruc0").val('');
	$("#proAssAcqDate").val('');

	var schDetId=	$("#lastpaymentMadeUpto option:selected").val();
	var data = {"schDetId" : schDetId};
	var URL = 'DataEntrySuite.html?getNextScheduleFromLastPayDet'; 
	 
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	clearTableData();
	//yearLimit();
	//yearLimit(returnData[1]);
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
	var url = "DataEntrySuite.html?displayYearListBasedOnDate";
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
	$(".firstUnitRow").find("select:eq(2)").attr("id", "assdConstruType");
	$(".firstUnitRow").find("select:eq(3)").attr("id", "assdUsagetype1");
	$(".firstUnitRow").find("select:eq(4)").attr("id", "assdUsagetype2");
	$(".firstUnitRow").find("select:eq(5)").attr("id", "assdUsagetype3");
	$(".firstUnitRow").find("select:eq(6)").attr("id", "assdUsagetype4");
	$(".firstUnitRow").find("select:eq(7)").attr("id", "assdUsagetype5");
//	$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdRoadFactor");
	$(".secondUnitRow ").find("select:eq(0)").attr("id", "assdOccupancyType");
	$(".secondUnitRow").find("select:eq(1)").attr("id", "natureOfProperty1");
	$(".secondUnitRow").find("select:eq(2)").attr("id", "natureOfProperty2");
	$(".secondUnitRow").find("select:eq(3)").attr("id", "natureOfProperty3");
	$(".secondUnitRow").find("select:eq(4)").attr("id", "natureOfProperty4");
	$(".secondUnitRow").find("select:eq(5)").attr("id", "natureOfProperty5");
}

//To reorder ID and Path
function reOrderUnitTabIdSequence(classNameFirst,classNamesecond) {

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
			$(this).find("input:text:eq(1)").attr("id", "yearOfConstruc"+i);
			
			$(this).find("input:text:eq(2)").attr("id", "firstAssesmentDate"+i);
			
			$(this).find("input:text:eq(1)").attr("onchange", "successErrorCheck("+(i)+")");
			$(this).find("input:text:eq(2)").attr("onchange", "successErrorCheck("+(i)+")");
			$(this).find("select:eq(2)").attr("id", "assdConstruType"+i);
			
			
			//Newly Added
			$(this).find("input:text:eq(3)").attr("id", "flatNo"+i);
			$(this).find("input:text:eq(4)").attr("id", "carpetArea"+i);
			$(this).find("input:text:eq(5)").attr("id", "taxableArea"+i);
			$(this).find("input:text:eq(6)").attr("id", "constructPermissionNo"+i);
			$(this).find("input:text:eq(7)").attr("id", "permissionUseNo"+i);
			$(this).find("input:text:eq(8)").attr("id", "assessmentRemark"+i);
			$(this).find("input:checkbox:eq(0)").attr("id", "legal" + i);
			$(this).find('.addRoomClass').attr("id", "addRoom" + i);
			$(this).find(".addRoomClass").attr("onclick", "addRoomDetails("+(i)+")");
			
			$(this).find('.addArrearClass').attr("id", "addArrearForFlat" + i);
			$(this).find(".addArrearClass").attr("onclick", "addArrearDetailsForFlat("+(i)+")");
			//
			
			$(this).find("a:eq(0)").attr("data-target", "#group-of-rows-"+i);
			$(this).find("a:eq(0)").attr("aria-controls", "#group-of-rows-"+i);
			$(this).find("a:eq(1)").attr("id", "deleteRow_"+i);
			var utp=i;
			if(i>0){
			utp=i*6;
			}
			$(this).find("select:eq(3)").attr("id", "assdUsagetype"+utp);
			$(this).find("select:eq(4)").attr("id", "assdUsagetype"+(utp+1));
			$(this).find("select:eq(5)").attr("id", "assdUsagetype"+(utp+2));
			$(this).find("select:eq(6)").attr("id", "assdUsagetype"+(utp+3));
			$(this).find("select:eq(7)").attr("id", "assdUsagetype"+(utp+4));
			
			// re-ordering path binding
//			if($("#checkDetail").prop('checked')==false){
				$("#unitNo"+i).val(incr);
//			}
		
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitNo");
			$(this).find("input:hidden:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].faYearId");
//			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitTypeId");
			$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdFloorNo");
			$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdYearConstruction");
			$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdConstruType");
			
			$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].firstAssesmentDate");
			
			$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype1");
			$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype2");
			$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype3");
			$(this).find("select:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype4");
			$(this).find("select:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype5");
			
						
			//newly added
			$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
			$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
			$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
			$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
			$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
			$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
			$(this).find("input:checkbox:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + i + "].legal");
			//
			
			if(i>0)
			{
//				if($("#checkDetail").prop('checked')==false){
					var a=i-1;
					$("#hiddenYear"+i).val($("#hiddenYear"+a).val());
					$("#year"+i).val($("#hiddenYear"+a).val());
//				}
			}
			var unitNoFact=$("#unitNo"+i).val();
			if($(".selectUnit option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined){				
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
			$(this).find("select:eq(0)").attr("id", "assdOccupancyType"+j);
//			$(this).find("input:text:eq(0)").attr("id", "proAssdAnnualRent"+j);
			$(this).find("input:text:eq(0)").attr("id", "occupierName"+j);		
			//$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(j)+")");
			$(this).attr("id", "group-of-rows-"+j);
			
			///newly dded
			$(this).find("input:text:eq(1)").attr("id", "occupierNameReg"+j);		
			$(this).find("input:text:eq(2)").attr("id", "occupierMobNo"+j);		
			$(this).find("input:text:eq(3)").attr("id", "occupierEmail"+j);
			$(this).find("input:text:eq(4)").attr("id", "actualRent"+j);
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
			$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].actualRent");
			//		
		});
}



function downloadSheet(){
		
		window.location.href='DataEntrySuite.html?downloadSheet';

}
 
function resetFormData(resetBtn) {
    cleareFile(resetBtn);  
    if (resetBtn && resetBtn.form) {
            $(resetBtn.form)[0].reset();
            $('.alert').html('');
        	$("#unitDetailTable").find("tr:gt(2)").remove();
        	$('.secondUnitRow:last').addClass('in');        	      
        	$('.unitSpecificInfo').html('');
      
        	$('#isCorrespondenceAddressSame').prop('checked', true);
        	reorderFirstRow();
        	yearLength();
    };
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
 
function saveDataEntryFrom(element,flag){

	//var value="DataEntrySuite";
	//this.element['form']=value;
	if(flag=='A'){
		 
		return saveOrUpdateForm(element,"Your application saved successfully!", 'AdminHome.html', 'saveDataEntryForm');
	}
	else
		//return saveOrUpdateForm(element,"Your application saved successfully!", 'DataEntrySuite.html', 'saveDataEntryForm');
		return saveOrUpdateForms(element,"Your application saved successfully!", 'DataEntrySuite.html', 'saveDataEntryForm');
	
	}


function getLandApiDetails(obj){
	var landTypePrefix=$(".landValue").val();
	var data = {};
	var URL = 'DataEntrySuite.html?getLandTypeApiDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);		
	$("#showAuthApiDetails").html(returnData);
	$("#showAuthApiDetails").show();	

	
}

function BackToSearch(){
	 var data={};
			var URL='DataEntrySuite.html?backToSearch';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$(formDivName).html(returnData);
}


function openUpdateForm(formName,actionParam)
{		
	showloader(true);
	setTimeout(function(){codeopenUpdateForm(formName,actionParam)},2);	
}
function codeopenUpdateForm(formName,actionParam){
	//common function for grid rewrite here for more functionality

	var values=formName.split('_');
	var one=values[0];
	var two=values[1];
	var len=one.length;
	var splitStr = one.substr(18, len);	
	
	//var theForm	=	'#'+one;

	var divName	=	".widget-content";

	//var url	=	$(theForm).attr('action');

	if (!actionParam) {}
	else
	{
		url+='?'+actionParam;
	}
	if(two!=undefined){
		url="DataEntrySuite.html?edit&rowId="+splitStr+'_'+two;
	}else{
		url="DataEntrySuite.html?edit&rowId="+splitStr;
	}
	var theForm	=	'#DataEntrySuite';	
	var requestData = __serializeForm(theForm);

	var ajaxResponse = __doAjaxRequest(url, 'POST', requestData, false);

	if(ajaxResponse==""){
		showAlertBoxForPropertyEdit();
		}
	else{
		var	formName =	"DataEntrySuiteView";
		var theForm	=	'#'+formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'DataEntrySuite.html?editDataEntryForm';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);

		$(formDivName).html(returnData);
		var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
		var landTypePrefix=$("#assLandType"+ " option:selected").attr("code");
		
		reorderFirstRow();
		reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
		//occupancyTypeChange1();   
		yearLength();
		reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
		var data1 = {"ownershipType" : ownerType};
		var URL1 = 'DataEntrySuite.html?getOwnershipTypesDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
		$("#owner").show();
		
		if(landTypePrefix !=undefined){
		var landData = {"landType" : landTypePrefix};
		var landURL = 'DataEntrySuite.html?getLandTypeDetails';
		var returnLandData = __doAjaxRequest(landURL, 'POST', landData, false);
		$("#landType").html(returnLandData);
		var plot=$("#getEnteredPlotNo").val();
		var khasara=$('#tppPlotNoCs').val();
		$("#landType").show();	
		}


		if(landTypePrefix=='KPK'){
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		$('#khasraNo').val(khasara);

		var khasraData = {"landType" : landTypePrefix,"khasara":khasara,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined){				

		var khasraURL = 'DataEntrySuite.html?getKhasaraDetails';
		var returnkhasraData = __doAjaxRequest(khasraURL, 'POST', khasraData, false);		
		$("#showApiDetails").html(returnkhasraData);
		$("#showApiDetails").show();
		}
		}
		else if(landTypePrefix=='NZL' || landTypePrefix=='DIV' ){
			var districtId=$('#assDistrict').val();
			var TehsilId=$('#assTahasil').val();
			var villageId=$('#tppVillageMauja').val();
			var mohallaId=$('#mohalla').val();
			var streetNo=$('#assStreetNo').val();	
			$("#plotNo").val(plot);
			if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#mohalla').val()!=undefined && $('#assStreetNo').val()!=undefined ){				
			var data = {"landType" : landTypePrefix,"plotNo" : plot,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId,"mohallaId" : mohallaId,"streetNo" : streetNo};
			var URL = 'DataEntrySuite.html?getNajoolAndDiversionDetails';
			var returnData = __doAjaxRequest(URL, 'POST', data, false);		
			$("#showApiDetails").html(returnData);
			$("#showApiDetails").show();
		}
		}	
		var yearOfAcq =$("#proAssAcqDate").val();	
		$(".datepicker2").datepicker("option","maxDate", yearOfAcq);
		
	}
}

function showAlertBoxForPropertyEdit(){
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls = 'Ok';
	
	message	+='<h4 class=\"text-center text-blue-2 padding-10\">This Property is not Editable</h4>';
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

function closeAlertForm()
{
	var childDivName	=	'.child-popup-dialog';
	$(childDivName).hide();
	$(childDivName).empty();
	disposeModalBox();
	$.fancybox.close();
}

function resetDESSeachGrid(){
	 var data={};
			var URL='DataEntrySuite.html?resetDESSeachGrid';
			var returnData=__doAjaxRequest(URL, 'POST', data, false);
			$(formDivName).html(returnData);
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
		//newly added
		$('.actualRentClass').hide();
		$('#actualRent'+index).val('');
	}else{
		if((index != 'e') && (index > '0' || index == '0')){
			$("#occupierName" + index).val('');
		}else{
			$("#occupierName").val('');
		}
		//newly added
		$('.actualRentClass').show();
	}
}

function saveOrUpdateForms(obj, successMessage, successUrl, actionParam)
{
 	
	if (!actionParam) {
		
		actionParam = "save";
	}
	
	return doFormActionForSavee(obj,successMessage, actionParam, true , successUrl);
}

function doFormActionForSavee(obj,successMessage, actionParam, sendFormData, successUrl)
{

	var	formName =	"DataEntrySuiteView";
	var theForm	=	'#'+formName;
	
	             var requestData = {};
	
				if (sendFormData) {
					
					requestData = __serializeForm(theForm);
				}
			
				var url	=	$(theForm).attr('action')+'?' + actionParam;
				 
				var returnData=__doAjaxRequestForSave(url, 'post',requestData, false,'',obj);
				if ($.isPlainObject(returnData))
				{
					var message = returnData.command.message;
					
					var hasError = returnData.command.hasValidationError;
					
					if (!message) {
						message = successMessage;
					}
					
					if(message && !hasError)
						{
						   	if(returnData.command.hiddenOtherVal == 'SERVERERROR')
						   		
						   		showSaveResultBox(returnData, message, 'AdminHome.html');
						   	
						   	else
						   		
						   		showSaveResultBox(returnData, message, successUrl);
						}
					else if(hasError)
					{
						$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');	
					}
					else
						return returnData;
					
				}
				else if (typeof(returnData) === "string")
				{
					$(formDivName).html(returnData);	
					prepareTags();
				}
				else 
				{
					alert("Invalid datatype received : " + returnData);
				}
				showloader(false);
				return false;				
	
}

function enableArrears(element){
	var code=$('#billMethod').find(':selected').attr('code');
	$('#billMethodDesc').val(code);
	if(code=="W")
	{
		$('.wholeArrears').show();
		$('.addArrearClass').prop("disabled", true);
	}else{		
		$('.wholeArrears').hide();
		$('.addArrearClass').prop("disabled", false);
	}
}

function handleBillingMethodChange(element) {

try {

    // Ensure 'element' is a valid DOM element
    if (!element || !element.options || !element.options[element.selectedIndex]) {
        console.error('Invalid element or missing options property:', element);
        return;
    }
var selectedBillingMethodCode  = element.options[element.selectedIndex].getAttribute('code');

// Store the selected billing method code in local storage
localStorage.setItem('selectedBillingMethodCode', selectedBillingMethodCode);

// Reset visibility or inclusion based on the selected billing method
if (selectedBillingMethodCode  === 'W') {
    $('#utilitySection').show();
    $('.electricDetailsColumn').hide();
} else if (selectedBillingMethodCode  === 'I') {
    $('#utilitySection').hide();
    $('.electricDetailsColumn').show();
} else {
    // Handle other cases if needed
    $('#utilitySection').hide();
    $('.electricDetailsColumn').hide();
}
}
catch (error) {
    console.error('Error in handleBillingMethodChange:', error);
}
}


function ValidateMasterDetails(errorList){

	var ward=$('#assWard1 option:selected').attr('code');
	var zone=$('#assWard2 option:selected').attr('code');
	var roadType=$('#propLvlRoadType option:selected').attr('code');
	var billMethod=$('#billMethod option:selected').attr('code');
	var proAssAcqDate=$("#proAssAcqDate").val();
	var totalplot=$("#totalplot").val();
	var firstAssessmentDate=$("#firstAssessmentDate").val();
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
		errorList.push(getLocalMessage("property.valid.proAssAcqDate"));
	}
	if(totalplot=="" || totalplot=='0' || totalplot == undefined){
		errorList.push(getLocalMessage("property.valid.totalplot"));
	}
	if(firstAssessmentDate=="" || firstAssessmentDate=='0' || firstAssessmentDate == undefined){
		errorList.push(getLocalMessage("property.valid.firstAssessmentDate"));
	}
	return errorList;
}