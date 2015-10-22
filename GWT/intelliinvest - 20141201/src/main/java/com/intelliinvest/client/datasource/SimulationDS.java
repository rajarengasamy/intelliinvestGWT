package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.SimulationData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SimulationDS extends IntelliInvestDS{
	
	public SimulationDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.SIMULATION);
		
		DataSourceTextField codeField = new DataSourceTextField("code", "Code");
		codeField.setPrimaryKey(true);
		DataSourceTextField nameField = new DataSourceTextField("name", "Name");
		nameField.setPrimaryKey(true);
		DataSourceFloatField eodPriceField = new DataSourceFloatField("eodPrice", "EOD Price");
		DataSourceTextField quarterlyField = new DataSourceTextField("quarterly", "Quarterly PNL");
		DataSourceTextField halfYearlyField = new DataSourceTextField("halfYearly", "Half Yearly PNL");
		DataSourceTextField yearlyField = new DataSourceTextField("yearly", "Yearly PNL");
		
		setFields(codeField, nameField, eodPriceField, quarterlyField, halfYearlyField, yearlyField);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		SimulationData simulationData = new SimulationData();
		simulationData.setCode(record.getAttribute("code"));
		if(null!=record.getAttribute("quarterly"))
			simulationData.setQuarterly(new Double(record.getAttribute("quarterly")));
		if(null!=record.getAttribute("halfYearly"))
			simulationData.setHalfYearly(new Double(record.getAttribute("halfYearly")));
		if(null!=record.getAttribute("yearly"))
			simulationData.setYearly(new Double(record.getAttribute("yearly")));
		return simulationData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		SimulationData simulationData = (SimulationData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("code", simulationData.getCode());
		record.setAttribute("quarterly", simulationData.getQuarterly());
		record.setAttribute("halfYearly", simulationData.getHalfYearly());
		record.setAttribute("yearly", simulationData.getYearly());
		return record;
	}
}
