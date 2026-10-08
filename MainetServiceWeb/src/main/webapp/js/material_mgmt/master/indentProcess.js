$(document).ready(function() {

	$('#binDefMasterFrm').validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}
	});
	
	$("#searchStore").dataTable({
		"oLanguage" : {
			"sSearch" : ""
		},
		"aLengthMenu" : [ [ 5, 10, 15, -1 ], [ 5, 10, 15, "All" ] ],
		"iDisplayLength" : 5,
		"bInfo" : true,
		"lengthChange" : true
	});
	
	
	var indentdate =$('#indentdate').val();
	if (indentdate)
	    $('#indentdate').val(indentdate.split(' ')[0]);

});


function addIndent(formUrl, actionParam) {
    var divName = '.content-page';
    var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, {}, 'html', divName);
    $(divName).removeClass('ajaxloader');
    $(divName).html(ajaxResponse);
    
    prepareTags();

    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0');
    var yyyy = today.getFullYear();
    var formattedDate = dd + '/' + mm + '/' + yyyy;

    var dateInput = $("input[name='indentProcessDTO.indentdate']");
    dateInput.val(formattedDate).prop("readonly", true);
}

function getQuantity(index) {
	$("#errorDiv").hide();
	var errorList = [];
	quantityErrorFlag = false;  // reset before validation

	$(".withHeld").each(
			function(i) {
				var itemid = $("#itemid" + i).val();
				var storeid = $("#storeid").val();
				var quantity = $("#quantity" + i).val();
				var itemGroupId = $("#itemGroupId" + i).val();
				var row = i + 1;

				requestData = {
					"itemid" : itemid,
					"storeid" : storeid,

				};
				var availableQty = parseInt(__doAjaxRequest(
						"IndentProcess.html?getAvailableQntyForCompare",
						'POST', requestData, false, 'json'));
				
				if (isNaN(availableQty) || availableQty < quantity
						|| availableQty == null || availableQty == undefined
						|| availableQty == '0' || availableQty == 'NaN') {
					errorList.push(getLocalMessage('Stock is Not Available' + " - " + row));
					
					quantityErrorFlag = true; // mark error
					
				}

			});

	if (errorList.length > 0) {
		$("#errorDiv").show();
		$("#errorDiv").removeClass('hide');
		displayErrorsOnPage(errorList);

	}
}

function submitIndentForm(obj) {
	$("#errorDiv").hide();
	var errorList = [];
	errorList = validateUniqueItem();
	errorList = validateItemTable(errorList);
	errorList = validateWithHeld(errorList);
	
	// Check stock error from getQuantity
	if (quantityErrorFlag) {
        errorList.push("Stock is Not Available, Please Change Your Quantities");
    }
	
	if (errorList.length > 0) {
		$("#errorDiv").show();
		$("#errorDiv").removeClass('hide');
		displayErrorsOnPage(errorList);
	} else{
		
			return saveOrUpdateForm(obj, " ", 'IndentProcess.html?printDepartmentalIndent', 'saveform');
				}
		
}

