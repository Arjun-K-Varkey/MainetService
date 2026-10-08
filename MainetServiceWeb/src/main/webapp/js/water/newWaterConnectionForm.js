$(document)
		.ready(
				function() {
					
					$('#frmNewWaterForm').validate({
						onkeyup : function(element) {
							this.element(element);
							console.log('onkeyup fired');
						},
						onfocusout : function(element) {
							this.element(element);
							console.log('onfocusout fired');
						}
					});
					$("#isBhagirathi").hide();
					$("#withBhagiRathi").attr("disabled", true);
					$("#bplNo").addClass("changeParameterClass");
					$(".changeParameterClass").change(function(e) {
						var valueAfterData = $(this).val();
						var id = $(this).attr("id");
						if ($('#free').val() != "O") {
							$('#divSubmit').hide();
							$('#chekListChargeId').show();
							clearCheckListAndCharges(id, valueAfterData);
							// getChecklistAndCharges(this);
						}

					});
					$(".changeParameter").keyup(function(e) {
						var valueAfterData = $(this).val();
						var id = $(this).attr("id");
						if ($('#free').val() != "O") {
							$('#divSubmit').hide();
							$('#chekListChargeId').show();
							clearCheckListAndCharges(id, valueAfterData);
						}

					});

					$(".ownerDetails").hide();
					var dateFields = $('.Moredatepicker');
					dateFields.each(function() {
						var fieldValue = $(this).val();
						if (fieldValue.length > 10) {
							$(this).val(fieldValue.substr(0, 10));
						}
					});

					if ($("#taxPayerFlag").val() == 'Y') {
						$("#pandiv1").show();
					} else {
						$("#pandiv1").hide();
					}

					$("#taxPayerFlag").change(function() {
						
						var chk = $('#taxPayerFlag').val();
						if (chk == "Y") {

							$("#pandiv1").show();
							$("#taxPayerFlag").val("Y");
							$("#panNo").data('rule-required', true);

						} else {
							$("#pandiv1").hide();
							$("#taxPayerFlag").val("N");
							$("#panNo").data('rule-required', false);
						}

					});

					$('.p_element').hide();
					$("#trmGroup1").change(
							function(e) {
								
								if ($('#trmGroup1 option:selected')
										.attr('code') == "H") {
									$('#trans-restaurant').hide();
									$('#trans-hotel').show();
									$('#hotelNumber').prop("disabled", false);
								} else if ($('#trmGroup1 option:selected')
										.attr('code') == "RFC") {
									$('#trans-hotel').hide();
									$('#trans-restaurant').show();
									$('#restaurantNumber').prop("disabled",
											false);
								} else {
									$('#trans-restaurant').hide();
									$('#trans-hotel').hide();
									$('#hotelNumber').prop("disabled", true);
									$('#restaurantNumber').prop("disabled",
											true);
								}

							});

					$("#fromtoperiod").hide();
					$("#numberofday").hide();
					/*
					 * var propchecked = $('#ExistingProperty').is(':checked');
					 * if(propchecked) {
					 * $("#propertyNo").prop("readonly",false).data('rule-required',true); }
					 * else { $("#propertyNo").val("");
					 * $("#propertyOutstanding").val("");
					 * $("#propertyNo").prop("readonly",true).data('rule-required',false); }
					 */
					$("#plumberName").prop("readonly", true);
					$("#chargesDiv").hide();
					if ($("#typeOfApplication").val() == 'T') {

						$("#fromtoperiod").show();
						$("#numberofday").show();

					} else {
						$("#fromdate").val("");
						$("#todate").val("");
						$("#fromtoperiod").hide();
						$("#numberofday").hide();
					}

					if ($('#free').val() == 'Y' || $('#free').val() == 'N') {
						$('#divSubmit').show();
						$('#chekListChargeId').hide();
					}

					if ($('#free').val() == 'O') {
						$('#divSubmit').hide();
						$('#chekListChargeId').show();
					}
					/* $("#consumerDiv").hide(); */
					$("#tbl2").hide();

					if ($('#bplNo').val() == 'N') {
						$("#bpldiv").hide();
					}
					if ($('#bplNo').val() == 'Y') {
						$("#bpldiv").show();
						$("#bplNo").data('rule-required', true);
					} else {
						$("#bpldiv").hide();
						$("#bplNo").data('rule-required', false);
					}
					if ($('#existingNumber').val() == 'Y') {
						$("#ExistingConnection").attr("checked", "checked");
					} /*
						 * else { $("#noOfTaps0").attr("readonly", false); }
						 */
					var checked = $('#ExistingConnection').is(':checked');
					if (checked) {
						/* $("#consumerDiv").show(); */
						$("#tbl2").show();
						$("#consumerNo0").data('rule-required', true);
					} else {
						/* $("#consumerDiv").hide(); */

						$("#tbl2").hide();
						$("#consumerNo0").data('rule-required', false);
					}
					if ($('#applicantType option:selected').attr('code') == "O"
							|| $('#applicantType option:selected').attr('code') == "G"
							|| $('#applicantType option:selected').attr('code') == "TR") {
						$("#OrgName").show();
						$("#orgName").data('rule-required', true);
						if ($('#applicantType option:selected').attr('code') == "O") {
							$("#orgLabel").show();
							$("#grpLabel").hide();
							$("#trustLabel").hide();
						}
						if ($('#applicantType option:selected').attr('code') == "G") {
							$("#grpLabel").show();
							$("#orgLabel").hide();
							$("#trustLabel").hide();
						}
						if ($('#applicantType option:selected').attr('code') == "TR") {
							$("#trustLabel").show();
							$("#grpLabel").hide();
							$("#orgLabel").hide();
						}
					} else {
						$("#orgName").val("");
						$("#OrgName").hide();
						$("#orgName").data('rule-required', false);
					}
					if ($('#applicantType option:selected').attr('code') == "G"
							|| $('#applicantType option:selected').attr('code') == "g") {
						$(".ownerDetails").show();
						$('[id^="ownerTitle"]').data('rule-required', true);
						$('[id^="ownerFName"]').data('rule-required', true);
						$('[id^="ownerLName"]').data('rule-required', true);
						$('[id^="ownerGender"]').data('rule-required', true);
					}

					$("input[name='csmrInfo.csPtype']").click(function() {
						
						if ($(this).attr('value') == 'U') {
							/*
							 * $("#plumberName").val("");
							 * $("#plumberName").prop("readonly", true)
							 * .data('rule-required', false);
							 */
							$("#ulbPlumber").show();
							$("#licensePlumber").hide();
							$("#licPlumber").attr("disabled", true);
							$("#plumber").attr("disabled", false);

						} else if ($(this).attr('value') == 'L') {
							/*
							 * $("#plumberName").prop("readonly", false)
							 * .data('rule-required', true);
							 */
							$("#ulbPlumber").hide();
							$("#licensePlumber").show();
							$("#plumber").attr("disabled", true);
							$("#licPlumber").attr("disabled", false);
						} else {
							/*
							 * $("#plumberName").prop("readonly", false)
							 * .data('rule-required', true);
							 */
						}
					});

					if ($("input[name='csmrInfo.csPtype']").attr('value') == 'U') {
						
						$("#ulbPlumber").show();
						$("#licensePlumber").hide();
						$("#licPlumber").attr("disabled", true);
					} else {
						$("#ulbPlumber").hide();
						$("#licensePlumber").show();
						$("#plumber").attr("disabled", true);
					}

					$("#billing").click(function() {
						var chk = $('#billing').is(':checked');
						if (chk) {
							
							$('#hiddenBillingSame').val("Y");
							
							$("#billingAreaName").val($("#csAddress1").val());
							$("#billingAreaName").data('rule-required', true);

							$("#billingPinCode").val($("#csPinCode").val());
							$("#billingPinCode").data('rule-required', true);
							
							$("#billingMobile").val($("#csMobileNo").val());
							$("#billingMobile").data('rule-required', true);
							
							$("#billingEmail").val($("#csEmailId").val());
							$("#billingEmail").data('rule-required', true);
							
							
							$("#billingAadhar").val($("#cSaadharNo").val());
							$("#billingAadhar").data('rule-required', true);

						} else {
							$('#hiddenBillingSame').val("N")
	
							$("#billingAreaName").val('');
							$("#billingAreaName").data('rule-required', true);

							$("#billingPinCode").val('');
							$("#billingPinCode").data('rule-required', true);
							
							$("#billingMobile").val('');
							$("#billingMobile").data('rule-required', true);
							
							$("#billingEmail").val('');
							$("#billingEmail").data('rule-required', true);
							
							$("#billingAadhar").val('');
							$("#billingAadhar").data('rule-required', true);

						}

					});

					/*
					 * $("#ExistingProperty").click(function() { var checked =
					 * $('#ExistingProperty').is(':checked'); if(checked) {
					 * $("#propertyNo").prop("readonly",false).data('rule-required',true); }
					 * else { $("#propertyNo").val("");
					 * $("#propertyOutstanding").val("");
					 * $("#propertyNo").prop("readonly",true).data('rule-required',false); }
					 * });
					 */

					$("#ExistingConnection").click(function() {
						var checked = $('#ExistingConnection').is(':checked');
						if (checked) {
							$("#consumerNo").prop("readonly", false);
							$("#connectionSize0").attr("readonly", false);
							$("#connectionSize0").val(0);
							//$("#noOfFamilies0").val("");
							//$("#meterNo0").prop("readonly", false);
							$("#noOfTaps0").attr("readonly", false);
							//$("#noOfFamilies0").attr("readonly", false);
							$("#deleteConnection").removeClass("disabled");
							$("#addConnection").removeClass("disabled");
							/* $("#consumerDiv").show(); */
							$("#tbl2").show();
							$("#consumerNo0").data('rule-required', true);
						} else {
							$("#consumerNo0").val("");
							$("#connectionSize0").val("");
							//$("#meterNo0").val("");
							$("#noOfTaps0").val("");
							//$("#noOfFamilies0").val("");
							$("#connectionSize").prop("disabled", false);
							//$("#meterNo").prop("disabled", false);
							$("#noOfTaps").prop("readonly", false);
							$("#consumerNo").prop("readonly", true);
							//$("#noOfFamilies").attr("readonly", true);
							$("#deleteConnection").addClass("disabled");
							$("#addConnection").addClass("disabled");
							var cnt = $('#tbl2 tr').length - 1;
							while (cnt > 1) {
								$('#tbl2 tr:last-child').remove();
								cnt--;
							}
							/* $("#consumerDiv").hide(); */
							$("#tbl2").hide();
							$("#consumerNo0").data('rule-required', false);
						}
					});
					$("#typeOfApplication").change(function(e) {

						if ($("#typeOfApplication").val() == 'T') {
							$("#fromtoperiod").show();
							$("#numberofday").show();
							$("#fromdate").data('rule-required', true);
							$("#todate").data('rule-required', true);
						} else {
							$("#fromdate").val("");
							$("#todate").val("");
							$("#fromtoperiod").hide();
							$("#numberofday").hide();
							$("#fromdate").data('rule-required', false);
							$("#todate").data('rule-required', false);
						}
					});

					$("#bplNo").change(function(e) {
						

						if ($("#bplNo").val() == 'Y') {
							$("#bpldiv").show();
							$("#bplNumber").val('');
							$("#bplNo").data('rule-required', true);
						} else {
							$("#bpldiv").hide();
							$("#bplNo").data('rule-required', false);
						}
					});

					$("#applicantType").change(
							function(e) {
								

								if ($('#applicantType option:selected').attr(
										'code') == "O"
										|| $('#applicantType option:selected')
												.attr('code') == "G"
										|| $('#applicantType option:selected')
												.attr('code') == "TR") {
									$("#OrgName").show();
									$("#orgName").data('rule-required', true);

									if ($('#applicantType option:selected')
											.attr('code') == "O") {
										$("#orgLabel").show();
										$("#grpLabel").hide();
										$("#trustLabel").hide();
									}
									if ($('#applicantType option:selected')
											.attr('code') == "G") {
										$("#orgLabel").hide();
										$("#grpLabel").show();
										$("#trustLabel").hide();
									}
									if ($('#applicantType option:selected')
											.attr('code') == "TR") {
										$("#orgLabel").hide();
										$("#grpLabel").hide();
										$("#trustLabel").show();
									}
								} else {
									$("#orgName").val("");
									$("#OrgName").hide();
									$("#orgName").data('rule-required', false);
								}
								if ($('#applicantType option:selected').attr(
										'code') == "G"
										|| $('#applicantType option:selected')
												.attr('code') == "g") {
									$(".ownerDetails").show();
									$('[id^="ownerTitle"]').data(
											'rule-required', true);
									$('[id^="ownerFName"]').data(
											'rule-required', true);
									$('[id^="ownerLName"]').data(
											'rule-required', true);
									$('[id^="ownerGender"]').data(
											'rule-required', true);
								} else {
									$(".ownerDetails").hide();
									$('[id^="ownerTitle"]').data(
											'rule-required', false);
									$('[id^="ownerFName"]').data(
											'rule-required', false);
									$('[id^="ownerLName"]').data(
											'rule-required', false);
									$('[id^="ownerGender"]').data(
											'rule-required', false);
								}
							});

					$(".datepicker").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : true,
						changeYear : true
					});
					$(".Moredatepicker").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : true,
						changeYear : true,
						minDate : '0',
					});
					$(".disDate").datepicker(
							{
								dateFormat : 'dd/mm/yy',
								changeMonth : true,
								changeYear : true,
								minDate : 0,
								onSelect : function(selected) {
									$(".distDate").datepicker("option",
											"minDate", selected)
								}
							});
					$(".distDate").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : true,
						changeYear : true,
						minDate : 0,
					});
					var dateFields = $('.disDate,.distDate');
					dateFields.each(function() {
						var fieldValue = $(this).val();
						if (fieldValue.length > 10) {
							$(this).val(fieldValue.substr(0, 10));
						}
					});

					$("#resetform").click(function() {
						this.form.reset();
						resetWaterForm();
						resetOtherFields();
					});

					var chkConsumer = $('#isConsumer').is(':checked');
					if (chkConsumer) {

						$("#hideConsumerDetails").hide();
						$("#cbillingAreaName").data('rule-required', false);
						$("#cbillingPinCode").data('rule-required', false);
						$("#csName").data('rule-required', false);
						$("#csLname").data('rule-required', false);
						$("#csTitle").data('rule-required', false);

					} else {

						$("#hideConsumerDetails").show();
						$("#cbillingAreaName").data('rule-required', true);
						$("#cbillingPinCode").data('rule-required', true);
						$("#csName").data('rule-required', true);
						$("#csLname").data('rule-required', true);
						$("#csTitle").data('rule-required', true);
					}

					$("#isConsumer").click(
							function() {

								var chkConsumer = $('#isConsumer').is(
										':checked');
								if (chkConsumer) {
									$('#hiddenConsumerSame').val("Y");
									$("#hideConsumerDetails").hide();
									$("#cbillingAreaName").data(
											'rule-required', false);
									$("#cbillingPinCode").data('rule-required',
											false);

								} else {
									$('#hiddenConsumerSame').val("N");
									$("#hideConsumerDetails").show();
									$("#cbillingAreaName").data(
											'rule-required', true);
									$("#cbillingPinCode").data('rule-required',
											true);
								}

							});

				});

