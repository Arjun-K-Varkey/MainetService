<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/property/dataEntrySuitDeleteBillSearch.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
 

<!-- Start breadcrumb Tags -->
<%-- <apptags:breadcrumb></apptags:breadcrumb> --%>
<!-- End breadcrumb Tags -->

<!-- Start Content here -->

	<!-- Start Main Page Heading -->
	<div class="widget">
	<div class="widget-header">
				<h2><strong><spring:message code="property.outstanding" text=""/></strong></h2>				
				<div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message code="property.Help"/></span></i></a>
				</div>
	</div>
	
	<!-- End Main Page Heading -->
<!-- Start Widget Content -->
	<div class="widget-content padding">
		<!-- Start mand-label -->
		<div class="mand-label clearfix">
			<span><spring:message code="property.Fieldwith"/><i class="text-red-1">* </i><spring:message code="property.ismandatory"/>
			</span>
		</div>
		<!-- End mand-label -->
		
		<!-- Start Form -->
	 <form:form action="DataEntrySuiteDeleteBill.html"
					class="form-horizontal form" name="DataEntryOutStandingView"
					id="DataEntryOutStandingView">	
			<jsp:include page="/jsp/tiles/validationerror.jsp" />
		<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
		<div class="form-group">
 			<label  class="col-sm-2  required-control ">
			<spring:message code="ownerdetail.Ownername"/>
			</label>	
			<div class="col-sm-4">
			<form:textarea id="assoOwnerName" 
			path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control mandColorClass hasCharacter"
			data-rule-required="true" placeholder="Please Enter Owners Name" maxlength="500" readonly="true"/>

			</div>
			</div>	
			
		<div class="form-group">
		
		 <label  class="col-sm-2  required-control ">
			<spring:message code="property.financialyear"/>
			</label>	
			
			<div class="col-sm-4">
				<select id="finId" class="form-control" name="" disabled="true">
    				<option value=""><spring:message code='master.selectDropDwn'/></option>
    				<c:forEach items="${command.finYearData}" var="finYearData" varStatus="status">
        				<option value="${finYearData.key}" ${status.first ? 'selected="selected"' : ''}>${finYearData.value}</option>
    				</c:forEach>
				</select>
			</div>
			
			<label class="col-sm-2 control-label"><spring:message code="propertydetails.oldpropertyno"/></label>
			<div class="col-sm-4">
		 <div class="input-group"> 
	 		<form:input path="provisionalAssesmentMstDto.assOldpropno" class="form-control" id="billDate" readonly="true"/> 
		</div>
			</div>
			</div>
			
		<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="property.bill.no"/></label>
		<div class="col-sm-4">
	 		<form:input path="billNo" class="form-control" id="billNo" readonly="true"/> 
		</div>
			<apptags:textArea labelCode="property.landDetails.Remark" path="provisionalAssesmentMstDto.remarks" maxlegnth="1000" cssClass="preventSpace" isMandatory="true"  isDisabled="${command.modeType eq 'AUT' ? true : false}"></apptags:textArea>
		</div>	
		<c:if test="${not empty command.getBillMasList()}"> 
	
		<div id="billWiseDetail">
			<div class="widget-header">
				<h2><strong><spring:message code="property.taxentry" text=""/></strong></h2>				
				<%-- <div class="additional-btn">
					<a href="#" data-toggle="tooltip" data-original-title="Help"><i
						class="fa fa-question-circle fa-lg"><span class="hide"><spring:message code="property.Help"/></span></i></a>
				</div> --%>
	       </div>
	
			<div>
			
			<c:set var="d" value="0" scope="page" />
                <table id="taxdetailTable" class="table table-striped table-bordered appendableClass taxDetails">
                    <tbody>
                        <tr>
                        <th width="8%"><spring:message code="dataEntry.finYear"/></th>
                            <th width="30%"><spring:message code="taxdetails.taxdescription"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.arrears"/></th>
                     	 	<%-- <th width="8%"><spring:message code="taxdetails.currentbill"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.total"/></th>
                     	 	<th width="8"><a href="javascript:void(0);" title="Add" class="addCF btn btn-success btn-sm unit" id="addUnitRow" ><i class="fa fa-plus-circle"></i></a></th>
                         --%>
                        </tr>
                    <c:forEach var="billMas" items="${command.getBillMasList()}" varStatus="masStatus" >              
                     <c:forEach var="billDet" items="${billMas.getTbWtBillDet()}" varStatus="detStatus" >     
                      
                  <tr class="firstUnitRow ${masStatus.count%2==0? "trfirst": "trSecond"}" >
                      		 <td>                                       	
                        	    <form:select path="billMasList[${masStatus.count-1}].bmYear" id="year0" class="form-control disabled text-center mandColorClass displayYearList" data-rule-required="true" disabled="true">
		  						<form:option value="0" label="Select Year"></form:option>
		  						<c:forEach items="${command.financialYearMapForTax}" var="yearMap">
		  						<form:option value="${yearMap.key}" label="${yearMap.value}"></form:option>
		  						</c:forEach>
		 						</form:select>
