<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script>
	function printContent(el) {
		var restorepage = document.body.innerHTML;
		var printcontent = document.getElementById(el).innerHTML;
		document.body.innerHTML = printcontent;
		window.print();
		document.body.innerHTML = restorepage;
	}
</script>
<style>
.margin-top-sign{ 
	margin-top: 5.5rem !important;
}

.margin-left-sign{
	margin-left: -7px !important;
}
</style>
<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget" id="receipt">
		<style>
		.widget-content{
			width:100%;
			margin:auto;
			padding: 2rem !important;
		}
		/* .license-section{
			border-right:2px solid #000;
		} */
		.new-plumber-license{
			padding:0px !important;
			border:2px solid #000;
			overflow: hidden;
			display: inline-flex;
		}
		.separator {
		  	align-self: stretch;
		  	width: 0;
		  	height: auto;
		  	border-right: inherit;
		}
	</style>
		<div class="widget-content padding">
			<div class="new-plumber-license">
			<div class="col-xs-6 col-sm-6 license-section">
			<div class="row">
				<div class="col-xs-2 text-left org-logo-div">
					<img width="80" src="${userSession.getCurrent().orgLogoPath}">
				</div>
				<div class="col-xs-8 text-center">
					<h4 class="margin-bottom-0">
						<%-- <c:if test="${userSession.languageId eq 1}">
							<spring:message code=""
								text="${userSession.organisation.ONlsOrgname}" />
						</c:if>
						<c:if test="${userSession.languageId ne 1}"> --%>
							<spring:message code=""
								text="${userSession.organisation.oNlsOrgnameMar}" />
						<%-- </c:if> --%>
					</h4>
					<p>${command.deptNameMar}</p>
					<p>${command.serviceNameMar}</p>
				</div>

				<div class="col-xs-2 text-right">
					<%-- <img width="80" src="${userSession.getCurrent().orgLogoPath}"> --%>
				</div>
			</div>

			<div class="form-group">
				
				<div class="row">
					<div class="col-sm-3 col-xs-3">
						 <img class="img_thumbnail" src="${command.fileDownLoadPath}" alt="license holder image"> 
						
					</div>
					<div class="col-xs-9 text-left margin-top-10">
						<p>
							<spring:message code="water.plumber.licno.print" />
							:${command.plumLicNo}
						</p>
						<p>
							<spring:message code="water.plumberLicense.name.print" />
							:${command.plumberFullName}
						</p>
						<p>
							<spring:message code="water.plumberLicense.address.print" />
							:${command.plumAddress}
						</p>
						<%-- <p>
							<spring:message code="water.plumberLicense.licensePeriod" />
							:${command.plumLicFrmDate}
							<spring:message code="water.plumberLicense.to" />
							${command.plumLicToDate}
						</p> --%>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 text-left margin-top-10">
						<spring:message code="water.plumberLicense.licensePeriod.print" />
							:${command.plumLicFrmDate}
							<spring:message code="water.plumberLicense.to" />
							${command.plumLicToDate}
					</div>
				</div>
			</div>
			<%-- <div class="form-group clearfix">
				<p class="margin-bottom-10 margin-top-10">
					<spring:message code="water.plumberLicense.termsAndConditions" />
				</p>
				<table class="table">
					<c:forEach items="${command.tbApprejMas}" var="singleDoc"
						varStatus="count">
						<tr>
							<td width="50">${count.index+1}</td>
							<td>${singleDoc.artRemarks}</td>
						</tr>
					</c:forEach>
				</table>
			</div> --%>

			<div class="row">
				<div class="col-xs-6 text-left margin-top-sign">
					<p>
						<spring:message code="water.plumberLicense.licenseHolderSign.print" />
					</p>
				</div>
				<div class="col-xs-6  text-right margin-left-sign margin-top-sign">
					<!-- <img src="../../images/sign.png" style="width:150px;"> -->
					<p class="text-center">
						<spring:message code="siteinspection.authorizedsign.print" />
					</p>
					<p class="text-center">
						<spring:message code="kalyan.dombivali.corporation.print" />
					</p>
					
				</div>
			</div>
			<%-- <c:if test="${command.serviceCode eq 'DPL' }">
				<p align="center">
					<spring:message code="" text="Duplicate Plumber License" />
				</p>
			</c:if> --%>
			</div>
			 <div class="separator"></div>
			<div class="col-xs-6 col-sm-6 terms-section margin-top-10">
				<c:forEach var="terms" items="${command.termsConditionList}"
					varStatus="count">
					<p>${count.count}) ${terms.artRemarksreg}</p>
				</c:forEach>
			</div>
		  </div>	
		  <div class="text-center margin-top-20">
				<button onclick="printContent('receipt')"
					class="btn btn-primary hidden-print">
					<i class="icon-print-2"></i> <spring:message code="water.btn.print" text="Print"/>
				</button>
				
	           <input type="button"
					onclick="window.location.href='AdminHome.html'"
					class="btn btn-danger  hidden-print" value="<spring:message code="water.btn.back" text="Back"/>">
		  </div>
		</div>
		


	</div>
</div>

