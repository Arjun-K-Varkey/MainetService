<!-- Start JSP Necessary Tags -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- End JSP Necessary Tags -->
<script type="text/javascript" src="js/property/viewHistory.js"></script>

<!-- Start Content here -->
<div id="taskDiv">
<div class="content">
	<!-- Start Main Page Heading -->
	<div class="widget">
		<div class="widget-header">
			<h2><strong><spring:message code="mutation.ownership.transfer.history" text="Ownership Transfer History"/></strong></h2>				
		</div>
	
	<!-- End Main Page Heading -->
		
<!-- Start Widget Content -->
	<div class="widget-content padding">
		<!-- Start Form -->
		<form:form action="PropertyShowHistory.html"
					class="form-horizontal form" name="PropertyShowHistory"
					id="PropertyShowHistory">	
			<form:hidden  path="" id="returnLOIResult" class="form-control" value="${command.showFlag}"/>
			<form:hidden  path="" id="isLastAuth" class="form-control" value="${command.isLastAuthorizer()}"/>
			<form:hidden  path="" id="isBeforeLastAuth" class="form-control" value="${command.isBeforeLastAuthorizer()}"/>
			<form:hidden  path="" id="bypassPublicNotice" class="form-control" value="${command.getBypassPublicNotice()}"/>
			<form:hidden  path="" id="authLevel" value="${command.isAuthLevel()}"/>
			<form:hidden  path="" id="decision" value="${command.getWorkflowActionDto().getDecision()}"/>
			<!-- Start Each Section -->

			<div class="accordion-toggle">


			<!-- End Each Section -->
			 <c:if test="${not empty command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">

				<!----------------------------------------Existing Owner Details (First owner will be the primary owner)---------------------------->
 					<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#OwnshipDetail"><spring:message code="propertyTax.Mutation.ExistOwnerDetails" text="Existing Owner Details"/></a>
					</h4>

					<div class="panel-collapse collapse in" id="OwnshipDetail">
			
						<div class="form-group">
							<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
							<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
 							<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
							</div>
			   			 </div>
		   
		    
					<c:choose>
					<c:when test="${command.getOwnershipPrefix() eq 'SO'}">
				 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
			        
				        <tr>			                   
				        <th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
				        <th width="10%"><spring:message code="ownersdetail.ownersnameReg" /></th>
						<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
						<th width="10%"><spring:message code="property.email" /></th>						
						<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
						<th width="10%"><spring:message code="ownersdetail.pancard" />                    	 
				        </tr>
			            <tbody>      		
					      <tr>
							<td>
								<form:input id="assoOwnerName" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/>
							</td>
							
							<td>
								<form:input id="assoOwnerNameReg" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control" disabled="true"/>
							</td>
														
							<td>
								<form:input id="assoMobileno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true"/>   
							</td>
								
							<td><form:input id="emailId"
										path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
							</td>						
							<td>
								<form:input id="assoAddharno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoAddharno" class="form-control "  disabled="true"/>
							</td>
							<td>
								<form:input id="assoPanno" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control"  disabled="true"/>
							</td>
						 </tr>
						</tbody>
			   		 </table>
					</c:when>
					 	 	
					 <c:when test="${command.getOwnershipPrefix() eq 'JO'}">	
					 <c:set var="a" value='0'/>
					 
						<table id="jointOwnerTable" class="table text-left table-striped table-bordered">
							
							<tr>
								<th width="15%"><spring:message code="ownersdetail.ownersname" /></th>
								<th width="10%" ><spring:message code="ownersdetail.ownersnameReg" /></th>
								<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
								<th width="11%"><spring:message code="ownersdetail.mobileno" /></th>
								<th width="10%"><spring:message code="property.email" /></th>												
								<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
								<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
							</tr>
							<tbody>
							<c:forEach var="ownershipTypeList" items="${command.getProvisionalAssesmentMstDto().getProvisionalAssesmentOwnerDtlDtoList()}">
					    	<tr>
						         <td>${ownershipTypeList.assoOwnerName}</td>
						         <td>${ownershipTypeList.assoOwnerNameReg}</td>
						         <td>${ownershipTypeList.propertyShare}</td>
						         <td>${ownershipTypeList.assoMobileno}</td>
						         <td>${ownershipTypeList.eMail}</td>					                		 
						         <td>${ownershipTypeList.assoAddharno}</td>
						         <td>${ownershipTypeList.assoPanno}</td>
					    	</tr>
					         </c:forEach>
	
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<table id="companyDetailTable" class="table text-left table-striped table-bordered">
													
							<tr>
								<th width="25%"><spring:message code="property.NameOf" /> ${command.getOwnershipTypeValue()}</th>
								<th width="15%" ><spring:message code="ownersdetail.ownersnameReg" /></th>
								<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
								<th width="10%"><spring:message code="property.email" /></th>												
								<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
							</tr>
							<tbody>
				 			<tr>
				 			<td> 
				 			<form:input id="assoOwnerName_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerName" class="form-control" disabled="true"/> 
				 			</td> 
							<td> 									
	 							<form:input id="assoOwnerNameReg_${d}"  
	 								path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoOwnerNameReg" class="form-control " 
	 								data-rule-required="true" placeholder="Please Enter" maxlength="200" disabled="true"/> 
	 						</td>
									
				 			<td><form:input id="assoMobileno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoMobileno" class="hasNumber form-control" disabled="true" />   
				 			</td> 
								
							<td><form:input id="emailId_${d}"
								path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
							</td>	
							<td class="companyDetails"> 
							<form:input id="assoPanno_${d}" path="provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList[0].assoPanno" class="form-control" disabled="true" /> 
							</td> 
				 			</tr> 								
							</tbody>
						</table>				
					</c:otherwise>	
					</c:choose>
				 </div>
				 
