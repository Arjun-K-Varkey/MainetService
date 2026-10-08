$(document).ready(
		function() {
			
			var mode = $('#viewMode').val();
			
			if(mode == 'A'){
				$('#compList').hide();
			}else{
				$('#compList').show();
			}
			
			
			
			$('.chosen-select-no-results').chosen();
			
			
			
			$("#btnSave").click(function(e) {
				var errorList = [];
				var ApplicationAction = $('#ApplicationAction').val();
				var Remark = $('#Remark').val();
				
				
				

				

				var empId = $("#EmployeeName").val();

				if (ApplicationAction == ''
					|| ApplicationAction == 'Select'
						|| ApplicationAction == undefined) {
					errorList
					.push(getLocalMessage("care.error.applicationAction"));
				}

				if(Remark != undefined && Remark != null)
					Remark = Remark.trim();
				if (Remark == '' || Remark == undefined) {
					errorList.push(getLocalMessage("care.error.remark"));
				}

				

				if (ApplicationAction == 'FORWARD_TO_EMPLOYEE') {
					if (empId === null || empId === '') {

						errorList.push(getLocalMessage("care.error.employee"));
					}

				}

				if (errorList.length == 0) {
					var data = new FormData(document.getElementById("ForwardComplaintForm"));
					$.ajax({	url : "ForwardComplaint.html?saveDetails",
						type : "POST",
						data : data,
						enctype : 'multipart/form-data',
						processData : false,
						contentType : false,
						async : false,
						success : function(response) {
							if (response != null) {
								displayMessageOnSubmit(response);
							}
					},
						error : function(response) {
							displayMessageOnError(response);
						}
					});

				} else {
					displayErrorsOnPage(errorList);
					$('html,body').animate({
						scrollTop : 0
					}, 'slow');
				}
				return true;

			});
			
			

		});

function searchForm(obj) {
	var errorList = [];
	
	var empId = $("#searchEmpId").val();
	var ward = $("#searchWard").val();
	
	var divName = '.content-page';
	if ((empId == "0" || empId == "" || empId == undefined) && (ward == "0" || ward == undefined || ward == "") ) {
		errorList.push(getLocalMessage("master.selectCriteria"));
	}
	if (errorList.length == 0) {
		var requestData = {
				"empId" : empId,
				"ward" : ward
				
		};
		var ajaxResponse = doAjaxLoading('ForwardComplaint.html?searchForm', requestData, 'html');
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
		prepareTags();
	} else {
		displayErrorsOnPage(errorList);
	}
}

function multiSelEmp(){
	  $("#EmployeeName").multiselect({
		  numberDisplayed: 1,
		  enableFiltering: true,
		  includeFilterClearBtn: true,
		    enableCaseInsensitiveFiltering: true,
		  includeSelectAllOption : true,
		  selectAllName : 'select-all-name'});
}

function multiSelComp(){
	  $("#searchComp").multiselect({
		  numberDisplayed: 1,
		  enableFiltering: true,
		  includeFilterClearBtn: true,
		    enableCaseInsensitiveFiltering: true,
		  includeSelectAllOption : true,
		  selectAllName : 'select-all-name'});
}

function displayMessageOnSubmit(Msg) {
	var errMsgDiv = '.msg-dialog-box';
	var message = '';
	/*Defect #115163*/
	/*Error msg added if workflow is not defined for forward_to_department action- WNF(Workflow Not Found)*/
	if(Msg === 'failure'){
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.error")+'</h4>';
	}else if(Msg === 'WNF'){
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.error.wardzone.workflow")+'</h4>';
	}else if(Msg === 'TNF'){
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.workflow.error.taskNotFound")+'</h4>';
	}else if(Msg === 'FORWARD_TO_DEPARTMENT'){
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.submit.msg.forwardtodepartment")+'</h4>';
	}else if(Msg === 'FORWARD_TO_EMPLOYEE'){
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.submit.msg.forwardtoemployee")+'</h4>';
	}else
		message += '<h4 class="text-center text-blue-2 padding-10 padding-bottom-0">'+getLocalMessage("care.report.submit")+'</h4>';
	
	message += '<div class="text-center">'
		+ '<a href="AdminHome.html" class=\'btn btn-blue-2 margin-bottom-10\'>'+getLocalMessage("care.close")+'</a>'
		+ '</div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutClose(errMsgDiv);
}