var yearList;
$(function(){
	
	$('.secondUnitRow').removeClass('in');
	$('.secondUnitRow:last').addClass('in');

	$('.firstUnitRow').each(function(i){	
		$("#year"+i).prop("disabled", true); 
		
	});
	
	
	//$('.hideDetails').removeClass('in'); 
	 $(".datepicker2").datepicker({
	        dateFormat: 'dd/mm/yy',		
			changeMonth: true,
			changeYear: true
	});
	 
	/*To add new Row into table*/ 
	$("#unitDetailTable").on('click','.addCF',function(){
			
			var errorList = [];			
			var rowCount = $('#unitDetailTable tr:last').index()/2 ;				
			errorList = validateUnitDetailTable(errorList);				
			if (errorList.length == 0) {	
				
			$(".datepicker2").removeClass("hasDatepicker");	
			$(".lessthancurrdate").removeClass("hasDatepicker");	
				var content =	$("#unitDetailTable").find('tr:eq(1),tr:eq(2)').clone();
				$('.secondUnitRow').removeClass('in');
				$("#unitDetailTable").append(content);
				$('.secondUnitRow:last').addClass('in');
												
				content.find("input:text").val('');
				content.find("select").val('0');
				content.find("input:hidden:eq(0)").val('0');

				$('.error-div').hide();
				datePickerLogic();
				reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');  // reorder id and Path after Addition of new Row
				if($("#checkDetail").prop('checked')==true){   // after tick unable year 
						$("#unitDetailTable tr:last").prev().find("select:eq(0)").prop('disabled',false).removeClass("disabled").addClass("mandColorClass");
				}
								
				var hiddenOther=$('#hiddenOther').val();
				content.find("select:eq(1)").val(hiddenOther);				
				return false;
			}							
			else {
				showVendorError(errorList);
		}			
	}); 

$(".actualRentToolTip :input").tooltip({
    position: "center right",
    offset: [-2, 10],
    effect: "fade",
    opacity: 0.7
});	 
	

	//To delete Row From the table 
	$("#unitDetailTable").on('click','.remCF',function(){
		
		//To remove entry from dto
		var inputId=this.id;		
		var left_text = inputId.lastIndexOf("_");
		var deletedRowCount = inputId.substring(left_text+1);		
		var data = {"deletedRowCount" : deletedRowCount};
		var URL = 'SelfAssessmentForm.html?deleteUnitTableRow';
		
		
		//To delete Unit No from unit specific additional information 
		var unitCount= $("#unitNo"+deletedRowCount).val();		
		var a=0; 
		var tableLength = $('#unitDetailTable tr').length;
		for(var j=1;j<tableLength;j++) // table tr length 
		{
			var unitNoFact=$("#unitNo"+j).val();   			
			if(unitCount==unitNoFact){
				a++;
			}
		}
		if(a==1){
			$(".selectUnit option[value="+unitCount+"]").remove();
		}

		
		//remove row From UI
		var index = $(this).closest("tr").index();
		//if(($("#unitDetailTable tr").length)>2 && (index!=1)) {
		if($("#unitDetailTable tr").length > 2){
			var tr = $(this).closest("tr");
		    tr.add(tr.next()).remove();
		    $('.secondUnitRow:last').addClass('in');
			var returnData = __doAjaxRequest(URL, 'POST', data, false);
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			
		}
		else {	
			var errorList = [];
			
			errorList.push(getLocalMessage("unit.Firstrowcannotberemove"));
			showVendorError(errorList);
		}	
	});
		
	$('body').on('click', 'tr.secondUnitRow', function() {
	      $('tr.in').removeClass('active');
	});
	
	$('body').on('click', 'a.click_advance', function() {	
		$("i", this).toggleClass("fa-caret-up fa-caret-down");
	}); 
	
});


function datePickerLogic(){
	var yearOfAcq =$("#proAssAcqDate").val();
	 $(".datepicker2").datepicker({
	        dateFormat: 'dd/mm/yy',		
			changeMonth: true,
			changeYear: true,
			maxDate:yearOfAcq
		});
	 
	 $('.lessthancurrdate').datepicker({
			dateFormat: 'dd/mm/yy',	
			changeMonth: true,
			changeYear: true,
			maxDate: '-0d',
			yearRange: "-100:-0"
		});
	//D#139215
	 $(".datepicker").datepicker({
	        dateFormat: 'dd/mm/yy',		
			changeMonth: true,
			changeYear: true,
		});
}


/*function occupancyTypeChange(id){
	if(($('option:selected', $("#assdOccupancyType"+id)).attr('code')) == 'TAN'){
		$("#proAssdAnnualRent"+id).prop('disabled', false);;
		$("#proAssdAnnualRent"+id).prop('required',true);
		
	}else{
		$("#proAssdAnnualRent"+id).val('');
		$("#proAssdAnnualRent"+id).prop('disabled', true);;
		$("#proAssdAnnualRent"+id).prop('required',false);
	}
}*/

//on edit
/*function occupancyTypeChange1(){
	$('.secondUnitRow').each(function(j){
		if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
			$("#proAssdAnnualRent"+j).prop('disabled', false);;
			$("#proAssdAnnualRent"+j).prop('required',true);
			
		}else{
			$("#proAssdAnnualRent"+j).val('');
			$("#proAssdAnnualRent"+j).prop('disabled', true);;
			$("#proAssdAnnualRent"+j).prop('required',false);
		}
	});
}*/


