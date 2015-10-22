package com.intelliinvest.client;

import java.util.Date;

import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.datasource.ManagePortfolioSummaryDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
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
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.SummaryFunction;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class NewManagePortfolioPanel implements IntelliInvestPanel{
	final Layout managePortfolioLayout;
	final ListGrid managePortfolioGrid;
	final DynamicForm managePortfolioForm; 
	final EnhancedGrid enhancedManagePortfolioGrid;
	Boolean resize = false;
	
	public NewManagePortfolioPanel() {
		this.managePortfolioLayout = new VLayout();
		this.managePortfolioGrid = createManagePortfolioGrid();
		this.managePortfolioForm = new DynamicForm(); 
		enhancedManagePortfolioGrid = new EnhancedGrid();
		initialize();
	}
	
	public Layout getManagePortfolioLayout() {
		return managePortfolioLayout;
	}
	
	@Override
	public boolean isVisible() {
		return managePortfolioLayout.isVisible();
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
		managePortfolioGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) );
		managePortfolioGrid.setHeight(IntelliInvest.HEIGHT-242); 
		enhancedManagePortfolioGrid.resize();
		
		managePortfolioForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) );
		
		managePortfolioLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		managePortfolioLayout.setHeight(IntelliInvest.HEIGHT-165);
	}
	
	public ListGrid createManagePortfolioGrid(){
		ListGrid managePortfolioGridTmp = new ListGrid(){
			
        	@Override
        	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        		if (getFieldName(colNum).equals("realisedPnl")) {  
       			 	return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
       		 	}else if (getFieldName(colNum).equals("unrealisedPnl")) {  
        			return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }else if(getFieldName(colNum).equals("todaysPnl")) { 
        			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }else if (getFieldName(colNum).equals("cp")) { 
        			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }
        		 
        		 return super.getCellCSSText(record, rowNum, colNum);
        	}
        	
        	@Override
        	protected String getCellStyle(ListGridRecord record, int rowNum, int colNum) {
        		String cellStyle = super.getCellStyle(record, rowNum, colNum);
        		if (getFieldName(colNum).equals("currentPrice")){
        			Double currentPrice = StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code"));
        			Double previousPrice = StockDetailStaticHolder.getPreviousPrice(record.getAttribute("code"));
        			if(currentPrice>previousPrice){
        				return "groupNodeGreen";
        			}else if(currentPrice<previousPrice){
        				return "groupNodeRed";
        			}
        		}
        		return cellStyle;
        	}
        };   
        
        return managePortfolioGridTmp;
	}
	public void initialize(){
        managePortfolioLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        managePortfolioLayout.setHeight(IntelliInvest.HEIGHT-165);
        managePortfolioLayout.setMembersMargin(0);
        managePortfolioLayout.setPadding(0);
        
        final ManagePortfolioSummaryDS managePortfolioSummaryDS =  new ManagePortfolioSummaryDS("SUMMARY");
        
        managePortfolioGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        managePortfolioGrid.setHeight(IntelliInvest.HEIGHT-242); 
       
        ListGridField idField = new ListGridField("managePortfolioId", "Id", 50);
        idField.setHidden(true);
        idField.setType(ListGridFieldType.TEXT);
        ListGridField codeField = new ListGridField("code", "Code", 0);
        codeField.setWrap(false);
        
        ListGridField directionField = new ListGridField("direction", "Direction", 50); 
        directionField.setWrap(false);
        directionField.setEmptyCellValue("Long");

        ListGridField quantityField = new ListGridField("quantity", "Traded", 50);
        quantityField.setWrap(false);
        quantityField.setType(ListGridFieldType.INTEGER);
        quantityField.setCellFormatter(CellStylesUtil.QUANTITY_CELL_FORMATTER);
        
        ListGridField remainingQuantityField = new ListGridField("remainingQuantity", "Balance", 60);
        remainingQuantityField.setWrap(false);
        remainingQuantityField.setType(ListGridFieldType.INTEGER);
        remainingQuantityField.setCellFormatter(CellStylesUtil.QUANTITY_CELL_FORMATTER);
        
        ListGridField priceField = new ListGridField("price", "Average", 70); 
        priceField.setWrap(false);
        priceField.setType(ListGridFieldType.FLOAT);
        priceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        final ListGridField currentPriceField = new ListGridField("currentPrice", "Current",60);
        currentPriceField.setWrap(false);
        currentPriceField.setType(ListGridFieldType.FLOAT);
        currentPriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);

        ListGridField cpField = new ListGridField("cp", "Change(%)",60);
        cpField.setWrap(false);
        cpField.setType(ListGridFieldType.FLOAT);
        cpField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField amountField = new ListGridField("amount", "Current", 80); 
        amountField.setWrap(false);
        amountField.setAutoFitWidth(true);
        amountField.setType(ListGridFieldType.FLOAT);
        amountField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField totalAmountField = new ListGridField("totalAmount", "Invested", 80); 
        totalAmountField.setWrap(false);
        totalAmountField.setAutoFitWidth(true);
        totalAmountField.setType(ListGridFieldType.FLOAT);
        totalAmountField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField realisedPnlField = new ListGridField("realisedPnl", "Realised",80);
        realisedPnlField.setWrap(false);
        realisedPnlField.setAutoFitWidth(true);
        realisedPnlField.setType(ListGridFieldType.FLOAT);
        realisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField unrealisedPnlField = new ListGridField("unrealisedPnl", "Unrealised",80); 
        unrealisedPnlField.setWrap(false);
        unrealisedPnlField.setAutoFitWidth(true);
        unrealisedPnlField.setType(ListGridFieldType.FLOAT);
        unrealisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField todaysPnlField = new ListGridField("todaysPnl", "Today",80); 
        todaysPnlField.setWrap(false);
        todaysPnlField.setAutoFitWidth(true);
        todaysPnlField.setType(ListGridFieldType.FLOAT);
        todaysPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField chartField = new ListGridField("chart", "Chart", 80);
        chartField.setWrap(false);
        chartField.setAlign(Alignment.CENTER);
        chartField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/intelliinvest.ico\" width=\"20\" height=\"20\" alt=\"View Chart\"></img>";
			}
		});
        
        ListGridField historyField = new ListGridField("history", "History", 80);
        historyField.setWrap(false);
        historyField.setAlign(Alignment.CENTER);
        historyField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/history.jpg\" width=\"20\" height=\"20\" alt=\"View History\"></img>";
			}
		});
        
        
        managePortfolioGrid.setFields(idField, codeField, directionField, quantityField, remainingQuantityField, 
        		priceField, currentPriceField, cpField, totalAmountField, amountField, realisedPnlField, unrealisedPnlField, todaysPnlField
        		, chartField, historyField);
        
        managePortfolioGrid.setHeaderSpans(  
                new HeaderSpan("Quantity", new String[]{"quantity", "remainingQuantity"}),  
                new HeaderSpan("Amount", new String[]{"totalAmount", "amount"}),  
                new HeaderSpan("Price", new String[]{"price", "currentPrice", "cp"}),  
                new HeaderSpan("PNL", new String[]{"todaysPnl", "realisedPnl", "unrealisedPnl"}));  
        
        codeField.setWidth("10%");
        directionField.setWidth("7%");
        
        quantityField.setWidth("7%");
        remainingQuantityField.setWidth("7%");
        
        priceField.setWidth("7%");
        currentPriceField.setWidth("7%");
        cpField.setWidth("8%");
        
        amountField.setWidth("9%");
        totalAmountField.setWidth("9%");
        realisedPnlField.setWidth("9%");
        unrealisedPnlField.setWidth("9%");
        todaysPnlField.setWidth("9%");
        
        chartField.setWidth("7%");
        historyField.setWidth("7%");
        
        
        codeField.setCanEdit(false);
        realisedPnlField.setCanEdit(false);
        unrealisedPnlField.setCanEdit(false);
        todaysPnlField.setCanEdit(false);
        currentPriceField.setCanEdit(false);
        cpField.setCanEdit(false);
        remainingQuantityField.setCanEdit(false);
        amountField.setCanEdit(false);
        totalAmountField.setCanEdit(false);
        
        codeField.setShowGridSummary(false);
        directionField.setShowGridSummary(false);
        quantityField.setShowGridSummary(false);
        remainingQuantityField.setShowGridSummary(false);
        priceField.setShowGridSummary(false);
        currentPriceField.setShowGridSummary(false);
        cpField.setShowGridSummary(false);
        chartField.setShowGridSummary(false);
        historyField.setShowGridSummary(false);
        totalAmountField.setShowGridSummary(true);
        realisedPnlField.setShowGridSummary(true);
        unrealisedPnlField.setShowGridSummary(true);
        todaysPnlField.setShowGridSummary(true);
        amountField.setShowGridSummary(true);
        
        totalAmountField.setSummaryFunction(new SummaryFunction() {
			
			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				Double totalInvestedAmount = 0D;
				for(Record record : records){
					try{
						totalInvestedAmount = totalInvestedAmount + new Double(record.getAttribute("totalAmount"));
					}catch(Exception e){
						totalInvestedAmount = totalInvestedAmount + 0D;
					}
				}
				return CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalInvestedAmount, null, 0, 0);
			}
		});
        
        amountField.setSummaryFunction(new SummaryFunction() {
			
			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				Double totalAmount = 0D;
				for(Record record : records){
					try{
						totalAmount = totalAmount + new Double(record.getAttribute("amount"));
					}catch(Exception e){
						totalAmount = totalAmount + 0D;
					}
				}
				return CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalAmount, null, 0, 0);
			}
		});
        
        realisedPnlField.setSummaryFunction(new SummaryFunction() {
			
			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
					Double totalRealisedPnl = 0D;
					for(Record record : records){
						try{
							totalRealisedPnl = totalRealisedPnl + new Double(record.getAttribute("realisedPnl"));
						}catch(Exception e){
							totalRealisedPnl = totalRealisedPnl + 0D;
						}
					}
					
					if(totalRealisedPnl>=0D){
						return "<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalRealisedPnl, null, 0, 0) +"</font>";
					}else{
						return "<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalRealisedPnl, null, 0, 0) +"</font>";
					}
			}
		});
        
        unrealisedPnlField.setSummaryFunction(new SummaryFunction() {
			
			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				Double totalUnrelaisedPnl = 0D;
				for(Record record : records){
					try{
						totalUnrelaisedPnl = totalUnrelaisedPnl + new Double(record.getAttribute("unrealisedPnl"));
					}catch(Exception e){
						totalUnrelaisedPnl = totalUnrelaisedPnl + 0D;
					}
				}
				if(totalUnrelaisedPnl>=0D){
					return "<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalUnrelaisedPnl, null, 0, 0) +"</font>";
				}else{
					return "<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalUnrelaisedPnl, null, 0, 0) +"</font>";
				}
			}
		});
        
        todaysPnlField.setSummaryFunction(new SummaryFunction() {
			
			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				Double totalTodaysPnl = 0D;
				for(Record record : records){
					try{
						totalTodaysPnl = totalTodaysPnl + new Double(record.getAttribute("todaysPnl"));
					}catch(Exception e){
						totalTodaysPnl = totalTodaysPnl + 0D;
					}
				}
				if(totalTodaysPnl>=0D){
					return "<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalTodaysPnl, null, 0, 0) +"</font>";
				}else{
					return "<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalTodaysPnl, null, 0, 0)+"</font>";
				}
			}
		});
        
        managePortfolioGrid.setDataSource(managePortfolioSummaryDS);
        managePortfolioGrid.setAlign(Alignment.CENTER);
        managePortfolioGrid.setCanReorderFields(false);   
        managePortfolioGrid.setAutoFetchData(true);
        managePortfolioGrid.setCanEdit(false);
        managePortfolioGrid.setCanResizeFields(false);
        managePortfolioGrid.setShowHeaderMenuButton(false);
        managePortfolioGrid.setShowHeaderContextMenu(false);
        managePortfolioGrid.setHeaderHeight(40);
        managePortfolioGrid.setShowGridSummary(true);  
        managePortfolioGrid.setAutoSaveEdits(false);
        managePortfolioGrid.setFilterOnKeypress(true);  
        managePortfolioGrid.setGroupIndentSize(10);
        managePortfolioGrid.setAutoFitData(Autofit.HORIZONTAL);
        managePortfolioGrid.setAutoFitMaxWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        
        managePortfolioGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == managePortfolioGrid.getFieldNum("chart")){
					if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
						SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "userEODChart");
					}else{
						SC.say("This is user specific chart. Please login to see chart");
					}
					event.cancel();
				}
			}
		});
        
        managePortfolioGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == managePortfolioGrid.getFieldNum("history")){
					if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
						PortfolioHistoryWindow.showPortfolioHistoryWindow(managePortfolioGrid, record.getAttribute("code"));
					}else{
						SC.say("History will be displayed for logged in user's only.");
					}
					event.cancel();
				}
			}
		});

        managePortfolioForm.setIsGroup(true);  
        managePortfolioForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) );   
        managePortfolioForm.setHeight("30");
        managePortfolioForm.setPadding(0);
        
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
        stockComboBox.setStartRow(true);
        stockComboBox.setEndRow(false);
        
        DateItem dateItem = new DateItem("date", "Date");
        dateItem.setRequired(true);
        dateItem.setUseTextField(true);
        dateItem.setWidth(90);
        dateItem.setDefaultValue(new Date());
        dateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        IntegerItem quantityItem = new IntegerItem("quantity", "Quantity"); 
        quantityItem.setRequired(true);
        quantityItem.setWidth(90);
       
        
        SelectItem directionItem = new SelectItem("direction", "Direction");
        directionItem.setValueMap("Buy", "Sell");
        directionItem.setDefaultToFirstOption(true);
        directionItem.setRequired(true);
        directionItem.setWidth(50);
        
        FloatItem priceItem = new FloatItem("price", "Price");
        priceItem.setRequired(true);
        priceItem.setWidth(60);
        
        final ButtonItem addButton = new ButtonItem("Add");
        addButton.setWidth("50");
        addButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(!managePortfolioForm.validate()){
					SC.say("Please correct errors before adding staock to your portfolio");
					return;
				}
				if( (null==IntelliInvest.userDetailData || IntelliInvest.userDetailData.getUserType().equals("Default") ) 
             			&& managePortfolioGrid.getTotalRows()>=10){
             		SC.say("Guest and Free login supports manage Portfolio of max 10 stocks.Please buy plans to have unlimited stock simulation");
             	}else{
 	                Record record = managePortfolioForm.getValuesAsRecord();  
 	                record.setAttribute("managePortfolioId", record.getAttribute("code"));
 	                if(managePortfolioGrid.getRecordIndex(record)==-1){
 	                	managePortfolioSummaryDS.addData(record);
 	                	managePortfolioSummaryDS.executeAdd();
 	                }else{
 	                	managePortfolioSummaryDS.updateData(record);
 	                	managePortfolioSummaryDS.executeUpdate();
 	                }
 	                
             	}
				
			}
		});
        addButton.setStartRow(false);
        addButton.setEndRow(true);
        
        
        managePortfolioForm.setGroupTitle("Add");
        managePortfolioForm.setWrapItemTitles(false);
        managePortfolioForm.setNumCols(12);  
        managePortfolioForm.setFields(stockComboBox, dateItem, quantityItem, directionItem, priceItem, addButton);
        
        stockComboBox.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			@Override
			public void onChanged(com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				if(null!=stockComboBox.getSelectedRecord()){
					if(null!=stockComboBox.getSelectedRecord().getAttribute("code") && !stockComboBox.getSelectedRecord().getAttribute("code").equals("")){
						managePortfolioForm.getField("code").setValue(stockComboBox.getSelectedRecord().getAttribute("code"));
						managePortfolioForm.getField("price").setValue(StockDetailStaticHolder.getCurrentPrice(stockComboBox.getSelectedRecord().getAttribute("code")));
					}
				}
			}
        });
        
        managePortfolioLayout.addMember(managePortfolioForm);
        
        managePortfolioLayout.addMember(enhancedManagePortfolioGrid.enhanceGrid("My Portfolio", managePortfolioGrid, true));
        enhancedManagePortfolioGrid.getSaveButton().setVisible(false);
        
