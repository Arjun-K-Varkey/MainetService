<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/common/onlineTransactionStatus.js"></script>


<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">

		<div class="additional-btn">
			<a href="#" data-toggle="tooltip" data-original-title="Help"> <i
				class="fa fa-question-circle fa-lg"></i> <span class="hide">Help</span>
			</a>
		</div>
		<div class="widget-content padding">
			<form:form action="OnlineTransactionStatus.html" method="post"
				class="form-horizontal" name="onlineTransaction"
				id="onlineTransaction">

				<jsp:include page="/jsp/tiles/validationerror.jsp" />

				<div class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span id="errorId"></span>
				</div>

				<h4 class="margin-top-0">
					<spring:message code=""
						text="Online Transaction Status" />
				</h4>

				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="transaction.id" text="Transaction Id." /></label>
					<div class="col-sm-4">
						<form:input path="" id="transactionId"
							class="form-control" maxlength="100" />
					</div>
				
				<label class="col-sm-2 control-label " for="id_status"><spring:message
											code="online.transaction.status" text="Status" /></label>
					<div class="col-sm-4">
						<form:select class="form-control" id="status"
							path="" data-rule-prefixvalidation="true">
							<form:option code="care.select" value="">
								<spring:message code="care.select" text="Select" />
							</form:option>
							<form:option code="care.closed" value="success">
								<spring:message code="online.transaction.success" text="Success" />
							</form:option>
							<form:option code="care.pending" value="pending">
								<spring:message code="care.pending" text="Pending" />
							</form:option>
							<form:option code="care.rejected" value="failure">
								<spring:message code="online.transaction.failure" text="Failure" />
							</form:option>
						</form:select>
					</div>
				</div>
				<div class="form-group">

					<label class="col-sm-2 control-label"><spring:message
							code="cfc.application.date" text="Application Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
							<form:input path="" id="appDate"
								class="form-control datepicker" readonly="true"/>
							<label class="input-group-addon"><i
								class="fa fa-calendar"></i><span class="hide"> <spring:message
										code="" text="icon" /></span><input type="hidden"></label>
						</div>
					</div>

				</div>

				<div class="text-center margin-bottom-10">

					<button type="button" class="btn btn-success" title="Search"
						onclick="searchTransactionStatus()">
						<i class="fa fa-search padding-right-5" aria-hidden="true"></i>
						<spring:message code="SFT.search" text="Search"></spring:message>
					</button>

					<button type="button" class="btn btn-warning" title="Reset"
						onclick="ResetForm()">
						<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
						<spring:message code="NHP.reset" text="Reset"></spring:message>
					</button>
					
					<button type="button" class="btn btn-danger" id="back"
						onclick="window.location.href='AdminHome.html'">
						<spring:message code="NHP.back" text="Back"></spring:message>
					</button>

				</div>


					<div class="margin-top-10">
						<table class="table table-striped table-condensed table-bordered"
							id="onlineTransactionStatus">

							<thead>
								<tr>
									<th width="10%"><spring:message code="transaction.id"
											text="Transaction Id" /></th>
									<th width="15%"><spring:message code="cfc.date"
											text="Date" /></th>
									<th width="15%"><spring:message code="receipt.refno" text="Reference No." /></th>
									<th width="15%"><spring:message code="online.transaction.flat.no"
											text="Flat No." /></th>
									<th width="15%"><spring:message code="online.transaction.amount"
											text="Amount" /></th>
									<th width="10%"><spring:message code="online.transaction.success" text="Status" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${command.transactionMas}" var="dto"
									varStatus="index">
									<tr>

										<td class="text-center">${dto.tranCmId}</td>
										<td class="text-center">${dto.refDate}</td>
										<td class="text-center">${dto.referenceId}</td>
										<td class="text-center">${dto.flatNo}</td>
										<td class="text-center">${dto.sendAmount}</td>
										<td class="text-center">${dto.recvStatus}</td>
									</tr>
								</c:forEach>
							</tbody>


						</table> 
					</div>

			</form:form>
		</div>
	</div>
</div>

