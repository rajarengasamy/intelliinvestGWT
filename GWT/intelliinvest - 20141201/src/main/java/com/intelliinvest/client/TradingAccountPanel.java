package com.intelliinvest.client;

import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.datasource.TradingAccountDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TradingAccountPanel implements IntelliInvestPanel{
	final Layout tradingAccountLayout;
	final ListGrid tradingAccountGrid;
	final DynamicForm tradingAccountForm; 
	final EnhancedGrid enhancedTradingAccountGrid;
	Boolean resize = false;
	
	public TradingAccountPanel() {
		this.tradingAccountLayout = new VLayout();
		this.tradingAccountGrid = createTradingAccountGrid();
		this.tradingAccountForm = new DynamicForm(); 
		this.enhancedTradingAccountGrid = new EnhancedGrid();
		initialize();
	}
	
	public Layout getTradingAccountLayout() {
		return tradingAccountLayout;
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
		tradingAccountGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		tradingAccountGrid.setHeight(IntelliInvest.HEIGHT-242); 
		enhancedTradingAccountGrid.resize();
		
		tradingAccountForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		tradingAccountLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		tradingAccountLayout.setHeight(IntelliInvest.HEIGHT-165); 
		
		
	}
	
	@Override
	public boolean isVisible() {
		return tradingAccountLayout.isVisible();
	}
	
	public ListGrid createTradingAccountGrid(){
		ListGrid tradingAccountGrid = new ListGrid(){  
	        @Override
	    	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
	    		if (getFieldName(colNum).equals("signalType") || getFieldName(colNum).equals("yesterdaySignalType")) {  
	    			 return CellStylesUtil.getBuySellCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
	    		 }else if (getFieldName(colNum).equals("strategyROI")) {  
	    			 Double value = EditEventCalculator.getStrategyROI(record);
	 				 record.setAttribute("strategyROI", value);
	    			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
	    		 }else{
	    			 return super.getCellCSSText(record, rowNum, colNum);
	    		 }
	    	}
        };
        return tradingAccountGrid;
	}
	public void initialize(){
		tradingAccountLayout.setShowEdges(false);   
        tradingAccountLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) ); 
        tradingAccountLayout.setHeight(IntelliInvest.HEIGHT-165); 
        tradingAccountLayout.setMembersMargin(0);
        tradingAccountLayout.setPadding(0);
        
        final TradingAccountDS tradingAccountDS = new TradingAccountDS();
        
        tradingAccountGrid.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {
			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				tradingAccountDS.removeData(tradingAccountGrid.getRecord(event.getRowNum()));
			}
		});
        tradingAccountGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        tradingAccountGrid.setHeight(IntelliInvest.HEIGHT-242);   
        ListGridField removeField = new ListGridField("remove", "Remove", 15);
        removeField.setIsRemoveField(true);
        ListGridField codeField = new ListGridField("code", "Code", 105);  
        ListGridField nameField = new ListGridField("name", "Name",150); 
        nameField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
					if(null!=record.getAttribute("code")){
						return StockDetailStaticHolder.getName(record.getAttribute("code"));
					}else{
						return null!=value?value.toString():"";
					}
			}
		});
        ListGridField eodPriceField = new ListGridField("eodPrice", "EOD Price", 100);
        eodPriceField.setType(ListGridFieldType.FLOAT);
        eodPriceField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
					if(null!=record.getAttribute("code")){
						record.setAttribute("eodPrice", StockDetailStaticHolder.getEODPrice(record.getAttribute("code")));
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(StockDetailStaticHolder.getEODPrice(record.getAttribute("code")), record, rowNum, colNum);
					}else{
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
					}
			}
		});
        ListGridField priceField = new ListGridField("currentPrice", "Current Price", 100);
        priceField.setType(ListGridFieldType.FLOAT);
        priceField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
					if(null!=record.getAttribute("code")){
						record.setAttribute("currentPrice", StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code")));
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code")), record, rowNum, colNum);
					}else{
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
					}
			}
		});
        ListGridField signalPriceField = new ListGridField("signalPrice", "Signal Price", 100);
        signalPriceField.setType(ListGridFieldType.FLOAT);
        signalPriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        ListGridField yesterdaySignalField = new ListGridField("yesterdaySignalType", "Prev Signal", 95);
        yesterdaySignalField.setHidden(true);
        ListGridField signalField = new ListGridField("signalType", "Signal", 95);
        ListGridField strategyROIField = new ListGridField("strategyROI", "Strategy ROI", 120);
        strategyROIField.setType(ListGridFieldType.FLOAT);
        strategyROIField.setCellFormatter(CellStylesUtil.PERCENTAGE_CELL_FORMATTER);
        
        ListGridField signalDateField = new ListGridField("signalDate", "Signal Date", 80);
        signalDateField.setType(ListGridFieldType.DATE);
        signalDateField.setAlign(Alignment.CENTER);
        signalDateField.setFilterEditorProperties(new TextItem());
        signalDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        signalDateField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				tradingAccountGrid.getEditedRecord(event.getRowNum()).setAttribute("signalDate", event.getValue());
			}
		});
        
