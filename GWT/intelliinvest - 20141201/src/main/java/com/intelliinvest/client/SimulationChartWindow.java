package com.intelliinvest.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;

public class SimulationChartWindow {
	static final Window simulationChartWindow = new Window();
	static HTMLPane htmlPane = new HTMLPane();
	
	static{
		simulationChartWindow.setOverflow(Overflow.VISIBLE);
		simulationChartWindow.setWidth("463");
		simulationChartWindow.setHeight("290");
        simulationChartWindow.setShowMinimizeButton(false);  
        simulationChartWindow.setIsModal(true);  
        simulationChartWindow.setPadding(0);
		simulationChartWindow.setShowCloseButton(true);
		simulationChartWindow.centerInPage();
		simulationChartWindow.addItem(htmlPane);
	}

	public static void showSimulationWindow(final String code, final String type){
		
		IntelliInvestServiceFactory.utilService.getChartData(type, code, IntelliInvest.userDetailData.getUserId(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				simulationChartWindow.clear();
				htmlPane.destroy();
				htmlPane = null;
				htmlPane = new HTMLPane();
				htmlPane.setWidth("450");
				htmlPane.setHeight("250");
				htmlPane.setAlign(Alignment.CENTER);
		        htmlPane.setOverflow(Overflow.VISIBLE);
		        htmlPane.setContentsType(ContentsType.FRAGMENT);
		        htmlPane.setContents("<body><div width='450' height='250' id='chartDisplayDiv'></div></body>");
		        htmlPane.setEvalScriptBlocks(true);
		        simulationChartWindow.addItem(htmlPane);
				simulationChartWindow.setTitle("Simulation for " + code);
				simulationChartWindow.show();
				if(type.startsWith("eodSignalChart")){
					drawEODSignalChart(JSOHelper.eval(result));
				}else if(type.startsWith("userEODChart")){
					drawUserEODChart(JSOHelper.eval(result));
				}else if(type.startsWith("userEODSignalChart")){
					drawUserEODSignalChart(JSOHelper.eval(result));
				}else{
					simulationChartWindow.setContents("Error fetching chart. Type of chart specified not present");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("Error fetching chart");
			}
		});
		
	}
	
	
	
	public static native void drawEODSignalChart(JavaScriptObject rowData)  /*-{
		try
  		{
			$wnd.drawEODSignalChart(rowData);
  		}catch(err){
  		}
	}-*/;
	
	public static native void drawUserEODChart(JavaScriptObject rowData)  /*-{
		try
  		{
	 		$wnd.drawUserEODChart(rowData);
	 	}catch(err){
  		}
	}-*/;
	public static native void drawUserEODSignalChart(JavaScriptObject rowData)  /*-{
		try
  		{
 			$wnd.drawUserEODSignalChart(rowData);
 		}catch(err){
  		}
	}-*/;
	
}
