
//to return owner div onselection of drop down menu
function getOwnerTypeDetails() {

var authLevel = $("#authLevel").val();
var ownerType = $("#ownerTypeId" + " option:selected").attr("code");
if(ownerType!=undefined){
var data = {"ownershipType" : ownerType};
var URL = 'SelfAssessmentForm.html?getOwnershipTypeDiv';
var returnData = __doAjaxRequest(URL, 'POST', data, false);
clearOccupancyDetails();
$("#owner").html(returnData);
setTimeout(function () {
	$("#owner").show();
}, 100);
if(authLevel == "true"){
	$("#proceed").show();
}else{
	$("#proceed").hide();
}
$("#checkList").show();
$("#checkListDiv").hide();
}
else{
	$("#owner").html("");
}
}
// validation for joint owner second row
$('body').on('focus',".hasPanno", function(){
	jQuery('.hasPanno').keyup(function () { 
	    this.value = this.value.replace(/[^a-z A-Z 0-9]/g,'');
	    $(this).attr('maxlength','10');
	});
	});
	$('body').on('focus',".hasCharacter", function(){
		$('.hasCharacter').keyup(function () { 
		    this.value = this.value.replace(/[^a-z A-Z]/g,'');	   
		});
	});
	$('body').on('focus',".hasMobileNo", function(){
		$('.hasMobileNo').keyup(function () {
		    this.value = this.value.replace(/[^0-9]/g,'');
		    $(this).attr('maxlength','10');
		});
	});
	$('body').on('focus',".hasAadharNo", function(){
	$('.hasAadharNo').keyup(function () { 
	    this.value = this.value.replace(/[^0-9]/g,'');
	});
	});	
	// property share field
	$('body').on('focus',".hasNumber", function(){
		$('.hasNumber').keyup(function () { 
			 this.value = this.value.replace(/[^0-9]/g,'');		   
		});
	});			
	$('body').on('focus',".hasNoSpace", function(){	
	jQuery('.hasNoSpace').keyup(function () { 
	    this.value = this.value.replace(/\s/g,'');
	   
	});
	});


$(function(){

	var authLevel = $("#authLevel").val();
	if(authLevel == "true"){
		$("#owner").show();
	}else{
		//$("#owner").hide();
	}
	var langFlag = getLocalMessage('admin.lang.translator.flag');
		if(langFlag ==='Y'){
			$("#assoOwnerName, #assoOwnerName_0").keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#assoOwnerName, #assoOwnerName_0").val().trim();
				if(no_spl_char!='')
				{
					commonlanguageTranslate(no_spl_char,'assoOwnerNameReg',event,'');
					commonlanguageTranslate(no_spl_char,'assoOwnerNameReg_0',event,'');
				}else{
					$("#assoOwnerNameReg").val('');
					$("#assoOwnerNameReg_0").val('');
				}
			});
			}
	/*In case of Joint Owner: add new row into Joint Owner table and Reorder ID and Path*/
	$("#jointOwnerTable").on('click','.addCF',function(){
		var errorList = [];	
		if (errorList.length == 0) {
				var content = $(this).closest('tr').clone();
				$(this).closest("tr").after(content);
				content.find("input:text").val('');
				content.find("select").val('0');	
				content.find("input:hidden").remove();
				$('.error-div').hide();				
				reOrderJointOwnerTableSequence('.jointOwner');				
				return false;
			} else {
			displayErrorsOnPage(errorList);
		}
		
	}); 
	
	/*To remove row from Joint Owner table and Reorder ID and Path*/
	$("#jointOwnerTable").on('click','.remCF',function(){
	
		var rowId=this.id;	
		var leftText = rowId.lastIndexOf("_");
		var deletedOwnerRowId = rowId.substring(leftText+1);
		var data = {"deletedOwnerRowId" : deletedOwnerRowId};
		var URL = 'SelfAssessmentForm.html?deleteOwnerTable';
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		
		if($("#jointOwnerTable tr").length	!=2 ) {			
			$(this).parent().parent().remove();					
			reOrderJointOwnerTableSequence('.jointOwner');
		}
			 else {
				var errorList = [];
				errorList.push(getLocalMessage("unit.Firstrowcannotberemove"));
				displayErrorsOnPage(errorList);
			}
	});
	$("#joTable").on('click','.addOwner',function(){
		var errorList = [];	
		if (errorList.length == 0) {
				var content = $(this).closest('tr').clone();
				$(this).closest("tr").after(content);
				content.find("input:text").val('');
				content.find("select").val('0');	
				content.find("input:hidden").remove();
				$('.error-div').hide();				
				reOrderJointTransferOwnerTableSequence('.jointOwner');				
				return false;
			} else {
			displayErrorsOnPage(errorList);
		}
		
	}); 
	
	/*To remove row from Joint Owner table and Reorder ID and Path*/
	$("#joTable").on('click','.remOwner',function(){
		var rowId=this.id;	
		var leftText = rowId.lastIndexOf("_");
		var deletedOwnerRowId = rowId.substring(leftText+1);
		var data = {"deletedOwnerRowId" : deletedOwnerRowId};
		var mutationLevelFlag = $("#mutationLevelFlag").val();
		if('M' == mutationLevelFlag){
			var URL = 'MutationForm.html?deleteOwnerTable';
		}else{
			var URL = 'SelfAssessmentForm.html?deleteOwnerTable';
		}
		var returnData = __doAjaxRequest(URL, 'POST', data, false);
		
		if($("#joTable tr").length	!=2 ) {			
			$(this).parent().parent().remove();					
			reOrderJointTransferOwnerTableSequence('.jointOwner');
		}
			 else {
				var errorList = [];
				errorList.push(getLocalMessage("unit.Firstrowcannotberemove"));
				displayErrorsOnPage(errorList);
			}
	});
	
});




