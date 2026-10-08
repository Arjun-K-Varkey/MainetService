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
#totalArea{
    font-size: inherit ! important;
    }
</style>
<script>
	$(document).ready(function() {		
		$("#roomNo0").val(1);
		reOrderRoomTableIdSequence($("#countOfRow").val());
	});
	
  
</script>

<!-- Start breadcrumb Tags -->
<apptags:breadcrumb></apptags:breadcrumb>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="property.roomdetails" text="Room Details" /></strong>
			</h2>

			<apptags:helpDoc url="DataEntrySuite.html">
			</apptags:helpDoc>

		</div>

		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">
			<form:form action="SelfAssessmentForm.html" class="form-horizontal form"
				name="frmSelfAssessmentFormRoom" id="frmSelfAssessmentFormRoom">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>


				<c:set var="count" value="0" scope="page" />
				<c:set var="i" value="${command.countOfRow}" scope="page" />
				<form:hidden path="" value="${i}" id="countOfRow" />								
				<form:hidden path="" value="${command.unitDetailsCheck}" id="unitChecked" />
				<form:hidden path="" value="${command.getOwnershipPrefix()}" id="ownerType" />
				<form:hidden path="" value="${carpetArea}" id="carpetArea" />
				<form:hidden path="" value="${command.roomTypeJson}" id="roomTypeJson" />				

				<c:choose>
					<c:when
						test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList().get(i).getRoomDetailsDtoList()}">
						<div id="" class="">
							<div class="table-overflow-sm" id="">
								<tr class="thirdUnitRow collapse in" id="group-of-rows-0">
									<td colspan="15">

										<table id="roomDetailstable"
											class="table table-striped table-bordered appendableClass roomDetailstable">
											<tbody>
												<tr>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomNo" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomType" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomLength" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomWidth" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomArea" /></th>
													<th colspan="2"><a href="javascript:void(0);"
														title="Add"
														class="addRoomDetails btn btn-success btn-sm unit"
														id="addRoomDetails"><i class="fa fa-plus-circle"></i></a></th>
														

												</tr>
												  
												<c:forEach var="unitDetails"
													items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList().get(i).getRoomDetailsDtoList()}"
													varStatus="status">
													<tr class="thirdRowRoomDetails">
													    
														<td><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${status.count-1}].roomNo"
																type="text"
																class="form-control mandColorClass text-center unit required-control"
																id="roomNo0" data-rule-required="true" 
																readonly="true" /></td>
																
														<td><c:set var="baseLookupCode" value="RTP" /> <apptags:lookupField
																items="${command.getLevelData(baseLookupCode)}"
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${status.count-1}].roomType"
																cssClass="form-control changeParameterClass mandColorClass"
																hasChildLookup="false" hasId="true" showAll="false"
																selectOptionLabelCode="roomdetails.roomType"
																isMandatory="true" hasTableForm="true" /></td>

														<td width=""><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${status.count-1}].roomLength"
																type="text"
																class="form-control has2Decimal mandColorClass text-right"
																id="roomLength0" data-rule-required="true"
																onchange="calculateArea()"
																onkeypress="return hasAmount(event, this, 15, 2)" /></td>

														<td width=""><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${status.count-1}].roomWidth"
																type="text"
																class="form-control has2Decimal mandColorClass text-right"
																id="roomWidth0" data-rule-required="true"
																onchange="calculateArea(); areaCalculate();"
																onkeypress="return hasAmount(event, this, 15, 2)" /></td>

														<td width=""><form:input
																path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${status.count-1}].roomArea"
																type="text"
																class="form-control has2Decimal mandColorClass text-right"
																id="roomArea0"  readonly="true"
																onchange="return hasAmount(event, this, 15, 2)"
																data-rule-required="true" /></td>

														<td class="text-center"><button title="Delete"
																class="btn btn-danger btn-sm delButton"
																id="delButton${status.count-1}">
																<i class="fa fa-trash-o"></i>
															</button></td>
												
													</tr>
													
												</c:forEach>
											
											</tbody>
												<tfoot>
										            <tr>
										                <td colspan="4"><spring:message
															code="propertyTax.Total" text="Total"/></td>
										               <td colspan="1" id="totalArea"><form:input
																	path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomTotalArea"
																type="text"
																class="form-control has2Decimal mandColorClass text-right"
																id="totalAreaval"  readonly="true"
																
																/></td>
										            </tr>
										        </tfoot>
										</table></td>
								</tr>
							</div>
						</div>
					</c:when>

					<c:otherwise>
						<div id="" class="">
							<div class="table-overflow-sm" id="">
								<tr class="thirdUnitRow collapse in" id="group-of-rows-0">
									<td colspan="15">

										<table id="roomDetailstable"
											class="table table-striped table-bordered appendableClass roomDetailstable">
											<tbody>
												<tr>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomNo" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomType" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomLength" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomWidth" /></th>
													<th width="" class="required-control"><spring:message
															code="roomdetails.roomArea" /></th>
													<th colspan="2"><a href="javascript:void(0);"
														title="Add"
														class="addRoomDetails btn btn-success btn-sm unit"
														id="addRoomDetails"><i class="fa fa-plus-circle"></i></a></th>

												</tr>
													  
												<tr class="thirdRowRoomDetails">
													<td><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${count}].roomNo"
															type="text"
															class="form-control  text-center unit required-control"
															id="roomNo0"  data-rule-required="true"
															readonly="true" /></td>

													<td><c:set var="baseLookupCode" value="RTP" /> <apptags:lookupField
															items="${command.getLevelData(baseLookupCode)}"
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${count}].roomType"
															cssClass="form-control changeParameterClass mandColorClass"
															hasChildLookup="false" hasId="true" showAll="false"
															selectOptionLabelCode="roomdetails.roomType"
															isMandatory="true" hasTableForm="true" /></td>

													<td width=""><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${count}].roomLength"
															type="text"
															class="form-control has2Decimal mandColorClass text-right"
															id="roomLength0" data-rule-required="true"
															onchange="calculateArea()"
															onkeypress="return hasAmount(event, this, 15, 2)" /></td>

													<td width=""><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${count}].roomWidth"
															type="text"
															class="form-control has2Decimal mandColorClass text-right"
															id="roomWidth0" data-rule-required="true"
															onchange="calculateArea(); areaCalculate();"
															onkeypress="return hasAmount(event, this, 15, 2)" /></td>

													<td width=""><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomDetailsDtoList[${count}].roomArea"
															type="text"
															class="form-control has2Decimal mandColorClass text-right"
															id="roomArea0"  readonly="true"
															onchange="return hasAmount(event, this, 15, 2)"
															data-rule-required="true" /></td>

													<td class="text-center"><button title="Delete"
															class="btn btn-danger btn-sm delButton"
															id="delButton${count}">
															<i class="fa fa-trash-o"></i>
														</button></td>
												<%-- 	<c:set var="totalCount" value="${roomArea}"></c:set> --%>
												</tr>
														
											</tbody>
													<tfoot>
										            <tr>
										                <td colspan="4"><spring:message
															code="propertyTax.Total" text="Total"/></td>
										                
										                <td colspan="1" id="totalArea"><form:input
															path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].roomTotalArea"
																type="text"
																class="form-control has2Decimal mandColorClass text-right"
																id="totalAreaval"  readonly="true"
																
																/></td>
										               
										            </tr>
										        </tfoot>
										</table></td>
								</tr>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="text-center padding-top-10">

					<input type="button" id="saveBtn"
						class="btn btn-success btn-submit" onclick="saveRoomData(this)"
						value="<spring:message code="property.dataEntry.save" text="Save"/>" /> 
					<input type="button" id="saveBtn"
						class="btn btn-success btn-danger"
						onclick="backFromRoomdetails(this)" value="<spring:message code="property.Back" text="Back"/>" />
					
				</div>


			</form:form>
		</div>
	</div>
</div>
