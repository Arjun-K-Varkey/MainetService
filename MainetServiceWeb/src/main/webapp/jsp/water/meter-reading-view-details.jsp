<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<style>
.tariffCategory > label[for="tariff3"],
.tariffCategory > label[for="tariff3"] + div {
	margin-top: 0.7rem;
}
.t-header{
	position: sticky;
	top: 0;
} 
.meter-reading-table{
	max-height: 500px;
}
.deletebtn{
text-align: center;
}
</style>
<script>

$('#meter-reading-view').DataTable({
		"oLanguage": { "sSearch": "" } ,
		"aLengthMenu": [ [5, 10, 15, -1], [5, 10, 15, "All"] ],
	    "iDisplayLength" : 5, 
	    "bInfo" : true,
	    "lengthChange": true,
	    "bPaginate": true,
	    "bFilter": true,
	    "fixedHeader": {
	        header: true
	    }
	    });

function deleteReading(mrdId){
	var theForm	='#MeterReading';
	var requestData = {
			"id":mrdId
	};
	var returnData =__doAjaxRequest('MeterReading.html?deleteReading','POST',requestData, false,'html');
	openPopupWithResponse(returnData);
    var mmtnId=$("#meterId").val();
	viewDetails(mmtnId);
}

function viewDetails(id)
{
	var requestData = {"id":id
	};
	var returnData =__doAjaxRequest('MeterReading.html?viewDetails','POST',requestData, false,'html');
	openPopupWithResponse(returnData);
}
</script>
<!-- <ol class="breadcrumb">
  <li><a href="../index.html"><i class="fa fa-home"></i></a></li>
  <li>Services</li>
  <li>Meter View Details</li>
