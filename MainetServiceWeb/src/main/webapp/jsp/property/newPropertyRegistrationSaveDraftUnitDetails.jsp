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
<script src="js/property/unitDetails.js" type="text/javascript"></script>
<script src="js/property/unitSpecificAdditionalInfo.js" type="text/javascript"></script>
<script type="text/javascript" src="js/property/ownershipDetailsForm.js"></script>
<style>
.addColor{
	background-color: #fff !important
}
 #unitDetailTable label{
	font-size:0.7rem;
 }

.pagination-list {
	text-align: right;
	margin: 1rem 0rem;
}

.pagination-list li {
	display: inline;
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
<form:hidden path="ConstructFlag" id="ConstructFlag"/>

<!-- newly added -->
<form:hidden path="countOfRow" id="countOfRow"/>
<form:hidden path="unitDetailsCheck" id="unitDetailsCheck" />
<!-- newly added -->

<div class="panel-collapse collapse in" id="UnitDetail">
<c:if test="${not empty command.pageNoList}">
			<ul class="pagination-list">
				
				<c:forEach var="pageNo" items="${command.pageNoList}" varStatus="status" >
					<c:choose>
						<c:when test="${pageNo eq fn:length(command.pageNoList)}">
								<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
									<button type="button" class="btn btn-success btn-submit"
													onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
													${((pageNo-1)*75) + 1} <spring:message code="and.more" text="and more..." />
												</button>
								</li>
						</c:when>
						<c:otherwise>
							<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
								<button type="button" class="btn btn-success btn-submit"
												onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
												${((pageNo-1)*75) + 1} - ${pageNo*75}
											</button>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</c:if>
		<c:choose>
		<c:when test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}"> 
		<div class="table-responsive" ><!-- style="overflow:scroll" -->
		<c:set var="d" value="0" scope="page" />			
                <table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                 <form:hidden path="copyOfRow" class="copiedrow" id="copiedrow" value="" />
                    <tbody id="tableToModify">                        
                        <tr>
                        	<th width="10%" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="5%" class="required-control"><spring:message code="unitdetails.unitno"/></th>
							<th width="8%" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
                            <th width="12%" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            
                            <th class="electricDetailsColumn"><spring:message code="property.electno" text="Electricity Billing Unit Number"/></th>
				            <th class="electricDetailsColumn"><spring:message code="property.electConsNo" text="Electricity Consumer Number"/></th>
				            <th class="electricDetailsColumn"><spring:message code="property.electMeterNo" text="Electricity Meter Number"/></th>
				             
                            <c:if test="${command.getConstructFlag() eq 'Y' }">
                             <th width="13%" class="required-control" for="occupancy"><spring:message code="unitdetails.occupancy"/></th> 
                             </c:if>
                            <th width="12%" class="required-control" for="occupierName${status.count-1}" ><spring:message code="unitdetails.OccupierName"/></th>
                            <th class="required-control"><spring:message code="unitdetails.legal.illegal" text="Is Legal/Illegal?"/></th>
                            <th width="10%"><spring:message code="unitdetails.user.charges" text="User Charges Category"/></th>
                            <th width="10%" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control" showAll="false" hasTableForm="true" showData="false" columnWidth="10%" />
							<th for="actualRent${status.count-1}" class="col-sm-2 "><spring:message
													code="unitdetails.actualRent"/></th>
							
							<th width="12%" class="required-control"><spring:message
										code="unitdetails.carpetArea" text="Carpet Area" /></th>
							
                     	 	<th width="12%" class="required-control"><spring:message code="unitdetails.taxdetail"/></th>                     	 	
                        	 
                          	<th colspan="3">
                        	<input type="button" class="btn btn-success btn-sm unit" onclick="addRow()"  value="+"></input>
                        	 </th>
                        
                        </tr>
                          
       					<c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                                            
                    	<tr class="firstUnitRow" id="rowToClone">
                  
                      		 <td>                                       	
                        	    <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="year${status.count-1}" class="form-control disabled text-center mandColorClass displayYearList" disabled="true">
		  						<form:option value=""><spring:message code="prop.selectSelectYear"/></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="hiddenYear${status.count-1}"/>		 						    
		 					</td>
                        	 
                        	 <td>
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
              				</td>
												
							<td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo" type="text" 
											class="form-control  mandColorClass text-right dupFlatNo" id="flatNo${status.count-1}" data-rule-required="true"  onblur="getFlatValidation(this)"/></td>
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo" cssClass="form-control changeParameterClass mandColorClass floorNoClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="prop.selectFloorNo" isMandatory="true" hasTableForm="true" />
										
										<c:forEach
											items="${command.getLevelData(baseLookupCode)}" var="lookUp">
											<c:if test="${lookUp.lookUpCode eq 'OTH' }">
												<input type="hidden" value="${lookUp.lookUpId}" id="hiddenOther">
											</c:if>
										</c:forEach>
						</td>
						
						<td class="electricDetailsColumn">
			                <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricBillUnitNo" type="text" class="form-control hasNumber text-center" id="electricBillUnitNo${status.count-1}" maxlength="4"/>
			            </td>
			            <td class="electricDetailsColumn">
			                <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricConsumerNo" type="text" class="form-control hasNumber text-center" id="electricConsumerNo${status.count-1}" maxlength="12"/>
			            </td>
			            <td class="electricDetailsColumn">
			                <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricMeterNo" type="text" class="form-control hasNumber text-center" id="electricMeterNo${status.count-1}" maxlength="11"/>
			            </td>
			                
						<c:if test="${command.getConstructFlag() eq 'Y' }">
							<td>
								<c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/> 
		 						
								
							</td>
							</c:if>
							<td>
 			          		  <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" 
 			          		  	class="form-control alphaNumeric preventSpace" id="occupierName${status.count-1}" maxlength="500"/></div>
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
                            	<c:set var="baseLookupCode" value="CSC" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdConstruType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="prop.selectConstType" isMandatory="true" hasTableForm="true"/>
							</td>
							<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
															showOnlyLabel="false"
															pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
															isMandatory="true" hasLookupAlphaNumericSort="true"
															hasSubLookupAlphaNumericSort="true"															
															cssClass="form-control required-control " showAll="false"
															hasTableForm="true" showData="true" />
					
                           	<td><form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
													type="text"
													class="form-control has2Decimal  text-right"
													id="actualRent${status.count-1}" 
													onkeypress="return hasAmount(event, this, 15, 2)" placeholder="0.0" /></td>
                           	
                           	<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)"  disabled="true"/></td>
							
							<td width="150"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" disabled="true"/></td>
                           
							 <td> <input type="button" id="duppliquer${status.count-1}"  class="btn btn-success btn-submit duppliquer"  onclick="duppliquerDet(${status.count-1})" value="<spring:message code="property.copy.details" text="Copy" />"></td>
							  <td class="text-center"><!-- <a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete" id="deleteRow_"><i class="fa fa-minus-circle"></i></a> -->
							   <input type="button" id="delete_${status.count-1}"   class="btn btn-danger btn-xs delete"  onclick="removeRow(${status.count-1})" value="-"></input>
							 </td>
							<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails(${status.count-1},this)" value="<spring:message code="property.room" text="Room" />"></td>
							
                       	</tr> 
                    		
				</c:forEach>               		
                </tbody>
                </table>                
		</div>                        
 			 	 
 		</c:when>
 
 			 	<c:otherwise>	
 			 		
				<div class="table-responsive" ><!-- style="overflow:scroll" -->
				<c:set var="d" value="0" scope="page" />
			
                <table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>
                        <tr>
                        	<th width="10%" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="3%" class="required-control"><spring:message code="unitdetails.unitno"/></th>
							<th width="8%" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
                            <th width="12%" class="required-control"><spring:message code="unitdetails.floorno"/></th>              
                       
                              <c:if test="${command.getConstructFlag() eq 'Y'}">
                      
                              <th width="13%" class="required-control" for="occupancy"><spring:message code="unitdetails.occupancy"/></th> 
                             </c:if>
                            <th width="12%" class="required-control" for="occupierName${status.count-1}" ><spring:message code="unitdetails.OccupierName"/></th>
                            <th class="required-control"><spring:message code="unitdetails.legal" text="Is Legal/Illegalll?"/></th>
                            <th width="10%" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix=""
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control mandColorClass" showAll="false" hasTableForm="true" showData="false" columnWidth="10%" ></apptags:lookupFieldSet>
                     	 	
                   	 	    <th width="12%" class="required-control"><spring:message code="unitdetails.carpetArea" text="Carpet Area"/></th>
                   	 	    <th width="12%" class="required-control"><spring:message code="unitdetails.taxdetail"/></th>
                        	 
                        	<th colspan="3">
                        	<input type="button" class="btn btn-success btn-sm unit" onclick="addRow()" value="+"></input>
                        	 </th>
                        
                        </tr>
                                              
                    	<tr class="firstUnitRow">
                   
                      		 <td>                                       	
                        	    <form:select path="" id="year0" class="form-control disabled text-center mandColorClass displayYearList" data-rule-required="true">
		  						<form:option value=""><spring:message code="prop.selectYear"/></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].faYearId" id="hiddenYear0"/>
		 						    
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
							
							<td class="electricDetailsColumn">
			                    <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricBillUnitNo" type="text" class="form-control hasNumber text-center" id="electricBillUnitNo${status.count-1}" maxlength="4"/>
			                </td>
			                <td class="electricDetailsColumn">
			                    <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricConsumerNo" type="text" class="form-control hasNumber text-center" id="electricConsumerNo${status.count-1}" maxlength="12"/>
			                </td>
			                <td class="electricDetailsColumn">
			                    <form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].electricMeterNo" type="text" class="form-control hasNumber text-center" id="electricMeterNo${status.count-1}" maxlength="11"/>
			                </td>
			                
						<c:if test="${command.getConstructFlag() eq 'Y'}">
							<td>
								<c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdOccupancyType" cssClass="form-control changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/> 
							</td>
							</c:if>
							<td>
								<div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierName" 
									class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" maxlength="500"/></div>   		
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
					
							<td width="150"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea0" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" disabled="true"/></td>
                           
							<td> <input type="button" id="duppliquer0" class="btn btn-success btn-submit duppliquer" onclick="duppliquerDet('0')" value="<spring:message code="property.copy.details" text="Copy" />"></td>
						<td class="text-center"><!-- <a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete" id="deleteRow_"><i class="fa fa-minus-circle"></i></a>  -->
							   <input type="button" id="delete_0"   class="btn btn-danger btn-xs delete"  onclick="removeRow('0')" value=" -"></input>  </td>
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
													${((pageNo-1)*75) + 1} <spring:message code="and.more" text="and more..." />
												</button>
								</li>
						</c:when>
						<c:otherwise>
							<li class="hidden-print ${pageNo eq command.currentPage ? 'active' : ''}">
								<button type="button" class="btn btn-success btn-submit"
												onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
												${((pageNo-1)*75) + 1} - ${pageNo*75}
											</button>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</c:if>