<!-------------------------------------------------------------Land Details---------------------------------------------------------------------------------->
            
	
			<div class="panel-collapse collapse in margin-top-15" id="landDetail">
           		  <div class="form-group">
                  
	              	<label for="oldProNo" class="col-sm-2 control-label "><spring:message code="propertydetails.oldpropertyno"/></label>
	             	 <div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.assOldpropno" type="text" id="oldProNo" class="form-control" disabled="true"/></div>
       	  
            	  	<apptags:input labelCode="property.landType" path="provisionalAssesmentMstDto.assLandTypeDesc" isDisabled="true"></apptags:input>            	  	
            	  	<form:hidden  path="landTypePrefix" class="form-control landValue"/> 
                    <form:hidden class="form-control" id="khasraNo" path="knownKhaNo"/>	<!--For entering value in khasra/plot field-->
		     	  </div>
		     	  <c:if test="${propTransferDto.flatNo ne null}"></c:if>
		     	    <div class="form-group"><apptags:input labelCode="ownerdetail.occupierName" path="occupierName" isDisabled="true"></apptags:input>   
		     	    </div>
          	   
            </div>
         
			
<!----------------------------------------------Existing Property Address Details-------------------------------------------------------->
			
 				<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#propertyAddress"><spring:message code="propertyTax.Mutation.ExistPropDetails"/></a>
				</h4>
				<div class="panel-collapse collapse in" id="propertyAddress">
						<div class="form-group">
						<label for="assAddress" class="col-sm-2 control-label "><spring:message code="property.propertyaddress" text="Property Address"/></label>
                      		<div class="col-sm-4">
                       	<form:textarea class="form-control"  id="assAddress" path="provisionalAssesmentMstDto.assAddress" disabled="true"/>
                  		</div>
							
						<label for="location" class="col-sm-2 control-label "><spring:message code="property.location"/></label>
	              		<div class="col-sm-4"><form:input path="provisionalAssesmentMstDto.locationName" type="text" id="location" class="form-control" disabled="true"/></div>
      					
						 </div>              
			                 
	            <div class="form-group">
							<label class="col-sm-2 control-label " for="proAssPincode"><spring:message code="property.pincode"/></label>
							<div class="col-sm-4">
							<form:input class="form-control hasPincode"
				 					path="provisionalAssesmentMstDto.assPincode" id="proAssPincode" maxlength="6" readonly="true"></form:input>
			            	</div>
							<label class="col-sm-2 control-label " for="assdAlv"><spring:message code="mutation.arv" text="ARV"/></label>
							<div class="col-sm-4">
							<form:input class="form-control"
				 					path="provisionalAssesmentMstDto.totalArv" id="assdAlv"  readonly="true"></form:input>
			            	</div>
			    </div>
				<div class="form-group">
								<label class="col-sm-2 control-label " for="assNo"><spring:message
										code="property.PropertyNo" text="Property No." /></label>
								<div class="col-sm-4">
									<form:input class="form-control" path="provisionalAssesmentMstDto.assNo" id="assNo"
										readonly="true"></form:input>
								</div>
							</div>
						</div> 

	            
