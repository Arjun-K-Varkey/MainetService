 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="js/cfc/scrutiny.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#codDwzid1').attr('disabled',true);
	$('#codDwzid2').attr('disabled',true); 
	$('#ccnSizeLookUp').hide();
});

function saveDistributionDetails(element)
{
	  var appId='${command.lableValueDTO.applicationId}';
	  var serviceId= '${command.serviceId}';
	  var labelId='${command.lableValueDTO.lableId}';
	  saveOrUpdateForm(element,"Your application saved successfully!", '', 'save');
	  if($("#error").val()!='Y')
      {
	      loadScrutinyLabel('ScrutinyLabelView.html?setViewDataFromModule',appId,labelId,serviceId); 
	   } 


}
function calculateCCNSize(element)
	{
		var	formName =	findClosestElementId(element, 'form');
		var theForm	=	'#'+formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
	    var URL = 'DistributionConnectionForm.html?CalculateCCNSize';
        var returnData =__doAjaxRequest(URL,'POST',requestData, false);
        var divName = '#validationDiv';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		$(divName).show();
		
	}
	
function calculateCCNSizeAndDischargeRate(element)
{  
	 var message = '';
	if($('#noOfFamily').val() == null || $('#length').val() == null || $('#noOfFamily').val() == '' || $('#length').val() == '' || $('#csCcntype :selected').val() == 0 ) {
		message = 'Please Enter Number of Family And Length To Calculate Connection Size';
		$('#error-div').html(message);
		$('#error-div').show();
		return;
	}
	
	var	formName =	findClosestElementId(element, 'form');
	var theForm	=	'#'+formName;
	var requestData = {};
	requestData = __serializeForm(theForm);
    var URL = 'DistributionConnectionForm.html?calculateCCNSizeAndDischargeRate';
    var returnData =__doAjaxRequest(URL,'POST',requestData, false);
    if(returnData != null ) {
    	var retuenDataObj = returnData.split(',');
    	if(retuenDataObj.length == 3) {
    		$('#ccnSizeLookUp').hide();
    		if(retuenDataObj[0] != null && retuenDataObj[0] > 0) {
        		$('#dischargeRateId').val(retuenDataObj[0]);
        	}
        	if(retuenDataObj[1] != null && retuenDataObj[1] > 0 && retuenDataObj[2] != null && retuenDataObj[2]  ) {
        		$('#calculatedCcnSizeLabel').html(retuenDataObj[1]);
        		$('#calculatedCcnSize').val(retuenDataObj[2]);
        		$('#error-div').hide();
        		
        	}else {
        		message = 'Please select connection size';
        		$('#error-div').html(message);
        		$('#error-div').show();
        		$('#calculatedCcnSizeLabel').hide();
        		$('#ccnSizeLookUp').show();
        		$('#calculatedCcnSize').val('');
        	}
    	}else {
    		message = 'Please select connection size';
    		$('#error-div').html(message);
    		$('#error-div').show();
    		$('#calculatedCcnSizeLabel').hide();
    		$('#ccnSizeLookUp').show();
    		$('#calculatedCcnSize').val('');
    	}
    	 
    }

    
    /* var divName = '#validationDiv';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$(divName).show(); */
	
}

function resetDistributionDetails(element){
	$("#frmDistributionDetails").submit();
}

