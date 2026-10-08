/**
 * Author : Lalit.Prusti Created Date : 17 May, 2019
 */
var fileArray = [];
$(document).ready(function() {

	prepareDateTag();

	$("#frmComplainRegister").validate({
		onkeyup : function(element) {
			this.element(element);
			console.log('onkeyup fired');
		},
		onfocusout : function(element) {
			this.element(element);
			console.log('onfocusout fired');
		}
	});

});

function getComplainRegister(id, formUrl, actionParam, mode) {

	var divName = '.content-page';
	var requestData = {
		"mode" : mode,
		"id" : id
	};
	var ajaxResponse = doAjaxLoading(formUrl + '?' + actionParam, requestData,
			'html', divName);
	$(divName).removeClass('ajaxloader');
	$(divName).html(ajaxResponse);
	prepareTags();

}

//Form Validations
function confirmToProceed(element) {
	var reqData=__serializeForm("#frmComplainRegister");
	var errorList = [];
	var complaintType1 = $("#complaintType1").val();
	var complaintType2 = $("#complaintType2").val();
	
	var complainerName = $("#complainerName").val();
	var complainerMobile = $("#complainerMobile").val();
	var complainerAddress = $("#complainerAddress").val();
	var complaintDescription = $("#complaintDescription").val();
	var location = $("#location").val();
	var department = $("#department").val();
	var employee = $("#employee").val();
	var srtDepart='';
	var srtEmpl='';
	var rowCount = $('#billDetailTable tr').length-1;
	var cnt = 0;
	
	if(rowCount>=2){	
	$('.billDetailClass').each(function(i){
		//validation for duplicate record
		for(m=0; m<i; m++) {
			if($("#department"+m).val()==$("#department"+i).val() && $("#employee"+m).val()==$("#employee"+i).val()){
				cnt++;
			}
		}
		
		//if($("#department"+i).val()!="0" && $("#employee"+i).val()!="0"){
		if(($("#department"+i).val()!="0" && $("#department"+i).val()!="" && $("#department"+i).val()!= null && $("#department"+i).val()!= undefined  )
				&& ($("#employee"+i).val()!="0" && $("#employee"+i).val()!="" && $("#employee"+i).val()!= null && $("#employee"+i).val()!= undefined )){
				
		srtDepart=srtDepart+$("#department"+i).val()+',';
		srtEmpl=srtEmpl+$("#employee"+i).val()+',';
		}
		else{
			errorList.push(getLocalMessage("Please select Employee and Department"));
		}
		
	});
	}else{
		if(rowCount == 1){
		if( $("#department").val() != undefined){
			if( (($("#department").val()!="0" && $("#department").val()!="" && $("#department").val()!= null && $("#department").val()!= undefined  )
					&& ($("#employee").val()!="0" && $("#employee").val()!="" && $("#employee").val()!= null && $("#employee").val()!= undefined ))){
				srtDepart=srtDepart+$("#department").val()+',';
				srtEmpl=srtEmpl+$("#employee").val()+',';
				}
				else{
					errorList.push(getLocalMessage("Please select Employee and Department"));
				}
		}else{
			if( (($("#department0").val()!="0" && $("#department0").val()!="" && $("#department0").val()!= null && $("#department0").val()!= undefined  )
				&& ($("#employee0").val()!="0" && $("#employee0").val()!="" && $("#employee0").val()!= null && $("#employee0").val()!= undefined ))){
				srtDepart=srtDepart+$("#department0").val()+',';
				srtEmpl=srtEmpl+$("#employee0").val()+',';
				}
				else{
					errorList.push(getLocalMessage("Please select Employee and Department"));
				}
		}
		
		
		
		}
	}
	
	//validation for duplicate record
	if(cnt>=1) {
		errorList.push(getLocalMessage("Duplicate Record"));
	}

	$('#dept').val(srtDepart);
	$('#emp').val(srtEmpl);
	
	$('#dept').trigger("chosen:updated");
	$('#emp').trigger("chosen:updated");
	
	//errorList = validateComplainRegister(errorList);
	if (complaintType1 == "0" || complaintType1 == null) {
		errorList.push(getLocalMessage("Please select complaint type"));
	}
	if (complaintType2 == "0" || complaintType2 == null) {
		errorList.push(getLocalMessage("Please select complaint sub type"));
	}
	/*if (complainerName == "" || complainerName == null) {
		errorList.push(getLocalMessage("Please enter complainer name"));
	}*/
	/*if (complainerMobile == "" || complainerMobile == null) {
		errorList.push(getLocalMessage("Please enter complainer mobile no."));
	}*/
	
	/*if (complainerMobile == "" || complainerMobile == null || complainerMobile == undefined) {
		errorList.push("Please Enter Complainer Mobile No.");
	}*/
	
	if (complainerMobile.length < 10) {
		errorList.push("Please enter at least 10 digit mobile number");
	}
		
	if (complainerAddress == "" || complainerAddress == null) {
		errorList.push(getLocalMessage("Please enter complainer address"));
	}
	if (complaintDescription == "" || complaintDescription == null) {
		errorList.push(getLocalMessage("Please enter complaint description"));
	}
	if (location == "" || location == null) {
		errorList.push(getLocalMessage("Please enter location"));
	}
      
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
		return false;
	}
	else {
		return saveOrUpdateForm(element, "", 'ComplainRegister.html', 'saveform');
	}
}