//validation of unit details table
function validateUnitDetailTable(errorList){
	
	var serviceShortCode = $('#serviceShortCode').val();
	var draftApplication=$("#draftApplication").val();
	var constructDateFlag = $("#ConstructFlag").val();
	var a = a;
	var rowCount = $('#unitDetailTable tr').length;
	var billMethod=$('#billMethod option:selected').attr('code');	
	//var noOfDaysFlag =$("#noOfDaysEditableFlag").val();
	var duplicateFloor = {};
	var duplicateFloorNo = false;
	var duplicateFlat = {};
	var duplicateFlatNo = false;
	var duplicateElectUnit = {};
	var duplicateElectUnitNo = false;
	var duplicateElectCons = {};
	var duplicateElectConsNo = false;
	var duplicateElectMeter = {};
	var duplicateElectMeterNo = false;

    var floorflat=[];
    var assNo =$("#assNo").val() ;
    var flatNoList = [];
    if (billMethod == 'I' && 'CIA' == serviceShortCode){
		var data = {
			"assNo" : assNo
		};
		var URL = 'ChangeInAssessmentForm.html?getflatvalidation';
		flatNoList = __doAjaxRequest(URL, 'POST', data, false);
	}
    
	if(rowCount==2){
		if('CIA' == serviceShortCode){
			reorderFirstRowCIA();			
		}else{
			reorderFirstRow();			
		}
	}
	$('.firstUnitRow').each(function(i) {

		if(rowCount<=2)
			{			
			var year =$("#hiddenYear0").val();
//			var unitType=  $("#assdUnitTypeId").val();
			var floorNo=  $("#assdFloorNo").val();
			var occupancyType =$("#assdOccupancyType").val();
			var occupierName = $("#occupierName0").val();
			var actualRent= $("#actualRent0").val();
			//var =  $("#yearOfConstruc").val();
			//var firstAssesmentDate=  $("#firstAssesmentDate").val();
    		var legal=  $("#legal").val();
			var usageType1=  $("#assdUsagetype1").val();
			var usageType2=  $("#assdUsagetype2").val();
			var usageType3=  $("#assdUsagetype3").val();
			var usageType4=  $("#assdUsagetype4").val();
			var usageType5=  $("#assdUsagetype5").val();
			var ConstruType=  $("#assdConstruType").val();
			var taxableArea=  $("#taxableArea0").val();	
			var carpetArea=  $("#carpetArea0").val();	
			var flatNo=  $("#flatNo0").val();	
			var electricBillUnitNo =  $("#electricBillUnitNo"+i).val();
			var electricConsumerNo =  $("#electricConsumerNo"+i).val();
			var electricMeterNo =  $("#electricMeterNo"+i).val();
			var areaOrRvBasedChange=  $("#areaOrRvBasedChange").val();
			var rv =  $("#assdRv0").val();	
			var level = 1;
			}
		else{
		var year =$("#hiddenYear"+i).val();	
//		var unitType=  $("#assdUnitTypeId"+i).val();
		var floorNo=  $("#assdFloorNo"+i).val();
	//	var yearOfConstruc=  $("#yearOfConstruc"+i).val();
		//var firstAssesmentDate=  $("#firstAssesmentDate"+i).val();
        var legal=  $("#legal"+i).val();
        var occupancyType =$("#assdOccupancyType"+i).val();
		var occupierName = $("#occupierName"+i).val();
		var actualRent= $("#actualRent"+i).val();
		
		var utp=i;
		if(i>0){
		utp=i*6;
		}
		var usageType1=  $("#assdUsagetype"+utp).val();
		var usageType2=  $("#assdUsagetype"+(utp+1)).val();
		var usageType3=  $("#assdUsagetype"+(utp+2)).val();
		var usageType4=  $("#assdUsagetype"+(utp+3)).val();
		var usageType5=  $("#assdUsagetype"+(utp+4)).val();
		var ConstruType=  $("#assdConstruType"+i).val();
		var taxableArea=  $("#taxableArea"+i).val();
		var carpetArea=  $("#carpetArea"+i).val();
		var flatNo=  $("#flatNo"+i).val();
		var electricBillUnitNo =  $("#electricBillUnitNo"+i).val();
		var electricConsumerNo =  $("#electricConsumerNo"+i).val();
		var electricMeterNo =  $("#electricMeterNo"+i).val();
		var areaOrRvBasedChange =  $("#areaOrRvBasedChange"+i).val();
		var rv =  $("#assdRv"+i).val();
		//floorflat.push(floorNo+"."+flatNo);
		/*var constructDateFlag = $("#ConstructFlag").val();*/
		var level = i+1;
		}
		
		 if(year=='' || year == undefined || year == '0'){			 
			 errorList.push(getLocalMessage('unitdetails.selectYearlevel')+" " +level); 
		 }
		/* if(unitType=='0' || unitType == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectUnitType')+" " +level); 
		 } */		 
		 if(floorNo=='0' || floorNo == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectFloorNo')+ " " +level); 
		 }
		 if (duplicateFlat[flatNo]){
			    duplicateFlatNo = true;
			    return false;
		} else if(("" != electricBillUnitNo && electricBillUnitNo != undefined && electricBillUnitNo != null) && duplicateElectUnit[electricBillUnitNo]){
			 duplicateElectUnitNo = true;
			    return false;
		} else if(("" != electricConsumerNo && electricConsumerNo != undefined && electricConsumerNo != null) && duplicateElectCons[electricConsumerNo]){
			    duplicateElectConsNo = true;
			    return false;
		} else if(("" != electricMeterNo && electricMeterNo != undefined && electricMeterNo != null) && duplicateElectMeter[electricMeterNo]){
			    duplicateElectMeterNo = true;
			    return false;
		}
		 else if(billMethod == 'I' && 'CIA' == serviceShortCode && flatNo!=null && rowCount > 2){
			 	if(i>0 && flatNoList.includes(flatNo)){
			 		errorList.push(getLocalMessage("property.bif.flat.duplicate.valid"));
			 	}
			}
			duplicateFlat[flatNo] = true; 
			duplicateElectUnit[electricBillUnitNo] = true; 
			duplicateElectCons[electricConsumerNo] = true; 
			duplicateElectMeter[electricMeterNo] = true; 
						
			if(constructDateFlag != null && constructDateFlag == 'Y'){
				if(occupancyType=='0' || occupancyType == undefined){
					  errorList.push(getLocalMessage('unitdetails.selectOccupacyType')+" " +level); 
				 }
				 if(occupierName=='' || occupierName == undefined){
						  errorList.push(getLocalMessage('unitdetails.selectOccupierName')+ " " +level); 
				 } 
			/*	 if(actualRent=='' || actualRent == undefined){
					  errorList.push(getLocalMessage('unitdetails.selectActualRent')+ " " +level); 
				 } */
				 }
	/*	 if(constructDateFlag != null && constructDateFlag == 'Y'){
			 if(yearOfConstruc=='' || yearOfConstruc == undefined){
				  errorList.push(getLocalMessage('unitdetails.selectConstructionCompletionDate')+ " " +level); 
			 } 
		 }
		*/
		 /*if(firstAssesmentDate=='' || firstAssesmentDate == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectFirstAssessmentDate')+ " " +level); 
		 }*/ 
		/*if(noOfDaysFlag == "N"){
			 if(firstAssesmentDate=='' || firstAssesmentDate == undefined){
				  errorList.push(getLocalMessage('unitdetails.selectFirstAssessmentDate')+ " " +level); 
			 } 
		 }*/
		 
		 if(usageType1=='0' || usageType1 == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectUsageType')+" " +level); 
		 } 
		 if((usageType2 != undefined) && usageType2=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 } 
		 if((usageType3 != undefined) && usageType3=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 } 
		 if((usageType4 != undefined) && usageType4=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 }
		 if((usageType5 != undefined) && usageType5=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubUsageType')+" " +level); 
		 }
		 if(ConstruType=='0' || ConstruType == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectConstructionType')+" "+level); 
		 }
		 if (flatNo == '' || flatNo == undefined) {
			errorList.push(getLocalMessage('unitdetails.enter.flatNoValid')+ " " + level);
		 }
		 //if(constructDateFlag != null && constructDateFlag == 'Y'){
		 //if(taxableArea=='' || taxableArea == undefined )
		//	  errorList.push(getLocalMessage('unitdetails.selectTaxableArea')+" " +level);
		if((draftApplication != 'Y') && ('CIA' == serviceShortCode)){
			if((taxableArea=='' || taxableArea == undefined || taxableArea == null | taxableArea == '0.00') && 
				(rv == '' || rv == undefined || rv == null || rv == '0')){
			  errorList.push(getLocalMessage('unitdetails.valid.areaOrRv')+" " +level);				
			}
			if(areaOrRvBasedChange =='' || areaOrRvBasedChange == undefined || areaOrRvBasedChange == null){
			  errorList.push(getLocalMessage('unitdetails.select.areaOrRv')+" "+level); 
		 }
		}else{
			if(carpetArea=='' || carpetArea == undefined || carpetArea <= 0)
			  errorList.push(getLocalMessage('property.add.roomdetails')+" " +level);
		} 		
//		 if(carpetArea<=0)
//			 errorList.push(getLocalMessage('unitdetails.selectValidCarpetArea')+" " +level);
//		 if(taxableArea<=0)
//			 errorList.push(getLocalMessage('unitdetails.selectValidTaxableArea')+" " +level);
 		 if(legal =='' || legal == undefined){
			  errorList.push(getLocalMessage('unitdetails.select.legal.illegal')+" "+level); 
		 }
		 
		 //}		 	 
	});
	/*if(floorflat.length > 1){
		// var uniquefloorflat =  $.unique(floorflat);
		 if(floorflat.length !== new Set(floorflat).size){
			 errorList.push(getLocalMessage("property.flat.duplicate.valid"));
		 }
	 }*/
	if(duplicateFlatNo){
			errorList.push(getLocalMessage("property.bif.flat.duplicate.valid"));
		}
	if(duplicateElectUnitNo){
		errorList.push(getLocalMessage("property.bif.electno.duplicate.valid"));
	}
	if(duplicateElectConsNo){
		errorList.push(getLocalMessage("property.bif.electConsNo.duplicate.valid"));
	}
	if(duplicateElectMeterNo){
		errorList.push(getLocalMessage("property.bif.electMeterNo.duplicate.valid"));
	}
	$('.secondUnitRow').each(function(j) {	
		if(rowCount<=3){
//			var roadFactor =$("#assdRoadFactor").val();
			var occupancyType =$("#assdOccupancyType").val();
			var natureOfProperty1=  $("#natureOfProperty1").val();
			var natureOfProperty2=  $("#natureOfProperty2").val();
			var natureOfProperty3=  $("#natureOfProperty3").val();
			var natureOfProperty4=  $("#natureOfProperty4").val();
			var natureOfProperty5=  $("#natureOfProperty5").val();
			var occupieremail = $("#occupierEmail").val();
            var occupierMobNo = $("#occupierMobNo").val();
			var level=1;
		}
		else{
//			var roadFactor =$("#assdRoadFactor"+j).val();
			var occupieremail = $("#occupierEmail"+j).val();
            var occupierMobNo = $("#occupierMobNo"+j).val();
			var occupancyType =$("#assdOccupancyType"+j).val();
			var utp=j;
			if(j>0){
			utp=j*6;
			}
			var natureOfProperty1=  $("#natureOfProperty"+utp).val();
			var natureOfProperty2=  $("#natureOfProperty"+(utp+1)).val();
			var natureOfProperty3=  $("#natureOfProperty"+(utp+2)).val();
			var natureOfProperty4=  $("#natureOfProperty"+(utp+3)).val();
			var natureOfProperty5=  $("#natureOfProperty"+(utp+4)).val();
			var level=j+1;
		}
		
		/*if(roadFactor=='0' || roadFactor == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectRoadFactor')+" "+level); 
		 }*/
		if(constructDateFlag != null && constructDateFlag == 'Y'){
		/*if(occupancyType=='0' || occupancyType == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectOccupacyType')+" " +level); 
		 }*/
		}
		/*if(constructDateFlag != null && constructDateFlag == 'Y'){*/
		/*if(natureOfProperty1=='0' || natureOfProperty1 == undefined){
			  errorList.push(getLocalMessage('unitdetails.selectNatureOfProperty')+" " +level); 
		 } */
		 if((natureOfProperty2 != undefined) && natureOfProperty2=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubNatureOfProperty')+" " +level); 
		 } 
		 if((natureOfProperty3 != undefined) && natureOfProperty3=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubNatureOfProperty')+" " +level); 
		 } 
		 if((natureOfProperty4 != undefined) && natureOfProperty4=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubNatureOfProperty')+" " +level); 
		 }
		 if((natureOfProperty5 != undefined) && natureOfProperty5=='0' ){
			  errorList.push(getLocalMessage('unitdetails.selectSubNatureOfProperty')+" " +level); 
		 }
		 if (occupieremail != ""  && occupieremail != undefined) {
			  var emailRegex = new RegExp(/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
			  var valid = emailRegex.test(occupieremail);
			  if (!valid) {errorList.push(getLocalMessage("property.valid.occupieremail")+" " +level);
			  }
		}
		var filter = /[1-9]{1}[0-9]{9}/;
		 if (occupierMobNo == undefined) {

			} else if (occupierMobNo != '') {
				if (!filter.test(occupierMobNo)) {
					errorList.push(getLocalMessage('property.valid.occupiermobile')+" " +level);
				}
			}
	});
	return errorList;
}

// display details till current year after click on checkbox (unit detail) 
function displayDetailsTillCurrentYear(){
	
	if($('#checkDetail').is(":checked")){
		var errorList = [];
		var rowCount = $('#unitDetailTable tr').length;	
		if(rowCount<=2){
			reorderFirstRow();
		}
		errorList = validateUnitDetailTable(errorList);	
		if (errorList.length == 0) {
			var finYearId= $("#hiddenYear0").val();
			var data = {"finYearId" : finYearId};
			var URL = 'SelfAssessmentForm.html?getFinanceYearListFromGivenDate';
			var returnData = __doAjaxRequest(URL, 'POST', data, false);
			yearList=returnData;
			var returnDataLength =returnData.length;
			var tableLength=$("#unitDetailTable tr").length;
			for(var i=1; i<=returnDataLength-1;i++)    // yearList
			{ 
				var a=0;
				var c=0;
				var year= returnData[i];
				for(var j=1;j<tableLength;j++) // table tr length 
				{
					var content =	$("#unitDetailTable").find('tr:eq(\"'+j+'\")').clone();
					$('.secondUnitRow').removeClass('in');
					$("#unitDetailTable").append(content);
					$('.secondUnitRow:last').addClass('in');
					reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');		
					if(j%2!=0)  
					{						
						var utp=a;
						if(a>0){
						utp=a*6;
						}						
						
//						var unitSelectedValue=	$("#assdUnitTypeId"+a+" option:selected").val();
						var floorSelectedValue=	$("#assdFloorNo"+a+" option:selected").val();
						var usageSelectedValue1=$("#assdUsagetype"+utp+" option:selected").val();
						var usageSelectedValue2=$("#assdUsagetype"+(utp+1)+" option:selected").val();
						var usageSelectedValue3=$("#assdUsagetype"+(utp+2)+" option:selected").val();
						var usageSelectedValue4=$("#assdUsagetype"+(utp+3)+" option:selected").val();
						var usageSelectedValue5=$("#assdUsagetype"+(utp+4)+" option:selected").val();
						var constSelectedValue=	$("#assdConstruType"+a+" option:selected").val();
						var occupancyTypeSelectedValue=	$("#assdOccupancyType"+a+" option:selected").val();
								
//						$('#unitDetailTable tr:last').find("select:eq(1)").find("option[value = '" + unitSelectedValue + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(1)").find("option[value = '" + floorSelectedValue + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(2)").find("option[value = '" + constSelectedValue + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(3)").find("option[value = '" + usageSelectedValue1 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(4)").find("option[value = '" + usageSelectedValue2 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(5)").find("option[value = '" + usageSelectedValue3 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(6)").find("option[value = '" + usageSelectedValue4 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(7)").find("option[value = '" + usageSelectedValue5 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("input:hidden:eq(0)").attr("value",year);
						$('#unitDetailTable tr:last').find("select:eq(0)").find("option[value = '" + year + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(8)").find("option[value = '" + occupancyTypeSelectedValue + "']").attr("selected", "selected");
						a++;
					}
					
					if(j%2==0)
					{	
						var ntp=c;
						if(c>0){
						ntp=c*6;
						}
//						var roadTypeSelectedValue=	$("#assdRoadFactor"+c+" option:selected").val();
						var occupancyTypeSelectedValue=	$("#assdOccupancyType"+c+" option:selected").val();
						var natureOfProperty1=$("#natureOfProperty"+ntp+" option:selected").val();
						var natureOfProperty2=$("#natureOfProperty"+(ntp+1)+" option:selected").val();
						var natureOfProperty3=$("#natureOfProperty"+(ntp+2)+" option:selected").val();
						var natureOfProperty4=$("#natureOfProperty"+(ntp+3)+" option:selected").val();
						var natureOfProperty5=$("#natureOfProperty"+(ntp+4)+" option:selected").val();
						
//						$('#unitDetailTable tr:last').find("select:eq(0)").find("option[value = '" + roadTypeSelectedValue + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(0)").find("option[value = '" + occupancyTypeSelectedValue + "']").attr("selected", "selected");
												
						$('#unitDetailTable tr:last').find("select:eq(1)").find("option[value = '" + natureOfProperty1 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(2)").find("option[value = '" + natureOfProperty2 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(3)").find("option[value = '" + natureOfProperty3 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(4)").find("option[value = '" + natureOfProperty4 + "']").attr("selected", "selected");
						$('#unitDetailTable tr:last').find("select:eq(5)").find("option[value = '" + natureOfProperty5 + "']").attr("selected", "selected");
						
						//To make Monthly field required in case of Tenanted.
					/*	$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(c)+")");
						if(($('option:selected', $("#assdOccupancyType"+c)).attr('code')) == 'TAN'){							
							$("#proAssdAnnualRent"+c).prop('required',true);
						}else{
							$("#proAssdAnnualRent"+c).prop('required',false);
						}*/
						c++;
					
					}		
				}
			}
	
			//newly added
			var requestData = {};
			var theForm = '#frmSelfAssessmentForm';
			requestData = __serializeForm(theForm);		
			var URL = 'SelfAssessmentForm.html?setRoomDetails';
			var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json');	
			//newly added end
			
		}else {
			showVendorError(errorList);
			$('#checkDetail').prop('checked', false);			
		}
		
	}else{		
		$(".selectUnit option:gt(2)").remove();		
		clearTableData();  
	}
}

//reorder id and path after click on checkbox
function reOrderChangeUnitTabIdSequence(classNameFirst,classNamesecond){
	
	$(classNameFirst).each(function(i) {
		
		var ConstructFlag =  $("#ConstructFlag").val();
		//$("#year"+i).prop("disabled", true);
		var utp=i;
		if(i>0){
		utp=i*6;
		}
	$(this).find("select:eq(0)").attr("id", "year"+i);
	$(this).find("input:hidden:eq(0)").attr("id", "hiddenYear"+i);
	$(this).find("input:text:eq(0)").attr("id", "unitNo"+i);	
//	$(this).find("select:eq(1)").attr("id", "assdUnitTypeId"+i);
	$(this).find("select:eq(1)").attr("id", "assdFloorNo"+i);
	$(this).find("select:eq(2)").attr("id", "assdOccupancyType"+i);
	
	
	if(ConstructFlag != null && ConstructFlag == 'Y'){
		$(this).find("input:text:eq(1)").attr("id", "flatNo"+i);
		$(this).find("input:text:eq(2)").attr("id", "electricBillUnitNo"+i);
		$(this).find("input:text:eq(3)").attr("id", "electricConsumerNo"+i);
		$(this).find("input:text:eq(4)").attr("id", "electricMeterNo"+i);
		$(this).find("input:text:eq(5)").attr("id", "occupierName"+i);
		$(this).find("input:text:eq(6)").attr("id", "actualRent"+i);
		//newly added
		
		$(this).find("input:text:eq(7)").attr("id", "carpetArea"+i);
		$(this).find("input:text:eq(8)").attr("id", "taxableArea"+i);
//		$(this).find("input:text:eq(6)").attr("id", "constructPermissionNo"+i);
//		$(this).find("input:text:eq(7)").attr("id", "permissionUseNo"+i);
//		$(this).find("input:text:eq(8)").attr("id", "assessmentRemark"+i);
	}else{
		$(this).find("input:text:eq(3)").attr("id", "actualRent"+i);
		$(this).find("input:text:eq(2)").attr("id", "flatNo"+i);
		$(this).find("input:text:eq(3)").attr("id", "carpetArea"+i);
		$(this).find("input:text:eq(4)").attr("id", "taxableArea"+i);
//		$(this).find("input:text:eq(5)").attr("id", "constructPermissionNo"+i);
//		$(this).find("input:text:eq(6)").attr("id", "permissionUseNo"+i);
//		$(this).find("input:text:eq(7)").attr("id", "assessmentRemark"+i);
	}
	
	
    $(this).find("select:eq(3)").attr("id", "legal"+i);
    $(this).find("select:eq(4)").attr("id", "userChargesType"+i);
	$(this).find("select:eq(5)").attr("id", "assdConstruType"+i);
	$(this).find("select:eq(6)").attr("id", "assdUsagetype"+utp);
	$(this).find("a:eq(0)").attr("data-target", "#group-of-rows-"+i);
	$(this).find("a:eq(0)").attr("aria-controls", "#group-of-rows-"+i);
	$(this).find("a:eq(1)").attr("id", "deleteRow_"+i);
	$(this).find("select:eq(7)").attr("id", "assdUsagetype"+(utp+1));
	$(this).find("select:eq(8)").attr("id", "assdUsagetype"+(utp+2));
	$(this).find("select:eq(9)").attr("id", "assdUsagetype"+(utp+3));
	$(this).find("select:eq(10)").attr("id", "assdUsagetype"+(utp+4));
	
    
	$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitNo");
	$(this).find("input:hidden:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].faYearId");
	$(this).find("select:eq(0)").attr("name","");
	
//	$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUnitTypeId");
	$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdFloorNo");
	$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdOccupancyType");
	if(ConstructFlag != null && ConstructFlag == 'Y'){
		$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
		$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricBillUnitNo");
		$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricConsumerNo");
		$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].electricMeterNo");
		$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].occupierName");
		$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");
		
		//newly changed
		
		$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");
		$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");
//		$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
//		$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
//		$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
				
	}else{
		$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].actualRent");       
        $(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].flatNo");
        $(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].carpetArea");     
		$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdBuildupArea");		
//		$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].constructPermissionNo");
//		$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].permissionUseNo");
//		$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assessmentRemark");
	}
   	$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].legal");
   	$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].userChargesType");
	$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdConstruType");
	$(this).find("select:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype1");
	$(this).find("select:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype2");
	$(this).find("select:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype3");
	$(this).find("select:eq(9)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype4");
	$(this).find("select:eq(10)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+i+"].assdUsagetype5");
	
	//Newly Added	
	//$(this).find("input:checkbox:eq(0)").attr("id", "legal" + i);
	$(this).find('.addRoomClass').attr("id", "addRoom" + i);
	$(this).find(".addRoomClass").attr("onclick", "addRoomDetails("+(i)+")");

	$(this).find('.duppliquer').attr("id", "duppliquer" + i);
	$(this).find(".duppliquer").attr("onclick", "duppliquerDet("+(i)+")");
	
	$(this).find('.delete').attr("id", "delete_" + i);
	$(this).find(".delete").attr("onclick", "removeRow("+(i)+")");
	
	//
	
	var unitNoFact=$("#unitNo"+i).val();
	if($(".selectUnit option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined){
		$(".selectUnit").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
	}
	
	});
	
	$(classNamesecond).each(function(j){

		var utp=j;
		if(j>0){
		utp=j*6;
		}
		
//		$(this).find("select:eq(0)").attr("id", "assdRoadFactor"+j);
		$(this).find("select:eq(0)").attr("id", "assdOccupancyType"+j);
//		$(this).find("input:text:eq(0)").attr("id", "proAssdAnnualRent"+j);
		
		$(this).find("input:text:eq(0)").attr("id", "occupierName"+j);		
//		$(this).find("select:eq(0)").attr("onchange", "occupancyTypeChange("+(j)+")");
		$(this).attr("id", "group-of-rows-"+j);
		
		$(this).find("select:eq(1)").attr("id", "natureOfProperty"+utp);
		$(this).find("select:eq(2)").attr("id", "natureOfProperty"+(utp+1));
		$(this).find("select:eq(3)").attr("id", "natureOfProperty"+(utp+2));
		$(this).find("select:eq(4)").attr("id", "natureOfProperty"+(utp+3));
		$(this).find("select:eq(5)").attr("id", "natureOfProperty"+(utp+4));
		
		///newly dded
		$(this).find("input:text:eq(1)").attr("id", "occupierNameReg"+j);		
		$(this).find("input:text:eq(2)").attr("id", "occupierMobNo"+j);		
		$(this).find("input:text:eq(3)").attr("id", "occupierEmail"+j);
		$(this).find("input:text:eq(4)").attr("id", "constructPermissionNo"+j);
		$(this).find("input:text:eq(5)").attr("id", "permissionUseNo"+j);
		$(this).find("input:text:eq(6)").attr("id", "assessmentRemark"+j);
		$(this).find("input:text:eq(7)").attr("id", "constructPermissionDate"+j);
		$(this).find("input:text:eq(8)").attr("id", "actualRent"+j);
		/*if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
			$(".actualRent"+j).hide();
			$("#actualRent"+j).hide();
		}*/
		//
		
		// re-ordering path binding
//		$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdRoadFactor");
		$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdOccupancyType");
//		$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdAnnualRent");
		$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierName");

		$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty1");
		$(this).find("select:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty2");
		$(this).find("select:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty3");
		$(this).find("select:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty4");
		$(this).find("select:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assdNatureOfproperty5");
		
		//newly added
		$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierNameReg");
		$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierMobNo");
		$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].occupierEmail");
		$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].constructPermissionNo");
		$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].permissionUseNo");
		$(this).find("input:text:eq(6)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].assessmentRemark");
		$(this).find("input:text:eq(7)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].constructPermissionDate");
		$(this).find("input:text:eq(8)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentDetailDtoList["+j+"].actualRent");
		//
		
		var langFlag = getLocalMessage('admin.lang.translator.flag');
		if(langFlag ==='Y'){
			$("#occupierName"+j).keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#occupierName"+j).val().trim();
				if(no_spl_char!='')
				{
					commonlanguageTranslate(no_spl_char,'occupierNameReg'+j,event,'');
				}else{
					$("#occupierNameReg"+j).val('');
				}
			});
			
			}
	});
}


