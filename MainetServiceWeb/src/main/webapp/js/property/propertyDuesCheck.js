$(document).ready(function() {
	$("#showFlatNo").hide();
	var billMethod = $("#billingMethod").val();
	if('I' == billMethod){
		$("#showFlatNo").show();
	}
	$("#propDuesCheck").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	
});

/*$(function() {
	$("#grid").jqGrid({
		url:"PropertyDuesCheck.html?SEARCH_GRID_RESULTS",
		datatype: "json",
        mtype: "POST",
        colNames: [getLocalMessage('property.PropertyNo'), getLocalMessage('property.FlatNo'),getLocalMessage('property.OwnerName'),getLocalMessage('property.occupierName'),
        	getLocalMessage('property.mobile'),getLocalMessage('property.Address'),getLocalMessage('property.totalDuesAmt'),getLocalMessage('view.dues')],
        colModel: [            
            { name: "proertyNo", width: 40, sortable: false,search:true, align: 'center !important' },
            { name: "flatNo", width: 40, sortable: false,search:true, align: 'center !important' },
            { name: "ownerName", width: 40, sortable: false,search:true, align: 'center !important' },
            { name: "occupierName", width: 40, sortable: false,search:true, align: 'center !important' },
            { name: "mobileno", width: 40, sortable: false,search:true, align: 'center !important' },
            { name: 'address', width: 40, sortable: false,search:false, align: 'center !important' },
            { name: 'outstandingAmt', width: 40, sortable: false,search:false, align: 'center !important' },
            { name: 'rowId', index: 'rowId', width: 35, align: 'center !important', sortable: false,formatter:actionFormatter,search:false}
        ],
        pager: "#pagered",
        rowNum: 30,
        rowList: [5, 10, 20, 30],
        sortname: "rowId",
        sortorder: "desc",
        height:'auto',
        viewrecords: true,
        gridview: true,
        loadonce: true,
        jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records", 
            repeatitems: false,
           }, 
        autoencode: true,
        caption: getLocalMessage('property.duesCheck.details')
	});
	 jQuery("#grid").jqGrid('navGrid', '#pagered', {edit : false,add : false,del : false,refresh : false});
		$("#pagered_left").css("width", "");
});

function actionFormatter(cellvalue, options, rowObject){
	return "<a class='btn btn-blue-3 btn-sm' title='View' onclick=\"viewProperty('"+rowObject.rowId+"')\"><i class='fa fa-eye' aria-hidden='true'></i></a> ";
}*/

function viewProperty(rowId) {
showloader(true);
setTimeout(function () {
	var divName = ".widget-content";
	var requestData = {
			"rowId" : rowId
    };
	var ajaxResponse = __doAjaxRequestForSave('PropertyDuesCheck.html?edit', 'post', requestData, false,
		    '', '');
	var errMsg = ajaxResponse["errMsg"];
    if (errMsg != '' && errMsg != undefined) {
	var errorList = [];
	errorList.push(errMsg);
	showErr(errorList);
    } else {
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	$(divName).show();
    }
 showloader(false);
	 },2000);
}

function redirectToSearchPropNo(obj) {
	var errorList = [];
    var propNo = $("#proertyNo").val();
    
    if (propNo!="" && !validatePropNo( $("#proertyNo").val())) {
		errorList.push(getLocalMessage("property.duesCheck.valid.propNo"));
	}
    
    if (errorList.length==0){
	if(propNo != "" || propNo != null || propNo != undefined){
		var requestData = {
				"propNo" : propNo
			    };
		var ajaxResponse = doAjaxLoading(
			    'PropertyDuesCheck.html?getBillingMethod',
			    requestData, 'html');
		if (ajaxResponse != null && ajaxResponse != "") {
	    	var prePopulate = JSON.parse(ajaxResponse);

	    	$.each(prePopulate, function(index, value) {
	    		$('#flatNo').append(
	    				$("<option></option>").attr("value", value).text(
	    						(value)));
	    	});
	    	$('#flatNo').trigger("chosen:updated");
	    	  $("#showFlatNo").show();
	    	  $("#billingMethod").val('I');
	    }
	}
	}
    else{
    	displayErrorsOnPage(errorList);
    }
}
function searchPropertyDetail(element) {
	$(element).prop('disabled', true);

	var errorList = [];
	var propNo = $("#proertyNo").val();
	var ownerName = $("#ownerName").val();
	var occupierName = $("#occupierName").val();
	var address = $("#address").val();
	var assWard1 = $("#assWard1").val();
	var assWard2= $("#assWard2").val();

	if(propNo == "" && ownerName == "" && occupierName == ""  && address== ""  && assWard1 == "0" && assWard2 == "0" ){
		errorList.push(getLocalMessage("property.select.atleast.oneField.criteria"));
	}
	if (propNo != "" && !validatePropNo($("#proertyNo").val())) {
		errorList.push(getLocalMessage("property.duesCheck.valid.propNo"));
	}
	if (errorList.length == 0) {
		var data = $('#PropertyDuesCheck').serialize();
		var URL = 'PropertyDuesCheck.html?searchData';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		// openPopupWithResponse(returnData);
		showloader(true);
		setTimeout(function () {
		$(formDivName).html(returnData);
		$(element).prop('disabled',false);
		//reloadGrid('gridPropertyDuesCheck');
		showloader(false);
		},2000);
		// reloadGrid('gridPropertyDuesCheck');
	} else {
		$(element).prop('disabled', false);
		displayErrorsOnPage(errorList);
	}

}

function resetForm(element) {
	$('input[type=text]').val('');
	$(".alert-danger").hide();
	$('#flatNo').val('').trigger('chosen:updated');
	$("#PropertyDuesCheck").submit();
}

function validatePropNo(propNo) {
	var regexPattern = /^([a-zA-Z0-9 ]){12,20}$/;
	return regexPattern.test(propNo);
}


function backToPropDetailPage() {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading("ViewPropertyDetail.html?backToPropDetailPage", {},
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();

}

function backToPropDetails(){
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading("PropertyDuesCheck.html?backToMainPage", {},
	'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function propertyBillPrint(element) {

	$(element).prop('disabled',true);
	var data = $('#PropertyDuesCheckView').serialize();
	var URL = 'PropertyDuesCheck.html?viewPropertyLedgerReport';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	window.open(returnData, '_blank');

}