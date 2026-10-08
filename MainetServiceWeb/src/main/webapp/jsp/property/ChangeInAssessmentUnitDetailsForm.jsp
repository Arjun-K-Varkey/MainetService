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
<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
<script src="js/property/changeUnitDetails.js" type="text/javascript"></script>
<script src="js/property/selfAssessment.js" type="text/javascript"></script>
<script src="js/property/unitSpecificAdditionalInfo.js" type="text/javascript"></script>
<script src="js/property/unitDetails.js" type="text/javascript"></script>
<style>
	.overflow-y-hidden {
		overflow-y: hidden;
	}
	table#unitDetailTable tbody tr th,
	table#unitDetailTable tbody tr td{
		white-space: nowrap;
	}
	.width-100 {
		width: 100px !important;
	}
	.tooltip {
    background-color:#000;
    border:1px solid #fff;
    padding:10px 15px;
    width:200px;
    display:none;
    color:#fff;
    text-align:left;
    font-size:15px;
 
    /* outline radius for mozilla/firefox only */
    -moz-box-shadow:0 0 10px #000;
    -webkit-box-shadow:0 0 10px #000;
}
.pagination-list{
	text-align:right;
	margin:1rem 0rem;
}
.pagination-list li{
	display:inline;
}
.pagination-list li.active button {
    background-color: #007bff;
    color: white;
    font-weight: bold;
}
</style>
<div class="accordion-toggle">
<h4 class="margin-top-0 margin-bottom-10 panel-title" id="scrollToDiv">
<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
</h4>
<form:hidden  path="noOfDaysEditableFlag"  id="noOfDaysEditableFlag"/>
<form:hidden path="ConstructFlag" id="ConstructFlag"/>
<form:hidden path="countOfRow" id="countOfRow"/>
<form:hidden path="selfAss" id="selfAss"/>
<div class="panel-collapse collapse in" id="UnitDetail">
		<c:if test="${not empty command.pageNoList}">
			<ul class="pagination-list">
				
				<c:forEach var="pageNo" items="${command.pageNoList}" varStatus="status" >
					<c:choose>
						<c:when test="${pageNo eq fn:length(command.pageNoList)}">
								<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
									<button type="button" class="btn btn-success btn-submit"
													onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
													<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) + 1} <spring:message code="and.more" text="and more..." />
												</button>
								</li>
						</c:when>
						<c:otherwise>
							<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
								<button type="button" class="btn btn-success btn-submit"
												onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
												<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) + 1} - ${pageNo*75}
											</button>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</c:if>
		<c:choose> 
		<c:when test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"> 
		<%-- <div class="form-group" style="margin-left: 0">
 			 		<spring:message code="property.propFloorDetail"/>
 		</div> --%> 
		<div class="table-responsive overflow-y-hidden"> 
		<c:set var="d" value="0" scope="page" />			
                <table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>                
                        <tr>
                        	<th width="13%" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="10%" class="required-control"><spring:message code="unitdetails.unitSrNo"/></th>
	                       	<th width="10%" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
 							<th width="13%" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            <%-- <th width="15%" class="required-control"><spring:message code="unitdetails.ConstcompletionDate"/></th> --%>
                            <th width="10%" class="required-control" for="occupancy"><spring:message code="unitdetails.occupancy"/></th>
                            <%-- <th width="15%"><spring:message code="" text="Last Assessment Date"/></th> --%>
                            <th width="12%" class="required-control" for="occupierName${status.count-1}" ><spring:message code="unitdetails.OccupierName"/></th>
                            <th class="required-control"><spring:message code="unitdetails.legal.illegal" text="Is Legal/Illegal?"/></th>
                            <th width="10%"><spring:message code="unitdetails.user.charges" text="User Charges Category"/></th>
                     	 	<th width="15%" class="required-control"><spring:message code="unitdetails.areaOrRv" text="Area/RV Based"/></th>
                            <th width="15%" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix=""
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control" showAll="false" hasTableForm="true" showData="false" columnWidth="13%" />
							<th for="actualRent${status.count-1}" class="col-sm-2 "><spring:message	code="unitdetails.actualRent"/></th>
							<th width="10%" class="required-control"><spring:message code="unitdetails.carpetArea" /></th>
                     	 	<th width="10%" class="required-control"><spring:message code="unitdetails.taxable"/></th>
                     	 	<th width="15%" class=""><spring:message code="unitdetails.RV" text="RV"/></th>
							<th colspan="3"><input type="button" class="btn btn-success btn-sm unit" onclick="addCFCIA()"  value="+"></input></th>
							</tr>
                          
       					<c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                                            
                    	<tr class="firstUnitRow">                     
                      		 <td width="13%">
                      			<c:set value="" var="year"></c:set>                                  	
                        	    <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="year${status.count-1}" class="form-control disabled text-center mandColorClass displayYearList">
		  						<form:option value="0" label="Select Year"></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" selected="${year == yearMap.key ? 'selected' : ''}">${yearMap.value}</form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="hiddenYear${status.count-1}"/>		 						    
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].firstAssesmentDate" id="hiddenFirstAssesmentDate${status.count-1}"/>			
		 					</td>
                        	 
                        	 <td width="10%">
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
              				 </td>
						
						
							<td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo" type="text" 
											class="form-control  mandColorClass text-right" id="flatNo${status.count-1}" data-rule-required="true" 
											readonly="${command.getExistingFlatListForDisable().contains(unitDetails.flatNo)}" /></td>
							
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo" cssClass="form-control changeParameterClass mandColorClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="Select Floor No" isMandatory="true" hasTableForm="true" />
							</td>
						
							<td>
								<c:set var="baseLookupCode" value="OCS" /> 
				 					<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}" 
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control changeParameterClass " 
										hasChildLookup="false" hasId="true" showAll="false"
					 					selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/> 
							</td>
							
							<td>
 			          		   	<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" maxlength="500"/>								
							</td>
						
							<td>
                           <form:select class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].legal" id="legal${status.count-1}">
							<form:option value="">
								<spring:message code="" text="Select"></spring:message>
							</form:option>
								<form:option value="Legal"></form:option>
								<form:option value="Illegal"></form:option>
								<form:option value="Illegal(Core)"></form:option>
							</form:select>
                           </td>
                           <td> 
                            	<c:set var="baseLookupCode" value="UCT" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].userChargesType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="property.sel.optn" isMandatory="false" hasTableForm="true"/>
							</td>
                           <td>
                           <form:select class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].areaOrRvBasedChange" id="areaOrRvBasedChange${status.count-1}">
							<form:option value="">
								<spring:message code="unit.select" text="Select"></spring:message>
							</form:option>
								<form:option value="RV Based"></form:option>
								<form:option value="Area Based"></form:option>
							</form:select>
                           </td>
                            <td> 
                            	<c:set var="baseLookupCode" value="CSC" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdConstruType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="Select Construction Type" isMandatory="true" hasTableForm="true"/>
							</td>
							<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control " showAll="false" hasTableForm="true" showData="true" columnWidth="10%"/>
							
							<td><form:input	title="Monthly Rent *12-10% of standard deduction"
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
								type="text"	class="form-control has2Decimal actualRentToolTip text-right" id="actualRent${status.count-1}" 
								onkeypress="return hasAmount(event, this, 15, 2)" placeholder="0.0" />
							</td>
							
							<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" disabled="true"/></td>
							<td width="150"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" onchange="getAmountFormatInDynamic((this),'proAssdBuildupArea')"  disabled="true"/></td>
                           	<td width="15%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdRv" type="text" 
											class="form-control  preventSpace has2Decimal mandColorClass text-right width-100 " id="assdRv${status.count-1}"   /></td>
	                         <%-- <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-${status.count-1}" aria-expanded="false" aria-controls="group-of-rows-${status.count-1}"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td> --%>	                           
							<td> <input type="button" id="duppliquer${status.count-1}"  class="btn btn-success btn-submit duppliquer"  onclick="duppliquerDet(${status.count-1})" value="<spring:message code="property.copy.details" text="Copy" />"></td>
							 <td class="text-center">
							   <input type="button" id="delete_0"   class="btn btn-danger btn-xs delete"  onclick="removeRowCIA('0')" value=" -"></input>  </td>
							<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails(${status.count-1},this)" value="<spring:message code="property.room" text="Room" />"></td>
                       	</tr> 
                    
				</c:forEach>               		
                </tbody>
                </table>                  
