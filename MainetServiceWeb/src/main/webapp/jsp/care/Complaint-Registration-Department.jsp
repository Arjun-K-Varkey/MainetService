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
<script type="text/javascript"
	src="js/mainet/jQueryMaskedInputPlugin.js"></script>
<script type="text/javascript" src="js/care/complaint-registration.js"></script>
 <script type="text/javascript" src="js/care/complaint-map.js"></script> 
<script type="text/javascript">
	$(document).ready(function() {
		$("input").on("keypress", function(e) {
			if (e.which === 32 && !this.value.length)
				e.preventDefault();
		});

		$("#care").validate({
			onkeyup : function(element) {
				this.element(element);
				console.log('onkeyup fired');
			},
			onfocusout : function(element) {
				this.element(element);
				console.log('onfocusout fired');
			}
		});

		document.care.MobileNumber.focus();
	});
</script>

<style>
textarea.form-control {
	resize: vertical !important;
	height: 4.6em;
}
#map{
	height:400px;
}
#address_list li{
	display:inline-block;
	background : #EEE;
	border-radius: 3px;
	padding:.5rem;
	margin:0.2rem 0.5rem;
}
#close-button{
	text-align:center;
	margin-top:1rem
}
label[for="ward3"],
label[for="ward3"]+div{
	margin-top: 0.7rem;
}
</style>


