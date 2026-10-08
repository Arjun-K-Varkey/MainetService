<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript"
	src="js/trade_license/tradeLicenseReportFormat.js"></script>


<style>
#receipt img {
	width: 150px;
	height: 110px;
}
/* hr{
	border-top: 1px solid #000;
} */
.invoice .widget-content.padding {
	padding: 20px;
}

.border-black {
	border: 1px solid #000;
	padding: 10px;
	min-height: 130px;
}

.padding-left-50 {
	padding-left: 50px !important;
}

.no-arrow {
	-webkit-appearance: none;
}

.no-arrow::-ms-expand {
	display: none;
}

table, td, th {
	border: 1px solid #595959;
	border-collapse: collapse;
}

td, th {
	padding: 3px;
	width: 600px;
	height: 25px;
}

th {
	background: #f0e6cc;
}

.even {
	background: #fbf8f0;
}

.odd {
	background: #fefcf9;
}

.display-block {
	display: block;
}

.trade-license-cert .trade-license-cert-content {
	border: 1px solid #000;
	padding: 1rem;
}

.trade-license-cert .widget-content .form-horizontal h4 {
	background: transparent;
	border-left: none;
	padding: 0rem;
}

.trade-license-cert .widget-content .form-horizontal p {
	font-size: 0.9rem;
}

.trade-license-cert .widget-content .form-horizontal p.tlc-label {
	font-weight: 600;
	position: relative;
}

.trade-license-cert .widget-content .form-horizontal p.tlc-label::after
	{
	content: '\003A';
	position: absolute;
	right: 0;
}
</style>

