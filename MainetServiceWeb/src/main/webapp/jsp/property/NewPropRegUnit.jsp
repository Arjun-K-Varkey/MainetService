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
<script src="js/property/newPropRegUnit.js" type="text/javascript"></script>
<script src="js/property/newPropRegUnitSpec.js" type="text/javascript"></script>
<script type="text/javascript" src="js/property/newPropRegOwner.js"></script>

<%-- Code added by ABM2144 for Property Data entry suite Edit form - datepicker reset--%>
<c:choose>
<c:when test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}">
<script>
$(function(){	
	$("body").attr("onmouseover", "refreshDates()");	
});
function refreshDates(){
	$("body").removeAttr("onmouseover");
	$(".datepicker2").removeClass("hasDatepicker");	
	$(".lessthancurrdate").removeClass("hasDatepicker");
	datePickerLogic();
}

</script>
</c:when>
</c:choose>
<%-- Code added by ABM2144 for Property Data entry suite Edit form - datepicker reset--%>
<style>
.addColor{
	background-color: #fff !important
}
</style>
<div class="accordion-toggle">
<h4 class="margin-top-0 margin-bottom-10 panel-title">
<a data-toggle="collapse" href="#UnitDetail"><spring:message code="property.unitdetails"/></a>
</h4>
<div class="panel-collapse collapse in" id="UnitDetail">

		<!-- Newly added -->
		<c:choose>
			<c:when test="${param.formName ne null}">
				<input type="hidden" value="${param.formName}" id="formNamee">				
			</c:when>
			<c:otherwise>
				<input type="hidden" value="" id="formNamee">			
			</c:otherwise>
		</c:choose>

		<%-- <jsp:forward page="/jsp/property/AddRoomDetails.jsp">
			<jsp:param name="formNamee" value="${formNamee}" />
		</jsp:forward> --%>
		<!-- Newly added -->

		<c:choose>
		<c:when test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}">
		<div class="form-group" style="margin-left: 0">
 			 		<spring:message code="property.propFloorDetail"/>
 		</div> 
		<div class="table-responsive">
		
					<c:set var="d" value="0" scope="page" />					
					
					<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>                        
                        <tr>
                        	<th width="" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.unitno"/></th>