function submitGoodsReceivedForm(obj) {
	  var errorList = [];	  
	    errorList = validateGoodsReceivedWithHeld(errorList);
	    var receivedReceiptDate = $("#receivedReceiptDate").val();
	    var requisitionNo = $("#requisitionNo").val();
	    var vendorName = $("#vendorName").val();
	    var remark = $("#remark").val();
	    var challanDate = $("#challanDate").val();
	    var challanNo= $("#challanNo").val();
	    
	    if(receivedReceiptDate =="" || receivedReceiptDate =="0"){
			 errorList.push(getLocalMessage("Please Select Order Date."));
		 }
	    if(requisitionNo =="" || requisitionNo =="0"){
	    	errorList.push(getLocalMessage("Please Enter Order No."));
	    }
	    if(vendorName =="" || vendorName =="0"){
	    	$("#errorDiv").show();
			errorList.push(getLocalMessage("Please Select Vendor Name."));
			displayErrorsOnPage(errorList);
	    }
	   /* if(remark =="" || remark =="0"){
	    	errorList.push(getLocalMessage("Please Select remark"));
	    }*/
	 
	    if(challanDate =="" || challanDate =="0"){
			 errorList.push(getLocalMessage("Please Select Challan Date"));
		 }
	    
	    if(challanNo =="" || challanNo =="0"){
	    	errorList.push(getLocalMessage("Please Select Challan No."));
	    }
	    
	    if(requisitionNo != null || requisitionNo != "0" || requisitionNo != ""){
	    	
	    	var requestData = {
	    			"requisitionNo": requisitionNo
	    		};
	    	if(requisitionNo != null || requisitionNo != "" || requisitionNo != undefined){
	    		var reqNo = __doAjaxRequest("GoodReceiveReceiptForm.html?getDuplicateDataOrNot", 'POST', requestData, false, 'json');
	    	}
	    	
	    	if (reqNo != '0'){
	    		errorList.push(getLocalMessage(requisitionNo + " " + "Order Number Already Exsists."));
	    		
	    	}
	    	
	    }
	    
	
	    
	    if (errorList.length > 0) {
	        $("#errorDiv").show();
	        $("#errorDiv").removeClass('hide');
	        displayErrorsOnPage(errorList);
	    } else {
	var formName = findClosestElementId(obj, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	/*var errorList = [];*/
	var url = "GoodReceiveReceiptForm.html";
	return saveOrUpdateForm(obj, " ", 'GoodReceiveReceiptForm.html', 'saveGoodsReceiptData');
	    }
}

function searchGoodsReceivedReceiptData() {
	var errorList = [];
	var vendorName = $('#vendorName').val();
	var requisitionNo = $('#requisitionNo').val();
	if ($('#vendorName').val() == "" && $('#requisitionNo').val() == "") {
		$("#errorDiv").show();
		errorList.push(getLocalMessage("material.item.selectAtleast1criteria"));
		displayErrorsOnPage(errorList);
	} else {
		var data = {
			"vendorId" : $('#vendorName').val(),
			"requisitionNo" : $('#requisitionNo').val(),
		};
		var divName = '.content-page';
		var ajaxResponse = doAjaxLoading('GoodReceiveReceiptForm.html?searchGoodsReceivedReceipt',
				data, 'html', divName);
		$(divName).removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
		//prepareTags();
	}
}

function showConfirmBoxSave(){
	
	
	var saveorAproveMsg=getLocalMessage("account.btn.save.msg");
	var	errMsgDiv		=	'.msg-dialog-box';
	var message='';
	var cls =getLocalMessage("account.btn.save.yes");
	var no=getLocalMessage("account.btn.save.no");
	
	 message	+='<h4 class=\"text-center text-blue-2\">'+saveorAproveMsg+'</h4>';
	 message	+='<div class=\'text-center padding-bottom-10\'>  '+	
	'<input type=\'button\' value=\''+cls+'\'  id=\'btnNo\' class=\'btn btn-blue-2 \'    '+ 
	' onclick="saveDataAndShowSuccessMsg()"/>   '+ 
	'<input type=\'button\' value=\''+no+'\' tabindex=\'0\' id=\'btnNo\' class=\'btn btn-blue-2 autofocus\'    '+ 
	' onclick="closeConfirmBoxForm()"/>'+ 
	'</div>';

	$(errMsgDiv).addClass('ok-msg').removeClass('warn-msg'); 
	$(errMsgDiv).html(message);
	$(errMsgDiv).show();
	$('#btnNo').focus();
	showModalBoxWithoutCloseaccount(errMsgDiv);
	
}


function validateItemTable(errorList){
	 var indenter = $('#indenter').val();
	 var indentdate = $('#indentdate').val();
	 var storeid = $('#storeid').val();
	 var deliveryat = $('#deliveryat').val();
	 var expecteddate = $('#expectedDate').val();
	 var beneficiary = $('#beneficiary').val();
	 
	 if (indentdate == "" || indentdate == undefined)
			errorList.push(getLocalMessage('department.indent.validation.date'));
	 
	 if (beneficiary == "" || beneficiary == undefined || beneficiary == null)
			errorList.push(getLocalMessage('material.goods.beneficiary.wardValidation.msg'));
	 
	 /*if (indenter == "" || indenter == undefined)
			errorList.push(getLocalMessage('department.indent.validation.indentor'));*/
	 if (storeid == "" || storeid == undefined)
			errorList.push(getLocalMessage('department.indent.validation.store'));
	 /*if (deliveryat == "" || deliveryat == undefined)
			errorList.push(getLocalMessage('department.indent.validation.deliveryAdd'));*/
	 /*if (expecteddate == "" || expecteddate == undefined)
			errorList.push(getLocalMessage('department.indent.validation.expecteddate'));*/
	 if (Date.parse(indentdate) > Date.parse(expecteddate))
		 	errorList.push(getLocalMessage('department.indent.validation.deliveryExpected'));
	 return errorList;
}
/**
 * search indent summary
 * 
 * @param errorList
 * @returns
 */
function searchIndentStore(formUrl, actionParam) {
	var errorList = [];
	var data = {
			"storeid" : $('#storeid').val(),
			"indentno" : $('#indentno').val(),
			"indenter" : $('#indenter').val(),
			"status" : $('#status').val(),
			"deptId" : $('#deptId').val()
	};
	if($('#storeid').val() == '' &&  $('#indentno').val() == '' && $('#indenter').val() == '' && $('#status').val() == '0' && $('#deptId').val() == ''){
		errorList.push(getLocalMessage("department.indent.validation.search"));
		if (errorList.length > 0) {
			$("#errorDiv").show();
			displayErrorsOnPage(errorList);
	}	 
	}else{
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, data, 'html',divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
	}
}
/**
 * disable future dates
 * 
 * @param errorList
 * @returns
 */
/*$('#indentdate').datepicker({
	dateFormat : 'dd/mm/yy',
	changeMonth : true,
	maxDate : '0d',
	changeYear : true,
	pickTime: false
});
*/
$('#indentdate').datepicker({
    dateFormat: 'dd/mm/yy',
    changeMonth: true,
    changeYear: true,
    minDate: '0d',  // Sets the minimum date to today
    maxDate: '0d',  // Sets the maximum date to today
    beforeShowDay: function(date) {
        var today = new Date();
        return [date.toDateString() === today.toDateString()];
    }
});




$('#expecteddate').datepicker({
	dateFormat : 'dd/mm/yy',
	changeMonth : true,
	minDate : '0',
	changeYear : true
})
/*function addIndentDetailsWith(e) {
	var errorList = [];
	errorList = validateUniqueItem();
    errorList = validateWithHeld(errorList);
	
	if (errorList.length > 0) { 
	      $("#errorDiv").show();
	      $("#errorDiv").removeClass('hide');
	       displayErrorsOnPage(errorList);
	 } else{
		var count = $('#initialTable tr').length - 1;
		  
	        	$("#errorDiv").addClass('hide');
						var clickedRow = $(this).parent().parent().index();
						var content = $('#initialTable tr').last().clone();
						$('#initialTable tr').last().after(content);
						//content.find("input:text").attr("value", "");
						content.find("select").val("");
						content.find("input:text").val('');
						content.find("input:hidden").val('');
						content.find('div.chosen-container').remove();
			            content.find("select:eq(0)").chosen().trigger("chosen:updated");
			            content.find("select:eq(1)").chosen().trigger("chosen:updated");
						reOrderMachineryDetails();
					}
		
}*/


function addIndentDetailsWith(e) {
    var errorList = [];
    errorList = validateUniqueItem();
    errorList = validateWithHeld(errorList);
 
    if (errorList.length > 0) {
        $("#errorDiv").show();
        $("#errorDiv").removeClass('hide');
        displayErrorsOnPage(errorList);
    } else {
        var count = $('#initialTable tr').length - 1;
 
        $("#errorDiv").addClass('hide');
        var clickedRow = $(this).parent().parent().index();
        var content = $('#initialTable tr').last().clone();
        $('#initialTable tr').last().after(content);
        // Clear values in the cloned row
        content.find("select").val("");
        content.find("input:text").val('');
        content.find("input:hidden").val('');
        content.find('div.chosen-container').remove();
        // Reinitialize the chosen plugin
        content.find("select:eq(0)").chosen().trigger("chosen:updated");
        content.find("select:eq(1)").chosen().trigger("chosen:updated");
 
        // Reorder machinery details after adding the row
        reOrderMachineryDetails();
    }
}

function reOrderMachineryDetails() {
    $('.withHeld').each(function(i) {
        var $row = $(this);

        // Serial Number
        $row.find("input[id^='sNo']")
            .attr("id", "sNo" + i)
            .val(i + 1);

        // Item ID dropdown
        $row.find("select[id^='itemid']")
            .attr("id", "itemid" + i)
            .attr("name", "indentProcessDTO.item[" + i + "].itemid")
            .attr("onchange", "getUom(" + i + ")");

        // Item Group Name (readonly text, UI only)
        $row.find("input[id^='itemGroupName']")
            .attr("id", "itemGroupName" + i)
            .attr("name", "indentProcessDTO.item[" + i + "].itemGpName");

        // Item Group ID (hidden, DB value)
        $row.find("input[type='hidden'][id^='itemGroupId']")
            .attr("id", "itemGroupId" + i)
            .attr("name", "indentProcessDTO.item[" + i + "].itemGpId");

        // UOM
        $row.find("input[id^='uom']")
            .attr("id", "uom" + i)
            .attr("name", "indentProcessDTO.item[" + i + "].uom");

        // Quantity
        $row.find("input[id^='quantity']")
            .attr("id", "quantity" + i)
            .attr("name", "indentProcessDTO.item[" + i + "].quantity");

        // Reinit chosen for dropdowns
        $row.find("select").chosen().trigger("chosen:updated");
    });
}


function addGoodReceivedIndentDetailsWith(e) {
    var errorList = [];
    errorList = goodsValidateUniqueItem();
    errorList = validateGoodsReceivedWithHeld(errorList);

    if (errorList.length > 0) {
        $("#errorDiv").show().removeClass('hide');
        displayErrorsOnPage(errorList);
        return;
    }

    $("#errorDiv").addClass('hide');

    // Clone last row
    var lastRow = $('#initialTable tr.withHeld').last();
    var newRow = lastRow.clone();

    // Clear only the INPUT/SELECT values in the cloned row
    newRow.find("input:text").val("");
    newRow.find("input:hidden").val("");
    newRow.find("select").val("");

    // Remove chosen container before re-init
    newRow.find('div.chosen-container').remove();

    // Append cloned row
    lastRow.after(newRow);

    // Reorder all rows to reassign ids/names properly
    reOrderGoodsReceivedDetails();

    // Reinitialize chosen after reorder
    newRow.find("select").chosen().trigger("chosen:updated");
}

function reOrderGoodsReceivedDetails() {
    $('.withHeld').each(function (i) {
        // Serial Number
        $(this).find("input:text:eq(0)")
            .attr("id", "sNo" + i)
            .val(i + 1)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].sNo");

        // Item ID (select)
        $(this).find("select:eq(0)")
            .attr("id", "itmId" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].itemId")
            .attr("onchange", "getGoodReceiptUom(" + i + ")");

        // Item Group Name
        $(this).find("input:text:eq(1)")
            .attr("id", "itemGroupName" + i);

        // Item Group ID (hidden)
        $(this).find("input:hidden:eq(0)")
            .attr("id", "itemGroupId" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].itemGroup");

        // UOM
        $(this).find("input:text:eq(2)")
            .attr("id", "uom" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].uom");

        // Bin Location
        $(this).find("input:text:eq(3)")
            .attr("id", "binLocationName" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].locationName");

        // Received Quantity
        $(this).find(".receivedQuantity")
            .attr("id", "receivedQuantity" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].receQuantity");

        // Unit Price
        $(this).find(".unitPrice")
            .attr("id", "unitPrice" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].unPrice")
            .attr("onblur", "calculateTotalValue(" + i + ")");

        // Total Value
        $(this).find(".totalValue")
            .attr("id", "totalValue" + i)
            .attr("name", "goodreceivedDto.goodsReceivedReceiptDetDto[" + i + "].totalValue");
    });
}









