<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script>
var loiSaveObj;
$( document ).ready(function(){
	var counterScheduleFlag = $("#counterScheduleFlag").val();
	var errorList = [];
	$('#errorslist').html('');
	if (counterScheduleFlag == 'Y') {
		errorList.push(getLocalMessage("Counter.ScheduledValidation"));
	   $('#error-div').css('display', 'block');
		for (var i = 0; i < errorList.length; i++) {
			$('#errorslist').append('<li>' + errorList[i] + '</li>');
		}
		return false;
	}
	$('.lessthancurrdate').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		maxDate: '-0d',
		yearRange: "-100:-0"
	});
	
	prepareChallanDateTag();
	$('#onlinebutton').prop('disabled', 'disabled');
	$('#onlinebutton').hide(); 
	var loiRecordFound=$('#loiRecordFound').val();
	if(loiRecordFound=="Y"){
		$('#applicationId').prop('disabled', true);
		$('#loiNo').prop('disabled', true);
		$('#searchDto\\.loiDate').prop('disabled', true);
	}else{
		$('#applicationId').prop('disabled', false);
		$('#loiNo').prop('disabled', false);
		$('#searchDto\\.loiDate').prop('disabled', false);
	}
});

function getLoiSearchData(obj)
{
	/* var url	= "LoiPayment.html";
	
	url+='?searchLOIRecords';

	var formName	=	findClosestElementId(obj, 'form');
	
	var theForm	=	'#'+formName;
	
	var requestData = __serializeForm(theForm);
	
	var response=__doAjaxRequest(url,'post',requestData,false); */
	/* $(".content").html(response); */

		var counterScheduleFlag = $("#counterScheduleFlag").val();
		var errorList = [];
		$('#errorslist').html('');
		if (counterScheduleFlag == 'Y') {
			errorList.push(getLocalMessage("Counter.ScheduledValidation"));
			$('#error-div').css('display', 'block');
			for (var i = 0; i < errorList.length; i++) {
				$('#errorslist').append('<li>' + errorList[i] + '</li>');
			}
			return false;
		}
		if (errorList.length == 0){
		showloader(true);
		setTimeout(function() {
			var formName = findClosestElementId(obj, 'form');

			var theForm = '#' + formName;

			var url = 'LoiPayment.html?searchLOIRecords';

			$(theForm).attr('action', url);

			$(theForm).submit();
			showloader(false);
		}, 2000);
		}
		

	}

	function saveData(element) {
		var errorList = [];
		$('#errorslist').html('');
		var payModeIn = $('#payModeIn').val();
		if (payModeIn == 0) {
			errorList.push(getLocalMessage("pg.selPayMode"));
		} else if (payModeIn == 119 || payModeIn == 132) {

		} else {
			var bankID = $('#bankID').val();
			var acNo = $('#acNo').val();
			var bmChqDDDate = $('.chqDate ').val();
			var chqNo = $('#chqNo').val();
		 	if (bankID == 0 || bankID == null) {
				errorList.push(getLocalMessage("payment.bankName.error"));
			}
			if (acNo == "" || acNo == null) {
				errorList.push(getLocalMessage("payment.bank.accountNo.error"));
			}
			if (bmChqDDDate == "" || bmChqDDDate == null) {
				errorList
						.push(getLocalMessage("payment.bank.chqOrDDDate.error"));
			}
			if (chqNo == "" || chqNo == null) {
				errorList.push(getLocalMessage("payment.bank.chqOrDDNo.error"));
			} 
		}
		if (errorList.length == 0) {
			$('#error-div').css('display', 'none');
			var result;

			//D#127375 specific file present
			loiSaveObj = element;
			var yes = getLocalMessage('eip.commons.yes');
			//#129512
			var no = getLocalMessage('eip.commons.no');
			var warnMsg = getLocalMessage('loi.payment.popup');

			message = '<p class="text-blue-2 text-center padding-15">'
					+ warnMsg + '</p>';
			message += '<div class=\'text-center padding-bottom-10\'>'
					+ '<input class="btn btn-success" type=\'button\' value=\''
					+ yes
					+ '\'  id=\'yes\' '
					+ ' onclick="onPaymentYes()"/> &nbsp;'
					+ '<input class="btn btn-success " type=\'button\' value=\''
					+ no + '\'  id=\'no\' '
					+ ' onclick="closeConfirmBoxForm()"/>' + '</div>';

			$(childDivName).addClass('ok-msg').removeClass('warn-msg');
			$(childDivName).html(message);
			$(childDivName).show();
			$('#yes').focus();
			showModalBox(childDivName);
			return false;
		} else {
			$('#error-div').css('display', 'block');
			for (var i = 0; i < errorList.length; i++) {
				$('#errorslist').append('<li>' + errorList[i] + '</li>');
			}
			$(window).scrollTop(0);
		}
	}

	//D#127375
	function onPaymentYes() {
		$.fancybox.close();
		if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
				":checked").val() == 'Y') {
			result = saveOrUpdateForm(loiSaveObj,
					getLocalMessage("master.loi.val.saveSucessfully"),
					'LoiPayment.html?redirectToPay', 'saveform');
		} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']")
				.filter(":checked").val() == 'N'
				|| $("input:radio[name='offlineDTO.onlineOfflineCheck']")
						.filter(":checked").val() == 'P') {
			result = saveOrUpdateForm(loiSaveObj,
					getLocalMessage("master.loi.val.saveSucessfully"),
					'LoiPayment.html?PrintReport', 'saveform');
		} else {
			result = saveOrUpdateForm(loiSaveObj,
					getLocalMessage("master.loi.val.saveSucessfully"),
					'LoiPayment.html', 'saveform');
		}
		prepareChallanDateTag();
		return result;
	}

	function fetchPropertyBill(propNo, flatNo) {
		window.open('PropertyBillPayment.html?fetchBillPaymentDetail&propNo='
				+ propNo + '&flatNo=' + flatNo);
	}

	function clearData(obj) {
		/* 	$('#applicationId').val('');
		 $('#loiNo').val('');
		 $('#searchDto.loiDate').val(''); */
		window.location.href = 'LoiPayment.html';
	}

	function prepareChallanDateTag() {
		var dateFields = $('.lessthancurrdate');
		dateFields.each(function() {
			var fieldValue = $(this).val();
			if (fieldValue.length > 10) {
				$(this).val(fieldValue.substr(0, 10));
			}
		});
	}