<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<!-- Start info box -->
	<div class="widget">
		<%-- <div class="widget-header">
		 
          <h2><spring:message code="care.complaint.registration.dept" text="Complaint Registration Department"/></h2>

           </div> --%>
		<apptags:helpDoc url="GrievanceDepartmentRegistration.html"></apptags:helpDoc>

		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="care.fieldwith"
						text="Field with " /><i class="text-red-1">*</i> <spring:message
						code="care.ismandatory" text="is mandatory" /></span>
			</div>


			<form:form id="care" name="care" class="form-horizontal"
				commandName="command" action="GrievanceDepartmentRegistration.html"
				method="POST" enctype="multipart/form-data">


				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
				</div>


				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<ul>
						<li><label id="errorId"></label></li>
					</ul>
				</div>
				<form:hidden path="" id="reqType" value="${command.requestType}" />
				<form:hidden path="" id="kdmcEnv" value="${command.kdmcEnv}" />
				<form:hidden path="" id="currentOrgId" value="${userSession.organisation.orgid}" />
				<h4>
					<spring:message code="care.referencedetails" text="Complaint Details" />
				</h4>

				<div class="form-group">
					<div id="id_department_div">
						<label class="col-sm-2 control-label required-control"> <spring:message
								code="care.skdclCompType" text="Complaint Type" />
						</label>
						<div class="col-sm-4">
							<form:select path="careRequest.departmentComplaint"
								class="form-control chosen-select-no-results mandColorClass"
								id="departmentComplaint" data-rule-required="true">
								<form:option value="0">
									<spring:message code='care.select' text="Select"/>
								</form:option>

								<c:forEach items="${command.departments}" var="lookup">
									<c:choose>
										<c:when test="${userSession.languageId eq 1}">
											<form:option code="${lookup.lookUpCode}" value="${lookup.lookUpId}">${lookup.descLangFirst}</form:option>
										</c:when>
										<c:otherwise>
											<form:option code="${lookup.lookUpCode}" value="${lookup.lookUpId}">${lookup.descLangSecond}</form:option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<div id="id_complaintType_div">
						<apptags:select labelCode="care.skdclCompSubType"
							items="${command.complaintTypes}" 
							path="careRequest.complaintType" selectOptionLabelCode="Select"
							isMandatory="true" isLookUpItem="true"
							cssClass="chosen-select-no-results  compType"></apptags:select>
					</div>
				</div>
				
				<div class="form-group">

					
					<input type="hidden" id="amtDuesCheck" name="" value="N" />
					<!-- on selection of property and water label is change -->
					<%-- <label class="col-sm-2 control-label" id="refNoLblId" for="extReferNumber"><spring:message
							code="care.extReferNumber" text="Reference No" /></label> --%>
					<label class="col-sm-2 control-label required-control" id="propertyLblId" for="propertyLblId"><spring:message
							code="care.propertyLbl" text="Property No" /></label>
					<label class="col-sm-2 control-label required-control" id="waterLblId" for="waterLblId"><spring:message
							code="care.waterLbl" text="Connection No" /></label>
					<div class="col-sm-4 refNoClass">
						<form:input name="" type="text" id="extReferNumber" autocomplete="off"
							class="form-control" path="careRequest.extReferNumber"></form:input>
					</div>
					
					<div id="flatNoSelectId">
						<label class="col-sm-2 control-label required-control"><spring:message
								code="care.prop.flatNo" text="Flat No" /></label>
						<div class="col-sm-4">
							<form:select path="careRequest.propFlatNo"
								class="form-control chosen-select-no-results" id="flatNo">
							<form:option value="">
								<spring:message code='care.select' text="Select" />
							</form:option>
							<c:forEach items="${command.flatNoList}" var="flatNo">
								<form:option value="${flatNo}">${flatNo}</form:option>
							</c:forEach>	
							</form:select>
						</div>
					</div>
					
					
				</div>
				
				
				
				
				
				


				<div class="form-group">

					<label class="col-sm-2 control-label required-control" for="Landmark"><spring:message
							code="care.skdcllandmarkLoc" text="Location" /></label>
					<div class="col-sm-4">
						<form:input name="" type="text" id="Landmark" class="form-control"
							path="careRequest.landmark"  autocomplete="off" maxlength="200" onblur="addr_search();"></form:input>
					</div>
					
					<label class="col-sm-2 control-label required-control"
						for="ComplaintDescription"><spring:message
							code="care.skdclcomplaintdescription" text="Complaint Description" /></label>
					<div class="col-sm-4">
						<form:textarea name="" cols="" rows="" class="form-control"
							id="ComplaintDescription" path="careRequest.description" maxlength="1000"
							onkeyup="countChar(this,1000,'complaintDescriptionCount')" onfocus="countChar(this,1000,'complaintDescriptionCount')"></form:textarea>
						<div class="pull-right">
							<spring:message code="charcter.remain" text="characters remaining " /> <span id="complaintDescriptionCount">1000</span>
						</div>
						
					</div>					

				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="care.old.complaint.reference"
							text="Old Reference Complaint No" /></label>
					<div class="col-sm-4">
							<form:input name="" type="text" id="" class="form-control"
								path="careRequest.oldComplaintNumber" maxlength="20" autocomplete="off"></form:input>
						</div>
				</div>

				<%-- <div class="form-group">

					<label class="col-sm-2 control-label"><spring:message
							code="care.department.registartion.JantaDarbar"
							text="Janata Darbar" /></label>
					<div class="col-sm-4">



						<label class="radio-inline margin-top-5"> <form:radiobutton
								path="careRequest.mode" class="" id="" value="1" /> <spring:message
								code="" text="Yes" /></label> <label class="radio-inline margin-top-5">
							<form:radiobutton path="careRequest.mode" class="" id=""
								value="0" /> <spring:message code="" text="No" />
						</label>
					</div>


					<label class="col-sm-2 control-label"><spring:message
							code="care.department.registartion.Municipalcommissioner"
							text="Municipal Commissioner" /></label>
					<div class="col-sm-4">



						<label class="radio-inline margin-top-5"> <form:radiobutton
								path="careRequest.municiaplCommissioner" class="" id=""
								value="1" /> <spring:message code="" text="Yes" /></label> <label
							class="radio-inline margin-top-5"> <form:radiobutton
								path="careRequest.municiaplCommissioner" class="" id=""
								value="0" /> <spring:message code="" text="No" />
						</label>
					</div>

				</div> --%>

				<%-- <div class="form-group" >
					<apptags:lookupFieldSet cssClass="form-control required-control"
						baseLookupCode="CWZ" hasId="true" pathPrefix="careRequest.ward"
						hasLookupAlphaNumericSort="true"
						hasSubLookupAlphaNumericSort="true" showAll="false"
						isMandatory="true" />
				</div>  --%>
				<div id="complaint-zone-ward" class="form-group">
				
				</div>
				
				<div class="form-group">

					<div id="residentDivId">
						<label class="col-sm-2 control-label required-control" for="residentId"><spring:message
							code="care.residentId" text="Resident ID (UID/Election ID)" /></label>
						<div class="col-sm-4">
							<form:input name="" type="text" id="residentId" class="form-control"
								path="careRequest.residentId" maxlength="19" autocomplete="off"></form:input>
						</div>
					</div>					

				</div>
	
				<h4>
					<spring:message code="care.applicantInformation"
						text="Complainant Information" />
				</h4>

				<div class="form-group">
					<label class="col-sm-2 control-label required-control"
						for="MobileNumber"><spring:message
							code="care.mobilenumber" text="Mobile Number" /></label>
					<div class="col-sm-4">
						<form:input name="mobileNo" type="text" autocomplete="off"
							class="form-control hasNumber" id="MobileNumber"
							path="applicantDetailDto.mobileNo" maxlength="10"></form:input>
					</div>
					<label class="col-sm-2 control-label required-control"
						for="titleId"><spring:message code="care.name" /></label>

					<div class="col-sm-1 padding-right-0">
						<form:select path="applicantDetailDto.titleId"
							class="form-control" id="titleId">
							<c:set var="baseLookupCode" value="TTL" />
							<form:option value="0">
								<spring:message code="Select" text="Select" />
							</form:option>
							<c:forEach items="${command.getLevelData(baseLookupCode)}"
								var="lookUp">
								<form:option value="${lookUp.lookUpId}"
									code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
							</c:forEach>
						</form:select>
					</div>

					<div class="col-sm-1 padding-right-0 padding-left-0">
						<spring:message code="care.firstname" text="care.firstname"
							var="firstnameplh"></spring:message>
						<form:input name="firstName" type="text"
							class="form-control hasSpecialChara" id="FirstName1" autocomplete="off"
							path="applicantDetailDto.fName" placeholder="${firstnameplh}" maxlength="20"></form:input>
					</div>
					<div class="col-sm-1 padding-right-0 padding-left-0">
						<spring:message code="care.middlename" text="care.middlename"
							var="middlenameplh"></spring:message>
						<form:input name="MiddleName" type="text" autocomplete="off"
							class="form-control hasSpecialChara" id="MiddleName"
							path="applicantDetailDto.mName" placeholder="${middlenameplh}" maxlength="20"></form:input>
					</div>
					<div class="col-sm-1  padding-left-0">
						<spring:message code="care.lastname" text="care.lastname"
							var="lastnameplh"></spring:message>
						<form:input name="lastName" type="text" autocomplete="off"
							class="form-control hasSpecialChara " id="LastName1"
							path="applicantDetailDto.lName" placeholder="${lastnameplh}" maxlength="20"></form:input>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label required-control" for="gender"><spring:message
							code="care.gender" /></label>
					<apptags:lookupField items="${command.getLevelData('GEN')}"
						path="applicantDetailDto.gender" cssClass="form-control"
						selectOptionLabelCode="Select" hasId="true" isMandatory="false" />

					<label class="col-sm-2 control-label" for="EmailID"><spring:message
							code="care.emailid" text="Email ID" /></label>
					<div class="col-sm-4">
						<form:input name="" type="email" class="form-control" id="EmailID" autocomplete="off"
							path="applicantDetailDto.email" data-rule-email="true"></form:input>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label required-control"
						for="address"><spring:message code="care.address"
							text="Address" /></label>
					<div class="col-sm-4">
						<form:textarea class="form-control" id="address" maxlength="1000"
							path="applicantDetailDto.areaName" autocomplete="off" onblur="addr_search();"></form:textarea>
					</div>

					<label class="col-sm-2 control-label required-control" for="Pincode"><spring:message
							code="care.pincode" text="Pincode" /></label>
					<div class="col-sm-4">
						<form:input name="" type="text" class="form-control hasNumber" autocomplete="off"
							id="Pincode" path="applicantDetailDto.pincodeNum" maxlength="6" minlength="6"></form:input>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label required-control"
						for="address"><spring:message code=""
							text="Find Your Address in Map" /></label>
					<div class="col-sm-4">
						<input type="text" name="addr" value="" id="addr" size="58"  />
					</div>
				</div> --%>
				<div class="form-group col-sm-12" id="search-result">
					
					<div id="results" class="padding-10">
						
					</div>
					
				</div>
				
				<div class="form-group col-sm-12" >
				<form:hidden id="lat"  path="careRequest.latitude" />
				<form:hidden id="lon"  path="careRequest.longitude" />
				</div>
				
				
				<!-- <input type="text" name="lat" id="lat" size=12 value="" path="careRequest.latitude">
				<input type="text" name="lon" id="lon" size=12 value="" path="careRequest.longitude"> -->
				<!-- <input type="text" name="addr" value="" id="addr" size="58" />
				<button type="button" onclick="addr_search();">Search</button> -->
				<!-- <div id="results"></div> -->
				<div class="margin-top-10 col-sm-12" id="map">
				</div>
				<div style="clear:both"></div>
				<div class="form-group pendingDues">
					<label class="col-sm-2 control-label"><spring:message code="care.pendingDues" text="Pending Dues" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="applicantDetailDto.payAmount"
									cssClass="form-control mandColorClass hasDecimal text-right"
									maxlength="10" id="payAmount" readonly="true"
									onkeypress="return hasAmount(event, this, 8, 2)"
									onchange="getAmountFormatInDynamic((this),'payAmount')" />
								<label class="input-group-addon"><i
									class="fa fa-inr"></i></label>
						</div>
					</div>
				</div>


				<div class="form-group margin-top-20">
					<label class="col-sm-2 control-label" id="careDocLbl" for="UploadDocument"><spring:message
							code="care.upload" text="Upload Document" /></label>
					<div class="col-sm-10">
						<apptags:formField fieldType="7" labelCode="" hasId="true"
							fieldPath="" isMandatory="false" showFileNameHTMLId="true"
							fileSize="CARE_COMMON_MAX_SIZE" 
							maxFileCount="CHECKLIST_MAX_UPLOAD_COUNT"
							validnFunction="CARE_VALIDATION_EXTENSION"
							currentCount="0" cssClass="btn-sm" />
						<div class="col-sm-9 padding-0">
							<small class="text-blue-2 "> <spring:message
									code="care.form.validator.fileUploadNote"
									text="(Upload File upto 5MB and only pdf,jpeg,jpg,png)" />
							</small>
						</div>
					</div>
				</div>



				<div class="text-center">
					<apptags:submitButton entityLabelCode="Submit" buttonLabel="care.form.submit"
						cssClass="button-input btn btn-submit compSubmit" actionParam="saveDetails"></apptags:submitButton>
					<button type="Reset" class="btn btn-warning" onclick="window.location.href='GrievanceDepartmentRegistration.html'">
						<spring:message code="care.reset" text="Reset" />
					</button>
					<c:if test="${userSession.employee.designation.dsgshortname ne 'OPR' }">
						<button type="button"
							onclick="window.location.href='AdminHome.html'"
							class="btn btn-danger">
							<spring:message code="care.back" text="Back" />
						</button>
					</c:if>
				</div>
				<input type="hidden" id="taskName" name="taskName"
					value="${taskName}" />
				<input type="hidden" id="taskId" name="taskId" value="${taskId}" />
				<input type="hidden" id="processName" name="processName"
					value="${processName}" />
				<form:hidden id="decision" name="decision" path=""
					value="${actionStatus}" />
				<form:hidden id="comments" name="comments" path=""
					value="${actionComment}" />
				<input type="hidden" id="csrf_parameter_name"
					name="csrf_parameter_name" value="${_csrf.parameterName}" />
				<input type="hidden" id="csrf_token" name="csrf_token"
					value="${_csrf.token}" />
				<input type="hidden" id="complaintTypeHidden"
					name="complaintTypeHidden" value="${complaintType}" />
				<form:hidden path="labelType" />
				<form:hidden path="applicationType" />
			</form:form>
		</div>
	</div>
	<!-- End of info box -->
	<!--------- GIS MAP Initialization --------------->
