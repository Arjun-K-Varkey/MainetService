<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
 <script type="text/javascript">
$(document).ready(function(e){
	$("#single").prop("checked", true);
$('.lessthancurrdate').datepicker({
	dateFormat: 'dd/mm/yy',	
	changeMonth: true,
	changeYear: true,
	maxDate: '-0d',
	yearRange: "-100:-0"
});
$('.toDatee').datepicker({
	dateFormat: 'dd/mm/yy',	
	changeMonth: true,
	changeYear: true,
	maxDate: '-0d',
	yearRange: "-100:-0",
	onClose: function() {
		getEmployeeList();
	}
});
prepareChallanDateTag();

if($("#dataSize").val()>0)
	{
	var radioValue = $("input[name='conType']:checked").val();
	if(radioValue==='S')
		{
		 $('#selectall').prop('checked',true);
	 $('.checkall').each(function() { 
         this.checked = true;
     });
	}
	}

var radioValue = $("input[name='conType']:checked").val();
if(radioValue==='S')
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
	
$(".conSelect").click(function() {
	var radioValueSel = $("input[name='conType']:checked").val();
	if(radioValueSel==='S')
	{
	$('#connNumber').show();
	$('#multiSearch').hide();
	}
else
	{
	$('#connNumber').hide();
	$('#ccnNumber').val('');
	$('#multiSearch').show();
	$('.connectionDetails').hide();
	}
});



	    $('#selectall').click(function(event) { 
	        if(this.checked) { 
	            $('.checkall').each(function() { 
	                this.checked = true;
	            });
	        }else{
	            $('.checkall').each(function() { 
	                this.checked = false;  
	              
	            });         
	        }
	    });
	    
	    $('.checkall').on('click',function(){
	        if($('.checkall:checked').length == $('.checkall').length){
	            $('#selectall').prop('checked',true);
	        }else{
	            $('#selectall').prop('checked',false);
	        }
	        
	    });
});

function serachWaterBillPrintData(obj)
{
	showloader(true);
	var formName	=	findClosestElementId(obj,'form');
	
	var theForm	=	'#'+formName;
	
	var url		=	'WaterBillPrinting.html?serachWaterBillPrintData';
		
	$(theForm).attr('action',url);
	
	$(theForm).submit();
}

function saveData(element)
{
	return saveOrUpdateForm(element,"Water Bill printed successfully!", 'WaterBillPrinting.html', 'saveform');
}
function resetbillPrint(element){
	$("#waterBillPrintingForm").submit();
}

function prepareChallanDateTag() {
	var dateFields = $('.lessthancurrdate, .toDatee');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}
function generateBill(element){	
	var errorList = [];
	var msgType = $("input[name='msgType']:checked").val();
	if(msgType == undefined || msgType == "" || msgType == null){
		errorList.push(getLocalMessage("Please select message type"));
		displayErrorsOnPage(errorList);
	}
	if(errorList.length == 0){
		showloader(true);
		setTimeout(function(){startGenBill(element)},2);
	}	
}

