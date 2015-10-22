google.load('visualization', '1.0', {'packages':['corechart']});

function drawUserEODSignalChart(rowData){
	var data = new google.visualization.DataTable(); 	
		data.addColumn('date', 'Date');		    
		data.addColumn('number', 'EOD Price');		    
		data.addColumn('number', 'Signal Price');		    
		data.addColumn({type: 'string', role:'annotation'});		    
		data.addColumn('number', 'Your Buy');		    
		data.addColumn('number', 'Your Sell');  
		data.addRows(rowData);
		new google.visualization.LineChart(document.getElementById('chartDisplayDiv')).draw(
		data, {      
			series: {        
  			0: {				
  				curveType: "function"			   
  			},        
  			1: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			},        
  			2: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			},        
  			3: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			}      
  		}    
		});
}

function drawUserEODChart(rowData) {
	var data = new google.visualization.DataTable(); 	
		data.addColumn('date', 'Date');		    
		data.addColumn('number', 'EOD Price');
		data.addColumn('number', 'Your Buy');		    
		data.addColumn('number', 'Your Sell'); 
		data.addRows(rowData);
		new google.visualization.LineChart(document.getElementById('chartDisplayDiv')).draw(
		data, {      
			series: {        
  			0: {				
  				curveType: "function"			   
  			},        
  			1: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			},        
  			2: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			}   
  		}    
		});
}



function drawEODSignalChart(rowData) {
	var data = new google.visualization.DataTable(); 	
		data.addColumn('date', 'Date');		    
		data.addColumn('number', 'EOD Price');		    
		data.addColumn('number', 'Signal Price');		    
		data.addColumn({type: 'string', role:'annotation'});		    
		data.addRows(rowData);
		new google.visualization.LineChart(document.getElementById('chartDisplayDiv')).draw(
		data, {      
			series: {        
  			0: {				
  				curveType: "function"			   
  			},        
  			1: {          
  				lineWidth: 0,          pointSize: 5,          visibleInLegend: true        
  			}   
  		}    
		});
}
