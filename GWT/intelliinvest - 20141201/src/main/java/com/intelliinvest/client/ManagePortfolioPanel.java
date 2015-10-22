package com.intelliinvest.client;

import java.util.Date;

import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.datasource.ManagePortfolioDS;
import com.intelliinvest.client.datasource.TradingAccountDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
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
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.GridSummaryCustomizer;
import com.smartgwt.client.widgets.grid.GroupSummary;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.SummaryFunction;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ManagePortfolioPanel implements IntelliInvestPanel{
	final Layout managePortfolioLayout;
	final ListGrid managePortfolioGrid;
	final DynamicForm managePortfolioForm; 
	final EnhancedGrid enhancedManagePortfolioGrid;
	final TradingAccountDS tradingAccountDS = new TradingAccountDS();
	Boolean resize = false;
	public static int clickedRow = -1;
	public static String clickedCode = "";
	
	public ManagePortfolioPanel() {
		this.managePortfolioLayout = new VLayout();
		this.managePortfolioGrid = createManagePortfolioGrid();
		this.managePortfolioForm = new DynamicForm(); 
		enhancedManagePortfolioGrid = new EnhancedGrid();
		tradingAccountDS.fetchData();
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
	       		 	if(null!=record.getAttribute("code")){
						Double price = null!=record.getAttribute("price")?new Double(record.getAttribute("price")):0;
						Double currentPrice = StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code"));
						Integer remainingQuantity = null!=record.getAttribute("remainingQuantity")?new Integer(record.getAttribute("remainingQuantity")):0;
						record.setAttribute("unrealisedPnl", remainingQuantity * (currentPrice - price));
	       		 	}
        			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }else if(getFieldName(colNum).equals("todaysPnl")) { 
        			 if(null!=record.getAttribute("code")){
 						Double price = StockDetailStaticHolder.getEODPrice(record.getAttribute("code"));
 						Double cp = StockDetailStaticHolder.getCP(record.getAttribute("code"));
 						Integer remainingQuantity = null!=record.getAttribute("remainingQuantity")?new Integer(record.getAttribute("remainingQuantity")):0;
 						record.setAttribute("todaysPnl", (remainingQuantity * price * cp)/100);
        			 }
        			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }else if (getFieldName(colNum).equals("cp")) { 
        			 if(null!=record.getAttribute("code")){
 						record.setAttribute("cp", StockDetailStaticHolder.getCP(record.getAttribute("code")));
        			 }
        			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
        		 }
        		 return super.getCellCSSText(record, rowNum, colNum);
        	}
        	
        	@Override
        	protected String getCellStyle(ListGridRecord record, int rowNum, int colNum) {
        		String cellStyle = super.getCellStyle(record, rowNum, colNum);
        		if (getFieldName(colNum).equals("currentPrice") && null!=record.getIsGroupSummary() && record.getIsGroupSummary()){
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
        
        managePortfolioGridTmp.addRowContextClickHandler(new RowContextClickHandler() {
			public void onRowContextClick(RowContextClickEvent event) {
				clickedRow = event.getRowNum();
				clickedCode = event.getRecord().getAttribute("code");
			}
			
		});
        
//        Menu menu = new Menu();
//        MenuItem addToWatchListItem = new MenuItem("Add to watch list");
//        addToWatchListItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//			
//			@Override
//			public void onClick(MenuItemClickEvent event) {
//				if(StockDetailStaticHolder.intelliStockDetailsMap.containsKey(clickedCode)){
// 	                Record record = new Record(); 
// 	                record.setAttribute("code", clickedCode);
// 	                tradingAccountDS.addData(record);
// 	                tradingAccountDS.executeAdd();
//				}else{
//					SC.say("Sorry we dont have analysis for selected stock. Please send a mail to admin to add it for you.");
//				}
//			}
//		});
//		menu.addItem(addToWatchListItem);
//		managePortfolioGridTmp.setContextMenu(menu);
        return managePortfolioGridTmp;
	}
	public void initialize(){
        managePortfolioLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        managePortfolioLayout.setHeight(IntelliInvest.HEIGHT-165);
        managePortfolioLayout.setMembersMargin(0);
        managePortfolioLayout.setPadding(0);
        
        final ManagePortfolioDS managePortfolioDS =  new ManagePortfolioDS();
        
        managePortfolioGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        managePortfolioGrid.setHeight(IntelliInvest.HEIGHT-242); 
       
        ListGridField idField = new ListGridField("managePortfolioId", "Id", 50);
        idField.setHidden(true);
        idField.setType(ListGridFieldType.TEXT);
        ListGridField codeField = new ListGridField("code", "Code", 0);
        codeField.setWrap(false);
//        codeField.setAutoFitWidth(true);
//        codeField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        codeField.setHidden(true);
        
        ListGridField directionField = new ListGridField("direction", "Direction", 50); 
        directionField.setWrap(false);
        directionField.setEmptyCellValue("Buy");
        directionField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) {  
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getDirection();
            	}else{
            		return "--";
            	}
            }  
        }); 
        ListGridField dateField = new ListGridField("date", "Date", 80);
        dateField.setWrap(false);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setShowGroupSummary(false);
        dateField.setAlign(Alignment.CENTER);
        dateField.setFilterEditorProperties(new TextItem());
        dateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        dateField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				managePortfolioGrid.getEditedRecord(event.getRowNum()).setAttribute("date", event.getValue());
			}
		});
        dateField.setShowGroupSummary(true);
        dateField.setSummaryFunction(new SummaryFunction() {
			@Override
			public Object getSummaryValue(final Record[] records, ListGridField field) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/intelliinvest.ico\" width=\"20\" height=\"20\" alt=\"View Chart\"></img>";
			}
		});
        
        ListGridField quantityField = new ListGridField("quantity", "Traded", 50);
        quantityField.setWrap(false);
        quantityField.setType(ListGridFieldType.INTEGER);
        quantityField.setCellFormatter(CellStylesUtil.QUANTITY_CELL_FORMATTER);
        quantityField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getQuantity();
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField remainingQuantityField = new ListGridField("remainingQuantity", "Balance", 60);
        remainingQuantityField.setWrap(false);
        remainingQuantityField.setType(ListGridFieldType.INTEGER);
        remainingQuantityField.setCellFormatter(CellStylesUtil.QUANTITY_CELL_FORMATTER);
        remainingQuantityField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRemainingQuantity();
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField priceField = new ListGridField("price", "Average", 70); 
        priceField.setWrap(false);
        priceField.setType(ListGridFieldType.FLOAT);
        priceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        priceField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) {  
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getPrice();
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        
        final ListGridField currentPriceField = new ListGridField("currentPrice", "Current",60);
        currentPriceField.setWrap(false);
        currentPriceField.setType(ListGridFieldType.FLOAT);
        currentPriceField.setCellFormatter(new CellFormatter() {
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
        currentPriceField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return StockDetailStaticHolder.getCurrentPrice(records[0].getAttribute("code"));
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField cpField = new ListGridField("cp", "Change(%)",60);
        cpField.setWrap(false);
        cpField.setType(ListGridFieldType.FLOAT);
        cpField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
//        cpField.setCellFormatter(new CellFormatter() {
//			@Override
//			public String format(Object value, ListGridRecord record, int rowNum,
//					int colNum) {
//					if(null!=record.getAttribute("code")){
//						record.setAttribute("cp", StockDetailStaticHolder.getCP(record.getAttribute("code")));
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(StockDetailStaticHolder.getCP(record.getAttribute("code")), record, rowNum, colNum);
//					}else{
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
//					}
//			}
//		});
        cpField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return StockDetailStaticHolder.getCP(records[0].getAttribute("code"));
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField amountField = new ListGridField("amount", "Current", 80); 
        amountField.setWrap(false);
        amountField.setAutoFitWidth(true);
        amountField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        amountField.setType(ListGridFieldType.FLOAT);
        amountField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
					if(null!=record.getAttribute("code")){
						Double price = StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code"));
						Integer quantity = null!=record.getAttribute("remainingQuantity")?new Integer(record.getAttribute("remainingQuantity")):0;
						record.setAttribute("amount", quantity * price);
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(quantity * price, record, rowNum, colNum);
					}else{
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
					}
			}
		});
        amountField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		Integer remainingQuantity = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRemainingQuantity();
            		Double price = StockDetailStaticHolder.getCurrentPrice(records[0].getAttribute("code"));
            		return remainingQuantity * price;
            	}else{
            		return "0";
            	}
            }  
        });  
        amountField.setShowGridSummary(true);
        
        ListGridField totalAmountField = new ListGridField("totalAmount", "Invested", 80); 
        totalAmountField.setWrap(false);
        totalAmountField.setAutoFitWidth(true);
        totalAmountField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        totalAmountField.setType(ListGridFieldType.FLOAT);
        totalAmountField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
					if(null!=record.getAttribute("code")){
						Double price = null!=record.getAttribute("price")?new Double(record.getAttribute("price")):0D;
						Integer quantity = null!=record.getAttribute("quantity")?new Integer(record.getAttribute("quantity")):0;
						record.setAttribute("totalAmount", quantity * price);
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(quantity * price, record, rowNum, colNum);
					}else{
						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
					}
			}
		});
        totalAmountField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		Integer remainingQuantity = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRemainingQuantity();
            		Double price = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getPrice();
            		return remainingQuantity * price;
            	}else{
            		return "0";
            	}
            }  
        });  
        
        ListGridField realisedPnlField = new ListGridField("realisedPnl", "Realised",80);
        realisedPnlField.setWrap(false);
        realisedPnlField.setAutoFitWidth(true);
        realisedPnlField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        realisedPnlField.setType(ListGridFieldType.FLOAT);
        realisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        realisedPnlField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		return ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRealisedPnl();
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField unrealisedPnlField = new ListGridField("unrealisedPnl", "Unrealised",80); 
        unrealisedPnlField.setWrap(false);
        unrealisedPnlField.setAutoFitWidth(true);
        unrealisedPnlField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        unrealisedPnlField.setType(ListGridFieldType.FLOAT);
        unrealisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
