<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script src="assets/libs/fullcalendar/moment.min.js"></script>
<script>
var previousMrdDate;
$(document).ready(function(e){

var value = $('#mtrStatus' + 0 + ' option:selected').text() == 'MOK';
$("#csmp"+0).prop("readonly", value);
$("#single").prop("checked", true);
	if($('#single').is(':checked'))
		{
		$('#connNumber').show();
		$('#multiSearch').hide();
		$('.lessthancurrdate').datepicker({
			dateFormat: 'dd/mm/yy',	
			changeMonth: true,
			changeYear: true,
			minDate: $("#installDate0").val(),
			maxDate: '-0d',
			yearRange: "-100:-0"
		});
		
		}
	else
		{
		$('#connNumber').hide();
		$('#ccnNumber').val('');
		$('#multiSearch').show();
		$('.lessthancurrdate').datepicker({
			dateFormat: 'dd/mm/yy',	
			changeMonth: true,
			changeYear: true,
			maxDate: '-0d',
			yearRange: "-100:-0"
		});
		}
	
	prepareChallanDateTag();
	
	$("#single").click(function() {
		if($('#single').is(':checked'))
		{
		$('#connNumber').show();
		$('#multiSearch').hide();
		}
	else
		{
		$('#connNumber').hide();
		$('#ccnNumber').val('');
		$('#multiSearch').show();
		}
		$('#selectHideShow').hide();
		$('#changeCycle,#changeCycleMulti').attr('checked', false);

	});
	
	$("#multiple").click(function() {
		if($('#multiple').is(':checked'))
		{
		$('#connNumber').hide();
		$('#ccnNumber').val('');
		$('#multiSearch').show();
		}
	else
		{
		$('#ccnNumber').val('');
		$('#connNumber').show();
		$('#multiSearch').hide();
		}
		$('#selectHideShow').hide();
		$('#changeCycle,#changeCycleMulti').attr('checked', false);
	});
	
	$('#changeCycle,#changeCycleMulti').click(function() {
		 if(this.checked) {
			 openBillCyclePage();
		}
	});

	$("#wardZone1,#wardZone2,#wardZone3,#wardZone4,#wardZone5").change(function (obj) {
		$('#changeCycle,#changeCycleMulti').attr('checked', false);
	});

	previousMrdDate=$("#mrdDate").val();
	$("#mrdDate").val($.datepicker.formatDate('dd/mm/yy', new Date()));
	$("#mrdDate").focus();	
	$('#mrdDate').on('change', function(){
		validDate();
	});
});
function validDate(){
	  var errorList = [];
	  var input = $("#mrdDate").val();
	  var reg = /(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d/;
	  if(input == ''){
		  errorList.push(getLocalMessage('water.meterReading.readDate'));
		  displayErrorsOnPage(errorList);
	  }
	  else if (!input.match(reg)) {
		errorList.push("Please enter valid date");
		displayErrorsOnPage(errorList);
	  }
	  else{
		 $('.error-div').empty(); 
	  }
	}

$('#mtrStatus0').load(function() {
	
	 if($('#mtrStatus0').val() != 'MOK') {
		alert("Please add cut-off details");
		$('#btn-submit').prop("disabled", true);
		$('.reading').prop("disabled", true);
	}
});

function disableCsmpField(index){

	var value = $('#mtrStatus' + index + ' option:selected').text() == 'MOK';
	$("#csmp"+index).prop("readonly", value);
	 
};

function serachWaterData(obj)
{	
	var flag = $("#deleteFlag").val();

	if(flag == "D"){
		var url		=	'MeterReading.html?getdeletedrecords';
	}else{
		var url		=	'MeterReading.html?searchWaterRecords';
	}
	var formName	=	findClosestElementId(obj,'form');
		
	var theForm	=	'#'+formName;
		
			
	$(theForm).attr('action',url);
		
	$(theForm).submit();
	
}

function checkConcumption(index,csCCN){ 
	var errorList = [];
	if($("#lastMtrRead"+index).val()!='' && $("#mrdMtrread"+index).val()!=''){
		var consumption=$("#mrdMtrread"+index).val()-$("#lastMtrRead"+index).val();
		if(consumption<0){
			 if($('#mtrStatus' + 0 + ' option:selected').text() != 'MOK') {
			var messageText="Eneterd reading is less than Previous reading.<br>Do you want to continue as meter reset?";
				var message='';
				message	+=	'<h5 class=\"text-center text-blue-2 padding-5\">'+messageText+'</h5>';
				message	+=	'<div class=\"text-center\">';					
				message	+=	'<input class=\"btn btn-blue-2\" type=\'button\' value=\'Yes\'';
				message	+=	' onclick="return closeBillCycleForm()"/> '; 
				message	+=	'<input class=\"btn btn-danger\" type=\'button\' value=\'No\'';
				message	+=	' onclick="return NoValue('+index+')"/>';
				message	+=	'</div>';
				$(errMsgDiv).html(message);	
				$(errMsgDiv).show();
				showModalBoxWithoutClose(errMsgDiv);	
			return false;
		}else{
			errorList.push(getLocalMessage('water.meterReading.currentLessThanPrevious'));
			  displayErrorsOnPage(errorList);
		}
		}
	}
}

function NoValue(index){
	$("#mrdMtrread"+index).val('');
	closeBillCycleForm();
}



function viewDetails(id)
{
	var requestData = {"id":id
	};
	var returnData =__doAjaxRequest('MeterReading.html?viewDetails','POST',requestData, false,'html');
	openPopupWithResponse(returnData);
}

function saveData(element)
{
	var errorList = [];
	var newMrdDate = $("#mrdDate").val();
	var previousMrdDat = moment(previousMrdDate, "DD.MM.YYYY HH.mm").toDate();
    var newMrdDat = moment(newMrdDate, "DD.MM.YYYY HH.mm").toDate();
    const currntDate = new Date();
    var curtDate = moment(currntDate, "DD.MM.YYYY HH.mm").toDate();
	if(newMrdDat.getTime() > curtDate.getTime()){
		errorList.push('Current Meter Reading date should not be greater than todays date');}
	
	if($("#csmp0").val()==0){
		errorList.push(getLocalMessage('water.meterReading.consumption.zero.validation'));
	}
	displayErrorsOnPage(errorList);
   	if(newMrdDat.getTime() < previousMrdDat.getTime()){
		errorList.push('Current Reading date should be greater than previous reading date');}
	
	displayErrorsOnPage(errorList);
	if(errorList ==0){
	if($('.error-msg')[0]!=null && $('.error-msg')[0].innerText.includes('Meter has been hampered with, please enter cut-off details')){ 
		alert("Please add cut-off details");
		$('#btn-submit').prop("disabled", true);
		$('.reading').prop("disabled", true);
		return false;
	} 
	var result= saveOrUpdateForm(element,"Meter reading done successfully!", 'MeterReading.html', 'saveform');
	prepareChallanDateTag();
	return result; 
	}
}

function calDaysAndConsumption(eve)
{
	var idArray=[];
	 $('.checkall').each(function() { 
		 if(this.checked) {
         var id = $(this).attr('id');
         idArray.push(id);
		 }
     });
    $("#selectedvalues").val(idArray);
    
    if(idArray.length>0){
		 var idsData=idArray.join(", ");
		var theForm	=	'meterReadingForm';
		var formData ={
				"idarray":idsData,
		}
		var returnData =__doAjaxRequest('MeterReading.html?assignBillCycle','post',formData,false);
		closeBillCycleForm();
		return true;
		}
    else
    	{
    	var errMsg = 'Please select atleast one bill cycle for meter reading !';
		$('.error-div').html(errMsg);
		return false;
    	}
}


 function clearCalculation()
{
		$('#changeCycle,#changeCycleMulti').attr('checked', false);
	 	return true;
} 

function openBillCyclePage()
{
	 var requestData = {
			 "W1":$('#wardZone1').val()+"",
			 "W2":$('#wardZone2').val()+"",
			 "W3":$('#wardZone3').val()+"",
			 "W4":$('#wardZone4').val()+"",
			 "W5":$('#wardZone5').val()+""
	 }
	var returnData =__doAjaxRequest('MeterReading.html?openBillCycle','POST',requestData, false,'html');
	showModalBoxWithoutClose(returnData);
}

function closeBillCycleForm()
{
	var childDivName	=	'.child-popup-dialog';
	$(childDivName).hide();
	$(childDivName).empty();
	disposeModalBox();
	$.fancybox.close();
}

function prepareChallanDateTag() {
	var dateFields = $('.lessthancurrdate');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}

function resetMeterData(element){
	$("#meterReadingForm").submit();
}

function getConsumptionAndNoOfDays(index, csCcn) {	
	var errorList = [];
	reqData = {"csCcn" : csCcn}
	var returnData =__doAjaxRequest('MeterReading.html?isCcnCutOff','POST',reqData, false, 'json');
	if(returnData){
		var mtrStatus = $('#mtrStatus' + index + ' option:selected').text();
		var gapCode = $('#gapCode' + index + ' option:selected').text();
		if(mtrStatus !='NAM' || gapCode !='AVG'){
			errorList.push('Something is wrong with the meter. Allowed only to enter NAM and AVG values');
			displayErrorsOnPage(errorList);
			$('#btn-submit').prop("disabled", true);
			return false;
		}
		else{
			 $('.error-div').empty(); 
			 $('#btn-submit').prop("disabled", false);
			 checkConcumption(index, csCcn);
		  }
	}
	checkConcumption(index, csCcn);
	var instllDate=$("#installDate0").val();
	var newMrdDate = $("#mrdDate").val();
	var instllDat=moment(instllDate, "DD.MM.YYYY HH.mm").toDate();
	var previousMrdDat = moment(previousMrdDate, "DD.MM.YYYY HH.mm").toDate();
    var newMrdDat = moment(newMrdDate, "DD.MM.YYYY HH.mm").toDate();
    if($('#gapCode' + 0 + ' option:selected').text() != 'ANS') {
	    if(newMrdDat.getTime() < instllDat.getTime()){
	    	errorList.push(getLocalMessage('water.meterReading.currentLessThanInst'));
			displayErrorsOnPage(errorList);
			return false;
	    }
    }
	if(newMrdDat.getTime() < previousMrdDat.getTime()){
		errorList.push('current reading date should be greater than previous reading date');
		displayErrorsOnPage(errorList);
		return false;
	}
	if($("#mrdMtrread"+index).val()!=''){
		var curMeterDate = null;
		if($("#mrdDate").val()!='') {
			curMeterDate = $("#mrdDate").val();
		}
			
		if(curMeterDate != null && curMeterDate != '') {
			var currentMeterReading = $("#mrdMtrread"+index).val();
			var meterStatus = $("#mtrStatus"+index).val();
			var gapCode = $("#gapCode"+index).val();
			var requestData = {
				"toDate" : curMeterDate,
				"currentMeterReading" : currentMeterReading,
				"meterStatus" : meterStatus,
				"csCcn" : csCcn,
				"gapCode" : gapCode
			};
			var returnData =__doAjaxRequest('MeterReading.html?getConsumptionAndNoOfDays','POST',requestData, false, 'json');
			if(returnData != 'N') {
				var splitArray = [];
				splitArray = returnData.split(",");
				$('#csmp'+index).val(
						splitArray[0]);
				$('#ndays'+index).val(
						splitArray[1]);
			}
		}
		else {
			errorList.push('Please enter Current Meter Reading date');
			displayErrorsOnPage(errorList);
		}
	}
}
</script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content">
	<!-- Start info box -->
	<div class="widget">
		<div class="widget-header">
			<h2><spring:message code="water.meter.reading" /></h2>
			<div class="additional-btn">
				<!-- <a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a> -->
				<apptags:helpDoc url="MeterReading.html"></apptags:helpDoc>
			</div>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="water.fieldwith" /><i
					class="text-red-1">*</i> <spring:message code="water.ismandtry" />
				</span>
			</div>
			<form:form action="MeterReading.html" method="post"
				class="form-horizontal" name="meterReadingSearch"
				id="meterReadingForm">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				<div class="form-group">
					<label class="control-label col-sm-2"><spring:message
							code="water.connectiontype" /><span class="mand">*</span></label>
					<div class="col-sm-4">
						<label class="radio-inline"><form:radiobutton
								path="entityDTO.meterType" value="S" id="single" /> <spring:message
								code="water.meterReading.Single" /></label> <label class="radio-inline"><form:radiobutton
								path="entityDTO.meterType" value="M" id="multiple" /> <spring:message
								code="water.meterReading.Multiple" /></label>
					</div>
					<div id='connNumber'>
						<label class="control-label col-sm-2 required-control"><spring:message
								code="water.ConnectionNo" /></label>
						<div class="col-sm-4">
							<form:input path="entityDTO.csCcn" type="text"
								class="form-control" id="ccnNumber"></form:input>
						</div>
					</div>
				</div>
				<div id='multiSearch'>

					<div class="panel-group accordion-toggle margin-bottom-0"
						id="accordion_single_collapse">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse"
										href="#Additional_Owners"><spring:message code="water.bill.Multiple" /></a>
								</h4>
							</div>
							<div id="Additional_Owners" class="panel-collapse collapse in">
								<div class="panel-body">

									<div class="form-group margin-bottom-0">

										<apptags:lookupFieldSet
											cssClass="form-control required-control" baseLookupCode="WWZ"
											hasId="true" pathPrefix="entityDTO.wardZone"
											isMandatory="true" hasLookupAlphaNumericSort="true"
											hasSubLookupAlphaNumericSort="true" showAll="true" />
									</div>

									<div class="form-group margin-bottom-0">

										<apptags:lookupFieldSet
											cssClass="form-control required-control" baseLookupCode="TRF"
											hasId="true" pathPrefix="entityDTO.tariff" isMandatory="true"
											hasLookupAlphaNumericSort="true"
											hasSubLookupAlphaNumericSort="true" showAll="true" />
									</div>


									<div class="form-group">
										<label class="col-sm-2 control-label "><spring:message
												code="water.ConnectionSize" /> </label>
										<div>
											<c:set var="baseLookupCode" value="CSZ" />
											<apptags:lookupField
												items="${command.getLevelData(baseLookupCode)}"
												path="entityDTO.conSize" cssClass="form-control"
												hasChildLookup="false" hasId="true" showAll="true"
												selectOptionLabelCode="eip.select" isMandatory="true" />
										</div>

									</div>
									<%-- <c:if test="${command.dependsOnType eq '1' && command.entityDTO.meterType eq 'M'}"> --%>
									<div class="form-group">
										<label class="col-sm-2 control-label"> <spring:message
												code="water.meterReading.MaterReadingCycle" /></label>
										<div class="col-sm-4">
											<label class="checkbox-inline"> <form:checkbox
													path="changeCycle" id="changeCycleMulti" value="Y" /></label>
										</div>
									</div>
									<%-- </c:if> --%>

								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<form:hidden path="deleteHistoryFlag" id = "deleteFlag" value = "${command.deleteHistoryFlag}"></form:hidden>
				</div>

				<div class="text-center padding-bottom-10">
					<button type="submit" class="btn btn-success" id="searchBtn"
						onclick="return serachWaterData(this);">
						<i class="fa fa-search"></i> <spring:message code="water.search" />
					</button>
					<button type="button" class="btn btn-warning"
						onclick="return resetMeterData(this);"><spring:message code="water.btn.reset" /></button>
						<button type="button" class="btn btn-danger"
							onclick="window.location.href ='AdminHome.html'">
							<spring:message code="water.btn.back" />
						</button>
				</div>
				
				<c:if test="${not empty command.entityList}">
					<div id="selectHideShow">
					
						<div class="form-group padding-top-10">
	
							<!-- <c:if test="${command.entityDTO.meterType eq 'S'}">
								<label class="col-sm-3 control-label required-control"><spring:message
										code="meter.cycle" /></label>
								<div class="col-sm-3 checkbox">
									<form:checkbox path="changeCycle" id="changeCycle" value="Y" />
								</div>
							</c:if> -->

	
							<label class="col-sm-2 control-label required-control"><spring:message
									code="water.CurrentMeterReadingdate" /></label>
							<div class="col-sm-4">
									<form:input path="mrdDate" type="text"
										class="form-control" maxlength="10" 
										onkeydown="this.value=this.value.replace(/^(\d\d)(\d)$/g,'$1/$2').replace(/^(\d\d\/\d\d)(\d+)$/g,'$1/$2').replace(/[^\d\/]/g,'')"
										placeholder="DD/MM/YYYY"></form:input>
							</div>
						</div>
					
						<div class="table-responsive max-height-300 padding-top-10">
							<table class="table table-bordered table-condensed table-striped">
								<tr>
									<th width="20"><spring:message code="water.nodues.srno" /></th>
									<th width="70"><spring:message code="water.ConnectionNo" /></th>
									<th width="180"><spring:message code="water.consumerName" /></th>
									<th width="100"><spring:message
											code="water.meterDet.metNo" /></th>
									<th width="100"><spring:message
											code="water.meterReading.PreviousReading" /></th>
									<th width="100"><spring:message code="water.met.status" /></th>
									<th width="100"><spring:message
											code="water.meterReading.GapCode" /></th>
									<th width="100"><spring:message
											code="water.meterReading.CurrentReading" /></th>
									<th width="80"><spring:message
											code="water.meterReading.AttachImage" /></th>
									<th width="40"><spring:message
											code="water.meterReading.View" /></th>
									<th width="50"><spring:message
											code="water.meterReading.csmp" /></th>
									<th width="50"><spring:message
											code="water.meterReading.ndays"  /></th>
								</tr>
								<spring:message code="water.meterReading.View" var="viewTooltip" />
								<c:forEach items="${command.entityList}" var="data"
									varStatus="status">
									<tr>

										<fmt:formatDate pattern="dd/MM/yyyy"
											value="${data.meterInstallDate}" var="installDateDta" />
										<form:hidden path="" value="${installDateDta}"
											id="installDate${status.index}"></form:hidden>

										<td>${status.count}</td>
										<td><form:input tabindex="-1" path="entityList[${status.index}].csCcn"
												type="text" class="form-control disablefield"
												readonly="true"></form:input></td>

										<td><form:input tabindex="-1" path="entityList[${status.index}].name"
												type="text" class="form-control disablefield"
												readonly="true"></form:input></td>

										<td><form:input tabindex="-1"
												path="entityList[${status.index}].mtrNumber" type="text"
												class="form-control disablefield" readonly="true"></form:input></td>

										<td><form:input tabindex="-1" path="entityList[${status.index}].lastMtrRead" id="lastMtrRead${status.index}"
												class="form-control hasNumber" maxlength="8"
												value="${data.lastMtrRead}" readonly="true" /></td>

										<td><c:set var="baseLookupCode" value="MST" />
											 <form:select onBlur = "disableCsmpField(${status.index})"
												path="entityList[${status.index}].cpdMtrstatus" 
												class="form-control hasNumber" id="mtrStatus${status.index}">
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp" >
													<form:option value="${lookUp.lookUpId}">${lookUp.lookUpCode}</form:option>
												</c:forEach>
											</form:select></td>

										<td><c:set var="baseLookupCode1" value="GAP" /> <form:select
												path="entityList[${status.index}].cpdGap"
												class="form-control cpdCode" id="gapCode${status.index}">
												<c:forEach items="${command.getLevelData(baseLookupCode1)}"
													var="lookUp1">
													<form:option value="${lookUp1.lookUpId}">${lookUp1.lookUpCode}</form:option>
												</c:forEach>
											</form:select></td>
										<td>
										<form:input name=""
												path="entityList[${status.index}].mrdMtrread" type="text"
												id="mrdMtrread${status.index}"
												class="form-control hasNumber reading" maxlength="8" 
												readonly="$('#mtrStatus'+${status.index}+'option:selected').text() == 'MOK' ? false : true"
												value= "${isMOK}" 
												onblur="getConsumptionAndNoOfDays(${status.index},'${data.csCcn}')"></form:input></td>

										<td><apptags:formField fieldType="7" labelCode=""
												hasId="true"
												fieldPath="entityList[${status.index}].mrdImagePath"
												isMandatory="false" showFileNameHTMLId="true"
												fileSize="BND_COMMOM_MAX_SIZE"
												maxFileCount="CHECK_LIST_MAX_COUNT"
												validnFunction="IMAGE_UPLOAD_VALIDATION_EXTENSION"
												currentCount="${status.index}" /></td>
										<td><a data-toggle="tooltip" data-placement="top"
											title="" class="btn btn-blue-2 btn-sm"
											data-original-title="${viewTooltip}"
											onclick="return viewDetails(${data.mmMtnid})"><i
												class="fa fa-file-text-o"
												onclick="return viewDetails(${data.mmMtnid})"></i></a></td>
												
										<td><form:input tabindex="-1"
												path="entityList[${status.index}].csmp" type="text" id = "csmp${status.index}"
												class="form-control hasTwoDecimal" readOnly = "$('#mtrStatus'+${status.index}+'option:selected').text() == 'MOK' ? false : true" ></form:input></td>
												
										<td><form:input tabindex="-1"
												path="entityList[${status.index}].ndays" type="text" id = "ndays${status.index}"
												class="form-control disablefield" readonly="true" ></form:input></td>
										<form:hidden path="" id = "mrdMrdate${status.index}" value = "${data.mrdMrdate}"></form:hidden>
									</tr>
								</c:forEach>


							</table>
						</div>
					

						<div class="text-center padding-top-10">
							<button type="button" class="btn btn-success btn-submit" id="btn-submit"
								onclick="return saveData(this);"><spring:message code="water.btn.submit" /></button>
							<button type="button" class="btn btn-danger"
								onclick="window.location.href='AdminHome.html'"><spring:message code="water.btn.back" /></button>
						</div>

					</div>

				</c:if>
			</form:form>
		</div>
	</div>
	<!-- End of info box -->
</div>