function startGenBill(element) {
var idArray=[];
$('.checkall').each(function() { 
	 if(this.checked) {
    var id = $(this).attr('id');
    idArray.push(id);
	 }
});

if(idArray.length>0){
	 var idsData=idArray.join(", ");
	var msgType = $("input[name='msgType']:checked").val();
	var formData ={"idarray":idsData,
					"msgType":msgType
	}
	
	var returnData =__doAjaxRequest('WaterBillPrinting.html?generateBillAndPrintForJasper','post',formData,false);
	window.open(returnData,'_blank' );
}
else{
	showloader(false);
	showErrormsgboxTitle(getLocalMessage("water.bill.print.select.record"))
	return false;
}
}
function getEmployeeList(){
	showloader(true);
	setTimeout(function () {
		var fromDt = $("#fromDate").val();
		var toDt = $("#toDate").val();
		var data = {
	 			"fromDt" : fromDt,
	 			"toDt" : toDt
	 			};
		var URL = 'WaterBillPrinting.html?getEmployeeList';
		var returnData = __doAjaxRequest(URL, 'POST', data, false, 'json');
		if(returnData != null && returnData != "" && returnData != "[]"){
			$('#empId').html('');
			 $('#empId').append($("<option></option>").attr("value",0).attr("code",0).text("select Employee"));
			 
			 $.each(returnData, function(index, value) {
					 $('#empId')
					    .append($("<option></option>")
					    .attr("value",value.empId).text(value.empname +" "+ value.emplname));
			 });	 
		 }
		 $("#empId").trigger("chosen:updated");
		$(".chosen-select-no-results").trigger("chosen:updated");
	showloader(false);
	},1000);
}
 </script>
  <!-- Start right content -->
   <apptags:breadcrumb></apptags:breadcrumb>
    <!-- ============================================================== --> 
    <!-- Start Content here --> 
    <!-- ============================================================== -->
    <div class="content"> 
      <!-- Start info box -->
      <div class="widget ">
        <div class="widget-header">
          <h2><spring:message code="water.bill.print" text="Bill Printing"/></h2>
          <div class="additional-btn"> <a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i></a> </div>
        </div>
        <div class="widget-content padding">
          <div class="mand-label clearfix"><span><spring:message code="water.fieldwith" /> <i
						class="text-red-1">*</i> <spring:message code="water.ismandtry" /></span></div>
          <form:form action="WaterBillPrinting.html" method="POST" class="form-horizontal" id="waterBillPrintingForm">
           	<jsp:include page="/jsp/tiles/validationerror.jsp" />
           	<div class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
            <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="water.connectiontype"/><span class="mand">*</span></label>
            <div class="col-sm-4">  <label class="radio-inline"><form:radiobutton path="conType"  value="S" id="single" class="conSelect"/><spring:message code="water.meterReading.Single"/> </label>
              <label class="radio-inline"><form:radiobutton path="conType"  value="M" id="Multi" class="conSelect"/> <spring:message code="water.meterReading.Multiple"/></label></div>
	           <div id='connNumber'><label class="control-label col-sm-2 required-control"><spring:message code="water.ConnectionNo"/></label>
	          <div class="col-sm-4"><form:input path="entity.csCcn" class="form-control" id="ccnNumber"></form:input></div>
	          </div>
            </div>
            
            <div id='multiSearch'>
            <div class="panel-group accordion-toggle margin-bottom-0" id="accordion_single_collapse">
            <div class="panel panel-default">
		  	<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#Additional_Owners"><spring:message code="water.meterReading.Multiple"/></a></h4></div>
		 	<div id="Additional_Owners" class="panel-collapse collapse in">
			<div class="panel-body">
            
             <div class="form-group">
              <div class="col-sm-12"><label class="checkbox-inline"><form:checkbox path="meterType"  value="MTR"/> <spring:message code="water.meter"/></label></div>
             </div>
             <div class="form-group">
					<label class="col-sm-2"><spring:message
							code="water.billGenPeriod" /></label>
			 </div>
            <div class="form-group">
              <label class="col-sm-2 required-control control-label"><spring:message code="water.billPrintingSearch.FromDate"/></label>
              <div class="col-sm-4">
	              <div class="input-group">
	                  <form:input path="entity.csFromdt" type="text" id="fromDate" class="lessthancurrdate form-control"></form:input>  
	                  <label class="input-group-addon" for="fromDate"><i class="fa fa-calendar"></i></label>
	              </div>
			 </div> 
              
              <label class="col-sm-2 required-control"><spring:message code="water.disconnect.to"/></label>
              <div class="col-sm-4">
	              <div class="input-group">
	                  <form:input path="entity.csTodt" type="text" id="toDate" class="toDatee form-control" ></form:input>  
	                  <label class="input-group-addon" for="toDate"><i class="fa fa-calendar"></i></label>
	              </div>
     		  </div> 
              
            </div>
           <div class="form-group margin-bottom-0">
			<apptags:lookupFieldSet cssClass="form-control required-control" baseLookupCode="WWZ" hasId="true"
				pathPrefix="entity.codDwzid" isMandatory="true"
				hasLookupAlphaNumericSort="true"
				hasSubLookupAlphaNumericSort="true"  showAll="true"/>
           </div>
           
           <div class ="form-group">
           	<label class="col-sm-2 required-control"><spring:message code="cfc.employeeWise.empname"/></label>
           
			<div class="col-sm-4">
				<form:select path="empId" class="form-control chosen-select-no-results" id="empId">
					<option value=""><spring:message code='' text="Select Employee"/></option>
					<%-- <form:option value="">
						<spring:message code="water.dataentry.select" />
					</form:option>
					<c:forEach items="${command.empList}" var="lookUp">
						<form:option value="${lookUp.empId}">${lookUp.empname} ${lookUp.empmname} ${lookUp.emplname}</form:option>
					</c:forEach> --%>
				</form:select>
			</div>
				
           </div>
           
           
           </div>
           </div>
           </div>
           </div>
           </div>
           
           <form:hidden path="" value="${fn:length(command.billMas)}" id="dataSize"/>
           
           
           
           <div class="text-center padding-bottom-10">
           <button type="submit" class="btn btn-success" onclick="return serachWaterBillPrintData(this);"><i class="fa fa-search"></i> <spring:message code="water.search"/></button>
          <button type="button" class="btn btn-warning" onclick="resetbillPrint(this)"><spring:message code="water.btn.reset"/></button>
       		<button type="button" class="btn btn-danger" onclick="window.location.href ='AdminHome.html'"><spring:message code="water.btn.back" /></button>
        </div>
              <c:if test="${not empty command.billMas}">
             
             <div class="connectionDetails">
             <h4><spring:message code="water.connectiondetails"/></h4>
             <div class="table-responsive max-height-300">
              <table class="table table-bordered table-condensed table-striped">
	              	<tr>
		               <th width="120"><label class="checkbox-inline"><input type="checkbox" id="selectall"/><spring:message code="water.selectall" text="Select All"/></label></th>
					   <th width="100"><spring:message code="water.nodues.srno"/></th>
					   <th><spring:message code="water.ConnectionNo"/></th>
					   <th><spring:message code="water.nodues.consumername"/></th>
					   <th><spring:message code="water.meterReadingViewDetails.BillNo."/></th>
					   <th><spring:message code="water.meterReadingViewDetails.BillAmount"/></th>
					   <th><spring:message code="water.meterReadingViewDetails.BillPeriod"/></th>
					   <th><spring:message code="water.waterBillGeneration.BillDate" text="Bill Generation Date"/></th>
					   <th><spring:message code="water.download"/></th>
	              	</tr>
	              <c:forEach items="${command.billMas}" var="data"  varStatus="status"> 
	              	<tr>
	              	 <%-- <td><label class="checkbox margin-left-20"><form:checkbox path="entityList[${status.index}].pcFlg" value="Y" cssClass="checkall" id="${data.bmIdno}"/></label></td> --%>
		              <td><label class="checkbox margin-left-40"><form:checkbox path="" value="Y" cssClass="checkall" id="${data.bmIdno}"/></label></td>
		              <td><c:out value="${status.index+1}"></c:out></td>
		              <td><c:out value="${data.waterMas.csCcn}"></c:out></td>
		              <td><c:out value="${data.waterMas.csName} ${data.waterMas.csMname} ${data.waterMas.csLname}"></c:out></td>
		              <td><c:out value="${data.bmNo}"></c:out></td>
		              <td><c:out value="${data.billAmount}"></c:out></td>
   		              <td><c:out value="${data.billPeriod}"></c:out></td>
   		              <td><c:out value="${data.billGenDate}"></c:out></td>
   		              <td><button type="button" class="btn btn-success" onclick="return generateBill(${data.bmIdno});">
   		              	<spring:message code="water.print.bill"/></button></td>
   		              
	              	</tr>
             	 </c:forEach>
         		</table>
         	</div>
	         	 <div class="text-center">
	            	<label class="radio-inline"><form:radiobutton path="msgType"  value="WS" id="msgType" /><spring:message code="" text="With SMS"/> </label>
	              	<label class="radio-inline"><form:radiobutton path="msgType"  value="NS" id="msgType" /> <spring:message code="" text="No SMS"/></label>	            
	              </div>
         		<div class="text-center margin-top-10">
	             <button type="button" class="btn btn-success" onclick="return generateBill(this);"><spring:message code="water.print.bill"/></button>
	            	 <button type="button" class="btn btn-danger" onclick="window.location.href='AdminHome.html'"><spring:message code="water.btn.back"/></button>
	           	
	            </div>
            </div>
            
            </c:if>
            
            
          </form:form>
        </div>
      </div>
      <!-- End of info box --> 
      </div>
      