function unitCount(id){		
	//var unitCount= $("#unitNo"+id).val();
	var year =$("#year"+id).val();
	var data = {"finId":year};
	var URL = 'SelfAssessmentForm.html?compareDate';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	
	$('#yearOfConstruc'+id).datepicker('option', 'maxDate', new Date(returnData));  //restrict construction completion date based on year of Acquisition

	var a=0;
	var count=0;
	var selYear=$("#year"+id).val();
	$("#hiddenYear"+id).val(selYear);
	var tableLength = $('#unitDetailTable tr').length;
	for(var j=1;j<tableLength;j++) // unit detail table tr length 
	{
		var dropDownvalue=$("#year"+a).val();
		if(dropDownvalue == selYear){
			count++;
		}
		a++;
	}
	$("#unitNo"+id).val(count);
	if($(".selectUnit option[value="+count+"]").length == 0){
		$(".selectUnit").append("<option value="+count+">" +count+ "</option>");	
	}
}
function successErrorCheck(id){	
	var check = $("#yearOfConstruc"+id).val();
	if (check != '') {
		$('.successErrorCheck').removeClass("has-error").addClass("has-success");
	}	
}


function savePropertyFrom(element,flag){
	showloader(true);
	setTimeout(function () {
			   if(flag=='A'){
				var requestData = {};
				var theForm	=	'#frmSelfAssessmentForm';
				requestData = __serializeForm(theForm);
				var URL = 'SelfAssessmentAuthorization.html?saveAuthorization';
				var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
				if(returnData)
				{
					var divName = '.AuthContent';
			/*			$(divName).removeClass('ajaxloader');
			*/			$(divName).html(returnData);
				}
			}else{
				var errorList = [];
				var partialAmt= $("#partialAmt").val();
				var hiddenPayAppicableCheck= $("#hiddenPayAppicableCheck").val();
				if(hiddenPayAppicableCheck=='N'){
					$('#partialAmt').val(0);
					$("input[name='offlineDTO.onlineOfflineCheck']:checked").val('');
				}
			
				if(partialAmt == ''){
					$("#partialAmt").val(0);
				}
				if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'Y') {
					 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'SelfAssessmentForm.html?redirectToPay', 'saveform');
					}
				else if (flag == 'BIF' && $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'N'|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'P')
					{
						return saveOrUpdateForm(element,"Bill Payment done successfully!", 'BifurcationForm.html?PrintReport', 'saveform');
					}
					else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'N'|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'P')
						{
						 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'SelfAssessmentForm.html?PrintReport', 'saveform');
						}
					else
					{
					 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'AdminHome.html', 'saveform');
					}
			  
			}
			
			var a ="String";
			showloader(false);
	},1000);
}