//        unrealisedPnlField.setCellFormatter(new CellFormatter() {
//			@Override
//			public String format(Object value, ListGridRecord record, int rowNum,
//					int colNum) {
//					if(null!=record.getAttribute("code")){
//						Double price = null!=record.getAttribute("price")?new Double(record.getAttribute("price")):0;
//						Double currentPrice = StockDetailStaticHolder.getCurrentPrice(record.getAttribute("code"));
//						Integer remainingQuantity = null!=record.getAttribute("remainingQuantity")?new Integer(record.getAttribute("remainingQuantity")):0;
//						record.setAttribute("unrealisedPnl", remainingQuantity * (currentPrice - price));
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(remainingQuantity * (currentPrice - price), record, rowNum, colNum);
//					}else{
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
//					}
//			}
//		});
        unrealisedPnlField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		Double price = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getPrice();
            		Double currentPrice = StockDetailStaticHolder.getCurrentPrice(records[0].getAttribute("code"));
					Integer remianingQuantity = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRemainingQuantity();
					return remianingQuantity * (currentPrice-price);
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        ListGridField todaysPnlField = new ListGridField("todaysPnl", "Today",80); 
        todaysPnlField.setWrap(false);
        todaysPnlField.setAutoFitWidth(true);
        todaysPnlField.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        todaysPnlField.setType(ListGridFieldType.FLOAT);
        todaysPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
