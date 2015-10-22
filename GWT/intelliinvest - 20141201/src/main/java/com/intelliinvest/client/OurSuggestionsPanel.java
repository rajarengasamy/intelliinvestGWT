
package com.intelliinvest.client;

import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.OurOptionSuggestionDS;
import com.intelliinvest.client.datasource.OurSuggestionDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class OurSuggestionsPanel implements IntelliInvestPanel{
	final Layout ourSuggestionsLayout;
	
	final HStack ourSuggestionsStackH1;
	final HStack ourSuggestionsStackH2;
	
	final OurSuggestionDS ourSuggestionDS1;
	final OurSuggestionDS ourSuggestionDS2;
//	final OurSuggestionDS ourSuggestionDS3;
//	final OurSuggestionDS ourSuggestionDS4;
	final OurOptionSuggestionDS ourOptionSuggestionDS;
	
	final ListGrid ourSuggestionGrid1;
	final ListGrid ourSuggestionGrid2;
//	final ListGrid ourSuggestionGrid3;
//	final ListGrid ourSuggestionGrid4;
	final ListGrid ourOptionSuggestionGrid;
	
	final EnhancedGrid enhancedSimulationGrid1;
	final EnhancedGrid enhancedSimulationGrid2;
//	final EnhancedGrid enhancedSimulationGrid3;
//	final EnhancedGrid enhancedSimulationGrid4;
	final EnhancedGrid enhancedOptionSimulationGrid;
	
	Boolean resize = false;
	
	public OurSuggestionsPanel() {
		this.ourSuggestionsLayout = new VLayout();
		
		this.enhancedSimulationGrid1 = new EnhancedGrid();
		this.enhancedSimulationGrid2 = new EnhancedGrid();
//		this.enhancedSimulationGrid3 = new EnhancedGrid();
//		this.enhancedSimulationGrid4 = new EnhancedGrid();
		this.enhancedOptionSimulationGrid = new EnhancedGrid();
		
		this.ourSuggestionsStackH1 = new HStack();
		this.ourSuggestionsStackH2 = new HStack();
		
		this.ourSuggestionDS1 = new OurSuggestionDS("Type1");
		this.ourSuggestionDS2 = new OurSuggestionDS("Type2");
//		this.ourSuggestionDS3 = new OurSuggestionDS("Type3");
//		this.ourSuggestionDS4 = new OurSuggestionDS("Type4");
		this.ourOptionSuggestionDS = new OurOptionSuggestionDS();
		
		this.ourSuggestionGrid1 = getOurSuggestionGrid(ourSuggestionDS1);
		this.ourSuggestionGrid2 = getOurSuggestionGrid(ourSuggestionDS2);
//		this.ourSuggestionGrid3 = getOurSuggestionGrid(ourSuggestionDS3);
//		this.ourSuggestionGrid4 = getOurSuggestionGrid(ourSuggestionDS4);
		this.ourOptionSuggestionGrid = getOurOptionSuggestionGrid(ourOptionSuggestionDS);
		
		initialize();
	}
	
	public Layout getOurSuggestionsLayout() {
		return ourSuggestionsLayout;
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
		ourSuggestionGrid1.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		ourSuggestionGrid2.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
//		ourSuggestionGrid3.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
//		ourSuggestionGrid4.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		ourOptionSuggestionGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		ourSuggestionsLayout.setHeight(IntelliInvest.HEIGHT-200); 
		
		ourSuggestionsStackH1.setHeight(IntelliInvest.HEIGHT-200); 
		ourSuggestionsStackH2.setHeight(IntelliInvest.HEIGHT-200); 
		
		ourSuggestionGrid1.setHeight(IntelliInvest.HEIGHT-260);   
		ourSuggestionGrid2.setHeight(IntelliInvest.HEIGHT-260);   
//		ourSuggestionGrid3.setHeight((IntelliInvest.HEIGHT-260)/2);   
//		ourSuggestionGrid4.setHeight((IntelliInvest.HEIGHT-260)/2);   
		ourOptionSuggestionGrid.setHeight(IntelliInvest.HEIGHT-260);     
		
		this.enhancedSimulationGrid1.resize();
		this.enhancedSimulationGrid2.resize();
//		this.enhancedSimulationGrid3.resize();
//		this.enhancedSimulationGrid4.resize();
		this.enhancedOptionSimulationGrid.resize();
		
		ourSuggestionsStackH1.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		ourSuggestionsStackH2.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		
		ourSuggestionsLayout.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
	}
	
	@Override
	public boolean isVisible() {
		return ourSuggestionsLayout.isVisible();
	}
	public void initialize(){
		
		ourSuggestionsLayout.setShowEdges(false);   
		ourSuggestionsLayout.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		ourSuggestionsLayout.setHeight(IntelliInvest.HEIGHT-200); 
		ourSuggestionsLayout.setMembersMargin(2); 
		ourSuggestionsLayout.setPadding(2);
		
		
		ourSuggestionsStackH1.setShowEdges(false);   
		ourSuggestionsStackH1.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2); 
		ourSuggestionsStackH1.setHeight(IntelliInvest.HEIGHT-200); 
		ourSuggestionsStackH1.setMembersMargin(2);   
		ourSuggestionsStackH1.setPadding(2);
		
        Layout ourSuggestionGridLayout1 = enhancedSimulationGrid1.enhanceGrid("Intra-day Suggestions", ourSuggestionGrid1, false, "100%");
        
		Layout ourSuggestionGridLayout2 =enhancedSimulationGrid2.enhanceGrid("Long-term Suggestions", ourSuggestionGrid2, false, "100%");

		ourSuggestionsStackH1.addMember(ourSuggestionGridLayout1);
        ourSuggestionsStackH1.addMember(ourSuggestionGridLayout2);
        
		ourSuggestionsStackH2.setShowEdges(false);   
		ourSuggestionsStackH2.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2); 
		ourSuggestionsStackH2.setHeight(IntelliInvest.HEIGHT-200); 
		ourSuggestionsStackH2.setMembersMargin(2);   
		ourSuggestionsStackH2.setPadding(2);
		
