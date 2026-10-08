$(document).ready(function() {
$("#advertiserTable").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});

let totalPay= Number($('#payableAmount').text());
$('#payableAmount').text(totalPay.toFixed(2));
});


function searchWaterDetail(element) {
showloader(true);
setTimeout(function () {
	var errorList = [];
	
	var connNo = $("#connectionNo").val();
	var csNme = $("#csFirstName").val();
	
	if (connNo=="") {
		errorList.push(getLocalMessage("water.duesCheck.valid.conNo"));
		 displayErrorsOnPage(errorList);
		 return false;
	}
	

	var div = $("#codDwzid1") != null && $("#codDwzid1").val() != '0' ? $("#codDwzid1").val() : ''; 
	var ward = $("#codDwzid2") != null && $("#codDwzid2").val() != '0' ? $("#codDwzid2").val() : ''; 
	var zone = $("#codDwzid3") != null && $("#codDwzid3").val() != '0' ? $("#codDwzid3").val() : ''; 
	


	if (connNo != '' || csNme != '' || div != '' || ward != '' || zone != '') {
		var divName = '.content-page';
		var formName = findClosestElementId(element, 'form');
		var theForm = '#' + formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
		var table = $('#advertiserTable').DataTable();
		table.rows().remove().draw();

		var ajaxResponse = doAjaxLoading(
				'WaterDuesCheck.html?searchWaterDetails', requestData, 'html');

		var prePopulate = JSON.parse(ajaxResponse);
		var result = [];
		$
				.each(
						prePopulate,
						function(index) {
							var obj = prePopulate[index];
							result
									.push([
											obj.csCcn,
											obj.csName,
											obj.ward,
											obj.zone,
											obj.csAdd,
											'<td><i class="fa fa-inr"></i>' +obj.payableAmount+ '</td>',
											'<td >'
													+ '<button type="button"  class="btn btn-blue-2 btn-sm margin-right-5 margin-left-5"  onclick="viewWaterDetail(\''
													+ obj.csCcn + '_' + obj.payableAmount
													+ '\')" title="View"><i class="fa fa-eye"></i></button></td>'
											 ]);
											

						});
		table.rows.add(result);
		table.draw();
		if (prePopulate.length == 0) {
			errorList.push(getLocalMessage("water.norecord.search.criteria"));
			$("#errorDiv").show();
			displayErrorsOnPage(errorList);
		} else {
			$("#errorDiv").hide();
		}
	} else {
		errorList.push(getLocalMessage("water.select.atleast.oneField.criteria2"));
		displayErrorsOnPage(errorList);
	}
	showloader(false);
},2000);
}

function resetWaterDuesForm(element) {
		$("#WaterDuesCheck").submit();
}



function viewWaterDetail(connectionNo) {
showloader(true);
setTimeout(function () {
	var errorList = [];
	var parts = connectionNo.split("_");
    var connNo = parts[0];
    var amt = parts[1];
    /*if (amt == '₹ 0.0'){
    	errorList.push(getLocalMessage("water.duesCheck.noDues"));
		 displayErrorsOnPage(errorList);
		 return false;
    }*/
    if(errorList.length == 0){
	var divName = '.content-page';
	var requestData = {
		"connNo" : connNo
	};

	var ajaxResponse = doAjaxLoading('WaterDuesCheck.html?getWaterDetail',
			requestData, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
    }
    showloader(false);
},2000);
}

function ViewBillDetails(bmid) {

	var data = {
		"bmIdNo" : bmid
	};
	var URL = 'ViewWaterDetails.html?viewWaterDet';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$("#viewWaterDetails").html(returnData);
}

function validateConnNo(connNo) {
	var regexPattern = /^([a-zA-Z0-9 ]){10,20}$/;
	return regexPattern.test(connNo);
}

function backToSearchForm(){
	var divName = '.content-page';
	var connNo = $("#connectionNo").val();
	var requestData={
	"connNo":connNo
	};
	var ajaxResponse = doAjaxLoading(
	'WaterDuesCheck.html?backToSearchWaterDetails', requestData, 'html');
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	$("#csFirstName").val("");
	prepareTags();
}
function waterBillPrint(element){
	var data = {};
	var URL = 'WaterDuesCheck.html?waterBillPrint';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	window.open(returnData, '_blank');
	
}