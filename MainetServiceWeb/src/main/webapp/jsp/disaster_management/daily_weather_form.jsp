<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>  
<script type="text/javascript" src="js/mainet/bootstrap-multiselect.js"></script> 
<script type="text/javascript"	src="js/disaster_management/dailyWeather.js"></script>   
    
    
    
    
<apptags:breadcrumb></apptags:breadcrumb>

<div class="content">
	<div class="widget">
		<div class="widget-header">
			<h2>
				<spring:message code="Daily Weather Report" text="Daily Weather Report" />
				<apptags:helpDoc url="DailyWeather.html"></apptags:helpDoc>
			</h2>
		</div>
		<div  class="widget-content padding">
		<form:form action="DailyWeather.html" method="POST" id="frmDailyWeather" class="form-horizontal form" commandName="command">
		
					
				<div class="form-group">
				<label for="text-1" class="col-sm-2 control-label ">
					<spring:message code="DailyWeatherDTO.temperature" text="Temperature" /><span class="mand">*</span>
				</label>
				<div class="col-sm-2">
					<form:input path="dailyWeatherDTO.minTemperature" cssClass="hasSpecialCharAndNumber form-control" maxlegnth="100" placeholder="Min" data-rule-required="true"/>
				</div>
				<div class="col-sm-2">
					<form:input path="dailyWeatherDTO.maxTemperature" cssClass="hasSpecialCharAndNumber form-control" maxlegnth="100" placeholder="Max" data-rule-required="true"/>
				</div>
				<label for="text-1" class="col-sm-2 control-label ">
					<spring:message code="DailyWeatherDTO.humidity" text="Humidity" /><span class="mand">*</span>
				</label>
				<div class="col-sm-4">
					<form:input path="dailyWeatherDTO.humidity" isMandatory="true" cssClass="fhasSpecialCharAndNumber form-control" maxlegnth="100" data-rule-required="true"/>
				</div>
				
						
				</div> 
			
				<div class="form-group">
					<apptags:input labelCode="DailyWeatherDTO.windSpeed"
						path="dailyWeatherDTO.windSpeed" isMandatory="true"
						cssClass="hasSpecialCharAndNumber form-control" maxlegnth="100"></apptags:input>
						
					<apptags:input labelCode="DailyWeatherDTO.rainFall"
						path="dailyWeatherDTO.rainFall" isMandatory="true"
						cssClass="hasSpecialCharAndNumber form-control" maxlegnth="100"></apptags:input>
				</div> 
		 
				<div class="form-group">
					<apptags:input labelCode="DailyWeatherDTO.rainSpeed"
						path="dailyWeatherDTO.rainSpeed" isMandatory="true"
						cssClass="hasSpecialCharAndNumber form-control" maxlegnth="100"></apptags:input>
						
						
					
					 <label class="col-sm-2 control-label required-control" for="employee">Employee</label>
					 
					<div class="col-sm-4">
						<form:select id="employee" path="dailyWeatherDTO.employee" cssClass="form-control chosen-select-no-results" data-rule-required="true" multiple="true">
							
							<form:option value="0">
								<spring:message code="Select" text="Select" />
							</form:option> 
							<c:forEach items="${employees}" var="employee">
								<option value="${employee.empId}">${employee.fullName}</option>
							</c:forEach>
						</form:select>
					</div> 
					
				</div>  
		
				 <div class="text-center margin-top-10">
					<input type="button" onclick="confirmToProceed(this)" value="<spring:message code="bt.save" text="Submit"/>" class="button-input btn btn-success" id="Submit"> 
					<input type="button" onclick="window.location.href='AdminHome.html'" class="btn btn-danger hidden-print" value="Back">
				</div>   
			 
		</form:form>
		</div>	
	</div>
</div>

