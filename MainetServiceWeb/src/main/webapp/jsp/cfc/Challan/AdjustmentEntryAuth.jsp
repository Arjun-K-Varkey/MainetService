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
    <!-- Start Content here --> 
    <div class="content"> 
      <!-- Start info box -->
      <div class="widget ">
        <div class="widget-header">
          <h2><spring:message text="Adjustment Entry" code="master.lbl.adjEntry"/></h2>
          <apptags:helpDoc url="AdjustmentEntryAuthorization.html"></apptags:helpDoc>
        </div>
        <div class="widget-content padding">
          <div class="mand-label clearfix"><span><spring:message code="contract.breadcrumb.fieldwith" />
				<i class="text-red-1">*</i> <spring:message
					code="contract.breadcrumb.ismandatory" /> </span></div>
          <form:form action="AdjustmentEntryAuthorization.html" method="POST" class="form-horizontal" id="adjustmentEntryForm">
           	<jsp:include page="/jsp/tiles/validationerror.jsp" />
           	<div class="warning-div error-div alert alert-danger alert-dismissible" id="errorDiv"></div>
           	<form:hidden path="billingMethod" id="billingMethod"/>
            
            <c:if test="${command.bill ne null && command.bill.tbWtBillDet ne null}">
            <div class="form-group">
             <label class="col-sm-2 control-label " for="propNo"><spring:message text="Property No." code="property.PropertyNo"/></label>
             <div class="col-sm-4">
                 <form:input path="bill.propNo"  value="" class="form-control" readonly="true" id="propNo"/>
              </div>
             <label class="control-label col-sm-2" for="flatNo"><spring:message code="property.FlatNo" text="Flat No"/></label>
              <div class="col-sm-4">
                <form:input path="bill.flatNo"  class="form-control" readonly="true" id="flatNo"></form:input>
              </div>
            </div>
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
              <label class="col-sm-2 control-label" for="adType"><spring:message text="Adjusted Type" code="prop.adType"/></label>
              <div class="col-sm-4">
                <form:select path="adjustmentDto.adjType" class="form-control " id="adType" disabled="true">
                  <form:option value="">Select</form:option>
                   	<form:option value="P"><spring:message code="adjustment.positive" text="Positive"/></form:option>
                    <form:option value="N"><spring:message code="adjustment.negative" text="Negative"/></form:option>
                   	<form:option value="W"><spring:message code="adjustment.writeOff" text="Write Off"/></form:option>
                    <form:option value="B"><spring:message code="adjustment.boza" text="BOZA Entry"/></form:option>
                    <form:option value="E"><spring:message code="adjustment.palava.exemption" text="Palava Exemption"/></form:option>
                </form:select>
              </div>
              
              <!-- <a data-toggle="tooltip" onclick="fetchAdjustmentHistory(this)"   data-placement="top" title="" class="btn btn-blue-1 btn-sm" data-original-title="History"><i class="fa fa-history"></i></a> -->
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
                <%-- <c:set var="count" value="0"/> --%>
                <c:forEach items="${command.bill.tbWtBillDet}" var="details" varStatus="detIndex">
                 <c:if test="${details.taxDesc eq 'Boja'}">
              		<form:hidden path="" value="Y" id="BozaId"/>
                 </c:if>
                <tr>
                  <td>${detIndex.count}</td>
                  <td>
                  <form:input path="" id="taxDesc${detIndex.index}" value="${details.taxDesc}" class="form-control" disabled="true"/>
                  <%-- <form:hidden path="adjustmentDto.adjDetailDto[${detIndex.index}].taxId" value=" ${details.taxId}"/> --%>
                  </td>
                  
                  <td class="text-right"><form:input path="" id="taxAmountVal${detIndex.index}" value="${details.bdCurBalTaxamt+details.bdPrvBalArramt}" class="form-control" disabled="true"/></td>
                  <form:hidden path="" id="taxAMT${detIndex.index}" value=" ${details.bdCurBalTaxamt+details.bdPrvBalArramt}"/>
                  
                  
                  
                  <c:forEach items="${command.adjustmentDto.adjDetailDto}" var="adjDetList" varStatus="adjIndex">
                  <c:choose>
                  <c:when test="${details.taxId eq adjDetList.taxId}">
                  <c:if test="${tax ne adjDetList.taxId }">
                     <td class="text-right">
	                   	<form:input path="adjustmentDto.adjDetailDto[${adjIndex.index}].adjAmount"  class="form-control hasDecimal" id="adjAmount${adjIndex.index}" maxlength="10" disabled="true"/>
	                  	<form:hidden path="adjustmentDto.adjDetailDto[${adjIndex.index}].taxId" id="adjAMT${adjIndex.index}" value="${details.taxId}"/>
	                 </td>
	                 <td>
	                   <form:input path="adjustmentDto.adjDetailDto[${adjIndex.index}].adjRemark"  class="form-control" id="adjRemark${adjIndex.index}" maxlength="200" disabled="true"/>
	                 </td>
	               </c:if>  
                  <c:set var="tax" value="${adjDetList.taxId }"/>
	               </c:when>
                  </c:choose>
	                
                  </c:forEach>
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
			
			<c:if test="${not empty command.documentList}">	
                 <%-- <h4 class="margin-top-0 margin-bottom-10 panel-title"> <a data-toggle="collapse" href="#DocumentUpload"><spring:message code="propertyTax.UploadDocumnet"/></a></h4> --%>
                <h4>
					<spring:message code="checklistVerification.tableCaption" />
				</h4> 
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
												<apptags:filedownload filename="${lookUp.attFname}" filePath="${lookUp.attPath}" dmsDocId="${lookUp.dmsDocId}" actionUrl="AdjustmentEntry.html?Download"></apptags:filedownload>
			                                 </td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</fieldset>
				
			 </c:if> 
			 
			<c:if test="${not empty command.remarks}">
				<div class="form-group">
		  			<apptags:textArea labelCode="approval.remarks" path="remarks" isDisabled="true" cssClass="mandColorClass comment" />
				</div>
			</c:if>	
					
            <apptags:CheckerAction hideForward="true" hideSendback="true"></apptags:CheckerAction>
            							
            <div class="text-center margin-top-10">
              <button type="button" class="btn btn-success btn-submit" 
              onclick="return saveAuthorization(this);">
              <spring:message code="property.dataEntry.save" text="Submit" />
              </button>
              
              <button type="button" class="button-input btn btn-danger"
						name="button-Cancel" value="Cancel" style=""
						onclick="window.location.href='AdminHome.html'" id="button-Cancel">
						<spring:message code="property.Back" text="Back" />
					</button>
            </div>
            </c:if>
          </form:form>
        </div>
      </div>
      </div>
      </div>