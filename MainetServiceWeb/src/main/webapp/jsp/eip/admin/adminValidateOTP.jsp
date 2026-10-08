<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<style>
.login-input {
    position: relative;
    }
.login-input span.overlay-right {
	position: absolute;
	right: 0px;
	top:0px;
	color: #FFF;
	padding : 7px 12px 9px 2px;
	background:#323c57;
}
.successTimerClass{
	color: #337ab7;
}
.warnTimerClass{
	color: #ff345c !important;
}
.resendOtp.disabled-link{
    pointer-events: none;
}
</style>
<script>
$( '.form-control' ).on( "copy cut paste drop", function() {
    return false;
});
function showOtp() {
	  var x = document.getElementById("adminEmployee.mobNoOtp");
	  if (x.type === "password") {
	    x.type = "text";
	  } else {
	    x.type = "password";
	  }
	}
</script>


	<div class="widget login">
	<div class="widget-header">
		<h2>
			<strong>
		<spring:message code="eip.admin.otp.verification" text="OTP Verification" />
</strong>
</h2>
</div>
<div class="widget-content padding">

<!-- Admin Reset Password -->
	<div class="error-div alert alert-danger" id="rest_error"></div> 

	<form:form id="adminValidateOTP" name="adminValidateOTP" method="POST" autocomplete="on">
			<form:hidden path="otpTimer" id="otpTimerId"/>
			<form:hidden path="adminEmployee.empmobno" id="empmobnoId"/>
			
			<p id="timer_div" class="text-center successTimerClass"></p>
			<div class="form-group">
				<label class="control-label"> 
					<spring:message	code="eip.admin.otp" text="OTP"/> :
				</label> 
			</div>
			<div class="form-group login-input">
				<input type="password" class="form-control text-input" id="adminEmployee.mobNoOtp" name="adminEmployee.mobNoOtp" maxlength="10"
							placeholder='<spring:message code="" text="OTP" />' onkeypress="return trySubmit(event)" 
							autofocus="autofocus"><span class="overlay-right" onclick="showOtp();"><i class="fa fa-eye" aria-hidden="true"></i></span>		
			</div>

		
		<a href="javascript:void(0);"  onclick="resendOTP()" class="resendOtp"><spring:message code="eip.admin.otp.resendOTP" text="Resend OTP" /></a>
			
		<div class="clear"></div>
		<div class="row">
			<div class="col-sm-6">
			<input class="btn btn-success btn-block" type="button"
						onclick="doOtpVerification(this);" id="submitOTP"
						value='<spring:message code="eip.commons.submitBT"/>' />
			</div>
			<div class="col-sm-6">
			<input type="reset" class="btn btn-danger btn-block" value="<spring:message code="eip.commons.resetBT"/>" onclick="{$('.error-div').html('');}">  
		</div>	
		</div>
		
	</form:form>
</div>
</div>