//        strategyROIField.setCellFormatter(new CellFormatter() {
//			@Override
//			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
//				value = EditEventCalculator.getStrategyROI(record);
//				record.setAttribute("strategyROI", value);
//				return CellStylesUtil.PERCENTAGE_CELL_FORMATTER.format(value, record, rowNum, colNum);
//			}
//		});
        
        ListGridField chartField = new ListGridField("chart", "Chart", 90);
        chartField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/intelliinvest.ico\" width=\"20\" height=\"20\" alt=\"View Chart\"></img>";
			}
		});
        chartField.setAlign(Alignment.CENTER);
        
        tradingAccountGrid.setCanEdit(false);
        
        tradingAccountGrid.setFields(removeField, codeField,  nameField, eodPriceField, priceField, yesterdaySignalField,
        							signalField, signalPriceField, signalDateField, strategyROIField, chartField);
        
        removeField.setWidth("2%");
        codeField.setWidth("11%");
        nameField.setWidth("16%");
        eodPriceField.setWidth("9%");
        priceField.setWidth("9%");
        signalPriceField.setWidth("9%");
        signalDateField.setWidth("9%");
        yesterdaySignalField.setWidth("9%");
		signalField.setWidth("9%");
		strategyROIField.setWidth("9%");
		chartField.setWidth("8%");
		
        tradingAccountGrid.setDataSource(tradingAccountDS);
        tradingAccountGrid.setShowAllRecords(true); 
        tradingAccountGrid.setShowHeaderMenuButton(false);
        tradingAccountGrid.setShowFilterEditor(true);  
        tradingAccountGrid.setCanReorderFields(false);   
        tradingAccountGrid.setAutoFetchData(true);
        tradingAccountGrid.setAutoSaveEdits(false);
        tradingAccountGrid.setSaveLocally(true);
        tradingAccountGrid.setCanRemoveRecords(true);
        tradingAccountGrid.setShowHeaderMenuButton(false);
        tradingAccountGrid.setShowHeaderContextMenu(false);
        tradingAccountGrid.setCanResizeFields(false);
        
        tradingAccountGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == tradingAccountGrid.getFieldNum("chart")){
					if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
						SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "userEODSignalChart");
					}else{
						SC.say("This is user specific chart. Please login to see chart");
					}
				}
			}
		});

