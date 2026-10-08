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
<script type="text/javascript" src="js/water/updateWaterDataEntry.js"></script>

<style>

.remark > label[for="remark"] {
		padding-left: 1px;
		width: 19%;
}

#remark{
	height:5rem;
	width: 330% !important;
} 
</style>
<div id="validationDiv">

	<!-- Start breadcrumb Tags -->
	<apptags:breadcrumb></apptags:breadcrumb>
	<!-- End breadcrumb Tags -->

	<!-- Start Content here -->
	<div class="content">
		<!-- Start Main Page Heading -->
		<div class="widget">
		<div class="mand-label clearfix">
			<span><spring:message code="property.Fieldwith"/><i class="text-red-1">* </i><spring:message code="property.ismandatory"/>
			</span>
		</div>
			<div class="widget-header">
				<h2>
					<strong><spring:message code="water.update.data.entry" text="Update Data Entry" /></strong>
				</h2>
				<div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message
									code="property.Help" /></span></i></a>
				</div>
			</div>
			<div class="widget-content padding">
				<form:form action="UpdateWaterDataEntrySuite.html"
					class="form-horizontal form" name="UpdateWaterDataEntrySuite"
					id="UpdateWaterDataEntrySuite">
					<div class="compalint-error-div">
						<jsp:include page="/jsp/tiles/validationerror.jsp" />
						<div
							class="warning-div error-div alert alert-danger alert-dismissible"
							id="errorDiv"></div>
					</div>
					
				<!---------------------------Occupier Details---------------------------->
				<form:hidden path="" value="${command.checkPhysicalEntryFlag}" id="checkPhysicalEntryFlag"/>
				<form:hidden path="" value="${command.checkMeterDetailsFlag}" id="checkMeterDetailsFlag"/>
				<form:hidden path="" value="${command.checkMeterInstallDateFlag}" id="checkMeterInstallDateFlag"/>
				<form:hidden path="" value="${command.checkFirstBilling}" id="checkFirstBilling"/>
				<form:hidden path="" value="${command.checkMeterActiveFlag}" id="checkMeterActiveFlag"/>
				<form:hidden path="" value="${command.checkFirstBillGeneratedFlag}" id="checkFirstBillGeneratedFlag"/>
				<div class="accordion-toggle">
		            <h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#ConsumerDetails"><spring:message
						code="water.consumerDetails" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="OccupierDetail">
						<div class="form-group">
						
							<label for="connectionNo" class="col-sm-2 control-label"><spring:message code="WaterDisconnectionModel.connectionNo"/></label> 
				          	<div class="col-sm-4">
				          		<form:input path="waterConnectionDetailsDto.connectionNo" disabled="true"
				          					class="form-control preventSpace" id="connectionNo" maxlength="500"/>
				          	</div>
						
							<label for="consumerFirstName" class="col-sm-2 control-label"><spring:message code="ApplicantDetailDTO.applicantFirstName"/></label> 
				          	<div class="col-sm-4">
				          		<form:input path="waterConnectionDetailsDto.firstName" 
				          					class="form-control hasCharacter preventSpace" id="firstName" maxlength="500"/>
				          	</div>
		 			          		   		
						</div>
							
						<div class="form-group">
						
							<label for="consumerMiddleName" class="col-sm-2 control-label"><spring:message code="water.middleName"/></label> 
				          	<div class="col-sm-4">
				          		<form:input path="waterConnectionDetailsDto.middleName" 
				          					class="form-control hasCharacter preventSpace" id="middleName" maxlength="500"/>
				          	</div>
				          	
				          	<label for="consumerLastName" class="col-sm-2 control-label"><spring:message code="ApplicantDetailDTO.applicantLastName"/></label> 
				          	<div class="col-sm-4">
				          		<form:input path="waterConnectionDetailsDto.lastName" 
				          					class="form-control hasCharacter preventSpace" id="lastName" maxlength="500"/>
				          	</div>
						
							
						</div>
						<div class="form-group">
							<label for="consumerContactNo" class="col-sm-2 control-label"><spring:message code="unitdetails.occupierMobNo" /></label>
								
							<div class="col-sm-4">
								<form:input	path="waterConnectionDetailsDto.contactNo"
									type="text" class="form-control hasMobileNo  preventSpace" maxlength="10" id="occupierMobNo" />
							</div>
							
							<label for="email" class="col-sm-2 control-label"><spring:message code="property.email" /></label>
								
							<div class="col-sm-4">
								<form:input	path="waterConnectionDetailsDto.emailAddress" onBlur= "emailValidation()"
									type="text" class="hasemailclass form-control preventSpace" id="emailId" />
								<label id="emailError" class="error"></label>
									
							</div>
							
						</div>
						<div class="form-group">
							<label for="noOfFamilies" class="col-sm-2 control-label">
									<spring:message code="water.nwwtrcnctn.noOffamilies" /></label>
							<div class="col-sm-4">
								<form:input	path="waterConnectionDetailsDto.noOfFamilies"
									type="text" class="form-control preventSpace" maxlength="10" id="noOfFamilies" />
							</div>	
						</div>
					</div>
				</div>
				<!---------------------------End of Occupier Details---------------------------->
				
				<!--------------------------- Connection Details ------------------------------->
				
				<div class="accordion-toggle">
		            <h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#ConnectionDetails"><spring:message
						code="water.connectionDetails.information" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="ConnectionDetails">
						
						 <div class="form-group">
							<apptags:lookupFieldSet cssClass="form-control required-control" baseLookupCode="TRF"
							hasId="true" pathPrefix="waterConnectionDetailsDto.trmGroup"
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true" showAll="true"/>
						</div>
						
						<div class="form-group">
						
							<label class="control-label col-sm-2 required-control">
							<spring:message code="MeterReadingDTO.meterType" /></label>
							<div>
								<c:set var="baseLookupCode" value="WMN" />
								<apptags:lookupField items="${command.getLevelData(baseLookupCode)}"
								path="waterConnectionDetailsDto.csMeteredccn" cssClass="form-control"
								hasChildLookup="false" hasId="true" showAll="false"
								selectOptionLabelCode="eip.select" isMandatory="true"/>
							</div>
							<label class="control-label col-sm-2"><spring:message code="water.connection.information.pc.date" text="PC Date"/></label>
							<div class="col-sm-4">
								 <form:input path="waterConnectionDetailsDto.pcDate" cssClass="pcDateCal form-control"  id="pcDate" />
   						    </div>
						</div>
						<div class="MeterDetailsDiv">
							<div class="form-group">
								<label class="control-label col-sm-2"><spring:message code="water.meterDet.mtrMake"/></label>
								<div class="col-sm-4">
								
								 <form:input path="waterConnectionDetailsDto.meterMake" cssClass="form-control hasCharacter" id="meterMake" maxlength="39" />
								 </div>
								<label class="control-label col-sm-2 required-control"><spring:message code="water.meterDet.metNo"/></label>
								<div class="col-sm-4">
									<form:input id="meterNumber" path="waterConnectionDetailsDto.meterNumber" cssClass="form-control mandClassColor hasNumber " maxLength="19" />									
								</div>						
							</div>				
							<div class="form-group">				
								<label class="control-label col-sm-2 required-control"><spring:message code="water.meterDet.initalmtrRead"/></label>
								<div class="col-sm-4">
									<form:input path="waterConnectionDetailsDto.initialMeterReading" cssClass="form-control mandClassColor hasNumber" id="initialMeterReading" maxLength="7" />
									
								</div>
								
								<label class="control-label col-sm-2 required-control"><spring:message code="water.meterDet.mtrMaxRead"/></label>
								<div class="col-sm-4">
									 <form:input path="waterConnectionDetailsDto.meterMaxReading" cssClass="form-control hasNumber" id="meterMaxReading" maxLength="7" />
								</div>
							</div>				
							<div class="form-group">						
								<label class="control-label col-sm-2 required-control"><spring:message code="water.meterDet.instlDt"/></label>
								<div class="col-sm-4">
								 <form:input path="waterConnectionDetailsDto.meterInstallationDate" cssClass="lessthancurrdate cal form-control"  id="meterInstallationDate" />
								 
								 </div>
							
							
								<label class="control-label col-sm-2"><spring:message code="water.meterDet.mtrCost"/></label>
								<div class="col-sm-4">
									 <form:input path="waterConnectionDetailsDto.meterCost" cssClass="form-control hasNumber" id="meterCost" maxLength="10"/>
								 </div>
							</div>
							<div class="form-group">							
								<label class="control-label col-sm-2 required-control"><spring:message code="water.meterDet.ownerShip"/></label>
								<div class="col-sm-4">
								<form:select path="waterConnectionDetailsDto.meterOwnerShip" cssClass="form-control mandClassColor" id="meterOwnerShip" >
										<form:option value="" >Select</form:option>
										<c:forEach items="${command.ownerShip}" var="ownerShip">
											<form:option value="${ownerShip.lookUpId}">${ownerShip.descLangFirst}</form:option>					
										</c:forEach>
								</form:select>							
								</div> 
							</div>
						</div>						
		          	</div>
		       	</div>
				
				<!---------------------End of connection details ------------------------------->
				<!---------------------------Property Address Location-------------------------->
				<div class="accordion-toggle">
					<h4 class="margin-top-0 margin-bottom-10 panel-title">
						<a data-toggle="collapse" href="#propertyAddress"><spring:message
							code="property.Propertyaddress" /></a>
					</h4>
					<div class="panel-collapse collapse in" id="propertyAddress">
					<div class="form-group">
								<label class="col-sm-2 control-label"> <spring:message
										code="water.propNO" text="Property No." /></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control preventSpace"
										path="waterConnectionDetailsDto.propNo" minlength="12"
										maxlength="12" placeholder="Enter 12-digit Property Number"
										id="propNo" disabled="${command.waterConnectionDetailsDto.propNo ne '' && command.isPropertyValidFlag}" ></form:input>										
								</div>
							</div>
					
						<div class="form-group">
							<apptags:textArea labelCode="property.propertyaddress" path="waterConnectionDetailsDto.address"
							 maxlegnth="500" cssClass="preventSpace"></apptags:textArea>
							 
							 <label class="col-sm-2 control-label">
								<spring:message code="water.nwwtrcnctn.noOfFlats" text="Flat No."/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.flatNo" 
									id="flatNo"></form:input>
							</div>	
						</div>
						
						<div class="form-group">
							
							<label class="col-sm-2 control-label"><spring:message code="water.buildingName"/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.buildingName" 
									id="buildingName"></form:input>
							</div>
							<label class="col-sm-2 control-label"><spring:message code="water.roadName" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.roadCrossName" 
									id="roadCrossName"></form:input>
							</div>			
						</div>
						
						<div class="form-group">
							
							<label class="col-sm-2 control-label"><spring:message code="water.lane" /></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.lane" 
									id="lane"></form:input>
							</div>	
							<label class="col-sm-2 control-label">
								<spring:message code="water.cityName" text="City Name"/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.cityName" 
									id="cityName"></form:input>
							</div>		
						</div>
						
						<div class="form-group">
							<%-- <apptags:textArea labelCode="property.landDetails.Remark" path="waterConnectionDetailsDto.landmark"
							 maxlegnth="1000" cssClass="preventSpace"></apptags:textArea>
							   --%>
							<label class="col-sm-2 control-label" for="proAssPincode"><spring:message code="water.pincode"/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control hasPincode" path="waterConnectionDetailsDto.pincode" 
									id="pincode" maxlength="6" data-rule-digits="true" data-rule-minlength="6" data-rule-maxlength="6"></form:input>
							</div>	
							<label class="col-sm-2 control-label" for="landmark"><spring:message code="water.landMark"/></label>
							<div class="col-sm-4">
								<form:input type="text" class="form-control preventSpace" path="waterConnectionDetailsDto.landmark" 
									id="landmark"></form:input>
							</div>	
						</div>
					
						<div class="form-group">
						
							<%-- <label class="col-sm-2 control-label required-control" for="remark"><spring:message code="water.plumberLicense.remarks"/></label> --%>
							<div class="col-sm-10 remark">
							<apptags:textArea labelCode="water.plumberLicense.remarks" path="waterConnectionDetailsDto.remark"
							 maxlegnth="500" cssClass="preventSpace"></apptags:textArea>
							<!-- 	<textArea type="text" class="form-control" path="waterConnectionDetailsDto.remark" 
									id="remark"></textArea>
							 --></div>
						</div>
					</div>
				</div>
					
					<!-------------------------End Property Address Location------------------------>
					
					<!--Search Button-->
					<div class="text-center padding-15 clear">
						<button type="button" class="btn btn-success btn-submit"
							onclick="updateDataEntry(this)" id="submit">
							<spring:message code="water.entry.btn.update" text="Update" />
						</button>

						<button type="button" class="btn btn-danger"
							data-original-title="Back"
							onclick="window.location.href='UpdateWaterDataEntrySuite.html'">
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

