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
 <script type="text/javascript" src="js/property/taxExemptionManuallyDeduction.js"></script>




 <div id="adjustment">
    <apptags:breadcrumb></apptags:breadcrumb>
    <!-- ============================================================== --> 
    <!-- Start Content here --> 
    <!-- ============================================================== -->
    <div class="content"> 
      <!-- Start info box -->
      <div class="widget ">
        <div class="widget-header">
          <apptags:helpDoc url="TaxExemptionManuallyDeduction.html"></apptags:helpDoc>
        </div>
        <div class="widget-content padding">
          <div class="mand-label clearfix"><span><spring:message code="contract.breadcrumb.fieldwith" />
				<i class="text-red-1">*</i> <spring:message
					code="contract.breadcrumb.ismandatory" /> </span></div>
          <form:form action="TaxExemptionManuallyDeduction.html" method="POST" class="form-horizontal" id="adjustmentEntryForm">
           	<jsp:include page="/jsp/tiles/validationerror.jsp" />
           	<form:hidden path="billingMethod" id="billingMethod"/>
            
            
            <div class="form-group ">

									<apptags:input labelCode="propertydetails.PropertyNo." path="provisionalAssesmentMstDto.assNo" ></apptags:input>
									<apptags:input labelCode="Flat No" path="provisionalAssesmentMstDto.flatNo" ></apptags:input>
									

							</div>
            
            <div class="form-group ">

									<apptags:input labelCode="Owner Name" path="provisionalAssesmentMstDto.ownerFullName" ></apptags:input>
									

							</div>
            
            <h4><spring:message text="Tax Exemption Details" code="prop.disde"/></h4>
            <div class="table-responsive">
              <table id="adjDetailTable" class="table table-bordered table-condensed">
                <tr>
                  <th><spring:message text="Sr No." code="roomdetails.roomNo"/></th>
                  <th><spring:message text="Tax Name" code="propertyTax.TaxName"/></th>
                  <th><spring:message text="Tax Amount" code="prop.taamt"/></th>
                </tr>
                <c:forEach items="${command.manualExempDtoList}" var="details" varStatus="detIndex">
                <tr>
                <td>
                   <form:input path="manualExempDtoList[${detIndex.index}].count"  class="form-control" id="count${detIndex.index}" maxlength="10"/>
                  </td>
                 <td>
                   <form:input path="manualExempDtoList[${detIndex.index}].taxDescription"  class="form-control" id="taxDescription${detIndex.index}" maxlength="10"/>
                  </td>
                  
                  
                  <td>
                   <form:input path="manualExempDtoList[${detIndex.index}].taxAmount"  class="form-control" id="taxAmount${detIndex.index}" maxlength="10"/>
                  </td>
                </tr>
                </c:forEach>
              </table>
            </div>
            <div class="text-center padding-bottom-10">

					<button type="button" class="btn btn-success" id="serchBtnDirect1"
						onclick="savePropertyEditForm(this)">
						<spring:message code="" text="Submit" />
					</button>




				</div>
										
										
          </form:form>
        </div>
      </div>
      </div>
      </div>