<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css">
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/script-library.js"></script>
<script src="js/material_mgmt/master/propertyList.js"
	type="text/javascript"></script>
<div id="searchIndPage">
	<apptags:breadcrumb></apptags:breadcrumb>
	<div class="content">
		<div class="widget">
			<div class="widget-header">
				<h2>
					<spring:message code="department.indent.summaryHeading"
						text="Department Indent Summary" />
				</h2>
				<apptags:helpDoc url="EstatePropMas.html"></apptags:helpDoc>
			</div>
			<div class="pagediv">
				<div class="widget-content padding">
					<form:form id="defSummaryFrm" name="expiredContractDetailsList"
						class="form-horizontal" action="EstatePropMas.html" method="post">
						<jsp:include page="/jsp/tiles/validationerror.jsp" />
						<div
							class="warning-div error-div alert alert-danger alert-dismissible"
							id="errorDiv"></div>


						<div class="table-responsive clear">
							<table class="table table-striped table-bordered"
								id="searchStore">
								<thead>
									<tr>
										<th align="center"><spring:message
												code="store.master.srno" text="Sr No." /></th>
										<th align="center"><spring:message
												code="estate.label.name" text="Estate Name" /></th>
										<th align="center"><spring:message
												code="rnl.master.property.name" text="Property Name" /></th>
										<th align="center"><spring:message
												code="rnl.master.property" text="Property No." /></th>
										<th align="center"><spring:message
												code="rnl.expCont.galaNo"
												text="Gala No" /></th>
										<th align="center"><spring:message
												code="rnl.leaseSrt.Date" text="Lease Start Date" /></th>
										<th align="center"><spring:message
												code="rnl.leaseEnd.Date" text="Lease End Date" /></th>
										<th align="center"><spring:message
												code="rnl.outstanding.report.outAmt" text="Outstanding Amount" /></th>
										<th align="center"><spring:message
												code="rnl.propMast.expContDetails.installmentAmt" text="Installment Amount" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${command.estatePropMasterDtoList}"
										var="summaryDTO" varStatus="index">
										<tr>
											<td class="text-center">${index.count}</td>
											<td align="center">${summaryDTO.estateNameEng}</td>
											<td align="center">${summaryDTO.propertyName}</td>
											<td align="center">${summaryDTO.propertyNo}</td>
											<td align="center">${summaryDTO.oldPropertyNo}</td>
											<td align="center">${summaryDTO.leaseStartDate}</td>
											<td align="center">${summaryDTO.leaseEndDate}</td>
											<td align="center">${summaryDTO.outstandingAmt}</td>
											<td align="center">${summaryDTO.monthlyRent}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						
						<div class="text-center clear padding-10">
						<apptags:backButton url="EstatePropMas.html"></apptags:backButton>
						</div>

					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
