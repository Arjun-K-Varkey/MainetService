<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script>
$(document).ready(function(){
	$('.lessthancurrdate').datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true,
		maxDate : '-0d',
		yearRange : "-100:-0"
	});
	
	prepareDateTag();
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
function searchProperty() {
	

		var requestData = $('#NochangeInAssessmentId').serialize();
		var ajaxResponse = __doAjaxRequest(
				'NoChangeInAssessment.html?SearchPropNo', 'POST',
				requestData, false, 'html');

		if(($('option:selected', $("#selectedAssType")).attr('code')) == 'NC'){
		$(".content").html(ajaxResponse);
		}
		$(".content").html(ajaxResponse);
		
		return false;
}
function backToMain(){
	var data={};
	var URL='PropertyAssessmentType.html?backToMainPage';
	var returnData=__doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
}
</script>
<div  id="dataDiv">
<c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
<apptags:breadcrumb></apptags:breadcrumb> 
</c:if>
 <div class="content" >
 <div class="widget">
     <c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
         <div class="widget-header">
          <h2><strong>No Change in Assessment</strong></h2>
         <!--  <div class="additional-btn">  -->
          <apptags:helpDoc url="NoChangeInAssessment.html"></apptags:helpDoc>
          
          <!--  </div> -->
        </div> 
       </c:if>
       <div class="widget-content padding">

			<form:form action="NoChangeInAssessment.html"
					method="post" class="form-horizontal form"
					name="NoChangeAssessmentSearchForm" id="NochangeInAssessmentId">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
					<div class="mand-label clearfix">
						<span><spring:message code="property.ChangeInAss.EnterPropertyNo"/><strong class="text-red-1"><spring:message code="property.ChangeInAss.OR"/></strong>
							<spring:message code="property.ChangeInAss.OLDPID"/>
						</span>
					</div> 
					
				<div class="form-group" >
				
				
				 	<label class="col-sm-2 control-label required-control" for="assNo"><spring:message code="property.ChangeInAss.EnterPropertyNo"/></label>
					<div class="col-sm-3">
						<form:input path="provisionalAssesmentMstDto.assNo"  class="form-control" id="assNo"/>
					</div>
					
					<div class="col-sm-2 text-center"><i class="text-red-1">OR</i></div>
					<label class="col-sm-2 control-label required-control" for="assOldpropno"><spring:message code="property.ChangeInAss.oldpid"/></label>
					
					<div class="col-sm-3">					
						<form:input path="provisionalAssesmentMstDto.assOldpropno"  class="form-control" id="assOldpropno"/>
					</div>	
 						
				</div> 
				
				<div class="form-group" >
						<apptags:date labelCode="Manual Receipt Date" datePath="assesmentManualDate" fieldclass="lessthancurrdate"></apptags:date>
				</div>
				
				<div class="text-center padding-bottom-10">
<%-- 					<input type="button" id="NoChangeSubmitBtn" onclick="serachProperty()" class="btn btn-success"><i class="fa fa-search"></i><spring:message code="property.changeInAss.Search"/></> --%>
				
					<button type="button" class="btn btn-blue-2" id="NoChangeSubmitBtn"
									onclick="searchProperty()">
									<i class="fa fa-search"></i><spring:message code="property.changeInAss.Search"/>
					</button>
					<c:if test="${not empty command.provisionalAssesmentMstDto.assesssmentCategory}">
								<button type="button" class="btn btn-danger" id="back"
									onclick="backToMain(this)">
									Back
								</button>
						</c:if>	
				</div>
				
				
			</form:form>
		
 <c:if test="${empty command.provisionalAssesmentMstDto.assesssmentCategory}">
    </div>
</c:if> 

</div>
</div>
</div>