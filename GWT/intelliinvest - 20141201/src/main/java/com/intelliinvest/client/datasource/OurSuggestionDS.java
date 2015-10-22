package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.OurSuggestionData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OurSuggestionDS extends IntelliInvestDS{
	
	String suggestionType = "";
	
	public String getSuggestionType() {
		return suggestionType;
	}
	
	public OurSuggestionDS(String suggestionType) {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.OUR_SUGGESTION);
		
		this.suggestionType = suggestionType;
		
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		codeField.setPrimaryKey(true);
		DataSourceTextField signalField = new DataSourceTextField("signalType", "Signal");
		DataSourceTextField suggestionTypeField = new DataSourceTextField("suggestionType", "Suggestion Type");
		DataSourceFloatField signalPriceField = new DataSourceFloatField("signalPrice", "Signal Price");
		DataSourceDateField signalDateField = new DataSourceDateField("signalDate", "Signal Date");
		setFields(codeField, signalField, signalPriceField, suggestionTypeField, signalDateField);
	}
	
	@Override
	public void preProcessRequest(IntelliInvestRequest intelliInvestRequest) {
		super.preProcessRequest(intelliInvestRequest);
		intelliInvestRequest.getRequestAttributes().put("suggestionType", suggestionType);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		OurSuggestionData ourSuggestionData = new OurSuggestionData();
		ourSuggestionData.setCode(record.getAttribute("code"));
		ourSuggestionData.setSignalType(record.getAttribute("signalType"));
		ourSuggestionData.setSuggestionType(record.getAttribute("suggestionType"));
		ourSuggestionData.setSignalPrice(new Double(record.getAttribute("signalPrice")));
		ourSuggestionData.setSignalDate(DateUtil.getDate(record.getAttribute("signalDate")));
		return ourSuggestionData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		OurSuggestionData ourSuggestionData = (OurSuggestionData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("code", ourSuggestionData.getCode());
		record.setAttribute("signalType", ourSuggestionData.getSignalType());
		record.setAttribute("suggestionType", ourSuggestionData.getSuggestionType());
		record.setAttribute("signalPrice", ourSuggestionData.getSignalPrice());
		record.setAttribute("signalDate", ourSuggestionData.getSignalDate());
		return record;
	}
}