function saveWaterForm(element) {
	
	if ($('#accept').is(':checked')) {
		if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(
				":checked").val() == 'N'
				|| $("input:radio[name='offlineDTO.onlineOfflineCheck']")
						.filter(":checked").val() == 'P') {
			return saveOrUpdateForm(
					element,
					"Your application for new Water connection saved successfully!",
					'NewWaterConnectionForm.html?PrintReport',
					'SaveAndViewApplication');
		} else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']")
				.filter(":checked").val() == 'Y') {
			return saveOrUpdateForm(
					element,
					"Your application for new Water connection saved successfully!",
					'NewWaterConnectionForm.html?redirectToPay',
					'SaveAndViewApplication');
		} else {
			return saveOrUpdateForm(
					element,
					"Your application for new Water connection saved successfully!",
					'AdminHome.html', 'SaveAndViewApplication');
		}
	} else {
		showErrormsgboxTitle(getLocalMessage("water.accept.terms.cond"));
	}
}
function showViewFormJsp(element) {
		
		$.fancybox.close();
		var errorList=[];
		var formName = "frmNewWaterForm";
		var theForm = '#' + formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
		var AppId = $.trim($('#AppId').val());
		var URL = 'NewWaterConnectionForm.html?ShowViewForm';
		if (AppId == 0 || AppId == undefined || AppId == null) {
			errorList = validateFormDetails();
		}
		var checkbox = document.getElementById("checkId");
		if(checkbox!=undefined && checkbox.checked==false){
			errorList.push(getLocalMessage('Please accept terms and condition')); 
		}
		if (errorList == 0) {
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
			var divName = '#validationDiv';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			$(divName).show();
			$.fancybox.close();
		} else {
			displayErrorsOnPage(errorList);
		}

	}
