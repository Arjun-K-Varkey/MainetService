<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
 <script type="text/javascript" src="js/mainet/file-upload.js"></script>
 <script type="text/javascript" src="js/water/waterBillPayment.js"></script>
 <link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>

</script>
 <div id="validationDiv">
<apptags:breadcrumb></apptags:breadcrumb>
<!-- Start Content here -->
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="water.bill.collection" />
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i></a>
			</div>
		</div>
		<div class="widget-content padding">
		<div class="error-div alert alert-danger alert-dismissible" id="errorDivId" style="display: none;">
				<button type="button" class="close" onclick="closeOutErrBox()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	             	</div>
			<form:form action="WaterBillPayment.html" method="post"
				class="form-horizontal" name="WaterBillPayment"
				id="WaterBillPaymentId">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<form:hidden path="" id="counterScheduleFlag" value="${command.counterScheduleFlag}" />
				<form:hidden path="" id="serviceCode" value="BPW" />
       			<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="water.nodues.connectionNo" /></label>
					<div class="col-sm-4">
						<form:input path="ccnNumber" cssClass="form-control" id="connum" readonly="${command.showMode eq 'Y' ? true : false }"/>
					</div>
					<c:if test="${command.receiptType eq 'M' && command.showMode eq 'N'}">
						<apptags:date labelCode="water.nodues.manual.receipt.date"
									datePath="manualReceiptdate" fieldclass="datepicker"
									isMandatory="true"></apptags:date>
					</c:if>
					
					<c:if test="${command.receiptType ne 'M'}">
					<div class="col-sm-6">
						<button type="button" class="btn btn-success"
							onclick="return serachWaterBillPaymentData(this);"  id="search_1">
							<i class="fa fa-search"></i>
							<spring:message code="water.search" />
						</button>
						
						<button type="button" class="btn btn-warning"
							onclick="resetbillPayment(this)"  id="resetbt">
							<spring:message code="water.btn.reset" />
						</button>
						
						<button type="button" class="btn btn-danger"
							onclick="window.location.href ='AdminHome.html'"  id="backbt">
							<spring:message code="water.btn.back" />
						</button>
						
					</div>
					
					</c:if>

				</div>
				
				<c:if test="${command.receiptType eq 'M'}">
					<div class="text-center padding-top-10">
					<c:if test="${command.showMode eq 'N'}">
						<button type="button" class="btn btn-success"
							onclick="return serachWaterBillPaymentData(this);" id="search_2">
							<i class="fa fa-search"></i>
							<spring:message code="water.search" />
						</button>
							<button type="button" class="btn btn-warning"
							onclick="resetManualForm(this)"  id="resetbt">
							<spring:message code="water.btn.reset" />
						</button>
						
						<button type="button" class="btn btn-danger"
							onclick="window.location.href ='AdminHome.html'"  id="backbt">
							<spring:message code="water.btn.back" />
						</button>
						</c:if>
					</div>
				</c:if>

				<div class="panel-group accordion-toggle"
					id="#accordion_single_collapse">
					<form:hidden path="" value="${command.billMas.csIdn}" id="bill_csidn" />
					<form:hidden path="" value="${command.message}" id="messge" />
					<c:if
						test="${(command.billMas.csIdn ne null) or (not empty command.message && command.message ne null)}">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse"
										href="#ConnectionDetails"><spring:message code="water.billPayment.conDet" text="Connection Details" /></a>
								</h4>
							</div>

							<div id="#ConnectionDetails" class="panel-collapse collapse in">
								<div class="panel-body">

									<div class="form-group">
										<label class="col-sm-2 control-label" for="CsName"> <spring:message
												code="water.bill.payment.consumer.name" text="Consumer Name" />
										</label>
										<div class="col-sm-4">
											<c:if
												test="${ not empty command.applicantDetailDto.applicantMiddleName}">
												<form:input type="text" class="form-control" path=""
													value="${command.applicantDetailDto.applicantFirstName} ${command.applicantDetailDto.applicantMiddleName} ${command.applicantDetailDto.applicantLastName}"
													readonly="true"></form:input>
											</c:if>
											<c:if
												test="${empty command.applicantDetailDto.applicantMiddleName}">
												<form:input type="text" class="form-control" path=""
													value="${command.applicantDetailDto.applicantFirstName} ${command.applicantDetailDto.applicantLastName}"
													readonly="true"></form:input>
											</c:if>
										</div>
										
											<%-- <label class="col-sm-2 control-label"> <spring:message
													code="" text="Application No." />
											</label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="apmApplicationId" readonly="true"></form:input>
											</div> --%>
											<label class="col-sm-2 control-label" for="billingFlatNo">
												<spring:message code="water.flatOrbuildingNo" />
											</label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path="" id="billingFlatNo" value="${command.applicantDetailDto.flatBuildingNo}"
													readonly="true"></form:input>
											</div>
											
									</div>
										<div class="form-group">
											
											<label class="col-sm-2 control-label"> <spring:message
													code="water.buildingName" />
											</label>
											<div class="col-sm-4">
												<form:input type="text" class="form-control"
													path=""
													id="billingBuildingName" value="${command.applicantDetailDto.buildingName}" readonly="true"></form:input>
											</div>
											
											<label class="col-sm-2 control-label"> <spring:message
													code="water.roadName" />
											</label>
											<div class="col-sm-4">
												<form:input name="" type="text" class="form-control"
													path="" value="${command.applicantDetailDto.roadName}" id="billingRoadName"
													readonly="true"></form:input>
											</div>
											
										</div>
								

									
									<div class="form-group">
											
										<label class="col-sm-2 control-label "> <spring:message
												code="water.areaName" />
										</label>
										<div class="col-sm-4">
											<form:input name="" type="text" class="form-control"
												path="applicantDetailDto.areaName" value="${command.applicantDetailDto.areaName}" id="billingAreaName"
												readonly="true"></form:input>
										</div>
										<apptags:input labelCode="water.billPayment.mobileNo" path="applicantDetailDto.mobileNo" cssClass="hasMobileNo mandColorClass hasNumber" maxlegnth="10"></apptags:input>
						
									</div>
													
					<div class="form-group" >		
						<apptags:input labelCode="water.billPayment.emailId" path="applicantDetailDto.emailId" cssClass="mandColorClass" maxlegnth="20"></apptags:input>	
						<apptags:input labelCode="water.billPayment.payeeName" path="payeeName" cssClass="mandColorClass" maxlegnth="99"></apptags:input>	
					</div>
					

								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" class=""
										data-parent="#accordion_single_collapse"
										href="#PayableAmountDetails"><spring:message code="water.billPayment.amt" text="Receipt Amount Details"/></a>
								</h4>
							</div>

							<div id="PayableAmountDetails" class="panel-collapse collapse in">
								<div class="panel-body">


									<c:if
										test="${command.taxes ne null && not empty command.taxes}">
										<%-- <div class="table-responsive margin-top-10">
			<table class="table table-bordered table-condensed table-striped">
				<tr>
					<th width="100">Sr. No.</th>
					<th>Tax Name</th>
					<th>Tax Amount</th>
				</tr>
				<c:forEach items="${command.taxes}" var="data" varStatus="status">
					<tr>
						<td><c:out value="${status.index+1}"></c:out></td>
						<td><c:out value="${data.key}"></c:out></td>
						<td><c:out value="${data.value}"></c:out></td>
					</tr>
				</c:forEach>
			</table>
		</div> --%>
										<div class="table-responsive">
											<table class="table table-bordered table-condensed">

												<tr>
													<th><spring:message
															code="water.waterBillGeneration.TaxDescription" /></th>
													<th><spring:message
															code="water.waterBillGeneration.BalanceArrears" /></th>
													<th><spring:message
															code="water.waterBillGeneration.CurrentTax" /></th>
													<%-- <th><p class="text-right">
															<spring:message
																code="water.waterBillGeneration.CurrentBalanceTax" />
														</p></th> --%>
													<th><p class="text-right">
															<spring:message
																code="water.waterBillGeneration.TotalBalance" />
														</p></th>
												</tr>
												<c:forEach items="${command.taxes}" var="billDet"
													varStatus="detStatus">
													<tr>
														<td><p class="text-right format">${billDet.taxdescription}</p></td>
															<fmt:formatNumber type="number"
																value="${billDet.arrearTaxAmount}" groupingUsed="false"
																maxFractionDigits="2" minFractionDigits="2"
																var="arrearTaxAmount" />
															<td><p class="text-right format">${arrearTaxAmount}</p></td>
															<fmt:formatNumber type="number"
																value="${billDet.taxAmount}" groupingUsed="false"
																maxFractionDigits="2" minFractionDigits="2"
																var="taxAmount" />
														<td><p class="text-right format">${billDet.taxAmount}</p></td>
														<%-- <fmt:formatNumber type="number"
																value="${billDet.balabceTaxAmount}" groupingUsed="false"
																maxFractionDigits="2" minFractionDigits="2"
																var="balabceTaxAmount" />
														<td><p class="text-right">${balabceTaxAmount}</p></td> --%>
														<fmt:formatNumber type="number"
																value="${billDet.total}" groupingUsed="false"
																maxFractionDigits="2" minFractionDigits="2"
																var="total" />
														<td><p class="text-right format">${total}</p></td>
													</tr>
												</c:forEach>
												<tr>
													<th colspan="3"><p class="text-right">
															<spring:message
																code="water.waterBillGeneration.AdjustmentEntry" />
														</p></th>
													<td colspan="2"><p class="text-right format">
																<fmt:formatNumber type="number"
																	value="${command.adjustmentEntry}" groupingUsed="false"
																	maxFractionDigits="2" minFractionDigits="2"
																	var="adjustmentEntry" />
																	${adjustmentEntry}
																
															</p></td>
												</tr>
												<tr>
													<th colspan="3"><p class="text-right format">
															<spring:message
																code="water.waterBillGeneration.AdjustedAmount" />
														</p></th>
														<td colspan="2"><p class="text-right">
																<fmt:formatNumber type="number"
																	value="${command.excessAmount}" groupingUsed="false"
																	maxFractionDigits="2" minFractionDigits="2"
																	var="excessAmount" />
																${excessAmount}
															</p></td>
													</tr>
												<tr>
													<th colspan="3"><p class="text-right">
															<spring:message
																code="water.waterBillGeneration.BalanceExcessAmount" />
														</p></th>
														<td colspan="2"><p class="text-right format">
																<fmt:formatNumber type="number"
																	value="${command.balanceExcessAmount}"
																	groupingUsed="false" maxFractionDigits="2"
																	minFractionDigits="2" var="balanceExcessAmount" />
																${balanceExcessAmount}
															</p></td>
													</tr>
												<tr>
													<%-- <th colspan="3"><p class="text-right">
															<spring:message
																code="water.waterBillGeneration.RebateAmount" />
														</p></th>
														<td colspan="2"><p class="text-right">
																<fmt:formatNumber type="number"
																	value="${command.rebateAmount}" groupingUsed="false"
																	maxFractionDigits="2" minFractionDigits="2"
																	var="rebateAmount" />
																${rebateAmount}
															</p></td> --%>
													</tr>
													<tr>
													<table class="table table-bordered table-condensed">
													<th class="text-right"><spring:message code="water.nodues.total.penality" text="Total Penalty"/></th>
														<th class="text-right format"><fmt:formatNumber type="number"
																value="${command.curSurchargeAmount}" groupingUsed="false" maxFractionDigits="2"
																minFractionDigits="2" var="currentSurAmount" /> ${currentSurAmount}</th>
																
													<c:if test="${command.isAbhayYojanaApplicable() eq true}">	
														
											               <th class="text-right format"><spring:message code="water.abhay.yojana" text="Abhay Yojana Rebate"/></th>
											              <fmt:formatNumber type="number" value="${command.penaltyDiscount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="penaltyDiscount"/> 
											               <th class="text-right">${penaltyDiscount}</th>
											           	
										            </c:if>   
							            			
														<th class="text-right"><spring:message code="water.nodues.total.rebate" text="Total Rebate"/></th>
													<th class="text-right format"><fmt:formatNumber type="number"
																	value="${command.rebateAmount}" groupingUsed="false"
																	maxFractionDigits="2" minFractionDigits="2"
																	var="rebateAmount" /> ${rebateAmount}</th>
													</table>				
												</tr>
													
											</table>
										</div>

									</c:if>

									<c:if test="${command.rebateMessage eq 'Y'}">
										<div class="form-group">
											<label class="col-sm-10 control-label text-red"><spring:message
													code="water.rebate.message" /></label>
										</div>
									</c:if>
									<div class="form-group padding-top-10">
										<label class="col-sm-2 control-label"><spring:message
												code="water.waterBillGeneration.TotalReceivable" /></label>
										<div class="col-sm-4">
											<div class="input-group">
												<form:input path="billMas.bmTotalOutstanding"
													cssClass="form-control text-right format" id="totalPayable"
													readonly="true" />
												<label class="input-group-addon"><i
													class="fa fa-inr"></i></label>
											</div>
										</div>
										<label class="col-sm-2 control-label"><spring:message
												code="water.waterBillGeneration.EnterReceiptAmount" /></label>
										<div class="col-sm-4">
											<div class="input-group">
												<form:input path="payAmount"
													cssClass="form-control mandColorClass text-right"
													maxlength="10" id="payAmount" onblur="payAmoutFun()"
													 />
												<label class="input-group-addon"><i
													class="fa fa-inr"></i></label>
											</div>
										</div>
									</div>
					
									
									<c:if
										test="${not empty command.message && command.message ne null }">
										<div class="form-group">
											<label class="col-sm-10 control-label text-red">${command.message}</label>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					<form:hidden path="" id="validationFlag" value="${command.receiptType}" />	
						<c:if test="${command.receiptType eq 'M'}">
