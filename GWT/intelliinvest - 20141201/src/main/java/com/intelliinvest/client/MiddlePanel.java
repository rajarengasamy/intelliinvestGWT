package com.intelliinvest.client;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.util.CallbackUtil;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class MiddlePanel implements IntelliInvestPanel{
	final VLayout middlePanel;
	static TabSet middlePanelTabSet;
	static boolean automaticallyRefresh = false;
	Boolean resize = false;
	static ImgButton chatImg;
	
	HomePanel homePanel;
//	ManagePortfolioPanel managePortfolioPanel;
	NewManagePortfolioPanel newManagePortfolioPanel;
	TradingAccountPanel tradingAccountPanel;
	OurSuggestionsPanel ourSuggestionsPanel;
//	SimulationPanel simulationPanel;
	RiskReturnMatrixPanel riskReturnMatrixPanel;
	AdminPanel adminPanel;
	PaymentPanel paymentPanel;
	IntraDayChartPanel intraDayChartPanel;
	ChatPanel chatPanel;
	BlogsPanel blogsPanel;
	
//	Tab homeTab;
//	Tab managePortfolioTab;
	Tab newManagePortfolioTab;
	Tab tradingAccountTab;
	Tab ourSuggestionsTab;
//	Tab simulationTab;
	Tab riskReturnMatrixTab;
	Tab adminTab;
	Tab paymentTab;
	Tab intraDayChartTab;
//	Tab chatTab;
	Tab blogTab;
	
	public MiddlePanel() {
		this.middlePanel = new VLayout();  
		middlePanelTabSet = new TabSet(); 
		initialize();
	}
	
	public Layout getMiddleLayout() {
		return middlePanel;
	}
	@Override
	public boolean isVisible() {
		return middlePanel.isVisible();
	}
	
	@Override
	public void markForResize() {
		this.resize = true;
		if(null!=homePanel){
			homePanel.markForResize();
		}
		if(null!=chatPanel){
			chatPanel.markForResize();
		}
		if(null!=newManagePortfolioPanel){
			newManagePortfolioPanel.markForResize();
		}
		if(null!=tradingAccountPanel){
			tradingAccountPanel.markForResize();
		}
		if(null!=ourSuggestionsPanel){
			ourSuggestionsPanel.markForResize();
		}
//		if(null!=simulationPanel){
//			simulationPanel.markForResize();
//		}
		if(null!=riskReturnMatrixPanel){
			riskReturnMatrixPanel.markForResize();
		}
		if(null!=adminPanel){
			adminPanel.markForResize();
		}
		if(null!=paymentPanel){
			paymentPanel.markForResize();
		}
		if(null!=intraDayChartPanel){
			intraDayChartPanel.markForResize();
		}
		if(null!=chatPanel){
			chatPanel.markForResize();
		}
		if(null!=blogsPanel){
			blogsPanel.markForResize();
		}
	}
	
	@Override
	public void resize() {
		if(!resize){
			return;
		}
		resize = false;
		middlePanel.setWidth(IntelliInvest.WIDTH - 210 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		middlePanelTabSet.setWidth(IntelliInvest.WIDTH - 210 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		middlePanel.setHeight(IntelliInvest.HEIGHT-120); 
		middlePanelTabSet.setHeight(IntelliInvest.HEIGHT-120); 
		
		createOrResizeTab(middlePanelTabSet.getSelectedTab().getID());
	}
	
	public void initialize(){
        middlePanel.setShowEdges(false);  
        middlePanel.setWidth(IntelliInvest.WIDTH - 210 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        middlePanel.setHeight(IntelliInvest.HEIGHT-120); 
        middlePanel.setMembersMargin(0);   
        middlePanel.setLayoutMargin(0);
        middlePanel.setPadding(0);
        
	    middlePanelTabSet.setTabBarPosition(Side.TOP);   
	    middlePanelTabSet.setWidth(IntelliInvest.WIDTH - 210 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
	    middlePanelTabSet.setHeight(IntelliInvest.HEIGHT-120);
	    middlePanelTabSet.setMargin(0);
	    middlePanelTabSet.setPadding(0);
	   
	    final Timer timer = new Timer() {
			@Override
			public void run() {
				if(automaticallyRefresh && null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
					IntelliInvest.refreshCurrentPrice(false, new CallbackUtil() {
						@Override
						public void finished() {
							RefreshPriceHandler.refreshScreenDataOnPriceChange();
						}
					});
				}
			}
		};
    
		timer.scheduleRepeating(Constants.PRICE_REFRESH_INTERVAL);
		
	    chatImg = new ImgButton();
	    chatImg.setWidth(20);
	    chatImg.setHeight(20);
	    chatImg.setBorder("1px black;");
	    chatImg.setSrc("/data/images/message_static.png");
	    chatImg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null!=middlePanelTabSet.getTab("Chat")){
					if(null==chatPanel){
						chatPanel = new ChatPanel();
						middlePanelTabSet.getTab(middlePanelTabSet.getTabNumber("Chat")).setPane(chatPanel.getChatPanelLayout());
					}
					middlePanelTabSet.selectTab(middlePanelTabSet.getTabNumber("Chat"));
		    	}
				if(chatImg.getSrc().equals("/data/images/message.gif")){
					chatImg.setSrc("/data/images/message_static.png");
				}
			}
		});
	    
		DynamicForm form = new DynamicForm();
		form.setNumCols(8);
		form.setWidth("200");
		RadioGroupItem radioGroupItem = new RadioGroupItem("radioForRefresh", "Refresh Prices");  
        radioGroupItem.setWrap(false);
        radioGroupItem.setWrapTitle(false);
        radioGroupItem.setValueMap("Automatic", "Manual");
        radioGroupItem.setVertical(false);
        radioGroupItem.setDefaultValue("Manual");
        
	    final IButton refreshRates = new IButton("");
	    refreshRates.setWidth("45");
	    refreshRates.setIcon("[SKIN]/actions/refresh.png");
	    refreshRates.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IntelliInvest.refreshCurrentPrice(true, new CallbackUtil() {
					@Override
					public void finished() {
						RefreshPriceHandler.refreshScreenDataOnPriceChange();
					}
				});
			}
		});
	    
	    radioGroupItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(event.getValue().toString().equals("Automatic")){
					automaticallyRefresh = true;
					refreshRates.hide();
				}else{
					automaticallyRefresh = false;
					refreshRates.show();
				}
			}
		});
	   
	    form.setFields(radioGroupItem);
	    middlePanelTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, chatImg, form, refreshRates);
	    
	    Tab homeTab = new Tab("Home");
		homeTab.setID("Home");
	    homePanel = new HomePanel();
	    homeTab.setPane(homePanel.getHomePanelLayout());
	    middlePanelTabSet.addTab(homeTab);
	    
	    middlePanelTabSet.setSelectedTab(middlePanelTabSet.getTabNumber("Home"));
	    
	    Tab chatTab = new Tab("Discussions"); 
	    chatTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(chatImg.getSrc().equals("/data/images/message.gif")){
					chatImg.setSrc("/data/images/message_static.png");
				}
			}
		});
	    chatTab.setID("Chat");
	    chatPanel = new ChatPanel();
	    chatTab.setPane(chatPanel.getChatPanelLayout());
	    middlePanelTabSet.addTab(chatTab);
	    
	    initializeTabs();
	    middlePanel.addMember(middlePanelTabSet);
	    middlePanelTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				createOrResizeTab(event.getTab().getID());
				History.newItem("IntelliInvest_" + event.getTab().getID());	
			}
		});
	    
	    History.addValueChangeHandler(new ValueChangeHandler<String>() {
	    		@Override
	    		public void onValueChange(ValueChangeEvent<String> event) {
	    			String historyToken = event.getValue();
		            /* parse the history token */
		            try {
		               if (historyToken.startsWith("IntelliInvest_")) {
		                  String tabName = historyToken.replace("IntelliInvest_", "");
		                  middlePanelTabSet.selectTab(tabName);
		               } else {
		            	   middlePanelTabSet.selectTab("Home");
		               }
		            } catch (IndexOutOfBoundsException e) {
		            	middlePanelTabSet.selectTab("Home");
		            }
	    		}
	    });
	}
	
	public void refreshTabs(){
		removeTabs();
		initializeTabs();
	}
	
	public void initializeTabs() {
		
		newManagePortfolioTab = new Tab("Manage portfolio"); 
		newManagePortfolioTab.setID("NewManagePortfolio");
	    middlePanelTabSet.addTab(newManagePortfolioTab);
	    
	    tradingAccountTab = new Tab("Watch List"); 
	    tradingAccountTab.setID("WatchList");
	    middlePanelTabSet.addTab(tradingAccountTab);
	    
	    ourSuggestionsTab = new Tab("Our Suggestions"); 
	    ourSuggestionsTab.setID("OurSuggestions");
	    middlePanelTabSet.addTab(ourSuggestionsTab);
	    
//	    simulationTab = new Tab("Simulation");  
//	    simulationTab.setID("Simulation");
//	    
//	    middlePanelTabSet.addTab(simulationTab);
	    
	    riskReturnMatrixTab = new Tab("Risk Return Matrix");  
	    riskReturnMatrixTab.setID("RiskReturnMatrix");
	    middlePanelTabSet.addTab(riskReturnMatrixTab);
	    
	    paymentTab = new Tab("Plans");  
	    paymentTab.setID("Payment");
	    middlePanelTabSet.addTab(paymentTab);
	    
	    intraDayChartTab = new Tab("Interactive Chart");  
	    intraDayChartTab.setID("IntraDayChart");
	    intraDayChartTab.addTabDeselectedHandler(new TabDeselectedHandler() {
			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if(null!=intraDayChartPanel && null!=intraDayChartPanel.getIntraDayChartPanelLayout()){
					intraDayChartPanel.getHtmlPane().clear();
				}
			}
		});
	    
	    middlePanelTabSet.addTab(intraDayChartTab);
	    
