  <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/water/noDuesCertificate.js"></script>
<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>
<script>
/* function printContent(el){
	var restorepage = document.body.innerHTML;
	var printcontent = document.getElementById(el).innerHTML;
	document.body.innerHTML = printcontent;
	window.print();
	document.body.innerHTML = restorepage;
} */
function printdiv(printpage) {
	
	var headstr = "<html><head><title></title></head><body>";
	var footstr = "</body>";
	var newstr = document.all.item(printpage).innerHTML;
	var oldstr = document.body.innerHTML;
	var printStyles = `
        <style>
            @media print {
            	#now{
    				margin: 2rem;
    			    padding: 1rem;
    			    border: 3px solid #000;
    			}
            }
        </style>
    `;
	var headstr = "<html><head><title>Print</title>" + printStyles + "</head><body>";
	document.body.innerHTML = headstr + newstr + footstr;
	window.print();
	document.body.innerHTML = oldstr;
	return false;
}
</script>
<style>
			#now{
				margin: 2rem;
			    padding: 1rem;
			    border: 3px solid #000;
			}
			.margin-bottom-0{
				margin-bottom:0px;
			}
			.margin-top-5{
				margin-top:5px;
			}
			.margin-top-10{
				margin-top:10px;
			}
			.margin-top-15{
				margin-top:15px;
			}
			.margin-top-20{
				margin-top:20px;
			}
			.margin-top-25{
				margin-top:25px;
			}
			.text-captlize{
				text-transform: capitalize;
			}
			.margin-left-10{
				margin-left:10px;
			}
			.btn-primary {
			  background-color: #4A525F;
			  border-color: #4A525F;
			  color: #FFFFFF;
			}
			</style>

 <%--  <apptags:breadcrumb></apptags:breadcrumb> --%>

    <!-- ============================================================== --> 
    <!-- Start Content here --> 

    <!-- ============================================================== -->
    <div class="content"> 
      <!-- Start info box -->
      <div class="widget" id="receipt">
      
        <div class="widget-content padding">
        <div id="now">
          <div class="row">
            <div class="col-xs-3 text-left"><img src="${userSession.orgLogoPath}" width="80"></div>
            <div class="col-xs-6 text-center">
              <h4 class="margin-bottom-0"><strong>${command.nodueCertiDTO.orgName}</strong></h4>
               <p><spring:message code="water.dept" /></p>
              <p class="margin-top-5 text-captlize"><strong>${command.nodueCertiDTO.serviceName}</strong></p>
            </div>
            <div class="col-xs-3 text-right"></div>
          </div>
          <p class="text-right"><strong>Date : </strong>${command.nodueCertiDTO.noDuesCertiDate}</p>
          <p class="text-right"><strong>Certificate No. : </strong>${command.nodueCertiDTO.certificateNo}</p>
          <div class="form-group clearfix margin-top-15">
            <div class="col-xs-6">
              <p>To,</p>
            </div>
          </div>
          <div class="row clearfix margin-top-10 margin-left-10">
            <div class="col-xs-1 text-left">Name:</div>
            <div class="col-xs-11 text-left">${command.nodueCertiDTO.applicantName}</div>
          </div>
          <div class="row clearfix margin-top-10 margin-left-10">
            <div class="col-xs-1 text-left">Subject:</div>
            <div class="col-xs-11 text-left text-captlize">${command.nodueCertiDTO.serviceName}</div>
          </div>
          <div class="row clearfix margin-top-10 margin-left-10">
            <div class="col-xs-1 text-left">Reference:</div>
            <div class="col-xs-9 margin-left-10">1) Application No. ${command.nodueCertiDTO.applicationId} Dated  ${command.nodueCertiDTO.applicantDate}</div>
            <div class="col-xs-9 col-xs-push-1 margin-left-10">2) Water Connection Inspection</div>
            <div class="col-xs-9 col-xs-push-1 margin-left-10">
            	 <p><spring:message code="water.zone.div" />${command.nodueCertiDTO.zone}</p>
            	<p><spring:message code="water.ward.div" />${command.nodueCertiDTO.ward} </p>
                <p><spring:message code="water.block.div" />${command.nodueCertiDTO.block}</p>
               
            </div>
          </div>
          
          
          <p class="margin-top-20">Sir/Madam,</p>
          <p class="margin-top-5">This is to certify that the connection no. ${command.nodueCertiDTO.connectionNo} Address ${command.nodueCertiDTO.applicantAdd} India stands in the name of ${command.nodueCertiDTO.applicantName}.</p>
          <p>The water tax for the said premised has been Year date (${command.nodueCertiDTO.noDuesCertiDate}) as record maintained by Municipal Corporation.</p>
          <div class="row margin-top-25">
            <div class="col-xs-5 col-xs-push-7 text-center">
              <p><spring:message code="water.orderr.munc.eng" /></p>
              <p><strong>${command.nodueCertiDTO.orgName}</strong></p>
            </div>
          </div>
          </div>
         
        </div>
      </div>
      </div>
      <div class="text-center">
            <button onclick="printdiv('receipt')" class="btn btn-primary hidden-print"><i class="icon-print-2"></i> Print</button>
			<button onclick="window.location.href='AdminHome.html'" class="btn btn-danger hidden-print"> Close</button>
          </div>