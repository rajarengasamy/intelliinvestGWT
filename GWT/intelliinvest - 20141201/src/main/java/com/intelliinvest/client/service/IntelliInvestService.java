package com.intelliinvest.client.service;

import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.google.gwt.user.client.rpc.RemoteService;

public interface IntelliInvestService extends RemoteService{
	IntelliInvestResponse fetch(IntelliInvestRequest intelliInvestRequest);
	IntelliInvestResponse add(IntelliInvestRequest intelliInvestRequest);
	IntelliInvestResponse remove(IntelliInvestRequest intelliInvestRequest);
	IntelliInvestResponse update(IntelliInvestRequest intelliInvestRequest);
}