//        Layout ourSuggestionGridLayout3 = enhancedSimulationGrid3.enhanceGrid("Futures Suggestions", ourSuggestionGrid3, false);
//		Layout ourSuggestionGridLayout4 =enhancedSimulationGrid4.enhanceGrid("Options Suggestions", ourSuggestionGrid4, false);
        
		Layout ourOptionSuggestionGridLayout = enhancedOptionSimulationGrid.enhanceGrid("Future & OptionSuggestions", ourOptionSuggestionGrid, false, "100%");
//        ourSuggestionsStackH2.addMember(ourSuggestionGridLayout3);
//        ourSuggestionsStackH2.addMember(ourSuggestionGridLayout4);
		
		ourSuggestionsStackH2.addMember(ourOptionSuggestionGridLayout);
        
//        enhancedSimulationGrid2.getSaveButton().setVisible(false);
//        enhancedSimulationGrid2.getPaddingForm().setWidth(50);
        IButton refreshButton = new IButton("Refresh");
        refreshButton.addClickHandler(new  ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ourSuggestionDS1.setFetch(true);
				ourSuggestionGrid1.invalidateCache();
				ourSuggestionGrid1.fetchData();
				ourSuggestionDS2.setFetch(true);
				ourSuggestionGrid2.invalidateCache();
				ourSuggestionGrid2.fetchData();
//				ourSuggestionDS3.setFetch(true);
//				ourSuggestionGrid3.invalidateCache();
//				ourSuggestionGrid3.fetchData();
//				ourSuggestionDS4.setFetch(true);
//				ourSuggestionGrid4.invalidateCache();
//				ourSuggestionGrid4.fetchData();
				ourOptionSuggestionDS.setFetch(true);
				ourOptionSuggestionGrid.invalidateCache();
				ourOptionSuggestionGrid.fetchData();
			}
		});
        
        ourSuggestionsLayout.addMember(refreshButton);
        ourSuggestionsLayout.addMember(ourSuggestionsStackH1);
