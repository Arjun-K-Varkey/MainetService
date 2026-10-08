<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="js/property/viewHistory.js"></script>



 <div class="widget">
    <div class="widget-header">
		<h2><strong><spring:message code="property.AuthorizationHistoryView" text="Authorization History View"/></strong></h2>
	</div>	
		
        <div class="widget-content padding">
			<form:form action="PropertyShowHistory.html" class="form-horizontal form" name="PropertyShowHistory" id="PropertyShowHistory">
			
				<h3 class="text-center"><spring:message code="property.applicationInProgress" text="Application against this property is in progress"/></h3>
           
                  
	      		<div class="text-center padding-top-10 hidden-print">
					<apptags:backButton url="AdminHome.html"></apptags:backButton>									
				</div>
		
	       </form:form>
	  </div>
 </div> 


