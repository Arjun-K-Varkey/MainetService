<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <script type="text/javascript" src="js/mainet/validation.js"></script>
 <script type="text/javascript" src="js/property/propertyBillPayment.js" ></script>   
 <script type="text/javascript" src="js/mainet/file-upload.js"></script>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <script>
	$(document).ready(function(){
		 var payAmount=$('#payAmount').val();
			$('#amountToPay').val(payAmount);
	});

	$('#payAmount').on('change',function(){
		 var payAmount=$('#payAmount').val();
			$('#amountToPay').val(payAmount);
		});
 </script>
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start Content here --> 
<div class="content"> 
  <div class="widget">
    <div class="widget-header">
      <h2><spring:message code="propertyBill.BillCollection"/></h2>
     
      	<apptags:helpDoc url="PropertyBillPayment.html"></apptags:helpDoc>	     
     
    </div>
    <div class="widget-content padding">
        <div class="mand-label clearfix">
				<span><spring:message code="cfc.mandatory.message"
						text="Field with * is mandatory" /></span>
		</div>
      <form:form action="PropertyBillPayment.html" class="form-horizontal" name="PropertyBillPayment" id="PropertyBillPayment">
       		<jsp:include page="/jsp/tiles/validationerror.jsp" />
       		<form:hidden path="billingMethod" id="billingMethod"/>
       		<form:hidden path="halfPaymentRebate" id="halfPaymentRebate"/>
       		<form:hidden path="rebateApplFlag" id="rebateApplFlag"/>
       		<form:hidden path="receivedAmount" id="receivedAmount"/>
       		<form:hidden path="billPayDto.fullOutstandingAmount" id="fullOutstandingAmount"/>
       		<form:hidden path="billPayDto.actualRebate" id="actualRebate"/>
       		<form:hidden path="billPayDto.halfOutstandingAmount" id="halfOutstandingAmount"/>
       		<form:hidden path="billPayDto.PaidAmountInCurrentYear" id="PaidAmountInCurrentYear"/>
       		<form:hidden path="billPayDto.actualFullOutstandingAmount" id="actualFullOutstandingAmount"/>
       		<form:hidden path="billPayDto.actualHalfOutstandingAmount" id="actualHalfOutstandingAmount"/>
       		<form:hidden path="billPayDto.penaltyDiscount" id="penaltyDiscount"/>
       		<form:hidden path="" id="serviceCode" value="PBP"/>
       		
       		<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
       		
		
            <div class="panel-group accordion-toggle" id="#accordion_single_collapse">  
<%--        <c:if test="${(command.billMas.csIdn ne null) or (not empty command.message && command.message ne null)}"> --%>
        	<div class="panel panel-default" id="paymentInfo">
	                <div class="panel-heading">
	                  <h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#ConnectionDetails"><spring:message code="property.Propertydetail" text="Property Details"/></a></h4>
	  				</div>
	  				<div id="ConnectionDetails" class="panel-collapse collapse in">
				         <div class="panel-body">
	  				
					<div class="form-group" >		
						<apptags:input labelCode="propertydetails.PropertyNo." path="billPayDto.propNo" cssClass="mandColorClass" isReadonly="true"></apptags:input>
						<%-- <apptags:input labelCode="propertydetails.oldpropertyno" path="billPayDto.oldpropno" cssClass="mandColorClass" isReadonly="true"></apptags:input> --%>		
					</div>
  				
				         
				         <div class="form-group">
				         <apptags:input labelCode="property.FlatNo" path="propBillPaymentDto.flatNo" isReadonly="true"></apptags:input>		         
				         	<apptags:textArea labelCode="ownerdetail.Ownername" path="billPayDto.primaryOwnerName" isDisabled="true" ></apptags:textArea>
