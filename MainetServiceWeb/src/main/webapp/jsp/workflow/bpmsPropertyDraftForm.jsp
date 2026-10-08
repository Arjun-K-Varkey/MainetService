<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/workflow/bpmsPropertyDraftForm.js"></script>
<link rel="stylesheet"
	href="assets/libs/bootstrap-multiselect/css/bootstrap-multiselect.css"
	type="text/css">
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script>
<script>
$(function() {
	$(document).ready(
			function() {
				$("#id_itemOpeningBalance").dataTable({
					"oLanguage" : {
						"sSearch" : ""
					},
					"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
					"iDisplayLength" : 5,
					"bInfo" : true,
					"lengthChange" : true
				});
			});

 
});
</script>



<apptags:breadcrumb></apptags:breadcrumb>
<div class="content animated slideInDown">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code=""
					text="BPMS Property Draft" />
			</h2>
			<apptags:helpDoc url="BPMSPropertyDraft.html"></apptags:helpDoc>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="contract.breadcrumb.fieldwith"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="common.master.mandatory" text="is mandatory" /> </span>
			</div>
			<div class="error-div alert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<ul>
					<li><label id="errorId"></label></li>
				</ul>
			</div>
			 <form:form action="ForwardComplaint.html" method="POST"
				name="ForwardComplaintForm" class="form-horizontal"
				id="ForwardComplaintForm">
				<form:hidden path="viewMode" id="viewMode" />
				<div class="form-group">
					<label class="col-sm-2 control-label required-control"><spring:message
							code="bpms.integration.proId" text="BPMS No" /></label>
					<div class="col-sm-4">	
						<form:select id="propId" path="proposalNo"
							cssClass="form-control chosen-select-no-results">
							<form:option value="">
								<spring:message code="bpms.select.proNo" text="Select Proposal No." />
							</form:option>
							<c:forEach var="dto" items="${command.proposalNoList}"
								varStatus="status">

								<form:option value="${dto}">${dto}</form:option>

							</c:forEach>
						</form:select>
					</div>

				</div>




				<div class="text-center padding-bottom-20">
					<button type="button" class="btn btn-success btn-submit"
							id="" onclick="BpmsSearchButton(this)">
							<spring:message code="property.search" text="Search" />
					</button>
					<button type="button" class="btn btn-danger"
							onclick="window.location.href='AdminHome.html'">
							<spring:message code="care.back" text="Back" />
					</button>
				</div>

				<br />
				
				<div class="table-responsive">
							<table id="id_itemOpeningBalance" summary="Store Master Data"
								class="table table-bordered table-striped rcm">
								<thead>
									<tr>
										<th><spring:message code="store.master.srno"
												text="Sr.No." /></th>
												
										<th><spring:message code="bpms.integration.proId"
												text="" /></th>		
										
												
										<th><spring:message code="bpms.appDate"
												text="Bpms Application Received Date" /></th>
												
										<th><spring:message code="property.OwnerName"
												text="Owner Name" /></th>
												
										<th><spring:message code="property.duesCheck.ward"
												text="ward" /></th>
												
										<th><spring:message code="ownerdetail.OwnerAdd"
												text="Owner Address" /></th>
												
										<th width="150"><spring:message
												code="material.management.action" text="Action" /></th>

									</tr>
								</thead>
								<tbody>
									<c:forEach items="${command.bpmsSummaryPageDtoList}"
										var="data" varStatus="loop">
										<tr>
											<td class="text-center"><c:out value="${loop.index + 1}"></c:out></td>
											<td class="text-center">${data.proposalNumber}</td>
											
											<td class="text-center">${data.updatedDate}</td>
											<td class="text-center">${data.ownerName}</td>
											<td class="text-center">${data.ward}</td>
											<td class="text-center">${data.ownerAddress}</td>

											<td class="text-center">
												<button type="button" class="btn btn-blue-2 btn-sm"
												onClick="BpmsView(${data.proposalNumber},'V')"
													title="<spring:message code="material.management.view" text="View" />">
													<i class="fa fa-eye"></i>
												</button>
												<button type="button" class="btn btn-warning btn-sm btn-sm"
													onClick="editBpmsForm(${data.proposalNumber},'E')"
													title="<spring:message code="material.management.edit" text="Edit"></spring:message>">
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
												
												</td>										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>


			</form:form>
		</div>
	</div>
</div>