<!----------------------------------------------------------Tax-Zone details------------------------------------------------------ --> 
			<h4 class="margin-top-0 margin-bottom-10 panel-title">
				<a data-toggle="collapse" href="#taxZone"><spring:message
				code="property.taxZoneDetails" /></a>
			</h4>
			<div class="panel-collapse collapse in" id="taxZone">
			<div class="panel-body">
				<div class="form-group">
			
				<apptags:input labelCode="property.propertyZone" path="provisionalAssesmentMstDto.assWardDesc1" isDisabled="true"></apptags:input>
				
				<c:if test="${command.provisionalAssesmentMstDto.assWard2 ne null}">			
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc2" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group">
				<c:if test="${command.provisionalAssesmentMstDto.assWard3 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc3" isDisabled="true"></apptags:input>
				</c:if>
				<c:if test="${command.provisionalAssesmentMstDto.assWard4 ne null}">
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc4" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group">
				<c:if test="${command.provisionalAssesmentMstDto.assWard5 ne null}">
				
				<apptags:input labelCode="property.propertyWard" path="provisionalAssesmentMstDto.assWardDesc5" isDisabled="true"></apptags:input>
				</c:if>
				</div>
				<div class="form-group">
				<apptags:input labelCode="unitdetails.RoadType" path="provisionalAssesmentMstDto.proAssdRoadfactorDesc" isDisabled="true"></apptags:input>
				</div>
			
			</div>
			</div>
					
					
					
				<!-----------------------------------------------Transfer Details-------------------------------------------------------------------->
					<h4 class="margin-top-0 margin-bottom-10 panel-title">
					<a data-toggle="collapse" href="#transferDetails"><spring:message code="propertyTax.mutation.TransferDetails" text=""/></a>
					</h4>
				 <div class="panel-collapse collapse in" id="transferDetails">
					 <div class="form-group">
							<label class="col-sm-2 control-label "><spring:message code="propertyTax.mutation.TransferType" text="Transfer Type"/></label>
									<div class="col-sm-4">
									<c:set var="baseLookupCode" value="TFT" /> <form:select path="propTransferDto.transferType"
											 class="form-control changeParameterClass mandColorClass"   disabled="true">
												<form:option value="">
													<spring:message code="property.sel.optn.ownerType" />
												</form:option>
												<c:forEach items="${command.getLevelData(baseLookupCode)}"
													var="lookUp">
													<form:option value="${lookUp.lookUpId}"
														code="${lookUp.lookUpCode}">${lookUp.lookUpDesc}</form:option>
												</c:forEach>
									</form:select>
									</div>
		
							<label class="col-sm-2 control-label "><spring:message code="propertyTax.mutation.TransferDate" text="Actual Transfer Date"/></label>
							<div class="col-sm-4">
							<div class="input-group"> 
									<form:input path="propTransferDto.actualTransferDate" class="lessthancurrdate form-control mandColorClass dateClass" id="actualTransferDate"  placeholder="DD/MM/YYYY" readonly="true" autocomplete="off"/>
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>			
							</div>
							
							</div>
					</div>
					<div class="form-group">
					
						<label class="col-sm-2 control-label " for="marketValue"><spring:message code="property.MarketValue" text="Market Value"/></label>
							<div class="col-sm-4">
							<form:input  type="text" class="form-control mandColorClass hasNumber"
				 					path="propTransferDto.marketValue" id="marketValue" readonly="true" ></form:input>
							</div>
							
							<label class="col-sm-2 control-label " for="salesDeedValue"><spring:message code="property.SalesDeedValue" text="Sales Deed Value"/></label>
							<div class="col-sm-4">
							<form:input  type="text" class="form-control mandColorClass hasNumber"
				 					path="propTransferDto.salesDeedValue" id="salesDeedValue" readonly="true" ></form:input>
							</div>
						
					</div> 
					
					<div class="form-group">
								<label class="col-sm-2 control-label" for="regNo"><spring:message
										code="property.regNo" /></label>
								<div class="col-sm-4">
									<form:input
										path="propTransferDto.regNo" readonly="true"
										id="regNo" class="form-control" maxlength="25" />
								</div>

								<label class="col-sm-2 control-label" for="flatNo"><spring:message
										code="unitdetails.flatNo" text="Flat No." /></label>
								<div class="col-sm-4">
									<form:input path="propTransferDto.flatNo" readonly="true"
										id="flatNo" class="form-control" maxlength="25" />
								</div>
							</div>
					</div> 
					
		<!-------------------------------------------------------New Owner Details On Authorization------------------------------------------------------->			
					
						<h4 class="margin-top-0 margin-bottom-10 panel-title">
							<a data-toggle="collapse" href="#newOwnerDetails"><spring:message code="propertyTax.mutation.NewOwnerDetails" text="New Owner Details (First owner will be the primary owner)"/></a>
						</h4>
						
							<div class="panel-collapse collapse in" id="newOwnerDetails">
			
						<div class="form-group">
							<label class="col-sm-2 control-label" for="ownershiptype"><spring:message code="property.ownershiptype" /></label>
							<div class="col-sm-4"><form:input path="propTransferDto.proAssOwnerTypeName"  id="ownershiptype" class="form-control" disabled="true"/>
 							<form:hidden  path="ownershipPrefix"  id="ownershipId" class="form-control"/>
							</div>
			   			 </div>
		   
		    
					<c:choose>
					<c:when test="${command.getOwnershipPrefixNew() eq 'SO'}">
				 	<table id="singleOwnerTable" class="table table-striped table-bordered ">
			        
				        <tr>			                   
				        <th width="20%"><spring:message code="ownersdetail.ownersname" /></th>
				        <th width="10%"><spring:message code="ownersdetail.ownersnameReg" /></th>
						<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
						<th width="10%"><spring:message code="property.email" /></th>					
						<th width="12%"><spring:message code="ownersdetail.adharno" /></th>
						<th width="10%"><spring:message code="ownersdetail.pancard" />                    	 
				        </tr>
			            <tbody>      		
					      <tr>
							<td>
								<form:input id="assoOwnerName" path="propTransferDto.propTransferOwnerList[0].ownerName" class="form-control" disabled="true"/>
							</td>
							
							<td>
								<form:input id="assoOwnerNameReg" 
											path="propTransferDto.propTransferOwnerList[0].assoOwnerNameReg" class="form-control "
											data-rule-required="true" placeholder="Please Enter Owners Name (Reg)" maxlength="200" disabled="true"/>
							</td>
														
							<td>
								<form:input id="assoMobileno" path="propTransferDto.propTransferOwnerList[0].mobileno" class="hasNumber form-control" disabled="true"/>   
							</td>
							
							<td><form:input id="emailId"
										path="propTransferDto.propTransferOwnerList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
							</td>
														
							<td>
								<form:input id="assoAddharno" path="propTransferDto.propTransferOwnerList[0].addharno" class="form-control "  disabled="true"/>
							</td>
							<td>
								<form:input id="assoPanno" path="propTransferDto.propTransferOwnerList[0].panno" class="form-control"  disabled="true"/>
							</td>
						 </tr>
						</tbody>
			   		 </table>
					</c:when>
					 	 	
					 <c:when test="${command.getOwnershipPrefixNew() eq 'JO'}">	
					 <c:set var="a" value='0'/>
					 
						<table id="jointOwnerTable" class="table text-left table-striped table-bordered">
							
							<tr>
								<th width="15%"><spring:message code="ownersdetail.ownersname" /></th>
								<th width="7%"><spring:message code="ownersdetail.ownersnameReg" /></th>
								<th width="5%"><spring:message code="ownerdetails.PropertyShare" /></th>	
								<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
								<th width="10%"><spring:message code="property.email" /></th>																	
								<th width="10%"><spring:message code="ownersdetail.adharno" /></th>
								<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
							</tr>
							<tbody>
							<c:forEach var="ownershipTypeList" items="${command.getPropTransferDto().getPropTransferOwnerList()}">
					    	<tr>
					         <td>${ownershipTypeList.ownerName}</td>
					         <td>${ownershipTypeList.assoOwnerNameReg}</td>
					         <td>${ownershipTypeList.propertyShare}</td>
					         <td>${ownershipTypeList.mobileno}</td>
					         <td>${ownershipTypeList.eMail}</td>					                		 
					         <td>${ownershipTypeList.addharno}</td>
					         <td>${ownershipTypeList.panno}</td>
					    	</tr>
					         </c:forEach>
	
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<table id="companyDetailTable" class="table text-left table-striped table-bordered">
													
						<tr>
							<th width="25%"><spring:message code="property.NameOf" /> ${command.getOwnershipTypeValue()}</th>
							<th width="15%" class="required-control"><spring:message code="ownersdetail.ownersnameReg" /></th>
							<%-- <th width="10%"><spring:message code="ownersdetail.contactpersonName" /></th> --%>
							<th width="10%"><spring:message code="ownersdetail.mobileno" /></th>
							<th width="10%"><spring:message code="property.email" /></th>											
							<th width="10%"><spring:message code="ownersdetail.pancard" /></th>
						</tr>
						<tbody>
				 			<tr>
					 			<td> 
					 			<form:input id="assoOwnerName_${d}" path="propTransferDto.propTransferOwnerList[0].ownerName" class="form-control" disabled="true"/> 
					 			</td> 
								
								<td> 									
		 							<form:input id="assoOwnerNameReg_${d}"  
		 								path="propTransferDto.propTransferOwnerList[0].assoOwnerNameReg" class="form-control " 
		 								data-rule-required="true" placeholder="Please Enter" maxlength="200" disabled="true"/> 
		 						</td>
										
					 			<td><form:input id="assoMobileno_${d}" path="propTransferDto.propTransferOwnerList[0].mobileno" class="hasNumber form-control" disabled="true" />   
					 			</td> 
								
								<td><form:input id="emailId_${d}"
									path="propTransferDto.propTransferOwnerList[0].eMail" class="hasemailclass form-control" disabled="true"/>   
								</td>
								<td class="companyDetails"> 
								<form:input id="assoPanno_${d}" path="propTransferDto.propTransferOwnerList[0].panno" class="form-control" disabled="true" /> 
								</td> 
				 			</tr> 								
						</tbody>
						</table>				
					</c:otherwise>	
					</c:choose>
				 </div>			
					
							
		
			</c:if>
		
