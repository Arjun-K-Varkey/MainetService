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

<script type="text/javascript" src="js/property/viewPropertyDetails.js"></script>
<style>
#totalArea{
    font-size: inherit ! important;
    }
</style>

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="" text="Room Details" /></strong>
			</h2>
		</div>
		<!-- End Main Page Heading -->

		<!-- Start Widget Content -->
		<div class="widget-content padding">
			<form:form action="ViewPropertyDetail.html"
				class="form-horizontal form" name="ViewPropertyDetail"
				id="ViewPropertyDetail">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>

				<c:set var="count" value="0" scope="page" />
				<c:set var="i" value="${command.countOfRow}" scope="page" />
				<form:hidden path="" value="${i}" id="countOfRow" />

				<div class="form-group">
					<c:if test="${command.getServiceShortCode() eq 'CIA'}">
						<apptags:textArea labelCode="property.change.remark" path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${i}].assessmentRemark" isDisabled="true"></apptags:textArea>
					</c:if>
				</div>
				
				<div class="table-overflow-sm" id="">
					<tr class="thirdUnitRow collapse in" id="group-of-rows-0">
						<td colspan="15">

							<table
								class="table table-striped table-bordered">
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
													id="totalAreaval"  readonly="true"/></td>
										     
										 </tr>
										      
										        </tfoot>
							</table>
						</td>
					</tr>
				</div>
				<div class="text-center padding-top-10">
					<button type="button" class="btn btn-danger" onclick="BackToDetails(this)"
				         id="saveBtn"><spring:message code="property.Back" text="Back"/></button>
				</div>
			</form:form>
		</div>
	</div>
</div>