//	    chatTab = new Tab("Chat");  
//	    chatTab.setID("Chat");
//	    middlePanelTabSet.addTab(chatTab);
	    
	    blogTab = new Tab("Blogs");  
	    blogTab.setID("Blogs");
	    middlePanelTabSet.addTab(blogTab);
	    
	    if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
	    	adminTab = new Tab("Admin");  
		    adminTab.setID("Admin");
		    middlePanelTabSet.addTab(adminTab);
	    }else{
	    	if(null!=middlePanelTabSet.getTab("Admin")){
	    		middlePanelTabSet.removeTab("Admin");
	    		adminPanel = null;
	    	}
	    }
	    
	    if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getExpiryDate().before(new Date())){
//	    	simulationTab.setDisabled(true);
	    	tradingAccountTab.setDisabled(true);
	    	SC.say("Watch List and Simulation tabs are disabled as your subscription ended. Please buy plans to continue using these facilities.", new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					middlePanelTabSet.selectTab("Payment");
				}
			});
	    }
	}
	public void createOrResizeTab(String tabId){
		Tab tab = middlePanelTabSet.getTab(tabId);
		if(null==tab.getPane()){
			if(tabId.equalsIgnoreCase("NewManagePortfolio")){
				newManagePortfolioPanel = new NewManagePortfolioPanel();
				tab.setPane(newManagePortfolioPanel.getManagePortfolioLayout());
			}else if(tabId.equalsIgnoreCase("WatchList")){
					tradingAccountPanel = new TradingAccountPanel();
					tab.setPane(tradingAccountPanel.getTradingAccountLayout());
			}else if(tabId.equalsIgnoreCase("OurSuggestions")){
				ourSuggestionsPanel = new OurSuggestionsPanel();
				tab.setPane(ourSuggestionsPanel.getOurSuggestionsLayout());
			}
//			else if(tabId.equalsIgnoreCase("Simulation")){
//				simulationPanel = new SimulationPanel();
//				tab.setPane(simulationPanel.getSimulationLayout());
//			}
			else if(tabId.equalsIgnoreCase("RiskReturnMatrix")){
				riskReturnMatrixPanel = new RiskReturnMatrixPanel();
				tab.setPane(riskReturnMatrixPanel.getRiskReturnsMatrixLayout());
			}else if(tabId.equalsIgnoreCase("Admin")){
				adminPanel = new AdminPanel();
				tab.setPane(adminPanel.getAdminLayout());
			}else if(tabId.equalsIgnoreCase("Payment")){
				paymentPanel = new PaymentPanel();
				tab.setPane(paymentPanel.getPaymentPanelLayout());
			}else if(tabId.equalsIgnoreCase("IntraDayChart")){
				intraDayChartPanel = new IntraDayChartPanel();
				tab.setPane(intraDayChartPanel.getIntraDayChartPanelLayout());
			}
//			else if(tabId.equalsIgnoreCase("Chat")){
//				chatPanel = new ChatPanel();
//				tab.setPane(chatPanel.getChatPanelLayout());
//			}
			else if(tabId.equalsIgnoreCase("Blogs")){
				blogsPanel = new BlogsPanel();
				tab.setPane(blogsPanel.getBlogPanelLayout());
			}
		}else{
			if(tabId.equalsIgnoreCase("Home")){
				homePanel.resize();
			}else if(tabId.equalsIgnoreCase("Chat")){
				chatPanel.resize();
			}else if(tabId.equalsIgnoreCase("NewManagePortfolio")){
				newManagePortfolioPanel.resize();
			}else if(tabId.equalsIgnoreCase("WatchList")){
				tradingAccountPanel.resize();
			}else if(tabId.equalsIgnoreCase("OurSuggestions")){
				ourSuggestionsPanel.resize();
			}
//			else if(tabId.equalsIgnoreCase("Simulation")){
//				simulationPanel.resize();
//			}
			else if(tabId.equalsIgnoreCase("RiskReturnMatrix")){
				riskReturnMatrixPanel.resize();
			}else if(tabId.equalsIgnoreCase("Admin")){
				adminPanel.resize();
			}else if(tabId.equalsIgnoreCase("Payment")){
				paymentPanel.resize();
			}else if(tabId.equalsIgnoreCase("IntraDayChart")){
				intraDayChartPanel.resize();
			}else if(tabId.equalsIgnoreCase("Blogs")){
				blogsPanel.resize();
			}
		}
	}
	
	public void removeTabs(){
		
		if(null!=middlePanelTabSet.getTab("NewManagePortfolio")){
			if(null!=newManagePortfolioPanel){
				newManagePortfolioPanel.getManagePortfolioLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("NewManagePortfolio"), null);
				newManagePortfolioPanel = null;
			}
    		middlePanelTabSet.removeTab("NewManagePortfolio");
    	}
		
		if(null!=middlePanelTabSet.getTab("WatchList")){
			if(null!=tradingAccountPanel){
				tradingAccountPanel.getTradingAccountLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("WatchList"), null);
				tradingAccountPanel = null;
			}
    		middlePanelTabSet.removeTab("WatchList");
    	}
		
		if(null!=middlePanelTabSet.getTab("OurSuggestions")){
			if(null!=ourSuggestionsPanel){
				ourSuggestionsPanel.getOurSuggestionsLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("OurSuggestions"), null);
				ourSuggestionsPanel = null;
			}
    		middlePanelTabSet.removeTab("OurSuggestions");
    	}
		