function editApplicationForm(element) {
	var requestData = {};
	var URL = 'NewWaterConnectionForm.html?EditApplicationForm';
	var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
	var divName = '#validationDiv';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$(divName).show();
}

function continueForPayment(element) {
	if ($("#onlineOfflinePay").val() == 'N'
			|| $("#onlineOfflinePay").val() == 'P') {
		window.open('NewWaterConnectionForm.html?PrintReport', '_blank');
	} else if ($("#onlineOfflinePay").val() == 'Y') {
		closebox("", "NewWaterConnectionForm.html?redirectToPay");
	}
}

function removeRow(cnt) {

	/* $('#tbl1 tr:#tr'+cnt).remove(); */

	if ($('#tbl1 tr').size() > 2) {
		$("#tr" + cnt).remove();
		reorderOwner();
		cnt--;
	} else {
		var msg = "can not remove";
		showErrormsgboxTitle(msg);
	}
}

function removeConnection(cntConnection) {
	/* $('#tbl2 tr:last-child').remove(); */
	if ($('#tbl2 tr').size() > 2) {

		$("#row" + cntConnection).remove();
		var data = {
			"deletedRow" : cntConnection
		};
		var URL = 'NewWaterConnectionForm.html?deletedLinkCCnRow';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);

		reorderConnection();
		cntConnection--;

	} else {
		var msg = "can not remove";
		showErrormsgboxTitle(msg);
	}

}

