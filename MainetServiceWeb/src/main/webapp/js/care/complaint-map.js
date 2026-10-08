function chooseAddr(lat1, lng1)
	{
	 myMarker.closePopup();
	 map.setView([lat1, lng1],14);
	 myMarker.setLatLng([lat1, lng1]);
	 lat = lat1.toFixed(14);
	 lon = lng1.toFixed(14);
	 document.getElementById('lat').value = lat;
	 document.getElementById('lon').value = lon;
	 myMarker.bindPopup("Lat " + lat + "<br />Lon " + lon).openPopup();
	}
function resetmarker(lat1, lng1)
	{
	 myMarker.closePopup();
	 map.setView([lat1, lng1],12);
	 myMarker.setLatLng([lat1, lng1]);
	 lat = lat1.toFixed(8);
	 lon = lng1.toFixed(8);
	 myMarker.bindPopup("Lat " + lat + "<br />Lon " + lon).openPopup();
	} 
function getAddressforJSon(arr)
{
	 
	 var logx=[];
	 var laty=[];
	//console.log(arr);
	if(arr['candidates'].length == 0){
		document.getElementById('results').innerHTML = "No Address Found";
		 $('#results').addClass('alert alert-danger');
	}else{
		
		
		for(i = 0; i < arr['candidates'].length; i++){
				console.log(arr['candidates'][i]['attributes'].City);
				logx.push(arr['candidates'][0]['location'].y);
				laty.push(arr['candidates'][0]['location'].x);
			}
			
			chooseAddr(logx[0],laty[0]);
			 document.getElementById('results').innerHTML = "";
			 $('#results').removeClass('alert alert-danger');
			 findWardZone(logx[0],laty[0]);
		}
	
}

	function addr_search()
	{
		
	 var inp = $('#Landmark').val();
	 inp=inp.replace(/[^a-zA-Z ]/g, "");
	 var addrstr="&outfields=city,type&searchExtent=73.076932,19.142670,73.225719,19.313508&f=json";
	 var xmlhttp = new XMLHttpRequest();
	 var url = "https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/findAddressCandidates?&singleLine=" + inp+""+addrstr;
	// console.log(url);
	 xmlhttp.onreadystatechange = function()
	 {
	  
	    var myArr = JSON.parse(this.responseText);
	    getAddressforJSon(myArr);
	 
	 };
	 xmlhttp.open("GET", url, true);
	 xmlhttp.send();
	}
	function hideSearchResult(){
		$('#results').hide();
	}
	
function findWardZone(lat1, lng1){
	var deptMap = $('#departmentComplaint').find(":selected").attr('code');
	if(deptMap == 'BND' || deptMap == 'AS' || deptMap == 'WT'){
		
	}else{
		var url = 'https://maps.kdmc.gov.in/agserver/rest/services/SKDCL_BASEMAP_24_03_2022/MapServer/13/query?where=&text=&objectIds=&time=&geometry={"x" :'+lng1+', "y" :'+lat1+', "spatialReference" : {"wkid" : 4326}}&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelWithin&relationParam=&outFields=WARD_ID&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&having=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&historicMoment=&returnDistinctValues=false&resultOffset=&resultRecordCount=&queryByDistance=&returnExtentOnly=false&datumTransformation=&parameterValues=&rangeValues=&quantizationParameters=&featureEncoding=esriDefault&f=geojson';
		 var xmlhttp = new XMLHttpRequest();
		 xmlhttp.onreadystatechange = function()
		 {
		  
		    var myArr = JSON.parse(this.responseText);
		    //getAddressforJSon(myArr);
		    if(myArr['features'].length == 0){
		    	$('#ward1 option[value=0]').attr('selected','selected').change();
				$('#ward2 option[value=0]').attr('selected','selected').change();
				$('#Landmark').val('');
		    	let yes = "Ok";
		    	let warnMsg ="Address Not Found in Kalyan Boundries";
		    	 message	='<p class="text-blue-2 text-center padding-15">'+ warnMsg+'</p>';
				 message	+='<div class=\'text-center padding-bottom-10\'>'+	
				'<input class="btn btn-success" type=\'button\' value=\''+yes+'\'  id=\'yes\' '+ 
				' onclick="warningClose()"/>'+	
				'</div>';
				$(childDivName).addClass('ok-msg').removeClass('warn-msg');
				$(childDivName).html(message);
				$(childDivName).show();
				$('#yes').focus();
				showModalBoxWithoutClose(childDivName);
		    }else{
		    	putWardZoneval(myArr['features'][0].properties.ward_id);
			    console.log(myArr['features'][0].properties.ward_id);
		    }
		    
		    
		 };
		 xmlhttp.open("GET", url, true);
		 xmlhttp.send();
	}
	
	
	
	//console.log(url);
}

