$(document).ready(function() {
	$("#codDwzid2").on('change', function() {
		var wardVal = $('#codDwzid2 option:selected').attr('code');
		var selectobject = document.getElementById("codDwzid3");
		for (var i=0; i<selectobject.length; i++) {
		    if (selectobject.options[i].getAttribute('code') == 'ALL_'+wardVal){
		    	selectobject.remove(i);
		    }		        
		}
	});	
});

function calculatePenalty(element){

	showloader(true);
	setTimeout(function(){calculateBillPenalty(element)},2000);
}

function calculateBillPenalty(element){

	return saveOrUpdateForm(element,
			"Bill generation  done successfully",
			'AdminHome.html', 'saveform');
}
function resetFormDetails() {
    $('input[type=text]').val('');
    $('#codDwzid1, #codDwzid2, #codDwzid3').val('0');
    $("#validationerrordiv").html('');
}