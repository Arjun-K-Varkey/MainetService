<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="js/property/publicNotice.js" ></script>   
<!-- End JSP Necessary Tags -->

<!-- Start Content here -->
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
	<%-- <div class="widget-header">
				<h2><b><spring:message text="Mutation Certificate"/></b></h2>				
	</div> --%>
		
<!-- Start Widget Content -->
<div id="printPreview">
	<div class="widget-content padding">


                    <div class="col-xs-12 col-sm-12  text-center">
                        <h3 class="text-medium margin-bottom-0 margin-top-0 text-bold">
							${command.publicNoticeDto.orgNameMar},&nbsp;${command.publicNoticeDto.orgAddressMar}
						</h3>
                        <div class="clearfix padding-10"></div>
                        <div class = "form-group clearfix padding-vertical-15">
							<div class="col-xs-8 text-center"></div>
							<div class="col-xs-4 text-center">
				              <p class="margin-top-15"><spring:message text="" code="mutation.certificate.content1"/></p>
				              <p class="margin-top-15"><spring:message text="" code="mutation.certificate.content2"/></p>
				            </div>
					</div>
                    </div>

                    <div class = "form-group clearfix padding-vertical-15">
                    <div class="col-xs-8 text-center"></div>
							<div class="col-xs-4 text-center">
                        <p>
                            <spring:message code="mutation.certificate.date"/>&nbsp;&nbsp;${command.publicNoticeDto.noticeDate}
                        </p>
                        </div>
                    </div>
                  
                    <div class="col-xs-11">
                        <p class="margin-top-15"><span class="text-bold"> <spring:message code="mutation.certificate.content3"/>
                        </span></p><p><spring:message text="" code="mutation.certificate.content4"/>
                        <span class="text-bold">&nbsp;${command.publicNoticeDto.referenceNo}</span></p>
                        <p><spring:message text="" code="mutation.certificate.content5"/><span class="text-bold">
                        &nbsp;&nbsp;${command.publicNoticeDto.certificateNo}</span></p>
                        <p><spring:message text="" code="mutation.certificate.content6"/></p>
                    </div>
                    <div class="col-xs-12 col-sm-12  text-center">
                        <h5 class="text-large margin-bottom-0 margin-top-0 text-bold">
							<spring:message text="" code="mutation.certificate.order"/>
						</h5>
					</div>
                    
                    <div class="col-xs-11">
                       <p class="margin-top-15 text-justify"> <spring:message text="" code="mutation.certificate.content7"/>
                       <span class="text-bold">&nbsp;${command.publicNoticeDto.propNo}</span> 
                       <spring:message code="mutation.certificate.content8"/>${command.publicNoticeDto.houseNo}
                       &nbsp;&nbsp;<span class="text-bold">${command.publicNoticeDto.oldOwnerFullName}</span>
                       <spring:message text="" code="mutation.certificate.content9"/>
                       <spring:message text="" code="mutation.certificate.content10"/>
                       <span class="text-bold">${command.publicNoticeDto.newOwnerFullName}</span>
                       <spring:message text="" code="mutation.certificate.content11"/>
                       <spring:message text="" code="mutation.certificate.content12"/>
                       <span class="text-bold">${command.publicNoticeDto.newOwnerFullName}</span>
                       <spring:message text="" code="mutation.certificate.content13"/>
                       <div class="clearfix padding-10"></div>
                       <spring:message text="" code="mutation.certificate.content14"/>
                       <div class="clearfix padding-10"></div>
                       <spring:message text="" code="mutation.certificate.content15"/>
                       <spring:message text="" code="mutation.certificate.content16"/>
                       <span class="text-bold">${command.publicNoticeDto.newOwnerFullName}</span>
                       <spring:message text="" code="mutation.certificate.content17"/>
                       <div class="clearfix padding-10"></div>
                       <spring:message text="" code="mutation.certificate.content18"/>
                       <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                       <spring:message text="" code="mutation.certificate.content19"/>
                       <spring:message text="" code="mutation.certificate.content20"/>
                       <span class="text-bold">&nbsp;${command.publicNoticeDto.propNo}</span>
                       <spring:message text="" code="mutation.certificate.content21"/>
                       <spring:message code="mutation.certificate.content8"/>${command.publicNoticeDto.houseNo}
                       <spring:message text="" code="mutation.certificate.content22"/>
                       <span class="text-bold">${command.publicNoticeDto.newOwnerFullName}</span>
                       <spring:message text="" code="mutation.certificate.content23"/>
                        </p>
                        <div class="clearfix padding-10"></div>
                      <div class="col-xs-11">
                      <p><spring:message text="" code="mutation.certificate.content24"/></p>
                      <p><spring:message text="" code="srno1"/><span class="text-bold">&nbsp;&nbsp;${command.publicNoticeDto.oldOwnerFullName}</span></p>
                      <p><spring:message text="" code="srno2"/><span class="text-bold">&nbsp;&nbsp;${command.publicNoticeDto.newOwnerFullName}</span></p>
                      <p><spring:message text="" code="srno3"/>&nbsp;&nbsp;<spring:message text="" code="mutation.certificate.content28"/></p>
                      </div>   
                    </div>
                    <div class="padding-5 clear">&nbsp;</div>					
					<div class = "form-group clearfix padding-vertical-15">
							<div class="col-xs-8 text-center"></div>
							<div class="col-xs-4 text-center">
				              <p class="margin-top-15"><spring:message text="" code="mutation.certificate.content25"/></p>
				              <p>${command.publicNoticeDto.orgNameMar}</p>
				              <p>${command.publicNoticeDto.orgAddressMar}</p>
				            </div>
					</div>
                    <div class="padding-5 clear">&nbsp;</div>
					
                
	                <div class="text-center hidden-print padding-10">
	                    
	                    <button onClick="printdiv('printPreview');"
								class="btn btn-primary hidden-print">
								<i class="fa fa-print"></i><spring:message code="property.report.Print"/>
						</button>
	                    <!-- <button type="button" class="btn btn-danger" onclick="back()">
	                        Cancel
	                    </button> -->
	                </div>

           
            </div>
			</div><!-- End Widget Content -->		
		</div>
</div><!-- End Content here -->

