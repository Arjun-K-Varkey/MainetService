<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
 
<style>
.radio-button{
	margin-top: 0.4rem;
    margin-left: 10rem;
}
</style> 
<script type="text/javascript">
	fileUploadMultipleValidationList();
	
	function viewApplicantDetails(element) {
		var appNo = $('#apmApplicationId').val();
        var servShortCode = $('#servShortCode').val();
        var serviceId = $('#serviceId').val();
        var url;
        var data = {};
        if(servShortCode == 'BRC'){
        	url="BirthRegCorrectionApprovalLevel.html?viewApplicantDetails";
        	data.appNo = appNo;
        }else if(servShortCode =='DRC'){
        	url="DeathRegCorrectionApprovalLevel.html?viewApplicantDetails";
        	data.appNo = appNo;
        }else if(servShortCode =='MUT'){
        	url="MutationAuthorization.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } else if(servShortCode =='MRG'){
        	url="MarChecklistApproval.html?mrgRegView";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } else if(servShortCode =='CNA'){
        	url="EditNameAddressAuthorization.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } 
        else if(servShortCode =='NTL'){
        	url="TradeApplicationForm.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } 
        else if(servShortCode =='TLA'){
        	url="TransperLicense.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } 
        else if(servShortCode =='CBN'){
        	url="ChangeInBusinessNameForm.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } 
        else if(servShortCode =='CCS'){
        	url="ChangeInCategorySubcategoryForm.html?viewApplicantDetails";
        	data.appNo = appNo;
        	data.serviceId = serviceId;
        } 
		var returnData = __doAjaxRequest(url, 'post', data,false, 'html');
	    if (returnData) {
	    	var title = 'View Applicant details';
	    	prepareTags();
	    	var printWindow = window.open('', '_blank');

	    	printWindow.document.write('<html><head><title>' + title + '</title>');
	    	printWindow.document
	    			.write('<link href="assets/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />')
	    	printWindow.document
	    			.write('<link href="assets/libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" />')
	    	printWindow.document
	    			.write('<link href="assets/css/style.css" rel="stylesheet" type="text/css" />')
	    	printWindow.document
	    			.write('<link href="assets/css/style-responsive.css" rel="stylesheet" type="text/css" />')
	    	printWindow.document
	    			.write('<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>')
	        printWindow.document.write('</head><body style="background:#fff;">');
	    	printWindow.document.write(returnData);
	    	printWindow.document.write('</body></html>');
	    	printWindow.document.close();
 
	    }
	}
	function saveForm(element, successurl) {
		return saveOrUpdateForm(element,
					'Your information has been saved successfully!', successurl,
					'CitizenEditSave');
		}
		

	function submitForm(element) {
	var popupMsg = "";
		if($("input:radio[name='statusVariable']").filter(":checked").val() == 'R' || $("input:radio[name='statusVariable']").filter(":checked").val() == 'H')

		{
			return saveOrUpdateForm(element, popupMsg,'ChecklistVerification.html?PrintChecklistReport', 'saveform');
		}
		else
		{  
			return saveOrUpdateForm(element, popupMsg,'AdminHome.html', 'saveform');
	    } 
		
		
	}

	function validateRejMsg(obj,count) 
	{
		if(obj.checked) 
		{
		  $('#rej'+count).attr('disabled', true);	
		  $('#rej'+count).val(''); 
	    } 
		
		else 
		{ 
		  $('#rej'+count).attr('disabled', false); 
		  $('#rej'+count).val('');
		}		
		canApprove();
	 }
	
	/* Start of code by ABM2144 for Defect ID#34046 */
	function canApprove(){var c=true;
	var d=true;
	  $(".chkbx").each(function(){e=$(this);
	  if(c){if((e.attr("data-mandatory")=="Y")) {
    if(e.is(':checked')){
      $("#rejected").prop("checked",false).prop("disabled",true);
      $("#approved").prop("checked",true).prop("disabled",false);
	  $("#selectall").prop("checked",true);
      $("#hold").prop("checked",false).prop("disabled",true);}else{
      $("#approved").prop("checked",false).prop("disabled",true);
	  $("#selectall").prop("checked",false);
      $("#rejected").prop("disabled",false);
      $("#hold").prop("disabled",false);c=false;
    }}else{
   	var flag=true;
  	  $(".chkbx").each(function(){
      		e=$(this);
  			if(c){if((e.attr("data-mandatory")=="Y")){		
  					flag=false;
  				}else{
					}
  			}}); 
  	  if(flag==false){
  		  c=true;
  	  }else{	
			c=false;
			 var len=$(".chkbx").length;
			if(len == 1){
				$(".chkbx").each(function(){e=$(this);
				if(e.is(':checked')){
				      $("#rejected").prop("checked",false).prop("disabled",true);
				      $("#approved").prop("checked",true).prop("disabled",false);
					  $("#selectall").prop("checked",true);
				      $("#hold").prop("checked",false).prop("disabled",true);}else{
				      $("#approved").prop("checked",false).prop("disabled",true);
					  $("#selectall").prop("checked",false);
				      $("#rejected").prop("disabled",false);
				      $("#hold").prop("disabled",false);c=false;
				    }
				});
			}
  	  }			
    }}else{
  	  $(".chkbx").each(function(){
    		e=$(this);
			if(d){if((e.attr("data-mandatory")=="Y")){
					d=false;
				}
			}
    	  });
  	  if(d == true){
  		  $(".chkbx").each(function(){
  			  e=$(this);
  			  if(d){
  				  if(e.is(':checked')){
      				  $("#rejected").prop("checked",false).prop("disabled",true);
      	      	      $("#approved").prop("checked",true).prop("disabled",false);
					  $("#selectall").prop("checked",true);
      	      	      $("#hold").prop("checked",false).prop("disabled",true);
      	      	      d=false;
      			  }else{
      			        $("#approved").prop("checked",false).prop("disabled",true);
						$("#selectall").prop("checked",false);
      			        $("#rejected").prop("disabled",false);
      			        $("#hold").prop("disabled",false);
      			      }
  			  }
  			  
  		  });
				
			}
			
    }})};
	/* End   of code by ABM2144 for Defect ID#34046 */
	
</script>

<ol class="breadcrumb">
	<li><a href="AdminHome.html"><spring:message
				code="menu.home" /></a></li>
	<li><a href="javascript:void(0);"><spring:message text="CFC"
				code="cfc.module.breadcrumb" /></a></li>
	<li><a href="javascript:void(0);"><spring:message
				text="Transaction" code="audit.transactions" /></a></li>
	<li class="active"><spring:message
			text="Document Verification Search"
			code="cfc.service.checklist.search" /></li>
</ol>

<div class="content">
	
      <div class="widget">
        <div class="widget-header">
          <h2>
		<spring:message code="checklistVerification.breadcrumb" />
	</h2>
	<div class="additional-btn"> <a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i></a> </div>
        </div>
        <div class="widget-content padding">
          <div class="mand-label clearfix"><span>Field with <i class="text-red-1">*</i> is mandatory</span></div>
          
		<form:form method="post" action="ChecklistVerification.html" name="ChecklistVerification" id="frmMaster" class="form-horizontal">
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			
			<c:if test="${not empty command.attachmentList}">
			<h4 class="margin-top-0"><spring:message text="Applicant Details" code="cfc.applicant.detail"/></h4>
			
				<div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="checklistVerification.serviceName"  text="Service Name" /> </label>
                <div class="col-sm-4">
                	<span class="form-control height-auto">${command.applicationDetails.applicationService }</span>
                </div>
                <label class="col-sm-2 control-label"><spring:message text="Name Of Applicant" code="cfc.applicant.name" /> </label> 
				<div class="col-sm-4">
					<span class="form-control text-hidden">${command.applicationDetails.applicantsName }</span>
				</div>
				</div>
				<div class="form-group">
	                <label class="col-sm-2 control-label"><spring:message text="Application ID " code="cfc.application" /> </label> 
					<div class="col-sm-4">
	                	<c:choose>
						<c:when test="${not empty command.attachmentList[0].referenceId and command.departmentCode ne 'WT'}">
							<span class="form-control">${command.attachmentList[0].referenceId }</span>
						</c:when>
						<c:otherwise>
							<span class="form-control">${command.applicationDetails.apmApplicationId }</span>
						</c:otherwise>
					</c:choose>
					</div>
					<label class="col-sm-2 control-label"><spring:message text="Application Date" code="cfc.applIcation.date" /> </label>
					<div class="col-sm-4">
					<c:set value="${command.applicationDetails.apmApplicationDate }" var="appDate"/>
							<span class="form-control"><fmt:formatDate type="date" value="${appDate}" pattern="dd/MM/yyyy" /></span>
					</div>
				</div>

			</c:if>
			<c:set var="loopCount" value='0' />
			<c:if test="${not empty command.attachmentList}">
			<input type="hidden" value="${fn:length(command.attachmentList)}" id="docCount"/>
				<h4>
					<spring:message code="checklistVerification.tableCaption" />
				</h4>
				 <div class="table-responsive">
              <table class="table table-hover table-bordered table-striped">
					<tr>
						<th><spring:message code="checklistVerification.srNo" /></th>
						<th><spring:message code="checklistVerification.documentName" text= "Document Name" /></th>
						<%-- <th><spring:message code="checklistVerification.docDesc" /></th> --%>
						<th><spring:message code="checklistVerification.documentStatus" /></th>
						<th><spring:message code="checklistVerification.fileName" /></th>
						<%-- <c:if test="${command.newApplication}">
						<th><spring:message code="cfc.upload.doc" /></th>
						</c:if> --%>
						<th><spring:message code="checklistVerification.isAttached" text="Is Attached?"/></th>
						<th><spring:message code="checklist.remark" text="Rejection Remark"/></th>      
					</tr>
					
					<c:forEach var="singleDoc" items="${command.documentsList}"
						varStatus="count">
						
						<c:if test="${count.index eq 0}">
							<input type="hidden" value=" ${fn:length(command.documentsList)}"  id="attSize"> 
						</c:if>
						<tr class="checklistData">
							<td>${count.count}</td>
							<td>${singleDoc.lookUpDesc}</td>
							<%-- <td>${singleDoc.docDescription}</td> --%>
							<td><c:choose>
									<c:when test="${singleDoc.otherField eq 'Y'}">
										<spring:message code="checklistVerification.mandatory" />
										<c:set var="docStatus" value="Mandatory" />
									</c:when>
									<c:otherwise>
										<spring:message code="checklistVerification.optional" />
										<c:set var="docStatus" value="Optional" />
									</c:otherwise>
								</c:choose></td>
							<td class="row1">
								
									<c:set var="links"	value="${fn:split(singleDoc.defaultVal,',')}" />
									<c:forEach items="${singleDoc.defaultVal}" var="download" varStatus="status">
									<!--changes for Defect #127371  --><apptags:filedownload filename="${singleDoc.lookUpCode}" dmsDocId="${singleDoc.lookUpType}"  filePath="${download}" elem="#chkbx${count.index},#rej${count.index}" callback="true" actionUrl="ChecklistVerification.html?Download"></apptags:filedownload>
									</c:forEach>
							</td>
							<c:if test="${command.newApplication}">
								<%-- <td><apptags:formField fieldType="7" fieldPath="attechEntity.attPath"
									labelCode="" currentCount="${count.index}"
									showFileNameHTMLId="true" fileSize="COMMOM_MAX_SIZE"
									maxFileCount="CHECK_LIST_MAX_COUNT"
									validnFunction="CHECK_LIST_VALIDATION_EXTENSION"
									folderName="${count.index}"
									checkListSrNo="${singleDoc.lookUpExtraLongOne}"
									checkListSStatus="${singleDoc.lookUpExtraLongTwo}"
									checkListMStatus="${singleDoc.lookUpType}"
									checkListMandatoryDoc="${singleDoc.lookUpId}"
									checkListDesc="${singleDoc.defaultVal}"
									checkListId="${singleDoc.lookUpId}" /></td> --%>
							
							<%-- <td>
							<label class="checkbox-inline">
							<c:if test="${singleDoc.otherField eq 'Y'}">
							<form:checkbox onclick="validateRejMsg(this,${count.index})" disabled="true" id="chkbx${count.index}" class="chkbx"
									data-mandatory="${singleDoc.otherField}"
									path="listOfChkboxStatus[${count.index}]"
									value="${count.index}" /> <c:set var="loopCount"
									value="${count.index}" /></label>
									</c:if>
									</td> --%>
									<td>
									<c:choose>
									<c:when test="${singleDoc.otherField eq 'Y'}">
									<label class="checkbox-inline">
									<form:checkbox onclick="validateRejMsg(this,${count.index})" disabled="true" id="chkbx${count.index}" class="chkbx"
									data-mandatory="${singleDoc.otherField}"
									path="listOfChkboxStatus[${count.index}]"
									value="${count.index}" /> <c:set var="loopCount"
									value="${count.index}" /></label>
									</c:when>
									<c:otherwise>
									<label class="checkbox-inline">
									<form:checkbox onclick="validateRejMsg(this,${count.index})" id="chkbx${count.index}" class="chkbx"
									data-mandatory="${singleDoc.otherField}"
									path=""
									value="${count.index}" /> <c:set var="loopCount"
									value="${count.index}" /></label>
									</c:otherwise>
									</c:choose>
									</td>
					       <td><form:textarea id="rej${count.index}" maxlength="100" path="attachmentList[${count.index}].clmRemark" disabled="true" value="${singleDoc.extraStringField1}" cssClass="form-control" /> 
									<c:set var="loopCount" value="${count.index}" />
						   </td> 
						</c:if>
						
						<c:if test="${!command.newApplication}">
							<c:if test="${command.applicationDetails.apmChklstVrfyFlag eq 'Y'}">
								<td><label class="checkbox-inline"><form:checkbox path="" value="${count.index}" disabled="true" checked="checked"/></label></td>
								<td><form:textarea path="" value="" disabled="true" cssClass="form-control" /> </td> 
							</c:if>
							
							<c:if test="${command.applicationDetails.apmChklstVrfyFlag ne 'Y' }">
								<td><label class="checkbox-inline"><form:checkbox path="" value="${count.index}" disabled="true" /></label></td>
								<td><form:textarea path="" value="${singleDoc.extraStringField1}" disabled="true" cssClass="form-control"/> </td> 
							</c:if>
						</c:if>
					</tr>
					</c:forEach>
				</table> 
				</div>
				<c:if test="${command.newApplication}">
				<div class="form-group padding-top-10 col-sm-9">
					<label class="col-sm-2 control-label required-control">
						<spring:message code="cfc.service.status" text="Status"/>
					</label> 
						<div class="radio-button">
						<label class="radio-inline">
							<form:radiobutton  id="approved" path="statusVariable" value="A" disabled="true" /> 
							<spring:message code="eip.dept.auth.approve" /></label> 
						<label class="radio-inline">
							<form:radiobutton
							id="rejected"
							path="statusVariable" value="R" disabled="false" /> <spring:message
							code="eip.dept.auth.reject" /></label> 
						<label class="radio-inline">
							<form:radiobutton 
							id="hold" 
							path="statusVariable" value="H" disabled="false" /> <spring:message  
							code="eip.dept.auth.hold" /></label> 
						</div>	
				</div>
				<div class="form-group padding-top-10 col-sm-3" style="margin-left:-1rem">
							 <label class="radio-inline"><form:checkbox disabled="true"
														path="" value="Marked All"  id="selectall" /><spring:message code="checklist.marked.all" text="Marked All" /></label>
				</div>
				<div class="col-sm-12">
						<b><spring:message code="checklist.verify.note" text="Note :  Please download all mandatory document for verification"/></b>
				</div>
				</c:if>
				<input type="hidden" name="apmApplicationId" id="apmApplicationId" value="${command.applicationDetails.apmApplicationId}"/>
                <input type="hidden" name="servShortCode" id="servShortCode" value="${command.servShortCode}"/>
                <input type="hidden" name="serviceId" id="serviceId" value="${command.serviceId}"/>
				<div class="text-center padding-top-10">
						<c:if test="${command.servShortCode ne null && ( command.servShortCode eq 'BRC' || command.servShortCode eq 'DRC' || command.servShortCode eq 'MUT' || command.servShortCode eq 'MRG' || command.servShortCode eq 'CNA'|| command.servShortCode eq 'NTL'|| command.servShortCode eq 'TLA'|| command.servShortCode eq 'CCS'|| command.servShortCode eq 'CBN')}">
							<input type="button"
								value="<spring:message code="checklistVerification.viewDetails" text="View Details" />"
								onclick="viewApplicantDetails(this);" class="btn btn-blue-3" />
						</c:if>
						<c:if test="${command.newApplication}">
						<input type="button" value="<spring:message code="checklistVerification.submit" />" onclick="return submitForm(this);" class="btn btn-success btn-submit" />
					</c:if>
					<apptags:backButton url="AdminHome.html"/>
				</div>
			</c:if>
		</form:form>
	</div>
	</div>
</div>

<script type="text/javascript">
//D#139363
$("#selectall").click(function () {
  $('.chkbx').prop('checked', this.checked);
  
  var docCount = $('#docCount').val();
  if($("#selectall").is(':checked')){
	  for(var i=0;i<docCount;i++) {
	 	 $('#rej'+i).prop("disabled",true).val('');
	  }
	  $("#rejected").prop("checked",false).prop("disabled",true);
	  $("#approved").prop("checked",true).prop("disabled",false);
	  $("#hold").prop("checked",false).prop("disabled",true); 
  }else{
	  for(var i=0;i<docCount;i++) {
	 	 $('#rej'+i).prop("disabled",false).val('');
	  }
	  $("#approved").prop("checked",false).prop("disabled",true);
      $("#rejected").prop("disabled",false);
      $("#hold").prop("disabled",false);
  }
  
});

</script>