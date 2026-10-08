<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<style>

@media print {
	@page {
		size: A4 landscape;
		margin: 0.5cm;
	}
	#btnPrint {
		display: none;
	}
	#removePrint {
		display: none;
	}
	
}
  
</style>
<script>
/* function printContent(el){
	var restorepage = document.body.innerHTML;
	var printcontent = document.getElementById(el).innerHTML;
	document.body.innerHTML = printcontent;
	window.print();
	document.body.innerHTML = restorepage;
} */
function printContent(el, event) {
    if (event) event.preventDefault();

    // Clone the element so we can modify it without affecting the live DOM
    const originalElement = document.getElementById(el);
    const clonedElement = originalElement.cloneNode(true);

    // Remove the print button from the cloned content
    const printButton = clonedElement.querySelector('.hidden-print');
    if (printButton) {
        printButton.remove();
    }

    // Open a new tab/window
    const printWindow = window.open('', '_blank');
    printWindow.document.write('<html><head><title>Print</title>');

    // Copy all <link rel="stylesheet"> from the original page
    document.querySelectorAll('link[rel="stylesheet"]').forEach(link => {
        printWindow.document.write(link.outerHTML);
    });

    // Copy all <style> tags
    document.querySelectorAll('style').forEach(style => {
        printWindow.document.write(style.outerHTML);
    });

    printWindow.document.write('</head><body>');
    printWindow.document.write(clonedElement.innerHTML);
    printWindow.document.write('</body></html>');

    printWindow.document.close();
    printWindow.focus();

    // Trigger print after window loads
    printWindow.onload = function () {
        printWindow.print();
    };
}



/* $("html, body").animate({ scrollTop: 0 }, "slow"); */
</script>

      <div class="widget " id="receipt">
        <div class="widget-header">
         <h2>
             <b><spring:message code="adjust.prop.his" text="Adjustment History"></spring:message></b>
         </h2>
          <div class="additional-btn"> <a href="#" data-toggle="tooltip" data-original-title="Help"><i class="fa fa-question-circle fa-lg"></i></a> </div>
        </div>
        <div class="widget-content padding">
          <form:form action="AdjustmentEntry.html" method="POST" class="form-horizontal" id="adjustmentHistory">
          <c:if test="${command.historyMsg eq null}">
          <c:forEach items="${command.adjustmentHistoryDto}" var="dto" varStatus="dtoIndex">  
          
          <h4 class="margin-top-0">${dto.adjType} Adjustment (${dto.adjDate})</h4>
          
           <div class="table-responsive">
              <table class="table table-bordered table-condensed">
                <tr>
                  <th><spring:message text="Sr No." code="roomdetails.roomNo"/></th>
                  <th><spring:message text="Tax Name" code="propertyTax.TaxName"/></th>
                  <th><spring:message text="Adjustment Amount" code="property.duesCheck.adjustmentAmount"/></th>
                  <th><spring:message text="Adjusted Amount" code="propertyBill.AdjustedAmount"/></th>
                   <th><spring:message text="Balance Amount" code="property.BalanceAmount"/></th>
                  <th><spring:message text="Remark" code="property.landDetails.Remark"/></th>
                </tr>
                <c:forEach items="${dto.adjDetailDto}" var="detailsDTO" varStatus="detIndexdto">
                <%-- <c:if test="${detailsDTO.adjAdjustedAmount > 0}"> --%>
                <tr>
                  <td>${detIndexdto.count}</td>
                  <td>${detailsDTO.taxDesc}</td>
                  <fmt:formatNumber type="number" value="${detailsDTO.adjAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="adjAmount"/>
                  <td>${adjAmount}</td>
                   <fmt:formatNumber type="number" value="${detailsDTO.adjAdjustedAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="adjAdjustedAmount"/>
                  <td>${adjAdjustedAmount}</td>
                   <fmt:formatNumber type="number" value="${detailsDTO.adjBalanceAmount}" groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" var="adjBalanceAmount"/>
                  <td>${adjBalanceAmount}</td>
                  <td>${detailsDTO.adjRemark}</td>
                </tr>
                <%-- </c:if> --%>
                
                </c:forEach>
              </table> 
            </div>
            </c:forEach>
            <button onclick="printContent('receipt', event)" class="btn btn-primary hidden-print" style="display: block; margin: 0 auto;"><i class="fa fa-print"></i> <spring:message code="property.report.Print" text="Print"/></button>
            </c:if>
            
        <c:if test="${command.historyMsg ne null}">
        ${command.historyMsg}
        </c:if>
            
          </form:form>
        </div>
      </div>