//        todaysPnlField.setCellFormatter(new CellFormatter() {
//			@Override
//			public String format(Object value, ListGridRecord record, int rowNum,
//					int colNum) {
//					if(null!=record.getAttribute("code")){
//						Double price = StockDetailStaticHolder.getEODPrice(record.getAttribute("code"));
//						Double cp = StockDetailStaticHolder.getCP(record.getAttribute("code"));
//						Integer remainingQuantity = null!=record.getAttribute("remainingQuantity")?new Integer(record.getAttribute("remainingQuantity")):0;
//						record.setAttribute("todaysPnl", remainingQuantity * cp);
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format((remainingQuantity * price * cp)/100, record, rowNum, colNum);
//					}else{
//						return CellStylesUtil.NUMBER_CELL_FORMATTER.format(value, record, rowNum, colNum);
//					}
//			}
//		});
        todaysPnlField.setSummaryFunction(new SummaryFunction() {  
            public Object getSummaryValue(Record[] records, ListGridField field) { 
            	if(null!=managePortfolioDS.getSummaryRecords() && managePortfolioDS.getSummaryRecords().containsKey(records[0].getAttribute("code"))){
            		Double price = StockDetailStaticHolder.getEODPrice(records[0].getAttribute("code"));
            		Double cp = StockDetailStaticHolder.getCP(records[0].getAttribute("code"));
					Integer remainingQuantity = ((ManagePortfolioData)(managePortfolioDS.getSummaryRecords().get(records[0].getAttribute("code")))).getRemainingQuantity();
					return (remainingQuantity * price * cp)/100;
            	}else{
            		return "0";
            	}
            }  
        }); 
        
        managePortfolioGrid.setFields(idField, codeField, directionField, quantityField, remainingQuantityField, 
        		priceField, currentPriceField, cpField, totalAmountField, amountField, realisedPnlField, unrealisedPnlField, todaysPnlField, dateField);
        
        managePortfolioGrid.setHeaderSpans(  
                new HeaderSpan("Quantity", new String[]{"quantity", "remainingQuantity"}),  
                new HeaderSpan("Amount", new String[]{"totalAmount", "amount"}),  
                new HeaderSpan("Price", new String[]{"price", "currentPrice", "cp"}),  
                new HeaderSpan("PNL", new String[]{"todaysPnl", "realisedPnl", "unrealisedPnl"}));  
        
        codeField.setWidth("10%");
        directionField.setWidth("7%");
        dateField.setWidth("8%");
        
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
        dateField.setShowGridSummary(false);
        totalAmountField.setShowGridSummary(true);
        realisedPnlField.setShowGridSummary(true);
        unrealisedPnlField.setShowGridSummary(true);
        todaysPnlField.setShowGridSummary(true);
        amountField.setShowGridSummary(true);
        
        totalAmountField.setGridSummaryCustomizer(new GridSummaryCustomizer() {
			@Override
			public Object[] getGridSummary(ListGridRecord[] records, ListGridField field, GroupSummary[] groupSummary) {
				if(field.getName().equals("totalAmount") && null!=groupSummary){
					Double totalInvestedAmount = 0D;
					for(GroupSummary GroupSummaryValue : groupSummary){
						try{
							totalInvestedAmount = totalInvestedAmount + new Double(GroupSummaryValue.getAttribute("totalAmount"));
						}catch(Exception e){
							totalInvestedAmount = totalInvestedAmount + 0D;
						}
					}
					return new String[]{ CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalInvestedAmount, null, 0, 0)};
				}else{
					return new Object[0];
				}
			}
		});
        
        amountField.setGridSummaryCustomizer(new GridSummaryCustomizer() {
			@Override
			public Object[] getGridSummary(ListGridRecord[] records, ListGridField field, GroupSummary[] groupSummary) {
				if(field.getName().equals("amount") && null!=groupSummary){
					Double totalAmount = 0D;
					for(GroupSummary GroupSummaryValue : groupSummary){
						try{
							totalAmount = totalAmount + new Double(GroupSummaryValue.getAttribute("amount"));
						}catch(Exception e){
							totalAmount = totalAmount + 0D;
						}
					}
					return new String[]{ CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalAmount, null, 0, 0)};
				}else{
					return new Object[0];
				}
			}
		});
        
        realisedPnlField.setGridSummaryCustomizer(new GridSummaryCustomizer() {
			@Override
			public Object[] getGridSummary(ListGridRecord[] records, ListGridField field, GroupSummary[] groupSummary) {
				if(field.getName().equals("realisedPnl") && null!=groupSummary){
					Double totalRealisedPnl = 0D;
					for(GroupSummary GroupSummaryValue : groupSummary){
						try{
							totalRealisedPnl = totalRealisedPnl + new Double(GroupSummaryValue.getAttribute("realisedPnl"));
						}catch(Exception e){
							totalRealisedPnl = totalRealisedPnl + 0D;
						}
					}
					if(totalRealisedPnl>=0D){
						return new String[]{"<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalRealisedPnl, null, 0, 0) +"</font>"};
					}else{
						return new String[]{"<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalRealisedPnl, null, 0, 0) +"</font>"};
					}
				}else{
					return new Object[0];
				}
			}
		});
        
        unrealisedPnlField.setGridSummaryCustomizer(new GridSummaryCustomizer() {
			@Override
			public Object[] getGridSummary(ListGridRecord[] records, ListGridField field, GroupSummary[] groupSummary) {
				if(field.getName().equals("unrealisedPnl") && null!=groupSummary){
					Double totalUnrelaisedPnl = 0D;
					for(GroupSummary GroupSummaryValue : groupSummary){
						try{
							totalUnrelaisedPnl = totalUnrelaisedPnl + new Double(GroupSummaryValue.getAttribute("unrealisedPnl"));
						}catch(Exception e){
							totalUnrelaisedPnl = totalUnrelaisedPnl + 0D;
						}
					}
					if(totalUnrelaisedPnl>=0D){
						return new String[]{"<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalUnrelaisedPnl, null, 0, 0) +"</font>"};
					}else{
						return new String[]{"<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalUnrelaisedPnl, null, 0, 0) +"</font>"};
					}
				}else{
					return new Object[0];
				}
			}
		});
        
        todaysPnlField.setGridSummaryCustomizer(new GridSummaryCustomizer() {
			@Override
			public Object[] getGridSummary(ListGridRecord[] records, ListGridField field, GroupSummary[] groupSummary) {
				if(field.getName().equals("todaysPnl") && null!=groupSummary){
					Double totalTodaysPnl = 0D;
					for(GroupSummary GroupSummaryValue : groupSummary){
						try{
							totalTodaysPnl = totalTodaysPnl + new Double(GroupSummaryValue.getAttribute("todaysPnl"));
						}catch(Exception e){
							totalTodaysPnl = totalTodaysPnl + 0D;
						}
					}
					if(totalTodaysPnl>=0D){
						return new String[]{"<font class=\"tallCellGreen\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalTodaysPnl, null, 0, 0) +"</font>"};
					}else{
						return new String[]{"<font class=\"tallCellRed\">" + CellStylesUtil.NUMBER_CELL_FORMATTER.format(totalTodaysPnl, null, 0, 0)+"</font>"};
					}
				}else{
					return new Object[0];
				}
			}
		});
        
        managePortfolioGrid.setDataSource(managePortfolioDS);
        managePortfolioGrid.setAlign(Alignment.CENTER);
        managePortfolioGrid.setCanReorderFields(false);   
        managePortfolioGrid.setAutoFetchData(true);
        managePortfolioGrid.setCanEdit(true);
        managePortfolioGrid.setCanResizeFields(false);
        managePortfolioGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        managePortfolioGrid.setShowHeaderMenuButton(false);
        managePortfolioGrid.setShowHeaderContextMenu(false);
        managePortfolioGrid.setHeaderHeight(40);
        managePortfolioGrid.setGroupByField("code");  
        managePortfolioGrid.setGroupStartOpen(GroupStartOpen.NONE); 
        managePortfolioGrid.setShowGridSummary(true);  
        managePortfolioGrid.setShowGroupSummary(true);  
        managePortfolioGrid.setShowGroupSummaryInHeader(true);  
        managePortfolioGrid.setAutoSaveEdits(false);
        managePortfolioGrid.setFilterOnKeypress(true);  
        managePortfolioGrid.setGroupIndentSize(10);
        ListGridField groupField = new ListGridField();
        groupField.setTitle("Code");
        groupField.setWrap(false);
        managePortfolioGrid.setGroupTitleColumnProperties(groupField);
//        managePortfolioGrid.setGroupTitleField("code");
        managePortfolioGrid.setAutoFitData(Autofit.HORIZONTAL);
        managePortfolioGrid.setAutoFitMaxWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        
        managePortfolioGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				System.out.println("inside onCellClick");
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == managePortfolioGrid.getFieldNum("date") && null!=record.getAttribute("date") && record.getAttribute("date").contains("Chart")){
					if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
						SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "userEODChart");
					}else{
						SC.say("This is user specific chart. Please login to see chart");
					}
					event.cancel();
				}
			}
		});