function saveAuthorizationWithEdit(element,flag){
	
		var requestData = {};
		var theForm	=	'#frmSelfAssessmentForm';
		requestData = __serializeForm(theForm);
		var URL = 'SelfAssessmentAuthorization.html?saveAuthorizationWithEdit';
		var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
		if(returnData)
		{
			var divName = '.AuthContent';
	/*			$(divName).removeClass('ajaxloader');
	*/			$(divName).html(returnData);
		}
	}
//on edit
function openOccupancyOnEdit(){
	
	$('.secondUnitRow').each(function(j){
		if(($('option:selected', $("#assdOccupancyType"+j)).attr('code')) == 'TAN'){
			$('.actualRentClass').show();
		}else{
			$('.actualRentClass').hide();
		}
	});
}

function editSelfAssForm(obj){
	var ownerType = $("#ownershipId").val();
	var landTypePrefix=$(".landValue").val();
	var khasara=$('#displayEnteredKhaNo').val();
	var plot=$("#tppPlotNo").val();
	var data = {};
	var URL = 'SelfAssessmentForm.html?editSelfAssForm';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	reorderFirstRow();
	reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
	reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
	openOccupancyOnEdit();
//	occupancyTypeChange1();   
	yearLength();
	
	reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table

	var data1 = {"ownershipType" : ownerType};
	var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
	var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
	
	$("#owner").html(returnData1);
	$("#owner").show();
	if($("#editFlag").val()=="Y"){
	$("#proceed").show();
	}
	$("#checkList").hide();
	$("#ResetSelfAssForm").hide();
	//$('#checkDetail').prop('checked', true);	
	
	if(landTypePrefix !=undefined && landTypePrefix!=""){

	var landData = {"landType" : landTypePrefix};
	var landURL = 'SelfAssessmentForm.html?getLandTypeDetails';
	var returnLandData = __doAjaxRequest(landURL, 'POST', landData, false);
	$("#landType").html(returnLandData);
	$("#landType").show();	
	}
	
	if(landTypePrefix=='KPK'){
	var districtId=$('#assDistrict').val();
	var TehsilId=$('#assTahasil').val();
	var villageId=$('#tppVillageMauja').val();
	$('#khasraNo').val(khasara);
	
	var khasraData = {"landType" : landTypePrefix,"khasara":khasara,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId};
	if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#displayKhaNo').val()!=""){				

	var khasraURL = 'SelfAssessmentForm.html?getKhasaraDetails';
	var returnkhasraData = __doAjaxRequest(khasraURL, 'POST', khasraData, false);		
	$("#showApiDetails").html(returnkhasraData);
	$("#showApiDetails").show();
	}
	}
	else if(landTypePrefix=='NZL' || landTypePrefix=='DIV' ){
		//var landType=$("#assLandType" + " option:selected").attr("code");
		var districtId=$('#assDistrict').val();
		var TehsilId=$('#assTahasil').val();
		var villageId=$('#tppVillageMauja').val();
		var mohallaId=$('#mohalla').val();
		var streetNo=$('#assStreetNo').val();	
		//var plotNo=$('#tppPlotNo').val();
		$('#plotNo').val(plot);	
		var data = {"landType" : landTypePrefix,"plotNo" : plot,"districtId" : districtId,"TehsilId" : TehsilId,"villageId" : villageId,"mohallaId" : mohallaId,"streetNo" : streetNo};
		if($('#assDistrict').val()!=undefined && $('#assTahasil').val()!=undefined && $('#tppVillageMauja').val()!=undefined && $('#mohalla').val()!=undefined && $('#assStreetNo').val()!=undefined){				

		var URL = 'SelfAssessmentForm.html?getNajoolAndDiversionDetails';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);		
		$("#showApiDetails").html(returnData);
		$("#showApiDetails").show();
	}
	}
	
}