<%-- < %--                             <th width="9%" class="required-control"><spring:message code="unitdetails.unittype"/></th> --%> 
                            <th width="" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.ConstcompletionDate"/></th>
                            <th width="" class="required-control"><spring:message code="unitDetails.firstAssessmentDate"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control" showAll="false" hasTableForm="true" showData="false" columnWidth="12%" />
                     	 	

								<!-- New fields Added -->
								<th width="" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
								<th width="" class=""><spring:message
										code="unitdetails.carpetArea" text="Carpet Area" /></th>
								<th width="" class="required-control"><spring:message code="unitdetails.taxable"/></th>
								<%-- <th width="" class=""><spring:message
										code="unitdetails.age" text="Age" /></th> --%>
								<th width="" class=""><spring:message
										code="unitdetails.constructPermissionNo"
										text="Construction Permission No." /></th>
								<th width="" class=""><spring:message
										code="unitdetails.permissionUseNo" text="Permission Use No." /></th>
								<th width="" class=""><spring:message
										code="unitdetails.assessmentRemark" text="Assessment Remark" /></th>
								<th width="" class=""><spring:message
										code="unitdetails.legal" text="Legal" /></th>
								<!-- New fields end -->
								
								<th colspan="4"><a href="javascript:void(0);" title="Add" class="addCF btn btn-success btn-sm unit" id="addUnitRow"><i class="fa fa-plus-circle"></i></a></th>
                        
                        </tr>
                          
       					<c:forEach var="unitDetails" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList()}" varStatus="status" >
                                            
                    	<tr class="firstUnitRow">
                       
                      		 <td>                                       	
                        	    <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="year${status.count-1}" onchange="unitCount(${status.count-1})" class="form-control text-center mandColorClass displayYearList">
		  						<form:option value=""><spring:message code="prop.selectSelectYear"/></form:option>
		  						<c:forEach items="${command.financialYearMap}" var="yearMap">
		  						<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
		  						</c:forEach>
		 						</form:select>
		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].faYearId" id="hiddenYear0"/>		 						    
		 					</td>
                        	 
                        	 <td>
              					<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
              				</td>
						
							<%-- <td>
								<c:set var="baseLookupCode" value="UTP" />
									<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitTypeId" cssClass="form-control changeParameterClass mandColorClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="Select Unit Type"  hasTableForm="true" isMandatory="true" />
							</td> --%>
						
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdFloorNo" cssClass="form-control changeParameterClass mandColorClass"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="prop.selectFloorNo" isMandatory="true" hasTableForm="true" />
							</td>
						
							<td>
		 						<div class="input-group successErrorCheck"> 
								<form:input type="text" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdYearConstruction" class="form-control datepicker2 mandColorClass dateClass addColor" id="yearOfConstruc0" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" readonly="true" />									
								<span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
							</td>
							<td>
		 						<div class="input-group successErrorCheck"> 
								<form:input type="text" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].firstAssesmentDate" class="form-control lessthancurrdate mandColorClass dateClass addColor" id="firstAssesmentDate0" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" readonly="true"/>									
								<span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
								
							</td>
                            <td> 
                            	<c:set var="baseLookupCode" value="CSC" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdConstruType" cssClass="form-control changeParameterClass mandColorClass"
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="prop.selectSelectConstructionType" isMandatory="true" hasTableForm="true"/>
							</td>
							<apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control " showAll="false" hasTableForm="true" showData="true" columnWidth="10%"/>
					
							<!-- New fields Added -->
							<td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].flatNo" type="text" class="form-control  mandColorClass text-right" id="flatNo${status.count-1}" data-rule-required="true"  /></td>
                           	<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" /></td>
							<td width="150">
							<spring:message code="property.Area.PlaceHolder" text="999999.99 sq.ft" var="area"/>
							
							<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea${status.count-1}" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" onchange="getAmountFormatInDynamic((this),'proAssdBuildupArea')" placeholder="${area}"/></td>
                           
                           
                           <%-- <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].age" id="age${status.count-1}"></form:input></td>  --%>                      
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].constructPermissionNo" id="constructPermissionNo${status.count-1}"></form:input></td>
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].permissionUseNo" id="permissionUseNo${status.count-1}"></form:input></td>
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assessmentRemark" id="assessmentRemark${status.count-1}"></form:input></td>                        
                           <td><label class="checkbox-inline padding-top-0" for="legal"><form:checkbox path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].legal" value="Y" /></label>                           
                           
                           <!-- New fields Added -->
                           
	                         <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-0" aria-expanded="false" aria-controls="group-of-rows-0"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td>	                           
							 <td class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete" id="deleteRow_"><i class="fa fa-minus-circle"></i></a></td>
							<td><input type="button" id="addRoom${status.count-1}" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails(${status.count-1},this)" value="Room Details"></td>
                       		<td><input type="button" id="addArrearForFlat${status.count-1}" class="btn btn-success btn-submit addArrearClass" onclick="addArrearDetailsForFlat(${status.count-1},this)" value="Add Arrears"></td>																												
                       	
                       	</tr> 
                    
			      		<tr class="secondUnitRow collapse in" id="group-of-rows-0">
			           		 <td colspan="19">

			              	 <legend class="text-blue-3 text-left"><spring:message code="unitdetails.AdditionalUnitDetails" />
							 </legend>
            
				             <div class="addunitdetails0 col-xs-6 col-xs-offset-0 col-md-12">
				             	<div class="form-group" >   
           			
				           			<%-- <label for="road-type" class="col-sm-2 control-label required-control"><spring:message code="unitdetails.RoadType"/> </label>
				            		<div class="col-sm-2">    		
				           				<c:set var="baseLookupCode" value="RFT" />
										<apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
				 									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdRoadFactor" cssClass="form-control changeParameterClass " 
				 									hasChildLookup="false" hasId="true" showAll="false" 
													selectOptionLabelCode="Select Road Factor" isMandatory="true" hasTableForm="true"  />
			                		</div> --%> 
										<label for="occupancy" class="col-sm-2 control-label required-control"><spring:message code="unitdetails.occupancy"/> </label>
				               		<div class="col-sm-4">	
				           				<c:set var="baseLookupCode" value="OCS" /> 
				 						<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}" 
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdOccupancyType" cssClass="form-control changeParameterClass " 
													hasChildLookup="false" hasId="true" showAll="false"
				 									selectOptionLabelCode="prop.selectSelectOccupancyType" isMandatory="true" hasTableForm="true" changeHandler="loadOccupierName(this)"/> 
		 							</div>
		 							
		 							
										<%-- <label for="proAssdAnnualRent${status.count-1}" class="col-sm-2 control-label"><spring:message code="property.rent"/></label>	 							
										<div class="col-sm-2">
										<form:input cssClass="form-control text-right"
																	onkeypress="return hasAmount(event, this, 15, 2)"
																	id="proAssdAnnualRent${status.count-1}"  path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdAnnualRent"
																	placeholder="999999.99"
																	onchange="getAmountFormatInDynamic((this),'monthly-rent')"></form:input>	 							
										</div> --%>
									
									<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
									showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfproperty"
									isMandatory="true" hasLookupAlphaNumericSort="true"
									hasSubLookupAlphaNumericSort="true"
									cssClass="form-control required-control" showAll="false"  columnWidth="10%"/>
							
									
								</div>
								<%-- <div class="form-group">
 			          				<label for="occupierName${status.count-1}" class="col-sm-2 control-label"><spring:message code="unitdetails.OccupierName"/></label> 
 			          		   		<div class="col-sm-2"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"  class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" /></div>
			          			</div> --%>	
			          			<div class="form-group">
			          			
			          			<!-- Location chnaged -->
			          			<label for="occupierName${status.count-1}" class="col-sm-2 control-label"><spring:message code="unitdetails.OccupierName"/></label> 
 			          		   	<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierName"  class="form-control hasCharacter preventSpace" id="occupierName${status.count-1}" /></div>
			          			
			          			
			          			<label for="occupierNameReg${status.count-1}" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierNameReg" /></label>
								<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierNameReg"
													type="text" class="form-control  preventSpace"
													id="occupierNameReg${status.count-1}" /></div>
								</div>
			          			
			          			
			          			<div class="form-group">
											<label for="occupierMobNo${status.count-1}" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierMobNo" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierMobNo"
													type="text" class="form-control hasMobileNo  preventSpace"
													id="occupierMobNo${status.count-1}" />
											</div>

											<label for="occupierEmail${status.count-1}" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierEmail" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].occupierEmail"
													type="text" class="form-control hasemailclass  preventSpace"
													id="occupierEmail${status.count-1}" />
											</div>
									</div>
									
									<div class="form-group actualRentClass">
											<label for="actualRent${status.count-1}" class="col-sm-2 control-label"><spring:message
													code="unitdetails.actualRent" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].actualRent"
													type="text"
													class="form-control has2Decimal mandColorClass text-left"
													id="actualRent${status.count-1}" data-rule-required="true"
													onkeypress="return hasAmount(event, this, 15, 2)" /></td>
											</div>
									</div>
			          			
								<%-- <label class="col-sm-2 control-label required-control"
									for="natureOfProperty"><spring:message
										code="property.selectNatureOfProperty"/></label>
									<div class="col-sm-2">---------------------------------
									<c:set var="baseLookupCode" value="PTP" /> <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdNatureOfproperty1"
											onchange="" class="form-control mandColorClass" id="natureOfProperty" data-rule-required="true">
												<form:option value="">
													<spring:message code="property.sel.optn" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
									</form:select> --%>
									</div>		
							</div>		
						</div>
			        	 </td>
			         	</tr> 
				</c:forEach>               		
                </tbody>
                </table>                
