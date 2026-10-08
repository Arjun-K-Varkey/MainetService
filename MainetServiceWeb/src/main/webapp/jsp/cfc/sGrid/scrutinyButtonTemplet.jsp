<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<form:form action="ScrutinyLabelView.html?setViewData" method="post">
	
	<div class="btn_fld clear margin-top-5 text-center"> 
       		<input type="hidden" value="${userSession.getScrutinyCommonParamMap().get('APM_APPLICATION_ID')}" name="appId"/>
       		<input type="hidden" value="${userSession.getScrutinyCommonParamMap().get('SM_SERVICE_ID')}" name="serviceId"/>
       		<input type="hidden" value="${userSession.getScrutinyCommonParamMap().get('taskId')}" name="taskId"/>
       		<input type="button" class="btn btn-success" value="<spring:message code="cfc.scrutiny" />" id="scrutinyBtn" onclick="openScrutinyDetailsForm(this);"/>
      	 		<apptags:backButton url="AdminHome.html"></apptags:backButton>
	</div>
	</form:form>
