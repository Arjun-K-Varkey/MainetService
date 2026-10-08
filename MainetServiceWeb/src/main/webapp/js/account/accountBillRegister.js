$(document)
		.ready(
				function() {
							
					
					$('#PrintButn').click(function() {
					
						showloader(true);
						setTimeout(function(){
						$('.error-div').hide();
						var errorList = [];
						
						var Fromdate = $("#frmdate").val();
						var Todate = $("#tocdate").val();
						var accountHead= $("#accountHead").val();
						var fieldId=$("#fieldId").val();
						var billType=$("#billType").val();
						
					
						if (Fromdate == "") {
							errorList.push(getLocalMessage("account.enter.fromDate"));
					
						}

						if (Todate == "") {
							errorList.push(getLocalMessage("account.enter.toDate"));
						
						}
						if (accountHead == "") {
							errorList.push(getLocalMessage("account.enter.accounthead"));
					
						}
						if (fieldId == "") {
							errorList.push(getLocalMessage("account.enter.fieldid"));
					
						}
						if (billType == "") {
							errorList.push(getLocalMessage("account.enter.billtype"));
					
						}
						if(Fromdate!=null)
							{
							errorList = validatedate(errorList,'frmdate');
							}
						if(Todate!=null)
							{
							errorList = validatedate(errorList,'tocdate');
							}
						  var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
						  var eDate = new Date($("#frmdate").val().replace(pattern,'$3-$2-$1'));
						  var sDate = new Date($("#tocdate").val().replace(pattern,'$3-$2-$1'));
						  if (eDate > sDate) {
							  errorList.push("To Date can not be less than From Date");
							  $( "#reportTypeId" ).prop( "disabled", true );
						    }

						if (errorList.length > 0) {
						
							var errorMsg = '<ul>';
								$.each(errorList,function(index) {
													errorMsg += '<li><i class="fa fa-exclamation-circle"></i>&nbsp;'+ errorList[index] + '</li>';
									  });
								errorMsg += '</ul>';
						
								$('#errorId').html(errorMsg);
								$('#errorDivId').show();
								$('html,body').animate({scrollTop : 0}, 'slow');
							
						} 
						if (errorList.length == 0) 
						{
							var Fromdate1 = $("#frmdate").val();
							var Todate1 = $("#tocdate").val();
							var accountHead= $("#accountHead").val();
							var fieldId=$("#fieldId").val();
							
						//	alert("Transacdate"+EmployeeListid1+EmployeeListid1)
							var theForm = '#frmBillRegisterReport';
		                    var requestData = __serializeForm(theForm);
							var url ="BillRegister.html?PrintBillRegisterReport";
							 requestData = {
									"Fromdate" : Fromdate1,
									"Todate" : Todate1,
									"accountHead":accountHead,
									"fieldId":fieldId,
									"billType":billType,
								};
							//alert(requestData);
							var ajaxResponse = __doAjaxRequest(url, 'POST', requestData, false, 'html');
							 if (ajaxResponse == 'ERROR' && ajaxResponse=="") {
								 var errorList = [];
									
									errorList.push(getLocalMessage("account.norecord.criteria"));
									$("#viewDishonourDetails").hide();
									if(errorList.length>0){		    	
								    	var errorMsg = '<ul>';
								    	$.each(errorList, function(index){
								    		errorMsg +='<li><i class="fa fa-exclamation-circle"></i>&nbsp;' + errorList[index] + '</li>';		    		
								    	});
								    	errorMsg +='</ul>';		    	
								    	$('#errorId').html(errorMsg);
								    	$('#errorDivId').show();
										$('html,body').animate({ scrollTop: 0 }, 'slow');	 
							 }
							 }else{
							$('#content').html(ajaxResponse);
							 }
						}
					},50);
						
						});
					
			
				});


$(document).ready(function() {

	/*$('.hasMyNumber').keyup(function() {
		this.value = this.value.replace(/[^0-9.]/g, '');
		$(this).attr('maxlength', '13');
	});*/
	
	//$('select[id="faYearid"]').val('3');
	//$('select[id="faYearidform"]').val('3');
	
	$(".datepicker").datepicker({
	    dateFormat: 'dd/mm/yy',		
		changeMonth: true,
		changeYear: true,	
		maxDate: '-0d',
	});
	//$(".datepicker").datepicker('setDate', new Date()); 
	
});