<%-- 				         	<apptags:input labelCode="propertydetails.PropertyNo." path="billPayDto.propNo" isDisabled="true" ></apptags:input> --%>
				         	
				         </div>
				         <c:if test="${not empty command.occupierName}">
				         <div class="form-group">	         
				         	<apptags:textArea labelCode="ownerdetail.occupierName" path="occupierName" isDisabled="true" ></apptags:textArea>				         	
				         </div>
				         </c:if>
				         <div class="form-group" >		
						<apptags:input labelCode="property.billPayment.mobileNo" path="billPayDto.mobileNo" cssClass="mandColorClass hasNumber"  maxlegnth="10"></apptags:input>
						<apptags:input labelCode="property.billPayment.emailId" path="billPayDto.emailId" cssClass="mandColorClass"></apptags:input>		
					</div>
					
					 <div class="form-group" >		
						<apptags:input labelCode="property.billPayment.payeeName" path="payeeName" cssClass="mandColorClass"></apptags:input>
					</div>
				         </div>
		         	</div>
         
		          	<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyAddress"><spring:message
									code="property.Propertyaddress" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyAddress">
							<div class="form-group">
								
								<apptags:textArea isMandatory="true" labelCode="property.propertyaddress" path="billPayDto.address" isDisabled="true"></apptags:textArea>
								<apptags:input labelCode="property.location" path="billPayDto.location" isDisabled="true"></apptags:input>
								<%-- <apptags:select cssClass="chosen-select-no-results" labelCode="property.location" items="" path="" isMandatory="true" isLookUpItem="true" selectOptionLabelCode="select Location">
								</apptags:select> --%>		
							</div>
							
							<%-- <div class="form-group">					
								<apptags:input cssClass="form-control hasPincode" labelCode="property.pincode" path="billPayDto.pinCode"  isDisabled="true"></apptags:input>							
							</div> --%>
					     </div> 
		            </div>
          
         

        	<div class="panel panel-default">
                <div class="panel-heading">
                  <h4 class="panel-title"><a data-toggle="collapse" class="" data-parent="#accordion_single_collapse" href="#PayableAmountDetails"><spring:message code="property.ReceiptAmountDetails" text="Receipt Amount Details"/></a></h4>
  				</div>
  				
  				<div id="PayableAmountDetails" class="panel-collapse collapse in">
         			<div class="panel-body">

									<c:if test="${command.billPayDto.billDisList  ne null && not empty command.billPayDto.billDisList}">
									 <div class="table-responsive">
							            <table class="table table-bordered table-condensed">
							            
							              <tr>
							                <th colspan="3"><spring:message code="propertyBill.TaxDescription" text="Tax Description"/></th>
							                <th><spring:message code="propertyBill.BalanceArrears" text="Balance Arrears"/></th>
							                <th><spring:message code="propertyBill.CurrentTax" text="Current Tax"/></th>
							                <th class="text-right"><spring:message code="propertyBill.TotalBalance" text="Total Balance"/></th>
							              </tr>
						               <c:forEach items="${command.billPayDto.billDisList}" var="billDet" varStatus="detStatus">  
						            <tr>
						              <td colspan="3">${billDet.taxDesc}</td> 
						                <td class="text-right">${billDet.arrearsTaxAmt}</td> 
						                <td class="text-right">${billDet.currentYearTaxAmt}</td> 
						               <td class="text-right">${billDet.totalTaxAmt}</td> 
							             </tr> 
							               </c:forEach>
							               <tr>
							               <th colspan="3" class="text-right"><spring:message code="property.billPayment.total" text="Total"/></th>
							               <th class="text-right">${command.billPayDto.totalArrearAmt}</th>
							               <th class="text-right">${command.billPayDto.totalCurrentAmt}</th>
							               <th class="text-right">${command.billPayDto.totalBalAmt}</th>
							               
							               
							               </tr>

												<tr>
													<th class="text-right"><spring:message code="property.billPayment.totalPenalty" text="Current Penalty"/></th>
													<th class="text-right">${command.billPayDto.totalPenalty}</th>
													<th class="text-right"><spring:message code="Advance.property.Amountbilltable" text="Advance Amount"/></th>
													<th class="text-right">${command.billPayDto.advanceAmt}</th>
													<th class="text-right"><spring:message code="property.billPayment.rebate" text="Rebate"/></th>
													<fmt:formatNumber type="number" value="${command.billPayDto.totalRebate}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="rebateAmount"/>
													<th id="rebateId" class="text-right">${rebateAmount}</th>
												</tr>
										<c:if test="${command.isAbhayYojanaApplicable() eq true}">		
										<tr>
							               <th colspan="5" class="text-right"><spring:message code="property.intrest.waive" text="75% Interest Waive"/></th>
							               <fmt:formatNumber type="number" value="${command.billPayDto.penaltyDiscount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="penaltyDiscount"/>
							               <th class="text-right">${penaltyDiscount}</th>
							           	</tr>
							            </c:if>   
											</table>
							          </div>
								
								</c:if> 
							        <div class="form-group padding-top-10" >
									<label class="col-sm-2 control-label"><spring:message code="propertyBill.TotalReceivable"/></label>
									<div class="col-sm-4">
									<div class="input-group">
							          <form:input path="billPayDto.totalPayableAmt" cssClass="form-control" id="totalPayable" readonly="true"/>
							          <label class="input-group-addon"><i class="fa fa-inr"></i></label>
							        </div>
									</div>
									
									<c:choose>
	              					<c:when test="${command.billPayDto.partialAdvancePayStatus eq 'PAI'}">	              					
									<label class="col-sm-2 control-label"><spring:message code="propertyBill.EnterReceiptAmount"/></label>
									<div class="col-sm-4">
 									<div class="input-group"> 
								         <form:input path="billPayDto.totalPaidAmt" cssClass="form-control mandColorClass" maxlength="10" id="payAmount" readonly="true"/>
								         <label class="input-group-addon"><i class="fa fa-inr"></i></label>
								     </div> 
									</div>
									</c:when>
									<c:otherwise>
									<label class="col-sm-2 control-label"><spring:message code="propertyBill.EnterReceiptAmount"/></label>
									<div class="col-sm-4">
 									<div class="input-group"> 
								      	   <form:input path="billPayDto.totalPaidAmt" cssClass="form-control mandColorClass"  maxlength="10" id="payAmount" onblur="checkRebateAppl()"/>
								           <label class="input-group-addon"><i class="fa fa-inr"></i></label>
								     </div> 
								     
								   <%--   <form:input path="billPayDto.totalRebate" cssClass="form-control" id="totalRebate" readonly="true"/> --%>
								     
									</div>
									</c:otherwise>
									</c:choose>
									</div>
		<c:if test="${command.billPayDto.billDisList eq null || empty command.billPayDto.billDisList}">
		 <div class="form-group" >
		<label class="col-sm-10 text-red"><spring:message code="property.billDueValid" text="Since No Dues are pending, Your Payment will be considered as Advance Payment"/></label>
		</div>
		</c:if> 
									
				<c:if test="${command.receiptType eq 'M'}">
