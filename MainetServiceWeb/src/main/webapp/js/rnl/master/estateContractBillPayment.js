$(document)
.ready(
		function(e) {
			var payAmt = $("#payAmount").val();
			if(payAmt == "" || payAmt==0.0){
				$('#submitBT').prop('disabled', true);
			}
			// code update ISRAT-01-OCT
			var totalPayable = Number($("#totalPayable").val());
			$("#payAmount").keyup(
					function() {
						let payAmount = Number($("#payAmount").val());
						if (totalPayable == 0
								|| totalPayable == 'undefined'
									|| payAmount <= 0) {
							$('#submitBT').prop('disabled', true);
						} else {
							$('#submitBT').prop('disabled', false);
						}
					});


			$("#amountToPay").val($("#payAmount").val());

			$("#searchStore" +
			"").dataTable({
				"oLanguage" : {
					"sSearch" : ""
				},
				"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
				"iDisplayLength" : 5,
				"bInfo" : true,
				"lengthChange" : true
			});


			/*//Old Bill Payment Search Function
					$("#btnsearch")
							.click(
									function() {
										// T#37721
										var contractNo = $("#contractNo").val();
										let propertyContractNo = $(
												"#propertyNo").val();
										if ((contractNo != 0 && contractNo != '') || (propertyContractNo != 0 && propertyContractNo != '')) {

											let requestData = {
												"contNo" : contractNo,
												"propertyContractNo" : propertyContractNo
											}
											var ajaxResponse = __doAjaxRequest(
													'EstateContractBillPayment.html?serachBillPayment',
													'POST', requestData, false,
													'html');
											if (ajaxResponse != false) {
												$('.content-page').html(
														ajaxResponse);
												getPropertyDetail();
												prepareTags();
											}
										} else {
											let errorList = [];
											errorList
													.push(getLocalMessage("rnl.bill.pay.search.record"));
											displayErrorsOnPage(errorList);
										}
										tableTextRoundOff();
									});*/

			//New Bill Payment Search Function as per TSCL #220467
			$("#btnsearch") .click( function() {
				showloader(true);
				setTimeout(function() {
					var contractNo = $("#contractNo").val();
					let propertyContractNo = $("#propertyNo").val();
					let mobileNo = $('#mobileNo').val();
					let estateName = $('#estateName').val();
                   
					let errorList = [];
					var counterScheduleFlag = $("#counterScheduleFlag").val();
					if (counterScheduleFlag == 'Y'){
						errorList.push(getLocalMessage("Counter.ScheduledValidation"));
					}
					if (errorList.length == 0 && (contractNo != '' || propertyContractNo != '')) {

						let requestData = {
								"contNo" : contractNo,
								"propertyContractNo" : propertyContractNo
								/*"estateName" : estateName,
								"mobileNo" : mobileNo*/
						}
						/*var ajaxResponse = __doAjaxRequest(
								'EstateContractBillPayment.html?getBillDetailbyContAndMobile',
								'POST', requestData, false,
						'html');*/
						
						var ajaxResponse = __doAjaxRequest(
								'EstateContractBillPayment.html?serachBillPayment',
								'POST', requestData, false,
						'html');
						
						
						
						if (ajaxResponse != false) {
							$('.content-page').html(
									ajaxResponse);
							getPropertyDetail();
							prepareTags();
						}
					} else {
						if (counterScheduleFlag != 'Y') {
						errorList.push(getLocalMessage("rnl.bill.pay.search.record"));
						}
						displayErrorsOnPage(errorList);
					}
					tableTextRoundOff();
					showloader(false);
				}, 200);
			});
			
			var counterScheduleFlag  = $("#counterScheduleFlag").val();
			var errorList = [];
			if (counterScheduleFlag == 'Y'){
				errorList.push(getLocalMessage("Counter.ScheduledValidation"));
				displayErrorsOnPage(errorList);
				return false;		
		  }
		});

function tableTextRoundOff() {
	var tableColText = $( "#second table tbody tr td p" );
	tableColText.each( function() {
		var value = $(this).text();
		value = Number(Math.round(value * 100) / 100).toFixed(2); //Round to 2 decimal places
		$(this).text(value);
	});
}

function receiptAmountRoundOff() {
	var payAmount = $('#payAmount').val();
	payAmount = (Math.round(payAmount * 100) / 100).toFixed(2); //Round to 2 decimal places
	$('#payAmount').val(payAmount);
}

function resetbillPayment(element) {
	$("#contractBillPayment").submit();
}

function saveData(element) {
	// check reciept amt and receivable amt equal or not
	let amt = Number($('#payAmount').val());
	if (amt <= 0) {
		showErrormsgboxTitle(getLocalMessage("rnl.receiptAmt.invalid"));
	}/*
	 * else if(Number($('#payAmount').val()) !=
	 * Number($("#totalPayable").val())){
	 * showErrormsgboxTitle(getLocalMessage("receipt amt can't be more than
	 * receivable amt ")); return false; }
	 */
	$("#errorDiv").hide();
	if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
	":checked").val() == 'N'
		|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
		":checked").val() == 'P') {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'EstateContractBillPayment.html?PrintReport', 'saveform');
	} else {
		return saveOrUpdateForm(element, "Bill Payment done successfully!",
				'EstateContractBillPayment.html', 'saveform');
	}
}

