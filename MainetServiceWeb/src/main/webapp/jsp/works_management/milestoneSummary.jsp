<!-- Start JSP Necessary Tags -->
<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/works_management/milestone.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>

<%
    response.setContentType("text/html; charset=utf-8");
%>

<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">

			<h2>
				<spring:message code="works.physcial.milestone"
					text="Milestone Summary" />
			</h2>
			<div class="additional-btn">
				<apptags:helpDoc url="FinancialMilestone.html"></apptags:helpDoc>
			</div>
		</div>
		<div class="widget-content padding">
			<div class="mand-label clearfix">
				<span><spring:message code="" text="" /> <i
					class="text-red-1"></i> <spring:message
						code="works.fiels.mandatory.message" text="" /></span>
			</div>
			<form:form action="Milestone.html" class="form-horizontal"
				id="milestone" name="milestone">

				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<div
					class="warning-div error-div alert alert-danger alert-dismissible"
					id="errorDiv" style="display: none;"></div>
				<form:hidden path="mileStoneFlag" id="mileStoneFlag" />
				<div class="form-group">
					<label class="col-sm-2 control-label "><spring:message
							code="project.master.projname" /></label>
					<div class="col-sm-4">
						<form:select path="mileStoneDTO.projId" id="projId"
							class="form-control chosen-select-no-results"
							onchange="getWorkName(this);">
							<form:option value="">
								<spring:message code='work.management.select' />
							  </form:option>
							
							     <c:if test="${userSession.getCurrent().getLanguageId() ne '1'}">
							     <c:forEach items="${command.projectMasterList}"
								var="activeProjName">
								<form:option value="${activeProjName.projId }"
									code="${activeProjName.projId }">${activeProjName.projNameReg}</form:option>	
							  </c:forEach>
						    </c:if>
						    
                               <c:if test="${userSession.getCurrent().getLanguageId() eq '1'}">
							   <c:forEach items="${command.projectMasterList}"
								var="activeProjName">
								<form:option value="${activeProjName.projId }"
									code="${activeProjName.projId }">${activeProjName.projNameEng}</form:option>	
							</c:forEach>
							</c:if>
					 	</form:select>
						
					</div>
					<label class="col-sm-2 control-label"><spring:message
							code="work.def.workname" /></label>
					<div class="col-sm-4">
						<form:select path="mileStoneDTO.workId" id="workId"
							class="form-control chosen-select-no-results">
							<form:option value="">
								<spring:message code='work.management.select' />
							</form:option>
						</form:select>
					</div>
				</div>
				<div class="text-center padding-bottom-10">
					<button class="btn btn-blue-2  search" onclick="searchMilestone();"
						type="button">
						<i class="fa fa-search padding-right-5"></i>
						<spring:message code="works.management.search" text="" />
					</button>
					<button class="btn btn-warning  reset" type="reset">
						<i class="fa fa-undo padding-right-5"></i>
						<spring:message code="works.management.reset" />
					</button>
					<button class="btn btn-primary"
						onclick="openMilestoneAddForm('C');" type="button">
						<i class="fa fa-plus-circle padding-right-5"></i>
						<spring:message code="" text="Add Activity" />
					</button>
					<button class="btn btn-primary"
						onclick="openMilestoneForm('C');" type="button">
						<i class="fa fa-plus-circle padding-right-5"></i>
						<spring:message code="" text="Add Milestone" />
					</button>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="datatables">
						<thead>
							<tr>
								<th scope="col" width="10%" align="center"><spring:message
										code="project.master.projcode" text="Project Code" /><input
									type="hidden" id="srNo"></th>
								<th scope="col" width="18%" align="center"><spring:message
										code="project.master.projname" text="Project Name" /></th>
								<th scope="col" width="12%" align="center"><spring:message
										code="milestone.works.name" text="Works Name" /></th>
								<%-- <th scope="col" width="10%" align="center"><spring:message
										code="milestone.startdate" text="Start Date" /></th>
								<th scope="col" width="10%" align="center"><spring:message
										code="milestone.enddate" text="End date" /></th> --%>
								<th scope="col" width="10%" align="center"><spring:message
										code="milestone.amount" text="Amount" /></th>
								<th scope="col" width="15%" class="text-center"><spring:message
										code="estate.grid.column.action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${projectDtoList}" var="mstDto">
								<tr>
									<%-- <td>${mstDto.projCode}</td>
									<td>${mstDto.projNameEng}</td>
									<td>${mstDto.workName}</td>
									<td>${mstDto.startDate}</td>
									<td>${mstDto.endDate}</td>
									<td align="right">${mstDto.projActualCost}</td>
									<td class="text-center">
										<button type="button" class="btn btn-blue-2 btn-sm"
											title="View Milestone"
											onclick="getActionForDefination(${mstDto.projId},'V',${mstDto.workId});">
											<i class="fa fa-eye"></i>
										</button>
										<button type="button" class="btn btn-warning btn-sm"
											title="Edit Milestone"
											onclick="getActionForDefination(${mstDto.projId},'E',${mstDto.workId});">
											<i class="fa fa-pencil"></i>
										</button> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form:form>
		</div>
	</div>
</div>