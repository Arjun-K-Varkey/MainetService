<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script src="js/mainet/ui/i18n/grid.locale-en.js"></script>
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script src="js/mainet/validation.js"></script>
<script src="js/mainet/script-library.js"></script>
<script src="js/account/accountInvesmantMaster.js"
	type="text/javascript"></script>
	<script>$("select").chosen();</script>
<apptags:breadcrumb></apptags:breadcrumb>
<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2><spring:message code="account.invMaster.invEntryForm" text="Investment Entry Form" /></h2>
		</div>
		<div class="widget-content padding">
			<form:form action="investmentMaster.html" name="investmentMaster"
				id="investmentMasterId" class="form-horizontal" onsubmit="return validateForm()">
		
				<!-- Start Validation include tag -->
				<jsp:include page="/jsp/tiles/validationerror.jsp" />
				<!-- ----------------- this div below is used to display the error message on the top of the page--------------------------->
				
				
				<div
					class="error-div alert alert-danger alert-dismissible"
					id="errorDivId" style="display: none;">
					<button type="button" class="close" onclick="closeOutErrBox()"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
					</button>
						<span id="errorId"></span>
				</div>

				<!-- ---------------------------------------------------------------------------------------------------------------------- -->
				<form:hidden path="accountInvestmentMasterDto.sliDate" id = "sliDate"/>

				<div class="mand-label clearfix">
					<span>Field with <i class="text-red-1">*</i> is mandatory
					</span>
				</div>
				<h4><spring:message code="account.invMaster.invInformation" text="Investment Information" /></h4>
				
				<c:if test= "${command.saveMode eq 'V' || command.saveMode eq 'E'}">
				<div class="form-group">
					<label for="text-1"
						class="control-label col-sm-2">Investment No.</label> 
						<div class="col-sm-4">
					<form:input 
								id = "invstNo"
								path="accountInvestmentMasterDto.invstNo"
								cssClass= "form-control"	
								readonly="true"		
							/>
							</div>
							
				</div>
				</c:if>
				
				<div class="form-group">
					<label for="text-1"
						class="control-label col-sm-2 required-control ">
						<spring:message code="accounts.bankfortds.ptbbankname" text="Bank Name" />
						</label>
					<div class="col-sm-4">
						<form:select
							class="form-control mandColorClass chosen-select-no-results"
							path="accountInvestmentMasterDto.bankId" id="bankId"
							placeholder="Enter Bank"
							disabled="${command.saveMode eq 'V' ? true : false }">
							<form:option value="" selected="true">
								<spring:message code="account.common.select" text="Select" />
							</form:option>
							<c:forEach items="${command.bankMap}" var="bankData">
								<form:option code="${bankData.key}" value="${bankData.key}">${bankData.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<spring:message code="account.invMaster.enterFdrNo" text="Please Enter FDRNO" var="acInvEntFdrNo" />
					<apptags:input labelCode="account.invMaster.invFdrNo"
						path="accountInvestmentMasterDto.inFdrNo" isMandatory="true"
					
						placeholder="${ acInvEntFdrNo }"
						isDisabled="${command.saveMode eq 'V' ? true : false }"></apptags:input>
				</div>
				<div class="form-group">
					<label for="text-1" class="control-label col-sm-2 required-control">
						<spring:message code="account.head.map.investment.type" text="Investment Type" />
						</label>
					<c:set var="baseLookupCode" value="IVT" />
		 			<%-- <apptags:lookupField
						items="${command.getLevelData(baseLookupCode)}"
						path="accountInvestmentMasterDto.invstType"
						cssClass="form-control required-control" isMandatory="true"
						selectOptionLabelCode="selectdropdown" hasId="true" disabled="${command.saveMode eq 'V' ? true : false }"
						/>  --%> 
						
					<div class="col-sm-4">
				<c:set var="baseLookupCode" value="IVT" />
				<form:select path="accountInvestmentMasterDto.invstType" class="form-control mandColorClass chosen-select-no-results"
					id="invstType"
					disabled="${command.saveMode eq 'V' ? true : false}"
					>
					<form:option value="0">
						<spring:message code="account.select.investmentType" text="Select Investment Type" />
					</form:option>
					<c:forEach items="${command.getLevelData(baseLookupCode)}"
						var="lookUp">
						<form:option value="${lookUp.lookUpId}"
							code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
					</c:forEach>
				</form:select>
			</div>
						
					<label for="text-1" class="control-label col-sm-2 required-control">
						<spring:message code="account.investment.invDate" text="Investment Date" />
						</label>
					<div class="col-sm-4">
						<div class="input-group">
						<spring:message code="account.investmentMass.enterInvDate" text="Enter Investment Date" var="accInvEnterInvDate" />
							<form:input class="form-control datepicker" id="frmdate"
								path="accountInvestmentMasterDto.invstDate"
								placeholder="${ accInvEnterInvDate }" maxlength="10"
								disabled="${command.saveMode eq 'V' ? true : false }" 
							    onkeydown="this.value=this.value.replace(/^(\d\d)(\d)$/g,'$1/$2').replace(/^(\d\d\/\d\d)(\d+)$/g,'$1/$2').replace(/[^\d\/]/g,'')"/>
							<label class="input-group-addon" for="trasaction-date-icon30"><i
								class="fa fa-calendar"></i></label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<%-- 	<apptags:input labelCode="Investment Amount"
								cssClass = "hasDecimal"
								path="accountInvestmentMasterDto.invstAmount" isMandatory="true"
								placeholder="Enter Investment Amount"
								isDisabled="${command.saveMode eq 'V' ? true : false }"></apptags:input> --%>
						<label class ="col-sm-2 control-label required-control" for ="text-1">
						<spring:message code="account.investMaster.inveAmt" text="Investment Amount" />
						</label>
						<div class= "col-sm-4">
							<form:input 
								cssClass = "form-control text-right"
								onkeypress ="return hasAmount(event, this, 10,2)"
								id = "invstAmount"
								onkeyup = "dynamicSum()"
								path="accountInvestmentMasterDto.invstAmount"
								disabled="${command.saveMode eq 'V' ? true : false }"
							/> 
						</div>
				<%-- 	--%>
					
						<label for="text-1" class="control-label col-sm-2 required-control">
						<spring:message code="account.budgetopenmaster.fundcode" text="Fund" />
						</label>
					<div class="col-sm-4">
						<form:select
							class="form-control mandColorClass chosen-select-no-results"
							path="accountInvestmentMasterDto.fundId" id="fundId"
							placeholder="Enter fund"
							disabled="${command.saveMode eq 'V' ? true : false }">
							<form:option value="" selected="true">
								<spring:message code="account.common.select" text="Select" />
							</form:option>
							<c:forEach items="${command.fundList}" var="fundData">
								<form:option code="${fundData.fundCode}"
									value="${fundData.fundId}">${fundData.fundCompositecode} ${fundData.fundDesc}</form:option>
							</c:forEach>
						</form:select>
					</div>
					
					
				</div>
				<div class="form-group">
					<%-- <apptags:input labelCode="Interest Amount"
					cssClass = "hasDecimal"
						path="accountInvestmentMasterDto.instAmt" isMandatory="true"
						placeholder="Enter Interest Amount"
						isDisabled="${command.saveMode eq 'V' ? true : false }"
							onChange = "dynamicSum()"
						></apptags:input> --%>
						
						<label class ="col-sm-2 control-label required-control">
						<spring:message code="account.loanMaster.interAmt" text="Interest Amount" />
						</label>
						<div class ="col-sm-4">
							<form:input 
								 
								id = "instAmt"
								path="accountInvestmentMasterDto.instAmt"
								cssClass= "form-control text-right"	
								onkeypress="return hasAmount(event,this,10,2)"	
								onkeyup = "dynamicSum()"
								disabled="${command.saveMode eq 'V' ? true : false }"				
							/>
						</div>
						
						
				<%-- 	<apptags:input labelCode="Interest Rate"
						path="accountInvestmentMasterDto.instRate" isMandatory="true"
						placeholder="Enter Interest Rate"
						cssClass = "hasDecimal"
						isDisabled="${command.saveMode eq 'V' ? true : false }" 
					
						></apptags:input> --%>
						
						<label class ="col-sm-2 control-label required-control">
						<spring:message code="account.loanMaster.intrestRate" text="Interest Rate" />
						</label>
						<div class ="col-sm-4">
						<spring:message code="accounnt.enter.interestRate" text="Please Enter Interest Rate" var="accInvEntIntRte" />
							<form:input 
								placeholder="${ accInvEntIntRte }"
								id = "instRate"
								path="accountInvestmentMasterDto.instRate"
								cssClass= "form-control"	
								onkeypress="return hasAmount(event,this,14,2)"	
								disabled="${command.saveMode eq 'V' ? true : false }"				
							/>
						</div>
						
				</div>
				<div class="form-group">
					
					<apptags:input labelCode="account.invMaster.maturiAmt"
						cssClass = "text-right"
						path="accountInvestmentMasterDto.maturityAmt" isMandatory="true"
						placeholder="0.00"
						isDisabled="true">
					</apptags:input>
					<form:input type="hidden" path="accountInvestmentMasterDto.maturityAmt" id="maturityAmount"/>
					
				<label for="text-1" class="control-label col-sm-2 required-control">
						<spring:message code="account.investMaster.matuDte" text="Maturity Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
						<spring:message code="account.enter.maturityDate" text="Please Enter Maturity Date" var="accInvEntMatuDate" /></label>
							<form:input class="form-control datepicker" id="tocdate"
								path="accountInvestmentMasterDto.invstDueDate"
									onkeydown="this.value=this.value.replace(/^(\d\d)(\d)$/g,'$1/$2').replace(/^(\d\d\/\d\d)(\d+)$/g,'$1/$2').replace(/[^\d\/]/g,'')"
								placeholder="${ accInvEntMatuDate }" maxlength="10"
								disabled="${command.saveMode eq 'V' ? true : false }" />
							<label class="input-group-addon" for="trasaction-date-icon30"><i
								class="fa fa-calendar"></i></label>
						</div>
					</div>
					
				</div>
				<div class="form-group">
					<%-- <apptags:input labelCode="Resolution Number"
					
						path="accountInvestmentMasterDto.resNo" isMandatory="true"
						placeholder="Enter Resolution Number"
						isDisabled="${command.saveMode eq 'V' ? true : false }"></apptags:input> --%>
						
						<label class = "control-label col-sm-2  required-control" for = "text-1"> 
						<spring:message code="account.loanMaster.resoluNum" text="Resolution Number" />
						</label>
						<div class=  "col-sm-4">
						<spring:message code="account.enter.resolution.no" text="Please Enter Resolution Number" var="acInvEntResoNum" />
							<form:input 
							id = "resNo" placeholder="${ acInvEntResoNum }"
							path="accountInvestmentMasterDto.resNo"
							cssClass = "form-control hasNumber"
							maxlength ="12"
							disabled="${command.saveMode eq 'V' ? true : false }"
							/>
						</div>
					
					<label for="text-1" class="control-label col-sm-2 required-control">
						<spring:message code="bill.resolution.date" text="Resolution Date" /></label>
					<div class="col-sm-4">
						<div class="input-group">
						<spring:message code="account.invMaster.plEntResoDate" text="Please Enter Resolution Date" var="accInvResoDate" />
							<form:input class="form-control datepicker" id="rsnDate"
								path="accountInvestmentMasterDto.resDate"
								placeholder="${ accInvResoDate }" maxlength="10"
								disabled="${command.saveMode eq 'V' ? true : false }"  
								onkeydown="this.value=this.value.replace(/^(\d\d)(\d)$/g,'$1/$2').replace(/^(\d\d\/\d\d)(\d+)$/g,'$1/$2').replace(/[^\d\/]/g,'')"/>
							<label class="input-group-addon" for="trasaction-date-icon30"><i
								class="fa fa-calendar"></i></label>

						</div>
					</div>
				</div>
				<div class="form-group">
				<label for="text-1"
						class="ccontrol-label col-sm-2 required-control">
						<spring:message code="accounts.vendormaster.remark" text="Remark(s)" />
						</label>
					<div class="col-sm-4">
					<spring:message code="accounts.invMaster.mentionRemark" text="Mention Remark(s), If Any" var="accInvMentionReAny" />
						<form:textarea rows="1" cols="50" path="accountInvestmentMasterDto.remarks"
							id ="remarks"
							placeholder="${ accInvMentionReAny }"
							class="form-control padding-left-10"
							disabled="${command.saveMode eq 'V' ? true : false }"></form:textarea>
					</div> 
				</div>
				<div class="text-center margin-top-10">
					<c:if test="${command.saveMode eq 'C' || command.saveMode eq 'E'}">
						<button type="button" class="btn btn-success" title="Save"
							onclick="saveInvestMentForm(this)">
							<i class="fa fa-floppy-o padding-right-5" aria-hidden="true"></i>
							<spring:message code="account.bankmaster.save" text="Save" />
						</button>
						<button type="button" class="btn btn-warning" title="Reset"
							onclick="ResetForm(this)">
							<i class="fa fa-undo padding-right-5" aria-hidden="true"></i>
							<spring:message code="account.bankmaster.reset" text="Reset" />
						</button>
					</c:if>
					<button type="button" class="btn btn-danger" title="Back"
						onclick="backMasterForm()">
						<i class="fa fa-chevron-circle-left padding-right-5"
							aria-hidden="true"></i>
							<spring:message code="account.bankmaster.back" text="Back" />
					</button>
				</div>
			</form:form>
		</div>
	</div>
</div>