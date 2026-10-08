<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/property/mutation.js"></script>
<script type="text/javascript" src="js/common/dsc.js"></script>

<style>
.btn.btn-primary > a {
color: #ffffff;
}
</style>
<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<strong><spring:message code="propertyTax.Mutation"
						text="Mutation" /></strong>
			</h2>
			<div class="additional-btn">
				<apptags:helpDoc url="MutationPrintCertificate.html"></apptags:helpDoc>
			</div>
		</div>

		<div class="widget-content padding">
			<form:form action="MutationPrintCertificate.html"
				class="form-horizontal form" name="MutationPrintCertificate"
				id="MutationPrintCertificate">
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<form:hidden path="billingMethod" id="billingMethod" />

				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv"></div>
				<div class="form-group">
					<spring:message code="property.PleaseEnterPropertyNo"
						text="Please enter property no" var="propNo" />
					<spring:message code="property.PleaseEnterOldPropertyNo"
						text="Please enter old property no" var="oldPropNo" />
					<apptags:input labelCode="property.ChangeInAss.EnterPropertyNo"
						path="provisionalAssesmentMstDto.assNo" cssClass="preventSpace"
						placeholder="${propNo}"></apptags:input>
					<apptags:input labelCode="property.ChangeInAss.oldpid"
						path="provisionalAssesmentMstDto.assOldpropno"
						cssClass="preventSpace" placeholder="${oldPropNo}"></apptags:input>
				</div>

				<div class="form-group" id="showFlatNoForPrint">
					<%-- <spring:message code="property.ApplicationNo" text="Application no"
						var="appNo" />
					<apptags:input labelCode="Application no"
						path="provisionalAssesmentMstDto.apmApplicationId"
						cssClass="preventSpace" placeholder="${appNo}"></apptags:input> --%>
						
					<label class="col-sm-2 control-label" ><spring:message
								code="mutation.flat.no" text="Flat No" /></label>
					<div class="col-sm-4" >
							<form:select path="provisionalAssesmentMstDto.flatNo" id="flatNo"
								class="form-control mandColorClass chosen-select-no-results">
								<form:option value="">
									<spring:message code='adh.select' />
								</form:option>
								<c:forEach items="${command.flatNoList}" var="flatNoList">
									<form:option value="${flatNoList}">${flatNoList}</form:option>
								</c:forEach>
							</form:select>
					</div>
				</div>

				<div class="text-center padding-bottom-10">
					<button type="button" class="btn btn-blue-2" id="serchBtnDirect"
						onclick="SearchMutation(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.changeInAss.Search" />
					</button>

					<button type="button" class="btn btn-blue-2" id="serchBtn"
						onclick="redirectToSearchPropNoPrintCertificate(this)">
						<i class="fa fa-search"></i>
						<spring:message code="property.search" text="Search" />
					</button>
					
					<button type="button" class="btn btn-warning resetSearch"
						onclick="window.location.href = 'MutationPrintCertificate.html'">
						<spring:message code="property.reset" text="Reset" />
					</button>
				</div>
				<select name="ddl1" id="ddl1">
				<option value="0"><spring:message code="property.select.certificate" text="Select Certificate" /></option>
				</select>

				<div class="table-responsive clear">
				<spring:eval
				expression="T(com.abm.mainet.common.utility.CommonMasterUtility).getValueFromPrefixLookUp('C1','CRD',${UserSession.organisation}).getOtherField()" var="otherField"/>
					<table class="table table-striped table-bordered"
						id="MutationPrintCertificateTable">
						<thead>
							<tr>
								<th width="10%" align="center"><spring:message
										code="propertyTax.SrNo" text="Sr.No" /></th>
								<th width="20%" align="center"><spring:message
										code="property.PropertyNo" text="Property No" /></th>
								<th width="20%" align="center"><spring:message
										code="property.ApplicationNo" text="Application No" /></th>
								<th width="20%" align="center"><spring:message
										code="ownerdetail.Ownername" text="Owner Name" /></th>
								<%--<th width="10%" align="center"><spring:message
										code="property.CertificateNo" text="Certificate No" /></th> --%>
								<th width="30%" align="center"><spring:message
										code="property.report.Print" text="Print" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${data}" var="mutation" varStatus="item">
								<tr>
									<td class="text-center">${item.count}</td>
									<td class="text-center">${mutation.proAssNo}</td>
									<td class="text-center">${mutation.apmApplicationId}</td>
									<td class="text-center">
										<c:forEach items="${mutation.getPropTransferOwnerList()}" var="owner">
											${owner.ownerName}<br>
										</c:forEach>
									</td>
									<%--<td>${birth.certificateNo}</td> --%>
									<td class="text-center">
										<button type="button" class="btn btn-primary btn-sm"
											onclick="printCertificate('${mutation.proAssNo}','${mutation.apmApplicationId}','${mutation.flatNo}')">
											<spring:message code="property.downloadCertificate" text="Download Certificate"/>
										</button>
										 <button type="button" class="btn btn-primary btn-sm"
												onclick="signCertificate('${mutation.proAssNo}','${mutation.certificateNo}',${mutation.apmApplicationId},'MutationPrintCertificate.html','${otherField}')">
												<spring:message code="property.sign.certificate" text="Sign Certificate" />
										</button> 										
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form:form>
		</div>
	</div>
</div>