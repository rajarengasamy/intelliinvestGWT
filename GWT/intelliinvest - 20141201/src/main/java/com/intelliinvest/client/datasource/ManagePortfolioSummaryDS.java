package com.intelliinvest.client.datasource;

import java.util.HashMap;
import java.util.List;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ManagePortfolioSummaryDS extends IntelliInvestDS{
	
	ListGrid grid;
	HashMap<String, List<ManagePortfolioData>> managePortfolioDetails = new HashMap<String, List<ManagePortfolioData>>();
	
	String code;
	
	public void setGrid(ListGrid grid) {
		this.grid = grid;
	}
	
	public HashMap<String, List<ManagePortfolioData>> getManagePortfolioDetails() {
		return managePortfolioDetails;
	}
	
	public ManagePortfolioSummaryDS(String code) {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.NEW_MANAGE_PORTFOLIO);
		
		this.code = code;
		
		DataSourceTextField idField = new DataSourceTextField("managePortfolioId", "Id");
		idField.setPrimaryKey(true);
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		DataSourceIntegerField quantityField = new DataSourceIntegerField("quantity", "Quantity");
		DataSourceIntegerField remainingQuantityField = new DataSourceIntegerField("remainingQuantity", "Remaining Quantity");
		DataSourceFloatField priceField = new DataSourceFloatField("price", "price");
		DataSourceTextField directionField = new DataSourceTextField("direction", "Direction");
		directionField.setValueMap("Buy", "Sell");
		DataSourceFloatField realisedPnl = new DataSourceFloatField("realisedPnl", "Realised PNL");
		DataSourceFloatField cp = new DataSourceFloatField("cp", "Change");
		DataSourceFloatField currentPrice = new DataSourceFloatField("currentPrice", "Current Price");
		DataSourceFloatField unrealisedPnl = new DataSourceFloatField("unrealisedPnl", "Unrealised PNL");
		DataSourceFloatField amount = new DataSourceFloatField("amount", "Amount");
		DataSourceFloatField totalAmount = new DataSourceFloatField("totalAmount", "Total Amount");
		DataSourceFloatField todaysPnl = new DataSourceFloatField("todaysPnl", "Today's PNL");
		
		setFields(idField, codeField, quantityField, remainingQuantityField, priceField, directionField, realisedPnl, cp, currentPrice, unrealisedPnl, amount, totalAmount, todaysPnl);
	}
	
	@Override
	public void postProcessRequest1(IntelliInvestResponse intelliInvestResponse) {
		super.postProcessRequest1(intelliInvestResponse);
		if(null!=intelliInvestResponse.getScreenData().get("SUMMARY")){
			for(IntelliInvestData intelliInvestData : intelliInvestResponse.getScreenData().get("SUMMARY").values()){
				ListGridRecord newRecord = copyValues(intelliInvestData);
				int index = grid.getRecordIndex(newRecord);
				ListGridRecord record =  grid.getRecord(index);
				copyValues(record, intelliInvestData);
				grid.refreshRow(index);
			}
			grid.recalculateGridSummary();
		}
	}
	
	@Override
	public void preProcessRequest(IntelliInvestRequest intelliInvestRequest) {
		super.preProcessRequest(intelliInvestRequest);
		intelliInvestRequest.getRequestAttributes().put("code", code);
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
		record.setAttribute("cp", managePortfolioData.getCp());
		record.setAttribute("currentPrice", managePortfolioData.getCurrentPrice());
		record.setAttribute("amount", managePortfolioData.getAmount());
		record.setAttribute("totalAmount", managePortfolioData.getTotalAmount());
		record.setAttribute("unrealisedPnl", managePortfolioData.getUnrealisedPnl());
		record.setAttribute("todaysPnl", managePortfolioData.getTodaysPnl());
		return record;
	}
	
	public ListGridRecord copyValues(ListGridRecord record, IntelliInvestData data) {
		ManagePortfolioData managePortfolioData = (ManagePortfolioData)data;
		record.setAttribute("managePortfolioId", managePortfolioData.getId());
		record.setAttribute("code", managePortfolioData.getCode());
		record.setAttribute("price", managePortfolioData.getPrice());
		record.setAttribute("quantity", managePortfolioData.getQuantity());
		record.setAttribute("remainingQuantity", managePortfolioData.getRemainingQuantity());
		record.setAttribute("direction", managePortfolioData.getDirection());
		record.setAttribute("date", managePortfolioData.getDate());
		record.setAttribute("realisedPnl", managePortfolioData.getRealisedPnl());
		record.setAttribute("cp", managePortfolioData.getCp());
		record.setAttribute("currentPrice", managePortfolioData.getCurrentPrice());
		record.setAttribute("amount", managePortfolioData.getAmount());
		record.setAttribute("totalAmount", managePortfolioData.getTotalAmount());
		record.setAttribute("unrealisedPnl", managePortfolioData.getUnrealisedPnl());
		record.setAttribute("todaysPnl", managePortfolioData.getTodaysPnl());
		return record;
	}
}