</ol> -->
<!-- Start Content here -->
<div class="content max-width-850"> 
  <!-- Start info box -->
  <div class="widget">
    <div class="widget-header">
      <h2><spring:message code="water.meter.view.details"/></h2>
    </div>
    <div class="widget-content padding">
      <form:form action="MeterReading.html" method="post" class="form-horizontal" id="viewPageMeterReading">
        <div class="form-group">
          <label class="col-sm-2 control-label"><spring:message code="water.meterReadingViewDetails.Name"/></label>
          <div class="col-sm-4">
            <form:input path="viewDTO.name" type="text" class="form-control" readonly="true"></form:input>
          </div>
          <label class="col-sm-2 control-label"><spring:message code="water.plumberLicense.address"/></label>
          <div class="col-sm-4">
            <form:input path="viewDTO.address" type="text" class="form-control" readonly="true"></form:input>
          </div>
        </div>
        
        <div class="form-group">
          <label class="control-label col-sm-2"><spring:message code="water.meterReadingViewDetails.PhysicalConnectionDate"/></label>
          <div class="col-sm-4">
            <div class="input-group">
            <fmt:formatDate pattern="dd/MM/yyyy"  value="${command.viewDTO.pcDate}" var="pcdate"/>
              <form:input path="" id="datepicker" value="${pcdate}" type="text" class="form-control" readonly="true"></form:input>
              <label for="datepicker" class="input-group-addon"><i class="fa fa-calendar"></i></label>
            </div>
          </div>
          <label class="control-label col-sm-2"><spring:message code="water.meterReadingViewDetails.MeterInstallationDate"/></label>
          <div class="col-sm-4">
            <div class="input-group">
            <fmt:formatDate pattern="dd/MM/yyyy"  value="${command.meterMas.mmInstallDate}" var="installdate"/>
              <form:input path="" value="${installdate}"  id="datepicker2" type="text" class="form-control" readonly="true"></form:input>
              <label for="datepicker2" class="input-group-addon"><i class="fa fa-calendar"></i></label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label"><spring:message code="water.meterDet.mtrMake"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.mmMtrmake" type="text" class="form-control" readonly="true"></form:input>
          </div>
          <label class="col-sm-2 control-label"><spring:message code="water.meterDet.metNo"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.mmMtrno" type="text" class="form-control" readonly="true"></form:input>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label"><spring:message code="water.meterDet.mtrCost"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.mmMtrcost" type="text" class="form-control" readonly="true"></form:input>
          </div>
          <label class="col-sm-2 control-label"><spring:message code="water.meterDet.ownerShip"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.meterOwnerShip" type="text" class="form-control" readonly="true"></form:input>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label"><spring:message code="water.meter.cutOffMeterIntialReading"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.mmInitialReading" type="text" class="form-control" readonly="true"></form:input>
          </div>
          <label class="col-sm-2 control-label"><spring:message code="water.meterDet.mtrMaxRead"/></label>
          <div class="col-sm-4">
            <form:input path="meterMas.maxMeterRead" type="text" class="form-control" readonly="true"></form:input>
          </div>
        </div>
        <div class="form-group tariffCategory">
        	<apptags:lookupFieldSet baseLookupCode="TRF" hasId="true"
				showOnlyLabel="false" pathPrefix="viewDTO.tariff"
				isMandatory="false" hasLookupAlphaNumericSort="true"
				hasSubLookupAlphaNumericSort="true" disabled="true"
				cssClass="form-control"/>
        </div>
        
        <div class="table-responsive meter-reading-table">
        
            <table id="meter-reading-view" class="table table-bordered table-condensed">
              <thead>
              <tr class="t-header">
                  <th><spring:message code="water.meter.cutOffMeterReadingDate"/></th>
                  <th><spring:message code="water.meterReadingViewDetails.MeterReading"/></th>
                  <th><spring:message code="water.meterReadingViewDetails.Typeofperiod"/></th>
                  <th><spring:message code="water.met.status"/></th>
                  <th><spring:message code="water.meterReading.GapCode"/></th>
                  <th><spring:message code="water.meterReadingViewDetails.TotalDays"/></th>
                  <th><spring:message code="water.meterReadingViewDetails.Csmp"/></th>
                  <th><spring:message code="water.meterReadingViewDetails.MeterPhoto"/></th>
                  <th style="width: 5%"><spring:message code="water.meterReadingViewDetails.BillGenStatus"/></th>
                  <th style="width: 15%"><spring:message code="water.meterReadingViewDetails.BillNo."/></th>
                  <th style="width: 9%"><spring:message code="water.meterReadingViewDetails.BillFromDate"/></th>
                  <th style="width: 9%"><spring:message code="water.metetReadingViewDetails.BillToDate"/></th>
                   <th style="width: 9%"><spring:message code="water.delete" text="Delete"/></th>
               </tr>
               </thead>
               <tbody>
               <c:forEach items="${command.meterDetailsViewDTO}" var="data" varStatus="status">
             
	               <tr>
		              <td>
		              	<fmt:formatDate pattern="dd/MM/yyyy" value="${data.mrdMrdate}" var="readDate"/>
		              	<form:input path="meterDetailsViewDTO[${status.index}].mrdMrdate" value="${readDate}" type="text" id="readDate"
		              		class="form-control" readonly="true"></form:input>
		              </td>
		             
		              <td><form:input path="meterDetailsViewDTO[${status.index}].mrdMtrread" type="text" class="form-control"
		              	readonly="true"></form:input></td>
		              <td><form:input path="meterDetailsViewDTO[${status.index}].status" type="text" class="form-control" 
		              	readonly="true"></form:input></td>
		              <td><form:input path="meterDetailsViewDTO[${status.index}].meterStatusCode" type="text" class="form-control" 
		              	readonly="true"></form:input></td>
		              <td><form:input path="meterDetailsViewDTO[${status.index}].meterGapCode" type="text" class="form-control" 
		              	readonly="true"></form:input></td>
	                  <td><form:input path="meterDetailsViewDTO[${status.index}].ndays" type="text" class="form-control" 
	                  	readonly="true"></form:input></td>
	              	  <td><form:input path="meterDetailsViewDTO[${status.index}].csmp" type="text" class="form-control" 
	              	  	readonly="true"></form:input></td>
	              
		              <td>
						  <c:if test="${not empty fn:trim(data.mrdImagePath)}">
						   	<apptags:filedownload filename="${data.mrdImageName}"
						   		 filePath="${data.mrdImagePath}"
						   			 actionUrl="MeterReading.html?Download"></apptags:filedownload>
						  </c:if>
						  <c:if test="${empty fn:trim(data.mrdImagePath)}">
					      	<span><spring:message code="water.meter.image"/></span>
						  </c:if> 
					  </td>	
	              	  <td><form:input path="meterDetailsViewDTO[${status.index}].billGen" type="text" class="form-control" 
	              	  	readonly="true"></form:input></td>
		              <td><form:input path="meterDetailsViewDTO[${status.index}].bmNo" type="text" class="form-control" 
		              	readonly="true"></form:input></td>
		              <td>
		              	<form:input path="meterDetailsViewDTO[${status.index}].bmFromdt" type="text" class="form-control"
		              		 value="${billFromDate}" readonly="true"></form:input>
		              
		              </td>
		              <td>
		              	<form:input path="meterDetailsViewDTO[${status.index}].bmTodt" type="text" value="${billToDate}"
		              	 	class="form-control" readonly="true"></form:input>
		              </td>
					  <td class="deletebtn"><form:hidden	path="meterDetailsViewDTO[${status.index}].mrdId" id="metered" /> 
							<form:hidden path="" value="${command.meterMas.mmMtnid}" id="meterId" /> 
							<c:if	test="${data.billGen eq 'N'}">
								<a  title="Delete"
									class="btn btn-danger btn-sm" 
									onclick="deleteReading(${data.mrdId})"><i
									class="fa fa-trash-o"></i>
								</a>
						     </c:if>
					   </td>
	                </tr>
	              
                </c:forEach>
                </tbody>
              </table>
          
        </div>
      </form:form>
    </div>
  </div>
  <!-- End of info box --> 
</div>
<!-- End Content here --> 
