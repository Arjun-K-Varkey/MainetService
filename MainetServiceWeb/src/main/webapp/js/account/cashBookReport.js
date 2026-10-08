
function cashBookReport(formUrl, actionParam) {
	showloader(true);
	setTimeout(function(){
	   var errorList = [];
	   errorList = validateForm(errorList);
	   
	   var fromDateId = $('#fromDateId').val();
	   var toDateId=$("#toDateId").val();
	   var fieldCode2=$("#fieldCode2").val();
	   
	   
		if (errorList.length > 0) {
			displayErrorsPage(errorList);
		} else if (errorList.length == 0) {
	      var data = {
				 "fromDateId" : fromDateId,
				 "toDateId"   :toDateId,
				 "fieldCode2" :fieldCode2
			};
		    var divName = '.content-page';
			var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam,data,'html');
			$(divName).removeClass('ajaxloader');
			$(divName).html(ajaxResponse);
			prepareTags();
		}
	},50);
}
function validateForm(errorList) {
	
	var fromDateId = $('#fromDateId').val();
	var toDateId = $("#toDateId").val();
	if (fromDateId == "" || fromDateId == null) {
		errorList.push(getLocalMessage('account.select.fromDate'));
	}

	if (toDateId == "" || toDateId == null) {
		errorList.push(getLocalMessage('account.select.toDate'));
	}
	if (errorList.length == 0) {
		var fromDate = moment(fromDateId, "DD.MM.YYYY HH.mm").toDate();

		var toDate = moment(toDateId, "DD.MM.YYYY HH.mm").toDate();

		if (fromDate != null && toDate != null) {

			if (toDate.getTime() < fromDate.getTime()) {

				errorList
						.push(getLocalMessage("acc.fdate.tdate.val"));

			}
		}
	}
	return errorList;
}

function displayErrorsPage(errorList) {
	if (errorList.length > 0) {
		var errMsg = '<ul>';
		$.each(errorList, function(index) {
			errMsg += '<li> <i class="fa fa-exclamation-circle"></i>&nbsp;'+ errorList[index] + '</li>';
		});
		errMsg += '</ul>';
		$('#errorId').html(errMsg);
		$('#errorDivId').show();
		$('html,body').animate({
			scrollTop : 0
		}, 'slow');
		return false;
	}
}

/** **********Start Back Button Report Function*********** */
function back() {
	
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', 'cashBookReport.html');
	$("#postMethodForm").submit();
}
/** ********************************************************** */

function cashBookReset() {
	//debugger;
	$("#postMethodForm").prop('action', '');
	$("#fromDateId").val("").trigger("chosen:updated");
	$("#toDateId").val("").trigger("chosen:updated");
	$('.error-div').hide();
	prepareTags();
}