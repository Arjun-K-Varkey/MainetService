<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css">
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/asset/assetRequisition.js"></script>


<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="${userSession.moduleDeptCode == 'AST' ? 'asset.requisition.header':'asset.ITrequisition.header'}"/>
			</h2>
			<apptags:helpDoc url="AssetRequisition.html"></apptags:helpDoc>
		</div>

		<div class="widget-content padding">
			<form:form id="astReq" name="astReq" class="form-horizontal"
				action="AssetRequisition.html" method="post">
				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				</div>
				<input type="hidden" id="moduleDeptUrl"  value="${userSession.moduleDeptCode == 'AST' ? 'AssetRequisition.html':'ITAssetRequisition.html'}">
				<form:input type="hidden" path="saveMode"
					value="${command.saveMode}" />
				<div class="panel-group accordion-toggle"
					id="accordion_single_collapse1">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse1" href="#Applicant"><spring:message
										code="asset.information.assettype" /></a>
							</h4>

						</div>

						<div id="Applicant" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="form-group">
									<c:set var="baseLookupCodeACL"
										value="${userSession.moduleDeptCode == 'AST' ? 'ACL':'ICL'}" />
									<label class="col-sm-2 control-label required-control"
										for="assetgroup"> <spring:message
											code="asset.requisition.category" />
									</label>
									<apptags:lookupField path="astRequisitionDTO.astCategory"
										disabled="${command.approvalViewFlag eq 'V'}"
										items="${command.getLevelData(baseLookupCodeACL)}"
										cssClass="form-control chosen-select-no-results"
										hasChildLookup="false" hasId="true" showAll="false"
										selectOptionLabelCode="Select" isMandatory="true" />

									<apptags:input labelCode="asset.requisition.name"
										path="astRequisitionDTO.astName" isMandatory="true"
										maxlegnth="180"
										isDisabled="${command.approvalViewFlag eq 'V'}"></apptags:input>
								</div>

								<div class="form-group">
									<apptags:textArea labelCode="asset.requisition.description"
										path="astRequisitionDTO.astDesc" isMandatory="true"
										isDisabled="${command.approvalViewFlag eq 'V'}"></apptags:textArea>

									<label class="col-sm-2 control-label required-control"
										for="quantity"><spring:message
											code="asset.requisition.quantity" /> <!-- <span class="mand">*</span> --></label>
									<div class="col-sm-4">
										<form:input path="astRequisitionDTO.astQty" type="text"
											disabled="${command.approvalViewFlag eq 'V'}"
											class="form-control  text-right" id="astQty"
											onkeypress="return hasAmount(event, this, 6, 2)" />
										<!-- <span class="input-group-addon"><i class="fa fa-inr"></i></span> -->
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label required-control"
										for="assetgroup"> <spring:message
											code="asset.requisition.location" /></label>
									<div class="col-sm-4">
										<form:select path="astRequisitionDTO.astLoc" id="astLoc"
											disabled="${command.approvalViewFlag eq 'V'}"
											cssClass="form-control chosen-select-no-results">
											<form:option value="">
												<spring:message code="asset.info.select" />
											</form:option>
											<c:forEach items="${command.locList}" var="location">
												<form:option value="${location.locId}">${location.locNameEng}</form:option>
											</c:forEach>
										</form:select>
									</div>

									<label class="col-sm-2 control-label required-control"
										for="assetgroup"> <spring:message
											code="asset.requisition.department" /></label>

									<div class="col-sm-4">
										<form:select path="astRequisitionDTO.astDept" id="astDept"
											disabled="${command.approvalViewFlag eq 'V'}"
											cssClass="form-control chosen-select-no-results">
											<form:option value="">
												<spring:message code="asset.info.select" />
											</form:option>
											<c:forEach items="${command.departmentsList}" var="obj">
												<form:option value="${obj.dpDeptid}"
													code="${obj.dpDeptcode}">${obj.dpDeptdesc}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>

				<c:if test="${command.saveMode eq 'C' }">
					<div class="text-center clear padding-10">
						<button type="button" class="btn btn-success" title="Submit"
							onclick="saveRequisition(this)">
							<i class="fa fa-sign-out padding-right-5" aria-hidden="true"></i>
							<spring:message code="asset.requisition.submit" text="Submit" />
						</button>
						<button type="button" id="resetForm" class="btn btn-warning" title="Reset">
							<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
							<spring:message code="bt.clear" text="Reset" />
						</button>

						<%-- <button type="reset" class="btn btn-warning"
							onclick="window.location.href='AssetRequisition.html'">
							<spring:message code="asset.requisition.reset" />
						</button> --%>
						<button type="button" class="button-input btn btn-danger"
							onclick="window.location.href='${userSession.moduleDeptCode == 'AST' ? 'AssetRequisition.html':'ITAssetRequisition.html'}'">
							<i class="fa fa-chevron-circle-left padding-right-5"></i>
							<spring:message code="asset.requisition.back" text="Back" />
						</button>
					</div>
				</c:if>

				<c:if test="${command.approvalViewFlag eq 'V'}">
					<div class="widget-content padding panel-group accordion-toggle"
						id="accordion_single_collapse1">
						<apptags:CheckerAction hideForward="true" hideSendback="true"></apptags:CheckerAction>
					</div>

					<div class="text-center widget-content padding">
						<button type="button" id="save" class="btn btn-success btn-submit"
							onclick="saveDecisionAction(this);">
							<spring:message code="master.save" text="Save" />
						</button>
						<button type="button" class="button-input btn btn-danger"
							name="button-Cancel" value="Cancel"
							onclick="window.location.href='AdminHome.html'"
							id="button-Cancel">
							<spring:message code="bt.backBtn" text="Back" />
						</button>
					</div>
				</c:if>
			</form:form>


		</div>

	</div>
</div>