<div class="form-group padding-top-10" >	
			<apptags:input labelCode="Manual.prop.ReceiptNolabel" path="manualReceiptNo"></apptags:input>
			<apptags:input labelCode="Manual.billReceiptDateprop" path="manualReeiptDate"  isReadonly="true" cssClass="trimDateTime"></apptags:input>
			</div>
			
			
		<div class="overflow margin-top-10">
											<div class="table-responsive">
												<table
													class="table table-hover table-bordered table-striped">
													<tbody>
														<tr>
															<th> <spring:message
																		code="water.serialNo" text="Sr No" />
															</th>
															<th> <spring:message
																		code="water.docName" text="Document Name" />
															</th>
															<th> <spring:message
																		code="water.status" text="Status" />
															</th>
															<th> <spring:message
																		code="water.uploadText" text="Upload" />
															</th>
														</tr>
															<tr>
																<td>1</td>
																<td>Manual Receipt</td>
																	<td> <spring:message
																				code="water.doc.mand" />
																	</td>
																<td><div id="docs_0" class="">
																		<apptags:formField fieldType="7" labelCode=""
																			hasId="true" fieldPath=""
																			isMandatory="false" showFileNameHTMLId="true"
																			fileSize="BND_COMMOM_MAX_SIZE"
																			maxFileCount="CHECK_LIST_MAX_COUNT"
																			validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
																			currentCount="0" />
																	</div>
																	</td>
															</tr>
													</tbody>
												</table>
											</div>
										</div>									
			

</c:if>					
								<%-- <c:if test="${not empty command.message && command.message ne null }">
								 <div class="form-group" >
								<label class="col-sm-10 control-label text-red">${command.message}</label>
								</div>
								</c:if> --%> 
					</div>
				</div>
			</div>
		<div class="panel panel-default">
		<jsp:include page="/jsp/cfc/Challan/offlinePay.jsp"/>
		</div>
		<%-- <div class="panel panel-default">
		<jsp:include page="/jsp/cfc/Challan/offlinePayForMultiMode.jsp"/>
		</div> --%>
		
         <div class="text-center padding-top-10">
          <button type="button" class="btn btn-success btn-submit"  onclick="savePropertyFrom(this)"><spring:message code="propertyBill.Submit"/></button>
           <button type="button" class="btn btn-danger" onclick="backToMain(this)"><spring:message code="propertyBill.Back"/></button>
         </div>
<%--        </c:if> --%>
       </div>
       </div>
      </form:form>
    </div>
  </div>
</div>