function reorderConnection() {
	$('.appendableClass')
			.each(
					function(i) {
						// Ids
						$(this).find("input:text:eq(0)").attr("id",
								"consumerNo" + (i));
						$(this).find("input:text:eq(0)").attr("onblur",
								"checkDuplicateCcn(" + (i) + ")");
						$(this).find("input:text:eq(1)").attr("id",
								"meterNo" + (i));
						$(this).find("select:eq(0)").attr("id",
								"connectionSize" + (i));
						$(this).find("input:text:eq(2)").attr("id",
								"noOfTaps" + (i));
						$(this).find("input:text:eq(3)").attr("id",
								"noOfFamilies" + (i));
						$(this).closest("tr").attr("id", "row" + (i));
						// names
						$(this).find("input:text:eq(0)").attr("name",
								"csmrInfo.linkDetails[" + (i) + "].consumerNo");
						/*$(this).find("input:text:eq(0)").attr("name",
								"csmrInfo.linkDetails[" + (i) + "].consumerNo");*/
						$(this).find("input:text:eq(1)").attr("name",
								"csmrInfo.linkDetails[" + (i) + "].meterNo");
						$(this).find("select:eq(0)").attr(
								"name",
								"csmrInfo.linkDetails[" + (i)
										+ "].connectionSize");
						$(this).find("input:text:eq(2)").attr("name",
								"csmrInfo.linkDetails[" + (i) + "].ccnOutStandingAmt");
						$(this).find("input:text:eq(3)").attr("name",
								"csmrInfo.linkDetails[" + (i) + "].noOfFamilies");
						$(this).find("#deleteConnection").attr("onclick",
								"removeConnection(" + (i) + ")");
					});

}

