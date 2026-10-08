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

<script type="text/javascript" src="js/property/Bifurcation.js" ></script> 
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
</style>	
<!-- <script src="js/property/changeUnitDetails.js" type="text/javascript"></script>
<script src="js/property/unitSpecificAdditionalInfo.js" type="text/javascript"></script> -->

<form:hidden path="" id="bifurcationId" value="${command.getSelfAss()}"/>

<div class="accordion-toggle">
<h4 class="margin-top-0 margin-bottom-10 panel-title">
<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
</h4>
<form:hidden path="ConstructFlag" id="ConstructFlag"/>
<form:hidden path="totalBuildUpArea" id="totalBuildUpArea"/>
<form:hidden path="totalrvArea" id="totalrvArea"/>

<div class="panel-collapse collapse in" id="UnitDetail">
		<c:if test="${not empty command.pageNoList}">
			<ul class="pagination-list">
				
				<c:forEach var="pageNo" items="${command.pageNoList}" varStatus="status" >
					<c:choose>
						<c:when test="${pageNo eq fn:length(command.pageNoList)}">
								<li class="hidden-print">
									<button type="button" class="btn btn-success btn-submit"
													onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
													<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) + 1} <spring:message code="and.more" text="and more..." />
												</button>
								</li>
						</c:when>
						<c:otherwise>
							<li class="hidden-print">
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
                <form:hidden path="countOfRow" class="copiedrow" id="countOfRow" value="" />	
                    <tbody>                
                        <tr>
                        	<th width="12%" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="5%" class="required-control"><spring:message code="unitdetails.unitno"/></th>
							<th width="7%" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
                            <th width="10%" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            <th width="13%" class="required-control" for="occupancy"><spring:message code="unitdetails.occupancy"/></th>
							<th width="12%" class="required-control" for="occupierName" ><spring:message code="unitdetails.OccupierName"/></th>
							<th class=""><spring:message code="unitdetails.legal.illegal" text="Is Legal/Illegal?"/></th>
                     	 	<th width="15%" class="required-control"><spring:message code="unitdetails.areaOrRv" text="Area/RV Based"/></th>
 							<th width="10%" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                     	 	<th width="10%" class="required-control"><spring:message code="unitdetails.bifurcate" text="Select to bifurcate"/></th>        	 
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix=""
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control" showAll="false" hasTableForm="true" showData="false" columnWidth="12%" />
							<th width="10%" class=""><spring:message
										code="unitdetails.carpetArea" text="Carpet Area" /></th>
                     	 	<th width="10%" class="required-control"><spring:message code="unitdetails.taxable"/></th> 
                     	 	<th width="15%" class=""><spring:message code="unitdetails.RV" text="RV"/></th>     	 
                        	<th colspan="3"></th>
                        	                      
                        </tr>
                          
       					<c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                                            
                    	<tr class="firstUnitRow">                     
                      		 <td>
                      			<c:set value="" var="year"></c:set>                                  	
                        	    <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="year${status.count-1}" class="form-control disabled text-center mandColorClass" >
		  						<form:option value="0" label="Select Year"></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" selected="${year == yearMap.key ? 'selected' : ''}">${yearMap.value}</form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="hiddenYear${status.count-1}"/>		 						    
		 										
		 					</td>
                        	 
                        	 <td>
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
              				 </td>
              				 
              				 <td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo" type="text" 
											class="form-control  mandColorClass text-right" id="flatNo${status.count-1}" data-rule-required="true"  /></td>
						
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
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" /> 
							</td>
							
							<td>
 			          			 <div class=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName" class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" maxlength="500"/></div>
							</td>
							
							<td>
                           <form:select class="form-control" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].legal" id="legal${status.count-1}">
							<form:option value="">
								<spring:message code="unit.select" text="Select"></spring:message>
							</form:option>
								<form:option value="Legal"></form:option>
								<form:option value="Illegal"></form:option>
								<form:option value="Illegal(Core)"></form:option>
							</form:select>
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
									<th width="12%"><label class="checkbox-inline">
									
									<form:checkbox path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBifurcateNo" value="Y" cssClass="checkall" id="assdBifurcateNo" /> </label>
	                         
							<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control " showAll="false" hasTableForm="true" showData="true" columnWidth="10%"/>
							
							<td width="15%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" disabled="true"/></td>
							<td width="15%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea" type="text" class="form-control taxableArea has2Decimal mandColorClass text-right" id="taxableArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" onchange="getAmountFormatInDynamic((this),'proAssdBuildupArea')" disabled="true"/></td>
							
							<%-- <td width="20%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdRv" 
							type="text" class="form-control assdRv has2Decimal mandColorClass text-right" 
							id="assdRv${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" 
							onchange="getAmountFormatInDynamic((this),'assdRv')" /></td> --%>
							
							<td width="15%"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdRv" type="text" onkeypress="return hasAmount(event, this, 15, 2)" 
											class="form-control  mandColorClass text-right width-100 hasDecimal" id="assdRv${status.count-1}" data-rule-required="true"  /></td>
											
                           	<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails(${status.count-1},this)" value="Room"></td>               
	                        <%-- <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-${status.count-1}" aria-expanded="false" aria-controls="group-of-rows-${status.count-1}"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td> --%>	                           
							 <td class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete" id="deleteRow_${status.count-1}"><i class="fa fa-minus-circle"></i></a></td>
							<td class="text-center"><a href="javascript:void(0);" class="addBIF btn btn-success btn-xs add" id="addRow_${status.count-1}" onclick="addBIF()"><i class="fa fa-plus-circle"></i></a></td>
                       	</tr> 
                    
			      		 
				</c:forEach>               		
                </tbody>
                </table>                  
