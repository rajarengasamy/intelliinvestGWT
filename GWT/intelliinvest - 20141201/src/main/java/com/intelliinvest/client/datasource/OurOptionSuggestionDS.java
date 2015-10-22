package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.OurOptionSuggestionData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OurOptionSuggestionDS extends IntelliInvestDS{
	
	public OurOptionSuggestionDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.OUR_OPTION_SUGGESTION);
		
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		codeField.setPrimaryKey(true);
		DataSourceTextField instrumentField = new DataSourceTextField("instrument", "Instrument");
		instrumentField.setPrimaryKey(true);
		DataSourceDateField expiryDateField = new DataSourceDateField("expiryDate", "Expiry Date");
		expiryDateField.setPrimaryKey(true);
		DataSourceFloatField strikePriceField = new DataSourceFloatField("strikePrice", "Strike Price");
		DataSourceTextField optionTypeField = new DataSourceTextField("optionType", "Option Type");
		DataSourceFloatField optionPriceField = new DataSourceFloatField("optionPrice", "Option Price");
		optionPriceField.setPrimaryKey(true);
		setFields(codeField, instrumentField, expiryDateField, strikePriceField, optionTypeField, optionPriceField);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		OurOptionSuggestionData ourSuggestionData = new OurOptionSuggestionData();
		ourSuggestionData.setCode(record.getAttribute("code"));
		ourSuggestionData.setInstrument(record.getAttribute("instrument"));
		ourSuggestionData.setExpiryDate(DateUtil.getDate(record.getAttribute("expiryDate")));
		ourSuggestionData.setStrikePrice(new Double(record.getAttribute("strikePrice")));
		ourSuggestionData.setOptionType(record.getAttribute("optionType"));
		ourSuggestionData.setOptionPrice(new Double(record.getAttribute("optionPrice")));
		return ourSuggestionData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		OurOptionSuggestionData ourSuggestionData = (OurOptionSuggestionData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("code", ourSuggestionData.getCode());
		record.setAttribute("instrument", ourSuggestionData.getInstrument());
		record.setAttribute("expiryDate", ourSuggestionData.getExpiryDate());
		record.setAttribute("strikePrice", ourSuggestionData.getStrikePrice());
		record.setAttribute("optionType", ourSuggestionData.getOptionType());
		record.setAttribute("optionPrice", ourSuggestionData.getOptionPrice());
		return record;
	}
}
