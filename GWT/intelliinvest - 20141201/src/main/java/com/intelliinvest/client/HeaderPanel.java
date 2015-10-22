package com.intelliinvest.client;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.StockPriceData;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.ConverterUtils;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.BackgroundRepeat;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;

public class HeaderPanel implements IntelliInvestPanel{
	final VStack headerLayout;
	final HStack subHeaderLayout1;
	final HStack subHeaderLayout2;
	final HTMLFlow stockQuotes;
	final Layout companyName;
	final Layout dummyLayout;
	final DynamicForm loginForm;
	final DynamicForm forgotRegisterForm;
	final DynamicForm loggedInForm;
//	final Layout menuLayout;
	
	Timer idleScreenTimer;
	Boolean resize = false;
	
	private static int HEADER_HEIGHT = 80;
	private static int SUB_HEADER_1_HEIGHT = 58;
	private static int SUB_HEADER_2_HEIGHT = 22;
	
	private static int COMPANY_LOGO_WIDTH = 200;
	
	private static int LOGIN_WIDTH = 348;
	private static int LOGIN_HEIGHT = 27;
	private static int FORGOT_REGISTER_HEIGHT = 27;
	private static int LOGGED_IN_HEIGHT = 27;
	
	private static int PRICE_LABEL_WIDTH = 0;
	private static int STOCK_QUOTES_HEIGHT = 22;
//	private static int MENU_HEIGHT = 25;
	
	public static Boolean IS_ADMIN_ONLINE = false;
	static StaticTextItem isAdminOnlineLabel;
	static Timer liveGridTimer;
	
