
package com.intelliinvest.client.datasource;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;

public class StockDS extends DataSource {
	
	public StockDS(String id, String dataUrl) {
		super();
		setDataFormat(DSDataFormat.XML);
        setDataProtocol(DSProtocol.GETPARAMS);
        setRecordXPath("//stock");
        setID(id);
        setDataURL(dataUrl);
        DataSourceTextField stockDetailCodeDSField = new DataSourceTextField("code", "Code", 50);   
        stockDetailCodeDSField.setPrimaryKey(true);
        DataSourceTextField stockDetailFullNameDSField = new DataSourceTextField("fullName", "Full Name",150); 
        DataSourceFloatField stockDetailQuantityDSField = new DataSourceFloatField("quantity", "Quantity", 50); 
        DataSourceFloatField stockDetailPriceDSField = new DataSourceFloatField("price", "Price",70); 
        DataSourceFloatField stockDetailEODPriceDSField = new DataSourceFloatField("eodPrice", "EOD Price",80); 
        DataSourceFloatField stockDetailPnlDSField = new DataSourceFloatField("pnl", "PNL", 70);  
        DataSourceFloatField stockDetailRoiDSField = new DataSourceFloatField("roi", "ROI", 70);  
        DataSourceFloatField stockDetailSignalPriceDSField = new DataSourceFloatField("signalPrice", "Signal Price",70); 
        DataSourceTextField stockDetailSignalDSField = new DataSourceTextField("signal", "Signal", 70);
        DataSourceFloatField stockDetailStrategyPnlDSField = new DataSourceFloatField("strategyPnl", "Strategy PNL", 70);
        DataSourceFloatField stockDetail3MDSField = new DataSourceFloatField("quaterly", "Quaterly",70); 
        DataSourceFloatField stockDetail6mDSField = new DataSourceFloatField("halfYearly", "Half-Yearly", 70);
        DataSourceFloatField stockDetail12mPnlDSField = new DataSourceFloatField("yearly", "Yearly", 70);
        DataSourceTextField stockDetailGroupDSField = new DataSourceTextField("group", "Group", 70);
        
        setFields(stockDetailCodeDSField, stockDetailFullNameDSField, stockDetailQuantityDSField, stockDetailPriceDSField, stockDetailEODPriceDSField,
        					stockDetailPnlDSField, stockDetailRoiDSField, stockDetailSignalPriceDSField, stockDetailSignalDSField, stockDetailStrategyPnlDSField,
        					stockDetailGroupDSField,stockDetail3MDSField,stockDetail6mDSField,stockDetail12mPnlDSField);
        setCacheAllData(true);
	}
	
	
}