</div>                        
 			  
				
 		</c:when>
 		
 		<c:otherwise>	
 			 		
				<div class="table-responsive overflow-y-hidden" ><!-- style="overflow:scroll" -->
				<c:set var="d" value="0" scope="page" />
			
                <table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>
                        <tr>
                        	<th width="10%" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="3%" class="required-control"><spring:message code="unitdetails.unitno"/></th>
							<th width="8%" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
                            <th width="12%" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            <th width="13%" class="required-control" for="occupancy"><spring:message code="unitdetails.occupancy"/></th> 
                            <th width="12%" class="required-control" for="occupierName${status.count-1}" ><spring:message code="unitdetails.OccupierName"/></th>
                            <th class="required-control"><spring:message code="unitdetails.legal" text="Is Legal/Illegalll?"/></th>
                   	 	    <th width="15%" class="required-control"><spring:message code="unitdetails.areaOrRv" text="Area/RV Based"/></th>
                            <th width="10%" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix=""
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control mandColorClass" showAll="false" hasTableForm="true" showData="false" columnWidth="10%" ></apptags:lookupFieldSet>
                     	 	
                   	 	    <th width="10%" class="required-control"><spring:message code="unitdetails.carpetArea" text="Carpet Area"/></th>
                   	 	    <th width="10%" class="required-control"><spring:message code="unitdetails.taxdetail"/></th>
                   	 	    <th width="15%" class=""><spring:message code="unitdetails.RV" text="RV"/></th>
                        	<th colspan="3"><input type="button" class="btn btn-success btn-sm unit" onclick="addCFCIA()" value="+"></input></th>
                        
                        </tr>
                                              
                    	<tr class="firstUnitRow">
                   
                      		 <td width="13%">                                       	
                        	    <form:select path="" id="year0" class="form-control disabled text-center mandColorClass displayYearList" data-rule-required="true" disabled="true">
		  						<form:option value=""><spring:message code="prop.selectYear"/></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].faYearId" id="hiddenYear0"/>