function editSelfAssBifurcationForm(obj){

	var ownerType = $("#ownershipId").val();
	var data = {};
	var URL = 'SelfAssessmentForm.html?editSelfAssForm';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	$(formDivName).html(returnData);
	reorderFirstRow();
	reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	 // reordering Id and Path for unit details table	
	reOrderJointOwnerTableSequence('.jointOwner');   // reordering Id and Path for joint owner table
	
//	occupancyTypeChange1();   
	yearLength();
	
	reorderfactor('.specFact','.eachFact');  // reordering Id and Path for unit specific Additional Info Table
	$("#checkListBifu").hide();
	$("#proceedBifu").show();
	var data1 = {"ownershipType" : ownerType};
	var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
	var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
	
	$("#owner").html(returnData1);
	$("#owner").show();
	//$('#checkDetail').prop('checked', true);
	
}

function showAddInfo(factorCode,factRadioVal){
	
	if($('input[name="provisionalAssesmentMstDto.proAssfactor['+factRadioVal+']"]:checked').val()=="Y"){
		
		var data = {"factorCode" : factorCode};		
		var URL = 'SelfAssessmentForm.html?getFactorValueDiv';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html(returnData);
		
		$("#"+factorCode).show();
		reorderfactor('.specFact','.eachFact');
		var a=0;
		var tableLength = $('#unitDetailTable tr').length;
		for(var j=1;j<tableLength;j++) // table tr length 
		{
			var unitNoFact=$("#unitNo"+a).val();
			var unitSelectId=".factQuestion"+factRadioVal;
			if($(""+unitSelectId+" option[value="+unitNoFact+"]").length == 0 && unitNoFact!=undefined){
				$(""+unitSelectId+"").append("<option value="+unitNoFact+">" +unitNoFact+ "</option>");	
			}
			a++;
		}
	}else{	 
		var data = {"factorCode" : factorCode};
		var URL = 'SelfAssessmentForm.html?deleteFactorStatus';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		$("#"+factorCode).html('');
		reorderfactor('.specFact','.eachFact');
	}
}
// reorder id and path for unit specific addition information table
function reorderfactor(specFact,eachFact)
{
	$(eachFact).each(function(j) {
		$(specFact).each(function(i) {
			$(this).find("select:eq(0)").attr("id", "unitNoFact"+i).attr('onchange','resetFactorValue(this,'+i+')');
			$(this).find("select:eq(1)").attr("id", "assfFactorValueId"+i).attr('onchange','enabledisable(this,'+i+')');
			$(this).find("input:hidden:eq(0)").attr("name","provAsseFactDtlDto["+i+"].assfFactorId");
			$(this).find("input:hidden:eq(1)").attr("id","factPref"+i);
			$(this).find("input:hidden:eq(1)").attr("name","provAsseFactDtlDto["+i+"].factorValueCode");
			$(this).find("input:hidden:eq(2)").attr("id","proAssfId"+i);
			$(this).find("input:hidden:eq(2)").attr("name","provAsseFactDtlDto["+i+"].proAssfId");
			$(this).find("select:eq(0)").attr("name","provAsseFactDtlDto["+i+"].unitNoFact");
			$(this).find("select:eq(1)").attr("name","provAsseFactDtlDto["+i+"].assfFactorValueId");
			
			///newly added
			$(".datepicker").removeClass("hasDatepicker");
			$(".lessthancurrdate").removeClass("hasDatepicker");
			datePickerLogic();
			
			$(this).find("input:text:eq(0)").attr("id","factorDate"+i);
			$(this).find("input:text:eq(1)").attr("id","factorRemark"+i);
			$(this).find("input:text:eq(2)").attr("id","factorAmount"+i);
			$(this).find("input:text:eq(0)").attr("name", "provAsseFactDtlDto["+i+"].factorDate");
			$(this).find("input:text:eq(1)").attr("name", "provAsseFactDtlDto["+i+"].factorRemark");
			$(this).find("input:text:eq(2)").attr("name", "provAsseFactDtlDto["+i+"].factorAmount");
			//
			
			$(this).find("a:eq(0)").attr("onclick", "addUnitRow("+(i)+")");
			$(this).find("a:eq(1)").attr("id", "deleteFactorTableRow_"+i);			
		 });
		$(this).find("select:eq(0)").attr("class","form-control mandColorClass selectUnit factQuestion"+j);

		});
}

