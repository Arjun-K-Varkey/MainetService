$(document)
		.ready(
				function() {
				
					$('#PrintButn').click(function() {
						$('.error-div').hide();
						var errorList = [];
			
						var Fromdate = $("#frmdate").val();
						var dpDeptid = $("#dpDeptid").val();
					    var deptName = $('#dpDeptid option:selected').text();
					    var accountHeadId = $("#accountHead").val();
					    var allAccountHeads = $('#accountHead option:selected').text();
					    var billType= $("#billType").val();
						var fieldId = $("#fieldId").val();
					    
						
						if (Fromdate == "") {
							errorList.push(getLocalMessage("account.select.as.on.date"));
						}
						if (dpDeptid == ""|| dpDeptid==null) {
							errorList.push(getLocalMessage("receivable.demand.entry.validation.deptName"));
						}
						if(accountHeadId=="" || accountHeadId==null){
							errorList.push(getLocalMessage("account.select.acchead"));
						}
						if (billType == ""|| billType==null) {
							errorList.push(getLocalMessage("account.billtype"));
						}
						if(fieldId=="" || fieldId==null){
							errorList.push(getLocalMessage("account.field.id"));
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
							var dpDeptid = $("#dpDeptid").val();
							 var deptName = $('#dpDeptid option:selected').text();
							var theForm = '#frmBillRegisterReport';
		                    var requestData = __serializeForm(theForm);
							var url ="OutstandingBillRegister.html?OutStdBillRegisterReport";
							 requestData = {
									"Fromdate" : Fromdate1,
									"dpDeptid" : dpDeptid,
									"deptName" :deptName,
									"accountHeadId":accountHeadId,
									"allAccountHeads":allAccountHeads,
									"fieldId":fieldId,
									"billType":billType,
								};
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
						
						});
					
			
				});


$(document).ready(function() {
	$("#frmdate").keyup(function(e){
	    if (e.keyCode != 8){    
	        if ($(this).val().length == 2){
	            $(this).val($(this).val() + "/");
	        }else if ($(this).val().length == 5){
	            $(this).val($(this).val() + "/");
	        }
	     }
	    });
	$(".datepicker").datepicker({
	    dateFormat: 'dd/mm/yy',		
		changeMonth: true,
		changeYear: true,	
		maxDate: '-0d',
	});
});