//        addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(null==IntelliInvest.userDetailData){
//					SC.say("Please login to save changes");
//				}else{
//					managePortfolioGrid.saveAllEdits();
//					managePortfolioSummaryDS.executeAdd();
//					managePortfolioSummaryDS.executeUpdate();
//					managePortfolioSummaryDS.executeRemove();
//				}
//			}
//		});
        enhancedManagePortfolioGrid.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || managePortfolioGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								managePortfolioSummaryDS.setFetch(true);
								managePortfolioGrid.invalidateCache();
								managePortfolioGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					managePortfolioSummaryDS.setFetch(true);
					managePortfolioGrid.invalidateCache();
					managePortfolioGrid.fetchData();
				}
			}
		});
        
        managePortfolioSummaryDS.setGrid(managePortfolioGrid);
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				Record[] records = managePortfolioGrid.getRecords();
				for(Record record : records){
					String code = record.getAttribute("code");
					Double currentPrice = StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code"));
					Double eodPrice = StockDetailStaticHolder.getEODPrice(record.getAttribute("code"));
					Double cp = StockDetailStaticHolder.getCP(record.getAttribute("code"));
					Double remainingQuantity = new Double(record.getAttribute("remainingQuantity"));
					Double quantity = new Double(record.getAttribute("quantity"));
					Double price = new Double(record.getAttribute("price"));
					
					record.setAttribute("cp", StockDetailStaticHolder.getCP(code));
					record.setAttribute("currentPrice", currentPrice);
					record.setAttribute("amount", remainingQuantity * currentPrice);
					record.setAttribute("totalAmount", quantity * price );
					record.setAttribute("unrealisedPnl", remainingQuantity * (currentPrice - price));
					record.setAttribute("todaysPnl", (remainingQuantity * eodPrice * cp)/100);
				}
				managePortfolioGrid.redraw();
				managePortfolioGrid.recalculateGridSummary();
			}
		};
		RefreshPriceHandler.addHandler("managePortfolioGridRefreshHandler" , refreshPriceHandler);
	}
	
	public void refresh(){
		
	}

}


