
function addUnitRow(count){
	var errorList = [];
	errorList = validateUnitSpecificInfoTable(errorList);

		if (errorList.length == 0) {
			var factPrex=$("#factPref"+count).val();
			var content =	$("#unitSpecificInfoTable"+factPrex).find('tr:eq(1)').clone();
 			$("#unitSpecificInfoTable"+factPrex).append(content);	
			content.find("select").val('0');
			content.find("input:hidden:eq(2)").val('0');
			//newly added
			content.find("input:text").val("");
			//
			reorderfactor('.specFact','.eachFact');
			return false;
		}
		else 
		{
		displayErrorsOnPage(errorList);
		}
	
}

function addRoomDetails(count,element) {

	$('#countOfRow').val(count);
	var formName = $('#formNamee').val();
	if(formName=="DES"){
		var theForm = '#DataEntrySuite';
	}else if(formName=="NPR"){
		var theForm = '#NewPropertyRegistration';
	}	
	var action=$(theForm).attr('action');
	
	var requestData = {};
	requestData = __serializeForm(theForm);
	var URL = action+'?addRoomDetails';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');

	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);

}

function saveRoomData(element){

	var errorList = [];
	errorList=validateRoomDetails(element,errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);	
	}else{
		var formName = $('#formCodes').val();
		var ownerType=$('#ownerType').val();
		if(formName=="DES"){
			var theForm = '#DataEntrySuite';
		}else if(formName=="NPR"){
			var theForm = '#NewPropertyRegistration';
		}	
		var action=$(theForm).attr('action');		
		var requestData = {};
		requestData = __serializeForm(theForm);
		var URL = action+'?saveRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');

		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		
		//set owner details
		if (ownerType != "") {
			var data1 = {
				"ownershipType" : ownerType
			};
			var URL1 = 'DataEntrySuite.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
		}
	}	
}

function backFromRoomdetails(element){

	var formName = $('#formCodes').val();
	var ownerType=$('#ownerType').val();
	if(formName=="DES"){
		var theForm = '#DataEntrySuite';
	}else if(formName=="NPR"){
		var theForm = '#NewPropertyRegistration';
	}	
	var action=$(theForm).attr('action');	
	var requestData = {};
	var URL = action+'?saveRoomDetails';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');

	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	
	// set owner details
	if (ownerType != "") {
		var data1 = {
			"ownershipType" : ownerType
		};
		var URL1 = 'DataEntrySuite.html?getOwnershipTypeDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		$("#owner").show();
	}
}

function validateRoomDetails(element, errorList) {
	$('.thirdRowRoomDetails').each(function(i) {

		var rowCount = $('#roomDetailstable tr').length;
		if (rowCount <= 2) {
			var roomType = $('#roomType').val();
		}else{
			var roomType = $('#roomType' + i).val();
		}		
		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		if (errorList.length <= 0) {
			if (roomType == "") {
				errorList.push(getLocalMessage("property.valid.roomType"));
			}
			if (roomLength == "") {
				errorList.push(getLocalMessage("property.valid.roomLength"));
			}
			if (roomWidth == "") {
				errorList.push(getLocalMessage("property.valid.roomWidth"));
			}
		}else{
			return errorList;
		}
	});
	return errorList;
}

$("#roomDetailstable").on('click','.addRoomDetails',function(){
	
	var countOfRow=$("#countOfRow").val();
	var content =	$("#roomDetailstable").find('tr:eq(1)').clone();
	$(this).closest("#roomDetailstable").append(content);
	content.find("select").attr("value", "");
	content.find("input:text").val("");
	reOrderRoomTableIdSequence(countOfRow);
	return false;
});

// to delete row
$("#roomDetailstable").on("click", '.delButton', function(e) {

	var errorList = [];
	var countOfRow=$("#countOfRow").val();
	var rowCount = $('#roomDetailstable tr').length;
	if (rowCount <= 2) {
		
		errorList.push(getLocalMessage('property.valid.firstRow'));
		displayErrorsOnPage(errorList);		
		return false;
	} else {
		
		var index= $(this).closest('#roomDetailstable tr').index();
		var formName = $('#formCodes').val();
		if(formName=="DES"){
			var theForm = '#DataEntrySuite';
		}else if(formName=="NPR"){
			var theForm = '#NewPropertyRegistration';
		}	
		var action=$(theForm).attr('action');			
		var requestData = {
			"countOfRow" : countOfRow,
			"index":index
		};
		//requestData = __serializeForm(theForm);
		$(this).closest('#roomDetailstable tr').remove();
		var URL = action+'?removeRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json');			
		reOrderRoomTableIdSequence(countOfRow);
	}
});