<script>
	
	var startlat = 19.2403;
	var startlon = 73.1305;
	var options = {
			 center: [startlat, startlon],
			 zoom: 9
			}
		var apiKey = "API_KEY";
	    var basemapEnum = "ArcGIS:Streets";
	
	    var map = L.map('map', {
	      minZoom: 2
	    }).setView([startlat, startlon], 12);
	
	    L.esri.Vector.vectorBasemapLayer(basemapEnum, {
	      apiKey: apiKey
	    }).addTo(map);
	
	    
	
/* 	    L.esri.Vector.vectorTileLayer(
	        "https://basemaps.arcgis.com/arcgis/rest/services/World_Basemap_v2/VectorTileServer"
	      
	    ).addTo(map); 

	    //add ESRI WMS layer
	    L.esri
        .dynamicMapLayer({
          url: "https://maps.kdmc.gov.in/agserver/rest/services/SKDCL_BASEMAP_24_03_2022/MapServer/",
          layers: [5],
          opacity: 1,
          zoom:6
        }).addTo(map);

	    L.esri
        .dynamicMapLayer({
          url: " https://maps.kdmc.gov.in/agserver/rest/services/SKDCL_BASEMAP_24_03_2022/MapServer/",
          layers: [13],
          opacity: 1,
          zoom:6
        }).addTo(map); */
        
        

        var esriLayer = L.esri.tiledMapLayer({
            url: 'https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer',
            opacity: 1
        }).addTo(map); 
        
        
        //add ESRI WMS layer
        L.esri
        .dynamicMapLayer({
          url: "https://maps.kdmc.gov.in/agserver/rest/services/SKDCL_BASEMAP_24_03_2022/MapServer/",
          layers: [5],
          opacity: 1,
          zoom:6
        }).addTo(map);

        L.esri
        .dynamicMapLayer({
          url: "https://maps.kdmc.gov.in/agserver/rest/services/SKDCL_BASEMAP_24_03_2022/MapServer/",
          layers: [13],
          opacity: 1,
          zoom:6
        }).addTo(map);
	    
	
	var myMarker = L.marker([startlat, startlon], {title: "Coordinates", alt: "Coordinates", draggable: true}).addTo(map).on('dragend', function() {
	 var lat = myMarker.getLatLng().lat.toFixed(8);
	 var lon = myMarker.getLatLng().lng.toFixed(8);
	 var czoom = map.getZoom();
	 if(czoom != 14) { map.setView([lat,lon], 14); } else { map.setView([lat,lon]); }
	 document.getElementById('lat').value = lat;
	 document.getElementById('lon').value = lon;
	 //var serAddress=getAddressLatlong(lat,lon);
	 
	 //myMarker.bindPopup("Addresss :"+serAddress['address'][0].Match_addr+ "<br/ > Lat " + lat + "<br />Lon " + lon).openPopup();
	 myMarker.bindPopup("Lat " + lat + "<br />Lon " + lon).openPopup();
	 findAddressfromlatlon(lat,lon);
	});
	map.on("moveend", function(e){
			//alert("Hello");
	})
</script>

</div>

<!--Scroll To Top-->
<a class="tothetop" href="javascript:;"><i class="fa fa-angle-up"></i></a>

<!-- End of page -->

<div class="md-overlay"></div>
</body>

</html>