function validateWithHeld(errorList){
   $('.withHeld').each(function(i) {
	     var itemid = $("#itemid" + i).val();
		 var quantity = $("#quantity" + i).val();
		 var no = i+1;	
		 if(itemid =="" || itemid =="0"){
			 errorList.push(getLocalMessage("material.management.item.name")+" "+no);
		 }
		 if(quantity == "0" || quantity == ""){
			 errorList.push(getLocalMessage("material.management.quantity.validation")+" "+no); 
		 }
   });
   return errorList;
}

function validateGoodsReceivedWithHeld(errorList){
	   $('.withHeld').each(function(i) {
		     var itemGroupId = $("#itemGroupId" + i).val();
		     var itemid = $("#itmId" + i).val();
			 var receivedQuantity = $("#receivedQuantity" + i).val();
			 var unitPrice = $("#unitPrice" + i).val();
			 
			 var no = i+1;	
			 if(itemGroupId =="" || itemGroupId =="0"){
				 errorList.push(getLocalMessage("Please Select Item Group")+" "+no);
			 }
			 if(itemid =="" || itemid =="0"){
				 errorList.push(getLocalMessage("material.management.item.name")+" "+no);
			 }
			 if(receivedQuantity == "0" || receivedQuantity == ""){
				 errorList.push(getLocalMessage("Please Enter Received Quantity")+" "+no); 
			 }
			 
			 if(unitPrice == "0" || unitPrice == ""){
				 errorList.push(getLocalMessage("Please Enter Unit Price")+" "+no); 
			 }
	   });
	   return errorList;
	}





