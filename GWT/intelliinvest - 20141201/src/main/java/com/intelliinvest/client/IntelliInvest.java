package com.intelliinvest.client;

import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.data.StockDetailData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.StockPriceData;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.CallbackUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IntelliInvest implements EntryPoint {
	public static RootPanel rootPanel;
	public static UserDetailData userDetailData;
	public static HeaderPanel HEADER_PANEL = null;
	public static LeftPanel LEFT_PANEL = null;
	public static MiddlePanel MIDDLE_PANEL = null;
	public static RightPanel RIGHT_PANEL = null;
	public static FooterPanel FOOTER_PANEL = null;
	public static String role;
	public static int MIN_HEIGHT = 	600;
	public static int MIN_WIDTH = 	1200;
	public static int HEIGHT = 	600;
	public static int WIDTH = 	1200;
	public static int IDLE_TIME=0;
	public static boolean IDLE_WARNING_SHOWN = false;
	public static int mouseX = 0;
	public static int mouseY = 0;
	
	public void onModuleLoad() {
		initialize();
	}
	
	public void initialize(){
		IntelliInvestServiceFactory.initialize();
		loadStockDetailsWithEODPriceData();
	}
	
	public void loadScreen(){
		
//		WIDTH = getAvailWidth()-20;
//		HEIGHT = getAvailHeight() -20;
//		Window.enableScrolling(false);
		
		IntelliInvest.WIDTH = RootPanel.getBodyElement().getClientWidth();
		IntelliInvest.HEIGHT = RootPanel.getBodyElement().getClientHeight();
		
		IntelliInvest.WIDTH = (IntelliInvest.WIDTH>IntelliInvest.MIN_WIDTH?IntelliInvest.WIDTH:IntelliInvest.MIN_WIDTH);
		IntelliInvest.HEIGHT = (IntelliInvest.HEIGHT>IntelliInvest.MIN_HEIGHT?IntelliInvest.HEIGHT:IntelliInvest.MIN_HEIGHT);
		
		final VStack mainStack = new VStack();
//		mainStack.setStyleName("intelliinvest");
		mainStack.setPadding(0);
		mainStack.setMargin(0);
		mainStack.setMembersMargin(0);
		mainStack.setBorder("0px");
		mainStack.setAlign(Alignment.CENTER);
		
		HEADER_PANEL = new HeaderPanel();
		LEFT_PANEL = new LeftPanel();
		MIDDLE_PANEL = new MiddlePanel();
		RIGHT_PANEL = new RightPanel();
		FOOTER_PANEL = new FooterPanel();
		
		final SectionStack sectionStack = new SectionStack(); 
		sectionStack.setStyleName("intelliinvest");
		sectionStack.setOverflow(Overflow.VISIBLE);
		sectionStack.setMargin(0);
		sectionStack.setBorder("1px");
		sectionStack.setPadding(2);
		sectionStack.setVertical(false);
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);   
        
		final SectionStackSection leftSection = new SectionStackSection("");
		leftSection.addItem(LEFT_PANEL.getLeftLayout());
		leftSection.setExpanded(true);   
		leftSection.setCanCollapse(false);   
        sectionStack.addSection(leftSection);
        leftSection.setShowHeader(false);
        
        final SectionStackSection middleSection = new SectionStackSection("");
		middleSection.addItem(MIDDLE_PANEL.getMiddleLayout());
		middleSection.setExpanded(true);   
		middleSection.setCanCollapse(false);
		middleSection.setShowHeader(false);
        sectionStack.addSection(middleSection);
        
        
		SectionStackSection rightSection = new SectionStackSection("");
		rightSection.addItem(RIGHT_PANEL.getRightLayout());
		rightSection.setExpanded(true);   
		rightSection.setCanCollapse(false);   
		rightSection.setShowHeader(false);
		sectionStack.addSection(rightSection);
        
		LEFT_PANEL.getLeftLayout().addVisibilityChangedHandler(new VisibilityChangedHandler() {
			
			@Override
			public void onVisibilityChanged(VisibilityChangedEvent event) {
				MIDDLE_PANEL.markForResize();
				LEFT_PANEL.markForResize();
				RIGHT_PANEL.markForResize();
				MIDDLE_PANEL.resize();
			}
		});
		
		final Timer timer = new Timer() {
			@Override
			public void run() {
				HEADER_PANEL.markForResize();
				MIDDLE_PANEL.markForResize();
				LEFT_PANEL.markForResize();
				RIGHT_PANEL.markForResize();
				FOOTER_PANEL.markForResize();
				
				HEADER_PANEL.resize();
				MIDDLE_PANEL.resize();
				LEFT_PANEL.resize();
				RIGHT_PANEL.resize();
				FOOTER_PANEL.resize(); 
			}
		};
		
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				int previousHeight = IntelliInvest.HEIGHT;
				int previousWidth = IntelliInvest.WIDTH;
				
				IntelliInvest.WIDTH = RootPanel.getBodyElement().getClientWidth();
				IntelliInvest.HEIGHT = RootPanel.getBodyElement().getClientHeight();
				
				IntelliInvest.WIDTH = (IntelliInvest.WIDTH>IntelliInvest.MIN_WIDTH?IntelliInvest.WIDTH:IntelliInvest.MIN_WIDTH);
				IntelliInvest.HEIGHT = (IntelliInvest.HEIGHT>IntelliInvest.MIN_HEIGHT?IntelliInvest.HEIGHT:IntelliInvest.MIN_HEIGHT);
				
				if(previousHeight!=IntelliInvest.HEIGHT || previousWidth!=IntelliInvest.WIDTH){
					timer.schedule(500);
				}
				
			}
		});
		
		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			
			@Override
			public void onWindowClosing(ClosingEvent event) {
				event.setMessage("Closing browser will exit IntelliInvest screen . . .");
			}
		});
		
		mainStack.addMember(HEADER_PANEL.getHeaderLayout());
		mainStack.addMember(sectionStack);
		mainStack.addMember(FOOTER_PANEL.getFooterLayout());
		
		mainStack.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				IntelliInvest.IDLE_TIME = 0;
			}
		});
		mainStack.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if(event.getX()!=mouseX || event.getY()!=mouseY){
					IntelliInvest.IDLE_TIME = 0;
					mouseX = event.getX();
					mouseY = event.getY(); 
				}
			}
		});
		
		mainStack.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				IntelliInvest.IDLE_TIME = 0;
			}
		});
		
		RootPanel.get("loadingWrapper").getElement().removeFromParent();
		
		mainStack.draw();
	}
	
	
