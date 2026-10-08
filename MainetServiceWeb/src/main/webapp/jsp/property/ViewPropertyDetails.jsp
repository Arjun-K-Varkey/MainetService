<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/property/viewPropertyDetails.js"></script>

<style>
.labelfont{
	    font-size: 0.85em;
    	font-weight: 700;
	 }
</style>
<!-- End JSP Necessary Tags -->

<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<div class="content">
	<div class="widget">
	<div class="widget-header">
				<h2><strong><spring:message code="viewPropDetails" text="View Property Details"/></strong></h2>				
				<div class="additional-btn">
					<apptags:helpDoc url="ViewPropertyDetail.html"></apptags:helpDoc>	
				</div>
	</div>

	<div class="widget-content padding">
	
		<div class="mand-label clearfix">
			<span><spring:message code="property.ChangeInAss.EnterPropertyNo" text="Enter Property No "/><i class="text-red-1"><spring:message code="property.ChangeInAss.OR" text="OR"/></i> <spring:message code="property.OldPropertyNo" text="Old Property No"/> <i class="text-red-1"><spring:message code="property.ChangeInAss.OR" text="OR"/></i> <spring:message code="ownersdetail.mobileno" text="Mobile No"/>
			<i class="text-red-1"><spring:message code="property.ChangeInAss.OR" text="OR"/></i> <spring:message code="property.OwnerName" text="Owner Name"/><i class="text-red-1"><spring:message code="property.ChangeInAss.OR" text="OR"/></i> <spring:message code="property.ward/Zone" text="ward/Zone"/> <i class="text-red-1"><spring:message code="property.ChangeInAss.OR" text="OR"/></i><spring:message code="property.location" text="Location"/>
			</span>
		</div>
 		
		<!-- Start Form -->
		<form:form action="ViewPropertyDetail.html"
					class="form-horizontal form" name="ViewPropertyDetail"
					id="ViewPropertyDetail">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
			<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
		<div class="form-group">
				<spring:message code="property.PleaseEnterPropertyNo" text="Please enter property no" var="propNo"/>
				<spring:message code="property.PleaseEnterOldPropertyNo" text="Please enter old property no" var="oldPropNo"/>
				<apptags:input labelCode="property.ChangeInAss.EnterPropertyNo" path="searchDto.proertyNo" cssClass="preventSpace" placeholder="${propNo}"></apptags:input>
				<%-- <apptags:input labelCode="property.ChangeInAss.oldpid" path="searchDto.oldPid" cssClass="preventSpace" placeholder="${oldPropNo}"></apptags:input> --%>
			<div id="showFlatNo">
					<label class="col-sm-2 control-label"><spring:message
							code="property.FlatNo" text="Flat No" /></label>
					<div class="col-sm-4">
						<form:select path="searchDto.flatNo" id="flatNo"
							class="form-control mandColorClass chosen-select-no-results">
							<form:option value="">
								<spring:message code='property.select' text="select"/>
							</form:option>
							<c:forEach items="${command.flatNoList}" var="flatNoList">
								<form:option value="${flatNoList}">${flatNoList}</form:option>
							</c:forEach>
						</form:select>
					</div>
		</div>	

		</div>
		
		
		<%-- <div class="form-group ">					
					<apptags:input labelCode="unitdetails.flatNo"
						path="searchDto.flatNo" cssClass="alphaNumeric"></apptags:input>
		</div> --%>
			
			<strong><p class="text-center text-small padding-bottom-10 text-red-1 "><spring:message code="property.OR"></spring:message></p></strong>
	
			<div class="form-group" >
				<spring:message code="property.PleaseEnterOwnerName" text="Please enter owner Name" var="occupierName"/>
				<spring:message code="property.PleaseEnterMobileNo" text="Please enter mobile no" var="mobileNo"/>
			
				<apptags:input labelCode="property.OwnerName" path="searchDto.occupierName" cssClass="preventSpace" placeholder="${occupierName}"></apptags:input>
				<apptags:input labelCode="ownersdetail.mobileno" path="searchDto.mobileno" cssClass="preventSpace hasMobileNo" maxlegnth="10" placeholder="${mobileNo}"></apptags:input>
			
			</div>
			
			<strong><p class="text-center text-small padding-bottom-10 text-red-1 "><spring:message code="property.OR"></spring:message></p></strong>
			
			<div class="form-group wardZone">

									<apptags:lookupFieldSet baseLookupCode="WZB" hasId="true"
										showOnlyLabel="false"
										pathPrefix="searchDto.assWard" isMandatory="true"
										hasLookupAlphaNumericSort="true"
										hasSubLookupAlphaNumericSort="true"
										cssClass="form-control required-control " showAll="false"
										showData="true" />
								</div>
			
			<div class="form-group Loc">
									<apptags:select cssClass="chosen-select-no-results"
										labelCode="property.location" items="${command.location}"
										path="searchDto.locId" isMandatory="true"
										isLookUpItem="true" selectOptionLabelCode="property.selectLocation">
									</apptags:select>

								</div>
			
      
			<div class="form-group">
			<label for="road-type" class="col-sm-2 control-label"><spring:message code="unitdetails.RoadType"/> </label>
				          <div>
				          <c:set var="baseLookupCode" value="RFT" />
						  <apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
				 							path="searchDto.propLvlRoadType" cssClass="form-control" 
				 							hasChildLookup="false" hasId="true" showAll="false" 
											selectOptionLabelCode="prop.selectRoadFactor" /> 
						 </div>
				<label for="Construction Type" class="col-sm-2 control-label"><spring:message code="unitdetails.constructiontype"/> </label>
				<c:set var="baseLookupCode" value="CSC" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="searchDto.assdConstruType"
									 cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false" isMandatory="true"
									selectOptionLabelCode="prop.selectConstType" />
									</div>

				<spring:eval
					expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getValueFromPrefixLookUp('UTY','ENV',${UserSession.organisation}).getOtherField()"
					var="otherField" />
				<c:if test="${otherField eq 'Y' }">
					<div class="form-group usageType">
						<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="searchDto.assdUsagetype"
							cssClass="form-control changeParameterClass mandColorClass"
							hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true" showAll="false"
							showData="true" columnWidth="10%" />
					</div>
				</c:if>


				<div class="text-center padding-bottom-10">
				
				<button type="button" class="btn btn-success" id="serchBtn"
						onclick="populateFlatNo(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.changeInAss.Search" />
					</button>
					
						<button type="button" class="btn btn-blue-2" id="serchBtnDirect"
									onclick="SearchButton(this)">
						<i class="fa fa-search"></i><spring:message code="property.changeInAss.Search"/>
						</button> 	
						
						<button type="button" class="btn btn-warning" onclick="Reset()">
							<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>	
							<spring:message code="property.reset" text="Reset"/>
						</button>
			</div>
			
			<%-- <div class="text-right">
				<label class="col-sm-11 labelfont"><spring:message code="unitdetails.totalBuildupArea" text="Total BuildUp Area"/>:</label>
				<div class="col-sm-1">${command.totalViewBuildArea} </div>
			</div>
			<div  class="text-right">
				<label class="col-sm-11 labelfont"><spring:message code="unitdetails.totalRV" text="Total RV "/>:</label>		
				<div  class="col-sm-1">${command.totalViewRV}</div>
				</div> --%>
			<table class="table table-striped table-bordered">
			<tbody>
				<tr>
					<th></th>
					<th><spring:message code="unitdetails.flatcount"/></th>
					<th><spring:message code="unitdetails.taxable"/></th>
					<th><spring:message code="unitdetails.RV"/></th>
				</tr>
			</tbody>
			<c:if test="${command.totalFloorCountResidential gt 0}">
			       <tr>
			           <td><spring:message code="unitdetails.totalResidentila"/></td>
			           <td class="text-center">${command.totalFloorCountResidential}</td> 
			           <td class="text-center">${command.totalBuildupAreaResidential}</td>
			           <fmt:formatNumber type="number" value="${command.totalRvResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvResidential"/>
	                   <td class="text-center">${totalRvResidential}</td> 
			       </tr>
			   </c:if>
			   <c:if test="${command.totalFloorCountNonResidential gt 0}">    
			        <tr>
			           <td><spring:message code="unitdetails.totalNonResidential"/></td>
			           <td class="text-center">${command.totalFloorCountNonResidential}</td> 
			           <td class="text-center">${command.totalBuildupAreaNonResidential}</td> 
			           <fmt:formatNumber type="number" value="${command.totalRvNonResidential}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="totalRvNonResidential"/>
	                   <td class="text-center">${totalRvNonResidential}</td> 
			       </tr>
			   </c:if> 
			   		<tr>
			           <td><spring:message code="propertyTax.Total"/></td>
			           <td class="text-center">${command.grandTotalFloorCount}</td> 
			           <td class="text-center">${command.grandTotalBuildUpArea}</td> 
			           <fmt:formatNumber type="number" value="${command.grandTotalRv}" groupingUsed="false" maxFractionDigits="" minFractionDigits="" var="grandTotalRv"/>
	                   <td class="text-center">${grandTotalRv}</td> 
			       </tr>
			</table>
			<div class="form-group"></div>	
			<div class="clear">
			<apptags:jQgrid id="ViewPropertyDetails"
					url="ViewPropertyDetail.html?SEARCH_GRID_RESULTS" mtype="post"
					gridid="gridViewPropertyDetail"
					colHeader="propertydetails.PropertyNo.,property.FlatNo,unitdetails.floorno,property.OwnerName,unitdetails.usagefactor,unitdetails.legal,unitdetails.taxable,unitdetails.RV,property.mobile"
					colModel="[
								{name : 'proertyNo',index : 'proertyNo',editable : false,sortable : true,search : true, align : 'center', width: 80},
								{name : 'flatNo',index : 'flatNo',editable : false,sortable : false,search : false, align : 'center', width: 60},
								{name : 'floorNo',index : 'floorNo',editable : false,sortable : true,search : true, align : 'center', width: 80},
								{name : 'occupierName',index : 'occupierName',editable : false,sortable : false,search : true, align : 'center', width: 180},
								{name : 'proAssdUsagetypeDesc',index : 'proAssdUsagetypeDesc',editable : false,sortable : false,search : true, align : 'center', width: 80},	
								{name : 'legalStatus',index : 'legalStatus',editable : false,sortable : false,search : true, align : 'center', width: 60},
								{name : 'buildupArea',index : 'buildupArea',editable : false,sortable : false,search : true, align : 'center', width: 80},
								{name : 'assdRV',index : 'assdRV',editable : false,sortable : false,search : true, align : 'center', width: 60},
								{name : 'mobileno',index : 'mobileno',editable : false,sortable : false,search : false, align : 'center', width: 60}	
								]"
					height="200" caption="" isChildGrid="false" hasActive="false"
					propViewDet="true" hasEdit="false" hasDelete="false"
					loadonce="false" sortCol="rowId" showrow="true" />
						
				</div>	
			
			
		</form:form>
	</div>
	</div>
</div>