//T#86583
function getTaxCharges(element) {
	var errorList = [];
	let inputAmountId = Number($("#inputAmountId").val());
	if (inputAmountId == 0
			|| inputAmountId == 'undefined'
				|| inputAmountId <= 0) {
		$('#submitBT').prop('disabled', true);
	} else {
		//hit for get taxable charge
		var divName=".content-page"
			var URL = 'EstateContractBillPayment.html?getTaxCharges';
		var formName = findClosestElementId(element, 'form');
		var theForm = '#' + formName;
		var requestData = {};
		requestData = __serializeForm(theForm);

		var returnData = __doAjaxRequest(URL, 'Post', requestData, false,'html');

		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		prepareTags();
		tableTextRoundOff();
		receiptAmountRoundOff();
	}


}

//Old getPropertyDetail
/*function getPropertyDetail(element){
	var optionsAsString = '';
	var contractNo = $("#contractNo").val();
	$('#propertyNo').html('');
	if (contractNo != '' ) {
		let requestData = {
				"contNo" : contractNo,

			}
			var ajaxResponse = __doAjaxRequest(
					'EstateContractBillPayment.html?getPorpertyDetail',
					'POST', requestData, false,
					'json');

		for (var i = 0; i < ajaxResponse.length; i++) {
			optionsAsString += "<option value='" + ajaxResponse[i][5]+ "'>" +ajaxResponse[i][3]+' - ' + ajaxResponse[i][4] +"</option>";
		}
			if (ajaxResponse != false) {

				$("#propertyNo").append(optionsAsString);


			}
			$("#propertyNo").trigger("chosen:updated");
	}
}*/

function getEstateDetail(element){

	var optionsAsString = '';
	var contractNo = $("#contractNo").val();
	$('#estateName').html('');
	if (contractNo != 0 ) {
		let requestData = {
				"contNo" : contractNo,

		}
		var ajaxResponse = __doAjaxRequest(
				'EstateContractBillPayment.html?getEstateName',
				'POST', requestData, false,
		'json');

		for (var i = 0; i < ajaxResponse.length; i++) {
			optionsAsString += "<option value='" + ajaxResponse[i][0]+ "'>" +ajaxResponse[i][0]+' - ' + ajaxResponse[i][1] +"</option>";
		}
		if (ajaxResponse != false) {				
			$("#estateName").append(optionsAsString);			
		}
		$("#estateName").trigger("chosen:updated");
	}
}

//getPropertyDetail Function as per TSCL #220467
function getPropertyDetail(element){

	var optionsAsString = '';
	var contractNo = $("#contractNo").val();
	var estateName = $("#estateName").val();
	var selectvar = getLocalMessage('rnl.master.select');
	$('#propertyNo').html('');
	if (contractNo != 0  || estateName != '') {
		let requestData = {
				"contNo" : contractNo,
				"estateName" : estateName
		}
		var ajaxResponse = __doAjaxRequest(
				'EstateContractBillPayment.html?getPropertyDetails',
				'POST', requestData, false,
		'json');
		if(ajaxResponse.length == 1){
			optionsAsString += "<option value='" + ajaxResponse[0][3]+ "'>" +ajaxResponse[0][3]+' - ' + ajaxResponse[0][4] +"</option>";
		}else{
			optionsAsString += "<option value=''>"+selectvar+"</option>";
			for (var i = 0; i < ajaxResponse.length; i++) {
				optionsAsString += "<option value='" + ajaxResponse[i][3]+ "'>" +ajaxResponse[i][3]+' - ' + ajaxResponse[i][4] +"</option>";
			}
		}
		if (ajaxResponse != false) {	
			$("#propertyNo").append(optionsAsString);
		}
		$("#propertyNo").trigger("chosen:updated");
	}
}

function getContractDetail(element){
	
	var optionsAsString = '';
	var propertyContractNo = $("#propertyNo").val();
	var selectvar = getLocalMessage('rnl.master.select');
	$('#contractNo').html('');
	
	if (propertyContractNo != 0  || propertyContractNo != ''){
		
		let requestData = {
				"propertyContractNo" : propertyContractNo
		}
		
		var ajaxResponse = __doAjaxRequest(
				'EstateContractBillPayment.html?getPropertyContractDetails',
				'POST', requestData, false,
		'json');
		
		if(ajaxResponse.length == 1){
			optionsAsString += "<option value='" + ajaxResponse[0][0]+ "'>" +ajaxResponse[0][0]+' - ' + ajaxResponse[0][1] +"</option>";
		}
		
		if (ajaxResponse != false) {	
			$("#contractNo").append(optionsAsString);
		}
		
		$("#contractNo").trigger("chosen:updated");
		
		
	}
	
}

function getBillDetailforAction(contNo,propNo) {
	let contractNo = contNo;
	let propertyContractNo = propNo;

	if (contractNo != '' || propertyContractNo != '') {

		let requestData = {
				"contNo" : contractNo,
				"propertyContractNo" : propertyContractNo

		}
		var ajaxResponse = __doAjaxRequest(
				'EstateContractBillPayment.html?serachBillPayment',
				'POST', requestData, false,
		'html');

		if (ajaxResponse != false) {
			$(".content-page").html(
					ajaxResponse);
			getPropertyDetail();
			getEstateDetail();
			prepareTags();
		}
	}
	else {
		let errorList = [];
		errorList
		.push(getLocalMessage("rnl.bill.pay.search.record"));
		displayErrorsOnPage(errorList);
	}
	tableTextRoundOff();
}



function searchTdsDetails() {
	
	var contNo = $("#rnlTds").val();

	var data = {

		"contNo" : contNo
		

	};
	var divName = '.content-page';
	var formUrl = "EstateContractBillPayment.html?submitTdsChallan";
	var ajaxResponse = doAjaxLoading(formUrl, data, 'html', divName);
	$('.content').removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}