//        ourSuggestionsLayout.addMember(ourSuggestionsStackH2);
//        ourSuggestionsLayout.addMember(getMarketSnapShot());
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				ourSuggestionGrid1.redraw();
				ourSuggestionGrid2.redraw();
//				ourSuggestionGrid3.redraw();
//				ourSuggestionGrid4.redraw();
				ourOptionSuggestionGrid.redraw();
			}
		};
		RefreshPriceHandler.addHandler("tradingAccountGridRefreshHandler" , refreshPriceHandler);
	}

	public ListGrid getOurSuggestionGrid(final OurSuggestionDS ourSuggestionDS){
		final ListGrid ourSuggestionGrid = new ListGrid(){
        	@Override
	    	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
	    		 if (getFieldName(colNum).equals("strategyROI")) {  
	    			 Double value = EditEventCalculator.getStrategyROI(record);
		 			 record.setAttribute("strategyROI", value);
	    			 return CellStylesUtil.getPositiveNegativeCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
	    		 }if (getFieldName(colNum).equals("signalType")) {  
	    			 return CellStylesUtil.getBuySellCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
	    		 }else{
	    			 return super.getCellCSSText(record, rowNum, colNum);
	    		 }
	    	}
        };
        ourSuggestionGrid.setCanResizeFields(false);
        ourSuggestionGrid.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);   
        ourSuggestionGrid.setHeight((IntelliInvest.HEIGHT-260));   
        ListGridField codeField = new ListGridField("code", "Code", 110); 
        codeField.setAlign(Alignment.CENTER);
        ListGridField eodPriceField = new ListGridField("eodPrice", "EOD Price", 60);
        eodPriceField.setAttribute("wrap", true);
        eodPriceField.setAlign(Alignment.CENTER);
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
        
        ListGridField currentPriceField = new ListGridField("currentPrice", "Current Price", 60);
        currentPriceField.setAttribute("wrap", true);
        currentPriceField.setAlign(Alignment.CENTER);
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
        ListGridField signalPriceField = new ListGridField("signalPrice", "Signal Price", 60);
        signalPriceField.setAttribute("wrap", true);
        signalPriceField.setAlign(Alignment.CENTER);
        signalPriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        ListGridField signalField = new ListGridField("signalType", "Signal", 60);
        ListGridField suggestionType = new ListGridField("suggestionType", "Suggestion Type", 60);
        suggestionType.setHidden(true);
        ListGridField strategyROIField = new ListGridField("strategyROI", "Strategy ROI", 75);
        strategyROIField.setAttribute("wrap", true);
        strategyROIField.setAlign(Alignment.CENTER);
        strategyROIField.setCellFormatter(CellStylesUtil.PERCENTAGE_CELL_FORMATTER);
        
        ListGridField signalDateField = new ListGridField("signalDate", "Signal Date", 60);
        signalDateField.setType(ListGridFieldType.DATE);
        signalDateField.setAlign(Alignment.CENTER);
        signalDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
