<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/loi/loidashboard.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<style>
#loiPrint h5.sub-heading {
	font-size: 1.1rem;
}
#loiPrint .cert-outer-div {
	border: 3px solid #000000;
	padding: 0.4rem;
}
#loiPrint .row {
	margin: 0rem -0.8rem;
}
.colon {
	position: relative;
}
.colon::after {
	content:'\003A';
	position: absolute;
	top: 0rem;
    right: 0rem;
}
#loiprintform .loiPaymentTable table{
	margin: 0 auto;
    width: 70%;
}
</style>
<script>

$( document ).ready(function(){
	
	$('.lessthancurrdate').datepicker({
		dateFormat: 'dd/mm/yy',	
		changeMonth: true,
		changeYear: true,
		maxDate: '-0d',
		yearRange: "-100:-0"
	});
});

</script>
<!--     <ol class="breadcrumb">
      <li><a href="admin-home.html"><i class="fa fa-home"></i></a></li>
      <li>Home</li>
      <li>LOI</li>
      <li class="active">LOI Print</li>
    </ol> -->
    <!-- ============================================================== --> 
    <!-- Start Content here --> 
    <!-- ============================================================== -->
<!--    <div id="loiPrint" class="padding-20"> -->
    <div class="content">
      <div class="widget">
        <!-- <div class="widget-header">
          <h2><strong>LOI Print</strong></h2>
        </div> -->
        <div class="widget invoice" id="loiPrint">
        <div class="widget-content padding">         
      <form:form action="#" method="POST" class="form-horizontal" id="loiprintform">	
          <c:if test="${command.loiMaster.loiRecordFound eq 'Y' }">
 	        <%-- <h4>Application Details</h4>		  
            <div class="form-group">
              <label class="col-sm-2 control-label">Service Name</label>
              <div class="col-sm-4">
                <form:input path="searchDto.serviceName" type="text" class="form-control" readonly="true"/>
              </div>
              <label class="col-sm-2 control-label">Application Number</label>
              <div class="col-sm-4">
                <form:input path="loiMaster.loiApplicationId" type="text" class="form-control" readonly="true"/>
              </div>
            </div>
			<h4>Letter of Indent (LOI) Details</h4>

            <div class="form-group">
              <label class="col-sm-2 control-label">LOI Number</label>
              <div class="col-sm-4"> <form:input path="loiMaster.loiNo" type="text" class="form-control" readonly="true"/></div> 
              <label class="col-sm-2 control-label">LOI Date</label>
              <div class="col-sm-4"><fmt:formatDate pattern="dd/MM/yyyy" value="${command.loiMaster.loiDate}" /></div>
            </div>
               
             <h4>Applicant Details</h4>  
              <div class="form-group">
              <label class="col-sm-2 control-label">Applicant Mobile No</label>
              <div class="col-sm-4">
                <form:input path="searchDto.mobileNo" type="text" class="form-control" readonly="true"/>
              </div>
              <label class="col-sm-2 control-label">Applicant EmailId</label>
              <div class="col-sm-4">
               <form:input path="searchDto.email" type="text" class="form-control" readonly="true"/>
              </div>
            	</div>
            
 			 <div class="form-group">
              <label class="col-sm-2 control-label">Applicant Name</label>
              <div class="col-sm-4">
                <form:input path="searchDto.applicantName" type="text" class="form-control" readonly="true"/>
              </div>
            </div> --%>
            
		<div class="cert-outer-div">
           	<div class="row">
				<div class="col-xs-2">
					<img src="${userSession.orgLogoPath}" width="80">
				</div>
				
				<div class="col-xs-8 text-center">
					<h3 class="margin-bottom-0">
						<span class="text-bold"><spring:message code=""
							text="${userSession.organisation.oNlsOrgnameMar}" /></span>
					</h3>
					<h5 class="text-bold sub-heading">
						<spring:message code="loi.name"
							text="Letter of Intimation" />
					</h5>
				</div>
			</div>

			<div class="row margin-top-10 clear">
				<div class="col-sm-1 col-sm-offset-9 col-xs-2 col-xs-offset-8 colon">
					<p>
						<spring:message code="loi.no"
							text="LOI No." />
					</p>
				</div>
				<div class="col-sm-2 col-xs-2">
					<p>
						<span class="text-bold">${command.loiMaster.loiNo}</span>
					</p>
				</div>
				<div class="col-sm-1 col-sm-offset-9 col-xs-2 col-xs-offset-8 colon">
					<p>
						<spring:message code="loi.date"
							text="Date" />
					</p>
				</div>
				<div class="col-sm-2 col-xs-2">
					<p>
						<span class="text-bold">${command.searchDto.loiDate}</span>
					</p>
				</div>
			</div>
			
			<div class="row margin-top-30 clear">
				<div class="col-sm-12">
					<p><spring:message code="loi.to"
							text="To," /></p>
					<p class="text-bold margin-left-30">
						<!-- ApplicantName -->
						 ${command.searchDto.applicantName}
					</p>

					<p class="text-bold margin-left-30">
						<!-- ApplicantAddress -->
						${command.searchDto.address}
					</p>
					
					<p class="padding-top-20">
						<span class="text-bold"><spring:message code="loi.subject"
							text="Subject:" /></span>
							<spring:message code ="loi.subject.detail1" />
							<span class="text-bold">${command.searchDto.applicationId}</span>
							<spring:message code ="loi.subject.detail2" />
					</p>
					
					<p class="margin-top-20 margin-bottom-20">
						<spring:message code="loi.sirmadam" text="Sir/Madam," />
					</p>
					<%-- <c:choose>
					<c:when test="${userSession.languageId eq 1}">
					<p>
						<span><spring:message code="loi.report1"></spring:message></span>
						<span class="text-bold">${command.searchDto.applicationId}</span>&nbsp;
						<spring:message code="loi.report2.for" text="for "></spring:message>&nbsp;
							<span class="text-bold">${command.searchDto.serviceName}</span>&nbsp;
						<spring:message code="loi.report2"></spring:message>
						<span class="text-bold">${command.searchDto.applicationDate}</span>
						<spring:message code="loi.report3"></spring:message>
						<span class="text-bold">${command.searchDto.total}</span>
						<spring:message code="loi.report4"></spring:message>
					<!-- 	<span class="text-bold">Days</span> -->
						<spring:message code="loi.report5"></spring:message>
						<span class="text-bold">${userSession.organisation.ONlsOrgname}</span>
						<spring:message code="loi.report6"></spring:message>
					
					</p>
					</c:when>
					<c:otherwise> --%>
					<p>
						<span class="text-bold">${command.searchDto.serviceNameMar}</span>&nbsp;
						<span><spring:message code="loi.report3"></spring:message></span>
						<span class="text-bold">${command.searchDto.applicationId}</span>&nbsp;
						<spring:message code="loi.report1"></spring:message>
						<span class="text-bold">${command.searchDto.loiDate}</span>
						<spring:message code="loi.report4"></spring:message>
						<spring:message code="loi.report5"></spring:message>
						<span class="text-bold">${command.searchDto.total}</span>
						<spring:message code="loi.report6"></spring:message>					
					</p>
					<%-- </c:otherwise>
					</c:choose> --%>
				</div>
			</div>

							<c:if test="${command.loiMaster.propDueAmnt ne null && command.loiMaster.propDueAmnt gt 0}">
								<table class="table table-bordered table-striped">
									<tr>
										<th scope="col" width="80"><spring:message text="Sr. No"
												code="loi.lbl.srNo" /></th>
										<th scope="col"><spring:message text="Charge Name"
												code="loi.charge.name" /></th>
										<th scope="col"><spring:message text="Amount"
												code="loi.amt" /></th>
									</tr>

									<tr>
										<td>1</td>
										<td>Property Due Amount</td>
										<td><form:input path=""
												value="${command.loiMaster.propDueAmnt}" type="text" name=""
												class="form-control text-right amount hasNumber"
												readonly="true" /></td>
									</tr>
								</table>
							</c:if>

							<!-- <h4>LOI Fees and Charges in Details</h4> -->
			<div class="table-responsive margin-top-20 loiPaymentTable">
	            <table class="table table-bordered table-striped">
				<tr>
				<th scope="col" width="10%"><spring:message text="Sr. No" code="loi.lbl.srNo" /></th>
				<th scope="col" width="75%"><spring:message text="Charge Name" code="loi.charge.name" /></th>
				<th scope="col" width="15%"><spring:message text="Amount" code="loi.amt" /></th>
				</tr>
				<c:forEach var="charges" items="${command.searchDto.getChargeDesc()}" varStatus="status">
				<tr>
				<td class="text-center">${status.count}</td>
				<td>${charges.key}</td>
				<td class="text-right">${charges.value}</td>
				</tr>
				</c:forEach>
				
				<tr>
				<td colspan="2"><span class="pull-right"><b><spring:message text="Total Amount" code="loi.total.amt" /></b></span></td>
				<td class="text-right">${command.searchDto.total}</td>
				</tr>
				</table>
			</div>
			<p class="text-center"><spring:message code="loi.valid" text="This LOI is Valid for Period of 30 days"></spring:message>
			<spring:message code="loi.sys.gen.message" text="This is system generated document. Signature is not required"></spring:message></p>
		</div>

		<div class="text-center hidden-print padding-bottom-20 margin-top-20">
			<!--  <button type="submit" class="btn btn-success" onclick="return saveData(this);">Submit</button> -->
			<button onclick="printLoiContent('loiPrint')" class="btn btn-success"> <i class="icon-print-2"></i> <spring:message code="loi.print" text="Print"/></button>	
			<button type="button" class="btn btn-danger" onclick="window.location.href='LoiPrintingController.html'"><spring:message code="loi.back" text="Back"/></button>
		</div>
 </c:if>
 <c:if test="${command.loiMaster.loiRecordFound eq 'N' }">
  <span><b>No Record Found</b></span>
 </c:if>
 </form:form>
 </div>
 </div>
 </div>
 </div>
          