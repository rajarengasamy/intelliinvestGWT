package com.intelliinvest.client.datasource;

import java.util.Collection;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.intelliinvest.client.data.IntelliInvestData;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class IntelliInvestDataDS extends DataSource {
	public ListGridRecord[] records;
	public String screenId;
	Collection<? extends IntelliInvestData> data;
	private boolean fetch = true;

	public IntelliInvestDataDS(Collection<? extends IntelliInvestData> data, String screenId) {
		this.data = data;
		this.screenId = screenId;
		setDataProtocol(DSProtocol.CLIENTCUSTOM);
		setDataFormat(DSDataFormat.CUSTOM);
		setClientOnly(true);
		setFieldsForDS();
	}

	public abstract void setFieldsForDS();
	
	@Override
	public Object transformRequest(DSRequest dsRequest) {
		String requestId = dsRequest.getRequestId();
		DSResponse dsResponse = new DSResponse();
		dsResponse.setAttribute("clientContext", dsRequest.getAttributeAsObject("clientContext"));
		dsResponse.setStatus(0);
		switch (dsRequest.getOperationType()) {
		case FETCH:
			executeFetch(requestId, dsRequest, dsResponse);
			break;
		default:
			break;
		}
		return dsRequest.getData();
	}

	public void executeFetch(final String requestId, final DSRequest dsRequest, final DSResponse dsResponse) {
		if(fetch){
			records = new ListGridRecord[data.size()];
			int k = 0;
			String result = data.toString();
			if (result != null) {
				JSONValue jsonValue = JSONParser.parseLenient(result);
				JSONArray jsonArray = new JSONArray();
				if ((jsonArray = jsonValue.isArray()) == null) {
					if (jsonValue != null) {
						jsonArray = new JSONArray();
						jsonArray.set(0, jsonValue);
					}
				}
				for (int j = 0; j < jsonArray.size(); j++) {
					ListGridRecord record = new ListGridRecord(jsonArray.get(j).isObject().getJavaScriptObject());
					record.setAttribute("rowNum", k);
					records[k] = record;
					k++;
				}
			}
			dsResponse.setData(records);
			dsResponse.setStartRow(0);
			dsResponse.setEndRow(records.length);
			dsResponse.setTotalRows(records.length);
			processResponse(requestId, dsResponse);
			fetch=false;
		}else{
			dsResponse.setData(records);
			dsResponse.setStartRow(0);
			dsResponse.setEndRow(records.length);
			dsResponse.setTotalRows(records.length);
			processResponse(requestId, dsResponse);
		}
	}
}