</div>                    
	 	 <c:if test="${command.getAssType() ne 'DES'  && command.getAssType() ne 'E'  && command.getAssType() ne 'A'}">   
		       <div class="form-group">
					<div class="col-sm-6">
						<label for="" class="radio-inline">
						<form:checkbox path="" value="" onchange="displayDetailsTillCurrentYear()" id="checkDetail" />
						<spring:message code="unitdetails.changeInunitdetails"/>
						</label>
					</div>
				</div>
 			 	 
 			 	 </c:if>
 		</c:when>
 
 			 	<c:otherwise>	
 			 		<div class="form-group" style="margin-left: 0">
 			 		<spring:message code="property.propFloorDetail"/>
 			 		</div>
				<div class="table-responsive">
				<c:set var="d" value="0" scope="page" />

					<!-- Newly added -->
					<c:set var="count" value="0" scope="page" />
					<!-- Newly added -->
					
					<table id="unitDetailTable" class="table table-striped table-bordered appendableClass unitDetails">
                    <tbody>
                        <tr>
                        	<th width="" class="required-control"><spring:message code="unitdetails.year"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.unitno"/></th>
<%--                             <th width="9%" class="required-control"><spring:message code="unitdetails.unittype"/></th> --%>
                            <th width="" class="required-control"><spring:message code="unitdetails.floorno"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.ConstcompletionDate"/></th>
                            <th width="" class="required-control"><spring:message code="unitDetails.firstAssessmentDate"/></th>
                            <th width="" class="required-control"><spring:message code="unitdetails.constructiontype"/></th>
                            <apptags:lookupFieldSet baseLookupCode="USA" hasId="true"
							showOnlyLabel="false" pathPrefix=""
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control" showAll="false" hasTableForm="true" showData="false" columnWidth="12%" />
                     	 	
                        	 
                        	 <!-- New fields Added -->
                        	<th width="" class="required-control"><spring:message code="unitdetails.flatNo"/></th>
                   	 	    <th width="" class=""><spring:message code="unitdetails.carpetArea" text="Carpet Area"/></th>
                   	 	    <th width="" class="required-control"><spring:message code="unitdetails.taxable"/></th>
                   	 	    <%-- <th width="" class=""><spring:message code="unitdetails.age" text="Age"/></th> --%>
                   	 	    <th width="" class=""><spring:message code="unitdetails.constructPermissionNo" text="Construction Permission No."/></th>
                     	 	<th width="" class=""><spring:message code="unitdetails.permissionUseNo" text="Permission Use No."/></th>
                     	 	<th width="" class=""><spring:message code="unitdetails.assessmentRemark" text="Assessment Remark"/></th>
                     	 	<th width="" class=""><spring:message code="unitdetails.legal" text="Legal"/></th>
                        	<!-- New fields end -->
                        	
                        	<th colspan="4"><a href="javascript:void(0);" title="Add" class="addCF btn btn-success btn-sm unit" id="addUnitRow"><i class="fa fa-plus-circle"></i></a></th>
                        
                        </tr>
                                              
                    	<tr class="firstUnitRow">
                       
                      		 <td>                                       	
                        	    <form:select path="" id="year0" class="form-control text-center mandColorClass displayYearList" data-rule-required="true" onchange="unitCount(0)">
		  						<form:option value=""><spring:message code="prop.selectSelectYear"/></form:option>
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
						
							<%-- <td>
								<c:set var="baseLookupCode" value="UTP" />
								<apptags:lookupField
									items="${command.getLevelData(baseLookupCode)}"
									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUnitTypeId" cssClass="form-control changeParameterClass "
									hasChildLookup="false" hasId="true" showAll="false"
									selectOptionLabelCode="Select Unit Type"  hasTableForm="true" isMandatory="true" />
							</td> --%>
							<td>
								<c:set var="baseLookupCode" value="IDE" />
								<apptags:lookupField
										items="${command.getLevelData(baseLookupCode)}"
										path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdFloorNo" cssClass="form-control changeParameterClass "
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="prop.selectFloorNo" isMandatory="true" hasTableForm="true" />
							</td>
						
							<td>
		 						<div class="input-group successErrorCheck"> 
								<form:input type="text" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdYearConstruction" class="form-control datepicker2 mandColorClass dateClass addColor" id="yearOfConstruc0" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" readonly="true"/>									
								<span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
								
							</td>
							<td>
		 						<div class="input-group successErrorCheck1"> 
								<form:input type="text" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].firstAssesmentDate" class="form-control lessthancurrdate mandColorClass dateClass addColor" id="firstAssesmentDate0" data-rule-required="true" placeholder="DD/MM/YYYY" autocomplete="off" readonly="true"/>									
								<span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
								
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
							showOnlyLabel="false" pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdUsagetype"
							isMandatory="true" hasLookupAlphaNumericSort="true"
							hasSubLookupAlphaNumericSort="true"
							cssClass="form-control required-control " showAll="false" hasTableForm="true" showData="true" columnWidth="10%"/>
					
							<!-- New fields Added -->
							<td width=""><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].flatNo" type="text" class="form-control  mandColorClass text-right" id="flatNo0" data-rule-required="true"  /></td>
                           	<td><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].carpetArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="carpetArea0" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" /></td>
							<td width="">
							<spring:message code="property.Area.PlaceHolder" text="999999.99 sq.ft/sq.mt" var="area"/>							
							<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdBuildupArea" type="text" class="form-control has2Decimal mandColorClass text-right" id="taxableArea0" data-rule-required="true" onkeypress="return hasAmount(event, this, 15, 2)" onchange="getAmountFormatInDynamic((this),'proAssdBuildupArea')" placeholder="${area}"/></td>
                           
                           
                           <%-- <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].age" id="age0"></form:input></td>  --%>                      
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].constructPermissionNo" id="constructPermissionNo0"></form:input></td>
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].permissionUseNo" id="permissionUseNo0"></form:input></td>
                           <td><form:input type="text" class="form-control " path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assessmentRemark" id="assessmentRemark0"></form:input></td>                        

                           <td><label class="checkbox-inline padding-top-0" for="legal"><form:checkbox path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].legal" value="Y" /> </label>                          
                           </td> 
                           <!-- New fields Added -->
                           
	                         <td class="text-center"><a class="clickable btn btn-success btn-xs click_advance" data-toggle="collapse" data-target="#group-of-rows-0" aria-expanded="false" aria-controls="group-of-rows-0"><i class="fa fa-caret-down" aria-hidden="true"></i></a></td>	                           
							<td class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete"  id="deleteRow_"><i class="fa fa-minus-circle"></i></a></td>
							<td><input type="button" id="addRoom0" class="btn btn-success btn-submit addRoomClass" onclick="addRoomDetails('0',this)" value="Room Details"></td>
							<td><input type="button" id="addArrearForFlat0" class="btn btn-success btn-submit addArrearClass" onclick="addArrearDetailsForFlat('0',this)" value="Add Arrears"></td>																												
                       	</tr> 
                    
			      <tr class="secondUnitRow collapse in" id="group-of-rows-0">
								<td colspan="19"><legend class="text-blue-3 text-left">
										<spring:message code="unitdetails.AdditionalUnitDetails" />
									</legend>

									<div class="addunitdetails0 col-xs-6 col-xs-offset-0 col-md-12">
										<div class="form-group">

											<%-- <label for="road-type" class="col-sm-2 control-label required-control"><spring:message code="unitdetails.RoadType"/> </label>
				            		<div class="col-sm-2">
	
			           				<c:set var="baseLookupCode" value="RFT" />
									<apptags:lookupField  items="${command.getLevelData(baseLookupCode)}"
			 									path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdRoadFactor" cssClass="form-control changeParameterClass " 
			 									hasChildLookup="false" hasId="true" showAll="false" 
												selectOptionLabelCode="Select Road Factor" isMandatory="true" hasTableForm="true"  />
			                		</div> --%>
											<label for="occupancy"
												class="col-sm-2 control-label required-control"><spring:message
													code="unitdetails.occupancy" /> </label>
											<div class="col-sm-4">

												<c:set var="baseLookupCode" value="OCS" />
												<apptags:lookupField
													items="${command.getLevelData(baseLookupCode)}"
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdOccupancyType"
													cssClass="form-control changeParameterClass "
													hasChildLookup="false" hasId="true" showAll="false"
													selectOptionLabelCode="prop.selectSelectOccupancyType"
													isMandatory="true" hasTableForm="true"
													changeHandler="loadOccupierName(this)" />
											</div>

											<%-- <label for="proAssdAnnualRent" class="col-sm-2 control-label"><spring:message code="property.rent"/></label>	 							
									<div class="col-sm-2">
									<form:input cssClass="form-control text-right"
												onkeypress="return hasAmount(event, this, 15, 2)"
												id="proAssdAnnualRent" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdAnnualRent"
												placeholder="999999.99"
												onchange="getAmountFormatInDynamic((this),'monthly-rent')"></form:input>	 							
									</div> --%>

											<apptags:lookupFieldSet baseLookupCode="PTP" hasId="true"
												showOnlyLabel="false"
												pathPrefix="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty"
												isMandatory="true" hasLookupAlphaNumericSort="true"
												hasSubLookupAlphaNumericSort="true"
												cssClass="form-control required-control" showAll="false"
												columnWidth="10%" />

										</div>

										<%-- <div class="form-group">
						    
				          		    <label for="occupier-name" class="col-sm-2 control-label"><spring:message code="unitdetails.OccupierName"/></label>
				          		    <div class="col-sm-2"><form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierName" type="text" class="form-control hasCharacter preventSpace" id="occupierName" /></div>
				          		
				          		</div> --%>
										<div class="form-group">

											<label for="occupier-name" class="col-sm-2 control-label"><spring:message
													code="unitdetails.OccupierName" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierName"
													type="text" class="form-control hasCharacter preventSpace"
													id="occupierName" />
											</div>

											<label for="occupierNameReg" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierNameReg" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierNameReg"
													type="text" class="form-control  preventSpace"
													id="occupierNameReg" />
											</div>




											<%-- <label class="col-sm-2 control-label required-control"
									for="natureOfProperty"><spring:message
										code="property.selectNatureOfProperty" /></label>
									<div class="col-sm-2">+++++++++++++++++++++++
									<c:set var="baseLookupCode" value="PTP" /> <form:select path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].assdNatureOfproperty1"
											onchange="" class="form-control mandColorClass" id="natureOfProperty" data-rule-required="true">
												<form:option value="">
													<spring:message code="property.sel.optn" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
									</form:select> --%>
										</div>

										<div class="form-group">
											<label for="occupierMobNo" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierMobNo" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierMobNo"
													type="text" class="form-control hasMobileNo  preventSpace"
													id="occupierMobNo" />
											</div>

											<label for="occupierEmail" class="col-sm-2 control-label"><spring:message
													code="unitdetails.occupierEmail" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].occupierEmail"
													type="text" class="form-control hasemailclass  preventSpace"
													id="occupierEmail" />
											</div>
										</div>

										<div class="form-group actualRentClass">
											<label for="actualRent" class="col-sm-2 control-label"><spring:message
													code="unitdetails.actualRent" /></label>
											<div class="col-sm-4">
												<form:input
													path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].actualRent"
													type="text"
													class="form-control has2Decimal mandColorClass text-right"
													id="actualRent" data-rule-required="true"
													onkeypress="return hasAmount(event, this, 15, 2)" /></td>
											</div>
										</div>

								</div>		
						</div>
			         </td>
			         </tr>
							
						</tbody>
                </table>                
			</div>      
			 <c:if test="${command.getAssType() ne 'DES' && command.getAssType() ne 'E'  && command.getAssType() ne 'A'  }">     
		       <div class="form-group">
					<div class="col-sm-6">
					<label for="" class="radio-inline">
						<form:checkbox path="" value="" onchange="displayDetailsTillCurrentYear()" id="checkDetail" />
							<spring:message code="unitdetails.changeInunitdetails"/>
					</label>
					</div>
					
					<form:hidden path="unitDetailTableCount" id="tblCount"/>								
				</div>
				</c:if>
				
		</c:otherwise>
