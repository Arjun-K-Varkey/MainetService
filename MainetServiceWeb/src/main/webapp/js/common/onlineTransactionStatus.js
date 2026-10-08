$(document).ready(function(){
	$("#appDate").datepicker({
		dateFormat : 'dd/mm/yy',
		changeMonth : true,
		changeYear : true
	});
	
	$("#onlineTransactionStatus").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
})

function searchTransactionStatus(){
	showloader(true);
	setTimeout(function () {
		$("#errorDiv").hide();
		transactionId = $("#transactionId").val();
		status= $("#status").val();	
		appDate=$("#appDate").val();
		
		var errorList=[];
		if(transactionId == "" && status == "" && appDate == ""){
			errorList.push(getLocalMessage('check.status.search.criteria'));	
		}

		if(errorList.length>0){
			displayErrorsOnPage(errorList);
		}else{
			var divName = '.content-page';
			var requestData = {
					"transactionId":transactionId,
					"status":status,
					"appDate":appDate					
			};
			var ajaxResponse = doAjaxLoading('OnlineTransactionStatus.html?searchTransactionData', requestData, 'html');	
			$(divName).removeClass('ajaxloader');
			$(divName).html(ajaxResponse);
			prepareTags();
		}
		showloader(false);
	},2000);
}

function ResetForm() {
	window.location.reload("#onlineTransaction")
}