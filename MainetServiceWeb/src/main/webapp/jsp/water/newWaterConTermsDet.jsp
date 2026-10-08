<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- <script type="text/javascript"
	src="js/trade_license/tradeLicenseApplicationForm.js"></script> -->
<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>


<div id="validationDiv">
	
	<div class="content">
		<div class="widget">
			<div class="widget-header text-large">
				<h2>
					<b><spring:message code="trade.license.termscondition"></spring:message></b>

				</h2>
			</div>
			<p>
				<spring:message code="water.terms.content1" text="" />${command.applicantDetailDto.applicantFirstName}
				<spring:message code="water.terms.content2" text="" /></br>
				<spring:message code="water.terms.content3" text="" />
			</p>

			
		</div>
	</div>

</div>