function deleteEntry(obj, ids) {
	deleteTableRow('initialTable', obj, ids);
	if ($.fn.DataTable.isDataTable('#initialTable')) {
		$('#initialTable').DataTable().destroy();
	}
}


function getEmpDetails() {
	var indenter = $("#indenter").val();
	if (indenter == "" || indenter == null || indenter == undefined || indenter == 0) {
		$('#deptId').val('');
		$('#deptName').val('');
		$('#desgId').val('');
		$('#desgName').val('');
		$('#reportingmgr').val('');
		$('#reportingMgrName').val('');
	} else {
		var requestData = "indenter=" + $("#indenter").val();
		var object = __doAjaxRequest("IndentProcess.html?getEmpDetail", 'POST', requestData, false, 'json');				
		$.each(object, function(key, value) {
			if (key == 'deptId') {
				$('#deptId').val(value[0]);
				$('#deptName').val(value[1]);
			}
			if (key == 'desgId') {
				$('#desgId').val(value[0]);
				$('#desgName').val(value[1]);
			}
			if (key == 'reportingmgr') {
				$('#reportingmgr').val(value[0]);
				$('#reportingMgrName').val(value[1] + " " + value[2]);
			}			
		});
		
		var beneLocationName = object.beneficiaryLocName;
		var mobileNo = object.mobileNo
		
		$('.beneficiaryPattern').val(beneLocationName);
		$('.empMobNo').val(mobileNo);
		
	}
}