class ManagePortfolioRecord extends ListGridRecord {  
  
    public ManagePortfolioRecord(ManagePortfolioData managePortfolioData) {  
    	
    	Double currentPrice = StockDetailStaticHolder.getCurrentPrice(managePortfolioData.getCode());
		Double eodPrice = StockDetailStaticHolder.getEODPrice(managePortfolioData.getCode());
		Double cp = StockDetailStaticHolder.getCP(managePortfolioData.getCode());
		
    	setId(managePortfolioData.getId());
    	setCode(managePortfolioData.getCode());
    	setDirection(managePortfolioData.getDirection());
    	setDate(managePortfolioData.getDate());
    	setPrice(managePortfolioData.getPrice());
    	setQuantity(managePortfolioData.getQuantity());
    	setRemainingQuantity(managePortfolioData.getRemainingQuantity());
    	setRealisedPnl(managePortfolioData.getRealisedPnl());
    	setUnrealisedPnl(managePortfolioData.getRemainingQuantity() * (currentPrice - managePortfolioData.getPrice()));
    	setAmount(managePortfolioData.getRemainingQuantity() * currentPrice);
    	setTotalAmount(managePortfolioData.getQuantity() * managePortfolioData.getPrice());
    	setTodaysPnl((managePortfolioData.getRemainingQuantity() * eodPrice * cp)/100);
    }   
  
