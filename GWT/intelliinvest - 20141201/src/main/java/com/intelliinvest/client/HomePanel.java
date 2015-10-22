package com.intelliinvest.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class HomePanel implements IntelliInvestPanel{
	final Layout homePanelLayout;
	final HTMLFlow homeIntroHtml;
	Boolean resize = false;
	
	public HomePanel() {
		this.homePanelLayout = new VLayout();
		homeIntroHtml = new HTMLFlow();
		initialize();
	}
	
	@Override
	public boolean isVisible() {
		return homePanelLayout.isVisible();
	}
	
	public Layout getHomePanelLayout() {
		return homePanelLayout;
	}
	
	@Override
	public void markForResize() {
		this.resize = true;
	}
	
	@Override
	public void resize() {
		if(!resize){
			return;
		}
		resize = false;
		
		homeIntroHtml.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		homePanelLayout.setWidth(IntelliInvest.WIDTH - 225 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		homePanelLayout.setHeight(IntelliInvest.HEIGHT-165);
		
	}
	
	public void initialize(){
		homePanelLayout.setWidth(IntelliInvest.WIDTH-222-(IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		homePanelLayout.setHeight(IntelliInvest.HEIGHT-155);
		homePanelLayout.setStyleName("intelliinvest");		
		homeIntroHtml.setID("homeIntroHtml");
//		homeIntroHtml.setBackgroundImage("/data/images/intelliinvest_home.jpg");
//		homeIntroHtml.setBackgroundRepeat(BackgroundRepeat.REPEAT);
		homeIntroHtml.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		homeIntroHtml.setHeight(IntelliInvest.HEIGHT-165);
        homeIntroHtml.setOverflow(Overflow.AUTO);   
        
        IntelliInvestServiceFactory.blogsService.getBlogContents(new Integer(0), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				homeIntroHtml.setContents(result);
				homeIntroHtml.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				homeIntroHtml.setContents("Error");
			}
		});
        
//        String contents = "<font color=#000000; size=2>"
//        					+ "<br><b>Intelliinvest</b> is there to guide you through the ups and downs of the market and help you make informed decision about "
//        					+ "which stocks to hold to sell or to buy. We believe that retail investors are the one to suffer at the hands of institutional investor "
//        					+ "as they dont have power to manipulate prices because of their small buying/selling potential. "
//        					+ "Hence we believe we need to look into it in a technical manner. "
//        					+ "We would like every retail investor to do technical trading. "
//        					+ "But given the complexity around analysis , we have tried to bring all analysis at one place for retail investor to take a call."
//        					+ "<br><br>This site helps you manage your equity portfolio and provides you trading signals based on technical analysis. "
//        					+ "It also provides you all information related to stocks for you to make a final trading call. "
//        					+ "There is a team sitting to help you with your queries and provide you necessary guidance. "
//        					+ "If there is a stock you are interested in but for which analysis is not available, "
//        					+ "raise it to our support team and they will make sure its there for you the very next day."
//        					+ "<br<br>We wish you a very happy trading experience with us.  "
//        					+ "As for the disclaimer, we really don't know what the future has in store for us, else we would be Gods. "
//        					+ "What we can best do is to predict based on the publicly available information as of date and that information is embedded into the prices. "
//        					+ "So please take the signals as mere advice and use your own discretion in making a trading call."
//        					+ "</font>"
//        					;   
  
//        homeIntroHtml.setContents(contents);
        
        homePanelLayout.addMember(homeIntroHtml);
        
	}

}
