package com.intelliinvest.client;

import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.datasource.IntelliInvestDataDS;
import com.intelliinvest.client.datasource.SimulationDS;
import com.intelliinvest.client.util.CellStylesUtil;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
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
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SimulationPanel implements IntelliInvestPanel {
	final Layout simulationLayout;
	final ListGrid simulationGrid;
	final DynamicForm simulationForm; 
	final EnhancedGrid enhancedSimulationGrid;
	Boolean resize = false;
	
	public SimulationPanel() {
		this.simulationLayout = new VLayout();
		this.simulationGrid = createSimulationGrid();
		this.simulationForm = new DynamicForm(); 
		this.enhancedSimulationGrid = new EnhancedGrid();
		initialize();
	}
	
	public Layout getSimulationLayout() {
		return simulationLayout;
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
		simulationGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		simulationGrid.setHeight(IntelliInvest.HEIGHT-242); 
		enhancedSimulationGrid.resize();
		
		simulationForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		simulationLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		simulationLayout.setHeight(IntelliInvest.HEIGHT-165);
		System.out.println("Resized simulation " + simulationGrid.getWidth());
	}
	
	@Override
	public boolean isVisible() {
		return simulationLayout.isVisible();
	}
	
	public ListGrid createSimulationGrid(){
		ListGrid simulationGrid = new ListGrid(){  
	        @Override
	    	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
	    		if (getFieldName(colNum).equals("quarterly") || getFieldName(colNum).equals("halfYearly") || getFieldName(colNum).equals("yearly")) {  
	    			 return CellStylesUtil.getPositiveNegativeWithChartCSSFormatter(getEditedRecord(rowNum), getFieldName(colNum));
	    		 }else{
	    			 return super.getCellCSSText(record, rowNum, colNum);
	    		 }
	    	}
	    };
	    return simulationGrid;
	}
    
	@Override
	public void initialize(){
		simulationLayout.setShowEdges(false);   
        simulationLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
        simulationLayout.setHeight(IntelliInvest.HEIGHT-165); 
        simulationLayout.setMembersMargin(0);   
        simulationLayout.setPadding(0);
        
        final SimulationDS simulationDS =  new SimulationDS();
        
        simulationGrid.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {
			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				simulationDS.removeData(simulationGrid.getRecord(event.getRowNum()));
			}
		});
        
        simulationGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        simulationGrid.setHeight(IntelliInvest.HEIGHT-242);   
        ListGridField removeField = new ListGridField("remove", "Remove", 15);
        removeField.setIsRemoveField(true);
        
        ListGridField codeField = new ListGridField("code", "Code", 110);  
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
        ListGridField quaterlyField = new ListGridField("quarterly", "Quaterly PNL", 100);
        quaterlyField.setType(ListGridFieldType.FLOAT);
        quaterlyField.setCellFormatter(CellStylesUtil.PERCENTAGE_CELL_FORMATTER);
        ListGridField halfYearlyField = new ListGridField("halfYearly", "Half-Yearly PNL", 100);
        halfYearlyField.setType(ListGridFieldType.FLOAT);
        halfYearlyField.setCellFormatter(CellStylesUtil.PERCENTAGE_CELL_FORMATTER);
        ListGridField yearlyField = new ListGridField("yearly", "Yearly PNL", 100);
        yearlyField.setType(ListGridFieldType.FLOAT);
        yearlyField.setCellFormatter(CellStylesUtil.PERCENTAGE_CELL_FORMATTER);
        ListGridField chartField = new ListGridField("chart", "Chart", 89);
        chartField.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<img style=\"cursor:hand;\" src=\"/data/images/intelliinvest.ico\" width=\"20\" height=\"20\" alt=\"View Chart\"></img>";
			}
		});
        chartField.setAlign(Alignment.CENTER);

        simulationGrid.setFields(removeField, codeField,  nameField, eodPriceField, currentPriceField, quaterlyField, halfYearlyField, yearlyField, chartField);
     
        removeField.setWidth("2%");
        codeField.setWidth("13%");
        nameField.setWidth("19%");
        eodPriceField.setWidth("11%");
        currentPriceField.setWidth("11%");
        quaterlyField.setWidth("13%");
        halfYearlyField.setWidth("13%");
        yearlyField.setWidth("13%");
		chartField.setWidth("5%");
		
        simulationGrid.setDataSource(simulationDS);
        simulationGrid.setShowAllRecords(true); 
        simulationGrid.setShowHeaderMenuButton(false);
        simulationGrid.setShowFilterEditor(true);  
        simulationGrid.setCanReorderFields(false);   
        simulationGrid.setAutoFetchData(true);
        simulationGrid.setCanEdit(false);
        simulationGrid.setAutoSaveEdits(false);
        simulationGrid.setSaveLocally(true);
        simulationGrid.setShowHeaderMenuButton(false);
        simulationGrid.setShowHeaderContextMenu(false);
        simulationGrid.setCanResizeFields(false);
        simulationGrid.setCanRemoveRecords(true);
        