function reorderOwner() {
	$('.ownerClass')
			.each(
					function(i) {
						// Ids
						$(this).find("input:text:eq(0)").attr("id",
								"ownerFName" + (i));
						$(this).find("input:text:eq(1)").attr("id",
								"ownerMName" + (i));
						$(this).find("input:text:eq(2)").attr("id",
								"ownerLName" + (i));
						$(this).find("input:text:eq(3)").attr("id",
								"ownerUID" + (i));
						$(this).find("select:eq(0)").attr("id",
								"ownerTitle" + (i));
						$(this).find("select:eq(1)").attr("id",
								"ownerGender" + (i));
						$(this).closest("tr").attr("id", "tr" + (i));
						// names
						$(this).find("input:text:eq(0)").attr(
								"name",
								"csmrInfo.ownerList[" + (i)
										+ "].ownerFirstName");
						$(this).find("input:text:eq(1)").attr(
								"name",
								"csmrInfo.ownerList[" + (i)
										+ "].ownerMiddleName");
						$(this).find("input:text:eq(2)")
								.attr(
										"name",
										"csmrInfo.ownerList[" + (i)
												+ "].ownerLastName");
						$(this).find("input:text:eq(3)").attr("name",
								"csmrInfo.ownerList[" + (i) + "].caoUID");
						$(this).find("select:eq(0)").attr("name",
								"csmrInfo.ownerList[" + (i) + "].ownerTitle");
						$(this).find("select:eq(1)").attr("name",
								"csmrInfo.ownerList[" + (i) + "].gender");
						$(this).find("#deleteOwner").attr("onclick",
								"removeRow(" + (i) + ")");
					});
}

$("#noOfFlats").blur(function (){
	$("#propOutstanding").prop("disabled", false);

});

function getChecklistAndCharges(element) {
	var errorList = [];
	var flag = false;
	var csName = $("#csFirstName").val();
	if ($("#frmNewWaterForm").valid()) {
		// $("#applicantTitle").prop("disabled", false);
		$("#csOname").prop("disabled", false);
		$("#opincode").prop("disabled", false);
		$("#csOadd").prop("disabled", false);
		$("#mobileNo").prop("disabled", false);
		$("#emailId").prop("disabled", false);
		$("#csOGender").prop("disabled", false);

	}
	if ($("#propOutStanding").val() == 'Y' && $("#propDuesCheck").val() == 'true') {
		errorList
				.push(getLocalMessage('water.propDue.exist.notProceed.newConn'));
		displayErrorsOnPage(errorList);
	}
	if (csName.length > 100) {
	    errorList.push("Consumer Name cannot exceed 100 characters.");
	}
	
	if (errorList.length == 0)
		var errorList = validateFormDetails();
	if (errorList.length == 0) {
		
		var formName = findClosestElementId(element, 'form');
		var theForm = '#' + formName;
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = 'NewWaterConnectionForm.html?getCheckListAndCharges';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false,
				'html');
		/*
		 * var returnData = __doAjaxRequestValidationAccor(element, URL, 'POST',
		 * requestData, false, 'html');
		 */

		if (returnData) {
			var divName = '#validationDiv';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			$(divName).show();
			var free = $('#free').val();
			if (free == 'N' || free == 'Y') {
				// $("#applicantDetails").show();
				$('#chekListChargeId').hide();
				$('a[href$="#Applicant"]').addClass('collapsed');
				$('#Applicant').removeClass('in');
			} else {
				$('#chekListChargeId').show();
				// $("#applicantDetails").hide();
				$('.required-control').next().children().addClass(
						'mandColorClass');
			}

		}
	} else {
		displayErrorsOnPage(errorList);
	}

}

function resetWaterForm() {
	$('input[type=text]').val('');
	$(".alert-danger").hide();
	$("#frmNewWaterForm").validate().resetForm();
}
function clearCheckListAndCharges(id, value) {

	var URL = 'NewWaterConnectionForm.html?clearCheckListAndCharges';
	var returnData = __doAjaxRequest(URL, 'POST', '', false);
	var divName = '#validationDiv';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	$(divName).show();
	$("#" + id).val(value);
	if (id == 'trmGroup1') {
		$("#trmGroup1").val("0");
		$("#trmGroup2").val("0");
		$("#trmGroup3").val("0");
		$("#trmGroup4").val("0");
	}
	if (id == 'trmGroup2') {
		$("#" + id).val("0");
		$("#trmGroup1").val("0");
	}

}

