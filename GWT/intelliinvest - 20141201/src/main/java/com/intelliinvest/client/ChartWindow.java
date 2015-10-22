package com.intelliinvest.client;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class ChartWindow {

	static public String stockCode = "";
	static public String chartType = "";
	static Img img = null;
	static final Window chartWindow = new Window();

	public static Window getChartWindow() {
		return chartWindow;
	}
	
	public static Window initialize(){
		chartWindow.setWidth(550);  
        chartWindow.setHeight(360);  
        chartWindow.setTitle("Charts");  
        chartWindow.setShowMinimizeButton(false);  
        chartWindow.setShowCloseButton(true);
        chartWindow.setIsModal(true);  
        chartWindow.centerInPage();  
        chartWindow.setPadding(10);
        
        VStack chartVStack = new VStack();
        
        HStack chartButtonStack = new HStack();
        chartButtonStack.setHeight(25);
        IButton big1Day = new IButton("1D");
        big1Day.setWidth("60");
        big1Day.addClickHandler(new ChartButtonClickHandler("1D"));
        IButton big5Day = new IButton("5D");
        big5Day.setWidth("60");
        big5Day.addClickHandler(new ChartButtonClickHandler("5D"));
        IButton big1Month = new IButton("1M");
        big1Month.setWidth("60");
        big1Month.addClickHandler(new ChartButtonClickHandler("1M"));
        IButton big3Months = new IButton("3M");
        big3Months.setWidth("60");
        big3Months.addClickHandler(new ChartButtonClickHandler("3M"));
        IButton big6Months = new IButton("6M");
        big6Months.setWidth("60");
        big6Months.addClickHandler(new ChartButtonClickHandler("6M"));
        IButton big1Year = new IButton("1Y");
        big1Year.setWidth("60");
        big1Year.addClickHandler(new ChartButtonClickHandler("1Y"));
        IButton bigMax = new IButton("Max");
        bigMax.setWidth("60");
        bigMax.addClickHandler(new ChartButtonClickHandler("Max"));
        IButton bigInteractive = new IButton("Interactive");
        bigInteractive.setWidth("60");
        bigInteractive.addClickHandler(new ChartButtonClickHandler("Interactive"));
        chartButtonStack.addMember(big1Day);
        chartButtonStack.addMember(big5Day);
        chartButtonStack.addMember(big1Month);
        chartButtonStack.addMember(big3Months);
        chartButtonStack.addMember(big6Months);
        chartButtonStack.addMember(big1Year);
        chartButtonStack.addMember(bigMax);
//        chartButtonStack.addMember(bigInteractive);
        
        chartVStack.addMember(chartButtonStack);
        
        img = new Img("/intelliinvest/stockchart.chart?chartType=default&amp;stockCode=default" , 500, 300);
        chartVStack.addMember(img);
        
        chartWindow.addItem(chartVStack);
        
        chartWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				chartWindow.animateHide(AnimationEffect.WIPE);
			}
		});
        chartWindow.setVisible(false);
        return chartWindow;
	}
	
	public static void refresh(){
		if(stockCode.equals("")){
			SC.say("Please select a stock Code");
		}else{
			img.setSrc("/intelliinvest/stockchart.chart?chartType=" + chartType + "&amp;stockCode=" + stockCode );
			img.redraw();
		}
	}
	
}

class ChartButtonClickHandler implements ClickHandler{
	
	String value = "";
	public ChartButtonClickHandler(String value) {
		this.value = value;
	}
	@Override
	public void onClick(ClickEvent event) {
		ChartWindow.chartType = value;
		ChartWindow.refresh();
	}
}