<%-- 		 					<form:hidden path="leastFinYear" id="minYear"/>	 --%>
		 						    
		 					</td>
                        	 
                        	 <td>
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUnitNo" type="text" class="form-control  text-center unit required-control" id="unitNo0" value="1" data-rule-required="true" readonly="true" />
              				</td>
						
							<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].flatNo" type="text" 
										class="form-control  mandColorClass text-right" id="flatNo0" data-rule-required="true"  /></td>
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdFloorNo" cssClass="form-control changeParameterClass floorNoClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="prop.selectFloorNo" isMandatory="true" hasTableForm="true" />
										
										<c:forEach
											items="${command.getLevelData(baseLookupCode)}" var="lookUp">
											<c:if test="${lookUp.lookUpCode eq 'OTH' }">
												<input type="hidden" value="${lookUp.lookUpId}" id="hiddenOther">
											</c:if>
										</c:forEach>
							</td>
							<td>
								<c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdOccupancyType" cssClass="form-control changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/> 
		 						
							</td>
							
							<td>
								<div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierName" class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" maxlength="500"/></div>
							</td>
							<td>
                           <form:select class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].legal" id="legal0">
							<form:option value="">
								<spring:message code="" text="Select"></spring:message>
							</form:option>
								<form:option value="Legal"></form:option>
								<form:option value="Illegal"></form:option>
								<form:option value="Illegal(Core)"></form:option>
							</form:select>
                           </td>
                           <td> 
                            	<c:set var="baseLookupCode" value="UCT" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].userChargesType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="property.sel.optn" isMandatory="false" hasTableForm="true"/>
							</td>
                           <td>
                           <form:select class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].areaOrRvBasedChange" id="areaOrRvBasedChange0">
							<form:option value="">
								<spring:message code="unit.select" text="Select"></spring:message>
							</form:option>
								<form:option value="RV Based"></form:option>
								<form:option value="Area Based"></form:option>
							</form:select>
                           </td>
                            <td> 
                            	<c:set var="baseLookupCode" value="CSC" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdConstruType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="prop.selectSelectConstructionType" isMandatory="true" hasTableForm="true"/>
							</td>
							
           					<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
															showOnlyLabel="false"
															pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
															isMandatory="true" hasLookupAlphaNumericSort="true"
															hasSubLookupAlphaNumericSort="true"															
															cssClass="form-control required-control " showAll="false"
															hasTableForm="true" showData="true" />
							<td><form:input	title="Monthly Rent *12-10% of standard deduction"
								path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].actualRent"
								type="text"	class="form-control has2Decimal actualRentToolTip text-right" id="actualRent0" 
								onkeypress="return hasAmount(event, this, 15, 2)" placeholder="0.0" />
							</td>
							
							<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea0" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" disabled="true"/></td>
							
							<td width="150"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea0" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" onchange="getAmountFormatInDynamic((this),'proAssdBuildupArea')"  disabled="true"/></td>
                            <td width="15%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0}].assdRv" type="text" 
											class="form-control  preventSpace has2Decimal mandColorClass text-right width-100 " id="assdRv0"   /></td>
											<td> <input type="button" id="duppliquer0" class="btn btn-success btn-submit duppliquer" onclick="duppliquerDet('0')" value="<spring:message code="property.copy.details" text="Copy" />"></td>
						<td class="text-center"> <input type="button" id="delete_0"   class="btn btn-danger btn-xs delete"  onclick="removeRowCIA('0')" value=" -"></input></td>
						<td><input type="button" id="addRoom0" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails('0',this)" value="<spring:message code="property.room" text="Room" />"></td>																												
							
                       	</tr> 
                    
			                 		
                  </tbody>
                </table>                
			</div>                        
				
		</c:otherwise>