/* Function to reorder ID and Path after deletion/addition of Row*/
function reOrderJointOwnerTableSequence(className) {
	$(className).each(function(i) {
		$(this).find("input:text:eq(0)").attr("id", "assoOwnerName_"+i);
		/*$(this).find("select:eq(0)").attr("id", "ownerGender_"+i);
		$(this).find("select:eq(1)").attr("id", "ownerRelation_"+i);

		$(this).find("input:text:eq(1)").attr("id", "assoGuardianName_"+i);*/
		//$(this).find("input:text:eq(1)").attr("id", "assoOwnerNameReg_"+i);
		$(this).find("input:text:eq(1)").attr("id", "assoPropertyShare_"+i);
		$(this).find("input:text:eq(2)").attr("id", "assoMobileno_"+i);
		$(this).find("input:text:eq(2)").attr("onchange", "fetchPropertyDetilsByMobileNoWithId("+i+")");
		$(this).find("input:text:eq(3)").attr("id", "emailId_"+i);
		$(this).find("input:text:eq(4)").attr("id", "assoAddharno_"+i);
		$(this).find("input:text:eq(5)").attr("id", "pannumber"+i);
		$(this).find("a:eq(1)").attr("id", "#deleteOwnerRow_"+i);

		
		// re-ordering path binding
		$(this).find("input:text:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].assoOwnerName");
		/*$(this).find("select:eq(0)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].genderId");
		$(this).find("select:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].relationId");
		$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].assoGuardianName");*/
		//$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].assoOwnerNameReg");
		$(this).find("input:text:eq(1)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].propertyShare");		
		$(this).find("input:text:eq(2)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].assoMobileno");
		$(this).find("input:text:eq(3)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].eMail");
		$(this).find("input:text:eq(4)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].assoAddharno");
		$(this).find("input:text:eq(5)").attr("name","provisionalAssesmentMstDto.provisionalAssesmentOwnerDtlDtoList["+i+"].pannumber");
		
		var langFlag = getLocalMessage('admin.lang.translator.flag');
		if(langFlag ==='Y'){
			$("#assoOwnerName, #assoOwnerName_"+i).keyup(function(event){
				var no_spl_char;
				no_spl_char = $("#assoOwnerName, #assoOwnerName_"+i).val().trim();
				/*if(no_spl_char!='')
				{
				//	commonlanguageTranslate(no_spl_char,'assoOwnerNameReg',event,'');
			//		commonlanguageTranslate(no_spl_char,'assoOwnerNameReg_'+i,event,'');
				}else{
				//	$("#assoOwnerNameReg").val('');
					//$("#assoOwnerNameReg_"+i).val('');
				}*/
			});
			
			}
	});
	
}

