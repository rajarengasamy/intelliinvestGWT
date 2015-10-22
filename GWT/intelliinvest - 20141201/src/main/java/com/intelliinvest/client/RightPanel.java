package com.intelliinvest.client;

import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.StockPriceData;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.datasource.NewsDS;
import com.intelliinvest.client.util.ConverterUtils;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;

public class RightPanel implements IntelliInvestPanel{
	final SectionStack sectionStack;
	final VStack newsCanvas;
	final HTMLPane indexDetails;
	final HStack logoDetails;
	Boolean resize = false;
	
	public RightPanel() {
		sectionStack = new SectionStack();
		this.newsCanvas = new VStack();
		indexDetails = new HTMLPane();
		logoDetails = new HStack();
		initialize();
		ChartWindow.initialize();
	}
	
	public Layout getRightLayout() {
		return sectionStack;
	}
	
	static Boolean mouseAboveGrid = false;
	static Integer newsGridCenter = 0;
	
	@Override
	public boolean isVisible() {
		return sectionStack.isVisible();
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
//		rightPanel.setHeight(IntelliInvest.HEIGHT-120);   
		newsCanvas.setHeight(IntelliInvest.HEIGHT-420);
		sectionStack.setHeight(IntelliInvest.HEIGHT-120);
	}
	
	public void initialize(){
		
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);   
        sectionStack.setWidth(198);   
        sectionStack.setHeight(IntelliInvest.HEIGHT-120);  
        sectionStack.setCanResizeSections(false);
        sectionStack.setBorder("1px solid #ababab");
        
		DynamicForm form = new DynamicForm();   
        form.setWidth("200");   
        form.setHeight("25");
        form.setNumCols(4);
        final ComboBoxItem stockComboBox = new ComboBoxItem("stockComboBox");   
        stockComboBox.setShowTitle(false); 
        stockComboBox.setWidth("100");
        
        final DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        final DataSourceTextField nameDSField = new DataSourceTextField("name", "Name",150); 
        