</c:choose>
</div>
</div>


<c:if test="${not empty command.pageNoList}">
<ul class="pagination-list">
	
	<c:forEach var="pageNo" items="${command.pageNoList}" varStatus="status" >
		<c:choose>
			<c:when test="${pageNo eq fn:length(command.pageNoList)}">
					<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
						<button type="button" class="btn btn-success btn-submit"
										onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
										<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) +1} <spring:message code="and.more" text="and more..." />
									</button>
					</li>
			</c:when>
			<c:otherwise>
				<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
					<button type="button" class="btn btn-success btn-submit"
									onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
									<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) + 1} - ${pageNo*75}
								</button>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
</c:if>

<!-- D#99306 -->
<c:if test="${command.getFactorNotApplicable() ne 'NA'}"> 
<div class="accordion-toggle">           
<h4 class="margin-top-0 margin-bottom-10 panel-title">
<a data-toggle="collapse" href="#unitSpecificInfo"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 </h4>
<div class="panel-collapse collapse in" id="unitSpecificInfo">      
 			<div class="form-group">
 			<c:choose>
 			<c:when test="${not empty command.getProvAsseFactDtlDto()}">
 			 
 			<c:set var="baseLookupCode" value="FCT" /> 
            <c:forEach items="${command.getLevelData(baseLookupCode)}" var="factor" varStatus="status"><!--Question loop  -->
	            <label class="col-sm-8 control-label padding-top-0" ><spring:message code="property.Is" text="Is "/>${factor.lookUpDesc}<spring:message  code="property.Applicable" text=" Applicable?"/></label>
				<c:set value='${factor.lookUpCode}' var="facCode"></c:set>
				<c:set value='${factor.lookUpId}' var="factId"></c:set>
				
				<div class="col-sm-4">
				<label for="Yes" class="radio-inline">	
				<form:radiobutton class="factorSet"  path="provisionalAssesmentMstDto.proAssfactor[${status.count-1}]" value="Y"  id="Yes" onchange="showAddInfo('${facCode}','${status.count-1}')"/>
				<spring:message code="unitdetails.Yes"></spring:message></label>
				<label for="No" class="radio-inline">
				<form:radiobutton class="factorStatus" path="provisionalAssesmentMstDto.proAssfactor[${status.count-1}]" value="N"  checked="checked" id="No" onchange="showAddInfo('${facCode}','${status.count-1}')"/>
				<spring:message code="Unitdetails.No"></spring:message></label>
				</div> 
		
   			<c:set var="p1" value="1" > </c:set>
   			<div id="${facCode}" class="eachFact"> 
	    	<c:forEach items="${command.getProvAsseFactDtlDto()}" var="factableLoop" varStatus="status">    <!--loop to check weather data is available for question   -->
		   		<c:if test="${factableLoop.assfFactorId eq factId}">
	           	<c:if test="${p1 eq 1}">
	           	<div id="${facCode}" class="eachFact"> 
		          	<div class="padding-15 unitSpecificInfo">
		            <table id="unitSpecificInfoTable${facCode}" class="table table-striped table-bordered ">
		                    <tbody>
		                        <tr>
		                        	<th width="10%" class="required-control"><spring:message code="unit.ApplicableUnitNo"/></th>
		                            <th width="25%" class="required-control"><spring:message code="unit.FactorValue"/></th>
		                            <th width="25%" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                            		<th width="30%" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                            		<th style="width: 30%" class=""><spring:message code="property.Amount" text="Amount"/></th>
		                            <th width="10%"><spring:message code="unit.Add/Delete"/></th>                       	 
		                        </tr>
		                        
				       		<c:forEach items="${command.getProvAsseFactDtlDto()}" var="details" varStatus="status">    
				            <c:if test="${details.assfFactorId  eq factId}">
		                        
		                        <tr class="specFact">  
			                       	<td >    
			                       	<form:select id="unitNoFact0" class="form-control mandColorClass selectUnit" path="provAsseFactDtlDto[${status.count-1}].unitNoFact" onchange="resetFactorValue(this,0);">                   		                        		
											<form:option value="0">Select Option</form:option>
											<form:option value="ALL">All</form:option>
											<form:option value="1">1</form:option>
											<c:if test="${command.maxUnit gt 0}">
											<c:forEach  var="i" begin="1" end="${command.maxUnit}">
												<form:option value="${i}">${i}</form:option>	
											</c:forEach>
											</c:if>
											<%-- <form:option value="${details.unitNoFact}">${details.unitNoFact}</form:option>  --%>								
									</form:select>
			                        </td> 
			                        
			                                        
		                        	<td >
		                        		<form:hidden path="provAsseFactDtlDto[${status.count-1}].assfFactorId" value="${factId}"/>
		                        	 	<form:hidden path="provAsseFactDtlDto[${status.count-1}].factorValueCode" id="factPref" value="${facCode}"/>
		                        	
										<c:set var="baseLookupCode" value="${details.getFactorValueCode()}" />
										<form:select path="provAsseFactDtlDto[${status.count-1}].assfFactorValueId" 
										id="FactorValue0" cssClass="form-control changeParameterClass mandColorClass factor" onchange="enabledisable(this,0);">
										 <form:option value="0" code="">select</form:option>
										<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
										<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}" >${lookUp.lookUpDesc}</form:option>
										</c:forEach>
										
										</form:select>
									</td>
									
									<td >
											<div class="input-group">
													<form:input
															path="provAsseFactDtlDto[${status.count-1}].factorDate"
															class="form-control datepicker2  dateClass addColor hasDatepicker"
															id="factorDate0" maxlength="10"
															placeholder="DD/MM/YYYY" autocomplete="off" />
													<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
									</td>

									<td ><form:input
												path="provAsseFactDtlDto[${status.count-1}].factorRemark"
												type="text" class="form-control  preventSpace"
												id="factorRemark0" />
									</td>
									<td><form:input
											path="provAsseFactDtlDto[${status.count-1}].factorAmount" type="text"
											class="form-control  preventSpace has2Decimal" id="factorAmount0" />
									</td>
									<td class="text-center">
										<a href="javascript:void(0);" title="Add" class="unitSpecificAdd btn btn-success btn-sm" onclick="addUnitRow('0')"><i class="fa fa-plus-circle"></i></a>								
		 								<a href="javascript:void(0);" title="Delete" class="unitSpecificRem btn btn-danger btn-sm"><i class="fa fa-trash-o"></i></a>  
										
									</td>
								<%-- 	<form:hidden path="unitStatusCount" id="UnitInfotblCount"/> --%>
		                        </tr>
		                        		</c:if>
		     				</c:forEach>
		                    </tbody>
		            </table>
				</div>
	           </div>   
	           </c:if>
         		<c:set var="p1" value="${p1+1}" > </c:set>            
           		</c:if>
            </c:forEach>
            </div>
            </c:forEach>
 			 </c:when>
			
			<c:otherwise>					
	            <c:set var="baseLookupCode" value="FCT" /> 
	            <c:set var="d" value="0" />
	            <c:forEach items="${command.getLevelData(baseLookupCode)}" var="factor" varStatus="status">
	            	<!-- if check  lookUpCode is NA then ignore -->
	            	<c:if test="${factor.lookUpCode ne 'NA' }">
	            	
		          	<label class="col-sm-8 control-label padding-top-0" ><spring:message code="property.Is" text="Is "/>${factor.lookUpDesc} <spring:message  code="property.Applicable" text=" Applicable?"/></label>
					<c:set value='${factor.lookUpCode}' var="facCode"></c:set>
					
					<div class="col-sm-4 factorQes" >
					<label for="Yes" class="radio-inline">	
					<form:radiobutton class="factorSet selectFactor" path="provisionalAssesmentMstDto.proAssfactor[${d}]" value="Y"  id="Yes"  onchange="showAddInfo('${facCode}','${d}')"/>
					<spring:message code="unitdetails.Yes"></spring:message></label>
					<label for="No_" class="radio-inline">
					<form:radiobutton class="factorStatus selectFactor" path="provisionalAssesmentMstDto.proAssfactor[${d}]" value="N"  checked="checked"   id="No_"  onchange="showAddInfo('${facCode}','${d}')"/>
					<spring:message code="Unitdetails.No"></spring:message></label>
					</div>  
					  
		           	<div id="${facCode}" class="eachFact"> 
		          
		           	</div>
		           	<c:set var="d" value="${d+1}" />  
		           	</c:if>          
	            </c:forEach>
	          <%--   <form:hidden path="unitStatusCount" id="factorValue"/> --%>
            </c:otherwise>
            </c:choose>
            </div>
</div>
</div>
</c:if>