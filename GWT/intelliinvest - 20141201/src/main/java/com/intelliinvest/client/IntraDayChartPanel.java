package com.intelliinvest.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class IntraDayChartPanel implements IntelliInvestPanel{
	final Layout intraDayChartPanelLayout;
	Boolean resize = false;
	HTMLPane htmlPane ;
	
	public IntraDayChartPanel() {
		this.intraDayChartPanelLayout = getIntraDayChartPanel();
		initialize();
	}
	
	@Override
	public boolean isVisible() {
		return intraDayChartPanelLayout.isVisible();
	}
	
	public Layout getIntraDayChartPanelLayout() {
		return intraDayChartPanelLayout;
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
		
		intraDayChartPanelLayout.setWidth(IntelliInvest.WIDTH - 240 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		intraDayChartPanelLayout.setHeight((IntelliInvest.HEIGHT-180) /2);
		
	}
	
	public void initialize(){
		intraDayChartPanelLayout.setWidth(IntelliInvest.WIDTH-225-(IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		intraDayChartPanelLayout.setHeight(IntelliInvest.HEIGHT-165);    
	}
	
	public HTMLPane getHtmlPane() {
		return htmlPane;
	}
	
	private Layout getIntraDayChartPanel(){
		VLayout intraDayChartPanelLayout = new VLayout();
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setIsGroup(true); 
		dynamicForm.setGroupTitle("Select Stock");
        dynamicForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) );   
        dynamicForm.setHeight("30");
        dynamicForm.setPadding(0);
        
        final RadioGroupItem radioGroupItem = new RadioGroupItem("radioForRefresh");  
        radioGroupItem.setWrap(false);
        radioGroupItem.setWrapTitle(false);
        radioGroupItem.setShowTitle(false);
        radioGroupItem.setValueMap("Day Wise", "Intra Day");
        radioGroupItem.setVertical(false);
        radioGroupItem.setDefaultValue("Intra Day");
        radioGroupItem.setStartRow(true);
        radioGroupItem.setEndRow(false);
        
        final ComboBoxItem stockComboBox = new ComboBoxItem("code", "Select");   
        stockComboBox.setWidth("110");
        final DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        final DataSourceTextField nameDSField = new DataSourceTextField("name", "Name",150);
        
        stockComboBox.setOptionDataSource(new IntelliInvestDataDS(StockDetailStaticHolder.stockDetailsMap.values(), "managePortfolioPanelStockCB"){
        	@Override
        	public void setFieldsForDS() {
        		setFields(codeDSField, nameDSField);
        	}
        });
        stockComboBox.setCompleteOnTab(true);
        stockComboBox.setDisplayField("code");
        stockComboBox.setValueField("code");
        stockComboBox.setFilterFields("code", "name");
        stockComboBox.setSortField("code");
        stockComboBox.setRequired(true);
        
        ListGridField codeFieldCB = new ListGridField("code", 100);  
        ListGridField nameFieldCB = new ListGridField("name", 150); 
        stockComboBox.setPickListFields(codeFieldCB, nameFieldCB);
        stockComboBox.setPickListWidth(270);
        stockComboBox.setPickListHeight(400);
        stockComboBox.setTextMatchStyle(TextMatchStyle.SUBSTRING);
        stockComboBox.setStartRow(false);
        stockComboBox.setEndRow(false);
        
        ButtonItem getChartButton = new ButtonItem("getChartButton", "Get Chart");
        getChartButton.setShowTitle(false);
        getChartButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(null!=stockComboBox.getSelectedRecord()){
					getChart(stockComboBox.getSelectedRecord().getAttribute("code"), radioGroupItem.getValueAsString());
				}
			}
		});
        
        getChartButton.setStartRow(false);
        getChartButton.setEndRow(true);
        
        dynamicForm.setWrapItemTitles(false);
        dynamicForm.setNumCols(12);  
        
        
        dynamicForm.setFields(radioGroupItem, stockComboBox, getChartButton);
        intraDayChartPanelLayout.addMember(dynamicForm);
        
        htmlPane = new HTMLPane();
        htmlPane.setID("EODChartHTMLPanel");
        intraDayChartPanelLayout.addMember(htmlPane, 1);
        
        return intraDayChartPanelLayout;
	}
	
	private void getChart(final String code, final String type){
		if(null!=intraDayChartPanelLayout.getMember("EODChartHTMLPanel")){
			intraDayChartPanelLayout.removeMember(htmlPane);
		}
		htmlPane.clear();
		htmlPane.destroy();
		if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
			if(type.equals("Intra Day")){
				IntelliInvestServiceFactory.utilService.getIntraDayChartData("all", code, IntelliInvest.userDetailData.getUserId(), new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						drawChart(result);
					}
					@Override
					public void onFailure(Throwable caught) {
						SC.say("Error fetching chart");
					}
				});
			}else{
				IntelliInvestServiceFactory.utilService.getDayChartData("all", code, IntelliInvest.userDetailData.getUserId(), new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						drawChart(result);
					}
					@Override
					public void onFailure(Throwable caught) {
						SC.say("Error fetching chart");
					}
				});
			}
		}else{
			SC.say("Trend line charts for logged in users only. Please login to use this facility.");
		}
	}
	
	private void drawChart(final String result){
		try{
			htmlPane = new HTMLPane();
			htmlPane.setID("EODChartHTMLPanel");
			htmlPane.setCanSelectText(false);
			htmlPane.setWidth(IntelliInvest.WIDTH - 250 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
			htmlPane.setHeight(IntelliInvest.HEIGHT-200);
			htmlPane.setAlign(Alignment.CENTER);
	        htmlPane.setOverflow(Overflow.VISIBLE);
	        htmlPane.setContentsType(ContentsType.FRAGMENT);
	        htmlPane.setContents("<div id='intraDayChartDiv'></div>");
	        htmlPane.setEvalScriptBlocks(true);
	        intraDayChartPanelLayout.addMember(htmlPane, 1);
        	drawChart(JSOHelper.eval(result));
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static native void drawChart(JavaScriptObject rowData)  /*-{
		try
		{
			$wnd.drawChart(rowData);
		}catch(err){
		}
	}-*/;
	
}