function validateComplainRegister(errorList) {

	var department = $("#department").val();
	var location = $("#location").val();

	var complaintType1 = $("#complaintType1").val();
	var complaintType2 = $("#complaintType2").val();
	var complaintType3 = $("#complaintType3").val();
	var complaintType4 = $("#complaintType4").val();
	var complaintType5 = $("#complaintType5").val();

	var codWard1 = $("#codWard1").val();
	var codWard2 = $("#codWard2").val();
	var codWard3 = $("#codWard3").val();
	var codWard4 = $("#codWard4").val();
	var codWard5 = $("#codWard5").val();

	var complainerName = $("#complainerName").val();
	var complainerMobile = $("#complainerMobile").val();
	var complainerAddress = $("#complainerAddress").val();
	var complaintDescription = $("#complaintDescription").val();

	if (department == "0" || department == null) {
		errorList.push($('label[for="department"]').text());
	}
	if (location == "0" || location == null) {
		errorList.push($('label[for="location"]').text());
	}

	if (complaintType1 != undefined)
		if (complaintType1 == "0" || complaintType1 == null) {
			errorList.push($('label[for="complaintType1"]').text());
		}
	if (complaintType2 != undefined)
		if (complaintType2 == "0" || complaintType2 == null) {
			errorList.push($('label[for="complaintType2"]').text());
		}
	if (complaintType3 != undefined)
		if (complaintType3 == "0" || complaintType3 == null) {
			errorList.push($('label[for="complaintType3"]').text());
		}
	if (complaintType4 != undefined)
		if (complaintType4 == "0" || complaintType4 == null) {
			errorList.push($('label[for="complaintType4"]').text());
		}
	if (complaintType5 != undefined)
		if (complaintType5 == "0" || complaintType5 == null) {
			errorList.push($('label[for="complaintType5"]').text());
		}

	if (codWard1 != undefined)
		if (codWard1 == "0" || codWard1 == null) {
			errorList.push($('label[for="codWard1"]').text());
		}
	if (codWard2 != undefined)
		if (codWard2 == "0" || codWard2 == null) {
			errorList.push($('label[for="codWard2"]').text());
		}
	if (codWard3 != undefined)
		if (codWard3 == "0" || codWard3 == null) {
			errorList.push($('label[for="codWard3"]').text());
		}
	if (codWard4 != undefined)
		if (codWard4 == "0" || codWard4 == null) {
			errorList.push($('label[for="codWard4"]').text());
		}
	if (codWard5 != undefined)
		if (codWard5 == "0" || codWard5 == null) {

		}

	/*if (complainerName == "" || complainerName == null) {
		errorList.push($('label[for="complainerName"]').text());
	}*/
	/*if (complainerMobile == "" || complainerMobile == null) {
		errorList.push($('label[for="complainerMobile"]').text());
	}*/
	if (complainerAddress == "" || complainerAddress == null) {
		errorList.push($('label[for="complainerAddress"]').text());
	}
	if (complaintDescription == "" || complaintDescription == null) {
		errorList.push($('label[for="complaintDescription"]').text());
	}

	return errorList;

}