<div class="form-group padding-top-10" >	
			<apptags:input labelCode="water.nodues.manual.receipt.no" isMandatory="true" path="manualReceiptNo"></apptags:input>
			
			<%-- <apptags:date labelCode="Manual Receipt Date"
									datePath="manualReceiptdate" 
									fieldclass="trimDateTime" isMandatory="true" readonly="true"></apptags:date> --%>



<apptags:input labelCode="water.nodues.manual.receipt.date" path="manualReceiptDateString" isReadonly="true"></apptags:input>

								<%-- <apptags:date labelCode="Manual Receipt Date"
									datePath="manualReceiptdate" fieldclass="datepicker"
									isMandatory="true" readonly="true"></apptags:date> --%>
							</div>
						<%-- 	<div class="form-group padding-top-10">
								<label class="col-sm-2 control-label "> <spring:message
										code="" text="Add/Update Mobile No." />
								</label>
								<div class="col-sm-4">
									<form:input name="" type="text" class="form-control"
										path="applicantDetailDto.mobileNo" id="mobileNo" maxlength="10"/>
								</div>
							</div> --%>
							
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
																			hasId="true" fieldPath="checkList"
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
						<div class="panel panel-default">
							<jsp:include page="/jsp/cfc/Challan/offlinePay.jsp" />
						</div>
				
					<%-- <div class="panel-group accordion-toggle" id="accordion_single_collapse">
						<jsp:include page="/jsp/cfc/Challan/offlinePayForMultiMode.jsp"/>
					</div> --%>
	            	<div class="text-center padding-top-10">
							<button type="button" class="btn btn-success btn-submit"
								onclick="saveData(this)">
								<spring:message code="water.btn.submit" />
							</button>
							<button type="button" class="btn btn-danger"
								onclick="window.location.href='AdminHome.html'">
								<spring:message code="water.btn.back" />
							</button>
							<c:if test="${command.receiptType eq 'M' && command.showMode eq 'Y'}">
							<button type="button" class="btn btn-warning"
							onclick="resetCommonForm()">
							<spring:message code="water.btn.reset" />
						</button>
						</c:if>
						
						</div>
					</c:if>
				</div>
			</form:form>
		</div>
	</div>
</div>
</div>