function downloadYearWiseTax(){
	var data = {};
	var URL = 'SelfAssessmentForm.html?exportYearWiseTaxDetail';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
}

function doFormActionForSaveProperty(obj,successMessage, URL, sendFormData, successUrl)
{
	
	
	var	formName =	findClosestElementId(obj, 'form');
	var theForm	=	'#'+formName;
	
	             var requestData = {};
	
				if (sendFormData) {
					
					requestData = __serializeForm(theForm);
				}
			
				var url	=URL;
				
				
				var returnData=__doAjaxRequestForSave(url, 'post',requestData, false,'',obj);
				if ($.isPlainObject(returnData))
				{
					var message = returnData.command.message;
					
					var hasError = returnData.command.hasValidationError;
					
					if (!message) {
						message = successMessage;
					}
					
					if(message && !hasError)
						{
						   	if(returnData.command.hiddenOtherVal == 'SERVERERROR')
						   		
						   		showSaveResultBox(returnData, message, 'AdminHome.html');
						   	
						   	else
						   		
						   		showSaveResultBox(returnData, message, successUrl);
						}
					else if(hasError)
					{
						$('.error-div').html('<h2>ddddddddddddddddddddddddddddddd</h2>');	
					}
					else
						return returnData;
					
				}
				else if (typeof(returnData) === "string")
				{
					$(formDivName).html(returnData);	
					prepareTags();
				}
				else 
				{
					alert("Invalid datatype received : " + returnData);
				}
				
				return false;
				
	
}

//fetch API details on selection of button 
function getLandApiDetails(obj){
	var landTypePrefix=$(".landValue").val();
	var data = {};
	var URL = 'ChangeInAssessmentForm.html?getLandTypeApiDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);		
	$("#showAuthApiDetails").html(returnData);
	$("#showAuthApiDetails").show();	

	
}

function saveNoOfDaysEditProperty(element){
	
	$.fancybox.close();
	var divName = '.content-page';
	var errorList = [];
	var formName = findClosestElementId(element, 'form');
	var theForm = '#' + formName;
	var requestData = __serializeForm(theForm);
	var object = __doAjaxRequest(
		"ChangeInAssessmentForm.html?saveNoOfDaysEditForm", 'POST',
		requestData, false, 'json');
	
	if (object.successFlag == "Y") {
		if(object.applicationId != null && object.applicationId != ''){
			showBoxForApproval(getLocalMessage("prop.application.edit.success.appNo" + object.applicationId));
		}else{
			showBoxForApproval(getLocalMessage("prop.application.edit.success"));
		}
	}else{
		if (object.error != null && object.error != 0) {
		    $.each(object.error, function(key, value) {
			$.each(value, function(key, value) {
			    if (value != null && value != '') {
				errorList.push(value);
			    }
			});
		    });
		    displayErrorsOnPage(errorList);
		}
	}
}

function showBoxForApproval(succesMessage) {

	var childDivName = '.msg-dialog-box';
	var message = '';
	var Proceed = getLocalMessage("proceed");
	var no = 'No';
	message += '<p class="text-blue-2 text-center padding-15">' + succesMessage
			+ '</p>';

	var totalpayableAmnt = $("#partialAmt").val();
	if (totalpayableAmnt > 0) {
		var redirectUrl = 'ChangeInAssessmentForm.html?PrintReport';
		message += '<div class=\'text-center padding-bottom-10\'>'
				+ '<input type=\'button\' value=\'' + 'Proceed'
				+ '\'  id=\'btnNo\' class=\'btn btn-success\'    '
				+ ' onclick="closebox(\'' + errMsgDiv + '\',\'' + redirectUrl
				+ '\')"/>' + '</div>';
	} else {
		message += '<div class=\'text-center padding-bottom-10\'>'
				+ '<input class="btn btn-success" style="margin-right:10px" type=\'button\' value=\''
				+ Proceed + '\'  id=\'Proceed\' '
				+ ' onclick="closeApproval();"/>' + '</div>';
	}

	$(childDivName).addClass('ok-msg').removeClass('warn-msg');
	$(childDivName).html(message);
	$(childDivName).show();
	$('#Proceed').focus();
	showModalBoxWithoutClose(childDivName);
}

function closeApproval() {
	window.location.href = 'AdminHome.html';
	$.fancybox.close();
}

function addRoomDetails(count, element) {
	$('#countOfRow').val(count);
	var serviceShortCode = $('#serviceShortCode').val();
	var selfAss = $('#selfAss').val();
	var errorList = [];
	//errorList = validateUnitDetailTable(errorList);
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);
	} else {
		/*if ($("#checkDetail").prop('checked') == true) {
			$('#unitDetailsCheck').val('Y');
		} else {
			$('#unitDetailsCheck').val('');
		}*/
		var requestData = {};
		if('CIA' == serviceShortCode){
			
			var action = "ChangeInAssessmentForm.html";
			var data = {"countOfRow" : count};
			var theForm = '#frmChangeAssessmentForm';
			requestData = __serializeForm(theForm);
			var URL = "ChangeInAssessmentForm.html?addRoomDetails";
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
		}else{
			var theForm = '#frmSelfAssessmentForm';
			var action = "SelfAssessmentForm.html";
			requestData = __serializeForm(theForm);
			var URL = action + '?addRoomDetails';
			var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		}
		
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		//areaCalculate();
	}
}

function viewRoomDetails(count, element) {
	
	$('#countOfRow').val(count);
		var assType = $("#assType").val();
		var countOfRow = count;
		var URL = "SelfAssessmentAuthorization.html?viewRoomDetails";
		var data = {"countOfRow" : countOfRow};
		var returnData = __doAjaxRequest(URL, 'POST', data, false);

		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		var totalAreaval = $("#totalAreaval").val();
		totalAreaval = parseFloat(totalAreaval).toFixed(2);
		$("#totalAreaval").val(totalAreaval);
}

function viewApplicantRoomDetails(count, element) {
	
	$('#countOfRow').val(count);
		var countOfRow = count;
		var URL = "SelfAssessmentAuthorization.html?viewApplicantRoomDetails";
		var data = {"countOfRow" : countOfRow};
		var returnData = __doAjaxRequest(URL, 'POST', data, false);

		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		var totalAreaval = $("#totalAreaval").val();
		totalAreaval = parseFloat(totalAreaval).toFixed(2);
		$("#totalAreaval").val(totalAreaval);
}

function saveRoomData(element){
	var errorList = [];
	errorList=validateRoomDetails(element,errorList);
//	var totalBuildUpArea = populateBuildUpArea();
//	var totalCarpetArea = populateCarpetArea();
	if (errorList.length > 0) {
		displayErrorsOnPage(errorList);	
	}else{
		//var countOfRow=$("#countOfRow").val();
		var unitChecked=$('#unitChecked').val();
		var ownerType=$('#ownerType').val();
		var theForm = '#frmSelfAssessmentFormRoom';
		var serviceShortCode = $('#serviceShortCode').val();
		if('CIA' == serviceShortCode){
			var action="ChangeInAssessmentForm.html";
		}else{
			var action="SelfAssessmentForm.html";
		}
		var requestData = __serializeForm(theForm);
		var URL = action+'?saveRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');
		var divName = '.content-page';
		$(divName).removeClass('ajaxloader');
		$(divName).html(returnData);
		
		//$('#carpetArea'+countOfRow).val(totalCarpetArea.toFixed(2));
		//$('#taxableArea'+countOfRow).val(totalBuildUpArea.toFixed(2));
		//set owner details
		if('CIA' != serviceShortCode){
			if (ownerType != "") {
			var data1 = {
				"ownershipType" : ownerType
			};
			var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
			var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
			$("#owner").html(returnData1);
			$("#owner").show();
			$("#proceed").hide();
			$("#checkList").show();			
			$("#ResetSelfAssForm").show();
		}
		}
		
		//set checkbox
		/*if(unitChecked=="Y"){
			$('#checkDetail').prop('checked', true);
		}else{
			$('#checkDetail').prop('checked', false);
		}*/
		if('CIA' == serviceShortCode){
			reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
		}else{
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
		}
		reOrderJointOwnerTableSequence('.jointOwner'); 
		reorderfactor('.specFact','.eachFact');
		$(document).ready(function() {
			$('html, body').animate({
				  scrollTop: $("#scrollToDiv").offset().top
				});
		});
	}	
}

