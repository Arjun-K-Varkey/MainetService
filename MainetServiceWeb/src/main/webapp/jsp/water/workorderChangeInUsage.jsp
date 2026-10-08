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
#workprintusage .cert-outer-div {
	border: 3px solid #000000;
	margin: 2rem !important;
}
#signusage{
	padding-top:100px !important;
}
.designation{
margin-top: 50px !important;
}
</style>
    <div class="animated slideInDown"> 
      <!-- Start info box -->
      <div class="widget" id="letter">
        <div class="widget-content padding"  id="workprintusage">
          <div class="form-group clearfix">
          <div class="cert-outer-div">
            <div class="col-xs-3 text-left"><img width="80" src="${userSession.orgLogoPath}"></div>
            <div class="col-xs-6 text-center margin-top-10">
             <h4 class="margin-bottom-0"> ${userSession.ULBName.lookUpDesc}</h4>
            </div>
            <div class="col-xs-12 text-center">
            	${command.commonDto.deptName}
            </div>
             <div class="col-xs-12 text-center margin-bottom-15">
             <h5 class="margin-bottom-0">(ORDER)</h5>
           <!--  <div class="col-xs-3 text-right"><img src="assets/img/logo.png"></div> -->
          </div>
          <div class="form-group clearfix margin-top-15">
          </div> 
          <div class="form-group clearfix margin-top-20">
          <div class="col-xs-1 ">To</div>
            <div class="col-xs-6">
               <p><b>${command.commonDto.name}</b></p>
              </div>
              <div class="col-xs-5 text-right">
              <p class="">Execution Date: <b>${command.commonDto.executionDate}</b> </p>
              </div>  
           
           </div>  
           <div class="form-group clearfix margin-top-20">
            <div class="col-xs-11"> 
              <p> Address <i class="fa fa-map-marker fa-1x red"></i>: 
              <b>${command.commonDto.address}</b></p>
              <%-- <p><b>${command.commonDto.city}</b></p> --%>
           </div>
          </div>
          <div class="form-group clearfix margin-top-20">
            <div class="col-xs-1">Subject</div>
            <div class="col-xs-11">
               <p><b> ${command.commonDto.serviceName}</b></p>
              </div> 
           </div> 
           
           <div class="form-group clearfix margin-top-20">
            <div class="col-xs-1">Reference</div>
            <div class="col-xs-11">
               <p>1. Your Application No. <b>${command.commonDto.apmApplicationId} </b>Date: <b>${command.commonDto.applicationDate}</b></p>
               <p>2. Date of acceptance letter (LOI No. <b>${command.commonDto.loiNo}</b> Date <b>${command.commonDto.loiDate}</b>)</p>
              </div> 
           </div> 
           
          <div class="form-group clearfix margin-top-20">
	          <div class="col-xs-11"> 
	            <p>Sir/Madam,<br><br>
	            	${command.commonDto.customString}
  				</p> 
    		  </div>	
        <div class="form-group clearfix padding-vertical-15">
            
           <div class="col-xs-4">
              <p class="padding-top-30">Copy To:  </p>
              <div class="padding-top-10"><b>Water Supply Department</b></div>
            </div>
             <div class="text-right col-xs-8 designation" id="signusage"> 
             <c:if test="${command.commonDto.serviceCode eq 'WCO' }"><b>Ward Officer</b></c:if>
              <c:if test="${command.commonDto.serviceCode ne 'WCO' }"><b><p>Deputy Engineer</p></b></c:if>
              <p> Water Supply Department</p>
              <p>Kalyan Dombivli Municipal Corporation</p>
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
             <input type="button" onclick="window.location.href='AdminHome.html'" class="btn btn-danger hidden-print" value="Back">
           </div>
          
        
       
      </div> 
     </div>
     </div>

    </div>