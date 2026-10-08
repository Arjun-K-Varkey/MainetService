$(document)
		.ready(
				function() {
					
						
						showSingleMultiple();
						
						
						
						$('#datatables').DataTable({
				    		"oLanguage": { "sSearch": "" } ,
				    		"aLengthMenu": [ [5, 10, 15, -1], [5, 10, 15, "All"] ],
				    	    "iDisplayLength" : 5, 
				    	    "bInfo" : true,
				    	    "lengthChange": true,
				    	    "bPaginate": true,
				    	    "bFilter": true
				    	    });
						
						$('#selectall').click(function(event) {
							if (this.checked) {
								$('.checkall').each(function() {
									this.checked = true;
								});
							} else {
								$('.checkall').each(function() {
									this.checked = false;

								});
							}
						});
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
				});




function serachProperty() {
	showloader(true);
		/*  $('#datatables').DataTable().destroy();
	
		var requestData = $('#propertyBillGeneration').serialize();*/
		
	var formAction	=	$('#propertyBillprinting').attr('action');
		var url=formAction+'?SearchPropNo';
		$('#propertyBillprinting').attr('action', url);

		$('#propertyBillprinting').submit();
		
		/*
		var ajaxResponse = __doAjaxRequest(
				formAction+'?SearchPropNo', 'POST',
				requestData, false, 'html');

		$(".content").html(ajaxResponse);
		return false;*/
}

function printBill(element){	
	showloader(true);
	setTimeout(function(){startPrint(element)},2);
}

function startPrint(element){
	$('#datatables').DataTable().destroy();
	var someObj={};
	someObj.checked=[];
	
	$("input:checkbox").each(function(){
	    var $this = $(this);

	    if($this.is(":checked")){
	    	someObj.checked.push($this.attr("id"));
	    }else{
	        
	    }
	});
	
	    var checkedValue = someObj.checked.join();
	    
	    $("#propertyBillprinting :input[value!='on']").serialize();
	    
	    
	    var requestData = "checkedValue="+checkedValue;
		var url = "PropertyBillPrinting.html?PropertyBillPrinting";
		var ajaxResponse = __doAjaxRequest(url, 'post',requestData , false);

		var divName = formDivName;
		$('.content').removeClass('ajaxloader');
		$(divName).html(ajaxResponse);
		prepareTags();
}
function resetBillPrintForm(element){
	//$("#propertyBillprinting").submit();
	value = "PropertyBillPrinting.html";
	$("#postMethodForm").prop('action', '');
	$("#postMethodForm").prop('action', value);
	$("#postMethodForm").submit();
	

}



function showSingleMultiple(){
	
	
	$('#selectall').prop('checked', true);
	$('.checkall').each(function() {
		this.checked = true;
	});

	
	
	
	
	
	
	
	
	
	
	if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="SM"){
		$('.PropDetail').show();
		$('.propLable').show();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.usageType').hide();
		$('.searchBtn').show();
		$('.sectionSeperator').hide();
		
		$('#assWard1').val('');
		$('#assWard2').val('');
		$('#locId').val('');
		
		
	}
	else if($("input[name='specialNotGenSearchDto.specNotSearchType']:checked").val()=="AL"){
		$('.PropDetail').hide();
		$('.wardZone').show();
		$('.Loc').show();
		$('.usageType').show();
		$('.searchBtn').show();
		$('.sectionSeperator').show();
		$('.PropDetail').val('');

		$('#propertyNo').val('');
		$('#oldPropertyNo').val('');
	}else{
		$('.PropDetail').hide();
		$('.wardZone').hide();
		$('.Loc').hide();
		$('.propLable').hide();
		$('.usageType').hide();
		$('.searchBtn').hide();
		$('.sectionSeperator').hide();
	}
	
}

function showErrorOnPage(errorList){
	var errMsg = '<ul>';
	var errMsg = '<button type="button" class="close" aria-label="Close" src="css/images/close.png" onclick="closeOutErrBox()"><span aria-hidden="true">&times;</span></button><ul>';
	$.each(errorList, function(index) {
		errMsg += '<li><i class="fa fa-exclamation-circle"></i> &nbsp;' + errorList[index] + '</li>';
	});

	errMsg += '</ul>';
	$("#errorDiv").html(errMsg);
	$("#errorDiv").show();
	$("html, body").animate({ scrollTop: 0 }, "slow");
	return false;
}