//        HStack addStack = new HStack();
//        addStack.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
//        addStack.setHeight(30);
//        addStack.setPadding(0);
        
        simulationForm.setIsGroup(true);  
        simulationForm.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        simulationForm.setHeight("30");
        simulationForm.setPadding(0);
        
        final ComboBoxItem stockComboBox = new ComboBoxItem("code", "Select");   
        stockComboBox.setWidth("110");
        final DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        final DataSourceTextField nameDSField = new DataSourceTextField("name", "Name",150);
        
        stockComboBox.setOptionDataSource(new IntelliInvestDataDS(StockDetailStaticHolder.stockDetailsMap.values(), "simulationPanelStockCB"){
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

        StaticTextItem nameItem = new StaticTextItem("name", "Name");
        nameItem.setRequired(true);
        nameItem.setWrap(false);
        nameItem.setWidth(150);
        
        StaticTextItem priceItem = new StaticTextItem("price", "Price");
        priceItem.setRequired(true);
        priceItem.setWrap(true);
        priceItem.setWidth(80);
        
        StaticTextItem eodPriceItem = new StaticTextItem("eodPrice", " EODPrice");
        eodPriceItem.setRequired(true);
        eodPriceItem.setWrap(true);
        eodPriceItem.setWidth(80);
        
        StaticTextItem dummyTextItem = new StaticTextItem("dummy");
        dummyTextItem.setShowTitle(false);
        dummyTextItem.setValue("  ");
        dummyTextItem.setWidth(5);
        
        ButtonItem addButton = new ButtonItem("Add");
        addButton.setWidth("50");
        addButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(!simulationForm.validate()){
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
				
				if( simulationGrid.getTotalRows()>=allowedRowCount){
             		SC.say("Your account supports simulation of max 10 stocks. Please contact support for activating a different plan.");
             		return;
             	}else{
             		for(ListGridRecord record : simulationGrid.getRecords()){
             			if(record.getAttribute("code").equals(simulationForm.getValue("code"))){
             				SC.say("Stock you are trying to add already exists.");
             				return;
             			}
             		}
 	                Record record = simulationForm.getValuesAsRecord();  
 	                simulationDS.addData(record);
 	               simulationDS.executeAdd();
             	}
				
			}
		});
        addButton.setEndRow(true);
        addButton.setStartRow(false);
        
        simulationForm.setGroupTitle("Add");
        simulationForm.setWrapItemTitles(true);
        simulationForm.setNumCols(16);  
        simulationForm.setFields(stockComboBox, nameItem, priceItem, eodPriceItem, dummyTextItem, addButton);
        
        stockComboBox.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			@Override
			public void onChanged(com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				if(null!=stockComboBox.getSelectedRecord()){
					if(null!=stockComboBox.getSelectedRecord().getAttribute("code") && !stockComboBox.getSelectedRecord().getAttribute("code").equals("")){
						simulationForm.getField("code").setValue(stockComboBox.getSelectedRecord().getAttribute("code"));
						simulationForm.getField("name").setValue(stockComboBox.getSelectedRecord().getAttribute("name"));
						simulationForm.getField("price").setValue(StockDetailStaticHolder.getCurrentPrice(stockComboBox.getSelectedRecord().getAttribute("code")));
						simulationForm.getField("eodPrice").setValue(StockDetailStaticHolder.getEODPrice(stockComboBox.getSelectedRecord().getAttribute("code")));
					}
				}
			}
        });
        
//        addStack.addMember(simulationForm);
        simulationGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				int colNum = event.getColNum();
				ListGridRecord record = event.getRecord();
				if(colNum == simulationGrid.getFieldNum("quarterly") || colNum == simulationGrid.getFieldNum("halfYearly") 
						|| colNum == simulationGrid.getFieldNum("yearly") || colNum == simulationGrid.getFieldNum("chart")){
					if(null==IntelliInvest.userDetailData || null==IntelliInvest.userDetailData.getUserId()){
						SC.say("This is user specific chart. Please login to see chart");
						return;
					}
				}
				if(colNum == simulationGrid.getFieldNum("quarterly")){
					SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "eodSignalChartQuaterly");
				}else if(colNum == simulationGrid.getFieldNum("halfYearly")){
					SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "eodSignalChartHalfYearly");
				}else if(colNum == simulationGrid.getFieldNum("yearly")){
					SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "eodSignalChartYearly");
				}if(colNum == simulationGrid.getFieldNum("chart")){
					SimulationChartWindow.showSimulationWindow(record.getAttribute("code"), "userEODSignalChart");
				}
			}
		});
        
        simulationLayout.addMember(simulationForm);
        
        simulationLayout.addMember(enhancedSimulationGrid.enhanceGrid("Simulations", simulationGrid, true));
        enhancedSimulationGrid.getSaveButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				simulationGrid.saveAllEdits();
				simulationDS.executeAdd();
				simulationDS.executeUpdate();
				simulationDS.executeRemove();
			}
		});
        enhancedSimulationGrid.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData){
					SC.say("Please login to save changes");
				}else{
					simulationGrid.saveAllEdits();
					simulationDS.executeAdd();
					simulationDS.executeUpdate();
					simulationDS.executeRemove();
				}
			}
		});
        
        enhancedSimulationGrid.getRefreshButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(null==IntelliInvest.userDetailData || simulationGrid.getAllEditRows().length>0){
						SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if(value){
									simulationDS.setFetch(true);
									simulationGrid.invalidateCache();
									simulationGrid.fetchData();
								}else{
									return;
								}
							}
						});
					}else{
						simulationDS.setFetch(true);
						simulationGrid.invalidateCache();
						simulationGrid.fetchData();
					}
				}
			});
        
        RefreshPriceHandler refreshPriceHandler = new RefreshPriceHandler() {
			@Override
			public void onRefreshPrice() {
				simulationGrid.redraw();
			}
		};
		RefreshPriceHandler.addHandler("simulationGridRefreshHandler" , refreshPriceHandler);
		
	}

}

