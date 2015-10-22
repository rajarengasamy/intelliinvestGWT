package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.TradingAccountData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TradingAccountDS extends IntelliInvestDS{
	
	public TradingAccountDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.TRADING_ACCOUNT);
		
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		codeField.setPrimaryKey(true);
		DataSourceFloatField signalPriceField = new DataSourceFloatField("signalPrice", "Signal Price");
		DataSourceTextField yesterdaySignalField = new DataSourceTextField("yesterdaySignalType", "Yesterday Signal");
		DataSourceTextField signalField = new DataSourceTextField("signalType", "Signal");
		DataSourceDateField signalDateField = new DataSourceDateField("signalDate", "Signal Date");
		setFields(codeField, yesterdaySignalField, signalField, signalPriceField, signalDateField);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		TradingAccountData tradingAccountData = new TradingAccountData();
		tradingAccountData.setCode(record.getAttribute("code"));
		if(null!=record.getAttribute("signalPrice")){
			tradingAccountData.setSignalPrice(new Double(record.getAttribute("signalPrice")));
		}
		if(null!=record.getAttribute("signalType")){
			tradingAccountData.setSignalType(record.getAttribute("signalType"));
		}
		if(null!=record.getAttribute("yesterdaySignalType")){
			tradingAccountData.setSignalType(record.getAttribute("yesterdaySignalType"));
		}
		if(null!=record.getAttribute("signalDate")){
			tradingAccountData.setSignalDate(DateUtil.getDate(record.getAttribute("signalDate")));
		}
		return tradingAccountData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		TradingAccountData tradingAccountData = (TradingAccountData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("code", tradingAccountData.getCode());
		record.setAttribute("signalPrice", tradingAccountData.getSignalPrice());
		record.setAttribute("yesterdaySignalType", tradingAccountData.getYesterdaySignalType());
		record.setAttribute("signalType", tradingAccountData.getSignalType());
		record.setAttribute("signalDate", tradingAccountData.getSignalDate());
		return record;
	}
}
