function calculatePenalty(element){

	showloader(true);
	setTimeout(function(){startCalculatePenalty(element)},2);
}

function startCalculatePenalty(element){

	return saveOrUpdateForm(element,
			"Bill generation  done successfully",
			'PropertyAsOnDatePenaltyGenLog.html', 'saveform');
}