function backFromRoomdetails(element){
	var ownerType=$('#ownerType').val();
	var unitChecked=$('#unitChecked').val();
	var serviceShortCode = $('#serviceShortCode').val();
	if('CIA' == serviceShortCode){
		var action="ChangeInAssessmentForm.html";
	}else{
		var action="SelfAssessmentForm.html";
	}	
	var requestData = {};
//	var countOfRow=$("#countOfRow").val();
//	var totalBuildUpArea = populateBuildUpArea();
//	var totalCarpetArea = populateCarpetArea();
	var URL = action+'?saveRoomDetails';
	var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'html');

	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
	//$('#carpetArea'+countOfRow).val(totalCarpetArea.toFixed(2));
	//$('#taxableArea'+countOfRow).val(totalBuildUpArea.toFixed(2));
	// set owner details
	if('CIA' != serviceShortCode){
	if (ownerType != "") {
		var data1 = {
			"ownershipType" : ownerType
		};
		var URL1 = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
		var returnData1 = __doAjaxRequest(URL1, 'POST', data1, false);
		$("#owner").html(returnData1);
		$("#owner").show();
		$("#proceed").hide();
		$("#checkList").show();			
		$("#ResetSelfAssForm").show();
		}
	}
	//set checkbox
	/*if(unitChecked=="Y"){
		$('#checkDetail').prop('checked', true);
	}else{
		$('#checkDetail').prop('checked', false);
	}*/
	if('CIA' == serviceShortCode){
		reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
	}else{
		reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
	}
	//reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
	reOrderJointOwnerTableSequence('.jointOwner'); 
	reorderfactor('.specFact','.eachFact');
	$(document).ready(function() {
		$('html, body').animate({
			  scrollTop: $("#scrollToDiv").offset().top
			});
	});
}

function backToAuthPage(obj){
	var data = {};
	var URL = 'SelfAssessmentAuthorization.html?backToFirstLevelAuth';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}

function backToViewApplicantDetails(obj){
	var data = {};
	var URL = 'SelfAssessmentAuthorization.html?backToViewApplicantDetails';
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	var divName = '.content-page';
	$(divName).removeClass('ajaxloader');
	$(divName).html(returnData);
}

function validateRoomDetails(element, errorList) {
	
	var area=0.0;	
	var roomType = "";
	$('.thirdRowRoomDetails').each(function(i) {

		var rowCount = $('#roomDetailstable tr').length;
		if (rowCount <= 2) {
			roomType = $('#roomType' + i).val();			
		}else{
			roomType = $('#roomType' + i).val();			
		}		
		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		
		var level = i+1;
		
		if (errorList.length <= 0) {
			if (roomType == "" || roomType == undefined || roomType == null || roomType == '0') {
				errorList.push(getLocalMessage("property.valid.roomType") +" "+ level);
			}
			if (roomLength == "" || roomLength == undefined || roomLength == null) {
				errorList.push(getLocalMessage("property.valid.roomLength") +" "+ level);
			}
			if (roomWidth == "" || roomWidth == undefined || roomWidth == null) {
				errorList.push(getLocalMessage("property.valid.roomWidth") +" "+ level);
			}
		}else{
			return errorList;
		}
	});
	if (errorList.length == 0) {
		var carpetArea =parseFloat($('#carpetArea').val());
		$('.thirdRowRoomDetails').each(function(i) {
			var roomArea = $('#roomArea' + i).val();
			area += roomArea;
		});
		area=parseFloat(area);
//		if (area > carpetArea) {
//			errorList.push(getLocalMessage("property.valid.validroomArea"));
//		}
	}
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
	if (rowCount <= 3) {
		
		errorList.push(getLocalMessage('property.valid.firstRow'));
		displayErrorsOnPage(errorList);		
		return false;
	} else {

		var index= $(this).closest('#roomDetailstable tr').index();				
		var action="SelfAssessmentForm.html";						
		var requestData = {
			"countOfRow" : countOfRow,
			"index":index
		};	
		$(this).closest('#roomDetailstable tr').remove();
		var URL = action+'?removeRoomDetails';
		var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json');			
		reOrderRoomTableIdSequence(countOfRow);
	}
	areaCalculate();
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

		var roomLength = parseFloat($('#roomLength' + i).val());
		var roomWidth = parseFloat($('#roomWidth' + i).val());
		if ((roomLength != "" || roomLength != null || roomLength != undefined) && (roomWidth != "" || roomWidth != null || roomWidth != undefined)) {
			var num =roomLength * roomWidth;
			var result = num.toFixed(2);
			if("NaN" == result){
				result = "";
			}
			$('#roomArea' + i).val(result);
		} else {
			$('#roomArea' + i).val();
		}
	});
}

function populateBuildUpArea(){
	var totalarea = 0;
	$('.thirdRowRoomDetails').each(function(i) {
		let totalRoomArea = Number($("#roomArea" + i).val());
		totalarea += totalRoomArea;
	});
	return totalarea;
}

function populateCarpetArea(){
	var totalarea = 0;
	$('.thirdRowRoomDetails').each(function(i) {
		var roomLength = $('#roomLength' + i).val();
		var roomWidth = $('#roomWidth' + i).val();
		if (roomLength != "" && roomWidth != "") {
			var num =roomLength * roomWidth;
			var result = num.toFixed(2);
			let totalRoomArea = Number(num);
			totalarea += totalRoomArea;
		}
	});
	return totalarea;
}

/*function duppliquerDet(count){

var copiedRow=count;
count=count+1;
var rw=$("#unitDetailTable").find('tr:eq('+count+')');
var xr=$('#unitDetailTable tr').last();
console.log(rw);
var new_rw = rw.clone( true );
xr.after(new_rw);
reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); 
var content =	$("#roomDetailstable").find('tr:eq(1)').clone();
$(this).closest("#roomDetailstable").append(content);
reOrderRoomTableIdSequence(countOfRow);
var tableLength = $('#unitDetailTable tr').length;
var tableCount=tableLength-2;
//$("#taxableArea"+tableCount).val("");	
//$("#carpetArea"+tableCount).val("");	
$('#copiedrow').val(copiedRow);
}*/
function addRow(){
var errorList = [];			
var rowCount = $('#unitDetailTable tr:last').index()/2 ;			
errorList = validateUnitDetailTable(errorList);	
if (errorList.length == 0) {	
$(".datepicker2").removeClass("hasDatepicker");	
$(".lessthancurrdate").removeClass("hasDatepicker");	
	var content =	$("#unitDetailTable").find('tr:eq(1)').clone();
	$("#unitDetailTable").append(content);

	content.find("input:text").val('');
	content.find("select").val('0');
	content.find("input:hidden:eq(0)").val('0');

	$('.error-div').hide();
	datePickerLogic();
	reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');  // reorder id and Path after Addition of new Row

//	return false;
}							
else {
	showVendorError(errorList);
}		
}


function removeRow(count){

//To remove entry from dto
var inputId=count;		
//var left_text = inputId.lastIndexOf("_");
var deletedRowCount =inputId+1;		
var data = {"deletedRowCount" : count};
var URL = 'SelfAssessmentForm.html?deleteUnitTableRow';


//To delete Unit No from unit specific additional information 
var unitCount= $("#unitNo"+deletedRowCount).val();		
var a=0; 
var tableLength = $('#unitDetailTable tr').length;
for(var j=1;j<tableLength;j++) // table tr length 
{
	var unitNoFact=$("#unitNo"+j).val();   			
	if(unitCount==unitNoFact){
		a++;
	}
}
if(a==1){
	$(".selectUnit option[value="+unitCount+"]").remove();
}

countrow=count+1;
//remove row From UI
var index =  $('#unitDetailTable tr:last').index();
//if(($("#unitDetailTable tr").length)>2 && (index!=1)) {
if($("#unitDetailTable tr").length > 2){
	var tr = $("#unitDetailTable").find('tr:eq('+countrow+')');
    tr.remove();
  
	var returnData = __doAjaxRequest(URL, 'POST', data, false);
	reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
	
}
else {	
	var errorList = [];
	
	errorList.push(getLocalMessage("unit.Firstrowcannotberemove"));
	showVendorError(errorList);
}	
}



function areaCalculate(){
	var finalTotal=0;
	var roomlen=$('#roomDetailstable tr').length;
	//var roomlen=4;
	for(var i=0; i< (roomlen-2); i++ ){
		var are=$('#roomArea'+i).val();
		/*if(are=='' || are==undefined || are==null){
			are=0;
		}*/
		finalTotal=finalTotal+parseFloat(are);
		//console.log(finalTotal);
	}
	finalTotal=finalTotal.toFixed(2);
	if(finalTotal == 'NaN'){
		finalTotal = '';
	}
	$('#totalAreaval').val(finalTotal);
}