function getPropertyDetails() {
	var propertyNo = $("#propertyNo").val();
	var flatNo = $("#flatNo").val();
	var theForm = '#frmNewWaterForm';
	if (propertyNo != '') {
		var requestData = {};
		requestData = __serializeForm(theForm);

		var URL = 'NewWaterConnectionForm.html?getPropertyDetails';
		var returnData = __doAjaxRequest(URL, 'POST', requestData, false);
		var errMsg = returnData["errMsg"];
		if (errMsg != '' && errMsg != undefined) {
			var errorList = [];
			errorList.push(errMsg);
			showMessageOnSubmit(errMsg, 'NewWaterConnectionForm.html');
		} else {
			$(formDivName).html(returnData);
			$("#flatNo").text('').append("<option value='" + flatNo+ "'>"+ flatNo + "</option>").attr("readOnly","readOnly");
			
			prepareDateTag();
		}
	}

}

function checkDuplicateCcn(index) {
	var ccnNo = $("#consumerNo" + index).val().trim();
	var duplicate = '';
	var myarray = new Array();
	for (i = 0; i < index; i++) {
		myarray[i] = $("#consumerNo" + i).val();
	}

	if (myarray.includes(ccnNo)) {
		duplicate = 'There is an existing connection with same number';
	}

	if (duplicate != '') {
		$("#consumerNo" + index).val('');
		$("#connectionSize" + index).val('');
		var errorList = [];
		errorList.push(duplicate);
		displayErrorsOnPage(errorList);
	} else {
		if (ccnNo != '') {
			var divName = formDivName;
			var requestData = {
				"ccnNo" : ccnNo,
			}
			var response = __doAjaxRequest(
					'NewWaterConnectionForm.html?getConnectionDetails', 'post',
					requestData, false, 'json');

			if (response.csIdn != 0) {
				$("#connectionSize" + index).val(response.csCcnsize);
				$("#connectionSize" + index).attr("readonly", true);
				if(response.meterNumber != null){
					$("#meterNo" + index).val(response.meterNumber);	
				}else{
					$("#meterNo" + index).attr("readonly", true);
				}
				$("#noOfTaps" + index).val(response.ccnOutStandingAmt);
				$("#noOfFamilies" + index).val(response.noOfFamilies);
				
			} else {
				var errorList = [];
				$("#consumerNo" + index).val('');
				errorList
						.push(getLocalMessage('water.valid.check.connection.number'));
				displayErrorsOnPage(errorList);
			}
		}

	}
}

function getConnectionSize() {
	
	var bplNo = $("#bplNo").val();
	if (bplNo != '') {
		if (bplNo == 'Y') {
			$("#isBhagirathi").show();
			$("#notBhagirathi").hide();
			$("#withoutBhagiRathi").attr("disabled", true);
			$("#withBhagiRathi").attr("disabled", false);
		} else {
			$("#isBhagirathi").hide();
			$("#notBhagirathi").show();
			$("#withBhagiRathi").attr("disabled", true);
			$("#withoutBhagiRathi").attr("disabled", false);
		}
	}
}

function validateBhagirathiConnection() {
	
	var bplNo = $("#bplNo").val();
	if (bplNo != '') {
		var withBhagiRathi = $("#withBhagiRathi").val();

		if (bplNo == 'Y') {
			if (withBhagiRathi != '') {

				var bhagiRathi = $("#withBhagiRathi").find("option:selected")
						.attr('code');
				if (bhagiRathi != '0.5') {
					var errorList = [];
					errorList
							.push(getLocalMessage('water.connection.bagh.con'));
					displayErrorsOnPage(errorList);
				}
			}
		}
	} else {
		$("#withoutBhagiRathi").val('');
		var errorList = [];
		errorList.push(getLocalMessage('water.validation.isabovepovertyline'));
		displayErrorsOnPage(errorList);
	}
}

function showMessageOnSubmit(message, redirectURL) {
	
	var errMsgDiv = '.msg-dialog-box';
	var cls = "Ok";

	var d = '<h5 class=\'text-center text-blue-2 padding-5\'>' + message
			+ '</h5>';
	d += '<div class=\'text-center\'><input type=\'button\' class= "btn btn-success" value=\''
			+ cls + '\'  id=\'btnNo\' onclick="proceed()"/></div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg');
	$(errMsgDiv).html(d);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showPopUpMsg(errMsgDiv);
}

function showPopUpMsg(childDialog) {
	$.fancybox({
		type : 'inline',
		href : childDialog,
		openEffect : 'elastic',
		closeBtn : false,
		helpers : {
			overlay : {
				closeClick : false
			}
		},
		keys : {
			close : null
		}
	});
	return false;
}

function proceed() {
	window.location.href = "NewWaterConnectionForm.html";
}

function fnValidatePAN(Obj) {
	
	$('.error-div').hide();
	var errorList = [];
	Obj = $('#panNo').val();
	if (Obj != "") {
		ObjVal = Obj;
		var panPat = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
		var code = /([C,P,H,F,A,T,B,L,J,G])/;
		var code_chk = ObjVal.substring(3, 4);
		if (ObjVal.search(panPat) == -1) {
			errorList.push(getLocalMessage('water.invalid.pan.num'));
			/* $('#advPanno').val(""); */
		} else if (code.test(code_chk) == false) {
			errorList.push(getLocalMessage('water.invalid.pan.num'));
			/* $('#advPanno').val(""); */
		}
	}
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	}
	return errorList;
}

