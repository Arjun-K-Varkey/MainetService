<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/material_mgmt/master/StoreMaster.js"></script>
<%
    response.setContentType("text/html; charset=utf-8");
%>
<style>
.input-group-field {
	display: table-cell;
	width: 114px;
}
</style>
<!-- Start Content here -->
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="store.master.heading"
					text="Store Master" />
			</h2>
			<apptags:helpDoc url="StoreMaster.html" />
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="material.management.mand"
						text="Field with" /> <i class="text-red-1">*</i> <spring:message
						code="material.management.mand.field" text="is mandatory" /> </span>
			</div>
			<form:form action="StoreMaster.html" name="StoreMasterForm" id="StoreMasterId"
				class="form-horizontal">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>
				<h4>
					<spring:message code="store.master.details" text="Store Details" />
				</h4>
				<div class="form-group">
					<apptags:input labelCode="store.master.code"
						path="StoreMasterDTO.storeCode" isMandatory="true"
						cssClass="form control mandcolour"  isReadonly="true"  >
					</apptags:input>
					<apptags:input labelCode="store.master.name"
						path="StoreMasterDTO.storeName" isMandatory="true"
						cssClass="form control mandcolour hasNameClass" isReadonly="true">
					</apptags:input>
				</div>
				
				<div class="form-group">
					<label class="control-label col-sm-2 required-control "
						for="Location"><spring:message
							code="store.master.location" text="Location" /></label>
					<div class="col-sm-4">
						<form:select path="StoreMasterDTO.location"
							cssClass="form-control   mandColorClass" id="Location"
							data-rule-required="true" disabled="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<c:forEach items="${command.locList}" var="lookUp">
								<form:option value="${lookUp.locId}" code="">${lookUp.locNameEng}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<apptags:input labelCode="store.master.address"
						 path="StoreMasterDTO.address" isMandatory="false"
						 cssClass="form control mandcolour"  isReadonly="true">
					</apptags:input>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 required-control "
						for="Store Incharge"><spring:message
							code="store.master.store.incharge" text="Store Incharge" /></label>
					   <div class="col-sm-4">
						<form:select path="StoreMasterDTO.storeIncharge"
							cssClass="form-control   mandColorClass" id="storeInchargeId"
							data-rule-required="true" disabled="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<c:forEach items="${command.list}" var="Emp">
								<form:option value="${Emp.empId}" code="">${Emp.empname}</form:option>
							</c:forEach>
						</form:select>
					   </div>
						<label class="control-label col-sm-2 required-control "
						for="StoreStatus"><spring:message
							code="store.master.status" text="Status" /></label>
						<div class="col-sm-4">
						<form:select path="StoreMasterDTO.statusData"
							cssClass="form-control   mandColorClass" id="statusId"
							data-rule-required="true" disabled="true">
							<form:option value="">
								<spring:message code="material.management.select" text="Select" />
							</form:option>
							<form:option value="Y" code="">Active</form:option>
							<form:option value="N" code="">Inactive</form:option>
						</form:select>
					   </div>
				</div><br><br>
				<h4>
					<spring:message code="store.master.item.group.details" text="Item Group Details" />
				</h4>
				<div class="panel-default">
					<div class="panel-collapse collapse in" id="UnitDetail">
						<div class=" clear padding-10">
							<c:set var="d" value="0" scope="page" />
							<table id="unitDetailTable"
								class="table table-striped table-bordered appendableClass unitDetails">
								<thead>
									<tr>
										<th scope="col" width="10%"><spring:message
												code="store.master.item.group.name" text="Item Group Name" /></th>
										<th width="2%"><a href="javascript:void(0);" data-toggle="tooltip"
											data-original-title="Add"
											class="addPurReq btn btn-success btn-sm unit" id="addUnitRow"><i
												class="fa fa-plus-circle"></i> </a></th>
							        </tr>
							     </thead>
							     <tbody>
							     <tr class="firstUnitRow">
							      <td>
							      <div class="col-sm-4" class="text-center">
						            <form:select path="StoreMasterDTO.storeGrMappingDtoList[${d}].itemGroupId"
							           cssClass="form-control   mandColorClass" id="itemGroupId${d}"
							            data-rule-required="true" disabled="true">
											<form:option value="">
												<spring:message code="material.management.select" text="Select" />
											</form:option>
											<c:forEach items="${command.lookupList}" var="lookUp">
												<form:option value="${lookUp.lookUpId}" code="">${lookUp.lookUpDesc}</form:option>
											</c:forEach>
										</form:select>
					                </div>
					             </td>
							      <td class="text-center" ><a href="#" data-toggle="tooltip"
												data-placement="top" class="btn btn-danger btn-sm"
												onclick="deleteEntry($(this),'removedIds');"> <strong
													class="fa fa-trash"></strong> <span class="hide"><spring:message
															code="material.management.delete" text="Delete" /></span>
												</a>
										</td>
							     </tr>
							     <c:set var="d" value="${d + 1}" scope="page" />
							     </tbody>
							</table>
						  </div>
						</div>
					</div>
					<div class="text-center padding-bottom-10">
					<button type="button" class="button-input btn btn-danger"
								name="button-Cancel" value="Cancel" style=""
								onclick="backStoreMasterForm();" id="button-Cancel">
								<spring:message code="material.management.back" text="Back" />
					</button>
				</div>
			 </form:form>
		</div>
	</div>
</div>