function duppliquerDet(count){
	
	var errorList = [];	
	var serviceCode= $('#serviceShortCode').val();
	var draftApplication= $('#draftApplication').val();
	if($('#pagenationFlag').val() == 'Y' && draftApplication == 'Y' && count >= 74){
		errorList.push(getLocalMessage("save.draft.next.page"));
		if (errorList.length > 0){
			showVendorError(errorList);
		}
	}
	errorList = validateUnitDetailTable(errorList);	
	if (errorList.length == 0) {	
		var copiedRow=count;
		count=count+1;
		var rw=$("#unitDetailTable").find('tr:eq('+count+')');
		var xr=$('#unitDetailTable tr').last();
		console.log(rw);
		var new_rw = rw.clone( true );
		xr.after(new_rw);
		$('#unitDetailTable tr:last').find("input[id^='flatNo']").prop('readonly', false);
		var utp = copiedRow;
		if (copiedRow > 0) {
			utp = copiedRow * 6;
		}
		
		
		if(serviceCode == 'CIA'){
			reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			var floorSelectedValue=	$("#assdFloorNo"+copiedRow+" option:selected").val();
			var occupancyTypeSelectedValue=	$("#assdOccupancyType"+copiedRow+" option:selected").val();
			var legalSelectedValue=$("#legal"+(copiedRow)+" option:selected").val();
			var userChargesTypeSelectedValue=$("#userChargesType"+(copiedRow)+" option:selected").val();
			var areaOrRvBasedChangeSelectedValue=$("#areaOrRvBasedChange"+(copiedRow)+" option:selected").val();
			var constSelectedValue=	$("#assdConstruType"+copiedRow+" option:selected").val();
			var usageSelectedValue1=$("#assdUsagetype"+utp+" option:selected").val();
			var usageSelectedValue2=$("#assdUsagetype"+(utp+1)+" option:selected").val();
							
			$('#unitDetailTable tr:last').find("select:eq(1)").find("option[value = '" + floorSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(2)").find("option[value = '" + occupancyTypeSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(3)").find("option[value = '" + legalSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(4)").find("option[value = '" + userChargesTypeSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(5)").find("option[value = '" + areaOrRvBasedChangeSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(6)").find("option[value = '" + constSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(7)").find("option[value = '" + usageSelectedValue1 + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(8)").find("option[value = '" + usageSelectedValue2 + "']").attr("selected", "selected");
		
		}else{
			reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			var floorSelectedValue=	$("#assdFloorNo"+copiedRow+" option:selected").val();
			var occupancyTypeSelectedValue=	$("#assdOccupancyType"+copiedRow+" option:selected").val();
			var legalSelectedValue=$("#legal"+(copiedRow)+" option:selected").val();
			var userChargesTypeSelectedValue=$("#userChargesType"+(copiedRow)+" option:selected").val();
			var constSelectedValue=	$("#assdConstruType"+copiedRow+" option:selected").val();
			var usageSelectedValue1=$("#assdUsagetype"+utp+" option:selected").val();
			var usageSelectedValue2=$("#assdUsagetype"+(utp+1)+" option:selected").val();
							
			$('#unitDetailTable tr:last').find("select:eq(1)").find("option[value = '" + floorSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(2)").find("option[value = '" + occupancyTypeSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(3)").find("option[value = '" + legalSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(4)").find("option[value = '" + userChargesTypeSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(5)").find("option[value = '" + constSelectedValue + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(6)").find("option[value = '" + usageSelectedValue1 + "']").attr("selected", "selected");
			$('#unitDetailTable tr:last').find("select:eq(7)").find("option[value = '" + usageSelectedValue2 + "']").attr("selected", "selected");
		}
		 
		//var content =    $("#roomDetailstable").find('tr:eq(1)').clone();
		//$(this).closest("#roomDetailstable").append(content);
		var action="SelfAssessmentForm.html";                        
		        var requestData = {
		            "countOfRow" : copiedRow,
		            "index":count
		        };    
		        //$(this).closest('#roomDetailstable tr').remove();
		        var URL = action+'?copyRoomDetails';
		        var returnData = __doAjaxRequest(URL, 'post', requestData, false, 'json');    
		reOrderRoomTableIdSequence(countOfRow);
		var tableLength = $('#unitDetailTable tr').length;
		var tableCount=tableLength-2;
		//$("#taxableArea"+tableCount).val("");    
		//$("#carpetArea"+tableCount).val("");    
		$('#copiedrow').val(copiedRow);
	}else{
		showVendorError(errorList);
	}
}

document.onkeyup=function(e){
	var e = e || window.event; // for IE to cover IEs window object
	if(e.altKey && e.which == 65) {
		e.preventDefault();
		if ($("#frmSelfAssessmentForm").find('#unitDetailTable')){
		addRow();
		}
		if($("#frmSelfAssessmentFormRoom").find('#roomDetailstable')){	
		addRoomDetailsnew();
		}
		return false;
	}
}
$(document).on('keyup keypress', 'form input[type="text"]', function(e) {
	  if(e.keyCode == 13) {
	    e.preventDefault();
	    return false;
	  }
	});

   
	
function addRoomDetailsnew(){
	var countOfRow=$("#countOfRow").val();
	var rw=$("#roomDetailstable").find('tr:eq(1)');
	var xr=$('#roomDetailstable tbody tr').last();
	console.log(rw);
	var new_rw = rw.clone( true );
	xr.after(new_rw);
	reOrderRoomTableIdSequence(countOfRow);
	var i=($('#roomDetailstable tr').length) -3;
	$('#roomLength'+i).val("");
	$('#roomWidth'+i).val("");
	$('#roomArea'+i).val("");
	
	return false;
	}

function savePropertyFromAsDraft(element,flag){
	showloader(true);
	setTimeout(function () {
		$('#draftFlag').val('Y');
			   if(flag=='A'){
				var requestData = {};
				var theForm	=	'#frmSelfAssessmentForm';
				requestData = __serializeForm(theForm);
				var URL = 'SelfAssessmentAuthorization.html?saveAuthorization';
				var returnData =doFormActionForSaveProperty(element,"Saved Successfully",URL, true, "AdminHome.html");
				if(returnData)
				{
					var divName = '.AuthContent';
			/*			$(divName).removeClass('ajaxloader');
			*/			$(divName).html(returnData);
				}
			}else{
				var errorList = [];
				var partialAmt= $("#partialAmt").val();
				var hiddenPayAppicableCheck= $("#hiddenPayAppicableCheck").val();
				if(hiddenPayAppicableCheck=='N'){
					$('#partialAmt').val(0);
					$("input[name='offlineDTO.onlineOfflineCheck']:checked").val('');
				}
			
				if(partialAmt == ''){
					$("#partialAmt").val(0);
				}
				if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'Y') {
					 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'SelfAssessmentForm.html?redirectToPay', 'saveform');
					}
				else if (flag == 'BIF' && $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'N'|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'P')
					{
						return saveOrUpdateForm(element,"Bill Payment done successfully!", 'BifurcationForm.html?PrintReport', 'saveform');
					}
					else if ($("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'N'|| $("input:radio[name='offlineDTO.onlineOfflineCheck']").filter(":checked").val() == 'P')
						{
						 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'SelfAssessmentForm.html?PrintReport', 'saveform');
						}
					else
					{
					 return saveOrUpdateForm(element,"Bill Payment done successfully!", 'AdminHome.html', 'saveform');
					}
			  
			}
			
			var a ="String";
			showloader(false);
	},1000);
}

function getDataForNextPage(obj,pageNo) {
	$(".error-msg").hide();
		var errorList = [];
		 showloader(true);
		 setTimeout(function () {
		if (errorList.length == 0)
		{
			
			var data = {"pageNo" : pageNo};
			var returnData1 = __doAjaxRequest('ChangeInAssessmentForm.html?getPageCount', 'POST', data, false);	
			var action = "ChangeInAssessmentForm.html";
			var theForm = '#frmSelfAssessmentForm';
			if ($("#serviceShortCode").val() === 'CIA') {
   				 theForm = '#frmChangeAssessmentForm';
			}
			requestData = __serializeForm(theForm);
			var URL = 'ChangeInAssessmentForm.html?getDataForNextPage';
			var returnData = __doAjaxRequest(URL, 'POST', requestData, false, 'html');
			//$(formDivName).html(returnData);
			var divName = '.content-page';
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			if ($("#serviceShortCode").val() === 'CIA') {
				reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow');
			} else {
				reOrderChangeUnitTabIdSequence('.firstUnitRow','.secondUnitRow'); // reordering Id and Path for unit details table				
			}
			reorderfactor('.specFact','.eachFact');
			
			return false;
 	 	}		
		else{
 		displayErrorsOnPage(errorList);
		}
		showloader(false);
		 }, 6000);
 }