//        HStack addStack = new HStack();
//        addStack.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
//        addStack.setHeight(30);
//        addStack.setPadding(0);
        
        tradingAccountForm.setIsGroup(true);  
        tradingAccountForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        tradingAccountForm.setHeight("30");
        tradingAccountForm.setPadding(0);
        
        final ComboBoxItem stockComboBox = new ComboBoxItem("code", "Select");   
        stockComboBox.setWidth("110");
        final DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        final DataSourceTextField nameDSField = new DataSourceTextField("name", "Name",150);
        
        stockComboBox.setOptionDataSource(new IntelliInvestDataDS(StockDetailStaticHolder.stockDetailsMap.values(), "tradingaccountPanelStockCB"){
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
        stockComboBox.setStartRow(true);
        stockComboBox.setEndRow(false);
        
        ListGridField codeFieldCB = new ListGridField("code", 100);  
        ListGridField nameFieldCB = new ListGridField("name", 150);  
        stockComboBox.setPickListFields(codeFieldCB, nameFieldCB);
        stockComboBox.setPickListWidth(270);
        stockComboBox.setPickListHeight(400);
        stockComboBox.setTextMatchStyle(TextMatchStyle.SUBSTRING);

        StaticTextItem nameItem = new StaticTextItem("name", "Name");
        nameItem.setWrap(false);
        nameItem.setWidth(150);
        nameItem.setRequired(true);
        
        StaticTextItem priceItem = new StaticTextItem("price", "Price");
        priceItem.setWrap(false);
        priceItem.setWidth(80);
        priceItem.setRequired(true);
        
        StaticTextItem eodPriceItem = new StaticTextItem("eodPrice", " EODPrice");
        eodPriceItem.setWrap(false);
        eodPriceItem.setWidth(80);
        eodPriceItem.setRequired(true);
        
        StaticTextItem dummyTextItem = new StaticTextItem("dummy");
        dummyTextItem.setShowTitle(false);
        dummyTextItem.setValue("  ");
        dummyTextItem.setWidth(5);
        
        ButtonItem addButton = new ButtonItem("Add");
        addButton.setWidth("50");
        addButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(!tradingAccountForm.validate()){
					SC.say("Please correct errors before adding staock to your portfolio");
					return;
				}
				int allowedRowCount = 10;
				if( (null!=IntelliInvest.userDetailData 
						&& null!=IntelliInvest.userDetailData.getUserId() 
						&& null!=IntelliInvest.userDetailData.getPlan())){
						String plan = IntelliInvest.userDetailData.getPlan();
						allowedRowCount = new Integer(plan.split("_")[1]);
				}
				
				if(tradingAccountGrid.getTotalRows()>=allowedRowCount){
             		SC.say("Your account supports watch list of max " + allowedRowCount + " stocks. Please contact support for activating a different plan.");
             		return;
             	}else{
             		for(ListGridRecord record : tradingAccountGrid.getRecords()){
             			if(record.getAttribute("code").equals(tradingAccountForm.getValue("code"))){
             				SC.say("Stock you are trying to add already exists.");
             				return;
             			}
             		}
 	                Record record = tradingAccountForm.getValuesAsRecord();  
 	                tradingAccountDS.addData(record);
 	                tradingAccountDS.executeAdd();
             	}
				
			}
		});
        addButton.setEndRow(true);
        addButton.setStartRow(false);
        
        tradingAccountForm.setGroupTitle("Add");
        tradingAccountForm.setWrapItemTitles(true);
        tradingAccountForm.setNumCols(16);  
        tradingAccountForm.setFields(stockComboBox, nameItem, priceItem, eodPriceItem, dummyTextItem, addButton);
        
        stockComboBox.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			@Override
			public void onChanged(com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				if(null!=stockComboBox.getSelectedRecord()){
					if(null!=stockComboBox.getSelectedRecord().getAttribute("code") && !stockComboBox.getSelectedRecord().getAttribute("code").equals("")){
						tradingAccountForm.getField("code").setValue(stockComboBox.getSelectedRecord().getAttribute("code"));
						tradingAccountForm.getField("name").setValue(stockComboBox.getSelectedRecord().getAttribute("name"));
						tradingAccountForm.getField("price").setValue(StockDetailStaticHolder.getCurrentPrice(stockComboBox.getSelectedRecord().getAttribute("code")));
						tradingAccountForm.getField("eodPrice").setValue(StockDetailStaticHolder.getEODPrice(stockComboBox.getSelectedRecord().getAttribute("code")));
					}
				}
			}
        });
        
//        addStack.addMember(tradingAccountForm);
        
        tradingAccountLayout.addMember(tradingAccountForm);
        
        tradingAccountLayout.addMember(enhancedTradingAccountGrid.enhanceGrid("Watch List", tradingAccountGrid, true));
        enhancedTradingAccountGrid.getSaveButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				tradingAccountGrid.saveAllEdits();
				tradingAccountDS.executeAdd();
				tradingAccountDS.executeUpdate();
				tradingAccountDS.executeRemove();
			}
		});
        enhancedTradingAccountGrid.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || tradingAccountGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								tradingAccountDS.setFetch(true);
								tradingAccountGrid.invalidateCache();
								tradingAccountGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					tradingAccountDS.setFetch(true);
					tradingAccountGrid.invalidateCache();
					tradingAccountGrid.fetchData();
				}
			}
		});
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				tradingAccountGrid.redraw();
			}
		};
		RefreshPriceHandler.addHandler("tradingAccountGridRefreshHandler" , refreshPriceHandler);
        
	}

}

