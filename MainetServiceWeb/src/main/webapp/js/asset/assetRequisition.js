$(document).ready(function() {
	
	$("#requisitionSummaryDT").dataTable(
			{
				"oLanguage" : {
					"sSearch" : ""
				},
				"aLengthMenu" : [ [ 5, 10, 15, -1 ],
						[ 5, 10, 15, "All" ] ],
				"iDisplayLength" : 5,
				"bInfo" : true,
				"lengthChange" : true
			});
	
	$("#searchRequisition").click(function() {
				var errorList = [];
				var astCategory = $("#astCategory").val();
				var astLoc = $("#astLoc").val();
				var astDept = $("#astDept").val();

				if (astCategory != 0 || astLoc != ''||  astDept != '') {

					var requestData = '&astCategoryId=' + astCategory + '&locId=' + astLoc + '&deptId=' +astDept;
					var table = $('#requisitionSummaryDT').DataTable();
					table.rows().remove().draw();
					$(".warning-div").hide();
					var ajaxResponse = __doAjaxRequest('AssetRequisition.html?searchReqData','POST', requestData, false,'json');
					if (ajaxResponse.length == 0) {
						errorList.push(getLocalMessage("asset.search.dataNotFound"));
						displayErrorsOnPage(errorList);
						return false;
					}

					var result = [];
					$.each(
									ajaxResponse,
									function(index) {
									    var obj = ajaxResponse[index];
										let astCategoryDesc = obj.astCategoryDesc;
										let astName = obj.astName;
										let astDesc = obj.astDesc;
										let astQty = obj.astQty;
										let astLocDesc = obj.astLocDesc;
										let astDeptDesc = obj.astDeptDesc;
										let status = obj.status;
										result
												.push([
														'<div class="text-center">'
																+ (index + 1)
																+ '</div>',
														'<div class="text-center">'
																+ astCategoryDesc
																+ '</div>',
														'<div class="text-center">'
																+ astName
																+ '</div>',
														'<div class="text-center">'
																+ astDesc
																+ '</div>',
														'<div class="text-center">'
																+ astQty
																+ '</div>',
														'<div class="text-center">'
																+ astLocDesc
																+ '</div>',
														'<div class="text-center">'
																+ astDeptDesc
																+ '</div>',
														'<div class="text-center">'
																+ status
																+ '</div>']);
									});
					table.rows.add(result);
					table.draw();
				} else {
					errorList
							.push(getLocalMessage("land.acq.val.selectAtleastOneField"));
					displayErrorsOnPage(errorList);
				}

			});
	
	/*Reset Form*/
	$("#resetForm").click(function(){
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading('AssetRequisition.html?addRequisition', {}, 'html',divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	});

});

function openRequisitionForm(formUrl, actionParam) {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, {}, 'html',divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function saveRequisition(element) {
	var errorList = [];
	$("#errorDiv").html('');
	let astCategory = $('#astCategory').val();
	let astName = $("#astName").val();
	let astDesc = $("#astDesc").val();
	let astQty = $("#astQty").val();
	let astLoc = $("#astLoc").val();
	let astDept = $("#astDept").val();

	if (astCategory == "0" || astCategory == undefined || astCategory == '') 
		errorList.push(getLocalMessage('asset.requisition.vldnn.astCategory'));
	if (astName == undefined || astName == '')
		errorList.push(getLocalMessage('asset.requisition.vldnn.name'));
	if (astDesc == undefined || astDesc == '')
		errorList.push(getLocalMessage('asset.requisition.vldnn.description'));
	if (astQty == undefined || astQty == '')
		errorList.push(getLocalMessage('asset.requisition.vldnn.quantity'));
	if (astLoc == "0" || astLoc == undefined || astLoc == '')
		errorList.push(getLocalMessage('asset.requisition.vldnn.location'));
	if (astDept == "0" || astDept == undefined || astDept == '')
		errorList.push(getLocalMessage('asset.requisition.vldnn.department'));

	if (errorList.length > 0) {
		$("#errorDiv").show();
		displayErrorsOnPage(errorList);
		return false;
	} else {
		$("#errorDiv").hide();
		return saveOrUpdateForm(element, getLocalMessage('asset.requisition.savesuccessmsg'), ''+$("#moduleDeptUrl").val()+'', 'saveform');
	}
}

function saveDecisionAction(approvalData){
	var errorList = [];
	var decision = $("input[id='decision']:checked").val();
	var comments = document.getElementById("comments").value;
	
	if(decision == undefined || decision == '') {
		errorList.push(getLocalMessage('asset.info.approval'));
	} else if(comments == undefined || comments =='') {
		errorList.push(getLocalMessage('asset.info.comment'));
	}
	
	if (errorList.length > 0) {
		$("#errorDiv").show();
		displayErrorsOnPage(errorList);
	} else {
		$("#errorDiv").hide();
		return saveOrUpdateForm(approvalData, '', 'AdminHome.html', 'saveDecision');
	}

}
	