<%-- 		 						<form:hidden path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[0].faYearId" id="hiddenYear0"/>
 --%><%-- 		 					<form:hidden path="leastFinYear" id="minYear"/>	 --%>
		 						    
		 					</td>
                    <td width="150"> 
	           			 <form:select id="taxDesc" path="billMasList[${masStatus.count-1}].tbWtBillDet[${detStatus.count-1}].taxId" class="form-control mandColorClass" data-rule-prefixValidation="true" onchange="getPaymentMadeDetails()" disabled="true">
							<form:option value="0">
								<spring:message code="property.sel.optn" text="Select" />
							</form:option>
							<c:forEach items="${command.taxMasterList}" var="lookup">
							<form:option value="${lookup.lookUpId}">${lookup.otherField}</form:option>
							</c:forEach>
					  </form:select>
			   	</td>
							<td width="150"><form:input path="billMasList[${masStatus.count-1}].tbWtBillDet[${detStatus.count-1}].bdCsmp" type="text"  class="form-control has2Decimal mandColorClass text-right" id="areear0" disabled="true"/></td>
							<%-- <td width="150"><form:input path="tbBillMas.tbWtBillDet[${status.count-1}].bdCurTaxamt" type="text" class="form-control has2Decimal mandColorClass text-right" id="current0" data-rule-required="true"  placeholder="999999.99" onchange="calculate('0')"/></td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[${status.count-1}].total"  type="text"  class="form-control has2Decimal mandColorClass text-right" id="total0"   data-rule-required="true" placeholder="999999.99" disabled="true"  /></td>
						    <td width="150" class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete"  id="removeRow"><i class="fa fa-minus-circle"></i></a></td> --%>
                       	</tr> 
                       	</c:forEach>
                       	</c:forEach>
                    </tbody>
                 </table>

           </div>
			</div>
		</c:if>
			<div>
			
