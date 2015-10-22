
package com.intelliinvest.client;

import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.datasource.IntelliInvestDS;
import com.intelliinvest.client.service.IntelliInvestServiceAsync;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class StockDetailsDS extends IntelliInvestDS  {
	
	public StockDetailsDS(IntelliInvestServiceAsync intelliInvestService, String screenId) {
		super(intelliInvestService, screenId);
        DataSourceTextField codeDSField = new DataSourceTextField("code", "Code", 50);   
        codeDSField.setPrimaryKey(true);
        DataSourceTextField nameDSField = new DataSourceTextField("name", "Name",150); 
        
        setFields(codeDSField, nameDSField);
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		// TODO Auto-generated method stub
		return null;
	}
	
}