	public HeaderPanel() {
		
		headerLayout = new VStack();
		subHeaderLayout1 = new HStack();
		subHeaderLayout2 = new HStack();
		
		stockQuotes = getStockQuotes();
		
		companyName = getCompanyName();
		dummyLayout = getDummyLayout();
		
		loginForm = getLoginForm();
        forgotRegisterForm = getForgotRegisterForm();
        loggedInForm = getLoggedInForm();
//		menuLayout = getMenuLayout1();
        
		initialize();
		RegisterWindow.initialize();
		MyProfileWindow.initialize();
		
		if(null==liveGridTimer){
			liveGridTimer = new Timer() {
				@Override
				public void run() {
					String userId = "";
			 		if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
			 			userId = IntelliInvest.userDetailData.getUserId();
			 		}
	         		IntelliInvestServiceFactory.utilService.getAdminAvailableForChat(userId, new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
//							System.out.println("Admin online : " + result);
							HeaderPanel.IS_ADMIN_ONLINE = result;
							if(HeaderPanel.IS_ADMIN_ONLINE){
								HeaderPanel.isAdminOnlineLabel.setValue("<b Style=\"color:green;font-size:12px;white-space:nowrap\">&nbsp;We are online</b>");
							}else{
								HeaderPanel.isAdminOnlineLabel.setValue("<b Style=\"color:red;font-size:12px;white-space:nowrap\">&nbsp;We are offline</b>");
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
				
			};
			System.out.println("adding chat time schedule");
			liveGridTimer.scheduleRepeating(20000);
		}
	}
	
	public Layout getHeaderLayout() {
		return headerLayout;
	}
	
	@Override
	public boolean isVisible() {
		return headerLayout.isVisible();
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
		headerLayout.setWidth(IntelliInvest.WIDTH - 5);
		dummyLayout.setWidth(IntelliInvest.WIDTH - COMPANY_LOGO_WIDTH - LOGIN_WIDTH - 5);
		stockQuotes.setWidth(IntelliInvest.WIDTH - PRICE_LABEL_WIDTH - 5);
	}
	
	public void initialize(){
		
		headerLayout.setShowEdges(false);   
        headerLayout.setWidth(IntelliInvest.WIDTH - 5);   
        headerLayout.setHeight(HEADER_HEIGHT);  
        headerLayout.setMembersMargin(0);   
        headerLayout.setLayoutMargin(0); 
        headerLayout.setPadding(0);
        
		subHeaderLayout1.setHeight(SUB_HEADER_1_HEIGHT);  
		subHeaderLayout1.setMembersMargin(0);   
		subHeaderLayout1.setLayoutMargin(0); 
		subHeaderLayout1.setPadding(0);
		subHeaderLayout1.setAlign(VerticalAlignment.BOTTOM);
		subHeaderLayout1.setStyleName("intelliinvest");
		
		subHeaderLayout2.setHeight(SUB_HEADER_2_HEIGHT);  
		subHeaderLayout2.setMembersMargin(0);   
		subHeaderLayout2.setLayoutMargin(0); 
		subHeaderLayout2.setPadding(0);
		subHeaderLayout2.setAlign(VerticalAlignment.BOTTOM);
		subHeaderLayout2.setStyleName("intelliinvest");
		
	        
		subHeaderLayout1.addMember(companyName);
		subHeaderLayout1.addMember(dummyLayout);
		subHeaderLayout1.addMember(getLoginStack());

//		menuLayout.setHeight(MENU_HEIGHT);
//		subHeaderLayout2.addMember(menuLayout);
		subHeaderLayout2.addMember(stockQuotes);
		
		headerLayout.addMember(subHeaderLayout1);
		headerLayout.addMember(subHeaderLayout2);
	}

	private VStack getLoginStack() {
		VStack loginStack = new VStack();
		loginStack.setMargin(0);
		loginStack.setPadding(0);
		loginStack.setMembersMargin(0);
		loginStack.setWidth(LOGIN_WIDTH);
        loginStack.setHeight(SUB_HEADER_1_HEIGHT);
        loginStack.setAlign(VerticalAlignment.CENTER);
        loginStack.setPadding(0);
        DynamicForm label = new DynamicForm();
        label.setPadding(0);
        label.setMargin(0);
        label.setWidth(LOGIN_WIDTH);
        label.setHeight(4);
        loginStack.addMember(label);
        loginStack.addMember(loginForm);
        loginStack.addMember(forgotRegisterForm);
        loginStack.addMember(loggedInForm);
		return loginStack;
	}

	private Layout getCompanyName() {
		Layout companyName = new Layout();  
		companyName.setMargin(0);
		companyName.setMembersMargin(0);
		companyName.setPadding(0);
		companyName.setHeight(SUB_HEADER_1_HEIGHT);
		companyName.setBackgroundImage("/data/images/intelliinvest_logo.png");
		companyName.setBackgroundRepeat(BackgroundRepeat.NO_REPEAT);
		companyName.setWidth(COMPANY_LOGO_WIDTH);
		return companyName;
	}
	
	private Layout getDummyLayout() {
		Layout dummyLayout = new Layout();
		dummyLayout.setWidth(IntelliInvest.WIDTH - COMPANY_LOGO_WIDTH - LOGIN_WIDTH - 5);
		return dummyLayout;
	}
	
	private DynamicForm getLoggedInForm(){
		DynamicForm loggedInForm = new DynamicForm();  
		loggedInForm.setPadding(0);
		loggedInForm.setMargin(0);
		loggedInForm.setNumCols(6);
        loggedInForm.setWidth(LOGIN_WIDTH);
        loggedInForm.setHeight(LOGGED_IN_HEIGHT);
        loggedInForm.setPadding(0);
        
        ButtonItem logoutButton = new ButtonItem("logout", "Logout");
        logoutButton.setShowTitle(false);
        logoutButton.setStartRow(false);
        logoutButton.setEndRow(false);
        logoutButton.setWidth("70");
        logoutButton.setAlign(Alignment.CENTER);
        logoutButton.setVAlign(VerticalAlignment.CENTER);
        
        ButtonItem myProfileButton = new ButtonItem("myProfileButton", "My Profile");
        myProfileButton.setShowTitle(false);
        myProfileButton.setStartRow(false);
        myProfileButton.setEndRow(true);
        myProfileButton.setWidth("70");
        myProfileButton.setAlign(Alignment.CENTER);
        myProfileButton.setVAlign(VerticalAlignment.CENTER);
        
        StaticTextItem welcomeMessageStaticTextItem = new StaticTextItem("welcomeMessageStaticTextItem");
        welcomeMessageStaticTextItem.setWidth("160");
        welcomeMessageStaticTextItem.setShowTitle(false);
        welcomeMessageStaticTextItem.setAlign(Alignment.CENTER);
        welcomeMessageStaticTextItem.setVAlign(VerticalAlignment.CENTER);
        welcomeMessageStaticTextItem.setStartRow(true);
        welcomeMessageStaticTextItem.setEndRow(false);
        
        StaticTextItem lastLoginItem = new StaticTextItem("lastLoginDate");
        lastLoginItem.setShowTitle(false);
        lastLoginItem.setHeight(10);
        lastLoginItem.setColSpan(2);
        lastLoginItem.setAlign(Alignment.CENTER);
        
		isAdminOnlineLabel = new StaticTextItem();
		isAdminOnlineLabel.setShowTitle(false);
		isAdminOnlineLabel.setWidth("70");
		if(IS_ADMIN_ONLINE){
			isAdminOnlineLabel.setValue("<b Style=\"color:green;font-size:12px;white-space:nowrap\">&nbsp;We are online</b>");
		}else{
			isAdminOnlineLabel.setValue("<b Style=\"color:red;font-size:12px;white-space:nowrap\">&nbsp;We are offline</b>");
		}
        
        loggedInForm.setFields(lastLoginItem, isAdminOnlineLabel, welcomeMessageStaticTextItem, logoutButton, myProfileButton);
        loggedInForm.setVisible(false);
        
        myProfileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MyProfileWindow.setValues();
				MyProfileWindow.getMyProfileWindow().animateShow(AnimationEffect.WIPE);
			}
		});
        
        
        logoutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeUserDetailsInHeader();
			}
		});
        
        return loggedInForm;
	}
	
	private DynamicForm getLoginForm(){
		final DynamicForm loginForm = new DynamicForm();  
		loginForm.setPadding(0);
		loginForm.setMargin(0);
        loginForm.setNumCols(6);
        loginForm.setWidth(LOGIN_WIDTH);
        loginForm.setHeight(LOGIN_HEIGHT);
        
        final TextItem mailTextItem = new TextItem("mailTextItem");
        mailTextItem.setDefaultValue("user@mail.com");
        mailTextItem.setWidth(160);
        mailTextItem.setShowTitle(false);
        mailTextItem.setStartRow(true);
        mailTextItem.setEndRow(false);
        mailTextItem.addFocusHandler(Util.getEmptyTextHandler("user@mail.com"));
        mailTextItem.addBlurHandler(Util.getDefaultTextHandler("user@mail.com"));
        mailTextItem.setPrompt("Enter your mail id");
        String mailId = Cookies.getCookie("mailId");
        if(null!=mailId && !"".equals(mailId)){
        	mailTextItem.setValue(mailId);
        }
        
        final PasswordItem passwordItem = new PasswordItem("passwordItem");
        passwordItem.setDefaultValue("password");
        passwordItem.setWidth(100);
        passwordItem.setShowTitle(false);
        passwordItem.setStartRow(false);
        passwordItem.setEndRow(false);
        passwordItem.addFocusHandler(Util.getEmptyTextHandler("password"));
        passwordItem.addBlurHandler(Util.getDefaultTextHandler("password"));
        passwordItem.setPrompt("Enter your password");
        String password = Cookies.getCookie("password");
        
        if(null!=password && !"".equals(password)){
        	try{
        		passwordItem.setValue(EncryptUtil.decrypt(password));
        	}catch(Exception e){
        		password = "";
        	}
        }
        
        if(null!=mailId && !"".equals(mailId) && null!=password && !"".equals(password)){
        	login(mailId, password);
        }
        
        ButtonItem loginButton = new ButtonItem("loginButton", "Login");
        loginButton.setShowTitle(false);
        loginButton.setStartRow(false);
        loginButton.setEndRow(true);
        loginButton.setWidth("60");
        
        loginForm.setFields(mailTextItem, passwordItem, loginButton);
        
        passwordItem.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName().equals(KeyNames.ENTER)){
					String password = passwordItem.getValue().toString();
					 try {
						 password = EncryptUtil.encrypt(password);
					} catch (Exception e) {
						SC.say("Please enter a valid password");
						return;
					} 
					login(mailTextItem.getValue().toString(), password);
				}
			}

		});
        
        loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String password = passwordItem.getValue().toString();
				 try {
					 password = EncryptUtil.encrypt(password);
				} catch (Exception e) {
					SC.say("Please enter a valid password");
					return;
				} 
				 login(mailTextItem.getValue().toString(), password);
			}
		});
        
        return loginForm;
	}
	
	private DynamicForm getForgotRegisterForm(){
        final DynamicForm forgotRegisterForm = new DynamicForm();  
        forgotRegisterForm.setPadding(0);
        forgotRegisterForm.setMargin(0);
        forgotRegisterForm.setNumCols(6);
        forgotRegisterForm.setWidth(LOGIN_WIDTH);
        forgotRegisterForm.setHeight(FORGOT_REGISTER_HEIGHT);
        
        StaticTextItem guestTextItem = new StaticTextItem("guestTextItem");
        guestTextItem.setShowTitle(false);
        guestTextItem.setValue("<b Style=\"color:gray;align:center;valign:center;font-size:12px;white-space:nowrap\"> Welcome Guest</b>");
        guestTextItem.setAlign(Alignment.CENTER);
        guestTextItem.setWidth(160);
        guestTextItem.setStartRow(true);
        guestTextItem.setEndRow(false);
        
        ButtonItem forgotPasswordButton = new ButtonItem("forgotPasswordButton", "Forgot Password");
        forgotPasswordButton.setShowTitle(false);
        forgotPasswordButton.setStartRow(false);
        forgotPasswordButton.setEndRow(false);
        forgotPasswordButton.setWidth(100);

        ButtonItem registerButton = new ButtonItem("registerButton", "Register");
        registerButton.setShowTitle(false);
        registerButton.setStartRow(false);
        registerButton.setEndRow(true);
        registerButton.setWidth(60);
        
        forgotRegisterForm.setFields(guestTextItem, forgotPasswordButton, registerButton);
        
        registerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RegisterWindow.getRegisterWindow().animateShow(AnimationEffect.WIPE);
			}
		});
        
        forgotPasswordButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IntelliInvestServiceFactory.loginService.forgotPassword(loginForm.getField("mailTextItem").getValue().toString(), new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						SC.say(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
				});
			}
		});
        
        return forgotRegisterForm;
        
	}

	private HTMLFlow getStockQuotes() {
		final HTMLFlow stockQuotes = new HTMLFlow();
		stockQuotes.setPadding(0);
		stockQuotes.setMargin(0);
//		stockQuotes.setBorder("1px solid #ababab;");
		stockQuotes.setStyleName("intelliinvest");
		String value = getStockQuotesContent();         
        stockQuotes.setOverflow(Overflow.HIDDEN);   
        stockQuotes.setWidth(IntelliInvest.WIDTH - PRICE_LABEL_WIDTH - 5);
        stockQuotes.setHeight(STOCK_QUOTES_HEIGHT);
        stockQuotes.setContents(value);
        
        final Timer stockQuotesTimer = new Timer() {
			@Override
			public void run() {
				String value = getStockQuotesContent();
				stockQuotes.setContents(value);
				stockQuotes.redraw();
			}
		};
		
		stockQuotesTimer.scheduleRepeating(300000);
		return stockQuotes;
	}
	