<c:if test="${command.getApprovalFlag() eq 'Y' && not empty command.propTransferDto.charges}">  
   <h4 class="margin-top-10 margin-bottom-10 panel-title ">
   <a data-toggle="collapse" href="#TaxCalculation" class="contentRemove"><spring:message code="propertyTax.mutation.Loi.charges" text="LOI Charges"/></a>
    </h4>         
    <div class="panel-collapse collapse in" id="TaxCalculation">      
     
      <c:set var="totPayAmt" value="0"/>
            <div class="table-responsive">
              <table class="table table-striped table-condensed table-bordered">
                <tbody>
                  <tr>
                    <th width="50"><spring:message code="propertyTax.SrNo"/></th>
                    <th width="600"><spring:message code="propertyTax.TaxName"/></th>
                    <th width="400"><spring:message code="propertyTax.Total"/></th>
                  </tr>
                <c:forEach var="tax" items="${command.propTransferDto.charges}"  varStatus="status">
                  <tr>
                  <td>${status.count}</td>
                  <td>${tax. getTaxDesc()}</td>
                  <td class="text-right">${tax.getTotalTaxAmt()}</td>
                </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
            <div class="table-responsive margin-top-10">
              <table class="table table-striped table-bordered">
                <tr>
                  <th width="500"><spring:message code="propertyTax.TotalTaxPayable"/></th>
                  <th width="500" class="text-right">${command.propTransferDto.billTotalAmt}</th>
                </tr>
              </table>
            </div>
          </div>
      </c:if> 				
					
		
					
			
        
        <c:if test="${command.showFlag eq 'Y' }">
					<h4><spring:message code="loi.fees.and.charges" text="LOI Fees and Charges in Details"/></h4>
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<tr>
								<th scope="col" width="80"><spring:message code="bill.srNo" text="Sr. No"/></th>
								<th scope="col"><spring:message code="charge.name" text="Charge Name"/></th>
								<th scope="col"><spring:message code="property.Amount" text="Amount"/></th>
							</tr>
							<c:forEach var="charges" items="${command.loiDetail}"
								varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td>${charges.loiRemarks}</td>
									<td><fmt:formatNumber value="${charges.loiAmount}"
											type="number" var="amount" minFractionDigits="2"
											maxFractionDigits="2" groupingUsed="false" /> <form:input
											path="" type="text" class="form-control text-right"
											value="${amount}" readonly="true" /></td>
								</tr>
							</c:forEach>

							<tr>
								<td colspan="2"><span class="pull-right"><b><spring:message code="total.loi.amount" text="Total LOI Amount"/></b></span></td>
								<td class="text-right">${command.totalLoiAmount}</td>

							</tr>
						</table>
					</div>
				</c:if>

		<div class="text-center padding-top-10 hidden-print">
			<apptags:backButton url="AdminHome.html"></apptags:backButton>									
		</div>
					
		</div>
	</form:form>

		</div>
	
		</div>
</div>
</div>

