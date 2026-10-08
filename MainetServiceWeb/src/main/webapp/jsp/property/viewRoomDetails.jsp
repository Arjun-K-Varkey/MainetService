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
<script src="js/property/unitSpecificAdditionalInfo.js"	type="text/javascript"></script>
<script type="text/javascript" src="js/property/ownershipDetailsForm.js"></script>

<style>
#totalArea{
    font-size: inherit ! important;
    }
    input[type='text'], textarea {
		text-transform: uppercase;
	}
</style>
<script>
	$(document).ready(function() {
		$("#roomNo0").val(1);
		reOrderRoomTableIdSequence($("#countOfRow").val());
	});
</script>

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="property.roomdetails" text="Room Details" /></strong>
			</h2>
		</div>
		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">
			<form:form action="SelfAssessmentForm.html"
				class="form-horizontal form" name="frmSelfAssessmentFormRoom"
				id="frmSelfAssessmentFormRoom">
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
				<form:hidden path="serviceShortCode" id="serviceShortCode"/>

				<c:if test="${command.serviceShortCode eq 'CIA'}">  
				<div class="form-group">
						<apptags:textArea labelCode="property.change.remark" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].assessmentRemark" isDisabled="true"></apptags:textArea>
				</div>
				</c:if>
				
				<div class="table-overflow-sm" id="">
					<tr class="thirdUnitRow collapse in" id="group-of-rows-0">
						<td colspan="15">

							<table id="roomDetailstable"
								class="table table-striped table-bordered appendableClass roomDetailstable">
								<tbody>
									<tr>
										<th><spring:message code="roomdetails.roomNo" /></th>
										<th><spring:message code="roomdetails.roomType" /></th>
										<th><spring:message code="roomdetails.roomLength" /></th>
										<th><spring:message code="roomdetails.roomWidth" /></th>
										<th><spring:message code="roomdetails.roomArea" /></th>

									</tr>
									<c:forEach var="roomDetails"
										items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentDetailDtoList().get(i).getRoomDetailsDtoList()}"
										varStatus="status">
										<tr class="thirdRowRoomDetails">
											<td class="text-center">${roomDetails.roomNo}</td>
											<td class="text-center">${roomDetails.roomTypeDesc}</td>
											<fmt:formatNumber type="number" value="${roomDetails.roomLength}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="roomLength"/>
											<td class="text-center">${roomLength}</td>
											<fmt:formatNumber type="number" value="${roomDetails.roomWidth}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="roomWidth"/>
											<td class="text-center">${roomWidth}</td>
											<fmt:formatNumber type="number" value="${roomDetails.roomArea}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="roomArea"/>
											<td class="text-center">${roomArea}</td>

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
																class="form-control has2Decimal mandColorClass text-center"
																id="totalAreaval"  readonly="true"
																
																/></td>
										               
										            </tr>
										        </tfoot>
							</table>
						</td>
					</tr>
				</div>
				<div class="text-center padding-top-10">
					<button type="button" class="btn btn-danger" onclick="backToAuthPage(this)"
				         id="saveBtn"><spring:message code="property.Back" text="Back"/></button>
				</div>
			</form:form>
		</div>
	</div>
</div>
