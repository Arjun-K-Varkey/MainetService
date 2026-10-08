<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script>
<script type="text/javascript"  src="js/masters/LandRateMaster.js"></script>
<style>
	.non-edited {
	  pointer-events: none;        
	}
	.hide{
		display:none;
	}
</style>

<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="Rent Master Form" text="Rent Master Form" />
	<%-- 			<apptags:helpDoc url="LandRateMasterController.html"></apptags:helpDoc> --%>
			</h2>
		</div>
		<div class="widget-content padding">

			<div
				class="warning-div error-div aaaaaalert alert-danger alert-dismissible"
				id="errorDivId" style="display: none;">
				<button type="button" class="close" onclick="closeOutErrBox()"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<span id="errorId"></span>
			</div>

			<%-- <form:form action="LandRateMasterController.html" id="LandRateMaster"
				method="POST" commandName="command" class="form-horizontal form"> --%>
				<form:form  modelAttribute = "landRateMasterBean" action="LandRateMasterController.html" method="POST" class="form-horizontal form" id="LandRateMaster" name="LandRateMaster">


				<div class="form-group">
					<label class="control-label col-sm-2 required-control" for="Census">
						<spring:message code="master.area" text="Area" />
					</label>
					<div class="col-sm-4">
			
			 	<c:choose>
							<c:when test="${landRateMasterBean.btnShow ne 'Y'}"> 
								<form:select class="form-control" id="areaId" onchange=""
									path="rateMaterDTO.area">
									<option value=""><spring:message code="account.select"
											text="Select" /></option>
									<c:forEach items="${area}" var="activeItem">
										<option value="${activeItem.lookUpCode}"
											code="${activeItem.descLangFirst}">${activeItem.descLangFirst}</option>
									</c:forEach>
								</form:select>
						 </c:when>
							<c:otherwise> 
								<form:input path="rateMaterDTO.area" id="surveyNo"
									cssClass="required-control form-control" readonly="true" />
						 	</c:otherwise>
						</c:choose> 


					</div>
		
					<label class="col-sm-2 control-label"
								for="Survey No"><spring:message code="master.surveyNo"
									text="Survey No" /></label>
				   <div class="col-sm-4">
				         <form:input path="rateMaterDTO.surveyNo" id="surveyNo"
								cssClass="required-control form-control" />
				   </div>
				</div>
				 <c:if test="${ empty landRateMasterBean.lndRateList}">
				<div class="text-center margin-top-10">
					<input type="button" onClick="fetchRentMasterData(this);"
						value="Search" class="btn btn-blue-2" id="Search"/>
					<button type="Reset" class="btn btn-warning"
						onclick="resetForm(this);"/>
						<spring:message code="reset" text="Reset" />
					</button>
					<input type="button"
						onclick="window.location.href='AdminHome.html'"
						class="btn btn-danger  hidden-print" value="Back"/>
				</div>
				</c:if>
				<div class="table-responsive">
					<div class="table-responsive margin-top-10 overflow-y-hidden">
						<table class="table table-striped table-condensed table-bordered"
							id="landDetailTable">
							<thead>
								<tr>
									<th class="width-6"><spring:message
											code="" text="Sr No" /></th>
									<th class="width-6"><spring:message
											code="" text="Survey/City Survey No" /></th>
									<th><spring:message
											code="" text="Survey Type" /></th>
									<th class="width-10"><spring:message code=""
											text="From Date" /></th>
									<th><spring:message
											code="" text="To Date" /></th>
									<th><spring:message
											code="" text="Land rate" /></th>
									<%-- <c:if test="${command.saveMode ne 'V'}"> --%>
										<th class="width-8"><%-- <spring:message
											code="" text="Action" /> --%>
											<a href="#" data-toggle="tooltip"
											data-placement="top" class="btn btn-blue-2  btn-sm"
											data-original-title="Add" onclick="addLandRow();"><strong
												class="fa fa-plus"></strong><span class="hide"></span></a>		
										</th>
									<%-- </c:if> --%>
								</tr>
							</thead>
							<tbody>	
							
							<c:choose>
								<c:when test="${not empty landRateMasterBean.lndRateList }">
								<c:set var="e" value="0" scope="page"></c:set>
								<c:forEach items="${landRateMasterBean.lndRateList}" var="lndRateList">
									<tr class="">
									<td align="center">
									<form:hidden  path="lndRateList[${e}].isUpdate" id="editStatus${e}" value="" />
									<form:hidden  path="lndRateList[${e}].tbAreaLandId" id="tbAreaLandId${e}" value="" />
									<form:input path=""
											cssClass="form-control mandColorClass non-edited" id="sequence${e}"
											value="${e+1}"/></td>
									<td class="width-10"><form:input
											path="lndRateList[${e}].surveyNo"
											id="surveyNo${e}"
											cssClass="required-control form-control non-edited"/></td>	
								
									<td class="width-10">
												<form:select path="lndRateList[${e}].surveyType" onchange=""
													class="form-control mandColorClass non-edited"  id="survetType${e}"
													data-rule-required="">
													<form:option value="">
														<spring:message code="trade.sel.optn.gender" />
													</form:option>
													<c:forEach items="${surveyType}"
														var="lookUp">
														<form:option value="${lookUp.lookUpDesc}"
															code="${lookUp.lookUpDesc}">${lookUp.lookUpDesc}</form:option>
													</c:forEach>
												</form:select></td>
												
									<td class="width-10"><%-- <form:input
											path="lndRateList[${e}].fromDate"
											id="fromDate"
											cssClass="required-control form-control"/> --%>
											<div class="input-group">
												<form:input class="form-control datepicker non-edited fromDate"
															id="fromDate${e}" path="lndRateList[${e}].fromDate"
															autocomplete="off" maxlength="10"></form:input>
															<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
												</td>
												
									<td class="width-10">
												<%-- <form:input
											path="lndRateList[${e}].toDate"
											id="toDate"
											cssClass="required-control form-control"/> --%> 
											<div class="input-group">
											<form:input
													class="form-control datepicker non-edited toDate" id="toDate${e}"
													path="lndRateList[${e}].toDate" autocomplete="off" maxlength="10"></form:input>
												<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
												
											</td>
												
									<td class="width-10"><form:input
											path="lndRateList[${e}].amt"
											id="amt${e}" 
											cssClass="required-control form-control non-edited"/></td>	
	
										<td class="text-center">
											
											<a href="#" data-toggle="tooltip" id="editRow${e }"
											data-placement="top" class="btn btn-warning btn-sm"
											data-original-title="Edit"
											onclick="editLandRow(${e})">
												<strong class="fa fa-pencil"></strong> <span class="hide"><spring:message
														code="adh.delete" text="Delete" /></span>
										</a></td>
								</tr> 
								<c:set var="e" value="${e + 1}" scope="page" />
								</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="hide" id="forNoneData" >
									<td align="center">
									<form:hidden  path="lndRateList[0].isUpdate" id="editStatus0" value="" />
									<form:input path=""
											cssClass="form-control mandColorClass non-edited" id="sequence0"
											value="1"/></td>
									<td class="width-10"><form:input
											path="lndRateList[0].surveyNo"
											id="surveyNo0"
											cssClass="required-control form-control non-edited"/></td>	
								
									<td class="width-10">
												<form:select path="lndRateList[0].surveyType" onchange=""
													class="form-control mandColorClass non-edited"  id="survetType0"
													data-rule-required="">
													<form:option value="">
														<spring:message code="trade.sel.optn.gender" />
													</form:option>
													<c:forEach items="${surveyType}"
														var="lookUp">
														<form:option value="${lookUp.lookUpDesc}"
															code="${lookUp.lookUpDesc}">${lookUp.lookUpDesc}</form:option>
													</c:forEach>
												</form:select></td>
												
									<td class="width-10"><%-- <form:input
											path="lndRateList[${e}].fromDate"
											id="fromDate"
											cssClass="required-control form-control"/> --%>
											<div class="input-group">
												<form:input class="form-control datepicker non-edited fromDate"
															id="fromDate0" path="lndRateList[0].fromDate"
															autocomplete="off" maxlength="10"></form:input>
															<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
												</td>
												
									<td class="width-10">
												<%-- <form:input
											path="lndRateList[${e}].toDate"
											id="toDate"
											cssClass="required-control form-control"/> --%> 
											<div class="input-group">
											<form:input
													class="form-control datepicker non-edited toDate" id="toDate0"
													path="lndRateList[0].toDate" autocomplete="off" maxlength="10"></form:input>
												<span class="input-group-addon"><i
																				class="fa fa-calendar"></i></span>
											</div>
												
											</td>
												
									<td class="width-10"><form:input
											path="lndRateList[0].amt"
											id="amt0" 
											cssClass="required-control form-control non-edited"/></td>	
	
										<td class="text-center">
											
											<a href="#" data-toggle="tooltip" id="editRow0"
											data-placement="top" class="btn btn-warning btn-sm"
											data-original-title="Edit"
											onclick="editLandRow(${e})">
												<strong class="fa fa-pencil"></strong> <span class="hide"><spring:message
														code="adh.delete" text="Delete" /></span>
										</a></td>
								</tr> 
								</c:otherwise>
								</c:choose>
									
								</tbody>
						</table>
					</div>
				</div>
          
				   <c:if test="${landRateMasterBean.btnShow eq 'Y'}">
				   <div class="text-center margin-top-10">
					<input type="button" onClick="saveRentForm(this);"
						value="Submit" class="btn btn-blue-2" id="Search"/>
					
					<input type="button"
						onclick="window.location.href='AdminHome.html'"
						class="btn btn-danger  hidden-print" value="Back"/>
				</div>
				   </c:if>
             <table id="grid"></table>
				<div id="pagered"></div>
				
			</form:form>
		</div>
	</div>
</div>

<script>
	

	/* 	$(document).ready(function() {
			debugger;
			$('#fromDate0').datepicker({
				dateFormat : 'dd/mm/yy',
				changeMonth : true,
				changeYear : true,
				maxDate : '0',

			});
		}); */
	
		$(function () {
		    $('body').tooltip({
		        selector: '[data-toggle="tooltip"]'
		    }).click(function () {
		        $('[data-toggle="tooltip"]').tooltip("hide");
		    });
		})
</script>