</script>
<apptags:breadcrumb></apptags:breadcrumb> 
<%-- <ol class="breadcrumb">
	<li><a href="AdminHome.html"><spring:message
				text="Registration of births and deaths" code="birt.death.node" /></a></li>
	<li><a href="javascript:void(0);"><spring:message text="Birth Registration Correction"
				code="BirthRegDto.BrRegCorr" /></a></li>
	<li class="active"><spring:message
			text="LOI Payment"
			code="LOI.Payment" /></li>
</ol> --%>


    <!-- ============================================================== --> 
    <!-- Start Content here --> 
    <!-- ============================================================== -->
    <div class="content">
      <div class="widget">
        <div class="widget-header">
          <h2><spring:message code="master.loi" text="LOI"/> <spring:message code="master.loi.payment" text="Payment"/></h2>
           <apptags:helpDoc url="LoiPayment.html"></apptags:helpDoc>
        </div>
        <div class="widget-content padding"> 
         <div class="mand-label clearfix"><span><spring:message code="contract.breadcrumb.fieldwith"
				text="Field with" /> <i class="text-red-1">*</i> <spring:message
				code="common.master.mandatory" text="is mandatory" /> </span></div>
          <form:form action="LoiPayment.html" method="POST" class="form-horizontal" id="loipaymentSearch">
		  <form:hidden path="" id="counterScheduleFlag" value="${command.counterScheduleFlag}" />
		  <form:hidden path="" id="loiRecordFound" value="${command.loiMaster.loiRecordFound}" />
		  <jsp:include page="/jsp/tiles/validationerror.jsp"/>
		  <div class="alert alert-danger alert-dismissible" role="alert" id="error-div" style="display: none;">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <ul id="errorslist"> 
	   				
      			</ul>
           </div>
		  <c:if test="${command.pageUrl eq 'L' }">
            <div class="form-group">
            <%--   <label class="col-sm-2 control-label">Service Name</label>
              <div class="col-sm-4">
              <form:select path="dto.serviceId" cssClass="form-control">
	              	<form:option value="0" >--select service--</form:option>
					<c:forEach var="service" items="${command.serviceList}">
					<form:option value="${service.key}" >${service.value}</form:option>
					</c:forEach>
			 </form:select>
              </div> --%>
              <label class="col-sm-2 control-label"><spring:message code="master.loi.app.no" text="Application Number"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.applicationId" type="text" class="form-control hasNumber" id="applicationId" maxlength="16"/>
              </div><span class="text-info col-sm-1 padding-top-5 text-small"><spring:message code="master.loi.or" text="OR"/></span>
            </div>
            <div class="form-group">
           <label class="col-sm-2 control-label required-control"><spring:message code="master.loi.loinumber" text="LOI Number"/></label>
              <div class="col-sm-4"> <form:input path="searchDto.loiNo" type="text" class="form-control" id="loiNo" maxlength="16"/></div>
              <label class="col-sm-2 control-label required-control"><spring:message code="master.loi.loidate" text="LOI Date"/></label>
              <div class="col-sm-4">
      		<apptags:dateField fieldclass="lessthancurrdate" datePath="searchDto.loiDate" cssClass="form-control"/>
             </div>
            </div>

		<%-- 	<div class="form-group">
			 <label class="col-sm-2 control-label">Applicant Name</label>
              <div class="col-sm-4">
                <form:input path="dto.applicantName" type="text" class="form-control"/>
              </div>
            </div>
 --%>
 <div class="text-center padding-bottom-10">
   <button type="button" class="btn btn-success" onclick="return getLoiSearchData(this);"><strong class="fa fa-search"></strong> <spring:message code="master.search" text="Search" /></button>
   <button type="button" class="btn btn-warning" onclick="clearData(this)"><spring:message code="reset.msg" text="Reset"/></button>
 </div>
 </c:if>
 <c:if test="${command.loiMaster.loiRecordFound eq 'Y' }">
 
 
 		
			<div class="panel-group accordion-toggle" id="accordion_single_collapse">
			
			<div class="panel panel-default">
		  	<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" class="collapsed" data-parent="#accordion_single_collapse" href="#Additional_Owners"><spring:message code="master.loi.app.details" text="Application Details"/></a></h4></div>
		 	<div id="Additional_Owners" class="panel-collapse collapse in">
			<div class="panel-body">
			
		      <!--  Defect #113330 -->
            <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.service.name" text="Service Name"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.serviceName" type="text" class="form-control" readonly="true"/>
              </div>
              <label class="col-sm-2 control-label"><spring:message code="master.loi.app.num" text="Application Number"/></label>
              <div class="col-sm-4">
              <!-- US#96773 KDMC -->
              <c:choose>
				<c:when test="${not empty command.searchDto.referenceNo && command.departmentCode != 'WT'}">
					<form:input path="searchDto.referenceNo" type="text" class="form-control"  readonly="true" />
				</c:when>
				<c:when test="${not empty command.searchDto.applicationId && command.departmentCode == 'WT'}">
					<form:input path="searchDto.applicationId" type="text" class="form-control"  readonly="true" />
				</c:when>
				<c:otherwise>
					 <form:input path="loiMaster.loiApplicationId" type="text" class="form-control" readonly="true"/>
				</c:otherwise>
			</c:choose>
               
              </div>
            </div>
            
              
              <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.name" text="Applicant Name"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.applicantName" type="text" class="form-control" readonly="true"/>
              </div>
              <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.mob" text="Applicant Mobile No"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.mobileNo" type="text" class="form-control" readonly="true"/>
              </div>       
            	</div>
            
 			 <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.email" text="Applicant Email Id"/></label>
              <div class="col-sm-4">
               <form:input path="searchDto.email" type="text" class="form-control" readonly="true"/>
              </div>
            </div>
            </div>
            </div>
            </div>
            
            
        <%--      	<div class="panel panel-default">
		  	<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#collection"><spring:message code="master.loi.ind.det" text="Letter of Indent (LOI) Details"/></a></h4></div>
		 	<div id="collection" class="panel-collapse collapse in">
			<div class="panel-body">
            

            <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.loiNo" text="LOI Number"/></label>
              <div class="col-sm-4"> <form:input path="loiMaster.loiNo" type="text" class="form-control" readonly="true"/></div> 
              <label class="col-sm-2 control-label"><spring:message code="master.loi.loidate" text="LOI Date"/></label>
              <div class="col-sm-4"><fmt:formatDate pattern="dd/MM/yyyy" value="${command.loiMaster.loiDate}" var="date" />
              <form:input path="" type="text" class="form-control" readonly="true" value="${date}"/>
              </div>
            </div>
            </div>
            </div>
            </div>
               
               
            	<div class="panel panel-default">
		  	<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#applicantDet"><spring:message code="master.loi.applicant.det" text="Applicant Details"/></a></h4></div>
		 	<div id="applicantDet" class="panel-collapse collapse in">
			<div class="panel-body">  
               
              <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.mob" text="Applicant Mobile No"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.mobileNo" type="text" class="form-control" readonly="true"/>
              </div>
              <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.email" text="Applicant EmailId"/></label>
              <div class="col-sm-4">
               <form:input path="searchDto.email" type="text" class="form-control" readonly="true"/>
              </div>
            	</div>
            
 			 <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.applicant.name" text="Applicant Name"/></label>
              <div class="col-sm-4">
                <form:input path="searchDto.applicantName" type="text" class="form-control" readonly="true"/>
              </div>
            </div>
            
            </div>
            </div>
            </div> --%>
            
            
            <div class="panel panel-default">
		  	<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#loifeeDet"><spring:message code="master.loi.ind.det" text="Letter of Indent (LOI) Details"/></a></h4></div>
		 	<div id="loifeeDet" class="panel-collapse collapse in">
			<div class="panel-body">  
            
            <div class="form-group">
              <label class="col-sm-2 control-label"><spring:message code="master.loi.loiNo" text="LOI Number"/></label>
              <div class="col-sm-4"> <form:input path="loiMaster.loiNo" type="text" class="form-control" readonly="true"/></div> 
              <label class="col-sm-2 control-label"><spring:message code="master.loi.loidate" text="LOI Date"/></label>
              <div class="col-sm-4"><fmt:formatDate pattern="dd/MM/yyyy" value="${command.loiMaster.loiDate}" var="date" />
              <form:input path="" type="text" class="form-control" readonly="true" value="${date}"/>
              </div>
            </div>
			
			<div class="table-responsive">
            <table class="table table-bordered table-striped">
	<tr>
	<th scope="col" width="80"><spring:message code="master.loi.sr.no" text="Sr. No"/></th>
	<th scope="col"><spring:message code="master.loi.charge.name" text="Charge Name"/></th>
	<th scope="col"><spring:message code="master.loi.amt" text="Amount"/></th>
	</tr>
	<c:forEach var="charges" items="${command.searchDto.getChargeDesc()}" varStatus="status">
	<tr>
	<td>${status.count}</td>
	<c:set var="str1" value="${charges.key}"/>  
