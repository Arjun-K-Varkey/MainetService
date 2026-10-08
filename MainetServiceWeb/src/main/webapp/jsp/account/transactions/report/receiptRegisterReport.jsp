<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    response.setContentType("text/html; charset=utf-8");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="www.ABMFrameworkTag.org" prefix="frameworktag"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<jsp:useBean id="date" class="java.util.Date"  scope="request"/>
<script src="js/account/accountCollectionSummaryReport.js"
	type="text/javascript"></script>

<script src="js/mainet/validation.js"></script>
<script
	src="js/mainet/jQueryMaskedInputPlugin.js"></script>
	<script>
	function printdiv(printpage) {
		var headstr = "<html><head><title></title></head><body>";
		var footstr = "</body>";
		var newstr = document.all.item(printpage).innerHTML;
		var oldstr = document.body.innerHTML;
		document.body.innerHTML = headstr + newstr + footstr;
		window.print();
		document.body.innerHTML = oldstr;
		return false;
	}
</script>
 <div id="content">
    <apptags:breadcrumb></apptags:breadcrumb>
    <!-- Start Content here -->
    <div class="content">
      <div class="widget">
        <div class="widget-header">
          <h2><spring:message code="receipt.register"
										text="Receipt Register" /></h2>
        </div>
        <div class="widget-content padding">
          <form action="" method="get" class="form-horizontal">
            <div id="receipt">
              <div class="form-group">
                <div class="col-sm-8 col-sm-offset-2 col-xs-8 col-xs-offset-1 text-center">
                  <h3 class="text-extra-large margin-bottom-0 margin-top-0">${ userSession.getCurrent().organisation.ONlsOrgname}</h3>
                 <h3 class="text-extra-large margin-bottom-0 margin-top-0"><spring:message code="receipt.register.caps"
										text="RECEIPT REGISTER" /> </h3> 
                <p><strong>From Date: ${reportData.fromDate}   To Date:${reportData.toDate} </strong></p>
                </div>
                <div class="col-sm-2 col-xs-3" ><p> Date:<fmt:formatDate value="${date}" pattern="dd/MM/yyyy" /><br>Time:<fmt:formatDate value="${date}" pattern="hh:mm a" /></p></div>
             </div>
              <div class=" clear">&nbsp;</div>
             
              <table  class="table table-bordered table-condensed">
  <tbody>
    <tr>
      <th><spring:message code="receipt.register.srno" text="Sr. No." /></th>
      <th><spring:message code="receipt.register.receiptnumber" text="Receipt Number" /></th>
      <th><spring:message code="receipt.register.receiptdate" text="Receipt Date" /></th>
      <th><spring:message code="receipt.register.mode" text="Mode of receipt" /></th>
      <th><spring:message code="receipt.register.namedepositor" text="Name of  the Depositor" /></th>
      <th><spring:message code="receipt.register.cheque" text="Cheque/Draft No." /></th>
      <th><spring:message code="receipt.register.receiptamount" text="Receipt Amount" /></th>
      <th><spring:message code="receipt.register.bankaccountno" text="Bank Account No." /></th>
      <th><spring:message code="receipt.register.dateofdeposit" text="Date of Deposit" /></th>
      <th><spring:message code="receipt.register.realisation" text="Date of Realisation" /></th>
      <th><spring:message code="receipt.register.whetherreturned" text="Whether Returned(Y/N)" /></th>
      <th><spring:message code="receipt.register.remarks" text="Remarks*" /></th>
    </tr>
    <c:forEach items="${reportData.listOfCollectionDetail}" var="collectionDetail" varStatus="status">
    <tr>
      <td>${status.count}</td>
      <td align="right" >${collectionDetail.receiptNumber}</td>
      <td>${collectionDetail.receiptDate}</td>
      <td>${collectionDetail.receiptMode}</td>
      <td width="15%">${collectionDetail.nameOfDepositer}</td>
      <td align="right" >${collectionDetail.chequeNumber}</td>
      <td align="right">${collectionDetail.cashAmountIndianCurrency}</td>
      <td>${collectionDetail.bankAccountNumber}</td>
      <td>${collectionDetail.dateOfDeposit}</td>
      <td>${collectionDetail.dateOfRealisation}</td>
      <td>${collectionDetail.whetherReturned}</td>
      <td>${collectionDetail.remarks}</td>
    </tr>
    </c:forEach>
    
   
    <tr>
      
      <th colspan="6"><spring:message code="receipt.register.total " text="Total Receipt Amount" /></th>
      <th class="text-right">${reportData.cashAmountTotalIndianCurrency}</th>
      <th class="text-right"></th>
      <th>&nbsp;</th>
      <th>&nbsp;</th>
      <th>&nbsp;</th>
      <th>&nbsp;</th>
     
    </tr>
    
  </tbody>
</table>
			
	</div>					


               <div class="text-center hidden-print padding-10">
							<button onclick="printdiv('receipt');"
								class="btn btn-primary hidden-print">
								<i class="fa fa-print"></i>
								<spring:message code="account.budgetestimationpreparation.print"
									text="Print" />
							</button>
							<button type="button" class="btn btn-danger"
								onclick="window.location.href='AccountCollectionReports.html'">
								<spring:message code="account.bankmaster.back" text="Back" />
							</button>
						</div>
           
          </form>
        </div>
      </div>
    </div>
    </div>