//        HStack addStack = new HStack();
//        addStack.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
//        addStack.setHeight(30);
//        addStack.setPadding(0);
        
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
        
//        StaticTextItem nameItem = new StaticTextItem("name", "Name");
//        nameItem.setWrap(false);
//        nameItem.setWidth(150);
//        nameItem.setRequired(true);
        
//        StaticTextItem dummyTextItem = new StaticTextItem("dummy");
//        dummyTextItem.setShowTitle(false);
//        dummyTextItem.setWidth(5);
//        dummyTextItem.setValue("");
        
        final ButtonItem addButton = new ButtonItem("Add");
        addButton.setWidth("50");
        addButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				if(addButton.isDisabled()){
//					return;
//				}
				if(!managePortfolioForm.validate()){
					SC.say("Please correct errors before adding staock to your portfolio");
					return;
				}
				if( (null==IntelliInvest.userDetailData || IntelliInvest.userDetailData.getUserType().equals("Default") ) 
             			&& managePortfolioGrid.getTotalRows()>=10){
             		SC.say("Guest and Free login supports manage Portfolio of max 10 stocks.Please buy plans to have unlimited stock simulation");
             	}else{
//             		addButton.disable();
 	                Record record = managePortfolioForm.getValuesAsRecord();  
 	                managePortfolioDS.addData(record);
 	                managePortfolioDS.executeAdd();
// 	                addButton.enable();
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
        
//        addStack.addMember(managePortfolioForm);
        
        managePortfolioLayout.addMember(managePortfolioForm);
        
        managePortfolioLayout.addMember(enhancedManagePortfolioGrid.enhanceGrid("My Portfolio", managePortfolioGrid, true));
        enhancedManagePortfolioGrid.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData){
					SC.say("Please login to save changes");
				}else{
					managePortfolioGrid.saveAllEdits();
					managePortfolioDS.executeAdd();
					managePortfolioDS.executeUpdate();
					managePortfolioDS.executeRemove();
				}
			}
		});
        enhancedManagePortfolioGrid.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || managePortfolioGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								managePortfolioDS.setFetch(true);
								managePortfolioGrid.invalidateCache();
								managePortfolioGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					managePortfolioDS.setFetch(true);
					managePortfolioGrid.invalidateCache();
					managePortfolioGrid.fetchData();
				}
			}
		});
        
        managePortfolioDS.setGrid(managePortfolioGrid);
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				managePortfolioGrid.redraw();
				managePortfolioGrid.recalculateGridSummary();
			}
		};
		RefreshPriceHandler.addHandler("managePortfolioGridRefreshHandler" , refreshPriceHandler);
	}
	
	public void refresh(){
		
	}

}
