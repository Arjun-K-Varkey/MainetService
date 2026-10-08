<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
<script src="js/mainet/validation.js"></script>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<script src="js/account/chequeOrCashDeposite.js"></script>
<link href="assets/css/print.css" media="print" rel="stylesheet" type="text/css"/>
<script>
	function printdiv(printpage)
	{
		var headstr = "<html><head><title></title></head><body>";
		var footstr = "</body>";
		var newstr = document.all.item(printpage).innerHTML;
		var oldstr = document.body.innerHTML;
		document.body.innerHTML = headstr+newstr+footstr;
		window.print();
		document.body.innerHTML = oldstr;
		return false;
	}
</script>
<style>
.cert-outer-div {
	border: 2px solid #000000;
}
.DepositSlipReport .border-bottom-black { 
     border-bottom: 2px solid #000000;
}
.depositSlipTable{
	margin-left: 1.2rem !important;
    width: 97%;
}
</style>

 <div class="widget">
            <div class="widget-header">
                <h2><spring:message code="account.deposit.slip"
								text="Bank Deposit Slip" />
                </h2>
            </div>
            <div id="receipt">
            <div class="widget-content padding">
                <form:form  modelAttribute = "accountChequeOrCashDepositDTO" action="AccountDepositSlip.html" method="POST" class="form-horizontal DepositSlipReport">
                	<div class="cert-outer-div">
                        <!-- <div class="row"> -->
                          <div class="border-bottom-black margin-0">	
	                        <div class="overflow-hidden padding-vertical-10">
	                        	<div class="col-xs-2 col-sm-2">
									<img width="80" src="${userSession.orgLogoPath}">
								</div>
	                        	<div class="col-xs-8 col-sm-8 text-center">
	                            	<h3 class="text-extra-large  margin-bottom-0 margin-top-0">${ userSession.getCurrent().organisation.ONlsOrgname}</h3>
	                            </div>
	                        </div>  
                            <div class="col-xs-12 col-sm-12">
                            	<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'PC'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(POS Cheque)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'Q'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(Cheque)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'D'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(DemandDraft)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'POS'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(POS)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'W'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(WEB)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'P'}">
	                                <p class="text-center "><spring:message code="account.slip"
									text="Bank Deposit Slip(PAY ORDER)" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'C'}">
									<p class="text-center "><spring:message code=""
									text="Cash Deposit Slip" /></p>
								</c:if>
								<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'B'}">
									<p class="text-center "><spring:message code=""
									text="Bank Deposit Slip (Bank)" /></p>
								</c:if>
                            </div>
                          </div>  
                          
                            
                            <div class="clearfix"></div>
                            <div class="col-xs-8 col-sm-8">
                                <h5 class="padding-top-9"><strong><spring:message code="account.deposit.holder.name"
								text="Name of Account Holder " /> </strong>${accountChequeOrCashDepositDTO.organisationName}--${accountChequeOrCashDepositDTO.bankAccountName}</h5>
                            </div>
                            <div class="col-xs-4 col-sm-4 text-right padding-right-20">
                               <h5><strong><spring:message code="account.deposit.slipNumber"
								text="SLIP NUMBER" /></strong> : ${accountChequeOrCashDepositDTO.slipNumber}</h5>
                            </div>
                            <div class="col-xs-8 col-sm-8">
                                <h5 ><strong><spring:message code="account.deposit.type"
								text="Bank Account Type  " /> </strong>${accountChequeOrCashDepositDTO.bankAccountType}</h5>
                            </div>
                            <div class="col-xs-4 col-sm-4 text-right">
                               <h5><strong><spring:message code="account.deposit.dates"
								text="Date:" /></strong> : ${accountChequeOrCashDepositDTO.remittanceDate}</h5>
                            </div>                      
                            
                            <div class="col-xs-12 col-sm-12">
                              
                                    <h5><strong><spring:message code="account.deposit.account"
								text="Account No." /></strong>${accountChequeOrCashDepositDTO.bankaccountNo}</h5>
                                
                                
                            </div>
                            <div class="col-xs-12 col-sm-12">
                                <h5 ><strong><spring:message code="account.deposit.account.holder"
								text="Account Holder Branch" /></strong>${accountChequeOrCashDepositDTO.bank}-${accountChequeOrCashDepositDTO.branch}</h5>
                            </div>
                            
                            <div class="clearfix"></div>
                           
                            
                            
                        <!-- </div> -->
                        	<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'POS'}">
                          <table id="table1" class="table table-bordered table-condensed depositSlipTable">
                          
                                <tr>
                                 <th><spring:message code="account.deposit.sr.no"
								text="SR. NO." /></th>
                                    <th><spring:message code="account.deposit.receiptno"
								text="Receipt No" /></th>
                                    <th><spring:message code="account.deposit.receiptdate"
								text="Receipt Date" /></th>
                                     <th><spring:message code="account.deposit.redept"
								text="Department" /></th>                                    
                                    <th><spring:message code="account.deposit.rs"
								text="Rs." /></th>
                                   <%--   <th><spring:message code="account.deposit.modeid"
								text="MODE ID" /> </th>  --%>
                                     </tr>  
                                     <c:set var="totalCount" value="0"></c:set>  
                                     <c:forEach items="${accountChequeOrCashReceipts}"
											varStatus="status" var="accReceipts">     
									<tr>
									<td align="center">${status.index+1}</td>
									<td align="left">${accReceipts.rmReceiptNo}</td>
									<td align="left">${accReceipts.transactionDate}</td>
                                    <td align="right">${accReceipts.deptName}</td>
                                    <td align="right">${accReceipts.rmAmount}</td>
                              <%--       <td align="right">${denLookupVar.modeId}</td> --%>
                                     <c:set var="totalCount" value="${status.index+1}"></c:set>
                                    </tr>
                                   </c:forEach>
                                <tr>
                                
	                               <%--  <td colspan="2"><strong><spring:message code="account.deposit.rupeesinwords"
									text="Rupees In Words" /> </strong>${accountChequeOrCashDepositDTO.totalAmountInWords}</td> --%>
                                    <th colspan="2"><strong>TOTAL</strong></th>
                                      <th style="text-align: right;"><c:out value="${totalCount}"/></th>
                                    <th style="text-align: right;"></th>
                                    <th style="text-align: right;">${accountChequeOrCashDepositDTO.indainCurrencyAmount}</th>
                                    <c:if test="${not empty accountChequeOrCashDepositDTO.indianCurrencyDecimalAmount}">
                                    <th style="text-align: right;">${accountChequeOrCashDepositDTO.indianCurrencyDecimalAmount}</th>
                                    </c:if>
                                </tr>
                                <tr>
                                     <td colspan="5"><strong><spring:message code="account.deposit.rupeesinwords"
									text="Rupees In Words" /> </strong>${accountChequeOrCashDepositDTO.totalAmountInWords}</td>
                                    
                                </tr>
                                <tr>
                                    <th class="text-center" colspan="5"><strong><spring:message code="account.deposit.office"
								text="FOR OFFICE USE" /> </strong></th>
                                    
                                </tr>
                                <tr>
                                    <%-- <th height="40"><spring:message code="account.deposit.slipNumber"
								text="SLIP NUMBER" /></th>
                                    <td>${accountChequeOrCashDepositDTO.slipNumber}</td> --%>
                                    <th><spring:message code="account.deposit.account.ddeposit.verifyingofficer"
								text="VERIFYING OFFICER" /> </th>
                                    <td></td>
                                    <th colspan="2"><strong><spring:message code="account.deposit.signature"
								text="SINNATURE OF DEPOSITER" /> </strong></th>
                                    <td colspan=""></td>
                                </tr>
                           
                          </table>
                        </c:if>
						<c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode ne 'C' && accountChequeOrCashDepositDTO.depositeTypeCode ne 'POS'}">
                          <table id="table1" class="table table-bordered table-condensed depositSlipTable">
                          
                                <tr>
                                 <th><spring:message code="account.deposit.sr.no"
								text="SR. NO." /></th>
                                    <th><spring:message code="account.deposit.bank"
								text="BANK" /></th>
                                    <th><spring:message code="account.deposit.branch"
								text="BRANCH" /></th>
                                     <th><spring:message code="account.deposit.chequeno"
								text="CHEQUE NO." /></th>
                                    <%-- <th width="10%"><spring:message code="account.deposit.deno"
								text="DENO" /> </th> --%>
                                    <th><spring:message code="account.deposit.chequedate"
								text="CHEQUE DATE" /> </th>
                                    <th><spring:message code="account.deposit.rs"
								text="Rs." /></th>
                                   <%--   <th><spring:message code="account.deposit.modeid"
								text="MODE ID" /> </th>  --%>
                                     </tr>  
                                     <c:set var="totalCount" value="0"></c:set>  
                                     <c:forEach items="${accountChequeOrCashDepositDTO.denomination}"
											varStatus="status" var="denLookupVar">     
									<tr>
									<td align="center">${status.index+1}</td>
									<td align="left">${denLookupVar.depositBankName}</td>
									<td align="left">${denLookupVar.branch}</td>
									<td align="right">${denLookupVar.payOrderNo}</td>
									<%-- <td align="right">${denLookupVar.rupesTpye}</td> --%>
                                    <td align="right">${denLookupVar.chequeDate}</td>
                                    <td align="right">${denLookupVar.indainCurrencyAmount}</td>
                              <%--       <td align="right">${denLookupVar.modeId}</td> --%>
                                     <c:set var="totalCount" value="${status.index+1}"></c:set></td>
                                    </tr>
                                   </c:forEach>
                                <tr>
                                
	                               <%--  <td colspan="2"><strong><spring:message code="account.deposit.rupeesinwords"
									text="Rupees In Words" /> </strong>${accountChequeOrCashDepositDTO.totalAmountInWords}</td> --%>
                                    <th colspan="3"><strong>TOTAL</strong></th>
                                      <th style="text-align: right;"><c:out value="${totalCount}"/></th>
                                    <th style="text-align: right;"></th>
                                    <th style="text-align: right;">${accountChequeOrCashDepositDTO.indainCurrencyAmount}</th>
                                    <c:if test="${not empty accountChequeOrCashDepositDTO.indianCurrencyDecimalAmount}">
                                    <th style="text-align: right;">${accountChequeOrCashDepositDTO.indianCurrencyDecimalAmount}</th>
                                    </c:if>
                                </tr>
                                <tr>
                                     <td colspan="7"><strong><spring:message code="account.deposit.rupeesinwords"
									text="Rupees In Words" /> </strong>${accountChequeOrCashDepositDTO.totalAmountInWords}</td>
                                    
                                </tr>
                                <tr>
                                    <th class="text-center" colspan="7"><strong><spring:message code="account.deposit.office"
								text="FOR OFFICE USE" /> </strong></th>
                                    
                                </tr>
                                <tr>
                                    <%-- <th height="40"><spring:message code="account.deposit.slipNumber"
								text="SLIP NUMBER" /></th>
                                    <td>${accountChequeOrCashDepositDTO.slipNumber}</td> --%>
                                    <th><spring:message code="account.deposit.account.ddeposit.verifyingofficer"
								text="VERIFYING OFFICER" /> </th>
                                    <td></td>
                                    <th colspan="3"><strong><spring:message code="account.deposit.signature"
								text="SINNATURE OF DEPOSITER" /> </strong></th>
                                    <td colspan="2"></td>
                                </tr>
                           
                          </table>
                        </c:if>
                        <c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'C'}">
                       		 <table class="table table-bordered table-condensed depositSlipTable">
                          
                                <tr>
                                   <%--  <th width="180"><spring:message code="account.deposit.bank"
								text="BANK" /></th>
                                    <th width="154"><spring:message code="account.deposit.branch"
								text="BRANCH" /></th>
                                     <th width="10%"><spring:message code="account.deposit.chequeno"
								text="CHEQUE NO." /></th> --%>
                                    <th width="40%"><spring:message code="account.deposit.deno"
								text="DENO" /> </th>
                                    <th colspan="2"><spring:message code="account.deposit.count"
								text="COUNT" /> </th>
                                    <th><spring:message code="account.deposit.rs"
								text="Rs." /></th>
                                     <th><spring:message code="account.deposit.pices"
								text="PAISA" /> </th> 
                                     </tr>  
                                     <c:forEach items="${accountChequeOrCashDepositDTO.denomination}"
											varStatus="status" var="denLookupVar">                           
									<tr>
									<%-- <td align="left">${denLookupVar.depositBankName}</td>
									<td align="left">${denLookupVar.branch}</td>
									<td align="right">${denLookupVar.payOrderNo}</td> --%>
									<td align="right">${denLookupVar.rupesTpye}</td>
                                    <td align="right" colspan="2">${denLookupVar.numberOfRupes}</td>
                                    <td align="right">${denLookupVar.indainCurrencyAmount}</td>
                                    <td align="right">${denLookupVar.indianCurrencyDecimalAmount}</td>
                                    </tr>
                                   </c:forEach>
                             <%--    <tr>
                                    <td><strong><spring:message code="account.deposit.rupeesinwords"
								text="Rupees In Words" />${accountChequeOrCashDepositDTO.totalAmountInWords} </strong></td>
                                    
                                   
                                   <td>TOTAL: </th>
                                    <td colspan="2" style="text-align: right;">${accountChequeOrCashDepositDTO.picesCount}</td>
                                    <td style="text-align: right;">${accountChequeOrCashDepositDTO.indainCurrencyAmount}</td>
                                    <td style="text-align: right;">${accountChequeOrCashDepositDTO.indianCurrencyDecimalAmount}</td>

                                </tr> --%>
                                <tr>
                                <th></th>
                                    <th class="text-center" colspan="2"><strong><spring:message code=""
								text="TOTAL AMOUNT" /></strong></th>
                                    <th style="text-align: right;"><strong> ${accountChequeOrCashDepositDTO.totalAmount}</strong></th>
                                    <!-- <th></th> -->
                                </tr>
                                <tr>
                                     <td colspan="7"><strong><spring:message code="account.deposit.rupeesinwords"
									text="Rupees In Words" /> </strong>${accountChequeOrCashDepositDTO.totalAmountInWords}</td>
                                    
                                </tr>
                                <tr>
                                    <th class="text-center" colspan="7"><strong><spring:message code="account.deposit.office"
								text="FOR OFFICE USE" /> </strong></th>
                                    
                                </tr>
                                <tr>
                                    <%-- <th height="40"><spring:message code="account.deposit.slipNumber"
								text="SLIP NUMBER" /></th>
                                    <td>${accountChequeOrCashDepositDTO.slipNumber}</td> --%>
                                    <th><spring:message code="account.deposit.account.ddeposit.verifyingofficer"
								text="VERIFYING OFFICER" /> </th>
                                    <td></td>
                                    <th colspan="2"><strong><spring:message code="account.deposit.signature"
								text="SINNATURE OF DEPOSITER" /> </strong></th>
                                    <td></td>
                                </tr>
                           
                           </table>
                        </c:if>
                        
                    <%--     <c:if test="${accountChequeOrCashDepositDTO.depositeTypeCode eq 'W' || accountChequeOrCashDepositDTO.depositeTypeCode eq 'POS'}">
	                            <table class="table table-bordered table-condensed depositSlipTable">
                          
	                                <tr>
	                               
	                                   			 <th><spring:message code=""
															text="Amount in Words" /></th>
	                                    		<th></th>
	                                    		<th><spring:message code="account.deposit.rs"
															text="Rs." /></th>
	                                    		<th></th>
	                                 </tr>  
                                     <c:forEach items="${accountChequeOrCashDepositDTO.denomination}"
											varStatus="status" var="denLookupVar">                           
										<tr>
										
	                                    		<td align="right">${denLookupVar.total}</td>
	                                    </tr>
                                 	  </c:forEach>
                                	  <tr>
			                                   	 <td><strong><spring:message code="account.deposit.rupeesinwords"
														text="Rupees In Words" />${accountChequeOrCashDepositDTO.totalAmountInWords} </strong></td>
			                                    
			                                   
			                                    <td>TOTAL: </td>
			                                    <td style="text-align: right;">${accountChequeOrCashDepositDTO.total}</td>
			                                	<td></td>

                                	</tr>
                              		<tr>
	                                	<th></th>
	                               
	                                    <th class="text-center" colspan=""><strong><spring:message code=""
												text="TOTAL AMOUNT" /></strong></th>
	                                    <th style="text-align: right;"><strong> ${accountChequeOrCashDepositDTO.totalAmount}</strong></th>
	                                    <th></th>
		                             </tr>
		                             <tr>
		                                    	<th class="text-center" colspan="4"><strong><spring:message code="account.deposit.office"
														text="FOR OFFICE USE" /> </strong></th>
                                    
		                              </tr>
		                              <tr>
                                  
		                                    <th><spring:message code="account.deposit.account.ddeposit.verifyingofficer"
										text="VERIFYING OFFICER" /> </th>
		                                    <td></td>
		                                    <th colspan=""><strong><spring:message code="account.deposit.signature"
										text="SINNATURE OF DEPOSITER" /> </strong></th>
		                                    <td></td>
		                                </tr>
                           
		                           </table>
								</c:if> --%>
                        
                        
                        
                        	
                        </div>
                        <div class="text-center hidden-print padding-10">
                            <button onclick="printdiv('receipt');" class="btn btn-primary hidden-print"><i class="fa fa-print"></i> Print</button>
                            <input type="button" class="btn btn-danger"
							onclick="window.location.href='AccountDepositSlip.html'"
							value="Back" id="cancelEdit" />
                        </div>    
                </form:form>
               </div> 
            </div>
        </div>
  