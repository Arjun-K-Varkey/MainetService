
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
<script type="text/javascript" src="js/property/billingMethodChange.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>
<style>
@media print {
  /* Hide everything by default */
  body * {
    visibility: hidden !important;
  }

  /* Show only the print section */
  #printapplication, #printapplication * {
    visibility: visible !important;
  }

  /* Position the print section at the top-left of the page */
  #printapplication {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
  }

  /* Optionally hide browser's default headers/footers */
  @page {
    margin: 0;
  }
}

</style>

<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="property.billMethodChange"
						text="Bill Method Change" /></strong>
			</h2>
		</div>

		<div class="widget-content padding">
		<div id="printapplication">
			<div class="mand-label clearfix">
				<span><spring:message code="property.billMethodChange"
						text="Bill Method Change" /> </span>
			</div>

			<form:form action="BillingMethodChange.html"
				class="form-horizontal form" name="BillingMethodChangeEdit"
				id="BillingMethodChangeEdit">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

				<form:hidden path="billMethodCode" id="billMethodCode" />
                <form:hidden path="draftFlag" id="draftFlag"/>
                <form:hidden path="draftApplication" id="draftApplication"/>
                
				<div class="form-group">
					<apptags:input labelCode="propertydetails.PropertyNo."
						path="provisionalAssesmentMstDto.assNo" cssClass="mandColorClass"
						isDisabled="true"></apptags:input>

					<label for="" class="col-sm-2 control-label required-control"><spring:message
							code="property.billingMethod" text="Billing Method" /> </label>
					<c:set var="baseLookupCodess" value="BLM" />
					<apptags:lookupField
						items="${command.getLevelData(baseLookupCodess)}"
						path="provisionalAssesmentMstDto.billMethod"
						cssClass="form-control" hasChildLookup="false" hasId="true"
						showAll="false" selectOptionLabelCode="Select " />

					<%-- <apptags:input labelCode="OLD PID."
						path="provisionalAssesmentMstDto.assOldpropno"
						cssClass="mandColorClass" isDisabled="true"></apptags:input> --%>
				</div>

				<div class="accordion-toggle">
					<c:if
						test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">

						<!----------------------------------------Existing Owner Details (First owner will be the primary owner)---------------------------->
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#OwnshipDetail"><spring:message
									code="propertyTax.Mutation.ExistOwnerDetails"
									text="Existing Owner Details" /></a>
						</h4>

						<div class="panel-collapse collapse in" id="OwnshipDetail">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="ownershiptype"><spring:message
										code="property.ownershiptype" /></label>
								<div class="col-sm-4">
									<form:input
										path="provisionalAssesmentMstDto.proAssOwnerTypeName"
										id="ownershiptype" class="form-control" disabled="true" />
									<form:hidden path="ownershipPrefix" id="ownershipId"
										class="form-control" />
								</div>
							</div>

							<c:choose>
								<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
									<table id="singleOwnerTable"
										class="table table-striped table-bordered ">

										<tr>
											<th width="20%"><spring:message
													code="ownersdetail.ownersname" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.ownersnameReg" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.mobileno" /></th>
											<th width="10%"><spring:message code="property.email" /></th>
											<th width="12%"><spring:message
													code="ownersdetail.adharno" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.pancard" />
										</tr>
										<tbody>
											<tr>
												<td><form:input id="assoOwnerName"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control" disabled="true" /></td>

												<td><form:input id="assoOwnerNameReg"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg"
														class="form-control" disabled="true" /></td>

												<td><form:input id="assoMobileno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" disabled="true" /></td>

												<td><form:input id="emailId"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control" disabled="true" /></td>

												<td><form:input id="assoAddharno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno"
														class="form-control " disabled="true" /></td>
												<td><form:input id="assoPanno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control" disabled="true" /></td>
											</tr>
										</tbody>
									</table>
								</c:when>

								<c:when test="${command.getOwnershipPrefix() eq 'JO'}">
									<c:set var="a" value='0' />
									<table id="jointOwnerTable"
										class="table text-left table-striped table-bordered">

										<tr>
											<th width="10%"><spring:message
													code="ownersdetail.ownersname" /></th>
											<th width="7%"><spring:message
													code="ownersdetail.ownersnameReg" /></th>
											<th width="5%"><spring:message
													code="ownerdetails.PropertyShare" /></th>
											<th width="11%"><spring:message
													code="ownersdetail.mobileno" /></th>
											<th width="10%"><spring:message code="property.email" /></th>
											<th width="12%"><spring:message
													code="ownersdetail.adharno" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.pancard" /></th>
										</tr>
										<tbody>
											<c:forEach var="ownershipTypeList"
												items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
												<tr>
													<td>${ownershipTypeList.assoOwnerName}</td>
													<td>${ownershipTypeList.assoOwnerNameReg}</td>
													<td>${ownershipTypeList.propertyShare}</td>
													<td>${ownershipTypeList.assoMobileno}</td>
													<td>${ownershipTypeList.eMail}</td>
													<td>${ownershipTypeList.assoAddharno}</td>
													<td>${ownershipTypeList.assoPanno}</td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
								</c:when>
								<c:otherwise>
									<table id="companyDetailTable"
										class="table text-left table-striped table-bordered">

										<tr>
											<th width="25%"><spring:message
													code="ownersdetail.ownersname" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.ownersnameReg" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.mobileno" /></th>
											<th width="10%"><spring:message code="property.email" /></th>
											<th width="10%"><spring:message
													code="ownersdetail.pancard" /></th>
										</tr>
										<tbody>
											<tr>
												<td><form:input id="assoOwnerName"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName"
														class="form-control" disabled="true" /></td>

												<td><form:input id="assoOwnerNameReg"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg"
														class="form-control" disabled="true" /></td>

												<td><form:input id="assoMobileno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno"
														class="hasNumber form-control" disabled="true" /></td>

												<td><form:input id="emailId"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail"
														class="hasemailclass form-control" disabled="true" /></td>

												<td class="companyDetails"><form:input id="assoPanno"
														path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno"
														class="form-control" disabled="true" /></td>
											</tr>
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
				</div
				<!----------------------------------------------Existing Property Address Details-------------------------------------------------------->

				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#propertyAddress"><spring:message
							code="propertyTax.Mutation.ExistPropDetails" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="propertyAddress">
					<div class="form-group">
						<label for="assAddress" class="col-sm-2 control-label "><spring:message
								code="property.propertyaddress" text="Property address" /></label>
						<div class="col-sm-4">
							<form:textarea class="form-control" id="assAddress"
								path="provisionalAssesmentMstDto.assAddress" disabled="true" />
						</div>

						<apptags:input labelCode="property.location"
							path="provisionalAssesmentMstDto.locationName" isDisabled="true"></apptags:input>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label required-control"
							for="proAssPincode"><spring:message
								code="property.pincode" /></label>
						<div class="col-sm-4">
							<form:input class="form-control hasPincode"
								path="provisionalAssesmentMstDto.assPincode" id="proAssPincode"
								maxlength="6" readonly="true"></form:input>
						</div>
					</div>
				</div>

				<!----------------------------------------------------------Tax-Zone details------------------------------------------------------ -->
				<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#taxZone"><spring:message
							code="property.taxZoneDetails" /></a>
				</h4>
				<div class="panel-collapse collapse in" id="taxZone">
					<div class="panel-body">
						<div class="form-group">

							<apptags:input labelCode="property.propertyZone"
								path="provisionalAssesmentMstDto.assWardDesc1" isDisabled="true"></apptags:input>

							<c:if
								test="${command.provisionalAssesmentMstDto.assWard2 ne null}">
								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc2"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard3 ne null}">

								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc3"
									isDisabled="true"></apptags:input>
							</c:if>
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc4"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<c:if
								test="${command.provisionalAssesmentMstDto.assWard5 ne null}">

								<apptags:input labelCode="property.propertyWard"
									path="provisionalAssesmentMstDto.assWardDesc5"
									isDisabled="true"></apptags:input>
							</c:if>
						</div>
						<div class="form-group">
							<apptags:input labelCode="unitdetails.RoadType"
								path="provisionalAssesmentMstDto.proAssdRoadfactorDesc"
								isDisabled="true"></apptags:input>
						</div>

					</div>
				</div>

				<!-- ---------------------------------------------Unit Details --------------------------------------------------------------------------->

		<div class="accordion-toggle">
			<h4 class="margin-top-0 margin-bottom-10 panel-title" id="scrollToDiv">
				<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
			</h4>
		<div class="panel-collapse collapse in" id="UnitDetail">
		<c:choose>
		<c:when test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"> 
		<div class="table-responsive" ><!-- style="overflow:scroll" -->
		<c:set var="d" value="0" scope="page" />			
                <table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody id="tableToModify">                        
                        <tr>
                            <th width="7%" class=""><spring:message code="unitdetails.unitno" text="Sr No."/></th>
							<th width="8%" class="required-control"><spring:message code="unitdetails.flatNo" text="Flat No./ Identifier No."/></th>
                            <th width="12%" class=""><spring:message code="unitdetails.floorno" text="Floor No."/></th>
                            <th width="15%" class=""><spring:message code="unitdetails.old.OccupierName" text="Old Occupier Name"/></th>
                            <th width="15%" class=""><spring:message code="unitdetails.new.OccupierName" text="New Occupier Name"/></th>
                            <th width="15%" class=""><spring:message code="unitdetails.occupierMobNo" text="Occupier Mobile No."/></th>
                            <th width="15%" class=""><spring:message code="unitdetails.occupierEmail" text="Occupier Email"/></th>
                            <th width="15%" class=""><spring:message code="unitdetails.indexNo" text="Index No."/></th>
                        </tr>
                          
       					<c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                                            
                    	<tr class="firstUnitRow" id="rowToClone">
                  
                        	 <td>
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" 
              					id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
              				</td>
						
							<td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo" type="text" 
											class="form-control  mandColorClass text-right" id="flatNo${status.count-1}" data-rule-required="true"  /></td>
							
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo" cssClass="form-control changeParameterClass mandColorClass floorNoClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="prop.selectFloorNo" isMandatory="true" hasTableForm="true" disabled="true"/>
										
										<c:forEach
											items="${command.getLevelData(baseLookupCode)}" var="lookUp">
											<c:if test="${lookUp.lookUpCode eq 'OTH' }">
												<input type="hidden" value="${lookUp.lookUpId}" id="hiddenOther">
											</c:if>
										</c:forEach>
							</td>
							<td>
 			          		   <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" class="form-control alphaNumeric preventSpace" id="occupierName${status.count-1}" maxlength="500" readonly="true"/></div>
							</td>
							<td>
 			          		   <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].newOccupierName" class="form-control alphaNumeric preventSpace" id="newOccupierName${status.count-1}" maxlength="500" onchange="blankReqToMaintainSession()" /></div>
							</td>
							<td>
 			          		   <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierMobNo" class="form-control hasNumber preventSpace" id="occupierMobNo${status.count-1}" maxlength="10"/></div>
							</td>
							<td>
 			          		   <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierEmail" class="form-control alphaNumeric preventSpace" id="occupierEmail${status.count-1}" maxlength="500"/></div>
							</td>
							<td>
 			          		   <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assessmentRemark" class="form-control alphaNumeric preventSpace" id="assessmentRemark${status.count-1}" maxlength="500"/></div>
							</td>
                       	</tr> 
                    
				</c:forEach>               		
                </tbody>
                </table>                
