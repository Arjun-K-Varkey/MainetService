<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
<script type="text/javascript" src="js/property/selfAssessment.js"></script>
<style>
.addColor{
	background-color: #fff !important
}
 #unitDetailTable label{
 	font-size:0.7rem;
 }
 .tooltip {
    background-color:#000;
    border:1px solid #fff;
    padding:10px 15px;
    width:200px;
    display:none;
    color:#fff;
    text-align:left;
    font-size:15px;
 
    /* outline radius for mozilla/firefox only */
    -moz-box-shadow:0 0 10px #000;
    -webkit-box-shadow:0 0 10px #000;
}
</style>

<div class="accordion-toggle">
					    <h4 class="margin-top-0 margin-bottom-10 panel-title" id="scrollToDiv">
					        <a data-toggle="collapse" href="#UtilitySection"><spring:message text="Utility Section"/></a>
					    </h4>
					    <div class="panel-collapse collapse in" id="UtilitySection">
					        <div class="table-responsive">
					            <c:set var="d" scope="page" />
					           <table id="utilitySectionTable" class="table table-striped table-bordered appendableClass">
								    <tbody>
								        <tr>
								        	<%-- <th><spring:message code="unitdetails.unitSrNo"/></th> --%>
								            <th><spring:message text="Electricity Billing Unit Number"/></th>
								            <th><spring:message text="Electricity Consumer Number"/></th>
								            <th><spring:message text="Electricity Meter Number"/></th>
								            <%-- <th colspan="3"><spring:message text="Action"/></th> --%>
								        </tr>
								            <tr>
								            	<%-- <td>
								              		<form:input path="provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[${status.count-1}].assdUnitNo" type="text" class="form-control mandColorClass text-center unit required-control" id="unitNo${status.count-1}" data-rule-required="true" readonly="true" />
								              	</td> --%>
								                <td>
								                    <form:input path="provisionalAssesmentMstDto.electricBillUnitNo" type="text" class="form-control hasNumber text-center" id="newElectricBillUnitNo${status.count-1}" maxlength="4"/>
								                </td>
								                <td>
								                    <form:input path="provisionalAssesmentMstDto.electricConsumerNo" type="text" class="form-control hasNumber text-center" id="newElectricConsumerNo${status.count-1}" maxlength="12"/>
								                </td>
								                <td>
								                    <form:input path="provisionalAssesmentMstDto.electricMeterNo" type="text" class="form-control hasNumber text-center" id="newElectricMeterNo${status.count-1}" maxlength="11"/>
								                </td>
								               <%--  <td>
								                    <input type="button" id="duppliquer${status.count-1}" class="btn btn-success btn-submit duppliquer" onclick="copyRow(this)" value="<spring:message code="property.copy.details" text="Copy" />">
								                </td>
								                <td class="text-center">
								                    <input type="button" id="delete_${status.count-1}" class="btn btn-danger btn-xs delete" onclick="deleteRow(this)" value="<spring:message text="Delete" />">
								                </td> --%>
								            </tr>
								    </tbody>
								</table>
					        </div>
					    </div>
					</div>
