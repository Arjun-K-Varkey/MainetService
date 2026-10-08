
$(document).ready(function() {

	$("#dramaProducerTableId").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});

});

function formForCreate() {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading('DramaProducerMasterController.html?formForCreate',
			{}, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse)
	$('.error-div').hide();
}

function saveDramaMastForm(obj){

	var errorList = [];
	errorList = validateDramaProducerData(errorList);

	if (errorList.length == 0) {
		return saveOrUpdateForm(obj, "Drama Producer Master Data Saved Successfully!", 'DramaProducerMasterController.html', 'saveform');
	} else {
		displayErrorsOnPage(errorList);
	}
}

function validateDramaProducerData(errorList) {

	var dramaCompanyName = $("#dramaCompanyName").val();
	var dramaCompanyAddress = $("#dramaCompanyAddress").val();
	var dramaCompanyContNo = $("#dramaCompanyContNo").val();
	var dramaCompanyEmail = $("#dramaCompanyEmail").val();
	var producerName = $("#producerName").val();
	var producerAddress = $("#producerAddress").val();
	var producerMobNo = $("#producerMobNo").val();
	var producerEmail = $("#producerEmail").val();
	var regAuthorityName = $("#regAuthorityName").val();

	if(dramaCompanyName == undefined || dramaCompanyName == null || dramaCompanyName == "")
		errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyName"));

	if(dramaCompanyAddress == undefined || dramaCompanyAddress == null || dramaCompanyAddress == "")
		errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyAddress"));

	if(dramaCompanyContNo == undefined || dramaCompanyContNo == null || dramaCompanyContNo == "")
		errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyContNo"));
	else{
		if(dramaCompanyContNo.length < 10)
			errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyContNo.valid"));
		else{
			var regexPattern = /^[789]\d{9}$/;
			var valid = regexPattern.test(dramaCompanyContNo);
			if (!valid) {
				errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyContNo.valid"));
			}
		}
	}

	if(dramaCompanyEmail!=""){		

		var emailRegex = new RegExp(
				/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,'');
		var valid = emailRegex.test(dramaCompanyEmail);
		if (!valid) {
			errorList.push(getLocalMessage("rnl.drama.validation.dramaCompanyEmail.valid"));
		}		

	}

	if(producerName == undefined || producerName == null || producerName == "")
		errorList.push(getLocalMessage("rnl.drama.validation.producerName"));

	if(producerAddress == undefined || producerAddress == null || producerAddress == "")
		errorList.push(getLocalMessage("rnl.drama.validation.producerAddress"));

	if(producerMobNo == undefined || producerMobNo == null || producerMobNo == "")
		errorList.push(getLocalMessage("rnl.drama.validation.producerMobNo"));
	else{
		if(producerMobNo.length < 10)
			errorList.push(getLocalMessage("rnl.drama.validation.producerMobNo.valid"));
		else{
			var regexPattern = /^[789]\d{9}$/;
			var valid = regexPattern.test(producerMobNo);
			if (!valid) {
				errorList.push(getLocalMessage("rnl.drama.validation.producerMobNo.valid"));
			}
		}
	}

	if(producerEmail == undefined || producerEmail == null || producerEmail == "")
		errorList.push(getLocalMessage("rnl.drama.validation.producerEmail"));
	else{

		var emailRegex = new RegExp(
				/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,'');
		var valid = emailRegex.test(producerEmail);
		if (!valid) {
			errorList.push(getLocalMessage("rnl.drama.validation.producerEmail.valid"));
		}

	}

	if(regAuthorityName == undefined || regAuthorityName == null || regAuthorityName == "")
		errorList.push(getLocalMessage("rnl.drama.validation.regAuthorityName"));

	return errorList;

}

function modifyCase(dramaProdId, formUrl, actionParam, mode) {
	var divName = '.content-page';
	var requestData = {
			"mode" : mode,
			"dramaProdId" : dramaProdId
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function searchDramaRecord(obj) {
	var errorList = [];
	var divName = '.content-page';
	var dramaProdId = $("#dramaProdId").val();

	if(dramaProdId == undefined || dramaProdId == null || dramaProdId == '0')
		errorList.push(getLocalMessage("rnl.drama.select.drama.comp.name"));

	if (errorList.length == 0) {
		
		var requestData = {
				"dramaProdId" : dramaProdId	
		};
		var ajaxResponse = doAjaxLoading('DramaProducerMasterController.html?searchDramaRecord', requestData, 'html');
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
		prepareTags();
		
	} else {
		displayErrorsOnPage(errorList);
	}
}
