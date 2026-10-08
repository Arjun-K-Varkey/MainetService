$(function() {
	$("#workOrderGrid").jqGrid(
			{
				url : "PhysicalDateEntry.html?getGridData",datatype : "json",mtype : "GET",colNames : [ getLocalMessage("water.meterDet.appliNo"), getLocalMessage("MeterReadingDTO.csCcn"), getLocalMessage("water.meterDet.servName"), getLocalMessage("water.meterDet.appliName"), "ServiceShortCode",getLocalMessage("view.edit")],
				colModel : [ {name : "applicationNumber",index:'applicationNumber',width : 30,sortable : true,search : true}, 
				             {name :"connectionNo",index:'connectionNo',width :30,sorttable: false, search: true},
				             {name : "serviceName",width : 30,sortable : false,search : false },
				             {name : "applicantName",width : 20,sortable : false,search : false}, 
				             {name : "serviceShortCode",width : 20,sortable : false,search : false,hidden :true},
				             {name : 'applicationNumber',index : 'applicationNumber',width : 20,align : 'center',formatter : returnViewUrl,editoptions : {value : "Yes:No"},formatoptions : {disabled : false},search:false,sortable : false}
				            ],
				pager : "#pagered",
				rowNum : 10000,
				rowList : [ 5, 10, 20, 30 ],
				sortname : "applicationNumber",
				sortorder : "desc",
				height : 'auto',
				viewrecords : true,
				gridview : true,
				loadonce : true,
				jsonReader : {
					root : "rows",
					page : "page",
					total : "total",
					records : "records",
					repeatitems : false,
				},
				autoencode : true,
				caption : getLocalMessage("water.physConn.det")
			});
	 jQuery("#workOrderGrid").jqGrid('navGrid','#pagered',{edit:false,add:false,del:false,refresh:false}); 
	 $("#pagered_left").css("width", "");
});


function returnViewUrl(cellValue, options, rowdata, action) {

	return "<a href='#'  return false; class='editClass' value='"+rowdata.applicationNumber+"'><img src='css/images/edit.png' width='20px' alt='Edit Charge Master' title='Edit Scrutiny Data' /></a>";
}

$(function() {
	$(document).on('click', '.editClass', function() {
		var $link = $(this);
		var divName = '.content-page';
		var rowId = $link.closest('tr').find('td:eq(0)').text();
		var serviceShortCode = $link.closest('tr').find('td:eq(4)').text();
	
		if(serviceShortCode != '' && serviceShortCode == 'WNC'){
			var url = "PhysicalDateEntry.html?edit";
			var requestData = "rowId=" + rowId;
			var returnData =__doAjaxRequest(url,'post',requestData,false);
			
			$('.content').html(returnData);
			
			prepareDateTag();
		}else if(serviceShortCode != '' && serviceShortCode == 'WCC'){
			var url = "ExecuteDisConnectionProcess.html?showDetails";
			var requestData =  {
					"actualTaskId" : 0,
					"appNo" : rowId
					};
			var returnData =__doAjaxRequest(url,'post',requestData,false);
			
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			prepareTags();
		}else if(serviceShortCode != '' && serviceShortCode == 'WRC'){
			var url = "waterExecuteReconnectionForm.html?showDetails";
			var requestData =  {
					"actualTaskId" : 0,
					"appNo" : rowId
					};
			var returnData =__doAjaxRequest(url,'post',requestData,false);
			$(divName).removeClass('ajaxloader');
			$(divName).html(returnData);
			prepareTags();
		}
		
			
	});
		
		
});


function flagCheck(){
	
	 var checked = $('#physicalConnFlag').is(':checked');
	if(checked){
	 $('#connDate').attr('disabled',false);
	}else{
		$('#connDate').val('');
		$('#connDate').attr('disabled',true);
	}
}


function savePhysicalConnectionDateEntry(obj){
	
	return saveOrUpdateForm(obj, '', 'PhysicalDateEntry.html', 'saveform');
	/*var url = "PhysicalDateEntry.html?saveform";
	var data = $("#frmPhysicalDateEntry").serialize();
	
	var returnData =__doAjaxRequest(url,'post',data,false,'',obj);
	if ($.isPlainObject(returnData))
	{
	var message = returnData.command.message;
	if(message)
		{
			showSaveResultBox(returnData, message, 'PhysicalDateEntry.html');
		}else{
			showSaveResultBox(returnData, 'Please Try again..', 'PhysicalDateEntry.html');
		}
	}
	$("#widget").html(returnData);
	$("#widget").show();
	$(".warning-div").removeClass("hide");
	return false;*/
}

function preparePhysicalDateTag() {
	var dateFields = $('.lessthancurrdate');
	dateFields.each(function () {
		var fieldValue = $(this).val();
		if (fieldValue.length > 10) {
			$(this).val(fieldValue.substr(0, 10));
		}
	});
}