//	private Layout getMenuLayout(){
//		Layout menuLayout = new Layout();
//		TabSet middlePanelTabSet= new TabSet(); 
//		 ImgButton chatImg = new ImgButton();
//		    chatImg.setWidth(20);
//		    chatImg.setHeight(20);
//		    chatImg.setBorder("1px black;");
//		    chatImg.setSrc("/data/images/message_static.png");
//		    
//			DynamicForm form = new DynamicForm();
//			form.setNumCols(8);
//			form.setWidth("200");
//			RadioGroupItem radioGroupItem = new RadioGroupItem("radioForRefresh", "Refresh Prices");  
//	        radioGroupItem.setWrap(false);
//	        radioGroupItem.setWrapTitle(false);
//	        radioGroupItem.setValueMap("Automatic", "Manual");
//	        radioGroupItem.setVertical(false);
//	        radioGroupItem.setDefaultValue("Manual");
//		    final IButton refreshRates = new IButton("");
//		    refreshRates.setWidth("45");
//		    refreshRates.setIcon("[SKIN]/actions/refresh.png");
//		    form.setFields(radioGroupItem);
//		middlePanelTabSet.setTabBarControls(chatImg, form, refreshRates);
//		middlePanelTabSet.setTabBarPosition(Side.TOP);  
//		middlePanelTabSet.setWidth(IntelliInvest.WIDTH-10);
//	    middlePanelTabSet.setHeight(25);
//	    middlePanelTabSet.setMargin(0);
//	    middlePanelTabSet.setPadding(0);
//	    
//	    Tab homeTab = new Tab("Home");
//		homeTab.setID("Home1");
//	    middlePanelTabSet.addTab(homeTab);
//	    middlePanelTabSet.setSelectedTab(middlePanelTabSet.getTabNumber("Home1"));
//
//	    Tab chatTab = new Tab("Discussions"); 
//	    chatTab.setID("Chat1");
//	    middlePanelTabSet.addTab(chatTab);
//	    
//	    Tab newManagePortfolioTab = new Tab("Manage portfolio"); 
//		newManagePortfolioTab.setID("NewManagePortfolio");
//	    middlePanelTabSet.addTab(newManagePortfolioTab);
//	    
//	    Tab tradingAccountTab = new Tab("Watch List"); 
//	    tradingAccountTab.setID("WatchList");
//	    middlePanelTabSet.addTab(tradingAccountTab);
//	    
//	    Tab ourSuggestionsTab = new Tab("Our Suggestions"); 
//	    ourSuggestionsTab.setID("OurSuggestions");
//	    middlePanelTabSet.addTab(ourSuggestionsTab);
//	    
//	    Tab simulationTab = new Tab("Simulation");  
//	    simulationTab.setID("Simulation");
//	    
//	    middlePanelTabSet.addTab(simulationTab);
//	    
//	    Tab riskReturnMatrixTab = new Tab("Risk Return Matrix");  
//	    riskReturnMatrixTab.setID("RiskReturnMatrix");
//	    middlePanelTabSet.addTab(riskReturnMatrixTab);
//	    
//	    Tab paymentTab = new Tab("Plans");  
//	    paymentTab.setID("Payment");
//	    middlePanelTabSet.addTab(paymentTab);
//	    
//	    Tab intraDayChartTab = new Tab("Interactive Chart");  
//	    intraDayChartTab.setID("IntraDayChart");
//	    middlePanelTabSet.addTab(intraDayChartTab);
//	    
//	    Tab blogTab = new Tab("Blogs");  
//	    blogTab.setID("Blogs");
//	    middlePanelTabSet.addTab(blogTab);
//	    
//	    Tab adminTab = new Tab("Admin");  
//	    adminTab.setID("Admin");
//	    middlePanelTabSet.addTab(adminTab);
//	    
//	    menuLayout.addMember(middlePanelTabSet);
//        return menuLayout;
//	}
//	
//	private Layout getMenuLayout1(){
//		Layout menuLayout = new Layout();
//		menuLayout.setWidth(IntelliInvest.WIDTH-10);
//		ToolStrip toolStrip = new ToolStrip(); 
//		toolStrip.setWidth(IntelliInvest.WIDTH-10);
////		toolStrip.addFill();
//		toolStrip.setMembersMargin(3);
////		toolStrip.setStyleName("tab");
//		toolStrip.setHeight(MENU_HEIGHT);
//		
//		toolStrip.addMember(getIconButton("Home", "message_static", false));
//		toolStrip.addSeparator();
//		
//		toolStrip.addMember(getIconButton("Discussion", "message_static", false));
//		toolStrip.addSeparator();
//		
//		toolStrip.addMember(getIconButton("Manage Portfolio", "message_static", false));  
//		toolStrip.addMember(getIconButton("&nbsp;Watch List", "message_static", false));  
//		toolStrip.addSeparator();
//		
//		
//		toolStrip.addMember(getIconButton("Our Suggestion", "message_static", false));  
//		toolStrip.addMember(getIconButton("Simulation", "message_static", false)); 
//		toolStrip.addMember(getIconButton("Risk Return Matrix", "message_static", false)); 
//		toolStrip.addSeparator();
//		
//		toolStrip.addMember(getIconButton("Interactive Chart", "message_static", false));  
//		toolStrip.addSeparator();
//		
//		toolStrip.addMember(getIconButton("Plan", "message_static", false));
//		toolStrip.addSeparator();
//		toolStrip.addMember(getIconButton("Blogs", "message_static", false));
//		toolStrip.addSeparator();
//		toolStrip.addMember(getIconButton("Admin", "message_static", false));
//		toolStrip.addSeparator();
//		
//		ImgButton chatImg = new ImgButton();
//	    chatImg.setWidth(20);
//	    chatImg.setHeight(20);
//	    chatImg.setBorder("1px black;");
//	    chatImg.setSrc("/data/images/message_static.png");
//	    toolStrip.addMember(chatImg);
//	    toolStrip.addSeparator();
//	    
//		DynamicForm form = new DynamicForm();
//		form.setNumCols(8);
//		form.setWidth("200");
//		RadioGroupItem radioGroupItem = new RadioGroupItem("radioForRefresh", "Refresh Prices");  
//        radioGroupItem.setWrap(false);
//        radioGroupItem.setWrapTitle(false);
//        radioGroupItem.setValueMap("Automatic", "Manual");
//        radioGroupItem.setVertical(false);
//        radioGroupItem.setDefaultValue("Manual");
//	    final IButton refreshRates = new IButton("");
//	    refreshRates.setWidth("45");
//	    refreshRates.setIcon("[SKIN]/actions/refresh.png");
//	    form.setFields(radioGroupItem);
//	    toolStrip.addMember(form);
//	    
//		menuLayout.addMember(toolStrip);
//		
//        return menuLayout;
//	}
//	
//	 private IconButton getIconButton(String title, String iconName, boolean vertical) {  
//        final IconButton button = new IconButton("<font style=\"font-size: 11px;text-align:center;\">" + title + "</font>");
//        button.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//			@Override
//			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//				button.setSelected(true);
//			}
//		});
////        button.setShowShadow(true); 
//        button.setEdgeOffset(2);
////        button.setShadowSoftness(0);
////        button.setShadowDepth(1);
//        return button;  
//	}  
//	 
////	 private MenuButton getIconButton(String title, String iconName, boolean vertical) {  
////        Menu menu = new Menu();  
////        menu.setAutoFitData(Autofit.HORIZONTAL);
////        menu.setShowShadow(true);  
////        menu.setShadowDepth(10);
////        MenuItem newItem = new MenuItem(title, "/data/images/" + iconName + ".png");
////        menu.addItem(newItem);
////        MenuButton menuButton = new MenuButton(title, menu);
////        return menuButton;  
////	}  
	 
	 void login(final String mailId, String password) {
			IntelliInvestServiceFactory.loginService.login(mailId, password, new AsyncCallback<UserDetailData>() {
				@Override
				public void onSuccess(UserDetailData userDetailData) {
					if(null==userDetailData){
						SC.say("Error logging in. Please try again.");
					}else{
						if(null!=userDetailData.getError()){
							SC.say(userDetailData.getError());
						}else{
							setUserDetailsInHeader(userDetailData);
						}
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
			});
		}
	 
	public void setUserDetailsInHeader(UserDetailData userDetailData){
		
		IntelliInvest.userDetailData = userDetailData;
		
		loggedInForm.getField("welcomeMessageStaticTextItem").setValue("<b Style=\"color:gray;align:center;valign:center;font-size:12px;white-space:nowrap\"> Welcome " + userDetailData.getUsername() + "</b>");
		String lastLoginDate = "";
		if(null!=userDetailData.getLastLoginDate()){
			lastLoginDate = DateUtil.getYYYYMMDDHHMMSSDate(userDetailData.getLastLoginDate());
		}
		loggedInForm.getField("lastLoginDate").setValue("<b Style=\"color:gray;align:center;valign:center;font-size:12px;white-space:nowrap\"> Last Login: " + lastLoginDate + "</b>");
		loginForm.setVisible(false);
		forgotRegisterForm.setVisible(false);
		loggedInForm.setVisible(true);
		
		LeftPanel.contactForm.getItem("mailId").setValue(userDetailData.getMail());
		LeftPanel.contactForm.getItem("mailId").setCanEdit(false);
		
		LeftPanel.contactForm.getItem("contactNo").setValue(userDetailData.getPhone());
		LeftPanel.contactForm.getItem("contactNo").setCanEdit(false);
		
		IntelliInvest.MIDDLE_PANEL.refreshTabs();
		
		setCookies();
		
		idleScreenTimer = getIdleScreenTimeoutTimer();
		idleScreenTimer.scheduleRepeating(60000);
		
	}
	
	public void removeUserDetailsInHeader(){
		IntelliInvest.userDetailData = null;

		loginForm.setVisible(true);
		forgotRegisterForm.setVisible(true);
		loggedInForm.setVisible(false);
		
		LeftPanel.contactForm.getItem("mailId").setValue("");
		LeftPanel.contactForm.getItem("mailId").setCanEdit(true);
		
		LeftPanel.contactForm.getItem("contactNo").setValue("");
		LeftPanel.contactForm.getItem("contactNo").setCanEdit(true);
		
		IntelliInvest.MIDDLE_PANEL.refreshTabs();
		
		removeCookies();
		cancelIdleScreenTimeout();
	}

	private String getStockQuotesContent() {
		String value = "<table><tbody><tr>";
		for(IntelliInvestData intelliInvestData : StockDetailStaticHolder.NIFTYStockPriceMap.values()){
			String code = ((StockPriceData)intelliInvestData).getCode();
			Double currentPrice =  ((StockPriceData)intelliInvestData).getPrice();
			Double cp = StockDetailStaticHolder.getCP(code);
			if(currentPrice!=0d && null!=code){
				if(cp<0){
					value = value + "<td width=50>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td style=\"color:red;font-size:12px;white-space: nowrap\"><b>"+ code + " " + ConverterUtils.NumberToString(currentPrice,2) + "  &darr; " + Math.abs(cp) + "%</b></td>";
				}else{
					value = value + "<td width=50>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td style=\"color:green;font-size:12px;white-space: nowrap\"><b>"+ code + " " + ConverterUtils.NumberToString(currentPrice,2) + " &uarr; " + Math.abs(cp) + "%</b></td>";
				}
			}
		}
		value = value + "</tr></tbody></table>";
		return "<marquee scrollamount=3; onmouseover=this.stop(); onmouseout=this.start();>" + value + "</marquee>";
		
	}
	
	private Timer getIdleScreenTimeoutTimer(){
		Timer idleScreenTimer = new Timer() {
			@Override
			public void run() {
				if(IntelliInvest.IDLE_WARNING_SHOWN){
					IntelliInvest.IDLE_WARNING_SHOWN = false;
					removeUserDetailsInHeader();
					SC.say("Logged off ....");
				}else{
					IntelliInvest.IDLE_TIME++;
					if(IntelliInvest.IDLE_TIME>=Constants.IDLE_TIMEOUT){
						IntelliInvest.IDLE_WARNING_SHOWN = true;
						SC.confirm("Session is idle for " + IntelliInvest.IDLE_TIME + " minutes. Will be logged of automatically in a minute. Do you want to keep session alive?",
								new BooleanCallback() {
									@Override
									public void execute(Boolean value) {
										if(value){
											IntelliInvest.IDLE_TIME = 0;
											IntelliInvest.IDLE_WARNING_SHOWN = false;
										}else{
											IntelliInvest.IDLE_WARNING_SHOWN = false;
											removeUserDetailsInHeader();
										}
									}
								});
					}
				}
			}
		};
		return idleScreenTimer;
	}
	
	private void cancelIdleScreenTimeout(){
		if(null!=idleScreenTimer){
			idleScreenTimer.cancel();
		}
	}
	
	private void setCookies(){
		Cookies.setCookie("mailId", IntelliInvest.userDetailData.getMail());
		Cookies.setCookie("password", IntelliInvest.userDetailData.getPassword());
		
	}
	
	private void removeCookies(){
		Cookies.setCookie("password", "");
		
	}
}