function validateFormDetails() {
	var errorList = [];
	var propertyNo = $.trim($('#propertyNo').val());
	var bplNo = $("#bplNo").val();
	var bplNumber = $("#bplNumber").val();
	var csOname = $("#csOname").val();
	var propNoOptional = $('#propNoOptionalFlag').val();
	var chkConsumer = $('#isConsumer').is(':checked');
	var ccnCategory1 = $('#csCcncategory1 option:selected').text();
	var ccnCategory2 = $('#csCcncategory2 option:selected').text();
	
	
	if(propNoOptional == null || propNoOptional == 'Y'){
		if((ccnCategory1 == 'Permanent' && (ccnCategory2 == 'Unauthorized before 1995' || ccnCategory2 == 'Unauthorized after 1995'))
			|| (ccnCategory1 == 'Temporary') || (ccnCategory1 == 'Slum Area')){
			// no property related validation error
		}else{
		
			if (propertyNo == "" || propertyNo == '0' || propertyNo == undefined) {
				errorList
						.push(getLocalMessage('water.dataentry.validate.property.num'));
			}
			if (csOname == '') {
				errorList.push(getLocalMessage('water.enter.owner.name'));
			}
		}
	}else{
		if(propNoOptional != null && propNoOptional == 'Y'){
			if(chkConsumer == true){
				if (propertyNo == "" || propertyNo == '0' || propertyNo == undefined) {
					errorList
							.push(getLocalMessage('water.dataentry.validate.property.num'));
				}
				if (csOname == '') {
					errorList.push(getLocalMessage('water.enter.owner.name'));
				}
			}
		}	
	}

	if (bplNo == '') {
		errorList.push(getLocalMessage('water.validation.isabovepovertyline'));
	} else {
		if (bplNo == 'Y' && bplNumber == '')
			errorList.push(getLocalMessage('water.validation.bplNo'));
	}

	if (!chkConsumer) {
		var csFirstName = $("#csFirstName").val().trim();
		var csAddress1 = $("#csAddress1").val().trim();
		var csPinCode = $("#csPinCode").val();
		var csMobileNo = $("#csMobileNo").val().trim();

		if (csFirstName == '') {
			errorList.push(getLocalMessage('water.enter.consumer.name'));
		}
		if (csAddress1 == '') {
			errorList.push(getLocalMessage('water.enter.consumer.adddress'));
		}
		if (csPinCode == '') {
			errorList.push(getLocalMessage('water.validation.OpinCode'));
		}
		if (csMobileNo == '') {
			errorList
					.push(getLocalMessage('water.validation.Applicantentermobileno'));
		}
		if (csMobileNo != '' && !validMobileno(csMobileNo)) {
			errorList.push(getLocalMessage('water.validation.ApplicantInvalidmobile'));
		}

	}
	/*
	 * var codDwzid1 = $("#codDwzid1").val(); var codDwzid2 =
	 * $("#codDwzid2").val(); if (codDwzid1 == 0) {
	 * errorList.push(getLocalMessage('water.validation.selZone')); } if
	 * (codDwzid2 == 0) {
	 * errorList.push(getLocalMessage('water.validation.seleWard')); }
	 */
	var chk = $('#billing').is(':checked');
	if (!chk) {
		var billingAreaName = $("#billingAreaName").val().trim();
		var billingPinCode = $("#billingPinCode").val().trim();
		var billingMobile= $("#billingMobile").val().trim();

		if (billingAreaName == '') {
			errorList.push(getLocalMessage('water.enter.bill.area.name'));
		}
		if (billingPinCode == '') {
			errorList.push(getLocalMessage('water.enter.bill.pincode'));
		}
		if (billingMobile == '') {
			errorList.push(getLocalMessage('water.enter.bill.mobile'));
		}
		if (billingMobile != '' && !validMobileno(billingMobile)) {
		    errorList.push(getLocalMessage('water.validation.ApplicantInvalidmobile'));
		}

	}

	var typeOfApplication = $("#typeOfApplication").val();
	var trmGroup1 = $("#trmGroup1").val();

	if (typeOfApplication == '') {
		errorList.push(getLocalMessage('water.validation.typeApplication'));
	}

	if (trmGroup1 == 0) {
		errorList.push(getLocalMessage('water.validation.tariff'));
	}

	else {
		if ($('#trmGroup1 option:selected').attr('code') == "H") {
			var hotelNumber = $("#hotelNumber").val().trim();
			if (hotelNumber == '') {
				errorList.push(getLocalMessage('water.enter.rooms'));
			}
		} else if ($('#trmGroup1 option:selected').attr('code') == "RFC") {
			var restaurantNumber = $("#restaurantNumber").val().trim();
			if (restaurantNumber == '') {
				errorList.push(getLocalMessage('water.enter.table.num'));
			}
		}
	}
	if (bplNo != '') {
		if (bplNo == 'N') {
			var withoutBhagiRathi = $("#withoutBhagiRathi").val();
			if (withoutBhagiRathi == 0) {
				errorList.push(getLocalMessage('water.sel.connectionsize'));
			}
		} else {
			var withBhagiRathi = $("#withBhagiRathi").val();
			if (withBhagiRathi == 0) {
				errorList.push(getLocalMessage('water.sel.connectionsize'));
			}
		}
	}

	/*if ($("input[name='csmrInfo.csPtype']").attr('value') == 'U') {
		var plumber = $("#plumber").val();
		if (plumber == '') {
			errorList.push(getLocalMessage('water.select.plumber.name'));
		}
	} else {
		var licPlumber = $("#licPlumber").val();
		if (licPlumber == '') {
			errorList.push(getLocalMessage('water.select.plumber.name'));
		}
	}
*/
	 var csTYpe = document
	    .querySelector('input[name="csmrInfo.csPtype"]:checked').value;

 if (csTYpe == 'U') {
	var plumber = $("#plumber").val();
	if (plumber == '') {
	    errorList.push(getLocalMessage('water.select.plumber.name'));
	}
 }
 if (csTYpe == 'L') {
	var licPlumber = $("#licPlumber").val();
	if (licPlumber == '') {
	    errorList.push(getLocalMessage('water.select.plumber.name'));
	}
 }
 
	var checked = $('#ExistingConnection').is(':checked');
	if (checked) {
		$("#tbl2 tbody tr")
				.each(
						function(i) {
							var consumerNo = $("#consumerNo" + i).val().trim();
							if (consumerNo == '') {
								errorList
										.push(getLocalMessage('water.select.connec.entry.num')
												+ (i + 1));
							}
						});
	}
	return errorList;
}