</c:choose>

            </div>
</div>
<!-- newly added -->
<form:hidden path="countOfRow" id="countOfRow"/>
<form:hidden path="flatNoOfRow" id="flatNoOfRow"/>
<!--  Need to set else error -->
<form:hidden path="provisionalAssesmentMstDto.formCode" id="formCode" value="${param.formName}"/>
<!-- newly added -->

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
				<form:radiobutton class="factorStatus" path="provisionalAssesmentMstDto.proAssfactor[${status.count-1}]" value="N"  id="No" onchange="showAddInfo('${facCode}','${status.count-1}')"/>
				<spring:message code="Unitdetails.No"></spring:message></label>
				</div> 
		
   			<c:set var="p1" value="1" > </c:set>
   			<div id="${facCode}" class="eachFact"> 
	    	<c:forEach items="${command.getProvAsseFactDtlDto()}" var="factableLoop" varStatus="status">    <!--loop to check weather data is available for question   -->
		   		<c:if test="${factableLoop.assfFactorId eq factId}">
	           	<c:if test="${p1 eq 1}">
	      <%--      	<div id="${facCode}" class="eachFact">  --%>
		          	<div class="padding-15 unitSpecificInfo">
		            <table id="unitSpecificInfoTable${facCode}" class="table table-striped table-bordered ">
		                    <tbody>
		                        <tr>
		                        	<th width="25%" class="required-control"><spring:message code="unit.ApplicableUnitNo"/></th>
		                            <th width="25%" class="required-control"><spring:message code="unit.FactorValue"/></th>
		                            <th width="" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                            		<th width="" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
		                            <th width="15%"><spring:message code="unit.Add/Delete"/></th>                       	 
		                        </tr>
		                        
				       		<c:forEach items="${command.getProvAsseFactDtlDto()}" var="details" varStatus="status">    
				            <c:if test="${details.assfFactorId  eq factId}">
		                        
		                        <tr class="specFact">  
			                       	<td class="col-sm-5">    
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
			                        
			                                        
		                        	<td class="col-sm-5 ">
		                        		<form:hidden path="provAsseFactDtlDto[${status.count-1}].assfFactorId" value="${factId}"/>
		                        	 	<form:hidden path="provAsseFactDtlDto[${status.count-1}].factorValueCode" id="factPref" value="${facCode}"/>
		                        		<form:hidden path="provAsseFactDtlDto[${status.count-1}].proAssfId" id="proAssfId"/>	
										<c:set var="baseLookupCode" value="${details.getFactorValueCode()}" />
										<form:select path="provAsseFactDtlDto[${status.count-1}].assfFactorValueId" 
										id="FactorValue0" cssClass="form-control changeParameterClass mandColorClass factor" onchange="enabledisable(this,0);">
										 <form:option value="0" code="">select</form:option>
										<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
										<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}" >${lookUp.lookUpDesc}</form:option>
										</c:forEach>
										
										</form:select>
									</td>

									<td class="col-sm-2">
											<div class="input-group">
													<form:input
															path="provAsseFactDtlDto[${status.count-1}].factorDate"
															class="form-control datepicker2 mandColorClass dateClass addColor hasDatepicker"
															id="factorDate0" data-rule-required="true"
															placeholder="DD/MM/YYYY" autocomplete="off" />
													<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
									</td>

									<td class="col-sm-2"><form:input
												path="provAsseFactDtlDto[${status.count-1}].factorRemark"
												type="text" class="form-control  preventSpace"
												id="factorRemark0" /></td>
												
									<td class="col-sm-2 text-center col-sm-2">
										<a href="javascript:void(0);" title="Add" class="unitSpecificAdd btn btn-success btn-sm" onclick="addUnitRow('0')"><i class="fa fa-plus-circle"></i></a>								
		 								<a href="javascript:void(0);" title="Delete" class="unitSpecificRem btn btn-danger btn-sm"><i class="fa fa-trash-o"></i></a>  
										
									</td>
<%-- 									<form:hidden path="unitStatusCount" id="UnitInfotblCount"/>
 --%>		                        </tr>
		                        		</c:if>
		     				</c:forEach>
		                    </tbody>
		            </table>
				</div>
	          <!--  </div>    -->
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
					<form:radiobutton class="factorStatus selectFactor" path="provisionalAssesmentMstDto.proAssfactor[${d}]" value="N"  id="No_"  onchange="showAddInfo('${facCode}','${d}')"/>
					<spring:message code="Unitdetails.No"></spring:message></label>
					</div>  
					  
		           	<div id="${facCode}" class="eachFact"> 
		          
		           	</div> 
		           	<c:set var="d" value="${d+1}" />  
		           	</c:if>		           	         
	            </c:forEach>
	       <%--     <form:hidden path="unitStatusCount" id="factorValue"/> --%>
            </c:otherwise>
            </c:choose>
            </div>
</div>
</div>
</c:if>