function reOrderJointTransferOwnerTableSequence(className) {
	
	$(className).each(function(i) {
		
		$(this).find("input:text:eq(0)").attr("id", "assoOwnerName_"+i);
		$(this).find("input:text:eq(1)").attr("id", "assoOwnerNameReg_"+i);
		/*$(this).find("select:eq(0)").attr("id", "ownerGender_"+i);
		$(this).find("select:eq(1)").attr("id", "ownerRelation_"+i);
		$(this).find("input:text:eq(2)").attr("id", "assoGuardianName_"+i);*/
		$(this).find("input:text:eq(2)").attr("id", "assoPropertyShare_"+i);
		$(this).find("input:text:eq(3)").attr("id", "assoMobileno_"+i);
		$(this).find("input:text:eq(4)").attr("id", "emailId_"+i);
		$(this).find("input:text:eq(5)").attr("id", "assoAddharno_"+i);
		$(this).find("input:text:eq(6)").attr("id", "pannumber"+i);
		$(this).find("a:eq(1)").attr("id", "#deleteOwnerRow_"+i);

		
		// re-ordering path binding
		$(this).find("input:text:eq(0)").attr("name","propTransferDto.propTransferOwnerList["+i+"].ownerName");
		$(this).find("input:text:eq(1)").attr("name","propTransferDto.propTransferOwnerList["+i+"].assoOwnerNameReg");
		/*$(this).find("select:eq(0)").attr("name","propTransferDto.propTransferOwnerList["+i+"].genderId");
		$(this).find("select:eq(1)").attr("name","propTransferDto.propTransferOwnerList["+i+"].relationId");
		$(this).find("input:text:eq(2)").attr("name","propTransferDto.propTransferOwnerList["+i+"].guardianName");*/
		$(this).find("input:text:eq(2)").attr("name","propTransferDto.propTransferOwnerList["+i+"].propertyShare");		
		$(this).find("input:text:eq(3)").attr("name","propTransferDto.propTransferOwnerList["+i+"].mobileno");
		$(this).find("input:text:eq(4)").attr("name","propTransferDto.propTransferOwnerList["+i+"].eMail");
		$(this).find("input:text:eq(5)").attr("name","propTransferDto.propTransferOwnerList["+i+"].addharno");
		$(this).find("input:text:eq(6)").attr("name","propTransferDto.propTransferOwnerList["+i+"].panno");
	});
	
}

function fnValidatePAN(Obj) {
	
	$('.error-div').hide();
	var errorList = [];	
    if (Obj == null) Obj = $('#pannumber').val();
    if (Obj.value != "") {
        ObjVal = Obj.value;
        var panPat = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
        var code = /([C,P,H,F,A,T,B,L,J,G])/;
        var code_chk = ObjVal.substring(3,4);
        if (ObjVal.search(panPat) == -1) {
        	errorList.push('Invaild PAN Number');
        	$('#pannumber').val("");
        }else if(code.test(code_chk) == false) {
        	errorList.push('Invaild PAN Number');
        	$('#pannumber').val("");
        }
    }
    
    if (errorList.length > 0) {   	
    	displayErrorsOnPage(errorList);
	} 
};

function loadOccupierNameAtAuth() {
	var serviceShortCode = $('#serviceShortCode').val();
	if('CIA' == serviceShortCode){
		reOrderChangeAssessUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
	}else{
		reOrderUnitTabIdSequence('.firstUnitRow','.secondUnitRow');	
	}
	var value = null;
	var ownerTypeCode = $("#ownerTypeId").find("option:selected").attr('code');
	
		$('.secondUnitRow').each(function(j) {
			value = $("#assdOccupancyType"+j).find("option:selected").attr('code');
			if('JO' == ownerTypeCode && 'OWN' == value){
				var ownerName = $("#assoOwnerName_0").val();
		//		var ownerNameReg = $("#assoOwnerNameReg_0").val();
				$("#occupierName"+j).val(ownerName);
			//	$("#occupierNameReg"+j).val(ownerNameReg);
				$('#actualRentDiv'+j).hide();
			}else if ('OWN' == value) {
				var ownerName = $("#assoOwnerName").val();
				var ownerNameReg = $("#assoOwnerNameReg").val();
				$("#occupierName"+j).val(ownerName);
			//	$("#occupierNameReg"+j).val(ownerNameReg);
				$('#actualRentDiv'+j).hide();
			}else{
				$('#actualRentDiv'+j).show();
			}
		});
}


function clearOccupancyDetails(){
	
	var rowCount = $('#unitDetailTable tr').length;
	if(rowCount > 3){
		for(var i=0; i<rowCount; i++){
			$("#occupierName"+i).val('');
			$('#assdOccupancyType' + i).val('0').trigger('chosen:updated');
		}
	}else{
		$("#occupierName").val('');
		$('#assdOccupancyType').val('0').trigger('chosen:updated');
	}
	
	
}


function chg_translate(obj,EleName,outEleID){
	var eleID=$(obj).attr('id');
	if(eleID == EleName){
		outputID=outEleID;
	}else{
		var splString = eleID.split(EleName);
		var numstring=splString[1];
		outputID=outEleID+numstring;
	}
	var eleValue=$('#'+eleID).val();
	var langFlag = getLocalMessage('admin.lang.translator.flag');
	if(langFlag ==='Y'){
			//var no_spl_char;
			var test_var = eleValue.trim();
			if(test_var!='')
			{
				commonlanguageTranslate(test_var,outputID,event,'');
				//commonlanguageTranslate(no_spl_char,'assoOwnerNameReg_0',event,'');
			}else{
				$("#"+outputID).val('');
				//$("#assoOwnerNameReg_0").val('');
			}
		}
}
