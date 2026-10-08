<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/property/propertyDuesCheck.js"></script>
<style>
.header-div {
	display: none;
}
#BillDetails table th{
	text-align: left !important;
}
</style>

<!-- Start Content here -->
<div class="content">
	<div class="widget" id="receipt">
		
		<div class="widget-content padding">
		<form:form action="PropertyDuesCheck.html" class="form-horizontal"
				name="PropertyDuesCheck" id="PropertyDuesCheckView">
			<div class="cert-outer-div overflow-hidden">	
				<div class="border-bottom-black margin-0 header-div">
					<div class="overflow-hidden padding-vertical-10">
						<div class="col-xs-2 col-sm-2">
							<img width="80" src="${userSession.orgLogoPath}">
						</div>
						<div class="col-xs-8 col-sm-8text-center">
							<h3 class="margin-bottom-10 padding-bottom-10">${userSession.organisation.ONlsOrgname}</h3>
						</div>
					</div>
					<div class="col-xs-12 text-center">
						<h5><spring:message	code="property.duescheck" text="Property Dues" /></h5>
						<p class="text-right padding-10"><spring:message	code="" text="Date" /> : ${command.duesDate}</p>
						<p class="text-right"><spring:message	code="" text="Time" /> : ${command.duesTime}</p>
					</div>
				</div>
				<c:if test="${command.viewCheckPropDueFlg eq 'Y' && command.errorMsg != null}">
    				<strong><p class="text-red-1">${command.errorMsg}</p></strong>
				</c:if>
				<div class="col-xs-6 col-sm-6">
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message
								code="property.duesCheck.property.number" text="Property Number" /></label>
						<div class="col-sm-6">
							<form:input type="text" class="form-control preventSpace"
								disabled="true" path="searchDto.proertyNo" id="proertyNo"
								maxlength="20"></form:input>
						</div>
					</div>
					<div class="form-group">	
				     <c:if test="${command.viewflatNoFlag eq 'Y'}">
						<label class="col-sm-4 control-label"><spring:message
								code="property.FlatNo" text="Flat No" /></label>
						<div class="col-sm-6">
							<form:input name="" type="text" class="form-control preventSpace"
								path="searchDto.flatNo" id="flatNo" disabled="true"></form:input>
						</div>
					</c:if>
					</div>
					<div class="form-group">
							<label for="ownerName" class="col-sm-4 control-label"><spring:message code="property.OwnerName/OccupierName" text="Owner Name/Occupier Name"/></label>
					        <div class="col-sm-6"><form:textarea path="searchDto.occupierName" type="text" class="form-control" disabled="true"/></div>
					</div>
					<div class="form-group">
						<label for="MobileNo" class="col-sm-4 control-label"><spring:message code="property.duesCheck.Mobile.No" text="Mobile No."/></label>
				        <div class="col-sm-6"><form:input path="searchDto.mobileno" type="text" class="form-control" disabled="true"/></div>
			        </div>
			        <div class="form-group">
			        	<label for="duesDate" class="col-sm-4 control-label"><spring:message code="property.duesCheck.as.on.date" text="As On Date"/></label>
				        <div class="col-sm-6"><form:input path="duesDate" type="text" class="form-control" disabled="true"/></div>
			        </div>
				</div>
				
				<div class="col-xs-6 col-sm-6">
					<h4><spring:message code="property.duesCheck.rebate.heading" text="Early Payment Rebate" /></h4>
					<table class="table table-striped table-bordered margin-top-10">
						<tr>
							<th class="text-center"><spring:message code="property.duesCheck.balance.amount" text="Balance Amount" /></th>
							<th class="text-center"><spring:message code="property.duesCheck.payable.amount" text="Payable Amount" /></th>
							<th class="text-center"><spring:message code="property.duesCheck.rabate.amount" text="Rebate Amount" /></th>
						</tr>
						<tr>
							<td><p class="text-right">
								<fmt:formatNumber type="number"
									value="${command.duesBalanceAmount}" groupingUsed="false"
									maxFractionDigits="2" minFractionDigits="2"
									var="duesBalanceAmount" />${duesBalanceAmount} <spring:message code="property.duesCheck.full.amount" text="Full" /></p> </td>
							<td><p class="text-right">
									<fmt:formatNumber type="number"
										value="${command.duesPayableAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="duesPayableAmount" />${duesPayableAmount}</p></td>
							<td><p class="text-right">
									<fmt:formatNumber type="number"
										value="${command.rebateAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="rebateAmount" />${rebateAmount}</p></td>
						</tr>
						<tr>
							<td><p class="text-right">
									<fmt:formatNumber type="number"
										value="${command.halfBalanceAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="halfBalanceAmount" />${halfBalanceAmount} <spring:message code="property.duesCheck.half.amount" text="Half" /></p></td>
							<td><p class="text-right">
									<fmt:formatNumber type="number"
										value="${command.halfDuesPayableAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="halfDuesPayableAmount" />${halfDuesPayableAmount}</p></td>
							<td><p class="text-right"><fmt:formatNumber type="number"
								value="${command.halfRebateAmount}" groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								var="halfRebateAmount" />${halfRebateAmount}</p></td></td>
						</tr>
					</table>
					
					<%-- <div class="form-group padding-20">
			        	<label for="totalPaybale" class="col-sm-4 control-label"><spring:message code="property.duesCheck.totalPayable" text="Total Payable"/></label>
				        <div class="col-sm-6" style="font-weight: 600;"><form:input path="duesBalanceAmount" type="text" class="form-control text-right" disabled="true"/></div>
			        </div> --%>
				</div>
				<div class="clear"></div>
				<div class="accordion-toggle propertyDues col-xs-6 col-sm-6">
						<%-- <h4 class="margin-top-10 margin-bottom-10 panel-title" id="">
							<spring:message
									code="property.duesCheck.details" text="Property Dues Details" />
						</h4> --%>
							<table id="BillDetails"
								class="table table-striped table-bordered margin-top-10">
								<tr>
									<th><p>
												<spring:message code="property.duesCheck.totalArrears" text="Total Arrears" />
											</p></th>
									<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.totalArrearsAmt}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="totalArrearsAmt" />${totalArrearsAmt}</p></td>	
								</tr>
								<tr>						
									<th><p>
											<spring:message code="property.duesCheck.currentBillAmt" text="Current Bill Amount" />
											<span class="text-red"><spring:message code="property.duesCheck.current.amount1" text="(April to September)" /></span>
										</p></th>
									<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.currentFirstSemBalanceAmt}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="halfCurrentBalanceAmt" />${halfCurrentBalanceAmt}</p></td>	
								</tr>
								<tr>				
									<th><p>
											<spring:message code="property.duesCheck.currentBillAmt" text="Current Bill Amount" />
											<span class="text-red"><spring:message code="property.duesCheck.current.amount2" text="(October to March)" /></span>
										</p></th>	
									<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.currentSecondSemBalanceAmt}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="halfCurrentBalanceAmt" />${halfCurrentBalanceAmt}</p></td>			
								</tr>
			
								
								<tr>
										<th><p>
												<spring:message code="property.duesCheck.totalPenalty" text="Total Penalty" />
											</p></th>
										<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.totalPenalty}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="totalPenalty" />${totalPenalty}</p></td>
								</tr>
								<tr>					
										<th><p>
												<spring:message code="property.duesCheck.advance.amount" text="Advance Payment"/>
											</p></th>
										<td><p class="text-right">
												<fmt:formatNumber type="number"
													value="${command.advanceAmount}" groupingUsed="false"
													maxFractionDigits="2" minFractionDigits="2"
													var="advanceAmt" />${advanceAmt}</p></td>
								</tr>
								<%-- <tr>				
										<th><p>
											<spring:message code="property.duesCheck.positive.adjustmentAmount" text="+ve Adjustment Amount" />
										</p></th>
										<c:choose>
											<c:when test="${command.positiveAajustment eq 'Negative' && command.positiveAdjustmentAmount gt 0}">
												<td><p class="text-right"><fmt:formatNumber type="number"
													value="${command.positiveAdjustmentAmount}" groupingUsed="false"
													maxFractionDigits="2" minFractionDigits="2" var="positiveAdjAmount"/>${positiveAdjAmount}</p></td>
											</c:when>
											<c:otherwise>
												<td><p class="text-right"><fmt:formatNumber type="number"
													value="${command.positiveAdjustmentAmount}" groupingUsed="false"
													maxFractionDigits="2" minFractionDigits="2" var="positiveAdjAmount"/>${positiveAdjAmount}</p></td>
											</c:otherwise>
										</c:choose>
								</tr>
								
								<tr>				
										<th><p>
											<spring:message code="property.duesCheck.negative.adjustmentAmount" text="-ve Adjustment Amount" />
										</p></th>
										<c:choose>
											<c:when test="${command.positiveAajustment eq 'Negative' && command.negativeAdjustmentAmount gt 0}">
												<td><p class="text-right"><fmt:formatNumber type="number"
													value="${command.negativeAdjustmentAmount}" groupingUsed="false"
													maxFractionDigits="2" minFractionDigits="2" var="negativeAdjAmount"/>${negativeAdjAmount}</p></td>
											</c:when>
											<c:otherwise>
												<td><p class="text-right"><fmt:formatNumber type="number"
													value="${command.negativeAdjustmentAmount}" groupingUsed="false"
													maxFractionDigits="2" minFractionDigits="2" var="negativeAdjAmount"/>${negativeAdjAmount}</p></p></td>
											</c:otherwise>
										</c:choose>
								</tr> --%>
									
								<tr>					
										<th><p>
												<spring:message code="" text="Total Amount" />
											</p></th>	
										<td><p class="text-right"  style="font-weight: 600;">
											<fmt:formatNumber type="number"
												value="${command.duesBalanceAmount}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="duesBalanceAmount" />${duesBalanceAmount}</p> </td>		
								</tr>						
								
								<%-- <tr>
								   <th><p class="text-center">
											<spring:message code="property.duesCheck.fullRebate" text="Full Rebate" />
										</p></th>
									<th><p class="text-center">
											<spring:message code="property.duesCheck.halfRebate" text="Half Rebate" />
										</p></th>
									<th><p class="text-center">
											<spring:message code="property.duesCheck.totalPayable" text="Total Payable" />
										</p></th>
									
								</tr>
								
								<tr>
								<td><p class="text-center">
											<fmt:formatNumber type="number"
												value="${command.rebateAmount}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="rebateAmount" />${rebateAmount}</p></td>
								<td><p class="text-center">
											<fmt:formatNumber type="number"
												value="${command.halfRebateAmount}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="halfRebateAmount" />${halfRebateAmount}</p></td>	
												
									<td><p class="text-center">
											<fmt:formatNumber type="number"
												value="${command.totalPaybale}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="totalPaybale" />${totalPaybale}</p></td>
											
								</tr> --%>

							</table>
					</div>
					<c:if test="${command.isAbhayYojanaApplicable() eq true}">
					<div class="accordion-toggle propertyDues col-xs-6 col-sm-6">
						<table id="abhayYojan" class="table table-striped table-bordered margin-top-10">
							<thead>
								<tr>
									<th colspan="2"><spring:message code="property.abhay.yojana" text="Abhay Yojana-2024" /> </th>
								</tr>
							</thead>	
							<tbody>
								<tr>
									<td><spring:message code="property.totalDuesAmt" text="Total Dues" /></td>
									<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.duesBalanceAmount}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="duesBalanceAmount" />${duesBalanceAmount}</p> </td>
								</tr>
								<tr>
									<td><spring:message code="property.intrest.waive" text="75% Intrest Waive" /></td>
									<td><p class="text-right">
											<fmt:formatNumber type="number"
												value="${command.abhayYojanaAmnt}" groupingUsed="false"
												maxFractionDigits="2" minFractionDigits="2"
												var="abhayYojanaAmnt" />${abhayYojanaAmnt}</p> </td>
								</tr>
								<tr>
									<td><spring:message code="property.duesCheck.rebate.heading" text="Early Payment Rebate" /></td>
									<td><p class="text-right">
									<fmt:formatNumber type="number"
										value="${command.rebateAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="rebateAmount" />${rebateAmount}</p></td>
								</tr>
								<tr>
									<td><p style="font-weight: 600;"><spring:message code="property.bill.netpayable" text="Net Payable" /></p></td>
									<td><p class="text-right" style="font-weight: 600;">
									<fmt:formatNumber type="number"
										value="${command.netPayableAmount}" groupingUsed="false"
										maxFractionDigits="2" minFractionDigits="2"
										var="netPayableAmount" />${netPayableAmount}</p></td>
								</tr>
							</tbody>
						</table>
					</div>
					</c:if>
				</div>
					<div class="col-xs-12 col-sm-12 padding-top-10 text-center hidden-print">
						<button type="button" class="btn btn-danger"  id="back"
							onclick="propertyBillPrint()"><spring:message code="property.checkDues.viewLedgerReport" text="View Ledger Report"/>
						</button>
					</div>
					<div class="col-xs-12 col-sm-12 padding-top-10 text-center hidden-print">
						<button onclick="printContent('receipt')"
							class="btn btn-primary hidden-print">
							<i class="icon-print-2"></i>
							<spring:message code="bnd.acknowledgement.print" text="Print"></spring:message>
						</button>
						<c:if test="${command.viewCheckPropDueFlg ne 'Y'}">
						<button type="button" class="btn btn-danger" id="back"
							onclick="backToPropDetails();">
							<spring:message code="property.Back" text="Back"></spring:message>
						</button>
						</c:if>
						
					<c:if test="${command.viewCheckPropDueFlg eq 'Y'}">
						<button type="button" class="btn btn-danger" id="back"
							onclick="backToPropDetailPage();">
							<spring:message code="property.Back" text="Back" />
						</button>
					</c:if>

				</div>
					</form:form>
					<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>
		</div>
	</div>
</div>
<script>
    function printContent(el) {
	var restorepage = document.body.innerHTML;
	var printcontent = document.getElementById(el).innerHTML;
	document.body.innerHTML = printcontent;
	window.print();
	document.body.innerHTML = restorepage;
    }
</script>