function warningClose(){
	$.fancybox.close();
	resetmarker(startlat,startlon);
}



function putWardZoneval(ward){
	var warnMsg ="Address Not Found in Kalyan Boundries";
	if(ward == "" || ward == undefined || ward == null){
		$('#ward1 option[value=0]').attr('selected','selected').change();
		$('#ward2 option[value=0]').attr('selected','selected').change();
		 message	='<p class="text-blue-2 text-center padding-15">'+ warnMsg+'</p>';
		 message	+='<div class=\'text-center padding-bottom-10\'>'+	
		'<input class="btn btn-success" type=\'button\' value=\''+yes+'\'  id=\'yes\' '+ 
		' onclick="warningClose()"/>'+	
		'</div>';
		$(childDivName).addClass('ok-msg').removeClass('warn-msg');
		$(childDivName).html(message);
		$(childDivName).show();
		$('#yes').focus();
		showModalBoxWithoutClose(childDivName);
				
		
	}else{
		if(ward == 'E' || ward == 'F' || ward == 'G' || ward == 'H'){
			$('#ward1 option[code=12]').removeAttr('selected','selected').change();
			$('#ward1 option[code=05]').attr('selected','selected').change();
		}else{
			$('#ward1 option[code=05]').removeAttr('selected','selected').change();
			$('#ward1 option[code=12]').attr('selected','selected').change();
		}
		
		var wardMapping ;
		
		
		var wardValue = ward+" Ward";
		var wardArr ={'A':'W1', 'B':'02','C':'03','D':'04','E':'06','F':'07','G':'08','H':'09','I':'10','J':'11'}
			
		//var domibivili= {'E':'06','F':'07','G':'08','H':'09'};
		var wardMapping = wardArr[ward];
		$('#ward2 option[code='+wardMapping+']').attr('selected','selected').change();
	}
	
	
	/*if(ward == '')
	
		$('#ward1 option[code=5]').attr('selected','').change();
		$('#ward1 option[code=12]').attr('selected','selected').change();
		$('#ward2 option[code='+wardMapping+']').attr('selected','selected').change();
	if(wardMapping == "" || wardMapping == null){
		wardMapping =domibivili[ward];
		$('#ward1 option[code=12]').attr('selected','').change();
		$('#ward1 option[code=05]').attr('selected','selected').change();
		$('#ward2 option[code='+wardMapping+']').attr('selected','selected').change();
	}*/
	
	
}


function findAddressfromlatlon(lat1, lng1){
	url ="https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/reverseGeocode?&location="+lng1+","+lat1+"&f=json";
	var xmlhttp = new XMLHttpRequest();
	 xmlhttp.onreadystatechange = function()
	 {
	  
	    var myArr = JSON.parse(this.responseText);
	   // console.log(myArr['address']['Match_addr']);
	    $('#Landmark').val(myArr['address']['Match_addr']);
	    findWardZone(lat1, lng1);
	 };
	 xmlhttp.open("GET", url, true);
	 xmlhttp.send();
}