</div>                        
 			 <div class="form-group">

				</div>	 
 		</c:when>
</c:choose>
</div>
</div>


<c:if test="${not empty command.pageNoList}">
<ul class="pagination-list">
	
	<c:forEach var="pageNo" items="${command.pageNoList}" varStatus="status" >
		<c:choose>
			<c:when test="${pageNo eq fn:length(command.pageNoList)}">
					<li class="hidden-print">
						<button type="button" class="btn btn-success btn-submit"
										onclick="getDataForNextPage(this,'${pageNo}')" id="submit">
										<%-- <spring:message code="Page" text="page" /> --%>${((pageNo-1)*75) +1} <spring:message code="and.more" text="and more..." />
									</button>
					</li>
			</c:when>
			<c:otherwise>
				<li class="hidden-print">
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
<%--             <label class="col-sm-8 control-label" ><spring:message code="" text="Is ${lookup.lookUpDesc} Applicable?"/></label> --%>
	        <label class="col-sm-8 control-label padding-top-0" ><spring:message code="property.Is" text="Is "/>${lookup.lookUpDesc} <spring:message  code="property.Applicable" text=" Applicable?"/></label>

          	<c:if test="${taxValue eq null}">
             <apptags:radio cssClass="col-sm-4 addInfo" radioLabel="Yes,No" radioValue="Y,N" labelCode="" path="provisionalAssesmentMstDto.proAssfactor[${key.count-1}]"  defaultCheckedValue="N" changeHandler="showAddInfo('${lookup.lookUpCode}','${key.count-1}')"></apptags:radio>	          
             																									
             </c:if>
             <c:if test="${taxValue ne null}">
             <apptags:radio cssClass="col-sm-4 addInfo" radioLabel="Yes,No" radioValue="Y,N" labelCode="" path="provisionalAssesmentMstDto.proAssfactor[${key.count-1}]"  defaultCheckedValue="Y" changeHandler="showAddInfo('${lookup.lookUpCode}','${key.count-1}')"></apptags:radio>	          
			 </c:if> 
			 

 			
 			 
			<div id="${lookup.lookUpCode}" class="eachFact"> 
		    <c:if test="${taxValue ne null}">
		  	<div  class="padding-15"> 
            <table id="unitSpecificInfoTable${lookup.lookUpCode}" class="table table-striped table-bordered ">
                <tbody>
                        <tr>
                        	<th width="10%" class="required-control"><spring:message code="unit.ApplicableUnitNo"/></th>
                            <th width="25%" class="required-control"><spring:message code="unit.FactorValue"/></th>
                            <th style="width: 25%" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                            <th style="width: 30%" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                            <th width="10%"><spring:message code="unit.Add/Delete"/></th>                       	 
                        </tr>  
                        <c:forEach items="${taxValue}" var="factor" varStatus="status">      
                                      
                        <tr class="specFact"> 
	                       	<td >	                    		
	                        		<form:select id="unitNoFact${p1}" class="form-control mandColorClass selectUnit" path="provAsseFactDtlDto[${p1}].unitNoFact" onchange="resetFactorValue(this,0);">
									<form:option value="0">Select Option</form:option> 																	
										<c:forEach items="${command.getUnitNoList()}" var="unitNo">
										<form:option  value="${unitNo}" selected="${unitNo == factor.getUnitNoFact() ? 'selected' : ''}">${unitNo}</form:option>										
										</c:forEach>
									</form:select> 
	                        </td>                 
	                        <td >
<%--   		                        	<form:hidden value="${factor.getProAssfFactorIdDesc()}" path="" id="proAssfFactorId"/> 
 --%> 		                      
 		                        	<form:hidden path="provAsseFactDtlDto[${p1}].assfFactorId" value="${factor.getAssfFactorId()}"/>
 		                        	 <form:hidden value="${factor.getFactorValueCode()}" path="" id="factPref${status.count-1}"/> 
		                        	
  	
									<c:set var="baseLookupCode" value="${factor.getFactorValueCode()}" />
										<form:select path="provAsseFactDtlDto[${p1}].assfFactorValueId" id="assfFactorValueId${p1}" cssClass="form-control changeParameterClass mandColorClass factor" onchange="enabledisable(this,0);">
										<form:option value="0" code="">select</form:option>
										<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
<%-- 										<form:option code="${lookUp.lookUpCode}" value="${lookUp.lookUpId}">${lookUp.lookUpDesc}</form:option>										
 --%>										<form:option  value="${lookUp.lookUpId}" selected="${lookUp.lookUpId == factor.getAssfFactorValueId() ? 'selected' : ''}">${lookUp.lookUpDesc}</form:option>										
										</c:forEach>
										</form:select> 
	
							</td>
							<td>
								<div class="input-group">
															<form:input
																path="provAsseFactDtlDto[${p1}].factorDate"
																class="form-control datepicker2  dateClass addColor hasDatepicker"
																id="factorDate${p1}" 
																placeholder="DD/MM/YYYY" autocomplete="off" />
															<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
								</div>
							</td>

							<td><form:input
															path="provAsseFactDtlDto[${p1}].factorRemark"
															type="text" class="form-control  preventSpace"
															id="factorRemark${p1}" />
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
            </c:forEach>
            </div>
</div>
</div>
