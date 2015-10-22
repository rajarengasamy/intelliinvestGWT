package com.intelliinvest.client.datasource;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.FieldType;

public class NewsDS extends DataSource {
	String stockCode;
	
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public NewsDS(String id, String dataUrl) {
		super();
		setDataFormat(DSDataFormat.XML);
        setDataProtocol(DSProtocol.GETPARAMS);
        setRecordXPath("//item");
//        if(!"".equals(id)){
//        	setID(id);
//        }
        setDataURL(dataUrl);
        DataSourceTextField titleDSField = new DataSourceTextField("title", "News", 50);   
        titleDSField.setPrimaryKey(true);
        DataSourceTextField linkDSField = new DataSourceTextField("link", "Link",150); 
        linkDSField.setType(FieldType.LINK);
        DataSourceTextField descriptionDSField = new DataSourceTextField("description", "Description",150);
        DataSourceTextField pubDateDSField = new DataSourceTextField("pubDate", "Date",150);
        
        setFields(titleDSField, linkDSField, descriptionDSField, pubDateDSField);
	}
	
}