//        strategyROIField.setCellFormatter(new CellFormatter() {
//			@Override
//			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
//				value = EditEventCalculator.getStrategyROI(record);
//				record.setAttribute("strategyROI", value);
//				return CellStylesUtil.PERCENTAGE_CELL_FORMATTER.format(value, record, rowNum, colNum);
//			}
//		});
        
        ourSuggestionGrid.setCanEdit(false);
        ourSuggestionGrid.setHeaderHeight(35);
        ourSuggestionGrid.setFields(codeField, eodPriceField, currentPriceField, signalPriceField, signalField, strategyROIField, suggestionType, signalDateField);
     
        if(ourSuggestionDS.getSuggestionType().equals("Type1")){
        	strategyROIField.setHidden(true);
        	signalDateField.setHidden(true);
        }
        codeField.setWidth("22%");
        eodPriceField.setWidth("12%");
        currentPriceField.setWidth("16%");
        signalPriceField.setWidth("16%");
		signalField.setWidth("9%");
		strategyROIField.setWidth("10%");
		signalDateField.setWidth("15%");
		
        ourSuggestionGrid.setDataSource(ourSuggestionDS);
        ourSuggestionGrid.setShowAllRecords(true); 
        ourSuggestionGrid.setShowHeaderMenuButton(false);
        ourSuggestionGrid.setShowFilterEditor(false);  
        ourSuggestionGrid.setCanReorderFields(false);   
        ourSuggestionGrid.setAutoFetchData(true);
        ourSuggestionGrid.setCanEdit(false);
        ourSuggestionGrid.setShowHeaderMenuButton(false);
        ourSuggestionGrid.setShowHeaderContextMenu(false);
        ourSuggestionGrid.setHeaderBarStyle("align=center");
        
        return ourSuggestionGrid;
	}
	
	public ListGrid getOurOptionSuggestionGrid(final OurOptionSuggestionDS ourOptionSuggestionDS){
		final ListGrid ourSuggestionGrid = new ListGrid();
        ourSuggestionGrid.setCanResizeFields(false);
        ourSuggestionGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        ourSuggestionGrid.setHeight((IntelliInvest.HEIGHT-260)/2);  
        ourSuggestionGrid.setHeaderHeight(35);
        ListGridField codeField = new ListGridField("code", "Code", 110); 
        codeField.setAlign(Alignment.CENTER);
        ListGridField instrumentField = new ListGridField("instrument", "Instrument", 110); 
        instrumentField.setAlign(Alignment.CENTER);
        
        ListGridField currentPriceField = new ListGridField("currentPrice", "Current Price", 100);
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
        
        ListGridField eodPriceField = new ListGridField("eodPrice", "EOD Price", 60);
        eodPriceField.setAttribute("wrap", true);
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
        
        ListGridField expiryDateField = new ListGridField("expiryDate", "ExpiryDate", 80);
        expiryDateField.setType(ListGridFieldType.DATE);
        expiryDateField.setAlign(Alignment.CENTER);
        expiryDateField.setFilterEditorProperties(new TextItem());
        expiryDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        expiryDateField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				ourSuggestionGrid.getEditedRecord(event.getRowNum()).setAttribute("date", event.getValue());
			}
		});
        
        ListGridField strikePriceField = new ListGridField("strikePrice", "Strike Price", 60);
        strikePriceField.setAttribute("wrap", true);
        strikePriceField.setAlign(Alignment.CENTER);
        strikePriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ListGridField optionTypeField = new ListGridField("optionType", "Option Type", 60);
        
        ListGridField optionPriceField = new ListGridField("optionPrice", "Option Price", 60);
        optionPriceField.setAttribute("wrap", true);
        optionPriceField.setAlign(Alignment.CENTER);
        optionPriceField.setCellFormatter(CellStylesUtil.NUMBER_CELL_FORMATTER);
        
        ourSuggestionGrid.setCanEdit(false);
        ourSuggestionGrid.setFields(codeField, instrumentField, expiryDateField, strikePriceField, optionTypeField, optionPriceField,eodPriceField, currentPriceField);
     
        codeField.setWidth("20%");
        instrumentField.setWidth("15%");
        expiryDateField.setWidth("15%");
        strikePriceField.setWidth("10%");
        optionTypeField.setWidth("10%");
        optionPriceField.setWidth("10%");
        eodPriceField.setWidth("10%");
        currentPriceField.setWidth("10%");
		
        ourSuggestionGrid.setDataSource(ourOptionSuggestionDS);
        ourSuggestionGrid.setShowAllRecords(true); 
        ourSuggestionGrid.setShowHeaderMenuButton(false);
        ourSuggestionGrid.setShowFilterEditor(false);  
        ourSuggestionGrid.setCanReorderFields(false);   
        ourSuggestionGrid.setAutoFetchData(true);
        ourSuggestionGrid.setCanEdit(false);
        ourSuggestionGrid.setShowHeaderMenuButton(false);
        ourSuggestionGrid.setShowHeaderContextMenu(false);
        ourSuggestionGrid.setHeaderBarStyle("align=center");
        
        return ourSuggestionGrid;
	}
}

