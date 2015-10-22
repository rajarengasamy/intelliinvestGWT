function drawChart(chartData){
	var chart = AmCharts.makeChart("intraDayChartDiv", {
		type: "stock",
	    "theme": "light",
	    pathToImages: "http://www.amcharts.com/lib/3/images/",
	    categoryAxesSettings: {
	    	maxSeries: 0,
	    	minPeriod: "mm",
	    	equalSpacing: true
	    },
		dataSets: [{
			color: "#b0de09",
			fieldMappings: [{
				fromField: "value",
				toField: "value"
			},{
				fromField: "volume",
				toField: "volume"
			}],
			dataProvider: chartData,
			categoryField: "date"
		}],

		panels: [{
			showCategoryAxis: false,
			title: "Price",
			allLabels: [{
				x: 0,
				y: 0,
				text: "Click on the pencil icon on top-right to start drawing trend lines",
				align: "center",
				size: 12
			}],

			stockGraphs: [{
				id: "g1",
				valueField: "value",
				useDataSetColors: false,
				lineThickness: 1
			}],


			stockLegend: {
				valueTextRegular: " ",
				markerType: "none"
			},

			drawingIconsEnabled: true
		},
		
		{
			title: "Volume",
			percentHeight: 20,
			stockGraphs: [{
				valueField: "volume",
				type: "column",
				cornerRadiusTop: 1,
				fillAlphas: 1
			}],

			stockLegend: {
				valueTextRegular: " ",
				markerType: "none"
			}
		}
		],

		chartScrollbarSettings: {
			graph: "g1"
		},
		chartCursorSettings: {
			valueBalloonsEnabled: true,
            categoryBalloonDateFormats: [{period:'mm',format:'DD/MM/YYYY JJ:NN'}]
		},
		periodSelector: {
			position: "bottom",
			dateFormat: "YYYY-MM-DD JJ:NN",
			inputFieldWidth: 150,
			periods: [{
				period: "DD",
				count: 1,
				label: "1 Day"
			}, {
				period: "DD",
				count: 7,
				label: "Week"
			}, {
				period: "MM",
				count: 1,
				label: "1 M"
			}, {
				period: "MM",
				count: 3,
				label: "3 M"
			}, {
				period: "MM",
				count: 6,
				label: "6 M"
			}, {
				period: "YTD",
				label: "YTD"
			}, {
				period: "MAX",
				label: "MAX"
			}]
		}
	});
	
}
