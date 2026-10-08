<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>

<script type="text/javascript">
	jQuery(document).bind("keyup keydown", function(e){
	    if(e.ctrlKey && e.keyCode == 80){
	        alert('PLEASE USE THE PRINT BUTTON TO PRINT THIS DOCUMENT');
	        return false;
	    }
	});
	
	
	function printCertificate(el) {
		var restorepage = document.body.innerHTML;
		var printcontent = document.getElementById(el).innerHTML;
		document.body.innerHTML = printcontent;
		window.print();
		document.body.innerHTML = restorepage;
	    }
	
</script>
<style>
.width-50{
	width:"50% !important";
}

#footer {
   bottom:0;
   width:100%;
   height:30px;   /* Height of the footer */
   background:#4F2BC1;
   color:white;
   padding-top: 0.3rem;
}
a:link {
  color: white;
  background-color: transparent;
  text-decoration: none;
}

</style>

<div class="content animated slideInDown">
	<!-- Start info box -->
	<div class="widget">
		<div class="widget-header">
			<h2><spring:message code="nwc.certi.header" text="Provisional Certificate" /></h2>
			<div class="additional-btn"><a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i><span class="hide">Help</span></a> 
			</div>
		</div>
		<div class="widget-content padding">
			<!-- Print section starts here -->
			<div id="printDiv">
			<div class="">
				<form:form action="" name="" id="provisional" class="form-horizontal">
					<div>
						<div class="text-center">
							<h2 class="margin-bottom-20 text-bold">
								<img src="assets\img\nwc_header.png" style="width:100%;">
							 </h2>	
							 <h3>
							 	${userSession.organisation.ONlsOrgname}
							</h3>
						</div>
					</div>
					<div class="clear"></div>
					<h5 class="text-bold text-center margin-bottom-30">
						<spring:message code="nwc.certi.title" text="NEW TAP CONNECTION RECIEPT" /><br>
						<spring:message code="nwc.certi.type" text="(PROVISIONAL CERTIFICATE)" />
					</h5>
					
					<div class="text-bold margin-bottom-30 ">
						 <div class="col-xs-4 col-sm-6">
							<div class="">
								<spring:message code="nwc.certi.from" text="From," />
							</div>
							<div class="">
								<spring:message code="nwc.certi.from.content1" text="Municipal Engineer" />
							</div>	
							<div class="">	
								${userSession.ULBName.lookUpDesc}
							</div>
						  </div>
					 </div>
					 <div class="text-bold margin-bottom-30 ">
						<div  class="col-xs-8 col-sm-6">
								<div><span class="col-xs-8 col-sm-8 text-right margin-bottom-10">
									<spring:message code="nwc.certi.application.no" text="Application No.:" />${command.provisionCertificateDTO.applicationNo}
									</span>
								<span class="col-xs-8 col-sm-6"></span>
								</div>
								<div>
									<span class="col-xs-8 col-sm-8 text-right margin-bottom-10">
										<spring:message code="nwc.certi.date" text="Date:" />${command.provisionCertificateDTO.applicationDate}
									</span>
									<span class="col-xs-6 col-sm-6"></span>
								</div>
					     </div>
					   </div>
	 					<div class="clear"></div>
	 				
		 				<div class="text-bold margin-top-30 ">
		 				<div class="col-xs-12 col-sm-12">
		 					<spring:message code="nwc.certi.to" text="To," />
		 					
		 				</div>
		 				<div class=" text-bold margin-bottom-10  margin-top-10 ">
		 				<div class="col-xs-12 col-sm-12">
		 					 <p> 
		 						<span class="">${command.provisionCertificateDTO.fullName}</span>
		 					 </p>
							 <p>
							    <spring:message code="nwc.certi.address1" text="Address: " /> 
							    <span class=""> ${command.provisionCertificateDTO.address1} </span>
							 </p>
							 <p>
								<span class=""> ${command.provisionCertificateDTO.address2} </span>
		 					 </p>
		 				 </div>
		 			</div>
		 			</div>
		 			<div class="clear"></div>
	 				<div class="text-bold">
		 				<div class="col-xs-12 col-sm-12">
		 				<p class="margin-left-50 ">
		 					<span><spring:message code="nwc.certi.subject" text="Subject:" /> New Tap Connection</span>
		 					<span></span>
		 				</p>
		 				<p class="margin-top-10 margin-left-50">
		 					<span><spring:message code="nwc.certi.reference" text="Reference:" /></span>	
		 					<span>
		 						<spring:message code="nwc.certi.ref.content1" text="Your Application for New Water Connection" />
		 					</span>	
		 					<span><spring:message code="nwc.certi.ref.content2" text=" Application number:" /></span>
		 					<span> ${command.provisionCertificateDTO.applicationNo} </span>
		 					<span><spring:message code="nwc.certi.ref.content3" text="and Application Date:" /></span>
		 					<span> ${command.provisionCertificateDTO.applicationDate} </span>
		 					<span><spring:message code="nwc.certi.ref.content4" text="is approved ." /></span>
		 					
		 				</p>
		 				</div>
	 				 </div>
	 		      <div class="margin-top-30 ">
	 					<div class="col-xs-12 col-sm-12">
		 					<div class="text-left text-bold"> 
		 						<spring:message code="nwc.certi.sir" text="Sir/Madam," />
		 					</div>
		 					<div class="clear"></div>
		 					<p class="margin-top-10">
		 						<p class="text-indent-50">
		 							<spring:message code="nwc.certi.body.content1" text="In the context of the application submitted by you, it is informed that the authority for the new tap connection will be done according to the rules and regulations and not being subject to the conditions, approval has been given by the Municipal Corporation, as presented in the application. " />
		 							<spring:message code="nwc.certi.body.content2" text="Approval of new tap connection will be valid only on the basis of availability of water supply pipeline in the area." />
		 				
		 						</p>		 						
		 					</p>
	 					<p class="margin-top-10 text-center text-bold">
							<span><spring:message code="nwc.certi.body.content3" text="Applicant Information" /></span>
						</p>
						<div class="col-xs-6 col-sm-6 col-xs-offset-3 col-sm-offset-3">
							<table class="table table table-bordered ">
								<tr>
									<td width="30%"><spring:message code="nwc.certi.tablea.content1" text="Name"/></td>
									<td class="">${command.provisionCertificateDTO.fullName}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content2" text="House No."/></td>
									<td class="">${command.provisionCertificateDTO.consumerHouseNumber}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content3" text="Address"/></td>
									<td class="">${command.provisionCertificateDTO.address1}  ${command.provisionCertificateDTO.address2}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content4" text="Landmark"/></td>
									<td class="">${command.provisionCertificateDTO.consumerLandmark}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content5" text="Ward No."/></td>
									<td class="">${command.provisionCertificateDTO.ward}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content6" text="Mobile No."/></td>
									<td class="">${command.provisionCertificateDTO.consumerMobile}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tablea.content7" text="Email ID"/></td>
									<td class="">${command.provisionCertificateDTO.consumerEmail}</td>
								</tr>
							</table>
						</div>
						<p class="margin-top-10 text-center text-bold clear">
							<span><spring:message code="nwc.certi.body.content4" text="Application lnformation" /></span>
						</p>
						<div class="col-xs-6 col-sm-6 col-xs-offset-3 col-sm-offset-3">
							<table class="table table table-bordered table-condensed">
								<tr>
									<td width="30%"><spring:message code="nwc.certi.tableb.content1" text="Type of Tap Connection"/></td>
									<td class="">${command.provisionCertificateDTO.tapTypeConnection}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tableb.content2" text="Type of Tap Usage"/></td>
									<td class="">${command.provisionCertificateDTO.tapUsage}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tableb.content3" text="Application Fees"/></td>
									<td class="">${command.provisionCertificateDTO.applicationFee}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tableb.content4" text="Mode of Payment"/></td>
									<td class="">${command.provisionCertificateDTO.paymentMode}</td>
								</tr>
								<tr>
									<td class=""><spring:message code="nwc.certi.tableb.content5" text="Date of Payment"/></td>
									<td class="">${command.provisionCertificateDTO.paymentDate}</td>
								</tr>
								
							</table>
						</div>
						<div class="col-xs-12 col-sm-12">
							<p class="margin-top-10 clear">
								<spring:message code="nwc.certi.body.content5" text="Approval is granted based on the subjects listed above, as well as the conditions approved by the Authority in accordance with the rules and the papers submitted by the applicant along with the application." />
							</p>
						</div>
	 					<!-- <p style="page-break-after: always;"></p> -->
	 					<div class="col-xs-12 col-sm-12">
		 					<p class="margin-top-30 text-bold">
		 						<spring:message code="nwc.certi.note.content" text="The details that can be found are as follows." />
		 					</p>
		 					<p>
	
							   <p><spring:message code="nwc.certi.note.content1" text=" 1. This new tap connection certificate has been issued according to the documents attached with the application and the conditions mentioned in the memorandum." /></p>
							
							   <p><spring:message code="nwc.certi.note.content2" text=" 2. The acceptance of this new top connection is approved subject to the following terms and conditions: -" /></p>
							
							   <p class="text-indent-50"><spring:message code="nwc.certi.note.content3" text="◦ According to the availability of water pressure in the pipeline, in the prescribed area, in the prescribed words under the municipal area." /></p>
							
							   <p class="text-indent-50"><spring:message code="nwc.certi.note.content4" text="◦ The pipe will be arranged by the applicant for connecting to the main pipeline / pipeline connection from the house." /></p>
							
							   <p class="text-indent-50"><spring:message code="nwc.certi.note.content5" text="◦ The applicant will be responsible for the installation of the pipe from the main pipeline to the dwelling at thier own cost." /></p>
							 
							   <p><spring:message code="nwc.certi.note.content6" text=" 3. In the case of the New Tap Connection, the connection will be granted only when all requirements have been followed and all Municipal Corporation dues have been paid." /></p>
							
							   <p><spring:message code="nwc.certi.note.content7" text=" 4. In respect of this new tap connection, this is mandatory to deposit of tap connection fee, rood cutting fee (if applicable), water meter fee (if applicable), fitting fee and other effective charges within 30 days from the dote of issue of intimation through online portal. 
							   In case of non-deposit/non-compliance of fee within the said period, the permission will automatically stand cancelled." /></p>
		
		 					</p>
	 					 </div>
	 			  </div>
	 			</div>
	 			<div class="col-xs-12 col-sm-12">
	 			<div id="footer" class="margin-top-30 margin-bottom-30 text-center"><a href="https://urbanecg.gov.in"><spring:message code="nwc.certi.footer.content" text="https://urbanecg.gov.in"/></a>
	 			</div>
				</div>
				<div class="clear"></div>
				</form:form>
			</div>
			<!-- Print section ends here -->	 <!-- printCertificate('printDiv') -->
		</div>	
		  <div class="text-center clear padding-top-20">
						<button onclick="printCertificate('printDiv')"
							class="btn btn-primary hidden-print">
							<i class="icon-print-2"></i>
								<spring:message code="" text="Print"></spring:message>
						</button>
						
						<button type="button" class="btn btn-danger hidden-print"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="" text="Back"></spring:message>
							
						</button>
		  </div>	
		</div>
	</div>
</div>