        stockComboBox.setOptionDataSource(new IntelliInvestDataDS(StockDetailStaticHolder.stockDetailsMap.values(), "rightPanel_allStockGrid"){
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
        stockComboBox.setValue("INFY");
        
        ListGridField codeField = new ListGridField("code", 100);  
        ListGridField fullNameField = new ListGridField("name", 150);  
        stockComboBox.setPickListFields(codeField, fullNameField);
        stockComboBox.setPickListWidth(270);
        stockComboBox.setPickListHeight(400);
        stockComboBox.setTextMatchStyle(TextMatchStyle.SUBSTRING);
        
        final TextItem priceTextItem = new TextItem();
        priceTextItem.setWidth("100");
        priceTextItem.setDisabled(true);
        priceTextItem.setShowTitle(false);
        priceTextItem.setCellStyle("15 px");
		priceTextItem.setValue(StockDetailStaticHolder.getCurrentPrice("INFY"));
		
		form.setFields(stockComboBox, priceTextItem);
		
        final VStack chartCanvas = new VStack();
        chartCanvas.setWidth(198);
        chartCanvas.setHeight(100);
        chartCanvas.setBorder("1 px");
        final ChartClickHandler chartClickHandler = new ChartClickHandler();
        
        final Img img = new Img("/intelliinvest/stockchart.chart?chartType=1DS&stockCode=INFY", 198, 100);
        img.setBorder("1px solid #ababab");
        img.setPrompt("Click to see more charts");
        img.setHoverWrap(false);
        img.setPadding(0);
        img.addClickHandler(chartClickHandler);
        img.setCursor(Cursor.HAND);
        chartClickHandler.setStockCode("INFY");
        chartCanvas.addMember(img);
        
        SectionStackSection chartSection = new SectionStackSection("Stock details");
        chartSection.setExpanded(true);  
        chartSection.setCanCollapse(false);
        chartSection.addItem(form); 
        chartSection.addItem(chartCanvas);  
        
	    newsCanvas.setWidth(198);
	    newsCanvas.setHeight(IntelliInvest.HEIGHT-420);
	    newsCanvas.setMargin(1);
	    newsCanvas.addMember(getNewsGrid("INFY"));
	    newsCanvas.setCursor(Cursor.HAND);
	    stockComboBox.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(null!=stockComboBox.getSelectedRecord()){
					if(null!=stockComboBox.getSelectedRecord().getAttribute("code") && !stockComboBox.getSelectedRecord().getAttribute("code").equals("")){
						final String stockCode = stockComboBox.getSelectedRecord().getAttribute("code");
						final Double price = StockDetailStaticHolder.getCurrentPrice(stockCode);
						priceTextItem.setValue(price);
						img.setSrc("/intelliinvest/stockchart.chart?chartType=1DS&stockCode="+ stockCode);
						chartClickHandler.setStockCode(stockCode);
//						img.redraw();
						newsCanvas.removeMember(newsCanvas.getMember(0));
						ListGrid newsGrid = getNewsGrid(stockCode);
						newsCanvas.addMember(newsGrid);
//						newsCanvas.redraw();
					}
				}
			}
		});
	    
	    SectionStackSection newsSection = new SectionStackSection("Stock News");
        newsSection.setExpanded(true);  
        newsSection.setCanCollapse(false);
        newsSection.addItem(newsCanvas);  
        
        
        SectionStackSection indexSection = new SectionStackSection("Index");
        indexSection.setExpanded(true);  
        indexSection.setCanCollapse(false);
        
        setIndexDetails();
        setLogoDetails();
        
        VStack vStack = new VStack();
        vStack.setHeight(100);
        vStack.setWidth(198);
        vStack.addMember(indexDetails);
        vStack.addMember(logoDetails);
        
        indexSection.addItem(vStack);  
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				indexDetails.setContents(getIndexPrices()); 
			}
		};
		RefreshPriceHandler.addHandler("rightPanelRefreshHandler" , refreshPriceHandler);
		
		sectionStack.addSection(chartSection);
		sectionStack.addSection(newsSection);
		sectionStack.addSection(indexSection);
	}
	
	private ListGrid getNewsGrid(String stockCode){
		final ListGrid newsGrid = new ListGrid();
        newsGrid.setWidth(198);  
        newsGrid.setHeight("100%");
        ListGridField newsHeading = new ListGridField("title", "News");  
        newsGrid.setFixedRecordHeights(false);  
        NewsDS newsDS = new NewsDS("" , "/intelliinvest/stocknews.ns?stockCode=" + stockCode);
        newsGrid.setDataSource(newsDS);  
        newsGrid.setWrapCells(true);
        newsGrid.setAutoFetchData(true);
        newsGrid.setCanHover(true);
        newsGrid.setHoverWrap(true);
        newsGrid.setHoverWidth(250);
        newsGrid.setShowHeaderMenuButton(false);
        newsGrid.setShowHeaderContextMenu(false);
        newsGrid.setShowHeader(false);

        if("".equals(stockCode)){
        	newsGrid.setEmptyMessage("Display news related to selected stock above");
        }else{
        	newsGrid.setEmptyMessage("No news found for stock " + stockCode);
        }
        
        newsGrid.setHoverCustomizer(new HoverCustomizer() {
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
            	String description = record.getAttribute("description");
           	 	description = (null==description || "".equals(description))?"Not available" : description;
           	 	String pubDate = record.getAttribute("pubDate");
           	 	pubDate = (null==pubDate || "".equals(pubDate) || "null".equals(pubDate))?"Not available" : pubDate;
           	 	String hover = "<b>Description:</b><br>" + description + "<br><b>Published Date:</b><br>" + pubDate;
                return hover;
            }
        });
        newsGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				ListGridRecord record = event.getRecord();
				com.google.gwt.user.client.Window.open(record.getAttribute("link"),"_blank",null);
			}
		});
        newsGrid.setFields(newsHeading);  
		
		newsGrid.setCursor(Cursor.HAND);
        return newsGrid;
	}
	
	private void setIndexDetails(){
		indexDetails.setWidth("198");
		indexDetails.setHeight("65");
		indexDetails.setAlign(Alignment.CENTER);
	    indexDetails.setOverflow(Overflow.HIDDEN);
	    indexDetails.setContents(getIndexPrices()); 
	}
	
	private void setLogoDetails(){
		logoDetails.setWidth("198");
		logoDetails.setHeight("35");
//		logoDetails.setAlign(Alignment.CENTER);
		logoDetails.setAlign(VerticalAlignment.CENTER);
		logoDetails.setOverflow(Overflow.HIDDEN);
		logoDetails.setBorder("1px solid gray");
		logoDetails.setMembersMargin(15);
		logoDetails.setPadding(7);
	    ImgButton facebookImg = new ImgButton();
	    facebookImg.setWidth(20);
	    facebookImg.setHeight(20);
	    facebookImg.setSrc("/data/images/facebook.png");
	    facebookImg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.Window.open("https://www.facebook.com/IntelliInvestEmpoweringInvestors","_blank",null);
			}
		});
	    logoDetails.addMember(facebookImg);
	    
	    ImgButton linkedInImg = new ImgButton();
	    linkedInImg.setWidth(20);
	    linkedInImg.setHeight(20);
	    linkedInImg.setSrc("/data/images/linkedin.png");
	    linkedInImg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.Window.open("http://www.linkedin.com/pub/intelliinvest-empowering-investor/93/282/820","_blank",null);
			}
		});
	    logoDetails.addMember(linkedInImg);
	    
	}

	private String getIndexPrices(){
		String value = "<table style=\"position:center;width:197px;height:65px;\"><tbody>"
				+ "<th style=\"\"></th>";
		for(int i =0; i<StockDetailStaticHolder.worldStockPriceMap.values().size(); i++){
			StockPriceData stockPriceData = StockDetailStaticHolder.worldStockPriceMap.values().toArray(new StockPriceData[0])[i];
			String code = stockPriceData.getCode();
			Double price = stockPriceData.getPrice();
			Double cp = stockPriceData.getCp();
			if(price!=0d && null!=code){
				if(cp>0){
					value = value + "<tr><td style=\"width:197px;height:65px;text-align:center;position:center;font-weight:bold;color:green;font-size:12px;white-space: nowrap;\"><br>"+ code + " " + ConverterUtils.NumberToString(price,2) + "  &uarr; "  + ConverterUtils.NumberToString( Math.abs( price - (price/( 1 + (cp/100) )) ) ,2)  + " <br><br></td></tr>";
				}else if(cp==0){
					value = value + "<tr><td style=\"width:197px;height:65px;text-align:center;position:center;font-weight:bold;color:green;font-size:12px;white-space: nowrap;\"><br>"+ code + " " + ConverterUtils.NumberToString(price,2) + "  &uarr; 0 <br><br></td></tr>";
				}else{
					value = value + "<tr><td style=\"width:197px;height:65px;text-align:center;position:center;font-weight:bold;color:red;font-size:12px;white-space: nowrap;\"><br>"+ code + " " + ConverterUtils.NumberToString(price,2) + "  &darr; "  + ConverterUtils.NumberToString( Math.abs( price - (price/( 1 + (cp/100) )) ) ,2) + " <br><br></td></tr>";
				}
				
			}
		}
		value = value + "</tbody></table>";
		value = "<marquee direction=\"up\" scrollamount=3; onmouseover=this.stop(); onmouseout=this.start();>" + value + "</marquee>";
		return value;
	}
}
class ChartClickHandler implements ClickHandler{
	String stockCode = "";
	
	public ChartClickHandler() {
	}
	@Override
	public void onClick(ClickEvent event) {
		if(null==stockCode || "".equals(stockCode)){
			SC.say("Please select a stock code");
			return;
		}
		ChartWindow.stockCode = stockCode;
		ChartWindow.chartType = "1D";
		ChartWindow.refresh();
		ChartWindow.getChartWindow().setTitle("Charts - " + stockCode);
		ChartWindow.getChartWindow().animateShow(AnimationEffect.WIPE);
		
	}
	
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
}