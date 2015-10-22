package com.intelliinvest.client;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class StaticWindow {

	static final Window window = new Window();
	static final HTMLFlow htmlFlow = new HTMLFlow();
	
	static {
		initialize();
	}
	
	public static Window getChartWindow() {
		return window;
	}
	
	public static Window initialize(){
		window.setOverflow(Overflow.AUTO);
        window.setShowMinimizeButton(false);  
        window.setShowCloseButton(true);
        window.setIsModal(true);  
        window.centerInPage();  
        window.setPadding(0);
        window.setMembersMargin(0);
        window.setMargin(0);
        
        htmlFlow.setOverflow(Overflow.AUTO);
        htmlFlow.setStyleName("staticWindowCss");
        htmlFlow.setPadding(5);
        
        window.addItem(htmlFlow);
        
        window.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				window.animateHide(AnimationEffect.WIPE);
			}
		});
        window.setVisible(false);
        return window;
	}
	
	public static void show(int width, int height, String title, String contents) {
			window.setTitle(title);
			window.setWidth(width);
			window.setHeight(height);
			
			htmlFlow.setWidth("100%");
			htmlFlow.setHeight("100%");
			
			htmlFlow.setContents(contents);
			window.centerInPage(); 
			window.animateShow(AnimationEffect.WIPE);
	}
}