function getLocation() {
    var storeid = $("#storeid").val();
    var requestData = "storeid=" + storeid;

    if (storeid === "") {
        $('#location').val('');
        $('#locationName').val('');

        // Disable and reset each row's related fields
        $("[id^=itemGroupId]").each(function () {
            var selectElement = $(this);
            var d = selectElement.attr("id").replace("itemGroupId", "");

            // Reset and disable itemGroupId dropdown
            selectElement.prop("disabled", true);
            selectElement.empty().append('<option value="0">Select</option>');
            if (selectElement.hasClass("chosen-select-no-results")) {
                selectElement.chosen("destroy").chosen();
            }

            // Reset itemid dropdown
            var itemidSelect = $("#itemid" + d);
            if (itemidSelect.length) {
                itemidSelect.val("");
                itemidSelect.find("option").not(':first').remove();
                if (itemidSelect.hasClass("chosen-select-no-results")) {
                    itemidSelect.chosen("destroy").chosen();
                }
            }

            // Reset uom input
            var uomInput = $("#uom" + d);
            if (uomInput.length) {
                uomInput.val("");
            }

            // Reset quantity input
            var qtyInput = $("#quantity" + d);
            if (qtyInput.length) {
                qtyInput.val("");
            }
        });

        return; // Exit early if store is unselected
    }

    // Fetch location info
    var location = __doAjaxRequest("IndentProcess.html?getLocation", 'POST', requestData, false, 'json');
    $.each(location, function (key, value) {
        $('#location').val(key);
        $('#locationName').val(value);
    });

    // Fetch and populate item group dropdowns
    var findItemNames = __doAjaxRequest("IndentProcess.html?getIndentProcessItemNames", 'POST', requestData, false, 'json');
    if (findItemNames) {
        populateIdentIssueItmGrpNames(findItemNames);
    }
}