</div>                        
 			 	 
 		</c:when>
 
</c:choose>

            </div>
            </div>
		<!-------------------------------------------------Unit Details End  -------------------------------------------->

				<div id="checkListDiv">
					<c:if test="${not empty command.checklist}">
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#DocumentUpload"><spring:message
									code="propertyTax.DocumentsUpload" /></a>
						</h4>
						<div id="DocumentUpload" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="overflow margin-top-10">
									<div class="table-responsive">
										<table class="table table-hover table-bordered table-striped">
											<tbody>
												<tr>
													<th><spring:message code="water.serialNo" text="Sr No" />
													</th>
													<th><spring:message code="water.docName"
															text="Document Name" /></th>
													<th><spring:message code="water.status" text="Status" />
													</th>
													<th><spring:message code="water.uploadText"
															text="Upload" /></th>
												</tr>
												<c:forEach items="${command.checklist}" var="lookUp"
													varStatus="lk">
													<tr>
														<td>${lookUp.documentSerialNo}</td>
														<c:choose>
															<c:when
																test="${userSession.getCurrent().getLanguageId() eq 1}">
																<td>${lookUp.doc_DESC_ENGL}</td>
															</c:when>
															<c:otherwise>
																<td>${lookUp.doc_DESC_Mar}</td>
															</c:otherwise>
														</c:choose>
														<c:if test="${lookUp.checkkMANDATORY eq 'Y'}">
															<td><spring:message code="water.doc.mand" /></td>
														</c:if>
														<c:if test="${lookUp.checkkMANDATORY eq 'N'}">
															<td><spring:message code="water.doc.opt" /></td>
														</c:if>
														<td><div id="docs_${lk}" class="">
																<apptags:formField fieldType="7" labelCode=""
																	hasId="true" fieldPath="checklist[${lk.index}]"
																	isMandatory="false" showFileNameHTMLId="true"
																	fileSize="BND_COMMOM_MAX_SIZE"
																	maxFileCount="CHECK_LIST_MAX_COUNT"
																	validnFunction="ALL_UPLOAD_VALID_EXTENSION"
																	currentCount="${lk.index}" />
															</div></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</c:if>
				</div>

				<div class="text-center padding-top-10">
					<button type="button" class="btn btn-success btn-submit hidden-print"
						onclick="saveBillingMethodForm(this)" id="saveBillMethodForm">
						<spring:message code="bt.save" text="Submit" />
					</button>
					<button type="button" class="btn btn-success btn-submit hidden-print"
						onclick="saveBillingMethodFormForDrat(this)" id="saveBillMethodForm">
						<spring:message code="" text="Save As Draft" />
					</button>
					
					<button type="button" class="btn btn-success btn-submit hidden-print"
						onclick="addArrearDetails(this)" id="addArrears">
						<spring:message code="" text="Add arears" />
					</button>
					
					<button type="button" class="btn btn-blue-2 hidden-print" id="backBtn"
						onclick="backToSearchPage(this)">
						<spring:message code="bt.backBtn" text="Back" />
					</button>
						
					<button type="button" onclick="window.print()"
							class="btn btn-primary hidden-print">
						<i class="fa fa-print"></i>
						<spring:message code="property.noDues.Print" text="Print" />
					</button>
				</div>

			</form:form>

		</div>
		</div>
	</div>
</div>

<script>
	function printAppliactionDiv(divName) {
		var printContents = document.getElementById(divName).innerHTML;
		var originalContents = document.body.innerHTML;
		document.body.innerHTML = printContents;
		window.print();
		document.body.innerHTML = originalContents;
	} 
</script>
