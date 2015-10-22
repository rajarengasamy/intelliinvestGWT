package com.intelliinvest.client.datasource;

import java.util.HashMap;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ManagePortfolioDS extends IntelliInvestDS{
	
	HashMap<String, IntelliInvestData> summaryRecords = new HashMap<String, IntelliInvestData>();
	ListGrid grid;
	
	public HashMap<String, IntelliInvestData> getSummaryRecords() {
		return summaryRecords;
	}
	
	public void setGrid(ListGrid grid) {
		this.grid = grid;
	}
	
	public ManagePortfolioDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.MANAGE_PORTFOLIO);
		
		DataSourceTextField idField = new DataSourceTextField("managePortfolioId", "Id");
		idField.setPrimaryKey(true);
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		DataSourceIntegerField quantityField = new DataSourceIntegerField("quantity", "Quantity");
		DataSourceIntegerField remainingQuantityField = new DataSourceIntegerField("remainingQuantity", "Remaining Quantity");
		DataSourceFloatField priceField = new DataSourceFloatField("price", "price");
		DataSourceTextField directionField = new DataSourceTextField("direction", "Direction");
		DataSourceDateField dateField = new DataSourceDateField("date", "Date");
		directionField.setValueMap("Buy", "Sell");
		dateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		DataSourceFloatField realisedPnl = new DataSourceFloatField("realisedPnl", "Realised PNL");
		setFields(idField, codeField, quantityField, remainingQuantityField, priceField, directionField, dateField, realisedPnl);
	}
	
	@Override
	public void postProcessRequest1(IntelliInvestResponse intelliInvestResponse) {
		super.postProcessRequest1(intelliInvestResponse);
		for(String code : intelliInvestResponse.getScreenData().get("SUMMARY").keySet()){
			summaryRecords.put(code, intelliInvestResponse.getScreenData().get("SUMMARY").get(code));
		}
	}
	
	@Override
	public void postProcessRequest2(IntelliInvestResponse intelliInvestResponse) {
		super.postProcessRequest2(intelliInvestResponse);
		if(null!=intelliInvestResponse.getScreenData().get("CHANGED")){
			HashMap<String, ? extends IntelliInvestData> changedData = intelliInvestResponse.getScreenData().get("CHANGED");
			for(ListGridRecord record : grid.getRecords()){
				String id = record.getAttribute("managePortfolioId");
				if(changedData.containsKey(id)){
					record.setAttribute("realisedPnl", ((ManagePortfolioData)changedData.get(id)).getRealisedPnl());
					record.setAttribute("remainingQuantity", ((ManagePortfolioData)changedData.get(id)).getRemainingQuantity());
					int recordIndex = grid.getRecordIndex(record);
					if(-1!=recordIndex){
						grid.refreshRow(grid.getRecordIndex(record));
					}
				}
				grid.recalculateSummaries();
			}
		}
		
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		ManagePortfolioData managePortfolioData = new ManagePortfolioData();
		managePortfolioData.setId(record.getAttribute("managePortfolioId"));
		managePortfolioData.setCode(record.getAttribute("code"));
		managePortfolioData.setPrice(new Double(record.getAttribute("price")));
		managePortfolioData.setQuantity(new Integer(record.getAttribute("quantity")));
		managePortfolioData.setDirection(record.getAttribute("direction"));
		managePortfolioData.setDate(DateUtil.getDate(record.getAttribute("date")));
		return managePortfolioData;
	}
	
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		ManagePortfolioData managePortfolioData = (ManagePortfolioData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("managePortfolioId", managePortfolioData.getId());
		record.setAttribute("code", managePortfolioData.getCode());
		record.setAttribute("price", managePortfolioData.getPrice());
		record.setAttribute("quantity", managePortfolioData.getQuantity());
		record.setAttribute("remainingQuantity", managePortfolioData.getRemainingQuantity());
		record.setAttribute("direction", managePortfolioData.getDirection());
		record.setAttribute("date", managePortfolioData.getDate());
		record.setAttribute("realisedPnl", managePortfolioData.getRealisedPnl());
		return record;
	}
}
