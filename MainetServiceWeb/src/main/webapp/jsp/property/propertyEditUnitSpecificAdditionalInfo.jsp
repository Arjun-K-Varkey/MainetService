<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script src="js/property/unitSpecificAdditionalInfo.js" type="text/javascript"></script>
<script>
	//newly added
	$(document).ready(function() {
		reorderfactor('.specFact', '.eachFact');
	});
	
	
	$(function() {
		$("body").attr("onmouseover", "refreshDates()");
	});
	function refreshDates() {
		$("body").removeAttr("onmouseover");
		$(".datepicker").removeClass("hasDatepicker");
		$(".lessthancurrdate").removeClass("hasDatepicker");
		datePickerLogic();
	}
	//newly added
</script>
<div class="padding-15 unitSpecificInfo">          
            <table id="unitSpecificInfoTable${command.getFactorPrefix()}" class="table table-striped table-bordered ">
                    <tbody>
                        <tr>
                        	<th style="width: 10%"  class="required-control"><spring:message code="unit.ApplicableUnitNo"/></th>
                            <th style="width: 25%" class="required-control"><spring:message code="unit.FactorValue"/></th>
                            <th style="width: 25%" class=""><spring:message code="unit.factorDate" text="Date"/></th>
                            <th style="width: 30%" class=""><spring:message code="unit.factoRemark" text="Remark"/></th>
                            <th style="width: 30%" class=""><spring:message code="" text="Amount"/></th>
                            <th style="width: 10%"><spring:message code="unit.Add/Delete"/></th>                       	 
                        </tr>                                    
                     
                        <tr class="specFact">  
	                       	<td>                        		                        		
	                        		<form:select id="unitNoFact0" class="form-control mandColorClass selectUnit" path="command.provAsseFactDtlDto[0].unitNoFact" onchange="resetFactorValue(this,0);">
									<form:option value="0">Select Option</form:option> 																	
									<form:option value="ALL">All</form:option> 																	
										<c:forEach items="${command.getUnitNoList()}" var="unitNo">
										<form:option  value="${unitNo}" selected="${unitNo == factor.getUnitNoFact() ? 'selected' : ''}">${unitNo}</form:option>										
										</c:forEach>
									</form:select>
	                        </td> 
	                        
	                                        
	                        <td>
		                        	<form:hidden path="command.provAsseFactDtlDto[0].assfFactorId" value="${command.getFactorId()}"/>
		                        	<form:hidden value="${command.getFactorPrefix()}" path="command.provAsseFactDtlDto[0].factorValueCode" id="factPref"/>
		                        	<form:hidden path="command.provAsseFactDtlDto[0].proAssfId" id="proAssfId"/>	
		                        		
										<c:set var="baseLookupCode" value="${command.getFactorPrefix()}" />
										<form:select path="command.provAsseFactDtlDto[0].assfFactorValueId" 
										id="FactorValue0" cssClass="form-control changeParameterClass mandColorClass factor" onchange="enabledisable(this,0);">
										<form:option value="0" code="">select</form:option>
										<c:forEach items="${command.getLevelData(baseLookupCode)}" var="lookUp">
										<form:option value="${lookUp.lookUpId}" code="${lookUp.lookUpCode}" >${lookUp.lookUpDesc}</form:option>
										</c:forEach>
										</form:select>
							 </td>
							 
							 <td>
								<div class="input-group">
											<form:input
												path="command.provAsseFactDtlDto[0].factorDate"
												class="form-control datepicker  dateClass addColor hasDatepicker"
												id="factorDate0" 
												placeholder="DD/MM/YYYY" autocomplete="off" />
											<span class="input-group-addon"><i
												class="fa fa-calendar"></i></span>
								</div>
							</td>
												
							<td>
								<form:input
								path="command.provAsseFactDtlDto[0].factorRemark"
								type="text" class="form-control  preventSpace"
								id="factorRemark0" />
							</td>

				<td><form:input
						path="command.provAsseFactDtlDto[0].factorAmount" type="text"
						class="form-control  preventSpace" id="factorAmount0" /></td>
						
				<td class="text-center">
									<a href="javascript:void(0);" title="Add" class="unitSpecificAdd btn btn-success btn-sm" onclick="addUnitRow('0')"><i class="fa fa-plus-circle"></i></a>			
	 								<a href="javascript:void(0);" title="Delete" class="unitSpecificRem btn btn-danger btn-sm" id="deleteFactorTableRow_"><i class="fa fa-trash-o"></i></a> 
									
							</td>
				<%-- 			<form:hidden path="command.unitStatusCount" id="UnitInfotblCount"/> --%>
							
                        </tr>
                	</tbody>
            </table>
</div>

