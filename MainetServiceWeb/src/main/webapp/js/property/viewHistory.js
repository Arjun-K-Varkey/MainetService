function backToAuthPage(obj) {
	
	var data = {};
	var URL = 'PropertyShowHistory.html?backToAssessmentViewHistory';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}

function viewRoomHistory(count, element) {

	$('#countOfRow').val(count);
	var countOfRow = count;
	var URL = "PropertyShowHistory.html?viewRoomHistory";
	var data = { "countOfRow": countOfRow };
	var returnData = __doAjaxRequest(URL, 'POST', data, false);

	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	var totalAreaval = $("#totalAreaval").val();
	totalAreaval = parseFloat(totalAreaval).toFixed(2);
	$("#totalAreaval").val(totalAreaval);
}