function getEmployeeByDept(obj) {

	$('#employee').html('');
	
	//$("#employee option").remove(); 
	//$("#employee").empty();
	//$('#employee').append($("<option></option>").attr("value", "0").text(getLocalMessage('selectdropdown')));
	var url = "ComplainRegister.html?getEmployeeByDept";
	var deptId;
	var flag=false;
	if(obj==0){
		deptId=$('#designation').val();
		if(deptId==undefined){
			deptId=$('#designation'+obj).val();
			flag=true;
		}
	}
	else{
	 deptId=$('#designation'+obj).val();
	}
	if(obj==0 && flag==false){
		$('#employee').find('option').remove().end().append('<option value="0"></option>');
		$("#employee").trigger("chosen:updated"); 
	}
	else{
		$('#employee'+obj).find('option').remove().end().append('<option value="0"></option>');
		$("#employee"+obj).trigger("chosen:updated");
	}
	$('#employee'+obj).html('');
	var requestData = "deptId=" + deptId;
	var response = __doAjaxRequest(url, 'POST', requestData, false, 'json');
	//$('#employee'+obj).text('');
	//$('#employee'+obj).val('');
	//$("#employee option").remove();

	$.each(response, function(key, value) {
	 if(obj==0 && flag==false){
		$('#employee').append($('<option>', {value:value.empId, text:value.empname}));
		$('#employee').trigger("chosen:updated");
	 }
	 else{
		$('#employee'+obj).append($('<option>', {value:value.empId, text:value.empname}));
		$("#employee"+obj).trigger("chosen:updated");
	 }
	});

}		




function resetForm() {


	addComplainRegister('ComplainRegister.html', 'ADD', 'A');
}

function getLable(lable) {
	return $('label[for=' + lable + ']').text();
}



$(function() {

	$("#billDetailTable").unbind('click').on("click",'.addButton11',function(e) {

	var errorList=[];
	$('.billDetailClass').each(function(i) {
		var rowCount = $('#billDetailTable tr').length;
		if(rowCount==2){
			var department =$('#department').val();
			var employee=$('#employee').val();
			if(department=="" || department=="0"){
				errorList.push(getLocalMessage("complaintRegister.dept"));
			}
			if(employee=="" || employee=="0"){
				errorList.push(getLocalMessage("complaintRegister.employee"));
			}
		}
		else{
			var department =$('#department'+i).val();
			var employee=$('#employee'+i).val();
			if(department==undefined || department=="0"){
				errorList.push(getLocalMessage("complaintRegister.dept"));
			}
			if(employee==undefined || employee=="0"){
				errorList.push(getLocalMessage("complaintRegister.employee"));
			}
		}			
	});
	if(errorList.length > 0){
		displayErrorsOnPage(errorList);
	}
	else{
		var content = $(this).closest('#billDetailTable tr').clone();
		$(this).closest("#billDetailTable").append(content);
		content.find("select").attr("value", "");
		content.find("input:text").val("");
		content.find("input:checkbox").attr("checked", false);
		content.find('div.chosen-container').remove();
		content.find('label').closest('.error').remove(); //for removal duplicate
		reOrderTableIdSequenceVish();
		e.preventDefault();
		content.find("select:eq(0)").chosen().trigger("chosen:updated");
		}
	});
	//to delete row
	$("#billDetailTable").on("click", '.delButton', function(e) {
		var rowCount = $('#billDetailTable tr').length;
		if (rowCount <= 2) {
			return false;
		}
		$(this).closest('#billDetailTable tr').remove();

		var billAmountTotal = 0;
		var rowCount = $('#billDetailTable tr').length;
		reOrderTableIdSequenceVish();
		e.preventDefault();
	});
	
});
 	
	function reOrderTableIdSequenceVish(){
		$('.billDetailClass').each(function(i) {
			$(this).find("select:eq(0)").attr("id",	"department" + i);
			$(this).find("select:eq(1)").attr("id",	"designation" + i);
			$(this).find("select:eq(2)").attr("id",	"employee" + i);
			$(this).find("select:eq(0)").attr("name","complainRegisterDTO["+ i + "].department");
			$(this).find("select:eq(1)").attr("name","complainRegisterDTO["+ i + "].designation");
			$(this).find("select:eq(2)").attr("name","complainRegisterDTO["+ i + "].employee");
			$(this).find("select:eq(0)").attr("onchange", "getEmployeeByDept(" + i + ")");
			$(this).find("select:eq(1)").attr("onchange", "getEmployeeByDept(" + i + ")");
		});
	} 	
	
	
 	