function showTermsConditionForm(element) {
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var response = __doAjaxRequest(
			'NewWaterConnectionForm.html?viewTermsCondition', 'POST',
			requestData, false, 'html');
	// $(formDivName).html(response);
	var divContents = response;
	var printWindow = window.open('', '_blank');
	printWindow.document.write('<html><head><title></title>');
	printWindow.document
			.write('<link href="assets/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<link href="assets/libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" />')
	printWindow.document
			.write('<link href="assets/css/style.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<link href="assets/css/style-responsive.css" rel="stylesheet" type="text/css" />')
	printWindow.document
			.write('<script src="js/mainet/ui/jquery.min.js"></script>')
	printWindow.document
			.write('<script type="text/javascript" src="assets/libs/excel-export/excel-export.js"></script>')
	printWindow.document
			.write('<script>$(window).on("load",function() {$(".table-pagination, .remove-btn, .paging-nav, tfoot").remove(); $(".table thead tr th").removeClass("tablesorter-headerDesc tablesorter-headerAsc tablesorter-header"); $(".table tr").removeAttr("style");});</script>')
	printWindow.document.write('</head><body style="background:#fff;">');
	printWindow.document
			.write('<div style="position:fixed; width:100%; bottom:0px; z-index:1111;"><div class="text-center"><button onclick="window.print();" class="btn btn-success hidden-print" type="button"><i class="fa fa-print" aria-hidden="true"></i> Print</button>  <button onClick="window.close();" type="button" class="btn btn-blue-2 hidden-print">Close</button></div></div>')
	printWindow.document.write(divContents);
	printWindow.document.write('</body></html>');
	printWindow.document.close();

}

function getFlatDetails() {
	var optionsAsString = '';
	var propNo = $("#propertyNo").val();
	$("#flatNo").text('');
	
	if (propNo != 0 && propNo != '') {
		var data = {
			"propNo" : propNo
		};

		var URL = 'NewWaterConnectionForm.html?getBillingMethod';
		var ajaxResponse = __doAjaxRequest(URL, 'POST', data, false, 'json');
			if (ajaxResponse) {
				if (ajaxResponse.length != 0) {
					let flatMap = ajaxResponse;
					var flatlist = [];
					var singleFlatBilling = false;
					if (flatMap.I != undefined) {
						singleFlatBilling = true;
						flatlist = flatMap.I;
					} else if (flatMap.W != undefined) {
						singleFlatBilling = false;
						flatlist = flatMap.W;
					} else if (flatMap.NULL != undefined) {
						singleFlatBilling = false;
						flatlist = flatMap.NULL;
					}
					if (singleFlatBilling) {
						optionsAsString += "<option value=''> Select</option>";
					}
					for (var i = 0; i < flatlist.length; i++) {
						optionsAsString += "<option value='" + flatlist[i] + "'>"
								+ flatlist[i] + "</option>";
					}
					$("#flatNo").append(optionsAsString);
					if (!singleFlatBilling) {
						getPropertyDetails();
					}
				}
			}
		}
	
}

function validMobileno(mobileNo) {
	var regex = /^[6789]/;
    return regex.test(mobileNo);
}