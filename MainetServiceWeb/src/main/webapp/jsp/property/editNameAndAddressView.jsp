<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/property/updateDataEntry.js"></script>

<div id="validationDiv">

	<ol class="breadcrumb">
		<li><a href="AdminHome.html"><spring:message code="cfc.home" text="Home"/></a></li>
		<li><a href="javascript:void(0);"><spring:message code="property.property" text="Property tax"/></a></li>
		<li><a href="javascript:void(0);"><spring:message code="cfc.transaction" text="Transactions"/></a></li>
		<li class="active"><spring:message text="Property View" code="propety.Authorization" /></li>
	</ol><br>

	<!-- Start Content here -->
	<div class="AuthContent">
		<!-- Start Main Page Heading -->
		<div class="widget">
			<div class="mand-label clearfix">
				<span><spring:message code="property.Fieldwith" /><i
					class="text-red-1">* </i>
				<spring:message code="property.ismandatory" /> </span>
			</div>
			<div class="widget-header">
				<h2>
					<strong><spring:message code="edit.name.and.address"
							text="Edit Name and Address" /></strong>
				</h2>
				<div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message
									code="property.Help" /></span></i></a>
				</div>
			</div>
			<div class="widget-content padding">
				<form:form action="EditNameAddressAuthorization.html"
					class="form-horizontal form" name="EditNameAddressAuthorization"
					id="EditNameAddressAuthorization">
					<div class="compalint-error-div">
						<jsp:include page="/jsp/tiles/validationerror.jsp" />
						<div
							class="warning-div error-div alert alert-danger alert-dismissible"
							id="errorDiv"></div>
					</div>
					<%-- <form:hidden path="" value="${command.flatNo}" id="flatNoo" />
					<form:hidden path="" value="${command.editType}" id="editType" /> --%>
					<!-- START Before Correction -->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#BeforeCorrection"><spring:message
								text="Before Correction"	code="edit.property.before.correction" /></a>
						</h4>

						<div class="panel-collapse collapse in" id="BeforeCorrection">
						
					<div class="form-group">
						<label class="col-sm-2 control-label" for="ownershiptype"><spring:message
								code="property.ownershiptype" /></label>
						<div class="col-sm-4">
							<form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"
								id="ownershiptype" class="form-control" disabled="true" />
							<form:hidden path="ownershipPrefix" id="ownershipId"
								class="form-control" />
						</div>
						<apptags:input labelCode="propertydetails.PropertyNo."
							path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
					</div>

					<!----------------------------Owner Details------------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
									code="property.Ownerdetail" /></a>
						</h4>

						<div class="panel-collapse collapse in" id="OwnshipDetail">
							<c:set var="e" value="0" scope="page" />
							<c:choose>
								<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
									<table id="singleOwnerTable"
										class="table table-striped table-bordered ">
										<tbody>
											<tr>

												<th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
												<th width="10%"><spring:message	code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="12%"><spring:message	code="ownersdetail.adharno" /></th>
												<th width="10%"><spring:message	code="ownersdetail.pancard" /></th>
											</tr>

											<tr>
												<td><form:input id="assoOwnerName"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control"
														disabled="true" />
												</td>

												<td><form:input id="assoMobileno"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" maxlength="10"
														disabled="true" />
												</td>

												<td><form:input id="eMail"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control preventSpace"
														disabled="true" />
												</td>

												<td><form:input id="assoAddharno"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno"
														class="form-control hasNumber" maxlength="12"
														disabled="true" />
												</td>
												<td class="companyDetails"><form:input id="assoPanno"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control"
														disabled="true" />
												</td>
											</tr>
											<c:set var="e" value="${e + 1}" scope="page" />
										</tbody>
									</table>
								</c:when>

								<c:when test="${command.getOwnershipPrefix() eq 'JO'}">
									<%-- <c:set var="a" value='0'/> --%>

									<table id="jointOwnerTable"
										class="table text-left table-striped table-bordered">
										<tbody>
											<tr>
												<th width="3%"><spring:message code="" text="Sr.No" /></th>
												<th width="14%"><spring:message	code="ownersdetail.ownersname" /></th>
												<th width="11%"><spring:message	code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="12%"><spring:message	code="ownersdetail.adharno" /></th>
												<th width="10%"><spring:message	code="ownersdetail.pancard" /></th>
											</tr>
											<c:forEach var="provisionalAssesmentOwnerDtlDtoList"
												items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
												<%-- <c:set var="e" value="0" scope="page" /> --%>
												<tr>
													<td><form:input path="" id="${e}" value="${e + 1}"
															cssClass=" form-control" disabled="true" />
													</td>
													<td><form:input
															path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoOwnerName"
															id="assoOwnerName${e}"
															cssClass=" form-control"
															disabled="true" />
													</td>
													<td><form:input
															path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoMobileno"
															id="assoMobileno${e}"
															cssClass=" form-control hasNumber"
															disabled="true"
															maxlength="10" /></td>
													<td><form:input
															path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].eMail"
															id="eMail${e}" cssClass=" form-control"
															disabled="true" />
													</td>
													<td><form:input
															path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoAddharno"
															id="assoAddharno${e}"
															cssClass=" form-control hasNumber"
															disabled="false" maxlength="12" /></td>
													<td><form:input
															path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoPanno"
															id="assoPanno${e}"
															cssClass=" form-control"
															disabled="true" />
													</td>
												</tr>
												<c:set var="e" value="${e + 1}" scope="page" />
											</c:forEach>
										</tbody>
									</table>
								</c:when>
								<c:otherwise>
									<table id="ownerTable"
										class="table text-left table-striped table-bordered">

										<tbody>
											<tr>
												<th width="25%"><spring:message code="property.NameOf" />
													${command.getOwnershipTypeValue()}</th>
												<th width="10%"><spring:message
														code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="10%"><spring:message
														code="ownersdetail.pancard" /></th>
											</tr>
											<tr>
												<td><form:input id="assoOwnerName"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control"
														disabled="true" />
												</td>

												<td><form:input id="assoMobileno"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" maxlength="10"
														disabled="true" />
												</td>

												<td><form:input id="eMail"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control preventSpace"
														disabled="true" />
												</td>

												<td class="companyDetails"><form:input id="assoPanno"
														path="oldProvisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control"
														disabled="true" />
												</td>
											</tr>
											<c:set var="e" value="${e + 1}" scope="page" />
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<!---------------------------End Owner Details---------------------------->

					<!-------------------------------------------------  -------------------------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title"
							id="scrollToDiv">
							<a data-toggle="collapse" href="#UnitDetail"><spring:message
									code="property.unitdetails" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="UnitDetail">
							<c:choose>
								<c:when
									test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}">
									<div class="table-responsive">
										<!-- style="overflow:scroll" -->
										<c:set var="d" value="0" scope="page" />
										<table id="unitDetailTable"
											class="table table-striped table-bordered appendableClass unitDetails">
											<tbody id="tableToModify">
												<tr>
													<%-- <th width="10%" class="required-control"><spring:message code="unitdetails.year"/></th> --%>
													<th width="7%" class=""><spring:message	code="unitdetails.unitno" /></th>
													<th width="8%" class=""><spring:message	code="unitdetails.flatNo" /></th>
													<th width="12%" class=""><spring:message code="unitdetails.floorno" /></th>

													<th width="13%" class="" for="occupancy"><spring:message code="unitdetails.occupancy" /></th>
													<th width="15%" class=""><spring:message code="unitdetails.OccupierName" /></th>
													<th class=""><spring:message code="unitdetails.legal.illegal" text="Is Legal/Illegal?" /></th>
													<th width="10%" class=""><spring:message code="unitdetails.constructiontype" /></th>
													<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
														showOnlyLabel="false"
														pathPrefix="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
														isMandatory="true" hasLookupAlphaNumericSort="true"
														hasSubLookupAlphaNumericSort="true"
														cssClass="form-control " showAll="false"
														hasTableForm="true" showData="false" columnWidth="10%" />
													<th width="8%" for="actualRent${status.count-1}"
														class="col-sm-2 "><spring:message code="unitdetails.actualRent" /></th>

													<th width="12%" class=""><spring:message code="unitdetails.carpetArea" text="Carpet Area" /></th>

													<th width="12%" class=""><spring:message code="unitdetails.taxdetail" /></th>

												</tr>

												<c:forEach var="unitDetails"
													items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"
													varStatus="status">

													<tr class="firstUnitRow" id="rowToClone">

														<td><form:input
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo"
																type="text" class="form-control mandColorClass text-center unit "
																id="unitNo${status.count-1}" data-rule-required="true" readonly="true" /></td>

														<c:choose>
															<c:when
																test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList().get(status.count-1).flatNo}">
																<td width=""><form:input
																		path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo"
																		type="text" class="form-control  mandColorClass text-right"
																		id="flatNo${status.count-1}" data-rule-required="true" disabled="true" /></td>
															</c:when>
															<c:otherwise>
																<td width=""><form:input
																		path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo"
																		type="text" class="form-control  mandColorClass text-right"
																		id="flatNo${status.count-1}" data-rule-required="true" /></td>
															</c:otherwise>
														</c:choose>

														<td><c:set var="baseLookupCode" value="IDE" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo"
																cssClass="form-control changeParameterClass mandColorClass floorNoClass"
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectFloorNo"
																isMandatory="true" hasTableForm="true" disabled="true" />

															<c:forEach
																items="${command.getLevelData(baseLookupCode)}"
																var="lookUp">
																<c:if test="${lookUp.lookUpCode eq 'OTH' }">
																	<input type="hidden" value="${lookUp.lookUpId}" id="hiddenOther">
																</c:if>
															</c:forEach></td>
														<td><c:set var="baseLookupCode" value="OCS" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType"
																cssClass="form-control changeParameterClass "
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectSelectOccupancyType"
																isMandatory="true" hasTableForm="true"
																changeHandler="loadOccupierName(this)" disabled="true" />


														</td>
														<td>

															<div class="">
																<form:input
																	path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
																	class="form-control alphaNumeric preventSpace"
																	id="occupierName${status.count-1}" maxlength="500" disabled="true" />
															</div>

														</td>
														<td><form:select class="form-control"
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].legal"
																id="legal${status.count-1}" disabled="true">
																<form:option value="">
																	<spring:message code="" text="Select"></spring:message>
																</form:option>
																<form:option value="Legal"></form:option>
																<form:option value="Illegal"></form:option>
																<form:option value="Illegal(Core)"></form:option>
															</form:select></td>
														<td><c:set var="baseLookupCode" value="CSC" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdConstruType"
																cssClass="form-control changeParameterClass mandColorClass"
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectConstType"
																isMandatory="true" hasTableForm="true" disabled="true" />
														</td>
														<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
															showOnlyLabel="false"
															pathPrefix="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
															isMandatory="true" hasLookupAlphaNumericSort="true"
															hasSubLookupAlphaNumericSort="true"
															cssClass="form-control " showAll="false"
															hasTableForm="true" showData="true" disabled="true" />

														<td><form:input
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
																type="text" class="form-control has2Decimal  text-right"
																id="actualRent${status.count-1}" onkeypress="return hasAmount(event, this, 15, 2)"
																placeholder="0.0" disabled="true" /></td>

														<td><form:input
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea"
																type="text" class="form-control has2Decimal mandColorClass text-right"
																id="carpetArea${status.count-1}" data-rule-required="true"
																onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" /></td>

														<td width="150"><form:input
																path="oldProvisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea"
																type="text" class="form-control has2Decimal mandColorClass text-right"
																id="taxableArea${status.count-1}" data-rule-required="true"
																onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" /></td>
													</tr>

												</c:forEach>
											</tbody>
										</table>
									</div>

								</c:when>

							</c:choose>

						</div>
					</div>

					<!---------------------------Property Address Location-------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyAddress"><spring:message
									code="property.Propertyaddress" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyAddress">

							<div class="form-group">
								<apptags:textArea labelCode="property.propertyaddress"
									path="oldProvisionalAssesmentMstDto.assAddress" maxlegnth="500"
									cssClass="preventSpace" isDisabled="true"></apptags:textArea>
								<label class="col-sm-2 control-label" for="proAssPincode"><spring:message
										code="property.pincode" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control hasPincode"
										path="oldProvisionalAssesmentMstDto.assPincode" id="assPincode"
										maxlength="6" data-rule-digits="true" data-rule-minlength="6"
										data-rule-maxlength="6"
										disabled="true"></form:input>
								</div>
							</div>
							<%-- <div class="form-group">
								<apptags:textArea labelCode="property.landDetails.Remark"
									path="provisionalAssesmentMstDto.remarks" maxlegnth="1000"
									cssClass="preventSpace" isDisabled="true"></apptags:textArea>
							</div> --%>
						</div>
					</div>
				</div>
			</div>
			<!------------------ END Before Correction ---------------------------------->
					
					<!---------------- START After Correction ---------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#AfterCorrection"><spring:message
								text="After Correction"	code="edit.property.after.correction" /></a>
						</h4>

						<div class="panel-collapse collapse in" id="AfterCorrection">
						
					<div class="form-group">
						<label class="col-sm-2 control-label" for="ownershiptype"><spring:message
								code="property.ownershiptype" /></label>
						<div class="col-sm-4">
							<form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"
								id="ownershiptype" class="form-control" disabled="true" />
							<form:hidden path="ownershipPrefix" id="ownershipId"
								class="form-control" />
						</div>
						<apptags:input labelCode="propertydetails.PropertyNo."
							path="provisionalAssesmentMstDto.assNo" isDisabled="true"></apptags:input>
					</div>

					<!----------------------------Owner Details------------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
									code="property.Ownerdetail" /></a>
						</h4>

						<div class="panel-collapse collapse in" id="OwnshipDetail">
							<c:set var="e" value="0" scope="page" />
							<c:choose>
								<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
									<table id="singleOwnerTable"
										class="table table-striped table-bordered ">
										<tbody>
											<tr>

												<th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
												<th width="10%"><spring:message	code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="12%"><spring:message	code="ownersdetail.adharno" /></th>
												<th width="10%"><spring:message	code="ownersdetail.pancard" /></th>
											</tr>

											<tr>
												<td><form:input id="assoOwnerName"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control"
														disabled="true" />
												</td>

												<td><form:input id="assoMobileno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" maxlength="10"
														disabled="true" />
												</td>

												<td><form:input id="eMail"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control preventSpace"
														disabled="true" />
												</td>

												<td><form:input id="assoAddharno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno"
														class="form-control hasNumber" maxlength="12"
														disabled="true" />
												</td>
												<td class="companyDetails"><form:input id="assoPanno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control"
														disabled="true" />
												</td>
											</tr>
											<c:set var="e" value="${e + 1}" scope="page" />
										</tbody>
									</table>
								</c:when>

								<c:when test="${command.getOwnershipPrefix() eq 'JO'}">
									<%-- <c:set var="a" value='0'/> --%>

									<table id="jointOwnerTable"
										class="table text-left table-striped table-bordered">
										<tbody>
											<tr>
												<th width="3%"><spring:message code="" text="Sr.No" /></th>
												<th width="14%"><spring:message	code="ownersdetail.ownersname" /></th>
												<th width="11%"><spring:message	code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="12%"><spring:message	code="ownersdetail.adharno" /></th>
												<th width="10%"><spring:message	code="ownersdetail.pancard" /></th>
											</tr>
											<c:forEach var="provisionalAssesmentOwnerDtlDtoList"
												items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
												<%-- <c:set var="e" value="0" scope="page" /> --%>
												<tr>
													<td><form:input path="" id="${e}" value="${e + 1}"
															cssClass=" form-control" disabled="true" />
													</td>
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoOwnerName"
															id="assoOwnerName${e}"
															cssClass=" form-control"
															disabled="true" />
													</td>
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoMobileno"
															id="assoMobileno${e}"
															cssClass=" form-control hasNumber"
															disabled="true"
															maxlength="10" /></td>
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].eMail"
															id="eMail${e}" cssClass=" form-control"
															disabled="true" />
													</td>
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoAddharno"
															id="assoAddharno${e}"
															cssClass=" form-control hasNumber"
															disabled="false" maxlength="12" /></td>
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[${e}].assoPanno"
															id="assoPanno${e}"
															cssClass=" form-control"
															disabled="true" />
													</td>
												</tr>
												<c:set var="e" value="${e + 1}" scope="page" />
											</c:forEach>
										</tbody>
									</table>
								</c:when>
								<c:otherwise>
									<table id="ownerTable"
										class="table text-left table-striped table-bordered">

										<tbody>
											<tr>
												<th width="25%"><spring:message code="property.NameOf" />
													${command.getOwnershipTypeValue()}</th>
												<th width="10%"><spring:message
														code="ownersdetail.mobileno" /></th>
												<th width="10%"><spring:message code="property.email" /></th>
												<th width="10%"><spring:message
														code="ownersdetail.pancard" /></th>
											</tr>
											<tr>
												<td><form:input id="assoOwnerName"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control"
														disabled="true" />
												</td>

												<td><form:input id="assoMobileno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" maxlength="10"
														disabled="true" />
												</td>

												<td><form:input id="eMail"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control preventSpace"
														disabled="true" />
												</td>

												<td class="companyDetails"><form:input id="assoPanno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control"
														disabled="true" />
												</td>
											</tr>
											<c:set var="e" value="${e + 1}" scope="page" />
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<!---------------------------End Owner Details---------------------------->

					<!-------------------------------------------------  -------------------------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title"
							id="scrollToDiv">
							<a data-toggle="collapse" href="#UnitDetail"><spring:message
									code="property.unitdetails" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="UnitDetail">
							<c:choose>
								<c:when
									test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}">
									<div class="table-responsive">
										<!-- style="overflow:scroll" -->
										<c:set var="d" value="0" scope="page" />
										<table id="unitDetailTable"
											class="table table-striped table-bordered appendableClass unitDetails">
											<tbody id="tableToModify">
												<tr>
													<%-- <th width="10%" class="required-control"><spring:message code="unitdetails.year"/></th> --%>
													<th width="7%" class=""><spring:message	code="unitdetails.unitno" /></th>
													<th width="8%" class=""><spring:message	code="unitdetails.flatNo" /></th>
													<th width="12%" class=""><spring:message code="unitdetails.floorno" /></th>

													<th width="13%" class="" for="occupancy"><spring:message code="unitdetails.occupancy" /></th>
													<th width="15%" class=""><spring:message code="unitdetails.OccupierName" /></th>
													<th class=""><spring:message code="unitdetails.legal.illegal" text="Is Legal/Illegal?" /></th>
													<th width="10%" class=""><spring:message code="unitdetails.constructiontype" /></th>
													<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
														showOnlyLabel="false"
														pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
														isMandatory="true" hasLookupAlphaNumericSort="true"
														hasSubLookupAlphaNumericSort="true"
														cssClass="form-control " showAll="false"
														hasTableForm="true" showData="false" columnWidth="10%" />
													<th width="8%" for="actualRent${status.count-1}"
														class="col-sm-2 "><spring:message code="unitdetails.actualRent" /></th>

													<th width="12%" class=""><spring:message code="unitdetails.carpetArea" text="Carpet Area" /></th>

													<th width="12%" class=""><spring:message code="unitdetails.taxdetail" /></th>

												</tr>

												<c:forEach var="unitDetails"
													items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"
													varStatus="status">

													<tr class="firstUnitRow" id="rowToClone">

														<td><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo"
																type="text" class="form-control mandColorClass text-center unit "
																id="unitNo${status.count-1}" data-rule-required="true" readonly="true" /></td>

														<c:choose>
															<c:when
																test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList().get(status.count-1).flatNo}">
																<td width=""><form:input
																		path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo"
																		type="text" class="form-control  mandColorClass text-right"
																		id="flatNo${status.count-1}" data-rule-required="true" disabled="true" /></td>
															</c:when>
															<c:otherwise>
																<td width=""><form:input
																		path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo"
																		type="text" class="form-control  mandColorClass text-right"
																		id="flatNo${status.count-1}" data-rule-required="true" /></td>
															</c:otherwise>
														</c:choose>

														<td><c:set var="baseLookupCode" value="IDE" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo"
																cssClass="form-control changeParameterClass mandColorClass floorNoClass"
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectFloorNo"
																isMandatory="true" hasTableForm="true" disabled="true" />

															<c:forEach
																items="${command.getLevelData(baseLookupCode)}"
																var="lookUp">
																<c:if test="${lookUp.lookUpCode eq 'OTH' }">
																	<input type="hidden" value="${lookUp.lookUpId}" id="hiddenOther">
																</c:if>
															</c:forEach></td>
														<td><c:set var="baseLookupCode" value="OCS" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType"
																cssClass="form-control changeParameterClass "
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectSelectOccupancyType"
																isMandatory="true" hasTableForm="true"
																changeHandler="loadOccupierName(this)" disabled="true" />


														</td>
														<td>

															<div class="">
																<form:input
																	path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"
																	class="form-control alphaNumeric preventSpace"
																	id="occupierName${status.count-1}" maxlength="500" disabled="true" />
															</div>

														</td>
														<td><form:select class="form-control"
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].legal"
																id="legal${status.count-1}" disabled="true">
																<form:option value="">
																	<spring:message code="" text="Select"></spring:message>
																</form:option>
																<form:option value="Legal"></form:option>
																<form:option value="Illegal"></form:option>
																<form:option value="Illegal(Core)"></form:option>
															</form:select></td>
														<td><c:set var="baseLookupCode" value="CSC" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdConstruType"
																cssClass="form-control changeParameterClass mandColorClass"
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="prop.selectConstType"
																isMandatory="true" hasTableForm="true" disabled="true" />
														</td>
														<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
															showOnlyLabel="false"
															pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
															isMandatory="true" hasLookupAlphaNumericSort="true"
															hasSubLookupAlphaNumericSort="true"
															cssClass="form-control " showAll="false"
															hasTableForm="true" showData="true" disabled="true" />

														<td><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
																type="text" class="form-control has2Decimal  text-right"
																id="actualRent${status.count-1}" onkeypress="return hasAmount(event, this, 15, 2)"
																placeholder="0.0" disabled="true" /></td>

														<td><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea"
																type="text" class="form-control has2Decimal mandColorClass text-right"
																id="carpetArea${status.count-1}" data-rule-required="true"
																onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" /></td>

														<td width="150"><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea"
																type="text" class="form-control has2Decimal mandColorClass text-right"
																id="taxableArea${status.count-1}" data-rule-required="true"
																onkeypress="return hasAmount(event, this, 15, 2)" disabled="true" /></td>
													</tr>

												</c:forEach>
											</tbody>
										</table>
									</div>

								</c:when>

							</c:choose>

						</div>
					</div>

					<!---------------------------Property Address Location-------------------------->
					<div class="accordion-toggle">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#propertyAddress"><spring:message
									code="property.Propertyaddress" /></a>
						</h4>
						<div class="panel-collapse collapse in" id="propertyAddress">

							<div class="form-group">
								<apptags:textArea labelCode="property.propertyaddress"
									path="provisionalAssesmentMstDto.assAddress" maxlegnth="500"
									cssClass="preventSpace" isDisabled="true"></apptags:textArea>
								<label class="col-sm-2 control-label" for="proAssPincode"><spring:message
										code="property.pincode" /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control hasPincode"
										path="provisionalAssesmentMstDto.assPincode" id="assPincode"
										maxlength="6" data-rule-digits="true" data-rule-minlength="6"
										data-rule-maxlength="6"
										disabled="true"></form:input>
								</div>
							</div>
							<div class="form-group">
								<apptags:textArea labelCode="property.landDetails.Remark"
									path="provisionalAssesmentMstDto.remarks" maxlegnth="1000"
									cssClass="preventSpace" isDisabled="true"></apptags:textArea>
							</div>
						</div>
					</div>
				</div>
			</div>
					<!-------------------------End Property Address Location------------------------>
			<!------------------------ END After Correction -------------------------------->

<div>
<jsp:include page="/jsp/cfc/checklist/ChecklistVerificationApproval.jsp"/>
</div>

<c:if test="${command.isHideUserAction() eq false }">
	<c:choose>
		<c:when test="${command.isLastChecker() ne true}">
			<apptags:CheckerAction hideSendback="true"></apptags:CheckerAction>
		</c:when>
		<c:otherwise>
			<apptags:CheckerAction></apptags:CheckerAction>
		</c:otherwise>
	</c:choose>
</c:if>
					<!--Search Button-->
					<div class="text-center padding-15 clear">
					<c:if test="${command.isHideUserAction() eq false }">
						<button type="button" class="btn btn-success btn-submit"
							onclick="savePropertyFrom(this)" id="submit">
							<spring:message code="property.btn.submit" text="Submit" />
						</button>
					</c:if>
						<button type="button" class="btn btn-danger"
							data-original-title="Back"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="property.Back" text="Back"></spring:message>
						</button>
					</div>
				</form:form>
				<!-- End Form -->
			</div>
			<!-- End Widget Content here -->
		</div>
		<!-- End Widget  here -->
	</div>
	<!-- End of Content -->
</div>

