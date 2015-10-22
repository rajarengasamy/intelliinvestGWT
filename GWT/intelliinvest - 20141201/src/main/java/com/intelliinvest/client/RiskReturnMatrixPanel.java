
package com.intelliinvest.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.RiskReturnMatrixSummaryData;
import com.intelliinvest.client.data.StockDetailData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class RiskReturnMatrixPanel implements IntelliInvestPanel{
	Layout riskReturnMatrixLayout;
	
	HStack riskReturnMatrixStackH1;
	HStack riskReturnMatrixStackH2;
	
	static Layout riskReturnMatrix1;
	static Layout riskReturnMatrix2;
	static Layout riskReturnMatrix3;
	static Layout riskReturnMatrix4;
	
	static Layout riskStocksGridLayout;
	
	static Label selectedStocks;
	
	Boolean resize = false;
	
	public static int TILE_SIZE = 7;
		
	static Map<String, RiskReturnMatrixSummaryData> riskReturnMatrixSummaryDataMap;
	
	public RiskReturnMatrixPanel() {
		this.riskReturnMatrixLayout = new VLayout();
		
		this.riskReturnMatrixStackH1 = new HStack();
		this.riskReturnMatrixStackH2 = new HStack();
		selectedStocks = new Label("<b>Click on Matrix for details of stock </b>");
		selectedStocks.setHeight(15);
		selectedStocks.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		
		if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
			
			IntelliInvestServiceFactory.utilService.getRiskReturnSummaryData(new AsyncCallback<Map<String,RiskReturnMatrixSummaryData>>() {
				@Override
				public void onSuccess(Map<String, RiskReturnMatrixSummaryData> result) {
					riskReturnMatrixSummaryDataMap = result;
					riskReturnMatrix1 = getRiskReturnMatrix("3M");
					riskReturnMatrix2 = getRiskReturnMatrix("6M");
					riskReturnMatrix3 = getRiskReturnMatrix("9M");
					riskReturnMatrix4 = getRiskReturnMatrix("12M");
					riskStocksGridLayout = new Layout();
					initialize();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					SC.say("Error retrieving Risk Return data. Please contact admin.");
				}
			});
		}else{
			riskReturnMatrixSummaryDataMap = new HashMap<String, RiskReturnMatrixSummaryData>();
			riskReturnMatrix1 = getRiskReturnMatrix("3M");
			riskReturnMatrix2 = getRiskReturnMatrix("6M");
			riskReturnMatrix3 = getRiskReturnMatrix("9M");
			riskReturnMatrix4 = getRiskReturnMatrix("12M");
			riskStocksGridLayout = new Layout();
			initialize();
		}
		
	}
	
	public Layout getRiskReturnsMatrixLayout() {
		return riskReturnMatrixLayout;
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
		riskReturnMatrix1.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		riskReturnMatrix2.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		riskReturnMatrix3.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		riskReturnMatrix4.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		
		riskReturnMatrixLayout.setHeight(IntelliInvest.HEIGHT-200); 
		
		riskReturnMatrixStackH1.setHeight((IntelliInvest.HEIGHT-200)/2); 
		riskReturnMatrixStackH2.setHeight((IntelliInvest.HEIGHT-200)/2); 
		
		riskReturnMatrix1.setHeight((IntelliInvest.HEIGHT-260)/2);   
		riskReturnMatrix2.setHeight((IntelliInvest.HEIGHT-260)/2);   
		riskReturnMatrix3.setHeight((IntelliInvest.HEIGHT-260)/2);   
		riskReturnMatrix4.setHeight((IntelliInvest.HEIGHT-260)/2);   
		
		riskReturnMatrixStackH1.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		riskReturnMatrixStackH2.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		riskReturnMatrixLayout.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		selectedStocks.setWidth((IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2);
		
	}
	
	@Override
	public boolean isVisible() {
		return riskReturnMatrixLayout.isVisible();
	}
	
	public void initialize(){
		
		riskReturnMatrixLayout.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		riskReturnMatrixLayout.setHeight(IntelliInvest.HEIGHT-200); 
		riskReturnMatrixLayout.setMembersMargin(2); 
		riskReturnMatrixLayout.setPadding(2);
		
		
		riskReturnMatrixStackH1.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		riskReturnMatrixStackH1.setHeight((IntelliInvest.HEIGHT-200)/2); 
		riskReturnMatrixStackH1.setMembersMargin(2);   
		riskReturnMatrixStackH1.setPadding(2);
		
		riskReturnMatrixStackH1.addMember(riskReturnMatrix1);
        riskReturnMatrixStackH1.addMember(riskReturnMatrix2);
        
		riskReturnMatrixStackH2.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		riskReturnMatrixStackH2.setHeight((IntelliInvest.HEIGHT-200)/2); 
		riskReturnMatrixStackH2.setMembersMargin(2);   
		riskReturnMatrixStackH2.setPadding(2);
		
        riskReturnMatrixStackH2.addMember(riskReturnMatrix3);
        riskReturnMatrixStackH2.addMember(riskReturnMatrix4);
        
        riskStocksGridLayout.addMember(getRiskReturnStockDetails(new ArrayList<String>()));
		
        IButton refreshButton = new IButton("Refresh");
        refreshButton.addClickHandler(new  ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
					
					IntelliInvestServiceFactory.utilService.getRiskReturnSummaryData(new AsyncCallback<Map<String,RiskReturnMatrixSummaryData>>() {
						@Override
						public void onSuccess(Map<String, RiskReturnMatrixSummaryData> result) {
							
							riskReturnMatrixSummaryDataMap = result;
							
							riskReturnMatrix1.destroy();
							riskReturnMatrix2.destroy();
							riskReturnMatrix3.destroy();
							riskReturnMatrix4.destroy();
							
							riskReturnMatrix1 = getRiskReturnMatrix("3M");
							riskReturnMatrix2 = getRiskReturnMatrix("6M");
							riskReturnMatrix3 = getRiskReturnMatrix("9M");
							riskReturnMatrix4 = getRiskReturnMatrix("12M");
							
							riskReturnMatrixStackH1.addMember(riskReturnMatrix1);
					        riskReturnMatrixStackH1.addMember(riskReturnMatrix2);
					        riskReturnMatrixStackH2.addMember(riskReturnMatrix3);
					        riskReturnMatrixStackH2.addMember(riskReturnMatrix4);
					        
					        riskReturnMatrixLayout.removeMember(riskStocksGridLayout);
					        riskStocksGridLayout.destroy();
							riskStocksGridLayout = new Layout();
							riskStocksGridLayout.addMember(getRiskReturnStockDetails(new ArrayList<String>()));
							riskReturnMatrixLayout.addMember(riskStocksGridLayout);
							riskStocksGridLayout.draw();
							
							selectedStocks.setContents("<b>Click on Matrix for details of stock </b>");
							
							riskReturnMatrixLayout.markForRedraw();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							SC.say("Error retrieving Risk Return data. Please contact admin.");
						}
					});
				}
			}
		});
        
        DynamicForm form = new DynamicForm();
		form.setNumCols(8);
		form.setWidth("200");
		RadioGroupItem radioGroupItem = new RadioGroupItem("radioForTerm", "Select Term");  
        radioGroupItem.setWrap(false);
        radioGroupItem.setWrapTitle(false);
        radioGroupItem.setValueMap("Short Term", "Long Term");
        radioGroupItem.setVertical(false);
        radioGroupItem.setDefaultValue("Short Term");
        radioGroupItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(event.getValue().toString().equals("Short Term")){
					riskReturnMatrixLayout.removeMember(riskStocksGridLayout);
					riskStocksGridLayout.destroy();
					riskStocksGridLayout = new Layout();
					riskStocksGridLayout.addMember(getRiskReturnStockDetails(new ArrayList<String>()));
					riskReturnMatrixLayout.addMember(riskStocksGridLayout);
					selectedStocks.setContents("<b>Click on Matrix for details of stock </b>");
					riskStocksGridLayout.draw();
					riskReturnMatrixStackH2.hide();
					riskReturnMatrixStackH1.show();
				}else{
					riskReturnMatrixLayout.removeMember(riskStocksGridLayout);
					riskStocksGridLayout.destroy();
					riskStocksGridLayout = new Layout();
					riskStocksGridLayout.addMember(getRiskReturnStockDetails(new ArrayList<String>()));
					riskReturnMatrixLayout.addMember(riskStocksGridLayout);
					selectedStocks.setContents("<b>Click on Matrix for details of stock </b>");
					riskStocksGridLayout.draw();
					riskReturnMatrixStackH2.show();
					riskReturnMatrixStackH1.hide();
				}
			}
		});
        StaticTextItem staticTextItem = new StaticTextItem();
        staticTextItem.setShowTitle(false);
        staticTextItem.setWidth(100);
        form.setFields(radioGroupItem, staticTextItem);
        
        HStack hStack = new HStack();
        hStack.addMember(form);
        hStack.addMember(refreshButton);

        riskReturnMatrixLayout.addMember(hStack);
        riskReturnMatrixLayout.addMember(riskReturnMatrixStackH1);
        riskReturnMatrixLayout.addMember(riskReturnMatrixStackH2);
        riskReturnMatrixLayout.addMember(selectedStocks);
        riskReturnMatrixLayout.addMember(riskStocksGridLayout);
        
        riskReturnMatrixStackH2.hide();
        
	}

	public Layout getRiskReturnMatrix(String type){
		VStack vStack = new VStack();
		
		vStack.setMargin(2);
		int width = (IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0))/2;
		int height = (IntelliInvest.HEIGHT-260)/2;
		
		Label headingLabel = new Label("<b>Risk Return Matrix (" + type + ")</b>");
		headingLabel.setHeight(25);
				
		vStack.setWidth(width-30);   
		vStack.setHeight(height-45);  
		vStack.setAlign(Alignment.CENTER);
		vStack.setAlign(VerticalAlignment.CENTER);
		vStack.addMember(headingLabel);
		
		for(int i=TILE_SIZE-1; i>=0; i--){
			HStack hstack = new HStack();
			hstack.setWidth(width-30);
			hstack.setHeight((height-45)/TILE_SIZE);
			for(int j=0; j<TILE_SIZE; j++){
				final Label button = new Label();
				button.setCursor(Cursor.HAND);
				button.setID("ID" + type+i+j);
				button.setPadding(0);
				button.setMargin(0);
				button.setBorder("1px solid black");
				button.setAlign(Alignment.CENTER);
				Double noOfBuy = 0D;
				Double noOfSell = 0D;
				if(riskReturnMatrixSummaryDataMap.containsKey(type+i+j)){
					RiskReturnMatrixSummaryData riskReturnMatrixSummaryData = riskReturnMatrixSummaryDataMap.get(type+i+j);
					noOfBuy = Integer.valueOf(riskReturnMatrixSummaryData.getBuyCount()).doubleValue();
					noOfSell = Integer.valueOf(riskReturnMatrixSummaryData.getSellCount()).doubleValue();
				}
				
				Double total = noOfBuy + noOfSell;
				Integer greenPercent = 0;
				Integer redPercent = 0;
				
				if(noOfBuy!=0){
					greenPercent = Double.valueOf((noOfBuy/total)*255).intValue();
				}
				if(noOfSell!=0){
					redPercent = Double.valueOf((noOfSell/total)*255).intValue();
				}				
				String colourGreen = Integer.toHexString(greenPercent); 
				String colourRed = Integer.toHexString(redPercent); 
				if(colourGreen.length()==1){
					colourGreen = "0" + colourGreen;
				}
				if(colourRed.length()==1){
					colourRed = "0" + colourRed;
				}
				button.setContents("<b>" + total.intValue() + "</b>");
				if(noOfBuy==0 && noOfSell==0){
					button.setBackgroundColor("#FFFFFF");
				}else{
					button.setBackgroundColor("#" + colourRed+ colourGreen + "00");
				}
				button.setWidth((width-30)/TILE_SIZE);
				button.setHeight((height-45)/TILE_SIZE);
				button.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if(button.getContents().contains("<b>0</b>")){
							SC.say("No stocks to show.");
						}else{
							String id = button.getID();
							selectedStocks.setContents("<b>Stock list for " + id.replaceFirst("ID" , "").replaceFirst("M" , "M ") + "</b>");  
							List<String> stocks = RiskReturnMatrixPanel.riskReturnMatrixSummaryDataMap.get(id.replaceFirst("ID" , "")).getCodes();
							riskReturnMatrixLayout.removeMember(riskStocksGridLayout);
							riskStocksGridLayout.destroy();
							riskStocksGridLayout = new Layout();
							riskStocksGridLayout.addMember(getRiskReturnStockDetails(stocks));
							riskReturnMatrixLayout.addMember(riskStocksGridLayout);
							riskStocksGridLayout.draw();
						}
					}
				});
				hstack.addMember(button);

			}
			vStack.addMember(hstack);
		}
		
		Img riskImg = new Img("/data/images/risk.png", 160, 20);
		riskImg.setAlign(Alignment.CENTER);
		vStack.addMember(riskImg);
		HStack hStack = new HStack();
		hStack.setAlign(Alignment.CENTER);
		hStack.setAlign(VerticalAlignment.CENTER);
		Img returnsImg = new Img("/data/images/returns.png", 20, 160);
		returnsImg.setAlign(Alignment.CENTER);
		hStack.addMember(returnsImg);
		hStack.addMember(vStack);
		return hStack;
	}
	
	private ListGrid getRiskReturnStockDetails(List<String> stocks){
		final ListGrid riskReturnStockGrid = new ListGrid();
		riskReturnStockGrid.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		riskReturnStockGrid.setHeight((IntelliInvest.HEIGHT-230)/2);
		
        final DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        final DataSourceTextField nameDSField = new DataSourceTextField("name", "Name", 50); 
        List<StockDetailData> stockDetails = new ArrayList<StockDetailData>();
        for(String stock : stocks){
        	if(StockDetailStaticHolder.stockDetailsMap.containsKey(stock))
        		stockDetails.add(StockDetailStaticHolder.stockDetailsMap.get(stock));	
        }
        IntelliInvestDataDS rrDS = new IntelliInvestDataDS(stockDetails, "rr_stockdetails"){
        	@Override
        	public void setFieldsForDS() {
        		setFields(codeDSField, nameDSField);
        	}
        };
        
        ListGridField codeField = new ListGridField("code", "Code", 110); 
        codeField.setAlign(Alignment.CENTER);
        
        ListGridField nameField = new ListGridField("name", "Name", 210); 
        
        ListGridField eodPriceField = new ListGridField("eodPrice", "EOD Price", 70);
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
        
        ListGridField currentPriceField = new ListGridField("currentPrice", "Current Price", 90);
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
        
        ListGridField chartField = new ListGridField("chart", "Chart", 90);
        chartField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/intelliinvest.ico\" width=\"20\" height=\"20\" alt=\"View Chart\"></img>";
			}
		});
        chartField.setAlign(Alignment.CENTER);
        
        riskReturnStockGrid.setDataSource(rrDS);
        riskReturnStockGrid.setShowAllRecords(true); 
        riskReturnStockGrid.setShowHeaderMenuButton(false);
        riskReturnStockGrid.setShowFilterEditor(false);  
        riskReturnStockGrid.setCanReorderFields(false);   
        riskReturnStockGrid.setAutoFetchData(true);
        riskReturnStockGrid.setCanEdit(false);
        riskReturnStockGrid.setShowHeaderMenuButton(false);
        riskReturnStockGrid.setShowHeaderContextMenu(false);
        riskReturnStockGrid.setHeaderBarStyle("align=center");
        riskReturnStockGrid.setCanEdit(false);
        
        codeField.setWidth("15%");
        nameField.setWidth("40%");
        eodPriceField.setWidth("15%");
        currentPriceField.setWidth("15%");
        chartField.setWidth("15%");
        
        riskReturnStockGrid.setFields(codeField, nameField, eodPriceField, currentPriceField, chartField);
        
        riskReturnStockGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == riskReturnStockGrid.getFieldNum("chart")){
					if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
						SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "userEODChart");
					}else{
						SC.say("This is user specific chart. Please login to see chart");
					}
				}
			}
		});

        return riskReturnStockGrid;
	}
}