function populateIdentIssueItmGrpNames(findItemNames) {
    console.log("Received storeDetail: ", findItemNames);

    $("[id^=itemid]").each(function () {
        var selectElement = $(this);

        // Enable the dropdown if it was disabled
        selectElement.prop("disabled", false);

        // Clear and repopulate options
        selectElement.empty();
        selectElement.append('<option value="0">Select</option>');

        $.each(findItemNames, function (key, value) {
            selectElement.append('<option value="' + key + '">' + value + '</option>');
        });

        // Refresh Chosen plugin: destroy and re-initialize
        if (selectElement.hasClass("chosen-select-no-results")) {
            selectElement.chosen("destroy").chosen();
        }
    });
}


function getItemDetail(index) {
	var itemgroup = $("#itemGroupId" + index).val();
	var storeid= $("#storeid").val();
	
	
	var requestData = {
			"itemgroup": itemgroup,
			"storeid": storeid,
		};
		
		if (itemgroup != "" || itemgroup != null || itemgroup != undefined || itemgroup !='0'   ) {
			
			var storeDetail = __doAjaxRequest("IndentProcess.html?getItemGroupDetById", 'POST', requestData, false, 'json');
			
			if (storeDetail) {
		        populateDropdown(storeDetail, index);
		    }
			
		} 
		
	
}


function populateDropdown(storeDetail, index) {
	console.log("Received storeDetail: ", storeDetail);
	
    var selectElement = $("#itemid" + index); // Target only the relevant item select dropdown

    selectElement.empty(); // Clear existing options
    selectElement.append('<option value="">Select</option>'); // Add default option

    $.each(storeDetail, function (key, value) {
        selectElement.append('<option value="' + key + '">' + value + '</option>');
    });

    selectElement.trigger("chosen:updated"); // Refresh Chosen dropdown
}