<%-- 				<c:if test="${ command.getAssType() eq 'A'}"> 
				
				<c:choose>
		<c:when test="${not empty command.getTbBillMas().getTbWtBillDet()}"> 
			<c:set var="d" value="0" scope="page" />
                <table id="taxdetailTable" class="table table-striped table-bordered appendableClass taxDetails">
                    <tbody>
                        <tr>
                            <th width="8%"><spring:message code="taxdetails.taxdescription"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.arrears"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.currentbill"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.total"/></th>
                     	 	<th width="8"><a href="javascript:void(0);" title="Add" class="addCF btn btn-success btn-sm unit" id="addUnitRow" ><i class="fa fa-plus-circle"></i></a></th>
                        
                        </tr>
                    <c:forEach var="unitDetails" items="${command.getTbBillMas().getTbWtBillDet()}" varStatus="status" >                          
                  <tr class="firstUnitRow">
                    <td width="150"> 
	           			 <form:select id="taxDesc" path="tbBillMas.tbWtBillDet[${status.count-1}].taxId" class="form-control mandColorClass" data-rule-prefixValidation="true" onchange="getPaymentMadeDetails()">
							<form:option value="0">
								<spring:message code="property.sel.optn" text="Select" />
							</form:option>
							<c:forEach items="${command.taxesMaster}" var="tbTax">
							<form:option value="${tbTax.taxId}">${tbTax.taxDesc}</form:option>
							</c:forEach>
					  </form:select>
			   	</td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[${status.count-1}].bdPrvArramt" type="text" class="form-control has2Decimal mandColorClass text-right" id="areear0"  data-rule-required="true"  placeholder="999999.99" onchange="calculate('0')"/></td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[${status.count-1}].bdCurTaxamt" type="text" class="form-control has2Decimal mandColorClass text-right" id="current0" data-rule-required="true"  placeholder="999999.99" onchange="calculate('0')"/></td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[${status.count-1}].total"  type="text"  class="form-control has2Decimal mandColorClass text-right" id="total0" data-rule-required="true" placeholder="999999.99" disabled="true"  /></td>
						    <td width="150" class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete"  id="removeRow"><i class="fa fa-minus-circle"></i></a></td>
                       	</tr> 
                       	</c:forEach>
                    </tbody>
                 </table>
                 </c:when>
                
                 <c:otherwise>
                  <c:set var="d" value="0" scope="page" />
                <table id="taxdetailTable" class="table table-striped table-bordered appendableClass taxDetails">
                    <tbody>
                        <tr>
                            <th width="8%"><spring:message code="taxdetails.taxdescription"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.arrears"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.currentbill"/></th>
                     	 	<th width="8%"><spring:message code="taxdetails.total"/></th>
                     	 	<th width="8"><a href="javascript:void(0);" title="Add" class="addCF btn btn-success btn-sm unit" id="addUnitRow" ><i class="fa fa-plus-circle"></i></a></th>
                        
                        </tr>
                  <tr class="firstUnitRow">
                    <td width="150"> 
	           			 <form:select id="taxDesc" path="tbBillMas.tbWtBillDet[0].taxId" class="form-control mandColorClass" data-rule-prefixValidation="true" onchange="getPaymentMadeDetails()">
							<form:option value="0">
								<spring:message code="property.sel.optn" text="Select" />
							</form:option>
							<c:forEach items="${command.taxesMaster}" var="tbTax">
							<form:option value="${tbTax.taxId}">${tbTax.taxDesc}</form:option>
							</c:forEach>
					  </form:select>
			   	</td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[0].bdPrvArramt" type="text" class="form-control has2Decimal mandColorClass text-right" id="areear0"  data-rule-required="true"  placeholder="999999.99" onchange="calculate('0')"/></td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[0].bdCurTaxamt" type="text" class="form-control has2Decimal mandColorClass text-right" id="current0" data-rule-required="true"  placeholder="999999.99" onchange="calculate('0')"/></td>
							<td width="150"><form:input path="tbBillMas.tbWtBillDet[0].total"  type="text"  class="form-control has2Decimal mandColorClass text-right" id="total0" data-rule-required="true" placeholder="999999.99" disabled="true"  /></td>
						    <td width="150" class="text-center"><a href="javascript:void(0);" class="remCF btn btn-danger btn-xs delete"  id="removeRow"><i class="fa fa-minus-circle"></i></a></td>
                       	</tr> 
                       	 
                    </tbody>
                 </table>
                 </c:otherwise>
             </c:choose>
				</c:if> --%>
			</div>
			<!-- Start button -->
			<c:if test="${command.getModeType() ne 'AUT'}">
			<div class="overflow margin-top-10">
											<div class="table-responsive">
												<table
													class="table table-hover table-bordered table-striped">
													<tbody>
														<tr>
															<th> <spring:message
																		code="water.serialNo" text="Sr No" />
															</th>
															<th> <spring:message
																		code="water.docName" text="Document Name" />
															</th>
															<th> <spring:message
																		code="water.status" text="Status" />
															</th>
															<th> <spring:message
																		code="water.uploadText" text="Upload" />
															</th>
														</tr>
															<tr>
																<td>1</td>
																<td>Bill Deletion</td>
																	<td> <spring:message
																				code="water.doc.mand" />
																	</td>
																<td><div id="docs_0" class="">
																		<apptags:formField fieldType="7" labelCode=""
																			hasId="true" fieldPath=""
																			isMandatory="false" showFileNameHTMLId="true"
																			fileSize="SKDCL_MAX_FILE_SIZE"
																			maxFileCount="CHECK_LIST_MAX_COUNT"
																			validnFunction="CHECK_LIST_VALIDATION_EXTENSION_PDF_DOC_XLS"
																			currentCount="0" />
																	</div>
																	<div class="col-sm-12">
																		<small class="text-blue-2 "> <spring:message
																		code="property.file.upload.validator1"
																		text="Documents/Images up to 20 MB can be upload. only Pdf,Doc,Docx,Xls,Xlsx is allowed." />
																		</small>
					  												</div>
																	</td>
															</tr>
													</tbody>
												</table>
											</div>
										</div>
										
										</c:if>
									<c:if test="${command.getModeType() eq 'AUT'}">	
										
										<c:if test="${not empty command.documentList}">	
                         <h4 class="margin-top-0 margin-bottom-10 panel-title"> <a data-toggle="collapse" href="#DocumentUpload"><spring:message code="propertyTax.UploadDocumnet"/></a></h4>
                         
							<fieldset class="fieldRound">
								<div class="overflow">
									<div class="table-responsive">
										<table class="table table-hover table-bordered table-striped">
											<tbody>
												<tr>
													<th><label class="tbold"><spring:message
																code="water.serialNo" text="Sr No" /></label></th>
													<th><label class="tbold"><spring:message
																code="water.docName" text="Document Name" /></label></th>
												     <th><label class="tbold"><spring:message
																code="water.download"/></label></th>
												</tr>

												<c:forEach items="${command.documentList}" var="lookUp"
													varStatus="lk">
													<tr>
														<td><label>${lookUp.clmSrNo}</label></td>
														<c:choose>
															<c:when
																test="${userSession.getCurrent().getLanguageId() eq 1}">
																<td><label>${lookUp.clmDescEngl}</label></td>
															</c:when>
															<c:otherwise>
																<td><label>${lookUp.clmDesc}</label></td>
															</c:otherwise>
														</c:choose>
														  <td>
														  <c:set var="links" value="${fn:substringBefore(lookUp.attPath, lookUp.attFname)}" />
															<apptags:filedownload filename="${lookUp.attFname}" filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}" actionUrl="DataEntrySuiteDeleteBill.html?Download"></apptags:filedownload>
					                                    </td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</fieldset>
						
					 </c:if>     
					 
					 </c:if>
			<c:if test="${command.getModeType() eq 'AUT'}">
			<c:if test="${not empty command.remarks}">
			<div class="form-group">
	  			<apptags:textArea labelCode="approval.remarks" path="remarks" isDisabled="true" cssClass="mandColorClass comment" />
			</div>
		</c:if>
			<apptags:CheckerAction hideSendback="true" hideForward="true"></apptags:CheckerAction>
		     </c:if>
			<div class="text-center padding-10">
			<c:if test="${command.getModeType() eq 'AUT'}">
			<button type="button" class="btn btn-success btn-submit"
					 onclick="approveBillDeletionApplication(this)" id="deleteBill"><spring:message code="" text="Approve"/></button>
					 
			<button type="button" class="btn btn-danger"  id="back"
									onclick="window.location.href='AdminHome.html'"><i class="fa fa-chevron-circle-left padding-right-5"
							aria-hidden="true"></i><spring:message code="property.Back" text="Back"/>
						</button> 
			</c:if>
			<c:if test="${command.getModeType() ne 'AUT'}">
			<button type="button" class="btn btn-success btn-submit"
					 onclick="deletePropertyBill(this)" id="deleteBill"><spring:message code="property.dataEntry.delete.Bill"/></button>
					 
			<button class="btn btn-blue-2" type="button"  onclick="backToDeleteSearchPage(this)" id="back">
										<spring:message code="property.Back"/></button> 
			</c:if>
				  
				   
			</div>
			
			<!--  End button -->
			
			
		</form:form>
		<!-- End Form -->
		</div>
		<!-- End Widget Content here -->
		</div>
		<!-- End Widget  here -->							

<!-- End of Content -->
 