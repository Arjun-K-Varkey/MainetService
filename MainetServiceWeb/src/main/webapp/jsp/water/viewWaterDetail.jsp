<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/water/viewWaterDetails.js"></script>
<style>
label[for="codDwzid3"] {
	clear: both;
}

label[for="codDwzid3"], label[for="codDwzid3"]+div {
	margin-top: 0.7rem;
}
th {
  position: sticky;
  top: 0px;
}
</style>
<div id="viewWaterDetails">
	<apptags:breadcrumb></apptags:breadcrumb>

	<div class="content">
		<div class="widget">
			<div class="widget-header">
				<h2>
					<spring:message code="water.connection.details"
						text="Water Connection Details" />
				</h2>
			</div>

			<div class="widget-content padding">

				<form:form action="ViewWaterDetails.html" name="ViewWaterDetails"
					id="ViewWaterDetails" class="form-horizontal">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>

					<c:if test="${command.showForm eq 'N'}">
						<div class="form-group">
							<label class="col-sm-2 control-label"><spring:message
									code="water.dataentry.property.number" text="Property number" /></label>
							<div class="col-sm-4">
								<form:input type="text"
									class="form-control preventSpace alphaNumeric"
									path="entrySearchDto.propertyNo" id="propertyNo"></form:input>
							</div>
							<label class="col-sm-2 control-label"><spring:message
									code="water.dataentry.connection.number"
									text="Connection number" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace"
									path="entrySearchDto.csCcn" id="connectionNo" maxlength="20"
									minlength="9"></form:input>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="firstName"><spring:message
									code="water.dataentry.consumer.name" text="Consumer Name" /></label>
							<div class="col-sm-4">
								<form:input name="" type="text" class="form-control"
									path="entrySearchDto.csName" id="csFirstName"></form:input>
							</div>
							<label class="col-sm-2 control-label"><spring:message
									code="applicantinfo.label.mobile" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control"
									path="entrySearchDto.csContactno" id="csMobileNo"
									data-rule-minlength="10" data-rule-maxlength="10"></form:input>
							</div>
						</div>

						<div class="form-group wardZone">
							<apptags:lookupFieldSet baseLookupCode="WWZ" hasId="true"
								showOnlyLabel="false" pathPrefix="entrySearchDto.codDwzid"
								hasLookupAlphaNumericSort="true"
								hasSubLookupAlphaNumericSort="true"
								cssClass="form-control required-control " showAll="false"
								showData="true" />
						</div>
						<div class="padding-top-10 text-center">

							<button type="button" class="btn btn-success"
								onclick="return searchWaterDetail(this);">
								<i class="fa fa-search"></i>
								<spring:message code="water.search" />
							</button>
							<button type="button" class="btn btn-warning"
								onclick="resetViewWater()">
								<spring:message code="water.btn.reset" />
							</button>
							<button type="button" class="btn btn-danger"
								onclick="window.location.href='AdminHome.html'">
								<spring:message code="water.nodues.cancel" />
							</button>
						</div>

						<div class="table-responsive">
							<div class="table-responsive margin-top-10">
								<table
									class="table table-striped table-condensed table-bordered"
									id="advertiserTable">
									<thead>
										<tr>
											<th><spring:message code="water.nodues.property.number" text="Property Number" /></th>
											<th><spring:message code="water.nodues.connection.number" text="Connection Number" /></th>
											<th><spring:message code="water.nodues.consumer.name" text="Consumer Name" /></th>
											<th><spring:message code="water.reconnection.mobileNo" text="Mobile No" /></th>
											<th><spring:message code="water.plumberLicense.address" text="Address" /></th>
											<th><spring:message code="water.Action" text="Action" /></th>

										</tr>
									</thead>
									<tbody>
										<c:forEach items="${command.entrySearchDtoList}"
											var="searchDto">
											<tr>
												<td>${searchDto.propertyNo }</td>
												<td>${searchDto.csCcn }</td>
												<td>${searchDto.csName }</td>
												<td>${searchDto.csContactno }</td>
												<td>${searchDto.csAdd }</td>
												<td class="text-center">
													<button type="button" class="btn btn-blue-2 btn-sm"
														title="View Advertiser Master"
														onclick="viewWaterDetail(${searchDto.csCcn})">
														<i class="fa fa-eye"></i>
													</button>

												</td>
											</tr>
										</c:forEach>

									</tbody>
								</table>
							</div>
						</div>

					</c:if>
					<c:if test="${command.showForm eq 'Y'}">

						<div class="accordion-toggle">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#a1"> <spring:message
										code="water.details.page.connection.details" text="Connection Details" /></a>
							</h4>
							<div id="a1" class="panel-collapse collapse in">
								<div class="form-group">
									<label class="col-sm-2 control-label"><spring:message
											code="water.ConnectionNo" text="Connection number" /></label>
									<div class="col-sm-4">
										<form:input type="text"
											class="form-control preventSpace alphaNumeric"
											path="entrySearchDto.csCcn" id="connNo" disabled="true"></form:input>
									</div>
									<label class="col-sm-2 control-label"><spring:message
											code="water.details.page.water.outstanding" text="Water Outstanding Amount" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" disabled="true"
											path="entrySearchDto.payableAmount" id="payableAmount"></form:input>
									</div>

								</div>
								<div class="form-group">
									<apptags:input labelCode="water.details.page.property.number"
										path="viewConnectionDto.propertyNo" isReadonly="true"></apptags:input>
									<apptags:input labelCode="water.details.page.bhagirathi.connection"
										path="viewConnectionDto.bplFlag" isReadonly="true"></apptags:input>
								</div>

								<div class="form-group">
									<apptags:input labelCode="water.details.page.total.outstanding.amt"
										path="viewConnectionDto.totalOutsatandingAmt"
										isReadonly="true"></apptags:input>

									<label class="col-sm-2 control-label"><spring:message
											code="water.ConnectionSize" /></label>

									<div class="col-sm-4">
										<div class="input-group">
											<c:set var="baseLookupCode" value="CSZ" />
											<form:select path="viewConnectionDto.connSize"
												class="form-control" disabled="true" id="conSize">
												<form:option value="0">
													<spring:message code="water.select" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
											</form:select>
											<span class="input-group-addon"><spring:message
													code="water.inch" /></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<apptags:lookupFieldSet baseLookupCode="CCG" hasId="true"
										showOnlyLabel="false"
										pathPrefix="viewConnectionDto.csCcncategory"
										hasLookupAlphaNumericSort="true"
										hasSubLookupAlphaNumericSort="true" disabled="true"
										cssClass="form-control" />
								</div>
								<div class="form-group">
									<apptags:lookupFieldSet
										cssClass="form-control required-control" baseLookupCode="TRF"
										hasId="true" pathPrefix="viewConnectionDto.trmGroup"
										hasLookupAlphaNumericSort="true"
										hasSubLookupAlphaNumericSort="true" showAll="true"
										disabled="true" />
								</div>
								<div class="form-group">

									<label class="control-label col-sm-2"> <spring:message
											code="MeterReadingDTO.meterType" /></label>
									<div>
										<c:set var="baseLookupCode" value="WMN" />
										<apptags:lookupField
											items="${command.getLevelData(baseLookupCode)}"
											path="viewConnectionDto.csMeteredccn" cssClass="form-control"
											hasChildLookup="false" hasId="true" showAll="false"
											selectOptionLabelCode="eip.select" disabled="true" />
									</div>

									<label class="col-sm-2 control-label"><spring:message
											code="water.details.page.application.date" text="Application Date" />:</label>
									<div class="col-sm-4">
										<form:input path="applicationDate" cssClass="form-control cal"
											disabled="true" />
									</div>
								</div>
								<div class="form-group">

									<label class="control-label col-sm-2"> <spring:message
											code="MeterReadingDTO.pcDate" /></label>
									<div class="col-sm-4">
										<form:input path="physicalConnectionDate"
											cssClass="form-control cal" disabled="true" />
									</div>

									<label class="col-sm-2 control-label"><spring:message
											code="MeterReadingDTO.meterInstallationRestorationDate" /></label>
									<div class="col-sm-4">
										<form:input path="meterInsRestDate"
											cssClass="form-control cal" disabled="true" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label"><spring:message
											code="water.division" text="divison" /></label>
									<div class="col-sm-4">
										<form:input path="viewConnectionDto.codDwzid1"
											cssClass="form-control" disabled="true" />
									</div>
									<label class="col-sm-2 control-label"><spring:message
											code="water.Ward" text="ward" /></label>
									<div class="col-sm-4">
										<form:input path="viewConnectionDto.codDwzid2"
											cssClass="form-control" disabled="true" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label"><spring:message
											code="water.zone" text="zone" /></label>
									<div class="col-sm-4">
										<form:input path="viewConnectionDto.codDwzid3"
											cssClass="form-control" disabled="true" />
									</div>
									<label class="col-sm-2 control-label"><spring:message
											code="water.meterDet.metNo" text="Meter No" /></label>
									<div class="col-sm-4">
										<form:input path="viewConnectionDto.meterNo"
											cssClass="form-control" disabled="true" />
									</div>	
								</div>
								<div class="form-group">
									<label for="goGreenReq" class="col-sm-2 control-label">
			    						<spring:message code="go.green" />
									</label>
									<div class="col-sm-4">
									    <form:input type="text" class="form-control" id="goGreenReq"
									           path="viewConnectionDto.goGreenReq"
									           disabled="true" />
									</div>
											
									<label for="goGreenReqDate" class="col-sm-2 control-label">
										<spring:message code="go.green.date" />
									</label>
									<div class="col-xs-4 col-sm-4">
										<form:input
											path="viewConnectionDto.goGreenReqDateFormat"
											type="text" id="goGreenReqDate" class="form-control"
											disabled="true" />
									</div>
							</div>
							</div>
						</div>

						<div class="accordion-toggle">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#a2"><spring:message
										code="water.dataentry.consumer.details"
										text="Consumer Details" /></a>
							</h4>
							<div id="a2" class="panel-collapse collapse in">
								<div class="form-group">
									<apptags:input labelCode="water.consumerName"
										path="viewConnectionDto.csName" isReadonly="true"></apptags:input>
									<apptags:input labelCode="water.details.page.gender"
										path="viewConnectionDto.csOGender" isReadonly="true"></apptags:input>
								</div>

								<div class="form-group">
									<apptags:input labelCode="water.plumberLicense.address"
										path="viewConnectionDto.csAdd" isReadonly="true"></apptags:input>
									<apptags:input labelCode="water.pincode"
										path="viewConnectionDto.csCpinCode" isReadonly="true"></apptags:input>
								</div>
								<div class="form-group">
									<apptags:input labelCode="water.reconnection.mobileNo"
										path="viewConnectionDto.csContactno" isReadonly="true"></apptags:input>
									<apptags:input labelCode="water.billPayment.emailId"
										path="viewConnectionDto.csEmail" isReadonly="true"></apptags:input>
								</div>
								<form:hidden path="viewConnectionDto.csCcn" id="connNo" />
							</div>
						</div>

						<%-- <div class="accordion-toggle">

							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#a3"> <spring:message
										code="" text="Consumer Details" /></a>
							</h4>
							<div id="a3" class="panel-collapse collapse in">
								<div class="form-group">
									<apptags:input labelCode="Zone"
										path="viewConnectionDto.codDwzid1" isReadonly="true"></apptags:input>
									<apptags:input labelCode="Ward"
										path="viewConnectionDto.codDwzid2" isReadonly="true"></apptags:input>
								</div>
							</div>

						</div>
						<div class="accordion-toggle">
							<h4 class="margin-top-0 margin-bottom-10 panel-title">
								<a data-toggle="collapse" href="#a4"> <spring:message
										code="" text="Connection Details" />
								</a>
							</h4>
							<div id="a4" class="panel-collapse collapse in">
								<div class="form-group">
									<apptags:input labelCode="Consumer Type" path=""></apptags:input>
								</div>

							</div>
						</div> --%>


						<%-- <c:if
							test="${not empty  command.viewConnectionDto.viewBillMasList}"> --%>
						<div class="accordion-toggle">
							<h4 class="panel-title table margin-top-10" id="">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse" href="#a5"><spring:message
										code="property.BillHistory" text="Bill History" /></a>
							</h4>
							<div id="a5" class="panel-collapse collapse in">
								<div style="height: 250px; overflow: auto">
									<table id="BillDetails"
										class="table table-striped table-bordered margin-top-10">

										<tr>
											<th width="10%"><spring:message code="propertyTax.SrNo" /></th>
											<th width="10%"><spring:message code="property.bill.no"
													text="Bill No" /></th>
											<th width="10%"><spring:message code="water.details.page.bill.generation.dt"
													text="Bill Generation Date" /></th>
											<th width="10%"><spring:message code="water.details.page.bill.due.dt"
													text="Bill Due Date" /></th>
											<th style="text-align: left;" width="10%"><spring:message
													code="water.bill.from.date" text="Bill From Date" /></th>
											<th style="text-align: left;"width="10%"><spring:message
													code="water.bill.to.date" text="Bill To Date" /></th>
											<th width="12%"><spring:message
													code="property.TotalDemand" text="Total Demand" /></th>
											<th width="12%"><spring:message code="water.details.page.total.balance.amt" text="Total Balance Amount" /></th>		
											<th width="8%"><spring:message code="water.details.page.penalty" text="Penalty" /></th>
											<th width="8%"><spring:message code="property.receiptno"
													text="Receipt No" /></th>
											<th width="10%"><spring:message
													code="property.receiptdate" text="Receipt Date" /></th>
											<th width="8%"><spring:message code="water.details.page.receipt.amt"
													text="Receipt Amount" /></th>
											<th width="24%"><spring:message code="water.details.page.narration" text="Narration" /></th>

											<th colspan="2"><spring:message code="property.action"
													text="Action" /></th>

										</tr>

										<tbody>
									<div id="a9" class="panel-collapse collapse in">
									<c:forEach var="billMasList" items="${command.waterBillReceiptList}" varStatus="status">	
									
									
									 <tr>
									  	<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${status.count}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.billno}
										<a href="javascript:void(0);" onclick="downoladBill(${billMasList.bmIdno})"><spring:message code="" text="Download Bill"/></a>
										</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmBilldt}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList) +1}">${billMasList.bmDuedate}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}">${billMasList.bmFromdt}</td>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}">${billMasList.bmTodt}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalAmount"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${bmTotalAmount}</td>
										<fmt:formatNumber type="number" value="${billMasList.bmTotalBalAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="bmTotalBalAmount"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${bmTotalBalAmount}</td>
										<fmt:formatNumber type="number" value="${billMasList.penaltyAmt}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="penaltyAmt"/>
										<td rowspan="${fn:length(billMasList.billWiseReceiptList)+1}" class="text-right">${penaltyAmt}</td>
										<c:choose>
										<c:when test="${empty billMasList.billWiseReceiptList}">
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										</c:when>
										<c:otherwise>
										<c:forEach var="receipt" items="${billMasList.billWiseReceiptList}" varStatus="status">
									<tr>
									
									
											<c:choose>
											  <c:when test="${receipt.extraStringField1 eq 'Y'}">
											      
									    <td><i class="text-red-1">${receipt.lookUpParentId}</i></td>
										<td><i class="text-red-1">${receipt.lookUpCode}</i></td>
										<td><i class="text-red-1">${receipt.otherField}</i></td>
										<td><i class="text-red-1">${receipt.docDescription} (Cheque Dishour)</i></td>
										
										
											   </c:when>
											   <c:otherwise>
											       <td>${receipt.lookUpParentId}</td>
										<td>${receipt.lookUpCode}</td>
										<td>${receipt.otherField}</td>
										<td>${receipt.docDescription}</td>
											    </c:otherwise>
											</c:choose>
	
										
									
									    
										
										
										
										<td class="text-center">
												<button class="btn btn-primary btn-sm" type="button"
																onclick="downoladReceipt(${receipt.lookUpId},${receipt.lookUpParentId},'${receipt.extraStringField2}')">Download
																Receipt</button>
														</td>
									</tr>
									</c:forEach>
										
										</c:otherwise>
										</c:choose>
										
										
									</tr>
									
									</c:forEach>
									</div>
									</tbody>
										<tfoot>
											<tr>
												<th colspan="5" class="text-right"><spring:message code="taxdetails.total"
														text="Total" /></th>
												<th class="text-right">${command.grandTotalDemandWt}</th>
												<th class="text-right">${command.grandTotalBalanceAmtWt}</th>
												<th class="text-right">${command.grandPenaltyAmtwt}</th>
												<th></th>
												<th></th>
												<th class="text-right">${command.grandReceiptAmountWt}</th>
												<th></th>
												<th></th>
											</tr>
										</tfoot>

									</table>
								</div>
							</div>
						</div>
						<%-- </c:if> --%>

						<%-- 				<c:if test="${not empty  command.collectionDetails}"> --%>
						<%-- 	<div class="accordion-toggle">
							<h4 class="panel-title table margin-top-10" id="">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse" href="#a6"><spring:message
										code="property.PaymentHistory" text="Payment History" /></a>
							</h4>
							<div id="a6" class="panel-collapse collapse in">
							<div style="height:250px; overflow:auto">
								<table id="CollectionDetails"
									class="table table-striped table-bordered margin-top-10">

									<tr>

										<th width="10%"><spring:message code="propertyTax.SrNo" /></th>
										<th width="10%"><spring:message code="property.receiptno"
												text="Receipt No" /></th>
										<th width="12%"><spring:message
												code="property.receiptdate" text="Receipt Date" /></th>
										<th width="12%"><spring:message
												code="prop.rec.print.pay.mode" text="Payment Mode" /></th>
										<th width="12%"><spring:message code="property.Amount"
												text="Amount" /></th>
										<th width="12%">Action</th>
									</tr>

									<tbody>
										<c:forEach var="collection"
											items="${command.collectionDetails}" varStatus="status">
											<tr>
												<td>${status.count}</td>
												<td>${collection.lookUpParentId}</td>
												<td>${collection.lookUpCode}</td>
												<td>${collection.lookUpType}</td>
												<td>${collection.otherField}</td>
												<td class="text-center"><button
														class="btn btn-primary btn-sm" type="button"
														onclick="downoladReceipt(${collection.lookUpId},${collection.lookUpParentId})"><spring:message code="water.Download.Receipt"
												text="Download Receipt" /></button></td>

											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							</div>
                         </div> --%>
						<%-- 
						</c:if> --%>
						<div class="text-center padding-top-10">
							<button class="btn btn-blue-2 padding-top-10" id='btnPrint'
								onclick="waterBillPrint(); return false;">
								<spring:message code="water.print.ledger.report"
									text="Print Bill Detail" />
							</button>
						</div>
						<div class="padding-top-10 text-center">
							<button type="button" class="btn btn-danger" id="back"
								onclick="window.location.href='ViewWaterDetails.html'">
								<spring:message code="water.btn.back" text="Back"></spring:message>
							</button>
						</div>
					</c:if>

				</form:form>
			</div>

		</div>
	</div>
</div>