function reOrderRoomTableIdSequence(countOfRow) {

	$('.thirdRowRoomDetails').each(function(i) {

			var j=countOfRow;
						
			$(this).find("input:text:eq(0)").attr("id", "roomNo" + i);
			$(this).find("select:eq(0)").attr("id", "roomType" + i);
			$(this).find("input:text:eq(1)").attr("id", "roomLength" + i);
			$(this).find("input:text:eq(2)").attr("id", "roomWidth" + i);
			$(this).find("input:text:eq(3)").attr("id", "roomArea" + i);
			
			$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomNo");
			$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomType");
			$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomLength");
			$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomWidth");
			$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList[" + j + "].roomDetailsDtoList[" + i + "].roomArea");
			
			var incr=i+1;
			$("#roomNo"+i).val(incr);
			
		});
}

function calculateArea() {

	$('.thirdRowRoomDetails').each(function(i) {

		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		if (roomLength != "" && roomWidth != "") {
			var num =roomLength * roomWidth;
			var result = num.toFixed(2);
			$('#roomArea' + i).val(result);
		} else {
			$('#roomArea' + i).val();
		}
	});
}

$(document).on('click', '.unitSpecificRem', function () {
	
	//To remove entry from database
	var inputId=this.id;	
	var left_text = inputId.lastIndexOf("_");
	var deletedUnitRow = inputId.substring(left_text+1);
	var data = {"deletedUnitRow" : deletedUnitRow};
	var URL = 'NewPropertyRegistration.html?deleteUnitSpecificInfo';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	reorderfactor('.specFact','.eachFact');
	var id=$(this).closest('table').attr('id');	
	if($("#"+id+" tr").length >2) {
    $(this).closest('tr').remove();
    reorderfactor('.specFact','.eachFact');
	}
	
});

function validateUnitSpecificInfoTable(errorList){

		$('.specFact').each(function(i) {
	
			var unit=  $("#unitNoFact"+i).val();			
			var factor=  $("#assfFactorValueId"+i).val();
			var level = i+1;
			
			 if(unit=='0' || unit == undefined){
				  errorList.push(getLocalMessage('unit.selectUnitNo')+" " +level); 
			 } 
			 
			 if(factor=='0' || factor == undefined){
				  errorList.push(getLocalMessage('unit.selectfactor')+" " +level); 
			 } 
		});
		/*$('.factorQes').each(function(i){
			var level = i+1;
			var selectFactor = $.trim($('input[name="provisionalAssesmentMstDto.proAssfactor['+i+']"]:checked').val());
			if(selectFactor==0 || selectFactor=="")
			errorList.push(getLocalMessage("property.factorStatus")+" "+level);
			});*/
		
		return errorList;
}


function enabledisable(event, currentRow){
	 $(".error-div").hide();
	var errorList = [];
	$('.specFact').each(function(i) {
	    			var unit=  $("#unitNoFact"+i).val();
	    			var factor=  $("#assfFactorValueId"+i).val();
	    			var level = i+1;
	    			
	    			 if(unit=='0' || unit == undefined){
	    				  errorList.push(getLocalMessage('unit.selectUnitNo')+" " +level); 
	    			 } 
	    			 
	    			 if(factor=='0' || factor == undefined){
	    				  errorList.push(getLocalMessage('unit.selectfactor')+" " +level); 
	    			 } 
	    			
	    			 if(errorList.length == 0) {
	    				 
	    				if(currentRow != i && (event.value ==factor && $("#unitNoFact"+currentRow).val() == unit))
		  	    		{	
		  	    				errorList.push(getLocalMessage('unit.Duplicatefactorvalue'));
		  	    				$("#assfFactorValueId"+currentRow).val("0");		
		  	    				displayErrorsOnPage(errorList);
		  	    				return false;
		  	    		} 
	    		      }else{
	    		    	  $("#assfFactorValueId"+i).val('0');
	    		    	  displayErrorsOnPage(errorList);
	    					 return false;  
	    		      }
	});
}

function resetFactorValue(obj,index){
	 $("#assfFactorValueId"+index).val("0");	
}