    public String getId() {  
        return getAttribute("id");  
    }  
  
    public void setId(String id) {  
        setAttribute("id", id);  
    }  
    
    public String getCode() {  
        return getAttribute("code");  
    }  
  
    public void setCode(String code) {  
        setAttribute("code", code);  
    }  
    
    public String getDirection() {  
        return getAttribute("direction");  
    }  
  
    public void setDirection(String direction) {  
        setAttribute("direction", direction);  
    }  
  
    public Date getDate() {  
        return DateUtil.getDate(getAttribute("date"));  
    }  
  
    public void setDate(Date date) {  
        setAttribute("date", date);  
    } 
    
    public Double getPrice() {  
        return new Double(getAttribute("price"));  
    }  
  
    public void setPrice(Double price) {  
        setAttribute("price", price);  
    } 
    
    public Integer getQuantity() {  
        return new Integer(getAttribute("quantity"));  
    }  
  
    public void setQuantity(Integer quantity) {  
        setAttribute("quantity", quantity);  
    } 
    
    public Integer getRemainingQuantity() {  
        return new Integer(getAttribute("remainingQuantity"));  
    }  
  
    public void setRemainingQuantity(Integer remainingQuantity) {  
        setAttribute("remainingQuantity", remainingQuantity);  
    } 
    
    public Double getRealisedPnl() {  
        return new Double(getAttribute("realisedPnl"));  
    }  
  
    public void setRealisedPnl(Double realisedPnl) {  
        setAttribute("realisedPnl", realisedPnl);  
    } 
    
    public Double getUnrealisedPnl() {  
        return new Double(getAttribute("unrealisedPnl"));  
    }  
  
    public void setUnrealisedPnl(Double unrealisedPnl) {  
        setAttribute("unrealisedPnl", unrealisedPnl);  
    } 
    
    public Double getAmount() {  
        return new Double(getAttribute("amount"));  
    }  
  
    public void setAmount(Double amount) {  
        setAttribute("amount", amount);  
    } 
    
    public Double getTotalAmount() {  
        return new Double(getAttribute("totalAmount"));  
    }  
  
    public void setTotalAmount(Double totalAmount) {  
        setAttribute("totalAmount", totalAmount);  
    } 
    
    public Double getTodaysPnl() {  
        return new Double(getAttribute("todaysPnl"));  
    }  
  
    public void setTodaysPnl(Double todaysPnl) {  
        setAttribute("todaysPnl", todaysPnl);  
    } 
	
}  