<c:set var="str2" value="${fn:split(str1, '-')}" /> 
	<td><form:input path="" type="text" class="form-control" value="${str2[0]}" readonly="true"/></td>
	<td>
		<fmt:formatNumber value = "${charges.value}" type = "number" var="amount" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false" />
		<form:input path="" type="text" class="form-control text-right" value="${amount}" readonly="true"/>
	</tr>
	</c:forEach>
	
	<c:if test="${command.wndServiceFlag eq 'Y'}">
		<tr>
			<td>2</td>
								<c:forEach var="waterDue" items="${command.waterDues}"
								varStatus="status">
									<td><form:input path="" type="text" class="form-control"  value="${waterDue.key}"
											readonly="true"/></td>
									<td><form:input path="" value="${waterDue.value}" type="text"
											name=""
											class="form-control text-right amount hasNumber" readonly="true"/></td>
								
								</c:forEach>
							</tr>
							<tr>
								<c:forEach var="propDue" items="${command.propDues}"
								varStatus="status">
									<td>3</td>
									<td><form:input path="" type="text" class="form-control"  value="${propDue.key}"
											readonly="true"/></td>
									<td><form:input path="" value="${propDue.value}" type="text"
											name=""
											class="form-control text-right amount hasNumber" readonly="true"/>
									
									</td>
								
								</c:forEach>
							</tr>	
							
							
							
						</c:if>
	
	<tr>
	<td colspan="2"><span class="pull-right"><b><spring:message code="master.loi.total.amt" text="Total LOI Amount"/></b></span></td>
	<td> 
		<fmt:formatNumber value="${command.searchDto.total}" type="number" var="totalAmount" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false" />
		<form:input path="searchDto.total" value="${totalAmount}" cssClass="form-control text-right" readonly="true"/>
	</td>
	</tr>
	</table>
	</div>
	</div>
	</div>
	</div>
						<c:if test="${command.showPropDues }">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a data-toggle="collapse" class=""
											data-parent="#accordion_single_collapse"
											href="#propOutstanding"><spring:message code=""
												text="Property Outstanding" /></a>
									</h4>
								</div>
								<div id="propOutstanding" class="panel-collapse collapse in">
									<div class="panel-body">

										<div class="table-responsive">
											<table class="table table-bordered table-striped">
												<tr>
													<th scope="col" width="80"><spring:message
															text="Sr. No" code="master.lbl.srNo" /></th>
													<th scope="col"><spring:message text="Charge Name"
															code="master.loi.charge.name" /></th>
													<th scope="col"><spring:message text="Amount"
															code="master.loi.amt" /></th>
													<th scope="col"><spring:message text="Action" code="" /></th>
												</tr>

												<tr>
													<c:forEach var="propDue" items="${command.propDues}"
														varStatus="status">
														<td>1</td>
														<td><form:checkbox value="Y" style="margin: 10px 10px;"
														 	path="searchDto.isTaxApplicable"/>
															<form:input path="" type="text" style="text-align: center;"
																class="form-control" value="${propDue.key}"
																readonly="true" /></td>
														<td><form:input path="" value="${propDue.value}"
																type="text" name="" 
																class="form-control text-right amount hasNumber"
																readonly="true" /></td>
														<td>
															<button type="button" class="btn btn-primary"
																onclick="fetchPropertyBill('${command.prPropertyNo}','${command.prFlatNo}');">
																<spring:message code="" text="Pay Bills" />
															</button>
														</td>

													</c:forEach>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
						</c:if>

						<div class="panel panel-default">
 <jsp:include page="/jsp/cfc/Challan/offlinePay.jsp"/>
 </div>

 <div class="text-center padding-bottom-20">
  <c:if test="${command.counterScheduleFlag ne 'Y' }">
   <button type="button" class="btn btn-success btn-submit" onclick="return saveData(this);"><spring:message code="submit.msg" text="Submit"/></button>
  </c:if>
    <c:if test="${command.pageUrl eq 'L' }">
   <button type="button" class="btn btn-danger" onclick="window.location.href='LoiPayment.html'"><spring:message code="back.msg" text="Back"/></button>
   </c:if>
    <c:if test="${command.pageUrl eq 'C' }">
     <button type="button" class="btn btn-danger" onclick="window.location.href='AdminHome.html'"><spring:message code="back.msg" text="Back"/></button>
    </c:if>
 </div>
 </div>
 </c:if>
 <c:if test="${command.loiMaster.loiRecordFound eq 'N' }">
  <span><b><spring:message code="master.loi.val.norecord" text="No Record Found For Selected Search Criteria"/></b></span>
 </c:if>
 
 </form:form>
 </div>
 </div>
 </div>
          