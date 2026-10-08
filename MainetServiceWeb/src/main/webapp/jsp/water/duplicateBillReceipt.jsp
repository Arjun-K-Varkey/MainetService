<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/water/duplicateBillReceipt.js"></script>
 <style>
 
.spaceTop{
	margin-top: 10px;
}
label[for="dwzid3"]{
		clear: both;
}
label[for="dwzid3"],
label[for="dwzid3"] + div{
	margin-top: 0.7rem;
}
</style>
 <apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2> <!--   code="receipt.title"  -->
				<spring:message code="water.duplicate.bill.page" text="Duplicate Bill" />
			</h2>
			<apptags:helpDoc url="DuplicateBillReceipt.html"></apptags:helpDoc>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="" text="" /> <i
					class="text-red-1"></i> <spring:message
					code="cfc.mandatory.message"
					text="Field with * is mandatory" /></span>
			</div>
			<form:form id="duplicateBillReceipt" action="DuplicateBillReceipt.html" method="post"
				class="form-horizontal">
 				<form:hidden path="rmRcptid" id="rmRcptid"/>
 				<form:hidden path="serviceCode" id="serviceCode"/>
 				
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span id="errorId"></span>
				</div>
				<apptags:applicantDetail wardZone="WWZ"></apptags:applicantDetail>
				<div class="panel-group accordion-toggle"
						id="accordion_single_collapse">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 id="consumerBillDetails" class="panel-title">
								<a data-toggle="collapse" class="collapsed"
									data-parent="#accordion_single_collapse"
									href="#consumerBillDetails"> <spring:message
									code="water.duplicate.bill.consumer.details" text="Consumer Detail" />
								</a>
							</h4>
						</div>
						
					<div class="spaceTop"></div>
	 
					<div class="form-group">
						<label class="col-sm-2 control-label required-control">
							<spring:message code="water.ConnectionNo"></spring:message></label>
						<div class="col-sm-4">
							<form:input path="connectionNo" type="text" class="form-control" id="conNum"  
								onblur="getBillList()"></form:input>
						</div>
						<label class="col-sm-2 control-label required-control"><spring:message
								 code="water.duplicate.page.bill.no" text="Bill No" />
						</label>
						<div class="col-sm-4">
							<form:select path="billNo" id="bill" class="form-control" name="" selected ="true">
	 							<option value=""><spring:message text="Select" /></option>
							</form:select>
						</div>
						
					</div>
					
					<div class="form-group">
					<!-- code="water.lable.name.noOfcopies"  -->
						<label class="col-sm-2 control-label required-control"><spring:message
								code="water.duplicate.page.bill.copies" text="Number Of Copies" /></label>
						<div class="col-sm-4">
							<form:input path="noOfCopies" id="noOfCopies"
								name="noOfCopies" type="text" class="form-control"></form:input>
						</div>
					</div>
				</div>
			</div>
                <form:hidden path="paymentCheck" id="paymentCheck"></form:hidden> 
  				 <c:if test="${command.paymentCheck ne null && command.paymentCheck eq 'Y'}">
 				
					<div class="form-group">
					<!-- code="cfc.field.name.amounttopay" --> 
						<label class="col-sm-2 control-label"><spring:message
								code="water.duplicate.page.payableamount" text="Payable Amount" /></label>
						<div class="col-sm-4">
							<form:input class="form-control" path=""
								value="${command.applicationCharge}" maxlength="12"
								readonly="true"></form:input>
							 <a class="fancybox fancybox.ajax text-small text-info"
								href="DuplicateBillReceipt.html?showChargeDetails" target="_blank"><spring:message
									code="water.duplicate.page.charge.details" text="Charge Details" /> <!-- code="cfc.lable.name.chargedetail" -->
							 <i class="fa fa-question-circle "></i></a>
						</div>
					</div>
					<div class="panel">
	  					<jsp:include page="/jsp/cfc/Challan/offlinePay.jsp" />
  					</div>
  				</c:if> 

				<c:if test="${command.paymentCheck ne null}">

					<div class="padding-bottom-10 text-center">

						<button type="button" class="btn btn-submit" id="continueForm"
							onclick = "proceedToSaveDetails(this,'DuplicateBillReceipt.html')">
 							<spring:message code="water.btn.submit" text="Submit"/> <!-- code="NHP.submit" -->
						</button>
						<button type="button" class="btn btn-danger" id="back"
							onclick="backPage()">
							<spring:message code="water.btn.back" text="Back"></spring:message> <!-- code="NHP.back" -->
						</button>

					</div>

				</c:if>


 				<c:if test="${command.paymentCheck eq null}">
					<div class="padding-top-10 text-center">
						<button type="button" class="btn btn-submit" id="continueForm"
							onclick = "getCharges(this)">
							<spring:message code="water.duplicate.page.btn.proceed" text="Proceed"/>
						</button>

						<button type="button" class="btn btn-warning" title="Reset"
							onclick="ResetForm()">
							<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
							<spring:message code="water.btn.reset" text="Reset"></spring:message> <!--  code="NHP.reset"  -->
						</button>
						<button type="button" class="btn btn-danger" id="back"
							onclick="backPage()">
							<spring:message code="water.btn.back" text="Back"></spring:message> <!-- code="receipt.back" -->
						</button>

					</div>
 				</c:if> 
 			</form:form>
 
		</div>
	</div>
		
</div>