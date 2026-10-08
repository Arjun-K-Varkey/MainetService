<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>

<%
	response.setContentType("text/html; charset=utf-8");
%>
<script type="text/javascript"> 
 HTMLElementObject.contentEditable=false

 function printContent(el) {
		var restorepage = document.body.innerHTML;
		var printcontent = document.getElementById(el).innerHTML;
		document.body.innerHTML = printcontent;
		window.print();
		document.body.innerHTML = restorepage;
	}
</script> 
<style>
.designation{
margin-top: 50px !important;
}
#signexecu{
padding-top: 180px !important;
}
#workprintrecon .cert-outer-div {
	border: 3px solid #000000;
	margin: 2rem !important;
}
</style>
    <div class="animated slideInDown"> 
      <!-- Start info box -->
      <div class="widget" id="letter">
        <div class="widget-content padding"  id="workprintrecon">
          <div class="form-group clearfix">
          <div class="cert-outer-div">
            <div class="col-xs-3 text-left"><img width="80" src="${userSession.orgLogoPath}"></div>
            <div class="col-xs-6 text-center margin-top-10">
             <h4 class="margin-bottom-0"> ${userSession.ULBName.lookUpDesc}</h4>
            </div>
            <div class="col-xs-12 text-center">
            	${command.deptName}
            </div>
             <div class="col-xs-12 text-center margin-bottom-15">
             <h5 class="margin-bottom-0">(ORDER)</h5>
            </div>
           <!--  <div class="col-xs-3 text-right"><img src="assets/img/logo.png"></div>
          </div> -->
          <div class="form-group clearfix margin-top-20">
            <div class="col-xs-1">To</div>
            <div class="col-xs-6">
               <p><b>${command.applicantFullName}</b></p>
              </div>
              <div class="col-xs-5 text-right">
               <p>Execution Date: <b>${command.reconnExecutionDate}</b> </p>
              </div>  
           </div>  
           <div class="form-group clearfix margin-top-20">
            <div class="col-xs-11"> 
              <p> Address <i class="fa fa-map-marker fa-1x red"></i>: <b>${command.addressLine1}</b></p>
              <p><b>${command.cityName}</b></p>
           </div>
          </div>
          <div class="form-group clearfix margin-top-20">
            <div class="col-xs-1">Subject</div>
            <div class="col-xs-11">
               <p><b> ${command.serviceName}</b></p>
              </div> 
           </div> 
           
           <div class="form-group clearfix margin-top-20">
            <div class="col-xs-1">Reference</div>
            <div class="col-xs-11">
               <p>1. Your Application No. <b>${command.applicationId} </b>Date: <b>${command.applicationDate}</b></p>
               <p>2. Date of acceptance letter (LOI No. <b>${command.loiNo}</b> Date <b>${command.loiDate}</b>)</p>
              </div> 
           </div> 
           
          <div class="form-group clearfix margin-top-20">
	          <div class="col-xs-11"> 
	            <p>Sir/Madam,<br><br>
	            	${command.customString}
  				</p> 
    		  </div>	
        <div class="form-group clearfix padding-vertical-15">
            
           <div class="col-xs-4">
              <p class="padding-top-30">Copy To:  </p>
              <div class="padding-top-10"><b>Water Supply Department</b></div>
            </div>
             <div class="text-right col-xs-8 designation"  id="signexecu"> 
              <b><p>Executive Engineer</p>
              <p> Water Supply Department</p>
              <p>${userSession.ULBName.lookUpDesc}</p>
              <p>Division: Kalyan/Dombivli </p></b>
            </div>
          </div>
          </div>
			<div class="col-xs-12 text-center margin-top-10">
             	<button onclick="printContent('letter')"
					class="btn btn-primary hidden-print">
					<i class="icon-print-2"></i>
					<spring:message code="water.btn.print" />
				</button>
             <input type="button" onclick="window.location.href='AdminHome.html'" class="btn btn-danger hidden-print" value="<spring:message code="water.btn.back" text="Back" />">
           </div>
          
        
       
      </div> 
     </div>
     </div>

    </div>