<c:if test="${command.getFactorNotApplicable() ne 'NA'}"> 
<div class="accordion-toggle">           
<h4 class="margin-top-0 margin-bottom-10 panel-title">
<a data-toggle="collapse" href="#unitSpecificInfo"><spring:message code="unit.UnitSpecificAddiInfo"/></a>
 </h4>
<div class="panel-collapse collapse in" id="unitSpecificInfo">      
 			<div class="form-group">
    		<c:set var="p1" value="0" > </c:set>																																							
            <c:forEach items="${command.getDisplayfactorMap()}" var="entry" varStatus="key">
            <c:set var="taxValue" value="${entry.value}"/>
            <c:set var="prefix"  value='FCT' />
            <c:set var="id"  value="${entry.key}" />
            <c:set var="lookup" value="${command.getdesc(id,prefix)}" />
            <!-- D#99306 -->
            <c:if test="${lookup.lookUpCode ne 'NA' }">
	        <label class="col-sm-8 control-label padding-top-0" ><spring:message code="property.Is" text="Is "/>${lookup.lookUpDesc} <spring:message  code="property.Applicable" text=" Applicable?"/></label>

          	<c:if test="${taxValue eq null}">
             <apptags:radio cssClass="col-sm-4 addInfo" radioLabel="Yes,No" radioValue="Y,N" labelCode="" path="provisionalAssesmentMstDto.proAssfactor[${key.count-1}]"  defaultCheckedValue="N" changeHandler="showAddInfo('${lookup.lookUpCode}','${key.count-1}')"></apptags:radio>	          
             																									
             </c:if>
             <c:if test="${taxValue ne null}">
             <apptags:radio cssClass="col-sm-4 addInfo" radioLabel="Yes,No" radioValue="Y,N" labelCode="" path="provisionalAssesmentMstDto.proAssfactor[${key.count-1}]"  defaultCheckedValue="Y" changeHandler="showAddInfo('${lookup.lookUpCode}','${key.count-1}')"></apptags:radio>	          
			 </c:if> 
			 
			<div id="${lookup.lookUpCode}" class="eachFact"> 
		    <c:if test="${not empty taxValue}">
		  	<div  class="padding-15"> 
            <table id="unitSpecificInfoTable${lookup.lookUpCode}" class="table table-striped table-bordered ">
                <tbody>
                        <tr>
                        	<th width="10%" class="required-control"><spring:message code="unit.ApplicableUnitNo"/></th>
                            <th width="25%" class="required-control"><spring:message code="unit.FactorValue"/></th>
                            <th style="width: 25%" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                            <th style="width: 30%" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                            <th style="width: 30%" class=""><spring:message code="" text="Amount"/></th>
                            <th width="10%"><spring:message code="unit.Add/Delete"/></th>                       	 
                        </tr>  
                        <c:forEach items="${taxValue}" var="factor" varStatus="status">      
                                      
                        <tr class="specFact"> 
	                       	<td >	                    		
	                        		<form:select id="unitNoFact${p1}" class="form-control mandColorClass selectUnit" path="provAsseFactDtlDto[${p1}].unitNoFact" onchange="resetFactorValue(this,0);">
									<form:option value="0">Select Option</form:option> 																	
									<form:option value="ALL">All</form:option>
										<c:forEach items="${command.getUnitNoList()}" var="unitNo">
										<form:option  value="${unitNo}" selected="${unitNo == factor.getUnitNoFact() ? 'selected' : ''}">${unitNo}</form:option>										
										</c:forEach>
									</form:select> 
	                        </td>                 
	                        <td >
		                      
 		                        		<form:hidden path="provAsseFactDtlDto[${p1}].assfFactorId" value="${factor.getAssfFactorId()}"/>
 		                        	 	<form:hidden value="${factor.getFactorValueCode()}" path="" id="factPref${status.count-1}"/> 
		                        	
  	
										<c:set var="baseLookupCode" value="${factor.getFactorValueCode()}" />
										<form:select path="provAsseFactDtlDto[${p1}].assfFactorValueId" id="assfFactorValueId${p1}" cssClass="form-control changeParameterClass mandColorClass factor" onchange="enabledisable(this,0);">
										<form:option value="0" code="">select</form:option>
										<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
										<form:option  value="${lookUp.lookUpId}" selected="${lookUp.lookUpId == factor.getAssfFactorValueId() ? 'selected' : ''}">${lookUp.lookUpDesc}</form:option>										
										</c:forEach>
										</form:select> 
	
							</td>
							<td>
								<div class="input-group">
															<form:input
																path="provAsseFactDtlDto[${p1}].factorDate"
																class="form-control datepicker mandColorClass dateClass addColor hasDatepicker"
																id="factorDate${p1}" maxlength="10"
																placeholder="DD/MM/YYYY" autocomplete="off" />
															<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
								</div>
							</td>

							<td><form:input
															path="provAsseFactDtlDto[${p1}].factorRemark"
															type="text" class="form-control  preventSpace"
															id="factorRemark${p1}" />
							</td>
							<td><form:input
															path="provAsseFactDtlDto[${p1}].factorAmount"
															type="text" class="form-control  preventSpace"
															id="factorAmount${p1}" />
							</td>
							
							<c:set var="p1" value="${p1+1}" > </c:set>
							<td class="col-sm-2 text-center col-sm-2">
									<a href="javascript:void(0);" title="Add" class="unitSpecificAdd btn btn-success btn-sm" onclick="addUnitRow(${status.count-1})"><i class="fa fa-plus-circle"></i></a>			
	 								<a href="javascript:void(0);" title="Delete" class="unitSpecificRem btn btn-danger btn-sm" id="deleteFactorTableRow_"><i class="fa fa-trash-o"></i></a> 
									
							</td>
                        </tr>
                        </c:forEach>                         
          	</tbody>         	  
           </table>
           </div>  
           </c:if>
               </div>   
               </c:if>
            </c:forEach>
            </div>
</div>
</div>
</c:if>