function chkPressure(element)
{	
		var chkprsValue=getLocalMessage("water.pressure.value");
		var prsError=getLocalMessage("water.pressure.validmsg");
		var prsValue=$(element).val();
		if(prsValue < chkprsValue){
			$('#prsError').html(prsError);
		}else{
			$('#prsError').html('');
		}
}
function checkDiameter(element)
{	
		debugger;
		var fromValue = parseInt(getLocalMessage("water.diameter.fromValue"));
		var toValue = parseInt(getLocalMessage("water.diameter.toValue"));
		var diameterValue=parseInt($(element).val());
		if( diameterValue < fromValue || diameterValue > toValue){
			var diameterError=getLocalMessage("water.diameter.validmsg");
			$('#diameterError').html(diameterError);
		}else{
			$('#diameterError').html('');
		}
}
</script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content">
	<!-- Start info box -->
	<div class="widget">
		<div class="widget-header">
			<h2><spring:message code="water.dist.det"/></h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a>
			</div>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="water.fieldwith"/> <i class="text-red-1">*</i><spring:message code="water.ismandtry"/> 
				</span>
			</div>
			<form:form action="DistributionConnectionForm.html"
				class="form-horizontal form" name="frmDistributionDetails"
				id="frmDistributionDetails">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div style="font-size: 12px; color: red; font-weight: bold;" id="error-div"></div>
				<h4 class="margin-top-0"><spring:message code="water.form.appdetails"/></h4>
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message code="water.road.service"/></label>
					<div class="col-sm-4">
						<form:input  type="text" class="form-control" path="serviceName" id="serviceName" disabled="true"></form:input>
					</div>
					<label class="col-sm-2 control-label"><spring:message code="water.road.applNo"/></label>
					<div class="col-sm-4">
						<form:input  type="text" class="form-control" disabled="true"
							path="csmrInfo.applicationNo"></form:input>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message code="water.road.applName"/></label>
					<div class="col-sm-4">
						<form:input name="" type="text" class="form-control" disabled="true"
							path="applicantFullName"></form:input>
					</div>
					<label class="col-sm-2 control-label"><spring:message code="water.road.applDate"/></label>
					<div class="col-sm-4">
						<form:input  type="text" class="form-control" disabled="true"
							path="csmrInfo.csApldate"></form:input>
					</div>
				</div>
				<div class="form-group"> 
             <apptags:lookupFieldSet baseLookupCode="WWZ" hasId="true" showOnlyLabel="false"
									pathPrefix="csmrInfo.codDwzid" isMandatory="false" hasLookupAlphaNumericSort="false" hasSubLookupAlphaNumericSort="true" cssClass="form-control" showAll="true"/>
  
             </div>
				<h4><spring:message code="water.dist.legalStatus"/></h4>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message code="water.status"/></label>
					<div class="col-sm-4">
						<c:set var="baseLookupCode" value="WLI" />
						<form:select path="csmrInfo.csListatus" class="form-control" id="">
							<form:option value="0"><spring:message code="water.dist.connStatus"/></form:option>
							<c:forEach items="${command.getLevelData(baseLookupCode)}"
								var="lookUp">
								<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<label class="col-sm-2 control-label required-control"><spring:message code="water.dist.connType"/></label>
							<div class="col-sm-4">
											<c:set var="baseLookupCode" value="WCT" /> <form:select
										path="csmrInfo.csCcntype" class="form-control"
										id="csCcntype">
											<form:option value="0"><spring:message code="water.dist.sel.connType"/></form:option>
											<c:forEach items="${command.getLevelData(baseLookupCode)}"
												var="lookUp">
												<form:option value="${lookUp.lookUpId}"
													code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
									</form:select>
											</div>
				</div>
				<h4><spring:message code="water.dist.linedet"/></h4>
				
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message code="water.noOfFamily"/></label>
					<div class="col-sm-4">
						
							<form:input  type="text" value="1" class="form-control hasNumber"
								path="csmrInfo.noOfFamilies" id="noOfFamily"></form:input>
							
						
					</div>
					<label class="col-sm-2 control-label " for="newnoOfTaps"><spring:message
						code="water.NoofTaps" /></label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control hasNumber"
							path="csmrInfo.csNooftaps" ></form:input>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label "><spring:message
						code="water.diameter" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							 <form:input type="text" class="form-control hasNumber" id="diameter"
								path="diameter" onblur="checkDiameter(this);"></form:input>
							 <span class="input-group-addon">MM</span>
						</div> 
						<label id="diameterError" class="error"></label>
						
					</div>  
				</div>
				
				
				<c:if test="${command.noOfUsageLabel ne null  && not empty command.noOfUsageLabel}">
				<div class="form-group">
					<label class="col-sm-2 control-label required-control">${command.noOfUsageLabel}</label>
					<div class="col-sm-4">
						
							<form:input name="" type="text" class="form-control hasNumber"
								path="csmrInfo.csNoofusers"></form:input>
						
					</div>
				</div>
				</c:if>
				
				<div class="form-group">
				<label class="col-sm-2 control-label required-control"><spring:message code="water.csSourceLine"/></label>
					<div class="col-sm-4">
						<c:set var="baseLookupCode" value="SLN" /> <form:select
								path="csmrInfo.csSourceLine" class="form-control"
								id="csSourceLine">
								<form:option value="0"><spring:message code="water.dist.sel.csSourceLine"/></form:option>
								<c:forEach items="${command.getLevelData(baseLookupCode)}"
									var="lookUp">
									<form:option value="${lookUp.lookUpCode}"
										code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
								</c:forEach>
							</form:select>
				<%-- <apptags:lookupFieldSet baseLookupCode="SLN" hasId="true" showOnlyLabel="false"
									pathPrefix="codId" isMandatory="false" hasLookupAlphaNumericSort="false" hasSubLookupAlphaNumericSort="false" cssClass="form-control" showAll="false"/> --%>
						</div>
					</div> 
					<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message code="water.dist.discharge"/></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input type="text" class="form-control hasNumber"
								path="csmrInfo.distribution.rcDistccndif" id="dischargeRateId" disabled="true"></form:input>
							<span class="input-group-addon" id="basic-addon1">M<sup>3</sup>/h
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message code="water.dist.pressure"/></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="csmrInfo.distribution.rcDistpres" cssClass="form-control mandColorClass text-right hasTwoDecimal"
							maxlength="10" id="pressure" onblur="chkPressure(this);"  value = "${csmrInfo.distribution.rcDistpres}" />						
							<span class="input-group-addon" id="basic-addon1">Kg/cm<sup>2</sup></span>
						</div>
						<label id="prsError" class="error"></label>
					</div>
					<label class="col-sm-2 control-label required-control"><spring:message code="water.dist.length"/></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input name="" type="text" class="form-control hasNumber"
								path="csmrInfo.distribution.rcLength" id="length"></form:input>
							<span class="input-group-addon" id="basic-addon1">Mtr.</span>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 col-xs-12 control-label"><spring:message code="water.dist.period"/></label>
						<div class="col-sm-4">
							<form:input  type="text" class="form-control hasNumber" path="csmrInfo.distribution.rcTotdisttime" value="4"></form:input>
		    			</div>
					
					  <label class="col-sm-2 control-label required-control"><spring:message code="water.ConnectionSize"/></label>
					
					 <div class="col-sm-4">
						 <div>
							<form:label id="calculatedCcnSizeLabel" type="text" class="form-control hasNumber" path=""  disabled="true"></form:label>
						</div>
						  
						  <div id="ccnSizeLookUp">
						  	<c:set var="baseLookupCode" value="CSZ"/>
	                             <apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="csmrInfo.recommendedCcnSize" cssClass="form-control"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="select" isMandatory="true" />
						  </div>
						  <div style = "padding-top: 10px;">
						  	<button type="button" class="btn btn-success " onclick="calculateCCNSizeAndDischargeRate(this);">Calculate CCN Size</button>
						  </div>
					  </div>
					
					  
			</div>
			<%-- 	<div class="form-group">
				 <label class="col-sm-2 control-label required-control">Institute Type </label>
							    <div class="col-sm-4">
											 <form:select
										path="csmrInfo.distribution.instId" class="form-control"
										id="">
											<form:option value="0">Select Institute Type</form:option>
											<c:forEach items="${command.instituteList}"
												var="lookUp">
												<form:option value="${lookUp.instId}"
													code="">${lookUp.instType}</form:option>
											</c:forEach>
									</form:select>
											</div>
											</div> --%>
			
				<c:set var="appId" value="${command.lableValueDTO.applicationId}"/>
				<c:set var="labelId" value="${command.lableValueDTO.lableId}"/>
				<c:set var="serviceId" value="${command.serviceId}"/>
			    <form:hidden path="hasError" id="error"/> 
			    <form:hidden path="csmrInfo.csCcnsize" id="calculatedCcnSize"/> 
				
				<div class="text-center padding-top-10">
				<button type="button" class="btn btn-success btn-submit" onclick="saveDistributionDetails(this)"><spring:message code="water.btn.submit"/></button>
				<button type="Reset" class="btn btn-warning" onclick="resetPage(this)">
					<spring:message code="water.btn.reset" />
				</button>
				<input type="button" class="btn btn-danger" onclick="loadScrutinyLabel('ScrutinyLabelView.html?setViewDataFromModule','${appId}','${labelId}','${serviceId}')"
						class="btn" value="<spring:message code="water.btn.back"/>">
				</div>
			</form:form>
		</div>
	</div>
	<!-- End of info box -->
</div>


 