//		if(null!=middlePanelTabSet.getTab("Simulation")){
//			if(null!=simulationPanel){
//				simulationPanel.getSimulationLayout().destroy();
//				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("Simulation"), null);
//				simulationPanel = null;
//			}
//    		middlePanelTabSet.removeTab("Simulation");
//    	}
		
		if(null!=middlePanelTabSet.getTab("RiskReturnMatrix")){
			if(null!=riskReturnMatrixPanel){
				riskReturnMatrixPanel.getRiskReturnsMatrixLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("RiskReturnMatrix"), null);
				riskReturnMatrixPanel = null;
			}
    		middlePanelTabSet.removeTab("RiskReturnMatrix");
    	}
		
		if(null!=middlePanelTabSet.getTab("Payment")){
			if(null!=paymentPanel){
				paymentPanel.getPaymentPanelLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("Payment"), null);
				paymentPanel = null;
			}
    		middlePanelTabSet.removeTab("Payment");
    	}
		
		if(null!=middlePanelTabSet.getTab("Admin")){
			if(null!=adminPanel){
				adminPanel.getAdminLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("Admin"), null);
				adminPanel = null;
			}
    		middlePanelTabSet.removeTab("Admin");
    	}

		if(null!=middlePanelTabSet.getTab("IntraDayChart")){
			if(null!=intraDayChartPanel){
				intraDayChartPanel.getIntraDayChartPanelLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("IntraDayChart"), null);
				intraDayChartPanel = null;
			}
    		middlePanelTabSet.removeTab("IntraDayChart");
    	}
		
//		if(null!=middlePanelTabSet.getTab("Chat")){
//			if(null!=chatPanel){
//				chatPanel.getChatPanelLayout().destroy();
//				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("Chat"), null);
//				chatPanel = null;
//			}
//    		middlePanelTabSet.removeTab("Chat");
//    	}
		
		if(null!=middlePanelTabSet.getTab("Blogs")){
			if(null!=blogsPanel){
				blogsPanel.getBlogPanelLayout().destroy();
				middlePanelTabSet.updateTab(middlePanelTabSet.getTabNumber("Blogs"), null);
				blogsPanel = null;
			}
    		middlePanelTabSet.removeTab("Blogs");
    	}
	}
	
}
