<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>

<script type="text/javascript" src="js/rnl/master/rl-file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript"
	src="js/rnl/service/dramaProducerMasterForm.js"></script>

<apptags:breadcrumb></apptags:breadcrumb>

<!-- Start Content here -->
<div class="content animated slideInDown">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="rnl.drama.producer.master"
					text="Drama Producer Master"></spring:message>
			</h2>
			<div class="additional-btn">
				<a href="#" data-toggle="tooltip" data-original-title="Help"><i
					class="fa fa-question-circle fa-lg"></i><span class="hide"><spring:message
							code="rnl.book.help" text="Help"></spring:message></span></a>
			</div>
		</div>
		<div class="widget-content padding">
			<form:form method="POST" action="DramaProducerMasterController.html"
				class="form-horizontal" name="dramaProducerMastForm"
				id="dramaProducerMastForm">

				<div class="compalint-error-div">
					<jsp:include page="/jsp/tiles/validationerror.jsp" />
					<div
						class="warning-div error-div alert alert-danger alert-dismissible"
						id="errorDiv"></div>
				</div>

				<div class="panel-group accordion-toggle"
					id="accordion_single_collapse">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse"
									href="#dramaProducerDetails"><spring:message
										code="rnl.drama.producer.info"
										text="Drama Producer Information" /> </a>
							</h4>
						</div>

						<div id="dramaMaster" class="panel-collapse collapse in">
							<div class="panel-body">

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="dramaCompanyName"><spring:message
											code='rnl.drama.company.name' text="Drama Company Name" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control alphaNumeric"
											path="dramaDto.dramaCompanyName" id="dramaCompanyName"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="500" />
									</div>

									<label class="control-label col-sm-2 required-control"
										for="dramaCompanyAddress"><spring:message
											code='rnl.drama.company.address' text="Drama Company Address" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="dramaDto.dramaCompanyAddress" id="dramaCompanyAddress"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="500" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="dramaCompanyContNo"><spring:message
											code='rnl.drama.company.contact.number'
											text="Drama Company Contact Number" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control hasNumber"
											path="dramaDto.dramaCompanyContNo" id="dramaCompanyContNo"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="10" />
									</div>

									<label class="control-label col-sm-2" for="dramaCompanyEmail"><spring:message
											code='rnl.drama.company.email.address'
											text="Drama Company Email Address" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="dramaDto.dramaCompanyEmail" id="dramaCompanyEmail"
											disabled="${command.viewMode eq 'V' ? true : false}" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="producerName"><spring:message
											code='rnl.drama.producer.name' text="Producer Name" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control alphaNumeric"
											path="dramaDto.producerName" id="producerName"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="500" />
									</div>

									<label class="control-label col-sm-2 required-control"
										for="producerAddress"><spring:message
											code='rnl.drama.producer.address' text="Producer Address" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="dramaDto.producerAddress" id="producerAddress"
											maxlength="500"
											disabled="${command.viewMode eq 'V' ? true : false}" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="producerMobNo"><spring:message
											code='rnl.drama.producer.mob.no'
											text="Producer Mobile Number" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control hasNumber"
											path="dramaDto.producerMobNo" id="producerMobNo"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="10" />
									</div>

									<label class="control-label col-sm-2 required-control"
										for="producerEmail"><spring:message
											code='rnl.drama.producer.email.address'
											text="Producer Email Address" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control"
											path="dramaDto.producerEmail" id="producerEmail"
											maxlength="500"
											disabled="${command.viewMode eq 'V' ? true : false}" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2 required-control"
										for="regAuthorityName"><spring:message
											code='rnl.drama.regis.auth.name'
											text="Registration Authority Name" /></label>
									<div class="col-sm-4">
										<form:input type="text" class="form-control alphaNumeric"
											path="dramaDto.regAuthorityName" id="regAuthorityName"
											disabled="${command.viewMode eq 'V' ? true : false}"
											maxlength="500" />
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>

				<div class="panel-group accordion-toggle"
					id="accordion_single_collapse">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" class=""
									data-parent="#accordion_single_collapse"
									href="#dramaDocDetails"><spring:message
										code='rl.property.label.Upload.Attachment'
										text="Upload Attachment" /> </a>
							</h4>
						</div>

						<div id="dramaDoc" class="panel-collapse collapse in">
							<div class="panel-body">
								<table class="table table-hover table-bordered table-striped">
									<tbody>
										<tr>
											<th><spring:message code='estate.table.column.srno' /></th>
											<th><spring:message code='estate.table.column.doc' /></th>
											<th><spring:message code='estate.table.column.header' /></th>
										</tr>
										<tr>
											<td>1</td>
											<td><spring:message code='rnl.drama.doc.censo.certi' /></td>
											<td><c:if test="${command.viewMode eq 'V'}">
													<c:forEach items="${command.documentList}" var="lookUp"
														varStatus="lk">
														<c:if test="${lookUp.serialNo eq 0}">
															<apptags:filedownload filename="${lookUp.attFname}"
																filePath="${lookUp.attPath}"
																actionUrl="DramaProducerMasterController.html?Download" />
														</c:if>
													</c:forEach>
												</c:if> <c:if test="${command.viewMode ne 'V'}">
													<apptags:formField fieldType="7"
														fieldPath="dramaDto.docsPath1" labelCode=""
														currentCount="0" showFileNameHTMLId="true"
														fileSize="COMMOM_MAX_SIZE"
														maxFileCount="CHECKLIST_MAX_UPLOAD_COUNT" folderName="0"
														validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS" />
												</c:if></td>
										</tr>
										<tr>
											<td>2</td>
											<td><spring:message code='rnl.drama.doc.others' /></td>
											<td><c:if test="${command.viewMode eq 'V'}">
													<c:forEach items="${command.documentList}" var="lookUp"
														varStatus="lk">
														<c:if test="${lookUp.serialNo eq 1}">
															<apptags:filedownload filename="${lookUp.attFname}"
																filePath="${lookUp.attPath}"
																actionUrl="DramaProducerMasterController.html?Download" />
														</c:if>
													</c:forEach>
												</c:if> <c:if test="${command.viewMode ne 'V'}">
													<apptags:formField fieldType="7" labelCode="" hasId="true"
														fieldPath="dramaDto.docsPath2" isMandatory="false"
														showFileNameHTMLId="true" fileSize="COMMOM_MAX_SIZE"
														maxFileCount="CHECKLIST_MAX_UPLOAD_COUNT"
														validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
														folderName="1" currentCount="1" />
												</c:if></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>


				<div class="text-center padding-bottom-10">
					<c:if test="${command.viewMode ne 'V'}">
						<button type="button" class="btn btn-success"
							title='<spring:message code="bt.save" text="Submit" />'
							onclick="saveDramaMastForm(this);">
							<spring:message code="bt.save" text="Submit" />
						</button>
					</c:if>

					<apptags:backButton url="DramaProducerMasterController.html"></apptags:backButton>
				</div>

			</form:form>
		</div>
	</div>
</div>
