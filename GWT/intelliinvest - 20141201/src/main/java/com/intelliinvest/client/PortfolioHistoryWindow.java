package com.intelliinvest.client;

import com.intelliinvest.client.datasource.ManagePortfolioSummaryDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class PortfolioHistoryWindow {

	public static void showPortfolioHistoryWindow(final ListGrid grid, final String code){
		Window portfolioHistoryWindow = new Window();
		portfolioHistoryWindow.setOverflow(Overflow.VISIBLE);
		portfolioHistoryWindow.setWidth(IntelliInvest.WIDTH - 255 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		portfolioHistoryWindow.setHeight("100");
        portfolioHistoryWindow.setShowMinimizeButton(false);  
        portfolioHistoryWindow.setIsModal(true);  
        portfolioHistoryWindow.setPadding(0);
		portfolioHistoryWindow.setShowCloseButton(true);
		portfolioHistoryWindow.centerInPage();
		portfolioHistoryWindow.setAutoSize(true);
		portfolioHistoryWindow.addItem(getPortfolioHistoryGridLayout(grid, code));
		portfolioHistoryWindow.setTitle("History for " + code);
		portfolioHistoryWindow.centerInPage();
		portfolioHistoryWindow.show();
	}

	public static Layout getPortfolioHistoryGridLayout(final ListGrid grid, final String code){
		final ListGrid detailsGrid = new ListGrid(){
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
        	
		};
        detailsGrid.setWidth(IntelliInvest.WIDTH - 255 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) );  
        detailsGrid.setHeight(100);  
        detailsGrid.setCellHeight(22);  
        detailsGrid.setCanEdit(true);  
        detailsGrid.setModalEditing(true);  
        detailsGrid.setEditEvent(ListGridEditEvent.CLICK);  
        detailsGrid.setListEndEditAction(RowEndEditAction.NEXT);  
        detailsGrid.setAutoSaveEdits(false);  
        detailsGrid.setCanResizeFields(false);
        detailsGrid.setShowHeaderMenuButton(false);
        detailsGrid.setShowHeaderContextMenu(false);
        detailsGrid.setAutoFitData(Autofit.BOTH);
        detailsGrid.setAutoFitMaxHeight(250);
        detailsGrid.setSortField("date");
        detailsGrid.setSortDirection(SortDirection.DESCENDING);
        detailsGrid.setHeaderHeight(40);
        detailsGrid.setAutoFetchData(true);
        
        ListGridField idField = new ListGridField("id", "Id", 50);
        idField.setHidden(true);
        
        idField.setType(ListGridFieldType.TEXT);
        ListGridField codeField = new ListGridField("code", "Code", 0);
        codeField.setWrap(false);
        
        ListGridField directionField = new ListGridField("direction", "Direction", 50); 
        directionField.setWrap(false);
        directionField.setEmptyCellValue("Buy");

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
        currentPriceField.setWrap(true);
        currentPriceField.setType(ListGridFieldType.FLOAT);
        currentPriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField amountField = new ListGridField("amount", "Current", 80); 
        amountField.setWrap(false);
        amountField.setType(ListGridFieldType.FLOAT);
        amountField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField totalAmountField = new ListGridField("totalAmount", "Invested", 80); 
        totalAmountField.setWrap(false);
        totalAmountField.setType(ListGridFieldType.FLOAT);
        totalAmountField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField realisedPnlField = new ListGridField("realisedPnl", "Realised",80);
        realisedPnlField.setWrap(false);
        realisedPnlField.setType(ListGridFieldType.FLOAT);
        realisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField unrealisedPnlField = new ListGridField("unrealisedPnl", "Unrealised",80); 
        unrealisedPnlField.setWrap(false);
        unrealisedPnlField.setType(ListGridFieldType.FLOAT);
        unrealisedPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField todaysPnlField = new ListGridField("todaysPnl", "Today",80); 
        todaysPnlField.setWrap(false);
        todaysPnlField.setType(ListGridFieldType.FLOAT);
        todaysPnlField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField dateField = new ListGridField("date", "Date", 80);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setAlign(Alignment.CENTER);
        dateField.setFilterEditorProperties(new TextItem());
        dateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        dateField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				detailsGrid.getEditedRecord(event.getRowNum()).setAttribute("date", event.getValue());
			}
		});
        
        codeField.setWidth("10%");
        dateField.setWidth("10%");
        directionField.setWidth("7%");
        quantityField.setWidth("9%");
        remainingQuantityField.setWidth("9%");
        priceField.setWidth("7%");
        amountField.setWidth("10%");
        totalAmountField.setWidth("10%");
        realisedPnlField.setWidth("10%");
        unrealisedPnlField.setWidth("9%");
        todaysPnlField.setWidth("9%");
        
        codeField.setCanEdit(false);
        realisedPnlField.setCanEdit(false);
        unrealisedPnlField.setCanEdit(false);
        todaysPnlField.setCanEdit(false);
        remainingQuantityField.setCanEdit(false);
        amountField.setCanEdit(false);
        totalAmountField.setCanEdit(false);
        
        detailsGrid.setFields(idField, codeField, directionField, dateField, quantityField, remainingQuantityField, 
        		priceField, currentPriceField, totalAmountField, amountField, realisedPnlField, unrealisedPnlField, todaysPnlField);
        
        detailsGrid.setHeaderSpans(  
                new HeaderSpan("Quantity", new String[]{"quantity", "remainingQuantity"}),  
                new HeaderSpan("Amount", new String[]{"totalAmount", "amount"}),  
                new HeaderSpan("Price", new String[]{"price", "currentPrice"}),  
                new HeaderSpan("PNL", new String[]{"todaysPnl", "realisedPnl", "unrealisedPnl"}));  
        
        final ManagePortfolioSummaryDS managePortfolioSummaryDS = new ManagePortfolioSummaryDS(code);
        managePortfolioSummaryDS.setGrid(grid);
        detailsGrid.setDataSource(managePortfolioSummaryDS);
       
        VLayout layout = new VLayout(5);  
        layout.setPadding(5);  
        
        HLayout hLayout = new HLayout(10);  
        hLayout.setAlign(Alignment.RIGHT);  

        IButton saveButton = new IButton("Save");  
        saveButton.setTop(250);  
        saveButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	if(null==IntelliInvest.userDetailData){
					SC.say("Please login to save changes");
				}else{
					detailsGrid.saveAllEdits();
					managePortfolioSummaryDS.executeAdd();
					managePortfolioSummaryDS.executeUpdate();
					managePortfolioSummaryDS.executeRemove();
				}
            }  
        });  
        hLayout.addMember(saveButton);  

        IButton discardButton = new IButton("Discard");  
        discardButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
                detailsGrid.discardAllEdits();  
            }  
        });  
        hLayout.addMember(discardButton);
        
        IButton refreshButton = new IButton("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || detailsGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								managePortfolioSummaryDS.setFetch(true);
								detailsGrid.invalidateCache();
								detailsGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					managePortfolioSummaryDS.setFetch(true);
					detailsGrid.invalidateCache();
					detailsGrid.fetchData();
				}
			}
		});

        hLayout.addMember(refreshButton);
        
        layout.addMember(hLayout);  
        
        layout.addMember(detailsGrid);

        return layout;  
		
	}

	
}
