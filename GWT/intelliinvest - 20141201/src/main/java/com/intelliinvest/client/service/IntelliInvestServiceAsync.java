package com.intelliinvest.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;

public interface IntelliInvestServiceAsync {
	void add(IntelliInvestRequest intelliInvestRequest,	AsyncCallback<IntelliInvestResponse> callback);
	void fetch(IntelliInvestRequest intelliInvestRequest, AsyncCallback<IntelliInvestResponse> callback);
	void remove(IntelliInvestRequest intelliInvestRequest, AsyncCallback<IntelliInvestResponse> callback);
	void update(IntelliInvestRequest intelliInvestRequest, AsyncCallback<IntelliInvestResponse> callback);
}
