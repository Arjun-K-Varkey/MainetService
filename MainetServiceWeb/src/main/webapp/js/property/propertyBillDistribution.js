var billDistributionTable;
var selectedBillRows = {};
var selectedBillDates = {};

$(document).ready(function() {

	if ($("#flatNo").val() == "") {
		$("#showFlatNo").hide();
	} else {
		$("#showFlatNo").show();
	}

	showSingleMultiple();

	billDistributionTable = $('#datatables').DataTable({
		"oLanguage": { "sSearch": "" },
		"aLengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
		"iDisplayLength": 5,
		"bInfo": true,
		"lengthChange": true,
		"bPaginate": true,
		"bFilter": true,
		/*
		 * DataTables calls this every time the table is drawn:
		 * pagination, search, page-size change, sorting, etc.
		 */
		"drawCallback": function() {

			/*
			 * Restore checkbox state whenever DataTables
			 * changes page/search/sort.
			 */
			restoreSelectedRows();
			updateSelectAllState();
			initBillDistributionDatepicker();
			setBillDistriDate();
		}
	});
	
	/*
	* IMPORTANT:
	* Remove any previous select-all handler.
	*/
	$(document).off('change.billSelectAll', '#selectall');
	/*
	* Select ALL rows across ALL DataTables pages.
	*/
	$(document).on('change.billSelectAll', '#selectall', function() {

		var isChecked = $(this).prop('checked');

		/*
		 * DataTables $() searches ALL rows,
		 * including rows on other pages.
		 */
		billDistributionTable
			.$('input.checkall')
			.each(function() {

				var checkboxName = $(this).attr('name');

				if (!checkboxName) {
					return;
				}

				if (isChecked) {
					selectedBillRows[checkboxName] = true;
				} else {
					delete selectedBillRows[checkboxName];
				}
			});

		/*
		 * Update currently visible checkboxes.
		 */
		billDistributionTable
			.$('input.checkall')
			.prop('checked', isChecked);

		updateSelectAllState();
	});

	/*
	  * Individual checkbox.
	  */
	$('#datatables tbody').on(
		'change',
		'input.checkall',
		function() {

			var name = $(this).attr('name');

			if (!name) {
				return;
			}

			if (this.checked) {
				selectedBillRows[name] = true;
			} else {
				delete selectedBillRows[name];
			}

			updateSelectAllState();
			setBillDistriDate();
		}
	);

	/*
	 * Datepicker
	 */
	initBillDistributionDatepicker();

	$(".billDistribDate1").datepicker({
		dateFormat: 'dd/mm/yy',
		changeMonth: true,
		changeYear: true,
		maxDate: '-0d',
	});


	var dateFields = $('.Moredatepicker');
	dateFields.each(function() {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});

	$('#billFromDate').datepicker({
		dateFormat: 'dd/mm/yy',
		changeMonth: true,
		changeYear: true,
		maxDate: 0
	});

	$("#billFromDate").keyup(function(e) {

		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

	$('#billToDate').datepicker({
		dateFormat: 'dd/mm/yy',
		changeMonth: true,
		changeYear: true,
		maxDate: 0
	});

	$("#billToDate").keyup(function(e) {
		if (e.keyCode != 8) {
			if ($(this).val().length == 2) {
				$(this).val($(this).val() + "/");
			} else if ($(this).val().length == 5) {
				$(this).val($(this).val() + "/");
			}
		}
	});

});

function toggleAllBills(selectAll) {

	var checked = $(selectAll).prop('checked');

	if (!billDistributionTable) {
		return;
	}

	/*
	 * IMPORTANT:
	 * $() from DataTables searches ALL rows,
	 * including rows on page 2 and page 3.
	 */
	billDistributionTable
		.$('input.checkall')
		.each(function() {

			var name = $(this).attr('name');

			if (!name) {
				return;
			}

			$(this).prop('checked', checked);

			if (checked) {
				selectedBillRows[name] = true;
			} else {
				delete selectedBillRows[name];
			}
		});

	updateSelectAllState();
	setBillDistriDate();
}


function restoreSelectedRows() {

	if (!billDistributionTable) {
		return;
	}

	$('#datatables tbody input.checkall').each(function() {

		var name = $(this).attr('name');

		$(this).prop(
			'checked',
			name && selectedBillRows[name] === true
		);
	});
}


function updateSelectAllState() {

	if (!billDistributionTable) {
		return;
	}

	var total =
		billDistributionTable.$('input.checkall').length;

	var selected =
		Object.keys(selectedBillRows).length;

	if (total === 0 || selected === 0) {

		$('#selectall')
			.prop('checked', false)
			.prop('indeterminate', false);

	} else if (selected === total) {

		$('#selectall')
			.prop('checked', true)
			.prop('indeterminate', false);

	} else {

		$('#selectall')
			.prop('checked', false)
			.prop('indeterminate', true);
	}
}

function prepareAllSelectedRowsForSubmit() {

	/*
	 * Remove hidden fields created by previous submit attempt.
	 */
	$('.crossPageSelectedCheckbox').remove();

	/*
	 * Create hidden checkbox values for ALL selected rows.
	 */
	$.each(selectedBillRows, function(checkboxName, selected) {

		if (!selected) {
			return;
		}

		$('<input>')
			.attr('type', 'hidden')
			.attr('name', checkboxName)
			.attr('value', 'Y')
			.addClass('crossPageSelectedCheckbox')
			.appendTo('#PropertyBillDistribution');
	});
}

function initBillDistributionDatepicker() {

	$('.billDistribDate1').each(function() {

		var $field = $(this);
		var fieldValue = $.trim($field.val());

		/*
		 * DataTables recreates rows when changing page.
		 * Therefore normalize the date on every draw.
		 *
		 * Example:
		 * 05/09/2026 12:00
		 * becomes
		 * 05/09/2026
		 */
		if (fieldValue.length > 10) {
			$field.val(fieldValue.substring(0, 10));
		}
		/*
		 * Don't initialize the same input multiple times.Initialize datepicker only once.
		 */
		if (!$(this).hasClass('hasDatepicker')) {

			$(this).datepicker({
				dateFormat: 'dd/mm/yy',
				changeMonth: true,
				changeYear: true,
				maxDate: 0
			});
		}
	});
}

function getFlatNo() {

	var errorList = [];
	var propNo = $("#propertyNo").val();
	var oldPropNo = $("#oldPropertyNo").val();
	if (propNo == "" && oldPropNo == "") {
		errorList.push(getLocalMessage("property.changeInAss"));
	}
	if (errorList.length == 0) {
		var propNo = $("#propertyNo").val();
		var requestData = {
			"propNo": propNo
		};

		var ajaxResponse = doAjaxLoading(
			'PropertyBillDistribution.html?getBillingMethod', requestData,
			'html');

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
	} else {
		showErrorOnPage(errorList);
	}

}

function showSingleMultiple() {

	selectedBillRows = {};
	$('#selectall').prop('checked', false);
	$('.checkall').each(function() {
		this.checked = false;
	});

	if ($("input[name='specialNotGenSearchDto.specNotSearchType']:checked")
		.val() == "S") {
		$('.PropDetail').show();
		$('.propLable').show();
		$('.wardZone').hide();
		$('.buildClusterNo').hide();
		$('.Loc').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();

		$('#assWard1').val('');
		$('#assWard2').val('');
		$('#locId').val('');

	} else if ($(
		"input[name='specialNotGenSearchDto.specNotSearchType']:checked")
		.val() == "M") {
		$('.PropDetail').hide();
		$('.wardZone').show();
		$('.buildClusterNo').show();
		$('.Loc').show();
		$('.usageType').show();
		$('.searchBtn').show();
		$('.sectionSeperator').show();
		$('.PropDetail').val('');

		$('#propertyNo').val('');
		$('#oldPropertyNo').val('');
	} else {
		$('.PropDetail').hide();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.propLable').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();
	}

}

function serachProperty() {

	var errorList = [];
	var billMethod = $("#billingMethod").val();
	if (billMethod == 'I') {
		var flatNo = $("#flatNo").val();
		if (flatNo == "" || flatNo == undefined || flatNo == null
			|| flatNo == '0') {
			errorList.push(getLocalMessage('prop.select.flatNo'));
		}
	}
	if (errorList.length == 0) {
		showloader(true);

		var formAction = $('#PropertyBillDistribution').attr('action');
		var url = formAction + '?serachPropForBillDistribution';
		$('#PropertyBillDistribution').attr('action', url);

		$('#PropertyBillDistribution').submit();

	} else {
		displayErrorsOnPage(errorList);
	}
}

function updateDueDate(element) {

	/*
	 * Collect selected checkboxes from ALL DataTable pages
	 * before submitting the form.
	 */
	prepareAllSelectedRowsForSubmit();

	return saveOrUpdateForm(
		element,
		"Bill generation done successfully",
		'PropertyBillDistribution.html',
		'saveform'
	);
}

function resetFormDetails(element) {
	value = "PropertyBillDistribution.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();


}

function setBillDistriDate() {

	var multiBillDistrDate = $('#multiBillDistrDate').val();
	if (multiBillDistrDate != "") {
		$('#datatables tbody .checkall:checked').each(function() {

			$(this)
				.closest('tr')
				.find('.billDistribDate1')
				.val(multiBillDistrDate);
		});
	}
}


