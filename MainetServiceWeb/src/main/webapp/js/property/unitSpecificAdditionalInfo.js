
function addUnitRow(count){
	
	var errorList = [];
	errorList = validateUnitSpecificInfoTable(errorList);
	
		if (errorList.length == 0) {
			var factPrex=$("#factPref"+count).val();
			var factval=$("#unitNoFact"+count).val();
			var content =	$("#unitSpecificInfoTable"+factPrex).find('tr:eq(1)').clone();
 			$("#unitSpecificInfoTable"+factPrex).append(content);	
			content.find("select").val('0');
			content.find("input:hidden:eq(2)").val('0');
			content.find("input:text:eq(0)").val('');
			content.find("input:text:eq(1)").val('');						
			content.find("input:text:eq(2)").val('0.0');						
			var action="SelfAssessmentForm.html";                        
		        var requestData = {
		            "countOfRow" : factval,
		            "index":count
		        };    
		        //$(this).closest('#roomDetailstable tr').remove();
		        var URL = action+'?addUnitRow';
		        var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json'); 
			reorderfactor('.specFact','.eachFact');
			//count.preventDefault();
			return false;
		}
		else 
		{
		displayErrorsOnPage(errorList);
		}
	
}

$(document).on('click', '.unitSpecificRem', function () {
	
	var errorList = [];
	//To remove entry from database
	var inputId=this.id;	
	var left_text = inputId.lastIndexOf("_");
	var deletedUnitRow = inputId.substring(left_text+1);
	
	var id=$(this).closest('table').attr('id');	
	if($("#"+id+" tr").length == 2){
		errorList.push(getLocalMessage('unit.Firstrowcannotberemove'));
		displayErrorsOnPage(errorList);
		return false;
	}
	var data = {"deletedUnitRow" : deletedUnitRow};
	var URL = 'SelfAssessmentForm.html?deleteUnitSpecificInfo';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	reorderfactor('.specFact','.eachFact');	  	    				
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