//	public static native int getAvailWidth()  /*-{
//		return $wnd.screen.availWidth;
//	}-*/;
//	
//	public static native int getAvailHeight()  /*-{
//		return $wnd.screen.availHeight;
//	}-*/;
	
	public void loadStockDetailsWithEODPriceData(){
		IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(Constants.INTELLI_INVEST_STATIC_DATA_REQUEST);
		IntelliInvestServiceFactory.stockDetailsService.fetch(intelliInvestRequest, new AsyncCallback<IntelliInvestResponse>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(IntelliInvestResponse response) {
				StockDetailStaticHolder.stockDetailsMap = (HashMap<String, StockDetailData>) response.getScreenData().get(Constants.STOCK_DETAILS);
//				StockDetailStaticHolder.intelliStockDetailsMap = (HashMap<String, StockDetailData>) response.getScreenData().get(Constants.INTELLI_STOCK_DETAILS);
				StockDetailStaticHolder.stockCurrentPriceMap = (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.STOCK_CURRENT_PRICES);
				StockDetailStaticHolder.stockPreviousPriceMap = StockDetailStaticHolder.stockCurrentPriceMap;
				StockDetailStaticHolder.stockEODPriceMap =  (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.STOCK_EOD_PRICES);
				StockDetailStaticHolder.worldStockPriceMap = (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.WORLD_STOCK_PRICES);
				StockDetailStaticHolder.NIFTYStockPriceMap = (HashMap<String,StockPriceData>)response.getScreenData().get(Constants.NIFTY_STOCK_PRICES);
				loadScreen();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("Screen Initialization failed. Refresh screen, if problem persist contact admin");
			}
		});
	}
	
	public static void refreshCurrentPrice(final boolean showDoneMessage, final CallbackUtil callbackUtil) {
		IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(Constants.INTELLI_INVEST_CURRENT_PRICE_DATA_REQUEST);
		IntelliInvestServiceFactory.stockDetailsService.fetch(intelliInvestRequest, new AsyncCallback<IntelliInvestResponse>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(IntelliInvestResponse response) {
				StockDetailStaticHolder.stockPreviousPriceMap = StockDetailStaticHolder.stockCurrentPriceMap;
				StockDetailStaticHolder.stockCurrentPriceMap = (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.STOCK_CURRENT_PRICES);
				StockDetailStaticHolder.NIFTYStockPriceMap = (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.NIFTY_STOCK_PRICES);
				StockDetailStaticHolder.worldStockPriceMap = (HashMap<String, StockPriceData>) response.getScreenData().get(Constants.WORLD_STOCK_PRICES);
				
				if(null!=callbackUtil){
					callbackUtil.finished();
				}
				if(showDoneMessage){
					SC.say("Refresh done successfully");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(null!=callbackUtil){
					callbackUtil.finished();
				}
				if(showDoneMessage){
					SC.say("Error while refreshing current prices");
				}
			}
		});
	}
}