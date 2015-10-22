package com.intelliinvest.client.datasource;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.IntelliInvest;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.service.IntelliInvestServiceAsync;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public abstract class IntelliInvestDS extends DataSource {
	public IntelliInvestResponse intelliInvestResponse;
	ArrayList<ListGridRecord> recordsAL = new ArrayList<ListGridRecord>();

	private ArrayList<String> updateRequestIds = new ArrayList<String>();
	private ArrayList<DSResponse> updateDSResponses = new ArrayList<DSResponse>();
	private ArrayList<IntelliInvestData> updateIntelliInvestDatas = new ArrayList<IntelliInvestData>();

	private ArrayList<String> addRequestIds = new ArrayList<String>();
	private ArrayList<DSResponse> addDSResponses = new ArrayList<DSResponse>();
	private ArrayList<IntelliInvestData> addIntelliInvestDatas = new ArrayList<IntelliInvestData>();

	private ArrayList<String> removeRequestIds = new ArrayList<String>();
	private ArrayList<DSResponse> removeDSResponses = new ArrayList<DSResponse>();
	private ArrayList<IntelliInvestData> removeIntelliInvestDatas = new ArrayList<IntelliInvestData>();
	private boolean fetch = true;
	public String screenId;
	IntelliInvestServiceAsync intelliInvestService;

	public IntelliInvestDS(IntelliInvestServiceAsync intelliInvestService, String screenId) {
		this.intelliInvestService = intelliInvestService;
		this.screenId = screenId;
		setDataProtocol(DSProtocol.CLIENTCUSTOM);
		setDataFormat(DSDataFormat.CUSTOM);
		setClientOnly(true);
	}

	public ArrayList<ListGridRecord> getRecords(){
		return recordsAL;
	}
	
	
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
		case ADD:
			executeAdd(requestId, dsRequest, dsResponse);
			break;
		case UPDATE:
			executeUpdate(requestId, dsRequest, dsResponse);
			break;
		case REMOVE:
			executeRemove(requestId, dsRequest, dsResponse);
			break;
		default:
			// Operation not implemented.
			break;
		}
		return dsRequest.getData();
	}

	public boolean isFetch() {
		return fetch;
	}

	public void setFetch(boolean fetch) {
		this.fetch = fetch;
	}
	
	public String[] getJsonString() {
		return new String[]{intelliInvestResponse.getResponseData().toString()};
	}

	public void preProcessRequest(IntelliInvestRequest intelliInvestRequest) {
		if(null!=IntelliInvest.userDetailData){
			intelliInvestRequest.getRequestAttributes().put("userId", IntelliInvest.userDetailData.getUserId());
			intelliInvestRequest.getRequestAttributes().put("mail", IntelliInvest.userDetailData.getMail());
		}
	}
	
	public void postProcessRequest1(IntelliInvestResponse intelliInvestResponse) {
	}
	
	public void postProcessRequest2(IntelliInvestResponse intelliInvestResponse) {
	}
	
	public void executeFetch(final String requestId, final DSRequest dsRequest, final DSResponse dsResponse) {
		if (isFetch()) {
			IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+Constants.FETCH);
			preProcessRequest(intelliInvestRequest);
			intelliInvestService.fetch(intelliInvestRequest, new AsyncCallback<IntelliInvestResponse>() {
				public void onFailure(Throwable caught) {
					dsResponse.setStatus(RPCResponse.STATUS_FAILURE);
					processResponse(requestId, dsResponse);
				}

				public void onSuccess(IntelliInvestResponse intelliInvestResponse) {
					postProcessRequest1(intelliInvestResponse);
					processFetch(intelliInvestResponse, dsResponse);
					processResponse(requestId, dsResponse);
					postProcessRequest2(intelliInvestResponse);
				}
			});
			setFetch(false);
			updateRequestIds.clear();
			updateDSResponses.clear();
			updateIntelliInvestDatas.clear();
		} else {
			dummyProcessFetch(dsRequest, dsResponse);
		}
	}

	public void dummyProcessFetch(DSRequest dsRequest, DSResponse dsResponse) {
		dsResponse.setData(recordsAL.toArray(new ListGridRecord[0]));
		if (recordsAL.size()>0) {
			dsResponse.setStartRow(0);
			dsResponse.setEndRow(recordsAL.size()-1);
			dsResponse.setTotalRows(recordsAL.size());
		}else{
			dsResponse.setStartRow(-1);
			dsResponse.setEndRow(-1);
			dsResponse.setTotalRows(0);
		}
		processResponse(dsRequest.getRequestId(), dsResponse);
	}

	public void processFetch(IntelliInvestResponse intelliInvestResponse, DSResponse dsResponse) {
		this.intelliInvestResponse = intelliInvestResponse;
		recordsAL = new ArrayList<ListGridRecord>();
		if(null!=intelliInvestResponse.getResponseData() && intelliInvestResponse.getResponseData().size()>0){
			ArrayList<? extends IntelliInvestData> responseData = intelliInvestResponse.getResponseData();
			for(int i=0;i<responseData.size();i++){
				ListGridRecord record = copyValues(responseData.get(i));
				record.setAttribute("rowNum", i);
				recordsAL.add(record);
			}
		}
		dsResponse.setData(recordsAL.toArray(new ListGridRecord[0]));
		if (recordsAL.size()>0) {
			dsResponse.setStartRow(0);
			dsResponse.setEndRow(recordsAL.size()-1);
			dsResponse.setTotalRows(recordsAL.size());
		}else{
			dsResponse.setStartRow(-1);
			dsResponse.setEndRow(-1);
			dsResponse.setTotalRows(0);
		}
	}

	public void executeAdd(final String requestId, final DSRequest dsRequest, final DSResponse dsResponse) {
		ListGridRecord record = getEditedRecord(dsRequest);
		IntelliInvestData intelliInvestData = copyValues(record);
		IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+"Add");
		ArrayList<IntelliInvestData> requestData = new ArrayList<IntelliInvestData>();
		requestData.add(intelliInvestData);
		intelliInvestRequest.setRequestData(requestData);
		if (null != intelliInvestRequest.getRequestData().get(0)) {
			addRequestIds.add(requestId);
			addDSResponses.add(dsResponse);
			addIntelliInvestDatas.add(intelliInvestRequest.getRequestData().get(0));
		} else {
			ListGridRecord[] list = new ListGridRecord[1];
			list[0] = record;
			dsResponse.setData(list);
			processResponse(requestId, dsResponse);
		}
	}

	public void executeLocalAdd() {
		if (addIntelliInvestDatas.size() > 0) {
			for (int i = 0; i < addIntelliInvestDatas.size(); i++) {
				DSResponse dsResponse = addDSResponses.get(i);
				String requestId = addRequestIds.get(i);
				IntelliInvestData data = addIntelliInvestDatas.get(i);
				ListGridRecord[] list = new ListGridRecord[1];
				list[0] = copyValues(data);
				dsResponse.setData(list);
				processResponse(requestId, dsResponse);
			}
			addRequestIds.clear();
			addDSResponses.clear();
			addIntelliInvestDatas.clear();
		}
	}
	public void executeAdd() {
		if (addIntelliInvestDatas.size() > 0) {
			IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+"Add");
			intelliInvestRequest.setRequestData(addIntelliInvestDatas);
			preProcessRequest(intelliInvestRequest);
			intelliInvestService.add(intelliInvestRequest,
					new AsyncCallback<IntelliInvestResponse>() {
						public void onFailure(Throwable caught) {
							for (int i = 0; i < addIntelliInvestDatas.size(); i++) {
								addDSResponses.get(i).setStatus(RPCResponse.STATUS_FAILURE);
								processResponse(addRequestIds.get(i), addDSResponses.get(i));
							}

						}

						public void onSuccess(IntelliInvestResponse intelliInvestResponse) {
							postProcessRequest1(intelliInvestResponse);
							ArrayList<? extends IntelliInvestData> intelliInvestDatas = intelliInvestResponse.getResponseData();
							int recordSize = intelliInvestDatas.size();
							for (int i = 0; i < recordSize; i++) {
								DSResponse dsResponse = addDSResponses.get(i);
								String requestId = addRequestIds.get(i);
								IntelliInvestData data = intelliInvestDatas.get(i);
								ListGridRecord[] list = new ListGridRecord[1];
								list[0] = copyValues(data);
								dsResponse.setData(list);
								processResponse(requestId, dsResponse);
							}
							addRequestIds.clear();
							addDSResponses.clear();
							addIntelliInvestDatas.clear();
							intelliInvestDatas.clear();
							postProcessRequest2(intelliInvestResponse);
						}
					});
		}
	}

	public void executeRemove(final String requestId,
			final DSRequest dsRequest, final DSResponse dsResponse) {
		ListGridRecord record = getEditedRecord(dsRequest);
		IntelliInvestData intelliInvestData = copyValues(record);
		IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+ Constants.REMOVE);
		ArrayList<IntelliInvestData> requestData = new ArrayList<IntelliInvestData>();
		requestData.add(intelliInvestData);
		intelliInvestRequest.setRequestData(requestData);
		if (null != intelliInvestRequest.getRequestData().get(0)) {
			removeRequestIds.add(requestId);
			removeDSResponses.add(dsResponse);
			removeIntelliInvestDatas.add(intelliInvestRequest.getRequestData().get(0));
		} else {
			ListGridRecord[] list = new ListGridRecord[1];
			list[0] = record;
			dsResponse.setData(list);
			processResponse(requestId, dsResponse);
		}
	}

	public void executeRemove() {
		if (removeIntelliInvestDatas.size() > 0) {
			IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+ Constants.REMOVE);
			intelliInvestRequest.setRequestData(removeIntelliInvestDatas);
			preProcessRequest(intelliInvestRequest);
			intelliInvestService.remove(intelliInvestRequest,
					new AsyncCallback<IntelliInvestResponse>() {
						public void onFailure(Throwable caught) {
							for (int i = 0; i < removeIntelliInvestDatas.size(); i++) {
								removeDSResponses.get(i).setStatus(RPCResponse.STATUS_FAILURE);
								processResponse(removeRequestIds.get(i), removeDSResponses.get(i));
							}

						}

						public void onSuccess(IntelliInvestResponse intelliInvestResponse) {
							postProcessRequest1(intelliInvestResponse);
							ArrayList<? extends IntelliInvestData> intelliInvestDatas = intelliInvestResponse.getResponseData();
							int recordSize = intelliInvestDatas.size();
							for (int i = 0; i < recordSize; i++) {
								DSResponse dsResponse = removeDSResponses.get(i);
								String requestId = removeRequestIds.get(i);
								IntelliInvestData data = intelliInvestDatas.get(i);
								ListGridRecord[] list = new ListGridRecord[1];
								list[0] = copyValues(data);
								dsResponse.setData(list);
								processResponse(requestId, dsResponse);
							}
							removeRequestIds.clear();
							removeDSResponses.clear();
							removeIntelliInvestDatas.clear();
							intelliInvestDatas.clear();
							postProcessRequest2(intelliInvestResponse);
						}
					});
		}
	}

	public void executeUpdate(final String requestId, final DSRequest dsRequest, final DSResponse dsResponse) {
		ListGridRecord record = getEditedRecord(dsRequest);
		IntelliInvestData intelliInvestData = copyValues(record);
		IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+ Constants.UPDATE);
		ArrayList<IntelliInvestData> requestData = new ArrayList<IntelliInvestData>();
		requestData.add(intelliInvestData);
		intelliInvestRequest.setRequestData(requestData);
		if (null != intelliInvestRequest.getRequestData().get(0)) {
			updateRequestIds.add(requestId);
			updateDSResponses.add(dsResponse);
			updateIntelliInvestDatas.add(intelliInvestRequest.getRequestData().get(0));
		} else {
			ListGridRecord[] list = new ListGridRecord[1];
			list[0] = record;
			dsResponse.setData(list);
			processResponse(requestId, dsResponse);
		}
	}

	public void executeUpdate() {
		if (updateIntelliInvestDatas.size() > 0) {
			IntelliInvestRequest intelliInvestRequest = new IntelliInvestRequest(screenId+Constants.UPDATE);
			intelliInvestRequest.setRequestData(updateIntelliInvestDatas);
			preProcessRequest(intelliInvestRequest);
			intelliInvestService.update(intelliInvestRequest,
					new AsyncCallback<IntelliInvestResponse>() {
						public void onFailure(Throwable caught) {
							for (int i = 0; i < updateIntelliInvestDatas.size(); i++) {
								updateDSResponses.get(i).setStatus(RPCResponse.STATUS_FAILURE);
								processResponse(updateRequestIds.get(i), updateDSResponses.get(i));
							}

						}

						public void onSuccess(IntelliInvestResponse intelliInvestResponse) {
							postProcessRequest1(intelliInvestResponse);
							ArrayList<? extends IntelliInvestData> intelliInvestDatas = intelliInvestResponse.getResponseData();
							int recordSize = intelliInvestDatas.size();
							for (int i = 0; i < recordSize; i++) {
								if (updateDSResponses.size() != 0) {
									DSResponse dsResponse = updateDSResponses
											.get(i);
									String requestId = updateRequestIds.get(i);
									IntelliInvestData data = intelliInvestDatas.get(i);
									ListGridRecord[] list = new ListGridRecord[1];
									list[0] = copyValues(data);
									dsResponse.setData(list);
									processResponse(requestId, dsResponse);
								}
							}
							updateRequestIds.clear();
							updateDSResponses.clear();
							updateIntelliInvestDatas.clear();
							intelliInvestDatas.clear();
							postProcessRequest2(intelliInvestResponse);
						}
					});
		}
	}

	private ListGridRecord getEditedRecord(DSRequest request) {
		JavaScriptObject oldValues = request
				.getAttributeAsJavaScriptObject("oldValues");
		ListGridRecord newRecord = new ListGridRecord();
		JSOHelper.apply(oldValues, newRecord.getJsObj());
		JavaScriptObject data = request.getData();
		JSOHelper.apply(data, newRecord.getJsObj());
		return newRecord;
	}

	public abstract IntelliInvestData copyValues(ListGridRecord record);

	public abstract ListGridRecord copyValues(IntelliInvestData data);

}