<!-- Start Content here -->
<div class="content animated slideInDown">
	<div class="trade-license-cert">
		<div class="widget invoice" id="receipt">
			<div class="widget-content padding">
				<div class="trade-license-cert-content">
					<form:form action="LicenseGeneration.html" class="form-horizontal"
						name="tradeLicenseReportPrinting" id="tradeLicenseReportPrinting">
						<c:if test="${not empty command.imagePath}">
							<form:hidden path="" id="imgMode" value="${command.imagePath}" />
						</c:if>
					<%-- 	<div class="row">
							<div class="col-xs-3 col-sm-3">
								<img src="${userSession.orgLogoPath}" width="80">
							</div>
							<div class="col-xs-6 col-sm-6 text-center">
								<h3 class="margin-bottom-0">
									<spring:message code=""
										text="${userSession.organisation.oNlsOrgnameMar}" />
								</h3>
								<h3 class="text-bold text-center margin-top-20">
									<spring:message code="license.certi.title" text="${command.department.dpNameMar}" />
								</h3>
							</div> --%>
							<%-- <p>
							<spring:message code=""
								text="${userSession.organisation.orgAddress}" />

						</p> --%>
						<!-- </div> -->
						<spring:eval
							expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getHierarchicalLookUp(${command.tradeDetailDTO.trdWard2},${command.tradeDetailDTO.orgid})"
							var="ward" />
						<div class="row margin-top-20 clear">
							<p class="col-xs-2 col-sm-1 tlc-label">
								<spring:message code="license.cert.lic.no" text="License Number" />
							</p>
							<p class="col-xs-3 col-sm-2">
								<b>${command.tradeDetailDTO.trdLicno}</b>
							</p>
							<p class="col-xs-2 col-sm-1 tlc-label">
								<spring:message code="license.cert.ward" text="Ward" />
							</p>
							<p class="col-xs-2 col-sm-2">
								<b>${ward.descLangSecond}</b>
							</p>
							<p class="col-xs-1 col-sm-1 tlc-label">
								<spring:message code="lisence.cert.date" text="Date" />
							</p>
							<p class="col-xs-1 col-sm-2">
								<b><fmt:formatDate value="<%=new java.util.Date()%>"
									pattern="dd/MM/yyyy" /></b>
							</p>
						</div>
						<br>
						<br>
						<p class="text margin-top-10">
							<spring:message code="license.cert.description"
								text="Under Sections 376 and 383, 381 (b) of the Maharashtra Provincial Corporation Act, 1949, licenses are being issued for specific items / stocks up to the date given on the previous terms and conditions." />
						</p>
						<p class="text-center">
							<b><spring:message code="license.cert.no.refund"
									text="(No license fee refund)" /><b>
						</p>
						<br>
						<br>
						<div class="row margin-top-10 clear">
							<c:set var="d" value="0" scope="page"></c:set>
							<p class="col-xs-3 col-sm-3 tlc-label">
								<spring:message code="owner.cert.name" text="Owner Name" />
							</p>
							<c:forEach
								items="${command.tradeDetailDTO.tradeLicenseOwnerdetailDTO}"
								var="ownerDTO" varStatus="loop">

								<c:if test="${ownerDTO.troPr eq 'A'}">
									<c:if test="${d lt 3}">
										<c:if test="${d eq 0}">&nbsp;&nbsp;&nbsp;</c:if>
										<c:if test="${d ne 0}">,&nbsp;</c:if>

										<b>${ownerDTO.troName}</b>

										<%-- <p class="col-sm-2">
											<!-- Defect #99393 -->
											Father/Husband Name : 
										</p>
										<p class="col-sm-4">
											<b>${ownerDTO.troMname}</b>
										</p> --%>
									</c:if>
									<c:set var="d" value="${d + 1}" scope="page" />
								</c:if>
							</c:forEach>
						</div>

						<%-- <div class="row margin-top-5">
							<p class="col-xs-3 col-sm-3 tlc-label">
								<spring:message code="license.cert.businessName"
									text="Business Name" />
							</p>
							<p class="col-xs-4 col-sm-4">
								<b>${command.tradeDetailDTO.trdBusnm}</b>
							</p>
						</div> --%>
						<div class="row margin-top-5">
							<p class="col-xs-3 col-sm-3 tlc-label">
								<spring:message code="license.cert.businessAddress"
									text="Address" />
							</p>
							<p class="col-xs-4 col-sm-4">
								<b>${command.tradeDetailDTO.trdBusnm}</b>
							</p><br>
							<p class="col-xs-3 col-sm-3"></p>
							<p class="col-xs-8 col-sm-8"><b>${command.tradeDetailDTO.trdBusadd}</b></p>
							
						</div>
						<br>
						<br>
						<div class="row">
							<div class="padding-horizontal-15">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th rowspan="2"><spring:message code="license.srno"
													text="Sr.No" /></th>
											<%-- <th rowspan="2"><spring:message code="license.category"
													text="Type of item sold / stocked /manufactured" /></th> --%>
											<th rowspan="2"><spring:message
													code="license.subCategory" text="Sub Category" /></th>
											<th colspan="4" align="center"><spring:message
													code="license.perticular" text="Particular" /></th>
											<th rowspan="2"><spring:message code="tla.license.fee"
													text="Fee Rs. P." /></th>

										</tr>
										<tr>
											<th><spring:message code="license.cert.area"
													text="Area Sq. m." /></th>
											<th><spring:message code="license.cert.no" text="No" /></th>
											<th><spring:message code="license.cert.kgs" text="kgs" /></th>
											<th><spring:message code="license.cert.ltrs" text="ltrs" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach
											items="${command.tradeDetailDTO.tradeLicenseItemDetailDTO}"
											var="itemList" varStatus="loop">
                                        <c:if test="${itemList.triStatus ne 'I' && itemList.triStatus ne 'N'}">
											<spring:eval
												expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getHierarchicalLookUp(${itemList.triCod1},${command.tradeDetailDTO.orgid})"
												var="catagory" />
											<spring:eval
												expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getHierarchicalLookUp(${itemList.triCod2},${command.tradeDetailDTO.orgid})"
												var="subCatagory" />
											<spring:eval
												expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getHierarchicalLookUp(${itemList.triCod4},${command.tradeDetailDTO.orgid})"
												var="unit" />
											<tr>
												<td>${loop.count}</td>
												<%-- <td>${catagory.lookUpDesc }</td> --%>
												<td>${subCatagory.descLangSecond }</td>
												<td><c:if
														test="${not empty unit.otherField && unit.otherField eq 'SM'}">
													${itemList.trdUnit}
												</c:if></td>
												<td><c:if
														test="${not empty unit.otherField && unit.otherField eq 'STD'}">
													${itemList.trdUnit}
												</c:if></td>
												<td><c:if
														test="${not empty unit.otherField && unit.otherField eq 'KGS'}">
													${itemList.trdUnit}
												</c:if></td>
												<td><c:if
														test="${not empty unit.otherField && unit.otherField eq 'LTR'}">
													${itemList.trdUnit}
												</c:if></td>

												<c:if test="${loop.count ==1 }">
													<td
														rowspan="${command.tradeDetailDTO.tradeLicenseItemDetailDTO.size()}"
														class="vertical-align-middle">${command.rmAmount}</td>
												</c:if>
											</tr>
										</c:if>
										</c:forEach>
										<tr>
												<td colspan="6" style="text-align:right;">
													<p>
													<spring:message code="license.total" text="Total" />
													</p>
												</td>
												<td colspan="2">
													<p>${command.rmAmount}</p>
												</td>
												
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<br>
						<div class="row margin-top-5">
							<p class="col-xs-2 col-sm-2 tlc-label">
								<spring:message code="license.security.fee" text="Security Deposit Rs. P." />
							</p>
							<p class="col-xs-2 col-sm-2">${itemList.depositAmt}</p>
						</div>
						<%-- <div class="row margin-top-5">
							<p
								class="col-xs-offset-8 col-sm-offset-8 col-xs-2 col-sm-2 tlc-label">
								<spring:message code="license.total" text="Total" />
							</p>
							<p class="col-xs-2 col-sm-2">${command.rmAmount}</p>
						</div> --%>
						<div class="row margin-top-5">
							<%-- <p class="col-xs-2 col-sm-2 tlc-label">
								<spring:message code="license.security.deposit"
									text="Security Deposit" />
							</p>
							<p class="col-xs-2 col-sm-2">${command.chargeDescAndAmount.get('Security Deposit')}</p> --%>
							<p class="col-xs-2 col-sm-2 tlc-label">
								<spring:message code="tla.license.cert.total"
									text="Total License Fee" />
							</p>
							<p class="col-xs-2 col-sm-2">${command.rmAmount}</p>
							
							<p class="col-xs-2 col-sm-2 tlc-label">
								<spring:message code="license.reciept.no" text="Receipt No" />
							</p>
							<p class="col-xs-1 col-sm-1">${command.rmRcptno}</p>
							<p class="col-xs-1 col-sm-1 tlc-label">
								<spring:message code="lisence.cert.date" text="Date" />
							</p>
							<p class="col-xs-2 col-sm-2">${command.loiDateDesc}</p>
						</div>
						<div class="row margin-top-5">
							<p class="col-xs-2 col-sm-2 tlc-label">
								<spring:message code="lisence.cert.date" text="Date" />
							</p>
							<p class="col-xs-2 col-sm-2">
								<fmt:formatDate value="<%=new java.util.Date()%>"
									pattern="dd/MM/yyyy" />
							</p>
							<p class="col-xs-2 col-sm-2"></p>
							<p class="col-xs-1 col-sm-1"></p>
							
							<p class="col-xs-1 col-sm-1 tlc-label">
								<spring:message code="license.cert.from.date" text="From Date" /></p>
							<p class="col-xs-4 col-sm-4"><span class="margin-right-10">${command.licenseFromDateDesc}</span>
								<span class="margin-right-10"><spring:message code="trade.cert.to" text="to"/></span>
								<span class="margin-right-10">${command.tradeDetailDTO.licenseDateDesc}</span>
								<span><spring:message code="license.cert.toDate" text="Till Date" /></span>
							</p>
						</div>
						
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<div class="row padding-top-30 margin-top-30">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="pull-right">
									<p align="center" class="display-block text-bold">
										<spring:message code="license.deputy.commisoner.name"
											text="Deputy Commissioner" />
									</p>
									<p align="center" class="display-block text-bold">
										<%-- <spring:message code="" text="License" /> --%>
										<spring:message code="license.or" text="" />
									</p>
									<p  align="center" class="display-block text-bold">
										<%-- <spring:message code="" text="License" /> --%>
										<spring:message code="license.deputy.dept.name" text="" />
									</p>
									<p class="display-block text-bold">
										<spring:message code="license.deputy.org.name" text="" />
									</p>
								</div>
							</div>
						</div>
						<br>
						<br>
						<br>
						<div class="row">
							<div class="padding-horizontal-15">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th colspan="9"><spring:message
													code="licence.renewal.details"
													text="License Renewal Details" /></th>
										</tr>
										<tr>
											<th rowspan="2"><spring:message
													code="licence.renewal.srNo" text="Sr no" /></th>
											<th rowspan="2"><spring:message
													code="licence.renewal.rcpt.no"
													text="Renewal Fee Receipt No" /></th>
											<th rowspan="2"><spring:message
													code="licence.renewal.date" text="Date" /></th>
											<th rowspan="2"><spring:message
													code="licence.renewal.fee" text="License Fee" /></th>
											<th rowspan="2"><spring:message
													code="licence.renewal.late.fee" text="Late Fee" /></th>
											<th rowspan="2"><spring:message
													code="licence.renewal.totalfee" text="Total" /></th>
											<th colspan="2"><spring:message
													code="licence.renewal.validity" text="License Validity" /></th>
											<%-- <th rowspan="2"><spring:message
													code="licence.renewal.officer.sign"
													text="Officer's sign and stamp" /></th> --%>
										</tr>
										<tr>
											<th><spring:message code="licence.renewal.from.date"
													text="From" /></th>
											<th><spring:message code="licence.renewal.to.date"
													text="To" /></th>
										</tr>
									</thead>
									<%-- <tbody>
										<c:forEach items="${command.renHistList}" var="itemList"
											varStatus="loop">

											<c:if test="${itemList.serviceShrtCode eq 'RTL' }">

												<tr>
													<td>${loop.count}</td>
													<td>${itemList.rcptNo}</td>
													<td><fmt:formatDate pattern="dd/MM/yyyy"
															value="${itemList.updatedDate}" /></td>
													<td><c:if
															test="${not empty itemList.chargeDesc.get('License Fee')}">
													${itemList.chargeDesc.get('License Fee')}</c:if> <c:if
															test="${not empty itemList.chargeDesc.get('License Fee-N')}">
													${itemList.chargeDesc.get('License Fee-N')}</c:if> <c:if
															test="${not empty itemList.chargeDesc.get('License Durusti Fee')}">
													${itemList.chargeDesc.get('License Durusti Fee')}</c:if> <c:if
															test="${not empty itemList.chargeDesc.get('Application Charge')}">
													${itemList.chargeDesc.get('Application Charge')}</c:if></td>
													<td><c:if
															test="${not empty itemList.chargeDesc.get('Late Fee')}">
													${itemList.chargeDesc.get('Late Fee')}</c:if> <c:if
															test="${not empty itemList.chargeDesc.get('Penalty')}">
													${itemList.chargeDesc.get('Penalty')}</c:if></td>


													</td>
													<td>${itemList.totalAmt}</td>
													<td>${itemList.renewalFromDateDesc}</td>
													<td>${itemList.renewalTodDateDesc}</td>
													<td></td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody> --%>
								</table>
							</div>
						</div>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<br>
						<div class="text-bold text-center padding-vertical-10">
							<spring:message code="trade.term.cond1"
								text="TERMS
							AND CONDITIONS" />
						</div>
						<br>

						<div class="col-sm-10 col-xs-10 text-center ">
							<!-- new -->
							<div class="row margin-top-10 clear text-center">

								<!-- <div class="col-sm-8 col-xs-8 text-left "> -->
								<p>
								<table class="table .table-responsive text-center">

									<tbody>

										<!-- 
											<tr>
												<th width="8%" class="text-center"></th>
												
											</tr> -->


										<c:forEach items="${command.apprejMasList}" var="lookup"
											varStatus="loop">

											<tr>

												<td class="text-left"><b>${loop.index+1}.</b> <c:choose>
														<c:when
															test="${userSession.getCurrent().getLanguageId() eq 1}">
																		 ${lookup.artRemarks }
																	</c:when>
														<c:otherwise>
																		 ${lookup.artRemarksreg }
																	</c:otherwise>
													</c:choose></td>

											</tr>
										</c:forEach>

									</tbody>
								</table>


								<b>${command.issuanceDateDesc}</b>
								</p>
								<!-- </div> -->
							</div>


						</div>
						<div class="row padding-top-30 margin-top-30">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="pull-right">
									<p align="center" class="display-block text-bold">
										<spring:message code="license.deputy.commisoner.name"
											text="Deputy Commissioner" />
									</p>
									<p align="center" class="display-block text-bold">
										<%-- <spring:message code="" text="License" /> --%>
										<spring:message code="license.or" text="" />
									</p>
									<p align="center" class="display-block text-bold">
										<%-- <spring:message code="" text="License" /> --%>
										<spring:message code="license.deputy.dept.name" text="" />
									</p>
									<p class="display-block text-bold">
										<spring:message code="license.deputy.org.name" text="" />
									</p>
								</div>
							</div>
						</div>
						<div class="clear"></div>

					</form:form>
				</div>

				<div class="text-center hidden-print padding-20">
					<button onclick="printdiv('receipt')"
						class="btn btn-primary hidden-print">
						<i class="fa fa-print"></i>
						<spring:message code="trade.print" text="Print" />
					</button>
					<button type="button" class="btn btn-danger backButton" id=""
						onclick="window.location.href ='AdminHome.html',true">
						<spring:message code="trade.back"></spring:message>
					</button>
				</div>


			</div>
		</div>
	</div>
</div>