function addGoodReceiveForm(formUrl, actionParam) {
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, {}, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

function getUom(i) {
    var errorList = validateUniqueItem();
    if (errorList.length > 0) {
        displayErrorsOnPage(errorList);
    } else {
        var itemid = $("#itemid" + i).val();
        var requestData = "itemid=" + itemid;

        if (itemid === "") {
            $('#uom' + i).val("");
            $('#itemGroupId' + i).val("");   // clear ID
            $('#itemGroupName' + i).val(""); // clear Name
        } else {
            var response = __doAjaxRequest("IndentProcess.html?getUom", 'POST', requestData, false, 'json');

            // set UOM
            $('#uom' + i).val(response.uom);

            // response.itemGroups = {1363: "Stationary"}
            var keys = Object.keys(response.itemGroups);
            if (keys.length > 0) {
                var onlyKey = keys[0];         // 1363
                var onlyValue = response.itemGroups[onlyKey]; // Stationary

                // Store ID in hidden field (DB)
                $('#itemGroupId' + i).val(onlyKey);

                // Show Name on UI
                $('#itemGroupName' + i).val(onlyValue);
            }
        }
    }
}




function getIndentDataById(formUrl, actionParam, indentid) {
	var data = {
		"indentid" : indentid
	};
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, data, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}

$('.beneficiaryPattern').on('keyup', function() {
    this.value = this.value.replace(/[`~!@#$%^*()_|+\=?;:".<>\{\}\[\]\\\/]/gi,'');
});


function validateUniqueItem() {
	var errorList = [];
	var itemArray = [];
	$('.withHeld').each(function(i) {
		var itemId = $("#itemid" + i).val();
		if (itemArray.includes(itemId))
			errorList.push(getLocalMessage("purchase.requisition.duplicate.item.validate") + " " + (i + 1));
		itemArray.push(itemId);
	});
	if (errorList.length > 0) {
		$("#errorDiv").show();
		$("#errorDiv").removeClass('hide');
		displayErrorsOnPage(errorList);
	}
	return errorList;
}

function goodsValidateUniqueItem() {
	var errorList = [];
	var itemArray = [];
	$('.withHeld').each(function(i) {
		var itemId = $("#itmId" + i).val();
		if (itemArray.includes(itemId))
			errorList.push(getLocalMessage("purchase.requisition.duplicate.item.validate") + " " + (i + 1));
		itemArray.push(itemId);
	});
	if (errorList.length > 0) {
		$("#errorDiv").show();
		$("#errorDiv").removeClass('hide');
		displayErrorsOnPage(errorList);
	}
	return errorList;
}

function getGoodReceiptUom(index) {
    var itemid = $("#itmId" + index).val();
    var itemGroupId = $("#itemGroupId" + index).val();

    var requestData = "itemid=" + itemid;
    if (itemid === "") {
        // Clear UOM, Group ID, and Group Name
        $('#uom' + index).val("");
        $('#itemGroupId' + index).val("");   // clear ID
        $('#itemGroupName' + index).val(""); // clear Name
    } else {
        var object = __doAjaxRequest("GoodReceiveReceiptForm.html?getUomAndBinLocName",
                                     'POST', requestData, false, 'json');
        if (object && object.binLocationName != null && object.uomName != null) {
            var binLocation = object.binLocationName;
            var uomName = object.uomName;

            $('#uom' + index).val(uomName);
            $('#binLocationName' + index).val(binLocation);

            // Handle itemGroups if available
            var keys = Object.keys(object.itemGroups || {});
            if (keys.length > 0) {
                var onlyKey = keys[0];
                var onlyValue = object.itemGroups[onlyKey];

                // Store ID in hidden field (DB)
                $('#itemGroupId' + index).val(onlyKey);

                // Show Name on UI
                $('#itemGroupName' + index).val(onlyValue);
            }
        }
    }
}


function calculateTotalValue(index) {
	var receivedQuantity = $("#receivedQuantity" + index).val();
	var unitPrice = $("#unitPrice" + index).val();

	var calculatedValue = receivedQuantity * unitPrice;

	$('#totalValue' + index).val(calculatedValue);

}

function getItemNames(index) {
	var itemgroup = $("#itemGroupId" + index).val();
	var storeId= $("#storeId").val();
	
	
	var requestData = {
			"itemgroup": itemgroup,
			"storeId": storeId,
		};
		
		if (itemgroup != "" || itemgroup != null || itemgroup != undefined || itemgroup !='0'   ) {
			
			var itemDetails = __doAjaxRequest("GoodReceiveReceiptForm.html?getGoodsReceiptItemNamesById", 'POST', requestData, false, 'json');
			
			if (itemDetails) {
		        goodReceivedpopulateDropdowns(itemDetails, index);
		    }
			
		} 
		
	
}

function viewGoodsReceivedReceipt(formUrl, actionParam, requisitionNo) {
	var data = {
		"requisitionNo" : requisitionNo
	};
	var divName = '.content-page';
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, data, 'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();
}


function goodReceivedpopulateDropdowns(itemDetails, index) {
	console.log("Received storeDetail: ", itemDetails);

	var selectElement = $("#itmId" + index); // Target only the relevant item
												// select dropdown

	selectElement.empty(); // Clear existing options
	selectElement.append('<option value="">Select</option>'); // Add default
																// option

	$.each(itemDetails, function(key, value) {
		selectElement.append('<option value="' + key + '">' + value
				+ '</option>');
	});

	selectElement.trigger("chosen:updated"); // Refresh Chosen dropdown
}


function getGoodsReceiptItemGroupsNames(){
	
	var storeId = $("#storeId").val();
    var requestData = "storeId=" + storeId;
    
    if (storeId === "") {
       
        // Disable and reset each row's related fields
        $("[id^=itemGroupId]").each(function () {
            var selectElement = $(this);
            var d = selectElement.attr("id").replace("itemGroupId", "");

            // Reset and disable itemGroupId dropdown
            selectElement.prop("disabled", true);
            selectElement.empty().append('<option value="0">Select</option>');
            if (selectElement.hasClass("chosen-select-no-results")) {
                selectElement.chosen("destroy").chosen();
            }

            // Reset itemid dropdown
            var itemidSelect = $("#itmId" + d);
            if (itemidSelect.length) {
                itemidSelect.val("");
                itemidSelect.find("option").not(':first').remove();
                if (itemidSelect.hasClass("chosen-select-no-results")) {
                    itemidSelect.chosen("destroy").chosen();
                }
            }

            // Reset uom input
            var uomInput = $("#uom" + d);
            if (uomInput.length) {
                uomInput.val("");
            }
            
            //Reset Bin location
            var uomInput = $("#binLocationName" + d);
            if (uomInput.length) {
                uomInput.val("");
            }
            

            // Reset quantity input
            var qtyInput = $("#receivedQuantity" + d);
            if (qtyInput.length) {
                qtyInput.val("");
            }
            
          //Reset unit price
            var uomInput = $("#unitPrice" + d);
            if (uomInput.length) {
                uomInput.val("");
            }
            
          //Reset Total value
            var uomInput = $("#totalValue" + d);
            if (uomInput.length) {
                uomInput.val("");
            }
        });

        return; // Exit early if store is unselected
    }
	
	var findGoodsItemNames = __doAjaxRequest("GoodReceiveReceiptForm.html?getgoodsReceivedReceiptItemGroup", 'POST', requestData, false, 'json');
	
	if (findGoodsItemNames) {
	    populateGoodsReceivedItmGrpNames(findGoodsItemNames);
	
}

}

function populateGoodsReceivedItmGrpNames(findGoodsItemNames) {
    console.log("Received storeDetail: ", findGoodsItemNames);

    $("[id^=itmId]").each(function () {
        var selectElement = $(this);

        // Enable the dropdown if it was disabled
        selectElement.prop("disabled", false);

        // Clear and repopulate options
        selectElement.empty();
        selectElement.append('<option value="0">Select</option>');

        $.each(findGoodsItemNames, function (key, value) {
            selectElement.append('<option value="' + key + '">' + value + '</option>');
        });

        // Refresh Chosen plugin: destroy and re-initialize
        if (selectElement.hasClass("chosen-select-no-results")) {
            selectElement.chosen("destroy").chosen();
        }
    });
}

