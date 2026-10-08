<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
 <script type="text/javascript" src="js/mainet/file-upload.js"></script>
 <script type="text/javascript" src="js/cfc/challan/adjustmentEntry.js"></script>


 <div id="adjustment">
    <apptags:breadcrumb></apptags:breadcrumb>
    <!-- ============================================================== --> 
    <!-- Start Content here --> 
    <!-- ============================================================== -->
    <div class="content"> 
      <!-- Start info box -->
      <div class="widget ">
        <div class="widget-header">
          <h2><spring:message text="Adjustment Entry" code="master.lbl.adjEntry"/></h2>
          <apptags:helpDoc url="AdjustmentEntry.html"></apptags:helpDoc>
        </div>
        <div class="widget-content padding">
          <div class="mand-label clearfix"><span><spring:message code="contract.breadcrumb.fieldwith" />
				<i class="text-red-1">*</i> <spring:message
					code="contract.breadcrumb.ismandatory" /> </span></div>
          <form:form action="AdjustmentEntry.html" method="POST" class="form-horizontal" id="adjustmentEntryForm">
           	<jsp:include page="/jsp/tiles/validationerror.jsp" />
           	<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
           	<form:hidden path="billingMethod" id="billingMethod"/>
            <div class="form-group">
              <label class="col-sm-2 control-label required-control" for="department" ><spring:message text="Department" code="master.lbl.dept"/></label>
              <div class="col-sm-4">
               <form:select path="adjustmentDto.dpDeptId" onchange="getDeptWiseDocumentReady()" id="department" cssClass="form-control chosen-select-no-results">
					<form:option value="0" ><spring:message text="Select department" code="master.lbl.selDept"/> </form:option>
					<c:forEach items="${command.department}" var="dept">
					 <form:option value="${dept.dpDeptid}">${dept.dpDeptdesc}</form:option>
					 </c:forEach>
				</form:select>    
              </div>
                <label class="col-sm-2 control-label required-control" for="ccnOrPropNo"><spring:message text="Connection No./ Property No." code="master.lbl.conNo"/></label>
              <div class="col-sm-4">
                <form:input path="adjustmentDto.adjRefNo"  class="form-control" id="ccnOrPropNo"/>
              </div>
            </div>
            
             <div class="form-group">
                <label class="col-sm-2 control-label required-control" for="oldccnOrPropNo"><spring:message
                code="property.oldccnOrPropNo" text="Old Connection No./ Old Property No."/></label>
              <div class="col-sm-4">
                <form:input path=""  class="form-control" id="oldccnOrPropNo"/>
              </div>
            </div>
            
            <div class="form-group" id="showFlatNo">
				<%-- <apptags:input labelCode="Flat No" path="propBillPaymentDto.flatNo" ></apptags:input> --%>
				
				<label class="col-sm-2 control-label"><spring:message
							code="property.FlatNo" text="Flat No" /></label>
					<div class="col-sm-4">
						<form:select path="adjustmentDto.flatNo" id="flatNo"
							class="form-control mandColorClass chosen-select-no-results">
							<form:option value="">
								<spring:message code='property.select' text="select"/>
							</form:option>
							<c:forEach items="${command.flatNoList}" var="flatNoList">
								<form:option value="${flatNoList}">${flatNoList}</form:option>
							</c:forEach>
						</form:select>
					</div>
					
			</div>
            
             <div class="text-center margin-bottom-10 " id="btnDiv">
          <button type="button" class="btn btn-info" id="serchBtnDirect" onclick="return searchAjustmentData(this);"><i class="fa fa-search"></i><spring:message text=" Search" code="master.search"/></button>
          
          <button type="button" class="btn btn-success" id="serchBtn"
										onclick="redirectToSearchPropNo(this)">
										<i class="fa fa-search"></i><spring:message code="property.changeInAss.Search"/>
			</button> 
              
              <button type="button" class="btn btn-warning" onclick="resetAdjustmentForm(this)"><i class="fa fa-undo"></i><spring:message text=" Reset" code="reset.msg"/></button>
              <button type="button" class="btn btn-danger" onclick="window.location.href ='AdminHome.html'"><i class="fa fa-chevron-circle-left padding-right-5"></i><spring:message code="water.btn.back" /></button>
              </div>
            
            <c:if test="${command.bill ne null && command.bill.tbWtBillDet ne null}">
            <div class="form-group">
             <label class="col-sm-2 control-label " for="owner"><spring:message text="Owner/Occupier Name" code="property.OwnerName/OccupierName"/></label>
             <div class="col-sm-4">
                 <form:input path="bill.bmCcnOwner"  value="" class="form-control" readonly="true" id="owner"/>
              </div>
             <label class="control-label col-sm-2" for="address"><spring:message text="Address" code="prop.rec.print.addr"/></label>
              <div class="col-sm-4">
                <form:textarea path="bill.address"  class="form-control" readonly="true" id="address"></form:textarea>
              </div>
            </div>
             <div class="form-group">
              <label class="col-sm-2 control-label " for="BmNO"><spring:message text="Bill No." code="prop.bill.print.No"/></label>
              <div class="col-sm-4">
               <form:input path="bill.bmNo" class="form-control" id="BmNO" readonly="true" />
              </div>
              <label class="col-sm-2 control-label" for="BmDate"><spring:message text="Bill Date" code="property.bill.date"/></label>
              <div class="col-sm-4">
                <div class="input-group">
                 <fmt:formatDate pattern="dd/MM/yyyy" value="${command.bill.bmBilldt}" var="billDate" />
                 <form:input path="" class="form-control" id="BmDate" readonly="true" value="${billDate}"/>
                  <label for="datepicker2" class="input-group-addon"><i class="fa fa-calendar"></i></label>
                </div>
              </div>
            </div>
            
            <div class="form-group">
             <label class="col-sm-2 control-label required-control"><spring:message code="property.FlatNo" text="Flat No"/></label>
            <div class="col-sm-4">
                 <form:input path="flatNo" class="form-control" id="flatNo" readonly="true"/>
              </div>
              </div>
              
            <div class="form-group">
              <label class="col-sm-2 control-label required-control" for="adType"><spring:message text="Adjusted Type" code="prop.adType"/></label>
              <div class="col-sm-4">
                <form:select path="adjustmentDto.adjType" class="form-control " id="adType" onchange="populateAdjAmt(this);">
                  <form:option value="">Select</form:option>
                   	<form:option value="P"><spring:message code="adjustment.positive" text="Positive"/></form:option>
                    <form:option value="N"><spring:message code="adjustment.negative" text="Negative"/></form:option>
                   	<form:option value="W"><spring:message code="adjustment.writeOff" text="Write Off"/></form:option>
                    <form:option value="B"><spring:message code="adjustment.boza" text="BOZA Entry"/></form:option>
                    <form:option value="E"><spring:message code="adjustment.palava.exemption" text="Palava Exemption"/></form:option>
                </form:select>
              </div>
              
              <a data-toggle="tooltip" onclick="fetchAdjustmentHistory(this)"   data-placement="top" title="" class="btn btn-blue-1 btn-sm" data-original-title="History"><i class="fa fa-history"></i></a>
            </div>
            
            <h4><spring:message text="Distribution Details" code="prop.disde"/></h4>
            <div class="table-responsive">
              <table id="adjDetailTable" class="table table-bordered table-condensed">
              <thead>
                <tr>
                  <th><spring:message text="Sr No." code="roomdetails.roomNo"/></th>
                  <th><spring:message text="Tax Name" code="propertyTax.TaxName"/></th>
                  <th><spring:message text="Tax Amount" code="prop.taamt"/></th>
                  <th><spring:message text="Adjustment Amount" code="property.duesCheck.adjustmentAmount"/></th>
                  <th><spring:message text="Remark" code="property.landDetails.Remark"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${command.bill.tbWtBillDet}" var="details" varStatus="detIndex">
                 <c:if test="${details.taxDesc eq 'Boja'}">
              <form:hidden path="" value="Y" id="BozaId"/>
                  </c:if>
                <tr>
                  <td>${detIndex.count}</td>
                  <td>
                  <form:input path="" id="taxDesc${detIndex.index}" value="${details.taxDesc}" class="form-control" disabled="true"/>
                  <form:hidden path="adjustmentDto.adjDetailDto[${detIndex.index}].taxId" value=" ${details.taxId}"/>
                  </td>
                  
                  <td class="text-right"><form:input path="" id="taxAmountVal${detIndex.index}" value="${details.bdCurBalTaxamt+details.bdPrvBalArramt}" class="form-control" disabled="true"/></td>
                  <form:hidden path="" id="taxAMT${detIndex.index}" value=" ${details.bdCurBalTaxamt+details.bdPrvBalArramt}"/>
                  <td class="text-right">
                   <form:input path="adjustmentDto.adjDetailDto[${detIndex.index}].adjAmount"  class="form-control hasDecimal" id="adjAmount${detIndex.index}" maxlength="10" onchange="addTotalAdjAmount(this)"/>
                  </td>
                  <td>
                   <form:input path="adjustmentDto.adjDetailDto[${detIndex.index}].adjRemark"  class="form-control" id="adjRemark${detIndex.index}" maxlength="200"/>
                  </td>
                </tr>
                </c:forEach>
                </tbody>
                
                <tfoot>
			      <tr>
			        <th colspan="2" class="text-right"><spring:message code="taxdetails.total" text="Total"/></th>
			        <th class="text-right">${command.totalBalanceAmount}</th>
			        <fmt:formatNumber type="number" value="${command.totalAmountToAdjust}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="rebateAmount"/>
			        <th id="rebateId" class="text-right">${rebateAmount}</th>
			        <th></th>
			      </tr>
    			</tfoot>
    			
              </table>
							
			</div>
            
            
            
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
																<td>Adjustment Entry</td>
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
										
										
										
										
            <div class="text-center margin-top-10">
              <button type="button" class="btn btn-success btn-submit" 
              onclick="return saveData(this);">
              <spring:message code="property.dataEntry.save" text="Submit" />
              </button>
              
              <button type="button" class="button-input btn btn-danger"
						name="button-Cancel" value="Cancel" style=""
						onclick="window.location.href='AdminHome.html'" id="button-Cancel">
						<spring:message code="property.Cancel" text="Cancel" />
					</button>
            </div>
            </c:if>
          </form:form>
        </div>
      </div>
      </div>
      </div>